/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-08-20 20:04:14
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsVinPartItemsReturnPO extends PO{

	private Long vrtId;
	private Date wrEndDate;
	private Double wrMileage;
	private Double curMileage;
	private Integer partWrType;
	private String vin;
	private Date updateDate;
	private String partName;
	private Long createBy;
	private Integer curWrTimes;
	private String partCode;
	private Date buyDate;
	private Integer wrMonth;
	private Long updateBy;
	private Integer curTimes;
	private Date createDate;
	private Integer curLawTimes;

	public void setVrtId(Long vrtId){
		this.vrtId=vrtId;
	}

	public Long getVrtId(){
		return this.vrtId;
	}

	public void setWrEndDate(Date wrEndDate){
		this.wrEndDate=wrEndDate;
	}

	public Date getWrEndDate(){
		return this.wrEndDate;
	}

	public void setWrMileage(Double wrMileage){
		this.wrMileage=wrMileage;
	}

	public Double getWrMileage(){
		return this.wrMileage;
	}

	public void setCurMileage(Double curMileage){
		this.curMileage=curMileage;
	}

	public Double getCurMileage(){
		return this.curMileage;
	}

	public void setPartWrType(Integer partWrType){
		this.partWrType=partWrType;
	}

	public Integer getPartWrType(){
		return this.partWrType;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCurWrTimes(Integer curWrTimes){
		this.curWrTimes=curWrTimes;
	}

	public Integer getCurWrTimes(){
		return this.curWrTimes;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setBuyDate(Date buyDate){
		this.buyDate=buyDate;
	}

	public Date getBuyDate(){
		return this.buyDate;
	}

	public void setWrMonth(Integer wrMonth){
		this.wrMonth=wrMonth;
	}

	public Integer getWrMonth(){
		return this.wrMonth;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCurTimes(Integer curTimes){
		this.curTimes=curTimes;
	}

	public Integer getCurTimes(){
		return this.curTimes;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCurLawTimes(Integer curLawTimes){
		this.curLawTimes=curLawTimes;
	}

	public Integer getCurLawTimes(){
		return this.curLawTimes;
	}

}