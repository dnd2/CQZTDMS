/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-04-25 17:05:30
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartSalesPriceChgPO extends PO{

	private String applyNo;
	private Integer state;
	private Long auditBy;
	private Date validFrom;
	private String remark;
	private Long createBy;
	private Long chgId;
	private Integer status;
	private Double retailPrice;
	private Date validTo;
	private String partCname;
	private Double retailPriceC;
	private Double svcPriceC;
	private Long submitBy;
	private Double svcPrice;
	private Date auditDate;
	private Long partId;
	private String partOldcode;
	private Date createDate;
	private Date submitDate;

	public void setApplyNo(String applyNo){
		this.applyNo=applyNo;
	}

	public String getApplyNo(){
		return this.applyNo;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setAuditBy(Long auditBy){
		this.auditBy=auditBy;
	}

	public Long getAuditBy(){
		return this.auditBy;
	}

	public void setValidFrom(Date validFrom){
		this.validFrom=validFrom;
	}

	public Date getValidFrom(){
		return this.validFrom;
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

	public void setChgId(Long chgId){
		this.chgId=chgId;
	}

	public Long getChgId(){
		return this.chgId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setRetailPrice(Double retailPrice){
		this.retailPrice=retailPrice;
	}

	public Double getRetailPrice(){
		return this.retailPrice;
	}

	public void setValidTo(Date validTo){
		this.validTo=validTo;
	}

	public Date getValidTo(){
		return this.validTo;
	}

	public void setPartCname(String partCname){
		this.partCname=partCname;
	}

	public String getPartCname(){
		return this.partCname;
	}

	public void setRetailPriceC(Double retailPriceC){
		this.retailPriceC=retailPriceC;
	}

	public Double getRetailPriceC(){
		return this.retailPriceC;
	}

	public void setSvcPriceC(Double svcPriceC){
		this.svcPriceC=svcPriceC;
	}

	public Double getSvcPriceC(){
		return this.svcPriceC;
	}

	public void setSubmitBy(Long submitBy){
		this.submitBy=submitBy;
	}

	public Long getSubmitBy(){
		return this.submitBy;
	}

	public void setSvcPrice(Double svcPrice){
		this.svcPrice=svcPrice;
	}

	public Double getSvcPrice(){
		return this.svcPrice;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
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

	public void setSubmitDate(Date submitDate){
		this.submitDate=submitDate;
	}

	public Date getSubmitDate(){
		return this.submitDate;
	}

}