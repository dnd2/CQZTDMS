/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-11 08:53:45
* CreateBy   : chun_chang
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrWrauthorizationPO extends PO{

	private Date updateDate;
	private String approvalReason;
	private String approvalLevelBak;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private String approvalLevelCode;
	private Long id;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setApprovalReason(String approvalReason){
		this.approvalReason=approvalReason;
	}

	public String getApprovalReason(){
		return this.approvalReason;
	}

	public void setApprovalLevelBak(String approvalLevelBak){
		this.approvalLevelBak=approvalLevelBak;
	}

	public String getApprovalLevelBak(){
		return this.approvalLevelBak;
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

	public void setApprovalLevelCode(String approvalLevelCode){
		this.approvalLevelCode=approvalLevelCode;
	}

	public String getApprovalLevelCode(){
		return this.approvalLevelCode;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}