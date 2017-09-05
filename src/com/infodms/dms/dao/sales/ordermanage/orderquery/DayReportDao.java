package com.infodms.dms.dao.sales.ordermanage.orderquery;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

public class DayReportDao extends BaseDao{

	public static Logger logger = Logger.getLogger(DayReportDao.class);
	private static final DayReportDao dao = new DayReportDao ();
	public static final DayReportDao getInstance() {
		return dao;
	}
	/**
	 * 获取重庆基地实销启票数据
	 * @param year
	 * @param month
	 * @param orgId
	 * @return
	 */
	public List<Map<String,Object>> getBoardListCq_All() {
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT A.SERIES_NAME,\n");
		sql.append("A.MONTH_BILL_AMOUNT,\n");
		sql.append("A.YEAR_BILL_AMOUNT,\n");
		sql.append("A.MONTH_SALES_AMOUNT,\n");
		sql.append("A.YEAR_SALES_AMOUNT,\n");
		sql.append("A.TODAY_SALES_AMOUNT,\n");
		sql.append("A.TODAY_BILL_AMOUNT\n");
		sql.append("FROM TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("TM_AREA_GROUP TAG,\n");
		sql.append("TM_BUSINESS_AREA TBA,\n");
		sql.append("(SELECT D.SERIES_NAME,\n");
		sql.append("SUM(D.MONTH_BILL_AMOUNT) MONTH_BILL_AMOUNT,\n");
		sql.append("SUM(D.YEAR_BILL_AMOUNT) YEAR_BILL_AMOUNT,\n");
		sql.append("SUM(D.MONTH_SALES_AMOUNT) MONTH_SALES_AMOUNT,\n");
		sql.append("SUM(D.YEAR_SALES_AMOUNT) YEAR_SALES_AMOUNT,\n");
		sql.append("SUM(D.TODAY_SALES_AMOUNT) TODAY_SALES_AMOUNT,\n");
		sql.append("SUM(D.TODAY_BILL_AMOUNT) TODAY_BILL_AMOUNT\n");
		sql.append("FROM TT_VS_DASHBOARD D\n");
		sql.append("GROUP BY D.SERIES_NAME) A\n");
		sql.append("WHERE TVMG.GROUP_ID = TAG.MATERIAL_GROUP_ID\n");
		sql.append("AND TBA.AREA_ID = TAG.AREA_ID\n");
		sql.append("AND A.SERIES_NAME = TVMG.GROUP_NAME\n");
		sql.append("AND TVMG.GROUP_LEVEL = 2\n");
		sql.append("AND TBA.ERP_CODE IN (82)\n");
		sql.append("GROUP BY A.SERIES_NAME,\n");
		sql.append("A.MONTH_BILL_AMOUNT,\n");
		sql.append("A.YEAR_BILL_AMOUNT,\n");
		sql.append("A.MONTH_SALES_AMOUNT,\n");
		sql.append("A.YEAR_SALES_AMOUNT,\n");
		sql.append("A.TODAY_SALES_AMOUNT,\n");
		sql.append("A.TODAY_BILL_AMOUNT\n");
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	/**
	 * 获取河北基地实销启票数据
	 * @param year
	 * @param month
	 * @param orgId
	 * @return
	 */
	public List<Map<String,Object>> getBoardListHb_All() {
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT A.SERIES_NAME,\n");
		sql.append("A.MONTH_BILL_AMOUNT,\n");
		sql.append("A.YEAR_BILL_AMOUNT,\n");
		sql.append("A.MONTH_SALES_AMOUNT,\n");
		sql.append("A.YEAR_SALES_AMOUNT,\n");
		sql.append("A.TODAY_SALES_AMOUNT,\n");
		sql.append("A.TODAY_BILL_AMOUNT\n");
		sql.append("FROM TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("TM_AREA_GROUP TAG,\n");
		sql.append("TM_BUSINESS_AREA TBA,\n");
		sql.append("(SELECT D.SERIES_NAME,\n");
		sql.append("SUM(D.MONTH_BILL_AMOUNT) MONTH_BILL_AMOUNT,\n");
		sql.append("SUM(D.YEAR_BILL_AMOUNT) YEAR_BILL_AMOUNT,\n");
		sql.append("SUM(D.MONTH_SALES_AMOUNT) MONTH_SALES_AMOUNT,\n");
		sql.append("SUM(D.YEAR_SALES_AMOUNT) YEAR_SALES_AMOUNT,\n");
		sql.append("SUM(D.TODAY_SALES_AMOUNT) TODAY_SALES_AMOUNT,\n");
		sql.append("SUM(D.TODAY_BILL_AMOUNT) TODAY_BILL_AMOUNT\n");
		sql.append("FROM TT_VS_DASHBOARD D\n");
		sql.append("GROUP BY D.SERIES_NAME) A\n");
		sql.append("WHERE TVMG.GROUP_ID = TAG.MATERIAL_GROUP_ID\n");
		sql.append("AND TBA.AREA_ID = TAG.AREA_ID\n");
		sql.append("AND A.SERIES_NAME = TVMG.GROUP_NAME\n");
		sql.append("AND TVMG.GROUP_LEVEL = 2\n");
		sql.append("AND TBA.ERP_CODE IN (142)\n");
		sql.append("GROUP BY A.SERIES_NAME,\n");
		sql.append("A.MONTH_BILL_AMOUNT,\n");
		sql.append("A.YEAR_BILL_AMOUNT,\n");
		sql.append("A.MONTH_SALES_AMOUNT,\n");
		sql.append("A.YEAR_SALES_AMOUNT,\n");
		sql.append("A.TODAY_SALES_AMOUNT,\n");
		sql.append("A.TODAY_BILL_AMOUNT\n");
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	/**
	 * 获取南京基地实销启票数据
	 * @param year
	 * @param month
	 * @param orgId
	 * @return
	 */
	public List<Map<String,Object>> getBoardListNj_All() {
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT A.SERIES_NAME,\n");
		sql.append("A.MONTH_BILL_AMOUNT,\n");
		sql.append("A.YEAR_BILL_AMOUNT,\n");
		sql.append("A.MONTH_SALES_AMOUNT,\n");
		sql.append("A.YEAR_SALES_AMOUNT,\n");
		sql.append("A.TODAY_SALES_AMOUNT,\n");
		sql.append("A.TODAY_BILL_AMOUNT\n");
		sql.append("FROM TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("TM_AREA_GROUP TAG,\n");
		sql.append("TM_BUSINESS_AREA TBA,\n");
		sql.append("(SELECT D.SERIES_NAME,\n");
		sql.append("SUM(D.MONTH_BILL_AMOUNT) MONTH_BILL_AMOUNT,\n");
		sql.append("SUM(D.YEAR_BILL_AMOUNT) YEAR_BILL_AMOUNT,\n");
		sql.append("SUM(D.MONTH_SALES_AMOUNT) MONTH_SALES_AMOUNT,\n");
		sql.append("SUM(D.YEAR_SALES_AMOUNT) YEAR_SALES_AMOUNT,\n");
		sql.append("SUM(D.TODAY_SALES_AMOUNT) TODAY_SALES_AMOUNT,\n");
		sql.append("SUM(D.TODAY_BILL_AMOUNT) TODAY_BILL_AMOUNT\n");
		sql.append("FROM TT_VS_DASHBOARD D\n");
		sql.append("GROUP BY D.SERIES_NAME) A\n");
		sql.append("WHERE TVMG.GROUP_ID = TAG.MATERIAL_GROUP_ID\n");
		sql.append("AND TBA.AREA_ID = TAG.AREA_ID\n");
		sql.append("AND A.SERIES_NAME = TVMG.GROUP_NAME\n");
		sql.append("AND TVMG.GROUP_LEVEL = 2\n");
		sql.append("AND TBA.ERP_CODE IN (197)\n");
		sql.append("GROUP BY A.SERIES_NAME,\n");
		sql.append("A.MONTH_BILL_AMOUNT,\n");
		sql.append("A.YEAR_BILL_AMOUNT,\n");
		sql.append("A.MONTH_SALES_AMOUNT,\n");
		sql.append("A.YEAR_SALES_AMOUNT,\n");
		sql.append("A.TODAY_SALES_AMOUNT,\n");
		sql.append("A.TODAY_BILL_AMOUNT\n");
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	/**
	 * 当日销售看板：主页面展示
	 * */
	public List<Map<String,Object>> getBoardList_All(int year,int month,String orgId) {
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT (SELECT TVMG.GROUP_NAME FROM TM_VHCL_MATERIAL_GROUP TVMG WHERE TVMG.GROUP_ID=D.SERIES_ID) SERIES_NAME,\n");
		sql.append("       SUM(D.MONTH_BILL_AMOUNT) MONTH_BILL_AMOUNT,\n");  
		sql.append("       SUM(D.YEAR_BILL_AMOUNT) YEAR_BILL_AMOUNT,\n");  
		sql.append("       SUM(D.MONTH_SALES_AMOUNT) MONTH_SALES_AMOUNT,\n");  
		sql.append("       SUM(D.YEAR_SALES_AMOUNT) YEAR_SALES_AMOUNT\n");  
		sql.append("  FROM TT_VS_DASHBOARD D\n");  
		sql.append(" WHERE D.NYEAR = "+year+"\n");  
		sql.append("   AND D.NMONTH = "+month+"\n");
		if(orgId!=null&&orgId!=""){
		sql.append(" AND D.ORG_ID='"+orgId+"'");
		}
		sql.append(" GROUP BY D.SERIES_ID\n");  
		sql.append(" ORDER BY D.SERIES_ID DESC\n");

		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	public List<Map<String,Object>> getBoardList_Day(int year,int month,int day,String orgId) {
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT DISTINCT B.SERIES_NAME,\n");
		sql.append("                NVL(A.TODAY_BILL_AMOUNT, 0) TODAY_BILL_AMOUNT,\n");  
		sql.append("                NVL(A.TODAY_SALES_AMOUNT, 0) TODAY_SALES_AMOUNT\n");
		sql.append("  FROM (SELECT D1.SERIES_NAME,\n");  
		sql.append("               SUM(D1.TODAY_BILL_AMOUNT) TODAY_BILL_AMOUNT,\n");  
		sql.append("               SUM(D1.TODAY_SALES_AMOUNT) TODAY_SALES_AMOUNT\n");  
		sql.append("          FROM TT_VS_DASHBOARD D1\n");  
		sql.append("         WHERE D1.NYEAR = "+year+"\n");  
		sql.append("           AND D1.NMONTH = "+month+"\n");  
		sql.append("           AND D1.NDAY = "+day+"\n");  
		if(orgId!=null&&orgId!=""){
		sql.append("  		   AND D1.ORG_ID='"+orgId+"'\n");
		}
		sql.append("         GROUP BY D1.SERIES_NAME) A,\n");  
		sql.append("       TT_VS_DASHBOARD B\n");  
		sql.append(" WHERE A.SERIES_NAME(+) = B.SERIES_NAME\n");
		sql.append(" ORDER BY B.SERIES_NAME DESC\n");

		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	//查询“月度启票任务”
	public String getMonthBillPlan(long companyId,int year,int month,String orgId){
		String monthBillPlan = "0";
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT NVL(SUM(D.SALE_AMOUNT),0) MONTH_BILL_SUM\n");
		sql.append("  FROM TT_VS_MONTHLY_PLAN P, TT_VS_MONTHLY_PLAN_DETAIL D\n");  
		sql.append(" WHERE P.PLAN_ID = D.PLAN_ID\n");  
		sql.append("   AND P.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND P.PLAN_YEAR = "+year+"\n");  
		sql.append("   AND P.PLAN_MONTH = "+month+"\n");  
		sql.append("   AND P.STATUS = "+Constant.PLAN_MANAGE_CONFIRM+"\n");  
		sql.append("   AND P.PLAN_TYPE = "+Constant.PLAN_TYPE_BUY+"\n");
		sql.append("   AND P.plan_desc <> '"+Constant.STATUS_DISABLE+"'\n");
		if(orgId!=null&&orgId!=""){
		sql.append("   AND P.ORG_ID='"+orgId+"'");
		}
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName()) ;
		if (null != list && list.size()>0) {
			monthBillPlan = String.valueOf(list.get(0).get("MONTH_BILL_SUM"));
		}
		return monthBillPlan;
	}
	//查询“年度启票任务”
	public String getYearBillPlan(long companyId,int year,String orgId){
		String yearBillPlan = "0";
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT NVL(SUM(D.SALE_AMOUNT),0) MONTH_BILL_SUM\n");
		sql.append("  FROM TT_VS_MONTHLY_PLAN P, TT_VS_MONTHLY_PLAN_DETAIL D\n");  
		sql.append(" WHERE P.PLAN_ID = D.PLAN_ID\n");  
		sql.append("   AND P.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND P.PLAN_YEAR = "+year+"\n");  
		sql.append("   AND P.STATUS = "+Constant.PLAN_MANAGE_CONFIRM+"\n");  
		sql.append("   AND P.PLAN_TYPE = "+Constant.PLAN_TYPE_BUY+"\n");
		sql.append("   AND P.plan_desc <> '"+Constant.STATUS_DISABLE+"'\n");
		if(orgId!=null&&orgId!=""){
			sql.append("   AND P.ORG_ID='"+orgId+"'");
		}
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName()) ;
		if (null != list && list.size()>0) {
			yearBillPlan = String.valueOf(list.get(0).get("MONTH_BILL_SUM"));
		}
		return yearBillPlan;
	}
	//查询“月度零售任务”
	public String getMonthSalesPlan(long companyId,int year,int month,String orgId){
		String monthSalesPlan = "0";
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT NVL(SUM(D.SALE_AMOUNT),0) MONTH_BILL_SUM\n");
		sql.append("  FROM TT_VS_MONTHLY_PLAN P, TT_VS_MONTHLY_PLAN_DETAIL D\n");  
		sql.append(" WHERE P.PLAN_ID = D.PLAN_ID\n");  
		sql.append("   AND P.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND P.PLAN_YEAR = "+year+"\n");  
		sql.append("   AND P.PLAN_MONTH = "+month+"\n");  
		if(orgId!=null&& orgId!=""){
		sql.append("   AND P.ORG_ID='"+orgId+"'");
		}
		sql.append("   AND P.STATUS = "+Constant.PLAN_MANAGE_CONFIRM+"\n");  
		sql.append("   AND P.PLAN_TYPE = "+Constant.PLAN_TYPE_SALE+"\n");
		sql.append("   AND P.plan_desc <> '"+Constant.STATUS_DISABLE+"'\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName()) ;
		if (null != list && list.size()>0) {
			monthSalesPlan = String.valueOf(list.get(0).get("MONTH_BILL_SUM"));
		}
		return monthSalesPlan;
	}
	//查询“年度零售任务”
	public String getYearSalesPlan(long companyId,int year,String orgId){
		String yearSalesPlan = "0";
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT NVL(SUM(D.SALE_AMOUNT),0) MONTH_BILL_SUM\n");
		sql.append("  FROM TT_VS_MONTHLY_PLAN P, TT_VS_MONTHLY_PLAN_DETAIL D\n");  
		sql.append(" WHERE P.PLAN_ID = D.PLAN_ID\n");  
		sql.append("   AND P.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND P.PLAN_YEAR = "+year+"\n");  
		sql.append("   AND P.STATUS = "+Constant.PLAN_MANAGE_CONFIRM+"\n");  
		sql.append("   AND P.PLAN_TYPE = "+Constant.PLAN_TYPE_SALE+"\n");
		sql.append("   AND P.plan_desc <> '"+Constant.STATUS_DISABLE+"'\n");
		if(orgId!=null&&orgId!=""){
		sql.append("   AND P.ORG_ID='"+orgId+"'");
		}
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName()) ;
		if (null != list && list.size()>0) {
			yearSalesPlan = String.valueOf(list.get(0).get("MONTH_BILL_SUM"));
		}
		return yearSalesPlan;
	}
	/**
	 * 从TM_VHCL_MATERIAL_GROUP取SERIES_NAME
	 * */
	public List<Map<String,Object>> getSeriesList(){
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT G.GROUP_NAME,G.GROUP_ID\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP G\n");  
		sql.append(" WHERE G.GROUP_LEVEL = 2 AND G.STATUS=10011001\n");  
		sql.append(" ORDER BY G.GROUP_NAME DESC\n");
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	/**
	 * 点击看板“合计”按钮查看详细信息
	 * */
	public List<Map<String,Object>> getDetailList(int year,int month,int day,List seriesList,String areaId ) {
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT D.REGION_NAME,\n");
		sql.append("D.ORG_ID,\n");

		if (null != seriesList && seriesList.size()>0) {
			
			if (0 == month && 0 == day) {
				for (int j = 0; j < seriesList.size(); j++) {
					sql.append("       SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.YEAR_BILL_AMOUNT, 0)) BILL_AMOUNT"+j+",\n");
					if (j != seriesList.size() -1) {
						sql.append("       SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.YEAR_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+",\n");
					}else{
						sql.append("       SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.YEAR_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+"\n");
					}
				}
			}
			if(0 != month && 0 == day){
				for (int j = 0; j < seriesList.size(); j++) {
					
					if("2010010100000007".equals(((Map)seriesList.get(j)).get("GROUP_ID").toString())){ //把CX30归到CX30两箱的月启票量中 YH 2011.1.27
						
						sql.append(" SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.MONTH_BILL_AMOUNT, 0))+SUM(DECODE(D.SERIES_NAME, 'CX30', D.MONTH_BILL_AMOUNT, 0)) BILL_AMOUNT"+j+",\n");
						
					}else {
						
					  sql.append(" SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.MONTH_BILL_AMOUNT, 0)) BILL_AMOUNT"+j+",\n");
					}
					if (j != seriesList.size() -1) {
						
						if("2010010100000007".equals(((Map)seriesList.get(j)).get("GROUP_ID").toString())){ //把CX30归到CX30两箱的月销售量中 YH 2011.1.27
							
							sql.append(" SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.MONTH_SALES_AMOUNT, 0))+SUM(DECODE(D.SERIES_NAME, 'CX30', D.MONTH_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+",\n");
							
						}else {
						   sql.append(" SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.MONTH_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+",\n");
						}
					}else{
						sql.append(" SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.MONTH_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+"\n");
					}
				}
			}
			if(0 != month && 0 != day){
				for (int j = 0; j < seriesList.size(); j++) {					
					    sql.append("       SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.TODAY_BILL_AMOUNT, 0)) BILL_AMOUNT"+j+",\n");				
					if (j != seriesList.size() -1) {
						sql.append("       SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.TODAY_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+",\n");
					}else{
						sql.append("       SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.TODAY_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+"\n");
					}
				}
			}
		}
		sql.append("  FROM TT_VS_DASHBOARD D\n");  
		sql.append(" WHERE D.NYEAR = "+year+"\n");  
		if (0 != month) {
			sql.append("   AND D.NMONTH = "+month+"\n"); 
		}
		if (0 != day) {
			sql.append("   AND D.NDAY = "+day+"\n"); 
		} 
		if(areaId!=null&&areaId!=""){
		sql.append("AND D.ORG_ID="+areaId+"\n");
		}
		sql.append(" GROUP BY D.REGION_NAME, D.ORG_ID\n");
		sql.append(" ORDER BY D.REGION_NAME DESC\n");

		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	/**
	 * 点击区域，查看区域详细信息
	 * */
	public List<Map<String,Object>> getRegionDetail(int year,int month,int day,List seriesList,String flag,String orgId){
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT D.DEALER_NAME,\n");
		if (null != flag && "3".equals(flag)) {
			for (int j = 0; j < seriesList.size(); j++) {
				sql.append("       SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.TODAY_BILL_AMOUNT, 0)) BILL_AMOUNT"+j+",\n");
				if (j != seriesList.size() -1) {
					sql.append("       SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.TODAY_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+",\n");
				}else{
					sql.append("       SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.TODAY_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+"\n");
				}
			}
		}
		if (null != flag && "2".equals(flag)) {
			for (int j = 0; j < seriesList.size(); j++) {
				sql.append("       SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.MONTH_BILL_AMOUNT, 0)) BILL_AMOUNT"+j+",\n");
				if (j != seriesList.size() -1) {
					sql.append("       SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.MONTH_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+",\n");
				}else{
					sql.append("       SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.MONTH_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+"\n");
				}
			}
		}
		if(null != flag && "1".equals(flag)){
			for (int j = 0; j < seriesList.size(); j++) {
				sql.append("       SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.YEAR_BILL_AMOUNT, 0)) BILL_AMOUNT"+j+",\n");
				if (j != seriesList.size() -1) {
					sql.append("       SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.YEAR_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+",\n");
				}else{
					sql.append("       SUM(DECODE(D.SERIES_NAME, '"+((Map)seriesList.get(j)).get("GROUP_NAME")+"', D.YEAR_SALES_AMOUNT, 0)) SALES_AMOUNT"+j+"\n");
				}
			}
		}
		sql.append(" FROM TT_VS_DASHBOARD D\n");
		sql.append("WHERE 1=1 AND D.ORG_ID = "+orgId+"\n");  
		sql.append("GROUP BY DEALER_NAME\n");
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	public List<Map<String,Object>> newBoardList(int year,int month,int day,String orgId) { 
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT A.SERIES_NAME,\n");
		sql.append("       A.TODAY_BILL_AMOUNT,\n");  
		sql.append("       B.MONTH_BILL_AMOUNT,\n");  
		sql.append("       B.YEAR_BILL_AMOUNT,\n");  
		sql.append("       A.TODAY_SALES_AMOUNT,\n");  
		sql.append("       B.MONTH_SALES_AMOUNT,\n");  
		sql.append("       B.YEAR_SALES_AMOUNT\n");  
		sql.append("  FROM (SELECT (SELECT TVMG.GROUP_NAME\n");  
		sql.append("                  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");  
		sql.append("                 WHERE TVMG.GROUP_ID = D1.SERIES_ID) SERIES_NAME,\n");  
		sql.append("               SUM(D1.TODAY_BILL_AMOUNT) TODAY_BILL_AMOUNT,\n");  
		sql.append("               SUM(D1.TODAY_SALES_AMOUNT) TODAY_SALES_AMOUNT\n");  
		sql.append("          FROM TT_VS_DASHBOARD D1\n");  
		sql.append("         WHERE D1.NYEAR = ?\n");  
		params.add(year) ;
		
		sql.append("           AND D1.NMONTH = ?\n");  
		params.add(month) ;
		
		sql.append("           AND D1.NDAY = ?\n");  
		params.add(day) ;
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("           AND D1.ORG_ID = ?\n");  
			params.add(orgId) ;
		}
		
		sql.append("         GROUP BY D1.SERIES_ID) A,\n");  
		sql.append("       (SELECT (SELECT TVMG.GROUP_NAME\n");  
		sql.append("                  FROM TM_VHCL_MATERIAL_GROUP TVMG\n");  
		sql.append("                 WHERE TVMG.GROUP_ID = D.SERIES_ID) SERIES_NAME,\n");  
		sql.append("               SUM(D.MONTH_BILL_AMOUNT) MONTH_BILL_AMOUNT,\n");  
		sql.append("               SUM(D.YEAR_BILL_AMOUNT) YEAR_BILL_AMOUNT,\n");  
		sql.append("               SUM(D.MONTH_SALES_AMOUNT) MONTH_SALES_AMOUNT,\n");  
		sql.append("               SUM(D.YEAR_SALES_AMOUNT) YEAR_SALES_AMOUNT\n");  
		sql.append("          FROM TT_VS_DASHBOARD D\n");  
		sql.append("         WHERE D.NYEAR = ?\n");  
		params.add(year) ;
		
		sql.append("           AND D.NMONTH = ?\n");  
		params.add(month) ;
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("           AND D.ORG_ID = ?\n"); 
			params.add(orgId) ;
		}
		
		sql.append("         GROUP BY D.SERIES_ID) B\n");  
		sql.append(" WHERE A.SERIES_NAME = B.SERIES_NAME\n");  
		sql.append(" ORDER BY A.SERIES_NAME\n");
        //System.out.println("jiangluoling:"+sql);
		
		return dao.pageQuery(sql.toString(), params, getFunName()) ;
	}
	
	public List<Map<String,Object>> getBoardList(String[] shortCode) {
		List<Object> params = new ArrayList<Object>() ;
		
		int len = shortCode.length ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TVD.SERIES_NAME,\n");
		sql.append("       SUM(TVD.MONTH_BILL_AMOUNT) MONTH_BILL_AMOUNT,\n");  
		sql.append("       SUM(TVD.YEAR_BILL_AMOUNT) YEAR_BILL_AMOUNT,\n");  
		sql.append("       SUM(TVD.MONTH_SALES_AMOUNT) MONTH_SALES_AMOUNT,\n");  
		sql.append("       SUM(TVD.YEAR_SALES_AMOUNT) YEAR_SALES_AMOUNT,\n");  
		sql.append("       SUM(TVD.TODAY_SALES_AMOUNT) TODAY_SALES_AMOUNT,\n");  
		sql.append("       SUM(TVD.TODAY_BILL_AMOUNT) TODAY_BILL_AMOUNT\n");  
		sql.append("  from tt_vs_dashboard tvd\n");  
		sql.append(" where (tvd.dealer_code like '").append(shortCode[0]).append("%'\n"); 
		
		for(int i=1; i<len; i++) {
			sql.append("  or tvd.dealer_code like '").append(shortCode[i]).append("%'\n") ;
		}
		
		sql.append(" )") ;
		
		sql.append(" GROUP BY TVD.SERIES_NAME\n");

		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	
	/**
	 * 获取销售日报表
	 * @param 
	 * */
	public List<Map<String,Object>> getSaleOfDailyReportList(List<Map<String, Object>> areaList) {
		
		StringBuffer sql = new StringBuffer() ;
		StringBuffer areaId = new StringBuffer();
		sql.append("select /*+ all_rows*/ dabi.org_name,\n");
		sql.append("       dabi.org_id,\n");  
		sql.append("       dabi.region_name,\n");  
		sql.append("       dabi.dealer_shortname,\n");
		sql.append("	  sum(nvl(dabi.act_month_f,0)) act_month_f,\n");
		sql.append("      sum(nvl(dabi.act_year_f,0)) act_year_f,\n");  
		sql.append("      sum(nvl(dabi.bill_month_f,0)) bill_month_f,\n");  
		sql.append("      sum(nvl(dabi.bill_year_f, 0)) bill_year_f,\n");
		sql.append("      sum(nvl(dabi.act_day,0)) act_day,\n");  
		sql.append("      sum(nvl(dabi.act_plan,0)) act_plan,\n");  
		sql.append("      sum(nvl(dabi.act_month,0)) act_month,\n");  
		sql.append("      decode(sum(nvl(dabi.act_plan,0)),0,0,sum(nvl(dabi.act_month,0)) / decode(sum(nvl(dabi.act_plan,0)),0,1, sum(nvl(dabi.act_plan,0)))) ACT_MONTH_COMPLETION_RATE,\n");  
		sql.append("      decode(sum(nvl(dabi.act_month_f,0)), 0, 0, (sum(nvl(dabi.act_month,0))/ decode(sum(nvl(dabi.act_month_f,0)),0,1, sum(dabi.act_month_f))))  ACT_MONTH_YEAR_ON_YEAR,\n");  
		sql.append("      sum(nvl(dabi.ACT_YEAR,0)) ACT_YEAR,\n");  
		sql.append("      decode(sum(nvl(dabi.ACT_YEAR_f,0)),0,0,(sum(nvl(dabi.ACT_YEAR,0))/ decode(sum(nvl(dabi.ACT_YEAR_f,0)),0,1, sum(dabi.act_year_f)))) AATU,\n");  
		sql.append("      sum(nvl(dabi.DLVRY_AMOUT,0)) DLVRY_AMOUT,\n");  
		sql.append("	  sum(nvl(dabi.EXISTS_AMOUNT,0)) EXISTS_AMOUNT,\n");  
		sql.append("	  sum(nvl(dabi.RESERVER_AMOUNT,0)) RESERVER_AMOUNT,\n");  
		sql.append("	  sum(nvl(dabi.DLVRY_AMOUT,0)) + sum(nvl(dabi.EXISTS_AMOUNT,0)) + sum(nvl(dabi.RESERVER_AMOUNT,0))  AVAILABLE_AMOUNT,\n");  
		sql.append("      sum(nvl(dabi.BILL_DAY,0)) BILL_DAY,\n");  
		sql.append("      sum(nvl(dabi.BILL_PLAN,0)) BILL_PLAN,\n");  
		sql.append("      sum(nvl(dabi.BILL_MONTH,0)) BILL_MONTH,\n");  
		sql.append("      decode(sum(nvl(dabi.bill_plan,0)), 0, 0,sum(nvl(dabi.bill_month,0)) / decode(sum(nvl(dabi.bill_plan,0)),0,1,sum(dabi.bill_plan))) BILL_MONTH_COMPLETION_RATE,\n");  
		sql.append("      decode(sum(nvl(dabi.bill_month_f,0)),0,0,sum(nvl(dabi.bill_month,0)) / decode(sum(nvl(dabi.bill_month_f,0)),0,1,sum(dabi.bill_month_f))) BILL_MONTH_YEAR_ON_YEAR,\n");  
		sql.append("      sum(nvl(dabi.bill_year,0)) bill_year,\n");  
		sql.append("      decode(sum(nvl(dabi.bill_year_f,0)),0,0,sum(nvl(dabi.bill_year,0)) / decode(sum(nvl(dabi.bill_year_f,0)),0,1, sum(dabi.bill_year_f))) BATU\n"); 
		sql.append("      from  day_act_bill_info dabi\n");  
		sql.append("	where 1=1\n");
		sql.append("	and dabi.series_id is not null\n");
		for(int i=0; i<areaList.size(); i++) {
			areaId.append(areaList.get(i).get("AREA_ID"));
			if(i != areaList.size() - 1) {
				areaId.append(",");
			}
		}
		sql.append("	and dabi.area_id in( ");
		sql.append(areaId.toString());
		sql.append(")\n");
		sql.append("      group by dabi.org_name, dabi.region_name, dabi.dealer_shortname, dabi.org_id\n");  
		sql.append("      order by case when dabi.org_name = '专用车' then 1 when dabi.org_name = '国际公司' then 1 else 0 end asc, dabi.org_id, dabi.region_name\n");
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	/**
	 * 获取销售日报表
	 * @param orgId 大区id
	 * @param regionId 省份id
	 * @param groupId 物料组id
	 * @param areaId 业务范围id
	 * */
	public List<Map<String,Object>> getSaleOfDailyReportList(String orgId, String regionId, String groupId, String areaId, List<Map<String, Object>> areaList) {
		
		StringBuffer sql = new StringBuffer() ;
		
		sql.append("select /*+ all_rows*/ dabi.org_name,\n");
		sql.append("       dabi.org_id,\n");  
		sql.append("       dabi.region_name,\n");  
		sql.append("       dabi.dealer_shortname,\n");
		sql.append("	  sum(nvl(dabi.act_month_f,0)) act_month_f,\n");
		sql.append("      sum(nvl(dabi.act_year_f,0)) act_year_f,\n");  
		sql.append("      sum(nvl(dabi.bill_month_f,0)) bill_month_f,\n");  
		sql.append("      sum(nvl(dabi.bill_year_f, 0)) bill_year_f,\n");
		sql.append("      sum(nvl(dabi.act_day,0)) act_day,\n");  
		sql.append("      sum(nvl(dabi.act_plan,0)) act_plan,\n");  
		sql.append("      sum(nvl(dabi.act_month,0)) act_month,\n");  
		sql.append("      decode(sum(nvl(dabi.act_plan,0)),0,0,sum(nvl(dabi.act_month,0)) / decode(sum(nvl(dabi.act_plan,0)),0,1, sum(nvl(dabi.act_plan,0)))) ACT_MONTH_COMPLETION_RATE,\n");  
		sql.append("      decode(sum(nvl(dabi.act_month_f,0)), 0, 0, (sum(nvl(dabi.act_month,0))/ decode(sum(nvl(dabi.act_month_f,0)),0,1, sum(dabi.act_month_f))))  ACT_MONTH_YEAR_ON_YEAR,\n");  
		sql.append("      sum(nvl(dabi.ACT_YEAR,0)) ACT_YEAR,\n");  
		sql.append("      decode(sum(nvl(dabi.ACT_YEAR_f,0)),0,0,(sum(nvl(dabi.ACT_YEAR,0))/ decode(sum(nvl(dabi.ACT_YEAR_f,0)),0,1, sum(dabi.act_year_f)))) AATU,\n");  
		sql.append("      sum(nvl(dabi.DLVRY_AMOUT,0)) DLVRY_AMOUT,\n");  
		sql.append("	  sum(nvl(dabi.EXISTS_AMOUNT,0)) EXISTS_AMOUNT,\n");  
		sql.append("	  sum(nvl(dabi.RESERVER_AMOUNT,0)) RESERVER_AMOUNT,\n");  
		sql.append("	  sum(nvl(dabi.DLVRY_AMOUT,0)) + sum(nvl(dabi.EXISTS_AMOUNT,0)) + sum(nvl(dabi.RESERVER_AMOUNT,0))  AVAILABLE_AMOUNT,\n");  
		sql.append("      sum(nvl(dabi.BILL_DAY,0)) BILL_DAY,\n");  
		sql.append("      sum(nvl(dabi.BILL_PLAN,0)) BILL_PLAN,\n");  
		sql.append("      sum(nvl(dabi.BILL_MONTH,0)) BILL_MONTH,\n");  
		sql.append("      decode(sum(nvl(dabi.bill_plan,0)), 0, 0,sum(nvl(dabi.bill_month,0)) / decode(sum(nvl(dabi.bill_plan,0)),0,1,sum(dabi.bill_plan))) BILL_MONTH_COMPLETION_RATE,\n");  
		sql.append("      decode(sum(nvl(dabi.bill_month_f,0)),0,0,sum(nvl(dabi.bill_month,0)) / decode(sum(nvl(dabi.bill_month_f,0)),0,1,sum(dabi.bill_month_f))) BILL_MONTH_YEAR_ON_YEAR,\n");  
		sql.append("      sum(nvl(dabi.bill_year,0)) bill_year,\n");  
		sql.append("      decode(sum(nvl(dabi.bill_year_f,0)),0,0,sum(nvl(dabi.bill_year,0)) / decode(sum(nvl(dabi.bill_year_f,0)),0,1, sum(dabi.bill_year_f))) BATU\n");  
		sql.append("      from  day_act_bill_info dabi\n");  
		sql.append("	where 1=1\n");
		sql.append("	and dabi.series_id is not null\n");

		if(orgId != null && !"".equals(orgId)) {
			sql.append("	and dabi.org_id in (");
			sql.append(orgId);
			sql.append(")\n");
		}
	
		if(regionId != null && !"".equals(regionId)) {
			sql.append("	and dabi.region_code in (");
			sql.append(regionId);
			sql.append(")\n");
		}
		
		if(groupId != null && !"".equals(groupId)) {
			sql.append("	and dabi.series_id in (");
			sql.append(groupId);
			sql.append(")\n");
		}
		
		if("-1".equals(areaId)) {
			for(int i=0; i<areaList.size(); i++) {
				if(i == 0) {
					areaId = ((BigDecimal)areaList.get(i).get("AREA_ID")).toString();
				} else {
					areaId += areaList.get(i).get("AREA_ID");
				}
				if(i != areaList.size() - 1) {
					areaId += ",";
				}
			}
		} 
		sql.append("	and dabi.area_id in( ");
		sql.append(areaId);
		sql.append(")\n");
		
		
		sql.append("      group by dabi.org_name, dabi.region_name, dabi.dealer_shortname, dabi.org_id\n");  
		sql.append("      order by case when dabi.org_name = '专用车' then 1 when dabi.org_name = '国际公司' then 1 else 0 end asc, dabi.org_id, dabi.region_name\n");
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
