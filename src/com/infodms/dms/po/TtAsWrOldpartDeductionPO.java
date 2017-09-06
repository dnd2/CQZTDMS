/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-09-04 17:51:04
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrOldpartDeductionPO extends PO{

	private Long dealerId;
	private Float partDeductionAmount;
	private Date updateDate;
	private Long createBy;
	private Long deductionId;
	private Integer isSecond;
	private String balanceNo;
	private String deductionNo;
	private Integer status;
	private Float outwardDeductionAmount;
	private Long updateBy;
	private Float hoursDeductionAmount;
	private Float secondDeductionAmount;
	private Long claimId;
	private Date createDate;

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setPartDeductionAmount(Float partDeductionAmount){
		this.partDeductionAmount=partDeductionAmount;
	}

	public Float getPartDeductionAmount(){
		return this.partDeductionAmount;
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

	public void setDeductionId(Long deductionId){
		this.deductionId=deductionId;
	}

	public Long getDeductionId(){
		return this.deductionId;
	}

	public void setIsSecond(Integer isSecond){
		this.isSecond=isSecond;
	}

	public Integer getIsSecond(){
		return this.isSecond;
	}

	public void setBalanceNo(String balanceNo){
		this.balanceNo=balanceNo;
	}

	public String getBalanceNo(){
		return this.balanceNo;
	}

	public void setDeductionNo(String deductionNo){
		this.deductionNo=deductionNo;
	}

	public String getDeductionNo(){
		return this.deductionNo;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setOutwardDeductionAmount(Float outwardDeductionAmount){
		this.outwardDeductionAmount=outwardDeductionAmount;
	}

	public Float getOutwardDeductionAmount(){
		return this.outwardDeductionAmount;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setHoursDeductionAmount(Float hoursDeductionAmount){
		this.hoursDeductionAmount=hoursDeductionAmount;
	}

	public Float getHoursDeductionAmount(){
		return this.hoursDeductionAmount;
	}

	public void setSecondDeductionAmount(Float secondDeductionAmount){
		this.secondDeductionAmount=secondDeductionAmount;
	}

	public Float getSecondDeductionAmount(){
		return this.secondDeductionAmount;
	}

	public void setClaimId(Long claimId){
		this.claimId=claimId;
	}

	public Long getClaimId(){
		return this.claimId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}