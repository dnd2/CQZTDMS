/**
 * @Title: OrderAdjustDao.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-11
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.dao.sales.ordermanage.orderadjust;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsResourceChngPO;
import com.infodms.dms.po.TtVsResourcePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class OrderAdjustDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(OrderAdjustDao.class);
	private static final OrderAdjustDao dao = new OrderAdjustDao();

	public static final OrderAdjustDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 订单取消查询列表
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getOrderCancelQueryList(Map<String, Object> map,
			int curPage, int pageSize) {

		/*String orderYear1 = (String) map.get("orderYear1");
		String orderWeek1 = (String) map.get("orderWeek1");
		String orderYear2 = (String) map.get("orderYear2");
		String orderWeek2 = (String) map.get("orderWeek2");*/
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");
		String areaId = (String) map.get("areaId");
		String dealerCode = (String) map.get("dealerCode");
		//String orderType = (String) map.get("orderType");
		String orderNo = (String) map.get("orderNo");
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		/*orderWeek1 = orderWeek1.length() == 1 ? "0" + orderWeek1 : orderWeek1;
		orderWeek2 = orderWeek2.length() == 1 ? "0" + orderWeek2 : orderWeek2;*/

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TMP.*\n");
		sql.append("  FROM (SELECT TD1.DEALER_SHORTNAME ORDER_ORG_NAME,\n");
		sql.append("               TD2.DEALER_SHORTNAME BILLING_ORG_NAME,\n");
		sql.append("               TVO.ORDER_ID,\n");
		sql.append("               TVO.ORDER_NO,\n");
		sql.append("               TVO.ORDER_YEAR || '年' || TVO.ORDER_WEEK || '周' ORDER_DATE,\n");
		sql.append("               TVO.ORDER_TYPE,\n");
		sql.append("               NVL(TVO.VER,0) VER,");
		sql.append("               TVO.ORDER_STATUS,\n");
		sql.append("               SUM(TVOD.ORDER_AMOUNT) ORDER_AMOUNT,\n");
		sql.append("               NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("               NVL(SUM(TVOD.CALL_AMOUNT), 0) CALL_AMOUNT,\n");
		sql.append("               NVL(SUM(TVOD.DELIVERY_AMOUNT), 0) DELIVERY_AMOUNT,\n");
		sql.append("               NVL(SUM(TVOD.MATCH_AMOUNT), 0) MATCH_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER        TVO,\n");
		sql.append("               TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("               TM_DEALER          TD1,\n");
		sql.append("               TM_DEALER          TD2\n");
		sql.append("         WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("           AND TVO.ORDER_ORG_ID = TD1.DEALER_ID\n");
		sql.append("           AND TVO.BILLING_ORG_ID = TD2.DEALER_ID\n");
		sql.append("           AND TVO.COMPANY_ID = " + companyId + "\n");
		sql.append("   		   AND " + dealerSql);
		sql.append("           AND TVO.ORDER_STATUS = " + Constant.ORDER_STATUS_05 + "\n");
		if(!startDate.equals("")){
			sql.append("           AND TVO.RAISE_DATE >= TO_DATE('" + startDate + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!endDate.equals("")){
			sql.append("           AND TVO.RAISE_DATE <= TO_DATE('" + endDate + " 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		/*sql.append("           AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
		sql.append("                                        1,\n");
		sql.append("                                        '0' || TVO.ORDER_WEEK,\n");
		sql.append("                                        TVO.ORDER_WEEK) >= " + orderYear1
				+ orderWeek1 + "\n");
		sql.append("           AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
		sql.append("                                        1,\n");
		sql.append("                                        '0' || TVO.ORDER_WEEK,\n");
		sql.append("                                        TVO.ORDER_WEEK) <= " + orderYear2
				+ orderWeek2 + "\n");*/
		sql.append("           AND TVO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("           AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (!dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD1.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		/*if (!orderType.equals("")) {
			sql.append("           AND TVO.ORDER_TYPE = " + orderType + "\n");
		}*/
		sql.append("           AND TVO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		if (!orderNo.equals("")) {
			sql.append("           AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		sql.append("         GROUP BY TD1.DEALER_SHORTNAME,\n");
		sql.append("                  TD2.DEALER_SHORTNAME,\n");
		sql.append("                  TVO.ORDER_ID,\n");
		sql.append("                  TVO.ORDER_NO,\n");
		sql.append("                  TVO.ORDER_YEAR || '年' || TVO.ORDER_WEEK || '周',\n");
		sql.append("                  TVO.ORDER_TYPE,\n");
		sql.append("                  TVO.VER,\n");
		sql.append("                  TVO.ORDER_STATUS\n");
		sql.append("         ORDER BY TVO.ORDER_TYPE) TMP\n");
		sql.append(" WHERE TMP.CHECK_AMOUNT > 0\n");
		sql.append("   AND TMP.CHECK_AMOUNT > TMP.CALL_AMOUNT");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderAdjustDao.getOrderCancelQueryList", pageSize,
				curPage);
		return ps;
	}

	/**
	 * 订单部分取消列表
	 * 
	 * @param orderId
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getOrderPartCancelQueryList(String orderId, int curPage,
			int pageSize) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TD1.DEALER_SHORTNAME ORDER_ORG_NAME,\n");
		sql.append("       TD1.DEALER_ID,\n");
		sql.append("       TD2.DEALER_SHORTNAME BILLING_ORG_NAME,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TVO.ORDER_YEAR || '年' || TVO.ORDER_WEEK || '周' ORDER_DATE,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TVO.ORDER_STATUS,\n");
		sql.append("       TVOD.DETAIL_ID,\n");
		sql.append("       NVL(TVOD.VER,0) VER,\n");
		sql.append("       TVOD.SPECIAL_BATCH_NO,\n");
		sql.append("       TVOD.ORDER_AMOUNT,\n");
		sql.append("       NVL(TVOD.CHECK_AMOUNT, 0) CHECK_AMOUNT,\n");
		sql.append("       NVL(TVOD.CALL_AMOUNT, 0) CALL_AMOUNT,\n");
		sql.append("       NVL(TVOD.DELIVERY_AMOUNT, 0) DELIVERY_AMOUNT,\n");
		sql.append("       NVL(TVOD.MATCH_AMOUNT, 0) MATCH_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER        TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       TM_VHCL_MATERIAL   TVM,\n");
		sql.append("       TM_DEALER          TD1,\n");
		sql.append("       TM_DEALER          TD2\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TD1.DEALER_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TD2.DEALER_ID\n");
		sql.append("   AND TVO.ORDER_ID = " + orderId + "");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderAdjustDao.getOrderPartCancelQueryList", pageSize,
				curPage);
		return ps;
	}

	/**
	 * 订单取消查询(OEM)
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getOemOrderCancelQueryList(Map<String, Object> map,
			int curPage, int pageSize) {

		String orderYear1 = (String) map.get("orderYear1");
		String orderWeek1 = (String) map.get("orderWeek1");
		String orderYear2 = (String) map.get("orderYear2");
		String orderWeek2 = (String) map.get("orderWeek2");
		String dealerCode = (String) map.get("dealerCode");
		String orderType = (String) map.get("orderType");
		String orderNo = (String) map.get("orderNo");
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");
		String areaId = (String) map.get("areaId");
		String areaIds = (String) map.get("areaIds");
		orderWeek1 = orderWeek1.length() == 1 ? "0" + orderWeek1 : orderWeek1;
		orderWeek2 = orderWeek2.length() == 1 ? "0" + orderWeek2 : orderWeek2;
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TD1.DEALER_SHORTNAME ORDER_ORG_NAME,\n");
		sql.append("       TD2.DEALER_SHORTNAME BILLING_ORG_NAME,\n");
		sql.append("       TVO.ORDER_ID,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       TVO.ORDER_STATUS,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TVOC.CHNG_AMT,\n");
		sql.append("       TO_CHAR(TVOC.CHNG_DATE, 'YYYY-MM-DD HH24:MI:SS') CHNG_DATE,\n");
		sql.append("       TU.NAME USER_NAME,\n");
		sql.append("       TVOC.CHNG_REASON\n");
		sql.append("  FROM TT_VS_ORDER_CHNG   TVOC,\n");
		sql.append("       TT_VS_ORDER        TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       TC_USER            TU,\n");
		sql.append("       TM_DEALER          TD1,\n");
		sql.append("       TM_DEALER          TD2,\n");
		sql.append("       TM_VHCL_MATERIAL   TVM\n");
		sql.append(" WHERE TVO.ORDER_ORG_ID = TD1.DEALER_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TD2.DEALER_ID\n");
		sql.append("   AND TVOC.ORDER_ID = TVO.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVOC.DETAIL_ID = TVOD.DETAIL_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVOC.CHNG_USER_ID = TU.USER_ID\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND " + dealerSql);
		/*sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
		sql.append("                                1,\n");
		sql.append("                                '0' || TVO.ORDER_WEEK,\n");
		sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1+ "\n");
		sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
		sql.append("                                1,\n");
		sql.append("                                '0' || TVO.ORDER_WEEK,\n");
		sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2+ "\n");*/
		if (null != beginTime && !"".equals(beginTime)) {
			sql.append("   AND TVO.RAISE_DATE >= TO_DATE('"+beginTime+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append("   AND TVO.RAISE_DATE <= TO_DATE('"+endTime+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (!dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("   AND TD1.DEALER_CODE IN (");
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
		if (!orderNo.equals("")) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		sql.append(" ORDER BY TVOC.CHNG_DATE DESC, TVO.ORDER_ID ASC");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderAdjustDao.getOemOrderCancelQueryList", pageSize,
				curPage);
		return ps;
	}

	/**
	 * 订单取消查询(DEALER)
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getDealerOrderCancelQueryList(Map<String, Object> map,
			int curPage, int pageSize) {

		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");
		String orderType = (String) map.get("orderType");
		String areaId = (String) map.get("areaId");
		String dealerId = (String) map.get("dealerId");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TD1.DEALER_SHORTNAME ORDER_ORG_NAME,\n");
		sql.append("       TD2.DEALER_SHORTNAME BILLING_ORG_NAME,\n");
		sql.append("       TVO.ORDER_ID,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       TVO.ORDER_STATUS,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TVOC.CHNG_AMT,\n");
		sql.append("       TO_CHAR(TVOC.CHNG_DATE, 'YYYY-MM-DD HH24:MI:SS') CHNG_DATE,\n");
		sql.append("       TU.NAME USER_NAME,\n");
		sql.append("       TVOC.CHNG_REASON\n");
		sql.append("  FROM TT_VS_ORDER_CHNG   TVOC,\n");
		sql.append("       TT_VS_ORDER        TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       TC_USER            TU,\n");
		sql.append("       TM_DEALER          TD1,\n");
		sql.append("       TM_DEALER          TD2,\n");
		sql.append("       TM_VHCL_MATERIAL   TVM\n");
		sql.append(" WHERE TVO.ORDER_ORG_ID = TD1.DEALER_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TD2.DEALER_ID\n");
		sql.append("   AND TVOC.ORDER_ID = TVO.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVOC.DETAIL_ID = TVOD.DETAIL_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVOC.CHNG_USER_ID = TU.USER_ID\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TD1.DEALER_ID IN (" + dealerId + ")\n");
		if (!startDate.equals("")) {
			sql.append("   AND TVOC.CHNG_DATE >= TO_DATE('" + startDate + " 00:00:00"
					+ "', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!endDate.equals("")) {
			sql.append("   AND TVOC.CHNG_DATE <= TO_DATE('" + endDate + " 23:59:59"
					+ "', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (!orderType.equals("")) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		sql.append(" ORDER BY TVOC.CHNG_DATE DESC, TVO.ORDER_ID ASC");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderAdjustDao.getDealerOrderCancelQueryList", pageSize,
				curPage);
		return ps;
	}

	public int resourceRelease(TtVsOrderPO orderPo, TtVsOrderDetailPO detailPo, TmDateSetPO datePo,
			String chngAmt, AclUserBean logonUser) {
		String orderYear = orderPo.getOrderYear().toString();
		String orderWeek = orderPo.getOrderWeek().intValue() < 10 ? "0"
				+ orderPo.getOrderWeek().toString() : orderPo.getOrderWeek().toString();
		int orderDate = Integer.parseInt(orderYear + orderWeek);

		String setYear = datePo.getSetYear();
		String setWeek = datePo.getSetWeek().length() == 1 ? "0" + datePo.getSetWeek() : datePo
				.getSetWeek();
		int setDate = Integer.parseInt(setYear + setWeek);

		Integer resourceYear = orderDate > setDate ? new Integer(orderYear) : new Integer(setYear);
		Integer resourceWeek = orderDate > setDate ? new Integer(orderWeek) : new Integer(setWeek);

		StringBuffer sql = new StringBuffer();

		sql
				.append("SELECT TVR.RESOURCE_ID, TVR.CHECK_AMOUNT, TVR.RESOURCE_AMOUNT FROM TT_VS_RESOURCE TVR\n");
		/*
		 * sql.append(" SET TVR.CHECK_AMOUNT = TVR.CHECK_AMOUNT - " + chngAmt + ",
		 * TVR.RESOURCE_AMOUNT = TVR.RESOURCE_AMOUNT + " + chngAmt + "\n");
		 */
		sql.append(" WHERE TVR.RESOURCE_YEAR = " + resourceYear + "\n");
		sql.append("   AND TVR.RESOURCE_WEEK = " + resourceWeek + "\n");
		sql.append("   AND TVR.MATERIAL_ID = " + detailPo.getMaterialId() + "\n");
		// 定做车订单
		if (orderPo.getOrderType().intValue() == Constant.ORDER_TYPE_03) {
			sql.append("   AND TVR.SPECIAL_BATCH_NO = '" + detailPo.getSpecialBatchNo() + "'\n");
		} else {
			sql.append("   AND TVR.SPECIAL_BATCH_NO IS NULL\n");
		}
		sql.append("   AND TVR.COMPANY_ID = " + orderPo.getCompanyId());

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			BigDecimal resourceId = (BigDecimal) map.get("RESOURCE_ID");
			BigDecimal checkAmount = (BigDecimal) map.get("CHECK_AMOUNT");
			BigDecimal resourceAmount = (BigDecimal) map.get("RESOURCE_AMOUNT");

			TtVsResourcePO condition = new TtVsResourcePO();
			condition.setResourceId(resourceId.longValue());

			TtVsResourcePO value = new TtVsResourcePO();
			value.setCheckAmount(new Integer(checkAmount.intValue() - Integer.parseInt(chngAmt)));
			value.setResourceAmount(new Integer(resourceAmount.intValue()
					+ Integer.parseInt(chngAmt)));

			dao.update(condition, value);

			TtVsResourceChngPO tvrcp = new TtVsResourceChngPO();
			tvrcp.setChngId(Long.parseLong(SequenceManager.getSequence("")));
			tvrcp.setResourceId(resourceId.longValue());
			tvrcp.setChngType(Constant.RESOURCE_CHNG_TYPE_02);
			tvrcp.setOrderId(orderPo.getOrderId());
			tvrcp.setOrderDetailId(detailPo.getDetailId());
			tvrcp.setChngOrgId(logonUser.getOrgId());
			tvrcp.setChngUserId(logonUser.getUserId());
			tvrcp.setChngDate(new Date(System.currentTimeMillis()));
			tvrcp.setCreateBy(logonUser.getUserId());
			tvrcp.setCreateDate(new Date(System.currentTimeMillis()));
			dao.insert(tvrcp); // 插入资源变更表
		}

		return 1;
	}
}
