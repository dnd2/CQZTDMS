/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-04-29 10:42:58
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartUserprovinceDefinePO extends PO{

	private Integer state;
	private Long userId;
	private Long updateBy;
	private Date deleteDate;
	private Date updateDate;
	private Long defineId;
	private Long createBy;
	private Long userType;
	private Long deleteBy;
	private Date createDate;
	private Integer status;
	private Long provinceId;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return this.userId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
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

	public void setDefineId(Long defineId){
		this.defineId=defineId;
	}

	public Long getDefineId(){
		return this.defineId;
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

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setProvinceId(Long provinceId){
		this.provinceId=provinceId;
	}

	public Long getProvinceId(){
		return this.provinceId;
	}

}