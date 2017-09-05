package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.STRING;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class orgSalesCheckReportQueryDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(BillDetailTicketDao.class);
	public static orgSalesCheckReportQueryDao dao = new orgSalesCheckReportQueryDao();
	public static orgSalesCheckReportQueryDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Map<String, Object>> getDealerCheckInfo(Map<String, Object> map){
		StringBuffer sbSql  =  new StringBuffer();
		List<Map<String, Object>> seriesList = getSeriesList();
		String startDate = (String)map.get("startDate");
		String endDate = (String)map.get("endDate");
		StringBuffer sum = new StringBuffer();
		sbSql.append("WITH AAA AS(\n");
		sbSql.append("SELECT \n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String,Object> series = seriesList.get(i);
			sbSql.append("         DECODE(SUM("+series.get("GROUP_NAME")+"),0,'',SUM("+series.get("GROUP_NAME")+")) "+series.get("GROUP_NAME")+"1,\n");
			if(i == seriesList.size()-1){
				sum.append("SUM("+series.get("GROUP_NAME")+")");
			}else{
				sum.append("SUM("+series.get("GROUP_NAME")+")+");
			}
		}
		sbSql.append(sum.toString()+" SUM_COUNT_1,\n");
		sbSql.append("ROOT_ORG_NAME FROM( SELECT  \n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String,Object> series = seriesList.get(i);
			sbSql.append("         NVL(DECODE(C.SERIES_ID,"+series.get("GROUP_ID")+",SUM(NVL(B.CHECK_AMOUNT,0))),0) "+series.get("GROUP_NAME")+",\n");
		}
		sbSql.append(" D.ROOT_ORG_NAME FROM   TT_VS_ORDER A,\n");
		sbSql.append("         TT_VS_ORDER_DETAIL B,\n");
		sbSql.append("         VW_MATERIAL_GROUP_MAT C,\n");
		sbSql.append("         VW_ORG_DEALER D\n");
		sbSql.append(" WHERE       A.ORDER_ID = B.ORDER_ID\n");
		sbSql.append("         AND B.MATERIAL_ID = C.MATERIAL_ID\n");
		sbSql.append("         AND A.DEALER_ID=D.DEALER_ID\n");
		sbSql.append("         AND A.ORDER_STATUS IN (10211007,10211008,10211009,10211011)\n");
		sbSql.append("         AND A.INVOICE_NO IS NOT NULL\n");
		sbSql.append("         AND A.INVO_DATE IS NOT NULL\n");
		if(!"".equals(startDate)){
			sbSql.append("         AND A.INVO_DATE >= TO_DATE('"+startDate+"','YYYY-MM-DD')\n");
		}
		if(!"".equals(endDate)){
			sbSql.append("         AND TRUNC(A.INVO_DATE) < TO_DATE('"+endDate+"','YYYY-MM-DD')+1\n");
		}
		sbSql.append("         GROUP BY D.ROOT_ORG_NAME,C.SERIES_ID,C.SERIES_CODE,C.SERIES_NAME\n");
		sbSql.append("         UNION ALL\n");
		sbSql.append("         ----------------------------------------\n");
		sbSql.append("        SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String,Object> series = seriesList.get(i);
			sbSql.append("         NVL(DECODE(C.SERIES_ID,"+series.get("GROUP_ID")+",SUM(NVL(B.OUT_AMOUNT,0))),0) "+series.get("GROUP_NAME")+",\n");
		}
		sbSql.append(" D.ROOT_ORG_NAME FROM   TT_OUT_ORDER A,\n");
		sbSql.append("         TT_OUT_ORDER_DETAIL B,\n");
		sbSql.append("         VW_MATERIAL_GROUP_MAT C,\n");
		sbSql.append("         VW_ORG_DEALER D\n");
		sbSql.append(" WHERE       A.ORDER_ID = B.ORDER_ID\n");
		sbSql.append("         AND B.MATERIAL_ID = C.MATERIAL_ID\n");
		sbSql.append("         AND A.DEALER_ID=D.DEALER_ID\n");
		sbSql.append("         AND A.ORDER_STATUS = 13741006\n");
		sbSql.append("         AND A.INVOICE_NO IS NOT NULL\n");
		sbSql.append("         AND A.INVO_DATE IS NOT NULL\n");
		if(!"".equals(startDate)){
			sbSql.append("         AND A.INVO_DATE >= TO_DATE('"+startDate+"','YYYY-MM-DD')\n");
		}
		if(!"".equals(endDate)){
			sbSql.append("         AND TRUNC(A.INVO_DATE) < TO_DATE('"+endDate+"','YYYY-MM-DD')+1\n");
		}
		sbSql.append("         GROUP BY D.ROOT_ORG_NAME,C.SERIES_ID,C.SERIES_CODE,C.SERIES_NAME\n");
		sbSql.append("         ----------------------------------------\n");
		sbSql.append("         UNION ALL\n");
		sbSql.append("SELECT \n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String,Object> series = seriesList.get(i);
			sbSql.append("         NVL(DECODE(D.GROUP_ID,"+series.get("GROUP_ID")+",SUM(NVL(CHECK_NUM,0))),0) "+series.get("GROUP_NAME")+",\n");
		}
		sbSql.append(" C.ROOT_ORG_NAME FROM TT_OLD_ORDER A,VW_ORG_DEALER C,TM_VHCL_MATERIAL_GROUP B,TM_VHCL_MATERIAL_GROUP D\n");
		sbSql.append(" WHERE A.DEALER_ID=C.DEALER_ID AND A.MODEL=B.GROUP_CODE\n");
		sbSql.append("  AND A.FLAG IN('0','2','4')\n");
		sbSql.append("  AND B.GROUP_LEVEL=3\n");
		sbSql.append("  AND D.GROUP_LEVEL=2\n");
		sbSql.append("  AND B.PARENT_GROUP_ID=D.GROUP_ID\n");
		if(!"".equals(startDate)){
			sbSql.append("  AND A.INVO_DATE >= TO_DATE('"+startDate+"','YYYY-MM-DD')\n");
		}
		if(!"".equals(endDate)){
			sbSql.append("  AND TRUNC(A.INVO_DATE) < TO_DATE('"+endDate+"','YYYY-MM-DD')+1\n");
		}	
		sbSql.append("  AND PROV_ID NOT IN(\n");
		sbSql.append("    '2013080510045491',\n");
		sbSql.append("    '2013082525987270',\n");
		sbSql.append("    '2013082525987271',\n");
		sbSql.append("    '2013082525987272',\n");
		sbSql.append("    '2013082525987273',\n");
		sbSql.append("    '2013082525987274',\n");
		sbSql.append("    '2013082525987275',\n");
		sbSql.append("    '2013082525987276',\n");
		sbSql.append("    '2013092677635962',\n");
		sbSql.append("    '2013082956190007'\n");
		sbSql.append("  )\n");
		sbSql.append(" GROUP BY C.ROOT_ORG_NAME,D.GROUP_ID\n");
		sbSql.append(") TT GROUP BY ROOT_ORG_NAME),\n");
		sbSql.append("BBB AS(\n");
		sbSql.append("SELECT \n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("DECODE(SUM("+series.get("GROUP_NAME")+"),0,'',SUM("+series.get("GROUP_NAME")+")) "+series.get("GROUP_NAME")+"2,\n");
		}
		sbSql.append(sum.toString()+" SUM_COUNT_2,\n");
		sbSql.append("ROOT_ORG_NAME ROOT_ORG_NAME_2 FROM( SELECT \n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("         NVL(DECODE(D.SERIES_ID,"+series.get("GROUP_ID")+",COUNT(A.VEHICLE_ID)),0) "+series.get("GROUP_NAME")+",\n");
		}
		sbSql.append("  E.ROOT_ORG_NAME  FROM   TT_DEALER_ACTUAL_SALES A,\n");
		sbSql.append("           TM_DEALER B,\n");
		sbSql.append("           TM_VEHICLE C,\n");
		sbSql.append("           VW_MATERIAL_GROUP_MAT D,\n");
		sbSql.append("           VW_ORG_DEALER E\n");
		sbSql.append("   WHERE       A.DEALER_ID = B.DEALER_ID\n");
		sbSql.append("           AND A.VEHICLE_ID = C.VEHICLE_ID\n");
		sbSql.append("           AND C.MATERIAL_ID = D.MATERIAL_ID\n");
		sbSql.append("           AND B.DEALER_ID=E.DEALER_ID AND A.IS_RETURN = 10041002\n");
		if(!"".equals(startDate)){
			sbSql.append("           AND A.SALES_DATE >= TO_DATE('"+startDate+"','YYYY-MM-DD')\n");
		}
		if(!"".equals(endDate)){
			sbSql.append("           AND TRUNC(A.SALES_DATE) < TO_DATE('"+endDate+"','YYYY-MM-DD')+1\n");
		}
		sbSql.append("GROUP BY   E.ROOT_ORG_NAME, D.SERIES_CODE, D.SERIES_NAME,D.SERIES_ID\n");
		sbSql.append(") TTT GROUP BY  ROOT_ORG_NAME)\n");
		sbSql.append("SELECT NVL(BB.ROOT_ORG_NAME_2,'合计') ROOT_ORG_NAME_2, \n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("SUM(AA."+series.get("GROUP_NAME")+"1) "+series.get("GROUP_NAME")+"1,\n");
		}
		sbSql.append("SUM(AA.SUM_COUNT_1) SUM_COUNT_1,");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("SUM(BB."+series.get("GROUP_NAME")+"2) "+series.get("GROUP_NAME")+"2,\n");
		}
		sbSql.append("SUM(BB.SUM_COUNT_2) SUM_COUNT_2 \n");
		sbSql.append(" FROM BBB BB,AAA AA WHERE BB.ROOT_ORG_NAME_2=AA.ROOT_ORG_NAME(+)\n"); 
		sbSql.append("GROUP BY ROLLUP(BB.ROOT_ORG_NAME_2)\n");
		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}
	
	/**
	 * 各大区开票统计
	 * @return
	 */
	public List<Map<String, Object>> getOrgCheckInfo(String censusDate){
		List<Map<String, Object>> seriesList = getSeriesList();
		StringBuffer little_sum = new StringBuffer();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("WITH DAY_CHECK AS ( SELECT * FROM ( SELECT  ROOT_ORG_NAME,ORG_NAME,\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("         DECODE(SUM("+series.get("GROUP_NAME")+"),0,'',SUM("+series.get("GROUP_NAME")+")) "+series.get("GROUP_NAME")+"1,\n");
			if(i == seriesList.size()-1){
				little_sum.append("         \nNVL(SUM("+series.get("GROUP_NAME")+"),0)");
			}else{
				little_sum.append("         \nNVL(SUM("+series.get("GROUP_NAME")+"),0)+");
			}
		}
		sbSql.append(little_sum.toString()+" SUM_COUNT_1 \n");
		sbSql.append("         FROM( SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("         NVL(DECODE(C.SERIES_ID,"+series.get("GROUP_ID")+",SUM(NVL(B.CHECK_AMOUNT,0))),0) "+series.get("GROUP_NAME")+",\n");
		}
		sbSql.append("         D.ORG_NAME,D.ROOT_ORG_NAME FROM   TT_VS_ORDER A,\n");
		sbSql.append("         TT_VS_ORDER_DETAIL B,\n");
		sbSql.append("         VW_MATERIAL_GROUP_MAT C,\n");
		sbSql.append("         VW_ORG_DEALER D\n");
		sbSql.append(" WHERE   A.ORDER_ID = B.ORDER_ID\n");
		sbSql.append("         AND B.MATERIAL_ID = C.MATERIAL_ID\n");
		sbSql.append("         AND A.DEALER_ID=D.DEALER_ID\n");
		sbSql.append("         AND A.ORDER_STATUS IN (10211007,10211008,10211009,10211011)\n");
		sbSql.append("         AND A.INVOICE_NO IS NOT NULL\n");
		sbSql.append("         AND A.INVO_DATE IS NOT NULL\n");
		sbSql.append("         AND TO_CHAR(A.INVO_DATE,'YYYY-MM-DD') = '"+censusDate+"'\n");
		sbSql.append("         GROUP BY D.ORG_NAME,D.ROOT_ORG_NAME,C.SERIES_ID,C.SERIES_CODE,C.SERIES_NAME\n");
		sbSql.append("         UNION ALL\n");
		sbSql.append("         ----------------------------------------\n");
		sbSql.append("        SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("         NVL(DECODE(C.SERIES_ID,"+series.get("GROUP_ID")+",SUM(NVL(B.OUT_AMOUNT,0))),0) "+series.get("GROUP_NAME")+",\n");
		}
		sbSql.append("         D.ORG_NAME,D.ROOT_ORG_NAME FROM   TT_OUT_ORDER A,\n");
		sbSql.append("         TT_OUT_ORDER_DETAIL B,\n");
		sbSql.append("         VW_MATERIAL_GROUP_MAT C,\n");
		sbSql.append("         VW_ORG_DEALER D\n");
		sbSql.append(" WHERE       A.ORDER_ID = B.ORDER_ID\n");
		sbSql.append("         AND B.MATERIAL_ID = C.MATERIAL_ID\n");
		sbSql.append("         AND A.DEALER_ID=D.DEALER_ID\n");
		sbSql.append("         AND A.ORDER_STATUS = 13741006\n");
		sbSql.append("         AND A.INVOICE_NO IS NOT NULL\n");
		sbSql.append("         AND A.INVO_DATE IS NOT NULL\n");
		sbSql.append("         AND TO_CHAR(A.INVO_DATE,'YYYY-MM-DD') = '"+censusDate+"'\n");
		sbSql.append("         GROUP BY D.ORG_NAME,D.ROOT_ORG_NAME,C.SERIES_ID,C.SERIES_CODE,C.SERIES_NAME\n");
		sbSql.append("         ----------------------------------------\n");
		sbSql.append("         UNION ALL\n");
		sbSql.append("SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("         NVL(DECODE(D.GROUP_ID,"+series.get("GROUP_ID")+",SUM(NVL(CHECK_NUM,0))),0) "+series.get("GROUP_NAME")+",\n");
		}
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019870633,SUM(NVL(CHECK_NUM,0))),0) 北斗星,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019871896,SUM(NVL(CHECK_NUM,0))),0) 北斗星X5,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019873341,SUM(NVL(CHECK_NUM,0))),0) 浪迪,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019874575,SUM(NVL(CHECK_NUM,0))),0) 派喜,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019878559,SUM(NVL(CHECK_NUM,0))),0) 利亚纳,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073010000507,SUM(NVL(CHECK_NUM,0))),0) 单排,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073010000509,SUM(NVL(CHECK_NUM,0))),0) 双排,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073010000511,SUM(NVL(CHECK_NUM,0))),0) 福瑞达,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073010000513,SUM(NVL(CHECK_NUM,0))),0) 福瑞达II,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073010000515,SUM(NVL(CHECK_NUM,0))),0) 福运,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019878916,SUM(NVL(CHECK_NUM,0))),0) 爱迪尔,\n");
		sbSql.append(" C.ORG_NAME, C.ROOT_ORG_NAME FROM TT_OLD_ORDER A,VW_ORG_DEALER C,TM_VHCL_MATERIAL_GROUP B,TM_VHCL_MATERIAL_GROUP D\n");
		sbSql.append(" WHERE A.DEALER_ID=C.DEALER_ID AND A.MODEL=B.GROUP_CODE\n");
		sbSql.append("  AND A.FLAG IN('0','2','4')\n");
		sbSql.append("  AND B.GROUP_LEVEL=3\n");
		sbSql.append("  AND D.GROUP_LEVEL=2\n");
		sbSql.append("  AND B.PARENT_GROUP_ID=D.GROUP_ID\n");
		sbSql.append("         AND TO_CHAR(A.INVO_DATE,'YYYY-MM-DD') = '"+censusDate+"'\n");
		sbSql.append("  AND PROV_ID NOT IN(\n");
		sbSql.append("    '2013080510045491',\n");
		sbSql.append("    '2013082525987270',\n");
		sbSql.append("    '2013082525987271',\n");
		sbSql.append("    '2013082525987272',\n");
		sbSql.append("    '2013082525987273',\n");
		sbSql.append("    '2013082525987274',\n");
		sbSql.append("    '2013082525987275',\n");
		sbSql.append("    '2013082525987276'\n");
		sbSql.append("  )\n");
		sbSql.append(" GROUP BY C.ORG_NAME, C.ROOT_ORG_NAME,D.GROUP_ID\n");
		sbSql.append(") TT GROUP BY ROLLUP(ROOT_ORG_NAME),ROLLUP(ORG_NAME)) WHERE ( ROOT_ORG_NAME IS NOT NULL AND ORG_NAME IS NOT NULL ) OR (ROOT_ORG_NAME IS NULL AND ORG_NAME IS NULL)),\n");
		sbSql.append(" MONTH_CHECK AS (SELECT * FROM ( SELECT  ROOT_ORG_NAME,ORG_NAME,\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("         DECODE(SUM("+series.get("GROUP_NAME")+"),0,'',SUM("+series.get("GROUP_NAME")+")) "+series.get("GROUP_NAME")+"2,\n");
		}
