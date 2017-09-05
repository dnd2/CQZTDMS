/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-10-28 15:44:37
* CreateBy   : Iverson
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrCheckApplicationPO extends PO{

	private String updateBy;
	private Date updateDate;
	private Integer checkCount;
	private Long id;
	private String createBy;
	private Date checkDate;
	private String balanceNo;
	private Date createDate;
	private Integer status;
	private Long delaerId;
	private String checkNo;
	private Integer isFrint;
	private Integer sb_status;
	
	private Date authDate;

	public Integer getSb_status() {
		return sb_status;
	}

	public void setSb_status(Integer sbStatus) {
		sb_status = sbStatus;
	}

	public Date getAuthDate() {
		return authDate;
	}

	public void setAuthDate(Date authDate) {
		this.authDate = authDate;
	}

	public Integer getIsFrint() {
		return isFrint;
	}

	public void setIsFrint(Integer isFrint) {
		this.isFrint = isFrint;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCheckCount(Integer checkCount){
		this.checkCount=checkCount;
	}

	public Integer getCheckCount(){
		return this.checkCount;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setCheckDate(Date checkDate){
		this.checkDate=checkDate;
	}

	public Date getCheckDate(){
		return this.checkDate;
	}

	public void setBalanceNo(String balanceNo){
		this.balanceNo=balanceNo;
	}

	public String getBalanceNo(){
		return this.balanceNo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setDelaerId(Long delaerId){
		this.delaerId=delaerId;
	}

	public Long getDelaerId(){
		return this.delaerId;
	}

	public void setCheckNo(String checkNo){
		this.checkNo=checkNo;
	}

	public String getCheckNo(){
		return this.checkNo;
	}

}