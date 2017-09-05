/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-13 14:51:06
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcDecorationPO extends PO{

	private Long ctmId;
	private String giveorbuy;
	private Date updateDate;
	private String exname;
	private Long createBy;
	private Date createDate;
	private Long decorationId;
	private Long amount;
	private Long status;
	private Long updateBy;
	private Double price;
	private Double money;
	private String exproject;

	public void setCtmId(Long ctmId){
		this.ctmId=ctmId;
	}

	public Long getCtmId(){
		return this.ctmId;
	}

	public void setGiveorbuy(String giveorbuy){
		this.giveorbuy=giveorbuy;
	}

	public String getGiveorbuy(){
		return this.giveorbuy;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setExname(String exname){
		this.exname=exname;
	}

	public String getExname(){
		return this.exname;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDecorationId(Long decorationId){
		this.decorationId=decorationId;
	}

	public Long getDecorationId(){
		return this.decorationId;
	}

	public void setAmount(Long amount){
		this.amount=amount;
	}

	public Long getAmount(){
		return this.amount;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public void setExproject(String exproject){
		this.exproject=exproject;
	}

	public String getExproject(){
		return this.exproject;
	}

}