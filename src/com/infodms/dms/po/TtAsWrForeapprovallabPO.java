/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-21 10:19:21
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrForeapprovallabPO extends PO{

	private Date closeDate;
	private Long oemCompanyId;
	private Long updateBy;
	private String operationDesc;
	private Long wrgroupId;
	private Date updateDate;
	private Long id;
	private Long createBy;
	private Date openDate;
	private Integer isSend;
	private Date createDate;
	private String operationCode;

	public void setCloseDate(Date closeDate){
		this.closeDate=closeDate;
	}

	public Date getCloseDate(){
		return this.closeDate;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setOperationDesc(String operationDesc){
		this.operationDesc=operationDesc;
	}

	public String getOperationDesc(){
		return this.operationDesc;
	}

	public void setWrgroupId(Long wrgroupId){
		this.wrgroupId=wrgroupId;
	}

	public Long getWrgroupId(){
		return this.wrgroupId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOpenDate(Date openDate){
		this.openDate=openDate;
	}

	public Date getOpenDate(){
		return this.openDate;
	}

	public void setIsSend(Integer isSend){
		this.isSend=isSend;
	}

	public Integer getIsSend(){
		return this.isSend;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOperationCode(String operationCode){
		this.operationCode=operationCode;
	}

	public String getOperationCode(){
		return this.operationCode;
	}

}