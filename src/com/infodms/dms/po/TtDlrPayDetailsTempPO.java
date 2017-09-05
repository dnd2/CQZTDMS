/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-03-14 14:13:56
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDlrPayDetailsTempPO extends PO{

	private String contactDeptCode;//经销商代码   
	private Date payDate;
	private Long contactDeptId;
	private String ticketNo;
	private Long accountTypeId;
	private String remark;
	private String paySum;
	private Long romNum;
	private String  contactDeptShortname; 
	
	public void setContactDeptCode(String contactDeptCode){
		this.contactDeptCode=contactDeptCode;
	}

	public String getContactDeptCode(){
		return this.contactDeptCode;
	}

	public void setPayDate(Date payDate){
		this.payDate=payDate;
	}

	public Date getPayDate(){
		return this.payDate;
	}

	public void setContactDeptId(Long contactDeptId){
		this.contactDeptId=contactDeptId;
	}

	public Long getContactDeptId(){
		return this.contactDeptId;
	}

	public void setTicketNo(String ticketNo){
		this.ticketNo=ticketNo;
	}

	public String getTicketNo(){
		return this.ticketNo;
	}

	public void setAccountTypeId(Long accountTypeId){
		this.accountTypeId=accountTypeId;
	}

	public Long getAccountTypeId(){
		return this.accountTypeId;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setPaySum(String paySum){
		this.paySum=paySum;
	}

	public String getPaySum(){
		return this.paySum;
	}

	public void setRomNum(Long romNum){
		this.romNum = romNum;
	}
	
	public Long getRomNum(){
		return romNum;
	}
	
	
	public void setContactDeptShortname(String contactDeptShortname){
		this.contactDeptShortname = contactDeptShortname;
	}
	
	public String getContactDeptShortname(){
		return contactDeptShortname;
	}
}