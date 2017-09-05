/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-01-28 13:15:05
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcRevistDetailPO extends PO{

	private String revisitSpeak;
	private Date updateDate;
	private String clientTip;
	private Long status;
	private String revisitNum;
	private Long updateBy;
	private Long createBy;
	private String comments;
	private Date createDate;
	private Long detailId;
	private Long revisitId;
	private String revisitCycle;

	public void setRevisitSpeak(String revisitSpeak){
		this.revisitSpeak=revisitSpeak;
	}

	public String getRevisitSpeak(){
		return this.revisitSpeak;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setClientTip(String clientTip){
		this.clientTip=clientTip;
	}

	public String getClientTip(){
		return this.clientTip;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setRevisitNum(String revisitNum){
		this.revisitNum=revisitNum;
	}

	public String getRevisitNum(){
		return this.revisitNum;
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

	public void setComments(String comments){
		this.comments=comments;
	}

	public String getComments(){
		return this.comments;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}

	public Long getDetailId(){
		return this.detailId;
	}

	public void setRevisitId(Long revisitId){
		this.revisitId=revisitId;
	}

	public Long getRevisitId(){
		return this.revisitId;
	}

	public void setRevisitCycle(String revisitCycle){
		this.revisitCycle=revisitCycle;
	}

	public String getRevisitCycle(){
		return this.revisitCycle;
	}

}