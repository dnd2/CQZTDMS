/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-12-02 12:35:06
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartPoMainPO extends PO{

	private Integer state;
	private Integer partType;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Integer isUrgentIn;
	private Integer planType;
	private Integer status;
	private Long sumQty;
	private Date closeDate;
	private Long updateBy;
	private Long disableBy;
	private Long deleteBy;
	private String remark1;
	private Date disableDate;
	private Long planId;
	private Date deleteDate;
	private String orderCode;
	private Double amount;
	private Long whId;
	private Integer produceFac;
	private Long orgId;
	private Long buyerId;
	private Long orderId;
	private String buyer;
	private String whName;
	private Integer ver;
	private Date createDate;
	private String purOrderCode;
    private Date printDate;
    private Long printBy;
    private Integer buyerType;

	public Integer getBuyerType() {
		return buyerType;
	}

	public void setBuyerType(Integer buyerType) {
		this.buyerType = buyerType;
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

	public void setCloseDate(Date closeDate){
		this.closeDate=closeDate;
	}

	public Date getCloseDate(){
		return this.closeDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setRemark1(String remark1){
		this.remark1=remark1;
	}

	public String getRemark1(){
		return this.remark1;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setPlanId(Long planId){
		this.planId=planId;
	}

	public Long getPlanId(){
		return this.planId;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}

	public String getOrderCode(){
		return this.orderCode;
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

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setBuyer(String buyer){
		this.buyer=buyer;
	}

	public String getBuyer(){
		return this.buyer;
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

	public void setPurOrderCode(String purOrderCode){
		this.purOrderCode=purOrderCode;
	}

	public String getPurOrderCode(){
		return this.purOrderCode;
	}

    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

    public Long getPrintBy() {
        return printBy;
    }

    public void setPrintBy(Long printBy) {
        this.printBy = printBy;
    }
}