/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-08-25 09:09:13
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsCostPO extends PO{

	private Integer costType;
	private Date updateDate;
	private Double costAmount;
	private Long updateBy;
	private Long costId;
	private Long createBy;
	private Long orgId;
	private Date createDate;
	private Double availableAmount;
	private Double freezeAmount;
	private Integer costSource;
	private Long dealerId;
	private Long areaId ;

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public void setCostType(Integer costType){
		this.costType=costType;
	}

	public Integer getCostType(){
		return this.costType;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCostAmount(Double costAmount){
		this.costAmount=costAmount;
	}

	public Double getCostAmount(){
		return this.costAmount;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCostId(Long costId){
		this.costId=costId;
	}

	public Long getCostId(){
		return this.costId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAvailableAmount(Double availableAmount){
		this.availableAmount=availableAmount;
	}

	public Double getAvailableAmount(){
		return this.availableAmount;
	}

	public void setFreezeAmount(Double freezeAmount){
		this.freezeAmount=freezeAmount;
	}

	public Double getFreezeAmount(){
		return this.freezeAmount;
	}

	public void setCostSource(Integer costSource){
		this.costSource=costSource;
	}

	public Integer getCostSource(){
		return this.costSource;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

}