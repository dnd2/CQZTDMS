/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-05-16 16:16:11
* CreateBy   : Iverson
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrAuthPricePO extends PO{

	private Long claimbalanceId;
	private Long dealerId;
	private String dealerCode;
	private Date updateDate;
	private Long yielyld;
	private Long createBy;
	private Integer status;
	private Double newPrice;
	private Long returnId;
	private String dealerName;
	private Long updateBy;
	private Long id;
	private Date createDate;
	private Double oldPrice;

	public void setClaimbalanceId(Long claimbalanceId){
		this.claimbalanceId=claimbalanceId;
	}

	public Long getClaimbalanceId(){
		return this.claimbalanceId;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setYielyld(Long yielyld){
		this.yielyld=yielyld;
	}

	public Long getYielyld(){
		return this.yielyld;
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

	public void setNewPrice(Double newPrice){
		this.newPrice=newPrice;
	}

	public Double getNewPrice(){
		return this.newPrice;
	}

	public void setReturnId(Long returnId){
		this.returnId=returnId;
	}

	public Long getReturnId(){
		return this.returnId;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
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

	public void setOldPrice(Double oldPrice){
		this.oldPrice=oldPrice;
	}

	public Double getOldPrice(){
		return this.oldPrice;
	}

}