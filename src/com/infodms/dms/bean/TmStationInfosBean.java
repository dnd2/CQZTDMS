/**********************************************************************
 * <pre>
 * FILE : TmStationInfosBean.java
 * CLASS : TmStationInfosBean
 *
 * AUTHOR : yangyong
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2009-12-24|yangyong| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.bean;

public class TmStationInfosBean {
	
	//关键岗位信息	
	private Long stationId;	
	private String staName;
	
	//关系表状态
	private Long stationMappingId;
	private Integer status;
	
	private String updateDate;
	private String linkTel;
	private Long createBy;
	private String createDate;
	private String auditDate;
	private String work;
	private Integer infosStatus;
	private Long auditUser;
	private Long dealerId;
	private String email;
	private Integer auditStatus;
	private Long updateBy;
	private Integer gender;
	private Long recordId;
	private String name;
	private String personal;
	private String train;
	private String other;
	
	//页面传递过来的申请日期字段
	private String approveDateStart;
	private String approveDateEnd;
	
	//代理商代码、名称
	private String dealerCode;
	private String dealerShortname;	
	
	private String auditName;
	
	public String getAuditName() {
		return auditName;
	}
	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getDealerShortname() {
		return dealerShortname;
	}
	public void setDealerShortname(String dealerShortname) {
		this.dealerShortname = dealerShortname;
	}
	public String getApproveDateStart() {
		return approveDateStart;
	}
	public void setApproveDateStart(String approveDateStart) {
		this.approveDateStart = approveDateStart;
	}
	public String getApproveDateEnd() {
		return approveDateEnd;
	}
	public void setApproveDateEnd(String approveDateEnd) {
		this.approveDateEnd = approveDateEnd;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public String getStaName() {
		return staName;
	}
	public void setStaName(String staName) {
		this.staName = staName;
	}
	public Long getStationMappingId() {
		return stationMappingId;
	}
	public void setStationMappingId(Long stationMappingId) {
		this.stationMappingId = stationMappingId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getLinkTel() {
		return linkTel;
	}
	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public Integer getInfosStatus() {
		return infosStatus;
	}
	public void setInfosStatus(Integer infosStatus) {
		this.infosStatus = infosStatus;
	}
	public Long getAuditUser() {
		return auditUser;
	}
	public void setAuditUser(Long auditUser) {
		this.auditUser = auditUser;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPersonal() {
		return personal;
	}
	public void setPersonal(String personal) {
		this.personal = personal;
	}
	public String getTrain() {
		return train;
	}
	public void setTrain(String train) {
		this.train = train;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
}
