/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-03-02 14:32:00
* CreateBy   : nova
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpVsEnergyConservationPO extends PO{

	private String conservationNo;
	private String checkOpera;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Long poseId;
	private Date createDate;
	private Long orgId;
	private Long conservationTempId;

	public void setConservationNo(String conservationNo){
		this.conservationNo=conservationNo;
	}

	public String getConservationNo(){
		return this.conservationNo;
	}

	public void setCheckOpera(String checkOpera){
		this.checkOpera=checkOpera;
	}

	public String getCheckOpera(){
		return this.checkOpera;
	}


	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}


	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public void setPoseId(Long poseId){
		this.poseId=poseId;
	}

	public Long getPoseId(){
		return this.poseId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setConservationTempId(Long conservationTempId){
		this.conservationTempId=conservationTempId;
	}

	public Long getConservationTempId(){
		return this.conservationTempId;
	}

}