/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-11-22 13:33:00
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmCustomerPO extends PO{

	private String ctmName;
	private String otherPhone;
	private String modelname;
	private String vin;
	private String companyPhone;
	private String ctmType;
	private String mainPhone;
	private Long ctmId;
	private String phone;
	private Long orderId;
	private String gueststars;
	private String dealername;
	private String province;
	private Date buydate;
	private String city;

	public void setCtmName(String ctmName){
		this.ctmName=ctmName;
	}

	public String getCtmName(){
		return this.ctmName;
	}

	public void setOtherPhone(String otherPhone){
		this.otherPhone=otherPhone;
	}

	public String getOtherPhone(){
		return this.otherPhone;
	}

	public void setModelname(String modelname){
		this.modelname=modelname;
	}

	public String getModelname(){
		return this.modelname;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setCompanyPhone(String companyPhone){
		this.companyPhone=companyPhone;
	}

	public String getCompanyPhone(){
		return this.companyPhone;
	}

	public void setCtmType(String ctmType){
		this.ctmType=ctmType;
	}

	public String getCtmType(){
		return this.ctmType;
	}

	public void setMainPhone(String mainPhone){
		this.mainPhone=mainPhone;
	}

	public String getMainPhone(){
		return this.mainPhone;
	}

	public void setCtmId(Long ctmId){
		this.ctmId=ctmId;
	}

	public Long getCtmId(){
		return this.ctmId;
	}

	public void setPhone(String phone){
		this.phone=phone;
	}

	public String getPhone(){
		return this.phone;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setGueststars(String gueststars){
		this.gueststars=gueststars;
	}

	public String getGueststars(){
		return this.gueststars;
	}

	public void setDealername(String dealername){
		this.dealername=dealername;
	}

	public String getDealername(){
		return this.dealername;
	}

	public void setProvince(String province){
		this.province=province;
	}

	public String getProvince(){
		return this.province;
	}

	public void setBuydate(Date buydate){
		this.buydate=buydate;
	}

	public Date getBuydate(){
		return this.buydate;
	}

	public void setCity(String city){
		this.city=city;
	}

	public String getCity(){
		return this.city;
	}

}