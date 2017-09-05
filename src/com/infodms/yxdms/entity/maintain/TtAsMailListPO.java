/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-03-17 18:31:12
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.maintain;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsMailListPO extends PO{

	private String area;
	private String userPhone;
	private String userName;
	private Long id;
	private Long createBy;
	private Date createDate;
	private String positionName;
	private String positionDuty;

	public void setArea(String area){
		this.area=area;
	}

	public String getArea(){
		return this.area;
	}

	public void setUserPhone(String userPhone){
		this.userPhone=userPhone;
	}

	public String getUserPhone(){
		return this.userPhone;
	}

	public void setUserName(String userName){
		this.userName=userName;
	}

	public String getUserName(){
		return this.userName;
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

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getPositionDuty() {
		return positionDuty;
	}

	public void setPositionDuty(String positionDuty) {
		this.positionDuty = positionDuty;
	}

}