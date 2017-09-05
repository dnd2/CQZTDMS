package com.infodms.dms.dao.sales.marketmanage.planissued;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.marketmanage.planmanage.ActivitiesPlanManageDao;
import com.infodms.dms.po.TmDealerOrgRelationPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtCampaignPO;
import com.infodms.dms.po.TtCampaignSpaceExecutePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * @Title: 活动方案下发DAO
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-29
 * @author 
 * @mail 
 * @version 1.0
 * @remark 
 */
public class ActivitiesPlanIssuedDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ActivitiesPlanIssuedDao.class);
	private static final ActivitiesPlanIssuedDao dao = new ActivitiesPlanIssuedDao ();
	private static POFactory factory = POFactoryBuilder.getInstance();
	public static final ActivitiesPlanIssuedDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public  List<Map<String,Object>>  queryAreaOemCost(String areaId,Long orgId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT sum(TVC.AVAILABLE_AMOUNT) AVAILABLE_AMOUNT\n" );
		sql.append("   FROM TT_VS_COST TVC,TM_ORG TMO\n" );
		sql.append("  WHERE TVC.ORG_ID = TMO.ORG_ID\n" );
		sql.append(" and tvc.cost_type="+Constant.COST_TYPE_03+"\n");
//		sql.append("SELECT TVC.COST_ID,TVC.COST_TYPE, TMO.ORG_ID,TVC.FREEZE_AMOUNT,TVC.AVAILABLE_AMOUNT\n" );
//		sql.append("   FROM TT_VS_COST TVC,TM_ORG TMO\n" );
//		sql.append("  WHERE TVC.ORG_ID = TMO.ORG_ID\n" );
//		sql.append("    AND TVC.COST_SOURCE = "+Constant.COST_SOURCE_02+"\n" );
//		sql.append("    AND TVC.ORG_ID="+orgId+"\n");
		sql.append("    AND TVC.AREA_ID="+areaId+"\n");
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public  List<Map<String,Object>>  queryAreaOem2Cost(String areaId,Long orgId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT sum(TVC.AVAILABLE_AMOUNT) AVAILABLE_AMOUNT\n" );
		sql.append("   FROM TT_VS_COST TVC,TM_ORG TMO\n" );
		sql.append("  WHERE TVC.ORG_ID = TMO.ORG_ID\n" );
		sql.append(" and tvc.cost_type="+Constant.COST_TYPE_04+"\n");
//		sql.append("SELECT TVC.COST_ID,TVC.COST_TYPE, TMO.ORG_ID,TVC.FREEZE_AMOUNT,TVC.AVAILABLE_AMOUNT\n" );
//		sql.append("   FROM TT_VS_COST TVC,TM_ORG TMO\n" );
//		sql.append("  WHERE TVC.ORG_ID = TMO.ORG_ID\n" );
//		sql.append("    AND TVC.COST_SOURCE = "+Constant.COST_SOURCE_02+"\n" );
//		sql.append("    AND TVC.ORG_ID="+orgId+"\n");
 		sql.append("    AND TVC.AREA_ID="+areaId+"\n");
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * Function         : 品牌查询
	 * @return          : 品牌信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-29
	 */
	public List<Map<String, Object>> selectPinPai(Long companyId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVMG.GROUP_ID,TVMG.GROUP_NAME\n" );
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG\n" );
		sql.append(" WHERE TVMG.GROUP_LEVEL = 1\n");
		sql.append("   AND TVMG.STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		sql.append(" AND TVMG.COMPANY_ID = "+companyId+"\n");
		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName());
		return rs;
	}
	/**
	 * Function         : 方案车型查询
	 * @param           : 方案ID
	 * @return          : 方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-07-05
	 */
	public String getModelName(String campaignId) throws Exception{
		String modelName ="";
		StringBuffer sql= new StringBuffer();
		sql.append("select tvmg.group_name\n" );
		sql.append("  from tt_campaign_group_r tcgr, tm_vhcl_material_group tvmg\n" );
		sql.append(" where tcgr.campaign_model = tvmg.group_id\n" );
		sql.append("   and tcgr.campaign_id ="+campaignId+"\n");
		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName());
		if(rs.size()>0&&rs!=null){
			for(int i=0;i<rs.size();i++){
				Map<String,Object> map = rs.get(i);
				if("".endsWith(modelName)){
					modelName= map.get("GROUP_NAME").toString();
				}else{
					modelName= map.get("GROUP_NAME").toString()+","+modelName;
				}
			}
		}
		return modelName;
	}
	/**
	 * Function         : 活动车型查询
	 * @param           : 车型代码
	 * @param           : 车型名称
	 * @param           : 分页参数
	 * @return          : 活动车型信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-29
	 */
	public PageResult<Map<String, Object>> selectModel(String prodCode,String prodName,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TMCP.PROD_ID, TMCP.PROD_CODE, TMCP.PROD_NAME\n" );
		sql.append("  FROM TM_CAMPAIGN_PROD TMCP\n" );
		sql.append(" WHERE TMCP.PROD_STATUS = "+Constant.STATUS_ENABLE+"\n");
		if(!"".equals(prodCode)&&prodCode!=null){
			sql.append(" AND TMCP.PROD_CODE LIKE '%"+prodCode+"%'\n");
		}
		if(!"".equals(prodName)&&prodName!=null){
			sql.append(" AND TMCP.PROD_NAME LIKE '%"+prodName+"%'\n");
		}
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 活动方案查询
	 * @param           : 方案编号
	 * @param           : 方案名称
	 * @param           : 活动主题
	 * @param           : 分页参数
	 * @return          : 活动方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-29
	 */
	public PageResult<Map<String, Object>> selectCampaignOLD(String campaignNo,String campaignName,String campaignSubject,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
		sql.append("       TTC.CAMPAIGN_NO,\n" );
		sql.append("       TTC.CAMPAIGN_NAME,\n" );
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n" );
		sql.append("       TTC.CAMPAIGN_TYPE,\n" );
		sql.append("       TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
		sql.append("       TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE,\n" );
		sql.append("       TCP.PROD_NAME\n" );
		sql.append("  FROM TT_CAMPAIGN TTC,TM_CAMPAIGN_PROD TCP\n" );
		sql.append("  WHERE TTC.CAMPAIGN_MODEL = TCP.PROD_ID\n");
		sql.append("  AND TTC.CAMPAIGN_TYPE = "+Constant.CAMPAIGN_TYPE_01+"\n");
		if(!"".equals(campaignNo)&&campaignNo!=null){
			sql.append("AND TTC.CAMPAIGN_NO LIKE'%"+campaignNo+"%'\n");
		}
		if(!"".equals(campaignName)&&campaignName!=null){
			sql.append("AND TTC.CAMPAIGN_NAME LIKE'%"+campaignName+"%'\n");
		}
		if(!"".equals(campaignSubject)&&campaignSubject!=null){
			sql.append("AND TTC.CAMPAIGN_SUBJECT LIKE'%"+campaignSubject+"%'\n");
		}
		sql.append("AND (TTC.CAMPAIGN_ID IN(SELECT TCE.CAMPAIGN_ID FROM TT_CAMPAIGN_EXECUTE TCE WHERE TCE.CHECK_STATUS = ");
		sql.append(Constant.CAMPAIGN_CHECK_STATUS_12);
		sql.append(") OR TTC.CAMPAIGN_ID NOT IN(SELECT CAMPAIGN_ID FROM TT_CAMPAIGN_EXECUTE)");
		sql.append(")\n");
		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	
	public static Map<String, Object> activitiesInfoQuery2(String campaignId, String orgId) throws Exception {
		StringBuffer sql = new StringBuffer("\n");
		sql.append("SELECT TTC.CAMPAIGN_ID,\n");
		sql.append("       TTC.CAMPAIGN_MODEL,\n");
		sql.append("       TTC.CAMPAIGN_NO,\n");
		sql.append("       TTC.CAMPAIGN_NAME,\n");
		sql.append("       TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n");
		sql.append("       TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE,\n");
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n");
		sql.append("       TTC.CAMPAIGN_OBJECT,\n");
		sql.append("       TTC.CAMPAIGN_PURPOSE,\n");
		sql.append("       TTC.CAMPAIGN_NEED,\n");
		sql.append("       TTC.CAMPAIGN_DESC,\n");
		sql.append("       TTC.CAMPAIGN_TYPE,\n");
		sql.append("       TCSE.ORG_ID,\n");
		sql.append("       TCSE.REMARK,\n");
		sql.append("       TCSE.TO_PLACE_COUNT,\n");
		sql.append("       TCSE.TO_TEL_STORE_COUNT,\n");
		sql.append("       TCSE.CREATE_CARDS_COUNT,\n");
		sql.append("       TCSE.ORDER_COUNT,\n");
		sql.append("       TCSE.TURN_CAR_COUNT,\n");

		sql.append("       TCSE.SPACE_ID EXECUTE_ID\n");
		sql.append(" FROM TT_CAMPAIGN TTC,TT_CAMPAIGN_SPACE_EXECUTE TCSE\n");
		sql.append(" WHERE TTC.CAMPAIGN_ID = TCSE.CAMPAIGN_ID\n");

		if (!CommonUtils.isNullString(campaignId)) {
			sql.append(" AND TTC.CAMPAIGN_ID=" + campaignId + "\n");
		}

		if (!CommonUtils.isNullString(orgId)) {
			sql.append(" AND TCSE.ORG_ID = " + orgId + "\n");
		}

		Map<String, Object> rs1 = dao.pageQueryMap(sql.toString(), null, dao.getFunName());
		Map<String, Object> rs = ActivitiesPlanManageDao.getInstance().getModelByCampaignId(rs1);

		return rs;
	}
	
	
	
	
	/**
	 * Function         : 活动方案查询(用于长安汽车)
	 * @param           : 方案编号
	 * @param           : 方案名称
	 * @param           : 活动主题
	 * @param           : 分页参数
	 * @return          : 活动方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-08-25
	 */
	public PageResult<Map<String, Object>> selectCampaign(Map<String,String> paraMap,int curPage,int pageSize) throws Exception{

        String campaignNo = paraMap.get("campaignNo");
		String campaignName = paraMap.get("campaignName");
		String campaignSubject = paraMap.get("campaignSubject");
		String startDate = paraMap.get("startDate");
		String endDate = paraMap.get("endDate");

        StringBuffer sql= new StringBuffer();
		sql.append("SELECT TTC.CAMPAIGN_ID,\n");
		sql.append("       TTC.CAMPAIGN_NO,\n");  
		sql.append("       TTC.CAMPAIGN_NAME,\n");  
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n");
		sql.append("       TTC.CAMPAIGN_STATUS,\n");
		sql.append("       TO_CHAR(TTC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");
		sql.append("       TO_CHAR(TTC.END_DATE, 'YYYY-MM-DD') END_DATE\n");  
		sql.append("  FROM TT_CAMPAIGN TTC\n");
		sql.append(" WHERE 1 = 1\n");  

		if(!"".equals(campaignNo)&&campaignNo!=null){
			sql.append("AND TTC.CAMPAIGN_NO LIKE'%"+campaignNo+"%'\n");
		}
		if(!"".equals(campaignName)&&campaignName!=null){
			sql.append("AND TTC.CAMPAIGN_NAME LIKE'%"+campaignName+"%'\n");
		}
		if(!"".equals(campaignSubject)&&campaignSubject!=null){
			sql.append("AND TTC.CAMPAIGN_SUBJECT LIKE'%"+campaignSubject+"%'\n");
		}
		if (!CommonUtils.isNullString(startDate)) {
			sql.append(" AND TRUNC(TTC.START_DATE) >= TO_DATE('").append(startDate).append("','yyyy-mm-dd')\n");
		}

		if (!CommonUtils.isNullString(endDate)) {
			sql.append(" AND TRUNC(TTC.END_DATE) <= TO_DATE('").append(endDate).append("','yyyy-mm-dd')\n");
		}
		sql.append("ORDER BY TTC.CREATE_DATE DESC, TTC.CAMPAIGN_ID\n");

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}	
	/**
	 * Function         : 活动方案区域审批查询
	 * @param           : 方案编号
	 * @param           : 方案名称
	 * @param           : 方案主题
	 * @param           : 经销商CODE
	 * @param           : 分页参数
	 * @return          : 活动方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-07-05
	 */
	public static PageResult<Map<String, Object>> querySpaceAct(Map<String, String> paraMap, int curPage, int pageSize) {
		String poseId = paraMap.get("poseId");
		String orgId = paraMap.get("orgId");
		String areaId = paraMap.get("areaId");
		String campaignNo = paraMap.get("campaignNo");
		String campaignName = paraMap.get("campaignName");
		String campaignSubject = paraMap.get("campaignSubject");
		String startDate = paraMap.get("startDate");
		String endDate = paraMap.get("endDate");

		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT TTC.CAMPAIGN_ID,\n");
		sql.append("     TTC.CAMPAIGN_NO,\n");
		sql.append("     TTC.CAMPAIGN_NAME,\n");
		sql.append("     TTC.CAMPAIGN_TYPE,\n");
		sql.append("     TTC.CAMPAIGN_SUBJECT,\n");
		sql.append("     TCSE.ORG_ID,\n");
		sql.append("     TCSE.SPACE_ID,\n");
		sql.append("     TBA.AREA_NAME,\n");
		sql.append("     TCSE.CHECK_STATUS,\n");
		sql.append("     TO_CHAR(TTC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");
		sql.append("     TO_CHAR(TTC.END_DATE, 'YYYY-MM-DD') END_DATE,\n");
		sql.append("     TO_CHAR(TCSE.SUBMITS_DATE, 'YYYY-MM-DD') SUBMITS_DATE\n");
		
		
		sql.append("FROM TT_CAMPAIGN               TTC,\n");
		sql.append("     TT_CAMPAIGN_SPACE_EXECUTE TCSE,\n");
		sql.append("     TM_BUSINESS_AREA          TBA,\n");
		sql.append("     TM_POSE_BUSINESS_AREA     TMPBA\n");
		sql.append("WHERE TTC.CAMPAIGN_ID = TCSE.CAMPAIGN_ID\n");
		sql.append(" AND TMPBA.AREA_ID = TTC.AREA_ID\n");
		sql.append(" AND TMPBA.AREA_ID = TBA.AREA_ID\n");
		sql.append(" AND TTC.CAMPAIGN_TYPE = ").append(Constant.CAMPAIGN_TYPE_01).append("\n");
		sql.append(" AND TMPBA.POSE_ID = ").append(poseId).append("\n");
		sql.append(" AND TCSE.CHECK_STATUS IN (").append(Constant.PLAN_CHECK_STATUS_02).append(") --已保存,方案审核驳回\n");

		if (!CommonUtils.isNullString(areaId)) {
			sql.append(" AND TTC.AREA_ID = ").append(areaId).append("\n");
		}

		if (!CommonUtils.isNullString(campaignNo)) {
			sql.append(" AND TTC.CAMPAIGN_NO LIKE '%").append(campaignNo).append("%'\n");
		}

		if (!CommonUtils.isNullString(campaignName)) {
			sql.append(" AND TTC.CAMPAIGN_NAME LIKE '%").append(campaignName).append("%'\n");
		}

		if (!CommonUtils.isNullString(campaignSubject)) {
			sql.append(" AND TTC.CAMPAIGN_SUBJECT LIKE '%").append(campaignSubject).append("%'\n");
		}

		if (!CommonUtils.isNullString(startDate)) {
			sql.append(" AND TRUNC(TTC.START_DATE) >= TO_DATE('").append(startDate).append("','yyyy-mm-dd')\n");
		}

		if (!CommonUtils.isNullString(endDate)) {
			sql.append(" AND TRUNC(TTC.END_DATE) <= TO_DATE('").append(endDate).append("','yyyy-mm-dd')\n");
		}

		sql.append("ORDER BY TTC.CREATE_DATE DESC, TCSE.CHECK_STATUS\n");

		PageResult<Map<String, Object>> spaceActList = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);

		return spaceActList;
	}
	
	/**
	 * Function         : 活动方案总部审批查询
	 * @param           : 方案编号
	 * @param           : 方案名称
	 * @param           : 方案主题
	 * @param           : 经销商CODE
	 * @param           : 分页参数
	 * @return          : 活动方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-07-05
	 */
	public PageResult<Map<String, Object>> activitiesPlanOemCheckQuery(Map<String,String> paraMap,int curPage,int pageSize) throws Exception{
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sql= new StringBuffer("\n");
		String orgId = paraMap.get("orgId");
		String campaignNo = paraMap.get("campaignNo");
		String campaignName = paraMap.get("campaignName");
		String campaignSubject = paraMap.get("campaignSubject");
		String startDate = paraMap.get("startDate");
		String endDate = paraMap.get("endDate");

		sql.append("SELECT TTC.CAMPAIGN_ID,\n");
		sql.append("     TTC.CAMPAIGN_NO,\n");
		sql.append("     TTC.CAMPAIGN_NAME,\n");
		sql.append("     TTC.CAMPAIGN_TYPE,\n");
		sql.append("     TTC.CAMPAIGN_SUBJECT,\n");
		sql.append("     TCSE.ORG_ID,\n");
		sql.append("     TMO.ORG_NAME,\n");
		sql.append("     TCSE.SPACE_ID,\n");
		sql.append("     TCSE.CHECK_STATUS,\n");
		sql.append("     TO_CHAR(TTC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");
		sql.append("     TO_CHAR(TTC.END_DATE, 'YYYY-MM-DD') END_DATE,\n");
		sql.append("     TO_CHAR(TCSE.SUBMITS_DATE, 'YYYY-MM-DD') SUBMITS_DATE\n");
		sql.append("FROM TT_CAMPAIGN               TTC,\n");
		sql.append("     TT_CAMPAIGN_SPACE_EXECUTE TCSE,\n");
		sql.append("     TM_ORG          TMO\n");
		sql.append("WHERE TTC.CAMPAIGN_ID = TCSE.CAMPAIGN_ID\n");
		sql.append(" AND TCSE.ORG_ID = TMO.ORG_ID\n");
		sql.append(" AND TCSE.CHECK_STATUS IN (").append(Constant.CAMPAIGN_CHECK_STATUS_03).append(") --大区已提报(等待车厂审核)\n");

		if (!CommonUtils.isNullString(campaignNo)) {
			sql.append(" AND TTC.CAMPAIGN_NO LIKE '%").append(campaignNo).append("%'\n");
		}

		if (!CommonUtils.isNullString(campaignName)) {
			sql.append(" AND TTC.CAMPAIGN_NAME LIKE '%").append(campaignName).append("%'\n");
		}

		if (!CommonUtils.isNullString(campaignSubject)) {
			sql.append(" AND TTC.CAMPAIGN_SUBJECT LIKE '%").append(campaignSubject).append("%'\n");
		}

		if (!CommonUtils.isNullString(startDate)) {
			sql.append(" AND TRUNC(TTC.START_DATE) >= TO_DATE('").append(startDate).append("','yyyy-mm-dd')\n");
		}

		if (!CommonUtils.isNullString(endDate)) {
			sql.append(" AND TRUNC(TTC.END_DATE) <= TO_DATE('").append(endDate).append("','yyyy-mm-dd')\n");
		}

		sql.append("ORDER BY TTC.CREATE_DATE DESC, TCSE.CHECK_STATUS\n");


		
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 活动方案总部审批查询
	 * @param           : 方案编号
	 * @param           : 方案名称
	 * @param           : 方案主题
	 * @param           : 经销商CODE
	 * @param           : 分页参数
	 * @return          : 活动方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-07-05
	 */
	public PageResult<Map<String, Object>> getAciviesPlanList(Map<String,String> paraMap,int curPage,int pageSize) throws Exception{
		List<Object> params = new LinkedList<Object>();

		StringBuffer sql= new StringBuffer("\n");
		String orgId = paraMap.get("orgId");
		String campaignNo = paraMap.get("campaignNo");
		String campaignName = paraMap.get("campaignName");
		String campaignSubject = paraMap.get("campaignSubject");
		String startDate = paraMap.get("startDate");
		String endDate = paraMap.get("endDate");

		sql.append("SELECT TTC.CAMPAIGN_ID,\n");
		sql.append("     TTC.CAMPAIGN_NO,\n");
		sql.append("     TTC.CAMPAIGN_NAME,\n");
		sql.append("     TTC.CAMPAIGN_TYPE,\n");
		sql.append("     TTC.CAMPAIGN_SUBJECT,\n");
		sql.append("     TCSE.ORG_ID,\n");
		sql.append("     TMO.ORG_NAME,\n");
		sql.append("     TCSE.SPACE_ID,\n");
		sql.append("     TCSE.CHECK_STATUS,\n");
		sql.append("     TO_CHAR(TTC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");
		sql.append("     TO_CHAR(TTC.END_DATE, 'YYYY-MM-DD') END_DATE,\n");
		sql.append("     TO_CHAR(TCSE.SUBMITS_DATE, 'YYYY-MM-DD') SUBMITS_DATE\n");
		sql.append("FROM TT_CAMPAIGN               TTC,\n");
		sql.append("     TT_CAMPAIGN_SPACE_EXECUTE TCSE,\n");
		sql.append("     TM_ORG          TMO\n");
		sql.append("WHERE TTC.CAMPAIGN_ID = TCSE.CAMPAIGN_ID\n");
		sql.append(" AND TCSE.ORG_ID = TMO.ORG_ID\n");
		sql.append(" AND TCSE.CHECK_STATUS IN ("+Constant.CAMPAIGN_CHECK_STATUS_03+","+Constant.CAMPAIGN_CHECK_STATUS_04+","+Constant.CAMPAIGN_CHECK_STATUS_05+")\n");

		if (!CommonUtils.isNullString(campaignNo)) {
			sql.append(" AND TTC.CAMPAIGN_NO LIKE '%").append(campaignNo).append("%'\n");
		}

		if (!CommonUtils.isNullString(campaignName)) {
			sql.append(" AND TTC.CAMPAIGN_NAME LIKE '%").append(campaignName).append("%'\n");
		}

		if (!CommonUtils.isNullString(campaignSubject)) {
			sql.append(" AND TTC.CAMPAIGN_SUBJECT LIKE '%").append(campaignSubject).append("%'\n");
		}

		if (!CommonUtils.isNullString(startDate)) {
			sql.append(" AND TRUNC(TTC.START_DATE) >= TO_DATE('").append(startDate).append("','yyyy-mm-dd')\n");
		}

		if (!CommonUtils.isNullString(endDate)) {
			sql.append(" AND TRUNC(TTC.END_DATE) <= TO_DATE('").append(endDate).append("','yyyy-mm-dd')\n");
		}

		sql.append("ORDER BY TTC.CREATE_DATE DESC, TCSE.CHECK_STATUS\n");



		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 方案信息查询
	 * @param           : 方案ID
	 * @return          : 方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-07-05
	 */
	public Map<String, Object> activitiesInfoQuery(String campaignId,String executeId,String orgId) throws Exception{
		StringBuffer sql= new StringBuffer();
		TmOrgPO tmo = new TmOrgPO() ;
		tmo.setOrgId(Long.parseLong(orgId)) ;
		tmo.setOrgType(Constant.ORG_TYPE_OEM) ;
		List<TmOrgPO> orgList = dao.select(tmo) ;
		
		if(!CommonUtils.isNullList(orgList)){
		sql.append("SELECT TTC.CAMPAIGN_ID,\n");
		sql.append("TTC.CAMPAIGN_MODEL,\n");
		sql.append("TVMG.GROUP_NAME,\n");
		sql.append("TTC.AREA_ID,\n");
		sql.append("TBA.AREA_NAME,\n");
		sql.append("TTC.CAMPAIGN_NO,\n");
		sql.append("TTC.CAMPAIGN_NAME,\n");
		sql.append("TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n");
		sql.append("TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE,\n");
		sql.append("TO_CHAR(TCE.SUBMITS_DATE,'YYYY-MM-DD') SUBMITS_DATE,\n");
		sql.append("TCU.NAME,\n");
		sql.append("TTC.CAMPAIGN_SUBJECT,\n");
		sql.append("TTC.CAMPAIGN_OBJECT,\n");
		sql.append("TTC.CAMPAIGN_PURPOSE,\n");
		sql.append("TTC.CAMPAIGN_NEED,\n");
		sql.append("TTC.CAMPAIGN_DESC,\n");
		sql.append("TTC.CAMPAIGN_TYPE,\n");
		sql.append("TCE.EVALUATE_DESC,\n");
		sql.append("TCE.ADVICE_DESC,\n");
		sql.append("TCE.SPACE_ID,\n");
		sql.append("TCE.REMARK,\n");
		sql.append("TCE.IS_FLEET,\n");
		sql.append("nvl(TCE.IS_ALL_PROTOCOL, ").append(Constant.IF_TYPE_NO).append(") IS_ALL_PROTOCOL,\n");
		sql.append("TMO.ORG_CODE,\n");
		sql.append("TMO.ORG_ID,\n");
		sql.append("TMO.ORG_NAME\n");
		sql.append("FROM TT_CAMPAIGN TTC,TT_CAMPAIGN_SPACE_EXECUTE TCE,TM_VHCL_MATERIAL_GROUP TVMG, TM_ORG TMO, TC_USER TCU, TM_BUSINESS_AREA TBA\n");
		sql.append("WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n");
		sql.append("AND TTC.AREA_ID = TBA.AREA_ID\n");
		sql.append("AND TVMG.GROUP_ID = TTC.GROUP_ID\n");
		sql.append("AND TCE.ORG_ID = TMO.ORG_ID\n");
		sql.append("AND TCE.CREATE_BY = TCU.USER_ID\n");
		if(!"".equals(campaignId)&&campaignId!=null){
			sql.append(" AND TTC.CAMPAIGN_ID ="+campaignId+"\n");
		}
		if(!"".equals(executeId)&&executeId!=null){
			sql.append(" AND TCE.SPACE_ID ="+executeId+"\n");
		}
		}else{
		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
		sql.append("       TTC.CAMPAIGN_MODEL,\n" );
		sql.append("       TVMG.GROUP_NAME,\n" );
		sql.append("       TTC.AREA_ID,\n" );
		sql.append("TBA.AREA_NAME,\n");
//		sql.append("       TCP.PROD_NAME,\n" );
		sql.append("       TTC.CAMPAIGN_NO,\n" );
		sql.append("       TTC.CAMPAIGN_NAME,\n" );
		sql.append("       TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
		sql.append("       TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE,\n" );
		sql.append("TO_CHAR(TCE.SUBMITS_DATE,'YYYY-MM-DD') SUBMITS_DATE,\n");
		sql.append("TCU.NAME,\n");
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n" );
		sql.append("       TTC.CAMPAIGN_OBJECT,\n" );
		sql.append("       TTC.CAMPAIGN_PURPOSE,\n" );
		sql.append("       TTC.CAMPAIGN_NEED,\n" );
		sql.append("       TTC.CAMPAIGN_DESC,\n" );
		sql.append("       TTC.CAMPAIGN_TYPE,\n" );
		sql.append("       TCE.DEALER_ID,\n" );
		sql.append("       TMD.DEALER_CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       TCE.EXEC_ADD_DESC,\n" );
		sql.append("       TCE.EVALUATE_DESC,\n" );
		sql.append("       TCE.ADVICE_DESC,\n" );
		sql.append("TCE.REMARK,\n");
		sql.append("TCE.IS_FLEET,\n");
		sql.append("-1 IS_ALL_PROTOCOL,\n");
		sql.append("       TMO.ORG_ID,\n" );
		sql.append("       TMO.ORG_NAME,\n" );
		sql.append("       TCE.EXECUTE_ID\n" );
		sql.append(" FROM TT_CAMPAIGN TTC,TT_CAMPAIGN_EXECUTE TCE," +
//				"TM_CAMPAIGN_PROD TCP," +
				"TM_DEALER TMD,TM_VHCL_MATERIAL_GROUP TVMG, TC_USER TCU, TM_ORG TMO, TM_BUSINESS_AREA TBA\n" );
		sql.append(" WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n");
//		sql.append(" AND TTC.CAMPAIGN_MODEL = TCP.PROD_ID\n");
		sql.append("AND TTC.AREA_ID = TBA.AREA_ID\n");
		sql.append(" AND TCE.DEALER_ID = TMD.DEALER_ID\n");
		sql.append(" AND TMO.ORG_ID = TCE.ORG_ID\n");
		sql.append(" AND TVMG.GROUP_ID = TTC.GROUP_ID\n");
		sql.append(" AND TCE.CREATE_BY = TCU.USER_ID\n");
		if(!"".equals(campaignId)&&campaignId!=null){
			sql.append(" AND TTC.CAMPAIGN_ID ="+campaignId+"\n");
		}
		if(!"".equals(executeId)&&executeId!=null){
			sql.append(" AND TCE.EXECUTE_ID ="+executeId+"\n");
		}
		}
		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, getFunName());
		return rs;
	}
	
	public Map<String, Object> activitiesInfoQuery(String campaignId,String executeId,Long orgId) throws Exception{
		StringBuffer sql= new StringBuffer("\n");
		
		sql.append("SELECT TTC.CAMPAIGN_ID,\n");
		sql.append("TTC.CAMPAIGN_MODEL,\n");
		sql.append("TVMG.GROUP_NAME,\n");
		sql.append("TTC.AREA_ID,\n");
		sql.append("TTC.CAMPAIGN_NO,\n");
		sql.append("TTC.CAMPAIGN_NAME,\n");
		sql.append("TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n");
		sql.append("TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE,\n");
		sql.append("TO_CHAR(TCE.SUBMITS_DATE,'YYYY-MM-DD') SUBMITS_DATE,\n");
		sql.append("TCU.NAME,\n");
		sql.append("TTC.CAMPAIGN_SUBJECT,\n");
		sql.append("TTC.CAMPAIGN_OBJECT,\n");
		sql.append("TTC.CAMPAIGN_PURPOSE,\n");
		sql.append("TTC.CAMPAIGN_NEED,\n");
		sql.append("TTC.CAMPAIGN_DESC,\n");
		sql.append("TCE.EXEC_ADD_DESC,\n");
		sql.append("TMO.ORG_CODE,\n");
		sql.append("TMO.ORG_NAME,\n");
		sql.append("TTC.CAMPAIGN_TYPE,\n");
		sql.append("TCE.EVALUATE_DESC,\n");
		sql.append("TCE.ADVICE_DESC,\n");
		sql.append("TCE.IS_FLEET,\n");
		sql.append("nvl(TCE.IS_ALL_PROTOCOL, ").append(Constant.IF_TYPE_NO).append(") IS_ALL_PROTOCOL,\n");
		sql.append("TCE.REMARK,\n");
		sql.append("TCE.SPACE_ID\n");
		sql.append("FROM TT_CAMPAIGN TTC,TT_CAMPAIGN_SPACE_EXECUTE TCE,TM_VHCL_MATERIAL_GROUP TVMG, TM_ORG TMO, TC_USER TCU\n");
		sql.append("WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n");
		sql.append("AND TVMG.GROUP_ID = TTC.GROUP_ID\n");
		sql.append("AND TCE.ORG_ID = TMO.ORG_ID\n");
		sql.append("AND TCE.CREATE_BY = TCU.USER_ID\n");
		if(!"".equals(campaignId)&&campaignId!=null){
			sql.append(" AND TTC.CAMPAIGN_ID ="+campaignId+"\n");
		}
		if(!"".equals(executeId)&&executeId!=null){
			sql.append(" AND TCE.SPACE_ID ="+executeId+"\n");
		}
		
		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, getFunName());
		return rs;
	}
	
	/**
	 * Function         : 大区方案信息查询
	 * @param           : 方案ID
	 * @return          : 方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-07-05
	 */
	public Map<String, Object> activitiesMyInfoQuery(String campaignId,String executeId,String orgId) throws Exception{
		TmOrgPO tmo = new TmOrgPO() ;
		tmo.setOrgId(Long.parseLong(orgId)) ;
		List<TmOrgPO> orgList = dao.select(tmo) ;
		
		StringBuffer sql= new StringBuffer();
		
		if(!CommonUtils.isNullList(orgList)){
		sql.append("SELECT TTC.CAMPAIGN_ID,\n");
		sql.append("TTC.CAMPAIGN_MODEL,\n");
		sql.append("TVMG.GROUP_NAME,\n");
		sql.append("TTC.AREA_ID,\n");
		sql.append("TTC.CAMPAIGN_NO,\n");
		sql.append("TTC.CAMPAIGN_NAME,\n");
		sql.append("TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n");
		sql.append("TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE,\n");
		sql.append("TO_CHAR(TCE.SUBMITS_DATE,'YYYY-MM-DD') SUBMITS_DATE,\n");
		sql.append("TCU.NAME,\n");
		sql.append("TTC.CAMPAIGN_SUBJECT,\n");
		sql.append("TTC.CAMPAIGN_OBJECT,\n");
		sql.append("TTC.CAMPAIGN_PURPOSE,\n");
		sql.append("TTC.CAMPAIGN_NEED,\n");
		sql.append("TTC.CAMPAIGN_DESC,\n");
		sql.append("TTC.CAMPAIGN_TYPE,\n");
		sql.append("TCE.EVALUATE_DESC,\n");
		sql.append("TCE.ADVICE_DESC,\n");
		sql.append("TMO.ORG_NAME,\n");
		sql.append("TMO.ORG_CODE,\n");
		sql.append("TMO.ORG_ID,\n");
		sql.append("TCE.is_fleet,\n");
		sql.append("tce.is_all_protocol,\n");
		sql.append("TCE.REMARK,\n");
		sql.append("TCE.SPACE_ID EXECUTE_ID\n");
		sql.append("FROM TT_CAMPAIGN TTC,TT_CAMPAIGN_SPACE_EXECUTE TCE,TM_VHCL_MATERIAL_GROUP TVMG, TM_ORG TMO, TC_USER TCU\n");
		sql.append("WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n");
		sql.append("AND TVMG.GROUP_ID = TTC.GROUP_ID\n");
		sql.append("AND TCE.ORG_ID = TMO.ORG_ID\n");
		sql.append("AND TCE.CREATE_BY = TCU.USER_ID\n");
		if(!"".equals(campaignId)&&campaignId!=null){
			sql.append(" AND TTC.CAMPAIGN_ID ="+campaignId+"\n");
		}
		if(!"".equals(executeId)&&executeId!=null){
			sql.append(" AND TCE.SPACE_ID ="+executeId+"\n");
		}
		}else{
		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
		sql.append("       TTC.CAMPAIGN_MODEL,\n" );
		sql.append("       TVMG.GROUP_NAME,\n" );
		sql.append("       TTC.AREA_ID,\n" );
//		sql.append("       TCP.PROD_NAME,\n" );
		sql.append("       TTC.CAMPAIGN_NO,\n" );
		sql.append("       TTC.CAMPAIGN_NAME,\n" );
		sql.append("       TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
		sql.append("       TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE,\n" );
		sql.append("TO_CHAR(TCE.SUBMITS_DATE,'YYYY-MM-DD') SUBMITS_DATE,\n");
		sql.append("TCU.NAME,\n");
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n" );
		sql.append("       TTC.CAMPAIGN_OBJECT,\n" );
		sql.append("       TTC.CAMPAIGN_PURPOSE,\n" );
		sql.append("       TTC.CAMPAIGN_NEED,\n" );
		sql.append("       TTC.CAMPAIGN_DESC,\n" );
		sql.append("       TTC.CAMPAIGN_TYPE,\n" );
		sql.append("       TCE.DEALER_ID,\n" );
		sql.append("       TMD.DEALER_CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       TCE.EXEC_ADD_DESC,\n" );
		sql.append("       TCE.EVALUATE_DESC,\n" );
		sql.append("       TCE.ADVICE_DESC,\n" );
		sql.append("       tce.is_fleet,\n");
		sql.append("       -1 is_all_protocol,\n");
		sql.append("       TCE.REMARK,\n");
		sql.append("       TCE.EXECUTE_ID\n" );
		sql.append(" FROM TT_CAMPAIGN TTC,TT_CAMPAIGN_EXECUTE TCE," +
//				"TM_CAMPAIGN_PROD TCP," +
				"TM_DEALER TMD,TM_VHCL_MATERIAL_GROUP TVMG, TC_USER TCU\n" );
		sql.append(" WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n");
//		sql.append(" AND TTC.CAMPAIGN_MODEL = TCP.PROD_ID\n");
		sql.append(" AND TCE.DEALER_ID = TMD.DEALER_ID\n");
		sql.append(" AND TVMG.GROUP_ID = TTC.GROUP_ID\n");
		sql.append(" AND TCE.CREATE_BY = TCU.USER_ID\n");
		if(!"".equals(campaignId)&&campaignId!=null){
			sql.append(" AND TTC.CAMPAIGN_ID ="+campaignId+"\n");
		}
		if(!"".equals(executeId)&&executeId!=null){
			sql.append(" AND TCE.EXECUTE_ID ="+executeId+"\n");
		}
		}
		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, getFunName());
		return rs;
	}
	public  List<Map<String,Object>>  queryMyExecute(String dealers,String campaign){
		StringBuffer sql= new StringBuffer();
		sql.append(" select * from  tt_campaign_space_execute tcse where tcse.campaign_id="+campaign+" and tcse.org_id in("+dealers+")\n" );
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public  List<Map<String,Object>>  queryMyCamExecute(String dealers,String campaign){
		StringBuffer sql= new StringBuffer();
		sql.append(" select * from  tt_campaign_execute tcse where tcse.campaign_id="+campaign+" and tcse.dealer_id in("+dealers+")\n" );
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * Function         : 方案执行目标查询
	 * @param           : 执行ID
	 * @return          : 目标信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-07-05
	 */
	public Map<String, Object> activitiesTargetQuery(String executeId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TCT.TARGET_ID,\n" );
		sql.append("       TCT.CALLS_HOUSES_CNT_TGT,\n" );
		sql.append("       TCT.RESERVE_CNT_TGT,\n" );
		sql.append("       TCT.ORDER_CNT_TGT,\n" );
		sql.append("       TCT.DELIVERY_CNT_TGT,\n" );
		sql.append("       NVL(TCT.CALLS_HOUSES_CNT_ACT,0) CALLS_HOUSES_CNT_ACT,\n" );
		sql.append("       NVL(TCT.RESERVE_CNT_ACT,0) RESERVE_CNT_ACT,\n" );
		sql.append("       NVL(TCT.ORDER_CNT_ACT,0) ORDER_CNT_ACT,\n" );
		sql.append("       NVL(TCT.DELIVERY_CNT_ACT,0) DELIVERY_CNT_ACT,\n" );
		sql.append("       ROUND(NVL(TCT.CALLS_HOUSES_CNT_ACT,0) / TCT.CALLS_HOUSES_CNT_TGT * 100) AS CALLS_HOUSES_CNT,\n" );
		sql.append("       ROUND(NVL(TCT.RESERVE_CNT_ACT,0) / TCT.RESERVE_CNT_TGT * 100) AS RESERVE_CNT,\n" );
		sql.append("       ROUND(NVL(TCT.ORDER_CNT_ACT,0) / TCT.ORDER_CNT_TGT * 100) AS ORDER_CNT,\n" );
		sql.append("       ROUND(NVL(TCT.DELIVERY_CNT_ACT,0) / TCT.DELIVERY_CNT_TGT * 100) AS DELIVERY_CNT\n" );
		sql.append("  FROM TT_CAMPAIGN_TARGET TCT\n" );
		sql.append(" WHERE TCT.EXECUTE_ID ="+executeId+"\n");
		sql.append(" GROUP BY TCT.TARGET_ID,\n" );
		sql.append("          TCT.CALLS_HOUSES_CNT_TGT,\n" );
		sql.append("          TCT.RESERVE_CNT_TGT,\n" );
		sql.append("          TCT.ORDER_CNT_TGT,\n" );
		sql.append("          TCT.DELIVERY_CNT_TGT,\n" );
		sql.append("          TCT.CALLS_HOUSES_CNT_ACT,\n" );
		sql.append("          TCT.RESERVE_CNT_ACT,\n" );
		sql.append("          TCT.ORDER_CNT_ACT,\n" );
		sql.append("          TCT.DELIVERY_CNT_ACT");
		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, getFunName());
		return rs;
	}
	/**
	 * 根据执行ID查询活动费用
	 * @param  :执行ID
	 * @return :活动费用信息
	 */
	public List<Map<String, Object>> getActivitiesCostbyId(String executeId,String campaignId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TCC.COST_ID,\n" );
		sql.append("       TCC.COST_TYPE,\n" );
		sql.append("	   TCC.ACTIVITY_CONTENT,\n");
		sql.append("       TCC.ITEM_NAME,\n" );
		sql.append("       TCC.ITEM_REMARK,\n" );
		sql.append("       TCC.ITEM_PRICE,\n" );
		sql.append("       TCC.ITEM_COUNT,\n" );
		sql.append("       TCC.COST_ACCOUNT,\n" );
		sql.append("       TCC.REGION,\n" );
		sql.append("       TO_CHAR(TCC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");
		sql.append("       TO_CHAR(TCC.END_DATE, 'YYYY-MM-DD') END_DATE,\n");
		sql.append("       nvl(TCC.REAL_COST,0) REAL_COST,\n" );
		sql.append("       nvl(TCC.ITEM_COST,0) ITEM_COST,");
		sql.append("       TCC.ACTIVITY_TYPE,");
		sql.append("       TCC.PLAN_COST\n" );
		sql.append("  FROM TT_CAM_CAMPAIGN_COST TCC\n" );
		sql.append(" WHERE TCC.EXECUTE_ID ="+executeId+"\n" );
		sql.append("   AND TCC.CAMPAIGN_ID ="+campaignId+"\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	
	/**
	 * 根据执行ID,组织ID查询活动费用，组织可用费用
	 * @param  :执行ID
	 * @return :活动费用信息
	 * @author snake
	 */
	public List<Map<String, Object>> getActivitiesCostbyId(String executeId,String campaignId,String dealer_id,String area_org_id,String areaId,String oem_org_id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TCC.COST_ID,\n" );
		sql.append("       TCC.COST_TYPE,\n" );
		sql.append("	   TCC.ACTIVITY_TYPE ,\n");
		sql.append("	   TCC.ACTIVITY_CONTENT,\n");
		sql.append("       TCC.ITEM_NAME,\n" );
		sql.append("       TCC.ITEM_REMARK,\n" );
		sql.append("       TCC.REGION,\n" );
		sql.append("       TO_CHAR(TCC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");
		sql.append("       TO_CHAR(TCC.END_DATE, 'YYYY-MM-DD') END_DATE,\n");
		sql.append("       NVL(TCC.ITEM_PRICE,0) ITEM_PRICE,\n" );
		sql.append("       NVL(TCC.ITEM_COUNT,0) ITEM_COUNT,\n" );
		sql.append("       NVL(TCC.COST_ACCOUNT,0) COST_ACCOUNT,\n" );
		sql.append("       NVL(TCC.REAL_COST,0) REAL_COST,\n" );
		sql.append("       NVL(TCC.ITEM_COST,0) ITEM_COST,\n" );
		sql.append("       NVL(TCC.PLAN_COST,0) PLAN_COST,\n" );
		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id= "+dealer_id+" AND cost_type = "+Constant.COST_TYPE_01+" and cost_source = "+Constant.COST_SOURCE_01+"AND AREA_ID="+areaId+"),0) DEALER_COST, \n" );                    
		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id IS NULL AND v.org_id = "+area_org_id+" and cost_type = "+Constant.COST_TYPE_01+" and cost_source = "+Constant.COST_SOURCE_02+" AND AREA_ID="+areaId+" ),0) AREA_COST,\n" );
		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id IS NULL AND v.org_id = "+oem_org_id+" and cost_type = "+Constant.COST_TYPE_01+" and cost_source = "+Constant.COST_SOURCE_03+" AND AREA_ID="+areaId+"),0) OEMPRCost,\n" );
		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id IS NULL AND v.org_id = "+oem_org_id+" and cost_type = "+Constant.COST_TYPE_02+" and cost_source = "+Constant.COST_SOURCE_03+" AND AREA_ID="+areaId+"),0) OEMADCost \n" );
		sql.append("  FROM TT_CAM_CAMPAIGN_COST TCC\n" );
		sql.append(" WHERE TCC.EXECUTE_ID ="+executeId+"\n" );
		sql.append("   AND TCC.CAMPAIGN_ID ="+campaignId+"\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
//	public List<Map<String, Object>> getActivitiesCostbyId(String executeId,String campaignId,String dealer_id,String area_org_id,String oem_org_id){
//		StringBuffer sql= new StringBuffer();
//		sql.append("SELECT TCC.COST_ID,\n" );
//		sql.append("       TCC.COST_TYPE,\n" );
//		sql.append("       TCC.ITEM_NAME,\n" );
//		sql.append("       TCC.ITEM_REMARK,\n" );
//		sql.append("       NVL(TCC.ITEM_PRICE,0) ITEM_PRICE,\n" );
//		sql.append("       NVL(TCC.ITEM_COUNT,0) ITEM_COUNT,\n" );
//		sql.append("       NVL(TCC.COST_ACCOUNT,0) COST_ACCOUNT,\n" );
//		sql.append("       NVL(TCC.REAL_COST,0) REAL_COST,\n" );
//		sql.append("       NVL(TCC.ITEM_COST,0) ITEM_COST,\n" );
//		sql.append("       NVL(TCC.PLAN_COST,0) PLAN_COST,\n" );
//		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id= "+dealer_id+" AND cost_type = 11291001 and cost_source = "+Constant.COST_SOURCE_01+"),0) DEALER_COST, \n" );                    
//		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id IS NULL AND v.org_id = "+area_org_id+" and cost_type = "+Constant.COST_TYPE_01+" and cost_source = "+Constant.COST_SOURCE_02+"),0) AREA_COST, \n" );
//		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id IS NULL AND v.org_id = "+oem_org_id+" and cost_type = "+Constant.COST_TYPE_01+" and cost_source = "+Constant.COST_SOURCE_03+"),0) OEMPR_COST,\n" );
//		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id IS NULL AND v.org_id = "+oem_org_id+" and cost_type = "+Constant.COST_TYPE_02+" and cost_source = "+Constant.COST_SOURCE_03+"),0) OEMAD_COST \n" );
//		sql.append("  FROM TT_CAM_CAMPAIGN_COST TCC\n" );
//		sql.append(" WHERE TCC.EXECUTE_ID ="+executeId+"\n" );
//		sql.append("   AND TCC.CAMPAIGN_ID ="+campaignId+"\n");
//		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
//		return list;
//	}
	/**
	 * 根据执行ID查询活动费用
	 * @param  :执行ID
	 * @return :活动费用信息
	 */
	public List<Map<String, Object>> getActivitiesMediaCost(String executeId,String dealer_id,String area_org_id,String areaId,String oem_org_id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TCMC.COST_ID,\n" );
		sql.append("       TCMC.COST_TYPE,\n" );
		sql.append("       TCMC.MEDIA_TYPE,\n" );
		sql.append("       TCMC.ADV_SUBJECT,\n" );
		sql.append("       TCMC.EXECUTE_ID,\n");  
		sql.append("       TCMC.COST_TYPE,\n");  
		sql.append("       TO_CHAR(TCMC.ADV_DATE,'YYYY-MM-DD') ADV_DATE,\n");
		sql.append("       TO_CHAR(TCMC.END_DATE,'YYYY-MM-DD') END_DATE,\n");  
		sql.append("       TCMC.REGION,\n");  
		sql.append("       TCMC.MEDIA_MODEL,\n");  
		sql.append("       TCMC.MEDIA_NAME,\n");  
		sql.append("       TCMC.MEDIA_PUBLISH,\n");  
		sql.append("       TCMC.MEDIA_SIZE,\n");  
		sql.append("       TCMC.MEDIA_COLUMN,\n");  
		sql.append("       TMR.REGION_NAME,\n");  
		sql.append("       NVL(TCMC.PAYMENT_ACCOUNT,0) PAYMENT_ACCOUNT,\n" );
		sql.append("       NVL(TCMC.ITEM_PRICE,0) ITEM_PRICE,\n" );
		sql.append("       NVL(TCMC.ITEM_COUNT,0) ITEM_COUNT,\n" );
		sql.append("       NVL(TCMC.REAL_COST,0) REAL_COST,\n" );
		sql.append("       NVL(TCMC.ITEM_COST,0) ITEM_COST,\n");
		sql.append("       NVL(TCMC.PLAN_COST,0) PLAN_COST,\n" );
		sql.append("       NVL(TCMC.TOTAL_COUNT,0) TOTAL_COUNT,\n");  
		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id= "+dealer_id+" AND cost_type = "+Constant.COST_TYPE_01+" and cost_source = "+Constant.COST_SOURCE_01+"AND AREA_ID="+areaId+"),0) DEALER_COST, \n" );                    
		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id IS NULL AND v.org_id = "+area_org_id+" and cost_type = "+Constant.COST_TYPE_01+" and cost_source = "+Constant.COST_SOURCE_02+" AND AREA_ID="+areaId+"),0) AREA_COST, \n" );
 		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id IS NULL AND v.org_id = "+oem_org_id+" and cost_type = "+Constant.COST_TYPE_01+" and cost_source = "+Constant.COST_SOURCE_03+" AND AREA_ID="+areaId+"),0) OEMPR_COST,\n" );
 		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id IS NULL AND v.org_id = "+oem_org_id+" and cost_type = "+Constant.COST_TYPE_02+" and cost_source = "+Constant.COST_SOURCE_03+" AND AREA_ID="+areaId+"),0) OEMAD_COST \n" );

		sql.append("  FROM TT_CAM_MEDIA_COST TCMC, TM_REGION TMR\n" );
		sql.append(" WHERE TCMC.EXECUTE_ID ="+executeId+"\n");
		sql.append(" AND TCMC.REGION = TMR.REGION_ID(+) \n");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	
