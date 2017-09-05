package com.infodms.dms.dao.sales.storageManage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class CheckVehicleQueryDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(CheckVehicleQueryDAO.class);
	private static final CheckVehicleDAO dao = new CheckVehicleDAO();

	public static final CheckVehicleDAO getInstance() {
		return dao;
	}

	/***************************************************************************
	 * 查询车辆验收历史记录(DLR)
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getCheckQueryDLR(String dealer_Id, String deliverystartDate, String deliveryendDate, String checkstartDate, String checkendDate, String vin, String dlvNo, int pageSize, int curPage) {

		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT DISTINCT /*+ first_rows(10)*/ tmv.*,\n");
		sql.append("       TO_CHAR(TTTSI.CARRIVEDATE, 'yyyy-MM-dd') TARRIVE_DATE --物流到店日期\n");
		sql.append("FROM (\n");
		sql.append("SELECT TMV.VEHICLE_ID,\n");
		sql.append("       G.GROUP_NAME, --车型名称\n");
		sql.append("       TMVM.MATERIAL_CODE, --物料代码\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料名称\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TO_CHAR(TTD.DELIVERY_DATE, 'yyyy-MM-dd') DELIVERY_DATE, --发车日期\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') STORAGE_DATE, --验收日期\n");
		// sql.append(" TO_CHAR(TTTSI.CARRIVEDATE, 'yyyy-MM-dd') TARRIVE_DATE,
		// --物流到店日期\n");
		sql.append("       TTI.INSPECTION_PERSON, --验收人\n");
		sql.append("       TMW.WAREHOUSE_NAME,\n");
		sql.append("       TTD.DELIVERY_NO,\n");
		sql.append("       TTD.ERP_ORDER,\n");
		sql.append("	   TTI.VEHICLE_AREA,\n");
		sql.append("	   DECODE(TVDR.CALL_LEAVEL,").append(Constant.DEALER_LEVEL_02).append(",\n");
		sql.append("                      TVDR.ORDER_DEALER_ID,\n");
		sql.append("                      TVO.ORDER_ORG_ID) ORDER_DEALER_ID\n");
		sql.append("  FROM TT_VS_INSPECTION          TTI,\n");
		// sql.append(" CA_VDR_ARRIVE TTTSI,\n");
		sql.append("       TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TT_VS_DLVRY_MCH      TTDM,\n");
		sql.append("       TT_VS_DLVRY_DTL     TTDD,\n");
		sql.append("       TT_VS_DLVRY            TTD,\n");
		sql.append("       TT_VS_DLVRY_REQ        TVDR,\n");
		sql.append("       TT_VS_ORDER            TVO,\n");
		sql.append("	   TM_WAREHOUSE         TMW\n");

		sql.append(" WHERE TTI.MATCH_ID = TTDM.MATCH_ID\n");
		// sql.append(" AND TTTSI.VIN(+) = TMV.VIN \n");
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		// sql.append(" and ttd.erp_order = tttsi.salesno\n");
		sql.append("   AND TMV.VEHICLE_ID = TTDM.VEHICLE_ID\n");
		sql.append("   AND TTDM.DELIVERY_DETAIL_ID = TTDD.DETAIL_ID\n");
		sql.append("   AND TTDD.DELIVERY_ID = TTD.DELIVERY_ID\n");
		sql.append("   AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID\n");
		sql.append("   AND TTD.REQ_ID = TVDR.REQ_ID\n");
		sql.append("   AND TVDR.ORDER_ID = TVO.ORDER_ID\n");

		sql.append("   AND TTD.RECEIVER IN(" + dealer_Id + ")\n");

		// sql.append(" AND (TMV.DEALER_ID IN ("+dealer_Id+") OR TTD.RECEIVER
		// IN("+dealer_Id+"))\n");
		// 发车日期
		if (null != deliverystartDate && !"".equals(deliverystartDate)) {
			sql.append("   AND TTD.DELIVERY_DATE >= TO_DATE('" + deliverystartDate + "','yyyy-MM-dd')\n");
		}
		if (null != deliveryendDate && !"".equals(deliveryendDate)) {
			sql.append("   AND TTD.DELIVERY_DATE <= (TO_DATE('" + deliveryendDate + "','yyyy-MM-dd')+1)\n");
		}
		// 验收日期
		if (null != checkstartDate && !"".equals(checkstartDate)) {
			sql.append("   AND TMV.STORAGE_DATE >= TO_DATE('" + checkstartDate + "','yyyy-MM-dd')\n");
		}
		if (null != checkendDate && !"".equals(checkendDate)) {
			sql.append("   AND TMV.STORAGE_DATE <= (TO_DATE('" + checkendDate + "','yyyy-MM-dd')+1)\n");
		}
		// VIN
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != dlvNo && !"".equals(dlvNo)) {
			sql.append("   AND TTD.DELIVERY_NO like UPPER('%" + dlvNo + "%') \n");
		}
		sql.append(") TMV,\n");

		sql.append("       CA_VDR_ARRIVE   TTTSI\n");
		sql.append("   where TTTSI.VIN(+) = TMV.VIN \n");
		sql.append("   and tmv.erp_order = tttsi.salesno(+)\n");
		// sql.append("and TMV.ORDER_DEALER_ID IN
		// (").append(dealer_Id).append(")\n");

		// sql.append(" ORDER BY rownum DESC\n");

		return dao.pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.CheckVehicleQueryDAO.getCheckQueryDLR", pageSize, curPage);
	}

	public static PageResult<Map<String, Object>> getCheckQueryDLR_CSC(String dealer_Id, String deliverystartDate, String deliveryendDate, String checkstartDate, String checkendDate, String vin, String dlvNo, int pageSize, int curPage) {

		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT TVDE.SENDCAR_ORDER_NUMBER,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TV.VIN,\n");
		sql.append("       TV.VEHICLE_ID,\n");

		sql.append("       TDW.WAREHOUSE_NAME,\n");
		sql.append("       TVI.ARRIVE_DATE,\n");
		sql.append("       TVI.INSPECTION_PERSON,\n");

		sql.append("       TVDE.FLATCAR_ASSIGN_DATE,\n");
		sql.append("       TV.STORAGE_DATE\n");

		sql.append("  FROM TT_VS_INSPECTION    TVI,\n");
		sql.append("       TT_VS_DLVRY_ERP     TVDE,\n");
		sql.append("       TT_VS_DLVRY_ERP_DTL TVDED,\n");
		sql.append("       TM_VEHICLE          TV,\n");
		sql.append("       TM_VHCL_MATERIAL    TVM,\n");
		sql.append("       TM_DEALER_WAREHOUSE TDW\n");
		sql.append(" WHERE TVI.DLVRY_DTL_ID = TVDED.DETAIL_ID\n");
		sql.append("   AND TVDE.SENDCAR_HEADER_ID = TVDED.SENDCAR_HEADER_ID\n");
		sql.append("   AND TV.VEHICLE_ID = TVI.VEHICLE_ID\n");
		sql.append("   AND TV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TV.VEHICLE_AREA = TDW.WAREHOUSE_ID\n");

		sql.append("   AND TVI.OPERATE_DEALER IN(" + dealer_Id + ")\n");

		// sql.append(" AND (TMV.DEALER_ID IN ("+dealer_Id+") OR TTD.RECEIVER
		// IN("+dealer_Id+"))\n");
		// 发车日期
		if (null != deliverystartDate && !"".equals(deliverystartDate)) {
			sql.append("   AND TVDE.FLATCAR_ASSIGN_DATE >= TO_DATE('" + deliverystartDate + "','yyyy-MM-dd')\n");
		}
		if (null != deliveryendDate && !"".equals(deliveryendDate)) {
			sql.append("   AND TVDE.FLATCAR_ASSIGN_DATE <= (TO_DATE('" + deliveryendDate + "','yyyy-MM-dd')+1)\n");
		}
		// 验收日期
		if (null != checkstartDate && !"".equals(checkstartDate)) {
			sql.append("   AND TV.STORAGE_DATE >= TO_DATE('" + checkstartDate + "','yyyy-MM-dd')\n");
		}
		if (null != checkendDate && !"".equals(checkendDate)) {
			sql.append("   AND TV.STORAGE_DATE <= (TO_DATE('" + checkendDate + "','yyyy-MM-dd')+1)\n");
		}
		// VIN
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TV"));
		}
		if (null != dlvNo && !"".equals(dlvNo)) {
			sql.append("   AND TVDE.SENDCAR_ORDER_NUMBER like UPPER('%" + dlvNo + "%') \n");
		}

		// sql.append("and TMV.ORDER_DEALER_ID IN
		// (").append(dealer_Id).append(")\n");

		// sql.append(" ORDER BY rownum DESC\n");

		return dao.pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.CheckVehicleQueryDAO.getCheckQueryDLR", pageSize, curPage);
	}
	public static PageResult<Map<String, Object>> getCheckQueryDLR_CSCTOTAL(String dealer_Id, String deliverystartDate, String deliveryendDate, String checkstartDate, String checkendDate, String vin, String dlvNo, int pageSize, int curPage) {

		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT \n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		
		sql.append("       COUNT(1) COUNT \n");

		sql.append("  FROM TT_VS_INSPECTION    TVI,\n");
		sql.append("       TT_VS_DLVRY_ERP     TVDE,\n");
		sql.append("       TT_VS_DLVRY_ERP_DTL TVDED,\n");
		sql.append("       TM_VEHICLE          TV,\n");
		sql.append("       TM_VHCL_MATERIAL    TVM,\n");
		sql.append("       TM_DEALER_WAREHOUSE TDW\n");
		sql.append(" WHERE TVI.DLVRY_DTL_ID = TVDED.DETAIL_ID\n");
		sql.append("   AND TVDE.SENDCAR_HEADER_ID = TVDED.SENDCAR_HEADER_ID\n");
		sql.append("   AND TV.VEHICLE_ID = TVI.VEHICLE_ID\n");
		sql.append("   AND TV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TV.VEHICLE_AREA = TDW.WAREHOUSE_ID\n");

		sql.append("   AND TVI.OPERATE_DEALER IN(" + dealer_Id + ")\n");

		// sql.append(" AND (TMV.DEALER_ID IN ("+dealer_Id+") OR TTD.RECEIVER
		// IN("+dealer_Id+"))\n");
		// 发车日期
		if (null != deliverystartDate && !"".equals(deliverystartDate)) {
			sql.append("   AND TVDE.FLATCAR_ASSIGN_DATE >= TO_DATE('" + deliverystartDate + "','yyyy-MM-dd')\n");
		}
		if (null != deliveryendDate && !"".equals(deliveryendDate)) {
			sql.append("   AND TVDE.FLATCAR_ASSIGN_DATE <= (TO_DATE('" + deliveryendDate + "','yyyy-MM-dd')+1)\n");
		}
		// 验收日期
		if (null != checkstartDate && !"".equals(checkstartDate)) {
			sql.append("   AND TV.STORAGE_DATE >= TO_DATE('" + checkstartDate + "','yyyy-MM-dd')\n");
		}
		if (null != checkendDate && !"".equals(checkendDate)) {
			sql.append("   AND TV.STORAGE_DATE <= (TO_DATE('" + checkendDate + "','yyyy-MM-dd')+1)\n");
		}
		// VIN
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TV"));
		}
		if (null != dlvNo && !"".equals(dlvNo)) {
			sql.append("   AND TVDE.SENDCAR_ORDER_NUMBER like UPPER('%" + dlvNo + "%') \n");
		}

		// sql.append("and TMV.ORDER_DEALER_ID IN
		// (").append(dealer_Id).append(")\n");

		 sql.append(" GROUP  BY TVM.MATERIAL_CODE, TVM.MATERIAL_NAME\n");

		return dao.pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.CheckVehicleQueryDAO.getCheckQueryDLR", pageSize, curPage);
	}
	/***************************************************************************
	 * 查询车辆验收详细历史记录:基本信息(DLR)
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getCheckVehicleQueryDLR_Detail(String vehicle_id) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TTI.VEHICLE_ID, --查询验收基本信息\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TO_CHAR(TTD.DELIVERY_DATE, 'yyyy-MM-dd') DELIVERY_DATE, --发车日期\n");
		sql.append("       G.GROUP_NAME, --车型\n");
		sql.append("       TMV.ENGINE_NO, --发动机号\n");
		sql.append("       TMV.COLOR, --颜色\n");
		sql.append("       TO_CHAR(TTI.ARRIVE_DATE, 'yyyy-MM-dd') ARRIVE_DATE, --实际到车日期\n");
		sql.append("       TTI.ARRIVE_TIME, --实际到车时间\n");
		sql.append("       TO_CHAR(TTTSI.CARRIVEDATE, 'yyyy-MM-dd hh24:mi:ss') TARRIVE_TIME, --物流登记到车时间\n");
		sql.append("       TTI.TRANSPORT_PERSON, --送车人员\n");
		sql.append("       TTI.INSPECTION_NO, --验收单号\n");
		sql.append("       TTI.INSPECTION_PERSON, --验收人员\n");
		sql.append("       TTI.REMARK, --备注\n");
		sql.append("       TMW.WAREHOUSE_NAME,\n");
		sql.append("       TVDE.SENDCAR_ORDER_NUMBER,\n");
		sql.append("       TDW.WAREHOUSE_NAME VEHICLE_AREA\n");
		sql.append("  FROM TT_VS_INSPECTION       TTI,\n");
		sql.append("       CA_VDR_ARRIVE   TTTSI,\n");
		sql.append("       TT_VS_DLVRY_MCH        TTDM,\n");
		sql.append("       Tt_Vs_Dlvry_Erp        tvde,\n");
		sql.append("       TT_VS_DLVRY_DTL        TTDD,\n");
		sql.append("       TT_VS_DLVRY            TTD,\n");
		sql.append("       TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       Tm_Dealer_Warehouse    TDW,\n");
		sql.append("       TM_WAREHOUSE           TMW\n");
		sql.append(" WHERE TTI.VEHICLE_ID = " + vehicle_id + "\n");
		sql.append("   AND TTTSI.VIN(+) = TMV.VIN \n");
		sql.append("   AND TTI.VEHICLE_ID = TTDM.VEHICLE_ID\n");
		sql.append("   AND TVDE.SENDCAR_HEADER_ID = TTDM.ERP_SENDCAR_ID\n");
		sql.append("   AND TTDM.DELIVERY_DETAIL_ID = TTDD.DETAIL_ID\n");
		sql.append("   AND TTDD.DELIVERY_ID = TTD.DELIVERY_ID\n");
		sql.append("   AND TTI.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID\n");
		sql.append("   AND TDW.WAREHOUSE_ID = TMV.VEHICLE_AREA\n");

		return dao.pageQueryMap(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.CheckVehicleQueryDAO.getCheckVehicleQueryDLR_Detail");
	}

	public static Map<String, Object> getCheckVehicleQueryDLR_Detail_SUZUKI(String vehicle_id) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TVI.VEHICLE_ID, --查询验收基本信息\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TO_CHAR(TVDE.FLATCAR_ASSIGN_DATE, 'YYYY-MM-DD') DELIVERY_DATE, --发车日期\n");
		sql.append("       G.GROUP_NAME, --车型\n");
		sql.append("       TMV.ENGINE_NO, --发动机号\n");
		sql.append("       TMV.COLOR, --颜色\n");
		sql.append("       TO_CHAR(TVI.ARRIVE_DATE, 'YYYY-MM-DD') ARRIVE_DATE, --实际到车日期\n");
		sql.append("       TVI.ARRIVE_TIME, --实际到车时间\n");
		sql.append("       TVDE.MOTORMAN, --送车人员\n");
		sql.append("       TVI.INSPECTION_NO, --验收单号\n");
		sql.append("       TVI.INSPECTION_PERSON, --验收人员\n");
		sql.append("       TVI.REMARK, --备注\n");
		sql.append("       TMW.WAREHOUSE_NAME,\n");
		sql.append("       TVDE.SENDCAR_ORDER_NUMBER,\n");
		sql.append("       TDW.WAREHOUSE_NAME VEHICLE_AREA\n");
		sql.append("  FROM TT_VS_INSPECTION       TVI,\n");
		sql.append("       TT_VS_DLVRY_ERP        TVDE,\n");
		sql.append("       TT_VS_DLVRY_ERP_DTL    TVDED,\n");
		sql.append("       TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_DEALER_WAREHOUSE    TDW,\n");
		sql.append("       TM_WAREHOUSE           TMW\n");
		sql.append(" WHERE TVI.VEHICLE_ID = " + vehicle_id + "\n");
		sql.append("   AND TVI.DLVRY_DTL_ID = TVDED.DETAIL_ID\n");
		sql.append("   AND TVI.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("   AND TVDE.SENDCAR_HEADER_ID = TVDED.SENDCAR_HEADER_ID\n");
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID\n");
		sql.append("   AND TDW.WAREHOUSE_ID = TMV.VEHICLE_AREA");

		return dao.pageQueryMap(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.CheckVehicleQueryDAO.getCheckVehicleQueryDLR_Detail");
	}

	/***************************************************************************
	 * 查询车辆验收历史记录(OEM)
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getCheckQueryOEM(String orgId, String dealerCode, String deliverystartDate, String deliveryendDate, String checkstartDate, String checkendDate, String vin, String areaIds, int pageSize, int curPage) {

		StringBuffer sql = new StringBuffer("\n");
		List<Object> params = new LinkedList<Object>();

		/*
		 * sql.append("SELECT *\n"); sql.append("FROM ("); sql.append("SELECT
		 * DISTINCT TMV.VEHICLE_ID,\n"); sql.append("
		 * TMD.DEALER_SHORTNAME,--经销商名称 \n"); sql.append(" G.GROUP_NAME,
		 * --车型名称\n"); sql.append(" TMVM.MATERIAL_CODE, --物料代码\n"); sql.append("
		 * TMVM.MATERIAL_NAME, --物料名称\n"); sql.append(" TMV.VIN, --VIN\n");
		 * sql.append(" TMW.WAREHOUSE_NAME,\n"); sql.append("
		 * TO_CHAR(TTD.DELIVERY_DATE, 'yyyy-MM-dd') DELIVERY_DATE, --发车日期\n");
		 * sql.append(" TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') STORAGE_DATE,
		 * --验收日期\n"); sql.append(" TO_CHAR(TTTSI.CARRIVEDATE, 'yyyy-MM-dd')
		 * TARRIVE_DATE, --物流到店日期\n"); sql.append(" TTI.INSPECTION_PERSON,
		 * --验收人\n"); sql.append(" TTI.VEHICLE_AREA\n"); sql.append(" FROM
		 * TT_VS_INSPECTION TTI,\n"); sql.append(" CA_VDR_ARRIVE TTTSI,\n");
		 * sql.append(" TM_VEHICLE TMV,\n"); sql.append(" TM_VHCL_MATERIAL_GROUP
		 * G,\n"); sql.append(" TM_VHCL_MATERIAL TMVM,\n"); sql.append("
		 * TT_VS_DLVRY_MCH TTDM,\n"); sql.append(" TT_VS_DLVRY_DTL TTDD,\n");
		 * sql.append(" TT_VS_DLVRY TTD,\n"); sql.append(" TM_DEALER TMD, \n");
		 * sql.append(" TM_WAREHOUSE TMW \n");
		 * 
		 * sql.append(" WHERE TTI.VEHICLE_ID = TMV.VEHICLE_ID\n"); sql.append("
		 * AND TTTSI.VIN(+) = TMV.VIN \n"); sql.append(" AND TMV.MODEL_ID =
		 * G.GROUP_ID\n"); sql.append(" AND TMV.MATERIAL_ID =
		 * TMVM.MATERIAL_ID\n"); sql.append(" AND TMV.VEHICLE_ID =
		 * TTDM.VEHICLE_ID\n"); sql.append(" AND TTDM.DELIVERY_DETAIL_ID =
		 * TTDD.DETAIL_ID\n"); sql.append(" AND TTDD.DELIVERY_ID =
		 * TTD.DELIVERY_ID\n"); sql.append(" AND TMV.DEALER_ID = TMD.DEALER_ID
		 * \n"); sql.append(" AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID \n");
		 * //经销商 if (null != dealerCode && !"".equals(dealerCode)) {
		 * sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,
		 * "TMD", "DEALER_CODE")); } //发车日期 if (null != deliverystartDate &&
		 * !"".equals(deliverystartDate)) { sql.append(" AND TTD.DELIVERY_DATE >=
		 * TO_DATE('"+deliverystartDate+"','yyyy-MM-dd')\n"); } if (null !=
		 * deliveryendDate && !"".equals(deliveryendDate)) { sql.append(" AND
		 * TTD.DELIVERY_DATE <=
		 * (TO_DATE('"+deliveryendDate+"','yyyy-MM-dd')+1)\n"); } //验收日期 if
		 * (null != checkstartDate && !"".equals(checkstartDate)) { sql.append("
		 * AND TMV.STORAGE_DATE >=
		 * TO_DATE('"+checkstartDate+"','yyyy-MM-dd')\n"); } if (null !=
		 * checkendDate && !"".equals(checkendDate)) { sql.append(" AND
		 * TMV.STORAGE_DATE <= (TO_DATE('"+checkendDate+"','yyyy-MM-dd')+1)\n"); }
		 * //VIN if (null != vin && !"".equals(vin)) {
		 * sql.append(GetVinUtil.getVins(vin,"TMV")); } if (null != areaIds &&
		 * !"".equals(areaIds)) { sql.append(" AND TMV.AREA_ID IN
		 * ("+areaIds+")\n"); }
		 * 
		 * if(!CommonUtils.isNullString(orgId)) {
		 * sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId)) ; }
		 * 
		 * sql.append(") TMV"); sql.append(" ORDER BY TMV.STORAGE_DATE DESC\n");
		 */

		sql.append("select t.VEHICLE_ID,\n");
		sql.append("       t.DEALER_SHORTNAME, --经销商名称\n");
		sql.append("       t.GROUP_NAME, --车型名称\n");
		sql.append("       t.MATERIAL_CODE, --物料代码\n");
		sql.append("       t.MATERIAL_NAME, --物料名称\n");
		sql.append("       t.VIN, --VIN\n");
		sql.append("       t.WAREHOUSE_NAME,\n");
		sql.append("       t.DELIVERY_DATE, --发车日期\n");
		sql.append("       t.STORAGE_DATE, --验收日期\n");
