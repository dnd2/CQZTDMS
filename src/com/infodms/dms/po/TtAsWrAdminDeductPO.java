/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-10-13 16:05:58
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrAdminDeductPO extends PO{

	private Long yieldly;
	private Date updateDate;
	private Long claimbalanceId;
	private Long createBy;
	private Date createDate;
	private Long dealerId;
	private String dealerName;
	private Integer deductStatus;
	private String dealerCode;
	private Long updateBy;
	private Long oemCompanyId;
	private Double deductAmount;
	private Long id;
	private Long fromClaimbalanceId ;

	public Long getFromClaimbalanceId() {
		return fromClaimbalanceId;
	}

	public void setFromClaimbalanceId(Long fromClaimbalanceId) {
		this.fromClaimbalanceId = fromClaimbalanceId;
	}
	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setClaimbalanceId(Long claimbalanceId){
		this.claimbalanceId=claimbalanceId;
	}

	public Long getClaimbalanceId(){
		return this.claimbalanceId;
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

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setDeductStatus(Integer deductStatus){
		this.deductStatus=deductStatus;
	}

	public Integer getDeductStatus(){
		return this.deductStatus;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setDeductAmount(Double deductAmount){
		this.deductAmount=deductAmount;
	}

	public Double getDeductAmount(){
		return this.deductAmount;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public Long getYieldly() {
		return yieldly;
	}

	public void setYieldly(Long yieldly) {
		this.yieldly = yieldly;
	}

}