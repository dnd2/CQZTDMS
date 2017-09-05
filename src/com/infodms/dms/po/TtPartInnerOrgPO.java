/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-09-22 15:14:44
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartInnerOrgPO extends PO{

	private Integer state;
	private Date disableDate;
	private String inOrgCode;
	private String address;
	private Date updateDate;
	private Long inOrgId;
	private String remark;
	private Long createBy;
	private Integer status;
	private String linkMan;
	private String linkPhone;
	private Long prtOrgId;
	private Long updateBy;
	private String inOrgName;
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

	public void setInOrgCode(String inOrgCode){
		this.inOrgCode=inOrgCode;
	}

	public String getInOrgCode(){
		return this.inOrgCode;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getAddress(){
		return this.address;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setInOrgId(Long inOrgId){
		this.inOrgId=inOrgId;
	}

	public Long getInOrgId(){
		return this.inOrgId;
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

	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}

	public String getLinkMan(){
		return this.linkMan;
	}

	public void setLinkPhone(String linkPhone){
		this.linkPhone=linkPhone;
	}

	public String getLinkPhone(){
		return this.linkPhone;
	}

	public void setPrtOrgId(Long prtOrgId){
		this.prtOrgId=prtOrgId;
	}

	public Long getPrtOrgId(){
		return this.prtOrgId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setInOrgName(String inOrgName){
		this.inOrgName=inOrgName;
	}

	public String getInOrgName(){
		return this.inOrgName;
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