/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-19 18:33:29
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcCompetVechilePO extends PO{

	private Long parId;
	private String competName;
	private Date updateDate;
	private String competCode;
	private Long status;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private Long competId;
	private Integer competLevel;
	
	
	

	public Integer getCompetLevel() {
		return competLevel;
	}

	public void setCompetLevel(Integer competLevel) {
		this.competLevel = competLevel;
	}

	public void setParId(Long parId){
		this.parId=parId;
	}

	public Long getParId(){
		return this.parId;
	}

	public void setCompetName(String competName){
		this.competName=competName;
	}

	public String getCompetName(){
		return this.competName;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCompetCode(String competCode){
		this.competCode=competCode;
	}

	public String getCompetCode(){
		return this.competCode;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCompetId(Long competId){
		this.competId=competId;
	}

	public Long getCompetId(){
		return this.competId;
	}

}