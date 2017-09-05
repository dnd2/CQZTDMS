/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-10-11 23:51:45
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartPickOrderMainPO extends PO{

	private String transOrg;
	private Integer state;
	private Long pickId;
	private String dealerCode;
	private Integer pkgNum;
	private String remark;
	private String pickOrderId;
	private String logisticsNo;
	private Long whId;
	private String dealerName;
	private Date transDate;
	private String pkgBy;
	private String weight;
	private Integer isCheck;
	private Long sellerId;
	private String createByName;
	private String checkPickBy;
	private Date createDate;
	private String orderTransType;

	public void setOrderTransType(String orderTransType){
		this.orderTransType=orderTransType;
	}

	public String getOrderTransType(){
		return this.orderTransType;
	}
	
	public void setTransOrg(String transOrg){
		this.transOrg=transOrg;
	}

	public String getTransOrg(){
		return this.transOrg;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setPickId(Long pickId){
		this.pickId=pickId;
	}

	public Long getPickId(){
		return this.pickId;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setPkgNum(Integer pkgNum){
		this.pkgNum=pkgNum;
	}

	public Integer getPkgNum(){
		return this.pkgNum;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setPickOrderId(String pickOrderId){
		this.pickOrderId=pickOrderId;
	}

	public String getPickOrderId(){
		return this.pickOrderId;
	}

	public void setLogisticsNo(String logisticsNo){
		this.logisticsNo=logisticsNo;
	}

	public String getLogisticsNo(){
		return this.logisticsNo;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setTransDate(Date transDate){
		this.transDate=transDate;
	}

	public Date getTransDate(){
		return this.transDate;
	}

	public void setPkgBy(String pkgBy){
		this.pkgBy=pkgBy;
	}

	public String getPkgBy(){
		return this.pkgBy;
	}

	public void setWeight(String weight){
		this.weight=weight;
	}

	public String getWeight(){
		return this.weight;
	}

	public void setIsCheck(Integer isCheck){
		this.isCheck=isCheck;
	}

	public Integer getIsCheck(){
		return this.isCheck;
	}

	public void setSellerId(Long sellerId){
		this.sellerId=sellerId;
	}

	public Long getSellerId(){
		return this.sellerId;
	}

	public void setCreateByName(String createByName){
		this.createByName=createByName;
	}

	public String getCreateByName(){
		return this.createByName;
	}

	public void setCheckPickBy(String checkPickBy){
		this.checkPickBy=checkPickBy;
	}

	public String getCheckPickBy(){
		return this.checkPickBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}