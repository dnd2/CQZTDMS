/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-01 17:35:01
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesContractHisPO extends PO{

	private Long contractId;
	private String contractName;
	private Date signDate;
	private Long dealerId;
	private Date updateDate;
	private Date serviceEnd;
	private String remark;
	private Long createBy;
	private Integer status;
	private String contractYear;
	private Long updateBy;
	private Integer ver;
	private Date createDate;
	private String contractNo;
	private Date serviceStart;

	public void setContractId(Long contractId){
		this.contractId=contractId;
	}

	public Long getContractId(){
		return this.contractId;
	}

	public void setContractName(String contractName){
		this.contractName=contractName;
	}

	public String getContractName(){
		return this.contractName;
	}

	public void setSignDate(Date signDate){
		this.signDate=signDate;
	}

	public Date getSignDate(){
		return this.signDate;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setServiceEnd(Date serviceEnd){
		this.serviceEnd=serviceEnd;
	}

	public Date getServiceEnd(){
		return this.serviceEnd;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setContractYear(String contractYear){
		this.contractYear=contractYear;
	}

	public String getContractYear(){
		return this.contractYear;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setContractNo(String contractNo){
		this.contractNo=contractNo;
	}

	public String getContractNo(){
		return this.contractNo;
	}

	public void setServiceStart(Date serviceStart){
		this.serviceStart=serviceStart;
	}

	public Date getServiceStart(){
		return this.serviceStart;
	}

}