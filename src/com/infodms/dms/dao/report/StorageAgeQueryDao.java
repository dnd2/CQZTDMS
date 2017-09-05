package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class StorageAgeQueryDao extends BaseDao<PO>{
	public static final Logger logger = Logger.getLogger(BillDetailTicketDao.class);
	public static StorageAgeQueryDao dao = new StorageAgeQueryDao();
	public static StorageAgeQueryDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
public List<Map<String, Object>> getStorageAgeReportInfo(Map<String, Object> map,Integer pageSize,Integer curPage){
	//经销商库存，配车锁定，在途，经销商在库
	//车厂库存,车厂库存，除开配车锁定
	String areaId = (String)map.get("areaId");
	List<Object> par=new ArrayList<Object>();
	StringBuffer sbSql=new StringBuffer();
	sbSql.append("WITH A AS ( SELECT SERIES_ID, \n"); 
	sbSql.append("                  -- YIELDLY,\n"); 
	sbSql.append("                   SUM(DECODE(STORAGE_AGE,'D3',AMOUNT)) STORAGE_AMOUNT_1,\n"); 
	sbSql.append("                   SUM(DECODE(STORAGE_AGE,'D3-6',AMOUNT)) STORAGE_AMOUNT_2,\n"); 
	sbSql.append("                   SUM(DECODE(STORAGE_AGE,'D6',AMOUNT)) STORAGE_AMOUNT_3,\n"); 
	sbSql.append("                   SUM(AMOUNT) TOTAL_STORAGE\n"); 
	sbSql.append("              FROM (\n"); 
	sbSql.append("            SELECT TV.SERIES_ID,\n"); 
	sbSql.append("                  --DECODE(TV.YIELDLY,2010010100000003,'商用车','乘用车') YIELDLY,\n"); 
	sbSql.append("                   CASE WHEN TV.STORAGE_DATE > ADD_MONTHS(SYSDATE,-3) THEN 'D3'\n"); 
	sbSql.append("                        WHEN TV.STORAGE_DATE < ADD_MONTHS(SYSDATE,-6) THEN 'D6'\n"); 
	sbSql.append("                        ELSE 'D3-6' END STORAGE_AGE,\n"); 
	sbSql.append("                   1 AMOUNT\n"); 
	sbSql.append("             FROM TM_VEHICLE TV\n"); 
	sbSql.append("            WHERE ((TV.LIFE_CYCLE = 10321002 AND TV.LOCK_STATUS=10241008) OR (TV.LIFE_CYCLE = 10321005 AND TV.LOCK_STATUS=10241001) OR (TV.LIFE_CYCLE = 10321003))--经销商库存\n");
	sbSql.append("            		AND TV.PROV_ID NOT IN (2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
	sbSql.append("            AND DEALER_ID>0\n");//没有挂上经销商的库存不纳入统计
	if(!"".equals(areaId)){
		sbSql.append(" AND TV.YIELDLY=?\n");
		par.add(areaId);
	}
	sbSql.append(" )            GROUP BY SERIES_ID ) ,    \n"); 
	sbSql.append("    B AS ( SELECT SERIES_ID,\n"); 
	sbSql.append("               --   YIELDLY,\n"); 
	sbSql.append("                   SUM(DECODE(OEM_STORAGE_AGE,'O3',AMOUNT)) OEM_STORAGE_AGE_1,\n"); 
	sbSql.append("                   SUM(DECODE(OEM_STORAGE_AGE,'O3-6',AMOUNT)) OEM_STORAGE_AGE_2,\n"); 
	sbSql.append("                   SUM(DECODE(OEM_STORAGE_AGE,'O6',AMOUNT)) OEM_STORAGE_AGE_3,\n"); 
	sbSql.append("                   SUM(AMOUNT) TOTAL_OEM_STORAGE FROM\n"); 
	sbSql.append("           ( SELECT TV.SERIES_ID,\n"); 
	sbSql.append("                   CASE WHEN TV.ORG_STORAGE_DATE > ADD_MONTHS(SYSDATE,-3) THEN 'O3'\n"); 
	sbSql.append("                   WHEN TV.ORG_STORAGE_DATE < ADD_MONTHS(SYSDATE,-6) THEN 'O6'\n"); 
	sbSql.append("                   ELSE 'O3-6' END OEM_STORAGE_AGE,\n"); 
	sbSql.append("                   1 AMOUNT\n"); 
	sbSql.append("              FROM TM_VEHICLE TV\n"); 
	sbSql.append("             WHERE TV.LIFE_CYCLE = 10321002 AND TV.LOCK_STATUS<>10241008\n"); 
	sbSql.append("            		AND TV.PROV_ID NOT IN (2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
	if(!"".equals(areaId)){
		sbSql.append(" AND TV.YIELDLY=?\n");
		par.add(areaId);
	}
	sbSql.append("           )  GROUP BY SERIES_ID--车厂库存\n"); 
	sbSql.append("    ),\n"); 
	sbSql.append("C AS (SELECT  VMG.GROUP_ID,VMG.GROUP_NAME,\n"); 
	sbSql.append("        DECODE(TAG.AREA_ID,2010010100000003,'商用车','乘用车') YIELDLY,\n"); 
	sbSql.append("        A.STORAGE_AMOUNT_1 STORAGE_1,\n"); 
	sbSql.append("        A.STORAGE_AMOUNT_2 STORAGE_2,\n"); 
	sbSql.append("        A.STORAGE_AMOUNT_3 STORAGE_3,\n"); 
	sbSql.append("        A.TOTAL_STORAGE TOTAL_STORAGE,\n"); 
	sbSql.append("        B.OEM_STORAGE_AGE_1 OEM_STORAGE_1,\n"); 
	sbSql.append("        B.OEM_STORAGE_AGE_2 OEM_STORAGE_2,\n"); 
	sbSql.append("        B.OEM_STORAGE_AGE_3 OEM_STORAGE_3,\n"); 
	sbSql.append("        B.TOTAL_OEM_STORAGE TOTAL_OEM_STORAGE,\n"); 
	sbSql.append("        NVL(A.TOTAL_STORAGE,0) +  NVL(B.TOTAL_OEM_STORAGE,0) TOTAL\n"); 
	sbSql.append("  FROM  A , B ,  TM_VHCL_MATERIAL_GROUP VMG , TM_AREA_GROUP TAG\n"); 
	sbSql.append(" WHERE  VMG.GROUP_ID = A.SERIES_ID(+)\n"); 
	sbSql.append("   AND  VMG.GROUP_ID = B.SERIES_ID(+)\n"); 
	sbSql.append("   AND  VMG.GROUP_LEVEL = 2\n"); 
	sbSql.append("   AND  VMG.STATUS = 10011001\n"); 
	sbSql.append("   AND  VMG.GROUP_ID = TAG.MATERIAL_GROUP_ID)\n"); 
	sbSql.append("  --SELECT * FROM C\n"); 
	sbSql.append("   SELECT MAX(C.GROUP_ID) GROUP_ID,C.GROUP_NAME,\n"); 
	sbSql.append("          C.YIELDLY,\n"); 
	sbSql.append("          NVL(SUM(STORAGE_1),0) STORAGE_1,\n"); 
	sbSql.append("          NVL(SUM(STORAGE_2),0) STORAGE_2,\n"); 
	sbSql.append("          NVL(SUM(STORAGE_3),0) STORAGE_3,\n"); 
	sbSql.append("          NVL(SUM(TOTAL_STORAGE),0) TOTAL_STORAGE,\n"); 
	sbSql.append("          NVL(SUM(OEM_STORAGE_1),0) OEM_STORAGE_1,\n"); 
	sbSql.append("          NVL(SUM(OEM_STORAGE_2),0) OEM_STORAGE_2,\n"); 
	sbSql.append("          NVL(SUM(OEM_STORAGE_3),0) OEM_STORAGE_3,\n"); 
	sbSql.append("          NVL(SUM(TOTAL_OEM_STORAGE),0) TOTAL_OEM_STORAGE,\n"); 
	sbSql.append("          NVL(SUM(TOTAL),0) TOTAL\n"); 
	sbSql.append("     FROM C\n"); 
	sbSql.append("    GROUP BY ROLLUP(GROUP_NAME),ROLLUP(YIELDLY)\n"); 
	sbSql.append("    ORDER BY YIELDLY,GROUP_NAME\n");

	return pageQuery(sbSql.toString(), par, getFunName());
}
/**
 * 查看详细明细
 * 
 * @param map
 * @param curPage
 * @param pageSize
 * @return
 * @throws Exception
 */
public PageResult<Map<String, Object>> getStorageAgeReportInfoMsg(Map<String, Object> map, int curPage, int pageSize) throws Exception
{
	//String areaId = (String) map.get("areaId");
	//String dealerId = (String) map.get("dealerId");
	//String haveCon = (String) map.get("haveCon");
	//String type = (String) map.get("type");
	String serName = (String) map.get("serName");
	String kcType = (String) map.get("kcType");
	String daylen = (String) map.get("daylen");
	String vin = (String) map.get("vin");
	List<Object> param=new ArrayList<Object>();
	StringBuffer sbSql=new StringBuffer();
	if(!"".equals(kcType)){
		if("QYKC".equals(kcType)){//企业库存
			sbSql.append("--企业库存\n");
			sbSql.append("SELECT TV.VIN,\n");
			sbSql.append("       S.SERIES_NAME,\n");
			sbSql.append("       S.MODEL_CODE,\n");
			sbSql.append("       S.MODEL_NAME,\n");
			sbSql.append("       S.MATERIAL_CODE,\n");
			sbSql.append("       S.MATERIAL_NAME,\n");
			sbSql.append("       A.SIT_NAME,\n");
			sbSql.append("       B.ROAD_NAME,\n");
			sbSql.append("       C.AREA_NAME,\n");
			sbSql.append("       TO_CHAR(TV.STORAGE_DATE,'YYYY-MM-DD') STORAGE_DATE,\n");
			sbSql.append("       TO_CHAR(TV.PRODUCT_DATE,'YYYY-MM-DD') PRODUCT_DATE,\n");
			sbSql.append("       TO_CHAR(TV.ORG_STORAGE_DATE,'YYYY-MM-DD')ORG_STORAGE_DATE\n");
			sbSql.append("  FROM TM_VEHICLE            TV,\n");
			sbSql.append("       VW_MATERIAL_GROUP_MAT S,\n");
			sbSql.append("       TT_SALES_SIT          A,\n");
			sbSql.append("       TT_SALES_ROAD         B,\n");
			sbSql.append("       TT_SALES_AREA         C\n");
			sbSql.append(" WHERE TV.MATERIAL_ID = S.MATERIAL_ID\n");
			sbSql.append("   AND TV.SIT_ID = A.SIT_ID(+)\n");
			sbSql.append("   AND A.ROAD_ID = B.ROAD_ID(+)\n");
			sbSql.append("   AND B.AREA_ID = C.AREA_ID(+)\n");
			sbSql.append("   AND TV.LIFE_CYCLE = 10321002\n");
			sbSql.append("   AND TV.LOCK_STATUS <> 10241008\n");
			sbSql.append("   AND TV.PROV_ID NOT IN (2013080510045491,\n");
			sbSql.append("                          2013082525987270,\n");
			sbSql.append("                          2013082525987271,\n");
			sbSql.append("                          2013082525987272,\n");
			sbSql.append("                          2013082525987273,\n");
			sbSql.append("                          2013082525987274,\n");
			sbSql.append("                          2013082525987275,\n");
			sbSql.append("                          2013082525987276,\n");
			sbSql.append("                          2013082956190007)\n");
			if(!"".equals(serName)){
				sbSql.append("   AND S.SERIES_ID = ?\n");
				param.add(serName);
			}
			if(!"".equals(daylen)){
				if("1".equals(daylen))
					sbSql.append("   AND TV.ORG_STORAGE_DATE > ADD_MONTHS(SYSDATE, -3)"); 
				if("2".equals(daylen))
					sbSql.append("   AND TV.ORG_STORAGE_DATE BETWEEN ADD_MONTHS(SYSDATE, -6) AND  ADD_MONTHS(SYSDATE, -3)"); 
				if("3".equals(daylen))
					sbSql.append(" AND  TV.ORG_STORAGE_DATE < ADD_MONTHS(SYSDATE, -6)"); 

			}
			if(!"".equals(vin)){
				sbSql.append("   AND TV.VIN = ?\n");
				param.add(vin);
			}
			sbSql.append("   ORDER BY TV.ORG_STORAGE_DATE\n");

		}else{//经销商库存
			sbSql.append("--经销商库存\n");
			sbSql.append("SELECT TV.VIN,\n");
			sbSql.append("       S.SERIES_NAME,\n");
			sbSql.append("       S.MODEL_CODE,\n");
			sbSql.append("       S.MODEL_NAME,\n");
			sbSql.append("       S.MATERIAL_CODE,\n");
			sbSql.append("       S.MATERIAL_NAME,\n");
			sbSql.append("       TO_CHAR(TV.STORAGE_DATE,'YYYY-MM-DD') STORAGE_DATE,\n");
			sbSql.append("       TO_CHAR(TV.PRODUCT_DATE,'YYYY-MM-DD') PRODUCT_DATE,\n");
			sbSql.append("       TO_CHAR(TV.ORG_STORAGE_DATE,'YYYY-MM-DD')ORG_STORAGE_DATE\n");
			sbSql.append("  FROM TM_VEHICLE            TV,\n");
			sbSql.append("       VW_MATERIAL_GROUP_MAT S\n");
			sbSql.append(" WHERE TV.MATERIAL_ID = S.MATERIAL_ID\n");
			sbSql.append("   AND ((TV.LIFE_CYCLE = 10321002 AND TV.LOCK_STATUS = 10241008)\n");
			sbSql.append("              OR (TV.LIFE_CYCLE = 10321005 AND TV.LOCK_STATUS = 10241001)\n");
			sbSql.append("              OR (TV.LIFE_CYCLE = 10321003)) --经销商库存\n");
			sbSql.append("   AND TV.PROV_ID NOT IN (2013080510045491,\n");
			sbSql.append("                          2013082525987270,\n");
			sbSql.append("                          2013082525987271,\n");
			sbSql.append("                          2013082525987272,\n");
			sbSql.append("                          2013082525987273,\n");
			sbSql.append("                          2013082525987274,\n");
			sbSql.append("                          2013082525987275,\n");
			sbSql.append("                          2013082525987276,\n");
			sbSql.append("                          2013082956190007)\n");
			sbSql.append("   AND DEALER_ID > 0\n");
			if(!"".equals(serName)){
				sbSql.append("   AND S.SERIES_ID = ?\n");
				param.add(serName);
			}
			if(!"".equals(daylen)){
				if("1".equals(daylen))
					sbSql.append("   AND TV.STORAGE_DATE > ADD_MONTHS(SYSDATE, -3)"); 
				if("2".equals(daylen))
					sbSql.append("   AND TV.STORAGE_DATE BETWEEN ADD_MONTHS(SYSDATE, -6) AND  ADD_MONTHS(SYSDATE, -3)"); 
				if("3".equals(daylen))
					sbSql.append("  AND TV.STORAGE_DATE < ADD_MONTHS(SYSDATE, -6)"); 

			} 
			if(!"".equals(vin)){
				sbSql.append("   AND TV.VIN = ?\n");
				param.add(vin);
			}
			sbSql.append("   ORDER BY TV.ORG_STORAGE_DATE\n");
		}
	
	}
	PageResult<Map<String, Object>> ps= dao.pageQuery(sbSql.toString(),param, getFunName(),pageSize,curPage);
	return ps;
}
}
