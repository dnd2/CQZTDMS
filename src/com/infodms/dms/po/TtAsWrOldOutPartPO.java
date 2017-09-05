/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-25 13:51:10
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrOldOutPartPO extends PO{

	private String claimNo;
	private Integer outType;
	private String supplyCode;
	private Integer outAmout;
	private Long createBy;
	private String outNo;
	private Long outBy;
	private String outPartCode;
	private Date outDate;
	private String supplyName;
	private Long id;
	private String outPartName;
	private Integer yieldly;
	private Date createDate;
	private String remark;
	private Integer outPartType;
	private String relationalOutNo;
	private String rangeNo;
	private Integer outFlag;
	private Integer diyFlag;
	private Integer handMark;
	private String  lineNum;
	
	

	public String getLineNum() {
		return lineNum;
	}

	public void setLineNum(String lineNum) {
		this.lineNum = lineNum;
	}

	public Integer getHandMark() {
		return handMark;
	}

	public void setHandMark(Integer handMark) {
		this.handMark = handMark;
	}

	public Integer getOutFlag() {
		return outFlag;
	}

	public void setOutFlag(Integer outFlag) {
		this.outFlag = outFlag;
	}

	public String getRangeNo() {
		return rangeNo;
	}

	public void setRangeNo(String rangeNo) {
		this.rangeNo = rangeNo;
	}

	public String getRelationalOutNo() {
		return relationalOutNo;
	}

	public void setRelationalOutNo(String relationalOutNo) {
		this.relationalOutNo = relationalOutNo;
	}

	public Integer getOutPartType() {
		return outPartType;
	}

	public void setOutPartType(Integer outPartType) {
		this.outPartType = outPartType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setClaimNo(String claimNo){
		this.claimNo=claimNo;
	}

	public String getClaimNo(){
		return this.claimNo;
	}

	public void setOutType(Integer outType){
		this.outType=outType;
	}

	public Integer getOutType(){
		return this.outType;
	}

	public void setSupplyCode(String supplyCode){
		this.supplyCode=supplyCode;
	}

	public String getSupplyCode(){
		return this.supplyCode;
	}

	public void setOutAmout(Integer outAmout){
		this.outAmout=outAmout;
	}

	public Integer getOutAmout(){
		return this.outAmout;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOutNo(String outNo){
		this.outNo=outNo;
	}

	public String getOutNo(){
		return this.outNo;
	}

	public void setOutBy(Long outBy){
		this.outBy=outBy;
	}

	public Long getOutBy(){
		return this.outBy;
	}

	public void setOutPartCode(String outPartCode){
		this.outPartCode=outPartCode;
	}

	public String getOutPartCode(){
		return this.outPartCode;
	}

	public void setOutDate(Date outDate){
		this.outDate=outDate;
	}

	public Date getOutDate(){
		return this.outDate;
	}

	public void setSupplyName(String supplyName){
		this.supplyName=supplyName;
	}

	public String getSupplyName(){
		return this.supplyName;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setOutPartName(String outPartName){
		this.outPartName=outPartName;
	}

	public String getOutPartName(){
		return this.outPartName;
	}

	public void setYieldly(Integer yieldly){
		this.yieldly=yieldly;
	}

	public Integer getYieldly(){
		return this.yieldly;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public Integer getDiyFlag() {
		return diyFlag;
	}

	public void setDiyFlag(Integer diyFlag) {
		this.diyFlag = diyFlag;
	}

}