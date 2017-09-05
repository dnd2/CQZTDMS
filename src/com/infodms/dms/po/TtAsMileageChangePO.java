/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-10-16 14:09:53
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsMileageChangePO extends PO{

	private Long modBy;
	private String roNo;
	private Integer modAfter;
	private Date modDate;
	private Long id;
	private Integer modBefore;
	private Integer modSystem;
	private Long dealerId;
	private String vin;
	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public void setModBy(Long modBy){
		this.modBy=modBy;
	}

	public Long getModBy(){
		return this.modBy;
	}

	public void setRoNo(String roNo){
		this.roNo=roNo;
	}

	public String getRoNo(){
		return this.roNo;
	}

	public void setModAfter(Integer modAfter){
		this.modAfter=modAfter;
	}

	public Integer getModAfter(){
		return this.modAfter;
	}

	public void setModDate(Date modDate){
		this.modDate=modDate;
	}

	public Date getModDate(){
		return this.modDate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setModBefore(Integer modBefore){
		this.modBefore=modBefore;
	}

	public Integer getModBefore(){
		return this.modBefore;
	}

	public void setModSystem(Integer modSystem){
		this.modSystem=modSystem;
	}

	public Integer getModSystem(){
		return this.modSystem;
	}

}