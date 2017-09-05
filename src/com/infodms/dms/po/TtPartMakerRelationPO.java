/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-30 16:35:01
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartMakerRelationPO extends PO{

	private Integer state;
	private Integer isDefault;
	private Date disableDate;
	private Long relaionId;
	private Date deleteDate;
	private Date updateDate;
	private Long createBy;
	private Long makerId;
	private Integer status;
	private Long spyBy;
	private Double claimPrice;
	private Date claimDate;
	private Long updateBy;
	private Long partId;
	private Long claimBy;
	private Date createDate;
	private Long disableBy;
	private Long deleteBy;
	private Double partXs;
	private Double labourXs;
	
	public Double getPartXs() {
		return partXs;
	}

	public void setPartXs(Double partXs) {
		this.partXs = partXs;
	}

	public Double getLabourXs() {
		return labourXs;
	}

	public void setLabourXs(Double labourXs) {
		this.labourXs = labourXs;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
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

	public void setRelaionId(Long relaionId){
		this.relaionId=relaionId;
	}

	public Long getRelaionId(){
		return this.relaionId;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
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

	public void setMakerId(Long makerId){
		this.makerId=makerId;
	}

	public Long getMakerId(){
		return this.makerId;
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

	public void setClaimPrice(Double claimPrice){
		this.claimPrice=claimPrice;
	}

	public Double getClaimPrice(){
		return this.claimPrice;
	}

	public void setClaimDate(Date claimDate){
		this.claimDate=claimDate;
	}

	public Date getClaimDate(){
		return this.claimDate;
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

	public void setClaimBy(Long claimBy){
		this.claimBy=claimBy;
	}

	public Long getClaimBy(){
		return this.claimBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
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

}