/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-22 15:42:50
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCrmQueDetailPO extends PO{

	private Long qrId;
	private Integer qdNo;
	private String qdQuestion;
	private Date updateDate;
	private Long createBy;
	private Integer qdPoints;
	private Integer edTxtHight;
	private Long qdId;
	private Integer qdTxtType;
	private Integer var;
	private Integer qdQueType;
	private Integer edTxtWidth;
	private Long updateBy;
	private Date createDate;
	private String qdChoice;
	private Integer qdStatus;

	public void setQrId(Long qrId){
		this.qrId=qrId;
	}

	public Long getQrId(){
		return this.qrId;
	}

	public void setQdNo(Integer qdNo){
		this.qdNo=qdNo;
	}

	public Integer getQdNo(){
		return this.qdNo;
	}

	public void setQdQuestion(String qdQuestion){
		this.qdQuestion=qdQuestion;
	}

	public String getQdQuestion(){
		return this.qdQuestion;
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

	public void setQdPoints(Integer qdPoints){
		this.qdPoints=qdPoints;
	}

	public Integer getQdPoints(){
		return this.qdPoints;
	}

	public void setEdTxtHight(Integer edTxtHight){
		this.edTxtHight=edTxtHight;
	}

	public Integer getEdTxtHight(){
		return this.edTxtHight;
	}

	public void setQdId(Long qdId){
		this.qdId=qdId;
	}

	public Long getQdId(){
		return this.qdId;
	}

	public void setQdTxtType(Integer qdTxtType){
		this.qdTxtType=qdTxtType;
	}

	public Integer getQdTxtType(){
		return this.qdTxtType;
	}

	public void setVar(Integer var){
		this.var=var;
	}

	public Integer getVar(){
		return this.var;
	}

	public void setQdQueType(Integer qdQueType){
		this.qdQueType=qdQueType;
	}

	public Integer getQdQueType(){
		return this.qdQueType;
	}

	public void setEdTxtWidth(Integer edTxtWidth){
		this.edTxtWidth=edTxtWidth;
	}

	public Integer getEdTxtWidth(){
		return this.edTxtWidth;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setQdChoice(String qdChoice){
		this.qdChoice=qdChoice;
	}

	public String getQdChoice(){
		return this.qdChoice;
	}

	public Integer getQdStatus() {
		return qdStatus;
	}

	public void setQdStatus(Integer qdStatus) {
		this.qdStatus = qdStatus;
	}
}