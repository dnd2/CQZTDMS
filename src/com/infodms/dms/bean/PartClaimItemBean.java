package com.infodms.dms.bean;

public class PartClaimItemBean {
	//临时id
	private String uuid;
	//索赔项ID
	private String itemId;
	//配件ID
	private String partId;
	//配件代码
	private String partCode;
	//配件名称
	private String partName;
	//配件单位
	private String unit;
	//订货数量
	private String orderCount;
	//货运数量
	private String transCount;
	//签收数量
	private String signCount;
	//索赔数量
	private String claimCount;
	//索赔类型
	private String claimType;
	//备注
	private String remark;
	//索赔单ID
	private String claimId;
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getPartId() {
		return partId;
	}
	public void setPartId(String partId) {
		this.partId = partId;
	}
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}
	public String getTransCount() {
		return transCount;
	}
	public void setTransCount(String transCount) {
		this.transCount = transCount;
	}
	public String getSignCount() {
		return signCount;
	}
	public void setSignCount(String signCount) {
		this.signCount = signCount;
	}
	public String getClaimCount() {
		return claimCount;
	}
	public void setClaimCount(String claimCount) {
		this.claimCount = claimCount;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/*
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((claimType == null) ? 0 : claimType.hashCode());
		result = prime * result + ((partId == null) ? 0 : partId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PartClaimItemBean other = (PartClaimItemBean) obj;
		if (claimType == null) {
			if (other.claimType != null)
				return false;
		} else if (!claimType.equals(other.claimType))
			return false;
		if (partId == null) {
			if (other.partId != null)
				return false;
		} else if (!partId.equals(other.partId))
			return false;
		return true;
	}
	*/
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getClaimId() {
		return claimId;
	}
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
}
