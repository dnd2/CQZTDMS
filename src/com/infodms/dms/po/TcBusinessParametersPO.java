/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-07-13 00:15:46
* CreateBy   : Fengalon
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TcBusinessParametersPO extends PO{

	private Long isValue;
	private String name;
	private Long isShow;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Long status;
	private Long sort;
	private Long updateBy;
	private String value;
	private Long id;
	private Date createDate;
	private Integer type;
	private Long code;

	public void setIsValue(Long isValue){
		this.isValue=isValue;
	}

	public Long getIsValue(){
		return this.isValue;
	}

	public void setName(String name){
		this.name=name;
	}

	public String getName(){
		return this.name;
	}

	public void setIsShow(Long isShow){
		this.isShow=isShow;
	}

	public Long getIsShow(){
		return this.isShow;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setSort(Long sort){
		this.sort=sort;
	}

	public Long getSort(){
		return this.sort;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setValue(String value){
		this.value=value;
	}

	public String getValue(){
		return this.value;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setType(Integer type){
		this.type=type;
	}

	public Integer getType(){
		return this.type;
	}

	public void setCode(Long code){
		this.code=code;
	}

	public Long getCode(){
		return this.code;
	}

}