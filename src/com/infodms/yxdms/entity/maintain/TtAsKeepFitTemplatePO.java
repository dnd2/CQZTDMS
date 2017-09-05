/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-20 20:11:03
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.maintain;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsKeepFitTemplatePO extends PO{

	private Double keepFitAmount;
	private Long id;
	private String createBy;
	private String keepFitNo;
	private Date createDate;
	private Integer status;
	private Integer isDel;
	private String keepFitName;
	private Integer chooseType;
	private String packageCode;

	public void setKeepFitAmount(Double keepFitAmount){
		this.keepFitAmount=keepFitAmount;
	}

	public Double getKeepFitAmount(){
		return this.keepFitAmount;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setKeepFitNo(String keepFitNo){
		this.keepFitNo=keepFitNo;
	}

	public String getKeepFitNo(){
		return this.keepFitNo;
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

	public void setKeepFitName(String keepFitName){
		this.keepFitName=keepFitName;
	}

	public String getKeepFitName(){
		return this.keepFitName;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public void setChooseType(Integer chooseType) {
		this.chooseType = chooseType;
	}

	public Integer getChooseType() {
		return chooseType;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

}