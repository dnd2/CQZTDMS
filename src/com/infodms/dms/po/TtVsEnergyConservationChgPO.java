/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-03-02 14:32:01
* CreateBy   : nova
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsEnergyConservationChgPO extends PO{

	private Long conservationId;
	private String description;
	private Long changeId;
	private Long createBy;
	private Date createDate;
	private Integer status;

	public void setConservationId(Long conservationId){
		this.conservationId=conservationId;
	}

	public Long getConservationId(){
		return this.conservationId;
	}

	public void setDescription(String description){
		this.description=description;
	}

	public String getDescription(){
		return this.description;
	}

	public void setChangeId(Long changeId){
		this.changeId=changeId;
	}

	public Long getChangeId(){
		return this.changeId;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
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

}