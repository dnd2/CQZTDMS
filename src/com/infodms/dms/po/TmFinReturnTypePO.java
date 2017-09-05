/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-10-27 15:42:46
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmFinReturnTypePO extends PO{

	private String finReturnCode;
	private Long returnTypeId;
	private Long updateBy;
	private String finReturnName;
	private Date updateDate;
	private Integer finReturnType;
	private Long createBy;
	private Date createDate;
	private Integer status;
	private Integer importType;

	public void setFinReturnCode(String finReturnCode){
		this.finReturnCode=finReturnCode;
	}

	public String getFinReturnCode(){
		return this.finReturnCode;
	}

	public void setReturnTypeId(Long returnTypeId){
		this.returnTypeId=returnTypeId;
	}

	public Long getReturnTypeId(){
		return this.returnTypeId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setFinReturnName(String finReturnName){
		this.finReturnName=finReturnName;
	}

	public String getFinReturnName(){
		return this.finReturnName;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setFinReturnType(Integer finReturnType){
		this.finReturnType=finReturnType;
	}

	public Integer getFinReturnType(){
		return this.finReturnType;
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

	public Integer getImportType() {
		return importType;
	}

	public void setImportType(Integer importType) {
		this.importType = importType;
	}

	
}