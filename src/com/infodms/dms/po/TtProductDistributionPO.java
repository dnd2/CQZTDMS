/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-10-24 10:08:03
* CreateBy   : Iverson
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtProductDistributionPO extends PO{

	private Long productId;
	private Long updateBy;
	private String isCheck;
	private Long companyId;
	private Date updateDate;
	private String companyName;
	private Long createBy;
	private Date createDate;
	private String companyCode;
	private Long productDistributionId;

	public void setProductId(Long productId){
		this.productId=productId;
	}

	public Long getProductId(){
		return this.productId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setIsCheck(String isCheck){
		this.isCheck=isCheck;
	}

	public String getIsCheck(){
		return this.isCheck;
	}

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCompanyName(String companyName){
		this.companyName=companyName;
	}

	public String getCompanyName(){
		return this.companyName;
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

	public void setCompanyCode(String companyCode){
		this.companyCode=companyCode;
	}

	public String getCompanyCode(){
		return this.companyCode;
	}

	public void setProductDistributionId(Long productDistributionId){
		this.productDistributionId=productDistributionId;
	}

	public Long getProductDistributionId(){
		return this.productDistributionId;
	}

}