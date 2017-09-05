/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-06-08 14:06:12
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmGroupColorPO extends PO{

	private Long groupId;
	private String colorCode;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Long colorId;
	private Date createDate;
	private String colorName;

	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}

	public Long getGroupId(){
		return this.groupId;
	}

	public void setColorCode(String colorCode){
		this.colorCode=colorCode;
	}

	public String getColorCode(){
		return this.colorCode;
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

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setColorId(Long colorId){
		this.colorId=colorId;
	}

	public Long getColorId(){
		return this.colorId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setColorName(String colorName){
		this.colorName=colorName;
	}

	public String getColorName(){
		return this.colorName;
	}

}