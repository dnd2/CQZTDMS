package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class dealerSalesReportQueryDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(BillDetailTicketDao.class);
	public static dealerSalesReportQueryDao dao = new dealerSalesReportQueryDao();
	public static dealerSalesReportQueryDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Map<String, Object>> getDealerSaleInfo(Map<String, Object> map){
		String command = (String)map.get("command");
		String dealerId = map.get("dealerId").toString();
		String poseId = map.get("poseId").toString();
		String startDate = map.get("startDate").toString();
		String endDate = map.get("endDate").toString();
		String sonDealer = map.get("sonDealer")==null?"":map.get("sonDealer").toString();
		List<Map<String, Object>> seriesList = getSeriesList();
		List<String> params = new ArrayList<String>();
		StringBuffer sbSql = new StringBuffer();
		StringBuffer sum = new StringBuffer();
		if(("1".equals(command) && "yes".equals(sonDealer)) || "2".equals(command)){//一级经销商显示下级或者是二级经销商
			sbSql.append("SELECT ");
			for(int i = 0 ; i < seriesList.size() ; i++){
				Map<String, Object> series = seriesList.get(i);
				sbSql.append("   SUM("+series.get("GROUP_NAME")+") "+series.get("GROUP_NAME")+",\n");
				if(i == seriesList.size()-1){
					sum.append("   SUM("+series.get("GROUP_NAME")+")");
				}else{
					sum.append("   SUM("+series.get("GROUP_NAME")+")+ ");
				}
			}
			sbSql.append(sum.toString()+" SUM_COUNT,\n");
			sbSql.append("DEALER_ID,DEALER_CODE,NVL(DEALER_NAME,'合计') DEALER_NAME \n");
			sbSql.append("FROM( SELECT   \n");
			for(int i = 0 ; i < seriesList.size() ; i++){
				Map<String, Object> series = seriesList.get(i);
				sbSql.append("         NVL(DECODE(D.SERIES_ID,"+series.get("GROUP_ID")+",COUNT(A.VEHICLE_ID)),0) "+series.get("GROUP_NAME")+",\n");
			}
			sbSql.append("         E.DEALER_ID,E.DEALER_CODE,E.DEALER_NAME\n");
			sbSql.append("    FROM   TT_DEALER_ACTUAL_SALES A,\n");
			sbSql.append("           TM_DEALER B,\n");
			sbSql.append("           TM_VEHICLE C,\n");
			sbSql.append("           VW_MATERIAL_GROUP_MAT D,\n");
			sbSql.append("           VW_ORG_DEALER E\n");
			sbSql.append("   WHERE       A.DEALER_ID = B.DEALER_ID\n");
			sbSql.append("           AND A.VEHICLE_ID = C.VEHICLE_ID\n");
			sbSql.append("           AND C.MATERIAL_ID = D.MATERIAL_ID\n");
			sbSql.append("           AND B.DEALER_ID=E.DEALER_ID AND A.IS_RETURN = 10041002\n");
			if(!"".equals(startDate)){
				   sbSql.append("             AND A.SALES_DATE >= TO_DATE('"+startDate+"','yyyy-MM-dd')\n");
			}
			if(!"".equals(endDate)){
				   sbSql.append("             AND A.SALES_DATE <  TO_DATE('"+endDate+"','yyyy-MM-dd')+1\n");
			}
			if(!"".equals(command) && "1".equals(command)){//一级经销商
					sbSql.append("           AND A.DEALER_ID IN (SELECT DEALER_ID FROM TM_DEALER WHERE PARENT_DEALER_D = ? OR DEALER_ID = ?)\n");
					params.add(dealerId);
					params.add(dealerId);
			}
			if(!"".equals(command) && "2".equals(command)){//二级经销商
				sbSql.append("           AND A.DEALER_ID = ?\n");
				params.add(dealerId);
			}
//			sbSql.append(MaterialGroupManagerDao.getDealerBusinessSql("C.YIELDLY", poseId));
			sbSql.append("GROUP BY   E.DEALER_ID,E.DEALER_CODE,E.DEALER_NAME,D.SERIES_NAME,D.SERIES_ID) GROUP BY ROLLUP(DEALER_ID,DEALER_CODE,DEALER_NAME)\n");
			sbSql.append("HAVING (DEALER_ID IS NOT NULL AND DEALER_CODE IS NOT NULL AND DEALER_NAME IS NOT NULL) OR (DEALER_ID IS NULL AND DEALER_CODE IS NULL AND DEALER_NAME IS NULL)\n");
		}
		if("1".equals(command) && "no".equals(sonDealer)){
			sbSql.append("SELECT ");
			for(int i = 0 ; i < seriesList.size() ; i++){
				Map<String, Object> series = seriesList.get(i);
				sbSql.append("   SUM("+series.get("GROUP_NAME")+") "+series.get("GROUP_NAME")+",\n");
				if(i == seriesList.size()-1){
					sum.append("   SUM("+series.get("GROUP_NAME")+")");
				}else{
					sum.append("   SUM("+series.get("GROUP_NAME")+")+ ");
				}
			}
			sbSql.append(sum.toString()+" SUM_COUNT,\n");
			sbSql.append("DEALER_ID,DEALER_CODE,NVL(DEALER_NAME,'合计') DEALER_NAME \n");
			sbSql.append("FROM( SELECT   \n");
			for(int i = 0 ; i < seriesList.size() ; i++){
				Map<String, Object> series = seriesList.get(i);
				sbSql.append("         NVL(DECODE(D.SERIES_ID,"+series.get("GROUP_ID")+",COUNT(A.VEHICLE_ID)),0) "+series.get("GROUP_NAME")+",\n");
			}
			sbSql.append("         E.ROOT_DEALER_ID DEALER_ID,E.ROOT_DEALER_CODE DEALER_CODE,E.ROOT_DEALER_NAME DEALER_NAME \n");
			sbSql.append("    FROM   TT_DEALER_ACTUAL_SALES A,\n");
			sbSql.append("           TM_DEALER B,\n");
			sbSql.append("           TM_VEHICLE C,\n");
			sbSql.append("           VW_MATERIAL_GROUP_MAT D,\n");
			sbSql.append("           VW_ORG_DEALER E\n");
			sbSql.append("   WHERE       A.DEALER_ID = B.DEALER_ID\n");
			sbSql.append("           AND A.VEHICLE_ID = C.VEHICLE_ID\n");
			sbSql.append("           AND C.MATERIAL_ID = D.MATERIAL_ID\n");
			sbSql.append("           AND B.DEALER_ID=E.DEALER_ID AND A.IS_RETURN = 10041002\n");
			sbSql.append("           AND E.ROOT_DEALER_ID = ?\n");
			if(!"".equals(startDate)){
				   sbSql.append("             AND A.SALES_DATE >= TO_DATE('"+startDate+"','yyyy-MM-dd')\n");
			}
			if(!"".equals(endDate)){
				   sbSql.append("             AND A.SALES_DATE <  TO_DATE('"+endDate+"','yyyy-MM-dd')+1\n");
			}
//			if(!"".equals(command) && "1".equals(command)){//一级经销商
//					sbSql.append("           AND A.DEALER_ID IN (SELECT DEALER_ID FROM TM_DEALER WHERE PARENT_DEALER_D = ? OR DEALER_ID = ?)\n");
//					params.add(dealerId);
//					params.add(dealerId);
//			}
//			if(!"".equals(command) && "2".equals(command)){//二级经销商
//				sbSql.append("           AND A.DEALER_ID = ?\n");
//				params.add(dealerId);
//			}
			params.add(dealerId);
//			sbSql.append(MaterialGroupManagerDao.getDealerBusinessSql("C.YIELDLY", poseId));
			sbSql.append("GROUP BY   E.ROOT_DEALER_ID,E.ROOT_DEALER_CODE,E.ROOT_DEALER_NAME,D.SERIES_NAME,D.SERIES_ID) GROUP BY ROLLUP(DEALER_ID,DEALER_CODE,DEALER_NAME)\n");
			sbSql.append("HAVING (DEALER_ID IS NOT NULL AND DEALER_CODE IS NOT NULL AND DEALER_NAME IS NOT NULL ) OR (DEALER_ID IS NULL AND DEALER_CODE IS NULL AND DEALER_NAME IS NULL)\n");
		}
		
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
	public List<Map<String, Object>> getOrgDealerSalesInfo(Map<String, Object> map,AclUserBean logonUser){
		StringBuffer sbSql = new StringBuffer();
		String poseId = map.get("poseId").toString();
		String dealerId = map.get("dealerId").toString();
		String command = map.get("command").toString();
		String startDate = map.get("startDate").toString();
		String endDate = map.get("endDate").toString();
		String sonDealer = map.get("sonDealer").toString();
		List<Map<String, Object>> seriesList = getSeriesList();
			Integer chooseDealer = logonUser.getChooseDealer() == null ? 0 : logonUser.getChooseDealer();
			if(chooseDealer.longValue() == Constant.IF_TYPE_YES.longValue()){
				sbSql.append("WITH AA AS (SELECT DISTINCT DEALER_ID FROM ( SELECT   TPD.DEALER_ID \n"); 
				sbSql.append("  FROM   TR_POSE_DEALER TPD\n"); 
				sbSql.append(" WHERE   TPD.POSE_ID = "+logonUser.getPoseId()+"\n"); 
				sbSql.append("UNION ALL\n"); 
				sbSql.append("SELECT   TMDE.DEALER_ID\n"); 
				sbSql.append("  FROM   TM_DEALER TMDE, TR_POSE_DEALER TPPD\n"); 
				sbSql.append(" WHERE       TMDE.PARENT_DEALER_D = TPPD.DEALER_ID\n"); 
				sbSql.append("         AND TMDE.DEALER_TYPE = 10771001\n"); 
				sbSql.append("         AND TMDE.DEALER_LEVEL = 10851002\n"); 
				sbSql.append("         AND TPPD.POSE_ID = "+logonUser.getPoseId()+"))\n");
			}else{
				sbSql.append("WITH AA AS (SELECT TRD.DEALER_ID FROM TR_POSE_REGION_DEALER  TRD WHERE  TRD.POSE_ID="+logonUser.getPoseId()+")\n");
				
			}
	   StringBuffer sum = new StringBuffer();
		sbSql.append("SELECT ");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("   DECODE(SUM("+series.get("GROUP_NAME")+"),0,'',SUM("+series.get("GROUP_NAME")+")) AS "+series.get("GROUP_NAME")+",\n");
			if(i == seriesList.size()-1){
				sum.append("   SUM("+series.get("GROUP_NAME")+")");
			}else{
				sum.append("   SUM("+series.get("GROUP_NAME")+")+");
			}
		}
		sbSql.append(sum.toString()+"  SUM_COUNT,\n");
		sbSql.append("  ROOT_ORG_NAME,ORG_NAME,DEALER_ID,DEALER_CODE,NVL(DEALER_NAME,'合计') DEALER_NAME\n");
		sbSql.append("FROM( SELECT   \n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("         NVL(DECODE(D.SERIES_ID,"+series.get("GROUP_ID")+",COUNT(A.VEHICLE_ID)),0) "+series.get("GROUP_NAME")+",\n");
		}
		if(!"".equals(sonDealer) && "yes".equals(sonDealer)){
			sbSql.append("         E.ROOT_ORG_NAME,E.ORG_NAME,E.DEALER_ID,E.DEALER_CODE,E.DEALER_NAME\n");
		}
		if(!"".equals(sonDealer) && "no".equals(sonDealer)){
			sbSql.append("         E.ROOT_ORG_NAME,E.ORG_NAME,E.ROOT_DEALER_ID DEALER_ID,E.ROOT_DEALER_CODE DEALER_CODE,E.ROOT_DEALER_NAME DEALER_NAME \n");
		}
		sbSql.append("    FROM   TT_DEALER_ACTUAL_SALES A,\n");
		sbSql.append("           TM_DEALER B,\n");
		sbSql.append("           TM_VEHICLE C,\n");
		sbSql.append("           VW_MATERIAL_GROUP_MAT D,AA AA,\n");
		sbSql.append("           VW_ORG_DEALER E\n");
		sbSql.append("   WHERE       A.DEALER_ID = B.DEALER_ID\n");
		sbSql.append("           AND A.VEHICLE_ID = C.VEHICLE_ID\n");
		sbSql.append("           AND A.DEALER_ID = AA.DEALER_ID\n");
		sbSql.append("           AND C.MATERIAL_ID = D.MATERIAL_ID\n");
		sbSql.append("           AND B.DEALER_ID=E.DEALER_ID AND A.IS_RETURN = 10041002 \n");
		if(!"".equals(dealerId)){

			sbSql.append("          AND A.DEALER_ID IN (SELECT DEALER_ID FROM TM_DEALER WHERE PARENT_DEALER_D IN ("+dealerId+") OR DEALER_ID IN ("+dealerId+"))\n");
		}
		if(!"".equals(command) && "3".equals(command)){
			String orgId = map.get("orgId").toString();
			if(!"".equals(orgId)){
				sbSql.append("            AND E.ORG_ID IN ("+orgId+")\n");
			}
		}
		if(!"".equals(startDate)){
			   sbSql.append("             AND A.SALES_DATE >= TO_DATE('"+startDate+"','yyyy-MM-dd')\n");
		}
		if(!"".equals(endDate)){
			   sbSql.append("             AND A.SALES_DATE <  TO_DATE('"+endDate+"','yyyy-MM-dd')+1\n");
		}
//		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("C.YIELDLY", poseId));
//		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("A.DEALER_ID", logonUser));
//		
		if(!"".equals(sonDealer) && "yes".equals(sonDealer)){
			sbSql.append("GROUP BY  E.ROOT_ORG_NAME,E.ORG_NAME,E.DEALER_ID,E.DEALER_CODE,E.DEALER_NAME,D.SERIES_NAME,D.SERIES_ID) GROUP BY ROLLUP(ROOT_ORG_NAME,ORG_NAME,DEALER_ID,DEALER_CODE,DEALER_NAME)\n");

		}
		if(!"".equals(sonDealer) && "no".equals(sonDealer)){
			sbSql.append("GROUP BY  E.ROOT_ORG_NAME,E.ORG_NAME,E.ROOT_DEALER_ID,E.ROOT_DEALER_CODE,E.ROOT_DEALER_NAME,D.SERIES_NAME,D.SERIES_ID)  GROUP BY ROLLUP(ROOT_ORG_NAME,ORG_NAME,DEALER_ID,DEALER_CODE,DEALER_NAME)\n");

		}
		sbSql.append("HAVING ( ROOT_ORG_NAME IS NOT NULL AND ORG_NAME IS NOT NULL AND DEALER_ID IS NOT NULL AND DEALER_CODE IS NOT NULL AND DEALER_NAME IS NOT NULL )");
		sbSql.append("OR (ROOT_ORG_NAME IS NULL AND ORG_NAME IS NULL AND DEALER_ID IS NULL AND DEALER_CODE IS NULL AND DEALER_NAME IS NULL )");
		sbSql.append("ORDER BY ROOT_ORG_NAME,ORG_NAME,DEALER_NAME\n");
		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}
	
	public List<Map<String, Object>> getDealerSalesCheckInfo(Map<String, Object> map,AclUserBean logonUser){
		String command = (String)map.get("command");
		String dealerId = map.get("dealerId").toString();
		String startDate = map.get("startDate").toString();
		String endDate = map.get("endDate").toString();
		List<Map<String, Object>> seriesList = getSeriesList();
		StringBuffer sbSql = new StringBuffer();
		StringBuffer sum = new StringBuffer();
		/*********************************开票START*************************************/
		Integer chooseDealer = logonUser.getChooseDealer() == null ? 0 : logonUser.getChooseDealer();
		if(chooseDealer.longValue() == Constant.IF_TYPE_YES.longValue()){
			sbSql.append("WITH AA AS (SELECT DISTINCT DEALER_ID ( SELECT   TPD.DEALER_ID \n"); 
			sbSql.append("  FROM   TR_POSE_DEALER TPD\n"); 
			sbSql.append(" WHERE   TPD.POSE_ID = "+logonUser.getPoseId()+"\n"); 
			sbSql.append("UNION ALL\n"); 
			sbSql.append("SELECT   TMDE.DEALER_ID\n"); 
			sbSql.append("  FROM   TM_DEALER TMDE, TR_POSE_DEALER TPPD\n"); 
			sbSql.append(" WHERE       TMDE.PARENT_DEALER_D = TPPD.DEALER_ID\n"); 
			sbSql.append("         AND TMDE.DEALER_TYPE = 10771001\n"); 
			sbSql.append("         AND TMDE.DEALER_LEVEL = 10851002\n"); 
			sbSql.append("         AND TPPD.POSE_ID = "+logonUser.getPoseId()+")),\n");
		}else{
			sbSql.append("WITH AA AS (SELECT TRD.DEALER_ID FROM TR_POSE_REGION_DEALER  TRD WHERE  TRD.POSE_ID="+logonUser.getPoseId()+"),\n");
			
		}
		sbSql.append("D_CHE AS (SELECT \n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("     DECODE(SUM("+series.get("GROUP_NAME")+"),0,'',SUM("+series.get("GROUP_NAME")+")) AS "+series.get("GROUP_NAME")+",\n");
			if(i == seriesList.size()-1){
				sum.append("     SUM("+series.get("GROUP_NAME")+")");
			}else{
				sum.append("     SUM("+series.get("GROUP_NAME")+")+");
			}
		}
		sbSql.append(sum.toString() + "  SUM_COUNT,\n");
		sbSql.append(" ROOT_ORG_NAME,ORG_NAME,NVL(DEALER_ID,-2222222) DEALER_ID,DEALER_CODE,NVL(DEALER_NAME,'合计') DEALER_NAME  FROM ( SELECT ");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("     NVL(DECODE(C.SERIES_ID,"+series.get("GROUP_ID")+",SUM(NVL(B.CHECK_AMOUNT,0))),0) "+series.get("GROUP_NAME")+",\n");
		}
		sbSql.append("      D.ROOT_ORG_NAME,D.ORG_NAME,D.DEALER_ID,D.DEALER_CODE,D.DEALER_NAME\n");
		sbSql.append("      FROM   TT_VS_ORDER A,\n");
		sbSql.append("             TT_VS_ORDER_DETAIL B,AA AA,\n");
		sbSql.append("             VW_MATERIAL_GROUP_MAT C,\n");
		sbSql.append("             VW_ORG_DEALER D\n");
		sbSql.append("     WHERE       A.ORDER_ID = B.ORDER_ID AND A.DEALER_ID = AA.DEALER_ID\n");
		sbSql.append("             AND B.MATERIAL_ID = C.MATERIAL_ID\n");
		sbSql.append("             AND A.DEALER_ID=D.DEALER_ID\n");
		if(!"".equals(dealerId)){
			sbSql.append("           AND A.DEALER_ID IN ("+dealerId+")\n");
		}
