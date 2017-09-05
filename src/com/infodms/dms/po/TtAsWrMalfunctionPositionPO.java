/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-25 17:35:57
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrMalfunctionPositionPO extends PO{

	private Double wrMileage;
	private Date updateDate;
	private Double attMileage;
	private Long posId;
	private Long createBy;
	private String posName;
	private String posCode;
	private Integer isDel;
	private Long kdId;
	private Long updateBy;
	private Integer wrMonths;
	private Integer attDays;
	private Integer ver;
	private Date createDate;

	public void setWrMileage(Double wrMileage){
		this.wrMileage=wrMileage;
	}

	public Double getWrMileage(){
		return this.wrMileage;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setAttMileage(Double attMileage){
		this.attMileage=attMileage;
	}

	public Double getAttMileage(){
		return this.attMileage;
	}

	public void setPosId(Long posId){
		this.posId=posId;
	}

	public Long getPosId(){
		return this.posId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setPosName(String posName){
		this.posName=posName;
	}

	public String getPosName(){
		return this.posName;
	}

	public void setPosCode(String posCode){
		this.posCode=posCode;
	}

	public String getPosCode(){
		return this.posCode;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setKdId(Long kdId){
		this.kdId=kdId;
	}

	public Long getKdId(){
		return this.kdId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setWrMonths(Integer wrMonths){
		this.wrMonths=wrMonths;
	}

	public Integer getWrMonths(){
		return this.wrMonths;
	}

	public void setAttDays(Integer attDays){
		this.attDays=attDays;
	}

	public Integer getAttDays(){
		return this.attDays;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}