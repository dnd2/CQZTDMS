package com.infoservice.dms.chana.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.dao.DeMaterialGroupDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.MaterialGroupVO;

/**
 * @Title: DeMaterialGroup.java
 *
 * @Description:物料组下发接口
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-23
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class OSA11 {
	private static Logger logger = Logger.getLogger(OSA11.class);
	public DeMaterialGroupDao dao = DeMaterialGroupDao.getInstance();
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	
	public void execute(TmVhclMaterialGroupPO po) throws Exception {
		DeUtility de = new DeUtility();
		logger.info("====物料组下发开始==== groupCode " + po.getGroupCode() + ", groupLevel = " + po.getGroupLevel());
		try {
			Map<String, Serializable> body = new HashMap<String, Serializable>();
			MaterialGroupVO vo = new MaterialGroupVO();
			if (po.getGroupLevel()!= 1) {//根据物料组级别判断
				List<MaterialGroupVO> list = dao.getMaterialGroup(po);
				if (list.size() > 0) {
					if (po.getGroupLevel()==2) { //车系
						vo.setBrandCode(list.get(0).getBrandCode());
						vo.setBrandName(list.get(0).getBrandName());
						vo.setSeriesCode(po.getGroupCode());
						vo.setSeriesName(po.getGroupName());
					} else if (po.getGroupLevel()==3) { //车型
						vo.setBrandCode(list.get(1).getBrandCode());
						vo.setBrandName(list.get(1).getBrandName());
						vo.setSeriesCode(list.get(0).getBrandCode());
						vo.setSeriesName(list.get(0).getBrandName());
						vo.setModelCode(po.getGroupCode());
						vo.setModelName(po.getGroupName()); 
					} else if (po.getGroupLevel()==4) { //配置
						vo.setBrandCode(list.get(2).getBrandCode());
						vo.setBrandName(list.get(2).getBrandName());
						vo.setSeriesCode(list.get(1).getBrandCode());
						vo.setSeriesName(list.get(1).getBrandName());
						vo.setModelCode(list.get(0).getBrandCode());
						vo.setModelName(list.get(0).getBrandName());
						vo.setConfigCode(po.getGroupCode());
						vo.setConfigName(po.getGroupName());
					} else {
						throw new IllegalArgumentException("Unknown group level == " + po.getGroupLevel());
					}
				}
			} else { //品牌
				vo.setBrandCode(po.getGroupCode());
				vo.setBrandName(po.getGroupName());
			}
			vo.setDownTimestamp(new Date());
			vo.setIsValid(po.getStatus());
			body.put("body", vo);
			de.sendAllMsg("DRA11", body);
			logger.info("====物料组下发结束==== 下发了 " + vo.toString());
		} catch(Exception e) {
			logger.error("===物料组下发失败" + po.getGroupCode(), e);
		}
	}
	
	public List<String> sendData(List<String> dealerCodes) {
		logger.info("====物料组下发开始==== " + dealerCodes.size());
		DeUtility de = new DeUtility();
		try{
			List<MaterialGroupVO> list = dao.getMaterialGroup();
			HashMap<String, Serializable> body = DEUtil.assembleBody(list);
			List<String> dmsCodes = new ArrayList<String>();
			List<String> errCodes = new ArrayList<String>();
			for (String dealerCode : dealerCodes) {
				try {
					Map<String, Object> dmsDealer = deCommonDao.getDmsDealerCode(dealerCode);
					//可下发的经销商列表
					dmsCodes.add(dmsDealer.get("DMS_CODE").toString());
				} catch (Exception e) {
					logger.error("Cann't send to " + dealerCode, e);
					errCodes.add(dealerCode);
				}
			}
			//可下发的经销商列表
			if (dmsCodes.size() > 0) {
				de.sendMsg("DRA11", dmsCodes, body);
				logger.info("====物料组下发结束====,下发了(" + body.size() + ")条数据");
			}
			return errCodes;
		} catch(Exception e){
			logger.error("物料组下发失败", e);
			throw new RpcException(e);
		}
	}
	
}
