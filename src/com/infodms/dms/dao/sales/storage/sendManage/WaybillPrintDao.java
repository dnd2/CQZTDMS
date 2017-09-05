package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesBoardPO;
import com.infodms.dms.util.XHBUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName : WaybillPrintDao
 * @Description : 运单打印DAO
 * @author : wenyd CreateDate : 2013-5-20
 */
public class WaybillPrintDao extends BaseDao<PO> {

	private static final WaybillPrintDao dao = new WaybillPrintDao();

	public static final WaybillPrintDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TtSalesBoardPO> select(TtSalesBoardPO t) {
		return factory.select(t);
	}

	/**
	 * 运单生成管理查询
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getWaybillManageQuery(
			Map<String, Object> map, int curPage, int pageSize)
			throws Exception {
		Object[] obj = getSQL(map);
		PageResult<Map<String, Object>> ps = dao.pageQuery(obj[0].toString(),
				(List<Object>) obj[1], getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 运单生成管理查询数量和 param map 查询参数
	 * 
	 * @return 运单数量
	 */
	public Map<String, Object> tgSumPrint(Map<String, Object> map) {
		Object[] obj = getSQL(map);
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT SUM(NVL(VEH_NUM,0)) VEH_NUM FROM (\n");
		sbSql.append(obj[0].toString());
		sbSql.append(")\n");
		Map<String, Object> mapR = dao.pageQueryMap(sbSql.toString(),
				(List<Object>) obj[1], getFunName());
		return mapR;
	}

	// get sql
	public Object[] getSQL(Map<String, Object> map) {
		/****************************** 页面查询字段start **************************/
		String dealerCode = (String) map.get("dealerCode"); // 经销商CODE
		String orderNo = (String) map.get("orderNo"); // 运单号
		String poseId = (String) map.get("poseId");
		String sendStartDate = (String) map.get("sendStartDate"); // 发运时间
		String sendEndDate = (String) map.get("sendEndDate");
		String address = (String) map.get("address"); // 地点
		String countyId = (String) map.get("countyId");// 区县
		String cityId = (String) map.get("CITY_ID");// 地市
		String provinceId = (String) map.get("PROVINCE_ID");// 省份
		String isPrint = (String) map.get("isPrint");// 是否打印
		String yieldly = (String) map.get("yieldly"); //
		String logiName = (String) map.get("logiName");
		String isConfirm = (String)map.get("isConfirm");
		/****************************** 页面查询字段end ***************************/
		List<Object> params = new LinkedList<Object>();
		params.add(Constant.IF_TYPE_YES);
		params.add(Constant.IF_TYPE_YES);
		params.add(Constant.IF_TYPE_YES);
		params.add(Constant.SEND_STATUS_01);
		params.add(Constant.STATUS_ENABLE);
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("WITH T1_WITH AS\n");
		sbSql.append(" (SELECT BILL_ID, COUNT(1) CCOUNT\n");
		sbSql.append("    FROM TT_SALES_BOARD     TSB,\n");
		sbSql.append("         TT_SALES_BO_DETAIL TSBD,\n");
		sbSql.append("         TT_SALES_ALLOCA_DE TSAD\n");
		sbSql.append("   WHERE TSB.BO_ID = TSBD.BO_ID\n");
		sbSql.append("     AND TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
		sbSql.append("     AND TSB.IS_ENABLE = ?\n");
		sbSql.append("     AND TSBD.IS_ENABLE = ?\n");
		sbSql.append("     AND TSAD.IS_ACC = ?\n");
		sbSql.append("   GROUP BY TSBD. BILL_ID)\n");
		sbSql.append("SELECT TSW.BILL_ID,\n");
		sbSql.append("       TSW.BILL_NO,\n");
		sbSql.append("       TD.DEALER_CODE,\n");
		sbSql.append("       TD.DEALER_NAME,\n");
		sbSql.append("       TSL.LOGI_NAME,\n");
		sbSql.append("       NVL(TSW.VEH_NUM, 0) VEH_NUM,\n");
		sbSql.append("       NVL(T1.CCOUNT, 0) C_COUNT,\n");
		sbSql.append("       TSW.IS_CONFIRM,\n");
		sbSql.append("       A.REGION_NAME || '-' || B.REGION_NAME || '-' || C.REGION_NAME SEND_CITY\n");
		sbSql.append("  FROM TT_SALES_WAYBILL TSW,\n");
		sbSql.append("       TM_DEALER        TD,\n");
		sbSql.append("       TT_SALES_LOGI    TSL,\n");
		sbSql.append("       T1_WITH          T1,\n");
		sbSql.append("       TM_VS_ADDRESS    X,\n");
		sbSql.append("       TM_REGION        A,\n");
		sbSql.append("       TM_REGION        B,\n");
		sbSql.append("       TM_REGION        C\n");
		sbSql.append(" WHERE TSW.SEND_DEALER_ID = TD.DEALER_ID\n");
		sbSql.append("   AND TSW.LOGI_ID = TSL.LOGI_ID\n");
		sbSql.append("   AND TSW.BILL_ID = T1.BILL_ID(+)\n");
		sbSql.append("   AND TSW.ADDRESS_ID = X.ID(+)\n");
		sbSql.append("   AND X.PROVINCE_ID = A.REGION_CODE(+)\n");
		sbSql.append("   AND X.CITY_ID = B.REGION_CODE(+)\n");
		sbSql.append("   AND X.AREA_ID = C.REGION_CODE(+)\n");
		sbSql.append("   AND TSW.SEND_STATUS = ?\n");
		sbSql.append("   AND TSW.STATUS = ?\n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSqlByPar(
				"TSW.AREA_ID", poseId, params));// 车厂端查询列表产地数据权限
		sbSql.append(MaterialGroupManagerDao.getPoseIdLogiSqlByPar(
				"TSW.LOGI_ID", poseId, params));// 物流判断（是否是物流公司）

		if (null != dealerCode && !"".equals(dealerCode)) {
			sbSql.append(Utility.getConSqlByParamForEqual(dealerCode, params,
					"TD", "DEALER_CODE"));
		}
		if (orderNo != null && !"".equals(orderNo)) {
			sbSql.append("   AND TSW.BILL_NO  LIKE ?\n");// 运单号
			params.add("%" + orderNo + "%");
		}
		if (sendStartDate != null && !"".equals(sendStartDate)) {
			sbSql.append("   AND TSW.BILL_CRT_DATE >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(sendStartDate + " 00:00:00");
		}
		if (sendEndDate != null && !"".equals(sendEndDate)) {
			sbSql.append("   AND TSW.BILL_CRT_DATE <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(sendEndDate + " 23:59:59");
		}
		if (address != null && !"".equals(address)) {
			sbSql.append("   AND EXISTS (SELECT 1 FROM TM_VS_ADDRESS WHERE ID = TSW.ADDRESS_ID AND ADDRESS LIKE ? )\n");
			params.add("%" + address + "%");
		}
		if (provinceId != null && !"".equals(provinceId)) {
			sbSql.append(" AND A.REGION_CODE =?\n");
			params.add(provinceId);
		}
		if (cityId != null && !"".equals(cityId)) {
			sbSql.append(" AND B.REGION_CODE=?\n");
			params.add(cityId);
		}
		if (countyId != null && !"".equals(countyId)) {
			sbSql.append(" AND C.REGION_NAME LIKE ?\n");
			params.add("%" + countyId + "%");
		}
		if (yieldly != null && !"".equals(yieldly)) {//
			sbSql.append("  AND TSW.AREA_ID=?\n");
			params.add(yieldly);
		}
		if (logiName != null && !"".equals(logiName)) {
			sbSql.append(" AND TSW.LOGI_ID = ?\n");
			params.add(logiName);
		}
		if (isPrint != null && !"".equals(isPrint)) {
			if ("YDY".equals(isPrint)) {
				sbSql.append(" AND TSW.BILL_PRINT_DATE IS NOT NULL \n");
			} else {
				sbSql.append(" AND TSW.BILL_PRINT_DATE IS  NULL \n");
			}

		}
		if (isConfirm != null && !"".equals(isConfirm)) {
			sbSql.append(" AND TSW.IS_CONFIRM = ?");
			params.add(isConfirm);
		}
		sbSql.append(" ORDER BY  TSW.BILL_ID ASC\n");
		Object[] arr = new Object[2];
		arr[0] = sbSql;
		arr[1] = params;
		return arr;
	}

	/**
	 * 运单取消回送配车明细表值
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public void updateSendNum(Long billId, Long userId) {//
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE TT_SALES_ALLOCA_DE TSAD\n");
		sql.append("   SET TSAD.IS_SEND = ?,\n");
		sql.append("   TSAD.UPDATE_DATE = SYSDATE,\n");
		sql.append("   TSAD.UPDATE_BY = ?\n");
		sql.append(" WHERE TSAD.BO_DE_ID IN\n");
		sql.append("       (SELECT TSBD.BO_DE_ID\n");
		sql.append("          FROM TT_SALES_BO_DETAIL TSBD WHERE TSBD.BILL_ID = ?)\n");
		// sql.append("         WHERE TSBD.BO_ID IN (select TSB.BO_ID\n" );
		// sql.append("                                from TT_SALES_BOARD TSB\n"
		// );
		// sql.append("                               where TSb.BILL_ID = ?))\n"
		// );
		sql.append("");
		List<Object> list = new ArrayList<Object>();
		list.add(Constant.IF_TYPE_NO);
		list.add(userId);
		list.add(billId);
		factory.update(sql.toString(), list);// 更新组板明细配车发运数量

	}

	/**
	 * 修改车辆表生命周期和锁定状态
	 * 
	 * @param billID
	 */
	public void updateVehicleStatus(Long billId, Long userId) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE TM_VEHICLE TV\n");
		sbSql.append("   SET TV.LIFE_CYCLE  = ?,\n");
		sbSql.append("       TV.LOCK_STATUS = ?,\n");
		sbSql.append("       TV.UPDATE_BY   = ?,\n");
		sbSql.append("       TV.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE EXISTS (SELECT TSAD.VEHICLE_ID\n");
		sbSql.append("          FROM TT_SALES_WAYBILL   TSW,\n");
		sbSql.append("               TT_SALES_BOARD     TSB,\n");
		sbSql.append("               TT_SALES_BO_DETAIL TSBD,\n");
		sbSql.append("               TT_SALES_ALLOCA_DE TSAD\n");
		sbSql.append("         WHERE TSW.BILL_ID = TSBD.BILL_ID\n");
		sbSql.append("           AND TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
		sbSql.append("           AND TV.VEHICLE_ID = TSAD.VEHICLE_ID\n");
		sbSql.append("           AND TSAD.STATUS = ?\n");
		sbSql.append("           AND TSW.BILL_ID = ?)");
		List<Object> list6 = new ArrayList<Object>();
		list6.add(Constant.VEHICLE_LIFE_08);
		list6.add(Constant.LOCK_STATUS_08);
		list6.add(userId);
		list6.add(Constant.STATUS_ENABLE);
		list6.add(billId);
		factory.update(sbSql.toString(), list6);// 修改车辆表生命周期和锁定状态
	}

	/**
	 * 修改车辆表生命周期(已确认环节)
	 * 
	 * @param billId
	 * @param userId
	 */
	public void updateVStatusConf(Long billId, Long userId) {//
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE TM_VEHICLE TV\n");
		sbSql.append("   SET TV.LIFE_CYCLE  = ?,\n");
		// sbSql.append("       TV.LOCK_STATUS = ?,\n");
		sbSql.append("       TV.UPDATE_BY   = ?,\n");
		sbSql.append("       TV.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE TV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_09
				+ " AND EXISTS (SELECT TSAD.VEHICLE_ID\n");
		sbSql.append("          FROM TT_SALES_BOARD     TSS,\n");
		sbSql.append("               TT_SALES_BO_DETAIL TSBD,\n");
		sbSql.append("               TT_SALES_ALLOCA_DE TSAD\n");
		sbSql.append("         WHERE TSS.BO_ID = TSBD.BO_ID\n");
		sbSql.append("           AND TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
		sbSql.append("           AND TV.VEHICLE_ID = TSAD.VEHICLE_ID\n");
		sbSql.append("           AND TSBD.BILL_ID = ?)");
		List<Object> list6 = new ArrayList<Object>();
		list6.add(Constant.VEHICLE_LIFE_05);
		// list6.add(Constant.LOCK_STATUS_01);
		list6.add(userId);
		list6.add(billId);
		factory.update(sbSql.toString(), list6);// 修改车辆表生命周期和锁定状态
	}

	/**
	 * 修改车辆表生命周期(确认后的环节)
	 * 
	 * @param boId
	 * @param userId
	 */
	public void updateVStatusConfConfim(Long boId, Long userId) {//
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE TM_VEHICLE TV\n");
		sbSql.append("   SET TV.LIFE_CYCLE  = ?,\n");
		// sbSql.append("       TV.LOCK_STATUS = ?,\n");
		sbSql.append("       TV.UPDATE_BY   = ?,\n");
		sbSql.append("       TV.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE EXISTS (SELECT TSAD.VEHICLE_ID\n");
		sbSql.append("          FROM TT_SALES_BOARD     TSS,\n");
		sbSql.append("               TT_SALES_BO_DETAIL TSBD,\n");
		sbSql.append("               TT_SALES_ALLOCA_DE TSAD,\n");
		sbSql.append("               TT_SALES_WAYBILL   TSW\n");
		sbSql.append("         WHERE TSS.BO_ID = TSBD.BO_ID\n");
		sbSql.append("           AND TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
		sbSql.append("           AND TV.VEHICLE_ID = TSAD.VEHICLE_ID\n");
		sbSql.append("           AND TSBD.BILL_ID = TSW.BILL_ID\n");
		sbSql.append("           AND TSW.BILL_ID = ?)");
		List<Object> list6 = new ArrayList<Object>();
		list6.add(Constant.VEHICLE_LIFE_05);
		// list6.add(Constant.LOCK_STATUS_01);
		list6.add(userId);
		list6.add(boId);
		factory.update(sbSql.toString(), list6);// 修改车辆表生命周期和锁定状态
	}

	public List<Map<String, Object>> getBoIds(String billId) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT  TSD.BO_NO,TSD.BO_ID\n");
		sbSql.append("             FROM  TT_SALES_BOARD TSD,\n");
		sbSql.append("                   TT_SALES_BO_DETAIL TSBD\n");
		sbSql.append("             WHERE TSD.BO_ID = TSBD.BO_ID\n");
		sbSql.append("             AND   TSBD.BILL_ID = " + billId + "\n");
		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}

	public List<Map<String, Object>> getBillIds(String billId) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TSBD.BILL_ID\n");
		sbSql.append("  FROM TT_SALES_BOARD TSD, TT_SALES_BO_DETAIL TSBD\n");
		sbSql.append(" WHERE TSD.BO_ID = TSBD.BO_ID\n");
		sbSql.append("   AND TSd.Bo_Id = (SELECT TSD.BO_ID\n");
		sbSql.append("                      FROM TT_SALES_BOARD TSD, TT_SALES_BO_DETAIL TSBD\n");
		sbSql.append("                     WHERE TSD.BO_ID = TSBD.BO_ID\n");
		sbSql.append("                       and tsbd.bill_id = ?\n");
		sbSql.append("                       and rownum = 1)\n");
		sbSql.append(" group by tsbd.bill_id");
		List<Object> params = new ArrayList<Object>();
		params.add(billId);
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}

	public List<Map<String, Object>> getConfirmBillIds(String billId) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT A.BILL_NO\n");
		sbSql.append("  FROM TT_SALES_WAYBILL A\n");
		sbSql.append(" WHERE A.BILL_ID IN (SELECT TSBD.BILL_ID\n");
		sbSql.append("                       FROM TT_SALES_BOARD TSD, TT_SALES_BO_DETAIL TSBD\n");
		sbSql.append("                      WHERE TSD.BO_ID = TSBD.BO_ID\n");
		sbSql.append("                        AND TSD.BO_ID = (SELECT TSD.BO_ID\n");
		sbSql.append("                                           FROM TT_SALES_BOARD     TSD,\n");
		sbSql.append("                                                TT_SALES_BO_DETAIL TSBD\n");
		sbSql.append("                                          WHERE TSD.BO_ID = TSBD.BO_ID\n");
		sbSql.append("                                            AND TSBD.BILL_ID = ?\n");
		sbSql.append("                                            AND ROWNUM = 1)\n");
		sbSql.append("                      GROUP BY TSBD.BILL_ID)\n");
		sbSql.append("   AND A.IS_CONFIRM = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(billId);
		params.add(Constant.IF_TYPE_YES);// 已确认的
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}

	/**
	 * 运单打印基本信息
	 * 
	 * @param billId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> billPrintMsg(Long billId, String ser_id) {//
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TSW.BILL_ID,TO_CHAR(TSW.BILL_CRT_DATE,'yyyy-MM-dd') BILL_CRT_DATE, TSW.BILL_CRT_DATE CREATE_DATE,--发运日期\n");
		sbSql.append("       TSW.BILL_NO, --运单号\n");
		sbSql.append("       TD.DEALER_NAME, --收车单位\n");// 取的是订货经销商地址
		sbSql.append("       TVA.LINK_MAN, --  联系人\n");
		sbSql.append("       TD.PHONE, --联系电话\n");
		sbSql.append("       TVA.ADDRESS, --交车地址\n");
		sbSql.append("       TR.REGION_NAME, --地点\n");
		sbSql.append("       TVA.TEL, --手机\n");
		/** add by wangsw 添加订单类型 */
		sbSql.append("		 TSBD.ORDER_TYPE,TSBD.ORDER_NO,(SELECT TC.CODE_DESC  FROM TC_CODE TC WHERE TC.CODE_ID= TSBD.SEND_TYPE ) AS SENDTYPE , \n");
		sbSql.append("       BOTABLE.BONO, --组板号\n");
		sbSql.append("       TSW.VEH_NUM, --车数（总数）\n");
		// sbSql.append("       TO_CHAR((TSW.BILL_CRT_DATE + NVL(TSCD.ARRIVE_DAYS, 0)),\n");
		// sbSql.append("               'YYYY-MM-DD HH24:MI:SS') ARRIVE_DATE,\n");
		sbSql.append("       TSB.CAR_TEAM,TSB.CAR_NO,TSB.DRIVER_NAME,TSB.DRIVER_TEL,TSB.POLICY_NO,TC.CODE_DESC,");
		sbSql.append("       TU.NAME, --车队经办人（取得运生成人）\n");
		sbSql.append("       TSW.REMARK,TSW.AREA_ID,TR.REGION_ID CITY_ID ,--说明备注\n");
		sbSql.append("		 TO_CHAR(TV.STORAGE_DATE,'yyyy-MM-dd') STORAGE_DATE,AC.INSPECTION_PERSON\n"); 
		sbSql.append("  FROM TT_SALES_WAYBILL TSW,\n");
		sbSql.append("       TM_DEALER TD,\n");
		sbSql.append("		 TM_VEHICLE TV,\n");
		sbSql.append("       TT_VS_INSPECTION AC,"); 
		sbSql.append("       TM_VS_ADDRESS TVA,TT_SALES_BOARD TSB,TT_SALES_BO_DETAIL TSBD,VW_MATERIAL_GROUP_MAT B,\n");
		sbSql.append("       TM_REGION TR,\n");
		sbSql.append("       (SELECT T1.BILL_ID, ZA_CONCAT(DISTINCT T.BO_NO) BONO\n");
		sbSql.append("          FROM TT_SALES_BOARD T ,TT_SALES_BO_DETAIL T1 \n");
		sbSql.append("          WHERE T.BO_ID = T1.BO_ID");
		sbSql.append("         GROUP BY T1.BILL_ID) BOTABLE,\n");
		// sbSql.append("       TT_SALES_LOGI TSL,\n");
		// sbSql.append("       TT_SALES_LOGI_AREA TSLA,\n");
		// sbSql.append("       TT_SALES_CITY_DIS TSCD,\n");
		sbSql.append("       TC_USER TU,TC_CODE TC\n");
		sbSql.append(" WHERE TSW.OR_DEALER_ID = TD.DEALER_ID\n");
		sbSql.append("   AND TSW.ADDRESS_ID = TVA.ID AND TSW.BILL_ID = TSBD.BILL_ID AND TSBD.BO_ID = TSB.BO_ID AND TSB.POLICY_TYPE = TC.CODE_ID(+) \n");
		sbSql.append("   AND TVA.AREA_ID = TR.REGION_CODE\n");
		sbSql.append("	 AND TV.Dealer_Id = TD.Dealer_Id AND TV.Vehicle_Id = AC.Vehicle_Id\n"); 
		sbSql.append("   AND TSW.BILL_ID = BOTABLE.BILL_ID\n");
		sbSql.append("   AND TSBD.MAT_ID = B.MATERIAL_ID\n");
		// sbSql.append("   AND TSW.LOGI_ID = TSL.LOGI_ID\n");
		// sbSql.append("   AND TSL.LOGI_ID = TSLA.LOGI_ID\n");
		// sbSql.append("   AND TSCD.DIS_ID = TSLA.DIS_ID\n");
		// sbSql.append("   AND TR.REGION_ID = TSCD.CITY_ID\n");
		sbSql.append("   AND TU.USER_ID = TSW.BILL_CRT_PER\n");
		sbSql.append("   AND TSW.BILL_ID = ?\n");
		List<Object> params = new ArrayList<Object>();
		params.add(billId);
		if (ser_id != null) {
			sbSql.append("   AND B.SERIES_ID = ?");
			params.add(ser_id);
		}

		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), params,
				getFunName());
		return map;

	}