//		sbSql.append("         NVL(SUM(北斗星),0) 北斗星1,\n");
//		sbSql.append("         NVL(SUM(北斗星X5),0) 北斗星X51,\n");
//		sbSql.append("         NVL(SUM(浪迪),0) 浪迪1,\n");
//		sbSql.append("         NVL(SUM(派喜),0) 派喜1,\n");
//		sbSql.append("         NVL(SUM(利亚纳),0) 利亚纳1,\n");
//		sbSql.append("         NVL(SUM(单排),0) 单排1,\n");
//		sbSql.append("         NVL(SUM(双排),0) 双排1,\n");
//		sbSql.append("         NVL(SUM(福瑞达),0) 福瑞达1,\n");
//		sbSql.append("         NVL(SUM(福瑞达II),0) 福瑞达II1,\n");
//		sbSql.append("         NVL(SUM(福运),0) 福运1,\n");
//		sbSql.append("         NVL(SUM(爱迪尔),0) 爱迪尔1,\n");
//		sbSql.append("\n");
//		sbSql.append("        NVL(SUM(北斗星),0)+\n");
//		sbSql.append("        NVL(SUM(北斗星X5),0)+\n");
//		sbSql.append("        NVL(SUM(浪迪),0)+\n");
//		sbSql.append("        NVL(SUM(派喜),0)+\n");
//		sbSql.append("        NVL(SUM(利亚纳),0)+\n");
//		sbSql.append("        NVL(SUM(单排),0)+\n");
//		sbSql.append("        NVL(SUM(双排),0)+\n");
//		sbSql.append("        NVL(SUM(福瑞达),0)+\n");
//		sbSql.append("        NVL(SUM(福瑞达II),0)+\n");
//		sbSql.append("        NVL(SUM(福运),0)+\n");
//		sbSql.append("        NVL(SUM(爱迪尔),0)  SUM_COUNT_1\n");
		sbSql.append(little_sum.toString()+" SUM_COUNT_2 \n");
		sbSql.append("         FROM( SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("         NVL(DECODE(C.SERIES_ID,"+series.get("GROUP_ID")+",SUM(NVL(B.CHECK_AMOUNT,0))),0) "+series.get("GROUP_NAME")+",\n");
		}
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019870633,SUM(NVL(B.CHECK_AMOUNT,0))),0) 北斗星,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019871896,SUM(NVL(B.CHECK_AMOUNT,0))),0) 北斗星X5,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019873341,SUM(NVL(B.CHECK_AMOUNT,0))),0) 浪迪,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019874575,SUM(NVL(B.CHECK_AMOUNT,0))),0) 派喜,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019878559,SUM(NVL(B.CHECK_AMOUNT,0))),0) 利亚纳,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000507,SUM(NVL(B.CHECK_AMOUNT,0))),0) 单排,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000509,SUM(NVL(B.CHECK_AMOUNT,0))),0) 双排,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000511,SUM(NVL(B.CHECK_AMOUNT,0))),0) 福瑞达,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000513,SUM(NVL(B.CHECK_AMOUNT,0))),0) 福瑞达II,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000515,SUM(NVL(B.CHECK_AMOUNT,0))),0) 福运,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019878916,SUM(NVL(B.CHECK_AMOUNT,0))),0) 爱迪尔,\n");
		sbSql.append("         D.ORG_NAME,D.ROOT_ORG_NAME FROM   TT_VS_ORDER A,\n");
		sbSql.append("         TT_VS_ORDER_DETAIL B,\n");
		sbSql.append("         VW_MATERIAL_GROUP_MAT C,\n");
		sbSql.append("         VW_ORG_DEALER D\n");
		sbSql.append(" WHERE   A.ORDER_ID = B.ORDER_ID\n");
		sbSql.append("         AND B.MATERIAL_ID = C.MATERIAL_ID\n");
		sbSql.append("         AND A.DEALER_ID=D.DEALER_ID\n");
		sbSql.append("         AND A.ORDER_STATUS IN (10211007,10211008,10211009,10211011)\n");
		sbSql.append("         AND A.INVOICE_NO IS NOT NULL\n");
		sbSql.append("         AND A.INVO_DATE IS NOT NULL\n");
		sbSql.append("         AND TO_CHAR(A.INVO_DATE,'YYYY-MM') = '"+censusDate.substring(0,7)+"'\n");
		sbSql.append("         AND A.INVO_DATE < TO_DATE('"+censusDate+"','yyyy-MM-dd')+1\n");
		sbSql.append("         GROUP BY D.ORG_NAME, D.ROOT_ORG_NAME,C.SERIES_ID,C.SERIES_CODE,C.SERIES_NAME\n");
		sbSql.append("         UNION ALL\n");
		sbSql.append("         ----------------------------------------\n");
		sbSql.append("        SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("         NVL(DECODE(C.SERIES_ID,"+series.get("GROUP_ID")+",SUM(NVL(B.OUT_AMOUNT,0))),0) "+series.get("GROUP_NAME")+",\n");
		}
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019870633,SUM(NVL(B.OUT_AMOUNT,0))),0) 北斗星,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019871896,SUM(NVL(B.OUT_AMOUNT,0))),0) 北斗星X5,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019873341,SUM(NVL(B.OUT_AMOUNT,0))),0) 浪迪,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019874575,SUM(NVL(B.OUT_AMOUNT,0))),0) 派喜,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019878559,SUM(NVL(B.OUT_AMOUNT,0))),0) 利亚纳,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000507,SUM(NVL(B.OUT_AMOUNT,0))),0) 单排,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000509,SUM(NVL(B.OUT_AMOUNT,0))),0) 双排,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000511,SUM(NVL(B.OUT_AMOUNT,0))),0) 福瑞达,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000513,SUM(NVL(B.OUT_AMOUNT,0))),0) 福瑞达II,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000515,SUM(NVL(B.OUT_AMOUNT,0))),0) 福运,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019878916,SUM(NVL(B.OUT_AMOUNT,0))),0) 爱迪尔,\n");
		sbSql.append("         D.ORG_NAME,D.ROOT_ORG_NAME FROM   TT_OUT_ORDER A,\n");
		sbSql.append("         TT_OUT_ORDER_DETAIL B,\n");
		sbSql.append("         VW_MATERIAL_GROUP_MAT C,\n");
		sbSql.append("         VW_ORG_DEALER D\n");
		sbSql.append(" WHERE       A.ORDER_ID = B.ORDER_ID\n");
		sbSql.append("         AND B.MATERIAL_ID = C.MATERIAL_ID\n");
		sbSql.append("         AND A.DEALER_ID=D.DEALER_ID\n");
		sbSql.append("         AND A.ORDER_STATUS = 13741006\n");
		sbSql.append("         AND A.INVOICE_NO IS NOT NULL\n");
		sbSql.append("         AND A.INVO_DATE IS NOT NULL\n");
		sbSql.append("         AND TO_CHAR(A.INVO_DATE,'YYYY-MM') = '"+censusDate.substring(0,7)+"'\n");
		sbSql.append("         AND A.INVO_DATE < TO_DATE('"+censusDate+"','yyyy-MM-dd')+1\n");
		sbSql.append("         GROUP BY D.ORG_NAME, D.ROOT_ORG_NAME,C.SERIES_ID,C.SERIES_CODE,C.SERIES_NAME\n");
		sbSql.append("         ----------------------------------------\n");
		sbSql.append("         UNION ALL\n");
		sbSql.append("SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("         NVL(DECODE(D.GROUP_ID,"+series.get("GROUP_ID")+",SUM(NVL(CHECK_NUM,0))),0) "+series.get("GROUP_NAME")+",\n");
		}
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019870633,SUM(NVL(CHECK_NUM,0))),0) 北斗星,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019871896,SUM(NVL(CHECK_NUM,0))),0) 北斗星X5,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019873341,SUM(NVL(CHECK_NUM,0))),0) 浪迪,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019874575,SUM(NVL(CHECK_NUM,0))),0) 派喜,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019878559,SUM(NVL(CHECK_NUM,0))),0) 利亚纳,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073010000507,SUM(NVL(CHECK_NUM,0))),0) 单排,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073010000509,SUM(NVL(CHECK_NUM,0))),0) 双排,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073010000511,SUM(NVL(CHECK_NUM,0))),0) 福瑞达,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073010000513,SUM(NVL(CHECK_NUM,0))),0) 福瑞达II,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073010000515,SUM(NVL(CHECK_NUM,0))),0) 福运,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019878916,SUM(NVL(CHECK_NUM,0))),0) 爱迪尔,\n");
		sbSql.append(" C.ORG_NAME,C.ROOT_ORG_NAME FROM TT_OLD_ORDER A,VW_ORG_DEALER C,TM_VHCL_MATERIAL_GROUP B,TM_VHCL_MATERIAL_GROUP D\n");
		sbSql.append(" WHERE A.DEALER_ID=C.DEALER_ID AND A.MODEL=B.GROUP_CODE\n");
		sbSql.append("  AND A.FLAG IN('0','2','4')\n");
		sbSql.append("  AND B.GROUP_LEVEL=3\n");
		sbSql.append("  AND D.GROUP_LEVEL=2\n");
		sbSql.append("  AND B.PARENT_GROUP_ID=D.GROUP_ID\n");
		sbSql.append("         AND TO_CHAR(A.INVO_DATE,'YYYY-MM') = '"+censusDate.substring(0,7)+"'\n");
		sbSql.append("    AND A.INVO_DATE < TO_DATE("+censusDate+",'yyyy-MM-dd')+1\n");
		sbSql.append("     AND PROV_ID NOT IN(\n");
		sbSql.append("    '2013080510045491',\n");
		sbSql.append("    '2013082525987270',\n");
		sbSql.append("    '2013082525987271',\n");
		sbSql.append("    '2013082525987272',\n");
		sbSql.append("    '2013082525987273',\n");
		sbSql.append("    '2013082525987274',\n");
		sbSql.append("    '2013082525987275',\n");
		sbSql.append("    '2013082525987276'\n");
		sbSql.append("  )\n");
		sbSql.append(" GROUP BY C.ORG_NAME,C.ROOT_ORG_NAME,D.GROUP_ID\n");
		sbSql.append(") TT GROUP BY ROLLUP(ROOT_ORG_NAME),ROLLUP(ORG_NAME)) WHERE ( ROOT_ORG_NAME IS NOT NULL AND ORG_NAME IS NOT NULL ) OR (ROOT_ORG_NAME IS NULL AND ORG_NAME IS NULL)),\n");
		sbSql.append(" YEAR_CHECK AS (SELECT * FROM ( SELECT ROOT_ORG_NAME,ORG_NAME,\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("         DECODE(SUM("+series.get("GROUP_NAME")+"),0,'',SUM("+series.get("GROUP_NAME")+")) "+series.get("GROUP_NAME")+"3,\n");
		}
