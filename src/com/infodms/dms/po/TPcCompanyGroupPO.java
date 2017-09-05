/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-03-10 05:50:37
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcCompanyGroupPO extends PO{

	private Date updateDate;
	private Long status;
	private String groupName;
	private Long groupId;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private Long parDealerId;
	private Long companyGroupId;
	private String dealerCodes;
	private Long dealerIds;
	
	
	
	

	public Long getDealerIds() {
		return dealerIds;
	}

	public void setDealerIds(Long dealerIds) {
		this.dealerIds = dealerIds;
	}

	public String getDealerCodes() {
		return dealerCodes;
	}

	public void setDealerCodes(String dealerCodes) {
		this.dealerCodes = dealerCodes;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setGroupName(String groupName){
		this.groupName=groupName;
	}

	public String getGroupName(){
		return this.groupName;
	}

	public void setGroupId(Long groupId){
		this.groupId=groupId;
	}

	public Long getGroupId(){
		return this.groupId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
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

	public void setParDealerId(Long parDealerId){
		this.parDealerId=parDealerId;
	}

	public Long getParDealerId(){
		return this.parDealerId;
	}

	public void setCompanyGroupId(Long companyGroupId){
		this.companyGroupId=companyGroupId;
	}

	public Long getCompanyGroupId(){
		return this.companyGroupId;
	}

}