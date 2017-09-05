/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-09-11 08:32:20
* CreateBy   : wangsw
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmProDetailPO extends PO{

	private String colorCode;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Long detailId;
	private String xpSendStatus;
	private String xpGysName;
	private Long proOrderId;
	private Long updateBy;
	private String xpCode;
	private Long packageId;
	private Date createDate;
	private String xpName;
	private Integer num;
	private String colorName;
	private Long maiId;
	private Integer proNum;
	private String xpGysCode;

	public void setColorCode(String colorCode){
		this.colorCode=colorCode;
	}

	public String getColorCode(){
		return this.colorCode;
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

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setXpSendStatus(String xpSendStatus){
		this.xpSendStatus=xpSendStatus;
	}

	public String getXpSendStatus(){
		return this.xpSendStatus;
	}

	public void setXpGysName(String xpGysName){
		this.xpGysName=xpGysName;
	}

	public String getXpGysName(){
		return this.xpGysName;
	}

	public void setProOrderId(Long proOrderId){
		this.proOrderId=proOrderId;
	}

	public Long getProOrderId(){
		return this.proOrderId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setXpCode(String xpCode){
		this.xpCode=xpCode;
	}

	public String getXpCode(){
		return this.xpCode;
	}

	public void setPackageId(Long packageId){
		this.packageId=packageId;
	}

	public Long getPackageId(){
		return this.packageId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setXpName(String xpName){
		this.xpName=xpName;
	}

	public String getXpName(){
		return this.xpName;
	}

	public void setNum(Integer num){
		this.num=num;
	}

	public Integer getNum(){
		return this.num;
	}

	public void setColorName(String colorName){
		this.colorName=colorName;
	}

	public String getColorName(){
		return this.colorName;
	}

	public void setMaiId(Long maiId){
		this.maiId=maiId;
	}

	public Long getMaiId(){
		return this.maiId;
	}

	public void setProNum(Integer proNum){
		this.proNum=proNum;
	}

	public Integer getProNum(){
		return this.proNum;
	}

	public void setXpGysCode(String xpGysCode){
		this.xpGysCode=xpGysCode;
	}

	public String getXpGysCode(){
		return this.xpGysCode;
	}

}