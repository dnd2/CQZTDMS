/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-05-24 14:58:37
* CreateBy   : wanginbao
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtIfExchangePO extends PO{

	private String exContent;
	private Integer exStatus;
	private Date updateDate;
	private Long createBy;
	private String orderId;
	private Date createDate;
	private Long dealerId;
	private String linkManager;
	private String linkMan;
	private Date exDate;
	private Integer exType;
	private Long updateBy;
	private String vin;
	private String tel;
	private Integer isDel;
	private Long companyId;
	private String problemDescribe;
	private String userRequest;
	private String adviceDealMode;
	private String costDetail;
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProblemDescribe() {
		return problemDescribe;
	}

	public void setProblemDescribe(String problemDescribe) {
		this.problemDescribe = problemDescribe;
	}

	public String getUserRequest() {
		return userRequest;
	}

	public void setUserRequest(String userRequest) {
		this.userRequest = userRequest;
	}

	public String getAdviceDealMode() {
		return adviceDealMode;
	}

	public void setAdviceDealMode(String adviceDealMode) {
		this.adviceDealMode = adviceDealMode;
	}

	public String getCostDetail() {
		return costDetail;
	}

	public void setCostDetail(String costDetail) {
		this.costDetail = costDetail;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public void setExContent(String exContent){
		this.exContent=exContent;
	}

	public String getExContent(){
		return this.exContent;
	}

	public void setExStatus(Integer exStatus){
		this.exStatus=exStatus;
	}

	public Integer getExStatus(){
		return this.exStatus;
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

	public void setOrderId(String orderId){
		this.orderId=orderId;
	}

	public String getOrderId(){
		return this.orderId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setLinkManager(String linkManager){
		this.linkManager=linkManager;
	}

	public String getLinkManager(){
		return this.linkManager;
	}

	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}

	public String getLinkMan(){
		return this.linkMan;
	}

	public void setExDate(Date exDate){
		this.exDate=exDate;
	}

	public Date getExDate(){
		return this.exDate;
	}

	public void setExType(Integer exType){
		this.exType=exType;
	}

	public Integer getExType(){
		return this.exType;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setTel(String tel){
		this.tel=tel;
	}

	public String getTel(){
		return this.tel;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

}