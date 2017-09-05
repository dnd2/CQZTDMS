/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2016-06-13 10:09:59
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartContractDefineLogPO extends PO{

	private Integer state;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Integer status;
	private Long defId;
	private Integer contractType;
	private String contractNumber;
	private Integer istemp;
	private Date contractEdate;
	private Long updateBy;
	private Long partId;
	private Long venderId;
	private Date contractSdate;
	private Date createDate;
	private Double contractPrice;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setDefId(Long defId){
		this.defId=defId;
	}

	public Long getDefId(){
		return this.defId;
	}

	public void setContractType(Integer contractType){
		this.contractType=contractType;
	}

	public Integer getContractType(){
		return this.contractType;
	}

	public void setContractNumber(String contractNumber){
		this.contractNumber=contractNumber;
	}

	public String getContractNumber(){
		return this.contractNumber;
	}

	public void setIstemp(Integer istemp){
		this.istemp=istemp;
	}

	public Integer getIstemp(){
		return this.istemp;
	}

	public void setContractEdate(Date contractEdate){
		this.contractEdate=contractEdate;
	}

	public Date getContractEdate(){
		return this.contractEdate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setVenderId(Long venderId){
		this.venderId=venderId;
	}

	public Long getVenderId(){
		return this.venderId;
	}

	public void setContractSdate(Date contractSdate){
		this.contractSdate=contractSdate;
	}

	public Date getContractSdate(){
		return this.contractSdate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setContractPrice(Double contractPrice){
		this.contractPrice=contractPrice;
	}

	public Double getContractPrice(){
		return this.contractPrice;
	}

}