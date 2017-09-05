/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-08-01 10:38:51
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrOldOutDoorDetailPO extends PO{

	private Long doorId;
	private Integer remandNum;
	private String partName;
	private Long id;
	private Long createBy;
	private String outRemark;
	private Integer outNum;
	private Date createDate;
	private String partCode;
	private String modelName;
	private Integer outPartType;
	public Integer getOutPartType() {
		return outPartType;
	}

	public void setOutPartType(Integer outPartType) {
		this.outPartType = outPartType;
	}

	public void setDoorId(Long doorId){
		this.doorId=doorId;
	}

	public Long getDoorId(){
		return this.doorId;
	}

	public void setRemandNum(Integer remandNum){
		this.remandNum=remandNum;
	}

	public Integer getRemandNum(){
		return this.remandNum;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOutRemark(String outRemark){
		this.outRemark=outRemark;
	}

	public String getOutRemark(){
		return this.outRemark;
	}

	public void setOutNum(Integer outNum){
		this.outNum=outNum;
	}

	public Integer getOutNum(){
		return this.outNum;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setModelName(String modelName){
		this.modelName=modelName;
	}

	public String getModelName(){
		return this.modelName;
	}

}