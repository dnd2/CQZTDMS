/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-04 14:53:45
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infoservice.dms.chana.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPtSalesRpcPO extends PO{

	private Date updateDate;
	private Integer salesStatus;
	private Long updateBy;
	private Double salesSum;
	private Long createBy;
	private Integer itemsCount;
	private String soNo;
	private Date salesDate;
	private Date createDate;
	private String remark;
	private String orderNo;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setSalesStatus(Integer salesStatus){
		this.salesStatus=salesStatus;
	}

	public Integer getSalesStatus(){
		return this.salesStatus;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setSalesSum(Double salesSum){
		this.salesSum=salesSum;
	}

	public Double getSalesSum(){
		return this.salesSum;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setItemsCount(Integer itemsCount){
		this.itemsCount=itemsCount;
	}

	public Integer getItemsCount(){
		return this.itemsCount;
	}

	public void setSoNo(String soNo){
		this.soNo=soNo;
	}

	public String getSoNo(){
		return this.soNo;
	}

	public void setSalesDate(Date salesDate){
		this.salesDate=salesDate;
	}

	public Date getSalesDate(){
		return this.salesDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

}