/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-19 13:00:49
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrTrobleMapPO extends PO{

	private Long mapId;
	private Long troubleId;
	private Long laborId;
	private Integer isDel;

	public void setMapId(Long mapId){
		this.mapId=mapId;
	}

	public Long getMapId(){
		return this.mapId;
	}

	public void setTroubleId(Long troubleId){
		this.troubleId=troubleId;
	}

	public Long getTroubleId(){
		return this.troubleId;
	}

	public void setLaborId(Long laborId){
		this.laborId=laborId;
	}

	public Long getLaborId(){
		return this.laborId;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

}