/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-05-21 15:42:28
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtInspectionDetailPO extends PO{

	private Long inspectionId;
	private Long updateBy;
	private Date updateDate;
	private String damageDesc;
	private Long createBy;
	private Long detailId;
	private Date createDate;
	private String damagePart;

	public void setInspectionId(Long inspectionId){
		this.inspectionId=inspectionId;
	}

	public Long getInspectionId(){
		return this.inspectionId;
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

	public void setDamageDesc(String damageDesc){
		this.damageDesc=damageDesc;
	}

	public String getDamageDesc(){
		return this.damageDesc;
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

	public void setDamagePart(String damagePart){
		this.damagePart=damagePart;
	}

	public String getDamagePart(){
		return this.damagePart;
	}

}