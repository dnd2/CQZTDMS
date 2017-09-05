/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-12 20:34:13
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartDiscountDefineDtlPO extends PO{

	private Integer state;
	private Long discountId;
	private Long dpId;
	private Float rate;
	private Date disableDate;
	private String dpCode;
	private Date deleteDate;
	private Date updateDate;
	private Long createBy;
	private Long amount;
	private Integer status;
	private Long perAmount;
	private Long dtlId;
	private String dpName;
	private Long updateBy;
	private Date createDate;
	private Long disableBy;
	private Long deleteBy;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setDiscountId(Long discountId){
		this.discountId=discountId;
	}

	public Long getDiscountId(){
		return this.discountId;
	}

	public void setDpId(Long dpId){
		this.dpId=dpId;
	}

	public Long getDpId(){
		return this.dpId;
	}

	public void setRate(Float rate){
		this.rate=rate;
	}

	public Float getRate(){
		return this.rate;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setDpCode(String dpCode){
		this.dpCode=dpCode;
	}

	public String getDpCode(){
		return this.dpCode;
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

	public void setAmount(Long amount){
		this.amount=amount;
	}

	public Long getAmount(){
		return this.amount;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setPerAmount(Long perAmount){
		this.perAmount=perAmount;
	}

	public Long getPerAmount(){
		return this.perAmount;
	}

	public void setDtlId(Long dtlId){
		this.dtlId=dtlId;
	}

	public Long getDtlId(){
		return this.dtlId;
	}

	public void setDpName(String dpName){
		this.dpName=dpName;
	}

	public String getDpName(){
		return this.dpName;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
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