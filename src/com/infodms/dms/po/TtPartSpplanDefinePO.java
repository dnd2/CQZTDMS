/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-31 10:11:02
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartSpplanDefinePO extends PO{

	private String room;
	private Integer state;
	private String tbd1;
	private String tbd2;
	private Date disableDate;
	private String whman;
	private Date deleteDate;
	private Date updateDate;
	private Long createBy;
	private Integer status;
	private Long defId;
	private Long updateBy;
	private Long partId;
	private Long deleteBy;
	private Date createDate;
	private Long disableBy;
	private Long planQty;
	private Long plannerId;

	public void setRoom(String room){
		this.room=room;
	}

	public String getRoom(){
		return this.room;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setTbd1(String tbd1){
		this.tbd1=tbd1;
	}

	public String getTbd1(){
		return this.tbd1;
	}

	public void setTbd2(String tbd2){
		this.tbd2=tbd2;
	}

	public String getTbd2(){
		return this.tbd2;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setWhman(String whman){
		this.whman=whman;
	}

	public String getWhman(){
		return this.whman;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setDefId(Long defId){
		this.defId=defId;
	}

	public Long getDefId(){
		return this.defId;
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

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setPlanQty(Long planQty){
		this.planQty=planQty;
	}

	public Long getPlanQty(){
		return this.planQty;
	}
	
	public void setPlannerId(Long plannerId){
		this.plannerId=plannerId;
	}

	public Long getPlannerId(){
		return this.plannerId;
	}

}