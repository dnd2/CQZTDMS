/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-01 10:06:16
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrClaimAutoPO extends PO{

	private Integer autoType;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private Long autoId;
	private Integer isUse;
	private String action;
	private Integer status;
	private Long updateBy;
	private String authCode;
	private Long oemCompanyId;
	private String authDesc;
	private String remark;

	public void setAutoType(Integer autoType){
		this.autoType=autoType;
	}

	public Integer getAutoType(){
		return this.autoType;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAutoId(Long autoId){
		this.autoId=autoId;
	}

	public Long getAutoId(){
		return this.autoId;
	}

	public void setIsUse(Integer isUse){
		this.isUse=isUse;
	}

	public Integer getIsUse(){
		return this.isUse;
	}

	public void setAction(String action){
		this.action=action;
	}

	public String getAction(){
		return this.action;
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

	public void setAuthCode(String authCode){
		this.authCode=authCode;
	}

	public String getAuthCode(){
		return this.authCode;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setAuthDesc(String authDesc){
		this.authDesc=authDesc;
	}

	public String getAuthDesc(){
		return this.authDesc;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

}