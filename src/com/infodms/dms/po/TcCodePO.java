/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-06 14:20:00
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TcCodePO extends PO{

	private String type;
	private Date updateDate;
	private String codeId;
	private Long createBy;
	private Long ifDealer;
	private Date createDate;
	private Integer canModify;
	private Integer num;
	private Integer status;
	private Long updateBy;
	private Integer isDown;
	private String codeDesc;
	private String typeName;
	private Long ifVisible;
	private Integer codeLevel;
	private Long dealerId;
	
	private String codeParentId;
	

	public String getCodeParentId() {
		return codeParentId;
	}

	public void setCodeParentId(String codeParentId) {
		this.codeParentId = codeParentId;
	}

	public Integer getCodeLevel() {
		return codeLevel;
	}

	public void setCodeLevel(Integer codeLevel) {
		this.codeLevel = codeLevel;
	}

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public void setType(String type){
		this.type=type;
	}

	public String getType(){
		return this.type;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCodeId(String codeId){
		this.codeId=codeId;
	}

	public String getCodeId(){
		return this.codeId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setIfDealer(Long ifDealer){
		this.ifDealer=ifDealer;
	}

	public Long getIfDealer(){
		return this.ifDealer;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCanModify(Integer canModify){
		this.canModify=canModify;
	}

	public Integer getCanModify(){
		return this.canModify;
	}

	public void setNum(Integer num){
		this.num=num;
	}

	public Integer getNum(){
		return this.num;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setIsDown(Integer isDown){
		this.isDown=isDown;
	}

	public Integer getIsDown(){
		return this.isDown;
	}

	public void setCodeDesc(String codeDesc){
		this.codeDesc=codeDesc;
	}

	public String getCodeDesc(){
		return this.codeDesc;
	}

	public void setTypeName(String typeName){
		this.typeName=typeName;
	}

	public String getTypeName(){
		return this.typeName;
	}

	public void setIfVisible(Long ifVisible){
		this.ifVisible=ifVisible;
	}

	public Long getIfVisible(){
		return this.ifVisible;
	}

}