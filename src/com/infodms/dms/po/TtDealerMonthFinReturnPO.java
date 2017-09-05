/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-06-16 15:20:55
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDealerMonthFinReturnPO extends PO{

	private Double shfwReturn;
	private Long dealerId;
	private Double finReturn;
	private String remark;
	private Integer createBy;
	private Long detailId;
	private Double otherReturn;
	private Integer status;
	private Date finDate;
	private Date auditDate;
	private Double totalAmount;
	private Double bigcusReturn;
	private Integer isSum;
	private Date createDate;
	private Double monthReturn;
	private Double quaterReturn;
	private Double scsjReturn;

	public void setShfwReturn(Double shfwReturn){
		this.shfwReturn=shfwReturn;
	}

	public Double getShfwReturn(){
		return this.shfwReturn;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setFinReturn(Double finReturn){
		this.finReturn=finReturn;
	}

	public Double getFinReturn(){
		return this.finReturn;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(Integer createBy){
		this.createBy=createBy;
	}

	public Integer getCreateBy(){
		return this.createBy;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setOtherReturn(Double otherReturn){
		this.otherReturn=otherReturn;
	}

	public Double getOtherReturn(){
		return this.otherReturn;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setFinDate(Date finDate){
		this.finDate=finDate;
	}

	public Date getFinDate(){
		return this.finDate;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setTotalAmount(Double totalAmount){
		this.totalAmount=totalAmount;
	}

	public Double getTotalAmount(){
		return this.totalAmount;
	}

	public void setBigcusReturn(Double bigcusReturn){
		this.bigcusReturn=bigcusReturn;
	}

	public Double getBigcusReturn(){
		return this.bigcusReturn;
	}

	public void setIsSum(Integer isSum){
		this.isSum=isSum;
	}

	public Integer getIsSum(){
		return this.isSum;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setMonthReturn(Double monthReturn){
		this.monthReturn=monthReturn;
	}

	public Double getMonthReturn(){
		return this.monthReturn;
	}

	public void setQuaterReturn(Double quaterReturn){
		this.quaterReturn=quaterReturn;
	}

	public Double getQuaterReturn(){
		return this.quaterReturn;
	}

	public void setScsjReturn(Double scsjReturn){
		this.scsjReturn=scsjReturn;
	}

	public Double getScsjReturn(){
		return this.scsjReturn;
	}

}