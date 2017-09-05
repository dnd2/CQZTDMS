/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-09 14:17:14
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesLogiPO extends PO{

	private Long yieldly;
	private Date updateDate;
	private Long logiId;
	private String conTel;
	private String corporation;
	private Long createBy;
	private String logiName;
	private Date createDate;
	private String logiFullName;
	private String logiCode;
	private Long status;
	private Long updateBy;
	private String conPer;
	private String remark;
	private String address;
	private Integer isStatus;

	public Integer getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(Integer isStatus) {
		this.isStatus = isStatus;
	}

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setLogiId(Long logiId){
		this.logiId=logiId;
	}

	public Long getLogiId(){
		return this.logiId;
	}

	public void setConTel(String conTel){
		this.conTel=conTel;
	}

	public String getConTel(){
		return this.conTel;
	}

	public void setCorporation(String corporation){
		this.corporation=corporation;
	}

	public String getCorporation(){
		return this.corporation;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setLogiName(String logiName){
		this.logiName=logiName;
	}

	public String getLogiName(){
		return this.logiName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setLogiFullName(String logiFullName){
		this.logiFullName=logiFullName;
	}

	public String getLogiFullName(){
		return this.logiFullName;
	}

	public void setLogiCode(String logiCode){
		this.logiCode=logiCode;
	}

	public String getLogiCode(){
		return this.logiCode;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setConPer(String conPer){
		this.conPer=conPer;
	}

	public String getConPer(){
		return this.conPer;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getAddress(){
		return this.address;
	}

}