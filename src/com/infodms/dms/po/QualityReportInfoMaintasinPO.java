/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-25 15:12:05
* CreateBy   : asus
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class QualityReportInfoMaintasinPO extends PO{

	private Integer tYear;
	private Integer tMonth;
	private Long id;
	private Integer randomId;
	private String serviceCode;
	private Integer tDay;

	public QualityReportInfoMaintasinPO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QualityReportInfoMaintasinPO(Long id,String serviceCode,
			Integer tYear, Integer tMonth, Integer tDay, Integer randomId) {
		super();
		this.tYear = tYear;
		this.tMonth = tMonth;
		this.id = id;
		this.randomId = randomId;
		this.serviceCode = serviceCode;
		this.tDay = tDay;
	}



	public void setTYear(Integer tYear){
		this.tYear=tYear;
	}

	public Integer getTYear(){
		return this.tYear;
	}

	public void setTMonth(Integer tMonth){
		this.tMonth=tMonth;
	}

	public Integer getTMonth(){
		return this.tMonth;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setRandomId(Integer randomId){
		this.randomId=randomId;
	}

	public Integer getRandomId(){
		return this.randomId;
	}

	public void setServiceCode(String serviceCode){
		this.serviceCode=serviceCode;
	}

	public String getServiceCode(){
		return this.serviceCode;
	}

	public void setTDay(Integer tDay){
		this.tDay=tDay;
	}

	public Integer getTDay(){
		return this.tDay;
	}

}