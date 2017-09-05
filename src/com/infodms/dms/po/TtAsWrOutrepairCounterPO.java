/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-11-11 14:35:27
* CreateBy   : chenyu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrOutrepairCounterPO extends PO{

	private Integer isReadyCar;
	private String roNo;
	private String fromAdress;
	private Long updateBy;
	private Date updateDate;
	private Long id;
	private Long createBy;
	private String outLicenseno;
	private String endAdress;
	private Date createDate;
	private Double outMileage;
	private String outPerson;
	private Date endTime;
	private Date startTime;
	private String outCar;
	private String outSite;

	public void setIsReadyCar(Integer isReadyCar){
		this.isReadyCar=isReadyCar;
	}

	public Integer getIsReadyCar(){
		return this.isReadyCar;
	}

	public void setRoNo(String roNo){
		this.roNo=roNo;
	}

	public String getRoNo(){
		return this.roNo;
	}

	public void setFromAdress(String fromAdress){
		this.fromAdress=fromAdress;
	}

	public String getFromAdress(){
		return this.fromAdress;
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

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOutLicenseno(String outLicenseno){
		this.outLicenseno=outLicenseno;
	}

	public String getOutLicenseno(){
		return this.outLicenseno;
	}

	public void setEndAdress(String endAdress){
		this.endAdress=endAdress;
	}

	public String getEndAdress(){
		return this.endAdress;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOutMileage(Double outMileage){
		this.outMileage=outMileage;
	}

	public Double getOutMileage(){
		return this.outMileage;
	}

	public void setOutPerson(String outPerson){
		this.outPerson=outPerson;
	}

	public String getOutPerson(){
		return this.outPerson;
	}

	public void setEndTime(Date endTime){
		this.endTime=endTime;
	}

	public Date getEndTime(){
		return this.endTime;
	}

	public void setStartTime(Date startTime){
		this.startTime=startTime;
	}

	public Date getStartTime(){
		return this.startTime;
	}

	public void setOutCar(String outCar){
		this.outCar=outCar;
	}

	public String getOutCar(){
		return this.outCar;
	}

	public void setOutSite(String outSite){
		this.outSite=outSite;
	}

	public String getOutSite(){
		return this.outSite;
	}

}