/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-08 15:34:22
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmSortShiftPO extends PO{

	private Date dutyDate;
	private Long userId;
	private Date updateDate;
	private Date ssDate;
	private Integer wtType;
	private String shiftKind;
	private String shiftKindDesc;
	private Long createBy;
	private String seName;
	private Integer status;
	private Long ssId;
	private Long updateBy;
	private Date createDate;
	private String seAccount;
	private Long ssBy;
	private Date StaDate;
	private Date EndDate;

	public Date getStaDate() {
		return StaDate;
	}

	public void setStaDate(Date staDate) {
		StaDate = staDate;
	}

	public Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}

	public void setDutyDate(Date dutyDate){
		this.dutyDate=dutyDate;
	}

	public Date getDutyDate(){
		return this.dutyDate;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return this.userId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setSsDate(Date ssDate){
		this.ssDate=ssDate;
	}

	public Date getSsDate(){
		return this.ssDate;
	}

	public void setWtType(Integer wtType){
		this.wtType=wtType;
	}

	public Integer getWtType(){
		return this.wtType;
	}

	public void setShiftKind(String shiftKind){
		this.shiftKind=shiftKind;
	}

	public String getShiftKind(){
		return this.shiftKind;
	}	

	public void setShiftKindDesc(String shiftKindDesc){
		this.shiftKindDesc=shiftKindDesc;
	}

	public String getShiftKindDesc(){
		return this.shiftKindDesc;
	}	
	
	
	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setSeName(String seName){
		this.seName=seName;
	}

	public String getSeName(){
		return this.seName;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setSsId(Long ssId){
		this.ssId=ssId;
	}

	public Long getSsId(){
		return this.ssId;
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

	public void setSeAccount(String seAccount){
		this.seAccount=seAccount;
	}

	public String getSeAccount(){
		return this.seAccount;
	}

	public void setSsBy(Long ssBy){
		this.ssBy=ssBy;
	}

	public Long getSsBy(){
		return this.ssBy;
	}

}