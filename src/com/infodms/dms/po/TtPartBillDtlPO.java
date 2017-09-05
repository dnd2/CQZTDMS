/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-16 15:13:38
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartBillDtlPO extends PO{

	private Float tax;
	private String unit;
	private Integer billQty;
	private Long impBy;
	private Integer status;
	private String partCode;
	private Double taxAmount;
	private Date impDate;
	private Long dtlId;
	private String partCname;
	private Double billAmountnotax;
	private Double billPrice;
	private Long partId;
	private Integer ver;
	private Long billId;
	private String partOldcode;
	private Float discount;
	private Double billAmunt;

	public void setTax(Float tax){
		this.tax=tax;
	}

	public Float getTax(){
		return this.tax;
	}

	public void setUnit(String unit){
		this.unit=unit;
	}

	public String getUnit(){
		return this.unit;
	}

	public void setBillQty(Integer billQty){
		this.billQty=billQty;
	}

	public Integer getBillQty(){
		return this.billQty;
	}

	public void setImpBy(Long impBy){
		this.impBy=impBy;
	}

	public Long getImpBy(){
		return this.impBy;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setTaxAmount(Double taxAmount){
		this.taxAmount=taxAmount;
	}

	public Double getTaxAmount(){
		return this.taxAmount;
	}

	public void setImpDate(Date impDate){
		this.impDate=impDate;
	}

	public Date getImpDate(){
		return this.impDate;
	}

	public void setDtlId(Long dtlId){
		this.dtlId=dtlId;
	}

	public Long getDtlId(){
		return this.dtlId;
	}

	public void setPartCname(String partCname){
		this.partCname=partCname;
	}

	public String getPartCname(){
		return this.partCname;
	}

	public void setBillAmountnotax(Double billAmountnotax){
		this.billAmountnotax=billAmountnotax;
	}

	public Double getBillAmountnotax(){
		return this.billAmountnotax;
	}

	public void setBillPrice(Double billPrice){
		this.billPrice=billPrice;
	}

	public Double getBillPrice(){
		return this.billPrice;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setBillId(Long billId){
		this.billId=billId;
	}

	public Long getBillId(){
		return this.billId;
	}

	public void setPartOldcode(String partOldcode){
		this.partOldcode=partOldcode;
	}

	public String getPartOldcode(){
		return this.partOldcode;
	}

	public void setDiscount(Float discount){
		this.discount=discount;
	}

	public Float getDiscount(){
		return this.discount;
	}

	public void setBillAmunt(Double billAmunt){
		this.billAmunt=billAmunt;
	}

	public Double getBillAmunt(){
		return this.billAmunt;
	}

}