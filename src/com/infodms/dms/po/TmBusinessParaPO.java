/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-16 14:46:10
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmBusinessParaPO extends PO{

	private Integer typeCode;
	private Date updateDate;
	private String paraValue;
	private Long updateBy;
	private Long createBy;
	private Long oemCompanyId;
	private Integer paraId;
	private Date createDate;
	private String typeName;
	private String paraName;
	private String remark;

	public void setTypeCode(Integer typeCode){
		this.typeCode=typeCode;
	}

	public Integer getTypeCode(){
		return this.typeCode;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setParaValue(String paraValue){
		this.paraValue=paraValue;
	}

	public String getParaValue(){
		return this.paraValue;
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

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setParaId(Integer paraId){
		this.paraId=paraId;
	}

	public Integer getParaId(){
		return this.paraId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setTypeName(String typeName){
		this.typeName=typeName;
	}

	public String getTypeName(){
		return this.typeName;
	}

	public void setParaName(String paraName){
		this.paraName=paraName;
	}

	public String getParaName(){
		return this.paraName;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

}