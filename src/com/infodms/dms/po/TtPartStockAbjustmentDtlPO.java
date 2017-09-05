/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-07-25 11:10:44
* CreateBy   : MEpaper
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartStockAbjustmentDtlPO extends PO{

	private String locCode;
	private String batchCode;
	private Long dltId;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Integer dealStatus;
	private Long abjustmentId;
	private String partCode;
	private String locName;
	private String partCname;
	private Long updateBy;
	private Long partId;
	private Long locId;
	private Long stockId;
	private Long bookId;
    private Long itemQty;
	private Date createDate;
	private Long abjustmentNum;
	private Integer checkStatus;
	private Long normalQty;
	private String partOldcode;
	private Integer status;
	
    public Integer getStatus() {
        return status;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPartOldcode() {
        return partOldcode;
    }

    public void setPartOldcode(String partOldcode) {
        this.partOldcode = partOldcode;
    }

    public Long getNormalQty() {
        return normalQty;
    }

    public void setNormalQty(Long normalQty) {
        this.normalQty = normalQty;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public void setLocCode(String locCode){
		this.locCode=locCode;
	}

	public String getLocCode(){
		return this.locCode;
	}

	public void setBatchCode(String batchCode){
		this.batchCode=batchCode;
	}

	public String getBatchCode(){
		return this.batchCode;
	}

	public void setDltId(Long dltId){
		this.dltId=dltId;
	}

	public Long getDltId(){
		return this.dltId;
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

	public void setDealStatus(Integer dealStatus){
		this.dealStatus=dealStatus;
	}

	public Integer getDealStatus(){
		return this.dealStatus;
	}

	public void setAbjustmentId(Long abjustmentId){
		this.abjustmentId=abjustmentId;
	}

	public Long getAbjustmentId(){
		return this.abjustmentId;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setLocName(String locName){
		this.locName=locName;
	}

	public String getLocName(){
		return this.locName;
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

	public void setLocId(Long locId){
		this.locId=locId;
	}

	public Long getLocId(){
		return this.locId;
	}

	public void setItemQty(Long itemQty){
		this.itemQty=itemQty;
	}

	public Long getItemQty(){
		return this.itemQty;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAbjustmentNum(Long abjustmentNum){
		this.abjustmentNum=abjustmentNum;
	}

	public Long getAbjustmentNum(){
		return this.abjustmentNum;
	}

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

}