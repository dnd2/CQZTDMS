/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-31 16:13:27
* CreateBy   : Andy
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmShuntDealerChangePO extends PO{

	private String faxNo;
	private Date updateDate;
	private Long createBy;
	private String shuntName;
	private Long changeId;
	private Date createDate;
	private String comLevel;
	private String phone;
	private String zipCode;
	private String cooperationNature;
	private String linkMan;
	private Long updateBy;
	private String address;

	public void setFaxNo(String faxNo){
		this.faxNo=faxNo;
	}

	public String getFaxNo(){
		return this.faxNo;
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

	public void setShuntName(String shuntName){
		this.shuntName=shuntName;
	}

	public String getShuntName(){
		return this.shuntName;
	}

	public void setChangeId(Long changeId){
		this.changeId=changeId;
	}

	public Long getChangeId(){
		return this.changeId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setComLevel(String comLevel){
		this.comLevel=comLevel;
	}

	public String getComLevel(){
		return this.comLevel;
	}

	public void setPhone(String phone){
		this.phone=phone;
	}

	public String getPhone(){
		return this.phone;
	}

	public void setZipCode(String zipCode){
		this.zipCode=zipCode;
	}

	public String getZipCode(){
		return this.zipCode;
	}

	public void setCooperationNature(String cooperationNature){
		this.cooperationNature=cooperationNature;
	}

	public String getCooperationNature(){
		return this.cooperationNature;
	}

	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}

	public String getLinkMan(){
		return this.linkMan;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getAddress(){
		return this.address;
	}

}