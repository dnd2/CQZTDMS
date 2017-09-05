/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-15 10:05:33
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesMilsetPO extends PO{

	private Long yieldly;
	private Date updateDate;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private Integer milEnd;
	private Integer milStart;
	private Long milId;

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
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

	public void setMilEnd(Integer milEnd){
		this.milEnd=milEnd;
	}

	public Integer getMilEnd(){
		return this.milEnd;
	}

	public void setMilStart(Integer milStart){
		this.milStart=milStart;
	}

	public Integer getMilStart(){
		return this.milStart;
	}

	public void setMilId(Long milId){
		this.milId=milId;
	}

	public Long getMilId(){
		return this.milId;
	}

}