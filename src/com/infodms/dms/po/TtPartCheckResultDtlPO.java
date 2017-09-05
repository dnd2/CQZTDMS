/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-06 11:44:39
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartCheckResultDtlPO extends PO{

	private Long bookQty;
	private Date updateDate;
	private Long resultId;
	private String remark;
	private Long createBy;
	private Integer status;
	private Long normalQty;
	private Long dtlId;
	private Integer checkResult;
	private Long updateBy;
	private Long partId;
	private Long disableBy;
	private Long deleteBy;
	private String unit;
	private Integer isOver;
	private Date disableDate;
	private Date deleteDate;
	private Long mbQty;
	private String partCode;
	private Long diffQty;
	private Long checkQty;
	private String partCname;
	private Integer ver;
	private String partOldcode;
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

    public void setBookQty(Long bookQty) {
        this.bookQty = bookQty;
    }

    public Long getBookQty(){
		return this.bookQty;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setResultId(Long resultId){
		this.resultId=resultId;
	}

	public Long getResultId(){
		return this.resultId;
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

	public void setNormalQty(Long normalQty){
		this.normalQty=normalQty;
	}

	public Long getNormalQty(){
		return this.normalQty;
	}

	public void setDtlId(Long dtlId){
		this.dtlId=dtlId;
	}

	public Long getDtlId(){
		return this.dtlId;
	}

	public void setCheckResult(Integer checkResult){
		this.checkResult=checkResult;
	}

	public Integer getCheckResult(){
		return this.checkResult;
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

	public void setUnit(String unit){
		this.unit=unit;
	}

	public String getUnit(){
		return this.unit;
	}

	public void setIsOver(Integer isOver){
		this.isOver=isOver;
	}

	public Integer getIsOver(){
		return this.isOver;
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

	public void setMbQty(Long mbQty){
		this.mbQty=mbQty;
	}

	public Long getMbQty(){
		return this.mbQty;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setDiffQty(Long diffQty){
		this.diffQty=diffQty;
	}

	public Long getDiffQty(){
		return this.diffQty;
	}

	public void setCheckQty(Long checkQty){
		this.checkQty=checkQty;
	}

	public Long getCheckQty(){
		return this.checkQty;
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