/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-21 14:02:56
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesBalancePO extends PO{

	private Integer balCount;
	private Long balPer;
	private Date updateDate;
	private Long logiId;
	private Long updateBy;
	private Long createBy;
	private Date balDate;
	private Date createDate;
	private Long areaId;
	private Long balId;
	private Double balAmount;
	private String balNo;
	private Double otherMoney;
	private String balMonth;
	private Integer status;
	private Integer isChange;
	private Double supplyMoney;
	private Double deductMoney;
	
	
	public Double getDeductMoney() {
		return deductMoney;
	}

	public void setDeductMoney(Double deductMoney) {
		this.deductMoney = deductMoney;
	}

	public Double getSupplyMoney() {
		return supplyMoney;
	}

	public void setSupplyMoney(Double supplyMoney) {
		this.supplyMoney = supplyMoney;
	}

	public Integer getIsChange() {
		return isChange;
	}

	public void setIsChange(Integer isChange) {
		this.isChange = isChange;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the balMonth
	 */
	public String getBalMonth() {
		return balMonth;
	}

	/**
	 * @param balMonth the balMonth to set
	 */
	public void setBalMonth(String balMonth) {
		this.balMonth = balMonth;
	}

	public Double getOtherMoney() {
		return otherMoney;
	}

	public void setOtherMoney(Double otherMoney) {
		this.otherMoney = otherMoney;
	}

	public void setBalCount(Integer balCount){
		this.balCount=balCount;
	}

	public Integer getBalCount(){
		return this.balCount;
	}

	public void setBalPer(Long balPer){
		this.balPer=balPer;
	}

	public Long getBalPer(){
		return this.balPer;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setLogiId(Long logiId){
		this.logiId=logiId;
	}

	public Long getLogiId(){
		return this.logiId;
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

	public void setBalDate(Date balDate){
		this.balDate=balDate;
	}

	public Date getBalDate(){
		return this.balDate;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}

	public Long getAreaId(){
		return this.areaId;
	}

	public void setBalId(Long balId){
		this.balId=balId;
	}

	public Long getBalId(){
		return this.balId;
	}

	public void setBalAmount(Double balAmount){
		this.balAmount=balAmount;
	}

	public Double getBalAmount(){
		return this.balAmount;
	}

	public void setBalNo(String balNo){
		this.balNo=balNo;
	}

	public String getBalNo(){
		return this.balNo;
	}

}