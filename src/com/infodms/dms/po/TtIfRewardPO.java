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
public class TtIfRewardPO extends PO{

	private Date updateDate;
	private Long createBy;
	private String orderId;
	private Integer rewardMode;
	private Integer rewardStatus;
	private Date createDate;
	private Integer rewardType;
	private Date rewardDate;
	private Long dealerId;
	private Double rewardMoney;
	private String linkMan;
	private Long updateBy;
	private String rewardContent;
	private String tel;
	private Integer isDel;
	private String dealerCode;
	private Long id;
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

	public void setRewardMode(Integer rewardMode){
		this.rewardMode=rewardMode;
	}

	public Integer getRewardMode(){
		return this.rewardMode;
	}

	public void setRewardStatus(Integer rewardStatus){
		this.rewardStatus=rewardStatus;
	}

	public Integer getRewardStatus(){
		return this.rewardStatus;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setRewardType(Integer rewardType){
		this.rewardType=rewardType;
	}

	public Integer getRewardType(){
		return this.rewardType;
	}

	public void setRewardDate(Date rewardDate){
		this.rewardDate=rewardDate;
	}

	public Date getRewardDate(){
		return this.rewardDate;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setRewardMoney(Double rewardMoney){
		this.rewardMoney=rewardMoney;
	}

	public Double getRewardMoney(){
		return this.rewardMoney;
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

	public void setRewardContent(String rewardContent){
		this.rewardContent=rewardContent;
	}

	public String getRewardContent(){
		return this.rewardContent;
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

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

}