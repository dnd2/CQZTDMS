/**
 * @Title: OrderReport.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-24
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.dao.sales.ordermanage.orderreport;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;





import com.infodms.dms.actions.sales.ordermanage.orderreport.UrgentOrderReport;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmDealerPriceRelationPO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsAccountRebatePO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetOrderNOUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmWarehousePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.callbackimpl.POCallBack;

/**
 * @author yuyong
 * 
 */
public class OrderReportDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(OrderReportDao.class);
	private static final OrderReportDao dao = new OrderReportDao();

	public static final OrderReportDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * 
	 * 获取经销商省份
	 */

	public Map<String,Object> getDealerAreas(String dealerId){
		StringBuffer sql=new StringBuffer();
		sql.append("select * from vw_org_dealer where dealer_id="+dealerId);
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	/**
	 * 根据当前登录角色过滤车系范围
	 * 
	 * @param poseId
	 * @return
	 */
	public List<Map<String, Object>> getSerieList(Long poseId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVMG.GROUP_ID, TVMG.GROUP_NAME\n");
		sql.append("  FROM TC_POSE                TP,\n");
		sql.append("       TM_POSE_BUSINESS_AREA  TPBA,\n");
		sql.append("       TM_AREA_GROUP          TAG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append(" WHERE TP.POSE_ID = TPBA.POSE_ID\n");
		sql.append("   AND TPBA.AREA_ID = TAG.AREA_ID\n");
		sql.append("   AND TAG.MATERIAL_GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TP.POSE_ID = " + poseId + "");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getSeriesList");
		return list;
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
		po.setOemCompanyId(companyId);
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

	public List<Map<String, String>> getGeneralDateList(Long companyId) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		TmDateSetPO dateSet = getTmDateSetPO(new Date(), companyId); // 查看日期配置表中当天的记录

		if (dateSet != null) {
			TmBusinessParaPO para1 = getTmBusinessParaPO(Constant.GENEREAL_ORDER_BEFORE_WEEK_PARA, new Long(companyId));// 常规订单提报提前周度参数
			TmBusinessParaPO para2 = getTmBusinessParaPO(Constant.GENEREAL_ORDER_WEEK_PARA, new Long(companyId));// 常规订单提报周度参数

			String currentYear = dateSet.getSetYear() ; //获取系统日历表中对应年份
			String currentWeek = dateSet.getSetWeek() ; //获取系统日历表中对应月份

			String startYear = null ; //初始化开始年

			Map<String, String> map = new HashMap<String, String>() ;

			map.put("year", currentYear) ;

			String maxWeek = this.getMaxWeek(map).get("SET_WEEK").toString() ;

			String nextYear = null ;
			String nextWeek = null ;

			if(Integer.parseInt(maxWeek) < Integer.parseInt(currentWeek) + Integer.parseInt(para1.getParaValue())) {
				nextYear = (Integer.parseInt(currentYear) + 1) + "" ;
				nextWeek = (Integer.parseInt(currentWeek) + Integer.parseInt(para1.getParaValue()) - Integer.parseInt(maxWeek)) + "" ;
			} else if(Integer.parseInt(currentWeek) + Integer.parseInt(para1.getParaValue()) < 0) {
				nextYear = (Integer.parseInt(currentYear) - 1) + "" ;

				map.put("year", nextYear) ;

				String nextMaxWeek = this.getMaxWeek(map).get("SET_WEEK").toString() ;

				nextWeek = (Integer.parseInt(currentWeek) + Integer.parseInt(para1.getParaValue()) + nextMaxWeek) + "" ;
			} else {
				nextYear = currentYear ;
				nextWeek = (Integer.parseInt(currentWeek) + Integer.parseInt(para1.getParaValue())) + "" ;
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String date_start = null ;
			String date_end = null ;

			if (dateSet != null) {
				for (int i = 0; i < Integer.parseInt(para2.getParaValue()); i++) {
					maxWeek = this.getMaxWeek(map).get("SET_WEEK").toString() ;
					
					if(Integer.parseInt(maxWeek) < Integer.parseInt(nextWeek)) {
						nextYear = (Integer.parseInt(nextYear) + 1) + "" ;
						nextWeek = (Integer.parseInt(nextWeek) - Integer.parseInt(maxWeek)) + "" ;
					}
					map.put("year", nextYear) ;
					map.put("week", nextWeek) ;
					
					Map<String, Object> dateMap = this.getDayByWeek(map) ;

					if (dateMap != null) {
						date_start = dateMap.get("MINDATE").toString().substring(0, 4) + "/" + dateMap.get("MINDATE").toString().substring(4, 6) + "/" + dateMap.get("MINDATE").toString().substring(6, 8) ;
						date_end = dateMap.get("MAXDATE").toString().substring(0, 4) + "/" + dateMap.get("MAXDATE").toString().substring(4, 6) + "/" + dateMap.get("MAXDATE").toString().substring(6, 8) ;

						Map<String, String> newMap = new HashMap<String, String>();
						newMap.put("code", nextYear + "-" + nextWeek);
						newMap.put("name", nextYear + "年" + nextWeek + "周");
						newMap.put("date_start", date_start);
						newMap.put("date_end", date_end);
						list.add(newMap);
					}

					nextWeek = (Integer.parseInt(nextWeek) + 1) + ""  ;
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
	public List<Map<String, Object>> getGeneralReportDateList(Long companyId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = getTmDateSetPO(new Date(), companyId);
		if (dateSet != null) {
			TmBusinessParaPO para1 = getTmBusinessParaPO(Constant.GENEREAL_ORDER_BEFORE_WEEK_PARA,
					new Long(companyId));// 常规订单提报提前周度参数
			TmBusinessParaPO para2 = getTmBusinessParaPO(Constant.GENEREAL_ORDER_WEEK_PARA,
					new Long(companyId));// 常规订单提报周度参数

			// 获得提报起始周
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, 7 * Integer.parseInt(para1.getParaValue()));
			dateSet = getTmDateSetPO(c.getTime(), companyId);
			
			//计算周度的日期范围
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String year = dateSet.getSetYear();
			String week = dateSet.getSetWeek();
			Calendar calendar = Calendar.getInstance();
			calendar.clear(); 
			calendar.set(Calendar.YEAR, Integer.parseInt(year));
			calendar.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(week));
			calendar.setFirstDayOfWeek(Calendar.MONDAY); 
			String date_start = dateFormat.format(calendar.getTime());//起始日期
			calendar.add(Calendar.DATE, 6 );
			String date_end = dateFormat.format(calendar.getTime());//结束日期
			
			if (dateSet != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", dateSet.getSetYear() + "-" + dateSet.getSetWeek());
				map.put("name", dateSet.getSetYear() + "年" + dateSet.getSetWeek() + "周");
				map.put("date_start", date_start);
				map.put("date_end", date_end);
				list.add(map);

				for (int i = 1; i < Integer.parseInt(para2.getParaValue()); i++) {
					c.add(Calendar.DATE, 7);
					calendar.setFirstDayOfWeek(Calendar.MONDAY); 
					date_start = dateFormat.format(calendar.getTime());//起始日期
					calendar.add(Calendar.DATE, 6 );
					date_end = dateFormat.format(calendar.getTime());//结束日期
					dateSet = getTmDateSetPO(c.getTime(), companyId);
					if (dateSet != null) {
						map = new HashMap<String, Object>();
						map.put("code", dateSet.getSetYear() + "-" + dateSet.getSetWeek());
						map.put("name", dateSet.getSetYear() + "年" + dateSet.getSetWeek() + "周");
						map.put("date_start", date_start);
						map.put("date_end", date_end);
						list.add(map);
					}
				}
			}
		}

		return list;
		
		
	}
	
	public List<Map<String, Object>> getUrgentDateList(Long companyId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = getTmDateSetPO(new Date(), companyId);
		if (dateSet != null) {
			TmBusinessParaPO para1 = getTmBusinessParaPO(Constant.URGENT_ORDER_WEEK_BEFORE_PARA, new Long(companyId));// 补充订单提报提前周度参数
			TmBusinessParaPO para2 = getTmBusinessParaPO(Constant.URGENT_ORDER_WEEK_PARA, new Long(companyId));// 补充订单提报周度参数

			String currentYear = dateSet.getSetYear() ; //获取系统日历表中对应年份
			String currentWeek = dateSet.getSetWeek() ; //获取系统日历表中对应月份


			Map<String, String> map = new HashMap<String, String>() ;

			map.put("year", currentYear) ;

			String maxWeek = this.getMaxWeek(map).get("SET_WEEK").toString() ;

			String nextYear = null ;
			String nextWeek = null ;

			if(Integer.parseInt(maxWeek) < Integer.parseInt(currentWeek) + Integer.parseInt(para1.getParaValue())) {
				nextYear = (Integer.parseInt(currentYear) + 1) + "" ;
				nextWeek = (Integer.parseInt(currentWeek) + Integer.parseInt(para1.getParaValue()) - Integer.parseInt(maxWeek)) + "" ;
			} else if(Integer.parseInt(currentWeek) + Integer.parseInt(para1.getParaValue()) <= 0) {
				nextYear = (Integer.parseInt(currentYear) - 1) + "" ;

				map.put("year", nextYear) ;

				String nextMaxWeek = this.getMaxWeek(map).get("SET_WEEK").toString() ;

				nextWeek = (Integer.parseInt(currentWeek) + Integer.parseInt(para1.getParaValue()) + Integer.parseInt(nextMaxWeek)) + "" ;
			} else {
				nextYear = currentYear ;
				nextWeek = (Integer.parseInt(currentWeek) + Integer.parseInt(para1.getParaValue())) + "" ;
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String date_start = null ;
			String date_end = null ;

			if (dateSet != null) {
				for (int i = 0; i < Integer.parseInt(para2.getParaValue()); i++) {
					map.put("year", nextYear) ;
					maxWeek = this.getMaxWeek(map).get("SET_WEEK").toString() ;
					
					if(Integer.parseInt(maxWeek) < Integer.parseInt(nextWeek)) {
						nextYear = (Integer.parseInt(nextYear) + 1) + "" ;
						nextWeek = (Integer.parseInt(nextWeek) - Integer.parseInt(maxWeek)) + "" ;
					}

					map.put("week", nextWeek) ;
					
					Map<String, Object> dateMap = this.getDayByWeek(map) ;

					if (dateMap != null) {
						date_start = dateMap.get("MINDATE").toString().substring(0, 4) + "/" + dateMap.get("MINDATE").toString().substring(4, 6) + "/" + dateMap.get("MINDATE").toString().substring(6, 8) ;
						date_end = dateMap.get("MAXDATE").toString().substring(0, 4) + "/" + dateMap.get("MAXDATE").toString().substring(4, 6) + "/" + dateMap.get("MAXDATE").toString().substring(6, 8) ;

						Map<String, Object> newMap = new HashMap<String, Object>();
						newMap.put("code", nextYear + "-" + nextWeek);
						newMap.put("name", nextYear + "年" + nextWeek + "周");
						newMap.put("date_start", date_start);
						newMap.put("date_end", date_end);
						list.add(newMap);
					}

					nextWeek = (Integer.parseInt(nextWeek) + 1) + ""  ;
				}
				
				if(Integer.parseInt(para2.getParaValue()) == 0) {
					map.put("year", nextYear) ;
					map.put("week", nextWeek) ;
					
					Map<String, Object> dateMap = this.getDayByWeek(map) ;

					if (dateMap != null) {
						date_start = dateMap.get("MINDATE").toString().substring(0, 4) + "/" + dateMap.get("MINDATE").toString().substring(4, 6) + "/" + dateMap.get("MINDATE").toString().substring(6, 8) ;
						date_end = dateMap.get("MAXDATE").toString().substring(0, 4) + "/" + dateMap.get("MAXDATE").toString().substring(4, 6) + "/" + dateMap.get("MAXDATE").toString().substring(6, 8) ;

						Map<String, Object> newMap = new HashMap<String, Object>();
						newMap.put("code", nextYear + "-" + nextWeek);
						newMap.put("name", nextYear + "年" + nextWeek + "周");
						newMap.put("date_start", date_start);
						newMap.put("date_end", date_end);
						list.add(newMap);
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
	public List<Map<String, Object>> getUrgentReportDateList(Long companyId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = getTmDateSetPO(new Date(), companyId);
		if (dateSet != null) {
			TmBusinessParaPO para1 = getTmBusinessParaPO(Constant.URGENT_ORDER_WEEK_BEFORE_PARA,
					new Long(companyId));// 补充订单提报提前周度参数
			TmBusinessParaPO para2 = getTmBusinessParaPO(Constant.URGENT_ORDER_WEEK_PARA, new Long(
					companyId));// 补充订单提报周度参数

			// 获得提报起始周
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, 7 * Integer.parseInt(para1.getParaValue()));
			dateSet = getTmDateSetPO(c.getTime(), companyId);
			
			//计算周度的日期范围
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String year = dateSet.getSetYear();
			String week = dateSet.getSetWeek();
			Calendar calendar = Calendar.getInstance();
			calendar.clear(); 
			calendar.set(Calendar.YEAR, Integer.parseInt(year));
			calendar.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(week)+1);
			calendar.setFirstDayOfWeek(Calendar.MONDAY); 
			String date_start = dateFormat.format(calendar.getTime());//起始日期
			calendar.add(Calendar.DATE, 6 );
			String date_end = dateFormat.format(calendar.getTime());//结束日期
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", dateSet.getSetYear() + "-" + dateSet.getSetWeek());
			map.put("name", dateSet.getSetYear() + "年" + dateSet.getSetWeek() + "周");
			map.put("date_start", date_start);
			map.put("date_end", date_end);
			list.add(map);
			if (dateSet != null) {
				for (int i = 1; i < Integer.parseInt(para2.getParaValue()); i++) {
					calendar.clear();
					c.add(Calendar.DATE, 7);
					dateSet = getTmDateSetPO(c.getTime(), companyId);
					//计算周度的日期范围
					year = dateSet.getSetYear();//下一周的年份
					week = dateSet.getSetWeek();//下一周的周度
					calendar.set(Calendar.YEAR, Integer.parseInt(year));
					calendar.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(week)+1);
					calendar.setFirstDayOfWeek(Calendar.MONDAY); 
					date_start = dateFormat.format(calendar.getTime());//起始日期
					calendar.add(Calendar.DATE, 6 );
					date_end = dateFormat.format(calendar.getTime());//结束日期
					dateSet = getTmDateSetPO(c.getTime(), companyId);
					if (dateSet != null) {
						map = new HashMap<String, Object>();
						map.put("code", dateSet.getSetYear() + "-" + dateSet.getSetWeek());
						map.put("name", dateSet.getSetYear() + "年" + dateSet.getSetWeek() + "周");
						map.put("date_start", date_start);
						map.put("date_end", date_end);
						list.add(map);
					}
				}


				
			}
		}

		return list;
	}

	/**
	 * 常规订单列表查询
	 * 
	 * @param orderWeek
	 * @param areaId
	 * @param dealerId
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getGeneralOrderReportList(String orderYear,
			String orderWeek, String areaId, String areaIds, String dealerIds, String companyId,
			int curPage, int pageSize) {

		TmBusinessParaPO para = getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比
		TmBusinessParaPO maxPara = getTmBusinessParaPO(Constant.QUTOA_MAX_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT A.QUOTA_YEAR || '年' || A.QUOTA_WEEK || '周' QUOTA_DATE,\n");
		sql.append("       A.QUOTA_YEAR,\n");
		sql.append("       A.QUOTA_WEEK,\n");
		sql.append("       A.SERIES_CODE,\n");
		sql.append("       A.SERIES_NAME,\n");
		sql.append("       A.GROUP_ID,\n");
		sql.append("       A.GROUP_CODE,\n");
		sql.append("       A.GROUP_NAME,\n");
		sql.append("       A.QUOTA_AMT,\n");
		sql.append("       A.DEALER_ID,\n");
		sql.append("       A.AREA_ID,\n");
		// sql.append(" NVL(B.MIN_AMOUNT, 0) MIN_AMOUNT,\n");
		sql.append("       ROUND(A.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT,\n");
		sql.append("       ROUND(A.QUOTA_AMT*" + maxPara.getParaValue() + "/100, 0) MAX_AMOUNT,\n");
		sql.append("       NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		sql.append("       NVL(D.ORDER_AMOUNT, 0) WTB\n");
		sql.append("  FROM (SELECT TQ.QUOTA_YEAR,\n");
		sql.append("               TQ.QUOTA_WEEK,\n");
		sql.append("               TVMG3.GROUP_CODE SERIES_CODE,\n");
		sql.append("               TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("               TVMG1.GROUP_ID,\n");
		sql.append("               TVMG1.GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_NAME,\n");
		sql.append("               SUM(TQD.QUOTA_AMT) QUOTA_AMT,\n");
		sql.append("               TQ.DEALER_ID,\n");
		sql.append("               TQ.AREA_ID\n");
		sql.append("          FROM TT_VS_QUOTA            TQ,\n");
		sql.append("               TT_VS_QUOTA_DETAIL     TQD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append("         WHERE TQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TQ.QUOTA_ID=TQD.QUOTA_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TQ.DEALER_ID IN (" + dealerIds + ")\n");
		sql.append("           AND TQ.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("           AND TQ.AREA_ID = " + areaId + "\n");
		}
		sql.append("           AND TQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("           AND TQ.QUOTA_YEAR = " + orderYear + "\n");
		sql.append("           AND TQ.QUOTA_WEEK = " + orderWeek + "\n");
		sql.append("         GROUP BY TQ.QUOTA_YEAR,\n");
		sql.append("              TQ.QUOTA_WEEK,\n");
		sql.append("              TVMG3.GROUP_CODE,\n");
		sql.append("              TVMG3.GROUP_NAME,\n");
		sql.append("              TVMG1.GROUP_ID,\n");
		sql.append("              TVMG1.GROUP_CODE,\n");
		sql.append("              TVMG1.GROUP_NAME,\n");
		sql.append("              TQ.DEALER_ID,\n");
		sql.append("              TQ.AREA_ID) A,\n");

		sql.append("       (SELECT TQM.GROUP_ID, TQM.MIN_AMOUNT\n");
		sql.append("          FROM TT_VS_QUOTA_MIN TQM\n");
		sql.append("         WHERE TQM.DEALER_ID IN (" + dealerIds + ")\n");
		sql.append("           AND TQM.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("           AND TQM.AREA_ID = " + areaId + "\n");
		}
		sql.append("           AND TQM.QUOTA_YEAR = " + orderYear + "\n");
		sql.append("           AND TQM.QUOTA_WEEK = " + orderWeek + ") B,\n");
		sql.append("       (SELECT TVMG1.GROUP_ID, SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER              TSO,\n");
		sql.append("               TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG1\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("           AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TSO.ORDER_ORG_ID IN (" + dealerIds + ")\n");
		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		}
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_COM_STATUS_03 + ","
				+ Constant.ORDER_COM_STATUS_05 + ")\n");
		sql.append("           AND TSO.ORDER_YEAR = " + orderYear + "\n");
		sql.append("           AND TSO.ORDER_WEEK = " + orderWeek + "\n");
		sql.append("         GROUP BY TVMG1.GROUP_ID) C,\n");
		sql.append("       (SELECT TVMG1.GROUP_ID, SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER              TSO,\n");
		sql.append("               TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG1\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("           AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TSO.ORDER_ORG_ID IN (" + dealerIds + ")\n");
		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		if (!areaId.equals("")) {
			sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		}
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS = " + Constant.ORDER_COM_STATUS_01 + "\n");
		sql.append("           AND TSO.ORDER_YEAR = " + orderYear + "\n");
		sql.append("           AND TSO.ORDER_WEEK = " + orderWeek + "\n");
		sql.append("         GROUP BY TVMG1.GROUP_ID) D\n");
		sql.append(" WHERE A.GROUP_ID = B.GROUP_ID(+)\n");
		sql.append("   AND A.GROUP_ID = C.GROUP_ID(+)\n");
		sql.append("   AND A.GROUP_ID = D.GROUP_ID(+)\n");
		sql.append("   AND A.QUOTA_AMT <> 0\n");
		sql.append("  ORDER BY A.GROUP_CODE");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getGeneralOrderReportList", pageSize,
				curPage);
		return ps;
	}
	
	public Map<String, Object> getGeneralOrderReportDetail(String orderYear, String orderWeek,
			String areaId, String dealerId, String groupId, String companyId) {

		TmBusinessParaPO para = getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比
		TmBusinessParaPO maxPara = getTmBusinessParaPO(Constant.QUTOA_MAX_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比
		

		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT A.QUOTA_YEAR || '年' || A.QUOTA_WEEK || '周' QUOTA_DATE,\n");
		sql.append("       A.QUOTA_YEAR,\n");
		sql.append("       A.QUOTA_WEEK,\n");
		sql.append("       A.SERIES_CODE,\n");
		sql.append("       A.SERIES_NAME,\n");
		sql.append("       A.GROUP_ID,\n");
		sql.append("       A.GROUP_CODE,\n");
		sql.append("       A.GROUP_NAME,\n");
		sql.append("       A.QUOTA_AMT,\n");
		sql.append("       A.DEALER_ID,\n");
		sql.append("       A.AREA_ID,\n");
		// sql.append(" NVL(B.MIN_AMOUNT, 0) MIN_AMOUNT,\n");
		sql.append("       ROUND(A.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT,\n");
		sql.append("       ROUND(A.QUOTA_AMT*" + maxPara.getParaValue() + "/100, 0) MAX_AMOUNT,\n");
		sql.append("       NVL(C.ORDER_AMOUNT, 0) YTB,\n");
		sql.append("       NVL(D.ORDER_AMOUNT, 0) WTB\n");
		sql.append("  FROM (SELECT TQ.QUOTA_YEAR,\n");
		sql.append("               TQ.QUOTA_WEEK,\n");
		sql.append("               TVMG3.GROUP_CODE SERIES_CODE,\n");
		sql.append("               TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("               TVMG1.GROUP_ID,\n");
		sql.append("               TVMG1.GROUP_CODE,\n");
		sql.append("               TVMG1.GROUP_NAME,\n");
		sql.append("               SUM(NVL(TQD.QUOTA_AMT,0)) QUOTA_AMT,\n");
		sql.append("               TQ.DEALER_ID,\n");
		sql.append("               TQ.AREA_ID\n");
		sql.append("          FROM TT_VS_QUOTA            TQ,\n");
		sql.append("               TT_VS_QUOTA_DETAIL     TQD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append("         WHERE TQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TQ.QUOTA_ID = TQD.QUOTA_ID\n");
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("           AND TQ.DEALER_ID IN (" + dealerId + ")\n");
		sql.append("           AND TQ.AREA_ID = " + areaId + "\n");
		sql.append("           AND TQD.GROUP_ID = " + groupId + "\n");
		sql.append("           AND TQ.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("           AND TQ.QUOTA_YEAR = " + orderYear + "\n");
		sql.append("           AND TQ.QUOTA_WEEK = " + orderWeek + "\n");

		sql.append("         GROUP BY TQ.QUOTA_YEAR,\n");
		sql.append("              TQ.QUOTA_WEEK,\n");
		sql.append("              TVMG3.GROUP_CODE,\n");
		sql.append("              TVMG3.GROUP_NAME,\n");
		sql.append("              TVMG1.GROUP_ID,\n");
		sql.append("              TVMG1.GROUP_CODE,\n");
		sql.append("              TVMG1.GROUP_NAME,\n");
		sql.append("              TQ.DEALER_ID,\n");
		sql.append("              TQ.AREA_ID) A,\n");

		sql.append("       (SELECT TQM.GROUP_ID, TQM.MIN_AMOUNT\n");
		sql.append("          FROM TT_VS_QUOTA_MIN TQM\n");
		sql.append("         WHERE TQM.DEALER_ID IN (" + dealerId + ")\n");
		sql.append("           AND TQM.AREA_ID = " + areaId + "\n");
		sql.append("           AND TQM.GROUP_ID = " + groupId + "\n");
		sql.append("           AND TQM.QUOTA_YEAR = " + orderYear + "\n");
		sql.append("           AND TQM.QUOTA_WEEK = " + orderWeek + ") B,\n");
		sql.append("       (SELECT TVMG1.GROUP_ID, SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER              TSO,\n");
		sql.append("               TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG1\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("           AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TSO.ORDER_ORG_ID IN (" + dealerId + ")\n");
		sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		sql.append("           AND TVMG1.GROUP_ID = " + groupId + "\n");
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_COM_STATUS_03 + ","
				+ Constant.ORDER_COM_STATUS_05 + ")\n");
		sql.append("           AND TSO.ORDER_YEAR = " + orderYear + "\n");
		sql.append("           AND TSO.ORDER_WEEK = " + orderWeek + "\n");
		sql.append("         GROUP BY TVMG1.GROUP_ID) C,\n");
		sql.append("       (SELECT TVMG1.GROUP_ID, SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER              TSO,\n");
		sql.append("               TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG1\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("           AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("           AND TSO.ORDER_ORG_ID IN (" + dealerId + ")\n");
		sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		sql.append("           AND TVMG1.GROUP_ID = " + groupId + "\n");
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS = " + Constant.ORDER_COM_STATUS_01 + "\n");
		sql.append("           AND TSO.ORDER_YEAR = " + orderYear + "\n");
		sql.append("           AND TSO.ORDER_WEEK = " + orderWeek + "\n");
		sql.append("         GROUP BY TVMG1.GROUP_ID) D\n");
		sql.append(" WHERE A.GROUP_ID = B.GROUP_ID(+)\n");
		sql.append("   AND A.GROUP_ID = C.GROUP_ID(+)\n");
		sql.append("   AND A.GROUP_ID = D.GROUP_ID(+)");

		Map<String, Object> map = pageQueryMap(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getGeneralOrderReportDetail");
		return map;
	}

	/**
	 * 获得提报数量小于最小提保量列表
	 * 
	 * @param year
	 * @param week
	 * @param dealerId
	 * @return
	 */
	public List<Map<String, Object>> getGeneralReportErrorList(String year, String week,String areaId, String dealerId, String companyId) {
		TmBusinessParaPO para = getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT *\n");
		sql.append("  FROM (SELECT A.MIN_AMOUNT,\n");
		sql.append("               NVL(B.ORDER_AMOUNT, 0) YTB,\n");
		sql.append("               NVL(C.ORDER_AMOUNT, 0) WTB\n");
		sql.append("          FROM (SELECT TVMG1.GROUP_ID,\n");
		sql.append("                       TVMG1.GROUP_CODE,\n");
		sql.append("                       TVMG1.GROUP_NAME,\n");
		sql.append("       				   ROUND(TQD.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT\n");
		// sql.append(" TQM.MIN_AMOUNT\n");
		sql.append("                  FROM TT_VS_QUOTA TQM, TT_VS_QUOTA_DETAIL TQD, TM_VHCL_MATERIAL_GROUP TVMG1\n");
		sql.append("                 WHERE TQM.QUOTA_ID = TQD.QUOTA_ID\n");
		sql.append("                   AND TQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("                   AND TQM.AREA_ID = " + areaId + "\n");
		sql.append("                   AND TQM.DEALER_ID IN (" + dealerId + ")\n");
		sql.append("                   AND TQM.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("                   AND TVMG1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("                   AND TQM.QUOTA_YEAR = " + year + "\n");
		sql.append("                   AND TQM.QUOTA_WEEK = " + week + ") A,\n");
		sql.append("               (SELECT TVMG1.GROUP_ID,\n");
		sql.append("                       TVMG1.GROUP_CODE,\n");
		sql.append("                       TVMG1.GROUP_NAME,\n");
		sql.append("                       SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("                  FROM TT_VS_ORDER              TSO,\n");
		sql.append("                       TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP   TVMG1\n");
		sql.append("                 WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("                   AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("                   AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("                   AND TSO.AREA_ID = " + areaId + "\n");
		sql.append("                   AND TSO.ORDER_ORG_ID IN (" + dealerId + ")\n");
		sql.append("                   AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("                   AND TSO.ORDER_STATUS IN (" + Constant.ORDER_COM_STATUS_03 + "," + Constant.ORDER_COM_STATUS_05 + ")\n");
		sql.append("                   AND TVMG1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("                   AND TSO.ORDER_YEAR = " + year + "\n");
		sql.append("                   AND TSO.ORDER_WEEK = " + week + "\n");
		sql.append("                 GROUP BY TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME) B,\n");
		sql.append("               (SELECT TVMG1.GROUP_ID,\n");
		sql.append("                       TVMG1.GROUP_CODE,\n");
		sql.append("                       TVMG1.GROUP_NAME,\n");
		sql.append("                       SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("                  FROM TT_VS_ORDER              TSO,\n");
		sql.append("                       TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP   TVMG1\n");
		sql.append("                 WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("                   AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("                   AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("                   AND TSO.AREA_ID = " + areaId + "\n");
		sql.append("                   AND TSO.ORDER_ORG_ID IN (" + dealerId + ")\n");
		sql.append("                   AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("                   AND TSO.ORDER_STATUS = " + Constant.ORDER_COM_STATUS_01 + "\n");
		sql.append("                   AND TVMG1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("                   AND TSO.ORDER_YEAR = " + year + "\n");
		sql.append("                   AND TSO.ORDER_WEEK = " + week + "\n");
		sql.append("                 GROUP BY TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME) C\n");
		sql.append("         WHERE A.GROUP_ID = B.GROUP_ID(+)\n");
		sql.append("           AND A.GROUP_ID = C.GROUP_ID(+)) C\n");
		sql.append(" WHERE C.MIN_AMOUNT > (C.YTB + C.WTB)");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getGeneralReportErrorList");
		return list;
	}
	
	public List<Map<String, Object>> getGeneralReportErrorListCVS(String year, String week,String areaId, String dealerId, String companyId) {
		TmBusinessParaPO para = getTmBusinessParaPO(Constant.QUTOA_MIN_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT *\n");
		sql.append("  FROM (SELECT A.group_code, A.MIN_AMOUNT,\n");
		sql.append("               NVL(B.ORDER_AMOUNT, 0) YTB,\n");
		sql.append("               NVL(C.ORDER_AMOUNT, 0) WTB\n");
		sql.append("          FROM (SELECT TVMG1.GROUP_ID,\n");
		sql.append("                       TVMG1.GROUP_CODE,\n");
		sql.append("                       TVMG1.GROUP_NAME,\n");
		sql.append("       				   ROUND(TQD.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MIN_AMOUNT\n");
		// sql.append(" TQM.MIN_AMOUNT\n");
		sql.append("                  FROM TT_VS_QUOTA TQM, TT_VS_QUOTA_DETAIL TQD, TM_VHCL_MATERIAL_GROUP TVMG1\n");
		sql.append("                 WHERE TQM.QUOTA_ID = TQD.QUOTA_ID\n");
		sql.append("                   AND TQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("                   AND TQM.AREA_ID = " + areaId + "\n");
		sql.append("                   AND TQM.DEALER_ID IN (" + dealerId + ")\n");
		sql.append("                   AND TQM.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("                   AND TVMG1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("                   AND TQM.QUOTA_YEAR = " + year + "\n");
		sql.append("                   AND TQM.QUOTA_WEEK = " + week + ") A,\n");
		sql.append("               (SELECT TVMG1.GROUP_ID,\n");
		sql.append("                       TVMG1.GROUP_CODE,\n");
		sql.append("                       TVMG1.GROUP_NAME,\n");
		sql.append("                       SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("                  FROM TT_VS_ORDER              TSO,\n");
		sql.append("                       TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP   TVMG1\n");
		sql.append("                 WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("                   AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("                   AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("                   AND TSO.AREA_ID = " + areaId + "\n");
		sql.append("                   AND TSO.ORDER_ORG_ID IN (" + dealerId + ")\n");
		sql.append("                   AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("                   AND TSO.ORDER_STATUS IN (" + Constant.ORDER_COM_STATUS_03 + "," + Constant.ORDER_COM_STATUS_05 + ")\n");
		sql.append("                   AND TVMG1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("                   AND TSO.ORDER_YEAR = " + year + "\n");
		sql.append("                   AND TSO.ORDER_WEEK = " + week + "\n");
		sql.append("                 GROUP BY TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME) B,\n");
		sql.append("               (SELECT TVMG1.GROUP_ID,\n");
		sql.append("                       TVMG1.GROUP_CODE,\n");
		sql.append("                       TVMG1.GROUP_NAME,\n");
		sql.append("                       SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("                  FROM TT_VS_ORDER              TSO,\n");
		sql.append("                       TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP   TVMG1\n");
		sql.append("                 WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("                   AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("                   AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("                   AND TSO.AREA_ID = " + areaId + "\n");
		sql.append("                   AND TSO.ORDER_ORG_ID IN (" + dealerId + ")\n");
		sql.append("                   AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("                   AND TSO.ORDER_STATUS = " + Constant.ORDER_COM_STATUS_01 + "\n");
		sql.append("                   AND TVMG1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("                   AND TSO.ORDER_YEAR = " + year + "\n");
		sql.append("                   AND TSO.ORDER_WEEK = " + week + "\n");
		sql.append("                 GROUP BY TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME) C\n");
		sql.append("         WHERE A.GROUP_ID = B.GROUP_ID(+)\n");
		sql.append("           AND A.GROUP_ID = C.GROUP_ID) C\n");
		sql.append(" WHERE C.MIN_AMOUNT > (C.YTB + C.WTB)");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getGeneralReportErrorList");
		return list;
	}
	
	public List<Map<String, Object>> getGeneralReportMaxErrorList(String year, String week,String areaId, String dealerId, String companyId) {
		TmBusinessParaPO para = getTmBusinessParaPO(Constant.QUTOA_MAX_WEIGHT_PARA, new Long(companyId));// 配额最小提报量百分比

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT *\n");
		sql.append("  FROM (SELECT A.MAX_AMOUNT,\n");
		sql.append("               NVL(B.ORDER_AMOUNT, 0) YTB,\n");
		sql.append("               NVL(C.ORDER_AMOUNT, 0) WTB\n");
		sql.append("          FROM (SELECT TVMG1.GROUP_ID,\n");
		sql.append("                       TVMG1.GROUP_CODE,\n");
		sql.append("                       TVMG1.GROUP_NAME,\n");
		sql.append("       				   ROUND(TQD.QUOTA_AMT*" + para.getParaValue() + "/100, 0) MAX_AMOUNT\n");
		// sql.append(" TQM.MIN_AMOUNT\n");
		sql.append("                  FROM TT_VS_QUOTA TQM, TT_VS_QUOTA_DETAIL TQD, TM_VHCL_MATERIAL_GROUP TVMG1\n");
		sql.append("                 WHERE TQM.QUOTA_ID = TQD.QUOTA_ID\n");
		sql.append("                   AND TQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("                   AND TQM.AREA_ID = " + areaId + "\n");
		sql.append("                   AND TQM.DEALER_ID IN (" + dealerId + ")\n");
		sql.append("                   AND TQM.STATUS = " + Constant.QUOTA_STATUS_02 + "\n");
		sql.append("                   AND TVMG1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("                   AND TQM.QUOTA_YEAR = " + year + "\n");
		sql.append("                   AND TQM.QUOTA_WEEK = " + week + ") A,\n");
		sql.append("               (SELECT TVMG1.GROUP_ID,\n");
		sql.append("                       TVMG1.GROUP_CODE,\n");
		sql.append("                       TVMG1.GROUP_NAME,\n");
		sql.append("                       SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("                  FROM TT_VS_ORDER              TSO,\n");
		sql.append("                       TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP   TVMG1\n");
		sql.append("                 WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("                   AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("                   AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("                   AND TSO.AREA_ID = " + areaId + "\n");
		sql.append("                   AND TSO.ORDER_ORG_ID IN (" + dealerId + ")\n");
		sql.append("                   AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("                   AND TSO.ORDER_STATUS IN (" + Constant.ORDER_COM_STATUS_03 + "," + Constant.ORDER_COM_STATUS_05 + ")\n");
		sql.append("                   AND TVMG1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("                   AND TSO.ORDER_YEAR = " + year + "\n");
		sql.append("                   AND TSO.ORDER_WEEK = " + week + "\n");
		sql.append("                 GROUP BY TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME) B,\n");
		sql.append("               (SELECT TVMG1.GROUP_ID,\n");
		sql.append("                       TVMG1.GROUP_CODE,\n");
		sql.append("                       TVMG1.GROUP_NAME,\n");
		sql.append("                       SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("                  FROM TT_VS_ORDER              TSO,\n");
		sql.append("                       TT_VS_ORDER_DETAIL       TSOD,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("                       TM_VHCL_MATERIAL_GROUP   TVMG1\n");
		sql.append("                 WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("                   AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("                   AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("                   AND TSO.AREA_ID = " + areaId + "\n");
		sql.append("                   AND TSO.ORDER_ORG_ID IN (" + dealerId + ")\n");
		sql.append("                   AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("                   AND TSO.ORDER_STATUS = " + Constant.ORDER_COM_STATUS_01 + "\n");
		sql.append("                   AND TVMG1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("                   AND TSO.ORDER_YEAR = " + year + "\n");
		sql.append("                   AND TSO.ORDER_WEEK = " + week + "\n");
		sql.append("                 GROUP BY TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME) C\n");
		sql.append("         WHERE A.GROUP_ID = B.GROUP_ID(+)\n");
		sql.append("           AND A.GROUP_ID = C.GROUP_ID(+)) C\n");
		sql.append(" WHERE C.MAX_AMOUNT < (C.YTB + C.WTB)");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getGeneralReportErrorList");
		return list;
	}

	/**
	 * 根据配置id获得未保存过的物料列表
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Map<String, Object>> getUnsaveMaterialList(String dealerId, String groupId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVM.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TVM.COLOR_NAME,\n");
		sql.append("       F_GET_PRICE(" + dealerId + ", TVM.MATERIAL_ID) PRICE,\n");
		sql.append("       0 ORDER_AMOUNT\n");
		sql.append("  FROM TM_VHCL_MATERIAL TVM, TM_VHCL_MATERIAL_GROUP_R TVMGR\n");
		sql.append(" WHERE TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVM.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TVMGR.GROUP_ID = " + groupId + "");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getUnsaveMaterialList");
		return list;
	}

	/**
	 * 根据配置id获得保存过的物料列表
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Map<String, Object>> getSaveMaterialList(String orderYear, String orderWeek,
			String dealerId, String groupId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVM.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TVM.COLOR_NAME,\n");
		sql.append("       TSOD.SINGLE_PRICE PRICE,\n");
		sql.append("       TSOD.ORDER_AMOUNT\n");
		sql.append("  FROM TM_VHCL_MATERIAL         TVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TT_VS_ORDER              TSO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TSOD\n");
		sql.append(" WHERE TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TSOD.MATERIAL_ID\n");
		sql.append("   AND TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("   AND TVM.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("   AND TSO.ORDER_STATUS = " + Constant.ORDER_COM_STATUS_01 + "\n");
		sql.append("   AND TSO.ORDER_YEAR = " + orderYear + "\n");
		sql.append("   AND TSO.ORDER_WEEK = " + orderWeek + "\n");
		sql.append("   AND TSO.ORDER_ORG_ID = " + dealerId + "\n");
		sql.append("   AND TVMGR.GROUP_ID = " + groupId);

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getSaveMaterialList");
		return list;
	}

	/**
	 * 获得常规订单提报物料列表
	 * 
	 * @param orderYear
	 * @param orderWeek
	 * @param dealerId
	 * @param groupId
	 * @return
	 */
	public List<Map<String, Object>> getGeneralReportMaterialList(String orderYear,
			String orderWeek, String dealerId, String groupId,String orderType) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVM.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TVM.COLOR_NAME,\n");
		sql
				.append("       NVL(TMP.PRICE, F_GET_PRICE(" + dealerId
						+ ", TVM.MATERIAL_ID, " + orderType + ")) PRICE,\n");
		sql.append("       NVL(TMP.ORDER_AMOUNT, 0) ORDER_AMOUNT\n");
		sql.append("  FROM TM_VHCL_MATERIAL TVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       (SELECT TVM.MATERIAL_ID, TSOD.SINGLE_PRICE PRICE, TSOD.ORDER_AMOUNT\n");
		sql.append("          FROM TM_VHCL_MATERIAL         TVM,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("               TT_VS_ORDER              TSO,\n");
		sql.append("               TT_VS_ORDER_DETAIL       TSOD\n");
		sql.append("         WHERE TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("           AND TVM.MATERIAL_ID = TSOD.MATERIAL_ID\n");
		sql.append("           AND TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TVM.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS = " + Constant.ORDER_COM_STATUS_01 + "\n");
		sql.append("           AND TSO.ORDER_YEAR = " + orderYear + "\n");
		sql.append("           AND TSO.ORDER_WEEK = " + orderWeek + "\n");
		sql.append("           AND TSO.ORDER_ORG_ID = " + dealerId + "\n");
		sql.append("           AND TVMGR.GROUP_ID = " + groupId + ") TMP\n");
		sql.append(" WHERE TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TMP.MATERIAL_ID(+)\n");
		sql.append("   AND TVM.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TVM.ORDER_FLAG = " + Constant.NASTY_ORDER_REPORT_TYPE_01 + "\n");
		sql.append("   AND TVMGR.GROUP_ID = " + groupId + "\n");
		sql.append("  ORDER BY TVM.MATERIAL_ID ASC");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getGeneralReportMaterialList");
		return list;
	}

	public TtVsOrderPO geTtVsOrderPO(TtVsOrderPO po) {
		List<PO> list = select(po);
		return list.size() != 0 ? (TtVsOrderPO) list.get(0) : null;
	}

	/**
	 * 订单保存
	 * 
	 * @param po
	 */
	public void insertSalesOrder(TtVsOrderPO po) {
		dao.insert(po);
	}

	public TtVsOrderDetailPO geTtVsOrderDetailPO(TtVsOrderDetailPO po) {
		List<PO> list = select(po);
		return list.size() != 0 ? (TtVsOrderDetailPO) list.get(0) : null;
	}

	public void deleteSalesOrderDetail(TtVsOrderDetailPO po) {
		dao.delete(po);
	}

	public void deleteSalesOrder(TtVsOrderPO po) {
		dao.delete(po);
	}

	public void insertSalesOrderDetail(TtVsOrderDetailPO po) {
		dao.insert(po);
	}

	public void updateSalesOrder(TtVsOrderPO condition, TtVsOrderPO value) {
		dao.update(condition, value);
	}

	public PageResult<Map<String, Object>> getUrgentOrderResList(String groupCode,
			String materialCode, String areaId, String areaIds, String companyId, String year,
			String week, int curPage, int pageSize) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT A. MATERIAL_ID,\n");
		sql.append("       A.MATERIAL_CODE,\n");
		sql.append("       A.MATERIAL_NAME,\n");
		sql.append("       A.COLOR_NAME,\n");
		sql.append("       A.RESOURCE_AMOUNT - NVL(B.ORDER_AMOUNT, 0) RESOURCE_AMOUNT\n");
		sql.append("  FROM (SELECT TVM.MATERIAL_ID,\n");
		sql.append("               TVM.MATERIAL_CODE,\n");
		sql.append("               TVM.MATERIAL_NAME,\n");
		sql.append("               TVM.COLOR_NAME,\n");
		sql.append("               NVL(TR.RESOURCE_AMOUNT, 0) RESOURCE_AMOUNT\n");
		sql.append("          FROM TM_VHCL_MATERIAL         TVM,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("               TT_VS_RESOURCE           TR\n");
		sql.append("         WHERE TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("           AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("           AND TVM.MATERIAL_ID = TR.MATERIAL_ID(+)\n");
		sql.append("           AND TR.SPECIAL_BATCH_NO IS NULL\n");
		if (!groupCode.equals("")) {
			sql.append("           AND TVMG.GROUP_CODE = '" + groupCode + "'\n");
		}
		if (!materialCode.equals("")) {
			sql.append("           AND TVM.MATERIAL_CODE = '" + materialCode + "'\n");
		}
		if (!companyId.equals("")) {
			sql.append("           AND TR.COMPANY_ID = " + companyId + "\n");
		}
		if (!year.equals("")) {
			sql.append("           AND TR.RESOURCE_YEAR = " + year + "\n");
		}
		if (!week.equals("")) {
			sql.append("           AND TR.RESOURCE_WEEK = " + week + "\n");
		}
		sql.append("           AND TVMGR.GROUP_ID IN\n");
		sql.append("               (SELECT T1.GROUP_ID\n");
		sql.append("                  FROM TM_VHCL_MATERIAL_GROUP T1\n");
		sql.append("                 WHERE T1.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("                 START WITH T1.GROUP_ID IN\n");
		sql.append("                            (SELECT TAP.MATERIAL_GROUP_ID\n");
		sql.append("                               FROM TM_AREA_GROUP TAP\n");
		sql.append("                              WHERE TAP.AREA_ID IN\n");
		sql.append("                                    (" + areaIds + "))\n");
		sql.append("                CONNECT BY PRIOR T1.GROUP_ID = T1.PARENT_GROUP_ID)) A,\n");
		sql.append("       (SELECT TVOD.MATERIAL_ID, SUM(TVOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER TVO, TT_VS_ORDER_DETAIL TVOD\n");
		sql.append("         WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("           AND TVO.ORDER_STATUS = " + Constant.ORDER_COM_STATUS_03 + "\n");
		sql.append("           AND TVO.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TVOD.SPECIAL_BATCH_NO IS NULL\n");
		sql.append("         GROUP BY MATERIAL_ID) B\n");
		sql.append(" WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getUrgentOrderResList", pageSize, curPage);
		return ps;
	}

	/**
	 * 补充订单列表
	 * 
	 * @param orderYear
	 * @param orderWeek
	 * @param areaId
	 * @param dealerId
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getUrgentOrderReportList(String orderYear,
			String orderWeek, String areaId, String areaIds, String dealerIds, String companyId,
			int curPage, int pageSize) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT A.ORDER_ORG_NAME,\n");
		sql.append("       A.ORDER_ORG_ID,\n");
		sql.append("       A.BILLING_ORG_NAME,\n");
		sql.append("       A.ORDER_ID,\n");
		sql.append("       A.ORDER_NO,\n");
		sql.append("       A.QUOTA_DATE,\n");
		sql.append("       A.ORDER_TYPE,\n");
		sql.append("       A.ORDER_STATUS,\n");
		sql.append("       B.ORDER_AMOUNT\n");
		sql.append("  FROM (SELECT TD1.DEALER_SHORTNAME ORDER_ORG_NAME,\n");
		sql.append("               TD1.DEALER_ID ORDER_ORG_ID,\n");
		sql.append("               TD2.DEALER_SHORTNAME BILLING_ORG_NAME,\n");
		sql.append("               TSO.ORDER_ID,\n");
		sql.append("               TSO.ORDER_NO,\n");
		//sql.append("               TSO.ORDER_YEAR || '年' || TSO.ORDER_WEEK || '周' QUOTA_DATE,\n");
		sql.append("               TO_CHAR(TSO.CREATE_DATE,'YYYY-MM-DD') QUOTA_DATE,\n");
		sql.append("               TSO.ORDER_TYPE,\n");
		sql.append("               TSO.ORDER_STATUS\n");
		sql.append("          FROM TT_VS_ORDER        TSO,\n");
		sql.append("               TM_DEALER             TD1,\n");
		sql.append("               TM_DEALER             TD2\n");
		sql.append("         WHERE TSO.ORDER_ORG_ID = TD1.DEALER_ID(+)\n");
		sql.append("           AND TSO.BILLING_ORG_ID = TD2.DEALER_ID(+)\n");
		if(!"".equals(orderYear)&&orderYear!=null){
		sql.append("           AND TSO.ORDER_YEAR = " + orderYear + "\n");
		}
		if(!"".equals(orderWeek)&&orderWeek!=null){
		sql.append("           AND TSO.ORDER_WEEK = " + orderWeek + "\n");
		}
		sql.append("           AND TSO.COMPANY_ID = " + companyId + "\n");
		if (!areaId.equals("")) {
			sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		}
		//sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		sql.append("           AND TSO.ORDER_ORG_ID IN (" + dealerIds + ")\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_COM_STATUS_01 + ", "
				+ Constant.ORDER_COM_STATUS_04 + ")\n");
		//sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_02 + ") A,\n");
		sql.append(" ) A,\n");
		sql.append("       (SELECT TSO.ORDER_ID, SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER TSO, TT_VS_ORDER_DETAIL TSOD\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		if(!"".equals(orderYear)&&orderYear!=null){
		sql.append("           AND TSO.ORDER_YEAR = " + orderYear + "\n");
		}
		if(!"".equals(orderWeek)&&orderWeek!=null){
		sql.append("           AND TSO.ORDER_WEEK = " + orderWeek + "\n");
		}
		sql.append("           AND TSO.COMPANY_ID = " + companyId + "\n");
		if (!areaId.equals("")) {
			sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		}
		//sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		sql.append("           AND TSO.ORDER_ORG_ID IN (" + dealerIds + ")\n");
		sql.append("           AND TSO.ORDER_STATUS IN (" + Constant.ORDER_COM_STATUS_01 + ", "
				+ Constant.ORDER_COM_STATUS_04 + ")\n");
		//sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_02 + "\n");
		sql.append("         GROUP BY TSO.ORDER_ID) B\n");
		sql.append(" WHERE A.ORDER_ID = B.ORDER_ID ORDER BY A.QUOTA_DATE DESC");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getUrgentOrderReportList", pageSize,
				curPage);
		return ps;
	}

	public List<Map<String, Object>> getFundTypeList() {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TYPE_ID, TYPE_NAME\n");
		sql.append("          FROM TT_VS_ACCOUNT_TYPE\n");
		sql.append("         WHERE STATUS = " + Constant.STATUS_ENABLE);

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getFundTypeList");
		return list;
	}

	public Map<String, Object> getAvailableAmount(String fundTypeId, String dealerId) {
		Map<String, Object> map = null ;
		
		if(UrgentOrderReport.chkBinCai(fundTypeId, null)) {
			map = getBinCaiAvailable(dealerId, Long.parseLong(fundTypeId)) ;
		} else {
			StringBuffer sql = new StringBuffer();
	
			sql.append("SELECT TSA.ACCOUNT_ID, TSA.AVAILABLE_AMOUNT\n");
			sql.append("  FROM TT_VS_ACCOUNT TSA\n");
			sql.append(" WHERE TSA.DEALER_ID = " + dealerId + "\n");
			sql.append("   AND TSA.ACCOUNT_TYPE_ID = " + fundTypeId);
	
			map = pageQueryMap(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getAvailableAmount");
		}
		
		return map;
	}

	public Map<String, Object> getAvailableAmount(String fundTypeId, String dealerId, String reqId) {
		Map<String, Object> map = null ;
		
		if(UrgentOrderReport.chkBinCai(fundTypeId, null)) {
			map = getBinCaiAvailable(dealerId, fundTypeId, reqId) ;
		} else {
			StringBuffer sql = new StringBuffer();
	
			sql.append("SELECT A.ACCOUNT_ID, (A.AVAILABLE_AMOUNT + NVL(B.FREEZE_AMOUNT, 0)) AVAILABLE_AMOUNT, AVAILABLE_AMOUNT AVAILABLE_AMOUNT_1\n");
			sql.append("  FROM (SELECT TSA.ACCOUNT_ID, TSA.ACCOUNT_TYPE_ID, TSA.AVAILABLE_AMOUNT\n");
			sql.append("          FROM TT_VS_ACCOUNT TSA\n");
			sql.append("         WHERE TSA.DEALER_ID = " + dealerId + "\n");
			sql.append("           AND TSA.ACCOUNT_TYPE_ID = " + fundTypeId + ") A,\n");
			sql.append("       (SELECT TVAF.ACCOUNT_ID, TVAF.FREEZE_AMOUNT\n");
			sql.append("          FROM TT_VS_ACCOUNT_FREEZE TVAF\n");
			sql.append("         WHERE TVAF.REQ_ID = " + reqId + "\n");
			sql.append("           AND TVAF.DEALER_ID = " + dealerId + " AND TVAF.STATUS = ").append(Constant.ACCOUNT_FREEZE_STATUS_01).append(") B\n");
			sql.append(" WHERE A.ACCOUNT_ID = B.ACCOUNT_ID(+)");
			
			map = pageQueryMap(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getAvailableAmount2");
		}
		
		return map;
	}
	
	public Map<String, Object> getAvailableAmount2(String accountId, String reqId) {
		Map<String, Object> map = null ;
		
		if(UrgentOrderReport.chkBinCai(null, accountId)) {
			map = getBinCaiAvailable2(accountId, reqId) ;
		} else {
			StringBuffer sql = new StringBuffer();
	
			sql.append("SELECT A.ACCOUNT_ID, (A.AVAILABLE_AMOUNT + NVL(B.FREEZE_AMOUNT, 0)) AVAILABLE_AMOUNT\n");
			sql.append("  FROM (SELECT TSA.ACCOUNT_ID, TSA.ACCOUNT_TYPE_ID, TSA.AVAILABLE_AMOUNT\n");
			sql.append("          FROM TT_VS_ACCOUNT TSA\n");
			sql.append("         WHERE TSA.ACCOUNT_ID = " + accountId + ") A,\n");
			sql.append("       (SELECT TVAF.ACCOUNT_ID, TVAF.FREEZE_AMOUNT\n");
			sql.append("          FROM TT_VS_ACCOUNT_FREEZE TVAF\n");
			sql.append("         WHERE TVAF.REQ_ID = " + reqId + "\n");
			sql.append("           AND TVAF.ACCOUNT_ID = '" + accountId + "' AND TVAF.STATUS = ").append(Constant.ACCOUNT_FREEZE_STATUS_01).append(") B\n");
			sql.append(" WHERE A.ACCOUNT_ID = B.ACCOUNT_ID(+)");

			map = pageQueryMap(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getAvailableAmount2");
		}
		
		return map;
	}

	public List<Map<String, Object>> getAddressList(String dealerId, String areaId) {

		StringBuffer sql = new StringBuffer("\n");
		
		/*sql.append("SELECT TA.ID, TA.ADDRESS FROM TM_VS_ADDRESS TA WHERE TA.DEALER_ID = " + dealerId + "");
		sql.append("AND TA.STATUS='"+Constant.STATUS_ENABLE+"'");*/
		
		sql.append("select distinct tva.id, tva.address\n");
		sql.append("  from tm_vs_address tva\n");  
		sql.append(" where 1 = 1\n");  
		sql.append("   and tva.dealer_id =" + dealerId + "\n");  
		sql.append("   and tva.status =" + Constant.STATUS_ENABLE + "\n");  
		if(!"".equals(areaId)&&areaId!=null){
		sql.append("   and tva.b_area_id =" + areaId + "\n");
		}
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getAddressList");
		return list;
	}

	public Map<String, Object> getAddressInfo_(String addressId,String orderId) {

		StringBuffer sql = new StringBuffer();
		/*sql.append("SELECT TA.ADDRESS, TA.LINK_MAN, TA.TEL, TA.RECEIVE_ORG FROM TM_VS_ADDRESS TA WHERE 1=1 ");
		if (!addressId.equals("")) {
			sql.append(" AND TA.ID = " + addressId);
		} else {
			sql.append(" AND TA.ID IS NULL");
		}*/
		
		sql.append("SELECT TA.ADDRESS, TTO.LINK_MAN, TTO.TEL, TA.RECEIVE_ORG\n");
		sql.append("  FROM TT_VS_ORDER TTO, TM_VS_ADDRESS TA\n");  
		sql.append(" WHERE TTO.DELIVERY_ADDRESS = TA.ID\n");  
		sql.append("   AND TTO.ORDER_ID = "+orderId+"\n");

		Map<String, Object> map = pageQueryMap(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getAddressInfo");
		if (map == null) {
			map = new HashMap<String, Object>();
			map.put("ADDRESS", "");
			map.put("LINK_MAN", "");
			map.put("TEL", "");
			map.put("RECEIVE_ORG", "");
		}
		return map;
	}
	
	public Map<String, Object> getAddressInfo(String addressId) {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TA.ADDRESS, TA.LINK_MAN, TA.TEL, TA.RECEIVE_ORG FROM TM_VS_ADDRESS TA WHERE 1=1 ");
		sql.append("AND TA.STATUS='"+Constant.STATUS_ENABLE+"'");
		if (!addressId.equals("")) {
			sql.append(" AND TA.ID = " + addressId);
		} else {
			sql.append(" AND TA.ID IS NULL");
		}
		

		Map<String, Object> map = pageQueryMap(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getAddressInfo");
		if (map == null) {
			map = new HashMap<String, Object>();
			map.put("ADDRESS", "");
			map.put("LINK_MAN", "");
			map.put("TEL", "");
			map.put("RECEIVE_ORG", "");
		}
		return map;
	}
	public List<Map<String, Object>> getTmFaDealer(String dealerId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select vod.root_dealer_id ROOT_DEALER_ID from vw_org_dealer vod where vod.dealer_id="+dealerId);
		List<Map<String, Object>> list=pageQuery(sql.toString(), null,"com.infodms.dms.dao.sales.OrderReportDao.getTmFaDealer");
		return list;
	}

	public TmDealerPO getTmDealer(String dealerId) {
		TmDealerPO po = new TmDealerPO();
		po.setDealerId(new Long(dealerId));
		List<PO> list = select(po);
		return list.size() != 0 ? (TmDealerPO) list.get(0) : null;
	}

	public Map<String, Object> getMaterialInfo(String materialCode, String warehouseId, String priceId, String companyId, String entityCode,String dealerCode) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVMG2.GROUP_CODE SERIES_CODE,\n");
		sql.append("       TVMG2.GROUP_NAME SERIES_NAME,\n");
		sql.append("       TVM.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       CASE\n");
		sql.append("        WHEN NVL(VR.STOCK_AMOUNT, 0)-NVL(VR.LOCK_AMOUT,0) <= 0 THEN\n");
		sql.append("          '无'\n");
		sql.append("         ELSE\n");
		sql.append("          '有'\n");
		sql.append("       end RESOURCE_AMOUNT,\n");
//		sql.append("       F_GET_PRICE(" + priceId + ", TVM.MATERIAL_ID) PRICE,\n");
//		sql.append("       F_GET_MATPRICE('" + dealerCode + "', TVMG1.GROUP_CODE) PRICE,\n");
		sql.append("       NVL(TVPD.SALES_PRICE,0) SALES_PRICE,\n");
		sql.append("      NVL(VR.STOCK_AMOUNT, 0)-NVL(VR.LOCK_AMOUT,0) AVA_STOCK,\n");
		//sql.append("       NVL(TORDER.N_UN_REMAIN_AMOUNT, 0) GENERAL_AMOUNT,\n");
		sql.append("       TVPD.DETAIL_ID\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("       TM_VHCL_MATERIAL TVM,\n");
		sql.append("   (SELECT * FROM  TT_VS_PRICE_DTL where PRICE_ID="+priceId+") TVPD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		/*sql.append("       (SELECT VVOR.MATERIAL_ID, VVOR.ENABLE_VEHICLE\n");
		sql.append("          FROM VW_VS_ORDER_RESOURCE VVOR\n");
		sql.append("         ) VOR,\n");
		sql.append("       (SELECT VVR.MATERIAL_ID, SUM(VVR.AVA_STOCK) AVA_STOCK\n");
		sql.append("          FROM VW_VS_RESOURCE_ENTITY VVR\n");
		sql.append("         WHERE 1=1 \n");
		if (!entityCode.equals("")) {
			sql.append("           AND VVR.ENTITY_CODE = " + entityCode + "\n");
		}
		// sql.append(" AND VVR.SPECIAL_BATCH_NO IS NULL\n");
		sql.append("         GROUP BY VVR.MATERIAL_ID) VR,\n");
		sql.append("       (SELECT VVO.MATERIAL_ID, VVO.N_UN_REMAIN_AMOUNT\n");
		sql.append("          FROM VW_VS_ORDER_ENTITY VVO\n");
		sql.append("         WHERE 1=1\n");
		if (!entityCode.equals("")) {
			sql.append("           AND VVO.ENTITY_CODE = " + entityCode);
		}
		sql.append(") TORDER\n");*/
		sql.append("(SELECT * FROM VW_VS_RESOURCE_ENTITY_WEEK_NEW WHERE WAREHOUSE_ID="+warehouseId+") VR \n ");
		sql.append(" WHERE TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TVMG1.GROUP_ID = TVMGR.GROUP_ID\n");
		sql.append("   AND TVMG1.GROUP_ID = TVPD.GROUP_ID(+)\n");
		sql.append("   AND TVMGR.MATERIAL_ID = TVM.MATERIAL_ID\n");
		/*sql.append("   AND TVM.MATERIAL_ID = VOR.MATERIAL_ID(+)\n");
		sql.append("   AND TVM.MATERIAL_ID = VR.MATERIAL_ID(+)\n");
		sql.append("   AND TVM.MATERIAL_ID = TORDER.MATERIAL_ID(+)\n");*/
		sql.append("   AND TVM.MATERIAL_ID = VR.MATERIAL_ID(+)\n");
		sql.append("   AND TVM.MATERIAL_CODE in( '" + materialCode + "')");
		//sql.append("   AND TVPD.PRICE_ID="+priceId);

		Map<String, Object> map = pageQueryMap(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getMaterialInfo");
		return map;
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
	 * 根据detailId获得订单po
	 * 
	 * @param detailId
	 * @return
	 */
	public TtVsOrderDetailPO getTtSalesOrderDetail(String detailId) {
		TtVsOrderDetailPO po = new TtVsOrderDetailPO();
		po.setDetailId(new Long(detailId));
		List<PO> list = select(po);
		return list.size() != 0 ? (TtVsOrderDetailPO) list.get(0) : null;
	}

	/**
	 * 根据reqId获得发运申请po
	 * 
	 * @param reqId
	 * @return
	 */
	public TtVsDlvryReqPO getTtVsDlvryReq(String reqId) {
		TtVsDlvryReqPO po = new TtVsDlvryReqPO();
		po.setReqId(new Long(reqId));
		List<PO> list = select(po);
		return list.size() != 0 ? (TtVsDlvryReqPO) list.get(0) : null;
	}

	public List<Map<String, Object>> getSalesOrderDetailList(String orderId, String year,
			String week, String companyId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVMG2.GROUP_CODE SERIES_CODE,\n");
		sql.append("       TVMG2.GROUP_NAME SERIES_NAME,\n");  
		sql.append("       TVM.MATERIAL_ID,\n");  
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		//sql.append("       NVL(TVR.ENABLE_VEHICLE, 0) RESOURCE_AMOUNT,\n");  
		sql.append("       CASE\n");  
		sql.append(" WHEN NVL(VR.STOCK_AMOUNT, 0)-NVL(VR.LOCK_AMOUT,0) <= 0 THEN \n");
		sql.append("             '无' \n");  
		sql.append("           ELSE \n");  
		sql.append("             '有' \n");  
		sql.append("          end RESOURCE_AMOUNT,\n");  
		sql.append("NVL(VR.STOCK_AMOUNT, 0)-NVL(VR.LOCK_AMOUT,0) AVA_STOCK,\n");
		sql.append("       TVM.MATERIAL_ID,\n");  
		sql.append("       TVOD.ORDER_AMOUNT,\n"); 
		sql.append("       TVOD.PRICE_LIST_ID,\n"); 
		sql.append("       TVOD.SINGLE_PRICE,\n");  
		sql.append("       TVOD.TOTAL_PRICE\n");  
		//sql.append("       NVL(TVOD.DISCOUNT_RATE, 0) DISCOUNT_RATE, --折扣率\n");  
		//sql.append("       NVL(TVOD.DISCOUNT_S_PRICE, 0) DISCOUNT_S_PRICE, --折扣后单价\n");  
		//sql.append("       NVL(TVOD.DISCOUNT_PRICE, 0) DISCOUNT_PRICE --折扣额\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG1,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3,\n");  
		sql.append("       TM_VHCL_MATERIAL TVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");  
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");  
		//sql.append("       TT_VS_ORDER TVO,\n");  
		//sql.append("       (SELECT VVOR.MATERIAL_ID, VVOR.ENABLE_VEHICLE\n");  
		//sql.append("          FROM VW_VS_ORDER_RESOURCE VVOR\n");  
		//sql.append("         WHERE 1=1) TVR\n");
		sql.append("(SELECT * FROM VW_VS_RESOURCE_ENTITY_WEEK_NEW V,TT_VS_ORDER T WHERE V.WAREHOUSE_ID(+)=T.WAREHOUSE_ID AND T.ORDER_ID="+ orderId +") VR \n");
		sql.append(" WHERE TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");  
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");  
		sql.append("   AND TVMG1.GROUP_ID = TVMGR.GROUP_ID\n");  
		sql.append("   AND TVMGR.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVM.MATERIAL_ID = VR.MATERIAL_ID(+)\n");  
		sql.append("   AND TVM.MATERIAL_ID = TVOD.MATERIAL_ID\n"); 
		//sql.append("   AND TVO.ORDER_ID=TVOD.ORDER_ID \n");
		//sql.append("   AND TVO.WAREHOUSE_ID=VR.WAREHOUSE_ID\n");
		sql.append("   AND TVOD.ORDER_ID = "+ orderId+"\n");  
		sql.append(" ORDER BY TVOD.CREATE_DATE DESC, TVOD.DETAIL_ID");

		
		/*sql.append("SELECT TVMG3.GROUP_CODE SERIES_CODE,\n");
		sql.append("       TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("       TVM.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       NVL(TVR.RESOURCE_AMOUNT, 0) RESOURCE_AMOUNT,\n");
		sql.append("       TVM.MATERIAL_ID,\n");
		sql.append("       TVOD.ORDER_AMOUNT,\n");
		sql.append("       TVOD.SINGLE_PRICE,\n");
		sql.append("       TVOD.TOTAL_PRICE,\n");
		sql.append("	   NVL(TVOD.DISCOUNT_RATE, 0) DISCOUNT_RATE, --折扣率\n");
		sql.append("	   NVL(TVOD.DISCOUNT_S_PRICE, 0) DISCOUNT_S_PRICE, --折扣后单价\n");  
		sql.append("       NVL(TVOD.DISCOUNT_PRICE, 0) DISCOUNT_PRICE --折扣额\n");

		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("       TM_VHCL_MATERIAL TVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       (SELECT TR.MATERIAL_ID, TR.RESOURCE_AMOUNT\n");
		sql.append("          FROM TT_VS_RESOURCE TR\n");
		sql.append("         WHERE TR.RESOURCE_YEAR = '" + year + "'\n");
		sql.append("           AND TR.RESOURCE_WEEK = '" + week + "'\n");
		sql.append("           AND TR.SPECIAL_BATCH_NO IS NULL) TVR\n");
		sql.append(" WHERE TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TVMG1.GROUP_ID = TVMGR.GROUP_ID\n");
		sql.append("   AND TVMGR.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVR.MATERIAL_ID(+)\n");
		sql.append("   AND TVM.MATERIAL_ID = TVOD.MATERIAL_ID\n");
		sql.append("   AND TVOD.ORDER_ID = " + orderId);
		sql.append("ORDER BY TVOD.CREATE_DATE DESC, TVOD.DETAIL_ID\n");*/

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getSalesOrderDetailList");
		return list;
	}

	/**
	 * 补充订单与审核明细列表
	 * 
	 * @param orderId
	 * @param companyId
	 * @param dealerId
	 * @return
	 */
	public List<Map<String, Object>> getSalesOrderPreCheckDetailList(String orderId,
			String companyId, String dealerId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVMG3.GROUP_CODE SERIES_CODE,\n");
		sql.append("       TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("       TVM.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       NVL(TVR.RESOURCE_AMOUNT, 0) RESOURCE_AMOUNT,\n");
		sql.append("       TVM.MATERIAL_ID,\n");
		sql.append("       TVOD.ORDER_AMOUNT,\n");
		sql.append("       TVOD.SINGLE_PRICE,\n");
		sql.append("       TVOD.TOTAL_PRICE,\n");
		sql.append("       TVOD.DETAIL_ID\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("       TM_VHCL_MATERIAL TVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       (SELECT MATERIAL_ID, COUNT(1) AS RESOURCE_AMOUNT\n");
		sql.append("          FROM TM_VEHICLE\n");
		sql.append("         WHERE OEM_COMPANY_ID = " + companyId + "\n");
		sql.append("           AND DEALER_ID IN (" + dealerId + ")\n");
		sql.append("           AND LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n");
		sql.append("           AND LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_03 + ", "
				+ Constant.VEHICLE_LIFE_05 + ")\n");
		sql.append("           AND SPECIAL_BATCH_NO IS NULL\n");
		sql.append("         GROUP BY MATERIAL_ID) TVR\n");
		sql.append(" WHERE TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TVMG1.GROUP_ID = TVMGR.GROUP_ID\n");
		sql.append("   AND TVMGR.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVR.MATERIAL_ID(+)\n");
		sql.append("   AND TVM.MATERIAL_ID = TVOD.MATERIAL_ID\n");
		sql.append("   AND TVOD.ORDER_ID = " + orderId + "");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getSalesOrderPreCheckDetailList");
		return list;
	}

	public List<Map<String, Object>> getOrderCheckList(String orderId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TO_CHAR(TVOC.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE,\n");
		sql.append("       TOG.ORG_NAME,\n");
		sql.append("       TU.NAME,\n");
		sql.append("       TVOC.CHECK_STATUS,\n");
		sql.append("       TVOC.CHECK_DESC\n");
		sql.append("  FROM TT_VS_ORDER_CHECK TVOC, TM_ORG TOG, TC_USER TU\n");
		sql.append(" WHERE TVOC.CHECK_ORG_ID = TOG.ORG_ID\n");
		sql.append("   AND TVOC.CHECK_USER_ID = TU.USER_ID");
		sql.append("   AND TVOC.ORDER_ID = " + orderId);
		sql.append("	ORDER BY TVOC.CREATE_DATE DESC") ;

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getOrderCheckList");
		return list;
	}

	/**
	 * 
	 * @param dealerId
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getUrgentOrderPrepCheckList(String dealerId,
			String areaIds, int curPage, int pageSize) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT A.ORDER_ORG_NAME,\n");
		sql.append("       A.BILLING_ORG_NAME,\n");
		sql.append("       A.ORDER_ID,\n");
		sql.append("       A.ORDER_NO,\n");
		sql.append("       A.QUOTA_DATE,\n");
		sql.append("       A.ORDER_TYPE,\n");
		sql.append("       A.ORDER_STATUS,\n");
		sql.append("       B.ORDER_AMOUNT,\n");
		sql.append("       A.IS_FLEET,\n");
		sql.append("       A.FLEET_ID\n");
		sql.append("  FROM (SELECT TD1.DEALER_SHORTNAME ORDER_ORG_NAME,\n");
		sql.append("               TD2.DEALER_SHORTNAME BILLING_ORG_NAME,\n");
		sql.append("               TSO.ORDER_ID,\n");
		sql.append("               TSO.ORDER_NO,\n");
		sql.append("               TSO.ORDER_YEAR || '年' || TSO.ORDER_WEEK || '周' QUOTA_DATE,\n");
		sql.append("               TSO.ORDER_TYPE,\n");
		sql.append("               TSO.ORDER_STATUS,\n");
		sql.append("               NVL(TSO.IS_FLEET, 0) IS_FLEET,\n");
		sql.append("               NVL(TSO.FLEET_ID, 0) FLEET_ID\n");
		sql.append("          FROM TT_VS_ORDER        TSO,\n");
		sql.append("               TM_DEALER          TD1,\n");
		sql.append("               TM_DEALER          TD2\n");
		sql.append("         WHERE TSO.ORDER_ORG_ID = TD1.DEALER_ID\n");
		sql.append("           AND TSO.BILLING_ORG_ID = TD2.DEALER_ID(+)\n");
		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		sql.append("           AND TSO.ORDER_TYPE in (" + Constant.ORDER_TYPE_02 + "," + Constant.ORDER_TYPE_03 + ")\n");
		sql.append("           AND TSO.ORDER_STATUS = " + Constant.ORDER_COM_STATUS_02 + "\n");
		sql.append("           AND TD2.DEALER_ID IN (" + dealerId + ")) A,\n");
		sql.append("       (SELECT TSO.ORDER_ID, SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER TSO, TT_VS_ORDER_DETAIL TSOD, TM_DEALER TD\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TSO.ORDER_ORG_ID = TD.DEALER_ID\n");
		sql.append("           AND TSO.AREA_ID IN (" + areaIds + ")\n");
		sql.append("           AND TSO.ORDER_TYPE in (" + Constant.ORDER_TYPE_02 + "," + Constant.ORDER_TYPE_03 + ")\n");
		sql.append("           AND TSO.ORDER_STATUS = " + Constant.ORDER_COM_STATUS_02 + "\n");
		sql.append("           AND TSO.BILLING_ORG_ID IN (" + dealerId + ")\n");
		sql.append("         GROUP BY TSO.ORDER_ID) B\n");
		sql.append(" WHERE A.ORDER_ID = B.ORDER_ID \n");
		sql.append("ORDER BY A.ORDER_ID DESC\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getUrgentOrderPrepCheckList", pageSize,
				curPage);
		return ps;
	}

	public String getParentDealerId(String dealerId, Integer dealerLevel) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT *\n");
		sql.append("  FROM (SELECT TMD.DEALER_ID, TMD.DEALER_LEVEL\n");
		sql.append("          FROM TM_DEALER TMD\n");
		sql.append("         WHERE TMD.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("         START WITH TMD.DEALER_ID = " + dealerId + "\n");
		sql.append("        CONNECT BY PRIOR TMD.PARENT_DEALER_D = TMD.DEALER_ID) A\n");
		sql.append(" WHERE A.DEALER_LEVEL = " + dealerLevel + "");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getParentDealerId");

		return list.size() != 0 ? ((BigDecimal) (list.get(0).get("DEALER_ID"))).toString() : null;
	}

	// 获得TmDateSetPO
	public TmDateSetPO geTmDateSetPO(TmDateSetPO po) {
		List<PO> list = select(po);
		return list.size() != 0 ? (TmDateSetPO) list.get(0) : null;
	}

	// 获得TtVsAccountPO
	public TtVsAccountPO geTtVsAccountPO(TtVsAccountPO po) {
		List<PO> list = select(po);
		return list.size() != 0 ? (TtVsAccountPO) list.get(0) : null;
	}

	public String getOrderPrice(String orderId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT NVL(SUM(TVOD.TOTAL_PRICE), 0) TOTAL_PRICE\n");
		sql.append("          FROM TT_VS_ORDER_DETAIL TVOD\n");
		sql.append("         WHERE TVOD.ORDER_ID = " + orderId);

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getParentDealerId");
		int size = list.size();
		System.out.println("size" + size);

		return list.size() != 0 ? ((BigDecimal) (list.get(0).get("TOTAL_PRICE"))).toString() : null;
	}

	public List<Map<String, Object>> getGeneralOrderReportList(Map<String, Object> map) {
		String year = (String) map.get("year");
		String week = (String) map.get("week");
		String areaId = (String) map.get("areaId");
		String dealerId = (String) map.get("dealerId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVO.ORDER_ID\n");
		sql.append("  FROM TT_VS_ORDER TVO\n");
		sql.append(" WHERE TVO.ORDER_YEAR = " + year + "\n");
		sql.append("   AND TVO.ORDER_WEEK = " + week + "\n");
		sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		sql.append("   AND TVO.ORDER_ORG_ID IN (" + dealerId + ")\n");
		sql.append("   AND TVO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("   AND TVO.ORDER_STATUS = " + Constant.ORDER_COM_STATUS_01 + "\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getGeneralOrderReportList2");
		return list;
	}
	public Map<String, Object> getMyMap(Long companyId,String dealerId) {

		StringBuffer sql = new StringBuffer();
	    sql.append("SELECT TVP.PRICE_ID,\n");  
	    sql.append("       TVP.PRICE_CODE,\n");  
	    sql.append("       TVP.PRICE_DESC PRICE_DESC,TDPR.IS_DEFAULT,\n");  
	    sql.append("       TC.CODE_DESC,\n");  
	    sql.append("       TDPR.RELATION_ID,TVP.CREATE_DATE\n");  
	    sql.append("  FROM TT_VS_PRICE              TVP,\n");  
	    sql.append("       TM_DEALER_PRICE_RELATION TDPR,\n");  
	    sql.append("       TC_CODE                  TC,\n");  
	    sql.append("       TM_DEALER                TD\n");  
	    sql.append(" WHERE TDPR.PRICE_ID = TVP.PRICE_ID\n");  
	    sql.append("   AND TDPR.DEALER_ID = TD.DEALER_ID\n");  
	    sql.append("   AND TDPR.IS_DEFAULT = TC.CODE_ID\n");  
	    //sql.append("	AND TDPR.CREATE_BY = '-1'\n");
	    //sql.append("   AND TVP.COMPANY_ID = " + companyId + "\n");  
	    sql.append("   AND TDPR.DEALER_ID IN (" + dealerId + ")\n");  
	    sql.append("   order by TVP.CREATE_DATE DESC  ");
	   // sql.append(" ORDER BY TC.CODE_DESC DESC, TVP.PRICE_DESC\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getMyMap");
		return list.size() != 0 ? (Map<String, Object>) list.get(0) : null;
	}

	/**
	 * 获得价格类型列表
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPriceList(Map<String, Object> map) {
		String dealerId = (String) map.get("dealerId");
		String companyId = (String) map.get("companyId");

		StringBuffer sql = new StringBuffer();
		
		
//		sql.append("SELECT *\n");
//	    sql.append("FROM (\n");  
//	    sql.append("SELECT TVP.PRICE_ID,\n");  
//	    sql.append("       TVP.PRICE_CODE,\n");  
//	    sql.append("       TVP.PRICE_DESC,\n");  
//	    sql.append("       TC.CODE_DESC,\n");  
//	    sql.append("       TDPR.IS_DEFAULT,\n");  
//	    sql.append("       TDPR.RELATION_ID\n");  
//	    sql.append("  FROM TT_VS_PRICE              TVP,\n");  
//	    sql.append("       TM_DEALER_PRICE_RELATION TDPR,\n");  
//	    sql.append("       TC_CODE                  TC,\n");  
//	    sql.append("       TM_DEALER                TD\n");  
//	    sql.append(" WHERE TRUNC(TVP.START_DATE) <= TRUNC(SYSDATE)\n");  
//	    sql.append("   AND TRUNC(TVP.END_DATE) >= TRUNC(SYSDATE)\n");  
//	    sql.append("   AND TDPR.PRICE_ID = TVP.PRICE_ID\n");  
//	    sql.append("   AND TDPR.DEALER_ID = TD.DEALER_ID\n");  
//	    sql.append("   AND TDPR.IS_DEFAULT = TC.CODE_ID\n");  
//	    sql.append("	AND TDPR.CREATE_BY != '-1'\n");
//	    sql.append("	AND TDPR.CREATE_BY != '-100'\n");
//	    sql.append("	 AND TDPR.IS_DEFAULT="+Constant.IF_TYPE_NO+"\n");
//	    sql.append("   AND TVP.COMPANY_ID = " + companyId + "\n");  
//	    sql.append("   AND TDPR.DEALER_ID = '" + dealerId + "'\n");  
//	    sql.append("   UNION\n");  
		 sql.append("SELECT TVPA.PRICE_ID,\n");  
		    sql.append("       TVPA.PRICE_CODE,\n");  
		    sql.append("       TVPA.PRICE_DESC PRICE_DESC,\n");  
		    sql.append("       TCA.CODE_DESC,TDPRA.IS_DEFAULT,\n");  
		    sql.append("       TDPRA.RELATION_ID\n");  
		    sql.append("  FROM TT_VS_PRICE              TVPA,\n");  
		    sql.append("       TM_DEALER_PRICE_RELATION TDPRA,\n");  
		    sql.append("       TC_CODE TCA,\n");  
		    sql.append("       TM_DEALER                TDA\n");  
		    sql.append(" WHERE TDPRA.PRICE_ID = TVPA.PRICE_ID\n");  
		    sql.append("   AND TDPRA.DEALER_ID = TDA.DEALER_ID\n");  
		    sql.append("   AND TDPRA.IS_DEFAULT = TCA.CODE_ID\n");  
		    //sql.append("	 AND TDPRA.CREATE_BY!=-1");
		    //sql.append("	 AND TDPRA.CREATE_BY!=-100");
		    //sql.append("   AND TDPRA.IS_DEFAULT="+Constant.IF_TYPE_YES+"\n");
		    //sql.append("   AND TVPA.CREATE_BY = '-100'\n");  
		    //sql.append("   AND TVPA.COMPANY_ID = " + companyId + "\n");  
		    sql.append("   AND TDPRA.DEALER_ID IN (" + dealerId + ")\n");  
		    sql.append(" ORDER BY TCA.CODE_DESC DESC, TVPA.PRICE_DESC\n");

//		sql.append("SELECT TVP.PRICE_ID,\n");
//		sql.append("       TVP.PRICE_CODE,\n");  
//		sql.append("       TVP.PRICE_DESC,\n");  
//		sql.append("       TDPR.IS_DEFAULT,\n");  
//		sql.append("       TDPR.RELATION_ID\n");  
//		sql.append("  FROM TT_VS_PRICE TVP, TM_DEALER_PRICE_RELATION TDPR\n");  
//		sql.append(" WHERE TVP.PRICE_ID = TDPR.PRICE_ID\n");  
//		sql.append("   AND TRUNC(TVP.START_DATE) <= TRUNC(SYSDATE)\n");  
//		sql.append("   AND TRUNC(TVP.END_DATE) >= TRUNC(SYSDATE)\n");  
//		sql.append("   AND TDPR.RELATION_ID NOT IN\n");  
//		sql.append("       ((SELECT TDPR.RELATION_ID\n");  
//		sql.append("          FROM TM_DEALER_PRICE_RELATION TDPR\n");  
//		sql.append("         WHERE TDPR.IS_DEFAULT = " + Constant.IF_TYPE_NO + "\n");  
//		sql.append("           AND TDPR.CREATE_BY = -1))\n");  
//		sql.append("   AND TVP.COMPANY_ID = " + companyId + "\n");  
//		sql.append("   AND TDPR.DEALER_ID = " + dealerId + "\n");  
//		sql.append("UNION\n");  
//		sql.append("SELECT TVPA.PRICE_ID,\n");  
//		sql.append("       TVPA.PRICE_CODE,\n");  
//		sql.append("       TVPA.PRICE_DESC,\n");  
//		sql.append("       TDPRA.IS_DEFAULT,\n");  
//		sql.append("       TDPRA.RELATION_ID\n");  
//		sql.append("  FROM TT_VS_PRICE TVPA, TM_DEALER_PRICE_RELATION TDPRA\n");  
//		sql.append(" WHERE TVPA.PRICE_ID = TDPRA.PRICE_ID\n");  
//		sql.append("   AND TVPA.CREATE_BY = '-100'\n");  
//		sql.append("   AND TVPA.COMPANY_ID = " + companyId + "\n");  
//		sql.append("   AND TDPRA.DEALER_ID = " + dealerId + "\n");  


		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getPriceList");
		return list;
	}
	
	
	/**
	 * 获得价格类型列表
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPriceListByReqId(Map<String, Object> map) {
		String dealerId = (String) map.get("dealerId");
		String companyId = (String) map.get("companyId");
		String reqId = (String) map.get("reqId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVP.PRICE_ID, TVP.PRICE_CODE, TVP.PRICE_DESC, TDPR.IS_DEFAULT\n");
		sql.append("  FROM vw_TT_VS_PRICE TVP, vw_TM_DEALER_PRICE_RELATION TDPR\n");  
		sql.append(" WHERE TVP.PRICE_ID = TDPR.PRICE_ID\n");  
		//sql.append("   AND TVP.START_DATE < SYSDATE\n");  
		//sql.append("   AND TRUNC(TVP.END_DATE) >= TRUNC(SYSDATE)\n");  
		sql.append("   and tdpr.is_default = " +Constant.IF_TYPE_YES+ "\n");  
		sql.append("   AND TVP.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND TDPR.DEALER_ID = "+dealerId+"\n");  
		sql.append("UNION\n");  
		sql.append("SELECT TVP2.PRICE_ID, TVP2.PRICE_CODE, TVP2.PRICE_DESC, TDPR2.IS_DEFAULT\n");  
		sql.append("  FROM vw_TT_VS_PRICE              TVP2,\n");  
		sql.append("       vw_TM_DEALER_PRICE_RELATION TDPR2,\n");  
		sql.append("       TT_VS_DLVRY_REQ          TVDR\n");  
		sql.append(" WHERE TVP2.PRICE_ID = TDPR2.PRICE_ID\n");  
		sql.append("   AND TVP2.PRICE_ID = TVDR.PRICE_ID\n");  
		sql.append("   AND TDPR2.DEALER_ID = "+dealerId+"\n");  
		sql.append("   AND TVDR.REQ_ID = "+reqId+"\n");  
		sql.append("UNION\n");  
		sql.append("SELECT TVPA.PRICE_ID, TVPA.PRICE_CODE, TVPA.PRICE_DESC, TDPRA.IS_DEFAULT\n");  
		sql.append("  FROM vw_TT_VS_PRICE TVPA, vw_TM_DEALER_PRICE_RELATION TDPRA\n");  
		sql.append(" WHERE TVPA.PRICE_ID = TDPRA.PRICE_ID\n");  
		sql.append("   AND TDPRA.CREATE_BY <> '-100'\n");  
		sql.append("   and TDPRA.CREATE_BY <> '-1'\n");  
		sql.append("   AND TVPA.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND TDPRA.DEALER_ID = "+dealerId+"");


		/*sql.append("SELECT TVP.PRICE_ID, TVP.PRICE_CODE, TVP.PRICE_DESC, TDPR.IS_DEFAULT\n");
		sql.append("  FROM TT_VS_PRICE TVP, TM_DEALER_PRICE_RELATION TDPR\n");  
		sql.append(" WHERE TVP.PRICE_ID = TDPR.PRICE_ID\n");  
		sql.append("   AND TVP.START_DATE < SYSDATE\n");  
		sql.append("   AND TRUNC(TVP.END_DATE) >= TRUNC(SYSDATE)\n"); 
		sql.append(" AND TDPR.RELATION_ID NOT IN (\n");
		sql.append("    SELECT TDPR3.RELATION_ID\n");  
		sql.append("    FROM TM_DEALER_PRICE_RELATION TDPR3\n");  
		sql.append("    WHERE TDPR3.IS_DEFAULT = " +Constant.IF_TYPE_NO+ "\n");  
		sql.append("    AND TDPR3.CREATE_BY = -1\n");  
		sql.append(")\n");
		sql.append("   AND TVP.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND TDPR.DEALER_ID = "+dealerId+"\n");  
		sql.append("UNION\n");  
		sql.append("SELECT TVP2.PRICE_ID, TVP2.PRICE_CODE, TVP2.PRICE_DESC, TDPR2.IS_DEFAULT\n");  
		sql.append("  FROM TT_VS_PRICE              TVP2,\n");  
		sql.append("       TM_DEALER_PRICE_RELATION TDPR2,\n");  
		sql.append("       TT_VS_DLVRY_REQ          TVDR\n");  
		sql.append(" WHERE TVP2.PRICE_ID = TDPR2.PRICE_ID\n");  
		sql.append("   AND TVP2.PRICE_ID = TVDR.PRICE_ID\n"); 
		sql.append(" AND TDPR2.RELATION_ID NOT IN (\n");
		sql.append("    SELECT TDPR4.RELATION_ID\n");  
		sql.append("    FROM TM_DEALER_PRICE_RELATION TDPR4\n");  
		sql.append("    WHERE TDPR4.IS_DEFAULT = " +Constant.IF_TYPE_NO+ "\n");  
		sql.append("    AND TDPR4.CREATE_BY = -1\n"); 
		sql.append(")\n");
		sql.append("   AND TDPR2.DEALER_ID = "+dealerId+"\n");  
		sql.append("   AND TVDR.REQ_ID = "+reqId+"");
		sql.append("UNION\n");
		sql.append("SELECT TVPA.PRICE_ID,\n");  
		sql.append("       TVPA.PRICE_CODE,\n");  
		sql.append("       TVPA.PRICE_DESC,\n");  
		sql.append("       TDPRA.IS_DEFAULT\n");  
		sql.append("  FROM TT_VS_PRICE TVPA, TM_DEALER_PRICE_RELATION TDPRA\n");  
		sql.append(" WHERE TVPA.PRICE_ID = TDPRA.PRICE_ID\n");  
		sql.append("   AND TVPA.CREATE_BY = '-100'\n");  
		sql.append("   AND TVPA.COMPANY_ID = " + companyId + "\n");  
		sql.append("   AND TDPRA.DEALER_ID = "+ dealerId +"\n");*/


		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}

	/**
	 * 收货方列表
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getReceiverList(Map<String, Object> map) {
		String dealerId = (String) map.get("dealerId");
		String areaId = (String) map.get("areaId");
		String dealerClass = map.get("dealerClass").toString() ;

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT distinct TD.DEALER_ID, TD.DEALER_SHORTNAME DEALER_NAME\n");
		//sql.append("  FROM TM_DEALER TD, TM_DEALER_BUSINESS_AREA TDBA\n");
		sql.append("  FROM TM_DEALER TD\n");
		sql.append(" WHERE TD.STATUS = " + Constant.STATUS_ENABLE + "\n");
		/*sql.append("   AND TD.DEALER_ID = TDBA.DEALER_ID\n");
		if(!"".equals(areaId)&&areaId!=null){
		sql.append("   AND TDBA.AREA_ID = "+areaId+"\n");
		}*/
		if(Constant.DEALER_CLASS_TYPE_12.toString().equals(dealerClass)) {
			sql.append("   AND td.dealer_class = "+Constant.DEALER_CLASS_TYPE_12+"\n");
		}
		sql.append(" START WITH TD.DEALER_ID = " + dealerId + "\n");
		sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getReceiverList");
		return list;
	}

	public List<Map<String, Object>> getSinglePriceList(Map<String, Object> map) {
		String priceId = (String) map.get("priceId");
		String ids = (String) map.get("ids");
		String orderType = (String)map.get("orderType") ;

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVM.MATERIAL_ID,F_GET_PRICE(" + priceId + ", TVM.MATERIAL_ID) as PRICE\n");
		sql.append("  FROM TM_VHCL_MATERIAL TVM\n");
		sql.append(" WHERE 1 = 1\n");
		if (ids.equals("")) {
			sql.append("   AND TVM.MATERIAL_ID IS NULL");
		} else {
			sql.append("   AND TVM.MATERIAL_ID IN (" + ids + ")");
		}

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getSinglePriceList");
		return list;
	}
	
	public List<Map<String,Object>> getWarehouseResourseList(Map<String,Object> map){
		String priceId = (String) map.get("priceId");
		String ids = (String) map.get("ids");
		String warehouseId = (String)map.get("warehouseId") ;

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVMG2.GROUP_CODE SERIES_CODE,\n");
		sql.append("       TVMG2.GROUP_NAME SERIES_NAME,\n");
		sql.append("       TVM.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       CASE\n");
		sql.append("        WHEN NVL(VR.STOCK_AMOUNT, 0)-NVL(VR.LOCK_AMOUT,0) <= 0 THEN\n");
		sql.append("          '无'\n");
		sql.append("         ELSE\n");
		sql.append("          '有'\n");
		sql.append("       end RESOURCE_AMOUNT,\n");
		sql.append("       NVL(TVPD.SALES_PRICE,0) SALES_PRICE,\n");
		sql.append("      NVL(VR.STOCK_AMOUNT, 0)-NVL(VR.LOCK_AMOUT,0) AVA_STOCK,\n");
		sql.append("       TVPD.DETAIL_ID\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("       TM_VHCL_MATERIAL TVM,\n");
		sql.append("   (SELECT * FROM  TT_VS_PRICE_DTL where PRICE_ID="+priceId+") TVPD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("(SELECT * FROM VW_VS_RESOURCE_ENTITY_WEEK_NEW WHERE WAREHOUSE_ID="+warehouseId+") VR \n ");
		sql.append(" WHERE TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TVMG1.GROUP_ID = TVMGR.GROUP_ID\n");
		sql.append("   AND TVMG1.GROUP_ID = TVPD.GROUP_ID(+)\n");
		sql.append("   AND TVMGR.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = VR.MATERIAL_ID(+)\n");
		if(ids!=null&&!"".equals(ids)){
		sql.append("   AND TVM.MATERIAL_ID IN (" + ids + ")");
		}
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getWarehouseResourseList");
		return list;
	}

	public List<Map<String, Object>> getDetailSinglePriceList(Map<String, Object> map) {
		String priceId = (String) map.get("priceId");
		String ids = (String) map.get("ids");
		String orderType = (String)map.get("orderType") ;

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVOD.DETAIL_ID, F_GET_PRICE(" + priceId + ", TVOD.MATERIAL_ID, " + orderType + ") PRICE\n");
		sql.append("  FROM TT_VS_ORDER_DETAIL TVOD\n");
		sql.append(" WHERE 1 = 1\n");
		if (ids.equals("")) {
			sql.append("   AND TVOD.DETAIL_ID IS NULL");
		} else {
			sql.append("   AND TVOD.DETAIL_ID IN (" + ids + ")");
		}

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getDetailSinglePriceList");
		return list;
	}
	
	/**
	 *常规订单提报：获得常规订单详细信息
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getOrderDetailList(String year,String  week, String areaId, String areaIds,String dealerIds) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TSO.ORDER_ID,\n");
		sql.append("       TSO.ORDER_NO,\n");  
		sql.append("       TSO.AREA_ID,\n");
		sql.append("       TSO.ORDER_ORG_ID,\n");
		sql.append("       TVD.ORDER_AMOUNT,\n");  
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TVM.COLOR_NAME,\n");  
		sql.append("       TVM.MATERIAL_ID,\n");  
		sql.append("       G3.GROUP_CODE,\n");  
		sql.append("       G3.GROUP_NAME\n");  
		sql.append("  FROM TT_VS_ORDER              TSO,\n");  
		sql.append("       TT_VS_ORDER_DETAIL       TVD,\n");  
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G1,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G3\n");  
		sql.append(" WHERE TSO.ORDER_ORG_ID IN ("+dealerIds+")\n");  
		sql.append("   AND TSO.AREA_ID IN ("+areaIds+")\n");  
		sql.append("   AND TSO.AREA_ID = "+areaId+"\n");  
		sql.append("   AND TSO.ORDER_TYPE = "+Constant.ORDER_TYPE_01+" \n");  
		sql.append("   AND TSO.ORDER_STATUS = "+Constant.ORDER_COM_STATUS_01+"\n");  
		sql.append("   AND TSO.ORDER_YEAR = "+year+"\n");  
		sql.append("   AND TSO.ORDER_WEEK = "+week+"\n");
		sql.append("   AND TSO.ORDER_ID = TVD.ORDER_ID\n");  
		sql.append("   AND TVM.MATERIAL_ID = TVD.MATERIAL_ID\n");  
		sql.append("   AND TVM.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("   AND R.GROUP_ID = G1.GROUP_ID\n");  
		sql.append("   AND G1.PARENT_GROUP_ID = G2.GROUP_ID\n");  
		sql.append("   AND G2.PARENT_GROUP_ID = G3.GROUP_ID\n");  
		sql.append(" ORDER BY TSO.ORDER_NO DESC\n");


		List<Map<String, Object>> list = pageQuery(sql.toString(), null,"com.infodms.dms.dao.sales.OrderReportDao.getOrderDetailList");
		return list;
	}
	
	public List<Map<String, Object>> getTMOREG(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ORG_ID, ORG_NAME FROM TM_ORG WHERE  DUTY_TYPE = ").append(Constant.DUTY_TYPE_LARGEREGION);
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public void insertTtVsDlvryReq(TtVsOrderPO po, Long reqId, Integer reqTotalAmount) {
		TtVsDlvryReqPO tvdrp = new TtVsDlvryReqPO();
		tvdrp.setReqId(reqId);
		tvdrp.setAreaId(po.getAreaId());
		tvdrp.setOrderId(po.getOrderId());
		tvdrp.setFundType(po.getFundTypeId());
		tvdrp.setDeliveryType(po.getDeliveryType());
		if(po.getIsCustomAddr().toString().equals(Constant.IF_TYPE_NO.toString())){
			tvdrp.setAddressId(po.getDeliveryAddress());
		}else{
			tvdrp.setAddressId(null);
		}
		
		tvdrp.setReqDate(new Date());
		tvdrp.setReqTotalAmount(reqTotalAmount);
		tvdrp.setReqExecStatus(Constant.REQ_EXEC_STATUS_01);
		tvdrp.setVer(new Integer(0));
		tvdrp.setCreateBy(po.getCreateBy());
		tvdrp.setCreateDate(new Date());
		/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	     * 描述:银翔原型报错修复
	     * 发运申请相关
	     * urgentDlvryReq 第三参数“po.getFleetId()” --> “1L”
	     * Date:2017-06-29
	     */
		tvdrp.setFleetId(1L);
		/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	     * 描述:银翔原型报错修复
	     * 发运申请相关
	     * urgentDlvryReq 第三参数“po.getFleetAddress()” --> “""”
	     * Date:2017-06-29
	     */
		tvdrp.setFleetAddress("");
		tvdrp.setPriceId(po.getPriceId());
		tvdrp.setTmpLicenseAmount(po.getTmpLicenseAmount());
		//tvdrp.setOtherPriceReason(po.getOtherPriceReason());
		tvdrp.setReceiver(po.getReceiver());
		tvdrp.setLinkMan(po.getLinkMan());
		tvdrp.setTel(po.getTel());
		/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	     * 描述:银翔原型报错修复
	     * 发运申请相关
	     * urgentDlvryReq 第三参数“po.getDiscount()” --> “1.0”
	     * Date:2017-06-29
	     */
		tvdrp.setDiscount(1.0);
		//tvdrp.setIsFleet(po.getIsFleet());
		tvdrp.setReqRemark(po.getOrderRemark()) ;
		/*if(po.getIsFleet().intValue() == 1){
			tvdrp.setReqStatus(Constant.ORDER_REQ_STATUS_08);
		}
		else{
			tvdrp.setReqStatus(Constant.ORDER_REQ_STATUS_01);			
		}*/
		tvdrp.setReqStatus(Constant.ORDER_REQ_STATUS_01);
		tvdrp.setReqTotalPrice(po.getOrderPrice());
		String dlvryReqNo = po.getOrderNo();
		//String dlvryReqNo = GetOrderNOUtil.getDlvryReqNO(po.getOrderNo());
		tvdrp.setDlvryReqNo(dlvryReqNo);
		tvdrp.setWarehouseId(new Long(po.getWarehouseId()));
		tvdrp.setDealerId(po.getBillingOrgId()); //经销商ID
		tvdrp.setIsRebate(po.getIsRebate()); //是否返利
		tvdrp.setRebateAmount(po.getRebateAmount());//返利额度
		dao.insert(tvdrp);
	}
	
	public void updateTtVsDlvryReq(TtVsOrderPO po, Integer reqTotalAmount) {
		TtVsDlvryReqPO oldTvdrp = new TtVsDlvryReqPO();
		oldTvdrp.setOrderId(po.getOrderId());
		TtVsDlvryReqPO tvdrp = new TtVsDlvryReqPO();
		tvdrp.setAreaId(po.getAreaId());
		//tvdrp.setOrderId(po.getOrderId());
		tvdrp.setFundType(po.getFundTypeId());
		tvdrp.setDeliveryType(po.getDeliveryType());
		if(po.getIsCustomAddr().toString().equals(Constant.IF_TYPE_NO.toString())){
			tvdrp.setAddressId(po.getDeliveryAddress());
		}else{
			tvdrp.setAddressId(null);
		}
		tvdrp.setReqDate(new Date());
		tvdrp.setReqTotalAmount(reqTotalAmount);
		tvdrp.setReqExecStatus(Constant.REQ_EXEC_STATUS_01);
		tvdrp.setVer(new Integer(0));
		tvdrp.setCreateBy(po.getCreateBy());
		tvdrp.setCreateDate(new Date());
		/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	     * 描述:银翔原型报错修复
	     * 发运申请相关
	     * urgentDlvryReq 第三参数“po.getFleetId()” --> “1L”
	     * Date:2017-06-29
	     */
		tvdrp.setFleetId(1L);
		/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	     * 描述:银翔原型报错修复
	     * 发运申请相关
	     * urgentDlvryReq 第三参数“po.getFleetAddress()” --> “""”
	     * Date:2017-06-29
	     */
		tvdrp.setFleetAddress("");
		tvdrp.setPriceId(po.getPriceId());
		tvdrp.setTmpLicenseAmount(po.getTmpLicenseAmount());
		//tvdrp.setOtherPriceReason(po.getOtherPriceReason());
		tvdrp.setReceiver(po.getReceiver());
		tvdrp.setLinkMan(po.getLinkMan());
		tvdrp.setTel(po.getTel());
		/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	     * 描述:银翔原型报错修复
	     * 发运申请相关
	     * urgentDlvryReq 第三参数“po.getDiscount()” --> “1.0”
	     * Date:2017-06-29
	     */
		tvdrp.setDiscount(1.0);
		//tvdrp.setIsFleet(po.getIsFleet());
		tvdrp.setReqRemark(po.getOrderRemark()) ;
		/*if(tvdrp.getIsFleet().intValue() == 1){
			tvdrp.setReqStatus(Constant.ORDER_REQ_STATUS_08);
		}
		else{
			tvdrp.setReqStatus(Constant.ORDER_REQ_STATUS_01);			
		}*/
		tvdrp.setReqStatus(Constant.ORDER_REQ_STATUS_01);	
		tvdrp.setReqTotalPrice(po.getOrderPrice());
		String dlvryReqNo = po.getOrderNo();
		//String dlvryReqNo = GetOrderNOUtil.getDlvryReqNO(po.getOrderNo());
		tvdrp.setDlvryReqNo(dlvryReqNo);
		tvdrp.setWarehouseId(new Long(po.getWarehouseId()));
		tvdrp.setDealerId(po.getBillingOrgId()); //经销商ID
		tvdrp.setIsRebate(po.getIsRebate()); //是否返利
		tvdrp.setRebateAmount(po.getRebateAmount());//返利额度
		dao.update(oldTvdrp, tvdrp);
	}
	
	public void insertTtVsDlvryReqDtl(TtVsOrderDetailPO po, Long reqId) {
		TtVsDlvryReqDtlPO tvdrdp = new TtVsDlvryReqDtlPO();
		tvdrdp.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
		tvdrdp.setReqId(reqId);
		tvdrdp.setOrderDetailId(po.getDetailId());
		tvdrdp.setMaterialId(po.getMaterialId());
	//	tvdrdp.setPatchNo(po.getSpecialBatchNo());
		tvdrdp.setReqAmount(po.getOrderAmount());
		tvdrdp.setDealerReqAmount(po.getOrderAmount());
		tvdrdp.setVer(new Integer(0));
		tvdrdp.setCreateBy(po.getCreateBy());
		tvdrdp.setCreateDate(new Date());
		tvdrdp.setSinglePrice(po.getSinglePrice());
		tvdrdp.setTotalPrice(po.getTotalPrice());
		tvdrdp.setRebateAmount(po.getRebateAmount());
		tvdrdp.setAfterRebatePrice(po.getAfterRebatePrice());
		/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	     * 描述:银翔原型报错修复
	     * 发运申请相关
	     * urgentDlvryReq 第三参数“po.getDiscountRate()” --> “1.0F”
	     * Date:2017-06-29
	     */
		tvdrdp.setDiscountRate(1.0F);
		/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	     * 描述:银翔原型报错修复
	     * 发运申请相关
	     * urgentDlvryReq 第三参数“po.getDiscountSPrice()” --> “1.0”
	     * Date:2017-06-29
	     */
		tvdrdp.setDiscountSPrice(1.0);
		/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	     * 描述:银翔原型报错修复
	     * 发运申请相关
	     * urgentDlvryReq 第三参数“po.getDiscountPrice()” --> “1.0”
	     * Date:2017-06-29
	     */
		tvdrdp.setDiscountPrice(1.0);
		
		dao.insert(tvdrdp);
	}
	
	public List<Map<String, Object>> getStockList(Map<String, Object> map) {

		String ids = (String)map.get("ids");
		String companyId = (String)map.get("companyId");
		String warehouseId = (String)map.get("warehouseId");
		
		StringBuffer sql= new StringBuffer();		

		sql.append("SELECT A.MATERIAL_ID,\n");
		sql.append("       A.MATERIAL_CODE,\n");  
		sql.append("       A.MATERIAL_NAME,\n");  
		sql.append("       A.STOCK_AMOUNT - NVL(B.RESERVE_AMOUNT, 0) AMOUNT\n");  
		sql.append("  FROM (SELECT TVM.MATERIAL_ID,\n");  
		sql.append("               TVM.MATERIAL_CODE,\n");  
		sql.append("               TVM.MATERIAL_NAME,\n");  
		sql.append("               NVL(TEMP.STOCK_AMOUNT, 0) STOCK_AMOUNT\n");  
		sql.append("          FROM TM_VHCL_MATERIAL TVM,\n");  
		sql.append("               (SELECT TMV.MATERIAL_ID, COUNT(TMV.VEHICLE_ID) STOCK_AMOUNT\n");  
		sql.append("                  FROM TM_VEHICLE TMV, TM_WAREHOUSE W\n");  
		sql.append("                 WHERE TMV.WAREHOUSE_ID = W.WAREHOUSE_ID\n");  
		sql.append("                   AND W.WAREHOUSE_TYPE <> "+Constant.WAREHOUSE_TYPE_04+"\n"); 
		// sql.append("                   AND TMV.ORG_TYPE IS NULL\n");  
		sql.append("                   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_02+"\n");  
		sql.append("                   AND TMV.LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n");  
		// sql.append("                   AND TMV.ORG_ID IS NULL\n");  
		sql.append("                   AND TMV.DEALER_ID IS NULL\n");  
		sql.append("                   AND TMV.OEM_COMPANY_ID = "+companyId+"\n");  
		sql.append("                   AND TMV.WAREHOUSE_ID = "+warehouseId+"\n");  
		sql.append("                 GROUP BY TMV.MATERIAL_ID) TEMP\n");  
		sql.append("         WHERE TVM.MATERIAL_ID = TEMP.MATERIAL_ID(+)\n");  
		sql.append("           AND TVM.MATERIAL_ID IN ("+ids+")) A,\n");  
		sql.append("       (SELECT TVM.MATERIAL_ID,\n");  
		sql.append("               TVM.MATERIAL_CODE,\n");  
		sql.append("               TVM.MATERIAL_NAME,\n");  
		sql.append("               (SUM(NVL(AMOUNT, 0)) - SUM(NVL(DELIVERY_AMOUNT, 0))) AS RESERVE_AMOUNT\n");  
		sql.append("          FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR, TM_VHCL_MATERIAL TVM\n");  
		sql.append("         WHERE TVORR.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("           AND TVORR.RESERVE_STATUS = "+Constant.RESOURCE_RESERVE_STATUS_01+"\n");  
		sql.append("           AND TVORR.MATERIAL_ID IN ("+ids+")\n");  
		sql.append("           AND TVORR.OEM_COMPANY_ID = "+companyId+"\n");  
		sql.append("           AND TVORR.WAREHOUSE_ID = "+warehouseId+"\n");  
		sql.append("         GROUP BY TVM.MATERIAL_ID, TVM.MATERIAL_CODE, TVM.MATERIAL_NAME) B\n");  
		sql.append(" WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 通过结算中心订单id获得下级订单价格
	 * @param orderId
	 * @param materialCode
	 * @return
	 */
	public String getPriceIdByNewOrderId(String orderId, String materialCode) {
		
		StringBuffer sql= new StringBuffer();		

		sql.append("SELECT F_GET_PRICE(TVO2.PRICE_ID, TVM.MATERIAL_ID) PRICE\n");
		sql.append("  FROM TT_VS_ORDER TVO1, TT_VS_ORDER TVO2, TM_VHCL_MATERIAL TVM\n");  
		sql.append(" WHERE TVO1.OLD_ORDER_ID = TVO2.ORDER_ID\n");  
		sql.append("   AND TVO1.ORDER_ID = "+orderId+"\n");  
		sql.append("   AND TVM.MATERIAL_CODE = '"+materialCode+"'");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		if(list.size() != 0){
			Map<String, Object> map = list.get(0);
			BigDecimal price = (BigDecimal)map.get("PRICE");
			return price.toString();
		}
		
		return null;
	}
	
	/**
	 * 通过结算中心价格是否存在
	 * @param orderId
	 * @param materialCode
	 * @return
	 */
	public String getPriceIdByNewOrderIdYz(String orderId, String materialCode) {
		
		StringBuffer sql= new StringBuffer();		

		sql.append("SELECT F_GET_PRICE(TVO1.PRICE_ID, TVM.MATERIAL_ID) PRICE\n");
		sql.append("  FROM TT_VS_ORDER TVO1, TT_VS_ORDER TVO2, TM_VHCL_MATERIAL TVM\n");  
		sql.append(" WHERE TVO1.OLD_ORDER_ID = TVO2.ORDER_ID\n");  
		sql.append("   AND TVO1.ORDER_ID = "+orderId+"\n");  
		sql.append("   AND TVM.MATERIAL_CODE = '"+materialCode+"'");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		if(list.size() != 0){
			Map<String, Object> map = list.get(0);
			BigDecimal price = (BigDecimal)map.get("PRICE");
			return price.toString();
		}
		
		return null;
	}
	
	/**
	 * 对补充订单提报时收货方错误做控制
	 * @param areaId 当前提报订单的业务范围
	 * @param dealerId 界面上传回到action的收货方id
	 * @param flagNo 收货方需要控制修改的单号或id
	 * @return String:返回对应业务范围的经销商id以作为真正的收货方id
	 */
	public static String contralReceiver(String areaId, String dealerId, String flagNo) {
		String receiver = "" ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TD1.DEALER_ID\n");
		sql.append("  FROM TM_DEALER TD1, TM_DEALER_BUSINESS_AREA TDBA\n");  
		sql.append(" WHERE TD1.DEALER_ID = TDBA.DEALER_ID\n");  
		if(!"".equals(areaId)&&areaId!=null){
			sql.append("   AND TDBA.AREA_ID = ").append(areaId).append("\n");  
		}
		sql.append("   AND TD1.STATUS = ").append(Constant.STATUS_ENABLE).append("\n");  
		sql.append("   AND EXISTS (SELECT 1\n");  
		sql.append("          FROM TM_DEALER TD\n");  
		sql.append("         WHERE TD.COMPANY_ID = TD1.COMPANY_ID\n");  
		sql.append("           AND TD.DEALER_ID = ").append(dealerId).append(")\n");

		Map<String, Object> dealerMap = dao.pageQueryMap(sql.toString(), null, dao.getFunName()) ;
		
		if(null != dealerMap && dealerMap.size() > 0) {
			receiver = dealerMap.get("DEALER_ID").toString() ;
		} else {
			logger.info(flagNo + "中没有业务范围为： " + areaId + " 的经销商id！*==>") ;
			receiver = dealerId ;  // 当通过以上sql不能找到对应的经销商id，则说明该公司没有设置经销商与对应业务范围的关系，此时收货方默认为传入的dealerId值
		}
		
		if (!dealerId.equals(receiver)) {
			logger.info(flagNo + "收货方控制修改： " + dealerId + " *==> " + receiver) ;
		}
		
		return  receiver ;
	}
	
	public Map<String, Object> getBinCaiAvailable(String dealerId, Long fundTypeId) {
		List<Object> param = new  ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TTVA.ACCOUNT_ID,\n");
		sql.append("       (NVL(TTVA.BALANCE_AMOUNT, 0) - NVL(VW.FREEZE_AMOUNT, 0)) AVAILABLE_AMOUNT\n");  
		sql.append("  FROM TT_VS_ACCOUNT TTVA, TM_DEALER TMD, VW_FREEZE_SCGC VW\n");  
		sql.append(" WHERE TTVA.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TMD.ERP_CODE = VW.ERP_CODE(+)\n"); 
		sql.append("   AND TTVA.ACCOUNT_TYPE_ID = ?\n");
		sql.append("   AND TTVA.DEALER_ID = ?\n");
		
		param.add(fundTypeId) ;	// 资金类型为兵财
		param.add(dealerId) ;	//经销商id

		Map<String, Object> avaiMap = dao.pageQueryMap(sql.toString(), param, dao.getFunName()) ;
		
		return avaiMap ;
	}
	
	public Map<String, Object> getBinCaiAvailable(String dealerId, String fundTypeId, String reqId) {
		List<Object> param = new  ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT A.ACCOUNT_ID,\n");
		sql.append("       (A.AVAILABLE_AMOUNT + NVL(B.FREEZE_AMOUNT, 0)) AVAILABLE_AMOUNT,\n");  
		sql.append("       A.AVAILABLE_AMOUNT AVAILABLE_AMOUNT_1\n");  
		sql.append("  FROM (SELECT TTVA.ACCOUNT_ID,\n");  
		sql.append("               (NVL(TTVA.BALANCE_AMOUNT, 0) - NVL(VW.FREEZE_AMOUNT, 0)) AVAILABLE_AMOUNT\n");  
		sql.append("          FROM TT_VS_ACCOUNT TTVA, TM_DEALER TMD, VW_FREEZE_SCGC VW\n");  
		sql.append("         WHERE TTVA.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("           AND TMD.ERP_CODE = VW.ERP_CODE(+)\n"); 
		sql.append("   			AND TTVA.ACCOUNT_TYPE_ID = ?\n");
		sql.append("           AND TTVA.DEALER_ID = ?) A,\n");  
		sql.append("       (SELECT TVAF.ACCOUNT_ID, TVAF.FREEZE_AMOUNT\n");  
		sql.append("          FROM TT_VS_ACCOUNT_FREEZE TVAF\n");  
		sql.append("         WHERE TVAF.REQ_ID = ?\n");  
		sql.append("           AND TVAF.STATUS = ").append(Constant.ACCOUNT_FREEZE_STATUS_01).append(") B\n");
		sql.append(" WHERE A.ACCOUNT_ID = B.ACCOUNT_ID(+)\n");

		param.add(fundTypeId) ;	// 资金类型为兵财
		param.add(dealerId) ;	// 经销商id
		param.add(reqId) ;		// 发运请求id
		// param.add(dealerId) ;	// 经销商id

		Map<String, Object> avaiMap = dao.pageQueryMap(sql.toString(), param, dao.getFunName()) ;
		
		return avaiMap ;
	}
	
	public Map<String, Object> getBinCaiAvailable2(String acountId, String reqId) {
		List<Object> param = new  ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT A.ACCOUNT_ID,\n");
		sql.append("       (A.AVAILABLE_AMOUNT + NVL(B.FREEZE_AMOUNT, 0)) AVAILABLE_AMOUNT\n");  
		sql.append("  FROM (SELECT TVA.ACCOUNT_ID,\n");  
		sql.append("               (NVL(TVA.BALANCE_AMOUNT, 0) - NVL(VW.FREEZE_AMOUNT, 0)) AVAILABLE_AMOUNT\n");  
		sql.append("          FROM TT_VS_ACCOUNT TVA, TM_DEALER TMD, VW_FREEZE_SCGC VW\n");  
		sql.append("         WHERE TVA.DEALER_ID = TMD.DEALER_ID, TMD.ERP_CODE = VW.ERP_CODE(+)\n");  
		sql.append("           AND TVA.ACCOUNT_ID = ?) A,\n");  
		sql.append("       (SELECT TVAF.ACCOUNT_ID, TVAF.FREEZE_AMOUNT\n");  
		sql.append("          FROM TT_VS_ACCOUNT_FREEZE TVAF\n");  
		sql.append("         WHERE TVAF.REQ_ID = ?\n");  
		sql.append("           AND TVAF.STATUS = ").append(Constant.ACCOUNT_FREEZE_STATUS_01).append(") B\n");
		sql.append(" WHERE A.ACCOUNT_ID = B.ACCOUNT_ID\n");
		
		param.add(acountId) ;	//账户id
		param.add(reqId) ;	//发运请求

		Map<String, Object> avaiMap = dao.pageQueryMap(sql.toString(), param, dao.getFunName()) ;
		
		return avaiMap ;
	}
	
	public static String getOrderDlrId(String reqId) {
		List<Object> param = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TVO.ORDER_ORG_ID\n");
		sql.append("  FROM TT_VS_ORDER TVO, TT_VS_DLVRY_REQ TVDR\n");  
		sql.append(" WHERE TVO.ORDER_ID = TVDR.ORDER_ID\n");  
		sql.append("   AND TVDR.REQ_ID = ?\n");
		
		param.add(reqId) ;
		
		Map<String, Object> dlrMap = dao.pageQueryMap(sql.toString(), param, dao.getFunName()) ;
		
		return dlrMap.get("ORDER_ORG_ID").toString() ;
	}
	
	public static String getOrderDlrErpCode(String reqId) {
		List<Object> param = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TMD.ERP_CODE\n");
		sql.append("  FROM TT_VS_ORDER TVO, TT_VS_DLVRY_REQ TVDR, TM_DEALER TMD\n");  
		sql.append(" WHERE TVO.ORDER_ID = TVDR.ORDER_ID\n");  
		sql.append("   AND TVO.ORDER_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TVDR.REQ_ID = ?\n");
		
		param.add(reqId) ;
		
		Map<String, Object> dlrMap = dao.pageQueryMap(sql.toString(), param, dao.getFunName()) ;
		String erpCode = "" ;
		
		if (!CommonUtils.isNullMap(dlrMap)) {
			erpCode = dlrMap.get("ERP_CODE").toString() ;
		}
		
		return erpCode ;
	}
	
	public static String getAccountId(String reqId) {
		List<Object> param = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TVA.ACCOUNT_ID\n");
		sql.append("  FROM TT_VS_ORDER TVO, TT_VS_DLVRY_REQ TVDR, TT_VS_ACCOUNT TVA\n");  
		sql.append(" WHERE TVO.ORDER_ID = TVDR.ORDER_ID\n");  
		sql.append("   AND TVA.DEALER_ID = TVO.ORDER_ORG_ID\n");  
		sql.append("   AND TVA.ACCOUNT_CODE = ?\n");  
		sql.append("   AND TVDR.REQ_ID = ?\n");


		param.add("CSGC") ;
		param.add(reqId) ;
		
		Map<String, Object> accountMap = dao.pageQueryMap(sql.toString(), param, dao.getFunName()) ;
		
		return accountMap.get("ACCOUNT_ID").toString() ;
	} 
	
	public static String getOrderDlr(String orderId) {
		List<Object> param = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TVO.ORDER_ORG_ID\n");
		sql.append("  FROM TT_VS_ORDER TVO\n");  
		sql.append("   WHERE TVO.ORDER_ID = ?\n");
		
		param.add(orderId) ;
		
		Map<String, Object> dlrMap = dao.pageQueryMap(sql.toString(), param, dao.getFunName()) ;
		
		return dlrMap.get("ORDER_ORG_ID").toString() ;
	}
	
	public Map<String, Object> chkAmount(Map<String, String> map) {
		String dealerId = map.get("dealerId") ;
		String areaId = map.get("areaId") ;
		String year = map.get("year") ;
		String week = map.get("week") ;
		String groupId = map.get("groupId") ;
		String companyId = map.get("companyId") ;
		
		TmBusinessParaPO maxPara = getTmBusinessParaPO(Constant.QUTOA_MAX_WEIGHT_PARA, new Long(companyId)) ;
		
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer() ;
		
		sql.append("SELECT A.GROUP_ID, ROUND(A.QUOTA_AMT*" + maxPara.getParaValue() + "/100, 0) QUOTA_AMT, NVL(B.ORDER_AMOUNT, 0) ORDER_AMOUNT\n");
		sql.append("  FROM (SELECT TQD.GROUP_ID, SUM(TQD.QUOTA_AMT) QUOTA_AMT\n");  
		sql.append("          FROM TT_VS_QUOTA TQ, TT_VS_QUOTA_DETAIL TQD\n");  
		sql.append("         WHERE TQ.QUOTA_ID = TQD.QUOTA_ID\n");  
		sql.append("           AND TQ.DEALER_ID = ?\n");  
		params.add(dealerId) ;
		
		sql.append("           AND TQ.AREA_ID = ?\n");  
		params.add(areaId) ;
		
		sql.append("           AND TQ.STATUS = ?\n"); 
		params.add(Constant.QUOTA_STATUS_02) ;
		
		sql.append("           AND TQ.QUOTA_YEAR = ?\n"); 
		params.add(year) ;
		
		sql.append("           AND TQ.QUOTA_WEEK = ?\n");  
		params.add(week) ;
		
		sql.append("         GROUP BY TQD.GROUP_ID) A,\n");  
		sql.append("       (SELECT TVMG1.GROUP_ID, SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");  
		sql.append("          FROM TT_VS_ORDER              TSO,\n");  
		sql.append("               TT_VS_ORDER_DETAIL       TSOD,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG1\n");  
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");  
		sql.append("           AND TSOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");  
		sql.append("           AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");  
		sql.append("           AND TSO.ORDER_ORG_ID = ?\n");  
		params.add(dealerId) ;
		
		sql.append("           AND TSO.AREA_ID = ?\n");  
		params.add(areaId) ;
		
		sql.append("           AND TSO.ORDER_TYPE = ?\n");  
		params.add(Constant.ORDER_TYPE_01) ;
		
		sql.append("           AND TSO.ORDER_STATUS IN (?, ?)\n");  
		params.add(Constant.ORDER_COM_STATUS_03) ;
		params.add(Constant.ORDER_COM_STATUS_05) ;
		
		sql.append("           AND TSO.ORDER_YEAR = ?\n");  
		params.add(year) ;
		
		sql.append("           AND TSO.ORDER_WEEK = ?\n");  
		params.add(week) ;
		
		sql.append("         GROUP BY TVMG1.GROUP_ID) B\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND A.GROUP_ID = B.GROUP_ID(+)\n");  
		sql.append("   AND A.GROUP_ID = ?\n");
		params.add(groupId) ;

		Map<String, Object> groupMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;
		
		return groupMap ;
	}
	
	public static String chkOrder(Map<String, String> map) {
		String dealerId = map.get("dealerId").toString() ;
		String areaId = map.get("areaId").toString() ;
		String orderType = map.get("orderType").toString() ;
		String orderStatus = map.get("orderStatus").toString() ;
		String year = map.get("year").toString() ;
		String week = map.get("week").toString() ;
		
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer() ;
		
		sql.append("SELECT NVL(SUM(TSOD.ORDER_AMOUNT), 0) ORDER_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TSO,\n");  
		sql.append("       TT_VS_ORDER_DETAIL       TSOD\n");  
		sql.append(" WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");  
		sql.append("   AND TSO.ORDER_ORG_ID IN (").append(dealerId).append(")\n");  
		sql.append("   AND TSO.AREA_ID = ?\n");  
		params.add(areaId) ;
		
		sql.append("   AND TSO.ORDER_TYPE = ?\n");  
		params.add(orderType) ;
		
		sql.append("   AND TSO.ORDER_STATUS = ?\n");  
		params.add(orderStatus) ;
		
		sql.append("   AND TSO.ORDER_YEAR = ?\n");  
		params.add(year) ;
		
		sql.append("   AND TSO.ORDER_WEEK = ?\n");
		params.add(week) ;

		Map<String, Object> amountMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;
		
		if(!CommonUtils.isNullMap(amountMap)) {
			return amountMap.get("ORDER_AMOUNT").toString() ;
		}
		
		return "0" ;
	}
	
	public List<TmDealerPriceRelationPO> getDealerPriceRelation(String dealerId,String priceId){
		String sql = "SELECT * FROM vw_Tm_Dealer_Price_Relation WHERE Dealer_Id="+dealerId+"  AND Price_Id="+priceId;
		List<TmDealerPriceRelationPO> list = factory.select(sql, null, new POCallBack<TmDealerPriceRelationPO>(factory, TmDealerPriceRelationPO.class));// dao.select(TmDealerPriceRelationPO.class, sql,null);
		return list;
	}
	
	public static String viewOrderCount(String year,int month,String dealerId,String materialCode) {
	StringBuffer sql= new StringBuffer();
	sql.append("select sum(nvl(vo.check_amount,0)-nvl(vo.call_amount,0)) cou from tt_vs_order o ,tt_vs_order_detail vo,vw_org_dealer od,TM_VHCL_MATERIAL mg\n" );
	sql.append("where o.order_org_id = od.dealer_id\n" );
	sql.append("and od.dealer_id="+dealerId+"\n" );
	sql.append("and o.order_month='"+month+"'\n" );
	sql.append("and o.order_year='"+year+"'\n" );
	sql.append("and o.order_type="+Constant.ORDER_TYPE_01+"\n" );
	sql.append("and o.order_status = "+Constant.ORDER_COM_STATUS_05+"\n" );
	sql.append("and o.order_id = vo.order_id\n" );
	sql.append("and mg.material_code='"+materialCode+"'");
	sql.append(" and vo.material_id=mg.material_id\n");
	List<Map<String, Object>> amountMap = dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		if(amountMap.get(0).get("COU")!=null){
			return amountMap.get(0).get("COU").toString().equals("0")?"true" :"false";
		}
		else{
			return "true";
		}
	}
	
	public static String viewOrderCount(String year, String week, String dealerId, String materialCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(nvl(vo.check_amount,0)-nvl(vo.call_amount,0)) cou from tt_vs_order o ,tt_vs_order_detail vo,vw_org_dealer od,TM_VHCL_MATERIAL mg\n");
		sql.append("where o.order_org_id = od.dealer_id\n");
		sql.append("and od.dealer_id=" + dealerId + "\n");
		sql.append("and o.order_week='" + week + "'\n");
		sql.append("and o.order_year='" + year + "'\n");
		sql.append("and o.order_type=" + Constant.ORDER_TYPE_01 + "\n");
		sql.append("and o.order_status = " + Constant.ORDER_COM_STATUS_05 + "\n");
		sql.append("and o.order_id = vo.order_id\n");
		sql.append("and mg.material_code='" + materialCode + "'");
		sql.append(" and vo.material_id=mg.material_id\n");
		List<Map<String, Object>> amountMap = dao.pageQuery(sql.toString(), null, dao.getFunName());
		if (amountMap.get(0).get("COU") != null) {
			return amountMap.get(0).get("COU").toString().equals("0") ? "true" : "false";
		} else {
			return "true";
		}
	}
	
	public static String viewOrderCount(String year,String week,String dealerId,String materialCode, String sysPara) {
	//	DeliveryApply da = new DeliveryApply() ;
		
		StringBuffer sql= new StringBuffer("\n");
		/*sql.append("select sum(nvl(vo.check_amount,0)-nvl(vo.call_amount,0)) cou from tt_vs_order o ,tt_vs_order_detail vo,vw_org_dealer od,TM_VHCL_MATERIAL mg\n" );
		sql.append("where o.order_org_id = od.dealer_id\n" );
		sql.append("and od.dealer_id="+dealerId+"\n" );
		
		if(!CommonUtils.isNullString(week)) {
			sql.append("and o.order_week='"+week+"'\n" );
		}
		
		if(!CommonUtils.isNullString(year)) {
			sql.append("and o.order_year='"+year+"'\n" );
		}
		
		sql.append("and o.order_type="+Constant.ORDER_TYPE_01+"\n" );
		sql.append("and o.order_status = "+Constant.ORDER_COM_STATUS_05+"\n" );
		sql.append("and o.order_id = vo.order_id\n" );
		sql.append("and mg.material_code='"+materialCode+"'");
		sql.append(" and vo.material_id=mg.material_id\n");*/
		
		sql.append("select nvl(tvo.cou - nvl(ttvo.call_amount, 0), 0) cou\n") ;
		sql.append("  from (select o.order_id,\n") ;
		sql.append("               vo.material_id,\n") ;
		sql.append("               nvl(sum(nvl(vo.check_amount, 0) - nvl(vo.call_amount, 0)), 0) cou\n") ;
		sql.append("          from tt_vs_order        o,\n") ;
		sql.append("               tt_vs_order_detail vo,\n") ;
		sql.append("               vw_org_dealer      od,\n") ;
		sql.append("               TM_VHCL_MATERIAL   mg\n") ;
		sql.append("         where o.order_org_id = od.dealer_id\n") ;
		sql.append("           and vo.material_id = mg.material_id\n") ;
		sql.append("           and o.order_id = vo.order_id\n") ;
		
		sql.append("and od.dealer_id="+dealerId+"\n" );
		
		if(Constant.COMPANY_CODE_CVS.equals(sysPara)) {
			if(!CommonUtils.isNullString(week)) {
				sql.append("and o.order_week='"+week+"'\n" );
			}
		}
		
	/*	if(Constant.COMPANY_CODE_JC.equals(sysPara)) {
			sql.append("and o.order_year || decode(length(o.order_week), 2, '' || o.order_week, '0' || o.order_week) <='"+year+ da.addZero(week, 2, 2) +"'\n" );
		}*/
		
		if(Constant.COMPANY_CODE_CVS.equals(sysPara)) {
			if(!CommonUtils.isNullString(year)) {
				sql.append("and o.order_year='"+year+"'\n" );
			}
		}
		if(Constant.COMPANY_CODE_JC.equals(sysPara)) {
				sql.append("and o.order_year>=2013\n" );
		}
		
		sql.append("and o.order_type="+Constant.ORDER_TYPE_01+"\n" );
		sql.append("and o.order_status = "+Constant.ORDER_COM_STATUS_05+"\n" );
		sql.append("and mg.material_code='"+materialCode+"'");
		sql.append("         group by o.order_id, vo.material_id) tvo,\n") ;
		sql.append("       (select tvo.old_order_id,\n") ;
		sql.append("               tvod.material_id,\n") ;
		sql.append("               nvl(sum(tvod.call_amount), 0) call_amount\n") ;
		sql.append("          from tt_vs_order        tvo,\n") ;
		sql.append("               tt_vs_order        otvo,\n") ;
		sql.append("               tt_vs_order_detail tvod,\n") ;
		sql.append("               vw_org_dealer      od,\n") ;
		sql.append("               tm_vhcl_material   tvm\n") ;
		sql.append("         where tvo.order_id = tvod.order_id\n") ;
		sql.append("           and tvo.old_order_id = otvo.order_id\n") ;
		sql.append("           and tvod.material_id = tvm.material_id\n") ;
		sql.append("           and tvo.order_org_id = od.dealer_id\n") ;
		
		sql.append("and od.dealer_id="+dealerId+"\n" );
		
		/*if(!CommonUtils.isNullString(week)) {
			sql.append("and tvo.order_week='"+week+"'\n" );
		}*/
		
		/*if(!CommonUtils.isNullString(year)) {
			sql.append("and tvo.order_year='"+year+"'\n" );
		}*/
		
		sql.append("and tvo.order_type="+Constant.ORDER_TYPE_02+"\n" );
		sql.append("and otvo.order_type = "+Constant.ORDER_TYPE_01+"\n" );
		sql.append("and tvm.material_code='"+materialCode+"'");
		
		sql.append("           and tvo.old_order_id >= 1\n") ;
		sql.append("         group by tvo.old_order_id, tvod.material_id) ttvo\n") ;
		sql.append(" where tvo.order_id = ttvo.old_order_id(+)\n") ;
		sql.append("   and tvo.material_id = ttvo.material_id(+)") ;

		
		List<Map<String, Object>> amountMap = dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
			if(!CommonUtils.isNullList(amountMap)) {
				if(amountMap.get(0).get("COU")!=null){
					return amountMap.get(0).get("COU").toString().equals("0")?"true" :"false";
				}
				else{
					return "true";
				}
			} else {
				return "true";
			}
		}
	
	public Map<String, Object> getMaxWeek(Map<String, String> map) {
		String year = map.get("year") ;
		
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select max(to_number(set_week)) set_week\n");
		sql.append("  from tm_date_set\n");  
		sql.append(" where 1 = 1\n");  

		if(null != year && !"".equals(year)) {
			sql.append("   and set_year = ").append(year).append("\n");
		}
		
		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}

	public Map<String, Object> getDayByWeek(Map<String, String> map) {
		String year = map.get("year") ;
		String week = map.get("week") ;
		
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select max(set_date) maxdate, min(set_date) mindate\n");
		sql.append("  from tm_date_set\n");  
		sql.append(" where 1 = 1\n");  

		if(null != year && !"".equals(year)) {
			sql.append("   and set_year = ").append(year).append("\n");
		}

		if(null != week && !"".equals(week)) {
			sql.append("   and set_week = ").append(week).append("\n");
		}

		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	
	public String getWarehouseFlag(String orderId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select dohv.warehouse_flag\n");
		sql.append("  from dms_orderhead_v dohv\n");  
		sql.append(" where dohv.doc_id = ").append(orderId).append("\n");  
		sql.append(" order by rowid desc\n");

		List<Map<String, Object>> list = super.pageQuery(sql.toString(), null, super.getFunName()) ;
		
		return list.get(0).get("WAREHOUSE_FLAG").toString() ; 
	}
	
	public Map<String, String> insertNewReq(TtVsDlvryReqPO oReq, TtVsDlvryReqPO noReq, List olist, List nolist, Map<String, String> map) {
		/*String fundTypeId = map.get("fundTypeId") ;
		String userId = map.get("userId") ;
		String priceId = map.get("priceId") ;
		String warehouseId = map.get("warehouseId") ;
		String dlvryReqNo = map.get("dlvryReqNo") ;
		String deliveryType = map.get("deliveryType") ;
		String reqRemark = map.get("reqRemark") ;
		
		String reqTotalPrice = map.get("reqTotalPrice") ;*/
		
		Long reqId = Long.parseLong(SequenceManager.getSequence("")) ;
		int tAmount = 0 ;
		BigDecimal tDiscount = new BigDecimal(0) ;
		BigDecimal tPrice = new BigDecimal(0) ;
		
		String flag = "0" ;
		
		Map<String, String> retMap = new HashMap<String, String>() ;
		
		int osize = olist.size() ;
		
		for(int i=0; i<osize; i++) {
			TtVsDlvryReqDtlPO oTvdrd = (TtVsDlvryReqDtlPO)olist.get(i) ;
			
			int nosize = nolist.size() ;
			for(int j=0; j<nosize; j++) {
				TtVsDlvryReqDtlPO noTvdrd = (TtVsDlvryReqDtlPO)nolist.get(j) ;
				
				if(oTvdrd.getMaterialId().doubleValue() == noTvdrd.getMaterialId().doubleValue() && oTvdrd.getReqAmount().intValue() != noTvdrd.getReserveAmount().intValue()) {
					Long dtlId = Long.parseLong(SequenceManager.getSequence("")) ;
					
					TtVsDlvryReqDtlPO nTvdrd = new TtVsDlvryReqDtlPO() ;
					nTvdrd.setDetailId(dtlId) ;
					nTvdrd.setReqId(reqId) ;
					nTvdrd.setOrderDetailId(oTvdrd.getOrderDetailId()) ;
					nTvdrd.setMaterialId(oTvdrd.getMaterialId()) ;
					nTvdrd.setPatchNo(oTvdrd.getPatchNo()) ;
					nTvdrd.setReqAmount(oTvdrd.getReqAmount() - noTvdrd.getReserveAmount()) ;
					tAmount += oTvdrd.getReqAmount() - noTvdrd.getReserveAmount() ;
					
					nTvdrd.setVer(0) ;
					nTvdrd.setCreateBy(noTvdrd.getUpdateBy()) ;
					nTvdrd.setCreateDate(noTvdrd.getUpdateDate()) ;
					nTvdrd.setSinglePrice(noTvdrd.getSinglePrice()) ;
					nTvdrd.setDiscountRate(oTvdrd.getDiscountRate()) ;
					
					BigDecimal disSinPrice = new BigDecimal(nTvdrd.getSinglePrice()).subtract(new BigDecimal(nTvdrd.getDiscountRate()).divide(new BigDecimal(100)).multiply(new BigDecimal(nTvdrd.getSinglePrice()))) ;
					nTvdrd.setDiscountSPrice(disSinPrice.doubleValue()) ;
					
					nTvdrd.setDiscountPrice(nTvdrd.getSinglePrice() - disSinPrice.doubleValue()) ;
					tDiscount = tDiscount.add(new BigDecimal(nTvdrd.getSinglePrice()).subtract(disSinPrice)) ;
					
					BigDecimal totPrice = new BigDecimal(nTvdrd.getReqAmount()).multiply(new BigDecimal(nTvdrd.getDiscountSPrice())) ;
					nTvdrd.setTotalPrice(totPrice.doubleValue()) ;
					tPrice = tPrice.add(totPrice) ;
					
					dao.insert(nTvdrd) ;
					
					break ;
				}
			}
		}
		
		TtVsDlvryReqDtlPO flagPo = new TtVsDlvryReqDtlPO() ;
		flagPo.setReqId(reqId) ;
		
		List flagList = dao.select(flagPo) ;
		
		if(!CommonUtils.isNullList(flagList)) {
			int totalAmount = 0 ;
			int size = flagList.size() ;
			
			for(int i=0; i<size; i++) {
				totalAmount += ((TtVsDlvryReqDtlPO)flagList.get(i)).getReqAmount().intValue() ;
			}
			
			TtVsDlvryReqPO nReq = new TtVsDlvryReqPO() ;
			nReq.setReqId(reqId) ;
			nReq.setAreaId(oReq.getAreaId()) ;
			nReq.setOrderId(oReq.getOrderId()) ;
			nReq.setFundType(noReq.getFundType()) ;
			nReq.setDeliveryType(noReq.getDeliveryType()) ;
			nReq.setAddressId(oReq.getAddressId()) ;
			nReq.setReqDate(oReq.getReqDate()) ;
			nReq.setReqTotalAmount(totalAmount) ; //		nReq.set
			nReq.setReqExecStatus(oReq.getReqExecStatus()) ;
			nReq.setVer(0) ;
			nReq.setCreateBy(noReq.getUpdateBy());
			nReq.setCreateDate(noReq.getUpdateDate()) ;
			nReq.setFleetId(oReq.getFleetId()) ;
			nReq.setFleetAddress(oReq.getFleetAddress()) ;
			nReq.setPriceId(noReq.getPriceId()) ;
			nReq.setOtherPriceReason(oReq.getOtherPriceReason()) ;
			nReq.setReceiver(oReq.getReceiver()) ;
			nReq.setLinkMan(oReq.getLinkMan()) ;
			nReq.setTel(oReq.getTel()) ;
			nReq.setDiscount(tDiscount.doubleValue()) ; //
			nReq.setWarehouseId(noReq.getWarehouseId()) ;
			nReq.setReqStatus(Constant.ORDER_REQ_STATUS_01) ;
			nReq.setReqTotalPrice(tPrice.doubleValue()) ; //
			nReq.setReserveTotalAmount(0) ;
			nReq.setIsFleet(oReq.getIsFleet()) ;
			
			Map<String,String> codeMap  = GetOrderNOUtil.getAreaShortcode(nReq.getAreaId().toString());
			String areaCode = codeMap.get("AREA_SHORTCODE");
			
			StringBuffer sql = new StringBuffer("\n") ;
	
			sql.append("select decode(tvdr.call_leavel, 10851002, tvdr.order_dealer_id, tvo.order_org_id) order_dealer_id\n") ;
			sql.append("  from tt_vs_order tvo, tt_vs_dlvry_req tvdr\n") ;
			sql.append(" where tvo.order_id = tvdr.order_id\n") ;
			sql.append("   and tvdr.req_id = ").append(oReq.getReqId()).append("\n") ;
			
			Map<String, Object> resultMap = dao.pageQueryMap(sql.toString(), null, super.getFunName()) ;
			
			TmDealerPO tmDealerPO = new TmDealerPO();
			tmDealerPO.setDealerId(new Long(resultMap.get("ORDER_DEALER_ID").toString()));
			List<PO> dealerList = dao.select(tmDealerPO);
			
			String dealerCode = ((TmDealerPO)dealerList.get(0)).getDealerCode();
			
			String dlvryReqNO = GetOrderNOUtil.getDlvryReqNO(areaCode, "D", dealerCode);
			
			nReq.setDlvryReqNo(dlvryReqNO) ;
			nReq.setOrderDealerId(oReq.getOrderDealerId()) ;
			nReq.setBilDealerId(oReq.getBilDealerId()) ;
			nReq.setCallLeavel(oReq.getCallLeavel()) ;
			nReq.setReqRemark(oReq.getReqRemark()) ;
			
			retMap.put("reqId", reqId.toString()) ;
			retMap.put("tPrice", tPrice.toString()) ;
			retMap.put("tDiscount", tDiscount.toString()) ;
			
			super.insert(nReq) ;
			
			flag = "1" ;
		}
		
		retMap.put("flag", flag) ;
		
		return retMap ;
	}
	
	/**
	 * 获得常规订单提报物料列表
	 * 
	 * @param orderYear
	 * @param orderWeek
	 * @param dealerId
	 * @param groupId
	 * @return
	 */
	public List<Map<String, Object>> getGeneralReportMaterialList(String orderYear, String orderWeek, String dealerId, String groupId) {
		TmDealerPO tmp = new TmDealerPO();
		tmp.setDealerId(new Long(dealerId));
		tmp = (TmDealerPO) dao.select(tmp).get(0);
		String dealerCode = tmp.getDealerCode();
		TmVhclMaterialGroupPO tvmg = new TmVhclMaterialGroupPO();
		tvmg.setGroupId(new Long(groupId));
		tvmg = (TmVhclMaterialGroupPO) dao.select(tvmg).get(0);
		String groupCode = tvmg.getGroupCode();
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVM.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TVM.COLOR_NAME,\n");
		sql.append("       NVL(F_GET_MATPRICE('" + dealerCode + "', '" + groupCode + "'),0) PRICE,\n");
		sql.append("       NVL(TMP.ORDER_AMOUNT, 0) ORDER_AMOUNT\n");
		sql.append("  FROM TM_VHCL_MATERIAL TVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       (SELECT TVM.MATERIAL_ID, TSOD.SINGLE_PRICE PRICE, TSOD.ORDER_AMOUNT\n");
		sql.append("          FROM TM_VHCL_MATERIAL         TVM,\n");
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("               TT_VS_ORDER              TSO,\n");
		sql.append("               TT_VS_ORDER_DETAIL       TSOD\n");
		sql.append("         WHERE TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("           AND TVM.MATERIAL_ID = TSOD.MATERIAL_ID\n");
		sql.append("           AND TSO.ORDER_ID = TSOD.ORDER_ID\n");
		sql.append("           AND TVM.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_01 + "\n");
		sql.append("           AND TSO.ORDER_STATUS = " + Constant.ORDER_COM_STATUS_01 + "\n");
		sql.append("           AND TSO.ORDER_YEAR = " + orderYear + "\n");
		sql.append("           AND TSO.ORDER_WEEK = " + orderWeek + "\n");
		sql.append("           AND TSO.ORDER_ORG_ID = " + dealerId + "\n");
		sql.append("           AND TVMGR.GROUP_ID = " + groupId + ") TMP\n");
		sql.append(" WHERE TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TMP.MATERIAL_ID(+)\n");
		sql.append("   AND TVM.STATUS = " + Constant.STATUS_ENABLE + "\n");
		// sql.append(" AND TVM.ORDER_FLAG = " +
		// Constant.NASTY_ORDER_REPORT_TYPE_01 + "\n");
		sql.append("   AND TVM.NORMAL_ORDER_FLAG = " + Constant.NASTY_ORDER_REPORT_TYPE_01 + "\n");// 常规订单提报
		sql.append("   AND TVMGR.GROUP_ID = " + groupId + "\n");
		sql.append("  ORDER BY TVM.MATERIAL_ID ASC");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getGeneralReportMaterialList");
		return list;
	}
	
	/**
	 * 获取各个经销商冻结金额
	 * 
	 * 
	 * @param dealerId
	 * @param fundTypeId
	 * @return
	 */
	public BigDecimal getOutstandingAccount(String dealerId,String fundTypeId){
		BigDecimal resust = new BigDecimal(0);
		if(!"".equals(fundTypeId)){
			StringBuffer sqlStr= new StringBuffer();
			/*sqlStr.append("SELECT NVL(SUM((NVL(L.REQ_AMOUNT, 0) - NVL(L.BILL_AMOUNT, 0)) * \n" );
			sqlStr.append("           NVL(L.SINGLE_PRICE, 0)),0) OUTSTANDING_ACCOUNT \n" );
			sqlStr.append("  FROM TT_VS_DLVRY_REQ_DTL L\n" );
			sqlStr.append(" WHERE L.REQ_ID IN (SELECT R.REQ_ID\n" );
			sqlStr.append("                      FROM TT_VS_DLVRY_REQ R,tt_vs_order tvo \n" );
			sqlStr.append("                     WHERE tvo.order_org_id = "+dealerId+"\n" );
			sqlStr.append("                       and tvo.order_id=r.order_id \n" );
			sqlStr.append("                       AND R.REQ_STATUS = "+Constant.ORDER_REQ_STATUS_FINAL+"  \n");//终审完成
			sqlStr.append("                       AND R.FUND_TYPE =" +fundTypeId+")\n");*/
			sqlStr.append("SELECT frozen_amount  as OUTSTANDING_ACCOUNT FROM VW_ACCOUNT_FROZEN_AMOUNT VAFA\n");//各个经销商各个账户锁定余额
			sqlStr.append("       WHERE VAFA.DEALER_ID= "+dealerId+"\n");
			sqlStr.append("       AND FUND_TYPE =" +fundTypeId+"\n");
			List<Map<String, Object>> list = super.pageQuery(sqlStr.toString(), null, super.getFunName());
			if(list != null && list.size() > 0){
				resust = new BigDecimal(list.get(0).get("OUTSTANDING_ACCOUNT").toString());
			}
		}
		return resust;
	}
	
	/**
	 * 返利可用余额
	 * @param dealerId
	 * @return
	 */
	public BigDecimal getRebateAccount(String dealerId){
		//String sql = "SELECT rebate_discount,rebate_amount FROM tt_vs_account_rebate WHERE Dealer_Id="+dealerId;
		//String sql="select balance_amount as rebate_amount from tt_vs_account tva,tt_vs_account_type tvat where tva.account_type_id=tvat.type_id and account_code='2000'";
		BigDecimal resust = new BigDecimal(0);
		StringBuffer sql= new StringBuffer();
		sql.append(" select nvl(tva.balance_amount,0) - nvl(loc.rebate_frozen_amount,0) REBATE_AMOUNT \n");
		sql.append(" from tt_vs_account tva, \n");
		sql.append("  tt_vs_account_type tvat, \n");
		/*sql.append(" (select tvdr.dealer_id, \n");
		sql.append("   sum(decode(tvdr.is_rebate, \n");
		sql.append("      10041001, \n");
		sql.append("         (decode(tvdr.is_rebate_bill, \n");
		sql.append("                 10041001, \n");
		sql.append("                   0, \n");
		sql.append("                     tvdr.rebate_amount)), \n");
		sql.append("                0)) loc_Amount \n");
		sql.append("   from tt_vs_dlvry_req tvdr \n");
		sql.append("   where tvdr.req_status in (11571003, 11571014, 11571016,11571017) \n");*/
		sql.append("       vw_rebate_frozen_amount loc \n");
		sql.append("  where tva.account_type_id = tvat.type_id \n");
		sql.append("  and tva.dealer_id = loc.dealer_id(+) \n");
		sql.append("  and tvat.type_code = '2001' \n");
		sql.append("   and tva.dealer_id ="+dealerId);
		List<Map<String, Object>> list = super.pageQuery(sql.toString(), null, super.getFunName());
		if(list != null && list.size() > 0){
			resust = new BigDecimal(list.get(0).get("REBATE_AMOUNT").toString());
		}
		return resust;
	}
	
	/*
	 * 返利百分比
	 */
    public Double getRebateDiscount(){
    	String sql="select para_value as rebate_Discount from TM_BUSINESS_PARA where type_code='2000' ";
    	Double rebateDiscount = new Double(0);
    	List<Map<String, Object>> list = super.pageQuery(sql.toString(), null, super.getFunName());
		if(list != null && list.size() > 0){
			rebateDiscount = new Double(list.get(0).get("REBATE_DISCOUNT").toString());
		}
		return rebateDiscount;
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
}
