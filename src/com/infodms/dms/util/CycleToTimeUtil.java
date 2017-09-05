package com.infodms.dms.util;
import java.util.Date;
import java.util.Calendar;

import com.infodms.dms.constants.CalendarConstants;


public class CycleToTimeUtil implements CalendarConstants {
	
	public static void main(String[] args) {
		Date date = CycleToTimeUtil.getTime(1, 2);
		System.out.println(new Date(getStartTime(date.getTime())));
		System.out.println(new Date(getEndTime(date.getTime())));
	}
	/**
	 * 取一天的开始
	 * @param ct
	 * @return
	 */
	public static long getStartTime(long ct) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(ct);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTimeInMillis();
	}
	/**
	 * 取一天的结束
	 * @param ct
	 * @return
	 */
	public static long getEndTime(long ct) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(ct);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTimeInMillis();
	}
	
	/**
	 * 
	 * @param cycleType 周期类型
	 * @param day  天数
	 * @return 确切的时间
	 */
	public static Date getTime(int cycleType, int day) {
		switch(cycleType) {
		case CYCLE_TYPE_WEEK : 
			return getTimeByWeek(day);
		case CYCLE_TYPE_MONTH : 
			return getTimeByMonth(day);
		case CYCLE_TYPE_SEASON : 
			return getTimeBySeason(day);
		default :
			throw new IllegalArgumentException("Invalid cycleType, cycleType == " + cycleType);
		}
		
	}
	
	private static Date getTimeByWeek(int day) {
		Calendar c = getCalendar();
		if (day < 0) {
			//取本星期第一天
			setCalendar(c, Calendar.DAY_OF_WEEK, c      
		            .getActualMinimum(Calendar.DAY_OF_WEEK) + 1);
			//往前推几天
			addCalendar(c, Calendar.DAY_OF_WEEK, day);
		} else {
			setCalendar(c, Calendar.DAY_OF_WEEK, day + 1);
		}
		return c.getTime();
	}
	
	private static Date getTimeByMonth(int day) {
		Calendar c = getCalendar();
		if (day < 0) {
			//取本月第一天
			setCalendar(c, Calendar.DAY_OF_MONTH, c      
		            .getActualMinimum(Calendar.DAY_OF_MONTH));
			//往前推几天
			addCalendar(c, Calendar.DAY_OF_MONTH, day);
		} else {
			setCalendar(c, Calendar.DAY_OF_MONTH, day);
		}
		return c.getTime();
	}
	
	private static Date getTimeBySeason(int day) {
		Calendar c = getCalendar();
		int month = c.get(Calendar.MONTH) + 1;
		//取当月是第几个季度
		int season = getSeasonByMonth(month);
		//取这个季度的第一个月
		int firstMonth = getFirstMonthBySeason(season);
		System.out.println("current month == " + month + ", current season == " + season
				+ ", " + season + " season first month == " + firstMonth);
		if (day < 0) {
			//取季度的第一个月
			setCalendar(c, Calendar.MONTH, firstMonth - 1);
			//取季度的第一个月的第一天
			setCalendar(c, Calendar.DAY_OF_MONTH, c      
		            .getActualMinimum(Calendar.DAY_OF_MONTH));
			addCalendar(c, Calendar.DAY_OF_YEAR, day);
		} else {
			//取季度的第一个月
			setCalendar(c, Calendar.MONTH, firstMonth - 1);
			addCalendar(c, Calendar.DAY_OF_YEAR, day);
		}
		return c.getTime();
	}
	
	/**
	 * 根据月份取季度
	 * @param month
	 * @return season
	 */
	private static int getSeasonByMonth(int month) {
		switch(month) {
		
		case JANUARY :
		case FEBRUARY : 
		case MARCH: 
			return SPRING;
		
		case APRIL: 
		case MAY: 
		case JUNE : 
			return SUMMER;

		case JULY: 
		case AUGUST: 
		case SEPTEMBER : 
			return AUTUMN;

		case OCTOBER: 
		case NOVEMBER: 
		case DECEMBER : 
			return WINTER;

		default: throw new IllegalArgumentException("Invalid month == " + month);
		}
	}
	/**
	 * 取季度的第一个月
	 * @param season
	 * @return firstMonth
	 */
	private static int getFirstMonthBySeason(int season) {
		switch(season) {
		case SPRING : 
			return JANUARY;
		case SUMMER : 
			return APRIL;
		case AUTUMN : 
			return JULY;
		case WINTER : 
			return OCTOBER;
		default : 
			throw new IllegalArgumentException("Invalid season == " + season); 
		}
	}
	
	private static void addCalendar(Calendar c, int field, int day) {
		c.add(field, day);
	} 
	
	private static void setCalendar(Calendar c, int field, int day) {
		c.set(field, day);
	} 
	
	private static Calendar getCalendar() {
		Calendar c = Calendar.getInstance();
		//c.clear();
		return c;
	}

}
