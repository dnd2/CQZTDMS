/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-08 18:36:25
* CreateBy   : Zhang tianheng
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TcFuncPO2 extends PO{

	private String funcType;
	private Date updateDate;
	private String funcId;
	private String updateBy;
	private String createBy;
	private String funcName;
	private String parFuncId;
	private Date createDate;
	private String funcCode;
	// 是否功能,为了扩展性,特将此字段设置为整型,1:叶子节点,点开有内容数据的;0:没有内容的父节点;其他情况待添加
	// add by chenyub@yonyou.com
	private String isFunc;

	public void setFuncType(String funcType){
		this.funcType=funcType;
	}

	public String getFuncType(){
		return this.funcType;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setFuncId(String funcId){
		this.funcId=funcId;
	}

	public String getFuncId(){
		return this.funcId;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setFuncName(String funcName){
		this.funcName=funcName;
	}

	public String getFuncName(){
		return this.funcName;
	}

	public void setParFuncId(String parFuncId){
		this.parFuncId=parFuncId;
	}

	public String getParFuncId(){
		return this.parFuncId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setFuncCode(String funcCode){
		this.funcCode=funcCode;
	}

	public String getFuncCode(){
		return this.funcCode;
	}

	public String getIsFunc() {
		return isFunc;
	}

	public void setIsFunc(String isFunc) {
		this.isFunc = isFunc;
	}

}