/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-08-29 14:59:02
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtStockChangeHisPO extends PO{

	private String state;
	private Long newWhid;
	private Long oldWhid;
	private Long id;
	private Long createBy;
	private Date createDate;
	private Long vehicleId;

	public void setState(String state){
		this.state=state;
	}

	public String getState(){
		return this.state;
	}

	public void setNewWhid(Long newWhid){
		this.newWhid=newWhid;
	}

	public Long getNewWhid(){
		return this.newWhid;
	}

	public void setOldWhid(Long oldWhid){
		this.oldWhid=oldWhid;
	}

	public Long getOldWhid(){
		return this.oldWhid;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

}