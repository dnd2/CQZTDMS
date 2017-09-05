/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-06-03 10:31:29
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TcPosePO extends PO{

	private Long companyId;
	private Integer poseStatus;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Long logiId;
	private Long orgId;
	private Long updateBy;
	private Integer poseType;
	private Long poseId;
	private String poseName;
	private Date createDate;
	private String poseCode;
	private Integer poseBusType;
	private Integer chooseDealer;
	
	private Long poseRank;
	private Long parPoseId;

	public Long getParPoseId() {
		return parPoseId;
	}

	public void setParPoseId(Long parPoseId) {
		this.parPoseId = parPoseId;
	}

	public Long getPoseRank() {
		return poseRank;
	}

	public void setPoseRank(Long poseRank) {
		this.poseRank = poseRank;
	}

	public Integer getChooseDealer() {
		return chooseDealer;
	}

	public void setChooseDealer(Integer chooseDealer) {
		this.chooseDealer = chooseDealer;
	}

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setPoseStatus(Integer poseStatus){
		this.poseStatus=poseStatus;
	}

	public Integer getPoseStatus(){
		return this.poseStatus;
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

	public void setLogiId(Long logiId){
		this.logiId=logiId;
	}

	public Long getLogiId(){
		return this.logiId;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPoseType(Integer poseType){
		this.poseType=poseType;
	}

	public Integer getPoseType(){
		return this.poseType;
	}

	public void setPoseId(Long poseId){
		this.poseId=poseId;
	}

	public Long getPoseId(){
		return this.poseId;
	}

	public void setPoseName(String poseName){
		this.poseName=poseName;
	}

	public String getPoseName(){
		return this.poseName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPoseCode(String poseCode){
		this.poseCode=poseCode;
	}

	public String getPoseCode(){
		return this.poseCode;
	}

	public void setPoseBusType(Integer poseBusType){
		this.poseBusType=poseBusType;
	}

	public Integer getPoseBusType(){
		return this.poseBusType;
	}

}