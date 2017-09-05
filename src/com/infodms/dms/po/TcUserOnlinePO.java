/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-09 10:59:46
* CreateBy   : Zhang tianheng
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TcUserOnlinePO extends PO{

	private String userIp;
	private Date updateDate;
	private Long userOnlineId;
	private Long updateBy;
	private Integer userOnlineStatus;
	private Long createBy;
	private Long orgId;
	private Long userId;
	private Date createDate;
	private Date loginDate;

	public void setUserIp(String userIp){
		this.userIp=userIp;
	}

	public String getUserIp(){
		return this.userIp;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setUserOnlineId(Long userOnlineId){
		this.userOnlineId=userOnlineId;
	}

	public Long getUserOnlineId(){
		return this.userOnlineId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setUserOnlineStatus(Integer userOnlineStatus){
		this.userOnlineStatus=userOnlineStatus;
	}

	public Integer getUserOnlineStatus(){
		return this.userOnlineStatus;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
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

	public void setLoginDate(Date loginDate){
		this.loginDate=loginDate;
	}

	public Date getLoginDate(){
		return this.loginDate;
	}

}