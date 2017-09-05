/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-24 20:07:42
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrMalfunctionPO extends PO{

	private Long malId;
	private Long oemCompanyId;
	private String malName;
	private String malCode;
	private Integer ver;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private Long qudId;

	public Long getQudId() {
		return qudId;
	}

	public void setQudId(Long qudId) {
		this.qudId = qudId;
	}

	public void setMalId(Long malId){
		this.malId=malId;
	}

	public Long getMalId(){
		return this.malId;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setMalName(String malName){
		this.malName=malName;
	}

	public String getMalName(){
		return this.malName;
	}

	public void setMalCode(String malCode){
		this.malCode=malCode;
	}

	public String getMalCode(){
		return this.malCode;
	}


	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public Integer getVer() {
		return ver;
	}

	public void setVer(Integer ver) {
		this.ver = ver;
	}

}