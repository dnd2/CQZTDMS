package com.infodms.dms.util.vehicle;

import java.util.HashMap;
import java.util.Map;

public class AsSqlUtils {
	
	/**
	 * 获取车厂可用库存数SQL
	 * 
	 * @return
	 */
	public static String getAsSqlVehicleResourceBuffer() {
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("VEHICLE_STOCK AS(\n");
		sbSql.append(" SELECT A.MATERIAL_ID,\n");
		sbSql.append("        C.KEEP_ID,\n");
		sbSql.append("        NVL(A.STOCK_AMOUNT, 0) STOCK_AMOUNT,\n");
		sbSql.append("        NVL(B.RESAVE_AMOUNT, 0) RESAVE_AMOUNT,\n");
		sbSql.append("        NVL(C.LOCK_AMOUNT,0) LOCK_AMOUNT,\n");
		sbSql.append("        NVL(A.STOCK_AMOUNT, 0) - NVL(B.RESAVE_AMOUNT, 0) - NVL(C.LOCK_AMOUNT,0) VEHICLE_AMOUNT\n");
		sbSql.append("   FROM (SELECT TV.MATERIAL_ID, COUNT(1) STOCK_AMOUNT\n");
		sbSql.append("           FROM TM_VEHICLE TV\n");
		sbSql.append("          WHERE TV.LIFE_CYCLE = 10321002\n");
		sbSql.append("            AND TV.LOCK_STATUS = 10241001\n");
		sbSql.append("            AND TV.WAREHOUSE_ID NOT IN ('8814','8833') \n");
		sbSql.append("          GROUP BY TV.MATERIAL_ID) A,\n");
		sbSql.append("        (SELECT TVORR.MATERIAL_ID,\n");
		sbSql.append("                SUM(NVL(TVORR.AMOUNT, 0) - NVL(TVORR.ALLOCA_NUM, 0)) RESAVE_AMOUNT\n");
		sbSql.append("           FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR WHERE TVORR.WAREHOUSE_CODE NOT IN ('8814','8833')\n");
		sbSql.append("          GROUP BY TVORR.MATERIAL_ID) B,\n");
		sbSql.append("		  (SELECT TCRK.MATERIAL_ID,TCRK.KEEP_ID,\n");
		sbSql.append("                SUM(NVL(TCRK.NUM, 0)) LOCK_AMOUNT\n");
		sbSql.append("           FROM TT_COMP_RES_KEEP TCRK\n");
		sbSql.append("          WHERE TCRK.NUM <> 0 AND WAREHOUSE_CODE NOT IN ('8814','8833')\n");
		sbSql.append("          GROUP BY TCRK.MATERIAL_ID,TCRK.KEEP_ID) C"); 
		sbSql.append("  WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)\n");
		sbSql.append("    AND A.MATERIAL_ID = C.MATERIAL_ID(+)\n");
		sbSql.append(")\n"); 

		return sbSql.toString();
	}
	
	
	/**
	 * 获取车厂可用库存数SQL
	 * 
	 * @return
	 */
	public static String getAsSqlVehicleResourceBuffer(String materialId) {
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append(" SELECT NVL(A.STOCK_AMOUNT, 0) - NVL(B.RESAVE_AMOUNT, 0) - NVL(C.LOCK_AMOUNT,0) VEHICLE_AMOUNT\n");
		sbSql.append("   FROM (SELECT TV.MATERIAL_ID, COUNT(1) STOCK_AMOUNT\n");
		sbSql.append("           FROM TM_VEHICLE TV\n");
		sbSql.append("          WHERE TV.LIFE_CYCLE = 10321002\n");
		sbSql.append("            AND TV.LOCK_STATUS = 10241001\n");
		sbSql.append("          GROUP BY TV.MATERIAL_ID) A,\n");
		sbSql.append("        (SELECT TVORR.MATERIAL_ID,\n");
		sbSql.append("                SUM(NVL(TVORR.AMOUNT, 0) - NVL(TVORR.ALLOCA_NUM, 0)) RESAVE_AMOUNT\n");
		sbSql.append("           FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR\n");
		sbSql.append("          GROUP BY TVORR.MATERIAL_ID) B,\n");
		sbSql.append("		  (SELECT TCRK.MATERIAL_ID,TCRK.KEEP_ID,\n");
		sbSql.append("                SUM(NVL(TCRK.NUM, 0)) LOCK_AMOUNT\n");
		sbSql.append("           FROM TT_COMP_RES_KEEP TCRK\n");
		sbSql.append("          WHERE TCRK.NUM <> 0\n");
		sbSql.append("          GROUP BY TCRK.MATERIAL_ID,TCRK.KEEP_ID) C"); 
		sbSql.append("  WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)\n");
		sbSql.append("    AND A.MATERIAL_ID = C.MATERIAL_ID(+)\n");
		if(materialId != null && !"".equals(materialId)) {
			sbSql.append(" AND A.MATERIAL_ID = ?");
		}

		return sbSql.toString();
	}
	
