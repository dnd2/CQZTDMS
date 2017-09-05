/**
 * @Title: OrderReport.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-24
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.crm.travel;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class NetSpeciaTravelDao extends BaseDao {
	public static Logger logger = Logger.getLogger(NetSpeciaTravelDao.class);
	private static final NetSpeciaTravelDao dao = new NetSpeciaTravelDao();
	private static POFactory factory = POFactoryBuilder.getInstance();

	public static NetSpeciaTravelDao getInstance() {
		return dao;
	}

	public PageResult<Map<String, Object>> getNetSpeciaList(String userId, String startDate, String endDate, int curPage, int pageSize) {

		StringBuffer sql = new StringBuffer();

		sql.append("select nss.specia_id,\n");
		sql.append("       nss.province_name,\n");
		sql.append("       nss.city_name,\n");
		sql.append("       nss.create_date,\n");
		sql.append("       to_char(nss.business_date,'YYYY-MM-DD') business_date,\n");
		sql.append("       nss.business_traveller, \n");
		sql.append("       nss.submit_status \n");
		sql.append("  from net_specia_stroke nss\n");
		sql.append(" WHERE nss.status='10011001' and nss.create_by = '" + userId + "'\n");
	
		if (startDate != null && !"".equals(startDate)) {
			sql.append("   AND TRUNC(nss.CREATE_DATE, 'DD') >= TO_DATE('" + startDate + "', 'YYYY-MM-DD')\n");
		}

		if (endDate != null && !"".equals(endDate)) {
			sql.append("   AND TRUNC(nss.CREATE_DATE, 'DD') <= TO_DATE('" + endDate + "', 'YYYY-MM-DD')\n");
		}

	

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getGeneralOrderReportList", pageSize, curPage);
		return ps;
	}
	
	//网络日志审核界面列表
	public PageResult<Map<String, Object>> getSubmitNetSpeciaList(String userId, String startDate, String endDate, int curPage, int pageSize) {

		StringBuffer sql = new StringBuffer();

		sql.append("select nss.specia_id,\n");
		sql.append("       nss.province_name,\n");
		sql.append("       nss.city_name,\n");
		sql.append("       nss.create_date,\n");
		sql.append("       to_char(nss.business_date,'YYYY-MM-DD') business_date,\n");
		sql.append("       nss.business_traveller, \n");
		sql.append("       nss.submit_status \n");
		sql.append("  from net_specia_stroke nss\n");
		sql.append(" WHERE nss.status='10011001' and nss.submit_status= '" +10301002+ "'\n");
	
		if (startDate != null && !"".equals(startDate)) {
			sql.append("   AND TRUNC(nss.CREATE_DATE, 'DD') >= TO_DATE('" + startDate + "', 'YYYY-MM-DD')\n");
		}

		if (endDate != null && !"".equals(endDate)) {
			sql.append("   AND TRUNC(nss.CREATE_DATE, 'DD') <= TO_DATE('" + endDate + "', 'YYYY-MM-DD')\n");
		}

	

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getGeneralOrderReportList", pageSize, curPage);
		return ps;
	}
	
	
	public List<Map<String, Object>> getNetSpeciaQueryInfoList(String speciaId) {

		StringBuffer sql = new StringBuffer();
		sql.append("select nss.specia_id,\n");
		sql.append("       nss.province_name,\n");
		sql.append("       nss.city_name,\n");
		sql.append("       nss.create_date,\n");
		sql.append("       to_char(nss.business_date,'YYYY-MM-DD') business_date,\n");
		sql.append("       nss.business_traveller, \n");
		sql.append("  		nss.business_goal,\n");
	    sql.append("    	nss.business_jobam,\n");
		sql.append("    	nss.business_jobpm,\n");
		sql.append("    	nss.operat_item_id,\n");
		sql.append("     	nss.operat_way_id \n");
		
		sql.append("   from net_specia_stroke nss \n");
		sql.append(" WHERE nss.status='10011001' \n");
	
		sql.append("   AND nss.specia_id = '" + speciaId + "'");

		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getGeneralOrderReportList");
		return ps;
	}
	
	
	public List<Map<String, Object>> getNetOperatQueryInfoList(String speciaId) {

		StringBuffer sql = new StringBuffer();
		sql.append("select nsso.operat_id,\n");
		sql.append("        nsso.specia_id,\n");
		sql.append("        nsso.company_name,\n");
		sql.append("        nsso.intent_level,\n");
		sql.append("       nsso.company_nature,\n");
		sql.append("        nsso.location_circle, \n");
		sql.append("  		nsso.car_brand,\n");
	    sql.append("    	nsso.company_address,\n");
		sql.append("    	nsso.dealer_negotiation,\n");
		sql.append("    	nsso.follow_date,\n");
		sql.append("     	nsso.follow_way, \n");
		sql.append("     	nsso.negotiat_content \n");
		
		sql.append(" from  net_specia_stroke_operat nsso \n");
		sql.append(" WHERE 1=1 \n");
	
		sql.append("   AND nsso.specia_id = '" + speciaId + "'");

		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getGeneralOrderReportList");
		return ps;
	}
	
	
	public List<Map<String, Object>> getLinkQueryInfoList(String speciaId) {

		StringBuffer sql = new StringBuffer();
		sql.append("select  nssr.linkman_id,\n");
		sql.append("        nssr.operat_id,\n");
		sql.append("        nssr.linkman_name,\n");
		sql.append("        nssr.linkman_job,\n");
		sql.append("        nssr.linkman_way \n");
		
		sql.append(" from net_specia_stroke_operat_rela nssr \n");
		sql.append(" WHERE 1=1 and nssr.operat_id in \n");
		sql.append("(select nsso.operat_id from net_specia_stroke_operat nsso where 1=1 \n");
		sql.append("   AND nsso.specia_id = '" + speciaId + "' )");
		sql.append(" order by nssr.linkman_id ");
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getGeneralOrderReportList");
		return ps;
	}



	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}



}
