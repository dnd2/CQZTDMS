package com.infodms.dms.dao.sales.marketmanage.planmanage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

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
public class ActivitiesSummaryReportDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ActivitiesSummaryReportDao.class);
	private static final ActivitiesSummaryReportDao dao = new ActivitiesSummaryReportDao ();
	private static POFactory factory = POFactoryBuilder.getInstance();
	public static final ActivitiesSummaryReportDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function         : 品牌查询
	 * @return          : 品牌信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-29
	 */
	public Map<String, Object> selectPinPai() throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVMG.GROUP_NAME\n" );
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG\n" );
		sql.append(" WHERE TVMG.GROUP_LEVEL = 1");
		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, getFunName());
		return rs;
	}
	public  List<Map<String,Object>>  queryAreaCost(String dealerId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVC.COST_ID, TMO.ORG_ID, TVC.FREEZE_AMOUNT,TVC.AVAILABLE_AMOUNT\n" );
		sql.append("   FROM TT_VS_COST TVC, TM_DEALER_ORG_RELATION TDOR, TM_ORG TMO\n" );
		sql.append("  WHERE TVC.ORG_ID = TDOR.ORG_ID\n" );
		sql.append("    AND TDOR.ORG_ID = TMO.ORG_ID\n" );
		sql.append("    AND TVC.COST_TYPE = "+Constant.COST_TYPE_01+"\n" );
		sql.append("    AND TVC.COST_SOURCE = "+Constant.COST_SOURCE_02+"\n" );
		sql.append("    AND TDOR.DEALER_ID IN ("+dealerId+")\n");
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
	/**
	 * 根据执行ID查询活动费用
	 * @param  :执行ID
	 * @return :活动费用信息
	 */
	public List<Map<String, Object>> getActivitiesMediaCost(String executeId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TCMC.COST_ID,\n" );
		sql.append("       TCMC.COST_TYPE,\n" );
		sql.append("       TCMC.ADV_SUBJECT,\n" );
		sql.append("       TO_CHAR(TCMC.ADV_DATE,'YYYY-MM-DD') ADV_DATE,\n" );
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
	public  List<Map<String,Object>>  queryDealerCost(String dealerId){
		StringBuilder sql = new StringBuilder();
		sql.append("  select COST_ID,DEALER_ID,FREEZE_AMOUNT,AVAILABLE_AMOUNT  from tt_vs_cost v   \n");
		sql.append("  WHERE  1=1 ");
		if (dealerId!=null&&!("").equals(dealerId)){
			sql.append(" AND v.DEALER_ID IN("+dealerId+")\n");
		}
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
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
		sql.append("       TCE.EXECUTE_ID,\n");  
		sql.append("       TTC.CAMPAIGN_NO,\n");  
		sql.append("       TTC.CAMPAIGN_NAME,\n");  
		sql.append("       TTC.CAMPAIGN_TYPE,\n");  
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n");  
		sql.append("       TCE.CHECK_STATUS,\n");  
		sql.append("       TO_CHAR(TTC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");  
		sql.append("       TO_CHAR(TTC.END_DATE, 'YYYY-MM-DD') END_DATE\n");  
		sql.append("  FROM TT_CAMPAIGN TTC, TT_CAMPAIGN_EXECUTE TCE, TM_POSE_BUSINESS_AREA TMPBA\n");  
		sql.append(" WHERE TTC.CAMPAIGN_ID = TCE.CAMPAIGN_ID\n");  
		sql.append("   AND TTC.AREA_ID = TMPBA.AREA_ID\n");  
		sql.append("AND (TTC.CAMPAIGN_TYPE = ").append(Constant.CAMPAIGN_TYPE_01).append(" OR TTC.CAMPAIGN_TYPE = ").append(Constant.CAMPAIGN_TYPE_02).append(")\n");
		sql.append("   AND TMPBA.POSE_ID = " + poseId + "\n");
		sql.append("   AND TCE.CHECK_STATUS IN("+Constant.CAMPAIGN_CHECK_STATUS_06+","+Constant.CAMPAIGN_CHECK_STATUS_09+")\n" );//方案审核完成,总结审核驳回
		sql.append("   AND TCE.DEALER_ID IN("+dealerId+")\n");
		if(!"".equals(campaignNo)&&campaignNo!=null){
			sql.append("AND TTC.CAMPAIGN_NO LIKE'%"+campaignNo+"%'\n");
		}
		if(!"".equals(areaId)&&areaId!=null){
			sql.append("AND TTC.AREA_ID ="+areaId+"\n");
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
			sql.append("   AND TTC.START_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TTC.END_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	//查询经销商树
	public static PageResult<Map<String, Object>> getAllDRLByDeptId(String dcode,String dsname,String provice,Long companyId,int curPage,
			int pageSize,String inputOrgId,Integer poseBusType,String campaignId) throws Exception {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT A.DEALER_CODE, A.DEALER_ID, A.DEALER_NAME,A.DEALER_SHORTNAME,A.PROVINCE_ID,TMO.ORG_NAME FROM TM_DEALER a,TM_DEALER_ORG_RELATION REL ,TM_ORG TMO");
		query.append(" where   A.STATUS = ");
		query.append(Constant.STATUS_ENABLE);
		query.append(" AND A.DEALER_ID = REL.DEALER_ID \n");
		query.append(" AND REL.ORG_ID = TMO.ORG_ID \n");
		query.append(" AND A.OEM_COMPANY_ID = "+companyId+"\n");
		if(!"".equals(inputOrgId)&&inputOrgId!=null)
		{
			query.append(" AND REL.ORG_ID IN (");
			query.append(" SELECT ORG_ID FROM TM_ORG ORG WHERE ORG.STATUS = " + Constant.STATUS_ENABLE);
			query.append(" START WITH ORG.ORG_ID = " + inputOrgId);
			query.append(" CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID) ");	
		}
		query.append(" AND NOT EXISTS ( SELECT 1 \n");
		query.append(" FROM TT_CAMPAIGN_EXECUTE TCE \n");
		query.append(" WHERE TCE.DEALER_ID = A.DEALER_ID \n");
		query.append(" AND TCE.CAMPAIGN_ID = "+campaignId+" ) \n");	
		if (dcode != null && !dcode.equals("")) {
			query.append(" AND A.DEALER_CODE LIKE '%");
			query.append(dcode);
			query.append("%' ");
		}
		if (dsname != null && !dsname.equals("")) {
			query.append(" and A.DEALER_NAME like '%");
			query.append(dsname);
			query.append("%' ");
		}
		// 增加经销商所属身份查询条件 
		if (provice != null && !provice.equals("")) {
			query.append(" and A.PROVINCE_ID =");
			query.append(provice);
			query.append("\n");
		}
		
		if(Constant.POSE_BUS_TYPE_WR==poseBusType||Constant.POSE_BUS_TYPE_DWR==poseBusType){
			query.append(" and DEALER_TYPE ="+Constant.DEALER_TYPE_DWR+" \n");
		}else if(Constant.POSE_BUS_TYPE_VS==poseBusType||Constant.POSE_BUS_TYPE_DVS==poseBusType)
		{
			query.append(" and DEALER_TYPE ="+Constant.DEALER_TYPE_DVS+" \n");
		}
		logger.info("SQL: " + query);
		PageResult<Map<String, Object>> rs = dao.pageQuery(query.toString(), null, dao.getFunName(),curPage,pageSize );
		return rs;
	}
	//查询经销商树
	public static PageResult<Map<String, Object>> getMyAllDRLByDeptId(String areaId,String dcode,String dsname,String provice,Long companyId,int curPage,
			int pageSize,String inputOrgId,Integer poseBusType,String campaignId) throws Exception {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT A.DEALER_CODE, A.DEALER_ID, A.DEALER_NAME,A.DEALER_SHORTNAME,A.PROVINCE_ID,TMO.ORG_NAME FROM TM_DEALER a,TM_DEALER_ORG_RELATION REL ,TM_ORG TMO");
		query.append(" where   A.STATUS = "); 
		query.append(Constant.STATUS_ENABLE);
		query.append(" AND REL.ORG_ID=TMO.ORG_ID\n");
		query.append(" AND A.DEALER_ID = REL.DEALER_ID \n");
		query.append(" AND REL.ORG_ID = "+inputOrgId+" \n");
		query.append(" AND A.OEM_COMPANY_ID = "+companyId+"\n");
		if (null != campaignId && !"".equals(campaignId)) {
			query.append(" AND NOT EXISTS ( SELECT 1 \n");
			query.append(" FROM TT_CAMPAIGN_EXECUTE TCE \n");
			query.append(" WHERE TCE.DEALER_ID = A.DEALER_ID \n");
			query.append(" AND TCE.CAMPAIGN_ID = "+campaignId+" ) \n");	
		}
		if (areaId != null && !areaId.equals("")) {
			query.append(" AND a.dealer_id in(select tdba.dealer_id from tm_dealer_business_area tdba where tdba.area_id="+areaId+")\n");
		}
		if (dcode != null && !dcode.equals("")) {
			query.append(" AND A.DEALER_CODE LIKE '%");
			query.append(dcode);
			query.append("%' ");
		}
		if (dsname != null && !dsname.equals("")) {
			query.append(" and A.DEALER_NAME like '%");
			query.append(dsname);
			query.append("%' ");
		}
		// 增加经销商所属身份查询条件 
		if (provice != null && !provice.equals("")) {
			query.append(" and A.PROVINCE_ID =");
			query.append(provice);
			query.append("\n");
		}
		
	
	if(Constant.POSE_BUS_TYPE_WR==poseBusType||Constant.POSE_BUS_TYPE_DWR==poseBusType){
		query.append(" and DEALER_TYPE ="+Constant.DEALER_TYPE_DWR+" \n");
	}else if(Constant.POSE_BUS_TYPE_VS==poseBusType||Constant.POSE_BUS_TYPE_DVS==poseBusType)
	{
		query.append(" and DEALER_TYPE <>"+Constant.DEALER_TYPE_DWR+" \n");
	}
	logger.info("SQL: " + query);
	PageResult<Map<String, Object>> rs = dao.pageQuery(query.toString(), null, dao.getFunName(),curPage,pageSize );
	return rs;
}
	/**
	*取得当前用户在业务范围为areaId的唯一dealerId
	*@param dealerIds 当前用的经销商id（可能为多个）
	*@param areaId 业务范围id
	*@return List
	*/
	public List<Map<String, Object>> getDel(String dealerIds, String areaId) {
		StringBuffer sql = new StringBuffer("") ;

		sql.append("SELECT TMD.DEALER_ID\n");
		sql.append("  FROM TM_DEALER TMD, TM_DEALER_BUSINESS_AREA TMDBA\n");  
		sql.append(" WHERE TMD.DEALER_ID = TMDBA.DEALER_ID\n");  
		sql.append("   AND TMDBA.AREA_ID = " + areaId + "\n");  
		sql.append("   AND TMD.DEALER_ID IN (" + dealerIds + ")\n");
		
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}

	/**
	*通过经销商ID查找其属于的区域ID
	*@param dealerId 经销商id
	*@return List
	*/
	public List<Map<String, Object>> getOrgId(String dealerId) {
		StringBuffer sql = new StringBuffer("") ;

		sql.append("SELECT TMDOR.ORG_ID\n");
		sql.append("  FROM TM_DEALER TMD, TM_DEALER_ORG_RELATION TMDOR\n");  
		sql.append(" WHERE TMD.DEALER_ID = TMDOR.DEALER_ID\n");  
		sql.append("   AND TMD.DEALER_ID = " + dealerId + "\n");

		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
}
