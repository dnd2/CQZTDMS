/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-04 16:08:02
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPtShippingsheetitemPO extends PO{

	private Date updateDate;
	private Long createBy;
	private String partCode;
	private Date createDate;
	private Integer count;
	private String doNo;
	private String partName;
	private String cartonNo;
	private Double instructPrice;
	private Long updateBy;
	private Double totalPrice;
	private Double price;
	private Long itemId;
	private String remark;

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCartonNo(String cartonNo){
		this.cartonNo=cartonNo;
	}

	public String getCartonNo(){
		return this.cartonNo;
	}

	public void setTotalPrice(Double totalPrice){
		this.totalPrice=totalPrice;
	}

	public Double getTotalPrice(){
		return this.totalPrice;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setItemId(Long itemId){
		this.itemId=itemId;
	}

	public Long getItemId(){
		return this.itemId;
	}

	public void setDoNo(String doNo){
		this.doNo=doNo;
	}

	public String getDoNo(){
		return this.doNo;
	}

	public Double getInstructPrice() {
		return instructPrice;
	}

	public void setInstructPrice(Double instructPrice) {
		this.instructPrice = instructPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}