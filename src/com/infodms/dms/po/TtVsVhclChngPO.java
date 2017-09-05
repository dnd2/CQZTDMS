/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-02 17:20:16
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsVhclChngPO extends PO{

	private Long dealerId;
	private String changeMemo;
	private Integer orgType;
	private Date updateDate;
	private Long createBy;
	private Integer changeCode;
	private Long orgId;
	private Long vhclChangeId;
	private Long updateBy;
	private String changeName;
	private Date changeDate;
	private String docNo;
	private Date createDate;
	private String changeDesc;
	private Long vehicleId;

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setChangeMemo(String changeMemo){
		this.changeMemo=changeMemo;
	}

	public String getChangeMemo(){
		return this.changeMemo;
	}

	public void setOrgType(Integer orgType){
		this.orgType=orgType;
	}

	public Integer getOrgType(){
		return this.orgType;
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

	public void setChangeCode(Integer changeCode){
		this.changeCode=changeCode;
	}

	public Integer getChangeCode(){
		return this.changeCode;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setVhclChangeId(Long vhclChangeId){
		this.vhclChangeId=vhclChangeId;
	}

	public Long getVhclChangeId(){
		return this.vhclChangeId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setChangeName(String changeName){
		this.changeName=changeName;
	}

	public String getChangeName(){
		return this.changeName;
	}

	public void setChangeDate(Date changeDate){
		this.changeDate=changeDate;
	}

	public Date getChangeDate(){
		return this.changeDate;
	}

	public void setDocNo(String docNo){
		this.docNo=docNo;
	}

	public String getDocNo(){
		return this.docNo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setChangeDesc(String changeDesc){
		this.changeDesc=changeDesc;
	}

	public String getChangeDesc(){
		return this.changeDesc;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

}