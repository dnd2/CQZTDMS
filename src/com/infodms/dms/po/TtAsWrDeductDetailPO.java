/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-28 15:18:54
* CreateBy   : ZLD
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrDeductDetailPO extends PO{

	private Double deductMoney;
	private Integer deductReason;
	private Integer itemType;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Long updateBy;
	private Long claimId;
	private String itemName;
	private Long id;
	private Long deductId;
	private String itemCode;
	private Date createDate;
	
	private Long partId;

	public Long getPartId() {
		return partId;
	}

	public void setPartId(Long partId) {
		this.partId = partId;
	}

	public void setDeductMoney(Double deductMoney){
		this.deductMoney=deductMoney;
	}

	public Double getDeductMoney(){
		return this.deductMoney;
	}

	public void setDeductReason(Integer deductReason){
		this.deductReason=deductReason;
	}

	public Integer getDeductReason(){
		return this.deductReason;
	}

	public void setItemType(Integer itemType){
		this.itemType=itemType;
	}

	public Integer getItemType(){
		return this.itemType;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setClaimId(Long claimId){
		this.claimId=claimId;
	}

	public Long getClaimId(){
		return this.claimId;
	}

	public void setItemName(String itemName){
		this.itemName=itemName;
	}

	public String getItemName(){
		return this.itemName;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setDeductId(Long deductId){
		this.deductId=deductId;
	}

	public Long getDeductId(){
		return this.deductId;
	}

	public void setItemCode(String itemCode){
		this.itemCode=itemCode;
	}

	public String getItemCode(){
		return this.itemCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}