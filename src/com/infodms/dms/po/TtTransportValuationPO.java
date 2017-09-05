/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-07-07 11:11:01
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtTransportValuationPO extends PO{

	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private String valuationCode;
	private String tvId;
	private Date createDate;
	private Integer status;
	private String transportCode;
	private String tvName;

	public String getTvName() {
		return tvName;
	}

	public void setTvName(String tvName) {
		this.tvName = tvName;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

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

	public void setValuationCode(String valuationCode){
		this.valuationCode=valuationCode;
	}

	public String getValuationCode(){
		return this.valuationCode;
	}

	public void setTvId(String tvId){
		this.tvId=tvId;
	}

	public String getTvId(){
		return this.tvId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setTransportCode(String transportCode){
		this.transportCode=transportCode;
	}

	public String getTransportCode(){
		return this.transportCode;
	}

}