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
public class TtIfStandardPO extends PO{

	private Long id;
	private Date updateDate;
	private Long createBy;
	private String orderId;
	private Date createDate;
	private Long dealerId;
	private String zipCode;
	private String stContent;
	private String linkMan;
	private Integer stStatus;
	private Long updateBy;
	private String vin;
	private Integer stType;
	private Date stDate;
	private String tel;
	private Integer stAction;
	private String address;
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

	public void setZipCode(String zipCode){
		this.zipCode=zipCode;
	}

	public String getZipCode(){
		return this.zipCode;
	}

	public void setStContent(String stContent){
		this.stContent=stContent;
	}

	public String getStContent(){
		return this.stContent;
	}

	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}

	public String getLinkMan(){
		return this.linkMan;
	}

	public void setStStatus(Integer stStatus){
		this.stStatus=stStatus;
	}

	public Integer getStStatus(){
		return this.stStatus;
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

	public void setStType(Integer stType){
		this.stType=stType;
	}

	public Integer getStType(){
		return this.stType;
	}

	public void setStDate(Date stDate){
		this.stDate=stDate;
	}

	public Date getStDate(){
		return this.stDate;
	}

	public void setTel(String tel){
		this.tel=tel;
	}

	public String getTel(){
		return this.tel;
	}

	public void setStAction(Integer stAction){
		this.stAction=stAction;
	}

	public Integer getStAction(){
		return this.stAction;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getAddress(){
		return this.address;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

}