/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-08-07 15:43:34
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartVenderDefinePO extends PO{

	private Integer state;
	private Date disableDate;
	private Date deleteDate;
	private Date updateDate;
	private String venderCode;
	private Long createBy;
	private Integer isAbroad;
	private Integer status;
	private Integer invType;
	private Long updateBy;
	private String linkman;
	private String tel;
	private String venderName;
	private Long venderId;
	private Integer venderType;
	private Long deleteBy;
	private Long disableBy;
	private Date createDate;
	private String addr;
	private String fax;
	
	private Integer isSuspend;

	public Integer getIsSuspend() {
		return isSuspend;
	}

	public void setIsSuspend(Integer isSuspend) {
		this.isSuspend = isSuspend;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
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

	public void setVenderCode(String venderCode){
		this.venderCode=venderCode;
	}

	public String getVenderCode(){
		return this.venderCode;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setIsAbroad(Integer isAbroad){
		this.isAbroad=isAbroad;
	}

	public Integer getIsAbroad(){
		return this.isAbroad;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setInvType(Integer invType){
		this.invType=invType;
	}

	public Integer getInvType(){
		return this.invType;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setLinkman(String linkman){
		this.linkman=linkman;
	}

	public String getLinkman(){
		return this.linkman;
	}

	public void setTel(String tel){
		this.tel=tel;
	}

	public String getTel(){
		return this.tel;
	}

	public void setVenderName(String venderName){
		this.venderName=venderName;
	}

	public String getVenderName(){
		return this.venderName;
	}

	public void setVenderId(Long venderId){
		this.venderId=venderId;
	}

	public Long getVenderId(){
		return this.venderId;
	}

	public void setVenderType(Integer venderType){
		this.venderType=venderType;
	}

	public Integer getVenderType(){
		return this.venderType;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAddr(String addr){
		this.addr=addr;
	}

	public String getAddr(){
		return this.addr;
	}

	public void setFax(String fax){
		this.fax=fax;
	}

	public String getFax(){
		return this.fax;
	}

}