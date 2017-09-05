/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-06-28 15:22:18
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartSalesRelationPO extends PO{

	private Integer state;
	private String childorgName;
	private String childorgCode;
	private Date disableDate;
	private Date deleteDate;
	private Long childorgId;
	private Date updateDate;
	private Long createBy;
	private Integer status;
	private Long boDays;
	private Long updateBy;
	private Long parentorgId;
	private String parentorgCode;
	private Long deleteBy;
	private Long disableBy;
	private String parentorgName;
	private Date createDate;
	private String priceCode;
	private Double discount;
	private Long relationId;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setChildorgName(String childorgName){
		this.childorgName=childorgName;
	}

	public String getChildorgName(){
		return this.childorgName;
	}

	public void setChildorgCode(String childorgCode){
		this.childorgCode=childorgCode;
	}

	public String getChildorgCode(){
		return this.childorgCode;
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

	public void setChildorgId(Long childorgId){
		this.childorgId=childorgId;
	}

	public Long getChildorgId(){
		return this.childorgId;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setBoDays(Long boDays){
		this.boDays=boDays;
	}

	public Long getBoDays(){
		return this.boDays;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setParentorgId(Long parentorgId){
		this.parentorgId=parentorgId;
	}

	public Long getParentorgId(){
		return this.parentorgId;
	}

	public void setParentorgCode(String parentorgCode){
		this.parentorgCode=parentorgCode;
	}

	public String getParentorgCode(){
		return this.parentorgCode;
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

	public void setParentorgName(String parentorgName){
		this.parentorgName=parentorgName;
	}

	public String getParentorgName(){
		return this.parentorgName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPriceCode(String priceCode){
		this.priceCode=priceCode;
	}

	public String getPriceCode(){
		return this.priceCode;
	}

	public void setDiscount(Double discount){
		this.discount=discount;
	}

	public Double getDiscount(){
		return this.discount;
	}

	public void setRelationId(Long relationId){
		this.relationId=relationId;
	}

	public Long getRelationId(){
		return this.relationId;
	}

}