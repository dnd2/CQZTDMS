/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-19 11:04:00
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrWrlabinfoPO extends PO{

	private Date updateDate;
	private String treeCode;
	private String remark;
	private Long createBy;
	private Integer isSend;
	private Float labourQuotiety;
	private String labourCode;
	private Integer isDel;
	private Float labourHour;
	private Long oemCompanyId;
	private Long updateBy;
	private Long wrgroupId;
	private String enDes;
	private Long id;
	private Long paterId;
	private String cnDes;
	private Date createDate;
	private Integer ifStatus;
	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setTreeCode(String treeCode){
		this.treeCode=treeCode;
	}

	public String getTreeCode(){
		return this.treeCode;
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

	public void setIsSend(Integer isSend){
		this.isSend=isSend;
	}

	public Integer getIsSend(){
		return this.isSend;
	}

	public void setLabourQuotiety(Float labourQuotiety){
		this.labourQuotiety=labourQuotiety;
	}

	public Float getLabourQuotiety(){
		return this.labourQuotiety;
	}

	public void setLabourCode(String labourCode){
		this.labourCode=labourCode;
	}

	public String getLabourCode(){
		return this.labourCode;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setLabourHour(Float labourHour){
		this.labourHour=labourHour;
	}

	public Float getLabourHour(){
		return this.labourHour;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setWrgroupId(Long wrgroupId){
		this.wrgroupId=wrgroupId;
	}

	public Long getWrgroupId(){
		return this.wrgroupId;
	}

	public void setEnDes(String enDes){
		this.enDes=enDes;
	}

	public String getEnDes(){
		return this.enDes;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setPaterId(Long paterId){
		this.paterId=paterId;
	}

	public Long getPaterId(){
		return this.paterId;
	}

	public void setCnDes(String cnDes){
		this.cnDes=cnDes;
	}

	public String getCnDes(){
		return this.cnDes;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public Integer getIfStatus() {
		return ifStatus;
	}

	public void setIfStatus(Integer ifStatus) {
		this.ifStatus = ifStatus;
	}

}