//		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("A.DEALER_ID", logonUser));
		sbSql.append("             AND A.ORDER_STATUS IN (10211007,10211008,10211009,10211011)\n");
		sbSql.append("             AND A.INVOICE_NO IS NOT NULL\n");
		sbSql.append("             AND A.INVO_DATE IS NOT NULL\n");
		if(!"".equals(startDate)){
			sbSql.append("             AND A.INVO_DATE >= TO_DATE('"+startDate+"','YYYY-MM-DD')\n");
		}
		if(!"".equals(endDate)){
			sbSql.append("             AND A.INVO_DATE < TO_DATE('"+endDate+"','YYYY-MM-DD')+1 \n");
		}
		if(!"".equals(command) && "3".equals(command)){
			String orgId = map.get("orgId").toString();
			if(!"".equals(orgId)){
				sbSql.append("            AND D.ORG_ID IN ("+orgId+")\n");
			}
		}
		sbSql.append("             GROUP BY D.ROOT_ORG_NAME,D.ORG_NAME,D.DEALER_ID,D.DEALER_CODE,D.DEALER_NAME,C.SERIES_ID,C.SERIES_CODE,C.SERIES_NAME\n");
		sbSql.append("             UNION ALL\n");
		sbSql.append("             -------------------------------\n");
		sbSql.append("     SELECT \n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("     NVL(DECODE(C.SERIES_ID,"+series.get("GROUP_ID")+",SUM(NVL(B.OUT_AMOUNT,0))),0) "+series.get("GROUP_NAME")+",\n");
		}
		sbSql.append("      D.ROOT_ORG_NAME, D.ORG_NAME,D.DEALER_ID,D.DEALER_CODE,D.DEALER_NAME\n");
		sbSql.append("      FROM   TT_OUT_ORDER A,\n");
		sbSql.append("             TT_OUT_ORDER_DETAIL B,AA AA,\n");
		sbSql.append("             VW_MATERIAL_GROUP_MAT C,\n");
		sbSql.append("             VW_ORG_DEALER D\n");
		sbSql.append("     WHERE       A.ORDER_ID = B.ORDER_ID AND A.DEALER_ID = AA.DEALER_ID\n");
		sbSql.append("             AND B.MATERIAL_ID = C.MATERIAL_ID\n");
		sbSql.append("             AND A.DEALER_ID=D.DEALER_ID\n");
		if(!"".equals(dealerId)){
			sbSql.append("           AND A.DEALER_ID IN ("+dealerId+")\n");
		}
