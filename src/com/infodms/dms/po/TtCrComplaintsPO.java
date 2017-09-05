/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-01 09:20:00
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrComplaintsPO extends PO{

	private Date purchasedDate;
	private Long createBy;
	private String eMail;
	private Integer province;
	private Integer comType;
	private Integer compType;
	private Integer district;
	private Date createDate;
	private Long compDealer;
	private Long dealerId;
	private String licenseNo;
	private Integer sex;
	private Long compId;
	private Integer age;
	private Integer status;
	private String engineNo;
	private Integer intStatus;
	private String vin;
	private Integer compSource;
	private Integer isDel;
	private Date birthday;
	private Date updateDate;
	private String compContent;
	private Integer compLevel;
	private Long orgId;
	private String compCode;
	private String zipCode;
	private String ownOrgId;
	private String linkMan;
	private Long updateBy;
	private Integer city;
	private String tel;
	private String modelCode;
	private String address;
	private Integer isReturn;
	private Integer ifStatus;
	private String callCenCon;
	private Date sendDate;

	public Integer getIfStatus() {
		return ifStatus;
	}

	public void setIfStatus(Integer ifStatus) {
		this.ifStatus = ifStatus;
	}

	public void setPurchasedDate(Date purchasedDate){
		this.purchasedDate=purchasedDate;
	}

	public Date getPurchasedDate(){
		return this.purchasedDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setEMail(String eMail){
		this.eMail=eMail;
	}

	public String getEMail(){
		return this.eMail;
	}

	public void setProvince(Integer province){
		this.province=province;
	}

	public Integer getProvince(){
		return this.province;
	}

	public Integer getComType() {
		return comType;
	}

	public void setComType(Integer comType) {
		this.comType = comType;
	}

	public void setCompType(Integer compType){
		this.compType=compType;
	}

	public Integer getCompType(){
		return this.compType;
	}

	public void setDistrict(Integer district){
		this.district=district;
	}

	public Integer getDistrict(){
		return this.district;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCompDealer(Long compDealer){
		this.compDealer=compDealer;
	}

	public Long getCompDealer(){
		return this.compDealer;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setLicenseNo(String licenseNo){
		this.licenseNo=licenseNo;
	}

	public String getLicenseNo(){
		return this.licenseNo;
	}

	public void setSex(Integer sex){
		this.sex=sex;
	}

	public Integer getSex(){
		return this.sex;
	}

	public void setCompId(Long compId){
		this.compId=compId;
	}

	public Long getCompId(){
		return this.compId;
	}

	public void setAge(Integer age){
		this.age=age;
	}

	public Integer getAge(){
		return this.age;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setEngineNo(String engineNo){
		this.engineNo=engineNo;
	}

	public String getEngineNo(){
		return this.engineNo;
	}

	public void setIntStatus(Integer intStatus){
		this.intStatus=intStatus;
	}

	public Integer getIntStatus(){
		return this.intStatus;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setCompSource(Integer compSource){
		this.compSource=compSource;
	}

	public Integer getCompSource(){
		return this.compSource;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setBirthday(Date birthday){
		this.birthday=birthday;
	}

	public Date getBirthday(){
		return this.birthday;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCompContent(String compContent){
		this.compContent=compContent;
	}

	public String getCompContent(){
		return this.compContent;
	}

	public void setCompLevel(Integer compLevel){
		this.compLevel=compLevel;
	}

	public Integer getCompLevel(){
		return this.compLevel;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setCompCode(String compCode){
		this.compCode=compCode;
	}

	public String getCompCode(){
		return this.compCode;
	}

	public void setZipCode(String zipCode){
		this.zipCode=zipCode;
	}

	public String getZipCode(){
		return this.zipCode;
	}

	public void setOwnOrgId(String ownOrgId){
		this.ownOrgId=ownOrgId;
	}

	public String getOwnOrgId(){
		return this.ownOrgId;
	}

	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}

	public String getLinkMan(){
		return this.linkMan;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCity(Integer city){
		this.city=city;
	}

	public Integer getCity(){
		return this.city;
	}

	public void setTel(String tel){
		this.tel=tel;
	}

	public String getTel(){
		return this.tel;
	}

	public void setModelCode(String modelCode){
		this.modelCode=modelCode;
	}

	public String getModelCode(){
		return this.modelCode;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getAddress(){
		return this.address;
	}

	public Integer getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Integer isReturn) {
		this.isReturn = isReturn;
	}

	public String getCallCenCon() {
		return callCenCon;
	}

	public void setCallCenCon(String callCenCon) {
		this.callCenCon = callCenCon;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	
}