	/**
	 * 获取车厂可用库存数SQL
	 * 
	 * @return
	 */
	public static String getAsSqlVehicleResourceByZydBuffer() {
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("VEHICLE_STOCK AS(\n");
		sbSql.append(" SELECT A.MATERIAL_ID,\n");
		sbSql.append("        C.KEEP_ID,\n");
		sbSql.append("        NVL(A.STOCK_AMOUNT, 0) STOCK_AMOUNT,\n");
		sbSql.append("        NVL(B.RESAVE_AMOUNT, 0) RESAVE_AMOUNT,\n");
		sbSql.append("        NVL(C.LOCK_AMOUNT,0) LOCK_AMOUNT,\n");
		sbSql.append("        NVL(A.STOCK_AMOUNT, 0) - NVL(B.RESAVE_AMOUNT, 0) - NVL(C.LOCK_AMOUNT,0) VEHICLE_AMOUNT\n");
		sbSql.append("   FROM (SELECT TV.MATERIAL_ID, COUNT(1) STOCK_AMOUNT\n");
		sbSql.append("           FROM TM_VEHICLE TV\n");
		sbSql.append("          WHERE TV.LIFE_CYCLE = 10321002\n");
		sbSql.append("            AND TV.LOCK_STATUS = 10241001\n");
		sbSql.append("            AND TV.WAREHOUSE_ID = '8814'\n");
		sbSql.append("          GROUP BY TV.MATERIAL_ID) A,\n");
		sbSql.append("        (SELECT TVORR.MATERIAL_ID,\n");
		sbSql.append("                SUM(NVL(TVORR.AMOUNT, 0) - NVL(TVORR.ALLOCA_NUM, 0)) RESAVE_AMOUNT\n");
		sbSql.append("           FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR WHERE TVORR.WAREHOUSE_CODE = '8814'\n");
		sbSql.append("          GROUP BY TVORR.MATERIAL_ID) B,\n");
		sbSql.append("		  (SELECT TCRK.MATERIAL_ID,TCRK.KEEP_ID,\n");
		sbSql.append("                SUM(NVL(TCRK.NUM, 0)) LOCK_AMOUNT\n");
		sbSql.append("           FROM TT_COMP_RES_KEEP TCRK\n");
		sbSql.append("          WHERE TCRK.NUM <> 0 AND WAREHOUSE_CODE = '8814'\n");
		sbSql.append("          GROUP BY TCRK.MATERIAL_ID,TCRK.KEEP_ID) C"); 
		sbSql.append("  WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)\n");
		sbSql.append("    AND A.MATERIAL_ID = C.MATERIAL_ID(+)\n");
		sbSql.append(")\n"); 

		return sbSql.toString();
	}
	
