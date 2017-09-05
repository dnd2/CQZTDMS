/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-03 10:26:38
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TsiExpBusVehStorePO extends PO{

	private String plant;
	private String material;
	private Long revId;
	private String orderid;
	private String headerTxt;
	private Long entryQnt;
	private Date pstngDate;
	private String entryUom;
	private String stgeLoc;
	private Integer isRead;
	private String moveType;
	private Date createDate;
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setPlant(String plant){
		this.plant=plant;
	}

	public String getPlant(){
		return this.plant;
	}

	public void setMaterial(String material){
		this.material=material;
	}

	public String getMaterial(){
		return this.material;
	}

	public void setRevId(Long revId){
		this.revId=revId;
	}

	public Long getRevId(){
		return this.revId;
	}

	public void setOrderid(String orderid){
		this.orderid=orderid;
	}

	public String getOrderid(){
		return this.orderid;
	}

	public void setHeaderTxt(String headerTxt){
		this.headerTxt=headerTxt;
	}

	public String getHeaderTxt(){
		return this.headerTxt;
	}

	public void setEntryQnt(Long entryQnt){
		this.entryQnt=entryQnt;
	}

	public Long getEntryQnt(){
		return this.entryQnt;
	}

	public void setPstngDate(Date pstngDate){
		this.pstngDate=pstngDate;
	}

	public Date getPstngDate(){
		return this.pstngDate;
	}

	public void setEntryUom(String entryUom){
		this.entryUom=entryUom;
	}

	public String getEntryUom(){
		return this.entryUom;
	}

	public void setStgeLoc(String stgeLoc){
		this.stgeLoc=stgeLoc;
	}

	public String getStgeLoc(){
		return this.stgeLoc;
	}

	public void setIsRead(Integer isRead){
		this.isRead=isRead;
	}

	public Integer getIsRead(){
		return this.isRead;
	}

	public void setMoveType(String moveType){
		this.moveType=moveType;
	}

	public String getMoveType(){
		return this.moveType;
	}

}