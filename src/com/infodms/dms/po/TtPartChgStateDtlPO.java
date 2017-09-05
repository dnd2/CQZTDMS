/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-05-16 00:26:52
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartChgStateDtlPO extends PO{

	private Long colseQty;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Integer changeReason;
	private Integer status;
	private Long dtlId;
	private Long updateBy;
	private Long partId;
	private Long changeId;
	private Long locId;
	private Long disableBy;
	private Long deleteBy;
	private String remark2;
	private String unit;
	private Long returnQty;
	private Date disableDate;
	private Integer changeType;
	private Date deleteDate;
	private String partCode;
	private Long stockQty;
	private String partCname;
	private Integer ver;
	private String partOldcode;
	private Date createDate;
	private Long lineNo;
	private Long batchNo;
	private Long venderId;
	
	public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public void setColseQty(Long colseQty){
		this.colseQty=colseQty;
	}

	public Long getColseQty(){
		return this.colseQty;
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

	public void setChangeReason(Integer changeReason){
		this.changeReason=changeReason;
	}

	public Integer getChangeReason(){
		return this.changeReason;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setDtlId(Long dtlId){
		this.dtlId=dtlId;
	}

	public Long getDtlId(){
		return this.dtlId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setChangeId(Long changeId){
		this.changeId=changeId;
	}

	public Long getChangeId(){
		return this.changeId;
	}

	public void setLocId(Long locId){
		this.locId=locId;
	}

	public Long getLocId(){
		return this.locId;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setRemark2(String remark2){
		this.remark2=remark2;
	}

	public String getRemark2(){
		return this.remark2;
	}

	public void setUnit(String unit){
		this.unit=unit;
	}

	public String getUnit(){
		return this.unit;
	}

	public void setReturnQty(Long returnQty){
		this.returnQty=returnQty;
	}

	public Long getReturnQty(){
		return this.returnQty;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setChangeType(Integer changeType){
		this.changeType=changeType;
	}

	public Integer getChangeType(){
		return this.changeType;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setStockQty(Long stockQty){
		this.stockQty=stockQty;
	}

	public Long getStockQty(){
		return this.stockQty;
	}

	public void setPartCname(String partCname){
		this.partCname=partCname;
	}

	public String getPartCname(){
		return this.partCname;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setPartOldcode(String partOldcode){
		this.partOldcode=partOldcode;
	}

	public String getPartOldcode(){
		return this.partOldcode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setLineNo(Long lineNo){
		this.lineNo=lineNo;
	}

	public Long getLineNo(){
		return this.lineNo;
	}

}