/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-05 18:40:34
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.eai.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TiNOrderEvoPO extends PO{

	private Integer actType;
	private Double usemoney;
	private Date updateDate;
	private String flag;
	private String evoNum;
	private Long updateBy;
	private Date confirmdate;
	private Long createBy;
	private String orderid;
	private Date createDate;
	private Long seqId;
	private Long credenceBackId;

	public void setActType(Integer actType){
		this.actType=actType;
	}

	public Integer getActType(){
		return this.actType;
	}

	public void setUsemoney(Double usemoney){
		this.usemoney=usemoney;
	}

	public Double getUsemoney(){
		return this.usemoney;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setFlag(String flag){
		this.flag=flag;
	}

	public String getFlag(){
		return this.flag;
	}

	public void setEvoNum(String evoNum){
		this.evoNum=evoNum;
	}

	public String getEvoNum(){
		return this.evoNum;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setConfirmdate(Date confirmdate){
		this.confirmdate=confirmdate;
	}

	public Date getConfirmdate(){
		return this.confirmdate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOrderid(String orderid){
		this.orderid=orderid;
	}

	public String getOrderid(){
		return this.orderid;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSeqId(Long seqId){
		this.seqId=seqId;
	}

	public Long getSeqId(){
		return this.seqId;
	}

	public void setCredenceBackId(Long credenceBackId){
		this.credenceBackId=credenceBackId;
	}

	public Long getCredenceBackId(){
		return this.credenceBackId;
	}

}