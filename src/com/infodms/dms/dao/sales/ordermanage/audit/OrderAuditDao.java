/**********************************************************************
 * <pre>
 * FILE : OrderAuditDao.java
 * CLASS : OrderAuditDao
 * AUTHOR : 
 * FUNCTION : 订单审核
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2010-05-28|            | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.dao.sales.ordermanage.audit;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.ordermanage.reqquery.ReqQueryDao;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVsAddressPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 订单审核DAO
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-28
 * @author
 * @mail
 * @version 1.0
 * @remark
 */
public class OrderAuditDao extends BaseDao<PO> {

	public static Logger logger = Logger.getLogger(OrderDeliveryDao.class);
	private static final OrderAuditDao dao = new OrderAuditDao();

	public static final OrderAuditDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Function : 补充订单可提报资源设定---物料组层面
	 * 
	 * @param :
	 *            物料组CODE
	 * @return : 物料组信息
	 * @throws :
	 *             Exception LastUpdate : 2010-05-28
	 */
	public PageResult<Map<String, Object>> getOrderResourceList(String groupCode, String areaIds, Long companyId, int curPage, int pageSize) throws Exception {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT T.GROUP_ID, ---物料组ID\n");
		sql.append("                T.SERIESE_CODE, ---车系代码\n");
		sql.append("                T.SERIESE_NAME, ---车系名称\n");
		sql.append("                T.MODEL_CODE, ---车型代码\n");
		sql.append("                T.MODEL_NAME, ---车型名称\n");
		sql.append("                T.CONFIG_CODE, ---配置代码\n");
		sql.append("                T.CONFIG_NAME, ---配置名称\n");
		sql.append("                DECODE(M.COUNTS, 0, '不可提报', '可提报') AS RUSH_ORDER_FLAG ---是否可提报\n");
		sql.append("  FROM (SELECT TVMG3.GROUP_ID,\n");
		sql.append("               TVMG1.GROUP_CODE AS SERIESE_CODE,\n");
		sql.append("               TVMG1.GROUP_NAME AS SERIESE_NAME,\n");
		sql.append("               TVMG2.GROUP_CODE AS MODEL_CODE,\n");
		sql.append("               TVMG2.GROUP_NAME AS MODEL_NAME,\n");
		sql.append("               TVMG3.GROUP_CODE AS CONFIG_CODE,\n");
		sql.append("               TVMG3.GROUP_NAME AS CONFIG_NAME\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("               TM_AREA_GROUP          TMAG\n");
		sql.append("         WHERE TVMG1.GROUP_ID = TVMG2.PARENT_GROUP_ID\n");
		sql.append("           AND TVMG2.GROUP_ID = TVMG3.PARENT_GROUP_ID\n");
		sql.append("           AND (TMAG.MATERIAL_GROUP_ID = TVMG3.GROUP_ID OR\n");
		sql.append("               TMAG.MATERIAL_GROUP_ID = TVMG2.GROUP_ID OR\n");
		sql.append("               TMAG.MATERIAL_GROUP_ID = TVMG1.GROUP_ID)\n");
		sql.append("           AND TMAG.AREA_ID IN (" + areaIds + ")\n");
		if (!"".equals(groupCode) && null != groupCode) {
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG3", "GROUP_CODE"));
		}
		sql.append("           AND TVMG3.GROUP_LEVEL = 4\n");
		sql.append("           AND TVMG3.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TVMG1.STATUS = " + Constant.STATUS_ENABLE + ") T\n");
		sql.append("  LEFT JOIN (SELECT TVMGR.GROUP_ID, COUNT(TVM.MATERIAL_ID) AS COUNTS\n");
		sql.append("               FROM TM_VHCL_MATERIAL_GROUP_R TVMGR\n");
		sql.append("               LEFT JOIN TM_VHCL_MATERIAL TVM ON TVMGR.MATERIAL_ID =\n");
		sql.append("                                                 TVM.MATERIAL_ID\n");
		sql.append("                                             AND TVM.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("                                             AND TVM.RUSH_ORDER_FLAG =\n");
		sql.append("                                                 " + Constant.NASTY_ORDER_REPORT_TYPE_01 + "\n");
		sql.append("              GROUP BY GROUP_ID) M ON T.GROUP_ID = M.GROUP_ID\n");
		sql.append(" ORDER BY SERIESE_CODE");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return rs;
	}

	/**
	 * Function : 补充订单可提报资源设定---物料层面
	 * 
	 * @param :
	 *            物料组ID
	 * @return : 物料信息
	 * @throws :
	 *             Exception LastUpdate : 2010-05-28
	 */
	public PageResult<Map<String, Object>> getMaterialList(String groupId, String areaIds, Long companyId, int curPage, int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT T.SERIESE_CODE,---车系代码\n");
		sql.append("       T.SERIESE_NAME,---车系名称\n");
		sql.append("       T.MODEL_CODE,---车型代码\n");
		sql.append("       T.MODEL_NAME,---车型名称\n");
		sql.append("       T.CONFIG_CODE,---配置代码\n");
		sql.append("       T.CONFIG_NAME,---配置名称\n");
		sql.append("       M.MATERIAL_ID,---物料ID\n");
		sql.append("       DECODE(M.RUSH_ORDER_FLAG," + Constant.NASTY_ORDER_REPORT_TYPE_01 + ",'可提报','不可提报') RUSH_ORDER_FLAG,---是否可提报\n");
		sql.append("       M.COLOR_NAME---颜色名称\n");
		sql.append("  FROM (SELECT TVMG3.GROUP_ID,\n");
		sql.append("               TVMG1.GROUP_CODE AS SERIESE_CODE,\n");
		sql.append("               TVMG1.GROUP_NAME AS SERIESE_NAME,\n");
		sql.append("               TVMG2.GROUP_CODE AS MODEL_CODE,\n");
		sql.append("               TVMG2.GROUP_NAME AS MODEL_NAME,\n");
		sql.append("               TVMG3.GROUP_CODE AS CONFIG_CODE,\n");
		sql.append("               TVMG3.GROUP_NAME AS CONFIG_NAME\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("               TM_AREA_GROUP          TMAG\n");
		sql.append("         WHERE TVMG1.GROUP_ID = TVMG2.PARENT_GROUP_ID\n");
		sql.append("           AND TVMG2.GROUP_ID = TVMG3.PARENT_GROUP_ID\n");
		sql.append("           AND (TMAG.MATERIAL_GROUP_ID = TVMG3.GROUP_ID OR\n");
		sql.append("               TMAG.MATERIAL_GROUP_ID = TVMG2.GROUP_ID OR\n");
		sql.append("               TMAG.MATERIAL_GROUP_ID = TVMG1.GROUP_ID)\n");
		sql.append("           AND TMAG.AREA_ID IN (" + areaIds + ")\n");
		sql.append("                                           AND TVMG3.GROUP_LEVEL = 4\n");
		sql.append("           AND TVMG3.COMPANY_ID = " + companyId + "\n");
		sql.append("                                           AND TVMG1.STATUS = " + Constant.STATUS_ENABLE + ") T,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R P,\n");
		sql.append("       TM_VHCL_MATERIAL M\n");
		sql.append(" WHERE T.GROUP_ID = P.GROUP_ID\n");
		sql.append("   AND P.MATERIAL_ID = M.MATERIAL_ID\n");
		sql.append("   AND M.STATUS = " + Constant.STATUS_ENABLE + "\n");
		if (!"".equals(groupId) && groupId != null) {
			sql.append("   AND P.GROUP_ID = " + groupId + "\n");
		}
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 获得业务配置参数PO
	 * 
	 * @param paraId
	 * @return
	 */
	public TmBusinessParaPO getTmBusinessParaPO(Integer paraId, Long companyId) {
		TmBusinessParaPO po = new TmBusinessParaPO();
		po.setParaId(paraId);
		List<PO> list = select(po);
		return list.size() != 0 ? (TmBusinessParaPO) list.get(0) : null;
	}

	/**
	 * 获得TmDateSetPO
	 * 
	 * @param po
	 * @return
	 */
	public TmDateSetPO getTmDateSetPO(TmDateSetPO po) {
		List<PO> list = select(po);
		return list.size() != 0 ? (TmDateSetPO) list.get(0) : null;
	}

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

	public List<Map<String, Object>> getGeneralDateList_New(Long companyId, TmBusinessParaPO tpa) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = getTmDateSetPO(new Date(), companyId);
		if (dateSet != null) {
			TmBusinessParaPO para1 = getTmBusinessParaPO(Constant.GENEREAL_ORDER_BEFORE_WEEK_PARA, new Long(companyId));// 常规订单提报提前周度参数
			TmBusinessParaPO para2 = getTmBusinessParaPO(Constant.GENEREAL_ORDER_WEEK_PARA, new Long(companyId));// 常规订单提报周度参数
			// TmBusinessParaPO para3 = getTmBusinessParaPO(
			// Constant.ORDER_DEFERRED_PARA, new Long(companyId));// 订单延期周度参数

			String currentYear = dateSet.getSetYear(); // 获取系统日历表中对应年份
			String currentWeek = dateSet.getSetWeek(); // 获取系统日历表中对应月份

			int index = Integer.parseInt(tpa.getParaValue());

			if (index >= 0) {
				String maxWeek = null;

				OrderReportDao ord = new OrderReportDao();
				Map<String, String> map = new HashMap<String, String>();

				map.put("year", currentYear);

				maxWeek = ord.getMaxWeek(map).get("SET_WEEK").toString();

				String nextYear = null;
				String nextWeek = null;

				if (Integer.parseInt(maxWeek) < Integer.parseInt(currentWeek) + Integer.parseInt(para1.getParaValue())) {
					nextYear = (Integer.parseInt(currentYear) + 1) + "";
					nextWeek = (Integer.parseInt(currentWeek) + Integer.parseInt(para1.getParaValue()) - Integer.parseInt(maxWeek)) + "";
				} else if (Integer.parseInt(currentWeek) + Integer.parseInt(para1.getParaValue()) < 0) {
					nextYear = (Integer.parseInt(currentYear) - 1) + "";

					map.put("year", nextYear);

					String nextMaxWeek = ord.getMaxWeek(map).get("SET_WEEK").toString();

					nextWeek = (Integer.parseInt(currentWeek) + Integer.parseInt(para1.getParaValue()) + nextMaxWeek) + "";
				} else {
					nextYear = currentYear;
					nextWeek = (Integer.parseInt(currentWeek) + Integer.parseInt(para1.getParaValue())) + "";
				}

				if (!"0".equals(tpa.getParaValue())) {
					nextYear = currentYear;
					nextWeek = (Integer.parseInt(currentWeek) - Integer.parseInt(tpa.getParaValue())) + "";

					if (Integer.parseInt(nextWeek) < 0) {
						nextYear = (Integer.parseInt(currentYear) - 1) + "";

						map.put("year", nextYear);

						maxWeek = ord.getMaxWeek(map).get("SET_WEEK").toString();

						nextWeek = (Integer.parseInt(maxWeek) + Integer.parseInt(nextWeek)) + "";
					} else if (Integer.parseInt(maxWeek) - Integer.parseInt(currentWeek) < 0) {
						nextYear = (Integer.parseInt(currentYear) + 1) + "";

						nextWeek = (Integer.parseInt(currentWeek) - Integer.parseInt(maxWeek)) + "";
					}
				}

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				String date_start = null;
				String date_end = null;

				if (dateSet != null) {
					for (int i = 0; i < (Integer.parseInt(para2.getParaValue()) + index); i++) {
						map.put("year", nextYear);
						maxWeek = ord.getMaxWeek(map).get("SET_WEEK").toString();

						if (Integer.parseInt(maxWeek) < Integer.parseInt(nextWeek)) {
							nextYear = (Integer.parseInt(nextYear) + 1) + "";
							nextWeek = (Integer.parseInt(nextWeek) - Integer.parseInt(maxWeek)) + "";
						}

						map.put("week", nextWeek);

						Map<String, Object> dateMap = ord.getDayByWeek(map);

						if (dateMap != null) {
							date_start = dateMap.get("MINDATE").toString().substring(0, 4) + "/" + dateMap.get("MINDATE").toString().substring(4, 6) + "/" + dateMap.get("MINDATE").toString().substring(6, 8);
							date_end = dateMap.get("MAXDATE").toString().substring(0, 4) + "/" + dateMap.get("MAXDATE").toString().substring(4, 6) + "/" + dateMap.get("MAXDATE").toString().substring(6, 8);

							Map<String, Object> newMap = new HashMap<String, Object>();
							newMap.put("code", nextYear + "-" + nextWeek);
							newMap.put("name", nextYear + "年" + nextWeek + "周");
							newMap.put("date_start", date_start);
							newMap.put("date_end", date_end);
							list.add(newMap);
						}

						nextWeek = (Integer.parseInt(nextWeek) + 1) + "";
					}
				}
			}
		}

		return list;
	}

	/**
	 * 常规订单周度列表
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getGeneralDateList(Long companyId, TmBusinessParaPO tpa) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = getTmDateSetPO(new Date(), companyId);
		if (dateSet != null) {
			TmBusinessParaPO para1 = getTmBusinessParaPO(Constant.GENEREAL_ORDER_BEFORE_WEEK_PARA, new Long(companyId));// 常规订单提报提前周度参数
			TmBusinessParaPO para2 = getTmBusinessParaPO(Constant.GENEREAL_ORDER_WEEK_PARA, new Long(companyId));// 常规订单提报周度参数
			// TmBusinessParaPO para3 = getTmBusinessParaPO(
			// Constant.ORDER_DEFERRED_PARA, new Long(companyId));// 订单延期周度参数

			Calendar c = Calendar.getInstance();
			if ("0".equals(tpa.getParaValue())) { // YH 2011.5.23
				c.add(Calendar.DATE, 7 * Integer.parseInt(para1.getParaValue()));
			} else {
				c.add(Calendar.DATE, 7 * (Integer.parseInt(para1.getParaValue()) - Integer.parseInt(tpa.getParaValue())));
			}

			dateSet = getTmDateSetPO(c.getTime(), companyId);

			// 计算周度的日期范围
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String year = dateSet.getSetYear();
			String week = dateSet.getSetWeek();
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(Calendar.YEAR, Integer.parseInt(year));
			calendar.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(week) + 1);
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			String date_start = dateFormat.format(calendar.getTime());// 起始日期
			calendar.add(Calendar.DATE, 6);
			String date_end = dateFormat.format(calendar.getTime());// 结束日期

			// for (int j = Integer.parseInt(para3.getParaValue()); j >0 ; j--)
			// {
			// //c.add(Calendar.DATE, 7);
			// Map<String, Object> map1 = new HashMap<String, Object>();
			// if (dateSet != null) {
			// map1 = new HashMap<String, Object>();
			// map1.put("code", dateSet.getSetYear() + "-"
			// + (Integer.parseInt(dateSet.getSetWeek())-j));
			// map1.put("name", dateSet.getSetYear() + "年"
			// + (Integer.parseInt(dateSet.getSetWeek())-j) + "周");
			// list.add(map1);
			// }
			// }
			// 审核对应的第一周
			if (dateSet != null) {
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("code", dateSet.getSetYear() + "-" + dateSet.getSetWeek());
				map2.put("name", dateSet.getSetYear() + "年" + dateSet.getSetWeek() + "周");
				map2.put("date_start", date_start);
				map2.put("date_end", date_end);
				list.add(map2);
			}
			// 为了适应跨年周，若可提报周度大于1时，每次都从DATE_SET中取对应的周度

			int rollweek = 0;
			if ("0".equals(tpa.getParaValue())) { // YH 2011.5.23
				rollweek = Integer.parseInt(para2.getParaValue());
			} else {
				rollweek = Integer.parseInt(para2.getParaValue()) + Integer.parseInt(tpa.getParaValue());
			}

			if (rollweek > 1) {
				for (int i = 1; i < rollweek; i++) {
					c.add(Calendar.DATE, 7);
					dateSet = getTmDateSetPO(c.getTime(), companyId);

					calendar.add(Calendar.DATE, 1); // YH 2011.5.24 修改日期BUG
					date_start = dateFormat.format(calendar.getTime());// 起始日期
					calendar.add(Calendar.DATE, 6);
					date_end = dateFormat.format(calendar.getTime());// 结束日期
					Map<String, Object> map3 = new HashMap<String, Object>();
					if (dateSet != null) {
						map3 = new HashMap<String, Object>();
						map3.put("code", dateSet.getSetYear() + "-" + dateSet.getSetWeek());
						map3.put("name", dateSet.getSetYear() + "年" + dateSet.getSetWeek() + "周");
						map3.put("date_start", date_start);
						map3.put("date_end", date_end);
						list.add(map3);
					}
				}
			}

		}
		return list;
	}

	/**
	 * 补充订单周度列表
	 * 
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> getNastyDateList(Long companyId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = getTmDateSetPO(new Date(), companyId);
		if (dateSet != null) {
			TmBusinessParaPO para1 = getTmBusinessParaPO(Constant.URGENT_ORDER_WEEK_BEFORE_PARA, new Long(companyId));// 补充订单提报提前周度参数
			TmBusinessParaPO para2 = getTmBusinessParaPO(Constant.URGENT_ORDER_WEEK_PARA, new Long(companyId));// 补充订单提报周度参数

			// 获得提报起始周
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, 7 * Integer.parseInt(para1.getParaValue()));
			dateSet = getTmDateSetPO(c.getTime(), companyId);
			if (dateSet != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", dateSet.getSetYear() + "-" + dateSet.getSetWeek());
				map.put("name", dateSet.getSetYear() + "年" + dateSet.getSetWeek() + "周");
				list.add(map);

				for (int i = 1; i < Integer.parseInt(para2.getParaValue()); i++) {
					c.add(Calendar.DATE, 7);
					dateSet = getTmDateSetPO(c.getTime(), companyId);
					if (dateSet != null) {
						map = new HashMap<String, Object>();
						map.put("code", dateSet.getSetYear() + "-" + dateSet.getSetWeek());
						map.put("name", dateSet.getSetYear() + "年" + dateSet.getSetWeek() + "周");
						list.add(map);
					}
				}
			}
		}

		return list;
	}

	/**
	 * 年度列表
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getDateYearList() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = -1; i <= 1; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear", year + i);
			list.add(map);
		}
		return list;
	}

	/**
	 * 周度列表
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getDateWeekList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 1; i <= 53; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderWeek", i);
			list.add(map);
		}
		return list;
	}

	/**
	 * Function : 常规订单审核查询---配置层面
	 * 
	 * @param :
	 *            订单年
	 * @param :
	 *            订单周
	 * @param :
	 *            业务范围ID
	 * @param :
	 *            业务范围
	 * @return : 配置层面汇总信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	public PageResult<Map<String, Object>> getGeneralOrderCheckList(String orderYear, String orderWeek, String areaId, String area, Long companyId, int curPage, int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.GROUP_ID, ---物料组ID\n");
		sql.append("       T.GROUP_CODE, ---物料组代码\n");
		sql.append("       T.GROUP_NAME, ---物料组名称\n");
		sql.append("       T.ORDER_YEAR, ---订单年\n");
		sql.append("       T.ORDER_WEEK, ---订单周\n");
		sql.append("       T.QUOTA_AMT, ---配额数量\n");
		sql.append("       T.ORDER_AMOUNT, ---订单数量\n");
		sql.append("       T.CHECK_AMOUNT, ---审核数量\n");
		sql.append("       T.STOCK_AMOUNT, ---当前库存数量\n");
		sql.append("       NVL(M.PLAN_AMOUNT, 0) PLAN_AMOUNT,\n");
		sql.append("       NVL(M.AMOUNT, 0) AMOUNT\n");
		sql.append("  FROM (SELECT T1.GROUP_ID,\n");
		sql.append("               T1.GROUP_CODE,\n");
		sql.append("               T1.GROUP_NAME,\n");
		sql.append("               T1.ORDER_YEAR,\n");
		sql.append("               T1.ORDER_WEEK,\n");
		sql.append("               T1.ORDER_AMOUNT,\n");
		sql.append("               T1.CHECK_AMOUNT,\n");
		sql.append("               T1.STOCK_AMOUNT,\n");
		sql.append("               T2.QUOTA_AMT\n");

		sql.append("from (SELECT TVMG.GROUP_ID,\n");
		sql.append("       TVMG.GROUP_CODE,\n");
		sql.append("       TVMG.GROUP_NAME,\n");
		sql.append("       TVO.ORDER_YEAR,\n");
		sql.append("       TVO.ORDER_WEEK,\n");
		sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("       NVL(VVR.STOCK_AMOUNT, 0) STOCK_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER TVO, ---订单表\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD, ---订单明细表\n");
		sql.append("       TM_VHCL_MATERIAL TVM, ---物料表\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG, ---物料组表\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR, ---物料与物料组关系表\n");
		sql.append("       (SELECT tvmgr.group_id, sum(TEMP.STOCK_AMOUNT) STOCK_AMOUNT\n");
		sql.append("          FROM VW_VS_RESOURCE TEMP, tm_vhcl_material_group_r tvmgr\n");
		sql.append("         WHERE temp.MATERIAL_ID = tvmgr.material_id\n");

		sql.append("                         and TEMP.company_id = " + companyId + "\n");

		sql.append("AND TEMP.SPECIAL_BATCH_NO IS NULL\n");
		sql.append("         group by tvmgr.group_id) VVR\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n");
		sql.append("   and tvmg.group_id = vvr.group_id(+)\n");

		sql.append("           		   AND TVO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("                   AND TVO.ORDER_STATUS = " + Constant.ORDER_STATUS_03 + "\n");
		sql.append("                   AND TVO.COMPANY_ID = " + companyId + "\n");

		if (!"".equals(orderYear) && orderYear != null) {
			sql.append("           AND TVO.ORDER_YEAR = " + orderYear + "\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			sql.append("           AND TVO.ORDER_WEEK = " + orderWeek + "\n");
		}
		if (!"-1".equals(areaId) && !"".equals(areaId) && areaId != null) {
			sql.append("           AND TVO.AREA_ID = " + areaId + "\n");
		}
		if ("-1".equals(areaId) && !"".equals(area) && area != null) {
			sql.append("           AND TVO.AREA_ID IN( " + area + ")\n");
		}
		sql.append("                 GROUP BY TVMG.GROUP_ID,\n");
		sql.append("                          TVMG.GROUP_CODE,\n");
		sql.append("                          TVMG.GROUP_NAME,\n");
		sql.append("                          TVO.ORDER_YEAR,\n");
		sql.append("                          TVO.ORDER_WEEK,VVR.STOCK_AMOUNT) T1,\n");
		sql.append("               (SELECT G.GROUP_ID,\n");
		sql.append("                       TVQ.QUOTA_YEAR,\n");
		sql.append("                       TVQ.QUOTA_WEEK,\n");
		sql.append("                       SUM(NVL(TVQD.QUOTA_AMT, 0)) QUOTA_AMT\n");
		sql.append("                  FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("                       TT_VS_QUOTA_DETAIL     TVQD,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP G\n");
		sql.append("                 WHERE TVQD.GROUP_ID = G.GROUP_ID\n");
		sql.append("                   AND TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("                   AND TVQ.DEALER_ID IS NOT NULL\n");
		sql.append("                 GROUP BY G.GROUP_ID, TVQ.QUOTA_YEAR, TVQ.QUOTA_WEEK) T2\n");
		sql.append("         WHERE T1.GROUP_ID = T2.GROUP_ID\n");
		sql.append("           AND T2.QUOTA_YEAR = T1.ORDER_YEAR\n");
		sql.append("           AND T2.QUOTA_WEEK = T1.ORDER_WEEK) T\n");
		sql.append("LEFT JOIN (SELECT B.YEAR,\n");
		sql.append("                  B.WEEK,\n");
		sql.append("                  B.GROUP_ID,\n");
		sql.append("                  b.plan_amount,\n");
		sql.append("                  (B.PLAN_AMOUNT - NVL(A.CHECK_AMOUNT, 0)) AS AMOUNT\n");
		sql.append("             FROM (SELECT A.ORDER_YEAR AS YEAR,\n");
		sql.append("                          A.ORDER_WEEK AS WEEK,\n");
		sql.append("                          C.GROUP_ID,\n");
		sql.append("                          NVL(SUM(B.CHECK_AMOUNT), 0) CHECK_AMOUNT\n");
		sql.append("                     FROM TT_VS_ORDER              A,\n");
		sql.append("                          TT_VS_ORDER_DETAIL       B,\n");
		sql.append("                          TM_VHCL_MATERIAL_GROUP_R C\n");
		sql.append("                    WHERE A.ORDER_ID = B.ORDER_ID\n");
		sql.append("                      AND B.MATERIAL_ID = C.MATERIAL_ID\n");
		sql.append("                      AND A.ORDER_TYPE = ").append(Constant.ORDER_TYPE_01).append("\n");
		sql.append("                    GROUP BY A.ORDER_YEAR, A.ORDER_WEEK, C.GROUP_ID\n");
		sql.append("                    ORDER BY A.ORDER_WEEK ASC) A,\n");
		sql.append("\n");
		sql.append("                  (SELECT A.PLAN_YEAR AS YEAR,\n");
		sql.append("                          A.PLAN_WEEK AS WEEK,\n");
		sql.append("                          B.GROUP_ID,\n");
		sql.append("                          NVL(SUM(B.PLAN_AMOUNT), 0) PLAN_AMOUNT\n");
		sql.append("                     FROM TT_VS_PRODUCTION_PLAN        A,\n");
		sql.append("                          TT_VS_PRODUCTION_PLAN_DETAIL B\n");
		sql.append("                    WHERE A.PLAN_ID = B.PLAN_ID\n");
		sql.append("                    GROUP BY A.PLAN_YEAR, A.PLAN_WEEK, B.GROUP_ID\n");
		sql.append("                    ORDER BY A.PLAN_WEEK ASC) B\n");
		sql.append("            WHERE A.YEAR(+) = B.YEAR\n");
		sql.append("              AND A.WEEK(+) = B.WEEK\n");
		sql.append("              AND A.GROUP_ID(+) = B.GROUP_ID) M\n");
		sql.append("  ON M.YEAR = T.ORDER_YEAR\n");
		sql.append(" AND M.WEEK = T.ORDER_WEEK\n");
		sql.append(" AND M.GROUP_ID = T.GROUP_ID");

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * Function : 常规订单审核查询---配置层面
	 * 
	 * @param :
	 *            订单年
	 * @param :
	 *            订单周
	 * @param :
	 *            业务范围ID
	 * @param :
	 *            业务范围
	 * @param :
	 *            配置ID
	 * @return : 配置层面汇总信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	public List<Map<String, Object>> getGeneralOrderGroupDetail(String orderYear, String orderWeek, String areaId, String area, String groupId, Long companyId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.GROUP_ID, ---物料组ID\n");
		sql.append("       T.GROUP_CODE, ---物料组代码\n");
		sql.append("       T.GROUP_NAME, ---物料组名称\n");
		sql.append("       T.ORDER_YEAR, ---订单年\n");
		sql.append("       T.ORDER_WEEK, ---订单周\n");
		sql.append("       T.QUOTA_AMT, ---配额数量\n");
		sql.append("       T.ORDER_AMOUNT, ---订单数量\n");
		sql.append("       T.CHECK_AMOUNT, ---审核数量\n");
		sql.append("       T.STOCK_AMOUNT, ---当前库存\n");
		sql.append("       NVL(M.PLAN_AMOUNT, 0) PLAN_AMOUNT,\n");
		sql.append("       NVL(M.AMOUNT, 0) AMOUNT\n");
		sql.append("  FROM (SELECT T1.GROUP_ID,\n");
		sql.append("               T1.GROUP_CODE,\n");
		sql.append("               T1.GROUP_NAME,\n");
		sql.append("               T1.ORDER_YEAR,\n");
		sql.append("               T1.ORDER_WEEK,\n");
		sql.append("               T1.ORDER_AMOUNT,\n");
		sql.append("               T1.CHECK_AMOUNT,\n");
		sql.append("               T1.STOCK_AMOUNT,\n");
		sql.append("               T2.QUOTA_AMT\n");
		sql.append("          FROM (SELECT TVMG.GROUP_ID,\n");
		sql.append("                       TVMG.GROUP_CODE,\n");
		sql.append("                       TVMG.GROUP_NAME,\n");
		sql.append("                       TVO.ORDER_YEAR,\n");
		sql.append("                       TVO.ORDER_WEEK,\n");
		sql.append("                       NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sql.append("                       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("                       NVL(VVR.STOCK_AMOUNT, 0) STOCK_AMOUNT\n");
		sql.append("                  FROM TT_VS_ORDER              TVO, ---订单表\n");
		sql.append("                       TT_VS_ORDER_DETAIL       TVOD, ---订单明细表\n");
		sql.append("                       TM_VHCL_MATERIAL         TVM, ---物料表\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP   TVMG, ---物料组表\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP_R TVMGR, ---物料与物料组关系表\n");
		sql.append("					   (SELECT tvmgr.group_id, sum(TEMP.STOCK_AMOUNT) STOCK_AMOUNT\n");
		sql.append("   						  FROM VW_VS_RESOURCE TEMP, tm_vhcl_material_group_r tvmgr\n");
		sql.append("                         WHERE temp.material_id = tvmgr.material_id and TEMP.company_id = " + companyId + "\n");
		sql.append("                           AND TEMP.SPECIAL_BATCH_NO IS NULL group by tvmgr.group_id) VVR\n");
		sql.append("                 WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("                   AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("                   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("                   AND TVMG.GROUP_ID = VVR.group_id(+)\n");
		sql.append("                   AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n");
		sql.append("           		   AND TVO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("                   AND TVO.ORDER_STATUS = " + Constant.ORDER_STATUS_03 + "\n");
		sql.append("                   AND TVO.COMPANY_ID = " + companyId + "\n");
		if (!"".equals(groupId) && groupId != null) {
			sql.append("           AND TVMG.GROUP_ID = " + groupId + "\n");
		}
		if (!"".equals(orderYear) && orderYear != null) {
			sql.append("           AND TVO.ORDER_YEAR = " + orderYear + "\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			sql.append("           AND TVO.ORDER_WEEK = " + orderWeek + "\n");
		}
		if (!"-1".equals(areaId) && !"".equals(areaId) && areaId != null) {
			sql.append("           AND TVO.AREA_ID = " + areaId + "\n");
		}
		if ("-1".equals(areaId) && !"".equals(area) && area != null) {
			sql.append("           AND TVO.AREA_ID IN( " + area + ")\n");
		}
		sql.append("                 GROUP BY TVMG.GROUP_ID,\n");
		sql.append("                          TVMG.GROUP_CODE,\n");
		sql.append("                          TVMG.GROUP_NAME,\n");
		sql.append("                          TVO.ORDER_YEAR,\n");
		sql.append("                          TVO.ORDER_WEEK,vvr.STOCK_AMOUNT) T1,\n");
		sql.append("               (SELECT G.GROUP_ID,\n");
		sql.append("                       TVQ.QUOTA_YEAR,\n");
		sql.append("                       TVQ.QUOTA_WEEK,\n");
		sql.append("                       SUM(NVL(TVQD.QUOTA_AMT, 0)) QUOTA_AMT\n");
		sql.append("                  FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("                       TT_VS_QUOTA_DETAIL     TVQD,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP G\n");
		sql.append("                 WHERE TVQD.GROUP_ID = G.GROUP_ID\n");
		sql.append("                   AND TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("                   AND TVQ.DEALER_ID IS NOT NULL\n");
		sql.append("                 GROUP BY G.GROUP_ID, TVQ.QUOTA_YEAR, TVQ.QUOTA_WEEK) T2\n");
		sql.append("         WHERE T1.GROUP_ID = T2.GROUP_ID\n");
		sql.append("           AND T2.QUOTA_YEAR = T1.ORDER_YEAR\n");
		sql.append("           AND T2.QUOTA_WEEK = T1.ORDER_WEEK) T\n");
		sql.append("LEFT JOIN (SELECT B.YEAR,\n");
		sql.append("                   B.WEEK,\n");
		sql.append("                   B.GROUP_ID,\n");
		sql.append("                   b.plan_amount,\n");
		sql.append("                   (B.PLAN_AMOUNT - NVL(A.CHECK_AMOUNT, 0)) AS AMOUNT\n");
		sql.append("              FROM (SELECT A.ORDER_YEAR AS YEAR,\n");
		sql.append("                           A.ORDER_WEEK AS WEEK,\n");
		sql.append("                           C.GROUP_ID,\n");
		sql.append("                           NVL(SUM(B.CHECK_AMOUNT), 0) CHECK_AMOUNT\n");
		sql.append("                      FROM TT_VS_ORDER              A,\n");
		sql.append("                           TT_VS_ORDER_DETAIL       B,\n");
		sql.append("                           TM_VHCL_MATERIAL_GROUP_R C\n");
		sql.append("                     WHERE A.ORDER_ID = B.ORDER_ID\n");
		sql.append("                       AND B.MATERIAL_ID = C.MATERIAL_ID\n");
		sql.append("                       AND A.ORDER_TYPE = ").append(Constant.ORDER_TYPE_01).append("\n");
		sql.append("                     GROUP BY A.ORDER_YEAR, A.ORDER_WEEK, C.GROUP_ID\n");
		sql.append("                     ORDER BY A.ORDER_WEEK ASC) A,\n");
		sql.append("\n");
		sql.append("                   (SELECT A.PLAN_YEAR AS YEAR,\n");
		sql.append("                           A.PLAN_WEEK AS WEEK,\n");
		sql.append("                           B.GROUP_ID,\n");
		sql.append("                           NVL(SUM(B.PLAN_AMOUNT), 0) PLAN_AMOUNT\n");
		sql.append("                      FROM TT_VS_PRODUCTION_PLAN        A,\n");
		sql.append("                           TT_VS_PRODUCTION_PLAN_DETAIL B\n");
		sql.append("                     WHERE A.PLAN_ID = B.PLAN_ID\n");
		sql.append("                     GROUP BY A.PLAN_YEAR, A.PLAN_WEEK, B.GROUP_ID\n");
		sql.append("                     ORDER BY A.PLAN_WEEK ASC) B\n");
		sql.append("             WHERE A.YEAR(+) = B.YEAR\n");
		sql.append("               AND A.WEEK(+) = B.WEEK\n");
		sql.append("               AND A.GROUP_ID(+) = B.GROUP_ID) M\n");
		sql.append("   ON M.YEAR = T.ORDER_YEAR\n");
		sql.append("  AND M.WEEK = T.ORDER_WEEK\n");
		sql.append("  AND M.GROUP_ID = T.GROUP_ID");

		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName());
		return ps;
	}

	/**
	 * Function : 常规订单审核查询---配置--经销商层面
	 * 
	 * @param :
	 *            订单年
	 * @param :
	 *            订单周
	 * @param :
	 *            业务范围ID
	 * @param :
	 *            业务范围
	 * @param :
	 *            配置ID
	 * @return : 配置层面汇总信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	public PageResult<Map<String, Object>> getGeneralOrderDlrCheckList(String orderYear, String orderWeek, String areaId, String area, String groupId, Long companyId, int curPage, int pageSize) throws Exception {

		TmBusinessParaPO para = getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.DEALER_ID,\n");
		sql.append("       T.DEALER_CODE,\n");
		sql.append("       T.DEALER_NAME,\n");
		sql.append("       T.GROUP_ID,\n");
		sql.append("       T.GROUP_CODE,\n");
		sql.append("       T.GROUP_NAME,\n");
		sql.append("       T.QUOTA_AMT,\n");
		sql.append("       T.ORDER_AMOUNT,\n");
		sql.append("       T.CHECK_AMOUNT,\n");
		sql.append("       ROUND(T.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT\n");
		// sql.append(" NVL(TVQM.MIN_AMOUNT, 0) MIN_AMOUNT\n" );
		sql.append("  FROM (\n");
		sql.append("SELECT T1.GROUP_ID,\n");
		sql.append("       T1.DEALER_NAME,\n");
		sql.append("       T1.DEALER_CODE,\n");
		sql.append("       T1.DEALER_ID,\n");
		sql.append("       T1.GROUP_CODE,\n");
		sql.append("       T1.GROUP_NAME,\n");
		sql.append("       T1.ORDER_YEAR,\n");
		sql.append("       T1.ORDER_WEEK,\n");
		sql.append("       T1.ORDER_AMOUNT,\n");
		sql.append("       T1.CHECK_AMOUNT,\n");
		sql.append("       T2.QUOTA_AMT\n");
		sql.append("  FROM (SELECT TVMG.GROUP_ID,\n");
		sql.append("               TMD.DEALER_CODE,\n");
		sql.append("               TMD.DEALER_ID,\n");
		sql.append("               TMD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("               TVO.ORDER_ORG_ID,\n");
		sql.append("               TVMG.GROUP_CODE,\n");
		sql.append("               TVMG.GROUP_NAME,\n");
		sql.append("               TVO.ORDER_YEAR,\n");
		sql.append("               TVO.ORDER_WEEK,\n");
		sql.append("               NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sql.append("               NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER              TVO, ---订单表\n");
		sql.append("               TT_VS_ORDER_DETAIL       TVOD, ---订单明细表\n");
		sql.append("               TM_VHCL_MATERIAL         TVM, ---物料表\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG, ---物料组表\n");
		sql.append("               TM_DEALER                TMD, ---经销商表\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR ---物料与物料组关系表\n");
		sql.append("         WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("           AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("           AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("           AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n");
		sql.append("           AND TVO.ORDER_ORG_ID = TMD.DEALER_ID\n");
		sql.append("           AND TVO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TVO.ORDER_STATUS = " + Constant.ORDER_STATUS_03 + "\n");
		sql.append("           AND TVO.COMPANY_ID = " + companyId + "\n");
		if (!"".equals(groupId) && groupId != null)
			sql.append("           AND TVMG.GROUP_ID = " + groupId + "\n");
		if (!"".equals(orderYear) && orderYear != null) {
			sql.append("           AND TVO.ORDER_YEAR = " + orderYear + "\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			sql.append("           AND TVO.ORDER_WEEK = " + orderWeek + "\n");
		}
		if (!"-1".equals(areaId) && !"".equals(areaId) && areaId != null) {
			sql.append("           AND TVO.AREA_ID = " + areaId + "\n");
		}
		if ("-1".equals(areaId) && !"".equals(area) && area != null) {
			sql.append("           AND TVO.AREA_ID IN( " + area + ")\n");
		}
		sql.append("         GROUP BY TVMG.GROUP_ID,\n");
		sql.append("                  TVMG.GROUP_CODE,\n");
		sql.append("                  TVMG.GROUP_NAME,\n");
		sql.append("                  TMD.DEALER_ID,\n");
		sql.append("                  TMD.DEALER_CODE,\n");
		sql.append("                  TMD.DEALER_SHORTNAME,\n");
		sql.append("                  TVO.ORDER_YEAR,\n");
		sql.append("                  TVO.ORDER_WEEK,\n");
		sql.append("                  ORDER_ORG_ID\n");
		sql.append("                  ) T1,\n");
		sql.append("       (SELECT G.GROUP_ID,TVQ.QUOTA_YEAR,TVQ.QUOTA_WEEK,\n");
		sql.append("               SUM(NVL(TVQD.QUOTA_AMT, 0)) QUOTA_AMT,\n");
		sql.append("               TVQ.DEALER_ID\n");
		sql.append("          FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("               TT_VS_QUOTA_DETAIL     TVQD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP G\n");
		sql.append("         WHERE TVQD.GROUP_ID = G.GROUP_ID\n");
		sql.append("           AND TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("           AND TVQ.DEALER_ID IS NOT NULL\n");
		sql.append("         GROUP BY G.GROUP_ID,TVQ.QUOTA_YEAR,TVQ.QUOTA_WEEK, TVQ.DEALER_ID) T2\n");
		sql.append(" WHERE T1.GROUP_ID = T2.GROUP_ID\n");
		sql.append("   AND T1.ORDER_ORG_ID = T2.DEALER_ID");
		sql.append("   AND T2.QUOTA_YEAR = T1.ORDER_YEAR\n");
		sql.append("   AND T2.QUOTA_WEEK = T1.ORDER_WEEK\n");

		sql.append(" ) T\n");

		sql.append("  LEFT JOIN TT_VS_QUOTA_MIN TVQM ON T.DEALER_ID = TVQM.DEALER_ID\n");
		sql.append("                                AND T.GROUP_ID = TVQM.GROUP_ID\n");
		sql.append("                                AND T.ORDER_YEAR = TVQM.QUOTA_YEAR\n");
		sql.append("                                AND T.ORDER_WEEK = TVQM.QUOTA_WEEK");

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * Function : 常规订单审核查询---配置--经销商层面
	 * 
	 * @param :
	 *            订单年
	 * @param :
	 *            订单周
	 * @param :
	 *            业务范围ID
	 * @param :
	 *            业务范围
	 * @param :
	 *            配置ID
	 * @param :
	 *            经销商ID
	 * @return : 配置经销商层面汇总信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	public List<Map<String, Object>> getGeneralOrderDlrList(String orderYear, String orderWeek, String areaId, String area, String groupId, String dealerId, Long companyId) throws Exception {
		TmBusinessParaPO para = getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT T.DEALER_ID,\n");
		sql.append("       T.DEALER_CODE,\n");
		sql.append("       T.DEALER_NAME,\n");
		sql.append("       T.GROUP_ID,\n");
		sql.append("       T.GROUP_CODE,\n");
		sql.append("       T.GROUP_NAME,\n");
		sql.append("       T.QUOTA_AMT,\n");
		sql.append("       T.ORDER_AMOUNT,\n");
		sql.append("       T.CHECK_AMOUNT,\n");
		sql.append("       T.STOCK_AMOUNT,\n");
		sql.append("       ROUND(T.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT\n");
		// /sql.append(" NVL(TVQM.MIN_AMOUNT, 0) MIN_AMOUNT\n" );
		sql.append("  FROM (\n");

		sql.append("SELECT T1.GROUP_ID,\n");
		sql.append("       T1.DEALER_ID,\n");
		sql.append("       T1.DEALER_CODE,\n");
		sql.append("       T1.DEALER_NAME,\n");
		sql.append("       T1.GROUP_CODE,\n");
		sql.append("       T1.GROUP_NAME,\n");
		sql.append("       T1.ORDER_YEAR,\n");
		sql.append("       T1.ORDER_WEEK,\n");
		sql.append("       T1.ORDER_AMOUNT,\n");
		sql.append("       T1.CHECK_AMOUNT,\n");
		sql.append("       T1.STOCK_AMOUNT,\n");
		sql.append("       T2.QUOTA_AMT\n");
		sql.append("  FROM (SELECT TVMG.GROUP_ID,\n");
		sql.append("               TMD.DEALER_CODE,\n");
		sql.append("               TMD.DEALER_ID,\n");
		sql.append("               TMD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("               TVO.ORDER_ORG_ID,\n");
		sql.append("               TVMG.GROUP_CODE,\n");
		sql.append("               TVMG.GROUP_NAME,\n");
		sql.append("               TVO.ORDER_YEAR,\n");
		sql.append("               TVO.ORDER_WEEK,\n");
		sql.append("               NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sql.append("               NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("               NVL(SUM(VVR.STOCK_AMOUNT), 0) STOCK_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER              TVO, ---订单表\n");
		sql.append("               TT_VS_ORDER_DETAIL       TVOD, ---订单明细表\n");
		sql.append("               TM_VHCL_MATERIAL         TVM, ---物料表\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG, ---物料组表\n");
		sql.append("               TM_DEALER                TMD, ---经销商表\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR, ---物料与物料组关系表\n");
		sql.append("			   (SELECT TEMP.material_id, sum(TEMP.STOCK_AMOUNT) STOCK_AMOUNT\n");
		sql.append("   				  FROM VW_VS_RESOURCE TEMP\n");
		sql.append("                 WHERE TEMP.company_id = " + companyId + "\n");
		sql.append("                   AND TEMP.SPECIAL_BATCH_NO IS NULL group by TEMP.material_id) VVR\n");
		sql.append("         WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("           AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("           AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("           AND TVM.MATERIAL_ID = VVR.material_id(+)\n");
		sql.append("           AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n");
		sql.append("           AND TVO.ORDER_ORG_ID = TMD.DEALER_ID\n");
		sql.append("           AND TVO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TVO.ORDER_STATUS = " + Constant.ORDER_STATUS_03 + "\n");
		sql.append("           AND TVO.COMPANY_ID = " + companyId + "\n");
		if (!"".equals(dealerId) && dealerId != null) {
			sql.append("           AND TMD.DEALER_ID = " + dealerId + "\n");
		}
		if (!"".equals(groupId) && groupId != null)
			sql.append("           AND TVMG.GROUP_ID = " + groupId + "\n");
		if (!"".equals(orderYear) && orderYear != null) {
			sql.append("           AND TVO.ORDER_YEAR = " + orderYear + "\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			sql.append("           AND TVO.ORDER_WEEK = " + orderWeek + "\n");
		}
		if (!"-1".equals(areaId) && !"".equals(areaId) && areaId != null) {
			sql.append("           AND TVO.AREA_ID = " + areaId + "\n");
		}
		if ("-1".equals(areaId) && !"".equals(area) && area != null) {
			sql.append("           AND TVO.AREA_ID IN( " + area + ")\n");
		}
		sql.append("         GROUP BY TVMG.GROUP_ID,\n");
		sql.append("                  TVMG.GROUP_CODE,\n");
		sql.append("                  TVMG.GROUP_NAME,\n");
		sql.append("                  TMD.DEALER_ID,\n");
		sql.append("                  TMD.DEALER_CODE,\n");
		sql.append("                  TMD.DEALER_SHORTNAME,\n");
		sql.append("                  TVO.ORDER_YEAR,\n");
		sql.append("                  TVO.ORDER_WEEK,\n");
		sql.append("                  ORDER_ORG_ID\n");
		sql.append("                  ) T1,\n");
		sql.append("       (SELECT G.GROUP_ID,TVQ.QUOTA_YEAR,TVQ.QUOTA_WEEK,\n");
		sql.append("               SUM(NVL(TVQD.QUOTA_AMT, 0)) QUOTA_AMT,\n");
		sql.append("               TVQ.DEALER_ID\n");
		sql.append("          FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("               TT_VS_QUOTA_DETAIL     TVQD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP G\n");
		sql.append("         WHERE TVQD.GROUP_ID = G.GROUP_ID\n");
		sql.append("           AND TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("           AND TVQ.DEALER_ID IS NOT NULL\n");
		sql.append("         GROUP BY G.GROUP_ID,TVQ.QUOTA_YEAR,TVQ.QUOTA_WEEK, TVQ.DEALER_ID) T2\n");
		sql.append(" WHERE T1.GROUP_ID = T2.GROUP_ID\n");
		sql.append("   AND T1.ORDER_ORG_ID = T2.DEALER_ID");
		sql.append("   AND T2.QUOTA_YEAR = T1.ORDER_YEAR\n");
		sql.append("   AND T2.QUOTA_WEEK = T1.ORDER_WEEK\n");

		sql.append(" ) T\n");

		sql.append("  LEFT JOIN TT_VS_QUOTA_MIN TVQM ON T.DEALER_ID = TVQM.DEALER_ID\n");
		sql.append("                                AND T.GROUP_ID = TVQM.GROUP_ID\n");
		sql.append("                                AND T.ORDER_YEAR = TVQM.QUOTA_YEAR\n");
		sql.append("                                AND T.ORDER_WEEK = TVQM.QUOTA_WEEK");
		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName());
		return ps;
	}

	/**
	 * Function : 常规订单审核查询---物料--经销商层面
	 * 
	 * @param :
	 *            订单年
	 * @param :
	 *            订单周
	 * @param :
	 *            业务范围ID
	 * @param :
	 *            业务范围
	 * @param :
	 *            配置ID
	 * @param :
	 *            经销商ID
	 * @return : 物料层面汇总信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	public PageResult<Map<String, Object>> getGeneralOrderDetailDlrList(String orderYear, String orderWeek, String areaId, String area, String groupId, String dealerId, Long companyId, int curPage, int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVM.MATERIAL_ID,\n");
		sql.append("       TVO.ORDER_ID,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TVOD.DETAIL_ID,\n");
		sql.append("       NVL(TVOD.VER,0) VER,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TVM.COLOR_NAME,\n");
		sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO, ---订单表\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD, ---订单明细表\n");
		sql.append("       TM_VHCL_MATERIAL         TVM, ---物料表\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG, ---物料组表\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR, ---物料与物料组关系表\n");
		sql.append("       TM_DEALER                TMD ---经销商表\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TVO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("   AND TVO.ORDER_STATUS = " + Constant.ORDER_STATUS_03 + "\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		if (!"".equals(dealerId) && dealerId != null) {
			sql.append("           AND TMD.DEALER_ID = " + dealerId + "\n");
		}
		if (!"".equals(groupId) && groupId != null)
			sql.append("           AND TVMG.GROUP_ID = " + groupId + "\n");
		if (!"".equals(orderYear) && orderYear != null) {
			sql.append("           AND TVO.ORDER_YEAR = " + orderYear + "\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			sql.append("           AND TVO.ORDER_WEEK = " + orderWeek + "\n");
		}
		if (!"-1".equals(areaId) && !"".equals(areaId) && areaId != null) {
			sql.append("           AND TVO.AREA_ID = " + areaId + "\n");
		}
		if ("-1".equals(areaId) && !"".equals(area) && area != null) {
			sql.append("           AND TVO.AREA_ID IN( " + area + ")\n");
		}
		sql.append(" GROUP BY TVM.MATERIAL_ID,\n");
		sql.append("          TVO.ORDER_ID,\n");
		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TVOD.DETAIL_ID,\n");
		sql.append("          TVOD.VER,\n");
		sql.append("          TVM.MATERIAL_CODE,\n");
		sql.append("          TVM.MATERIAL_NAME,\n");
		sql.append("          TVM.COLOR_NAME");
		sql.append(" ORDER BY TVO.ORDER_NO");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * Function : 常规订单审核查询---订单ID查询
	 * 
	 * @param :
	 *            订单年
	 * @param :
	 *            订单周
	 * @param :
	 *            业务范围ID
	 * @param :
	 *            业务范围
	 * @return : 订单ID信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	public List<Map<String, Object>> getGeneralOrderIdList(String orderYear, String orderWeek, String areaId, String area) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVO.ORDER_ID,NVL(TVO.VER,0)VER\n");
		sql.append("  FROM TT_VS_ORDER TVO\n");
		sql.append(" WHERE 1=1\n");
		sql.append("   AND TVO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("   AND TVO.ORDER_STATUS = " + Constant.ORDER_STATUS_03 + "\n");
		if (!"".equals(orderYear) && orderYear != null) {
			sql.append("   AND TVO.ORDER_YEAR = " + orderYear + "\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			sql.append("   AND TVO.ORDER_WEEK = " + orderWeek + "\n");
		}
		if (!"-1".equals(areaId) && !"".equals(areaId) && areaId != null) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		if ("-1".equals(areaId) && !"".equals(area) && area != null) {
			sql.append("   AND TVO.AREA_ID IN( " + area + ")\n");
		}
		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName());
		return ps;
	}

	/**
	 * Function : 常规订单审核逐单审核查询
	 * 
	 * @param :
	 *            订单年
	 * @param :
	 *            订单周
	 * @param :
	 *            业务范围ID
	 * @param :
	 *            业务范围
	 * @return : 订单ID信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	public PageResult<Map<String, Object>> getGeneralOrderDetailList(String orderYear, String orderWeek, String areaId, String area, String dealerCode, String orderNo, Long companyId, int curPage, int pageSize) throws Exception {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVO.ORDER_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       NVL(TVO.VER,0) VER,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_YEAR,\n");
		sql.append("       TVO.ORDER_WEEK,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) - NVL(SUM(TVOD.CHECK_AMOUNT), 0) WAIT_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER TVO, TT_VS_ORDER_DETAIL TVOD, TM_DEALER TMD\n");
		sql.append(" WHERE TVO.ORDER_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("   AND TVO.ORDER_STATUS = " + Constant.ORDER_STATUS_03 + "\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		if (!"".equals(orderNo) && orderNo != null) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (!"".equals(orderYear) && orderYear != null) {
			sql.append("   AND TVO.ORDER_YEAR = " + orderYear + "\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			sql.append("   AND TVO.ORDER_WEEK = " + orderWeek + "\n");
		}
		if (!"-1".equals(areaId) && !"".equals(areaId) && areaId != null) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		if ("-1".equals(areaId) && !"".equals(area) && area != null) {
			sql.append("   AND TVO.AREA_ID IN( " + area + ")\n");
		}
		if (!"".equals(dealerCode) && dealerCode != null) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		sql.append(" GROUP BY TVO.ORDER_ID,\n");
		sql.append("          TMD.DEALER_CODE,\n");
		sql.append("          TMD.DEALER_SHORTNAME,\n");
		sql.append("          TVO.ORDER_NO,\n");
		sql.append("       	  TVO.VER,\n");
		sql.append("          TVO.RAISE_DATE,\n");
		sql.append("          TVO.ORDER_YEAR,\n");
		sql.append("          TVO.ORDER_WEEK,\n");
		sql.append("          TVO.ORDER_TYPE");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * Function : 常规订单审核逐单审核查询
	 * 
	 * @param :
	 *            订单年
	 * @param :
	 *            订单周
	 * @param :
	 *            业务范围ID
	 * @param :
	 *            业务范围
	 * @return : 订单ID信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	public Map<String, Object> getGeneralOrderInfo(String orderId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVO.ORDER_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TVO.VER,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_YEAR,\n");
		sql.append("       TVO.ORDER_WEEK,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) - NVL(SUM(TVOD.CHECK_AMOUNT), 0) WAIT_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER TVO, TT_VS_ORDER_DETAIL TVOD, TM_DEALER TMD\n");
		sql.append(" WHERE TVO.ORDER_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		if (!"".equals(orderId) && orderId != null) {
			sql.append("   AND TVO.ORDER_ID = " + orderId + "\n");
		}
		sql.append(" GROUP BY TVO.ORDER_ID,\n");
		sql.append("          TMD.DEALER_CODE,\n");
		sql.append("          TMD.DEALER_SHORTNAME,\n");
		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TVO.VER,\n");
		sql.append("          TVO.RAISE_DATE,\n");
		sql.append("          TVO.ORDER_YEAR,\n");
		sql.append("          TVO.ORDER_WEEK,\n");
		sql.append("          TVO.ORDER_TYPE");
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}

	/**
	 * Function : 常规订单审核逐单审核查询
	 * 
	 * @param :
	 *            订单年
	 * @param :
	 *            订单周
	 * @param :
	 *            业务范围ID
	 * @param :
	 *            业务范围
	 * @return : 订单ID信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	public List<Map<String, Object>> getGeneralOrderDetailQuery(String orderId, String companyId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T2.DETAIL_ID,\n");
		sql.append("       T2.SERIES_NAME,\n");
		sql.append("       T2.MATERIAL_ID,\n");
		sql.append("       T2.MATERIAL_CODE,\n");
		sql.append("       T2.MATERIAL_NAME,\n");
		sql.append("       T2.COLOR_NAME,\n");
		sql.append("       T2.VER,\n");
		sql.append("       T2.ORDER_AMOUNT,\n");
		sql.append("       T2.CHECK_AMOUNT,\n");
		sql.append("       T2.STOCK_AMOUNT,\n");
		sql.append("       T2.WAIT_AMOUNT,\n");
		sql.append("       NVL(TVR.AMOUNT, 0) AMOUNT\n");
		sql.append("  FROM (SELECT T1.*, R.GROUP_ID\n");
		sql.append("          FROM (SELECT TVOD.DETAIL_ID,\n");
		sql.append("                       TVOD.MATERIAL_ID,\n");
		sql.append("                       TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("                       TVO.ORDER_YEAR,\n");
		sql.append("                       TVO.ORDER_WEEK,\n");
		sql.append("                       TVO.AREA_ID,\n");
		sql.append("                       TVM.MATERIAL_CODE,\n");
		sql.append("                       TVM.MATERIAL_NAME,\n");
		sql.append("                       TVM.COLOR_NAME,\n");
		sql.append("                       NVL(TVOD.VER, 0) VER,\n");
		sql.append("                       NVL(TVOD.ORDER_AMOUNT, 0) ORDER_AMOUNT,\n");
		sql.append("                       NVL(TVOD.CHECK_AMOUNT, 0) CHECK_AMOUNT,\n");
		sql.append("NVL(VVR.STOCK_AMOUNT, 0) -\n");
		sql.append("                       (SELECT NVL(SUM(TVORR.AMOUNT), 0) -\n");
		sql.append("                               NVL(SUM(TVORR.DELIVERY_AMOUNT), 0)\n");
		sql.append("                          FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR\n");
		sql.append("                         WHERE TVORR.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("                           AND LENGTH(TVORR.BATCH_NO) = 4\n");
		sql.append("                           AND TVORR.RESERVE_STATUS = " + Constant.RESOURCE_RESERVE_STATUS_01 + ") STOCK_AMOUNT,\n");
		sql.append("                       NVL(TVOD.ORDER_AMOUNT, 0) - NVL(TVOD.CHECK_AMOUNT, 0) WAIT_AMOUNT\n");
		sql.append("                  FROM TT_VS_ORDER              TVO,\n");
		sql.append("                       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("                       TM_VHCL_MATERIAL         TVM,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP   TVMG1,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP   TVMG2,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP   TVMG3,\n");
		sql.append("					   (SELECT TEMP.material_id, sum(TEMP.STOCK_AMOUNT) STOCK_AMOUNT\n");
		sql.append("   						  FROM VW_VS_RESOURCE TEMP\n");
		sql.append("                         WHERE TEMP.company_id = " + companyId + "\n");
		sql.append("                           AND TEMP.SPECIAL_BATCH_NO IS NULL\n");
		sql.append("                           GROUP BY TEMP.material_id) VVR\n");
		sql.append("                 WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("                   AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("                   AND TVMGR.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("                   AND TVM.MATERIAL_ID = VVR.MATERIAL_ID(+)\n");
		sql.append("                   AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("                   AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("                   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("                   AND TVO.ORDER_ID = ").append(orderId).append("\n");
		sql.append("                   ) T1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R R\n");
		sql.append("         WHERE T1.MATERIAL_ID = R.MATERIAL_ID) T2\n");
		sql.append("  LEFT JOIN (SELECT B.YEAR,\n");
		sql.append("                    B.WEEK,\n");
		sql.append("                    B.GROUP_ID,\n");
		sql.append("                    (B.PLAN_AMOUNT - NVL(A.CHECK_AMOUNT, 0)) AS AMOUNT\n");
		sql.append("               FROM (SELECT A.ORDER_YEAR AS YEAR,\n");
		sql.append("                            A.ORDER_WEEK AS WEEK,\n");
		sql.append("                            C.GROUP_ID,\n");
		sql.append("                            NVL(SUM(B.CHECK_AMOUNT), 0) CHECK_AMOUNT\n");
		sql.append("                       FROM TT_VS_ORDER              A,\n");
		sql.append("                            TT_VS_ORDER_DETAIL       B,\n");
		sql.append("                            TM_VHCL_MATERIAL_GROUP_R C\n");
		sql.append("                      WHERE A.ORDER_ID = B.ORDER_ID\n");
		sql.append("                        AND B.MATERIAL_ID = C.MATERIAL_ID\n");
		sql.append("                        AND A.ORDER_TYPE = ").append(Constant.ORDER_TYPE_01).append("\n");
		sql.append("                      GROUP BY A.ORDER_YEAR, A.ORDER_WEEK, C.GROUP_ID\n");
		sql.append("                      ORDER BY A.ORDER_WEEK ASC) A,\n");
		sql.append("\n");
		sql.append("                    (SELECT A.PLAN_YEAR AS YEAR,\n");
		sql.append("                            A.PLAN_WEEK AS WEEK,\n");
		sql.append("                            B.GROUP_ID,\n");
		sql.append("                            NVL(SUM(B.PLAN_AMOUNT), 0) PLAN_AMOUNT\n");
		sql.append("                       FROM TT_VS_PRODUCTION_PLAN        A,\n");
		sql.append("                            TT_VS_PRODUCTION_PLAN_DETAIL B\n");
		sql.append("                      WHERE A.PLAN_ID = B.PLAN_ID\n");
		sql.append("                      GROUP BY A.PLAN_YEAR, A.PLAN_WEEK, B.GROUP_ID\n");
		sql.append("                      ORDER BY A.PLAN_WEEK ASC) B\n");
		sql.append("              WHERE A.YEAR(+) = B.YEAR\n");
		sql.append("                AND A.WEEK(+) = B.WEEK\n");
		sql.append("                AND A.GROUP_ID(+) = B.GROUP_ID) TVR\n");
		sql.append("    ON TVR.YEAR = T2.ORDER_YEAR\n");
		sql.append("   AND TVR.WEEK = T2.ORDER_WEEK\n");
		sql.append("   AND TVR.GROUP_ID = T2.GROUP_ID");

		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName());
		return ps;
	}

	public Map<String, Object> getResource(String orderYear, String orderWeek, String materialId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVR.RESOURCE_ID, TVR.RESOURCE_AMOUNT, TVR.CHECK_AMOUNT\n");
		sql.append("  FROM TT_VS_RESOURCE TVR\n");
		sql.append(" WHERE TVR.MATERIAL_ID =" + materialId + "\n");
		sql.append("   AND TVR.RESOURCE_YEAR =" + orderYear + "\n");
		sql.append("   AND TVR.RESOURCE_WEEK =" + orderWeek + "\n");
		sql.append("   AND TVR.SPECIAL_BATCH_NO IS NULL");
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}

	/**
	 * Function : 补充订单审核查询
	 * 
	 * @param :
	 *            订单年
	 * @param :
	 *            订单周
	 * @param :
	 *            业务范围ID
	 * @param :
	 *            业务范围
	 * @param :
	 *            物料组CODE
	 * @param :
	 *            经销商CODE
	 * @param :
	 *            订单类型
	 * @param :
	 *            订单号码
	 * @return : 订单信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	public PageResult<Map<String, Object>> getNastyOrderCheckList(String orderYear, String orderWeek, String areaId, String area, String groupCode, String dealerCode, String orderType, String orderNo, Long companyId, int curPage, int pageSize) throws Exception {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVO.ORDER_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("       TMD2.DEALER_SHORTNAME DEALER_NAME2,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_YEAR,\n");
		sql.append("       TVO.ORDER_WEEK,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       NVL(TVO.VER,0) VER,\n");
		sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) - NVL(SUM(TVOD.CHECK_AMOUNT), 0) WAIT_CHECK\n");
		sql.append("  FROM TT_VS_ORDER TVO, TT_VS_ORDER_DETAIL TVOD, TM_DEALER TMD,TM_DEALER TMD2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TMD2.DEALER_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID =TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID");
		sql.append("   AND TVO.ORDER_STATUS = " + Constant.ORDER_STATUS_03 + "\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		if (!"".equals(orderType) && orderType != null) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		if (!"".equals(orderNo) && orderNo != null) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (!"".equals(orderYear) && orderYear != null) {
			sql.append("   AND TVO.ORDER_YEAR =" + orderYear + "\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			sql.append("   AND TVO.ORDER_WEEK = " + orderWeek + "\n");
		}
		if (!"-1".equals(areaId) && !"".equals(areaId) && areaId != null) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		if ("-1".equals(areaId) && !"".equals(area) && area != null) {
			sql.append("   AND TVO.AREA_ID IN( " + area + ")\n");
		}
		if (!"".equals(groupCode) && groupCode != null) {
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
		}
		if (!"".equals(dealerCode) && dealerCode != null) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		sql.append(" GROUP BY TVO.ORDER_ID,\n");
		sql.append("          TMD.DEALER_CODE,\n");
		sql.append("          TMD.DEALER_SHORTNAME,\n");
		sql.append("          TMD2.DEALER_SHORTNAME,\n");
		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TVO.RAISE_DATE,\n");
		sql.append("          TVO.ORDER_YEAR,\n");
		sql.append("          TVO.ORDER_WEEK,\n");
		sql.append("          TVO.ORDER_TYPE,\n");
		sql.append("          TVO.VER");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * Function : 集团客户代交车审核查询
	 * 
	 * @param :
	 *            订单年
	 * @param :
	 *            订单周
	 * @param :
	 *            业务范围ID
	 * @param :
	 *            业务范围
	 * @param :
	 *            物料组CODE
	 * @param :
	 *            经销商CODE
	 * @param :
	 *            订单类型
	 * @param :
	 *            订单号码
	 * @return : 订单信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	public PageResult<Map<String, Object>> getFleetOrderCheckList(String orgId, String startYear, String endYear, String startWeek, String endWeek, String areaId, String groupCode, String dealerCode, String orderType, String orderNo, Long companyId, String areaIds, int curPage, int pageSize) throws Exception {
		List<Object> params = new LinkedList<Object>();

		/*
		 * startWeek = startWeek.length() == 1 ? "0" + startWeek : startWeek;
		 * endWeek = endWeek.length() == 1 ? "0" + endWeek : endWeek;
		 */

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT DISTINCT TVDR.REQ_ID,\n");
		sql.append("       TVDR.DLVRY_REQ_NO,\n");
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_YEAR,\n");
		sql.append("       TVO.ORDER_WEEK,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       NVL(TVDR.VER, 0) VER,\n");
		sql.append("       TVDR.FLEET_ADDRESS\n");
		sql.append("  FROM TT_VS_DLVRY_REQ          TVDR,\n");
		sql.append("       TT_VS_ORDER              TVO,\n");
		sql.append("       TM_DEALER                TMD,\n");
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG\n");
		sql.append(" WHERE TVDR.ORDER_ID = TVO.ORDER_ID\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");
		sql.append("   AND TVDRD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVDR.REQ_STATUS = " + Constant.ORDER_REQ_STATUS_08 + "\n");
		sql.append("   AND (TVO.ORDER_STATUS = " + Constant.ORDER_STATUS_03 + " OR TVO.ORDER_STATUS = " + Constant.ORDER_STATUS_05 + ")\n");
		/*
		 * if(startYear!=null&&!"".equals(startYear)&&startWeek!=null&&!"".equals(startWeek)){
		 * sql.append(" AND TVO.ORDER_YEAR ||
		 * DECODE(LENGTH(TVO.ORDER_WEEK),\n"); sql.append(" 1,\n"); sql.append("
		 * '0' || TVO.ORDER_WEEK,\n"); sql.append(" TVO.ORDER_WEEK) >=
		 * "+startYear+startWeek+"\n"); }
		 * if(endYear!=null&&!"".equals(endYear)&&endWeek!=null&&!"".equals(endWeek)){
		 * sql.append(" AND TVO.ORDER_YEAR ||
		 * DECODE(LENGTH(TVO.ORDER_WEEK),\n"); sql.append(" 1,\n"); sql.append("
		 * '0' || TVO.ORDER_WEEK,\n"); sql.append(" TVO.ORDER_WEEK) <=
		 * "+endYear+endWeek+"\n"); }
		 */
		if (orderType != null && !"".equals(orderType)) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		if (orderNo != null && !"".equals(orderNo)) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (areaId != null && !"".equals(areaId)) {
			sql.append("   AND TVDR.AREA_ID = " + areaId + "\n");
		}
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
		}
		if (dealerCode != null && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		if (areaIds != null && !"".equals(areaIds)) {
			sql.append("   AND TVDR.AREA_ID IN (" + areaIds + ")\n");
		}

		if (!CommonUtils.isNullString(orgId)) {
			sql.append(" and exists (select 1 from tm_dealer_org_relation tdor where tdor.dealer_id = tmd.dealer_id and tdor.org_id = ").append(orgId).append(")\n");
		}

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * Function : 补充订单审核明细查询
	 * 
	 * @param :
	 *            订单ID
	 * @return : 订单信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-11
	 */
	public Map<String, Object> getOrderInfo(String orderId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TMD1.DEALER_SHORTNAME DEALER_NAME1,\n");
		sql.append("       TMD2.DEALER_SHORTNAME DEALER_NAME2,\n");
		sql.append("       TMD3.DEALER_SHORTNAME DEALER_NAME3,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       NVL(TVO.VER,0) VER,\n");
		sql.append("       TVO.ORDER_YEAR,\n");
		sql.append("       TVO.ORDER_WEEK,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       TVO.AREA_ID,\n");
		sql.append("       TVO.DELIVERY_TYPE,\n");
		sql.append("       TMF.FLEET_NAME,\n");
		sql.append("       TVO.FLEET_ADDRESS,\n");
		sql.append("       TVA.ADDRESS,\n");
		sql.append("       TCC1.CODE_DESC ORDER_TYPE_NAME,\n");
		sql.append("       TCC2.CODE_DESC DELIVERY_TYPE_NAME,\n");
		sql.append("       TVAT.TYPE_NAME,\n");
		sql.append("       TVO.REFIT_REMARK,\n");
		sql.append("       TVO.ORDER_REMARK,\n");
		sql.append("       TVO.FUND_TYPE_ID,\n");
		sql.append("       TVO.ORDER_PRICE,\n");
		sql.append("       TVO.PRICE_ID,\n");
		sql.append("       TVO.OTHER_PRICE_REASON,\n");
		sql.append("       TVO.RECEIVER,\n");
		sql.append("       TVO.LINK_MAN,\n");
		sql.append("       TVO.TEL,\n");
		sql.append("       TVO.DISCOUNT,\n");
		sql.append("       TVO.BILLING_ORG_ID,\n");
		sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT),0) - NVL(SUM(TVOD.CHECK_AMOUNT), 0) WAIT_CHECK\n");
		sql.append("  FROM TT_VS_ORDER        TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       TM_DEALER          TMD1,\n");
		sql.append("       TM_DEALER          TMD2,\n");
		sql.append("       TM_DEALER          TMD3,\n");
		sql.append("       TC_CODE            TCC1,\n");
		sql.append("       TC_CODE            TCC2,\n");
		sql.append("       TT_VS_ACCOUNT_TYPE TVAT,\n");
		sql.append("       TM_FLEET           TMF,\n");
		sql.append("       TM_VS_ADDRESS      TVA\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TMD1.DEALER_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TMD2.DEALER_ID\n");
		sql.append("   AND TVO.RECEIVER = TMD3.DEALER_ID(+)\n");
		sql.append("   AND TVO.ORDER_TYPE = TCC1.CODE_ID\n");
		sql.append("   AND TVO.FUND_TYPE_ID = TVAT.TYPE_ID\n");
		sql.append("   AND TVO.DELIVERY_TYPE = TCC2.CODE_ID\n");
		sql.append("   AND TVA.ID(+) = TVO.DELIVERY_ADDRESS\n");
		sql.append("   AND TMF.FLEET_ID(+) = TVO.FLEET_ID\n");
		if (!"".equals(orderId) && orderId != null) {
			sql.append("   AND TVO.ORDER_ID = " + orderId + "\n");
		}
		sql.append(" GROUP BY TMD1.DEALER_SHORTNAME,\n");
		sql.append("          TMD2.DEALER_SHORTNAME,\n");
		sql.append("          TMD3.DEALER_SHORTNAME,\n");
		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TVO.VER,\n");
		sql.append("          TVO.ORDER_YEAR,\n");
		sql.append("          TVO.ORDER_WEEK,\n");
		sql.append("          TVO.ORDER_TYPE,\n");
		sql.append("          TVO.AREA_ID,\n");
		sql.append("          TVO.DELIVERY_TYPE,\n");
		sql.append("          TMF.FLEET_NAME,\n");
		sql.append("          TVO.FLEET_ADDRESS,\n");
		sql.append("          TVA.ADDRESS,\n");
		sql.append("          TCC1.CODE_DESC,\n");
		sql.append("          TCC2.CODE_DESC,\n");
		sql.append("          TVAT.TYPE_NAME,\n");
		sql.append("          TVO.REFIT_REMARK,\n");
		sql.append("          TVO.ORDER_REMARK,\n");
		sql.append("       	  TVO.FUND_TYPE_ID,\n");
		sql.append("          TVO.ORDER_PRICE,\n");
		sql.append("          TVO.PRICE_ID,\n");
		sql.append("          TVO.OTHER_PRICE_REASON,\n");
		sql.append("          TVO.RECEIVER,\n");
		sql.append("          TVO.LINK_MAN,\n");
		sql.append("          TVO.TEL,\n");
		sql.append("       	  TVO.DISCOUNT,");
		sql.append("       	  TVO.BILLING_ORG_ID");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		Map<String, Object> map = new HashMap<String, Object>();
		if (list.size() > 0) {
			map = list.get(0);
		}
		return map;
	}

	/**
	 * Function : 补充订单审核订单明细物料查询
	 * 
	 * @param :
	 *            订单ID
	 * @return : 订单详细信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	public List<Map<String, Object>> getNastyOrderDetailList(String orderId, String orderType, String companyId) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT T.*, NVL(TVR.RESOURCE_AMOUNT, 0) RESOURCE_AMOUNT\n");
		sql.append("  FROM (SELECT TVOD.DETAIL_ID,\n");
		sql.append("               TVOD.MATERIAL_ID,\n");
		sql.append("               TVO.ORDER_ID,\n");
		sql.append("               TVO.ORDER_YEAR,\n");
		sql.append("               TVO.ORDER_WEEK,\n");
		sql.append("               TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("               TVM.MATERIAL_CODE,\n");
		sql.append("               TVM.MATERIAL_NAME,\n");
		sql.append("               TVOD.SPECIAL_BATCH_NO,\n");
		sql.append("               TVOD.SINGLE_PRICE,\n");
		sql.append("               TVOD.TOTAL_PRICE,\n");
		sql.append("               NVL(TVOD.VER,0) VER,\n");
		sql.append("               NVL(TVOD.ORDER_AMOUNT, 0) ORDER_AMOUNT,\n");
		sql.append("               NVL(TVOD.CHECK_AMOUNT, 0) CHECK_AMOUNT,\n");
		sql.append("               NVL(VVR.AVA_STOCK, 0) AVA_STOCK,\n");
		sql.append("               NVL(TVOD.ORDER_AMOUNT, 0) - NVL(TVOD.CHECK_AMOUNT, 0) WAIT_CHECK\n");
		sql.append("          FROM TT_VS_ORDER              TVO,\n");
		sql.append("               TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("               TM_VHCL_MATERIAL         TVM,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG3,\n");
		sql.append("			   (SELECT TEMP.material_id, TEMP.SPECIAL_BATCH_NO, TEMP.AVA_STOCK\n");
		sql.append("   				 FROM VW_VS_RESOURCE TEMP\n");
		sql.append("                WHERE TEMP.company_id = " + companyId + " \n");
		if (Integer.parseInt(orderType) != Constant.ORDER_TYPE_03.intValue()) {
			sql.append("           AND TEMP.SPECIAL_BATCH_NO IS NULL\n");
		}
		sql.append(") VVR\n");
		sql.append("         WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("           AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("           AND TVOD.MATERIAL_ID = VVR.MATERIAL_ID(+)\n");
		if (Integer.parseInt(orderType) == Constant.ORDER_TYPE_03.intValue()) {
			sql.append("           AND TVOD.SPECIAL_BATCH_NO = VVR.SPECIAL_BATCH_NO(+)\n");
		}
		sql.append("           AND TVMGR.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("           AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		if (!"".equals(orderId) && orderId != null) {
			sql.append("       AND TVO.ORDER_ID = " + orderId + "\n");
		}
		sql.append("  )T LEFT JOIN TT_VS_RESOURCE TVR ON T.MATERIAL_ID = TVR.MATERIAL_ID\n");
		sql.append("                              AND T.ORDER_YEAR = TVR.RESOURCE_YEAR\n");
		sql.append("                              AND T.ORDER_WEEK = TVR.RESOURCE_WEEK\n");
		if (!"".equals(orderType) && orderType != null && Integer.parseInt(orderType) == Constant.ORDER_TYPE_03.intValue()) {
			sql.append("                              AND T.SPECIAL_BATCH_NO = TVR.SPECIAL_BATCH_NO\n");
		} else {
			sql.append("                              AND TVR.SPECIAL_BATCH_NO IS NULL\n");
		}
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	/**
	 * Function : 补充订单审核审核日志查询
	 * 
	 * @param :
	 *            订单ID
	 * @return : 订单详细信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	public List<Map<String, Object>> getOrderCheckList(String orderId) throws Exception {
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
		if (!"".equals(orderId) && orderId != null) {
			sql.append("   AND TVOC.ORDER_ID = " + orderId + "\n");
		}
		sql.append(" ORDER BY TVOC.CHECK_DATE ASC");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	/**
	 * Function : 集团客户代交车审核明细查询
	 * 
	 * @param :
	 *            订单ID
	 * @return : 订单信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-11
	 */
	public Map<String, Object> getFleetOrderInfo(String reqId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVO.ORDER_NO,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       TVO.ORDER_ID,\n");
		sql.append("       TVDR.DLVRY_REQ_NO,\n");
		sql.append("       TBA.AREA_NAME,\n");
		sql.append("       TBA.AREA_ID,\n");
		sql.append("       TMF.FLEET_NAME,\n");
		sql.append("       TVDR.FLEET_ADDRESS,\n");
		sql.append("       TVAT.TYPE_NAME,\n");
		sql.append("       TVO.REFIT_REMARK,\n");
		sql.append("       TVO.PAY_REMARK,\n");
		sql.append("       TVDR.REQ_REMARK ORDER_REMARK,\n");
		sql.append("       TVA.AVAILABLE_AMOUNT,\n");
		sql.append("       TMD1.DEALER_NAME,\n");
		sql.append("       TMVA.ADDRESS,\n");
		sql.append("       TMVA.LINK_MAN,\n");
		sql.append("       TMVA.TEL,\n");
		sql.append("       TMVA.RECEIVE_ORG,\n");
		sql.append("       TVDR.DELIVERY_TYPE,\n");
		sql.append("       NVL(TVDR.VER, 0) VER\n");
		sql.append("  FROM TT_VS_ORDER        TVO,\n");
		sql.append("       TT_VS_DLVRY_REQ    TVDR,\n");
		sql.append("       TM_VS_ADDRESS      TMVA,\n");
		sql.append("       TM_DEALER          TMD1,\n");
		sql.append("       TM_DEALER          TMD,\n");
		sql.append("       TT_VS_ACCOUNT      TVA,\n");
		sql.append("       TT_VS_ACCOUNT_TYPE TVAT,\n");
		sql.append("       TM_BUSINESS_AREA   TBA,\n");
		sql.append("       TM_FLEET           TMF\n");
		sql.append(" WHERE TVO.ORDER_ID = TVDR.ORDER_ID\n");
		sql.append("   AND TVDR.RECEIVER = TMD1.DEALER_ID(+)\n");
		sql.append("   AND TVDR.ADDRESS_ID = TMVA.ID(+)\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TVA.DEALER_ID\n");
		sql.append("   AND TVA.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("   AND TMF.FLEET_ID = TVDR.FLEET_ID\n");
		sql.append("   AND TVDR.FUND_TYPE = TVA.ACCOUNT_TYPE_ID\n");
		sql.append("   AND TVA.ACCOUNT_TYPE_ID = TVAT.TYPE_ID\n");
		sql.append("   AND TBA.AREA_ID = TVDR.AREA_ID\n");
		sql.append("   AND TVDR.REQ_ID = " + reqId + "\n");
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}

	/**
	 * Function : 集团客户代交车审核订单明细物料查询
	 * 
	 * @param :
	 *            订单ID
	 * @return : 订单详细信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	public List<Map<String, Object>> getFleetOrderDetailList(String reqId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVMG3.GROUP_NAME,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TVDRD.REQ_AMOUNT,\n");
		sql.append("       TVDRD.SINGLE_PRICE,\n");
		sql.append("       (TVDRD.TOTAL_PRICE + TVDRD.DISCOUNT_PRICE) PRICE,\n");
		sql.append("       TVDRD.DISCOUNT_RATE,\n");
		sql.append("       TVDRD.DISCOUNT_S_PRICE,\n");
		sql.append("       TVDRD.DISCOUNT_PRICE,\n");
		sql.append("	   TVOD.DETAIL_ID ORDER_DETAIL_ID,\n");
		sql.append("	   TVDRD.DETAIL_ID,");
		// sql.append(" TVDRD.TOTAL_PRICE AS PRICE,\n" );
		sql.append("       TVDRD.PATCH_NO\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TT_VS_DLVRY_REQ          TVDR,\n");
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG3\n");
		sql.append(" WHERE TVO.ORDER_ID = TVDR.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");
		sql.append("   AND TVDRD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("   AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID\n");
		sql.append("   AND TVDR.REQ_ID = " + reqId + "\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	/**
	 * Function : 集团客户代交车审核订单明细经销商地址查询
	 * 
	 * @param :
	 *            经销商ID
	 * @return : 经销商地址信息
	 * @throws :
	 *             Exception LastUpdate : 2010-06-09
	 */
	public List<Map<String, Object>> getdealerAddress(String dealerCode) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVA.ID ADDRESS_ID, TVA.ADDRESS\n");
		sql.append("  FROM TM_VS_ADDRESS TVA,TM_DEALER TMD\n");
		sql.append(" WHERE TVA.DEALER_ID = TMD.DEALER_ID AND TMD.DEALER_CODE ='" + dealerCode + "'\n");
		sql.append("   AND TVA.STATUS =" + Constant.STATUS_ENABLE + "\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	/**
	 * Function : 查询资源PO
	 * 
	 * @param :
	 *            物料ID
	 * @param :
	 *            业务范围ID
	 * @param :
	 *            订单类型
	 * @param :
	 *            资源年
	 * @param :
	 *            资源周
	 * @return : 资源PO
	 * @throws :
	 *             Exception LastUpdate : 2010-06-21
	 */
	public Map<String, Object> getResourcePO(String materialId, String orderType, String resourceYear, String resourceWeek) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT *\n");
		sql.append("  FROM Tt_Vs_Resource\n");
		sql.append(" WHERE 1 = 1\n");
		if (!"".equals(materialId) && materialId != null) {
			sql.append("   AND Material_Id = " + materialId + "\n");
		}
		if (!"".equals(orderType) && orderType != null && Integer.parseInt(orderType) == Constant.ORDER_TYPE_02) {
			sql.append("   AND Special_Batch_No is null\n");
		}
		if (!"".equals(resourceYear) && resourceYear != null) {
			sql.append("   AND Resource_Year = " + resourceYear + "\n");
		}
		if (!"".equals(resourceWeek) && resourceWeek != null) {
			sql.append("   AND Resource_Week = " + resourceWeek + "\n");
		}
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		if (list.size() > 0) {
			map = list.get(0);
		}
		return map;
	}

	/**
	 * Function : 查询资源PO
	 * 
	 * @param :
	 *            物料ID
	 * @param :
	 *            业务范围ID
	 * @param :
	 *            订单类型
	 * @param :
	 *            批次号
	 * @param :
	 *            资源年
	 * @param :
	 *            资源周
	 * @return : 资源PO
	 * @throws :
	 *             Exception LastUpdate : 2010-06-21
	 */
	public Map<String, Object> getResourceBatchPO(String materialId, String orderType, String specialBatchNo, String resourceYear, String resourceWeek) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT *\n");
		sql.append("  FROM Tt_Vs_Resource\n");
		sql.append(" WHERE 1 = 1\n");
		if (!"".equals(materialId) && materialId != null) {
			sql.append("   AND Material_Id = " + materialId + "\n");
		}
		if (!"".equals(orderType) && orderType != null && Integer.parseInt(orderType) == Constant.ORDER_TYPE_03) {
			sql.append("   AND Special_Batch_No ='" + specialBatchNo.trim() + "'\n");
		}
		if (!"".equals(resourceYear) && resourceYear != null) {
			sql.append("   AND Resource_Year = " + resourceYear + "\n");
		}
		if (!"".equals(resourceWeek) && resourceWeek != null) {
			sql.append("   AND Resource_Week = " + resourceWeek + "\n");
		}
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		if (list.size() > 0) {
			map = list.get(0);
		}
		return map;
	}

	/**
	 * 订单资源审核查询
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getOrderResourceReserveQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String areaId = (String) map.get("areaId");
		String area = (String) map.get("area");
		String groupCode = (String) map.get("groupCode");
		String dealerCode = (String) map.get("dealerCode");
		String orderType = (String) map.get("orderType");
		String orderNo = (String) map.get("orderNo");
		String companyId = (String) map.get("companyId");
		String reqStatus = (String) map.get("reqStatus");
		String orgCode = (String) map.get("orgCode");
		String dutyType = (String) map.get("dutyType");
		String orgId = (String) map.get("orgId");
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");
		String operateType=(String)map.get("operateType");
		String userId=(String)map.get("userId");
		List<Object> params = new LinkedList<Object>();

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVO.ORDER_ID,\n");
		sql.append("       TVDR.REQ_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("       DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_CODE, TMD1.DEALER_CODE) ORDER_DEALER_CODE,\n");

		sql.append("       DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_SHORTNAME, TMD1.DEALER_SHORTNAME) ORDER_DEALER_NAME,\n");

		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_YEAR,\n");
		sql.append("       TVO.ORDER_WEEK,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       TC2.CODE_DESC REQ_STATUS,\n");
		sql.append("       NVL(TVO.VER, 0) VER,\n");
		sql.append("       TVDR.REQ_TOTAL_AMOUNT,\n");
		sql.append("       TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("tvdr.F_AUDIT_TIME,\n");

		sql.append("       TVAT.TYPE_NAME FUND_TYPE,TVDR.DLVRY_REQ_NO,\n");
		sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVDRD.RESERVE_AMOUNT), 0) RESERVE_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TT_VS_DLVRY_REQ          TVDR,\n");
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n");
		sql.append("       TM_DEALER                TMD,\n");
		sql.append("       TM_DEALER                TMD1,\n");
		sql.append("       TM_DEALER                TMD2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_DEALER_ORG_RELATION   TDOR,\n");
		sql.append("       TT_VS_ACCOUNT_TYPE       TVAT,TC_CODE TC2\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVDR.ORDER_ID  AND TC2.CODE_ID=TVDR.REQ_STATUS\n");
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");
		sql.append("   AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TVDR.ORDER_DEALER_ID = TMD2.DEALER_ID(+)\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TMD1.DEALER_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TMD.DEALER_ID = TDOR.DEALER_ID\n");
		sql.append("   AND TVDR.FUND_TYPE = TVAT.TYPE_ID\n");
		if(operateType!=null&&"1".equals(operateType)){
			sql.append("   AND TVDR.REQ_STATUS IN (" + Constant.ORDER_REQ_STATUS_01 + ", " + Constant.ORDER_REQ_STATUS_05 + ", " + Constant.ORDER_REQ_STATUS_09 + ","+Constant.ORDER_REQ_STATUS_BH+","+Constant.ORDER_REQ_STATUS_CYBH+")\n");
		}
		if(operateType!=null&&"2".equals(operateType)){
			sql.append("   AND TVDR.REQ_STATUS IN (" + Constant.ORDER_REQ_STATUS_03 + ")\n");
		}
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		if (orderType != null && !"".equals(orderType)) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		if (orderNo != null && !"".equals(orderNo)) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (orderYear != null && !"".equals(orderYear)) {
			sql.append("   AND TVO.ORDER_YEAR =" + orderYear + "\n");
		}
		if (orderWeek != null && !"".equals(orderWeek)) {
			sql.append("   AND TVO.ORDER_WEEK = " + orderWeek + "\n");
		}
		if (reqStatus != null && !"".equals(reqStatus)) {
			sql.append("   AND TVDR.REQ_STATUS = " + reqStatus + "\n");
		}
		//注释 
		/*if (areaId != null && !"-1".equals(areaId) && !"".equals(areaId)) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		
		if (area != null && !"-1".equals(area) && !"".equals(area)) {
			sql.append("   AND TVO.AREA_ID IN( " + area + ")\n");
		}*/
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
		}
		if (dealerCode != null && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		if (orgCode != null && !"".equals(orgCode)) {
			sql.append("   AND TVO.BILLING_ORG_ID IN\n");
			sql.append("       (SELECT M.DEALER_ID\n");
			sql.append("          FROM TM_DEALER M\n");
			sql.append("        CONNECT BY PRIOR M.DEALER_ID = M.PARENT_DEALER_D\n");
			sql.append("         START WITH M.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("                AND M.DEALER_ID IN\n");
			sql.append("                    (SELECT REL.DEALER_ID\n");
			sql.append("                       FROM TM_DEALER_ORG_RELATION REL, TM_ORG ORG\n");
			sql.append("                      WHERE REL.ORG_ID = ORG.ORG_ID\n");
			sql.append("                        AND ORG.ORG_CODE IN\n");
			sql.append("                            (" + PlanUtil.createSqlStr(orgCode) + ")))");

		}
		/*
		 * if(dutyType!=null&&!"".equals(dutyType)){
		 * if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))||dutyType.equals(String.valueOf(Constant.DUTY_TYPE_SMALLREGION))){
		 * if(orgId!=null&&!"".equals(orgId)){ sql.append(" AND TDOR.ORG_ID =
		 * "+orgId+"\n" ); } } }
		 */

		/*if (!CommonUtils.isNullString(orgId)) {
			sql.append("   AND TDOR.ORG_ID in (" + orgId + ")\n");
		}*/

		if (beginTime != null && !beginTime.equals("")) {
			sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if (endTime != null && !endTime.equals("")) {
			sql.append("   AND TVO.RAISE_DATE < =TO_DATE('" + endTime + " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");

		}
		sql.append(" AND TVDR.DEALER_ID IN (SELECT DEALER_ID FROM VW_ORG_DEALER_ALL_NEW WHERE ROOT_ORG_ID IN (SELECT ORG_ID FROM TM_ORDER_ORG WHERE STATUS="+Constant.STATUS_ENABLE+" AND USER_ID="+userId+"))\n");
		sql.append(" GROUP BY TVO.ORDER_ID,\n");
		sql.append("          TVDR.REQ_ID,\n");
		sql.append("          TMD.DEALER_CODE,\n");
		sql.append("          TMD.DEALER_SHORTNAME,\n");
		sql.append("          DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_CODE, TMD1.DEALER_CODE),\n");

		sql.append("          DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_SHORTNAME, TMD1.DEALER_SHORTNAME),\n");

		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TVO.RAISE_DATE,\n");
		sql.append("          TVO.ORDER_YEAR,\n");
		sql.append("          TVO.ORDER_WEEK,TVDR.DLVRY_REQ_NO,\n");
		sql.append("          TVO.ORDER_TYPE,\n");
		sql.append("          TC2.CODE_DESC,\n");
		sql.append("          TVO.VER,\n");
		sql.append("          TVDR.REQ_TOTAL_AMOUNT,");
		sql.append("       	  TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("          TVAT.TYPE_NAME,\n");
		sql.append("		tvdr.F_AUDIT_TIME\n");
		sql.append("   		  ORDER BY tvdr.F_AUDIT_TIME DESC,TVO.RAISE_DATE DESC	");

		/*
		 * sql.append("SELECT TVO.ORDER_ID,\n"); sql.append(" TVDR.REQ_ID,\n");
		 * sql.append(" TMD.DEALER_CODE,\n"); sql.append(" TMD.DEALER_SHORTNAME
		 * DEALER_NAME,\n"); sql.append(" TMD1.DEALER_CODE
		 * ORDER_DEALER_CODE,\n"); sql.append(" TMD1.DEALER_SHORTNAME
		 * ORDER_DEALER_NAME,\n"); sql.append(" TVO.ORDER_NO,\n"); sql.append("
		 * TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n"); sql.append("
		 * TVO.ORDER_YEAR,\n"); sql.append(" TVO.ORDER_WEEK,\n"); sql.append("
		 * TC1.CODE_DESC ORDER_TYPE,\n"); sql.append(" TC2.CODE_DESC
		 * REQ_STATUS,TVAR.FREEZE_AMOUNT FREEZEAMOUNT,TVAT.TYPE_NAME
		 * TYPENAME,\n"); sql.append(" NVL(TVO.VER, 0) VER,\n"); sql.append("
		 * TVDR.REQ_TOTAL_AMOUNT,\n"); sql.append(" NVL(SUM(TVOD.ORDER_AMOUNT),
		 * 0) ORDER_AMOUNT,\n"); sql.append(" NVL(SUM(TVDRD.RESERVE_AMOUNT), 0)
		 * RESERVE_AMOUNT\n"); sql.append(" FROM TT_VS_ORDER TVO,\n");
		 * sql.append(" TT_VS_ORDER_DETAIL TVOD,\n"); sql.append("
		 * TT_VS_DLVRY_REQ TVDR,\n"); sql.append(" TT_VS_DLVRY_REQ_DTL
		 * TVDRD,\n"); sql.append(" TM_DEALER TMD,\n"); sql.append(" TM_DEALER
		 * TMD1,\n"); sql.append(" TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		 * sql.append(" TM_VHCL_MATERIAL_GROUP TVMG,TT_VS_ACCOUNT_FREEZE
		 * TVAR,TT_VS_ACCOUNT_TYPE TVAT,TT_VS_ACCOUNT TVA,\n"); sql.append("
		 * TM_DEALER_ORG_RELATION TDOR,TC_CODE TC1,TC_CODE TC2\n"); sql.append("
		 * WHERE TVO.ORDER_ID = TVOD.ORDER_ID AND TVDR.REQ_ID=TVAR.REQ_ID AND
		 * TVA.ACCOUNT_ID=TVAR.ACCOUNT_ID AND
		 * TVA.ACCOUNT_TYPE_ID=TVAT.TYPE_ID\n"); sql.append(" AND TVO.ORDER_ID =
		 * TVDR.ORDER_ID\n"); sql.append(" AND TVDR.REQ_ID = TVDRD.REQ_ID AND
		 * TC1.CODE_ID=TVO.ORDER_TYPE AND TC2.CODE_ID=TVDR.REQ_STATUS\n");
		 * sql.append(" AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID\n");
		 * sql.append(" AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n"); sql.append("
		 * AND TVO.ORDER_ORG_ID = TMD1.DEALER_ID\n"); sql.append(" AND
		 * TVOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n"); sql.append(" AND
		 * TVMGR.GROUP_ID = TVMG.GROUP_ID\n"); sql.append(" AND TMD.DEALER_ID =
		 * TDOR.DEALER_ID\n"); sql.append(" AND TVDR.REQ_STATUS IN
		 * ("+Constant.ORDER_REQ_STATUS_01+", "+Constant.ORDER_REQ_STATUS_05+",
		 * "+Constant.ORDER_REQ_STATUS_09+")\n"); sql.append(" AND
		 * TVO.COMPANY_ID = "+companyId+"\n");
		 * if(orderType!=null&&!"".equals(orderType)){ sql.append(" AND
		 * TVO.ORDER_TYPE = "+orderType+"\n" ); }
		 * if(orderNo!=null&&!"".equals(orderNo)){ sql.append(" AND TVO.ORDER_NO
		 * LIKE '%"+orderNo+"%'\n" ); }
		 * if(orderYear!=null&&!"".equals(orderYear)){ sql.append(" AND
		 * TVO.ORDER_YEAR ="+orderYear+"\n" ); }
		 * if(orderWeek!=null&&!"".equals(orderWeek)){ sql.append(" AND
		 * TVO.ORDER_WEEK = "+orderWeek+"\n" ); }
		 * if(reqStatus!=null&&!"".equals(reqStatus)){ sql.append(" AND
		 * TVDR.REQ_STATUS = "+reqStatus+"\n" ); }
		 * if(areaId!=null&&!"-1".equals(areaId)&&!"".equals(areaId)){
		 * sql.append(" AND TVO.AREA_ID = "+areaId+"\n" ); }
		 * if(area!=null&&!"-1".equals(area)&&!"".equals(area)){ sql.append("
		 * AND TVO.AREA_ID IN( "+area+")\n" ); }
		 * if(beginTime!=null&&!beginTime.equals("")){ sql.append(" AND
		 * TVO.RAISE_DATE >= TO_DATE('"+beginTime+" 00:00:00','YYYY-MM-DD
		 * HH24:mi:ss') \n"); } if(beginTime!=null&&!beginTime.equals("")){
		 * sql.append(" AND TVO.RAISE_DATE < =TO_DATE('"+endTime+"
		 * 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		 *  } if(groupCode!=null&&!"".equals(groupCode)){
		 * sql.append(Utility.getConSqlByParamForEqual(groupCode, params,
		 * "TVMG", "GROUP_CODE")); }
		 * if(dealerCode!=null&&!"".equals(dealerCode)){
		 * sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,
		 * "TMD", "DEALER_CODE")); } if(orgCode!=null&&!"".equals(orgCode)){
		 * sql.append(" AND TVO.BILLING_ORG_ID IN\n"); sql.append(" (SELECT
		 * M.DEALER_ID\n"); sql.append(" FROM TM_DEALER M\n"); sql.append("
		 * CONNECT BY PRIOR M.DEALER_ID = M.PARENT_DEALER_D\n"); sql.append("
		 * START WITH M.STATUS = "+Constant.STATUS_ENABLE+"\n"); sql.append("
		 * AND M.DEALER_ID IN\n"); sql.append(" (SELECT REL.DEALER_ID\n");
		 * sql.append(" FROM TM_DEALER_ORG_RELATION REL, TM_ORG ORG\n");
		 * sql.append(" WHERE REL.ORG_ID = ORG.ORG_ID\n"); sql.append(" AND
		 * ORG.ORG_CODE IN\n"); sql.append("
		 * ("+PlanUtil.createSqlStr(orgCode)+")))");
		 *  } if(dutyType!=null&&!"".equals(dutyType)){
		 * if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))||dutyType.equals(String.valueOf(Constant.DUTY_TYPE_SMALLREGION))){
		 * if(orgId!=null&&!"".equals(orgId)){ sql.append(" AND TDOR.ORG_ID =
		 * "+orgId+"\n" ); } } } sql.append(" GROUP BY TVO.ORDER_ID,\n");
		 * sql.append(" TVDR.REQ_ID,\n"); sql.append(" TMD.DEALER_CODE,\n");
		 * sql.append(" TMD.DEALER_SHORTNAME,\n"); sql.append("
		 * TMD1.DEALER_CODE,\n"); sql.append(" TMD1.DEALER_SHORTNAME,\n");
		 * sql.append(" TVO.ORDER_NO,\n"); sql.append(" TVO.RAISE_DATE,\n");
		 * sql.append(" TVO.ORDER_YEAR,\n"); sql.append(" TVO.ORDER_WEEK,\n");
		 * sql.append(" TC1.CODE_DESC,TC2.CODE_DESC,\n"); sql.append("
		 * TVO.VER,\n"); sql.append("
		 * TVDR.REQ_TOTAL_AMOUNT,TVAR.FREEZE_AMOUNT,TVAT.TYPE_NAME");
		 */

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 订单储运审核查询
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getOrderStorAgeQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String areaId = (String) map.get("areaId");
		String area = (String) map.get("area");
		String groupCode = (String) map.get("groupCode");
		String dealerCode = (String) map.get("dealerCode");
		String orderType = (String) map.get("orderType");
		String orderNo = (String) map.get("orderNo");
		String companyId = (String) map.get("companyId");
		String reqStatus = (String) map.get("reqStatus");
		String orgCode = (String) map.get("orgCode");
		String dutyType = (String) map.get("dutyType");
		String orgId = (String) map.get("orgId");
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");
		String operateType=(String)map.get("operateType");
		List<Object> params = new LinkedList<Object>();

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVO.ORDER_ID,\n");
		sql.append("       TVDR.REQ_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("       DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_CODE, TMD1.DEALER_CODE) ORDER_DEALER_CODE,\n");

		sql.append("       DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_SHORTNAME, TMD1.DEALER_SHORTNAME) ORDER_DEALER_NAME,\n");

		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_YEAR,\n");
		sql.append("       TVO.ORDER_WEEK,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       TC2.CODE_DESC REQ_STATUS,\n");
		sql.append("       NVL(TVO.VER, 0) VER,\n");
		sql.append("       TVDR.REQ_TOTAL_AMOUNT,\n");
		sql.append("       TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("tvdr.F_AUDIT_TIME,\n");

		sql.append("       TVAT.TYPE_NAME FUND_TYPE,TVDR.DLVRY_REQ_NO,\n");
		sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVDRD.RESERVE_AMOUNT), 0) RESERVE_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TT_VS_DLVRY_REQ          TVDR,\n");
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n");
		sql.append("       TM_DEALER                TMD,\n");
		sql.append("       TM_DEALER                TMD1,\n");
		sql.append("       TM_DEALER                TMD2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_DEALER_ORG_RELATION   TDOR,\n");
		sql.append("       TT_VS_ACCOUNT_TYPE       TVAT,TC_CODE TC2\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVDR.ORDER_ID  AND TC2.CODE_ID=TVDR.REQ_STATUS\n");
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");
		sql.append("   AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TVDR.ORDER_DEALER_ID = TMD2.DEALER_ID(+)\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TMD1.DEALER_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TMD.DEALER_ID = TDOR.DEALER_ID\n");
		sql.append("   AND TVDR.FUND_TYPE = TVAT.TYPE_ID\n");
		if(operateType!=null&&"1".equals(operateType)){
			sql.append("   AND TVDR.REQ_STATUS IN (" + Constant.ORDER_REQ_STATUS_01 + ", " + Constant.ORDER_REQ_STATUS_05 + ", " + Constant.ORDER_REQ_STATUS_09 + ","+Constant.ORDER_REQ_STATUS_BH+")\n");
		}
		if(operateType!=null&&"2".equals(operateType)){
			sql.append("   AND TVDR.REQ_STATUS IN (" + Constant.ORDER_REQ_STATUS_FINAL + ")\n");
		}
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		if (orderType != null && !"".equals(orderType)) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		if (orderNo != null && !"".equals(orderNo)) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (orderYear != null && !"".equals(orderYear)) {
			sql.append("   AND TVO.ORDER_YEAR =" + orderYear + "\n");
		}
		if (orderWeek != null && !"".equals(orderWeek)) {
			sql.append("   AND TVO.ORDER_WEEK = " + orderWeek + "\n");
		}
		if (reqStatus != null && !"".equals(reqStatus)) {
			sql.append("   AND TVDR.REQ_STATUS = " + reqStatus + "\n");
		}
		//注释 
		/*if (areaId != null && !"-1".equals(areaId) && !"".equals(areaId)) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		
		if (area != null && !"-1".equals(area) && !"".equals(area)) {
			sql.append("   AND TVO.AREA_ID IN( " + area + ")\n");
		}*/
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
		}
		if (dealerCode != null && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		if (orgCode != null && !"".equals(orgCode)) {
			sql.append("   AND TVO.BILLING_ORG_ID IN\n");
			sql.append("       (SELECT M.DEALER_ID\n");
			sql.append("          FROM TM_DEALER M\n");
			sql.append("        CONNECT BY PRIOR M.DEALER_ID = M.PARENT_DEALER_D\n");
			sql.append("         START WITH M.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("                AND M.DEALER_ID IN\n");
			sql.append("                    (SELECT REL.DEALER_ID\n");
			sql.append("                       FROM TM_DEALER_ORG_RELATION REL, TM_ORG ORG\n");
			sql.append("                      WHERE REL.ORG_ID = ORG.ORG_ID\n");
			sql.append("                        AND ORG.ORG_CODE IN\n");
			sql.append("                            (" + PlanUtil.createSqlStr(orgCode) + ")))");

		}
		/*
		 * if(dutyType!=null&&!"".equals(dutyType)){
		 * if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))||dutyType.equals(String.valueOf(Constant.DUTY_TYPE_SMALLREGION))){
		 * if(orgId!=null&&!"".equals(orgId)){ sql.append(" AND TDOR.ORG_ID =
		 * "+orgId+"\n" ); } } }
		 */

		/*if (!CommonUtils.isNullString(orgId)) {
			sql.append("   AND TDOR.ORG_ID in (" + orgId + ")\n");
		}*/

		if (beginTime != null && !beginTime.equals("")) {
			sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if (endTime != null && !endTime.equals("")) {
			sql.append("   AND TVO.RAISE_DATE < =TO_DATE('" + endTime + " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");

		}
		sql.append(" GROUP BY TVO.ORDER_ID,\n");
		sql.append("          TVDR.REQ_ID,\n");
		sql.append("          TMD.DEALER_CODE,\n");
		sql.append("          TMD.DEALER_SHORTNAME,\n");
		sql.append("          DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_CODE, TMD1.DEALER_CODE),\n");

		sql.append("          DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_SHORTNAME, TMD1.DEALER_SHORTNAME),\n");

		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TVO.RAISE_DATE,\n");
		sql.append("          TVO.ORDER_YEAR,\n");
		sql.append("          TVO.ORDER_WEEK,TVDR.DLVRY_REQ_NO,\n");
		sql.append("          TVO.ORDER_TYPE,\n");
		sql.append("          TC2.CODE_DESC,\n");
		sql.append("          TVO.VER,\n");
		sql.append("          TVDR.REQ_TOTAL_AMOUNT,");
		sql.append("       	  TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("          TVAT.TYPE_NAME,\n");
		sql.append("		tvdr.F_AUDIT_TIME\n");
		sql.append("   		  ORDER BY tvdr.F_AUDIT_TIME DESC,TVO.RAISE_DATE DESC	");


		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 订单资源审核查询
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getOrderResourceReserveLoadQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String areaId = (String) map.get("areaId");
		String area = (String) map.get("area");
		String groupCode = (String) map.get("groupCode");
		String dealerCode = (String) map.get("dealerCode");
		String orderType = (String) map.get("orderType");
		String orderNo = (String) map.get("orderNo");
		String companyId = (String) map.get("companyId");
		String reqStatus = (String) map.get("reqStatus");
		String orgCode = (String) map.get("orgCode");
		String dutyType = (String) map.get("dutyType");
		String orgId = (String) map.get("orgId");
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVO.ORDER_ID,\n");
		sql.append("       TVDR.REQ_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("       TMD1.DEALER_CODE ORDER_DEALER_CODE,\n");
		sql.append("       TMD1.DEALER_SHORTNAME ORDER_DEALER_NAME,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_YEAR,\n");
		sql.append("       TVO.ORDER_WEEK,\n");
		sql.append("       TC1.CODE_DESC ORDER_TYPE,\n");
		sql.append("       TC2.CODE_DESC REQ_STATUS,\n");
		sql.append("       NVL(TVO.VER, 0) VER,\n");
		sql.append("       TVDR.REQ_TOTAL_AMOUNT,\n");
		sql.append("       TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("       TVAT.TYPE_NAME FUND_TYPE,\n");
		sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVDRD.RESERVE_AMOUNT), 0) RESERVE_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TT_VS_DLVRY_REQ          TVDR,\n");
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n");
		sql.append("       TM_DEALER                TMD,\n");
		sql.append("       TM_DEALER                TMD1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_DEALER_ORG_RELATION   TDOR,\n");
		sql.append("       TT_VS_ACCOUNT_TYPE       TVAT,TC_CODE TC1,TC_CODE TC2\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVDR.ORDER_ID AND TC1.CODE_ID=TVO.ORDER_TYPE  AND TC2.CODE_ID=TVDR.REQ_STATUS\n");
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");
		sql.append("   AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TMD1.DEALER_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TMD.DEALER_ID = TDOR.DEALER_ID\n");
		sql.append("   AND TVDR.FUND_TYPE = TVAT.TYPE_ID\n");
		sql.append("   AND TVDR.REQ_STATUS IN (" + Constant.ORDER_REQ_STATUS_01 + ", " + Constant.ORDER_REQ_STATUS_05 + ", " + Constant.ORDER_REQ_STATUS_09 + ")\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		if (orderType != null && !"".equals(orderType)) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		if (orderNo != null && !"".equals(orderNo)) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (orderYear != null && !"".equals(orderYear)) {
			sql.append("   AND TVO.ORDER_YEAR =" + orderYear + "\n");
		}
		if (orderWeek != null && !"".equals(orderWeek)) {
			sql.append("   AND TVO.ORDER_WEEK = " + orderWeek + "\n");
		}
		if (reqStatus != null && !"".equals(reqStatus)) {
			sql.append("   AND TVDR.REQ_STATUS = " + reqStatus + "\n");
		}
		if (areaId != null && !"-1".equals(areaId) && !"".equals(areaId)) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (area != null && !"-1".equals(area) && !"".equals(area)) {
			sql.append("   AND TVO.AREA_ID IN( " + area + ")\n");
		}
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
		}
		if (dealerCode != null && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		if (orgCode != null && !"".equals(orgCode)) {
			sql.append("   AND TVO.BILLING_ORG_ID IN\n");
			sql.append("       (SELECT M.DEALER_ID\n");
			sql.append("          FROM TM_DEALER M\n");
			sql.append("        CONNECT BY PRIOR M.DEALER_ID = M.PARENT_DEALER_D\n");
			sql.append("         START WITH M.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("                AND M.DEALER_ID IN\n");
			sql.append("                    (SELECT REL.DEALER_ID\n");
			sql.append("                       FROM TM_DEALER_ORG_RELATION REL, TM_ORG ORG\n");
			sql.append("                      WHERE REL.ORG_ID = ORG.ORG_ID\n");
			sql.append("                        AND ORG.ORG_CODE IN\n");
			sql.append("                            (" + PlanUtil.createSqlStr(orgCode) + ")))");

		}
		if (dutyType != null && !"".equals(dutyType)) {
			if (dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION)) || dutyType.equals(String.valueOf(Constant.DUTY_TYPE_SMALLREGION))) {
				if (orgId != null && !"".equals(orgId)) {
					sql.append("   AND TDOR.ORG_ID = " + orgId + "\n");
				}
			}
		}
		if (beginTime != null && !beginTime.equals("")) {
			sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if (beginTime != null && !beginTime.equals("")) {
			sql.append("   AND TVO.RAISE_DATE < =TO_DATE('" + endTime + " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");

		}
		sql.append(" GROUP BY TVO.ORDER_ID,\n");
		sql.append("          TVDR.REQ_ID,\n");
		sql.append("          TMD.DEALER_CODE,\n");
		sql.append("          TMD.DEALER_SHORTNAME,\n");
		sql.append("          TMD1.DEALER_CODE,\n");
		sql.append("          TMD1.DEALER_SHORTNAME,\n");
		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TVO.RAISE_DATE,\n");
		sql.append("          TVO.ORDER_YEAR,\n");
		sql.append("          TVO.ORDER_WEEK,\n");
		sql.append("          TC1.CODE_DESC,\n");
		sql.append("          TC2.CODE_DESC,\n");
		sql.append("          TVO.VER,\n");
		sql.append("          TVDR.REQ_TOTAL_AMOUNT,");
		sql.append("       	  TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("          TVAT.TYPE_NAME\n");

		/*
		 * sql.append("SELECT TVO.ORDER_ID,\n"); sql.append(" TVDR.REQ_ID,\n");
		 * sql.append(" TMD.DEALER_CODE,\n"); sql.append(" TMD.DEALER_SHORTNAME
		 * DEALER_NAME,\n"); sql.append(" TMD1.DEALER_CODE
		 * ORDER_DEALER_CODE,\n"); sql.append(" TMD1.DEALER_SHORTNAME
		 * ORDER_DEALER_NAME,\n"); sql.append(" TVO.ORDER_NO,\n"); sql.append("
		 * TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n"); sql.append("
		 * TVO.ORDER_YEAR,\n"); sql.append(" TVO.ORDER_WEEK,\n"); sql.append("
		 * TC1.CODE_DESC ORDER_TYPE,\n"); sql.append(" TC2.CODE_DESC
		 * REQ_STATUS,TVAR.FREEZE_AMOUNT FREEZEAMOUNT,TVAT.TYPE_NAME
		 * TYPENAME,\n"); sql.append(" NVL(TVO.VER, 0) VER,\n"); sql.append("
		 * TVDR.REQ_TOTAL_AMOUNT,\n"); sql.append(" NVL(SUM(TVOD.ORDER_AMOUNT),
		 * 0) ORDER_AMOUNT,\n"); sql.append(" NVL(SUM(TVDRD.RESERVE_AMOUNT), 0)
		 * RESERVE_AMOUNT\n"); sql.append(" FROM TT_VS_ORDER TVO,\n");
		 * sql.append(" TT_VS_ORDER_DETAIL TVOD,\n"); sql.append("
		 * TT_VS_DLVRY_REQ TVDR,\n"); sql.append(" TT_VS_DLVRY_REQ_DTL
		 * TVDRD,\n"); sql.append(" TM_DEALER TMD,\n"); sql.append(" TM_DEALER
		 * TMD1,\n"); sql.append(" TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		 * sql.append(" TM_VHCL_MATERIAL_GROUP TVMG,TT_VS_ACCOUNT_FREEZE
		 * TVAR,TT_VS_ACCOUNT_TYPE TVAT,TT_VS_ACCOUNT TVA,\n"); sql.append("
		 * TM_DEALER_ORG_RELATION TDOR,TC_CODE TC1,TC_CODE TC2\n"); sql.append("
		 * WHERE TVO.ORDER_ID = TVOD.ORDER_ID AND TVDR.REQ_ID=TVAR.REQ_ID AND
		 * TVA.ACCOUNT_ID=TVAR.ACCOUNT_ID AND
		 * TVA.ACCOUNT_TYPE_ID=TVAT.TYPE_ID\n"); sql.append(" AND TVO.ORDER_ID =
		 * TVDR.ORDER_ID\n"); sql.append(" AND TVDR.REQ_ID = TVDRD.REQ_ID AND
		 * TC1.CODE_ID=TVO.ORDER_TYPE AND TC2.CODE_ID=TVDR.REQ_STATUS\n");
		 * sql.append(" AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID\n");
		 * sql.append(" AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n"); sql.append("
		 * AND TVO.ORDER_ORG_ID = TMD1.DEALER_ID\n"); sql.append(" AND
		 * TVOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n"); sql.append(" AND
		 * TVMGR.GROUP_ID = TVMG.GROUP_ID\n"); sql.append(" AND TMD.DEALER_ID =
		 * TDOR.DEALER_ID\n"); sql.append(" AND TVDR.REQ_STATUS IN
		 * ("+Constant.ORDER_REQ_STATUS_01+", "+Constant.ORDER_REQ_STATUS_05+",
		 * "+Constant.ORDER_REQ_STATUS_09+")\n"); sql.append(" AND
		 * TVO.COMPANY_ID = "+companyId+"\n");
		 * if(orderType!=null&&!"".equals(orderType)){ sql.append(" AND
		 * TVO.ORDER_TYPE = "+orderType+"\n" ); }
		 * if(orderNo!=null&&!"".equals(orderNo)){ sql.append(" AND TVO.ORDER_NO
		 * LIKE '%"+orderNo+"%'\n" ); }
		 * if(orderYear!=null&&!"".equals(orderYear)){ sql.append(" AND
		 * TVO.ORDER_YEAR ="+orderYear+"\n" ); }
		 * if(orderWeek!=null&&!"".equals(orderWeek)){ sql.append(" AND
		 * TVO.ORDER_WEEK = "+orderWeek+"\n" ); }
		 * if(reqStatus!=null&&!"".equals(reqStatus)){ sql.append(" AND
		 * TVDR.REQ_STATUS = "+reqStatus+"\n" ); }
		 * if(areaId!=null&&!"-1".equals(areaId)&&!"".equals(areaId)){
		 * sql.append(" AND TVO.AREA_ID = "+areaId+"\n" ); }
		 * if(area!=null&&!"-1".equals(area)&&!"".equals(area)){ sql.append("
		 * AND TVO.AREA_ID IN( "+area+")\n" ); }
		 * if(beginTime!=null&&!beginTime.equals("")){ sql.append(" AND
		 * TVO.RAISE_DATE >= TO_DATE('"+beginTime+" 00:00:00','YYYY-MM-DD
		 * HH24:mi:ss') \n"); } if(beginTime!=null&&!beginTime.equals("")){
		 * sql.append(" AND TVO.RAISE_DATE < =TO_DATE('"+endTime+"
		 * 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		 *  } if(groupCode!=null&&!"".equals(groupCode)){
		 * sql.append(Utility.getConSqlByParamForEqual(groupCode, params,
		 * "TVMG", "GROUP_CODE")); }
		 * if(dealerCode!=null&&!"".equals(dealerCode)){
		 * sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,
		 * "TMD", "DEALER_CODE")); } if(orgCode!=null&&!"".equals(orgCode)){
		 * sql.append(" AND TVO.BILLING_ORG_ID IN\n"); sql.append(" (SELECT
		 * M.DEALER_ID\n"); sql.append(" FROM TM_DEALER M\n"); sql.append("
		 * CONNECT BY PRIOR M.DEALER_ID = M.PARENT_DEALER_D\n"); sql.append("
		 * START WITH M.STATUS = "+Constant.STATUS_ENABLE+"\n"); sql.append("
		 * AND M.DEALER_ID IN\n"); sql.append(" (SELECT REL.DEALER_ID\n");
		 * sql.append(" FROM TM_DEALER_ORG_RELATION REL, TM_ORG ORG\n");
		 * sql.append(" WHERE REL.ORG_ID = ORG.ORG_ID\n"); sql.append(" AND
		 * ORG.ORG_CODE IN\n"); sql.append("
		 * ("+PlanUtil.createSqlStr(orgCode)+")))");
		 *  } if(dutyType!=null&&!"".equals(dutyType)){
		 * if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))||dutyType.equals(String.valueOf(Constant.DUTY_TYPE_SMALLREGION))){
		 * if(orgId!=null&&!"".equals(orgId)){ sql.append(" AND TDOR.ORG_ID =
		 * "+orgId+"\n" ); } } } sql.append(" GROUP BY TVO.ORDER_ID,\n");
		 * sql.append(" TVDR.REQ_ID,\n"); sql.append(" TMD.DEALER_CODE,\n");
		 * sql.append(" TMD.DEALER_SHORTNAME,\n"); sql.append("
		 * TMD1.DEALER_CODE,\n"); sql.append(" TMD1.DEALER_SHORTNAME,\n");
		 * sql.append(" TVO.ORDER_NO,\n"); sql.append(" TVO.RAISE_DATE,\n");
		 * sql.append(" TVO.ORDER_YEAR,\n"); sql.append(" TVO.ORDER_WEEK,\n");
		 * sql.append(" TC1.CODE_DESC,TC2.CODE_DESC,\n"); sql.append("
		 * TVO.VER,\n"); sql.append("
		 * TVDR.REQ_TOTAL_AMOUNT,TVAR.FREEZE_AMOUNT,TVAT.TYPE_NAME");
		 */

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 订单资源审核明细下载
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 * @author yx add 20101213
	 */
	public PageResult<Map<String, Object>> getOrderResourceReserveDetailDownLoad(Map<String, Object> map, int curPage, int pageSize) throws Exception {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String areaId = (String) map.get("areaId");
		String area = (String) map.get("area");
		String groupCode = (String) map.get("groupCode");
		String dealerCode = (String) map.get("dealerCode");
		String orderType = (String) map.get("orderType");
		String orderNo = (String) map.get("orderNo");
		String companyId = (String) map.get("companyId");
		String reqStatus = (String) map.get("reqStatus");
		String orgCode = (String) map.get("orgCode");
		String dutyType = (String) map.get("dutyType");
		String orgId = (String) map.get("orgId");
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVO.ORDER_ID,\n");
		sql.append("       TVDR.REQ_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD2.DEALER_CODE, TMD1.DEALER_CODE) ORDER_DEALER_CODE,\n");
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD2.DEALER_SHORTNAME, TMD1.DEALER_SHORTNAME) ORDER_DEALER_NAME,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TVDR.DLVRY_REQ_NO,\n");
		sql.append("       TO_CHAR(TVDR.CREATE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_YEAR,\n");
		sql.append("       TVO.ORDER_WEEK,\n");
		sql.append("       TC1.CODE_DESC ORDER_TYPE,\n");
		sql.append("       TC2.CODE_DESC REQ_STATUS,\n");
		sql.append("       NVL(TVO.VER, 0) VER,\n");
		sql.append("       TVDR.REQ_TOTAL_AMOUNT,\n");
		sql.append("       TVDRD.SINGLE_PRICE,\n");
		sql.append("       TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("       TVAT.TYPE_NAME FUND_TYPE,\n");
		sql.append("       NVL(SUM(TVDRD.REQ_AMOUNT), 0) ORDER_AMOUNT,TVM.MATERIAL_CODE CO,\n");
		sql.append("       NVL(SUM(TVDRD.RESERVE_AMOUNT), 0) RESERVE_AMOUNT,TVMG.GROUP_CODE TYPECODE,NVL(SUM(TVOD.TOTAL_PRICE), 0) MY_PRICE\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TT_VS_DLVRY_REQ          TVDR,\n");
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n");
		sql.append("       TM_DEALER                TMD,\n");
		sql.append("       TM_DEALER                TMD1,\n");
		sql.append("       TM_DEALER                TMD2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_DEALER_ORG_RELATION   TDOR,\n");
		sql.append("       TT_VS_ACCOUNT_TYPE       TVAT,TC_CODE TC2,TC_CODE TC1,TM_VHCL_MATERIAL TVM\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVDR.ORDER_ID  AND TC2.CODE_ID=TVDR.REQ_STATUS\n");
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");
		sql.append("   AND TVDR.ORDER_DEALER_ID = TMD2.DEALER_ID(+)\n");
		sql.append("   AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID AND TVM.MATERIAL_ID=TVOD.MATERIAL_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TMD1.DEALER_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TMD.DEALER_ID = TDOR.DEALER_ID\n");
		sql.append("   AND TVDR.FUND_TYPE = TVAT.TYPE_ID AND TC1.CODE_ID=TVO.ORDER_TYPE\n");
		sql.append("   AND TVDR.REQ_STATUS IN (" + Constant.ORDER_REQ_STATUS_01 + ", " + Constant.ORDER_REQ_STATUS_05 + ", " + Constant.ORDER_REQ_STATUS_09 + ")\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		if (orderType != null && !"".equals(orderType)) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		if (orderNo != null && !"".equals(orderNo)) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (orderYear != null && !"".equals(orderYear)) {
			sql.append("   AND TVO.ORDER_YEAR =" + orderYear + "\n");
		}
		if (orderWeek != null && !"".equals(orderWeek)) {
			sql.append("   AND TVO.ORDER_WEEK = " + orderWeek + "\n");
		}
		if (reqStatus != null && !"".equals(reqStatus)) {
			sql.append("   AND TVDR.REQ_STATUS = " + reqStatus + "\n");
		}
		if (areaId != null && !"-1".equals(areaId) && !"".equals(areaId)) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (area != null && !"-1".equals(area) && !"".equals(area)) {
			sql.append("   AND TVO.AREA_ID IN( " + area + ")\n");
		}
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
		}
		if (dealerCode != null && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		if (orgCode != null && !"".equals(orgCode)) {
			sql.append("   AND TVO.BILLING_ORG_ID IN\n");
			sql.append("       (SELECT M.DEALER_ID\n");
			sql.append("          FROM TM_DEALER M\n");
			sql.append("        CONNECT BY PRIOR M.DEALER_ID = M.PARENT_DEALER_D\n");
			sql.append("         START WITH M.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("                AND M.DEALER_ID IN\n");
			sql.append("                    (SELECT REL.DEALER_ID\n");
			sql.append("                       FROM TM_DEALER_ORG_RELATION REL, TM_ORG ORG\n");
			sql.append("                      WHERE REL.ORG_ID = ORG.ORG_ID\n");
			sql.append("                        AND ORG.ORG_CODE IN\n");
			sql.append("                            (" + PlanUtil.createSqlStr(orgCode) + ")))");

		}
		if (dutyType != null && !"".equals(dutyType)) {
			if (dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION)) || dutyType.equals(String.valueOf(Constant.DUTY_TYPE_SMALLREGION))) {
				if (orgId != null && !"".equals(orgId)) {
					sql.append("   AND TDOR.ORG_ID = " + orgId + "\n");
				}
			}
		}
		if (beginTime != null && !beginTime.equals("")) {
			sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if (beginTime != null && !beginTime.equals("")) {
			sql.append("   AND TVO.RAISE_DATE < =TO_DATE('" + endTime + " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");

		}
		sql.append(" GROUP BY TVO.ORDER_ID,\n");
		sql.append("          TVDR.REQ_ID,\n");
		sql.append("          TMD.DEALER_CODE,\n");
		sql.append("          TMD.DEALER_SHORTNAME,\n");
		sql.append("          TMD1.DEALER_CODE,\n");
		sql.append("          TMD1.DEALER_SHORTNAME,\n");
		sql.append("          TMD2.DEALER_CODE,\n");
		sql.append("          TMD2.DEALER_SHORTNAME,TVDR.DLVRY_REQ_NO,\n");
		sql.append("          TVDR.CALL_LEAVEL,\n");
		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TVDR.DLVRY_REQ_NO,\n");
		sql.append("          TVDR.CREATE_DATE,\n");
		sql.append("          TVO.ORDER_YEAR,TVM.MATERIAL_CODE,\n");
		sql.append("          TVO.ORDER_WEEK,\n");
		sql.append("          TC1.CODE_DESC,\n");
		sql.append("          TC2.CODE_DESC,\n");
		sql.append("          TVO.VER,\n");
		sql.append("          TVDR.REQ_TOTAL_AMOUNT,");
		sql.append("          TVDRD.SINGLE_PRICE,\n");
		sql.append("       	  TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("          TVAT.TYPE_NAME,TVMG.GROUP_CODE\n");

		/*
		 * sql.append("SELECT TVO.ORDER_ID,\n"); sql.append(" TVDR.REQ_ID,\n");
		 * sql.append(" TMD.DEALER_CODE,\n"); sql.append(" TMD.DEALER_SHORTNAME
		 * DEALER_NAME,\n"); sql.append(" TMD1.DEALER_CODE
		 * ORDER_DEALER_CODE,\n"); sql.append(" TMD1.DEALER_SHORTNAME
		 * ORDER_DEALER_NAME,\n"); sql.append(" TVO.ORDER_NO,\n"); sql.append("
		 * TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n"); sql.append("
		 * TVO.ORDER_YEAR,\n"); sql.append(" TVO.ORDER_WEEK,\n"); sql.append("
		 * TC1.CODE_DESC ORDER_TYPE,\n"); sql.append(" TC2.CODE_DESC
		 * REQ_STATUS,TVAR.FREEZE_AMOUNT FREEZEAMOUNT,TVAT.TYPE_NAME
		 * TYPENAME,\n"); sql.append(" NVL(TVO.VER, 0) VER,\n"); sql.append("
		 * TVDR.REQ_TOTAL_AMOUNT,\n"); sql.append(" NVL(SUM(TVOD.ORDER_AMOUNT),
		 * 0) ORDER_AMOUNT,\n"); sql.append(" NVL(SUM(TVDRD.RESERVE_AMOUNT), 0)
		 * RESERVE_AMOUNT\n"); sql.append(" FROM TT_VS_ORDER TVO,\n");
		 * sql.append(" TT_VS_ORDER_DETAIL TVOD,\n"); sql.append("
		 * TT_VS_DLVRY_REQ TVDR,\n"); sql.append(" TT_VS_DLVRY_REQ_DTL
		 * TVDRD,\n"); sql.append(" TM_DEALER TMD,\n"); sql.append(" TM_DEALER
		 * TMD1,\n"); sql.append(" TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		 * sql.append(" TM_VHCL_MATERIAL_GROUP TVMG,TT_VS_ACCOUNT_FREEZE
		 * TVAR,TT_VS_ACCOUNT_TYPE TVAT,TT_VS_ACCOUNT TVA,\n"); sql.append("
		 * TM_DEALER_ORG_RELATION TDOR,TC_CODE TC1,TC_CODE TC2\n"); sql.append("
		 * WHERE TVO.ORDER_ID = TVOD.ORDER_ID AND TVDR.REQ_ID=TVAR.REQ_ID AND
		 * TVA.ACCOUNT_ID=TVAR.ACCOUNT_ID AND
		 * TVA.ACCOUNT_TYPE_ID=TVAT.TYPE_ID\n"); sql.append(" AND TVO.ORDER_ID =
		 * TVDR.ORDER_ID\n"); sql.append(" AND TVDR.REQ_ID = TVDRD.REQ_ID AND
		 * TC1.CODE_ID=TVO.ORDER_TYPE AND TC2.CODE_ID=TVDR.REQ_STATUS\n");
		 * sql.append(" AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID\n");
		 * sql.append(" AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n"); sql.append("
		 * AND TVO.ORDER_ORG_ID = TMD1.DEALER_ID\n"); sql.append(" AND
		 * TVOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n"); sql.append(" AND
		 * TVMGR.GROUP_ID = TVMG.GROUP_ID\n"); sql.append(" AND TMD.DEALER_ID =
		 * TDOR.DEALER_ID\n"); sql.append(" AND TVDR.REQ_STATUS IN
		 * ("+Constant.ORDER_REQ_STATUS_01+", "+Constant.ORDER_REQ_STATUS_05+",
		 * "+Constant.ORDER_REQ_STATUS_09+")\n"); sql.append(" AND
		 * TVO.COMPANY_ID = "+companyId+"\n");
		 * if(orderType!=null&&!"".equals(orderType)){ sql.append(" AND
		 * TVO.ORDER_TYPE = "+orderType+"\n" ); }
		 * if(orderNo!=null&&!"".equals(orderNo)){ sql.append(" AND TVO.ORDER_NO
		 * LIKE '%"+orderNo+"%'\n" ); }
		 * if(orderYear!=null&&!"".equals(orderYear)){ sql.append(" AND
		 * TVO.ORDER_YEAR ="+orderYear+"\n" ); }
		 * if(orderWeek!=null&&!"".equals(orderWeek)){ sql.append(" AND
		 * TVO.ORDER_WEEK = "+orderWeek+"\n" ); }
		 * if(reqStatus!=null&&!"".equals(reqStatus)){ sql.append(" AND
		 * TVDR.REQ_STATUS = "+reqStatus+"\n" ); }
		 * if(areaId!=null&&!"-1".equals(areaId)&&!"".equals(areaId)){
		 * sql.append(" AND TVO.AREA_ID = "+areaId+"\n" ); }
		 * if(area!=null&&!"-1".equals(area)&&!"".equals(area)){ sql.append("
		 * AND TVO.AREA_ID IN( "+area+")\n" ); }
		 * if(beginTime!=null&&!beginTime.equals("")){ sql.append(" AND
		 * TVO.RAISE_DATE >= TO_DATE('"+beginTime+" 00:00:00','YYYY-MM-DD
		 * HH24:mi:ss') \n"); } if(beginTime!=null&&!beginTime.equals("")){
		 * sql.append(" AND TVO.RAISE_DATE < =TO_DATE('"+endTime+"
		 * 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		 *  } if(groupCode!=null&&!"".equals(groupCode)){
		 * sql.append(Utility.getConSqlByParamForEqual(groupCode, params,
		 * "TVMG", "GROUP_CODE")); }
		 * if(dealerCode!=null&&!"".equals(dealerCode)){
		 * sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,
		 * "TMD", "DEALER_CODE")); } if(orgCode!=null&&!"".equals(orgCode)){
		 * sql.append(" AND TVO.BILLING_ORG_ID IN\n"); sql.append(" (SELECT
		 * M.DEALER_ID\n"); sql.append(" FROM TM_DEALER M\n"); sql.append("
		 * CONNECT BY PRIOR M.DEALER_ID = M.PARENT_DEALER_D\n"); sql.append("
		 * START WITH M.STATUS = "+Constant.STATUS_ENABLE+"\n"); sql.append("
		 * AND M.DEALER_ID IN\n"); sql.append(" (SELECT REL.DEALER_ID\n");
		 * sql.append(" FROM TM_DEALER_ORG_RELATION REL, TM_ORG ORG\n");
		 * sql.append(" WHERE REL.ORG_ID = ORG.ORG_ID\n"); sql.append(" AND
		 * ORG.ORG_CODE IN\n"); sql.append("
		 * ("+PlanUtil.createSqlStr(orgCode)+")))");
		 *  } if(dutyType!=null&&!"".equals(dutyType)){
		 * if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))||dutyType.equals(String.valueOf(Constant.DUTY_TYPE_SMALLREGION))){
		 * if(orgId!=null&&!"".equals(orgId)){ sql.append(" AND TDOR.ORG_ID =
		 * "+orgId+"\n" ); } } } sql.append(" GROUP BY TVO.ORDER_ID,\n");
		 * sql.append(" TVDR.REQ_ID,\n"); sql.append(" TMD.DEALER_CODE,\n");
		 * sql.append(" TMD.DEALER_SHORTNAME,\n"); sql.append("
		 * TMD1.DEALER_CODE,\n"); sql.append(" TMD1.DEALER_SHORTNAME,\n");
		 * sql.append(" TVO.ORDER_NO,\n"); sql.append(" TVO.RAISE_DATE,\n");
		 * sql.append(" TVO.ORDER_YEAR,\n"); sql.append(" TVO.ORDER_WEEK,\n");
		 * sql.append(" TC1.CODE_DESC,TC2.CODE_DESC,\n"); sql.append("
		 * TVO.VER,\n"); sql.append("
		 * TVDR.REQ_TOTAL_AMOUNT,TVAR.FREEZE_AMOUNT,TVAT.TYPE_NAME");
		 */

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}

	public Map<String, Object> getOrderInfoByReqId(String reqId) throws Exception {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ",TMD5.DEALER_SHORTNAME, TMD1.DEALER_SHORTNAME) DEALER_NAME1,\n");
		sql.append("       TMD1.DEALER_CODE,\n");
		sql.append("       TMD2.DEALER_TYPE,\n");
		sql.append("       TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("       TMD2.DEALER_SHORTNAME DEALER_NAME2,\n");
		sql.append("       TMD3.DEALER_SHORTNAME DEALER_NAME3,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       NVL(TVDR.VER, 0) VER,\n");
		sql.append("       TVO.ORDER_YEAR,\n");
		sql.append("       TVO.ORDER_WEEK,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       TVO.PRODUCT_COMBO_ID,\n");
		sql.append("       TVDR.AREA_ID,\n");
		//sql.append("       TBA.AREA_NAME,\n");
		sql.append("       TVDR.REQ_STATUS,\n");
		sql.append("       TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("       TVDR.REBATE_AMOUNT,\n");
		sql.append("       TVDR.DELIVERY_TYPE,\n");
		sql.append("       TMF.FLEET_NAME,\n");
		sql.append("       TVDR.FLEET_ADDRESS,\n");
		sql.append("       TVDR.ADDRESS_ID,\n");
		sql.append("       TVA.ADDRESS,\n");
		sql.append("       TVA.RECEIVE_ORG,\n");
		sql.append("       TCC1.CODE_DESC ORDER_TYPE_NAME,\n");
		sql.append("       TCC2.CODE_DESC DELIVERY_TYPE_NAME,\n");
		sql.append("       TVAT.TYPE_NAME,\n");
		sql.append("       TVO.REFIT_REMARK,\n");
		sql.append("       TVO.PAY_REMARK,\n");
		sql.append("       TVDR.REQ_REMARK ORDER_REMARK,\n");
		sql.append("       TVDR.FUND_TYPE FUND_TYPE_ID,\n");
		sql.append("       TVO.ORDER_PRICE,\n");
		sql.append("       TVDR.PRICE_ID,\n");
		sql.append("        '' PRICE_DESC,\n");
		sql.append("       TVDR.OTHER_PRICE_REASON,\n");
		sql.append("       TVDR.RECEIVER,\n");
		sql.append("       TVDR.LINK_MAN,\n");
		sql.append("       TVDR.TEL,\n");
		sql.append("       TVDR.DISCOUNT,\n");
		sql.append("       TVO.ORDER_ORG_ID,\n");
		sql.append("       TVO.BILLING_ORG_ID,\n");
		sql.append("       TVDR.WAREHOUSE_ID,\n");
		sql.append("       TW.WAREHOUSE_NAME,\n");
		sql.append("       TVO.IS_FLEET,\n");
		sql.append("       TVDR.REQ_TOTAL_AMOUNT,\n");
		sql.append("       TVDR.IS_REBATE,\n");
		sql.append("       TVDR.REBATE_AMOUNT,\n");
		sql.append("       TVO.IS_CUSTOM_ADDR,\n");
		sql.append("       TR1.REGION_NAME PROVICE,\n");
		sql.append("       TR2.REGION_NAME CITY,\n");
		sql.append("       TR3.REGION_NAME TOWN,\n");
		sql.append("       TVO.CUSTOM_ADDR,\n");
		sql.append("       TVO.CUSTOM_LINK_MAN,\n");
		sql.append("       TVO.CUSTOM_TEL,\n");
		sql.append("       TOR.ORG_NAME,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("       NVL(SUM(TVDRD.RESERVE_AMOUNT), 0) RESERVE_AMOUNT,\n");
		sql.append("       NVL(TVDR.TMP_LICENSE_AMOUNT, 0) TMP_LICENSE_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER            TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL     TVOD,\n");
		sql.append("       TT_VS_DLVRY_REQ        TVDR,\n");
		sql.append("       TT_VS_DLVRY_REQ_DTL    TVDRD,\n");
		sql.append("       TM_DEALER              TMD1,\n");
		sql.append("       TM_DEALER              TMD2,\n");
		sql.append("       TM_DEALER              TMD3,\n");
		sql.append("       TM_DEALER              TMD5,\n");
		sql.append("       TC_CODE                TCC1,\n");
		sql.append("       TC_CODE                TCC2,\n");
		sql.append("       TT_VS_ACCOUNT_TYPE     TVAT,\n");
		sql.append("       TM_FLEET               TMF,\n");
		sql.append("       TM_VS_ADDRESS          TVA,\n");
		//sql.append("       TM_BUSINESS_AREA       TBA,\n");
		sql.append("       TM_DEALER_ORG_RELATION TDOR,\n");
		sql.append("       TM_REGION              TR1,\n");
		sql.append("        TM_REGION              TR2,\n");
		sql.append("        TM_REGION              TR3,\n");
		sql.append("       TM_WAREHOUSE			  TW,\n");
		sql.append("       TM_ORG                 TOR\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVDR.ORDER_ID\n");
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");
		sql.append("   AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TMD1.DEALER_ID\n");
		sql.append("   AND TVDR.ORDER_DEALER_ID = TMD5.DEALER_ID(+)\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TMD2.DEALER_ID\n");
		sql.append("   AND TVDR.RECEIVER = TMD3.DEALER_ID(+)\n");
		sql.append("   AND TVO.ORDER_TYPE = TCC1.CODE_ID\n");
		sql.append("   AND TVDR.FUND_TYPE = TVAT.TYPE_ID\n");
		sql.append("   AND TVDR.DELIVERY_TYPE = TCC2.CODE_ID\n");
		sql.append("   AND TVA.ID(+) = TVDR.ADDRESS_ID\n");
		sql.append("   AND TMF.FLEET_ID(+) = TVDR.FLEET_ID\n");
		//sql.append("   AND TVDR.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TDOR.DEALER_ID\n");
		sql.append("   AND TDOR.ORG_ID = TOR.ORG_ID\n");
		sql.append("   AND TVO.PROVINCE_ID=TR1.REGION_CODE(+)\n");
		sql.append("    AND TVO.CITY_ID=TR2.REGION_CODE(+)\n");
		sql.append("   	AND TVO.TOWN_ID=TR3.REGION_CODE(+)\n");
		sql.append("   AND TVDR.WAREHOUSE_ID = TW.WAREHOUSE_ID(+)\n");
		sql.append("   AND TVDR.REQ_ID = " + reqId + "\n");
		sql.append(" GROUP BY DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ",TMD5.DEALER_SHORTNAME, TMD1.DEALER_SHORTNAME),\n");
		sql.append("          TMD1.DEALER_CODE,\n");
		sql.append("          TMD2.DEALER_TYPE,\n");
		sql.append("          TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("          TMD2.DEALER_SHORTNAME,\n");
		sql.append("          TMD3.DEALER_SHORTNAME,\n");
		sql.append("          TVO.PRODUCT_COMBO_ID,\n");
		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TVDR.VER,\n");
		sql.append("          TVO.ORDER_YEAR,\n");
		sql.append("          TVO.ORDER_WEEK,\n");
		sql.append("          TVO.ORDER_TYPE,\n");
		sql.append("          TVDR.AREA_ID,\n");
		//sql.append("          TBA.AREA_NAME,\n");
		sql.append("          TVDR.DELIVERY_TYPE,\n");
		sql.append("          TMF.FLEET_NAME,\n");
		sql.append("          TVDR.FLEET_ADDRESS,\n");
		sql.append("          TVDR.ADDRESS_ID,\n");
		sql.append("          TVA.ADDRESS,\n");
		sql.append("          TVA.RECEIVE_ORG,\n");
		sql.append("          TCC1.CODE_DESC,\n");
		sql.append("          TCC2.CODE_DESC,\n");
		sql.append("          TVAT.TYPE_NAME,\n");
		sql.append("          TVO.REFIT_REMARK,\n");
		sql.append("          TVO.PAY_REMARK,\n");
		sql.append("          TVDR.REQ_REMARK,\n");
		sql.append("          TVDR.FUND_TYPE,\n");
		sql.append("          TVO.ORDER_PRICE,\n");
		sql.append("          TVDR.PRICE_ID,\n");
		sql.append("       TVDR.REQ_STATUS,\n");
		sql.append("       TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("       TVDR.REBATE_AMOUNT,\n");
		sql.append("          TVDR.OTHER_PRICE_REASON,\n");
		sql.append("          TVDR.RECEIVER,\n");
		sql.append("          TVDR.LINK_MAN,\n");
		sql.append("          TVDR.TEL,\n");
		sql.append("          TVDR.DISCOUNT,\n");
		sql.append("          TVO.ORDER_ORG_ID,\n");
		sql.append("          TVO.BILLING_ORG_ID,\n");
		sql.append("          TVDR.WAREHOUSE_ID,\n");
		sql.append("       	  TW.WAREHOUSE_NAME,\n");
		sql.append("          TVO.IS_FLEET,\n");
		sql.append("          TVDR.REQ_TOTAL_AMOUNT,\n");
		sql.append("          TOR.ORG_NAME,\n");
		sql.append("          TVDR.TMP_LICENSE_AMOUNT,\n");
		sql.append("       	  TVDR.IS_REBATE,\n");
		sql.append("          TVDR.REBATE_AMOUNT,\n");
		sql.append("          TVO.IS_CUSTOM_ADDR,\n");
		sql.append("          TR1.REGION_NAME,\n");
		sql.append("          TR2.REGION_NAME,\n");
		sql.append("          TR3.REGION_NAME,\n");
		sql.append("          TVO.CUSTOM_ADDR,\n");
		sql.append("          TVO.CUSTOM_LINK_MAN,\n");
		sql.append("          TVO.CUSTOM_TEL,\n");
		sql.append("          TVO.RAISE_DATE");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		Map<String, Object> map = new HashMap<String, Object>();
		if (list.size() > 0) {
			map = list.get(0);
		}
		return map;
	}

	/**
	 * 订单资源审核明细列表
	 * 
	 * @param orderId
	 * @param orderType
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getorderResourceReserveDetailList(String warehouse_id,String reqId, String orderType, String companyId) throws Exception {
		StringBuffer sql = new StringBuffer();

		/*sql.append("SELECT TVDRD.DETAIL_ID,\n");
		sql.append("       TVDRD.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       decode(TVDRD.PATCH_NO,'null','',TVDRD.PATCH_NO) SPECIAL_BATCH_NO,\n");
		sql.append("       NVL(TVDRD.REQ_AMOUNT, 0) REQ_AMOUNT,\n");
		sql.append("       NVL(TVDRD.DELIVERY_AMOUNT, 0) DELIVERY_AMOUNT,\n");
		sql.append("       TVDRD.ORDER_DETAIL_ID,\n");
		sql.append("       NVL(TVDRD.RESERVE_AMOUNT, 0) RESERVE_AMOUNT,\n");
		sql.append("       NVL(TVDRD.SINGLE_PRICE, 0) SINGLE_PRICE,\n");
		sql.append("       NVL(TVDRD.TOTAL_PRICE, 0) + NVL(TVDRD.DISCOUNT_PRICE, 0) TOTAL_PRICE,\n");
		sql.append("       NVL(TVDRD.DISCOUNT_RATE, 0) DISCOUNT_RATE,\n");
		sql.append("       NVL(TVDRD.DISCOUNT_S_PRICE, 0) DISCOUNT_S_PRICE,\n");
		sql.append("       NVL(TVDRD.DISCOUNT_PRICE, 0) DISCOUNT_PRICE,\n");
		sql.append("       TVDRD.VER,\n");
		sql.append("       NVL(VVR.STOCK_AMOUNT,0)-NVL(VVR.LOCK_AMOUT,0) AVA_STOCK,\n"); //可用资源数
		sql.append("       NVL(VVR.GENERAL_AMOUNT,0)-NVL(VVR.satisfy_general_order,0) GENERAL_AMOUNT,\n"); //未满足常规订单
		sql.append("       F_GETBATCH_NO(TVDRD.DETAIL_ID) BATCH_NO,\n");
		sql.append("       NVL(VVR.STOCK_AMOUNT,0)-NVL(VVR.LOCK_AMOUT,0) WARHOUSE_STOCK,\n");
		sql.append("       TVDR.DLVRY_REQ_NO DLVRY_REQ_NO\n");
		sql.append("  FROM TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       TT_VS_DLVRY_REQ TVDR,\n");
		sql.append("       TM_VHCL_MATERIAL TVM,\n");
		sql.append("       TT_VS_DLVRY_REQ_DTL TVDRD\n");
		sql.append("       LEFT JOIN  VW_VS_RESOURCE_ENTITY_WEEK_NEW VVR ON TVDRD.MATERIAL_ID = VVR.MATERIAL_ID AND VVR.COMPANY_ID =" + companyId+"\n");
		sql.append(" WHERE TVDR.REQ_ID = TVDRD.REQ_ID\n");
		sql.append("   AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID\n");
		sql.append("   AND TVDRD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVDRD.REQ_ID = " + reqId + "");*/
		sql.append("SELECT *                                                                            \n");
		sql.append("  FROM (SELECT TVDRD.DETAIL_ID,                                                     \n");
		sql.append("               TVDR.Dealer_Id,                                                      \n");
		sql.append("               VMI.series_id,                                                       \n");
		sql.append("               TVDRD.MATERIAL_ID,                                                   \n");
		sql.append("               TVM.MATERIAL_CODE,                                                   \n");
		sql.append("               TVM.MATERIAL_NAME,                                                   \n");
		sql.append("               TVDR.WAREHOUSE_ID,                                                   \n");
		sql.append("               decode(TVDRD.PATCH_NO, 'null', '', TVDRD.PATCH_NO) SPECIAL_BATCH_NO, \n");
		sql.append("               NVL(TVDRD.REQ_AMOUNT, 0) REQ_AMOUNT,                                 \n");
		sql.append("               NVL(TVDRD.DELIVERY_AMOUNT, 0) DELIVERY_AMOUNT,                       \n");
		sql.append("               TVDRD.ORDER_DETAIL_ID,                                               \n");
		sql.append("               NVL(TVDRD.RESERVE_AMOUNT, 0) RESERVE_AMOUNT,                         \n");
		sql.append("               NVL(TVDRD.SINGLE_PRICE, 0) SINGLE_PRICE,                             \n");
		sql.append("               NVL(TVDRD.TOTAL_PRICE, 0) + NVL(TVDRD.DISCOUNT_PRICE, 0) TOTAL_PRICE,\n");
		sql.append("               NVL(TVDRD.DISCOUNT_RATE, 0) DISCOUNT_RATE,                           \n");
		sql.append("               NVL(TVDRD.DISCOUNT_S_PRICE, 0) DISCOUNT_S_PRICE,                     \n");
		sql.append("               NVL(TVDRD.DISCOUNT_PRICE, 0) DISCOUNT_PRICE,                         \n");
		sql.append("               TVDRD.VER,                                                           \n");
		sql.append("               NVL(VVR.STOCK_AMOUNT, 0) - NVL(VVR.LOCK_AMOUT, 0) AVA_STOCK,         \n");
		sql.append("               NVL(VVR.GENERAL_AMOUNT, 0) -                                         \n");
		sql.append("               NVL(VVR.satisfy_general_order, 0) GENERAL_AMOUNT,                    \n");
		sql.append("               NVL(VVR.STOCK_AMOUNT, 0) - NVL(VVR.LOCK_AMOUT, 0) WARHOUSE_STOCK,    \n");
		sql.append("               TVDR.DLVRY_REQ_NO DLVRY_REQ_NO,                                       \n");
		sql.append("               TVDRD.REBATE_AMOUNT                                       \n");
		sql.append("          FROM TT_VS_ORDER_DETAIL  TVOD,                                            \n");
		sql.append("               TT_VS_DLVRY_REQ     TVDR,                                            \n");
		sql.append("               TM_VHCL_MATERIAL    TVM,                                             \n");
		sql.append("               VW_MATERIAL_INFO    VMI,                                             \n");
		sql.append("               TT_VS_DLVRY_REQ_DTL TVDRD                                            \n");
		sql.append("          LEFT JOIN (SELECT *                                                       \n");
		sql.append("                      FROM VW_VS_RESOURCE_ENTITY_WEEK_NEW                           \n");
		sql.append("                     WHERE WAREHOUSE_ID =                                           \n");
		sql.append("                           (SELECT WAREHOUSE_ID                                     \n");
		sql.append("                              FROM TT_VS_DLVRY_REQ                                  \n");
		sql.append("                             WHERE REQ_ID = "+reqId+")) VVR                         \n");
		sql.append("            ON TVDRD.MATERIAL_ID = VVR.MATERIAL_ID                                  \n");
		sql.append("           AND VVR.COMPANY_ID = "+companyId+"                                       \n");
		sql.append("         WHERE TVDR.REQ_ID = TVDRD.REQ_ID                                           \n");
		sql.append("           AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID                               \n");
		sql.append("           AND TVDRD.MATERIAL_ID = TVM.MATERIAL_ID                                  \n");
		sql.append("           AND TVDRD.REQ_ID = "+reqId+"                                             \n");
		sql.append("           AND TVDRD.MATERIAL_ID = VMI.material_id) INFO                            \n");
		sql.append("  LEFT JOIN VW_VS_PLAN_REQ_AMOUNT VPR                                               \n");
		sql.append("    ON INFO.DEALER_ID = VPR.DEALER_ID                                               \n");
		sql.append("   AND INFO.SERIES_ID = VPR.SERIES_ID                                               \n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	public List<Map<String, Object>> getWareHouseList(String companyId, Long aaa) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TW.WAREHOUSE_ID, TW.WAREHOUSE_NAME\n");
		sql.append("  FROM TM_WAREHOUSE TW\n");
		sql.append(" WHERE TW.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("  AND TW.WAREHOUSE_TYPE <> " + Constant.WAREHOUSE_TYPE_04 + "\n");
		sql.append("  AND TW.COMPANY_ID = " + companyId + "\n");
		sql.append(" ORDER BY TW.WAREHOUSE_LEVEL ASC");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	public List<Map<String, Object>> getWareHouseList(String companyId, String areaId) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append("select * from (");

		sql.append("SELECT DISTINCT TW.WAREHOUSE_ID, TW.WAREHOUSE_NAME, TW.WAREHOUSE_LEVEL\n");
		sql.append("  FROM TM_WAREHOUSE TW, TM_BUSINESS_AREA TBA\n");
		// sql.append(" WHERE TW.ENTITY_CODE = TBA.ERP_CODE\n");
		sql.append(" WHERE TW.ENTITY_CODE = TBA.ERP_CODE(+)\n");
		sql.append("   AND TW.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TW.WAREHOUSE_TYPE <> " + Constant.WAREHOUSE_TYPE_04 + "\n");
		sql.append("   AND TW.COMPANY_ID = " + companyId + "\n");
		// sql.append(" AND TBA.AREA_ID IN ("+areaId+")\n");
		sql.append("   AND TBA.AREA_ID(+) IN (" + areaId + ")\n");

		sql.append(") temp ORDER BY case\n");
		sql.append("            when temp.warehouse_level = ").append(Constant.WAREHOUSE_LEVEL_01).append(" then\n");
		sql.append("             1\n");
		sql.append("            else\n");
		sql.append("             2\n");
		sql.append("          end\n");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	public List<Map<String, Object>> getWareHouseAllList(String companyId, String areaId) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append("select * from (SELECT DISTINCT TW.WAREHOUSE_ID, TW.WAREHOUSE_TYPE, TW.WAREHOUSE_CODE, TW.WAREHOUSE_NAME, TW.WAREHOUSE_LEVEL\n");
		sql.append("  FROM TM_WAREHOUSE TW, TM_BUSINESS_AREA TBA\n");
		sql.append(" WHERE TW.ENTITY_CODE = TBA.ERP_CODE\n");
		sql.append("   AND TW.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TW.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TBA.AREA_ID IN (" + areaId + ")) temp\n");
		sql.append(" ORDER BY temp.WAREHOUSE_LEVEL ASC");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	/**
	 * 批次号查询
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getPatchNoSelectQuery1(Map<String, Object> map) throws Exception {

		String wareHouseId = (String) map.get("wareHouseId");
		String materalId = (String) map.get("materalId");
		String companyId = (String) map.get("companyId");
		// String orderType = (String)map.get("orderType");
		// String specialBatchNo = (String)map.get("specialBatchNo");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT A.MATERIAL_ID,\n");
		sql.append("       A.MATERIAL_CODE,\n");
		sql.append("       A.MATERIAL_NAME,\n");
		sql.append("       A.BATCH_NO,\n");
		sql.append("       A.STOCK_AMOUNT - NVL(B.RESERVE_AMOUNT, 0) AVA_AMOUNT\n");
		sql.append("  FROM (SELECT TV.OEM_COMPANY_ID COMPANY_ID,\n");
		sql.append("               TV.MATERIAL_ID,\n");
		sql.append("               TVM.MATERIAL_CODE,\n");
		sql.append("               TVM.MATERIAL_NAME,\n");
		sql.append("               TV.BATCH_NO,\n");
		sql.append("               TV.SPECIAL_BATCH_NO,\n");
		sql.append("               COUNT(TV.VEHICLE_ID) STOCK_AMOUNT\n");
		sql.append("          FROM TM_VEHICLE TV, TM_WAREHOUSE W, TM_VHCL_MATERIAL TVM\n");
		sql.append("         WHERE TV.WAREHOUSE_ID = W.WAREHOUSE_ID\n");
		sql.append("           AND TV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("           AND W.WAREHOUSE_TYPE <> " + Constant.WAREHOUSE_TYPE_04 + " --特殊商品库\n");
//		sql.append("           AND TV.DEALER_ID IS NULL\n");
		// sql.append(" AND TV.ORG_TYPE IS NULL\n");
		sql.append("           AND TV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_02 + " --车厂库存\n");
		sql.append("           AND TV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + " --正常状态\n");
		/*
		 * if(Integer.parseInt(orderType)==Constant.ORDER_TYPE_03){ sql.append("
		 * AND TV.SPECIAL_BATCH_NO ="+specialBatchNo+"\n"); } else{ sql.append("
		 * AND TV.SPECIAL_BATCH_NO IS NULL\n"); }
		 */
		sql.append("           AND TV.OEM_COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TV.WAREHOUSE_ID = " + wareHouseId + "\n");
		sql.append("           AND TV.MATERIAL_ID = " + materalId + "\n");
		sql.append("         GROUP BY TV.MATERIAL_ID,\n");
		sql.append("               	  TVM.MATERIAL_CODE,\n");
		sql.append("                  TVM.MATERIAL_NAME,\n");
		sql.append("                  TV.BATCH_NO,\n");
		sql.append("                  TV.OEM_COMPANY_ID,\n");
		sql.append("                  TV.SPECIAL_BATCH_NO\n");
		sql.append("                  ) A,\n");
//		sql.append("       (SELECT OEM_COMPANY_ID COMPANY_ID,\n");
//		sql.append("               MATERIAL_ID,\n");
//		sql.append("               BATCH_NO,\n");
//		sql.append("               (SUM(NVL(AMOUNT, 0)) - SUM(NVL(DELIVERY_AMOUNT, 0))) RESERVE_AMOUNT --保留资源数量\n");
//		sql.append("          FROM TT_VS_ORDER_RESOURCE_RESERVE\n");
//		sql.append("         WHERE RESERVE_STATUS = " + Constant.RESOURCE_RESERVE_STATUS_01 + " --\n");
//		sql.append("           AND OEM_COMPANY_ID = " + companyId + "\n");
//		sql.append("           AND WAREHOUSE_ID = " + wareHouseId + "\n");
//		sql.append("           AND MATERIAL_ID = " + materalId + "\n");
//		sql.append("         GROUP BY OEM_COMPANY_ID, MATERIAL_ID, BATCH_NO) B\n");
		sql.append("  (SELECT COMPANY_ID,\n" );
		sql.append("           MATERIAL_ID,\n" );
		sql.append("           vvrr.UN_DLVRY_AMOUNT RESERVE_AMOUNT --保留资源数量\n" );
		sql.append("      FROM vw_vs_resource_entity_week  vvrr\n" );
		sql.append("     WHERE\n" );
//		sql.append("     --RESERVE_STATUS = 11581001\n" );
		sql.append("      COMPANY_ID = 2010010100070674\n" );
		sql.append("       AND WAREHOUSE_ID = "+wareHouseId+"\n" );
		sql.append("       AND MATERIAL_ID = "+materalId+" ) b");

		sql.append(" WHERE A.COMPANY_ID = B.COMPANY_ID(+)\n");
		sql.append("   AND A.MATERIAL_ID = B.MATERIAL_ID(+)\n");
//		sql.append("    AND NVL(A.BATCH_NO, 0) = NVL(B.BATCH_NO(+), 0)\n");
		sql.append("   ORDER BY A.BATCH_NO\n");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	
	public List<Map<String, Object>> getPatchNoSelectQuery(Map<String, Object> map) throws Exception {

		String wareHouseId = (String) map.get("wareHouseId");
		String materalId = (String) map.get("materalId");
		String companyId = (String) map.get("companyId");
		// String orderType = (String)map.get("orderType");
		// String specialBatchNo = (String)map.get("specialBatchNo");

		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT TVM.MATERIAL_ID, TVM.MATERIAL_CODE, TVM.MATERIAL_NAME,1 AS BATCH_NO,VV.STOCK_AMOUNT-VV.LOCK_AMOUT AVA_AMOUNT \n");
		sql.append("    FROM VW_VS_RESOURCE_ENTITY_WEEK_NEW VV, TM_VHCL_MATERIAL TVM \n");
		sql.append("   WHERE 1=1 AND TVM.MATERIAL_ID=VV.MATERIAL_ID \n");
		sql.append("   AND VV.MATERIAL_ID=" + materalId + "\n");
		sql.append("   AND VV.WAREHOUSE_ID=" + wareHouseId + "\n");
		sql.append("   AND VV.COMPANY_ID=" + companyId + "\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	
	
	
	public List<Map<String, Object>> getPatchNoSelectQueryForAdjust(Map<String, Object> map) throws Exception {

		String wareHouseId = (String) map.get("wareHouseId");
		String materalId = (String) map.get("materalId");
		String companyId = (String) map.get("companyId");
		String reqId = (String) map.get("reqId");

		StringBuffer sql = new StringBuffer();

		sql.append("WITH X AS (\n");
		sql.append("SELECT NVL(A.COMPANY_ID, B.COMPANY_ID) COMPANY_ID,\n");
		sql.append("       NVL(A.MATERIAL_ID, B.MATERIAL_ID) MATERIAL_ID,\n");
		sql.append("       NVL(A.MATERIAL_CODE, B.MATERIAL_CODE) MATERIAL_CODE,\n");
		sql.append("       NVL(A.MATERIAL_NAME, B.MATERIAL_NAME) MATERIAL_NAME,\n");
		sql.append("       NVL(A.BATCH_NO, B.BATCH_NO) BATCH_NO,\n");
		sql.append("       NVL(A.STOCK_AMOUNT, 0) - NVL(B.RESERVE_AMOUNT, 0) AVA_AMOUNT\n");
		sql.append("  FROM (SELECT TV.OEM_COMPANY_ID COMPANY_ID,\n");
		sql.append("               TV.MATERIAL_ID,\n");
		sql.append("               TVM.MATERIAL_CODE,\n");
		sql.append("               TVM.MATERIAL_NAME,\n");
		sql.append("               TV.SPECIAL_BATCH_NO,\n");
		sql.append("               TV.BATCH_NO,\n");
		sql.append("               COUNT(TV.VEHICLE_ID) STOCK_AMOUNT\n");
		sql.append("          FROM TM_VEHICLE TV, TM_WAREHOUSE W, TM_VHCL_MATERIAL TVM\n");
		sql.append("         WHERE TV.WAREHOUSE_ID = W.WAREHOUSE_ID\n");
		sql.append("           AND TV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("           AND W.WAREHOUSE_TYPE <> " + Constant.WAREHOUSE_TYPE_04 + " --特殊商品库\n");
		sql.append("           AND TV.DEALER_ID IS NULL\n");
		// sql.append(" AND TV.ORG_TYPE IS NULL\n");
		sql.append("           AND TV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_02 + " --车厂库存\n");
		sql.append("           AND TV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + " --正常状态\n");
		sql.append("           AND TV.OEM_COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TV.WAREHOUSE_ID = " + wareHouseId + "\n");
		sql.append("           AND TV.MATERIAL_ID = " + materalId + "\n");
		sql.append("         GROUP BY TV.MATERIAL_ID,\n");
		sql.append("                  TVM.MATERIAL_CODE,\n");
		sql.append("                  TVM.MATERIAL_NAME,\n");
		sql.append("                  TV.BATCH_NO,\n");
		sql.append("                  TV.OEM_COMPANY_ID,\n");
		sql.append("                  TV.SPECIAL_BATCH_NO) A\n");
		sql.append("  FULL OUTER JOIN (SELECT TVORR.OEM_COMPANY_ID COMPANY_ID,\n");
		sql.append("                          TVM.MATERIAL_ID,\n");
		sql.append("                          TVM.MATERIAL_CODE,\n");
		sql.append("                          TVM.MATERIAL_NAME,\n");
		sql.append("                          TVORR.BATCH_NO,\n");
		sql.append("                          (SUM(NVL(AMOUNT, 0)) -\n");
		sql.append("                          SUM(NVL(DELIVERY_AMOUNT, 0))) RESERVE_AMOUNT --保留资源数量\n");
		sql.append("                     FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR,\n");
		sql.append("                          TM_VHCL_MATERIAL             TVM\n");
		sql.append("                    WHERE TVORR.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("                      AND TVORR.RESERVE_STATUS = " + Constant.RESOURCE_RESERVE_STATUS_01 + "\n");
		sql.append("                      AND TVORR.OEM_COMPANY_ID = " + companyId + "\n");
		sql.append("                      AND TVORR.WAREHOUSE_ID = " + wareHouseId + "\n");
		sql.append("                      AND TVORR.MATERIAL_ID = " + materalId + "\n");
		sql.append("                    GROUP BY TVORR.OEM_COMPANY_ID,\n");
		sql.append("                             TVM.MATERIAL_ID,\n");
		sql.append("                             TVM.MATERIAL_CODE,\n");
		sql.append("                             TVM.MATERIAL_NAME,\n");
		sql.append("                             TVORR.BATCH_NO) B ON A.COMPANY_ID =\n");
		sql.append("                                                  B.COMPANY_ID\n");
		sql.append("                                              AND A.MATERIAL_ID =\n");
		sql.append("                                                  B.MATERIAL_ID\n");
		sql.append("                                              AND A.BATCH_NO = B.BATCH_NO\n");
		sql.append("\n");
		sql.append("),\n");
		sql.append("C AS (\n");
		sql.append("SELECT TVORR.OEM_COMPANY_ID COMPANY_ID,\n");
		sql.append("       TVORR.MATERIAL_ID,\n");
		sql.append("       TVORR.BATCH_NO,\n");
		sql.append("       SUM(NVL(TVORR.DELIVERY_AMOUNT, 0)) DELIVERY_AMOUNT --发运数量\n");
		sql.append("  FROM TT_VS_DLVRY_REQ_DTL TVDRD, TT_VS_ORDER_RESOURCE_RESERVE TVORR\n");
		sql.append(" WHERE TVDRD.DETAIL_ID = TVORR.REQ_DETAIL_ID\n");
		sql.append("   AND TVORR.OEM_COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVORR.WAREHOUSE_ID = " + wareHouseId + "\n");
		sql.append("   AND TVORR.MATERIAL_ID = " + materalId + "\n");
		sql.append("   AND TVDRD.REQ_ID = " + reqId + "\n");
		sql.append(" GROUP BY TVORR.OEM_COMPANY_ID, TVORR.MATERIAL_ID, TVORR.BATCH_NO\n");
		sql.append(")\n");
		sql.append("\n");
		sql.append("SELECT X.MATERIAL_ID,\n");
		sql.append("       X.MATERIAL_CODE,\n");
		sql.append("       X.MATERIAL_NAME,\n");
		sql.append("       X.BATCH_NO,\n");
		sql.append("       X.AVA_AMOUNT,\n");
		sql.append("       NVL(C.DELIVERY_AMOUNT, 0) DELIVERY_AMOUNT\n");
		sql.append("  FROM X, C\n");
		sql.append(" WHERE X.COMPANY_ID = C.COMPANY_ID(+)\n");
		sql.append("   AND X.MATERIAL_ID = C.MATERIAL_ID(+)\n");
		sql.append("   AND X.BATCH_NO = C.BATCH_NO(+)");

		/*
		 * sql.append("SELECT A.MATERIAL_ID,\n"); sql.append("
		 * A.MATERIAL_CODE,\n"); sql.append(" A.MATERIAL_NAME,\n"); sql.append("
		 * A.BATCH_NO,\n"); sql.append(" A.STOCK_AMOUNT - NVL(B.RESERVE_AMOUNT,
		 * 0) AVA_AMOUNT,\n"); sql.append(" NVL(C.DELIVERY_AMOUNT, 0)
		 * DELIVERY_AMOUNT\n"); sql.append(" FROM (SELECT TV.OEM_COMPANY_ID
		 * COMPANY_ID,\n"); sql.append(" TV.MATERIAL_ID,\n"); sql.append("
		 * TVM.MATERIAL_CODE,\n"); sql.append(" TVM.MATERIAL_NAME,\n");
		 * sql.append(" TV.SPECIAL_BATCH_NO,\n"); sql.append(" TV.BATCH_NO,\n");
		 * sql.append(" COUNT(TV.VEHICLE_ID) STOCK_AMOUNT\n"); sql.append(" FROM
		 * TM_VEHICLE TV, TM_WAREHOUSE W, TM_VHCL_MATERIAL TVM\n"); sql.append("
		 * WHERE TV.WAREHOUSE_ID = W.WAREHOUSE_ID\n"); sql.append(" AND
		 * TV.MATERIAL_ID = TVM.MATERIAL_ID\n"); sql.append(" AND
		 * W.WAREHOUSE_TYPE <> "+Constant.WAREHOUSE_TYPE_04+" --特殊商品库\n");
		 * sql.append(" AND TV.ORG_TYPE IS NULL\n"); sql.append(" AND
		 * TV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_02+" --车厂库存\n"); sql.append("
		 * AND TV.LOCK_STATUS = "+Constant.LOCK_STATUS_01+" --正常状态\n");
		 * sql.append(" AND TV.OEM_COMPANY_ID = "+companyId+"\n"); sql.append("
		 * AND TV.WAREHOUSE_ID = "+wareHouseId+"\n"); sql.append(" AND
		 * TV.MATERIAL_ID = "+materalId+"\n"); sql.append(" GROUP BY
		 * TV.MATERIAL_ID,\n"); sql.append(" TVM.MATERIAL_CODE,\n");
		 * sql.append(" TVM.MATERIAL_NAME,\n"); sql.append(" TV.BATCH_NO,\n");
		 * sql.append(" TV.OEM_COMPANY_ID,\n"); sql.append("
		 * TV.SPECIAL_BATCH_NO) A,\n"); sql.append(" (SELECT OEM_COMPANY_ID
		 * COMPANY_ID,\n"); sql.append(" MATERIAL_ID,\n"); sql.append("
		 * BATCH_NO,\n"); sql.append(" (SUM(NVL(AMOUNT, 0)) -
		 * SUM(NVL(DELIVERY_AMOUNT, 0))) RESERVE_AMOUNT --保留资源数量\n");
		 * sql.append(" FROM TT_VS_ORDER_RESOURCE_RESERVE\n"); sql.append("
		 * WHERE RESERVE_STATUS = "+Constant.RESOURCE_RESERVE_STATUS_01+"\n");
		 * sql.append(" AND OEM_COMPANY_ID = "+companyId+"\n"); sql.append(" AND
		 * WAREHOUSE_ID = "+wareHouseId+"\n"); sql.append(" AND MATERIAL_ID =
		 * "+materalId+"\n"); sql.append(" GROUP BY OEM_COMPANY_ID, MATERIAL_ID,
		 * BATCH_NO) B,\n"); sql.append(" (SELECT TVORR.OEM_COMPANY_ID
		 * COMPANY_ID,\n"); sql.append(" TVORR.MATERIAL_ID,\n"); sql.append("
		 * TVORR.BATCH_NO,\n"); sql.append(" SUM(NVL(TVORR.DELIVERY_AMOUNT, 0))
		 * DELIVERY_AMOUNT --发运数量\n"); sql.append(" FROM TT_VS_DLVRY_REQ_DTL
		 * TVDRD, TT_VS_ORDER_RESOURCE_RESERVE TVORR\n"); sql.append(" WHERE
		 * TVDRD.DETAIL_ID = TVORR.REQ_DETAIL_ID\n"); sql.append(" AND
		 * TVORR.OEM_COMPANY_ID = "+companyId+"\n"); sql.append(" AND
		 * TVORR.WAREHOUSE_ID = "+wareHouseId+"\n"); sql.append(" AND
		 * TVORR.MATERIAL_ID = "+materalId+"\n"); sql.append(" AND TVDRD.REQ_ID =
		 * "+reqId+"\n"); sql.append(" GROUP BY TVORR.OEM_COMPANY_ID,
		 * TVORR.MATERIAL_ID, TVORR.BATCH_NO) C\n"); sql.append(" WHERE
		 * A.COMPANY_ID = B.COMPANY_ID(+)\n"); sql.append(" AND A.MATERIAL_ID =
		 * B.MATERIAL_ID(+)\n"); sql.append(" AND NVL(A.BATCH_NO, 0) =
		 * NVL(B.BATCH_NO(+), 0)\n"); sql.append(" AND A.COMPANY_ID =
		 * C.COMPANY_ID(+)\n"); sql.append(" AND A.MATERIAL_ID =
		 * C.MATERIAL_ID(+)\n"); sql.append(" AND NVL(A.BATCH_NO, 0) =
		 * NVL(C.BATCH_NO(+), 0)\n"); sql.append(" ORDER BY A.BATCH_NO");
		 */

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	/**
	 * Function : 订单审核审核日志查询
	 * 
	 * @param :
	 *            订单发运申请ID
	 * @return : 订单详细信息
	 * @throws :
	 *             Exception LastUpdate : 2010-09-23
	 */
	public List<Map<String, Object>> getReqCheckList(String reqId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TO_CHAR(TVOC.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE,\n");
		sql.append("       TMO.ORG_NAME,\n");
		sql.append("       TCU.NAME USER_NAME,\n");
		sql.append("       TCC.CODE_DESC CHECK_STATUS,\n");
		sql.append("       TVOC.CHECK_DESC\n");
		sql.append("  FROM TT_VS_REQ_CHECK TVOC, TM_ORG TMO, TC_USER TCU, TC_CODE TCC\n");
		sql.append(" WHERE TVOC.CHECK_ORG_ID = TMO.ORG_ID\n");
		sql.append("   AND TVOC.CHECK_USER_ID = TCU.USER_ID\n");
		sql.append("   AND TVOC.CHECK_STATUS = TCC.CODE_ID\n");
		if (reqId != null && !"".equals(reqId)) {
			sql.append("   AND TVOC.REQ_ID = " + reqId + "\n");
		}
		sql.append(" ORDER BY TVOC.CHECK_DATE,TCC.CODE_ID ASC");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	/**
	 * Function : 查询车厂在库车辆批次号
	 */
	public List<Map<String, Object>> getBatchNOList() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT TMV.BATCH_NO BATCH_NO\n");
		sql.append("  FROM TM_VEHICLE TMV\n");
		sql.append(" WHERE TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_02 + "\n");
		sql.append("UNION\n");
		sql.append("SELECT DISTINCT TOR.BATCH_NO BATCH_NO FROM TT_VS_ORDER_RESOURCE_RESERVE TOR\n");
		sql.append("ORDER BY BATCH_NO DESC\n");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	public PageResult<Map<String, Object>> getDlvryResourceAdjustQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String areaId = (String) map.get("areaId");
		String area = (String) map.get("area");
		String groupCode = (String) map.get("groupCode");
		String dealerCode = (String) map.get("dealerCode");
		String orderType = (String) map.get("orderType");
		String orderNo = (String) map.get("orderNo");
		String dlvNo = (String) map.get("dlvNo");
		String companyId = (String) map.get("companyId");
		String deliveryStatus = (String) map.get("deliveryStatus");
		String orgCode = (String) map.get("orgCode");
		String orgId = (String) map.get("orgId");

		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT DISTINCT TVO.ORDER_ID,\n");
		sql.append("                TVDR.REQ_ID,\n");
		sql.append("                TMD.DEALER_CODE,\n");
		sql.append("                TMD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("                TVO.ORDER_NO,\n");
		sql.append("                TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("                TVO.ORDER_YEAR,\n");
		sql.append("                TVO.ORDER_WEEK,\n");
		sql.append("                TVO.ORDER_TYPE,\n");
		sql.append("                TVD.DELIVERY_STATUS,\n");
		sql.append("                TVD.DELIVERY_NO,\n");
		sql.append("                NVL(TVO.VER, 0) VER,\n");
		sql.append("                TVDR.REQ_TOTAL_AMOUNT,\n");
		sql.append("                TVDR.RESERVE_TOTAL_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_DLVRY_REQ          TVDR,\n");
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n");
		sql.append("       TT_VS_DLVRY              TVD,\n");
		sql.append("       TM_DEALER                TMD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG\n");
		sql.append(" WHERE TVO.ORDER_ID = TVDR.ORDER_ID\n");
		sql.append("   AND TVDR.REQ_ID = TVD.REQ_ID\n");
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TVDRD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		// sql.append(" AND TVD.ERP_ORDER IS NOT NULL\n");
		sql.append("   AND TVD.DELIVERY_STATUS IN (" + Constant.DELIVERY_STATUS_04 + ", " + Constant.DELIVERY_STATUS_07 + ", " + Constant.DELIVERY_STATUS_08 + ", " + Constant.DELIVERY_STATUS_10 + ", " + Constant.DELIVERY_STATUS_11 + ")\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		if (orderType != null && !"".equals(orderType)) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		if (orderNo != null && !"".equals(orderNo)) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (dlvNo != null && !"".equals(dlvNo)) {
			sql.append("   AND TVD.DELIVERY_NO LIKE '%" + dlvNo + "%'\n");
		}
		if (orderYear != null && !"".equals(orderYear)) {
			sql.append("   AND TVO.ORDER_YEAR =" + orderYear + "\n");
		}
		if (orderWeek != null && !"".equals(orderWeek)) {
			sql.append("   AND TVO.ORDER_WEEK = " + orderWeek + "\n");
		}
		if (deliveryStatus != null && !"".equals(deliveryStatus)) {
			sql.append("   AND TVD.DELIVERY_STATUS = " + deliveryStatus + "\n");
		}
		if (areaId != null && !"-1".equals(areaId) && !"".equals(areaId)) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (area != null && "-1".equals(areaId) && !"".equals(area)) {
			sql.append("   AND TVO.AREA_ID IN( " + area + ")\n");
		}
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
		}
		if (dealerCode != null && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		if (orgCode != null && !"".equals(orgCode)) {
			sql.append("   AND TVO.BILLING_ORG_ID IN\n");
			sql.append("       (SELECT M.DEALER_ID\n");
			sql.append("          FROM TM_DEALER M\n");
			sql.append("        CONNECT BY PRIOR M.DEALER_ID = M.PARENT_DEALER_D\n");
			sql.append("         START WITH M.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("                AND M.DEALER_ID IN\n");
			sql.append("                    (SELECT REL.DEALER_ID\n");
			sql.append("                       FROM TM_DEALER_ORG_RELATION REL, TM_ORG ORG\n");
			sql.append("                      WHERE REL.ORG_ID = ORG.ORG_ID\n");
			sql.append("                        AND ORG.ORG_CODE IN\n");
			sql.append("                            (" + PlanUtil.createSqlStr(orgCode) + ")))");

		}

		if (!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId));
		}

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}

	public List<Map<String, Object>> getGeneralOrderDtlList(String orderId) {
		List<Object> params = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT TVOD.DETAIL_ID, NVL(TVOD.CHECK_AMOUNT, -1) CHECK_AMOUNT, NVL(TVOD.ORDER_AMOUNT, 0) ORDER_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER_DETAIL TVOD\n");
		sql.append(" WHERE TVOD.ORDER_ID = ?\n");
		params.add(orderId);

		List<Map<String, Object>> dtlMap = dao.pageQuery(sql.toString(), params, dao.getFunName());

		return dtlMap;
	}

	public int updateOrderDtl(Long dtlId, Long orderAmount, Date currTime, Long userId) {
		List<Object> params = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer("\n");

		sql.append("UPDATE TT_VS_ORDER_DETAIL TVOD SET TVOD.CHECK_AMOUNT = ?, TVOD.UPDATE_DATE = ?, TVOD.UPDATE_BY = ? WHERE TVOD.DETAIL_ID = ?\n");
		params.add(orderAmount);
		params.add(currTime);
		params.add(userId);
		params.add(dtlId);

		return dao.update(sql.toString(), params);
	}

	public PageResult<Map<String, Object>> getFleetOrderList(String status, String dlrCode, Long orgId, String startYear, String endYear, String startWeek, String endWeek, String areaId, String groupCode, String dealerCode, String orderType, String orderNo, Long companyId, String areaIds, int curPage, int pageSize) throws Exception {
		List<Object> params = new LinkedList<Object>();

		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT DISTINCT TVDR.REQ_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TVDR.DLVRY_REQ_NO,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_YEAR,\n");
		sql.append("       TVO.ORDER_WEEK,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       NVL(TVDR.VER, 0) VER,\n");
		sql.append("       TVDR.FLEET_ADDRESS\n");
		sql.append("  FROM TT_VS_DLVRY_REQ          TVDR,\n");
		sql.append("       TT_VS_ORDER              TVO,\n");
		sql.append("       TM_DEALER                TMD,\n");
		sql.append("       TM_DEALER                TMD1,\n");
		sql.append("       vw_org_dealer            VOD,\n");
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG\n");
		sql.append(" WHERE TVDR.ORDER_ID = TVO.ORDER_ID\n");
		sql.append("   AND TMD1.DEALER_ID = VOD.DEALER_ID\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TVDR.RECEIVER = TMD1.DEALER_ID\n");
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");
		sql.append("   AND TVDRD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVDR.REQ_STATUS = ").append(status).append("\n");
		sql.append("   AND (TVO.ORDER_STATUS = " + Constant.ORDER_STATUS_03 + " OR TVO.ORDER_STATUS = " + Constant.ORDER_STATUS_05 + ")\n");

		if (null != orgId && !"".equals(orgId.toString())) {
			sql.append("   AND VOD.ROOT_ORG_ID = " + orgId + "\n");
		}

		if (orderType != null && !"".equals(orderType)) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}

		if (orderNo != null && !"".equals(orderNo)) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}

		if (areaId != null && !"".equals(areaId)) {
			sql.append("   AND TVDR.AREA_ID = " + areaId + "\n");
		}

		if (groupCode != null && !"".equals(groupCode)) {
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
		}

		if (dealerCode != null && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}

		if (dlrCode != null && !"".equals(dlrCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dlrCode, params, "TMD1", "DEALER_CODE"));
		}

		if (areaIds != null && !"".equals(areaIds)) {
			sql.append("   AND TVDR.AREA_ID IN (" + areaIds + ")\n");
		}

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}

	public PageResult<Map<String, Object>> getFleetOrderSureList(String dlrCode, Long orgId, String startYear, String endYear, String startWeek, String endWeek, String areaId, String groupCode, String dealerCode, String orderType, String orderNo, Long companyId, String areaIds, int curPage, int pageSize) throws Exception {
		return getFleetOrderList(Constant.ORDER_REQ_STATUS_DJCQR.toString(), dlrCode, orgId, startYear, endYear, startWeek, endWeek, areaId, groupCode, dealerCode, orderType, orderNo, companyId, areaIds, curPage, pageSize);
	}
	// /**
	// * 传入<b>发运申请 ID</b>将释放此发运申请之前冻结的金额
	// * 如果使用了折让也将会释放折让金额
	// * @param dlvryRegId 发运申请 ID
	// */
	// public void releaseAccountFreezeAmount(String dlvryRegId){
	// Map data = getAccountData(dlvryRegId);
	// BigDecimal accountId = null;
	// BigDecimal accountPrice = null;
	// BigDecimal dicountPrice = null;
	// BigDecimal dealerId = null;
	//				
	// if(data != null){
	// accountId = (BigDecimal) data.get("ACCOUNT_ID");
	// accountPrice = (BigDecimal) data.get("ACTUAL_PRICE");
	// dealerId = (BigDecimal) data.get("DEALER_ID");
	// dicountPrice = (BigDecimal) data.get("DISCOUNT_PRICE");
	//			
	// }
	// //释放非折扣账户冻结金额
	// releaseFreezeAmountByAccountId(accountId, accountPrice);
	//		
	//		
	// BigDecimal discountAccountId = null;
	// List list = getDiscountAccountInfoByDealerId(dealerId.toString());
	// if(list != null && list.size()>0){
	// Map resultMap = (Map)list.get(0);
	// discountAccountId = (BigDecimal) resultMap.get("ACCOUNT_ID,");
	// //释放折让账户冻结金额
	// releaseFreezeAmountByAccountId(discountAccountId, dicountPrice);
	// }
	//		
	//
	// }
	// /**
	// * @param accountId 释放账户ID
	// * @param accountPrice 释放账户金额
	// */
	// private void releaseFreezeAmountByAccountId(BigDecimal accountId,
	// BigDecimal accountPrice) {
	//		
	// List<BigDecimal> paras = new ArrayList<BigDecimal>();
	// StringBuffer sql = new StringBuffer();
	// sql.append("UPDATE tt_vs_account vsa\n");
	// sql.append(" SET vsa.available_amount = vsa.available_amount + ?,\n");
	// paras.add(accountPrice);
	// sql.append(" vsa.freeze_amount = vsa.freeze_amount - ?\n");
	// paras.add(accountPrice);
	// sql.append(" WHERE vsa.account_id = ?");
	// paras.add(accountId);
	//		
	// dao.update(sql.toString(), paras);
	// }
	//	
	// /**
	// * 得到账户信息
	// * <code>Map</code> 中包含 账户ID(ACCOUNT_ID)和 要释放的金额 (ACTUAL_PRICE)
	// * 以及折扣金额
	// * @param dlvryRegId 发运申请 ID
	// * @return
	// */
	// private Map<String,Object> getAccountData(String dlvryRegId){
	//
	// StringBuffer sql = new StringBuffer();
	// sql.append("SELECT x.account_id, x.total_price
	// actual_price,X.DISCOUNT_PRICE,x.DEALER_ID\n");
	// sql.append(" FROM (SELECT va.account_id,\n");
	// sql.append(" re.req_id,\n");
	// sql.append(" O.BILLING_ORG_ID DEALER_ID,\n");
	// sql.append(" SUM(nvl(red.total_price,0)) AS total_price,\n");
	// sql.append(" SUM(nvl(red.discount_price,0)) AS discount_price\n");
	// sql.append(" FROM tt_vs_dlvry_req_dtl red,\n");
	// sql.append(" tt_vs_dlvry_req re,\n");
	// sql.append(" tt_vs_order o,\n");
	// sql.append(" tt_vs_account va\n");
	// sql.append(" WHERE re.req_id = red.req_id\n");
	// sql.append(" AND o.order_id = re.order_id\n");
	// sql.append(" AND va.account_type_id = o.fund_type_id\n");
	// sql.append(" AND va.dealer_id = o.billing_org_id\n");
	// sql.append(" AND re.req_id = "+ dlvryRegId +"\n");
	// sql.append(" GROUP BY va.account_id, re.req_id ,O.BILLING_ORG_ID) x");
	//		
	// List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null,
	// getFunName());
	//		
	// if(list == null || list.size() != 1 ) {
	// return null;
	// }
	//		
	// return list.get(0);
	// }
	//	
	// /**
	// * 通过经销商ID取得<b>非折扣</b>账户信息
	// * @param dealerId
	// * @return
	// */
	// public List<Map<String,Object>> getNoDiscountAccountInfoByDealerId(String
	// dealerId){
	//
	// StringBuffer sql = new StringBuffer();
	// sql.append("SELECT ATP.TYPE_CODE,\n");
	// sql.append(" ATP.TYPE_NAME,\n");
	// sql.append(" ATP.TYPE_ID,\n");
	// sql.append(" A.ACCOUNT_ID,\n");
	// sql.append(" NVL(A.BALANCE_AMOUNT,0) BALANCE_AMOUNT,\n");
	// sql.append(" NVL(A.FREEZE_AMOUNT,0) FREEZE_AMOUNT,\n");
	// sql.append(" NVL(A.AVAILABLE_AMOUNT,0) AVAILABLE_AMOUNT\n");
	// sql.append(" FROM TT_VS_ACCOUNT A, TT_VS_ACCOUNT_TYPE ATP\n");
	// sql.append(" WHERE A.ACCOUNT_TYPE_ID = ATP.TYPE_ID\n");
	// sql.append(" AND ATP.IS_DISCOUNT = 0\n");
	// sql.append(" AND a.DEALER_ID = "+dealerId);
	//		
	// List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null,
	// getFunName());
	// if(list == null || list.size()<1){
	// return null;
	// }
	// return list;
	//
	// }
	//	
	// /**
	// * 通过经销商ID取得<b>折扣</b>账户信息
	// * @param dealerId
	// * @return
	// */
	// public List<Map<String,Object>> getDiscountAccountInfoByDealerId(String
	// dealerId){
	//
	// StringBuffer sql = new StringBuffer();
	// sql.append("SELECT ATP.TYPE_CODE,\n");
	// sql.append(" ATP.TYPE_NAME,\n");
	// sql.append(" ATP.TYPE_ID,\n");
	// sql.append(" A.ACCOUNT_ID,\n");
	// sql.append(" NVL(A.BALANCE_AMOUNT,0) BALANCE_AMOUNT,\n");
	// sql.append(" NVL(A.FREEZE_AMOUNT,0) FREEZE_AMOUNT,\n");
	// sql.append(" NVL(A.AVAILABLE_AMOUNT,0) AVAILABLE_AMOUNT\n");
	// sql.append(" FROM TT_VS_ACCOUNT A, TT_VS_ACCOUNT_TYPE ATP\n");
	// sql.append(" WHERE A.ACCOUNT_TYPE_ID = ATP.TYPE_ID\n");
	// sql.append(" AND ATP.IS_DISCOUNT = 1\n");
	// sql.append(" AND a.DEALER_ID = "+dealerId);
	//		
	// List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null,
	// getFunName());
	// if(list == null || list.size()<1){
	// return null;
	// }
	// return list;
	//
	// }
	//	
	// /**
	// * 通过经销商ID取得<b>全部</b>账户信息
	// * @param dealerId
	// * @return
	// */
	// public List<Map<String,Object>> getAllAccountInfoByDealerId(String
	// dealerId){
	//
	// StringBuffer sql = new StringBuffer();
	// sql.append("SELECT ATP.TYPE_CODE,\n");
	// sql.append(" ATP.TYPE_NAME,\n");
	// sql.append(" ATP.TYPE_ID,\n");
	// sql.append(" ATP.IS_DISCOUNT,\n");
	// sql.append(" A.ACCOUNT_ID,\n");
	// sql.append(" NVL(A.BALANCE_AMOUNT,0) BALANCE_AMOUNT,\n");
	// sql.append(" NVL(A.FREEZE_AMOUNT,0) FREEZE_AMOUNT,\n");
	// sql.append(" NVL(A.AVAILABLE_AMOUNT,0) AVAILABLE_AMOUNT\n");
	// sql.append(" FROM TT_VS_ACCOUNT A, TT_VS_ACCOUNT_TYPE ATP\n");
	// sql.append(" WHERE A.ACCOUNT_TYPE_ID = ATP.TYPE_ID\n");
	// sql.append(" AND a.DEALER_ID = "+dealerId);
	//		
	// List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null,
	// getFunName());
	// if(list == null || list.size()<1){
	// return null;
	// }
	// return list;
	//
	// }
	
	
	/**
	 * 根据经销商ID获取经销商
	 * 
	 * @param id
	 * @return
	 */
	public  TmDealerPO getDlrByID(Long id) {
		TmDealerPO po = new TmDealerPO();
		po.setDealerId(id);
		List<TmDealerPO> list = factory.select(po);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
	/**
	 * 根据经销商ID获取经销商地址列表
	 * 
	 * @param id
	 * @return
	 */
	public  TmVsAddressPO getDlrAddressByID(Long id) {
		TmVsAddressPO po = new TmVsAddressPO();
		po.setDealerId(id);
		List<TmVsAddressPO> list = factory.select(po);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
	
	/**
	 * 根据PO获取PO信息
	 * 
	 * @param id
	 * @return
	 */
	public  PO getPoObject(PO po) {
		List<PO> list = factory.select(po);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}	
	public List<Map<String,Object>> balanceSelect(Map<String,String> map){
		String dealerId=map.get("dealerId");
		List<Map<String,Object>> ps=null;
		StringBuilder sql= new StringBuilder();
		/*sql.append("SELECT T.DEALER_NAME, T.DEALER_CODE,TVAT.TYPE_NAME, T.BALANCE_AMOUNT\n" );
		sql.append("  FROM CUX_DMS_ACOUNT_V@DMS2EBS2 T, TT_VS_ACCOUNT_TYPE TVAT\n" );
		sql.append(" WHERE TVAT.MARK_CODE = T.ACCOUNT_CODE\n" );
		sql.append("   AND DEALER_CODE = '"+dealerCode+"'");*/
		sql.append("select c.dealer_id,                                                    \n");
		sql.append("       c.dealer_code,                                                  \n");
		sql.append("       c.dealer_name,                                                  \n");
		sql.append("       b.type_id,                                                      \n");
		sql.append("       b.type_name,                                                    \n");
		sql.append("       sum(balance_amount) as balance_amount,                          \n");
		sql.append("       sum(asfrozen_amount) as asfrozen_amount,                        \n");
		sql.append("       sum(balance_amount) - sum(asfrozen_amount) as enable_amount     \n");
		sql.append("  from (select tva.dealer_id,                                          \n");
		sql.append("               tvat.type_id,                                           \n");
		sql.append("               tva.balance_amount,                                     \n");
		sql.append("               0 asfrozen_amount                                       \n");
		sql.append("          from tt_vs_account tva, tt_vs_account_type tvat              \n");
		sql.append("         where tva.account_type_id = tvat.type_id                      \n");
		sql.append("        union                                                          \n");
		sql.append("        select vm.dealer_id,                                           \n");
		sql.append("               vm.fund_type,                                           \n");
		sql.append("               0 as balance_amount,                                    \n");
		sql.append("               vm.frozen_amount                                        \n");
		sql.append("          from (select *                                               \n");
		sql.append("                  from vw_account_frozen_amount vm                     \n");
		sql.append("                union                                                  \n");
		sql.append("                select tva.dealer_id,                                  \n");
		sql.append("                       tvat.type_id,                                   \n");
		sql.append("                       nvl(loc.loc_Amount, 0) REBATE_AMOUNT            \n");
		sql.append("                  from tt_vs_account tva,                              \n");
		sql.append("                       tt_vs_account_type tvat,                        \n");
		sql.append("                       (select tvdr.dealer_id,                         \n");
		sql.append("                               sum(decode(tvdr.is_rebate,              \n");
		sql.append("                                          10041001,                    \n");
		sql.append("                                          (decode(tvdr.is_rebate_bill, \n");
		sql.append("                                                  10041001,            \n");
		sql.append("                                                  0,                   \n");
		sql.append("                                                  tvdr.rebate_amount)),\n");
		sql.append("                                          0)) loc_Amount               \n");
		sql.append("                          from tt_vs_dlvry_req tvdr                    \n");
		sql.append("                         where tvdr.req_status in                      \n");
		sql.append("                               (11571003,                              \n");
		sql.append("                                11571005,                              \n");
		sql.append("                                11571014,                              \n");
		sql.append("                                11571016,                              \n");
		sql.append("                                11571017)                              \n");
		sql.append("                         group by tvdr.dealer_id) loc                  \n");
		sql.append("                 where tva.account_type_id = tvat.type_id              \n");
		sql.append("                   and tva.dealer_id = loc.dealer_id                   \n");
		sql.append("                   and tvat.type_code = '2001') vm) a,                 \n");
		sql.append("       tt_vs_account_type b,                                           \n");
		sql.append("       tm_dealer c                                                     \n");
		sql.append(" where a.dealer_id = c.dealer_id                                       \n");
		sql.append("   and a.type_id = b.type_id                                           \n");
		sql.append("   and a.dealer_id = "+dealerId+"                               \n");
		sql.append(" group by c.dealer_id,                                                 \n");
		sql.append("       c.dealer_code,                                                  \n");
		sql.append("       c.dealer_name,                                                  \n");
		sql.append("       b.type_id,                                                      \n");
		sql.append("       b.type_name                                                     \n");
		sql.append("                                                                       \n");
		ps=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return ps;
	}
	/**
	 * 未满足常规订单的明细信息
	 * @return
	 */
	public static List <Map<String,Object>> getUnFitNormalOrderDetail(String material_id){
		List<Map<String,Object>> list=null;
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TD.DEALER_CODE,\n" );
		sql.append("       TD.DEALER_SHORTNAME,\n" );
		sql.append("       TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("       T.ORDER_NO,\n" );
		sql.append("       T.AMOUNT UN_AMOUNT\n" );
		sql.append("  FROM VW_VS_UNENTITY_ORDER_DETAIL T,\n" );
		sql.append("       TM_DEALER                 TD,\n" );
		//sql.append("       TT_VS_ORDER               TVO,\n" );
		sql.append("       TM_VHCL_MATERIAL          TVM\n" );
		sql.append(" WHERE T.ORDER_ORG_ID = TD.DEALER_ID\n" );
		//sql.append("   AND TVO.ORDER_ID(+) = T.ORDER_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = T.MATERIAL_ID\n" );
		sql.append("   AND T.MATERIAL_ID='"+material_id+"'");
		//sql.append("   AND T.N_UN_REMAIN_AMOUNT!=0");

		
		list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	/**
	 * 获取锁定资源情况
	 * 资源如果够用就是0
	 * 不够用就是1
	 * @return
	 */
	public  int getResourceStatus(String [] material_id,String[] resAmount,String orderType,String warehouseId){
		int flag=0;
		for (int k=0;k<material_id.length;k++){
			Map<String,Object> map=null;
			StringBuilder sql= new StringBuilder();
			sql.append("SELECT (NVL(VRE.STOCK_AMOUNT,0)-NVL(VRE.LOCK_AMOUT,0)) AVA_STOCK\n" );
			sql.append("  FROM VW_VS_RESOURCE_ENTITY_WEEK_NEW VRE\n" );
			sql.append(" WHERE VRE.MATERIAL_ID = "+material_id[k]);
			sql.append(" AND VRE.WAREHOUSE_ID="+warehouseId);
			map=dao.pageQueryMap(sql.toString(), null, dao.getFunName());
			int curNumber=0;
			if("10201001".equals(orderType)){
				curNumber=Integer.parseInt(map==null?"0":map.get("ACT_STOCK").toString());
			}else{
				curNumber=Integer.parseInt(map==null?"0":map.get("AVA_STOCK").toString());
			}
			
			int resNumber=Integer.parseInt(resAmount[k]);
			if(resNumber!=0&&curNumber-resNumber<0){
				flag=1;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 验证可用余额
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDealerBalanceAmountDao(String dealerId){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT VOD.DEALER_CODE,\n" );
		sql.append("       VOD.DEALER_SHORTNAME,\n" );
		sql.append("       TR1.ORG_NAME,\n" );
		sql.append("       SUM(NVL(DT.BALANCE_AMOUNT, 0)) BALANCE_AMOUNT,\n" );
		sql.append("       SUM(NVL(DT.OUTSTANDING_ACCOUNT, 0)) OUTSTANDING_ACCOUNT,\n" );
		sql.append("       SUM(NVL(DT.BALANCE_AMOUNT, 0) - NVL(DT.OUTSTANDING_ACCOUNT, 0)) TOTAL_AMOUNT\n" );
		sql.append("  FROM (SELECT 0 BALANCE_AMOUNT,\n" );
		sql.append("               SUM((NVL(L.REQ_AMOUNT, 0) - NVL(L.BILL_AMOUNT, 0)) *\n" );
		sql.append("                   NVL(L.SINGLE_PRICE, 0)) OUTSTANDING_ACCOUNT,\n" );
		sql.append("               TVO.ORDER_ORG_ID DEALER_ID\n" );
		sql.append("          FROM TT_VS_DLVRY_REQ_DTL L, TT_VS_DLVRY_REQ R, TT_VS_ORDER TVO\n" );
		sql.append("         WHERE L.REQ_ID = R.REQ_ID\n" );
		sql.append("           AND TVO.ORDER_ID = R.ORDER_ID\n" );
		sql.append("           AND TVO.ORDER_ORG_ID="+dealerId+"\n" );
		sql.append("           AND R.REQ_STATUS = 11571014\n" );
		sql.append("         GROUP BY TVO.ORDER_ORG_ID\n" );
		sql.append("        UNION\n" );
		sql.append("        SELECT SUM(NVL(T.BALANCE_AMOUNT, 0)) BALANCE_AMOUNT,\n" );
		sql.append("               0 OUTSTANDING_ACCOUNT,\n" );
		sql.append("               VOD.DEALER_ID DEALER_ID\n" );
		sql.append("          FROM CUX_DMS_ACOUNT_V@DMS2EBS2 T,\n" );
		sql.append("               TT_VS_ACCOUNT_TYPE        TV,\n" );
		sql.append("               VW_ORG_DEALER             VOD\n" );
		sql.append("         WHERE T.DEALER_CODE = VOD.DEALER_CODE\n" );
		sql.append("           AND T.ACCOUNT_CODE = TV.TYPE_CODE\n" );
		sql.append("           AND VOD.DEALER_ID="+dealerId+"\n" );
		sql.append("         GROUP BY VOD.DEALER_ID) DT,\n" );
		sql.append("       TM_ORG TR,\n" );
		sql.append("       TM_ORG TR1,\n" );
		sql.append("       VW_ORG_DEALER VOD\n" );
		sql.append(" WHERE TR.PARENT_ORG_ID = TR1.ORG_ID\n" );
		sql.append("   AND TR.ORG_CODE = VOD.PQ_ORG_CODE\n" );
		sql.append("   AND VOD.DEALER_ID = DT.DEALER_ID\n" );

		
		sql.append(" GROUP BY VOD.DEALER_CODE, VOD.DEALER_SHORTNAME, TR1.ORG_NAME\n");
		sql.append("  ORDER BY TR1.ORG_NAME \n");

		 List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getDelvyInfo(String reqId) {
		List<Object> params = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT t.req_id,                                                                   \n");      
		sql.append("         t.dlvry_req_no,                                                           \n");      
		sql.append("         t.req_total_amount,                                                       \n");      
		sql.append("         t.delivery_type,                                                          \n");      
		sql.append("         t.req_date,                                                               \n");      
		sql.append("         t.receiver,                                                               \n");      
		sql.append("         t.address_id,                                                             \n");      
		sql.append("         decode(o.is_custom_addr,10041001,o.custom_addr,a.address) address,\n");      
		sql.append("         decode(o.is_custom_addr,10041001,o.custom_link_man,a.link_man) link_man,\n");                  
		sql.append("         decode(o.is_custom_addr,10041001,o.custom_tel,a.tel) tel,\n");       
		//sql.append("         a.address,                                                                \n");      
		//sql.append("         a.link_man,                                                               \n");      
		//sql.append("         a.tel,                                                                    \n");      
		sql.append("         t.warehouse_id,                                                           \n");      
		sql.append("         t.req_remark,                                                             \n");      
		sql.append("         t.order_id,                                                               \n");
		sql.append("         o.order_no,                                                               \n");
		sql.append("         t.order_dealer_id,                                                        \n");      
		sql.append("         t.reserve_total_amount,                                                   \n");      
		sql.append("         12131001 as dlv_type,                                                                 \n");      
		sql.append("         t.delivery_type,                                                          \n");      
		sql.append("         10211001 as dlv_status,                     \n");      
		sql.append("         t.warehouse_id,                                                           \n");      
		sql.append("         decode(o.is_custom_addr,10041001,o.province_id,a.province_id) province_id,\n");      
		sql.append("         decode(o.is_custom_addr,10041001,o.city_id,a.city_id) city_id,\n");                  
		sql.append("         decode(o.is_custom_addr,10041001,o.town_id,a.area_id) town_id,\n");                  
		sql.append("         --a.city_id,                                                  \n");                  
		sql.append("         --a.area_id,                                                  \n");                  
		sql.append("         10041002  as dlv_is_zz  \n");                  
		sql.append("    FROM tt_vs_dlvry_req t, tm_vs_address a,tt_vs_order o              \n");                  
		sql.append("   WHERE t.address_id = a.ID(+) and t.order_id=o.order_id                 \n");                  
		sql.append("   and t.req_id=?");                        

		params.add(reqId);

		List<Map<String, Object>> dtlMap = dao.pageQuery(sql.toString(), params, dao.getFunName());

		return dtlMap;
	}
	
	public List<Map<String, Object>> getDelvyDtlInfo(String reqId) {
		List<Object> params = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer("\n");
		
		//sql.append(" SELECT detail_id,\n");
		//sql.append("    req_id,\n");
		//sql.append("   order_detail_id,\n");
		//sql.append("   material_id,\n");
		//sql.append("   RESERVE_AMOUNT,\n");
		//sql.append("    dealer_req_amount\n");
		//sql.append("  FROM tt_vs_dlvry_req_dtl \n");
		sql.append("  SELECT tvdrd.detail_id, \n");
		sql.append("    tvdrd.req_id, \n");
		sql.append("    tvod.order_id, \n");
		sql.append("   tvdrd.order_detail_id, \n");
		sql.append("   tvdrd.material_id, \n");
		sql.append("   tvdrd.RESERVE_AMOUNT, \n");
		sql.append("    tvdrd.dealer_req_amount \n");
		sql.append("  FROM tt_vs_dlvry_req_dtl tvdrd,tt_vs_order_detail tvod   \n");
		sql.append("   where tvdrd.order_detail_id=tvod.detail_id \n");
		sql.append("   and req_id="+reqId);
		
		
		List<Map<String, Object>> dtlMap = dao.pageQuery(sql.toString(), params, dao.getFunName());

		return dtlMap;
		
	}
}
