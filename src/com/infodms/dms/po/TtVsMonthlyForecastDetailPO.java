/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-21 14:04:57
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsMonthlyForecastDetailPO extends PO{

	private Long materialId;
	private Integer forecastAmount;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Long detailId;
	private Date createDate;
	private Long proDetailId;
	private Integer isChange;
	private Long forecastId;
	private Long packageId;
	private String xpCode;
	private String xpName;
	private String colorCode;
	private String colorName;
	private String xpGysCode;
	private String xpGysName;
	
	private Long groupId;
	
	

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}

	public void setForecastAmount(Integer forecastAmount){
		this.forecastAmount=forecastAmount;
	}

	public Integer getForecastAmount(){
		return this.forecastAmount;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
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

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setProDetailId(Long proDetailId){
		this.proDetailId=proDetailId;
	}

	public Long getProDetailId(){
		return this.proDetailId;
	}

	public void setIsChange(Integer isChange){
		this.isChange=isChange;
	}

	public Integer getIsChange(){
		return this.isChange;
	}

	public void setForecastId(Long forecastId){
		this.forecastId=forecastId;
	}

	public Long getForecastId(){
		return this.forecastId;
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

	public String getColorCode()
	{
		return colorCode;
	}

	public void setColorCode(String colorCode)
	{
		this.colorCode = colorCode;
	}

	public String getColorName()
	{
		return colorName;
	}

	public void setColorName(String colorName)
	{
		this.colorName = colorName;
	}

	public String getXpGysCode()
	{
		return xpGysCode;
	}

	public void setXpGysCode(String xpGysCode)
	{
		this.xpGysCode = xpGysCode;
	}

	public String getXpGysName()
	{
		return xpGysName;
	}

	public void setXpGysName(String xpGysName)
	{
		this.xpGysName = xpGysName;
	}

	public Long getPackageId()
	{
		return packageId;
	}

	public void setPackageId(Long packageId)
	{
		this.packageId = packageId;
	}

}