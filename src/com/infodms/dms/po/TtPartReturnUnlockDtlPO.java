/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2016-06-22 17:26:22
* CreateBy   : fanzhineng
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartReturnUnlockDtlPO extends PO{

	private Date updateDate;
	private String remark;
	private Long createBy;
	private String partCode;
	private Double amount;
	private Long dtlId;
	private String partCname;
	private Double buyPrice;
	private Long updateBy;
	private Long partId;
	private Long applyQty;
	private Long unlocId;
	private String partOldcode;
	private Date createDate;
	private Long rdtlId;
	private Long sellerId;
	private String sellerCode;
	private String sellerName;
	private Long whId;
	private Long inlocId;
	private String soCode;
	private String inCode;
	
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

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setDtlId(Long dtlId){
		this.dtlId=dtlId;
	}

	public Long getDtlId(){
		return this.dtlId;
	}

	public void setPartCname(String partCname){
		this.partCname=partCname;
	}

	public String getPartCname(){
		return this.partCname;
	}

	public void setBuyPrice(Double buyPrice){
		this.buyPrice=buyPrice;
	}

	public Double getBuyPrice(){
		return this.buyPrice;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setApplyQty(Long applyQty){
		this.applyQty=applyQty;
	}

	public Long getApplyQty(){
		return this.applyQty;
	}

	public void setUnlocId(Long unlocId){
		this.unlocId=unlocId;
	}

	public Long getUnlocId(){
		return this.unlocId;
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

	public void setRdtlId(Long rdtlId){
		this.rdtlId=rdtlId;
	}

	public Long getRdtlId(){
		return this.rdtlId;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerCode() {
		return sellerCode;
	}

	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public Long getWhId() {
		return whId;
	}

	public void setWhId(Long whId) {
		this.whId = whId;
	}

	public Long getInlocId() {
		return inlocId;
	}

	public void setInlocId(Long inlocId) {
		this.inlocId = inlocId;
	}

	public String getSoCode() {
		return soCode;
	}

	public void setSoCode(String soCode) {
		this.soCode = soCode;
	}

    public String getInCode() {
        return inCode;
    }

    public void setInCode(String inCode) {
        this.inCode = inCode;
    }

}