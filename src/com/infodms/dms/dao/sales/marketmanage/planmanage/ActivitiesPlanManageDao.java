package com.infodms.dms.dao.sales.marketmanage.planmanage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtVsCostPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 活动方案管理DAO
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-30
 * @author 
 * @mail 
 * @version 1.0
 * @remark 
 */
public class ActivitiesPlanManageDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ActivitiesPlanManageDao.class);
	private static final ActivitiesPlanManageDao dao = new ActivitiesPlanManageDao ();
	private static POFactory factory = POFactoryBuilder.getInstance();
	public static final ActivitiesPlanManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 通过业务主键取得附件信息
	 * @param executeIds
	 * @return
	 */
	public List<Map<String, Object>>  getAttachInfos(String ywzjs){
		
		String sql = "SELECT F.FJID,F.FILEURL,F.FILENAME FROM FS_FILEUPLOAD F WHERE F.YWZJ ="+ywzjs+"ORDER BY F.FJID";

		List<Map<String, Object>> list = pageQuery(sql,null,getFunName());
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
	 * Function         : 活动执行方案提报查询
	 * @param           : 方案编号
	 * @param           : 方案名称
	 * @param           : 活动主题
	 * @param           : 活动类别
	 * @param           : 活动时间
	 * @param           : 经销商ID
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-04
	 */
	public PageResult<Map<String, Object>> activitiesPlanReportQuery(Long poseId, String areaId,String campaignNo,String campaignName,String campaignSubject,String campaignType,String startDate,
			String endDate,String dealerId,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer("\n");
		sql.append("SELECT TTC.CAMPAIGN_ID,\n");
		sql.append("       TTC.CAMPAIGN_NO,\n");  
		sql.append("       TTC.CAMPAIGN_NAME,\n");  
		sql.append("       TTC.CAMPAIGN_TYPE,\n");  
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n");  
		sql.append("       TCE.DEALER_ID,\n");  
		sql.append("       TCE.EXECUTE_ID,\n");  
		sql.append("       TBA.AREA_NAME,\n");  
		sql.append("       TO_CHAR(TTC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");  
		sql.append("       TO_CHAR(TTC.END_DATE, 'YYYY-MM-DD') END_DATE\n");  
		sql.append("  FROM TT_CAMPAIGN TTC, TT_CAMPAIGN_EXECUTE TCE, TM_BUSINESS_AREA TBA, TM_POSE_BUSINESS_AREA TMPBA\n");  
		sql.append(" WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n");  
		sql.append("   AND TMPBA.AREA_ID = TTC.AREA_ID\n"); 
		sql.append("   AND TMPBA.AREA_ID = TBA.AREA_ID\n"); 
		sql.append("AND (TTC.CAMPAIGN_TYPE = ").append(Constant.CAMPAIGN_TYPE_01).append(" OR TTC.CAMPAIGN_TYPE = ").append(Constant.CAMPAIGN_TYPE_02).append(")\n");
		sql.append("   AND TMPBA.POSE_ID = " + poseId + "\n");
		sql.append("   AND TCE.CHECK_STATUS IN("+Constant.CAMPAIGN_CHECK_STATUS_01+","+Constant.CAMPAIGN_CHECK_STATUS_02+","+Constant.CAMPAIGN_CHECK_STATUS_05+")\n" );
		if(null != dealerId && !"".equals(dealerId)){
			sql.append("   AND TCE.DEALER_ID IN(" + dealerId + ")");
		}
		if(!"".equals(campaignNo)&&campaignNo!=null){
			sql.append("AND TTC.CAMPAIGN_NO LIKE'%"+campaignNo+"%'\n");
		}
		if(!"".equals(campaignName)&&campaignName!=null){
			sql.append("AND TTC.CAMPAIGN_NAME LIKE'%"+campaignName+"%'\n");
		}
		if(!"".equals(campaignSubject)&&campaignSubject!=null){
			sql.append("AND TTC.CAMPAIGN_SUBJECT LIKE'%"+campaignSubject+"%'\n");
		}
		if(!"-1".equals(campaignType)&&!"".equals(campaignType)&&campaignType!=null){
			sql.append("AND TTC.CAMPAIGN_TYPE ="+campaignType+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND (trunc(TTC.START_DATE) >= to_date('"+startDate+"','yyyy-mm-dd')").append("   or (trunc(TTC.START_DATE) < to_date('"+startDate+"','yyyy-mm-dd')").append("   AND trunc(TTC.end_date) >= to_date('"+startDate+"','yyyy-mm-dd')))\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   and (trunc(TTC.END_DATE) <= to_date('"+endDate+"','yyyy-mm-dd')").append("   or (trunc(TTC.END_DATE) > to_date('"+endDate+"','yyyy-mm-dd')").append("   and trunc(TTC.start_date) <= to_date('"+endDate+"','yyyy-mm-dd')))\n"); //YH 2011.6.3
		}
		
		if(!"".equals(areaId) && areaId != null){
			sql.append("   AND TTC.AREA_ID='"+areaId+"'\n");
		}
		
		sql.append("ORDER BY TTC.CREATE_DATE DESC\n");

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 方案信息查询
	 * @param           : 方案ID
	 * @return          : 方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-29
	 */
	/*public Map<String, Object> activitiesInfoQuery(String campaignId,String dealerId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
		sql.append("       TTC.GROUP_ID,\n" );
		sql.append("       TTC.CAMPAIGN_MODEL,\n" );
		sql.append("       TCP.PROD_NAME,\n" );
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
		sql.append("       TCE.EXEC_ADD_DESC,\n" );
		sql.append("       TCE.EVALUATE_DESC,\n" );
		sql.append("       TCE.ADVICE_DESC,\n" );
		sql.append("       TCE.EXECUTE_ID\n" );
		sql.append(" FROM TT_CAMPAIGN TTC,TT_CAMPAIGN_EXECUTE TCE,TM_CAMPAIGN_PROD TCP\n" );
		sql.append(" WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n");
		sql.append(" AND TTC.CAMPAIGN_MODEL = TCP.PROD_ID\n");
		if(!"".equals(campaignId)&&campaignId!=null){
			sql.append(" AND TTC.CAMPAIGN_ID="+campaignId+"\n");
		}
		if(!"".equals(dealerId)&&dealerId!=null){
			sql.append(" AND TCe.DEALER_ID="+dealerId+"\n");
		}
		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, getFunName());
		return rs;
	}*/
	public Map<String, Object> activitiesInfoQuery(String campaignId,String dealerId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
		sql.append("       TTC.GROUP_ID,\n" );
		sql.append("       TTC.CAMPAIGN_MODEL,\n" );
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
		sql.append("       TCE.EXEC_ADD_DESC,\n" );
		sql.append("       TCE.EVALUATE_DESC,\n" );
		sql.append("       TCE.ADVICE_DESC,\n" );
		sql.append("       TCE.IS_FLEET,\n" );
		sql.append("       TCE.REMARK,\n" );
		sql.append("       TCE.EXECUTE_ID\n" );
		sql.append(" FROM TT_CAMPAIGN TTC,TT_CAMPAIGN_EXECUTE TCE\n" );
		sql.append(" WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n");
		if(!"".equals(campaignId)&&campaignId!=null){
			sql.append(" AND TTC.CAMPAIGN_ID="+campaignId+"\n");
		}
		if(!"".equals(dealerId)&&dealerId!=null){
			sql.append(" AND TCe.DEALER_ID="+dealerId+"\n");
		}
		Map<String, Object> rs1 = dao.pageQueryMap(sql.toString(), null, getFunName());
		Map<String, Object> rs  = getModelByCampaignId(rs1);		
		return rs;
	}
	
	public  Map<String, Object> getModelByCampaignId(Map<String, Object> rs) {
		if (rs != null && !"".equals(rs.get("CAMPAIGN_ID"))) {
			String modelId = "";
			String modelCode = "";
			StringBuffer sql= new StringBuffer();
			sql.append("select r.relation_id, r.campaign_id, r.campaign_model,t.group_code\n" );
			sql.append("  from tt_campaign_group_r r, tm_vhcl_material_group t\n" );
			sql.append(" where r.campaign_model = t.group_id\n");
			sql.append(" and r.campaign_id = "+rs.get("CAMPAIGN_ID")+" \n");
			
			List<Map<String, Object>> modelList = dao.pageQuery(sql.toString(), null,getFunName());
			for (int j = 0; j < modelList.size(); j++) {
				modelId += modelList.get(j).get("CAMPAIGN_MODEL") + ",";
				modelCode += modelList.get(j).get("GROUP_CODE") + ",";
			}
			if (!"".equals(modelId) && modelId.length() > 0) {
				modelId = modelId.substring(0, modelId.length() - 1);
			}
			if (!"".equals(modelCode) && modelCode.length() > 0) {
				modelCode = modelCode.substring(0, modelCode.length() - 1);
			}			
			rs.put("MODELID", modelId);//车型ID
			rs.put("MODELCODE", modelCode);//车型代码
		}
		return rs;
	}
	/**
	 * Function         : 方案执行目标查询
	 * @param           : 执行ID
	 * @return          : 目标信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-29
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
		sql.append("       TCC.ITEM_NAME,\n" );
		sql.append("       TCC.ITEM_REMARK,\n" );
		sql.append("       TCC.ITEM_PRICE,\n" );
		sql.append("       TCC.ITEM_COUNT,\n" );
		sql.append("       TCC.COST_ACCOUNT,\n" );
		sql.append("       NVL(TCC.REAL_COST, 0) REAL_COST,\n" );
		sql.append("       NVL(TCC.ITEM_COST, 0) ITEM_COST,\n");
		sql.append("       TCC.ACTIVITY_TYPE,\n");
		sql.append("       TCC.ACTIVITY_CONTENT,\n");
		sql.append("       TCC.REGION,\n");
		sql.append("       TO_CHAR(TCC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");
		sql.append("       TO_CHAR(TCC.END_DATE, 'YYYY-MM-DD') END_DATE,\n");
		sql.append("       TCC.PLAN_COST\n" );
		sql.append("  FROM TT_CAM_CAMPAIGN_COST TCC\n" );
		sql.append(" WHERE TCC.EXECUTE_ID ="+executeId+"\n" );
		sql.append("   AND TCC.CAMPAIGN_ID ="+campaignId+"\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	/**
	 * 根据执行ID查询活动费用
	 * @param  :执行ID
	 * @return :活动费用信息
	 */
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
		sql.append("  FROM TT_CAM_MEDIA_COST TCMC \n" );
		sql.append(" WHERE TCMC.EXECUTE_ID ="+executeId+"\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	public  List<Map<String,Object>>  queryOrg(String dealers){
		StringBuffer sql= new StringBuffer();
		sql.append("select vod.root_org_id org_id from vw_org_dealer vod where vod.dealer_id in("+dealers+")\n" );
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public  List<Map<String,Object>>  queryExecute(String dealers,String campaign){
		StringBuffer sql= new StringBuffer();
		sql.append(" select * from  tt_campaign_execute tcse where tcse.campaign_id="+campaign+" and tcse.dealer_id in("+dealers+")\n" );
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * Function         : 活动信息查询
	 * @param           : 方案编号
	 * @param           : 方案名称
	 * @param           : 活动主题
	 * @param           : 活动类别
	 * @param           : 活动时间
	 * @param           : 活动方案状态
	 * @param           : 经销商ID
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-04
	 */
	public PageResult<Map<String, Object>> activitiesInfoSearch(String costType, String areaId,String dealerIds,String orgIds,String dealerCode,String campaignNo,String campaignName,String campaignSubject,String campaignType,String startDate,
			String endDate,String checkStatus,String dealerId,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		
		sql.append("SELECT TTC.CAMPAIGN_ID,\n");
		sql.append("       TTC.CREATE_DATE,\n");  
		sql.append("       TTC.CAMPAIGN_NO,\n");  
		sql.append("       TTC.CAMPAIGN_NAME,\n");  
		sql.append("       TTC.CAMPAIGN_TYPE,\n");  
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n");  
		sql.append("       TCE.EXECUTE_ID EXECUTE_ID,\n");  
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n");  
		sql.append("       TMD.DEALER_CODE DEALER_CODE,\n");  
		sql.append("       TMD.DEALER_ID DEALER_ID,\n");  
		sql.append("       TCE.CHECK_STATUS,\n");  
		sql.append("       tce.submits_date,\n");  
		sql.append("       tce.price_type,\n");  
		sql.append("       TO_CHAR(TTC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");  
		sql.append("       TO_CHAR(TTC.END_DATE, 'YYYY-MM-DD') END_DATE\n");  
		sql.append("  FROM TT_CAMPAIGN TTC, TT_CAMPAIGN_EXECUTE TCE, TM_DEALER TMD\n");  
		sql.append(" WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n");  
		sql.append("   AND TCE.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTC.AREA_ID IN (" + areaId + ")\n");  
		sql.append("   AND TMD.DEALER_ID in (" + dealerIds + ")\n");  
		sql.append("AND ((TCE.CHECK_STATUS NOT IN (" + Constant.CAMPAIGN_CHECK_STATUS_01 + ", " + Constant.CAMPAIGN_CHECK_STATUS_02 + ", " + Constant.CAMPAIGN_CHECK_STATUS_12 + "," + Constant.CAMPAIGN_CHECK_STATUS_11 + ") AND\n");
		sql.append("      (TTC.CAMPAIGN_TYPE = " + Constant.CAMPAIGN_TYPE_01 + " OR TTC.CAMPAIGN_TYPE = " + Constant.CAMPAIGN_TYPE_02 + ")) OR\n");  
		sql.append("      (TCE.CHECK_STATUS = " + Constant.CAMPAIGN_CHECK_STATUS_06 + " AND\n");  
		sql.append("      TTC.CAMPAIGN_TYPE = " + Constant.CAMPAIGN_TYPE_03 + "))\n");


//		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
//		sql.append("       TTC.CAMPAIGN_NO,\n" );
//		sql.append("       TTC.CAMPAIGN_NAME,\n" );
//		sql.append("       TTC.CAMPAIGN_TYPE,\n" );
//		sql.append("       TTC.CAMPAIGN_SUBJECT,\n" );
//		sql.append("       TCE.EXECUTE_ID,\n" );
//		sql.append("       TCE.DEALER_ID,\n" );
//		sql.append("       TCE.CHECK_STATUS,\n" );
//		sql.append("       TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
//		sql.append("       TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE\n" );
//		sql.append("   FROM TT_CAMPAIGN TTC,TT_CAMPAIGN_EXECUTE TCE \n" );
//		sql.append(" WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n" );
//		sql.append("   AND TCE.DEALER_ID in ("+dealerId+")");
//		sql.append("   AND TCE.CHECK_STATUS NOT IN("+Constant.CAMPAIGN_CHECK_STATUS_01+","+Constant.CAMPAIGN_CHECK_STATUS_12+")\n" );
		if(!"-1".equals(checkStatus)&&!"".equals(checkStatus)&&checkStatus!=null){
			sql.append("  AND TCE.CHECK_STATUS = "+checkStatus+"\n");
		}
		if(!"".equals(campaignNo)&&campaignNo!=null){
			sql.append("AND TTC.CAMPAIGN_NO LIKE'%"+campaignNo+"%'\n");
		}
		if(!"".equals(campaignName)&&campaignName!=null){
			sql.append("AND TTC.CAMPAIGN_NAME LIKE'%"+campaignName+"%'\n");
		}
		if(!"".equals(campaignSubject)&&campaignSubject!=null){
			sql.append("AND TTC.CAMPAIGN_SUBJECT LIKE'%"+campaignSubject+"%'\n");
		}
		if(!"-1".equals(campaignType)&&!"".equals(campaignType)&&campaignType!=null){
			sql.append("AND TTC.CAMPAIGN_TYPE ="+campaignType+"\n");
		}
		/*if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TTC.START_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TTC.END_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}*/
		
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND (trunc(TTC.START_DATE) >= to_date('"+startDate+"','yyyy-mm-dd')").append("   or (trunc(TTC.START_DATE) < to_date('"+startDate+"','yyyy-mm-dd')").append("   AND trunc(TTC.end_date) >= to_date('"+startDate+"','yyyy-mm-dd')))\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   and (trunc(TTC.END_DATE) <= to_date('"+endDate+"','yyyy-mm-dd')").append("   or (trunc(TTC.END_DATE) > to_date('"+endDate+"','yyyy-mm-dd')").append("   and trunc(TTC.start_date) <= to_date('"+endDate+"','yyyy-mm-dd')))\n"); //YH 2011.6.3
		}
		
		if(!"-1".equals(costType)&&!"".equals(costType)&&costType!=null){
			sql.append("AND TCE.PRICE_TYPE ="+costType+"\n");
		}
		if (!"".equals(dealerCode)&&null!=dealerCode){
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "allcam", "DEALER_CODE"));
		}
		
		sql.append("ORDER BY TTC.CREATE_DATE DESC, TTC.CAMPAIGN_ID\n");

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
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
		sql.append("       FSF.FILENAME\n" );
		sql.append("  FROM TT_CAMPAIGN_EXECUTE_CHK TCEC,\n" );
		sql.append("       TM_ORG                  TMO,\n" );
		sql.append("       TC_USER                 TCU,\n" );
		sql.append("       FS_FILEUPLOAD           FSF\n" );
		sql.append(" WHERE TCEC.CHECK_ORG_ID = TMO.ORG_ID\n" );
		sql.append("   AND TCEC.CHECK_USER_ID = TCU.USER_ID\n" );
		sql.append("   AND TCEC.CHECK_ID = FSF.YWZJ(+)\n" );
		sql.append("   AND TCEC.EXECUTE_ID ="+executeId+"\n");
		sql.append("   ORDER BY CHECK_DATE ASC \n");
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
		sql.append("       TO_CHAR(TCSC.CHECK_DATE, 'YYYY-MM-DD HH24:MI:SS') CHECK_DATE,\n" );
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
	 * Function         : 方案信息查询
	 * @param           : 方案ID
	 * @return          : 方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-07-05
	 */
	public Map<String, Object> activitiesInfo(String campaignId,String executeId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TTC.CAMPAIGN_ID,\n" );
		sql.append("       TVMG.GROUP_NAME,\n" );
		sql.append("       TTC.CAMPAIGN_NO,\n" );
		sql.append("       TTC.AREA_ID,\n" );
		sql.append("       TTC.CAMPAIGN_NAME,\n" );
		sql.append("       TO_CHAR(TTC.START_DATE,'YYYY-MM-DD') START_DATE,\n" );
		sql.append("       TO_CHAR(TTC.END_DATE,'YYYY-MM-DD') END_DATE,\n" );
		sql.append("       TO_CHAR(TCE.SUBMITS_DATE,'YYYY-MM-DD') SUBMITS_DATE,\n" );
		sql.append("       TCU.NAME,\n" );
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n" );
		sql.append("       TTC.CAMPAIGN_OBJECT,\n" );
		sql.append("       TTC.CAMPAIGN_PURPOSE,\n" );
		sql.append("       TTC.CAMPAIGN_NEED,\n" );
		sql.append("       TTC.CAMPAIGN_DESC,\n" );
		sql.append("       TTC.CAMPAIGN_TYPE,\n" );
		sql.append("       TCE.DEALER_ID,\n" );
		sql.append("       TMD.DEALER_CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       TCE.IS_FLEET,\n" );
		sql.append("       TCE.REMARK,\n" );
		sql.append("       TCE.EXEC_ADD_DESC,\n" );
		sql.append("       TCE.EVALUATE_DESC,\n" );
		sql.append("       TCE.ADVICE_DESC,\n" );
		sql.append("       TCE.EXECUTE_ID\n" );
		sql.append(" FROM TT_CAMPAIGN TTC,TT_CAMPAIGN_EXECUTE TCE,TM_DEALER TMD,TM_VHCL_MATERIAL_GROUP TVMG, TC_USER TCU\n" );
		sql.append(" WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n");
		sql.append(" AND TCE.DEALER_ID = TMD.DEALER_ID\n");
		sql.append(" AND TVMG.GROUP_ID = TTC.GROUP_ID\n");
		sql.append(" AND TCE.CREATE_BY = TCU.USER_ID\n");
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
		System.out.println("111111111111111111111111111111111111111111111"+modelName);
		return modelName;
	}
	
	/**
	 * Function         : 方案信息查询
	 * @param           : 方案ID
	 * @return          : 方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-08-25
	 */
	@SuppressWarnings("unchecked")
	public  List<Map<String,Object>>  queryDealerCost(String dealerId,String areaId){
			StringBuilder sql = new StringBuilder();
			sql.append("  select COST_ID,DEALER_ID,FREEZE_AMOUNT,AVAILABLE_AMOUNT  from tt_vs_cost v   \n");
			sql.append("  WHERE  1=1 ");
			if (dealerId!=null&&!("").equals(dealerId)){
				sql.append(" AND v.DEALER_ID IN("+dealerId+")\n");
			}
			sql.append("    AND v.COST_TYPE = "+Constant.COST_TYPE_07+"\n" );
			sql.append("AND v.area_id="+areaId);
			List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
			return list;
	}
	public  List<Map<String,Object>>  queryAreaCost(String dealerId,String areaId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVC.COST_ID, TMO.ORG_ID, TVC.FREEZE_AMOUNT,TVC.AVAILABLE_AMOUNT\n" );
		sql.append("   FROM TT_VS_COST TVC, TM_DEALER_ORG_RELATION TDOR, TM_ORG TMO,tm_org_business_area toba\n" );
		sql.append("  WHERE TVC.ORG_ID = TDOR.ORG_ID\n" );
		sql.append("    AND TDOR.ORG_ID = TMO.ORG_ID\n" );
		sql.append("    AND TVC.COST_TYPE = "+Constant.COST_TYPE_01+"\n" );
		sql.append("    AND TVC.COST_SOURCE = "+Constant.COST_SOURCE_02+"\n" );
		sql.append("    AND TDOR.DEALER_ID IN ("+dealerId+")\n");
		sql.append("	AND TOBA.ORG_ID=TMO.ORG_ID\n");
		sql.append("	AND TOBA.AREA_ID="+areaId+"\n");
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public  List<Map<String,Object>>  queryOemCost(){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVC.COST_ID,TVC.COST_TYPE, TMO.ORG_ID,TVC.FREEZE_AMOUNT,TVC.AVAILABLE_AMOUNT\n" );
		sql.append("   FROM TT_VS_COST TVC,TM_ORG TMO\n" );
		sql.append("  WHERE TVC.ORG_ID = TMO.ORG_ID\n" );
		sql.append("    AND TVC.COST_SOURCE = "+Constant.COST_SOURCE_03+"\n" );
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public  List<Map<String,Object>>  queryAreaOrgCost(String areaId,String orgId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVC.COST_ID,TVC.COST_TYPE, TMO.ORG_ID,TVC.FREEZE_AMOUNT,TVC.AVAILABLE_AMOUNT\n" );
		sql.append("   FROM TT_VS_COST TVC,TM_ORG TMO\n" );
		sql.append("  WHERE TVC.ORG_ID = TMO.ORG_ID\n" );
		sql.append("    AND TVC.COST_SOURCE = "+Constant.COST_SOURCE_02+"\n" );
		sql.append("    AND TVC.ORG_ID="+orgId+"\n");
		sql.append("    AND TVC.AREA_ID="+areaId+"\n");
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public  List<Map<String,Object>>  queryAreaOemCost(String areaId,String orgId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT sum(TVC.AVAILABLE_AMOUNT) AVAILABLE_AMOUNT\n" );
		sql.append("   FROM TT_VS_COST TVC,TM_ORG TMO\n" );
		sql.append("  WHERE TVC.ORG_ID = TMO.ORG_ID\n" );
		sql.append("    AND TVC.COST_SOURCE = "+Constant.COST_SOURCE_03+"\n" );
		sql.append(" and tvc.cost_type="+Constant.COST_TYPE_01+"\n");
//		sql.append("SELECT TVC.COST_ID,TVC.COST_TYPE, TMO.ORG_ID,TVC.FREEZE_AMOUNT,TVC.AVAILABLE_AMOUNT\n" );
//		sql.append("   FROM TT_VS_COST TVC,TM_ORG TMO\n" );
//		sql.append("  WHERE TVC.ORG_ID = TMO.ORG_ID\n" );
//		sql.append("    AND TVC.COST_SOURCE = "+Constant.COST_SOURCE_02+"\n" );
//		sql.append("    AND TVC.ORG_ID="+orgId+"\n");
//		sql.append("    AND TVC.AREA_ID="+areaId+"\n");
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public  List<Map<String,Object>>  queryAreaOem2Cost(String areaId,String orgId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT sum(TVC.AVAILABLE_AMOUNT) AVAILABLE_AMOUNT\n" );
		sql.append("   FROM TT_VS_COST TVC,TM_ORG TMO\n" );
		sql.append("  WHERE TVC.ORG_ID = TMO.ORG_ID\n" );
		sql.append("    AND TVC.COST_SOURCE = "+Constant.COST_SOURCE_03+"\n" );
		sql.append(" and tvc.cost_type="+Constant.COST_TYPE_02+"\n");
//		sql.append("SELECT TVC.COST_ID,TVC.COST_TYPE, TMO.ORG_ID,TVC.FREEZE_AMOUNT,TVC.AVAILABLE_AMOUNT\n" );
//		sql.append("   FROM TT_VS_COST TVC,TM_ORG TMO\n" );
//		sql.append("  WHERE TVC.ORG_ID = TMO.ORG_ID\n" );
//		sql.append("    AND TVC.COST_SOURCE = "+Constant.COST_SOURCE_02+"\n" );
//		sql.append("    AND TVC.ORG_ID="+orgId+"\n");
//		sql.append("    AND TVC.AREA_ID="+areaId+"\n");
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	
	public  List<Map<String,String>>  queryActiveVehicleType(String campaignId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT G.GROUP_ID, G.GROUP_CODE\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP G\n");  
		sql.append(" WHERE G.GROUP_ID IN\n");  
		sql.append("       (SELECT R.CAMPAIGN_MODEL\n");  
		sql.append("          FROM TT_CAMPAIGN_GROUP_R R\n");  
		sql.append("         WHERE R.CAMPAIGN_ID = "+campaignId+")\n");  
		sql.append("   --AND G.GROUP_LEVEL = 3\n");

		List<Map<String,String>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public  List<Map<String,String>>  queryActiveVehicleType__(String campaignId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT G.GROUP_ID, G.GROUP_CODE\n");
		sql.append("  FROM TT_CAMPAIGN_GROUP_R R, TM_VHCL_MATERIAL_GROUP G\n");  
		sql.append(" WHERE R.CAMPAIGN_ID = "+campaignId+"\n");  
		sql.append("   AND R.CAMPAIGN_MODEL = G.GROUP_ID\n");


		List<Map<String,String>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}
