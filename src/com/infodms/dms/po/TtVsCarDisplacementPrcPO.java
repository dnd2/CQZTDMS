/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-05-20 11:59:57
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsCarDisplacementPrcPO extends PO{

	private Long displacementPriceId;
	private Date updateDate;
	private Long updateBy;
	private String displacementType;
	private Long createBy;
	private Double price;
	private String displacementPrc;
	private Date createDate;
	private Long dealerId;

	public void setDisplacementPriceId(Long displacementPriceId){
		this.displacementPriceId=displacementPriceId;
	}

	public Long getDisplacementPriceId(){
		return this.displacementPriceId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDisplacementType(String displacementType){
		this.displacementType=displacementType;
	}

	public String getDisplacementType(){
		return this.displacementType;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
	}

	public void setDisplacementPrc(String displacementPrc){
		this.displacementPrc=displacementPrc;
	}

	public String getDisplacementPrc(){
		return this.displacementPrc;
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

}