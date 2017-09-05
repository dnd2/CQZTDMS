/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-08 21:57:15
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrOldOutPrintDetailPO extends PO{

	private Long printBy;
	private Date printTime;
	private Long id;
	private String noticeNo;
	private String outNo;

	public void setPrintBy(Long printBy){
		this.printBy=printBy;
	}

	public Long getPrintBy(){
		return this.printBy;
	}

	public void setPrintTime(Date printTime){
		this.printTime=printTime;
	}

	public Date getPrintTime(){
		return this.printTime;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setNoticeNo(String noticeNo){
		this.noticeNo=noticeNo;
	}

	public String getNoticeNo(){
		return this.noticeNo;
	}

	public void setOutNo(String outNo){
		this.outNo=outNo;
	}

	public String getOutNo(){
		return this.outNo;
	}

}