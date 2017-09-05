/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-11 15:59:01
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartFixcodeDefinePO extends PO{

	private String sortNo;
	private Integer state;
	private String fixName;
	private Date disableDate;
	private Date deleteDate;
	private Date updateDate;
	private Integer fixGouptype;
	private Long createBy;
	private Long fixId;
	private Integer status;
	private String fixValue;
	private Long updateBy;
	private Long deleteBy;
	private Date createDate;
	private Long disableBy;
	private String fixGroupname;

	public void setSortNo(String sortNo){
		this.sortNo=sortNo;
	}

	public String getSortNo(){
		return this.sortNo;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setFixName(String fixName){
		this.fixName=fixName;
	}

	public String getFixName(){
		return this.fixName;
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

	public void setFixGouptype(Integer fixGouptype){
		this.fixGouptype=fixGouptype;
	}

	public Integer getFixGouptype(){
		return this.fixGouptype;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setFixId(Long fixId){
		this.fixId=fixId;
	}

	public Long getFixId(){
		return this.fixId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setFixValue(String fixValue){
		this.fixValue=fixValue;
	}

	public String getFixValue(){
		return this.fixValue;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
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

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setFixGroupname(String fixGroupname){
		this.fixGroupname=fixGroupname;
	}

	public String getFixGroupname(){
		return this.fixGroupname;
	}

}