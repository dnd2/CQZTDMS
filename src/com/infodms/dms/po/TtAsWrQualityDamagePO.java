/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-25 10:42:56
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrQualityDamagePO extends PO{

	private String qudCode;
	private String qudName;
	private String qudSonCode;
	private String qudSonName;
	private Long oemCompanyId;
	private Long updateBy;
	private Date updateDate;
	private Long qudId;
	private Integer ver;
	private Long createBy;
	private Date createDate;
	private String qudAllName;

	public String getQudAllName() {
		return qudAllName;
	}

	public void setQudAllName(String qudAllName) {
		this.qudAllName = qudAllName;
	}

	public void setQudCode(String qudCode){
		this.qudCode=qudCode;
	}

	public String getQudCode(){
		return this.qudCode;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
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

	public void setQudId(Long qudId){
		this.qudId=qudId;
	}

	public Long getQudId(){
		return this.qudId;
	}

	public void setQudName(String qudName){
		this.qudName=qudName;
	}

	public String getQudName(){
		return this.qudName;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
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

	public String getQudSonCode() {
		return qudSonCode;
	}

	public void setQudSonCode(String qudSonCode) {
		this.qudSonCode = qudSonCode;
	}

	public String getQudSonName() {
		return qudSonName;
	}

	public void setQudSonName(String qudSonName) {
		this.qudSonName = qudSonName;
	}

}