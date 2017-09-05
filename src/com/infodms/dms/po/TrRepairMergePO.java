/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2012-11-14 10:41:05
* CreateBy   : ray
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TrRepairMergePO extends PO{

	private Long srId;
	private Long drId;
	private String updateBy;
	private Date updateDate;
	private Date mergeDate;
	private Long id;
	private String createBy;
	private Date createDate;
	private Integer status;

	public void setSrId(Long srId){
		this.srId=srId;
	}

	public Long getSrId(){
		return this.srId;
	}

	public void setDrId(Long drId){
		this.drId=drId;
	}

	public Long getDrId(){
		return this.drId;
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

	public void setMergeDate(Date mergeDate){
		this.mergeDate=mergeDate;
	}

	public Date getMergeDate(){
		return this.mergeDate;
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

}