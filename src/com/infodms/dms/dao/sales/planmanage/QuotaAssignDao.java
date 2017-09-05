/**
 * @Title: QuotaAssignDao.java
 *
 * @Description:
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-18
 *
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.dao.sales.planmanage;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmpVsQuotaPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 *
 */
public class QuotaAssignDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(QuotaAssignDao.class);
	private static final QuotaAssignDao dao = new QuotaAssignDao();
	private static final OrderReportDao reportDao = new OrderReportDao();

	public static final QuotaAssignDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 预留资源设定列表
	 *
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getResourceReserveSetQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String serie = (String) map.get("serie");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT A.GROUP_ID,\n");
		sql.append("       A.GROUP_CODE,\n");
		sql.append("       A.GROUP_NAME,\n");
		sql.append("       A.PLAN_YEAR,\n");
		sql.append("       A.PLAN_MONTH,\n");
		sql.append("       A.PLAN_WEEK,\n");
		sql.append("       A.PLAN_AMOUNT,\n");
		sql.append("       NVL(B.LAST_RESERVE_AMT, 0) LAST_RESERVE_AMT\n");
		sql.append("  FROM (SELECT TVMG1.GROUP_ID,\n");
		sql.append("               TVMG1.GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_NAME,\n");
		sql.append("               TVPP.PLAN_YEAR,\n");
		sql.append("               TVPP.PLAN_MONTH,\n");
		sql.append("               TVPP.PLAN_WEEK,\n");
		sql.append("               TVPPD.PLAN_AMOUNT\n");
		sql.append("          FROM TT_VS_PRODUCTION_PLAN        TVPP,\n");
		sql.append("               TT_VS_PRODUCTION_PLAN_DETAIL TVPPD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP       TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP       TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP       TVMG3\n");
		sql.append("         WHERE TVPP.PLAN_ID = TVPPD.PLAN_ID\n");
		sql.append("           AND TVPPD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TVPP.AREA_ID IN (" + areaIds + ")\n");
		sql.append("           AND TVPP.PLAN_VER =\n");
		sql.append("               (SELECT MAX(TVPP1.PLAN_VER)\n");
		sql.append("                  FROM TT_VS_PRODUCTION_PLAN TVPP1\n");
		sql.append("                 WHERE TVPP1.PLAN_YEAR = TVPP.PLAN_YEAR\n");
		sql
				.append("                   AND TVPP1.PLAN_MONTH = TVPP.PLAN_MONTH)\n");
		sql.append("           AND TVPP.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TVPP.STATUS = "
				+ Constant.PLAN_MANAGE_CONFIRM + "\n");
		if (!year.equals("")) {
			sql.append("           AND TVPP.PLAN_YEAR = " + year + "\n");
		}
		if (!month.equals("")) {
			sql.append("           AND TVPP.PLAN_MONTH = " + month + "\n");
		}
		if (!serie.equals("")) {
			sql.append("            AND TVMG3.GROUP_ID = " + serie + "\n");
		}
		sql.append(") A,\n");
		sql.append("       (SELECT TVMG1.GROUP_ID,\n");
		sql.append("               TVRR.RESERVE_WEEK,\n");
		sql.append("               TVRR.RESERVE_AMT LAST_RESERVE_AMT\n");
		sql.append("          FROM TT_VS_RESOURCE_RESERVE TVRR,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append("         WHERE TVRR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TVRR.COMPANY_ID = " + companyId + "\n");
		if (!year.equals("")) {
			sql.append("           AND TVRR.RESERVE_YEAR = " + year + "\n");
		}
		if (!month.equals("")) {
			sql.append("           AND TVRR.RESERVE_MONTH = " + month + "\n");
		}
		if (!serie.equals("")) {
			sql.append("            AND TVMG3.GROUP_ID = " + serie + "\n");
		}
		sql.append(") B\n");
		sql.append(" WHERE A.GROUP_ID = B.GROUP_ID(+)\n");
		sql.append("   AND A.PLAN_WEEK = B.RESERVE_WEEK(+)\n");
		sql.append(" ORDER BY A.PLAN_WEEK ASC, A.GROUP_CODE");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getResourceReserveSetQueryList",
				pageSize, curPage);
		return ps;
	}

	/**
	 * 预留资源查询列表
	 *
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getResourceReserveQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String serie = (String) map.get("serie");
		String companyId = (String) map.get("companyId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TMP1.GROUP_ID,\n");
		sql.append("       TMP1.GROUP_CODE,\n");
		sql.append("       TMP1.GROUP_NAME,\n");
		sql.append("       TMP1.PLAN_YEAR,\n");
		sql.append("       TMP1.PLAN_MONTH,\n");
		sql.append("       TMP1.PLAN_WEEK,\n");
		sql.append("       TMP1.PLAN_AMOUNT,\n");
		sql.append("       NVL(TMP2.RESERVE_AMT, 0) RESERVE_AMT\n");
		sql.append("  FROM (SELECT TVMG1.GROUP_ID,\n");
		sql.append("               TVMG1.GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_NAME,\n");
		sql.append("               TVPP.PLAN_YEAR,\n");
		sql.append("               TVPP.PLAN_MONTH,\n");
		sql.append("               TVPP.PLAN_WEEK,\n");
		sql.append("               TVPPD.PLAN_AMOUNT\n");
		sql.append("          FROM TT_VS_PRODUCTION_PLAN        TVPP,\n");
		sql.append("               TT_VS_PRODUCTION_PLAN_DETAIL TVPPD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP       TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP       TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP       TVMG3\n");
		sql.append("         WHERE TVPP.PLAN_ID = TVPPD.PLAN_ID\n");
		sql.append("           AND TVPPD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TVPP.PLAN_VER =\n");
		sql.append("               (SELECT MAX(TVPP1.PLAN_VER)\n");
		sql.append("                  FROM TT_VS_PRODUCTION_PLAN TVPP1\n");
		sql.append("                 WHERE TVPP1.PLAN_YEAR = TVPP.PLAN_YEAR\n");
		sql
				.append("                   AND TVPP1.PLAN_MONTH = TVPP.PLAN_MONTH)\n");
		sql.append("           AND TVPP.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TVPP.STATUS = "
				+ Constant.PLAN_MANAGE_CONFIRM + "\n");
		if (!year.equals("")) {
			sql.append("           AND TVPP.PLAN_YEAR = " + year + "\n");
		}
		if (!month.equals("")) {
			sql.append("           AND TVPP.PLAN_MONTH = " + month + "\n");
		}
		if (!serie.equals("")) {
			sql.append("            AND TVMG3.GROUP_ID = " + serie + "\n");
		}
		sql.append(") TMP1,\n");
		sql.append("       (SELECT TVMG1.GROUP_ID,\n");
		sql.append("               TVRR.RESERVE_WEEK,\n");
		sql.append("               TVRR.RESERVE_AMT\n");
		sql.append("          FROM TT_VS_RESOURCE_RESERVE TVRR,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append("         WHERE TVRR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TVRR.COMPANY_ID = " + companyId + "\n");
		if (!year.equals("")) {
			sql.append("           AND TVRR.RESERVE_YEAR = " + year + "\n");
		}
		if (!month.equals("")) {
			sql.append("           AND TVRR.RESERVE_MONTH = " + month + "\n");
		}
		if (!serie.equals("")) {
			sql.append("            AND TVMG3.GROUP_ID = " + serie + "\n");
		}
		sql.append(") TMP2\n");
		sql.append(" WHERE TMP1.GROUP_ID = TMP2.GROUP_ID(+)\n");
		sql.append("   AND TMP1.PLAN_WEEK = TMP2.RESERVE_WEEK(+)\n");
		sql.append(" ORDER BY TMP1.PLAN_WEEK ASC, TMP1.GROUP_CODE\n");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getResourceReserveQueryList",
				pageSize, curPage);
		return ps;
	}

	/**
	 * 最大配额总量查询列表
	 *
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getMaxQuotaTotalQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String groupCode = (String) map.get("groupCode");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		String areaId = (String) map.get("areaId");

		List<Map<String, Object>> list = getWeekList(year, month, companyId);

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TMP.GROUP_ID,\n");
		sql.append("       TMP.GROUP_CODE,\n");
		sql.append("       TMP.GROUP_NAME,\n");
		sql.append("       SUM(TMP.PLAN_AMOUNT) PLAN_AMOUNT,\n");
		sql.append("       SUM(TMP.RESERVE_AMT) RESERVE_AMT,\n");
		sql
				.append("       SUM(TMP.PLAN_AMOUNT - TMP.RESERVE_AMT) QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			if (i != list.size() - 1) {
				sql.append("       SUM(NVL(TMP.W" + i + ", 0)) W" + i + ",\n");
			} else {
				sql.append("       SUM(NVL(TMP.W" + i + ", 0)) W" + i + "\n");
			}
		}
		sql.append("  FROM (SELECT A.GROUP_ID,\n");
		sql.append("               A.GROUP_CODE,\n");
		sql.append("               A.GROUP_NAME,\n");
		sql.append("               A.PLAN_YEAR,\n");
		sql.append("               A.PLAN_MONTH,\n");
		sql.append("               A.PLAN_WEEK,\n");
		sql.append("               A.PLAN_AMOUNT,\n");
		sql.append("               NVL(B.RESERVE_AMT, 0) RESERVE_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mapGet = (Map<String, Object>) list.get(i);
			if (i != list.size() - 1) {
				sql
						.append("               DECODE(A.PLAN_WEEK, "
								+ (String) mapGet.get("SET_WEEK")
								+ ", NVL(A.PLAN_AMOUNT, 0) - NVL(B.RESERVE_AMT, 0), 0) W"
								+ i + ",\n");
			} else {
				sql
						.append("               DECODE(A.PLAN_WEEK, "
								+ (String) mapGet.get("SET_WEEK")
								+ ", NVL(A.PLAN_AMOUNT, 0) - NVL(B.RESERVE_AMT, 0), 0) W"
								+ i + "\n");
			}
		}
		sql.append("          FROM (SELECT TVMG1.GROUP_ID,\n");
		sql.append("                       TVMG1.GROUP_CODE,\n");
		sql.append("                       TVMG1.GROUP_NAME,\n");
		sql.append("                       TVPP.PLAN_YEAR,\n");
		sql.append("                       TVPP.PLAN_MONTH,\n");
		sql.append("                       TVPP.PLAN_WEEK,\n");
		sql.append("                       TVPPD.PLAN_AMOUNT\n");
		sql
				.append("                  FROM TT_VS_PRODUCTION_PLAN        TVPP,\n");
		sql
				.append("                       TT_VS_PRODUCTION_PLAN_DETAIL TVPPD,\n");
		sql
				.append("                       TM_VHCL_MATERIAL_GROUP       TVMG1\n");
		sql.append("                 WHERE TVPP.PLAN_ID = TVPPD.PLAN_ID\n");
		sql.append("                   AND TVPPD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("                   AND TVPP.COMPANY_ID = " + companyId
				+ "\n");
		sql
				.append("                   AND TVPP.AREA_ID IN (" + areaIds
						+ ")\n");
		if (!areaId.equals("")) {
			sql
					.append("                   AND TVPP.AREA_ID = " + areaId
							+ "\n");
		}
		sql.append("                   AND TVPP.PLAN_VER =\n");
		sql.append("                       (SELECT MAX(TVPP1.PLAN_VER)\n");
		sql
				.append("                          FROM TT_VS_PRODUCTION_PLAN TVPP1\n");
		sql
				.append("                         WHERE TVPP1.PLAN_YEAR = TVPP.PLAN_YEAR\n");
		sql
				.append("                           AND TVPP1.PLAN_MONTH = TVPP.PLAN_MONTH)\n");
		sql.append("                   AND TVPP.STATUS = "
				+ Constant.PLAN_MANAGE_CONFIRM + "\n");
		if (!year.equals("")) {
			sql
					.append("                   AND TVPP.PLAN_YEAR = " + year
							+ "\n");
		}
		if (!month.equals("")) {
			sql.append("                   AND TVPP.PLAN_MONTH = " + month
					+ "\n");
		}
		if (!groupCode.equals("")) {
			String[] array = groupCode.split(",");
			sql.append("                   AND TVMG1.GROUP_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")");
		}
		sql.append(") A,\n");
		sql
				.append("               (SELECT TVRR.GROUP_ID, TVRR.RESERVE_WEEK, TVRR.RESERVE_AMT\n");
		sql.append("                  FROM TT_VS_RESOURCE_RESERVE TVRR,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP TVMG1\n");
		sql.append("                 WHERE TVRR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("                   AND TVRR.COMPANY_ID = " + companyId
				+ "\n");
		sql.append("                   AND TVRR.COMPANY_ID = " + companyId
				+ "\n");
		if (!year.equals("")) {
			sql.append("                   AND TVRR.RESERVE_YEAR = " + year
					+ "\n");
		}
		if (!month.equals("")) {
			sql.append("                   AND TVRR.RESERVE_MONTH = " + month
					+ "\n");
		}
		if (!groupCode.equals("")) {
			String[] array = groupCode.split(",");
			sql.append("                   AND TVMG1.GROUP_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")");
		}
		sql.append(") B\n");
		sql.append("         WHERE A.GROUP_ID = B.GROUP_ID(+)\n");
		sql.append("           AND A.PLAN_WEEK = B.RESERVE_WEEK(+)) TMP\n");
		sql
				.append(" GROUP BY TMP.GROUP_ID, TMP.GROUP_CODE, TMP.GROUP_NAME ORDER BY TMP.GROUP_CODE ASC");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
				getFunName() + year + month, pageSize, curPage);
		return ps;
	}

	/**
	 * 最大配额总量查询导出列表
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMaxQuotaTotalExportList(
			Map<String, Object> map) {

		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String groupCode = (String) map.get("groupCode");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		String areaId = (String) map.get("areaId");

		List<Map<String, Object>> list = getWeekList(year, month, companyId);

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TMP.GROUP_ID,\n");
		sql.append("       TMP.GROUP_CODE,\n");
		sql.append("       TMP.GROUP_NAME,\n");
		sql.append("       SUM(TMP.PLAN_AMOUNT) PLAN_AMOUNT,\n");
		sql.append("       SUM(TMP.RESERVE_AMT) RESERVE_AMT,\n");
		sql
				.append("       SUM(TMP.PLAN_AMOUNT - TMP.RESERVE_AMT) QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			if (i != list.size() - 1) {
				sql.append("       SUM(TMP.W" + i + ") W" + i + ",\n");
			} else {
				sql.append("       SUM(TMP.W" + i + ") W" + i + "\n");
			}
		}
		sql.append("  FROM (SELECT A.GROUP_ID,\n");
		sql.append("               A.GROUP_CODE,\n");
		sql.append("               A.GROUP_NAME,\n");
		sql.append("               A.PLAN_YEAR,\n");
		sql.append("               A.PLAN_MONTH,\n");
		sql.append("               A.PLAN_WEEK,\n");
		sql.append("               A.PLAN_AMOUNT,\n");
		sql.append("               NVL(B.RESERVE_AMT, 0) RESERVE_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mapGet = (Map<String, Object>) list.get(i);
			if (i != list.size() - 1) {
				sql.append("               DECODE(A.PLAN_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", A.PLAN_AMOUNT - NVL(B.RESERVE_AMT, 0), 0) W" + i
						+ ",\n");
			} else {
				sql.append("               DECODE(A.PLAN_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", A.PLAN_AMOUNT - NVL(B.RESERVE_AMT, 0), 0) W" + i
						+ "\n");
			}
		}
		sql.append("          FROM (SELECT TVMG1.GROUP_ID,\n");
		sql.append("                       TVMG1.GROUP_CODE,\n");
		sql.append("                       TVMG1.GROUP_NAME,\n");
		sql.append("                       TVPP.PLAN_YEAR,\n");
		sql.append("                       TVPP.PLAN_MONTH,\n");
		sql.append("                       TVPP.PLAN_WEEK,\n");
		sql.append("                       TVPPD.PLAN_AMOUNT\n");
		sql
				.append("                  FROM TT_VS_PRODUCTION_PLAN        TVPP,\n");
		sql
				.append("                       TT_VS_PRODUCTION_PLAN_DETAIL TVPPD,\n");
		sql
				.append("                       TM_VHCL_MATERIAL_GROUP       TVMG1\n");
		sql.append("                 WHERE TVPP.PLAN_ID = TVPPD.PLAN_ID\n");
		sql.append("                   AND TVPPD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("                   AND TVPP.COMPANY_ID = " + companyId
				+ "\n");
		sql
				.append("                   AND TVPP.AREA_ID IN (" + areaIds
						+ ")\n");
		if (!areaId.equals("")) {
			sql
					.append("                   AND TVPP.AREA_ID = " + areaId
							+ "\n");
		}
		sql.append("                   AND TVPP.PLAN_VER =\n");
		sql.append("                       (SELECT MAX(TVPP1.PLAN_VER)\n");
		sql
				.append("                          FROM TT_VS_PRODUCTION_PLAN TVPP1\n");
		sql
				.append("                         WHERE TVPP1.PLAN_YEAR = TVPP.PLAN_YEAR\n");
		sql
				.append("                           AND TVPP1.PLAN_MONTH = TVPP.PLAN_MONTH)\n");
		sql.append("                   AND TVPP.STATUS = "
				+ Constant.PLAN_MANAGE_CONFIRM + "\n");
		if (!year.equals("")) {
			sql
					.append("                   AND TVPP.PLAN_YEAR = " + year
							+ "\n");
		}
		if (!month.equals("")) {
			sql.append("                   AND TVPP.PLAN_MONTH = " + month
					+ "\n");
		}
		if (!groupCode.equals("")) {
			String[] array = groupCode.split(",");
			sql.append("                   AND TVMG1.GROUP_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")");
		}
		sql.append(") A,\n");
		sql
				.append("               (SELECT TVRR.GROUP_ID, TVRR.RESERVE_WEEK, TVRR.RESERVE_AMT\n");
		sql.append("                  FROM TT_VS_RESOURCE_RESERVE TVRR,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP TVMG1\n");
		sql.append("                 WHERE TVRR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("                   AND TVRR.COMPANY_ID = " + companyId
				+ "\n");
		sql.append("                   AND TVRR.COMPANY_ID = " + companyId
				+ "\n");
		if (!year.equals("")) {
			sql.append("                   AND TVRR.RESERVE_YEAR = " + year
					+ "\n");
		}
		if (!month.equals("")) {
			sql.append("                   AND TVRR.RESERVE_MONTH = " + month
					+ "\n");
		}
		if (!groupCode.equals("")) {
			String[] array = groupCode.split(",");
			sql.append("                   AND TVMG1.GROUP_CODE IN (");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")");
		}
		sql.append(") B\n");
		sql.append("         WHERE A.GROUP_ID = B.GROUP_ID(+)\n");
		sql.append("           AND A.PLAN_WEEK = B.RESERVE_WEEK(+)) TMP\n");
		sql
				.append(" GROUP BY TMP.GROUP_ID, TMP.GROUP_CODE, TMP.GROUP_NAME ORDER BY TMP.GROUP_CODE ASC");

		List<Map<String, Object>> results = pageQuery(sql.toString(), null,
				getFunName() + year + month);
		return results;
	}

	/**
	 * 获得区域配额计算日期列表
	 *
	 * @param paraId
	 * @return
	 */
	public List<Map<String, Object>> getQuotaDateList(Long companyId) {
		TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(), companyId);
		int year = Integer.parseInt(dateSet.getSetYear());
		int month = Integer.parseInt(dateSet.getSetMonth());

		TmBusinessParaPO para = reportDao.getTmBusinessParaPO(
				Constant.QUTOA_AHEAD_ISSUE_MONTH_PARA, companyId);
		int count = Integer.parseInt(para.getParaValue());
		month = month + count;
		if (month > 12) {
			month = month - 12;
			year++;
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", year + "-" + month);
		map.put("name", year + "年" + month + "月");
		list.add(map);

		return list;
	}

	/**
	 * 获得区域配额计算查询列表
	 *
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getAreaQuotaCalculateQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String areaId = (String) map.get("areaId");
		String userId = (String) map.get("userId");
		String companyId = (String) map.get("companyId");

		List<Map<String, Object>> list = getWeekList(year, month, companyId);

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TMP.ORG_CODE,\n");
		sql.append("       TMP.ORG_NAME,\n");
		sql.append("       TMP.GROUP_CODE,\n");
		sql.append("       TMP.GROUP_NAME,\n");
		sql.append("       SUM(TMP.QUOTA_AMT) QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			if (i != list.size() - 1) {
				sql.append("       SUM(TMP.W" + i + ") W" + i + ",\n");
			} else {
				sql.append("       SUM(TMP.W" + i + ") W" + i + "\n");
			}
		}
		sql.append("  FROM (SELECT TOR.ORG_CODE,\n");
		sql.append("               TOR.ORG_NAME,\n");
		sql.append("               TVMG.GROUP_CODE,\n");
		sql.append("               TVMG.GROUP_NAME,\n");
		sql.append("               TVOB.QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mapGet = (Map<String, Object>) list.get(i);
			if (i != list.size() - 1) {
				sql.append("               DECODE(TVOB.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVOB.QUOTA_AMT, 0) W" + i + ",\n");
			} else {
				sql.append("               DECODE(TVOB.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVOB.QUOTA_AMT, 0) W" + i + "\n");
			}
		}
		sql
				.append("          FROM TMP_VS_ORG_BASE TVOB, TM_VHCL_MATERIAL_GROUP TVMG, TM_ORG TOR\n");
		sql.append("         WHERE TVOB.ORG_ID = TOR.ORG_ID\n");
		sql.append("           AND TVMG.GROUP_ID = TVOB.GROUP_ID\n");
		sql.append("           AND TVOB.QUOTA_YEAR = " + year + "\n");
		sql.append("           AND TVOB.QUOTA_MONTH = " + month + "\n");
		sql.append("           AND TVOB.USER_ID = " + userId + "\n");
		sql.append("           AND TVOB.AREA_ID = " + areaId + ") TMP\n");
		sql
				.append(" GROUP BY TMP.ORG_CODE, TMP.ORG_NAME, TMP.GROUP_CODE, TMP.GROUP_NAME");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getAreaQuotaCalculateQueryList",
				pageSize, curPage);
		return ps;
	}

	/**
	 * 区域配额计算导出列表
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAreaQuotaCalculateExportList(
			Map<String, Object> map) {

		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String areaId = (String) map.get("areaId");
		String userId = (String) map.get("userId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TOR.ORG_CODE,\n");
		sql.append("       TOR.ORG_NAME,\n");
		sql.append("       TVMG.GROUP_CODE,\n");
		sql.append("       TVOB.QUOTA_YEAR,\n");
		sql.append("       TVOB.QUOTA_WEEK,\n");
		sql.append("       TVOB.QUOTA_AMT\n");
		sql.append("  FROM TMP_VS_ORG_BASE TVOB, TM_VHCL_MATERIAL_GROUP TVMG, TM_ORG TOR\n");
		sql.append(" WHERE TVOB.ORG_ID = TOR.ORG_ID\n");
		sql.append("   AND TVOB.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVOB.QUOTA_YEAR = " + year + "\n");
		sql.append("   AND TVOB.QUOTA_MONTH = " + month + "\n");
		sql.append("   AND TVOB.USER_ID = " + userId + "\n");
		sql.append("   AND TVOB.AREA_ID = " + areaId + "\n");
		sql
				.append(" ORDER BY TOR.ORG_CODE, TVMG.GROUP_CODE, TVOB.QUOTA_YEAR, TVOB.QUOTA_WEEK");

		List<Map<String, Object>> results = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getAreaQuotaCalculateExportList");
		return results;
	}

	public List<Map<String, Object>> getAreaHead(Map<String, Object> map) {

		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String areaId = (String) map.get("areaId");
		String userId = (String) map.get("userId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TOR.ORG_CODE,\n");
		sql.append("       TVMG.GROUP_CODE\n");
		sql.append("  FROM TMP_VS_ORG_BASE TVOB, TM_VHCL_MATERIAL_GROUP TVMG, TM_ORG TOR\n");
		sql.append(" WHERE TVOB.ORG_ID = TOR.ORG_ID\n");
		sql.append("   AND TVOB.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVOB.QUOTA_YEAR = " + year + "\n");
		sql.append("   AND TVOB.QUOTA_MONTH = " + month + "\n");
		sql.append("   AND TVOB.USER_ID = " + userId + "\n");
		sql.append("   AND TVOB.AREA_ID = " + areaId + "\n");
		sql.append(" ORDER BY TOR.ORG_CODE, TVMG.GROUP_CODE");

		List<Map<String, Object>> results = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.QuotaAssignDao.getAreaQuotaCalculateExportList");
		return results;
	}

	/**
	 * 获得经销商配额计算查询列表
	 *
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getDealerQuotaCalculateQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String areaId = (String) map.get("areaId");
		String curType = (String) map.get("curType");
		String userId = (String) map.get("userId");
		String companyId = (String) map.get("companyId");

		List<Map<String, Object>> list = getWeekList(year, month, companyId);

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TMP.DEALER_CODE,\n");
		sql.append("       TMP.DEALER_NAME,\n");
		sql.append("       TMP.GROUP_CODE,\n");
		sql.append("       TMP.GROUP_NAME,\n");
		sql.append("       SUM(TMP.QUOTA_AMT) QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			if (i != list.size() - 1) {
				sql.append("       SUM(TMP.W" + i + ") W" + i + ",\n");
			} else {
				sql.append("       SUM(TMP.W" + i + ") W" + i + "\n");
			}
		}
		sql.append("  FROM (SELECT TD.DEALER_CODE,\n");
		sql.append("               TD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("               TVMG.GROUP_CODE,\n");
		sql.append("               TVMG.GROUP_NAME,\n");
		sql.append("               TVDQ.QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mapGet = (Map<String, Object>) list.get(i);
			if (i != list.size() - 1) {
				sql.append("               DECODE(TVDQ.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVDQ.QUOTA_AMT, 0) W" + i + ",\n");
			} else {
				sql.append("               DECODE(TVDQ.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVDQ.QUOTA_AMT, 0) W" + i + "\n");
			}
		}
		sql.append("          FROM TMP_VS_DLR_QUOTA       TVDQ,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("               TM_DEALER              TD\n");
		sql.append("         WHERE TVDQ.DEALER_ID = TD.DEALER_ID\n");
		sql.append("           AND TVMG.GROUP_ID = TVDQ.GROUP_ID\n");
		sql.append("           AND TVDQ.QUOTA_YEAR = " + year + "\n");
		sql.append("           AND TVDQ.QUOTA_MONTH = " + month + "\n");
		sql.append("           AND TVDQ.USER_ID = " + userId + "\n");
		sql.append("           AND TVDQ.CUR_TYPE = " + curType + "\n");
		sql.append("           AND TVDQ.AREA_ID = " + areaId + ") TMP\n");
		sql
				.append(" GROUP BY TMP.DEALER_CODE, TMP.DEALER_NAME, TMP.GROUP_CODE, TMP.GROUP_NAME");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getDealerQuotaCalculateQueryList",
				pageSize, curPage);
		return ps;
	}

	/**
	 * 经销商配额计算导出列表
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDealerQuotaCalculateExportList(
			Map<String, Object> map) {
		List<Object> params = new ArrayList<Object>() ;

		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String areaId = (String) map.get("areaId");
		String curType = (String) map.get("curType");
		String userId = (String) map.get("userId");
		String dutyType = map.get("dutyType").toString() ;
		String orgId = map.get("orgId").toString() ;

		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT DISTINCT TDA.DEALER_CODE,\n");
		sql.append("       TDA.DEALER_NAME,\n");
		sql.append("       TVMG.GROUP_CODE,\n");
		sql.append("       TMR.REGION_NAME,\n");
		sql.append("       TVDQ.QUOTA_YEAR,\n");
		sql.append("       TVDQ.QUOTA_WEEK,\n");
		sql.append("       TVDQ.QUOTA_AMT\n");
		sql.append("  FROM TMP_VS_DLR_QUOTA        TVDQ,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP  TVMG,\n");
		sql.append("       TM_DEALER               TDA,\n");
		sql.append("       TM_REGION               TMR,\n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,TC_POSE TCP, TC_ROLE TCR, TR_ROLE_POSE TRP\n");
		sql.append(" WHERE TVDQ.DEALER_ID = TDA.DEALER_ID\n");
		sql.append("   AND TDA.PROVINCE_ID = TMR.REGION_CODE(+)\n");
		sql.append("   AND TVDQ.DEALER_ID = TDBA.DEALER_ID AND TCR.ROLE_ID=TRP.ROLE_ID AND TCR.Role_Desc NOT  LIKE '%撤站%' AND TCP.COMPANY_ID=TDA.COMPANY_ID AND TCP.POSE_ID=TRP.POSE_ID\n");
		sql.append("   AND TVDQ.GROUP_ID = TVMG.GROUP_ID\n");

		if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			sql.append("and exists (select 1\n");
			sql.append("          from tm_dealer_org_relation tdor\n");
			sql.append("         where tdor.dealer_id = tvdq.dealer_id\n");
			sql.append("         and tdor.org_id = ?)\n");

			params.add(orgId) ;
		}

		sql.append("   AND TVDQ.QUOTA_YEAR = ?\n");
		params.add(year) ;

		sql.append("   AND TVDQ.QUOTA_MONTH = ?\n");
		params.add(month) ;

		sql.append("   AND TVDQ.USER_ID = ?\n");
		params.add(userId) ;

		sql.append("   AND TVDQ.AREA_ID = ?\n");
		params.add(areaId) ;

		sql.append("   AND TDBA.AREA_ID = ?\n");
		params.add(areaId) ;

		sql.append("   AND TVDQ.CUR_TYPE = ?\n");
		params.add(curType) ;

		sql.append(" ORDER BY TDA.DEALER_CODE,\n");
		sql.append("          TVMG.GROUP_CODE,\n");
		sql.append("          TVDQ.QUOTA_YEAR,\n");
		sql.append("          TVDQ.QUOTA_WEEK\n");

		List<Map<String, Object>> results = pageQuery(sql.toString(), params,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getDealerQuotaCalculateExportList");
		return results;
	}

	/**
	 * 经销商配额计算导出列表
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDealerQuotaCalculateExportList_CVS(
			Map<String, Object> map) {
		List<Object> params = new ArrayList<Object>() ;

		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String areaId = (String) map.get("areaId");
		String curType = (String) map.get("curType");
		String userId = (String) map.get("userId");
		String dutyType = map.get("dutyType").toString() ;
		String orgId = map.get("orgId").toString() ;

		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT DISTINCT TDA.DEALER_CODE,\n");
		sql.append("       TDA.DEALER_NAME,\n");
		sql.append("       TVMG.GROUP_CODE,\n");
		sql.append("       TMR.REGION_NAME,\n");
		sql.append("       TVDQ.QUOTA_YEAR,\n");
		sql.append("       TVDQ.QUOTA_WEEK,\n");
		sql.append("       TVDQ.QUOTA_AMT\n");
		sql.append("  FROM TMP_VS_DLR_QUOTA        TVDQ,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP  TVMG,\n");
		sql.append("       TM_DEALER               TDA,\n");
		sql.append("       TM_REGION               TMR,\n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,TC_POSE TCP, TC_ROLE TCR, TR_ROLE_POSE TRP\n");
		sql.append(" WHERE TVDQ.DEALER_ID = TDA.DEALER_ID\n");
		sql.append("   AND TDA.PROVINCE_ID = TMR.REGION_CODE(+)\n");
		sql.append("   AND TVDQ.DEALER_ID = TDBA.DEALER_ID AND TCR.ROLE_ID=TRP.ROLE_ID AND TCR.Role_Desc NOT  LIKE '%撤站%' AND TCP.COMPANY_ID=TDA.COMPANY_ID AND TCP.POSE_ID=TRP.POSE_ID\n");
		sql.append("   AND TVDQ.GROUP_ID = TVMG.GROUP_ID\n");

		if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			sql.append("and exists (select 1\n");
			sql.append("          from vw_org_dealer tdor\n");
			sql.append("         where tdor.dealer_id = tvdq.dealer_id\n");
			sql.append("         and tdor.root_org_id = ?)\n");

			params.add(orgId) ;
		}

		sql.append("   AND TVDQ.QUOTA_YEAR = ?\n");
		params.add(year) ;

		sql.append("   AND TVDQ.QUOTA_MONTH = ?\n");
		params.add(month) ;

		sql.append("   AND TVDQ.USER_ID = ?\n");
		params.add(userId) ;

		sql.append("   AND TVDQ.AREA_ID = ?\n");
		params.add(areaId) ;

		sql.append("   AND TDBA.AREA_ID = ?\n");
		params.add(areaId) ;

		sql.append("   AND TVDQ.CUR_TYPE = ?\n");
		params.add(curType) ;

		sql.append(" ORDER BY TDA.DEALER_CODE,\n");
		sql.append("          TVMG.GROUP_CODE,\n");
		sql.append("          TVDQ.QUOTA_YEAR,\n");
		sql.append("          TVDQ.QUOTA_WEEK\n");

		List<Map<String, Object>> results = pageQuery(sql.toString(), params,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getDealerQuotaCalculateExportList");
		return results;
	}

	public List<Map<String, Object>> getHeadList(Map<String, Object> map) {
		List<Object> params = new ArrayList<Object>() ;

		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String areaId = (String) map.get("areaId");
		String curType = (String) map.get("curType");
		String userId = (String) map.get("userId");
		String dutyType = (String)map.get("dutyType") ;
		String orgId = (String)map.get("orgId") ;

		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT DISTINCT TDA.DEALER_CODE,\n");
		sql.append("       TDA.DEALER_NAME,\n");
		sql.append("       TMR.REGION_NAME,\n");
		sql.append("       TVMG.GROUP_CODE\n");
		sql.append("  FROM TMP_VS_DLR_QUOTA        TVDQ,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP  TVMG,\n");
		sql.append("       TM_DEALER               TDA,\n");
		sql.append("       TM_REGION               TMR,\n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,TC_POSE TCP, TC_ROLE TCR, TR_ROLE_POSE TRP\n");
		sql.append(" WHERE TVDQ.DEALER_ID = TDA.DEALER_ID\n");
		sql.append("   AND TDA.PROVINCE_ID = TMR.REGION_CODE(+)\n");
		sql.append("   AND TVDQ.DEALER_ID = TDBA.DEALER_ID AND TCR.ROLE_ID=TRP.ROLE_ID AND TCR.Role_Desc NOT  LIKE '%撤站%' AND TCP.COMPANY_ID=TDA.COMPANY_ID AND TCP.POSE_ID=TRP.POSE_ID\n");
		sql.append("   AND TVDQ.GROUP_ID = TVMG.GROUP_ID\n");

		if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			sql.append("and exists (select 1\n");
			sql.append("          from tm_dealer_org_relation tdor\n");
			sql.append("         where tdor.dealer_id = tvdq.dealer_id\n");
			sql.append("         and tdor.org_id = ?)\n");

			params.add(orgId) ;
		}

		sql.append("   AND TVDQ.QUOTA_YEAR = ?\n");
		params.add(year) ;

		sql.append("   AND TVDQ.QUOTA_MONTH = ?\n");
		params.add(month) ;

		sql.append("   AND TVDQ.USER_ID = ?\n");
		params.add(userId) ;

		sql.append("   AND TVDQ.AREA_ID = ?\n");
		params.add(areaId) ;

		sql.append("   AND TDBA.AREA_ID = ?\n");
		params.add(areaId) ;

		sql.append("   AND TVDQ.CUR_TYPE = ?\n");
		params.add(curType) ;

		sql.append(" ORDER BY TDA.DEALER_CODE,\n");
		sql.append("          TVMG.GROUP_CODE\n");

		List<Map<String, Object>> results = pageQuery(sql.toString(), params,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getDealerQuotaCalculateExportList");
		return results;
	}

	public List<Map<String, Object>> getHeadList_CVS(Map<String, Object> map) {
		List<Object> params = new ArrayList<Object>() ;

		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String areaId = (String) map.get("areaId");
		String curType = (String) map.get("curType");
		String userId = (String) map.get("userId");
		String dutyType = (String)map.get("dutyType") ;
		String orgId = (String)map.get("orgId") ;

		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT DISTINCT TDA.DEALER_CODE,\n");
		sql.append("       TDA.DEALER_NAME,\n");
		sql.append("       TMR.REGION_NAME,\n");
		sql.append("       TVMG.GROUP_CODE\n");
		sql.append("  FROM TMP_VS_DLR_QUOTA        TVDQ,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP  TVMG,\n");
		sql.append("       TM_DEALER               TDA,\n");
		sql.append("       TM_REGION               TMR,\n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,TC_POSE TCP, TC_ROLE TCR, TR_ROLE_POSE TRP\n");
		sql.append(" WHERE TVDQ.DEALER_ID = TDA.DEALER_ID\n");
		sql.append("   AND TDA.PROVINCE_ID = TMR.REGION_CODE(+)\n");
		sql.append("   AND TVDQ.DEALER_ID = TDBA.DEALER_ID AND TCR.ROLE_ID=TRP.ROLE_ID AND TCR.Role_Desc NOT  LIKE '%撤站%' AND TCP.COMPANY_ID=TDA.COMPANY_ID AND TCP.POSE_ID=TRP.POSE_ID\n");
		sql.append("   AND TVDQ.GROUP_ID = TVMG.GROUP_ID\n");

		if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			sql.append("and exists (select 1\n");
			sql.append("          from vw_org_dealer tdor\n");
			sql.append("         where tdor.dealer_id = tvdq.dealer_id\n");
			sql.append("         and tdor.root_org_id = ?)\n");

			params.add(orgId) ;
		}

		sql.append("   AND TVDQ.QUOTA_YEAR = ?\n");
		params.add(year) ;

		sql.append("   AND TVDQ.QUOTA_MONTH = ?\n");
		params.add(month) ;

		sql.append("   AND TVDQ.USER_ID = ?\n");
		params.add(userId) ;

		sql.append("   AND TVDQ.AREA_ID = ?\n");
		params.add(areaId) ;

		sql.append("   AND TDBA.AREA_ID = ?\n");
		params.add(areaId) ;

		sql.append("   AND TVDQ.CUR_TYPE = ?\n");
		params.add(curType) ;

		sql.append(" ORDER BY TDA.DEALER_CODE,\n");
		sql.append("          TVMG.GROUP_CODE\n");

		List<Map<String, Object>> results = pageQuery(sql.toString(), params,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getDealerQuotaCalculateExportList");
		return results;
	}

	/**
	 * 根据年、月获得所有周度列表
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getWeekList(String year, String month,
			String companyId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT DISTINCT T.SET_YEAR, T.SET_MONTH, T.SET_WEEK\n");
		sql.append("  FROM TM_DATE_SET T\n");
		sql.append(" WHERE T.SET_YEAR = " + year + "\n");
		sql.append("   AND T.SET_MONTH = " + month + "\n");
		sql.append("   AND T.COMPANY_ID = " + companyId + "\n");
		sql.append(" ORDER BY T.SET_WEEK ASC");

		List<Map<String, Object>> results = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getWeekList");
		return results;
	}

	/**
	 * 查询每条数据在临时表是否重复 return: boolean true不重复，false重复
	 *
	 * @param paraPo
	 * @return
	 */
	public boolean isDateDump(TmpVsQuotaPO paraPo) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		boolean bl = true;

		sql.append("select count(*) amt \n");
		sql.append("  from TMP_VS_QUOTA p\n");
		sql.append(" where p.QUOTA_YEAR = ?\n");
		params.add(paraPo.getQuotaYear());
		sql.append("   and p.QUOTA_MONTH = ?\n");
		params.add(paraPo.getQuotaMonth());
		sql.append("   and p.QUOTA_WEEK = ?\n");
		params.add(paraPo.getQuotaWeek());
		sql.append("   and p.GROUP_CODE =?");
		params.add(paraPo.getGroupCode());
		if (paraPo.getOrgCode() != null) {
			sql.append("   and p.ORG_CODE =?");
			params.add(paraPo.getOrgCode());
		}
		if (paraPo.getDealerCode() != null) {
			sql.append("   and p.DEALER_CODE =?");
			params.add(paraPo.getDealerCode());
		}
		sql.append("   and p.USER_ID =?");
		params.add(paraPo.getUserId());

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params,
				getFunName());
		if (null != list && list.size() > 0) {
			Map<String, Object> map = list.get(0);
			Integer amt = new Integer(map.get("AMT").toString());
			if (amt.intValue() > 1) {
				bl = false;
			}
		}
		return bl;
	}

	public PageResult<Map<String, Object>> getAreaQuotaImportTempList(
			Map<String, Object> map, int curPage, int pageSize) {

		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String userId = (String) map.get("userId");
		String companyId = (String) map.get("companyId");

		List<Map<String, Object>> list = getWeekList(year, month, companyId);

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TMP.ORG_CODE,\n");
		sql.append("       TMP.ORG_NAME,\n");
		sql.append("       TVMG3.GROUP_CODE,\n");
		sql.append("       TVMG3.GROUP_NAME,\n");
		sql.append("       SUM(TMP.QUOTA_AMT) QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			if (i != list.size() - 1) {
				sql.append("       SUM(TMP.W" + i + ") W" + i + ",\n");
			} else {
				sql.append("       SUM(TMP.W" + i + ") W" + i + "\n");
			}
		}
		sql.append("  FROM (SELECT TVQ.ORG_CODE,\n");
		sql.append("               TVQ.ORG_NAME,\n");
		sql.append("               TVQ.GROUP_CODE,\n");
		sql.append("               TVQ.GROUP_NAME,\n");
		sql.append("               TVQ.QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mapGet = (Map<String, Object>) list.get(i);
			if (i != list.size() - 1) {
				sql.append("               DECODE(TVQ.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVQ.QUOTA_AMT, 0) W" + i + ",\n");
			} else {
				sql.append("               DECODE(TVQ.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVQ.QUOTA_AMT, 0) W" + i + "\n");
			}
		}
		sql.append("          FROM TMP_VS_QUOTA TVQ\n");
		sql.append("         WHERE TVQ.USER_ID = " + userId + "\n");
		sql.append("           AND TVQ.DEALER_CODE IS NULL) TMP,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append(" WHERE TMP.GROUP_CODE = TVMG1.GROUP_CODE\n");
		sql.append("   AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql
				.append(" GROUP BY TMP.ORG_CODE, TMP.ORG_NAME, TVMG3.GROUP_CODE, TVMG3.GROUP_NAME");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getAreaQuotaImportTempList",
				pageSize, curPage);
		return ps;
	}

	/**
	 * 区域配额删除
	 *
	 * @param map
	 * @return
	 */
	public int deleteOrgQuota(Map<String, Object> map) {
		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String areaId = (String) map.get("areaId");
		String userId = (String) map.get("userId") ;

		StringBuffer sql = new StringBuffer();

		// 删除配额明细
		sql.append("DELETE FROM TT_VS_QUOTA_DETAIL TVQD\n");
		sql.append(" WHERE TVQD.QUOTA_ID IN (SELECT TVQ.QUOTA_ID\n");
		sql.append("                           FROM TT_VS_QUOTA TVQ\n");
		sql.append("                          WHERE TVQ.QUOTA_YEAR = " + year + "\n");
		sql.append("                            AND TVQ.QUOTA_MONTH = " + month + "\n");
		sql.append("                            AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("                            AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_01 + "\n");
		sql.append("                            AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		//sql.append("                            AND TVQ.CREATE_BY = " + userId + "\n");
		sql.append("                            AND TVQ.DEALER_ID IS NULL)");

		delete(sql.toString(), null);

		sql = new StringBuffer();

		sql.append("DELETE FROM TT_VS_QUOTA TVQ\n");
		sql.append(" WHERE TVQ.QUOTA_YEAR = " + year + "\n");
		sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
		sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_01 + "\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		//sql.append("   AND TVQ.CREATE_BY = " + userId + "\n");
		sql.append("   AND TVQ.DEALER_ID IS NULL");

		return delete(sql.toString(), null);
	}

	public List<Map<String, Object>> getTmpVsQuotaInsertList(
			Map<String, Object> map) {
		String userId = (String) map.get("userId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVQ.AREA_ID,\n");
		sql.append("       TVQ.QUOTA_YEAR,\n");
		sql.append("       TVQ.QUOTA_MONTH,\n");
		sql.append("       TVQ.QUOTA_WEEK,\n");
		sql.append("       TOR.ORG_ID,\n");
		sql.append("       TVMG.GROUP_ID,\n");
		sql.append("       TVQ.QUOTA_AMT\n");
		sql
				.append("  FROM TMP_VS_QUOTA TVQ, TM_ORG TOR, TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append(" WHERE TVQ.ORG_CODE = TOR.ORG_CODE\n");
		sql.append("   AND TVQ.GROUP_CODE = TVMG.GROUP_CODE\n");
		sql.append("   AND TVQ.USER_ID = " + userId);

		List<Map<String, Object>> results = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getTmpVsQuotaInsertList");
		return results;
	}

	/**
	 * 获得经销商配额临时表查询列表
	 *
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getDealerQuotaImportTempList(
			Map<String, Object> map, int curPage, int pageSize) {

		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String userId = (String) map.get("userId");
		String companyId = (String) map.get("companyId");

		List<Map<String, Object>> list = getWeekList(year, month, companyId);

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TMP.DEALER_CODE,\n");
		sql.append("       TMP.DEALER_NAME,\n");
		sql.append("       TVMG3.GROUP_CODE,\n");
		sql.append("       TVMG3.GROUP_NAME,\n");
		sql.append("       SUM(TMP.QUOTA_AMT) QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			if (i != list.size() - 1) {
				sql.append("       SUM(TMP.W" + i + ") W" + i + ",\n");
			} else {
				sql.append("       SUM(TMP.W" + i + ") W" + i + "\n");
			}
		}
		sql.append("  FROM (SELECT TVQ.DEALER_CODE,\n");
		sql.append("               TVQ.DEALER_NAME,\n");
		sql.append("               TVQ.GROUP_CODE,\n");
		sql.append("               TVQ.GROUP_NAME,\n");
		sql.append("               TVQ.QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mapGet = (Map<String, Object>) list.get(i);
			if (i != list.size() - 1) {
				sql.append("               DECODE(TVQ.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVQ.QUOTA_AMT, 0) W" + i + ",\n");
			} else {
				sql.append("               DECODE(TVQ.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVQ.QUOTA_AMT, 0) W" + i + "\n");
			}
		}
		sql.append("          FROM TMP_VS_QUOTA TVQ\n");
		sql.append("         WHERE TVQ.USER_ID = " + userId + "\n");
		sql.append("           AND TVQ.ORG_CODE IS NULL) TMP,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append(" WHERE TMP.GROUP_CODE = TVMG1.GROUP_CODE\n");
		sql.append("   AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql
				.append(" GROUP BY TMP.DEALER_CODE, TMP.DEALER_NAME, TVMG3.GROUP_CODE, TVMG3.GROUP_NAME");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getDealerQuotaImportTempList",
				pageSize, curPage);
		return ps;
	}

	/**
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDealerTmpVsQuotaInsertList(
			Map<String, Object> map) {
		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String areaId = (String) map.get("areaId");
		String userId = (String) map.get("userId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVQ.AREA_ID,\n");
		sql.append("       TVQ.QUOTA_YEAR,\n");
		sql.append("       TVQ.QUOTA_MONTH,\n");
		sql.append("       TVQ.QUOTA_WEEK,\n");
		sql.append("       TD.DEALER_ID,\n");
		sql.append("       TVMG.GROUP_ID,\n");
		sql.append("       TVQ.QUOTA_AMT\n");
		sql
				.append("  FROM TMP_VS_QUOTA TVQ, TM_DEALER TD, TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append(" WHERE TVQ.DEALER_CODE = TD.DEALER_CODE\n");
		sql.append("   AND TVQ.GROUP_CODE = TVMG.GROUP_CODE\n");
		sql.append("   AND TVQ.QUOTA_YEAR = " + year + "\n");
		sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
		sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("   AND TVQ.USER_ID = " + userId);

		List<Map<String, Object>> results = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getTmpVsQuotaInsertList");
		return results;
	}

	/**
	 * 经销商配额删除
	 *
	 * @param map
	 * @return
	 */
	public int deleteDealerQuota(Map<String, Object> map) {
		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String areaId = (String) map.get("areaId");
		String dealerSql = (String) map.get("dealerSql");

		StringBuffer sql = new StringBuffer();

		sql.append("DELETE FROM TT_VS_QUOTA TVQ\n");
		sql.append(" WHERE TVQ.QUOTA_YEAR = " + year + "\n");
		sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
		sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		sql.append("   AND TVQ.ORG_ID IS NULL");
		sql.append("   AND " + dealerSql + "\n");

		return delete(sql.toString(), null);
	}

	/**
	 * 经销商配额删除
	 *
	 * @param map
	 * @return
	 */
	public int delDealerQuota(Map<String, Object> map) {
		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String areaId = (String) map.get("areaId");
		String userId = (String) map.get("userId") ;
		String orgId = (String) map.get("orgId") ;

		StringBuffer sql = new StringBuffer();

		sql.append("DELETE FROM TT_VS_QUOTA TVQ\n");
		sql.append(" WHERE TVQ.QUOTA_YEAR = " + year + "\n");
		sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
		sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		//sql.append("   AND TVQ.CREATE_BY = " + userId + "\n");
		sql.append("   AND exists (select 1 from tm_dealer_org_relation tdor where tdor.org_id = " + orgId + " and tdor.dealer_id = tvq.dealer_id)\n");
		sql.append("   AND TVQ.ORG_ID IS NULL");

		return delete(sql.toString(), null);
	}

	public int delDealerQuotaDtl(Map<String, Object> map) {
		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String areaId = (String) map.get("areaId");
		String userId = (String) map.get("userId") ;
		String orgId = (String) map.get("orgId") ;

		StringBuffer sql = new StringBuffer();

		sql.append("DELETE FROM TT_VS_QUOTA_DETAIL TVQD\n");
		sql.append(" WHERE TVQD.QUOTA_ID IN (SELECT TVQ.QUOTA_ID\n");
		sql.append("                           FROM TT_VS_QUOTA TVQ\n");
		sql.append("                          WHERE TVQ.QUOTA_YEAR = " + year + "\n");
		sql.append("                            AND TVQ.QUOTA_MONTH = " + month + "\n");
		sql.append("                            AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("                            AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		sql.append("and exists (SELECT 1\n");
		sql.append("  FROM TM_DEALER TD\n");
		sql.append("  where td.dealer_id = tvq.dealer_id\n");
		sql.append(" START WITH DEALER_ID IN\n");
		sql.append("            (SELECT TDOR.DEALER_ID\n");
		sql.append("               FROM TM_DEALER_ORG_RELATION TDOR\n");
		sql.append("              WHERE TDOR.ORG_ID IN\n");
		sql.append("                    (SELECT ORG_ID\n");
		sql.append("                       FROM TM_ORG TMO\n");
		sql.append("                      START WITH TMO.ORG_ID = " + orgId + "\n");
		sql.append("                     CONNECT BY PRIOR TMO.ORG_ID = TMO.PARENT_ORG_ID))\n");
		sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D)\n");
		//sql.append("                            AND TVQ.CREATE_BY = " + userId + "\n");
		//sql.append("   							AND exists (select 1 from tm_dealer_org_relation tdor where tdor.org_id = " + orgId + " and tdor.dealer_id = tvq.dealer_id)\n");
		sql.append("                            AND TVQ.org_id IS NULL)");

		return delete(sql.toString(), null);
	}

	/**
	 * 区域配额确认下发按业务范围分组查询
	 *
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getAreaQuotaConfirmQueryList(
			Map<String, Object> map, int curPage, int pageSize,Long orgId) {

		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT TBA.AREA_NAME,\n");
		sql.append("                TVQ.AREA_ID,\n");
		sql.append("                TVQ.QUOTA_YEAR,\n");
		sql.append("                TVQ.QUOTA_MONTH,\n");
		sql
				.append("                TVQ.QUOTA_YEAR || '-' || TVQ.QUOTA_MONTH QUOTA_DATE,\n");
		sql.append("                TVQ.QUOTA_TYPE\n");
		sql.append("  FROM TT_VS_QUOTA TVQ, TM_BUSINESS_AREA TBA\n");
		sql.append(" WHERE TVQ.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
		sql.append("   AND TVQ.ORG_ID NOT IN (" + orgId + ")\n");

		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_01 + "");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getAreaQuotaConfirmQueryList",
				pageSize, curPage);
		return ps;
	}

	/**
	 * 区域配额确认下发查询
	 *
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getAreaQuotaIssueQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		String year = (String) map.get("quotaYear");
		String month = (String) map.get("quotaMonth");
		String areaId = (String) map.get("areaId");
		String companyId = (String) map.get("companyId");

		List<Map<String, Object>> list = getWeekList(year, month, companyId);

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TOR.ORG_ID,\n");
		sql.append("       TOR.ORG_CODE,\n");
		sql.append("       TOR.ORG_NAME,\n");
		sql.append("       TVMG3.GROUP_ID,\n");
		sql.append("       TVMG3.GROUP_CODE,\n");
		sql.append("       TVMG3.GROUP_NAME,\n");
		sql.append("       SUM(TMP.QUOTA_AMT) QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			if (i != list.size() - 1) {
				sql.append("       SUM(TMP.W" + i + ") W" + i + ",\n");
			} else {
				sql.append("       SUM(TMP.W" + i + ") W" + i + "\n");
			}
		}
		sql.append("  FROM (SELECT TVQ.ORG_ID,\n");
		sql.append("               TVQD.GROUP_ID,\n");
		sql.append("               TVQD.QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mapGet = (Map<String, Object>) list.get(i);
			if (i != list.size() - 1) {
				sql.append("               DECODE(TVQ.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVQD.QUOTA_AMT, 0) W" + i + ",\n");
			} else {
				sql.append("               DECODE(TVQ.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVQD.QUOTA_AMT, 0) W" + i + "\n");
			}
		}
		sql.append("          FROM TT_VS_QUOTA TVQ, TT_VS_QUOTA_DETAIL TVQD\n");
		sql.append("         WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("           AND TVQ.QUOTA_YEAR = " + year + "\n");
		sql.append("           AND TVQ.QUOTA_MONTH = " + month + "\n");
		sql.append("           AND TVQ.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("           AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01
				+ "\n");
		sql.append("           AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_01
				+ ") TMP,\n");
		sql.append("       TM_ORG TOR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append(" WHERE TMP.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("   AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TMP.ORG_ID = TOR.ORG_ID\n");
		sql.append(" GROUP BY TOR.ORG_ID,\n");
		sql.append("          TOR.ORG_CODE,\n");
		sql.append("          TOR.ORG_NAME,\n");
		sql.append("          TVMG3.GROUP_ID,\n");
		sql.append("          TVMG3.GROUP_CODE,\n");
		sql.append("          TVMG3.GROUP_NAME");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getAreaQuotaIssueQueryList",
				pageSize, curPage);
		return ps;
	}

	/**
	 * 获得区域配额下发车系总量
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAreaQuotaIssueSerieAmount(
			Map<String, Object> map) {
		String quotaYear = (String) map.get("quotaYear");
		String quotaMonth = (String) map.get("quotaMonth");
		String areaId = (String) map.get("areaId");
		String orgId = (String) map.get("orgId");
		String groupId = (String) map.get("groupId");
		String companyId = (String) map.get("companyId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TOR.ORG_CODE,\n");
		sql.append("       TOR.ORG_NAME,\n");
		sql.append("       TVMG3.GROUP_CODE SERIE_CODE,\n");
		sql.append("       TVMG3.GROUP_NAME SERIE_NAME,\n");
		sql.append("       SUM(TVQD.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
		sql.append("       TM_ORG                 TOR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("   AND TVQ.ORG_ID = TOR.ORG_ID\n");
		sql.append("   AND TVQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("   AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVQ.QUOTA_YEAR = " + quotaYear + "\n");
		sql.append("   AND TVQ.QUOTA_MONTH = " + quotaMonth + "\n");
		sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_01 + "\n");
		sql.append("   AND TOR.ORG_ID = " + orgId + "\n");
		sql.append("   AND TVMG3.GROUP_ID = " + groupId + "\n");
		sql
				.append(" GROUP BY TOR.ORG_CODE, TOR.ORG_NAME, TVMG3.GROUP_CODE, TVMG3.GROUP_NAME");

		List<Map<String, Object>> results = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getAreaQuotaIssueSerieAmount");
		return results;
	}

	/**
	 * 获得区域配额下发明细列表
	 *
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getAreaQuotaIssueDetailList(
			Map<String, Object> map, int curPage, int pageSize) {

		String quotaYear = (String) map.get("quotaYear");
		String quotaMonth = (String) map.get("quotaMonth");
		String areaId = (String) map.get("areaId");
		String orgId = (String) map.get("orgId");
		String groupId = (String) map.get("groupId");
		String companyId = (String) map.get("companyId");

		StringBuffer sql = new StringBuffer();

		sql
				.append("SELECT TVQ.QUOTA_YEAR || '-' || TVQ.QUOTA_MONTH || '-' || TVQ.QUOTA_WEEK QUOTA_DATE,\n");
		sql.append("       TVMG1.GROUP_CODE,\n");
		sql.append("       TVMG1.GROUP_NAME,\n");
		sql.append("       TVQD.QUOTA_AMT\n");
		sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("   AND TVQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("   AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVQ.QUOTA_YEAR = " + quotaYear + "\n");
		sql.append("   AND TVQ.QUOTA_MONTH = " + quotaMonth + "\n");
		sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_01 + "\n");
		sql.append("   AND TVQ.ORG_ID = " + orgId + "\n");
		sql.append("   AND TVMG3.GROUP_ID = " + groupId + "");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getAreaQuotaIssueDetailList",
				pageSize, curPage);
		return ps;
	}

	/**
	 * 经销商配额确认下发按业务范围分组查询
	 *
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getDealerQuotaConfirmQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		StringBuffer sql = new StringBuffer();
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");

		sql.append("SELECT DISTINCT TBA.AREA_NAME,\n");
		sql.append("                TVQ.AREA_ID,\n");
		sql.append("                TVQ.QUOTA_YEAR,\n");
		sql.append("                TVQ.QUOTA_MONTH,\n");
		sql
				.append("                TVQ.QUOTA_YEAR || '-' || TVQ.QUOTA_MONTH QUOTA_DATE,\n");
		sql.append("                TVQ.QUOTA_TYPE\n");
		sql
				.append("  FROM TT_VS_QUOTA TVQ, TM_BUSINESS_AREA TBA, TM_DEALER TD\n");
		sql.append(" WHERE TVQ.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TVQ.DEALER_ID = TD.DEALER_ID\n");
		sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		sql.append("   AND " + dealerSql + "");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getDealerQuotaConfirmQueryList",
				pageSize, curPage);
		return ps;
	}

	/**
	 * 经销商配额确认下发查询
	 *
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getDealerQuotaIssueQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		String year = (String) map.get("quotaYear");
		String month = (String) map.get("quotaMonth");
		String areaId = (String) map.get("areaId");
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");
		String orgId=(String)map.get("orgId");
		List<Map<String, Object>> list = getWeekList(year, month, companyId);

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TDA.DEALER_ID,\n");
		sql.append("       TDA.DEALER_CODE,\n");
		sql.append("       TDA.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("       TVMG3.GROUP_ID,\n");
		sql.append("       TVMG3.GROUP_CODE,\n");
		sql.append("       TVMG3.GROUP_NAME,\n");
		sql.append("       SUM(TMP.QUOTA_AMT) QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			if (i != list.size() - 1) {
				sql.append("       SUM(TMP.W" + i + ") W" + i + ",\n");
			} else {
				sql.append("       SUM(TMP.W" + i + ") W" + i + "\n");
			}
		}
		sql.append("  FROM (SELECT TVQ.DEALER_ID,\n");
		sql.append("               TVQD.GROUP_ID,\n");
		sql.append("               TVQD.QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mapGet = (Map<String, Object>) list.get(i);
			if (i != list.size() - 1) {
				sql.append("               DECODE(TVQ.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVQD.QUOTA_AMT, 0) W" + i + ",\n");
			} else {
				sql.append("               DECODE(TVQ.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVQD.QUOTA_AMT, 0) W" + i + "\n");
			}
		}
		sql
				.append("          FROM TT_VS_QUOTA TVQ, TT_VS_QUOTA_DETAIL TVQD, TM_DEALER TD1\n");
		sql.append("         WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("           AND TVQ.DEALER_ID = TD1.DEALER_ID\n");
		sql.append("           AND TVQ.QUOTA_YEAR = " + year + "\n");
		sql.append("           AND TVQ.QUOTA_MONTH = " + month + "\n");
		sql.append("           AND TVQ.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TVQ.AREA_ID = " + areaId + "\n");
		//sql.append("           AND " + dealerSql);
		//start
		sql.append("  AND td1.dealer_id in\n ");
		sql.append("  (SELECT TDOR.DEALER_ID\n");
		sql.append("  FROM TM_DEALER_ORG_RELATION TDOR\n");
		sql.append("   WHERE TDOR.ORG_ID IN\n");
		sql.append("    (SELECT ORG_ID\n");
		sql.append("   FROM TM_ORG TMO\n");
		sql.append(" WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+"\n");
		sql.append("  START WITH TMO.ORG_ID = "+orgId+"\n");
		sql.append("    CONNECT BY PRIOR TMO.ORG_ID = TMO.PARENT_ORG_ID))");
		//end
		sql.append("           AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01
				+ "\n");
		sql.append("           AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02
				+ ") TMP,\n");
		sql.append("       TM_DEALER TDA,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append(" WHERE TMP.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("   AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TMP.DEALER_ID = TDA.DEALER_ID\n");
		sql.append(" GROUP BY TDA.DEALER_ID,\n");
		sql.append("          TDA.DEALER_CODE,\n");
		sql.append("          TDA.DEALER_SHORTNAME,\n");
		sql.append("          TVMG3.GROUP_ID,\n");
		sql.append("          TVMG3.GROUP_CODE,\n");
		sql.append("          TVMG3.GROUP_NAME");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getDealerQuotaIssueQueryList",
				pageSize, curPage);
		return ps;
	}

	/**
	 * 获得经销商配额下发车系总量
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDealerQuotaIssueSerieAmount(
			Map<String, Object> map) {
		String quotaYear = (String) map.get("quotaYear");
		String quotaMonth = (String) map.get("quotaMonth");
		String areaId = (String) map.get("areaId");
		String dealerId = (String) map.get("dealerId");
		String groupId = (String) map.get("groupId");
		String companyId = (String) map.get("companyId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TDA.DEALER_CODE,\n");
		sql.append("       TDA.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("       TVMG3.GROUP_CODE SERIE_CODE,\n");
		sql.append("       TVMG3.GROUP_NAME SERIE_NAME,\n");
		sql.append("       SUM(TVQD.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
		sql.append("       TM_DEALER              TDA,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("   AND TVQ.DEALER_ID = TDA.DEALER_ID\n");
		sql.append("   AND TVQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("   AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVQ.QUOTA_YEAR = " + quotaYear + "\n");
		sql.append("   AND TVQ.QUOTA_MONTH = " + quotaMonth + "\n");
		sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		sql.append("   AND TDA.DEALER_ID = " + dealerId + "\n");
		sql.append("   AND TVMG3.GROUP_ID = " + groupId + "\n");
		sql.append(" GROUP BY TDA.DEALER_CODE,\n");
		sql.append("          TDA.DEALER_SHORTNAME,\n");
		sql.append("          TVMG3.GROUP_CODE,\n");
		sql.append("          TVMG3.GROUP_NAME");

		List<Map<String, Object>> results = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getDealerQuotaIssueSerieAmount");
		return results;
	}

	/**
	 * 获得区域配额下发明细列表
	 *
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getDealerQuotaIssueDetailList(
			Map<String, Object> map, int curPage, int pageSize) {

		String quotaYear = (String) map.get("quotaYear");
		String quotaMonth = (String) map.get("quotaMonth");
		String areaId = (String) map.get("areaId");
		String dealerId = (String) map.get("dealerId");
		String groupId = (String) map.get("groupId");
		String companyId = (String) map.get("companyId");

		StringBuffer sql = new StringBuffer();

		sql
				.append("SELECT TVQ.QUOTA_YEAR || '-' || TVQ.QUOTA_MONTH || '-' || TVQ.QUOTA_WEEK QUOTA_DATE,\n");
		sql.append("       TVMG1.GROUP_CODE,\n");
		sql.append("       TVMG1.GROUP_NAME,\n");
		sql.append("       TVQD.QUOTA_AMT\n");
		sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("   AND TVQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("   AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TVQ.QUOTA_YEAR = " + quotaYear + "\n");
		sql.append("   AND TVQ.QUOTA_MONTH = " + quotaMonth + "\n");
		sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		sql.append("   AND TVQ.DEALER_ID = " + dealerId + "\n");
		sql.append("   AND TVMG3.GROUP_ID = " + groupId + "");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getDealerQuotaIssueDetailList",
				pageSize, curPage);
		return ps;
	}

	/**
	 * 经销商配额下发
	 *
	 * @param map
	 * @return
	 */
	public int dealerQuotaIssue(Map<String, Object> map) {
		String quotaYear = (String) map.get("quotaYear");
		String quotaMonth = (String) map.get("quotaMonth");
		String areaId = (String) map.get("areaId");
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");

		List<Object> params = new ArrayList<Object>();


		StringBuffer sql = new StringBuffer();

		sql.append("UPDATE TT_VS_QUOTA TVQ\n");
		sql.append("   SET TVQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");

		sql.append(" WHERE TVQ.QUOTA_YEAR = ?\n");
		sql.append("   AND TVQ.QUOTA_MONTH = ?\n");
		sql.append("   AND TVQ.COMPANY_ID = ?\n");
		sql.append("   AND TVQ.AREA_ID = ?\n");
		sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		sql.append("   AND TVQ.DEALER_ID IN (" + dealerSql + ")");

		params.add(quotaYear);
		params.add(quotaMonth);
		params.add(companyId);
		params.add(areaId);

		return update(sql.toString(), params);
	}

	/**
	 * 配额汇总查询(OEM)
	 *
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getOemQuotaTotalQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		String quotaType = (String) map.get("quotaType");
		String dateType = (String) map.get("dateType");
		String year1 = (String) map.get("year1");
		String year2 = (String) map.get("year2");
		String month = (String) map.get("month");
		String week = (String) map.get("week");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String orgCode = (String) map.get("orgCode");
		String dealerCode = (String) map.get("dealerCode");
		String orgSql = (String) map.get("orgSql");
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		String orgId=(String)map.get("orgId");
		StringBuffer sql = new StringBuffer();

		if (quotaType.equals(Constant.QUOTA_TYPE_01.toString())) {

			sql.append("SELECT TVMG.GROUP_CODE,\n");
			sql.append("       TVMG.GROUP_NAME,\n");
			sql.append("       TVMG2.GROUP_CODE MODEL_CODE,\n");
			sql.append("       TVMG2.GROUP_NAME MODEL_NAME,\n");
			sql.append("       SUM(TVQD.QUOTA_AMT) QUOTA_AMT\n");
			sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
			sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
			sql.append("       TM_ORG                 TOR\n");
			sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
			sql.append("   AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
			sql.append("   AND TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
			sql.append("   AND TVQ.ORG_ID = TOR.ORG_ID\n");
			sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
			sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_01
					+ "\n");
			sql
					.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_02
							+ "\n");
			sql.append("   AND " + orgSql + "");

			if (dateType.equals("1")) {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year1 + "\n");
				sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
			} else {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year2 + "\n");
				sql.append("   AND TVQ.QUOTA_WEEK = " + week + "\n");
			}
			sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
			if (!areaId.equals("")) {
				sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
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
			if (!orgCode.equals("")) {
				String[] array = orgCode.split(",");
				sql.append("   AND TOR.ORG_CODE IN (");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append(")\n");
			}
			sql.append(" GROUP BY TVMG.GROUP_CODE,\n");
			sql.append("          TVMG.GROUP_NAME,\n");
			sql.append("          TVMG2.GROUP_CODE,\n");
			sql.append("          TVMG2.GROUP_NAME");
			sql.append("  ORDER BY TVMG2.GROUP_NAME");
			//start
		}else if(quotaType.equals(Constant.QUOTA_TYPE_03.toString())){
			sql.append("SELECT TVMG.GROUP_CODE,\n");
			sql.append("       TVMG.GROUP_NAME,\n");
			sql.append("       TVMG2.GROUP_CODE MODEL_CODE,\n");
			sql.append("       TVMG2.GROUP_NAME MODEL_NAME,\n");
			sql.append("       SUM(TVQD.QUOTA_AMT) QUOTA_AMT\n");
			sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
			sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
			sql.append("       TM_ORG                 TOR\n");
			sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
			sql.append("   AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
			sql.append("   AND TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
			sql.append("   AND TVQ.ORG_ID = TOR.ORG_ID\n");
			sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
			sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_03
					+ "\n");
			sql
					.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_02
							+ "\n");
			sql.append("   AND " + orgSql + "");
			if (dateType.equals("1")) {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year1 + "\n");
				sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
			} else {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year2 + "\n");
				sql.append("   AND TVQ.QUOTA_WEEK = " + week + "\n");
			}
			sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
			if (!areaId.equals("")) {
				sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
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
			if (!orgCode.equals("")) {
				String[] array = orgCode.split(",");
				sql.append("   AND TOR.ORG_CODE IN (");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append(")\n");
			}
			sql.append(" GROUP BY TVMG.GROUP_CODE,\n");
			sql.append("          TVMG.GROUP_NAME,\n");
			sql.append("          TVMG2.GROUP_CODE,\n");
			sql.append("          TVMG2.GROUP_NAME");
			sql.append("  ORDER BY TVMG2.GROUP_NAME");
			//end
		} else {

			sql.append("SELECT TVMG.GROUP_CODE,\n");
			sql.append("       TVMG.GROUP_NAME,\n");
			sql.append("       TVMG2.GROUP_CODE MODEL_CODE,\n");
			sql.append("       TVMG2.GROUP_NAME MODEL_NAME,\n");
			sql.append("       SUM(TVQD.QUOTA_AMT) QUOTA_AMT\n");
			sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
			sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
			sql.append("       TM_DEALER              TDA\n");
			sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
			sql.append("   AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
			sql.append("   AND TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
			sql.append("   AND TVQ.DEALER_ID = TDA.DEALER_ID\n");
			sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
			sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02
					+ "\n");
			sql
					.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_02
							+ "\n");
			//sql.append("   AND " + dealerSql + "");
			//start
			sql.append("  AND tda.dealer_id in\n ");
			sql.append("  (SELECT TDOR.DEALER_ID\n");
			sql.append("  FROM TM_DEALER_ORG_RELATION TDOR\n");
			sql.append("   WHERE TDOR.ORG_ID IN\n");
			sql.append("    (SELECT ORG_ID\n");
			sql.append("   FROM TM_ORG TMO\n");
			sql.append(" WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+"\n");
			sql.append("  START WITH TMO.ORG_ID = "+orgId+"\n");
			sql.append("    CONNECT BY PRIOR TMO.ORG_ID = TMO.PARENT_ORG_ID))");
			//end
			if (dateType.equals("1")) {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year1 + "\n");
				sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
			} else {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year2 + "\n");
				sql.append("   AND TVQ.QUOTA_WEEK = " + week + "\n");
			}
			sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
			if (!areaId.equals("")) {
				sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
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
			if (!dealerCode.equals("")) {
				String[] array = dealerCode.split(",");
				sql.append(" AND TDA.DEALER_CODE IN (");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append(")\n");
			}
			sql.append(" GROUP BY TVMG.GROUP_CODE,\n");
			sql.append("          TVMG.GROUP_NAME,\n");
			sql.append("          TVMG2.GROUP_CODE,\n");
			sql.append("          TVMG2.GROUP_NAME");
			sql.append("		ORDER BY TVMG2.GROUP_NAME");
		}

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.OrderQueryDao.getOemQuotaTotalQueryList",
				pageSize, curPage);
		return ps;
	}

	/**
	 * 配额明细查询(OEM)
	 *
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getOemQuotaDetailQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		String quotaType = (String) map.get("quotaType");
		String dateType = (String) map.get("dateType");
		String year1 = (String) map.get("year1");
		String year2 = (String) map.get("year2");
		String month = (String) map.get("month");
		String week = (String) map.get("week");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String orgCode = (String) map.get("orgCode");
		String dealerCode = (String) map.get("dealerCode");
		String orgSql = (String) map.get("orgSql");
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");
		String orgId=(String)map.get("orgId");
		StringBuffer sql = new StringBuffer();

		if (quotaType.equals(Constant.QUOTA_TYPE_01.toString())) {

			sql.append("SELECT TVQ.QUOTA_YEAR || '-' || TVQ.QUOTA_MONTH || '-' || TVQ.QUOTA_WEEK QUOTA_DATE,\n");
			sql.append("       TOR.ORG_CODE,\n");
			sql.append("       TOR.ORG_NAME,\n");
			sql.append("       TVMG.GROUP_CODE,\n");
			sql.append("       TVMG.GROUP_NAME,\n");
			sql.append("       TVQD.QUOTA_AMT\n");
			sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
			sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
			sql.append("       TM_ORG                 TOR\n");
			sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
			sql.append("   AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
			sql.append("   AND TVQ.ORG_ID = TOR.ORG_ID\n");
			sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
			sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_01 + "\n");
			sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
			sql.append("   AND " + orgSql + "");
			if (dateType.equals("1")) {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year1 + "\n");
				sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
			} else {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year2 + "\n");
				sql.append("   AND TVQ.QUOTA_WEEK = " + week + "\n");
			}
			sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
			if (!areaId.equals("")) {
				sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
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
			if (!orgCode.equals("")) {
				String[] array = orgCode.split(",");
				sql.append("   AND TOR.ORG_CODE IN (");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append(")\n");
			}
			sql.append(" ORDER BY TOR.ORG_CODE, TVQ.QUOTA_WEEK, TVMG.GROUP_CODE");
			//start
		} else if(quotaType.equals(Constant.QUOTA_TYPE_03.toString())){
			sql.append("SELECT TVQ.QUOTA_YEAR || '-' || TVQ.QUOTA_MONTH || '-' || TVQ.QUOTA_WEEK QUOTA_DATE,\n");
			sql.append("       TOR.ORG_CODE,\n");
			sql.append("       TOR.ORG_NAME,\n");
			sql.append("       TVMG.GROUP_CODE,\n");
			sql.append("       TVMG.GROUP_NAME,\n");
			sql.append("       TVQD.QUOTA_AMT\n");
			sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
			sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
			sql.append("       TM_ORG                 TOR\n");
			sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
			sql.append("   AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
			sql.append("   AND TVQ.ORG_ID = TOR.ORG_ID\n");
			sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
			sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_03 + "\n");
			sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
			sql.append("   AND " + orgSql + "");
			if (dateType.equals("1")) {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year1 + "\n");
				sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
			} else {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year2 + "\n");
				sql.append("   AND TVQ.QUOTA_WEEK = " + week + "\n");
			}
			sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
			if (!areaId.equals("")) {
				sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
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
			if (!orgCode.equals("")) {
				String[] array = orgCode.split(",");
				sql.append("   AND TOR.ORG_CODE IN (");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append(")\n");
			}
			sql.append(" ORDER BY TOR.ORG_CODE, TVQ.QUOTA_WEEK, TVMG.GROUP_CODE");
			//end
		}else {
			sql.append("SELECT TVQ.QUOTA_YEAR || '-' || TVQ.QUOTA_MONTH || '-' || TVQ.QUOTA_WEEK QUOTA_DATE,\n");
			sql.append("       TDA.DEALER_CODE ORG_CODE,\n");
			sql.append("       TDA.DEALER_SHORTNAME ORG_NAME,\n");
			sql.append("       TVMG.GROUP_CODE,\n");
			sql.append("       TVMG.GROUP_NAME,\n");
			sql.append("       TVQD.QUOTA_AMT\n");
			sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
			sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
			sql.append("       TM_DEALER              TDA\n");
			sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
			sql.append("   AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
			sql.append("   AND TVQ.DEALER_ID = TDA.DEALER_ID\n");
			sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
			sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
			sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		//	sql.append("   AND " + dealerSql + "");
			//start
			sql.append("  AND tda.dealer_id in\n ");
			sql.append("  (SELECT TDOR.DEALER_ID\n");
			sql.append("  FROM TM_DEALER_ORG_RELATION TDOR\n");
			sql.append("   WHERE TDOR.ORG_ID IN\n");
			sql.append("    (SELECT ORG_ID\n");
			sql.append("   FROM TM_ORG TMO\n");
			sql.append(" WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+"\n");
			sql.append("  START WITH TMO.ORG_ID = "+orgId+"\n");
			sql.append("    CONNECT BY PRIOR TMO.ORG_ID = TMO.PARENT_ORG_ID))");
			//end
			if (dateType.equals("1")) {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year1 + "\n");
				sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
			} else {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year2 + "\n");
				sql.append("   AND TVQ.QUOTA_WEEK = " + week + "\n");
			}
			sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
			if (!areaId.equals("")) {
				sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
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
			if (!dealerCode.equals("")) {
				String[] array = dealerCode.split(",");
				sql.append(" and   TDA.DEALER_CODE IN (");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append(")\n");
			}
			sql.append(" ORDER BY TDA.DEALER_CODE, TVQ.QUOTA_WEEK, TVMG.GROUP_CODE");
		}

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderQueryDao.getOemQuotaDetailQueryList", pageSize, curPage);
		return ps;
	}

	/**
	 * 配额汇总查询导出列表(OEM)
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getOemQuotaTotalExportList(
			Map<String, Object> map) {

		String quotaType = (String) map.get("quotaType");
		String dateType = (String) map.get("dateType");
		String year1 = (String) map.get("year1");
		String year2 = (String) map.get("year2");
		String month = (String) map.get("month");
		String week = (String) map.get("week");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String orgCode = (String) map.get("orgCode");
		String dealerCode = (String) map.get("dealerCode");
		String orgSql = (String) map.get("orgSql");
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");

		StringBuffer sql = new StringBuffer();

		if (quotaType.equals(Constant.QUOTA_TYPE_01.toString())) {

			sql.append("SELECT TVMG.GROUP_CODE,\n");
			sql.append("       TVMG.GROUP_NAME,\n");
			sql.append("       TVMG2.GROUP_CODE MODEL_CODE,\n");
			sql.append("       TVMG2.GROUP_NAME MODEL_NAME,\n");
			sql.append("       SUM(TVQD.QUOTA_AMT) QUOTA_AMT\n");
			sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
			sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
			sql.append("       TM_ORG                 TOR\n");
			sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
			sql.append("   AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
			sql.append("   AND TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
			sql.append("   AND TVQ.ORG_ID = TOR.ORG_ID\n");
			sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
			sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_01
					+ "\n");
			sql
					.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_02
							+ "\n");
			sql.append("   AND " + orgSql + "");
			if (dateType.equals("1")) {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year1 + "\n");
				sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
			} else {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year2 + "\n");
				sql.append("   AND TVQ.QUOTA_WEEK = " + week + "\n");
			}
			sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
			if (!areaId.equals("")) {
				sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
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
			if (!orgCode.equals("")) {
				String[] array = orgCode.split(",");
				sql.append("   AND TOR.ORG_CODE IN (");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append(")\n");
			}
			sql.append(" GROUP BY TVMG.GROUP_CODE,\n");
			sql.append("          TVMG.GROUP_NAME,\n");
			sql.append("          TVMG2.GROUP_CODE,\n");
			sql.append("          TVMG2.GROUP_NAME");
			sql.append("		  ORDER BY TVMG2.GROUP_NAME");
			//start
		}else if(quotaType.equals(Constant.QUOTA_TYPE_03.toString())){
			sql.append("SELECT TVMG.GROUP_CODE,\n");
			sql.append("       TVMG.GROUP_NAME,\n");
			sql.append("       TVMG2.GROUP_CODE MODEL_CODE,\n");
			sql.append("       TVMG2.GROUP_NAME MODEL_NAME,\n");
			sql.append("       SUM(TVQD.QUOTA_AMT) QUOTA_AMT\n");
			sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
			sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
			sql.append("       TM_ORG                 TOR\n");
			sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
			sql.append("   AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
			sql.append("   AND TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
			sql.append("   AND TVQ.ORG_ID = TOR.ORG_ID\n");
			sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
			sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_03
					+ "\n");
			sql
					.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_02
							+ "\n");
			sql.append("   AND " + orgSql + "");
			if (dateType.equals("1")) {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year1 + "\n");
				sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
			} else {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year2 + "\n");
				sql.append("   AND TVQ.QUOTA_WEEK = " + week + "\n");
			}
			sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
			if (!areaId.equals("")) {
				sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
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
			if (!orgCode.equals("")) {
				String[] array = orgCode.split(",");
				sql.append("   AND TOR.ORG_CODE IN (");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append(")\n");
			}
			sql.append(" GROUP BY TVMG.GROUP_CODE,\n");
			sql.append("          TVMG.GROUP_NAME,\n");
			sql.append("          TVMG2.GROUP_CODE,\n");
			sql.append("          TVMG2.GROUP_NAME");
			sql.append("		  ORDER BY TVMG2.GROUP_NAME");
			//end
		} else {

			sql.append("SELECT TVMG.GROUP_CODE,\n");
			sql.append("       TVMG.GROUP_NAME,\n");
			sql.append("       TVMG2.GROUP_CODE MODEL_CODE,\n");
			sql.append("       TVMG2.GROUP_NAME MODEL_NAME,\n");
			sql.append("       SUM(TVQD.QUOTA_AMT) QUOTA_AMT\n");
			sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
			sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
			sql.append("       TM_DEALER              TDA\n");
			sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
			sql.append("   AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
			sql.append("   AND TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
			sql.append("   AND TVQ.DEALER_ID = TDA.DEALER_ID\n");
			sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
			sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02
					+ "\n");
			sql
					.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_02
							+ "\n");
			sql.append("   AND " + dealerSql + "");
			if (dateType.equals("1")) {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year1 + "\n");
				sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
			} else {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year2 + "\n");
				sql.append("   AND TVQ.QUOTA_WEEK = " + week + "\n");
			}
			sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
			if (!areaId.equals("")) {
				sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
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
			if (!dealerCode.equals("")) {
				String[] array = dealerCode.split(",");
				sql.append(" AND  TDA.DEALER_CODE IN (");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append(")\n");
			}
			sql.append(" GROUP BY TVMG.GROUP_CODE,\n");
			sql.append("          TVMG.GROUP_NAME,\n");
			sql.append("          TVMG2.GROUP_CODE,\n");
			sql.append("          TVMG2.GROUP_NAME");
			sql.append("		  ORDER BY TVMG2.GROUP_NAME");
		}

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderQueryDao.getOemQuotaTotalExportList");
		return list;
	}

	/**
	 * 配额明细查询导出列表(OEM)
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getOemQuotaDetailExportList(
			Map<String, Object> map) {

		String quotaType = (String) map.get("quotaType");
		String dateType = (String) map.get("dateType");
		String year1 = (String) map.get("year1");
		String year2 = (String) map.get("year2");
		String month = (String) map.get("month");
		String week = (String) map.get("week");
		String areaId = (String) map.get("areaId");
		String groupCode = (String) map.get("groupCode");
		String orgCode = (String) map.get("orgCode");
		String dealerCode = (String) map.get("dealerCode");
		String orgSql = (String) map.get("orgSql");
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");

		StringBuffer sql = new StringBuffer();

		if (quotaType.equals(Constant.QUOTA_TYPE_01.toString())) {

			sql
					.append("SELECT TVQ.QUOTA_YEAR || '-' || TVQ.QUOTA_MONTH || '-' || TVQ.QUOTA_WEEK QUOTA_DATE,\n");
			sql.append("       TOR.ORG_CODE,\n");
			sql.append("       TOR.ORG_NAME,\n");
			sql.append("       TVMG.GROUP_CODE,\n");
			sql.append("       TVMG.GROUP_NAME,\n");
			sql.append("       TVQD.QUOTA_AMT\n");
			sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
			sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
			sql.append("       TM_ORG                 TOR\n");
			sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
			sql.append("   AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
			sql.append("   AND TVQ.ORG_ID = TOR.ORG_ID\n");
			sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
			sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_01
					+ "\n");
			sql
					.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_02
							+ "\n");
			sql.append("   AND " + orgSql + "");
			if (dateType.equals("1")) {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year1 + "\n");
				sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
			} else {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year2 + "\n");
				sql.append("   AND TVQ.QUOTA_WEEK = " + week + "\n");
			}
			sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
			if (!areaId.equals("")) {
				sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
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
			if (!orgCode.equals("")) {
				String[] array = orgCode.split(",");
				sql.append("   AND TOR.ORG_CODE IN (");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append(")\n");
			}
			sql
					.append(" ORDER BY TOR.ORG_CODE, TVQ.QUOTA_WEEK, TVMG.GROUP_CODE");
			//start
		} else if(quotaType.equals(Constant.QUOTA_TYPE_03.toString())){
			sql
			.append("SELECT TVQ.QUOTA_YEAR || '-' || TVQ.QUOTA_MONTH || '-' || TVQ.QUOTA_WEEK QUOTA_DATE,\n");
	sql.append("       TOR.ORG_CODE,\n");
	sql.append("       TOR.ORG_NAME,\n");
	sql.append("       TVMG.GROUP_CODE,\n");
	sql.append("       TVMG.GROUP_NAME,\n");
	sql.append("       TVQD.QUOTA_AMT\n");
	sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
	sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
	sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
	sql.append("       TM_ORG                 TOR\n");
	sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
	sql.append("   AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
	sql.append("   AND TVQ.ORG_ID = TOR.ORG_ID\n");
	sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
	sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_03
			+ "\n");
	sql
			.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_02
					+ "\n");
	sql.append("   AND " + orgSql + "");
	if (dateType.equals("1")) {
		sql.append("   AND TVQ.QUOTA_YEAR = " + year1 + "\n");
		sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
	} else {
		sql.append("   AND TVQ.QUOTA_YEAR = " + year2 + "\n");
		sql.append("   AND TVQ.QUOTA_WEEK = " + week + "\n");
	}
	sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
	if (!areaId.equals("")) {
		sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
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
	if (!orgCode.equals("")) {
		String[] array = orgCode.split(",");
		sql.append("   AND TOR.ORG_CODE IN (");
		for (int i = 0; i < array.length; i++) {
			sql.append("'" + array[i] + "'");
			if (i != array.length - 1) {
				sql.append(",");
			}
		}
		sql.append(")\n");
	}
	sql
			.append(" ORDER BY TOR.ORG_CODE, TVQ.QUOTA_WEEK, TVMG.GROUP_CODE");
			//end
		}else {

			sql
					.append("SELECT TVQ.QUOTA_YEAR || '-' || TVQ.QUOTA_MONTH || '-' || TVQ.QUOTA_WEEK QUOTA_DATE,\n");
			sql.append("       TDA.DEALER_CODE ORG_CODE,\n");
			sql.append("       TDA.DEALER_SHORTNAME ORG_NAME,\n");
			sql.append("       TVMG.GROUP_CODE,\n");
			sql.append("       TVMG.GROUP_NAME,\n");
			sql.append("       TVQD.QUOTA_AMT\n");
			sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
			sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
			sql.append("       TM_DEALER              TDA\n");
			sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
			sql.append("   AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
			sql.append("   AND TVQ.DEALER_ID = TDA.DEALER_ID\n");
			sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
			sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02
					+ "\n");
			sql
					.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_02
							+ "\n");
			sql.append("   AND " + dealerSql + "");
			if (dateType.equals("1")) {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year1 + "\n");
				sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
			} else {
				sql.append("   AND TVQ.QUOTA_YEAR = " + year2 + "\n");
				sql.append("   AND TVQ.QUOTA_WEEK = " + week + "\n");
			}
			sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
			if (!areaId.equals("")) {
				sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
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
			if (!dealerCode.equals("")) {
				String[] array = dealerCode.split(",");
				sql.append("  AND TDA.DEALER_CODE IN (");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append(")\n");
			}
			sql
					.append(" ORDER BY TDA.DEALER_CODE, TVQ.QUOTA_WEEK, TVMG.GROUP_CODE");
		}

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderQueryDao.getOemQuotaDetailExportList");
		return list;
	}

	/**
	 * 配额明细查询(DEALER)
	 *
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getDealerQuotaQueryList(Map<String, Object> map, int curPage, int pageSize) {
		String dateType = (String) map.get("dateType");
		String year1 = (String) map.get("year1");
		String year2 = (String) map.get("year2");
		String month = (String) map.get("month");
		String week = (String) map.get("week");
		String groupCode = (String) map.get("groupCode");
		String dealerId = (String) map.get("dealerId");
		String areaIds = (String) map.get("areaIds");
		String isPassZero = map.get("isPassZero").toString() ;

		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT TVQ.QUOTA_YEAR || '-' || TVQ.QUOTA_MONTH || '-' || TVQ.QUOTA_WEEK QUOTA_DATE,\n");
		sql.append("       TDA.DEALER_CODE ORG_CODE,\n");
		sql.append("       TDA.DEALER_SHORTNAME ORG_NAME,\n");
		sql.append("       TVMG.GROUP_CODE,\n");
		sql.append("       TVMG.GROUP_NAME,\n");
		sql.append("       TVQD.QUOTA_AMT\n");
		sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("       TM_DEALER              TDA\n");
		sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("   AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVQ.DEALER_ID = TDA.DEALER_ID\n");
		sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("   AND TDA.DEALER_ID IN (" + dealerId + ")\n");
		sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
		if (dateType.equals("1")) {
			sql.append("   AND TVQ.QUOTA_YEAR = " + year1 + "\n");
			sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
		} else {
			sql.append("   AND TVQ.QUOTA_YEAR = " + year2 + "\n");
			sql.append("   AND TVQ.QUOTA_WEEK = " + week + "\n");
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

		if(!CommonUtils.isNullString(isPassZero) && Constant.IF_TYPE_YES.toString().equals(isPassZero)) {
			sql.append("and TVQD.QUOTA_AMT <> 0\n");
		}

		sql.append(" ORDER BY TDA.DEALER_CODE, TVQ.QUOTA_WEEK, TVMG.GROUP_CODE");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.OrderQueryDao.getDealerQuotaQueryList",
				pageSize, curPage);
		return ps;
	}

	/**
	 * 配额查询导出列表(OEM)
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDealerQuotaExportList(
			Map<String, Object> map) {

		String dateType = (String) map.get("dateType");
		String year1 = (String) map.get("year1");
		String year2 = (String) map.get("year2");
		String month = (String) map.get("month");
		String week = (String) map.get("week");
		String groupCode = (String) map.get("groupCode");
		String dealerId = (String) map.get("dealerId");
		String areaIds = (String) map.get("areaIds");

		StringBuffer sql = new StringBuffer();

		sql
				.append("SELECT TVQ.QUOTA_YEAR || '-' || TVQ.QUOTA_MONTH || '-' || TVQ.QUOTA_WEEK QUOTA_DATE,\n");
		sql.append("       TDA.DEALER_CODE ORG_CODE,\n");
		sql.append("       TDA.DEALER_SHORTNAME ORG_NAME,\n");
		sql.append("       TVMG.GROUP_CODE,\n");
		sql.append("       TVMG.GROUP_NAME,\n");
		sql.append("       TVQD.QUOTA_AMT\n");
		sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("       TM_DEALER              TDA\n");
		sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("   AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVQ.DEALER_ID = TDA.DEALER_ID\n");
		sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("   AND TDA.DEALER_ID IN (" + dealerId + ")\n");
		sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
		if (dateType.equals("1")) {
			sql.append("   AND TVQ.QUOTA_YEAR = " + year1 + "\n");
			sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
		} else {
			sql.append("   AND TVQ.QUOTA_YEAR = " + year2 + "\n");
			sql.append("   AND TVQ.QUOTA_WEEK = " + week + "\n");
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
		sql
				.append(" ORDER BY TDA.DEALER_CODE, TVQ.QUOTA_WEEK, TVMG.GROUP_CODE");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderQueryDao.getDealerQuotaExportList");
		return list;
	}

	public int insertAreaQuota(Map<String, Object> map) {
		String companyId = (String) map.get("companyId");
		String userId = (String) map.get("userId");

		StringBuffer sql = new StringBuffer();

		sql.append("MERGE INTO TT_VS_QUOTA A\n");
		sql.append("USING (SELECT TVQ.AREA_ID,\n");
		sql.append("              TVQ.QUOTA_YEAR,\n");
		sql.append("              TVQ.QUOTA_MONTH,\n");
		sql.append("              TVQ.QUOTA_WEEK,\n");
		sql.append("              TOR.ORG_ID\n");
		sql.append("         FROM TMP_VS_QUOTA TVQ, TM_ORG TOR\n");
		sql.append("        WHERE TVQ.ORG_CODE = TOR.ORG_CODE\n");
		sql.append("          AND TVQ.USER_ID = " + userId + "\n");
		sql.append("        GROUP BY TVQ.AREA_ID,\n");
		sql.append("                 TVQ.QUOTA_YEAR,\n");
		sql.append("                 TVQ.QUOTA_MONTH,\n");
		sql.append("                 TVQ.QUOTA_WEEK,\n");
		sql.append("                 TOR.ORG_ID) C\n");
		sql
				.append("ON (A.AREA_ID = C.AREA_ID AND A.QUOTA_YEAR = C.QUOTA_YEAR AND A.QUOTA_MONTH = C.QUOTA_MONTH AND A.QUOTA_WEEK = C.QUOTA_WEEK AND A.ORG_ID = C.ORG_ID)\n");
		sql.append("WHEN  MATCHED THEN UPDATE SET A.CREATE_DATE=A.CREATE_DATE\n");
		sql.append("WHEN NOT MATCHED THEN\n");
		sql.append("  INSERT\n");
		sql.append("    (QUOTA_ID,\n");
		sql.append("     COMPANY_ID,\n");
		sql.append("     AREA_ID,\n");
		sql.append("     QUOTA_TYPE,\n");
		sql.append("     QUOTA_YEAR,\n");
		sql.append("     QUOTA_MONTH,\n");
		sql.append("     QUOTA_WEEK,\n");
		sql.append("     ORG_ID,\n");
		sql.append("     STATUS,\n");
		sql.append("     CREATE_BY,\n");
		sql.append("     CREATE_DATE)\n");
		sql.append("  VALUES\n");
		sql.append("    (F_GETID(),\n");
		sql.append("     " + companyId + ",\n");
		sql.append("     C.AREA_ID,\n");
		sql.append("     " + Constant.QUOTA_TYPE_01 + ",\n");
		sql.append("     C.QUOTA_YEAR,\n");
		sql.append("     C.QUOTA_MONTH,\n");
		sql.append("     C.QUOTA_WEEK,\n");
		sql.append("     C.ORG_ID,\n");
		sql.append("     " + Constant.QUOTA_STATUS_01 + ",\n");
		sql.append("     " + userId + ",\n");
		sql.append("     SYSDATE)");

		// 保存TT_VS_QUOTA
		/*
		 * sql.append("INSERT INTO TT_VS_QUOTA\n"); sql.append(" (QUOTA_ID,\n");
		 * sql.append(" COMPANY_ID,\n"); sql.append(" AREA_ID,\n"); sql.append("
		 * QUOTA_TYPE,\n"); sql.append(" QUOTA_YEAR,\n"); sql.append("
		 * QUOTA_MONTH,\n"); sql.append(" QUOTA_WEEK,\n"); sql.append("
		 * ORG_ID,\n"); sql.append(" STATUS,\n"); sql.append(" CREATE_BY,\n");
		 * sql.append(" CREATE_DATE)\n"); sql.append(" SELECT F_GETID()
		 * QUOTA_ID,\n"); sql.append(" " + companyId + " COMPANY_ID,\n");
		 * sql.append(" C.AREA_ID,\n"); sql.append(" C.QUOTA_TYPE,\n");
		 * sql.append(" C.QUOTA_YEAR,\n"); sql.append(" C.QUOTA_MONTH,\n");
		 * sql.append(" C.QUOTA_WEEK,\n"); sql.append(" C.ORG_ID,\n");
		 * sql.append(" C.QUOTA_STATUS,\n"); sql.append(" C.CREATE_BY,\n");
		 * sql.append(" C.CREATE_DATE\n"); sql.append(" FROM (SELECT
		 * A.AREA_ID,\n"); sql.append(" " + Constant.QUOTA_TYPE_01 + "
		 * QUOTA_TYPE,\n"); sql.append(" A.QUOTA_YEAR,\n"); sql.append("
		 * A.QUOTA_MONTH,\n"); sql.append(" A.QUOTA_WEEK,\n"); sql.append("
		 * A.ORG_ID,\n"); sql.append(" " + Constant.QUOTA_STATUS_01 + "
		 * QUOTA_STATUS,\n"); sql.append(" " + userId + " CREATE_BY,\n");
		 * sql.append(" SYSDATE CREATE_DATE,\n"); sql.append(" B.AREA_ID
		 * BAREA_ID,\n"); sql.append(" B.QUOTA_YEAR BQUOTA_YEAR,\n");
		 * sql.append(" B.QUOTA_MONTH BQUOTA_MONTH,\n"); sql.append("
		 * B.QUOTA_WEEK BQUOTA_WEEK,\n"); sql.append(" B.ORG_ID BORG_ID\n");
		 * sql.append(" FROM (SELECT TVQ.AREA_ID,\n"); sql.append("
		 * TVQ.QUOTA_YEAR,\n"); sql.append(" TVQ.QUOTA_MONTH,\n"); sql.append("
		 * TVQ.QUOTA_WEEK,\n"); sql.append(" TOR.ORG_ID\n"); sql.append(" FROM
		 * TMP_VS_QUOTA TVQ, TM_ORG TOR\n"); sql.append(" WHERE TVQ.USER_ID = " +
		 * userId + "\n"); sql.append(" AND TVQ.ORG_CODE = TOR.ORG_CODE\n");
		 * sql.append(" GROUP BY TVQ.AREA_ID,\n"); sql.append("
		 * TVQ.QUOTA_YEAR,\n"); sql.append(" TVQ.QUOTA_MONTH,\n"); sql.append("
		 * TVQ.QUOTA_WEEK,\n"); sql.append(" TOR.ORG_ID) A,\n"); sql.append("
		 * TT_VS_QUOTA B\n"); sql.append(" WHERE A.AREA_ID = B.AREA_ID(+)\n");
		 * sql.append(" AND A.QUOTA_YEAR = B.QUOTA_YEAR(+)\n"); sql.append(" AND
		 * A.QUOTA_MONTH = B.QUOTA_MONTH(+)\n"); sql.append(" AND A.QUOTA_WEEK =
		 * B.QUOTA_WEEK(+)\n"); sql.append(" AND A.ORG_ID = B.ORG_ID(+)) C\n");
		 * sql.append(" WHERE C.BAREA_ID IS NULL\n"); sql.append(" AND
		 * C.BQUOTA_YEAR IS NULL\n"); sql.append(" AND C.BQUOTA_MONTH IS
		 * NULL\n"); sql.append(" AND C.BQUOTA_WEEK IS NULL\n"); sql.append("
		 * AND C.BORG_ID IS NULL");
		 */

		return update(sql.toString(), null);
	}

	public int insertAreaQuotaDetail(Map<String, Object> map) {
		String userId = (String) map.get("userId");

		StringBuffer sql = new StringBuffer();

		// 保存TT_VS_QUOTA_DETAIL
		sql.append("MERGE INTO TT_VS_QUOTA_DETAIL A\n");
		sql.append("USING (SELECT TVQ.QUOTA_ID   QUOTA_ID,\n");
		sql.append("              TVMG.GROUP_ID  GROUP_ID,\n");
		sql.append("              TPVP.QUOTA_AMT QUOTA_AMT\n");
		sql.append("         FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("              TMP_VS_QUOTA           TPVP,\n");
		sql.append("              TM_ORG                 TOR,\n");
		sql.append("              TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append("        WHERE TVQ.AREA_ID = TPVP.AREA_ID\n");
		sql.append("          AND TVQ.ORG_ID = TOR.ORG_ID\n");
		sql.append("          AND TOR.ORG_CODE = TPVP.ORG_CODE\n");
		sql.append("          AND TVMG.GROUP_CODE = TPVP.GROUP_CODE\n");
		sql.append("          AND TVQ.QUOTA_YEAR = TPVP.QUOTA_YEAR\n");
		sql.append("          AND TVQ.QUOTA_MONTH = TPVP.QUOTA_MONTH\n");
		sql.append("          AND TVQ.QUOTA_WEEK = TPVP.QUOTA_WEEK\n");
		sql.append("          AND TPVP.USER_ID = " + userId + ") C\n");
		sql
				.append("ON (A.QUOTA_ID = C.QUOTA_ID AND A.GROUP_ID = C.GROUP_ID)\n");
		sql.append("WHEN MATCHED THEN\n");
		sql.append("  UPDATE\n");
		sql.append("     SET QUOTA_AMT   = C.QUOTA_AMT,\n");
		sql.append("         UPDATE_DATE = SYSDATE,\n");
		sql.append("         UPDATE_BY   = " + userId + "\n");
		sql.append("WHEN NOT MATCHED THEN\n");
		sql.append("  INSERT\n");
		sql
				.append("    (DETAIL_ID, QUOTA_ID, GROUP_ID, QUOTA_AMT, CREATE_DATE, CREATE_BY)\n");
		sql.append("  VALUES\n");
		sql
				.append("    (F_GETID(), C.QUOTA_ID, C.GROUP_ID, C.QUOTA_AMT, SYSDATE, "
						+ userId + ")");

		return update(sql.toString(), null);
	}

	public int insertDealerQuota(Map<String, Object> map) {
		String companyId = (String) map.get("companyId");
		String userId = (String) map.get("userId");

		StringBuffer sql = new StringBuffer();

		// 保存TT_VS_QUOTA
		sql.append("MERGE INTO TT_VS_QUOTA A\n");
		sql.append("USING (SELECT TVQ.AREA_ID,\n");
		sql.append("              TVQ.QUOTA_YEAR,\n");
		sql.append("              TVQ.QUOTA_MONTH,\n");
		sql.append("              TVQ.QUOTA_WEEK,\n");
		sql.append("              TDA.DEALER_ID\n");
		sql.append("         FROM TMP_VS_QUOTA TVQ, TM_DEALER TDA\n");
		sql.append("        WHERE TVQ.DEALER_CODE = TDA.DEALER_CODE\n");
		sql.append("          AND TVQ.USER_ID = " + userId + "\n");
		sql.append("        GROUP BY TVQ.AREA_ID,\n");
		sql.append("                 TVQ.QUOTA_YEAR,\n");
		sql.append("                 TVQ.QUOTA_MONTH,\n");
		sql.append("                 TVQ.QUOTA_WEEK,\n");
		sql.append("                 TDA.DEALER_ID) C\n");
		sql
				.append("ON (A.AREA_ID = C.AREA_ID AND A.QUOTA_YEAR = C.QUOTA_YEAR AND A.QUOTA_MONTH = C.QUOTA_MONTH AND A.QUOTA_WEEK = C.QUOTA_WEEK AND A.DEALER_ID = C.DEALER_ID)\n");
		sql.append("WHEN MATCHED THEN\n");
		sql.append("  UPDATE\n");
		sql.append("     SET A.COMPANY_ID=A.COMPANY_ID\n");
		sql.append("WHEN NOT MATCHED THEN\n");
		sql.append("  INSERT\n");
		sql.append("    (QUOTA_ID,\n");
		sql.append("     COMPANY_ID,\n");
		sql.append("     AREA_ID,\n");
		sql.append("     QUOTA_TYPE,\n");
		sql.append("     QUOTA_YEAR,\n");
		sql.append("     QUOTA_MONTH,\n");
		sql.append("     QUOTA_WEEK,\n");
		sql.append("     DEALER_ID,\n");
		sql.append("     STATUS,\n");
		sql.append("     CREATE_BY,\n");
		sql.append("     CREATE_DATE)\n");
		sql.append("  VALUES\n");
		sql.append("    (F_GETID(),\n");
		sql.append("     " + companyId + ",\n");
		sql.append("     C.AREA_ID,\n");
		sql.append("     " + Constant.QUOTA_TYPE_02 + ",\n");
		sql.append("     C.QUOTA_YEAR,\n");
		sql.append("     C.QUOTA_MONTH,\n");
		sql.append("     C.QUOTA_WEEK,\n");
		sql.append("     C.DEALER_ID,\n");
		sql.append("     " + Constant.QUOTA_STATUS_01 + ",\n");
		sql.append("     " + userId + ",\n");
		sql.append("     SYSDATE)");

		return update(sql.toString(), null);
	}

	public int insertDealerQuotaDetail(Map<String, Object> map) {
		String userId = (String) map.get("userId");

		StringBuffer sql = new StringBuffer();

		// 保存TT_VS_QUOTA_DETAIL
		sql.append("MERGE INTO TT_VS_QUOTA_DETAIL A\n");
		sql.append("USING (SELECT TVQ.QUOTA_ID   QUOTA_ID,\n");
		sql.append("              TVMG.GROUP_ID  GROUP_ID,\n");
		sql.append("              TPVP.QUOTA_AMT QUOTA_AMT\n");
		sql.append("         FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("              TMP_VS_QUOTA           TPVP,\n");
		sql.append("              TM_DEALER              TDA,\n");
		sql.append("              TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append("        WHERE TVQ.AREA_ID = TPVP.AREA_ID\n");
		sql.append("          AND TVQ.DEALER_ID = TDA.DEALER_ID\n");
		sql.append("          AND TDA.DEALER_CODE = TPVP.DEALER_CODE\n");
		sql.append("          AND TVMG.GROUP_CODE = TPVP.GROUP_CODE\n");
		sql.append("          AND TVQ.QUOTA_YEAR = TPVP.QUOTA_YEAR\n");
		sql.append("          AND TVQ.QUOTA_MONTH = TPVP.QUOTA_MONTH\n");
		sql.append("          AND TVQ.QUOTA_WEEK = TPVP.QUOTA_WEEK\n");
		sql.append("          AND TPVP.USER_ID = " + userId + ") C\n");
		sql.append("ON (A.QUOTA_ID = C.QUOTA_ID AND A.GROUP_ID = C.GROUP_ID)\n");
		sql.append("WHEN MATCHED THEN\n");
		sql.append("  UPDATE\n");
		sql.append("     SET QUOTA_AMT   = C.QUOTA_AMT,\n");
		sql.append("         UPDATE_DATE = SYSDATE,\n");
		sql.append("         UPDATE_BY   = " + userId + "\n");
		sql.append("WHEN NOT MATCHED THEN\n");
		sql.append("  INSERT\n");
		sql.append("    (DETAIL_ID, QUOTA_ID, GROUP_ID, QUOTA_AMT, CREATE_DATE, CREATE_BY)\n");
		sql.append("  VALUES\n");
		sql.append("    (F_GETID(), C.QUOTA_ID, C.GROUP_ID, C.QUOTA_AMT, SYSDATE, " + userId + ")");

		return update(sql.toString(), null);
	}

	public List<Map<String, Object>> getDealerQuotaIssuedList(
			Map<String, Object> map) {

		String year = (String) map.get("quotaYear");
		String month = (String) map.get("quotaMonth");
		String areaId = (String) map.get("areaId");
		String dealerSql = (String) map.get("dealerSql");
		String companyId = (String) map.get("companyId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVQ.QUOTA_ID\n");
		sql.append("  FROM TT_VS_QUOTA TVQ\n");
		sql.append(" WHERE TVQ.QUOTA_YEAR = " + year + "\n");
		sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
		sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_02 + "\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("   AND exists (" + dealerSql + ")\n");

		List<Map<String, Object>> results = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getDealerQuotaIssuedList");
		return results;
	}

	public List<Map<String, Object>> getAreaDealerNotEqualList(
			Map<String, Object> map) {

		String areaId = (String) map.get("areaId");
		String quotaYear = (String) map.get("quotaYear");
		String quotaMonth = (String) map.get("quotaMonth");
		String orgId = (String) map.get("orgId");
		String companyId = (String) map.get("companyId");
		String dealerSql = (String) map.get("dealerSql");
		String userId = (String) map.get("userId");

		StringBuffer sql = new StringBuffer("\n");

		/*sql
				.append("SELECT C.GROUP_CODE, C.GROUP_NAME, C.QUOTA_WEEK, C.AQUOTA_AMT, C.BQUOTA_AMT\n");
		sql
				.append("  FROM (SELECT NVL(A.GROUP_CODE, B.GROUP_CODE) GROUP_CODE,\n");
		sql
				.append("               NVL(A.GROUP_NAME, B.GROUP_NAME) GROUP_NAME,\n");
		sql
				.append("               NVL(A.QUOTA_WEEK, B.QUOTA_WEEK) QUOTA_WEEK,\n");
		sql.append("               NVL(A.QUOTA_AMT, 0) AQUOTA_AMT,\n");
		sql.append("               NVL(B.QUOTA_AMT, 0) BQUOTA_AMT\n");
		sql.append("          FROM (SELECT TVMG.GROUP_CODE,\n");
		sql.append("                       TVMG.GROUP_NAME,\n");
		sql.append("                       TVQ.QUOTA_WEEK,\n");
		sql.append("                       TVQD.QUOTA_AMT\n");
		sql.append("                  FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("                       TT_VS_QUOTA_DETAIL     TVQD,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append("                 WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("                   AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("                   AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("                   AND TVQ.QUOTA_YEAR = " + quotaYear
				+ "\n");
		sql.append("                   AND TVQ.QUOTA_MONTH = " + quotaMonth
				+ "\n");
		sql.append("                   AND TVQ.ORG_ID = " + orgId + "\n");
		sql.append("                   AND TVQ.STATUS = "
				+ Constant.QUOTA_STATUS_02 + "\n");
		sql.append("                   AND TVQ.COMPANY_ID = " + companyId
				+ ") A\n");
		sql.append("          FULL OUTER JOIN (SELECT TVQ.GROUP_CODE,\n");
		sql.append("                                 TVQ.GROUP_NAME,\n");
		sql.append("                                 TVQ.QUOTA_WEEK,\n");
		sql
				.append("                                 SUM(TVQ.QUOTA_AMT) QUOTA_AMT\n");
		sql
				.append("                            FROM TMP_VS_QUOTA TVQ, TM_DEALER TDA\n");
		sql
				.append("                           WHERE TVQ.DEALER_CODE = TDA.DEALER_CODE\n");
		sql.append("                             AND TVQ.AREA_ID = " + areaId
				+ "\n");
		sql.append("                             AND TVQ.QUOTA_YEAR = "
				+ quotaYear + "\n");
		sql.append("                             AND TVQ.QUOTA_MONTH = "
				+ quotaMonth + "\n");
		sql.append("                             AND TDA.DEALER_ID IN ("
				+ dealerSql + ")\n");
		sql.append("                             AND TVQ.USER_ID = " + userId
				+ "\n");
		sql.append("                           GROUP BY TVQ.GROUP_CODE,\n");
		sql.append("                                    TVQ.GROUP_NAME,\n");
		sql
				.append("                                    TVQ.QUOTA_WEEK) B ON (A.GROUP_CODE =\n");
		sql
				.append("                                                         B.GROUP_CODE AND A.QUOTA_WEEK = B.QUOTA_WEEK)) C\n");
		sql.append(" WHERE C.AQUOTA_AMT <> C.BQUOTA_AMT");*/


		sql.append("WITH A AS\n");
		sql.append(" (SELECT TVMG.GROUP_CODE, TVMG.GROUP_NAME, TVQ.QUOTA_WEEK, TVQD.QUOTA_AMT\n");
		sql.append("    FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("         TT_VS_QUOTA_DETAIL     TVQD,\n");
		sql.append("         TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append("   WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("     AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("     AND TVQ.AREA_ID = ").append(areaId).append("\n");
		sql.append("     AND TVQ.QUOTA_YEAR = ").append(quotaYear).append("\n");
		sql.append("     AND TVQ.QUOTA_MONTH = ").append(quotaMonth).append("\n");
		sql.append("     AND TVQ.ORG_ID = ").append(orgId).append("\n");
		sql.append("     AND TVQ.COMPANY_ID = ").append(companyId).append("\n") ;
		sql.append("     AND TVQ.STATUS = ").append(Constant.QUOTA_STATUS_02).append("),\n");
		sql.append("B AS\n");
		sql.append(" (SELECT TVQ.GROUP_CODE,\n");
		sql.append("         TVQ.GROUP_NAME,\n");
		sql.append("         TVQ.QUOTA_WEEK,\n");
		sql.append("         SUM(TVQ.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("    FROM TMP_VS_QUOTA TVQ, TM_DEALER TDA\n");
		sql.append("   WHERE TVQ.DEALER_CODE = TDA.DEALER_CODE\n");
		sql.append("     AND TVQ.AREA_ID = ").append(areaId).append("\n");
		sql.append("     AND TVQ.QUOTA_YEAR = ").append(quotaYear).append("\n");
		sql.append("     AND TVQ.QUOTA_MONTH = ").append(quotaMonth).append("\n");
		sql.append("     AND exists (").append(dealerSql).append(")\n");
		sql.append("     AND TVQ.USER_ID = ").append(userId).append("\n");
		sql.append("   GROUP BY TVQ.GROUP_CODE, TVQ.GROUP_NAME, TVQ.QUOTA_WEEK)\n");
		sql.append("SELECT nvl(a.GROUP_CODE, b.GROUP_CODE) GROUP_CODE,\n");
		sql.append("       nvl(a.GROUP_NAME, b.GROUP_NAME) GROUP_NAME,\n");
		sql.append("       nvl(a.QUOTA_WEEK, b.QUOTA_WEEK) QUOTA_WEEK,\n");
		sql.append("       nvl(a.QUOTA_AMT, 0) AQUOTA_AMT,\n");
		sql.append("       nvl(b.QUOTA_AMT, 0) BQUOTA_AMT\n");
		sql.append("  from a\n");
		sql.append("  FULL OUTER JOIN b\n");
		sql.append("    ON (A.GROUP_CODE = B.GROUP_CODE AND A.QUOTA_WEEK = B.QUOTA_WEEK)\n");
		sql.append(" where nvl(a.QUOTA_AMT,0) <> nvl(b.QUOTA_AMT,0)\n");


		List<Map<String, Object>> results = pageQuery(sql.toString(), null, super.getFunName());
		return results;
	}
	public List<Map<String, Object>> getAreaNotEqualList(
			Map<String, Object> map) {

		String areaId = (String) map.get("areaId");
		String quotaYear = (String) map.get("quotaYear");
		String quotaMonth = (String) map.get("quotaMonth");
		String orgId = (String) map.get("orgId");
		String companyId = (String) map.get("companyId");
		String userId = (String) map.get("userId");

		StringBuffer sql = new StringBuffer("\n");

		sql.append("WITH A AS\n");
		sql.append(" (SELECT TVMG.GROUP_CODE, TVMG.GROUP_NAME, TVQ.QUOTA_WEEK, TVQD.QUOTA_AMT\n");
		sql.append("    FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("         TT_VS_QUOTA_DETAIL     TVQD,\n");
		sql.append("         TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append("   WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("     AND TVQD.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("     AND TVQ.AREA_ID = "+areaId+"\n");
		sql.append("     AND TVQ.QUOTA_Year = "+quotaYear+"\n");
		sql.append("     AND TVQ.QUOTA_MONTH = "+quotaMonth+"\n");
		sql.append("     AND TVQ.ORG_ID = "+orgId+"\n");
		sql.append("     AND TVQ.COMPANY_ID = "+companyId+"\n");
		sql.append("     AND TVQ.STATUS = "+Constant.QUOTA_STATUS_02+"),\n");
		sql.append("B AS\n");
		sql.append(" (SELECT TVQ.GROUP_CODE,\n");
		sql.append("         TVQ.GROUP_NAME,\n");
		sql.append("         TVQ.QUOTA_WEEK,\n");
		sql.append("         SUM(TVQ.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("    FROM TMP_VS_QUOTA TVQ\n");
		sql.append("   WHERE TVQ.AREA_ID = "+areaId+"\n");
		sql.append("     AND TVQ.QUOTA_YEAR = "+quotaYear+"\n");
		sql.append("     AND TVQ.QUOTA_MONTH = "+quotaMonth+"\n");
		sql.append("     AND TVQ.org_code in \n");
		sql.append("   (\n");
		sql.append("   select org_code from tm_org x where x.parent_org_id="+orgId+"\n");
		sql.append("\n");
		sql.append("   )\n");
		sql.append("     AND TVQ.USER_ID = "+userId+"\n");
		sql.append("   GROUP BY TVQ.GROUP_CODE, TVQ.GROUP_NAME, TVQ.QUOTA_WEEK)\n");
		sql.append("\n");
		sql.append("\n");
		sql.append("SELECT nvl(a.GROUP_CODE, b.GROUP_CODE) GROUP_CODE,\n");
		sql.append("       nvl(a.GROUP_NAME, b.GROUP_NAME) GROUP_NAME,\n");
		sql.append("       nvl(a.QUOTA_WEEK, b.QUOTA_WEEK) QUOTA_WEEK,\n");
		sql.append("       nvl(a.QUOTA_AMT, 0) AQUOTA_AMT,\n");
		sql.append("       nvl(b.QUOTA_AMT, 0) BQUOTA_AMT\n");
		sql.append("  from a\n");
		sql.append("  FULL OUTER JOIN b\n");
		sql.append("    ON (A.GROUP_CODE = B.GROUP_CODE AND A.QUOTA_WEEK = B.QUOTA_WEEK)\n");
		sql.append(" where nvl(a.QUOTA_AMT, 0) <> nvl(b.QUOTA_AMT, 0)");



		List<Map<String, Object>> results = pageQuery(sql.toString(), null, super.getFunName());
		return results;
	}
	/**
	 * 配额导入校验区域是否存在
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> quotaImportCheckOrg(Map<String, Object> map) {
		String userId = map.get("userId").toString();
		String companyId = map.get("companyId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_QUOTA p\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and p.USER_ID =" + userId + "\n");
		sql.append("   and p.DEALER_CODE is null\n");
		sql.append("   and not exists (select 1\n");
		sql.append("          from tm_org org\n");
		sql.append("         where org.ORG_CODE = p.ORG_CODE\n");
		sql.append("           and org.COMPANY_ID = " + companyId + "\n");// 多公司过滤
		sql.append("           and org.STATUS = " + Constant.STATUS_ENABLE
				+ "\n");// 有效
		sql.append("           and org.ORG_TYPE = " + Constant.ORG_TYPE_OEM
				+ "\n");// 车厂组织
		sql.append("           and org.DUTY_TYPE = "
				+ Constant.DUTY_TYPE_LARGEREGION + ")\n");// 大区
		sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/**
	 * 配额导入校验经销商是否存在
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> quotaImportCheckDealer(Map<String, Object> map) {
		String userId = map.get("userId").toString();
		String orgId = map.get("orgId").toString();
		String companyId = map.get("companyId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_QUOTA p\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and p.USER_ID =" + userId + "\n");
		sql.append("   and p.DEALER_CODE is not null\n");
		sql.append("   and not exists (select 1\n");
		sql.append("          from TM_DEALER dealer, TM_DEALER_ORG_RELATION r\n");
		sql.append("         where dealer.DEALER_CODE = p.DEALER_CODE\n");
		sql.append("           and dealer.DEALER_ID = r.DEALER_ID\n");
		//sql.append("           and r.ORG_ID = " + orgId + "\n");

		sql.append("           AND r.org_id IN\n");
		sql.append("               (SELECT org_id\n");
		sql.append("                  FROM tm_org o\n");
		sql.append("                 START WITH org_id = " + orgId + "\n");
		sql.append("                CONNECT BY PRIOR o.org_id = o.parent_org_id)\n");
		sql.append("           and dealer.OEM_COMPANY_ID = " + companyId + "\n");// 多公司过滤
		sql.append("           and dealer.STATUS = " + Constant.STATUS_ENABLE + ") \n");// 有效
		sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/**
	 * 配额导入校验经销商是否存在
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> quotaImportCheckDealer_CVS(Map<String, Object> map) {
		String userId = map.get("userId").toString();
		String orgId = map.get("orgId").toString();
		String companyId = map.get("companyId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_QUOTA p\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and p.USER_ID =" + userId + "\n");
		sql.append("   and p.DEALER_CODE is not null\n");
		sql.append("   and not exists (select 1\n");
		sql.append("          from TM_DEALER dealer, vw_org_dealer r\n");
		sql.append("         where dealer.DEALER_CODE = p.DEALER_CODE\n");
		sql.append("           and dealer.DEALER_ID = r.DEALER_ID\n");
		//sql.append("           and r.ORG_ID = " + orgId + "\n");

		sql.append("           AND r.root_org_id IN\n");
		sql.append("               (SELECT org_id\n");
		sql.append("                  FROM tm_org o\n");
		sql.append("                 START WITH org_id = " + orgId + "\n");
		sql.append("                CONNECT BY PRIOR o.org_id = o.parent_org_id)\n");
		sql.append("           and dealer.OEM_COMPANY_ID = " + companyId + "\n");// 多公司过滤
		sql.append("           and dealer.STATUS = " + Constant.STATUS_ENABLE + ") \n");// 有效
		sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/**
	 * 配额导入校验配置是否存在
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> quotaImportCheckGroup(
			Map<String, Object> map) {
		String userId = (String) map.get("userId");
		String companyId = (String) map.get("companyId");
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_QUOTA p\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and p.USER_ID =" + userId + "\n");
		sql.append("   and not exists (select 1\n");
		sql.append("       from TM_VHCL_MATERIAL_GROUP g\n");
		sql.append("      where g.GROUP_CODE = p.GROUP_CODE\n");
		sql.append("        and g.STATUS = " + Constant.STATUS_ENABLE + "\n");// 有效
		sql.append("        and g.COMPANY_ID = " + companyId + ")\n");
		sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/**
	 * 配额导入校验车系是否与业务范围一致
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> quotaImportCheckGroupArea(
			Map<String, Object> map) {
		String userId = (String) map.get("userId");
		String groupArea = (String) map.get("groupArea");
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_QUOTA p\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and p.USER_ID = " + userId + "\n");
		sql.append("   and p.GROUP_CODE not in("
				+ groupArea + ")\n");
		sql.append(" order by TO_NUMBER(p.ROW_NUMBER) asc");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/**
	 * 配额导入校验区域业务范围是否一致
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> quotaImportCheckOrgArea(
			Map<String, Object> map) {
		String userId = (String) map.get("userId");
		String companyId = (String) map.get("companyId");
		String areaId = (String) map.get("areaId");
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT P.ROW_NUMBER\n");
		sql.append("  FROM TMP_VS_QUOTA P\n");
		sql.append(" WHERE 1 = 1\n");
		sql.append("   AND P.USER_ID = " + userId + "\n");
		sql.append("   AND NOT EXISTS (SELECT 1\n");
		sql.append("          FROM TM_ORG_BUSINESS_AREA TOBA, TM_ORG TOR\n");
		sql.append("         WHERE TOBA.ORG_ID = TOR.ORG_ID\n");
		sql.append("           AND P.ORG_CODE = TOR.ORG_CODE\n");
		sql.append("           AND TOBA.AREA_ID = " + areaId + "\n");
		sql.append("           AND TOR.COMPANY_ID = " + companyId + ")\n");
		sql.append(" ORDER BY TO_NUMBER(p.ROW_NUMBER) ASC");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/**
	 * 配额导入校验经销商业务范围是否一致
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> quotaImportCheckDealerArea(
			Map<String, Object> map) {
		String userId = (String) map.get("userId");
		String companyId = (String) map.get("companyId");
		String areaId = (String) map.get("areaId");
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT P.ROW_NUMBER\n");
		sql.append("  FROM TMP_VS_QUOTA P\n");
		sql.append(" WHERE 1 = 1\n");
		sql.append("   AND P.USER_ID = " + userId + "\n");
		sql.append("   AND NOT EXISTS (SELECT 1\n");
		sql
				.append("          FROM TM_DEALER_BUSINESS_AREA TDBA, TM_DEALER TDA\n");
		sql.append("         WHERE TDBA.DEALER_ID = TDA.DEALER_ID\n");
		sql.append("           AND P.DEALER_CODE = TDA.DEALER_CODE\n");
		sql.append("           AND TDBA.AREA_ID = " + areaId + "\n");
		sql.append("           AND TDA.OEM_COMPANY_ID = " + companyId + ")\n");
		sql.append(" ORDER BY TO_NUMBER(p.ROW_NUMBER) ASC");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/**
	 * 查询临时表中周次是否在工作日历中存在
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> checkDateSetWeek(Map<String, Object> map) {
		String userId = (String) map.get("userId");
		String companyId = (String) map.get("companyId");

		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_QUOTA p\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and p.USER_ID = " + userId + "\n");
		sql.append("   and not exists (select 1\n");
		sql.append("          from TM_DATE_SET tds\n");
		sql.append("         where tds.SET_YEAR = p.QUOTA_YEAR\n");
		sql.append("           and tds.SET_MONTH = p.QUOTA_MONTH\n");
		sql.append("           and tds.SET_WEEK = p.QUOTA_WEEK\n");
		sql.append("           and tds.COMPANY_ID = " + companyId + ")\n");
		sql.append(" order by TO_NUMBER(p.ROW_NUMBER) asc");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params,
				getFunName());

		return list;
	}

	/**
	 * 查询临时表中是否有相同周导入相同的配置
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> talbeCheckDump(Map<String, Object> map) {
		String userId = (String) map.get("userId");
		String quotaType = (String) map.get("quotaType");
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql
				.append("select p1.ROW_NUMBER ROW_NUMBER1, p2.ROW_NUMBER ROW_NUMBER2\n");
		sql.append("  from TMP_VS_QUOTA p1, TMP_VS_QUOTA p2\n");
		sql.append(" where \n");
		sql.append("   p1.GROUP_CODE = p2.GROUP_CODE\n");
		sql.append("   and p1.ROW_NUMBER <> p2.ROW_NUMBER\n");
		sql.append("   and p1.QUOTA_YEAR = p2.QUOTA_YEAR\n");
		sql.append("   and p1.QUOTA_MONTH = p2.QUOTA_MONTH\n");
		sql.append("   and p1.QUOTA_WEEK = p2.QUOTA_WEEK\n");
		if (quotaType.equals(Constant.QUOTA_TYPE_01.toString())) {
			sql.append("   and p1.ORG_CODE = p2.ORG_CODE\n");
		} else {
			sql.append("   and p1.DEALER_CODE = p2.DEALER_CODE\n");
		}
		sql.append("   and p1.USER_ID = p2.USER_ID\n");
		sql.append("   and p1.USER_ID= ? \n");
		params.add(userId);
		sql.append("   order by TO_NUMBER(p1.ROW_NUMBER)\n");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params,
				getFunName());

		return list;
	}
	public List<Map<String, Object>> getTempDownWeek(Map<String, Object> map) {
		String companyId = map.get("companyId").toString() ;
		String year = map.get("year").toString() ;
		String month = map.get("month").toString() ;
		StringBuffer sql = new StringBuffer() ;
		sql.append("SELECT TEMP.SET_WEEK FROM (SELECT DISTINCT TO_NUMBER(TDS.SET_WEEK) SET_WEEK FROM TM_DATE_SET TDS WHERE TDS.SET_YEAR='" + year + "' AND TDS.SET_MONTH='" + month + "' AND TDS.COMPANY_ID='" + companyId + "') TEMP ORDER BY TEMP.SET_WEEK");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null,
				getFunName());
		return list;
	}

    	public List<Map<String, Object>> getTempDownWeek2(Map<String, Object> map) {
		String companyId = map.get("companyId").toString() ;
		String year = map.get("year").toString() ;
		String month = map.get("month").toString() ;
//		StringBuffer sql = new StringBuffer() ;
//		sql.append("SELECT TEMP.SET_WEEK FROM (SELECT DISTINCT TO_NUMBER(TDS.SET_WEEK) SET_WEEK FROM TM_DATE_SET TDS WHERE TDS.SET_YEAR='" + year + "' AND TDS.SET_MONTH='" + month + "' AND TDS.COMPANY_ID='" + companyId + "') TEMP ORDER BY TEMP.SET_WEEK");

        //updated by lichuang begin
        StringBuilder sql= new StringBuilder();
        sql.append("WITH X AS\n" );
        sql.append(" (SELECT DISTINCT TDS.SET_WEEK\n" );
        sql.append("    FROM TM_DATE_SET TDS\n" );
        sql.append("   WHERE TDS.SET_YEAR = '"+year+"'\n" );
        sql.append("     AND TDS.SET_MONTH = '"+month+"'\n" );
        sql.append("     AND TDS.COMPANY_ID = '"+companyId+"'),\n" );
        sql.append("Y AS\n" );
        sql.append(" (SELECT TDS.SET_WEEK, SET_DATE\n" );
        sql.append("    FROM TM_DATE_SET TDS\n" );
        sql.append("   WHERE TDS.SET_YEAR = '"+year+"'\n" );
        sql.append("     AND TDS.SET_MONTH = '"+month+"'\n" );
        sql.append("     AND TDS.COMPANY_ID = '"+companyId+"')\n" );
        sql.append("\n" );
        sql.append("SELECT SET_WEEK || '(' || F_DATE || '-' || L_DATE || ')' SET_WEEK\n" );
        sql.append("  FROM (SELECT X.SET_WEEK,\n" );
        sql.append("               (SELECT MIN(Y.SET_DATE) FROM Y WHERE X.SET_WEEK = Y.SET_WEEK) F_DATE,\n" );
        sql.append("               (SELECT MAX(Y.SET_DATE) FROM Y WHERE X.SET_WEEK = Y.SET_WEEK) L_DATE\n" );
        sql.append("\n" );
        sql.append("          FROM X\n" );
        sql.append("         ORDER BY X.SET_WEEK)");
        //end

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null,
				getFunName());
		return list;
	}
	public List<Map<String, Object>> getTempDownload(Map<String, Object> map) {
        String userId=(String)map.get("userId");

        StringBuilder sql= new StringBuilder();
        sql.append("SELECT \n" );
        List<Map<String, Object>> downWeeks = dao.getTempDownWeek(map);
        for (int i = 0; i < downWeeks.size(); i++) {
                int week=((BigDecimal)downWeeks.get(i).get("SET_WEEK")).intValue();
                sql.append("SUM(DECODE(TQOD.QUOTA_WEEK,"+week+",TQOD.QUOTA_AMT,NULL)) A"+week+",\n");
            }

        sql.append("       TQOD.ORG_CODE,TQOD.ORG_NAME,TQOD.GROUP_CODE,TQOD.GROUP_NAME\n" );
        sql.append("       FROM TMP_QUOTA_DISP_ORG TQOD\n" );
        sql.append("       WHERE TQOD.USER_ID = "+userId+"\n");
        sql.append("       GROUP BY TQOD.ORG_CODE,TQOD.ORG_NAME,TQOD.GROUP_CODE,TQOD.GROUP_NAME");


		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null,
				getFunName());

		return list;
	}
	public List<Map<String, Object>> getTempDownload2(Map<String, Object> map) {
		String companyId = map.get("companyId").toString() ;
		String areaId = map.get("areaId").toString() ;
		String year = map.get("year").toString() ;
		String month = map.get("month").toString() ;
		String moterIds=map.get("moterIds").toString();
		StringBuffer sql = new StringBuffer() ;

		sql.append("SELECT DISTINCT TMO.ORG_CODE, TMO.ORG_NAME, TVMG3.GROUP_CODE,TVMG3.GROUP_NAME, TDS.SET_YEAR\n");
		sql.append("  FROM TM_ORG                 TMO,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		//sql.append("       TM_ORG_BUSINESS_AREA   TOBA,\n");
		//sql.append("       TM_AREA_GROUP          TAG,\n");
		sql.append("       TM_DATE_SET            TDS\n");
		sql.append(" WHERE 1=1\n");
		//sql.append("   AND TMO.ORG_ID = TOBA.ORG_ID\n");
		//sql.append("   AND TOBA.AREA_ID = TAG.AREA_ID\n");
		//sql.append("   AND TAG.MATERIAL_GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("   AND TVMG1.GROUP_ID = TVMG2.PARENT_GROUP_ID\n");
		sql.append("   AND TVMG2.GROUP_ID = TVMG3.PARENT_GROUP_ID\n");

		sql.append("and (exists (select 1\n");
		sql.append("                  from tm_area_group tma\n");
		sql.append("                 where tvmg1.group_id = tma.material_group_id\n");
		sql.append("                   and tma.area_id = " + areaId + ") or exists\n");
		sql.append("        (select 1\n");
		sql.append("           from tm_area_group tma\n");
		sql.append("          where tvmg2.group_id = tma.material_group_id\n");

		sql.append("            and tma.area_id = " + areaId + ") or exists\n");
		sql.append("        (select 1\n");
		sql.append("           from tm_area_group tma\n");
		sql.append("          where tvmg3.group_id = tma.material_group_id\n");
		sql.append("            and tma.area_id = " + areaId + "))\n");


		sql.append("   AND TVMG1.FORCAST_FLAG=1\n");
		sql.append("   AND TVMG2.FORCAST_FLAG=1\n");
		sql.append("   AND TVMG3.FORCAST_FLAG=1\n");
        sql.append("AND TVMG1.STATUS="+Constant.STATUS_ENABLE+"\n" );
        sql.append("AND TVMG2.STATUS="+Constant.STATUS_ENABLE+"\n" );
        sql.append("AND TVMG3.STATUS="+Constant.STATUS_ENABLE+"\n" );
		sql.append("   AND TDS.COMPANY_ID = TMO.COMPANY_ID\n");
		sql.append("   AND TMO.ORG_TYPE = '" + Constant.ORG_TYPE_OEM + "'\n");
		sql.append("   AND TMO.DUTY_TYPE = '" + Constant.DUTY_TYPE_COMPANY + "'\n");
		//sql.append("   AND TOBA.AREA_ID = '" + areaId + "'\n");
		sql.append("   AND TMO.COMPANY_ID = '" + companyId + "'\n");
		sql.append("   AND TVMG3.GROUP_LEVEL = '4'\n");
		sql.append("   AND TDS.SET_YEAR = '" + year + "'\n");
		sql.append("   AND TDS.SET_MONTH = '" + month + "'\n");
		sql.append("   AND TMO.STATUS=10011001 \n");
		sql.append("   ORDER BY TDS.SET_YEAR, TMO.ORG_CODE, TVMG3.GROUP_CODE \n");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null,
				getFunName());

		return list;
	}

	public List<Map<String, Object>> downLoadDEL(Map<String, Object> map) {
        String orgId=(String)map.get("orgId");
        String areaId=(String)map.get("areaId");
        String year=(String)map.get("year");
        String month=(String)map.get("month");


        StringBuilder sql= new StringBuilder();
        sql.append("WITH A AS\n" );
        sql.append(" (SELECT\n" );
        List<Map<String, Object>> downWeeks = dao.getTempDownWeek(map);
        for (int i = 0; i < downWeeks.size(); i++) {
                int week=((BigDecimal)downWeeks.get(i).get("SET_WEEK")).intValue();
                sql.append("SUM(DECODE(TQDD.QUOTA_WEEK,"+week+",TQDD.QUOTA_AMT,NULL)) A"+week+",\n");
            }
        sql.append("         TQDD.DEALER_CODE,\n" );
        sql.append("         TQDD.DEALER_NAME,\n" );
        sql.append("         TQDD.PROVINCE,\n" );
        sql.append("         TQDD.GROUP_CODE,\n" );
        sql.append("         TQDD.GROUP_NAME,\n" );
        sql.append("         GROUP_ID,\n" );
        sql.append("         DEALER_ID\n" );
        sql.append("    FROM TMP_QUOTA_DISP_DLR TQDD\n" );
        sql.append("   WHERE TQDD.ORG_ID = "+orgId+"\n" );
        sql.append("   AND TQDD.QUOTA_YEAR = "+year+"\n" );
        sql.append("   AND TQDD.QUOTA_MONTH = "+month+"\n" );
        sql.append("   GROUP BY TQDD.DEALER_CODE,\n" );
        sql.append("            TQDD.DEALER_NAME,\n" );
        sql.append("            TQDD.PROVINCE,\n" );
        sql.append("            TQDD.GROUP_CODE,\n" );
        sql.append("            TQDD.GROUP_NAME,\n" );
        sql.append("            TQDD.GROUP_ID,\n" );
        sql.append("            TQDD.DEALER_ID),\n" );
        sql.append("B AS\n" );
        sql.append(" (SELECT *\n" );
        sql.append("    FROM TT_VS_MONTHLY_FORECAST TVMF, TT_VS_MONTHLY_FORECAST_DETAIL TVMFD\n" );
        sql.append("   WHERE TVMF.FORECAST_ID = TVMFD.FORECAST_ID\n" );
        sql.append("   AND TVMF.FORECAST_YEAR="+year+"\n" );
        sql.append("   AND TVMF.FORECAST_MONTH="+month+"\n" );
        sql.append("   AND TVMF.STATUS="+Constant.FORECAST_STATUS_CONFIRM+"--已提报\n");
        sql.append("   AND TVMF.ORG_TYPE="+Constant.ORG_TYPE_DEALER+")--经销商\n");

        sql.append("SELECT A.*, NVL(B.FORECAST_AMOUNT,0) FORECAST_AMOUNT\n" );
        sql.append("  FROM A, B\n" );
        sql.append(" WHERE A.DEALER_ID = B.DEALER_ID(+)\n" );
        sql.append("   AND A.GROUP_ID = B.GROUP_ID(+)\n" );


		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}

	/**
	 * 取得配额表中限制年份与月份的周度
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getWeek(Map<String, Object> map) {
		String year = map.get("year").toString() ;
		String month = map.get("month").toString() ;

		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("SELECT DISTINCT TVDQ.QUOTA_YEAR, TVDQ.QUOTA_WEEK\n");
		sql.append("  FROM TMP_VS_DLR_QUOTA TVDQ\n");
		sql.append(" WHERE TVDQ.QUOTA_YEAR = ").append(year).append("\n");
		sql.append("   AND TVDQ.QUOTA_MONTH = ").append(month).append("\n");
		sql.append(" ORDER BY TVDQ.QUOTA_WEEK\n");

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
}