package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import 

com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : RebackStockDao 
 * @Description   : 退回车入库调整DAO 
 * @author        : ranjian
 * CreateDate     : 2013-5-28
 */
public class RebackStockDao extends BaseDao<PO>{
	private static final RebackStockDao dao = new RebackStockDao ();
	public static final RebackStockDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 *  退回车入库查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getRebackStockQuery
(Map<String, Object> map, int curPage, int pageSize)throws Exception{
	    String groupCode = (String)map.get("groupCode"); // 物料组
	    String materialCode = (String)map.get("materialCode"); // 物料
		String offlineStartDate = (String)map.get("offlineStartDate"); // 下线日期开始
		String offlineEndDate = (String)map.get("offlineEndDate"); // 下线日期结束
		String productStartDate = (String)map.get("productStartDate"); // 入库日期开始
		String productEndDate = (String)map.get("productEndDate"); // 入库日期结束		
		String updateStartDate = (String)map.get("updateStartDate"); // 退库日期开始
		String updateEndDate = (String)map.get("updateEndDate"); // 退库日期结束		
		String vin = (String)map.get("vin"); // vin
		String areaIds = (String)map.get("areaIds"); // 产地
		String reqNo = (String)map.get("REQ_NO"); // 退库单号
		String rlockstatus = (String)map.get("LOCK_STATUS"); // 是否已入库
		String invoiceStatus = map.get("invoiceStatus")+"";//开票状态		
		List<Object> params = new LinkedList<Object>();
		params.add(Constant.RETURN_STATUS_02);
		params.add(Constant.RETURN_STATUS_02);
		params.add(Constant.LOCK_STATUS_05);
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT * FROM (\n");
		sbSql.append("SELECT T3.VEHICLE_ID,\n");
		sbSql.append("       T2.REQ_NO,\n"); 
		sbSql.append("       T4.MODEL_NAME,\n");
		sbSql.append("       T4.PACKAGE_NAME,\n");
		sbSql.append("       T4.MATERIAL_CODE,\n");
		sbSql.append("       T4.MATERIAL_NAME,\n");
		sbSql.append("       T3.VIN,\n");
		sbSql.append("       T3.ENGINE_NO,\n");
		sbSql.append("       TO_CHAR(T3.ORG_STORAGE_DATE, 'YYYY-MM-DD') ORG_STORAGE_DATE,\n");
		sbSql.append("       TO_CHAR(T3.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE,\n");
		sbSql.append("       TO_CHAR(T3.OFFLINE_DATE, 'YYYY-MM-DD') OFFLINE_DATE,\n");		
		if(rlockstatus!=null&&!"".equals(rlockstatus)){
		sbSql.append("       TO_CHAR(T6.UPDATE_DATE, 'YYYY-MM-DD') UPDATE_DATE,\n");
		}	
		sbSql.append("       T3.LIFE_CYCLE,\n");
		sbSql.append("       T3.LOCK_STATUS,\n");
		sbSql.append("       CASE WHEN T1.INVOICE_NO IS NOT NULL THEN '是' ELSE '否' END INVOICE_STATUS,\n");
		sbSql.append("       DENSE_RANK() OVER(PARTITION BY T2.VEHICLE_ID ORDER BY T1.CREATE_DATE DESC,T1.HEAD_ID DESC) RID\n");
		sbSql.append("  FROM TT_RETURN_VEHICLE_HEAD   T1,\n");
		sbSql.append("       TT_VS_RETURN_VEHICLE_REQ T2,\n");
		sbSql.append("       TM_VEHICLE               T3,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT    T4,\n");
		if(rlockstatus!=null&&!"".equals(rlockstatus)){
		sbSql.append("       TT_SALES_ALLOCA_DE       T6,\n");
		}
		sbSql.append("       TM_BUSINESS_AREA         T5\n");
		sbSql.append(" WHERE T1.HEAD_ID = T2.HEAD_ID\n");
		sbSql.append("   AND T2.VEHICLE_ID = T3.VEHICLE_ID\n");
		sbSql.append("   AND T3.MATERIAL_ID = T4.MATERIAL_ID\n");
		sbSql.append("   AND T3.YIELDLY = T5.AREA_ID\n");
		sbSql.append("   AND T1.STATUS = ?\n");
		sbSql.append("   AND T2.STATUS = ?\n");
//		sbSql.append("   AND T3.LOCK_STATUS =?\n "); 
		if(rlockstatus!=null&&!"".equals(rlockstatus)){
			sbSql.append("   AND T3.LOCK_STATUS !=?\n ");
			sbSql.append("   AND t2.vehicle_id = t6.vehicle_id\n ");
			sbSql.append("   AND T6.status = " +Constant.STATUS_DISABLE +"\n ");
		}
		else{
			sbSql.append("   AND T3.LOCK_STATUS =?\n ");
		}	
		if(groupCode!=null&&!"".equals(groupCode)){//物料组过滤
			sbSql.append(MaterialGroupManagerDao.getMaterialGroupQuerySql("T4.PACKAGE_ID", groupCode));
		}
		if(materialCode!=null&&!"".equals(materialCode)){//物料过滤
			sbSql.append(MaterialGroupManagerDao.getMaterialQuerySql("T4.MATERIAL_ID", materialCode));
		}
		if(offlineStartDate!=null&&!"".equals(offlineStartDate)){//下线日期开始过滤
			sbSql.append("   AND TO_CHAR(T3.OFFLINE_DATE,'YYYY-MM-DD')>='"+offlineStartDate+"'\n" );
		}
		if(offlineEndDate!=null&&!"".equals(offlineEndDate)){//下线日期结束过滤
			sbSql.append("   AND TO_CHAR(T3.OFFLINE_DATE,'YYYY-MM-DD')<='"+offlineEndDate+"'\n" );
		}
		if(productStartDate!=null&&!"".equals(productStartDate)){// 生产日期开始过滤
			sbSql.append("   AND TO_CHAR(T3.PRODUCT_DATE,'YYYY-MM-DD')>='"+productStartDate+"'\n" );
		}
		if(productEndDate!=null&&!"".equals(productEndDate)){// 生产日期结束过滤
			sbSql.append("   AND TO_CHAR(T3.PRODUCT_DATE,'YYYY-MM-DD')<='"+productEndDate+"'\n" );
		}				
		if(updateStartDate!=null&&!"".equals(updateStartDate)&&rlockstatus!=null&&!"".equals(rlockstatus)){// 退库日期开始过滤
			sbSql.append("   AND TO_CHAR(T6.UPDATE_DATE, 'YYYY-MM-DD')>='"+updateStartDate+"'\n" );
		}
		if(updateEndDate!=null&&!"".equals(updateEndDate)&&rlockstatus!=null&&!"".equals(rlockstatus)){//退库日期结束过滤
			sbSql.append("   AND TO_CHAR(T6.UPDATE_DATE,'YYYY-MM-DD')<='"+updateEndDate+"'\n" );
		}				
		if(vin!=null&&!"".equals(vin)){//vin过滤
			sbSql.append(GetVinUtil.getVins(vin, "T3"));
			//sbSql.append("   AND T3.VIN LIKE '%"+vin.trim()+"%'\n" );
		}
		if(reqNo!=null&&!"".equals(reqNo)){//退库单号
			sbSql.append("   AND T2.REQ_NO LIKE '%"+reqNo.trim()+"%'\n" );
		}
		if (!XHBUtil.IsNull(invoiceStatus)) {
			if ((Constant.IF_TYPE_YES+"").equals(invoiceStatus)) {
				sbSql.append(" AND T1.INVOICE_NO IS NOT NULL\n");
			} else {
				sbSql.append(" AND T1.INVOICE_NO IS NULL\n");
			}
		}
		sbSql.append("AND T3.YIELDLY in(").append(areaIds==""?Constant.DEFAULT_VALUE:areaIds).append(")\n");//权限控制
		sbSql.append(") T WHERE T.RID = 1 ORDER BY T.VIN ASC\n");	
		PageResult<Map<String, Object>> ps= dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	
	/**
	 * 
	 * 根据车辆id查询
	 * 查询资金类型、车辆id、
	 * 经销商账户余额、折后单价、
	 * 返还给经销商之后，经销商所剩的余额
	 * 
	 * @param vehicleId
	 * @return
	 */
	public List<Map<String, Object>> queryByVehicleId(String vehicleId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT d.fund_type_id,r.vehicle_id,NVL(T.AMOUNT, 0) AMOUNT,\n");
		sql.append("nvl(c.discount_s_price, 0) DISCOUNT_S_PRICE,\n");
		sql.append("NVL(T.AMOUNT, 0) + nvl(c.discount_s_price, 0) AMOUNT_ALL \n");
		sql.append("FROM TT_VS_RETURN_VEHICLE_REQ R,\n");
		sql.append("TT_SALES_ALLOCA_DE       A,\n");
		sql.append("TT_SALES_BO_DETAIL       B,\n");
		sql.append("tt_vs_order_detail       c,\n");
		sql.append("tt_vs_order              d,\n");
		sql.append("TT_SALES_FIN_ACC         T\n");
		sql.append("where R.VEHICLE_ID = A.Vehicle_Id\n");
		sql.append("and a.bo_de_id = b.bo_de_id\n");
		sql.append("and b.or_de_id = c.detail_id\n");
		sql.append("and c.order_id = d.order_id\n");
		sql.append("AND T.DEALER_ID = d.DEALER_ID \n");//--经销商条件
		sql.append("AND T.FIN_TYPE = d.FUND_TYPE_ID \n");//--资金类型条件
		sql.append("and t.yieldly = d.Area_Id \n");//-- 产地条件
		sql.append("and r.vehicle_id = "+vehicleId+"\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
}
