package com.infodms.dms.bean;

import com.infodms.dms.po.TtAsActivityPO;

public class ActivityDealerBean extends TtAsActivityPO {
	private Integer dealerSum;
	private Integer areaSum;
	private Long orgId;
	private String orgCode;
	private String orgName;
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Integer getDealerSum() {
		return dealerSum;
	}
	public void setDealerSum(Integer dealerSum) {
		this.dealerSum = dealerSum;
	}
	public Integer getAreaSum() {
		return areaSum;
	}
	public void setAreaSum(Integer areaSum) {
		this.areaSum = areaSum;
	}
	
}
