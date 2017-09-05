/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-27 10:01:25
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmInventoryPO extends PO{

	private Long companyId;
	private Integer vhclCount;
	private Date updateDate;
	private Date createDate;
	private Long productId;

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setVhclCount(Integer vhclCount){
		this.vhclCount=vhclCount;
	}

	public Integer getVhclCount(){
		return this.vhclCount;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setProductId(Long productId){
		this.productId=productId;
	}

	public Long getProductId(){
		return this.productId;
	}

}