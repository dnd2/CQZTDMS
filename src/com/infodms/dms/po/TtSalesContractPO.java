/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-18 15:14:11
* CreateBy   : ASUS
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesContractPO extends PO{

	private String contractName;
	private Long contractId;
	private Date signDate;
	private Long dealerId;
	private Long updateBy;
	private Date updateDate;
	private Integer ver;
	private Long createBy;
	private String remark;
	private Date createDate;
	private Integer status;
	private String contractNo;
	private String contractYear;
	private Date serviceStart;
	private Date serviceEnd;

	public Date getServiceStart() {
		return serviceStart;
	}

	public void setServiceStart(Date serviceStart) {
		this.serviceStart = serviceStart;
	}

	public Date getServiceEnd() {
		return serviceEnd;
	}

	public void setServiceEnd(Date serviceEnd) {
		this.serviceEnd = serviceEnd;
	}

	public String getContractYear() {
		return contractYear;
	}

	public void setContractYear(String contractYear) {
		this.contractYear = contractYear;
	}

	public void setContractName(String contractName){
		this.contractName=contractName;
	}

	public String getContractName(){
		return this.contractName;
	}

	public void setContractId(Long contractId){
		this.contractId=contractId;
	}

	public Long getContractId(){
		return this.contractId;
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

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setContractNo(String contractNo){
		this.contractNo=contractNo;
	}

	public String getContractNo(){
		return this.contractNo;
	}

}