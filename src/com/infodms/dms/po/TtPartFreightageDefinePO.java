/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-09-07 17:27:40
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartFreightageDefinePO extends PO{

	private Integer orderType;
	private Integer state;
	private Date disableDate;
	private Double minPirce;
	private Date deleteDate;
	private Double freeCondition;
	private Date updateDate;
	private Long createBy;
	private Integer status;
	private Long defId;
	private Float markupRatio;
	private Long updateBy;
	private Integer dealerType;
	private Integer freeTimes;
	private Long disableBy;
	private Long deleteBy;
	private Integer frgOption;

	public void setOrderType(Integer orderType){
		this.orderType=orderType;
	}

	public Integer getOrderType(){
		return this.orderType;
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

	public void setMinPirce(Double minPirce){
		this.minPirce=minPirce;
	}

	public Double getMinPirce(){
		return this.minPirce;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setFreeCondition(Double freeCondition){
		this.freeCondition=freeCondition;
	}

	public Double getFreeCondition(){
		return this.freeCondition;
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

	public void setDefId(Long defId){
		this.defId=defId;
	}

	public Long getDefId(){
		return this.defId;
	}

	public void setMarkupRatio(Float markupRatio){
		this.markupRatio=markupRatio;
	}

	public Float getMarkupRatio(){
		return this.markupRatio;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDealerType(Integer dealerType){
		this.dealerType=dealerType;
	}

	public Integer getDealerType(){
		return this.dealerType;
	}

	public void setFreeTimes(Integer freeTimes){
		this.freeTimes=freeTimes;
	}

	public Integer getFreeTimes(){
		return this.freeTimes;
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
	
	public void setFrgOption(Integer frgOption){
		this.frgOption=frgOption;
	}

	public Integer getFrgOption(){
		return this.frgOption;
	}

}