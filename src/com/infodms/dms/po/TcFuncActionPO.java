/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-09-02 11:37:43
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TcFuncActionPO extends PO{

	private String actionCode;
	private Long updateBy;
	private Long funcId;
	private Date updateDate;
	private Long createBy;
	private String paraCode;
	private String actionName;
	private Long sortOrder;
	private Date createDate;
	private Long actionId;
	private Long actionType;

	public void setActionCode(String actionCode){
		this.actionCode=actionCode;
	}

	public String getActionCode(){
		return this.actionCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setFuncId(Long funcId){
		this.funcId=funcId;
	}

	public Long getFuncId(){
		return this.funcId;
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

	public void setParaCode(String paraCode){
		this.paraCode=paraCode;
	}

	public String getParaCode(){
		return this.paraCode;
	}

	public void setActionName(String actionName){
		this.actionName=actionName;
	}

	public String getActionName(){
		return this.actionName;
	}

	public void setSortOrder(Long sortOrder){
		this.sortOrder=sortOrder;
	}

	public Long getSortOrder(){
		return this.sortOrder;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setActionId(Long actionId){
		this.actionId=actionId;
	}

	public Long getActionId(){
		return this.actionId;
	}

	public void setActionType(Long actionType){
		this.actionType=actionType;
	}

	public Long getActionType(){
		return this.actionType;
	}	
	
}