/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-01-15 13:58:03
* CreateBy   : wswcx
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesDealerDisratePO extends PO{
	//经销商ID
	private Long dealerId;
	//折扣ID
	private Long disrateId;
	//数据更新时间
	private Date updateDate;
	//折扣额度
	private Double disAmount;
	//更新人
	private Long updatePer;
	//数据创建时间
	private Date createDate;
	//创建人
	private Long createPer;

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setDisrateId(Long disrateId){
		this.disrateId=disrateId;
	}

	public Long getDisrateId(){
		return this.disrateId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setDisAmount(Double disAmount){
		this.disAmount=disAmount;
	}

	public Double getDisAmount(){
		return this.disAmount;
	}

	public void setUpdatePer(Long updatePer){
		this.updatePer=updatePer;
	}

	public Long getUpdatePer(){
		return this.updatePer;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCreatePer(Long createPer){
		this.createPer=createPer;
	}

	public Long getCreatePer(){
		return this.createPer;
	}

}