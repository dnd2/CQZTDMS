/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-09-11 11:47:48
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartLogisticsPO extends PO{

	private Long state;
	private String logiCode;
	private String conPer;
	private Date updateDate;
	private String address;
	private String logiFullName;
	private String remark;
	private Long createBy;
	private String logiName;
	private Long status;
	private Long logiId;
	private Double weightRatio;
	private Long updateBy;
	private String conTel;
	private Double miniWeight;
	private Date createDate;
	private String corporation;

	public void setState(Long state){
		this.state=state;
	}

	public Long getState(){
		return this.state;
	}

	public void setLogiCode(String logiCode){
		this.logiCode=logiCode;
	}

	public String getLogiCode(){
		return this.logiCode;
	}

	public void setConPer(String conPer){
		this.conPer=conPer;
	}

	public String getConPer(){
		return this.conPer;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getAddress(){
		return this.address;
	}

	public void setLogiFullName(String logiFullName){
		this.logiFullName=logiFullName;
	}

	public String getLogiFullName(){
		return this.logiFullName;
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

	public void setWeightRatio(Double weightRatio){
		this.weightRatio=weightRatio;
	}

	public Double getWeightRatio(){
		return this.weightRatio;
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

	public void setMiniWeight(Double miniWeight){
		this.miniWeight=miniWeight;
	}

	public Double getMiniWeight(){
		return this.miniWeight;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCorporation(String corporation){
		this.corporation=corporation;
	}

	public String getCorporation(){
		return this.corporation;
	}

}