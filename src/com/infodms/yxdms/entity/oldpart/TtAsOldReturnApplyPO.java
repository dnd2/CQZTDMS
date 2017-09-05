/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-01-20 15:06:42
* CreateBy   : yuewei
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.oldpart;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsOldReturnApplyPO extends PO{

	private Date startDate;
	private String rebutReason;
	private Long dealerId;
	private Date endDate;
	private Long id;
	private Date createDate;
	private Integer status;
	private String applyReason;
	private Long applyId;
	private  String  RETURN_NO;
	private  Long  PART_AMOUNT;
	private  Long  WR_AMOUNT;
	private  Long  PARKAGE_AMOUNT;
	

	public String getRETURN_NO() {
		return RETURN_NO;
	}

	public void setRETURN_NO(String return_no) {
		RETURN_NO = return_no;
	}

	public Long getPART_AMOUNT() {
		return PART_AMOUNT;
	}

	public void setPART_AMOUNT(Long part_amount) {
		PART_AMOUNT = part_amount;
	}

	public Long getWR_AMOUNT() {
		return WR_AMOUNT;
	}

	public void setWR_AMOUNT(Long wr_amount) {
		WR_AMOUNT = wr_amount;
	}

	public Long getPARKAGE_AMOUNT() {
		return PARKAGE_AMOUNT;
	}

	public void setPARKAGE_AMOUNT(Long parkage_amount) {
		PARKAGE_AMOUNT = parkage_amount;
	}

	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}

	public Date getStartDate(){
		return this.startDate;
	}

	public void setRebutReason(String rebutReason){
		this.rebutReason=rebutReason;
	}

	public String getRebutReason(){
		return this.rebutReason;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}

	public Date getEndDate(){
		return this.endDate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setApplyReason(String applyReason){
		this.applyReason=applyReason;
	}

	public String getApplyReason(){
		return this.applyReason;
	}

	public void setApplyId(Long applyId){
		this.applyId=applyId;
	}

	public Long getApplyId(){
		return this.applyId;
	}

}