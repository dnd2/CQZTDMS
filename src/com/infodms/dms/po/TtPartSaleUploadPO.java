/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-09-26 15:07:24
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartSaleUploadPO extends PO{

	private String state;
	private String unit;
	private String salePrice1;
	private String isPlan;
	private String isLack;
	private String partCode;
	private String ulId;
	private String partCname;
	private String uporgstock;
	private String isReplaced;
	private String minPackage;
	private String partId;
	private String qty;
	private String isDirect;
	private String partOldcode;
	private Date createDate;
	private String itemQty;
	private String buyqty;
	private String remark;

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}
	
	public void setState(String state){
		this.state=state;
	}

	public String getState(){
		return this.state;
	}

	public void setUnit(String unit){
		this.unit=unit;
	}

	public String getUnit(){
		return this.unit;
	}

	public void setSalePrice1(String salePrice1){
		this.salePrice1=salePrice1;
	}

	public String getSalePrice1(){
		return this.salePrice1;
	}

	public void setIsPlan(String isPlan){
		this.isPlan=isPlan;
	}

	public String getIsPlan(){
		return this.isPlan;
	}

	public void setIsLack(String isLack){
		this.isLack=isLack;
	}

	public String getIsLack(){
		return this.isLack;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setUlId(String ulId){
		this.ulId=ulId;
	}

	public String getUlId(){
		return this.ulId;
	}

	public void setPartCname(String partCname){
		this.partCname=partCname;
	}

	public String getPartCname(){
		return this.partCname;
	}

	public void setUporgstock(String uporgstock){
		this.uporgstock=uporgstock;
	}

	public String getUporgstock(){
		return this.uporgstock;
	}

	public void setIsReplaced(String isReplaced){
		this.isReplaced=isReplaced;
	}

	public String getIsReplaced(){
		return this.isReplaced;
	}

	public void setMinPackage(String minPackage){
		this.minPackage=minPackage;
	}

	public String getMinPackage(){
		return this.minPackage;
	}

	public void setPartId(String partId){
		this.partId=partId;
	}

	public String getPartId(){
		return this.partId;
	}

	public void setQty(String qty){
		this.qty=qty;
	}

	public String getQty(){
		return this.qty;
	}

	public void setIsDirect(String isDirect){
		this.isDirect=isDirect;
	}

	public String getIsDirect(){
		return this.isDirect;
	}

	public void setPartOldcode(String partOldcode){
		this.partOldcode=partOldcode;
	}

	public String getPartOldcode(){
		return this.partOldcode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setItemQty(String itemQty){
		this.itemQty=itemQty;
	}

	public String getItemQty(){
		return this.itemQty;
	}

	public void setBuyqty(String buyqty){
		this.buyqty=buyqty;
	}

	public String getBuyqty(){
		return this.buyqty;
	}

}