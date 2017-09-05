package com.infodms.dms.dao.sales.planmanage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

//import com.infodms.dms.actions.sales.marketmanage.planmanage.ActivitiesSpacePlanReport;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.marketmanage.planmanage.ActivitiesPlanManageDao;
import com.infodms.dms.po.TtCampaignPlanPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class SpacePlanReportDao extends BaseDao {
	public static Logger logger = Logger.getLogger(SpacePlanReportDao.class);
	private static final SpacePlanReportDao dao = new SpacePlanReportDao();

	public static final SpacePlanReportDao getInstance() {
		return dao;
	}

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
		sql.append("     TCSE.CHECK_STATUS,\n");
		sql.append("     TO_CHAR(TTC.START_DATE, 'YYYY-MM-DD') START_DATE,\n");
		sql.append("     TO_CHAR(TTC.END_DATE, 'YYYY-MM-DD') END_DATE\n");
		sql.append("FROM TT_CAMPAIGN               TTC,\n");
		sql.append("     TT_CAMPAIGN_SPACE_EXECUTE TCSE\n");
		sql.append("WHERE TTC.CAMPAIGN_ID = TCSE.CAMPAIGN_ID\n");
		sql.append(" AND TCSE.CHECK_STATUS IN ("+Constant.CAMPAIGN_CHECK_STATUS_02+","+Constant.CAMPAIGN_CHECK_STATUS_05+")\n");
		sql.append(" AND TCSE.ORG_ID = ").append(orgId).append("\n");

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
	 * 根据执行ID查询活动费用
	 * 
	 * @param :执行ID
	 * @return :活动费用信息
	 */
	public static List<Map<String, Object>> getActivitiesMediaCost(String executeId) {
		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT TCMC.COST_ID,\n");
		sql.append("       TCMC.EXECUTE_ID,\n");
		sql.append("       TCMC.COST_TYPE,\n");
		sql.append("       TCMC.ADV_SUBJECT,\n");
		sql.append("       TO_CHAR(TCMC.ADV_DATE,'YYYY-MM-DD') ADV_DATE,\n");
		sql.append("       TO_CHAR(TCMC.END_DATE,'YYYY-MM-DD') END_DATE,\n");
		sql.append("       TCMC.PAYMENT_ACCOUNT,\n");
		sql.append("       NVL(TCMC.ITEM_PRICE, 0) ITEM_PRICE,\n");
		sql.append("       NVL(TCMC.ITEM_COUNT, 0) ITEM_COUNT,\n");
		sql.append("       NVL(TCMC.PLAN_COST, 0) PLAN_COST,\n");
		sql.append("       NVL(TCMC.REAL_COST, 0) REAL_COST,\n");
		sql.append("       NVL(TCMC.ITEM_COST, 0) ITEM_COST,\n");
		sql.append("       TCMC.MEDIA_TYPE,\n");
		sql.append("       TCMC.REGION,\n");
		sql.append("       TMR.REGION_NAME,\n");
		sql.append("       TCMC.MEDIA_MODEL,\n");
		sql.append("       TCMC.MEDIA_NAME,\n");
		sql.append("       TCMC.MEDIA_PUBLISH,\n");
		sql.append("       TCMC.MEDIA_SIZE,\n");
		sql.append("       TCMC.MEDIA_COLUMN,\n");
		sql.append("       NVL(TCMC.TOTAL_COUNT, 0) TOTAL_COUNT\n");
		sql.append("  FROM TT_CAM_MEDIA_COST TCMC, TM_REGION TMR\n");
		sql.append(" WHERE 1 = 1\n");
		sql.append("   AND TCMC.REGION = TMR.REGION_ID(+)\n");
		sql.append("   AND TCMC.EXECUTE_ID = ").append(executeId).append("\n");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, dao.getFunName());

		return list;
	}

	public static Map<String, Object> activitiesInfoQuery(String campaignId, String orgId) throws Exception {
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

	public static List<Map<String, Object>> activitiesInfoQuery_EXECUTE(String campaignId, String orgId) throws Exception {
		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT TORG.ORG_ID,\n");
		sql.append("       TCP.PLAN_ID,\n");
		sql.append("       TORG.ORG_CODE,\n");
		sql.append("       TORG.ORG_NAME,\n");
		sql.append("       TCP.TO_PLACE_COUNT,\n");
		sql.append("       TCP.TO_TEL_STORE_COUNT,\n");
		sql.append("       TCP.CREATE_CARDS_COUNT,\n");
		sql.append("       TCP.ORDER_COUNT,\n");
		sql.append("       TCP.TURN_CAR_COUNT,\n");
		sql.append("       TCP.PROJECT_NAME,\n");
		sql.append("	   TO_CHAR(TCP.EXECUTION_TIME_B, 'YYYY-MM-DD') as EXECUTION_TIME_B,\n");
		sql.append("       TO_CHAR(TCP.EXECUTION_TIME_E, 'YYYY-MM-DD') as EXECUTION_TIME_E,\n");

		sql.append("       TCP.ALL_COST,\n");
		sql.append("       TCP.COMPANY_COST,\n");
		sql.append("       TCP.COST_TYPE,\n");
		sql.append("       TCP.PLAN_TYPE\n");
		sql.append("  FROM TT_CAMPAIGN_PLAN TCP, TM_ORG TORG\n");
		sql.append(" WHERE TCP.CAMPAIGN_ID = " + campaignId + "\n");
		sql.append("   AND TCP.ORG_ID = TORG.ORG_ID\n");
		sql.append("   AND EXISTS (SELECT 1\n");
		sql.append("          FROM vw_org_dealer ORG\n");
		sql.append("         WHERE TCP.ORG_ID = ORG.PQ_ORG_ID\n");
		sql.append("           AND ORG.ROOT_ORG_ID = " + orgId + ")");

		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}

	public static List activitiesInfoQuery_DEL(String campaignId, String orgId, String[] orgIds) throws Exception {
		StringBuffer sql = new StringBuffer("\n");
		String str = "";
		for (int i = 0; i < orgIds.length; i++) {
			if (i == 0) {
				str += orgIds[i];
			} else {
				str += "," + orgIds[i];
			}
		}

		sql.append("SELECT *\n");
		sql.append("  FROM TT_CAMPAIGN_PLAN T\n");
		sql.append(" WHERE T.PLAN_ID IN\n");
		sql.append("       (SELECT TCSE.PLAN_ID\n");
		sql.append("          FROM TT_CAMPAIGN_PLAN TCSE, TM_ORG TORG\n");
		sql.append("         WHERE TCSE.CAMPAIGN_ID = " + campaignId + "\n");
		sql.append("           AND TCSE.ORG_ID = TORG.ORG_ID\n");
		sql.append("           AND EXISTS (SELECT 1\n");//登陆人所属大区中欲下发方案的小区
		sql.append("                  FROM vw_org_dealer ORG\n");
		sql.append("                 WHERE ORG.PQ_ORG_ID = TCSE.ORG_ID\n");
		sql.append("                   AND ORG.ROOT_ORG_ID = " + orgId + ")\n");
		sql.append("           AND NOT EXISTS\n");
		sql.append("         (SELECT 1\n");//删除 TT_CAMPAIGN_PLAN 表中小区不在 str 中的小区
		sql.append("                  FROM vw_org_dealer ORG\n");
		sql.append("                 WHERE ORG.PQ_ORG_ID = TCSE.ORG_ID\n");
		sql.append("                   AND ORG.PQ_ORG_ID IN (" + str + ")))");

		return dao.select(TtCampaignPlanPO.class, sql.toString(), null);
	}

//	/**
//	 * 查询大区或总部的可用费用
//	 * 
//	 * @param orgId
//	 * @param areaId
//	 * @param costType
//	 * @return
//	 */
//	public static String getCost(Long orgId, String areaId, Integer costType) {
//		String cost = "";
//
//		StringBuffer sql = new StringBuffer("\n");
//
//		sql.append("SELECT TVC.AVAILABLE_AMOUNT,\n");
//		sql.append("       TVC.FREEZE_AMOUNT,\n");
//		sql.append("       TVC.COST_AMOUNT,\n");
//		sql.append("       TVC.COST_SOURCE,\n");
//		sql.append("       TVC.COST_TYPE\n");
//		sql.append("  FROM TT_VS_COST TVC\n");
//		sql.append(" WHERE TVC.ORG_ID = ").append(orgId).append("\n");
//		sql.append("   AND TVC.AREA_ID = ").append(areaId).append("\n");
//		sql.append("   AND TVC.COST_TYPE = ").append(costType).append("\n");
//
//		List<Map<String, Object>> costList = dao.pageQuery(sql.toString(), null, dao.getFunName());
//
//		if (!ActivitiesSpacePlanReport.isNullList(costList)) {
//			cost = costList.get(0).get("AVAILABLE_AMOUNT").toString();
//		}
//
//		if (CommonUtils.isNullString(cost)) {
//			cost = "0";
//		}
//
//		return cost;
//	}

	/**
	 * 通过职位获得业务范围
	 * 
	 * @param poseId
	 * @return List
	 */
	public static List<Map<String, Object>> getBussArea(Long poseId) {
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TBA.AREA_ID, TBA.AREA_NAME\n");
		sql.append("  FROM TM_BUSINESS_AREA TBA, TM_POSE_BUSINESS_AREA TPBA\n");
		sql.append(" WHERE TBA.AREA_ID = TPBA.AREA_ID\n");
		sql.append("   AND TPBA.POSE_ID = ").append(poseId).append("\n");

		List<Map<String, Object>> areaList = dao.pageQuery(sql.toString(), null, dao.getFunName());

		return areaList;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
