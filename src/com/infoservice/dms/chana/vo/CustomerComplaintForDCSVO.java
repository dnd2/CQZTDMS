package com.infoservice.dms.chana.vo;

import java.util.Date;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class CustomerComplaintForDCSVO extends BaseVO {

	private String complaintNo; //下端：投诉编号  CHAR(12) TSBH20100625001748前面6位截掉 上端：COMP_CODE VARCHAR2(20) 
	private String complaintName; //下端：投诉人姓名  varchar(30)  上端：LINK_MAN VARCHAR2(15) 
	private Integer complaintGender; //下端：投诉人性别  NUMERIC(8)  上端：SEX NUMBER(8) 
	private Integer complaintCorp; //下端：投诉人单位  varchar(120)  上端：?  
	private String complaintPhone; //下端：投诉人电话  VARCHAR(30)  上端：TEL VARCHAR2(15) 
	private String complaintMobile; //下端：投诉人手机  VARCHAR(31)  上端：?  
	private Integer complaintType; //下端：投诉类型  NUMERIC(8) 11241001服务人员服务态度
	private Integer complaintMainType; //下端：投诉大类  NUMERIC(8)  上端：TC_CODE(type = 1065) NUMBER(8) 
	private Integer complaintSubType; //下端：投诉小类  NUMERIC(8)  上端：TC_CODE(type = 1089)表里应该加上 NUMBER(8) 
	private Date complaintDate; //下端：投诉日期  TIMESTAMP  上端：CREATE_DATE ？ DATE 
	private String complaintSummary; //下端：投诉摘要  VARCHAR(6000)  上端：COMP_CONTENT VARCHAR2(600) 
	private String complaintReason; //下端：投诉原因  VARCHAR(600)  上端：COMP_CONTENT VARCHAR2(600) 
	private String resolvent; //下端：解决方案  VARCHAR(600)  上端：?  
	private Integer dealStatus; //下端：处理状态  NUMERIC(8) 12871001 未处理，
	private Integer closeStatus; //下端：是否已结案  NUMERIC(8) 默认11201001（未结案） 上端：写死未结案  
	private Integer complaintResult; //下端：回访结果  NUMERIC(8) 11351001 非常满意
	private Integer complaintSerious; //下端：严重性  NUMERIC(8) 11231001 轻微
	private Integer priority; //下端：优先级  NUMERIC(8)  上端：  
	private String license; //下端：车牌号  VARCHAR(30)  上端：LICENSE_NO VARCHAR2(30) 
	private String vin; //下端：VIN  VARCHAR(17)  上端：VIN VARCHAR2(17) 
	private String engineNo; //下端：发动机号  VARCHAR(30)  上端：ENGINE_NO VARCHAR2(30) 
	private String ownerName; //下端：车主  VARCHAR(120)  上端：?  
	private String linkAddress; //下端：车主地址  VARCHAR(120)  上端：?  
	private Integer complaintOrigin; //下端：投诉来源  NUMERIC(8)  上端：COMP_SOURCE(TC_CODE code_id = 10591002) NUMBER(8) 
	private Integer complaintStatus; //下端：投诉状态  NUMERIC(8) 1120 未结案
	//下发的list
	private LinkedList dealVoList; //下端：处理信息列表    上端：  
	
	public String getComplaintNo() {
		return complaintNo;
	}
	public void setComplaintNo(String complaintNo) {
		this.complaintNo = complaintNo;
	}
	public String getComplaintName() {
		return complaintName;
	}
	public void setComplaintName(String complaintName) {
		this.complaintName = complaintName;
	}
	public Integer getComplaintGender() {
		return complaintGender;
	}
	public void setComplaintGender(Integer complaintGender) {
		this.complaintGender = complaintGender;
	}
	public Integer getComplaintCorp() {
		return complaintCorp;
	}
	public void setComplaintCorp(Integer complaintCorp) {
		this.complaintCorp = complaintCorp;
	}
	public String getComplaintPhone() {
		return complaintPhone;
	}
	public void setComplaintPhone(String complaintPhone) {
		this.complaintPhone = complaintPhone;
	}
	public String getComplaintMobile() {
		return complaintMobile;
	}
	public void setComplaintMobile(String complaintMobile) {
		this.complaintMobile = complaintMobile;
	}
	public Integer getComplaintType() {
		return complaintType;
	}
	public void setComplaintType(Integer complaintType) {
		this.complaintType = complaintType;
	}
	public Integer getComplaintMainType() {
		return complaintMainType;
	}
	public void setComplaintMainType(Integer complaintMainType) {
		this.complaintMainType = complaintMainType;
	}
	public Integer getComplaintSubType() {
		return complaintSubType;
	}
	public void setComplaintSubType(Integer complaintSubType) {
		this.complaintSubType = complaintSubType;
	}
	public Date getComplaintDate() {
		return complaintDate;
	}
	public void setComplaintDate(Date complaintDate) {
		this.complaintDate = complaintDate;
	}
	public String getComplaintSummary() {
		return complaintSummary;
	}
	public void setComplaintSummary(String complaintSummary) {
		this.complaintSummary = complaintSummary;
	}
	public String getComplaintReason() {
		return complaintReason;
	}
	public void setComplaintReason(String complaintReason) {
		this.complaintReason = complaintReason;
	}
	public String getResolvent() {
		return resolvent;
	}
	public void setResolvent(String resolvent) {
		this.resolvent = resolvent;
	}
	public Integer getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(Integer dealStatus) {
		this.dealStatus = dealStatus;
	}
	public Integer getCloseStatus() {
		return closeStatus;
	}
	public void setCloseStatus(Integer closeStatus) {
		this.closeStatus = closeStatus;
	}
	public Integer getComplaintResult() {
		return complaintResult;
	}
	public void setComplaintResult(Integer complaintResult) {
		this.complaintResult = complaintResult;
	}
	public Integer getComplaintSerious() {
		return complaintSerious;
	}
	public void setComplaintSerious(Integer complaintSerious) {
		this.complaintSerious = complaintSerious;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getLinkAddress() {
		return linkAddress;
	}
	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}
	public Integer getComplaintOrigin() {
		return complaintOrigin;
	}
	public void setComplaintOrigin(Integer complaintOrigin) {
		this.complaintOrigin = complaintOrigin;
	}
	public Integer getComplaintStatus() {
		return complaintStatus;
	}
	public void setComplaintStatus(Integer complaintStatus) {
		this.complaintStatus = complaintStatus;
	}
	public LinkedList getDealVoList() {
		return dealVoList;
	}
	public void setDealVoList(LinkedList dealVoList) {
		this.dealVoList = dealVoList;
	}


}
