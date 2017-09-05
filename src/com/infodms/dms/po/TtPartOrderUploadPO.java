/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-07-10 17:54:10
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartOrderUploadPO extends PO{

	private Integer orderType;
	private Long uploadId;
	private Long dealerId;
	private String dealerCode;
	private String remark;
	private Long createBy;
	private Integer status;
	private Long repartId;
	private String activityCode;
	private Long partNum;
	private String dealerName;
	private Long partId;
	private String repartOldcode;
	private String partOldcode;
	private Date createDate;

	public void setOrderType(Integer orderType){
		this.orderType=orderType;
	}

	public Integer getOrderType(){
		return this.orderType;
	}

	public void setUploadId(Long uploadId){
		this.uploadId=uploadId;
	}

	public Long getUploadId(){
		return this.uploadId;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setRepartId(Long repartId){
		this.repartId=repartId;
	}

	public Long getRepartId(){
		return this.repartId;
	}

	public void setActivityCode(String activityCode){
		this.activityCode=activityCode;
	}

	public String getActivityCode(){
		return this.activityCode;
	}

	public void setPartNum(Long partNum){
		this.partNum=partNum;
	}

	public Long getPartNum(){
		return this.partNum;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setRepartOldcode(String repartOldcode){
		this.repartOldcode=repartOldcode;
	}

	public String getRepartOldcode(){
		return this.repartOldcode;
	}

	public void setPartOldcode(String partOldcode){
		this.partOldcode=partOldcode;
	}

	public String getPartOldcode(){
		return this.partOldcode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}