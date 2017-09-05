/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-01-22 14:32:24
* CreateBy   : wswcx
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmProOrderPO extends PO{
	//修改日期
	private Date updateDate;
	//生产订单号
	private String orderNo;
	//计划年
	private Integer planYear;
	//备注
	private String remark;
	//创建人员
	private Long createBy;
	//产地
	private Long areaId;
	//状态
	private Integer status;
	//排产类型 -1储备订单 1排产订单0经销商需求预测
	private Integer proType;
	//生产订单ID
	private Long proOrderId;
	//更新人员
	private Long updateBy;
	//版本号
	private Integer ver;
	//创建日期
	private Date createDate;
	//数量
	private Integer num;
	//周
	private Integer planWeek;
	//月
	private Integer planMonth;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}

	public String getOrderNo(){
		return this.orderNo;
	}

	public void setPlanYear(Integer planYear){
		this.planYear=planYear;
	}

	public Integer getPlanYear(){
		return this.planYear;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setProType(Integer proType){
		this.proType=proType;
	}

	public Integer getProType(){
		return this.proType;
	}

	public void setProOrderId(Long proOrderId){
		this.proOrderId=proOrderId;
	}

	public Long getProOrderId(){
		return this.proOrderId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setNum(Integer num){
		this.num=num;
	}

	public Integer getNum(){
		return this.num;
	}

	public void setPlanWeek(Integer planWeek){
		this.planWeek=planWeek;
	}

	public Integer getPlanWeek(){
		return this.planWeek;
	}

	public void setPlanMonth(Integer planMonth){
		this.planMonth=planMonth;
	}

	public Integer getPlanMonth(){
		return this.planMonth;
	}

}