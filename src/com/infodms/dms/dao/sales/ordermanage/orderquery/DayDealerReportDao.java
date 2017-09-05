package com.infodms.dms.dao.sales.ordermanage.orderquery;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class DayDealerReportDao  extends BaseDao {
	public static Logger logger = Logger.getLogger(DayReportDao.class);
	private static final DayDealerReportDao dao = new DayDealerReportDao ();
	public static final DayDealerReportDao getInstance() {
		return dao;
	}
	/**
	 * 从TM_VHCL_MATERIAL_GROUP取SERIES_NAME
	 * */
	public List<Map<String,Object>> getSeriesList(){
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT G.GROUP_NAME,G.GROUP_ID\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP G\n");  
		sql.append(" WHERE G.GROUP_LEVEL = 2\n");  
		sql.append(" ORDER BY G.GROUP_NAME DESC\n");
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	/**
	 * 点击区域，查看区域详细信息
	 * */
	public List<Map<String,Object>> getRegionDetail(int year,int month,int day,List seriesList,String flag,String dealerId){
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT D.DEALER_NAME,\n");
		if (null != flag && "3".equals(flag)) {
			for (int j = 0; j < seriesList.size(); j++) {
				sql.append("       MAX(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.TODAY_BILL_AMOUNT, 0)) BILL_AMOUNT"+j+",\n");
				if (j != seriesList.size() -1) {
					sql.append("       MAX(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.TODAY_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+",\n");
				}else{
					sql.append("       MAX(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.TODAY_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+"\n");
				}
			}
		}
		if (null != flag && "2".equals(flag)) {
			for (int j = 0; j < seriesList.size(); j++) {
				sql.append("       MAX(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.MONTH_BILL_AMOUNT, 0)) BILL_AMOUNT"+j+",\n");
				if (j != seriesList.size() -1) {
					sql.append("       MAX(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.MONTH_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+",\n");
				}else{
					sql.append("       MAX(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.MONTH_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+"\n");
				}
			}
		}
		if(null != flag && "1".equals(flag)){
			for (int j = 0; j < seriesList.size(); j++) {
				sql.append("       MAX(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.YEAR_BILL_AMOUNT, 0)) BILL_AMOUNT"+j+",\n");
				if (j != seriesList.size() -1) {
					sql.append("       MAX(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.YEAR_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+",\n");
				}else{
					sql.append("       MAX(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.YEAR_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+"\n");
				}
			}
		}
		sql.append(" FROM TT_VS_DASHBOARD D,TM_DEALER TD\n");
		sql.append("WHERE TD.DEALER_ID IN ("+dealerId+") AND TD.DEALER_CODE=D.DEALER_CODE\n");  
		sql.append("GROUP BY D.DEALER_NAME\n");
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	/**
	 * 当日销售看板：主页面展示
	 * */
	public List<Map<String,Object>> getBoardList_All(int year,int month,String dealerId) {
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT D.SERIES_NAME,\n");
		sql.append("       SUM(D.MONTH_BILL_AMOUNT) MONTH_BILL_AMOUNT,\n");  
		sql.append("       SUM(D.YEAR_BILL_AMOUNT) YEAR_BILL_AMOUNT,\n");  
		sql.append("       SUM(D.MONTH_SALES_AMOUNT) MONTH_SALES_AMOUNT,\n");  
		sql.append("       SUM(D.YEAR_SALES_AMOUNT) YEAR_SALES_AMOUNT\n");  
		sql.append("  FROM TT_VS_DASHBOARD D,TM_DEALER TD\n");  
		sql.append(" WHERE D.NYEAR = "+year+"\n");  
		sql.append("   AND D.NMONTH = "+month+"\n");  
		sql.append("   AND TD.DEALER_CODE=D.DEALER_CODE");
		sql.append(" AND TD.DEALER_ID IN ("+dealerId+")");
		sql.append(" GROUP BY D.SERIES_NAME\n");  
		sql.append(" ORDER BY D.SERIES_NAME DESC\n");

		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	public List<Map<String,Object>> getBoardList_Day(int year,int month,int day,String dealerId) {
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT DISTINCT B.SERIES_NAME,\n");
		sql.append("                NVL(A.TODAY_BILL_AMOUNT, 0) TODAY_BILL_AMOUNT,\n");  
		sql.append("                NVL(A.TODAY_SALES_AMOUNT, 0) TODAY_SALES_AMOUNT\n");
		sql.append("  FROM (SELECT D1.SERIES_NAME,\n");  
		sql.append("               SUM(D1.TODAY_BILL_AMOUNT) TODAY_BILL_AMOUNT,\n");  
		sql.append("               SUM(D1.TODAY_SALES_AMOUNT) TODAY_SALES_AMOUNT\n");  
		sql.append("          FROM TT_VS_DASHBOARD D1,TM_DEALER TD\n");  
		sql.append("         WHERE D1.NYEAR = "+year+"\n");  
		sql.append("           AND D1.NMONTH = "+month+"\n");  
		sql.append("           AND D1.NDAY = "+day+"\n");  
		sql.append("  		   AND TD.DEALER_ID IN ("+dealerId+")\n");
		sql.append("   AND TD.DEALER_CODE=D1.DEALER_CODE");
		sql.append("         GROUP BY D1.SERIES_NAME) A,\n");  
		sql.append("       TT_VS_DASHBOARD B\n");  
		sql.append(" WHERE A.SERIES_NAME(+) = B.SERIES_NAME\n");
		sql.append(" ORDER BY B.SERIES_NAME DESC\n");

		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	//查询“月度启票任务”
	public String getMonthBillPlan(long companyId,int year,int month,String dealerId){
		String monthBillPlan = "0";
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT NVL(SUM(D.SALE_AMOUNT),0) MONTH_BILL_SUM\n");
		sql.append("  FROM TT_VS_MONTHLY_PLAN P, TT_VS_MONTHLY_PLAN_DETAIL D\n");  
		sql.append(" WHERE P.PLAN_ID = D.PLAN_ID\n");  
		sql.append("   AND P.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND P.PLAN_YEAR = "+year+"\n");  
		sql.append("   AND P.PLAN_MONTH = "+month+"\n");  
		sql.append("   AND P.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND P.PLAN_TYPE = "+Constant.PLAN_TYPE_BUY+"\n");
		sql.append("   AND P.DEALER_ID IN ("+dealerId+")");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName()) ;
		if (null != list && list.size()>0) {
			monthBillPlan = String.valueOf(list.get(0).get("MONTH_BILL_SUM"));
		}
		return monthBillPlan;
	}
	//查询“年度启票任务”
	public String getYearBillPlan(long companyId,int year,String dealerId){
		String yearBillPlan = "0";
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT NVL(SUM(D.SALE_AMOUNT),0) MONTH_BILL_SUM\n");
		sql.append("  FROM TT_VS_MONTHLY_PLAN P, TT_VS_MONTHLY_PLAN_DETAIL D\n");  
		sql.append(" WHERE P.PLAN_ID = D.PLAN_ID\n");  
		sql.append("   AND P.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND P.PLAN_YEAR = "+year+"\n");  
		sql.append("   AND P.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND P.PLAN_TYPE = "+Constant.PLAN_TYPE_BUY+"\n");
		sql.append("   AND P.DEALER_ID IN ("+dealerId+")");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName()) ;
		if (null != list && list.size()>0) {
			yearBillPlan = String.valueOf(list.get(0).get("MONTH_BILL_SUM"));
		}
		return yearBillPlan;
	}
	//查询“月度零售任务”
	public String getMonthSalesPlan(long companyId,int year,int month,String dealerId){
		String monthSalesPlan = "0";
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT NVL(SUM(D.SALE_AMOUNT),0) MONTH_BILL_SUM\n");
		sql.append("  FROM TT_VS_MONTHLY_PLAN P, TT_VS_MONTHLY_PLAN_DETAIL D\n");  
		sql.append(" WHERE P.PLAN_ID = D.PLAN_ID\n");  
		sql.append("   AND P.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND P.PLAN_YEAR = "+year+"\n");  
		sql.append("   AND P.PLAN_MONTH = "+month+"\n");  
		sql.append("   AND P.DEALER_ID IN ("+dealerId+")");
		sql.append("   AND P.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND P.PLAN_TYPE = "+Constant.PLAN_TYPE_SALE+"\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName()) ;
		if (null != list && list.size()>0) {
			monthSalesPlan = String.valueOf(list.get(0).get("MONTH_BILL_SUM"));
		}
		return monthSalesPlan;
	}
	//查询“年度零售任务”
	public String getYearSalesPlan(long companyId,int year,String dealerId){
		String yearSalesPlan = "0";
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT NVL(SUM(D.SALE_AMOUNT),0) MONTH_BILL_SUM\n");
		sql.append("  FROM TT_VS_MONTHLY_PLAN P, TT_VS_MONTHLY_PLAN_DETAIL D\n");  
		sql.append(" WHERE P.PLAN_ID = D.PLAN_ID\n");  
		sql.append("   AND P.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND P.PLAN_YEAR = "+year+"\n");  
		sql.append("   AND P.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND P.PLAN_TYPE = "+Constant.PLAN_TYPE_SALE+"\n");
		sql.append("   AND P.DEALER_ID IN("+dealerId+")");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName()) ;
		if (null != list && list.size()>0) {
			yearSalesPlan = String.valueOf(list.get(0).get("MONTH_BILL_SUM"));
		}
		return yearSalesPlan;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
