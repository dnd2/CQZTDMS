/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-04-21 22:22:28
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartLocationLinePO extends PO{

	private Long outStatus;
	private String lineCode;
	private String dutyPer;
	private Integer state;
	private Date updateDate;
	private Long createBy;
	private Integer status;
	private Long whId;
	private Long inStatus;
	private Long updateBy;
	private Date createDate;
	private Long lineId;
	private String dutyTel;
	private Long type;
	private String lineName;

	public void setOutStatus(Long outStatus){
		this.outStatus=outStatus;
	}

	public Long getOutStatus(){
		return this.outStatus;
	}

	public void setLineCode(String lineCode){
		this.lineCode=lineCode;
	}

	public String getLineCode(){
		return this.lineCode;
	}

	public void setDutyPer(String dutyPer){
		this.dutyPer=dutyPer;
	}

	public String getDutyPer(){
		return this.dutyPer;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
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

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setInStatus(Long inStatus){
		this.inStatus=inStatus;
	}

	public Long getInStatus(){
		return this.inStatus;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setLineId(Long lineId){
		this.lineId=lineId;
	}

	public Long getLineId(){
		return this.lineId;
	}

	public void setDutyTel(String dutyTel){
		this.dutyTel=dutyTel;
	}

	public String getDutyTel(){
		return this.dutyTel;
	}

	public void setType(Long type){
		this.type=type;
	}

	public Long getType(){
		return this.type;
	}

	public void setLineName(String lineName){
		this.lineName=lineName;
	}

	public String getLineName(){
		return this.lineName;
	}

}