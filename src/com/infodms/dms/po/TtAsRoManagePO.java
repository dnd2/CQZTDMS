/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-08-05 14:44:40
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsRoManagePO extends PO{

	private Float labourRate;
	private Integer isManaging;
	private Date updateDate;
	private String manageSortCode;
	private Long createBy;
	private Long roId;
	private Double discount;
	private Date createDate;
	private Float overheadExpensesRate;
	private Long updateBy;
	private Float repairPartRate;
	private Double overItemAmount;
	private Float addItemRate;
	private Float salesPartRate;
	private Float labourAmountRate;
	private Long id;

	public void setLabourRate(Float labourRate){
		this.labourRate=labourRate;
	}

	public Float getLabourRate(){
		return this.labourRate;
	}

	public void setIsManaging(Integer isManaging){
		this.isManaging=isManaging;
	}

	public Integer getIsManaging(){
		return this.isManaging;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setManageSortCode(String manageSortCode){
		this.manageSortCode=manageSortCode;
	}

	public String getManageSortCode(){
		return this.manageSortCode;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setRoId(Long roId){
		this.roId=roId;
	}

	public Long getRoId(){
		return this.roId;
	}

	public void setDiscount(Double discount){
		this.discount=discount;
	}

	public Double getDiscount(){
		return this.discount;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOverheadExpensesRate(Float overheadExpensesRate){
		this.overheadExpensesRate=overheadExpensesRate;
	}

	public Float getOverheadExpensesRate(){
		return this.overheadExpensesRate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setRepairPartRate(Float repairPartRate){
		this.repairPartRate=repairPartRate;
	}

	public Float getRepairPartRate(){
		return this.repairPartRate;
	}

	public void setOverItemAmount(Double overItemAmount){
		this.overItemAmount=overItemAmount;
	}

	public Double getOverItemAmount(){
		return this.overItemAmount;
	}

	public void setAddItemRate(Float addItemRate){
		this.addItemRate=addItemRate;
	}

	public Float getAddItemRate(){
		return this.addItemRate;
	}

	public void setSalesPartRate(Float salesPartRate){
		this.salesPartRate=salesPartRate;
	}

	public Float getSalesPartRate(){
		return this.salesPartRate;
	}

	public void setLabourAmountRate(Float labourAmountRate){
		this.labourAmountRate=labourAmountRate;
	}

	public Float getLabourAmountRate(){
		return this.labourAmountRate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}