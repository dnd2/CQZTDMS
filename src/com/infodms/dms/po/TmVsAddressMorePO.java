/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-07-21 16:39:07
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmVsAddressMorePO extends PO{

	private Long dealerId;
	private String address;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Integer status;
	private String linkMan;
	private Long updateBy;
	private String tel;
	private String mobilePhone;
	private Integer sex;
	private Long id;
	private Integer addressType;
	private Date createDate;

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getAddress(){
		return this.address;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
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

	public void setTel(String tel){
		this.tel=tel;
	}

	public String getTel(){
		return this.tel;
	}

	public void setMobilePhone(String mobilePhone){
		this.mobilePhone=mobilePhone;
	}

	public String getMobilePhone(){
		return this.mobilePhone;
	}

	public void setSex(Integer sex){
		this.sex=sex;
	}

	public Integer getSex(){
		return this.sex;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setAddressType(Integer addressType){
		this.addressType=addressType;
	}

	public Integer getAddressType(){
		return this.addressType;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}