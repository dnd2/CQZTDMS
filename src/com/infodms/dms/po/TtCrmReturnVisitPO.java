/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-08-27 16:36:58
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmReturnVisitPO extends PO{

	private Integer rvSatisfaction;
	private Long qrId;
	private String rvCusName;
	private Integer rvStatus;
	private String vin;
	private Date rvDate;
	private Date updateDate;
	private Long rvCusId;
	private Integer rvType;
	private Long rvAssUserId;
	private Long createBy;
	private Integer rvResult;
	private String rvAssUser;
	private String rvPhone;
	private Integer var;
	private Long updateBy;
	private Long rvId;
	private Integer rvTimes;
	private Date createDate;
	private String remark;

	public void setRvSatisfaction(Integer rvSatisfaction){
		this.rvSatisfaction=rvSatisfaction;
	}

	public Integer getRvSatisfaction(){
		return this.rvSatisfaction;
	}

	public void setQrId(Long qrId){
		this.qrId=qrId;
	}

	public Long getQrId(){
		return this.qrId;
	}

	public void setRvCusName(String rvCusName){
		this.rvCusName=rvCusName;
	}

	public String getRvCusName(){
		return this.rvCusName;
	}

	public void setRvStatus(Integer rvStatus){
		this.rvStatus=rvStatus;
	}

	public Integer getRvStatus(){
		return this.rvStatus;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setRvDate(Date rvDate){
		this.rvDate=rvDate;
	}

	public Date getRvDate(){
		return this.rvDate;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setRvCusId(Long rvCusId){
		this.rvCusId=rvCusId;
	}

	public Long getRvCusId(){
		return this.rvCusId;
	}

	public void setRvType(Integer rvType){
		this.rvType=rvType;
	}

	public Integer getRvType(){
		return this.rvType;
	}

	public void setRvAssUserId(Long rvAssUserId){
		this.rvAssUserId=rvAssUserId;
	}

	public Long getRvAssUserId(){
		return this.rvAssUserId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRvResult(Integer rvResult){
		this.rvResult=rvResult;
	}

	public Integer getRvResult(){
		return this.rvResult;
	}

	public void setRvAssUser(String rvAssUser){
		this.rvAssUser=rvAssUser;
	}

	public String getRvAssUser(){
		return this.rvAssUser;
	}

	public void setRvPhone(String rvPhone){
		this.rvPhone=rvPhone;
	}

	public String getRvPhone(){
		return this.rvPhone;
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

	public void setRvId(Long rvId){
		this.rvId=rvId;
	}

	public Long getRvId(){
		return this.rvId;
	}

	public void setRvTimes(Integer rvTimes){
		this.rvTimes=rvTimes;
	}

	public Integer getRvTimes(){
		return this.rvTimes;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}