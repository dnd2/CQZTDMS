/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-03-06 09:57:07
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtShuzhengDetailPO extends PO{

	private Long updateBy;
	private Date updateDate;
	private Long pledgeId;
	private Long id;
	private Long createBy;
	private String remark;
	private Date expressDate;
	private String expressName;
	private Date createDate;
	private Integer status;
	private String expressNo;

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

	public void setPledgeId(Long pledgeId){
		this.pledgeId=pledgeId;
	}

	public Long getPledgeId(){
		return this.pledgeId;
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

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setExpressDate(Date expressDate){
		this.expressDate=expressDate;
	}

	public Date getExpressDate(){
		return this.expressDate;
	}

	public void setExpressName(String expressName){
		this.expressName=expressName;
	}

	public String getExpressName(){
		return this.expressName;
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

	public void setExpressNo(String expressNo){
		this.expressNo=expressNo;
	}

	public String getExpressNo(){
		return this.expressNo;
	}

}