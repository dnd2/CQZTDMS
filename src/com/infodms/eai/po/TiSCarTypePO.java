/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-05 18:40:34
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.eai.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TiSCarTypePO extends PO{

	private String carTypeShortCode;
	private Integer actType;
	private Date updateDate;
	private Long updateBy;
	private Long createBy;
	private String carNameCode;
	private String carTypeShortName;
	private Date createDate;
	private Long seqId;
	private String remark;

	public void setCarTypeShortCode(String carTypeShortCode){
		this.carTypeShortCode=carTypeShortCode;
	}

	public String getCarTypeShortCode(){
		return this.carTypeShortCode;
	}

	public void setActType(Integer actType){
		this.actType=actType;
	}

	public Integer getActType(){
		return this.actType;
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

	public void setCarNameCode(String carNameCode){
		this.carNameCode=carNameCode;
	}

	public String getCarNameCode(){
		return this.carNameCode;
	}

	public void setCarTypeShortName(String carTypeShortName){
		this.carTypeShortName=carTypeShortName;
	}

	public String getCarTypeShortName(){
		return this.carTypeShortName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSeqId(Long seqId){
		this.seqId=seqId;
	}

	public Long getSeqId(){
		return this.seqId;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

}