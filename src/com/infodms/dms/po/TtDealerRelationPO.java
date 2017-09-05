/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-05-22 14:57:18
* CreateBy   : ranke
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDealerRelationPO extends PO{

	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private Long xsDealerId;
	private Integer status;
	private Long shCompanyId;
	private Long xsCompanyId;
	private Long relationId;
	private Long shDealerId;

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

	public void setXsDealerId(Long xsDealerId){
		this.xsDealerId=xsDealerId;
	}

	public Long getXsDealerId(){
		return this.xsDealerId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setShCompanyId(Long shCompanyId){
		this.shCompanyId=shCompanyId;
	}

	public Long getShCompanyId(){
		return this.shCompanyId;
	}

	public void setXsCompanyId(Long xsCompanyId){
		this.xsCompanyId=xsCompanyId;
	}

	public Long getXsCompanyId(){
		return this.xsCompanyId;
	}

	public void setRelationId(Long relationId){
		this.relationId=relationId;
	}

	public Long getRelationId(){
		return this.relationId;
	}

	public void setShDealerId(Long shDealerId){
		this.shDealerId=shDealerId;
	}

	public Long getShDealerId(){
		return this.shDealerId;
	}

}