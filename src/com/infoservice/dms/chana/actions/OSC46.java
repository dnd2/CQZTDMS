package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.util.CustomerUtil;
import com.infoservice.de.DEException;
import com.infoservice.de.DEMessage;
import com.infoservice.de.DEService;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.CamGrouplVO;
import com.infoservice.dms.chana.vo.CampaignVO;
import com.infoservice.dms.chana.vo.VehicleStorageVO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

public class OSC46 {
	private Logger logger = Logger.getLogger(OSC46.class);
	private DeCommonDao commonDao = DeCommonDao.getInstance();
	private int flag = 0;
	public int sendData(Long camId){
		List<String> dmsIds = getSendDealers(camId);
		//不存在对应下端经销商
		if(dmsIds==null || dmsIds.size()==0){			
			this.flag = 0;
			return flag;
		}
		try {
			for(String id:dmsIds){
				//取出活动信息
				List<CampaignVO> cams = getCampaignInfos(camId,id);
				//取出车型明细信息
				LinkedList<CamGrouplVO> vgs = getVehicleModel(camId);
				cams.get(0).setCamGroupVoList(vgs);
				DeUtility de = new DeUtility();
				HashMap<String, Serializable> body = DEUtil.assembleBody(cams);
				String dmsCode = commonDao.getDmsDlrCodeNew(Long.valueOf(id)).get("DMS_CODE").toString();
//				de.sendAMsgReId("DRC46", dmsCode, body); 
				flag = 1;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 获取活动车型信息
	 * @param camId
	 * @return
	 */
	private LinkedList<CamGrouplVO> getVehicleModel(Long camId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT B.GROUP_CODE, B.GROUP_NAME, B.TREE_CODE, B.GROUP_LEVEL        \n");
		sql.append("  FROM TT_CAMPAIGN_GROUP_R A, TM_VHCL_MATERIAL_GROUP B               \n");
		sql.append(" WHERE A.CAMPAIGN_MODEL = B.GROUP_ID                                 \n");
		sql.append("   AND A.CAMPAIGN_ID = "+camId+"                             		 \n");
		List<Map<String,Object>> rs = commonDao.pageQuery(sql.toString(), null, commonDao.getFunName());
		LinkedList<CamGrouplVO> ms = new LinkedList<CamGrouplVO>();
		for(int i=0;i<rs.size();i++){
			Map<String,Object> r = rs.get(i);
			CamGrouplVO vo = new CamGrouplVO();
			vo.setGroupCode(r.get("GROUP_CODE")==null?"":r.get("GROUP_CODE").toString());
			vo.setGroupName(r.get("GROUP_NAME")==null?"":r.get("GROUP_NAME").toString());
			vo.setGroupLevel(r.get("GROUP_LEVEL")==null?0:Integer.valueOf(r.get("GROUP_LEVEL").toString()));
			vo.setTreeCode(r.get("TREE_CODE")==null?"":r.get("TREE_CODE").toString());
			ms.add(vo);
		}
		return ms;
	}
	/**
	 * 查询可下发经销商
	 * @param camId
	 * @return
	 */
	private List<String> getSendDealers(Long camId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT A.DEALER_ID, B.DEALER_CODE            \n");
		sql.append("  FROM TT_CAMPAIGN_EXECUTE A,                \n");
		sql.append("       TM_DEALER           B,                \n");
		sql.append("       TM_COMPANY          C,                \n");
		sql.append("       TI_DEALER_RELATION  D                 \n");
		sql.append(" WHERE A.DEALER_ID = B.DEALER_ID             \n");
		sql.append("   AND B.COMPANY_ID = C.COMPANY_ID           \n");
		sql.append("   AND C.COMPANY_CODE = D.DCS_CODE           \n");
		sql.append("   AND D.STATUS = 10011001                   \n");
		sql.append("   AND A.CHECK_STATUS = 11261006             \n");
		sql.append("   AND A.CAMPAIGN_ID = "+camId+"      		 \n");
		List<Map<String,Object>> rs = commonDao.pageQuery(sql.toString(), null, commonDao.getFunName());
		List<String> dealerIds = new ArrayList<String>();
		for(int i=0;i<rs.size();i++){
			dealerIds.add(rs.get(i).get("DEALER_ID").toString());
		}
		return dealerIds;
	}
	/**
	 * 查询活动相关信息
	 * @param camId
	 * @param id 
	 * @return
	 * @throws Exception 
	 */
	private List<CampaignVO> getCampaignInfos(Long camId, String id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT A.CAMPAIGN_NO   CAMPAIGN_CODE,                                      \n");
		sql.append("       A.CAMPAIGN_NAME,                                                    \n");
		sql.append("       A.START_DATE    START_TIME,                                         \n");
		sql.append("       A.END_DATE      END_TIME,                                           \n");
		sql.append("       A.CREATE_DATE   APPLY_TIME,                                         \n");
		sql.append("       B.PLAN_COST     CAMPAIGN_PRE_COST                                   \n");
		sql.append("  FROM TT_CAMPAIGN A, TT_CAM_CAMPAIGN_COST B, TT_CAMPAIGN_EXECUTE C        \n");
		sql.append(" WHERE A.CAMPAIGN_ID = B.CAMPAIGN_ID                                       \n");
		sql.append("   AND B.EXECUTE_ID = C.EXECUTE_ID                                         \n");
		sql.append("   AND A.CAMPAIGN_ID = "+camId+"                     		               \n");
		sql.append("   AND C.DEALER_ID = "+id+"                              			       \n");
		List<Map<String,Object>> rs = commonDao.pageQuery(sql.toString(), null, commonDao.getFunName());
		List<CampaignVO> cams = new LinkedList<CampaignVO>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for(int i=0;i<rs.size();i++){
			Map<String,Object> r = rs.get(i);
			CampaignVO vo = new CampaignVO();
			vo.setDownTimestamp(new Date());
			vo.setIsValid(Constant.STATUS_ENABLE);
			vo.setCampaignCode(r.get("CAMPAIGN_CODE")==null?"":r.get("CAMPAIGN_CODE").toString());
			vo.setCampaignName(r.get("CAMPAIGN_NAME")==null?"":r.get("CAMPAIGN_NAME").toString());
			vo.setCampaignPreCost(r.get("CAMPAIGN_PRE_COST")==null?0l:Double.valueOf(r.get("CAMPAIGN_PRE_COST").toString()));
			vo.setStartTime(r.get("START_TIME")==null?null:df.parse(r.get("START_TIME").toString()));
			vo.setEndTime(r.get("END_TIME")==null?null:df.parse(r.get("END_TIME").toString()));
			vo.setApplyTime(r.get("APPLY_TIME")==null?null:df.parse(r.get("APPLY_TIME").toString()));
			cams.add(vo);
		}
		return cams;
	}
	

}
