/**********************************************************************
 * <pre>
 * FILE : TmFleetinfoProductBean.java
 * CLASS : TmFleetinfoProductBean
 *
 * AUTHOR : zzg
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2009-12-23|zzg| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.bean;

public class TmFleetinfoProductBean {
	private Long fleetId;
	private Long requireId;
	private Long brandId;
	private Long series;
	private Long modelId;
	private String remark;
	private String brandName;
	private String modelName;
	private Integer requireNum;
	private String createDate;
	private Long createUser;
	private Long updateUser;
	private String updateDate;
	private String dealerArea;
	private String giveCarDate;
	public Long getFleetId() {
		return fleetId;
	}
	public void setFleetId(Long fleetId) {
		this.fleetId = fleetId;
	}
	public Long getRequireId() {
		return requireId;
	}
	public void setRequireId(Long requireId) {
		this.requireId = requireId;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Long getSeries() {
		return series;
	}
	public void setSeries(Long series) {
		this.series = series;
	}
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getRequireNum() {
		return requireNum;
	}
	public void setRequireNum(Integer requireNum) {
		this.requireNum = requireNum;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	public Long getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getDealerArea() {
		return dealerArea;
	}
	public void setDealerArea(String dealerArea) {
		this.dealerArea = dealerArea;
	}
	public String getGiveCarDate() {
		return giveCarDate;
	}
	public void setGiveCarDate(String giveCarDate) {
		this.giveCarDate = giveCarDate;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
}
