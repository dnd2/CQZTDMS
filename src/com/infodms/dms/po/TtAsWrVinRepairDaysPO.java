/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-20 17:42:43
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrVinRepairDaysPO extends PO{

	private Date wrEndDate;
	private Double wrMileage;
	private Double curMileage;
	private Integer curDays;
	private String vin;
	private Date updateDate;
	private Long createBy;
	private Date buyDate;
	private Integer wrMonth;
	private Long updateBy;
	private Integer curLawDays;
	private Integer curWrDays;
	private Date createDate;
	private Long vrdId;

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

	public void setCurDays(Integer curDays){
		this.curDays=curDays;
	}

	public Integer getCurDays(){
		return this.curDays;
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

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
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

	public void setCurLawDays(Integer curLawDays){
		this.curLawDays=curLawDays;
	}

	public Integer getCurLawDays(){
		return this.curLawDays;
	}

	public void setCurWrDays(Integer curWrDays){
		this.curWrDays=curWrDays;
	}

	public Integer getCurWrDays(){
		return this.curWrDays;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setVrdId(Long vrdId){
		this.vrdId=vrdId;
	}

	public Long getVrdId(){
		return this.vrdId;
	}

}