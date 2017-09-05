/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-05-16 14:54:40
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsUsedCarDisplacementPO extends PO{

	private Integer operateStatus;
	private Date updateDate;
	private Long createBy;
	private String oldVin;
	private Date createDate;
	private Long salesOrderId;
	private Long dealerId;
	private String oldModelName;
	private String oldBrandName;
	private Long vehicleId;
	private Date scrapDate;
	private Long updateBy;
	private Integer displacementType;
	private String scrapCertifyNo;
	private Long oemCompanyId;
	private Date oldSlesDate;
	private Long displacementId;
	private String remark;
	private Integer status;
	private String hostName ;
	private String displacementNo;
	private Double priceAmount;
	private Long isChana ;
	
	public Double getPriceAmount() {
		return priceAmount;
	}

	public void setPriceAmount(Double priceAmount) {
		this.priceAmount = priceAmount;
	}
	public String getDisplacementNo() {
		return displacementNo;
	}

	public void setDisplacementNo(String displacementNo) {
		this.displacementNo = displacementNo;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setOperateStatus(Integer operateStatus){
		this.operateStatus=operateStatus;
	}

	public Integer getOperateStatus(){
		return this.operateStatus;
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

	public void setOldVin(String oldVin){
		this.oldVin=oldVin;
	}

	public String getOldVin(){
		return this.oldVin;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSalesOrderId(Long salesOrderId){
		this.salesOrderId=salesOrderId;
	}

	public Long getSalesOrderId(){
		return this.salesOrderId;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setOldModelName(String oldModelName){
		this.oldModelName=oldModelName;
	}

	public String getOldModelName(){
		return this.oldModelName;
	}

	public void setOldBrandName(String oldBrandName){
		this.oldBrandName=oldBrandName;
	}

	public String getOldBrandName(){
		return this.oldBrandName;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

	public void setScrapDate(Date scrapDate){
		this.scrapDate=scrapDate;
	}

	public Date getScrapDate(){
		return this.scrapDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDisplacementType(Integer displacementType){
		this.displacementType=displacementType;
	}

	public Integer getDisplacementType(){
		return this.displacementType;
	}

	public void setScrapCertifyNo(String scrapCertifyNo){
		this.scrapCertifyNo=scrapCertifyNo;
	}

	public String getScrapCertifyNo(){
		return this.scrapCertifyNo;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setOldSlesDate(Date oldSlesDate){
		this.oldSlesDate=oldSlesDate;
	}

	public Date getOldSlesDate(){
		return this.oldSlesDate;
	}

	public void setDisplacementId(Long displacementId){
		this.displacementId=displacementId;
	}

	public Long getDisplacementId(){
		return this.displacementId;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public Long getIsChana() {
		return isChana;
	}

	public void setIsChana(Long isChana) {
		this.isChana = isChana;
	}

}