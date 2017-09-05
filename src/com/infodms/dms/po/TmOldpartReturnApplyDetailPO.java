/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-18 11:04:31
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmOldpartReturnApplyDetailPO extends PO{

	private String claimNo;
	private Date updateDate;
	private String partName;
	private String remark;
	private Long createBy;
	private Long detailId;
	private String partCode;
	private String barcodeNo;
	private String returnNo;
	private Integer isAgree;
	private Long returnDetailId;
	private Long updateBy;
	private Integer deductRemark;
	private Date createDate;
	private Long returnApplyId;

	public void setClaimNo(String claimNo){
		this.claimNo=claimNo;
	}

	public String getClaimNo(){
		return this.claimNo;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
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

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setBarcodeNo(String barcodeNo){
		this.barcodeNo=barcodeNo;
	}

	public String getBarcodeNo(){
		return this.barcodeNo;
	}

	public void setReturnNo(String returnNo){
		this.returnNo=returnNo;
	}

	public String getReturnNo(){
		return this.returnNo;
	}

	public void setIsAgree(Integer isAgree){
		this.isAgree=isAgree;
	}

	public Integer getIsAgree(){
		return this.isAgree;
	}

	public void setReturnDetailId(Long returnDetailId){
		this.returnDetailId=returnDetailId;
	}

	public Long getReturnDetailId(){
		return this.returnDetailId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}



	public Integer getDeductRemark() {
		return deductRemark;
	}

	public void setDeductRemark(Integer deductRemark) {
		this.deductRemark = deductRemark;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setReturnApplyId(Long returnApplyId){
		this.returnApplyId=returnApplyId;
	}

	public Long getReturnApplyId(){
		return this.returnApplyId;
	}

}