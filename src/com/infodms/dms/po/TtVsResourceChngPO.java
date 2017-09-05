/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-12 09:06:51
* CreateBy   : 
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsResourceChngPO extends PO{

	private Date chngDate;
	private Long orderDetailId;
	private Date updateDate;
	private Integer chngType;
	private Long chngUserId;
	private Long createBy;
	private Long chngOrgId;
	private Long chngPositionId;
	private Long resourceId;
	private Long chngId;
	private Long orderId;
	private Long updateBy;
	private Integer chngAmount;
	private Date createDate;

	public void setChngDate(Date chngDate){
		this.chngDate=chngDate;
	}

	public Date getChngDate(){
		return this.chngDate;
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

	public void setChngType(Integer chngType){
		this.chngType=chngType;
	}

	public Integer getChngType(){
		return this.chngType;
	}

	public void setChngUserId(Long chngUserId){
		this.chngUserId=chngUserId;
	}

	public Long getChngUserId(){
		return this.chngUserId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setChngOrgId(Long chngOrgId){
		this.chngOrgId=chngOrgId;
	}

	public Long getChngOrgId(){
		return this.chngOrgId;
	}

	public void setChngPositionId(Long chngPositionId){
		this.chngPositionId=chngPositionId;
	}

	public Long getChngPositionId(){
		return this.chngPositionId;
	}

	public void setResourceId(Long resourceId){
		this.resourceId=resourceId;
	}

	public Long getResourceId(){
		return this.resourceId;
	}

	public void setChngId(Long chngId){
		this.chngId=chngId;
	}

	public Long getChngId(){
		return this.chngId;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setChngAmount(Integer chngAmount){
		this.chngAmount=chngAmount;
	}

	public Integer getChngAmount(){
		return this.chngAmount;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}