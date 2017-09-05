/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-09-26 12:04:37
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSaleFundsRoseDetailPO extends PO{

	private Integer isType;
	private Double balance;
	private Integer finType;
	private Double frozenAmount;
	private Long dealerId;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private String remark;
	private Long detailId;
	private String createDate;
	private Double amount;
	private Integer num;
	private Integer finInType;

	public void setIsType(Integer isType){
		this.isType=isType;
	}

	public Integer getIsType(){
		return this.isType;
	}

	public void setBalance(Double balance){
		this.balance=balance;
	}

	public Double getBalance(){
		return this.balance;
	}

	public void setFinType(Integer finType){
		this.finType=finType;
	}

	public Integer getFinType(){
		return this.finType;
	}

	public void setFrozenAmount(Double frozenAmount){
		this.frozenAmount=frozenAmount;
	}

	public Double getFrozenAmount(){
		return this.frozenAmount;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
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

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setCreateDate(String createDate){
		this.createDate=createDate;
	}

	public String getCreateDate(){
		return this.createDate;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setNum(Integer num){
		this.num=num;
	}

	public Integer getNum(){
		return this.num;
	}

	public Integer getFinInType() {
		return finInType;
	}

	public void setFinInType(Integer finInType) {
		this.finInType = finInType;
	}

}