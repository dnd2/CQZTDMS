/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-01-22 14:32:23
* CreateBy   : wswcx
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmProPlanPO extends PO{

	private String planNo;
	private Long updateBy;
	private Long planId;
	private Date updateDate;
	private Integer planYear;
	private Long createBy;
	private Date createDate;
	private Long yieldly;
	private Integer status;
	private Integer planWeek;
	private Integer num;
	private Integer planMonth;
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setPlanNo(String planNo){
		this.planNo=planNo;
	}

	public String getPlanNo(){
		return this.planNo;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPlanId(Long planId){
		this.planId=planId;
	}

	public Long getPlanId(){
		return this.planId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPlanYear(Integer planYear){
		this.planYear=planYear;
	}

	public Integer getPlanYear(){
		return this.planYear;
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

	public void setYieldly(Long yieldly){
		this.yieldly=yieldly;
	}

	public Long getYieldly(){
		return this.yieldly;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setPlanWeek(Integer planWeek){
		this.planWeek=planWeek;
	}

	public Integer getPlanWeek(){
		return this.planWeek;
	}

	public void setNum(Integer num){
		this.num=num;
	}

	public Integer getNum(){
		return this.num;
	}

	public void setPlanMonth(Integer planMonth){
		this.planMonth=planMonth;
	}

	public Integer getPlanMonth(){
		return this.planMonth;
	}

}