/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-20 15:06:02
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartBillMainPO extends PO{

	private Float tax;
	private Date disableDate;
	private Date deleteDate;
	private Date updateDate;
	private Long soId;
	private String remark;
	private Long createBy;
	private Date billDate;
	private Integer status;
	private String billBy;
	private String soCode;
	private Double taxAmount;
	private Double billAmountnotax;
	private Double billAmount;
	private Long updateBy;
	private Integer ver;
	private Long billId;
	private Long deleteBy;
	private Long disableBy;
	private String billNo;
	private Date createDate;

	public void setTax(Float tax){
		this.tax=tax;
	}

	public Float getTax(){
		return this.tax;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setSoId(Long soId){
		this.soId=soId;
	}

	public Long getSoId(){
		return this.soId;
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

	public void setBillDate(Date billDate){
		this.billDate=billDate;
	}

	public Date getBillDate(){
		return this.billDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setBillBy(String billBy){
		this.billBy=billBy;
	}

	public String getBillBy(){
		return this.billBy;
	}

	public void setSoCode(String soCode){
		this.soCode=soCode;
	}

	public String getSoCode(){
		return this.soCode;
	}

	public void setTaxAmount(Double taxAmount){
		this.taxAmount=taxAmount;
	}

	public Double getTaxAmount(){
		return this.taxAmount;
	}

	public void setBillAmountnotax(Double billAmountnotax){
		this.billAmountnotax=billAmountnotax;
	}

	public Double getBillAmountnotax(){
		return this.billAmountnotax;
	}

	public void setBillAmount(Double billAmount){
		this.billAmount=billAmount;
	}

	public Double getBillAmount(){
		return this.billAmount;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
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

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setBillNo(String billNo){
		this.billNo=billNo;
	}

	public String getBillNo(){
		return this.billNo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}