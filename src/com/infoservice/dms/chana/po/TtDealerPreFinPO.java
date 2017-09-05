/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-10-28 14:50:18
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infoservice.dms.chana.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDealerPreFinPO extends PO{

	private Long dealerId;
	private Integer kpStatus;
	private Date updateDate;
	private Long createBy;
	private String remark;
	private Double amount;
	private Long updateBy;
	private Long preId;
	private Float disPercent;
	private Date createDate;
	private Date month;
	private Integer auditStatus;
	private Double actualAmount;

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setKpStatus(Integer kpStatus){
		this.kpStatus=kpStatus;
	}

	public Integer getKpStatus(){
		return this.kpStatus;
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

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPreId(Long preId){
		this.preId=preId;
	}

	public Long getPreId(){
		return this.preId;
	}

	public void setDisPercent(Float disPercent){
		this.disPercent=disPercent;
	}

	public Float getDisPercent(){
		return this.disPercent;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setMonth(Date month){
		this.month=month;
	}

	public Date getMonth(){
		return this.month;
	}

	public void setAuditStatus(Integer auditStatus){
		this.auditStatus=auditStatus;
	}

	public Integer getAuditStatus(){
		return this.auditStatus;
	}

	public void setActualAmount(Double actualAmount){
		this.actualAmount=actualAmount;
	}

	public Double getActualAmount(){
		return this.actualAmount;
	}

}