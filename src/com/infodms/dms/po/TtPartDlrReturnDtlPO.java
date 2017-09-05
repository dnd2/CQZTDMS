/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-06-03 20:33:48
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartDlrReturnDtlPO extends PO{

	private Double buyAmount;
	private Long inlocId;
	private Long slineId;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Integer status;
	private Long outQty;
	private Long dtlId;
	private Long salesQty;
	private Long updateBy;
	private Long partId;
	private Long disableBy;
	private Long deleteBy;
	private String unit;
	private Long returnQty;
	private Date disableDate;
	private Date deleteDate;
	private String partCode;
	private Long stockQty;
	private Long checkQty;
	private Long returnId;
	private String partCname;
	private Long outlocId;
	private Double buyPrice;
	private Long inQty;
	private Long applyQty;
	private Integer ver;
	private String partOldcode;
	private Date createDate;
	private Long lineNo;

    private Long unlocQty;
    private Long kyQty;
    private Integer isUnloc;
    

	public Long getUnlocQty() {
        return unlocQty;
    }

    public void setUnlocQty(Long unlocQty) {
        this.unlocQty = unlocQty;
    }

    public Long getKyQty() {
        return kyQty;
    }

    public void setKyQty(Long kyQty) {
        this.kyQty = kyQty;
    }

    public Integer getIsUnloc() {
        return isUnloc;
    }

    public void setIsUnloc(Integer isUnloc) {
        this.isUnloc = isUnloc;
    }

    public void setBuyAmount(Double buyAmount){
		this.buyAmount=buyAmount;
	}

	public Double getBuyAmount(){
		return this.buyAmount;
	}

	public void setInlocId(Long inlocId){
		this.inlocId=inlocId;
	}

	public Long getInlocId(){
		return this.inlocId;
	}

	public void setSlineId(Long slineId){
		this.slineId=slineId;
	}

	public Long getSlineId(){
		return this.slineId;
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

	public void setOutQty(Long outQty){
		this.outQty=outQty;
	}

	public Long getOutQty(){
		return this.outQty;
	}

	public void setDtlId(Long dtlId){
		this.dtlId=dtlId;
	}

	public Long getDtlId(){
		return this.dtlId;
	}

	public void setSalesQty(Long salesQty){
		this.salesQty=salesQty;
	}

	public Long getSalesQty(){
		return this.salesQty;
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

	public void setCheckQty(Long checkQty){
		this.checkQty=checkQty;
	}

	public Long getCheckQty(){
		return this.checkQty;
	}

	public void setReturnId(Long returnId){
		this.returnId=returnId;
	}

	public Long getReturnId(){
		return this.returnId;
	}

	public void setPartCname(String partCname){
		this.partCname=partCname;
	}

	public String getPartCname(){
		return this.partCname;
	}

	public void setOutlocId(Long outlocId){
		this.outlocId=outlocId;
	}

	public Long getOutlocId(){
		return this.outlocId;
	}

	public void setBuyPrice(Double buyPrice){
		this.buyPrice=buyPrice;
	}

	public Double getBuyPrice(){
		return this.buyPrice;
	}

	public void setInQty(Long inQty){
		this.inQty=inQty;
	}

	public Long getInQty(){
		return this.inQty;
	}

	public void setApplyQty(Long applyQty){
		this.applyQty=applyQty;
	}

	public Long getApplyQty(){
		return this.applyQty;
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