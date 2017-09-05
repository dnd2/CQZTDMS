/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-07-17 11:09:30
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmCoopDealerPO extends PO{

	private Long dsDealerId;
	private Long updateBy;
	private Date updateDate;
	private Long coopDealerId;
	private Long coopId;
	private Long createBy;
	private Date createDate;
	private Integer status;
	private String coopName;
	
	

	public String getCoopName() {
		return coopName;
	}

	public void setCoopName(String coopName) {
		this.coopName = coopName;
	}

	public void setDsDealerId(Long dsDealerId){
		this.dsDealerId=dsDealerId;
	}

	public Long getDsDealerId(){
		return this.dsDealerId;
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

	public void setCoopDealerId(Long coopDealerId){
		this.coopDealerId=coopDealerId;
	}

	public Long getCoopDealerId(){
		return this.coopDealerId;
	}

	public void setCoopId(Long coopId){
		this.coopId=coopId;
	}

	public Long getCoopId(){
		return this.coopId;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

}