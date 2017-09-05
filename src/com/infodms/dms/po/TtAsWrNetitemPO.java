/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-14 11:20:21
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrNetitemPO extends PO{

	private Long netitemId;
	private Date updateDate;
	private Integer isAgree;
	private Long createBy;
	private String itemCode;
	private Date createDate;
	private Integer payType;
	private Double amount;
	private Long debitNetitemId;
	private String itemDesc;
	private String authCode;
	private Long updateBy;
	private Double balanceAmount;
	private Integer isFore;
	private Double deductAmount;
	private String remark;
	private Long id;
	private String auditCon;
	private Double applyAmount;
	private String mainPartCode;
	public String getMainPartCode() {
		return mainPartCode;
	}

	public void setMainPartCode(String mainPartCode) {
		this.mainPartCode = mainPartCode;
	}

	public Double getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(Double applyAmount) {
		this.applyAmount = applyAmount;
	}

	public String getAuditCon() {
		return auditCon;
	}

	public void setAuditCon(String auditCon) {
		this.auditCon = auditCon;
	}

	public void setNetitemId(Long netitemId){
		this.netitemId=netitemId;
	}

	public Long getNetitemId(){
		return this.netitemId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setIsAgree(Integer isAgree){
		this.isAgree=isAgree;
	}

	public Integer getIsAgree(){
		return this.isAgree;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setItemCode(String itemCode){
		this.itemCode=itemCode;
	}

	public String getItemCode(){
		return this.itemCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPayType(Integer payType){
		this.payType=payType;
	}

	public Integer getPayType(){
		return this.payType;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setDebitNetitemId(Long debitNetitemId){
		this.debitNetitemId=debitNetitemId;
	}

	public Long getDebitNetitemId(){
		return this.debitNetitemId;
	}

	public void setItemDesc(String itemDesc){
		this.itemDesc=itemDesc;
	}

	public String getItemDesc(){
		return this.itemDesc;
	}

	public void setAuthCode(String authCode){
		this.authCode=authCode;
	}

	public String getAuthCode(){
		return this.authCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setBalanceAmount(Double balanceAmount){
		this.balanceAmount=balanceAmount;
	}

	public Double getBalanceAmount(){
		return this.balanceAmount;
	}

	public void setIsFore(Integer isFore){
		this.isFore=isFore;
	}

	public Integer getIsFore(){
		return this.isFore;
	}

	public void setDeductAmount(Double deductAmount){
		this.deductAmount=deductAmount;
	}

	public Double getDeductAmount(){
		return this.deductAmount;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}