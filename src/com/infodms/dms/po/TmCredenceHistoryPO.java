/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-12 15:47:37
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmCredenceHistoryPO extends PO{

	private Date beginDate;
	private Double totalAmount;
	private Integer backtype;
	private Long createBy;
	private String credenceCode;
	private Double ratio;
	private Long orgId;
	private String xmname;
	private Date createDate;
	private Integer backDays;
	private Integer loopflag;
	private Date backDate;
	private String credenceRemark;
	private Integer carStyle;
	private String wmflag;
	private Integer status;
	private Integer flag;
	private Integer modeCode;
	private Long credenceId;
	private Long historyId;
	private Date endDate;

	public void setBeginDate(Date beginDate){
		this.beginDate=beginDate;
	}

	public Date getBeginDate(){
		return this.beginDate;
	}

	public void setTotalAmount(Double totalAmount){
		this.totalAmount=totalAmount;
	}

	public Double getTotalAmount(){
		return this.totalAmount;
	}

	public void setBacktype(Integer backtype){
		this.backtype=backtype;
	}

	public Integer getBacktype(){
		return this.backtype;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCredenceCode(String credenceCode){
		this.credenceCode=credenceCode;
	}

	public String getCredenceCode(){
		return this.credenceCode;
	}

	public void setRatio(Double ratio){
		this.ratio=ratio;
	}

	public Double getRatio(){
		return this.ratio;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setXmname(String xmname){
		this.xmname=xmname;
	}

	public String getXmname(){
		return this.xmname;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setBackDays(Integer backDays){
		this.backDays=backDays;
	}

	public Integer getBackDays(){
		return this.backDays;
	}

	public void setLoopflag(Integer loopflag){
		this.loopflag=loopflag;
	}

	public Integer getLoopflag(){
		return this.loopflag;
	}

	public void setBackDate(Date backDate){
		this.backDate=backDate;
	}

	public Date getBackDate(){
		return this.backDate;
	}

	public void setCredenceRemark(String credenceRemark){
		this.credenceRemark=credenceRemark;
	}

	public String getCredenceRemark(){
		return this.credenceRemark;
	}

	public void setCarStyle(Integer carStyle){
		this.carStyle=carStyle;
	}

	public Integer getCarStyle(){
		return this.carStyle;
	}

	public void setWmflag(String wmflag){
		this.wmflag=wmflag;
	}

	public String getWmflag(){
		return this.wmflag;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setFlag(Integer flag){
		this.flag=flag;
	}

	public Integer getFlag(){
		return this.flag;
	}

	public void setModeCode(Integer modeCode){
		this.modeCode=modeCode;
	}

	public Integer getModeCode(){
		return this.modeCode;
	}

	public void setCredenceId(Long credenceId){
		this.credenceId=credenceId;
	}

	public Long getCredenceId(){
		return this.credenceId;
	}

	public void setHistoryId(Long historyId){
		this.historyId=historyId;
	}

	public Long getHistoryId(){
		return this.historyId;
	}

	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}

	public Date getEndDate(){
		return this.endDate;
	}

}