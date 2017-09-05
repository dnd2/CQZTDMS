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
public class TiSvoCarListPO extends PO{

	private Date updateDate;
	private Long createBy;
	private Date produceDate;
	private Date createDate;
	private String chassisNo;
	private String stockCode;
	private String carTypeCode;
	private String allowCarNo;
	private Long updateBy;
	private String engineNo;
	private Integer dmsStatus;
	private Long seqId;
	private Date stockDate;
	private String svoNo;
	private Date dmsDate;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setProduceDate(Date produceDate){
		this.produceDate=produceDate;
	}

	public Date getProduceDate(){
		return this.produceDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setChassisNo(String chassisNo){
		this.chassisNo=chassisNo;
	}

	public String getChassisNo(){
		return this.chassisNo;
	}

	public void setStockCode(String stockCode){
		this.stockCode=stockCode;
	}

	public String getStockCode(){
		return this.stockCode;
	}

	public void setCarTypeCode(String carTypeCode){
		this.carTypeCode=carTypeCode;
	}

	public String getCarTypeCode(){
		return this.carTypeCode;
	}

	public void setAllowCarNo(String allowCarNo){
		this.allowCarNo=allowCarNo;
	}

	public String getAllowCarNo(){
		return this.allowCarNo;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setEngineNo(String engineNo){
		this.engineNo=engineNo;
	}

	public String getEngineNo(){
		return this.engineNo;
	}

	public void setDmsStatus(Integer dmsStatus){
		this.dmsStatus=dmsStatus;
	}

	public Integer getDmsStatus(){
		return this.dmsStatus;
	}

	public void setSeqId(Long seqId){
		this.seqId=seqId;
	}

	public Long getSeqId(){
		return this.seqId;
	}

	public void setStockDate(Date stockDate){
		this.stockDate=stockDate;
	}

	public Date getStockDate(){
		return this.stockDate;
	}

	public void setSvoNo(String svoNo){
		this.svoNo=svoNo;
	}

	public String getSvoNo(){
		return this.svoNo;
	}

	public void setDmsDate(Date dmsDate){
		this.dmsDate=dmsDate;
	}

	public Date getDmsDate(){
		return this.dmsDate;
	}

}