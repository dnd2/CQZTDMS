package com.infodms.dms.bean;

/**
 * 抵扣通知查询条件
 * 
 * @author XZM
 */
public class DeduceNoticeBean {
	/** 抵扣单号 */
	private String transportNO;
	/** 抵扣通知时间范围（开始） */
	private String noticeDateS;
	/** 抵扣通知时间范围（结束） */
	private String noticeDateE;
	/** 经销商ID */
	private String dealerId;
	/** 经销商代码集合（以","分隔） */
	private String dealerCodes;
	/** 经销商名称 */
	private String dealerName;
	/** 抵扣单状态 */
	private String deductStatus;
	/** 登陆人companyId */
	private Long companyId;
	/** 用户拥有产地权限 */
	private String yieldlys;

	public String getTransportNO() {
		return transportNO;
	}

	public void setTransportNO(String transportNO) {
		this.transportNO = transportNO;
	}

	public String getNoticeDateS() {
		return noticeDateS;
	}

	public void setNoticeDateS(String noticeDateS) {
		this.noticeDateS = noticeDateS;
	}

	public String getNoticeDateE() {
		return noticeDateE;
	}

	public void setNoticeDateE(String noticeDateE) {
		this.noticeDateE = noticeDateE;
	}

	public String getDealerId() {
		return dealerId;
	}

	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}

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

	public String getDeductStatus() {
		return deductStatus;
	}

	public void setDeductStatus(String deductStatus) {
		this.deductStatus = deductStatus;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getYieldlys() {
		return yieldlys;
	}

	public void setYieldlys(String yieldlys) {
		this.yieldlys = yieldlys;
	}
	
}