//		sql.append("       TO_CHAR(TTTSI.CARRIVEDATE, 'yyyy-MM-dd') TARRIVE_DATE, --物流到店日期\n");
		sql.append("       t.INSPECTION_PERSON, --验收人\n");
		sql.append("       t.VEHICLE_AREA,\n");
		sql.append("       t.inspection_id\n");
		sql.append("  from (SELECT TMV.VEHICLE_ID,\n");
		sql.append("               tti.inspection_id,\n");
		sql.append("               TMD.DEALER_SHORTNAME, --经销商名称\n");
		sql.append("               G.GROUP_NAME, --车型名称\n");
		sql.append("               TMVM.MATERIAL_CODE, --物料代码\n");
		sql.append("               TMVM.MATERIAL_NAME, --物料名称\n");
		sql.append("               TMV.VIN, --VIN\n");
		sql.append("               TMW.WAREHOUSE_NAME,\n");
		sql.append("               ttd.erp_order,\n");
		sql.append("               TO_CHAR(TTD.DELIVERY_DATE, 'yyyy-MM-dd') DELIVERY_DATE, --发车日期\n");
		sql.append("               TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') STORAGE_DATE, --验收日期\n");
//		sql.append("               --TO_CHAR(TTTSI.CARRIVEDATE, 'yyyy-MM-dd') TARRIVE_DATE, --物流到店日期\n");
		sql.append("               TTI.INSPECTION_PERSON, --验收人\n");
		sql.append("               TTI.VEHICLE_AREA\n");
		sql.append("          FROM TT_VS_INSPECTION       TTI,\n");
		sql.append("               TM_VEHICLE             TMV,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("               TM_VHCL_MATERIAL       TMVM,\n");
//		sql.append("               TT_VS_DLVRY_MCH        TTDM,\n");
		sql.append("               TT_VS_DLVRY_DTL        TTDD,\n");
		sql.append("               TT_VS_DLVRY            TTD,\n");
		sql.append("               TM_DEALER              TMD,\n");
		sql.append("               TM_WAREHOUSE           TMW\n");
		sql.append("         WHERE TTI.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("           AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("           AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
//		sql.append("           AND tti.match_id = TTDM.Match_Id\n");
//		sql.append("           AND TTDM.DELIVERY_DETAIL_ID = TTDD.DETAIL_ID\n");
		sql.append("           AND TTDD.DELIVERY_ID = TTD.DELIVERY_ID\n");
		sql.append("           AND TMV.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("           AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID\n");

		// 发车日期
		if (null != deliverystartDate && !"".equals(deliverystartDate)) {
			sql.append("   AND TTD.DELIVERY_DATE >= TO_DATE('" + deliverystartDate + "','yyyy-MM-dd')\n");
		}
		if (null != deliveryendDate && !"".equals(deliveryendDate)) {
			sql.append("   AND TTD.DELIVERY_DATE <= (TO_DATE('" + deliveryendDate + "','yyyy-MM-dd')+1)\n");
		}
		// 验收日期
		if (null != checkstartDate && !"".equals(checkstartDate)) {
			sql.append("   AND TMV.STORAGE_DATE >= TO_DATE('" + checkstartDate + "','yyyy-MM-dd')\n");
		}
		if (null != checkendDate && !"".equals(checkendDate)) {
			sql.append("   AND TMV.STORAGE_DATE <= (TO_DATE('" + checkendDate + "','yyyy-MM-dd')+1)\n");
		}

		if (null != areaIds && !"".equals(areaIds)) {
			sql.append("           and exists\n");
			sql.append("         (select 1\n");
			sql.append("                  from tm_dealer_business_area tdba\n");
			sql.append("                 where 1 = 1\n");
			sql.append("                   and tdba.dealer_id = TMD.dealer_id\n");
			sql.append("                   and tdba.area_id IN (").append(areaIds).append("))\n");
		}

		// 经销商
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}

		// VIN
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		sql.append("         order by tti.inspection_id desc) t\n");
//		sql.append("         order by tti.inspection_id desc) t,\n");
//		sql.append("       CA_VDR_ARRIVE TTTSI\n");
//		sql.append(" where TTTSI.VIN(+) = t.VIN\n");
//		sql.append("   and tttsi.salesno(+) = t.erp_order\n");

		return dao.pageQuery(sql.toString(), params, dao.getFunName(), pageSize, curPage);
	}
	
	public static PageResult<Map<String, Object>> getCheckQueryOEM_CSC(String orgId, String dealerCode, String deliverystartDate, String deliveryendDate, String checkstartDate, String checkendDate, String vin, String areaIds,String dlvNo, int pageSize, int curPage) {

		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("SELECT A.DEALER_ID\n" );
		sqlStr.append("  FROM TM_DEALER A, TM_DEALER_BUSINESS_AREA TDBA\n" );
		sqlStr.append(" WHERE A.STATUS = 10011001\n" );
		sqlStr.append("   AND A.OEM_COMPANY_ID = 2010010100070674\n" );
		sqlStr.append("   AND TDBA.DEALER_ID = A.DEALER_ID\n" );
		sqlStr.append("   AND A.DEALER_LEVEL = 10851001\n" );
		sqlStr.append("   AND A.DEALER_CLASS <> 11451013\n" );
		sqlStr.append("   AND A.DEALER_TYPE IN (10771001, 10771004, 10771003)\n" );
		sqlStr.append("   AND A.DEALER_ID IN\n" );
		sqlStr.append("       (SELECT TD.DEALER_ID\n" );
		sqlStr.append("          FROM TM_DEALER TD\n" );
		sqlStr.append("         START WITH TD.DEALER_ID IN\n" );
		sqlStr.append("                    (SELECT TDOR.DEALER_ID\n" );
		sqlStr.append("                       FROM TM_DEALER_ORG_RELATION TDOR\n" );
		sqlStr.append("                      WHERE TDOR.ORG_ID IN\n" );
		sqlStr.append("                            (SELECT ORG_ID\n" );
		sqlStr.append("                               FROM TM_ORG ORG\n" );
		sqlStr.append("                              WHERE ORG.STATUS = 10011001\n" );
		sqlStr.append("                              START WITH ORG.ORG_ID = "+orgId+"\n" );
		sqlStr.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n" );
		sqlStr.append("        CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D)\n" );
		sqlStr.append("   AND EXISTS (SELECT TDBA.DEALER_ID\n" );
		sqlStr.append("          FROM TM_DEALER_BUSINESS_AREA TDBA\n" );
		sqlStr.append("         WHERE A.DEALER_ID = TDBA.DEALER_ID\n" );
		sqlStr.append("           AND TDBA.AREA_ID IN ("+areaIds+"))");

		
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("       TD.DEALER_SHORTNAME,\n");
		sql.append("       TV.VIN,\n" );
		sql.append("       TV.VEHICLE_ID,\n" );
//		sql.append("       TDW.WAREHOUSE_NAME,\n" );
		sql.append("       TW.WAREHOUSE_NAME,\n" );
		sql.append("       TVI.ARRIVE_DATE,\n" );
		sql.append("       TVI.INSPECTION_PERSON,\n" );
		sql.append("       TVDE.FLATCAR_ASSIGN_DATE,\n" );
		sql.append("       TV.STORAGE_DATE,\n" );
		sql.append("       Tvde.sendcar_order_number,\n" );
		sql.append("       G.GROUP_NAME\n" );
		sql.append("  FROM TT_VS_INSPECTION       TVI,\n" );
		sql.append("       TT_VS_DLVRY_ERP        TVDE,\n" );
		sql.append("       TT_VS_DLVRY_ERP_DTL    TVDED,\n" );
		sql.append("       TM_VEHICLE             TV,\n" );
		sql.append("       TM_VHCL_MATERIAL       TVM,\n" );
		sql.append("       TM_DEALER_WAREHOUSE    TDW,\n" );
		sql.append("       TM_DEALER              TD,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n" );
		sql.append("       TM_WAREHOUSE           TW\n" );
		sql.append(" WHERE TVI.DLVRY_DTL_ID = TVDED.DETAIL_ID\n" );
		sql.append("   AND TVDE.SENDCAR_HEADER_ID = TVDED.SENDCAR_HEADER_ID\n" );
		sql.append("   AND TV.VEHICLE_ID = TVI.VEHICLE_ID\n" );
		sql.append("   AND TV.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TV.VEHICLE_AREA = TDW.WAREHOUSE_ID\n" );
		sql.append("   AND TV.DEALER_ID = TD.DEALER_ID(+)\n" );
		sql.append("   AND TV.MODEL_ID = G.GROUP_ID\n" );
		sql.append("   AND TV.WAREHOUSE_ID = TW.WAREHOUSE_ID\n" );
//		sql.append("   AND TVI.OPERATE_DEALER IN (2013041019442376)\n");

		sql.append("   AND TVI.OPERATE_DEALER IN(" + sqlStr.toString() + ")\n");

		// 发车日期
		if (null != deliverystartDate && !"".equals(deliverystartDate)) {
			sql.append("   AND TVDE.FLATCAR_ASSIGN_DATE >= TO_DATE('" + deliverystartDate + "','yyyy-MM-dd')\n");
		}
		if (null != deliveryendDate && !"".equals(deliveryendDate)) {
			sql.append("   AND TVDE.FLATCAR_ASSIGN_DATE <= (TO_DATE('" + deliveryendDate + "','yyyy-MM-dd')+1)\n");
		}
		// 验收日期
		if (null != checkstartDate && !"".equals(checkstartDate)) {
			sql.append("   AND TV.STORAGE_DATE >= TO_DATE('" + checkstartDate + "','yyyy-MM-dd')\n");
		}
		if (null != checkendDate && !"".equals(checkendDate)) {
			sql.append("   AND TV.STORAGE_DATE <= (TO_DATE('" + checkendDate + "','yyyy-MM-dd')+1)\n");
		}

//		if (null != areaIds && !"".equals(areaIds)) {
//			sql.append("           and exists\n");
//			sql.append("         (select 1\n");
//			sql.append("                  from tm_dealer_business_area tdba\n");
//			sql.append("                 where 1 = 1\n");
//			sql.append("                   and tdba.dealer_id = TD.dealer_id\n");
//			sql.append("                   and tdba.area_id IN (").append(areaIds).append("))\n");
//		}

		// 经销商
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TD", "DEALER_CODE"));
		}

		// VIN
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TV"));
		}
		// VIN
		if (null != dlvNo && !"".equals(dlvNo)) {
			sql.append(" and tvde.sendcar_order_number like '%"+dlvNo+"%'");
		}

		return dao.pageQuery(sql.toString(), params, "com.infodms.dms.dao.sales.storageManage.CheckVehicleQueryDAO.getCheckQueryOEM_CSC", pageSize, curPage);
	}
	public static PageResult<Map<String, Object>> getCheckQueryOEM_CSCTOTAL(String orgId, String dealerCode, String deliverystartDate, String deliveryendDate, String checkstartDate, String checkendDate, String vin, String areaIds, int pageSize, int curPage) {

		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("SELECT A.DEALER_ID\n" );
		sqlStr.append("  FROM TM_DEALER A, TM_DEALER_BUSINESS_AREA TDBA\n" );
		sqlStr.append(" WHERE A.STATUS = 10011001\n" );
		sqlStr.append("   AND A.OEM_COMPANY_ID = 2010010100070674\n" );
		sqlStr.append("   AND TDBA.DEALER_ID = A.DEALER_ID\n" );
		sqlStr.append("   AND A.DEALER_LEVEL = 10851001\n" );
		sqlStr.append("   AND A.DEALER_CLASS <> 11451013\n" );
		sqlStr.append("   AND A.DEALER_TYPE IN (10771001, 10771004, 10771003)\n" );
		sqlStr.append("   AND A.DEALER_ID IN\n" );
		sqlStr.append("       (SELECT TD.DEALER_ID\n" );
		sqlStr.append("          FROM TM_DEALER TD\n" );
		sqlStr.append("         START WITH TD.DEALER_ID IN\n" );
		sqlStr.append("                    (SELECT TDOR.DEALER_ID\n" );
		sqlStr.append("                       FROM TM_DEALER_ORG_RELATION TDOR\n" );
		sqlStr.append("                      WHERE TDOR.ORG_ID IN\n" );
		sqlStr.append("                            (SELECT ORG_ID\n" );
		sqlStr.append("                               FROM TM_ORG ORG\n" );
		sqlStr.append("                              WHERE ORG.STATUS = 10011001\n" );
		sqlStr.append("                              START WITH ORG.ORG_ID = "+orgId+"\n" );
		sqlStr.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n" );
		sqlStr.append("        CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D)\n" );
		sqlStr.append("   AND EXISTS (SELECT TDBA.DEALER_ID\n" );
		sqlStr.append("          FROM TM_DEALER_BUSINESS_AREA TDBA\n" );
		sqlStr.append("         WHERE A.DEALER_ID = TDBA.DEALER_ID\n" );
		sqlStr.append("           AND TDBA.AREA_ID IN ("+areaIds+"))");

		
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("COUNT(1) COUNT \n");

//		sql.append("       TD.DEALER_SHORTNAME,\n");
//		sql.append("       TV.VIN,\n" );
//		sql.append("       TV.VEHICLE_ID,\n" );
//		sql.append("       TDW.WAREHOUSE_NAME,\n" );
//		sql.append("       TW.WAREHOUSE_NAME,\n" );
//		sql.append("       TVI.ARRIVE_DATE,\n" );
//		sql.append("       TVI.INSPECTION_PERSON,\n" );
//		sql.append("       TVDE.FLATCAR_ASSIGN_DATE,\n" );
//		sql.append("       TV.STORAGE_DATE,\n" );
//		sql.append("       G.GROUP_NAME\n" );
		sql.append("  FROM TT_VS_INSPECTION       TVI,\n" );
		sql.append("       TT_VS_DLVRY_ERP        TVDE,\n" );
		sql.append("       TT_VS_DLVRY_ERP_DTL    TVDED,\n" );
		sql.append("       TM_VEHICLE             TV,\n" );
		sql.append("       TM_VHCL_MATERIAL       TVM,\n" );
		sql.append("       TM_DEALER_WAREHOUSE    TDW,\n" );
		sql.append("       TM_DEALER              TD,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n" );
		sql.append("       TM_WAREHOUSE           TW\n" );
		sql.append(" WHERE TVI.DLVRY_DTL_ID = TVDED.DETAIL_ID\n" );
		sql.append("   AND TVDE.SENDCAR_HEADER_ID = TVDED.SENDCAR_HEADER_ID\n" );
		sql.append("   AND TV.VEHICLE_ID = TVI.VEHICLE_ID\n" );
		sql.append("   AND TV.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TV.VEHICLE_AREA = TDW.WAREHOUSE_ID\n" );
		sql.append("   AND TV.DEALER_ID = TD.DEALER_ID(+)\n" );
		sql.append("   AND TV.MODEL_ID = G.GROUP_ID\n" );
		sql.append("   AND TV.WAREHOUSE_ID = TW.WAREHOUSE_ID\n" );
//		sql.append("   AND TVI.OPERATE_DEALER IN (2013041019442376)\n");

		sql.append("   AND TVI.OPERATE_DEALER IN(" + sqlStr.toString() + ")\n");

		// 发车日期
		if (null != deliverystartDate && !"".equals(deliverystartDate)) {
			sql.append("   AND TVDE.FLATCAR_ASSIGN_DATE >= TO_DATE('" + deliverystartDate + "','yyyy-MM-dd')\n");
		}
		if (null != deliveryendDate && !"".equals(deliveryendDate)) {
			sql.append("   AND TVDE.FLATCAR_ASSIGN_DATE <= (TO_DATE('" + deliveryendDate + "','yyyy-MM-dd')+1)\n");
		}
		// 验收日期
		if (null != checkstartDate && !"".equals(checkstartDate)) {
			sql.append("   AND TV.STORAGE_DATE >= TO_DATE('" + checkstartDate + "','yyyy-MM-dd')\n");
		}
		if (null != checkendDate && !"".equals(checkendDate)) {
			sql.append("   AND TV.STORAGE_DATE <= (TO_DATE('" + checkendDate + "','yyyy-MM-dd')+1)\n");
		}

//		if (null != areaIds && !"".equals(areaIds)) {
//			sql.append("           and exists\n");
//			sql.append("         (select 1\n");
//			sql.append("                  from tm_dealer_business_area tdba\n");
//			sql.append("                 where 1 = 1\n");
//			sql.append("                   and tdba.dealer_id = TD.dealer_id\n");
//			sql.append("                   and tdba.area_id IN (").append(areaIds).append("))\n");
//		}

		// 经销商
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TD", "DEALER_CODE"));
		}

		// VIN
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TV"));
		}
		 sql.append(" GROUP  BY TVM.MATERIAL_CODE, TVM.MATERIAL_NAME\n");
		return dao.pageQuery(sql.toString(), params, "com.infodms.dms.dao.sales.storageManage.CheckVehicleQueryDAO.getCheckQueryOEM_CSC", pageSize, curPage);
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
