/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-23 16:32:28
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmSeatsPO extends PO{

	private Integer seWorkStatus;
	private Integer seIsManamger;
	private Date updateDate;
	private Long stId;
	private Long createBy;
	private String seName;
	private String seIp;
	private Integer seIsSeats;
	private String seSeatsNo;
	private Long seId;
	private Integer var;
	private Long updateBy;
	private Integer seStatus;
	private Date createDate;
	private Long seUserId;
	private String seAccount;
	private Integer seLevel;
	private Long seExt;

	public void setSeWorkStatus(Integer seWorkStatus){
		this.seWorkStatus=seWorkStatus;
	}

	public Integer getSeWorkStatus(){
		return this.seWorkStatus;
	}

	public void setSeIsManamger(Integer seIsManamger){
		this.seIsManamger=seIsManamger;
	}

	public Integer getSeIsManamger(){
		return this.seIsManamger;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStId(Long stId){
		this.stId=stId;
	}

	public Long getStId(){
		return this.stId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setSeName(String seName){
		this.seName=seName;
	}

	public String getSeName(){
		return this.seName;
	}

	public void setSeIp(String seIp){
		this.seIp=seIp;
	}

	public String getSeIp(){
		return this.seIp;
	}

	public void setSeIsSeats(Integer seIsSeats){
		this.seIsSeats=seIsSeats;
	}

	public Integer getSeIsSeats(){
		return this.seIsSeats;
	}

	public void setSeSeatsNo(String seSeatsNo){
		this.seSeatsNo=seSeatsNo;
	}

	public String getSeSeatsNo(){
		return this.seSeatsNo;
	}

	public void setSeId(Long seId){
		this.seId=seId;
	}

	public Long getSeId(){
		return this.seId;
	}

	public void setVar(Integer var){
		this.var=var;
	}

	public Integer getVar(){
		return this.var;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setSeStatus(Integer seStatus){
		this.seStatus=seStatus;
	}

	public Integer getSeStatus(){
		return this.seStatus;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSeUserId(Long seUserId){
		this.seUserId=seUserId;
	}

	public Long getSeUserId(){
		return this.seUserId;
	}

	public void setSeAccount(String seAccount){
		this.seAccount=seAccount;
	}

	public String getSeAccount(){
		return this.seAccount;
	}

	public void setSeLevel(Integer seLevel){
		this.seLevel=seLevel;
	}

	public Integer getSeLevel(){
		return this.seLevel;
	}

	public void setSeExt(Long seExt){
		this.seExt=seExt;
	}

	public Long getSeExt(){
		return this.seExt;
	}

}