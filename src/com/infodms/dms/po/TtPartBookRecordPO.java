/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-02 16:02:54
* CreateBy   : bianlanzhou
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartBookRecordPO extends PO{

	private Long bookedQty;
	private Integer state;
	private Integer configId;
	private String orderCode;
	private Long createBy;
	private Integer status;
	private Long normalQty;
	private Long whId;
	private Long orgId;
	private Long orderId;
	private String procName;
	private Long recordId;
	private Long partId;
	private String functionName;
	private Date createDate;

	public void setBookedQty(Long bookedQty){
		this.bookedQty=bookedQty;
	}

	public Long getBookedQty(){
		return this.bookedQty;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setConfigId(Integer configId){
		this.configId=configId;
	}

	public Integer getConfigId(){
		return this.configId;
	}

	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}

	public String getOrderCode(){
		return this.orderCode;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setNormalQty(Long normalQty){
		this.normalQty=normalQty;
	}

	public Long getNormalQty(){
		return this.normalQty;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setProcName(String procName){
		this.procName=procName;
	}

	public String getProcName(){
		return this.procName;
	}

	public void setRecordId(Long recordId){
		this.recordId=recordId;
	}

	public Long getRecordId(){
		return this.recordId;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setFunctionName(String functionName){
		this.functionName=functionName;
	}

	public String getFunctionName(){
		return this.functionName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}