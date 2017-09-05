/**********************************************************************
 * <pre>
 * FILE : DateUtil.java
 * CLASS : DateUtil
 *
 * AUTHOR : SuMMeR
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		  |2009-9-3| SuMMeR| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
/**
 * $Id: DateUtil.java,v 1.1 2010/08/16 01:44:18 yuch Exp $
 */

package com.infodms.dms.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.infodms.dms.util.DateTimeUtil;

/**
 * 处理时间转换及格式工具类
 */
public class DateUtil
{

	/**
	 * 字符格式时间转换为Date 暂时只支持yyyy-MM-dd和yyyy/MM/dd格式
	 * 
	 * @param date
	 *            字符串格式的时间,如yyyy-MM-dd和yyyy/MM/dd
	 * @param token
	 *            时间分割符,如"/","-"
	 * @return
	 */
	public static Date str2Date(String date, String token)
	{
		Calendar cal = str2Cal(date, token);
		return cal.getTime();
	}

	/**
	 * 字符格式时间转换为Calendar 暂时只支持yyyy-MM-dd和yyyy/MM/dd格式
	 * 
	 * @param date
	 *            字符串格式的时间,如yyyy-MM-dd和yyyy/MM/dd
	 * @param token
	 *            时间分割符,如"/","-"
	 * @return
	 */
	public static Calendar str2Cal(String date, String token)
	{
		String[] dateArray = date.split(token);
		int year = Integer.valueOf(dateArray[0]).intValue();
		int month = Integer.valueOf(dateArray[1]).intValue()-1;
		int day = Integer.valueOf(dateArray[2]).intValue();
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		return cal;
	}

	/**
	 * 根据Date对象获取改对象的年份
	 * 
	 * @param date
	 * @return
	 */
	public static String getYearByDate(Date date)
	{
		return getPartByDate(date, 0);
	}

	/**
	 * 根据Date对象获取改对象的月份
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonthByDate(Date date)
	{
		return getPartByDate(date, 1);
	}

	/**
	 * 根据Date对象获取改对象的月日期(当月第几号)
	 * 
	 * @param date
	 * @return
	 */
	public static String getDayByDate(Date date)
	{
		return getPartByDate(date, 2);
	}

	/**
	 * 根据Date对象获取改对象的年,月,日部分
	 * 
	 * @param date
	 * @param pos
	 * @return
	 */
	@SuppressWarnings("finally")
	public static String getPartByDate(Date date, int pos)
	{
		String part = "";
		try
		{
			String dateStr = DateTimeUtil.parseDateToDate(date);
			part = dateStr.split("-")[pos];
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		finally
		{
			return part;
		}
	}

	public static void main(String[] args) throws ParseException
	{
		// 获得提报起始周
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.DATE, -7);
//		String dayStr = DateTimeUtil.parseDateToDate(c.getTime());
//		System.out.println("--"+dayStr);
//		System.out.println(getDay("2014-04-11","2014-04-11"));
		System.out.println(getDay(getYesToDay(),"2014-11-10" ));
		if(getDay("2014-11-03", getFirstMonthDay(1)) >0){
			System.err.println(1);
		}else {
			System.err.println(2);
		}
		System.err.println(getbeforeMonthFirstDay());
		
	}
	 /**
	  * 计算两个日期相隔天数
	  * add by wangx
	  * @param beginDate
	  * @param endDate
	  * @return 
	  * @throws ParseException
	  */
	public static int getDay(String beginDate,String endDate) throws ParseException{
	    SimpleDateFormat sim = new SimpleDateFormat( "yyyy-MM-dd");
	    Date d1 = sim.parse(beginDate); 
	    Date d2 = sim.parse(endDate); 
	    if(d2.before(d1)){    //结束日期早于开始日期
	    	return -1;
	    }
	    return (int) ((d2.getTime() - d1.getTime()) / (3600L * 1000 * 24));
	    }
	
	public static String getDateStr(Date date,int type) throws ParseException{
		 SimpleDateFormat sdf =  null;
		 switch (type) {
		case 1:
			sdf = new SimpleDateFormat( "yyyy-MM-dd");
			break;
		case 2:
			sdf = new SimpleDateFormat( "yyyy/MM/dd");
			break;
		case 3:
			sdf = new SimpleDateFormat( "yyyy-MM-dd HH24:mm:ss");
			break;
		case 4:
			sdf = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss");
			break;
		default:
			sdf = new SimpleDateFormat( "yyyy-MM-dd");
			break;
		}
	     String str = sdf.format(new Date());
	     return str;
	}
	
