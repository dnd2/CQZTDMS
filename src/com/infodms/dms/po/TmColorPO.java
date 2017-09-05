/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-23 16:07:46
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmColorPO extends PO{

	private Date updateDate;
	private Integer status;
	private String colorCode;
	private String colorName;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private Long colorId;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setColorCode(String colorCode){
		this.colorCode=colorCode;
	}

	public String getColorCode(){
		return this.colorCode;
	}

	public void setColorName(String colorName){
		this.colorName=colorName;
	}

	public String getColorName(){
		return this.colorName;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setColorId(Long colorId){
		this.colorId=colorId;
	}

	public Long getColorId(){
		return this.colorId;
	}

}