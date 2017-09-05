/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-25 08:35:37
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmVhclMaterialPO extends PO{

	private Long companyId;
	private Long categoryId;
	private Integer specialOrderFlag;
	private String configuration;
	private String modelYear;
	private Long createBy;
	private Date createDate;
	private Date enableDate;
	private Integer ifStatus;
	private Double vhclPrice;
	private String erpModel;
	private Date issueDate;
	private Date disableDate;
	private String prCode;
	private Integer status;
	private Integer rushOrderFlag;
	private Long materialId;
	private String materialName;
	private Integer forecastFlag;
	private Integer orderFlag;
	private Date updateDate;
	private String erpName;
	private String erpNameRel;
	private String colorName;
	private String colorCode;
	private Integer matType;
	private Long erpId;
	private String trimCode;
	private Integer normalOrderFlag;
	private String erpPackage;
	private String materialCode;
	private Long updateBy;
	private String remark2;
	private Integer exportSalesFlag;
	private Double comVhclPrice;
	private Integer isInsale;
	private String xpCode;
	private String xpName;
	
	public Integer getIsInsale() {
		return isInsale;
	}

	public void setIsInsale(Integer isInsale) {
		this.isInsale = isInsale;
	}

	public Double getComVhclPrice() {
		return comVhclPrice;
	}

	public void setComVhclPrice(Double comVhclPrice) {
		this.comVhclPrice = comVhclPrice;
	}

	public Integer getExportSalesFlag() {
		return exportSalesFlag;
	}

	public void setExportSalesFlag(Integer exportSalesFlag) {
		this.exportSalesFlag = exportSalesFlag;
	}

	public Integer getProcuctFlag() {
		return procuctFlag;
	}

	public void setProcuctFlag(Integer procuctFlag) {
		this.procuctFlag = procuctFlag;
	}

	private String modelCode;
	private Integer procuctFlag;

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setCategoryId(Long categoryId){
		this.categoryId=categoryId;
	}

	public Long getCategoryId(){
		return this.categoryId;
	}

	public void setSpecialOrderFlag(Integer specialOrderFlag){
		this.specialOrderFlag=specialOrderFlag;
	}

	public Integer getSpecialOrderFlag(){
		return this.specialOrderFlag;
	}

	public void setConfiguration(String configuration){
		this.configuration=configuration;
	}

	public String getConfiguration(){
		return this.configuration;
	}

	public void setModelYear(String modelYear){
		this.modelYear=modelYear;
	}

	public String getModelYear(){
		return this.modelYear;
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

	public void setEnableDate(Date enableDate){
		this.enableDate=enableDate;
	}

	public Date getEnableDate(){
		return this.enableDate;
	}

	public void setIfStatus(Integer ifStatus){
		this.ifStatus=ifStatus;
	}

	public Integer getIfStatus(){
		return this.ifStatus;
	}

	public void setVhclPrice(Double vhclPrice){
		this.vhclPrice=vhclPrice;
	}

	public Double getVhclPrice(){
		return this.vhclPrice;
	}

	public void setErpModel(String erpModel){
		this.erpModel=erpModel;
	}

	public String getErpModel(){
		return this.erpModel;
	}

	public void setIssueDate(Date issueDate){
		this.issueDate=issueDate;
	}

	public Date getIssueDate(){
		return this.issueDate;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setPrCode(String prCode){
		this.prCode=prCode;
	}

	public String getPrCode(){
		return this.prCode;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setRushOrderFlag(Integer rushOrderFlag){
		this.rushOrderFlag=rushOrderFlag;
	}

	public Integer getRushOrderFlag(){
		return this.rushOrderFlag;
	}

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}

	public void setMaterialName(String materialName){
		this.materialName=materialName;
	}

	public String getMaterialName(){
		return this.materialName;
	}

	public void setForecastFlag(Integer forecastFlag){
		this.forecastFlag=forecastFlag;
	}

	public Integer getForecastFlag(){
		return this.forecastFlag;
	}

	public void setOrderFlag(Integer orderFlag){
		this.orderFlag=orderFlag;
	}

	public Integer getOrderFlag(){
		return this.orderFlag;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setErpName(String erpName){
		this.erpName=erpName;
	}

	public String getErpName(){
		return this.erpName;
	}

	public void setErpNameRel(String erpNameRel){
		this.erpNameRel=erpNameRel;
	}

	public String getErpNameRel(){
		return this.erpNameRel;
	}

	public void setColorName(String colorName){
		this.colorName=colorName;
	}

	public String getColorName(){
		return this.colorName;
	}

	public void setColorCode(String colorCode){
		this.colorCode=colorCode;
	}

	public String getColorCode(){
		return this.colorCode;
	}

	public void setMatType(Integer matType){
		this.matType=matType;
	}

	public Integer getMatType(){
		return this.matType;
	}

	public void setErpId(Long erpId){
		this.erpId=erpId;
	}

	public Long getErpId(){
		return this.erpId;
	}

	public void setTrimCode(String trimCode){
		this.trimCode=trimCode;
	}

	public String getTrimCode(){
		return this.trimCode;
	}

	public void setNormalOrderFlag(Integer normalOrderFlag){
		this.normalOrderFlag=normalOrderFlag;
	}

	public Integer getNormalOrderFlag(){
		return this.normalOrderFlag;
	}

	public void setErpPackage(String erpPackage){
		this.erpPackage=erpPackage;
	}

	public String getErpPackage(){
		return this.erpPackage;
	}

	public void setMaterialCode(String materialCode){
		this.materialCode=materialCode;
	}

	public String getMaterialCode(){
		return this.materialCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setRemark2(String remark2){
		this.remark2=remark2;
	}

	public String getRemark2(){
		return this.remark2;
	}

	public void setModelCode(String modelCode){
		this.modelCode=modelCode;
	}

	public String getModelCode(){
		return this.modelCode;
	}

	public String getXpCode()
	{
		return xpCode;
	}

	public void setXpCode(String xpCode)
	{
		this.xpCode = xpCode;
	}

	public String getXpName()
	{
		return xpName;
	}

	public void setXpName(String xpName)
	{
		this.xpName = xpName;
	}

}