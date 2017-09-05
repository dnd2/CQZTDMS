/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-05-26 09:52:16
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtIfWrActivityPO extends PO{

	private Long id;
	private Date updateDate;
	private Long createBy;
	private String fax;
	private String orderId;
	private Integer actStatus;
	private Date createDate;
	private Double actMoney;
	private Long dealerId;
	private String linkMan;
	private Integer actType;
	private String actContent;
	private Date actDate;
	private Long updateBy;
	private String actName;
	private String tel;
	private Integer isDel;
	private Long companyId;
	
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void setFax(String fax){
		this.fax=fax;
	}

	public String getFax(){
		return this.fax;
	}

	public void setOrderId(String orderId){
		this.orderId=orderId;
	}

	public String getOrderId(){
		return this.orderId;
	}

	public void setActStatus(Integer actStatus){
		this.actStatus=actStatus;
	}

	public Integer getActStatus(){
		return this.actStatus;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setActMoney(Double actMoney){
		this.actMoney=actMoney;
	}

	public Double getActMoney(){
		return this.actMoney;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}

	public String getLinkMan(){
		return this.linkMan;
	}

	public void setActType(Integer actType){
		this.actType=actType;
	}

	public Integer getActType(){
		return this.actType;
	}

	public void setActContent(String actContent){
		this.actContent=actContent;
	}

	public String getActContent(){
		return this.actContent;
	}

	public void setActDate(Date actDate){
		this.actDate=actDate;
	}

	public Date getActDate(){
		return this.actDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setActName(String actName){
		this.actName=actName;
	}

	public String getActName(){
		return this.actName;
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