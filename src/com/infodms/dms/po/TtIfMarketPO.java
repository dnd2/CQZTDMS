/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-05-17 10:46:20
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtIfMarketPO extends PO{

	private Date updateDate;
	private Long createBy;
	private String orderId;
	private String compType;
	private Date createDate;
	private Long dealerId;
	private Date orderDate;
	private String linkMan;
	private Integer status;
	private Long updateBy;
	private Integer infoType;
	private String vin;
	private String content;
	private String tel;
	private Double money;
	private Integer isDel;
	private Long companyId;
	private String problemDescribe;
	private String userRequest;
	private String adviceDealMode;
	
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

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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

	public void setCompType(String compType){
		this.compType=compType;
	}

	public String getCompType(){
		return this.compType;
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

	public void setOrderDate(Date orderDate){
		this.orderDate=orderDate;
	}

	public Date getOrderDate(){
		return this.orderDate;
	}

	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}

	public String getLinkMan(){
		return this.linkMan;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setInfoType(Integer infoType){
		this.infoType=infoType;
	}

	public Integer getInfoType(){
		return this.infoType;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setContent(String content){
		this.content=content;
	}

	public String getContent(){
		return this.content;
	}

	public void setTel(String tel){
		this.tel=tel;
	}

	public String getTel(){
		return this.tel;
	}

	public void setMoney(Double money){
		this.money=money;
	}

	public Double getMoney(){
		return this.money;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

}