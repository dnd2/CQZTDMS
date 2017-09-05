/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-05 18:40:34
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.eai.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TiSAgentCatalogPO extends PO{

	private String bankAccounts;
	private String teleNo;
	private Date updateDate;
	private String faxNo;
	private String agentEmail;
	private String agentCode;
	private Long createBy;
	private String agentClass;
	private String qadCode;
	private Date createDate;
	private String agentName;
	private String linkMan;
	private Integer actType;
	private Long updateBy;
	private String postCode;
	private Long seqId;
	private String address;

	public void setBankAccounts(String bankAccounts){
		this.bankAccounts=bankAccounts;
	}

	public String getBankAccounts(){
		return this.bankAccounts;
	}

	public void setTeleNo(String teleNo){
		this.teleNo=teleNo;
	}

	public String getTeleNo(){
		return this.teleNo;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setFaxNo(String faxNo){
		this.faxNo=faxNo;
	}

	public String getFaxNo(){
		return this.faxNo;
	}

	public void setAgentEmail(String agentEmail){
		this.agentEmail=agentEmail;
	}

	public String getAgentEmail(){
		return this.agentEmail;
	}

	public void setAgentCode(String agentCode){
		this.agentCode=agentCode;
	}

	public String getAgentCode(){
		return this.agentCode;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAgentClass(String agentClass){
		this.agentClass=agentClass;
	}

	public String getAgentClass(){
		return this.agentClass;
	}

	public void setQadCode(String qadCode){
		this.qadCode=qadCode;
	}

	public String getQadCode(){
		return this.qadCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAgentName(String agentName){
		this.agentName=agentName;
	}

	public String getAgentName(){
		return this.agentName;
	}

	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}

	public String getLinkMan(){
		return this.linkMan;
	}

	public void setActType(Integer actType){
		this.actType=actType;
	}

	public Integer getActType(){
		return this.actType;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPostCode(String postCode){
		this.postCode=postCode;
	}

	public String getPostCode(){
		return this.postCode;
	}

	public void setSeqId(Long seqId){
		this.seqId=seqId;
	}

	public Long getSeqId(){
		return this.seqId;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getAddress(){
		return this.address;
	}

}