/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-24 16:18:57
* CreateBy   : ranpok
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesAssDetailPO extends PO{

	private String erpMaterialName;
	private Integer accNum;
	private Long assId;
	private Long assDetailId;
	private Date updateDate;
	private Long createBy;
	private String remark;
	private String erpMaterialCode;
	private Integer outNum;
	private Integer status;
	private Long logiId;
	private Long matId;
	private Integer processStatus;
	private Long updateBy;
	private Long dealerId;
	private Integer sendNum;
	private Date createDate;
	private Integer isStatus;
	private Integer allocaNum;
	private Integer assNum;
	private String warehouseCode;

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public void setErpMaterialName(String erpMaterialName){
		this.erpMaterialName=erpMaterialName;
	}

	public String getErpMaterialName(){
		return this.erpMaterialName;
	}

	public void setAccNum(Integer accNum){
		this.accNum=accNum;
	}

	public Integer getAccNum(){
		return this.accNum;
	}

	public void setAssId(Long assId){
		this.assId=assId;
	}

	public Long getAssId(){
		return this.assId;
	}

	public void setAssDetailId(Long assDetailId){
		this.assDetailId=assDetailId;
	}

	public Long getAssDetailId(){
		return this.assDetailId;
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

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setErpMaterialCode(String erpMaterialCode){
		this.erpMaterialCode=erpMaterialCode;
	}

	public String getErpMaterialCode(){
		return this.erpMaterialCode;
	}

	public void setOutNum(Integer outNum){
		this.outNum=outNum;
	}

	public Integer getOutNum(){
		return this.outNum;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setLogiId(Long logiId){
		this.logiId=logiId;
	}

	public Long getLogiId(){
		return this.logiId;
	}

	public void setMatId(Long matId){
		this.matId=matId;
	}

	public Long getMatId(){
		return this.matId;
	}

	public void setProcessStatus(Integer processStatus){
		this.processStatus=processStatus;
	}

	public Integer getProcessStatus(){
		return this.processStatus;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setSendNum(Integer sendNum){
		this.sendNum=sendNum;
	}

	public Integer getSendNum(){
		return this.sendNum;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setIsStatus(Integer isStatus){
		this.isStatus=isStatus;
	}

	public Integer getIsStatus(){
		return this.isStatus;
	}

	public void setAllocaNum(Integer allocaNum){
		this.allocaNum=allocaNum;
	}

	public Integer getAllocaNum(){
		return this.allocaNum;
	}

	public void setAssNum(Integer assNum){
		this.assNum=assNum;
	}

	public Integer getAssNum(){
		return this.assNum;
	}

}