//		sbSql.append("         NVL(SUM(北斗星),0) 北斗星1,\n");
//		sbSql.append("         NVL(SUM(北斗星X5),0) 北斗星X51,\n");
//		sbSql.append("         NVL(SUM(浪迪),0) 浪迪1,\n");
//		sbSql.append("         NVL(SUM(派喜),0) 派喜1,\n");
//		sbSql.append("         NVL(SUM(利亚纳),0) 利亚纳1,\n");
//		sbSql.append("         NVL(SUM(单排),0) 单排1,\n");
//		sbSql.append("         NVL(SUM(双排),0) 双排1,\n");
//		sbSql.append("         NVL(SUM(福瑞达),0) 福瑞达1,\n");
//		sbSql.append("         NVL(SUM(福瑞达II),0) 福瑞达II1,\n");
//		sbSql.append("         NVL(SUM(福运),0) 福运1,\n");
//		sbSql.append("         NVL(SUM(爱迪尔),0) 爱迪尔1,\n");
//		sbSql.append("\n");
//		sbSql.append("        NVL(SUM(北斗星),0)+\n");
//		sbSql.append("        NVL(SUM(北斗星X5),0)+\n");
//		sbSql.append("        NVL(SUM(浪迪),0)+\n");
//		sbSql.append("        NVL(SUM(派喜),0)+\n");
//		sbSql.append("        NVL(SUM(利亚纳),0)+\n");
//		sbSql.append("        NVL(SUM(单排),0)+\n");
//		sbSql.append("        NVL(SUM(双排),0)+\n");
//		sbSql.append("        NVL(SUM(福瑞达),0)+\n");
//		sbSql.append("        NVL(SUM(福瑞达II),0)+\n");
//		sbSql.append("        NVL(SUM(福运),0)+\n");
//		sbSql.append("        NVL(SUM(爱迪尔),0)  SUM_COUNT_1\n");
		sbSql.append(little_sum.toString()+" SUM_COUNT_3 \n");
		sbSql.append("         FROM( SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("         NVL(DECODE(C.SERIES_ID,"+series.get("GROUP_ID")+",SUM(NVL(B.CHECK_AMOUNT,0))),0) "+series.get("GROUP_NAME")+",\n");
		}
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019870633,SUM(NVL(B.CHECK_AMOUNT,0))),0) 北斗星,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019871896,SUM(NVL(B.CHECK_AMOUNT,0))),0) 北斗星X5,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019873341,SUM(NVL(B.CHECK_AMOUNT,0))),0) 浪迪,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019874575,SUM(NVL(B.CHECK_AMOUNT,0))),0) 派喜,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019878559,SUM(NVL(B.CHECK_AMOUNT,0))),0) 利亚纳,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000507,SUM(NVL(B.CHECK_AMOUNT,0))),0) 单排,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000509,SUM(NVL(B.CHECK_AMOUNT,0))),0) 双排,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000511,SUM(NVL(B.CHECK_AMOUNT,0))),0) 福瑞达,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000513,SUM(NVL(B.CHECK_AMOUNT,0))),0) 福瑞达II,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000515,SUM(NVL(B.CHECK_AMOUNT,0))),0) 福运,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019878916,SUM(NVL(B.CHECK_AMOUNT,0))),0) 爱迪尔,\n");
		sbSql.append("         D.ORG_NAME,D.ROOT_ORG_NAME FROM   TT_VS_ORDER A,\n");
		sbSql.append("         TT_VS_ORDER_DETAIL B,\n");
		sbSql.append("         VW_MATERIAL_GROUP_MAT C,\n");
		sbSql.append("         VW_ORG_DEALER D\n");
		sbSql.append(" WHERE   A.ORDER_ID = B.ORDER_ID\n");
		sbSql.append("         AND B.MATERIAL_ID = C.MATERIAL_ID\n");
		sbSql.append("         AND A.DEALER_ID=D.DEALER_ID\n");
		sbSql.append("         AND A.ORDER_STATUS IN (10211007,10211008,10211009,10211011)\n");
		sbSql.append("         AND A.INVOICE_NO IS NOT NULL\n");
		sbSql.append("         AND A.INVO_DATE IS NOT NULL\n");
		sbSql.append("         AND TO_CHAR(A.INVO_DATE,'YYYY') = '"+censusDate.substring(0,4)+"' AND A.INVO_DATE < TO_DATE('"+censusDate+"','yyyy-MM-dd')+1\n");
		sbSql.append("         GROUP BY D.ORG_NAME,D.ROOT_ORG_NAME,C.SERIES_ID,C.SERIES_CODE,C.SERIES_NAME\n");
		sbSql.append("         UNION ALL\n");
		sbSql.append("         ----------------------------------------\n");
		sbSql.append("        SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("         NVL(DECODE(C.SERIES_ID,"+series.get("GROUP_ID")+",SUM(NVL(B.OUT_AMOUNT,0))),0) "+series.get("GROUP_NAME")+",\n");
		}
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019870633,SUM(NVL(B.OUT_AMOUNT,0))),0) 北斗星,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019871896,SUM(NVL(B.OUT_AMOUNT,0))),0) 北斗星X5,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019873341,SUM(NVL(B.OUT_AMOUNT,0))),0) 浪迪,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019874575,SUM(NVL(B.OUT_AMOUNT,0))),0) 派喜,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019878559,SUM(NVL(B.OUT_AMOUNT,0))),0) 利亚纳,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000507,SUM(NVL(B.OUT_AMOUNT,0))),0) 单排,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000509,SUM(NVL(B.OUT_AMOUNT,0))),0) 双排,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000511,SUM(NVL(B.OUT_AMOUNT,0))),0) 福瑞达,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000513,SUM(NVL(B.OUT_AMOUNT,0))),0) 福瑞达II,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073010000515,SUM(NVL(B.OUT_AMOUNT,0))),0) 福运,\n");
//		sbSql.append("         NVL(DECODE(C.SERIES_ID,2013073019878916,SUM(NVL(B.OUT_AMOUNT,0))),0) 爱迪尔,\n");
		sbSql.append("         D.ORG_NAME,D.ROOT_ORG_NAME FROM   TT_OUT_ORDER A,\n");
		sbSql.append("         TT_OUT_ORDER_DETAIL B,\n");
		sbSql.append("         VW_MATERIAL_GROUP_MAT C,\n");
		sbSql.append("         VW_ORG_DEALER D\n");
		sbSql.append(" WHERE       A.ORDER_ID = B.ORDER_ID\n");
		sbSql.append("         AND B.MATERIAL_ID = C.MATERIAL_ID\n");
		sbSql.append("         AND A.DEALER_ID=D.DEALER_ID\n");
		sbSql.append("         AND A.ORDER_STATUS = 13741006\n");
		sbSql.append("         AND A.INVOICE_NO IS NOT NULL\n");
		sbSql.append("         AND A.INVO_DATE IS NOT NULL\n");
		sbSql.append("         AND TO_CHAR(A.INVO_DATE,'YYYY') = '"+censusDate.substring(0,4)+"' AND A.INVO_DATE < TO_DATE('"+censusDate+"','yyyy-MM-dd')+1\n");
		sbSql.append("         GROUP BY D.ORG_NAME,D.ROOT_ORG_NAME,C.SERIES_ID,C.SERIES_CODE,C.SERIES_NAME\n");
		sbSql.append("         ----------------------------------------\n");
		sbSql.append("         UNION ALL\n");
		sbSql.append("SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("         NVL(DECODE(D.GROUP_ID,"+series.get("GROUP_ID")+",SUM(NVL(CHECK_NUM,0))),0) "+series.get("GROUP_NAME")+",\n");
		}
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019870633,SUM(NVL(CHECK_NUM,0))),0) 北斗星,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019871896,SUM(NVL(CHECK_NUM,0))),0) 北斗星X5,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019873341,SUM(NVL(CHECK_NUM,0))),0) 浪迪,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019874575,SUM(NVL(CHECK_NUM,0))),0) 派喜,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019878559,SUM(NVL(CHECK_NUM,0))),0) 利亚纳,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073010000507,SUM(NVL(CHECK_NUM,0))),0) 单排,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073010000509,SUM(NVL(CHECK_NUM,0))),0) 双排,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073010000511,SUM(NVL(CHECK_NUM,0))),0) 福瑞达,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073010000513,SUM(NVL(CHECK_NUM,0))),0) 福瑞达II,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073010000515,SUM(NVL(CHECK_NUM,0))),0) 福运,\n");
//		sbSql.append("         NVL(DECODE(D.GROUP_ID,2013073019878916,SUM(NVL(CHECK_NUM,0))),0) 爱迪尔,\n");
		sbSql.append(" C.ORG_NAME,C.ROOT_ORG_NAME FROM TT_OLD_ORDER A,VW_ORG_DEALER C,TM_VHCL_MATERIAL_GROUP B,TM_VHCL_MATERIAL_GROUP D\n");
		sbSql.append(" WHERE A.DEALER_ID=C.DEALER_ID AND A.MODEL=B.GROUP_CODE\n");
		sbSql.append("  AND A.FLAG IN('0','2','4')\n");
		sbSql.append("  AND B.GROUP_LEVEL=3\n");
		sbSql.append("  AND D.GROUP_LEVEL=2\n");
		sbSql.append("  AND B.PARENT_GROUP_ID=D.GROUP_ID\n");
		sbSql.append("  AND TO_CHAR(A.INVO_DATE,'YYYY') = '"+censusDate.substring(0,4)+"' AND A.INVO_DATE < TO_DATE('"+censusDate+"','yyyy-MM-dd')+1\n");
		sbSql.append("  AND PROV_ID NOT IN(\n");
		sbSql.append("    '2013080510045491',\n");
		sbSql.append("    '2013082525987270',\n");
		sbSql.append("    '2013082525987271',\n");
		sbSql.append("    '2013082525987272',\n");
		sbSql.append("    '2013082525987273',\n");
		sbSql.append("    '2013082525987274',\n");
		sbSql.append("    '2013082525987275',\n");
		sbSql.append("    '2013082525987276'\n");
		sbSql.append("  )\n");
		sbSql.append(" GROUP BY C.ORG_NAME,C.ROOT_ORG_NAME,D.GROUP_ID\n");
		sbSql.append(") TT GROUP BY ROLLUP(ROOT_ORG_NAME),ROLLUP(ORG_NAME)) WHERE ( ROOT_ORG_NAME IS NOT NULL AND ORG_NAME IS NOT NULL ) OR (ROOT_ORG_NAME IS NULL AND ORG_NAME IS NULL))\n");
		sbSql.append("SELECT C.ROOT_ORG_NAME ROOT_ORG,C.ORG_NAME ORG,A.*,B.*,C.* \n");
		sbSql.append("  FROM DAY_CHECK A ,\n");
		sbSql.append("       MONTH_CHECK B,\n");
		sbSql.append("       YEAR_CHECK C\n");
		sbSql.append(" WHERE NVL(C.ORG_NAME,'合计') = NVL(B.ORG_NAME(+),'合计')\n");
		sbSql.append("   AND NVL(C.ORG_NAME,'合计') = NVL(A.ORG_NAME(+),'合计')\n");
		sbSql.append("   ORDER BY C.ROOT_ORG_NAME ASC ,C.ORG_NAME ASC"); 
		return pageQuery(sbSql.toString(), null, getFunName());
	}
	
	public List<Map<String, Object>> getOrgSalesInfo(String censusDate){
		List<Map<String, Object>> seriesList = getSeriesList();
		StringBuffer little_sum = new StringBuffer();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("WITH DAY_SALES AS (SELECT * FROM (SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("              DECODE(SUM("+series.get("GROUP_NAME")+"),0,'',SUM("+series.get("GROUP_NAME")+")) "+series.get("GROUP_NAME")+"1,\n");
			if(i == seriesList.size()-1){
				little_sum.append("              \nSUM("+series.get("GROUP_NAME")+")");
			}else{
				little_sum.append("              \nSUM("+series.get("GROUP_NAME")+")+");
			}
		}
//		sbSql.append("              SUM(北斗星) 北斗星2,\n");
//		sbSql.append("              SUM(北斗星X5) 北斗星X52,\n");
//		sbSql.append("              SUM(浪迪) 浪迪2,\n");
//		sbSql.append("              SUM(派喜) 派喜2,\n");
//		sbSql.append("              SUM(利亚纳) 利亚纳2,\n");
//		sbSql.append("              SUM(单排) 单排2,\n");
//		sbSql.append("              SUM(双排) 双排2,\n");
//		sbSql.append("              SUM(福瑞达) 福瑞达2,\n");
//		sbSql.append("              SUM(福瑞达II) 福瑞达II2,\n");
//		sbSql.append("              SUM(福运) 福运2,\n");
//		sbSql.append("              SUM(爱迪尔) 爱迪尔2,\n");
		sbSql.append(little_sum.toString()+" SUM_COUNT_1,\n");
		sbSql.append("              ORG_NAME, ROOT_ORG_NAME FROM( SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("                     NVL(DECODE(D.SERIES_ID,"+series.get("GROUP_ID")+",COUNT(A.VEHICLE_ID)),0) "+series.get("GROUP_NAME")+",\n");
		}
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019870633,COUNT(A.VEHICLE_ID)),0) 北斗星,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019871896,COUNT(A.VEHICLE_ID)),0) 北斗星X5,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019873341,COUNT(A.VEHICLE_ID)),0) 浪迪,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019874575,COUNT(A.VEHICLE_ID)),0) 派喜,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019878559,COUNT(A.VEHICLE_ID)),0) 利亚纳,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073010000507,COUNT(A.VEHICLE_ID)),0) 单排,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073010000509,COUNT(A.VEHICLE_ID)),0) 双排,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073010000511,COUNT(A.VEHICLE_ID)),0) 福瑞达,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073010000513,COUNT(A.VEHICLE_ID)),0) 福瑞达II,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073010000515,COUNT(A.VEHICLE_ID)),0) 福运,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019878916,COUNT(A.VEHICLE_ID)),0) 爱迪尔,\n");
		sbSql.append("                     E.ORG_NAME,E.ROOT_ORG_NAME  FROM   TT_DEALER_ACTUAL_SALES A,\n");
		sbSql.append("                       TM_DEALER B,\n");
		sbSql.append("                       TM_VEHICLE C,\n");
		sbSql.append("                       VW_MATERIAL_GROUP_MAT D,\n");
		sbSql.append("                       VW_ORG_DEALER E\n");
		sbSql.append("               WHERE       A.DEALER_ID = B.DEALER_ID\n");
		sbSql.append("                       AND A.VEHICLE_ID = C.VEHICLE_ID AND A.IS_RETURN = 10041002 \n");
		sbSql.append("                       AND C.MATERIAL_ID = D.MATERIAL_ID\n");
		sbSql.append("                       AND B.DEALER_ID=E.DEALER_ID\n");
		sbSql.append("                       AND TO_CHAR(A.SALES_DATE,'YYYY-MM-DD') = '"+censusDate+"'\n");
		sbSql.append("            GROUP BY   E.ORG_NAME,E.ROOT_ORG_NAME, D.SERIES_CODE, D.SERIES_NAME,D.SERIES_ID\n");
		sbSql.append("           ) TTT GROUP BY ROLLUP(ROOT_ORG_NAME),ROLLUP(ORG_NAME))\n");
		sbSql.append("           WHERE (ORG_NAME IS NOT NULL AND ROOT_ORG_NAME IS NOT NULL) OR (ORG_NAME IS NULL AND ROOT_ORG_NAME IS NULL)),\n");
		sbSql.append("MONTH_SALES AS (SELECT * FROM (SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("              DECODE(SUM("+series.get("GROUP_NAME")+"),0,'',SUM("+series.get("GROUP_NAME")+")) "+series.get("GROUP_NAME")+"2,\n");
		}