//		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("A.DEALER_ID", logonUser));
		sbSql.append("             AND A.ORDER_STATUS = 13741006\n");
		sbSql.append("             AND A.INVOICE_NO IS NOT NULL\n");
		sbSql.append("             AND A.INVO_DATE IS NOT NULL\n");
		if(!"".equals(startDate)){
			sbSql.append("             AND A.INVO_DATE >= TO_DATE('"+startDate+"','YYYY-MM-DD')\n");
		}
		if(!"".equals(endDate)){
			sbSql.append("             AND A.INVO_DATE < TO_DATE('"+endDate+"','YYYY-MM-DD')+1 \n");
		}
		if(!"".equals(command) && "3".equals(command)){
			String orgId = map.get("orgId").toString();
			if(!"".equals(orgId)){
				sbSql.append("            AND D.ORG_ID IN ("+orgId+")\n");
			}
		}
		sbSql.append("             GROUP BY D.ROOT_ORG_NAME,D.ORG_NAME,D.DEALER_ID,D.DEALER_CODE,D.DEALER_NAME,C.SERIES_ID,C.SERIES_CODE,C.SERIES_NAME\n");
		sbSql.append("             -------------------------------\n");
		sbSql.append("             UNION ALL\n");
		sbSql.append("    SELECT ");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("     NVL(DECODE(D.GROUP_ID,"+series.get("GROUP_ID")+",SUM(NVL(CHECK_NUM,0))),0) "+series.get("GROUP_NAME")+",\n");
		}
		sbSql.append("     C.ROOT_ORG_NAME, C.ORG_NAME,C.DEALER_ID,C.DEALER_CODE,C.DEALER_NAME\n");
		sbSql.append("     FROM TT_OLD_ORDER A,VW_ORG_DEALER C,TM_VHCL_MATERIAL_GROUP B,TM_VHCL_MATERIAL_GROUP D,AA AA\n");
		sbSql.append("     WHERE A.DEALER_ID=C.DEALER_ID AND A.MODEL=B.GROUP_CODE AND A.DEALER_ID = AA.DEALER_ID\n");
		if(!"".equals(dealerId)){
			sbSql.append("           AND A.DEALER_ID IN ("+dealerId+")\n");
		}
