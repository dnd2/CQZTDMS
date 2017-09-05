package com.infodms.dms.dao.sales.marketmanage.planmanage;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ActivitiesMediaCostDao  extends BaseDao{
	public static Logger logger = Logger.getLogger(ActivitiesMediaCostDao.class);
	private static final ActivitiesMediaCostDao dao = new ActivitiesMediaCostDao ();
	public static final ActivitiesMediaCostDao getInstance() {
		return dao;
	}
	
	public static Map<String, Object> meidaCostQuery(String isFleet, String costType, String dutyType, String pageOrgId, String orgId, String startTime, String endTime, String areaId, String region, String model, String mediaType, String poseId, String mediaName, int pageSize, int curPage) {
		boolean orgFlag = Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType) || (!CommonUtils.isNullString(pageOrgId) && Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType) && !orgId.toString().equals(pageOrgId)) ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select *\n");
		sql.append("  from (select tcmc.cost_id,\n");  
		sql.append("               tcmc.execute_id,\n");  
		sql.append("               tcmc.cost_type,\n");  
		sql.append("               tcmc.adv_subject,\n");  
		sql.append("               to_char(tcmc.adv_date, 'yyyy-mm-dd') adv_date,\n");  
		sql.append("               to_char(tcmc.end_date, 'yyyy-mm-dd') end_date,\n");  
		sql.append("               tcmc.payment_account,\n");  
		sql.append("               nvl(tcmc.item_price, 0) item_price,\n");  
		sql.append("               nvl(tcmc.item_count, 0) item_count,\n");  
		sql.append("               nvl(tcmc.plan_cost, 0) plan_cost,\n");  
		sql.append("               nvl(tcmc.real_cost, 0) real_cost,\n");  
		sql.append("               nvl(tcmc.item_cost, 0) item_cost,\n");  
		sql.append("               tcmc.media_type,\n");  
		sql.append("               tcmc.region,\n");  
		sql.append("               tr.region_name,\n");  
		sql.append("               tcmc.media_model,\n");  
		sql.append("               tcmc.media_name,\n");  
		sql.append("               tcmc.media_publish,\n");  
		sql.append("               tcmc.media_size,\n");  
		sql.append("               tcmc.media_column,\n"); 
		sql.append("               tba.area_id,\n");  
		sql.append("               tba.area_name,\n");  
		sql.append("               tcmc.create_date,\n");  
		sql.append("               tmo.org_name,\n");  
		sql.append("               tc.campaign_type,\n");  
		sql.append("               tpba.pose_id,\n");  
		sql.append("               tcse.check_status,\n");  
		sql.append("               tmo.org_id,\n");  
		sql.append("               nvl(tcmc.total_count, 0) total_count\n");  
		sql.append("          from tt_cam_media_cost         tcmc,\n");  
		sql.append("               tt_campaign_space_execute tcse,\n");  
		sql.append("               tt_campaign               tc,\n");  
		sql.append("               tm_business_area          tba,\n");  
		sql.append("               tm_pose_business_area     tpba,\n");  
		sql.append("               tm_region                 tr,\n");  
		sql.append("               tm_org                    tmo\n");  
		sql.append("         where tcmc.execute_id = tcse.space_id\n");  
		sql.append("           and tcse.campaign_id = tc.campaign_id\n");  
		sql.append("           and tc.area_id = tba.area_id\n");  
		sql.append("           and tc.area_id = tpba.area_id\n");  
		sql.append("           and tcmc.region = tr.region_code\n");  
		sql.append("           and tcse.org_id = tmo.org_id\n");  
		sql.append("           and tc.campaign_type = ").append(Constant.CAMPAIGN_TYPE_03).append("\n");  
		sql.append("        union all\n");  
		sql.append("        select tcmc.cost_id,\n");  
		sql.append("               tcmc.execute_id,\n");  
		sql.append("               tcmc.cost_type,\n");  
		sql.append("               tcmc.adv_subject,\n");  
		sql.append("               to_char(tcmc.adv_date, 'yyyy-mm-dd') adv_date,\n");  
		sql.append("               to_char(tcmc.end_date, 'yyyy-mm-dd') end_date,\n");  
		sql.append("               tcmc.payment_account,\n");  
		sql.append("               nvl(tcmc.item_price, 0) item_price,\n");  
		sql.append("               nvl(tcmc.item_count, 0) item_count,\n");  
		sql.append("               nvl(tcmc.plan_cost, 0) plan_cost,\n");  
		sql.append("               nvl(tcmc.real_cost, 0) real_cost,\n");  
		sql.append("               nvl(tcmc.item_cost, 0) item_cost,\n");  
		sql.append("               tcmc.media_type,\n");  
		sql.append("               tcmc.region,\n");  
		sql.append("               tr.region_name,\n");  
		sql.append("               tcmc.media_model,\n");  
		sql.append("               tcmc.media_name,\n");  
		sql.append("               tcmc.media_publish,\n");  
		sql.append("               tcmc.media_size,\n");  
		sql.append("               tcmc.media_column,\n"); 
		sql.append("               tba.area_id,\n");  
		sql.append("               tba.area_name,\n");  
		sql.append("               tcmc.create_date,\n");  
		sql.append("               tmo.org_name,\n");  
		sql.append("               tc.campaign_type,\n");  
		sql.append("               tpba.pose_id,\n");  
		sql.append("               tce.check_status,\n");  
		sql.append("               tmo.org_id,\n");  
		sql.append("               nvl(tcmc.total_count, 0) total_count\n");  
		sql.append("          from tt_cam_media_cost     tcmc,\n");  
		sql.append("               tt_campaign_execute   tce,\n");  
		sql.append("               tt_campaign           tc,\n");  
		sql.append("               tm_business_area      tba,\n");  
		sql.append("               tm_pose_business_area tpba,\n");  
		sql.append("               tm_region             tr,\n");  
		sql.append("               tm_org                tmo\n");  
		sql.append("         where tcmc.execute_id = tce.execute_id\n");  
		sql.append("           and tce.campaign_id = tc.campaign_id\n");  
		sql.append("           and tc.area_id = tba.area_id\n");  
		sql.append("           and tc.area_id = tpba.area_id\n");  
		sql.append("           and tcmc.region = tr.region_code\n");  
		sql.append("           and tce.org_id = tmo.org_id\n");  
		sql.append("           and tc.campaign_type in (").append(Constant.CAMPAIGN_TYPE_01).append(", ").append(Constant.CAMPAIGN_TYPE_02).append(")) l\n");  
		sql.append(" where l.check_status in (").append(Constant.CAMPAIGN_CHECK_STATUS_06).append(",").append(Constant.CAMPAIGN_CHECK_STATUS_07).append(",").append(Constant.CAMPAIGN_CHECK_STATUS_08).append(",").append(Constant.CAMPAIGN_CHECK_STATUS_09).append(",").append(Constant.CAMPAIGN_CHECK_STATUS_10).append(")\n");

		if(orgFlag) {
			if(!CommonUtils.isNullString(pageOrgId)) {
				orgId = pageOrgId ;
			}
			
			sql.append(" AND L.ORG_ID = ").append(orgId).append("\n");
		}

		sql.append("   AND L.POSE_ID = ").append(poseId).append("\n");
		
		if(!CommonUtils.isNullString(startTime) && CommonUtils.isNullString(endTime)) {
			sql.append("   AND L.ADV_DATE >= '").append(startTime).append("'\n");
		}
		
		if(CommonUtils.isNullString(startTime) && !CommonUtils.isNullString(endTime)) {
			sql.append("   AND L.END_DATE <= '").append(endTime).append("'\n");
		}
		
		if(!CommonUtils.isNullString(startTime) && !CommonUtils.isNullString(endTime)) {
			sql.append("   AND ((L.ADV_DATE >= '").append(startTime).append("' AND L.ADV_DATE <= '").append(endTime).append("') OR (L.END_DATE >= '").append(startTime).append("' AND L.END_DATE <= '").append(endTime).append("'))\n");
		}
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("   AND L.AREA_ID in (").append(areaId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(region)) {
			sql.append("   AND L.REGION = ").append(region).append("\n");
		}
		
		if(!CommonUtils.isNullString(model)) {
			sql.append("   AND L.MEDIA_MODEL = ").append(model).append("\n");
		}
		
		if(!CommonUtils.isNullString(mediaType)) {
			sql.append("   AND L.MEDIA_TYPE = ").append(mediaType).append("\n");
		}
		
		if(!CommonUtils.isNullString(costType)) {
			sql.append("   AND L.COST_TYPE = ").append(costType).append("\n");
		} else {
			if(Constant.STATUS_ENABLE.toString().equals(isFleet)) {
				sql.append("   and l.cost_type in (").append(Constant.COST_TYPE_05).append(")\n");
			} else if(Constant.STATUS_DISABLE.toString().equals(isFleet)) {
				sql.append("   and l.cost_type in (").append(Constant.COST_TYPE_01).append(",").append(Constant.COST_TYPE_02).append(",").append(Constant.COST_TYPE_03).append(",").append(Constant.COST_TYPE_04).append(",").append(Constant.COST_TYPE_06).append(",").append(Constant.COST_TYPE_07).append(")\n");
			}
		}
		
		if(!CommonUtils.isNullString(mediaName)) {
			sql.append("   AND L.MEDIA_NAME LIKE '%").append(mediaName).append("%'\n");
		}
		
		sql.append("ORDER BY L.CREATE_DATE DESC, L.EXECUTE_ID") ;
		
		Map<String, Object> mediaCostMap = new HashMap<String, Object>() ;
		
		List<Map<String, Object>> mediaCostList = dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		
		PageResult<Map<String, Object>> mediaCostResult = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage) ;
		
		mediaCostMap.put("mediaCostList", mediaCostList) ;
		mediaCostMap.put("mediaCostResult", mediaCostResult) ;
		
		return mediaCostMap ;
	}
	
	public static List<Map<String, Object>> meidaCostDownload(String isFleet, String costType, String dutyType, String pageOrgId, String orgId, String startTime, String endTime, String areaId, String region, String model, String mediaType, String poseId, String mediaName) {
		boolean orgFlag = Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType) || (!CommonUtils.isNullString(pageOrgId) && Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType) && !orgId.toString().equals(pageOrgId)) ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select *\n");
		sql.append("  from (select tcmc.cost_id,\n");  
		sql.append("               tcmc.execute_id,\n");  
		sql.append("               tcmc.cost_type,\n"); 
		sql.append("               tcc1.code_desc cost_typeA,\n");  
		sql.append("               tcmc.adv_subject,\n");  
		sql.append("               to_char(tcmc.adv_date, 'yyyy-mm-dd') adv_date,\n");  
		sql.append("               to_char(tcmc.end_date, 'yyyy-mm-dd') end_date,\n");  
		sql.append("               tcmc.payment_account,\n");  
		sql.append("               nvl(tcmc.item_price, 0) item_price,\n");  
		sql.append("               nvl(tcmc.item_count, 0) item_count,\n");  
		sql.append("               nvl(tcmc.plan_cost, 0) plan_cost,\n");  
		sql.append("               nvl(tcmc.real_cost, 0) real_cost,\n");  
		sql.append("               nvl(tcmc.item_cost, 0) item_cost,\n"); 
		sql.append("               tcmc.media_type,\n"); 
		sql.append("               tcc2.code_desc media_typeA,\n");  
		sql.append("               tcmc.region,\n");  
		sql.append("               tr.region_name,\n");  
		sql.append("               tcmc.media_model,\n");
		sql.append("               tcc3.code_desc media_modelA,\n");  
		sql.append("               tcmc.media_name,\n");  
		sql.append("               tcmc.media_publish,\n");  
		sql.append("               tcmc.media_size,\n");  
		sql.append("               tcmc.media_column,\n");  
		sql.append("               tba.area_id,\n");  
		sql.append("               tba.area_name,\n");  
		sql.append("               tcmc.create_date,\n");  
		sql.append("               tmo.org_name,\n");  
		sql.append("               tc.campaign_type,\n");  
		sql.append("               tpba.pose_id,\n");  
		sql.append("               tcse.check_status,\n");  
		sql.append("               tmo.org_id,\n");  
		sql.append("               nvl(tcmc.total_count, 0) total_count\n");  
		sql.append("          from tt_cam_media_cost         tcmc,\n");  
		sql.append("               tt_campaign_space_execute tcse,\n");  
		sql.append("               tt_campaign               tc,\n");  
		sql.append("               tm_business_area          tba,\n");  
		sql.append("               tm_pose_business_area     tpba,\n");  
		sql.append("               tm_region                 tr,\n");  
		sql.append("               tm_org                    tmo,\n");  
		sql.append("               tc_code                   tcc1,\n");  
		sql.append("               tc_code                   tcc2,\n");  
		sql.append("               tc_code                   tcc3\n");  
		sql.append("         where tcmc.execute_id = tcse.space_id\n");  
		sql.append("           and tcse.campaign_id = tc.campaign_id\n");  
		sql.append("           and tc.area_id = tba.area_id\n");  
		sql.append("           and tc.area_id = tpba.area_id\n");  
		sql.append("           and tcmc.region = tr.region_code\n");  
		sql.append("           and tcse.org_id = tmo.org_id\n");  
		sql.append("           and tcmc.cost_type = tcc1.code_id\n");  
		sql.append("           and tcmc.media_type = tcc2.code_id\n");  
		sql.append("           and tcmc.media_model = tcc3.code_id\n");  
		sql.append("           and tc.campaign_type = ").append(Constant.CAMPAIGN_TYPE_03).append("\n");  
		sql.append("        union all\n");  
		sql.append("        select tcmc.cost_id,\n");  
		sql.append("               tcmc.execute_id,\n");  
		sql.append("               tcmc.cost_type,\n"); 
		sql.append("               tcc1.code_desc cost_typeA,\n");  
		sql.append("               tcmc.adv_subject,\n");  
		sql.append("               to_char(tcmc.adv_date, 'yyyy-mm-dd') adv_date,\n");  
		sql.append("               to_char(tcmc.end_date, 'yyyy-mm-dd') end_date,\n");  
		sql.append("               tcmc.payment_account,\n");  
		sql.append("               nvl(tcmc.item_price, 0) item_price,\n");  
		sql.append("               nvl(tcmc.item_count, 0) item_count,\n");  
		sql.append("               nvl(tcmc.plan_cost, 0) plan_cost,\n");  
		sql.append("               nvl(tcmc.real_cost, 0) real_cost,\n");  
		sql.append("               nvl(tcmc.item_cost, 0) item_cost,\n");
		sql.append("               tcmc.media_type,\n"); 
		sql.append("               tcc2.code_desc media_typeA,\n");  
		sql.append("               tcmc.region,\n");  
		sql.append("               tr.region_name,\n");  
		sql.append("               tcmc.media_model,\n");  
		sql.append("               tcc3.code_desc media_modelA,\n");  
		sql.append("               tcmc.media_name,\n");  
		sql.append("               tcmc.media_publish,\n");  
		sql.append("               tcmc.media_size,\n");  
		sql.append("               tcmc.media_column,\n");  
		sql.append("               tba.area_id,\n");  
		sql.append("               tba.area_name,\n");  
		sql.append("               tcmc.create_date,\n");  
		sql.append("               tmo.org_name,\n");  
		sql.append("               tc.campaign_type,\n");  
		sql.append("               tpba.pose_id,\n");  
		sql.append("               tce.check_status,\n");  
		sql.append("               tmo.org_id,\n");  
		sql.append("               nvl(tcmc.total_count, 0) total_count\n");  
		sql.append("          from tt_cam_media_cost     tcmc,\n");  
		sql.append("               tt_campaign_execute   tce,\n");  
		sql.append("               tt_campaign           tc,\n");  
		sql.append("               tm_business_area      tba,\n");  
		sql.append("               tm_pose_business_area tpba,\n");  
		sql.append("               tm_region             tr,\n");  
		sql.append("               tm_org                tmo,\n");  
		sql.append("               tc_code               tcc1,\n");  
		sql.append("               tc_code               tcc2,\n");  
		sql.append("               tc_code               tcc3\n");
		sql.append("         where tcmc.execute_id = tce.execute_id\n");  
		sql.append("           and tce.campaign_id = tc.campaign_id\n");  
		sql.append("           and tc.area_id = tba.area_id\n");  
		sql.append("           and tc.area_id = tpba.area_id\n");  
		sql.append("           and tcmc.region = tr.region_code\n");  
		sql.append("           and tce.org_id = tmo.org_id\n");  
		sql.append("           and tcmc.cost_type = tcc1.code_id\n");  
		sql.append("           and tcmc.media_type = tcc2.code_id\n");  
		sql.append("           and tcmc.media_model = tcc3.code_id\n");  
		sql.append("           and tc.campaign_type in (").append(Constant.CAMPAIGN_TYPE_01).append(", ").append(Constant.CAMPAIGN_TYPE_02).append(")) l\n");  
		sql.append(" where l.check_status in (").append(Constant.CAMPAIGN_CHECK_STATUS_06).append(",").append(Constant.CAMPAIGN_CHECK_STATUS_07).append(",").append(Constant.CAMPAIGN_CHECK_STATUS_08).append(",").append(Constant.CAMPAIGN_CHECK_STATUS_09).append(",").append(Constant.CAMPAIGN_CHECK_STATUS_10).append(")\n");

		if(orgFlag) {
			if(!CommonUtils.isNullString(pageOrgId)) {
				orgId = pageOrgId ;
			}
			
			sql.append(" AND L.ORG_ID = ").append(orgId).append("\n");
		}

		sql.append("   AND L.POSE_ID = ").append(poseId).append("\n");
		
		if(!CommonUtils.isNullString(startTime) && CommonUtils.isNullString(endTime)) {
			sql.append("   AND L.ADV_DATE >= '").append(startTime).append("'\n");
		}
		
		if(CommonUtils.isNullString(startTime) && !CommonUtils.isNullString(endTime)) {
			sql.append("   AND L.END_DATE <= '").append(endTime).append("'\n");
		}
		
		if(!CommonUtils.isNullString(startTime) && !CommonUtils.isNullString(endTime)) {
			sql.append("   AND ((L.ADV_DATE >= '").append(startTime).append("' AND L.ADV_DATE <= '").append(endTime).append("') OR (L.END_DATE >= '").append(startTime).append("' AND L.END_DATE <= '").append(endTime).append("'))\n");
		}
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("   AND L.AREA_ID in (").append(areaId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(region)) {
			sql.append("   AND L.REGION = ").append(region).append("\n");
		}
		
		if(!CommonUtils.isNullString(model)) {
			sql.append("   AND L.MEDIA_MODEL = ").append(model).append("\n");
		}
		
		if(!CommonUtils.isNullString(mediaType)) {
			sql.append("   AND L.MEDIA_TYPE = ").append(mediaType).append("\n");
		}
		
		if(!CommonUtils.isNullString(costType)) {
			sql.append("   AND L.COST_TYPE = ").append(costType).append("\n");
		} else {
			if(Constant.STATUS_ENABLE.toString().equals(isFleet)) {
				sql.append("   and l.cost_type in (").append(Constant.COST_TYPE_05).append(")\n");
			} else if(Constant.STATUS_DISABLE.toString().equals(isFleet)) {
				sql.append("   and l.cost_type in (").append(Constant.COST_TYPE_01).append(",").append(Constant.COST_TYPE_02).append(",").append(Constant.COST_TYPE_03).append(",").append(Constant.COST_TYPE_04).append(",").append(Constant.COST_TYPE_06).append(",").append(Constant.COST_TYPE_07).append(")\n");
			}
		} 
		
		if(!CommonUtils.isNullString(mediaName)) {
			sql.append("   AND L.MEDIA_NAME LIKE '%").append(mediaName).append("%'\n");
		}
		
		sql.append("ORDER BY L.CREATE_DATE DESC, L.EXECUTE_ID") ;
		
		List<Map<String, Object>> mediaCostList = dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		
		return mediaCostList ;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
