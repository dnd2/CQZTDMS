package com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage;

import java.sql.ResultSet;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**   
 * @Title  : DealerOrderDispatchDAO.java
 * @Package: com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage
 * @Description:  经销商订单调拨
 * @date   : 2010-6-26 
 * @version: V1.0   
 */
public class DealerOrderDispatchDAO extends BaseDao {

	private static final DealerOrderDispatchDAO dao = new DealerOrderDispatchDAO();

	public static final DealerOrderDispatchDAO getInstance() {
		return dao;
	}
	
	public static PageResult<Map<String, Object>> getVehicleList (Map<String, Object> map, int pageSize, int curPage) {
		String orderYear1 = (String) map.get("orderYear1");
		String orderWeek1 = (String) map.get("orderWeek1");
		String orderYear2 = (String) map.get("orderYear2");
		String orderWeek2 = (String) map.get("orderWeek2");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String orderNo = (String) map.get("orderNo");
		String dealerCode = (String) map.get("dealerCode");
		String dealerId = (String) map.get("dealerId");
		orderWeek1 = orderWeek1.length() == 1 ? "0" + orderWeek1 : orderWeek1;
		orderWeek2 = orderWeek2.length() == 1 ? "0" + orderWeek2 : orderWeek2;

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TMV.VEHICLE_ID,\n");
		sql.append("       TMD.DEALER_ID,\n");  
		sql.append("       TMD.DEALER_CODE, --经销商代码\n");  
		sql.append("       TMD.DEALER_NAME, --经销商名称\n");  
		sql.append("       TTO.ORDER_YEAR || '年' || ORDER_WEEK || '周' ORDER_WEEK, --订单周度\n");  
		sql.append("       TTO.ORDER_NO, --订单号码\n");  
		sql.append("       TMVM.MATERIAL_NAME, --物料名称\n");  
		sql.append("       TMV.VIN, --VIN\n");  
		sql.append("       TMW.WAREHOUSE_NAME --所在位置\n");  
		sql.append("  FROM TT_VS_SC_MATCH     TTVSM,\n");  
		sql.append("       TM_VEHICLE         TMV,\n");  
		sql.append("       TT_VS_ORDER_DETAIL TTOD,\n");  
		sql.append("       TT_VS_ORDER        TTO,\n");  
		sql.append("       TM_DEALER          TMD,\n");  
		sql.append("       TM_VHCL_MATERIAL   TMVM,\n");  
		sql.append("       TM_WAREHOUSE       TMW\n");  
		sql.append(" WHERE TTVSM.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TTOD.DETAIL_ID = TTVSM.ORDER_DETAIL_ID\n");  
		sql.append("   AND TTOD.ORDER_ID = TTO.ORDER_ID\n");  
		sql.append("   AND TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");  
		sql.append("   AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID\n");  
		sql.append("   AND TMD.PARENT_DEALER_D IN ("+dealerId+")\n");  
		sql.append("   AND NVL(TTVSM.STATUS, 0) <> "+Constant.STORAGE_CHANGE_TYPE_02+" \n");
		sql.append("   AND TTO.ORDER_YEAR || DECODE(LENGTH(TTO.ORDER_WEEK),\n");
		sql.append("                                1,\n");
		sql.append("                                '0' || TTO.ORDER_WEEK,\n");
		sql.append("                                TTO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
		sql.append("   AND TTO.ORDER_YEAR || DECODE(LENGTH(TTO.ORDER_WEEK),\n");
		sql.append("                                1,\n");
		sql.append("                                '0' || TTO.ORDER_WEEK,\n");
		sql.append("                                TTO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		if(null != areaId && !"".equals(areaId)) {
			sql.append("   AND TTO.AREA_ID = " + areaId + "\n");
		}
		if(null != groupCode && !"".equals(groupCode)) {
			String[] array = groupCode.split(",");
			sql.append("   AND TVMG.GROUP_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if(null != dealerCode && !"".equals(dealerCode)) {
			String[] array = dealerCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if(null != orderNo && !"".equals(orderNo)){
			sql.append("   AND TTO.ORDER_NO LIKE '%"+orderNo.trim()+"%'\n");
		}
		sql.append(" ORDER BY TTO.ORDER_NO DESC\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);

	}
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;  
	}

}
