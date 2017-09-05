/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-03-29 16:26:23
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtFleetContractPO extends PO{

	private Date updateDate;
	private Long createBy;
	private String discount;
	private Date createDate;
	private Date auditDate;
	private Long dlrCompanyId;
	private Double disAmount;
	private String sellTo;
	private Integer contractAmount;
	private String backReson;
	private Integer status;
	private String buyFrom;
	private Integer contractType;
	
	public Integer getContractType()
	{
		return contractType;
	}

	public void setContractType(Integer contractType)
	{
		this.contractType = contractType;
	}

	public Long getDealerId()
	{
		return dealerId;
	}

	public void setDealerId(Long dealerId)
	{
		this.dealerId = dealerId;
	}

	private Date checkDate;
	private Long updateBy;
	private Long intentId;
	private Long oemCompanyId;
	private String otherRemark;
	private String contractNo;
	private Double otherAmount;
	private Long contractId;
	private Date startDate;
	private Long fleetId;
	private Date endDate;
	private Long checkUser ;
	private Long dealerId;

	public Long getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(Long checkUser) {
		this.checkUser = checkUser;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setDiscount(String discount){
		this.discount=discount;
	}

	public String getDiscount(){
		return this.discount;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setDlrCompanyId(Long dlrCompanyId){
		this.dlrCompanyId=dlrCompanyId;
	}

	public Long getDlrCompanyId(){
		return this.dlrCompanyId;
	}

	public void setDisAmount(Double disAmount){
		this.disAmount=disAmount;
	}

	public Double getDisAmount(){
		return this.disAmount;
	}

	public void setSellTo(String sellTo){
		this.sellTo=sellTo;
	}

	public String getSellTo(){
		return this.sellTo;
	}

	public void setContractAmount(Integer contractAmount){
		this.contractAmount=contractAmount;
	}

	public Integer getContractAmount(){
		return this.contractAmount;
	}

	public void setBackReson(String backReson){
		this.backReson=backReson;
	}

	public String getBackReson(){
		return this.backReson;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setBuyFrom(String buyFrom){
		this.buyFrom=buyFrom;
	}

	public String getBuyFrom(){
		return this.buyFrom;
	}

	public void setCheckDate(Date checkDate){
		this.checkDate=checkDate;
	}

	public Date getCheckDate(){
		return this.checkDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setIntentId(Long intentId){
		this.intentId=intentId;
	}

	public Long getIntentId(){
		return this.intentId;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setOtherRemark(String otherRemark){
		this.otherRemark=otherRemark;
	}

	public String getOtherRemark(){
		return this.otherRemark;
	}

	public void setContractNo(String contractNo){
		this.contractNo=contractNo;
	}

	public String getContractNo(){
		return this.contractNo;
	}

	public void setOtherAmount(Double otherAmount){
		this.otherAmount=otherAmount;
	}

	public Double getOtherAmount(){
		return this.otherAmount;
	}

	public void setContractId(Long contractId){
		this.contractId=contractId;
	}

	public Long getContractId(){
		return this.contractId;
	}

	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}

	public Date getStartDate(){
		return this.startDate;
	}

	public void setFleetId(Long fleetId){
		this.fleetId=fleetId;
	}

	public Long getFleetId(){
		return this.fleetId;
	}

	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}

	public Date getEndDate(){
		return this.endDate;
	}

}