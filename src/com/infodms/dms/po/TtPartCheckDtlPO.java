/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-03 15:28:02
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartCheckDtlPO extends PO{

	private Long checkId;
	private String unit;
	private Date disableDate;
	private Date deleteDate;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Integer status;
	private String partCode;
	private Long stockQty;
	private Long dtlId;
	private String partCname;
	private Long updateBy;
	private Long partId;
	private Integer ver;
	private String partOldcode;
	private Long deleteBy;
	private Long disableBy;
	private Date createDate;
	private Long lineNo;
	private Long locId;
	private String locCode;
	private String locName;
	private Long venderId;
	private String batchCode;
	
	public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Long getLocId() {
        return locId;
    }

    public void setLocId(Long locId) {
        this.locId = locId;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public void setCheckId(Long checkId){
		this.checkId=checkId;
	}

	public Long getCheckId(){
		return this.checkId;
	}

	public void setUnit(String unit){
		this.unit=unit;
	}

	public String getUnit(){
		return this.unit;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
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

	public void setDtlId(Long dtlId){
		this.dtlId=dtlId;
	}

	public Long getDtlId(){
		return this.dtlId;
	}

	public void setPartCname(String partCname){
		this.partCname=partCname;
	}

	public String getPartCname(){
		return this.partCname;
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

	public void setLineNo(Long lineNo){
		this.lineNo=lineNo;
	}

	public Long getLineNo(){
		return this.lineNo;
	}

}