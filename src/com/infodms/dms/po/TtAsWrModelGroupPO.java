/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-19 14:15:03
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrModelGroupPO extends PO{

	private String wrgroupName;
	private Long oemCompanyId;
	private Integer wrgroupType;
	private Long updateBy;
	private Long wrgroupId;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private String wrgroupCode;
	private Double free;
	private Double labourPrice;
	private Double partPrice;
	private Double newCarFee ;
	private Long qamaintainId ;
	
	public Double getNewCarFee() {
		return newCarFee;
	}

	public void setNewCarFee(Double newCarFee) {
		this.newCarFee = newCarFee;
	}

	public Long getQamaintainId() {
		return qamaintainId;
	}

	public void setQamaintainId(Long qamaintainId) {
		this.qamaintainId = qamaintainId;
	}

	public Double getLabourPrice() {
		return labourPrice;
	}

	public void setLabourPrice(Double labourPrice) {
		this.labourPrice = labourPrice;
	}

	public Double getPartPrice() {
		return partPrice;
	}

	public void setPartPrice(Double partPrice) {
		this.partPrice = partPrice;
	}

	public Double getFree() {
		return free;
	}

	public void setFree(Double free) {
		this.free = free;
	}

	public void setWrgroupName(String wrgroupName){
		this.wrgroupName=wrgroupName;
	}

	public String getWrgroupName(){
		return this.wrgroupName;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setWrgroupType(Integer wrgroupType){
		this.wrgroupType=wrgroupType;
	}

	public Integer getWrgroupType(){
		return this.wrgroupType;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setWrgroupId(Long wrgroupId){
		this.wrgroupId=wrgroupId;
	}

	public Long getWrgroupId(){
		return this.wrgroupId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setWrgroupCode(String wrgroupCode){
		this.wrgroupCode=wrgroupCode;
	}

	public String getWrgroupCode(){
		return this.wrgroupCode;
	}

}