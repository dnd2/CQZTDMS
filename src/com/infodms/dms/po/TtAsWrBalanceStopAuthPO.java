/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2012-12-12 07:16:22
* CreateBy   : zhuwei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrBalanceStopAuthPO extends PO{

	private String stopBy;
	private Date updateDate;
	private Integer status;
	private String updateBy;
	private String createBy;
	private Date stopDate;
	private Date createDate;
	private String remark;
	private Long stopId;
	private Long id;

	public void setStopBy(String stopBy){
		this.stopBy=stopBy;
	}

	public String getStopBy(){
		return this.stopBy;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setStopDate(Date stopDate){
		this.stopDate=stopDate;
	}

	public Date getStopDate(){
		return this.stopDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setStopId(Long stopId){
		this.stopId=stopId;
	}

	public Long getStopId(){
		return this.stopId;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}