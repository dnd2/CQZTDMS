/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-10-28 11:26:12
* CreateBy   : Iverson
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsDealerCheckPO extends PO{

	private String checkPercentage;
	private Date lastCheckDate;
	private Long dealerId;
	private String updateBy;
	private Date updateDate;
	private Integer checkCount;
	private Long id;
	private String createBy;
	private Long yieldly;
	private Date createDate;
	private Integer status;

	public void setCheckPercentage(String checkPercentage){
		this.checkPercentage=checkPercentage;
	}

	public String getCheckPercentage(){
		return this.checkPercentage;
	}

	public void setLastCheckDate(Date lastCheckDate){
		this.lastCheckDate=lastCheckDate;
	}

	public Date getLastCheckDate(){
		return this.lastCheckDate;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCheckCount(Integer checkCount){
		this.checkCount=checkCount;
	}

	public Integer getCheckCount(){
		return this.checkCount;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
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

	public Long getYieldly() {
		return yieldly;
	}

	public void setYieldly(Long yieldly) {
		this.yieldly = yieldly;
	}

}