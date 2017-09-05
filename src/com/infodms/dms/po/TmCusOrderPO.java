/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-11-27 16:48:25
* CreateBy   : wswcx
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmCusOrderPO extends PO{

	private Date subDate;
	private Integer cusType;
	private Long yieldly;
	private String erpName;
	private Date chProDate;
	private Date updateDate;
	private Double totalAmount;
	private Long cusOrderId;
	private Long recDealerId;
	private Long createBy;
	private Date createDate;
	private String planRemark;
	private Long dealerId;
	private String orderNo;
	private Long cusNeedId;
	private Long finType;
	private Integer cusNum;
	private String areaRemark;
	private Integer status;
	private Long proOrderId;
	private Long updateBy;
	private Double finAmount;
	private String remark;
	private Date crtOrderDate;

	public void setSubDate(Date subDate){
		this.subDate=subDate;
	}

	public Date getSubDate(){
		return this.subDate;
	}

	public void setCusType(Integer cusType){
		this.cusType=cusType;
	}

	public Integer getCusType(){
		return this.cusType;
	}

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
	}

	public void setErpName(String erpName){
		this.erpName=erpName;
	}

	public String getErpName(){
		return this.erpName;
	}

	public void setChProDate(Date chProDate){
		this.chProDate=chProDate;
	}

	public Date getChProDate(){
		return this.chProDate;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setTotalAmount(Double totalAmount){
		this.totalAmount=totalAmount;
	}

	public Double getTotalAmount(){
		return this.totalAmount;
	}

	public void setCusOrderId(Long cusOrderId){
		this.cusOrderId=cusOrderId;
	}

	public Long getCusOrderId(){
		return this.cusOrderId;
	}

	public void setRecDealerId(Long recDealerId){
		this.recDealerId=recDealerId;
	}

	public Long getRecDealerId(){
		return this.recDealerId;
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

	public void setPlanRemark(String planRemark){
		this.planRemark=planRemark;
	}

	public String getPlanRemark(){
		return this.planRemark;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setCusNeedId(Long cusNeedId){
		this.cusNeedId=cusNeedId;
	}

	public Long getCusNeedId(){
		return this.cusNeedId;
	}

	public void setFinType(Long finType){
		this.finType=finType;
	}

	public Long getFinType(){
		return this.finType;
	}

	public void setCusNum(Integer cusNum){
		this.cusNum=cusNum;
	}

	public Integer getCusNum(){
		return this.cusNum;
	}

	public void setAreaRemark(String areaRemark){
		this.areaRemark=areaRemark;
	}

	public String getAreaRemark(){
		return this.areaRemark;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setProOrderId(Long proOrderId){
		this.proOrderId=proOrderId;
	}

	public Long getProOrderId(){
		return this.proOrderId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setFinAmount(Double finAmount){
		this.finAmount=finAmount;
	}

	public Double getFinAmount(){
		return this.finAmount;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCrtOrderDate(Date crtOrderDate){
		this.crtOrderDate=crtOrderDate;
	}

	public Date getCrtOrderDate(){
		return this.crtOrderDate;
	}

}