/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-04-17 11:21:31
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmHotSpotPO extends PO{

	private Integer spotTimes;
	private Long dealerId;
	private Date updateDate;
	private String remark;
	private String spotUserName;
	private Long createBy;
	private Integer spotSatisfaction;
	private String phone;
	private Long updateBy;
	private Integer spotResult;
	private Date spotDate;
	private Long spotId;
	private Date createDate;
	private Long spotUserId;

	public void setSpotTimes(Integer spotTimes){
		this.spotTimes=spotTimes;
	}

	public Integer getSpotTimes(){
		return this.spotTimes;
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

	public void setSpotUserName(String spotUserName){
		this.spotUserName=spotUserName;
	}

	public String getSpotUserName(){
		return this.spotUserName;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setSpotSatisfaction(Integer spotSatisfaction){
		this.spotSatisfaction=spotSatisfaction;
	}

	public Integer getSpotSatisfaction(){
		return this.spotSatisfaction;
	}

	public void setPhone(String phone){
		this.phone=phone;
	}

	public String getPhone(){
		return this.phone;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setSpotResult(Integer spotResult){
		this.spotResult=spotResult;
	}

	public Integer getSpotResult(){
		return this.spotResult;
	}

	public void setSpotDate(Date spotDate){
		this.spotDate=spotDate;
	}

	public Date getSpotDate(){
		return this.spotDate;
	}

	public void setSpotId(Long spotId){
		this.spotId=spotId;
	}

	public Long getSpotId(){
		return this.spotId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSpotUserId(Long spotUserId){
		this.spotUserId=spotUserId;
	}

	public Long getSpotUserId(){
		return this.spotUserId;
	}

}