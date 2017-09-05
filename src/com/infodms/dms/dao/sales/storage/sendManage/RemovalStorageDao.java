package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @ClassName : RemovalStorageDao
 * @Description : 车辆出库DAO
 * @author : ranjian
 *         CreateDate : 2013-5-15
 */
public class RemovalStorageDao extends BaseDao<PO> {
	private static final RemovalStorageDao dao = new RemovalStorageDao();
	public static final RemovalStorageDao getInstance()
	{
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		return null;
	}
	
	/**
	 * 车辆出库信息查询
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getRemovalStorageQuery(Map<String, Object> map, int curPage, int pageSize)
					throws Exception
	{
		/****************************** 页面查询字段start **************************/
		String groupCode = (String) map.get("groupCode"); // 物料组
		String materialCode = (String) map.get("materialCode"); // 物料
		String yieldly = (String) map.get("yieldly"); // 产地		
		String dealerCode = (String) map.get("dealerCode"); // 经销商CODE
		String orderType = (String) map.get("orderType"); // 订单类型
		String orderNo = (String) map.get("orderNo"); // 销售订单号
		String invoiceNo = (String) map.get("invoiceNo"); // 发票号
		String logiName = (String) map.get("logiName"); // 物流公司
		String raiseStartDate = (String) map.get("boStartDate"); // 组板日期开始
		String raiseEndDate = (String) map.get("boEndDate"); // 组板日期结束
		String boNo = (String) map.get("boNo"); //组板编号
		String allocaStartDate = (String) map.get("allocaStartDate"); // 配车日期开始
		String allocaEndDate = (String) map.get("allocaEndDate"); // 配车日期结束
		String poseId = (String) map.get("poseId");
		String boId = (String) map.get("boId");
		/****************************** 页面查询字段end ***************************/
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TD.DEALER_CODE,\n");
		sbSql.append("       TD.DEALER_NAME,\n");
		sbSql.append("       TSB.BO_NO,\n");
		sbSql.append("       VMGM.MODEL_NAME,\n");
		sbSql.append("       VMGM.PACKAGE_NAME,\n");
		sbSql.append("       VMGM.MATERIAL_CODE,\n");
		sbSql.append("       VMGM.MATERIAL_NAME,\n");
		sbSql.append("       TV.VIN,\n");
		sbSql.append("       decode(TV.IS_PASS_STATUS,1,1,0,0,-1) IS_PASS_STATUS,\n");
		sbSql.append("		 TSBD.ORDER_NO, -- 订单流水号\n");
		sbSql.append("		 TSBD.INVOICE_NO, --  发票号\n");
		sbSql.append("       TV.ENGINE_NO,\n");
		sbSql.append("       TO_CHAR(TV.PRODUCT_DATE,'YYYY-MM-DD') PRODUCT_DATE,\n");
		sbSql.append("       TO_CHAR(TV.OFFLINE_DATE,'YYYY-MM-DD') OFFLINE_DATE,\n");
		sbSql.append("       TSAA.AREA_NAME,\n");
		sbSql.append("       TSR.ROAD_NAME,\n");
		sbSql.append("       TSS.SIT_NAME,\n");
		sbSql.append("       TO_CHAR(TV.ORG_STORAGE_DATE,'YYYY-MM-DD') ORG_STORAGE_DATE,\n");
		sbSql.append("       TO_CHAR(TSB.ALLOCA_DATE,'YYYY-MM-DD') ALLOCA_DATE,\n");
		sbSql.append("       TO_CHAR(TSB.BO_DATE,'YYYY-MM-DD') BO_DATE,\n");
		sbSql.append("       CEIL(SYSDATE - TV.ORG_STORAGE_DATE) STORAGE_DAYS,\n");
		sbSql.append("       TSL.LOGI_NAME\n");
		sbSql.append("  FROM TT_SALES_ALLOCA_DE    TSAD, --配车明细表\n");
		sbSql.append("       TT_SALES_BO_DETAIL    TSBD, --组板明细表\n");
		sbSql.append("       TT_SALES_BOARD        TSB, --组板表\n");
//		sbSql.append("       TT_SALES_ASSIGN       TSAN, --分派表\n");
		sbSql.append("       TM_VEHICLE            TV, --车辆表\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT VMGM, --车辆有关信息试图\n");
		sbSql.append("       TM_DEALER             TD, --经销商表\n");
		sbSql.append("       TT_SALES_SIT          TSS, --库区表\n");
		sbSql.append("       TT_SALES_ROAD         TSR, --库区表\n");
		sbSql.append("       TT_SALES_AREA         TSAA, --库区表\n");
		sbSql.append("       TT_SALES_LOGI         TSL --物流商表\n");
		sbSql.append(" WHERE TSAD.BO_DE_ID = TSBD.BO_DE_ID\n");
		sbSql.append("   AND TSBD.IS_ENABLE=" + Constant.IF_TYPE_YES + "\n");
		sbSql.append("   AND TSBD.BO_ID = TSB.BO_ID\n");
//		sbSql.append("   AND TSB.ASS_ID = TSAN.ASS_ID\n");
		sbSql.append("   AND TSBD.REC_DEALER_ID = TD.DEALER_ID\n");
//		sbSql.append("   AND TSAN.REC_DEALER_ID = TD.DEALER_ID\n");
		sbSql.append("   AND TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append("   AND TV.MATERIAL_ID = VMGM.MATERIAL_ID\n");
		sbSql.append("   AND TSAD.VEHICLE_ID = TSS.VEHICLE_ID\n");
		sbSql.append("   AND TSS.ROAD_ID = TSR.ROAD_ID\n");
		sbSql.append("   AND TSR.AREA_ID = TSAA.AREA_ID\n");
		sbSql.append("   AND TSBD.LOGI_ID = TSL.LOGI_ID(+)");
		sbSql.append("   AND TV.LIFE_CYCLE = ").append(Constant.VEHICLE_LIFE_02);//车厂库存
		sbSql.append("   AND TV.LOCK_STATUS = ").append(Constant.LOCK_STATUS_08).append("\r\n");//配车锁定
		sbSql.append("   AND TSB.BO_ID=" + boId + "\n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TSBD.AREA_ID", poseId));//车厂端查询列表产地数据权限
		if (yieldly != null && !"".equals(yieldly))
		{//产地
			sbSql.append("   AND TSAN.AREA_ID =" + yieldly + "\n");
		}
		if (dealerCode != null && !"".equals(dealerCode))
		{//经销商CODE
			sbSql.append(MaterialGroupManagerDao.getDealerQuerySql("TSAN.REC_DEALER_ID", dealerCode));
		}
		if (orderType != null && !"".equals(orderType))
		{//订单类型
			sbSql.append("   AND TSAN.ORDER_TYPE = " + orderType + "\n");
		}
		if (orderNo != null && !"".equals(orderNo))
		{//销售订单号
			sbSql.append("   AND TSAN.ORDER_NO  LIKE '%" + orderNo + "%'\n");
		}
		if (invoiceNo != null && !"".equals(invoiceNo))
		{//发票号
			sbSql.append("   AND TSAN.INVOICE_NO LIKE '%" + invoiceNo + "%'\n");
		}
		if (logiName != null && !"".equals(logiName))
		{//物流公司
			sbSql.append("   AND TSL.LOGI_ID=" + logiName + "\n");
		}
		if (raiseStartDate != null && !"".equals(raiseStartDate))
		{//组板日期开始
			sbSql.append("   AND TO_CHAR(TSB.BO_DATE,'YYYY-MM-DD')>='" + raiseStartDate + "'\n");
		}
		if (raiseEndDate != null && !"".equals(raiseEndDate))
		{//组板日期结束
			sbSql.append("   AND TO_CHAR(TSB.BO_DATE,'YYYY-MM-DD')<='" + raiseEndDate + "'\n");
		}
		if (boNo != null && !"".equals(boNo))
		{//组板编号
			sbSql.append("   AND TSB.BO_NO like'%" + boNo + "%'\n");
		}
		if (allocaStartDate != null && !"".equals(allocaStartDate))
		{//配车日期开始
			sbSql.append("   AND TO_CHAR(TSB.ALLOCA_DATE,'YYYY-MM-DD')>='" + allocaStartDate + "'\n");
		}
		if (allocaEndDate != null && !"".equals(allocaEndDate))
		{//配车日期结束
			sbSql.append("   AND TO_CHAR(TSB.ALLOCA_DATE,'YYYY-MM-DD')<='" + allocaEndDate + "'\n");
		}
		if (groupCode != null && !"".equals(groupCode))
		{//物料组过滤
			sbSql.append("   AND VMGM.PACKAGE_CODE ='" + groupCode + "'\n");
		}
		if (groupCode != null && !"".equals(groupCode))
		{//物料过滤
			sbSql.append("   AND VMGM.PACKAGE_CODE ='" + groupCode + "'\n");
		}
		if (materialCode != null && !"".equals(materialCode))
		{//物料过滤
			sbSql.append("   AND VMGM.MATERIAL_CODE ='" + materialCode + "'\n");
		}
		sbSql.append("   ORDER BY TO_NUMBER(TSAA.AREA_NAME),TO_NUMBER(TSR.ROAD_NAME),TO_NUMBER(TSS.SIT_NAME) ASC\n");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 车辆出库信息查询
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> pdiCheckQuery(Map<String, Object> map, int curPage, int pageSize)
					throws Exception
	{
		/****************************** 页面查询字段start **************************/
		String groupCode = (String) map.get("groupCode"); // 物料组
		String materialCode = (String) map.get("materialCode"); // 物料
		String yieldly = (String) map.get("yieldly"); // 产地		
		String dealerCode = (String) map.get("dealerCode"); // 经销商CODE
		String orderType = (String) map.get("orderType"); // 订单类型
		String orderNo = (String) map.get("orderNo"); // 销售订单号
		String invoiceNo = (String) map.get("invoiceNo"); // 发票号
		String logiName = (String) map.get("logiName"); // 物流公司
		String raiseStartDate = (String) map.get("boStartDate"); // 组板日期开始
		String raiseEndDate = (String) map.get("boEndDate"); // 组板日期结束
		String boNo = (String) map.get("boNo"); //组板编号
		String allocaStartDate = (String) map.get("allocaStartDate"); // 配车日期开始
		String allocaEndDate = (String) map.get("allocaEndDate"); // 配车日期结束
		String poseId = (String) map.get("poseId");
		String VIN = (String)map.get("VIN");
		String checkStatus = (String)map.get("checkStatus");
		/****************************** 页面查询字段end ***************************/
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TD.DEALER_CODE,\n");
		sbSql.append("       TD.DEALER_NAME,\n");
		sbSql.append("       TSB.BO_NO,\n");
		sbSql.append("       VMGM.MODEL_NAME,\n");
		sbSql.append("       VMGM.PACKAGE_NAME,\n");
		sbSql.append("       VMGM.MATERIAL_CODE,\n");
		sbSql.append("       VMGM.MATERIAL_NAME,\n");
		sbSql.append("       TV.VIN,\n");
		sbSql.append("       decode(TV.IS_PASS_STATUS,1,1,0,0,-1) IS_PASS_STATUS,\n");
		sbSql.append("		 TSBD.ORDER_NO, -- 订单流水号\n");
		sbSql.append("		 TSBD.INVOICE_NO, --  发票号\n");
		sbSql.append("       TV.ENGINE_NO,\n");
		sbSql.append("       TO_CHAR(TV.PRODUCT_DATE,'YYYY-MM-DD') PRODUCT_DATE,\n");
		sbSql.append("       TO_CHAR(TV.OFFLINE_DATE,'YYYY-MM-DD') OFFLINE_DATE,\n");
		sbSql.append("       TSAA.AREA_NAME,\n");
		sbSql.append("       TSR.ROAD_NAME,\n");
		sbSql.append("       TSS.SIT_NAME,\n");
		sbSql.append("       TO_CHAR(TV.ORG_STORAGE_DATE,'YYYY-MM-DD') ORG_STORAGE_DATE,\n");
		sbSql.append("       TO_CHAR(TSB.ALLOCA_DATE,'YYYY-MM-DD') ALLOCA_DATE,\n");
		sbSql.append("       TO_CHAR(TSB.BO_DATE,'YYYY-MM-DD') BO_DATE,\n");
		sbSql.append("       CEIL(SYSDATE - TV.ORG_STORAGE_DATE) STORAGE_DAYS,\n");
		sbSql.append("       TSL.LOGI_NAME\n");
		sbSql.append("  FROM TT_SALES_ALLOCA_DE    TSAD, --配车明细表\n");
		sbSql.append("       TT_SALES_BO_DETAIL    TSBD, --组板明细表\n");
		sbSql.append("       TT_SALES_BOARD        TSB, --组板表\n");
//		sbSql.append("       TT_SALES_ASSIGN       TSAN, --分派表\n");
		sbSql.append("       TM_VEHICLE            TV, --车辆表\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT VMGM, --车辆有关信息试图\n");
		sbSql.append("       TM_DEALER             TD, --经销商表\n");
		sbSql.append("       TT_SALES_SIT          TSS, --库区表\n");
		sbSql.append("       TT_SALES_ROAD         TSR, --库区表\n");
		sbSql.append("       TT_SALES_AREA         TSAA, --库区表\n");
		sbSql.append("       TT_SALES_LOGI         TSL --物流商表\n");
		sbSql.append(" WHERE TSAD.BO_DE_ID = TSBD.BO_DE_ID\n");
		sbSql.append("   AND TSBD.IS_ENABLE=" + Constant.IF_TYPE_YES + "\n");
		sbSql.append("   AND TSBD.BO_ID = TSB.BO_ID\n");
//		sbSql.append("   AND TSB.ASS_ID = TSAN.ASS_ID\n");
		sbSql.append("   AND TSBD.REC_DEALER_ID = TD.DEALER_ID\n");
//		sbSql.append("   AND TSAN.REC_DEALER_ID = TD.DEALER_ID\n");
		sbSql.append("   AND TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append("   AND TV.MATERIAL_ID = VMGM.MATERIAL_ID\n");
		sbSql.append("   AND TSAD.VEHICLE_ID = TSS.VEHICLE_ID\n");
		sbSql.append("   AND TSS.ROAD_ID = TSR.ROAD_ID\n");
		sbSql.append("   AND TSR.AREA_ID = TSAA.AREA_ID\n");
		sbSql.append("   AND TSBD.LOGI_ID = TSL.LOGI_ID(+)");
//		sbSql.append("   AND NVL(TV.IS_PASS_STATUS,0) = 0");
		sbSql.append("   AND TSAD.STATUS = "+Constant.STATUS_ENABLE+"\n");
		sbSql.append("   AND TV.LIFE_CYCLE = ").append(Constant.VEHICLE_LIFE_02);//车厂库存
		sbSql.append("   AND TV.LOCK_STATUS = ").append(Constant.LOCK_STATUS_08).append("\r\n");//配车锁定
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TSBD.AREA_ID", poseId));//车厂端查询列表产地数据权限
		if (yieldly != null && !"".equals(yieldly))
		{//产地
			sbSql.append("   AND TSAN.AREA_ID =" + yieldly + "\n");
		}
		if (dealerCode != null && !"".equals(dealerCode))
		{//经销商CODE
			sbSql.append(MaterialGroupManagerDao.getDealerQuerySql("TSAN.REC_DEALER_ID", dealerCode));
		}
		if (orderType != null && !"".equals(orderType))
		{//订单类型
			sbSql.append("   AND TSAN.ORDER_TYPE = " + orderType + "\n");
		}
		if(VIN != null && !"".equals(VIN)) {
			//vin
			sbSql.append("	AND TV.VIN LIKE '%"+ VIN +"%'");
		}
		if (orderNo != null && !"".equals(orderNo))
		{//销售订单号
			sbSql.append("   AND TSAN.ORDER_NO  LIKE '%" + orderNo + "%'\n");
		}
		if (invoiceNo != null && !"".equals(invoiceNo))
		{//发票号
			sbSql.append("   AND TSAN.INVOICE_NO LIKE '%" + invoiceNo + "%'\n");
		}
		if (logiName != null && !"".equals(logiName))
		{//物流公司
			sbSql.append("   AND TSL.LOGI_ID=" + logiName + "\n");
		}
		if (raiseStartDate != null && !"".equals(raiseStartDate))
		{//组板日期开始
			sbSql.append("   AND TO_CHAR(TSB.BO_DATE,'YYYY-MM-DD')>='" + raiseStartDate + "'\n");
		}
		if (raiseEndDate != null && !"".equals(raiseEndDate))
		{//组板日期结束
			sbSql.append("   AND TO_CHAR(TSB.BO_DATE,'YYYY-MM-DD')<='" + raiseEndDate + "'\n");
		}
		if (boNo != null && !"".equals(boNo))
		{//组板编号
			sbSql.append("   AND TSB.BO_NO like'%" + boNo + "%'\n");
		}
		if (allocaStartDate != null && !"".equals(allocaStartDate))
		{//配车日期开始
			sbSql.append("   AND TO_CHAR(TSB.ALLOCA_DATE,'YYYY-MM-DD')>='" + allocaStartDate + "'\n");
		}
		if (allocaEndDate != null && !"".equals(allocaEndDate))
		{//配车日期结束
			sbSql.append("   AND TO_CHAR(TSB.ALLOCA_DATE,'YYYY-MM-DD')<='" + allocaEndDate + "'\n");
		}
		if (groupCode != null && !"".equals(groupCode))
		{//物料组过滤
			sbSql.append("   AND VMGM.PACKAGE_CODE ='" + groupCode + "'\n");
		}
		if (groupCode != null && !"".equals(groupCode))
		{//物料过滤
			sbSql.append("   AND VMGM.PACKAGE_CODE ='" + groupCode + "'\n");
		}
		if (materialCode != null && !"".equals(materialCode))
		{//物料过滤
			sbSql.append("   AND VMGM.MATERIAL_CODE ='" + materialCode + "'\n");
		}
		
		sbSql.append("   ORDER BY TO_NUMBER(TSAA.AREA_NAME),TO_NUMBER(TSR.ROAD_NAME),TO_NUMBER(TSS.SIT_NAME) ASC\n");
		
		String sql = null;
		if(!"".equals(checkStatus)) {
			sql = "select TMPT.* from (\n" + sbSql.toString() + ") TMPT where TMPT.IS_PASS_STATUS =" + checkStatus;
		} else {
			sql = "select TMPT.* from (\n" + sbSql.toString() + ") TMPT where TMPT.IS_PASS_STATUS = -1 or TMPT.IS_PASS_STATUS = 0";
		}
		
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql, params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * @Title :
	 * @Description: 释放库位
	 * @param : @param vins VIN数组
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-15
	 */
	public  void releasePlace(String vin)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE TT_SALES_SIT TSS\r\n");
		sql.append("   SET TSS.VEHICLE_ID =?\r\n");
		sql.append(" WHERE TSS.VEHICLE_ID IN (SELECT TSAD.VEHICLE_ID\r\n");
		sql.append("                            FROM TT_SALES_ALLOCA_DE TSAD, TM_VEHICLE TV\r\n");
		sql.append("                           WHERE TSAD.VEHICLE_ID = TV.VEHICLE_ID AND TSAD.STATUS=?\r\n");
		sql.append("                             AND TV.VIN = ?)");
		List<Object> parms=new ArrayList<Object>();
		parms.add(Constant.DEFAULT_VALUE);
		parms.add(Constant.STATUS_ENABLE);
		parms.add(vin);
		dao.update(sql.toString(), parms);
	}
	
	/**
	 * @Title :
	 * @Description: 修改车辆状态
	 * @param :vins VIN
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-15
	 */
	public  void vehicleStatusSet(List<Object> params)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE TM_VEHICLE TV\r\n");
		sql.append("   SET TV.LIFE_CYCLE =?, TV.LOCK_STATUS = ?\r\n");
		sql.append(" WHERE TV.VIN=?");
		dao.update(sql.toString(), params);
	}
	
