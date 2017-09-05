/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-01 19:00:50
* CreateBy   : 
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsAccountChngPO extends PO{

	private Date chngDate;
	private Long accountId;
	private Integer chngType;
	private String externalDocNo;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Long chngId;
	private Long updateBy;
	private Double chngAmount;
	private String erpDocNo;
	private Date createDate;
	private Long vehicleId;

	public void setChngDate(Date chngDate){
		this.chngDate=chngDate;
	}

	public Date getChngDate(){
		return this.chngDate;
	}

	public void setAccountId(Long accountId){
		this.accountId=accountId;
	}

	public Long getAccountId(){
		return this.accountId;
	}

	public void setChngType(Integer chngType){
		this.chngType=chngType;
	}

	public Integer getChngType(){
		return this.chngType;
	}

	public void setExternalDocNo(String externalDocNo){
		this.externalDocNo=externalDocNo;
	}

	public String getExternalDocNo(){
		return this.externalDocNo;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
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

	public void setChngId(Long chngId){
		this.chngId=chngId;
	}

	public Long getChngId(){
		return this.chngId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setChngAmount(Double chngAmount){
		this.chngAmount=chngAmount;
	}

	public Double getChngAmount(){
		return this.chngAmount;
	}

	public void setErpDocNo(String erpDocNo){
		this.erpDocNo=erpDocNo;
	}

	public String getErpDocNo(){
		return this.erpDocNo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

}