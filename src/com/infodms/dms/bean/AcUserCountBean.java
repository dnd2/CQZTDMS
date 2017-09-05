/**********************************************************************
* <pre>
* FILE : AcUserCountBean.java
* CLASS : AcUserCountBean
* 
* AUTHOR : ZhangLei
*
* FUNCTION :功能使用情况查询BEAN.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-08-20| ZhangLei  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: AcUserCountBean.java,v 1.1 2010/08/16 01:42:34 yuch Exp $
 */
package com.infodms.dms.bean;


public class AcUserCountBean {
	
	//Action 使用次数
	private String acCount;
	//月度统计
	private String month;
	//天数统计
	private String day;
	//小时统计
	private String hour;
	//Action名称
	private String acName;
	//Func名称
	private String fucName;
	//响应时间
	private String answerTime;
	//开始时间
	private String startTime;
	//结束时间
	private String endTime;
	//公司
	private String company;
	//部门
	private String dept;
	//用户
	private String userName;
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAnswerTime() {
		return answerTime;
	}
	public void setAnswerTime(String answerTime) {
		this.answerTime = answerTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	//平均响应时间
	private String avgTime;
	//最短相应时间
	private String minTime;
	//最长相应时间
	private String maxTime;
	//用户
	private String user;
	//部门
	private String section;
	
	public String getAcName() {
		return acName;
	}
	public void setAcName(String acName) {
		this.acName = acName;
	}
	public String getAcCount() {
		return acCount;
	}
	public void setAcCount(String acCount) {
		this.acCount = acCount;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getFucName() {
		return fucName;
	}
	public void setFucName(String fucName) {
		this.fucName = fucName;
	}
	public String getAvgTime() {
		return avgTime;
	}
	public void setAvgTime(String avgTime) {
		this.avgTime = avgTime;
	}
	public String getMinTime() {
		return minTime;
	}
	public void setMinTime(String minTime) {
		this.minTime = minTime;
	}
	public String getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}
}
