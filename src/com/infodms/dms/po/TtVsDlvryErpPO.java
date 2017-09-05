/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-30 18:48:00
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.math.BigDecimal;
import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsDlvryErpPO extends PO{

	private String motormanPhone;
	private BigDecimal sendcarHeaderId;
	private Date updateDate;
	private String motorman;
	private Long createBy;
	private String sendcarOrderNumber;
	private String shipMethodCode;
	private Date createDate;
	private String orderNumber;
	private Integer sendcarAmount;
	private String flatcarId;
	private Date flatcarAssignDate;
	private Long updateBy;
	private String deliveryId;
	private Date arriveDate;

	public void setMotormanPhone(String motormanPhone){
		this.motormanPhone=motormanPhone;
	}

	public String getMotormanPhone(){
		return this.motormanPhone;
	}

	public void setSendcarHeaderId(BigDecimal sendcarHeaderId){
		this.sendcarHeaderId=sendcarHeaderId;
	}

	public BigDecimal getSendcarHeaderId(){
		return this.sendcarHeaderId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setMotorman(String motorman){
		this.motorman=motorman;
	}

	public String getMotorman(){
		return this.motorman;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setSendcarOrderNumber(String sendcarOrderNumber){
		this.sendcarOrderNumber=sendcarOrderNumber;
	}

	public String getSendcarOrderNumber(){
		return this.sendcarOrderNumber;
	}

	public void setShipMethodCode(String shipMethodCode){
		this.shipMethodCode=shipMethodCode;
	}

	public String getShipMethodCode(){
		return this.shipMethodCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOrderNumber(String orderNumber){
		this.orderNumber=orderNumber;
	}

	public String getOrderNumber(){
		return this.orderNumber;
	}

	public void setSendcarAmount(Integer sendcarAmount){
		this.sendcarAmount=sendcarAmount;
	}

	public Integer getSendcarAmount(){
		return this.sendcarAmount;
	}

	public void setFlatcarId(String flatcarId){
		this.flatcarId=flatcarId;
	}

	public String getFlatcarId(){
		return this.flatcarId;
	}

	public void setFlatcarAssignDate(Date flatcarAssignDate){
		this.flatcarAssignDate=flatcarAssignDate;
	}

	public Date getFlatcarAssignDate(){
		return this.flatcarAssignDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDeliveryId(String deliveryId){
		this.deliveryId=deliveryId;
	}

	public String getDeliveryId(){
		return this.deliveryId;
	}

	public Date getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}

}