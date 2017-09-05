/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-06 16:36:07
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartSpcpIostockMainPO extends PO{

	private Integer state;
	private Long spcpdId;
	private Integer spcpdType;
	private String orgCname;
	private Date disableDate;
	private Date deleteDate;
	private Date updateDate;
	private String whCname;
	private String remark;
	private Long createBy;
	private Integer status;
	private Long iostockId;
	private Long whId;
	private Long orgId;
	private Long updateBy;
	private Integer ver;
	private String spcpdCode;
	private String orgCode;
	private Long deleteBy;
	private Long disableBy;
	private Date createDate;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setSpcpdId(Long spcpdId){
		this.spcpdId=spcpdId;
	}

	public Long getSpcpdId(){
		return this.spcpdId;
	}

	public void setSpcpdType(Integer spcpdType){
		this.spcpdType=spcpdType;
	}

	public Integer getSpcpdType(){
		return this.spcpdType;
	}

	public void setOrgCname(String orgCname){
		this.orgCname=orgCname;
	}

	public String getOrgCname(){
		return this.orgCname;
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

	public void setWhCname(String whCname){
		this.whCname=whCname;
	}

	public String getWhCname(){
		return this.whCname;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setIostockId(Long iostockId){
		this.iostockId=iostockId;
	}

	public Long getIostockId(){
		return this.iostockId;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
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

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setSpcpdCode(String spcpdCode){
		this.spcpdCode=spcpdCode;
	}

	public String getSpcpdCode(){
		return this.spcpdCode;
	}

	public void setOrgCode(String orgCode){
		this.orgCode=orgCode;
	}

	public String getOrgCode(){
		return this.orgCode;
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