//		sbSql.append("              SUM(北斗星) 北斗星2,\n");
//		sbSql.append("              SUM(北斗星X5) 北斗星X52,\n");
//		sbSql.append("              SUM(浪迪) 浪迪2,\n");
//		sbSql.append("              SUM(派喜) 派喜2,\n");
//		sbSql.append("              SUM(利亚纳) 利亚纳2,\n");
//		sbSql.append("              SUM(单排) 单排2,\n");
//		sbSql.append("              SUM(双排) 双排2,\n");
//		sbSql.append("              SUM(福瑞达) 福瑞达2,\n");
//		sbSql.append("              SUM(福瑞达II) 福瑞达II2,\n");
//		sbSql.append("              SUM(福运) 福运2,\n");
//		sbSql.append("              SUM(爱迪尔) 爱迪尔2,\n");
		sbSql.append(little_sum.toString()+" SUM_COUNT_2,\n");
		sbSql.append("              ORG_NAME, ROOT_ORG_NAME FROM( SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("                     NVL(DECODE(D.SERIES_ID,"+series.get("GROUP_ID")+",COUNT(A.VEHICLE_ID)),0) "+series.get("GROUP_NAME")+",\n");
		}
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019870633,COUNT(A.VEHICLE_ID)),0) 北斗星,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019871896,COUNT(A.VEHICLE_ID)),0) 北斗星X5,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019873341,COUNT(A.VEHICLE_ID)),0) 浪迪,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019874575,COUNT(A.VEHICLE_ID)),0) 派喜,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019878559,COUNT(A.VEHICLE_ID)),0) 利亚纳,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073010000507,COUNT(A.VEHICLE_ID)),0) 单排,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073010000509,COUNT(A.VEHICLE_ID)),0) 双排,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073010000511,COUNT(A.VEHICLE_ID)),0) 福瑞达,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073010000513,COUNT(A.VEHICLE_ID)),0) 福瑞达II,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073010000515,COUNT(A.VEHICLE_ID)),0) 福运,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019878916,COUNT(A.VEHICLE_ID)),0) 爱迪尔,\n");
		sbSql.append("                     E.ORG_NAME,E.ROOT_ORG_NAME  FROM   TT_DEALER_ACTUAL_SALES A,\n");
		sbSql.append("                       TM_DEALER B,\n");
		sbSql.append("                       TM_VEHICLE C,\n");
		sbSql.append("                       VW_MATERIAL_GROUP_MAT D,\n");
		sbSql.append("                       VW_ORG_DEALER E\n");
		sbSql.append("               WHERE       A.DEALER_ID = B.DEALER_ID\n");
		sbSql.append("                       AND A.VEHICLE_ID = C.VEHICLE_ID AND A.IS_RETURN = 10041002\n");
		sbSql.append("                       AND C.MATERIAL_ID = D.MATERIAL_ID\n");
		sbSql.append("                       AND B.DEALER_ID=E.DEALER_ID\n");
		sbSql.append("                       AND TO_CHAR(A.SALES_DATE,'YYYY-MM') = '"+censusDate.substring(0,7)+"'\n");
		sbSql.append("                       AND A.SALES_DATE < TO_DATE('"+censusDate+"','yyyy-MM-dd')+1\n");
		sbSql.append("            GROUP BY   E.ORG_NAME,E.ROOT_ORG_NAME, D.SERIES_CODE, D.SERIES_NAME,D.SERIES_ID\n");
		sbSql.append("           ) TTT GROUP BY ROLLUP(ROOT_ORG_NAME),ROLLUP(ORG_NAME))\n");
		sbSql.append("           WHERE (ORG_NAME IS NOT NULL AND ROOT_ORG_NAME IS NOT NULL) OR (ORG_NAME IS NULL AND ROOT_ORG_NAME IS NULL)),\n");
		sbSql.append("    YEAR_SALES AS (SELECT * FROM (SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("              DECODE(SUM("+series.get("GROUP_NAME")+"),0,'',SUM("+series.get("GROUP_NAME")+")) "+series.get("GROUP_NAME")+"3,\n");
		}
