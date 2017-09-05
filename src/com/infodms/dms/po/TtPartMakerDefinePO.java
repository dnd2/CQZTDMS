/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-06-18 15:09:29
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartMakerDefinePO extends PO{

	private Integer state;
	private Integer isDefault;
	private Date disableDate;
	private Date deleteDate;
	private String makerName;
	private Date updateDate;
	private Long createBy;
	private Long makerId;
	private Integer isAbroad;
	private Integer status;
	private String makerCode;
	private Integer makerType;
	private Long updateBy;
	private String linkman;
	private String tel;
	private Long venderId;
	private Long deleteBy;
	private Long disableBy;
	private Date createDate;
	private String addr;
	private String fax;
	private String makerShotname;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setIsDefault(Integer isDefault){
		this.isDefault=isDefault;
	}

	public Integer getIsDefault(){
		return this.isDefault;
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

	public void setMakerName(String makerName){
		this.makerName=makerName;
	}

	public String getMakerName(){
		return this.makerName;
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

	public void setMakerId(Long makerId){
		this.makerId=makerId;
	}

	public Long getMakerId(){
		return this.makerId;
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

	public void setMakerCode(String makerCode){
		this.makerCode=makerCode;
	}

	public String getMakerCode(){
		return this.makerCode;
	}

	public void setMakerType(Integer makerType){
		this.makerType=makerType;
	}

	public Integer getMakerType(){
		return this.makerType;
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

	public void setVenderId(Long venderId){
		this.venderId=venderId;
	}

	public Long getVenderId(){
		return this.venderId;
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

	public String getMakerShotname() {
		return makerShotname;
	}

	public void setMakerShotname(String makerShotname) {
		this.makerShotname = makerShotname;
	}

}