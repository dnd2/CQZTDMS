/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-22 09:47:57
* CreateBy   : ASUS
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrFeeWarrantyPO extends PO{

	private String ctmName;
	private Long warId;
	private Long dealerId;
	private String vin;
	private Long vrId;
	private Date updateDate;
	private Long createBy;
	private String mainPhone;
	private Integer warCountDays;
	private Double warMileage;
	private String roNo;
	private Integer warLevel;
	private Long updateBy;
	private String licenseNo;
	private Integer curLawDays;
	private Date createDate;
	private Date warDate;
	private Integer curWrDays;
	private String curWrStandard;
	private String warNo;

	public void setCtmName(String ctmName){
		this.ctmName=ctmName;
	}

	public String getCtmName(){
		return this.ctmName;
	}

	public void setWarId(Long warId){
		this.warId=warId;
	}

	public Long getWarId(){
		return this.warId;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setVrId(Long vrId){
		this.vrId=vrId;
	}

	public Long getVrId(){
		return this.vrId;
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

	public void setMainPhone(String mainPhone){
		this.mainPhone=mainPhone;
	}

	public String getMainPhone(){
		return this.mainPhone;
	}

	public void setWarCountDays(Integer warCountDays){
		this.warCountDays=warCountDays;
	}

	public Integer getWarCountDays(){
		return this.warCountDays;
	}

	public void setWarMileage(Double warMileage){
		this.warMileage=warMileage;
	}

	public Double getWarMileage(){
		return this.warMileage;
	}

	public void setRoNo(String roNo){
		this.roNo=roNo;
	}

	public String getRoNo(){
		return this.roNo;
	}

	public void setWarLevel(Integer warLevel){
		this.warLevel=warLevel;
	}

	public Integer getWarLevel(){
		return this.warLevel;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setLicenseNo(String licenseNo){
		this.licenseNo=licenseNo;
	}

	public String getLicenseNo(){
		return this.licenseNo;
	}

	public void setCurLawDays(Integer curLawDays){
		this.curLawDays=curLawDays;
	}

	public Integer getCurLawDays(){
		return this.curLawDays;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setWarDate(Date warDate){
		this.warDate=warDate;
	}

	public Date getWarDate(){
		return this.warDate;
	}

	public void setCurWrDays(Integer curWrDays){
		this.curWrDays=curWrDays;
	}

	public Integer getCurWrDays(){
		return this.curWrDays;
	}

	public void setCurWrStandard(String curWrStandard){
		this.curWrStandard=curWrStandard;
	}

	public String getCurWrStandard(){
		return this.curWrStandard;
	}

	public void setWarNo(String warNo){
		this.warNo=warNo;
	}

	public String getWarNo(){
		return this.warNo;
	}

}