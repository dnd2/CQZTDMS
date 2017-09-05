/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-06-14 13:44:49
* CreateBy   : fengalon
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtComplaintCustomerTrackPO extends PO{

	private String customerName;
	private String dealWith;
	private Date updateDate;
	private String dealerCode;
	private Long createBy;
	private String cusRequest;
	private String phone;
	private String result;
	private String complaintRecord;
	private Long cusId;
	private Long updateBy;
	private String licenseNo;
	private String times;
	private Date trackDate;
	private Date createDate;
	private String groupCode;

	public void setCustomerName(String customerName){
		this.customerName=customerName;
	}

	public String getCustomerName(){
		return this.customerName;
	}

	public void setDealWith(String dealWith){
		this.dealWith=dealWith;
	}

	public String getDealWith(){
		return this.dealWith;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCusRequest(String cusRequest){
		this.cusRequest=cusRequest;
	}

	public String getCusRequest(){
		return this.cusRequest;
	}

	public void setPhone(String phone){
		this.phone=phone;
	}

	public String getPhone(){
		return this.phone;
	}

	public void setResult(String result){
		this.result=result;
	}

	public String getResult(){
		return this.result;
	}

	public void setComplaintRecord(String complaintRecord){
		this.complaintRecord=complaintRecord;
	}

	public String getComplaintRecord(){
		return this.complaintRecord;
	}

	public void setCusId(Long cusId){
		this.cusId=cusId;
	}

	public Long getCusId(){
		return this.cusId;
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

	public void setTimes(String times){
		this.times=times;
	}

	public String getTimes(){
		return this.times;
	}

	public void setTrackDate(Date trackDate){
		this.trackDate=trackDate;
	}

	public Date getTrackDate(){
		return this.trackDate;
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