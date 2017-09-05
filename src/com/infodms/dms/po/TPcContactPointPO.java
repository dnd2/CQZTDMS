/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-06 11:03:41
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcContactPointPO extends PO{

	private Date pointDate;
	private Long pointId;
	private Date updateDate;
	private Long status;
	private Long pointWay;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private String pointContent;
	private String adviser;
	private String dealerId;
	private Long ctmId;

	public Long getCtmId() {
		return ctmId;
	}

	public void setCtmId(Long ctmId) {
		this.ctmId = ctmId;
	}

	public String getAdviser() {
		return adviser;
	}

	public void setAdviser(String adviser) {
		this.adviser = adviser;
	}

	public String getDealerId() {
		return dealerId;
	}

	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}

	public void setPointDate(Date pointDate){
		this.pointDate=pointDate;
	}

	public Date getPointDate(){
		return this.pointDate;
	}

	public void setPointId(Long pointId){
		this.pointId=pointId;
	}

	public Long getPointId(){
		return this.pointId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setPointWay(Long pointWay){
		this.pointWay=pointWay;
	}

	public Long getPointWay(){
		return this.pointWay;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPointContent(String pointContent){
		this.pointContent=pointContent;
	}

	public String getPointContent(){
		return this.pointContent;
	}

}