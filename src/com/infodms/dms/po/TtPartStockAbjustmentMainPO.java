/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-07-25 11:10:44
* CreateBy   : MEpaper
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartStockAbjustmentMainPO extends PO{

	private Integer state;
	private Integer checkState;
	private Date updateDate;
	private String whCname;
	private String remark;
	private Long createBy;
	private Date checkDate;
	private Date applyDate;
	private Long abjustmentId;
	private Integer status;
	private Integer applyState;
	private Long applyBy;
	private Long whId;
	private Long checkBy;
	private Long updateBy;
	private String abjustmentCode;
	private String abjustmentName;
	private Date createDate;
	private Integer abjustmentType;
	private Long orgId;
	private String orgCode;
	private String orgName;
	private String checkRemark;
	
	public String getCheckRemark() {
        return checkRemark;
    }

    public void setCheckRemark(String checkRemark) {
        this.checkRemark = checkRemark;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setCheckState(Integer checkState){
		this.checkState=checkState;
	}

	public Integer getCheckState(){
		return this.checkState;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setWhCname(String whCname){
		this.whCname=whCname;
	}

	public String getWhCname(){
		return this.whCname;
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

	public void setCheckDate(Date checkDate){
		this.checkDate=checkDate;
	}

	public Date getCheckDate(){
		return this.checkDate;
	}

	public void setApplyDate(Date applyDate){
		this.applyDate=applyDate;
	}

	public Date getApplyDate(){
		return this.applyDate;
	}

	public void setAbjustmentId(Long abjustmentId){
		this.abjustmentId=abjustmentId;
	}

	public Long getAbjustmentId(){
		return this.abjustmentId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setApplyState(Integer applyState){
		this.applyState=applyState;
	}

	public Integer getApplyState(){
		return this.applyState;
	}

	public void setApplyBy(Long applyBy){
		this.applyBy=applyBy;
	}

	public Long getApplyBy(){
		return this.applyBy;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setCheckBy(Long checkBy){
		this.checkBy=checkBy;
	}

	public Long getCheckBy(){
		return this.checkBy;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setAbjustmentCode(String abjustmentCode){
		this.abjustmentCode=abjustmentCode;
	}

	public String getAbjustmentCode(){
		return this.abjustmentCode;
	}
	
	public String getAbjustmentName() {
        return abjustmentName;
    }

    public void setAbjustmentName(String abjustmentName) {
        this.abjustmentName = abjustmentName;
    }

    public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAbjustmentType(Integer abjustmentType){
		this.abjustmentType=abjustmentType;
	}

	public Integer getAbjustmentType(){
		return this.abjustmentType;
	}

}