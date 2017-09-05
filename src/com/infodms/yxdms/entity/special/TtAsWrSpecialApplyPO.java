/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-04-10 15:35:38
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.special;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrSpecialApplyPO extends PO{

	private String applyNo;
	private String applyRemark;
	private Long dealerId;
	private String vin;
	private Long auditBy;
	private Date applyDate;
	private Integer status;
	private Long applyBy;
	private Integer specialType;
	private Long specialId;
	private Date auditDate;
	private Long id;
	private Double applyAmount;
	private String auditRemark;
	private Long yieldly;
    private Double approvalAmount;
    private Double  totleamount;//总金额
    private Integer  isInvoice;//是否开票
    private String  balanceNo;//结算单号

	
    
    

	public Integer getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(Integer isInvoice) {
		this.isInvoice = isInvoice;
	}

	public String getBalanceNo() {
		return balanceNo;
	}

	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}

	public Double getTotleamount() {
		return totleamount;
	}

	public void setTotleamount(Double totleamount) {
		this.totleamount = totleamount;
	}

	public Double getApprovalAmount() {
		return approvalAmount;
	}

	public void setApprovalAmount(Double approvalAmount) {
		this.approvalAmount = approvalAmount;
	}

	public void setApplyNo(String applyNo){
		this.applyNo=applyNo;
	}

	public String getApplyNo(){
		return this.applyNo;
	}

	public void setApplyRemark(String applyRemark){
		this.applyRemark=applyRemark;
	}

	public String getApplyRemark(){
		return this.applyRemark;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setAuditBy(Long auditBy){
		this.auditBy=auditBy;
	}

	public Long getAuditBy(){
		return this.auditBy;
	}

	public void setApplyDate(Date applyDate){
		this.applyDate=applyDate;
	}

	public Date getApplyDate(){
		return this.applyDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setApplyBy(Long applyBy){
		this.applyBy=applyBy;
	}

	public Long getApplyBy(){
		return this.applyBy;
	}

	public void setSpecialType(Integer specialType){
		this.specialType=specialType;
	}

	public Integer getSpecialType(){
		return this.specialType;
	}

	public void setSpecialId(Long specialId){
		this.specialId=specialId;
	}

	public Long getSpecialId(){
		return this.specialId;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setApplyAmount(Double applyAmount){
		this.applyAmount=applyAmount;
	}

	public Double getApplyAmount(){
		return this.applyAmount;
	}

	public void setAuditRemark(String auditRemark){
		this.auditRemark=auditRemark;
	}

	public String getAuditRemark(){
		return this.auditRemark;
	}

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
	}

}