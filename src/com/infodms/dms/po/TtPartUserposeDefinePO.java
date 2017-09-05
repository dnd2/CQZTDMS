/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-08-12 16:48:17
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartUserposeDefinePO extends PO{

	private Integer state;
	private Integer isLeader;
	private Long userId;
	private Date disableDate;
	private Date deleteDate;
	private Date updateDate;
	private Long createBy;
	private Long userType;
	private Integer status;
	private Long updateBy;
	private Integer isChkzy;
	private String userName;
	private Long defineId;
	private Integer isDirect;
	private Date createDate;
	private Long disableBy;
	private Long deleteBy;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setIsLeader(Integer isLeader){
		this.isLeader=isLeader;
	}

	public Integer getIsLeader(){
		return this.isLeader;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return this.userId;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
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

	public void setUserType(Long userType){
		this.userType=userType;
	}

	public Long getUserType(){
		return this.userType;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setIsChkzy(Integer isChkzy){
		this.isChkzy=isChkzy;
	}

	public Integer getIsChkzy(){
		return this.isChkzy;
	}

	public void setUserName(String userName){
		this.userName=userName;
	}

	public String getUserName(){
		return this.userName;
	}

	public void setDefineId(Long defineId){
		this.defineId=defineId;
	}

	public Long getDefineId(){
		return this.defineId;
	}

	public void setIsDirect(Integer isDirect){
		this.isDirect=isDirect;
	}

	public Integer getIsDirect(){
		return this.isDirect;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

}