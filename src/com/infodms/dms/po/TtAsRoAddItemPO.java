/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-20 22:09:11
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsRoAddItemPO extends PO{

	private Double addItemAmount;
	private String manageSortCode;
	private Date updateDate;
	private Integer isSel;
	private Long createBy;
	private Long roId;
	private Integer isClaim;
	private Double discount;
	private String chargePartitionCode;
	private Date createDate;
	private Integer payType;
	private String activityCode;
	private String addItemCode;
	private Long updateBy;
	private String addItemName;
	private String remark;
	private Long id;
	private String mainPartCode;

	public String getMainPartCode() {
		return mainPartCode;
	}

	public void setMainPartCode(String mainPartCode) {
		this.mainPartCode = mainPartCode;
	}

	public void setAddItemAmount(Double addItemAmount){
		this.addItemAmount=addItemAmount;
	}

	public Double getAddItemAmount(){
		return this.addItemAmount;
	}

	public void setManageSortCode(String manageSortCode){
		this.manageSortCode=manageSortCode;
	}

	public String getManageSortCode(){
		return this.manageSortCode;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setIsSel(Integer isSel){
		this.isSel=isSel;
	}

	public Integer getIsSel(){
		return this.isSel;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRoId(Long roId){
		this.roId=roId;
	}

	public Long getRoId(){
		return this.roId;
	}

	public void setIsClaim(Integer isClaim){
		this.isClaim=isClaim;
	}

	public Integer getIsClaim(){
		return this.isClaim;
	}

	public void setDiscount(Double discount){
		this.discount=discount;
	}

	public Double getDiscount(){
		return this.discount;
	}

	public void setChargePartitionCode(String chargePartitionCode){
		this.chargePartitionCode=chargePartitionCode;
	}

	public String getChargePartitionCode(){
		return this.chargePartitionCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPayType(Integer payType){
		this.payType=payType;
	}

	public Integer getPayType(){
		return this.payType;
	}

	public void setActivityCode(String activityCode){
		this.activityCode=activityCode;
	}

	public String getActivityCode(){
		return this.activityCode;
	}

	public void setAddItemCode(String addItemCode){
		this.addItemCode=addItemCode;
	}

	public String getAddItemCode(){
		return this.addItemCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setAddItemName(String addItemName){
		this.addItemName=addItemName;
	}

	public String getAddItemName(){
		return this.addItemName;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}