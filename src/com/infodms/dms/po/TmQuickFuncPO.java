/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-14 08:51:26
* CreateBy   : Andy
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmQuickFuncPO extends PO{

	private Date updateDate;
	private Long funcId;
	private Long quickFuncId;
	private Long updateBy;
	private Integer funcOrder;
	private Long createBy;
	private Long userId;
	private Date createDate;
	private Long poseId;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setFuncId(Long funcId){
		this.funcId=funcId;
	}

	public Long getFuncId(){
		return this.funcId;
	}

	public void setQuickFuncId(Long quickFuncId){
		this.quickFuncId=quickFuncId;
	}

	public Long getQuickFuncId(){
		return this.quickFuncId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setFuncOrder(Integer funcOrder){
		this.funcOrder=funcOrder;
	}

	public Integer getFuncOrder(){
		return this.funcOrder;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return this.userId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPoseId(Long poseId){
		this.poseId=poseId;
	}

	public Long getPoseId(){
		return this.poseId;
	}

}