/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-12 15:16:40
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartLoactionDefinePO extends PO{

	private String locCode;
	private Integer state;
	private Long minPkg;
	private Date disableDate;
	private Date deleteDate;
	private Date updateDate;
	private Long createBy;
	private String subLoc;
	private Integer status;
	private String pkgSize;
	private Long whId;
	private String locName;
	private Long orgId;
	private Long updateBy;
	private Long partId;
	private Long locId;
	private Long relocId;
	private Date createDate;
	private Long disableBy;
	private Long deleteBy;
	private Long oemMinPkg;
	private Long whmanId;

	public void setLocCode(String locCode){
		this.locCode=locCode;
	}

	public String getLocCode(){
		return this.locCode;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setMinPkg(Long minPkg){
		this.minPkg=minPkg;
	}

	public Long getMinPkg(){
		return this.minPkg;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

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

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setSubLoc(String subLoc){
		this.subLoc=subLoc;
	}

	public String getSubLoc(){
		return this.subLoc;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setPkgSize(String pkgSize){
		this.pkgSize=pkgSize;
	}

	public String getPkgSize(){
		return this.pkgSize;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setLocName(String locName){
		this.locName=locName;
	}

	public String getLocName(){
		return this.locName;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
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

	public void setLocId(Long locId){
		this.locId=locId;
	}

	public Long getLocId(){
		return this.locId;
	}

	public void setRelocId(Long relocId){
		this.relocId=relocId;
	}

	public Long getRelocId(){
		return this.relocId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setOemMinPkg(Long oemMinPkg){
		this.oemMinPkg=oemMinPkg;
	}

	public Long getOemMinPkg(){
		return this.oemMinPkg;
	}

	public void setWhmanId(Long whmanId){
		this.whmanId=whmanId;
	}

	public Long getWhmanId(){
		return this.whmanId;
	}

}