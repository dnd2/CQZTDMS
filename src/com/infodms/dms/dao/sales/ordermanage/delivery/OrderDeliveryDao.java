/**********************************************************************
* <pre>
* FILE : OrderDeliveryDao.java
* CLASS : OrderDeliveryDao
* AUTHOR : 
* FUNCTION : 订单发运
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-21|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.dao.sales.ordermanage.delivery;

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

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * @Title: 订单发运DAO
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-21
 * @author
 * @mail   
 * @version 1.0
 * @remark 
 */
public class OrderDeliveryDao extends BaseDao {
	
	public static Logger logger = Logger.getLogger(OrderDeliveryDao.class);
	private static final OrderDeliveryDao dao = new OrderDeliveryDao ();
	public static final OrderDeliveryDao getInstance() {
		return dao;
	}
	private OrderDeliveryDao() {}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
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

	/**
	 * 常规订单周度列表
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getGeneralDateList(Long companyId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = getTmDateSetPO(new Date(), companyId);
		if (dateSet != null) {
			TmBusinessParaPO para1 = getTmBusinessParaPO(
					Constant.GENEREAL_ORDER_BEFORE_WEEK_PARA, new Long(
							companyId));// 常规订单提报提前周度参数
			TmBusinessParaPO para2 = getTmBusinessParaPO(
					Constant.GENEREAL_ORDER_WEEK_PARA, new Long(companyId));// 常规订单提报周度参数

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
			
			if (dateSet != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", dateSet.getSetYear() + "-"
						+ dateSet.getSetWeek());
				map.put("name", dateSet.getSetYear() + "年"
						+ dateSet.getSetWeek() + "周");
				map.put("date_start", date_start);
				map.put("date_end", date_end);
				list.add(map);

				for (int i = 1; i < Integer.parseInt(para2.getParaValue()); i++) {
					c.add(Calendar.DATE, 7);
					dateSet = getTmDateSetPO(c.getTime(), companyId);
					if (dateSet != null) {
						map = new HashMap<String, Object>();
						map.put("code", dateSet.getSetYear() + "-"
								+ dateSet.getSetWeek());
						map.put("name", dateSet.getSetYear() + "年"
								+ dateSet.getSetWeek() + "周");
						map.put("date_start", date_start);
						map.put("date_end", date_end);
						list.add(map);
					}
				}
			}
		}

		return list;
	}
	
	public List<Map<String, Object>> getGeneralDeliveryDateList_New(Long companyId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = getTmDateSetPO(new Date(), companyId);
		if (dateSet != null) {
			/*TmBusinessParaPO para1 = getTmBusinessParaPO(
					Constant.GENEREAL_ORDER_BEFORE_WEEK_PARA, new Long(
							companyId));// 常规订单提报提前周度参数
			TmBusinessParaPO para2 = getTmBusinessParaPO(
					Constant.GENEREAL_ORDER_WEEK_PARA, new Long(companyId));*/// 常规订单提报周度参数

			String currentYear = dateSet.getSetYear() ; //获取系统日历表中对应年份
			String currentWeek = dateSet.getSetWeek() ; //获取系统日历表中对应月份


			String date_start = null ;
			String date_end = null ;

			Map<String, String> map = new HashMap<String, String>() ;
			map.put("year", currentYear) ;
			map.put("week", currentWeek) ;
					
			OrderReportDao ord = OrderReportDao.getInstance();
			Map<String, Object> dateMap = ord.getDayByWeek(map) ;
					
