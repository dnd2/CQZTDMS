/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-04-29 16:02:16
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesAssignPO extends PO{

	private Long addressId;
	private Integer accNum;
	private Integer orderNum;
	private Long dealerId;
	private String recShortdealerName;
	private Date updateDate;
	private Long createBy;
	private String assRemark;
	private Long areaId;
	private Integer status;
	private Date finChkDate;
	private Long updateBy;
	private String tel;
	private Integer sendNum;
	private Date assDate;
	private Integer assNum;
	private Date raiseDate;
	private Date planChkDate;
	private Long assId;
	private Long recDealerId;
	private String invoiceNo;
	private String assNo;
	private String orderNo;
	private String isRetail;
	private Integer outNum;
	private String shortdealerName;
	private Integer boardNum;
	private String linkMan;
	private Long logiId;
	private String createType;
	private String dealerName;
	private Long orderId;
	private Integer boStatus;
	private Integer statusTracking;
	private String recDealerName;
	private String invoiceNoVer;
	private Date createDate;
	private Long assPer;
	private Integer allocaNum;
	private String orderRemark;
	
	private Long wrOrderId;//需求订单ID	
	private Date planDate;//计划装车时间
	private String asRemark;//说明
	private Long orderType;
	private Long accType;
	private Long sendType;
	
	public Long getOrderType() {
		return orderType;
	}

	public void setOrderType(Long orderType) {
		this.orderType = orderType;
	}

	public Long getAccType() {
		return accType;
	}

	public void setAccType(Long accType) {
		this.accType = accType;
	}

	public Long getSendType() {
		return sendType;
	}

	public void setSendType(Long sendType) {
		this.sendType = sendType;
	}

	public Long getWrOrderId() {
		return wrOrderId;
	}

	public void setWrOrderId(Long wrOrderId) {
		this.wrOrderId = wrOrderId;
	}

	public Date getPlanDate() {
		return planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	public String getAsRemark() {
		return asRemark;
	}

	public void setAsRemark(String asRemark) {
		this.asRemark = asRemark;
	}

	public String getOrderRemark() {
		return orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public void setAddressId(Long addressId){
		this.addressId=addressId;
	}

	public Long getAddressId(){
		return this.addressId;
	}

	public void setAccNum(Integer accNum){
		this.accNum=accNum;
	}

	public Integer getAccNum(){
		return this.accNum;
	}

	public void setOrderNum(Integer orderNum){
		this.orderNum=orderNum;
	}

	public Integer getOrderNum(){
		return this.orderNum;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setRecShortdealerName(String recShortdealerName){
		this.recShortdealerName=recShortdealerName;
	}

	public String getRecShortdealerName(){
		return this.recShortdealerName;
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

	public void setAssRemark(String assRemark){
		this.assRemark=assRemark;
	}

	public String getAssRemark(){
		return this.assRemark;
	}

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setFinChkDate(Date finChkDate){
		this.finChkDate=finChkDate;
	}

	public Date getFinChkDate(){
		return this.finChkDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setTel(String tel){
		this.tel=tel;
	}

	public String getTel(){
		return this.tel;
	}

	public void setSendNum(Integer sendNum){
		this.sendNum=sendNum;
	}

	public Integer getSendNum(){
		return this.sendNum;
	}

	public void setAssDate(Date assDate){
		this.assDate=assDate;
	}

	public Date getAssDate(){
		return this.assDate;
	}

	public void setAssNum(Integer assNum){
		this.assNum=assNum;
	}

	public Integer getAssNum(){
		return this.assNum;
	}

	public void setRaiseDate(Date raiseDate){
		this.raiseDate=raiseDate;
	}

	public Date getRaiseDate(){
		return this.raiseDate;
	}

	public void setPlanChkDate(Date planChkDate){
		this.planChkDate=planChkDate;
	}

	public Date getPlanChkDate(){
		return this.planChkDate;
	}

	public void setAssId(Long assId){
		this.assId=assId;
	}

	public Long getAssId(){
		return this.assId;
	}

	public void setRecDealerId(Long recDealerId){
		this.recDealerId=recDealerId;
	}

	public Long getRecDealerId(){
		return this.recDealerId;
	}

	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}

	public String getInvoiceNo(){
		return this.invoiceNo;
	}

	public void setAssNo(String assNo){
		this.assNo=assNo;
	}

	public String getAssNo(){
		return this.assNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setIsRetail(String isRetail){
		this.isRetail=isRetail;
	}

	public String getIsRetail(){
		return this.isRetail;
	}

	public void setOutNum(Integer outNum){
		this.outNum=outNum;
	}

	public Integer getOutNum(){
		return this.outNum;
	}

	public void setShortdealerName(String shortdealerName){
		this.shortdealerName=shortdealerName;
	}

	public String getShortdealerName(){
		return this.shortdealerName;
	}

	public void setBoardNum(Integer boardNum){
		this.boardNum=boardNum;
	}

	public Integer getBoardNum(){
		return this.boardNum;
	}

	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}

	public String getLinkMan(){
		return this.linkMan;
	}

	public void setLogiId(Long logiId){
		this.logiId=logiId;
	}

	public Long getLogiId(){
		return this.logiId;
	}

	public void setCreateType(String createType){
		this.createType=createType;
	}

	public String getCreateType(){
		return this.createType;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setBoStatus(Integer boStatus){
		this.boStatus=boStatus;
	}

	public Integer getBoStatus(){
		return this.boStatus;
	}

	public void setStatusTracking(Integer statusTracking){
		this.statusTracking=statusTracking;
	}

	public Integer getStatusTracking(){
		return this.statusTracking;
	}

	public void setRecDealerName(String recDealerName){
		this.recDealerName=recDealerName;
	}

	public String getRecDealerName(){
		return this.recDealerName;
	}

	public void setInvoiceNoVer(String invoiceNoVer){
		this.invoiceNoVer=invoiceNoVer;
	}

	public String getInvoiceNoVer(){
		return this.invoiceNoVer;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAssPer(Long assPer){
		this.assPer=assPer;
	}

	public Long getAssPer(){
		return this.assPer;
	}

	public void setAllocaNum(Integer allocaNum){
		this.allocaNum=allocaNum;
	}

	public Integer getAllocaNum(){
		return this.allocaNum;
	}

}