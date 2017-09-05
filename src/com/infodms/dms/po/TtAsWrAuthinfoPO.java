/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-08-28 15:37:13
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrAuthinfoPO extends PO{

	private String type;
	private Integer approvalLevelTier;
	private Date updateDate;
	private Long updateBy;
	private Long createBy;
	private Long oemCompanyId;
	private Date createDate;
	private String approvalLevelCode;
	private String approvalLevelName;

	public void setType(String type){
		this.type=type;
	}

	public String getType(){
		return this.type;
	}

	public void setApprovalLevelTier(Integer approvalLevelTier){
		this.approvalLevelTier=approvalLevelTier;
	}

	public Integer getApprovalLevelTier(){
		return this.approvalLevelTier;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
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

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setApprovalLevelCode(String approvalLevelCode){
		this.approvalLevelCode=approvalLevelCode;
	}

	public String getApprovalLevelCode(){
		return this.approvalLevelCode;
	}

	public void setApprovalLevelName(String approvalLevelName){
		this.approvalLevelName=approvalLevelName;
	}

	public String getApprovalLevelName(){
		return this.approvalLevelName;
	}

}