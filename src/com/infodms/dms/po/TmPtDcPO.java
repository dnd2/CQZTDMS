/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-11-06 15:35:58
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmPtDcPO extends PO{

	private Date updateDate;
	private String address;
	private String remark;
	private Long createBy;
	private String shortName;
	private String linkMan;
	private Long dcId;
	private String phoneNumber;
	private Integer isDel;
	private String zipcode;
	private String dcCode;
	private Long updateBy;
	private String dcName;
	private Date createDate;
	private Integer isArc;
	private String fax;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getAddress(){
		return this.address;
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

	public void setShortName(String shortName){
		this.shortName=shortName;
	}

	public String getShortName(){
		return this.shortName;
	}

	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}

	public String getLinkMan(){
		return this.linkMan;
	}

	public void setDcId(Long dcId){
		this.dcId=dcId;
	}

	public Long getDcId(){
		return this.dcId;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber=phoneNumber;
	}

	public String getPhoneNumber(){
		return this.phoneNumber;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setZipcode(String zipcode){
		this.zipcode=zipcode;
	}

	public String getZipcode(){
		return this.zipcode;
	}

	public void setDcCode(String dcCode){
		this.dcCode=dcCode;
	}

	public String getDcCode(){
		return this.dcCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDcName(String dcName){
		this.dcName=dcName;
	}

	public String getDcName(){
		return this.dcName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setIsArc(Integer isArc){
		this.isArc=isArc;
	}

	public Integer getIsArc(){
		return this.isArc;
	}

	public void setFax(String fax){
		this.fax=fax;
	}

	public String getFax(){
		return this.fax;
	}

}