package com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtVsOrderPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title : DealerOrderCheckDAO.java
 * @Package: com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage
 * @Description: 结算中心管理：经销商订单审核
 * @date : 2010-6-23
 * @version: V1.0
 */
public class DealerOrderCheckDAO extends BaseDao {

	private static final DealerOrderCheckDAO dao = new DealerOrderCheckDAO();

	public static final DealerOrderCheckDAO getInstance() {
		return dao;
	}

	/** 
	* @Title	  : getDealerOrderCheckInit_Query 
	* @Description: 查询可审核的订单列表
	* @param      : @param map
	* @param      : @param pageSize
	* @param      : @param curPage
	* @return     : PageResult<Map<String,Object>> 
	* @throws 
	* @LastUpdate :2010-7-14
	*/
	public static PageResult<Map<String, Object>> getDealerOrderCheckInit_Query(Map<String, Object> map, int pageSize, int curPage) {

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
		sql.append("SELECT TTO.ORDER_ID,\n");
		sql.append("       TMD.DEALER_SHORTNAME D_NAME, --订货方\n");
		sql.append("       TMD1.DEALER_SHORTNAME K_NAME, --开票方\n");
		sql.append("       TTO.ORDER_NO, --订单号\n");
		sql.append("       TO_CHAR(TTO.RAISE_DATE, 'yyyy-MM-dd') RAISE_DATE, --提报日期\n");
		sql.append("       TTO.ORDER_YEAR || '年' || TTO.ORDER_WEEK || '周' ORDER_WEEK,\n");
		sql.append("       TTO.ORDER_TYPE, --订单类型\n");
		sql.append("       TTO.ORDER_STATUS, --订单状态\n");
		sql.append("       SUM(TTOD.ORDER_AMOUNT) ORDER_AMOUNT, --提报数量\n");
		sql.append("       NVL(SUM(TTOD.CHECK_AMOUNT), 0) CHECK_AMOUNT, --审核数量\n");
		sql.append("       SUM(TTOD.ORDER_AMOUNT) - NVL(SUM(TTOD.CHECK_AMOUNT), 0) NO_CHECK_AMOUNT, --待审核数量\n");
		sql.append("       TMD.DEALER_ID,\n");
		sql.append("       NVL(SUM(TTOD.APPLYED_AMOUNT), 0) APPLYED_AMOUNT\n");

		sql.append("  FROM TT_VS_ORDER        TTO,\n");
		sql.append("       TM_DEALER          TMD,\n");
		sql.append("       TM_DEALER          TMD1,\n");
		sql.append("       TT_VS_ORDER_DETAIL TTOD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM\n");
		sql.append(" WHERE TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TTOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TTO.BILLING_ORG_ID = TMD1.DEALER_ID\n");
		sql.append("   AND TTO.ORDER_ID = TTOD.ORDER_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TTO.ORDER_STATUS = " + Constant.ORDER_STATUS_02 + "\n");
		sql.append("   AND TMD.PARENT_DEALER_D IN (" + dealerId + ")\n");
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
		sql.append(" GROUP BY TTO.ORDER_ID,\n");
		sql.append("          TMD.DEALER_SHORTNAME,\n");
		sql.append("          TMD1.DEALER_SHORTNAME,\n");
		sql.append("          TTO.ORDER_NO,\n");
		sql.append("          TO_CHAR(TTO.RAISE_DATE, 'yyyy-MM-dd'),\n");
		sql.append("          TTO.ORDER_YEAR || '年' || TTO.ORDER_WEEK || '周',\n");
		sql.append("          TTO.ORDER_TYPE,\n");
		sql.append("          TTO.ORDER_STATUS,\n");
		sql.append("          TMD.DEALER_ID\n");
		sql.append("HAVING SUM(TTOD.ORDER_AMOUNT) > 0\n");
		sql.append(" ORDER BY TO_CHAR(TTO.RAISE_DATE, 'yyyy-MM-dd') DESC\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}
	/** 
	* @Title	  : getDealerOrderCheckInit_Query 
	* @Description: 查询可审核的订单列表下载
	* @param      : @param map
	* @param      : @param pageSize
	* @param      : @param curPage
	* @return     : PageResult<Map<String,Object>> 
	* @throws 
	* @LastUpdate :2010-7-14
	*/
	public static List<Map<String, Object>> getDealerOrderCheckInit_QueryLoad(Map<String, Object> map, int pageSize, int curPage) {

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
		sql.append("SELECT TTO.ORDER_ID,\n");
		sql.append("       TMD.DEALER_SHORTNAME D_NAME, --订货方\n");
		sql.append("       TMD1.DEALER_SHORTNAME K_NAME, --开票方\n");
		sql.append("       TTO.ORDER_NO, --订单号\n");
		sql.append("       TO_CHAR(TTO.RAISE_DATE, 'yyyy-MM-dd') RAISE_DATE, --提报日期\n");
		sql.append("       TTO.ORDER_YEAR || '年' || TTO.ORDER_WEEK || '周' ORDER_WEEK,\n");
		sql.append("       TC2.CODE_DESC ORDER_TYPE, --订单类型\n");
		sql.append("       TC1.CODE_DESC  ORDER_STATUS, --订单状态\n");
		sql.append("       SUM(TTOD.ORDER_AMOUNT) ORDER_AMOUNT, --提报数量\n");
		sql.append("       NVL(SUM(TTOD.CHECK_AMOUNT), 0) CHECK_AMOUNT, --审核数量\n");
		sql.append("       SUM(TTOD.ORDER_AMOUNT) - NVL(SUM(TTOD.CHECK_AMOUNT), 0) NO_CHECK_AMOUNT, --待审核数量\n");
		sql.append("       TMD.DEALER_ID,\n");
		sql.append("       NVL(SUM(TTOD.APPLYED_AMOUNT), 0) APPLYED_AMOUNT\n");

		sql.append("  FROM TT_VS_ORDER        TTO,\n");
		sql.append("       TM_DEALER          TMD,\n");
		sql.append("       TM_DEALER          TMD1,\n");
		sql.append("       TT_VS_ORDER_DETAIL TTOD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,TC_CODE TC1,TC_CODE TC2\n");
		sql.append(" WHERE TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TTOD.MATERIAL_ID = TVM.MATERIAL_ID   AND TC1.CODE_ID=TTO.ORDER_STATUS   AND TC2.CODE_ID=TTO.ORDER_TYPE\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TTO.BILLING_ORG_ID = TMD1.DEALER_ID\n");
		sql.append("   AND TTO.ORDER_ID = TTOD.ORDER_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TTO.ORDER_STATUS = " + Constant.ORDER_STATUS_02 + "\n");
		sql.append("   AND TMD.PARENT_DEALER_D IN (" + dealerId + ")\n");
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
		sql.append(" GROUP BY TTO.ORDER_ID,\n");
		sql.append("          TMD.DEALER_SHORTNAME,\n");
		sql.append("          TMD1.DEALER_SHORTNAME,\n");
		sql.append("          TTO.ORDER_NO,\n");
		sql.append("          TO_CHAR(TTO.RAISE_DATE, 'yyyy-MM-dd'),\n");
		sql.append("          TTO.ORDER_YEAR || '年' || TTO.ORDER_WEEK || '周',\n");
		sql.append("          TTO.ORDER_TYPE,\n");
		sql.append("          TTO.ORDER_STATUS,\n");
		sql.append("          TMD.DEALER_ID,TC1.CODE_DESC,TC2.CODE_DESC\n");
		sql.append("HAVING SUM(TTOD.ORDER_AMOUNT) > 0\n");
		sql.append(" ORDER BY TO_CHAR(TTO.RAISE_DATE, 'yyyy-MM-dd') DESC\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	/**
	 * 根据orderId获得订单po
	 * 
	 * @param orderId
	 * @return
	 */
	public TtVsOrderPO getTtSalesOrder(String orderId) {
		TtVsOrderPO po = new TtVsOrderPO();
		po.setOrderId(new Long(orderId));
		List<PO> list = select(po);
		return list.size() != 0 ? (TtVsOrderPO) list.get(0) : null;
	}
	/** 
	* @Title	  : getOrderInfo 
	* @Description: 查询某一订单详细信息
	* @param      : @param order_id：订单id
	* @return     : Map<String,Object>
	* @throws 
	* @LastUpdate :2010-6-23
	*/
	public static Map<String,Object> getOrderInfo(String order_id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TTO.ORDER_ID,\n");
		sql.append("       TMD.DEALER_SHORTNAME D_NAME, --订货方\n");  
		sql.append("       TMD1.DEALER_SHORTNAME K_NAME, --开票方\n");  
		sql.append("       TTO.ORDER_NO, --订单号\n");  
		sql.append("       TO_CHAR(TTO.RAISE_DATE, 'yyyy-MM-dd') RAISE_DATE, --提报日期\n");  
		sql.append("       TTO.ORDER_YEAR || '年' || TTO.ORDER_WEEK || '周' ORDER_WEEK,\n");  
		sql.append("       TTO.ORDER_TYPE, --订单类型\n");  
		//sql.append("       TTAT.TYPE_NAME, --资金类型\n");  
		sql.append("       SUM(TTOD.ORDER_AMOUNT) ORDER_AMOUNT, --提报数量\n");  
		sql.append("       NVL(SUM(TTOD.CHECK_AMOUNT), 0) CHECK_AMOUNT, --审核数量\n");  
		sql.append("       SUM(TTOD.ORDER_AMOUNT) - NVL(SUM(TTOD.CHECK_AMOUNT), 0) NO_CHECK_AMOUNT, --待审核数量\n");  
		sql.append("       TTO.DELIVERY_TYPE,--运送方式\n");
		sql.append("       TMA.ADDRESS DELIVERY_ADDRESS,--运送地点\n");
		sql.append("       TTO.REFIT_REMARK,--改装说明\n");
		sql.append("       TTO.PAY_REMARK,--付款信息备注\n");
		sql.append("       TTO.ORDER_REMARK,--备注说明\n");
		sql.append("       TMD1.DEALER_ID,\n");
		sql.append("	   A.AREA_ID,\n");
		sql.append("	   A.AREA_NAME,\n");
		sql.append("	   TMD.DEALER_ID D_DEALER_ID,\n");
		sql.append("       TMA.LINK_MAN,\n");
		sql.append("       TMA.TEL,\n");
		sql.append("       TMA.ID,\n");
		sql.append("       TTO.IS_FLEET\n");


		sql.append("  FROM TT_VS_ORDER        TTO,\n");  
		sql.append("       TM_DEALER          TMD,\n");  
		sql.append("       TM_DEALER          TMD1,\n");  
		sql.append("       TT_VS_ORDER_DETAIL TTOD,\n");  
		sql.append("       TM_BUSINESS_AREA   A,\n");
		sql.append("       TM_VS_ADDRESS      TMA\n");

		sql.append(" WHERE TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTO.BILLING_ORG_ID = TMD1.DEALER_ID\n");  
		sql.append("   AND TTO.ORDER_ID = TTOD.ORDER_ID\n");  
		sql.append("   AND TTO.ORDER_STATUS = "+Constant.ORDER_STATUS_02+"\n");
		sql.append("   AND A.AREA_ID = TTO.AREA_ID\n");
        sql.append("   AND TMA.ID(+) = TTO.DELIVERY_ADDRESS\n");
		sql.append("   AND TTO.ORDER_ID ="+order_id+"\n");  
		sql.append(" GROUP BY TTO.ORDER_ID,\n");  
		sql.append("          TMD.DEALER_SHORTNAME,\n");  
		sql.append("          TMD1.DEALER_SHORTNAME,\n");  
		sql.append("          TTO.ORDER_NO,\n");  
		sql.append("          TO_CHAR(TTO.RAISE_DATE, 'yyyy-MM-dd'),\n");  
		sql.append("          TTO.ORDER_YEAR || '年' || TTO.ORDER_WEEK || '周',\n");  
		sql.append("          TTO.ORDER_TYPE,\n");  
		//sql.append("          TTAT.TYPE_NAME,\n");
		sql.append("          TTO.DELIVERY_TYPE,\n");
		sql.append("          TTO.DELIVERY_ADDRESS,\n");
		sql.append("          TTO.REFIT_REMARK,\n");
		sql.append("       	  TTO.PAY_REMARK,\n");
		sql.append("          TTO.ORDER_REMARK,\n");
		sql.append("		  TMD1.DEALER_ID,\n");
		sql.append("	      A.AREA_ID,\n");
		sql.append("	      A.AREA_NAME,\n");
		sql.append("		  TMD.DEALER_ID,\n");
		sql.append("          TMA.ADDRESS,\n");
		sql.append("          TMA.LINK_MAN,\n");
		sql.append("          TMA.TEL,\n");
		sql.append("          TMA.ID,\n");
		sql.append("          TTO.IS_FLEET\n");

		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	
	/** 
	* @Title	  : getOrderDetailList 
	* @Description: 查询某一订单的订单详细列表
	* @param      : @param order_id：订单id
	* @return     : List<Map<String,Object>>
	* @throws 
	* @LastUpdate :2010-6-23
	*/
	public static List<Map<String,Object>> getOrderDetailList(String order_id,String dealerId){
		StringBuffer sql = new StringBuffer();
		
		
		
		
		sql.append("SELECT X.SERIES_NAME,\n");
		sql.append("       X.SERIES_ID,\n");  
		sql.append("       X.MATERIAL_CODE,\n");  
		sql.append("       X.MATERIAL_NAME,\n");  
		sql.append("       X.ORDER_AMOUNT,\n");  
		sql.append("       (X.SC_MATCH_AMOUNT + X.CHECK_AMOUNT) CHECK_AMOUNT,\n");  
		sql.append("       X.NO_CHECK_AMOUNT,\n");  
		sql.append("       X.SPECIAL_BATCH_NO,\n");  
		sql.append("       RESOURCE_AMOUNT,\n");  
		sql.append("       MATERIAL_ID,\n");  
		sql.append("       GROUP_ID,\n");  
		sql.append("       C_NUMBER,\n");  
		sql.append("       DETAIL_ID,\n");  
		sql.append("       APPLYED_AMOUNT\n");  
		sql.append("  FROM (SELECT (SELECT Gi.GROUP_NAME\n");  
		sql.append("                  FROM TM_VHCL_MATERIAL_GROUP Gi\n");  
		sql.append("                 WHERE Gi.GROUP_LEVEL = 2\n");  
		sql.append("                 START WITH Gi.GROUP_ID = G.group_id\n");  
		sql.append("                CONNECT BY PRIOR Gi.PARENT_GROUP_ID = Gi.GROUP_ID) SERIES_NAME, --车系\n");  
		sql.append("               (SELECT Gi.GROUP_ID\n");  
		sql.append("                  FROM TM_VHCL_MATERIAL_GROUP Gi\n");  
		sql.append("                 WHERE Gi.GROUP_LEVEL = 2\n");  
		sql.append("                 START WITH Gi.GROUP_ID = G.group_id\n");  
		sql.append("                CONNECT BY PRIOR Gi.PARENT_GROUP_ID = Gi.GROUP_ID) SERIES_ID, --车系ID\n");  
		sql.append("               M.MATERIAL_CODE, --物料编号\n");  
		sql.append("               M.MATERIAL_NAME, --物料名称\n");  
		sql.append("               TTOD.ORDER_AMOUNT, --提报数量\n");  
		sql.append("               NVL((SELECT COUNT(1)\n");  
		sql.append("                     FROM TT_VS_SC_MATCH TS, TM_VEHICLE V\n");  
		sql.append("                    WHERE TS.ORDER_DETAIL_ID(+) = TTOD.DETAIL_ID\n");  
		sql.append("                      AND TS.VEHICLE_ID = V.VEHICLE_ID\n");  
		sql.append("                      AND V.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("                    GROUP BY V.MATERIAL_ID),\n");  
		sql.append("                   0) SC_MATCH_AMOUNT,\n");  
		sql.append("               NVL((SELECT TSM.MATCH_NUMBER\n");  
		sql.append("                     FROM TMP_SC_MATCH TSM\n");  
		sql.append("                    WHERE TSM.ORDER_ID = "+order_id+"\n");  
		sql.append("                      AND TSM.MATERIAL_ID = M.MATERIAL_ID),\n");  
		sql.append("                   0) CHECK_AMOUNT, --已审核数量\n");  
		sql.append("               TTOD.ORDER_AMOUNT -\n");  
		sql.append("               NVL((SELECT TSM.MATCH_NUMBER\n");  
		sql.append("                     FROM TMP_SC_MATCH TSM\n");  
		sql.append("                    WHERE TSM.ORDER_ID = "+order_id+"\n");  
		sql.append("                      AND TSM.MATERIAL_ID = M.MATERIAL_ID),\n");  
		sql.append("                   0) NO_CHECK_AMOUNT, --待审核数量\n");  
		sql.append("               TTOD.SPECIAL_BATCH_NO, --订做批次号\n");  
		sql.append("               (SELECT COUNT(1)\n");  
		sql.append("                  FROM TM_VEHICLE TMV\n");  
		sql.append("                 WHERE TMV.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("                   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_03+"\n");  
		/*sql.append("                   AND NOT EXISTS\n");  
		sql.append("                 (SELECT VEHICLE_ID\n");  
		sql.append("                          FROM TT_VS_SC_MATCH TM\n");  
		sql.append("                         WHERE TM.VEHICLE_ID = TMV.VEHICLE_ID)\n");  */
		sql.append("                   AND TMV.DEALER_ID IN ("+dealerId+")) RESOURCE_AMOUNT, --资源数量\n");  
		sql.append("               M.MATERIAL_ID,\n");  
		sql.append("               G.GROUP_ID,\n");  
		sql.append("               TTOD.ORDER_AMOUNT - NVL(TTOD.CHECK_AMOUNT, 0) -\n");  
		sql.append("               (SELECT COUNT(1)\n");  
		sql.append("                  FROM TM_VEHICLE TMV\n");  
		sql.append("                 WHERE TMV.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("                   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_03+"\n");  
		sql.append("                   AND NOT EXISTS\n");  
		sql.append("                 (SELECT VEHICLE_ID\n");  
		sql.append("                          FROM TT_VS_SC_MATCH TM\n");  
		sql.append("                         WHERE TM.VEHICLE_ID = TMV.VEHICLE_ID)\n");  
		sql.append("                   AND TMV.DEALER_ID IN ("+dealerId+")) C_NUMBER, --采购数量\n");  
		sql.append("               TTOD.DETAIL_ID,\n");  
		sql.append("               NVL(TTOD.APPLYED_AMOUNT, 0) APPLYED_AMOUNT\n");  
		sql.append("          FROM TM_VHCL_MATERIAL         M,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP_R R,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP   G,\n");  
		sql.append("               TT_VS_ORDER_DETAIL       TTOD\n");  
		sql.append("         WHERE M.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("           AND R.GROUP_ID = G.GROUP_ID\n");  
		sql.append("           AND TTOD.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("           AND TTOD.ORDER_ID = "+order_id+"\n");  
		sql.append("         ORDER BY TTOD.CREATE_DATE DESC) X\n");

		return dao.pageQuery(sql.toString(), null,dao.getFunName());
	}
	
	public static PageResult<Map<String,Object>> getOrderDetailList_PageResult(String order_id,String dealerId, int pageSize, int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT (SELECT TG.GROUP_NAME\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP TG\n");  
		sql.append("         WHERE TG.TREE_CODE = SUBSTR(G.TREE_CODE, 0, 6)) SERIES_NAME, --车系\n");  
		sql.append("	   (SELECT TG.GROUP_ID\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP TG\n");  
		sql.append("         WHERE TG.TREE_CODE = SUBSTR(G.TREE_CODE, 0, 6)) SERIES_ID, --车系ID\n");
		sql.append("       M.MATERIAL_CODE, --物料编号\n");  
		sql.append("       M.MATERIAL_NAME, --物料名称\n");  
		sql.append("       TTOD.ORDER_AMOUNT, --提报数量\n");  
//		sql.append("       NVL(TTOD.CHECK_AMOUNT, 0) CHECK_AMOUNT, --已审核数量\n");  
		sql.append("		NVL((SELECT TSM.MATCH_NUMBER\n");
		sql.append("      		FROM TMP_SC_MATCH TSM\n");  
		sql.append("     		WHERE TSM.ORDER_ID = "+order_id+"\n");  
		sql.append("       		AND TSM.MATERIAL_ID = M.MATERIAL_ID),\n");  
		sql.append("    		0) CHECK_AMOUNT, --已审核数量\n");

//		sql.append("       TTOD.ORDER_AMOUNT - NVL(TTOD.CHECK_AMOUNT, 0) NO_CHECK_AMOUNT, --待审核数量\n");  
		sql.append("	   TTOD.ORDER_AMOUNT - NVL((SELECT TSM.MATCH_NUMBER\n");
		sql.append("                          FROM TMP_SC_MATCH TSM\n");  
		sql.append("                         WHERE TSM.ORDER_ID = "+order_id+"\n");  
		sql.append("                           AND TSM.MATERIAL_ID = M.MATERIAL_ID),\n");  
		sql.append("                        0) NO_CHECK_AMOUNT, --待审核数量\n");
		sql.append("       TTOD.SPECIAL_BATCH_NO, --订做批次号\n");  
		sql.append("       (SELECT COUNT(1)\n");  
		sql.append("          FROM TM_VEHICLE TMV\n");  
		sql.append("         WHERE TMV.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("           AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_03+"\n");
		sql.append("		   AND NOT EXISTS (SELECT VEHICLE_ID\n");
		sql.append("           		FROM TT_VS_SC_MATCH TM\n");  
		sql.append("         		WHERE TM.VEHICLE_ID = TMV.VEHICLE_ID)\n");
		sql.append("           AND TMV.DEALER_ID IN ("+dealerId+")) RESOURCE_AMOUNT, --资源数量\n");  
		sql.append("       M.MATERIAL_ID,\n");
		sql.append("       G.GROUP_ID,\n");
		sql.append("       TTOD.ORDER_AMOUNT - NVL(TTOD.CHECK_AMOUNT, 0) -\n");
		sql.append("       (SELECT COUNT(1)\n");  
		sql.append("          FROM TM_VEHICLE TMV\n");  
		sql.append("         WHERE TMV.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("           AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_03+"\n");
		sql.append("		   AND NOT EXISTS (SELECT VEHICLE_ID\n");
		sql.append("           		FROM TT_VS_SC_MATCH TM\n");  
		sql.append("         		WHERE TM.VEHICLE_ID = TMV.VEHICLE_ID)\n");
		sql.append("           AND TMV.DEALER_ID IN ("+dealerId+")) C_NUMBER, --采购数量\n");  
		sql.append("       TTOD.DETAIL_ID\n");

		sql.append("  FROM TM_VHCL_MATERIAL         M,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G,\n");  
		sql.append("       TT_VS_ORDER_DETAIL       TTOD\n");  
		sql.append(" WHERE M.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("   AND R.GROUP_ID = G.GROUP_ID\n");  
		sql.append("   AND TTOD.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("   AND TTOD.ORDER_ID = "+order_id+"\n");

		return dao.pageQuery(sql.toString(), null,dao.getFunName(), pageSize, curPage);
	}
	
	public static List<Map<String,Object>> getCheckHisList(String order_id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TO_CHAR(TVOC.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE,\n");
		sql.append("       TMO.ORG_NAME,\n");  
		sql.append("       TCU.NAME USER_NAME,\n");  
		sql.append("       TCC.CODE_DESC CHECK_STATUS,\n");  
		sql.append("       TVOC.CHECK_DESC\n");  
		sql.append("  FROM TT_VS_ORDER_CHECK TVOC, TM_ORG TMO, TC_USER TCU, TC_CODE TCC\n");  
		sql.append(" WHERE TVOC.CHECK_ORG_ID = TMO.ORG_ID\n");  
		sql.append("   AND TVOC.CHECK_USER_ID = TCU.USER_ID\n");  
		sql.append("   AND TVOC.CHECK_STATUS = TCC.CODE_ID\n");  
		sql.append("   AND TVOC.ORDER_ID = "+order_id+"\n");  
		sql.append(" ORDER BY TVOC.CHECK_DATE ASC\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName());
	}
	
	
	public static PageResult <Map<String,Object>> getVehicleList(String dealerId, String warehouse_id,String material_id ,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TMV.VEHICLE_ID,\n");
		sql.append("       G.GROUP_NAME, --车系\n");  
		sql.append("       TMVM.MATERIAL_CODE, --物料编号\n");  
		sql.append("       TMVM.MATERIAL_NAME, --物料名称\n");  
		sql.append("       TDW.WAREHOUSE_NAME VEHICLE_AREA, --所在仓库\n");  
		sql.append("       TMV.SPECIAL_BATCH_NO, --订做批次号\n");  
		sql.append("       TMV.VIN,\n");  
		sql.append("(      CASE\n");
		sql.append("         WHEN (TMV.VEHICLE_ID IN\n");  
		sql.append("              (SELECT TSV.VEHICLE_ID\n");  
		sql.append("                  FROM TMP_SC_MATCH TSM, TMP_SC_MATCH_VEHICLE TSV\n");  
		sql.append("                 WHERE TSM.TMP_MATCH_ID = TSV.TMP_MATCH_ID\n");  
		sql.append("                   AND TSM.MATERIAL_ID = "+material_id+")) THEN\n");  
		sql.append("          0\n");  
		sql.append("         ELSE\n");  
		sql.append("          1\n");  
		sql.append("       END) IS_MATCH\n");

		sql.append("  FROM TM_VEHICLE             TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");  
		sql.append("	   TM_DEALER_WAREHOUSE    TDW\n");

		sql.append(" WHERE TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_03+"\n");  
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");  
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");
		sql.append("   AND TDW.WAREHOUSE_ID(+) = TMV.VEHICLE_AREA\n");
		sql.append("   AND TMV.MATERIAL_ID = "+material_id+"\n");
		sql.append("   AND TMV.DEALER_ID IN ("+dealerId+")\n");  
		/*sql.append("AND NOT EXISTS\n");
		sql.append("(SELECT 1 FROM TT_VS_SC_MATCH S WHERE S.VEHICLE_ID = TMV.VEHICLE_ID)\n");*/

		if (null != warehouse_id && !"".equals(warehouse_id)) {
			sql.append("   AND TMV.VEHICLE_AREA = "+warehouse_id+"\n");
		}
		sql.append(" ORDER BY G.GROUP_NAME DESC\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	
	public static PageResult <Map<String,Object>> getDealerList(String dealerId,String dealerCode, String dealerName,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TMD.DEALER_CODE, TMD.DEALER_SHORTNAME\n");
		sql.append("  FROM TM_DEALER TMD\n");  
		sql.append(" WHERE TMD.PARENT_DEALER_D IN ("+dealerId+")\n");  
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append("  AND TMD.DEALER_CODE LIKE '%"+dealerCode.trim()+"%'\n");
		}
		if (null != dealerName && !"".equals(dealerName)) {
			sql.append("  AND TMD.DEALER_SHORTNAME LIKE '%"+dealerName.trim()+"%'\n");
		}
		sql.append(" ORDER BY TMD.DEALER_SHORTNAME\n");

		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	
	/**
	 * 根据登录用户的companyId查询仓库列表
	 * */
	public static List<Map<String,Object>> getDealerWarehouseList(Long companyId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TDW.WAREHOUSE_ID, TDW.WAREHOUSE_NAME\n");
		sql.append("  FROM TM_DEALER_WAREHOUSE TDW\n");  
		sql.append(" WHERE TDW.DEALER_COMANY_ID = "+companyId+"\n");  
		sql.append(" ORDER BY TDW.WAREHOUSE_ID DESC\n");

		return dao.pageQuery(sql.toString(), null,dao.getFunName());
	}
	
	/**
	 * 根据订单id和物料id查询TMP_SC_MATCH_VEHICLE (配车临时车辆表)的车辆信息
	 * 
	 * */
	public static List<Map<String,Object>> getTmpVehicleList(String order_id,String material_id,String dealerId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TSV.VEHICLE_ID, TMV.DEALER_ID\n");
		sql.append("  FROM TMP_SC_MATCH_VEHICLE TSV, TMP_SC_MATCH TSM, TM_VEHICLE TMV\n");  
		sql.append(" WHERE TSM.TMP_MATCH_ID = TSV.TMP_MATCH_ID\n");  
		sql.append("   AND TMV.VEHICLE_ID = TSV.VEHICLE_ID\n");
		sql.append("   AND TSM.ORDER_ID = "+order_id+"\n");  
		sql.append("   AND TSM.MATERIAL_ID = "+material_id+"\n");
		sql.append("   AND TMV.DEALER_ID IN ("+dealerId+")\n");

		return dao.pageQuery(sql.toString(), null,dao.getFunName());
	}
	
	/**
	 * 是否有上级经销商
	 * */
	public static Map<String,String> checkParentDealer(String dealerId ,String areaId){
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT DISTINCT TMD.PARENT_DEALER_D\n");
		sql.append("  FROM TM_DEALER TMD, TM_DEALER_BUSINESS_AREA AREA\n");  
		sql.append(" WHERE TMD.DEALER_ID IN ("+dealerId+")\n");  
		sql.append("   AND TMD.DEALER_ID = AREA.DEALER_ID\n");  
		sql.append("   AND AREA.AREA_ID ="+areaId+"\n");


		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	
	/**
	 * 根据订单id查询FLEET_ID，再根据FLEET_ID查询集团客户信息
	 * */
	public static Map<String,String> getFleetInfo(String orderId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TMF.FLEET_ID, TMF.FLEET_NAME, TMF.ADDRESS\n");
		sql.append("  FROM TT_VS_ORDER TTO, TM_FLEET TMF\n");  
		sql.append(" WHERE TTO.FLEET_ID = TMF.FLEET_ID\n");  
		sql.append("   AND TTO.ORDER_ID = "+orderId+"\n");

		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
