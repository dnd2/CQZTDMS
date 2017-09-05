/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-27 11:05:53
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartOnwayPO extends PO{

	private Integer state;
	private Long partId;
	private Long createBy;
	private Date createDate;
	private Long onQty;
	private Integer status;
	private Long whId;
	private Long onwayId;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
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

	public void setOnQty(Long onQty){
		this.onQty=onQty;
	}

	public Long getOnQty(){
		return this.onQty;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setOnwayId(Long onwayId){
		this.onwayId=onwayId;
	}

	public Long getOnwayId(){
		return this.onwayId;
	}

}