//	public List<Map<String, Object>> getActivitiesMediaCost(String executeId,String dealer_id,String area_org_id,String oem_org_id){
//		StringBuffer sql= new StringBuffer();
//		sql.append("SELECT TCMC.COST_ID,\n" );
//		sql.append("       TCMC.COST_TYPE,\n" );
//		sql.append("       TCMC.ADV_SUBJECT,\n" );
//		sql.append("       TO_CHAR(TCMC.ADV_DATE,'YYYY-MM-DD') ADV_DATE,\n" );
//		sql.append("       TCMC.ADV_MEDIA,\n" );
//		sql.append("       TCMC.REMARK,\n" );
//		sql.append("       NVL(TCMC.PAYMENT_ACCOUNT,0) PAYMENT_ACCOUNT,\n" );
//		sql.append("       NVL(TCMC.ITEM_PRICE,0) ITEM_PRICE,\n" );
//		sql.append("       NVL(TCMC.ITEM_COUNT,0) ITEM_COUNT,\n" );
//		sql.append("       NVL(TCMC.REAL_COST,0) REAL_COST,\n" );
//		sql.append("       NVL(TCMC.ITEM_COST,0) ITEM_COST,\n");
//		sql.append("       NVL(TCMC.PLAN_COST,0) PLAN_COST,\n" );
//		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id= "+dealer_id+" AND cost_type = 11291001 and cost_source = "+Constant.COST_SOURCE_01+"),0) DEALER_COST, \n" );                    
//		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id IS NULL AND v.org_id = "+area_org_id+" and cost_type = "+Constant.COST_TYPE_01+" and cost_source = "+Constant.COST_SOURCE_02+"),0) AREA_COST, \n" );
//		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id IS NULL AND v.org_id = "+oem_org_id+" and cost_type = "+Constant.COST_TYPE_01+" and cost_source = "+Constant.COST_SOURCE_03+"),0) OEMPR_COST,\n" );
//		sql.append("       NVL((select AVAILABLE_AMOUNT  from tt_vs_cost v WHERE v.dealer_id IS NULL AND v.org_id = "+oem_org_id+" and cost_type = "+Constant.COST_TYPE_02+" and cost_source = "+Constant.COST_SOURCE_03+"),0) OEMAD_COST \n" );
//		sql.append("  FROM TT_CAM_MEDIA_COST TCMC\n" );
//		sql.append(" WHERE TCMC.EXECUTE_ID ="+executeId+"\n");
//		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
//		return list;
//	}
	
	public List<Map<String, Object>> getActivitiesMediaCost(String executeId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TCMC.COST_ID,\n" );
		sql.append("       TCMC.COST_TYPE,\n" );
		sql.append("       TCMC.MEDIA_TYPE,\n" );
		sql.append("       TCMC.ADV_SUBJECT,\n" );
		sql.append("       TO_CHAR(TCMC.ADV_DATE,'YYYY-MM-DD') ADV_DATE,\n" );
		sql.append("       TO_CHAR(TCMC.END_DATE,'YYYY-MM-DD') END_DATE,\n" );
		sql.append("       TCMC.ADV_MEDIA,\n" );
		sql.append("       TCMC.REMARK,\n" );
		sql.append("       TCMC.PAYMENT_ACCOUNT,\n" );
		sql.append("       TCMC.ITEM_PRICE,\n" );
		sql.append("       TCMC.ITEM_COUNT,\n" );
		sql.append("       TCMC.REAL_COST,\n" );
		sql.append("       TCMC.ITEM_COST,");
		sql.append("       TCMC.PLAN_COST\n" );
		sql.append("  FROM TT_CAM_MEDIA_COST TCMC\n" );
		sql.append(" WHERE TCMC.EXECUTE_ID ="+executeId+"\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	/**
	 * 根据执行ID查询方案审核信息
	 * @param  :执行ID
	 * @return :审核信息
	 */
	public List<Map<String, Object>> getCheckInfo(String executeId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TCEC.CHECK_ID,\n" );
		sql.append("       TO_CHAR(TCEC.CHECK_DATE, 'YYYY-MM-DD HH24:MI:SS') CHECK_DATE,\n" );
		sql.append("       TCEC.CHECK_DESC,\n" );
		sql.append("       TCEC.CHECK_STATUS,\n" );
		sql.append("       TCU.NAME,\n" );
		sql.append("       TMO.ORG_NAME,\n" );
		sql.append("       FSF.FJID,\n" );
		sql.append("       FSF.FILEURL,\n" );
		sql.append("       FSF.FILENAME,\n" );
		sql.append("       A.POSE_NAME\n" );
		sql.append("  FROM TT_CAMPAIGN_EXECUTE_CHK TCEC,\n" );
		sql.append("       TM_ORG                  TMO,\n" );
		sql.append("       TC_USER                 TCU,\n" );
		sql.append("       FS_FILEUPLOAD           FSF,\n" );
		sql.append("       TC_POSE                 A\n" );
		sql.append(" WHERE TCEC.CHECK_ORG_ID = TMO.ORG_ID\n" );
		sql.append("   AND TCEC.CHECK_USER_ID = TCU.USER_ID\n" );
		sql.append("   AND TCEC.CHECK_POSITION_ID = A.POSE_ID(+)\n" );
		sql.append("   AND TCEC.CHECK_ID = FSF.YWZJ(+)\n" );
		sql.append("   AND TCEC.EXECUTE_ID = ").append(executeId).append("\n");
		sql.append(" ORDER BY TCEC.CHECK_DATE ASC\n");


		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	/**
	 * 根据执行ID查询总结审核信息
	 * @param  :执行ID
	 * @return :审核信息
	 */
	public List<Map<String, Object>> getZJCheckInfo(String executeId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TCSC.CHECK_ID,\n" );
		sql.append("       TO_CHAR(TCSC.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE,\n" );
		sql.append("       TCSC.CHECK_DESC,\n" );
		sql.append("       TCSC.CHECK_STATUS,\n" );
		sql.append("       TCU.NAME,\n" );
		sql.append("       TMO.ORG_NAME,\n" );
		sql.append("       FSF.FJID,\n" );
		sql.append("       FSF.FILEURL,\n" );
		sql.append("       FSF.FILENAME\n" );
		sql.append("  FROM TT_CAMPAIGN_SUMMERY_CHK TCSC,\n" );
		sql.append("       TM_ORG                  TMO,\n" );
		sql.append("       TC_USER                 TCU,\n" );
		sql.append("       FS_FILEUPLOAD           FSF\n" );
		sql.append(" WHERE TCSC.CHECK_ORG_ID = TMO.ORG_ID\n" );
		sql.append("   AND TCSC.CHECK_USER_ID = TCU.USER_ID\n" );
		sql.append("   AND TCSC.CHECK_ID = FSF.YWZJ(+)\n" );
		sql.append("   AND TCSC.EXECUTE_ID ="+executeId+"\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	/**
	 * 根据执行ID查询附件信息
	 * @param  :执行ID
	 * @return :附件信息
	 */
	public List<Map<String, Object>> getAttachInfo(String executeId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT FSF.FJID,FSF.FILEURL,FSF.FILENAME FROM FS_FILEUPLOAD FSF WHERE FSF.YWZJ ="+executeId+"\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	/**
	 * Function         : OEM活动信息查询
	 * @param           : 方案编号
	 * @param           : 方案名称
	 * @param           : 活动主题
	 * @param           : 活动类别
	 * @param           : 活动时间
	 * @param           : 活动方案状态
	 * @param           : 经销商CODE
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-04
	 */
	public PageResult<Map<String, Object>> activitiesQuery(String staReportDate, String endReportDate,Integer isFleet, String priceType, String orgIdR, String isPrint, String areaId, Long poseId, String dealerIds,String orgIds,String campaignNo,String campaignName,String campaignSubject,String campaignType,String startDate,
			String endDate,String checkStatus,String dealerCode,int curPage,int pageSize) throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT * FROM (\n" );
		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
		sql.append(" TTC.CREATE_DATE,\n");
		sql.append("TTC.CAMPAIGN_NO,\n" );
		sql.append("TTC.CAMPAIGN_NAME,\n" );
		sql.append("TTC.CAMPAIGN_TYPE,\n" );
		sql.append("TTC.CAMPAIGN_SUBJECT,\n" );
		sql.append("TMO.ORG_NAME ORG_NAME,\n" );
		sql.append("TMO.ORG_ID,\n" );
		sql.append("TCSE.SPACE_ID EXECUTE_ID,\n" );
		sql.append("'--' DEALER_NAME,\n" );
		sql.append("'--' DEALER_CODE,\n" );
		sql.append("0 DEALER_ID,\n" );
		sql.append("TCSE.CHECK_STATUS,\n" );
		sql.append("TCSE.price_type,\n" );
		sql.append("TTC.AREA_ID,\n");
		sql.append("TO_CHAR(tcse.submits_date,'YYYY-MM-DD') submits_date,\n");
		sql.append("tcu.name,\n");
		sql.append("nvl(tcse.is_print, ").append(Constant.IF_TYPE_NO).append(") is_print,\n");
		sql.append("TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
		sql.append("TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE\n" );
		sql.append("FROM TT_CAMPAIGN TTC, TT_CAMPAIGN_SPACE_EXECUTE TCSE,TM_ORG TMO, tc_user tcu, TM_POSE_BUSINESS_AREA TMPBA\n" );
		sql.append("WHERE  TTC.CAMPAIGN_ID = TCSE.CAMPAIGN_ID\n" );
		sql.append("AND TTC.AREA_ID = TMPBA.AREA_ID\n");
		sql.append("and tcu.user_id = tcse.create_by\n");
		sql.append("AND TMPBA.POSE_ID = " + poseId + "\n");
		sql.append("AND TCSE.ORG_ID =TMO.ORG_ID  ");
		sql.append("AND TCSE.CHECK_STATUS NOT IN("+Constant.CAMPAIGN_CHECK_STATUS_01+","+Constant.CAMPAIGN_CHECK_STATUS_12+","+ Constant.CAMPAIGN_CHECK_STATUS_11 +")\n" );
		sql.append("   AND TTC.CAMPAIGN_TYPE IN (").append(Constant.CAMPAIGN_TYPE_03).append(")\n");
		
		if(Constant.STATUS_ENABLE.intValue() == isFleet.intValue()) {
			sql.append("   AND TCSE.IS_FLEET = 0\n" );
		}
		
		sql.append("UNION ALL\n" );
		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
		sql.append(" TTC.CREATE_DATE,\n");
		sql.append("TTC.CAMPAIGN_NO,\n" );
		sql.append("TTC.CAMPAIGN_NAME,\n" );
		sql.append("TTC.CAMPAIGN_TYPE,\n" );
		sql.append("TTC.CAMPAIGN_SUBJECT,\n" );
		sql.append("'--' ORG_NAME,\n" );
		sql.append("TMO.ORG_ID,\n" );
		sql.append("TCSE.SPACE_ID EXECUTE_ID,\n" );
		sql.append("'--' DEALER_NAME,\n" );
		sql.append("'--' DEALER_CODE,\n" );
		sql.append("0 DEALER_ID,\n" );
		sql.append("TCSE.CHECK_STATUS,\n" );
		sql.append("TCSE.price_type,\n" );
		sql.append("TTC.AREA_ID,\n");
		sql.append("TO_CHAR(tcse.submits_date,'YYYY-MM-DD') submits_date,\n");
		sql.append("tcu.name,\n");
		sql.append("nvl(tcse.is_print, ").append(Constant.IF_TYPE_NO).append(") is_print,\n");
		sql.append("TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
		sql.append("TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE\n" );
		sql.append("FROM TT_CAMPAIGN TTC, TT_CAMPAIGN_SPACE_EXECUTE TCSE,TM_ORG TMO, tc_user tcu, TM_POSE_BUSINESS_AREA TMPBA\n" );
		sql.append("WHERE  TTC.CAMPAIGN_ID = TCSE.CAMPAIGN_ID\n" );
		sql.append("AND TTC.AREA_ID = TMPBA.AREA_ID\n");
		sql.append("and tcu.user_id = tcse.create_by\n");
		sql.append("AND TMPBA.POSE_ID = " + poseId + "\n");
		sql.append("AND TCSE.ORG_ID =TMO.ORG_ID  ");
		sql.append("AND TCSE.ORG_ID =  "+orgIds);
		sql.append("AND TCSE.CHECK_STATUS NOT IN("+Constant.CAMPAIGN_CHECK_STATUS_01+","+Constant.CAMPAIGN_CHECK_STATUS_12+")\n" );
		sql.append("   AND TTC.CAMPAIGN_TYPE IN (").append(Constant.CAMPAIGN_TYPE_01).append(", ").append(Constant.CAMPAIGN_TYPE_02).append(")\n");
		sql.append("AND TTC.CAMPAIGN_ID NOT IN (SELECT TTCE1.CAMPAIGN_ID FROM TT_CAMPAIGN_EXECUTE TTCE1)\n");
		
		if(Constant.STATUS_ENABLE.intValue() == isFleet.intValue()) {
			sql.append("   AND TCSE.IS_FLEET = 0\n" );
		}
		
		sql.append("UNION ALL\n" );
		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
		sql.append(" TTC.CREATE_DATE,\n");
		sql.append("TTC.CAMPAIGN_NO,\n" );
		sql.append("TTC.CAMPAIGN_NAME,\n" );
		sql.append("TTC.CAMPAIGN_TYPE,\n" );
		sql.append("TTC.CAMPAIGN_SUBJECT,\n" );
		sql.append("TMO.ORG_NAME ORG_NAME,\n" );
		sql.append("TMO.ORG_ID ORG_ID,\n" );
		sql.append("TCE.EXECUTE_ID,\n" );
		sql.append("TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("TMD.DEALER_CODE DEALER_CODE,\n" );
		sql.append("TMD.DEALER_ID DEALER_ID,\n" );
		sql.append("TCE.CHECK_STATUS,\n" );
		sql.append("TCE.price_type,\n" );
		sql.append("TTC.AREA_ID,\n");
		sql.append("TO_CHAR(tce.submits_date,'YYYY-MM-DD') submits_date,\n");
		sql.append("tcu.name,\n");
		sql.append("nvl(tce.is_print, ").append(Constant.IF_TYPE_NO).append(") is_print,\n");
		sql.append("TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
		sql.append("TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE\n" );
		sql.append("FROM TT_CAMPAIGN TTC, TT_CAMPAIGN_EXECUTE TCE,TM_DEALER TMD,vw_org_dealer VOD,TM_ORG TMO, tc_user tcu, TM_POSE_BUSINESS_AREA TMPBA\n" );
		sql.append("WHERE TTC.CAMPAIGN_ID=TCE.CAMPAIGN_ID\n" );
		sql.append("AND TTC.AREA_ID = TMPBA.AREA_ID\n");
		sql.append("and tcu.user_id = tce.create_by\n");
		sql.append("AND TMPBA.POSE_ID = " + poseId + "\n");
		sql.append("AND TCE.DEALER_ID=TMD.DEALER_ID\n" );
		sql.append("AND TMO.ORG_ID = VOD.ROOT_ORG_ID\n" );
		sql.append("AND VOD.DEALER_ID=TMD.DEALER_ID\n" );
		sql.append("   AND TTC.CAMPAIGN_TYPE IN (").append(Constant.CAMPAIGN_TYPE_01).append(", ").append(Constant.CAMPAIGN_TYPE_02).append(")\n");
		
		if(Constant.STATUS_ENABLE.intValue() == isFleet.intValue()) {
			sql.append("   AND TCE.IS_FLEET = 0\n" );
		}
		
		sql.append("AND TCE.CHECK_STATUS NOT IN(" + Constant.CAMPAIGN_CHECK_STATUS_01 + "," + Constant.CAMPAIGN_CHECK_STATUS_11 + ")) allcam\n" );
		sql.append("WHERE 1=1");
//		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
//		sql.append("       TTC.CAMPAIGN_NO,\n" );
//		sql.append("       TTC.CAMPAIGN_NAME,\n" );
//		sql.append("       TTC.CAMPAIGN_TYPE,\n" );
//		sql.append("       TTC.CAMPAIGN_SUBJECT,\n" );
//		sql.append("       TCE.SPACE_ID,\n" );
//		sql.append("       TCE.CHECK_STATUS,\n" );
//		sql.append("       TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
//		sql.append("       TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE\n" );
//		sql.append("  FROM TT_CAMPAIGN TTC, TT_CAMPAIGN_SPACE_EXECUTE TCE \n" );
//		sql.append(" WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n" );
//		sql.append("   AND TCE.ORG_ID IN( "+orgIds+")");
//		sql.append("   AND TCE.CHECK_STATUS NOT IN("+Constant.CAMPAIGN_CHECK_STATUS_01+","+Constant.CAMPAIGN_CHECK_STATUS_12+")\n" );
		if(!"-1".equals(checkStatus)&&!"".equals(checkStatus)&&checkStatus!=null){
			sql.append("  AND allcam.CHECK_STATUS = "+checkStatus+"\n");
		}
		if(!"".equals(campaignNo)&&campaignNo!=null){
			sql.append("AND allcam.CAMPAIGN_NO LIKE'%"+campaignNo+"%'\n");
		}
		if(!"".equals(campaignName)&&campaignName!=null){
			sql.append("AND allcam.CAMPAIGN_NAME LIKE'%"+campaignName+"%'\n");
		}
		if(!"".equals(campaignSubject)&&campaignSubject!=null){
			sql.append("AND allcam.CAMPAIGN_SUBJECT LIKE'%"+campaignSubject+"%'\n");
		}
		if(!"-1".equals(campaignType)&&!"".equals(campaignType)&&campaignType!=null){
			sql.append("AND allcam.CAMPAIGN_TYPE ="+campaignType+"\n");
		}
		
		if(!"-1".equals(priceType)&&!"".equals(priceType)&&priceType!=null){
			sql.append("AND allcam.price_type ="+priceType+"\n");
		}
		
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND (to_date(allcam.START_DATE,'yyyy-mm-dd') >= to_date('"+startDate+"','yyyy-mm-dd')").append("   or (to_date(allcam.START_DATE,'yyyy-mm-dd') < to_date('"+startDate+"','yyyy-mm-dd')").append("   AND to_date(allcam.end_date,'yyyy-mm-dd') >= to_date('"+startDate+"','yyyy-mm-dd')))\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   and (to_date(allcam.END_DATE,'yyyy-mm-dd') <= to_date('"+endDate+"','yyyy-mm-dd')").append("   or (to_date(allcam.END_DATE,'yyyy-mm-dd') > to_date('"+endDate+"','yyyy-mm-dd')").append("   and to_date(allcam.start_date,'yyyy-mm-dd') <= to_date('"+endDate+"','yyyy-mm-dd')))\n"); //YH 2011.6.3
		}
		if (!"".equals(dealerCode)&&null!=dealerCode){
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "allcam", "DEALER_CODE"));
		}
		
		if (!CommonUtils.isNullString(areaId)) {
			sql.append("AND allcam.AREA_ID = ").append(areaId).append("\n");
		}
		
		if (!CommonUtils.isNullString(staReportDate)){
			sql.append("AND allcam.SUBMITS_DATE >='").append(staReportDate).append("'\n");
		}
		
		if (!CommonUtils.isNullString(endReportDate)){
			sql.append("AND allcam.SUBMITS_DATE <='").append(endReportDate).append("'\n");
		}
		
		if (!CommonUtils.isNullString(orgIdR)) {
			sql.append("AND allcam.org_id in (").append(orgIdR).append(")\n");
		}
		
		if (!CommonUtils.isNullString(isPrint)) {
			sql.append("AND allcam.is_print = ").append(isPrint).append("\n");
		}
		sql.append("ORDER BY allcam.CREATE_DATE DESC, allcam.CAMPAIGN_ID\n"); 
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 区域活动信息查询
	 * @param           : 方案编号
	 * @param           : 方案名称
	 * @param           : 活动主题
	 * @param           : 活动类别
	 * @param           : 活动时间
	 * @param           : 活动方案状态
	 * @param           : 经销商CODE
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-04
	 */
	public PageResult<Map<String, Object>> activitiesAreaQuery(Map<String,String> paraMap,int curPage,int pageSize) throws Exception{
		List<Object> params = new LinkedList<Object>();

		StringBuffer sql= new StringBuffer("\n");
		String orgId = paraMap.get("orgId");
		String campaignNo = paraMap.get("campaignNo");
		String campaignName = paraMap.get("campaignName");
		String campaignSubject = paraMap.get("campaignSubject");
		String startDate = paraMap.get("startDate");
		String endDate = paraMap.get("endDate");

		sql.append("SELECT TTC.CAMPAIGN_ID,\n");
		sql.append("     TTC.CAMPAIGN_NO,\n");
		sql.append("     TTC.CAMPAIGN_NAME,\n");
		sql.append("     TTC.CAMPAIGN_TYPE,\n");
		sql.append("     TTC.CAMPAIGN_SUBJECT,\n");
		sql.append("     TCSE.ORG_ID,\n");
		sql.append("     TMO.ORG_NAME,\n");
		sql.append("     TCSE.SPACE_ID,\n");
		sql.append("     TCSE.CHECK_STATUS,\n");
		sql.append("     TO_CHAR(TTC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");
		sql.append("     TO_CHAR(TTC.END_DATE, 'YYYY-MM-DD') END_DATE,\n");
		sql.append("     TO_CHAR(TCSE.SUBMITS_DATE, 'YYYY-MM-DD') SUBMITS_DATE\n");
		sql.append("FROM TT_CAMPAIGN               TTC,\n");
		sql.append("     TT_CAMPAIGN_SPACE_EXECUTE TCSE,\n");
		sql.append("     TM_ORG          TMO\n");
		sql.append("WHERE TTC.CAMPAIGN_ID = TCSE.CAMPAIGN_ID\n");
		sql.append(" AND TCSE.ORG_ID = TMO.ORG_ID\n");
		sql.append(" AND TCSE.ORG_ID ="+orgId+"\n");
		sql.append(" AND TCSE.CHECK_STATUS IN ("+Constant.CAMPAIGN_CHECK_STATUS_02+","+Constant.CAMPAIGN_CHECK_STATUS_03+","+Constant.CAMPAIGN_CHECK_STATUS_04+","+Constant.CAMPAIGN_CHECK_STATUS_05+")\n");

		if (!CommonUtils.isNullString(campaignNo)) {
			sql.append(" AND TTC.CAMPAIGN_NO LIKE '%").append(campaignNo).append("%'\n");
		}

		if (!CommonUtils.isNullString(campaignName)) {
			sql.append(" AND TTC.CAMPAIGN_NAME LIKE '%").append(campaignName).append("%'\n");
		}

		if (!CommonUtils.isNullString(campaignSubject)) {
			sql.append(" AND TTC.CAMPAIGN_SUBJECT LIKE '%").append(campaignSubject).append("%'\n");
		}

		if (!CommonUtils.isNullString(startDate)) {
			sql.append(" AND TRUNC(TTC.START_DATE) >= TO_DATE('").append(startDate).append("','yyyy-mm-dd')\n");
		}

		if (!CommonUtils.isNullString(endDate)) {
			sql.append(" AND TRUNC(TTC.END_DATE) <= TO_DATE('").append(endDate).append("','yyyy-mm-dd')\n");
		}

		sql.append("ORDER BY TTC.CREATE_DATE DESC, TCSE.CHECK_STATUS\n");



		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
    }
	
	/**
	 * 活动执行方案新增
	 * @param po
	 */
	public void addExecutePlan(TtCampaignSpaceExecutePO po){
		dao.insert(po);
	}
	
	
	/**
	 * 活动执行方案更新
	 * @param po1
	 * @param po2
	 */
	public void updateExecutePlan(TtCampaignSpaceExecutePO po1,TtCampaignSpaceExecutePO po2){
		dao.update(po1, po2);
	}
	
	/**
	 * 活动执行方案删除
	 * @param po
	 */
	public void delExecutePlan(TtCampaignSpaceExecutePO po){
		dao.delete(po);
	}
	
	/**
	 * 更新活动方案
	 * @param po1
	 * @param po2
	 */
	public void updateCampaignPlan(TtCampaignPO po1,TtCampaignPO po2){
		dao.update(po1, po2);
	}
	
	
	/**
	 * 通过活动方案id查询活动方案信息
	 * @param campaignId
	 * @return
	 */
	public Map<String, Object> getCompaignPlanById(String campaignId){
		StringBuffer sql= new StringBuffer();
		//原有逻辑：
//		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
//		sql.append("       TTC.CAMPAIGN_MODEL,\n" );
//		sql.append("       TCP.PROD_NAME,\n" );
//		sql.append("       TTC.GROUP_ID,\n" );
//		sql.append("       TVMG.GROUP_NAME,\n" );
//		sql.append("       TCP.PROD_NAME,\n" );
//		sql.append("       TTC.CAMPAIGN_NO,\n" );
//		sql.append("       TTC.CAMPAIGN_NAME,\n" );
//		sql.append("       TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
//		sql.append("       TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE,\n" );
//		sql.append("       TTC.CAMPAIGN_SUBJECT,\n" );
//		sql.append("       TTC.CAMPAIGN_OBJECT,\n" );
//		sql.append("       TTC.CAMPAIGN_PURPOSE,\n" );
//		sql.append("       TTC.CAMPAIGN_NEED,\n" );
//		sql.append("       TTC.CAMPAIGN_DESC,\n" );
//		sql.append("       TTC.CAMPAIGN_TYPE\n" );
//		sql.append(" FROM TT_CAMPAIGN TTC,TM_CAMPAIGN_PROD TCP,TM_VHCL_MATERIAL_GROUP TVMG\n" );
//		sql.append(" WHERE TTC.CAMPAIGN_MODEL = TCP.PROD_ID\n" );
//		sql.append(" AND TVMG.GROUP_ID = TTC.GROUP_ID\n" );
//		sql.append(" AND TTC.CAMPAIGN_ID =");
//		sql.append(campaignId);
//		sql.append("\n");
		
		//长安汽车：
		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
		sql.append("       TTC.CAMPAIGN_NO,\n" );
		sql.append("       TTC.CAMPAIGN_NAME,\n" );
		sql.append("       TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
		sql.append("       TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE,\n" );
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n" );
		sql.append("       TTC.CAMPAIGN_OBJECT,\n" );
		sql.append("       TTC.CAMPAIGN_PURPOSE,\n" );
		sql.append("       TTC.CAMPAIGN_NEED,\n" );
		sql.append("       TTC.CAMPAIGN_DESC,\n" );
		sql.append("       TTC.CAMPAIGN_TYPE,\n" );
		sql.append("	   TTC.AREA_ID,\n");
		sql.append("	   TTC.TO_PLACE_COUNT,\n");
		sql.append("	   TTC.TO_TEL_STORE_COUNT,\n");
		sql.append("	   TTC.CREATE_CARDS_COUNT,\n");
		sql.append("	   TTC.ORDER_COUNT,\n");
		sql.append("	   TTC.TURN_CAR_COUNT\n");
		sql.append(" FROM TT_CAMPAIGN TTC\n" );
		sql.append(" WHERE TTC.CAMPAIGN_ID =");
		sql.append(campaignId);
		sql.append("\n");

		Map<String, Object> rs1 = dao.pageQueryMap(sql.toString(), null, getFunName());
		Map<String, Object> rs  = getModelByCampaignId(rs1);
		return rs;
	}
	
	
	private  Map<String, Object> getModelByCampaignId(Map<String, Object> rs) {
		if (rs != null && !"".equals(rs.get("CAMPAIGN_ID"))) {
			String modelId = "";
			String modelCode = "";
			String groupName = "";
			StringBuffer sql= new StringBuffer();
			sql.append("select r.relation_id, r.campaign_id, r.campaign_model,t.group_code,t.group_name\n" );
			sql.append("  from tt_campaign_group_r r, tm_vhcl_material_group t\n" );
			sql.append(" where r.campaign_model = t.group_id\n");
			sql.append(" and r.campaign_id = "+rs.get("CAMPAIGN_ID")+" \n");
			
			List<Map<String, Object>> modelList = dao.pageQuery(sql.toString(), null,getFunName());
			for (int j = 0; j < modelList.size(); j++) {
				modelId += modelList.get(j).get("CAMPAIGN_MODEL") + ",";
				modelCode += modelList.get(j).get("GROUP_CODE") + ",";
				groupName += modelList.get(j).get("GROUP_NAME") + ",";
			}
			if (!"".equals(modelId) && modelId.length() > 0) {
				modelId = modelId.substring(0, modelId.length() - 1);
			}
			if (!"".equals(modelCode) && modelCode.length() > 0) {
				modelCode = modelCode.substring(0, modelCode.length() - 1);
			}
            if (!"".equals(groupName) && groupName.length() > 0) {
				groupName = groupName.substring(0, groupName.length() - 1);
			}
			rs.put("MODELID", modelId);//车型ID
			rs.put("MODELCODE", modelCode);//车型代码
			rs.put("GROUPNAME", groupName);//车型代码
		}
		return rs;
	}
	
	
	/**
	 * 通过活动方案id找到与之相关联的执行方案
	 * @param campaignId
	 * @return
	 */
	public List<Map<String, Object>> getExecutePlanByCmpId(String campaignId,String orgId){
		StringBuffer sql= new StringBuffer();
		
		sql.append("SELECT *\n") ;
		sql.append("  FROM TT_CAMPAIGN_SPACE_EXECUTE TCSE, TM_ORG TMO\n") ;
		sql.append(" WHERE TCSE.ORG_ID = TMO.ORG_ID\n") ;
		sql.append("   AND TCSE.CAMPAIGN_ID='"+campaignId+"'") ;
		
		if(orgId!=""&&orgId!=null){
			sql.append(" AND TCSE.ORG_ID IN ("+orgId+")");
		}
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	
	/**
	 * 通过业务主键取得附件信息
	 * @param
	 * @return
	 */
	public List<Map<String, Object>>  getAttachInfos(String ywzjs){
		
		String sql = "SELECT F.FJID,F.FILEURL,F.FILENAME FROM FS_FILEUPLOAD F WHERE F.YWZJ ="+ywzjs+"ORDER BY F.FJID";

		List<Map<String, Object>> list = pageQuery(sql,null,getFunName());
		return list;
	}
	/**
	 * 通过经销商id获取经销商所在区域id
	 * @param dealerId
	 * @return
	 */
	public  TmDealerOrgRelationPO  getAreaOrgIdByDealerId(String dealerId){
		TmDealerOrgRelationPO tdo = new TmDealerOrgRelationPO();
		 List<Object> params = new LinkedList();
			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT * FROM TM_DEALER_ORG_RELATION t\n");
			sql.append("  WHERE  1=1 ");
			if (dealerId!=null&&!("").equals(dealerId)){
				sql.append(" AND t.DEALER_ID = ?  \n");
				params.add(dealerId);
			}
			List list = dao.select(TmDealerOrgRelationPO.class, sql.toString(), params);
			if (!CommonUtils.isNullList(list)){
				if (list.size()>0) {
					tdo = (TmDealerOrgRelationPO) list.get(0);
				}
			} else {
				String sqlStr = ("select * from vw_org_dealer vod where vod.dealer_id = " + dealerId + "") ;
				
				List<Map<String, Object>> myList = dao.pageQuery(sqlStr.toLowerCase(), null, dao.getFunName()) ;
				
				tdo.setOrgId(Long.parseLong(myList.get(0).get("ROOT_ORG_ID").toString())) ;
			}
		return tdo;
	}
	public  List<Map<String,Object>>  queryDealerCost(String dealerId, String areaId){
		StringBuilder sql = new StringBuilder();
		sql.append("  select COST_ID,DEALER_ID,FREEZE_AMOUNT,AVAILABLE_AMOUNT  from tt_vs_cost v   \n");
		sql.append("  WHERE  1=1 ");
		if (dealerId!=null&&!("").equals(dealerId)){
			sql.append(" AND v.DEALER_ID IN("+dealerId+")\n");
		}
		if (areaId != null && !("").equals(areaId)){
			sql.append(" AND v.AREA_ID IN("+areaId+")\n");
		}
		sql.append("    AND v.COST_TYPE = "+Constant.COST_TYPE_07+"\n" );
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public  List<Map<String,Object>>  queryAreaCost(String dealerId,String areaId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVC.COST_ID, TMO.ORG_ID, TVC.FREEZE_AMOUNT,TVC.AVAILABLE_AMOUNT\n" );
		sql.append("   FROM TT_VS_COST TVC, TM_DEALER_ORG_RELATION TDOR, TM_ORG TMO,tm_org_business_area toba\n" );
		sql.append("  WHERE TVC.ORG_ID = TDOR.ORG_ID\n" );
		sql.append("    AND TDOR.ORG_ID = TMO.ORG_ID\n" );
		sql.append("    AND TVC.COST_TYPE = "+Constant.COST_TYPE_02+"\n" );
		sql.append("    AND TDOR.DEALER_ID IN ("+dealerId+")\n");
		sql.append("	AND TOBA.ORG_ID=TMO.ORG_ID\n");
		sql.append(" AND TOBA.AREA_ID=TVC.AREA_ID");
		sql.append("	AND TOBA.AREA_ID="+areaId+"\n");
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	
	public  List<Map<String,Object>>  queryOemCost(Long orgId,String areaId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVC.COST_ID,TVC.COST_TYPE, TMO.ORG_ID,TVC.FREEZE_AMOUNT,TVC.AVAILABLE_AMOUNT\n" );
		sql.append("   FROM TT_VS_COST TVC,TM_ORG TMO\n" );
		sql.append("  WHERE TVC.ORG_ID = TMO.ORG_ID\n" );
		sql.append("AND TMO.ORG_ID="+orgId);
		sql.append("    AND TVC.COST_SOURCE = "+Constant.COST_SOURCE_03+"\n" );
		sql.append(" AND  TVC.AREA_ID="+areaId);
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * Function         : 方案信息查询
	 * @param           : 方案ID
	 * @return          : 方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-07-05
	 */
	public Map<String, Object> activitiesInfoQuery(String campaignId,String executeId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
		sql.append("       TTC.CAMPAIGN_MODEL,\n" );
		sql.append("       TVMG.GROUP_NAME,\n" );
		sql.append("       TTC.AREA_ID,\n" );
//		sql.append("       TCP.PROD_NAME,\n" );
		sql.append("       TTC.CAMPAIGN_NO,\n" );
		sql.append("       TTC.CAMPAIGN_NAME,\n" );
		sql.append("       TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
		sql.append("       TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE,\n" );
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n" );
		sql.append("       TTC.CAMPAIGN_OBJECT,\n" );
		sql.append("       TTC.CAMPAIGN_PURPOSE,\n" );
		sql.append("       TTC.CAMPAIGN_NEED,\n" );
		sql.append("       TTC.CAMPAIGN_DESC,\n" );
		sql.append("       TTC.CAMPAIGN_TYPE,\n" );
		sql.append("       TCE.DEALER_ID,\n" );
		sql.append("       TMD.DEALER_CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       TCE.EXEC_ADD_DESC,\n" );
		sql.append("       TCE.EVALUATE_DESC,\n" );
		sql.append("       TCE.ADVICE_DESC,\n" );
		sql.append("       TCE.EXECUTE_ID\n" );
		sql.append(" FROM TT_CAMPAIGN TTC,TT_CAMPAIGN_EXECUTE TCE," +
//				"TM_CAMPAIGN_PROD TCP," +
				"TM_DEALER TMD,TM_VHCL_MATERIAL_GROUP TVMG\n" );
		sql.append(" WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n");
//		sql.append(" AND TTC.CAMPAIGN_MODEL = TCP.PROD_ID\n");
		sql.append(" AND TCE.DEALER_ID = TMD.DEALER_ID\n");
		sql.append(" AND TVMG.GROUP_ID = TTC.GROUP_ID\n");
		if(!"".equals(campaignId)&&campaignId!=null){
			sql.append(" AND TTC.CAMPAIGN_ID ="+campaignId+"\n");
		}
		if(!"".equals(executeId)&&executeId!=null){
			sql.append(" AND TCE.EXECUTE_ID ="+executeId+"\n");
		}
		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, getFunName());
		return rs;
	}
	/**
	 * 总部审核方案为大客户审核时,调用该方法sql
	 * @param poseId
	 * @param campaignNo
	 * @param campaignName
	 * @param campaignSubject
	 * @param campaignType
	 * @param startDate
	 * @param endDate
	 * @param checkStatus
	 * @param dealerCode
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> activitiesQuery(Long poseId, String campaignNo,String campaignName,String campaignSubject,String campaignType,String startDate,
			String endDate,String checkStatus,String dealerCode,int curPage,int pageSize) throws Exception{
			
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		
		sql.append("SELECT * FROM (\n" );
		sql.append("	SELECT TTC.CAMPAIGN_ID,\n" );
		sql.append("	   		 TTC.CREATE_DATE,\n");
		sql.append("	       TTC.CAMPAIGN_NO,\n" );
		sql.append("	       TTC.CAMPAIGN_NAME,\n" );
		sql.append("	   	   TTC.CAMPAIGN_TYPE,\n" );
		sql.append("	   	   TTC.CAMPAIGN_SUBJECT,\n" );
		sql.append("	   		 TMO.ORG_NAME ORG_NAME,\n" );
		sql.append("	   		 TMO.ORG_ID ORG_ID,\n" );
		sql.append("	   		 TCE.EXECUTE_ID,\n" );
		sql.append("	   		 TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("	   		 TMD.DEALER_CODE DEALER_CODE,\n" );
		sql.append("	   		 TMD.DEALER_ID DEALER_ID,\n" );
		sql.append("	   		 TCE.CHECK_STATUS,\n" );
		sql.append("	   		 TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
		sql.append("	   		 TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE\n" );
		sql.append("	   		 FROM TT_CAMPAIGN TTC, TT_CAMPAIGN_EXECUTE TCE,TM_DEALER TMD,TM_DEALER_ORG_RELATION TDOR,TM_ORG TMO, TM_POSE_BUSINESS_AREA TMPBA\n" );
		sql.append("	WHERE TTC.CAMPAIGN_ID=TCE.CAMPAIGN_ID\n" );
		sql.append("	  		 AND TTC.AREA_ID = TMPBA.AREA_ID\n");
		sql.append("	  		 AND TMPBA.POSE_ID = " + poseId + "\n");
		sql.append("	  		 AND TCE.IS_FLEET = '0'\n" );
		sql.append("	  		 AND TCE.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("	  		 AND TMO.ORG_ID = TDOR.ORG_ID\n" );
		sql.append("	  		 AND TDOR.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("	  		 AND TCE.CHECK_STATUS NOT IN(" + Constant.CAMPAIGN_CHECK_STATUS_01 + ")) allcam\n" );
		sql.append("	  		 WHERE 1=1");
		if(!"-1".equals(checkStatus)&&!"".equals(checkStatus)&&checkStatus!=null){
			sql.append("				 AND allcam.CHECK_STATUS = " + checkStatus + "\n");
		}
		if(!"".equals(campaignNo)&&campaignNo!=null){
			sql.append("				 AND allcam.CAMPAIGN_NO LIKE'%" + campaignNo + "%'\n");
		}
		if(!"".equals(campaignName)&&campaignName!=null){
			sql.append("				 AND allcam.CAMPAIGN_NAME LIKE'%" + campaignName + "%'\n");
		}
		if(!"".equals(campaignSubject)&&campaignSubject!=null){
			sql.append("				 AND allcam.CAMPAIGN_SUBJECT LIKE'%" + campaignSubject + "%'\n");
		}
		if(!"-1".equals(campaignType)&&!"".equals(campaignType)&&campaignType!=null){
			sql.append("				 AND allcam.CAMPAIGN_TYPE =" + campaignType + "\n");
		}
		if(!"".equals(startDate)&&startDate != null){
			sql.append("				 AND allcam.START_DATE >= TO_DATE('" + startDate + " 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate) && endDate != null){
			sql.append("				 AND allcam.END_DATE <= TO_DATE('" + endDate + " 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!"".equals(dealerCode) && null != dealerCode){
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "allcam", "DEALER_CODE"));
		}
		sql.append("ORDER BY allcam.CREATE_DATE DESC, allcam.CAMPAIGN_ID\n"); 

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	
	public static String getCost(String costType, String areaId, String id) {
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TVC.AVAILABLE_AMOUNT\n");
		sql.append("  FROM TT_VS_COST TVC\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND TVC.COST_TYPE = ?\n");  
		params.add(costType) ;
		
		sql.append("   AND TVC.AREA_ID = ?\n");
		params.add(areaId) ;
		
		if(Constant.COST_TYPE_01.toString().equals(costType)) {
			sql.append("   AND TVC.DEALER_ID = ?\n");  
		} else {
			sql.append("   AND TVC.ORG_ID = ?\n");  
		}
		params.add(id) ;

		Map<String, Object> costMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;
		
		if(!CommonUtils.isNullMap(costMap)) {
			return costMap.get("AVAILABLE_AMOUNT").toString() ;
		}
		
		return "0" ;
	}
	
	public List<Map<String, Object>> getAreaPrintCheckInfo(String executeId){
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql= new StringBuffer("\n");
		
		sql.append("SELECT TCEC.CHECK_ID,\n");
		sql.append("       TO_CHAR(TCEC.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE,\n");  
		sql.append("       TCEC.CHECK_DESC,\n");  
		sql.append("       TCEC.CHECK_STATUS,\n");  
		sql.append("       TCU.NAME,\n");  
		sql.append("       TMO.ORG_NAME,\n");  
		sql.append("       A.POSE_NAME\n");  
		sql.append("  FROM TT_CAMPAIGN_EXECUTE_CHK TCEC, TM_ORG TMO, TC_USER TCU, TC_POSE A\n");  
		sql.append(" WHERE TCEC.CHECK_ORG_ID = TMO.ORG_ID\n");  
		sql.append("   AND TCEC.CHECK_USER_ID = TCU.USER_ID\n");  
		sql.append("   AND TCEC.CHECK_POSITION_ID = A.POSE_ID(+)\n");  
		sql.append("   AND TCEC.EXECUTE_ID = ?\n"); 
		params.add(executeId) ;
		
		sql.append("   AND TCEC.CHECK_STATUS = ?\n");  
		params.add(Constant.MARKET_CHECK_STATUS_01) ;
		
		sql.append("   and tcec.create_date > nvl((select cl.create_date\n");  
		sql.append("                             from (select tcec1.create_date\n");  
		sql.append("                                     from TT_CAMPAIGN_EXECUTE_CHK tcec1\n");  
		sql.append("                                    where tcec1.execute_id = ?\n");  
		params.add(executeId) ;
		
		sql.append("                                      and tcec1.check_status = ?\n"); 
		params.add(Constant.MARKET_CHECK_STATUS_02) ;
		
		sql.append("                                    order by rownum desc) cl\n");  
		sql.append("                            where rownum = 1),\n");
		sql.append("                            to_date('1977-01-01 00:00:00', 'yyyy-mm-dd hh24:mi:ss'))\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(),params,getFunName());
		return list;
	}
	
	public static List<Map<String, Object>> filesQuery(String ywzj) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT FSF.* FROM FS_FILEUPLOAD FSF WHERE FSF.YWZJ = ").append(ywzj).append(" \n");
		
		List<Map<String, Object>> fileList = dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		
		return fileList ;
	}
	
	public List<Map<String, Object>> getQueryStatus(Map<String, String> map) {
		String poseId = map.get("poseId") ;
		String isFleetLow = map.get("isFleetLow") ;
		String isAllProtocol = map.get("isAllProtocol") ;
		String isDlrStep = map.get("isDlrStep") ;
		String isAreaStep = map.get("isAreaStep") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select t.query_status, t.check_status, t.is_fleet, t.is_fleet_flow\n");
		sql.append("  from tt_campaign_pose_chk_r t\n");  
		sql.append(" where t.pose_id = ").append(poseId).append("\n");
		
		if(!CommonUtils.isNullString(isFleetLow)) {
			sql.append("  and t.is_fleet_flow = ").append(isFleetLow).append("\n") ;
		}
		
		if(!CommonUtils.isNullString(isAllProtocol)) {
			sql.append("  and t.IS_PROTOCOL_FLOW = ").append(isAllProtocol).append("\n") ;
		}
		
		if(!CommonUtils.isNullString(isDlrStep)) {
			sql.append("  and t.dlr_low_step >= ").append(isDlrStep).append("\n") ;
		}
		
		if(!CommonUtils.isNullString(isAreaStep)) {
			sql.append("  and t.area_low_step >= ").append(isAreaStep).append("\n") ;
		}

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public void filesInsert(Map<String, String> map) {
		String fjid = SequenceManager.getSequence("");
		String fileurl = map.get("fileurl") ;
		String filename = map.get("filename") ;
		String status = map.get("status") ;
		String create_dateStr = map.get("create_date") ;
		Date create_date = new Date(Long.parseLong(create_dateStr)) ;
		String create_by = map.get("create_by") ;
		String ywzj = map.get("ywzj") ;
		String fileid = map.get("fileid") ;
		
		List<Object> params = new ArrayList<Object>() ;
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("insert into fs_fileupload(fjid, fileurl, filename, status, create_date, create_by, ywzj, fileid) values (?, ?, ?, ?, ?, ?, ?, ?)\n");
		params.add(fjid) ;
		params.add(fileurl) ;
		params.add(filename) ;
		params.add(status) ;
		params.add(create_date) ;
		params.add(create_by) ;
		params.add(ywzj) ;
		params.add(fileid) ;

		dao.update(sql.toString(), params) ;
	}
	public static  List<Map<String, Object>> getExecuteChks(String executeId) {
        StringBuilder sql= new StringBuilder();
        sql.append("SELECT TO_CHAR(TRC.CHK_DATE,'YYYY-MM-DD') CHK_DATE,\n" );
        sql.append("TRC.CHK_DESC,\n" );
        sql.append("TRC.STATUS,");
        sql.append(" TU.NAME, TMO.ORG_NAME\n" );
        sql.append("  FROM TT_CAMPAIGN_SPACE_EXECUTE_CHK TRC, TC_USER TU, TM_ORG TMO\n" );
        sql.append(" WHERE TRC.CHK_BY = TU.USER_ID\n" );
        sql.append("   AND TRC.CHK_ORG_ID = TMO.ORG_ID\n" );
        sql.append("   AND TRC.EXECUTE_ID ="+executeId+"\n");


		return dao.pageQuery(sql.toString(),null,dao.getFunName());

	}
}
