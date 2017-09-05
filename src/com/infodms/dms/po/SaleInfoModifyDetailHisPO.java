/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-01-06 09:54:06
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class SaleInfoModifyDetailHisPO extends PO{

	private String afterModify;
	private String beforeModify;
	private Long createBy;
	private Long cusEditId;
	private Date createDate;
	private String modifyTitle;

	public void setAfterModify(String afterModify){
		this.afterModify=afterModify;
	}

	public String getAfterModify(){
		return this.afterModify;
	}

	public void setBeforeModify(String beforeModify){
		this.beforeModify=beforeModify;
	}

	public String getBeforeModify(){
		return this.beforeModify;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCusEditId(Long cusEditId){
		this.cusEditId=cusEditId;
	}

	public Long getCusEditId(){
		return this.cusEditId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setModifyTitle(String modifyTitle){
		this.modifyTitle=modifyTitle;
	}

	public String getModifyTitle(){
		return this.modifyTitle;
	}

}