			date_start = dateMap.get("MINDATE").toString().substring(0, 4) + "/" + dateMap.get("MINDATE").toString().substring(4, 6) + "/" + dateMap.get("MINDATE").toString().substring(6, 8) ;
			date_end = dateMap.get("MAXDATE").toString().substring(0, 4) + "/" + dateMap.get("MAXDATE").toString().substring(4, 6) + "/" + dateMap.get("MAXDATE").toString().substring(6, 8) ;

			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.put("code", currentYear + "-" + currentWeek);
			newMap.put("name", currentYear + "年" + currentWeek + "周");
			newMap.put("date_start", date_start);
			newMap.put("date_end", date_end);
			list.add(newMap);
		}
		return list;
	}
	/**
	 * 常规订单启票周度列表
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getGeneralDeliveryDateList(Long companyId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = getTmDateSetPO(new Date(), companyId);
		if (dateSet != null) {
			/*TmBusinessParaPO para1 = getTmBusinessParaPO(
					Constant.GENEREAL_ORDER_BEFORE_WEEK_PARA, new Long(
							companyId));// 常规订单提报提前周度参数
			TmBusinessParaPO para2 = getTmBusinessParaPO(
					Constant.GENEREAL_ORDER_WEEK_PARA, new Long(companyId));*/// 常规订单提报周度参数

			// 获得提报起始周
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, 0 /** Integer.parseInt(para1.getParaValue())*/);
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
				map.put("code", dateSet.getSetYear() + "-"
						+ dateSet.getSetWeek());
				map.put("name", dateSet.getSetYear() + "年"
						+ dateSet.getSetWeek() + "周");
				map.put("date_start", date_start);
				map.put("date_end", date_end);
				list.add(map);

				for (int i = 1; i < 1/*Integer.parseInt(para2.getParaValue())*/; i++) {
					c.add(Calendar.DATE, 7);
					dateSet = getTmDateSetPO(c.getTime(), companyId);
					if (dateSet != null) {
						map = new HashMap<String, Object>();
						map.put("code", dateSet.getSetYear() + "-"
								+ dateSet.getSetWeek());
						map.put("name", dateSet.getSetYear() + "年"
								+ dateSet.getSetWeek() + "周");
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
	 * 获得月度常规订单提报时间列表
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> getMonthGeneralDateList(Long companyId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = getTmDateSetPO(new Date(), companyId);
		if (dateSet != null) {
			TmBusinessParaPO para1 = getTmBusinessParaPO(
					Constant.MONTH_GENEREAL_ORDER_BEFORE_MONTH_PARA, new Long(
							companyId));// 月度常规订单提报提前月度参数
			TmBusinessParaPO para2 = getTmBusinessParaPO(
					Constant.MONTH_GENEREAL_ORDER_MONTH_PARA, new Long(companyId));// 月度常规订单提报月度参数

			int year = Integer.parseInt(dateSet.getSetYear());// 当前年
			int month = Integer.parseInt(dateSet.getSetMonth());// 当前月
			
			if(year != getNowYearOrMonth()[0]) {
				year = getNowYearOrMonth()[0] ;
			}
			if(month != getNowYearOrMonth()[1]) {
				month = getNowYearOrMonth()[1] ;
			}
			
			int beforeMonths = Integer.parseInt(para1.getParaValue());
			month = month + beforeMonths;
			if(month > 12){
				month -= 12;
				year += 1;
			}
			if(month < 0){
				month = 12 + month;
				year -= 1;
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", year + "-" + month);
			map.put("name", year + "年" + month + "月");
			list.add(map);
			
			for (int i = 1; i < Integer.parseInt(para2.getParaValue()); i++) {
				month += 1;
				if(month > 12){
					month -= 12;
					year += 1;
				}
				
				map = new HashMap<String, Object>();
				map.put("code", year + "-" + month);
				map.put("name", year + "年" + month + "月");
				list.add(map);
			}
			
		}

		return list;
	}
	
	/**
	 * 获得月度常规订单启票时间列表
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> getMonthMakeDateList(Long companyId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = getTmDateSetPO(new Date(), companyId);
		if (dateSet != null) {
			TmBusinessParaPO para1 = getTmBusinessParaPO(Constant.MONTH_MAKE_PARA, new Long(companyId));// 月度常规订单启票

			int year = Integer.parseInt(dateSet.getSetYear());// 当前年
			int month = Integer.parseInt(dateSet.getSetMonth());// 当前月
			int beforeMonths = Integer.parseInt(para1.getParaValue());
			month = month + beforeMonths;
			if(month > 12){
				month -= 12;
				year += 1;
			}
			if(month < 0){
				month = 12 + month;
				year -= 1;
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", year + "-" + month);
			map.put("name", year + "年" + month + "月");
			list.add(map);
		}

		return list;
	}
	
	public List<Map<String, Object>> getWarehouseEnable_Aim(Long companyId, String areaIds)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DISTINCT TMW.WAREHOUSE_ID, TMW.WAREHOUSE_NAME\n" );
		sql.append("  FROM TM_WAREHOUSE TMW, TM_BUSINESS_AREA TBA\n" );
		sql.append(" WHERE TMW.ENTITY_CODE = TBA.ERP_CODE\n" );
		sql.append("  AND TMW.STATUS = "+Constant.STATUS_ENABLE+"\n");
		sql.append("  AND TMW.WAREHOUSE_TYPE = "+Constant.WAREHOUSE_TYPE_01+"\n");
		sql.append("  AND TMW.COMPANY_ID = "+companyId+"\n");
		//sql.append("  AND TMW.WAREHOUSE_LEVEL = "+Constant.WAREHOUSE_LEVEL_02+"\n");
		sql.append("  AND TBA.AREA_ID IN ("+areaIds+")\n");

		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
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
		for(int i = -1 ; i <= 1 ; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear", year+i);
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
		for(int i = 1 ; i <= 53 ; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderWeek", i);
			list.add(map);
		}
		return list;
	}
	/**
	 * Function         : 常规订单发运申请查询
	 * @param           : 订单年周
	 * @param           : 订单号码
	 * @param           : 车系
	 * @return          : 满足条件的常规订单信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-24
	 */
	public List<Map<String, Object>> getDeliveryApplyList(String orderYear,String orderWeek,String areaId,
			String dealerId,Long oemCompanyId, String orderNO) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT distinct TVM.MATERIAL_ID, ---物料ID\n" );
		sql.append("       TSO.ORDER_ID, ---订单ID\n" );
		sql.append("       TSO.AREA_ID, ---业务范围ID\n" );
		sql.append("       TSOD.DETAIL_ID, ---订单明细ID\n" );
		sql.append("       TSO.ORDER_NO, ---订单号码\n" );
		sql.append("       VMG.SERIES_NAME,---车系\n");
		sql.append("       TVM.MATERIAL_CODE, ---物料编码\n" );
		sql.append("       TVM.MATERIAL_NAME, ---物料名称\n" );
		sql.append("       TVM.COLOR_NAME, ---颜色\n" );
		sql.append("       TSOD.SINGLE_PRICE, ---物料单价\n" );
		sql.append("       TSOD.ORDER_AMOUNT, ---订单数量\n" );
		sql.append("       NVL(TSOD.CHECK_AMOUNT, 0) CHECK_AMOUNT, ---已审核数量\n" );
		
		sql.append("                NVL(TSOD.CALL_AMOUNT, 0) +\n") ;
		sql.append("                nvl((select sum(tvod.call_amount)\n") ;
		sql.append("                      from tt_vs_order tvo, tt_vs_order_detail tvod\n") ;
		sql.append("                     where tvo.order_id = tvod.order_id\n") ;
		sql.append("                       and tvo.old_order_id = tso.order_id\n") ;
		sql.append("                       and tvod.material_id = tsod.material_id\n") ;
		sql.append("                       and tvo.order_type = 10201002),\n") ;
		sql.append("                    0) CALL_AMOUNT, ---已申请数量") ;

		sql.append("	   TSOD.DISCOUNT_RATE,\n");
		sql.append("       TSOD.DISCOUNT_S_PRICE,\n");  
		sql.append("       TSOD.DISCOUNT_PRICE\n");

		sql.append("  FROM TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		sql.append("       VW_MATERIAL_GROUP        VMG,\n" );
		sql.append("       TM_AREA_GROUP            TAG,\n" );
		sql.append("       TT_VS_ORDER              TSO,\n" );
		sql.append("       TT_VS_ORDER_DETAIL       TSOD\n" );
		sql.append(" WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n" );
		sql.append("   AND TSOD.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append("   AND TVMGR.GROUP_ID = VMG.PACKAGE_ID\n" );
		sql.append("   AND (VMG.SERIES_ID = TAG.MATERIAL_GROUP_ID OR\n");
		sql.append("       VMG.MODEL_ID = TAG.MATERIAL_GROUP_ID OR\n");  
		sql.append("       VMG.PACKAGE_ID = TAG.MATERIAL_GROUP_ID)\n");
		sql.append("   AND TAG.AREA_ID = TSO.AREA_ID\n" );
		sql.append("   AND TSO.ORDER_TYPE = "+Constant.ORDER_TYPE_01+"\n" );
		sql.append("   AND TSO.ORDER_STATUS = "+Constant.ORDER_STATUS_05+"\n" );
		sql.append("   AND TSO.COMPANY_ID = "+oemCompanyId+"\n" );
		sql.append("   AND TSO.ORDER_ORG_ID IN("+dealerId+")\n" );
		if(!"".equals(orderYear)&&orderYear!=null){
			sql.append(" AND TSO.ORDER_YEAR = '"+orderYear+"'\n");
		}
		if(!"".equals(orderWeek)&&orderWeek!=null){
			sql.append(" AND TSO.ORDER_WEEK = '"+orderWeek+"'\n");
		}
		if(!"-1".equals(areaId)&&!"".equals(areaId)&&areaId!=null){
			sql.append(" AND TSO.AREA_ID ="+areaId+"\n");
		}
		if(!CommonUtils.isNullString(orderNO)) {
			sql.append(" AND TSO.ORDER_NO = '" + orderNO + "'\n");
		}
		sql.append("	AND TSOD.CHECK_AMOUNT > 0\n");
		sql.append("	AND NVL(TSOD.CHECK_AMOUNT, 0) <> NVL(TSOD.CALL_AMOUNT, 0)\n");

		sql.append("    ORDER BY ORDER_ID\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 月度常规订单启票列表
	 * @param orderYear
	 * @param orderMonth
	 * @param areaId
	 * @param dealerId
	 * @param orderNo
	 * @param oemCompanyId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMonthGeneralOrderCallList(String orderYear,String orderMonth,String areaId,
			String firstDealer, String dealerId, String orderNo, Long oemCompanyId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVM.MATERIAL_ID, ---物料ID\n" );
		sql.append("       TSO.ORDER_ID, ---订单ID\n" );
		sql.append("       TSO.AREA_ID, ---业务范围ID\n" );
		sql.append("       TSOD.DETAIL_ID, ---订单明细ID\n" );
		sql.append("       TSO.ORDER_NO, ---订单号码\n" );
		sql.append("       (SELECT G.GROUP_NAME\n");
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP G\n");  
		sql.append("         WHERE G.GROUP_LEVEL = 2\n");  
		sql.append("         START WITH G.GROUP_ID = TVMG.GROUP_ID\n");  
		sql.append("        CONNECT BY G.GROUP_ID = PRIOR G.PARENT_GROUP_ID) SERIES_NAME, ---车系\n");
		
		sql.append("       (CASE\n");  
		sql.append("         WHEN NVL(VVOR.RESOURCE_AMOUNT, 0) > 0 THEN\n");  
		sql.append("          '有'\n");  
		sql.append("         ELSE\n");  
		sql.append("          '无'\n");  
		sql.append("       END) RAMOUNT,\n");  

		sql.append("       TVM.MATERIAL_CODE, ---物料编码\n" );
		sql.append("       TVM.MATERIAL_NAME, ---物料名称\n" );
		sql.append("       TVM.COLOR_NAME, ---颜色\n" );
		sql.append("       TSOD.SINGLE_PRICE, ---物料单价\n" );
		sql.append("       TSOD.ORDER_AMOUNT, ---订单数量\n" );
		sql.append("       NVL(TSOD.CHECK_AMOUNT, 0) CHECK_AMOUNT, ---已审核数量\n" );
		sql.append("       NVL(TSOD.CALL_AMOUNT, 0) CALL_AMOUNT ,---已申请数量\n");
		sql.append("(SELECT NVL(SUM(TVDRD.REQ_AMOUNT), 0) FROM TT_VS_DLVRY_REQ_DTL TVDRD, TT_VS_DLVRY_REQ TVDR WHERE TVDRD.REQ_ID = TVDR.REQ_ID AND TVDRD.ORDER_DETAIL_ID = TSOD.DETAIL_ID AND TVDR.REQ_STATUS <> " + Constant.ORDER_REQ_STATUS_07 + " AND TVDR.ORDER_DEALER_ID = " + dealerId + ") REQ_AMOUNT, --已提报数量\n");
		sql.append("(SELECT NVL(SUM(TVDRD.REQ_AMOUNT), 0) FROM TT_VS_DLVRY_REQ_DTL TVDRD, TT_VS_DLVRY_REQ TVDR WHERE TVDRD.REQ_ID = TVDR.REQ_ID AND TVDRD.ORDER_DETAIL_ID = TSOD.DETAIL_ID AND TVDR.REQ_STATUS = " + Constant.ORDER_REQ_STATUS_YSH + " AND TVDR.ORDER_DEALER_ID = " + dealerId + ") NC_AMOUNT, --一级未审数量\n");
		sql.append("	   TSOD.DISCOUNT_RATE,\n");
		sql.append("       TSOD.DISCOUNT_S_PRICE,\n");  
		sql.append("       TSOD.DISCOUNT_PRICE\n");

		sql.append("  FROM TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		sql.append("       TT_VS_ORDER              TSO,\n" );
		sql.append("       TT_VS_ORDER_DETAIL       TSOD,\n" );
		sql.append("       VW_VS_ORDER_RESOURCE     VVOR\n");  
		sql.append(" WHERE TVM.MATERIAL_ID = TSOD.MATERIAL_ID\n" );
		sql.append("   AND TSOD.ORDER_ID = TSO.ORDER_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n" );
		sql.append("   AND TSOD.MATERIAL_ID = VVOR.MATERIAL_ID(+)\n" );
		sql.append("   AND TSO.ORDER_TYPE = "+Constant.ORDER_TYPE_01+"\n" );
		sql.append("   AND TSO.ORDER_STATUS = "+Constant.ORDER_STATUS_05+"\n" );
		sql.append("   AND TSO.COMPANY_ID = "+oemCompanyId+"\n" );
		sql.append("   AND TSO.ORDER_ORG_ID IN("+firstDealer+")\n" );
		if(orderYear != null && !"".equals(orderYear)){
			sql.append(" AND TSO.ORDER_YEAR = '"+orderYear+"'\n");
		}
		if(orderMonth != null && !"".equals(orderMonth)){
			sql.append(" AND TSO.ORDER_MONTH = '"+orderMonth+"'\n");
		}
		if(areaId != null && !"".equals(areaId)){
			sql.append(" AND TSO.AREA_ID = "+areaId+"\n");
		}
		if(orderNo != null && !"".equals(orderNo)){
			sql.append(" AND TSO.ORDER_NO LIKE '%"+orderNo+"%'\n");
		}
		sql.append("	AND TSOD.CHECK_AMOUNT > 0\n");
		sql.append("	AND NVL(TSOD.CHECK_AMOUNT, 0) <> NVL(TSOD.CALL_AMOUNT, 0)\n");

		sql.append("    ORDER BY ORDER_ID\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 月度常规订单启票列表
	 * @param orderYear
	 * @param orderMonth
	 * @param areaId
	 * @param dealerId
	 * @param orderNo
	 * @param oemCompanyId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getWeekGeneralOrderCallList(String orderYear,String orderWeek,String areaId,
			String firstDealer, String dealerId, String orderNo, Long oemCompanyId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT distinct TVM.MATERIAL_ID, ---物料ID\n" );
		sql.append("       TSO.ORDER_ID, ---订单ID\n" );
		sql.append("       TSO.AREA_ID, ---业务范围ID\n" );
		sql.append("       TSOD.DETAIL_ID, ---订单明细ID\n" );
		sql.append("       TSO.ORDER_NO, ---订单号码\n" );
		sql.append("       VMG.SERIES_NAME, ---车系\n");
		sql.append("       (CASE\n");  
		sql.append("         WHEN NVL(VVOR.RESOURCE_AMOUNT, 0) > 0 THEN\n");  
		sql.append("          '有'\n");  
		sql.append("         ELSE\n");  
		sql.append("          '无'\n");  
		sql.append("       END) RAMOUNT,\n");  

		sql.append("       TVM.MATERIAL_CODE, ---物料编码\n" );
		sql.append("       TVM.MATERIAL_NAME, ---物料名称\n" );
		sql.append("       TVM.COLOR_NAME, ---颜色\n" );
		sql.append("       TSOD.SINGLE_PRICE, ---物料单价\n" );
		sql.append("       TSOD.ORDER_AMOUNT, ---订单数量\n" );
		sql.append("       NVL(TSOD.CHECK_AMOUNT, 0) CHECK_AMOUNT, ---已审核数量\n" );
		sql.append("       NVL(TSOD.CALL_AMOUNT, 0) CALL_AMOUNT ,---已申请数量\n");
		sql.append("(SELECT NVL(SUM(TVDRD.REQ_AMOUNT), 0) FROM TT_VS_DLVRY_REQ_DTL TVDRD, TT_VS_DLVRY_REQ TVDR WHERE TVDRD.REQ_ID = TVDR.REQ_ID AND TVDRD.ORDER_DETAIL_ID = TSOD.DETAIL_ID AND TVDR.REQ_STATUS <> " + Constant.ORDER_REQ_STATUS_07 + " AND TVDR.ORDER_DEALER_ID = " + dealerId + ") REQ_AMOUNT, --已提报数量\n");
		sql.append("(SELECT NVL(SUM(TVDRD.REQ_AMOUNT), 0) FROM TT_VS_DLVRY_REQ_DTL TVDRD, TT_VS_DLVRY_REQ TVDR WHERE TVDRD.REQ_ID = TVDR.REQ_ID AND TVDRD.ORDER_DETAIL_ID = TSOD.DETAIL_ID AND TVDR.REQ_STATUS = " + Constant.ORDER_REQ_STATUS_YSH + " AND TVDR.ORDER_DEALER_ID = " + dealerId + ") NC_AMOUNT, --已提报数量\n");
		sql.append("	   TSOD.DISCOUNT_RATE,\n");
		sql.append("       TSOD.DISCOUNT_S_PRICE,\n");  
		sql.append("       TSOD.DISCOUNT_PRICE\n");

		sql.append("  FROM TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		sql.append("       VW_MATERIAL_GROUP        VMG,\n" );
		sql.append("       TM_AREA_GROUP            TAG,\n" );
		sql.append("       TT_VS_ORDER              TSO,\n" );
		sql.append("       TT_VS_ORDER_DETAIL       TSOD,\n" );
		sql.append("       VW_VS_ORDER_RESOURCE     VVOR\n");  
		sql.append(" WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n" );
		sql.append("   AND TSOD.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append("   AND TVMGR.GROUP_ID = VMG.PACKAGE_ID\n" );
		sql.append("   AND TSOD.MATERIAL_ID = VVOR.MATERIAL_ID(+)\n" );
		sql.append("   AND (VMG.SERIES_ID = TAG.MATERIAL_GROUP_ID OR\n");
		sql.append("       VMG.MODEL_ID = TAG.MATERIAL_GROUP_ID OR\n");  
		sql.append("       VMG.PACKAGE_ID = TAG.MATERIAL_GROUP_ID)\n");
		sql.append("   AND TAG.AREA_ID = TSO.AREA_ID\n" );
		sql.append("   AND TSO.ORDER_TYPE = "+Constant.ORDER_TYPE_01+"\n" );
		sql.append("   AND TSO.ORDER_STATUS = "+Constant.ORDER_STATUS_05+"\n" );
		sql.append("   AND TSO.COMPANY_ID = "+oemCompanyId+"\n" );
		sql.append("   AND TSO.billing_org_id IN("+firstDealer+")\n" );
		if(orderYear != null && !"".equals(orderYear)){
			sql.append(" AND TSO.ORDER_YEAR = '"+orderYear+"'\n");
		}
		if(orderWeek != null && !"".equals(orderWeek)){
			sql.append(" AND TSO.ORDER_WEEK = '"+orderWeek+"'\n");
		}
		if(areaId != null && !"".equals(areaId)){
			sql.append(" AND TSO.AREA_ID = "+areaId+"\n");
		}
		if(orderNo != null && !"".equals(orderNo)){
			sql.append(" AND TSO.ORDER_NO LIKE '%"+orderNo+"%'\n");
		}
		sql.append("	AND TSOD.CHECK_AMOUNT > 0\n");
		sql.append("	AND NVL(TSOD.CHECK_AMOUNT, 0) <> NVL(TSOD.CALL_AMOUNT, 0)\n");

		sql.append("    ORDER BY ORDER_ID\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 月度常规订单启票列表
	 * @param orderYear
	 * @param orderMonth
	 * @param areaId
	 * @param dealerId
	 * @param orderNo
	 * @param oemCompanyId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getWeekGeneralOrderCallListCVS(String orderYear,String orderWeek,String areaId,
			String firstDealer, String dealerId, String orderNo, Long oemCompanyId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT distinct TVM.MATERIAL_ID, ---物料ID\n" );
		sql.append("       TSO.ORDER_ID, ---订单ID\n" );
		sql.append("       TSO.AREA_ID, ---业务范围ID\n" );
		sql.append("       TSOD.DETAIL_ID, ---订单明细ID\n" );
		sql.append("       TSO.ORDER_NO, ---订单号码\n" );
		sql.append("       VMG.SERIES_NAME, ---车系\n");
		sql.append("       (CASE\n");  
		sql.append("         WHEN NVL(VVOR.RESOURCE_AMOUNT, 0) > 0 THEN\n");  
		sql.append("          '有'\n");  
		sql.append("         ELSE\n");  
		sql.append("          '无'\n");  
		sql.append("       END) RAMOUNT,\n");  

		sql.append("       TVM.MATERIAL_CODE, ---物料编码\n" );
		sql.append("       TVM.MATERIAL_NAME, ---物料名称\n" );
		sql.append("       TVM.COLOR_NAME, ---颜色\n" );
		sql.append("       TSOD.SINGLE_PRICE, ---物料单价\n" );
		sql.append("       TSOD.ORDER_AMOUNT, ---订单数量\n" );
		sql.append("       NVL(TSOD.CHECK_AMOUNT, 0) CHECK_AMOUNT, ---已审核数量\n" );
		sql.append("       NVL(TSOD.CALL_AMOUNT, 0) CALL_AMOUNT ,---已申请数量\n");
		sql.append("(SELECT NVL(SUM(TVDRD.REQ_AMOUNT), 0) FROM TT_VS_DLVRY_REQ_DTL TVDRD, TT_VS_DLVRY_REQ TVDR WHERE TVDRD.REQ_ID = TVDR.REQ_ID AND TVDRD.ORDER_DETAIL_ID = TSOD.DETAIL_ID AND TVDR.REQ_STATUS <> " + Constant.ORDER_REQ_STATUS_07 + " AND TVDR.ORDER_DEALER_ID = " + dealerId + ") REQ_AMOUNT, --已提报数量\n");
		sql.append("(SELECT NVL(SUM(TVDRD.REQ_AMOUNT), 0) FROM TT_VS_DLVRY_REQ_DTL TVDRD, TT_VS_DLVRY_REQ TVDR WHERE TVDRD.REQ_ID = TVDR.REQ_ID AND TVDRD.ORDER_DETAIL_ID = TSOD.DETAIL_ID AND TVDR.REQ_STATUS in (" + Constant.ORDER_REQ_STATUS_YSH + "," + Constant.ORDER_REQ_STATUS_DCWSH + ") AND TVDR.ORDER_DEALER_ID = " + dealerId + ") NC_AMOUNT, --已提报数量\n");
		sql.append("	   TSOD.DISCOUNT_RATE,\n");
		sql.append("       TSOD.DISCOUNT_S_PRICE,\n");  
		sql.append("       TSOD.DISCOUNT_PRICE\n");

		sql.append("  FROM TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		sql.append("       VW_MATERIAL_GROUP        VMG,\n" );
		sql.append("       TM_AREA_GROUP            TAG,\n" );
		sql.append("       TT_VS_ORDER              TSO,\n" );
		sql.append("       TT_VS_ORDER_DETAIL       TSOD,\n" );
		sql.append("       VW_VS_ORDER_RESOURCE     VVOR\n");  
		sql.append(" WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n" );
		sql.append("   AND TSOD.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append("   AND TVMGR.GROUP_ID = VMG.PACKAGE_ID\n" );
		sql.append("   AND TSOD.MATERIAL_ID = VVOR.MATERIAL_ID(+)\n" );
		sql.append("   AND (VMG.SERIES_ID = TAG.MATERIAL_GROUP_ID OR\n");
		sql.append("       VMG.MODEL_ID = TAG.MATERIAL_GROUP_ID OR\n");  
		sql.append("       VMG.PACKAGE_ID = TAG.MATERIAL_GROUP_ID)\n");
		sql.append("   AND TAG.AREA_ID = TSO.AREA_ID\n" );
		sql.append("   AND TSO.ORDER_TYPE = "+Constant.ORDER_TYPE_01+"\n" );
		sql.append("   AND TSO.ORDER_STATUS = "+Constant.ORDER_STATUS_05+"\n" );
		sql.append("   AND TSO.COMPANY_ID = "+oemCompanyId+"\n" );
		sql.append("   AND TSO.ORDER_ORG_ID IN("+dealerId+")\n" );
		if(orderYear != null && !"".equals(orderYear)){
			sql.append(" AND TSO.ORDER_YEAR = '"+orderYear+"'\n");
		}
		if(orderWeek != null && !"".equals(orderWeek)){
			sql.append(" AND TSO.ORDER_WEEK = '"+orderWeek+"'\n");
		}
		if(areaId != null && !"".equals(areaId)){
			sql.append(" AND TSO.AREA_ID = "+areaId+"\n");
		}
		if(orderNo != null && !"".equals(orderNo)){
			sql.append(" AND TSO.ORDER_NO LIKE '%"+orderNo+"%'\n");
		}
		sql.append("	AND TSOD.CHECK_AMOUNT > 0\n");
		sql.append("	AND NVL(TSOD.CHECK_AMOUNT, 0) <> NVL(TSOD.CALL_AMOUNT, 0)\n");

		sql.append("    ORDER BY ORDER_ID\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * Function         : 经销商账户可用余额查询
	 * @param           : 经销商ID
	 * @return          : 经销商账户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-25
	 */
	public List<Map<String, Object>> getDealerAccount(String dealerId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSA.ACCOUNT_ID,TSA.AVAILABLE_AMOUNT, TSA.FREEZE_AMOUNT, TSAT.TYPE_ID,TSAT.TYPE_NAME\n" );
		sql.append("  FROM TT_VS_ACCOUNT TSA, TT_VS_ACCOUNT_TYPE TSAT\n" );
		sql.append(" WHERE TSA.ACCOUNT_TYPE_ID = TSAT.TYPE_ID\n" );
		sql.append("   AND TSA.DEALER_ID IN("+dealerId+")\n");
		sql.append("   AND TSAT.STATUS ="+Constant.STATUS_ENABLE+"\n" );
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * Function         : 经销商地址查询
	 * @param           : 经销商ID
	 * @return          : 经销商地址信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-25
	 */
	public List<Map<String, Object>> getDealerAddress(String dealerId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TMA.ID, TMA.ADDRESS\n" );
		sql.append("  FROM TM_VS_ADDRESS TMA\n" );
		sql.append("   WHERE TMA.DEALER_ID IN("+dealerId+")\n");
		sql.append("   AND TMA.STATUS ="+Constant.STATUS_ENABLE+"\n" );
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * Function         : 发运指令下达查询
	 * @param           : 订单开始年周
	 * @param           : 订单结束年周
	 * @param           : 订单类型
	 * @param			: 选择类型
	 * @param           : 经销商CODE
	 * @param           : 物料组CODE
	 * @param           : 运送方式
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-04
	 */
	public PageResult<Map<String, Object>> getDeliveryList(String areaIds,String orgId, String startYear,String startWeek,String endYear,String endWeek,String orderType,
			String areaId,String dealerCode,String transportType, String orderNo,Long companyId,int curPage,int pageSize) throws Exception{
		List <Object>params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT E.REQ_ID,\n");
		sql.append("       DECODE(E.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD.DEALER_SHORTNAME, B.DEALER_SHORTNAME) DEALER_NAME,\n");  
		sql.append("       C.DEALER_SHORTNAME DEALER_NAME1,\n");  
		sql.append("       C.DEALER_CODE,\n");  
		sql.append("       D.DEALER_SHORTNAME DEALER_NAME2,\n");  
		sql.append("       A.ORDER_TYPE,\n");  
		sql.append("       A.ORDER_NO,\n");  
		sql.append("       A.ORDER_YEAR || '.' || A.ORDER_WEEK ORDER_WEEK,\n");  
		sql.append("       E.DELIVERY_TYPE,\n");  
		sql.append("       E.DLVRY_REQ_NO,\n");  
		sql.append("       TCU.NAME UPDATE_BY,\n");  
		sql.append("       DECODE (E.DELIVERY_TYPE ,"+Constant.TRANSPORT_TYPE_02+",F.ADDRESS,'')ADDRESS,\n");  
		sql.append("       NVL(SUM(RESERVE_AMOUNT), 0) RESERVE_AMOUNT\n");
		sql.append("FROM TT_VS_ORDER A, TM_DEALER B, TM_DEALER C, TM_DEALER D, TM_DEALER TMD,\n" );
		sql.append("     TT_VS_DLVRY_REQ E,TM_VS_ADDRESS F, TC_USER TCU, TT_VS_DLVRY_REQ_DTL G\n" );
		/*sql.append("     (\n" );
		sql.append("        SELECT REQ_ID,NVL(SUM(RESERVE_AMOUNT), 0) RESERVE_AMOUNT\n" );
		sql.append("        FROM TT_VS_DLVRY_REQ_DTL\n" );
		sql.append("        GROUP BY REQ_ID\n" );
		sql.append("     ) G\n" );*/
		sql.append("WHERE A.ORDER_ORG_ID = B.DEALER_ID\n" );
		sql.append("AND E.UPDATE_BY = TCU.USER_ID\n" );
		sql.append("AND E.ORDER_DEALER_ID = TMD.DEALER_ID(+)\n" );
		sql.append("AND A.ORDER_ID = E.ORDER_ID\n" );
		sql.append("AND A.BILLING_ORG_ID = C.DEALER_ID\n" );
		sql.append("AND E.RECEIVER = D.DEALER_ID\n" );
		sql.append("AND E.ADDRESS_ID = F.ID(+)\n" );
		sql.append("AND E.REQ_ID = G.REQ_ID\n" );
		sql.append("AND A.COMPANY_ID = ").append(companyId).append("\n");
		
		if(Utility.testString(orgId)){
			sql.append("AND A.BILLING_ORG_ID IN (\n");
			sql.append("	SELECT DEALER_ID FROM TM_DEALER_ORG_RELATION WHERE ORG_ID = ").append(orgId).append(")\n");
		}
		
		if(Utility.testString(areaId)){
			sql.append("AND E.AREA_ID =").append(areaId).append("\n");
		}
		
		if(Utility.testString(dealerCode)){
			sql.append("AND C.DEALER_CODE IN (").append(dealerCode).append(")\n");
		}
		
		if(Utility.testString(orderType)){
			sql.append("AND A.ORDER_TYPE = ").append(orderType).append("\n");
		}
		
		if(Utility.testString(orderNo)){
			sql.append("AND E.DLVRY_REQ_NO LIKE '%").append(orderNo).append("%'\n");
		}
		
		if(Utility.testString(transportType)){
			sql.append("AND E.DELIVERY_TYPE = ").append(transportType).append("\n");
		}
		if (!"".equals(areaIds) && "".equals(areaId)) {
			sql.append("AND E.AREA_ID IN ("+areaIds+")\n");
		}
		sql.append("AND E.REQ_STATUS = ").append(Constant.ORDER_REQ_STATUS_03).append("\n");
		//sql.append("ORDER BY C.DEALER_CODE, E.REQ_DATE ASC\n");
		
		sql.append("group by E.REQ_ID,\n");
		sql.append("          E.CALL_LEAVEL,\n");  
		sql.append("          TMD.DEALER_SHORTNAME,\n");  
		sql.append("          B.DEALER_SHORTNAME,\n");  
		sql.append("          C.DEALER_SHORTNAME,\n");  
		sql.append("          C.DEALER_CODE,\n");  
		sql.append("          D.DEALER_SHORTNAME,\n");  
		sql.append("          A.ORDER_TYPE,\n");  
		sql.append("          A.ORDER_NO,\n");  
		sql.append("          A.ORDER_YEAR,\n");  
		sql.append("          A.ORDER_WEEK,\n");  
		sql.append("          E.DELIVERY_TYPE,\n");  
		sql.append("          E.DLVRY_REQ_NO,\n");  
		sql.append("          TCU.NAME,\n");  
		sql.append("          F.ADDRESS,\n");  
		sql.append("          E.REQ_DATE\n");
		
		sql.append("ORDER BY E.REQ_DATE DESC\n");//修改发运指令查询按日期为倒序排列
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	
	public PageResult<Map<String, Object>> getDealerDeliveryList(Long poseId, String dealerId,int curPage,int pageSize) throws Exception{
		List <Object>params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT E.REQ_ID, B.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       C.DEALER_SHORTNAME DEALER_NAME1,\n" );
		sql.append("       D.DEALER_SHORTNAME DEALER_NAME2, A.ORDER_TYPE,\n" );
		sql.append("       A.ORDER_NO, A.ORDER_YEAR||'.'||A.ORDER_WEEK ORDER_WEEK, \n" );
		sql.append("       E.DELIVERY_TYPE, F.ADDRESS, G.RESERVE_AMOUNT\n" );
		sql.append("FROM TT_VS_ORDER A, TM_DEALER B, TM_DEALER C, TM_DEALER D,TM_POSE_BUSINESS_AREA TMPBA,\n" );
		sql.append("     TT_VS_DLVRY_REQ E,TM_VS_ADDRESS F,\n" );
		sql.append("     (\n" );
		sql.append("        SELECT REQ_ID,NVL(SUM(RESERVE_AMOUNT), 0) RESERVE_AMOUNT\n" );
		sql.append("        FROM TT_VS_DLVRY_REQ_DTL\n" );
		sql.append("        GROUP BY REQ_ID\n" );
		sql.append("     ) G\n" );
		sql.append("WHERE A.ORDER_ORG_ID = B.DEALER_ID\n" );
		sql.append("AND A.ORDER_ID = E.ORDER_ID\n" );
		sql.append("AND A.BILLING_ORG_ID = C.DEALER_ID\n" );
		sql.append("AND E.RECEIVER = D.DEALER_ID\n" );
		sql.append("AND A.AREA_ID = TMPBA.AREA_ID\n" );
		sql.append("AND E.ADDRESS_ID = F.ID(+)\n" );
		sql.append("AND E.REQ_ID = G.REQ_ID\n" );
		sql.append("AND TMPBA.POSE_ID = " + poseId + "\n" );
//		sql.append("AND (A.BILLING_ORG_ID IN ( ").append(dealerId).append(") OR A.ORDER_ORG_ID IN (").append(dealerId).append("))\n");
		sql.append("AND (A.ORDER_ORG_ID IN (").append(dealerId).append("))\n");
		sql.append("AND E.REQ_STATUS = ").append(Constant.ORDER_REQ_STATUS_02).append("\n");

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function         : 发运指令下达可用库存查询
	 * @param           : 物料ID
	 * @return          : 物料可用库存信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-04
	 */
	public List<Map<String, Object>> getMaterialStockAmount(String materialId,String patchNo)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT T.MATERIAL_ID,\n" );
		sql.append("       T.MATERIAL_CODE,\n" );
		sql.append("       T.MATERIAL_NAME,\n" );
		sql.append("       T.STOCK_AMOUNT1 - M.DELIVERY_AMOUNT AS STOCK_AMOUNT\n" );
		sql.append("  FROM (SELECT S.MATERIAL_ID,\n" );
		sql.append("               S.MATERIAL_CODE,\n" );
		sql.append("               S.MATERIAL_NAME,\n" );
		sql.append("               COUNT(TMV.VEHICLE_ID) STOCK_AMOUNT1\n" );
		sql.append("          FROM (SELECT TVM.MATERIAL_ID, TVM.MATERIAL_CODE, TVM.MATERIAL_NAME\n" );
		sql.append("                  FROM TM_VHCL_MATERIAL TVM\n" );
		sql.append("                 WHERE TVM.MATERIAL_ID = "+materialId+") S\n" );
		sql.append("          LEFT JOIN TM_VEHICLE TMV ON TMV.MATERIAL_ID = S.MATERIAL_ID\n" );
		sql.append("                                  AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_02+"\n" );
		sql.append("                                  AND TMV.LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n" );
		sql.append("                                  AND TMV.ORG_ID IS NULL\n" );
		sql.append("                                  AND TMV.DEALER_ID IS NULL\n" );
		sql.append("                                  AND TMV.ORG_TYPE IS NULL\n" );
		if(null==patchNo || "".equals(patchNo)|| "null".equals(patchNo)){
			sql.append("   AND TMV.SPECIAL_BATCH_NO IS NULL\n" );
		}else{
			sql.append("   AND TMV.SPECIAL_BATCH_NO ='"+patchNo+"'\n" );
		}
		sql.append("         GROUP BY S.MATERIAL_ID, S.MATERIAL_CODE, S.MATERIAL_NAME) T\n" );
		sql.append("  JOIN (SELECT TVDRD.MATERIAL_ID,\n" );
		sql.append("               NVL(SUM(TVDRD.DELIVERY_AMOUNT), 0) AS DELIVERY_AMOUNT\n" );
		sql.append(" FROM TT_VS_DLVRY_REQ_DTL TVDRD, TT_VS_DLVRY_REQ TVDR\n" );
		sql.append("WHERE TVDRD.REQ_ID = TVDR.REQ_ID\n" );
		sql.append("  AND ((TVDR.FLEET_ID > 0 AND TVDR.ADDRESS_ID > 0) OR\n" );
		sql.append("      (TVDR.FLEET_ID IS NULL AND TVDR.FLEET_ADDRESS IS NULL))");
		sql.append("         GROUP BY TVDRD.MATERIAL_ID) M ON M.MATERIAL_ID = T.MATERIAL_ID");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public Map<String, Object> getDeliveryInfo(String reqId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       TVDR.DELIVERY_TYPE,\n" );
		sql.append("       TVA.ADDRESS,\n" );
		sql.append("       TVDR.LINK_MAN,\n" );
		sql.append("       TVDR.TEL\n" );
		sql.append("  FROM TT_VS_DLVRY_REQ TVDR,\n" );
		sql.append("       TT_VS_ORDER     TVO,\n" );
		sql.append("       TM_DEALER       TMD,\n" );
		sql.append("       TM_VS_ADDRESS   TVA\n" );
		sql.append(" WHERE TVDR.ORDER_ID = TVO.ORDER_ID\n" );
		sql.append("   AND TVO.ORDER_ORG_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVDR.ADDRESS_ID = TVA.ID(+)\n" );
		sql.append("   AND TVDR.REQ_ID ="+reqId+"\n");
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	/**
	 * Function         : 发运指令下达明细查询
	 * @param           : 订单开始年周
	 * @param           : 订单结束年周
	 * @param           : 订单类型
	 * @param			: 选择类型
	 * @param           : 经销商CODE
	 * @param           : 物料组CODE
	 * @param           : 运送方式
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-04
	 */
	public PageResult<Map<String, Object>> getDeliveryDetailList(String reqId,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();

		sql.append("SELECT T.DETAIL_ID,\n");
		sql.append("       T.ORDER_DETAIL_ID,\n");  
		sql.append("       T.VER,\n");  
		sql.append("       T.MATERIAL_ID,\n");  
		sql.append("       T.MATERIAL_CODE,\n");  
		sql.append("       T.MATERIAL_NAME,\n");  
		sql.append("       T.COLOR_NAME,\n");  
		sql.append("       T.PATCH_NO,\n");  
		sql.append("       T.REQ_AMOUNT,\n");  
		sql.append("       T.DELIVERY_AMOUNT,\n");  
		sql.append("       T.WAIT_AMOUNT,\n");  
		sql.append("       M.STOCK_AMOUNT STOCK\n");  
		sql.append("  FROM (SELECT TVDRD.DETAIL_ID,\n");  
		sql.append("               TVDRD.ORDER_DETAIL_ID,\n");  
		sql.append("               NVL(TVDRD.VER, 0) VER,\n");  
		sql.append("               TVM.MATERIAL_ID,\n");  
		sql.append("               TVM.MATERIAL_CODE,\n");  
		sql.append("               TVM.MATERIAL_NAME,\n");  
		sql.append("               TVM.COLOR_NAME,\n");  
		sql.append("               DECODE(TVDRD.PATCH_NO, NULL, ' ', TVDRD.PATCH_NO) PATCH_NO,\n");  
		sql.append("               NVL(TVDRD.REQ_AMOUNT, 0) REQ_AMOUNT,\n");  
		sql.append("               NVL(TVDRD.DELIVERY_AMOUNT, 0) DELIVERY_AMOUNT,\n");  
		sql.append("               NVL(TVDRD.REQ_AMOUNT, 0) - NVL(TVDRD.DELIVERY_AMOUNT, 0) WAIT_AMOUNT\n");  
		sql.append("          FROM TT_VS_DLVRY_REQ     TVDR,\n");  
		sql.append("               TT_VS_DLVRY_REQ_DTL TVDRD,\n");  
		sql.append("               TM_VHCL_MATERIAL    TVM\n");  
		sql.append("         WHERE TVDR.REQ_ID = TVDRD.REQ_ID\n");  
		sql.append("           AND TVDRD.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("           AND TVDR.REQ_EXEC_STATUS = "+Constant.REQ_EXEC_STATUS_01+"\n");  
		sql.append("           AND TVDRD.REQ_ID = "+reqId+") T\n");  
		sql.append("  LEFT JOIN (SELECT VVR.MATERIAL_ID,\n");  
		sql.append("                    DECODE(VVR.SPECIAL_BATCH_NO,\n");  
		sql.append("                           NULL,\n");  
		sql.append("                           ' ',\n");  
		sql.append("                           VVR.SPECIAL_BATCH_NO) SPECIAL_BATCH_NO,\n");  
		sql.append("                    VVR.ava_dlvry_stock STOCK_AMOUNT\n");  
		sql.append("               FROM VW_VS_RESOURCE VVR) M ON T.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("                                         AND T.PATCH_NO =\n");  
		sql.append("                                             M.SPECIAL_BATCH_NO");
		
	    PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}

	/**
	 * Function         : 发运指令查询
	 * @param           : 订单开始年周
	 * @param           : 订单结束年周
	 * @param           : 发运起始时间
	 * @param           : 发运结束时间
	 * @param           : 订单类型
	 * @param           : 订单号码
	 * @param			: 选择类型
	 * @param           : 经销商CODE
	 * @param           : 组织CODE
	 * @param           : 物料组CODE
	 * @param           : 运送方式
	 * @param           : 发运状态
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-24
	 */
	public PageResult<Map<String, Object>> getCommandQueryList(String regionId, String dutyType,String pageOrgId, Long orgId, String erpCode,String warehouseName,String billingOrgCode,String orderOrgCode,String startYear,String startWeek,String endYear,String endWeek,String startDate,String endDate,String orderType,
			String dealerCode,String orderNo,String deliveryNo,String transportType,String deliveryStatus,String groupCode,String areaIds,Long companyId,int curPage,int pageSize) throws Exception{
		List <Object>params = new LinkedList<Object>();
		boolean orgFlag = Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType) || (!CommonUtils.isNullString(pageOrgId) && Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType) && !orgId.toString().equals(pageOrgId)) ;
		
		StringBuffer sql= new StringBuffer();
		
		sql.append("SELECT TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("       TMD.DEALER_CODE CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME AS TTNAME,TMD.CREATE_DATE,\n" );
		sql.append("       DECODE(A.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_CODE, TMD2.DEALER_CODE) AS ORDER_ORG_CODE,\n");
		sql.append("       DECODE(A.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_SHORTNAME, TMD2.DEALER_SHORTNAME) AS ORDER_ORG_NAME,\n" );
		sql.append("       DECODE(A.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.CREATE_DATE, TMD2.CREATE_DATE) AS ORDER_CREATE_DATE,\n" );
		sql.append("       TVO.ORDER_NO, DECODE(TVDD.BILLING_CODE,null,'',TVDD.BILLING_CODE,'查看') INVOICE_NO, \n" );
		sql.append("       TVO.ORDER_TYPE,\n" );
		sql.append("       TVO.ORDER_YEAR,F.WAREHOUSE_NAME,\n" );
		sql.append("       TVO.ORDER_WEEK,\n" );
		sql.append("       TVD.DELIVERY_NO,\n" );
		sql.append("       TVD.DELIVERY_TYPE,TVD.RETURN_FLAG,\n" );
		sql.append("       DECODE (TVD.DELIVERY_TYPE ,"+Constant.TRANSPORT_TYPE_02+",TMVA.ADDRESS,'')ADDRESS,\n");
		sql.append("       TVD.DELIVERY_STATUS,\n" );
		//sql.append("       TVD.LOSE_REASON,\n" );
		//sql.append("       DECODE(XORR.REASON,null,'',XORR.REASON,'详细原因') LOSE_REASON,\n" );
		sql.append("       DECODE(TVD.LOSE_REASON,null,'',TVD.LOSE_REASON,'详细原因') LOSE_REASON,\n" );
		sql.append("       TVD.ERP_ORDER,\n" );
		sql.append("       TO_CHAR(TVD.DELIVERY_DATE,'YYYY-MM-DD HH24:MI:SS') DELIVERY_DATE,\n" );
		sql.append("       SUM(NVL(TVDD.MATCH_AMOUNT,0)) MATCH_AMOUNT,\n" );
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) DELIVERY_AMOUNT,\n" );
		sql.append("       SUM(TVDRD.TOTAL_PRICE+TVDRD.DISCOUNT_PRICE) TOTAL_PRICE\n" );
		sql.append("  FROM TT_VS_ORDER              TVO,\n" );
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n" );
		//sql.append("        XXDMS_ORDER_REASON_RETURN XORR,\n" );
		sql.append("       TT_VS_DLVRY              TVD,\n" );
		sql.append("       TT_VS_DLVRY_DTL          TVDD,\n" );
		sql.append("       TM_VS_ADDRESS            TMVA,\n" );
		sql.append("       TM_DEALER                TMD,\n" );
		sql.append("       TM_DEALER                TMD2,\n" );
		sql.append("       TM_DEALER                TMD3,\n" );
		sql.append("       TT_VS_DLVRY_REQ A,\n" );
		sql.append("       TM_WAREHOUSE F" );
		
		if(orgFlag) {
			sql.append(",\n" );
			sql.append("	   VW_ORG_DEALER           VOD\n");
		}
		
		sql.append(" WHERE TVO.ORDER_ID = TVD.ORDER_ID\n" );
		sql.append("       AND A.REQ_ID=TVD.REQ_ID \n");
		sql.append("       AND A.WAREHOUSE_ID(+) = F.WAREHOUSE_ID\n");
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n" );
		sql.append("   AND TVDRD.DETAIL_ID = TVDD.REQ_DTL_ID\n" );
		//sql.append("   AND TVM.ERP_ID=TVDDI.ERP_ID  AND TVM.MATERIAL_ID=TVDD.MATERIAL_ID \n");
		if(orgFlag) {
			sql.append("AND TVO.BILLING_ORG_ID = VOD.DEALER_ID\n");
			
			if(!CommonUtils.isNullString(pageOrgId)) {
				orgId = Long.parseLong(pageOrgId) ;
			}
			
			sql.append("AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		
		//sql.append("   AND  XORR.DMS_ORDER_NUMBER(+)=TVD.DELIVERY_NO\n" );
		sql.append("   AND TVDD.DELIVERY_AMOUNT <> 0\n" );
		sql.append("   AND TVO.AREA_ID IN("+areaIds+")\n" );
		//sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n" );
		sql.append("   AND TVD.ADDRESS_ID = TMVA.ID(+)\n" );
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n" );
		if (null != erpCode && !"".equals(erpCode)) {
			sql.append("   AND TVD.ERP_ORDER  LIKE '%"+erpCode+"%'\n");
		}
		sql.append("   AND TVO.ORDER_ORG_ID = TMD2.DEALER_ID\n" );
		sql.append("   AND A.ORDER_DEALER_ID = TMD3.DEALER_ID(+)\n" );
		sql.append("   AND TVO.COMPANY_ID = "+companyId+"\n" );
		
		if (!CommonUtils.isNullString(regionId)) {
			sql.append("AND TMD.PROVINCE_ID = ").append(regionId).append("\n");
		}
		
		//sql.append("   AND TVD.DELIVERY_TYPE != "+Constant.DELIVERY_STATUS_01+"\n" );
		
		if (null != billingOrgCode && !"".equals(billingOrgCode)) {
			String[] array = billingOrgCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != orderOrgCode && !"".equals(orderOrgCode)) {
			String[] array = orderOrgCode.split(",");
			sql.append("   AND (TMD2.DEALER_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");

			
			sql.append("   OR TMD3.DEALER_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append("))\n");
		}
		if(!"".equals(warehouseName)&&warehouseName!=null&&!"".equals(warehouseName)&&warehouseName!=null){
			sql.append("   AND F.WAREHOUSE_ID= "+warehouseName+"\n" );
		}
		if(!"-1".equals(deliveryStatus)&&!"".equals(deliveryStatus)&&deliveryStatus!=null){
			sql.append("   AND TVD.DELIVERY_STATUS = "+deliveryStatus+"\n" );
		}
		if(!"-1".equals(orderType)&&!"".equals(orderType)&&orderType!=null){
			sql.append("   AND TVO.ORDER_TYPE = "+orderType+"\n" );
		}
		if(!"".equals(dealerCode)&&dealerCode!=null){
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		if(!"".equals(deliveryNo)&&deliveryNo!=null){
			sql.append("   AND TVD.DELIVERY_NO LIKE '%"+deliveryNo+"%'\n" );
		}
		if(!"".equals(orderNo)&&orderNo!=null){
			sql.append("   AND TVO.ORDER_NO LIKE '%"+orderNo+"%'\n" );
		}
		if(!"-1".equals(transportType)&&!"".equals(transportType)&&transportType!=null){
			sql.append("   AND TVD.DELIVERY_TYPE = "+transportType+"\n" );	
		}
		if(!"".equals(startYear)&&startYear!=null&&!"".equals(endYear)&&endYear!=null){
			sql.append("   AND TVO.ORDER_YEAR BETWEEN "+startYear+" AND "+endYear+"\n" );
		}
		if(!"".equals(startWeek)&&startWeek!=null&&!"".equals(endWeek)&&endWeek!=null){
			sql.append("   AND TVO.ORDER_WEEK BETWEEN "+startWeek+" AND "+endWeek+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE < =TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		sql.append(" GROUP BY TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("          TMD.DEALER_CODE,\n" );
		sql.append("          TMD.DEALER_SHORTNAME,TMD.CREATE_DATE,\n" );
		sql.append("          DECODE(A.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_CODE, TMD2.DEALER_CODE),\n");
		sql.append("          DECODE(A.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_SHORTNAME, TMD2.DEALER_SHORTNAME),\n" );
		sql.append("          DECODE(A.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.CREATE_DATE, TMD2.CREATE_DATE),\n" );
		sql.append("          TVO.ORDER_NO,\n" );
		sql.append("          TVO.ORDER_TYPE,\n" );
		sql.append("          TVO.ORDER_YEAR,\n" );
		sql.append("          TVO.ORDER_WEEK,\n" );
		sql.append("          TVD.DELIVERY_NO,\n" );
		sql.append("          TVD.DELIVERY_TYPE,\n" );
		sql.append("          TMVA.ADDRESS,\n" );
		sql.append("          TVD.DELIVERY_STATUS,\n" );
		sql.append("          TVD.LOSE_REASON,F.WAREHOUSE_NAME,\n" );
		//sql.append("          XORR.REASON,\n" );
		sql.append("          TVD.ERP_ORDER,\n" );
		sql.append("          TVD.DELIVERY_DATE,TVDD.BILLING_CODE,TVD.RETURN_FLAG\n");
		sql.append("ORDER BY DELIVERY_DATE DESC\n");
		//sql.append("          TVOD.SPECIAL_BATCH_NO");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	public List<Map<String, Object>> getCommandQueryDown(String regionId, String dutyType,String pageOrgId, Long orgId, String warehouseName,String billingOrgCode,String orderOrgCode,String startYear,String startWeek,String endYear,String endWeek,String startDate,String endDate,String orderType,
			String dealerCode,String orderNo,String transportType,String deliveryStatus,String groupCode,String areaIds,Long companyId,String erpCode) throws Exception{
		List <Object>params = new LinkedList<Object>();
		boolean orgFlag = Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType) || (!CommonUtils.isNullString(pageOrgId) && Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType) && !orgId.toString().equals(pageOrgId)) ;
		
		StringBuffer sql= new StringBuffer();
		
		sql.append("SELECT TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("       TMD.DEALER_CODE CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME AS TTNAME, TMD.CREATE_DATE,\n" );
		sql.append("       TMD2.DEALER_CODE AS ORDER_ORG_CODE,\n");
		sql.append("       TMD2.DEALER_SHORTNAME AS ORDER_ORG_NAME,TMD2.CREATE_DATE AS ORDER_CREATE_DATE,\n" );
		sql.append("       TVO.ORDER_NO, DECODE(TVDD.BILLING_CODE,null,'',TVDD.BILLING_CODE,TVDD.BILLING_CODE) INVOICE_NO, \n" );
		sql.append("       TVO.ORDER_TYPE,\n" );
		sql.append("       TVO.ORDER_YEAR,F.WAREHOUSE_NAME,\n" );
		sql.append("       TVO.ORDER_WEEK,\n" );
		sql.append("       TVD.DELIVERY_NO,\n" );
		sql.append("       TVD.DELIVERY_TYPE,\n" );
		sql.append("       DECODE (TVD.DELIVERY_TYPE ,"+Constant.TRANSPORT_TYPE_02+",TMVA.ADDRESS,'')ADDRESS,\n");
		sql.append("       TVD.DELIVERY_STATUS,\n" );
		//sql.append("       TVD.LOSE_REASON,\n" );
		//sql.append("       DECODE(XORR.REASON,null,'',XORR.REASON,'详细原因') LOSE_REASON,\n" );
		sql.append("       DECODE(TVD.LOSE_REASON,null,'',TVD.LOSE_REASON,'详细原因') LOSE_REASON,\n" );
		sql.append("       TVD.ERP_ORDER,\n" );
		sql.append("       TO_CHAR(TVD.DELIVERY_DATE,'YYYY-MM-DD HH24:MI:SS') DELIVERY_DATE,\n" );
		sql.append("       SUM(NVL(TVDD.MATCH_AMOUNT,0)) MATCH_AMOUNT,\n" );
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) DELIVERY_AMOUNT,\n" );
		sql.append("       SUM(TVDRD.TOTAL_PRICE+TVDRD.DISCOUNT_PRICE) TOTAL_PRICE, TC1.CODE_DESC CODE_DESC1,TC2.CODE_DESC CODE_DESC2\n" );
		sql.append("  FROM TT_VS_ORDER              TVO,\n" );
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n" );
		//sql.append("        XXDMS_ORDER_REASON_RETURN XORR,\n" );
		sql.append("       TT_VS_DLVRY              TVD,\n" );
		sql.append("       TT_VS_DLVRY_DTL          TVDD,\n" );
		sql.append("       TM_VS_ADDRESS            TMVA,\n" );
		sql.append("       TM_DEALER                TMD,TM_DEALER           TMD3,\n" );
		sql.append("       TM_DEALER                TMD2,TC_CODE TC1,TC_CODE TC2,\n" );
		sql.append("       TT_VS_DLVRY_REQ A,\n" );
		sql.append("       TM_WAREHOUSE F" );
		
		if(orgFlag) {
			sql.append(",\n" );
			sql.append("	   VW_ORG_DEALER           VOD\n");
		}
		
		sql.append(" WHERE TVO.ORDER_ID = TVD.ORDER_ID\n" );
		sql.append("       AND A.REQ_ID=TVD.REQ_ID  AND A.ORDER_DEALER_ID = TMD3.DEALER_ID(+)\n");
		sql.append("       AND A.WAREHOUSE_ID(+) = F.WAREHOUSE_ID\n");
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID AND TC1.CODE_ID=TVD.DELIVERY_STATUS AND TC2.CODE_ID=TVD.DELIVERY_TYPE\n" );
		sql.append("   AND TVDRD.DETAIL_ID = TVDD.REQ_DTL_ID\n" );
		//sql.append("   AND TVM.ERP_ID=TVDDI.ERP_ID  AND TVM.MATERIAL_ID=TVDD.MATERIAL_ID \n");
		if(orgFlag) {
			sql.append("AND TVO.BILLING_ORG_ID = VOD.DEALER_ID\n");
			
			if(!CommonUtils.isNullString(pageOrgId)) {
				orgId = Long.parseLong(pageOrgId) ;
			}
			
			sql.append("AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		
		//sql.append("   AND  XORR.DMS_ORDER_NUMBER(+)=TVD.DELIVERY_NO\n" );
		sql.append("   AND TVDD.DELIVERY_AMOUNT <> 0\n" );
		sql.append("   AND TVO.AREA_ID IN("+areaIds+")\n" );
		//sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n" );
		sql.append(" AND TC1.CODE_ID=TVD.DELIVERY_STATUS AND TC2.CODE_ID=TVD.DELIVERY_TYPE  AND TVD.ADDRESS_ID = TMVA.ID(+)\n" );
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n" );
		if (null != erpCode && !"".equals(erpCode)) {
			sql.append("   AND TVD.ERP_ORDER  LIKE '%"+erpCode+"%'\n");
		}
		sql.append("   AND TVO.ORDER_ORG_ID = TMD2.DEALER_ID\n" );
		sql.append("   AND TVO.COMPANY_ID = "+companyId+"\n" );
		
		if (!CommonUtils.isNullString(regionId)) {
			sql.append("AND TMD.PROVINCE_ID = ").append(regionId).append("\n");
		}
		
		//sql.append("   AND TVD.DELIVERY_TYPE != "+Constant.DELIVERY_STATUS_01+"\n" );
		
		if (null != billingOrgCode && !"".equals(billingOrgCode)) {
			String[] array = billingOrgCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != orderOrgCode && !"".equals(orderOrgCode)) {
			String[] array = orderOrgCode.split(",");
			sql.append("   AND (TMD2.DEALER_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
			sql.append("    OR TMD3.DEALER_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append("))\n");
		}
		if(!"".equals(warehouseName)&&warehouseName!=null&&!"".equals(warehouseName)&&warehouseName!=null){
			sql.append("   AND F.WAREHOUSE_ID= "+warehouseName+"\n" );
		}
		if(!"-1".equals(deliveryStatus)&&!"".equals(deliveryStatus)&&deliveryStatus!=null){
			sql.append("   AND TVD.DELIVERY_STATUS = "+deliveryStatus+"\n" );
		}
		if(!"-1".equals(orderType)&&!"".equals(orderType)&&orderType!=null){
			sql.append("   AND TVO.ORDER_TYPE = "+orderType+"\n" );
		}
		if(!"".equals(dealerCode)&&dealerCode!=null){
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		if(!"".equals(orderNo)&&orderNo!=null){
			sql.append("   AND TVD.DELIVERY_NO LIKE '%"+orderNo+"%'\n" );
		}
		if(!"-1".equals(transportType)&&!"".equals(transportType)&&transportType!=null){
			sql.append("   AND TVD.DELIVERY_TYPE = "+transportType+"\n" );	
		}
		if(!"".equals(startYear)&&startYear!=null&&!"".equals(endYear)&&endYear!=null){
			sql.append("   AND TVO.ORDER_YEAR BETWEEN "+startYear+" AND "+endYear+"\n" );
		}
		if(!"".equals(startWeek)&&startWeek!=null&&!"".equals(endWeek)&&endWeek!=null){
			sql.append("   AND TVO.ORDER_WEEK BETWEEN "+startWeek+" AND "+endWeek+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE < =TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		sql.append(" GROUP BY TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("          TMD.DEALER_CODE,\n" );
		sql.append("          TMD.DEALER_SHORTNAME,  TMD.CREATE_DATE,\n" );
		sql.append("          TMD2.DEALER_CODE,\n");
		sql.append("          TMD2.DEALER_SHORTNAME, TMD2.CREATE_DATE,\n" );
		sql.append("          TVO.ORDER_NO,\n" );
		sql.append("          TVO.ORDER_TYPE,\n" );
		sql.append("          TVO.ORDER_YEAR,\n" );
		sql.append("          TVO.ORDER_WEEK,\n" );
		sql.append("          TVD.DELIVERY_NO,\n" );
		sql.append("          TVD.DELIVERY_TYPE,\n" );
		sql.append("          TMVA.ADDRESS,\n" );
		sql.append("          TVD.DELIVERY_STATUS,\n" );
		sql.append("          TVD.LOSE_REASON,F.WAREHOUSE_NAME,\n" );
		//sql.append("          XORR.REASON,\n" );
		sql.append("          TVD.ERP_ORDER,\n" );
		sql.append("          TVD.DELIVERY_DATE,TVDD.BILLING_CODE,TC1.CODE_DESC,TC2.CODE_DESC\n");
		sql.append("ORDER BY DELIVERY_DATE DESC\n");
		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName());
		return rs;
	}
	
	/**
	 * 经销商端发运指令查询
	 * @param startYear
	 * @param startWeek
	 * @param endYear
	 * @param endWeek
	 * @param startDate
	 * @param endDate
	 * @param orderType
	 * @param dealerCode
	 * @param orderNo
	 * @param transportType
	 * @param deliveryStatus
	 * @param groupCode
	 * @param areaIds
	 * @param companyId
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getDealerCommandQueryList(String warehouseName,String erpCode,String deliveryNo,String dealerId,String startYear,String startWeek,String endYear,String endWeek,String startDate,String endDate,String orderType,
			String orderNo,String transportType,String deliveryStatus,String groupCode,String areaIds,Long companyId,int curPage,int pageSize) throws Exception{
		List <Object>params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("       TMD.DEALER_CODE CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME AS TTNAME,\n" );
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_CODE, TMD2.DEALER_CODE) AS ORDER_ORG_CODE,\n");
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_SHORTNAME, TMD2.DEALER_SHORTNAME) AS ORDER_ORG_NAME,\n" );
		sql.append("       TVO.ORDER_NO,\n" );
		sql.append("       TVO.ORDER_TYPE,\n" );
		sql.append("       TVO.ORDER_YEAR,\n" );
		sql.append("       TVO.ORDER_WEEK,\n" );
		sql.append("       TVD.DELIVERY_NO,\n" );
		sql.append("       DECODE(TVDD.Billing_Code,null,'',TVDD.Billing_Code,'查看') INVOICE_NO, \n" );
		sql.append("       TVD.DELIVERY_TYPE,\n" );
		sql.append("       DECODE (TVD.DELIVERY_TYPE ,"+Constant.TRANSPORT_TYPE_02+",TMVA.ADDRESS,'')ADDRESS,\n");
		sql.append("       TVD.DELIVERY_STATUS,\n" );
		//sql.append("       TVD.LOSE_REASON,\n" );
		//sql.append("       DECODE(XORR.REASON,null,'',XORR.REASON,'详细原因') LOSE_REASON,\n" );
		sql.append("       DECODE(TVD.LOSE_REASON,null,'',TVD.LOSE_REASON,'详细原因') LOSE_REASON,\n" );
		sql.append("       TVD.ERP_ORDER,\n" );
		sql.append("       TO_CHAR(TVD.DELIVERY_DATE,'YYYY-MM-DD HH24:MI:SS') DELIVERY_DATE,\n" );
		sql.append("       SUM(NVL(TVDD.MATCH_AMOUNT,0)) MATCH_AMOUNT,\n" );
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) DELIVERY_AMOUNT,\n" );
		sql.append("       SUM(TVDRD.TOTAL_PRICE+TVDRD.DISCOUNT_PRICE) TOTAL_PRICE\n" );
		sql.append("  FROM TT_VS_ORDER              TVO,\n" );
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n" );
		//sql.append("        XXDMS_ORDER_REASON_RETURN XORR,\n" );
		sql.append("       TT_VS_DLVRY              TVD,\n" );
		sql.append("       TT_VS_DLVRY_DTL          TVDD,\n" );
		sql.append("       TM_VS_ADDRESS            TMVA,\n" );
		sql.append("       TM_DEALER                TMD,\n" );
		sql.append("       TT_VS_DLVRY_REQ      	TVDR,\n" );
		sql.append("       TM_DEALER                TMD3,\n" );
		sql.append("       TM_DEALER                TMD2\n" );
		sql.append(" WHERE TVO.ORDER_ID = TVD.ORDER_ID\n" );
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n" );
		sql.append("   AND TVDRD.DETAIL_ID = TVDD.REQ_DTL_ID\n" );
		sql.append("   AND TVD.REQ_ID = TVDR.REQ_ID\n" );
		sql.append("   AND TVDR.ORDER_DEALER_ID = TMD3.DEALER_ID(+)\n" );
		//sql.append("   AND TVD.DELIVERY_ID=TVDDI.ORDER_ID(+)  \n");
		//sql.append("   AND  XORR.DMS_ORDER_NUMBER(+)=TVD.DELIVERY_NO\n" );
		sql.append("   AND TVO.AREA_ID IN("+areaIds+")\n" );
		//sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n" );
		sql.append("   AND TVD.ADDRESS_ID = TMVA.ID(+)\n" );
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVO.ORDER_ORG_ID = TMD2.DEALER_ID\n" );
		sql.append("   AND TVDD.DELIVERY_AMOUNT <> 0\n" );
		sql.append("   AND TVD.COMPANY_ID = "+companyId+"\n" );
		//sql.append("   AND TVD.DELIVERY_TYPE != "+Constant.DELIVERY_STATUS_01+"\n" );
		if(dealerId !=null && !dealerId.equals("")){
			sql.append("   AND TVO.BILLING_ORG_ID IN ("+dealerId+")\n" );
		}
		
		if(!"-1".equals(deliveryStatus)&&!"".equals(deliveryStatus)&&deliveryStatus!=null){
			sql.append("   AND TVD.DELIVERY_STATUS = "+deliveryStatus+"\n" );
		}
		if(!"-1".equals(orderType)&&!"".equals(orderType)&&orderType!=null){
			sql.append("   AND TVO.ORDER_TYPE = "+orderType+"\n" );
		}
		if(!CommonUtils.isNullString(orderNo)) {
			sql.append("   AND TVO.ORDER_NO like '%").append(orderNo).append("%'\n" );
		}
		if(!"".equals(deliveryNo)&&deliveryNo!=null){
			sql.append("   AND TVD.DELIVERY_NO LIKE '%"+deliveryNo+"%'\n" );
		}
		
		if (!CommonUtils.isNullString(erpCode)) {
			sql.append("   AND TVD.ERP_ORDER  LIKE '%"+erpCode+"%'\n");
		}
		
		if(!"-1".equals(transportType)&&!"".equals(transportType)&&transportType!=null){
			sql.append("   AND TVD.DELIVERY_TYPE = "+transportType+"\n" );	
		}
		if(!"".equals(startYear)&&startYear!=null&&!"".equals(endYear)&&endYear!=null){
			sql.append("   AND TVO.ORDER_YEAR BETWEEN "+startYear+" AND "+endYear+"\n" );
		}
		if(!"".equals(startWeek)&&startWeek!=null&&!"".equals(endWeek)&&endWeek!=null){
			sql.append("   AND TVO.ORDER_WEEK BETWEEN "+startWeek+" AND "+endWeek+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE < =TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		
		if(!CommonUtils.isNullString(warehouseName)){
			sql.append("   AND TVD.WAREHOUSE_ID= "+warehouseName+"\n" );
		}
		
		sql.append(" GROUP BY TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("          TMD.DEALER_CODE,\n" );
		sql.append("          TMD.DEALER_SHORTNAME,\n" );
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_CODE, TMD2.DEALER_CODE),\n");
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_SHORTNAME, TMD2.DEALER_SHORTNAME),\n" );
		sql.append("          TVO.ORDER_NO,\n" );
		sql.append("          TVO.ORDER_TYPE,\n" );
		sql.append("          TVO.ORDER_YEAR,\n" );
		sql.append("          TVO.ORDER_WEEK,\n" );
		sql.append("          TVD.DELIVERY_NO,\n" );
		sql.append("          TVD.DELIVERY_TYPE,\n" );
		sql.append("          TMVA.ADDRESS,\n" );
		sql.append("          TVD.DELIVERY_STATUS,\n" );
		sql.append("          TVD.LOSE_REASON,\n" );
		//sql.append("          XORR.REASON,\n" );
		sql.append("          TVD.ERP_ORDER,\n" );
		sql.append("          TVD.DELIVERY_DATE,TVDD.Billing_Code\n");
		sql.append("ORDER BY DELIVERY_DATE DESC\n");
		//sql.append("          TVOD.SPECIAL_BATCH_NO");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * 经销商端发运指令查询下载
	 * @param startYear
	 * @param startWeek
	 * @param endYear
	 * @param endWeek
	 * @param startDate
	 * @param endDate
	 * @param orderType
	 * @param dealerCode
	 * @param orderNo
	 * @param transportType
	 * @param deliveryStatus
	 * @param groupCode
	 * @param areaIds
	 * @param companyId
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getDealerCommandQueryListLoad(String dealerId,String startYear,String startWeek,String endYear,String endWeek,String startDate,String endDate,String orderType,
			String orderNo,String transportType,String deliveryStatus,String groupCode,String areaIds,Long companyId,int curPage,int pageSize) throws Exception{
		List <Object>params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("       TMD.DEALER_CODE CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME AS TTNAME,\n" );
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_CODE, TMD2.DEALER_CODE) AS ORDER_ORG_CODE,\n");
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_SHORTNAME, TMD2.DEALER_SHORTNAME) AS ORDER_ORG_NAME,\n" );
		sql.append("       TVO.ORDER_NO,\n" );
		sql.append("       TVO.ORDER_TYPE,\n" );
		sql.append("       TVO.ORDER_YEAR,\n" );
		sql.append("       TVO.ORDER_WEEK,\n" );
		sql.append("       TVD.DELIVERY_NO,\n" );
		sql.append("       TVDD.Billing_Code INVOICE_NO, \n" );
		sql.append("       TC2.CODE_DESC DELIVERY_TYPE,\n" );
		sql.append("       DECODE (TVD.DELIVERY_TYPE ,"+Constant.TRANSPORT_TYPE_02+",TMVA.ADDRESS,'')ADDRESS,\n");
		sql.append("       TC3.CODE_DESC DELIVERY_STATUS,\n" );
		//sql.append("       TVD.LOSE_REASON,\n" );
		//sql.append("       DECODE(XORR.REASON,null,'',XORR.REASON,'详细原因') LOSE_REASON,\n" );
		sql.append("       TVD.LOSE_REASON LOSE_REASON,\n" );
		sql.append("       TVD.ERP_ORDER,\n" );
		sql.append("       TO_CHAR(TVD.DELIVERY_DATE,'YYYY-MM-DD HH24:MI:SS') DELIVERY_DATE,\n" );
		sql.append("       SUM(NVL(TVDD.MATCH_AMOUNT,0)) MATCH_AMOUNT,\n" );
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) DELIVERY_AMOUNT,\n" );
		sql.append("       SUM(TVDRD.TOTAL_PRICE+TVDRD.DISCOUNT_PRICE) TOTAL_PRICE\n" );
		sql.append("  FROM TT_VS_ORDER              TVO,\n" );
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n" );
		//sql.append("        XXDMS_ORDER_REASON_RETURN XORR,\n" );
		sql.append("       TT_VS_DLVRY              TVD,\n" );
		sql.append("       TT_VS_DLVRY_DTL          TVDD,\n" );
		sql.append("       TM_VS_ADDRESS            TMVA,\n" );
		sql.append("       TT_VS_DLVRY_REQ      	TVDR,\n" );
		sql.append("       TM_DEALER                TMD3,\n" );
		sql.append("       TM_DEALER                TMD,\n" );
		sql.append("       TM_DEALER                TMD2,TC_CODE TC2,TC_CODE TC3\n" );
		sql.append(" WHERE TVO.ORDER_ID = TVD.ORDER_ID\n" );
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n" );
		sql.append("   AND TVDRD.DETAIL_ID = TVDD.REQ_DTL_ID\n" );
		sql.append("   AND TVD.REQ_ID = TVDR.REQ_ID\n" );
		sql.append("   AND TVDR.ORDER_DEALER_ID = TMD3.DEALER_ID(+)\n" );
		sql.append("   AND TC2.CODE_ID=TVD.DELIVERY_TYPE  AND TC3.CODE_ID=TVD.DELIVERY_STATUS  \n");
		//sql.append("   AND  XORR.DMS_ORDER_NUMBER(+)=TVD.DELIVERY_NO\n" );
		sql.append("   AND TVO.AREA_ID IN("+areaIds+")\n" );
		//sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n" );
		sql.append("   AND TVD.ADDRESS_ID = TMVA.ID(+)\n" );
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVO.ORDER_ORG_ID = TMD2.DEALER_ID\n" );
		sql.append("   AND TVDD.DELIVERY_AMOUNT <> 0\n" );
		sql.append("   AND TVD.COMPANY_ID = "+companyId+"\n" );
		//sql.append("   AND TVD.DELIVERY_TYPE != "+Constant.DELIVERY_STATUS_01+"\n" );
		if(dealerId !=null && !dealerId.equals("")){
			sql.append("   AND TVO.BILLING_ORG_ID IN ("+dealerId+")\n" );
		}
		
		if(!"-1".equals(deliveryStatus)&&!"".equals(deliveryStatus)&&deliveryStatus!=null){
			sql.append("   AND TVD.DELIVERY_STATUS = "+deliveryStatus+"\n" );
		}
		if(!"-1".equals(orderType)&&!"".equals(orderType)&&orderType!=null){
			sql.append("   AND TVO.ORDER_TYPE = "+orderType+"\n" );
		}
		if(!"".equals(orderNo)&&orderNo!=null){
			sql.append("   AND TVD.DELIVERY_NO LIKE '%"+orderNo+"%'\n" );
		}
		if(!"-1".equals(transportType)&&!"".equals(transportType)&&transportType!=null){
			sql.append("   AND TVD.DELIVERY_TYPE = "+transportType+"\n" );	
		}
		if(!"".equals(startYear)&&startYear!=null&&!"".equals(endYear)&&endYear!=null){
			sql.append("   AND TVO.ORDER_YEAR BETWEEN "+startYear+" AND "+endYear+"\n" );
		}
		if(!"".equals(startWeek)&&startWeek!=null&&!"".equals(endWeek)&&endWeek!=null){
			sql.append("   AND TVO.ORDER_WEEK BETWEEN "+startWeek+" AND "+endWeek+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE < =TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		sql.append(" GROUP BY TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("          TMD.DEALER_CODE,\n" );
		sql.append("          TMD.DEALER_SHORTNAME,\n" );
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_CODE, TMD2.DEALER_CODE),\n");
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_SHORTNAME, TMD2.DEALER_SHORTNAME),\n" );
		sql.append("          TVO.ORDER_NO,\n" );
		sql.append("          TVO.ORDER_TYPE,\n" );
		sql.append("          TVO.ORDER_YEAR,\n" );
		sql.append("          TVO.ORDER_WEEK,\n" );
		sql.append("          TVD.DELIVERY_NO,TC2.CODE_DESC,\n" );
		sql.append("          TVD.DELIVERY_TYPE,\n" );
		sql.append("          TMVA.ADDRESS,\n" );
		sql.append("          TC3.CODE_DESC,\n" );
		sql.append("          TVD.LOSE_REASON,\n" );
		//sql.append("          XORR.REASON,\n" );
		sql.append("          TVD.ERP_ORDER,\n" );
		sql.append("          TVD.DELIVERY_DATE,TVDD.Billing_Code\n");
		sql.append("ORDER BY DELIVERY_DATE DESC\n");
		//sql.append("          TVOD.SPECIAL_BATCH_NO");
		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName());
		return rs;
	}
	
	/**
	 * 下级经销商端发运指令查询下载
	 * @param startYear
	 * @param startWeek
	 * @param endYear
	 * @param endWeek
	 * @param startDate
	 * @param endDate
	 * @param orderType
	 * @param dealerCode
	 * @param orderNo
	 * @param transportType
	 * @param deliveryStatus
	 * @param groupCode
	 * @param areaIds
	 * @param companyId
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getLowDlrCommandQueryListLoad(String dealerId,String startYear,String startWeek,String endYear,String endWeek,String startDate,String endDate,String orderType,
			String orderNo,String transportType,String deliveryStatus,String groupCode,String areaIds,Long companyId,int curPage,int pageSize) throws Exception{
		List <Object>params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("       TMD.DEALER_CODE CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME AS TTNAME,\n" );
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_CODE, TMD2.DEALER_CODE) AS ORDER_ORG_CODE,\n");
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_SHORTNAME, TMD2.DEALER_SHORTNAME) AS ORDER_ORG_NAME,\n" );
		sql.append("       TVO.ORDER_NO,\n" );
		sql.append("       TVO.ORDER_TYPE,\n" );
		sql.append("       TVO.ORDER_YEAR,\n" );
		sql.append("       TVO.ORDER_WEEK,\n" );
		sql.append("       TVD.DELIVERY_NO,\n" );
		sql.append("       TVDDI.INVOICE_NO INVOICE_NO, \n" );
		sql.append("       TC2.CODE_DESC DELIVERY_TYPE,\n" );
		sql.append("       DECODE (TVD.DELIVERY_TYPE ,"+Constant.TRANSPORT_TYPE_02+",TMVA.ADDRESS,'')ADDRESS,\n");
		sql.append("       TC3.CODE_DESC DELIVERY_STATUS,\n" );
		//sql.append("       TVD.LOSE_REASON,\n" );
		//sql.append("       DECODE(XORR.REASON,null,'',XORR.REASON,'详细原因') LOSE_REASON,\n" );
		sql.append("       TVD.LOSE_REASON LOSE_REASON,\n" );
		sql.append("       TVD.ERP_ORDER,\n" );
		sql.append("       TO_CHAR(TVD.DELIVERY_DATE,'YYYY-MM-DD HH24:MI:SS') DELIVERY_DATE,\n" );
		sql.append("       SUM(NVL(TVDD.MATCH_AMOUNT,0)) MATCH_AMOUNT,\n" );
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) DELIVERY_AMOUNT,\n" );
		sql.append("       SUM(TVDRD.TOTAL_PRICE+TVDRD.DISCOUNT_PRICE) TOTAL_PRICE\n" );
		sql.append("  FROM TT_VS_ORDER              TVO,TT_VS_DLVRY_DTL_INVOICE TVDDI,\n" );
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n" );
		//sql.append("        XXDMS_ORDER_REASON_RETURN XORR,\n" );
		sql.append("       TT_VS_DLVRY              TVD,\n" );
		sql.append("       TT_VS_DLVRY_DTL          TVDD,\n" );
		sql.append("       TM_VS_ADDRESS            TMVA,\n" );
		sql.append("       TM_DEALER                TMD,\n" );
		sql.append("       TT_VS_DLVRY_REQ      	TVDR,\n" );
		sql.append("       TM_DEALER                TMD3,\n" );
		sql.append("       TM_DEALER                TMD2,TC_CODE TC2,TC_CODE TC3\n" );
		sql.append(" WHERE TVO.ORDER_ID = TVD.ORDER_ID\n" );
		sql.append("   AND TVD.REQ_ID = TVDR.REQ_ID\n" );
		sql.append("   AND TVDR.ORDER_DEALER_ID = TMD3.DEALER_ID(+)\n" );
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n" );
		sql.append("   AND TVDRD.DETAIL_ID = TVDD.REQ_DTL_ID\n" );
		sql.append("   AND TVD.DELIVERY_ID=TVDDI.ORDER_ID(+)  AND TC2.CODE_ID=TVD.DELIVERY_TYPE  AND TC3.CODE_ID=TVD.DELIVERY_STATUS  \n");
		//sql.append("   AND  XORR.DMS_ORDER_NUMBER(+)=TVD.DELIVERY_NO\n" );
		sql.append("   AND TVO.AREA_ID IN("+areaIds+")\n" );
		//sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n" );
		sql.append("   AND TVD.ADDRESS_ID = TMVA.ID(+)\n" );
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVO.ORDER_ORG_ID = TMD2.DEALER_ID\n" );
		sql.append("   AND TVDD.DELIVERY_AMOUNT <> 0\n" );
		sql.append("   AND TVD.COMPANY_ID = "+companyId+"\n" );
		//sql.append("   AND TVD.DELIVERY_TYPE != "+Constant.DELIVERY_STATUS_01+"\n" );
		sql.append("   AND (TVO.ORDER_ORG_ID IN ("+dealerId+") OR TVDR.ORDER_DEALER_ID IN ("+dealerId+"))\n" );
		
		if(!"-1".equals(deliveryStatus)&&!"".equals(deliveryStatus)&&deliveryStatus!=null){
			sql.append("   AND TVD.DELIVERY_STATUS = "+deliveryStatus+"\n" );
		}
		if(!"-1".equals(orderType)&&!"".equals(orderType)&&orderType!=null){
			sql.append("   AND TVO.ORDER_TYPE = "+orderType+"\n" );
		}
		if(!"".equals(orderNo)&&orderNo!=null){
			sql.append("   AND TVD.DELIVERY_NO LIKE '%"+orderNo+"%'\n" );
		}
		if(!"-1".equals(transportType)&&!"".equals(transportType)&&transportType!=null){
			sql.append("   AND TVD.DELIVERY_TYPE = "+transportType+"\n" );	
		}
		if(!"".equals(startYear)&&startYear!=null&&!"".equals(endYear)&&endYear!=null){
			sql.append("   AND TVO.ORDER_YEAR BETWEEN "+startYear+" AND "+endYear+"\n" );
		}
		if(!"".equals(startWeek)&&startWeek!=null&&!"".equals(endWeek)&&endWeek!=null){
			sql.append("   AND TVO.ORDER_WEEK BETWEEN "+startWeek+" AND "+endWeek+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE < =TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		sql.append(" GROUP BY TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("          TMD.DEALER_CODE,\n" );
		sql.append("          TMD.DEALER_SHORTNAME,\n" );
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_CODE, TMD2.DEALER_CODE),\n");
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_SHORTNAME, TMD2.DEALER_SHORTNAME),\n" );
		sql.append("          TVO.ORDER_NO,\n" );
		sql.append("          TVO.ORDER_TYPE,\n" );
		sql.append("          TVO.ORDER_YEAR,\n" );
		sql.append("          TVO.ORDER_WEEK,\n" );
		sql.append("          TVD.DELIVERY_NO,TC2.CODE_DESC,\n" );
		sql.append("          TVD.DELIVERY_TYPE,\n" );
		sql.append("          TMVA.ADDRESS,\n" );
		sql.append("          TC3.CODE_DESC,\n" );
		sql.append("          TVD.LOSE_REASON,\n" );
		//sql.append("          XORR.REASON,\n" );
		sql.append("          TVD.ERP_ORDER,\n" );
		sql.append("          TVD.DELIVERY_DATE,TVDD.Billing_Code,TVDDI.INVOICE_NO\n");
		sql.append("ORDER BY DELIVERY_DATE DESC\n");
		//sql.append("          TVOD.SPECIAL_BATCH_NO");
		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName());
		return rs;
	}
	
	public PageResult<Map<String, Object>> getCommandPrintList(String areaIds, String areaId, String orgId, String startDate,String endDate,String deliveryNo, String printFlag, Long companyId,int curPage,int pageSize) throws Exception{
		List <Object>params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("       TMD.DEALER_CODE CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME AS TTNAME,\n" );
		sql.append("       TVO.ORDER_NO,\n" );
		sql.append("       TVO.ORDER_TYPE,\n" );
		sql.append("       TVO.ORDER_YEAR,\n" );
		sql.append("       TVO.ORDER_WEEK,\n" );
		sql.append("       TVD.DELIVERY_NO,\n" );
		sql.append("       TVD.DELIVERY_TYPE,\n" );
		sql.append("       DECODE (TVD.DELIVERY_TYPE ,"+Constant.TRANSPORT_TYPE_02+",TMVA.ADDRESS,'')ADDRESS,\n");
		sql.append("       TVD.ERP_ORDER,\n" );
		sql.append("       TVD.DELIVERY_STATUS,\n" );
		sql.append("       TO_CHAR(TVD.DELIVERY_DATE,'YYYY-MM-DD') DELIVERY_DATE,\n" );
		//sql.append("       DECODE(TVOD.SPECIAL_BATCH_NO,null,' ',TVOD.SPECIAL_BATCH_NO) SPECIAL_BATCH_NO,");
		sql.append("       SUM(NVL(TVDD.MATCH_AMOUNT,0)) MATCH_AMOUNT,\n" );
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) DELIVERY_AMOUNT,\n" );
		sql.append("       SUM(TVDRD.DELIVERY_AMOUNT * TVDRD.SINGLE_PRICE) TOTAL_PRICE\n" );
		sql.append("  FROM TT_VS_ORDER              TVO,\n" );
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n" );
		sql.append("       TT_VS_DLVRY              TVD,\n" );
		sql.append("       TT_VS_DLVRY_DTL          TVDD,\n" );
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n" );
		sql.append("       TM_VS_ADDRESS            TMVA,\n" );
		sql.append("       TM_DEALER                TMD,\n" );
		sql.append("       TM_DEALER_ORG_RELATION   R\n" );
		sql.append(" WHERE TVD.ORDER_ID = TVO.ORDER_ID\n" );
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n" );
		sql.append("   AND TVDD.REQ_DTL_ID = TVDRD.DETAIL_ID\n" );
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n" );
		sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n" );
		sql.append("   AND TVD.ADDRESS_ID = TMVA.ID(+)\n" );
		sql.append("   AND TVO.BILLING_ORG_ID = R.DEALER_ID\n" );
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVD.ERP_ORDER IS NOT NULL\n" );
		sql.append("   AND TVD.DELIVERY_STATUS <> " + Constant.DELIVERY_STATUS_09 +"\n" );
		sql.append("   AND TVO.COMPANY_ID = "+companyId+"\n" );

		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE < =TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(Utility.testString(deliveryNo)){
			sql.append("   AND TVD.DELIVERY_NO LIKE '%").append(deliveryNo).append("%'\n");
		}
		if(Utility.testString(printFlag)){
			sql.append("   AND TVD.PRINT_FLAG = ").append(printFlag).append("\n");
		}
		if(Utility.testString(orgId)){
			sql.append("   AND R.ORG_ID = ").append(orgId).append("\n"); 
		}
		if(null != areaId && !"".equals(areaId)) {
			sql.append("   AND TVO.AREA_ID = ").append(areaId).append("\n"); 
		}
		if (!"".equals(areaIds) && "".equals(areaId)) {
			sql.append("   AND TVO.AREA_ID IN ("+areaIds+")\n");
		}
		sql.append(" GROUP BY TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("          TMD.DEALER_CODE,\n" );
		sql.append("          TMD.DEALER_SHORTNAME,\n" );
		sql.append("          TVO.ORDER_NO,\n" );
		sql.append("          TVO.ORDER_TYPE,\n" );
		sql.append("          TVO.ORDER_YEAR,\n" );
		sql.append("          TVO.ORDER_WEEK,\n" );
		sql.append("          TVD.ERP_ORDER,\n" );
		sql.append("          TVD.DELIVERY_NO,\n" );
		sql.append("          TVD.DELIVERY_TYPE,\n" );
		sql.append("          TMVA.ADDRESS,\n" );
		sql.append("          TVD.DELIVERY_STATUS,\n" );
		sql.append("          TVD.DELIVERY_DATE");
		//sql.append("          TVOD.SPECIAL_BATCH_NO");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	
	/*
	 * 根据公司获取大区信息
	 */
	public List<Map<String, Object>> getOrgList(Long companyId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ORG_ID, ORG_NAME, ORG_DESC\n" );
		sql.append("FROM TM_ORG\n" );
		sql.append("WHERE COMPANY_ID = ").append(companyId).append("\n");
		sql.append("AND DUTY_TYPE = ").append(Constant.DUTY_TYPE_LARGEREGION).append("\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/*
	 * 根据区域获得业务范围
	 */
	public List<Map<String, Object>> getAreaList(Long poseId){
		StringBuffer sql= new StringBuffer();
		
		sql.append("SELECT TBA.AREA_ID, TBA.AREA_CODE, TBA.AREA_NAME, TPBA.POSE_ID\n");
		sql.append("  FROM TM_POSE_BUSINESS_AREA TPBA, TM_BUSINESS_AREA TBA\n");  
		sql.append(" WHERE TPBA.AREA_ID = TBA.AREA_ID\n");  
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");  
		sql.append("   AND TPBA.POSE_ID = " + poseId + "\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * Function         : 订单发运查询
	 * @param           : 订单开始年周
	 * @param           : 订单结束年周
	 * @param           : 发运起始时间
	 * @param           : 发运结束时间
	 * @param           : 订单类型
	 * @param           : 订单号码
	 * @param			：经销商ID
	 * @param           : 物料组CODE
	 * @param           : 运送方式
	 * @param           : 发运状态
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-24
	 */
	public PageResult<Map<String, Object>> getOrderDeliveryQueryList(String areaId, String startYear,String startWeek,String endYear,String endWeek,String startDate,String endDate,String orderType,
			String dealerId,String orderNo,String transportType,String deliveryStatus,String groupCode,String areaIds,Long oemCompanyId,int curPage,int pageSize) throws Exception{
		List <Object>params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		
		sql.append("SELECT A.DELIVERY_ID,\n");
		sql.append("       A.CODE,\n");  
		sql.append("       A.TTNAME,\n");  
		sql.append("       A.DEALER_LEVEL,\n");  
		sql.append("       A.ORDER_NO,\n");  
		sql.append("       A.ORDER_TYPE,\n");  
		sql.append("       A.ORDER_YEAR,\n");  
		sql.append("       A.ORDER_WEEK,\n");  
		sql.append("       A.DELIVERY_NO,\n");  
		sql.append("       A.DELIVERY_TYPE,\n");  
		sql.append("       A.ADDRESS,\n");  
		sql.append("       A.DELIVERY_STATUS,\n");  
		sql.append("       A.DELIVERY_DATE,\n");  
		sql.append("       A.SPECIAL_BATCH_NO,\n");  
		sql.append("       A.MATCH_AMOUNT,\n");  
		sql.append("       A.DELIVERY_AMOUNT,\n");  
		sql.append("       DECODE(A.DEALER_LEVEL, ").append(Constant.DEALER_LEVEL_02).append(", '-', A.TOTAL_PRICE) TOTAL_PRICE\n");  
		sql.append("  FROM (\n");

		sql.append("SELECT TVD.DELIVERY_ID,\n" );
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD1.DEALER_CODE, TMD.DEALER_CODE) CODE,\n" );
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD1.DEALER_SHORTNAME, TMD.DEALER_SHORTNAME) AS TTNAME,\n" );
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD1.DEALER_LEVEL, TMD.DEALER_LEVEL) DEALER_LEVEL,\n" );
		sql.append("       TVO.ORDER_NO,\n" );
		sql.append("       TVO.ORDER_TYPE,\n" );
		sql.append("       TVO.ORDER_YEAR,\n" );
		sql.append("       TVO.ORDER_WEEK,\n" );
		sql.append("       TVD.DELIVERY_NO,\n" );
		sql.append("       TVD.DELIVERY_TYPE,\n" );
		sql.append("       TMVA.ADDRESS,\n" );
		sql.append("       TVD.DELIVERY_STATUS,\n" );
		sql.append("       TO_CHAR(TVD.DELIVERY_DATE,'YYYY-MM-DD') DELIVERY_DATE,\n" );
		sql.append("       DECODE(TVOD.SPECIAL_BATCH_NO,null,' ',TVOD.SPECIAL_BATCH_NO) SPECIAL_BATCH_NO,\n");
		sql.append("       SUM(NVL(TVDD.MATCH_AMOUNT,0))    MATCH_AMOUNT,");
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) DELIVERY_AMOUNT,\n" );
		sql.append("       NVL(SUM(TVDD.DELIVERY_AMOUNT * TVDRD.SINGLE_PRICE), 0) TOTAL_PRICE\n" );
		sql.append("  FROM TT_VS_ORDER              TVO,\n" );
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n" );
		sql.append("       TT_VS_DLVRY              TVD,\n" );
		sql.append("       TT_VS_DLVRY_DTL          TVDD,\n" );
		sql.append("       TM_VS_ADDRESS            TMVA,\n" );
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
			sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n" );
		}
		sql.append("TT_VS_DLVRY_REQ     TVDR,\n");
		sql.append("TT_VS_DLVRY_REQ_DTL TVDRD,\n");  
		sql.append("TM_DEALER           TMD1,\n");
		sql.append("       TM_DEALER                TMD\n" );
		sql.append(" WHERE TVD.ORDER_ID = TVO.ORDER_ID\n" );
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n" );
		sql.append("AND TVD.REQ_ID = TVDR.REQ_ID\n");
		sql.append("AND TVDD.REQ_DTL_ID = TVDRD.DETAIL_ID\n");  
		sql.append("AND TVDR.ORDER_DEALER_ID = TMD1.DEALER_ID(+)\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n" );
		sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n" );
		sql.append("   AND TVD.ADDRESS_ID = TMVA.ID\n" );
		sql.append("   AND TVO.ORDER_ORG_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVO.COMPANY_ID = "+oemCompanyId+"\n" );
		//sql.append("   AND TVD.DELIVERY_TYPE != "+Constant.DELIVERY_STATUS_01+"\n" );
		if(!"".equals(areaIds)&&areaIds!=null && "".equals(areaId)){
			sql.append("   AND TVO.AREA_ID IN("+areaIds+")\n");
		}
		if (!"".equals(areaId)) {
			sql.append("   AND TVO.AREA_ID ="+areaId+"\n");
		}
		if(!"-1".equals(deliveryStatus)&&!"".equals(deliveryStatus)&&deliveryStatus!=null){
			sql.append("   AND TVD.DELIVERY_STATUS = "+deliveryStatus+"\n" );
		}
		if(!"-1".equals(orderType)&&!"".equals(orderType)&&orderType!=null){
			sql.append("   AND TVO.ORDER_TYPE = "+orderType+"\n" );
		}
		if(!"".equals(dealerId)&&dealerId!=null){
			sql.append("   AND ((TMD.DEALER_ID IN ("+dealerId+") AND TVO.ORDER_TYPE <> ").append(Constant.ORDER_TYPE_01).append(") OR (TMD.DEALER_ID IN (").append(dealerId).append(") AND TVO.ORDER_TYPE = ").append(Constant.ORDER_TYPE_01).append(" AND TVDR.ORDER_DEALER_ID IS NULL) OR TMD1.DEALER_ID IN("+dealerId+"))\n");
		}
		if(!"".equals(orderNo)&&orderNo!=null){
			sql.append("   AND TVO.ORDER_NO LIKE '%"+orderNo+"%'\n" );
		}
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
			sql.append("   AND TVMGR.MATERIAL_ID = TVDD.MATERIAL_ID\n" );
			sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n" );
		}
		if(!"-1".equals(transportType)&&!"".equals(transportType)&&transportType!=null){
			sql.append("   AND TVD.DELIVERY_TYPE = "+transportType+"\n" );	
		}
		if(!"".equals(startYear)&&startYear!=null&&!"".equals(endYear)&&endYear!=null){
			sql.append("   AND TVO.ORDER_YEAR BETWEEN "+startYear+" AND "+endYear+"\n" );
		}
		if(!"".equals(startWeek)&&startWeek!=null&&!"".equals(endWeek)&&endWeek!=null){
			sql.append("   AND TVO.ORDER_WEEK BETWEEN "+startWeek+" AND "+endWeek+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE < =TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		sql.append(" GROUP BY TVD.DELIVERY_ID,\n" );
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD1.DEALER_CODE, TMD.DEALER_CODE),\n" );
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD1.DEALER_SHORTNAME, TMD.DEALER_SHORTNAME),\n" );
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD1.DEALER_LEVEL, TMD.DEALER_LEVEL),\n" );
		sql.append("          TVO.ORDER_NO,\n" );
		sql.append("          TVO.ORDER_TYPE,\n" );
		sql.append("          TVO.ORDER_YEAR,\n" );
		sql.append("          TVO.ORDER_WEEK,\n" );
		sql.append("          TVD.DELIVERY_NO,\n" );
		sql.append("          TVD.DELIVERY_TYPE,\n" );
		sql.append("          TMVA.ADDRESS,\n" );
		sql.append("          TVD.DELIVERY_STATUS,\n" );
		sql.append("          TVD.DELIVERY_DATE,\n");
		sql.append("          TVOD.SPECIAL_BATCH_NO\n");
		sql.append(") A\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	
	
	
	public List<Map<String, Object>> getOrderDeliveryQueryListLoad(String areaId, String startYear,String startWeek,String endYear,String endWeek,String startDate,String endDate,String orderType,
			String dealerId,String orderNo,String transportType,String deliveryStatus,String groupCode,String areaIds,Long oemCompanyId,int curPage,int pageSize) throws Exception{
		List <Object>params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.DELIVERY_ID,\n");
		sql.append("       A.CODE,\n");  
		sql.append("       A.TTNAME,\n");  
		sql.append("       A.DEALER_LEVEL,\n");  
		sql.append("       A.ORDER_NO,\n");  
		sql.append("       A.ORDER_TYPE,\n");  
		sql.append("       A.ORDER_YEAR,\n");  
		sql.append("       A.ORDER_WEEK,\n");  
		sql.append("       A.DELIVERY_NO,\n");  
		sql.append("       A.DELIVERY_TYPE,\n");  
		sql.append("       A.ADDRESS,\n");  
		sql.append("       A.DELIVERY_STATUS,\n");  
		sql.append("       A.DELIVERY_DATE,\n");  
		sql.append("       A.SPECIAL_BATCH_NO,\n");  
		sql.append("       A.MATCH_AMOUNT,\n");  
		sql.append("       A.DELIVERY_AMOUNT,\n");  
		sql.append("       DECODE(A.DEALER_LEVEL, ").append(Constant.DEALER_LEVEL_02).append(", '-', A.TOTAL_PRICE) TOTAL_PRICE\n");  
		sql.append("  FROM (\n");
		
		sql.append("SELECT TVD.DELIVERY_ID,\n" );
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD1.DEALER_CODE, TMD.DEALER_CODE) CODE,\n" );
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD1.DEALER_SHORTNAME, TMD.DEALER_SHORTNAME) AS TTNAME,\n" );
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD1.DEALER_LEVEL, TMD.DEALER_LEVEL) DEALER_LEVEL,\n" );
		sql.append("       TVO.ORDER_NO,\n" );
		sql.append("       TC1.CODE_DESC ORDER_TYPE,\n" );
		sql.append("       TVO.ORDER_YEAR,\n" );
		sql.append("       TVO.ORDER_WEEK,\n" );
		sql.append("       TVD.DELIVERY_NO,\n" );
		sql.append("       TC3.CODE_DESC DELIVERY_TYPE,\n" );
		sql.append("       TMVA.ADDRESS,\n" );
		sql.append("       TC2.CODE_DESC DELIVERY_STATUS,\n" );
		sql.append("       TO_CHAR(TVD.DELIVERY_DATE,'YYYY-MM-DD') DELIVERY_DATE,\n" );
		sql.append("       DECODE(TVOD.SPECIAL_BATCH_NO,null,' ',TVOD.SPECIAL_BATCH_NO) SPECIAL_BATCH_NO,\n");
		sql.append("       SUM(NVL(TVDD.MATCH_AMOUNT,0))    MATCH_AMOUNT,");
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) DELIVERY_AMOUNT,\n" );
		sql.append("       NVL(SUM(TVDD.DELIVERY_AMOUNT * TVDRD.SINGLE_PRICE), 0) TOTAL_PRICE\n" );
		sql.append("  FROM TT_VS_ORDER              TVO,\n" );
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n" );
		sql.append("       TT_VS_DLVRY              TVD,\n" );
		sql.append("       TT_VS_DLVRY_DTL          TVDD,\n" );
		sql.append("       TM_VS_ADDRESS            TMVA,\n" );
		
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
			sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n" );
		}
		sql.append("TT_VS_DLVRY_REQ     TVDR,\n");
		sql.append("TT_VS_DLVRY_REQ_DTL TVDRD,\n");  
		sql.append("TM_DEALER           TMD1,\n");
		sql.append("       TM_DEALER                TMD,TC_CODE TC1,TC_CODE TC2,TC_CODE TC3\n" );
		sql.append(" WHERE TVD.ORDER_ID = TVO.ORDER_ID\n" );
		sql.append(" AND TC1.CODE_ID=TVO.ORDER_TYPE AND TC2.CODE_ID=TVD.DELIVERY_STATUS  AND TC3.CODE_ID= TVD.DELIVERY_TYPE \n");
		sql.append("AND TVD.REQ_ID = TVDR.REQ_ID\n");
		sql.append("AND TVDD.REQ_DTL_ID = TVDRD.DETAIL_ID\n");  
		sql.append("AND TVDR.ORDER_DEALER_ID = TMD1.DEALER_ID(+)\n");
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n" );
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n" );
		sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n" );
		sql.append("   AND TVD.ADDRESS_ID = TMVA.ID\n" );
		sql.append("   AND TVO.ORDER_ORG_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVO.COMPANY_ID = "+oemCompanyId+"\n" );
		//sql.append("   AND TVD.DELIVERY_TYPE != "+Constant.DELIVERY_STATUS_01+"\n" );
		if(!"".equals(areaIds)&&areaIds!=null && "".equals(areaId)){
			sql.append("   AND TVO.AREA_ID IN("+areaIds+")\n");
		}
		if (!"".equals(areaId)) {
			sql.append("   AND TVO.AREA_ID ="+areaId+"\n");
		}
		if(!"-1".equals(deliveryStatus)&&!"".equals(deliveryStatus)&&deliveryStatus!=null){
			sql.append("   AND TVD.DELIVERY_STATUS = "+deliveryStatus+"\n" );
		}
		if(!"-1".equals(orderType)&&!"".equals(orderType)&&orderType!=null){
			sql.append("   AND TVO.ORDER_TYPE = "+orderType+"\n" );
		}
		if(!"".equals(dealerId)&&dealerId!=null){
			sql.append("   AND ((TMD.DEALER_ID IN ("+dealerId+") AND TVO.ORDER_TYPE <> ").append(Constant.ORDER_TYPE_01).append(") OR (TMD.DEALER_ID IN (").append(dealerId).append(") AND TVO.ORDER_TYPE = ").append(Constant.ORDER_TYPE_01).append(" AND TVDR.ORDER_DEALER_ID IS NULL) OR TMD1.DEALER_ID IN("+dealerId+"))\n");
		}
		if(!"".equals(orderNo)&&orderNo!=null){
			sql.append("   AND TVO.ORDER_NO LIKE '%"+orderNo+"%'\n" );
		}
		if(!"".equals(groupCode)&&groupCode!=null){
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
			sql.append("   AND TVMGR.MATERIAL_ID = TVDD.MATERIAL_ID\n" );
			sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n" );
		}
		if(!"-1".equals(transportType)&&!"".equals(transportType)&&transportType!=null){
			sql.append("   AND TVD.DELIVERY_TYPE = "+transportType+"\n" );	
		}
		if(!"".equals(startYear)&&startYear!=null&&!"".equals(endYear)&&endYear!=null){
			sql.append("   AND TVO.ORDER_YEAR BETWEEN "+startYear+" AND "+endYear+"\n" );
		}
		if(!"".equals(startWeek)&&startWeek!=null&&!"".equals(endWeek)&&endWeek!=null){
			sql.append("   AND TVO.ORDER_WEEK BETWEEN "+startWeek+" AND "+endWeek+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE < =TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		sql.append(" GROUP BY TVD.DELIVERY_ID,\n" );
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD1.DEALER_CODE, TMD.DEALER_CODE),\n" );
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD1.DEALER_SHORTNAME, TMD.DEALER_SHORTNAME),\n" );
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD1.DEALER_LEVEL, TMD.DEALER_LEVEL),\n" );
		sql.append("          TVO.ORDER_NO,\n" );
		sql.append("          TC1.CODE_DESC,\n" );
		sql.append("          TVO.ORDER_YEAR,\n" );
		sql.append("          TVO.ORDER_WEEK,\n" );
		sql.append("          TVD.DELIVERY_NO,\n" );
		sql.append("          TC3.CODE_DESC,\n" );
		sql.append("          TMVA.ADDRESS,\n" );
		sql.append("          TC2.CODE_DESC,\n" );
		sql.append("          TVD.DELIVERY_DATE,\n");
		sql.append("          TVOD.SPECIAL_BATCH_NO\n");
		sql.append(") A\n");
		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName());
		return rs;
	}
	//得到仓库列表
	public List<Map<String, String>> getWarehouseList(Long companyId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TMW.WAREHOUSE_ID, TMW.WAREHOUSE_NAME\n");
		sql.append("  FROM TM_WAREHOUSE TMW\n");  
		sql.append(" WHERE TMW.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND TMW.COMPANY_ID = "+companyId+"\n");  
		sql.append(" ORDER BY TMW.WAREHOUSE_NAME, TMW.WAREHOUSE_ID\n");

		List<Map<String, String>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public PageResult<Map<String, Object>> getBatchNumberList(Long oemCompanyId,String materialId,String warehouse_id,int pageSize ,int curPage){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TMM.MATERIAL_CODE,\n");
		sql.append("       TMM.MATERIAL_NAME,\n");  
		sql.append("       TMV.SPECIAL_BATCH_NO,\n");  
		sql.append("       COUNT(SPECIAL_BATCH_NO) VEHICLE_NUMBER,\n");  
		sql.append("       TMM.MATERIAL_ID\n");

		sql.append("  FROM TM_VEHICLE TMV, TM_VHCL_MATERIAL TMM, TM_WAREHOUSE TMW\n");  
		sql.append(" WHERE TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_02+"\n");  
		sql.append("   AND TMV.LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n");  
		sql.append("   AND TMV.MATERIAL_ID = TMM.MATERIAL_ID\n");  
		sql.append("   AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = "+materialId+"\n");  
		sql.append("   AND TMV.OEM_COMPANY_ID = "+oemCompanyId+"\n");  

		if (null != warehouse_id && !"".equals(warehouse_id)) {
			sql.append("   AND TMV.WAREHOUSE_ID = "+warehouse_id+"\n");  
		}
		sql.append(" GROUP BY TMM.MATERIAL_CODE, TMM.MATERIAL_NAME, TMV.SPECIAL_BATCH_NO,TMM.MATERIAL_ID\n");  
		sql.append(" ORDER BY TMM.MATERIAL_CODE DESC\n");

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	public Map<String, Object> getdeliveryInfoMap(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT O.DELIVERY_ID, NVL(A.VER, 0) VER, A.REQ_ID, A.AREA_ID, B.AREA_NAME, E.ORG_NAME, F.WAREHOUSE_NAME,\n" );
		sql.append("       D.ORDER_NO, D.ORDER_TYPE, A.DELIVERY_TYPE, D.ORDER_ID, A.FUND_TYPE, \n" );
		sql.append("       D.ORDER_YEAR||'年'||D.ORDER_WEEK||'周' ORDER_WEEK,A.ADDRESS_ID, \n" );
		sql.append("       G.DEALER_NAME DNAME1, DECODE(A.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD.DEALER_NAME, H.DEALER_NAME) DNAME2,DECODE(A.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD.DEALER_TYPE, H.DEALER_TYPE) DEALER_TYPE, G.TAXES_NO, \n" );
		sql.append("       N.TYPE_NAME ACCOUNT_NAME, A.PRICE_ID, NVL(A.OTHER_PRICE_REASON, '') OTHER_PRICE_REASON, \n" );
		sql.append("       TO_CHAR(I.AVAILABLE_AMOUNT, 'FM9999,999,999,990.00') AVAILABLE_AMOUNT,\n" );
		sql.append("       TO_CHAR(A.DISCOUNT, 'FM9999,999,999,990.00') DISCOUNT,\n" );
		sql.append("       NVL(A.DISCOUNT, 0) DCOUNT, D.ORDER_ORG_ID, O.ERP_ORDER,\n" );
		sql.append("       J.ADDRESS, J.RECEIVE_ORG, K.DEALER_NAME DNAME3, A.RECEIVER, \n" );
		sql.append("       NVL(J.LINK_MAN, '') LINK_MAN, NVL(J.TEL, '') TEL, L.PRICE_DESC, DECODE(A.IS_FLEET, 1,'是', '否') IS_FLEET,\n" );
		sql.append("       A.FLEET_ADDRESS, M.FLEET_NAME, D.REFIT_REMARK, A.REQ_REMARK ORDER_REMARK, TO_CHAR(D.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n" );
		sql.append("	   TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI') SDATE, D.BILLING_ORG_ID, G.DEALER_NAME DEALER_NAME1, K.DEALER_NAME DEALER_NAME2, D.PAY_REMARK\n");
		sql.append("FROM TT_VS_DLVRY_REQ A, TM_BUSINESS_AREA B,\n" );
		sql.append("     TM_DEALER_ORG_RELATION C, TT_VS_ORDER D,\n" );
		sql.append("     TM_ORG E, TM_WAREHOUSE F, TM_DEALER G,\n" );
		sql.append("     TM_DEALER H, TM_DEALER TMD, TT_VS_ACCOUNT I, TM_VS_ADDRESS J,\n" );
		sql.append("     TM_DEALER K, vw_TT_VS_PRICE L, TM_FLEET M, TT_VS_ACCOUNT_TYPE N, TT_VS_DLVRY O\n" );
		sql.append("WHERE A.AREA_ID = B.AREA_ID\n" );
		sql.append("AND A.ORDER_ID = D.ORDER_ID\n" );
		sql.append("AND D.BILLING_ORG_ID = C.DEALER_ID(+)\n" );
		sql.append("AND C.ORG_ID = E.ORG_ID(+)\n" );
		sql.append("AND D.BILLING_ORG_ID = G.DEALER_ID\n" );
		sql.append("AND A.ORDER_DEALER_ID = TMD.DEALER_ID(+)\n" );
		sql.append("AND D.ORDER_ORG_ID = H.DEALER_ID\n" );
		sql.append("AND A.FUND_TYPE = N.TYPE_ID\n" );
		sql.append("AND A.FUND_TYPE = I.ACCOUNT_TYPE_ID\n" );
		sql.append("AND D.BILLING_ORG_ID = I.DEALER_ID\n" );
		sql.append("AND A.PRICE_ID = L.PRICE_ID(+)\n" );
		sql.append("AND A.WAREHOUSE_ID = F.WAREHOUSE_ID(+)\n");
		sql.append("AND A.ADDRESS_ID = J.ID(+)\n" );
		sql.append("AND A.FLEET_ID = M.FLEET_ID(+)\n" );
		sql.append("AND A.RECEIVER = K.DEALER_ID(+)\n" );
		sql.append("AND A.REQ_ID = O.REQ_ID(+)\n" );
		sql.append("AND A.REQ_ID = ").append(id).append("\n"); 
		sql.append("ORDER BY O.DELIVERY_DATE ASC\n");
		
		Map<String, Object> map= dao.pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	public Map<String, Object> getdeliveryMap(String reqId){
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql= new StringBuffer("\n");

		sql.append("SELECT NVL(A.VER, 0) VER,\n");
		sql.append("       A.REQ_ID,\n");  
		sql.append("       A.AREA_ID,\n");  
		sql.append("       B.AREA_NAME,\n");  
		sql.append("       E.ORG_NAME,\n");  
		sql.append("       F.WAREHOUSE_ID,\n");  
		sql.append("       F.WAREHOUSE_NAME,\n");  
		sql.append("       D.ORDER_NO,\n");  
		sql.append("       D.ORDER_TYPE,\n");  
		sql.append("       A.DELIVERY_TYPE,\n");  
		sql.append("       D.ORDER_ID,\n");  
		sql.append("       A.FUND_TYPE,\n");  
		sql.append("       D.ORDER_YEAR || '年' || D.ORDER_WEEK || '周' ORDER_WEEK,\n");  
		sql.append("       A.ADDRESS_ID,\n");  
		sql.append("       G.DEALER_NAME DNAME1,\n");  
		sql.append("       DECODE(A.CALL_LEAVEL, ?, TMD.DEALER_NAME, H.DEALER_NAME) DNAME2,\n");  
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("       DECODE(A.CALL_LEAVEL, ?, TMD.DEALER_TYPE, H.DEALER_TYPE) DEALER_TYPE,\n");  
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("       DECODE(A.CALL_LEAVEL, ?, TMD.TAXES_NO, H.TAXES_NO) TAXES_NO,\n");  
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("       N.TYPE_NAME ACCOUNT_NAME,\n");  
		sql.append("       A.PRICE_ID,\n");  
		sql.append("       NVL(A.OTHER_PRICE_REASON, '') OTHER_PRICE_REASON,\n");  
		sql.append("       TO_CHAR(I.AVAILABLE_AMOUNT, 'FM9999,999,999,990.00') AVAILABLE_AMOUNT,\n");  
		sql.append("       TO_CHAR(A.DISCOUNT, 'FM9999,999,999,990.00') DISCOUNT,\n");  
		sql.append("       NVL(A.DISCOUNT, 0) DCOUNT,\n");  
		sql.append("       D.ORDER_ORG_ID,\n");  
		sql.append("       J.ADDRESS,\n");  
		sql.append("       J.RECEIVE_ORG,\n");  
		sql.append("       K.DEALER_SHORTNAME DNAME3,\n");  
		sql.append("       A.RECEIVER,\n");  
		sql.append("       NVL(A.LINK_MAN, '') LINK_MAN,\n");  
		sql.append("       NVL(A.TEL, '') TEL,\n");  
		sql.append("       L.PRICE_DESC,\n");  
		sql.append("       DECODE(D.IS_FLEET, 1, '是', '否') IS_FLEET,\n");  
		sql.append("       A.FLEET_ADDRESS,\n");  
		sql.append("       M.FLEET_NAME,\n");  
		sql.append("       D.REFIT_REMARK,\n");  
		sql.append("       A.REQ_REMARK ORDER_REMARK,\n");  
		sql.append("       TO_CHAR(SYSDATE, 'YYYY/MM/DD') SDATE,\n");  
		sql.append("       D.BILLING_ORG_ID,\n");  
		sql.append("       TO_CHAR(D.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");  
		sql.append("       D.PAY_REMARK\n");  
		sql.append("  FROM TT_VS_DLVRY_REQ        A,\n");  
		sql.append("       TM_BUSINESS_AREA       B,\n");  
		sql.append("       TM_DEALER_ORG_RELATION C,\n");  
		sql.append("       TT_VS_ORDER            D,\n");  
		sql.append("       TM_ORG                 E,\n");  
		sql.append("       TM_WAREHOUSE           F,\n");  
		sql.append("       TM_DEALER              G,\n");  
		sql.append("       TM_DEALER              H,\n");  
		sql.append("       TT_VS_ACCOUNT          I,\n");  
		sql.append("       TM_VS_ADDRESS          J,\n");  
		sql.append("       TM_DEALER              K,\n");  
		sql.append("       vw_TT_VS_PRICE            L,\n");  
		sql.append("       TM_FLEET               M,\n");  
		sql.append("       TM_DEALER              TMD,\n");  
		sql.append("       TT_VS_ACCOUNT_TYPE     N\n");  
		sql.append(" WHERE A.AREA_ID = B.AREA_ID\n");  
		sql.append("   AND A.ORDER_ID = D.ORDER_ID\n");  
		sql.append("   AND D.BILLING_ORG_ID = C.DEALER_ID(+)\n");  
		sql.append("   AND C.ORG_ID = E.ORG_ID(+)\n");  
		sql.append("   AND D.BILLING_ORG_ID = G.DEALER_ID\n");  
		sql.append("   AND D.ORDER_ORG_ID = H.DEALER_ID\n");  
		sql.append("   AND A.ORDER_DEALER_ID = TMD.DEALER_ID(+)\n");  
		sql.append("   AND A.FUND_TYPE = N.TYPE_ID\n");  
		sql.append("   AND A.FUND_TYPE = I.ACCOUNT_TYPE_ID\n");  
		sql.append("   AND D.BILLING_ORG_ID = I.DEALER_ID\n");  
		sql.append("   AND A.PRICE_ID = L.PRICE_ID\n");  
		sql.append("   AND A.WAREHOUSE_ID = F.WAREHOUSE_ID(+)\n");  
		sql.append("   AND A.ADDRESS_ID = J.ID(+)\n");  
		sql.append("   AND A.FLEET_ID = M.FLEET_ID(+)\n");  
		sql.append("   AND A.RECEIVER = K.DEALER_ID\n");  
		sql.append("   AND A.REQ_ID = ?\n");
		
		params.add(reqId) ;

		
		Map<String, Object> map= dao.pageQueryMap(sql.toString(), params, getFunName());
		return map;
	}
	/**
	 * 批次号查询
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getPatchNoSelectQuery(Map<String, Object> map)throws Exception{
		
		String wareHouseId = (String)map.get("wareHouseId");
		String materalId = (String)map.get("materalId");
		String companyId = (String)map.get("companyId");
		//String orderType = (String)map.get("orderType");
		//String specialBatchNo = (String)map.get("specialBatchNo");
		
		StringBuffer sql= new StringBuffer();

		sql.append("SELECT A.MATERIAL_ID,\n");
		sql.append("       A.MATERIAL_CODE,\n");  
		sql.append("       A.MATERIAL_NAME,\n");  
		sql.append("       A.BATCH_NO,\n");  
		sql.append("       B.RESERVE_AMOUNT,\n");  
		sql.append("       A.STOCK_AMOUNT - NVL(B.RESERVE_AMOUNT, 0) AVA_AMOUNT\n");  
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
		sql.append("           AND W.WAREHOUSE_TYPE <> "+Constant.WAREHOUSE_TYPE_04+" --特殊商品库\n");  
		sql.append("           AND TV.ORG_TYPE IS NULL\n");  
		sql.append("           AND TV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_02+" --车厂库存\n");  
		sql.append("           AND TV.LOCK_STATUS = "+Constant.LOCK_STATUS_01+" --正常状态\n");
		/*if(Integer.parseInt(orderType)==Constant.ORDER_TYPE_03){
			sql.append("           AND TV.SPECIAL_BATCH_NO ="+specialBatchNo+"\n"); 
		}
		else{
			sql.append("           AND TV.SPECIAL_BATCH_NO IS NULL\n"); 
		}*/
		sql.append("           AND TV.OEM_COMPANY_ID = "+companyId+"\n");  
		sql.append("           AND TV.WAREHOUSE_ID = "+wareHouseId+"\n");  
		sql.append("           AND TV.MATERIAL_ID = "+materalId+"\n");  
		sql.append("         GROUP BY TV.MATERIAL_ID,\n");
		sql.append("               	  TVM.MATERIAL_CODE,\n");  
		sql.append("                  TVM.MATERIAL_NAME,\n");    
		sql.append("                  TV.BATCH_NO,\n");  
		sql.append("                  TV.OEM_COMPANY_ID,\n");  
		sql.append("                  TV.SPECIAL_BATCH_NO) A,\n");  
		sql.append("       (SELECT OEM_COMPANY_ID COMPANY_ID,\n");  
		sql.append("               MATERIAL_ID,\n");  
		sql.append("               BATCH_NO,\n");  
		sql.append("               (SUM(NVL(AMOUNT, 0)) - SUM(NVL(DELIVERY_AMOUNT, 0))) RESERVE_AMOUNT --保留资源数量\n");  
		sql.append("          FROM TT_VS_ORDER_RESOURCE_RESERVE\n");  
		sql.append("         WHERE RESERVE_STATUS = "+Constant.RESOURCE_RESERVE_STATUS_01+" --\n");  
		sql.append("           AND OEM_COMPANY_ID = "+companyId+"\n");  
		sql.append("           AND WAREHOUSE_ID = "+wareHouseId+"\n");  
		sql.append("           AND MATERIAL_ID = "+materalId+"\n");  
		sql.append("         GROUP BY OEM_COMPANY_ID, MATERIAL_ID, BATCH_NO) B\n");  
		sql.append(" WHERE A.COMPANY_ID = B.COMPANY_ID(+)\n");  
		sql.append("   AND A.MATERIAL_ID = B.MATERIAL_ID(+)\n");  
		sql.append("   AND NVL(A.BATCH_NO, 0) = NVL(B.BATCH_NO(+), 0)\n");
		sql.append("   ORDER BY A.BATCH_NO\n");

		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getorderResourceReserveDetailList(String reqId, String orderType,String companyId,String erpCode){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT tvdrd.detail_id,\n" );
		sql.append("       tvdrd.material_id,\n" );
		sql.append("       tvm.material_code,\n" );
		sql.append("       tvm.material_name,\n" );
		sql.append("       tvm.COLOR_NAME,\n" );
		sql.append("       tvdrd.patch_no special_batch_no,\n" );
		sql.append("       nvl(orr.amount, 0) delivery_amount,\n" );
		sql.append("	   NVL(TVDRD.REQ_AMOUNT, 0) REQ_AMOUNT,");
		sql.append("       tvdrd.order_detail_id,\n" );
		sql.append("       nvl(tvdrd.reserve_amount, 0) reserve_amount,\n" );
		sql.append("       nvl(tvdrd.single_price, 0) single_price,\n" );
		sql.append("       nvl(orr.amount, 0) * nvl(tvdrd.single_price, 0) total_price,\n" );
		sql.append("       nvl(tvdrd.discount_rate, 0) discount_rate,\n" );
		sql.append("       nvl(tvdrd.discount_s_price, 0) discount_s_price,\n" );
		sql.append("	   nvl(orr.amount, 0) * nvl(tvdrd.single_price, 0) *\n" );
		sql.append("	   nvl(tvdrd.discount_rate, 0) / 100 discount_price,\n");
		sql.append("       tvdrd.ver,\n" );
		sql.append("       nvl(vvr.ava_stock, 0) ava_stock,\n" );
		sql.append("       nvl(torder.n_un_remain_amount, 0) general_amount,\n" );
		sql.append("       orr.batch_no\n" );
		sql.append("  FROM tt_vs_order_detail tvod,\n" );
		sql.append("       tt_vs_dlvry_req tvdr,\n" );
		sql.append("       tt_vs_dlvry_req_dtl tvdrd,\n" );
		sql.append("       tm_vhcl_material tvm,\n" );
		sql.append("       tt_vs_order_resource_reserve orr,\n" );
		sql.append("       (SELECT TEMP.MATERIAL_ID, sum(TEMP.AVA_STOCK) AVA_STOCK\n");  
		sql.append("          FROM VW_VS_RESOURCE_ENTITY TEMP\n");  
		sql.append("         WHERE TEMP.COMPANY_ID = "+companyId+"\n");  
		sql.append("           AND TEMP.ENTITY_CODE = "+erpCode+"\n");  
		sql.append("         GROUP BY MATERIAL_ID) VVR,\n");
		sql.append("       (SELECT VVO.MATERIAL_ID, VVO.N_UN_REMAIN_AMOUNT\n");  
		sql.append("          FROM VW_VS_ORDER_ENTITY VVO\n");  
		sql.append("         WHERE VVO.COMPANY_ID = "+companyId+"\n");
		sql.append("           AND VVO.ENTITY_CODE = "+erpCode+") TORDER\n");
		sql.append(" WHERE tvdr.req_id = tvdrd.req_id\n" );
		sql.append("   AND orr.req_detail_id(+) = tvdrd.detail_id\n" );
		sql.append("   AND tvod.detail_id = tvdrd.order_detail_id\n" );
		sql.append("   AND tvdrd.material_id = tvm.material_id\n" );
		sql.append("   AND tvdrd.material_id = vvr.material_id(+)\n" );
		sql.append("   AND tvdrd.material_id = torder.material_id(+)\n" );
		sql.append("   AND tvdrd.req_id = ").append(reqId).append("\n");
		
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public int getBeforVer(String id){
		int ver = 0;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT NVL(VER, 0) VER\n" );
		sql.append("FROM TT_VS_DLVRY_REQ\n" );
		sql.append("WHERE REQ_ID = ").append(id);

		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, getFunName());
		if(map!=null&&map.size()>0){
			ver = Integer.parseInt(String.valueOf(map.get("VER")));
		}
		return ver;
	}
	
	public List<Map<String, Object>> getOrderList(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.DETAIL_ID ODID, A.DELIVERY_AMOUNT\n" );
		sql.append("FROM TT_VS_ORDER_DETAIL A, TT_VS_DLVRY_REQ_DTL B\n" );
		sql.append("WHERE A.DETAIL_ID = B.ORDER_DETAIL_ID\n" );
		sql.append("AND B.REQ_ID = ").append(id);

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public String getDealerIdByEndName(String endName){
		String dealerId = "";
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DEALER_ID\n" );
		sql.append("FROM TM_WAREHOUSE\n" );
		sql.append("WHERE WAREHOUSE_ID = ").append(endName);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		dealerId = String.valueOf(map.get("DEALER_ID"));
		return dealerId;
	}
	
	public List<Map<String, Object>> getPriceList(Map<String, Object> map) {
		String dealerId = (String) map.get("dealerId");
		String companyId = (String) map.get("companyId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVP.PRICE_ID, TVP.PRICE_CODE, TVP.PRICE_DESC, TDPR.IS_DEFAULT\n");
		sql.append("  FROM vw_TT_VS_PRICE TVP, vw_TM_DEALER_PRICE_RELATION TDPR\n");
		sql.append(" WHERE TVP.PRICE_ID = TDPR.PRICE_ID\n");
		sql.append("   AND TVP.START_DATE < SYSDATE\n");
		sql.append("   AND TVP.END_DATE > SYSDATE\n");
		sql.append("   AND TVP.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TDPR.DEALER_ID = " + dealerId + "\n");
		sql.append("   ORDER BY TDPR.IS_DEFAULT ASC	");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getMaterialList(Map<String, Object> map){
		StringBuffer sql = new StringBuffer();
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public PageResult<Map<String, Object>> getPassageQuery(String deliveryNo, String areaIds, String areaId,String dealerCode,String sendcarNo, String erpNo,String regionId,String pageOrgId,String dutyType,Long orgId, int curPage, int pageSize) {
		
		boolean orgFlag = Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType) || (!CommonUtils.isNullString(pageOrgId) && Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType) && !orgId.toString().equals(pageOrgId)) ;
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, XX.*,\n" );
		sql.append("     (CASE\n" );
		sql.append("       WHEN (XX.DE_AMOUNT - XX.DEING_AMOUNT) = 0 THEN\n" );
		sql.append("        '在途'\n" );
		sql.append("       WHEN (((XX.DE_AMOUNT - XX.DEING_AMOUNT) > 0) AND\n" );
		sql.append("            (XX.DEING_AMOUNT > 0)) THEN\n" );
		sql.append("        '部分验收'\n" );
		sql.append("       ELSE\n" );
		sql.append("        '全部验收'\n" );
		sql.append("     END) STATUS\n" );
		sql.append("FROM (SELECT DE.SENDCAR_ORDER_NUMBER,\n" );
		sql.append("             DE.ORDER_NUMBER,\n" );
		sql.append("             D.DELIVERY_TYPE,\n" );
		sql.append("             D.DELIVERY_NO,\n" );
		sql.append("             D.DELIVERY_ID,\n" );
		sql.append("             TO_CHAR(DE.FLATCAR_ASSIGN_DATE, 'YYYY-MM-DD') FLATCAR_ASSIGN_DATE,\n" );
		sql.append("             TO_CHAR(D.FINANCE_CHK_DATE, 'YYYY-MM-DD') FINANCE_CHK_DATE,\n" );
		sql.append("             DE.SHIP_METHOD_CODE,\n" );
		sql.append("             COUNT(DEDL.DETAIL_ID) DE_AMOUNT,\n" );
		sql.append("             SUM(CASE\n" );
		sql.append("                   WHEN LIFE_CYCLE = 10321005 THEN\n" );
		sql.append("                    1\n" );
		sql.append("                   ELSE\n" );
		sql.append("                    0\n" );
		sql.append("                 END) DEING_AMOUNT,\n" );
		sql.append("             DE.FLATCAR_ID,\n" );
		sql.append("             W.WAREHOUSE_NAME,\n" );
		sql.append("             KP.DEALER_NAME AS BILLING_DLR_NAME,\n" );
		sql.append("             DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD.DEALER_NAME, CG.DEALER_NAME) AS ORDER_DLR_NAME,\n" );
		sql.append("             SC.DEALER_NAME AS RECEIVE_DLR_NAME,\n" );
		sql.append("             VA.ADDRESS,\n" );
		sql.append("             DE.MOTORMAN,\n" );
		sql.append("             DE.MOTORMAN_PHONE\n" );
		sql.append("        FROM TT_VS_DLVRY_ERP DE,\n" );
		sql.append("             TT_VS_DLVRY D,\n" );
		sql.append("             TT_VS_ORDER O,\n" );
		sql.append("             TM_WAREHOUSE W,\n" );
		sql.append("TT_VS_DLVRY_REQ TVDR,\n");
		sql.append("TM_DEALER TMD,\n");
		sql.append("             TM_DEALER CG,\n" );
		sql.append("             TM_DEALER KP,\n" );
		sql.append("             TM_DEALER SC,\n" );
		sql.append("             TM_VS_ADDRESS VA,\n" );
		sql.append("             TT_VS_DLVRY_ERP_DTL DEDL,\n" );
		if(orgFlag) {
			sql.append("	   VW_ORG_DEALER           VOD,\n");
		}
		sql.append("             (SELECT * FROM TM_VEHICLE V WHERE v.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_05+") V\n" );
		
		sql.append("       WHERE DE.DELIVERY_ID = D.DELIVERY_ID\n" );
		sql.append("AND D.REQ_ID = TVDR.REQ_ID\n");
		sql.append("AND TVDR.ORDER_DEALER_ID = TMD.DEALER_ID(+)\n");
		sql.append("         AND O.ORDER_ID = D.ORDER_ID\n" );
		sql.append("         AND W.WAREHOUSE_ID(+) = D.WAREHOUSE_ID\n" );
		sql.append("         AND V.VIN = DEDL.VIN\n" );
		sql.append("         AND DEDL.SENDCAR_HEADER_ID = DE.SENDCAR_HEADER_ID\n" );
		sql.append("         AND CG.DEALER_ID = O.ORDER_ORG_ID\n" );
		sql.append("         AND KP.DEALER_ID = O.BILLING_ORG_ID\n" );
		sql.append("         AND SC.DEALER_ID = D.RECEIVER\n" );
		sql.append("         AND VA.ID = D.ADDRESS_ID\n" );
		if(Utility.testString(dealerCode)){
			sql.append("AND KP.DEALER_CODE IN (").append(dealerCode).append(")\n"); 
		}
		if(Utility.testString(sendcarNo)){
			sql.append("AND DE.SENDCAR_ORDER_NUMBER LIKE '%").append(sendcarNo).append("%'\n");
		}
		if(Utility.testString(erpNo)){
			sql.append("AND DE.ORDER_NUMBER LIKE '%").append(erpNo).append("%'\n");
		}
		if (!"".equals(areaId)) {
			sql.append("AND O.AREA_ID = "+areaId+"\n");
		}
		if (!"".equals(areaIds) && "".equals(areaId)) {
			sql.append("AND O.AREA_ID IN ("+areaIds+")\n");
		}
		if (!CommonUtils.isNullString(regionId)) {
			sql.append("AND KP.PROVINCE_ID = ").append(regionId).append("\n");
		}
		if(orgFlag) {
			sql.append("AND O.BILLING_ORG_ID = VOD.DEALER_ID\n");
			
			if(!CommonUtils.isNullString(pageOrgId)) {
				orgId = Long.parseLong(pageOrgId) ;
			}
			
			sql.append("AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		
		if (!CommonUtils.isNullString(deliveryNo)) {
			sql.append("AND D.DELIVERY_NO LIKE '%").append(deliveryNo).append("%'\n");
		}
		
		sql.append("       GROUP BY DE.SENDCAR_ORDER_NUMBER,\n" );
		sql.append("                DE.ORDER_NUMBER,\n" );
		sql.append("                D.DELIVERY_TYPE,\n" );
		sql.append("                D.DELIVERY_NO,\n" );
		sql.append("             D.DELIVERY_ID,\n" );
		sql.append("                DE.FLATCAR_ASSIGN_DATE,\n" );
		sql.append("                D.FINANCE_CHK_DATE,\n" );
		sql.append("                DE.SHIP_METHOD_CODE,\n" );
		sql.append("                DE.FLATCAR_ID,\n" );
		sql.append("                W.WAREHOUSE_NAME,\n" );
		sql.append("                KP.DEALER_NAME,\n" );
		sql.append("                DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD.DEALER_NAME, CG.DEALER_NAME),\n" );
		sql.append("                SC.DEALER_NAME,\n" );
		sql.append("                VA.ADDRESS,\n" );
		sql.append("                DE.MOTORMAN,\n" );
		sql.append("                DE.MOTORMAN_PHONE) XX\n");

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
public PageResult<Map<String, Object>> getTraceDlyQuery(String sqryDate,String eqryDate,String deliveryNo, String areaIds, String areaId,String dealerCode,String sendcarNo, String erpNo,String regionId,String pageOrgId,String dutyType,Long orgId, int curPage, int pageSize) {
		
		boolean orgFlag = Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType) || (!CommonUtils.isNullString(pageOrgId) && Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType) && !orgId.toString().equals(pageOrgId)) ;
		
		//String  subsql=" AND TO_CHAR(OTIS1.CREATED_DATE,'YYYY-MM-DD') >= '"+sqryDate+"'  AND TO_CHAR(OTIS1.CREATED_DATE,'YYYY-MM-DD') <= '"+eqryDate+"' \n";
		StringBuffer sql= new StringBuffer();
	      sql.append("SELECT DISTINCT OTID.VIN   VIN, \n" );
	      sql.append("       OTI.TRANSIT_ID          ID, \n" );
	      sql.append("       OTI.SALE_ORDER_NO       ERP_ORDER_NUMBER, \n" );
	      sql.append("       OTI.PURCHASE_ORDER_NO   DMS_ORDER_NUMBER, \n" );
	      sql.append("       OTI.RECEIPT_NO          SENDCAR_ORDER_NUMBER, \n" );
	      sql.append("        OTI.DISTRIBUTOR_NAME    BILLING_DLR_NAME, \n" );
	      sql.append("        OTI.DESTINATION_NAME    RECEIVE_DLR_NAME, \n" );
	      sql.append("        OTI.DESTINATION_ADDRESS ADDRESS, \n" );
	      sql.append("        OTID.AIC                MS_MODEL_CODE, \n" );
	      sql.append("        (SELECT TO_CHAR(TVO.RAISE_DATE,'YYYY-MM-DD') FROM TT_VS_ORDER TVO WHERE TVO.ORDER_ID=TVD.ORDER_ID) RAISE_DATE, \n" );
	      sql.append("        (SELECT TMG.GROUP_NAME  FROM  TM_VHCL_MATERIAL_GROUP TMG, TM_VEHICLE TMV WHERE TMG.GROUP_ID=TMV.MODEL_ID AND TMV.VIN=OTID.VIN )  MODEL_NAME, \n" ); 
	      sql.append("        TO_CHAR(TVD.BILLING_DATE,'YYYY-MM-DD HH24:MI') BILLING_DATE, \n" );
	      sql.append("        ( SELECT TEO.SHIP_WAY FROM  XXONT_OE_ORDER_HEADERS TEO WHERE TEO.ORDER_NUMBER = TVD.ERP_ORDER AND TEO.ORG_ID=726/*重庆轿车*/ AND TEO.USE_FLAG = 1 /*可用*/ ) DELIVERY_TYPE, \n" );
	      sql.append("        TO_CHAR(TS.SET_TRANS_DATE,'YYYY-MM-DD HH24:MI') DELIVERY_ASSIGN_DATE, \n" );
	      sql.append("        TO_CHAR(TS.OUTPUT_DATE,'YYYY-MM-DD HH24:MI') EXPORT_DATE, \n" );
	      sql.append("        OTI.DEPARTURE_DATE DEPARTURE_DATE, \n" );
	      sql.append("        OTI.PLAN_ARRIVE_DATE PLAN_ARRIVE_DATE, \n" );
	      sql.append("        TO_CHAR(OTI.ACTUAL_DELIVERY_DATE,'YYYY-MM-DD')  ARRIVE_DATE, \n" );
	      sql.append("        (SELECT TVI.ARRIVE_DATE FROM TT_VS_INSPECTION TVI,TM_VEHICLE TMV WHERE TVI.VEHICLE_ID=TMV.VEHICLE_ID AND TMV.VIN=OTID.VIN ) DMS_INSPE_DATE,  \n" );

	      sql.append("        '2' EXPORT_STANDARD, \n" );
	      sql.append("        OTI.PLAN_ARRIVE_DATE-OTI.DEPARTURE_DATE  DELIVERY_STANDARD, \n" );
	      sql.append("        TS.OUTPUT_DATE, \n" );
	      sql.append("        TS.SET_TRANS_DATE, \n" );
	      sql.append("        DECODE(TS.OUTPUT_DATE,NULL,DECODE(TS.SET_TRANS_DATE,NULL,-100,'',-100,ROUND(SYSDATE - TS.SET_TRANS_DATE)),DECODE(TS.SET_TRANS_DATE,NULL,-100,'',-100,DECODE(SIGN(TS.OUTPUT_DATE - TS.SET_TRANS_DATE - 2), -1, 0,ROUND(TS.OUTPUT_DATE - TS.SET_TRANS_DATE) - 2))) EXPORT_DELAYED,  \n" );
	      sql.append("        DECODE(OTI.ACTUAL_DELIVERY_DATE, NULL,(DECODE(OTI.PLAN_ARRIVE_DATE,NULL,-100,'',-100,(DECODE(SIGN(SYSDATE - OTI.PLAN_ARRIVE_DATE),-1,0,0,0,1,FLOOR(SYSDATE  - OTI.PLAN_ARRIVE_DATE/*时间未精确，所以向下取整*/))))),DECODE(SIGN(OTI.ACTUAL_DELIVERY_DATE - OTI.DEPARTURE_DATE - (OTI.PLAN_ARRIVE_DATE - OTI.DEPARTURE_DATE)), -1, 0, ROUND(OTI.ACTUAL_DELIVERY_DATE - OTI.DEPARTURE_DATE - (OTI.PLAN_ARRIVE_DATE - OTI.DEPARTURE_DATE)))) DELIVERY_DELAYED  \n" );
	      sql.append("  FROM OTD_TRANSIT_INFO OTI, \n" );
	      sql.append("       OTD_TRANSIT_DETAIL_INFO OTID, \n" );
	      sql.append("       TT_VS_DLVRY TVD, \n" );
	      sql.append("       TT_VS_DLVRY_ERP DE,  \n" );
	      sql.append("       TT_VS_ORDER TVO,  \n" );
		  if(orgFlag) {
			  sql.append("	 VW_ORG_DEALER           VOD,\n");
		  }
		  sql.append("             TM_DEALER TMD,\n" );
	      sql.append("       (SELECT OTIS1.TRANSIT_ID, OTIS1.SALE_ORDER_NO, (SELECT MAX(TEO.SHIP_DAY) /*,'YYYY-MM-DD HH24:MI:SS')*/ FROM  XXONT_OE_ORDER_HEADERS TEO \n" );
	      sql.append("               WHERE TEO.ORDER_NUMBER = OTIS1.SALE_ORDER_NO AND TEO.ORG_ID = 726 /*重庆轿车*/ AND TEO.USE_FLAG = 1 /*可用*/ ) SET_TRANS_DATE, \n" );
	      sql.append("       (SELECT /* TO_CHAR(*/ MIN(TEOB.CREATION_DATE) /*, 'YYYY-MM-DD HH24:MI:SS')*/ FROM XXINV_MOVE_ORDER_BARCODES TEOB \n" );
	      sql.append("               WHERE TEOB.ORGANIZATION_ID IN (SELECT DISTINCT TDIV.ORGANIZATION_ID FROM  XXDMS_INVENTORY_V TDIV) AND TEOB.ORDER_NUMBER = OTIS1.SALE_ORDER_NO) OUTPUT_DATE \n" );
	      sql.append("               FROM OTD_TRANSIT_INFO OTIS1 WHERE 1 = 1 AND  OTIS1.PROJECTID = 'CAJ' ) TS \n" );   
	      sql.append("   WHERE OTID.PROJECTID = 'CAJ' \n" );
		  sql.append("   AND OTID.RECEIPT_NO = OTI.RECEIPT_NO \n" );
	      sql.append("   AND TVD.ERP_ORDER = OTI.SALE_ORDER_NO \n" );
	      sql.append("   AND TS.TRANSIT_ID = OTI.TRANSIT_ID  \n" );
	      sql.append("   AND OTI.PROJECTID = 'CAJ' \n" );
	      sql.append("   AND DE.DELIVERY_ID = TVD.DELIVERY_ID \n" );
	      sql.append("   AND TVO.ORDER_ID = TVD.ORDER_ID  \n" );
	      sql.append("   AND TMD.DEALER_ID = TVO.BILLING_ORG_ID \n" );
	      sql.append("	 AND TVD.BILLING_DATE >= TO_DATE('"+sqryDate+"','YYYY-MM-DD')   AND TVD.BILLING_DATE <= TO_DATE('"+eqryDate+"','YYYY-MM-DD') \n");
	      if(Utility.testString(dealerCode)){
			sql.append("AND TMD.DEALER_CODE IN (").append(dealerCode).append(")\n"); 
	      }
	      if(Utility.testString(sendcarNo)){
			sql.append("AND /* DE.SENDCAR_ORDER_NUMBER */ OTI.RECEIPT_NO LIKE '%").append(sendcarNo).append("%'\n");
	      }
	      if(Utility.testString(erpNo)){
			sql.append("AND DE.ORDER_NUMBER LIKE '%").append(erpNo).append("%'\n");
	      }
	      if (!"".equals(areaId)) {
			sql.append("AND TVO.AREA_ID = "+areaId+"\n");
	      }
	      if (!"".equals(areaIds) && "".equals(areaId)) {
			sql.append("AND TVO.AREA_ID IN ("+areaIds+")\n");
	      }
	      if (!CommonUtils.isNullString(regionId)) {
			sql.append("AND TMD.PROVINCE_ID = ").append(regionId).append("\n");
	      }
	      if(orgFlag) {
			sql.append("AND TVO.BILLING_ORG_ID = VOD.DEALER_ID\n");
			
			if(!CommonUtils.isNullString(pageOrgId)) {
				orgId = Long.parseLong(pageOrgId) ;
			}
			
			sql.append("AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		
		if (!CommonUtils.isNullString(deliveryNo)) {
			sql.append("AND TVD.DELIVERY_NO LIKE '%").append(deliveryNo).append("%'\n");
		}

		System.out.println("新添加查询的语句是："+sql.toString());

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	public PageResult<Map<String, Object>> getDealerPassageQuery(String areaIds, String areaId, String dealerId,String sendcarNo, String erpNo, int curPage, int pageSize,String startTime,String endTime,String orderNo,String delivery_status) {
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql= new StringBuffer("\n");
		
		sql.append("SELECT ROWNUM NUM,\n");
		sql.append("       XX.*,\n");  
		sql.append("       (CASE\n");  
		sql.append("         WHEN (XX.DE_AMOUNT - XX.DEING_AMOUNT) = 0 THEN\n");  
		sql.append("          '在途'\n");  
		sql.append("         WHEN (((XX.DE_AMOUNT - XX.DEING_AMOUNT) > 0) AND\n");  
		sql.append("              (XX.DEING_AMOUNT > 0)) THEN\n");  
		sql.append("          '部分验收'\n");  
		sql.append("         ELSE\n");  
		sql.append("          '全部验收'\n");  
		sql.append("       END) STATUS\n");  
		sql.append("  FROM (SELECT DE.SENDCAR_ORDER_NUMBER,\n");  
		sql.append("               DE.ORDER_NUMBER,\n");  
		sql.append("               d.DELIVERY_TYPE,\n");  
		sql.append("               D.DELIVERY_NO,\n");  
		sql.append("               D.DELIVERY_ID,\n");  
		sql.append("               TO_CHAR(DE.FLATCAR_ASSIGN_DATE, 'YYYY-MM-DD') FLATCAR_ASSIGN_DATE,\n");  
		sql.append("               TO_CHAR(D.FINANCE_CHK_DATE, 'YYYY-MM-DD') FINANCE_CHK_DATE,\n");  
		sql.append("               DE.SHIP_METHOD_CODE,\n");  
		sql.append("               COUNT(DEDL.DETAIL_ID) DE_AMOUNT,\n");  
		sql.append("               SUM(CASE\n");  
		sql.append("                     WHEN LIFE_CYCLE = ? THEN\n");  
		sql.append("                      1\n");  
		sql.append("                     ELSE\n");  
		sql.append("                      0\n");  
		sql.append("                   END) DEING_AMOUNT,\n");
		params.add(Constant.VEHICLE_LIFE_05) ;
		
		sql.append("               DE.FLATCAR_ID,\n");  
		sql.append("               W.WAREHOUSE_NAME,\n");  
		sql.append("               KP.DEALER_NAME AS BILLING_DLR_NAME,\n");  
		sql.append("               decode(tdr.call_leavel,\n");  
		sql.append("                      ?,\n");  
		sql.append("                      cg1.dealer_name,\n");  
		sql.append("                      CG.DEALER_NAME) AS ORDER_DLR_NAME,\n");  
		params.add(Constant.DEALER_LEVEL_02) ;
		
		sql.append("               o.order_org_id,\n");
		sql.append("               o.billing_org_id,\n");  
		sql.append("               d.receiver,\n");
		
		sql.append("               SC.DEALER_NAME AS RECEIVE_DLR_NAME,\n");  
		sql.append("               VA.ADDRESS,\n");  
		sql.append("               DE.MOTORMAN,\n");  
		sql.append("               DE.MOTORMAN_PHONE\n");  
		sql.append("          FROM TT_VS_DLVRY_ERP     DE,\n");  
		sql.append("               TT_VS_DLVRY         D,\n");  
		sql.append("               tt_vs_dlvry_req     tdr,\n");  
		sql.append("               TT_VS_ORDER         O,\n");  
		sql.append("               TM_WAREHOUSE        W,\n");  
		sql.append("               TM_DEALER           CG,\n");  
		sql.append("               tm_dealer           cg1,\n");  
		sql.append("               TM_DEALER           KP,\n");  
		sql.append("               TM_DEALER           SC,\n");  
		sql.append("               TM_VS_ADDRESS       VA,\n");  
		sql.append("               TT_VS_DLVRY_ERP_DTL DEDL,\n");  
		sql.append("               TM_VEHICLE          V\n");  
		sql.append("         WHERE DE.DELIVERY_ID = D.DELIVERY_ID\n");  
		sql.append("           and d.req_id = tdr.req_id\n");  
		sql.append("           AND o.order_id = tdr.order_id\n");  
		sql.append("           AND W.WAREHOUSE_ID(+) = D.WAREHOUSE_ID\n");  
		sql.append("           AND V.VIN = DEDL.VIN\n");  
		sql.append("           AND DEDL.SENDCAR_HEADER_ID = DE.SENDCAR_HEADER_ID\n");  
		sql.append("           AND CG.DEALER_ID = O.ORDER_ORG_ID\n");  
		sql.append("           AND KP.DEALER_ID = d.BILLING_side\n");  
		sql.append("           AND SC.DEALER_ID = d.RECEIVER\n");  
		sql.append("           and tdr.order_dealer_id = cg1.dealer_id(+)\n");  
		sql.append("           AND VA.ID(+) = D.Address_Id\n");

		/*if(Utility.testString(dealerId)){
			sql.append("AND ((tdr.order_dealer_id in (" + dealerId + ") or\n");
			sql.append("              (o.order_org_id in (" + dealerId + ") and\n");  
			sql.append("              tdr.call_leavel is null)) OR\n");  
			sql.append("              O.BILLING_ORG_ID IN (" + dealerId + "))\n");
		}*/
		if(Utility.testString(sendcarNo)){
			sql.append("AND DE.SENDCAR_ORDER_NUMBER LIKE '%").append(sendcarNo).append("%'\n");
		}
		if(Utility.testString(erpNo)){
			sql.append("AND DE.ORDER_NUMBER LIKE '%").append(erpNo).append("%'\n");
		}
		if (!"".equals(areaId)) {
			sql.append("AND O.AREA_ID = "+areaId+"\n");
		}
		if (!"".equals(areaIds) && "".equals(areaId)) {
			sql.append("AND O.AREA_ID IN ("+areaIds+")\n");
		}
		if(Utility.testString(startTime)){
			sql.append(" AND  D.BILLING_DATE>=TO_DATE('"+startTime+"','yyyy-MM-dd HH24:MI:SS')  ");
		}
		if(Utility.testString(endTime)){
			sql.append(" AND  D.BILLING_DATE<=TO_DATE('"+endTime+"','yyyy-MM-dd HH24:MI:SS')  ");
		}
		if(Utility.testString(orderNo)){
			sql.append(" AND  O.ORDER_NO like '%"+orderNo+"%' ");
		}
		if(Utility.testString(delivery_status)){
			sql.append(" AND  D.DELIVERY_STATUS = '"+delivery_status+"' ");
		}
		
		
		sql.append("       GROUP BY DE.SENDCAR_ORDER_NUMBER,\n" );
		sql.append("                cg1.dealer_name,\n" );
		sql.append("                tdr.call_leavel,\n" );
		sql.append("                DE.ORDER_NUMBER,\n" );
		sql.append("                d.DELIVERY_TYPE,\n" );
		sql.append("                D.DELIVERY_NO,\n" );
		sql.append("               D.DELIVERY_ID,\n");  
		sql.append("                DE.FLATCAR_ASSIGN_DATE,\n" );
		sql.append("                D.FINANCE_CHK_DATE,\n" );
		sql.append("                DE.SHIP_METHOD_CODE,\n" );
		sql.append("                DE.FLATCAR_ID,\n" );
		sql.append("                W.WAREHOUSE_NAME,\n" );
		sql.append("                KP.DEALER_NAME,\n" );
		sql.append("                CG.DEALER_NAME,\n" );
		sql.append("			   o.order_org_id,\n");
		sql.append("               o.billing_org_id,\n");  
		sql.append("               d.receiver,\n");
		sql.append("                SC.DEALER_NAME,\n" );
		sql.append("                VA.ADDRESS,\n" );
		sql.append("                DE.MOTORMAN,\n" );
		sql.append("                DE.MOTORMAN_PHONE) XX\n");
		
		if(Utility.testString(dealerId)){
			sql.append("       where (xx.order_org_id in (").append(dealerId).append(") or\n");
			sql.append("       		 xx.billing_org_id in (").append(dealerId).append(") or\n");  
			sql.append("       		 xx.receiver in (").append(dealerId).append("))\n");
		}


		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	
	/**
	 * 发运指令取消查询
	 * @param startYear
	 * @param startWeek
	 * @param endYear
	 * @param endWeek
	 * @param startDate
	 * @param endDate
	 * @param orderType
	 * @param dealerCode
	 * @param orderNo
	 * @param transportType
	 * @param deliveryStatus
	 * @param groupCode
	 * @param areaIds
	 * @param companyId
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getDeliveryCommandCancelQueryList(String startYear,String startWeek,String endYear,String endWeek,String startDate,String endDate,String orderType,
			String dealerCode,String orderNo,String transportType,String deliveryStatus,String groupCode,String areaIds,Long companyId,int curPage,int pageSize) throws Exception{
		List <Object>params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("       TMD.DEALER_CODE CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME AS TTNAME,\n" );
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_SHORTNAME, TMD2.DEALER_SHORTNAME) AS ORDER_ORG_NAME,\n");
		sql.append("       TVO.ORDER_NO,\n" );
		sql.append("       TVO.ORDER_TYPE,\n" );
		sql.append("       TVO.ORDER_YEAR,\n" );
		sql.append("       TVO.ORDER_WEEK,\n" );
		sql.append("       TVD.DELIVERY_NO,\n" );
		sql.append("       TVD.DELIVERY_TYPE,\n" );
		sql.append("       DECODE (TVD.DELIVERY_TYPE ,"+Constant.TRANSPORT_TYPE_02+",TMVA.ADDRESS,'')ADDRESS,\n");
		sql.append("       TVD.DELIVERY_STATUS,\n" );
		sql.append("       TVD.LOSE_REASON,\n" );
		sql.append("       TVD.ERP_ORDER,\n" );
		sql.append("       TO_CHAR(TVD.DELIVERY_DATE,'YYYY-MM-DD HH24:MI:SS') DELIVERY_DATE,\n" );
		//sql.append("       DECODE(TVOD.SPECIAL_BATCH_NO,null,' ',TVOD.SPECIAL_BATCH_NO) SPECIAL_BATCH_NO,");
		sql.append("       SUM(NVL(TVDD.MATCH_AMOUNT,0)) MATCH_AMOUNT,\n" );
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) DELIVERY_AMOUNT,\n" );
		sql.append("       SUM(TVDRD.TOTAL_PRICE+TVDRD.DISCOUNT_PRICE) TOTAL_PRICE\n" );
		sql.append("  FROM TT_VS_ORDER              TVO,\n" );
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n" );
		sql.append("       TT_VS_DLVRY_REQ          TVDR,\n");
		sql.append("       TT_VS_DLVRY              TVD,\n" );
		sql.append("       TT_VS_DLVRY_DTL          TVDD,\n" );
		sql.append("       TM_VS_ADDRESS            TMVA,\n" );
		sql.append("       TM_DEALER                TMD,\n" );
		sql.append("       TM_DEALER                TMD3,\n");
		sql.append("       TM_DEALER                TMD2\n" );
		sql.append(" WHERE TVO.ORDER_ID = TVD.ORDER_ID\n" );
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n" );
		sql.append("   AND TVDRD.DETAIL_ID = TVDD.REQ_DTL_ID\n" );
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");
		sql.append("   AND TVDR.ORDER_DEALER_ID = TMD3.DEALER_ID(+)\n");
		sql.append("   AND TVO.AREA_ID IN("+areaIds+")\n" );
		//sql.append("   AND TVOD.MATERIAL_ID = TVDD.MATERIAL_ID\n" );
		sql.append("   AND TVD.ADDRESS_ID = TMVA.ID(+)\n" );
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVO.ORDER_ORG_ID = TMD2.DEALER_ID\n" );
		sql.append("   AND TVO.COMPANY_ID = "+companyId+"\n" );
		sql.append("   AND TVD.DELIVERY_STATUS IN ("+Constant.DELIVERY_STATUS_03+", "+Constant.DELIVERY_STATUS_04+", "+Constant.DELIVERY_STATUS_05+", "+Constant.DELIVERY_STATUS_07+", "+Constant.DELIVERY_STATUS_08+", "+Constant.DELIVERY_STATUS_10+", "+Constant.DELIVERY_STATUS_11+", "+Constant.DELIVERY_STATUS_12+")\n" );
		
		if(!"-1".equals(deliveryStatus)&&!"".equals(deliveryStatus)&&deliveryStatus!=null){
			sql.append("   AND TVD.DELIVERY_STATUS = "+deliveryStatus+"\n" );
		}
		if(!"-1".equals(orderType)&&!"".equals(orderType)&&orderType!=null){
			sql.append("   AND TVO.ORDER_TYPE = "+orderType+"\n" );
		}
		if(!"".equals(dealerCode)&&dealerCode!=null){
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		if(!"".equals(orderNo)&&orderNo!=null){
			sql.append("   AND TVD.DELIVERY_NO LIKE '%"+orderNo+"%'\n" );
		}
		if(!"-1".equals(transportType)&&!"".equals(transportType)&&transportType!=null){
			sql.append("   AND TVD.DELIVERY_TYPE = "+transportType+"\n" );	
		}
		if(!"".equals(startYear)&&startYear!=null&&!"".equals(endYear)&&endYear!=null){
			sql.append("   AND TVO.ORDER_YEAR BETWEEN "+startYear+" AND "+endYear+"\n" );
		}
		if(!"".equals(startWeek)&&startWeek!=null&&!"".equals(endWeek)&&endWeek!=null){
			sql.append("   AND TVO.ORDER_WEEK BETWEEN "+startWeek+" AND "+endWeek+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE < =TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		sql.append(" GROUP BY TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("          TMD.DEALER_CODE,\n" );
		sql.append("          TMD.DEALER_SHORTNAME,\n" );
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_SHORTNAME, TMD2.DEALER_SHORTNAME) ,\n");
		sql.append("          TVO.ORDER_NO,\n" );
		sql.append("          TVO.ORDER_TYPE,\n" );
		sql.append("          TVO.ORDER_YEAR,\n" );
		sql.append("          TVO.ORDER_WEEK,\n" );
		sql.append("          TVD.DELIVERY_NO,\n" );
		sql.append("          TVD.DELIVERY_TYPE,\n" );
		sql.append("          TMVA.ADDRESS,\n" );
		sql.append("          TVD.DELIVERY_STATUS,\n" );
		sql.append("          TVD.LOSE_REASON,\n" );
		sql.append("          TVD.ERP_ORDER,\n" );
		sql.append("          TVD.DELIVERY_DATE\n");
		sql.append("ORDER BY DELIVERY_DATE DESC\n");
		//sql.append("          TVOD.SPECIAL_BATCH_NO");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	
	public void updateOrderDetailByOrderId(Long orderId, Long userId) {

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE TT_VS_ORDER_DETAIL TVOD\n");
		sql.append("   SET TVOD.CHECK_AMOUNT = TVOD.ORDER_AMOUNT,\n");  
		sql.append("       TVOD.UPDATE_DATE  = SYSDATE,\n");  
		sql.append("       TVOD.UPDATE_BY    = "+userId+"\n");  
		sql.append(" WHERE TVOD.ORDER_ID = "+orderId+"");

		dao.update(sql.toString(), null);
	}
	
	public static List<Map<String, Object>> getOemOrg(String dutyType, Long orgId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("\n");
		sql.append("SELECT TMO.ORG_ID, TMO.ORG_NAME\n");  
		sql.append("  FROM TM_ORG TMO\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND TMO.STATUS = ").append(Constant.STATUS_ENABLE).append("\n");
		if (Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
			sql.append("   AND (TMO.DUTY_TYPE = ").append(Constant.DUTY_TYPE_COMPANY).append(" OR TMO.DUTY_TYPE = ").append(Constant.DUTY_TYPE_LARGEREGION).append(")\n");  
		} else if (Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			sql.append("   AND TMO.ORG_ID = ").append(orgId).append("\n");  
		}
		
		List<Map<String, Object>> orgList = dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		
		return orgList ;
	}
	
	/*---------------------------------------------------   by fengalon   ----------------------------------------------
	------------------------------------------------------------------------------------------   2011/01/06   --------*/
	
	public static String getMaterialCode(Long dtlId) {
		List<Object> param = new ArrayList() ;
		String matCode = "" ;

		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("SELECT TVM.MATERIAL_CODE\n") ;
		sql.append("  FROM TT_VS_ORDER_DETAIL TVOD, TM_VHCL_MATERIAL TVM\n") ;  
		sql.append(" WHERE TVOD.MATERIAL_ID = TVM.MATERIAL_ID\n") ;  
		sql.append("   AND TVOD.DETAIL_ID = ?\n") ;

		param.add(dtlId) ;

		Map<String, Object> map = dao.pageQueryMap(sql.toString(), param, dao.getFunName());

		if(!CommonUtils.isNullMap(map)) {
			matCode = map.get("MATERIAL_CODE").toString() ;
		}

		return matCode ;
	}
	
	public PageResult<Map<String, Object>> getSendCarsDetail(String sendCarNo, String coreId,int curPage,int pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.SENDCAR_HEADER_ID,\n");
		sql.append("       A.SENDCAR_ORDER_NUMBER,\n");
		sql.append("       B.VEHICLE_ID,\n");
		sql.append("       B.IF_INSPECTION,\n");
		sql.append("       C.VIN,\n");
		sql.append("       D.MATERIAL_ID,\n");
		sql.append("       D.MATERIAL_CODE,\n");
		sql.append("       D.MATERIAL_NAME\n");
//		sql.append("  	    (SELECT T.LOCATION_ADDRESS FROM OTD_TRANSIT_DETAIL_INFO T WHERE ROWNUM <= 1 AND T.VIN = C.VIN AND T.PROJECTID = 'CAJ' \n");
//		sql.append("  		 AND T.CREATED_DATE = (SELECT MAX(TSD.CREATED_DATE) FROM OTD_TRANSIT_DETAIL_INFO TSD WHERE TSD.VIN = C.VIN)) LOCATION_ADDRESS \n");
 
		sql.append("  FROM tt_vs_dlvry_erp  A,\n");
		sql.append("       tt_vs_dlvry_mch  B,\n");
		sql.append("       TM_VEHICLE       C,\n");
		sql.append("       TM_VHCL_MATERIAL D\n");
		sql.append(" WHERE A.SENDCAR_ORDER_NUMBER = '"+sendCarNo+"'\n");
		sql.append("   AND a.delivery_id = " + coreId + "\n");
		sql.append("   AND B.STATUS = '"+Constant.STATUS_ENABLE+"'\n");
		sql.append("   AND B.ERP_SENDCAR_ID = A.SENDCAR_HEADER_ID\n");
		sql.append("   AND B.VEHICLE_ID = C.VEHICLE_ID\n");
		sql.append("   AND C.MATERIAL_ID = D.MATERIAL_ID");

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	
	private static int[] getNowYearOrMonth() {
		Calendar cale = Calendar.getInstance() ;
		
		int year = cale.get(Calendar.YEAR) ;
		int month = cale.get(Calendar.MONTH) + 1 ;
		int weekOfYear = cale.get(Calendar.WEEK_OF_YEAR) ;
		
		// yOrM[0]表示当前系统时间年份，yOrM[1]表示当前系统时间月份， yOrM[2]表示当天是今年第几周
		int[] yOrM = {year, month, weekOfYear} ;	
		
		return yOrM ;
	}
	
	public static int[] getNowDate() {
		return getNowYearOrMonth() ;
	}
	
	public List<Map<String, Object>> getOrderNO(Map<String, String> map) {
		String areaId = map.get("areaId") ;
		String dealerId = map.get("dealerId") ;
		String year = map.get("year") ;
		String week = map.get("week") ;
		String month = map.get("month") ;
		String orderType = map.get("orderType") ;
		String orderStatus = map.get("orderStatus") ;
		
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TVO.ORDER_NO\n");
		sql.append("  FROM TT_VS_ORDER TVO, VW_ORG_DEALER VOD\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND TVO.BILLING_ORG_ID = VOD.ROOT_DEALER_ID\n"); 
		sql.append("   AND TVO.AREA_ID = ?\n"); 
		params.add(areaId) ;
		
		sql.append("   AND VOD.DEALER_ID = ?\n");  
		params.add(dealerId) ;
		
		sql.append("   AND TVO.ORDER_YEAR = ?\n");  
		params.add(year) ;
		
		if(!CommonUtils.isNullString(week)) {
			sql.append("   AND TVO.ORDER_WEEK = ?\n");
			params.add(week) ;
		}
		
		if(!CommonUtils.isNullString(month)) {
			sql.append("   AND TVO.ORDER_MONTH = ?\n");
			params.add(month) ;
		}
		
		sql.append("   AND TVO.ORDER_TYPE = ?\n");
		params.add(orderType) ;
		
		sql.append("   AND TVO.ORDER_STATUS = ?\n");
		params.add(orderStatus) ;
		
		List<Map<String, Object>> orderList = dao.pageQuery(sql.toString(), params, dao.getFunName()) ;
		
		return orderList ;
	}
	
	public List<Map<String, Object>> getOrderNOByOrderUnit(Map<String, String> map) {
		String areaId = map.get("areaId") ;
		String dealerId = map.get("dealerId") ;
		String year = map.get("year") ;
		String week = map.get("week") ;
		String month = map.get("month") ;
		String orderType = map.get("orderType") ;
		String orderStatus = map.get("orderStatus") ;
		
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TVO.ORDER_NO\n");
		sql.append("  FROM TT_VS_ORDER TVO, VW_ORG_DEALER VOD\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND TVO.ORDER_ORG_ID = VOD.DEALER_ID\n"); 
		sql.append("   AND TVO.AREA_ID = ?\n"); 
		params.add(areaId) ;
		
		sql.append("   AND VOD.DEALER_ID = ?\n");  
		params.add(dealerId) ;
		
		sql.append("   AND TVO.ORDER_YEAR = ?\n");  
		params.add(year) ;
		
		if(!CommonUtils.isNullString(week)) {
			sql.append("   AND TVO.ORDER_WEEK = ?\n");
			params.add(week) ;
		}
		
		if(!CommonUtils.isNullString(month)) {
			sql.append("   AND TVO.ORDER_MONTH = ?\n");
			params.add(month) ;
		}
		
		sql.append("   AND TVO.ORDER_TYPE = ?\n");
		params.add(orderType) ;
		
		sql.append("   AND TVO.ORDER_STATUS = ?\n");
		params.add(orderStatus) ;
		
		List<Map<String, Object>> orderList = dao.pageQuery(sql.toString(), params, dao.getFunName()) ;
		
		return orderList ;
	}
	
	/**
	 * 经销商端发运指令查询
	 * @param startYear
	 * @param startWeek
	 * @param endYear
	 * @param endWeek
	 * @param startDate
	 * @param endDate
	 * @param orderType
	 * @param dealerCode
	 * @param orderNo
	 * @param transportType
	 * @param deliveryStatus
	 * @param groupCode
	 * @param areaIds
	 * @param companyId
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getLowDlrCommandQueryList(String dealerId,String startYear,String startWeek,String endYear,String endWeek,String startDate,String endDate,String orderType,
			String orderNo,String transportType,String deliveryStatus,String groupCode,String areaIds,Long companyId,int curPage,int pageSize) throws Exception{
		List <Object>params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("       TMD.DEALER_CODE CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME AS TTNAME,\n" );
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_CODE, TMD2.DEALER_CODE) AS ORDER_ORG_CODE,\n");
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_SHORTNAME, TMD2.DEALER_SHORTNAME) AS ORDER_ORG_NAME,\n" );
		sql.append("       TVO.ORDER_NO,\n" );
		sql.append("       TVO.ORDER_TYPE,\n" );
		sql.append("       TVO.ORDER_YEAR,\n" );
		sql.append("       TVO.ORDER_WEEK,\n" );
		sql.append("       TVD.DELIVERY_NO,\n" );
		sql.append("       DECODE(TVDD.Billing_Code,null,'',TVDD.Billing_Code,'查看') INVOICE_NO, \n" );
		sql.append("       TVD.DELIVERY_TYPE,\n" );
		sql.append("       DECODE (TVD.DELIVERY_TYPE ,"+Constant.TRANSPORT_TYPE_02+",TMVA.ADDRESS,'')ADDRESS,\n");
		sql.append("       TVD.DELIVERY_STATUS,\n" );
		sql.append("       DECODE(TVD.LOSE_REASON,null,'',TVD.LOSE_REASON,'详细原因') LOSE_REASON,\n" );
		sql.append("       TVD.ERP_ORDER,\n" );
		sql.append("       TO_CHAR(TVD.DELIVERY_DATE,'YYYY-MM-DD HH24:MI:SS') DELIVERY_DATE,\n" );
		sql.append("       SUM(NVL(TVDD.MATCH_AMOUNT,0)) MATCH_AMOUNT,\n" );
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) DELIVERY_AMOUNT,\n" );
		sql.append("       SUM(TVDRD.TOTAL_PRICE+TVDRD.DISCOUNT_PRICE) TOTAL_PRICE\n" );
		sql.append("  FROM TT_VS_ORDER              TVO,\n" );
		sql.append("       TT_VS_DLVRY_REQ      	TVDR,\n" );
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n" );
		sql.append("       TT_VS_DLVRY              TVD,\n" );
		sql.append("       TT_VS_DLVRY_DTL          TVDD,\n" );
		sql.append("       TM_VS_ADDRESS            TMVA,\n" );
		sql.append("       TM_DEALER                TMD,\n" );
		sql.append("       TM_DEALER                TMD3,\n" );
		sql.append("       TM_DEALER                TMD2\n" );
		sql.append(" WHERE TVO.ORDER_ID = TVD.ORDER_ID\n" );
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n" );
		sql.append("   AND TVD.REQ_ID = TVDR.REQ_ID\n" );
		sql.append("   AND TVDR.ORDER_DEALER_ID = TMD3.DEALER_ID(+)\n" );
		sql.append("   AND TVDRD.DETAIL_ID = TVDD.REQ_DTL_ID\n" );
		sql.append("   AND TVO.AREA_ID IN("+areaIds+")\n" );
		sql.append("   AND TVD.ADDRESS_ID = TMVA.ID(+)\n" );
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVO.ORDER_ORG_ID = TMD2.DEALER_ID\n" );
		sql.append("   AND TVDD.DELIVERY_AMOUNT <> 0\n" );
		sql.append("   AND TVD.COMPANY_ID = "+companyId+"\n" );

		sql.append("   AND (TVO.ORDER_ORG_ID IN ("+dealerId+") OR TVDR.ORDER_DEALER_ID IN ("+dealerId+"))\n" );
		
		if(!"-1".equals(deliveryStatus)&&!"".equals(deliveryStatus)&&deliveryStatus!=null){
			sql.append("   AND TVD.DELIVERY_STATUS = "+deliveryStatus+"\n" );
		}
		if(!"-1".equals(orderType)&&!"".equals(orderType)&&orderType!=null){
			sql.append("   AND TVO.ORDER_TYPE = "+orderType+"\n" );
		}
		if(!"".equals(orderNo)&&orderNo!=null){
			sql.append("   AND TVD.DELIVERY_NO LIKE '%"+orderNo+"%'\n" );
		}
		if(!"-1".equals(transportType)&&!"".equals(transportType)&&transportType!=null){
			sql.append("   AND TVD.DELIVERY_TYPE = "+transportType+"\n" );	
		}
		if(!"".equals(startYear)&&startYear!=null&&!"".equals(endYear)&&endYear!=null){
			sql.append("   AND TVO.ORDER_YEAR BETWEEN "+startYear+" AND "+endYear+"\n" );
		}
		if(!"".equals(startWeek)&&startWeek!=null&&!"".equals(endWeek)&&endWeek!=null){
			sql.append("   AND TVO.ORDER_WEEK BETWEEN "+startWeek+" AND "+endWeek+"\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVD.DELIVERY_DATE < =TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		sql.append(" GROUP BY TVD.DELIVERY_ID, TVD.REQ_ID,\n" );
		sql.append("          TMD.DEALER_CODE,\n" );
		sql.append("          TMD.DEALER_SHORTNAME,\n" );
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_CODE, TMD2.DEALER_CODE),\n");
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", TMD3.DEALER_SHORTNAME, TMD2.DEALER_SHORTNAME),\n" );
		sql.append("          TVO.ORDER_NO,\n" );
		sql.append("          TVO.ORDER_TYPE,\n" );
		sql.append("          TVO.ORDER_YEAR,\n" );
		sql.append("          TVO.ORDER_WEEK,\n" );
		sql.append("          TVD.DELIVERY_NO,\n" );
		sql.append("          TVD.DELIVERY_TYPE,\n" );
		sql.append("          TMVA.ADDRESS,\n" );
		sql.append("          TVD.DELIVERY_STATUS,\n" );
		sql.append("          TVD.LOSE_REASON,\n" );
		//sql.append("          XORR.REASON,\n" );
		sql.append("          TVD.ERP_ORDER,\n" );
		sql.append("          TVD.DELIVERY_DATE,TVDD.Billing_Code\n");
		sql.append("ORDER BY DELIVERY_DATE DESC\n");
		//sql.append("          TVOD.SPECIAL_BATCH_NO");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	
	public static Map<String, Object> getLowOrder_CG(String reqId) {
		List<Object> params = new ArrayList<Object>() ;

		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select nvl(tvo.old_order_id, -1) old_order_id, tvo.order_org_id from tt_vs_order tvo, tt_vs_dlvry_req tvdr where tvo.order_id = tvdr.order_id and tvdr.req_id = ?\n");
		params.add(reqId) ;

		Map<String, Object> orderMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;

		return orderMap ;
	}

	public static String getNewPrice(String dealerId) {
		String priceId = "" ;

		List<Object> params = new ArrayList<Object>() ;

		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select vtdpr.price_id, vtdpr.create_date\n");
		sql.append("  from vw_tm_dealer_price_relation vtdpr, vw_tt_vs_price vtvp\n");  
		sql.append(" where vtdpr.price_id = vtvp.price_id\n");  
		sql.append("   and vtdpr.dealer_id = ?");  
		params.add(dealerId) ;

		sql.append("   and vtdpr.create_date is not null\n");  
		sql.append(" order by vtdpr.create_date desc\n");


		List<Map<String, Object>> priceList = dao.pageQuery(sql.toString(), params, dao.getFunName()) ;
		
		if(!CommonUtils.isNullList(priceList)) {
			priceId = priceList.get(0).get("PRICE_ID").toString() ;
		}

		return priceId ;
	}

	public static int updateOrderPrice(String orderId, String priceId) {
		List<Object> params = new ArrayList<Object>() ;

		StringBuffer sql = new StringBuffer("\n") ;	

		sql.append("update tt_vs_order tvo set tvo.price_id = ? where tvo.order_id = ?\n");
		params.add(priceId) ;
		params.add(orderId) ;

		return dao.update(sql.toString(), params) ;
	}
	
	public PageResult<Map<String, Object>> getMoveQuery(Map<String, String> map, int curPage, int pageSize) {
		String moveNo = map.get("moveNo") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tvde.sendcar_order_number,\n");
		sql.append("       tvde.order_number,\n");  
		sql.append("       ts.order_no,\n"); 
		sql.append("       ts.sto_id,\n"); 
		sql.append("       to_char(tvde.flatcar_assign_date, 'yyyy-mm-dd') fdate,\n");  
		sql.append("       tvde.ship_method_code,\n");  
		sql.append("       tvde.flatcar_id,\n");  
		sql.append("       fware.warehouse_name fname,\n");  
		sql.append("       tware.warehouse_name tname,\n");  
		sql.append("       tvde.motorman,\n");  
		sql.append("       tvde.motorman_phone,\n");  
		sql.append("       nvl(count(tvded.detail_id), 0) dcount, -- 发车数量\n");  
		sql.append("       nvl(etvded.ecount, 0) ecount, --  实际在途\n");  
		sql.append("       decode(nvl(count(etvded.ecount), 0), 0, '完全入库', '在途') status\n");  
		sql.append("  from tt_sto ts,\n");  
		sql.append("       tt_vs_dlvry_erp tvde,\n");  
		sql.append("       tt_vs_dlvry_erp_dtl tvded,\n");  
		sql.append("       (select tvde.sendcar_order_number, count(tvded.detail_id) ecount\n");  
		sql.append("          from tt_sto              ts,\n");  
		sql.append("               tt_vs_dlvry_erp     tvde,\n");  
		sql.append("               tt_vs_dlvry_erp_dtl tvded,\n");  
		sql.append("               tm_vehicle          tmv\n");  
		sql.append("         where ts.sto_id = tvde.delivery_id\n");  
		sql.append("           and tvde.sendcar_header_id = tvded.sendcar_header_id\n");  
		sql.append("           and tvded.vin = tmv.vin\n");  
		sql.append("           and ts.from_warehouse_id = tmv.warehouse_id\n");  
		sql.append("     	   and ts.status <> ").append(Constant.STO_STATUS_04).append("\n") ;
		sql.append("         group by sendcar_order_number) etvded,\n");  
		sql.append("       tm_warehouse fware,\n");  
		sql.append("       tm_warehouse tware\n");  
		sql.append(" where ts.sto_id = tvde.delivery_id\n");  
		sql.append("   and tvde.sendcar_header_id = tvded.sendcar_header_id\n");  
		sql.append("   and tvde.sendcar_order_number = etvded.sendcar_order_number(+)\n");  
		sql.append("   and ts.from_warehouse_id = fware.warehouse_id\n");  
		sql.append("   and ts.to_warehouse_id = tware.warehouse_id\n"); 
		sql.append("   and etvded.ecount <> 0\n");
		
		if(!CommonUtils.isNullString(moveNo)) {
			sql.append("   and ts.order_no like '%").append(moveNo).append("%'\n");  
		}
		
		sql.append(" group by tvde.sendcar_order_number,\n");  
		sql.append("          tvde.order_number,\n");  
		sql.append("          ts.order_no,\n");  
		sql.append("       ts.sto_id,\n"); 
		sql.append("          tvde.flatcar_assign_date,\n");  
		sql.append("          tvde.ship_method_code,\n");  
		sql.append("          tvde.flatcar_id,\n");  
		sql.append("          fware.warehouse_name,\n");  
		sql.append("          tware.warehouse_name,\n");  
		sql.append("          tvde.motorman,\n");  
		sql.append("          tvde.motorman_phone,\n");
		sql.append("          etvded.ecount\n");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage) ;
	}
	
	public PageResult<Map<String, Object>> getVechInfo(String sendCarNo, String coreId,int curPage,int pageSize) throws Exception{
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select tmv.vin,\n");
		sql.append("       tvm.material_code,\n");  
		sql.append("       tvm.material_name,\n");  
		sql.append("       decode(tmv.warehouse_id, ts.to_warehouse_id, '已入库', '未入库') status\n");  
		sql.append("  from tt_vs_dlvry_erp     tvde,\n");  
		sql.append("       tt_vs_dlvry_erp_dtl tvded,\n");  
		sql.append("       tt_sto              ts,\n");  
		sql.append("       tm_vehicle          tmv,\n");  
		sql.append("       tm_vhcl_material    tvm\n");  
		sql.append(" where tvde.delivery_id = ts.sto_id\n");  
		sql.append("   and tvde.sendcar_header_id = tvded.sendcar_header_id\n");  
		sql.append("   and tvded.vin = tmv.vin\n");  
		sql.append("   and tmv.material_id = tvm.material_id\n");  
		sql.append("   and tvde.delivery_id = ").append(coreId).append("\n");  
		sql.append("   and tvde.SENDCAR_ORDER_NUMBER like '%").append(sendCarNo).append("%' \n");


		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	public List<Map<String, Object>> getOrderDate(Map<String, String> map) {
		String dealerId = map.get("dealerId") ;
		String yearWeek = map.get("yearWeek") ;
		
		StringBuffer sql = new StringBuffer("\n") ;

		/*sql.append("select distinct tvo.order_year,\n") ;
		sql.append("       tvo.order_week\n") ;
		sql.append("  from tt_vs_order tvo, tt_vs_order_detail tvod, vw_org_dealer vod\n") ;
		sql.append(" where tvo.order_id = tvod.order_id\n") ;
		sql.append("   and tvo.billing_org_id = vod.root_dealer_id\n") ;
		sql.append("   and tvo.order_type = 10201001\n") ;
		sql.append("   and tvod.call_amount < tvod.check_amount\n") ;
		sql.append("   and tvo.order_year || tvo.order_week < ").append(yearWeek).append("\n") ;
		sql.append("   and vod.dealer_id in (").append(dealerId).append(")\n") ;
		//sql.append(" group by tvo.order_no, tvo.order_year, tvo.order_week\n") ;
		sql.append(" order by tvo.order_year desc, tvo.order_week desc\n") ;*/
		
		sql.append("select distinct tvo.order_year, tvo.order_week\n") ;
		sql.append("  from (select tvo.order_year,\n") ;
		sql.append("               tvo.order_week,\n") ;
		sql.append("               tvo.order_id,\n") ;
		sql.append("               tvod.material_id,\n") ;
		sql.append("               tvod.call_amount,\n") ;
		sql.append("               tvod.check_amount\n") ;
		sql.append("          from tt_vs_order tvo, tt_vs_order_detail tvod, vw_org_dealer vod\n") ;
		sql.append("         where tvo.order_id = tvod.order_id\n") ;
		sql.append("           and tvo.billing_org_id = vod.root_dealer_id\n") ;
		sql.append("           and tvo.order_type = 10201001\n") ;
		sql.append("           and tvod.call_amount < tvod.check_amount\n") ;
		sql.append("   and tvo.order_year || decode(length(tvo.order_week), 2, '' || tvo.order_week, '0' || tvo.order_week) < ").append(yearWeek).append("\n") ;
		sql.append("           and vod.dealer_id in\n") ;
		sql.append("               (").append(dealerId).append(")) tvo,\n") ;
		sql.append("       (select tvo.old_order_id,\n") ;
		sql.append("               tvod.material_id,\n") ;
		sql.append("               sum(tvod.call_amount) call_amount\n") ;
		sql.append("          from tt_vs_order tvo, tt_vs_order_detail tvod\n") ;
		sql.append("         where tvo.order_id = tvod.order_id\n") ;
		sql.append("           and tvo.order_type = 10201002\n") ;
		sql.append("           and tvo.old_order_id >= 1\n") ;
		sql.append("         group by tvo.old_order_id, tvod.material_id) ttvo\n") ;
		sql.append(" where tvo.order_id = ttvo.old_order_id(+)\n") ;
		sql.append("   and tvo.material_id = ttvo.material_id(+)\n") ;
		sql.append("   and nvl(tvo.call_amount, 0) + nvl(ttvo.call_amount, 0) < tvo.check_amount\n") ;
		sql.append(" order by tvo.order_year desc, tvo.order_week desc\n") ;

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public Map<String, Object> getInfoByYear(String year) {
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select tds.set_year, max(to_number(tds.set_week)) set_week\n") ;
		sql.append("  from tm_date_set tds\n") ;
		sql.append(" where tds.set_year = ").append(year).append("\n") ;
		sql.append(" group by tds.set_year\n") ;

		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	
	//校验价格id
	public boolean judgePrice(String[] priceIds) {
		boolean flag=false;
		for(int i=0;i<priceIds.length;i++){
			if("".equals(priceIds[i])||"-1".equals(priceIds[i])||"0".equals(priceIds[i])){
				flag=true;
				break;
			}
		}
		return flag;
	}
	public boolean judgeResource(String[] materialId, String[] applyAmount,String[] detailId) {
		boolean flag=true;
		//	int oldApplyAmount=0;
		//int orderStock=0;
		for(int i=0;i<detailId.length;i++){
			int avaStock=0;
			int curApplyAmout=0;
			//跟进detailId获取以前的发运数量
			TtVsOrderDetailPO tvod=new TtVsOrderDetailPO();
			tvod.setDetailId(new Long(detailId[i]));
			tvod=(TtVsOrderDetailPO) dao.select(tvod).get(0);
			//oldApplyAmount=tvod.getCallAmount();
			int checkAmount=tvod.getCheckAmount();
			
			StringBuilder sql1= new StringBuilder();
			sql1.append("select nvl(vvr.ACT_STOCK,0) ACT_STOCK from VW_VS_RESOURCE_ENTITY_week vvr where vvr.MATERIAL_ID="+materialId[i]);
			
//			StringBuilder sql= new StringBuilder();
//			sql.append("select nvl(t.r_amount,0)r_amount  from     (SELECT\n" );
//			sql.append("                      tvod.MATERIAL_ID,\n" );
//			sql.append("                      (SUM(NVL(tvod.check_amount, 0)) -\n" );
//			sql.append("                      SUM(NVL(tvod.DELIVERY_AMOUNT, 0))) R_AMOUNT --要减去已经发运的保留数\n" );
//			sql.append("                 FROM\n" );
//			sql.append("                      tm_vhcl_material tvm,\n" );
//			sql.append("                      tt_vs_order_detail tvod, --发运申请\n" );
//			sql.append("                      TT_VS_ORDER     TVO\n" );
//			sql.append("                WHERE tvm.material_id=tvod.material_id\n" );
//			sql.append("                  AND tvod.ORDER_ID = TVO.ORDER_ID\n" );
//			sql.append("                  AND TVO.ORDER_TYPE = 10201001\n" );
//			sql.append("                 and (tvo.is_lock is null or tvo.is_lock=10041001)\n" );
//			sql.append("                  AND TVO.ORDER_WEEK IN\n" );
//			sql.append("                      (SELECT DS.SET_WEEK -- 只计算当前订单周度\n" );
//			sql.append("                         FROM TM_DATE_SET DS\n" );
//			sql.append("                        WHERE DS.SET_DATE = TO_CHAR(SYSDATE, 'yyyymmdd')\n" );
//			sql.append("                          AND DS.COMPANY_ID = TVO.COMPANY_ID)\n" );
//			sql.append("                  and NVL(tvod.check_amount, 0)-NVL(tvod.DELIVERY_AMOUNT, 0)>0\n" );
//			sql.append("                GROUP BY  tvod.MATERIAL_ID  ) t where t.material_id="+materialId[i]);

			List<Map<String,Object>> list1=dao.pageQuery(sql1.toString(), null, dao.getFunName());
//			List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
			
			if(list1.size()>0){
				avaStock=Integer.parseInt(list1.get(0).get("ACT_STOCK").toString()) ;//实际数量
//				orderStock=Integer.parseInt(list.get(0).get("R_AMOUNT").toString()) ;
			}
			curApplyAmout=new Integer(applyAmount[i]);
			int count=avaStock-curApplyAmout;
			if(count<0){
				flag=false;
				break;
			}
		}
	return flag;
}
	
	/**
	 * Function : 常规订单发运申请查询
	 * 
	 * @param :
	 *            订单年周
	 * @param :
	 *            订单号码
	 * @param :
	 *            车系
	 * @return : 满足条件的常规订单信息
	 * @throws :
	 *             Exception LastUpdate : 2010-05-24
	 */
	public List<Map<String, Object>> getDeliveryDetailList(Map<String,String> map) throws Exception {
		String orderYear=map.get("orderYear");
		String orderWeek=map.get("orderWeek");
		String orderNo=map.get("orderNo");
		String orderId=map.get("orderId");
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT distinct TVM.MATERIAL_ID, ---物料ID\n" );
		sql.append("       td.dealer_id,--经销商\n" );
		sql.append("       td.dealer_code,--经销商代码\n" );
		sql.append("       td.dealer_shortname,--经销商名称\n" );
		sql.append("       TSO.ORDER_ID, ---订单ID\n" );
		sql.append("   	tvw.act_STOCK ,\n" );
		sql.append("   	nvl(tvw.UNDO_ORDER_AMOUNT,0) UNDO_ORDER_AMOUNT ,\n" );
		sql.append("       TSO.AREA_ID, ---业务范围ID\n" );
		sql.append("       TSOD.DETAIL_ID, ---订单明细ID\n" );
		sql.append("       TSO.ORDER_NO, ---订单号码\n" );
		sql.append("       VMG.SERIES_NAME,---车系\n" );
		sql.append("       TVM.MATERIAL_CODE, ---物料编码\n" );
		sql.append("       TVM.MATERIAL_NAME, ---物料名称\n" );
		sql.append("       TVM.COLOR_NAME, ---颜色\n" );
		sql.append("        F_GET_MATPRICE(td.dealer_code,TVM.MATERIAL_CODE)SINGLE_PRICE, ---物料单价\n" );
		sql.append("        F_GET_PRICELISTID(td.dealer_code,TVM.MATERIAL_CODE) PRICTLIST_ID, ---物料单价id\n" );
		//sql.append("      1000 SINGLE_PRICE, ---物料单价\n" );
		//sql.append("      10195 PRICTLIST_ID, ---物料单价id\n" );
		sql.append("       TSOD.ORDER_AMOUNT, ---订单数量\n" );
		sql.append("       NVL(TSOD.CHECK_AMOUNT, 0) CHECK_AMOUNT, ---已审核数量\n" );
		sql.append("       NVL(TSOD.CALL_AMOUNT, 0) CALL_AMOUNT ,---已申请数量\n" );
		sql.append("TO_NUMBER(NVL(TSOD.CHECK_AMOUNT, 0))-TO_NUMBER(NVL(TSOD.CALL_AMOUNT, 0)) APPLY_AMOUNT ,---默认申请数量");
		sql.append("(TO_NUMBER(NVL(TSOD.CHECK_AMOUNT, 0))-TO_NUMBER(NVL(TSOD.CALL_AMOUNT, 0)))*TO_NUMBER(1000) TOTAL_PRICE,");
		sql.append("        TSOD.DISCOUNT_RATE,\n" );
		sql.append("       TSOD.DISCOUNT_S_PRICE,\n" );
		sql.append("       TSOD.DISCOUNT_PRICE\n" );
		sql.append("  FROM TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		sql.append("       VW_MATERIAL_GROUP        VMG,\n" );
		sql.append("       TM_AREA_GROUP            TAG,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );
		sql.append("        TT_VS_ORDER              TSO,\n" );
		sql.append("       TT_VS_ORDER_DETAIL       TSOD,\n" );
		sql.append("  VW_VS_resource_ENTITY_week tvw,");
		sql.append("       tm_dealer td\n" );
		sql.append(" WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n" );
		sql.append("   AND TSOD.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append("   AND TVMGR.GROUP_ID = VMG.PACKAGE_ID\n" );
		sql.append("   AND tvw.material_id(+) = tvm.material_id\n" );
		sql.append("   AND (VMG.SERIES_ID = TAG.MATERIAL_GROUP_ID OR\n" );
		sql.append("       VMG.MODEL_ID = TAG.MATERIAL_GROUP_ID OR\n" );
		sql.append("       VMG.PACKAGE_ID = TAG.MATERIAL_GROUP_ID)\n" );
		sql.append("   AND TAG.AREA_ID = TSO.AREA_ID\n" );
		sql.append("AND TVMG.GROUP_ID=TVMGR.GROUP_ID\n" );
		sql.append("  AND TVM.MATERIAL_ID=TVMGR.MATERIAL_ID\n" );
		sql.append("  and td.dealer_id=tso.order_org_id\n" );
		sql.append("   AND TSO.ORDER_TYPE = 10201001\n" );
		sql.append("   AND TSO.ORDER_STATUS = 10211005\n" );
		sql.append("   AND (TSO.IS_RESPOND is null or tso.is_respond=10041002)\n");
		
		sql.append("  AND TSOD.CHECK_AMOUNT > 0\n" );
		sql.append("  AND NVL(TSOD.CHECK_AMOUNT, 0) <> NVL(TSOD.CALL_AMOUNT, 0)");
		
		if (!"".equals(orderYear) && orderYear != null) {
			sql.append(" AND TSO.ORDER_YEAR = '" + orderYear + "'\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			sql.append(" AND TSO.ORDER_WEEK = '" + orderWeek + "'\n");
		}
		if (!CommonUtils.isNullString(orderNo)) {
			sql.append(" AND TSO.ORDER_NO = '" + orderNo + "'\n");
		}
		if (!CommonUtils.isNullString(orderId)) {
			sql.append(" AND TSO.ORDER_ID = '" + orderId + "'\n");
		}
		
		sql.append("    ORDER BY tso.ORDER_ID,VMG.SERIES_NAME \n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * Function : 常规订单发运申请查询
	 * 
	 * @param :
	 *            订单年周
	 * @param :
	 *            订单号码
	 * @param :
	 *            车系
	 * @return : 满足条件的常规订单信息
	 * @throws :
	 *             Exception LastUpdate : 2010-05-24
	 */
	public PageResult<Map<String, Object>> getApplyTotalQuery(Map<String,String> map,int pageSize,int curPage) throws Exception {
		String orderYear=map.get("orderYear");
		String orderWeek=map.get("orderWeek");
		String orderNo=map.get("orderNo");
		String beginTime=map.get("startDate");
		String endTime=map.get("endDate");
		String dealerCodes=map.get("dealerCodes");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT distinct td.dealer_id,\n" );
		sql.append("                td.dealer_code,\n" );
		sql.append("                td.dealer_shortname,\n" );
		sql.append("                TSO.ORDER_ID, ---订单ID\n" );
		sql.append("                TSO.AREA_ID, ---业务范围ID\n" );
		sql.append("                TSO.ORDER_NO ---订单号码\n" );
		sql.append("  FROM TT_VS_ORDER TSO, TT_VS_ORDER_DETAIL TSOD, tm_dealer td\n" );
		sql.append(" WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n" );
		sql.append("   and td.dealer_id = tso.order_org_id\n" );
		sql.append("   AND TSO.ORDER_TYPE = 10201001\n" );
		sql.append("   AND TSO.ORDER_STATUS = 10211005\n" );
		sql.append("   AND TSO.COMPANY_ID = 2010010100070674\n" );
//		sql.append("   AND TSO.ORDER_YEAR = '2014'\n" );
//		sql.append("   AND TSO.ORDER_WEEK = '50'\n" );
		sql.append("   AND TSO.AREA_ID = 2012112619161228\n" );
		sql.append("   AND (TSO.IS_RESPOND is null or tso.is_respond=10041002)\n");
		sql.append("   AND TSOD.CHECK_AMOUNT > 0\n" );
		sql.append("   AND NVL(TSOD.CHECK_AMOUNT, 0) <> NVL(TSOD.CALL_AMOUNT, 0)");
		if (!"".equals(orderYear) && orderYear != null) {
			sql.append(" AND TSO.ORDER_YEAR = '" + orderYear + "'\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			sql.append(" AND TSO.ORDER_WEEK = '" + orderWeek + "'\n");
		}
		if (!CommonUtils.isNullString(orderNo)) {
			sql.append(" AND TSO.ORDER_NO = '" + orderNo + "'\n");
		}
		if (null != beginTime && !"".equals(beginTime)) {
			sql.append("   AND TSO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append("   AND TSO.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (!"".equals(dealerCodes) && dealerCodes != null) {
			sql.append(" AND Tso.ORDER_ORG_ID  in(select td.dealer_id from tm_dealer td where td.dealer_code in("+CommonUtils.getSplitStringForIn(dealerCodes)+")) \n");
		}
		sql.append("    ORDER BY tso.ORDER_ID\n");
		PageResult<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);
		return list;
	}
	
	/**
	 * Function : 常规订单发运申请查询
	 * 
	 * @param :
	 *            订单年周
	 * @param :
	 *            订单号码
	 * @param :
	 *            车系
	 * @return : 满足条件的常规订单信息
	 * @throws :
	 *             Exception LastUpdate : 2010-05-24
	 */
	public PageResult<Map<String, Object>> getDeliveryDetailQuery(Map<String,String> map,int pageSize,int curPage) throws Exception {
		String orderYear=map.get("orderYear");
		String orderWeek=map.get("orderWeek");
		String orderNo=map.get("orderNo");
		String orderId=map.get("orderId");
		String beginTime=map.get("startDate");
		String endTime=map.get("endDate");
		String dealerCodes=map.get("dealerCodes");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT td.*,decode(tvp.operand,\n" );
		sql.append("              null,\n" );
		sql.append("              (select tvs.operand\n" );
		sql.append("                 from CUX_DMS_MATERIAL_PRICE_V@dms2ebs2 tvs\n" );
		sql.append("                where tvs.categories = td.group_code and tvs.party_number=td.dealer_code),\n" );
		sql.append("              tvp.operand) SINGLE_PRICE ,\n" );
		sql.append("\n" );
		sql.append("           decode(tvp.operand,\n" );
		sql.append("              null,\n" );
		sql.append("              (select tvs.list_header_id\n" );
		sql.append("                 from CUX_DMS_MATERIAL_PRICE_V@dms2ebs2 tvs\n" );
		sql.append("                where tvs.categories = td.group_code and tvs.party_number=td.dealer_code),\n" );
		sql.append("              tvp.list_header_id) PRICTLIST_ID,\n" );
		sql.append("\n" );
		sql.append("       (TO_NUMBER(NVL(td.CHECK_AMOUNT, 0)) -\n" );
		sql.append("       TO_NUMBER(NVL(td.CALL_AMOUNT, 0))) *\n" );
		sql.append("       TO_NUMBER(decode(tvp.operand,\n" );
		sql.append("                        null,\n" );
		sql.append("                        (select tvs.operand\n" );
		sql.append("                           from CUX_DMS_MATERIAL_PRICE_V@dms2ebs2 tvs\n" );
		sql.append("                          where tvs.categories = td.group_code and tvs.party_number=td.dealer_code),\n" );
		sql.append("                        tvp.operand)) TOTAL_PRICE\n" );
		sql.append("  from ORDER_view td,\n" );
		sql.append("       CUX_DMS_MATERIAL_PRICE_V@dms2ebs2 tvp\n" );
		sql.append(" where\n" );
		sql.append("    tvp.categories(+) = td.material_code\n" );
		sql.append("   and tvp.party_number(+) = td.dealer_code\n" );
		
		if (!"".equals(orderYear) && orderYear != null) {
			sql.append(" AND Td.ORDER_YEAR = '" + orderYear + "'\n");
		}
		if (!"".equals(orderWeek) && orderWeek != null) {
			sql.append(" AND Td.ORDER_WEEK = '" + orderWeek + "'\n");
		}
		if (!CommonUtils.isNullString(orderNo)) {
			sql.append(" AND Td.ORDER_NO = '" + orderNo + "'\n");
		}
		if (!CommonUtils.isNullString(orderId)) {
			sql.append(" AND Td.ORDER_ID = '" + orderId + "'\n");
		}
		if (null != beginTime && !"".equals(beginTime)) {
			sql.append("   AND Td.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append("   AND Td.RAISE_DATE <= TO_DATE('" + endTime + " 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (!"".equals(dealerCodes) && dealerCodes != null) {
			sql.append(" AND Td.dealer_id  in(select td.dealer_id from tm_dealer td where td.dealer_code in("+CommonUtils.getSplitStringForIn(dealerCodes)+")) \n");
		}
		sql.append("    ORDER BY td.ORDER_ID\n");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);
		return ps;
	}
	
	public List<Map<String,Object>> getBusinessArea(){
		List<Map<String,Object>> list=null;
		StringBuilder sql= new StringBuilder();
		sql.append("select tba.area_id from tm_business_area tba");
		list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
		
	}
	
	public PageResult<Map<String, Object>> getSendCarsDetail(String sendCarNo, int curPage, int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.SENDCAR_HEADER_ID,\n");
		sql.append("       A.SENDCAR_ORDER_NUMBER,\n");
		sql.append("       B.VEHICLE_ID,\n");
		sql.append("       B.IF_INSPECTION,\n");
		sql.append("       C.VIN,\n");
		sql.append("       D.MATERIAL_ID,\n");
		sql.append("       D.MATERIAL_CODE,\n");
		sql.append("       D.MATERIAL_NAME\n");
		// sql.append(" (SELECT T.LOCATION_ADDRESS FROM OTD_TRANSIT_DETAIL_INFO
		// T WHERE ROWNUM <= 1 AND T.VIN = C.VIN AND T.PROJECTID = 'CAJ' \n");
		// sql.append(" AND T.CREATED_DATE = (SELECT MAX(TSD.CREATED_DATE) FROM
		// OTD_TRANSIT_DETAIL_INFO TSD WHERE TSD.VIN = C.VIN)) LOCATION_ADDRESS
		// \n");

		sql.append("  FROM tt_vs_dlvry_erp  A,\n");
		sql.append("       tt_vs_dlvry_mch  B,\n");
		sql.append("       TM_VEHICLE       C,\n");
		sql.append("       TM_VHCL_MATERIAL D\n");
		sql.append(" WHERE A.SENDCAR_ORDER_NUMBER = '" + sendCarNo + "'\n");
		sql.append("   AND B.STATUS = '" + Constant.STATUS_ENABLE + "'\n");
		sql.append("   AND B.ERP_SENDCAR_ID = A.SENDCAR_HEADER_ID\n");
		sql.append("   AND B.VEHICLE_ID = C.VEHICLE_ID\n");
		sql.append("   AND C.MATERIAL_ID = D.MATERIAL_ID");

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return rs;
	}
	
	public PageResult<Map<String, Object>> getVechInfo(String sendCarNo, int curPage, int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select tmv.vin,\n");
		sql.append("       tvm.material_code,\n");
		sql.append("       tvm.material_name,\n");
		sql.append("       decode(tmv.warehouse_id, ts.to_warehouse_id, '已入库', '未入库') status\n");
		sql.append("  from tt_vs_dlvry_erp     tvde,\n");
		sql.append("       tt_vs_dlvry_erp_dtl tvded,\n");
		sql.append("       tt_sto              ts,\n");
		sql.append("       tm_vehicle          tmv,\n");
		sql.append("       tm_vhcl_material    tvm\n");
		sql.append(" where tvde.delivery_id = ts.sto_id\n");
		sql.append("   and tvde.sendcar_header_id = tvded.sendcar_header_id\n");
		sql.append("   and tvded.vin = tmv.vin\n");
		sql.append("   and tmv.material_id = tvm.material_id\n");
		sql.append("   and tvde.SENDCAR_ORDER_NUMBER like '%").append(sendCarNo).append("%' \n");

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return rs;
	}
}
