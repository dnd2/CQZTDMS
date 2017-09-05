/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-09 02:40:30
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmVariableParaPO extends PO{

	private Long paraId;
	private String paraCode;
	private Date updateDate;
	private Integer status;
	private Long updateBy;
	private Integer issue;
	private Integer paraType;
	private Long createBy;
	private Date createDate;
	private String paraName;
	private String remark;
	private Long oemCompanyId;
	

	public Long getOemCompanyId() {
		return oemCompanyId;
	}

	public void setOemCompanyId(Long oemCompanyId) {
		this.oemCompanyId = oemCompanyId;
	}

	public Long getParaId() {
		return paraId;
	}

	public void setParaId(Long paraId) {
		this.paraId = paraId;
	}

	public void setParaCode(String paraCode){
		this.paraCode=paraCode;
	}

	public String getParaCode(){
		return this.paraCode;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
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

	public void setIssue(Integer issue){
		this.issue=issue;
	}

	public Integer getIssue(){
		return this.issue;
	}

	public void setParaType(Integer paraType){
		this.paraType=paraType;
	}

	public Integer getParaType(){
		return this.paraType;
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

	public void setParaName(String paraName){
		this.paraName=paraName;
	}

	public String getParaName(){
		return this.paraName;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

}