	/**
	 * 运单详细信息列表
	 * 
	 * @param billId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> billPrintMainMsg(Long billId, String ser_id) {//
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT VMGM.MODEL_CODE,\n");
		sbSql.append("       VMGM.MODEL_NAME,\n");
		sbSql.append("       VMGM.PACKAGE_NAME,\n");
		sbSql.append("       TV.ENGINE_NO,\n");
		sbSql.append("       TV.PASS_NO,\n");
		sbSql.append("       VMGM.COLOR_NAME,\n");
		sbSql.append("       TV.VIN, TV.SD_NUMBER,TV.VEHICLE_ID,\n");
		sbSql.append("       TSB.CAR_TEAM,TSB.LOADS,TSW.BILL_NO,\n");
		sbSql.append("       TSB.CAR_NO,TD.DEALER_NAME,\n");
		sbSql.append("       TSBD.INVOICE_NO,TSBD.ORDER_NO,TSBD.ORDER_ID ,(SELECT TC.CODE_DESC  FROM TC_CODE TC WHERE TC.CODE_ID= TSBD.SEND_TYPE ) "
				+ "AS SENDTYPE,TSW.REMARK,(SELECT T.LOGI_FULL_NAME  FROM TT_SALES_LOGI T WHERE T.LOGI_ID = TSW.LOGI_ID) AS LOGINAME,"
				+ "TVA.LINK_MAN,TVA.ADDRESS,TVA.TEL,TVA.MOBILE_PHONE  \n");
		sbSql.append("  FROM TT_SALES_WAYBILL      TSW,\n");
		sbSql.append("       TT_SALES_BOARD        TSB,\n");
		// sbSql.append("       TT_SALES_ASSIGN       TSA,\n");
		sbSql.append("       TT_SALES_BO_DETAIL    TSBD,\n");
		sbSql.append("       TT_SALES_ALLOCA_DE    TSAD,\n");
		sbSql.append("       TM_VEHICLE            TV, TM_DEALER        TD, \n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT VMGM,TM_VS_ADDRESS TVA \n");
		sbSql.append(" WHERE TSW.BILL_ID = TSBD.BILL_ID AND TSW.SEND_DEALER_ID = TD.DEALER_ID \n");
		sbSql.append("   AND TSB.BO_ID = TSBD.BO_ID\n");
		sbSql.append("   AND TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
		sbSql.append("   AND TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append("   AND TV.MATERIAL_ID = VMGM.MATERIAL_ID\n");
		sbSql.append("   AND TVA.ID = TSW.ADDRESS_ID\n");
		// sbSql.append("   AND TSB.ASS_ID = TSA.ASS_ID\n");
		sbSql.append("   AND TSW.BILL_ID = ?");

		List<Object> params = new ArrayList<Object>();
		params.add(billId);
		if (ser_id != null) {
			sbSql.append("   AND VMGM.SERIES_ID = ?");
			params.add(ser_id);
		}
		
		sbSql.append(" order by TV.PASS_NO ASC,VMGM.MODEL_NAME,VMGM.PACKAGE_NAME,VMGM.COLOR_NAME");
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(),
				params, getFunName());
		return list;

	}

	/**
	 * 运单查询
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getWaybillList(
			Map<String, Object> map, int curPage, int pageSize)
			throws Exception {
		Object[] objArr = getSql(map);
		PageResult<Map<String, Object>> ps = dao.pageQuery(
				String.valueOf(objArr[0]), (List<Object>) objArr[1],
				getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 运单查询查询(导出)
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getWaybillListExport(
			Map<String, Object> map) throws Exception {
		Object[] objArr = getSql(map);
		List<Map<String, Object>> list = dao.pageQuery(
				String.valueOf(objArr[0]), (List<Object>) objArr[1],
				getFunName());
		return list;
	}

	/**
	 * 运单查询查询统计 param map 查询参数
	 * 
	 * @return
	 */
	public Map<String, Object> tgSum(Map<String, Object> map) {
		Object[] obj = getSql(map);
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT SUM(NVL(CG_AMOUNT,0)) CG_AMOUNT,SUM(NVL(DISTANCE,0)) DISTANCE,SUM(NVL(VEH_NUM,0)) VEH_NUM,round(SUM(NVL(DISTANCE,0))/SUM(NVL(VEH_NUM,0)),0) AVG_DISTANCE,round(SUM(NVL(CG_AMOUNT,0))/SUM(NVL(VEH_NUM,0)),2) AVG_AMOUNT FROM (\n");
		sbSql.append(obj[0].toString());
		sbSql.append(")\n");
		Map<String, Object> mapR = dao.pageQueryMap(sbSql.toString(),
				(List<Object>) obj[1], getFunName());
		return mapR;
	}

	/**
	 * 运单查询查询(SQL)
	 * 
	 * @param map
	 */
	public Object[] getSql(Map<String, Object> map) {
		/****************************** 页面查询字段start **************************/
		String dealerCode = (String) map.get("dealerCode"); // 经销商CODE
		String dealerCodeN = (String) map.get("dealerCodeN");//不包含的经销商CODE
		String orderNo = (String) map.get("orderNo"); // 运单号
		String poseId = (String) map.get("poseId");
		String sendStartDate = (String) map.get("sendStartDate"); // 发运时间
		String sendEndDate = (String) map.get("sendEndDate");
		String logiName = (String) map.get("logiName"); // 物流商
		String countyId = (String) map.get("countyId");// 区县
		String cityId = (String) map.get("CITY_ID");// 地市
		String provinceId = (String) map.get("PROVINCE_ID");// 省份
		String area_id = (String) map.get("AREA_ID");// 产地
		String VIN = (String) map.get("VIN");// VIN
		String isConfirm = (String)map.get("isConfirm");//是否确认
		String invoiceStatus = (String)map.get("invoiceStatus");//运单开票状态
		String invoiceStartDate = (String)map.get("invoiceStartDate");//开票开始时间
		String invoiceEndDate = (String)map.get("invoiceEndDate");//开票结束时间
		String sOrderNo = (String)map.get("sOrderNo");//销售清单号
		String confirmBeginDate = (String)map.get("confirmBeginDate");//运单确认时间B
		String confirmEndDate = (String)map.get("confirmEndDate");//运单确认时间E

		/****************************** 页面查询字段end ***************************/
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();

		/**
		 * 以下语句包括已结算的运单按照车辆配车明细表数据计算，防止修改基础数据后算出来运费与实际运费的差异 未结算的按照基础数据表单价进行计算
		 **/

		sbSql.append("WITH TAB_WITH AS\n");
		sbSql.append(" (SELECT A.BO_ID,\n");
		sbSql.append("         B.SERIES_ID,\n");
		sbSql.append("         B.SERIES_NAME,\n");
		sbSql.append("         A.BILL_ID,\n");
		sbSql.append("         COUNT(1) SEND_NUM,\n");
		sbSql.append("         NVL(MAX(C.FUEL_COEFFICIENT),0) FUEL_COEFFICIENT,\n");
		sbSql.append("         MAX(C.SEND_DISTANCE) SEND_DISTANCE,\r\n");
		sbSql.append("         MAX(C.SINGLE_PRICE) SINGLE_PRICE"); 
		sbSql.append("    FROM TT_SALES_BO_DETAIL A, VW_MATERIAL_GROUP_MAT B, TT_SALES_ALLOCA_DE C\n");
		sbSql.append("   WHERE A.MAT_ID = B.MATERIAL_ID\n");
		sbSql.append("     AND A.BO_DE_ID = C.BO_DE_ID\n");
		sbSql.append("     AND A.IS_ENABLE = ?\n");
		//sbSql.append("     AND C.STATUS = ?\n");
		params.add(Constant.IF_TYPE_YES);
		//params.add(Constant.STATUS_ENABLE);
		sbSql.append("   GROUP BY A.BO_ID, B.SERIES_ID, B.SERIES_NAME, A.BILL_ID),\n");
		sbSql.append("TAB_WITH_01 AS\n");
		sbSql.append(" (SELECT A.BO_ID,\n");
		sbSql.append("         B.SERIES_ID,\n");
		sbSql.append("         B.SERIES_NAME,\n");
		sbSql.append("         A.BILL_ID,\n");
		sbSql.append("         SUM(CASE WHEN C.VEHICLE_ID > 0 THEN 1 ELSE 0 END) SEND_NUM\n");
		sbSql.append("    FROM TT_SALES_BO_DETAIL A, VW_MATERIAL_GROUP_MAT B, TT_SALES_ALLOCA_DE C\n");
		sbSql.append("   WHERE A.MAT_ID = B.MATERIAL_ID\n");
		sbSql.append("     AND A.BO_DE_ID=C.BO_DE_ID\n");
		sbSql.append("     AND A.IS_ENABLE = ?\n");
		//sbSql.append("     AND C.STATUS = ?\n");
		params.add(Constant.IF_TYPE_YES);
		//params.add(Constant.STATUS_ENABLE);
		sbSql.append("   GROUP BY A.BO_ID, B.SERIES_ID, B.SERIES_NAME, A.BILL_ID),\n");
		sbSql.append(" T AS\n");
		sbSql.append("(SELECT TSW.AREA_ID,\n");
		sbSql.append("       TSW.BILL_ID,\n");
		sbSql.append("       TSW.BILL_NO,\n");
		sbSql.append("       TSW.LOGI_ID,\n");
		sbSql.append("       TSW.INVOICE_STATUS,\n");
		sbSql.append("       TSW.INVOICE_DATE,\n");
		sbSql.append("       TSW.IS_CONFIRM,\n");
		sbSql.append("       TSW.CONFIRM_DATE,\n");
		sbSql.append("       TD.DEALER_CODE,\n");
		sbSql.append("       TD.DEALER_NAME,\n");
		sbSql.append("       TSL.LOGI_NAME,\n");
		sbSql.append("       NVL(TSBD.SEND_NUM, 0) VEH_NUM,\n");
		sbSql.append("       TSB.DRIVER_NAME,\n");
		sbSql.append("       TSB.DRIVER_TEL,\n");
		sbSql.append("       A.REGION_ID,\n");
		sbSql.append("       A.REGION_CODE,\n");
		sbSql.append("       B.REGION_ID REGION_ID1,\n");
		sbSql.append("       B.REGION_CODE REGION_CODE1,\n");
		sbSql.append("       C.REGION_ID REGION_ID2,\n");
		sbSql.append("       C.REGION_CODE REGION_CODE2,\n");
		sbSql.append("       A.REGION_NAME SEND_CITY,\n");
		sbSql.append("       B.REGION_NAME SEND_CITY1,\n");
		sbSql.append("       C.REGION_NAME SEND_CITY2,\n");
		sbSql.append("       TSW.BILL_CRT_DATE,\n");
		sbSql.append("       TSBD.SERIES_ID SER_ID,\n");
		sbSql.append("       TSBD.SERIES_NAME SER_NAME,\n");
		sbSql.append("       TSB.CAR_TEAM,\n");
		sbSql.append("       TSB.CAR_NO,\n");
		sbSql.append("       TSBD.SEND_DISTANCE DISTANCE,\n");
		sbSql.append("       TSBD.SINGLE_PRICE ONE_PRICE,\n");
		sbSql.append("       TSBD.FUEL_COEFFICIENT\n");
		sbSql.append("  FROM TT_SALES_BALANCE D,\n");
		sbSql.append("       TT_SALES_WAYBILL TSW,\n");
		sbSql.append("       TM_DEALER        TD,\n");
		sbSql.append("       TT_SALES_LOGI    TSL,\n");
		sbSql.append("       TAB_WITH         TSBD,\n");
		sbSql.append("       TT_SALES_BOARD   TSB,\n");
		sbSql.append("       TM_VS_ADDRESS    X,\n");
		sbSql.append("       TM_REGION        A,\n");
		sbSql.append("       TM_REGION        B,\n");
		sbSql.append("       TM_REGION        C\n");
		sbSql.append(" WHERE D.BAL_ID = TSW.BAL_ID\n");
		sbSql.append("   AND TSW.SEND_DEALER_ID = TD.DEALER_ID\n");
		sbSql.append("   AND TSW.LOGI_ID = TSL.LOGI_ID\n");
		sbSql.append("   AND TSW.BILL_ID = TSBD.BILL_ID\n");
		sbSql.append("   AND TSW.ADDRESS_ID = X.ID(+)\n");
		sbSql.append("   AND X.PROVINCE_ID = A.REGION_CODE(+)\n");
		sbSql.append("   AND X.CITY_ID = B.REGION_CODE(+)\n");
		sbSql.append("   AND X.AREA_ID = C.REGION_CODE(+)\n");
		sbSql.append("   AND TSBD.BO_ID = TSB.BO_ID\n");
		sbSql.append("   AND TSW.STATUS = ?\n");
		params.add(Constant.STATUS_ENABLE);
		sbSql.append("UNION ALL\n");
		sbSql.append("SELECT TSW.AREA_ID,\n");
		sbSql.append("       TSW.BILL_ID,\n");
		sbSql.append("       TSW.BILL_NO,\n");
		sbSql.append("       TSW.LOGI_ID,\n");
		sbSql.append("       TSW.INVOICE_STATUS,\n");
		sbSql.append("       TSW.INVOICE_DATE,\n");
		sbSql.append("       TSW.IS_CONFIRM,\n");
		sbSql.append("       TSW.CONFIRM_DATE,\n");
		sbSql.append("       TD.DEALER_CODE,\n");
		sbSql.append("       TD.DEALER_NAME,\n");
		sbSql.append("       TSL.LOGI_NAME,\n");
		sbSql.append("       NVL(TSBD.SEND_NUM, 0) VEH_NUM,\n");
		sbSql.append("       TSB.DRIVER_NAME,\n");
		sbSql.append("       TSB.DRIVER_TEL,\n");
		sbSql.append("       A.REGION_ID,\n");
		sbSql.append("       A.REGION_CODE,\n");
		sbSql.append("       B.REGION_ID REGION_ID1,\n");
		sbSql.append("       B.REGION_CODE REGION_CODE1,\n");
		sbSql.append("       C.REGION_ID REGION_ID2,\n");
		sbSql.append("       C.REGION_CODE REGION_CODE2,\n");
		sbSql.append("       A.REGION_NAME SEND_CITY,\n");
		sbSql.append("       B.REGION_NAME SEND_CITY1,\n");
		sbSql.append("       C.REGION_NAME SEND_CITY2,\n");
		sbSql.append("       TSW.BILL_CRT_DATE,\n");
		sbSql.append("       TSBD.SERIES_ID SER_ID,\n");
		sbSql.append("       TSBD.SERIES_NAME SER_NAME,\n");
		sbSql.append("       TSB.CAR_TEAM,\n");
		sbSql.append("       TSB.CAR_NO,\n");
		sbSql.append("       NVL((SELECT S.DISTANCE\n");
		sbSql.append("             FROM TT_SALES_CITY_DIS S\n");
		sbSql.append("            WHERE S.CITY_ID = C.REGION_ID\n");
		sbSql.append("              AND S.YIELDLY = TSW.AREA_ID\n");
		sbSql.append("              AND S.CAR_TIE_ID = TSBD.SERIES_ID),\n");
		sbSql.append("           0) DISTANCE,\n");
		sbSql.append("       NVL((SELECT S.SINGLE_PLACE\n");
		sbSql.append("             FROM TT_SALES_CITY_DIS S\n");
		sbSql.append("            WHERE S.CITY_ID = C.REGION_ID\n");
		sbSql.append("              AND S.YIELDLY = TSW.AREA_ID\n");
		sbSql.append("              AND S.CAR_TIE_ID = TSBD.SERIES_ID),\n");
		sbSql.append("           0) ONE_PRICE,\n");
		sbSql.append("       NVL((SELECT CASE WHEN TO_CHAR(S.FUEL_BEGIN_DATE,'YYYY-MM-DD') <= TO_CHAR(TSW.CONFIRM_DATE,'YYYY-MM-DD') AND TO_CHAR(S.FUEL_END_DATE,'YYYY-MM-DD') >= TO_CHAR(TSW.CONFIRM_DATE,'YYYY-MM-DD') THEN S.FUEL_COEFFICIENT ELSE 0 END\n");
		sbSql.append("             FROM TT_SALES_CITY_DIS S\n");
		sbSql.append("            WHERE S.CITY_ID = C.REGION_ID\n");
		sbSql.append("              AND S.YIELDLY = TSW.AREA_ID\n");
		sbSql.append("              AND S.CAR_TIE_ID = TSBD.SERIES_ID),\n");
		sbSql.append("           0) FUEL_COEFFICIENT\n");
		sbSql.append("  FROM TT_SALES_WAYBILL TSW,\n");
		sbSql.append("       TM_DEALER        TD,\n");
		sbSql.append("       TT_SALES_LOGI    TSL,\n");
		sbSql.append("       TAB_WITH_01      TSBD,\n");
		sbSql.append("       TT_SALES_BOARD   TSB,\n");
		sbSql.append("       TM_VS_ADDRESS    X,\n");
		sbSql.append("       TM_REGION        A,\n");
		sbSql.append("       TM_REGION        B,\n");
		sbSql.append("       TM_REGION        C\n");
		sbSql.append(" WHERE TSW.SEND_DEALER_ID = TD.DEALER_ID\n");
		sbSql.append("   AND TSW.LOGI_ID = TSL.LOGI_ID\n");
		sbSql.append("   AND TSW.BILL_ID = TSBD.BILL_ID\n");
		sbSql.append("   AND TSW.ADDRESS_ID = X.ID(+)\n");
		sbSql.append("   AND X.PROVINCE_ID = A.REGION_CODE(+)\n");
		sbSql.append("   AND X.CITY_ID = B.REGION_CODE(+)\n");
		sbSql.append("   AND X.AREA_ID = C.REGION_CODE(+)\n");
		sbSql.append("   AND TSBD.BO_ID = TSB.BO_ID\n");
		sbSql.append("   AND TSW.STATUS = ?\n");
		params.add(Constant.STATUS_ENABLE);
		sbSql.append("   AND NOT EXISTS\n");
		sbSql.append(" (SELECT S.BAL_ID FROM TT_SALES_BALANCE S WHERE S.BAL_ID = TSW.BAL_ID)\n");
		sbSql.append("   AND TSW.LOGI_ID != ?)\n");
		params.add(Constant.DEFAULT_VALUE);
		sbSql.append("SELECT S.LOGI_ID,\n");
		sbSql.append("       S.BILL_ID,\n");
		sbSql.append("       S.BILL_NO,\n");
		sbSql.append("       S.INVOICE_STATUS,\n");
		sbSql.append("       S.INVOICE_DATE,\n");
		sbSql.append("       S.IS_CONFIRM,\n");
		sbSql.append("       S.CONFIRM_DATE,\n");
		sbSql.append("       S.DEALER_CODE,\n");
		sbSql.append("       S.DEALER_NAME,\n");
		sbSql.append("       S.LOGI_NAME,\n");
		sbSql.append("       S.VEH_NUM,\n");
		sbSql.append("       S.DRIVER_NAME,\n");
		sbSql.append("       S.DRIVER_TEL,\n");
		sbSql.append("       S.SEND_CITY,\n");
		sbSql.append("       S.SEND_CITY1,\n");
		sbSql.append("       S.SEND_CITY2,\n");
		sbSql.append("       S.DISTANCE,\n");
		sbSql.append("       S.SER_ID,\n");
		sbSql.append("       S.SER_NAME,\n");
		sbSql.append("       S.AREA_ID,\n");
		sbSql.append("       S.REGION_ID,\n");
		sbSql.append("       S.REGION_ID1,\n");
		sbSql.append("       S.REGION_ID2,\n");
		sbSql.append("       S.REGION_CODE,\n");
		sbSql.append("       S.REGION_CODE1,\n");
		sbSql.append("       S.REGION_CODE2,\n");
		sbSql.append("       S.BILL_CRT_DATE,\n");
		sbSql.append("       S.CAR_TEAM,\n");
		sbSql.append("       S.CAR_NO,\n");
		sbSql.append("       S.ONE_PRICE,\n");
		sbSql.append("       CASE WHEN S.LOGI_NAME ='自提' THEN 0 ELSE S.CG_AMOUNT END CG_AMOUNT\n");
		/*sbSql.append("       DECODE(S.REGION_ID,\n");
		sbSql.append("              1000000000100021,\n");
		sbSql.append("              S.CG_AMOUNT + S.VEH_NUM * 270,\n");
		sbSql.append("              1000000000100026,\n");
		sbSql.append("              S.CG_AMOUNT * 1.15,\n");
		sbSql.append("              S.CG_AMOUNT) CG_AMOUNT\n");*/
		sbSql.append("  FROM (SELECT W.LOGI_ID,\n");
		sbSql.append("               W.BILL_ID,\n");
		sbSql.append("               W.BILL_NO,\n");
		sbSql.append("               W.INVOICE_STATUS,\n");
		sbSql.append("               W.INVOICE_DATE,\n");
		sbSql.append("               W.IS_CONFIRM,\n");
		sbSql.append("               W.CONFIRM_DATE,\n");
		sbSql.append("               W.DEALER_CODE,\n");
		sbSql.append("               W.DEALER_NAME,\n");
		sbSql.append("               W.LOGI_NAME,\n");
		sbSql.append("               W.VEH_NUM,\n");
		sbSql.append("               W.DRIVER_NAME,\n");
		sbSql.append("               W.DRIVER_TEL,\n");
		sbSql.append("               W.SEND_CITY,\n");
		sbSql.append("               W.SEND_CITY1,\n");
		sbSql.append("               W.SEND_CITY2,\n");
		sbSql.append("               W.DISTANCE * W.VEH_NUM DISTANCE,\n");
		sbSql.append("               W.SER_ID,\n");
		sbSql.append("               W.SER_NAME,\n");
		sbSql.append("               W.AREA_ID,\n");
		sbSql.append("               W.REGION_ID,\n");
		sbSql.append("               W.REGION_ID1,\n");
		sbSql.append("               W.REGION_ID2,\n");
		sbSql.append("               W.REGION_CODE,\n");
		sbSql.append("               W.REGION_CODE1,\n");
		sbSql.append("               W.REGION_CODE2,\n");
		sbSql.append("               W.BILL_CRT_DATE,\n");
		sbSql.append("               W.CAR_TEAM,\n");
		sbSql.append("               W.CAR_NO,\n");
		sbSql.append("               W.ONE_PRICE,\n");
		sbSql.append("               W.FUEL_COEFFICIENT,\n");
		sbSql.append("               --逻辑：如果特殊运费有，就是特殊运费，否则一般运费\n");
		sbSql.append("               (DECODE(W.TS_AMOUNT,\n");
		sbSql.append("                      0,\n");
		sbSql.append("                      W.ONE_PRICE,\n");
//		sbSql.append("                      NVL((SELECT I.AMOUNT\n");
//		sbSql.append("                            FROM TT_SALES_MILSET H, TT_SALES_FARE I\n");
//		sbSql.append("                           WHERE H.MIL_ID = I.MIL_ID\n");
//		sbSql.append("                             AND H.YIELDLY = W.AREA_ID\n");
//		sbSql.append("                             AND I.YIELDLY = W.AREA_ID\n");
//		sbSql.append("                             AND I.GROUP_ID = W.SER_ID\n");
//		sbSql.append("                             AND H.MIL_START <= W.DISTANCE\n");
//		sbSql.append("                             AND H.MIL_END >= W.DISTANCE),\n");
//		sbSql.append("                          0),\n");
		sbSql.append("                      W.TS_AMOUNT)+W.FUEL_COEFFICIENT) * W.DISTANCE * W.VEH_NUM CG_AMOUNT\n");
		sbSql.append("          FROM (SELECT T.BILL_ID,\n");
		sbSql.append("                       T.BILL_NO,\n");
		sbSql.append("                       T.INVOICE_STATUS,\n");
		sbSql.append("                       T.INVOICE_DATE,\n");
		sbSql.append("                       T.IS_CONFIRM,\n");
		sbSql.append("                       T.CONFIRM_DATE,\n");
		sbSql.append("                       T.DEALER_CODE,\n");
		sbSql.append("                       T.DEALER_NAME,\n");
		sbSql.append("                       T.LOGI_NAME,\n");
		sbSql.append("                       T.LOGI_ID,\n");
		sbSql.append("                       T.VEH_NUM,\n");
		sbSql.append("                       T.DRIVER_NAME,\n");
		sbSql.append("                       T.DRIVER_TEL,\n");
		sbSql.append("                       T.SEND_CITY,\n");
		sbSql.append("                       T.SEND_CITY1,\n");
		sbSql.append("                       T.SEND_CITY2,\n");
		sbSql.append("                       T.DISTANCE,\n");
		sbSql.append("                       T.SER_ID,\n");
		sbSql.append("                       T.AREA_ID,\n");
		sbSql.append("                       T.SER_NAME,\n");
		sbSql.append("                       T.REGION_ID,\n");
		sbSql.append("                       T.REGION_ID1,\n");
		sbSql.append("                       T.REGION_ID2,\n");
		sbSql.append("                       T.REGION_CODE,\n");
		sbSql.append("                       T.REGION_CODE1,\n");
		sbSql.append("                       T.REGION_CODE2,\n");
		sbSql.append("                       T.BILL_CRT_DATE,\n");
		sbSql.append("                       T.CAR_TEAM,\n");
		sbSql.append("                       T.CAR_NO,\n");
		sbSql.append("                       T.ONE_PRICE,\n");
		sbSql.append("                       T.FUEL_COEFFICIENT,\n");
		sbSql.append("                       NVL((SELECT NVL(N.AMOUNT, 0)\n");
		sbSql.append("                             FROM TM_SPECIAL_CITY_FARE N, TM_REGION O\n");
		sbSql.append("                            WHERE N.PROVINCE_ID = O.REGION_CODE\n");
		sbSql.append("                              AND N.YIELDLY = T.AREA_ID\n");
		sbSql.append("                              AND N.SERIES_ID = T.SER_ID\n");
		sbSql.append("                              AND O.REGION_ID = T.REGION_ID\n");
		sbSql.append("                            GROUP BY O.REGION_ID, N.AMOUNT),\n");
		sbSql.append("                           0) TS_AMOUNT\n");
		sbSql.append("                  FROM T) W)S\n");
		sbSql.append("         WHERE 1 = 1\n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSqlByPar(
				"S.AREA_ID", poseId, params));// 车厂端查询列表产地数据权限
		sbSql.append(MaterialGroupManagerDao.getPoseIdLogiSqlByPar("S.LOGI_ID",
				poseId, params));// 物流判断（是否是物流公司）
		if (null != dealerCode && !"".equals(dealerCode)) {
			sbSql.append(Utility.getConSqlByParamForEqual(dealerCode, params,"S", "DEALER_CODE"));
		}
		if (null != dealerCodeN && !"".equals(dealerCodeN)) {
			sbSql.append(Utility.getConSqlByParamForNotEqual(dealerCodeN, params, "S", "DEALER_CODE"));
		}
		if (orderNo != null && !"".equals(orderNo)) {
			sbSql.append("   AND S.BILL_NO  LIKE ?\n");// 运单号
			params.add("%" + orderNo + "%");
		}
		if (isConfirm != null && !"".equals(isConfirm)){
			sbSql.append(" AND S.IS_CONFIRM = ?");
			params.add(isConfirm);
		}
		if (invoiceStatus != null && !"".equals(invoiceStatus)){
			sbSql.append(" AND S.INVOICE_STATUS = ?");
			params.add(invoiceStatus);
		}
		if (invoiceStartDate != null && !"".equals(invoiceStartDate)) {
			sbSql.append(" AND TO_CHAR(S.INVOICE_DATE,'YYYY-MM-DD') >= ?");
			params.add(invoiceStartDate);
		}
		if (invoiceEndDate != null && !"".equals(invoiceEndDate)) {
			sbSql.append(" AND TO_CHAR(S.INVOICE_DATE,'YYYY-MM-DD') <= ?");
			params.add(invoiceEndDate);
		}
		if (sendStartDate != null && !"".equals(sendStartDate)) {
			sbSql.append("   AND S.BILL_CRT_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(sendStartDate + " 00:00:00");
		}
		if (sendEndDate != null && !"".equals(sendEndDate)) {
			sbSql.append("   AND S.BILL_CRT_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(sendEndDate + " 23:59:59");
		}
		if (provinceId != null && !"".equals(provinceId)) {
			sbSql.append(" AND S.REGION_CODE =?\n");
			params.add(provinceId);
		}
		if (cityId != null && !"".equals(cityId)) {
			sbSql.append(" AND S.REGION_CODE1=?\n");
			params.add(cityId);
		}
		if (countyId != null && !"".equals(countyId)) {
			sbSql.append(" AND S.SEND_CITY2 LIKE ?\n");
			params.add("%" + countyId + "%");
		}
		if (area_id != null && !"".equals(area_id)) {
			sbSql.append(" AND S.AREA_ID = ?\n");
			params.add(area_id);
		}
		if (logiName != null && !"".equals(logiName)) {
			sbSql.append(" AND S.LOGI_ID = ?\n");
			params.add(logiName);
		}
		if (VIN != null && !"".equals(VIN)) {
			sbSql.append("AND S.BILL_ID=(SELECT BILL_ID\n");
			sbSql.append("  FROM TT_SALES_BO_DETAIL A1, TT_SALES_ALLOCA_DE A2, TM_VEHICLE A3\n");
			sbSql.append(" WHERE A1.BO_DE_ID = A2.BO_DE_ID\n");
			sbSql.append("   AND A2.VEHICLE_ID = A3.VEHICLE_ID\n");
			sbSql.append("   AND A1.IS_ENABLE=?\n");
			sbSql.append("   AND A2.STATUS=?\n");
			sbSql.append("   AND A3.VIN=?)\n");
			sbSql.append("   AND S.SER_ID=( SELECT T.SERIES_ID FROM TM_VEHICLE T WHERE T.VIN=?)\n");
			params.add(Constant.IF_TYPE_YES);
			params.add(Constant.STATUS_ENABLE);
			params.add(VIN);
			params.add(VIN);
		}
		if(sOrderNo != null && !sOrderNo.equals("")) {
			sbSql.append("AND EXISTS (SELECT NULL FROM TT_VS_ORDER T1,TT_VS_ORDER_DETAIL T2,TT_SALES_BO_DETAIL T3\n");
			sbSql.append("WHERE T1.ORDER_ID=T2.ORDER_ID AND T2.DETAIL_ID = T3.OR_DE_ID AND T3.BILL_ID IS NOT NULL AND S.BILL_ID=T3.BILL_ID AND T1.ORDER_NO LIKE ?)");
			params.add("%"+sOrderNo+"%");
		}
		if(!XHBUtil.IsNull(confirmBeginDate)){
			sbSql.append(" AND TO_CHAR(S.CONFIRM_DATE,'YYYY-MM-DD') >= ?");
			params.add(confirmBeginDate);
		}
		
		if(!XHBUtil.IsNull(confirmEndDate)){
			sbSql.append(" AND TO_CHAR(S.CONFIRM_DATE,'YYYY-MM-DD') <= ?");
			params.add(confirmEndDate);
		}
		sbSql.append("ORDER BY S.BILL_CRT_DATE,S.BILL_ID ASC");// 排序
		Object[] obj = new Object[2];
		obj[0] = sbSql;
		obj[1] = params;
		return obj;
	}

	/**
	 * 根据运单ID获取组板ID
	 * 
	 * @param billId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBoIdByBillId(Long billId) {//
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select distinct t.BO_ID from tt_sales_bo_detail t where t.bill_id=?");
		List<Object> params = new ArrayList<Object>();
		params.add(billId);
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(),
				params, getFunName());
		return list;
	}
	
	/**
	 * 运单明细
	 * @author liufazhong
	 */
	public List<Map<String, Object>> queryWayBillDetail(String billId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVO.ORDER_NO,\n" );
		sql.append("       TD.DEALER_NAME,\n" );
		sql.append("       TC.CODE_DESC,\n" );
		sql.append("       VM.SERIES_NAME,\n" );
		sql.append("       VM.MODEL_CODE,\n" );
		sql.append("       vm.MODEL_NAME,\n" );
		sql.append("       VM.PACKAGE_NAME,\n" );
		sql.append("       TV.VIN,\n" );
		sql.append("       TV.ENGINE_NO,\n" );
		sql.append("       TV.HEGEZHENG_CODE,\n" );
		sql.append("       TVD.DISCOUNT_S_PRICE,\n" );
		sql.append("       TO_CHAR(TVO.CREATE_DATE, 'yyyy-mm-dd') CREATE_DATE\n" );
		sql.append("  FROM TT_SALES_WAYBILL TSW\n" );
		sql.append("  LEFT JOIN TT_SALES_BO_DETAIL TSB ON TSW.BILL_ID = TSB.BILL_ID\n" );
		sql.append("  LEFT JOIN TT_SALES_ALLOCA_DE TSA ON TSB.BO_DE_ID = TSA.BO_DE_ID\n" );
		sql.append("  LEFT JOIN TT_VS_ORDER_DETAIL TVD ON TSB.OR_DE_ID = TVD.DETAIL_ID\n" );
		sql.append("  LEFT JOIN TT_VS_ORDER TVO ON TVD.ORDER_ID = TVO.ORDER_ID\n" );
		sql.append("  LEFT JOIN TM_DEALER TD ON TVO.DEALER_ID = TD.DEALER_ID\n" );
		sql.append("  LEFT JOIN TM_VEHICLE TV ON TSA.VEHICLE_ID = TV.VEHICLE_ID\n" );
		sql.append("  LEFT JOIN TT_SA_ORDER TSO ON TVO.WR_ORDER_ID = TSO.ORDER_ID\n" );
		sql.append("  LEFT JOIN TC_CODE TC ON TSO.INVO_TYPE = TC.CODE_ID\n" );
		sql.append("  LEFT JOIN VW_MATERIAL_GROUP_MAT VM ON TV.MATERIAL_ID = VM.MATERIAL_ID\n" );
		sql.append(" WHERE TV.VIN IS NOT NULL\n");
		List<Object> params = new ArrayList<Object>();
		if (!XHBUtil.IsNull(billId)) {
			sql.append(" AND TSW.BILL_ID = ?");
			params.add(billId);
		}
		return pageQuery(sql.toString(), params, getFunName());
	}
	
	/**
	 * 查询运单明细
	 * @author liufazhong
	 */
	public PageResult<Map<String, Object>> queryWayBillDetailReport(Map<String, Object> map,int pageSize,int curPage) {
		String dealerCode = (String) map.get("dealerCode"); // 经销商CODE
		String dealerCodeN = (String) map.get("dealerCodeN");//不包含的经销商
		String orderNo = (String) map.get("orderNo"); // 运单号
		String sOrderNo = (String)map.get("sOrderNo");//销售清单号
		String VIN = (String) map.get("VIN");// VIN
		String isConfirm = (String)map.get("isConfirm");//是否确认
		String confirmBeginDate = (String)map.get("confirmBeginDate");//确认开始时间
		String confirmEndDate =(String)map.get("confirmEndDate");//确认结束时间
		String sendStartDate = (String) map.get("sendStartDate"); // 发运时间
		String sendEndDate = (String) map.get("sendEndDate");
		String invoiceStatus = (String)map.get("invoiceStatus");//运单开票状态
		String invoiceStartDate = (String)map.get("invoiceStartDate");//开票开始时间
		String invoiceEndDate = (String)map.get("invoiceEndDate");//开票结束时间
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSA.DETAIL_ID,\n" );
		sql.append("       TSW.BILL_ID,\n" );
		sql.append("       TSW.BILL_NO,\n" );
		sql.append("       TSW.IS_CONFIRM,\n" );
		sql.append("       TVO.ORDER_NO,\n" );
		sql.append("       TD.DEALER_NAME,\n" );
		sql.append("       TC.CODE_DESC INVO_TYPE,\n" );
		sql.append("       VM.SERIES_NAME,\n" );
		sql.append("       VM.MODEL_CODE,\n" );
		sql.append("       VM.MODEL_NAME,\n" );
		sql.append("       VM.PACKAGE_NAME,\n" );
		sql.append("       VM.COLOR_NAME,\n" );
		sql.append("       TV.VIN,\n" );
		sql.append("       TV.ENGINE_NO,\n" );
		sql.append("       TV.HEGEZHENG_CODE,\n" );
		sql.append("       TVD.DISCOUNT_S_PRICE,\n" );
		sql.append("       TO_CHAR(TVO.CREATE_DATE, 'yyyy-mm-dd') CREATE_DATE,\n" );
		sql.append("       TSA.INVOICE_STATUS,\n" );
		sql.append("       CASE\n" );
		sql.append("         WHEN TSA.INVOICE_STATUS = 10041001 AND TU.NAME IS NULL THEN\n" );
		sql.append("          'ERP开票'\n" );
		sql.append("         ELSE\n" );
		sql.append("          TU.NAME\n" );
		sql.append("       END INVOICE_USER,\n" );
		sql.append("       TSA.INVOICE_DATE,\n" );
		sql.append("       TSA.INVOICE_NO,\n" );
		sql.append("       TSA.INVOICE_REMARK\n" );
		sql.append("  FROM TT_SALES_WAYBILL TSW\n" );
		sql.append("  LEFT JOIN TT_SALES_BO_DETAIL TSB\n" );
		sql.append("    ON TSW.BILL_ID = TSB.BILL_ID\n" );
		sql.append("  LEFT JOIN TT_SALES_ALLOCA_DE TSA\n" );
		sql.append("    ON TSB.BO_DE_ID = TSA.BO_DE_ID\n" );
		sql.append("  LEFT JOIN TT_VS_ORDER_DETAIL TVD\n" );
		sql.append("    ON TSB.OR_DE_ID = TVD.DETAIL_ID\n" );
		sql.append("  LEFT JOIN TT_VS_ORDER TVO\n" );
		sql.append("    ON TVD.ORDER_ID = TVO.ORDER_ID\n" );
		sql.append("  LEFT JOIN TM_DEALER TD\n" );
		sql.append("    ON TVO.DEALER_ID = TD.DEALER_ID\n" );
		sql.append("  LEFT JOIN TM_VEHICLE TV\n" );
		sql.append("    ON TSA.VEHICLE_ID = TV.VEHICLE_ID\n" );
		sql.append("  LEFT JOIN TT_SA_ORDER TSO\n" );
		sql.append("    ON TVO.WR_ORDER_ID = TSO.ORDER_ID\n" );
		sql.append("  LEFT JOIN TC_CODE TC\n" );
		sql.append("    ON TSO.INVO_TYPE = TC.CODE_ID\n" );
		sql.append("  LEFT JOIN VW_MATERIAL_GROUP_MAT VM\n" );
		sql.append("    ON TV.MATERIAL_ID = VM.MATERIAL_ID\n" );
		sql.append("  LEFT JOIN TC_USER TU\n" );
		sql.append("    ON TSA.INVOICE_USER = TU.USER_ID\n" );
		sql.append(" WHERE TV.VIN IS NOT NULL\n");
		List<Object> params = new ArrayList<Object>();
		if (!XHBUtil.IsNull(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "td", "DEALER_CODE"));
		}
		if (!XHBUtil.IsNull(dealerCodeN)) {
			sql.append(Utility.getConSqlByParamForNotEqual(dealerCodeN, params, "td", "DEALER_CODE"));
		}
		if (!XHBUtil.IsNull(orderNo)) {
			sql.append(" AND tsw.BILL_NO LIKE ?");
			params.add("%"+orderNo+"%");
		}
		if (!XHBUtil.IsNull(sOrderNo)) {
			sql.append(" AND tvo.ORDER_NO LIKE ?");
			params.add("%"+sOrderNo+"%");
		}
		if (!XHBUtil.IsNull(VIN)) {
			sql.append(" AND tv.vin like ?");
			params.add("%"+VIN+"%");
		}
		if (!XHBUtil.IsNull(isConfirm)) {
			sql.append(" AND tsw.is_confirm = ?");
			params.add(isConfirm);
		}
		if (!XHBUtil.IsNull(invoiceStatus)) {
			sql.append(" AND tsa.invoice_status = ?");
			params.add(invoiceStatus);
		}
		if (invoiceStartDate != null && !"".equals(invoiceStartDate)) {
			sql.append(" AND TO_CHAR(tsa.INVOICE_DATE,'YYYY-MM-DD') >= ?");
			params.add(invoiceStartDate);
		}
		if (invoiceEndDate != null && !"".equals(invoiceEndDate)) {
			sql.append(" AND TO_CHAR(tsa.INVOICE_DATE,'YYYY-MM-DD') <= ?");
			params.add(invoiceEndDate);
		}
		if (!XHBUtil.IsNull(sendStartDate)) {
			sql.append(" AND to_char(tsw.bill_crt_date,'yyyy-mm-dd') >= ?");
			params.add(sendStartDate);
		}
		if (!XHBUtil.IsNull(sendEndDate)) {
			sql.append(" AND to_char(tsw.bill_crt_date,'yyyy-mm-dd') <= ?");
			params.add(sendEndDate);
		}
		if (!XHBUtil.IsNull(confirmBeginDate)) {
			sql.append(" AND to_char(tsw.confirm_date,'yyyy-mm-dd') >= ?");
			params.add(confirmBeginDate);
		}
		if (!XHBUtil.IsNull(confirmEndDate)) {
			sql.append(" AND to_char(tsw.confirm_date,'yyyy-mm-dd') <= ?");
			params.add(confirmEndDate);
		}
		return pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
	}
	

	/**
	 * 根据ID查询运单
	 * @author liufazhong
	 */
	public List<Map<String, Object>> queryWayBillById(String billId){
		String sql = "SELECT * FROM TT_SALES_WAYBILL WHERE BILL_ID = "+billId;
		return pageQuery(sql, null, getFunName());
	}
}
