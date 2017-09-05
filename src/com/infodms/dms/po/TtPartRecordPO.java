/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-02 13:50:45
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartRecordPO extends PO{

	private String locCode;
	private Integer state;
	private String orgName;
	private Long configId;
	private String partName;
	private Long partNum;
	private String whCode;
	private Long recordId;
	private Long partId;
	private Long venderId;
	private Long locId;
	private Integer orgKind;
	private String partBatch;
	private Long lineId;
	private Integer partState;
	private String orderCode;
	private String personName;
	private String partCode;
	private Date optDate;
	private Long whId;
	private Long orgId;
	private String locName;
	private Long orderId;
	private Long personId;
	private Long transtypeId;
	private String whName;
	private Long orgdumId;
	private String partOldcode;
	private String orgCode;
	private Date createDate;
	private Integer addFlag;
/*	private String batchNo;

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}*/

	public void setLocCode(String locCode){
		this.locCode=locCode;
	}

	public String getLocCode(){
		return this.locCode;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setOrgName(String orgName){
		this.orgName=orgName;
	}

	public String getOrgName(){
		return this.orgName;
	}

	public void setConfigId(Long configId){
		this.configId=configId;
	}

	public Long getConfigId(){
		return this.configId;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setPartNum(Long partNum){
		this.partNum=partNum;
	}

	public Long getPartNum(){
		return this.partNum;
	}

	public void setWhCode(String whCode){
		this.whCode=whCode;
	}

	public String getWhCode(){
		return this.whCode;
	}

	public void setRecordId(Long recordId){
		this.recordId=recordId;
	}

	public Long getRecordId(){
		return this.recordId;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setVenderId(Long venderId){
		this.venderId=venderId;
	}

	public Long getVenderId(){
		return this.venderId;
	}

	public void setLocId(Long locId){
		this.locId=locId;
	}

	public Long getLocId(){
		return this.locId;
	}

	public void setOrgKind(Integer orgKind){
		this.orgKind=orgKind;
	}

	public Integer getOrgKind(){
		return this.orgKind;
	}

	public void setPartBatch(String partBatch){
		this.partBatch=partBatch;
	}

	public String getPartBatch(){
		return this.partBatch;
	}

	public void setLineId(Long lineId){
		this.lineId=lineId;
	}

	public Long getLineId(){
		return this.lineId;
	}

	public void setPartState(Integer partState){
		this.partState=partState;
	}

	public Integer getPartState(){
		return this.partState;
	}

	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}

	public String getOrderCode(){
		return this.orderCode;
	}

	public void setPersonName(String personName){
		this.personName=personName;
	}

	public String getPersonName(){
		return this.personName;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setOptDate(Date optDate){
		this.optDate=optDate;
	}

	public Date getOptDate(){
		return this.optDate;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setLocName(String locName){
		this.locName=locName;
	}

	public String getLocName(){
		return this.locName;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setPersonId(Long personId){
		this.personId=personId;
	}

	public Long getPersonId(){
		return this.personId;
	}

	public void setTranstypeId(Long transtypeId){
		this.transtypeId=transtypeId;
	}

	public Long getTranstypeId(){
		return this.transtypeId;
	}

	public void setWhName(String whName){
		this.whName=whName;
	}

	public String getWhName(){
		return this.whName;
	}

	public void setOrgdumId(Long orgdumId){
		this.orgdumId=orgdumId;
	}

	public Long getOrgdumId(){
		return this.orgdumId;
	}

	public void setPartOldcode(String partOldcode){
		this.partOldcode=partOldcode;
	}

	public String getPartOldcode(){
		return this.partOldcode;
	}

	public void setOrgCode(String orgCode){
		this.orgCode=orgCode;
	}

	public String getOrgCode(){
		return this.orgCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAddFlag(Integer addFlag){
		this.addFlag=addFlag;
	}

	public Integer getAddFlag(){
		return this.addFlag;
	}

}