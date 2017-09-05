/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-13 15:07:48
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmOldpartReturnApplyPO extends PO{

	private Date sendDate;
	private Long reportUser;
	private Long dealerId;
	private String sendLinkPhone;
	private Date updateDate;
	private String sendLinkUser;
	private String sendNo;
	private Long createBy;
	private Long checkUser;
	private Date checkDate;
	private Integer status;
	private String returnApplyNo;
	private Long updateBy;
	private Long sendUser;
	private Date createDate;
	private Date reportDate;
	private Long returnApplyId;

	public void setSendDate(Date sendDate){
		this.sendDate=sendDate;
	}

	public Date getSendDate(){
		return this.sendDate;
	}

	public void setReportUser(Long reportUser){
		this.reportUser=reportUser;
	}

	public Long getReportUser(){
		return this.reportUser;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setSendLinkPhone(String sendLinkPhone){
		this.sendLinkPhone=sendLinkPhone;
	}

	public String getSendLinkPhone(){
		return this.sendLinkPhone;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}


	public String getSendLinkUser() {
		return sendLinkUser;
	}

	public void setSendLinkUser(String sendLinkUser) {
		this.sendLinkUser = sendLinkUser;
	}

	public void setSendNo(String sendNo){
		this.sendNo=sendNo;
	}

	public String getSendNo(){
		return this.sendNo;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCheckUser(Long checkUser){
		this.checkUser=checkUser;
	}

	public Long getCheckUser(){
		return this.checkUser;
	}

	public void setCheckDate(Date checkDate){
		this.checkDate=checkDate;
	}

	public Date getCheckDate(){
		return this.checkDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}


	public void setReturnApplyNo(String returnApplyNo){
		this.returnApplyNo=returnApplyNo;
	}

	public String getReturnApplyNo(){
		return this.returnApplyNo;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setSendUser(Long sendUser){
		this.sendUser=sendUser;
	}

	public Long getSendUser(){
		return this.sendUser;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setReportDate(Date reportDate){
		this.reportDate=reportDate;
	}

	public Date getReportDate(){
		return this.reportDate;
	}

	public void setReturnApplyId(Long returnApplyId){
		this.returnApplyId=returnApplyId;
	}

	public Long getReturnApplyId(){
		return this.returnApplyId;
	}

}