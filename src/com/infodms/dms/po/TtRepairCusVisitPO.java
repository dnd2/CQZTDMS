/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-06-11 14:48:06
* CreateBy   : fengalon
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtRepairCusVisitPO extends PO{

	private String threeGuarantees;
	private String customerName;
	private String satisfied;
	private String noSatisfied;
	private String dealerCode;
	private String noSatisfiedReason;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private String phone;
	private Date visitDate;
	private Long cusId;
	private String repairItem;
	private Long updateBy;
	private String licenseNo;
	private String noVisitReason;
	private String isRecommend;
	private Date createDate;
	private String groupCode;
	private String orgCode;
	private String regionCode;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public void setThreeGuarantees(String threeGuarantees){
		this.threeGuarantees=threeGuarantees;
	}

	public String getThreeGuarantees(){
		return this.threeGuarantees;
	}

	public void setCustomerName(String customerName){
		this.customerName=customerName;
	}

	public String getCustomerName(){
		return this.customerName;
	}

	public void setSatisfied(String satisfied){
		this.satisfied=satisfied;
	}

	public String getSatisfied(){
		return this.satisfied;
	}

	public void setNoSatisfied(String noSatisfied){
		this.noSatisfied=noSatisfied;
	}

	public String getNoSatisfied(){
		return this.noSatisfied;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setNoSatisfiedReason(String noSatisfiedReason){
		this.noSatisfiedReason=noSatisfiedReason;
	}

	public String getNoSatisfiedReason(){
		return this.noSatisfiedReason;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setPhone(String phone){
		this.phone=phone;
	}

	public String getPhone(){
		return this.phone;
	}

	public void setVisitDate(Date visitDate){
		this.visitDate=visitDate;
	}

	public Date getVisitDate(){
		return this.visitDate;
	}

	public void setCusId(Long cusId){
		this.cusId=cusId;
	}

	public Long getCusId(){
		return this.cusId;
	}

	public void setRepairItem(String repairItem){
		this.repairItem=repairItem;
	}

	public String getRepairItem(){
		return this.repairItem;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setLicenseNo(String licenseNo){
		this.licenseNo=licenseNo;
	}

	public String getLicenseNo(){
		return this.licenseNo;
	}

	public void setNoVisitReason(String noVisitReason){
		this.noVisitReason=noVisitReason;
	}

	public String getNoVisitReason(){
		return this.noVisitReason;
	}

	public void setIsRecommend(String isRecommend){
		this.isRecommend=isRecommend;
	}

	public String getIsRecommend(){
		return this.isRecommend;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setGroupCode(String groupCode){
		this.groupCode=groupCode;
	}

	public String getGroupCode(){
		return this.groupCode;
	}

}