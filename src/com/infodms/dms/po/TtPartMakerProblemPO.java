/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-23 10:24:54
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartMakerProblemPO extends PO{

	private String partName;
	private Date updateDate;
	private Long supplyId;
	private String remark;
	private Long createBy;
	private String partCode;
	private Integer status;
	private Long updateBy;
	private String supplyName;
	private Long partId;
	private Long problemId;
	private Date createDate;
	private String supplyCode;
	private Integer num;
	private Integer type;

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setSupplyId(Long supplyId){
		this.supplyId=supplyId;
	}

	public Long getSupplyId(){
		return this.supplyId;
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

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setSupplyName(String supplyName){
		this.supplyName=supplyName;
	}

	public String getSupplyName(){
		return this.supplyName;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setProblemId(Long problemId){
		this.problemId=problemId;
	}

	public Long getProblemId(){
		return this.problemId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSupplyCode(String supplyCode){
		this.supplyCode=supplyCode;
	}

	public String getSupplyCode(){
		return this.supplyCode;
	}

	public void setNum(Integer num){
		this.num=num;
	}

	public Integer getNum(){
		return this.num;
	}

	public void setType(Integer type){
		this.type=type;
	}

	public Integer getType(){
		return this.type;
	}

}