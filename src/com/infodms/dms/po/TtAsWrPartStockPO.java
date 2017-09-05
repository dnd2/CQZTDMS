/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-10-13 09:54:22
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrPartStockPO extends PO{

	private Long yieldly;
	private Date updateDate;
	private Long producerId;
	private Integer outAmount;
	private Long updateBy;
	private Long createBy;
	private Long partId;
	private Long oemCompanyId;
	private String partCode;
	private Date createDate;
	private Integer returnAmount;
	private Long id;

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setProducerId(Long producerId){
		this.producerId=producerId;
	}

	public Long getProducerId(){
		return this.producerId;
	}

	public void setOutAmount(Integer outAmount){
		this.outAmount=outAmount;
	}

	public Integer getOutAmount(){
		return this.outAmount;
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

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setReturnAmount(Integer returnAmount){
		this.returnAmount=returnAmount;
	}

	public Integer getReturnAmount(){
		return this.returnAmount;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}