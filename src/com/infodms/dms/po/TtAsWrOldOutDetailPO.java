/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-31 09:36:03
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrOldOutDetailPO extends PO{

	private String claimNo;
	private Integer isDikou;
	private Long outType;
	private Integer outAmount;
	private String supplayCode;
	private String noticeNo;
	private String outNo;
	private String remark;
	private Long outBy;
	private String outPartCode;
	private String barcodeNo;
	private Integer outPartType;
	private Date outDate;
	private Long oemCompanyId;
	private String supplayName;
	private Long id;
	private Integer yieldly;
	private String outSerial;//出库号
	private Integer isOutDoor;
	public Integer getIsOutDoor() {
		return isOutDoor;
	}

	public void setIsOutDoor(Integer isOutDoor) {
		this.isOutDoor = isOutDoor;
	}

	public String getOutSerial() {
		return outSerial;
	}

	public void setOutSerial(String outSerial) {
		this.outSerial = outSerial;
	}

	public void setClaimNo(String claimNo){
		this.claimNo=claimNo;
	}

	public String getClaimNo(){
		return this.claimNo;
	}

	public void setIsDikou(Integer isDikou){
		this.isDikou=isDikou;
	}

	public Integer getIsDikou(){
		return this.isDikou;
	}

	public void setOutType(Long outType){
		this.outType=outType;
	}

	public Long getOutType(){
		return this.outType;
	}

	public void setOutAmount(Integer outAmount){
		this.outAmount=outAmount;
	}

	public Integer getOutAmount(){
		return this.outAmount;
	}

	public void setSupplayCode(String supplayCode){
		this.supplayCode=supplayCode;
	}

	public String getSupplayCode(){
		return this.supplayCode;
	}

	public void setNoticeNo(String noticeNo){
		this.noticeNo=noticeNo;
	}

	public String getNoticeNo(){
		return this.noticeNo;
	}

	public void setOutNo(String outNo){
		this.outNo=outNo;
	}

	public String getOutNo(){
		return this.outNo;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setOutBy(Long outBy){
		this.outBy=outBy;
	}

	public Long getOutBy(){
		return this.outBy;
	}

	public void setOutPartCode(String outPartCode){
		this.outPartCode=outPartCode;
	}

	public String getOutPartCode(){
		return this.outPartCode;
	}

	public void setBarcodeNo(String barcodeNo){
		this.barcodeNo=barcodeNo;
	}

	public String getBarcodeNo(){
		return this.barcodeNo;
	}

	public void setOutPartType(Integer outPartType){
		this.outPartType=outPartType;
	}

	public Integer getOutPartType(){
		return this.outPartType;
	}

	public void setOutDate(Date outDate){
		this.outDate=outDate;
	}

	public Date getOutDate(){
		return this.outDate;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setSupplayName(String supplayName){
		this.supplayName=supplayName;
	}

	public String getSupplayName(){
		return this.supplayName;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setYieldly(Integer yieldly){
		this.yieldly=yieldly;
	}

	public Integer getYieldly(){
		return this.yieldly;
	}

}