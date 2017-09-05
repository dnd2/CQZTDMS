/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-18 15:44:36
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsResourceReservePO extends PO{

	private Long companyId;
	private Date updateDate;
	private Long groupId;
	private Integer reserveYear;
	private Long createBy;
	private Integer reserveMonth;
	private Date createDate;
	private Integer reserveWeek;
	private Integer lastReserveAmt;
	private Integer status;
	private Long updateBy;
	private Long reserveId;
	private Integer reserveAmt;

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}

	public Long getGroupId(){
		return this.groupId;
	}

	public void setReserveYear(Integer reserveYear){
		this.reserveYear=reserveYear;
	}

	public Integer getReserveYear(){
		return this.reserveYear;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setReserveMonth(Integer reserveMonth){
		this.reserveMonth=reserveMonth;
	}

	public Integer getReserveMonth(){
		return this.reserveMonth;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setReserveWeek(Integer reserveWeek){
		this.reserveWeek=reserveWeek;
	}

	public Integer getReserveWeek(){
		return this.reserveWeek;
	}

	public void setLastReserveAmt(Integer lastReserveAmt){
		this.lastReserveAmt=lastReserveAmt;
	}

	public Integer getLastReserveAmt(){
		return this.lastReserveAmt;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setReserveId(Long reserveId){
		this.reserveId=reserveId;
	}

	public Long getReserveId(){
		return this.reserveId;
	}

	public void setReserveAmt(Integer reserveAmt){
		this.reserveAmt=reserveAmt;
	}

	public Integer getReserveAmt(){
		return this.reserveAmt;
	}

}