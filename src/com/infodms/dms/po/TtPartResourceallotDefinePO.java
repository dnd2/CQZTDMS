/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-06 21:38:26
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartResourceallotDefinePO extends PO{

	private Integer state;
	private Date disableDate;
	private Float allotRatio;
	private Date deleteDate;
	private String partName;
	private Date updateDate;
	private Integer boTosales;
	private Long createBy;
	private Integer status;
	private String partCode;
	private Integer allotNum;
	private Long defId;
	private Long updateBy;
	private Long partId;
	private String partOldcode;
	private Long deleteBy;
	private Long disableBy;
	private Date createDate;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setAllotRatio(Float allotRatio){
		this.allotRatio=allotRatio;
	}

	public Float getAllotRatio(){
		return this.allotRatio;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setBoTosales(Integer boTosales){
		this.boTosales=boTosales;
	}

	public Integer getBoTosales(){
		return this.boTosales;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setAllotNum(Integer allotNum){
		this.allotNum=allotNum;
	}

	public Integer getAllotNum(){
		return this.allotNum;
	}

	public void setDefId(Long defId){
		this.defId=defId;
	}

	public Long getDefId(){
		return this.defId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setPartOldcode(String partOldcode){
		this.partOldcode=partOldcode;
	}

	public String getPartOldcode(){
		return this.partOldcode;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}