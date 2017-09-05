/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-23 17:20:27
* CreateBy   : ZLD
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrDeductPO extends PO{

	private Long dealerId;
	private Date updateDate;
	private Date noticeDate;
	private Long createBy;
	private String remark;
	private Integer partAmount;
	private Double materialMoney;
	private Integer isDel;
	private Integer deductAmount;
	private Integer noticeStatus;
	private Double otherMoney;
	private Integer isBal;
	private Long oemCompanyId;
	private Long updateBy;
	private Long claimId;
	private Long id;
	private String deductNo;
	private Date createDate;
	private Double manhourMoney;
	/***********Iverson add By 2010-12-24 添加旧件和旧件结算汇总关系********************/
	private Long deductBalanceId;
	
	private Long isCount;
	
	public Long getIsCount() {
		return isCount;
	}
	public void setIsCount(Long isCount) {
		this.isCount = isCount;
	}
	public Long getDeductBalanceId() {
		return deductBalanceId;
	}
	/***********Iverson add By 2010-12-24 添加旧件和旧件结算汇总关系********************/
	public void setDeductBalanceId(Long deductBalanceId) {
		this.deductBalanceId = deductBalanceId;
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

	public void setNoticeDate(Date noticeDate){
		this.noticeDate=noticeDate;
	}

	public Date getNoticeDate(){
		return this.noticeDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setPartAmount(Integer partAmount){
		this.partAmount=partAmount;
	}

	public Integer getPartAmount(){
		return this.partAmount;
	}

	public void setMaterialMoney(Double materialMoney){
		this.materialMoney=materialMoney;
	}

	public Double getMaterialMoney(){
		return this.materialMoney;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setDeductAmount(Integer deductAmount){
		this.deductAmount=deductAmount;
	}

	public Integer getDeductAmount(){
		return this.deductAmount;
	}

	public void setNoticeStatus(Integer noticeStatus){
		this.noticeStatus=noticeStatus;
	}

	public Integer getNoticeStatus(){
		return this.noticeStatus;
	}

	public void setOtherMoney(Double otherMoney){
		this.otherMoney=otherMoney;
	}

	public Double getOtherMoney(){
		return this.otherMoney;
	}

	public void setIsBal(Integer isBal){
		this.isBal=isBal;
	}

	public Integer getIsBal(){
		return this.isBal;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setClaimId(Long claimId){
		this.claimId=claimId;
	}

	public Long getClaimId(){
		return this.claimId;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setDeductNo(String deductNo){
		this.deductNo=deductNo;
	}

	public String getDeductNo(){
		return this.deductNo;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setManhourMoney(Double manhourMoney){
		this.manhourMoney=manhourMoney;
	}

	public Double getManhourMoney(){
		return this.manhourMoney;
	}

}