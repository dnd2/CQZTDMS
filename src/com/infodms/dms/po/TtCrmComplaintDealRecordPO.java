/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-10-21 12:39:53
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmComplaintDealRecordPO extends PO{

	private Integer cdIsDis;
	private Integer cdUserProcess;
	private Long cpId;
	private Long cdUserId;
	private Date updateDate;
	private Long createBy;
	private Long cdId;
	private Long cpNextDealId;
	private Long cpDealOrg;
	private String cpContent;
	private Long cpDealDealer;
	private Integer cpStatus;
	private Date cdDate;
	private Integer var;
	private String cdUser;
	private Long updateBy;
	private Date createDate;

	public void setCdIsDis(Integer cdIsDis){
		this.cdIsDis=cdIsDis;
	}

	public Integer getCdIsDis(){
		return this.cdIsDis;
	}

	public void setCdUserProcess(Integer cdUserProcess){
		this.cdUserProcess=cdUserProcess;
	}

	public Integer getCdUserProcess(){
		return this.cdUserProcess;
	}

	public void setCpId(Long cpId){
		this.cpId=cpId;
	}

	public Long getCpId(){
		return this.cpId;
	}

	public void setCdUserId(Long cdUserId){
		this.cdUserId=cdUserId;
	}

	public Long getCdUserId(){
		return this.cdUserId;
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

	public void setCdId(Long cdId){
		this.cdId=cdId;
	}

	public Long getCdId(){
		return this.cdId;
	}

	public void setCpNextDealId(Long cpNextDealId){
		this.cpNextDealId=cpNextDealId;
	}

	public Long getCpNextDealId(){
		return this.cpNextDealId;
	}

	public void setCpDealOrg(Long cpDealOrg){
		this.cpDealOrg=cpDealOrg;
	}

	public Long getCpDealOrg(){
		return this.cpDealOrg;
	}

	public void setCpContent(String cpContent){
		this.cpContent=cpContent;
	}

	public String getCpContent(){
		return this.cpContent;
	}

	public void setCpDealDealer(Long cpDealDealer){
		this.cpDealDealer=cpDealDealer;
	}

	public Long getCpDealDealer(){
		return this.cpDealDealer;
	}

	public void setCpStatus(Integer cpStatus){
		this.cpStatus=cpStatus;
	}

	public Integer getCpStatus(){
		return this.cpStatus;
	}

	public void setCdDate(Date cdDate){
		this.cdDate=cdDate;
	}

	public Date getCdDate(){
		return this.cdDate;
	}

	public void setVar(Integer var){
		this.var=var;
	}

	public Integer getVar(){
		return this.var;
	}

	public void setCdUser(String cdUser){
		this.cdUser=cdUser;
	}

	public String getCdUser(){
		return this.cdUser;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}