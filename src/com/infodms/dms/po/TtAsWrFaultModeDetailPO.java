/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-18 01:59:15
* CreateBy   : zhuwei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrFaultModeDetailPO extends PO{

	private Date updateDate;
	private Integer status;
	private Long updateBy;
	private Long createBy;
	private Long faultId;
	private Integer isDe;
	private Date createDate;
	private String failureModeName;
	private String failureModeCode;
	private Long id;
	private Long failureModeId;

	public Long getFailureModeId() {
		return failureModeId;
	}

	public void setFailureModeId(Long failureModeId) {
		this.failureModeId = failureModeId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
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

	public void setFaultId(Long faultId){
		this.faultId=faultId;
	}

	public Long getFaultId(){
		return this.faultId;
	}

	public void setIsDe(Integer isDe){
		this.isDe=isDe;
	}

	public Integer getIsDe(){
		return this.isDe;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setFailureModeName(String failureModeName){
		this.failureModeName=failureModeName;
	}

	public String getFailureModeName(){
		return this.failureModeName;
	}

	public void setFailureModeCode(String failureModeCode){
		this.failureModeCode=failureModeCode;
	}

	public String getFailureModeCode(){
		return this.failureModeCode;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}