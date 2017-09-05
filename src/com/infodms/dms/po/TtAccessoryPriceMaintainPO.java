/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-05-04 12:42:23
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAccessoryPriceMaintainPO extends PO{

	private Double price;
	private Long updateBy;
	private Long addBy;
	private Long id;
	private Date updateTime;
	private Date addTime;
	private String workhourCode;
	private String workhourName;
	private Integer status;

	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setAddBy(Long addBy){
		this.addBy=addBy;
	}

	public Long getAddBy(){
		return this.addBy;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}

	public Date getUpdateTime(){
		return this.updateTime;
	}

	public void setAddTime(Date addTime){
		this.addTime=addTime;
	}

	public Date getAddTime(){
		return this.addTime;
	}

	public void setWorkhourCode(String workhourCode){
		this.workhourCode=workhourCode;
	}

	public String getWorkhourCode(){
		return this.workhourCode;
	}

	public void setWorkhourName(String workhourName){
		this.workhourName=workhourName;
	}

	public String getWorkhourName(){
		return this.workhourName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}