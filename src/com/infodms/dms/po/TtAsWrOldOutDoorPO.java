/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-08-01 16:41:07
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrOldOutDoorPO extends PO{

	private String outCompanyCode;
	private String outNo;
	private Long createBy;
	private Integer noticePrintTimes;
	private String outByName;
	private String outCompanyTel;
	private String outCompany;
	private String outTittle;
	private Long doorId;
	private Integer yieldly;
	private Date createDate;
	private Integer printTimes;
	private Date lastPrintDate;
	private Date noticePrintDate;
	private String noticeNo;//通知单编号
	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public void setOutCompanyCode(String outCompanyCode){
		this.outCompanyCode=outCompanyCode;
	}

	public String getOutCompanyCode(){
		return this.outCompanyCode;
	}

	public void setOutNo(String outNo){
		this.outNo=outNo;
	}

	public String getOutNo(){
		return this.outNo;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setNoticePrintTimes(Integer noticePrintTimes){
		this.noticePrintTimes=noticePrintTimes;
	}

	public Integer getNoticePrintTimes(){
		return this.noticePrintTimes;
	}

	public void setOutByName(String outByName){
		this.outByName=outByName;
	}

	public String getOutByName(){
		return this.outByName;
	}

	public void setOutCompanyTel(String outCompanyTel){
		this.outCompanyTel=outCompanyTel;
	}

	public String getOutCompanyTel(){
		return this.outCompanyTel;
	}

	public void setOutCompany(String outCompany){
		this.outCompany=outCompany;
	}

	public String getOutCompany(){
		return this.outCompany;
	}

	public void setOutTittle(String outTittle){
		this.outTittle=outTittle;
	}

	public String getOutTittle(){
		return this.outTittle;
	}


	public void setDoorId(Long doorId){
		this.doorId=doorId;
	}

	public Long getDoorId(){
		return this.doorId;
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

	public void setPrintTimes(Integer printTimes){
		this.printTimes=printTimes;
	}

	public Integer getPrintTimes(){
		return this.printTimes;
	}

	public void setLastPrintDate(Date lastPrintDate){
		this.lastPrintDate=lastPrintDate;
	}

	public Date getLastPrintDate(){
		return this.lastPrintDate;
	}

	public void setNoticePrintDate(Date noticePrintDate){
		this.noticePrintDate=noticePrintDate;
	}

	public Date getNoticePrintDate(){
		return this.noticePrintDate;
	}

}