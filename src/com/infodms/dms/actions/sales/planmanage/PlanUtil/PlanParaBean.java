package com.infodms.dms.actions.sales.planmanage.PlanUtil;

public class PlanParaBean {

	//预测允许开始日期
	private int dealerForecastStartDay; 
	//预测允许结束日期
	private int dealerForecastEndDay;
	//预测允许开始日期
	private int sbuForecastStartDay; 
	//预测允许结束日期
	private int sbuForecastEndDay;
	//预测允许开始日期
	private int deptForecastStartDay; 
	//预测允许结束日期
	private int deptForecastEndDay;
	//提前几个月预测
	private int forecastPreMonth;
	//每次预测月数
	private int forecastMonthAmt;
	//展示历史月份数量
	private int showHistoryMonthAmt;

	

	public int getDealerForecastStartDay() {
		return dealerForecastStartDay;
	}

	public void setDealerForecastStartDay(int dealerForecastStartDay) {
		this.dealerForecastStartDay = dealerForecastStartDay;
	}

	public int getDealerForecastEndDay() {
		return dealerForecastEndDay;
	}

	public void setDealerForecastEndDay(int dealerForecastEndDay) {
		this.dealerForecastEndDay = dealerForecastEndDay;
	}

	public int getSbuForecastStartDay() {
		return sbuForecastStartDay;
	}

	public void setSbuForecastStartDay(int sbuForecastStartDay) {
		this.sbuForecastStartDay = sbuForecastStartDay;
	}

	public int getSbuForecastEndDay() {
		return sbuForecastEndDay;
	}

	public void setSbuForecastEndDay(int sbuForecastEndDay) {
		this.sbuForecastEndDay = sbuForecastEndDay;
	}

	public int getDeptForecastStartDay() {
		return deptForecastStartDay;
	}

	public void setDeptForecastStartDay(int deptForecastStartDay) {
		this.deptForecastStartDay = deptForecastStartDay;
	}

	public int getDeptForecastEndDay() {
		return deptForecastEndDay;
	}

	public void setDeptForecastEndDay(int deptForecastEndDay) {
		this.deptForecastEndDay = deptForecastEndDay;
	}

	public int getForecastPreMonth() {
		return forecastPreMonth;
	}

	public void setForecastPreMonth(int forecastPreMonth) {
		this.forecastPreMonth = forecastPreMonth;
	}

	public int getForecastMonthAmt() {
		return forecastMonthAmt;
	}

	public void setForecastMonthAmt(int forecastMonthAmt) {
		this.forecastMonthAmt = forecastMonthAmt;
	}

	public int getShowHistoryMonthAmt() {
		return showHistoryMonthAmt;
	}

	public void setShowHistoryMonthAmt(int showHistoryMonthAmt) {
		this.showHistoryMonthAmt = showHistoryMonthAmt;
	}
}
