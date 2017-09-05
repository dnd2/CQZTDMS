/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-19 13:37:59
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrOtherfeePO extends PO{

	private String feeCode;
	private Long oemCompanyId;
	private String feeName;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private Integer isDel;
	private Long feeId;

	public void setFeeCode(String feeCode){
		this.feeCode=feeCode;
	}

	public String getFeeCode(){
		return this.feeCode;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setFeeName(String feeName){
		this.feeName=feeName;
	}

	public String getFeeName(){
		return this.feeName;
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

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setFeeId(Long feeId){
		this.feeId=feeId;
	}

	public Long getFeeId(){
		return this.feeId;
	}

}