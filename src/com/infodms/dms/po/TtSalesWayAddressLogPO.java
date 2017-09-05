/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-08-08 11:15:25
* CreateBy   : ljie
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesWayAddressLogPO extends PO{

	private Long businessBy;
	private Long dtlId;
	private Long logId;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Integer afterStatus;
	private Integer beforeStatus;
	private Date createDate;
	private Long vehicleId;

	public void setBusinessBy(Long businessBy){
		this.businessBy=businessBy;
	}

	public Long getBusinessBy(){
		return this.businessBy;
	}

	public void setDtlId(Long dtlId){
		this.dtlId=dtlId;
	}

	public Long getDtlId(){
		return this.dtlId;
	}

	public void setLogId(Long logId){
		this.logId=logId;
	}

	public Long getLogId(){
		return this.logId;
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

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAfterStatus(Integer afterStatus){
		this.afterStatus=afterStatus;
	}

	public Integer getAfterStatus(){
		return this.afterStatus;
	}

	public void setBeforeStatus(Integer beforeStatus){
		this.beforeStatus=beforeStatus;
	}

	public Integer getBeforeStatus(){
		return this.beforeStatus;
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