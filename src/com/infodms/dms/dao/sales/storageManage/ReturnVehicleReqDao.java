/**
 * 
 */
package com.infodms.dms.dao.sales.storageManage;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.reqquery.ReqQueryDao;
import com.infodms.dms.po.TtReturnVehicleHeadPO;
import com.infodms.dms.po.TtVsReturnVehicleReqPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
public class ReturnVehicleReqDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(ReturnVehicleReqDao.class);
	public final static ReturnVehicleReqDao dao = new ReturnVehicleReqDao();
	public ReturnVehicleReqDao() {
	};
	public static ReturnVehicleReqDao getInstance() {
		return dao;
	}
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	public PageResult<Map<String, Object>> getReturnVehicleReq(String dealerId, String materialCode, String materialCode__, String vin, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VEHICLE_ID, TMV.VIN, TMV.DEALER_ID, --VIN\n");
		sql.append("       TDW.WAREHOUSE_NAME, --位置说明\n");
		sql.append("       TMD.DEALER_SHORTNAME, \n");
		sql.append("       TMV.LIFE_CYCLE, --库存状态\n");
		sql.append("       (SELECT TCO.CODE_DESC FROM TC_CODE TCO WHERE TCO.CODE_ID=TMV.LIFE_CYCLE) AS LIFE_CYCLE_DESC, --库存状态描述\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_TIME, --入库日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT --库存天数\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_DEALER TMD, \n");
		sql.append("       TM_DEALER_WAREHOUSE TDW \n");
		sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " \n");
		sql.append("   AND TMV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + "\n");
		sql.append("   AND TMV.DEALER_ID IN (" + dealerId + ")\n");
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.DEALER_ID = TMD.DEALER_ID \n");
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID(+) \n");
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
		sql.append("ORDER BY TMV.STORAGE_DATE DESC \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	public PageResult<Map<String, Object>> getQueryReturnVehicleReq(String orgId, String status, String dealerId, String materialCode, String materialCode__, String vin, String areaIds, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VEHICLE_ID, TMV.VIN, A.DEALER_ID, --VIN\n");
		sql.append("       TDW.WAREHOUSE_NAME, --位置说明\n");
		sql.append("       TMD.DEALER_SHORTNAME, \n");
		sql.append("       TMD.DEALER_NAME, \n");
		sql.append("       A.REQ_NO, \n");
		sql.append("	   A.REASON,  A.CHECK_REMARK, A.STATUS REQ_STATUS,\n");
		sql.append("	   (SELECT TCO.CODE_DESC FROM TC_CODE TCO WHERE TCO.CODE_ID=A.STATUS) AS REQ_STATUS_DESC,\n");
		sql.append("	   TO_CHAR(A.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE,\n");
		sql.append("	   TO_CHAR(A.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,");
		sql.append("       TMV.LIFE_CYCLE, --库存状态\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_TIME, --入库日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT --库存天数\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_DEALER TMD, \n");
		sql.append("       TM_DEALER_WAREHOUSE TDW, \n");
		sql.append("       TT_VS_RETURN_VEHICLE_REQ A \n");
		sql.append(" WHERE TMV.VEHICLE_ID = A.VEHICLE_ID \n");
		if (Utility.testString(dealerId)) {
			sql.append("   AND A.DEALER_ID IN (" + dealerId + ")\n");
		}
		if (Utility.testString(status)) {
			sql.append("   AND A.STATUS = ").append(status).append("\n");
		}
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND A.DEALER_ID = TMD.DEALER_ID \n");
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID(+) \n");
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
		if (Utility.testString(areaIds)) {
			sql.append("   AND TMV.AREA_ID IN (" + areaIds + ")\n");
		}
		if (!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId));
		}
		sql.append("ORDER BY TMV.STORAGE_DATE DESC \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	public Long getAreaId(Long id) {
		Long areaId = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT AREA_ID FROM TM_DEALER_BUSINESS_AREA WHERE DEALER_ID = ").append(id);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if (map != null) {
			areaId = Long.parseLong(String.valueOf(map.get("AREA_ID")));
		}
		return areaId;
	}
	public Long getVAreaId(String id) {
		List<Object> params = new ArrayList<Object>();
		Long areaId = null;
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select tmv.area_id from tm_vehicle tmv where tmv.vehicle_id = ?\n");
		params.add(id);
		Map<String, Object> map = pageQueryMap(sql.toString(), params, getFunName());
		if (map != null) {
			areaId = Long.parseLong(String.valueOf(map.get("AREA_ID")));
		}
		return areaId;
	}
	public PageResult<Map<String, Object>> getApplyReturnVehicleReq(String orgId, String dealerId, String areaId, String materialCode, String materialCode__, String vin, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.REQ_ID, G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VEHICLE_ID, TMV.VIN, TMV.DEALER_ID, --VIN\n");
		sql.append("       TDW.WAREHOUSE_NAME, --位置说明\n");
		sql.append("       TMD.DEALER_SHORTNAME, \n");
		sql.append("       TMD.DEALER_NAME, \n");
		sql.append("	   A.REQ_NO,\n");
		sql.append("	   A.REASON,  A.CHECK_REMARK, A.STATUS REQ_STATUS,\n");
		sql.append("	   TO_CHAR(A.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE,\n");
		sql.append("	   TO_CHAR(A.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,");
		sql.append("       TMV.LIFE_CYCLE, --库存状态\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_TIME, --入库日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT --库存天数\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_DEALER TMD, \n");
		sql.append("       TM_DEALER_WAREHOUSE TDW, \n");
		sql.append("       TT_VS_RETURN_VEHICLE_REQ A \n");
		sql.append(" WHERE TMV.VEHICLE_ID = A.VEHICLE_ID \n");
		sql.append(" AND A.STATUS = ").append(Constant.RETURN_STATUS_01).append("\n");
		if (Utility.testString(dealerId)) {
			sql.append("   AND TMV.DEALER_ID IN (" + dealerId + ")\n");
		}
		if (Utility.testString(areaId)) {
			sql.append("   AND A.AREAID = ").append(areaId).append("\n");
		}
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.DEALER_ID = TMD.DEALER_ID \n");
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID(+) \n");
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
		if (!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId));
		}
		sql.append("ORDER BY TMV.STORAGE_DATE DESC \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	public Map<String, Object> checkAvailableResource(String warehoursId, Long materialId, String bathNo) {
		StringBuffer sql = new StringBuffer("\n");
		sql.append("SELECT A.COMPANY_ID,\n");
		sql.append("       A.MATERIAL_ID,\n");
		sql.append("       A.BATCH_NO,\n");
		sql.append("       A.STOCK_AMOUNT,\n");
		sql.append("       A.SPECIAL_BATCH_NO,\n");
		sql.append("       NVL(B.RESERVE_AMOUNT, 0) UN_DLVRY_AMOUNT,\n");
		sql.append("       A.STOCK_AMOUNT - NVL(B.RESERVE_AMOUNT, 0) AVA_STOCK\n");
		sql.append("  FROM (SELECT TV.OEM_COMPANY_ID COMPANY_ID,\n");
		sql.append("               TV.MATERIAL_ID,\n");
		sql.append("               TV.SPECIAL_BATCH_NO,\n");
		sql.append("               TV.BATCH_NO,\n");
		sql.append("               W.WAREHOUSE_ID,\n");
		sql.append("               COUNT(TV.VEHICLE_ID) STOCK_AMOUNT\n");
		sql.append("          FROM TM_VEHICLE TV, TM_WAREHOUSE W\n");
		sql.append("         WHERE TV.WAREHOUSE_ID = W.WAREHOUSE_ID\n");
		sql.append("           AND W.WAREHOUSE_TYPE <> ").append(Constant.WAREHOUSE_TYPE_04).append("\n");
		sql.append("           AND TV.LIFE_CYCLE = ").append(Constant.VEHICLE_LIFE_02).append("\n");
		sql.append("           AND TV.LOCK_STATUS = ").append(Constant.LOCK_STATUS_01).append("\n");
		sql.append("           AND W.WAREHOUSE_ID = ").append(warehoursId).append("\n");
		sql.append("         GROUP BY TV.MATERIAL_ID,\n");
		sql.append("                  TV.BATCH_NO,\n");
		sql.append("                  TV.OEM_COMPANY_ID,\n");
		sql.append("                  TV.SPECIAL_BATCH_NO,\n");
		sql.append("                  W.WAREHOUSE_ID) A,\n");
		sql.append("       (SELECT OEM_COMPANY_ID COMPANY_ID,\n");
		sql.append("               MATERIAL_ID,\n");
		sql.append("               BATCH_NO,\n");
		sql.append("               WAREHOUSE_ID,\n");
		sql.append("               (SUM(NVL(AMOUNT, 0)) - SUM(NVL(DELIVERY_AMOUNT, 0))) RESERVE_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER_RESOURCE_RESERVE\n");
		sql.append("         WHERE RESERVE_STATUS = ").append(Constant.RESOURCE_RESERVE_STATUS_01).append("\n");
		sql.append("         GROUP BY OEM_COMPANY_ID, MATERIAL_ID, BATCH_NO, WAREHOUSE_ID) B\n");
		sql.append(" WHERE A.COMPANY_ID = B.COMPANY_ID(+)\n");
		sql.append("   AND A.MATERIAL_ID = B.MATERIAL_ID(+)\n");
		sql.append("   AND A.BATCH_NO = B.BATCH_NO(+)\n");
		sql.append("   AND A.WAREHOUSE_ID = B.WAREHOUSE_ID(+)\n");
		sql.append("   AND A.MATERIAL_id = ").append(materialId).append("\n");
		sql.append("   AND A.batch_no = ").append(bathNo).append("\n");
		return pageQueryMap(sql.toString(), null, getFunName());
	}
	public void moveOrderCancel(String stoId) {
		StringBuffer sql = new StringBuffer("\n");
		sql.append("update tt_vs_order_resource_reserve tvorr\n");
		sql.append("   set tvorr.reserve_status = ").append(Constant.RESOURCE_RESERVE_STATUS_02).append(", tvorr.delivery_amount = null\n");
		sql.append(" where tvorr.sto_id = ").append(stoId).append("\n");
		sql.append("   and tvorr.reserve_type = ").append(Constant.RESERVE_TYPE_02).append("\n");
		super.update(sql.toString(), null);
	}
	/** ************************************************************************************************************* */
	public PageResult<Map<String, Object>> ReturnVehicleReqQuery(Map<String, String> map, int pageSize, int curPage) {
		String dealerId = map.get("dealerId");
		String materialCode = map.get("materialCode");
		String materialCode__ = map.get("materialCode__");
		String vin = map.get("vin");
		String deliveryNo = map.get("deliveryNo");
		String exVeh = map.get("exVeh");
		String typeName = map.get("typeName");
		StringBuffer sql = new StringBuffer("\n");
		sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VEHICLE_ID, TMV.VIN, TMV.DEALER_ID, TMV.AREA_ID, --VIN\n");
		sql.append("       TDW.WAREHOUSE_NAME, --位置说明\n");
		sql.append("       TMD.DEALER_SHORTNAME, \n");
		sql.append("       TMV.LIFE_CYCLE, --库存状态\n");
		sql.append("       (SELECT TCO.CODE_DESC FROM TC_CODE TCO WHERE TCO.CODE_ID=TMV.LIFE_CYCLE) AS LIFE_CYCLE_DESC, --库存状态描述\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_TIME, --入库日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT, --库存天数\n");
		sql.append("       NVL(TEMP.TYPE_ID, NULL) TYPE_ID,\n");
		sql.append("       NVL(TEMP.TYPE_NAME, '未知') TYPE_NAME,\n");
		sql.append("       NVL(TEMP.SINGLE_PRICE, NULL) SINGLE_PRICE\n");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_DEALER TMD, \n");
		sql.append("       TM_DEALER_WAREHOUSE TDW, \n");
		sql.append(" (SELECT TVAT.TYPE_ID,\n");
		sql.append("               TVAT.TYPE_NAME,\n");
		sql.append("			   TVD.DELIVERY_NO,\n");
		sql.append("               TVDRD.SINGLE_PRICE,\n");
		sql.append("               TVDM.VEHICLE_ID\n");
		sql.append("          FROM TT_VS_DLVRY TVD,\n");
		sql.append("               TT_VS_DLVRY_DTL TVDD,\n");
		sql.append("               TT_VS_DLVRY_REQ_DTL TVDRD,\n");
		sql.append("               (SELECT VEHICLE_ID, MAX(DELIVERY_DETAIL_ID) DELIVERY_DETAIL_ID\n");
		sql.append("                  FROM TT_VS_DLVRY_MCH\n");
		sql.append("                 GROUP BY VEHICLE_ID) TVDM,\n");
		sql.append("               TT_VS_ACCOUNT_TYPE TVAT\n");
		sql.append("         WHERE TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n");
		sql.append("           AND TVDD.DETAIL_ID = TVDM.DELIVERY_DETAIL_ID\n");
		sql.append("           AND TVDD.REQ_DTL_ID = TVDRD.DETAIL_ID\n");
		sql.append("           AND TVD.FUND_TYPE = TVAT.TYPE_ID\n");
		sql.append("           ) TEMP\n");
		sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " \n");
		sql.append("   AND TMV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + "\n");
		sql.append("   AND TMV.DEALER_ID IN (" + dealerId + ")\n");
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.DEALER_ID = TMD.DEALER_ID \n");
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID(+) \n");
		sql.append("   AND TMV.VEHICLE_ID = TEMP.VEHICLE_ID(+)\n");
		if (!CommonUtils.isNullString(deliveryNo)) {
			sql.append("           AND TEMP.DELIVERY_NO LIKE '%").append(deliveryNo).append("%'\n");
		}
		if (!CommonUtils.isNullString(typeName)) {
			if ("未知".equals(typeName)) {
				sql.append("           AND TEMP.TYPE_NAME IS NULL \n");
			} else {
				sql.append("           AND TEMP.TYPE_NAME = '").append(typeName).append("'\n");
			}
		}
		if (!CommonUtils.isNullString(exVeh)) {
			sql.append("           AND TMV.VEHICLE_ID NOT IN (").append(exVeh).append(")\n");
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
		sql.append("ORDER BY TMV.STORAGE_DATE DESC \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 查询退车列表
	 * */
	public PageResult<Map<String, Object>> ReturnVehicleReqQuery_SUZUKI(Map<String, String> map, int pageSize, int curPage) {
		String dealerId = map.get("dealerId");
		String materialCode = map.get("materialCode");
		String materialCode__ = map.get("materialCode__");
		String vin = map.get("vin");
		String deliveryNo = map.get("deliveryNo");
		String exVeh = map.get("exVeh");
		StringBuffer sql = new StringBuffer("\n");
		sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VEHICLE_ID, TMV.VIN, TDW.DEALER_ID, TMV.AREA_ID, --VIN\n");
		sql.append("       TDW.WAREHOUSE_NAME, --位置说明\n");
		sql.append("       TMD.DEALER_SHORTNAME, \n");
		sql.append("       TMV.LIFE_CYCLE, --库存状态\n");
		sql.append("       (SELECT TCO.CODE_DESC FROM TC_CODE TCO WHERE TCO.CODE_ID=TMV.LIFE_CYCLE) AS LIFE_CYCLE_DESC, --库存状态描述\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') AS STORAGE_TIME, --入库日期\n");
		sql.append("       (CEIL (SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT, --库存天数\n");
		//因为从其他库读取 先用数字代替
		sql.append("  nvl(tpd.SALES_PRICE,0) SALES_PRICE,  --单价\n");   
		sql.append("   nvl(tpd.price_id,0) LIST_ID \n");
		//sql.append("	 (SELECT   F_GET_MATPRICE(TMD.DEALER_CODE, TMVM.MATERIAL_CODE) FROM DUAL) AS SALES_PRICE,\n");
		// sql.append("	 (SELECT F_GET_PRICELISTID(TMD.DEALER_CODE, TMVM.MATERIAL_CODE) FROM DUAL) AS LIST_ID");
		sql.append("  FROM TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_DEALER TMD, \n");
		sql.append("       TM_DEALER_WAREHOUSE TDW, \n");
		sql.append("		(\n");
		sql.append("			SELECT R.DEALER_ID,D.SALES_PRICE,D.PRICE_ID,D.GROUP_ID \n");
		sql.append("			FROM TM_DEALER_PRICE_RELATION R,TT_VS_PRICE_DTL D \n");
		sql.append("			WHERE R.PRICE_ID=D.PRICE_ID AND R.DEALER_ID IN("+dealerId+") \n");
		sql.append("		 ) TPD  \n");
		sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + " \n");
		sql.append("   AND TMV.LOCK_STATUS IN ( " + Constant.LOCK_STATUS_01 + "," + Constant.LOCK_STATUS_04 + ")\n");
		sql.append("   AND TDW.DEALER_ID IN (" + dealerId + ")\n");
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TDW.DEALER_ID = TMD.DEALER_ID \n");
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID \n");
		sql.append("   AND TMV.DEALER_ID=TPD.DEALER_ID(+) \n");
		sql.append("   AND TMV.PACKAGE_ID=TPD.GROUP_ID(+) ");
		if (!CommonUtils.isNullString(deliveryNo)) {
			sql.append("           AND TVDE.SENDCAR_ORDER_NUMBER LIKE '%").append(deliveryNo).append("%'\n");
		}
		if (!CommonUtils.isNullString(exVeh)) {
			sql.append("           AND TMV.VEHICLE_ID NOT IN (").append(exVeh).append(")\n");
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
		sql.append("ORDER BY TMV.STORAGE_DATE DESC \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	public Long returnHeadInsert(Map<String, String> map) {
		String acountTypeName = map.get("acountTypeName");
		String status = map.get("status");
		String dealerId = map.get("dealerId");
		String userId = map.get("userId");
		String applyAmount = map.get("applyAmount");
		String reason = map.get("reason");
		String companyId = map.get("companyId");
		TtReturnVehicleHeadPO trvh = new TtReturnVehicleHeadPO();
		// String accountTypeId = map.get("accountTypeId");
		Long headId = Long.parseLong(SequenceManager.getSequence(""));
		trvh.setHeadId(headId);
		String returnNo = "TC" + headId;
		trvh.setReturnVehicleNo(returnNo);
		trvh.setAccountTypeName(acountTypeName);
		trvh.setStatus(Integer.parseInt(status));
		trvh.setDealerId(Long.parseLong(dealerId));
		trvh.setCreateBy(Long.parseLong(userId));
		trvh.setCreateDate(new Date(System.currentTimeMillis()));
		trvh.setApplyAmount(Long.parseLong(applyAmount));
		trvh.setReason(reason);
		trvh.setCompanyId(Long.parseLong(companyId));
		// trvh.setAccountTypeId(new Long(accountTypeId));
		super.insert(trvh);
		return headId;
	}
	public void returnHeadUpdate_OLD(Map<String, String> map) {
		String headId = map.get("headId");
		String status = map.get("status");
		String dealerId = map.get("dealerId");
		String userId = map.get("userId");
		TtReturnVehicleHeadPO oldTrvh = new TtReturnVehicleHeadPO();
		oldTrvh.setHeadId(Long.parseLong(headId));
		TtReturnVehicleHeadPO newTrvh = new TtReturnVehicleHeadPO();
		if (!CommonUtils.isNullString(status)) {
			newTrvh.setStatus(Integer.parseInt(status));
		}
		if (!CommonUtils.isNullString(dealerId)) {
			newTrvh.setDealerId(Long.parseLong(dealerId));
		}
		newTrvh.setUpdateBy(Long.parseLong(userId));
		newTrvh.setUpdateDate(new Date(System.currentTimeMillis()));
		super.update(oldTrvh, newTrvh);
	}
	
	/**
	 * 审核通过处理
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void returnHeadUpdate(Map<String, String> map) {
		String headId = map.get("headId");
		String dealerId = map.get("dealerId");
		String userId = map.get("userId");
		String remark = map.get("remark");
		String no = map.get("no");
		String isWareHousing = map.get("isWareHousing");
		//更改前的状态
		String preStatus = map.get("preStatus").toString();
		//更改后的状态
		String nextStatus = map.get("nextStatus").toString();
		// String accountTypeId=map.get("accountTypeId");
		List list = new ArrayList();
		StringBuffer sql = new StringBuffer("\n");
		sql.append("UPDATE TT_RETURN_VEHICLE_HEAD TRVH\n");
		sql.append("   SET TRVH.STATUS       = " + Integer.parseInt(nextStatus)+ ",\n");
		if (!CommonUtils.isEmpty(isWareHousing)) {
			sql.append("    TRVH.IS_WARE_HOUSING       = " +Integer.parseInt(isWareHousing)+ ",\n");  // 表示储运确认退运单了
		}
		if (!CommonUtils.isNullString(dealerId)) {
			sql.append("       TRVH.DEALER_ID    = " + Long.parseLong(dealerId) + ",\n");
		}
		if (!CommonUtils.isNullString(remark)) {
			sql.append("       TRVH.CHECK_REMARK = ?,\n");
			list.add(remark);
		}
		if (!CommonUtils.isNullString(no)) {
			sql.append("       TRVH.RETURN_VEHICLE_NO = ?,\n");
			list.add(no);
		}
		/*if (!CommonUtils.isNullString(accountTypeId)) {
			sql.append("       TRVH.ACCOUNT_TYPE_ID = ?,\n");
			list.add(accountTypeId);
		}*/
		sql.append("       TRVH.UPDATE_BY    = " + Long.parseLong(userId) + ",\n");
		sql.append("       TRVH.UPDATE_DATE  = SYSDATE\n");
		sql.append(" WHERE TRVH.HEAD_ID IN\n");
		sql.append("       (SELECT HEAD_ID\n");
		sql.append("          FROM TT_VS_RETURN_VEHICLE_REQ T\n");
		sql.append("         WHERE T.HEAD_ID = " + headId + "\n");
		sql.append("         GROUP BY T.HEAD_ID\n");
		sql.append("        HAVING MAX(DECODE(T.STATUS, " + Integer.parseInt(preStatus) + ", 1, 0)) = 0)");
		dao.update(sql.toString(), list);
	}
	
	public List<PO> returnHeadQuery(Map<String, String> map) {
		String headId = map.get("headId");
		//String acountTypeId = map.get("acountTypeId");
		String status = map.get("status");
		String dealerId = map.get("dealerId");
		TtReturnVehicleHeadPO trvh = new TtReturnVehicleHeadPO();
		if (!CommonUtils.isNullString(headId)) {
			trvh.setHeadId(Long.parseLong(headId));
		}
		if (!CommonUtils.isNullString(status)) {
			trvh.setStatus(Integer.parseInt(status));
		}
		/*if (!CommonUtils.isNullString(acountTypeId)) {
			trvh.setAccountTypeId(Long.parseLong(acountTypeId));
		}*/
		if (!CommonUtils.isNullString(dealerId)) {
			trvh.setDealerId(Long.parseLong(dealerId));
		}
		return select(trvh);
	}
	
	public void returnLineInsert(Map<String, String> map) {
		String headId = map.get("headId");
		String vehicleId = map.get("vehicleId");
		String dealerId = map.get("dealerId");
		// String areaId = map.get("areaId");
		String reason = map.get("reason");
		String status = map.get("status");
		String companyId = map.get("companyId");
		String userId = map.get("userId");
		String singlePrice = map.get("singlePrice");
		String priceListId=map.get("priceListId");
		TtVsReturnVehicleReqPO po = new TtVsReturnVehicleReqPO();
		Long reqId = Long.parseLong(SequenceManager.getSequence(""));
		// String reqNo = "TC" + headId;
		po.setReqId(reqId);
		// po.setReqNo(reqNo) ;
		po.setVehicleId(Long.parseLong(vehicleId));// 车辆表ID
		po.setDealerId(Long.parseLong(dealerId));// 经销商ID
		// po.setAreaid(Long.parseLong(areaId));
		po.setReason(reason);// 退车原因
		po.setStatus(Integer.parseInt(status));
		po.setRaiseDate(new Date(System.currentTimeMillis()));
		po.setCompanyId(Long.parseLong(companyId));
		po.setCreateBy(Long.parseLong(userId));
		po.setCreateDate(po.getRaiseDate());
		po.setHeadId(Long.parseLong(headId));
		if (!CommonUtils.isNullString(priceListId)) {
			po.setPriceListId(Long.parseLong(priceListId));
		}
		if (!CommonUtils.isNullString(singlePrice)) {
			po.setSinglePrice(Double.parseDouble(singlePrice));
		}
		super.insert(po);
	}
	
	public void returnLineUpdate(Map<String, String> map) {
		String headId = map.get("headId");
		String reqId = map.get("reqId");
		String oldStatus = map.get("oldStatus");
		String status = map.get("status");
		String userId = map.get("userId");
		String remark = map.get("remark");
		String reqType = map.get("reqType");
		String no = map.get("no");
		String infactMoney = map.get("infactMoney");//车辆实退金额
		TtVsReturnVehicleReqPO oldTvrvr = new TtVsReturnVehicleReqPO();
		if (!CommonUtils.isNullString(reqId)) {
			oldTvrvr.setReqId(Long.parseLong(reqId));
		}
		if (!CommonUtils.isNullString(headId)) {
			oldTvrvr.setHeadId(Long.parseLong(headId));
		}
		if (!CommonUtils.isNullString(oldStatus)) {
			oldTvrvr.setStatus(Integer.parseInt(oldStatus));
		}
		TtVsReturnVehicleReqPO newTvrvr = new TtVsReturnVehicleReqPO();
		newTvrvr.setStatus(Integer.parseInt(status));
		newTvrvr.setUpdateBy(Long.parseLong(userId));
		newTvrvr.setUpdateDate(new Date(System.currentTimeMillis()));
		newTvrvr.setCheckDate(new Date(System.currentTimeMillis()));
		if (!CommonUtils.isNullString(remark)) {
			if ("2".equals(reqType)) {
				//销售部意见
				newTvrvr.setCheckSaleRemark(remark);
			} else if ("3".equals(reqType)) {
				//储运部意见
				newTvrvr.setCheckStorageRemark(remark);
			} else if ("4".equals(reqType)) {
				//财务意见
				newTvrvr.setCheckFinanceRemark(remark);
			} else {
				//区域意见
				newTvrvr.setCheckRemark(remark);
			}
		}
		if (!CommonUtils.isNullString(infactMoney)) {
			if (!CheckUtil.checkNull(infactMoney)) {
				newTvrvr.setFinanceReturnMoney(Double.parseDouble(infactMoney));
			}
		}
		if (!CommonUtils.isNullString(no)) {
			newTvrvr.setReqNo(no);
		}
		super.update(oldTvrvr, newTvrvr);
	}
	
	
	public void returnLineQuery(Map<String, String> map) {
		// StringBuffer sql = new StringBuffer("\n");
	}
	
	/***
	 * 审核时查询的sql，由于审核目前不要账户，所以与最后数据查询的sql分开
	 * */
	public PageResult<Map<String, Object>> returnQuery(Map<String, String> map, int pageSize, int curPage) {
		String status = map.get("status");
		String returnNo = map.get("returnNo");
		// String accountType = map.get("accountType");
		String dealerId = map.get("dealerId");
		String orgId = map.get("orgId");
		String checkstartDate = map.get("checkstartDate");
		String checkendDate = map.get("checkendDate");
		String reqStartDate = map.get("reqStartDate");
		String reqEndDate = map.get("reqEndDate");
		String dutyType = map.get("dutyType");
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select trvh.head_id,\n");
		sql.append("       trvh.return_vehicle_no,\n");
		sql.append("       trvh.account_type_name,\n");
		sql.append("       tcc.code_desc status_desc,\n");
		sql.append("       tmd.dealer_shortname,\n");
		sql.append("       tcu.name,\n");
		sql.append("       trvh.apply_amount,\n");
		sql.append("       trvh.reason,\n");
		sql.append("       trvh.status,\n");
		sql.append("       trvh.is_ware_housing,\n");
		sql.append("       to_char(trvh.update_date, 'yyyy-MM-dd') check_date,\n");
		sql.append("       to_char(trvh.create_date, 'yyyy-MM-dd') applay_create,\n");
		if (CommonUtils.isEmpty(status)) {
			sql.append("       0  COU\n");
		} else {
			sql.append("       SUM(DECODE(TVRVR.STATUS, " + status + ", 1, 0)) COU\n");
		}
		sql.append("  from tt_return_vehicle_head trvh,\n");
		sql.append("       tm_dealer             tmd,\n");
		sql.append("       tc_code                tcc,\n");
		sql.append("       tc_user                tcu,\n");
		sql.append("	   TT_VS_RETURN_VEHICLE_REQ TVRVR\n");
		sql.append(" where trvh.dealer_id = tmd.dealer_id\n");
		sql.append("   and trvh.create_by = tcu.user_id\n");
		sql.append("   and trvh.status = tcc.code_id\n");
		sql.append("   AND TVRVR.HEAD_ID = TRVH.HEAD_ID\n");
		if (!CommonUtils.isNullString(status)) {
			sql.append("   and trvh.status = ").append(status).append("\n");
		}
		if (!CommonUtils.isNullString(returnNo)) {
			sql.append("   and trvh.return_vehicle_no like '%").append(returnNo).append("%'\n");
		}
		/*if (!CommonUtils.isNullString(accountType)) {
			sql.append("   and trvh.account_type_id = ").append(accountType).append("\n");
		}*/
		if (!CommonUtils.isNullString(dealerId)) {
			sql.append("   and trvh.dealer_id in (").append(dealerId).append(")\n");
		}
        //如果是大区用户 只显示大区下边所有经销商
        if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
            sql.append("AND TMD.DEALER_ID IN(\n" );
            sql.append("    SELECT VOD.DEALER_ID FROM VW_ORG_DEALER_ALL VOD\n" );
            sql.append("    WHERE VOD.ROOT_ORG_ID="+orgId+")\n");
        //如果是小区用户 只显示小区下边所有经销商
        }else if(Constant.DUTY_TYPE_SMALLREGION.toString().equals(dutyType)){
            sql.append("AND TMD.DEALER_ID IN(\n" );
            sql.append("    SELECT TDOR.DEALER_ID FROM TM_DEALER_ORG_RELATION TDOR\n" );
            sql.append("    WHERE TDOR.ORG_ID="+orgId+")\n");
        }
		if (null != checkstartDate && !"".equals(checkstartDate)) {
			sql.append("   AND trvh.UPDATE_DATE >= TO_DATE('" + checkstartDate + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != checkendDate && !"".equals(checkendDate)) {
			sql.append("   AND trvh.UPDATE_DATE  <= TO_DATE('" + checkendDate + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != reqStartDate && !"".equals(reqStartDate)) {
			sql.append("   AND trvh.CREATE_DATE >= TO_DATE('" + reqStartDate + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != reqEndDate && !"".equals(reqEndDate)) {
			sql.append("   AND trvh.CREATE_DATE  <= TO_DATE('" + reqEndDate + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		sql.append("  GROUP BY TRVH.HEAD_ID,\n");
		sql.append("         TRVH.RETURN_VEHICLE_NO,\n");
		sql.append("         TRVH.ACCOUNT_TYPE_NAME,\n");
		sql.append("         TCC.CODE_DESC,\n");
		sql.append("         TMD.DEALER_SHORTNAME,\n");
		sql.append("         TCU.NAME,\n");
		sql.append("         TRVH.APPLY_AMOUNT,\n");
		sql.append("         TRVH.REASON,\n");
		sql.append("         TRVH.STATUS,\n");
		// sql.append("         TRVH.CHECK_REMARK,\n");
		sql.append("         TRVH.UPDATE_DATE,TRVH.IS_WARE_HOUSING,\n");
		sql.append("         TRVH.CREATE_DATE \n");
		sql.append("   order by trvh.head_id desc\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 经销商退车查询数据
	 */
	public PageResult<Map<String, Object>> returnQueryByDealerOrOrg(Map<String, String> map, int pageSize, int curPage) {
		String status = map.get("status");
		String returnNo = map.get("returnNo");
		String accountType = map.get("accountType");
		String dealerId = map.get("dealerId");
		String orgId = map.get("orgId");
		String checkstartDate = map.get("checkstartDate");
		String checkendDate = map.get("checkendDate");
		String reqStartDate = map.get("reqStartDate");
		String reqEndDate = map.get("reqEndDate");
		String dutyType = map.get("dutyType");
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select trvh.head_id,\n");
		sql.append("       trvh.return_vehicle_no,\n");
		sql.append("       trvh.account_type_name,\n");
		sql.append("       tcc.code_desc status_desc,\n");
		sql.append("       tmd.dealer_shortname,\n");
		sql.append("       tcu.name,\n");
		sql.append("       trvh.apply_amount,\n");
		sql.append("       trvh.reason,\n");
		sql.append("       trvh.status,\n");
		// sql.append("       trvh.check_remark,\n");
		sql.append("       to_char(trvh.update_date, 'yyyy-MM-dd') check_date,\n");
		sql.append("       to_char(trvh.create_date, 'yyyy-MM-dd') applay_create,\n");
		sql.append("       SUM(DECODE(TVRVR.STATUS, " + Constant.RETURN_CAR_STATUS_05 + ", 1, 0)) COU\n");
		sql.append("  from tt_return_vehicle_head trvh,\n");
		sql.append("       tm_dealer             tmd,\n");
		sql.append("       tc_code                tcc,\n");
		sql.append("       tc_user                tcu,\n");
		sql.append("	   TT_VS_RETURN_VEHICLE_REQ TVRVR\n");
		sql.append(" where trvh.dealer_id = tmd.dealer_id\n");
		sql.append("   and trvh.create_by = tcu.user_id\n");
		sql.append("   and trvh.status = tcc.code_id\n");
		sql.append("   AND TVRVR.HEAD_ID = TRVH.HEAD_ID\n");
		if (!CommonUtils.isNullString(status)) {
			sql.append("   and trvh.status = ").append(status).append("\n");
		}
		if (!CommonUtils.isNullString(returnNo)) {
			sql.append("   and trvh.return_vehicle_no like '%").append(returnNo).append("%'\n");
		}
		if (!CommonUtils.isNullString(accountType)) {
			sql.append("   and trvh.account_type_id = ").append(accountType).append("\n");
		}
		//经销商
		if (!CommonUtils.isNullString(dealerId)) {
			sql.append("   and trvh.dealer_id in (").append(dealerId).append(")\n");
		}
        //如果是大区用户 只显示大区下边所有经销商
        if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
            sql.append("AND TMD.DEALER_ID IN(\n" );
            sql.append("    SELECT VOD.DEALER_ID FROM VW_ORG_DEALER_ALL VOD\n" );
            sql.append("    WHERE VOD.ROOT_ORG_ID="+orgId+")\n");
        //如果是小区用户 只显示小区下边所有经销商
        }else if(Constant.DUTY_TYPE_SMALLREGION.toString().equals(dutyType)){
            sql.append("AND TMD.DEALER_ID IN(\n" );
            sql.append("    SELECT TDOR.DEALER_ID FROM TM_DEALER_ORG_RELATION TDOR\n" );
            sql.append("    WHERE TDOR.ORG_ID="+orgId+")\n");
        }
		if (null != checkstartDate && !"".equals(checkstartDate)) {
			sql.append("   AND trvh.UPDATE_DATE >= TO_DATE('" + checkstartDate + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != checkendDate && !"".equals(checkendDate)) {
			sql.append("   AND trvh.UPDATE_DATE  <= TO_DATE('" + checkendDate + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != reqStartDate && !"".equals(reqStartDate)) {
			sql.append("   AND trvh.CREATE_DATE >= TO_DATE('" + reqStartDate + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != reqEndDate && !"".equals(reqEndDate)) {
			sql.append("   AND trvh.CREATE_DATE  <= TO_DATE('" + reqEndDate + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		sql.append("  GROUP BY TRVH.HEAD_ID,\n");
		sql.append("         TRVH.RETURN_VEHICLE_NO,\n");
		sql.append("         TRVH.ACCOUNT_TYPE_NAME,\n");
		sql.append("         TCC.CODE_DESC,\n");
		sql.append("         TMD.DEALER_SHORTNAME,\n");
		sql.append("         TCU.NAME,\n");
		sql.append("         TRVH.APPLY_AMOUNT,\n");
		sql.append("         TRVH.REASON,\n");
		sql.append("         TRVH.STATUS,\n");
		// sql.append("         TRVH.CHECK_REMARK,\n");
		sql.append("         TRVH.UPDATE_DATE,\n");
		sql.append("         TRVH.CREATE_DATE \n");
		sql.append("   order by trvh.head_id desc\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 经销商退车查询数据_下载
	 */
	public List<Map<String, Object>> returnQueryByDealerOrOrgDowload(Map<String, String> map) {
		String status = map.get("status");
		String returnNo = map.get("returnNo");
		String accountType = map.get("accountType");
		String dealerId = map.get("dealerId");
		String orgId = map.get("orgId");
		String checkstartDate = map.get("checkstartDate");
		String checkendDate = map.get("checkendDate");
		String reqStartDate = map.get("reqStartDate");
		String reqEndDate = map.get("reqEndDate");
		String dutyType = map.get("dutyType");
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select trvh.head_id,\n");
		sql.append("       trvh.return_vehicle_no,\n");
		sql.append("       trvh.account_type_name,\n");
		sql.append("       tcc.code_desc status_desc,\n");
		sql.append("       tmd.dealer_shortname,\n");
		sql.append("       tcu.name,\n");
		sql.append("       trvh.apply_amount,\n");
		sql.append("       trvh.reason,\n");
		sql.append("       trvh.status,\n");
		// sql.append("       trvh.check_remark,\n");
		sql.append("       to_char(trvh.update_date, 'yyyy-MM-dd') check_date,\n");
		sql.append("       to_char(trvh.create_date, 'yyyy-MM-dd') applay_create,\n");
		sql.append("       SUM(DECODE(TVRVR.STATUS, " + Constant.RETURN_CAR_STATUS_05 + ", 1, 0)) COU\n");
		sql.append("  from tt_return_vehicle_head trvh,\n");
		sql.append("       tm_dealer             tmd,\n");
		sql.append("       tc_code                tcc,\n");
		sql.append("       tc_user                tcu,\n");
		sql.append("	   TT_VS_RETURN_VEHICLE_REQ TVRVR\n");
		sql.append(" where trvh.dealer_id = tmd.dealer_id\n");
		sql.append("   and trvh.create_by = tcu.user_id\n");
		sql.append("   and trvh.status = tcc.code_id\n");
		sql.append("   AND TVRVR.HEAD_ID = TRVH.HEAD_ID\n");
		if (!CommonUtils.isNullString(status)) {
			sql.append("   and trvh.status = ").append(status).append("\n");
		}
		if (!CommonUtils.isNullString(returnNo)) {
			sql.append("   and trvh.return_vehicle_no like '%").append(returnNo).append("%'\n");
		}
		if (!CommonUtils.isNullString(accountType)) {
			sql.append("   and trvh.account_type_id = ").append(accountType).append("\n");
		}
		//经销商用户
		if (!CommonUtils.isNullString(dealerId)) {
			sql.append("   and trvh.dealer_id in (").append(dealerId).append(")\n");
		}
        //如果是大区用户 只显示大区下边所有经销商
        if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
            sql.append("AND TMD.DEALER_ID IN(\n" );
            sql.append("    SELECT VOD.DEALER_ID FROM VW_ORG_DEALER_ALL VOD\n" );
            sql.append("    WHERE VOD.ROOT_ORG_ID="+orgId+")\n");
        //如果是小区用户 只显示小区下边所有经销商
        }else if(Constant.DUTY_TYPE_SMALLREGION.toString().equals(dutyType)){
            sql.append("AND TMD.DEALER_ID IN(\n" );
            sql.append("    SELECT TDOR.DEALER_ID FROM TM_DEALER_ORG_RELATION TDOR\n" );
            sql.append("    WHERE TDOR.ORG_ID="+orgId+")\n");
        }
		if (null != checkstartDate && !"".equals(checkstartDate)) {
			sql.append("   AND trvh.UPDATE_DATE >= TO_DATE('" + checkstartDate + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != checkendDate && !"".equals(checkendDate)) {
			sql.append("   AND trvh.UPDATE_DATE  <= TO_DATE('" + checkendDate + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != reqStartDate && !"".equals(reqStartDate)) {
			sql.append("   AND trvh.CREATE_DATE >= TO_DATE('" + reqStartDate + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != reqEndDate && !"".equals(reqEndDate)) {
			sql.append("   AND trvh.CREATE_DATE  <= TO_DATE('" + reqEndDate + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		sql.append("  GROUP BY TRVH.HEAD_ID,\n");
		sql.append("         TRVH.RETURN_VEHICLE_NO,\n");
		sql.append("         TRVH.ACCOUNT_TYPE_NAME,\n");
		sql.append("         TCC.CODE_DESC,\n");
		sql.append("         TMD.DEALER_SHORTNAME,\n");
		sql.append("         TCU.NAME,\n");
		sql.append("         TRVH.APPLY_AMOUNT,\n");
		sql.append("         TRVH.REASON,\n");
		sql.append("         TRVH.STATUS,\n");
		// sql.append("         TRVH.CHECK_REMARK,\n");
		sql.append("         TRVH.UPDATE_DATE,\n");
		sql.append("         TRVH.CREATE_DATE \n");
		sql.append("   order by trvh.head_id desc\n");
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	/***
	 * 财务查询审核列表
	 * */
	public PageResult<Map<String, Object>> returnFinfinalQuery(Map<String, String> map,AclUserBean logonUser,int pageSize, int curPage) {
		// String dealerSql = dao.getDealerIdByPostSql(logonUser);
		String returnNo = map.get("returnNo");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT TH.HEAD_ID,");		
		sql.append("TH.RETURN_VEHICLE_NO,");
		sql.append("V.DEALER_SHORTNAME,");		
		sql.append("TH.REASON,");		
		sql.append("TO_CHAR(TH.CREATE_DATE, 'yyyy-MM-dd') APPLAY_CREATE,");		
		sql.append("TH.APPLY_AMOUNT,");		
		sql.append("SUM(DECODE(tr.STATUS, "+Constant.RETURN_CAR_STATUS_04+", 0, 1)) COU \n");		
		sql.append("FROM TT_RETURN_VEHICLE_HEAD   TH,");		
		sql.append("TT_VS_RETURN_VEHICLE_REQ TR,");		
		sql.append("VW_ORG_DEALER_ALL        V \n");		
		sql.append("WHERE TH.HEAD_ID = TR.HEAD_ID AND TR.DEALER_ID = V.DEALER_ID \n");		
		sql.append("AND TH.STATUS="+Constant.RETURN_CAR_STATUS_04+" \n");	
		if (!CheckUtil.checkNull(returnNo)) {
			sql.append("  and th.return_vehicle_no like '%").append(returnNo.trim()).append("%'\n");
		}
		sql.append(" and v.COMPANY_ID=" + logonUser.getCompanyId() + " ");
		//因为财务是属于车厂，所以不用这种方式，速度更快
		// sql.append("AND exists(" + dealerSql + " and v.DEALER_ID=th.DEALER_ID) \n");
		sql.append("GROUP BY th.head_id,");		
		sql.append("th.return_vehicle_no,");		
		sql.append("v.DEALER_SHORTNAME,");		
		sql.append("th.create_date,");		
		sql.append("TH.REASON,");		
		sql.append("TH.APPLY_AMOUNT \n");		
		sql.append("order by th.head_id desc");		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public List<Map<String, Object>> returnQuery(Map<String, String> map) {
		String status = map.get("status");
		String returnNo = map.get("returnNo");
		String accountType = map.get("accountType");
		String dealerId = map.get("dealerId");
		String orgId = map.get("orgId");
		String checkstartDate = map.get("checkstartDate");
		String checkendDate = map.get("checkendDate");
		String reqStartDate = map.get("reqStartDate");
		String reqEndDate = map.get("reqEndDate");
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select trvh.head_id,\n");
		sql.append("       trvh.return_vehicle_no,\n");
		sql.append("       trvh.account_type_name,\n");
		sql.append("       tcc.code_desc status_desc,\n");
		sql.append("       tmd.dealer_shortname,\n");
		sql.append("       tcu.name,\n");
		sql.append("       trvh.apply_amount,\n");
		sql.append("       trvh.reason,\n");
		sql.append("       trvh.status,\n");
		sql.append("       trvh.check_remark,\n");
		sql.append("       to_char(trvh.update_date, 'yyyy-mm-dd') check_date,\n");
		sql.append("       to_char(trvh.create_date, 'yyyy-mm-dd') applay_create\n");
		sql.append("  from tt_return_vehicle_head trvh,\n");
		sql.append("       tm_dealer             tmd,\n");
		sql.append("       tc_code                tcc,\n");
		sql.append("       tc_user                tcu\n");
		sql.append(" where trvh.dealer_id = tmd.dealer_id\n");
		sql.append("   and trvh.create_by = tcu.user_id\n");
		sql.append("   and trvh.status = tcc.code_id\n");
		if (!CommonUtils.isNullString(status)) {
			sql.append("   and trvh.status = ").append(status).append("\n");
		}
		if (!CommonUtils.isNullString(returnNo)) {
			sql.append("   and trvh.return_vehicle_no like '%").append(returnNo).append("%'\n");
		}
		if (!CommonUtils.isNullString(accountType)) {
			sql.append("   and trvh.account_type_id = ").append(accountType).append("\n");
		}
		if (!CommonUtils.isNullString(dealerId)) {
			sql.append("   and trvh.dealer_id in (").append(dealerId).append(")\n");
		}
		if (!CommonUtils.isNullString(orgId)) {
			sql.append("   and exists (select 1 from vw_org_dealer vod where vod.dealer_id = trvh.dealer_id and vod.root_org_id = ").append(orgId).append(")\n");
		}
		if (null != checkstartDate && !"".equals(checkstartDate)) {
			sql.append("   AND trvh.UPDATE_DATE >= TO_DATE('" + checkstartDate + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != checkendDate && !"".equals(checkendDate)) {
			sql.append("   AND trvh.UPDATE_DATE  <= TO_DATE('" + checkendDate + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != reqStartDate && !"".equals(reqStartDate)) {
			sql.append("   AND trvh.CREATE_DATE >= TO_DATE('" + reqStartDate + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != reqEndDate && !"".equals(reqEndDate)) {
			sql.append("   AND trvh.CREATE_DATE  <= TO_DATE('" + reqEndDate + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		sql.append("   order by trvh.head_id desc\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public List<Map<String, Object>> returnDtlQuery(Map<String, String> map) {
		String headId = map.get("headId");
		String status = map.get("status");
		StringBuffer sql = new StringBuffer("\n");
		sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       TMV.VIN,\n");
		sql.append("       TMV.DEALER_ID,\n");
		sql.append("       TMV.AREA_ID, --VIN\n");
		sql.append("       TDW.WAREHOUSE_NAME, --位置说明\n");
		sql.append("       TMD.DEALER_SHORTNAME,\n");
		sql.append("       TMV.LIFE_CYCLE, --库存状态\n");
		sql.append("       TVRVR.REQ_ID,\n");
		sql.append("       TVRVR.STATUS,\n");
		sql.append("       TVRVR.CHECK_REMARK,\n");
		sql.append("       (SELECT TCO.CODE_DESC\n");
		sql.append("          FROM TC_CODE TCO\n");
		sql.append("         WHERE TCO.CODE_ID = TMV.LIFE_CYCLE) AS LIFE_CYCLE_DESC, --库存状态描述\n");
		sql.append("       TO_CHAR(TVRVR.UPDATE_DATE, 'YYYY-MM-DD') AS CHECK_DATE, --入库日期\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'YYYY-MM-DD') AS STORAGE_TIME, --入库日期\n");
		sql.append("       (CEIL(SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT, --库存天数\n");
		sql.append("       NVL(TEMP.TYPE_ID, NULL) TYPE_ID,\n");
		sql.append("       NVL(TEMP.TYPE_NAME, '未知') TYPE_NAME,\n");
		sql.append("       NVL(TEMP.SINGLE_PRICE, NULL) SINGLE_PRICE\n");
		sql.append("  FROM TM_VEHICLE TMV,\n");
		sql.append("       TT_VS_RETURN_VEHICLE_REQ TVRVR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL TMVM,\n");
		sql.append("       TM_DEALER TMD,\n");
		sql.append("       TM_DEALER_WAREHOUSE TDW,\n");
		sql.append("       (SELECT TVAT.TYPE_ID,\n");
		sql.append("               TVAT.TYPE_NAME,\n");
		sql.append("               TVD.DELIVERY_NO,\n");
		sql.append("               TVDRD.SINGLE_PRICE,\n");
		sql.append("               TVDM.VEHICLE_ID\n");
		sql.append("          FROM TT_VS_DLVRY TVD,\n");
		sql.append("               TT_VS_DLVRY_DTL TVDD,\n");
		sql.append("               TT_VS_DLVRY_REQ_DTL TVDRD,\n");
		sql.append("               (SELECT VEHICLE_ID, MAX(DELIVERY_DETAIL_ID) DELIVERY_DETAIL_ID\n");
		sql.append("                  FROM TT_VS_DLVRY_MCH\n");
		sql.append("                 GROUP BY VEHICLE_ID) TVDM,\n");
		sql.append("               TT_VS_ACCOUNT_TYPE TVAT\n");
		sql.append("         WHERE TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n");
		sql.append("           AND TVDD.DETAIL_ID = TVDM.DELIVERY_DETAIL_ID\n");
		sql.append("           AND TVDD.REQ_DTL_ID = TVDRD.DETAIL_ID\n");
		sql.append("           AND TVD.FUND_TYPE = TVAT.TYPE_ID) TEMP\n");
		sql.append(" WHERE TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID(+)\n");
		sql.append("   AND TMV.VEHICLE_ID = TEMP.VEHICLE_ID(+)\n");
		sql.append("   AND TVRVR.VEHICLE_ID = TMV.VEHICLE_ID\n");
		if (!CommonUtils.isNullString(status)) {
			sql.append("   AND TVRVR.STATUS = ").append(status).append("\n");
		}
		if (!CommonUtils.isNullString(headId)) {
			sql.append("   AND TVRVR.HEAD_ID = ").append(headId).append("\n");
		}
		sql.append(" ORDER BY TMV.STORAGE_DATE DESC\n");
		return super.pageQuery(sql.toString(), null, super.getFunName());
	}
	
	public List<Map<String, Object>> returnDtlQuery_SUZUKI(Map<String, String> map) {
		String headId = map.get("headId");
		String status = map.get("status");
		StringBuffer sql = new StringBuffer("\n");
		sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       TMV.VIN,\n");
		sql.append("       TMV.DEALER_ID,\n");
		sql.append("       TMV.AREA_ID, --VIN\n");
		sql.append("       TDW.WAREHOUSE_NAME, --位置说明\n");
		sql.append("       TMD.DEALER_SHORTNAME,\n");
		sql.append("       TMV.LIFE_CYCLE, --库存状态\n");
		sql.append("       TVRVR.REQ_ID,\n");
		sql.append("       TVRVR.STATUS,\n");
		// sql.append("       TVRVR.CHECK_REMARK,\n");
		sql.append("       (SELECT TCO.CODE_DESC\n");
		sql.append("          FROM TC_CODE TCO\n");
		sql.append("         WHERE TCO.CODE_ID = TMV.LIFE_CYCLE) AS LIFE_CYCLE_DESC, --库存状态描述\n");
		sql.append("       TO_CHAR(TVRVR.UPDATE_DATE, 'YYYY-MM-DD') AS CHECK_DATE, --入库日期\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'YYYY-MM-DD') AS STORAGE_TIME, --入库日期\n");
		sql.append("       (CEIL(SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT, --库存天数\n");
		sql.append("       NVL(TVRVR.SINGLE_PRICE, NULL) SINGLE_PRICE\n");
		sql.append("  FROM TM_VEHICLE TMV,\n");
		sql.append("       TT_VS_RETURN_VEHICLE_REQ TVRVR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL TMVM,\n");
		sql.append("       TM_DEALER TMD,\n");
		sql.append("       TM_DEALER_WAREHOUSE TDW\n");
		sql.append(" WHERE TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TDW.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID(+)\n");
		sql.append("   AND TVRVR.VEHICLE_ID = TMV.VEHICLE_ID\n");
		if (!CommonUtils.isNullString(status)) {
			sql.append("   AND TVRVR.STATUS = ").append(status).append("\n");
		}
		if (!CommonUtils.isNullString(headId)) {
			sql.append("   AND TVRVR.HEAD_ID = ").append(headId).append("\n");
		}
		sql.append(" ORDER BY TMV.STORAGE_DATE DESC\n");
		return super.pageQuery(sql.toString(), null, super.getFunName());
	}
	
	/**
	 * 储运入库查询的列表
	 * */
	public List<Map<String, Object>> returnDtlInWare(Map<String, String> map) {
		String headId = map.get("headId");
		String status = map.get("status");
		StringBuffer sql = new StringBuffer("\n");
		sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       TMV.VIN,\n");
		sql.append("       TMV.DEALER_ID,\n");
		sql.append("       TMV.AREA_ID, --VIN\n");
		sql.append("       TDW.WAREHOUSE_NAME, --位置说明\n");
		sql.append("       TMD.DEALER_SHORTNAME,\n");
		sql.append("       TMV.LIFE_CYCLE, --库存状态\n");
		sql.append("       TVRVR.REQ_ID,\n");
		sql.append("       TVRVR.STATUS,\n");
		// sql.append("       TVRVR.CHECK_REMARK,\n");
		sql.append("       (SELECT TCO.CODE_DESC\n");
		sql.append("          FROM TC_CODE TCO\n");
		sql.append("         WHERE TCO.CODE_ID = TMV.LIFE_CYCLE) AS LIFE_CYCLE_DESC, --库存状态描述\n");
		sql.append("       TO_CHAR(TVRVR.UPDATE_DATE, 'YYYY-MM-DD') AS CHECK_DATE, --入库日期\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'YYYY-MM-DD') AS STORAGE_TIME, --入库日期\n");
		sql.append("       (CEIL(SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT, --库存天数\n");
		sql.append("       NVL(TVRVR.SINGLE_PRICE, NULL) SINGLE_PRICE\n");
		sql.append("  FROM TM_VEHICLE TMV,\n");
		sql.append("       TT_VS_RETURN_VEHICLE_REQ TVRVR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL TMVM,\n");
		sql.append("       TM_DEALER TMD,\n");
		sql.append("       TM_DEALER_WAREHOUSE TDW\n");
		sql.append(" WHERE TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TDW.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID(+)\n");
		sql.append("   AND TVRVR.VEHICLE_ID = TMV.VEHICLE_ID\n");
		if (!CommonUtils.isNullString(status)) {
			sql.append("   AND TVRVR.STATUS = ").append(status).append("\n");
		}
		if (!CommonUtils.isNullString(headId)) {
			sql.append("   AND TVRVR.HEAD_ID = ").append(headId).append("\n");
		}
		sql.append(" ORDER BY TMV.STORAGE_DATE DESC\n");
		return super.pageQuery(sql.toString(), null, super.getFunName());
	}
	
	/**
	 * 退车单明细
	 * */
	public List<Map<String, Object>> returnDtlQueryNew(Map<String, String> map) {
		String headId = map.get("headId");
		// String status = map.get("status");
		StringBuffer sql = new StringBuffer("\n");
		sql.append("SELECT TVH.RETURN_VEHICLE_NO,TVH.STATUS,TV.LIFE_CYCLE,TVR.REQ_ID,TVR.CHECK_REMARK,TVR.SINGLE_PRICE,");
		sql.append("		TV.DEALER_SHORTNAME,VI.MODEL_NAME,VI.MATERIAL_CODE,VI.MATERIAL_NAME,tv.VIN, \n");
		sql.append("		to_char(tvr.update_date,'yyyy-MM-dd') CHECK_DATE,TVR.CHECK_SALE_REMARK,TVR.CHECK_STORAGE_REMARK,TVR.CHECK_FINANCE_REMARK \n");
		sql.append("FROM TT_RETURN_VEHICLE_HEAD TVH,TT_VS_RETURN_VEHICLE_REQ TVR,TM_VEHICLE TV,VW_MATERIAL_INFO VI \n");
		sql.append("WHERE TVH.HEAD_ID=TVR.HEAD_ID AND TVR.VEHICLE_ID=TV.VEHICLE_ID AND TV.MATERIAL_ID=VI.MATERIAL_ID \n");
		sql.append("		AND TVH.HEAD_ID="+headId);
		/*sql.append("SELECT G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       G1.GROUP_NAME AS PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       TMV.VIN,\n");
		sql.append("       TMV.DEALER_ID,\n");
		sql.append("       TMV.AREA_ID, --VIN\n");
		sql.append("       TMD.DEALER_SHORTNAME,\n");
		sql.append("       TMV.LIFE_CYCLE, --库存状态\n");
		sql.append("       TVRVR.REQ_ID,\n");
		sql.append("       TVRVR.STATUS,\n");
		sql.append("       TVRVR.CHECK_REMARK,\n");
		sql.append("       (SELECT TCO.CODE_DESC\n");
		sql.append("          FROM TC_CODE TCO\n");
		sql.append("         WHERE TCO.CODE_ID = TMV.LIFE_CYCLE) AS LIFE_CYCLE_DESC, --库存状态描述\n");
		sql.append("       TO_CHAR(TVRVR.UPDATE_DATE, 'YYYY-MM-DD') AS CHECK_DATE, --入库日期\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'YYYY-MM-DD') AS STORAGE_TIME, --入库日期\n");
		sql.append("       (CEIL(SYSDATE - TMV.STORAGE_DATE)) AS DAY_COUNT, --库存天数\n");
		sql.append("       NVL(TEMP.TYPE_ID, NULL) TYPE_ID,\n");
		sql.append("       NVL(TEMP.TYPE_NAME, '未知') TYPE_NAME,\n");
		sql.append("       NVL(TEMP.SINGLE_PRICE, NULL) SINGLE_PRICE\n");
		sql.append("  FROM TM_VEHICLE TMV,\n");
		sql.append("       TT_VS_RETURN_VEHICLE_REQ TVRVR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");
		sql.append("       TM_VHCL_MATERIAL TMVM,\n");
		sql.append("       TM_DEALER TMD,\n");
		sql.append("       (SELECT TVAT.TYPE_ID,\n");
		sql.append("               TVAT.TYPE_NAME,\n");
		sql.append("               TVD.DELIVERY_NO,\n");
		sql.append("               TVDRD.SINGLE_PRICE,\n");
		sql.append("               TVDM.VEHICLE_ID\n");
		sql.append("          FROM TT_VS_DLVRY TVD,\n");
		sql.append("               TT_VS_DLVRY_DTL TVDD,\n");
		sql.append("               TT_VS_DLVRY_REQ_DTL TVDRD,\n");
		sql.append("               (SELECT VEHICLE_ID, MAX(DELIVERY_DETAIL_ID) DELIVERY_DETAIL_ID\n");
		sql.append("                  FROM TT_VS_DLVRY_MCH\n");
		sql.append("                 GROUP BY VEHICLE_ID) TVDM,\n");
		sql.append("               TT_VS_ACCOUNT_TYPE TVAT\n");
		sql.append("         WHERE TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n");
		sql.append("           AND TVDD.DETAIL_ID = TVDM.DELIVERY_DETAIL_ID\n");
		sql.append("           AND TVDD.REQ_DTL_ID = TVDRD.DETAIL_ID\n");
		sql.append("           AND TVD.FUND_TYPE = TVAT.TYPE_ID) TEMP\n");
		sql.append(" WHERE TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G1.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TVRVR.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("   AND TMV.VEHICLE_ID = TEMP.VEHICLE_ID(+)\n");
		sql.append("   AND TVRVR.VEHICLE_ID = TMV.VEHICLE_ID\n");
		if (!CommonUtils.isNullString(status)) {
			sql.append("   AND TVRVR.STATUS = ").append(status).append("\n");
		}
		if (!CommonUtils.isNullString(headId)) {
			sql.append("   AND TVRVR.HEAD_ID = ").append(headId).append("\n");
		}
		sql.append(" ORDER BY TMV.STORAGE_DATE DESC\n");*/
		return super.pageQuery(sql.toString(), null, super.getFunName());
	}
	/**
	 * 根据PO获取PO信息
	 * 
	 * @param id
	 * @return
	 */
	public PO getPoObject(PO po) {
		List<PO> list = factory.select(po);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
	/**
	 * 
	 * @param headId
	 * @return
	 */
	public List<Map<String, Object>> getCuxErpReturn(String headId) {
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("SELECT R.DEALER_ID,\n" );
		sqlStr.append("       R.HEAD_ID,\n" );
		sqlStr.append("       R.PRICE_LIST_ID,\n" );
		sqlStr.append("       L.MATERIAL_CODE,\n" );
		sqlStr.append("       COUNT(L.MATERIAL_CODE) AS APPLY_COUNT \n" );
		sqlStr.append("  FROM TT_VS_RETURN_VEHICLE_REQ R, TM_VEHICLE V, TM_VHCL_MATERIAL L\n" );
		sqlStr.append(" WHERE R.VEHICLE_ID = V.VEHICLE_ID\n" );
		sqlStr.append("   AND V.MATERIAL_ID = L.MATERIAL_ID\n" );
		sqlStr.append("   AND R.HEAD_ID = "+headId+"\n" );
		sqlStr.append(" GROUP BY R.DEALER_ID, R.HEAD_ID, R.PRICE_LIST_ID, L.MATERIAL_CODE");
		return super.pageQuery(sqlStr.toString(), null, super.getFunName());
	}
	
	
	/***
	 * 检查车辆是否失效
	 * */
    public String getCheckCode(String[] codes){
    	String code="";
    	int count = 0;
    	for(int i =0 ; i<codes.length; i++){
    		StringBuffer sql= new StringBuffer();
    		sql.append(" select count(1) counts from CUX_INACTIVE_FG_V@DMS2EBS2 WHERE  item_code = '"+codes[i]+"'");
    		List<Map<String,Object>> list=super.pageQuery(sql.toString(), null, super.getFunName());
    		count=Integer.parseInt(list.get(0).get("COUNTS").toString());
    		if(count >= 1){
    			code += codes[i]+",";
    		}
    	}
    	return code;	
    }
    
    /***
     * 驳回时，将车辆变成经销商申请之前的状态
     * */
    public void commonChangeVechiceStatus(Long headId,Long userId) {
    	//更改退车状态
    	StringBuilder sqlTwo = new StringBuilder();
    	sqlTwo.append(" update tt_return_vehicle_head set status="+Constant.RETURN_CAR_STATUS_07+",update_by="+userId+",update_date=sysdate  where head_id="+headId);
    	dao.update(sqlTwo.toString(), null);
    	//将车辆状态改为最初状态
    	StringBuilder sqlThree = new StringBuilder();
    	sqlThree.append(" update TM_VEHICLE set life_cycle="+Constant.VEHICLE_LIFE_03+",lock_status="+Constant.LOCK_STATUS_01+"  \n");
    	sqlThree.append(" where vehicle_id in(select vehicle_id from TT_VS_RETURN_VEHICLE_REQ where head_id="+headId+") ");
    	dao.update(sqlThree.toString(), null);
    }
    
    /***
     * 更改退单明细
     * */
    public void commonChangeReturnDetail(Map<String,String> map) {
    	String reqType = map.get("reqType");
    	StringBuilder sqlOne = new StringBuilder();
    	sqlOne.append(" update TT_VS_RETURN_VEHICLE_REQ   \n");
    	sqlOne.append(" set status="+Integer.parseInt(map.get("status"))+", \n");
    	sqlOne.append(" update_by="+Long.parseLong(map.get("userId"))+", \n");
    	if ("2".equals(reqType)) {
    		//销售部意见
    		sqlOne.append(" check_sale_remark='"+map.get("remark")+"', \n");
    	} else if ("3".equals(reqType)) {
    		//储运部意见
    		sqlOne.append(" check_storage_remark='"+map.get("remark")+"', \n");
    	} else {
    		//区域意见
    		sqlOne.append(" check_remark='"+map.get("remark")+"', \n");
    	}
    	sqlOne.append(" update_date=sysdate \n");
    	sqlOne.append(" where req_id="+Long.parseLong(map.get("reqId")) +" \n");
    	dao.update(sqlOne.toString(), null);
    }
    
    /**
     * 查询仓库
     * */
    public List<Map<String,Object>> getWhereHoseList() {
    	String sql ="select WAREHOUSE_ID,WAREHOUSE_NAME from TM_WAREHOUSE "+
    			" where (warehouse_type="+Constant.SALES_WAREHOUSE_TYPE_IN+" or warehouse_type="+Constant.SALES_WAREHOUSE_TYPE_OUT+") and status=10011001";
    	return dao.pageQuery(sql, null, null);
    }
    
    /**
     * 查询仓库储运
     * */
    public List<Map<String,Object>> getWhereHoseListInWare(String headId) {
    	String sql ="select w.WAREHOUSE_NAME,w.warehouse_id  from TT_VS_RETURN_VEHICLE_REQ h,TM_VEHICLE v,TM_WAREHOUSE w "+
    			" where h.vehicle_id=v.vehicle_id and v.warehouse_id=w.warehouse_id  and  h.head_id="+headId+"  and rownum=1";
    	return dao.pageQuery(sql, null, null);
    }
    
    /**
     * 查询账户
     * */
    public List<Map<String,Object>> getAcounntDetail(String headId) {
    	String sql ="select  a.account_type_id TYPE_ID,a.account_name TYPE_NAME from tt_return_vehicle_head h,tt_vs_account a \n "+
    			"  where h.dealer_id=a.dealer_id and h.head_id="+headId+" and account_code !=2001 ";
    	 return dao.pageQuery(sql, null, null);
    }
    
    /**
     * 根据headId将车辆表仓库ID换成新的仓库ID（车厂的仓库），并把车辆的锁定改为正常，车辆变成车厂库存
     * */
    public void chnageVehiceStatus(String warHouseId,String headId) {
    	StringBuilder sql = new StringBuilder();
    	sql.append("update TM_VEHICLE v \n");
    	sql.append(" set v.life_cycle="+Constant.VEHICLE_LIFE_02+", ");
    	sql.append(" v.lock_status="+Constant.LOCK_STATUS_01+", ");
    	sql.append(" v.warehouse_id= "+warHouseId);
    	sql.append("\n  where exists(select 1 from TT_VS_RETURN_VEHICLE_REQ r where v.vehicle_id=r.vehicle_id and  r.head_id="+Long.parseLong(headId)+") ");
    	dao.update(sql.toString(), null);
    }
    
    /**
     * 给退运单修改状态和账户ID
     * */
    public void changAccountReturn(Long headId,Long acountId,Long userId) {
    	StringBuilder sql = new StringBuilder();
    	sql.append("update tt_return_vehicle_head \n");
    	sql.append("set account_type_id= "+acountId+",");
    	sql.append("update_by= "+userId+",");
    	sql.append("update_date=sysdate,");
    	sql.append("status="+Constant.RETURN_CAR_STATUS_05);
    	sql.append("where head_id="+headId);
    	dao.update(sql.toString(), null);
    }
    
    /**
     * 给当前账户的余额加上总数
     * */
    public void changAccountToatal(Long acountId,Long userId,String infactReturnMoneyTotal,String dealerId) {
    	StringBuilder sql = new StringBuilder();
    	sql.append("update tt_vs_account \n");
    	sql.append("set balance_amount=balance_amount+"+Double.parseDouble(infactReturnMoneyTotal)+", \n");
    	sql.append("update_by= "+userId+", \n");
    	sql.append("update_date=sysdate \n");
    	sql.append("where account_type_id="+acountId);
    	sql.append(" and dealer_id="+dealerId);
    	dao.update(sql.toString(), null);
    }
    
    /**
     * 储运部审核通过（入库）
     * */
    public void changeRetuenStorageStatus(String headId,Long userId) {
    	StringBuilder sql = new StringBuilder();
    	sql.append("update tt_return_vehicle_head \n");
    	sql.append("set status= "+Constant.RETURN_CAR_STATUS_04+",");
    	sql.append("update_by= "+userId+",");
    	sql.append("update_date=sysdate \n");
    	sql.append("where head_id="+headId);
    	dao.update(sql.toString(), null);
    }
    
}
