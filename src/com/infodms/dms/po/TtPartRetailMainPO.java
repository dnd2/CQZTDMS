/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-27 17:18:58
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartRetailMainPO extends PO{

	private Integer state;
	private Date disableDate;
	private Date deleteDate;
	private Date updateDate;
	private Long retailId;
	private String whCname;
	private String sorgCname;
	private String remark;
	private Long createBy;
	private Integer chgType;
	private Integer status;
	private Long whId;
	private String purpose;
	private Long updateBy;
	private String linkman;
	private String tel;
	private Integer ver;
	private String sorgCode;
	private Long deleteBy;
	private Long disableBy;
	private Date createDate;
	private String retailCode;
	private Long sorgId;

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

	public void setRetailId(Long retailId){
		this.retailId=retailId;
	}

	public Long getRetailId(){
		return this.retailId;
	}

	public void setWhCname(String whCname){
		this.whCname=whCname;
	}

	public String getWhCname(){
		return this.whCname;
	}

	public void setSorgCname(String sorgCname){
		this.sorgCname=sorgCname;
	}

	public String getSorgCname(){
		return this.sorgCname;
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

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setPurpose(String purpose){
		this.purpose=purpose;
	}

	public String getPurpose(){
		return this.purpose;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setLinkman(String linkman){
		this.linkman=linkman;
	}

	public String getLinkman(){
		return this.linkman;
	}

	public void setTel(String tel){
		this.tel=tel;
	}

	public String getTel(){
		return this.tel;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setSorgCode(String sorgCode){
		this.sorgCode=sorgCode;
	}

	public String getSorgCode(){
		return this.sorgCode;
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

	public void setRetailCode(String retailCode){
		this.retailCode=retailCode;
	}

	public String getRetailCode(){
		return this.retailCode;
	}

	public void setSorgId(Long sorgId){
		this.sorgId=sorgId;
	}

	public Long getSorgId(){
		return this.sorgId;
	}

}