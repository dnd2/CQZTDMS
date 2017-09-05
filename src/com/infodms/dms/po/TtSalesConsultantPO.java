/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-06-20 19:38:35
* CreateBy   : fengalon
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesConsultantPO extends PO{

	private String name;
	private String reason;
	private Long dealerId;
	private Date updateDate;
	private Long createBy;
	private Integer status;
	private Date tradeYear;
	private Date bornYear;
	private Integer academicRecords;
	private Date chanaTradeYear;
	private Long updateBy;
	private String tel;
	private Integer sex;
	private Long id;
	private Date createDate;
	private String identityNumber;

	public void setName(String name){
		this.name=name;
	}

	public String getName(){
		return this.name;
	}

	public void setReason(String reason){
		this.reason=reason;
	}

	public String getReason(){
		return this.reason;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setTradeYear(Date tradeYear){
		this.tradeYear=tradeYear;
	}

	public Date getTradeYear(){
		return this.tradeYear;
	}

	public void setBornYear(Date bornYear){
		this.bornYear=bornYear;
	}

	public Date getBornYear(){
		return this.bornYear;
	}

	public void setAcademicRecords(Integer academicRecords){
		this.academicRecords=academicRecords;
	}

	public Integer getAcademicRecords(){
		return this.academicRecords;
	}

	public void setChanaTradeYear(Date chanaTradeYear){
		this.chanaTradeYear=chanaTradeYear;
	}

	public Date getChanaTradeYear(){
		return this.chanaTradeYear;
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

	public void setSex(Integer sex){
		this.sex=sex;
	}

	public Integer getSex(){
		return this.sex;
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

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

}