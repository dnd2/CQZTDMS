/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-29 17:44:30
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsChangePricePO extends PO{

	private Date policyEndDate;
	private Date endsDate;
	private Long addType;
	private Date updateDate;
	private Double changValue;
	private String remark;
	private Long createBy;
	private Long status;
	private String policyNo;
	private Integer changStatus;
	private Long updateBy;
	private Long endsBy;
	private Long id;
	private Date makeDate;
	private String policyName;
	private Date generateDate;
	private Date createDate;
	private Date policyStartDate;
	private Long changType;

	public void setPolicyEndDate(Date policyEndDate){
		this.policyEndDate=policyEndDate;
	}

	public Date getPolicyEndDate(){
		return this.policyEndDate;
	}

	public void setEndsDate(Date endsDate){
		this.endsDate=endsDate;
	}

	public Date getEndsDate(){
		return this.endsDate;
	}

	public void setAddType(Long addType){
		this.addType=addType;
	}

	public Long getAddType(){
		return this.addType;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setChangValue(Double changValue){
		this.changValue=changValue;
	}

	public Double getChangValue(){
		return this.changValue;
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

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setPolicyNo(String policyNo){
		this.policyNo=policyNo;
	}

	public String getPolicyNo(){
		return this.policyNo;
	}

	public void setChangStatus(Integer changStatus){
		this.changStatus=changStatus;
	}

	public Integer getChangStatus(){
		return this.changStatus;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setEndsBy(Long endsBy){
		this.endsBy=endsBy;
	}

	public Long getEndsBy(){
		return this.endsBy;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setMakeDate(Date makeDate){
		this.makeDate=makeDate;
	}

	public Date getMakeDate(){
		return this.makeDate;
	}

	public void setPolicyName(String policyName){
		this.policyName=policyName;
	}

	public String getPolicyName(){
		return this.policyName;
	}

	public void setGenerateDate(Date generateDate){
		this.generateDate=generateDate;
	}

	public Date getGenerateDate(){
		return this.generateDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPolicyStartDate(Date policyStartDate){
		this.policyStartDate=policyStartDate;
	}

	public Date getPolicyStartDate(){
		return this.policyStartDate;
	}

	public void setChangType(Long changType){
		this.changType=changType;
	}

	public Long getChangType(){
		return this.changType;
	}

}