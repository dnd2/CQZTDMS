/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-05 18:40:34
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.eai.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TiMsalesPO extends PO{

	private Double totalMoney;
	private Date updateDate;
	private String invoiceNo;
	private Long createBy;
	private Date saledate;
	private Double qty;
	private Date createDate;
	private String orderNo;
	private Long updateBy;
	private Integer dmsStatus;
	private Long seqId;
	private String itemcode;
	private Date dmsDate;

	public void setTotalMoney(Double totalMoney){
		this.totalMoney=totalMoney;
	}

	public Double getTotalMoney(){
		return this.totalMoney;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}

	public String getInvoiceNo(){
		return this.invoiceNo;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setSaledate(Date saledate){
		this.saledate=saledate;
	}

	public Date getSaledate(){
		return this.saledate;
	}

	public void setQty(Double qty){
		this.qty=qty;
	}

	public Double getQty(){
		return this.qty;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDmsStatus(Integer dmsStatus){
		this.dmsStatus=dmsStatus;
	}

	public Integer getDmsStatus(){
		return this.dmsStatus;
	}

	public void setSeqId(Long seqId){
		this.seqId=seqId;
	}

	public Long getSeqId(){
		return this.seqId;
	}

	public void setItemcode(String itemcode){
		this.itemcode=itemcode;
	}

	public String getItemcode(){
		return this.itemcode;
	}

	public void setDmsDate(Date dmsDate){
		this.dmsDate=dmsDate;
	}

	public Date getDmsDate(){
		return this.dmsDate;
	}

}