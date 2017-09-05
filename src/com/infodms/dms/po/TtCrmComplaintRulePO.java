package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmComplaintRulePO extends PO{
	private Long cuId;
	private Integer cuLevel;
	private Integer cuItem;
	private Double minCuTarget;
	private Double maxCuTarget;
	private Double cuWeight;
	private Long createBy;
	private Date createDate;
	private Long updateBy;
	private Date updateDate;
	private Integer var;
	
	public Long getCuId() {
		return cuId;
	}
	public void setCuId(Long cuId) {
		this.cuId = cuId;
	}
	
	public Integer getCuItem() {
		return cuItem;
	}
	public void setCuItem(Integer cuItem) {
		this.cuItem = cuItem;
	}
	public Double getCuWeight() {
		return cuWeight;
	}
	public void setCuWeight(Double cuWeight) {
		this.cuWeight = cuWeight;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Integer getVar() {
		return var;
	}
	public void setVar(Integer var) {
		this.var = var;
	}
	public void setCuLevel(Integer cuLevel) {
		this.cuLevel = cuLevel;
	}
	public Integer getCuLevel() {
		return cuLevel;
	}
	public Double getMinCuTarget() {
		return minCuTarget;
	}
	public void setMinCuTarget(Double minCuTarget) {
		this.minCuTarget = minCuTarget;
	}
	public Double getMaxCuTarget() {
		return maxCuTarget;
	}
	public void setMaxCuTarget(Double maxCuTarget) {
		this.maxCuTarget = maxCuTarget;
	}
}
