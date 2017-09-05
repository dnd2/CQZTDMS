package com.infodms.dms.dao.sales.storageManage;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.reqquery.ReqQueryDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class StorageQueryDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(StorageQueryDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO();

	public static final CheckVehicleDAO getInstance() {
		return dao;
	}

	/***************************************************************************
	 * 经销商库存查询：汇总查询
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getMySumList(String areaId, String vehicleOwn, String whName, String dealerId, String materialCode, String materialCode__, String days, String vin, String vehicle_life, String lockStatus) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       G.GROUP_NAME AS SERIES_NAME, --车系\n");
		sql.append("       G1.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G2.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " THEN --经销商在途\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS ON_WAY,\n");
		sql.append("       SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " THEN --经销商库存\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS NO_WAY,\n");
		sql.append("      SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ") THEN --合计\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS SUM_NO\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G2\n");
		sql.append(" WHERE TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.MODEL_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G2.GROUP_ID\n");
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (areaId != null && !"".equals(areaId)) {
			sql.append("   AND TMV.AREA_ID IN (" + areaId + ")");
		}
		if (vehicle_life.equals(Constant.VEHICLE_LIFE_05.toString())) {
			sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " \n");
		} else if (vehicle_life.equals(Constant.VEHICLE_LIFE_03.toString())) {
			sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " \n");
		} else {
			sql.append("   AND TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ")\n");
		}

		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
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
		if (null != materialCode__ && !"".equals(materialCode__)) {
			String[] array = materialCode__.split(",");
			sql.append("   AND TMVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.STORAGE_DATE)>=" + days.trim() + ")\n");
		}

		if (null != vehicleOwn && ("01").equals(vehicleOwn)) { // 自有车
			sql.append("   AND TMV.DEALER_ID IN (" + dealerId + ")\n");
		} else if (null != vehicleOwn && ("02").equals(vehicleOwn)) { // 代管车
			sql.append("   AND TMV.VEHICLE_AREA IN\n");
			sql.append("       (SELECT WAREHOUSE_ID\n");
			sql.append("          FROM TM_DEALER_WAREHOUSE\n");
			sql.append("         WHERE TM_DEALER_WAREHOUSE.MANAGE_DEALER_ID IN (" + dealerId + "))\n");
		} else { // 全部
			sql.append("   AND (TMV.DEALER_ID IN (" + dealerId + ") OR\n");
			sql.append("       TMV.VEHICLE_AREA IN\n");
			sql.append("       (SELECT WAREHOUSE_ID\n");
			sql.append("           FROM TM_DEALER_WAREHOUSE\n");
			sql.append("          WHERE TM_DEALER_WAREHOUSE.MANAGE_DEALER_ID IN (" + dealerId + ")))\n");
		}
		if (null != whName && !"".equals(whName)) {
			sql.append("   AND TMV.VEHICLE_AREA = " + whName + "\n");
		}
		if (null != lockStatus && !"".equals(lockStatus)) {
			sql.append("   AND TMV.LOCK_STATUS = " + lockStatus + "\n");
		}
		sql.append(" GROUP BY TMVM.MATERIAL_NAME, G.GROUP_NAME, G1.GROUP_NAME, G2.GROUP_NAME, TMVM.MATERIAL_CODE\n");
		sql.append(" ORDER BY TMVM.MATERIAL_CODE\n");

		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return ps;
	}

	public static List<Map<String, Object>> getMySumList_SUZUKI(String areaId, String whName, String dealerId, String materialCode, String materialCode__, String days, String vin, String vehicle_life, String lockStatus,String startDate,String endDate) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       G.GROUP_NAME AS SERIES_NAME, --车系\n");
		sql.append("       G1.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G2.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " THEN --经销商在途\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS ON_WAY,\n");
		sql.append("       SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " THEN --经销商库存\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS NO_WAY,\n");
		sql.append("      SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ") THEN --合计\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS SUM_NO\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G2\n");
		sql.append(" WHERE TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.MODEL_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G2.GROUP_ID\n");
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (areaId != null && !"".equals(areaId)) {
			sql.append("   AND TMV.AREA_ID IN (" + areaId + ")");
		}
		if (vehicle_life.equals(Constant.VEHICLE_LIFE_05.toString())) {
			sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " \n");
		} else if (vehicle_life.equals(Constant.VEHICLE_LIFE_03.toString())) {
			sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " \n");
		} else {
			sql.append("   AND TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ")\n");
		}

		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
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
		if (null != materialCode__ && !"".equals(materialCode__)) {
			String[] array = materialCode__.split(",");
			sql.append("   AND TMVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.STORAGE_DATE)>=" + days.trim() + ")\n");
		}

		sql.append("   AND TMV.VEHICLE_AREA IN\n");
		sql.append("       (SELECT WAREHOUSE_ID\n");
		sql.append("          FROM TM_DEALER_WAREHOUSE\n");
		sql.append("         WHERE TM_DEALER_WAREHOUSE.DEALER_ID IN (" + dealerId + "))\n");

		if (null != whName && !"".equals(whName)) {
			sql.append("   AND TMV.VEHICLE_AREA = " + whName + "\n");
		}
		if (null != lockStatus && !"".equals(lockStatus)) {
			sql.append("   AND TMV.LOCK_STATUS = " + lockStatus + "\n");
		}
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TMV.STORAGE_date >= TO_DATE( '" + startDate + "','yyyy-mm-DD')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TMV.STORAGE_date <= TO_DATE( '" + endDate + "','yyyy-mm-DD')\n");
		}
		sql.append(" GROUP BY TMVM.MATERIAL_NAME, G.GROUP_NAME, G1.GROUP_NAME, G2.GROUP_NAME, TMVM.MATERIAL_CODE\n");
		sql.append(" ORDER BY TMVM.MATERIAL_CODE\n");

		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return ps;
	}

	public static List<Map<String, Object>> getMySumList_Lower(String dealerIds, String upDealerOrder, String downDealerOrder, String areaId, String vehicleOwn, String whName, String dealerId, String materialCode, String materialCode__, String days, String vin, String vehicle_life, String lockStatus) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       G.GROUP_NAME AS SERIES_NAME, --车系\n");
		sql.append("       G1.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G2.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " THEN --经销商在途\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS ON_WAY,\n");
		sql.append("       SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " THEN --经销商库存\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS NO_WAY,\n");
		sql.append("      SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ") THEN --合计\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS SUM_NO,\n");
		sql.append("       TMD.DEALER_SHORTNAME\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G2,vw_org_dealer VOD1,\n");
		sql.append("       TM_DEALER TMD\n");
		if ((upDealerOrder != null && !upDealerOrder.equals("")) || (downDealerOrder != null && !downDealerOrder.equals(""))) {
			sql.append(" ,TT_VS_ORDER     TVO,\n");
			sql.append(" TT_VS_DLVRY_MCH TVDM,\n");
			sql.append(" TT_VS_DLVRY_DTL TVDD,\n");
			sql.append(" TT_VS_DLVRY_REQ TVDR\n");
		}

		sql.append(" WHERE TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.MODEL_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G2.GROUP_ID AND VOD1.DEALER_ID=TMD.DEALER_ID\n");
		sql.append("AND TMV.DEALER_ID = TMD.DEALER_ID\n");
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (areaId != null && !"".equals(areaId)) {
			sql.append("   AND TMV.AREA_ID IN (" + areaId + ")");
		}
		if (vehicle_life.equals(Constant.VEHICLE_LIFE_05.toString())) {
			sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " \n");
		} else if (vehicle_life.equals(Constant.VEHICLE_LIFE_03.toString())) {
			sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " \n");
		} else {
			sql.append("   AND TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ")\n");
		}

		if ((upDealerOrder == null || upDealerOrder.equals("")) && (downDealerOrder == null || downDealerOrder.equals("")) && (dealerIds == null || dealerIds.equals(""))) {
			sql.append("   AND TMD.DEALER_ID IN ( SELECT VOD.DEALER_ID FROM vw_org_dealer VOD WHERE VOD.ROOT_DEALER_ID IN (" + dealerId + ")) \n");
		}
		if (dealerIds != null && !"".equals(dealerIds)) {
			sql.append("   AND VOD1.DEALER_ID IN (" + dealerIds + ") \n");
		}
		if (upDealerOrder != null && !upDealerOrder.equals("")) {
			sql.append(" AND TMD.DEALER_ID IN (" + dealerId + ")\n");
			sql.append("AND TMV.VEHICLE_ID = TVDM.VEHICLE_ID\n");
			sql.append("  AND TVDD.DETAIL_ID = TVDM.DELIVERY_DETAIL_ID\n");
			sql.append("  AND TVDR.REQ_ID = TVDD.REQ_ID\n");
			sql.append(" AND TVDR.ORDER_ID = TVO.ORDER_ID\n");
			sql.append("   AND TVO.ORDER_NO LIKE '%" + upDealerOrder + "%'\n");
		}
		if (downDealerOrder != null && !downDealerOrder.equals("")) {
			sql.append("   AND TMD.PARENT_DEALER_D IN (" + dealerId + ") \n");
			sql.append("AND TMV.VEHICLE_ID = TVDM.VEHICLE_ID\n");
			sql.append("  AND TVDD.DETAIL_ID = TVDM.DELIVERY_DETAIL_ID\n");
			sql.append("  AND TVDR.REQ_ID = TVDD.REQ_ID\n");
			sql.append(" AND TVDR.ORDER_ID = TVO.ORDER_ID\n");
			sql.append("   AND TVO.ORDER_NO LIKE '%" + downDealerOrder + "%'\n");
		}

		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
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
		if (null != materialCode__ && !"".equals(materialCode__)) {
			String[] array = materialCode__.split(",");
			sql.append("   AND TMVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.STORAGE_DATE)>=" + days.trim() + ")\n");
		}

		// sql.append(" AND TMD.PARENT_DEALER_D IN ("+dealerId+")\n");
		if (null != whName && !"".equals(whName)) {
			sql.append("   AND TMV.VEHICLE_AREA = " + whName + "\n");
		}
		if (null != lockStatus && !"".equals(lockStatus)) {
			sql.append("   AND TMV.LOCK_STATUS = " + lockStatus + "\n");
		}
		sql.append(" GROUP BY TMD.DEALER_SHORTNAME,TMVM.MATERIAL_NAME, G.GROUP_NAME, G1.GROUP_NAME, G2.GROUP_NAME, TMVM.MATERIAL_CODE\n");
		sql.append(" ORDER BY TMD.DEALER_SHORTNAME,TMVM.MATERIAL_CODE\n");

		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return ps;
	}

	/***************************************************************************
	 * 经销商库存查询：汇总查询
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getSumList(Long poseId, String areaId, String vehicleOwn, String whName, String dealerId, String materialCode, String materialCode__, String days, String vin, String vehicle_life, String lockStatus, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       G.GROUP_NAME AS SERIES_NAME, --车系\n");
		sql.append("       G1.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G2.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " THEN --经销商在途\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS ON_WAY,\n");
		sql.append("       SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " THEN --经销商库存\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS NO_WAY,\n");
		sql.append("      SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ") THEN --合计\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS SUM_NO\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G2\n");

		sql.append(" WHERE TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.MODEL_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G2.GROUP_ID\n");
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		// if( areaId!=null && !"".equals(areaId)){
		// sql.append(" AND TMV.DEALER_ID=TDBA.DEALER_ID");
		// sql.append(" AND TDBA.AREA_ID="+areaId);
		// }
		if (areaId != null && !"".equals(areaId)) {
			sql.append("   AND TMV.AREA_ID IN (" + areaId + ")\n");
		}
		if (vehicle_life.equals(Constant.VEHICLE_LIFE_05.toString())) {
			sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " \n");
		} else if (vehicle_life.equals(Constant.VEHICLE_LIFE_03.toString())) {
			sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " \n");
		} else {
			sql.append("   AND TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ")\n");
		}

		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
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
		if (null != materialCode__ && !"".equals(materialCode__)) {
			String[] array = materialCode__.split(",");
			sql.append("   AND TMVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.STORAGE_DATE)>=" + days.trim() + ")\n");
		}

		if (null != vehicleOwn && ("01").equals(vehicleOwn)) { // 自有车
			sql.append("   AND TMV.DEALER_ID IN (" + dealerId + ")\n");
		} else if (null != vehicleOwn && ("02").equals(vehicleOwn)) { // 代管车
			sql.append("   AND TMV.VEHICLE_AREA IN\n");
			sql.append("       (SELECT WAREHOUSE_ID\n");
			sql.append("          FROM TM_DEALER_WAREHOUSE\n");
			sql.append("         WHERE TM_DEALER_WAREHOUSE.MANAGE_DEALER_ID IN (" + dealerId + "))\n");
		} else { // 全部
			sql.append("   AND (TMV.DEALER_ID IN (" + dealerId + ") OR\n");
			sql.append("       TMV.VEHICLE_AREA IN\n");
			sql.append("       (SELECT WAREHOUSE_ID\n");
			sql.append("           FROM TM_DEALER_WAREHOUSE\n");
			sql.append("          WHERE TM_DEALER_WAREHOUSE.MANAGE_DEALER_ID IN (" + dealerId + ")))\n");
		}
		if (null != whName && !"".equals(whName)) {
			sql.append("   AND TMV.VEHICLE_AREA = " + whName + "\n");
		}
		if (null != lockStatus && !"".equals(lockStatus)) {
			sql.append("   AND TMV.LOCK_STATUS = " + lockStatus + "\n");
		}
		sql.append(" GROUP BY TMVM.MATERIAL_NAME, G.GROUP_NAME, G1.GROUP_NAME, G2.GROUP_NAME, TMVM.MATERIAL_CODE\n");
		sql.append(" ORDER BY TMVM.MATERIAL_CODE\n");

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}

	public static PageResult<Map<String, Object>> getSumList_SUZUKI(Long poseId, String areaId, String whName, String dealerId, String materialCode, String materialCode__, String days, String vin, String vehicle_life, String lockStatus,String startDate,String endDate, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       G.GROUP_NAME AS SERIES_NAME, --车系\n");
		sql.append("       G1.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G2.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " THEN --经销商在途\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS ON_WAY,\n");
		sql.append("       SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " THEN --经销商库存\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS NO_WAY,\n");
		sql.append("      SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ") THEN --合计\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS SUM_NO\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G2\n");

		sql.append(" WHERE TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.MODEL_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G2.GROUP_ID\n");
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		// if( areaId!=null && !"".equals(areaId)){
		// sql.append(" AND TMV.DEALER_ID=TDBA.DEALER_ID");
		// sql.append(" AND TDBA.AREA_ID="+areaId);
		// }
		if (areaId != null && !"".equals(areaId)) {
			sql.append("   AND TMV.AREA_ID IN (" + areaId + ")\n");
		}
		if (vehicle_life.equals(Constant.VEHICLE_LIFE_05.toString())) {
			sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " \n");
		} else if (vehicle_life.equals(Constant.VEHICLE_LIFE_03.toString())) {
			sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " \n");
		} else {
			sql.append("   AND TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ")\n");
		}

		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
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
		if (null != materialCode__ && !"".equals(materialCode__)) {
			String[] array = materialCode__.split(",");
			sql.append("   AND TMVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.STORAGE_DATE)>=" + days.trim() + ")\n");
		}

		sql.append("   AND TMV.VEHICLE_AREA IN\n");
		sql.append("       (SELECT WAREHOUSE_ID\n");
		sql.append("          FROM TM_DEALER_WAREHOUSE\n");
		sql.append("         WHERE TM_DEALER_WAREHOUSE.DEALER_ID IN (" + dealerId + "))\n");

		if (null != whName && !"".equals(whName)) {
			sql.append("   AND TMV.VEHICLE_AREA = " + whName + "\n");
		}
		if (null != lockStatus && !"".equals(lockStatus)) {
			sql.append("   AND TMV.LOCK_STATUS = " + lockStatus + "\n");
		}
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TMV.STORAGE_date >= TO_DATE( '" + startDate + "','yyyy-mm-DD')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TMV.STORAGE_date <= TO_DATE( '" + endDate + "','yyyy-mm-DD')\n");
		}
		sql.append(" GROUP BY TMVM.MATERIAL_NAME, G.GROUP_NAME, G1.GROUP_NAME, G2.GROUP_NAME, TMVM.MATERIAL_CODE\n");
		sql.append(" ORDER BY TMVM.MATERIAL_CODE\n");

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}

	public static PageResult<Map<String, Object>> getSumList_Lower(String dealerIds, String upDealerOrder, String downDealerOrder, String areaId, String vehicleOwn, String whName, String dealerId, String materialCode, String materialCode__, String days, String vin, String vehicle_life, String lockStatus, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       G.GROUP_NAME AS SERIES_NAME, --车系\n");
		sql.append("       G1.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G2.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " THEN --经销商在途\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS ON_WAY,\n");
		sql.append("       SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " THEN --经销商库存\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS NO_WAY,\n");
		sql.append("      SUM(CASE\n");
		sql.append("             WHEN TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ") THEN --合计\n");
		sql.append("              1\n");
		sql.append("             ELSE\n");
		sql.append("              0\n");
		sql.append("           END) AS SUM_NO,\n");
		sql.append("       TMD.DEALER_SHORTNAME\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G2,\n");
		sql.append("       TM_DEALER TMD,vw_org_dealer VOD1\n");
		if ((upDealerOrder != null && !upDealerOrder.equals("")) || (downDealerOrder != null && !downDealerOrder.equals(""))) {
			sql.append(",TT_VS_ORDER     TVO,\n");
			sql.append("TT_VS_DLVRY_MCH TVDM,\n");
			sql.append("TT_VS_DLVRY_DTL TVDD,\n");
			sql.append("TT_VS_DLVRY_REQ TVDR\n");
		}
		sql.append(" WHERE TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.MODEL_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G2.GROUP_ID\n");
		sql.append("   AND TMV.DEALER_ID = TMD.DEALER_ID AND VOD1.DEALER_ID=TMD.DEALER_ID\n");

		if ((upDealerOrder == null || upDealerOrder.equals("")) && (downDealerOrder == null || downDealerOrder.equals("")) && (dealerIds == null || dealerIds.equals(""))) {
			sql.append("   AND TMD.DEALER_ID IN ( SELECT VOD.DEALER_ID FROM vw_org_dealer VOD WHERE VOD.ROOT_DEALER_ID IN (" + dealerId + ")) \n");
		}
		if (dealerIds != null && !"".equals(dealerIds)) {
			sql.append("   AND VOD1.DEALER_ID IN (" + dealerIds + ") \n");
		}
		if (upDealerOrder != null && !upDealerOrder.equals("")) {
			sql.append(" AND TMD.DEALER_ID IN (" + dealerId + ")\n");
			sql.append("AND TMV.VEHICLE_ID = TVDM.VEHICLE_ID\n");
			sql.append("  AND TVDD.DETAIL_ID = TVDM.DELIVERY_DETAIL_ID\n");
			sql.append("  AND TVDR.REQ_ID = TVDD.REQ_ID\n");
			sql.append(" AND TVDR.ORDER_ID = TVO.ORDER_ID\n");
			sql.append("   AND TVO.ORDER_NO LIKE '%" + upDealerOrder + "%'\n");
		}
		if (downDealerOrder != null && !downDealerOrder.equals("")) {
			sql.append("   AND TMD.PARENT_DEALER_D IN (" + dealerId + ") \n");
			sql.append("AND TMV.VEHICLE_ID = TVDM.VEHICLE_ID\n");
			sql.append("  AND TVDD.DETAIL_ID = TVDM.DELIVERY_DETAIL_ID\n");
			sql.append("  AND TVDR.REQ_ID = TVDD.REQ_ID\n");
			sql.append(" AND TVDR.ORDER_ID = TVO.ORDER_ID\n");
			sql.append("   AND TVO.ORDER_NO LIKE '%" + downDealerOrder + "%'\n");
		}

		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		// if( areaId!=null && !"".equals(areaId)){
		// sql.append(" AND TMV.DEALER_ID=TDBA.DEALER_ID");
		// sql.append(" AND TDBA.AREA_ID="+areaId);
		// }
		if (areaId != null && !"".equals(areaId)) {
			sql.append("   AND TMV.AREA_ID IN (" + areaId + ")\n");
		}
		if (vehicle_life.equals(Constant.VEHICLE_LIFE_05.toString())) {
			sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " \n");
		} else if (vehicle_life.equals(Constant.VEHICLE_LIFE_03.toString())) {
			sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " \n");
		} else {
			sql.append("   AND TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ")\n");
		}

		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
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
		if (null != materialCode__ && !"".equals(materialCode__)) {
			String[] array = materialCode__.split(",");
			sql.append("   AND TMVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.STORAGE_DATE)>=" + days.trim() + ")\n");
		}

		if (null != vehicleOwn && ("01").equals(vehicleOwn)) { // 自有车
			sql.append("   AND TMV.DEALER_ID IN (" + dealerId + ")\n");
		} else if (null != vehicleOwn && ("02").equals(vehicleOwn)) { // 代管车
			sql.append("   AND TMV.VEHICLE_AREA IN\n");
			sql.append("       (SELECT WAREHOUSE_ID\n");
			sql.append("          FROM TM_DEALER_WAREHOUSE\n");
			sql.append("         WHERE TM_DEALER_WAREHOUSE.MANAGE_DEALER_ID IN (" + dealerId + "))\n");
		}
		if (null != whName && !"".equals(whName)) {
			sql.append("   AND TMV.VEHICLE_AREA = " + whName + "\n");
		}
		if (null != lockStatus && !"".equals(lockStatus)) {
			sql.append("   AND TMV.LOCK_STATUS = " + lockStatus + "\n");
		}
		sql.append(" GROUP BY TMVM.MATERIAL_NAME, G.GROUP_NAME, G1.GROUP_NAME, G2.GROUP_NAME, TMVM.MATERIAL_CODE,TMD.DEALER_SHORTNAME\n");
		sql.append(" ORDER BY TMVM.MATERIAL_CODE\n");

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}

	public static List<Map<String, Object>> getMyStorage_DLR(String areaId, String vehicleOwn, String whName, String dealerId, String materialCode, String materialCode__, String days, String vin, String vehicle_life, String lockStatus) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TMV.BATCH_NO, --批次号\n");
		sql.append("       TDW.WAREHOUSE_NAME, --位置说明\n");
		sql.append("       TMD.DEALER_SHORTNAME AS DEALER_SHORTNAME, \n");
		sql.append("       TC1.CODE_DESC LIFE_CYCLE, --库存状态\n");
		sql.append("       TC2.CODE_DESC LOCK_STATUS, --锁定状态\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') AS PRODUCT_DATE, --生产日期\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_TIME, --入库日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT --库存天数\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_DEALER TMD, \n");
		sql.append("       TM_DEALER_WAREHOUSE TDW, \n");
		sql.append("       TC_CODE 				TC1, \n");
		sql.append("       TC_CODE				TC2 \n");
		if (vehicle_life.equals(Constant.VEHICLE_LIFE_05.toString())) {
			sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " \n");
		} else if (vehicle_life.equals(Constant.VEHICLE_LIFE_03.toString())) {
			sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " \n");
		} else {
			sql.append(" WHERE TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ")\n");
		}
		// sql.append(" AND TMV.DEALER_ID IN ("+dealerId+")\n");
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.DEALER_ID = TMD.DEALER_ID \n");
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID(+) \n");
		sql.append("   AND TMV.LIFE_CYCLE = TC1.CODE_ID \n");
		sql.append("   AND TMV.LOCK_STATUS = TC2.CODE_ID \n");
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
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

		if (null != materialCode__ && !"".equals(materialCode__)) {
			String[] array = materialCode__.split(",");
			sql.append("   AND TMVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.STORAGE_DATE)>=" + days.trim() + ")\n");
		}
		if (null != vehicleOwn && ("01").equals(vehicleOwn)) { // 自有车
			sql.append("   AND TMV.DEALER_ID IN (" + dealerId + ")\n");
		} else if (null != vehicleOwn && ("02").equals(vehicleOwn)) { // 代管车
			sql.append("   AND TMV.VEHICLE_AREA IN\n");
			sql.append("       (SELECT WAREHOUSE_ID\n");
			sql.append("          FROM TM_DEALER_WAREHOUSE\n");
			sql.append("         WHERE TM_DEALER_WAREHOUSE.MANAGE_DEALER_ID IN (" + dealerId + "))\n");
		} else { // 全部
			sql.append("   AND (TMV.DEALER_ID IN (" + dealerId + ") OR\n");
			sql.append("       TMV.VEHICLE_AREA IN\n");
			sql.append("       (SELECT WAREHOUSE_ID\n");
			sql.append("           FROM TM_DEALER_WAREHOUSE\n");
			sql.append("          WHERE TM_DEALER_WAREHOUSE.MANAGE_DEALER_ID IN (" + dealerId + ")))\n");
		}
		if (null != whName && !"".equals(whName)) {
			sql.append("   AND TMV.VEHICLE_AREA = " + whName + "\n");
		}
		if (null != lockStatus && !"".equals(lockStatus)) {
			sql.append("   AND TMV.LOCK_STATUS = " + lockStatus + "\n");
		}
		// if( areaId!=null && !"".equals(areaId)){
		// sql.append(" AND TMD.DEALER_ID=TDBA.DEALER_ID");
		// sql.append(" AND TDBA.AREA_ID="+areaId);
		// }

		if (areaId != null && !"".equals(areaId)) {
			sql.append("   AND TMV.AREA_ID IN (" + areaId + ")");
		}
		sql.append("ORDER BY TMVM.MATERIAL_CODE,TMV.LIFE_CYCLE,STORAGE_TIME \n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());

	}

	public static List<Map<String, Object>> getMyStorage_DLR_SUZUKI(String areaId, String whName, String dealerId, String materialCode, String materialCode__, String days, String vin, String vehicle_life, String lockStatus,String startDate,String endDate) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TMV.BATCH_NO, --批次号\n");
		sql.append("       TDW.WAREHOUSE_NAME, --位置说明\n");
		sql.append("       TMD.DEALER_SHORTNAME AS DEALER_SHORTNAME, \n");
		sql.append("       TC1.CODE_DESC LIFE_CYCLE, --库存状态\n");
		sql.append("       TC2.CODE_DESC LOCK_STATUS, --锁定状态\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') AS PRODUCT_DATE, --生产日期\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_TIME, --入库日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT --库存天数\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_DEALER TMD, \n");
		sql.append("       TM_DEALER_WAREHOUSE TDW, \n");
		sql.append("       TC_CODE 				TC1, \n");
		sql.append("       TC_CODE				TC2 \n");
		if (vehicle_life.equals(Constant.VEHICLE_LIFE_05.toString())) {
			sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " \n");
		} else if (vehicle_life.equals(Constant.VEHICLE_LIFE_03.toString())) {
			sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " \n");
		} else {
			sql.append(" WHERE TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ")\n");
		}
		// sql.append(" AND TMV.DEALER_ID IN ("+dealerId+")\n");
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TDW.DEALER_ID = TMD.DEALER_ID \n");
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID(+) \n");
		sql.append("   AND TMV.LIFE_CYCLE = TC1.CODE_ID \n");
		sql.append("   AND TMV.LOCK_STATUS = TC2.CODE_ID \n");
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
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

		if (null != materialCode__ && !"".equals(materialCode__)) {
			String[] array = materialCode__.split(",");
			sql.append("   AND TMVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.STORAGE_DATE)>=" + days.trim() + ")\n");
		}
		sql.append("   AND TMV.VEHICLE_AREA IN\n");
		sql.append("       (SELECT WAREHOUSE_ID\n");
		sql.append("          FROM TM_DEALER_WAREHOUSE\n");
		sql.append("         WHERE TM_DEALER_WAREHOUSE.DEALER_ID IN (" + dealerId + "))\n");

		if (null != whName && !"".equals(whName)) {
			sql.append("   AND TMV.VEHICLE_AREA = " + whName + "\n");
		}
		if (null != lockStatus && !"".equals(lockStatus)) {
			sql.append("   AND TMV.LOCK_STATUS = " + lockStatus + "\n");
		}
		// if( areaId!=null && !"".equals(areaId)){
		// sql.append(" AND TMD.DEALER_ID=TDBA.DEALER_ID");
		// sql.append(" AND TDBA.AREA_ID="+areaId);
		// }

		if (areaId != null && !"".equals(areaId)) {
			sql.append("   AND TMV.AREA_ID IN (" + areaId + ")");
		}
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TMV.STORAGE_date >= TO_DATE( '" + startDate + "','yyyy-mm-DD')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TMV.STORAGE_date <= TO_DATE( '" + endDate + "','yyyy-mm-DD')\n");
		}
		sql.append("ORDER BY TMVM.MATERIAL_CODE,TMV.LIFE_CYCLE,STORAGE_TIME \n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());

	}

	public static List<Map<String, Object>> getMyStorage_DLR_Lower(String dealerIds, String upDealerOrder, String downDealerOrder, String areaId, String vehicleOwn, String whName, String dealerId, String materialCode, String materialCode__, String days, String vin, String vehicle_life, String lockStatus) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TMV.BATCH_NO, --批次号\n");
		sql.append("       TDW.WAREHOUSE_NAME, --位置说明\n");
		sql.append("       TMD.DEALER_SHORTNAME AS DEALER_SHORTNAME, \n");
		sql.append("       TC1.CODE_DESC LIFE_CYCLE, --库存状态\n");
		sql.append("       TC2.CODE_DESC LOCK_STATUS, --锁定状态\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') AS PRODUCT_DATE, --生产日期\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_TIME, --入库日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT --库存天数\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_DEALER TMD, \n");
		sql.append("       TM_DEALER_WAREHOUSE TDW, \n");
		sql.append("       TC_CODE 				TC1, \n");
		sql.append("       TC_CODE				TC2,vw_org_dealer VOD1 \n");
		if ((upDealerOrder != null && !upDealerOrder.equals("")) || (downDealerOrder != null && !downDealerOrder.equals(""))) {
			sql.append(",TT_VS_ORDER     TVO,\n");
			sql.append("TT_VS_DLVRY_MCH TVDM,\n");
			sql.append("TT_VS_DLVRY_DTL TVDD,\n");
			sql.append("TT_VS_DLVRY_REQ TVDR\n");
		}

		if (vehicle_life.equals(Constant.VEHICLE_LIFE_05.toString())) {
			sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " \n");
		} else if (vehicle_life.equals(Constant.VEHICLE_LIFE_03.toString())) {
			sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " \n");
		} else {
			sql.append(" WHERE TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ")\n");
		}

		if ((upDealerOrder == null || upDealerOrder.equals("")) && (downDealerOrder == null || downDealerOrder.equals("")) && (dealerIds == null || dealerIds.equals(""))) {
			sql.append("   AND TMD.DEALER_ID IN ( SELECT VOD.DEALER_ID FROM vw_org_dealer VOD WHERE VOD.ROOT_DEALER_ID IN (" + dealerId + ")) \n");
		}
		if (dealerIds != null && !"".equals(dealerIds)) {
			sql.append("   AND VOD1.DEALER_ID IN (" + dealerIds + ")  AND VOD1.DEALER_ID=TMD.DEALER_ID \n");
		}
		if (upDealerOrder != null && !upDealerOrder.equals("")) {
			sql.append(" AND TMD.DEALER_ID IN (" + dealerId + ")\n");
			sql.append("AND TMV.VEHICLE_ID = TVDM.VEHICLE_ID\n");
			sql.append("  AND TVDD.DETAIL_ID = TVDM.DELIVERY_DETAIL_ID\n");
			sql.append("  AND TVDR.REQ_ID = TVDD.REQ_ID\n");
			sql.append(" AND TVDR.ORDER_ID = TVO.ORDER_ID\n");
			sql.append("   AND TVO.ORDER_NO LIKE '%" + upDealerOrder + "%'\n");
		}
		if (downDealerOrder != null && !downDealerOrder.equals("")) {
			sql.append("   AND TMD.PARENT_DEALER_D IN (" + dealerId + ") \n");
			sql.append("AND TMV.VEHICLE_ID = TVDM.VEHICLE_ID\n");
			sql.append("  AND TVDD.DETAIL_ID = TVDM.DELIVERY_DETAIL_ID\n");
			sql.append("  AND TVDR.REQ_ID = TVDD.REQ_ID\n");
			sql.append(" AND TVDR.ORDER_ID = TVO.ORDER_ID\n");
			sql.append("   AND TVO.ORDER_NO LIKE '%" + downDealerOrder + "%'\n");
		}

		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.DEALER_ID = TMD.DEALER_ID AND VOD1.DEALER_ID=TMD.DEALER_ID\n");
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID(+) \n");
		sql.append("   AND TMV.LIFE_CYCLE = TC1.CODE_ID \n");
		sql.append("   AND TMV.LOCK_STATUS = TC2.CODE_ID \n");
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
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

		if (null != materialCode__ && !"".equals(materialCode__)) {
			String[] array = materialCode__.split(",");
			sql.append("   AND TMVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.STORAGE_DATE)>=" + days.trim() + ")\n");
		}
		// sql.append(" AND TMD.PARENT_DEALER_D IN ("+dealerId+")\n");
		if (null != whName && !"".equals(whName)) {
			sql.append("   AND TMV.VEHICLE_AREA = " + whName + "\n");
		}
		if (null != lockStatus && !"".equals(lockStatus)) {
			sql.append("   AND TMV.LOCK_STATUS = " + lockStatus + "\n");
		}
		if (areaId != null && !"".equals(areaId)) {
			sql.append("   AND TMV.AREA_ID IN (" + areaId + ")");
		}
		sql.append("ORDER BY DEALER_SHORTNAME,TMVM.MATERIAL_CODE,TMV.LIFE_CYCLE,STORAGE_TIME \n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());

	}

	/***************************************************************************
	 * 经销商库存查询：明细查询
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getStorage_DLR(String areaId, String vehicleOwn, String whName, String dealerId, String materialCode, String materialCode__, String days, String vin, String vehicle_life, String lockStatus, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n");
		sql.append("       TMV.BATCH_NO, --批次号\n");
		sql.append("       TDW.WAREHOUSE_NAME, --位置说明\n");
		sql.append("       TMD.DEALER_SHORTNAME AS DEALER_SHORTNAME, \n");
		sql.append("       TMV.LIFE_CYCLE, --库存状态\n");
		sql.append("       TMV.LOCK_STATUS, --锁定状态\n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') AS PRODUCT_DATE, --生产日期\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_TIME, --入库日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT --库存天数\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_DEALER TMD, \n");
		sql.append("       TM_DEALER_WAREHOUSE TDW \n");
		if (vehicle_life.equals(Constant.VEHICLE_LIFE_05.toString())) {
			sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " \n");
		} else if (vehicle_life.equals(Constant.VEHICLE_LIFE_03.toString())) {
			sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " \n");
		} else {
			sql.append(" WHERE TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ")\n");
		}
		// sql.append(" AND TMV.DEALER_ID IN ("+dealerId+")\n");
		// 加上这句就查询不出自己管理的代销库的车了
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.DEALER_ID = TMD.DEALER_ID \n");
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID(+) \n");
		if (areaId != null && areaId != "") {
			sql.append("       AND TMV.AREA_ID IN " + areaId + "\n");
		}
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
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

		if (null != materialCode__ && !"".equals(materialCode__)) {
			String[] array = materialCode__.split(",");
			sql.append("   AND TMVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.STORAGE_DATE)>=" + days.trim() + ")\n");
		}
		if (null != vehicleOwn && ("01").equals(vehicleOwn)) { // 自有车
			// sql.append(" AND TMV.DEALER_ID IN ("+dealerId+")\n");
			sql.append("   AND TMV.VEHICLE_AREA IN\n");
			sql.append("       (SELECT WAREHOUSE_ID\n");
			sql.append("          FROM TM_DEALER_WAREHOUSE\n");
			sql.append("         WHERE TM_DEALER_WAREHOUSE.DEALER_ID IN (" + dealerId + ") and warehouse_type = ").append(Constant.DEALER_WAREHOUSE_TYPE_01).append(")\n");
		} else if (null != vehicleOwn && ("02").equals(vehicleOwn)) { // 代管车
			sql.append("   AND TMV.VEHICLE_AREA IN\n");
			sql.append("       (SELECT WAREHOUSE_ID\n");
			sql.append("          FROM TM_DEALER_WAREHOUSE\n");
			sql.append("         WHERE TM_DEALER_WAREHOUSE.MANAGE_DEALER_ID IN (" + dealerId + ") and warehouse_type = ").append(Constant.DEALER_WAREHOUSE_TYPE_02).append(")\n");
		} else if (null != vehicleOwn && ("03").equals(vehicleOwn)) {
			sql.append("   AND TMV.VEHICLE_AREA IN\n");
			sql.append("       (SELECT WAREHOUSE_ID\n");
			sql.append("          FROM TM_DEALER_WAREHOUSE\n");
			sql.append("         WHERE TM_DEALER_WAREHOUSE.DEALER_ID IN (" + dealerId + ") and warehouse_type = ").append(Constant.DEALER_WAREHOUSE_TYPE_02).append(")\n");
		} else { // 全部
			sql.append("   AND (TMV.DEALER_ID IN (" + dealerId + ") OR\n");
			sql.append("       TMV.VEHICLE_AREA IN\n");
			sql.append("       (SELECT WAREHOUSE_ID\n");
			sql.append("           FROM TM_DEALER_WAREHOUSE\n");
			sql.append("          WHERE TM_DEALER_WAREHOUSE.MANAGE_DEALER_ID IN (" + dealerId + ")))\n");
		}
		if (null != whName && !"".equals(whName)) {
			sql.append("   AND TMV.VEHICLE_AREA = " + whName + "\n");
		}
		if (null != lockStatus && !"".equals(lockStatus)) {
			sql.append("   AND TMV.LOCK_STATUS = " + lockStatus + "\n");
		}
		// if( areaId!=null && !"".equals(areaId)){
		// sql.append(" AND TMD.DEALER_ID=TDBA.DEALER_ID");
		// sql.append(" AND TDBA.AREA_ID="+areaId);
		// }

		sql.append("ORDER BY TMVM.MATERIAL_CODE,TMV.LIFE_CYCLE,STORAGE_TIME,TMV.VEHICLE_ID \n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}

	public static PageResult<Map<String, Object>> getStorage_DLR_SUZUKI(String areaId, String whName, String dealerId, String materialCode, String materialCode__, String days, String vin, String vehicle_life, String lockStatus, String startDate,String endDate,int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n");
		sql.append("       TMV.BATCH_NO, --批次号\n");
		sql.append("       TDW.WAREHOUSE_NAME, --位置说明\n");
		sql.append("       TMD.DEALER_SHORTNAME AS DEALER_SHORTNAME, \n");
		sql.append("       TMV.LIFE_CYCLE, --库存状态\n");
		sql.append("       TMV.LOCK_STATUS, --锁定状态\n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') AS PRODUCT_DATE, --生产日期\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_TIME, --入库日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT --库存天数\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_DEALER TMD, \n");
		sql.append("       TM_DEALER_WAREHOUSE TDW \n");
		if (vehicle_life.equals(Constant.VEHICLE_LIFE_05.toString())) {
			sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " \n");
		} else if (vehicle_life.equals(Constant.VEHICLE_LIFE_03.toString())) {
			sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " \n");
		} else {
			sql.append(" WHERE TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ")\n");
		}
		// sql.append(" AND TMV.DEALER_ID IN ("+dealerId+")\n");
		// 加上这句就查询不出自己管理的代销库的车了
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TDW.DEALER_ID = TMD.DEALER_ID \n");
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID(+) \n");
		if (areaId != null && areaId != "") {
			sql.append("       AND TMV.AREA_ID IN " + areaId + "\n");
		}
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
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

		if (null != materialCode__ && !"".equals(materialCode__)) {
			String[] array = materialCode__.split(",");
			sql.append("   AND TMVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.STORAGE_DATE)>=" + days.trim() + ")\n");
		}
		// sql.append(" AND TMV.DEALER_ID IN ("+dealerId+")\n");
		sql.append("   AND TMV.VEHICLE_AREA IN\n");
		sql.append("       (SELECT WAREHOUSE_ID\n");
		sql.append("          FROM TM_DEALER_WAREHOUSE\n");
		sql.append("         WHERE TM_DEALER_WAREHOUSE.DEALER_ID IN (" + dealerId + ") and warehouse_type = ").append(Constant.DEALER_WAREHOUSE_TYPE_01).append(")\n");

		if (null != whName && !"".equals(whName)) {
			sql.append("   AND TMV.VEHICLE_AREA = " + whName + "\n");
		}
		if (null != lockStatus && !"".equals(lockStatus)) {
			sql.append("   AND TMV.LOCK_STATUS = " + lockStatus + "\n");
		}
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TMV.STORAGE_date >= TO_DATE( '" + startDate + "','yyyy-mm-DD')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TMV.STORAGE_date <= TO_DATE( '" + endDate + "','yyyy-mm-DD')\n");
		}
		// if( areaId!=null && !"".equals(areaId)){
		// sql.append(" AND TMD.DEALER_ID=TDBA.DEALER_ID");
		// sql.append(" AND TDBA.AREA_ID="+areaId);
		// }

		sql.append("ORDER BY TMVM.MATERIAL_CODE,TMV.LIFE_CYCLE,STORAGE_TIME,TMV.VEHICLE_ID \n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}

	public static PageResult<Map<String, Object>> getStorage_DLR_Lower(String dealerIds, String upDealerOrder, String downDealerOrder, String areaId, String vehicleOwn, String whName, String dealerId, String materialCode, String materialCode__, String days, String vin, String vehicle_life, String lockStatus, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n");
		sql.append("       TMV.BATCH_NO, --批次号\n");
		sql.append("       TDW.WAREHOUSE_NAME, --位置说明\n");
		sql.append("       TMD.DEALER_SHORTNAME AS DEALER_SHORTNAME, \n");
		sql.append("       TMV.LIFE_CYCLE, --库存状态\n");
		sql.append("       TMV.LOCK_STATUS, --锁定状态\n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') AS PRODUCT_DATE, --生产日期\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_TIME, --入库日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT --库存天数\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_DEALER TMD,vw_org_dealer VOD1, \n");
		sql.append("       TM_DEALER_WAREHOUSE TDW \n");
		if ((upDealerOrder != null && !upDealerOrder.equals("")) || (downDealerOrder != null && !downDealerOrder.equals(""))) {
			sql.append(",TT_VS_ORDER     TVO,\n");
			sql.append("TT_VS_DLVRY_MCH TVDM,\n");
			sql.append("TT_VS_DLVRY_DTL TVDD,\n");
			sql.append("TT_VS_DLVRY_REQ TVDR\n");
		}
		if (vehicle_life.equals(Constant.VEHICLE_LIFE_05.toString())) {
			sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + " \n");
		} else if (vehicle_life.equals(Constant.VEHICLE_LIFE_03.toString())) {
			sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " \n");
		} else {
			sql.append(" WHERE TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ")\n");
		}
		// sql.append(" AND TMV.DEALER_ID IN ("+dealerId+")\n");
		// 加上这句就查询不出自己管理的代销库的车了
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.DEALER_ID = TMD.DEALER_ID AND VOD1.DEALER_ID=TMD.DEALER_ID \n");
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID(+) \n");
		if (areaId != null && areaId != "") {
			sql.append("       AND TMV.AREA_ID IN " + areaId + "\n");
		}
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
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

		if (null != materialCode__ && !"".equals(materialCode__)) {
			String[] array = materialCode__.split(",");
			sql.append("   AND TMVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.STORAGE_DATE)>=" + days.trim() + ")\n");
		}
		if ((upDealerOrder == null || upDealerOrder.equals("")) && (downDealerOrder == null || downDealerOrder.equals("")) && (dealerIds == null || dealerIds.equals(""))) {
			sql.append("   AND TMD.DEALER_ID IN ( SELECT VOD.DEALER_ID FROM vw_org_dealer VOD WHERE VOD.ROOT_DEALER_ID IN (" + dealerId + ")) \n");
		}
		if (dealerIds != null && !"".equals(dealerIds)) {
			sql.append("   AND VOD1.DEALER_ID IN (" + dealerIds + ") \n");
		}
		if (upDealerOrder != null && !upDealerOrder.equals("")) {
			sql.append(" AND TMD.DEALER_ID IN (" + dealerId + ")\n");
			sql.append("AND TMV.VEHICLE_ID = TVDM.VEHICLE_ID\n");
			sql.append("  AND TVDD.DETAIL_ID = TVDM.DELIVERY_DETAIL_ID\n");
			sql.append("  AND TVDR.REQ_ID = TVDD.REQ_ID\n");
			sql.append(" AND TVDR.ORDER_ID = TVO.ORDER_ID\n");
			sql.append("   AND TVO.ORDER_NO LIKE '%" + upDealerOrder + "%'\n");
		}
		if (downDealerOrder != null && !downDealerOrder.equals("")) {
			sql.append("   AND TMD.PARENT_DEALER_D IN (" + dealerId + ") \n");
			sql.append("AND TMV.VEHICLE_ID = TVDM.VEHICLE_ID\n");
			sql.append("  AND TVDD.DETAIL_ID = TVDM.DELIVERY_DETAIL_ID\n");
			sql.append("  AND TVDR.REQ_ID = TVDD.REQ_ID\n");
			sql.append(" AND TVDR.ORDER_ID = TVO.ORDER_ID\n");
			sql.append("   AND TVO.ORDER_NO LIKE '%" + downDealerOrder + "%'\n");
		}

		if ("01".equals(vehicleOwn)) {
			sql.append("   AND TDW.WAREHOUSE_TYPE = " + Constant.DEALER_WAREHOUSE_TYPE_01 + "\n");
		} else if ("02".equals(vehicleOwn)) {
			sql.append("   AND TDW.WAREHOUSE_TYPE = " + Constant.DEALER_WAREHOUSE_TYPE_02 + "\n");
		}

		if (null != whName && !"".equals(whName)) {
			sql.append("   AND TMV.VEHICLE_AREA = " + whName + "\n");
		}
		if (null != lockStatus && !"".equals(lockStatus)) {
			sql.append("   AND TMV.LOCK_STATUS = " + lockStatus + "\n");
		}

		sql.append("ORDER BY TMD.DEALER_SHORTNAME,TMVM.MATERIAL_CODE,TMV.LIFE_CYCLE,STORAGE_TIME,TMV.VEHICLE_ID \n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}

	/***************************************************************************
	 * 车厂端经销商库存查询
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> storageQueryOEM(String dutyType,String orgId, String batchNo, String dealerCode, String materialCode, String materialCode__, String days, String vin, String areaIds, int pageSize, int curPage) {

		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型 \n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置 \n");
		sql.append("       TMVM.MATERIAL_NAME, --物料 \n");
		sql.append("	   TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VIN, --VIN \n");
		sql.append("       TMV.BATCH_NO, --批次号  \n");
		sql.append("       TDW.WAREHOUSE_NAME VEHICLE_AREA, --位置说明 \n");
		sql.append("TMDA.DEALER_NAME,--代管经销商\n");
		sql.append("       TMD.DEALER_SHORTNAME, --经销商名称 \n");
		sql.append("       TMV.LIFE_CYCLE, --库存状态 \n");
		sql.append("       TMV.ENGINE_NO, --VIN\n");
		sql.append("	   TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE , --生产日期\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_TIME, --入库日期 \n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT --库存天数 \n");
		sql.append("  FROM TM_VEHICLE             TMV, \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G, \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1, \n");
		sql.append("       TM_VHCL_MATERIAL       TMVM, \n");
		sql.append("TM_DEALER_WAREHOUSE    TDW,\n");
		sql.append("TM_DEALER              TMDA,\n");
		sql.append("       TM_DEALER TMD  \n");
		sql.append(" WHERE TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ")\n");
		sql.append("AND TDW.WAREHOUSE_ID(+) = TMV.VEHICLE_AREA\n");
		sql.append("AND TMDA.DEALER_ID(+) = TDW.MANAGE_DEALER_ID\n");
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID \n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID \n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID \n");
		sql.append("   AND TMV.DEALER_ID = TMD.DEALER_ID  \n");

		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
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
		if (null != materialCode__ && !"".equals(materialCode__)) {
			String[] array = materialCode__.split(",");
			sql.append("   AND TMVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.STORAGE_DATE)>=" + days.trim() + ")\n");
		}
		if (null != batchNo && !"".equals(batchNo)) {
			sql.append(" AND TMV.BATCH_NO = " + batchNo + "\n");
		}
		if (null != areaIds && !"".equals(areaIds)) {
			sql.append("           and exists\n");
			sql.append("         (select 1\n");
			sql.append("                  from tm_dealer_business_area tdba\n");
			sql.append("                 where 1 = 1\n");
			sql.append("                   and tdba.dealer_id = TMD.dealer_id\n");
			sql.append("                   and tdba.area_id IN (").append(areaIds).append("))\n");
		}

        //如果是大区用户 只显示大区下边所有经销商
        if(dutyType.equals(Constant.DUTY_TYPE_LARGEREGION.toString())){
            sql.append("AND TMD.DEALER_ID IN(\n" );
            sql.append("    SELECT VOD.DEALER_ID FROM VW_ORG_DEALER VOD\n" );
            sql.append("    WHERE VOD.ROOT_ORG_ID="+orgId+")\n");
        //如果是小区用户 只显示小区下边所有经销商
        }else if(dutyType.equals(Constant.DUTY_TYPE_SMALLREGION.toString())){
            sql.append("AND TMD.DEALER_ID IN(\n" );
            sql.append("    SELECT TDOR.DEALER_ID FROM TM_DEALER_ORG_RELATION TDOR\n" );
            sql.append("    WHERE TDOR.ORG_ID="+orgId+")\n");
        }

		sql.append("ORDER BY  TMD.DEALER_SHORTNAME, TMVM.MATERIAL_CODE,TMV.BATCH_NO\n");

		return dao.pageQuery(sql.toString(), params, "com.infodms.dms.dao.sales.storageManage.StorageQueryDAO.getStorage_DLR", pageSize, curPage);
	}

	/***************************************************************************
	 * 车厂端经销商库存下载
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> download(String orgId, String batchNo, String dealerCode, String materialCode, String materialCode__, String days, String vin, String areaIds) {

		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型 \n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置 \n");
		sql.append("       TMVM.MATERIAL_NAME, --物料 \n");
		sql.append("	   TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VIN, --VIN \n");
		sql.append("       TMV.ENGINE_NO, --VIN \n");
		sql.append("       TMV.BATCH_NO, --批次号  \n");
		sql.append("       TDW.WAREHOUSE_NAME VEHICLE_AREA, --位置说明 \n");
		sql.append("TMDA.DEALER_NAME,--代管经销商\n");
		sql.append("       TMD.DEALER_SHORTNAME, --经销商名称 \n");
		sql.append("      TC1.CODE_DESC, TMV.LIFE_CYCLE, --库存状态 \n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_TIME, --入库日期 \n");
		sql.append("	   TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE , --生产日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT --库存天数 \n");
		sql.append("  FROM TM_VEHICLE             TMV, \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G, \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1, TC_CODE TC1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM, \n");
		sql.append("TM_DEALER_WAREHOUSE    TDW,\n");
		sql.append("TM_DEALER              TMDA,\n");
		sql.append("       TM_DEALER TMD  \n");
		sql.append(" WHERE TMV.LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_05 + "," + Constant.VEHICLE_LIFE_03 + ")\n");
		sql.append("AND TDW.WAREHOUSE_ID(+) = TMV.VEHICLE_AREA  AND TC1.CODE_ID=TMV.LIFE_CYCLE\n");
		sql.append("AND TMDA.DEALER_ID(+) = TDW.MANAGE_DEALER_ID\n");
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID \n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID \n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID \n");
		sql.append("   AND TMV.DEALER_ID = TMD.DEALER_ID  \n");

		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
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
		if (null != materialCode__ && !"".equals(materialCode__)) {
			String[] array = materialCode__.split(",");
			sql.append("   AND TMVM.MATERIAL_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != days && !"".equals(days)) {
			sql.append("   AND (CEIL (SYSDATE - TMV.STORAGE_DATE)>=" + days.trim() + ")\n");
		}
		if (null != batchNo && !"".equals(batchNo)) {
			sql.append(" AND TMV.BATCH_NO = " + batchNo + "\n");
		}
		if (null != areaIds && !"".equals(areaIds)) {
			sql.append("           and exists\n");
			sql.append("         (select 1\n");
			sql.append("                  from tm_dealer_business_area tdba\n");
			sql.append("                 where 1 = 1\n");
			sql.append("                   and tdba.dealer_id = TMD.dealer_id\n");
			sql.append("                   and tdba.area_id IN (").append(areaIds).append("))\n");
		}

		if (!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId));
		}

		sql.append("ORDER BY   TMD.DEALER_SHORTNAME, TMVM.MATERIAL_CODE,TMV.BATCH_NO \n");

		return dao.pageQuery(sql.toString(), params, dao.getFunName());
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
