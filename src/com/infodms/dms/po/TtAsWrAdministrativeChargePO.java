/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-08-01 14:41:34
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrAdministrativeChargePO extends PO{

	private Double datumSum;
	private Long dealerid;
	private Long updateBy;
	private Date creatDate;
	private Double labourSum;
	private Date updateDate;
	private String balanceOder;
	private Long id;
	private Long status;
	private Long creatBy;

	public void setDatumSum(Double datumSum){
		this.datumSum=datumSum;
	}

	public Double getDatumSum(){
		return this.datumSum;
	}

	public void setDealerid(Long dealerid){
		this.dealerid=dealerid;
	}

	public Long getDealerid(){
		return this.dealerid;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreatDate(Date creatDate){
		this.creatDate=creatDate;
	}

	public Date getCreatDate(){
		return this.creatDate;
	}

	public void setLabourSum(Double labourSum){
		this.labourSum=labourSum;
	}

	public Double getLabourSum(){
		return this.labourSum;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setBalanceOder(String balanceOder){
		this.balanceOder=balanceOder;
	}

	public String getBalanceOder(){
		return this.balanceOder;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setCreatBy(Long creatBy){
		this.creatBy=creatBy;
	}

	public Long getCreatBy(){
		return this.creatBy;
	}

}