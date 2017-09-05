/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-10-16 18:24:20
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsResourceReserveLogDtlPO extends PO{

	private Long logId;
	private Date updateDate;
	private Long updateBy;
	private String batchNo;
	private Long createBy;
	private Integer newAmount;
	private Long materialId;
	private Integer oldAmount;
	private Date createDate;
	private Long dtlId;
	private Long reqDetailId;

	public void setLogId(Long logId){
		this.logId=logId;
	}

	public Long getLogId(){
		return this.logId;
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

	public void setBatchNo(String batchNo){
		this.batchNo=batchNo;
	}

	public String getBatchNo(){
		return this.batchNo;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setNewAmount(Integer newAmount){
		this.newAmount=newAmount;
	}

	public Integer getNewAmount(){
		return this.newAmount;
	}

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}

	public void setOldAmount(Integer oldAmount){
		this.oldAmount=oldAmount;
	}

	public Integer getOldAmount(){
		return this.oldAmount;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDtlId(Long dtlId){
		this.dtlId=dtlId;
	}

	public Long getDtlId(){
		return this.dtlId;
	}

	public void setReqDetailId(Long reqDetailId){
		this.reqDetailId=reqDetailId;
	}

	public Long getReqDetailId(){
		return this.reqDetailId;
	}

}