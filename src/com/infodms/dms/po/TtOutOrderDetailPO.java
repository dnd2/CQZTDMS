/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-04 10:00:55
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtOutOrderDetailPO extends PO{

	private Double singlePrice;
	private Double chkPrice;
	private Integer outAmount;
	private Double discountSPrice;
	private Date updateDate;
	private Double disAmount;
	private Long createBy;
	private Long detailId;
	private Float discountRate;
	private Long materialId;
	private Long orderId;
	private Double discountPrice;
	private Long updateBy;
	private Double totalPrice;
	private Integer ver;
	private Date createDate;

	public void setSinglePrice(Double singlePrice){
		this.singlePrice=singlePrice;
	}

	public Double getSinglePrice(){
		return this.singlePrice;
	}

	public void setChkPrice(Double chkPrice){
		this.chkPrice=chkPrice;
	}

	public Double getChkPrice(){
		return this.chkPrice;
	}

	public void setOutAmount(Integer outAmount){
		this.outAmount=outAmount;
	}

	public Integer getOutAmount(){
		return this.outAmount;
	}

	public void setDiscountSPrice(Double discountSPrice){
		this.discountSPrice=discountSPrice;
	}

	public Double getDiscountSPrice(){
		return this.discountSPrice;
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

	public void setDiscountRate(Float discountRate){
		this.discountRate=discountRate;
	}

	public Float getDiscountRate(){
		return this.discountRate;
	}

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setDiscountPrice(Double discountPrice){
		this.discountPrice=discountPrice;
	}

	public Double getDiscountPrice(){
		return this.discountPrice;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setTotalPrice(Double totalPrice){
		this.totalPrice=totalPrice;
	}

	public Double getTotalPrice(){
		return this.totalPrice;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}