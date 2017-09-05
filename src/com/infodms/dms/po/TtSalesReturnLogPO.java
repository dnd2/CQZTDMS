/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-08-20 18:40:15
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesReturnLogPO extends PO{

	private Long logId;
	private Integer hisFreetimes;
	private Double hisMileage;
	private String vin;
	private Long createBy;
	private Date createDate;

	public void setLogId(Long logId){
		this.logId=logId;
	}

	public Long getLogId(){
		return this.logId;
	}

	public void setHisFreetimes(Integer hisFreetimes){
		this.hisFreetimes=hisFreetimes;
	}

	public Integer getHisFreetimes(){
		return this.hisFreetimes;
	}

	public void setHisMileage(Double hisMileage){
		this.hisMileage=hisMileage;
	}

	public Double getHisMileage(){
		return this.hisMileage;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
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