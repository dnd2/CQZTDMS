package com.infodms.dms.util;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

public class DateOfJob extends BaseDao{
	public Logger logger = Logger.getLogger(DateOfJob.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private static Map<String, Object> poMap = new HashMap<String, Object>() ;
	private static String oemCompanyId = null ;
	private static final String START_DATE = "2010-10-31" ;   //默认系统上线日期减一天
	private static final String SET_END_DATE = "2012-12-31" ;   //运行是需要修改的日期参数
	
	public static void main(String[] args) {
		ContextUtil.loadConf();

		POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
		
		DateOfJob doj = new DateOfJob() ;
		
		poMap = doj.getSysDataDateQuery() ;
		oemCompanyId = doj.getOemCompanyIdQuery() ;
		
		List<Map<String, Object>> list = doj.setDateList(doj.getSysMaxDate(poMap), SET_END_DATE, "-") ;
		
		for(int i=1; i<list.size(); i++) {
			Map<String, Object> tds = list.get(i) ;
			
			doj.dateInsert(tds) ;
		}
		POContext.endTxn(true);
	}
	
	public void exeDateOfJob() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String resultStr = "1" ;
			poMap = this.getSysDataDateQuery() ;
			oemCompanyId = this.getOemCompanyIdQuery() ;
			
			List<Map<String, Object>> list = this.setDateList(this.getSysMaxDate(poMap), SET_END_DATE, "-") ;
			
			int len = list.size() ;
			
			if(len == 1) {
				resultStr = "2" ;
			}
			
			for(int i=1; i<list.size(); i++) {
				Map<String, Object> tds = list.get(i) ;
				
				this.dateInsert(tds) ;
			}
			
			act.setOutData("resultStr", resultStr) ;
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "工作日历自动生成");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void dateInsert(Map<String, Object> po) {
		TmDateSetPO tds = new TmDateSetPO() ;
		
		tds.setSetYear(po.get("year").toString()) ;
		tds.setSetMonth(po.get("month").toString()) ;
		tds.setSetWeek(po.get("week").toString()) ;
		tds.setSetDate(po.get("comStr").toString()) ;
		tds.setCompanyId(Long.parseLong(oemCompanyId)) ;
		
		super.insert(tds) ;
	}
	
	public String getSysMaxDate(Map<String, Object> map) {
		if(!CommonUtils.isNullMap(map)) {
			return map.get("SET_YEAR").toString() + "-" + map.get("SET_MONTH").toString() + "-" + map.get("SET_DATE").toString().substring(6) ;
		} else {
			return START_DATE ;
		}
	}
	
	public String getOemCompanyIdQuery() {
		StringBuffer sql = new StringBuffer() ;
		
		sql.append("select tmc.company_id from tm_company tmc where 1 = 1 and tmc.status = ").append(Constant.STATUS_ENABLE).append(" and tmc.company_type = ").append(Constant.COMPANY_TYPE_SGM).append("\n");
		
		Map<String, Object> map = super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
		
		return map.get("COMPANY_ID").toString() ;
	}
	
	public Map<String, Object> getSysDataDateQuery() {
		StringBuffer sql = new StringBuffer() ;
		
		sql.append("select tds.set_year,\n");
		sql.append("       tds.set_month,\n");  
		sql.append("       tds.set_week,\n");  
		sql.append("       tds.set_date,\n");  
		sql.append("       tds.company_id\n");  
		sql.append("  from tm_date_set tds, tm_company tmc\n");  
		sql.append(" where tds.company_id = tmc.company_id\n");  
		sql.append("   and tmc.company_type = ").append(Constant.COMPANY_TYPE_SGM).append("\n");  
		sql.append("   and set_date = (select max(set_date) from tm_date_set)\n");

		Map<String, Object> map = super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
		
		return map ;
	}
	
	/**
	*  通过开始日期以及结束日期,获取日期之间的星期
	*  @param startDate 开始日期
	*  @param endDate 结束日期
	*  @param separator 日期之间的分隔符
	*  @return List<Map<String, Object>> 日期容器集
	**/
	public List<Map<String, Object>> setDateList(String startDate, String endDate, String separator) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>() ;
		Map<String, Object> map = null ;

		long quot = 0 ;

		String[] date = startDate.split(separator) ;
		int year = Integer.parseInt(date[0]) ;
		int month = Integer.parseInt(date[1]) ;
		int day = Integer.parseInt(date[2]) ;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;

		try {
			Date stDate = sdf.parse(startDate) ;
			Date enDate = sdf.parse(endDate) ;

			quot = enDate.getTime() - stDate.getTime() ;

			quot = quot/1000/60/60/24 ;
		} catch(Exception e) {

		} 
		
		Calendar calendar = new GregorianCalendar() ;
		calendar.setFirstDayOfWeek(Calendar.MONDAY) ;

		int maxWeek = 0 ;
		int currentYear = 0 ;
		Calendar cal = new GregorianCalendar() ;
		cal.setFirstDayOfWeek(Calendar.MONDAY) ;
		
