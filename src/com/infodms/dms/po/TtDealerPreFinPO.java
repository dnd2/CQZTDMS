/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-18 22:46:51
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDealerPreFinPO extends PO{

	private Long dealerId;
	private Double preKpAmount;
	private Integer kpStatus;
	private Long auditBy;
	private Date updateDate;
	private Long finReturnType;
	private Long createBy;
	private String remark;
	private Double amount;
	private Long updateBy;
	private Date auditDate;
	private Long preId;
	private Double disPercent;
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

	public void setPreKpAmount(Double preKpAmount){
		this.preKpAmount=preKpAmount;
	}

	public Double getPreKpAmount(){
		return this.preKpAmount;
	}

	public void setKpStatus(Integer kpStatus){
		this.kpStatus=kpStatus;
	}

	public Integer getKpStatus(){
		return this.kpStatus;
	}

	public void setAuditBy(Long auditBy){
		this.auditBy=auditBy;
	}

	public Long getAuditBy(){
		return this.auditBy;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setFinReturnType(Long finReturnType){
		this.finReturnType=finReturnType;
	}

	public Long getFinReturnType(){
		return this.finReturnType;
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

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setPreId(Long preId){
		this.preId=preId;
	}

	public Long getPreId(){
		return this.preId;
	}

	public void setDisPercent(Double disPercent){
		this.disPercent=disPercent;
	}

	public Double getDisPercent(){
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