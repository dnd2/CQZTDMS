/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-15 14:58:32
* CreateBy   : bianlanzhou
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartOperationHistoryPO extends PO{

	private String optName;
	private Long orderId;
	private Long optBy;
	private Integer optType;
	private String what;
	private String remark;
	private String bussinessId;
	private Long optId;
	private Integer status;
	private Date optDate;

	public void setOptName(String optName){
		this.optName=optName;
	}

	public String getOptName(){
		return this.optName;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setOptBy(Long optBy){
		this.optBy=optBy;
	}

	public Long getOptBy(){
		return this.optBy;
	}

	public void setOptType(Integer optType){
		this.optType=optType;
	}

	public Integer getOptType(){
		return this.optType;
	}

	public void setWhat(String what){
		this.what=what;
	}

	public String getWhat(){
		return this.what;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setBussinessId(String bussinessId){
		this.bussinessId=bussinessId;
	}

	public String getBussinessId(){
		return this.bussinessId;
	}

	public void setOptId(Long optId){
		this.optId=optId;
	}

	public Long getOptId(){
		return this.optId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setOptDate(Date optDate){
		this.optDate=optDate;
	}

	public Date getOptDate(){
		return this.optDate;
	}

}