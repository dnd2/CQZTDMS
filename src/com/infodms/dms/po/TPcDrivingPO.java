/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-17 10:05:28
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcDrivingPO extends PO{

	private Long ctmId;
	private Long drivingId;
	private Date updateDate;
	private Long drivingVechile;
	private Long drivingMan;
	private Long createBy;
	private Date drivingDate;
	private Date createDate;
	private String drivingRoad;
	private Long firstMile;
	private Long status;
	private String cardNo;
	private Long updateBy;
	private Long endMile;

	public void setCtmId(Long ctmId){
		this.ctmId=ctmId;
	}

	public Long getCtmId(){
		return this.ctmId;
	}

	public void setDrivingId(Long drivingId){
		this.drivingId=drivingId;
	}

	public Long getDrivingId(){
		return this.drivingId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setDrivingVechile(Long drivingVechile){
		this.drivingVechile=drivingVechile;
	}

	public Long getDrivingVechile(){
		return this.drivingVechile;
	}

	public void setDrivingMan(Long drivingMan){
		this.drivingMan=drivingMan;
	}

	public Long getDrivingMan(){
		return this.drivingMan;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setDrivingDate(Date drivingDate){
		this.drivingDate=drivingDate;
	}

	public Date getDrivingDate(){
		return this.drivingDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDrivingRoad(String drivingRoad){
		this.drivingRoad=drivingRoad;
	}

	public String getDrivingRoad(){
		return this.drivingRoad;
	}

	public void setFirstMile(Long firstMile){
		this.firstMile=firstMile;
	}

	public Long getFirstMile(){
		return this.firstMile;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setCardNo(String cardNo){
		this.cardNo=cardNo;
	}

	public String getCardNo(){
		return this.cardNo;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setEndMile(Long endMile){
		this.endMile=endMile;
	}

	public Long getEndMile(){
		return this.endMile;
	}

}