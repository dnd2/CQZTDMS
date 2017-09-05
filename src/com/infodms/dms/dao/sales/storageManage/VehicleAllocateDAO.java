package com.infodms.dms.dao.sales.storageManage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class VehicleAllocateDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(VehicleAllocateDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO();

	public static final CheckVehicleDAO getInstance() {
		return dao;
	}

	public static PageResult<Map<String, Object>> getCanAllocateVehicleQuery(String org_id, String dutyType, String vin, int pageSize, int curPage) {

		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT * FROM  (\n");
		sql.append("  SELECT TMV.VEHICLE_ID,\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TMBA.AREA_NAME, --业务范围名称\n");
		sql.append("       TMV.ENGINE_NO, --发动机号\n");
		sql.append("       G.GROUP_NAME AS SERIES_NAME, --车系\n");
		sql.append("       G1.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       TMVM.MATERIAL_CODE, --物料代码\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料名称\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_DATE, --验收日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS STORAGE_DAY,\n");
		sql.append("		TVVA.ALLOCATE_NO,\n");
		sql.append("		TVVA.OUT_DEALER_NAME,\n");
		sql.append("		TVVA.IN_DEALER_NAME,\n");
		sql.append("		TVVA.CHECK_STATUS,\n");
		sql.append("		TVVA.ALLOCATE_REASON,\n");
		sql.append("		TVVA.ALLOCATE_ID,\n");
		sql.append("		TVVA.INTERFACE_FLAG,\n");
		
		sql.append("  	    TO_CHAR(TVVA.ALLOCATE_DATE_OUT, 'YYYY-MM-DD') AS ALLOCATE_DATE_OUT,\n") ;
		sql.append("  	    TO_CHAR(TVVA.ALLOCATE_DATE_IN, 'YYYY-MM-DD') AS ALLOCATE_DATE_IN,\n") ;
		sql.append("  	    TO_CHAR(TVVA.ALLOCATE_DATE, 'YYYY-MM-DD') AS ALLOCATE_DATE\n") ;

		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_BUSINESS_AREA       TMBA,\n");
		sql.append("	(SELECT TVVA.VEHICLE_ID,\n");
		sql.append(" 	       TVVA.ALLOCATE_NO,\n");
		sql.append(" 	       TVVA.ALLOCATE_ID,\n");
		sql.append("		TVVA.INTERFACE_FLAG,\n");
		sql.append(" 	       TVVA.ALLOCATE_DATE_OUT,\n");
		sql.append(" 	       TVVA.ALLOCATE_DATE_IN,\n");
		sql.append("  	      (SELECT T.DEALER_SHORTNAME\n");
		sql.append("           FROM TM_DEALER T\n");
		sql.append("  	        WHERE T.DEALER_ID = TVVA.OUT_DEALER_ID) AS OUT_DEALER_NAME,\n");
		sql.append("    	    TVVA.OUT_DEALER_ID,\n");
		sql.append("        	(SELECT T.DEALER_SHORTNAME\n");
		sql.append("    	       FROM TM_DEALER T\n");
		sql.append("          WHERE T.DEALER_ID = TVVA.IN_DEALER_ID) AS IN_DEALER_NAME,\n");
		sql.append("  	      TVVA.IN_DEALER_ID,\n");
		sql.append("    	    (SELECT T.CODE_DESC\n");
		sql.append("   	        FROM TC_CODE T\n");
		sql.append("   	       WHERE T.CODE_ID = TVVA.CHECK_STATUS) AS CHECK_STATUS,\n");
		sql.append("  	      TVVA.ALLOCATE_REASON,\n");
		sql.append("  	      TVVA.ALLOCATE_DATE\n");
		sql.append(" 	  FROM TT_VS_VEHICLE_ALLOCATE TVVA) TVVA\n");
		sql.append(" WHERE TMV.AREA_ID = TMBA.AREA_ID(+)\n");
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.MODEL_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.VEHICLE_ID = TVVA.VEHICLE_ID\n");

		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");

		
		sql.append("AND EXISTS (SELECT 1\n");
		sql.append("       FROM vw_org_dealer T\n");
		sql.append("      WHERE TVVA.OUT_DEALER_ID = T.DEALER_ID\n");
		sql.append("        AND (T.COMPANY_ID = " + org_id + " OR\n");
		sql.append("            T.ROOT_ORG_ID = " + org_id + " OR\n");
		sql.append("            T.PQ_ORG_ID = " + org_id + " OR\n");
		sql.append("            T.ROOT_DEALER_ID = " + org_id + " OR\n");
		sql.append("            T.DEALER_ID = " + org_id + ")\n");
		sql.append("     UNION ALL\n");
		sql.append("     SELECT 1\n");
		sql.append("       FROM vw_org_dealer T\n");
		sql.append("      WHERE TVVA.IN_DEALER_ID = T.DEALER_ID\n");
		sql.append("        AND (T.COMPANY_ID = " + org_id + " OR\n");
		sql.append("            T.ROOT_ORG_ID = " + org_id + " OR\n");
		sql.append("            T.PQ_ORG_ID = " + org_id + " OR\n");
		sql.append("            T.ROOT_DEALER_ID = " + org_id + " OR\n");
		sql.append("            T.DEALER_ID = " + org_id + ")\n");
		sql.append("\n");
		sql.append("     UNION ALL\n");
		sql.append("     SELECT 1\n");
		sql.append("       FROM vw_org_dealer T\n");
		sql.append("      WHERE TVVA.OUT_DEALER_ID = T.ROOT_DEALER_ID\n");
		sql.append("        AND (T.COMPANY_ID = " + org_id + " OR\n");
		sql.append("            T.ROOT_ORG_ID = " + org_id + " OR\n");
		sql.append("            T.PQ_ORG_ID = " + org_id + " OR\n");
		sql.append("            T.ROOT_DEALER_ID = " + org_id + ")\n");
		sql.append("     UNION ALL\n");
		sql.append("     SELECT 1\n");
		sql.append("       FROM vw_org_dealer T\n");
		sql.append("      WHERE TVVA.IN_DEALER_ID = T.ROOT_DEALER_ID\n");
		sql.append("        AND (T.COMPANY_ID = " + org_id + " OR\n");
		sql.append("            T.ROOT_ORG_ID = " + org_id + " OR\n");
		sql.append("            T.PQ_ORG_ID = " + org_id + " OR\n");
		sql.append("            T.ROOT_DEALER_ID = " + org_id + "))\n");

		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		sql.append(" ORDER BY ALLOCATE_DATE DESC ,TVVA.ALLOCATE_NO DESC,rownum   )TS \n");
		return dao.pageQuery(sql.toString(), params, "com.infodms.dms.dao.sales.storageManage.VehicleAllocateDAO.getCanAllocateVehicleQuery", pageSize, curPage);
	}

	public static PageResult<Map<String, Object>> getCanAllocateVehicleOutList(String org_id, String vin, int pageSize, int curPage) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMV.VEHICLE_ID,\n");
		sql.append("          TVAT.TYPE_NAME, --TYPE_NAME\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TMBA.AREA_NAME, --业务范围名称\n");
		sql.append("       TMV.ENGINE_NO, --发动机号\n");
		sql.append("       G.GROUP_NAME AS SERIES_NAME, --车系\n");
		sql.append("       G1.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       TMVM.MATERIAL_CODE, --物料代码\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料名称\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_DATE, --验收日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS STORAGE_DAY,\n");
		sql.append("        TVVA.ALLOCATE_ID,\n") ;
		sql.append("		TVVA.ALLOCATE_NO,\n");
		sql.append("		TVVA.OUT_DEALER_NAME,\n");
		sql.append("		TVVA.IN_DEALER_NAME,\n");
		sql.append("		TVVA.CHECK_STATUS,\n");
		sql.append("		TVVA.ALLOCATE_REASON,\n");
		sql.append("  TO_CHAR(TVVA.ALLOCATE_DATE_OUT, 'YYYY-MM-DD') AS ALLOCATE_DATE_OUT,\n") ;
		sql.append("  TO_CHAR(TVVA.ALLOCATE_DATE_IN, 'YYYY-MM-DD') AS ALLOCATE_DATE_IN,\n") ;
		sql.append("  TO_CHAR(TVVA.ALLOCATE_DATE, 'YYYY-MM-DD') AS ALLOCATE_DATE\n") ;
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_BUSINESS_AREA       TMBA,\n");
		sql.append("	(SELECT TVVA.VEHICLE_ID,\n");
		sql.append("           TVVA.ALLOCATE_ID,\n") ;
		sql.append(" 	       TVVA.ALLOCATE_NO,\n");
		sql.append(" 	       TVVA.ALLOCATE_DATE_OUT,\n");
		 sql.append(" 	       TVVA.FUNDTYPE_OUT_ID,\n");
		sql.append(" 	       TVVA.ALLOCATE_DATE_IN,\n");
		sql.append("  	      (SELECT T.DEALER_SHORTNAME\n");
		sql.append("           FROM TM_DEALER T\n");
		sql.append("  	        WHERE T.DEALER_ID = TVVA.OUT_DEALER_ID) AS OUT_DEALER_NAME,\n");
		sql.append("    	    TVVA.OUT_DEALER_ID,\n");
		sql.append("        	(SELECT T.DEALER_SHORTNAME\n");
		sql.append("    	       FROM TM_DEALER T\n");
		sql.append("          WHERE T.DEALER_ID = TVVA.IN_DEALER_ID) AS IN_DEALER_NAME,\n");
		sql.append("  	      TVVA.IN_DEALER_ID,\n");
		sql.append("    	    (SELECT T.CODE_DESC\n");
		sql.append("   	        FROM TC_CODE T\n");
		sql.append("   	       WHERE T.CODE_ID = TVVA.CHECK_STATUS) AS CHECK_STATUS,\n");
		sql.append("  	      TVVA.ALLOCATE_REASON,\n");
		sql.append("  	      TVVA.ALLOCATE_DATE\n");
		sql.append(" 	  FROM TT_VS_VEHICLE_ALLOCATE TVVA\n") ;
		sql.append(" WHERE TVVA.CHECK_STATUS = " + Constant.ALLOCATE_STATUS_01 + ") TVVA,\n") ;
		sql.append("    TT_VS_ACCOUNT_TYPE TVAT");
		sql.append("  WHERE  TVAT.TYPE_ID = TVVA.FUNDTYPE_OUT_ID ");
		sql.append(" AND  TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + "\n");
		sql.append("   AND TMV.LOCK_STATUS = " + Constant.LOCK_STATUS_08 + "\n");
		sql.append("   AND TMV.AREA_ID = TMBA.AREA_ID(+)\n");
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.MODEL_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.VEHICLE_ID = TVVA.VEHICLE_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("  AND TVVA.OUT_DEALER_ID = " + org_id + " \n");
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		sql.append(" ORDER BY TMV.STORAGE_DATE DESC\n");
		return dao.pageQuery(sql.toString(), params, "com.infodms.dms.dao.sales.storageManage.VehicleAllocateDAO.getCanAllocateVehicleQuery", pageSize, curPage);
	}

	public static PageResult<Map<String, Object>> getCanAllocateVehicleInList(String org_id, String vin, int pageSize, int curPage) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMV.VEHICLE_ID,\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TVAT.TYPE_NAME, \n");
		sql.append("       TMBA.AREA_NAME, --业务范围名称\n");
		sql.append("       TMV.ENGINE_NO, --发动机号\n");
		sql.append("       G.GROUP_NAME AS SERIES_NAME, --车系\n");
		sql.append("       G1.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       TMVM.MATERIAL_CODE, --物料代码\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料名称\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_DATE, --验收日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS STORAGE_DAY,\n");
		sql.append("        TVVA.ALLOCATE_ID,\n") ;
		sql.append("		TVVA.ALLOCATE_NO,\n");
		sql.append("		TVVA.OUT_DEALER_NAME,\n");
		sql.append("		TVVA.IN_DEALER_NAME,\n");
		sql.append("		TVVA.CHECK_STATUS,\n");
		sql.append("		TVVA.ALLOCATE_REASON,\n");
		sql.append("  TO_CHAR(TVVA.ALLOCATE_DATE_OUT, 'YYYY-MM-DD') AS ALLOCATE_DATE_OUT,\n") ;
		sql.append("  TO_CHAR(TVVA.ALLOCATE_DATE_IN, 'YYYY-MM-DD') AS ALLOCATE_DATE_IN,\n") ;
		sql.append("  TO_CHAR(TVVA.ALLOCATE_DATE, 'YYYY-MM-DD') AS ALLOCATE_DATE\n") ;
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_BUSINESS_AREA       TMBA,\n");
		sql.append("	(SELECT TVVA.VEHICLE_ID,\n");
		sql.append("           TVVA.ALLOCATE_ID,\n") ;
		sql.append(" 	       TVVA.ALLOCATE_NO,\n");
		sql.append(" 	       TVVA.FUNDTYPE_IN_ID,\n");
		sql.append(" 	       TVVA.ALLOCATE_DATE_OUT,\n");
		sql.append(" 	       TVVA.ALLOCATE_DATE_IN,\n");
		sql.append("  	      (SELECT T.DEALER_SHORTNAME\n");
		sql.append("           FROM TM_DEALER T\n");
		sql.append("  	        WHERE T.DEALER_ID = TVVA.OUT_DEALER_ID) AS OUT_DEALER_NAME,\n");
		sql.append("    	    TVVA.OUT_DEALER_ID,\n");
		sql.append("        	(SELECT T.DEALER_SHORTNAME\n");
		sql.append("    	       FROM TM_DEALER T\n");
		sql.append("          WHERE T.DEALER_ID = TVVA.IN_DEALER_ID) AS IN_DEALER_NAME,\n");
		sql.append("  	      TVVA.IN_DEALER_ID,\n");
		sql.append("    	    (SELECT T.CODE_DESC\n");
		sql.append("   	        FROM TC_CODE T\n");
		sql.append("   	       WHERE T.CODE_ID = TVVA.CHECK_STATUS) AS CHECK_STATUS,\n");
		sql.append("  	      TVVA.ALLOCATE_REASON,\n");
		sql.append("  	      TVVA.ALLOCATE_DATE\n");
		sql.append(" 	  FROM TT_VS_VEHICLE_ALLOCATE TVVA\n") ;
		sql.append(" WHERE TVVA.CHECK_STATUS = " + Constant.ALLOCATE_STATUS_02 + ") TVVA,\n") ;
		sql.append("   TT_VS_ACCOUNT_TYPE TVAT\n");
		sql.append("   WHERE TVVA.FUNDTYPE_IN_ID=TVAT.TYPE_ID\n");
		sql.append(" AND  TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + "\n");
		sql.append("   AND TMV.LOCK_STATUS = " + Constant.LOCK_STATUS_08 + "\n");
		sql.append("   AND TMV.AREA_ID = TMBA.AREA_ID(+)\n");
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.MODEL_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.VEHICLE_ID = TVVA.VEHICLE_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("  AND TVVA.IN_DEALER_ID = " + org_id + " \n");
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		sql.append(" ORDER BY TMV.STORAGE_DATE DESC\n");
		return dao.pageQuery(sql.toString(), params, "com.infodms.dms.dao.sales.storageManage.VehicleAllocateDAO.getCanAllocateVehicleQuery", pageSize, curPage);
	}
	
	
	/***************************************************************************
	 * 调拨申请：查询可调拨车辆
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getCanAllocateVehicleList(String dealerId, String vin, String exVeh,String materialCode, int pageSize, int curPage) {

		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMV.VEHICLE_ID,\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TMBA.AREA_NAME, --业务范围名称\n");
		sql.append("       TMV.ENGINE_NO, --发动机号\n");
		sql.append("       G.GROUP_NAME AS SERIES_NAME, --车系\n");
		sql.append("       G1.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       TMVM.MATERIAL_CODE, --物料代码\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料名称\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_DATE, --验收日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS STORAGE_DAY\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_BUSINESS_AREA       TMBA\n");
		sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + "\n");
		sql.append("   AND TMV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + "\n");
		sql.append("   AND TMV.AREA_ID = TMBA.AREA_ID(+)\n");
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.MODEL_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");

		sql.append("   AND NOT EXISTS (SELECT TVVA.VEHICLE_ID\n");
		sql.append("    	   FROM TT_VS_VEHICLE_ALLOCATE TVVA\n");
		sql.append("      	WHERE TVVA.CHECK_STATUS IN (" + Constant.ALLOCATE_STATUS_01 + ", " + Constant.ALLOCATE_STATUS_02 + ")\n");
		sql.append("       	 AND TVVA.VEHICLE_ID = TMV.VEHICLE_ID)\n");

		sql.append("   AND TMV.VEHICLE_AREA IN\n");
		sql.append("  	  (SELECT TDW.WAREHOUSE_ID\n");
		sql.append("   	    FROM TM_DEALER_WAREHOUSE TDW\n");
		sql.append("   	   WHERE TDW.DEALER_ID = " + dealerId + ")\n");
		
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if(null!=materialCode&&!"".equals(materialCode)){
			sql.append(" 	AND TMVM.MATERIAL_CODE='"+materialCode+"'\n");
		}
		if (!CommonUtils.isNullString(exVeh)) {
			sql.append("           AND TMV.VEHICLE_ID NOT IN (").append(exVeh).append(")\n");
		}
		sql.append(" ORDER BY TMV.STORAGE_DATE DESC\n");
		return dao.pageQuery(sql.toString(), params, "com.infodms.dms.dao.sales.storageManage.VehicleAllocateDAO.getCanAllocateVehicleList", pageSize, curPage);
	}

	/**
	 * 判断申请批发是结算中心，是否有库位
	 */
	public static String getVinRightCar(String vehicleId) {
		StringBuffer sql = new StringBuffer(" select *\n");
		sql.append("from tm_vehicle tv\n");
		sql.append("where tv.dealer_id in\n");
		sql.append("(select tm.dealer_id\n");
		sql.append("from tm_dealer tm\n");
		sql.append(" where tm.dealer_type =" + Constant.DEALER_TYPE_JSZX + ")\n");
		sql.append("and tv.vehicle_area is null\n");
		sql.append(" and tv.vehicle_id='" + vehicleId + "'\n");
		String vinString = "";
		List myList = dao.pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.VehicleAllocateDAO.getVinRightCar");
		for (int i = 0; i < myList.size(); i++) {
			Map map = (Map) myList.get(i);
			vinString = vinString + map.get("VIN") + "/";
		}

		return vinString;
	}

	/***************************************************************************
	 * 调拨审核：查询可审核车辆
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getVehicleAllocateCheckList(String outDealerCode, String inDealerCode, String materialCode, String vin, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer("");
		sql.append("  SELECT TTVT.TRANSFER_ID, \n");
		sql.append("         TMD1.DEALER_SHORTNAME AS OUT_DEALER,   --调出经销商 \n");
		sql.append("         TMD2.DEALER_SHORTNAME AS IN_DEALER,    --调入经销商 \n");
		sql.append("         TTVT.TRANSFER_REASON,                  --调拨原因 \n");
		sql.append("         TTVT.TRANSFER_NO,                 		--批发号 \n");
		sql.append("         TMV.VIN,                               --VIN \n");
		sql.append("         TMV.ENGINE_NO,                         --发动机号 \n");
		sql.append("         TMVM.MATERIAL_NAME,                    --物料名称 \n");
		sql.append("         TO_CHAR(TTVT.TRANSFER_DATE,'yyyy-MM-dd') TRANSFER_DATE,  --申请日期 \n");
		sql.append("         TO_CHAR(TMV.STORAGE_DATE,'yyyy-MM-dd') STORAGE_DATE  --首次入库日期 \n");
		sql.append("    FROM TT_VS_VEHICLE_TRANSFER TTVT, \n");
		sql.append("         TM_DEALER TMD1, \n");
		sql.append("         TM_DEALER TMD2, \n");
		sql.append("         TM_VEHICLE TMV, \n");
		sql.append("         TM_VHCL_MATERIAL_GROUP G, \n");
		sql.append("         TM_VHCL_MATERIAL TMVM \n");
		sql.append("   WHERE     TTVT.OUT_DEALER_ID = TMD1.DEALER_ID \n");
		sql.append("         AND TTVT.IN_DEALER_ID = TMD2.DEALER_ID \n");
		sql.append("         AND TTVT.VEHICLE_ID = TMV.VEHICLE_ID \n");
		sql.append("         AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID \n");
		sql.append("         AND TMV.SERIES_ID = G.GROUP_ID\n");
		// 调出经销商
		if (null != outDealerCode && !"".equals(outDealerCode)) {
			String[] outDealerCodes = outDealerCode.split(",");
			if (null != outDealerCodes && outDealerCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < outDealerCodes.length; i++) {
					buffer.append("'").append(outDealerCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND TMD1.DEALER_CODE  IN (" + buffer.toString() + ") \n");
			}
		}
		// 调入经销商
		if (null != inDealerCode && !"".equals(inDealerCode)) {
			String[] inDealerCodes = inDealerCode.split(",");
			if (null != inDealerCodes && inDealerCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < inDealerCodes.length; i++) {
					buffer.append("'").append(inDealerCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND TMD2.DEALER_CODE  IN (" + buffer.toString() + ") \n");
			}
		}
		// 物料组选择
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				System.out.println(buffer);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                              WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR  SUBSTR(G.TREE_CODE, 0, 3) IN\n");
				sql.append("       (SELECT G.TREE_CODE\n");
				sql.append("           FROM TM_VHCL_MATERIAL_GROUP G\n");
				sql.append("          WHERE G.GROUP_CODE IN (" + buffer.toString() + ")))\n");
			}
		}
		// VIN
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		sql.append("         AND TTVT.CHECK_STATUS = " + Constant.ALLOCATE_STATUS_01 + " \n");
		sql.append("ORDER BY TTVT.CREATE_DATE DESC \n");
		return dao.pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.VehicleAllocateDAO.getVehicleAllocateCheckList", pageSize, curPage);
	}

	public static List<Map<String, Object>> selectAreaId(String dealerCode) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TDBA.AREA_ID\n");
		sql.append("  FROM TM_DEALER TMD, TM_DEALER_BUSINESS_AREA TDBA\n");
		sql.append(" WHERE TMD.DEALER_ID = TDBA.DEALER_ID\n");
		sql.append("   AND TMD.DEALER_CODE = '" + dealerCode + "'\n");
		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return rs;
	}

	public static List<Map<String, Object>> selectVehicleAreaId(String vehicleId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TMAG.AREA_ID, TMV.VIN\n");
		sql.append("  FROM TM_VEHICLE TMV, TM_AREA_GROUP TMAG\n");
		sql.append(" WHERE (TMV.PACKAGE_ID = TMAG.MATERIAL_GROUP_ID OR\n");
		sql.append("       TMV.MODEL_ID = TMAG.MATERIAL_GROUP_ID OR\n");
		sql.append("       TMV.SERIES_ID = TMAG.MATERIAL_GROUP_ID)\n");
		sql.append("   AND TMV.VEHICLE_ID IN (" + vehicleId + ")\n");
		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return rs;
	}

	public static List<Map<String, Object>> selectVehicleAreaId(Long vehicleId) throws Exception {
		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT * FROM TM_VEHICLE TMV WHERE TMV.VEHICLE_ID = " + vehicleId + "\n");

		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return rs;
	}

	public static Map<String, Object> getParentDealerId(String dealerID) {
		StringBuffer sql = new StringBuffer("\n");
		/*
		 * sql.append("SELECT TMD.PARENT_DEALER_D\n"); sql.append(" FROM
		 * TM_DEALER TMD\n"); sql.append(" WHERE TMD.DEALER_ID IN
		 * ("+dealerID+")\n");
		 */
		sql.append("select tmd.dealer_id parent_dealer_d\n");
		sql.append("  from tm_dealer tmd\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and tmd.dealer_level = ").append(Constant.DEALER_LEVEL_01).append("\n");
		sql.append(" start with tmd.dealer_id = ").append(dealerID).append("\n");
		sql.append("connect by prior tmd.parent_dealer_d = tmd.dealer_id\n");

		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, dao.getFunName());
		return rs;
	}

	/**
	 * 验证两个经销商dealerId是否属于同一经销商级别树，若属于同一树，则返回true；若属于不同树，则返回false
	 * 
	 * @param dlrA
	 * @param dlrB
	 * @return boolean
	 */
	public static boolean chkDlr(String dlrA, String dlrB) {
		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT TMD.DEALER_ID\n");
		sql.append("  FROM TM_DEALER TMD\n");
		sql.append(" WHERE TMD.DEALER_ID = " + dlrB + "\n");
		sql.append(" START WITH TMD.DEALER_ID = " + dlrA + "\n");
		sql.append("CONNECT BY PRIOR TMD.DEALER_ID = TMD.PARENT_DEALER_D\n");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		boolean flag = false;

		if (null != list && list.size() > 0) {
			flag = true;
		}

		return flag;
	}

	public static PageResult<Map<String, Object>> getVehicleAllocateCheckListA(String outDealerCode, String inDealerCode, String materialCode, String vin, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT T.TRANSFER_ID,\n");
		sql.append("       T.OUT_DEALER AS OUT_DEALER, --调出经销商\n");
		sql.append("       T.IN_DEALER AS IN_DEALER, --调入经销商\n");
		sql.append("       T.TRANSFER_REASON, --调拨原因\n");
		sql.append("       T.TRANSFER_NO, --批发号\n");
		sql.append("       T.VIN, --VIN\n");
		sql.append("       T.ENGINE_NO, --发动机号\n");
		sql.append("       T.MATERIAL_NAME, --物料名称\n");
		sql.append("       T.TRANSFER_DATE, --申请日期\n");
		sql.append("       T.STORAGE_DATE --首次入库日期\n");
		sql.append("  FROM (SELECT TTVT.TRANSFER_ID,\n");
		sql.append("               TMD1.DEALER_SHORTNAME AS OUT_DEALER, --调出经销商\n");
		sql.append("               TMD2.DEALER_SHORTNAME AS IN_DEALER, --调入经销商\n");
		sql.append("               TTVT.TRANSFER_REASON, --调拨原因\n");
		sql.append("               TTVT.TRANSFER_NO, --批发号\n");
		sql.append("               TMV.VIN, --VIN\n");
		sql.append("               TMV.ENGINE_NO, --发动机号\n");
		sql.append("               TMVM.MATERIAL_NAME, --物料名称\n");
		sql.append("               TO_CHAR(TTVT.TRANSFER_DATE, 'YYYY-MM-DD') TRANSFER_DATE, --申请日期\n");
		sql.append("               TO_CHAR(TMV.STORAGE_DATE, 'YYYY-MM-DD') STORAGE_DATE --首次入库日期\n");
		sql.append("          FROM TT_VS_VEHICLE_TRANSFER TTVT,\n");
		sql.append("               TM_DEALER              TMD1,\n");
		sql.append("               TM_DEALER              TMD2,\n");
		sql.append("               TM_VEHICLE             TMV,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("               vw_org_dealer TMDOR1,\n");
		sql.append("               vw_org_dealer TMDOR2,\n");
		sql.append("               TM_VHCL_MATERIAL       TMVM\n");
		sql.append("         WHERE TTVT.OUT_DEALER_ID = TMD1.DEALER_ID\n");
		sql.append("           AND F_GET_PID(TMD1.DEALER_ID) = TMDOR1.DEALER_ID--F_GET_PID(DEALER_ID)取得本经销商的最上级经销商用于与大区关联\n");
		sql.append("           AND F_GET_PID(TMD2.DEALER_ID) = TMDOR2.DEALER_ID\n");
		sql.append("           AND TTVT.IN_DEALER_ID = TMD2.DEALER_ID\n");
		sql.append("           AND TTVT.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("           AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("           AND TMV.SERIES_ID = G.GROUP_ID\n");// and
		// tmv.lock_status
		// =
		// 10241006
		sql.append("           and tmv.lock_status = ").append(Constant.LOCK_STATUS_PFSD).append("\n");
		// sql.append(" AND (TMDOR1.ORG_ID != TMDOR2.ORG_ID OR TMV.AREA_ID NOT
		// IN (SELECT TDBA.AREA_ID FROM TM_DEALER_BUSINESS_AREA TDBA WHERE
		// TDBA.DEALER_ID = TTVT.OUT_DEALER_ID))\n");
		sql.append("	       AND TMDOR1.ROOT_ORG_ID != TMDOR2.ROOT_ORG_ID");

		// 调出经销商
		if (null != outDealerCode && !"".equals(outDealerCode)) {
			String[] outDealerCodes = outDealerCode.split(",");
			if (null != outDealerCodes && outDealerCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < outDealerCodes.length; i++) {
					buffer.append("'").append(outDealerCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND TMD1.DEALER_CODE  IN (" + buffer.toString() + ") \n");
			}
		}
		// 调入经销商
		if (null != inDealerCode && !"".equals(inDealerCode)) {
			String[] inDealerCodes = inDealerCode.split(",");
			if (null != inDealerCodes && inDealerCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < inDealerCodes.length; i++) {
					buffer.append("'").append(inDealerCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND TMD2.DEALER_CODE  IN (" + buffer.toString() + ") \n");
			}
		}
		// 物料组选择
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				System.out.println(buffer);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                              WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR  SUBSTR(G.TREE_CODE, 0, 3) IN\n");
				sql.append("       (SELECT G.TREE_CODE\n");
				sql.append("           FROM TM_VHCL_MATERIAL_GROUP G\n");
				sql.append("          WHERE G.GROUP_CODE IN (" + buffer.toString() + ")))\n");
			}
		}
		// VIN
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		sql.append("           AND TTVT.CHECK_STATUS = " + Constant.ALLOCATE_STATUS_01 + "\n");
		// sql.append(" UNION\n");
		// sql.append(" SELECT TTVT.TRANSFER_ID,\n");
		// sql.append(" TMD1.DEALER_SHORTNAME AS OUT_DEALER, --调出经销商\n");
		// sql.append(" TMD2.DEALER_SHORTNAME AS IN_DEALER, --调入经销商\n");
		// sql.append(" TTVT.TRANSFER_REASON, --调拨原因\n");
		// sql.append(" TTVT.TRANSFER_NO, --批发号\n");
		// sql.append(" TMV.VIN, --VIN\n");
		// sql.append(" TMV.ENGINE_NO, --发动机号\n");
		// sql.append(" TMVM.MATERIAL_NAME, --物料名称\n");
		// sql.append(" TO_CHAR(TTVT.TRANSFER_DATE, 'YYYY-MM-DD') TRANSFER_DATE,
		// --申请日期\n");
		// sql.append(" TO_CHAR(TMV.STORAGE_DATE, 'YYYY-MM-DD') STORAGE_DATE
		// --首次入库日期\n");
		// sql.append(" FROM TT_VS_VEHICLE_TRANSFER TTVT,\n");
		// sql.append(" TM_DEALER TMD1,\n");
		// sql.append(" TM_DEALER TMD2,\n");
		// sql.append(" TM_DEALER TMD3,\n");
		// sql.append(" TM_DEALER_ORG_RELATION TMDOR1,\n");
		// sql.append(" TM_DEALER_ORG_RELATION TMDOR2,\n");
		// sql.append(" TM_VEHICLE TMV,\n");
		// sql.append(" TM_VHCL_MATERIAL_GROUP G,\n");
		// sql.append(" TM_VHCL_MATERIAL TMVM\n");
		// sql.append(" WHERE TTVT.OUT_DEALER_ID = TMD1.DEALER_ID\n");
		// sql.append(" AND TTVT.IN_DEALER_ID = TMD2.DEALER_ID\n");
		// sql.append(" AND TMD1.PARENT_DEALER_D = TMD3.DEALER_ID\n");
		// sql.append(" AND TMD3.DEALER_ID = TMDOR1.DEALER_ID\n");
		// sql.append(" AND TMD2.DEALER_ID = TMDOR2.DEALER_ID\n");
		// sql.append(" AND TTVT.VEHICLE_ID = TMV.VEHICLE_ID\n");
		// sql.append(" AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		// sql.append(" AND TMV.SERIES_ID = G.GROUP_ID\n");
		// sql.append(" AND TMDOR1.ORG_ID != TMDOR2.ORG_ID\n");
		// //调出经销商
		// if (null != outDealerCode && !"".equals(outDealerCode)) {
		// String[] outDealerCodes = outDealerCode.split(",");
		// if (null != outDealerCodes && outDealerCodes.length>0) {
		// StringBuffer buffer = new StringBuffer();
		// for (int i = 0; i < outDealerCodes.length; i++) {
		// buffer.append("'").append(outDealerCodes[i]).append("'").append(",");
		// }
		// buffer = buffer.deleteCharAt(buffer.length()-1);
		// sql.append(" AND TMD1.DEALER_CODE IN ("+buffer.toString()+") \n");
		// }
		// }
		// //调入经销商
		// if (null != inDealerCode && !"".equals(inDealerCode)) {
		// String[] inDealerCodes = inDealerCode.split(",");
		// if (null != inDealerCodes && inDealerCodes.length>0) {
		// StringBuffer buffer = new StringBuffer();
		// for (int i = 0; i < inDealerCodes.length; i++) {
		// buffer.append("'").append(inDealerCodes[i]).append("'").append(",");
		// }
		// buffer = buffer.deleteCharAt(buffer.length()-1);
		// sql.append(" AND TMD2.DEALER_CODE IN ("+buffer.toString()+") \n");
		// }
		// }
		// //物料组选择
		// if (null != materialCode && !"".equals(materialCode)) {
		// String[] materialCodes = materialCode.split(",");
		// if (null != materialCodes && materialCodes.length>0) {
		// StringBuffer buffer = new StringBuffer();
		// for (int i = 0; i < materialCodes.length; i++) {
		// buffer.append("'").append(materialCodes[i]).append("'").append(",");
		// }
		// buffer = buffer.deleteCharAt(buffer.length()-1);
		// System.out.println(buffer);
		// sql.append(" AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
		// sql.append(" FROM TM_VHCL_MATERIAL_GROUP G \n");
		// sql.append(" WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
		// sql.append(" OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
		// sql.append(" FROM TM_VHCL_MATERIAL_GROUP G \n");
		// sql.append(" WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
		// sql.append(" OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
		// sql.append(" FROM TM_VHCL_MATERIAL_GROUP G \n");
		// sql.append(" WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
		// sql.append(" OR SUBSTR(G.TREE_CODE, 0, 3) IN\n");
		// sql.append(" (SELECT G.TREE_CODE\n");
		// sql.append(" FROM TM_VHCL_MATERIAL_GROUP G\n");
		// sql.append(" WHERE G.GROUP_CODE IN ("+buffer.toString()+")))\n");
		// }
		// }
		// //VIN
		// if (null != vin && !"".equals(vin)) {
		// sql.append(GetVinUtil.getVins(vin,"TMV"));
		// }
		// sql.append(" AND TTVT.CHECK_STATUS = " + Constant.ALLOCATE_STATUS_01
		// + ") T\n");
		sql.append("           ) T\n");
		sql.append("           ORDER BY T.TRANSFER_DATE DESC\n");

		return dao.pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.VehicleAllocateDAO.getVehicleAllocateCheckListA", pageSize, curPage);
	}

	public static PageResult<Map<String, Object>> getVehicleAllocateCheckListB(Long poseId, String orgId, String outDealerCode, String inDealerCode, String materialCode, String vin, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer("");

		/*
		 * sql.append("SELECT T.TRANSFER_ID,\n"); sql.append(" T.OUT_DEALER AS
		 * OUT_DEALER, --调出经销商\n"); sql.append(" T.IN_DEALER AS IN_DEALER,
		 * --调入经销商\n"); sql.append(" T.TRANSFER_REASON, --调拨原因\n"); sql.append("
		 * T.TRANSFER_NO, --批发号\n"); sql.append(" T.VIN, --VIN\n"); sql.append("
		 * T.ENGINE_NO, --发动机号\n"); sql.append(" T.MATERIAL_NAME, --物料名称\n");
		 * sql.append(" T.TRANSFER_DATE, --申请日期\n"); sql.append(" T.STORAGE_DATE
		 * --首次入库日期\n"); sql.append(" FROM (SELECT TTVT.TRANSFER_ID,\n");
		 * sql.append(" TMD1.DEALER_SHORTNAME AS OUT_DEALER, --调出经销商\n");
		 * sql.append(" TMD2.DEALER_SHORTNAME AS IN_DEALER, --调入经销商\n");
		 * sql.append(" TTVT.TRANSFER_REASON, --调拨原因\n"); sql.append("
		 * TTVT.TRANSFER_NO, --批发号\n"); sql.append(" TMV.VIN, --VIN\n");
		 * sql.append(" TMV.ENGINE_NO, --发动机号\n"); sql.append("
		 * TMVM.MATERIAL_NAME, --物料名称\n"); sql.append("
		 * TO_CHAR(TTVT.TRANSFER_DATE, 'YYYY-MM-DD') TRANSFER_DATE, --申请日期\n");
		 * sql.append(" TO_CHAR(TMV.STORAGE_DATE, 'YYYY-MM-DD') STORAGE_DATE
		 * --首次入库日期\n"); sql.append(" FROM TT_VS_VEHICLE_TRANSFER TTVT,\n");
		 * sql.append(" TM_DEALER TMD1,\n"); sql.append(" TM_DEALER TMD2,\n");
		 * sql.append(" TM_VEHICLE TMV,\n"); sql.append(" TM_VHCL_MATERIAL_GROUP
		 * G,\n"); sql.append(" TM_DEALER_ORG_RELATION TMDOR1,\n"); sql.append("
		 * TM_DEALER_ORG_RELATION TMDOR2,\n"); sql.append("
		 * TM_POSE_BUSINESS_AREA TMPBA,\n"); sql.append(" TM_VHCL_MATERIAL
		 * TMVM\n"); sql.append(" WHERE TTVT.OUT_DEALER_ID = TMD1.DEALER_ID\n");
		 * sql.append(" AND F_GET_PID(TMD1.DEALER_ID) = TMDOR1.DEALER_ID\n");
		 * sql.append(" AND F_GET_PID(TMD2.DEALER_ID) = TMDOR2.DEALER_ID\n");
		 * sql.append(" AND TTVT.IN_DEALER_ID = TMD2.DEALER_ID\n"); sql.append("
		 * AND TTVT.VEHICLE_ID = TMV.VEHICLE_ID\n"); sql.append(" AND
		 * TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n"); sql.append(" AND
		 * TMV.SERIES_ID = G.GROUP_ID\n"); sql.append(" AND TMDOR1.ORG_ID =
		 * TMDOR2.ORG_ID\n"); sql.append(" AND TMPBA.AREA_ID = TMV.AREA_ID\n");
		 */
		sql.append("SELECT T.TRANSFER_ID,\n");
		sql.append("       T.OUT_DEALER      AS OUT_DEALER, --调出经销商\n");
		sql.append("       T.IN_DEALER       AS IN_DEALER, --调入经销商\n");
		sql.append("       T.TRANSFER_REASON, --调拨原因\n");
		sql.append("       T.TRANSFER_NO, --批发号\n");
		sql.append("       T.VIN, --VIN\n");
		sql.append("       T.ENGINE_NO, --发动机号\n");
		sql.append("       T.MATERIAL_NAME, --物料名称\n");
		sql.append("       T.TRANSFER_DATE, --申请日期\n");
		sql.append("       T.STORAGE_DATE --首次入库日期\n");
		sql.append("  FROM (SELECT TTVT.TRANSFER_ID,\n");
		sql.append("               TMD11.Dealer_Shortname AS OUT_DEALER, --调出经销商\n");
		sql.append("               TMD22.Dealer_Shortname AS IN_DEALER, --调入经销商\n");
		sql.append("               TTVT.TRANSFER_REASON, --调拨原因\n");
		sql.append("               TTVT.TRANSFER_NO, --批发号\n");
		sql.append("               TMV.VIN, --VIN\n");
		sql.append("               TMV.ENGINE_NO, --发动机号\n");
		sql.append("               TMVM.MATERIAL_NAME, --物料名称\n");
		sql.append("               TO_CHAR(TTVT.TRANSFER_DATE, 'YYYY-MM-DD') TRANSFER_DATE, --申请日期\n");
		sql.append("               TO_CHAR(TMV.STORAGE_DATE, 'YYYY-MM-DD') STORAGE_DATE --首次入库日期\n");
		sql.append("          FROM TT_VS_VEHICLE_TRANSFER TTVT,\n");
		sql.append("               vw_org_dealer          TMD1,\n");
		sql.append("               vw_org_dealer          TMD2,\n");
		sql.append("               tm_dealer              tmd11,\n");
		sql.append("               tm_dealer              tmd22,\n");
		sql.append("               TM_VEHICLE             TMV,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("               TM_POSE_BUSINESS_AREA  TMPBA,\n");
		sql.append("               TM_VHCL_MATERIAL       TMVM\n");
		sql.append("         WHERE TTVT.OUT_DEALER_ID = TMD1.DEALER_ID\n");
		sql.append("           AND TTVT.IN_DEALER_ID = TMD2.DEALER_ID\n");
		sql.append("           and tmd1.dealer_id = tmd11.dealer_id\n");
		sql.append("           and tmd2.dealer_id = tmd22.dealer_id\n");
		sql.append("           AND TTVT.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("           AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("           AND TMV.SERIES_ID = G.GROUP_ID\n");
		sql.append("           AND tmd1.root_org_id = tmd2.root_org_id\n");
		sql.append("           AND TMPBA.AREA_ID = TMV.AREA_ID\n");

		// sql.append(" AND TMV.AREA_ID IN (SELECT TDBA.AREA_ID FROM
		// TM_DEALER_BUSINESS_AREA TDBA WHERE TDBA.DEALER_ID =
		// TTVT.OUT_DEALER_ID)\n");
		sql.append("	       AND TMD1.ROOT_ORG_ID  = TMD2.ROOT_ORG_ID");
		sql.append("	       AND TMD1.PQ_ORG_ID   != TMD2.PQ_ORG_ID");

		sql.append("           AND tmd1.root_ORG_ID = " + orgId + "\n");
		sql.append("           AND TMPBA.POSE_ID = " + poseId + "\n");
		// 调出经销商
		if (null != outDealerCode && !"".equals(outDealerCode)) {
			String[] outDealerCodes = outDealerCode.split(",");
			if (null != outDealerCodes && outDealerCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < outDealerCodes.length; i++) {
					buffer.append("'").append(outDealerCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND TMD1.DEALER_CODE  IN (" + buffer.toString() + ") \n");
			}
		}
		// 调入经销商
		if (null != inDealerCode && !"".equals(inDealerCode)) {
			String[] inDealerCodes = inDealerCode.split(",");
			if (null != inDealerCodes && inDealerCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < inDealerCodes.length; i++) {
					buffer.append("'").append(inDealerCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND TMD2.DEALER_CODE  IN (" + buffer.toString() + ") \n");
			}
		}
		// 物料组选择
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				System.out.println(buffer);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                              WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR  SUBSTR(G.TREE_CODE, 0, 3) IN\n");
				sql.append("       (SELECT G.TREE_CODE\n");
				sql.append("           FROM TM_VHCL_MATERIAL_GROUP G\n");
				sql.append("          WHERE G.GROUP_CODE IN (" + buffer.toString() + ")))\n");
			}
		}
		// VIN
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		sql.append("           AND TTVT.CHECK_STATUS = " + Constant.ALLOCATE_STATUS_01 + "\n");
		// sql.append(" UNION\n");
		// sql.append(" SELECT TTVT.TRANSFER_ID,\n");
		// sql.append(" TMD1.DEALER_SHORTNAME AS OUT_DEALER, --调出经销商\n");
		// sql.append(" TMD2.DEALER_SHORTNAME AS IN_DEALER, --调入经销商\n");
		// sql.append(" TTVT.TRANSFER_REASON, --调拨原因\n");
		// sql.append(" TTVT.TRANSFER_NO, --批发号\n");
		// sql.append(" TMV.VIN, --VIN\n");
		// sql.append(" TMV.ENGINE_NO, --发动机号\n");
		// sql.append(" TMVM.MATERIAL_NAME, --物料名称\n");
		// sql.append(" TO_CHAR(TTVT.TRANSFER_DATE, 'YYYY-MM-DD') TRANSFER_DATE,
		// --申请日期\n");
		// sql.append(" TO_CHAR(TMV.STORAGE_DATE, 'YYYY-MM-DD') STORAGE_DATE
		// --首次入库日期\n");
		// sql.append(" FROM TT_VS_VEHICLE_TRANSFER TTVT,\n");
		// sql.append(" TM_DEALER TMD1,\n");
		// sql.append(" TM_DEALER TMD2,\n");
		// sql.append(" TM_DEALER TMD3,\n");
		// sql.append(" TM_DEALER_ORG_RELATION TMDOR1,\n");
		// sql.append(" TM_DEALER_ORG_RELATION TMDOR2,\n");
		// sql.append(" TM_VEHICLE TMV,\n");
		// sql.append(" TM_VHCL_MATERIAL_GROUP G,\n");
		// sql.append(" TM_VHCL_MATERIAL TMVM\n");
		// sql.append(" WHERE TTVT.OUT_DEALER_ID = TMD1.DEALER_ID\n");
		// sql.append(" AND TTVT.IN_DEALER_ID = TMD2.DEALER_ID\n");
		// sql.append(" AND TMD1.PARENT_DEALER_D = TMD3.DEALER_ID\n");
		// sql.append(" AND TMD3.DEALER_ID = TMDOR1.DEALER_ID\n");
		// sql.append(" AND TMD2.DEALER_ID = TMDOR2.DEALER_ID\n");
		// sql.append(" AND TTVT.VEHICLE_ID = TMV.VEHICLE_ID\n");
		// sql.append(" AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		// sql.append(" AND TMV.SERIES_ID = G.GROUP_ID\n");
		// sql.append(" AND TMDOR1.ORG_ID = TMDOR2.ORG_ID\n");
		// sql.append(" AND TMDOR1.ORG_ID = " + orgId + "\n");
		// //调出经销商
		// if (null != outDealerCode && !"".equals(outDealerCode)) {
		// String[] outDealerCodes = outDealerCode.split(",");
		// if (null != outDealerCodes && outDealerCodes.length>0) {
		// StringBuffer buffer = new StringBuffer();
		// for (int i = 0; i < outDealerCodes.length; i++) {
		// buffer.append("'").append(outDealerCodes[i]).append("'").append(",");
		// }
		// buffer = buffer.deleteCharAt(buffer.length()-1);
		// sql.append(" AND TMD1.DEALER_CODE IN ("+buffer.toString()+") \n");
		// }
		// }
		// //调入经销商
		// if (null != inDealerCode && !"".equals(inDealerCode)) {
		// String[] inDealerCodes = inDealerCode.split(",");
		// if (null != inDealerCodes && inDealerCodes.length>0) {
		// StringBuffer buffer = new StringBuffer();
		// for (int i = 0; i < inDealerCodes.length; i++) {
		// buffer.append("'").append(inDealerCodes[i]).append("'").append(",");
		// }
		// buffer = buffer.deleteCharAt(buffer.length()-1);
		// sql.append(" AND TMD2.DEALER_CODE IN ("+buffer.toString()+") \n");
		// }
		// }
		// //物料组选择
		// if (null != materialCode && !"".equals(materialCode)) {
		// String[] materialCodes = materialCode.split(",");
		// if (null != materialCodes && materialCodes.length>0) {
		// StringBuffer buffer = new StringBuffer();
		// for (int i = 0; i < materialCodes.length; i++) {
		// buffer.append("'").append(materialCodes[i]).append("'").append(",");
		// }
		// buffer = buffer.deleteCharAt(buffer.length()-1);
		// System.out.println(buffer);
		// sql.append(" AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
		// sql.append(" FROM TM_VHCL_MATERIAL_GROUP G \n");
		// sql.append(" WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
		// sql.append(" OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
		// sql.append(" FROM TM_VHCL_MATERIAL_GROUP G \n");
		// sql.append(" WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
		// sql.append(" OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
		// sql.append(" FROM TM_VHCL_MATERIAL_GROUP G \n");
		// sql.append(" WHERE G.GROUP_CODE IN ("+buffer.toString()+"))) \n");
		// sql.append(" OR SUBSTR(G.TREE_CODE, 0, 3) IN\n");
		// sql.append(" (SELECT G.TREE_CODE\n");
		// sql.append(" FROM TM_VHCL_MATERIAL_GROUP G\n");
		// sql.append(" WHERE G.GROUP_CODE IN ("+buffer.toString()+")))\n");
		// }
		// }
		// //VIN
		// if (null != vin && !"".equals(vin)) {
		// sql.append(GetVinUtil.getVins(vin,"TMV"));
		// }
		// sql.append(" AND TTVT.CHECK_STATUS = " + Constant.ALLOCATE_STATUS_01
		// + ") T\n");
		sql.append("           ) T\n");
		sql.append("           ORDER BY T.TRANSFER_DATE DESC\n");

		return dao.pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.VehicleAllocateDAO.getVehicleAllocateCheckListB", pageSize, curPage);
	}

	public static PageResult<Map<String, Object>> getVehicleAllocateCheckListC(Long poseId, String orgId, String outDealerCode, String inDealerCode, String materialCode, String vin, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT T.TRANSFER_ID,\n");
		sql.append("       T.OUT_DEALER      AS OUT_DEALER, --调出经销商\n");
		sql.append("       T.IN_DEALER       AS IN_DEALER, --调入经销商\n");
		sql.append("       T.TRANSFER_REASON, --调拨原因\n");
		sql.append("       T.TRANSFER_NO, --批发号\n");
		sql.append("       T.VIN, --VIN\n");
		sql.append("       T.ENGINE_NO, --发动机号\n");
		sql.append("       T.MATERIAL_NAME, --物料名称\n");
		sql.append("       T.TRANSFER_DATE, --申请日期\n");
		sql.append("       T.STORAGE_DATE --首次入库日期\n");
		sql.append("  FROM (SELECT TTVT.TRANSFER_ID,\n");
		sql.append("               TMD11.Dealer_Shortname AS OUT_DEALER, --调出经销商\n");
		sql.append("               TMD22.Dealer_Shortname AS IN_DEALER, --调入经销商\n");
		sql.append("               TTVT.TRANSFER_REASON, --调拨原因\n");
		sql.append("               TTVT.TRANSFER_NO, --批发号\n");
		sql.append("               TMV.VIN, --VIN\n");
		sql.append("               TMV.ENGINE_NO, --发动机号\n");
		sql.append("               TMVM.MATERIAL_NAME, --物料名称\n");
		sql.append("               TO_CHAR(TTVT.TRANSFER_DATE, 'YYYY-MM-DD') TRANSFER_DATE, --申请日期\n");
		sql.append("               TO_CHAR(TMV.STORAGE_DATE, 'YYYY-MM-DD') STORAGE_DATE --首次入库日期\n");
		sql.append("          FROM TT_VS_VEHICLE_TRANSFER TTVT,\n");
		sql.append("               vw_org_dealer          TMD1,\n");
		sql.append("               vw_org_dealer          TMD2,\n");
		sql.append("               tm_dealer              tmd11,\n");
		sql.append("               tm_dealer              tmd22,\n");
		sql.append("               TM_VEHICLE             TMV,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("               TM_POSE_BUSINESS_AREA  TMPBA,\n");
		sql.append("               TM_VHCL_MATERIAL       TMVM\n");
		sql.append("         WHERE TTVT.OUT_DEALER_ID = TMD1.DEALER_ID\n");
		sql.append("           AND TTVT.IN_DEALER_ID = TMD2.DEALER_ID\n");
		sql.append("           and tmd1.dealer_id = tmd11.dealer_id\n");
		sql.append("           and tmd2.dealer_id = tmd22.dealer_id\n");
		sql.append("           AND TTVT.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("           AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("           AND TMV.SERIES_ID = G.GROUP_ID\n");
		sql.append("           AND tmd1.root_org_id = tmd2.root_org_id\n");
		sql.append("           AND TMPBA.AREA_ID = TMV.AREA_ID\n");

		// sql.append(" AND TMV.AREA_ID IN (SELECT TDBA.AREA_ID FROM
		// TM_DEALER_BUSINESS_AREA TDBA WHERE TDBA.DEALER_ID =
		// TTVT.OUT_DEALER_ID)\n");
		sql.append("	       AND TMD1.PQ_ORG_ID   = TMD2.PQ_ORG_ID");

		sql.append("           AND tmd1.PQ_ORG_ID = " + orgId + "\n");
		sql.append("           AND TMPBA.POSE_ID = " + poseId + "\n");
		// 调出经销商
		if (null != outDealerCode && !"".equals(outDealerCode)) {
			String[] outDealerCodes = outDealerCode.split(",");
			if (null != outDealerCodes && outDealerCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < outDealerCodes.length; i++) {
					buffer.append("'").append(outDealerCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND TMD1.DEALER_CODE  IN (" + buffer.toString() + ") \n");
			}
		}
		// 调入经销商
		if (null != inDealerCode && !"".equals(inDealerCode)) {
			String[] inDealerCodes = inDealerCode.split(",");
			if (null != inDealerCodes && inDealerCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < inDealerCodes.length; i++) {
					buffer.append("'").append(inDealerCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND TMD2.DEALER_CODE  IN (" + buffer.toString() + ") \n");
			}
		}
		// 物料组选择
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				System.out.println(buffer);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                              WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR  SUBSTR(G.TREE_CODE, 0, 3) IN\n");
				sql.append("       (SELECT G.TREE_CODE\n");
				sql.append("           FROM TM_VHCL_MATERIAL_GROUP G\n");
				sql.append("          WHERE G.GROUP_CODE IN (" + buffer.toString() + ")))\n");
			}
		}
		// VIN
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		sql.append("           AND TTVT.CHECK_STATUS = " + Constant.ALLOCATE_STATUS_01 + "\n");

		sql.append("           ) T\n");
		sql.append("           ORDER BY T.TRANSFER_DATE DESC\n");

		return dao.pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.VehicleAllocateDAO.getVehicleAllocateCheckListB", pageSize, curPage);
	}

	public static List<Map<String, Object>> selectAreaIdByDlr(String dealerId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TDBA.AREA_ID\n");
		sql.append("  FROM TM_DEALER_BUSINESS_AREA TDBA\n");
		sql.append(" WHERE 1 = 1\n");
		sql.append("   AND TDBA.dealer_id = " + dealerId + "\n");
		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return rs;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
