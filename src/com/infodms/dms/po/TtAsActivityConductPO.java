/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-10-16 19:44:20
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsActivityConductPO extends PO{

	private String wAdd;
	private String mediaName;
	private String conductCont;
	private String wName;
	private Long dealerId;
	private Long id;
	private Date publishDate;
	private Long summaryId;

	public void setWAdd(String wAdd){
		this.wAdd=wAdd;
	}

	public String getWAdd(){
		return this.wAdd;
	}

	public void setMediaName(String mediaName){
		this.mediaName=mediaName;
	}

	public String getMediaName(){
		return this.mediaName;
	}

	public void setConductCont(String conductCont){
		this.conductCont=conductCont;
	}

	public String getConductCont(){
		return this.conductCont;
	}

	public void setWName(String wName){
		this.wName=wName;
	}

	public String getWName(){
		return this.wName;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setPublishDate(Date publishDate){
		this.publishDate=publishDate;
	}

	public Date getPublishDate(){
		return this.publishDate;
	}

	public void setSummaryId(Long summaryId){
		this.summaryId=summaryId;
	}

	public Long getSummaryId(){
		return this.summaryId;
	}

}