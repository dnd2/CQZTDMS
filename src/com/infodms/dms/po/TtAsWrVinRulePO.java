/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-27 14:05:07
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrVinRulePO extends PO{

	private Integer partWrType;
	private String vrLawStandard;
	private Long vrId;
	private Date updateDate;
	private Long createBy;
	private Integer vrIsAtt;
	private Integer vrLaw;
	private Integer vrLevel;
	private String vrPartCode;
	private Long updateBy;
	private Integer vrType;
	private String vrCode;
	private Date createDate;
	private Integer vrWarranty;

	public void setPartWrType(Integer partWrType){
		this.partWrType=partWrType;
	}

	public Integer getPartWrType(){
		return this.partWrType;
	}

	public void setVrLawStandard(String vrLawStandard){
		this.vrLawStandard=vrLawStandard;
	}

	public String getVrLawStandard(){
		return this.vrLawStandard;
	}

	public void setVrId(Long vrId){
		this.vrId=vrId;
	}

	public Long getVrId(){
		return this.vrId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setVrIsAtt(Integer vrIsAtt){
		this.vrIsAtt=vrIsAtt;
	}

	public Integer getVrIsAtt(){
		return this.vrIsAtt;
	}

	public void setVrLaw(Integer vrLaw){
		this.vrLaw=vrLaw;
	}

	public Integer getVrLaw(){
		return this.vrLaw;
	}

	public void setVrLevel(Integer vrLevel){
		this.vrLevel=vrLevel;
	}

	public Integer getVrLevel(){
		return this.vrLevel;
	}

	public void setVrPartCode(String vrPartCode){
		this.vrPartCode=vrPartCode;
	}

	public String getVrPartCode(){
		return this.vrPartCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setVrType(Integer vrType){
		this.vrType=vrType;
	}

	public Integer getVrType(){
		return this.vrType;
	}

	public void setVrCode(String vrCode){
		this.vrCode=vrCode;
	}

	public String getVrCode(){
		return this.vrCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setVrWarranty(Integer vrWarranty){
		this.vrWarranty=vrWarranty;
	}

	public Integer getVrWarranty(){
		return this.vrWarranty;
	}

}