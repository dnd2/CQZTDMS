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
public class TcFuncPO extends PO{

	private Integer funcType;
	private Date updateDate;
	private Long funcId;
	private Long updateBy;
	private Long createBy;
	private String funcName;
	private Long parFuncId;
	private Date createDate;
	private String funcCode;
	private String icon;
	private Integer status ;
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	// 是否功能,为了扩展性,特将此字段设置为整型,1:叶子节点,点开有内容数据的;0:没有内容的父节点;
	// add by chenyub@yonyou.com
	private Integer isFunc;
	private String funcTablename;

	public void setFuncType(Integer funcType){
		this.funcType=funcType;
	}

	public Integer getFuncType(){
		return this.funcType;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setFuncId(Long funcId){
		this.funcId=funcId;
	}

	public Long getFuncId(){
		return this.funcId;
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

	public void setFuncName(String funcName){
		this.funcName=funcName;
	}

	public String getFuncName(){
		return this.funcName;
	}

	public void setParFuncId(Long parFuncId){
		this.parFuncId=parFuncId;
	}

	public Long getParFuncId(){
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

	public Integer getIsFunc() {
		return isFunc;
	}

	public void setIsFunc(Integer isFunc) {
		this.isFunc = isFunc;
	}

	public String getFuncTablename() {
		return funcTablename;
	}

	public void setFuncTablename(String funcTablename) {
		this.funcTablename = funcTablename;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	

}