/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-08-08 09:33:39
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartBillDefinePO extends PO{

	private Integer state;
	private Date disableDate;
	private Long dealerId;
	private Date deleteDate;
	private Date updateDate;
	private String dealerCode;
	private String remark;
	private Long createBy;
	private Integer status;
	private String account;
	private String taxNo;
	private Integer invType;
	private String dealerName;
	private String taxName;
	private String bank;
	private String mailAddr;
	private Long updateBy;
	private String tel;
	private Long billId;
	private Long yieldly;
	private Long deleteBy;
	private Long disableBy;
	private Date createDate;
	private String addr;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
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

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
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

	public void setAccount(String account){
		this.account=account;
	}

	public String getAccount(){
		return this.account;
	}

	public void setTaxNo(String taxNo){
		this.taxNo=taxNo;
	}

	public String getTaxNo(){
		return this.taxNo;
	}

	public void setInvType(Integer invType){
		this.invType=invType;
	}

	public Integer getInvType(){
		return this.invType;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setTaxName(String taxName){
		this.taxName=taxName;
	}

	public String getTaxName(){
		return this.taxName;
	}

	public void setBank(String bank){
		this.bank=bank;
	}

	public String getBank(){
		return this.bank;
	}

	public void setMailAddr(String mailAddr){
		this.mailAddr=mailAddr;
	}

	public String getMailAddr(){
		return this.mailAddr;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setTel(String tel){
		this.tel=tel;
	}

	public String getTel(){
		return this.tel;
	}

	public void setBillId(Long billId){
		this.billId=billId;
	}

	public Long getBillId(){
		return this.billId;
	}

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
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

	public void setAddr(String addr){
		this.addr=addr;
	}

	public String getAddr(){
		return this.addr;
	}

}