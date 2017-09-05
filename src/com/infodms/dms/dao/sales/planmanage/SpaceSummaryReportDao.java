package com.infodms.dms.dao.sales.planmanage;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class SpaceSummaryReportDao extends BaseDao{
	public static Logger logger = Logger.getLogger(SpaceSummaryReportDao.class);
	private static final SpaceSummaryReportDao dao = new SpaceSummaryReportDao ();
	public static final SpaceSummaryReportDao getInstance() {
		return dao;
	}
	
	public static PageResult<Map<String, Object>> activitiesSpacePlanReportQuery (Map<String, String> summaryMap, int curPage, int pageSize) {
		String poseId = summaryMap.get("poseId") ;
		String orgId = summaryMap.get("orgId") ;
		String areaId = summaryMap.get("areaId") ;
		String campaignNo = summaryMap.get("campaignNo") ;
		String campaignName = summaryMap.get("campaignName") ;
		String campaignSubject = summaryMap.get("campaignSubject") ;
		String startDate = summaryMap.get("startDate") ;
		String endDate = summaryMap.get("endDate") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TTC.CAMPAIGN_ID,\n");
		sql.append("       TCSE.SPACE_ID EXECUTE_ID,\n");  
		sql.append("       TTC.CAMPAIGN_NO,\n");  
		sql.append("       TTC.CAMPAIGN_NAME,\n");  
		sql.append("       TTC.CAMPAIGN_TYPE,\n");  
		sql.append("       TTC.CAMPAIGN_SUBJECT,\n");  
		sql.append("       TCSE.CHECK_STATUS,\n");  
		sql.append("       TO_CHAR(TTC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");  
		sql.append("       TO_CHAR(TTC.END_DATE, 'YYYY-MM-DD') END_DATE\n");  
		sql.append("  FROM TT_CAMPAIGN           TTC,\n");  
		sql.append("       TT_CAMPAIGN_SPACE_EXECUTE   TCSE,\n");  
		sql.append("       TM_POSE_BUSINESS_AREA TMPBA\n");  
		sql.append(" WHERE TTC.CAMPAIGN_ID = TCSE.CAMPAIGN_ID\n");  
		sql.append("   AND TTC.AREA_ID = TMPBA.AREA_ID\n");  
		sql.append("   AND TTC.CAMPAIGN_TYPE = ").append(Constant.CAMPAIGN_TYPE_03).append("\n");  
		sql.append("   AND TMPBA.POSE_ID = ").append(poseId).append("\n");  
		sql.append("   AND TCSE.CHECK_STATUS IN (").append(Constant.CAMPAIGN_CHECK_STATUS_06).append(", ").append(Constant.CAMPAIGN_CHECK_STATUS_09).append(") --方案审核完成,总结审核驳回\n");  
		sql.append("   AND TCSE.ORG_ID = ").append(orgId).append("\n");  
		
		if (!CommonUtils.isNullString(campaignNo)) {
			sql.append("   AND TTC.CAMPAIGN_NO LIKE '%").append(campaignNo).append("%'\n");  
		}
		if (!CommonUtils.isNullString(areaId)) {
			sql.append("   AND TTC.AREA_ID = ").append(areaId).append("\n");  
		}
		if (!CommonUtils.isNullString(campaignName)) {
			sql.append("   AND TTC.CAMPAIGN_NAME LIKE '%").append(campaignName).append("%'\n"); 
		}
		if (!CommonUtils.isNullString(campaignSubject)) {
			sql.append("   AND TTC.CAMPAIGN_SUBJECT LIKE '%").append(campaignSubject).append("%'\n");  
		}
		if (!CommonUtils.isNullString(startDate)) {
			sql.append("   AND TRUNC(TTC.START_DATE) >=\n");  
			sql.append("       TO_DATE('").append(startDate).append("','YYYY-MM-DD')\n");  
		}
		if (!CommonUtils.isNullString(endDate)) {
			sql.append("   AND TRUNC(TTC.END_DATE) <=\n");  
			sql.append("       TO_DATE('").append(endDate).append("','YYYY-MM-DD')\n");
		}

		PageResult<Map<String, Object>> spaceSumList = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage) ;
		
		return spaceSumList ;
	}
	
	/**
	 * Function         : 区域活动详细信息查询
	 * @return          : 活动详细信息
	 * @param           : 活动ID
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-29
	 */
	public static Map<String, Object> queryCampaignDetial(String executeId) throws Exception{
		StringBuffer sql= new StringBuffer("\n");
		
		sql.append("select TCSE.SPACE_ID EXECUTE_ID, --执行方案ID\n");
		sql.append("       TCSE.CAMPAIGN_ID, --活动方案ID\n"); 
		sql.append("       TCSE.EXEC_ADD_DESC, --活动地点\n");
		sql.append("       TCSE.EXECUTE_DESC, --执行描述\n");  
		sql.append("       TCSE.CHECK_STATUS, --方案审批状态\n");  
		sql.append("       TCSE.EVALUATE_DESC, --活动评估\n");  
		sql.append("       TCSE.ADVICE_DESC, --建议及整改措施\n");      
		sql.append("       TC.CAMPAIGN_NO, --活动编号\n");  
		sql.append("       TC.CAMPAIGN_NAME, --活动名称\n");  
		sql.append("       TC.CAMPAIGN_TYPE, --活动类型\n");  
		sql.append("       TC.AREA_ID, --活动类型\n");
		sql.append("       to_char(TC.START_DATE,'yyyy-mm-dd') START_DATE, --活动开始日期\n");  
		sql.append("       to_char(TC.END_DATE,'yyyy-mm-dd') END_DATE, --活动结束日期\n");  
		sql.append("       to_char(TCSE.SUBMITS_DATE,'yyyy-mm-dd') SUBMITS_DATE, --活动提报日期\n");  
		sql.append("       TC.CAMPAIGN_OBJECT, --活动对象\n");  
		sql.append("       TC.CAMPAIGN_PURPOSE, --活动目的\n");  
		sql.append("       TC.CAMPAIGN_NEED, --活动要求\n");  
		sql.append("       TC.CAMPAIGN_DESC, --活动主要内容\n");
		sql.append("       TC.campaign_subject, --活动主题\n");	
		sql.append("       TCSE.REMARK, --备注\n");	
		sql.append("       TCSE.ACTIVICE_SUMMARY_DESC --活动主题\n");	
		sql.append("  from TT_CAMPAIGN         TC,\n");  
		sql.append("       TT_CAMPAIGN_SPACE_EXECUTE TCSE\n");   
		sql.append(" where TC.CAMPAIGN_ID = TCSE.CAMPAIGN_ID\n");    
		sql.append("   and TCSE.SPACE_ID = "+executeId);

		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, dao.getFunName());
		return rs;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
