/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-25 15:13:20
* CreateBy   : 
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtFleetIntentPO extends PO{

	private String infoGivingMan;
	private Long dlrCompanyId;
	private Long fleetId;
	private Long intentId;
	private String infoRemark;
	private Date updateDate;
	private Long createBy;
	private Integer status;
	private Long oemCompanyId;
	private Long updateBy;
	private Date purchaseDate;
	private String competeRemark;
	private Date reportDate;
	private Date createDate;
	private Date purEndDate;

	public Date getPurEndDate() {
		return purEndDate;
	}

	public void setPurEndDate(Date purEndDate) {
		this.purEndDate = purEndDate;
	}

	public void setInfoGivingMan(String infoGivingMan){
		this.infoGivingMan=infoGivingMan;
	}

	public String getInfoGivingMan(){
		return this.infoGivingMan;
	}

	public void setDlrCompanyId(Long dlrCompanyId){
		this.dlrCompanyId=dlrCompanyId;
	}

	public Long getDlrCompanyId(){
		return this.dlrCompanyId;
	}

	public void setFleetId(Long fleetId){
		this.fleetId=fleetId;
	}

	public Long getFleetId(){
		return this.fleetId;
	}

	public void setIntentId(Long intentId){
		this.intentId=intentId;
	}

	public Long getIntentId(){
		return this.intentId;
	}

	public void setInfoRemark(String infoRemark){
		this.infoRemark=infoRemark;
	}

	public String getInfoRemark(){
		return this.infoRemark;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
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

	public void setPurchaseDate(Date purchaseDate){
		this.purchaseDate=purchaseDate;
	}

	public Date getPurchaseDate(){
		return this.purchaseDate;
	}

	public void setCompeteRemark(String competeRemark){
		this.competeRemark=competeRemark;
	}

	public String getCompeteRemark(){
		return this.competeRemark;
	}

	public void setReportDate(Date reportDate){
		this.reportDate=reportDate;
	}

	public Date getReportDate(){
		return this.reportDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}