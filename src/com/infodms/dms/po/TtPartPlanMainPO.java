/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-12-02 11:14:09
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartPlanMainPO extends PO{

	private Double planCycle;
	private Integer state;
	private Integer partType;
	private Double comeCycle;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Date checkDate;
	private Integer isUrgentIn;
	private Integer planType;
	private Integer status;
	private Long sumQty;
	private Long updateBy;
	private Long venderId;
	private Long deleteBy;
	private Long disableBy;
	private Date confirmDate;
	private Date submitDate;
	private Date yearMonth;
	private String planerName;
	private String remark2;
	private Date disableDate;
	private Date deleteDate;
	private Long planerId;
	private Long planId;
	private Long confirmBy;
	private String planCode;
	private Double amount;
	private Long whId;
	private Integer produceFac;
	private Integer createType;
	private Long orgId;
	private Long buyerId;
	private Long checkBy;
	private String buyer;
	private Long submitBy;
	private String vnederName;
	private String whName;
	private Integer ver;
	private Date createDate;
	private Integer itemNum;
	
	

	public Integer getItemNum() {
		return itemNum;
	}

	public void setItemNum(Integer itemNum) {
		this.itemNum = itemNum;
	}

	public void setPlanCycle(Double planCycle){
		this.planCycle=planCycle;
	}

	public Double getPlanCycle(){
		return this.planCycle;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setPartType(Integer partType){
		this.partType=partType;
	}

	public Integer getPartType(){
		return this.partType;
	}

	public void setComeCycle(Double comeCycle){
		this.comeCycle=comeCycle;
	}

	public Double getComeCycle(){
		return this.comeCycle;
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

	public void setCheckDate(Date checkDate){
		this.checkDate=checkDate;
	}

	public Date getCheckDate(){
		return this.checkDate;
	}

	public void setIsUrgentIn(Integer isUrgentIn){
		this.isUrgentIn=isUrgentIn;
	}

	public Integer getIsUrgentIn(){
		return this.isUrgentIn;
	}

	public void setPlanType(Integer planType){
		this.planType=planType;
	}

	public Integer getPlanType(){
		return this.planType;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setSumQty(Long sumQty){
		this.sumQty=sumQty;
	}

	public Long getSumQty(){
		return this.sumQty;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setVenderId(Long venderId){
		this.venderId=venderId;
	}

	public Long getVenderId(){
		return this.venderId;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setConfirmDate(Date confirmDate){
		this.confirmDate=confirmDate;
	}

	public Date getConfirmDate(){
		return this.confirmDate;
	}

	public void setSubmitDate(Date submitDate){
		this.submitDate=submitDate;
	}

	public Date getSubmitDate(){
		return this.submitDate;
	}

	public void setYearMonth(Date yearMonth){
		this.yearMonth=yearMonth;
	}

	public Date getYearMonth(){
		return this.yearMonth;
	}

	public void setPlanerName(String planerName){
		this.planerName=planerName;
	}

	public String getPlanerName(){
		return this.planerName;
	}

	public void setRemark2(String remark2){
		this.remark2=remark2;
	}

	public String getRemark2(){
		return this.remark2;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setPlanerId(Long planerId){
		this.planerId=planerId;
	}

	public Long getPlanerId(){
		return this.planerId;
	}

	public void setPlanId(Long planId){
		this.planId=planId;
	}

	public Long getPlanId(){
		return this.planId;
	}

	public void setConfirmBy(Long confirmBy){
		this.confirmBy=confirmBy;
	}

	public Long getConfirmBy(){
		return this.confirmBy;
	}

	public void setPlanCode(String planCode){
		this.planCode=planCode;
	}

	public String getPlanCode(){
		return this.planCode;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setProduceFac(Integer produceFac){
		this.produceFac=produceFac;
	}

	public Integer getProduceFac(){
		return this.produceFac;
	}

	public void setCreateType(Integer createType){
		this.createType=createType;
	}

	public Integer getCreateType(){
		return this.createType;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setBuyerId(Long buyerId){
		this.buyerId=buyerId;
	}

	public Long getBuyerId(){
		return this.buyerId;
	}

	public void setCheckBy(Long checkBy){
		this.checkBy=checkBy;
	}

	public Long getCheckBy(){
		return this.checkBy;
	}

	public void setBuyer(String buyer){
		this.buyer=buyer;
	}

	public String getBuyer(){
		return this.buyer;
	}

	public void setSubmitBy(Long submitBy){
		this.submitBy=submitBy;
	}

	public Long getSubmitBy(){
		return this.submitBy;
	}

	public void setVnederName(String vnederName){
		this.vnederName=vnederName;
	}

	public String getVnederName(){
		return this.vnederName;
	}

	public void setWhName(String whName){
		this.whName=whName;
	}

	public String getWhName(){
		return this.whName;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}