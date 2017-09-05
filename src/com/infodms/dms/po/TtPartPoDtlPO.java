/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-08-03 13:53:12
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartPoDtlPO extends PO{

	private Integer state;
	private Double buyAmount;
	private Integer partType;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Date forecastDate;
	private Integer isGuard;
	private Integer status;
	private Double guardPrice;
	private String openRemark;
	private Long polineId;
	private Long updateBy;
	private String venderName;
	private Long partId;
	private Long venderId;
	private Long deleteBy;
	private Long disableBy;
	private Long buyQty;
	private String closeRemark;
	private Long spareQty;
	private String unit;
	private Date disableDate;
	private Date deleteDate;
	private Long plineId;
	private Integer isProductRecv;
	private String partCode;
	private Long checkQty;
	private Long orgId;
	private String partCname;
	private Long orderId;
	private Double buyPrice;
	private Integer partCategory;
	private Integer ver;
	private String partOldcode;
	private Date createDate;
	private Long lineNo;
	private Long planQty;
	
	private Integer produceFac;
	private Integer superiorPurchasing;

	public Integer getProduceFac() {
		return produceFac;
	}

	public void setProduceFac(Integer produceFac) {
		this.produceFac = produceFac;
	}

	public Integer getSuperiorPurchasing() {
		return superiorPurchasing;
	}

	public void setSuperiorPurchasing(Integer superiorPurchasing) {
		this.superiorPurchasing = superiorPurchasing;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setBuyAmount(Double buyAmount){
		this.buyAmount=buyAmount;
	}

	public Double getBuyAmount(){
		return this.buyAmount;
	}

	public void setPartType(Integer partType){
		this.partType=partType;
	}

	public Integer getPartType(){
		return this.partType;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
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

	public void setForecastDate(Date forecastDate){
		this.forecastDate=forecastDate;
	}

	public Date getForecastDate(){
		return this.forecastDate;
	}

	public void setIsGuard(Integer isGuard){
		this.isGuard=isGuard;
	}

	public Integer getIsGuard(){
		return this.isGuard;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setGuardPrice(Double guardPrice){
		this.guardPrice=guardPrice;
	}

	public Double getGuardPrice(){
		return this.guardPrice;
	}

	public void setOpenRemark(String openRemark){
		this.openRemark=openRemark;
	}

	public String getOpenRemark(){
		return this.openRemark;
	}

	public void setPolineId(Long polineId){
		this.polineId=polineId;
	}

	public Long getPolineId(){
		return this.polineId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setVenderName(String venderName){
		this.venderName=venderName;
	}

	public String getVenderName(){
		return this.venderName;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setVenderId(Long venderId){
		this.venderId=venderId;
	}

	public Long getVenderId(){
		return this.venderId;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setBuyQty(Long buyQty){
		this.buyQty=buyQty;
	}

	public Long getBuyQty(){
		return this.buyQty;
	}

	public void setCloseRemark(String closeRemark){
		this.closeRemark=closeRemark;
	}

	public String getCloseRemark(){
		return this.closeRemark;
	}

	public void setSpareQty(Long spareQty){
		this.spareQty=spareQty;
	}

	public Long getSpareQty(){
		return this.spareQty;
	}

	public void setUnit(String unit){
		this.unit=unit;
	}

	public String getUnit(){
		return this.unit;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setPlineId(Long plineId){
		this.plineId=plineId;
	}

	public Long getPlineId(){
		return this.plineId;
	}

	public void setIsProductRecv(Integer isProductRecv){
		this.isProductRecv=isProductRecv;
	}

	public Integer getIsProductRecv(){
		return this.isProductRecv;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setCheckQty(Long checkQty){
		this.checkQty=checkQty;
	}

	public Long getCheckQty(){
		return this.checkQty;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setPartCname(String partCname){
		this.partCname=partCname;
	}

	public String getPartCname(){
		return this.partCname;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setBuyPrice(Double buyPrice){
		this.buyPrice=buyPrice;
	}

	public Double getBuyPrice(){
		return this.buyPrice;
	}

	public void setPartCategory(Integer partCategory){
		this.partCategory=partCategory;
	}

	public Integer getPartCategory(){
		return this.partCategory;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setPartOldcode(String partOldcode){
		this.partOldcode=partOldcode;
	}

	public String getPartOldcode(){
		return this.partOldcode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setLineNo(Long lineNo){
		this.lineNo=lineNo;
	}

	public Long getLineNo(){
		return this.lineNo;
	}

	public void setPlanQty(Long planQty){
		this.planQty=planQty;
	}

	public Long getPlanQty(){
		return this.planQty;
	}

}