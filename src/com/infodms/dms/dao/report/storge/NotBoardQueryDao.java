package com.infodms.dms.dao.report.storge;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class NotBoardQueryDao extends BaseDao<PO>{
	public static final Logger logger = Logger.getLogger(NotBoardQueryDao.class);
	public static NotBoardQueryDao dao = new NotBoardQueryDao();
	public static NotBoardQueryDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Map<String, Object>> getNotBoardInfo(Map<String, Object> map){
		
		List<Map<String, Object>> seriesList = getSeriesList();
		//String startDate = (String)map.get("startDate");
		String endDate = (String)map.get("endDate");
		List<Object> params=new ArrayList<Object>();
		StringBuffer sbSql  =  new StringBuffer();
		sbSql.append("\n");
		sbSql.append("WITH G_D AS\n");
		sbSql.append(" (SELECT D.REGION_ID,\n");
		sbSql.append("         D.REGION_NAME,\n");
		sbSql.append("         B.DEALER_ID,\n");
		sbSql.append("         B.DEALER_NAME,\n");
		sbSql.append("         TO_CHAR(A.ASS_DATE, 'YYYY-MM-DD') ASS_DATE,\n");
		sbSql.append("         SUM(NVL(H.CHECK_AMOUNT, 0) - NVL(H.BOARD_NUMBER, 0)) INNAGE_NUM,\n");
		sbSql.append("         I.SERIES_ID,\n");
		sbSql.append("         I.SERIES_NAME,\n");
		sbSql.append("         A.AREA_ID\n");
		sbSql.append("    FROM TT_SALES_ASSIGN       A,\n");
		sbSql.append("         TM_DEALER             B,\n");
		sbSql.append("         TM_VS_ADDRESS         C,\n");
		sbSql.append("         TM_REGION             D,\n");
		sbSql.append("         TT_VS_ORDER           F,\n");
		sbSql.append("         TT_VS_ORDER_DETAIL    H,\n");
		sbSql.append("         VW_MATERIAL_GROUP_MAT I\n");
		sbSql.append("   WHERE A.REC_DEALER_ID = B.DEALER_ID\n");
		sbSql.append("     AND A.ADDRESS_ID = C.ID\n");
		sbSql.append("     AND C.PROVINCE_ID = D.REGION_CODE\n");
		sbSql.append("     AND A.ORDER_ID = F.ORDER_ID\n");
		sbSql.append("     AND F.ORDER_ID = H.ORDER_ID\n");
		sbSql.append("     AND H.MATERIAL_ID = I.MATERIAL_ID\n");
		sbSql.append("     AND NVL(H.CHECK_AMOUNT, 0) - NVL(H.BOARD_NUMBER, 0) != 0\n");
		//if(!"".equals(startDate)){
			//sbSql.append("  AND A.ASS_DATE >= TO_DATE(?,'YYYY-MM-DD')\n");
			//params.add(startDate);
		//}
		if(endDate!=null && !"".equals(endDate)){
			sbSql.append("   AND A.ASS_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(endDate+":00");
		}
		sbSql.append("     AND A.BO_STATUS != ?\n");
		sbSql.append("   GROUP BY D.REGION_ID,\n");
		sbSql.append("            D.REGION_NAME,\n");
		sbSql.append("            B.DEALER_ID,\n");
		sbSql.append("            B.DEALER_NAME,\n");
		sbSql.append("            TO_CHAR(A.ASS_DATE, 'YYYY-MM-DD'),\n");
		sbSql.append("            I.SERIES_ID,\n");
		sbSql.append("            I.SERIES_NAME,\n");
		sbSql.append("            A.AREA_ID)\n");
		sbSql.append("SELECT * FROM(\n");
		//单一统计
		sbSql.append("SELECT T.REGION_NAME,\n");
		sbSql.append("       T.DEALER_NAME,3 AS ORDER_NUM,--排序字段,\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("SUM(DECODE(T.SERIES_ID,"+series.get("GROUP_ID")+",T.INNAGE_NUM, '')) "+series.get("GROUP_NAME")+",\n");
		}
		sbSql.append("       T.ASS_DATE\n");	
		sbSql.append("  FROM G_D T\n");
		sbSql.append(" GROUP BY T.REGION_NAME, T.DEALER_NAME, T.ASS_DATE\n"); 
		//根据经销商统计
		sbSql.append("UNION ALL\n");
		sbSql.append("--根据经销商小计\n");
		sbSql.append("SELECT '经销商小计',T2.DEALER_NAME,2,\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("SUM(DECODE(T2.SERIES_ID,"+series.get("GROUP_ID")+",T2.INNAGE_NUM, '')) "+series.get("GROUP_NAME")+",\n");
		}
		sbSql.append(" '-1' FROM G_D T2\n");
		sbSql.append(" GROUP BY   T2.REGION_ID,T2.DEALER_ID,T2.DEALER_NAME,T2.REGION_NAME\n"); 
		//根据省份统计
		sbSql.append("UNION ALL\n");
		sbSql.append("--根据省份小计\n");
		sbSql.append("SELECT '省份小计',T3.REGION_NAME,4,\n");
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("SUM(DECODE(T3.SERIES_ID,"+series.get("GROUP_ID")+",T3.INNAGE_NUM, '')) "+series.get("GROUP_NAME")+",\n");
		}
		sbSql.append(" '-1' FROM G_D T3\n");
		sbSql.append("\n");
		sbSql.append(" GROUP BY  T3.REGION_ID,T3.REGION_NAME\n"); 
		//总计
		sbSql.append("UNION ALL SELECT '-1','-1',1,\n"); 
		for(int i = 0 ; i < seriesList.size() ; i++){
			Map<String, Object> series = seriesList.get(i);
			sbSql.append("SUM(DECODE(T1.SERIES_ID,"+series.get("GROUP_ID")+",T1.INNAGE_NUM, '')) "+series.get("GROUP_NAME")+",\n");
		}
		sbSql.append(" '-1' FROM G_D T1)\n"); 
		sbSql.append(" ORDER BY ORDER_NUM,REGION_NAME, DEALER_NAME, ASS_DATE\n"); 
		params.add(Constant.BO_STATUS03);
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
	
	//得到车系列表
	public List<Map<String, Object>> getSeriesList(){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT A.AREA_ID, A.AREA_NAME, C.GROUP_ID, C.GROUP_NAME\n");
		sbSql.append("  FROM TM_BUSINESS_AREA A, TM_AREA_GROUP B, TM_VHCL_MATERIAL_GROUP C\n");
		sbSql.append(" WHERE A.AREA_ID = B.AREA_ID\n");
		sbSql.append("   AND B.MATERIAL_GROUP_ID = C.GROUP_ID\n");
		sbSql.append(" ORDER BY A.AREA_ID, C.GROUP_ID"); 
		List<Object> params=new ArrayList<Object>();
		//params.add(2);//车系第二级
		//params.add(Constant.STATUS_ENABLE);
		return pageQuery(sbSql.toString(), params, getFunName());
	}
	
}
