/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-08-29 19:03:05
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmSeatsTeamPO extends PO{

	private Integer var;
	private Long updateBy;
	private String stMemo;
	private String stCode;
	private Date deleteDate;
	private String stName;
	private Date updateDate;
	private Long stId;
	private Long createBy;
	private String deleteBy;
	private Date createDate;
	private Integer status;

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

	public void setStMemo(String stMemo){
		this.stMemo=stMemo;
	}

	public String getStMemo(){
		return this.stMemo;
	}

	public void setStCode(String stCode){
		this.stCode=stCode;
	}

	public String getStCode(){
		return this.stCode;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setStName(String stName){
		this.stName=stName;
	}

	public String getStName(){
		return this.stName;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStId(Long stId){
		this.stId=stId;
	}

	public Long getStId(){
		return this.stId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setDeleteBy(String deleteBy){
		this.deleteBy=deleteBy;
	}

	public String getDeleteBy(){
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

}