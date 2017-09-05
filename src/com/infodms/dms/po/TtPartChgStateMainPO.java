/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-26 11:39:37
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartChgStateMainPO extends PO{

	private Integer state;
	private Date disableDate;
	private Date deleteDate;
	private Date updateDate;
	private String whCname;
	private String changeCode;
	private String remark;
	private Long createBy;
	private Integer chgType;
	private Integer status;
	private String chgorgCode;
	private Long whId;
	private Long updateBy;
	private Long changeId;
	private Integer ver;
	private Long deleteBy;
	private Long disableBy;
	private Date createDate;
	private Long chgorgId;
	private String chgorgCname;

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

	public void setChangeCode(String changeCode){
		this.changeCode=changeCode;
	}

	public String getChangeCode(){
		return this.changeCode;
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

	public void setChgType(Integer chgType){
		this.chgType=chgType;
	}

	public Integer getChgType(){
		return this.chgType;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setChgorgCode(String chgorgCode){
		this.chgorgCode=chgorgCode;
	}

	public String getChgorgCode(){
		return this.chgorgCode;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setChangeId(Long changeId){
		this.changeId=changeId;
	}

	public Long getChangeId(){
		return this.changeId;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
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

	public void setChgorgId(Long chgorgId){
		this.chgorgId=chgorgId;
	}

	public Long getChgorgId(){
		return this.chgorgId;
	}

	public void setChgorgCname(String chgorgCname){
		this.chgorgCname=chgorgCname;
	}

	public String getChgorgCname(){
		return this.chgorgCname;
	}

}