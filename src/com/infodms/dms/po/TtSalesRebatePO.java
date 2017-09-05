/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-19 09:40:21
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesRebatePO extends PO{

	private Long auditPer;
	private Long dealerId;
	private String disItem;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Long status;
	private String evidenceNo;
	private String rebNo;
	private Long updateBy;
	private Double rebAmount;
	private Date auditDate;
	private Double usedAmount;
	private Double totalAmount;
	private Long ver;
	private Long useType;
	private String auditRemark;
	private Long yieldly;
	private Date createDate;
	private Long rebId;
	private String comCommand;//数据来源
	
	public String getComCommand() {
		return comCommand;
	}

	public void setComCommand(String comCommand) {
		this.comCommand = comCommand;
	}
	public void setAuditPer(Long auditPer){
		this.auditPer=auditPer;
	}

	public Long getAuditPer(){
		return this.auditPer;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setDisItem(String disItem){
		this.disItem=disItem;
	}

	public String getDisItem(){
		return this.disItem;
	}

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

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setEvidenceNo(String evidenceNo){
		this.evidenceNo=evidenceNo;
	}

	public String getEvidenceNo(){
		return this.evidenceNo;
	}

	public void setRebNo(String rebNo){
		this.rebNo=rebNo;
	}

	public String getRebNo(){
		return this.rebNo;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setRebAmount(Double rebAmount){
		this.rebAmount=rebAmount;
	}

	public Double getRebAmount(){
		return this.rebAmount;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setUsedAmount(Double usedAmount){
		this.usedAmount=usedAmount;
	}

	public Double getUsedAmount(){
		return this.usedAmount;
	}

	public void setTotalAmount(Double totalAmount){
		this.totalAmount=totalAmount;
	}

	public Double getTotalAmount(){
		return this.totalAmount;
	}

	public void setVer(Long ver){
		this.ver=ver;
	}

	public Long getVer(){
		return this.ver;
	}

	public void setUseType(Long useType){
		this.useType=useType;
	}

	public Long getUseType(){
		return this.useType;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setRebId(Long rebId){
		this.rebId=rebId;
	}

	public Long getRebId(){
		return this.rebId;
	}

}