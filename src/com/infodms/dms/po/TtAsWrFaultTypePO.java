/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-12 07:21:38
* CreateBy   : zhuwei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrFaultTypePO extends PO{

	private Long faultTypeId;
	private String faultTypeName;
	private String faultTypeCode;
	private Date updateDate;
	private Integer status;
	private Long updateBy;
	private Long createBy;
	private Integer isDe;
	private Date createDate;

	public void setFaultTypeId(Long faultTypeId){
		this.faultTypeId=faultTypeId;
	}

	public Long getFaultTypeId(){
		return this.faultTypeId;
	}

	public void setFaultTypeName(String faultTypeName){
		this.faultTypeName=faultTypeName;
	}

	public String getFaultTypeName(){
		return this.faultTypeName;
	}

	public void setFaultTypeCode(String faultTypeCode){
		this.faultTypeCode=faultTypeCode;
	}

	public String getFaultTypeCode(){
		return this.faultTypeCode;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
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

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setIsDe(Integer isDe){
		this.isDe=isDe;
	}

	public Integer getIsDe(){
		return this.isDe;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}