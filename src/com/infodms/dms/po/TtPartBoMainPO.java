/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-05-09 09:45:27
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartBoMainPO extends PO{

	private Integer state;
	private Integer locState;
	private Date disableDate;
	private Date deleteDate;
	private String orderCode;
	private String boType;
	private Date updateDate;
	private Long soId;
	private String remark;
	private Long createBy;
	private String boCode;
	private Long pickOrderId;
	private Integer status;
	private Long orderId;
	private Long boId;
	private Long updateBy;
	private Integer ver;
	private Long deleteBy;
	private Date createDate;
	private Long disableBy;
	private Double boAmount;
	
	public void setBoAmount(Double boAmount){
		this.boAmount=boAmount;
	}

	public Double getBoAmount(){
		return this.boAmount;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setLocState(Integer locState){
		this.locState=locState;
	}

	public Integer getLocState(){
		return this.locState;
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

	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}

	public String getOrderCode(){
		return this.orderCode;
	}

	public void setBoType(String boType){
		this.boType=boType;
	}

	public String getBoType(){
		return this.boType;
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

	public void setBoCode(String boCode){
		this.boCode=boCode;
	}

	public String getBoCode(){
		return this.boCode;
	}

	public void setPickOrderId(Long pickOrderId){
		this.pickOrderId=pickOrderId;
	}

	public Long getPickOrderId(){
		return this.pickOrderId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setBoId(Long boId){
		this.boId=boId;
	}

	public Long getBoId(){
		return this.boId;
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

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

}