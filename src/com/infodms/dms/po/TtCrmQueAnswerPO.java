/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-09 15:12:45
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmQueAnswerPO extends PO{

	private Long qrId;
	private Integer qdNo;
	private Date updateDate;
	private Long rdUserId;
	private String rdUser;
	private Date rdDate;
	private String createBy;
	private Long rdAnsId;
	private String qdQueAnswer;
	private String qdQueReason;
	private Integer var;
	private String updateBy;
	private Long rvId;
	private Date createDate;

	public void setQrId(Long qrId){
		this.qrId=qrId;
	}

	public Long getQrId(){
		return this.qrId;
	}

	public void setQdNo(Integer qdNo){
		this.qdNo=qdNo;
	}

	public Integer getQdNo(){
		return this.qdNo;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setRdUserId(Long rdUserId){
		this.rdUserId=rdUserId;
	}

	public Long getRdUserId(){
		return this.rdUserId;
	}

	public void setRdUser(String rdUser){
		this.rdUser=rdUser;
	}

	public String getRdUser(){
		return this.rdUser;
	}

	public void setRdDate(Date rdDate){
		this.rdDate=rdDate;
	}

	public Date getRdDate(){
		return this.rdDate;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setRdAnsId(Long rdAnsId){
		this.rdAnsId=rdAnsId;
	}

	public Long getRdAnsId(){
		return this.rdAnsId;
	}

	public void setQdQueAnswer(String qdQueAnswer){
		this.qdQueAnswer=qdQueAnswer;
	}

	public String getQdQueAnswer(){
		return this.qdQueAnswer;
	}

	public void setVar(Integer var){
		this.var=var;
	}

	public Integer getVar(){
		return this.var;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setRvId(Long rvId){
		this.rvId=rvId;
	}

	public Long getRvId(){
		return this.rvId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public String getQdQueReason() {
		return qdQueReason;
	}

	public void setQdQueReason(String qdQueReason) {
		this.qdQueReason = qdQueReason;
	}

}