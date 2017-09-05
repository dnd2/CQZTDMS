/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-07-02 14:31:48
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartDlrReturnMainPO extends PO{

	private Long createOrg;
	private Integer state;
	private Integer isWo;
	private Long dealerId;
	private Date updateDate;
	private String sellerName;
	private String remark;
	private Long createBy;
	private Date applyDate;
	private Integer status;
	private Long applyBy;
	private String soCode;
	private Long updateBy;
	private String sellerCode;
	private String returnCode;
	private Long disableBy;
	private Long deleteBy;
	private String wlno;
	private String remark1;
	private Integer orderType;
	private Date wlDate;
	private Date saleDate;
	private Date disableDate;
	private Date woDate;
	private Date deleteDate;
	private String dealerCode;
	private Long wlBy;
	private Long soId;
	private Long stockOut;
	private Double amount;
	private String wl;
	private String createOrgname;
	private Long stockIn;
	private Long verifyBy;
	private Date verifyDate;
	private String dealerName;
	private Long returnId;
	private String wlRemark;
	private Long inBy;
	private Date returnDate;
	private String invoNo;
	private Long sellerId;
	private Date inDate;
	private Integer ver;
	private Date createDate;
	private Long inId;
	private String inCode;
    private Integer verifyLevel;
    private Long vlOneBy;
    private Integer vlOneStatus;
    private Date vlOneDate;
    private Long vlTwoBy;
    private Integer vlTwoStatus;
    private Date vlTwoDate;
    private Integer verifyStatus;
    private String vlOneRemark;
    private String vlTwoRemark;
    
	public Integer getVerifyLevel() {
        return verifyLevel;
    }

    public String getVlOneRemark() {
        return vlOneRemark;
    }

    public void setVlOneRemark(String vlOneRemark) {
        this.vlOneRemark = vlOneRemark;
    }

    public String getVlTwoRemark() {
        return vlTwoRemark;
    }

    public void setVlTwoRemark(String vlTwoRemark) {
        this.vlTwoRemark = vlTwoRemark;
    }

    public void setVerifyLevel(Integer verifyLevel) {
        this.verifyLevel = verifyLevel;
    }

    public Long getVlOneBy() {
        return vlOneBy;
    }

    public void setVlOneBy(Long vlOneBy) {
        this.vlOneBy = vlOneBy;
    }

    public Integer getVlOneStatus() {
        return vlOneStatus;
    }

    public void setVlOneStatus(Integer vlOneStatus) {
        this.vlOneStatus = vlOneStatus;
    }

    public Date getVlOneDate() {
        return vlOneDate;
    }

    public void setVlOneDate(Date vlOneDate) {
        this.vlOneDate = vlOneDate;
    }

    public Long getVlTwoBy() {
        return vlTwoBy;
    }

    public void setVlTwoBy(Long vlTwoBy) {
        this.vlTwoBy = vlTwoBy;
    }

    public Integer getVlTwoStatus() {
        return vlTwoStatus;
    }

    public void setVlTwoStatus(Integer vlTwoStatus) {
        this.vlTwoStatus = vlTwoStatus;
    }

    public Date getVlTwoDate() {
        return vlTwoDate;
    }

    public void setVlTwoDate(Date vlTwoDate) {
        this.vlTwoDate = vlTwoDate;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Long getInId() {
        return inId;
    }

    public void setInId(Long inId) {
        this.inId = inId;
    }

    public String getInCode() {
        return inCode;
    }

    public void setInCode(String inCode) {
        this.inCode = inCode;
    }

    public void setCreateOrg(Long createOrg){
		this.createOrg=createOrg;
	}

	public Long getCreateOrg(){
		return this.createOrg;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setIsWo(Integer isWo){
		this.isWo=isWo;
	}

	public Integer getIsWo(){
		return this.isWo;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setSellerName(String sellerName){
		this.sellerName=sellerName;
	}

	public String getSellerName(){
		return this.sellerName;
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

	public void setApplyDate(Date applyDate){
		this.applyDate=applyDate;
	}

	public Date getApplyDate(){
		return this.applyDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setApplyBy(Long applyBy){
		this.applyBy=applyBy;
	}

	public Long getApplyBy(){
		return this.applyBy;
	}

	public void setSoCode(String soCode){
		this.soCode=soCode;
	}

	public String getSoCode(){
		return this.soCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setSellerCode(String sellerCode){
		this.sellerCode=sellerCode;
	}

	public String getSellerCode(){
		return this.sellerCode;
	}

	public void setReturnCode(String returnCode){
		this.returnCode=returnCode;
	}

	public String getReturnCode(){
		return this.returnCode;
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

	public void setWlno(String wlno){
		this.wlno=wlno;
	}

	public String getWlno(){
		return this.wlno;
	}

	public void setRemark1(String remark1){
		this.remark1=remark1;
	}

	public String getRemark1(){
		return this.remark1;
	}

	public void setOrderType(Integer orderType){
		this.orderType=orderType;
	}

	public Integer getOrderType(){
		return this.orderType;
	}

	public void setWlDate(Date wlDate){
		this.wlDate=wlDate;
	}

	public Date getWlDate(){
		return this.wlDate;
	}

	public void setSaleDate(Date saleDate){
		this.saleDate=saleDate;
	}

	public Date getSaleDate(){
		return this.saleDate;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setWoDate(Date woDate){
		this.woDate=woDate;
	}

	public Date getWoDate(){
		return this.woDate;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setWlBy(Long wlBy){
		this.wlBy=wlBy;
	}

	public Long getWlBy(){
		return this.wlBy;
	}

	public void setSoId(Long soId){
		this.soId=soId;
	}

	public Long getSoId(){
		return this.soId;
	}

	public void setStockOut(Long stockOut){
		this.stockOut=stockOut;
	}

	public Long getStockOut(){
		return this.stockOut;
	}

	public void setAmount(Double amount){
		this.amount=amount;
	}

	public Double getAmount(){
		return this.amount;
	}

	public void setWl(String wl){
		this.wl=wl;
	}

	public String getWl(){
		return this.wl;
	}

	public void setCreateOrgname(String createOrgname){
		this.createOrgname=createOrgname;
	}

	public String getCreateOrgname(){
		return this.createOrgname;
	}

	public void setStockIn(Long stockIn){
		this.stockIn=stockIn;
	}

	public Long getStockIn(){
		return this.stockIn;
	}

	public void setVerifyBy(Long verifyBy){
		this.verifyBy=verifyBy;
	}

	public Long getVerifyBy(){
		return this.verifyBy;
	}

	public void setVerifyDate(Date verifyDate){
		this.verifyDate=verifyDate;
	}

	public Date getVerifyDate(){
		return this.verifyDate;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setReturnId(Long returnId){
		this.returnId=returnId;
	}

	public Long getReturnId(){
		return this.returnId;
	}

	public void setWlRemark(String wlRemark){
		this.wlRemark=wlRemark;
	}

	public String getWlRemark(){
		return this.wlRemark;
	}

	public void setInBy(Long inBy){
		this.inBy=inBy;
	}

	public Long getInBy(){
		return this.inBy;
	}

	public void setReturnDate(Date returnDate){
		this.returnDate=returnDate;
	}

	public Date getReturnDate(){
		return this.returnDate;
	}

	public void setInvoNo(String invoNo){
		this.invoNo=invoNo;
	}

	public String getInvoNo(){
		return this.invoNo;
	}

	public void setSellerId(Long sellerId){
		this.sellerId=sellerId;
	}

	public Long getSellerId(){
		return this.sellerId;
	}

	public void setInDate(Date inDate){
		this.inDate=inDate;
	}

	public Date getInDate(){
		return this.inDate;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}