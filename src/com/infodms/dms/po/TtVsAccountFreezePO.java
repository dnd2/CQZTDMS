/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-10-05 15:51:14
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsAccountFreezePO extends PO{

	private Date freezeDate;
	private Long dealerId;
	private Double freezeAmount;
	private Long accountId;
	private Long companyId;
	private Date updateDate;
	private Long createBy;
	private Integer status;
	private Long freezeId;
	private Long reqId;
	private Long updateBy;
	private Date payDate;
	private Integer useType;
	private Date createDate;
	private Double payAmount;
	private Long buzId;
	private Integer isCsgc ;
	private String erpCode ;

	/**
	 * 先通过freezeId比较传入对象是否相同
	 * 或通过(account_id 与  req_id都相同)来判断是否相同
	 * @param po
	 * @return
	 */
	public boolean equals(TtVsAccountFreezePO po) {
		if(po.getFreezeId()!= null && this.getFreezeId() != null){
			if (this.getFreezeId().longValue() == po.getFreezeId().longValue()){
				return true;
			}
		}else if(this.getAccountId()!= null && po.getAccountId()!= null &&
				this.getReqId() != null && po.getReqId() != null){
			if(this.getAccountId().longValue() == po.getAccountId().longValue() 
					&& this.getReqId().longValue() == po.getReqId().longValue()) {
				return true;
			}
		}
		return false;
	}
	
	public Integer getIsCsgc() {
		return isCsgc;
	}

	public void setIsCsgc(Integer isCsgc) {
		this.isCsgc = isCsgc;
	}

	public String getErpCode() {
		return erpCode;
	}

	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}

	public void setFreezeDate(Date freezeDate){
		this.freezeDate=freezeDate;
	}

	public Date getFreezeDate(){
		return this.freezeDate;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setFreezeAmount(Double freezeAmount){
		this.freezeAmount=freezeAmount;
	}

	public Double getFreezeAmount(){
		return this.freezeAmount;
	}

	public void setAccountId(Long accountId){
		this.accountId=accountId;
	}

	public Long getAccountId(){
		return this.accountId;
	}

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
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

	public void setFreezeId(Long freezeId){
		this.freezeId=freezeId;
	}

	public Long getFreezeId(){
		return this.freezeId;
	}

	public void setReqId(Long reqId){
		this.reqId=reqId;
	}

	public Long getReqId(){
		return this.reqId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPayDate(Date payDate){
		this.payDate=payDate;
	}

	public Date getPayDate(){
		return this.payDate;
	}

	public void setUseType(Integer useType){
		this.useType=useType;
	}

	public Integer getUseType(){
		return this.useType;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPayAmount(Double payAmount){
		this.payAmount=payAmount;
	}

	public Double getPayAmount(){
		return this.payAmount;
	}

	public void setBuzId(Long buzId){
		this.buzId=buzId;
	}

	public Long getBuzId(){
		return this.buzId;
	}

}