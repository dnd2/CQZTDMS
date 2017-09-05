/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-10-15 09:55:55
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrPriceAdjustPO extends PO{

	private Integer adjustMode;
	private Date updateDate;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private Integer typeAdjust;
	private Double adjustPrice;
	private Long id;

	public void setAdjustMode(Integer adjustMode){
		this.adjustMode=adjustMode;
	}

	public Integer getAdjustMode(){
		return this.adjustMode;
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

	public void setTypeAdjust(Integer typeAdjust){
		this.typeAdjust=typeAdjust;
	}

	public Integer getTypeAdjust(){
		return this.typeAdjust;
	}

	public void setAdjustPrice(Double adjustPrice){
		this.adjustPrice=adjustPrice;
	}

	public Double getAdjustPrice(){
		return this.adjustPrice;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}