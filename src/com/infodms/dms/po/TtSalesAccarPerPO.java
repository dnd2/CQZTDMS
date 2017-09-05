/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-04-09 21:31:44
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesAccarPerPO extends PO{

	private String perCode;
	private Long yieldly;
	private Date updateDate;
	private Long status;
	private String conTel;
	private Long perId;
	private Long updateBy;
	private String perName;
	private Long createBy;
	private Date createDate;
	private String remark;

	public void setPerCode(String perCode){
		this.perCode=perCode;
	}

	public String getPerCode(){
		return this.perCode;
	}

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

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setConTel(String conTel){
		this.conTel=conTel;
	}

	public String getConTel(){
		return this.conTel;
	}

	public void setPerId(Long perId){
		this.perId=perId;
	}

	public Long getPerId(){
		return this.perId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPerName(String perName){
		this.perName=perName;
	}

	public String getPerName(){
		return this.perName;
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

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

}