	/**
	 * 修改交接单号
	 * @author liufazhong
	 */
	public void vehiclePassNoSet(String vin,String passNo){
		String sql = "UPDATE TM_VEHICLE SET PASS_NO = ? WHERE VIN = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(passNo);
		params.add(vin);
		dao.update(sql, params);
	}
	
	/**
	 * 根据VIN获取发运类型
	 * @author liufazhong
	 */
	public List<Map<String, Object>> queryTransWayByVin(String vin){
		String sql = "SELECT TCD.TRANS_WAY FROM TM_VEHICLE TV LEFT JOIN TT_SALES_ALLOCA_DE TD" +
				" ON TV.VEHICLE_ID = TD.VEHICLE_ID AND TD.STATUS=10011001 LEFT JOIN TT_SALES_BO_DETAIL TBD" +
				" ON TD.BO_DE_ID=TBD.BO_DE_ID AND TBD.IS_ENABLE=10041001 LEFT JOIN TT_VS_ORDER_DETAIL TVD" +
				" ON TBD.OR_DE_ID=TVD.DETAIL_ID LEFT JOIN TT_VS_ORDER TVO" +
				" ON TVD.ORDER_ID=TVO.ORDER_ID LEFT JOIN TM_VS_ADDRESS TVA" +
				" ON TVO.DELIV_ADD_ID=TVA.ID LEFT JOIN TM_REGION TR" +
				" ON TVA.AREA_ID=TR.REGION_CODE LEFT JOIN TT_SALES_CITY_DIS TCD" +
				" ON TR.REGION_ID=TCD.CITY_ID WHERE TV.VIN = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(vin);
		return pageQuery(sql, params, getFunName());
	}
	
