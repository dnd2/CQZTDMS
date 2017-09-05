package com.infodms.dms.dao.sales.ordermanage.orderquery;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("rawtypes")
public class OrderReportDao extends BaseDao {
	
	public static Logger logger = Logger.getLogger(OrderReportDao.class);
	private static final OrderReportDao dao = new OrderReportDao ();
	public static final OrderReportDao getInstance() {
		return dao;
	}
	
	
	/**
	 * 订单监控看板：主页面展示
	 * @param year	年份
	 * @param month	月份
	 * @param orgId	组织ID
	 * @param dutyType 组织层次(判断是公司还是大区,公司查询所有记录,大区仅查询所在大区记录)
	 * @return 		查询列表
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getOrderBoard(int year,int month, long orgId, int dutyType, String areaIds) {
		StringBuffer sql = new StringBuffer();
		sql.append("select tba.area_name,\n");
		sql.append("       o.org_name,\n");  
		sql.append("       tvmg.group_name,\n");  
		sql.append("       tpt.sale_amount,\n");  
		sql.append("       tpt.dlr_forecast,\n");  
		sql.append("       tpt.oem_forecast,\n");  
		sql.append("       tpt.oem_quata,\n");  
		sql.append("       tpt.dlr_quata,\n");  
		sql.append("	nvl(totalOrder, 0) totalOrder,\n");
		sql.append("    nvl(totalReq, 0) totalReq,\n");  
		sql.append("    nvl(totalReserve, 0) totalReserve,\n");  
		sql.append("    nvl(totalDelivery, 0) totalDelivery,\n");  
		sql.append("    nvl(totalMatch, 0) totalMatch,\n");
		sql.append("	nvl(totalDeliveryB, 0) totalDeliveryB,\n");
		sql.append("    nvl(totalDeliveryD, 0) totalDeliveryD,\n");
		sql.append("	ROUND(totalOrder / decode(tpt.dlr_quata, 0 ,1, tpt.dlr_quata), 2) orderPercent,\n");
		sql.append("    ROUND(totalDelivery / decode(totalOrder, 0 ,1, totalOrder), 2) runPercent,\n");
		
		sql.append("    ROUND(totalDelivery /\n");
		sql.append("                  decode(totalDelivery + totalDeliveryB + totalDeliveryD,\n");  
		sql.append("                         0,\n");  
		sql.append("                         1,\n");  
		sql.append("                         totalDelivery + totalDeliveryB + totalDeliveryD),\n");  
		sql.append("                  2) normalPercent\n");

		sql.append("  from tm_business_area tba,\n");  
		sql.append("       tm_org o,\n");  
		sql.append("       tm_vhcl_material_group tvmg,\n");  
		sql.append("       tt_plan_temp tpt,\n");  
		sql.append("       (select tvopi.area_id || tvopi.org_id || tvopi.series_id ||\n");  
		sql.append("               tvopi.order_year || tvopi.order_month flag_id,\n");  
		sql.append("               sum(tvopi.total_order) totalOrder,\n");  
		sql.append("               sum(tvopi.total_req) totalReq,\n");  
		sql.append("               sum(tvopi.total_reserve) totalReserve,\n");  
		sql.append("               sum(tvopi.total_delivery) totalDelivery,\n");  
		sql.append("               sum(tvopi.total_match) totalMatch,\n");  
		
		sql.append("               sum(tvopi.total_delivery_b) totalDeliveryB,\n");
		sql.append("               sum(tvopi.total_delivery_d) totalDeliveryD\n");

		
		sql.append("          from tt_vs_order_pause_info tvopi\n"); 
		sql.append("         group by tvopi.area_id,\n");  
		sql.append("                  tvopi.org_id,\n");  
		sql.append("                  tvopi.series_id,\n");  
		sql.append("                  tvopi.order_year,\n");  
		sql.append("                  tvopi.order_month) t\n");  
		sql.append(" where tpt.area_id = tba.area_id\n");  
		sql.append("   and tpt.series_id = tvmg.group_id\n");  
		sql.append("   and tpt.org_id = o.org_id\n");  
		sql.append("   and tpt.flag_id = t.flag_id(+)\n"); 
		sql.append("   and tpt.area_id in (").append(areaIds).append(")\n"); 
		sql.append("	and tpt.year = ");
		sql.append(year);
		sql.append("	and tpt.month = ");  
		sql.append(month);
		if(Constant.DUTY_TYPE_COMPANY != dutyType) {
			sql.append("	and tpt.org_id = ");
			sql.append(orgId);
		}
		sql.append("	and (tpt.sale_amount + tpt.dlr_forecast + tpt.oem_forecast +\n");
		sql.append("    tpt.oem_quata + tpt.dlr_quata) != 0\n");
		sql.append(" order by tba.area_name, o.org_name, tvmg.group_name\n");
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	/**
	 * 取得区域(A区和B区)的个数,方便页面rowspan计算
	 * @param year	年份
	 * @param month	月份
	 * @param orgId	组织ID
	 * @param dutyType 组织层次(判断是公司还是大区,公司查询所有记录,大区仅查询所在大区记录)
	 * @return 		查询列表
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getAreaCount(int year,int month, long orgId, int dutyType, String areaIds) {
		StringBuffer sql = new StringBuffer();
		sql.append("select tba.area_name,\n");
		sql.append("       count(o.org_name) areaCount\n");  
		sql.append("  from tm_business_area tba,\n");  
		sql.append("       tm_org o,\n");  
		sql.append("       tm_vhcl_material_group tvmg,\n");  
		sql.append("       tt_plan_temp tpt,\n");  
		sql.append("       (select tvopi.area_id || tvopi.org_id || tvopi.series_id ||\n");  
		sql.append("               tvopi.order_year || tvopi.order_month flag_id,\n");  
		sql.append("               sum(tvopi.total_order) totalOrder,\n");  
		sql.append("               sum(tvopi.total_req) totalReq,\n");  
		sql.append("               sum(tvopi.total_reserve) totalReserve,\n");  
		sql.append("               sum(tvopi.total_delivery) totalDelivery,\n");  
		sql.append("               sum(tvopi.total_match) totalMatch\n");  
		sql.append("          from tt_vs_order_pause_info tvopi\n");  
		sql.append("         group by tvopi.area_id,\n");  
		sql.append("                  tvopi.org_id,\n");  
		sql.append("                  tvopi.series_id,\n");  
		sql.append("                  tvopi.order_year,\n");  
		sql.append("                  tvopi.order_month) t\n");  
		sql.append(" where tpt.area_id = tba.area_id\n");  
		sql.append("   and tpt.series_id = tvmg.group_id\n");  
		sql.append("   and tpt.org_id = o.org_id\n");  
		sql.append("   and tpt.flag_id = t.flag_id(+)\n");  
		sql.append("   and tpt.area_id in (").append(areaIds).append(")\n"); 
		sql.append("	and tpt.year = ");
		sql.append(year);
		sql.append("	and tpt.month = ");  
		sql.append(month);
		if(Constant.DUTY_TYPE_COMPANY != dutyType) {
			sql.append("	and tpt.org_id = ");
			sql.append(orgId);
		}
		sql.append("	and (tpt.sale_amount + tpt.dlr_forecast + tpt.oem_forecast +\n");
		sql.append("    tpt.oem_quata + tpt.dlr_quata) != 0\n");
		sql.append("	group by tba.area_name\n");
		sql.append(" 	order by tba.area_name\n");
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
