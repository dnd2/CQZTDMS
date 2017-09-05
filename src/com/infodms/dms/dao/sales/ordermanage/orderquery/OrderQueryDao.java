/**
 * @Title: OrderQueryDao.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-9
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.dao.sales.ordermanage.orderquery;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class OrderQueryDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(OrderReportDao.class);
	private static final OrderQueryDao dao = new OrderQueryDao();

	public static final OrderQueryDao getInstance() {
		return dao;
	}

	// 查看日期配置表中当天的记录
	public TmDateSetPO getTmDateSetPO(Date date, Long companyId) {
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");
		String dayStr = formate.format(date);
		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = new TmDateSetPO();
		dateSet.setSetDate(dayStr);
		dateSet.setCompanyId(companyId);
		List<PO> list = select(dateSet);
		return list.size() != 0 ? (TmDateSetPO) list.get(0) : null;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 年列表
	 * 
	 * @return
	 */
	public List<String> getYearList() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		List<String> list = new ArrayList<String>();
		list.add(Integer.toString(year - 1));
		list.add(Integer.toString(year));
		list.add(Integer.toString(year + 1));
		return list;
	}

	/**
	 * 月列表
	 * 
	 * @return
	 */
	public List<String> getMonthList() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 12; i++) {
			list.add(Integer.toString(i + 1));
		}
		return list;
	}

	/**
	 * 周列表
	 * 
	 * @return
	 */
	public List<String> getWeekList() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 53; i++) {
			list.add(Integer.toString(i + 1));
		}
		return list;
	}

	/**
	 * 销售订单汇总查询（OEM）
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getOemSalesOrderTotalQueryList(Map<String, Object> map, int curPage, int pageSize) {
		String dealerCodes = (String) map.get("dealerCodes");
		String orderYear1 = (String) map.get("orderYear1");
		String orderWeek1 = (String) map.get("orderWeek1");
		String orderYear2 = (String) map.get("orderYear2");
		String orderWeek2 = (String) map.get("orderWeek2");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String materialCode = (String) map.get("materialCode");
		String dealerCode = (String) map.get("dealerCode");
		String orderType = (String) map.get("orderType");
		String orderStatus = (String) map.get("orderStatus");
		String orderNo = (String) map.get("orderNo");
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");
		String dealerCode2 = (String) map.get("dealerCode2");
		String areaIds = (String) map.get("areaIds");
		orderWeek1 = orderWeek1.length() == 1 ? "0" + orderWeek1 : orderWeek1;
		orderWeek2 = orderWeek2.length() == 1 ? "0" + orderWeek2 : orderWeek2;
		String dataflag = (String) map.get("dataflag");
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");

		StringBuffer sql = new StringBuffer();

		// sql.append("INSERT INTO TMP_VS_ORDERO1\n");
		sql.append("WITH TMP_VS_ORDERO1 AS (\n");
		/*
		 * sql.append(" SELECT TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID,
		 * SUM(TTVDD.BILLING_AMOUNT) BILLING_AMOUNT\n"); sql.append(" FROM
		 * --TT_VS_DLVRY TTVD,\n"); sql.append(" (SELECT MATERIAL_ID,\n");
		 * sql.append(" DELIVERY_ID,\n"); sql.append(" SUM(DELIVERY_AMOUNT)
		 * BILLING_AMOUNT\n"); sql.append(" FROM TT_VS_DLVRY_DTL\n");
		 * sql.append(" GROUP BY MATERIAL_ID, DELIVERY_ID) TTVDD,\n");
		 * sql.append(" TT_VS_ORDER_DETAIL TTVOD\n"); sql.append(" WHERE
		 * TTVOD.MATERIAL_ID = TTVDD.MATERIAL_ID\n"); sql.append(" AND
		 * EXISTS\n"); sql.append(" (SELECT 1\n"); sql.append(" FROM TT_VS_DLVRY
		 * TTVD\n"); sql.append(" WHERE TTVD.DELIVERY_ID =
		 * TTVDD.DELIVERY_ID\n"); sql.append(" AND TTVOD.ORDER_ID =
		 * TTVD.ORDER_ID\n"); sql.append(" AND TTVD.DELIVERY_STATUS IN\n");
		 * sql.append(" (" + Constant.DELIVERY_STATUS_04 + ", " +
		 * Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " +
		 * Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 +
		 * "))\n"); sql.append("group by TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID\n");
		 */

		sql.append("SELECT TVOD.DETAIL_ID,\n");
		sql.append("       TVOD.MATERIAL_ID,\n");
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER        TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       TT_VS_DLVRY        TVD,\n");
		sql.append("       TT_VS_DLVRY_DTL    TVDD\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVD.ORDER_ID\n");
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n");
		sql.append("   AND TVD.DELIVERY_STATUS IN\n");
		sql.append("       (" + Constant.DELIVERY_STATUS_04 + ", " + Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " + Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 + ")\n");
		sql.append("   AND TVO.ORDER_STATUS NOT IN (" + Constant.ORDER_STATUS_01 + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07 + ", " + Constant.ORDER_STATUS_10 + ")\n");
		if (null != dataflag && "1".equals(dataflag)) {
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}

		sql.append("   AND TVO.ORDER_STATUS NOT IN( " + Constant.ORDER_STATUS_01 + ")\n");
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
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

		sql.append(" GROUP BY TVOD.DETAIL_ID, TVOD.MATERIAL_ID\n");

		sql.append(")\n");

		// dao.update(sql.toString(), null);

		// sql = new StringBuffer();

		/*
		 * //TODO START 新增OTD自动匹配数量 2012-06-18 韩晓宇 sql.append("SELECT
		 * T.MATERIAL_CODE,\n"); sql.append(" T.MATERIAL_NAME,\n"); sql.append("
		 * T.ORDER_TYPE,\n"); sql.append(" T.ORDER_AMOUNT,\n"); sql.append("
		 * T.CHECK_AMOUNT,\n"); sql.append(" T.MATCH_AMOUNT,\n"); sql.append("
		 * T.BILLING_AMOUNT,\n"); sql.append(" COUNT(TOR.MATERIAL_CODE)
		 * OTD_AMOUNT\n"); sql.append(" FROM T_ORDERVIN_RELA TOR,\n");
		 * sql.append(" TT_VS_ORDER TVO2,\n"); sql.append(" TT_VS_ORDER_DETAIL
		 * TVOD2,\n"); sql.append(" TM_VHCL_MATERIAL TVM2,\n"); sql.append("
		 * (\n"); //TODO END
		 */
		sql.append("SELECT TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		/* sql.append(" COUNT(TVM.MATERIAL_CODE) OTD_AMOUNT,\n"); */
		sql.append("       SUM(NVL(TVOD.ORDER_AMOUNT, 0)) ORDER_AMOUNT,\n");
		sql.append("       SUM(NVL(TVOD.CHECK_AMOUNT, 0)) CHECK_AMOUNT,\n");
		sql.append("       SUM(NVL(TVOD.DELIVERY_AMOUNT, 0)) DELIVERY_AMOUNT,\n");
		sql.append("       SUM(NVL(A.BILLING_AMOUNT, 0)) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");
		sql.append("       TM_DEALER                TD1,\n");
		sql.append("       TM_DEALER                TD2,\n");
		sql.append("       TMP_VS_ORDERO1           A\n");
		/* sql.append(" T_ORDERVIN_RELA TOR\n"); */
		sql.append(" WHERE TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		// sql.append(" AND TVDR.ORDER_ID=TVO.ORDER_ID\n");
		sql.append("   AND A.DETAIL_ID(+) = TVOD.DETAIL_ID\n");
		/* sql.append(" AND TOR.MATERIAL_CODE(+) = TVM.MATERIAL_CODE\n"); */
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TD2.DEALER_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TD1.DEALER_ID\n");
		sql.append("   AND TVO.ORDER_STATUS NOT IN (" + Constant.ORDER_STATUS_01 + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07 + ", " + Constant.ORDER_STATUS_10 + ")\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND " + dealerSql);
		if (null != dataflag && "1".equals(dataflag)) {
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}

		sql.append("   AND TVO.ORDER_STATUS NOT IN( " + Constant.ORDER_STATUS_01 + ")\n");
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (!groupCode.equals("")) {
			/*
			 * String[] array = groupCode.split(","); sql.append(" AND
			 * TVMG.GROUP_CODE IN ("); for (int i = 0; i < array.length; i++) {
			 * sql.append("'" + array[i] + "'"); if (i != array.length - 1) {
			 * sql.append(","); } } sql.append(")\n");
			 */
			String[] array = groupCode.split(",");
			long len = array.length;
			sql.append("AND (TVMGR.GROUP_ID IN\n");
			sql.append("      (SELECT TMVMG.GROUP_ID\n");
			sql.append("         FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
			sql.append("        WHERE TMVMG.GROUP_LEVEL = 4\n");
			sql.append("        START WITH TMVMG.GROUP_ID = (SELECT TMVMG.GROUP_ID\n");
			sql.append("                                       FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
			sql.append("                                      WHERE GROUP_CODE = '" + array[0] + "')\n");
			sql.append("       CONNECT BY PRIOR TMVMG.GROUP_ID = TMVMG.PARENT_GROUP_ID)\n");
			for (int i = 1; i < len; i++) {
				sql.append("OR TVMGR.GROUP_ID IN\n");
				sql.append("      (SELECT TMVMG.GROUP_ID\n");
				sql.append("         FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
				sql.append("        WHERE TMVMG.GROUP_LEVEL = 4\n");
				sql.append("        START WITH TMVMG.GROUP_ID = (SELECT TMVMG.GROUP_ID\n");
				sql.append("                                       FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
				sql.append("                                      WHERE GROUP_CODE = '" + array[i] + "'\n");
				sql.append("                                      )\n");
				sql.append("       CONNECT BY PRIOR TMVMG.GROUP_ID = TMVMG.PARENT_GROUP_ID)\n");
			}
			sql.append(")\n");
		}
		if (!materialCode.equals("")) {
			String[] array = materialCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
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
		if (!dealerCode2.equals("")) {
			String[] array = dealerCode2.split(",");
			sql.append("   AND TD2.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		// start
		if (!dealerCodes.equals("")) {
			String[] array = dealerCodes.split(",");
			sql.append("   AND TD2.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		// end
		if (!orderType.equals("")) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		if (!orderStatus.equals("")) {
			sql.append("   AND TVO.ORDER_STATUS = " + orderStatus + "\n");
		}
		if (!orderNo.equals("")) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}

		sql.append(" GROUP BY TVM.MATERIAL_CODE, TVM.MATERIAL_NAME, TVO.ORDER_TYPE\n");
		// sql.append("HAVING SUM(TVOD.ORDER_AMOUNT) > 0\n");

		sql.append(" ORDER BY TVO.ORDER_TYPE");

		/*
		 * //TODO START 新增OTD自动匹配数量 2012-06-18 韩晓宇 sql.append(") T\n");
		 * sql.append(" WHERE T.MATERIAL_CODE = TOR.MATERIAL_CODE(+)\n");
		 * sql.append(" AND T.ORDER_TYPE = TVO2.ORDER_TYPE\n"); sql.append(" AND
		 * TVOD2.ORDER_ID(+) = TVO2.ORDER_ID\n"); sql.append(" AND
		 * TVOD2.MATERIAL_ID = TVM2.MATERIAL_ID\n"); sql.append(" AND
		 * TVM2.MATERIAL_CODE(+) = T.MATERIAL_CODE\n"); sql.append(" GROUP BY
		 * T.MATERIAL_CODE,\n"); sql.append(" T.MATERIAL_NAME,\n"); sql.append("
		 * T.ORDER_TYPE,\n"); sql.append(" T.ORDER_AMOUNT,\n"); sql.append("
		 * T.CHECK_AMOUNT,\n"); sql.append(" T.MATCH_AMOUNT,\n"); sql.append("
		 * T.BILLING_AMOUNT\n"); sql.append(" ORDER BY T.ORDER_TYPE\n"); //TODO
		 * END
		 */
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getOemSalesOrderTotalQueryList", pageSize, curPage);
		return ps;
	}

	/**
	 * 
	 */
	public List<Map<String, Object>> getOemSalesOrderTotalExportList(Map<String, Object> map) {
		String dealerCodes = (String) map.get("dealerCodes");
		String orderYear1 = (String) map.get("orderYear1");
		String orderWeek1 = (String) map.get("orderWeek1");
		String orderYear2 = (String) map.get("orderYear2");
		String orderWeek2 = (String) map.get("orderWeek2");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String materialCode = (String) map.get("materialCode");
		String dealerCode = (String) map.get("dealerCode");
		String orderType = (String) map.get("orderType");
		String orderStatus = (String) map.get("orderStatus");
		String orderNo = (String) map.get("orderNo");
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");
		String dealerCode2 = (String) map.get("dealerCode2");
		String areaIds = (String) map.get("areaIds");
		orderWeek1 = orderWeek1.length() == 1 ? "0" + orderWeek1 : orderWeek1;
		orderWeek2 = orderWeek2.length() == 1 ? "0" + orderWeek2 : orderWeek2;
		String dataflag = (String) map.get("dataflag");
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");

		StringBuffer sql = new StringBuffer();

		// sql.append("INSERT INTO TMP_VS_ORDERO1\n");
		sql.append("WITH TMP_VS_ORDERO1 AS (\n");
		/*
		 * sql.append(" SELECT TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID,
		 * SUM(TTVDD.BILLING_AMOUNT) BILLING_AMOUNT\n"); sql.append(" FROM
		 * --TT_VS_DLVRY TTVD,\n"); sql.append(" (SELECT MATERIAL_ID,\n");
		 * sql.append(" DELIVERY_ID,\n"); sql.append(" SUM(DELIVERY_AMOUNT)
		 * BILLING_AMOUNT\n"); sql.append(" FROM TT_VS_DLVRY_DTL\n");
		 * sql.append(" GROUP BY MATERIAL_ID, DELIVERY_ID) TTVDD,\n");
		 * sql.append(" TT_VS_ORDER_DETAIL TTVOD\n"); sql.append(" WHERE
		 * TTVOD.MATERIAL_ID = TTVDD.MATERIAL_ID\n"); sql.append(" AND
		 * EXISTS\n"); sql.append(" (SELECT 1\n"); sql.append(" FROM TT_VS_DLVRY
		 * TTVD\n"); sql.append(" WHERE TTVD.DELIVERY_ID =
		 * TTVDD.DELIVERY_ID\n"); sql.append(" AND TTVOD.ORDER_ID =
		 * TTVD.ORDER_ID\n"); sql.append(" AND TTVD.DELIVERY_STATUS IN\n");
		 * sql.append(" (" + Constant.DELIVERY_STATUS_04 + ", " +
		 * Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " +
		 * Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 +
		 * "))\n"); sql.append("group by TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID\n");
		 */

		sql.append("SELECT TVOD.DETAIL_ID,\n");
		sql.append("       TVOD.MATERIAL_ID,\n");
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER        TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       TT_VS_DLVRY        TVD,\n");
		sql.append("       TT_VS_DLVRY_DTL    TVDD\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVD.ORDER_ID\n");
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n");
		sql.append("   AND TVD.DELIVERY_STATUS IN\n");
		sql.append("       (" + Constant.DELIVERY_STATUS_04 + ", " + Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " + Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 + ")\n");
		sql.append("   AND TVO.ORDER_STATUS NOT IN (" + Constant.ORDER_STATUS_01 + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07 + ", " + Constant.ORDER_STATUS_10 + ")\n");
		if (null != dataflag && "1".equals(dataflag)) {
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}

		sql.append("   AND TVO.ORDER_STATUS NOT IN( " + Constant.ORDER_STATUS_01 + ")\n");
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
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

		sql.append(" GROUP BY TVOD.DETAIL_ID, TVOD.MATERIAL_ID\n");
		sql.append(")\n");

		// dao.update(sql.toString(), null);

		// sql = new StringBuffer();
		/*
		 * //TODO START 新增OTD自动匹配数量 2012-06-18 韩晓宇 sql.append("SELECT
		 * T.MATERIAL_CODE,\n"); sql.append(" T.MATERIAL_NAME,\n"); sql.append("
		 * T.ORDER_TYPE,\n"); sql.append(" T.ORDER_AMOUNT,\n"); sql.append("
		 * T.CHECK_AMOUNT,\n"); sql.append(" T.MATCH_AMOUNT,\n"); sql.append("
		 * T.BILLING_AMOUNT,\n"); sql.append(" COUNT(TOR.MATERIAL_CODE)
		 * OTD_AMOUNT\n"); sql.append(" FROM T_ORDERVIN_RELA TOR,\n");
		 * sql.append(" TT_VS_ORDER TVO2,\n"); sql.append(" TT_VS_ORDER_DETAIL
		 * TVOD2,\n"); sql.append(" TM_VHCL_MATERIAL TVM2,\n"); sql.append("
		 * TC_CODE TC2,\n"); sql.append(" (\n"); //TODO END
		 */
		sql.append("SELECT TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TC.CODE_DESC ORDER_TYPE,\n");
		sql.append("       SUM(NVL(TVOD.ORDER_AMOUNT, 0)) ORDER_AMOUNT,\n");
		sql.append("       SUM(NVL(TVOD.CHECK_AMOUNT, 0)) CHECK_AMOUNT,\n");
		sql.append("       SUM(NVL(TVOD.DELIVERY_AMOUNT, 0)) DELIVERY_AMOUNT,\n");
		sql.append("       SUM(NVL(A.BILLING_AMOUNT, 0)) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");
		sql.append("       TM_DEALER                TD1,\n");
		sql.append("       TM_DEALER                TD2,\n");
		sql.append("       TMP_VS_ORDERO1           A,\n");
		sql.append("       TC_CODE           		TC\n");
		sql.append(" WHERE TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND A.DETAIL_ID(+) = TVOD.DETAIL_ID\n");
		// sql.append(" AND TVDR.ORDER_ID=TVO.ORDER_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TD2.DEALER_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TD1.DEALER_ID\n");
		sql.append("   AND TVO.ORDER_TYPE = TC.CODE_ID\n");
		sql.append("   AND TVO.ORDER_STATUS NOT IN (" + Constant.ORDER_STATUS_01 + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07 + ", " + Constant.ORDER_STATUS_10 + ")\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND " + dealerSql);
		if (null != dataflag && "1".equals(dataflag)) {
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}

		sql.append("   AND TVO.ORDER_STATUS NOT IN( " + Constant.ORDER_STATUS_01 + ")\n");
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (!groupCode.equals("")) {
			/*
			 * String[] array = groupCode.split(","); sql.append(" AND
			 * TVMG.GROUP_CODE IN ("); for (int i = 0; i < array.length; i++) {
			 * sql.append("'" + array[i] + "'"); if (i != array.length - 1) {
			 * sql.append(","); } } sql.append(")\n");
			 */
			String[] array = groupCode.split(",");
			long len = array.length;
			sql.append("AND (TVMGR.GROUP_ID IN\n");
			sql.append("      (SELECT TMVMG.GROUP_ID\n");
			sql.append("         FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
			sql.append("        WHERE TMVMG.GROUP_LEVEL = 4\n");
			sql.append("        START WITH TMVMG.GROUP_ID = (SELECT TMVMG.GROUP_ID\n");
			sql.append("                                       FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
			sql.append("                                      WHERE GROUP_CODE = '" + array[0] + "')\n");
			sql.append("       CONNECT BY PRIOR TMVMG.GROUP_ID = TMVMG.PARENT_GROUP_ID)\n");
			for (int i = 1; i < len; i++) {
				sql.append("OR TVMGR.GROUP_ID IN\n");
				sql.append("      (SELECT TMVMG.GROUP_ID\n");
				sql.append("         FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
				sql.append("        WHERE TMVMG.GROUP_LEVEL = 4\n");
				sql.append("        START WITH TMVMG.GROUP_ID = (SELECT TMVMG.GROUP_ID\n");
				sql.append("                                       FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
				sql.append("                                      WHERE GROUP_CODE = '" + array[i] + "'\n");
				sql.append("                                      )\n");
				sql.append("       CONNECT BY PRIOR TMVMG.GROUP_ID = TMVMG.PARENT_GROUP_ID)\n");
			}
			sql.append(")\n");
		}
		if (!materialCode.equals("")) {
			String[] array = materialCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
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
		if (!dealerCode2.equals("")) {
			String[] array = dealerCode2.split(",");
			sql.append("   AND TD2.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		// start
		if (!dealerCodes.equals("")) {
			String[] array = dealerCodes.split(",");
			sql.append("   AND TD2.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		// end
		if (!orderType.equals("")) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		if (!orderStatus.equals("")) {
			sql.append("   AND TVO.ORDER_STATUS = " + orderStatus + "\n");
		}
		if (!orderNo.equals("")) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}

		sql.append(" GROUP BY TVM.MATERIAL_CODE, TVM.MATERIAL_NAME, TC.CODE_DESC\n");
		// sql.append("HAVING SUM(TVOD.ORDER_AMOUNT) > 0\n");

		sql.append(" ORDER BY TC.CODE_DESC");
		/*
		 * //TODO START 新增OTD自动匹配数量 2012-06-18 韩晓宇 sql.append(") T\n");
		 * sql.append(" WHERE T.MATERIAL_CODE = TOR.MATERIAL_CODE(+)\n");
		 * sql.append(" AND T.ORDER_TYPE = TC2.CODE_DESC(+)\n"); sql.append("
		 * AND TVO2.ORDER_TYPE = TC2.CODE_ID\n"); sql.append(" AND
		 * TVOD2.ORDER_ID(+) = TVO2.ORDER_ID\n"); sql.append(" AND
		 * TVOD2.MATERIAL_ID = TVM2.MATERIAL_ID\n"); sql.append(" AND
		 * TVM2.MATERIAL_CODE(+) = T.MATERIAL_CODE\n"); sql.append(" GROUP BY
		 * T.MATERIAL_CODE,\n"); sql.append(" T.MATERIAL_NAME,\n"); sql.append("
		 * T.ORDER_TYPE,\n"); sql.append(" T.ORDER_AMOUNT,\n"); sql.append("
		 * T.CHECK_AMOUNT,\n"); sql.append(" T.MATCH_AMOUNT,\n"); sql.append("
		 * T.BILLING_AMOUNT\n"); sql.append(" ORDER BY T.ORDER_TYPE\n"); //TODO
		 * END
		 */

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getOemSalesOrderTotalExportList");
		return list;
	}

	/**
	 * 销售订单明细查询（OEM）
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getOemSalesOrderDetailQueryList(Map<String, Object> map, int curPage, int pageSize) {
		String dealerCodes = (String) map.get("dealerCodes");
		String orderYear1 = (String) map.get("orderYear1");
		String orderWeek1 = (String) map.get("orderWeek1");
		String orderYear2 = (String) map.get("orderYear2");
		String orderWeek2 = (String) map.get("orderWeek2");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String materialCode = (String) map.get("materialCode");
		String dealerCode = (String) map.get("dealerCode");
		String orderType = (String) map.get("orderType");
		String orderStatus = (String) map.get("orderStatus");
		String orderNo = (String) map.get("orderNo");
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		orderWeek1 = orderWeek1.length() == 1 ? "0" + orderWeek1 : orderWeek1;
		orderWeek2 = orderWeek2.length() == 1 ? "0" + orderWeek2 : orderWeek2;
		String dataflag = (String) map.get("dataflag");
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");
		String groupCode2 = (String) map.get("groupCode2");

		String orgIdN = (String) map.get("orgIdN");

		StringBuffer sql = new StringBuffer("\n");
		String para = CommonDAO.getPara(Constant.CHANA_SYS.toString());

		// sql.append("INSERT INTO TMP_VS_ORDERO1\n");
		sql.append("WITH TMP_VS_ORDERO1 AS (\n");
		/*
		 * sql.append(" SELECT TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID,
		 * SUM(TTVDD.BILLING_AMOUNT) BILLING_AMOUNT\n"); sql.append(" FROM
		 * --TT_VS_DLVRY TTVD,\n"); sql.append(" (SELECT MATERIAL_ID,\n");
		 * sql.append(" DELIVERY_ID,\n"); sql.append(" SUM(DELIVERY_AMOUNT)
		 * BILLING_AMOUNT\n"); sql.append(" FROM TT_VS_DLVRY_DTL\n");
		 * sql.append(" GROUP BY MATERIAL_ID, DELIVERY_ID) TTVDD,\n");
		 * sql.append(" TT_VS_ORDER_DETAIL TTVOD\n"); sql.append(" WHERE
		 * TTVOD.MATERIAL_ID = TTVDD.MATERIAL_ID\n"); sql.append(" AND
		 * EXISTS\n"); sql.append(" (SELECT 1\n"); sql.append(" FROM TT_VS_DLVRY
		 * TTVD\n"); sql.append(" WHERE TTVD.DELIVERY_ID =
		 * TTVDD.DELIVERY_ID\n"); sql.append(" AND TTVOD.ORDER_ID =
		 * TTVD.ORDER_ID\n"); sql.append(" AND TTVD.DELIVERY_STATUS IN\n");
		 * sql.append(" (" + Constant.DELIVERY_STATUS_04 + ", " +
		 * Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " +
		 * Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 +
		 * "))\n"); sql.append("group by TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID\n");
		 */

		sql.append("SELECT TVOD.DETAIL_ID,\n");
		sql.append("       TVOD.MATERIAL_ID,\n");
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER        TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       TT_VS_DLVRY        TVD,\n");
		sql.append("       TT_VS_DLVRY_DTL    TVDD\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVD.ORDER_ID\n");
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n");
		sql.append("   AND TVD.DELIVERY_STATUS IN\n");
		sql.append("       (" + Constant.DELIVERY_STATUS_04 + ", " + Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " + Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 + ")\n");
		sql.append("   AND TVO.ORDER_STATUS NOT IN (" + Constant.ORDER_STATUS_01 + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07 + ", " + Constant.ORDER_STATUS_10 + ")\n");
		if (null != dataflag && "1".equals(dataflag)) {
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}

		sql.append("   AND TVO.ORDER_STATUS NOT IN( " + Constant.ORDER_STATUS_01 + ")\n");
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
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

		sql.append(" GROUP BY TVOD.DETAIL_ID, TVOD.MATERIAL_ID\n");

		sql.append(")\n");

		// dao.update(sql.toString(), null);

		// sql = new StringBuffer();

		if (Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
			sql.append("SELECT T.ORDER_ORG_NAME,\n");
			sql.append("       T.DEALER_CODE,\n");
			sql.append("       T.BILLING_ORG_NAME,\n");
			sql.append("       T.ORDER_NO,\n");
			sql.append("       T.RAISE_DATE,\n");
			sql.append("       T.ORDER_TYPE,\n");
			sql.append("       T.ORDER_STATUS,\n");
			sql.append("       T.DELIVERY_ADDRESS,\n");
			sql.append("       T.MATERIAL_id,\n");
			sql.append("       T.MATERIAL_CODE,\n");
			sql.append("       T.DELIVERY_TYPE,\n");
			sql.append("       T.ORDER_AMOUNT,\n");
			sql.append("       T.CHECK_AMOUNT,\n");
			sql.append("       T.DELIVERY_AMOUNT,\n");
			sql.append("       T.BILLING_AMOUNT,\n");
			sql.append("	   COUNT(TOR.MATERIAL_CODE) OTD_AMOUNT\n");
			sql.append("       FROM T_ORDERVIN_RELA          TOR,\n (");
		} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
		} else {
			throw new RuntimeException("判断当前系统的系统参数错误！");
		}
		// 2012-06-15 新增 韩晓宇
		/*
		 * sql.append("SELECT T.ORDER_ORG_NAME,\n"); sql.append("
		 * T.DEALER_CODE,\n"); sql.append(" T.BILLING_ORG_NAME,\n");
		 * sql.append(" T.ORDER_NO,\n"); sql.append(" T.RAISE_DATE,\n");
		 * sql.append(" T.ORDER_TYPE,\n"); sql.append(" T.ORDER_STATUS,\n");
		 * sql.append(" T.DELIVERY_ADDRESS,\n"); sql.append("
		 * T.MATERIAL_id,\n"); sql.append(" T.MATERIAL_CODE,\n"); sql.append("
		 * T.DELIVERY_TYPE,\n"); sql.append(" T.ORDER_AMOUNT,\n"); sql.append("
		 * T.CHECK_AMOUNT,\n"); sql.append(" T.MATCH_AMOUNT,\n"); sql.append("
		 * T.BILLING_AMOUNT,\n"); sql.append(" COUNT(TOR.MATERIAL_CODE)
		 * OTD_AMOUNT\n"); sql.append(" FROM T_ORDERVIN_RELA TOR,\n (");
		 */

		// 以前SQL
		sql.append("SELECT TD1.DEALER_SHORTNAME ORDER_ORG_NAME,TD1.DEALER_CODE DEALER_CODE,\n");
		sql.append("       TD2.DEALER_SHORTNAME BILLING_ORG_NAME,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       TVO.ORDER_STATUS,\n");
		sql.append("       TVA.ADDRESS DELIVERY_ADDRESS,tvm.MATERIAL_code,tvm.MATERIAL_id,\n");
		sql.append("       TVO.DELIVERY_TYPE DELIVERY_TYPE,\n");
		/* sql.append(" COUNT(TOR.MATERIAL_CODE) OTD_AMOUNT,\n"); */
		sql.append("	   SUM(nvl(TVOD.ORDER_AMOUNT, 0)) ORDER_AMOUNT,\n");
		sql.append("       SUM(nvl(TVOD.CHECK_AMOUNT, 0)) CHECK_AMOUNT,\n");
		sql.append("       SUM(nvl(TVOD.DELIVERY_AMOUNT, 0)) DELIVERY_AMOUNT,\n");
		sql.append("       SUM(nvl(A.BILLING_AMOUNT, 0)) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");
		sql.append("       TM_DEALER                TD1,\n");
		sql.append("       TM_DEALER                TD2,TM_VS_ADDRESS TVA,\n");
		sql.append("	   TMP_VS_ORDERO1			A\n");
		sql.append(" WHERE TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		// sql.append(" AND TVDR.ORDER_ID(+)=TVO.ORDER_ID\n");
		sql.append("   AND A.DETAIL_ID(+) = TVOD.DETAIL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TD1.DEALER_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TD2.DEALER_ID\n");
		sql.append("   AND TVA.ID(+)=TVO.DELIVERY_ADDRESS");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND " + dealerSql);

		if (null != dataflag && "1".equals(dataflag)) {
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}
		sql.append("   AND TVO.ORDER_STATUS NOT IN( " + Constant.ORDER_STATUS_01 + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07 + ", " + Constant.ORDER_STATUS_10 + ")\n");
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (!groupCode.equals("")) {
			/*
			 * String[] array = groupCode.split(","); sql.append(" AND
			 * TVMG.GROUP_CODE IN ("); for (int i = 0; i < array.length; i++) {
			 * sql.append("'" + array[i] + "'"); if (i != array.length - 1) {
			 * sql.append(","); } } sql.append(")\n");
			 */
			String[] array = groupCode.split(",");
			long len = array.length;
			sql.append("AND (TVMGR.GROUP_ID IN\n");
			sql.append("      (SELECT TMVMG.GROUP_ID\n");
			sql.append("         FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
			sql.append("        WHERE TMVMG.GROUP_LEVEL = 4\n");
			sql.append("        START WITH TMVMG.GROUP_ID = (SELECT TMVMG.GROUP_ID\n");
			sql.append("                                       FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
			sql.append("                                      WHERE GROUP_CODE = '" + array[0] + "')\n");
			sql.append("       CONNECT BY PRIOR TMVMG.GROUP_ID = TMVMG.PARENT_GROUP_ID)\n");
			for (int i = 1; i < len; i++) {
				sql.append("OR TVMGR.GROUP_ID IN\n");
				sql.append("      (SELECT TMVMG.GROUP_ID\n");
				sql.append("         FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
				sql.append("        WHERE TMVMG.GROUP_LEVEL = 4\n");
				sql.append("        START WITH TMVMG.GROUP_ID = (SELECT TMVMG.GROUP_ID\n");
				sql.append("                                       FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
				sql.append("                                      WHERE GROUP_CODE = '" + array[i] + "'\n");
				sql.append("                                      )\n");
				sql.append("       CONNECT BY PRIOR TMVMG.GROUP_ID = TMVMG.PARENT_GROUP_ID)\n");
			}
			sql.append(")\n");
		}
		if (!materialCode.equals("")) {
			String[] array = materialCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
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
		if (!dealerCodes.equals("")) {
			String[] array = dealerCodes.split(",");
			sql.append("   AND TD1.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (!groupCode2.equals("")) {
			String[] array = groupCode2.split(",");
			sql.append("   AND TD2.DEALER_CODE IN (");
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

		if (!CommonUtils.isNullString(orgIdN)) {
			sql.append("   AND exists (select 1 from vw_org_dealer vod where vod.dealer_id = td2.dealer_id and vod.root_org_id = " + orgIdN + ")\n");
		}

		sql.append(" GROUP BY TD1.DEALER_SHORTNAME,tvm.MATERIAL_code,tvm.MATERIAL_id,\n");
		sql.append("          TD2.DEALER_SHORTNAME,\n");
		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD'),\n");
		sql.append("          TVO.ORDER_TYPE,\n");
		sql.append("          TVO.ORDER_STATUS,TVA.ADDRESS,TVO.DELIVERY_TYPE,TD1.DEALER_CODE\n");
		// sql.append("HAVING SUM(TVOD.ORDER_AMOUNT) > 0\n");
		sql.append(" ORDER BY TVO.ORDER_NO DESC");

		// 2012-06-15 新增SQL 韩晓宇
		if (Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
			sql.append(") T\n");
			sql.append(" WHERE T.MATERIAL_CODE = TOR.MATERIAL_CODE(+)\n");
			sql.append(" AND TOR.ORDER_NO(+) = T.ORDER_NO\n");
			sql.append(" GROUP BY T.ORDER_ORG_NAME,\n");
			sql.append("       T.DEALER_CODE,\n");
			sql.append("       T.BILLING_ORG_NAME,\n");
			sql.append("       T.ORDER_NO,\n");
			sql.append("       T.RAISE_DATE,\n");
			sql.append("       T.ORDER_TYPE,\n");
			sql.append("       T.ORDER_STATUS,\n");
			sql.append("       T.DELIVERY_ADDRESS,\n");
			sql.append("       T.MATERIAL_id,\n");
			sql.append("       T.MATERIAL_CODE,\n");
			sql.append("       T.DELIVERY_TYPE,\n");
			sql.append("       T.ORDER_AMOUNT,\n");
			sql.append("       T.CHECK_AMOUNT,\n");
			sql.append("       T.DELIVERY_AMOUNT,\n");
			sql.append("       T.BILLING_AMOUNT\n");
			sql.append("       ORDER BY T.ORDER_NO\n");
		} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
		} else {
			throw new RuntimeException("判断当前系统的系统参数错误！");
		}
		/*
		 * sql.append(") T\n"); sql.append(" WHERE T.MATERIAL_CODE =
		 * TOR.MATERIAL_CODE(+)\n"); sql.append(" AND TOR.ORDER_NO(+) =
		 * T.ORDER_NO\n"); sql.append(" GROUP BY T.ORDER_ORG_NAME,\n");
		 * sql.append(" T.DEALER_CODE,\n"); sql.append("
		 * T.BILLING_ORG_NAME,\n"); sql.append(" T.ORDER_NO,\n"); sql.append("
		 * T.RAISE_DATE,\n"); sql.append(" T.ORDER_TYPE,\n"); sql.append("
		 * T.ORDER_STATUS,\n"); sql.append(" T.DELIVERY_ADDRESS,\n");
		 * sql.append(" T.MATERIAL_id,\n"); sql.append(" T.MATERIAL_CODE,\n");
		 * sql.append(" T.DELIVERY_TYPE,\n"); sql.append(" T.ORDER_AMOUNT,\n");
		 * sql.append(" T.CHECK_AMOUNT,\n"); sql.append(" T.MATCH_AMOUNT,\n");
		 * sql.append(" T.BILLING_AMOUNT\n"); sql.append(" ORDER BY
		 * T.ORDER_NO\n");
		 */

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getOemSalesOrderDetailQueryList", pageSize, curPage);
		return ps;
	}

	public List<Map<String, Object>> getOemSalesOrderDetailExportList(Map<String, Object> map) {
		String dealerCodes = (String) map.get("dealerCodes");
		String orderYear1 = (String) map.get("orderYear1");
		String orderWeek1 = (String) map.get("orderWeek1");
		String orderYear2 = (String) map.get("orderYear2");
		String orderWeek2 = (String) map.get("orderWeek2");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String materialCode = (String) map.get("materialCode");
		String dealerCode = (String) map.get("dealerCode");
		String orderType = (String) map.get("orderType");
		String orderStatus = (String) map.get("orderStatus");
		String orderNo = (String) map.get("orderNo");

		String orgIdN = (String) map.get("orgIdN");

		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		orderWeek1 = orderWeek1.length() == 1 ? "0" + orderWeek1 : orderWeek1;
		orderWeek2 = orderWeek2.length() == 1 ? "0" + orderWeek2 : orderWeek2;
		String dataflag = (String) map.get("dataflag");
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");
		String groupCode2 = (String) map.get("groupCode2");

		StringBuffer sql = new StringBuffer("\n");
		String para = CommonDAO.getPara(Constant.CHANA_SYS.toString());

		// sql.append("INSERT INTO TMP_VS_ORDERO1\n");
		sql.append("WITH TMP_VS_ORDERO1 AS (\n");
		/*
		 * sql.append(" SELECT TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID,
		 * SUM(TTVDD.BILLING_AMOUNT) BILLING_AMOUNT\n"); sql.append(" FROM
		 * --TT_VS_DLVRY TTVD,\n"); sql.append(" (SELECT MATERIAL_ID,\n");
		 * sql.append(" DELIVERY_ID,\n"); sql.append(" SUM(DELIVERY_AMOUNT)
		 * BILLING_AMOUNT\n"); sql.append(" FROM TT_VS_DLVRY_DTL\n");
		 * sql.append(" GROUP BY MATERIAL_ID, DELIVERY_ID) TTVDD,\n");
		 * sql.append(" TT_VS_ORDER_DETAIL TTVOD\n"); sql.append(" WHERE
		 * TTVOD.MATERIAL_ID = TTVDD.MATERIAL_ID\n"); sql.append(" AND
		 * EXISTS\n"); sql.append(" (SELECT 1\n"); sql.append(" FROM TT_VS_DLVRY
		 * TTVD\n"); sql.append(" WHERE TTVD.DELIVERY_ID =
		 * TTVDD.DELIVERY_ID\n"); sql.append(" AND TTVOD.ORDER_ID =
		 * TTVD.ORDER_ID\n"); sql.append(" AND TTVD.DELIVERY_STATUS IN\n");
		 * sql.append(" (" + Constant.DELIVERY_STATUS_04 + ", " +
		 * Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " +
		 * Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 +
		 * "))\n"); sql.append("group by TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID\n");
		 */

		sql.append("SELECT TVOD.DETAIL_ID,\n");
		sql.append("       TVOD.MATERIAL_ID,\n");
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER        TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       TT_VS_DLVRY        TVD,\n");
		sql.append("       TT_VS_DLVRY_DTL    TVDD\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVD.ORDER_ID\n");
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n");
		sql.append("   AND TVD.DELIVERY_STATUS IN\n");
		sql.append("       (" + Constant.DELIVERY_STATUS_04 + ", " + Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " + Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 + ")\n");
		sql.append("   AND TVO.ORDER_STATUS NOT IN (" + Constant.ORDER_STATUS_01 + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07 + ", " + Constant.ORDER_STATUS_10 + ")\n");
		if (null != dataflag && "1".equals(dataflag)) {
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}

		sql.append("   AND TVO.ORDER_STATUS NOT IN( " + Constant.ORDER_STATUS_01 + ")\n");
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
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

		sql.append(" GROUP BY TVOD.DETAIL_ID, TVOD.MATERIAL_ID\n");

		sql.append(")\n");

		// dao.update(sql.toString(), null);

		// sql = new StringBuffer();

		// TODO START 新增OTD自动匹配数量 2012-06-18 韩晓宇
		if (Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
			sql.append("SELECT T.ORDER_ORG_NAME,\n");
			sql.append("       T.DEALER_CODE,\n");
			sql.append("       T.BILLING_ORG_NAME,\n");
			sql.append("       T.ORDER_NO,\n");
			sql.append("       T.RAISE_DATE,\n");
			sql.append("       T.ORDER_TYPE,\n");
			sql.append("       T.ORDER_STATUS,\n");
			sql.append("       T.DELIVERY_ADDRESS,\n");
			sql.append("       T.MATERIAL_CODE,\n");
			sql.append("       T.DELIVERY_TYPE,\n");
			sql.append("       T.ORDER_AMOUNT,\n");
			sql.append("       T.CHECK_AMOUNT,\n");
			sql.append("       T.DELIVERY_AMOUNT,\n");
			sql.append("       T.BILLING_AMOUNT,\n");
			sql.append("     COUNT(TOR.MATERIAL_CODE) OTD_AMOUNT\n");
			sql.append("       FROM T_ORDERVIN_RELA          TOR,\n");
			sql.append(" (\n");
		} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
		} else {
			throw new RuntimeException("判断当前系统的系统参数错误！");
		}
		/*
		 * sql.append("SELECT T.ORDER_ORG_NAME,\n"); sql.append("
		 * T.DEALER_CODE,\n"); sql.append(" T.BILLING_ORG_NAME,\n");
		 * sql.append(" T.ORDER_NO,\n"); sql.append(" T.RAISE_DATE,\n");
		 * sql.append(" T.ORDER_TYPE,\n"); sql.append(" T.ORDER_STATUS,\n");
		 * sql.append(" T.DELIVERY_ADDRESS,\n"); sql.append("
		 * T.MATERIAL_CODE,\n"); sql.append(" T.DELIVERY_TYPE,\n"); sql.append("
		 * T.ORDER_AMOUNT,\n"); sql.append(" T.CHECK_AMOUNT,\n"); sql.append("
		 * T.MATCH_AMOUNT,\n"); sql.append(" T.BILLING_AMOUNT,\n"); sql.append("
		 * COUNT(TOR.MATERIAL_CODE) OTD_AMOUNT\n"); sql.append(" FROM
		 * T_ORDERVIN_RELA TOR,\n"); sql.append(" (\n");
		 */
		// TODO END
		sql.append("SELECT TD1.DEALER_SHORTNAME ORDER_ORG_NAME,TD1.DEALER_CODE DEALER_CODE,\n");
		sql.append("       TD2.DEALER_SHORTNAME BILLING_ORG_NAME,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TC1.CODE_DESC ORDER_TYPE,\n");
		sql.append("       TC2.CODE_DESC ORDER_STATUS,\n");
		sql.append("       TVA.ADDRESS DELIVERY_ADDRESS,\n");
		sql.append("       TC3.CODE_DESC DELIVERY_TYPE,tvm.MATERIAL_code,\n");
		sql.append("	   SUM(nvl(TVOD.ORDER_AMOUNT, 0)) ORDER_AMOUNT,\n");
		sql.append("       SUM(nvl(TVOD.CHECK_AMOUNT, 0)) CHECK_AMOUNT,\n");
		sql.append("       SUM(nvl(TVOD.DELIVERY_AMOUNT, 0)) DELIVERY_AMOUNT,\n");
		sql.append("       SUM(nvl(A.BILLING_AMOUNT, 0)) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");
		sql.append("       TM_DEALER                TD1,\n");
		sql.append("       TM_DEALER                TD2,TM_VS_ADDRESS TVA,\n");
		sql.append("       TMP_VS_ORDERO1 			A,\n");
		sql.append("       TC_CODE 				    TC1,\n");
		sql.append("       TC_CODE 				    TC2,\n");
		sql.append("       TC_CODE 				    TC3\n");
		sql.append(" WHERE TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		// sql.append(" AND TVDR.ORDER_ID=TVO.ORDER_ID\n");
		sql.append("   AND A.DETAIL_ID(+) = TVOD.DETAIL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TD1.DEALER_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TD2.DEALER_ID\n");
		sql.append("   AND TVA.ID(+)=TVO.DELIVERY_ADDRESS");
		sql.append("   AND TVO.ORDER_TYPE = TC1.CODE_ID\n");
		sql.append("   AND TVO.ORDER_STATUS = TC2.CODE_ID\n");
		sql.append("   AND TVO.DELIVERY_TYPE = TC3.CODE_ID(+)\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND " + dealerSql);

		if (null != dataflag && "1".equals(dataflag)) {
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}
		sql.append("   AND TVO.ORDER_STATUS NOT IN(" + Constant.ORDER_STATUS_01 + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07 + ", " + Constant.ORDER_STATUS_10 + ")\n");
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (!groupCode.equals("")) {
			/*
			 * String[] array = groupCode.split(","); sql.append(" AND
			 * TVMG.GROUP_CODE IN ("); for (int i = 0; i < array.length; i++) {
			 * sql.append("'" + array[i] + "'"); if (i != array.length - 1) {
			 * sql.append(","); } } sql.append(")\n");
			 */
			String[] array = groupCode.split(",");
			long len = array.length;
			sql.append("AND (TVMGR.GROUP_ID IN\n");
			sql.append("      (SELECT TMVMG.GROUP_ID\n");
			sql.append("         FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
			sql.append("        WHERE TMVMG.GROUP_LEVEL = 4\n");
			sql.append("        START WITH TMVMG.GROUP_ID = (SELECT TMVMG.GROUP_ID\n");
			sql.append("                                       FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
			sql.append("                                      WHERE GROUP_CODE = '" + array[0] + "')\n");
			sql.append("       CONNECT BY PRIOR TMVMG.GROUP_ID = TMVMG.PARENT_GROUP_ID)\n");
			for (int i = 1; i < len; i++) {
				sql.append("OR TVMGR.GROUP_ID IN\n");
				sql.append("      (SELECT TMVMG.GROUP_ID\n");
				sql.append("         FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
				sql.append("        WHERE TMVMG.GROUP_LEVEL = 4\n");
				sql.append("        START WITH TMVMG.GROUP_ID = (SELECT TMVMG.GROUP_ID\n");
				sql.append("                                       FROM TM_VHCL_MATERIAL_GROUP TMVMG\n");
				sql.append("                                      WHERE GROUP_CODE = '" + array[i] + "'\n");
				sql.append("                                      )\n");
				sql.append("       CONNECT BY PRIOR TMVMG.GROUP_ID = TMVMG.PARENT_GROUP_ID)\n");
			}
			sql.append(")\n");
		}
		if (!materialCode.equals("")) {
			String[] array = materialCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
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
		if (!dealerCodes.equals("")) {
			String[] array = dealerCodes.split(",");
			sql.append("   AND TD1.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (!groupCode2.equals("")) {
			String[] array = groupCode2.split(",");
			sql.append("   AND TD2.DEALER_CODE IN (");
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

		if (!CommonUtils.isNullString(orgIdN)) {
			sql.append("   AND exists (select 1 from vw_org_dealer vod where vod.dealer_id = td2.dealer_id and vod.root_org_id = " + orgIdN + ")\n");
		}

		sql.append(" GROUP BY TD1.DEALER_SHORTNAME,tvm.MATERIAL_code,\n");
		sql.append("          TD2.DEALER_SHORTNAME,\n");
		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD'),\n");
		sql.append("          TC1.CODE_DESC,\n");
		sql.append("          TC2.CODE_DESC,TVA.ADDRESS,TC3.CODE_DESC,TD1.DEALER_CODE\n");
		// sql.append("HAVING SUM(TVOD.ORDER_AMOUNT) > 0\n");
		sql.append(" ORDER BY TVO.ORDER_NO DESC");

		// TODO START 新增OTD自动匹配数量 2012-06-18 韩晓宇
		if (Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
			sql.append(") T\n");
			sql.append(" WHERE T.MATERIAL_CODE = TOR.MATERIAL_CODE(+)\n");
			sql.append(" AND TOR.ORDER_NO(+) = T.ORDER_NO\n");
			sql.append(" GROUP BY T.ORDER_ORG_NAME,\n");
			sql.append("       T.DEALER_CODE,\n");
			sql.append("       T.BILLING_ORG_NAME,\n");
			sql.append("       T.ORDER_NO,\n");
			sql.append("       T.RAISE_DATE,\n");
			sql.append("       T.ORDER_TYPE,\n");
			sql.append("       T.ORDER_STATUS,\n");
			sql.append("       T.DELIVERY_ADDRESS,\n");
			sql.append("       T.MATERIAL_CODE,\n");
			sql.append("       T.DELIVERY_TYPE,\n");
			sql.append("       T.ORDER_AMOUNT,\n");
			sql.append("       T.CHECK_AMOUNT,\n");
			sql.append("       T.DELIVERY_AMOUNT,\n");
			sql.append("       T.BILLING_AMOUNT\n");
			sql.append("       ORDER BY T.ORDER_NO\n");
		} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
		} else {
			throw new RuntimeException("判断当前系统的系统参数错误！");
		}
		/*
		 * sql.append(") T\n"); sql.append(" WHERE T.MATERIAL_CODE =
		 * TOR.MATERIAL_CODE(+)\n"); sql.append(" AND TOR.ORDER_NO(+) =
		 * T.ORDER_NO\n"); sql.append(" GROUP BY T.ORDER_ORG_NAME,\n");
		 * sql.append(" T.DEALER_CODE,\n"); sql.append("
		 * T.BILLING_ORG_NAME,\n"); sql.append(" T.ORDER_NO,\n"); sql.append("
		 * T.RAISE_DATE,\n"); sql.append(" T.ORDER_TYPE,\n"); sql.append("
		 * T.ORDER_STATUS,\n"); sql.append(" T.DELIVERY_ADDRESS,\n");
		 * sql.append(" T.MATERIAL_CODE,\n"); sql.append(" T.DELIVERY_TYPE,\n");
		 * sql.append(" T.ORDER_AMOUNT,\n"); sql.append(" T.CHECK_AMOUNT,\n");
		 * sql.append(" T.MATCH_AMOUNT,\n"); sql.append(" T.BILLING_AMOUNT\n");
		 * sql.append(" ORDER BY T.ORDER_NO\n");
		 */
		// TODO END
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getOemSalesOrderDetailExportList");
		return list;
	}

	/**
	 * 销售订单汇总查询（DEALER）
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getDealerSalesOrderTotalQueryList(Map<String, Object> map, int curPage, int pageSize) {

		String orderYear1 = (String) map.get("orderYear1");
		String orderWeek1 = (String) map.get("orderWeek1");
		String orderYear2 = (String) map.get("orderYear2");
		String orderWeek2 = (String) map.get("orderWeek2");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String materialCode = (String) map.get("materialCode");
		String orderType = (String) map.get("orderType");
		String orderStatus = (String) map.get("orderStatus");
		String orderNo = (String) map.get("orderNo");
		String companyId = (String) map.get("companyId");
		String dealerSql = (String) map.get("dealerSql");
		String areaIds = (String) map.get("areaIds");
		orderWeek1 = orderWeek1.length() == 1 ? "0" + orderWeek1 : orderWeek1;
		orderWeek2 = orderWeek2.length() == 1 ? "0" + orderWeek2 : orderWeek2;
		String dataflag = (String) map.get("dataflag");
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");

		StringBuffer sql = new StringBuffer();

		// sql.append("INSERT INTO TMP_VS_ORDERO1\n");
		sql.append("WITH TMP_VS_ORDERO1 AS (\n");
		/*
		 * sql.append(" SELECT TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID,
		 * SUM(TTVDD.BILLING_AMOUNT) BILLING_AMOUNT\n"); sql.append(" FROM
		 * --TT_VS_DLVRY TTVD,\n"); sql.append(" (SELECT MATERIAL_ID,\n");
		 * sql.append(" DELIVERY_ID,\n"); sql.append(" SUM(DELIVERY_AMOUNT)
		 * BILLING_AMOUNT\n"); sql.append(" FROM TT_VS_DLVRY_DTL\n");
		 * sql.append(" GROUP BY MATERIAL_ID, DELIVERY_ID) TTVDD,\n");
		 * sql.append(" TT_VS_ORDER_DETAIL TTVOD\n"); sql.append(" WHERE
		 * TTVOD.MATERIAL_ID = TTVDD.MATERIAL_ID\n"); sql.append(" AND
		 * EXISTS\n"); sql.append(" (SELECT 1\n"); sql.append(" FROM TT_VS_DLVRY
		 * TTVD\n"); sql.append(" WHERE TTVD.DELIVERY_ID =
		 * TTVDD.DELIVERY_ID\n"); sql.append(" AND TTVOD.ORDER_ID =
		 * TTVD.ORDER_ID\n"); sql.append(" AND TTVD.DELIVERY_STATUS IN\n");
		 * sql.append(" (" + Constant.DELIVERY_STATUS_04 + ", " +
		 * Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " +
		 * Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 +
		 * "))\n"); sql.append("group by TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID\n");
		 */

		sql.append("SELECT TVOD.DETAIL_ID,\n");
		sql.append("       TVOD.MATERIAL_ID,\n");
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER        TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       TT_VS_DLVRY        TVD,\n");
		sql.append("       TT_VS_DLVRY_DTL    TVDD\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVD.ORDER_ID\n");
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n");
		sql.append("   AND TVD.DELIVERY_STATUS IN\n");
		sql.append("       (" + Constant.DELIVERY_STATUS_04 + ", " + Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " + Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 + ")\n");
		sql.append("   AND TVO.ORDER_STATUS NOT IN (" + Constant.ORDER_STATUS_01 + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07 + ", " + Constant.ORDER_STATUS_10 + ")\n");
		if (null != dataflag && "1".equals(dataflag)) {
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}

		sql.append("   AND TVO.ORDER_STATUS NOT IN( " + Constant.ORDER_STATUS_01 + ")\n");
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
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

		sql.append(" GROUP BY TVOD.DETAIL_ID, TVOD.MATERIAL_ID\n");

		sql.append(")\n");

		// dao.update(sql.toString(), null);

		// sql = new StringBuffer();

		sql.append("SELECT TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       SUM(TVOD.ORDER_AMOUNT) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.DELIVERY_AMOUNT), 0) DELIVERY_AMOUNT,\n");
		sql.append("	   SUM(NVL(A.BILLING_AMOUNT, 0)) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");
		sql.append("       TMP_VS_ORDERO1 A\n");
		sql.append(" WHERE TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND A.DETAIL_ID(+) = TVOD.DETAIL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		// sql.append(" AND TVO.ORDER_STATUS NOT IN ('" +
		// Constant.ORDER_STATUS_04 + "','" + Constant.ORDER_STATUS_06 +
		// "')\n");
		sql.append("   AND TVO.ORDER_STATUS NOT IN (" + Constant.ORDER_STATUS_01 + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07 + ", " + Constant.ORDER_STATUS_10 + ")\n");
		sql.append("   AND TVO.ORDER_ORG_ID IN (" + dealerSql + ")\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		if (null != dataflag && "1".equals(dataflag)) {
			addYearAndWeekQueryCondition(sql, orderYear1, orderWeek1, orderYear2, orderWeek2);
			// sql.append(" AND TVO.ORDER_YEAR ||
			// DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			// sql.append(" 1,\n");
			// sql.append(" '0' || TVO.ORDER_WEEK,\n");
			// sql.append(" TVO.ORDER_WEEK) >= "+ orderYear1 + orderWeek1 +
			// "\n");
			// sql.append(" AND TVO.ORDER_YEAR ||
			// DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			// sql.append(" 1,\n");
			// sql.append(" '0' || TVO.ORDER_WEEK,\n");
			// sql.append(" TVO.ORDER_WEEK) <= "+ orderYear2 + orderWeek2 +
			// "\n");
			// sql.append(" AND TVO.ORDER_STATUS NOT IN( "+
			// Constant.ORDER_STATUS_01 + ")\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}

		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
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
		if (!materialCode.equals("")) {
			String[] array = materialCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (");
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
		sql.append(" GROUP BY TVM.MATERIAL_CODE, TVM.MATERIAL_NAME, TVO.ORDER_TYPE\n");
		// /sql.append("HAVING SUM(TVOD.ORDER_AMOUNT) > 0\n");
		sql.append(" ORDER BY TVO.ORDER_TYPE");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getDealerSalesOrderTotalQueryList", pageSize, curPage);
		return ps;
	}

	/**
	 * 销售订单汇总查询导出（DEALER）
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> getDealerSalesOrderTotalExportList(Map<String, Object> map) {

		String orderYear1 = (String) map.get("orderYear1");
		String orderWeek1 = (String) map.get("orderWeek1");
		String orderYear2 = (String) map.get("orderYear2");
		String orderWeek2 = (String) map.get("orderWeek2");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String materialCode = (String) map.get("materialCode");
		String orderType = (String) map.get("orderType");
		String orderStatus = (String) map.get("orderStatus");
		String orderNo = (String) map.get("orderNo");
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		String dataflag = (String) map.get("dataflag");
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");
		orderWeek1 = orderWeek1.length() == 1 ? "0" + orderWeek1 : orderWeek1;
		orderWeek2 = orderWeek2.length() == 1 ? "0" + orderWeek2 : orderWeek2;

		StringBuffer sql = new StringBuffer();

		// sql.append("INSERT INTO TMP_VS_ORDERO1\n");
		sql.append("WITH TMP_VS_ORDERO1 AS (\n");
		/*
		 * sql.append(" SELECT TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID,
		 * SUM(TTVDD.BILLING_AMOUNT) BILLING_AMOUNT\n"); sql.append(" FROM
		 * --TT_VS_DLVRY TTVD,\n"); sql.append(" (SELECT MATERIAL_ID,\n");
		 * sql.append(" DELIVERY_ID,\n"); sql.append(" SUM(DELIVERY_AMOUNT)
		 * BILLING_AMOUNT\n"); sql.append(" FROM TT_VS_DLVRY_DTL\n");
		 * sql.append(" GROUP BY MATERIAL_ID, DELIVERY_ID) TTVDD,\n");
		 * sql.append(" TT_VS_ORDER_DETAIL TTVOD\n"); sql.append(" WHERE
		 * TTVOD.MATERIAL_ID = TTVDD.MATERIAL_ID\n"); sql.append(" AND
		 * EXISTS\n"); sql.append(" (SELECT 1\n"); sql.append(" FROM TT_VS_DLVRY
		 * TTVD\n"); sql.append(" WHERE TTVD.DELIVERY_ID =
		 * TTVDD.DELIVERY_ID\n"); sql.append(" AND TTVOD.ORDER_ID =
		 * TTVD.ORDER_ID\n"); sql.append(" AND TTVD.DELIVERY_STATUS IN\n");
		 * sql.append(" (" + Constant.DELIVERY_STATUS_04 + ", " +
		 * Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " +
		 * Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 +
		 * "))\n"); sql.append("group by TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID\n");
		 */

		sql.append("SELECT TVOD.DETAIL_ID,\n");
		sql.append("       TVOD.MATERIAL_ID,\n");
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER        TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       TT_VS_DLVRY        TVD,\n");
		sql.append("       TT_VS_DLVRY_DTL    TVDD\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVD.ORDER_ID\n");
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n");
		sql.append("   AND TVD.DELIVERY_STATUS IN\n");
		sql.append("       (" + Constant.DELIVERY_STATUS_04 + ", " + Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " + Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 + ")\n");
		sql.append("   AND TVO.ORDER_STATUS NOT IN (" + Constant.ORDER_STATUS_01 + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07 + ", " + Constant.ORDER_STATUS_10 + ")\n");
		if (null != dataflag && "1".equals(dataflag)) {
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}

		sql.append("   AND TVO.ORDER_STATUS NOT IN( " + Constant.ORDER_STATUS_01 + ")\n");
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
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

		sql.append(" GROUP BY TVOD.DETAIL_ID, TVOD.MATERIAL_ID\n");

		sql.append(")\n");

		// dao.update(sql.toString(), null);

		// sql = new StringBuffer();

		sql.append("SELECT TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TC.CODE_DESC ORDER_TYPE,\n");
		sql.append("       SUM(TVOD.ORDER_AMOUNT) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.DELIVERY_AMOUNT), 0) DELIVERY_AMOUNT,\n");
		sql.append("	   SUM(NVL(A.BILLING_AMOUNT, 0)) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");
		sql.append("       TMP_VS_ORDERO1		    A,\n");
		sql.append("       TC_CODE		    	    TC\n");
		sql.append(" WHERE TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND A.DETAIL_ID(+) = TVOD.DETAIL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_TYPE = TC.CODE_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID IN (" + dealerSql + ")\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		if (null != dataflag && "1".equals(dataflag)) {
			addYearAndWeekQueryCondition(sql, orderYear1, orderWeek1, orderYear2, orderWeek2);
			// sql.append(" AND TVO.ORDER_YEAR ||
			// DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			// sql.append(" 1,\n");
			// sql.append(" '0' || TVO.ORDER_WEEK,\n");
			// sql.append(" TVO.ORDER_WEEK) >= "+ orderYear1 + orderWeek1 +
			// "\n");
			// sql.append(" AND TVO.ORDER_YEAR ||
			// DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			// sql.append(" 1,\n");
			// sql.append(" '0' || TVO.ORDER_WEEK,\n");
			// sql.append(" TVO.ORDER_WEEK) <= "+ orderYear2 + orderWeek2 +
			// "\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}
		sql.append("   AND TVO.ORDER_STATUS NOT IN( " + Constant.ORDER_STATUS_01 + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07 + ", " + Constant.ORDER_STATUS_10 + ")\n");
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
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
		if (!materialCode.equals("")) {
			String[] array = materialCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (");
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
		sql.append(" GROUP BY TVM.MATERIAL_CODE, TVM.MATERIAL_NAME, TC.CODE_DESC\n");
		// /sql.append("HAVING SUM(TVOD.ORDER_AMOUNT) > 0\n");
		sql.append(" ORDER BY TC.CODE_DESC");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getDealerSalesOrderTotalExportList");
		return list;
	}

	/**
	 * 销售订单明细查询（DEALER）
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getDealerSalesOrderDetailQueryList(Map<String, Object> map, int curPage, int pageSize) {

		String orderYear1 = (String) map.get("orderYear1");
		String orderWeek1 = (String) map.get("orderWeek1");
		String orderYear2 = (String) map.get("orderYear2");
		String orderWeek2 = (String) map.get("orderWeek2");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String materialCode = (String) map.get("materialCode");
		String orderType = (String) map.get("orderType");
		String orderStatus = (String) map.get("orderStatus");
		String orderNo = (String) map.get("orderNo");
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		orderWeek1 = orderWeek1.length() == 1 ? "0" + orderWeek1 : orderWeek1;
		orderWeek2 = orderWeek2.length() == 1 ? "0" + orderWeek2 : orderWeek2;
		String dataflag = (String) map.get("dataflag");
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");

		StringBuffer sql = new StringBuffer();

		// sql.append("INSERT INTO TMP_VS_ORDERO1\n");
		sql.append("WITH TMP_VS_ORDERO1 AS (\n");
		/*
		 * sql.append(" SELECT TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID,
		 * SUM(TTVDD.BILLING_AMOUNT) BILLING_AMOUNT\n"); sql.append(" FROM
		 * --TT_VS_DLVRY TTVD,\n"); sql.append(" (SELECT MATERIAL_ID,\n");
		 * sql.append(" DELIVERY_ID,\n"); sql.append(" SUM(DELIVERY_AMOUNT)
		 * BILLING_AMOUNT\n"); sql.append(" FROM TT_VS_DLVRY_DTL\n");
		 * sql.append(" GROUP BY MATERIAL_ID, DELIVERY_ID) TTVDD,\n");
		 * sql.append(" TT_VS_ORDER_DETAIL TTVOD\n"); sql.append(" WHERE
		 * TTVOD.MATERIAL_ID = TTVDD.MATERIAL_ID\n"); sql.append(" AND
		 * EXISTS\n"); sql.append(" (SELECT 1\n"); sql.append(" FROM TT_VS_DLVRY
		 * TTVD\n"); sql.append(" WHERE TTVD.DELIVERY_ID =
		 * TTVDD.DELIVERY_ID\n"); sql.append(" AND TTVOD.ORDER_ID =
		 * TTVD.ORDER_ID\n"); sql.append(" AND TTVD.DELIVERY_STATUS IN\n");
		 * sql.append(" (" + Constant.DELIVERY_STATUS_04 + ", " +
		 * Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " +
		 * Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 +
		 * "))\n"); sql.append("group by TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID\n");
		 */

		sql.append("SELECT TVOD.DETAIL_ID,\n");
		sql.append("       TVOD.MATERIAL_ID,\n");
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER        TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       TT_VS_DLVRY        TVD,\n");
		sql.append("       TT_VS_DLVRY_DTL    TVDD\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVD.ORDER_ID\n");
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n");
		sql.append("   AND TVD.DELIVERY_STATUS IN\n");
		sql.append("       (" + Constant.DELIVERY_STATUS_04 + ", " + Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " + Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 + ")\n");
		sql.append("   AND TVO.ORDER_STATUS NOT IN (" + Constant.ORDER_STATUS_01 + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07 + ", " + Constant.ORDER_STATUS_10 + ")\n");
		if (null != dataflag && "1".equals(dataflag)) {
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}

		sql.append("   AND TVO.ORDER_STATUS NOT IN( " + Constant.ORDER_STATUS_01 + ")\n");
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
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

		sql.append(" GROUP BY TVOD.DETAIL_ID, TVOD.MATERIAL_ID\n");

		sql.append(")\n");

		// dao.update(sql.toString(), null);

		// sql = new StringBuffer();
		sql.append("SELECT TD1.DEALER_SHORTNAME ORDER_ORG_NAME,TD1.DEALER_CODE DEALER_CODE,\n");
		sql.append("       TD2.DEALER_SHORTNAME BILLING_ORG_NAME,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TVO.Delivery_Type  DETYPE,\n");
		sql.append("       TVA.Address DEADDR,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       TVO.ORDER_STATUS,\n");
		sql.append("	   SUM(NVL(TVOD.ORDER_AMOUNT, 0)) ORDER_AMOUNT,\n");
		sql.append("       SUM(NVL(TVOD.CHECK_AMOUNT, 0)) CHECK_AMOUNT,\n");
		sql.append("       SUM(NVL(TVOD.DELIVERY_AMOUNT, 0)) DELIVERY_AMOUNT,\n");
		sql.append("       SUM(NVL(A.BILLING_AMOUNT, 0)) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");
		sql.append("       TM_DEALER                TD1,\n");
		sql.append("       TM_DEALER                TD2, TM_VS_ADDRESS TVA,\n");
		sql.append("       TMP_VS_ORDERO1           A\n");
		sql.append(" WHERE TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND A.DETAIL_ID(+) = TVOD.DETAIL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TD1.DEALER_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TD2.DEALER_ID\n");
		sql.append("   AND TVA.ID(+)=TVO.DELIVERY_ADDRESS");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVO.ORDER_ORG_ID IN (" + dealerSql + ")\n");
		if (null != dataflag && "1".equals(dataflag)) {

			addYearAndWeekQueryCondition(sql, orderYear1, orderWeek1, orderYear2, orderWeek2);

			// sql.append(" AND TVO.ORDER_YEAR ||
			// DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			// sql.append(" 1,\n");
			// sql.append(" '0' || TVO.ORDER_WEEK,\n");
			// sql.append(" TVO.ORDER_WEEK) >= "+ orderYear1 + orderWeek1 +
			// "\n");
			// sql.append(" AND TVO.ORDER_YEAR ||
			// DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			// sql.append(" 1,\n");
			// sql.append(" '0' || TVO.ORDER_WEEK,\n");
			// sql.append(" TVO.ORDER_WEEK) <= "+ orderYear2 + orderWeek2 +
			// "\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}
		// sql.append(" AND TVO.ORDER_STATUS NOT IN(" + Constant.ORDER_STATUS_01
		// + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07+
		// ", " + Constant.ORDER_STATUS_10 + ")\n");
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
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
		if (!materialCode.equals("")) {
			String[] array = materialCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (");
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
		sql.append(" GROUP BY TD1.DEALER_SHORTNAME,\n");
		sql.append("          TD2.DEALER_SHORTNAME,\n");
		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD'),\n");
		sql.append("          TVO.ORDER_TYPE,TVO.Delivery_Type,TVA.Address,TD1.DEALER_CODE,\n");
		sql.append("          TVO.ORDER_STATUS\n");
		sql.append("HAVING SUM(TVOD.ORDER_AMOUNT) > 0\n");
		sql.append(" ORDER BY TVO.ORDER_NO DESC");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getDealerSalesOrderDetailQueryList", pageSize, curPage);
		return ps;
	}

	/**
	 * 由于性能原因改写的年月查询条件
	 * 
	 * @param sql
	 * @param startYear
	 * @param startWeek
	 * @param endYear
	 * @param endWeek
	 */
	private void addYearAndWeekQueryCondition(StringBuffer sql, String startYear, String startWeek, String endYear, String endWeek) {
		startWeek = convertWeek(startWeek);
		endWeek = convertWeek(endWeek);
		if (startYear.equals(endYear)) {
			sql.append("   AND TVO.ORDER_YEAR = " + startYear + " \n");
			sql.append("   AND TVO.ORDER_WEEK >= " + startWeek + " \n");
			sql.append("   AND TVO.ORDER_WEEK <= " + endWeek + " \n");
		} else {
			sql.append("   AND ((TVO.ORDER_YEAR = " + startYear + " \n");
			sql.append("   AND TVO.ORDER_WEEK >= " + startWeek + ") \n");
			sql.append("   OR (TVO.ORDER_YEAR = " + endYear + " \n");
			sql.append("   AND  TVO.ORDER_WEEK <= " + endWeek + " ))\n");
		}

	}

	/**
	 * 将周度的第一位“0”去掉
	 * 
	 * @param week
	 * @return
	 */
	private String convertWeek(String week) {
		if (week.startsWith("0")) {
			week = week.replace("0", "");
		}
		return week;
	}

	public List<Map<String, Object>> getDealerSalesOrderDetailExportList(Map<String, Object> map) {

		String orderYear1 = (String) map.get("orderYear1");
		String orderWeek1 = (String) map.get("orderWeek1");
		String orderYear2 = (String) map.get("orderYear2");
		String orderWeek2 = (String) map.get("orderWeek2");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String materialCode = (String) map.get("materialCode");
		String orderType = (String) map.get("orderType");
		String orderStatus = (String) map.get("orderStatus");
		String orderNo = (String) map.get("orderNo");
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		orderWeek1 = orderWeek1.length() == 1 ? "0" + orderWeek1 : orderWeek1;
		orderWeek2 = orderWeek2.length() == 1 ? "0" + orderWeek2 : orderWeek2;
		String dataflag = (String) map.get("dataflag");
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");

		StringBuffer sql = new StringBuffer();

		// sql.append("INSERT INTO TMP_VS_ORDERO1\n");
		sql.append("WITH TMP_VS_ORDERO1 AS (\n");
		/*
		 * sql.append(" SELECT TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID,
		 * SUM(TTVDD.BILLING_AMOUNT) BILLING_AMOUNT\n"); sql.append(" FROM
		 * --TT_VS_DLVRY TTVD,\n"); sql.append(" (SELECT MATERIAL_ID,\n");
		 * sql.append(" DELIVERY_ID,\n"); sql.append(" SUM(DELIVERY_AMOUNT)
		 * BILLING_AMOUNT\n"); sql.append(" FROM TT_VS_DLVRY_DTL\n");
		 * sql.append(" GROUP BY MATERIAL_ID, DELIVERY_ID) TTVDD,\n");
		 * sql.append(" TT_VS_ORDER_DETAIL TTVOD\n"); sql.append(" WHERE
		 * TTVOD.MATERIAL_ID = TTVDD.MATERIAL_ID\n"); sql.append(" AND
		 * EXISTS\n"); sql.append(" (SELECT 1\n"); sql.append(" FROM TT_VS_DLVRY
		 * TTVD\n"); sql.append(" WHERE TTVD.DELIVERY_ID =
		 * TTVDD.DELIVERY_ID\n"); sql.append(" AND TTVOD.ORDER_ID =
		 * TTVD.ORDER_ID\n"); sql.append(" AND TTVD.DELIVERY_STATUS IN\n");
		 * sql.append(" (" + Constant.DELIVERY_STATUS_04 + ", " +
		 * Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " +
		 * Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 +
		 * "))\n"); sql.append("group by TTVOD.DETAIL_ID, TTVOD.MATERIAL_ID\n");
		 */

		sql.append("SELECT TVOD.DETAIL_ID,\n");
		sql.append("       TVOD.MATERIAL_ID,\n");
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER        TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       TT_VS_DLVRY        TVD,\n");
		sql.append("       TT_VS_DLVRY_DTL    TVDD\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVD.ORDER_ID\n");
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n");
		sql.append("   AND TVD.DELIVERY_STATUS IN\n");
		sql.append("       (" + Constant.DELIVERY_STATUS_04 + ", " + Constant.DELIVERY_STATUS_05 + ", " + Constant.DELIVERY_STATUS_10 + ", " + Constant.DELIVERY_STATUS_11 + ", " + Constant.DELIVERY_STATUS_12 + ")\n");
		sql.append("   AND TVO.ORDER_STATUS NOT IN (" + Constant.ORDER_STATUS_01 + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07 + ", " + Constant.ORDER_STATUS_10 + ")\n");
		if (null != dataflag && "1".equals(dataflag)) {
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) >= " + orderYear1 + orderWeek1 + "\n");
			sql.append("   AND TVO.ORDER_YEAR || DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			sql.append("                                1,\n");
			sql.append("                                '0' || TVO.ORDER_WEEK,\n");
			sql.append("                                TVO.ORDER_WEEK) <= " + orderYear2 + orderWeek2 + "\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}

		sql.append("   AND TVO.ORDER_STATUS NOT IN( " + Constant.ORDER_STATUS_01 + ")\n");
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
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

		sql.append(" GROUP BY TVOD.DETAIL_ID, TVOD.MATERIAL_ID\n");

		sql.append(")\n");

		// dao.update(sql.toString(), null);

		// sql = new StringBuffer();

		sql.append("SELECT TD1.DEALER_SHORTNAME ORDER_ORG_NAME,TD1.DEALER_CODE DEALER_CODE,\n");
		sql.append("       TD2.DEALER_SHORTNAME BILLING_ORG_NAME,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TC1.CODE_DESC DETYPE,\n");
		sql.append("       TVA.ADDRESS DEADDR,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TC2.CODE_DESC ORDER_TYPE,\n");
		sql.append("       TC3.CODE_DESC ORDER_STATUS,\n");
		sql.append("	   SUM(NVL(TVOD.ORDER_AMOUNT, 0)) ORDER_AMOUNT,\n");
		sql.append("       SUM(NVL(TVOD.CHECK_AMOUNT, 0)) CHECK_AMOUNT,\n");
		sql.append("       SUM(NVL(TVOD.DELIVERY_AMOUNT, 0)) DELIVERY_AMOUNT,\n");
		sql.append("       SUM(NVL(A.BILLING_AMOUNT, 0)) BILLING_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");
		sql.append("       TM_DEALER                TD1,\n");
		sql.append("       TM_DEALER                TD2, TM_VS_ADDRESS TVA,\n");
		sql.append("       TMP_VS_ORDERO1 			A,\n");
		sql.append("       TC_CODE 				TC1,\n");
		sql.append("       TC_CODE 				TC2,\n");
		sql.append("       TC_CODE				TC3\n");
		sql.append(" WHERE TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND A.DETAIL_ID(+) = TVOD.DETAIL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TD1.DEALER_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TD2.DEALER_ID\n");
		sql.append("   AND TVA.ID(+) = TVO.DELIVERY_ADDRESS");
		sql.append("   AND TVO.DELIVERY_TYPE = TC1.CODE_ID(+)");
		sql.append("   AND TVO.ORDER_TYPE = TC2.CODE_ID");
		sql.append("   AND TVO.ORDER_STATUS = TC3.CODE_ID");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVO.ORDER_ORG_ID IN (" + dealerSql + ")\n");
		if (null != dataflag && "1".equals(dataflag)) {
			addYearAndWeekQueryCondition(sql, orderYear1, orderWeek1, orderYear2, orderWeek2);
			// sql.append(" AND TVO.ORDER_YEAR ||
			// DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			// sql.append(" 1,\n");
			// sql.append(" '0' || TVO.ORDER_WEEK,\n");
			// sql.append(" TVO.ORDER_WEEK) >= "+ orderYear1 + orderWeek1 +
			// "\n");
			// sql.append(" AND TVO.ORDER_YEAR ||
			// DECODE(LENGTH(TVO.ORDER_WEEK),\n");
			// sql.append(" 1,\n");
			// sql.append(" '0' || TVO.ORDER_WEEK,\n");
			// sql.append(" TVO.ORDER_WEEK) <= "+ orderYear2 + orderWeek2 +
			// "\n");
		}
		if (null != dataflag && "2".equals(dataflag)) {
			if (null != beginTime && !"".equals(beginTime)) {
				sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
			}
			if (null != endTime && !"".equals(endTime)) {
				sql.append("   AND TVO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
			}
		}
		sql.append("   AND TVO.ORDER_STATUS NOT IN(" + Constant.ORDER_STATUS_01 + ", " + Constant.ORDER_STATUS_02 + ", " + Constant.ORDER_STATUS_07 + ", " + Constant.ORDER_STATUS_10 + ")\n");
		sql.append("   AND TVO.AREA_ID IN (" + areaIds + ")\n");
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
		if (!materialCode.equals("")) {
			String[] array = materialCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (");
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
		sql.append(" GROUP BY TD1.DEALER_SHORTNAME,\n");
		sql.append("          TD2.DEALER_SHORTNAME,\n");
		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD'),\n");
		sql.append("          TC1.CODE_DESC,TC2.CODE_DESC,TVA.Address,\n");
		sql.append("          TC3.CODE_DESC,TD1.DEALER_CODE\n");
		sql.append("HAVING SUM(TVOD.ORDER_AMOUNT) > 0\n");
		sql.append(" ORDER BY TVO.ORDER_NO DESC");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getDealerSalesOrderDetailExportList");
		return list;
	}

	public PageResult<Map<String, Object>> getOemQuotaTransOrderRateTotalQueryList(Map<String, Object> map, int curPage, int pageSize) {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String areaId = (String) map.get("areaId");
		String dealerCode = (String) map.get("dealerCode");
		String dealerSql = (String) map.get("dealerSql");
		String dealerId = (String) map.get("dealerId");
		String groupCode = (String) map.get("groupCode");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		String regionId = (String) map.get("regionId");
		String endYear = (String) map.get("endYear");
		String endWeek = (String) map.get("endWeek");

		OrderReportDao reportDao = new OrderReportDao();
		TmBusinessParaPO para = reportDao.getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT /*+ all_rows*/A.QUOTA_YEAR || '年' || A.QUOTA_WEEK || '周' QUOTA_DATE,\n");
		sql.append("       A.SERIES_NAME,\n");
		sql.append("       A.MODEL_NAME,\n");
		sql.append("       A.GROUP_NAME,\n");
		sql.append("       A.GROUP_CODE,\n");
		sql.append("       A.QUOTA_AMT,\n");
		sql.append("       ROUND(A.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT,\n");
		// sql.append(" NVL(B.MIN_AMOUNT, 0) MIN_AMOUNT,\n");
		sql.append("       NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		sql.append("	   nvl(c.check_AMOUNT, 0) CHECK_AMOUNT,\n");
		sql.append("       (A.QUOTA_AMT - NVL(C.ORDER_AMOUNT, 0)) CY,\n");
		sql.append("       nvl(d.req_amount, 0) req_amount,\n");
		sql.append("       nvl(d.reserve_amount, 0) reserve_amount,\n");
		sql.append("       nvl(e.delivery_amount, 0) delivery_amount,\n");
		sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 / NVL(c.check_amount, 1)), 2) || '%' ZXL,\n");
		sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 / decode(nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0)+nvl(e.d_delivery_amount,0), 0, 1,nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0)+nvl(e.d_delivery_amount,0))), 2) || '%' ZBL,\n");
		sql.append("       ROUND((NVL(C.ORDER_AMOUNT, 0) * 100 / A.QUOTA_AMT), 2) || '%' WCL\n");
		sql.append("  FROM (SELECT /*+ all_rows*/TQ.QUOTA_YEAR,\n");
		sql.append("               TQ.QUOTA_WEEK,\n");
		sql.append("               TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("               TVMG2.GROUP_NAME MODEL_NAME,\n");
		sql.append("               TVMG1.GROUP_NAME GROUP_NAME,\n");
		sql.append("               TVMG1.GROUP_CODE GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_ID GROUP_ID,\n");
		sql.append("               SUM(TQD.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("          FROM TT_VS_QUOTA            TQ,\n");
		sql.append("               TT_VS_QUOTA_DETAIL     TQD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("               TM_DEALER              TD\n");
		sql.append("         WHERE TQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TQ.QUOTA_ID=TQD.QUOTA_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("AND TQD.QUOTA_AMT <> 0\n");
		sql.append("           AND TQ.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TQ.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("           AND TQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if () { sql.append(" AND TQ.QUOTA_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TQ.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("           AND TQ.AREA_ID = " + areaId + "\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append("         GROUP BY TQ.QUOTA_YEAR,\n");
		sql.append("                  TQ.QUOTA_WEEK,\n");
		sql.append("                  TVMG3.GROUP_NAME,\n");
		sql.append("                  TVMG2.GROUP_NAME,\n");
		sql.append("                  TVMG1.GROUP_NAME,TVMG1.GROUP_CODE,\n");
		sql.append("                  TVMG1.GROUP_ID) A,\n");
		sql.append("       (SELECT /*+ all_rows*/TVMG1.GROUP_ID,\n");
		sql.append("               TVMG1.GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_NAME,\n");
		sql.append("tqm.quota_year,\n");
		sql.append("              tqm.quota_week,\n");
		sql.append("               SUM(TQM.MIN_AMOUNT) MIN_AMOUNT\n");
		sql.append("          FROM TT_VS_QUOTA_MIN        TQM,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("               TM_DEALER              TD\n");
		sql.append("         WHERE TQM.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TQM.DEALER_ID = TD.DEALER_ID\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}

		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TQM.QUOTA_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TQM.QUOTA_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TQM.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TQM.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append("         GROUP BY tqm.quota_year, tqm.quota_week,TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME) B,\n");
		sql.append("       (SELECT /*+ all_rows*/TVMG1.GROUP_ID,\n");
		sql.append("               TVMG1.GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_NAME,\n");
		sql.append("TSO.Order_Year,\n");
		sql.append("               tso.order_week,\n");
		sql.append("               SUM(TSOD.check_AMOUNT) check_AMOUNT,\n");
		sql.append("               SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER              TSO,\n");
		sql.append("               TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG3,\n");
		sql.append("               TM_DEALER                TD\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("           AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TSO.ORDER_ORG_ID = TD.DEALER_ID\n");
		sql.append("           AND TSO.COMPANY_ID = " + companyId + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TSO.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TSO.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append("         GROUP BY TSO.Order_Year,tso.order_week,TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME) C,\n");
		sql.append("(select /*+ all_rows*/tvmg1.group_id,\n");
		sql.append("               tvmg1.group_code,\n");
		sql.append("               tvmg1.group_name,\n");
		sql.append("tvo.order_year,\n");
		sql.append("               tvo.order_week,\n");
		sql.append("               sum(tvdrd.req_amount) req_amount,\n");
		sql.append("               sum(tvdrd.reserve_amount) reserve_amount\n");
		sql.append("          from tt_vs_order              tvo,\n");
		sql.append("               tt_vs_dlvry_req          tvdr,\n");
		sql.append("               tt_vs_dlvry_req_dtl      tvdrd,\n");
		sql.append("               tm_vhcl_material_group_r tvmgr,\n");
		sql.append("               tm_vhcl_material_group   tvmg1,\n");
		sql.append("               tm_vhcl_material_group   tvmg2,\n");
		sql.append("               tm_vhcl_material_group   tvmg3,\n");
		sql.append("               tm_dealer                td\n");
		sql.append("         where tvo.order_id = tvdr.order_id\n");
		sql.append("           and tvdr.req_id = tvdrd.req_id\n");
		sql.append("           and tvdrd.material_id = tvmgr.material_id\n");
		sql.append("           and tvmgr.group_id = tvmg1.group_id\n");
		sql.append("           and tvmg1.parent_group_id = tvmg2.group_id\n");
		sql.append("           and tvmg2.parent_group_id = tvmg3.group_id\n");
		sql.append("           and tvo.order_org_id = td.dealer_id\n");
		sql.append("           AND tvo.COMPANY_ID = " + companyId + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		sql.append("           AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND tvo.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");

		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		/*
		 * if (!orderYear.equals("")) { sql.append(" AND tvo.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * tvo.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvo.AREA_ID = " + areaId + "\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		sql.append("           and tvdr.req_status not in (").append(Constant.ORDER_REQ_STATUS_07).append(",").append(Constant.ORDER_REQ_STATUS_YSH).append(")\n");
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND td.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append("         group by tvo.order_year,tvo.order_week,tvmg1.group_id, tvmg1.group_code, tvmg1.group_name) d,\n");
		sql.append("(select /*+ all_rows*/\n");
		sql.append("         ttt.group_id,\n");
		sql.append("         ttt.group_code,\n");
		sql.append("         ttt.group_name,\n");
		sql.append("         ttt.order_year,\n");
		sql.append("         ttt.order_week,\n");
		sql.append("         sum(ttt.c_delivery_amount) delivery_amount,\n");
		sql.append("         sum(ttt.b_delivery_amount) b_delivery_amount,\n");
		sql.append("         sum(ttt.d_delivery_amount) d_delivery_amount\n");
		sql.append("from \n");
		sql.append("       (select /*+ all_rows*/tvmg1.group_id,\n");
		sql.append("               tvmg1.group_code,\n");
		sql.append("               tvmg1.group_name,\n");
		sql.append("tvo.order_year,\n");
		sql.append("               tvo.order_week,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_01).append(", sum(tvdd.delivery_amount), 0) c_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_02).append(", sum(tvdd.delivery_amount), 0) b_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_03).append(", sum(tvdd.delivery_amount), 0) d_delivery_amount\n");
		// sql.append(" sum(tvdd.delivery_amount) delivery_amount\n");
		sql.append("          from tt_vs_order              tvo,\n");
		sql.append("               tt_vs_dlvry              tvd,\n");
		sql.append("               tt_vs_dlvry_dtl          tvdd,\n");
		sql.append("               tm_vhcl_material_group_r tvmgr,\n");
		sql.append("               tm_vhcl_material_group   tvmg1,\n");
		sql.append("               tm_vhcl_material_group   tvmg2,\n");
		sql.append("               tm_vhcl_material_group   tvmg3,\n");
		sql.append("               tm_dealer                td\n");
		sql.append("         where tvo.order_id = tvd.order_id\n");
		sql.append("           and tvd.delivery_id = tvdd.delivery_id\n");
		sql.append("           and tvdd.material_id = tvmgr.material_id\n");
		sql.append("           and tvmgr.group_id = tvmg1.group_id\n");
		sql.append("           and tvmg1.parent_group_id = tvmg2.group_id\n");
		sql.append("           and tvmg2.parent_group_id = tvmg3.group_id\n");
		sql.append("           and tvo.order_org_id = td.dealer_id\n");
		sql.append("           AND tvo.COMPANY_ID = " + companyId + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		/*
		 * sql.append(" AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		 */
		sql.append("           AND tvo.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND tvo.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * tvo.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvo.AREA_ID = " + areaId + "\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		sql.append("           and tvd.delivery_status in\n");
		sql.append("               (").append(Constant.DELIVERY_STATUS_04).append(",").append(Constant.DELIVERY_STATUS_05).append(",").append(Constant.DELIVERY_STATUS_10).append(",").append(Constant.DELIVERY_STATUS_11).append(",").append(Constant.DELIVERY_STATUS_12).append(")\n");
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND td.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append("         group by tvo.ORDER_TYPE, tvo.order_year,tvo.order_week,tvmg1.group_id, tvmg1.group_code, tvmg1.group_name) ttt\n");
		sql.append("group by ttt.group_id,\n");
		sql.append("                  ttt.group_code,\n");
		sql.append("                  ttt.group_name,\n");
		sql.append("                  ttt.order_year,\n");
		sql.append("                  ttt.order_week) e\n");

		sql.append(" WHERE A.GROUP_ID = B.GROUP_ID(+)\n");
		sql.append("   AND A.GROUP_ID = C.GROUP_ID(+)");
		sql.append("   and a.group_id = d.group_id(+)\n");
		sql.append("   and a.group_id = e.group_id(+)\n");
		sql.append("and a.QUOTA_week = b.QUOTA_week(+)\n");
		sql.append("   and a.QUOTA_week = c.order_week(+)\n");
		sql.append("   and a.QUOTA_week = d.order_week(+)\n");
		sql.append("   and a.QUOTA_week = e.order_week(+)\n");
		sql.append("   and a.QUOTA_year = b.QUOTA_year(+)\n");
		sql.append("   and a.QUOTA_year = c.order_year(+)\n");
		sql.append("   and a.QUOTA_year = d.order_year(+)\n");
		sql.append("   and a.QUOTA_year = e.order_year(+)\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and A.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("order by A.QUOTA_YEAR,A.QUOTA_WEEK\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getOemQuotaTransOrderRateTotalQueryList", pageSize, curPage);
		return ps;
	}

	public List<Map<String, Object>> getOemQuotaTransOrderRateTotalExportList(Map<String, Object> map) {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String areaId = (String) map.get("areaId");
		String dealerCode = (String) map.get("dealerCode");
		String dealerSql = (String) map.get("dealerSql");
		String dealerId = (String) map.get("dealerId");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		String regionId = (String) map.get("regionId");
		String endYear = (String) map.get("endYear");
		String endWeek = (String) map.get("endWeek");
		String groupCode = (String) map.get("groupCode");

		OrderReportDao reportDao = new OrderReportDao();
		TmBusinessParaPO para = reportDao.getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT /*+ all_rows*/A.QUOTA_YEAR || '年' || A.QUOTA_WEEK || '周' QUOTA_DATE,\n");
		sql.append("       A.SERIES_NAME,\n");
		sql.append("       A.MODEL_NAME,\n");
		sql.append("       A.GROUP_NAME,\n");
		sql.append("       A.GROUP_CODE,\n");
		sql.append("       A.QUOTA_AMT,\n");
		sql.append("       ROUND(A.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT,\n");
		// sql.append(" NVL(B.MIN_AMOUNT, 0) MIN_AMOUNT,\n");
		sql.append("       NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		sql.append("       NVL(C.check_AMOUNT, 0) CHECK_AMOUNT,\n");
		sql.append("       (A.QUOTA_AMT - NVL(C.ORDER_AMOUNT, 0)) CY,\n");
		sql.append("       nvl(d.req_amount, 0) req_amount,\n");
		sql.append("       nvl(d.reserve_amount, 0) reserve_amount,\n");
		sql.append("       nvl(e.delivery_amount, 0) delivery_amount,\n");
		sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 / NVL(c.check_amount, 1)), 2) || '%' ZXL,\n");
		sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 / decode(nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0)+nvl(e.d_delivery_amount,0), 0, 1,nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0)+nvl(e.d_delivery_amount,0))), 2) || '%' ZBL,\n");
		sql.append("       ROUND((NVL(C.ORDER_AMOUNT, 0) * 100 / A.QUOTA_AMT), 2) || '%' WCL\n");
		sql.append("  FROM (SELECT /*+ all_rows*/TQ.QUOTA_YEAR,\n");
		sql.append("               TQ.QUOTA_WEEK,\n");
		sql.append("               TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("               TVMG2.GROUP_NAME MODEL_NAME,\n");
		sql.append("               TVMG1.GROUP_NAME GROUP_NAME,\n");
		sql.append("               TVMG1.GROUP_CODE GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_ID GROUP_ID,\n");
		sql.append("               SUM(TQD.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("          FROM TT_VS_QUOTA            TQ,\n");
		sql.append("               TT_VS_QUOTA_DETAIL     TQD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("               TM_DEALER              TD\n");
		sql.append("         WHERE TQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TQ.QUOTA_ID=TQD.QUOTA_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("AND TQD.QUOTA_AMT <> 0\n");
		sql.append("           AND TQ.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TQ.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("           AND TQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}

		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TQ.QUOTA_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TQ.QUOTA_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TQ.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TQ.AREA_ID = " + areaId + "\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append("         GROUP BY TQ.QUOTA_YEAR,\n");
		sql.append("                  TQ.QUOTA_WEEK,\n");
		sql.append("                  TVMG3.GROUP_NAME,\n");
		sql.append("                  TVMG2.GROUP_NAME,\n");
		sql.append("                  TVMG1.GROUP_NAME,TVMG1.GROUP_CODE,\n");
		sql.append("                  TVMG1.GROUP_ID) A,\n");
		sql.append("       (SELECT /*+ all_rows*/TVMG1.GROUP_ID,\n");
		sql.append("               TVMG1.GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_NAME,\n");
		sql.append("tqm.quota_year,\n");
		sql.append("              tqm.quota_week,\n");
		sql.append("               SUM(TQM.MIN_AMOUNT) MIN_AMOUNT\n");
		sql.append("          FROM TT_VS_QUOTA_MIN        TQM,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("               TM_DEALER              TD\n");
		sql.append("         WHERE TQM.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TQM.DEALER_ID = TD.DEALER_ID\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TQM.QUOTA_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TQM.QUOTA_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TQM.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TQM.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         GROUP BY tqm.quota_year, tqm.quota_week,TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME) B,\n");
		sql.append("       (SELECT /*+ all_rows*/TVMG1.GROUP_ID,\n");
		sql.append("               TVMG1.GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_NAME,\n");
		sql.append("TSO.Order_Year,\n");
		sql.append("               tso.order_week,\n");
		sql.append("               SUM(TSOD.check_AMOUNT) check_AMOUNT,\n");
		sql.append("               SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER              TSO,\n");
		sql.append("               TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG3,\n");
		sql.append("               TM_DEALER                TD\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("           AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TSO.ORDER_ORG_ID = TD.DEALER_ID\n");
		sql.append("           AND TSO.COMPANY_ID = " + companyId + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TSO.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TSO.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         GROUP BY TSO.Order_Year,tso.order_week,TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME) C,\n");
		sql.append("(select /*+ all_rows*/tvmg1.group_id,\n");
		sql.append("               tvmg1.group_code,\n");
		sql.append("               tvmg1.group_name,\n");
		sql.append("tvo.order_year,\n");
		sql.append("               tvo.order_week,\n");
		sql.append("               sum(tvdrd.req_amount) req_amount,\n");
		sql.append("               sum(tvdrd.reserve_amount) reserve_amount\n");
		sql.append("          from tt_vs_order              tvo,\n");
		sql.append("               tt_vs_dlvry_req          tvdr,\n");
		sql.append("               tt_vs_dlvry_req_dtl      tvdrd,\n");
		sql.append("               tm_vhcl_material_group_r tvmgr,\n");
		sql.append("               tm_vhcl_material_group   tvmg1,\n");
		sql.append("               tm_vhcl_material_group   tvmg2,\n");
		sql.append("               tm_vhcl_material_group   tvmg3,\n");
		sql.append("               tm_dealer                td\n");
		sql.append("         where tvo.order_id = tvdr.order_id\n");
		sql.append("           and tvdr.req_id = tvdrd.req_id\n");
		sql.append("           and tvdrd.material_id = tvmgr.material_id\n");
		sql.append("           and tvmgr.group_id = tvmg1.group_id\n");
		sql.append("           and tvmg1.parent_group_id = tvmg2.group_id\n");
		sql.append("           and tvmg2.parent_group_id = tvmg3.group_id\n");
		sql.append("           and tvo.order_org_id = td.dealer_id\n");
		sql.append("           AND tvo.COMPANY_ID = " + companyId + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		sql.append("           AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND tvo.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");

		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND tvo.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * tvo.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvo.AREA_ID = " + areaId + "\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		sql.append("           and tvdr.req_status not in (").append(Constant.ORDER_REQ_STATUS_07).append(",").append(Constant.ORDER_REQ_STATUS_YSH).append(")\n");
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND td.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         group by tvo.order_year,tvo.order_week,tvmg1.group_id, tvmg1.group_code, tvmg1.group_name) d,\n");
		// sql.append(" (select /*+ all_rows*/tvmg1.group_id,\n");
		// sql.append(" tvmg1.group_code,\n");
		// sql.append(" tvmg1.group_name,\n");
		// sql.append("tvo.order_year,\n");
		// sql.append(" tvo.order_week,\n");
		// sql.append(" sum(tvdd.delivery_amount) delivery_amount\n");
		// sql.append(" from tt_vs_order tvo,\n");
		// sql.append(" tt_vs_dlvry tvd,\n");
		// sql.append(" tt_vs_dlvry_dtl tvdd,\n");
		// sql.append(" tm_vhcl_material_group_r tvmgr,\n");
		// sql.append(" tm_vhcl_material_group tvmg1,\n");
		// sql.append(" tm_vhcl_material_group tvmg2,\n");
		// sql.append(" tm_vhcl_material_group tvmg3,\n");
		// sql.append(" tm_dealer td\n");
		// sql.append(" where tvo.order_id = tvd.order_id\n");
		// sql.append(" and tvd.delivery_id = tvdd.delivery_id\n");
		// sql.append(" and tvdd.material_id = tvmgr.material_id\n");
		// sql.append(" and tvmgr.group_id = tvmg1.group_id\n");
		// sql.append(" and tvmg1.parent_group_id = tvmg2.group_id\n");
		// sql.append(" and tvmg2.parent_group_id = tvmg3.group_id\n");
		// sql.append(" and tvo.order_org_id = td.dealer_id\n");
		// sql.append(" AND tvo.COMPANY_ID = " + companyId + "\n");
		// if (!dealerSql.equals("")) {
		// sql.append(" AND " + dealerSql);
		// }
		// sql.append(" AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01
		// + "\n");
		// sql.append(" AND tvo.ORDER_STATUS IN ("
		// + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05
		// + ")\n");
		// if (!orderYear.equals("") && !orderWeek.equals("")) {
		// sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// sql.append(" tvo.ORDER_WEEK) >=
		// ").append(orderYear).append(orderWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		//		
		// if (!endYear.equals("") && !endWeek.equals("")) {
		// sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// sql.append(" tvo.ORDER_WEEK) <=
		// ").append(endYear).append(endWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		// /*if (!orderYear.equals("")) {
		// sql.append(" AND tvo.ORDER_YEAR = " + orderYear + "\n");
		// }
		// if (!orderWeek.equals("")) {
		// sql.append(" AND tvo.ORDER_WEEK = " + orderWeek + "\n");
		// }*/
		// sql.append(" AND tvo.AREA_ID IN (" + areaIds + ")\n");
		// if (!areaId.equals("")) {
		// sql.append(" AND tvo.AREA_ID = " + areaId + "\n");
		// }
		// if (!"".equals(regionId)) {
		// sql.append(" AND TD.PROVINCE_ID = " + regionId + "\n");
		// }
		// sql.append(" and tvd.delivery_status in\n");
		// sql.append("
		// (").append(Constant.DELIVERY_STATUS_04).append(",").append(Constant.DELIVERY_STATUS_05).append(",").append(Constant.DELIVERY_STATUS_10).append(",").append(Constant.DELIVERY_STATUS_11).append(",").append(Constant.DELIVERY_STATUS_12).append(")\n");
		// if (!dealerCode.equals("")) {
		// String[] array = dealerCode.split(",");
		// sql.append(" AND td.DEALER_CODE IN (");
		// for (int i = 0; i < array.length; i++) {
		// sql.append("'" + array[i] + "'");
		// if (i != array.length - 1) {
		// sql.append(",");
		// }
		// }
		// sql.append(")\n");
		// }
		// sql.append(" group by tvo.order_year,tvo.order_week,tvmg1.group_id,
		// tvmg1.group_code, tvmg1.group_name) e\n");
		sql.append("(select /*+ all_rows*/\n");
		sql.append("         ttt.group_id,\n");
		sql.append("         ttt.group_code,\n");
		sql.append("         ttt.group_name,\n");
		sql.append("         ttt.order_year,\n");
		sql.append("         ttt.order_week,\n");
		sql.append("         sum(ttt.c_delivery_amount) delivery_amount,\n");
		sql.append("         sum(ttt.b_delivery_amount) b_delivery_amount,\n");
		sql.append("         sum(ttt.d_delivery_amount) d_delivery_amount\n");
		sql.append("from \n");
		sql.append("       (select /*+ all_rows*/tvmg1.group_id,\n");
		sql.append("               tvmg1.group_code,\n");
		sql.append("               tvmg1.group_name,\n");
		sql.append("tvo.order_year,\n");
		sql.append("               tvo.order_week,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_01).append(", sum(tvdd.delivery_amount), 0) c_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_02).append(", sum(tvdd.delivery_amount), 0) b_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_03).append(", sum(tvdd.delivery_amount), 0) d_delivery_amount\n");
		// sql.append(" sum(tvdd.delivery_amount) delivery_amount\n");
		sql.append("          from tt_vs_order              tvo,\n");
		sql.append("               tt_vs_dlvry              tvd,\n");
		sql.append("               tt_vs_dlvry_dtl          tvdd,\n");
		sql.append("               tm_vhcl_material_group_r tvmgr,\n");
		sql.append("               tm_vhcl_material_group   tvmg1,\n");
		sql.append("               tm_vhcl_material_group   tvmg2,\n");
		sql.append("               tm_vhcl_material_group   tvmg3,\n");
		sql.append("               tm_dealer                td\n");
		sql.append("         where tvo.order_id = tvd.order_id\n");
		sql.append("           and tvd.delivery_id = tvdd.delivery_id\n");
		sql.append("           and tvdd.material_id = tvmgr.material_id\n");
		sql.append("           and tvmgr.group_id = tvmg1.group_id\n");
		sql.append("           and tvmg1.parent_group_id = tvmg2.group_id\n");
		sql.append("           and tvmg2.parent_group_id = tvmg3.group_id\n");
		sql.append("           and tvo.order_org_id = td.dealer_id\n");
		sql.append("           AND tvo.COMPANY_ID = " + companyId + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		/*
		 * sql.append(" AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		 */
		sql.append("           AND tvo.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND tvo.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * tvo.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvo.AREA_ID = " + areaId + "\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		sql.append("           and tvd.delivery_status in\n");
		sql.append("               (").append(Constant.DELIVERY_STATUS_04).append(",").append(Constant.DELIVERY_STATUS_05).append(",").append(Constant.DELIVERY_STATUS_10).append(",").append(Constant.DELIVERY_STATUS_11).append(",").append(Constant.DELIVERY_STATUS_12).append(")\n");
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND td.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         group by tvo.ORDER_TYPE, tvo.order_year,tvo.order_week,tvmg1.group_id, tvmg1.group_code, tvmg1.group_name) ttt\n");
		sql.append("group by ttt.group_id,\n");
		sql.append("                  ttt.group_code,\n");
		sql.append("                  ttt.group_name,\n");
		sql.append("                  ttt.order_year,\n");
		sql.append("                  ttt.order_week) e\n");
		sql.append(" WHERE A.GROUP_ID = B.GROUP_ID(+)\n");
		sql.append("   AND A.GROUP_ID = C.GROUP_ID(+)");
		sql.append("   and a.group_id = d.group_id(+)\n");
		sql.append("   and a.group_id = e.group_id(+)\n");
		sql.append("and a.QUOTA_week = b.QUOTA_week(+)\n");
		sql.append("and a.QUOTA_week = c.order_week(+)\n");
		sql.append("and a.QUOTA_week = d.order_week(+)\n");
		sql.append("and a.QUOTA_week = e.order_week(+)\n");
		sql.append("and a.QUOTA_year = b.QUOTA_year(+)\n");
		sql.append("and a.QUOTA_year = c.order_year(+)\n");
		sql.append("and a.QUOTA_year = d.order_year(+)\n");
		sql.append("and a.QUOTA_year = e.order_year(+)\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and A.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("order by A.QUOTA_YEAR,A.QUOTA_WEEK\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}

	public PageResult<Map<String, Object>> getOemQuotaTransOrderRateDetailQueryList(Map<String, Object> map, int curPage, int pageSize) {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String areaId = (String) map.get("areaId");
		String dealerCode = (String) map.get("dealerCode");
		String dealerSql = (String) map.get("dealerSql");
		String dealerId = (String) map.get("dealerId");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		String regionId = (String) map.get("regionId");
		String endYear = (String) map.get("endYear");
		String endWeek = (String) map.get("endWeek");
		String groupCode = (String) map.get("groupCode");

		OrderReportDao reportDao = new OrderReportDao();
		TmBusinessParaPO para = reportDao.getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT /*+ all_rows*/A.QUOTA_YEAR || '年' || A.QUOTA_WEEK || '周' QUOTA_DATE,\n");
		sql.append("       A.ORG_NAME,\n");
		sql.append("       A.PROVINCE_ID,\n");
		sql.append("       A.DEALER_CODE,\n");
		sql.append("       A.DEALER_NAME,\n");
		sql.append("       A.QUOTA_AMT,\n");
		// sql.append(" A.group_code,\n");
		sql.append("       ROUND(A.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT,\n");
		// sql.append(" NVL(B.MIN_AMOUNT, 0) MIN_AMOUNT,\n");
		sql.append("       NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		sql.append("       NVL(C.check_AMOUNT, 0) CHECK_AMOUNT,\n");
		sql.append("       nvl(d.req_amount, 0) REQ_AMOUNT,\n");
		sql.append("       nvl(d.reserve_amount, 0) RESERVE_AMOUNT,\n");
		sql.append("       nvl(e.delivery_amount, 0) DELIVERY_AMOUNT,\n");
		sql.append("       nvl(e.b_delivery_amount, 0) B_DELIVERY_AMOUNT,\n");
		sql.append("       (A.QUOTA_AMT - NVL(C.ORDER_AMOUNT, 0)) CY,\n");
		sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 / NVL(decode(c.check_amount,0,1,c.check_amount), 1)), 2) || '%' ZXL,\n");
		sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 / decode(nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0)+nvl(e.d_delivery_amount,0), 0, 1,nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0)+nvl(e.d_delivery_amount,0))), 2) || '%' ZBL,\n");
		sql.append("       ROUND((NVL(C.ORDER_AMOUNT, 0) * 100 / A.QUOTA_AMT), 2) || '%' WCL\n");
		sql.append("  FROM (/*+ all_rows*/SELECT TQ.QUOTA_YEAR,\n");
		sql.append("               TQ.QUOTA_WEEK,\n");
		// sql.append(" TVMG.GROUP_CODE, /*新增*/\n");
		sql.append("               TD.DEALER_ID,\n");
		sql.append("               TD.DEALER_CODE,\n");
		sql.append("               TD.PROVINCE_ID,\n");
		sql.append("               TMO.ORG_NAME,\n");
		sql.append("               TD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("               SUM(TQD.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("          FROM TT_VS_QUOTA TQ,TT_VS_QUOTA_DETAIL TQD, TM_DEALER TD, TM_DEALER_ORG_RELATION TDOR, TM_VHCL_MATERIAL_GROUP TVMG, TM_ORG TMO\n");
		sql.append("         WHERE TQ.DEALER_ID = TD.DEALER_ID\n");
		sql.append("AND TQD.GROUP_ID = TVMG.GROUP_ID  /*新增*/\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("           AND TQ.QUOTA_ID=TQD.QUOTA_ID");
		sql.append("           AND TDOR.DEALER_ID = TD.DEALER_ID");
		sql.append("           AND TDOR.ORG_ID = TMO.ORG_ID");
		sql.append("           AND TQ.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("           AND TQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		sql.append("           AND TQD.QUOTA_AMT <> 0\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TQ.QUOTA_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TQ.QUOTA_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TQ.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TQ.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		sql.append("         GROUP BY TQ.QUOTA_YEAR,\n");
		sql.append("                  TQ.QUOTA_WEEK,\n");
		sql.append("                  TD.DEALER_ID,\n");
		sql.append("                  TD.DEALER_CODE,\n");
		sql.append("                  TD.PROVINCE_ID,\n");
		sql.append("                  TMO.ORG_NAME,\n");
		// sql.append(" TVMG.GROUP_CODE,\n");
		sql.append("                  TD.DEALER_SHORTNAME) A,\n");
		sql.append("       (SELECT /*+ all_rows*/TD.DEALER_ID, tqm.quota_year, tqm.quota_week,SUM(TQM.MIN_AMOUNT) MIN_AMOUNT\n");
		sql.append("          FROM TT_VS_QUOTA_MIN TQM, TM_DEALER TD, tm_vhcl_material_group tvmg\n");
		sql.append("         WHERE TQM.DEALER_ID = TD.DEALER_ID\n");
		sql.append("and tqm.group_id = tvmg.group_id\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE  in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TQM.QUOTA_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TQM.QUOTA_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TQM.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TQM.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         GROUP BY tqm.quota_year,tqm.quota_week,TD.DEALER_ID) B,\n");
		sql.append("       (SELECT /*+ all_rows*/TD.DEALER_ID, tso.order_year,tso.order_week,SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT,\n");
		sql.append("               SUM(TSOD.check_AMOUNT) check_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER TSO, TT_VS_ORDER_DETAIL TSOD, tm_vhcl_material_group tvmg, tm_vhcl_material_group_r tvmgr, TM_DEALER TD\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TSO.ORDER_ORG_ID = TD.DEALER_ID\n");
		sql.append("	and tsod.material_id = tvmgr.material_id\n");
		sql.append("           and tvmgr.group_id = tvmg.group_id\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("           AND TSO.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TSO.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TSO.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         GROUP BY TSO.Order_Year,tso.order_week,TD.DEALER_ID) C,\n");

		sql.append("(select /*+ all_rows*/tvo.billing_org_id, tvo.order_year,tvo.order_week,sum(tvdrd.req_amount) req_amount, sum(tvdrd.reserve_amount) reserve_amount\n");
		sql.append("          from tt_vs_order         tvo,\n");
		sql.append("               tt_vs_dlvry_req     tvdr,\n");
		sql.append("               tt_vs_dlvry_req_dtl tvdrd,\n");
		sql.append("			tm_vhcl_material_group tvmg,\n");
		sql.append("              tm_vhcl_material_group_r tvmgr\n");
		sql.append("         where tvo.order_id = tvdr.order_id\n");
		sql.append("and tvdrd.material_id = tvmgr.material_id\n");
		sql.append("          and tvmgr.group_id = tvmg.group_id\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("           and tvdr.req_id = tvdrd.req_id\n");
		sql.append("           AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           and tvdr.req_status not in (").append(Constant.ORDER_REQ_STATUS_07).append(",").append(Constant.ORDER_REQ_STATUS_YSH).append(")\n");
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND tvO.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * tvO.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvO.AREA_ID = " + areaId + "\n");
		}
		/*
		 * if (!dealerCode.equals("")) { String[] array = dealerCode.split(",");
		 * sql.append(" AND tvo.billing_org_id IN ("); for (int i = 0; i <
		 * array.length; i++) { sql.append("'" + array[i] + "'"); if (i !=
		 * array.length - 1) { sql.append(","); } } sql.append(")\n"); }
		 */
		sql.append("         group by tvo.order_year,tvo.order_week,tvo.billing_org_id) d,\n");

		sql.append("(select /*+ all_rows*/ttt.billing_org_id,\n");
		sql.append("       ttt.order_year,\n");
		sql.append("       ttt.order_week,\n");
		sql.append("       sum(ttt.c_delivery_amount) delivery_amount,\n");
		sql.append("       sum(ttt.b_delivery_amount) b_delivery_amount,\n");
		sql.append("       sum(ttt.d_delivery_amount) d_delivery_amount\n");
		sql.append("from (\n");
		// sql.append("(select /*+ all_rows*/tvo.billing_org_id,
		// tvo.order_year,tvo.order_week,sum(tvdd.delivery_amount)
		// delivery_amount\n");
		sql.append("select /*+ all_rows*/\n");
		sql.append("         tvo.billing_org_id,\n");
		sql.append("         tvo.order_year,\n");
		sql.append("         tvo.order_week,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_01).append(", sum(tvdd.delivery_amount), 0) c_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_02).append(", sum(tvdd.delivery_amount), 0) b_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_03).append(", sum(tvdd.delivery_amount), 0) d_delivery_amount\n");

		sql.append("          from tt_vs_order tvo, tt_vs_dlvry tvd, tt_vs_dlvry_dtl tvdd, tm_vhcl_material_group tvmg, tm_vhcl_material_group_r tvmgr\n");
		sql.append("         where tvo.order_id = tvd.order_id\n");
		sql.append("	and tvmgr.material_id = tvdd.material_id\n");
		sql.append("                 and tvmgr.group_id = tvmg.group_id\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("           and tvd.delivery_id = tvdd.delivery_id\n");
		// sql.append(" AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           and tvd.delivery_status in\n");
		sql.append("               (").append(Constant.DELIVERY_STATUS_04).append(",").append(Constant.DELIVERY_STATUS_05).append(",").append(Constant.DELIVERY_STATUS_10).append(",").append(Constant.DELIVERY_STATUS_11).append(",").append(Constant.DELIVERY_STATUS_12).append(")\n");

		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND tvO.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * tvO.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvO.AREA_ID = " + areaId + "\n");
		}
		/*
		 * if (!dealerCode.equals("")) { String[] array = dealerCode.split(",");
		 * sql.append(" AND tvo.billing_org_id IN ("); for (int i = 0; i <
		 * array.length; i++) { sql.append("'" + array[i] + "'"); if (i !=
		 * array.length - 1) { sql.append(","); } } sql.append(")\n"); }
		 */
		sql.append("         group by tvo.ORDER_TYPE,tvo.order_year,tvo.order_week,tvo.billing_org_id\n");
		sql.append(") ttt\n");
		sql.append(" group by ttt.billing_org_id, ttt.order_year, ttt.order_week) e\n");

		sql.append(" WHERE A.DEALER_ID = B.DEALER_ID(+)\n");
		sql.append("   AND A.DEALER_ID = C.DEALER_ID(+)");
		sql.append("   AND A.DEALER_ID = D.billing_org_id(+)");
		sql.append("   AND A.DEALER_ID = e.billing_org_id(+)");
		sql.append("and a.QUOTA_week = b.QUOTA_week(+)\n");
		sql.append("   and a.QUOTA_week = c.order_week(+)\n");
		sql.append("   and a.QUOTA_week = d.order_week(+)\n");
		sql.append("   and a.QUOTA_week = e.order_week(+)\n");
		sql.append("   and a.QUOTA_year = b.QUOTA_year(+)\n");
		sql.append("   and a.QUOTA_year = c.order_year(+)\n");
		sql.append("   and a.QUOTA_year = d.order_year(+)\n");
		sql.append("   and a.QUOTA_year = e.order_year(+)\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		/*
		 * if(groupCode != null && !"".equals(groupCode)) { sql.append("and
		 * A.GROUP_CODE in ('"); if(!groupCode.contains(",")) {
		 * sql.append(groupCode); } else { String[] groupCodes =
		 * groupCode.split(","); for(int i=0; i<groupCodes.length; i++) { if(i !=
		 * groupCodes.length-1) { sql.append(groupCodes[i]); sql.append("','"); }
		 * else { sql.append(groupCodes[i]); } } } sql.append("')\n"); }
		 */
		sql.append("order by A.QUOTA_YEAR,A.QUOTA_WEEK\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getOemQuotaTransOrderRatDetailQueryList", pageSize, curPage);
		return ps;
	}

	public List<Map<String, Object>> getOemQuotaTransOrderRateDetailExportList(Map<String, Object> map) {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String areaId = (String) map.get("areaId");
		String dealerCode = (String) map.get("dealerCode");
		String dealerSql = (String) map.get("dealerSql");
		String dealerId = (String) map.get("dealerId");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		String regionId = (String) map.get("regionId");
		String endYear = (String) map.get("endYear");
		String endWeek = (String) map.get("endWeek");
		String groupCode = (String) map.get("groupCode");
		OrderReportDao reportDao = new OrderReportDao();
		TmBusinessParaPO para = reportDao.getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT /*+ all_rows*/A.QUOTA_YEAR || '年' || A.QUOTA_WEEK || '周' QUOTA_DATE,\n");
		sql.append("       A.ORG_NAME,\n");
		sql.append("       A.REGION_NAME,\n");
		sql.append("       A.DEALER_CODE,\n");
		sql.append("       A.DEALER_NAME,\n");
		sql.append("       A.QUOTA_AMT,\n");
		// sql.append(" A.group_code,\n");
		sql.append("       ROUND(A.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT,\n");
		// sql.append(" NVL(B.MIN_AMOUNT, 0) MIN_AMOUNT,\n");
		sql.append("       NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		sql.append("       NVL(C.check_AMOUNT, 0) CHECK_AMOUNT,\n");
		sql.append("       nvl(d.req_amount, 0) REQ_AMOUNT,\n");
		sql.append("       nvl(d.reserve_amount, 0) RESERVE_AMOUNT,\n");
		sql.append("       nvl(e.delivery_amount, 0) DELIVERY_AMOUNT,\n");
		sql.append("       nvl(e.b_delivery_amount, 0) b_DELIVERY_AMOUNT,\n");
		sql.append("       (A.QUOTA_AMT - NVL(C.ORDER_AMOUNT, 0)) CY,\n");
		sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 / NVL(decode(c.check_amount,0,1,c.check_amount), 1)), 2) || '%' ZXL,\n");
		sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 / decode(nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0)+nvl(e.d_delivery_amount,0), 0, 1,nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0)+nvl(e.d_delivery_amount,0))), 2) || '%' ZBL,\n");
		sql.append("       ROUND((NVL(C.ORDER_AMOUNT, 0) * 100 / A.QUOTA_AMT), 2) || '%' WCL\n");
		sql.append("  FROM (SELECT /*+ all_rows*/TQ.QUOTA_YEAR,\n");
		sql.append("               TQ.QUOTA_WEEK,\n");
		sql.append("       TMO.ORG_NAME,\n");
		sql.append("       TR.REGION_NAME,\n");
		// sql.append(" TVMG.GROUP_CODE, /*新增*/\n");
		sql.append("               TD.DEALER_ID,\n");
		sql.append("               TD.DEALER_CODE,\n");
		sql.append("               TD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("               SUM(TQD.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("          FROM TT_VS_QUOTA TQ,TT_VS_QUOTA_DETAIL TQD, TM_DEALER TD, TM_DEALER_ORG_RELATION TDOR, TM_ORG TMO, TM_REGION TR, TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append("         WHERE TQ.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TQ.QUOTA_ID=TQD.QUOTA_ID");
		sql.append("	AND TQD.GROUP_ID = TVMG.GROUP_ID\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("           AND TDOR.DEALER_ID = TD.DEALER_ID");
		sql.append("           AND TDOR.ORG_ID = TMO.ORG_ID");
		sql.append("           AND TD.PROVINCE_ID = TR.REGION_CODE(+)");
		sql.append("           AND TQ.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("           AND TQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		sql.append("AND TQD.QUOTA_AMT <> 0\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TQ.QUOTA_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TQ.QUOTA_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TQ.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TQ.AREA_ID = " + areaId + "\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append("         GROUP BY TQ.QUOTA_YEAR,\n");
		sql.append("                  TQ.QUOTA_WEEK,\n");
		sql.append("                  TR.REGION_NAME,\n");
		sql.append("                  TMO.ORG_NAME,\n");
		sql.append("                  TD.DEALER_ID,\n");
		sql.append("                  TD.DEALER_CODE,\n");
		// sql.append(" TVMG.GROUP_CODE,\n");
		sql.append("                  TD.DEALER_SHORTNAME) A,\n");
		sql.append("       (SELECT /*+ all_rows*/TD.DEALER_ID, tqm.quota_year, tqm.quota_week,SUM(TQM.MIN_AMOUNT) MIN_AMOUNT\n");
		sql.append("          FROM TT_VS_QUOTA_MIN TQM, TM_DEALER TD, tm_vhcl_material_group tvmg\n");
		sql.append("         WHERE TQM.DEALER_ID = TD.DEALER_ID\n");
		sql.append("	and tqm.group_id = tvmg.group_id\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TQM.QUOTA_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TQM.QUOTA_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TQM.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TQM.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         GROUP BY tqm.quota_year, tqm.quota_week,TD.DEALER_ID) B,\n");
		sql.append("       (SELECT /*+ all_rows*/TD.DEALER_ID, TSO.Order_Year,tso.order_week,SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT,\n");
		sql.append("               SUM(TSOD.check_AMOUNT) check_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER TSO, TT_VS_ORDER_DETAIL TSOD, TM_DEALER TD, tm_vhcl_material_group tvmg, tm_vhcl_material_group_r tvmgr\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("and tsod.material_id = tvmgr.material_id\n");
		sql.append("          and tvmgr.group_id = tvmg.group_id\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("           AND TSO.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TSO.ORDER_ORG_ID = TD.DEALER_ID\n");
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TSO.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TSO.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         GROUP BY TSO.Order_Year,tso.order_week,TD.DEALER_ID) C,\n");
		sql.append("(select /*+ all_rows*/tvo.billing_org_id, tvo.order_year,tvo.order_week,sum(tvdrd.req_amount) req_amount, sum(tvdrd.reserve_amount) reserve_amount\n");
		sql.append("          from tt_vs_order         tvo,\n");
		sql.append("               tt_vs_dlvry_req     tvdr,\n");
		sql.append("               tt_vs_dlvry_req_dtl tvdrd,\n");
		sql.append("		tm_vhcl_material_group tvmg,\n");
		sql.append("               tm_vhcl_material_group_r tvmgr\n");

		sql.append("         where tvo.order_id = tvdr.order_id\n");
		sql.append("	and tvdrd.material_id = tvmgr.material_id\n");
		sql.append("           and tvmgr.group_id = tvmg.group_id\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("           and tvdr.req_id = tvdrd.req_id\n");
		sql.append("           AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           and tvdr.req_status not in (").append(Constant.ORDER_REQ_STATUS_07).append(",").append(Constant.ORDER_REQ_STATUS_YSH).append(")\n");
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND tvO.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * tvO.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvO.AREA_ID = " + areaId + "\n");
		}
		/*
		 * if (!dealerCode.equals("")) { String[] array = dealerCode.split(",");
		 * sql.append(" AND tvo.billing_org_id IN ("); for (int i = 0; i <
		 * array.length; i++) { sql.append("'" + array[i] + "'"); if (i !=
		 * array.length - 1) { sql.append(","); } } sql.append(")\n"); }
		 */
		sql.append("         group by tvo.order_year,tvo.order_week,tvo.billing_org_id) d,\n");

		// sql.append("(select /*+ all_rows*/tvo.billing_org_id,
		// tvo.order_year,tvo.order_week,sum(tvdd.delivery_amount)
		// delivery_amount\n");
		// sql.append(" from tt_vs_order tvo, tt_vs_dlvry tvd, tt_vs_dlvry_dtl
		// tvdd\n");
		// sql.append(" where tvo.order_id = tvd.order_id\n");
		// sql.append(" and tvd.delivery_id = tvdd.delivery_id\n");
		// sql.append(" AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		// sql.append(" and tvd.delivery_status in\n");
		// sql.append("
		// (").append(Constant.DELIVERY_STATUS_04).append(",").append(Constant.DELIVERY_STATUS_05).append(",").append(Constant.DELIVERY_STATUS_10).append(",").append(Constant.DELIVERY_STATUS_11).append(",").append(Constant.DELIVERY_STATUS_12).append(")\n");
		//		
		// if (!orderYear.equals("") && !orderWeek.equals("")) {
		// sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// sql.append(" tvo.ORDER_WEEK) >=
		// ").append(orderYear).append(orderWeek).append("\n");
		//
		// sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");
		// }
		//		
		// if (!endYear.equals("") && !endWeek.equals("")) {
		// sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// sql.append(" tvo.ORDER_WEEK) <=
		// ").append(endYear).append(endWeek).append("\n");
		//
		// sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");
		// }
		// if (!orderYear.equals("")) {
		// sql.append(" AND tvO.ORDER_YEAR = " + orderYear + "\n");
		// }
		// if (!orderWeek.equals("")) {
		// sql.append(" AND tvO.ORDER_WEEK = " + orderWeek + "\n");
		// }
		// sql.append(" AND tvo.AREA_ID IN (" + areaIds + ")\n");
		// if (!areaId.equals("")) {
		// sql.append(" AND tvO.AREA_ID = " + areaId + "\n");
		// }
		// if (!dealerCode.equals("")) {
		// String[] array = dealerCode.split(",");
		// sql.append(" AND tvo.billing_org_id IN (");
		// for (int i = 0; i < array.length; i++) {
		// sql.append("'" + array[i] + "'");
		// if (i != array.length - 1) {
		// sql.append(",");
		// }
		// }
		// sql.append(")\n");
		// }
		// sql.append(" group by
		// tvo.order_year,tvo.order_week,tvo.billing_org_id) e\n");
		sql.append("(select /*+ all_rows*/ttt.billing_org_id,\n");
		sql.append("       ttt.order_year,\n");
		sql.append("       ttt.order_week,\n");
		sql.append("       sum(ttt.c_delivery_amount) delivery_amount,\n");
		sql.append("       sum(ttt.b_delivery_amount) b_delivery_amount,\n");
		sql.append("       sum(ttt.d_delivery_amount) d_delivery_amount\n");
		sql.append("from (\n");
		// sql.append("(select /*+ all_rows*/tvo.billing_org_id,
		// tvo.order_year,tvo.order_week,sum(tvdd.delivery_amount)
		// delivery_amount\n");
		sql.append("select /*+ all_rows*/\n");
		sql.append("         tvo.billing_org_id,\n");
		sql.append("         tvo.order_year,\n");
		sql.append("         tvo.order_week,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_01).append(", sum(tvdd.delivery_amount), 0) c_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_02).append(", sum(tvdd.delivery_amount), 0) b_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_03).append(", sum(tvdd.delivery_amount), 0) d_delivery_amount\n");

		sql.append("          from tt_vs_order tvo, tt_vs_dlvry tvd, tt_vs_dlvry_dtl tvdd, tm_vhcl_material_group tvmg, tm_vhcl_material_group_r tvmgr\n");
		sql.append("         where tvo.order_id = tvd.order_id\n");
		sql.append("	and tvmgr.material_id = tvdd.material_id\n");
		sql.append("                 and tvmgr.group_id = tvmg.group_id\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("           and tvd.delivery_id = tvdd.delivery_id\n");
		// sql.append(" AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           and tvd.delivery_status in\n");
		sql.append("               (").append(Constant.DELIVERY_STATUS_04).append(",").append(Constant.DELIVERY_STATUS_05).append(",").append(Constant.DELIVERY_STATUS_10).append(",").append(Constant.DELIVERY_STATUS_11).append(",").append(Constant.DELIVERY_STATUS_12).append(")\n");

		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND tvO.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * tvO.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvO.AREA_ID = " + areaId + "\n");
		}
		/*
		 * if (!dealerCode.equals("")) { String[] array = dealerCode.split(",");
		 * sql.append(" AND tvo.billing_org_id IN ("); for (int i = 0; i <
		 * array.length; i++) { sql.append("'" + array[i] + "'"); if (i !=
		 * array.length - 1) { sql.append(","); } } sql.append(")\n"); }
		 */
		sql.append("         group by tvo.ORDER_TYPE,tvo.order_year,tvo.order_week,tvo.billing_org_id\n");
		sql.append(") ttt\n");
		sql.append(" group by ttt.billing_org_id, ttt.order_year, ttt.order_week) e\n");
		sql.append(" WHERE A.DEALER_ID = B.DEALER_ID(+)\n");
		sql.append("   AND A.DEALER_ID = C.DEALER_ID(+)");
		sql.append("   AND A.DEALER_ID = D.billing_org_id(+)");
		sql.append("   AND A.DEALER_ID = e.billing_org_id(+)");
		sql.append("and a.QUOTA_week = b.QUOTA_week(+)\n");
		sql.append("and a.QUOTA_week = c.order_week(+)\n");
		sql.append("and a.QUOTA_week = d.order_week(+)\n");
		sql.append("and a.QUOTA_week = e.order_week(+)\n");
		sql.append("and a.QUOTA_year = b.QUOTA_year(+)\n");
		sql.append("and a.QUOTA_year = c.order_year(+)\n");
		sql.append("and a.QUOTA_year = d.order_year(+)\n");
		sql.append("and a.QUOTA_year = e.order_year(+)\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		/*
		 * if(groupCode != null && !"".equals(groupCode)) { sql.append("and
		 * A.GROUP_CODE in ('"); if(!groupCode.contains(",")) {
		 * sql.append(groupCode); } else { String[] groupCodes =
		 * groupCode.split(","); for(int i=0; i<groupCodes.length; i++) { if(i !=
		 * groupCodes.length-1) { sql.append(groupCodes[i]); sql.append("','"); }
		 * else { sql.append(groupCodes[i]); } } } sql.append("')\n"); }
		 */
		sql.append("order by A.QUOTA_YEAR,A.QUOTA_WEEK\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}

	public PageResult<Map<String, Object>> getDealerQuotaTransOrderRateTotalQueryList(Map<String, Object> map, int curPage, int pageSize) {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String endYear = (String) map.get("endYear");
		String endWeek = (String) map.get("endWeek");
		String dealerId = (String) map.get("dealerId");
		String areaId = (String) map.get("areaId");
		String areaIds = (String) map.get("areaIds");
		String companyId = (String) map.get("companyId");

		OrderReportDao reportDao = new OrderReportDao();
		TmBusinessParaPO para = reportDao.getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT A.QUOTA_YEAR || '年' || A.QUOTA_WEEK || '周' QUOTA_DATE,\n");
		sql.append("       A.SERIES_NAME,\n");
		sql.append("       A.MODEL_NAME,\n");
		sql.append("       A.GROUP_NAME,\n");
		sql.append("       A.QUOTA_AMT,\n");
		sql.append("       ROUND(A.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT,\n");
		// sql.append(" NVL(B.MIN_AMOUNT, 0) MIN_AMOUNT,\n");
		sql.append("       NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		sql.append("	   nvl(c.check_AMOUNT, 0) CHECK_AMOUNT,\n");
		sql.append("       (A.QUOTA_AMT - NVL(C.ORDER_AMOUNT, 0)) CY,\n");
		sql.append("       nvl(d.req_amount, 0) req_amount,\n");
		sql.append("       nvl(d.reserve_amount, 0) reserve_amount,\n");
		sql.append("       nvl(e.delivery_amount, 0) delivery_amount,\n");
		sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 / NVL(c.check_amount, 1)), 2) || '%' ZXL,\n");
		sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 / decode(nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0)+nvl(e.d_delivery_amount,0), 0, 1,nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0)+nvl(e.d_delivery_amount,0))), 2) || '%' ZBL,\n");
		sql.append("       ROUND((NVL(C.ORDER_AMOUNT, 0) * 100 / A.QUOTA_AMT), 2) || '%' WCL\n");
		sql.append("  FROM (SELECT TQ.QUOTA_YEAR,\n");
		sql.append("               TQ.QUOTA_WEEK,\n");
		sql.append("               TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("               TVMG2.GROUP_NAME MODEL_NAME,\n");
		sql.append("               TVMG1.GROUP_NAME GROUP_NAME,\n");
		sql.append("               TVMG1.GROUP_CODE GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_ID GROUP_ID,\n");
		sql.append("               SUM(TQD.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("          FROM TT_VS_QUOTA            TQ,\n");
		sql.append("               TT_VS_QUOTA_DETAIL     TQD,");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("               TM_DEALER              TD\n");
		sql.append("         WHERE TQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TQ.QUOTA_ID=TQD.QUOTA_ID");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TQ.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TQD.QUOTA_AMT <> 0\n");
		sql.append("           AND TQ.AREA_ID IN (" + areaIds + ")\n");
		if (!dealerId.equals("")) {
			sql.append("           AND TD.DEALER_ID IN (" + dealerId + ")\n");
		}
		sql.append("           AND TQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("           AND TQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		if (!orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (!endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		sql.append("           AND TQ.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("           AND TQ.AREA_ID = " + areaId + "\n");
		}
		sql.append("         GROUP BY TQ.QUOTA_YEAR,\n");
		sql.append("                  TQ.QUOTA_WEEK,\n");
		sql.append("                  TVMG3.GROUP_NAME,\n");
		sql.append("                  TVMG2.GROUP_NAME,\n");
		sql.append("                  TVMG1.GROUP_NAME,TVMG1.GROUP_CODE,\n");
		sql.append("                  TVMG1.GROUP_ID) A,\n");
		sql.append("       (SELECT /*+ all_rows*/ TVMG1.GROUP_ID, SUM(TQM.MIN_AMOUNT) MIN_AMOUNT\n");
		sql.append("          FROM TT_VS_QUOTA_MIN        TQM,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1, TM_DEALER TD\n");
		sql.append("         WHERE TQM.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TQM.AREA_ID IN (" + areaIds + ")\n");
		if (!dealerId.equals("")) {
			sql.append("           AND TD.DEALER_ID IN (" + dealerId + ")\n");
		}
		if (!orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (!endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		if (!areaId.equals("")) {
			sql.append("           AND TQM.AREA_ID = " + areaId + "\n");
		}
		sql.append("         GROUP BY TVMG1.GROUP_ID) B,\n");
		sql.append("       (SELECT /*+ all_rows*/ TVMG1.GROUP_ID, SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER              TSO,\n");
		sql.append("               TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG1,\n");
		sql.append("               TM_DEALER                TD\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("           AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TSO.ORDER_ORG_ID = TD.DEALER_ID\n");
		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		if (!dealerId.equals("")) {
			sql.append("           AND TD.DEALER_ID IN (" + dealerId + ")\n");
		}
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		if (!orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (!endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		if (!areaId.equals("")) {
			sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		}
		sql.append("         GROUP BY TVMG1.GROUP_ID) C\n");
		sql.append(" WHERE A.GROUP_ID = B.GROUP_ID(+)\n");
		sql.append("   AND A.GROUP_ID = C.GROUP_ID(+)");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}

	public List<Map<String, Object>> getDealerQuotaTransOrderRateTotalExportList(Map<String, Object> map) {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String dealerId = (String) map.get("dealerId");
		String areaIds = (String) map.get("areaIds");
		String companyId = (String) map.get("companyId");

		OrderReportDao reportDao = new OrderReportDao();
		TmBusinessParaPO para = reportDao.getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT A.QUOTA_YEAR || '年' || A.QUOTA_WEEK || '周' QUOTA_DATE,\n");
		sql.append("       A.SERIES_NAME,\n");
		sql.append("       A.MODEL_NAME,\n");
		sql.append("       A.GROUP_NAME,\n");
		sql.append("       A.QUOTA_AMT,\n");
		sql.append("       ROUND(A.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT,\n");
		// sql.append(" NVL(B.MIN_AMOUNT, 0) MIN_AMOUNT,\n");
		sql.append("       NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		sql.append("       (A.QUOTA_AMT - NVL(C.ORDER_AMOUNT, 0)) CY,\n");
		sql.append("       ROUND((NVL(C.ORDER_AMOUNT, 0) * 100 / A.QUOTA_AMT), 2) || '%' WCL\n");
		sql.append("  FROM (SELECT TQ.QUOTA_YEAR,\n");
		sql.append("               TQ.QUOTA_WEEK,\n");
		sql.append("               TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("               TVMG2.GROUP_NAME MODEL_NAME,\n");
		sql.append("               TVMG1.GROUP_NAME GROUP_NAME,\n");
		sql.append("               TVMG1.GROUP_ID GROUP_ID,\n");
		sql.append("               SUM(TQD.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("          FROM TT_VS_QUOTA            TQ,\n");
		sql.append("               TT_VS_QUOTA_DETAIL     TQD,");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("               TM_DEALER              TD\n");
		sql.append("         WHERE TQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TQ.QUOTA_ID=TQD.QUOTA_ID");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TQD.QUOTA_AMT <> 0\n");
		sql.append("           AND TQ.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TQ.AREA_ID IN (" + areaIds + ")\n");
		if (!dealerId.equals("")) {
			sql.append("           AND TD.DEALER_ID IN (" + dealerId + ")\n");
		}
		sql.append("           AND TQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("           AND TQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		if (!orderYear.equals("")) {
			sql.append("           AND TQ.QUOTA_YEAR = " + orderYear + "\n");
		}
		if (!orderWeek.equals("")) {
			sql.append("           AND TQ.QUOTA_WEEK = " + orderWeek + "\n");
		}
		sql.append("         GROUP BY TQ.QUOTA_YEAR,\n");
		sql.append("                  TQ.QUOTA_WEEK,\n");
		sql.append("                  TVMG3.GROUP_NAME,\n");
		sql.append("                  TVMG2.GROUP_NAME,\n");
		sql.append("                  TVMG1.GROUP_NAME,\n");
		sql.append("                  TVMG1.GROUP_ID) A,\n");
		sql.append("       (SELECT TVMG1.GROUP_ID, SUM(TQM.MIN_AMOUNT) MIN_AMOUNT\n");
		sql.append("          FROM TT_VS_QUOTA_MIN        TQM,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1, TM_DEALER TD\n");
		sql.append("         WHERE TQM.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TQM.AREA_ID IN (" + areaIds + ")\n");
		if (!dealerId.equals("")) {
			sql.append("           AND TD.DEALER_ID IN (" + dealerId + ")\n");
		}
		if (!orderYear.equals("")) {
			sql.append("           AND TQM.QUOTA_YEAR = " + orderYear + "\n");
		}
		if (!orderWeek.equals("")) {
			sql.append("           AND TQM.QUOTA_WEEK = " + orderWeek + "\n");
		}
		sql.append("         GROUP BY TVMG1.GROUP_ID) B,\n");
		sql.append("       (SELECT TVMG1.GROUP_ID, SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER              TSO,\n");
		sql.append("               TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG1,\n");
		sql.append("               TM_DEALER                TD\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("           AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TSO.ORDER_ORG_ID = TD.DEALER_ID\n");
		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		if (!dealerId.equals("")) {
			sql.append("           AND TD.DEALER_ID IN (" + dealerId + ")\n");
		}
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		if (!orderYear.equals("")) {
			sql.append("           AND TSO.ORDER_YEAR = " + orderYear + "\n");
		}
		if (!orderWeek.equals("")) {
			sql.append("           AND TSO.ORDER_WEEK = " + orderWeek + "\n");
		}
		sql.append("         GROUP BY TVMG1.GROUP_ID) C\n");
		sql.append(" WHERE A.GROUP_ID = B.GROUP_ID(+)\n");
		sql.append("   AND A.GROUP_ID = C.GROUP_ID(+)");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getDealerQuotaTransOrderRateTotalExportList");
		return list;
	}

	public PageResult<Map<String, Object>> getDealerQuotaTransOrderRateDetailQueryList(Map<String, Object> map, int curPage, int pageSize) {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String dealerId = (String) map.get("dealerId");
		String areaIds = (String) map.get("areaIds");
		String companyId = (String) map.get("companyId");

		OrderReportDao reportDao = new OrderReportDao();
		TmBusinessParaPO para = reportDao.getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT A.QUOTA_YEAR || '年' || A.QUOTA_WEEK || '周' QUOTA_DATE,\n");
		sql.append("       A.DEALER_CODE,\n");
		sql.append("       A.DEALER_NAME,\n");
		sql.append("       A.QUOTA_AMT,\n");
		sql.append("       ROUND(A.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT,\n");
		// sql.append(" NVL(B.MIN_AMOUNT, 0) MIN_AMOUNT,\n");
		sql.append("       NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		sql.append("       (A.QUOTA_AMT - NVL(C.ORDER_AMOUNT, 0)) CY,\n");
		sql.append("       ROUND((NVL(C.ORDER_AMOUNT, 0) * 100 / A.QUOTA_AMT), 2) || '%' WCL\n");
		sql.append("  FROM (SELECT TQ.QUOTA_YEAR,\n");
		sql.append("               TQ.QUOTA_WEEK,\n");
		sql.append("               TD.DEALER_ID,\n");
		sql.append("               TD.DEALER_CODE,\n");
		sql.append("               TD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("               SUM(TQD.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("          FROM TT_VS_QUOTA TQ,TT_VS_QUOTA_DETAIL TQD, TM_DEALER TD\n");
		sql.append("         WHERE TQ.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TQ.QUOTA_ID=TQD.QUOTA_ID");
		sql.append("           AND TQD.QUOTA_AMT <> 0");
		sql.append("           AND TQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("           AND TQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		sql.append("           AND TQ.AREA_ID IN (" + areaIds + ")\n");
		sql.append("           AND TQ.DEALER_ID IN (" + dealerId + ")\n");
		if (!orderYear.equals("")) {
			sql.append("           AND TQ.QUOTA_YEAR = " + orderYear + "\n");
		}
		if (!orderWeek.equals("")) {
			sql.append("           AND TQ.QUOTA_WEEK = " + orderWeek + "\n");
		}
		sql.append("         GROUP BY TQ.QUOTA_YEAR,\n");
		sql.append("                  TQ.QUOTA_WEEK,\n");
		sql.append("                  TD.DEALER_ID,\n");
		sql.append("                  TD.DEALER_CODE,\n");
		sql.append("                  TD.DEALER_SHORTNAME) A,\n");
		sql.append("       (SELECT TD.DEALER_ID, SUM(TQM.MIN_AMOUNT) MIN_AMOUNT\n");
		sql.append("          FROM TT_VS_QUOTA_MIN TQM, TM_DEALER TD\n");
		sql.append("         WHERE TQM.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TQM.AREA_ID IN (" + areaIds + ")\n");
		sql.append("           AND TQM.DEALER_ID IN (" + dealerId + ")\n");
		if (!orderYear.equals("")) {
			sql.append("           AND TQM.QUOTA_YEAR = " + orderYear + "\n");
		}
		if (!orderWeek.equals("")) {
			sql.append("           AND TQM.QUOTA_WEEK = " + orderWeek + "\n");
		}
		sql.append("         GROUP BY TD.DEALER_ID) B,\n");
		sql.append("       (SELECT TD.DEALER_ID, SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER TSO, TT_VS_ORDER_DETAIL TSOD, TM_DEALER TD\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TSO.ORDER_ORG_ID = TD.DEALER_ID\n");
		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		sql.append("           AND TSO.ORDER_ORG_ID IN (" + dealerId + ")\n");
		if (!orderYear.equals("")) {
			sql.append("           AND TSO.ORDER_YEAR = " + orderYear + "\n");
		}
		if (!orderWeek.equals("")) {
			sql.append("           AND TSO.ORDER_WEEK = " + orderWeek + "\n");
		}
		sql.append("         GROUP BY TD.DEALER_ID) C\n");
		sql.append(" WHERE A.DEALER_ID = B.DEALER_ID(+)\n");
		sql.append("   AND A.DEALER_ID = C.DEALER_ID(+)");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getDealerQuotaTransOrderRateDetailQueryList", pageSize, curPage);
		return ps;
	}

	public List<Map<String, Object>> getDealerQuotaTransOrderRateDetailExportList(Map<String, Object> map) {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String dealerId = (String) map.get("dealerId");
		String areaIds = (String) map.get("areaIds");
		String companyId = (String) map.get("companyId");

		OrderReportDao reportDao = new OrderReportDao();
		TmBusinessParaPO para = reportDao.getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT A.QUOTA_YEAR || '年' || A.QUOTA_WEEK || '周' QUOTA_DATE,\n");
		sql.append("       A.DEALER_CODE,\n");
		sql.append("       A.DEALER_NAME,\n");
		sql.append("       A.QUOTA_AMT,\n");
		sql.append("       ROUND(A.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT,\n");
		// sql.append(" NVL(B.MIN_AMOUNT, 0) MIN_AMOUNT,\n");
		sql.append("       NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		sql.append("       (A.QUOTA_AMT - NVL(C.ORDER_AMOUNT, 0)) CY,\n");
		sql.append("       ROUND((NVL(C.ORDER_AMOUNT, 0) * 100 / A.QUOTA_AMT), 2) || '%' WCL\n");
		sql.append("  FROM (SELECT TQ.QUOTA_YEAR,\n");
		sql.append("               TQ.QUOTA_WEEK,\n");
		sql.append("               TD.DEALER_ID,\n");
		sql.append("               TD.DEALER_CODE,\n");
		sql.append("               TD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("               SUM(TQD.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("          FROM TT_VS_QUOTA TQ,TT_VS_QUOTA_DETAIL TQD, TM_DEALER TD\n");
		sql.append("         WHERE TQ.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TQ.QUOTA_ID=TQD.QUOTA_ID");
		sql.append("           AND TQD.QUOTA_AMT <> 0");
		sql.append("           AND TQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("           AND TQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		sql.append("           AND TQ.AREA_ID IN (" + areaIds + ")\n");
		sql.append("           AND TQ.DEALER_ID IN (" + dealerId + ")\n");
		if (!orderYear.equals("")) {
			sql.append("           AND TQ.QUOTA_YEAR = " + orderYear + "\n");
		}
		if (!orderWeek.equals("")) {
			sql.append("           AND TQ.QUOTA_WEEK = " + orderWeek + "\n");
		}
		sql.append("         GROUP BY TQ.QUOTA_YEAR,\n");
		sql.append("                  TQ.QUOTA_WEEK,\n");
		sql.append("                  TD.DEALER_ID,\n");
		sql.append("                  TD.DEALER_CODE,\n");
		sql.append("                  TD.DEALER_SHORTNAME) A,\n");
		sql.append("       (SELECT TD.DEALER_ID, SUM(TQM.MIN_AMOUNT) MIN_AMOUNT\n");
		sql.append("          FROM TT_VS_QUOTA_MIN TQM, TM_DEALER TD\n");
		sql.append("         WHERE TQM.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TQM.AREA_ID IN (" + areaIds + ")\n");
		sql.append("           AND TQM.DEALER_ID IN (" + dealerId + ")\n");
		if (!orderYear.equals("")) {
			sql.append("           AND TQM.QUOTA_YEAR = " + orderYear + "\n");
		}
		if (!orderWeek.equals("")) {
			sql.append("           AND TQM.QUOTA_WEEK = " + orderWeek + "\n");
		}
		sql.append("         GROUP BY TD.DEALER_ID) B,\n");
		sql.append("       (SELECT TD.DEALER_ID, SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER TSO, TT_VS_ORDER_DETAIL TSOD, TM_DEALER TD\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TSO.ORDER_ORG_ID = TD.DEALER_ID\n");
		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.STATUS_ENABLE + "\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		sql.append("           AND TSO.ORDER_ORG_ID IN (" + dealerId + ")\n");
		if (!orderYear.equals("")) {
			sql.append("           AND TSO.ORDER_YEAR = " + orderYear + "\n");
		}
		if (!orderWeek.equals("")) {
			sql.append("           AND TSO.ORDER_WEEK = " + orderWeek + "\n");
		}
		sql.append("         GROUP BY TD.DEALER_ID) C\n");
		sql.append(" WHERE A.DEALER_ID = B.DEALER_ID(+)\n");
		sql.append("   AND A.DEALER_ID = C.DEALER_ID(+)");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getDealerQuotaTransOrderRateDetailExportList");
		return list;
	}

	public PageResult<Map<String, Object>> deliveryOrderQuery(String startDate,String endDate,String dealerCodes,String myOrderType, String myOrderNo, String regionId, String pageOrgId, Long orgId, String isDaijiao, String areaIds, String dutyType, String dealerId, Long companyId, String beginTime, String endTime, String areaId, String dealerCode, String orderNo, String transType, String reqStatus, String oldOrderNo, int curPage, int pageSize) {
		boolean orgFlag = Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType) || (!CommonUtils.isNullString(pageOrgId) && Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType) && !orgId.toString().equals(pageOrgId));
		System.out.println(myOrderType);
		System.out.println(myOrderNo);
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			dealerCodes="'"+dealerCodes.replaceAll(",", "','")+"'";
		}
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT E.REQ_ID,\n");
		sql.append("DECODE(E.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(",TMD.DEALER_SHORTNAME, B.DEALER_SHORTNAME) DEALER_NAME,\n");
		sql.append("       C.DEALER_SHORTNAME DEALER_NAME1, C.DEALER_CODE,\n");
		sql.append("       D.DEALER_SHORTNAME DEALER_NAME2, A.ORDER_TYPE,\n");
		sql.append("       E.DELIVERY_TYPE, nvl(G.RESERVE_AMOUNT,0) RESERVE_AMOUNT, E.REQ_STATUS,\n");
		sql.append("       E.REQ_TOTAL_AMOUNT, E.DLVRY_REQ_NO, A.ORDER_NO,\n");
		sql.append("       TO_CHAR(E.REQ_DATE, 'YYYY-MM-DD') REQ_DATE, H.ORDER_NO OLD_ORDER_NO,TO_CHAR(E.AUDIT_TIME,'YYYY-MM-DD')UPDATE_DATE\n");
		sql.append("FROM TT_VS_ORDER A, TM_DEALER B, TM_DEALER C, TM_DEALER D,TM_DEALER TMD,\n");
		sql.append("     TT_VS_DLVRY_REQ E,TM_VS_ADDRESS F,\n");
		sql.append("     (\n");
		sql.append("        SELECT  DRD.REQ_ID,SUM(NVL(AMOUNT, 0)) RESERVE_AMOUNT\n");
		sql.append("        FROM TT_VS_ORDER_RESOURCE_RESERVE ORR,TT_VS_DLVRY_REQ_DTL DRD\n");
		sql.append("       WHERE ORR.REQ_DETAIL_ID = DRD.DETAIL_ID\n");
		sql.append("         AND ORR.RESERVE_STATUS in (" + Constant.RESOURCE_RESERVE_STATUS_01 + "," + Constant.RESOURCE_RESERVE_STATUS_03 + ")\n");
		sql.append("        GROUP BY  DRD.REQ_ID\n");
		sql.append("     ) G,TT_VS_ORDER H\n");

		if (orgFlag) {
			sql.append(",\n");
			sql.append("	   vw_org_dealer           VOD\n");
		}

		sql.append("WHERE A.ORDER_ORG_ID = B.DEALER_ID\n");
		sql.append("AND A.ORDER_ID = E.ORDER_ID\n");
		sql.append("AND A.OLD_ORDER_ID = H.ORDER_ID(+)\n");
		sql.append("AND E.ORDER_DEALER_ID = TMD.DEALER_ID(+)\n");
		sql.append("AND A.BILLING_ORG_ID = C.DEALER_ID\n");
		sql.append("AND E.RECEIVER = D.DEALER_ID(+)\n");
		sql.append("AND E.ADDRESS_ID = F.ID(+)\n");
		sql.append("AND E.REQ_ID = G.REQ_ID(+)\n");
		if (!"".equals(myOrderNo) && myOrderNo != null) {
			sql.append("AND A.ORDER_NO='" + myOrderNo + "'\n");
		}
		if (!"".equals(myOrderType) && myOrderType != null) {
			sql.append("AND A.ORDER_TYPE='" + myOrderType + "'\n");
		}
		if (orgFlag) {
			sql.append("AND A.BILLING_ORG_ID = VOD.DEALER_ID\n");

			if (!CommonUtils.isNullString(pageOrgId)) {
				orgId = Long.parseLong(pageOrgId);
			}

			sql.append("AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
		}

		if (!CommonUtils.isNullString(regionId)) {
			sql.append("AND C.PROVINCE_ID = ").append(regionId).append("\n");
		}

		sql.append("AND A.COMPANY_ID = ").append(companyId).append("\n");
		if (Utility.testString(areaId)) {
			sql.append("AND E.AREA_ID = ").append(areaId).append("\n");
		}
		if (!"".equals(areaIds) && "".equals(areaId)) {
			sql.append("AND E.AREA_ID IN (" + areaIds + ")\n");
		}
		if (dutyType.equals(String.valueOf(Constant.DUTY_TYPE_DEALER))) {
			sql.append("AND (C.DEALER_ID IN (").append(dealerId).append("))\n");
		} else {
			if (Utility.testString(dealerCode)) {
				sql.append("AND C.DEALER_CODE IN (").append(dealerCode).append(")\n");
			}
		}
		if (Utility.testString(transType)) {
			sql.append("AND E.DELIVERY_TYPE = ").append(transType).append("\n");
		}
		if (Utility.testString(reqStatus)) {
			sql.append("AND E.REQ_STATUS = ").append(reqStatus).append("\n");
		}
		if (Utility.testString(beginTime)) {
			sql.append("AND E.REQ_DATE >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (Utility.testString(endTime)) {
			sql.append("AND E.REQ_DATE <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (Utility.testString(startDate)) {
			sql.append("AND E.AUDIT_TIME >= TO_DATE('").append(startDate).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (Utility.testString(endDate)) {
			sql.append("AND E.AUDIT_TIME <= TO_DATE('").append(endDate).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (Utility.testString(orderNo)) {
			sql.append("AND E.DLVRY_REQ_NO LIKE '%").append(orderNo).append("%'\n");
		}
		if (Utility.testString(oldOrderNo)) {
			sql.append("AND H.ORDER_NO LIKE '%").append(oldOrderNo).append("%'\n");
		}
		if (!"".equals(isDaijiao)) {
			if ((Constant.IF_TYPE_YES + "").equals(isDaijiao)) {
				sql.append("   AND E.IS_FLEET = 1\n");
			} else {
				sql.append("   AND E.IS_FLEET = 0\n");
			}
		}
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			sql.append(" AND  B.DEALER_CODE IN("+dealerCodes+")");
		}
		if (Utility.testString(startDate)||Utility.testString(endDate)){
			sql.append("   ORDER BY E.AUDIT_TIME DESC\n");
		}
		if (Utility.testString(beginTime)||Utility.testString(endTime)){
			sql.append("   ORDER BY E.REQ_DATE DESC\n");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

	public List<Map<String, Object>> deliveryOrderExport(String startDate,String endDate,String myOrderNo, String myOrderType, String regionId, String pageOrgId, Long orgId, String isDaijiao, String areaIds, String dutyType, String dealerId, Long companyId, String beginTime, String endTime, String areaId, String dealerCode, String orderNo, String transType, String reqStatus, String oldOrderNo) {
		boolean orgFlag = Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType) || (!CommonUtils.isNullString(pageOrgId) && Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType) && !orgId.toString().equals(pageOrgId));

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT E.REQ_ID, B.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("       C.DEALER_SHORTNAME DEALER_NAME1, C.DEALER_CODE,\n");
		sql.append("       D.DEALER_SHORTNAME DEALER_NAME2, I.CODE_DESC ORDER_TYPE,\n");
		sql.append("       K.CODE_DESC DELIVERY_TYPE, nvl(G.RESERVE_AMOUNT,0) RESERVE_AMOUNT, J.CODE_DESC REQ_STATUS,\n");
		sql.append("       E.REQ_TOTAL_AMOUNT, E.DLVRY_REQ_NO, A.ORDER_NO,\n");
		sql.append("       TO_CHAR(E.REQ_DATE, 'YYYY-MM-DD') REQ_DATE, H.ORDER_NO OLD_ORDER_NO,TO_CHAR(E.UPDATE_DATE,'YYYY-MM-DD')UPDATE_DATE\n");
		sql.append("FROM TT_VS_ORDER A, TM_DEALER B, TM_DEALER C, TM_DEALER D,\n");
		sql.append("     TT_VS_DLVRY_REQ E,TM_VS_ADDRESS F,\n");
		sql.append("     (\n");
		sql.append("        SELECT  DRD.REQ_ID,SUM(NVL(AMOUNT, 0)) RESERVE_AMOUNT\n");
		sql.append("        FROM TT_VS_ORDER_RESOURCE_RESERVE ORR,TT_VS_DLVRY_REQ_DTL DRD\n");
		sql.append("       WHERE ORR.REQ_DETAIL_ID = DRD.DETAIL_ID\n");
		sql.append("         AND ORR.RESERVE_STATUS in (" + Constant.RESOURCE_RESERVE_STATUS_01 + "," + Constant.RESOURCE_RESERVE_STATUS_03 + ")\n");
		sql.append("        GROUP BY  DRD.REQ_ID\n");
		sql.append("     ) G,TT_VS_ORDER H, TC_CODE I, TC_CODE J, TC_CODE K\n");

		if (orgFlag) {
			sql.append(",\n");
			sql.append("	   vw_org_dealer           VOD\n");
		}

		sql.append("WHERE A.ORDER_ORG_ID = B.DEALER_ID\n");
		sql.append("AND A.ORDER_ID = E.ORDER_ID\n");
		sql.append("AND A.OLD_ORDER_ID = H.ORDER_ID(+)\n");
		sql.append("AND A.BILLING_ORG_ID = C.DEALER_ID\n");
		sql.append("AND E.RECEIVER = D.DEALER_ID(+)\n");
		sql.append("AND E.ADDRESS_ID = F.ID(+)\n");
		sql.append("AND E.REQ_ID = G.REQ_ID(+)\n");
		sql.append("AND A.ORDER_TYPE = I.CODE_ID\n");
		sql.append("AND E.REQ_STATUS = J.CODE_ID\n");
		sql.append("AND E.DELIVERY_TYPE = K.CODE_ID(+)\n");
		if (!"".equals(myOrderNo) && myOrderNo != null) {
			sql.append("AND A.ORDER_NO='" + myOrderNo + "'\n");
		}
		if (!"".equals(myOrderType) && myOrderType != null) {
			sql.append("AND A.ORDER_TYPE='" + myOrderType + "'\n");
		}
		if (orgFlag) {
			sql.append("AND A.BILLING_ORG_ID = VOD.DEALER_ID\n");

			if (!CommonUtils.isNullString(pageOrgId)) {
				orgId = Long.parseLong(pageOrgId);
			}

			sql.append("AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
		}

		if (!CommonUtils.isNullString(regionId)) {
			sql.append("AND C.PROVINCE_ID = ").append(regionId).append("\n");
		}

		sql.append("AND A.COMPANY_ID = ").append(companyId).append("\n");
		if (Utility.testString(areaId)) {
			sql.append("AND E.AREA_ID = ").append(areaId).append("\n");
		}
		if (!"".equals(areaIds) && "".equals(areaId)) {
			sql.append("AND E.AREA_ID IN (" + areaIds + ")\n");
		}
		if (dutyType.equals(String.valueOf(Constant.DUTY_TYPE_DEALER))) {
			sql.append("AND (C.DEALER_ID IN (").append(dealerId).append("))\n");
		} else {
			if (Utility.testString(dealerCode)) {
				sql.append("AND C.DEALER_CODE IN (").append(dealerCode).append(")\n");
			}
		}
		if (Utility.testString(transType)) {
			sql.append("AND E.DELIVERY_TYPE = ").append(transType).append("\n");
		}
		if (Utility.testString(reqStatus)) {
			sql.append("AND E.REQ_STATUS = ").append(reqStatus).append("\n");
		}
		if (Utility.testString(beginTime)) {
			sql.append("AND E.REQ_DATE >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (Utility.testString(endTime)) {
			sql.append("AND E.REQ_DATE <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
			
		}
		if (Utility.testString(startDate)) {
			sql.append("AND E.AUDIT_TIME >= TO_DATE('").append(startDate).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (Utility.testString(endDate)) {
			sql.append("AND E.AUDIT_TIME <= TO_DATE('").append(endDate).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (Utility.testString(orderNo)) {
			sql.append("AND E.DLVRY_REQ_NO LIKE '%").append(orderNo).append("%'\n");
		}
		if (Utility.testString(oldOrderNo)) {
			sql.append("AND H.ORDER_NO LIKE '%").append(oldOrderNo).append("%'\n");
		}
		if (!"".equals(isDaijiao)) {
			if ((Constant.IF_TYPE_YES + "").equals(isDaijiao)) {
				sql.append("   AND E.IS_FLEET = 1\n");
			} else {
				sql.append("   AND E.IS_FLEET = 0\n");
			}
		}
		if (Utility.testString(startDate)||Utility.testString(endDate)){
			sql.append("   ORDER BY E.AUDIT_TIME DESC\n");
		}
		if (Utility.testString(beginTime)||Utility.testString(endTime)){
			sql.append("   ORDER BY E.REQ_DATE DESC\n");
		}

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	/**
	 * 发运订单查询（二级经销商）
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> dlvryOrderQuery(Map<String, Object> map, int curPage, int pageSize) {
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");
		String areaId = (String) map.get("areaId");
		String transType = (String) map.get("transType");
		String reqStatus = (String) map.get("reqStatus");
		String orderNo = (String) map.get("orderNo");
		String initOrderNo = (String) map.get("initOrderNo");
		String dlvryOrderNo = (String) map.get("dlvryOrderNo");
		String isFleet = (String) map.get("isFleet");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		String dealerIds = (String) map.get("dealerIds");
		String orderType = map.get("orderType").toString();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT E.REQ_ID, DECODE(E.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD.DEALER_SHORTNAME, B.DEALER_SHORTNAME) DEALER_NAME,\n");
		sql.append("       C.DEALER_SHORTNAME DEALER_NAME1, C.DEALER_CODE,\n");
		sql.append("       D.DEALER_SHORTNAME DEALER_NAME2, A.ORDER_TYPE,\n");
		sql.append("       E.DELIVERY_TYPE, nvl(G.RESERVE_AMOUNT,0) RESERVE_AMOUNT, E.REQ_STATUS,\n");
		sql.append("       E.REQ_TOTAL_AMOUNT, E.DLVRY_REQ_NO, A.ORDER_NO,\n");
		sql.append("       TO_CHAR(E.REQ_DATE, 'YYYY-MM-DD') REQ_DATE, H.ORDER_NO INIT_ORDER_NO, DECODE(E.IS_FLEET, 1, '是', '否') IS_FLEET\n");
		sql.append("FROM TT_VS_ORDER A, TM_DEALER B, TM_DEALER C, TM_DEALER D, TM_DEALER TMD,\n");
		sql.append("     TT_VS_DLVRY_REQ E,TM_VS_ADDRESS F,\n");
		sql.append("     (\n");
		sql.append("        SELECT  DRD.REQ_ID,SUM(NVL(AMOUNT, 0)) RESERVE_AMOUNT\n");
		sql.append("        FROM TT_VS_ORDER_RESOURCE_RESERVE ORR,TT_VS_DLVRY_REQ_DTL DRD\n");
		sql.append("       WHERE ORR.REQ_DETAIL_ID = DRD.DETAIL_ID\n");
		sql.append("         AND ORR.RESERVE_STATUS in (" + Constant.RESOURCE_RESERVE_STATUS_01 + "," + Constant.RESOURCE_RESERVE_STATUS_03 + ")\n");
		sql.append("        GROUP BY  DRD.REQ_ID\n");
		sql.append("     ) G, TT_VS_ORDER H\n");
		sql.append("WHERE A.ORDER_ORG_ID = B.DEALER_ID\n");
		sql.append("AND E.ORDER_DEALER_ID = TMD.DEALER_ID(+)\n");
		sql.append("AND A.ORDER_ID = E.ORDER_ID\n");
		sql.append("AND A.BILLING_ORG_ID = C.DEALER_ID\n");
		sql.append("AND E.RECEIVER = D.DEALER_ID(+)\n");
		sql.append("AND E.ADDRESS_ID = F.ID(+)\n");
		sql.append("AND E.REQ_ID = G.REQ_ID(+)\n");
		sql.append("AND A.OLD_ORDER_ID = H.ORDER_ID(+)\n");
		sql.append("AND A.COMPANY_ID = ").append(companyId).append("\n");
		if (Utility.testString(beginTime)) {
			sql.append("AND E.REQ_DATE >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (Utility.testString(endTime)) {
			sql.append("AND E.REQ_DATE <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if (Utility.testString(areaId)) {
			sql.append("AND E.AREA_ID = ").append(areaId).append("\n");
		}
		if (Utility.testString(transType)) {
			sql.append("AND E.DELIVERY_TYPE = ").append(transType).append("\n");
		}
		if (Utility.testString(reqStatus)) {
			sql.append("AND E.REQ_STATUS = ").append(reqStatus).append("\n");
		}
		if (Utility.testString(orderNo)) {
			sql.append("AND A.ORDER_NO LIKE '%").append(orderNo).append("%'\n");
		}
		if (Utility.testString(initOrderNo)) {
			sql.append("AND H.ORDER_NO LIKE '%").append(initOrderNo).append("%'\n");
		}
		if (Utility.testString(dlvryOrderNo)) {
			sql.append("AND E.DLVRY_REQ_NO LIKE '%").append(dlvryOrderNo).append("%'\n");
		}
		if (!CommonUtils.isNullString(orderType)) {
			sql.append("AND A.ORDER_TYPE = ").append(orderType).append("\n");
		}
		if (Utility.testString(isFleet)) {
			if (Integer.parseInt(isFleet) == Constant.IF_TYPE_YES) {
				sql.append("AND E.IS_FLEET = 1\n");
			} else {
				sql.append("AND E.IS_FLEET = 0\n");
			}
		}
		if (Utility.testString(areaIds)) {
			sql.append("AND E.AREA_ID IN (" + areaIds + ")\n");
		}
		// if (Utility.testString(dealerIds)) {
		sql.append("AND (A.ORDER_ORG_ID IN (" + dealerIds + ") OR E.ORDER_DEALER_ID IN (" + dealerIds + "))\n");
		// }

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

	public static String getDlr(String dealerIds, String DlrLeavel) {
		StringBuffer newDlrIds = new StringBuffer("");

		StringBuffer sql = new StringBuffer("\n");

		sql.append("select tmd.dealer_id from tm_dealer tmd where tmd.status = ").append(Constant.STATUS_ENABLE).append(" and tmd.dealer_id in (").append(dealerIds).append(") and tmd.dealer_level = ").append(DlrLeavel).append("\n");

		List<Map<String, Object>> dlrList = dao.pageQuery(sql.toString(), null, dao.getFunName());

		if (!CommonUtils.isNullList(dlrList)) {
			int len = dlrList.size();

			for (int i = 0; i < len; i++) {
				if (newDlrIds.length() <= 0) {
					newDlrIds.append(dlrList.get(i).get("DEALER_ID").toString());
				} else {
					newDlrIds.append(",").append(dlrList.get(i).get("DEALER_ID").toString());
				}
			}
		}

		return newDlrIds.toString();
	}

	public PageResult<Map<String, Object>> getOemQuotaTransOrderRateTotalQueryListCVS(Map<String, Object> map, int curPage, int pageSize) {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String areaId = (String) map.get("areaId");
		String dealerCode = (String) map.get("dealerCode");
		String dealerSql = (String) map.get("dealerSql");
		String dealerId = (String) map.get("dealerId");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		String regionId = (String) map.get("regionId");
		String endYear = (String) map.get("endYear");
		String endWeek = (String) map.get("endWeek");
		String groupCode = (String) map.get("groupCode");

		OrderReportDao reportDao = new OrderReportDao();
		TmBusinessParaPO para = reportDao.getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT /*+ all_rows*/A.QUOTA_YEAR || '年' || A.QUOTA_WEEK || '周' QUOTA_DATE,\n");
		sql.append("       A.SERIES_NAME,\n");
		sql.append("       A.MODEL_NAME,\n");
		sql.append("       A.GROUP_NAME,\n");
		sql.append("       A.GROUP_CODE,\n");
		sql.append("       A.QUOTA_AMT,\n");
		sql.append("       ROUND(A.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT,\n");
		// sql.append(" NVL(B.MIN_AMOUNT, 0) MIN_AMOUNT,\n");
		sql.append("       NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		sql.append("	   nvl(c.check_AMOUNT, 0) CHECK_AMOUNT,\n");
		sql.append("       (A.QUOTA_AMT - NVL(C.ORDER_AMOUNT, 0)) CY,\n");
		sql.append("       nvl(d.req_amount, 0) req_amount,\n");
		sql.append("       nvl(d.reserve_amount, 0) reserve_amount,\n");
		sql.append("       nvl(e.delivery_amount, 0) delivery_amount,\n");
		sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 / NVL(c.check_amount, 1)), 2) || '%' ZXL,\n");
		sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 / decode(nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0), 0, 1,nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0))), 2) || '%' ZBL,\n");
		sql.append("       ROUND((NVL(C.ORDER_AMOUNT, 0) * 100 / A.QUOTA_AMT), 2) || '%' WCL\n");
		sql.append("  FROM (SELECT /*+ all_rows*/TQ.QUOTA_YEAR,\n");
		sql.append("               TQ.QUOTA_WEEK,\n");
		sql.append("               TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("               TVMG2.GROUP_NAME MODEL_NAME,\n");
		sql.append("               TVMG1.GROUP_NAME GROUP_NAME,\n");
		sql.append("               TVMG1.GROUP_CODE GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_ID GROUP_ID,\n");
		sql.append("               SUM(TQD.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("          FROM TT_VS_QUOTA            TQ,\n");
		sql.append("               TT_VS_QUOTA_DETAIL     TQD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("               TM_DEALER              TD\n");
		sql.append("         WHERE TQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TQ.QUOTA_ID=TQD.QUOTA_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("AND TQD.QUOTA_AMT <> 0\n");
		sql.append("           AND TQ.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TQ.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("           AND TQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if () { sql.append(" AND TQ.QUOTA_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TQ.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("           AND TQ.AREA_ID = " + areaId + "\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append("         GROUP BY TQ.QUOTA_YEAR,\n");
		sql.append("                  TQ.QUOTA_WEEK,\n");
		sql.append("                  TVMG3.GROUP_NAME,\n");
		sql.append("                  TVMG2.GROUP_NAME,\n");
		sql.append("                  TVMG1.GROUP_NAME,TVMG1.GROUP_CODE,\n");
		sql.append("                  TVMG1.GROUP_ID) A,\n");
		sql.append("       (SELECT /*+ all_rows*/TVMG1.GROUP_ID,\n");
		sql.append("               TVMG1.GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_NAME,\n");
		sql.append("tqm.quota_year,\n");
		sql.append("              tqm.quota_week,\n");
		sql.append("               SUM(TQM.MIN_AMOUNT) MIN_AMOUNT\n");
		sql.append("          FROM TT_VS_QUOTA_MIN        TQM,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("               TM_DEALER              TD\n");
		sql.append("         WHERE TQM.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TQM.DEALER_ID = TD.DEALER_ID\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}

		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TQM.QUOTA_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TQM.QUOTA_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TQM.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TQM.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append("         GROUP BY tqm.quota_year, tqm.quota_week,TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME) B,\n");
		sql.append("       (SELECT /*+ all_rows*/TVMG1.GROUP_ID,\n");
		sql.append("               TVMG1.GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_NAME,\n");
		sql.append("TSO.Order_Year,\n");
		sql.append("               tso.order_week,\n");
		sql.append("               SUM(TSOD.check_AMOUNT) check_AMOUNT,\n");
		sql.append("               SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER              TSO,\n");
		sql.append("               TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG3,\n");
		sql.append("               TM_DEALER                TD\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("           AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TSO.ORDER_ORG_ID = TD.DEALER_ID\n");
		sql.append("           AND TSO.COMPANY_ID = " + companyId + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TSO.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TSO.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append("         GROUP BY TSO.Order_Year,tso.order_week,TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME) C,\n");
		sql.append("(select /*+ all_rows*/tvmg1.group_id,\n");
		sql.append("               tvmg1.group_code,\n");
		sql.append("               tvmg1.group_name,\n");
		sql.append("tvo.order_year,\n");
		sql.append("               tvo.order_week,\n");
		sql.append("               sum(tvdrd.req_amount) req_amount,\n");
		sql.append("               sum(tvdrd.reserve_amount) reserve_amount\n");
		sql.append("          from tt_vs_order              tvo,\n");
		sql.append("               tt_vs_dlvry_req          tvdr,\n");
		sql.append("               tt_vs_dlvry_req_dtl      tvdrd,\n");
		sql.append("               tm_vhcl_material_group_r tvmgr,\n");
		sql.append("               tm_vhcl_material_group   tvmg1,\n");
		sql.append("               tm_vhcl_material_group   tvmg2,\n");
		sql.append("               tm_vhcl_material_group   tvmg3,\n");
		sql.append("               tm_dealer                td\n");
		sql.append("         where tvo.order_id = tvdr.order_id\n");
		sql.append("           and tvdr.req_id = tvdrd.req_id\n");
		sql.append("           and tvdrd.material_id = tvmgr.material_id\n");
		sql.append("           and tvmgr.group_id = tvmg1.group_id\n");
		sql.append("           and tvmg1.parent_group_id = tvmg2.group_id\n");
		sql.append("           and tvmg2.parent_group_id = tvmg3.group_id\n");
		sql.append("           and tvo.order_org_id = td.dealer_id\n");
		sql.append("           AND tvo.COMPANY_ID = " + companyId + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		sql.append("           AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND tvo.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");

		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		/*
		 * if (!orderYear.equals("")) { sql.append(" AND tvo.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * tvo.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvo.AREA_ID = " + areaId + "\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		sql.append("           and tvdr.req_status not in (").append(Constant.ORDER_REQ_STATUS_07).append(",").append(Constant.ORDER_REQ_STATUS_YSH).append(")\n");
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND td.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append("         group by tvo.order_year,tvo.order_week,tvmg1.group_id, tvmg1.group_code, tvmg1.group_name) d,\n");
		sql.append("(select /*+ all_rows*/\n");
		sql.append("         ttt.group_id,\n");
		sql.append("         ttt.group_code,\n");
		sql.append("         ttt.group_name,\n");
		sql.append("         ttt.order_year,\n");
		sql.append("         ttt.order_week,\n");
		sql.append("         sum(ttt.c_delivery_amount) delivery_amount,\n");
		sql.append("         sum(ttt.b_delivery_amount) b_delivery_amount,\n");
		sql.append("         sum(ttt.d_delivery_amount) d_delivery_amount\n");
		sql.append("from \n");
		sql.append("       (select /*+ all_rows*/tvmg1.group_id,\n");
		sql.append("               tvmg1.group_code,\n");
		sql.append("               tvmg1.group_name,\n");
		sql.append("tvo.order_year,\n");
		sql.append("               tvo.order_week,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_01).append(", sum(tvdd.delivery_amount), 0) c_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_02).append(", sum(tvdd.delivery_amount), 0) b_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_03).append(", sum(tvdd.delivery_amount), 0) d_delivery_amount\n");
		// sql.append(" sum(tvdd.delivery_amount) delivery_amount\n");
		sql.append("          from tt_vs_order              tvo,\n");
		sql.append("               tt_vs_dlvry              tvd,\n");
		sql.append("               tt_vs_dlvry_dtl          tvdd,\n");
		sql.append("               tm_vhcl_material_group_r tvmgr,\n");
		sql.append("               tm_vhcl_material_group   tvmg1,\n");
		sql.append("               tm_vhcl_material_group   tvmg2,\n");
		sql.append("               tm_vhcl_material_group   tvmg3,\n");
		sql.append("               tm_dealer                td\n");
		sql.append("         where tvo.order_id = tvd.order_id\n");
		sql.append("           and tvd.delivery_id = tvdd.delivery_id\n");
		sql.append("           and tvdd.material_id = tvmgr.material_id\n");
		sql.append("           and tvmgr.group_id = tvmg1.group_id\n");
		sql.append("           and tvmg1.parent_group_id = tvmg2.group_id\n");
		sql.append("           and tvmg2.parent_group_id = tvmg3.group_id\n");
		sql.append("           and tvo.order_org_id = td.dealer_id\n");
		sql.append("           AND tvo.COMPANY_ID = " + companyId + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		/*
		 * sql.append(" AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		 */
		sql.append("           AND tvo.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND tvo.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * tvo.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvo.AREA_ID = " + areaId + "\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		sql.append("           and tvd.delivery_status in\n");
		sql.append("               (").append(Constant.DELIVERY_STATUS_04).append(",").append(Constant.DELIVERY_STATUS_05).append(",").append(Constant.DELIVERY_STATUS_10).append(",").append(Constant.DELIVERY_STATUS_11).append(",").append(Constant.DELIVERY_STATUS_12).append(")\n");
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND td.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append("         group by tvo.ORDER_TYPE, tvo.order_year,tvo.order_week,tvmg1.group_id, tvmg1.group_code, tvmg1.group_name) ttt\n");
		sql.append("group by ttt.group_id,\n");
		sql.append("                  ttt.group_code,\n");
		sql.append("                  ttt.group_name,\n");
		sql.append("                  ttt.order_year,\n");
		sql.append("                  ttt.order_week) e\n");

		sql.append(" WHERE A.GROUP_ID = B.GROUP_ID(+)\n");
		sql.append("   AND A.GROUP_ID = C.GROUP_ID(+)");
		sql.append("   and a.group_id = d.group_id(+)\n");
		sql.append("   and a.group_id = e.group_id(+)\n");
		sql.append("and a.QUOTA_week = b.QUOTA_week(+)\n");
		sql.append("   and a.QUOTA_week = c.order_week(+)\n");
		sql.append("   and a.QUOTA_week = d.order_week(+)\n");
		sql.append("   and a.QUOTA_week = e.order_week(+)\n");
		sql.append("   and a.QUOTA_year = b.QUOTA_year(+)\n");
		sql.append("   and a.QUOTA_year = c.order_year(+)\n");
		sql.append("   and a.QUOTA_year = d.order_year(+)\n");
		sql.append("   and a.QUOTA_year = e.order_year(+)\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and A.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("order by A.QUOTA_YEAR,A.QUOTA_WEEK\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getOemQuotaTransOrderRateTotalQueryList", pageSize, curPage);
		return ps;
	}

	public PageResult<Map<String, Object>> getOemQuotaTransOrderRateDetailQueryListCVS(Map<String, Object> map, int curPage, int pageSize) {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String areaId = (String) map.get("areaId");
		String dealerCode = (String) map.get("dealerCode");
		String dealerSql = (String) map.get("dealerSql");
		String dealerId = (String) map.get("dealerId");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		String regionId = (String) map.get("regionId");
		String endYear = (String) map.get("endYear");
		String endWeek = (String) map.get("endWeek");
		String groupCode = (String) map.get("groupCode");

		OrderReportDao reportDao = new OrderReportDao();
		TmBusinessParaPO para = reportDao.getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer("\n");

		sql.append("  with a as (/*+ all_rows*/SELECT TQ.QUOTA_YEAR,\n");
		sql.append("               TQ.QUOTA_WEEK,\n");
		sql.append("               TD.DEALER_ID,\n");
		sql.append("               TD.DEALER_CODE,\n");
		sql.append("               TD.PROVINCE_ID,\n");
		sql.append("               TMO.ORG_NAME,\n");
		sql.append("               TD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("               SUM(TQD.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("          FROM TT_VS_QUOTA TQ,TT_VS_QUOTA_DETAIL TQD, TM_DEALER TD, vw_org_dealer TDOR, TM_ORG TMO, TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append("         WHERE TQ.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TQ.QUOTA_ID=TQD.QUOTA_ID");
		sql.append("	AND TQD.GROUP_ID = TVMG.GROUP_ID  /*新增*/\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("           AND TDOR.DEALER_ID = TD.DEALER_ID");
		sql.append("           AND TDOR.root_ORG_ID = TMO.ORG_ID");
		sql.append("           AND TQ.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("           AND TQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		sql.append("           AND TQD.QUOTA_AMT <> 0\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");
		}
		sql.append("           AND TQ.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TQ.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		sql.append("         GROUP BY TQ.QUOTA_YEAR,\n");
		sql.append("                  TQ.QUOTA_WEEK,\n");
		sql.append("                  TD.DEALER_ID,\n");
		sql.append("                  TD.DEALER_CODE,\n");
		sql.append("                  TD.PROVINCE_ID,\n");
		sql.append("                  TMO.ORG_NAME,\n");
		sql.append("                  TD.DEALER_SHORTNAME),\n");

		sql.append(" b as     (SELECT /*+ all_rows*/TD.DEALER_ID, tqm.quota_year, tqm.quota_week,SUM(TQM.MIN_AMOUNT) MIN_AMOUNT\n");
		sql.append("          FROM TT_VS_QUOTA_MIN TQM, TM_DEALER TD, tm_vhcl_material_group tvmg\n");
		sql.append("         WHERE TQM.DEALER_ID = TD.DEALER_ID\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		sql.append("and tqm.group_id = tvmg.group_id\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");
		}

		sql.append("           AND TQM.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TQM.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         GROUP BY tqm.quota_year,tqm.quota_week,TD.DEALER_ID),\n");

		sql.append(" c as      (SELECT /*+ all_rows*/TD.DEALER_ID, tso.order_year,tso.order_week,SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT,\n");
		sql.append("               SUM(TSOD.check_AMOUNT) check_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER TSO, TT_VS_ORDER_DETAIL TSOD, TM_DEALER TD, tm_vhcl_material_group tvmg, tm_vhcl_material_group_r tvmgr\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("	and tsod.material_id = tvmgr.material_id\n");
		sql.append("           and tvmgr.group_id = tvmg.group_id\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("           AND TSO.ORDER_ORG_ID = TD.DEALER_ID\n");
		sql.append("           AND TSO.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");
		}

		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         GROUP BY TSO.Order_Year,tso.order_week,TD.DEALER_ID),\n");

		sql.append("d as (select /*+ all_rows*/tvo.order_org_id billing_org_id, tvo.order_year,tvo.order_week,sum(tvdrd.req_amount) req_amount, sum(tvdrd.reserve_amount) reserve_amount\n");
		sql.append("          from tt_vs_order         tvo,\n");
		sql.append("               tt_vs_dlvry_req     tvdr,\n");
		sql.append("               tt_vs_dlvry_req_dtl tvdrd,\n");
		sql.append("			tm_vhcl_material_group tvmg,\n");
		sql.append("               tm_vhcl_material_group_r tvmgr\n");
		sql.append("         where tvo.order_id = tvdr.order_id\n");
		sql.append("           and tvdr.req_id = tvdrd.req_id\n");
		sql.append("	and tvdrd.material_id = tvmgr.material_id\n");
		sql.append("           and tvmgr.group_id = tvmg.group_id\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("           AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           and tvdr.req_status not in (").append(Constant.ORDER_REQ_STATUS_07).append(",").append(Constant.ORDER_REQ_STATUS_YSH).append(")\n");
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");
		}

		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvO.AREA_ID = " + areaId + "\n");
		}

		sql.append("         group by tvo.order_year,tvo.order_week,tvo.order_org_id),\n");

		sql.append(" e as (select /*+ all_rows*/ttt.billing_org_id,\n");
		sql.append("       ttt.order_year,\n");
		sql.append("       ttt.order_week,\n");
		sql.append("       ttt.dealer_code,\n");
		sql.append("       ttt.root_org_name,\n");
		sql.append("       ttt.dealer_name,\n");
		sql.append("       ttt.region_code province_id,\n");
		sql.append("       sum(ttt.c_delivery_amount) delivery_amount,\n");
		sql.append("       sum(ttt.b_delivery_amount) b_delivery_amount,\n");
		sql.append("       sum(ttt.d_delivery_amount) d_delivery_amount\n");
		sql.append("   from (select /*+ all_rows*/\n");
		sql.append("          tvo.order_org_id billing_org_id,\n");
		sql.append("          tvo.order_year,\n");
		sql.append("          tvo.order_week,\n");
		sql.append("          td.dealer_code,\n");
		sql.append("          vod.root_org_name,\n");
		sql.append("          td.dealer_shortname dealer_name,\n");
		sql.append("          tmr.region_code,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_01).append(", sum(tvdd.delivery_amount), 0) c_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_02).append(", sum(tvdd.delivery_amount), 0) b_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_03).append(", sum(tvdd.delivery_amount), 0) d_delivery_amount\n");
		sql.append("from             tt_vs_order              tvo,\n");
		sql.append("                 tt_vs_dlvry              tvd,\n");
		sql.append("                 tt_vs_dlvry_dtl          tvdd,\n");
		sql.append("                 tm_vhcl_material_group   tvmg,\n");
		sql.append("                 tm_vhcl_material_group_r tvmgr,\n");
		sql.append("                 vw_org_dealer            vod,\n");
		sql.append("                 tm_dealer                td,\n");
		sql.append("                 tm_region                tmr\n");
		sql.append("           where tvo.order_id = tvd.order_id\n");
		sql.append("             and tvmgr.material_id = tvdd.material_id\n");
		sql.append("             and tvmgr.group_id = tvmg.group_id\n");
		sql.append("             and tvd.delivery_id = tvdd.delivery_id\n");
		sql.append("             and tvo.order_org_id = td.dealer_id\n");
		sql.append("             and td.province_id = tmr.region_code\n");
		sql.append("             and vod.dealer_id = td.dealer_id\n");

		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}

		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}

		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}

		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}

		sql.append("           and tvd.delivery_id = tvdd.delivery_id\n");
		sql.append("           and tvd.delivery_status in\n");
		sql.append("               (").append(Constant.DELIVERY_STATUS_04).append(",").append(Constant.DELIVERY_STATUS_05).append(",").append(Constant.DELIVERY_STATUS_10).append(",").append(Constant.DELIVERY_STATUS_11).append(",").append(Constant.DELIVERY_STATUS_12).append(")\n");

		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");
		}

		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvO.AREA_ID = " + areaId + "\n");
		}

		sql.append("           group by tvo.ORDER_TYPE,\n");
		sql.append("                    tvo.order_year,\n");
		sql.append("                    tvo.order_week,\n");
		sql.append("                    tvo.order_org_id,\n");
		sql.append("                    td.dealer_code,\n");
		sql.append("                    vod.root_org_name,\n");
		sql.append("                    tmr.region_code,\n");
		sql.append("                    td.dealer_shortname) ttt\n");
		sql.append("   group by ttt.billing_org_id,\n");
		sql.append("            ttt.order_year,\n");
		sql.append("            ttt.order_week,\n");
		sql.append("            ttt.dealer_code,\n");
		sql.append("            ttt.root_org_name,\n");
		sql.append("            ttt.region_code,\n");
		sql.append("            ttt.dealer_name)\n");

		sql.append("select /*+ all_rows*/\n");
		sql.append(" nvl(A.QUOTA_YEAR, e.order_year) || '年' || nvl(A.QUOTA_WEEK, e.order_week) || '周' QUOTA_DATE,\n");
		sql.append(" nvl(A.ORG_NAME, e.root_org_name) org_name,\n");
		sql.append(" nvl(A.PROVINCE_ID, e.PROVINCE_ID) PROVINCE_ID,\n");
		sql.append(" nvl(A.DEALER_CODE, e.DEALER_CODE) DEALER_CODE,\n");
		sql.append(" nvl(A.DEALER_NAME, e.DEALER_NAME) DEALER_NAME,\n");
		sql.append(" nvl(A.QUOTA_AMT, 0) QUOTA_AMT,\n");
		sql.append(" nvl(ROUND(A.QUOTA_AMT * 80 / 100, 0), 0) MIN_AMOUNT,\n");
		sql.append(" NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		sql.append(" NVL(C.check_AMOUNT, 0) CHECK_AMOUNT,\n");
		sql.append(" nvl(d.req_amount, 0) REQ_AMOUNT,\n");
		sql.append(" nvl(d.reserve_amount, 0) RESERVE_AMOUNT,\n");
		sql.append(" nvl(e.delivery_amount, 0) DELIVERY_AMOUNT,\n");
		sql.append(" nvl(e.b_delivery_amount, 0) b_DELIVERY_AMOUNT,\n");
		sql.append(" nvl((A.QUOTA_AMT - NVL(C.ORDER_AMOUNT, 0)), 0) CY,\n");
		sql.append(" ROUND((NVL(e.delivery_amount, 0) * 100 /\n");
		sql.append("       NVL(decode(c.check_amount, 0, 1, c.check_amount), 1)),\n");
		sql.append("       2) || '%' ZXL,\n");
		sql.append(" ROUND((NVL(e.delivery_amount, 0) * 100 /\n");
		sql.append("       decode(nvl(e.delivery_amount, 0) + nvl(e.b_delivery_amount, 0),\n");
		sql.append("               0,\n");
		sql.append("               1,\n");
		sql.append("               nvl(e.delivery_amount, 0) + nvl(e.b_delivery_amount, 0))),\n");
		sql.append("       2) || '%' ZBL,\n");
		sql.append(" nvl(ROUND((NVL(C.ORDER_AMOUNT, 0) * 100 / A.QUOTA_AMT), 2), 0) || '%' WCL\n");
		sql.append("  from a\n");
		sql.append("  full outer join e\n");
		sql.append("    on A.DEALER_ID = e.billing_org_id\n");
		sql.append("   and a.QUOTA_week = e.order_week\n");
		sql.append("   and a.QUOTA_year = e.order_year\n");
		sql.append("  left outer join b\n");
		sql.append("    on A.DEALER_ID = B.DEALER_ID\n");
		sql.append("   and a.QUOTA_week = b.QUOTA_week\n");
		sql.append("   and a.QUOTA_year = b.QUOTA_year\n");
		sql.append("  left outer join c\n");
		sql.append("    on A.DEALER_ID = C.DEALER_ID\n");
		sql.append("   and a.QUOTA_week = c.order_week\n");
		sql.append("   and a.QUOTA_year = c.order_year\n");
		sql.append("  left outer join d\n");
		sql.append("    on A.DEALER_ID = D.billing_org_id\n");
		sql.append("   and a.QUOTA_week = d.order_week\n");
		sql.append("   and a.QUOTA_year = d.order_year\n");
		sql.append(" order by nvl(A.QUOTA_YEAR, e.order_year), nvl(A.QUOTA_WEEK, e.order_week)\n");

		// sql.append("SELECT /*+ all_rows*/A.QUOTA_YEAR || '年' || A.QUOTA_WEEK
		// || '周' QUOTA_DATE,\n");
		// sql.append(" A.ORG_NAME,\n");
		// sql.append(" A.PROVINCE_ID,\n");
		// sql.append(" A.DEALER_CODE,\n");
		// sql.append(" A.DEALER_NAME,\n");
		// sql.append(" A.QUOTA_AMT,\n");
		// //sql.append(" A.group_code,\n");
		// sql.append(" ROUND(A.QUOTA_AMT*" + para.getParaValue() + "/100, 0)
		// MIN_AMOUNT,\n");
		// //sql.append(" NVL(B.MIN_AMOUNT, 0) MIN_AMOUNT,\n");
		// sql.append(" NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		// sql.append(" NVL(C.check_AMOUNT, 0) CHECK_AMOUNT,\n");
		// sql.append(" nvl(d.req_amount, 0) REQ_AMOUNT,\n");
		// sql.append(" nvl(d.reserve_amount, 0) RESERVE_AMOUNT,\n");
		// sql.append(" nvl(e.delivery_amount, 0) DELIVERY_AMOUNT,\n");
		// sql.append(" nvl(e.b_delivery_amount, 0) b_DELIVERY_AMOUNT,\n");
		// sql.append(" (A.QUOTA_AMT - NVL(C.ORDER_AMOUNT, 0)) CY,\n");
		// sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 /
		// NVL(decode(c.check_amount,0,1,c.check_amount), 1)), 2) || '%'
		// ZXL,\n");
		// sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 /
		// decode(nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0), 0,
		// 1,nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0))), 2) || '%'
		// ZBL,\n");
		// sql.append(" ROUND((NVL(C.ORDER_AMOUNT, 0) * 100 / A.QUOTA_AMT), 2)
		// || '%' WCL\n");
		// sql.append(" FROM (/*+ all_rows*/SELECT TQ.QUOTA_YEAR,\n");
		// sql.append(" TQ.QUOTA_WEEK,\n");
		// sql.append(" TD.DEALER_ID,\n");
		// sql.append(" TD.DEALER_CODE,\n");
		// //sql.append(" TVMG.GROUP_CODE, /*新增*/\n");
		// sql.append(" TD.PROVINCE_ID,\n");
		// sql.append(" TMO.ORG_NAME,\n");
		// sql.append(" TD.DEALER_SHORTNAME DEALER_NAME,\n");
		// sql.append(" SUM(TQD.QUOTA_AMT) QUOTA_AMT\n");
		// sql.append(" FROM TT_VS_QUOTA TQ,TT_VS_QUOTA_DETAIL TQD, TM_DEALER
		// TD, vw_org_dealer TDOR, TM_ORG TMO, TM_VHCL_MATERIAL_GROUP TVMG\n");
		// sql.append(" WHERE TQ.DEALER_ID = TD.DEALER_ID\n");
		// sql.append(" AND TQ.QUOTA_ID=TQD.QUOTA_ID");
		// sql.append(" AND TQD.GROUP_ID = TVMG.GROUP_ID /*新增*/\n");
		// //TODO 添加物料组过滤条件 2012-05-22 HXY
		// if(groupCode != null && !"".equals(groupCode)) {
		// sql.append("and TVMG.GROUP_CODE in ('");
		// if(!groupCode.contains(",")) {
		// sql.append(groupCode);
		// } else {
		// String[] groupCodes = groupCode.split(",");
		// for(int i=0; i<groupCodes.length; i++) {
		// if(i != groupCodes.length-1) {
		// sql.append(groupCodes[i]);
		// sql.append("','");
		// } else {
		// sql.append(groupCodes[i]);
		// }
		// }
		// }
		// sql.append("')\n");
		// }
		// sql.append(" AND TDOR.DEALER_ID = TD.DEALER_ID");
		// sql.append(" AND TDOR.root_ORG_ID = TMO.ORG_ID");
		// sql.append(" AND TQ.COMPANY_ID = " + companyId + "\n");
		// sql.append(" AND TQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		// sql.append(" AND TQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		// sql.append(" AND TQD.QUOTA_AMT <> 0\n");
		// if (dealerSql != null && !dealerSql.equals("")) {
		// sql.append(" AND " + dealerSql);
		// }
		// if (orderYear != null && orderWeek != null && !orderYear.equals("")
		// && !orderWeek.equals("")) {
		// sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || TQ.QUOTA_WEEK,\n");
		// sql.append(" TQ.QUOTA_WEEK) >=
		// ").append(orderYear).append(orderWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		//		
		// if (endYear != null && endWeek != null && !endYear.equals("") &&
		// !endWeek.equals("")) {
		// sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || TQ.QUOTA_WEEK,\n");
		// sql.append(" TQ.QUOTA_WEEK) <=
		// ").append(endYear).append(endWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		// /*if (!orderYear.equals("")) {
		// sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");
		// }
		// if (!orderWeek.equals("")) {
		// sql.append(" AND TQ.QUOTA_WEEK = " + orderWeek + "\n");
		// }*/
		// sql.append(" AND TQ.AREA_ID IN (" + areaIds + ")\n");
		// if (areaId != null && !areaId.equals("")) {
		// sql.append(" AND TQ.AREA_ID = " + areaId + "\n");
		// }
		// if (dealerCode != null && !dealerCode.equals("")) {
		// String[] array = dealerCode.split(",");
		// sql.append(" AND TD.DEALER_CODE IN (");
		// for (int i = 0; i < array.length; i++) {
		// sql.append("'" + array[i] + "'");
		// if (i != array.length - 1) {
		// sql.append(",");
		// }
		// }
		// sql.append(")\n");
		// }
		// if(dealerId != null && !"".equals(dealerId)) {
		// sql.append(" AND td.dealer_id in (");
		// sql.append(dealerId);
		// sql.append(")\n");
		// }
		// if (regionId != null && !"".equals(regionId)) {
		// sql.append(" AND TD.PROVINCE_ID = " + regionId + "\n");
		// }
		// sql.append(" GROUP BY TQ.QUOTA_YEAR,\n");
		// sql.append(" TQ.QUOTA_WEEK,\n");
		// sql.append(" TD.DEALER_ID,\n");
		// sql.append(" TD.DEALER_CODE,\n");
		// sql.append(" TD.PROVINCE_ID,\n");
		// sql.append(" TMO.ORG_NAME,\n");
		// sql.append(" TD.DEALER_SHORTNAME) A,\n");
		// sql.append(" (SELECT /*+ all_rows*/TD.DEALER_ID, tqm.quota_year,
		// tqm.quota_week,SUM(TQM.MIN_AMOUNT) MIN_AMOUNT\n");
		// sql.append(" FROM TT_VS_QUOTA_MIN TQM, TM_DEALER TD,
		// tm_vhcl_material_group tvmg\n");
		// sql.append(" WHERE TQM.DEALER_ID = TD.DEALER_ID\n");
		// if (dealerSql != null && !dealerSql.equals("")) {
		// sql.append(" AND " + dealerSql);
		// }
		// sql.append("and tqm.group_id = tvmg.group_id\n");
		// //TODO 添加物料组过滤条件 2012-05-22 HXY
		// if(groupCode != null && !"".equals(groupCode)) {
		// sql.append("and TVMG.GROUP_CODE in ('");
		// if(!groupCode.contains(",")) {
		// sql.append(groupCode);
		// } else {
		// String[] groupCodes = groupCode.split(",");
		// for(int i=0; i<groupCodes.length; i++) {
		// if(i != groupCodes.length-1) {
		// sql.append(groupCodes[i]);
		// sql.append("','");
		// } else {
		// sql.append(groupCodes[i]);
		// }
		// }
		// }
		// sql.append("')\n");
		// }
		// if (orderYear != null && orderWeek != null && !orderYear.equals("")
		// && !orderWeek.equals("")) {
		// sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || TQm.QUOTA_WEEK,\n");
		// sql.append(" TQm.QUOTA_WEEK) >=
		// ").append(orderYear).append(orderWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		//		
		// if (endYear != null && endWeek != null && !endYear.equals("") &&
		// !endWeek.equals("")) {
		// sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || TQm.QUOTA_WEEK,\n");
		// sql.append(" TQm.QUOTA_WEEK) <=
		// ").append(endYear).append(endWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		// /*if (!orderYear.equals("")) {
		// sql.append(" AND TQM.QUOTA_YEAR = " + orderYear + "\n");
		// }
		// if (!orderWeek.equals("")) {
		// sql.append(" AND TQM.QUOTA_WEEK = " + orderWeek + "\n");
		// }*/
		// sql.append(" AND TQM.AREA_ID IN (" + areaIds + ")\n");
		// if (areaId != null && !areaId.equals("")) {
		// sql.append(" AND TQM.AREA_ID = " + areaId + "\n");
		// }
		// if (dealerCode != null && !dealerCode.equals("")) {
		// String[] array = dealerCode.split(",");
		// sql.append(" AND TD.DEALER_CODE IN (");
		// for (int i = 0; i < array.length; i++) {
		// sql.append("'" + array[i] + "'");
		// if (i != array.length - 1) {
		// sql.append(",");
		// }
		// }
		// sql.append(")\n");
		// }
		// sql.append(" GROUP BY tqm.quota_year,tqm.quota_week,TD.DEALER_ID)
		// B,\n");
		// sql.append(" (SELECT /*+ all_rows*/TD.DEALER_ID,
		// tso.order_year,tso.order_week,SUM(TSOD.ORDER_AMOUNT)
		// ORDER_AMOUNT,\n");
		// sql.append(" SUM(TSOD.check_AMOUNT) check_AMOUNT\n");
		// sql.append(" FROM TT_VS_ORDER TSO, TT_VS_ORDER_DETAIL TSOD, TM_DEALER
		// TD, tm_vhcl_material_group tvmg, tm_vhcl_material_group_r tvmgr\n");
		// sql.append(" WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		// sql.append(" and tsod.material_id = tvmgr.material_id\n");
		// sql.append(" and tvmgr.group_id = tvmg.group_id\n");
		// //TODO 添加物料组过滤条件 2012-05-22 HXY
		// if(groupCode != null && !"".equals(groupCode)) {
		// sql.append("and TVMG.GROUP_CODE in ('");
		// if(!groupCode.contains(",")) {
		// sql.append(groupCode);
		// } else {
		// String[] groupCodes = groupCode.split(",");
		// for(int i=0; i<groupCodes.length; i++) {
		// if(i != groupCodes.length-1) {
		// sql.append(groupCodes[i]);
		// sql.append("','");
		// } else {
		// sql.append(groupCodes[i]);
		// }
		// }
		// }
		// sql.append("')\n");
		// }
		// sql.append(" AND TSO.ORDER_ORG_ID = TD.DEALER_ID\n");
		// sql.append(" AND TSO.COMPANY_ID = " + companyId + "\n");
		// sql.append(" AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		// sql.append(" AND TSO.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 +
		// ", " + Constant.ORDER_STATUS_05 + ")\n");
		// if (dealerSql != null && !dealerSql.equals("")) {
		// sql.append(" AND " + dealerSql);
		// }
		// if (orderYear != null && orderWeek != null && !orderYear.equals("")
		// && !orderWeek.equals("")) {
		// sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tso.ORDER_WEEK,\n");
		// sql.append(" tso.ORDER_WEEK) >=
		// ").append(orderYear).append(orderWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		//		
		// if (endYear != null && endWeek != null && !endYear.equals("") &&
		// !endWeek.equals("")) {
		// sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tso.ORDER_WEEK,\n");
		// sql.append(" tso.ORDER_WEEK) <=
		// ").append(endYear).append(endWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		// /*if (!orderYear.equals("")) {
		// sql.append(" AND TSO.ORDER_YEAR = " + orderYear + "\n");
		// }
		// if (!orderWeek.equals("")) {
		// sql.append(" AND TSO.ORDER_WEEK = " + orderWeek + "\n");
		// }*/
		// sql.append(" AND TSO.AREA_ID IN (" + areaIds + ")\n");
		// if (areaId != null && !areaId.equals("")) {
		// sql.append(" AND TSO.AREA_ID = " + areaId + "\n");
		// }
		// if (dealerCode != null && !dealerCode.equals("")) {
		// String[] array = dealerCode.split(",");
		// sql.append(" AND TD.DEALER_CODE IN (");
		// for (int i = 0; i < array.length; i++) {
		// sql.append("'" + array[i] + "'");
		// if (i != array.length - 1) {
		// sql.append(",");
		// }
		// }
		// sql.append(")\n");
		// }
		// sql.append(" GROUP BY TSO.Order_Year,tso.order_week,TD.DEALER_ID)
		// C,\n");
		//		
		// sql.append("(select /*+ all_rows*/tvo.order_org_id billing_org_id,
		// tvo.order_year,tvo.order_week,sum(tvdrd.req_amount) req_amount,
		// sum(tvdrd.reserve_amount) reserve_amount\n");
		// sql.append(" from tt_vs_order tvo,\n");
		// sql.append(" tt_vs_dlvry_req tvdr,\n");
		// sql.append(" tt_vs_dlvry_req_dtl tvdrd,\n");
		// sql.append(" tm_vhcl_material_group tvmg,\n");
		// sql.append(" tm_vhcl_material_group_r tvmgr\n");
		// sql.append(" where tvo.order_id = tvdr.order_id\n");
		// sql.append(" and tvdr.req_id = tvdrd.req_id\n");
		// sql.append(" and tvdrd.material_id = tvmgr.material_id\n");
		// sql.append(" and tvmgr.group_id = tvmg.group_id\n");
		// //TODO 添加物料组过滤条件 2012-05-22 HXY
		// if(groupCode != null && !"".equals(groupCode)) {
		// sql.append("and TVMG.GROUP_CODE in ('");
		// if(!groupCode.contains(",")) {
		// sql.append(groupCode);
		// } else {
		// String[] groupCodes = groupCode.split(",");
		// for(int i=0; i<groupCodes.length; i++) {
		// if(i != groupCodes.length-1) {
		// sql.append(groupCodes[i]);
		// sql.append("','");
		// } else {
		// sql.append(groupCodes[i]);
		// }
		// }
		// }
		// sql.append("')\n");
		// }
		// sql.append(" AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		// sql.append(" and tvdr.req_status not in
		// (").append(Constant.ORDER_REQ_STATUS_07).append(",").append(Constant.ORDER_REQ_STATUS_YSH).append(")\n");
		// if (orderYear != null && orderWeek != null && !orderYear.equals("")
		// && !orderWeek.equals("")) {
		// sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// sql.append(" tvo.ORDER_WEEK) >=
		// ").append(orderYear).append(orderWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		//		
		// if (endYear != null && endWeek != null && !endYear.equals("") &&
		// !endWeek.equals("")) {
		// sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// sql.append(" tvo.ORDER_WEEK) <=
		// ").append(endYear).append(endWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		// /*if (!orderYear.equals("")) {
		// sql.append(" AND tvO.ORDER_YEAR = " + orderYear + "\n");
		// }
		// if (!orderWeek.equals("")) {
		// sql.append(" AND tvO.ORDER_WEEK = " + orderWeek + "\n");
		// }*/
		// sql.append(" AND tvo.AREA_ID IN (" + areaIds + ")\n");
		// if (areaId != null && !areaId.equals("")) {
		// sql.append(" AND tvO.AREA_ID = " + areaId + "\n");
		// }
		// /*if (!dealerCode.equals("")) {
		// String[] array = dealerCode.split(",");
		// sql.append(" AND tvo.billing_org_id IN (");
		// for (int i = 0; i < array.length; i++) {
		// sql.append("'" + array[i] + "'");
		// if (i != array.length - 1) {
		// sql.append(",");
		// }
		// }
		// sql.append(")\n");
		// }*/
		// sql.append(" group by tvo.order_year,tvo.order_week,tvo.order_org_id)
		// d,\n");
		//		
		// sql.append("(select /*+ all_rows*/ttt.billing_org_id,\n");
		// sql.append(" ttt.order_year,\n");
		// sql.append(" ttt.order_week,\n");
		// sql.append(" sum(ttt.c_delivery_amount) delivery_amount,\n");
		// sql.append(" sum(ttt.b_delivery_amount) b_delivery_amount,\n");
		// sql.append(" sum(ttt.d_delivery_amount) d_delivery_amount\n");
		// sql.append("from (\n");
		// // sql.append("(select /*+ all_rows*/tvo.billing_org_id,
		// tvo.order_year,tvo.order_week,sum(tvdd.delivery_amount)
		// delivery_amount\n");
		// sql.append("select /*+ all_rows*/\n");
		// sql.append(" tvo.order_org_id billing_org_id,\n");
		// sql.append(" tvo.order_year,\n");
		// sql.append(" tvo.order_week,\n");
		// sql.append(" decode(tvo.ORDER_TYPE,
		// ").append(Constant.ORDER_TYPE_01).append(",
		// sum(tvdd.delivery_amount), 0) c_delivery_amount,\n");
		// sql.append(" decode(tvo.ORDER_TYPE,
		// ").append(Constant.ORDER_TYPE_02).append(",
		// sum(tvdd.delivery_amount), 0) b_delivery_amount,\n");
		// sql.append(" decode(tvo.ORDER_TYPE,
		// ").append(Constant.ORDER_TYPE_03).append(",
		// sum(tvdd.delivery_amount), 0) d_delivery_amount\n");
		// sql.append(" from tt_vs_order tvo, tt_vs_dlvry tvd, tt_vs_dlvry_dtl
		// tvdd, tm_vhcl_material_group tvmg, tm_vhcl_material_group_r
		// tvmgr\n");
		// sql.append(" where tvo.order_id = tvd.order_id\n");
		// sql.append(" and tvmgr.material_id = tvdd.material_id\n");
		// sql.append(" and tvmgr.group_id = tvmg.group_id\n");
		// //TODO 添加物料组过滤条件 2012-05-22 HXY
		// if(groupCode != null && !"".equals(groupCode)) {
		// sql.append("and TVMG.GROUP_CODE in ('");
		// if(!groupCode.contains(",")) {
		// sql.append(groupCode);
		// } else {
		// String[] groupCodes = groupCode.split(",");
		// for(int i=0; i<groupCodes.length; i++) {
		// if(i != groupCodes.length-1) {
		// sql.append(groupCodes[i]);
		// sql.append("','");
		// } else {
		// sql.append(groupCodes[i]);
		// }
		// }
		// }
		// sql.append("')\n");
		// }
		// sql.append(" and tvd.delivery_id = tvdd.delivery_id\n");
		// //sql.append(" AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 +
		// "\n");
		// sql.append(" and tvd.delivery_status in\n");
		// sql.append("
		// (").append(Constant.DELIVERY_STATUS_04).append(",").append(Constant.DELIVERY_STATUS_05).append(",").append(Constant.DELIVERY_STATUS_10).append(",").append(Constant.DELIVERY_STATUS_11).append(",").append(Constant.DELIVERY_STATUS_12).append(")\n");
		//		
		// if (orderYear != null && orderWeek != null && !orderYear.equals("")
		// && !orderWeek.equals("")) {
		// sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// sql.append(" tvo.ORDER_WEEK) >=
		// ").append(orderYear).append(orderWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		//		
		// if (endYear != null && endWeek != null && !endYear.equals("") &&
		// !endWeek.equals("")) {
		// sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// sql.append(" tvo.ORDER_WEEK) <=
		// ").append(endYear).append(endWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		// /*if (!orderYear.equals("")) {
		// sql.append(" AND tvO.ORDER_YEAR = " + orderYear + "\n");
		// }
		// if (!orderWeek.equals("")) {
		// sql.append(" AND tvO.ORDER_WEEK = " + orderWeek + "\n");
		// }*/
		// sql.append(" AND tvo.AREA_ID IN (" + areaIds + ")\n");
		// if (areaId != null && !areaId.equals("")) {
		// sql.append(" AND tvO.AREA_ID = " + areaId + "\n");
		// }
		// /*if (!dealerCode.equals("")) {
		// String[] array = dealerCode.split(",");
		// sql.append(" AND tvo.billing_org_id IN (");
		// for (int i = 0; i < array.length; i++) {
		// sql.append("'" + array[i] + "'");
		// if (i != array.length - 1) {
		// sql.append(",");
		// }
		// }
		// sql.append(")\n");
		// }*/
		// sql.append(" group by
		// tvo.ORDER_TYPE,tvo.order_year,tvo.order_week,tvo.order_org_id\n");
		// sql.append(") ttt\n");
		// sql.append(" group by ttt.billing_org_id, ttt.order_year,
		// ttt.order_week) e\n");
		//
		// sql.append(" WHERE A.DEALER_ID = B.DEALER_ID(+)\n");
		// sql.append(" AND A.DEALER_ID = C.DEALER_ID(+)");
		// sql.append(" AND A.DEALER_ID = D.billing_org_id(+)");
		// sql.append(" AND A.DEALER_ID = e.billing_org_id(+)");
		// sql.append("and a.QUOTA_week = b.QUOTA_week(+)\n");
		// sql.append(" and a.QUOTA_week = c.order_week(+)\n");
		// sql.append(" and a.QUOTA_week = d.order_week(+)\n");
		// sql.append(" and a.QUOTA_week = e.order_week(+)\n");
		// sql.append(" and a.QUOTA_year = b.QUOTA_year(+)\n");
		// sql.append(" and a.QUOTA_year = c.order_year(+)\n");
		// sql.append(" and a.QUOTA_year = d.order_year(+)\n");
		// sql.append(" and a.QUOTA_year = e.order_year(+)\n");
		// //TODO 添加物料组过滤条件 2012-05-22 HXY
		// /*if(groupCode != null && !"".equals(groupCode)) {
		// sql.append("and A.GROUP_CODE in ('");
		// if(!groupCode.contains(",")) {
		// sql.append(groupCode);
		// } else {
		// String[] groupCodes = groupCode.split(",");
		// for(int i=0; i<groupCodes.length; i++) {
		// if(i != groupCodes.length-1) {
		// sql.append(groupCodes[i]);
		// sql.append("','");
		// } else {
		// sql.append(groupCodes[i]);
		// }
		// }
		// }
		// sql.append("')\n");
		// }*/
		// sql.append("order by A.QUOTA_YEAR,A.QUOTA_WEEK\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getOemQuotaTransOrderRatDetailQueryList", pageSize, curPage);
		return ps;
	}

	public List<Map<String, Object>> getOemQuotaTransOrderRateTotalExportListCVS(Map<String, Object> map) {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String areaId = (String) map.get("areaId");
		String dealerCode = (String) map.get("dealerCode");
		String dealerSql = (String) map.get("dealerSql");
		String dealerId = (String) map.get("dealerId");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		String regionId = (String) map.get("regionId");
		String endYear = (String) map.get("endYear");
		String endWeek = (String) map.get("endWeek");
		String groupCode = (String) map.get("groupCode");

		OrderReportDao reportDao = new OrderReportDao();
		TmBusinessParaPO para = reportDao.getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT /*+ all_rows*/A.QUOTA_YEAR || '年' || A.QUOTA_WEEK || '周' QUOTA_DATE,\n");
		sql.append("       A.SERIES_NAME,\n");
		sql.append("       A.MODEL_NAME,\n");
		sql.append("       A.GROUP_NAME,\n");
		sql.append("       A.GROUP_CODE,\n");
		sql.append("       A.QUOTA_AMT,\n");
		sql.append("       ROUND(A.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT,\n");
		// sql.append(" NVL(B.MIN_AMOUNT, 0) MIN_AMOUNT,\n");
		sql.append("       NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		sql.append("       NVL(C.check_AMOUNT, 0) CHECK_AMOUNT,\n");
		sql.append("       (A.QUOTA_AMT - NVL(C.ORDER_AMOUNT, 0)) CY,\n");
		sql.append("       nvl(d.req_amount, 0) req_amount,\n");
		sql.append("       nvl(d.reserve_amount, 0) reserve_amount,\n");
		sql.append("       nvl(e.delivery_amount, 0) delivery_amount,\n");
		sql.append("       nvl(e.d_delivery_amount, 0) d_delivery_amount,\n");
		sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 / NVL(c.check_amount, 1)), 2) || '%' ZXL,\n");
		sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 / decode(nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0), 0, 1,nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0))), 2) || '%' ZBL,\n");
		sql.append("       ROUND((NVL(C.ORDER_AMOUNT, 0) * 100 / A.QUOTA_AMT), 2) || '%' WCL\n");
		sql.append("  FROM (SELECT /*+ all_rows*/TQ.QUOTA_YEAR,\n");
		sql.append("               TQ.QUOTA_WEEK,\n");
		sql.append("               TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("               TVMG2.GROUP_NAME MODEL_NAME,\n");
		sql.append("               TVMG1.GROUP_NAME GROUP_NAME,\n");
		sql.append("               TVMG1.GROUP_CODE GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_ID GROUP_ID,\n");
		sql.append("               SUM(TQD.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("          FROM TT_VS_QUOTA            TQ,\n");
		sql.append("               TT_VS_QUOTA_DETAIL     TQD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("               TM_DEALER              TD\n");
		sql.append("         WHERE TQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TQ.QUOTA_ID=TQD.QUOTA_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("AND TQD.QUOTA_AMT <> 0\n");
		sql.append("           AND TQ.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TQ.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("           AND TQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}

		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TQ.QUOTA_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TQ.QUOTA_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TQ.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TQ.AREA_ID = " + areaId + "\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append("         GROUP BY TQ.QUOTA_YEAR,\n");
		sql.append("                  TQ.QUOTA_WEEK,\n");
		sql.append("                  TVMG3.GROUP_NAME,\n");
		sql.append("                  TVMG2.GROUP_NAME,\n");
		sql.append("                  TVMG1.GROUP_NAME,TVMG1.GROUP_CODE,\n");
		sql.append("                  TVMG1.GROUP_ID) A,\n");
		sql.append("       (SELECT /*+ all_rows*/TVMG1.GROUP_ID,\n");
		sql.append("               TVMG1.GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_NAME,\n");
		sql.append("tqm.quota_year,\n");
		sql.append("              tqm.quota_week,\n");
		sql.append("               SUM(TQM.MIN_AMOUNT) MIN_AMOUNT\n");
		sql.append("          FROM TT_VS_QUOTA_MIN        TQM,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("               TM_DEALER              TD\n");
		sql.append("         WHERE TQM.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TQM.DEALER_ID = TD.DEALER_ID\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TQM.QUOTA_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TQM.QUOTA_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TQM.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TQM.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         GROUP BY tqm.quota_year, tqm.quota_week,TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME) B,\n");
		sql.append("       (SELECT /*+ all_rows*/TVMG1.GROUP_ID,\n");
		sql.append("               TVMG1.GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_NAME,\n");
		sql.append("TSO.Order_Year,\n");
		sql.append("               tso.order_week,\n");
		sql.append("               SUM(TSOD.check_AMOUNT) check_AMOUNT,\n");
		sql.append("               SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER              TSO,\n");
		sql.append("               TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG3,\n");
		sql.append("               TM_DEALER                TD\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("           AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TSO.ORDER_ORG_ID = TD.DEALER_ID\n");
		sql.append("           AND TSO.COMPANY_ID = " + companyId + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND TSO.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * TSO.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         GROUP BY TSO.Order_Year,tso.order_week,TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME) C,\n");
		sql.append("(select /*+ all_rows*/tvmg1.group_id,\n");
		sql.append("               tvmg1.group_code,\n");
		sql.append("               tvmg1.group_name,\n");
		sql.append("tvo.order_year,\n");
		sql.append("               tvo.order_week,\n");
		sql.append("               sum(tvdrd.req_amount) req_amount,\n");
		sql.append("               sum(tvdrd.reserve_amount) reserve_amount\n");
		sql.append("          from tt_vs_order              tvo,\n");
		sql.append("               tt_vs_dlvry_req          tvdr,\n");
		sql.append("               tt_vs_dlvry_req_dtl      tvdrd,\n");
		sql.append("               tm_vhcl_material_group_r tvmgr,\n");
		sql.append("               tm_vhcl_material_group   tvmg1,\n");
		sql.append("               tm_vhcl_material_group   tvmg2,\n");
		sql.append("               tm_vhcl_material_group   tvmg3,\n");
		sql.append("               tm_dealer                td\n");
		sql.append("         where tvo.order_id = tvdr.order_id\n");
		sql.append("           and tvdr.req_id = tvdrd.req_id\n");
		sql.append("           and tvdrd.material_id = tvmgr.material_id\n");
		sql.append("           and tvmgr.group_id = tvmg1.group_id\n");
		sql.append("           and tvmg1.parent_group_id = tvmg2.group_id\n");
		sql.append("           and tvmg2.parent_group_id = tvmg3.group_id\n");
		sql.append("           and tvo.order_org_id = td.dealer_id\n");
		sql.append("           AND tvo.COMPANY_ID = " + companyId + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		sql.append("           AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND tvo.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");

		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND tvo.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * tvo.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvo.AREA_ID = " + areaId + "\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		sql.append("           and tvdr.req_status not in (").append(Constant.ORDER_REQ_STATUS_07).append(",").append(Constant.ORDER_REQ_STATUS_YSH).append(")\n");
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND td.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         group by tvo.order_year,tvo.order_week,tvmg1.group_id, tvmg1.group_code, tvmg1.group_name) d,\n");
		// sql.append(" (select /*+ all_rows*/tvmg1.group_id,\n");
		// sql.append(" tvmg1.group_code,\n");
		// sql.append(" tvmg1.group_name,\n");
		// sql.append("tvo.order_year,\n");
		// sql.append(" tvo.order_week,\n");
		// sql.append(" sum(tvdd.delivery_amount) delivery_amount\n");
		// sql.append(" from tt_vs_order tvo,\n");
		// sql.append(" tt_vs_dlvry tvd,\n");
		// sql.append(" tt_vs_dlvry_dtl tvdd,\n");
		// sql.append(" tm_vhcl_material_group_r tvmgr,\n");
		// sql.append(" tm_vhcl_material_group tvmg1,\n");
		// sql.append(" tm_vhcl_material_group tvmg2,\n");
		// sql.append(" tm_vhcl_material_group tvmg3,\n");
		// sql.append(" tm_dealer td\n");
		// sql.append(" where tvo.order_id = tvd.order_id\n");
		// sql.append(" and tvd.delivery_id = tvdd.delivery_id\n");
		// sql.append(" and tvdd.material_id = tvmgr.material_id\n");
		// sql.append(" and tvmgr.group_id = tvmg1.group_id\n");
		// sql.append(" and tvmg1.parent_group_id = tvmg2.group_id\n");
		// sql.append(" and tvmg2.parent_group_id = tvmg3.group_id\n");
		// sql.append(" and tvo.order_org_id = td.dealer_id\n");
		// sql.append(" AND tvo.COMPANY_ID = " + companyId + "\n");
		// if (!dealerSql.equals("")) {
		// sql.append(" AND " + dealerSql);
		// }
		// sql.append(" AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01
		// + "\n");
		// sql.append(" AND tvo.ORDER_STATUS IN ("
		// + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05
		// + ")\n");
		// if (!orderYear.equals("") && !orderWeek.equals("")) {
		// sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// sql.append(" tvo.ORDER_WEEK) >=
		// ").append(orderYear).append(orderWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		//		
		// if (!endYear.equals("") && !endWeek.equals("")) {
		// sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// sql.append(" tvo.ORDER_WEEK) <=
		// ").append(endYear).append(endWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		// /*if (!orderYear.equals("")) {
		// sql.append(" AND tvo.ORDER_YEAR = " + orderYear + "\n");
		// }
		// if (!orderWeek.equals("")) {
		// sql.append(" AND tvo.ORDER_WEEK = " + orderWeek + "\n");
		// }*/
		// sql.append(" AND tvo.AREA_ID IN (" + areaIds + ")\n");
		// if (!areaId.equals("")) {
		// sql.append(" AND tvo.AREA_ID = " + areaId + "\n");
		// }
		// if (!"".equals(regionId)) {
		// sql.append(" AND TD.PROVINCE_ID = " + regionId + "\n");
		// }
		// sql.append(" and tvd.delivery_status in\n");
		// sql.append("
		// (").append(Constant.DELIVERY_STATUS_04).append(",").append(Constant.DELIVERY_STATUS_05).append(",").append(Constant.DELIVERY_STATUS_10).append(",").append(Constant.DELIVERY_STATUS_11).append(",").append(Constant.DELIVERY_STATUS_12).append(")\n");
		// if (!dealerCode.equals("")) {
		// String[] array = dealerCode.split(",");
		// sql.append(" AND td.DEALER_CODE IN (");
		// for (int i = 0; i < array.length; i++) {
		// sql.append("'" + array[i] + "'");
		// if (i != array.length - 1) {
		// sql.append(",");
		// }
		// }
		// sql.append(")\n");
		// }
		// sql.append(" group by tvo.order_year,tvo.order_week,tvmg1.group_id,
		// tvmg1.group_code, tvmg1.group_name) e\n");
		sql.append("(select /*+ all_rows*/\n");
		sql.append("         ttt.group_id,\n");
		sql.append("         ttt.group_code,\n");
		sql.append("         ttt.group_name,\n");
		sql.append("         ttt.order_year,\n");
		sql.append("         ttt.order_week,\n");
		sql.append("         sum(ttt.c_delivery_amount) delivery_amount,\n");
		sql.append("         sum(ttt.b_delivery_amount) b_delivery_amount,\n");
		sql.append("         sum(ttt.d_delivery_amount) d_delivery_amount\n");
		sql.append("from \n");
		sql.append("       (select /*+ all_rows*/tvmg1.group_id,\n");
		sql.append("               tvmg1.group_code,\n");
		sql.append("               tvmg1.group_name,\n");
		sql.append("tvo.order_year,\n");
		sql.append("               tvo.order_week,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_01).append(", sum(tvdd.delivery_amount), 0) c_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_02).append(", sum(tvdd.delivery_amount), 0) b_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_03).append(", sum(tvdd.delivery_amount), 0) d_delivery_amount\n");
		// sql.append(" sum(tvdd.delivery_amount) delivery_amount\n");
		sql.append("          from tt_vs_order              tvo,\n");
		sql.append("               tt_vs_dlvry              tvd,\n");
		sql.append("               tt_vs_dlvry_dtl          tvdd,\n");
		sql.append("               tm_vhcl_material_group_r tvmgr,\n");
		sql.append("               tm_vhcl_material_group   tvmg1,\n");
		sql.append("               tm_vhcl_material_group   tvmg2,\n");
		sql.append("               tm_vhcl_material_group   tvmg3,\n");
		sql.append("               tm_dealer                td\n");
		sql.append("         where tvo.order_id = tvd.order_id\n");
		sql.append("           and tvd.delivery_id = tvdd.delivery_id\n");
		sql.append("           and tvdd.material_id = tvmgr.material_id\n");
		sql.append("           and tvmgr.group_id = tvmg1.group_id\n");
		sql.append("           and tvmg1.parent_group_id = tvmg2.group_id\n");
		sql.append("           and tvmg2.parent_group_id = tvmg3.group_id\n");
		sql.append("           and tvo.order_org_id = td.dealer_id\n");
		sql.append("           AND tvo.COMPANY_ID = " + companyId + "\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		/*
		 * sql.append(" AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		 */
		sql.append("           AND tvo.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");

			/* sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n"); */
		}
		/*
		 * if (!orderYear.equals("")) { sql.append(" AND tvo.ORDER_YEAR = " +
		 * orderYear + "\n"); } if (!orderWeek.equals("")) { sql.append(" AND
		 * tvo.ORDER_WEEK = " + orderWeek + "\n"); }
		 */
		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvo.AREA_ID = " + areaId + "\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		sql.append("           and tvd.delivery_status in\n");
		sql.append("               (").append(Constant.DELIVERY_STATUS_04).append(",").append(Constant.DELIVERY_STATUS_05).append(",").append(Constant.DELIVERY_STATUS_10).append(",").append(Constant.DELIVERY_STATUS_11).append(",").append(Constant.DELIVERY_STATUS_12).append(")\n");
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND td.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         group by tvo.ORDER_TYPE, tvo.order_year,tvo.order_week,tvmg1.group_id, tvmg1.group_code, tvmg1.group_name) ttt\n");
		sql.append("group by ttt.group_id,\n");
		sql.append("                  ttt.group_code,\n");
		sql.append("                  ttt.group_name,\n");
		sql.append("                  ttt.order_year,\n");
		sql.append("                  ttt.order_week) e\n");
		sql.append(" WHERE A.GROUP_ID = B.GROUP_ID(+)\n");
		sql.append("   AND A.GROUP_ID = C.GROUP_ID(+)");
		sql.append("   and a.group_id = d.group_id(+)\n");
		sql.append("   and a.group_id = e.group_id(+)\n");
		sql.append("and a.QUOTA_week = b.QUOTA_week(+)\n");
		sql.append("and a.QUOTA_week = c.order_week(+)\n");
		sql.append("and a.QUOTA_week = d.order_week(+)\n");
		sql.append("and a.QUOTA_week = e.order_week(+)\n");
		sql.append("and a.QUOTA_year = b.QUOTA_year(+)\n");
		sql.append("and a.QUOTA_year = c.order_year(+)\n");
		sql.append("and a.QUOTA_year = d.order_year(+)\n");
		sql.append("and a.QUOTA_year = e.order_year(+)\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and A.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("order by A.QUOTA_YEAR,A.QUOTA_WEEK\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}

	public List<Map<String, Object>> getOemQuotaTransOrderRateDetailExportListCVS(Map<String, Object> map) {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String areaId = (String) map.get("areaId");
		String dealerCode = (String) map.get("dealerCode");
		String dealerSql = (String) map.get("dealerSql");
		String dealerId = (String) map.get("dealerId");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		String regionId = (String) map.get("regionId");
		String endYear = (String) map.get("endYear");
		String endWeek = (String) map.get("endWeek");
		String groupCode = (String) map.get("groupCode");

		OrderReportDao reportDao = new OrderReportDao();
		TmBusinessParaPO para = reportDao.getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer();

		// sql.append("SELECT /*+ all_rows*/A.QUOTA_YEAR || '年' || A.QUOTA_WEEK
		// || '周' QUOTA_DATE,\n");
		// sql.append(" A.ORG_NAME,\n");
		// sql.append(" A.REGION_NAME,\n");
		// sql.append(" A.DEALER_CODE,\n");
		// sql.append(" A.DEALER_NAME,\n");
		// sql.append(" A.QUOTA_AMT,\n");
		// //sql.append(" A.group_code,\n");
		// sql.append(" ROUND(A.QUOTA_AMT*" + para.getParaValue()
		// + "/100, 0) MIN_AMOUNT,\n");
		// //sql.append(" NVL(B.MIN_AMOUNT, 0) MIN_AMOUNT,\n");
		// sql.append(" NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		// sql.append(" NVL(C.check_AMOUNT, 0) CHECK_AMOUNT,\n");
		// sql.append(" nvl(d.req_amount, 0) REQ_AMOUNT,\n");
		// sql.append(" nvl(d.reserve_amount, 0) RESERVE_AMOUNT,\n");
		// sql.append(" nvl(e.delivery_amount, 0) DELIVERY_AMOUNT,\n");
		// sql.append(" nvl(e.B_delivery_amount, 0) B_DELIVERY_AMOUNT,\n");
		// sql.append(" (A.QUOTA_AMT - NVL(C.ORDER_AMOUNT, 0)) CY,\n");
		// sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 /
		// NVL(c.check_amount, 1)), 2) || '%' ZXL,\n");
		// sql.append("ROUND((NVL(e.delivery_amount, 0) * 100 /
		// decode(nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0), 0,
		// 1,nvl(e.delivery_amount,0)+nvl(e.b_delivery_amount,0))), 2) || '%'
		// ZBL,\n");
		// sql.append(" ROUND((NVL(C.ORDER_AMOUNT, 0) * 100 / A.QUOTA_AMT), 2)
		// || '%' WCL\n");
		// sql.append(" FROM (SELECT /*+ all_rows*/TQ.QUOTA_YEAR,\n");
		// sql.append(" TQ.QUOTA_WEEK,\n");
		// sql.append(" TMO.ORG_NAME,\n");
		// sql.append(" TR.REGION_NAME,\n");
		// //sql.append(" TVMG.GROUP_CODE, /*新增*/\n");
		// sql.append(" TD.DEALER_ID,\n");
		// sql.append(" TD.DEALER_CODE,\n");
		// sql.append(" TD.DEALER_SHORTNAME DEALER_NAME,\n");
		// sql.append(" SUM(TQD.QUOTA_AMT) QUOTA_AMT\n");
		// sql.append(" FROM TT_VS_QUOTA TQ,TT_VS_QUOTA_DETAIL TQD, TM_DEALER
		// TD, vw_org_dealer TDOR, TM_ORG TMO, TM_REGION TR,
		// TM_VHCL_MATERIAL_GROUP TVMG \n");
		// sql.append(" WHERE TQ.DEALER_ID = TD.DEALER_ID\n");
		// sql.append(" AND TQD.GROUP_ID = TVMG.GROUP_ID\n");
		// //TODO 添加物料组过滤条件 2012-05-22 HXY
		// if(groupCode != null && !"".equals(groupCode)) {
		// sql.append("and TVMG.GROUP_CODE in ('");
		// if(!groupCode.contains(",")) {
		// sql.append(groupCode);
		// } else {
		// String[] groupCodes = groupCode.split(",");
		// for(int i=0; i<groupCodes.length; i++) {
		// if(i != groupCodes.length-1) {
		// sql.append(groupCodes[i]);
		// sql.append("','");
		// } else {
		// sql.append(groupCodes[i]);
		// }
		// }
		// }
		// sql.append("')\n");
		// }
		// sql.append(" AND TQ.QUOTA_ID=TQD.QUOTA_ID");
		// sql.append(" AND TDOR.DEALER_ID = TD.DEALER_ID");
		// sql.append(" AND TDOR.root_ORG_ID = TMO.ORG_ID");
		// sql.append(" AND TD.PROVINCE_ID = TR.REGION_CODE(+)");
		// sql.append(" AND TQ.COMPANY_ID = " + companyId + "\n");
		// sql.append(" AND TQ.STATUS = " + Constant.QUOTA_STATUS_02
		// + "\n");
		// sql.append(" AND TQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02
		// + "\n");
		// sql.append("AND TQD.QUOTA_AMT <> 0\n");
		// if (dealerSql != null && !dealerSql.equals("")) {
		// sql.append(" AND " + dealerSql);
		// }
		// if (orderYear != null && orderWeek != null && !orderYear.equals("")
		// && !orderWeek.equals("")) {
		// sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || TQ.QUOTA_WEEK,\n");
		// sql.append(" TQ.QUOTA_WEEK) >=
		// ").append(orderYear).append(orderWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		//		
		// if (endYear != null && endWeek != null && !endYear.equals("") &&
		// !endWeek.equals("")) {
		// sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || TQ.QUOTA_WEEK,\n");
		// sql.append(" TQ.QUOTA_WEEK) <=
		// ").append(endYear).append(endWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		// /*if (!orderYear.equals("")) {
		// sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");
		// }
		// if (!orderWeek.equals("")) {
		// sql.append(" AND TQ.QUOTA_WEEK = " + orderWeek + "\n");
		// }*/
		// sql.append(" AND TQ.AREA_ID IN (" + areaIds + ")\n");
		// if (areaId != null && !areaId.equals("")) {
		// sql.append(" AND TQ.AREA_ID = " + areaId + "\n");
		// }
		// if (regionId != null && !"".equals(regionId)) {
		// sql.append(" AND TD.PROVINCE_ID = " + regionId + "\n");
		// }
		// if (dealerCode != null && !dealerCode.equals("")) {
		// String[] array = dealerCode.split(",");
		// sql.append(" AND TD.DEALER_CODE IN (");
		// for (int i = 0; i < array.length; i++) {
		// sql.append("'" + array[i] + "'");
		// if (i != array.length - 1) {
		// sql.append(",");
		// }
		// }
		// sql.append(")\n");
		// }
		// if(dealerId != null && !"".equals(dealerId)) {
		// sql.append(" AND td.dealer_id in (");
		// sql.append(dealerId);
		// sql.append(")\n");
		// }
		// sql.append(" GROUP BY TQ.QUOTA_YEAR,\n");
		// sql.append(" TQ.QUOTA_WEEK,\n");
		// sql.append(" TR.REGION_NAME,\n");
		// sql.append(" TMO.ORG_NAME,\n");
		// sql.append(" TD.DEALER_ID,\n");
		// sql.append(" TD.DEALER_CODE,\n");
		// sql.append(" TD.DEALER_SHORTNAME) A,\n");
		// sql.append(" (SELECT /*+ all_rows*/TD.DEALER_ID, tqm.quota_year,
		// tqm.quota_week,SUM(TQM.MIN_AMOUNT) MIN_AMOUNT\n");
		// sql.append(" FROM TT_VS_QUOTA_MIN TQM, TM_DEALER TD,
		// tm_vhcl_material_group tvmg\n");
		// sql.append(" WHERE TQM.DEALER_ID = TD.DEALER_ID\n");
		// sql.append("and tqm.group_id = tvmg.group_id\n");
		// //TODO 添加物料组过滤条件 2012-05-22 HXY
		// if(groupCode != null && !"".equals(groupCode)) {
		// sql.append("and TVMG.GROUP_CODE in ('");
		// if(!groupCode.contains(",")) {
		// sql.append(groupCode);
		// } else {
		// String[] groupCodes = groupCode.split(",");
		// for(int i=0; i<groupCodes.length; i++) {
		// if(i != groupCodes.length-1) {
		// sql.append(groupCodes[i]);
		// sql.append("','");
		// } else {
		// sql.append(groupCodes[i]);
		// }
		// }
		// }
		// sql.append("')\n");
		// }
		// if (dealerSql != null && !dealerSql.equals("")) {
		// sql.append(" AND " + dealerSql);
		// }
		// if (orderYear != null && orderWeek != null && !orderYear.equals("")
		// && !orderWeek.equals("")) {
		// sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || TQm.QUOTA_WEEK,\n");
		// sql.append(" TQm.QUOTA_WEEK) >=
		// ").append(orderYear).append(orderWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		//		
		// if (endYear != null && endWeek != null && !endYear.equals("") &&
		// !endWeek.equals("")) {
		// sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || TQm.QUOTA_WEEK,\n");
		// sql.append(" TQm.QUOTA_WEEK) <=
		// ").append(endYear).append(endWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		// /*if (!orderYear.equals("")) {
		// sql.append(" AND TQM.QUOTA_YEAR = " + orderYear + "\n");
		// }
		// if (!orderWeek.equals("")) {
		// sql.append(" AND TQM.QUOTA_WEEK = " + orderWeek + "\n");
		// }*/
		// sql.append(" AND TQM.AREA_ID IN (" + areaIds + ")\n");
		// if (areaId != null && !areaId.equals("")) {
		// sql.append(" AND TQM.AREA_ID = " + areaId + "\n");
		// }
		// if (dealerCode != null && !dealerCode.equals("")) {
		// String[] array = dealerCode.split(",");
		// sql.append(" AND TD.DEALER_CODE IN (");
		// for (int i = 0; i < array.length; i++) {
		// sql.append("'" + array[i] + "'");
		// if (i != array.length - 1) {
		// sql.append(",");
		// }
		// }
		// sql.append(")\n");
		// }
		// sql.append(" GROUP BY tqm.quota_year, tqm.quota_week,TD.DEALER_ID)
		// B,\n");
		// sql.append(" (SELECT /*+ all_rows*/TD.DEALER_ID,
		// TSO.Order_Year,tso.order_week,SUM(TSOD.ORDER_AMOUNT)
		// ORDER_AMOUNT,\n");
		// sql.append(" SUM(TSOD.check_AMOUNT) check_AMOUNT\n");
		// sql.append(" FROM TT_VS_ORDER TSO, TT_VS_ORDER_DETAIL TSOD, TM_DEALER
		// TD, tm_vhcl_material_group tvmg, tm_vhcl_material_group_r tvmgr\n");
		// sql.append(" WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		// sql.append(" and tsod.material_id = tvmgr.material_id\n");
		// sql.append(" and tvmgr.group_id = tvmg.group_id\n");
		// //TODO 添加物料组过滤条件 2012-05-22 HXY
		// if(groupCode != null && !"".equals(groupCode)) {
		// sql.append("and TVMG.GROUP_CODE in ('");
		// if(!groupCode.contains(",")) {
		// sql.append(groupCode);
		// } else {
		// String[] groupCodes = groupCode.split(",");
		// for(int i=0; i<groupCodes.length; i++) {
		// if(i != groupCodes.length-1) {
		// sql.append(groupCodes[i]);
		// sql.append("','");
		// } else {
		// sql.append(groupCodes[i]);
		// }
		// }
		// }
		// sql.append("')\n");
		// }
		// sql.append(" AND TSO.COMPANY_ID = " + companyId + "\n");
		// sql.append(" AND TSO.ORDER_ORG_ID = TD.DEALER_ID\n");
		// sql.append(" AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01
		// + "\n");
		// sql.append(" AND TSO.ORDER_STATUS IN ("
		// + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05
		// + ")\n");
		// if (dealerSql != null && !dealerSql.equals("")) {
		// sql.append(" AND " + dealerSql);
		// }
		// if (orderYear != null && orderWeek != null && !orderYear.equals("")
		// && !orderWeek.equals("")) {
		// sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tso.ORDER_WEEK,\n");
		// sql.append(" tso.ORDER_WEEK) >=
		// ").append(orderYear).append(orderWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		//		
		// if (endYear != null && endWeek != null && !endYear.equals("") &&
		// !endWeek.equals("")) {
		// sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tso.ORDER_WEEK,\n");
		// sql.append(" tso.ORDER_WEEK) <=
		// ").append(endYear).append(endWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		// /*if (!orderYear.equals("")) {
		// sql.append(" AND TSO.ORDER_YEAR = " + orderYear + "\n");
		// }
		// if (!orderWeek.equals("")) {
		// sql.append(" AND TSO.ORDER_WEEK = " + orderWeek + "\n");
		// }*/
		// sql.append(" AND TSO.AREA_ID IN (" + areaIds + ")\n");
		// if (areaId != null && !areaId.equals("")) {
		// sql.append(" AND TSO.AREA_ID = " + areaId + "\n");
		// }
		// if (dealerCode != null && !dealerCode.equals("")) {
		// String[] array = dealerCode.split(",");
		// sql.append(" AND TD.DEALER_CODE IN (");
		// for (int i = 0; i < array.length; i++) {
		// sql.append("'" + array[i] + "'");
		// if (i != array.length - 1) {
		// sql.append(",");
		// }
		// }
		// sql.append(")\n");
		// }
		// sql.append(" GROUP BY TSO.Order_Year,tso.order_week,TD.DEALER_ID)
		// C,\n");
		// sql.append("(select /*+ all_rows*/tvo.order_org_id billing_org_id,
		// tvo.order_year,tvo.order_week,sum(tvdrd.req_amount) req_amount,
		// sum(tvdrd.reserve_amount) reserve_amount\n");
		// sql.append(" from tt_vs_order tvo,\n");
		// sql.append(" tt_vs_dlvry_req tvdr,\n");
		// sql.append(" tt_vs_dlvry_req_dtl tvdrd,\n");
		// sql.append(" tm_vhcl_material_group tvmg,\n");
		// sql.append(" tm_vhcl_material_group_r tvmgr\n");
		//
		// sql.append(" where tvo.order_id = tvdr.order_id\n");
		// sql.append(" and tvdrd.material_id = tvmgr.material_id\n");
		// sql.append(" and tvmgr.group_id = tvmg.group_id\n");
		// //TODO 添加物料组过滤条件 2012-05-22 HXY
		// if(groupCode != null && !"".equals(groupCode)) {
		// sql.append("and TVMG.GROUP_CODE in ('");
		// if(!groupCode.contains(",")) {
		// sql.append(groupCode);
		// } else {
		// String[] groupCodes = groupCode.split(",");
		// for(int i=0; i<groupCodes.length; i++) {
		// if(i != groupCodes.length-1) {
		// sql.append(groupCodes[i]);
		// sql.append("','");
		// } else {
		// sql.append(groupCodes[i]);
		// }
		// }
		// }
		// sql.append("')\n");
		// }
		// sql.append(" and tvdr.req_id = tvdrd.req_id\n");
		// sql.append(" AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		// sql.append(" and tvdr.req_status not in
		// (").append(Constant.ORDER_REQ_STATUS_07).append(",").append(Constant.ORDER_REQ_STATUS_YSH).append(")\n");
		// if (orderYear != null && orderWeek != null && !orderYear.equals("")
		// && !orderWeek.equals("")) {
		// sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// sql.append(" tvo.ORDER_WEEK) >=
		// ").append(orderYear).append(orderWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		//		
		// if (endYear != null && endWeek != null && !endYear.equals("") &&
		// !endWeek.equals("")) {
		// sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// sql.append(" tvo.ORDER_WEEK) <=
		// ").append(endYear).append(endWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		// /*if (!orderYear.equals("")) {
		// sql.append(" AND tvO.ORDER_YEAR = " + orderYear + "\n");
		// }
		// if (!orderWeek.equals("")) {
		// sql.append(" AND tvO.ORDER_WEEK = " + orderWeek + "\n");
		// }*/
		// sql.append(" AND tvo.AREA_ID IN (" + areaIds + ")\n");
		// if (areaId != null && !areaId.equals("")) {
		// sql.append(" AND tvO.AREA_ID = " + areaId + "\n");
		// }
		// /*if (!dealerCode.equals("")) {
		// String[] array = dealerCode.split(",");
		// sql.append(" AND tvo.billing_org_id IN (");
		// for (int i = 0; i < array.length; i++) {
		// sql.append("'" + array[i] + "'");
		// if (i != array.length - 1) {
		// sql.append(",");
		// }
		// }
		// sql.append(")\n");
		// }*/
		// sql.append(" group by tvo.order_year,tvo.order_week,tvo.order_org_id)
		// d,\n");
		//		
		// // sql.append("(select /*+ all_rows*/tvo.billing_org_id,
		// tvo.order_year,tvo.order_week,sum(tvdd.delivery_amount)
		// delivery_amount\n");
		// // sql.append(" from tt_vs_order tvo, tt_vs_dlvry tvd,
		// tt_vs_dlvry_dtl tvdd\n");
		// // sql.append(" where tvo.order_id = tvd.order_id\n");
		// // sql.append(" and tvd.delivery_id = tvdd.delivery_id\n");
		// // sql.append(" AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 +
		// "\n");
		// // sql.append(" and tvd.delivery_status in\n");
		// // sql.append("
		// (").append(Constant.DELIVERY_STATUS_04).append(",").append(Constant.DELIVERY_STATUS_05).append(",").append(Constant.DELIVERY_STATUS_10).append(",").append(Constant.DELIVERY_STATUS_11).append(",").append(Constant.DELIVERY_STATUS_12).append(")\n");
		// //
		// // if (!orderYear.equals("") && !orderWeek.equals("")) {
		// // sql.append("AND tvo.ORDER_YEAR ||
		// decode(length(tvo.ORDER_WEEK),\n");
		// // sql.append(" 1,\n");
		// // sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// // sql.append(" tvo.ORDER_WEEK) >=
		// ").append(orderYear).append(orderWeek).append("\n");
		// //
		// // sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");
		// // }
		// //
		// // if (!endYear.equals("") && !endWeek.equals("")) {
		// // sql.append("AND tvo.ORDER_YEAR ||
		// decode(length(tvo.ORDER_WEEK),\n");
		// // sql.append(" 1,\n");
		// // sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// // sql.append(" tvo.ORDER_WEEK) <=
		// ").append(endYear).append(endWeek).append("\n");
		// //
		// // sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");
		// // }
		// // if (!orderYear.equals("")) {
		// // sql.append(" AND tvO.ORDER_YEAR = " + orderYear + "\n");
		// // }
		// // if (!orderWeek.equals("")) {
		// // sql.append(" AND tvO.ORDER_WEEK = " + orderWeek + "\n");
		// // }
		// // sql.append(" AND tvo.AREA_ID IN (" + areaIds + ")\n");
		// // if (!areaId.equals("")) {
		// // sql.append(" AND tvO.AREA_ID = " + areaId + "\n");
		// // }
		// // if (!dealerCode.equals("")) {
		// // String[] array = dealerCode.split(",");
		// // sql.append(" AND tvo.billing_org_id IN (");
		// // for (int i = 0; i < array.length; i++) {
		// // sql.append("'" + array[i] + "'");
		// // if (i != array.length - 1) {
		// // sql.append(",");
		// // }
		// // }
		// // sql.append(")\n");
		// // }
		// // sql.append(" group by
		// tvo.order_year,tvo.order_week,tvo.billing_org_id) e\n");
		// sql.append("(select /*+ all_rows*/ttt.billing_org_id,\n");
		// sql.append(" ttt.order_year,\n");
		// sql.append(" ttt.order_week,\n");
		// sql.append(" sum(ttt.c_delivery_amount) delivery_amount,\n");
		// sql.append(" sum(ttt.b_delivery_amount) b_delivery_amount,\n");
		// sql.append(" sum(ttt.d_delivery_amount) d_delivery_amount\n");
		// sql.append("from (\n");
		// // sql.append("(select /*+ all_rows*/tvo.billing_org_id,
		// tvo.order_year,tvo.order_week,sum(tvdd.delivery_amount)
		// delivery_amount\n");
		// sql.append("select /*+ all_rows*/\n");
		// sql.append(" tvo.order_org_id billing_org_id,\n");
		// sql.append(" tvo.order_year,\n");
		// sql.append(" tvo.order_week,\n");
		// sql.append(" decode(tvo.ORDER_TYPE,
		// ").append(Constant.ORDER_TYPE_01).append(",
		// sum(tvdd.delivery_amount), 0) c_delivery_amount,\n");
		// sql.append(" decode(tvo.ORDER_TYPE,
		// ").append(Constant.ORDER_TYPE_02).append(",
		// sum(tvdd.delivery_amount), 0) b_delivery_amount,\n");
		// sql.append(" decode(tvo.ORDER_TYPE,
		// ").append(Constant.ORDER_TYPE_03).append(",
		// sum(tvdd.delivery_amount), 0) d_delivery_amount\n");
		//
		// sql.append(" from tt_vs_order tvo, tt_vs_dlvry tvd, tt_vs_dlvry_dtl
		// tvdd, tm_vhcl_material_group tvmg, tm_vhcl_material_group_r
		// tvmgr\n");
		// sql.append(" where tvo.order_id = tvd.order_id\n");
		// sql.append(" and tvmgr.material_id = tvdd.material_id\n");
		// sql.append(" and tvmgr.group_id = tvmg.group_id\n");
		// //TODO 添加物料组过滤条件 2012-05-22 HXY
		// if(groupCode != null && !"".equals(groupCode)) {
		// sql.append("and TVMG.GROUP_CODE in ('");
		// if(!groupCode.contains(",")) {
		// sql.append(groupCode);
		// } else {
		// String[] groupCodes = groupCode.split(",");
		// for(int i=0; i<groupCodes.length; i++) {
		// if(i != groupCodes.length-1) {
		// sql.append(groupCodes[i]);
		// sql.append("','");
		// } else {
		// sql.append(groupCodes[i]);
		// }
		// }
		// }
		// sql.append("')\n");
		// }
		// sql.append(" and tvd.delivery_id = tvdd.delivery_id\n");
		// //sql.append(" AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 +
		// "\n");
		// sql.append(" and tvd.delivery_status in\n");
		// sql.append("
		// (").append(Constant.DELIVERY_STATUS_04).append(",").append(Constant.DELIVERY_STATUS_05).append(",").append(Constant.DELIVERY_STATUS_10).append(",").append(Constant.DELIVERY_STATUS_11).append(",").append(Constant.DELIVERY_STATUS_12).append(")\n");
		//		
		// if (orderYear != null && orderWeek != null && !orderYear.equals("")
		// && !orderWeek.equals("")) {
		// sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// sql.append(" tvo.ORDER_WEEK) >=
		// ").append(orderYear).append(orderWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		//		
		// if (endYear != null && endWeek != null && !endYear.equals("") &&
		// !endWeek.equals("")) {
		// sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
		// sql.append(" 1,\n");
		// sql.append(" '0' || tvo.ORDER_WEEK,\n");
		// sql.append(" tvo.ORDER_WEEK) <=
		// ").append(endYear).append(endWeek).append("\n");
		//
		// /*sql.append(" AND TQ.QUOTA_YEAR = " + orderYear + "\n");*/
		// }
		// /*if (!orderYear.equals("")) {
		// sql.append(" AND tvO.ORDER_YEAR = " + orderYear + "\n");
		// }
		// if (!orderWeek.equals("")) {
		// sql.append(" AND tvO.ORDER_WEEK = " + orderWeek + "\n");
		// }*/
		// sql.append(" AND tvo.AREA_ID IN (" + areaIds + ")\n");
		// if (areaId != null && !areaId.equals("")) {
		// sql.append(" AND tvO.AREA_ID = " + areaId + "\n");
		// }
		// /*if (!dealerCode.equals("")) {
		// String[] array = dealerCode.split(",");
		// sql.append(" AND tvo.billing_org_id IN (");
		// for (int i = 0; i < array.length; i++) {
		// sql.append("'" + array[i] + "'");
		// if (i != array.length - 1) {
		// sql.append(",");
		// }
		// }
		// sql.append(")\n");
		// }*/
		// sql.append(" group by
		// tvo.ORDER_TYPE,tvo.order_year,tvo.order_week,tvo.order_org_id\n");
		// sql.append(") ttt\n");
		// sql.append(" group by ttt.billing_org_id, ttt.order_year,
		// ttt.order_week) e\n");
		// sql.append(" WHERE A.DEALER_ID = B.DEALER_ID(+)\n");
		// sql.append(" AND A.DEALER_ID = C.DEALER_ID(+)");
		// sql.append(" AND A.DEALER_ID = D.billing_org_id(+)");
		// sql.append(" AND A.DEALER_ID = e.billing_org_id(+)");
		// sql.append("and a.QUOTA_week = b.QUOTA_week(+)\n");
		// sql.append("and a.QUOTA_week = c.order_week(+)\n");
		// sql.append("and a.QUOTA_week = d.order_week(+)\n");
		// sql.append("and a.QUOTA_week = e.order_week(+)\n");
		// sql.append("and a.QUOTA_year = b.QUOTA_year(+)\n");
		// sql.append("and a.QUOTA_year = c.order_year(+)\n");
		// sql.append("and a.QUOTA_year = d.order_year(+)\n");
		// sql.append("and a.QUOTA_year = e.order_year(+)\n");
		// //TODO 添加物料组过滤条件 2012-05-22 HXY
		// /*if(groupCode != null && !"".equals(groupCode)) {
		// sql.append("and A.GROUP_CODE in ('");
		// if(!groupCode.contains(",")) {
		// sql.append(groupCode);
		// } else {
		// String[] groupCodes = groupCode.split(",");
		// for(int i=0; i<groupCodes.length; i++) {
		// if(i != groupCodes.length-1) {
		// sql.append(groupCodes[i]);
		// sql.append("','");
		// } else {
		// sql.append(groupCodes[i]);
		// }
		// }
		// }
		// sql.append("')\n");
		// }*/
		// sql.append("order by A.QUOTA_YEAR,A.QUOTA_WEEK\n");

		sql.append("  with a as (/*+ all_rows*/SELECT TQ.QUOTA_YEAR,\n");
		sql.append("               TQ.QUOTA_WEEK,\n");
		sql.append("               TD.DEALER_ID,\n");
		sql.append("               TD.DEALER_CODE,\n");
		sql.append("               tmr.region_name,\n");
		sql.append("               TMO.ORG_NAME,\n");
		sql.append("               TD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("               SUM(TQD.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("          FROM TT_VS_QUOTA TQ,TT_VS_QUOTA_DETAIL TQD, TM_DEALER TD, vw_org_dealer TDOR, TM_ORG TMO, TM_VHCL_MATERIAL_GROUP TVMG, tm_region tmr\n");
		sql.append("         WHERE TQ.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TQ.QUOTA_ID=TQD.QUOTA_ID");
		sql.append("           AND td.province_ID=tmr.region_code");
		sql.append("	AND TQD.GROUP_ID = TVMG.GROUP_ID  /*新增*/\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("           AND TDOR.DEALER_ID = TD.DEALER_ID");
		sql.append("           AND TDOR.root_ORG_ID = TMO.ORG_ID");
		sql.append("           AND TQ.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("           AND TQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		sql.append("           AND TQD.QUOTA_AMT <> 0\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQ.QUOTA_YEAR || decode(length(TQ.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQ.QUOTA_WEEK,\n");
			sql.append("                                       TQ.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");
		}
		sql.append("           AND TQ.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TQ.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}
		sql.append("         GROUP BY TQ.QUOTA_YEAR,\n");
		sql.append("                  TQ.QUOTA_WEEK,\n");
		sql.append("                  TD.DEALER_ID,\n");
		sql.append("                  TD.DEALER_CODE,\n");
		sql.append("                  tmr.region_name,\n");
		sql.append("                  TMO.ORG_NAME,\n");
		sql.append("                  TD.DEALER_SHORTNAME),\n");

		sql.append(" b as     (SELECT /*+ all_rows*/TD.DEALER_ID, tqm.quota_year, tqm.quota_week,SUM(TQM.MIN_AMOUNT) MIN_AMOUNT\n");
		sql.append("          FROM TT_VS_QUOTA_MIN TQM, TM_DEALER TD, tm_vhcl_material_group tvmg\n");
		sql.append("         WHERE TQM.DEALER_ID = TD.DEALER_ID\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		sql.append("and tqm.group_id = tvmg.group_id\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND TQm.QUOTA_YEAR || decode(length(TQm.QUOTA_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || TQm.QUOTA_WEEK,\n");
			sql.append("                                       TQm.QUOTA_WEEK) <= ").append(endYear).append(endWeek).append("\n");
		}

		sql.append("           AND TQM.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TQM.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         GROUP BY tqm.quota_year,tqm.quota_week,TD.DEALER_ID),\n");

		sql.append(" c as      (SELECT /*+ all_rows*/TD.DEALER_ID, tso.order_year,tso.order_week,SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT,\n");
		sql.append("               SUM(TSOD.check_AMOUNT) check_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER TSO, TT_VS_ORDER_DETAIL TSOD, TM_DEALER TD, tm_vhcl_material_group tvmg, tm_vhcl_material_group_r tvmgr\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("	and tsod.material_id = tvmgr.material_id\n");
		sql.append("           and tvmgr.group_id = tvmg.group_id\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("           AND TSO.ORDER_ORG_ID = TD.DEALER_ID\n");
		sql.append("           AND TSO.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_STATUS_03 + ", " + Constant.ORDER_STATUS_05 + ")\n");
		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tso.ORDER_YEAR || decode(length(tso.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tso.ORDER_WEEK,\n");
			sql.append("                                       tso.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");
		}

		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		}
		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append("         GROUP BY TSO.Order_Year,tso.order_week,TD.DEALER_ID),\n");

		sql.append("d as (select /*+ all_rows*/tvo.order_org_id billing_org_id, tvo.order_year,tvo.order_week,sum(tvdrd.req_amount) req_amount, sum(tvdrd.reserve_amount) reserve_amount\n");
		sql.append("          from tt_vs_order         tvo,\n");
		sql.append("               tt_vs_dlvry_req     tvdr,\n");
		sql.append("               tt_vs_dlvry_req_dtl tvdrd,\n");
		sql.append("			tm_vhcl_material_group tvmg,\n");
		sql.append("               tm_vhcl_material_group_r tvmgr\n");
		sql.append("         where tvo.order_id = tvdr.order_id\n");
		sql.append("           and tvdr.req_id = tvdrd.req_id\n");
		sql.append("	and tvdrd.material_id = tvmgr.material_id\n");
		sql.append("           and tvmgr.group_id = tvmg.group_id\n");
		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}
		sql.append("           AND tvo.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           and tvdr.req_status not in (").append(Constant.ORDER_REQ_STATUS_07).append(",").append(Constant.ORDER_REQ_STATUS_YSH).append(")\n");
		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");
		}

		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvO.AREA_ID = " + areaId + "\n");
		}

		sql.append("         group by tvo.order_year,tvo.order_week,tvo.order_org_id),\n");

		sql.append(" e as (select /*+ all_rows*/ttt.billing_org_id,\n");
		sql.append("       ttt.order_year,\n");
		sql.append("       ttt.order_week,\n");
		sql.append("       ttt.dealer_code,\n");
		sql.append("       ttt.root_org_name,\n");
		sql.append("       ttt.dealer_name,\n");
		sql.append("       ttt.region_name,\n");
		sql.append("       sum(ttt.c_delivery_amount) delivery_amount,\n");
		sql.append("       sum(ttt.b_delivery_amount) b_delivery_amount,\n");
		sql.append("       sum(ttt.d_delivery_amount) d_delivery_amount\n");
		sql.append("   from (select /*+ all_rows*/\n");
		sql.append("          tvo.order_org_id billing_org_id,\n");
		sql.append("          tvo.order_year,\n");
		sql.append("          tvo.order_week,\n");
		sql.append("          td.dealer_code,\n");
		sql.append("          vod.root_org_name,\n");
		sql.append("          td.dealer_shortname dealer_name,\n");
		sql.append("          tmr.region_name,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_01).append(", sum(tvdd.delivery_amount), 0) c_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_02).append(", sum(tvdd.delivery_amount), 0) b_delivery_amount,\n");
		sql.append("         decode(tvo.ORDER_TYPE, ").append(Constant.ORDER_TYPE_03).append(", sum(tvdd.delivery_amount), 0) d_delivery_amount\n");
		sql.append("from             tt_vs_order              tvo,\n");
		sql.append("                 tt_vs_dlvry              tvd,\n");
		sql.append("                 tt_vs_dlvry_dtl          tvdd,\n");
		sql.append("                 tm_vhcl_material_group   tvmg,\n");
		sql.append("                 tm_vhcl_material_group_r tvmgr,\n");
		sql.append("                 vw_org_dealer            vod,\n");
		sql.append("                 tm_dealer                td,\n");
		sql.append("                 tm_region                tmr\n");
		sql.append("           where tvo.order_id = tvd.order_id\n");
		sql.append("             and tvmgr.material_id = tvdd.material_id\n");
		sql.append("             and tvmgr.group_id = tvmg.group_id\n");
		sql.append("             and tvd.delivery_id = tvdd.delivery_id\n");
		sql.append("             and tvo.order_org_id = td.dealer_id\n");
		sql.append("             and td.province_id = tmr.region_code\n");
		sql.append("             and vod.dealer_id = td.dealer_id\n");

		if (dealerSql != null && !dealerSql.equals("")) {
			sql.append("           AND " + dealerSql);
		}

		if (dealerCode != null && !dealerCode.equals("")) {
			String[] array = dealerCode.split(",");
			sql.append("           AND TD.DEALER_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}

		// TODO 添加物料组过滤条件 2012-05-22 HXY
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append("and TVMG.GROUP_CODE in ('");
			if (!groupCode.contains(",")) {
				sql.append(groupCode);
			} else {
				String[] groupCodes = groupCode.split(",");
				for (int i = 0; i < groupCodes.length; i++) {
					if (i != groupCodes.length - 1) {
						sql.append(groupCodes[i]);
						sql.append("','");
					} else {
						sql.append(groupCodes[i]);
					}
				}
			}
			sql.append("')\n");
		}

		if (dealerId != null && !"".equals(dealerId)) {
			sql.append("	AND td.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		if (regionId != null && !"".equals(regionId)) {
			sql.append("           AND TD.PROVINCE_ID = " + regionId + "\n");
		}

		sql.append("           and tvd.delivery_id = tvdd.delivery_id\n");
		sql.append("           and tvd.delivery_status in\n");
		sql.append("               (").append(Constant.DELIVERY_STATUS_04).append(",").append(Constant.DELIVERY_STATUS_05).append(",").append(Constant.DELIVERY_STATUS_10).append(",").append(Constant.DELIVERY_STATUS_11).append(",").append(Constant.DELIVERY_STATUS_12).append(")\n");

		if (orderYear != null && orderWeek != null && !orderYear.equals("") && !orderWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) >= ").append(orderYear).append(orderWeek).append("\n");
		}

		if (endYear != null && endWeek != null && !endYear.equals("") && !endWeek.equals("")) {
			sql.append("AND tvo.ORDER_YEAR || decode(length(tvo.ORDER_WEEK),\n");
			sql.append("                                       1,\n");
			sql.append("                                       '0' || tvo.ORDER_WEEK,\n");
			sql.append("                                       tvo.ORDER_WEEK) <= ").append(endYear).append(endWeek).append("\n");
		}

		sql.append("           AND tvo.AREA_ID IN (" + areaIds + ")\n");
		if (areaId != null && !areaId.equals("")) {
			sql.append("           AND tvO.AREA_ID = " + areaId + "\n");
		}

		sql.append("           group by tvo.ORDER_TYPE,\n");
		sql.append("                    tvo.order_year,\n");
		sql.append("                    tvo.order_week,\n");
		sql.append("                    tvo.order_org_id,\n");
		sql.append("                    td.dealer_code,\n");
		sql.append("                    vod.root_org_name,\n");
		sql.append("                    tmr.region_name,\n");
		sql.append("                    td.dealer_shortname) ttt\n");
		sql.append("   group by ttt.billing_org_id,\n");
		sql.append("            ttt.order_year,\n");
		sql.append("            ttt.order_week,\n");
		sql.append("            ttt.dealer_code,\n");
		sql.append("            ttt.root_org_name,\n");
		sql.append("            ttt.region_name,\n");
		sql.append("            ttt.dealer_name)\n");

		sql.append("select /*+ all_rows*/\n");
		sql.append(" nvl(A.QUOTA_YEAR, e.order_year) || '年' || nvl(A.QUOTA_WEEK, e.order_week) || '周' QUOTA_DATE,\n");
		sql.append(" nvl(A.ORG_NAME, e.root_org_name) org_name,\n");
		sql.append(" nvl(A.region_NAME, e.region_NAME) region_NAME,\n");
		sql.append(" nvl(A.DEALER_CODE, e.DEALER_CODE) DEALER_CODE,\n");
		sql.append(" nvl(A.DEALER_NAME, e.DEALER_NAME) DEALER_NAME,\n");
		sql.append(" nvl(A.QUOTA_AMT, 0) QUOTA_AMT,\n");
		sql.append(" nvl(ROUND(A.QUOTA_AMT * 80 / 100, 0), 0) MIN_AMOUNT,\n");
		sql.append(" NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		sql.append(" NVL(C.check_AMOUNT, 0) CHECK_AMOUNT,\n");
		sql.append(" nvl(d.req_amount, 0) REQ_AMOUNT,\n");
		sql.append(" nvl(d.reserve_amount, 0) RESERVE_AMOUNT,\n");
		sql.append(" nvl(e.delivery_amount, 0) DELIVERY_AMOUNT,\n");
		sql.append(" nvl(e.b_delivery_amount, 0) b_DELIVERY_AMOUNT,\n");
		sql.append(" nvl((A.QUOTA_AMT - NVL(C.ORDER_AMOUNT, 0)), 0) CY,\n");
		sql.append(" ROUND((NVL(e.delivery_amount, 0) * 100 /\n");
		sql.append("       NVL(decode(c.check_amount, 0, 1, c.check_amount), 1)),\n");
		sql.append("       2) || '%' ZXL,\n");
		sql.append(" ROUND((NVL(e.delivery_amount, 0) * 100 /\n");
		sql.append("       decode(nvl(e.delivery_amount, 0) + nvl(e.b_delivery_amount, 0),\n");
		sql.append("               0,\n");
		sql.append("               1,\n");
		sql.append("               nvl(e.delivery_amount, 0) + nvl(e.b_delivery_amount, 0))),\n");
		sql.append("       2) || '%' ZBL,\n");
		sql.append(" nvl(ROUND((NVL(C.ORDER_AMOUNT, 0) * 100 / A.QUOTA_AMT), 2), 0) || '%' WCL\n");
		sql.append("  from a\n");
		sql.append("  full outer join e\n");
		sql.append("    on A.DEALER_ID = e.billing_org_id\n");
		sql.append("   and a.QUOTA_week = e.order_week\n");
		sql.append("   and a.QUOTA_year = e.order_year\n");
		sql.append("  left outer join b\n");
		sql.append("    on A.DEALER_ID = B.DEALER_ID\n");
		sql.append("   and a.QUOTA_week = b.QUOTA_week\n");
		sql.append("   and a.QUOTA_year = b.QUOTA_year\n");
		sql.append("  left outer join c\n");
		sql.append("    on A.DEALER_ID = C.DEALER_ID\n");
		sql.append("   and a.QUOTA_week = c.order_week\n");
		sql.append("   and a.QUOTA_year = c.order_year\n");
		sql.append("  left outer join d\n");
		sql.append("    on A.DEALER_ID = D.billing_org_id\n");
		sql.append("   and a.QUOTA_week = d.order_week\n");
		sql.append("   and a.QUOTA_year = d.order_year\n");
		sql.append(" order by nvl(A.QUOTA_YEAR, e.order_year), nvl(A.QUOTA_WEEK, e.order_week)\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}

	public List<Map<String, Object>> orderResourceQuery(Map<String, String> map) {
		String materialCode=map.get("materialCode");
		String orderYear=map.get("orderYear");
		String orderWeek=map.get("orderWeek");
		StringBuilder sql= new StringBuilder();
		sql.append("select   a.SERIES_NAME,---车系\n" );
		sql.append("       a.MATERIAL_CODE, ---物料编码\n" );
		sql.append("       a.MATERIAL_NAME, ---物料名称\n" );
		sql.append("       a.COLOR_NAME, ---颜色\n" );
		sql.append("       a.MATERIAL_ID,\n" );
		sql.append("       a.call_amount,\n" );
		sql.append("       tvw.ACT_STOCK,\n" );
		sql.append("       tvw.UNDO_ORDER_AMOUNT,\n" );
		sql.append("        nvl(tvw.ACT_STOCK,0)-nvl(tvw.UNDO_ORDER_AMOUNT,0) ACT_RESOURCE_AMOUNT\n" );
		sql.append("        from (SELECT\n" );
		sql.append("       VMG.SERIES_NAME,---车系\n" );
		sql.append("       TVM.MATERIAL_CODE, ---物料编码\n" );
		sql.append("       TVM.MATERIAL_NAME, ---物料名称\n" );
		sql.append("       TVM.COLOR_NAME, ---颜色\n" );
		sql.append("       TVM.MATERIAL_ID, ---物料ID\n" );
		sql.append("     sum (NVL(TSOD.check_AMOUNT, 0)) CALL_AMOUNT ---已审核数量\n" );
		sql.append("  FROM TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		sql.append("       VW_MATERIAL_GROUP        VMG,\n" );
		sql.append("       TM_AREA_GROUP            TAG,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );
		sql.append("        TT_VS_ORDER              TSO,\n" );
		sql.append("       TT_VS_ORDER_DETAIL       TSOD,\n" );
		sql.append("\n" );
		sql.append("      tm_dealer td\n" );
		sql.append(" WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n" );
		sql.append("   AND TSOD.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append("   AND TVMGR.GROUP_ID = VMG.PACKAGE_ID\n" );
		sql.append("   AND (VMG.SERIES_ID = TAG.MATERIAL_GROUP_ID OR\n" );
		sql.append("       VMG.MODEL_ID = TAG.MATERIAL_GROUP_ID OR\n" );
		sql.append("       VMG.PACKAGE_ID = TAG.MATERIAL_GROUP_ID)\n" );
		sql.append("   AND TAG.AREA_ID = TSO.AREA_ID\n" );
		sql.append("AND TVMG.GROUP_ID=TVMGR.GROUP_ID\n" );
		sql.append("  AND TVM.MATERIAL_ID=TVMGR.MATERIAL_ID\n" );
		sql.append("  and td.dealer_id=tso.order_org_id\n" );
		sql.append("   AND TSO.ORDER_TYPE = 10201001\n" );
		sql.append("   AND TSO.ORDER_STATUS = 10211005\n" );
		sql.append("  AND TSOD.CHECK_AMOUNT > 0\n" );
		sql.append("  AND NVL(TSOD.CHECK_AMOUNT, 0) <> NVL(TSOD.CALL_AMOUNT, 0)\n" );
		sql.append("   AND TSO.ORDER_YEAR = '"+orderYear+"'\n" );
		sql.append(" AND TSO.ORDER_WEEK = '"+orderWeek+"'\n" );
		if (materialCode != null && !materialCode.equals("")) {
			sql.append(" and tvm.material_code like '%"+materialCode+"%'");
		}
		sql.append(" group by  VMG.SERIES_NAME,---车系\n" );
		sql.append("       TVM.MATERIAL_CODE, ---物料编码\n" );
		sql.append("       TVM.MATERIAL_NAME, ---物料名称\n" );
		sql.append("       TVM.COLOR_NAME, ---颜色\n" );
		sql.append("       TVM.MATERIAL_ID) a, VW_VS_resource_ENTITY_week tvw\n" );
		sql.append("       where a.material_id=tvw.MATERIAL_ID\n" );
		sql.append("       order by a.series_name");
	
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
}
