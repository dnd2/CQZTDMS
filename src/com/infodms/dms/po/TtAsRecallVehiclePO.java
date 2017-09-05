/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-08 16:18:07
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsRecallVehiclePO extends PO{

	private String customerName;
	private Date buyDate;
	private String linkmanOfficePhone;
	private String postalcode;
	private String province;
	private String lincenseTag;
	private Long activityId;
	private String linkmanZoneNum;
	private Integer saleStatus;
	private Integer carStatus;
	private String email;
	private String dealerName;
	private Integer repairStatus;
	private String memo;
	private String linkmanMobile;
	private String linkmanFamilyPhone;
	private String dealerCode;
	private String linkman;
	private String area;
	private String vin;
	private String customerAddress;
	private String town;
	private Long id;
	private Long dealerId;
	private Long flag;
	private String errorRemark;
	public void setCustomerName(String customerName){
		this.customerName=customerName;
	}

	public String getCustomerName(){
		return this.customerName;
	}

	public void setBuyDate(Date buyDate){
		this.buyDate=buyDate;
	}

	public Date getBuyDate(){
		return this.buyDate;
	}

	public void setLinkmanOfficePhone(String linkmanOfficePhone){
		this.linkmanOfficePhone=linkmanOfficePhone;
	}

	public String getLinkmanOfficePhone(){
		return this.linkmanOfficePhone;
	}

	public void setPostalcode(String postalcode){
		this.postalcode=postalcode;
	}

	public String getPostalcode(){
		return this.postalcode;
	}

	public void setProvince(String province){
		this.province=province;
	}

	public String getProvince(){
		return this.province;
	}

	public void setLincenseTag(String lincenseTag){
		this.lincenseTag=lincenseTag;
	}

	public String getLincenseTag(){
		return this.lincenseTag;
	}

	public void setActivityId(Long activityId){
		this.activityId=activityId;
	}

	public Long getActivityId(){
		return this.activityId;
	}

	public void setLinkmanZoneNum(String linkmanZoneNum){
		this.linkmanZoneNum=linkmanZoneNum;
	}

	public String getLinkmanZoneNum(){
		return this.linkmanZoneNum;
	}

	public void setSaleStatus(Integer saleStatus){
		this.saleStatus=saleStatus;
	}

	public Integer getSaleStatus(){
		return this.saleStatus;
	}

	public void setCarStatus(Integer carStatus){
		this.carStatus=carStatus;
	}

	public Integer getCarStatus(){
		return this.carStatus;
	}

	public void setEmail(String email){
		this.email=email;
	}

	public String getEmail(){
		return this.email;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setRepairStatus(Integer repairStatus){
		this.repairStatus=repairStatus;
	}

	public Integer getRepairStatus(){
		return this.repairStatus;
	}

	public void setMemo(String memo){
		this.memo=memo;
	}

	public String getMemo(){
		return this.memo;
	}

	public void setLinkmanMobile(String linkmanMobile){
		this.linkmanMobile=linkmanMobile;
	}

	public String getLinkmanMobile(){
		return this.linkmanMobile;
	}

	public void setLinkmanFamilyPhone(String linkmanFamilyPhone){
		this.linkmanFamilyPhone=linkmanFamilyPhone;
	}

	public String getLinkmanFamilyPhone(){
		return this.linkmanFamilyPhone;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setLinkman(String linkman){
		this.linkman=linkman;
	}

	public String getLinkman(){
		return this.linkman;
	}

	public void setArea(String area){
		this.area=area;
	}

	public String getArea(){
		return this.area;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setCustomerAddress(String customerAddress){
		this.customerAddress=customerAddress;
	}

	public String getCustomerAddress(){
		return this.customerAddress;
	}

	public void setTown(String town){
		this.town=town;
	}

	public String getTown(){
		return this.town;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public Long getFlag() {
		return flag;
	}

	public void setFlag(Long flag) {
		this.flag = flag;
	}

	public String getErrorRemark() {
		return errorRemark;
	}

	public void setErrorRemark(String errorRemark) {
		this.errorRemark = errorRemark;
	}

}