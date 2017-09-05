/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-10-23 13:10:06
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsMailPrintDetailPO extends PO{

	private Long printBy;
	private String printInDept;
	private Date printDate;
	private String printRemark;
	private Long printToDealer;
	private Long id;
	private Long createBy;
	private Date createDate;
	private String printType;

	public String getPrintType() {
		return printType;
	}

	public void setPrintType(String printType) {
		this.printType = printType;
	}

	public void setPrintBy(Long printBy){
		this.printBy=printBy;
	}

	public Long getPrintBy(){
		return this.printBy;
	}

	public void setPrintInDept(String printInDept){
		this.printInDept=printInDept;
	}

	public String getPrintInDept(){
		return this.printInDept;
	}

	public void setPrintDate(Date printDate){
		this.printDate=printDate;
	}

	public Date getPrintDate(){
		return this.printDate;
	}

	public void setPrintRemark(String printRemark){
		this.printRemark=printRemark;
	}

	public String getPrintRemark(){
		return this.printRemark;
	}

	public void setPrintToDealer(Long printToDealer){
		this.printToDealer=printToDealer;
	}

	public Long getPrintToDealer(){
		return this.printToDealer;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
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

}