		for(int j=day; j<=day+quot; j++) {
			calendar.set(year, month-1, j) ;
			
			map = new HashMap<String, Object>() ;

			int itYear = calendar.get(Calendar.YEAR) ;
			int itMonth = calendar.get(Calendar.MONTH) + 1 ;
			int itDay = calendar.get(Calendar.DAY_OF_MONTH) ;
			int itWeek = calendar.get(Calendar.WEEK_OF_YEAR) ;
			
			if(12 == itMonth && 1 == itWeek) {
				if(currentYear != itYear) {
					currentYear = itYear ;
					
					cal.set(Calendar.YEAR, itYear) ;
					maxWeek = cal.getActualMaximum(Calendar.WEEK_OF_YEAR) + 1 ;
				}
				
				itWeek = maxWeek ;
			}
			
			/*if(12 == itMonth && 1 == itWeek) {
				if(currentYear != itYear) {
					currentYear = itYear ;
					for(int n=itDay; n>31-6; n--) {
						cal.set(itYear, itMonth-1,n) ;
						
						maxWeek = cal.get(Calendar.WEEK_OF_YEAR) ;
						
						if(maxWeek != 1) {
							break ;
						}
					}
				}
				
				itWeek = maxWeek + 1;
			}*/
			
			map.put("year", itYear) ;
			map.put("month", itMonth) ;
			map.put("day", itDay) ;
			map.put("week", itWeek) ;
			
			String[] dateStr = {itYear + "", this.formatDateDetail(itMonth, 2, "0"), this.formatDateDetail(itDay, 2, "0")} ;
			int[] startIndex = {0, 0, 0} ;
			int[] dateSize = {4, 2, 2} ;
			
			map.put("comStr", this.formatDateStr(dateStr, startIndex, dateSize)) ;
			
			list.add(map) ;
		}

		return list ;
	}
	
	public String formatDateDetail(int paramValue, int size, String addStr) {
		StringBuffer resultStr = new StringBuffer(paramValue + "") ;
		
		int len = resultStr.length() ;
		
		for(int i=len; i<size; i++) {
			resultStr.insert(0, addStr) ;
		}
		
		return resultStr.toString() ;
	}
	
	public String formatDateStr(String[] date, int[] startIndex, int[] endIndex) {
		StringBuffer resultStr = new StringBuffer() ;
		
		resultStr.append(date[0].substring(startIndex[0], endIndex[0])) ; //year取出格式化长度
		resultStr.append(date[1].substring(startIndex[1], endIndex[1])) ; //month取出格式化长度
		resultStr.append(date[2].substring(startIndex[2], endIndex[2])) ; //day取出格式化长度
		
		return resultStr.toString() ;
	}
	
	public String getMonthDayEndByWeek(int year, int month, int para, int count) {
		Calendar calendar = new GregorianCalendar() ;
		calendar.setFirstDayOfWeek(Calendar.MONDAY) ; //设置周一为周度的第一天
		
		calendar.set(Calendar.YEAR, year) ;
		calendar.set(Calendar.MONTH, month - 1) ;
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) ;
		
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) ; //获取指定月份最后一天的星期几
		
		if(1 == dayOfWeek) {
			dayOfWeek = 7 ;
		} else {
			dayOfWeek -= 1 ;
		}
		
		int minuseDay = calendar.getActualMaximum(Calendar.DAY_OF_WEEK) - dayOfWeek ;
		
		Calendar calendarEnd = new GregorianCalendar() ;
		calendarEnd.setFirstDayOfWeek(Calendar.MONDAY) ;
		calendarEnd.set(Calendar.YEAR, year) ;
		calendarEnd.set(Calendar.MONTH, month - 1) ;
		calendarEnd.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + minuseDay + count) ;
		
		int yearEnd = calendarEnd.get(Calendar.YEAR) ;
		int monthEnd = calendarEnd.get(Calendar.MONTH) + 1 ;
		int dayEnd = calendarEnd.get(Calendar.DAY_OF_MONTH) ;
		
		String dateEnd = "" ;
		switch(para) {
			case 0 : 
				dateEnd = yearEnd + "年" + monthEnd + "月" + dayEnd + "日" ;
				break ;
			case 1 :
				dateEnd = yearEnd + "/" + monthEnd + "/" + dayEnd ;
				break ;
			case 2 :
				dateEnd = yearEnd + "-" + monthEnd + "-" + dayEnd ;
				break ;
			default :
				if("".equals(dateEnd)) {
					dateEnd = yearEnd + "年" + monthEnd + "月" + dayEnd + "日" ;
				}
		}
		 
		return dateEnd ;
	}
	
	public String getMonthDayBeforeByWeek(int year, int month, int para) {
		Calendar calendar = new GregorianCalendar() ;
		calendar.setFirstDayOfWeek(Calendar.MONDAY) ; //设置周一为周度的第一天
		
		calendar.set(Calendar.YEAR, year) ;
		calendar.set(Calendar.MONTH, month - 1) ;
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH)) ;
		
		int day = calendar.get(Calendar.DAY_OF_MONTH) ;
		
		String dateBefore = "" ;
		switch(para) {
			case 0 : 
				dateBefore = year + "年" + month + "月" + day + "日" ;
				break ;
			case 1 :
				dateBefore = year + "/" + month + "/" + day ;
				break ;
			case 2 :
				dateBefore = year + "-" + month + "-" + day ;
				break ;
			default :
				if("".equals(dateBefore)) {
					dateBefore = year + "年" + month + "月" + day + "日" ;
				}
		}
		 
		return dateBefore ;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
