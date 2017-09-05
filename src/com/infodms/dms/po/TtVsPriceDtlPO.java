/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-09-29 13:47:30
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsPriceDtlPO extends PO{

	private Long detailId;  
	private Long priceId; 
	private Double  salesPrice; 
	private Double  noTaxPrice; 
	private Long createBy;  
	private Date  createDate;
	private Long  updateBy;  
	private Date  updateDate ;	
	private Long  groupId;
	
	public void setDetailId(Long detailId){
		this.detailId = detailId;
	}
	
	public Long getDetailId(){
		return detailId;
	}
	
	public void setPriceId(Long priceId){
		this.priceId = priceId;
	}
	
	public Long getPriceId(){
		return priceId;
	}
	
	public void setSalesPrice(Double salesPrice){
		this.salesPrice = salesPrice;
	}
	
	public Double getSalesPrice(){
		return salesPrice;
	}
	
	public void setNoTaxPrice(Double noTaxPrice){
		this.noTaxPrice = noTaxPrice;
	}
	
	public Double getNoTaxPrice(){
		return noTaxPrice;
	}
	
	public void setCreateBy(Long createBy){
		this.createBy = createBy;
	}
	
	public Long getCreateBy(){
		return createBy;
	}
	
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	
	public Date getCreateDate(){
		return createDate;
	}
	
	public void setUpdateBy(Long updateBy){
		this.updateBy = updateBy;
	}
	
	public Long getUpdateBy(){
		return updateBy;
	}
	
	
	public void setUpdateDate(Date updateDate){
		this.updateDate = updateDate;
	}
	
	public Date getUpdateDate(){
		return updateDate;
	}
	
	public void setGroupId(Long groupId){
		this.groupId = groupId;
	}
	
	public Long getGroupId(){
		return groupId;
	}
}