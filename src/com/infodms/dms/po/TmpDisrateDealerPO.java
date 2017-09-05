/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-04-22 16:20:32
* CreateBy   : -Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpDisrateDealerPO extends PO{

	private Double otherDisrate;
	private String disRateAmount;
	private String dealerCode;
	private String buildAmount;
	private String serName;
	private String modelName;
	private String dealerName;
	private String packageCode;
	private String lastSettleAmount;
	private Integer rowNum;
	private String settleAmount;
	private String packageName;
	private String disRate;
	private String startDate;
	private String stopDate;
	private String otherNo;

	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public String getStopDate()
	{
		return stopDate;
	}

	public void setStopDate(String stopDate)
	{
		this.stopDate = stopDate;
	}

	public String getOtherNo()
	{
		return otherNo;
	}

	public void setOtherNo(String otherNo)
	{
		this.otherNo = otherNo;
	}

	public void setOtherDisrate(Double otherDisrate){
		this.otherDisrate=otherDisrate;
	}

	public Double getOtherDisrate(){
		return this.otherDisrate;
	}

	public void setDisRateAmount(String disRateAmount){
		this.disRateAmount=disRateAmount;
	}

	public String getDisRateAmount(){
		return this.disRateAmount;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setBuildAmount(String buildAmount){
		this.buildAmount=buildAmount;
	}

	public String getBuildAmount(){
		return this.buildAmount;
	}

	public void setSerName(String serName){
		this.serName=serName;
	}

	public String getSerName(){
		return this.serName;
	}

	public void setModelName(String modelName){
		this.modelName=modelName;
	}

	public String getModelName(){
		return this.modelName;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setPackageCode(String packageCode){
		this.packageCode=packageCode;
	}

	public String getPackageCode(){
		return this.packageCode;
	}

	public void setLastSettleAmount(String lastSettleAmount){
		this.lastSettleAmount=lastSettleAmount;
	}

	public String getLastSettleAmount(){
		return this.lastSettleAmount;
	}

	public void setRowNum(Integer rowNum){
		this.rowNum=rowNum;
	}

	public Integer getRowNum(){
		return this.rowNum;
	}

	public void setSettleAmount(String settleAmount){
		this.settleAmount=settleAmount;
	}

	public String getSettleAmount(){
		return this.settleAmount;
	}

	public void setPackageName(String packageName){
		this.packageName=packageName;
	}

	public String getPackageName(){
		return this.packageName;
	}

	public void setDisRate(String disRate){
		this.disRate=disRate;
	}

	public String getDisRate(){
		return this.disRate;
	}

}