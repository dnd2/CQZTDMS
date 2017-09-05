/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-04 13:23:20
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsOrderSplitRelPO extends PO{

	private String orderNo;
	private Long createBy;
	private String remark;
	private Long childId;
	private Double vhclPrice;
	private String childOrderno;
	private Long orderId;
	private Integer splitNum;
	private Long newMaterialId;
	private Long splitId;
	private Integer splitIndex;
	private Date createDate;
	private Long oldMaterialId;

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setChildId(Long childId){
		this.childId=childId;
	}

	public Long getChildId(){
		return this.childId;
	}

	public void setVhclPrice(Double vhclPrice){
		this.vhclPrice=vhclPrice;
	}

	public Double getVhclPrice(){
		return this.vhclPrice;
	}

	public void setChildOrderno(String childOrderno){
		this.childOrderno=childOrderno;
	}

	public String getChildOrderno(){
		return this.childOrderno;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setSplitNum(Integer splitNum){
		this.splitNum=splitNum;
	}

	public Integer getSplitNum(){
		return this.splitNum;
	}

	public void setNewMaterialId(Long newMaterialId){
		this.newMaterialId=newMaterialId;
	}

	public Long getNewMaterialId(){
		return this.newMaterialId;
	}

	public void setSplitId(Long splitId){
		this.splitId=splitId;
	}

	public Long getSplitId(){
		return this.splitId;
	}

	public void setSplitIndex(Integer splitIndex){
		this.splitIndex=splitIndex;
	}

	public Integer getSplitIndex(){
		return this.splitIndex;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOldMaterialId(Long oldMaterialId){
		this.oldMaterialId=oldMaterialId;
	}

	public Long getOldMaterialId(){
		return this.oldMaterialId;
	}

}