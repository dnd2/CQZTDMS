/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-01-15 13:58:03
* CreateBy   : wswcx
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesOtherAcceptPO extends PO{

	private Date expireDate;
	private Date updateDate;
	private Long creId;
	private String acceptNo;
	private String remark;
	private Long createBy;
	private Date inTicketDate;
	private Integer status;
	private Double amount;
	private String bankName;
	private Long updateBy;
	private String bankCode;
	private Long creDetailId;
	private Date createDate;
	private Long finType;
	private Long finInType;
	private String vins;

	public Long getFinType()
	{
		return finType;
	}

	public void setFinType(Long finType)
	{
		this.finType = finType;
	}

	public void setExpireDate(Date expireDate){
		this.expireDate=expireDate;
	}

	public Date getExpireDate(){
		return this.expireDate;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreId(Long creId){
		this.creId=creId;
	}

	public Long getCreId(){
		return this.creId;
	}

	public void setAcceptNo(String acceptNo){
		this.acceptNo=acceptNo;
	}

	public String getAcceptNo(){
		return this.acceptNo;
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

	public void setInTicketDate(Date inTicketDate){
		this.inTicketDate=inTicketDate;
	}

	public Date getInTicketDate(){
		return this.inTicketDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setBankName(String bankName){
		this.bankName=bankName;
	}

	public String getBankName(){
		return this.bankName;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setBankCode(String bankCode){
		this.bankCode=bankCode;
	}

	public String getBankCode(){
		return this.bankCode;
	}

	public void setCreDetailId(Long creDetailId){
		this.creDetailId=creDetailId;
	}

	public Long getCreDetailId(){
		return this.creDetailId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public Long getFinInType() {
		return finInType;
	}

	public void setFinInType(Long finInType) {
		this.finInType = finInType;
	}

	public String getVins() {
		return vins;
	}

	public void setVins(String vins) {
		this.vins = vins;
	}

}