package com.infodms.dms.dao.sales.marketmanage.planmanage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ActivitiesCamCostDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ActivitiesCamCostDao.class);
	
	public PageResult<Map<String, Object>> activityCamQuery(Map<String, String> map, int pageSize, int curPage) {
		String orgId = map.get("orgId") ;
		String areaId = map.get("areaId") ;
		String startDate = map.get("startDate") ;
		String endDate = map.get("endDate") ;
		String region = map.get("region") ;
		String costType = map.get("costType") ;
		String activityType = map.get("activityType") ;
		String isFleet = map.get("isFleet") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select l.org_name,\n");
		sql.append("       l.area_name,\n");  
		sql.append("       l.cost_typeA,\n");  
		sql.append("       l.region_name,\n");  
		sql.append("       l.activity_typeA,\n");  
		sql.append("       l.start_date,\n");  
		sql.append("       l.end_date,\n");  
		sql.append("       l.activity_content,\n");  
		sql.append("       l.item_name,\n");  
		sql.append("       l.item_remark,\n");  
		sql.append("       l.item_count,\n");  
		sql.append("       l.item_price,\n");  
		sql.append("       l.plan_cost\n");  
		sql.append("  from (select tmo.org_name,\n");  
		sql.append("               tmo.org_id,\n"); 
		sql.append("               tba.area_name,\n"); 
		sql.append("               tba.area_id,\n"); 
		sql.append("               tccc.cost_type,\n");  
		sql.append("               tc1.code_desc         cost_typeA,\n");  
		sql.append("               tmr.region_name,\n"); 
		sql.append("               tccc.region,\n");  
		sql.append("               tccc.activity_type,\n");  
		sql.append("               tc2.code_desc         activity_typeA,\n");  
		sql.append("               to_char(tccc.start_date, 'yyyy-mm-dd') start_date,\n");  
		sql.append("               to_char(tccc.end_date, 'yyyy-mm-dd') end_date,\n");  
		sql.append("               tccc.activity_content,\n");  
		sql.append("               tccc.item_name,\n");  
		sql.append("               tccc.item_remark,\n");  
		sql.append("               tccc.item_count,\n");  
		sql.append("               tccc.item_price,\n");  
		sql.append("               tccc.plan_cost,\n"); 
		sql.append("               tccc.create_date,\n");  
		sql.append("               tcse.check_status\n");  
		sql.append("          from tt_cam_campaign_cost      tccc,\n");  
		sql.append("               tt_campaign               tc,\n");  
		sql.append("               tt_campaign_space_execute tcse,\n");  
		sql.append("               tm_org                    tmo,\n");  
		sql.append("               tm_business_area          tba,\n");  
		sql.append("               tm_region                 tmr,\n");  
		sql.append("               tc_code                   tc1,\n");  
		sql.append("               tc_code                   tc2\n");  
		sql.append("         where tccc.execute_id = tcse.space_id\n");  
		sql.append("           and tcse.campaign_id = tc.campaign_id\n");  
		sql.append("           and tcse.org_id = tmo.org_id\n");  
		sql.append("           and tc.area_id = tba.area_id\n");  
		sql.append("           and tccc.region = tmr.region_code(+)\n");  
		sql.append("           and tccc.cost_type = tc1.code_id\n");  
		sql.append("           and tccc.activity_type = tc2.code_id\n");  
		sql.append("           and tc.campaign_type = ").append(Constant.CAMPAIGN_TYPE_03).append("\n");  
		sql.append("        union all\n");  
		sql.append("        select tmo.org_name,\n");  
		sql.append("               tmo.org_id,\n"); 
		sql.append("               tba.area_name,\n");  
		sql.append("               tba.area_id,\n");
		sql.append("               tccc.cost_type,\n");  
		sql.append("               tc1.code_desc         cost_typeA,\n");  
		sql.append("               tmr.region_name,\n");  
		sql.append("               tccc.region,\n");  
		sql.append("               tccc.activity_type,\n");  
		sql.append("               tc2.code_desc         activity_typeA,\n");  
		sql.append("               to_char(tccc.start_date, 'yyyy-mm-dd') start_date,\n");  
		sql.append("               to_char(tccc.end_date, 'yyyy-mm-dd') end_date,\n");  
		sql.append("               tccc.activity_content,\n");  
		sql.append("               tccc.item_name,\n");  
		sql.append("               tccc.item_remark,\n");  
		sql.append("               tccc.item_count,\n");  
		sql.append("               tccc.item_price,\n");  
		sql.append("               tccc.plan_cost,\n"); 
		sql.append("               tccc.create_date,\n");  
		sql.append("               tce.check_status\n");  
		sql.append("          from tt_cam_campaign_cost tccc,\n");  
		sql.append("               tt_campaign          tc,\n");  
		sql.append("               tt_campaign_execute  tce,\n");  
		sql.append("               tm_org               tmo,\n");  
		sql.append("               tm_business_area     tba,\n");  
		sql.append("               tm_region            tmr,\n");  
		sql.append("               tc_code              tc1,\n");  
		sql.append("               tc_code              tc2\n");  
		sql.append("         where tccc.execute_id = tce.execute_id\n");  
		sql.append("           and tce.campaign_id = tc.campaign_id\n");  
		sql.append("           and tce.org_id = tmo.org_id\n");  
		sql.append("           and tc.area_id = tba.area_id\n");  
		sql.append("           and tccc.region = tmr.region_code(+)\n");  
		sql.append("           and tccc.cost_type = tc1.code_id\n");  
		sql.append("           and tccc.activity_type = tc2.code_id\n");  
		sql.append("           and tc.campaign_type in (").append(Constant.CAMPAIGN_TYPE_01).append(", ").append(Constant.CAMPAIGN_TYPE_02).append(")) l\n");  
		sql.append(" where l.check_status in (").append(Constant.CAMPAIGN_CHECK_STATUS_06).append(", ").append(Constant.CAMPAIGN_CHECK_STATUS_07).append(", ").append(Constant.CAMPAIGN_CHECK_STATUS_08).append(", ").append(Constant.CAMPAIGN_CHECK_STATUS_09).append(", ").append(Constant.CAMPAIGN_CHECK_STATUS_10).append(")\n");
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("   and l.org_id = ").append(orgId).append("\n") ;
		}
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("   and l.area_id in (").append(areaId).append(")\n") ;
		}
		
		if(!CommonUtils.isNullString(startDate) && CommonUtils.isNullString(endDate)) {
			sql.append("   and l.start_date >= '").append(startDate).append("'\n");
		}
		
		if(CommonUtils.isNullString(startDate) && !CommonUtils.isNullString(endDate)) {
			sql.append("   and l.end_date <= '").append(endDate).append("'\n");
		}
		
		if(!CommonUtils.isNullString(startDate) && !CommonUtils.isNullString(endDate)) {
			sql.append("   and ((l.start_date >= '").append(startDate).append("' and l.start_date <= '").append(endDate).append("') or (l.end_date >= '").append(startDate).append("' and l.end_date <= '").append(endDate).append("'))\n");
		}
		
		if(!CommonUtils.isNullString(region)) {
			sql.append("   and l.region = ").append(region).append("\n");
		}
		
		if(!CommonUtils.isNullString(costType)) {
			sql.append("   and l.cost_type = ").append(costType).append("\n");
		} else {
			if(Constant.STATUS_ENABLE.toString().equals(isFleet)) {
				sql.append("   and l.cost_type in (").append(Constant.COST_TYPE_05).append(")\n");
			} else if(Constant.STATUS_DISABLE.toString().equals(isFleet)) {
				sql.append("   and l.cost_type in (").append(Constant.COST_TYPE_01).append(",").append(Constant.COST_TYPE_02).append(",").append(Constant.COST_TYPE_03).append(",").append(Constant.COST_TYPE_04).append(",").append(Constant.COST_TYPE_06).append(",").append(Constant.COST_TYPE_07).append(")\n");
			}
		}
		
		if(!CommonUtils.isNullString(activityType)) {
			sql.append("   and l.activity_type = ").append(activityType).append("\n");
		}
		
		sql.append(" order by l.create_date desc") ;
		
		return super.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage) ;
	}
	
	public Map<String, Object> getTotal(Map<String, String>  map) {
		String orgId = map.get("orgId") ;
		String areaId = map.get("areaId") ;
		String startDate = map.get("startDate") ;
		String endDate = map.get("endDate") ;
		String region = map.get("region") ;
		String costType = map.get("costType") ;
		String activityType = map.get("activityType") ;
		String isFleet = map.get("isFleet") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select nvl(sum(l.item_count), 0) total_count, nvl(sum(l.plan_cost), 0) total_cost\n");
		sql.append("  from (select tcse.check_status,\n");  
		sql.append("               tc.area_id,\n");  
		sql.append("               tcse.org_id,\n");  
		sql.append("               tccc.cost_type,\n");  
		sql.append("               tccc.activity_type,\n");  
		sql.append("               to_char(tccc.start_date, 'yyyy-mm-dd') start_date,\n");  
		sql.append("               to_char(tccc.end_date, 'yyyy-mm-dd') end_date,\n");  
		sql.append("               tccc.region,\n");  
		sql.append("               nvl(sum(tccc.item_count), 0) item_count,\n");  
		sql.append("               nvl(sum(tccc.plan_cost), 0) plan_cost\n");  
		sql.append("          from tt_cam_campaign_cost      tccc,\n");  
		sql.append("               tt_campaign               tc,\n");  
		sql.append("               tt_campaign_space_execute tcse\n");  
		sql.append("         where tccc.execute_id = tcse.space_id\n");  
		sql.append("           and tcse.campaign_id = tc.campaign_id\n");  
		sql.append("           and tc.campaign_type = ").append(Constant.CAMPAIGN_TYPE_03).append("\n");  
		sql.append("         group by tcse.check_status,\n");  
		sql.append("                  tc.area_id,\n");  
		sql.append("                  tcse.org_id,\n");  
		sql.append("                  tccc.cost_type,\n");  
		sql.append("                  tccc.activity_type,\n");  
		sql.append("                  tccc.start_date,\n");  
		sql.append("                  tccc.end_date,\n");  
		sql.append("                  tccc.region\n");  
		sql.append("        union all\n");  
		sql.append("        select tce.check_status,\n");  
		sql.append("               tc.area_id,\n");  
		sql.append("               tce.org_id,\n");  
		sql.append("               tccc.cost_type,\n");  
		sql.append("               tccc.activity_type,\n");  
		sql.append("               to_char(tccc.start_date, 'yyyy-mm-dd') start_date,\n");  
		sql.append("               to_char(tccc.end_date, 'yyyy-mm-dd') end_date,\n");  
		sql.append("               tccc.region,\n");  
		sql.append("               nvl(sum(tccc.item_count), 0) item_count,\n");  
		sql.append("               nvl(sum(tccc.plan_cost), 0) plan_cost\n");  
		sql.append("          from tt_cam_campaign_cost tccc,\n");  
		sql.append("               tt_campaign          tc,\n");  
		sql.append("               tt_campaign_execute  tce\n");  
		sql.append("         where tccc.execute_id = tce.execute_id\n");  
		sql.append("           and tce.campaign_id = tc.campaign_id\n");  
		sql.append("           and tc.campaign_type in (").append(Constant.CAMPAIGN_TYPE_01).append(", ").append(Constant.CAMPAIGN_TYPE_02).append(")\n");  
		sql.append("         group by tce.check_status,\n");  
		sql.append("                  tc.area_id,\n");  
		sql.append("                  tce.org_id,\n");  
		sql.append("                  tccc.cost_type,\n");  
		sql.append("                  tccc.activity_type,\n");  
		sql.append("                  tccc.start_date,\n");  
		sql.append("                  tccc.end_date,\n");  
		sql.append("                  tccc.region) l\n");  
		sql.append(" where l.check_status in (").append(Constant.CAMPAIGN_CHECK_STATUS_06).append(", ").append(Constant.CAMPAIGN_CHECK_STATUS_07).append(", ").append(Constant.CAMPAIGN_CHECK_STATUS_08).append(", ").append(Constant.CAMPAIGN_CHECK_STATUS_09).append(", ").append(Constant.CAMPAIGN_CHECK_STATUS_10).append(")\n");
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("   and l.org_id = ").append(orgId).append("\n") ;
		}
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("   and l.area_id in (").append(areaId).append(")\n") ;
		}
		
		if(!CommonUtils.isNullString(startDate) && CommonUtils.isNullString(endDate)) {
			sql.append("   and l.start_date >= '").append(startDate).append("'\n");
		}
		
		if(CommonUtils.isNullString(startDate) && !CommonUtils.isNullString(endDate)) {
			sql.append("   and l.end_date <= '").append(endDate).append("'\n");
		}
		
		if(!CommonUtils.isNullString(startDate) && !CommonUtils.isNullString(endDate)) {
			sql.append("   and ((l.start_date >= '").append(startDate).append("' and l.start_date <= '").append(endDate).append("') or (l.end_date >= '").append(startDate).append("' and l.end_date <= '").append(endDate).append("'))\n");
		}
		
		if(!CommonUtils.isNullString(region)) {
			sql.append("   and l.region = ").append(region).append("\n");
		}
		
		if(!CommonUtils.isNullString(costType)) {
			sql.append("   and l.cost_type = ").append(costType).append("\n");
		} else {
			if(Constant.STATUS_ENABLE.toString().equals(isFleet)) {
				sql.append("   and l.cost_type in (").append(Constant.COST_TYPE_05).append(")\n");
			} else {
				sql.append("   and l.cost_type in (").append(Constant.COST_TYPE_01).append(",").append(Constant.COST_TYPE_02).append(",").append(Constant.COST_TYPE_03).append(",").append(Constant.COST_TYPE_04).append(",").append(Constant.COST_TYPE_06).append(",").append(Constant.COST_TYPE_07).append(")\n");
			}
		}
		
		if(!CommonUtils.isNullString(activityType)) {
			sql.append("   and l.activity_type = ").append(activityType).append("\n");
		}
		
		return super.pageQueryMap(sql.toString(), null, getFunName()) ;
	}
	
	public List<Map<String, Object>> activityCamDownload(Map<String, String> map) {
		String orgId = map.get("orgId") ;
		String areaId = map.get("areaId") ;
		String startDate = map.get("startDate") ;
		String endDate = map.get("endDate") ;
		String region = map.get("region") ;
		String costType = map.get("costType") ;
		String activityType = map.get("activityType") ;
		String isFleet = map.get("isFleet") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select l.org_name,\n");
		sql.append("       l.area_name,\n");  
		sql.append("       l.cost_typeA,\n");  
		sql.append("       l.region_name,\n");  
		sql.append("       l.activity_typeA,\n");  
		sql.append("       l.start_date,\n");  
		sql.append("       l.end_date,\n");  
		sql.append("       l.activity_content,\n");  
		sql.append("       l.item_name,\n");  
		sql.append("       l.item_remark,\n");  
		sql.append("       l.item_count,\n");  
		sql.append("       l.item_price,\n");  
		sql.append("       l.plan_cost\n");  
		sql.append("  from (select tmo.org_name,\n");  
		sql.append("               tmo.org_id,\n"); 
		sql.append("               tba.area_name,\n"); 
		sql.append("               tba.area_id,\n"); 
		sql.append("               tccc.cost_type,\n");  
		sql.append("               tc1.code_desc         cost_typeA,\n");  
		sql.append("               tmr.region_name,\n"); 
		sql.append("               tccc.region,\n");  
		sql.append("               tccc.activity_type,\n");  
		sql.append("               tc2.code_desc         activity_typeA,\n");  
		sql.append("               to_char(tccc.start_date, 'yyyy-mm-dd') start_date,\n");  
		sql.append("               to_char(tccc.end_date, 'yyyy-mm-dd') end_date,\n");  
		sql.append("               tccc.activity_content,\n");  
		sql.append("               tccc.item_name,\n");  
		sql.append("               tccc.item_remark,\n");  
		sql.append("               tccc.item_count,\n");  
		sql.append("               tccc.item_price,\n");  
		sql.append("               tccc.plan_cost,\n"); 
		sql.append("               tccc.create_date,\n");  
		sql.append("               tcse.check_status\n");  
		sql.append("          from tt_cam_campaign_cost      tccc,\n");  
		sql.append("               tt_campaign               tc,\n");  
		sql.append("               tt_campaign_space_execute tcse,\n");  
		sql.append("               tm_org                    tmo,\n");  
		sql.append("               tm_business_area          tba,\n");  
		sql.append("               tm_region                 tmr,\n");  
		sql.append("               tc_code                   tc1,\n");  
		sql.append("               tc_code                   tc2\n");  
		sql.append("         where tccc.execute_id = tcse.space_id\n");  
		sql.append("           and tcse.campaign_id = tc.campaign_id\n");  
		sql.append("           and tcse.org_id = tmo.org_id\n");  
		sql.append("           and tc.area_id = tba.area_id\n");  
		sql.append("           and tccc.region = tmr.region_code(+)\n");  
		sql.append("           and tccc.cost_type = tc1.code_id\n");  
		sql.append("           and tccc.activity_type = tc2.code_id\n");  
		sql.append("           and tc.campaign_type = ").append(Constant.CAMPAIGN_TYPE_03).append("\n");  
		sql.append("        union all\n");  
		sql.append("        select tmo.org_name,\n");  
		sql.append("               tmo.org_id,\n"); 
		sql.append("               tba.area_name,\n");  
		sql.append("               tba.area_id,\n");
		sql.append("               tccc.cost_type,\n");  
		sql.append("               tc1.code_desc         cost_typeA,\n");  
		sql.append("               tmr.region_name,\n");  
		sql.append("               tccc.region,\n");  
		sql.append("               tccc.activity_type,\n");  
		sql.append("               tc2.code_desc         activity_typeA,\n");  
		sql.append("               to_char(tccc.start_date, 'yyyy-mm-dd') start_date,\n");  
		sql.append("               to_char(tccc.end_date, 'yyyy-mm-dd') end_date,\n");  
		sql.append("               tccc.activity_content,\n");  
		sql.append("               tccc.item_name,\n");  
		sql.append("               tccc.item_remark,\n");  
		sql.append("               tccc.item_count,\n");  
		sql.append("               tccc.item_price,\n");  
		sql.append("               tccc.plan_cost,\n"); 
		sql.append("               tccc.create_date,\n");  
		sql.append("               tce.check_status\n");  
		sql.append("          from tt_cam_campaign_cost tccc,\n");  
		sql.append("               tt_campaign          tc,\n");  
		sql.append("               tt_campaign_execute  tce,\n");  
		sql.append("               tm_org               tmo,\n");  
		sql.append("               tm_business_area     tba,\n");  
		sql.append("               tm_region            tmr,\n");  
		sql.append("               tc_code              tc1,\n");  
		sql.append("               tc_code              tc2\n");  
		sql.append("         where tccc.execute_id = tce.execute_id\n");  
		sql.append("           and tce.campaign_id = tc.campaign_id\n");  
		sql.append("           and tce.org_id = tmo.org_id\n");  
		sql.append("           and tc.area_id = tba.area_id\n");  
		sql.append("           and tccc.region = tmr.region_code(+)\n");  
		sql.append("           and tccc.cost_type = tc1.code_id\n");  
		sql.append("           and tccc.activity_type = tc2.code_id\n");  
		sql.append("           and tc.campaign_type in (").append(Constant.CAMPAIGN_TYPE_01).append(", ").append(Constant.CAMPAIGN_TYPE_02).append(")) l\n");  
		sql.append(" where l.check_status in (").append(Constant.CAMPAIGN_CHECK_STATUS_06).append(", ").append(Constant.CAMPAIGN_CHECK_STATUS_07).append(", ").append(Constant.CAMPAIGN_CHECK_STATUS_08).append(", ").append(Constant.CAMPAIGN_CHECK_STATUS_09).append(", ").append(Constant.CAMPAIGN_CHECK_STATUS_10).append(")\n");
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("   and l.org_id = ").append(orgId).append("\n") ;
		}
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("   and l.area_id in (").append(areaId).append(")\n") ;
		}
		
		if(!CommonUtils.isNullString(startDate) && CommonUtils.isNullString(endDate)) {
			sql.append("   and l.start_date >= '").append(startDate).append("'\n");
		}
		
		if(CommonUtils.isNullString(startDate) && !CommonUtils.isNullString(endDate)) {
			sql.append("   and l.end_date <= '").append(endDate).append("'\n");
		}
		
		if(!CommonUtils.isNullString(startDate) && !CommonUtils.isNullString(endDate)) {
			sql.append("   and ((l.start_date >= '").append(startDate).append("' and l.start_date <= '").append(endDate).append("') or (l.end_date >= '").append(startDate).append("' and l.end_date <= '").append(endDate).append("'))\n");
		}
		
		if(!CommonUtils.isNullString(region)) {
			sql.append("   and l.region = ").append(region).append("\n");
		}
		
		if(!CommonUtils.isNullString(costType)) {
			sql.append("   and l.cost_type = ").append(costType).append("\n");
		} else {
			if(Constant.STATUS_ENABLE.toString().equals(isFleet)) {
				sql.append("   and l.cost_type in (").append(Constant.COST_TYPE_05).append(")\n");
			} else if(Constant.STATUS_DISABLE.toString().equals(isFleet)) {
				sql.append("   and l.cost_type in (").append(Constant.COST_TYPE_01).append(",").append(Constant.COST_TYPE_02).append(",").append(Constant.COST_TYPE_03).append(",").append(Constant.COST_TYPE_04).append(",").append(Constant.COST_TYPE_06).append(",").append(Constant.COST_TYPE_07).append(")\n");
			}
		}
		
		if(!CommonUtils.isNullString(activityType)) {
			sql.append("   and l.activity_type = ").append(activityType).append("\n");
		}
		
		sql.append(" order by l.create_date desc") ;
		
		return super.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
