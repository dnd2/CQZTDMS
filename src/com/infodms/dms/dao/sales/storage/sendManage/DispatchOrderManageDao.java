package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 调拨订单管理
 * @author shuyh
 * 2017/7/18
 */
public class DispatchOrderManageDao  extends BaseDao<PO>{
	private static final DispatchOrderManageDao dao = new DispatchOrderManageDao ();
	public static final DispatchOrderManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * 调拨订单查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getBatchOrderQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		Object[] obj=getSQL(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 导出EXCEL
	 * @param params 查询参数LIST
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBatchOrderQueryExport(Map<String, Object> map){
		Object[] obj=getSQL(map);
		List<Map<String, Object>> list= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName());
		return list;
	}
	/**
	 * 调拨订单查询sql
	 * @param map
	 * @return
	 */
	public Object[] getSQL(Map<String, Object> map){
		/******************************页面查询字段start**************************/
		String dispNo = CommonUtils.checkNull(map.get("dispNo")); //销售清单号
		String sendWareId = CommonUtils.checkNull(map.get("sendWareId")); //发运仓库ID
		String receiveWareId = CommonUtils.checkNull(map.get("receiveWareId")); //收货仓库ID
		String lastStartdate = CommonUtils.checkNull(map.get("lastStartdate")); //最晚到货日期开始
		String lastEndDate = CommonUtils.checkNull(map.get("lastEndDate")); // 最晚到货日期结束
		String transType = CommonUtils.checkNull(map.get("transType")); // 发运方式
		String subStartdate = CommonUtils.checkNull(map.get("subStartdate")); //提报日期开始
		String subEndDate = CommonUtils.checkNull(map.get("subEndDate")); // 提报日期结束
		String orderStatus = CommonUtils.checkNull(map.get("orderStatus")); //订单状态
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select TSO.DISP_ID,\n" );
		sql.append("       TSO.DISP_NO, --订单号\n" );
		sql.append("	   TSO.SEND_WAREHOUSE,--发运仓库\n" );
		sql.append("      TSO.RECEIVE_WAREHOUSE,--收货仓库\n");
		sql.append("       TO_CHAR(TSO.PLAN_DELIVER_DATE, 'YYYY-MM-DD') PLAN_DELIVER_DATE, --最晚到货日期\n" );
		sql.append("       TSO.SEND_WAY,\n" );
		sql.append("       (SELECT TC.CODE_DESC FROM TC_CODE TC WHERE TC.CODE_ID = TSO.SEND_WAY) SEND_WAY_TXT, --发运方式\n" );
		sql.append("       TO_CHAR(TSO.SUB_DATE, 'YYYY-MM-DD') SUB_DATE, --提交日期\n" );
		sql.append("       NVL(tso.SUB_NUM, 0) SUB_NUM, --提报数量\n" );
		sql.append("       NVL(TSO.FP_NUM, 0) ASS_NUM, --分派数量\n" );
		sql.append("       NVL(tso.BO_NUM, 0) BO_NUM, --组板数量\n" );
		sql.append("       NVL(tso.REP_NUM, 0) REP_NUM, --交接数量\n" );
		//sql.append("       NVL(TSO.ACC_NUM, 0) - NVL(TSO.SEND_NUM, 0) as IN_WAY_NUM, --在途数量\n" );
		sql.append("	CASE\n" );
		sql.append("         WHEN NVL(TSO.REP_NUM, 0) - NVL(TSO.ACC_NUM, 0) < 0 THEN\n" );
		sql.append("          0\n" );
		sql.append("         ELSE\n" );
		sql.append("          NVL(TSO.REP_NUM, 0) - NVL(TSO.ACC_NUM, 0)\n" );
		sql.append("       END AS IN_WAY_NUM, --在途数量\n");

		sql.append("       NVL(TSO.ACC_NUM, 0) ACC_NUM, --验收数量\n" );
		sql.append("       TSO.ORDER_STATUS,\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSO.ORDER_STATUS) ORDER_STATUS_TXT--状态\n" );
		sql.append("  from tt_vs_dispatch_order tso\n" );
		sql.append("  WHERE 1=1");
		if(!dispNo.equals("")&&null!=dispNo){
			sql.append("AND TSO.DISP_NO like ?\n");
			params.add('%'+dispNo+'%');
		}
		if(!sendWareId.equals("")&&null!=sendWareId){
			sql.append("AND TSO.SEND_WARE_ID=?\n");
			params.add(sendWareId);
		}
		if(!receiveWareId.equals("")&&null!=receiveWareId){
			sql.append("AND TSO.RECEIVE_WARE_ID=?\n");
			params.add(receiveWareId);
		}
		if(!lastStartdate.equals("")&&null!=lastStartdate){
			sql.append("AND TSO.PLAN_DELIVER_DATE>=TO_DATE('"+lastStartdate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n"); 
		}
		if(!lastEndDate.equals("")&&null!=lastEndDate){
			sql.append("AND TSO.PLAN_DELIVER_DATE<=TO_DATE('"+lastEndDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n"); 
		}
		if(!transType.equals("")&&null!=transType){
			sql.append("AND TSO.SEND_WAY=?\n");
			params.add(transType);
		}
		if(!subStartdate.equals("")&&null!=subStartdate){
			sql.append("AND TSO.SUB_DATE>=TO_DATE('"+subStartdate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n"); 
		}
		if(!subEndDate.equals("")&&null!=subEndDate){
			sql.append("AND TSO.SUB_DATE<=TO_DATE('"+subEndDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n"); 
		}
		if(!orderStatus.equals("")&&null!=orderStatus){
			sql.append("AND TSO.ORDER_STATUS=?\n");
			params.add(orderStatus);
		}
		sql.append("  ORDER BY TSO.SUB_DATE DESC\n");
		Object[] arr=new Object[2];
		arr[0]=sql;
		arr[1]=params;
		return arr;
	}
	
	/**
	 * 拼接字符串
	 * @param dealerCode
	 * @return
	 */
	public String getDealerCodeStr(String dealerCode){
		StringBuffer buffer = new StringBuffer();
		if (null != dealerCode && !"".equals(dealerCode))
		{
			String[] dealerCodes = dealerCode.split(",");
			if (null != dealerCodes && dealerCodes.length > 0)
			{
				for (int i = 0; i < dealerCodes.length; i++)
				{
					buffer.append("'").append(dealerCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
			}
		}
		return buffer.toString();
	}
	/**
	 * 根据订单ID查询主要信息
	 * @param orderId
	 * @return
	 */
	public Map<String,Object> getOrderMainInfo(String orderId){
		StringBuffer sql= new StringBuffer();
		sql.append("select\n" );
		sql.append("        TSD.*,\n" );
		sql.append("       TO_CHAR(TSD.PLAN_DELIVER_DATE, 'YYYY-MM-DD') PLAN_DELIVER_DATE2,\n" );
		sql.append("       (SELECT TC.CODE_DESC FROM TC_CODE TC WHERE TC.CODE_ID = TSD.SEND_WAY) SEND_WAY_TXT\n" );
		sql.append("  from TT_VS_DISPATCH_ORDER TSD\n" );
		sql.append("	 WHERE TSD.DISP_ID="+orderId+"\n");
		Map<String, Object> mapR= dao.pageQueryMap(sql.toString(), null, getFunName());
		return mapR;
	}
	/**
	 * 根据物料ID和发运仓库查询库存量
	 * @param materialId
	 * @param sendWareId
	 * @return
	 */
	public Map<String,Object> getMatCountByWhId(String materialId,String sendWareId){
		
		StringBuffer sql= new StringBuffer();
//		sql.append("SELECT nvl(SUM(sj_total), 0) WARE_NUM\n" );
//		sql.append("     FROM (SELECT tv.material_id,\n" );
//		sql.append("                  tv.warehouse_id,\n" );
//		sql.append("                  nvl(COUNT(tv.vehicle_id), 0) sj_total\n" );
//		sql.append("             FROM tm_vehicle tv\n" );
//		sql.append("            WHERE tv.life_cycle = 10321002\n" );
//		sql.append("              AND tv.lock_status = 10241001\n" );
//		sql.append("            GROUP BY tv.material_id, tv.warehouse_id\n" );
//		sql.append("           UNION ALL\n" );
//		sql.append("           SELECT tvdd.material_id,\n" );
//		sql.append("                  tvd.send_ware_id,\n" );
//		sql.append("                  nvl(SUM(tvdd.order_amount), 0) * (-1)\n" );
//		sql.append("             FROM tt_vs_dispatch_order tvd, tt_vs_dispatch_order_dtl tvdd\n" );
//		sql.append("            WHERE tvd.disp_id = tvdd.disp_id\n" );
//		sql.append("              AND tvd.order_status = 12141002\n" );
//		sql.append("            GROUP BY tvdd.material_id, tvd.send_ware_id) tt\n" );
//		sql.append("    where tt.material_id = "+materialId+"\n" );
//		sql.append("      AND tt.WAREHOUSE_ID ="+sendWareId+"\n");
		sql.append("SELECT VWS.WAREHOUSE_ID, VWS.MATERIAL_ID, nvl(VWS.STOCK_AMOUNT, 0)-nvl(VWS.LOCK_AMOUT, 0) WARE_NUM\n" );
		sql.append("  FROM VW_VS_RESOURCE_ENTITY_WEEK_NEW VWS\n" );
		sql.append(" WHERE VWS.WAREHOUSE_ID ="+sendWareId+"\n");
		sql.append("   AND VWS.MATERIAL_ID ="+materialId+"\n");

		Map<String, Object> mapR= dao.pageQueryMap(sql.toString(), null, getFunName());
		return mapR;
	}
	/**
	 * 根据物料ID和发运仓库获取在库的车辆列表
	 * @param materialId
	 * @param sendWareId
	 * @return
	 */
//	public List<Map<String, Object>> getVhclInStoreByWhId(String materialId,String sendWareId){
//		StringBuffer sql= new StringBuffer();
//		sql.append("select tv.vehicle_id\n" );
//		sql.append("  from tm_vehicle tv\n" );
//		sql.append(" where tv.material_id = "+materialId+"\n" );
//		sql.append("   AND TV.WAREHOUSE_ID = "+sendWareId+"\n" );
//		sql.append("   AND TV.LIFE_CYCLE = 10321002 --在库");
//		
//		return dao.pageQuery(sql.toString(), null, getFunName());
//	}
	/**
	 * 根据ID获取地址信息
	 * @param id
	 * @return
	 */
	public Map<String,Object> getAddrInfoById(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("select * from tm_vs_address tt where tt.id = "+id+"\n" );
		Map<String, Object> mapR= dao.pageQueryMap(sql.toString(), null, getFunName());
		return mapR;
	}
	/**
	 * 获取订单明细数据
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBatchOrderDel(String orderId) throws Exception {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT tvod.detail_id,\n" );
		sql.append("       nvl(tvod.order_amount, 0) order_amount,\n" );
		sql.append("       tvod.material_id,\n" );
		sql.append("       vmt.material_code, --物料代码\n" );
		sql.append("       vmt.material_name,\n" );
		sql.append("       vmt.color_code,\n" );
		sql.append("       vmt.color_name, --颜色\n" );
		sql.append("       vmt.series_id,\n" );
		sql.append("       vmt.series_code,\n" );
		sql.append("       vmt.series_name, --车系名称\n" );
		sql.append("       vmt.model_id,\n" );
		sql.append("       vmt.model_code,\n" );
		sql.append("       vmt.model_name, --车型名称\n" );
		sql.append("       vmt.package_id,\n" );
		sql.append("       vmt.package_name, --配置名称\n" );
		sql.append("       vmt.package_code,\n" );
		sql.append("       nvl(stkk.stock_amount, 0)-nvl(stkk.LOCK_AMOUT, 0) WARE_NUM\n" );
		sql.append("  FROM tt_vs_dispatch_order_dtl tvod\n" );
		sql.append("  JOIN vw_material_group_mat vmt ON tvod.material_id = vmt.material_id\n" );
		sql.append("  LEFT JOIN VW_VS_RESOURCE_ENTITY_WEEK_NEW stkk ON stkk.material_id =\n" );
		sql.append("                                                   tvod.material_id\n" );
		sql.append("                                               AND stkk.warehouse_id =\n" );
		sql.append("                                                   tvod.send_ware_id\n");
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TVOD.DISP_ID = "+orderId+"\n");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 根据收货仓库获取地址列表
	 * @param warehouseId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAddrByReceiveWrList(String warehouseId) throws Exception {
		
		StringBuffer sql= new StringBuffer();
//		sql.append("select TVA.*\n" );
//		sql.append("  from TM_WAREHOUSE TW, TM_VS_ADDRESS TVA\n" );
//		sql.append(" WHERE TW.PROV_CODE = TVA.PROVINCE_ID\n" );
//		sql.append("   AND TW.CITY_CODE = TVA.CITY_ID\n" );
//		sql.append("   AND TW.COUNTY_CODE = TVA.AREA_ID\n" );
//		sql.append("   AND TW.WAREHOUSE_ID="+warehouseId+"\n");
		sql.append("   select * from TM_WAREHOUSE TW where TW.WAREHOUSE_ID="+warehouseId+"\n");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 根据收货地址和发运仓库获取城市里程信息
	 * @param warehouseId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getPlanDateInfo(String warehouseId,String addrId) throws Exception {
		
//		StringBuffer sql= new StringBuffer();
//		sql.append("SELECT TSD.*\n" );
//		sql.append("  FROM TT_SALES_CITY_DIS TSD\n" );
//		sql.append(" INNER JOIN tm_vs_address tva ON TVA.PROVINCE_ID = TSD.PROVINCE_ID\n" );
//		sql.append("                             AND TVA.AREA_ID = TSD.CITY_ID\n" );
//		sql.append(" WHERE TSD.YIELDLY = "+warehouseId+"\n" );
//		sql.append("   and tva.id = "+addrId+"\n");
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSD.*\n" );
		sql.append("   FROM TT_SALES_CITY_DIS TSD\n" );
		sql.append("  INNER JOIN tm_warehouse tva ON TVA.Prov_Code = TSD.PROVINCE_ID\n" );
		sql.append("                             AND TVA.COUNTY_CODE = TSD.CITY_ID\n" );
		sql.append("  WHERE TSD.YIELDLY = "+warehouseId+"\n" );
		sql.append("    and tva.WAREHOUSE_ID = "+addrId+"\n");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	/***************************************************************************
	 * 车厂端查询调拨入库的车辆列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */

	public static List<Map<String,Object>> getCheckLists(String poseId,String dealer_Id,String vin, String dlvNo,String dlvryNo, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("select TMV.VEHICLE_ID,                                                           \n");
		sql.append("       tvd.req_id,                                                               \n");
		sql.append("       tvd.req_no,                                                               \n");
		sql.append("       G.GROUP_NAME, --车型名称                                                  \n");
		sql.append("       TMVM.MATERIAL_CODE, --物料代码                                            \n");
		sql.append("       TMVM.MATERIAL_NAME, --物料名称                                            \n");
		sql.append("       --TVD.RECEIVER, --接收方                                                  \n");
		sql.append("       TW.WAREHOUSE_ID DLV_WARE_ID, --发运仓库  id                             \n");
		sql.append("       SH.WAREHOUSE_ID REC_WARE_ID, --接收仓库ID                               \n");
		sql.append("       TW.WAREHOUSE_NAME DLV_WARE_NAME, --发运仓库                               \n");
		sql.append("       SH.WAREHOUSE_NAME REC_WARE_NAME, --接收仓库                               \n");
		sql.append("       TMV.VIN, --VIN                                                            \n");
		sql.append("       TMV.ENGINE_NO, --发动机号                                                 \n");
		sql.append("       -- TVD.DLVRY_REQ_NO,                                                      \n");
		sql.append("       TSL.LOGI_NAME,\n");
		sql.append("       TSB.DRIVER_NAME,                                                          \n");
		sql.append("       TSB.DRIVER_TEL,                                                           \n");
		sql.append("       (select TC.CODE_DESC                                                      \n");
		sql.append("          from tc_code tc                                                        \n");
		sql.append("         where tc.code_id = tvd.dlv_ship_type) SHIP_NAME, --发运方式             \n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE, 'YYYY-MM-DD') as PRODUCT_DATE, --生产日期       \n");
		sql.append("       TO_CHAR(TMV.FACTORY_DATE, 'YYYY-MM-DD') as FACTORY_DATE, --出厂日期       \n");
		sql.append("       to_char(TSB.PLAN_LOAD_DATE, 'yyyy-mm-dd') DELIVERY_DATE, --计划装车日期  \n");
		sql.append("       to_char(TVD.DLV_FY_DATE, 'yyyy-mm-dd') DLV_FY_DATE, --最晚发运日期        \n");
		sql.append("       to_char(TVD.DLV_JJ_DATE, 'yyyy-mm-dd') ARRIVE_DATE, --最晚到货日期        \n");
		sql.append("       DECODE(SIGN(TRUNC(TVD.DLV_JJ_DATE, 'DD') - (SYSDATE)), -1, 1, 0) AS IS_RED\n");
		sql.append("  from tt_vs_dlvry            tvd,                 \n");
		sql.append("       tt_vs_dlvry_dtl        tvdd,                \n");
		sql.append("       Tt_Sales_Waybill       tsw,                 \n");
		sql.append("       tt_sales_way_bill_dtl  tswb,                \n");
		sql.append("       VW_MATERIAL_GROUP_MAT  VMGM,                \n");
		sql.append("       TT_SALES_BOARD         TSB, --组板主表      \n");
		sql.append("       TT_SALES_BO_DETAIL     TSBD, --组板明细表   \n");
		sql.append("       TT_SALES_LOGI          TSL,                 \n");
		sql.append("       TM_VEHICLE             TMV,                 \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,                   \n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,                \n");
		sql.append("       TM_WAREHOUSE           TW,                  \n");
		sql.append("       TM_WAREHOUSE           SH                   \n");
		sql.append(" where tvd.req_id = tvdd.req_id                    \n");
		sql.append("   and tswb.mat_id = tvdd.material_id              \n");
		sql.append("   AND TSW.BILL_ID = TSBD.BILL_ID                  \n");
		sql.append("   AND TSB.BO_ID = TSBD.BO_ID                      \n");
		sql.append("   AND TSBD.ORDER_ID = TVD.ORD_ID                  \n");
		sql.append("   AND TSBD.OR_DE_ID = TVDD.ORD_DETAIL_ID          \n");
		sql.append("   AND TSBD.MAT_ID = TVDD.MATERIAL_ID              \n");
		sql.append("   AND VMGM.MATERIAL_ID = TSWB.MAT_ID              \n");
		sql.append("   AND TVD.DLV_LOGI_ID = TSL.LOGI_ID(+)            \n");
		sql.append("   and TMV.MODEL_ID = G.GROUP_ID                   \n");
		sql.append("   and TMV.MATERIAL_ID = TMVM.MATERIAL_ID          \n");
		sql.append("   and TMV.VEHICLE_ID = tswb.vehicle_id            \n");
		sql.append("   AND TVD.DLV_REC_WH_ID = SH.WAREHOUSE_ID(+)      \n");
		sql.append("   AND TVD.DLV_WH_ID = TW.WAREHOUSE_ID(+)          \n");
		sql.append("   AND TMV.LIFE_CYCLE =  "+Constant.VEHICLE_LIFE_08+"\n");
		sql.append("   AND TVD.Dlv_Type = "+Constant.DELIVERY_ORD_TYPE_ALLOCAT+"\n");
		//sql.append("	and TVDR.receiver IN ("+dealer_Id+")\n") ;
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != dlvNo && !"".equals(dlvNo)) {
			sql.append("   AND  tvd.req_no like UPPER('%" + dlvNo + "%') \n");
		}

		if (null != dlvryNo && !"".equals(dlvryNo)) {
			sql.append("   AND  tvd.req_no like UPPER('%" + dlvryNo + "%') \n");
		}
		sql.append("ORDER BY TVD.DLV_FY_DATE DESC, TVD.REQ_ID ASC") ;
		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
public static PageResult <Map<String,Object>> getCheckList(String poseId,String dealer_Id,String vin, String dlvNo,String dlvryNo, int pageSize,int curPage){
		
		StringBuffer sql = new StringBuffer("");
		sql.append("select TMV.VEHICLE_ID,                                                           \n");
		sql.append("       tvd.req_id,                                                               \n");
		sql.append("       tvd.req_no,                                                               \n");
		sql.append("       G.GROUP_NAME, --车型名称                                                  \n");
		sql.append("       TMVM.MATERIAL_CODE, --物料代码                                            \n");
		sql.append("       TMVM.MATERIAL_NAME, --物料名称                                            \n");
		sql.append("       --TVD.RECEIVER, --接收方                                                  \n");
		sql.append("       TW.WAREHOUSE_ID DLV_WARE_ID, --发运仓库  id                             \n");
		sql.append("       SH.WAREHOUSE_ID REC_WARE_ID, --接收仓库ID                               \n");
		sql.append("       TW.WAREHOUSE_NAME DLV_WARE_NAME, --发运仓库                               \n");
		sql.append("       SH.WAREHOUSE_NAME REC_WARE_NAME, --接收仓库                               \n");
		sql.append("       TMV.VIN, --VIN                                                            \n");
		sql.append("       TMV.ENGINE_NO, --发动机号                                                 \n");
		sql.append("       -- TVD.DLVRY_REQ_NO,                                                      \n");
		sql.append("       TSL.LOGI_NAME,\n");
		sql.append("       TSB.DRIVER_NAME,                                                          \n");
		sql.append("       TSB.DRIVER_TEL,                                                           \n");
		sql.append("       (select TC.CODE_DESC                                                      \n");
		sql.append("          from tc_code tc                                                        \n");
		sql.append("         where tc.code_id = tvd.dlv_ship_type) SHIP_NAME, --发运方式             \n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE, 'YYYY-MM-DD') as PRODUCT_DATE, --生产日期       \n");
		sql.append("       TO_CHAR(TMV.FACTORY_DATE, 'YYYY-MM-DD') as FACTORY_DATE, --出厂日期       \n");
		sql.append("       to_char(TSB.PLAN_LOAD_DATE, 'yyyy-mm-dd') DELIVERY_DATE, --计划装车日期  \n");
		sql.append("       to_char(TVD.DLV_FY_DATE, 'yyyy-mm-dd') DLV_FY_DATE, --最晚发运日期        \n");
		sql.append("       to_char(TVD.DLV_JJ_DATE, 'yyyy-mm-dd') ARRIVE_DATE, --最晚到货日期        \n");
		sql.append("       DECODE(SIGN(TRUNC(TVD.DLV_JJ_DATE, 'DD') - (SYSDATE)), -1, 1, 0) AS IS_RED\n");
		sql.append("  from tt_vs_dlvry            tvd,                 \n");
		sql.append("       tt_vs_dlvry_dtl        tvdd,                \n");
		sql.append("       Tt_Sales_Waybill       tsw,                 \n");
		sql.append("       tt_sales_way_bill_dtl  tswb,                \n");
		sql.append("       VW_MATERIAL_GROUP_MAT  VMGM,                \n");
		sql.append("       TT_SALES_BOARD         TSB, --组板主表      \n");
		sql.append("       TT_SALES_BO_DETAIL     TSBD, --组板明细表   \n");
		sql.append("       TT_SALES_LOGI          TSL,                 \n");
		sql.append("       TM_VEHICLE             TMV,                 \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,                   \n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,                \n");
		sql.append("       TM_WAREHOUSE           TW,                  \n");
		sql.append("       TM_WAREHOUSE           SH                   \n");
		sql.append(" where tvd.req_id = tvdd.req_id                    \n");
		sql.append("   and tswb.mat_id = tvdd.material_id              \n");
		sql.append("   AND TSW.BILL_ID = TSBD.BILL_ID                  \n");
		sql.append("   AND TSB.BO_ID = TSBD.BO_ID                      \n");
		sql.append("   AND TSBD.ORDER_ID = TVD.ORD_ID                  \n");
		sql.append("   AND TSBD.OR_DE_ID = TVDD.ORD_DETAIL_ID          \n");
		sql.append("   AND TSBD.MAT_ID = TVDD.MATERIAL_ID              \n");
		sql.append("   AND VMGM.MATERIAL_ID = TSWB.MAT_ID              \n");
		sql.append("   AND TVD.DLV_LOGI_ID = TSL.LOGI_ID(+)            \n");
		sql.append("   and TMV.MODEL_ID = G.GROUP_ID                   \n");
		sql.append("   and TMV.MATERIAL_ID = TMVM.MATERIAL_ID          \n");
		sql.append("   and TMV.VEHICLE_ID = tswb.vehicle_id            \n");
		sql.append("   AND TVD.DLV_REC_WH_ID = SH.WAREHOUSE_ID(+)      \n");
		sql.append("   AND TVD.DLV_WH_ID = TW.WAREHOUSE_ID(+)          \n");
		sql.append("   AND TMV.LIFE_CYCLE =  "+Constant.VEHICLE_LIFE_08+"\n");
		sql.append("   AND TVD.Dlv_Type = "+Constant.DELIVERY_ORD_TYPE_ALLOCAT+"\n");
		//sql.append("	and TVDR.receiver IN ("+dealer_Id+")\n") ;
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != dlvNo && !"".equals(dlvNo)) {
			sql.append("   AND  tvd.req_no like UPPER('%" + dlvNo + "%') \n");
		}

		if (null != dlvryNo && !"".equals(dlvryNo)) {
			sql.append("   AND  tvd.req_no like UPPER('%" + dlvryNo + "%') \n");
		}
		sql.append("ORDER BY TVD.DLV_FY_DATE DESC, TVD.REQ_ID ASC") ;
		return dao.pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO.getCheckList", pageSize, curPage);
	}

		/**
		 * 获取仓库
		 * @param dealerId
		 * @return
		 */
		public List<Map<String,Object>> getWarehouseList(){
			//String sql = "SELECT rebate_discount,rebate_amount FROM tt_vs_account_rebate WHERE Dealer_Id="+dealerId;
			String sql="select t.warehouse_id,t.warehouse_code,t.warehouse_name from tm_warehouse t where status="+Constant.STATUS_ENABLE;
			List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());// dao.select(TmDealerPriceRelationPO.class, sql,null);
			return list;
		}
		
		
		public static Map<String,Object> getCheckListByVehicleId(String veHicleId){
			
			StringBuffer sql = new StringBuffer("");
			sql.append("select TMV.VEHICLE_ID,                                                           \n");
			sql.append("       tvd.req_id,                                                               \n");
			sql.append("       tvd.req_no,                                                               \n");
			sql.append("       G.GROUP_NAME AS MODEL_NAME, --车型名称                                                  \n");
			sql.append(" 	   TMV.COLOR,\n");
			sql.append("       TMVM.MATERIAL_CODE, --物料代码                                            \n");
			sql.append("       TMVM.MATERIAL_NAME, --物料名称                                            \n");
			sql.append("       --TVD.RECEIVER, --接收方                                                  \n");
			sql.append("       TW.WAREHOUSE_ID DLV_WARE_ID, --发运仓库  id                             \n");
			sql.append("       SH.WAREHOUSE_ID REC_WARE_ID, --接收仓库ID                               \n");
			sql.append("       TW.WAREHOUSE_NAME DLV_WARE_NAME, --发运仓库                               \n");
			sql.append("       SH.WAREHOUSE_NAME REC_WARE_NAME, --接收仓库                               \n");
			sql.append("       TMV.VIN, --VIN                                                            \n");
			sql.append("       TMV.ENGINE_NO, --发动机号                                                 \n");
			sql.append("       -- TVD.DLVRY_REQ_NO,                                                      \n");
			sql.append("       TSL.LOGI_NAME,\n");
			sql.append("       TSB.DRIVER_NAME,                                                          \n");
			sql.append("       TSB.DRIVER_TEL,                                                           \n");
			sql.append("       (select TC.CODE_DESC                                                      \n");
			sql.append("          from tc_code tc                                                        \n");
			sql.append("         where tc.code_id = tvd.dlv_ship_type) SHIP_NAME, --发运方式             \n");
			sql.append("       TO_CHAR(TMV.PRODUCT_DATE, 'YYYY-MM-DD') as PRODUCT_DATE, --生产日期       \n");
			sql.append("       TO_CHAR(TMV.FACTORY_DATE, 'YYYY-MM-DD') as FACTORY_DATE, --出厂日期       \n");
			sql.append("       to_char(TSB.PLAN_LOAD_DATE, 'yyyy-mm-dd') DELIVERY_DATE, --计划装车日期  \n");
			sql.append("       to_char(TVD.DLV_FY_DATE, 'yyyy-mm-dd') DLV_FY_DATE, --最晚发运日期        \n");
			sql.append("       to_char(TVD.DLV_JJ_DATE, 'yyyy-mm-dd') ARRIVE_DATE, --最晚到货日期        \n");
			sql.append("       DECODE(SIGN(TRUNC(TVD.DLV_JJ_DATE, 'DD') - (SYSDATE)), -1, 1, 0) AS IS_RED\n");
			sql.append("  from tt_vs_dlvry            tvd,                 \n");
			sql.append("       tt_vs_dlvry_dtl        tvdd,                \n");
			sql.append("       Tt_Sales_Waybill       tsw,                 \n");
			sql.append("       tt_sales_way_bill_dtl  tswb,                \n");
			sql.append("       VW_MATERIAL_GROUP_MAT  VMGM,                \n");
			sql.append("       TT_SALES_BOARD         TSB, --组板主表      \n");
			sql.append("       TT_SALES_BO_DETAIL     TSBD, --组板明细表   \n");
			sql.append("       TT_SALES_LOGI          TSL,                 \n");
			sql.append("       TM_VEHICLE             TMV,                 \n");
			sql.append("       TM_VHCL_MATERIAL_GROUP G,                   \n");
			sql.append("       TM_VHCL_MATERIAL       TMVM,                \n");
			sql.append("       TM_WAREHOUSE           TW,                  \n");
			sql.append("       TM_WAREHOUSE           SH                   \n");
			sql.append(" where tvd.req_id = tvdd.req_id                    \n");
			sql.append("   and tswb.mat_id = tvdd.material_id              \n");
			sql.append("   AND TSW.BILL_ID = TSBD.BILL_ID                  \n");
			sql.append("   AND TSB.BO_ID = TSBD.BO_ID                      \n");
			sql.append("   AND TSBD.ORDER_ID = TVD.ORD_ID                  \n");
			sql.append("   AND TSBD.OR_DE_ID = TVDD.ORD_DETAIL_ID          \n");
			sql.append("   AND TSBD.MAT_ID = TVDD.MATERIAL_ID              \n");
			sql.append("   AND VMGM.MATERIAL_ID = TSWB.MAT_ID              \n");
			sql.append("   AND TVD.DLV_LOGI_ID = TSL.LOGI_ID(+)            \n");
			sql.append("   and TMV.MODEL_ID = G.GROUP_ID                   \n");
			sql.append("   and TMV.MATERIAL_ID = TMVM.MATERIAL_ID          \n");
			sql.append("   and TMV.VEHICLE_ID = tswb.vehicle_id            \n");
			sql.append("   AND TVD.DLV_REC_WH_ID = SH.WAREHOUSE_ID(+)      \n");
			sql.append("   AND TVD.DLV_WH_ID = TW.WAREHOUSE_ID(+)          \n");
			sql.append("   AND TMV.LIFE_CYCLE =  "+Constant.VEHICLE_LIFE_08+"\n");
			sql.append("   AND TVD.Dlv_Type = "+Constant.DELIVERY_ORD_TYPE_ALLOCAT+"\n");
			sql.append("   and TMV.VEHICLE_ID IN ("+veHicleId+")\n") ;
			return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
		}
}
