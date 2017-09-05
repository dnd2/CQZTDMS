package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmReturnRulePO extends PO{
	private Long ruId;
	private Integer ruLevel;
	private Integer ruItem;
	private Double ruTarget;
	private Double ruWeight;
	private Long createBy;
	private Date createDate;
	private Long updateBy;
	private Date updateDate;
	private Integer var;
	
	public Long getRuId() {
		return ruId;
	}
	public void setRuId(Long ruId) {
		this.ruId = ruId;
	}
	public Integer getRuLevel() {
		return ruLevel;
	}
	public void setRuLevel(Integer ruLevel) {
		this.ruLevel = ruLevel;
	}
	public Integer getRuItem() {
		return ruItem;
	}
	public void setRuItem(Integer ruItem) {
		this.ruItem = ruItem;
	}
	public Double getRuTarget() {
		return ruTarget;
	}
	public void setRuTarget(Double ruTarget) {
		this.ruTarget = ruTarget;
	}
	public Double getRuWeight() {
		return ruWeight;
	}
	public void setRuWeight(Double ruWeight) {
		this.ruWeight = ruWeight;
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
}
