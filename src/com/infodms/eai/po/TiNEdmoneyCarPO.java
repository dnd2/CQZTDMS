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
public class TiNEdmoneyCarPO extends PO{

	private String carTypeShortCode;
	private Date updateDate;
	private String confirmMan;
	private Long createBy;
	private String carSeriesCode;
	private Date createDate;
	private Long seqno;
	private Integer actType;
	private String carTypeCode;
	private Integer flag;
	private Date backdate;
	private Long updateBy;
	private String orderid;
	private String vin;
	private String carNameCode;
	private Long seqId;
	private Date confirmDate;
	private Long credenceBackId;

	public void setCarTypeShortCode(String carTypeShortCode){
		this.carTypeShortCode=carTypeShortCode;
	}

	public String getCarTypeShortCode(){
		return this.carTypeShortCode;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setConfirmMan(String confirmMan){
		this.confirmMan=confirmMan;
	}

	public String getConfirmMan(){
		return this.confirmMan;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCarSeriesCode(String carSeriesCode){
		this.carSeriesCode=carSeriesCode;
	}

	public String getCarSeriesCode(){
		return this.carSeriesCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setSeqno(Long seqno){
		this.seqno=seqno;
	}

	public Long getSeqno(){
		return this.seqno;
	}

	public void setActType(Integer actType){
		this.actType=actType;
	}

	public Integer getActType(){
		return this.actType;
	}

	public void setCarTypeCode(String carTypeCode){
		this.carTypeCode=carTypeCode;
	}

	public String getCarTypeCode(){
		return this.carTypeCode;
	}

	public void setFlag(Integer flag){
		this.flag=flag;
	}

	public Integer getFlag(){
		return this.flag;
	}

	public void setBackdate(Date backdate){
		this.backdate=backdate;
	}

	public Date getBackdate(){
		return this.backdate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setOrderid(String orderid){
		this.orderid=orderid;
	}

	public String getOrderid(){
		return this.orderid;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setCarNameCode(String carNameCode){
		this.carNameCode=carNameCode;
	}

	public String getCarNameCode(){
		return this.carNameCode;
	}

	public void setSeqId(Long seqId){
		this.seqId=seqId;
	}

	public Long getSeqId(){
		return this.seqId;
	}

	public void setConfirmDate(Date confirmDate){
		this.confirmDate=confirmDate;
	}

	public Date getConfirmDate(){
		return this.confirmDate;
	}

	public void setCredenceBackId(Long credenceBackId){
		this.credenceBackId=credenceBackId;
	}

	public Long getCredenceBackId(){
		return this.credenceBackId;
	}

}