/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-07-18 15:40:23
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpShDealerAddressPO extends PO{

	private String dealerName;
	private String state;
	private String mobilePhone;
	private String tel;
	private String linkman;
//	private String gender;
	private String dealerCode;
	private Long id;
	private String addressType;
	private String addr;

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setState(String state){
		this.state=state;
	}

	public String getState(){
		return this.state;
	}

	public void setMobilePhone(String mobilePhone){
		this.mobilePhone=mobilePhone;
	}

	public String getMobilePhone(){
		return this.mobilePhone;
	}

	public void setTel(String tel){
		this.tel=tel;
	}

	public String getTel(){
		return this.tel;
	}

	public void setLinkman(String linkman){
		this.linkman=linkman;
	}

	public String getLinkman(){
		return this.linkman;
	}

//	public void setGender(String gender){
//		this.gender=gender;
//	}
//
//	public String getGender(){
//		return this.gender;
//	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setAddressType(String addressType){
		this.addressType=addressType;
	}

	public String getAddressType(){
		return this.addressType;
	}

	public void setAddr(String addr){
		this.addr=addr;
	}

	public String getAddr(){
		return this.addr;
	}

}