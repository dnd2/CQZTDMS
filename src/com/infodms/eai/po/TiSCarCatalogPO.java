/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-05 18:40:34
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.eai.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TiSCarCatalogPO extends PO{

	private String carTypeShortCode;
	private Date updateDate;
	private String colorTypeCode;
	private Long createBy;
	private Date createDate;
	private Integer actType;
	private String carTypeCode;
	private Long updateBy;
	private Double carPrice1;
	private Integer dmsStatus;
	private Long seqId;
	private String carType;
	private Date dmsDate;

	public void setCarTypeShortCode(String carTypeShortCode){
		this.carTypeShortCode=carTypeShortCode;
	}

	public String getCarTypeShortCode(){
		return this.carTypeShortCode;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setColorTypeCode(String colorTypeCode){
		this.colorTypeCode=colorTypeCode;
	}

	public String getColorTypeCode(){
		return this.colorTypeCode;
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

	public void setActType(Integer actType){
		this.actType=actType;
	}

	public Integer getActType(){
		return this.actType;
	}

	public void setCarTypeCode(String carTypeCode){
		this.carTypeCode=carTypeCode;
	}

	public String getCarTypeCode(){
		return this.carTypeCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCarPrice1(Double carPrice1){
		this.carPrice1=carPrice1;
	}

	public Double getCarPrice1(){
		return this.carPrice1;
	}

	public void setDmsStatus(Integer dmsStatus){
		this.dmsStatus=dmsStatus;
	}

	public Integer getDmsStatus(){
		return this.dmsStatus;
	}

	public void setSeqId(Long seqId){
		this.seqId=seqId;
	}

	public Long getSeqId(){
		return this.seqId;
	}

	public void setCarType(String carType){
		this.carType=carType;
	}

	public String getCarType(){
		return this.carType;
	}

	public void setDmsDate(Date dmsDate){
		this.dmsDate=dmsDate;
	}

	public Date getDmsDate(){
		return this.dmsDate;
	}

}