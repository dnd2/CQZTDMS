/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-09-03 10:06:55
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmCallRecordPO extends PO{

	private String callid;
	private Date updateDate;
	private Date crTalkTime;
	private Long createBy;
	private Long crNumber;
	private Integer crCallType;
	private Long crId;
	private Date crEndDate;
	private Date crStaDate;
	private Long seId;
	private Integer var;
	private Long crExt;
	private Long updateBy;
	private String crRecordAddr;
	private Date createDate;
	private Integer crPoints;
	private Long crMoveNumber;
	private Integer crIncomeType;

	public void setCallid(String callid){
		this.callid=callid;
	}

	public String getCallid(){
		return this.callid;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCrTalkTime(Date crTalkTime){
		this.crTalkTime=crTalkTime;
	}

	public Date getCrTalkTime(){
		return this.crTalkTime;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCrNumber(Long crNumber){
		this.crNumber=crNumber;
	}

	public Long getCrNumber(){
		return this.crNumber;
	}

	public void setCrCallType(Integer crCallType){
		this.crCallType=crCallType;
	}

	public Integer getCrCallType(){
		return this.crCallType;
	}

	public void setCrId(Long crId){
		this.crId=crId;
	}

	public Long getCrId(){
		return this.crId;
	}

	public void setCrEndDate(Date crEndDate){
		this.crEndDate=crEndDate;
	}

	public Date getCrEndDate(){
		return this.crEndDate;
	}

	public void setCrStaDate(Date crStaDate){
		this.crStaDate=crStaDate;
	}

	public Date getCrStaDate(){
		return this.crStaDate;
	}

	public void setSeId(Long seId){
		this.seId=seId;
	}

	public Long getSeId(){
		return this.seId;
	}

	public void setVar(Integer var){
		this.var=var;
	}

	public Integer getVar(){
		return this.var;
	}

	public void setCrExt(Long crExt){
		this.crExt=crExt;
	}

	public Long getCrExt(){
		return this.crExt;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCrRecordAddr(String crRecordAddr){
		this.crRecordAddr=crRecordAddr;
	}

	public String getCrRecordAddr(){
		return this.crRecordAddr;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCrPoints(Integer crPoints){
		this.crPoints=crPoints;
	}

	public Integer getCrPoints(){
		return this.crPoints;
	}

	public void setCrMoveNumber(Long crMoveNumber){
		this.crMoveNumber=crMoveNumber;
	}

	public Long getCrMoveNumber(){
		return this.crMoveNumber;
	}

	public void setCrIncomeType(Integer crIncomeType){
		this.crIncomeType=crIncomeType;
	}

	public Integer getCrIncomeType(){
		return this.crIncomeType;
	}

}