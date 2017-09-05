package com.infodms.dms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
	private static final String DATE_PATTERN = "yyyy-MM-dd";
		
	public static Date stringToDate(String time) throws ParseException{
		if(time==null||time.equals("")){
			return null;
		}
		SimpleDateFormat formate = new SimpleDateFormat(DATE_PATTERN);
		return formate.parse(time);
	}
	
	public static Date stringToDateByPattern(String time, String datePattern) throws ParseException{
		if(time==null||time.equals("")){
			return null;
		}
		SimpleDateFormat formate = new SimpleDateFormat(datePattern);
		return formate.parse(time);
	}
	
	/**
	 * 将日期格式化成字符串
	 * add by zhangxianchao
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static String parseDateToString(Date date) throws ParseException{
		if(date==null){
			return null;
		}
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formate.format(date);
	}
	public static String parseDateToString2(Date date) throws ParseException{
		if(date==null){
			return null;
		}
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return formate.format(date);
	}
	/**
	 * 将日期格式化成字符串
	 * add by zhangxianchao
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static String parseDateToDate(Date date) throws ParseException{
		if(date==null){
			return null;
		}
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
		return formate.format(date);
	}
	/**
	 * 将毫秒转化成日期的字符串表示
	 * add by zhangxianchao
	 * @param time
	 * @return
	 */
	public static String getDateTimeFormat(long time){
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		String dateStr = formate.format(date);
		return dateStr;
	}
	/**
	 * 将毫秒转化成日期
	 * add by zhangxianchao
	 * @param time
	 * @return
	 */
	public static Date getDateByTime(long time){
		Date date = new Date(time);
		return date;
	}
	/**
	 * 获取当前年份
	 * add by zhilinshu
	 * @param time
	 * @return
	 */
	public static int getNowYear(Date date){
		SimpleDateFormat formate = new SimpleDateFormat("yyyy");
		return Integer.valueOf(formate.format(date));
	}
	
	  /** 
	   * 得到几天后的时间 
	   * @param d 
	   * @param day 
	   * @return 
	   */  
	  public static Date getDateAfter(Date date,int day){  
	   Calendar nowDate =Calendar.getInstance();  
	   nowDate.setTime(date);  
	   nowDate.set(Calendar.DATE,nowDate.get(Calendar.DATE)+day);  
	   return nowDate.getTime();  
	  } 

}
