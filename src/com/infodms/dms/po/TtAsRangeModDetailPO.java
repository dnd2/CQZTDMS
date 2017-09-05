/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-06-04 17:23:32
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsRangeModDetailPO extends PO{

	private Integer modType;
	private Long modBy;
	private String modBefor;
	private String modAfter;
	private Date modDate;
	private Long id;
	private Long dataId;
	private Double labourBefore;
	private Double labourAfter;
	private String labourCode;
	public Double getLabourBefore() {
		return labourBefore;
	}

	public void setLabourBefore(Double labourBefore) {
		this.labourBefore = labourBefore;
	}

	public Double getLabourAfter() {
		return labourAfter;
	}

	public void setLabourAfter(Double labourAfter) {
		this.labourAfter = labourAfter;
	}

	public String getLabourCode() {
		return labourCode;
	}

	public void setLabourCode(String labourCode) {
		this.labourCode = labourCode;
	}

	public Long getDataId() {
		return dataId;
	}

	public void setDataId(Long dataId) {
		this.dataId = dataId;
	}

	public void setModType(Integer modType){
		this.modType=modType;
	}

	public Integer getModType(){
		return this.modType;
	}

	public void setModBy(Long modBy){
		this.modBy=modBy;
	}

	public Long getModBy(){
		return this.modBy;
	}

	public void setModBefor(String modBefor){
		this.modBefor=modBefor;
	}

	public String getModBefor(){
		return this.modBefor;
	}

	public void setModAfter(String modAfter){
		this.modAfter=modAfter;
	}

	public String getModAfter(){
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

}