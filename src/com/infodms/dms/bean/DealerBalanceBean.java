package com.infodms.dms.bean;

import com.infodms.dms.util.CommonUtils;

public class DealerBalanceBean {
	private String startTime; // 结算应索赔单对开始时间
	private String endTime; // 结算应索赔单对结束时间
	private String yieldly; // 产地
	private Integer curPage; // 当前页码
	private Integer pageSize; // 每页数据量
	private Long dealerId;    //经销商ID 
	private Long companyId;   //区分轿车公司和微车公司
	
	/*********Iverson add By 2010-11-17 时间查询条件****************************/
	private String endBalanceDate;// 结算应索赔单对开始时间
	private String conEndDay;// 结算应索赔单对结束时间
	
	public String getEndBalanceDate() {
		return endBalanceDate;
	}

	public void setEndBalanceDate(String endBalanceDate) {
		this.endBalanceDate = endBalanceDate;
	}

	public String getConEndDay() {
		return conEndDay;
	}

	public void setConEndDay(String conEndDay) {
		this.conEndDay = conEndDay;
	}

	/*******Iverson add By 2010-11-17 时间查询条件******************************/
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = CommonUtils.checkNull(startTime);
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = CommonUtils.checkNull(endTime);
	}

	public String getYieldly() {
		return yieldly;
	}

	public void setYieldly(String yieldly) {
		this.yieldly = CommonUtils.checkNull(yieldly);
	}

	public Integer getCurPage() {
		return curPage;
	}

	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

}
