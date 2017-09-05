/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-06-24 19:10:00
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPlanDetailChangePO extends PO{

	private Long updateBy;
	private Date updateDate;
	private Long afterPlanNum;
	private Long planDetailId;
	private Long createBy;
	private Long beforePlanNum;
	private Date createDate;
	private Long afterMatId;
	private Long detailChangeId;
	private Long beforeMatId;

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

	public void setAfterPlanNum(Long afterPlanNum){
		this.afterPlanNum=afterPlanNum;
	}

	public Long getAfterPlanNum(){
		return this.afterPlanNum;
	}

	public void setPlanDetailId(Long planDetailId){
		this.planDetailId=planDetailId;
	}

	public Long getPlanDetailId(){
		return this.planDetailId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setBeforePlanNum(Long beforePlanNum){
		this.beforePlanNum=beforePlanNum;
	}

	public Long getBeforePlanNum(){
		return this.beforePlanNum;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAfterMatId(Long afterMatId){
		this.afterMatId=afterMatId;
	}

	public Long getAfterMatId(){
		return this.afterMatId;
	}

	public void setDetailChangeId(Long detailChangeId){
		this.detailChangeId=detailChangeId;
	}

	public Long getDetailChangeId(){
		return this.detailChangeId;
	}

	public void setBeforeMatId(Long beforeMatId){
		this.beforeMatId=beforeMatId;
	}

	public Long getBeforeMatId(){
		return this.beforeMatId;
	}

}