/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-31 14:41:42
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartPalnsortDefinePO extends PO{

	private Float saftyRate;
	private Integer state;
	private Date disableDate;
	private Long sortId;
	private Date deleteDate;
	private Date updateDate;
	private Long createBy;
	private Integer status;
	private String sortType;
	private Long updateBy;
	private Date createDate;
	private Long disableBy;
	private Long deleteBy;
	private Integer saftyCycle;

	public void setSaftyRate(Float saftyRate){
		this.saftyRate=saftyRate;
	}

	public Float getSaftyRate(){
		return this.saftyRate;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setSortId(Long sortId){
		this.sortId=sortId;
	}

	public Long getSortId(){
		return this.sortId;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setSortType(String sortType){
		this.sortType=sortType;
	}

	public String getSortType(){
		return this.sortType;
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

	public void setSaftyCycle(Integer saftyCycle){
		this.saftyCycle=saftyCycle;
	}

	public Integer getSaftyCycle(){
		return this.saftyCycle;
	}

}