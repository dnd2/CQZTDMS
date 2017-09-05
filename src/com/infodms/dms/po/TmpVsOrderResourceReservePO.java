/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-06-03 08:46:08
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpVsOrderResourceReservePO extends PO{

	private Integer reserveStatus;
	private Long materialId;
	private Long oemCompanyId;
	private Long updateBy;
	private Long reserveId;
	private Long orderDetailId;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private Integer amount;
	private Long orgId;

	public void setReserveStatus(Integer reserveStatus){
		this.reserveStatus=reserveStatus;
	}

	public Integer getReserveStatus(){
		return this.reserveStatus;
	}

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setReserveId(Long reserveId){
		this.reserveId=reserveId;
	}

	public Long getReserveId(){
		return this.reserveId;
	}

	public void setOrderDetailId(Long orderDetailId){
		this.orderDetailId=orderDetailId;
	}

	public Long getOrderDetailId(){
		return this.orderDetailId;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAmount(Integer amount){
		this.amount=amount;
	}

	public Integer getAmount(){
		return this.amount;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

}