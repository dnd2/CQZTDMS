/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-29 11:42:12
* CreateBy   : Andy
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmStationMappingsPO extends PO{

	private Integer status;
	private Long stationId;
	private Long recordId;
	private Long stationMappingId;

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setStationId(Long stationId){
		this.stationId=stationId;
	}

	public Long getStationId(){
		return this.stationId;
	}

	public void setRecordId(Long recordId){
		this.recordId=recordId;
	}

	public Long getRecordId(){
		return this.recordId;
	}

	public void setStationMappingId(Long stationMappingId){
		this.stationMappingId=stationMappingId;
	}

	public Long getStationMappingId(){
		return this.stationMappingId;
	}

}