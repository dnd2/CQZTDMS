/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-21 11:02:14
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtLoaction_historyPO extends PO{

	private String locCode;
	private String oldLocCode;
	private String oldLocName;
	private Long createBy;
	private Long locId;
	private Date createDate;
	private Integer status;
	private Long hsId;
	private Long orgId;
	private String locName;

	public void setLocCode(String locCode){
		this.locCode=locCode;
	}

	public String getLocCode(){
		return this.locCode;
	}

	public void setOldLocCode(String oldLocCode){
		this.oldLocCode=oldLocCode;
	}

	public String getOldLocCode(){
		return this.oldLocCode;
	}

	public void setOldLocName(String oldLocName){
		this.oldLocName=oldLocName;
	}

	public String getOldLocName(){
		return this.oldLocName;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setLocId(Long locId){
		this.locId=locId;
	}

	public Long getLocId(){
		return this.locId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setHsId(Long hsId){
		this.hsId=hsId;
	}

	public Long getHsId(){
		return this.hsId;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setLocName(String locName){
		this.locName=locName;
	}

	public String getLocName(){
		return this.locName;
	}

}