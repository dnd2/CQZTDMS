/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-05 18:40:34
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.eai.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TiRepairOrderPO extends PO{

	private String repairType;
	private Date completeTime;
	private Date updateDate;
	private Integer isPreSale;
	private Date deliveryDate;
	private String lastBalanceNo;
	private Long createBy;
	private String roNo;
	private Double outMileage;
	private Date createDate;
	private String repairItemSum;
	private String balanceNo;
	private Double inMileage;
	private Date startTime;
	private Long updateBy;
	private String roType;
	private Long seqId;
	private Integer isRed;
	private String serviceAdvisor;

	public void setRepairType(String repairType){
		this.repairType=repairType;
	}

	public String getRepairType(){
		return this.repairType;
	}

	public void setCompleteTime(Date completeTime){
		this.completeTime=completeTime;
	}

	public Date getCompleteTime(){
		return this.completeTime;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setIsPreSale(Integer isPreSale){
		this.isPreSale=isPreSale;
	}

	public Integer getIsPreSale(){
		return this.isPreSale;
	}

	public void setDeliveryDate(Date deliveryDate){
		this.deliveryDate=deliveryDate;
	}

	public Date getDeliveryDate(){
		return this.deliveryDate;
	}

	public void setLastBalanceNo(String lastBalanceNo){
		this.lastBalanceNo=lastBalanceNo;
	}

	public String getLastBalanceNo(){
		return this.lastBalanceNo;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRoNo(String roNo){
		this.roNo=roNo;
	}

	public String getRoNo(){
		return this.roNo;
	}

	public void setOutMileage(Double outMileage){
		this.outMileage=outMileage;
	}

	public Double getOutMileage(){
		return this.outMileage;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setRepairItemSum(String repairItemSum){
		this.repairItemSum=repairItemSum;
	}

	public String getRepairItemSum(){
		return this.repairItemSum;
	}

	public void setBalanceNo(String balanceNo){
		this.balanceNo=balanceNo;
	}

	public String getBalanceNo(){
		return this.balanceNo;
	}

	public void setInMileage(Double inMileage){
		this.inMileage=inMileage;
	}

	public Double getInMileage(){
		return this.inMileage;
	}

	public void setStartTime(Date startTime){
		this.startTime=startTime;
	}

	public Date getStartTime(){
		return this.startTime;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setRoType(String roType){
		this.roType=roType;
	}

	public String getRoType(){
		return this.roType;
	}

	public void setSeqId(Long seqId){
		this.seqId=seqId;
	}

	public Long getSeqId(){
		return this.seqId;
	}

	public void setIsRed(Integer isRed){
		this.isRed=isRed;
	}

	public Integer getIsRed(){
		return this.isRed;
	}

	public void setServiceAdvisor(String serviceAdvisor){
		this.serviceAdvisor=serviceAdvisor;
	}

	public String getServiceAdvisor(){
		return this.serviceAdvisor;
	}

}