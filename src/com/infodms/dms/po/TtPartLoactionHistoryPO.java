/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-27 14:25:21
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartLoactionHistoryPO extends PO{

	private String locCode;
	private Long createBy;
	private String subLoc;
	private Integer status;
	private Long hsId;
	private String oldSubLoc;
	private Long whId;
	private String locName;
	private Long orgId;
	private String oldLocCode;
	private Long oldWhmanId;
	private String oldLocName;
	private Long partId;
	private Long locId;
	private Date createDate;
	private Long whmanId;

	public void setLocCode(String locCode){
		this.locCode=locCode;
	}

	public String getLocCode(){
		return this.locCode;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setSubLoc(String subLoc){
		this.subLoc=subLoc;
	}

	public String getSubLoc(){
		return this.subLoc;
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

	public void setOldSubLoc(String oldSubLoc){
		this.oldSubLoc=oldSubLoc;
	}

	public String getOldSubLoc(){
		return this.oldSubLoc;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setLocName(String locName){
		this.locName=locName;
	}

	public String getLocName(){
		return this.locName;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setOldLocCode(String oldLocCode){
		this.oldLocCode=oldLocCode;
	}

	public String getOldLocCode(){
		return this.oldLocCode;
	}

	public void setOldWhmanId(Long oldWhmanId){
		this.oldWhmanId=oldWhmanId;
	}

	public Long getOldWhmanId(){
		return this.oldWhmanId;
	}

	public void setOldLocName(String oldLocName){
		this.oldLocName=oldLocName;
	}

	public String getOldLocName(){
		return this.oldLocName;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
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

	public void setWhmanId(Long whmanId){
		this.whmanId=whmanId;
	}

	public Long getWhmanId(){
		return this.whmanId;
	}

}