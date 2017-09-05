/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-31 13:54:27
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartTransPlanDtlPO extends PO{

	private Date deleteDate;
	private Date updateDate;
	private Long trplineId;
	private String remark;
	private Long createBy;
	private Long trplanId;
	private Long pickOrderId;
	private Integer status;
	private Long updateBy;
	private Integer ver;
	private Date createDate;
	private Long deleteBy;
	private String pkgNo;

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setTrplineId(Long trplineId){
		this.trplineId=trplineId;
	}

	public Long getTrplineId(){
		return this.trplineId;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setTrplanId(Long trplanId){
		this.trplanId=trplanId;
	}

	public Long getTrplanId(){
		return this.trplanId;
	}

	public void setPickOrderId(Long pickOrderId){
		this.pickOrderId=pickOrderId;
	}

	public Long getPickOrderId(){
		return this.pickOrderId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setPkgNo(String pkgNo){
		this.pkgNo=pkgNo;
	}

	public String getPkgNo(){
		return this.pkgNo;
	}

}