	/**
	 * 
	* @Title: getFirstMonthDay 
	* @author: xyfue
	* @Description: 获取当前月第一天 
	* @param @param type
	* @param @return
	* @param @throws ParseException    设定文件 
	* @date 2014年10月31日 下午8:04:21 
	* @return String    返回类型 
	* @throws
	 */
	public static String getFirstMonthDay(int type) throws ParseException {
		String first = "";
		SimpleDateFormat sdf =  null;
		 switch (type) {
		case 1:
			sdf = new SimpleDateFormat( "yyyy-MM-dd");
			break;
		case 2:
			sdf = new SimpleDateFormat( "yyyy/MM/dd");
			break;
		case 3:
			sdf = new SimpleDateFormat( "yyyy-MM-dd HH24:mm:ss");
			break;
		case 4:
			sdf = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss");
			break;
		default:
			sdf = new SimpleDateFormat( "yyyy-MM-dd");
			break;
		}
		 
		 Calendar c = Calendar.getInstance();    
	        c.add(Calendar.MONTH, 0);
	        c.set(Calendar.DAY_OF_MONTH,1);//
		
			first = sdf.format(c.getTime());
		return first;

	}
	
	/**
	 * 
	* @Title: getYesToDay 
	* @author: xyfue
	* @Description: 获取前一天日期
	* @param @param type
	* @param @return
	* @param @throws ParseException    设定文件 
	* @date 2014年10月31日 下午8:04:21 
	* @return String    返回类型 
	* @throws
	 */
	public static String getYesToDay() throws ParseException {
		String first = "";
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd");
			
		 Calendar c = Calendar.getInstance();    
		 c.add(Calendar.DAY_OF_MONTH, -1);
		
		first = sdf.format(c.getTime());
		return first;

	}
	
	/**
	 * 
	* @Title: getbeforeMonthFirstDay 
	* @author: xyfue
	* @Description: TODO上个月的第一天
	* @param @return
	* @param @throws ParseException    设定文件 
	* @date 2014年11月11日 下午5:15:06 
	* @return String    返回类型 
	* @throws
	 */
	public static String getbeforeMonthFirstDay() throws ParseException {
		String first = "";
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd");
			
		Calendar   cal_1=Calendar.getInstance();//获取当前日期 
	     cal_1.add(Calendar.MONTH, -1);
	     cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
	     first = sdf.format(cal_1.getTime());
		return first;

	}
	
	/**
	 * 时间 -> 字符串
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

	/**
	 * 计算两个日期间隔天数（secondDate - firstDate）
	 * @author 刘发忠
	 * @param firstDate 为String型
	 * @param secondDate  为String型
	 * @return 间隔天数
	 */
	public static int nDaysBetweenTwoDate(String firstString, String secondString) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date firstDate = null;
		Date secondDate = null;
		
		try {
			firstDate = df.parse(firstString);
			secondDate = df.parse(secondString);
		} catch (Exception e) {
			throw new Exception("日期格式不正确！");
		}
		int nDay =(int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
		return nDay;
	}
	
	/**
	 * 得到以"yyyy-MM-dd HH:mm:ss"格式的日期字符串
	 * @author 刘发忠
	 * @return 得到当前时间字符串
	 */
	public static String getCurrentDateTime(){
		Calendar curDate = Calendar.getInstance();
		return DateUtil.formatDateTime(curDate.getTime(),"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 将日期格式为指定格式的字符串。“yyyy-MM-dd HH:mm:ss:SSS”
	 * @author 刘发忠
	 * @param baseDate 日期对象型的日期数据
	 * @param strFormat 格式化字符串
	 * @return 格式化好的字符串
	 */
	public static String formatDateTime(Date baseDate,String strFormat){
		SimpleDateFormat df = new SimpleDateFormat(strFormat);
		return df.format(baseDate);
	}
	
	/**
	 * 得到自定义字符格式的日期字符串
	 * @author 刘发忠
	 * @param strFormat 日期格式
	 * @return 字符串的日期值
	 */
	public static String getCurrentDateTime(String strFormat){
		Calendar curDate = Calendar.getInstance();
		return DateUtil.formatDateTime(curDate.getTime(),strFormat);
	}
}
