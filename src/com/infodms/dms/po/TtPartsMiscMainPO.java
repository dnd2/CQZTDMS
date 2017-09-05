/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-06-04 13:34:23
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartsMiscMainPO extends PO{

	private Long miscOrderId;
	private Integer state;
	private Date deleteDate;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Long whId;
	private Long orgId;
	private Integer bType;
	private Integer exType;
	private String miscOrderCode;
	private Long updateBy;
	private String departmentCode;
	private Date createDate;
	private Long deleteBy;

	public void setMiscOrderId(Long miscOrderId){
		this.miscOrderId=miscOrderId;
	}

	public Long getMiscOrderId(){
		return this.miscOrderId;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
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

	public void setBType(Integer bType){
		this.bType=bType;
	}

	public Integer getBType(){
		return this.bType;
	}

	public void setExType(Integer exType){
		this.exType=exType;
	}

	public Integer getExType(){
		return this.exType;
	}

	public void setMiscOrderCode(String miscOrderCode){
		this.miscOrderCode=miscOrderCode;
	}

	public String getMiscOrderCode(){
		return this.miscOrderCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDepartmentCode(String departmentCode){
		this.departmentCode=departmentCode;
	}

	public String getDepartmentCode(){
		return this.departmentCode;
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

}