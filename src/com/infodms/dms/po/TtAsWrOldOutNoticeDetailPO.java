/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-13 14:39:09
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrOldOutNoticeDetailPO extends PO{

	private Double otherPrice;
	private Double partPrice;
	private String partName;
	private Long createBy;
	private Integer outNum;
	private Double total;
	private String modelName;
	private Float claimPrice;
	private Double smallTotal;
	private Float claimLabour;
	private Double labourPrice;
	private Double taxTotal;
	private Long id;
	private Date createDate;
	private Long noticeId;
	private String partCode;
	private Integer outPartType;
	public Integer getOutPartType() {
		return outPartType;
	}

	public void setOutPartType(Integer outPartType) {
		this.outPartType = outPartType;
	}

	public String getPartCode() {
		return partCode;
	}

	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}

	public void setOtherPrice(Double otherPrice){
		this.otherPrice=otherPrice;
	}

	public Double getOtherPrice(){
		return this.otherPrice;
	}

	public void setPartPrice(Double partPrice){
		this.partPrice=partPrice;
	}

	public Double getPartPrice(){
		return this.partPrice;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOutNum(Integer outNum){
		this.outNum=outNum;
	}

	public Integer getOutNum(){
		return this.outNum;
	}

	public void setTotal(Double total){
		this.total=total;
	}

	public Double getTotal(){
		return this.total;
	}

	public void setModelName(String modelName){
		this.modelName=modelName;
	}

	public String getModelName(){
		return this.modelName;
	}

	public void setClaimPrice(Float claimPrice){
		this.claimPrice=claimPrice;
	}

	public Float getClaimPrice(){
		return this.claimPrice;
	}

	public void setSmallTotal(Double smallTotal){
		this.smallTotal=smallTotal;
	}

	public Double getSmallTotal(){
		return this.smallTotal;
	}

	public void setClaimLabour(Float claimLabour){
		this.claimLabour=claimLabour;
	}

	public Float getClaimLabour(){
		return this.claimLabour;
	}

	public void setLabourPrice(Double labourPrice){
		this.labourPrice=labourPrice;
	}

	public Double getLabourPrice(){
		return this.labourPrice;
	}

	public void setTaxTotal(Double taxTotal){
		this.taxTotal=taxTotal;
	}

	public Double getTaxTotal(){
		return this.taxTotal;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setNoticeId(Long noticeId){
		this.noticeId=noticeId;
	}

	public Long getNoticeId(){
		return this.noticeId;
	}

}