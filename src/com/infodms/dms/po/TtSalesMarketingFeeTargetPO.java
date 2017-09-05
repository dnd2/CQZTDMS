/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-01-16 10:12:16
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesMarketingFeeTargetPO extends PO{

	private Long targetId;
	private Long intentNumber;
	private Long updateBy;
	private Long actualSalesNumber;
	private Date updateDate;
	private Long createBy;
	private Long sdtlId;
	private Date createDate;
	private Long serviceNumber;
	private Long orderNumber;
	private Long partakeNumber;

	public void setTargetId(Long targetId){
		this.targetId=targetId;
	}

	public Long getTargetId(){
		return this.targetId;
	}

	public void setIntentNumber(Long intentNumber){
		this.intentNumber=intentNumber;
	}

	public Long getIntentNumber(){
		return this.intentNumber;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setActualSalesNumber(Long actualSalesNumber){
		this.actualSalesNumber=actualSalesNumber;
	}

	public Long getActualSalesNumber(){
		return this.actualSalesNumber;
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

	public void setSdtlId(Long sdtlId){
		this.sdtlId=sdtlId;
	}

	public Long getSdtlId(){
		return this.sdtlId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setServiceNumber(Long serviceNumber){
		this.serviceNumber=serviceNumber;
	}

	public Long getServiceNumber(){
		return this.serviceNumber;
	}

	public void setOrderNumber(Long orderNumber){
		this.orderNumber=orderNumber;
	}

	public Long getOrderNumber(){
		return this.orderNumber;
	}

	public void setPartakeNumber(Long partakeNumber){
		this.partakeNumber=partakeNumber;
	}

	public Long getPartakeNumber(){
		return this.partakeNumber;
	}

}