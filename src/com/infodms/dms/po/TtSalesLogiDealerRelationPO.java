/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-06-19 15:27:28
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesLogiDealerRelationPO extends PO{

	private String logiCode;
	private Long dealerId;
	private String conPer;
	private String address;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private String logiFullName;
	private String logiName;
	private Long status;
	private Long logiId;
	private Long updateBy;
	private String conTel;
	private Long id;
	private Date createDate;
	private Long yieldly;
	private String corporation;
	private Long poseId;

	public Long getPoseId() {
		return poseId;
	}

	public void setPoseId(Long poseId) {
		this.poseId = poseId;
	}

	public void setLogiCode(String logiCode){
		this.logiCode=logiCode;
	}

	public String getLogiCode(){
		return this.logiCode;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setConPer(String conPer){
		this.conPer=conPer;
	}

	public String getConPer(){
		return this.conPer;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getAddress(){
		return this.address;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setLogiFullName(String logiFullName){
		this.logiFullName=logiFullName;
	}

	public String getLogiFullName(){
		return this.logiFullName;
	}

	public void setLogiName(String logiName){
		this.logiName=logiName;
	}

	public String getLogiName(){
		return this.logiName;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setLogiId(Long logiId){
		this.logiId=logiId;
	}

	public Long getLogiId(){
		return this.logiId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setConTel(String conTel){
		this.conTel=conTel;
	}

	public String getConTel(){
		return this.conTel;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
	}

	public void setCorporation(String corporation){
		this.corporation=corporation;
	}

	public String getCorporation(){
		return this.corporation;
	}

}