/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-19 10:59:09
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmBusinessChngCodePO extends PO{

	private Date updateDate;
	private String codeName;
	private String areaCode;
	private String remark;
	private Long createBy;
	private Integer isSend;
	private Long businessCodeId;
	private Integer isDel;
	private Long oemCompanyId;
	private Long updateBy;
	private Date createDate;
	private String typeCode;
	private String code;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCodeName(String codeName){
		this.codeName=codeName;
	}

	public String getCodeName(){
		return this.codeName;
	}

	public void setAreaCode(String areaCode){
		this.areaCode=areaCode;
	}

	public String getAreaCode(){
		return this.areaCode;
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

	public void setIsSend(Integer isSend){
		this.isSend=isSend;
	}

	public Integer getIsSend(){
		return this.isSend;
	}

	public void setBusinessCodeId(Long businessCodeId){
		this.businessCodeId=businessCodeId;
	}

	public Long getBusinessCodeId(){
		return this.businessCodeId;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setTypeCode(String typeCode){
		this.typeCode=typeCode;
	}

	public String getTypeCode(){
		return this.typeCode;
	}

	public void setCode(String code){
		this.code=code;
	}

	public String getCode(){
		return this.code;
	}

}