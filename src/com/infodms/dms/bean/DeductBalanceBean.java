package com.infodms.dms.bean;

/**
 * 抵扣单结算统计条件
 * 
 * @author XZM
 */
public class DeductBalanceBean {
	/** 经销商代码集合(以","分隔) */
	private String dealerCodes;
	/** 经销商名称 */
	private String dealerName;
	/** 截止日期 */
	private String lastDay;
	/** 抵扣单状态 */
	private String deductStatus;
	/** 是否删除 */
	private String isDel;
	/** 结算延迟天数 */
	private Integer delayDay;
	/** 产地 */
	private String yieldly;
	/** 用户拥有的产地权限 */
	private String yieldlys;
	
	private Long companyId;

	public String getDealerCodes() {
		return dealerCodes;
	}

	public void setDealerCodes(String dealerCodes) {
		this.dealerCodes = dealerCodes;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getLastDay() {
		return lastDay;
	}

	public void setLastDay(String lastDay) {
		this.lastDay = lastDay;
	}

	public String getDeductStatus() {
		return deductStatus;
	}

	public void setDeductStatus(String deductStatus) {
		this.deductStatus = deductStatus;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	public Integer getDelayDay() {
		return delayDay;
	}

	public void setDelayDay(Integer delayDay) {
		this.delayDay = delayDay;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		if(companyId==null)
			companyId = new Long(-1);
		this.companyId = companyId;
	}

	public String getYieldly() {
		return yieldly;
	}

	public void setYieldly(String yieldly) {
		this.yieldly = yieldly;
	}

	public String getYieldlys() {
		return yieldlys;
	}

	public void setYieldlys(String yieldlys) {
		this.yieldlys = yieldlys;
	}
	
}
