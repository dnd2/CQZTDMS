/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-03-14 15:14:13
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
* 			   DMS - > ERP 经销商订单发运申请提报表 (CUX_ERP_DELIVER) PO
*/

package com.infodms.dms.po;

import java.math.BigDecimal;
import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class CuxErpDeliverPO extends PO{

	private BigDecimal lastUpdatedBy;
	private BigDecimal plateNumber;
	private String attribute9;
	private BigDecimal orderedQuantity;
	private String attribute6;
	private BigDecimal priceListId;
	private String itemCode;
	private BigDecimal createdBy;
	private Date orderDate;
	private String attribute3;
	private String status;
	private String shipToLocation;
	private String invoiceToLocation;
	private String attribute4;
	private BigDecimal lastUpdateLogin;
	private String customerName;
	private Date lastUpdateDate;
	private Date requestDate;
	private String orderType;
	private String customerNumber;
	private String attribute2;
	private String shipFrom;
	private String attribute8;
	private String orderNumber;
	private String attribute5;
	private Date creationDate;
	private Date scheduleShipDate;
	private String fundsType;
	private String attribute7;
	private String attribute10;
	private String remark;
	private String attribute1;
	private String returnReason;
	
	private BigDecimal seqId;
	
	private BigDecimal lineNumber;
	
	
	

	public BigDecimal getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(BigDecimal lineNumber) {
		this.lineNumber = lineNumber;
	}

	public BigDecimal getSeqId() {
		return seqId;
	}

	public void setSeqId(BigDecimal seqId) {
		this.seqId = seqId;
	}

	public void setLastUpdatedBy(BigDecimal lastUpdatedBy){
		this.lastUpdatedBy=lastUpdatedBy;
	}

	public BigDecimal getLastUpdatedBy(){
		return this.lastUpdatedBy;
	}

	public void setPlateNumber(BigDecimal plateNumber){
		this.plateNumber=plateNumber;
	}

	public BigDecimal getPlateNumber(){
		return this.plateNumber;
	}

	public void setAttribute9(String attribute9){
		this.attribute9=attribute9;
	}

	public String getAttribute9(){
		return this.attribute9;
	}

	public void setOrderedQuantity(BigDecimal orderedQuantity){
		this.orderedQuantity=orderedQuantity;
	}

	public BigDecimal getOrderedQuantity(){
		return this.orderedQuantity;
	}

	public void setAttribute6(String attribute6){
		this.attribute6=attribute6;
	}

	public String getAttribute6(){
		return this.attribute6;
	}

	public void setPriceListId(BigDecimal priceListId){
		this.priceListId=priceListId;
	}

	public BigDecimal getPriceListId(){
		return this.priceListId;
	}

	public void setItemCode(String itemCode){
		this.itemCode=itemCode;
	}

	public String getItemCode(){
		return this.itemCode;
	}

	public void setCreatedBy(BigDecimal createdBy){
		this.createdBy=createdBy;
	}

	public BigDecimal getCreatedBy(){
		return this.createdBy;
	}

	public void setOrderDate(Date orderDate){
		this.orderDate=orderDate;
	}

	public Date getOrderDate(){
		return this.orderDate;
	}

	public void setAttribute3(String attribute3){
		this.attribute3=attribute3;
	}

	public String getAttribute3(){
		return this.attribute3;
	}

	public void setStatus(String status){
		this.status=status;
	}

	public String getStatus(){
		return this.status;
	}

	public void setShipToLocation(String shipToLocation){
		this.shipToLocation=shipToLocation;
	}

	public String getShipToLocation(){
		return this.shipToLocation;
	}

	public void setInvoiceToLocation(String invoiceToLocation){
		this.invoiceToLocation=invoiceToLocation;
	}

	public String getInvoiceToLocation(){
		return this.invoiceToLocation;
	}

	public void setAttribute4(String attribute4){
		this.attribute4=attribute4;
	}

	public String getAttribute4(){
		return this.attribute4;
	}

	public void setLastUpdateLogin(BigDecimal lastUpdateLogin){
		this.lastUpdateLogin=lastUpdateLogin;
	}

	public BigDecimal getLastUpdateLogin(){
		return this.lastUpdateLogin;
	}

	public void setCustomerName(String customerName){
		this.customerName=customerName;
	}

	public String getCustomerName(){
		return this.customerName;
	}

	public void setLastUpdateDate(Date lastUpdateDate){
		this.lastUpdateDate=lastUpdateDate;
	}

	public Date getLastUpdateDate(){
		return this.lastUpdateDate;
	}

	public void setRequestDate(Date requestDate){
		this.requestDate=requestDate;
	}

	public Date getRequestDate(){
		return this.requestDate;
	}

	public void setOrderType(String orderType){
		this.orderType=orderType;
	}

	public String getOrderType(){
		return this.orderType;
	}

	public void setCustomerNumber(String customerNumber){
		this.customerNumber=customerNumber;
	}

	public String getCustomerNumber(){
		return this.customerNumber;
	}

	public void setAttribute2(String attribute2){
		this.attribute2=attribute2;
	}

	public String getAttribute2(){
		return this.attribute2;
	}

	public void setShipFrom(String shipFrom){
		this.shipFrom=shipFrom;
	}

	public String getShipFrom(){
		return this.shipFrom;
	}

	public void setAttribute8(String attribute8){
		this.attribute8=attribute8;
	}

	public String getAttribute8(){
		return this.attribute8;
	}

	public void setOrderNumber(String orderNumber){
		this.orderNumber=orderNumber;
	}

	public String getOrderNumber(){
		return this.orderNumber;
	}

	public void setAttribute5(String attribute5){
		this.attribute5=attribute5;
	}

	public String getAttribute5(){
		return this.attribute5;
	}

	public void setCreationDate(Date creationDate){
		this.creationDate=creationDate;
	}

	public Date getCreationDate(){
		return this.creationDate;
	}

	public void setScheduleShipDate(Date scheduleShipDate){
		this.scheduleShipDate=scheduleShipDate;
	}

	public Date getScheduleShipDate(){
		return this.scheduleShipDate;
	}

	public void setFundsType(String fundsType){
		this.fundsType=fundsType;
	}

	public String getFundsType(){
		return this.fundsType;
	}

	public void setAttribute7(String attribute7){
		this.attribute7=attribute7;
	}

	public String getAttribute7(){
		return this.attribute7;
	}

	public void setAttribute10(String attribute10){
		this.attribute10=attribute10;
	}

	public String getAttribute10(){
		return this.attribute10;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setAttribute1(String attribute1){
		this.attribute1=attribute1;
	}

	public String getAttribute1(){
		return this.attribute1;
	}

	public void setReturnReason(String returnReason){
		this.returnReason=returnReason;
	}

	public String getReturnReason(){
		return this.returnReason;
	}

}