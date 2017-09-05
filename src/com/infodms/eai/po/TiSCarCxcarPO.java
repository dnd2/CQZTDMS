/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-28 16:15:16
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.eai.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TiSCarCxcarPO extends PO{

	private Date carCxendtm;
	private Date updateDate;
	private Date carCxbgtm;
	private Long createBy;
	private Date createDate;
	private Double carPrice;
	private String carTypeCode;
	private String status;
	private Long updateBy;
	private Long cxcarid;
	private Integer dmsStatus;
	private Long seqId;
	private Date dmsDate;

	public void setCarCxendtm(Date carCxendtm){
		this.carCxendtm=carCxendtm;
	}

	public Date getCarCxendtm(){
		return this.carCxendtm;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCarCxbgtm(Date carCxbgtm){
		this.carCxbgtm=carCxbgtm;
	}

	public Date getCarCxbgtm(){
		return this.carCxbgtm;
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

	public void setCarPrice(Double carPrice){
		this.carPrice=carPrice;
	}

	public Double getCarPrice(){
		return this.carPrice;
	}

	public void setCarTypeCode(String carTypeCode){
		this.carTypeCode=carTypeCode;
	}

	public String getCarTypeCode(){
		return this.carTypeCode;
	}

	public void setStatus(String status){
		this.status=status;
	}

	public String getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCxcarid(Long cxcarid){
		this.cxcarid=cxcarid;
	}

	public Long getCxcarid(){
		return this.cxcarid;
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

	public void setDmsDate(Date dmsDate){
		this.dmsDate=dmsDate;
	}

	public Date getDmsDate(){
		return this.dmsDate;
	}

}