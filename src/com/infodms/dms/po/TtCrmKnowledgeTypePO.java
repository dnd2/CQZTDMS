/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-06 15:01:19
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmKnowledgeTypePO extends PO{

	private String typeName;
	private Long updateBy;
	private Date updateDate;
	private Long typeId;
	private Long createBy;
	private Date createDate;
	private Integer status;
	private Long kind;

	public void setTypeName(String typeName){
		this.typeName=typeName;
	}

	public String getTypeName(){
		return this.typeName;
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

	public void setTypeId(Long typeId){
		this.typeId=typeId;
	}

	public Long getTypeId(){
		return this.typeId;
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

	public void setKind(Long kind){
		this.kind=kind;
	}

	public Long getKind(){
		return this.kind;
	}

}