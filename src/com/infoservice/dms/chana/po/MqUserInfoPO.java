/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-15 10:12:23
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infoservice.dms.chana.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class MqUserInfoPO extends PO{

	private String userType;
	private Integer enable;
	private String userId;
	private String description;
	private String replyQueue;
	private String receiveQueue;
	private String userName;

	public void setUserType(String userType){
		this.userType=userType;
	}

	public String getUserType(){
		return this.userType;
	}

	public void setEnable(Integer enable){
		this.enable=enable;
	}

	public Integer getEnable(){
		return this.enable;
	}

	public void setUserId(String userId){
		this.userId=userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setDescription(String description){
		this.description=description;
	}

	public String getDescription(){
		return this.description;
	}

	public void setReplyQueue(String replyQueue){
		this.replyQueue=replyQueue;
	}

	public String getReplyQueue(){
		return this.replyQueue;
	}

	public void setReceiveQueue(String receiveQueue){
		this.receiveQueue=receiveQueue;
	}

	public String getReceiveQueue(){
		return this.receiveQueue;
	}

	public void setUserName(String userName){
		this.userName=userName;
	}

	public String getUserName(){
		return this.userName;
	}

}