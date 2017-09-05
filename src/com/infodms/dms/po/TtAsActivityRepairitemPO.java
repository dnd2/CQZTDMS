/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-01 10:06:40
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsActivityRepairitemPO extends PO{

	private Date updateDate;
	private Float normalLabor;
	private String statTitle;
	private Long updateBy;
	private Long createBy;
	private String itemName;
	private String itemCode;
	private Long activityId;
	private Date createDate;
	private Long itemId;
	private Float laborFee;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setNormalLabor(Float normalLabor){
		this.normalLabor=normalLabor;
	}

	public Float getNormalLabor(){
		return this.normalLabor;
	}

	public void setStatTitle(String statTitle){
		this.statTitle=statTitle;
	}

	public String getStatTitle(){
		return this.statTitle;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setItemName(String itemName){
		this.itemName=itemName;
	}

	public String getItemName(){
		return this.itemName;
	}

	public void setItemCode(String itemCode){
		this.itemCode=itemCode;
	}

	public String getItemCode(){
		return this.itemCode;
	}

	public void setActivityId(Long activityId){
		this.activityId=activityId;
	}

	public Long getActivityId(){
		return this.activityId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setItemId(Long itemId){
		this.itemId=itemId;
	}

	public Long getItemId(){
		return this.itemId;
	}

	public void setLaborFee(Float laborFee){
		this.laborFee=laborFee;
	}

	public Float getLaborFee(){
		return this.laborFee;
	}

}