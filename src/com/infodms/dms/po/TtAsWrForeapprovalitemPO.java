/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-25 15:50:11
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrForeapprovalitemPO extends PO{

	private String checkRemark;
	private String attachmentFile2;
	private Long author;
	private Long createBy;
	private String applier;
	private String itemCode;
	private String roNo;
	private Date applyDate;
	private Date createDate;
	private Long fid;
	private String dealerRemark;
	private Integer status;
	private String itemDesc;
	private String authCode;
	private Date authDate;
	private String attachmentFile1;
	private Long itemId;
	private Date updateDate;
	private Date reportDate;
	private String attachmentFile3;
	private String dealerCode;
	private Integer claimType;
	private Long updateBy;
	private String reporter;
	private Integer itemType;
	private Long id;
	private Double itemQuantity;
	private Double  itemAmount;
	public Double getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(Double itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public Double getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(Double itemAmount) {
		this.itemAmount = itemAmount;
	}

	public void setCheckRemark(String checkRemark){
		this.checkRemark=checkRemark;
	}

	public String getCheckRemark(){
		return this.checkRemark;
	}

	public void setAttachmentFile2(String attachmentFile2){
		this.attachmentFile2=attachmentFile2;
	}

	public String getAttachmentFile2(){
		return this.attachmentFile2;
	}

	public void setAuthor(Long author){
		this.author=author;
	}

	public Long getAuthor(){
		return this.author;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setApplier(String applier){
		this.applier=applier;
	}

	public String getApplier(){
		return this.applier;
	}

	public void setItemCode(String itemCode){
		this.itemCode=itemCode;
	}

	public String getItemCode(){
		return this.itemCode;
	}

	public void setRoNo(String roNo){
		this.roNo=roNo;
	}

	public String getRoNo(){
		return this.roNo;
	}

	public void setApplyDate(Date applyDate){
		this.applyDate=applyDate;
	}

	public Date getApplyDate(){
		return this.applyDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setFid(Long fid){
		this.fid=fid;
	}

	public Long getFid(){
		return this.fid;
	}

	public void setDealerRemark(String dealerRemark){
		this.dealerRemark=dealerRemark;
	}

	public String getDealerRemark(){
		return this.dealerRemark;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setItemDesc(String itemDesc){
		this.itemDesc=itemDesc;
	}

	public String getItemDesc(){
		return this.itemDesc;
	}

	public void setAuthCode(String authCode){
		this.authCode=authCode;
	}

	public String getAuthCode(){
		return this.authCode;
	}

	public void setAuthDate(Date authDate){
		this.authDate=authDate;
	}

	public Date getAuthDate(){
		return this.authDate;
	}

	public void setAttachmentFile1(String attachmentFile1){
		this.attachmentFile1=attachmentFile1;
	}

	public String getAttachmentFile1(){
		return this.attachmentFile1;
	}

	public void setItemId(Long itemId){
		this.itemId=itemId;
	}

	public Long getItemId(){
		return this.itemId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setReportDate(Date reportDate){
		this.reportDate=reportDate;
	}

	public Date getReportDate(){
		return this.reportDate;
	}

	public void setAttachmentFile3(String attachmentFile3){
		this.attachmentFile3=attachmentFile3;
	}

	public String getAttachmentFile3(){
		return this.attachmentFile3;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setClaimType(Integer claimType){
		this.claimType=claimType;
	}

	public Integer getClaimType(){
		return this.claimType;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setReporter(String reporter){
		this.reporter=reporter;
	}

	public String getReporter(){
		return this.reporter;
	}

	public void setItemType(Integer itemType){
		this.itemType=itemType;
	}

	public Integer getItemType(){
		return this.itemType;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}