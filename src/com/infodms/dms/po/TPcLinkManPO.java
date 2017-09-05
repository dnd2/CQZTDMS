/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-13 17:33:16
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcLinkManPO extends PO{

	private Long ctmId;
	private Long linkId;
	private String linkMan;
	private Date updateDate;
	private Long status;
	private String linkPhone;
	private Long cardType;
	private Long updateBy;
	private Long createBy;
	private String cardCode;
	private Date createDate;
	private String relationship;
	private String relationCode;
	private String oldVehicleId;

	public String getRelationCode() {
		return relationCode;
	}

	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}

	public String getOldVehicleId() {
		return oldVehicleId;
	}

	public void setOldVehicleId(String oldVehicleId) {
		this.oldVehicleId = oldVehicleId;
	}

	public void setCtmId(Long ctmId){
		this.ctmId=ctmId;
	}

	public Long getCtmId(){
		return this.ctmId;
	}

	public void setLinkId(Long linkId){
		this.linkId=linkId;
	}

	public Long getLinkId(){
		return this.linkId;
	}

	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}

	public String getLinkMan(){
		return this.linkMan;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setLinkPhone(String linkPhone){
		this.linkPhone=linkPhone;
	}

	public String getLinkPhone(){
		return this.linkPhone;
	}

	public void setCardType(Long cardType){
		this.cardType=cardType;
	}

	public Long getCardType(){
		return this.cardType;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCardCode(String cardCode){
		this.cardCode=cardCode;
	}

	public String getCardCode(){
		return this.cardCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setRelationship(String relationship){
		this.relationship=relationship;
	}

	public String getRelationship(){
		return this.relationship;
	}

}