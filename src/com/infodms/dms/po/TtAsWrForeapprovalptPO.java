/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-21 10:22:30
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrForeapprovalptPO extends PO{

	private Date closeDate;
	private Long oemCompanyId;
	private Long updateBy;
	private Date updateDate;
	private String partName;
	private Long modelGroup;
	private Long id;
	private Long createBy;
	private Date openDate;
	private Integer isSend;
	private Date createDate;
	private String partCode;

	public void setCloseDate(Date closeDate){
		this.closeDate=closeDate;
	}

	public Date getCloseDate(){
		return this.closeDate;
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

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setModelGroup(Long modelGroup){
		this.modelGroup=modelGroup;
	}

	public Long getModelGroup(){
		return this.modelGroup;
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

	public void setOpenDate(Date openDate){
		this.openDate=openDate;
	}

	public Date getOpenDate(){
		return this.openDate;
	}

	public void setIsSend(Integer isSend){
		this.isSend=isSend;
	}

	public Integer getIsSend(){
		return this.isSend;
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

}