	/**
	 * 修改预留资源的数量
	 * @param params
	 */
	public  void Amount(List<Object> params)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE TT_VS_ORDER_RESOURCE_RESERVE T\n");
		sql.append("   SET T.AMOUNT = T.AMOUNT-1\n");
		sql.append(" WHERE T.RESERVE_ID = ? and T.AMOUNT=?"); 
		dao.update(sql.toString(), params);
	}
	
	/**
	 * 查询预留资源数据
	 * @param vin
	 * @return
	 */
	public List<Map<String ,Object>> getReserve(String vin)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT D.RESERVE_ID,D.AMOUNT\n");
		sql.append("FROM TT_SALES_ALLOCA_DE A,\n");
		sql.append("TM_VEHICLE B,\n");
		sql.append("TT_SALES_BO_DETAIL C,\n");
		sql.append("TT_VS_ORDER_RESOURCE_RESERVE D\n");
		sql.append("WHERE A.VEHICLE_ID = B.VEHICLE_ID\n");
		sql.append("AND A.BO_DE_ID = C.BO_DE_ID\n");
		sql.append("AND C.OR_DE_ID = D.ORDER_DETAIL_ID\n");
		sql.append("AND A.STATUS="+Constant.STATUS_ENABLE+"\n");
		sql.append("AND B.VIN = '"+vin+"'"); 
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;

	}
	
	/**
	 * 根据VEHICLE_ID查询
	 * @author liufazhong
	 */
	public List<Map<String ,Object>> getReserveByVehicleId(String vehicleId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT D.RESERVE_ID,D.AMOUNT\n");
		sql.append("FROM TT_SALES_ALLOCA_DE A,\n");
		sql.append("TM_VEHICLE B,\n");
		sql.append("TT_SALES_BO_DETAIL C,\n");
		sql.append("TT_VS_ORDER_RESOURCE_RESERVE D\n");
		sql.append("WHERE A.VEHICLE_ID = B.VEHICLE_ID\n");
		sql.append("AND A.BO_DE_ID = C.BO_DE_ID\n");
		sql.append("AND C.OR_DE_ID = D.ORDER_DETAIL_ID\n");
		sql.append("AND A.STATUS="+Constant.STATUS_ENABLE+"\n");
		sql.append("AND B.VEHICLE_ID = "+vehicleId+""); 
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;

	}
	
	/**
	 * @Title :
	 * @Description: 修改订单明细表已出库数量
	 * @param : param vins VIN号
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-15
	 */
	public  void updateOutAmount(String[] vins)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE TT_VS_ORDER_DETAIL TVOD\r\n");
		sql.append("   SET (TVOD.OUT_AMOUNT) =\r\n");
		sql.append("       (SELECT COUNT(1) OUT_AMOUNT\r\n");
		sql.append("          FROM TM_VEHICLE         TV,\r\n");
		sql.append("               TT_SALES_ALLOCA_DE TSAD,\r\n");
		sql.append("               TT_SALES_BO_DETAIL TSBD\r\n");
		sql.append("         WHERE TV.VEHICLE_ID = TSAD.VEHICLE_ID\r\n");
		sql.append("           AND TSAD.BO_DE_ID = TSBD.BO_DE_ID\r\n");
		sql.append("           and TSBD.OR_DE_ID = TVOD.DETAIL_ID\r\n");
		sql.append("           AND TV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_08 + "\r\n");//车辆出库
		sql.append("           AND TV.LOCK_STATUS = " + Constant.LOCK_STATUS_08 + "\r\n");//配车锁定
		sql.append("                AND TV.VIN IN ('',");
		if (vins != null && vins.length > 0)
		{
			for (int i = 0; i < vins.length; i++)
			{
				sql.append("'" + vins[i] + "',");
			}
		}
		sql.append("           '')\r\n");
		sql.append("         GROUP BY OR_DE_ID)\r\n");
		sql.append(" WHERE existS (SELECT TSBD.OR_DE_ID\r\n");
		sql.append("          FROM TM_VEHICLE         TV,\r\n");
		sql.append("               TT_SALES_ALLOCA_DE TSAD,\r\n");
		sql.append("               TT_SALES_BO_DETAIL TSBD\r\n");
		sql.append("         WHERE TV.VEHICLE_ID = TSAD.VEHICLE_ID\r\n");
		sql.append("           AND TSAD.BO_DE_ID = TSBD.BO_DE_ID\r\n");
		sql.append("           AND TVOD.DETAIL_ID = TSBD.OR_DE_ID\r\n");
		sql.append("           AND TV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_08 + "\r\n");
		sql.append("           AND TV.LOCK_STATUS = " + Constant.LOCK_STATUS_08 + "\r\n");
		sql.append("                AND TV.VIN IN ('',");
		if (vins != null && vins.length > 0)
		{
			for (int i = 0; i < vins.length; i++)
			{
				sql.append("'" + vins[i] + "',");
			}
		}
		sql.append("           ''))");
		dao.update(sql.toString(), null);
	}
	
	/**
	 * @Title :
	 * @Description: 修改分派单已出库数量
	 * @param : param vins VIN号
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-15
	 */
	public  void updateOutNum(String[] vins)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE TT_SALES_ASSIGN TSA\r\n");
		sql.append("   SET (TSA.OUT_NUM) =\r\n");
		sql.append("       (SELECT COUNT(1) OUT_NUM\r\n");
		sql.append("          FROM TM_VEHICLE         TV,\r\n");
		sql.append("               TT_SALES_ALLOCA_DE TSAD,\r\n");
		sql.append("               TT_SALES_BO_DETAIL TSBD,\r\n");
		sql.append("               TT_SALES_BOARD     TSB\r\n");
		sql.append("         WHERE TV.VEHICLE_ID = TSAD.VEHICLE_ID\r\n");
		sql.append("           AND TSAD.BO_DE_ID = TSBD.BO_DE_ID\r\n");
		sql.append("           AND TSBD.BO_ID = TSB.BO_ID\r\n");
		sql.append("           AND TSB.ASS_ID = TSA.ASS_ID\r\n");
		sql.append("                AND TV.VIN IN ('',");
		if (vins != null && vins.length > 0)
		{
			for (int i = 0; i < vins.length; i++)
			{
				sql.append("'" + vins[i] + "',");
			}
		}
		sql.append("           '')\r\n");
		sql.append("         GROUP BY TSB.ASS_ID)\r\n");
		sql.append(" WHERE EXISTS (SELECT TSB.ASS_ID\r\n");
		sql.append("          FROM TM_VEHICLE         TV,\r\n");
		sql.append("               TT_SALES_ALLOCA_DE TSAD,\r\n");
		sql.append("               TT_SALES_BO_DETAIL TSBD,\r\n");
		sql.append("               TT_SALES_BOARD     TSB\r\n");
		sql.append("         WHERE TV.VEHICLE_ID = TSAD.VEHICLE_ID\r\n");
		sql.append("           AND TSAD.BO_DE_ID = TSBD.BO_DE_ID\r\n");
		sql.append("           AND TSBD.BO_ID = TSB.BO_ID\r\n");
		sql.append("           AND TSB.ASS_ID = TSA.ASS_ID\r\n");
		sql.append("           AND TV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_08 + "\r\n");
		sql.append("           AND TV.LOCK_STATUS = " + Constant.LOCK_STATUS_08 + "\r\n");
		sql.append("                AND TV.VIN IN ('',");
		if (vins != null && vins.length > 0)
		{
			for (int i = 0; i < vins.length; i++)
			{
				sql.append("'" + vins[i] + "',");
			}
		}
		sql.append("           ''))");
		
	}
	
	/**
	 * 根据VIN号获取对应的组板ID
	 * 
	 * @param VINS
	 *            VIN号
	 * @return 组板号
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBoIdByVin(String vin) throws Exception
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TSB.BO_ID\n");
		sbSql.append("    FROM TM_VEHICLE         TV,\n");
		sbSql.append("         TT_SALES_ALLOCA_DE TSAD,\n");
		sbSql.append("         TT_SALES_BO_DETAIL TSBD,\n");
		sbSql.append("         TT_SALES_BOARD     TSB\n");
		sbSql.append("   WHERE TV.VEHICLE_ID = TSAD.VEHICLE_ID\n");
		sbSql.append("     AND TSAD.BO_DE_ID = TSBD.BO_DE_ID\n");
		sbSql.append("     AND TSBD.BO_ID = TSB.BO_ID\n");
		sbSql.append("     AND TSAD.STATUS = ?\n");
		sbSql.append(" AND TV.VIN =? ");
		List<Object> parms=new ArrayList<Object>();
		parms.add(Constant.STATUS_ENABLE);
		parms.add(vin);
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), parms, getFunName());
		return list;
	}
	
	/**
	 * @Title :
	 * @Description: 修改配车明细表出库状态
	 * @param :vin VIN号
	 * @param :userId 用户ID
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-15
	 */
	public  void updateOutStatus(String vin, Long userId)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE TT_SALES_ALLOCA_DE TSAD\n");
		sbSql.append("   SET TSAD.IS_OUT = ?, TSAD.OUT_BY = ?, TSAD.OUT_DATE = SYSDATE\n");
		sbSql.append(" WHERE TSAD.STATUS=? AND EXISTS (SELECT VEHICLE_ID\n");
		sbSql.append("          FROM TM_VEHICLE TV\n");
		sbSql.append("         WHERE TV.VEHICLE_ID = TSAD.VEHICLE_ID\n");
		sbSql.append("                AND TV.VIN =?)");
		List<Object> list = new ArrayList<Object>();
		list.add(Constant.IF_TYPE_YES);
		list.add(userId);
		list.add(Constant.STATUS_ENABLE);
		list.add(vin);
		dao.update(sbSql.toString(), list);
	}
	/**
	 * @Title :
	 * @Description: 修改配车明细表发运状态
	 * @param :vin VIN号
	 * @param :userId 用户ID
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-15
	 */
	public  void updateSendStatus(String vin, Long userId)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE TT_SALES_ALLOCA_DE TSAD\n");
		sbSql.append("   SET TSAD.IS_SEND = ?, TSAD.SEND_BY = ?, TSAD.SEND_DATE = SYSDATE\n");
		sbSql.append(" WHERE TSAD.STATUS=? AND EXISTS (SELECT VEHICLE_ID\n");
		sbSql.append("          FROM TM_VEHICLE TV\n");
		sbSql.append("         WHERE TV.VEHICLE_ID = TSAD.VEHICLE_ID\n");
		sbSql.append("                AND TV.VIN =?)");
		List<Object> list = new ArrayList<Object>();
		list.add(Constant.IF_TYPE_YES);
		list.add(userId);
		list.add(Constant.STATUS_ENABLE);
		list.add(vin);
		dao.update(sbSql.toString(), list);
	}
	/**
	 * 查看库位占用情况
	 * 
	 * @param VIN号
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSitQuery(String vin) throws Exception
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT ROAD_ID,COUNT(SIT_ID) LCOUNT\n");
		sbSql.append("  FROM TT_SALES_SIT K\n");
		sbSql.append(" WHERE ROAD_ID = (SELECT ROAD_ID\n");
		sbSql.append("                    FROM TT_SALES_SIT A\n");
		sbSql.append("                   WHERE EXISTS (SELECT *\n");
		sbSql.append("                            FROM TM_VEHICLE T\n");
		sbSql.append("                           WHERE A.VEHICLE_ID = T.VEHICLE_ID\n");
		sbSql.append("                             AND T.VIN = ?))\n");
		sbSql.append("   AND K.VEHICLE_ID != ?\n");
		sbSql.append("   AND K.STATUS = ?\n");
		sbSql.append("   GROUP BY ROAD_ID"); 
		List<Object> params = new ArrayList<Object>();
		params.add(vin);
		params.add(Constant.DEFAULT_VALUE);
		params.add(Constant.STATUS_ENABLE);
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, getFunName());
		return list;
		
	}
	
	/**
	 * 方法描述 ： 查询配成完成的的组板单据<br/>
	 * 
	 * @param hsMap
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> queryOutStorage(HashMap<String, Object> hsMap, Integer curPage,
					Integer pageSize) throws Exception
	{
		String poseId = (String) hsMap.get("poseId");
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT  -- 提车单号\n");
		sbSql.append("       TSB.BO_NO,TSB.BO_ID, -- 组板单号\n");
		sbSql.append("       TSB.BO_NUM, -- 组板数量\n");
		sbSql.append("       TSB.ALLOCA_NUM, -- 配车数量\n");
		sbSql.append("       NVL(TSB.OUT_NUM,0) OUT_NUM, -- 出库数量\n");
		sbSql.append("       TSB.BO_DATE, -- 组板完成时间\n");
		sbSql.append("		 TSB.HANDLE_STATUS");
		sbSql.append("  FROM TT_SALES_BOARD           TSB -- 组板表\n");
		sbSql.append(" WHERE TSB.BO_NUM = TSB.ALLOCA_NUM\n");
		sbSql.append("   AND TSB.BO_NUM <> 0\n");
		sbSql.append("	 AND TSB.HANDLE_STATUS IN (" + Constant.HANDLE_STATUS03 + "," + Constant.HANDLE_STATUS04 + ") \n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TSB.AREA_ID", poseId));//车厂端查询列表产地数据权限
		/****************************** 页面查询字段start **************************/
		String yieldly = (String) hsMap.get("yieldly"); // 产地		
		String raiseStartDate = (String) hsMap.get("boStartDate"); // 组板日期开始
		String raiseEndDate = (String) hsMap.get("boEndDate"); // 组板日期结束
		String boNo = (String) hsMap.get("boNo"); //组板编号
		/****************************** 页面查询字段end ***************************/
		List<Object> params = new ArrayList<Object>();
		
		if (yieldly != null && !"".equals(yieldly))
		{//产地
			sbSql.append("   AND TSB.AREA_ID = " + yieldly + "\n");
		}
		if (raiseStartDate != null && !"".equals(raiseStartDate))
		{//组板日期开始
			sbSql.append("   AND TO_CHAR(TSB.BO_DATE,'YYYY-MM-DD')>='" + raiseStartDate + "'\n");
		}
		if (raiseEndDate != null && !"".equals(raiseEndDate))
		{//组板日期结束
			sbSql.append("   AND TO_CHAR(TSB.BO_DATE,'YYYY-MM-DD')<='" + raiseEndDate + "'\n");
		}
		if (boNo != null && !"".equals(boNo))
		{//组板编号
			sbSql.append("   AND TSB.BO_NO like'%" + boNo + "%'\n");
		}
		return dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}
	/**
	 * //获取发运的方式
	 * 
	 * @param materialId
	 * @param yieldly
	 * @param fantype
	 * @return
	 */
	public Map<String,Object> getSendType(List<Object> param) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT C.SEND_TYPE\n");
		sbSql.append("    FROM TM_VEHICLE A, TT_SALES_ALLOCA_DE B, TT_SALES_BO_DETAIL C\n");
		sbSql.append("   WHERE A.VEHICLE_ID = B.VEHICLE_ID\n");
		sbSql.append("     AND B.BO_DE_ID = C.BO_DE_ID\n");
		sbSql.append("     AND B.STATUS=10011001\n");
		sbSql.append("     AND A.VIN = ?"); 
		Map<String, Object> map=dao.pageQueryMap(sbSql.toString(),param,getFunName());
		return map;
	}
	
	/**
	 * PDI检查历史检查记录查询
	 * @param vin
	 * @param curPage
	 * @param pageSizeMax
	 * @return
	 */
	public PageResult<Map<String, Object>> pdiCheckHistoryRecordQuery(
			String vin, Integer curPage, Integer pageSizeMax) {
		StringBuilder sbSql = new StringBuilder(100);
		sbSql.append("select t.vin, t.record, t.create_date, a.name\n");
		sbSql.append("  from TT_VEHICLE_PDI_CHECK t, TC_USER a\n");
		sbSql.append(" where t.vin = '"+ vin +"'\n");
		sbSql.append("   and t.create_by = a.user_id\n"); 
		sbSql.append("   order by t.create_date desc"); 
		return dao.pageQuery(sbSql.toString(), null, getFunName(), pageSizeMax, curPage);
	}
	
	public PageResult<Map<String, Object>> removelStorageQuery(
			Map<String,Object> hsMap, Integer curPage, Integer pageSizeMax) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSB.BO_DATE, -- 组板完成时间\n" );
		sql.append("       TM.VIN,\n" );
		sql.append("       TSB.BO_NO,\n" );
		sql.append("       FT.CODE_DESC FUND_TYPE,\n" );
		sql.append("       VOD.DISCOUNT_S_PRICE,\n" );
		sql.append("       BD.ORDER_ID,\n" );
		sql.append("       BD.ORDER_NO,\n" );
		sql.append("       TD.DEALER_SHORTNAME,\n" );
		sql.append("       MAT.SERIES_NAME,\n" );
		sql.append("       MAT.MATERIAL_NAME,\n" );
		sql.append("       MAT.MODEL_NAME,\n" );
		sql.append("       MAT.PACKAGE_NAME,\n" );
		sql.append("       MAT.COLOR_NAME,\n" );
		sql.append("       MAT.MATERIAL_CODE,\n" );
		sql.append("       TO_CHAR(AD.OUT_DATE, 'yyyy-mm-dd hh24:mi') OUT_DATE,\n" );
		sql.append("       F_GET_TCCODE_DESC(TM.LIFE_CYCLE) LIFE_CYCLE_DESC,\n" );
		sql.append("       TM.LIFE_CYCLE\n" );
		sql.append("  FROM TT_SALES_BOARD        TSB,\n" );
		sql.append("       TT_SALES_BO_DETAIL    BD,\n" );
		sql.append("       TT_SALES_ALLOCA_DE    AD,\n" );
		sql.append("       TM_VEHICLE            TM,\n" );
		sql.append("       TM_DEALER             TD,\n" );
		sql.append("       TT_VS_ORDER_DETAIL    VOD,\n" );
		sql.append("       TT_VS_ORDER           VO,\n" );
		sql.append("       TC_CODE               FT,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT MAT -- 组板表\n" );
		sql.append(" WHERE TSB.BO_ID = BD.BO_ID\n" );
		sql.append("   AND BD.BO_DE_ID = AD.BO_DE_ID\n" );
		sql.append("   AND BD.OR_DE_ID = VOD.DETAIL_ID\n" );
		sql.append("   AND VOD.ORDER_ID = VO.ORDER_ID\n" );
		sql.append("   AND VO.FUND_TYPE_ID = FT.CODE_ID\n" );
		sql.append("   AND AD.VEHICLE_ID = TM.VEHICLE_ID\n" );
		sql.append("   AND TM.MATERIAL_ID = MAT.MATERIAL_ID\n" );
		sql.append("   AND BD.DEALER_ID = TD.DEALER_ID\n" );
		sql.append("   AND TSB.BO_NUM = TSB.ALLOCA_NUM\n" );
		sql.append("   AND TSB.BO_NUM <> 0\n" );
		sql.append("   AND AD.IS_OUT = 10041001\n" );
		sql.append("   AND EXISTS (SELECT 1 FROM TM_POSE_BUSINESS_AREA TPBA, TM_BUSINESS_AREA TBA\n" );
		sql.append("         WHERE TPBA.AREA_ID = TBA.AREA_ID");
		sql.append("   AND TBA.STATUS = "+Constant.STATUS_ENABLE+"\n");
		String posId = (String) hsMap.get("poseId"); // 
		sql.append("   AND TPBA.POSE_ID = "+posId+" AND TBA.AREA_ID=TSB.AREA_ID)\n");
		
		
		String yieldly = (String) hsMap.get("yieldly"); // 产地		
		String raiseStartDate = (String) hsMap.get("boStartDate"); // 组板日期开始
		String raiseEndDate = (String) hsMap.get("boEndDate"); // 组板日期结束
		
		String outSDate = (String) hsMap.get("outSDate"); // 出库日期开始
		String outEDate = (String) hsMap.get("outEDate"); // 出库日期结束
		
		String boNo = (String) hsMap.get("boNo"); //组板编号
		String vin = (String) hsMap.get("VIN");
		String orderNo = (String) hsMap.get("orderNo");
		String dealerName = (String) hsMap.get("dealerName");
		
		String fundType = (String) hsMap.get("fundType"); // 资金类型	
		
		if (yieldly != null && !"".equals(yieldly))
		{//产地
			sql.append("   AND TSB.AREA_ID = " + yieldly + "\n");
		}
		if (raiseStartDate != null && !"".equals(raiseStartDate))
		{//组板日期开始
			sql.append("   AND TO_CHAR(TSB.BO_DATE,'YYYY-MM-DD')>='" + raiseStartDate + "'\n");
		}
		if (raiseEndDate != null && !"".equals(raiseEndDate))
		{//组板日期结束
			sql.append("   AND TO_CHAR(TSB.BO_DATE,'YYYY-MM-DD')<='" + raiseEndDate + "'\n");
		}
		if (outSDate != null && !"".equals(outSDate))
		{//出库日期开始
			sql.append("   AND TO_CHAR(ad.out_DATE,'YYYY-MM-DD')>='" + outSDate + "'\n");
		}
		if (outEDate != null && !"".equals(outEDate))
		{//出库日期结束
			sql.append("   AND TO_CHAR(ad.out_DATE,'YYYY-MM-DD')<='" + outEDate + "'\n");
		}
		
		if (boNo != null && !"".equals(boNo))
		{//组板编号
			sql.append("   AND TSB.BO_NO like'%" + boNo + "%'\n");
		}
		if (orderNo != null && !"".equals(orderNo))
		{//销售清单号
			sql.append("   AND BD.ORDER_NO like'%" + orderNo + "%'\n");
		}
		if (dealerName != null && !"".equals(dealerName))
		{//经销商名称
			sql.append("   AND td.dealer_shortname like'%" + dealerName + "%'\n");
		}
		if ( fundType != null && !"".equals(fundType))
		{//资金类型
			sql.append("   AND VO.FUND_TYPE_ID = " + fundType + "\n");
		}
		
		if(vin != null && !"".equals(vin)){
			sql.append("  AND TM.VIN like'%" + vin + "%'\n");
		}
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSizeMax, curPage);
	}
	
	
}
