package com.infodms.dms.dao.sales.ordermanage.carSubmission;

import com.infodms.dms.common.Constant;

/**
 * <ul>
 * <li>文件名称: ResourceSqlDao.java</li>
 * <li>文件描述:</li>
 * <li>版权所有: 版权所有(C)2012-2013</li>
 * <li>内容摘要:</li>
 * <li>完成日期: 2013-7-26 下午12:05:07</li>
 * <li>修改记录:</li>
 * </ul>
 * 
 * @version 1.0
 * @author wangsongwei
 */
public class ResourceSqlDao {
	private static final ResourceSqlDao dao = new ResourceSqlDao();
	
	public static final ResourceSqlDao getInstance() {
		return dao;
	}
	private ResourceSqlDao() {}
	
	/**
	 * 方法描述 ： 获取资源查询SQL - 所有车辆资源<br/>
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public static String getAsSqlResourceAllQuery()
	{
		StringBuffer sbSql = new StringBuffer();
		
		// -- ①资源查询，不分开站点
		sbSql.append("VEHICLE_STOCK_ALL AS (");
		sbSql.append("SELECT A.MATERIAL_ID,\n");
		sbSql.append("       A.STOCK_AMOUNT,\n");
		sbSql.append("       NVL(B.RESERVE_AMOUNT, 0) ORDER_RESERVE_AMOUNT,\n");
		sbSql.append("       NVL(C.NUM, 0) LOCK_AMOUNT,\n");
		sbSql.append("       A.STOCK_AMOUNT - NVL(B.RESERVE_AMOUNT, 0) - NVL(C.NUM, 0) RESOURCE_AMOUNT\n");
		sbSql.append("  FROM \n");
		sbSql.append("		 (" + getSqlStockAllQuery() + ") A,\n");
		sbSql.append("       (" + getSqlResourceResaveQuery() + ") B,\n");
		sbSql.append("       (" + getSqlCompStockQuery() + ") C\n");
		sbSql.append(" WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)\n");
		sbSql.append("   AND A.MATERIAL_ID = C.MATERIAL_ID(+)\n");
		sbSql.append(")");
		
		return sbSql.toString();
	}
	
	/**
	 * 方法描述 ： 获取资源查询SQL - 所有车辆资源,按站点分组<br/>
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public static String getAsSqlOrgResourceQuery()
	{
		StringBuffer sbSql = new StringBuffer();
		
		// -- ②资源查询，需要分开站点
		sbSql.append("VEHICLE_STOCK_ALL AS (");
		sbSql.append("SELECT MAT.MATERIAL_ID,\n");
		sbSql.append("       C.KEEP_ID,\n");
		sbSql.append("       MAT.ORG_ID,\n");
		sbSql.append("       MAT.REGION_NAME,\n");
		sbSql.append("       A.STOCK_AMOUNT,\n");
		sbSql.append("       NVL(B.RESERVE_AMOUNT, 0) ORDER_RESERVE_AMOUNT,\n");
		sbSql.append("       NVL(C.NUM, 0) LOCK_AMOUNT,\n");
		sbSql.append("       NVL(A.STOCK_AMOUNT,0) - NVL(B.RESERVE_AMOUNT, 0) - NVL(C.NUM, 0) RESOURCE_AMOUNT\n");
		sbSql.append("  FROM (SELECT A1.MATERIAL_ID, A1.MATERIAL_CODE,\n");
		sbSql.append("               -2222222222 AS ORG_ID,\n");
		sbSql.append("               '销售部' AS REGION_NAME\n");
		sbSql.append("          FROM TM_VHCL_MATERIAL A1\n");
		sbSql.append("        UNION ALL\n");
		sbSql.append("        SELECT A1.MATERIAL_ID,\n");
		sbSql.append("               A1.MATERIAL_CODE,\n");
		sbSql.append("               A2.ORG_ID,\n");
		sbSql.append("               A2.ORG_NAME AS REGION_NAME\n");
		sbSql.append("          FROM TM_VHCL_MATERIAL A1, TM_ORG A2\n");
		sbSql.append("         WHERE A2.ORG_LEVEL = 3\n");
		sbSql.append("           AND A2.STATUS = " + Constant.STATUS_ENABLE + ") MAT\n");
		sbSql.append("  LEFT JOIN (SELECT TV.MATERIAL_ID,\n");
		sbSql.append("                    TPD.ORG_ID,\n");
		sbSql.append("                    '销售部' REGION_NAME,\n");
		sbSql.append("                    COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("               FROM TM_VEHICLE TV, TM_PLAN_DETAIL TPD\n");
		sbSql.append("              WHERE TV.PLAN_DETAIL_ID = TPD.PLAN_DETAIL_ID\n");
		sbSql.append("                AND TV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_02 + " --车厂库存（生命周期 - TC_CODE）\n");
		sbSql.append("                AND TV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + " --正常状态（锁定状态 - TC_CODE）\n");
		sbSql.append("                AND TPD.ORG_ID = -2222222222\n");
		sbSql.append("              GROUP BY TV.MATERIAL_ID, TPD.ORG_ID\n");
		sbSql.append("             UNION ALL\n");
		sbSql.append("             SELECT TV.MATERIAL_ID,\n");
		sbSql.append("                    TORG.ORG_ID,\n");
		sbSql.append("                    TORG.ORG_NAME REGION_NAME,\n");
		sbSql.append("                    COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("               FROM TM_VEHICLE TV, TM_PLAN_DETAIL TPD, TM_ORG TORG\n");
		sbSql.append("              WHERE TV.PLAN_DETAIL_ID = TPD.PLAN_DETAIL_ID\n");
		sbSql.append("                AND TPD.ORG_ID = TORG.ORG_ID\n");
		sbSql.append("                AND TV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_02 + " --车厂库存（生命周期 - TC_CODE）\n");
		sbSql.append("                AND TV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + " --正常状态（锁定状态 - TC_CODE）\n");
		sbSql.append("                AND TPD.ORG_ID <> -2222222222\n");
		sbSql.append("              GROUP BY TV.MATERIAL_ID, TORG.ORG_ID, TORG.ORG_NAME) A\n");
		sbSql.append("         ON MAT.MATERIAL_ID =  A.MATERIAL_ID  AND MAT.ORG_ID = A.ORG_ID\n");
		sbSql.append("  LEFT JOIN (SELECT MATERIAL_ID,\n");
		sbSql.append("                    ORG_ID,\n");
		sbSql.append("                    SUM(NVL(AMOUNT, 0) - NVL(ALLOCA_NUM, 0)) RESERVE_AMOUNT --保留数量 = 预留 - 已配车\n");
		sbSql.append("               FROM TT_VS_ORDER_RESOURCE_RESERVE\n");
		sbSql.append("              WHERE RESERVE_STATUS = " + Constant.RESOURCE_RESERVE_STATUS_01 + " --保留\n");
		sbSql.append("              GROUP BY MATERIAL_ID, ORG_ID) B\n");
		sbSql.append("         ON MAT.MATERIAL_ID = B.MATERIAL_ID AND MAT.ORG_ID = B.ORG_ID\n");
		sbSql.append("  FULL JOIN (SELECT TCRK.MATERIAL_ID,\n");
		sbSql.append("                    TCRK.ORG_ID,\n");
		sbSql.append("                    TCRK.KEEP_ID,\n");
		sbSql.append("                    SUM(NVL(TCRK.NUM, 0)) NUM\n");
		sbSql.append("               FROM TT_COMP_RES_KEEP TCRK\n");
		sbSql.append("              WHERE TCRK.NUM <> 0\n");
		sbSql.append("              GROUP BY TCRK.MATERIAL_ID, TCRK.ORG_ID, TCRK.KEEP_ID) C\n");
		sbSql.append("         ON C.MATERIAL_ID = MAT.MATERIAL_ID AND C.ORG_ID =  MAT.ORG_ID");
		sbSql.append(")");
		
		return sbSql.toString();
	}
	
	/**
	 * 方法描述 ： 获取资源查询SQL - 所有车辆资源,按站点分组<br/>
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public static String getAsSqlNormalOrgResourceQuery()
	{
		StringBuffer sbSql = new StringBuffer();
		
		// -- ②资源查询，需要分开站点
		sbSql.append("VEHICLE_STOCK_NORMAL AS (SELECT A.MATERIAL_ID,\n");
		sbSql.append("       A.ORG_ID,\n");
		sbSql.append("       A.REGION_NAME,\n");
		sbSql.append("       A.STOCK_AMOUNT,\n");
		sbSql.append("       NVL(B.RESERVE_AMOUNT, 0) ORDER_RESERVE_AMOUNT,\n");
		sbSql.append("       NVL(C.NUM, 0) LOCK_AMOUNT,\n");
		sbSql.append("       NVL(A.STOCK_AMOUNT,0) - NVL(B.RESERVE_AMOUNT, 0) - NVL(C.NUM, 0) RESOURCE_AMOUNT\n");
		sbSql.append("  FROM (SELECT TV.MATERIAL_ID,\n");
		sbSql.append("                    TPD.ORG_ID,\n");
		sbSql.append("                    '销售部' REGION_NAME,\n");
		sbSql.append("                    COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("               FROM TM_VEHICLE TV, TM_PLAN_DETAIL TPD\n");
		sbSql.append("              WHERE TV.PLAN_DETAIL_ID = TPD.PLAN_DETAIL_ID\n");
		sbSql.append("                AND TV.LIFE_CYCLE = 10321002 --车厂库存（生命周期 - TC_CODE）\n");
		sbSql.append("                AND TV.LOCK_STATUS = 10241001 --正常状态（锁定状态 - TC_CODE）\n");
		sbSql.append("                AND TPD.ORG_ID = -2222222222\n");
		sbSql.append("                AND (TPD.IS_FLEET = 10041002 OR TPD.IS_FLEET IS NULL)\n");
		sbSql.append("              GROUP BY TV.MATERIAL_ID, TPD.ORG_ID\n");
		sbSql.append("             UNION ALL\n");
		sbSql.append("             SELECT TV.MATERIAL_ID,\n");
		sbSql.append("                    TORG.ORG_ID,\n");
		sbSql.append("                    TORG.ORG_NAME REGION_NAME,\n");
		sbSql.append("                    COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("               FROM TM_VEHICLE TV, TM_PLAN_DETAIL TPD, TM_ORG TORG\n");
		sbSql.append("              WHERE TV.PLAN_DETAIL_ID = TPD.PLAN_DETAIL_ID\n");
		sbSql.append("                AND TPD.ORG_ID = TORG.ORG_ID\n");
		sbSql.append("                AND TV.LIFE_CYCLE = 10321002 --车厂库存（生命周期 - TC_CODE）\n");
		sbSql.append("                AND TV.LOCK_STATUS = 10241001 --正常状态（锁定状态 - TC_CODE）\n");
		sbSql.append("                AND (TPD.IS_FLEET = 10041002 OR TPD.IS_FLEET IS NULL)\n");
		sbSql.append("                AND TPD.ORG_ID <> -2222222222\n");
		sbSql.append("              GROUP BY TV.MATERIAL_ID, TORG.ORG_ID, TORG.ORG_NAME) A\n");
		sbSql.append("  LEFT JOIN (SELECT MATERIAL_ID,\n");
		sbSql.append("                    ORG_ID,\n");
		sbSql.append("                    SUM(NVL(AMOUNT, 0) - NVL(ALLOCA_NUM, 0)) RESERVE_AMOUNT --保留数量 = 预留 - 已配车\n");
		sbSql.append("               FROM TT_VS_ORDER_RESOURCE_RESERVE\n");
		sbSql.append("              WHERE RESERVE_STATUS = 11581001 --保留\n");
		sbSql.append("                AND CREATE_ID IS NULL\n");
		sbSql.append("              GROUP BY MATERIAL_ID, ORG_ID) B\n");
		sbSql.append("         ON A.MATERIAL_ID = B.MATERIAL_ID AND A.ORG_ID = B.ORG_ID\n");
		sbSql.append("  FULL JOIN (SELECT TCRK.MATERIAL_ID,\n");
		sbSql.append("                    TCRK.ORG_ID,\n");
		sbSql.append("                    SUM(NVL(TCRK.NUM, 0)) NUM\n");
		sbSql.append("               FROM TT_COMP_RES_KEEP TCRK\n");
		sbSql.append("              WHERE TCRK.NUM <> 0\n");
		sbSql.append("              GROUP BY TCRK.MATERIAL_ID, TCRK.ORG_ID) C\n");
		sbSql.append("         ON C.MATERIAL_ID = A.MATERIAL_ID AND C.ORG_ID =  A.ORG_ID)");
		
		return sbSql.toString();
	}
	
	/**
	 * 方法描述 ： 获取资源查询SQL - 所有车辆资源,按站点分组<br/>
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public static String getAsSqlSpecialOrgResourceQuery()
	{
		StringBuffer sbSql = new StringBuffer();
		
		// -- ②资源查询，需要分开站点
		sbSql.append("VEHICLE_STOCK_SPECIAL AS (SELECT A.MATERIAL_ID,\n");
		sbSql.append("       A.ORG_ID,\n");
		sbSql.append("       A.REGION_NAME,\n");
		sbSql.append("       A.STOCK_AMOUNT,\n");
		sbSql.append("       A.PLAN_DETAIL_ID,\n");
		sbSql.append("		 0 AS LOCK_AMOUNT,\n");
		sbSql.append("       NVL(B.RESERVE_AMOUNT, 0) ORDER_RESERVE_AMOUNT,\n");
		sbSql.append("       NVL(A.STOCK_AMOUNT,0) - NVL(B.RESERVE_AMOUNT, 0) RESOURCE_AMOUNT\n");
		sbSql.append("  FROM (SELECT TV.MATERIAL_ID,\n");
		sbSql.append("                    TPD.ORG_ID,\n");
		sbSql.append("                    '销售部' REGION_NAME,\n");
		sbSql.append("                    TPD.PLAN_DETAIL_ID,\n");
		sbSql.append("                    COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("               FROM TM_VEHICLE TV, TM_PLAN_DETAIL TPD\n");
		sbSql.append("              WHERE TV.PLAN_DETAIL_ID = TPD.PLAN_DETAIL_ID\n");
		sbSql.append("                AND TV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_02 + " --车厂库存（生命周期 - TC_CODE）\n");
		sbSql.append("                AND TV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + " --正常状态（锁定状态 - TC_CODE）\n");
		sbSql.append("                AND TPD.ORG_ID = -2222222222\n");
		sbSql.append("                AND TPD.IS_FLEET = " + Constant.IF_TYPE_YES + "\n");
		sbSql.append("              GROUP BY TV.MATERIAL_ID, TPD.ORG_ID,TPD.PLAN_DETAIL_ID\n");
		sbSql.append("             UNION ALL\n");
		sbSql.append("             SELECT TV.MATERIAL_ID,\n");
		sbSql.append("                    TORG.ORG_ID,\n");
		sbSql.append("                    TORG.ORG_NAME REGION_NAME,\n");
		sbSql.append("                    TPD.PLAN_DETAIL_ID,\n");
		sbSql.append("                    COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("               FROM TM_VEHICLE TV, TM_PLAN_DETAIL TPD, TM_ORG TORG\n");
		sbSql.append("              WHERE TV.PLAN_DETAIL_ID = TPD.PLAN_DETAIL_ID\n");
		sbSql.append("                AND TPD.ORG_ID = TORG.ORG_ID\n");
		sbSql.append("                AND TV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_02 + " --车厂库存（生命周期 - TC_CODE）\n");
		sbSql.append("                AND TV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + " --正常状态（锁定状态 - TC_CODE）\n");
		sbSql.append("                AND TPD.ORG_ID <> -2222222222\n");
		sbSql.append("                AND TPD.IS_FLEET = " + Constant.IF_TYPE_YES + "\n");
		sbSql.append("              GROUP BY TV.MATERIAL_ID, TORG.ORG_ID, TPD.PLAN_DETAIL_ID, TORG.ORG_NAME) A\n");
		sbSql.append("  LEFT JOIN (SELECT MATERIAL_ID,\n");
		sbSql.append("                    ORG_ID,\n");
		sbSql.append("                    CREATE_ID,\n");
		sbSql.append("                    SUM(NVL(AMOUNT, 0) - NVL(ALLOCA_NUM, 0)) RESERVE_AMOUNT --保留数量 = 预留 - 已配车\n");
		sbSql.append("               FROM TT_VS_ORDER_RESOURCE_RESERVE\n");
		sbSql.append("              WHERE RESERVE_STATUS = 11581001 --保留\n");
		sbSql.append("              GROUP BY MATERIAL_ID, ORG_ID, CREATE_ID) B\n");
		sbSql.append("         ON A.MATERIAL_ID = B.MATERIAL_ID AND A.ORG_ID = B.ORG_ID AND A.PLAN_DETAIL_ID = B.CREATE_ID\n");
		sbSql.append(")");
		
		return sbSql.toString();
	}
	
	/**
	 * 方法描述 ： 获取资源查询SQL - 常规生产的车辆资源<br/>
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public static String getAsSqlNormalResourceQuery()
	{
		
		StringBuffer sbSql = new StringBuffer();
		
		// -- ①资源查询，不分开站点
		sbSql.append("VEHICLE_STOCK_ALL AS (");
		sbSql.append("SELECT A.MATERIAL_ID,\n");
		sbSql.append("       A.STOCK_AMOUNT,\n");
		sbSql.append("       NVL(B.RESERVE_AMOUNT, 0) ORDER_RESERVE_AMOUNT,\n");
		sbSql.append("       NVL(C.NUM, 0) LOCK_AMOUNT,\n");
		sbSql.append("       A.STOCK_AMOUNT - NVL(B.RESERVE_AMOUNT, 0) - NVL(C.NUM, 0) RESOURCE_AMOUNT\n");
		sbSql.append("  FROM (SELECT TV.MATERIAL_ID, COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("          FROM TM_VEHICLE TV, TM_PLAN_DETAIL TPD\n");
		sbSql.append("         WHERE TV.PLAN_DETAIL_ID = TPD.PLAN_DETAIL_ID\n");
		sbSql.append("           AND TV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_02 + " --车厂库存（生命周期 - TC_CODE）\n");
		sbSql.append("           AND TV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + " --正常状态（锁定状态 - TC_CODE）\n");
		sbSql.append("		     AND (TPD.IS_FLEET = " + Constant.IF_TYPE_NO + " OR TPD.IS_FLEET IS NULL)\n");
		sbSql.append("         GROUP BY TV.MATERIAL_ID) A,\n");
		sbSql.append("       (SELECT MATERIAL_ID,\n");
		sbSql.append("               SUM(NVL(AMOUNT, 0) - NVL(ALLOCA_NUM, 0)) RESERVE_AMOUNT --保留数量 = 预留 - 已配车\n");
		sbSql.append("          FROM TT_VS_ORDER_RESOURCE_RESERVE\n");
		sbSql.append("         WHERE RESERVE_STATUS = " + Constant.RESOURCE_RESERVE_STATUS_01 + " --保留\n");
		sbSql.append("		 	 AND CREATE_ID IS NULL\n");
		sbSql.append("         GROUP BY MATERIAL_ID) B,\n");
		sbSql.append("       (SELECT TCRK.MATERIAL_ID, SUM(NVL(TCRK.NUM, 0)) NUM\n");
		sbSql.append("          FROM TT_COMP_RES_KEEP TCRK\n");
		sbSql.append("         GROUP BY TCRK.MATERIAL_ID) C\n");
		sbSql.append(" WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)\n");
		sbSql.append("   AND A.MATERIAL_ID = C.MATERIAL_ID(+)\n");
		sbSql.append(")");
		
		return sbSql.toString();
	}
	
	/**
	 * 方法描述 ： 获取资源查询SQL - 集团订做生产的车辆资源<br/>
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public static String getAsSqlSpecialResourceQuery(String createId)
	{
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("VEHICLE_STOCK_ALL AS (");
		sbSql.append("SELECT A.MATERIAL_ID,\n");
		sbSql.append("       A.STOCK_AMOUNT,\n");
		sbSql.append("       NVL(B.RESERVE_AMOUNT, 0) ORDER_RESERVE_AMOUNT,\n");
		sbSql.append("       0 AS LOCK_AMOUNT,\n");
		sbSql.append("       A.STOCK_AMOUNT - NVL(B.RESERVE_AMOUNT, 0) RESOURCE_AMOUNT\n");
		sbSql.append("  FROM \n");
		sbSql.append("		 (" + getSqlStockSpecialQuery(createId) + ") A,\n");
		sbSql.append("       (" + getSqlSpecialResourceResaveQuery(createId) + ") B\n");
		sbSql.append(" WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)\n");
		sbSql.append(")");
		
		return sbSql.toString();
	}
	
	/**
	 * 方法描述 ： 获取车厂集团订做车总资源查询SQL<br/>
	 * 
	 * @author wangsongwei
	 */
	public static String getSqlStockSpecialQuery(String createId)
	{
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT TV.MATERIAL_ID, COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("  FROM TM_VEHICLE TV, TM_PLAN_DETAIL TPD\n");
		sbSql.append(" WHERE TV.PLAN_DETAIL_ID = TPD.PLAN_DETAIL_ID\n");
		sbSql.append("   AND TV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_02 + " --车厂库存（生命周期 - TC_CODE）\n");
		sbSql.append("   AND TV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + " --正常状态（锁定状态 - TC_CODE）\n");
		sbSql.append("   AND TPD.IS_FLEET = " + Constant.IF_TYPE_YES + "\n");
		sbSql.append("   AND EXISTS (SELECT 1\n");
		sbSql.append("          FROM TM_CUS_ORDER   A,\n");
		sbSql.append("               TM_PRO_ORDER   B,\n");
		sbSql.append("               TM_PRO_DETAIL  C,\n");
		sbSql.append("               TM_PLAN_DETAIL D\n");
		sbSql.append("         WHERE A.PRO_ORDER_ID = B.PRO_ORDER_ID\n");
		sbSql.append("           AND B.PRO_ORDER_ID = C.PRO_ORDER_ID\n");
		sbSql.append("           AND C.PLAN_DETAIL_ID = D.PLAN_DETAIL_ID\n");
		sbSql.append("           AND TV.PLAN_DETAIL_ID = D.PLAN_DETAIL_ID\n");
		if (createId != null && !"".equals(createId)) {
			sbSql.append("       AND A.CUS_ORDER_ID = " + createId + "\n");
		}
		sbSql.append(") GROUP BY TV.MATERIAL_ID");
		
		return sbSql.toString();
	}
	
	/**
	 * 方法描述 ： 获取车厂常规订做车总资源查询SQL<br/>
	 * 
	 * @author wangsongwei
	 */
	public static String getSqlStockNomalQuery()
	{
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT TV.MATERIAL_ID, COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("  FROM TM_VEHICLE TV, TM_PLAN_DETAIL TPD\n");
		sbSql.append(" WHERE TV.PLAN_DETAIL_ID = TPD.PLAN_DETAIL_ID\n");
		sbSql.append("   AND TV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_02 + " --车厂库存（生命周期 - TC_CODE）\n");
		sbSql.append("   AND TV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + " --正常状态（锁定状态 - TC_CODE）\n");
		sbSql.append("   AND (TPD.IS_FLEET = " + Constant.IF_TYPE_NO + " OR TPD.IS_FLEET IS NULL)\n");
		sbSql.append(" GROUP BY TV.MATERIAL_ID");
		
		return sbSql.toString();
	}
	
	/**
	 * 方法描述 ： 获取车厂总资源查询SQL<br/>
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public static String getSqlStockAllQuery()
	{
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT TV.MATERIAL_ID, COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("  FROM TM_VEHICLE TV, TM_PLAN_DETAIL TPD\n");
		sbSql.append(" WHERE TV.PLAN_DETAIL_ID = TPD.PLAN_DETAIL_ID\n");
		sbSql.append("   AND TV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_02 + " --车厂库存（生命周期 - TC_CODE）\n");
		sbSql.append("   AND TV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + " --正常状态（锁定状态 - TC_CODE）\n");
		sbSql.append(" GROUP BY TV.MATERIAL_ID");
		
		return sbSql.toString();
	}
	
	/**
	 * 方法描述 ： 获取车厂资源预留查询SQL<br/>
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public static String getSqlCompStockQuery()
	{
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("       SELECT TCRK.MATERIAL_ID, SUM(NVL(TCRK.NUM, 0)) NUM\n");
		sbSql.append("          FROM TT_COMP_RES_KEEP TCRK WHERE TCRK.NUM <> 0\n");
		sbSql.append("         GROUP BY TCRK.MATERIAL_ID\n");
		
		return sbSql.toString();
	}
	
	/**
	 * 方法描述 ： 获取车系折扣率查询条件<br/>
	 * -- MATERIAL_DISRATE
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public static String getSqlSeriesRateQuery(String seriesId, String yieldly)
	{
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("    SELECT TSD.DIS_VALUE FROM TT_SALES_DIS TSD WHERE 1 = 1\n");
		if (seriesId != null && !"".equals(seriesId)) {
			sbSql.append(" AND TSD.GROUP_ID = " + seriesId + "\n");
		}
		if (yieldly != null && !"".equals(yieldly)) {
			sbSql.append(" AND TSD.YIELDLY = " + yieldly + "\n");
		}
		
		return sbSql.toString();
	}
	
	/**
	 * 方法描述 ： 获取资金账户的折扣查询条件 <br/>
	 * -- FOUN_TYPE_DISRATE
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public static String getSqlFinAccRateQuery(String finType, String yieldly)
	{
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("    SELECT TSFD.DIS_VALUE FROM TT_SALES_FIN_DIS TSFD WHERE 1 = 1\n");
		if (finType != null && !"".equals(finType)) {
			sbSql.append(" AND TSFD.FIN_TYPE = " + finType + "\n");
		}
		if (yieldly != null && !"".equals(yieldly)) {
			sbSql.append(" AND TSFD.YIELDLY = " + yieldly + "\n");
		}
		
		return sbSql.toString();
	}
	
	/**
	 * 方法描述 ： 获取所有资源预留查询SQL<br/>
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public static String getSqlResourceResaveQuery()
	{
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("       SELECT MATERIAL_ID,\n");
		sbSql.append("               SUM(NVL(AMOUNT, 0) - NVL(ALLOCA_NUM, 0)) RESERVE_AMOUNT --保留数量 = 预留 - 已配车\n");
		sbSql.append("          FROM TT_VS_ORDER_RESOURCE_RESERVE\n");
		sbSql.append("         WHERE RESERVE_STATUS = " + Constant.RESOURCE_RESERVE_STATUS_01 + " --保留\n");
		sbSql.append("         GROUP BY MATERIAL_ID\n");
		
		return sbSql.toString();
	}
	
	/**
	 * 方法描述 ： 获取常规资源预留查询SQL<br/>
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public static String getSqlNormalResourceResaveQuery()
	{
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("       SELECT MATERIAL_ID,\n");
		sbSql.append("               SUM(NVL(AMOUNT, 0) - NVL(ALLOCA_NUM, 0)) RESERVE_AMOUNT --保留数量 = 预留 - 已配车\n");
		sbSql.append("          FROM TT_VS_ORDER_RESOURCE_RESERVE\n");
		sbSql.append("         WHERE RESERVE_STATUS = " + Constant.RESOURCE_RESERVE_STATUS_01 + " --保留\n");
		sbSql.append("           AND CREATE_ID IS NULL\n");
		sbSql.append("         GROUP BY MATERIAL_ID\n");
		
		return sbSql.toString();
	}
	
	/**
	 * 方法描述 ： 获取集团客户资源预留查询SQL<br/>
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public static String getSqlSpecialResourceResaveQuery(String createId)
	{
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT MATERIAL_ID, SUM(NVL(AMOUNT, 0) - NVL(ALLOCA_NUM, 0)) RESERVE_AMOUNT --保留数量 = 预留 - 已配车\n");
		sbSql.append("  FROM TT_VS_ORDER_RESOURCE_RESERVE\n");
		sbSql.append(" WHERE RESERVE_STATUS = " + Constant.RESOURCE_RESERVE_STATUS_01 + " --保留\n");
		sbSql.append("   AND CREATE_ID IN (SELECT D.PLAN_DETAIL_ID\n");
		sbSql.append("                       FROM TM_CUS_ORDER   A,\n");
		sbSql.append("                            TM_PRO_ORDER   B,\n");
		sbSql.append("                            TM_PRO_DETAIL  C,\n");
		sbSql.append("                            TM_PLAN_DETAIL D\n");
		sbSql.append("                      WHERE A.PRO_ORDER_ID = B.PRO_ORDER_ID\n");
		sbSql.append("                        AND B.PRO_ORDER_ID = C.PRO_ORDER_ID\n");
		sbSql.append("                        AND C.PLAN_DETAIL_ID = D.PLAN_DETAIL_ID\n");
		sbSql.append("                        AND A.CUS_ORDER_ID = " + createId + ")\n");
		sbSql.append(" GROUP BY MATERIAL_ID");
		
		return sbSql.toString();
	}
	
	/**
	 * 方法描述 ： 集团客户订做车资源占用明细查询SQL<br/>
	 * 
	 * @param orderId
	 * @return
	 * @author wangsongwei
	 */
	public static String getAsSqlCusOrderDetailQuery(String orderId)
	{
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("CUS_ORDER_DETIAL AS (");
		sbSql.append("SELECT TVOD.ORDER_AMOUNT,\n");
		sbSql.append("       TVO.FUND_TYPE_ID,\n");
		sbSql.append("       TVO.AREA_ID,\n");
		sbSql.append("       TVOD.CUS_DETAIL_ID\n");
		sbSql.append("  FROM TT_VS_ORDER TVO, TT_VS_ORDER_DETAIL TVOD\n");
		sbSql.append(" WHERE TVOD.ORDER_ID = TVO.ORDER_ID\n");
		if (orderId != null) {
			sbSql.append("    AND TVO.ORDER_ID = " + orderId);
		}
		else {
			sbSql.append("    AND TVO.ORDER_ID = NULL\n");
		}
		sbSql.append(")");
		
		return sbSql.toString();
	}
	
	public static String getSqlQueryCondition(String queryString)
	{
		if (queryString != null && !"".equals(queryString))
		{
			String[] splitString = queryString.split(",");
			String splitConnectString = "";
			for (int i = 0; i < splitString.length; i++)
			{
				splitConnectString += "'" + splitString[i] + "'" + ",";
			}
			splitConnectString = splitConnectString.substring(0, splitConnectString.length() - 1);
			
			return splitConnectString;
		}
		else
		{
			return "''";
		}
	}
	
	/**
	 * 方法描述 ： 获取物料价格SQL查询条件<br/>
	 * 
	 * @param dealerId
	 * @return
	 * @author wangsongwei
	 */
	public static String getSqlRegionPriceQuery(String dealerId, String yieldly)
	{
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("             SELECT   TSAP.AMOUNT\n");
		sbSql.append("             FROM\n");
		sbSql.append("                      TM_REGION                     TR,\n");
		sbSql.append("                      TT_SALES_AREA_PRICE           TSAP,\n");
		sbSql.append("                      TM_DEALER                     TD\n");
		sbSql.append("             WHERE\n");
		sbSql.append("                      TR.REGION_CODE = TSAP.AREA_ID(+)\n");
		sbSql.append("                      AND TR.REGION_TYPE = " + Constant.REGION_TYPE_02 + "\n");
		sbSql.append("                      AND TSAP.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sbSql.append("                      AND TD.PROVINCE_ID = TR.REGION_CODE\n");
		sbSql.append("                      AND TD.DEALER_ID = " + dealerId + "\n");
		sbSql.append("                      AND TSAP.YIELDLY = " + yieldly + "\n");
		
		return sbSql.toString();
	}
	
	public static void main(String[] args)
	{
		
		StringBuffer sbSql = new StringBuffer();
		
//		sbSql.append("WITH ");
//		sbSql.append(ResourceSqlDao.getAsSqlSpecialResourceQuery("1"));	// 物料库存资源
//		sbSql.append(",\n");
//		sbSql.append(ResourceSqlDao.getAsSqlSeriesRateQuery(null, null)); // 车系折扣率
//		sbSql.append(",\n");
//		sbSql.append(ResourceSqlDao.getAsSqlFinAccRateQuery(null, null)); // 资金账户折扣率
//		sbSql.append(" \n");
		
		System.out.println(getAsSqlNormalOrgResourceQuery());
	}
}