//		sbSql.append("              SUM(北斗星) 北斗星2,\n");
//		sbSql.append("              SUM(北斗星X5) 北斗星X52,\n");
//		sbSql.append("              SUM(浪迪) 浪迪2,\n");
//		sbSql.append("              SUM(派喜) 派喜2,\n");
//		sbSql.append("              SUM(利亚纳) 利亚纳2,\n");
//		sbSql.append("              SUM(单排) 单排2,\n");
//		sbSql.append("              SUM(双排) 双排2,\n");
//		sbSql.append("              SUM(福瑞达) 福瑞达2,\n");
//		sbSql.append("              SUM(福瑞达II) 福瑞达II2,\n");
//		sbSql.append("              SUM(福运) 福运2,\n");
//		sbSql.append("              SUM(爱迪尔) 爱迪尔2,\n");
		sbSql.append(little_sum.toString()+" SUM_COUNT_3,\n");
		sbSql.append("              ORG_NAME, ROOT_ORG_NAME FROM( SELECT\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("                     NVL(DECODE(D.SERIES_ID,"+series.get("GROUP_ID")+",COUNT(A.VEHICLE_ID)),0) "+series.get("GROUP_NAME")+",\n");
		}
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019870633,COUNT(A.VEHICLE_ID)),0) 北斗星,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019871896,COUNT(A.VEHICLE_ID)),0) 北斗星X5,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019873341,COUNT(A.VEHICLE_ID)),0) 浪迪,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019874575,COUNT(A.VEHICLE_ID)),0) 派喜,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019878559,COUNT(A.VEHICLE_ID)),0) 利亚纳,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073010000507,COUNT(A.VEHICLE_ID)),0) 单排,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073010000509,COUNT(A.VEHICLE_ID)),0) 双排,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073010000511,COUNT(A.VEHICLE_ID)),0) 福瑞达,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073010000513,COUNT(A.VEHICLE_ID)),0) 福瑞达II,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073010000515,COUNT(A.VEHICLE_ID)),0) 福运,\n");
//		sbSql.append("                     NVL(DECODE(D.SERIES_ID,2013073019878916,COUNT(A.VEHICLE_ID)),0) 爱迪尔,\n");
		sbSql.append("                     E.ORG_NAME,E.ROOT_ORG_NAME  FROM   TT_DEALER_ACTUAL_SALES A,\n");
		sbSql.append("                       TM_DEALER B,\n");
		sbSql.append("                       TM_VEHICLE C,\n");
		sbSql.append("                       VW_MATERIAL_GROUP_MAT D,\n");
		sbSql.append("                       VW_ORG_DEALER E\n");
		sbSql.append("               WHERE       A.DEALER_ID = B.DEALER_ID AND A.IS_RETURN = 10041002\n");
		sbSql.append("                       AND A.VEHICLE_ID = C.VEHICLE_ID\n");
		sbSql.append("                       AND C.MATERIAL_ID = D.MATERIAL_ID\n");
		sbSql.append("                       AND B.DEALER_ID=E.DEALER_ID\n");
		sbSql.append("                       AND TO_CHAR(A.SALES_DATE,'YYYY') = '"+censusDate.substring(0,4)+"'\n");
		sbSql.append("                       AND A.SALES_DATE < TO_DATE('"+censusDate+"','yyyy-MM-dd')+1\n");
		sbSql.append("            GROUP BY   E.ORG_NAME,E.ROOT_ORG_NAME, D.SERIES_CODE, D.SERIES_NAME,D.SERIES_ID\n");
		sbSql.append("           ) TTT GROUP BY ROLLUP(ROOT_ORG_NAME),ROLLUP(ORG_NAME))\n");
		sbSql.append("           WHERE (ORG_NAME IS NOT NULL AND ROOT_ORG_NAME IS NOT NULL) OR (ORG_NAME IS NULL AND ROOT_ORG_NAME IS NULL))\n");
		sbSql.append("       SELECT C.ROOT_ORG_NAME ROOT_ORG,C.ORG_NAME ORG,A.*,B.*,C.*\n");
		sbSql.append("         FROM DAY_SALES A,\n");
		sbSql.append("              MONTH_SALES B,\n");
		sbSql.append("              YEAR_SALES C\n");
		sbSql.append("         WHERE NVL(C.ORG_NAME,'合计') = NVL(B.ORG_NAME(+),'合计')\n");
		sbSql.append("           AND NVL(C.ORG_NAME,'合计') = NVL(A.ORG_NAME(+),'合计')\n");
		sbSql.append("           ORDER BY C.ROOT_ORG_NAME ASC,C.ORG_NAME ASC"); 
		return pageQuery(sbSql.toString(), null, getFunName());
	}
	
	//得到车系列表
	public List<Map<String, Object>> getSeriesList(){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT GROUP_ID,GROUP_NAME\n");
		sbSql.append("  FROM TM_VHCL_MATERIAL_GROUP VMG\n");
		sbSql.append(" WHERE VMG.GROUP_LEVEL = 2\n");
		sbSql.append("   AND STATUS = 10011001\n");
		sbSql.append(" ORDER BY PARENT_GROUP_ID,GROUP_ID"); 
		return pageQuery(sbSql.toString(), null, getFunName());
	}
}
