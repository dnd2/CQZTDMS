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
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 批售订单管理
 * @author shuyh
 * 2017/7/18
 */
public class BatchOrderManageDao  extends BaseDao<PO>{
	private static final BatchOrderManageDao dao = new BatchOrderManageDao ();
	public static final BatchOrderManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * 导出EXCEL
	 * @param params 查询参数LIST
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllocaSeachQueryExport(Map<String, Object> map){
		Object[] obj=getSQL(map);
		List<Map<String, Object>> list= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName());
		return list;
	}
	/**
	 * 批售订单查询
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
	 * 批售订单查询sql
	 * @param map
	 * @return
	 */
	public Object[] getSQL(Map<String, Object> map){
		/******************************页面查询字段start**************************/
		String dealerCode = CommonUtils.checkNull(map.get("dealerCode")); //经销商
		String orderNo = CommonUtils.checkNull(map.get("orderNo")); //批售单号
		String reqNo = CommonUtils.checkNull(map.get("reqNo")); //申请单号
		String warehouseId = CommonUtils.checkNull(map.get("warehouseId")); // 仓库ID
		String lastStartdate = CommonUtils.checkNull(map.get("lastStartdate")); //最晚到货日期开始
		String lastEndDate = CommonUtils.checkNull(map.get("lastEndDate")); // 最晚到货日期结束
		String transType = CommonUtils.checkNull(map.get("transType")); // 发运方式
		String subStartdate = CommonUtils.checkNull(map.get("subStartdate")); //提报日期开始
		String subEndDate = CommonUtils.checkNull(map.get("subEndDate")); // 提报日期结束
		String orderStatus = CommonUtils.checkNull(map.get("orderStatus")); //订单状态
		String poseId=CommonUtils.checkNull(map.get("poseId"));
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT td.dealer_id,\n" );
		sql.append("       td.dealer_code, --经销商代码\n" );
		sql.append("       td.dealer_shortname dealer_name, --经销商名称\n" );
		sql.append("       tso.req_id,\n" );
		sql.append("       tso.req_no, --申请单号\n" );
		sql.append("       tso.ord_no, --批售单号\n" );
		sql.append("       tw.warehouse_name, --仓库名称\n" );
		sql.append("       to_char(tso.dlv_jj_date, 'YYYY-MM-DD') jj_date, --最晚到货日期\n" );
		sql.append("       tso.req_ship_type delivery_type,\n" );
		sql.append("       fy.code_desc send_way_txt, --发运方式\n" );
		sql.append("       to_char(tso.req_date, 'YYYY-MM-DD') sub_date, --提交日期\n" );
		sql.append("       nvl(tso.req_total, 0) sub_num, --申请数量\n" );
		sql.append("       nvl(tso.dlv_fp_total, 0) fp_num, --分派数量\n" );
		sql.append("       nvl(tso.dlv_bd_total, 0) bd_num, --组板数量\n" );
		sql.append("       nvl(tso.dlv_fy_total, 0) fy_num, --发运数量\n" );
		sql.append("       nvl(tso.dlv_jj_total, 0) jj_num, --交接数量\n" );
		sql.append("       nvl(tso.dlv_ys_total, 0) ys_num, --验收数量\n" );
		sql.append("       nvl(tso.dlv_jj_total, 0) - nvl(tso.dlv_ys_total, 0) AS ZT_num, --在途数量\n" );
		sql.append("       tso.dlv_status, -- 订单状态\n" );
		sql.append("       dd.code_desc dlv_status_txt --状态\n" );
		sql.append("  FROM tt_vs_dlvry tso\n" );
		sql.append("  JOIN tm_dealer td ON tso.req_rec_dealer_id = td.dealer_id\n" );
		sql.append("  JOIN tm_warehouse tw ON tw.warehouse_id = tso.req_wh_id\n" );
		sql.append("  JOIN tm_pose_business_area pb on tw.area_id=pb.area_id\n" );
		sql.append("  JOIN TC_POSE TP ON PB.POSE_ID=TP.POSE_ID\n" );
		sql.append("  JOIN tc_code fy ON tso.req_ship_type = fy.code_id\n" );
		sql.append("                 AND fy.status = 10011001\n" );
		sql.append("  JOIN tc_code dd ON tso.dlv_status = dd.code_id\n" );
		sql.append("                 AND dd.status = 10011001\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append(" AND TP.POSE_ID=?\n");
		params.add(poseId);
		if(!dealerCode.equals("")&&null!=dealerCode){
			sql.append("AND TD.DEALER_CODE in("+getDealerCodeStr(dealerCode)+")\n");
			//params.add(dealerCode);
		}
		if(!orderNo.equals("")&&null!=orderNo){
			sql.append("AND TSO.ORD_NO like ?\n");
			params.add('%'+orderNo+'%');
		}
		if(!reqNo.equals("")&&null!=reqNo){
			sql.append("AND TSO.REQ_NO like ?\n");
			params.add('%'+reqNo+'%');
		}
		if(!warehouseId.equals("")&&null!=warehouseId){
			sql.append("AND TSO.REQ_WH_ID=?\n");
			params.add(warehouseId);
		}
		if(!lastStartdate.equals("")&&null!=lastStartdate){
			sql.append("AND TSO.DLV_JJ_DATE>=TO_DATE('"+lastStartdate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n"); 
		}
		if(!lastEndDate.equals("")&&null!=lastEndDate){
			sql.append("AND TSO.DLV_JJ_DATE<=TO_DATE('"+lastEndDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n"); 
		}
		if(!transType.equals("")&&null!=transType){
			sql.append("AND TSO.DLV_SHIP_TYPE=?\n");
			params.add(transType);
		}
		if(!subStartdate.equals("")&&null!=subStartdate){
			sql.append("AND TSO.REQ_DATE>=TO_DATE('"+subStartdate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n"); 
		}
		if(!subEndDate.equals("")&&null!=subEndDate){
			sql.append("AND TSO.REQ_DATE<=TO_DATE('"+subEndDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n"); 
		}
		if(!orderStatus.equals("")&&null!=orderStatus){
			sql.append("AND TSO.DLV_STATUS=?\n");
			params.add(orderStatus);
		}
		sql.append("  ORDER BY TSO.REQ_DATE DESC\n");
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
		sql.append("SELECT td.dealer_code,\n" );
		sql.append("       td.dealer_name, --经销商名称\n" );
		sql.append("       tso.req_id order_id,\n" );
		sql.append("       tso.dlvry_req_no order_no, --订单号\n" );
		sql.append("       tso.fund_type,\n" );
		sql.append("       (SELECT tvt.type_name\n" );
		sql.append("          FROM tt_vs_account_type tvt\n" );
		sql.append("         WHERE tvt.type_id = tso.fund_type) acc_type_txt, --资金类型\n" );
		sql.append("       tw.warehouse_name, --发运仓库\n" );
		sql.append("       tso.order_type,\n" );
		sql.append("       (SELECT tc.code_desc\n" );
		sql.append("          FROM tc_code tc\n" );
		sql.append("         WHERE tc.code_id = tso.order_type) order_type_txt, --订单类型\n" );
		sql.append("       to_char(tso.plan_deliver_date, 'YYYY-MM-DD') plan_deliver_date, --最晚到货日期\n" );
		sql.append("       tso.delivery_type,\n" );
		sql.append("       (SELECT tc.code_desc\n" );
		sql.append("          FROM tc_code tc\n" );
		sql.append("         WHERE tc.code_id = tso.delivery_type) send_way_txt, --发运方式\n" );
		sql.append("       tva.address, --收车地址\n" );
		sql.append("       tso.link_man, --收车联系人\n" );
		sql.append("       tso.tel, --收车联系电话\n" );
		sql.append("       tso.req_remark --备注\n" );
		sql.append("  FROM tt_vs_dlvry_req tso\n" );
		sql.append("   JOIN tm_dealer td ON tso.receiver = td.dealer_id\n" );
		sql.append("   LEFT JOIN tm_warehouse tw ON tw.warehouse_id = tso.warehouse_id\n" );
		sql.append("   LEFT JOIN tm_vs_address tva ON tva.id = tso.address_id\n");
		sql.append("  WHERE tso.req_id="+orderId+"\n");
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
		sql.append("select TVOD.DETAIL_ID,\n" );
		sql.append("       TVOD.REQ_AMOUNT,\n" );
		sql.append("       TVOD.MATERIAL_ID,\n" );
		sql.append("       TVM.MATERIAL_CODE, --物料代码\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("       TVM.COLOR_NAME, --颜色\n" );
		sql.append("       TVOD.SINGLE_PRICE,\n" );
		sql.append("       TVOD.SINGLE_PRICE DISCOUNT_S_PRICE, --启票价\n" );
		sql.append("       TVOD.DISCOUNT_RATE,\n" );
		sql.append("       TVOD.DISCOUNT_PRICE, --折扣金额\n" );
		sql.append("       TVOD.TOTAL_PRICE, --启票金额\n" );
		sql.append("       VMT.SERIES_ID,\n" );
		sql.append("       VMT.SERIES_NAME, --车系名称\n" );
		sql.append("       VMT.MODEL_NAME, --车型名称\n" );
		sql.append("       VMT.PACKAGE_NAME, --配置名称\n" );
		sql.append("       VMT.PACKAGE_CODE\n" );
		sql.append("  from Tt_Vs_Dlvry_Req_Dtl   TVOD,\n" );
		sql.append("       TM_VHCL_MATERIAL      TVM,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT VMT\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TVOD.MATERIAL_ID = VMT.MATERIAL_ID\n" );
		sql.append("   AND TVOD.REQ_ID = "+orderId+"\n");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
}
