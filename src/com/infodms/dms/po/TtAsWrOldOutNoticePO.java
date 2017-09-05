/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-08-02 16:06:46
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrOldOutNoticePO extends PO{

	private String noticeBank;
	private Long createBy;
	private String outNo;
	private String noticeNo;
	private Double total;
	private String noticeTittle;
	private Double smallTotal;
	private String noticeCompanyByTel;
	private String noticeCode;
	private String noticeCompanyBy;
	private Double taxTotal;
	private String noticeAcount;
	private Long spefeeId;
	private Integer yieldly;
	private Date createDate;
	private Long noticeId;
	private Date lastPrintDate;
	private Integer printTimes;
	private String noticeCompany;
	private Integer type;
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setNoticeBank(String noticeBank){
		this.noticeBank=noticeBank;
	}

	public String getNoticeBank(){
		return this.noticeBank;
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

	public void setNoticeNo(String noticeNo){
		this.noticeNo=noticeNo;
	}

	public String getNoticeNo(){
		return this.noticeNo;
	}

	public void setTotal(Double total){
		this.total=total;
	}

	public Double getTotal(){
		return this.total;
	}

	public void setNoticeTittle(String noticeTittle){
		this.noticeTittle=noticeTittle;
	}

	public String getNoticeTittle(){
		return this.noticeTittle;
	}

	public void setSmallTotal(Double smallTotal){
		this.smallTotal=smallTotal;
	}

	public Double getSmallTotal(){
		return this.smallTotal;
	}

	public void setNoticeCompanyByTel(String noticeCompanyByTel){
		this.noticeCompanyByTel=noticeCompanyByTel;
	}

	public String getNoticeCompanyByTel(){
		return this.noticeCompanyByTel;
	}

	public void setNoticeCode(String noticeCode){
		this.noticeCode=noticeCode;
	}

	public String getNoticeCode(){
		return this.noticeCode;
	}

	public void setNoticeCompanyBy(String noticeCompanyBy){
		this.noticeCompanyBy=noticeCompanyBy;
	}

	public String getNoticeCompanyBy(){
		return this.noticeCompanyBy;
	}

	public void setTaxTotal(Double taxTotal){
		this.taxTotal=taxTotal;
	}

	public Double getTaxTotal(){
		return this.taxTotal;
	}

	public void setNoticeAcount(String noticeAcount){
		this.noticeAcount=noticeAcount;
	}

	public String getNoticeAcount(){
		return this.noticeAcount;
	}

	public void setSpefeeId(Long spefeeId){
		this.spefeeId=spefeeId;
	}

	public Long getSpefeeId(){
		return this.spefeeId;
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

	public void setNoticeId(Long noticeId){
		this.noticeId=noticeId;
	}

	public Long getNoticeId(){
		return this.noticeId;
	}

	public void setLastPrintDate(Date lastPrintDate){
		this.lastPrintDate=lastPrintDate;
	}

	public Date getLastPrintDate(){
		return this.lastPrintDate;
	}

	public void setPrintTimes(Integer printTimes){
		this.printTimes=printTimes;
	}

	public Integer getPrintTimes(){
		return this.printTimes;
	}

	public void setNoticeCompany(String noticeCompany){
		this.noticeCompany=noticeCompany;
	}

	public String getNoticeCompany(){
		return this.noticeCompany;
	}

}