/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-25 09:18:05
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrKeyDesignPO extends PO{

	private Long kdId;
	private Long updateBy;
	private String kdName;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private String kdCode;

	public void setKdId(Long kdId){
		this.kdId=kdId;
	}

	public Long getKdId(){
		return this.kdId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setKdName(String kdName){
		this.kdName=kdName;
	}

	public String getKdName(){
		return this.kdName;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setKdCode(String kdCode){
		this.kdCode=kdCode;
	}

	public String getKdCode(){
		return this.kdCode;
	}

}