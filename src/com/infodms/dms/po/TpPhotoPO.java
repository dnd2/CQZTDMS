/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-05-17 10:46:20
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TpPhotoPO extends PO{

	private String photoAdd;
	private Long photoId;
	private Integer status;
	private Long createBy;
	private String orderId;
	private Integer photoType;
	private Date createDate;
	private String photoName;
	private Long isDel;

	public void setPhotoAdd(String photoAdd){
		this.photoAdd=photoAdd;
	}

	public String getPhotoAdd(){
		return this.photoAdd;
	}

	public void setPhotoId(Long photoId){
		this.photoId=photoId;
	}

	public Long getPhotoId(){
		return this.photoId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOrderId(String orderId){
		this.orderId=orderId;
	}

	public String getOrderId(){
		return this.orderId;
	}

	public void setPhotoType(Integer photoType){
		this.photoType=photoType;
	}

	public Integer getPhotoType(){
		return this.photoType;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPhotoName(String photoName){
		this.photoName=photoName;
	}

	public String getPhotoName(){
		return this.photoName;
	}

	public void setIsDel(Long isDel){
		this.isDel=isDel;
	}

	public Long getIsDel(){
		return this.isDel;
	}

}