	/**
	 * 获取车厂可用库存数SQL
	 * 
	 * @return
	 */
	public static String getAsSqlVehicleResourceByZydBuffer(String warehouseCode) {
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("VEHICLE_STOCK AS(\n");
		sbSql.append(" SELECT A.MATERIAL_ID,\n");
		sbSql.append("        C.KEEP_ID,\n");
		sbSql.append("        NVL(A.STOCK_AMOUNT, 0) STOCK_AMOUNT,\n");
		sbSql.append("        NVL(B.RESAVE_AMOUNT, 0) RESAVE_AMOUNT,\n");
		sbSql.append("        NVL(C.LOCK_AMOUNT,0) LOCK_AMOUNT,\n");
		sbSql.append("        NVL(A.STOCK_AMOUNT, 0) - NVL(B.RESAVE_AMOUNT, 0) - NVL(C.LOCK_AMOUNT,0) VEHICLE_AMOUNT\n");
		sbSql.append("   FROM (SELECT TV.MATERIAL_ID, COUNT(1) STOCK_AMOUNT\n");
		sbSql.append("           FROM TM_VEHICLE TV\n");
		sbSql.append("          WHERE TV.LIFE_CYCLE = 10321002\n");
		sbSql.append("            AND TV.LOCK_STATUS = 10241001\n");
		//增加其余中转库（非直营店和退回车库统一纳入幻速商品车库）
		if(warehouseCode!=null && "8809".equals(warehouseCode)){
			sbSql.append("            AND TV.WAREHOUSE_ID NOT IN ('8814','8833') \n");
		}else{
			sbSql.append("            AND TV.WAREHOUSE_ID = '"+warehouseCode+"'\n");
		}
		
		sbSql.append("          GROUP BY TV.MATERIAL_ID) A,\n");
		sbSql.append("        (SELECT TVORR.MATERIAL_ID,\n");
		sbSql.append("                SUM(NVL(TVORR.AMOUNT, 0) - NVL(TVORR.ALLOCA_NUM, 0)) RESAVE_AMOUNT\n");
		//增加其余中转库（非直营店和退回车库统一纳入幻速商品车库）
		if(warehouseCode!=null && "8809".equals(warehouseCode)){
			sbSql.append("           FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR WHERE TVORR.WAREHOUSE_CODE NOT IN ('8814','8833') \n");
		}else{
			sbSql.append("           FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR WHERE TVORR.WAREHOUSE_CODE = '"+warehouseCode+"'\n");
		}
		//sbSql.append("           FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR WHERE TVORR.WAREHOUSE_CODE = '"+warehouseCode+"'\n");
		sbSql.append("          GROUP BY TVORR.MATERIAL_ID) B,\n");
		sbSql.append("		  (SELECT TCRK.MATERIAL_ID,TCRK.KEEP_ID,\n");
		sbSql.append("                SUM(NVL(TCRK.NUM, 0)) LOCK_AMOUNT\n");
		sbSql.append("           FROM TT_COMP_RES_KEEP TCRK\n");
		sbSql.append("          WHERE TCRK.NUM <> 0 AND WAREHOUSE_CODE = '"+warehouseCode+"'\n");
		sbSql.append("          GROUP BY TCRK.MATERIAL_ID,TCRK.KEEP_ID) C"); 
		sbSql.append("  WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)\n");
		sbSql.append("    AND A.MATERIAL_ID = C.MATERIAL_ID(+)\n");
		sbSql.append(")\n"); 

		return sbSql.toString();
	}
	
	/**
	 * 根据经销商加载选配项条件限制
	 * @param dealerid
	 * @param packageId
	 * @return
	 */
	public static String getDealerPackageXpBuffer(String tabAlias,String dealerid){
		StringBuffer sbSql = new StringBuffer();
		if(null != tabAlias && !"".equals(tabAlias)){
			sbSql.append(" AND ").append(tabAlias).append(".DETAIL_ID IN(SELECT M.XP_DETAIL_ID FROM TT_SALES_XP_LIMIT M \n");
		}else
			sbSql.append(" AND DETAIL_ID IN(SELECT M.XP_DETAIL_ID FROM TT_SALES_XP_LIMIT M \n");
		sbSql.append(" WHERE M.DEALER_ID= ").append(dealerid).append("\n");
		sbSql.append(" AND M.STATUS=10011001)\n");
		return sbSql.toString();
	}
	
}
