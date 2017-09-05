/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-08-27 17:21:08
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartAddressMorePO extends PO{

	private Integer state;
	private Long dealerId;
	private String dealerCode;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private String dealerName;
	private Long addrId;
	private Long updateBy;
	private Integer gender;
	private String linkman;
	private String tel;
	private String mobilePhone;
	private Integer addressType;
	private Date createDate;
	private String addr;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
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

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setAddrId(Long addrId){
		this.addrId=addrId;
	}

	public Long getAddrId(){
		return this.addrId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setGender(Integer gender){
		this.gender=gender;
	}

	public Integer getGender(){
		return this.gender;
	}

	public void setLinkman(String linkman){
		this.linkman=linkman;
	}

	public String getLinkman(){
		return this.linkman;
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

	public void setAddr(String addr){
		this.addr=addr;
	}

	public String getAddr(){
		return this.addr;
	}

}