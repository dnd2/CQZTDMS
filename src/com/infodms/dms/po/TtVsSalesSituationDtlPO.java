/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-19 15:40:13
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsSalesSituationDtlPO extends PO{

	private Long orderAmount;
	private Long materialId;
	private Long retAmount;
	private Long situationId;
	private Long updateBy;
	private Date updateDate;
	private Long poorAmount;
	private Long createBy;
	private Long detailId;
	private Date createDate;

	public void setOrderAmount(Long orderAmount){
		this.orderAmount=orderAmount;
	}

	public Long getOrderAmount(){
		return this.orderAmount;
	}

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}

	public void setRetAmount(Long retAmount){
		this.retAmount=retAmount;
	}

	public Long getRetAmount(){
		return this.retAmount;
	}

	public void setSituationId(Long situationId){
		this.situationId=situationId;
	}

	public Long getSituationId(){
		return this.situationId;
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

	public void setPoorAmount(Long poorAmount){
		this.poorAmount=poorAmount;
	}

	public Long getPoorAmount(){
		return this.poorAmount;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}