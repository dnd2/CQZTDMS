package com.infodms.dms.dao.sales.planmanage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class RequireForecastDifferenceReportDao extends BaseDao<PO> {

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Logger logger = Logger.getLogger(RequireForecastDifferenceReportDao.class);

	private static RequireForecastDifferenceReportDao dao;

	public static RequireForecastDifferenceReportDao getInstance() {
		if (dao == null) {
			dao = new RequireForecastDifferenceReportDao();
		}
		return dao;
	}

	/**
	 * 需求差异预测汇总查询
	 * @param map	参数列表
	 * @param pageSize	每页记录数
	 * @param curPage	当前页数
	 * @return 当前页结果集
	 * */
	public PageResult<Map<String, Object>> selectDealerMonthForecastTotal(
			Map<String, Object> map, int pageSize, int curPage) {

		String forecast_year = (String) map.get("forecast_year");

		String forecast_month = (String) map.get("forecast_month");

		String groupCode = (String) map.get("groupCode");
		
		String areaId = (String) map.get("areaId");
		
		String orgCode = (String) map.get("orgCode");

		StringBuffer sql = new StringBuffer();

		sql.append("select t1.org_name,\n");
		sql.append("       t1.area_name,\n");  
		sql.append("       t1.forecast_year,\n");  
		sql.append("       t1.forecast_month,\n");  
		sql.append("       t1.group_code,\n");  
		sql.append("       t2.tt2,\n");  
		sql.append("       t1.tt1,\n"); 
		sql.append("       t2.tt2-t1.tt1 compare,\n");  
		sql.append("       tcu.name,\n");  
		sql.append("       t1.create_date\n");  
		sql.append("  from (select tmo.org_name,\n");  
		sql.append("               tba.area_name,\n");  
		sql.append("               sum(tvmfd.forecast_amount) tt1,\n");  
		sql.append("               tvmf.forecast_year,\n");  
		sql.append("               tvmf.forecast_month,\n");  
		sql.append("               tvmg.group_code,\n");  
		sql.append("               tvmf.create_by,\n");  
		sql.append("               tvmf.create_date\n");  
		sql.append("          from tt_vs_monthly_forecast        tvmf,\n");  
		sql.append("               tt_vs_monthly_forecast_detail tvmfd,\n");  
		sql.append("               tm_org                        tmo,\n");  
		sql.append("               tm_business_area              tba,\n");  
		sql.append("               tm_vhcl_material_group        tvmg\n");  
		sql.append("         where tvmf.forecast_id = tvmfd.forecast_id\n");  
		sql.append("           and tvmf.org_id = tmo.org_id\n");  
		sql.append("           and tvmf.area_id = tba.area_id\n");
		sql.append("		   and tvmf.org_type = 10191001\n");
		sql.append("		   and tvmg.status = 10011001\n");
		if(forecast_year != null && !"".equals(forecast_year)) {
			sql.append("        and tvmf.forecast_year = ");
			sql.append(forecast_year);
			sql.append("\n");
		} 
		if(forecast_month != null && !"".equals(forecast_month)) {
			sql.append("		and tvmf.forecast_month =");
			sql.append(forecast_month);
			sql.append("\n");
		}
		if(groupCode != null && !"".equals(groupCode)) {
			sql.append("       and tvmg.group_code in (");
			sql.append(PlanUtil.createSqlStr(groupCode));
			sql.append(")\n");
		}
		if(areaId != null && !"".equals(areaId)) {
			sql.append("       and tvmf.area_id in (");
			sql.append(PlanUtil.createSqlStr(areaId));
			sql.append(")\n");
		}
		if(orgCode != null && !"".equals(orgCode)) {
			sql.append("       and tmo.org_code in (");
			sql.append(PlanUtil.createSqlStr(orgCode));
			sql.append(")\n");
		}
		sql.append("           and tvmf.status = 10301002\n");  
		sql.append("           and tvmg.group_level = 4\n");  
		sql.append("           and tvmg.group_id = tvmfd.group_id\n");  
		sql.append("         group by tmo.org_name,\n");  
		sql.append("                  tba.area_name,\n");  
		sql.append("                  tvmf.create_by,\n");  
		sql.append("                  tvmf.create_date,\n");  
		sql.append("                  tvmf.forecast_year,\n");  
		sql.append("                  tvmf.forecast_month,\n");  
		sql.append("                  tvmg.group_code) t1,\n");  
		sql.append("\n");  
		sql.append("       (select vod.root_org_name,\n");  
		sql.append("               tba.area_name,\n");  
		sql.append("               sum(tvmfd.forecast_amount) tt2,\n");  
		sql.append("               tvmf.forecast_year,\n");  
		sql.append("               tvmf.forecast_month,\n");  
		sql.append("               tvmg.group_code\n");  
		sql.append("          from tt_vs_monthly_forecast        tvmf,\n");  
		sql.append("               tt_vs_monthly_forecast_detail tvmfd,\n");  
		sql.append("               vw_org_dealer                 vod,\n");  
		sql.append("               tm_business_area              tba,\n");  
		sql.append("               tm_vhcl_material_group        tvmg\n");  
		sql.append("         where tvmf.forecast_id = tvmfd.forecast_id\n");  
		sql.append("           and tvmf.dealer_id = vod.dealer_id\n");  
		sql.append("           and tvmf.area_id = tba.area_id\n");  
		sql.append("		   and tvmf.org_type = 10191002\n");
		sql.append("		   and tvmg.status = 10011001\n");
		if(forecast_year != null && !"".equals(forecast_year)) {
			sql.append("        and tvmf.forecast_year = ");
			sql.append(forecast_year);
			sql.append("\n");
		} 
		if(forecast_month != null && !"".equals(forecast_month)) {
			sql.append("		and tvmf.forecast_month =");
			sql.append(forecast_month);
			sql.append("\n");
		}
		if(groupCode != null && !"".equals(groupCode)) {
			sql.append("       and tvmg.group_code in (");
			sql.append(PlanUtil.createSqlStr(groupCode));
			sql.append(")\n");
		}
		if(areaId != null && !"".equals(areaId)) {
			sql.append("       and tvmf.area_id in (");
			sql.append(PlanUtil.createSqlStr(areaId));
			sql.append(")\n");
		}
		if(orgCode != null && !"".equals(orgCode)) {
			sql.append("       and vod.root_org_code in (");
			sql.append(PlanUtil.createSqlStr(orgCode));
			sql.append(")\n");
		}
		sql.append("           and tvmf.status = 10301002\n");  
		sql.append("           and tvmg.group_level = 4\n");  
		sql.append("           and tvmg.group_id = tvmfd.group_id\n");  
		sql.append("         group by vod.root_org_name,\n");  
		sql.append("                  tba.area_name,\n");  
		sql.append("                  tvmf.forecast_year,\n");  
		sql.append("                  tvmf.forecast_month,\n");  
		sql.append("                  tvmg.group_code) t2,\n");  
		sql.append("       tc_user tcu\n");  
		sql.append(" where t1.org_name = t2.root_org_name\n");  
		sql.append("   and t1.area_name = t2.area_name\n");  
		sql.append("   and t1.create_by = tcu.user_id\n");  
		sql.append("   and t1.group_code = t2.group_code\n");
		

		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}


	/**
	 * 下载时提供的需求预测差异结果集
	 * @param map 参数集合
	 * */
	public List<Map<String, Object>> getDifferenceTotal(
			Map<String, Object> map) {
		String forecast_year = (String) map.get("forecast_year");
		String forecast_month = (String) map.get("forecast_month");
		String groupCode = (String) map.get("groupCode");
		String areaId = map.get("areaId").toString();
		String orgCode = (String) map.get("orgCode");

		StringBuffer sql = new StringBuffer();
		sql.append("select t1.org_name,\n");
		sql.append("       t1.area_name,\n");  
		sql.append("       t1.forecast_year,\n");  
		sql.append("       t1.forecast_month,\n");  
		sql.append("       t1.group_code,\n");  
		sql.append("       t2.tt2,\n");  
		sql.append("       t1.tt1,\n");
		sql.append("       t2.tt2 - t1.tt1 compare,\n"); 
		sql.append("       tcu.name,\n");  
		sql.append("       TO_CHAR(t1.create_date,'YYYY-MM-DD hh:mm:ss') create_date\n");  
		sql.append("  from (select tmo.org_name,\n");  
		sql.append("               tba.area_name,\n");  
		sql.append("               sum(tvmfd.forecast_amount) tt1,\n");  
		sql.append("               tvmf.forecast_year,\n");  
		sql.append("               tvmf.forecast_month,\n");  
		sql.append("               tvmg.group_code,\n");  
		sql.append("               tvmf.create_by,\n");  
		sql.append("               tvmf.create_date\n");  
		sql.append("          from tt_vs_monthly_forecast        tvmf,\n");  
		sql.append("               tt_vs_monthly_forecast_detail tvmfd,\n");  
		sql.append("               tm_org                        tmo,\n");  
		sql.append("               tm_business_area              tba,\n");  
		sql.append("               tm_vhcl_material_group        tvmg\n");  
		sql.append("         where tvmf.forecast_id = tvmfd.forecast_id\n");  
		sql.append("           and tvmf.org_id = tmo.org_id\n");  
		sql.append("           and tvmf.area_id = tba.area_id\n");
		sql.append("		   and tvmf.org_type = 10191001\n");
		sql.append("		   and tvmg.status = 10011001\n");
		if(forecast_year != null && !"".equals(forecast_year)) {
			sql.append("        and tvmf.forecast_year = ");
			sql.append(forecast_year);
			sql.append("\n");
		} 
		if(forecast_month != null && !"".equals(forecast_month)) {
			sql.append("		and tvmf.forecast_month =");
			sql.append(forecast_month);
			sql.append("\n");
		}
		if(groupCode != null && !"".equals(groupCode)) {
			sql.append("       and tvmg.group_code in (");
			sql.append(PlanUtil.createSqlStr(groupCode));
			sql.append(")\n");
		}
		if(areaId != null && !"".equals(areaId)) {
			sql.append("       and tvmf.area_id in (");
			sql.append(PlanUtil.createSqlStr(areaId));
			sql.append(")\n");
		}
		if(orgCode != null && !"".equals(orgCode)) {
			sql.append("       and tmo.org_code in (");
			sql.append(PlanUtil.createSqlStr(orgCode));
			sql.append(")\n");
		}
		sql.append("           and tvmf.status = 10301002\n");  
		sql.append("           and tvmg.group_level = 4\n");  
		sql.append("           and tvmg.group_id = tvmfd.group_id\n");  
		sql.append("         group by tmo.org_name,\n");  
		sql.append("                  tba.area_name,\n");  
		sql.append("                  tvmf.create_by,\n");  
		sql.append("                  tvmf.create_date,\n");  
		sql.append("                  tvmf.forecast_year,\n");  
		sql.append("                  tvmf.forecast_month,\n");  
		sql.append("                  tvmg.group_code) t1,\n");  
		sql.append("\n");  
		sql.append("       (select vod.root_org_name,\n");  
		sql.append("               tba.area_name,\n");  
		sql.append("               sum(tvmfd.forecast_amount) tt2,\n");  
		sql.append("               tvmf.forecast_year,\n");  
		sql.append("               tvmf.forecast_month,\n");  
		sql.append("               tvmg.group_code\n");  
		sql.append("          from tt_vs_monthly_forecast        tvmf,\n");  
		sql.append("               tt_vs_monthly_forecast_detail tvmfd,\n");  
		sql.append("               vw_org_dealer                 vod,\n");  
		sql.append("               tm_business_area              tba,\n");  
		sql.append("               tm_vhcl_material_group        tvmg\n");  
		sql.append("         where tvmf.forecast_id = tvmfd.forecast_id\n");  
		sql.append("           and tvmf.dealer_id = vod.dealer_id\n");  
		sql.append("           and tvmf.area_id = tba.area_id\n");  
		sql.append("		   and tvmf.org_type = 10191002\n");
		sql.append("		   and tvmg.status = 10011001\n");
		if(forecast_year != null && !"".equals(forecast_year)) {
			sql.append("        and tvmf.forecast_year = ");
			sql.append(forecast_year);
			sql.append("\n");
		} 
		if(forecast_month != null && !"".equals(forecast_month)) {
			sql.append("		and tvmf.forecast_month =");
			sql.append(forecast_month);
			sql.append("\n");
		}
		if(groupCode != null && !"".equals(groupCode)) {
			sql.append("       and tvmg.group_code in (");
			sql.append(PlanUtil.createSqlStr(groupCode));
			sql.append(")\n");
		}
		if(areaId != null && !"".equals(areaId)) {
			sql.append("       and tvmf.area_id in (");
			sql.append(PlanUtil.createSqlStr(areaId));
			sql.append(")\n");
		}
		if(orgCode != null && !"".equals(orgCode)) {
			sql.append("       and vod.root_org_code in (");
			sql.append(PlanUtil.createSqlStr(orgCode));
			sql.append(")\n");
		}
		sql.append("           and tvmf.status = 10301002\n");  
		sql.append("           and tvmg.group_level = 4\n");  
		sql.append("           and tvmg.group_id = tvmfd.group_id\n");  
		sql.append("         group by vod.root_org_name,\n");  
		sql.append("                  tba.area_name,\n");  
		sql.append("                  tvmf.forecast_year,\n");  
		sql.append("                  tvmf.forecast_month,\n");  
		sql.append("                  tvmg.group_code) t2,\n");  
		sql.append("       tc_user tcu\n");  
		sql.append(" where t1.org_name = t2.root_org_name\n");  
		sql.append("   and t1.area_name = t2.area_name\n");  
		sql.append("   and t1.create_by = tcu.user_id\n");  
		sql.append("   and t1.group_code = t2.group_code\n");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}

}
