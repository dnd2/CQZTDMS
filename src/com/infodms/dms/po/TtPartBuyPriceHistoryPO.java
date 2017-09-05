/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-11 09:03:03
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartBuyPriceHistoryPO extends PO{

	private Integer state;
	private Date updateDate;
	private Long createBy;
	private String remark;
	private Double guarPrice;
	private Integer isGuard;
	private Integer status;
	private Long spyBy;
	private Long updateBy;
	private Long minPackage;
	private Long partId;
	private Long venderId;
	private Long priceId;
	private Long claimBy;
	private Long disableBy;
	private Long deleteBy;
	private Integer isDefault;
	private Date disableDate;
	private Date deleteDate;
	private Double claimPrice;
	private Long historyId;
	private Double planPrice;
	private Double buyPrice;
	private Date claimDate;
	private Date createDate;
	private Double oldBuyPrice;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
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

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setGuarPrice(Double guarPrice){
		this.guarPrice=guarPrice;
	}

	public Double getGuarPrice(){
		return this.guarPrice;
	}

	public void setIsGuard(Integer isGuard){
		this.isGuard=isGuard;
	}

	public Integer getIsGuard(){
		return this.isGuard;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setSpyBy(Long spyBy){
		this.spyBy=spyBy;
	}

	public Long getSpyBy(){
		return this.spyBy;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setMinPackage(Long minPackage){
		this.minPackage=minPackage;
	}

	public Long getMinPackage(){
		return this.minPackage;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setVenderId(Long venderId){
		this.venderId=venderId;
	}

	public Long getVenderId(){
		return this.venderId;
	}

	public void setPriceId(Long priceId){
		this.priceId=priceId;
	}

	public Long getPriceId(){
		return this.priceId;
	}

	public void setClaimBy(Long claimBy){
		this.claimBy=claimBy;
	}

	public Long getClaimBy(){
		return this.claimBy;
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

	public void setIsDefault(Integer isDefault){
		this.isDefault=isDefault;
	}

	public Integer getIsDefault(){
		return this.isDefault;
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

	public void setClaimPrice(Double claimPrice){
		this.claimPrice=claimPrice;
	}

	public Double getClaimPrice(){
		return this.claimPrice;
	}

	public void setHistoryId(Long historyId){
		this.historyId=historyId;
	}

	public Long getHistoryId(){
		return this.historyId;
	}

	public void setPlanPrice(Double planPrice){
		this.planPrice=planPrice;
	}

	public Double getPlanPrice(){
		return this.planPrice;
	}

	public void setBuyPrice(Double buyPrice){
		this.buyPrice=buyPrice;
	}

	public Double getBuyPrice(){
		return this.buyPrice;
	}

	public void setClaimDate(Date claimDate){
		this.claimDate=claimDate;
	}

	public Date getClaimDate(){
		return this.claimDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOldBuyPrice(Double oldBuyPrice){
		this.oldBuyPrice=oldBuyPrice;
	}

	public Double getOldBuyPrice(){
		return this.oldBuyPrice;
	}

}