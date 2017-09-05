/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-11-11 16:49:25
* CreateBy   : chenyu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrCompensationAppPO extends PO{

	private String claimNo;
	private String roNo;
	private String reason;
	private String partName;
	private Double passPrice;
	private Long pkid;
	private String supplierCode;
	private Date createDate;
	private String partCode;
	private Double applyPrice;

	public void setClaimNo(String claimNo){
		this.claimNo=claimNo;
	}

	public String getClaimNo(){
		return this.claimNo;
	}

	public void setRoNo(String roNo){
		this.roNo=roNo;
	}

	public String getRoNo(){
		return this.roNo;
	}

	public void setReason(String reason){
		this.reason=reason;
	}

	public String getReason(){
		return this.reason;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setPassPrice(Double passPrice){
		this.passPrice=passPrice;
	}

	public Double getPassPrice(){
		return this.passPrice;
	}

	public void setPkid(Long pkid){
		this.pkid=pkid;
	}

	public Long getPkid(){
		return this.pkid;
	}

	public void setSupplierCode(String supplierCode){
		this.supplierCode=supplierCode;
	}

	public String getSupplierCode(){
		return this.supplierCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setApplyPrice(Double applyPrice){
		this.applyPrice=applyPrice;
	}

	public Double getApplyPrice(){
		return this.applyPrice;
	}

}