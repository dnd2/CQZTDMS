/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-11-12 12:02:33
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrDiscountPO extends PO{

	private String claimNo;
	private Date discountDate;
	private Long dealerId;
	private String downPartName;
	private String downProductName;
	private Long downProductId;
	private Long createBy;
	private String wrLabourcode;
	private Integer discountSum;
	private String downProductCode;
	private Long downPartId;
	private String wrLabourname;
	private Long claimId;
	private String deductReson;
	private String balanceOder;
	private Long id;
	private String barCode;
	private Double discountPriec;
	private Date createDate;
	private String downPartCode;
	private Double discount;
	private Date fiDate;
	private Double labourPrice;

	public Double getLabourPrice() {
		return labourPrice;
	}

	public void setLabourPrice(Double labourPrice) {
		this.labourPrice = labourPrice;
	}

	public void setClaimNo(String claimNo){
		this.claimNo=claimNo;
	}

	public String getClaimNo(){
		return this.claimNo;
	}

	public void setDiscountDate(Date discountDate){
		this.discountDate=discountDate;
	}

	public Date getDiscountDate(){
		return this.discountDate;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setDownPartName(String downPartName){
		this.downPartName=downPartName;
	}

	public String getDownPartName(){
		return this.downPartName;
	}

	public void setDownProductName(String downProductName){
		this.downProductName=downProductName;
	}

	public String getDownProductName(){
		return this.downProductName;
	}

	public void setDownProductId(Long downProductId){
		this.downProductId=downProductId;
	}

	public Long getDownProductId(){
		return this.downProductId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setWrLabourcode(String wrLabourcode){
		this.wrLabourcode=wrLabourcode;
	}

	public String getWrLabourcode(){
		return this.wrLabourcode;
	}

	public void setDiscountSum(Integer discountSum){
		this.discountSum=discountSum;
	}

	public Integer getDiscountSum(){
		return this.discountSum;
	}

	public void setDownProductCode(String downProductCode){
		this.downProductCode=downProductCode;
	}

	public String getDownProductCode(){
		return this.downProductCode;
	}

	public void setDownPartId(Long downPartId){
		this.downPartId=downPartId;
	}

	public Long getDownPartId(){
		return this.downPartId;
	}

	public void setWrLabourname(String wrLabourname){
		this.wrLabourname=wrLabourname;
	}

	public String getWrLabourname(){
		return this.wrLabourname;
	}

	public void setClaimId(Long claimId){
		this.claimId=claimId;
	}

	public Long getClaimId(){
		return this.claimId;
	}

	public void setDeductReson(String deductReson){
		this.deductReson=deductReson;
	}

	public String getDeductReson(){
		return this.deductReson;
	}

	public void setBalanceOder(String balanceOder){
		this.balanceOder=balanceOder;
	}

	public String getBalanceOder(){
		return this.balanceOder;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setBarCode(String barCode){
		this.barCode=barCode;
	}

	public String getBarCode(){
		return this.barCode;
	}

	public void setDiscountPriec(Double discountPriec){
		this.discountPriec=discountPriec;
	}

	public Double getDiscountPriec(){
		return this.discountPriec;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDownPartCode(String downPartCode){
		this.downPartCode=downPartCode;
	}

	public String getDownPartCode(){
		return this.downPartCode;
	}

	public void setDiscount(Double discount){
		this.discount=discount;
	}

	public Double getDiscount(){
		return this.discount;
	}

	public void setFiDate(Date fiDate){
		this.fiDate=fiDate;
	}

	public Date getFiDate(){
		return this.fiDate;
	}

}