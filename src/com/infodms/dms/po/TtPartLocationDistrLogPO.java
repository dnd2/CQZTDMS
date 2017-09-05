/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-09-09 15:18:49
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartLocationDistrLogPO extends PO{

	private String locCode;
	private Integer state;
	private String remarks;
	private Long distributeId;
	private Date updateDate;
	private String partName;
	private Long createBy;
	private Integer status;
	private String partCode;
	private Long whId;
	private String locName;
	private Long partNum;
	private Long distriLogId;
	private Date storageDate;
	private String distriCode;
	private Long updateBy;
	private Long partId;
	private Long locId;
	private Long itemQty;
	private Date createDate;
	private Long oemMinPkg;

	public void setLocCode(String locCode){
		this.locCode=locCode;
	}

	public String getLocCode(){
		return this.locCode;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setRemarks(String remarks){
		this.remarks=remarks;
	}

	public String getRemarks(){
		return this.remarks;
	}

	public void setDistributeId(Long distributeId){
		this.distributeId=distributeId;
	}

	public Long getDistributeId(){
		return this.distributeId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setLocName(String locName){
		this.locName=locName;
	}

	public String getLocName(){
		return this.locName;
	}

	public void setPartNum(Long partNum){
		this.partNum=partNum;
	}

	public Long getPartNum(){
		return this.partNum;
	}

	public void setDistriLogId(Long distriLogId){
		this.distriLogId=distriLogId;
	}

	public Long getDistriLogId(){
		return this.distriLogId;
	}

	public void setStorageDate(Date storageDate){
		this.storageDate=storageDate;
	}

	public Date getStorageDate(){
		return this.storageDate;
	}

	public void setDistriCode(String distriCode){
		this.distriCode=distriCode;
	}

	public String getDistriCode(){
		return this.distriCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setLocId(Long locId){
		this.locId=locId;
	}

	public Long getLocId(){
		return this.locId;
	}

	public void setItemQty(Long itemQty){
		this.itemQty=itemQty;
	}

	public Long getItemQty(){
		return this.itemQty;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOemMinPkg(Long oemMinPkg){
		this.oemMinPkg=oemMinPkg;
	}

	public Long getOemMinPkg(){
		return this.oemMinPkg;
	}

}