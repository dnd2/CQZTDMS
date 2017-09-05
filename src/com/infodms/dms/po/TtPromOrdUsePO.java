/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-06-24 19:49:23
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPromOrdUsePO extends PO{

	private Long useId;
	private Long orderType;
	private Long ordDetId;
	private Date updateDate;
	private Long createBy;
	private Long detailId;
	private Double useAmount;
	private Integer status;
	private Long useBy;
	private Long updateBy;
	private Date useDate;
	private Date createDate;
	private Integer useNum;

	public void setUseId(Long useId){
		this.useId=useId;
	}

	public Long getUseId(){
		return this.useId;
	}

	public void setOrderType(Long orderType){
		this.orderType=orderType;
	}

	public Long getOrderType(){
		return this.orderType;
	}

	public void setOrdDetId(Long ordDetId){
		this.ordDetId=ordDetId;
	}

	public Long getOrdDetId(){
		return this.ordDetId;
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

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setUseAmount(Double useAmount){
		this.useAmount=useAmount;
	}

	public Double getUseAmount(){
		return this.useAmount;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setUseBy(Long useBy){
		this.useBy=useBy;
	}

	public Long getUseBy(){
		return this.useBy;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setUseDate(Date useDate){
		this.useDate=useDate;
	}

	public Date getUseDate(){
		return this.useDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setUseNum(Integer useNum){
		this.useNum=useNum;
	}

	public Integer getUseNum(){
		return this.useNum;
	}

}