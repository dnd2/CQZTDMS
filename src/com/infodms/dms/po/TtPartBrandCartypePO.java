/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-03 10:42:25
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartBrandCartypePO extends PO{

	private String carType;
	private Integer state;
	private Long updateBy;
	private Date updateDate;
	private Long partId;
	private Long createBy;
	private Date createDate;
	private Integer status;
	private String brand;
	private Long bcId;

	public void setCarType(String carType){
		this.carType=carType;
	}

	public String getCarType(){
		return this.carType;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setBrand(String brand){
		this.brand=brand;
	}

	public String getBrand(){
		return this.brand;
	}

	public void setBcId(Long bcId){
		this.bcId=bcId;
	}

	public Long getBcId(){
		return this.bcId;
	}

}