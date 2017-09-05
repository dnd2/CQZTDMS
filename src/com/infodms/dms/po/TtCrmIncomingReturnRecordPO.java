/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-04-22 20:48:04
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmIncomingReturnRecordPO extends PO{

	private Long cpId;
	private Integer crConfirmOpinion;
	private String crUser;
	private Date updateDate;
	private Long createBy;
	private Long cpDealOrg;
	private Long crId;
	private Long cpDealDealer;
	private Integer cpStatus;
	private Integer var;
	private Long updateBy;
	private Long crUserId;
	private Date createDate;
	private String crContent;
	private Date crDate;

	public void setCpId(Long cpId){
		this.cpId=cpId;
	}

	public Long getCpId(){
		return this.cpId;
	}

	public void setCrConfirmOpinion(Integer crConfirmOpinion){
		this.crConfirmOpinion=crConfirmOpinion;
	}

	public Integer getCrConfirmOpinion(){
		return this.crConfirmOpinion;
	}

	public void setCrUser(String crUser){
		this.crUser=crUser;
	}

	public String getCrUser(){
		return this.crUser;
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

	public void setCpDealOrg(Long cpDealOrg){
		this.cpDealOrg=cpDealOrg;
	}

	public Long getCpDealOrg(){
		return this.cpDealOrg;
	}

	public void setCrId(Long crId){
		this.crId=crId;
	}

	public Long getCrId(){
		return this.crId;
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

	public void setVar(Integer var){
		this.var=var;
	}

	public Integer getVar(){
		return this.var;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCrUserId(Long crUserId){
		this.crUserId=crUserId;
	}

	public Long getCrUserId(){
		return this.crUserId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCrContent(String crContent){
		this.crContent=crContent;
	}

	public String getCrContent(){
		return this.crContent;
	}

	public void setCrDate(Date crDate){
		this.crDate=crDate;
	}

	public Date getCrDate(){
		return this.crDate;
	}

}