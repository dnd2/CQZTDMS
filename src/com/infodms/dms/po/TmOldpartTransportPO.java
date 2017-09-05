/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-06 16:32:50
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmOldpartTransportPO extends PO{

	private String dealerName;
	private Long reportUser;
	private Long updateBy;
	private Long transportId;
	private Date updateDate;
	private String dealerCode;
	private String transportNo;
	private Long createBy;
	private Date checkDate;
	private Long checkUser;
	private Date createDate;
	private Date reportDate;
	private Integer transportStatus;
	private Long dealerId;
	
	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}


	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setReportUser(Long reportUser){
		this.reportUser=reportUser;
	}

	public Long getReportUser(){
		return this.reportUser;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setTransportId(Long transportId){
		this.transportId=transportId;
	}

	public Long getTransportId(){
		return this.transportId;
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

	public void setTransportNo(String transportNo){
		this.transportNo=transportNo;
	}

	public String getTransportNo(){
		return this.transportNo;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCheckDate(Date checkDate){
		this.checkDate=checkDate;
	}

	public Date getCheckDate(){
		return this.checkDate;
	}

	public void setCheckUser(Long checkUser){
		this.checkUser=checkUser;
	}

	public Long getCheckUser(){
		return this.checkUser;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setReportDate(Date reportDate){
		this.reportDate=reportDate;
	}

	public Date getReportDate(){
		return this.reportDate;
	}

	public void setTransportStatus(Integer transportStatus){
		this.transportStatus=transportStatus;
	}

	public Integer getTransportStatus(){
		return this.transportStatus;
	}

}