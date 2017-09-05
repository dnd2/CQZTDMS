/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-22 17:57:18
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesPromisePO extends PO{

	private Long proId;
	private Long dealerId;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Date applyDate;
	private Long status;
	private Long proType;
	private Long auditNum;
	private Date proDate;
	private Date chkDate;
	private Long applyNum;
	private Long updateBy;
	private String proNo;
	private Date createDate;
	private Long yieldly;
	private Long useNum;

	public void setProId(Long proId){
		this.proId=proId;
	}

	public Long getProId(){
		return this.proId;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setApplyDate(Date applyDate){
		this.applyDate=applyDate;
	}

	public Date getApplyDate(){
		return this.applyDate;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setProType(Long proType){
		this.proType=proType;
	}

	public Long getProType(){
		return this.proType;
	}

	public void setAuditNum(Long auditNum){
		this.auditNum=auditNum;
	}

	public Long getAuditNum(){
		return this.auditNum;
	}

	public void setProDate(Date proDate){
		this.proDate=proDate;
	}

	public Date getProDate(){
		return this.proDate;
	}

	public void setChkDate(Date chkDate){
		this.chkDate=chkDate;
	}

	public Date getChkDate(){
		return this.chkDate;
	}

	public void setApplyNum(Long applyNum){
		this.applyNum=applyNum;
	}

	public Long getApplyNum(){
		return this.applyNum;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setProNo(String proNo){
		this.proNo=proNo;
	}

	public String getProNo(){
		return this.proNo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
	}

	public void setUseNum(Long useNum){
		this.useNum=useNum;
	}

	public Long getUseNum(){
		return this.useNum;
	}

}