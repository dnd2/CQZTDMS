/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-14 07:02:49
* CreateBy   : Andy
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TcUserPasswordPO extends PO{

	private String oldPassword;
	private Date updateDate;
	private Long updateBy;
	private Long createBy;
	private Long passId;
	private Long userId;
	private Date createDate;
	private String nowPassword;

	public void setOldPassword(String oldPassword){
		this.oldPassword=oldPassword;
	}

	public String getOldPassword(){
		return this.oldPassword;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
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

	public void setPassId(Long passId){
		this.passId=passId;
	}

	public Long getPassId(){
		return this.passId;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return this.userId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setNowPassword(String nowPassword){
		this.nowPassword=nowPassword;
	}

	public String getNowPassword(){
		return this.nowPassword;
	}

}