/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-30 16:43:52
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPromiseDeductPO extends PO{

	private Long dedBy;
	private Long useId;
	private Double dedAmount;
	private Long updateBy;
	private Date updateDate;
	private Date dedDate;
	private Long dedId;
	private Long createBy;
	private Long accId;
	private Date createDate;

	public void setDedBy(Long dedBy){
		this.dedBy=dedBy;
	}

	public Long getDedBy(){
		return this.dedBy;
	}

	public void setUseId(Long useId){
		this.useId=useId;
	}

	public Long getUseId(){
		return this.useId;
	}

	public void setDedAmount(Double dedAmount){
		this.dedAmount=dedAmount;
	}

	public Double getDedAmount(){
		return this.dedAmount;
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

	public void setDedDate(Date dedDate){
		this.dedDate=dedDate;
	}

	public Date getDedDate(){
		return this.dedDate;
	}

	public void setDedId(Long dedId){
		this.dedId=dedId;
	}

	public Long getDedId(){
		return this.dedId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAccId(Long accId){
		this.accId=accId;
	}

	public Long getAccId(){
		return this.accId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}