//		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("A.DEALER_ID", logonUser));
		sbSql.append("      AND A.FLAG IN('0','2','4')\n");
		sbSql.append("      AND B.GROUP_LEVEL=3\n");
		sbSql.append("      AND D.GROUP_LEVEL=2\n");
		sbSql.append("      AND B.PARENT_GROUP_ID=D.GROUP_ID\n");
		if(!"".equals(startDate)){
			sbSql.append("             AND A.INVO_DATE >= TO_DATE('"+startDate+"','YYYY-MM-DD')\n");
		}
		if(!"".equals(endDate)){
			sbSql.append("             AND A.INVO_DATE < TO_DATE('"+endDate+"','YYYY-MM-DD')+1 \n");
		}
		sbSql.append("      AND PROV_ID NOT IN(\n");
		sbSql.append("        '2013080510045491',\n");
		sbSql.append("        '2013082525987270',\n");
		sbSql.append("        '2013082525987271',\n");
		sbSql.append("        '2013082525987272',\n");
		sbSql.append("        '2013082525987273',\n");
		sbSql.append("        '2013082525987274',\n");
		sbSql.append("        '2013082525987275',\n");
		sbSql.append("        '2013082525987276'\n");
		sbSql.append("      )\n");
		if(!"".equals(command) && "3".equals(command)){
			String orgId = map.get("orgId").toString();
			if(!"".equals(orgId)){
				sbSql.append("            AND C.ORG_ID IN ("+orgId+")\n");
			}
		}
		sbSql.append("     GROUP BY  C.ROOT_ORG_NAME,C.ORG_NAME,C.DEALER_ID,C.DEALER_CODE,C.DEALER_NAME,D.GROUP_ID\n");
		sbSql.append(") TT GROUP BY ROLLUP(ROOT_ORG_NAME,ORG_NAME,DEALER_ID,DEALER_CODE,DEALER_NAME)\n");
		sbSql.append("HAVING (ROOT_ORG_NAME IS NOT NULL AND ORG_NAME IS NOT NULL AND DEALER_ID IS NOT NULL AND DEALER_CODE IS NOT NULL AND DEALER_NAME IS NOT NULL )\n");
		sbSql.append("OR (ROOT_ORG_NAME IS NULL AND ORG_NAME IS NULL AND DEALER_ID IS NULL AND DEALER_CODE IS NULL AND DEALER_NAME IS NULL )\n"); 
		sbSql.append("   ORDER BY ROOT_ORG_NAME,ORG_NAME,DEALER_NAME),\n");
		/*********************************开票END*************************************/
		sbSql.append("D_SALES AS (SELECT ");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("   DECODE(SUM("+series.get("GROUP_NAME")+"),0,'',SUM("+series.get("GROUP_NAME")+")) AS "+series.get("GROUP_NAME")+",\n");
		}
		sbSql.append(sum.toString()+"  SUM_COUNT,\n");
		sbSql.append("  ROOT_ORG_NAME,ORG_NAME,NVL(DEALER_ID,-2222222) DEALER_ID,DEALER_CODE,NVL(DEALER_NAME,'合计') DEALER_NAME\n");
		sbSql.append("FROM( SELECT   \n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("         NVL(DECODE(D.SERIES_ID,"+series.get("GROUP_ID")+",COUNT(A.VEHICLE_ID)),0) "+series.get("GROUP_NAME")+",\n");
		}
		sbSql.append("         E.ROOT_ORG_NAME,E.ORG_NAME,E.ROOT_DEALER_ID DEALER_ID,E.ROOT_DEALER_CODE DEALER_CODE,E.ROOT_DEALER_NAME DEALER_NAME \n");
		sbSql.append("    FROM   TT_DEALER_ACTUAL_SALES A,\n");
		sbSql.append("           TM_DEALER B,\n");
		sbSql.append("           TM_VEHICLE C,\n");
		sbSql.append("           VW_MATERIAL_GROUP_MAT D,AA AA,\n");
		sbSql.append("           VW_ORG_DEALER E\n");
		sbSql.append("   WHERE       A.DEALER_ID = B.DEALER_ID\n");
		sbSql.append("           AND A.VEHICLE_ID = C.VEHICLE_ID\n");
		sbSql.append("           AND A.DEALER_ID = AA.DEALER_ID\n");
		sbSql.append("           AND C.MATERIAL_ID = D.MATERIAL_ID\n");
		sbSql.append("           AND B.DEALER_ID=E.DEALER_ID AND A.IS_RETURN = 10041002 \n");
		if(!"".equals(dealerId)){

			sbSql.append("          AND A.DEALER_ID IN (SELECT DEALER_ID FROM TM_DEALER WHERE PARENT_DEALER_D IN ("+dealerId+") OR DEALER_ID IN ("+dealerId+"))\n");
		}
		if(!"".equals(command) && "3".equals(command)){
			String orgId = map.get("orgId").toString();
			if(!"".equals(orgId)){
				sbSql.append("            AND E.ORG_ID IN ("+orgId+")\n");
			}
		}
		if(!"".equals(startDate)){
			   sbSql.append("             AND A.SALES_DATE >= TO_DATE('"+startDate+"','yyyy-MM-dd')\n");
		}
		if(!"".equals(endDate)){
			   sbSql.append("             AND A.SALES_DATE <  TO_DATE('"+endDate+"','yyyy-MM-dd')+1\n");
		}
		sbSql.append("GROUP BY  E.ROOT_ORG_NAME,E.ORG_NAME,E.ROOT_DEALER_ID,E.ROOT_DEALER_CODE,E.ROOT_DEALER_NAME,D.SERIES_NAME,D.SERIES_ID)  GROUP BY ROLLUP(ROOT_ORG_NAME,ORG_NAME,DEALER_ID,DEALER_CODE,DEALER_NAME)\n");
		sbSql.append("HAVING ( ROOT_ORG_NAME IS NOT NULL AND ORG_NAME IS NOT NULL AND DEALER_ID IS NOT NULL AND DEALER_CODE IS NOT NULL AND DEALER_NAME IS NOT NULL )");
		sbSql.append("OR (ROOT_ORG_NAME IS NULL AND ORG_NAME IS NULL AND DEALER_ID IS NULL AND DEALER_CODE IS NULL AND DEALER_NAME IS NULL )");
		sbSql.append("ORDER BY ROOT_ORG_NAME,ORG_NAME,DEALER_NAME)\n");
		sbSql.append("SELECT \n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("        A."+series.get("GROUP_NAME")+" AS "+series.get("GROUP_NAME")+"1,\n");
		}
		sbSql.append("           A.SUM_COUNT SUM_COUNT_1,\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("        B."+series.get("GROUP_NAME")+" AS "+series.get("GROUP_NAME")+"2,\n");
		}
		sbSql.append("           B.SUM_COUNT SUM_COUNT_2,\n");
		sbSql.append("           DLR.DEALER_ID,DLR.DEALER_CODE,DLR.DEALER_NAME,DLR.ORG_NAME,DLR.ROOT_ORG_NAME\n");
		sbSql.append("           FROM D_CHE A,  D_SALES B,\n");
		sbSql.append("              ( SELECT TD.DEALER_ID,TD.DEALER_CODE,TD.DEALER_NAME,VOD.ORG_ID,VOD.ORG_NAME,VOD.ROOT_ORG_NAME\n");
		sbSql.append("                  FROM TM_DEALER TD,\n");
		sbSql.append("                       VW_ORG_DEALER VOD\n");
		sbSql.append("                 WHERE TD.DEALER_ID = VOD.DEALER_ID\n");
		sbSql.append("                   AND TD.DEALER_TYPE =  10771001\n");
		sbSql.append("                   AND TD.DEALER_LEVEL = 10851001\n");
		sbSql.append("                  UNION ALL SELECT -2222222 DEALER_ID,'' DEALER_CODE,'' DEALER_NAME,-222222 ORG_ID,'' ORG_NAME, '' ROOT_ORG_NAME FROM DUAL \n");
		sbSql.append("              ) DLR\n");
		sbSql.append(" WHERE DLR.DEALER_ID = A.DEALER_ID(+)\n");
		if(!"".equals(command) && "3".equals(command)){
			String orgId = map.get("orgId").toString();
			if(!"".equals(orgId)){
				sbSql.append("            AND DLR.ORG_ID IN ("+orgId+")\n");
			}
		}
		sbSql.append("   AND DLR.DEALER_ID = B.DEALER_ID(+)\n"); 
		sbSql.append("ORDER BY DLR.ROOT_ORG_NAME");
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
