/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-19 15:19:57
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmExamDetailPO extends PO{

	private Long exId;
	private Date updateDate;
	private Long createBy;
	private Integer edWidth;
	private String edChoice;
	private Integer edQueType;
	private Long edId;
	private Integer edTxtType;
	private Integer edHight;
	private Integer var;
	private Long updateBy;
	private String edQuestion;
	private Date createDate;

	public void setExId(Long exId){
		this.exId=exId;
	}

	public Long getExId(){
		return this.exId;
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

	public void setEdWidth(Integer edWidth){
		this.edWidth=edWidth;
	}

	public Integer getEdWidth(){
		return this.edWidth;
	}

	public void setEdChoice(String edChoice){
		this.edChoice=edChoice;
	}

	public String getEdChoice(){
		return this.edChoice;
	}

	public void setEdQueType(Integer edQueType){
		this.edQueType=edQueType;
	}

	public Integer getEdQueType(){
		return this.edQueType;
	}

	public void setEdId(Long edId){
		this.edId=edId;
	}

	public Long getEdId(){
		return this.edId;
	}

	public void setEdTxtType(Integer edTxtType){
		this.edTxtType=edTxtType;
	}

	public Integer getEdTxtType(){
		return this.edTxtType;
	}

	public void setEdHight(Integer edHight){
		this.edHight=edHight;
	}

	public Integer getEdHight(){
		return this.edHight;
	}

	public void setVar(Integer var){
		this.var=var;
	}

	public Integer getVar(){
		return this.var;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setEdQuestion(String edQuestion){
		this.edQuestion=edQuestion;
	}

	public String getEdQuestion(){
		return this.edQuestion;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}