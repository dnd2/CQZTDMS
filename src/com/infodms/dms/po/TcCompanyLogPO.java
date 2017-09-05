/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-07-10 12:33:25
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TcCompanyLogPO extends PO{

	private String status1;
	private String newCompanyName;
	private String oldValue;
	private Long companyId;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private String keyGbk;
	private String key;
	private String status;
	private Long logId;
	private String oldCompanyName;
	private Long updateBy;
	private String createBy1;
	private String value;
	private Integer ver;
	private String companyCode;
	private Date createDate;

	public void setStatus1(String status1){
		this.status1=status1;
	}

	public String getStatus1(){
		return this.status1;
	}

	public void setNewCompanyName(String newCompanyName){
		this.newCompanyName=newCompanyName;
	}

	public String getNewCompanyName(){
		return this.newCompanyName;
	}

	public void setOldValue(String oldValue){
		this.oldValue=oldValue;
	}

	public String getOldValue(){
		return this.oldValue;
	}

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
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

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setKeyGbk(String keyGbk){
		this.keyGbk=keyGbk;
	}

	public String getKeyGbk(){
		return this.keyGbk;
	}

	public void setKey(String key){
		this.key=key;
	}

	public String getKey(){
		return this.key;
	}

	public void setStatus(String status){
		this.status=status;
	}

	public String getStatus(){
		return this.status;
	}

	public void setLogId(Long logId){
		this.logId=logId;
	}

	public Long getLogId(){
		return this.logId;
	}

	public void setOldCompanyName(String oldCompanyName){
		this.oldCompanyName=oldCompanyName;
	}

	public String getOldCompanyName(){
		return this.oldCompanyName;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy1(String createBy1){
		this.createBy1=createBy1;
	}

	public String getCreateBy1(){
		return this.createBy1;
	}

	public void setValue(String value){
		this.value=value;
	}

	public String getValue(){
		return this.value;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setCompanyCode(String companyCode){
		this.companyCode=companyCode;
	}

	public String getCompanyCode(){
		return this.companyCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}