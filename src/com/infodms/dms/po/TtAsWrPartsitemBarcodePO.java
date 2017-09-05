/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-17 17:46:40
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrPartsitemBarcodePO extends PO{

	private String deliveryType;
	private String storageType;
	private String serialNumber;
	private Long updateBy;
	private Long barcodeId;
	private Date updateDate;
	private Long partId;
	private Long createBy;
	private Date createDate;
	private String barcodeNo;
	private Integer isNotice;
	private Date noticeDate;

	public Integer getIsNotice() {
		return isNotice;
	}

	public void setIsNotice(Integer isNotice) {
		this.isNotice = isNotice;
	}

	public Date getNoticeDate() {
		return noticeDate;
	}

	public void setNoticeDate(Date noticeDate) {
		this.noticeDate = noticeDate;
	}

	public void setDeliveryType(String deliveryType){
		this.deliveryType=deliveryType;
	}

	public String getDeliveryType(){
		return this.deliveryType;
	}

	public void setStorageType(String storageType){
		this.storageType=storageType;
	}

	public String getStorageType(){
		return this.storageType;
	}

	public void setSerialNumber(String serialNumber){
		this.serialNumber=serialNumber;
	}

	public String getSerialNumber(){
		return this.serialNumber;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setBarcodeId(Long barcodeId){
		this.barcodeId=barcodeId;
	}

	public Long getBarcodeId(){
		return this.barcodeId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setBarcodeNo(String barcodeNo){
		this.barcodeNo=barcodeNo;
	}

	public String getBarcodeNo(){
		return this.barcodeNo;
	}

}