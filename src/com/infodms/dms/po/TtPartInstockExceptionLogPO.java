/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-06-20 16:38:19
* CreateBy   : bianlanzhou
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartInstockExceptionLogPO extends PO{

	private Long exceptionId;
	private Integer state;
	private Long exceptionNum;
	private Long soId;
	private Long createBy;
	private Date createDate;
	private String inCode;
	private String exceptionRemark;
	private String soCode;
	private Long inId;
	private Long partId;

	private String oemRemark;
	private Long replyBy;
	private Date replyDate;
	private Long closeBy;
	private Date closeDate;
	public Long getCloseBy() {
		return closeBy;
	}

	public void setCloseBy(Long closeBy) {
		this.closeBy = closeBy;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public Long getReplyBy() {
		return replyBy;
	}

	public void setReplyBy(Long replyBy) {
		this.replyBy = replyBy;
	}

	public Date getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}

	public String getOemRemark() {
		return oemRemark;
	}

	public void setOemRemark(String oemRemark) {
		this.oemRemark = oemRemark;
	}

	public Long getPartId() {
		return partId;
	}

	public void setPartId(Long partId) {
		this.partId = partId;
	}

	public void setExceptionId(Long exceptionId){
		this.exceptionId=exceptionId;
	}

	public Long getExceptionId(){
		return this.exceptionId;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setExceptionNum(Long exceptionNum){
		this.exceptionNum=exceptionNum;
	}

	public Long getExceptionNum(){
		return this.exceptionNum;
	}

	public void setSoId(Long soId){
		this.soId=soId;
	}

	public Long getSoId(){
		return this.soId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setInCode(String inCode){
		this.inCode=inCode;
	}

	public String getInCode(){
		return this.inCode;
	}

	public void setExceptionRemark(String exceptionRemark){
		this.exceptionRemark=exceptionRemark;
	}

	public String getExceptionRemark(){
		return this.exceptionRemark;
	}

	public void setSoCode(String soCode){
		this.soCode=soCode;
	}

	public String getSoCode(){
		return this.soCode;
	}

	public void setInId(Long inId){
		this.inId=inId;
	}

	public Long getInId(){
		return this.inId;
	}

}