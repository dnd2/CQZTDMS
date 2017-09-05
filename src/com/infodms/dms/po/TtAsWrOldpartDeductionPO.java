/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-07-31 10:42:37
* CreateBy   : chenzheng
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrOldpartDeductionPO extends PO{

	private Float otherPrice;
	private Long dealerId;
	private String invoiceNo;
	private Float partPrice;
	private Date updateDate;
	private Long createBy;
	private String deductionNo;
	private Integer status;
	private Long returnedId;
	private Float labourPrice;
	private Long updateBy;
	private Long id;
	private Float totalPrice;
	private Date createDate;

	public void setOtherPrice(Float otherPrice){
		this.otherPrice=otherPrice;
	}

	public Float getOtherPrice(){
		return this.otherPrice;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}

	public String getInvoiceNo(){
		return this.invoiceNo;
	}

	public void setPartPrice(Float partPrice){
		this.partPrice=partPrice;
	}

	public Float getPartPrice(){
		return this.partPrice;
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

	public void setDeductionNo(String deductionNo){
		this.deductionNo=deductionNo;
	}

	public String getDeductionNo(){
		return this.deductionNo;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setReturnedId(Long returnedId){
		this.returnedId=returnedId;
	}

	public Long getReturnedId(){
		return this.returnedId;
	}

	public void setLabourPrice(Float labourPrice){
		this.labourPrice=labourPrice;
	}

	public Float getLabourPrice(){
		return this.labourPrice;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setTotalPrice(Float totalPrice){
		this.totalPrice=totalPrice;
	}

	public Float getTotalPrice(){
		return this.totalPrice;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}