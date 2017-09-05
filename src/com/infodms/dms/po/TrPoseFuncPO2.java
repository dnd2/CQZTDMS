/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-08 18:36:25
* CreateBy   : Zhang tianheng
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TrPoseFuncPO2 extends PO{

	private Date updateDate;
	private String funcId;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private String poseId;
	private Long poseFuncId;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setFuncId(String funcId){
		this.funcId=funcId;
	}

	public String getFuncId(){
		return this.funcId;
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

	public void setPoseId(String poseId){
		this.poseId=poseId;
	}

	public String getPoseId(){
		return this.poseId;
	}

	public void setPoseFuncId(Long poseFuncId){
		this.poseFuncId=poseFuncId;
	}

	public Long getPoseFuncId(){
		return this.poseFuncId;
	}

}