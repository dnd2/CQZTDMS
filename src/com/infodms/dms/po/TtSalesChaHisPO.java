/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-27 09:28:05
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesChaHisPO extends PO{

	private Long newVehicleId;
	private Date updateDate;
	private Long hisId;
	private Long updateBy;
	private Long createBy;
	private Long boDeId;
	private Long chaPer;
	private Date createDate;
	private Date chaDate;
	private Long oldVehicleId;
	private Integer isReturn;
	private Integer isStatus;
	private String remark;
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(Integer isStatus) {
		this.isStatus = isStatus;
	}

	public Integer getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Integer isReturn) {
		this.isReturn = isReturn;
	}

	public void setNewVehicleId(Long newVehicleId){
		this.newVehicleId=newVehicleId;
	}

	public Long getNewVehicleId(){
		return this.newVehicleId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setHisId(Long hisId){
		this.hisId=hisId;
	}

	public Long getHisId(){
		return this.hisId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setBoDeId(Long boDeId){
		this.boDeId=boDeId;
	}

	public Long getBoDeId(){
		return this.boDeId;
	}

	public void setChaPer(Long chaPer){
		this.chaPer=chaPer;
	}

	public Long getChaPer(){
		return this.chaPer;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setChaDate(Date chaDate){
		this.chaDate=chaDate;
	}

	public Date getChaDate(){
		return this.chaDate;
	}

	public void setOldVehicleId(Long oldVehicleId){
		this.oldVehicleId=oldVehicleId;
	}

	public Long getOldVehicleId(){
		return this.oldVehicleId;
	}

}