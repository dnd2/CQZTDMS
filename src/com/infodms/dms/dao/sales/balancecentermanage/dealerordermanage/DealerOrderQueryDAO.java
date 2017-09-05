package com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.balancecentermanage.dealerordermanage.DealerOrderQuery;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title : DealerOrderQuery.java
 * @Package: com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage
 * @Description: 结算中心：经销商订单查询
 * @date : 2010-6-25
 * @version: V1.0
 */
public class DealerOrderQueryDAO extends BaseDao {

	public Logger logger = Logger.getLogger(DealerOrderQueryDAO.class);
	private ActionContext act = ActionContext.getContext();
	private static final DealerOrderQueryDAO dao = new DealerOrderQueryDAO();

	public static final DealerOrderQueryDAO getInstance() {
		return dao;
	}

	/**
	 * @Title : dealerOrderTotalQueryList
	 * @Description: 汇总查询
	 * @return : PageResult<Map<String,Object>>返回类型
	 * @throws
	 * @LastUpdate :2010-6-25
	 */
	public static PageResult<Map<String, Object>> dealerOrderTotalQueryList(Map<String, Object> map, int curPage, int pageSize) {

		String orderYear1 = (String) map.get("orderYear1");
		String orderWeek1 = (String) map.get("orderWeek1");
		String orderYear2 = (String) map.get("orderYear2");
		String orderWeek2 = (String) map.get("orderWeek2");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String orderType = (String) map.get("orderType");
		String orderStatus = (String) map.get("orderStatus");
		String orderNo = (String) map.get("orderNo");
		String newOrderNo = (String) map.get("newOrderNo");
		orderWeek1 = orderWeek1.length() == 1 ? "0" + orderWeek1 : orderWeek1;
		orderWeek2 = orderWeek2.length() == 1 ? "0" + orderWeek2 : orderWeek2;
		String dealerId = (String) map.get("dealerId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       SUM(TVOD.ORDER_AMOUNT) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.MATCH_AMOUNT), 0) DELIVERY_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER       		TVO1,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");
		sql.append("       TM_DEALER                TD\n");
		sql.append(" WHERE TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TD.DEALER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVO1.OLD_ORDER_ID(+)\n");
		sql.append("   AND TD.PARENT_DEALER_D IN (" + dealerId + ")\n");
		sql.append("   AND TVO.OLD_ORDER_ID IS NULL\n");
		sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
		sql.append("                                1,\n");
		sql.append("                                '0' || TVO.ORDER_WEEK,\n");
		sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
		sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
		sql.append("                                1,\n");
		sql.append("                                '0' || TVO.ORDER_WEEK,\n");
		sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (!groupCode.equals("")) {
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
		if (!orderType.equals("")) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		if (!orderStatus.equals("")) {
			sql.append("   AND TVO.ORDER_STATUS = " + orderStatus + "\n");
		}
		if (!orderNo.equals("")) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (!newOrderNo.equals("")) {
			sql.append("   AND TVO1.ORDER_NO LIKE '%" + newOrderNo + "%'\n");
		}

		sql.append(" GROUP BY TVM.MATERIAL_CODE, TVM.MATERIAL_NAME, TVO.ORDER_TYPE\n");
		sql.append("HAVING SUM(TVOD.ORDER_AMOUNT) > 0\n");
		sql.append(" ORDER BY TVO.ORDER_TYPE");

		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}
	
	
	/** 
	* @Title	  : getDealerSalesOrderTotalExportList 
	* @Description: 汇总下载
	* @throws 
	* @LastUpdate :2010-6-25
	*/
	public static List<Map<String, Object>> getDealerSalesOrderTotalExportList (Map<String, Object> map){
		String orderYear1 = (String) map.get("orderYear1");
		String orderWeek1 = (String) map.get("orderWeek1");
		String orderYear2 = (String) map.get("orderYear2");
		String orderWeek2 = (String) map.get("orderWeek2");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String orderType = (String) map.get("orderType");
		String orderStatus = (String) map.get("orderStatus");
		String orderNo = (String) map.get("orderNo");
		String newOrderNo = (String) map.get("newOrderNo");
		orderWeek1 = orderWeek1.length() == 1 ? "0" + orderWeek1 : orderWeek1;
		orderWeek2 = orderWeek2.length() == 1 ? "0" + orderWeek2 : orderWeek2;
		String dealerId = (String) map.get("dealerId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TC.CODE_DESC,\n");
		sql.append("       SUM(TVOD.ORDER_AMOUNT) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.MATCH_AMOUNT), 0) DELIVERY_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER       		TVO1,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");
		sql.append("       TM_DEALER                TD,\n");
		sql.append("       TC_CODE 				    TC\n");
		sql.append(" WHERE TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TD.DEALER_ID\n");
		sql.append("   AND TC.CODE_ID = TVO.ORDER_TYPE\n");
		sql.append("   AND TVO.ORDER_ID = TVO1.OLD_ORDER_ID(+)\n");
		sql.append("   AND TD.PARENT_DEALER_D IN (" + dealerId + ")\n");
		sql.append("   AND TVO.OLD_ORDER_ID IS NULL\n");
		sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
		sql.append("                                1,\n");
		sql.append("                                '0' || TVO.ORDER_WEEK,\n");
		sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
		sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
		sql.append("                                1,\n");
		sql.append("                                '0' || TVO.ORDER_WEEK,\n");
		sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (!groupCode.equals("")) {
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
		if (!orderType.equals("")) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		if (!orderStatus.equals("")) {
			sql.append("   AND TVO.ORDER_STATUS = " + orderStatus + "\n");
		}
		if (!orderNo.equals("")) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (!newOrderNo.equals("")) {
			sql.append("   AND TVO1.ORDER_NO LIKE '%" + newOrderNo + "%'\n");
		}

		sql.append(" GROUP BY TVM.MATERIAL_CODE, TVM.MATERIAL_NAME, TC.CODE_DESC\n");
		sql.append("HAVING SUM(TVOD.ORDER_AMOUNT) > 0\n");
		sql.append(" ORDER BY TC.CODE_DESC");

		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}

	/**
	 * @Title : dealerOrderDetailQueryList
	 * @Description:明细查询
	 * @return : PageResult<Map<String,Object>>返回类型
	 * @throws
	 * @LastUpdate :2010-6-25
	 */
	public static PageResult<Map<String, Object>> dealerOrderDetailQueryList(Map<String, Object> map, int curPage, int pageSize) {

		String orderYear1 = (String) map.get("orderYear1");
		String orderWeek1 = (String) map.get("orderWeek1");
		String orderYear2 = (String) map.get("orderYear2");
		String orderWeek2 = (String) map.get("orderWeek2");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String orderType = (String) map.get("orderType");
		String orderStatus = (String) map.get("orderStatus");
		String orderNo = (String) map.get("orderNo");
		String newOrderNo = (String) map.get("newOrderNo");
		orderWeek1 = orderWeek1.length() == 1 ? "0" + orderWeek1 : orderWeek1;
		orderWeek2 = orderWeek2.length() == 1 ? "0" + orderWeek2 : orderWeek2;
		String dealerId = (String) map.get("dealerId");
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TD1.DEALER_SHORTNAME ORDER_ORG_NAME,\n");
		sql.append("       TD2.DEALER_SHORTNAME BILLING_ORG_NAME,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TVO1.ORDER_NO OLD_ORDER_NO,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       TVO.ORDER_STATUS,\n");
		sql.append("       TVO1.ORDER_STATUS ORDER_STATUSS,\n");
		sql.append("       SUM(TVOD.ORDER_AMOUNT) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.MATCH_AMOUNT), 0) DELIVERY_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER       		TVO1,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");
		sql.append("       TM_DEALER                TD1,\n");
		sql.append("       TM_DEALER                TD2\n");
		sql.append(" WHERE TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TD1.DEALER_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TD2.DEALER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVO1.OLD_ORDER_ID(+)\n");
		sql.append("   AND TD1.PARENT_DEALER_D IN (" + dealerId + ")\n");
		sql.append("   AND TVO.OLD_ORDER_ID IS NULL\n");
		sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
		sql.append("                                1,\n");
		sql.append("                                '0' || TVO.ORDER_WEEK,\n");
		sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
		sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
		sql.append("                                1,\n");
		sql.append("                                '0' || TVO.ORDER_WEEK,\n");
		sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (!groupCode.equals("")) {
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
		if (!orderType.equals("")) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		if (!orderStatus.equals("")) {
			sql.append("   AND TVO.ORDER_STATUS = " + orderStatus + "\n");
		}
		if (!orderNo.equals("")) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (!newOrderNo.equals("")) {
			sql.append("   AND TVO1.ORDER_NO LIKE '%" + newOrderNo + "%'\n");
		}
		sql.append(" GROUP BY TD1.DEALER_SHORTNAME,\n");
		sql.append("          TD2.DEALER_SHORTNAME,\n");
		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TVO1.ORDER_NO,\n");
		sql.append("          TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD'),\n");
		sql.append("          TVO.ORDER_TYPE,\n");
		sql.append("          TVO.ORDER_STATUS,\n");
		sql.append("          TVO.CREATE_DATE,\n");
		sql.append("          TVO1.ORDER_STATUS,\n");
		sql.append("          TVO.ORDER_ID\n");

		sql.append("HAVING SUM(TVOD.ORDER_AMOUNT) > 0\n");
		sql.append("  ORDER BY TVO.CREATE_DATE DESC, TVO.ORDER_TYPE, TVO.ORDER_ID");

		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}

	/** 
	* @Title	  : getDealerSalesOrderDetailExportList 
	* @Description: 明细下载 
	* @return     : List<Map<String,Object>>返回类型 
	* @throws 
	* @LastUpdate :2010-6-25
	*/
	public static List<Map<String, Object>> getDealerSalesOrderDetailExportList(Map<String, Object> map){
		String orderYear1 = (String) map.get("orderYear1");
		String orderWeek1 = (String) map.get("orderWeek1");
		String orderYear2 = (String) map.get("orderYear2");
		String orderWeek2 = (String) map.get("orderWeek2");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String orderType = (String) map.get("orderType");
		String orderStatus = (String) map.get("orderStatus");
		String orderNo = (String) map.get("orderNo");
		String newOrderNo = (String) map.get("newOrderNo");
		orderWeek1 = orderWeek1.length() == 1 ? "0" + orderWeek1 : orderWeek1;
		orderWeek2 = orderWeek2.length() == 1 ? "0" + orderWeek2 : orderWeek2;
		String dealerId = (String) map.get("dealerId");
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TD1.DEALER_SHORTNAME ORDER_ORG_NAME,\n");
		sql.append("       TD2.DEALER_SHORTNAME BILLING_ORG_NAME,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TVO1.ORDER_NO OLD_ORDER_NO,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TC1.CODE_DESC,\n");
		sql.append("       TC2.CODE_DESC CODE_DESC1,\n");
		sql.append("	   TC3.CODE_DESC CODE_DESC2,\n");
		sql.append("       SUM(TVOD.ORDER_AMOUNT) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.MATCH_AMOUNT), 0) DELIVERY_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER       		TVO1,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");
		sql.append("       TM_DEALER                TD1,\n");
		sql.append("       TM_DEALER                TD2,\n");
		sql.append("       TC_CODE 				    TC1,\n");
		sql.append("       TC_CODE 				    TC2,\n");
		sql.append("	   TC_CODE                  TC3\n");
		sql.append(" WHERE TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TD1.DEALER_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TD2.DEALER_ID\n");
		sql.append("   AND TVO.ORDER_TYPE = TC1.CODE_ID\n");
		sql.append("   AND TVO.ORDER_STATUS = TC2.CODE_ID\n");
		sql.append("   AND TVO1.ORDER_STATUS = TC3.CODE_ID(+)\n");
		sql.append("   AND TVO.ORDER_ID = TVO1.OLD_ORDER_ID(+)\n");
		sql.append("   AND TD1.PARENT_DEALER_D IN (" + dealerId + ")\n");
		sql.append("   AND TVO.OLD_ORDER_ID IS NULL\n");
		sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
		sql.append("                                1,\n");
		sql.append("                                '0' || TVO.ORDER_WEEK,\n");
		sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
		sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
		sql.append("                                1,\n");
		sql.append("                                '0' || TVO.ORDER_WEEK,\n");
		sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (!groupCode.equals("")) {
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
		if (!orderType.equals("")) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		if (!orderStatus.equals("")) {
			sql.append("   AND TVO.ORDER_STATUS = " + orderStatus + "\n");
		}
		if (!orderNo.equals("")) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (!newOrderNo.equals("")) {
			sql.append("   AND TVO1.ORDER_NO LIKE '%" + newOrderNo + "%'\n");
		}
		sql.append(" GROUP BY TD1.DEALER_SHORTNAME,\n");
		sql.append("          TD2.DEALER_SHORTNAME,\n");
		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TVO1.ORDER_NO,\n");
		sql.append("          TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD'),\n");
		sql.append("          TC1.CODE_DESC,\n");
		sql.append("          TC3.CODE_DESC,\n");
		sql.append("          TC2.CODE_DESC\n");
		sql.append("HAVING SUM(TVOD.ORDER_AMOUNT) > 0\n");
		sql.append(" ORDER BY TC1.CODE_DESC");

		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
