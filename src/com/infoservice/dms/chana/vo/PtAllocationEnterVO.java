package com.infoservice.dms.chana.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 
 * @ClassName : PtAllocationEnterVO
 * @Description : 配件调拨入库VO
 * @author : wangjun 
 * @CreateDate : 2013-5-28
 */
@SuppressWarnings("serial")
public class PtAllocationEnterVO extends BaseVO {
	private Long enterId;//上端：下发ID NUMBER(16) 下端：
	private Date allocationTime;//上端：调拨日期 TIMESTAMP 下端：
	private String supplierName;//上端：供货商名称 VARCHAR(50) 下端：
	private String allocationEnterNo;//上端：调拨入库单号 VARCHAR(20) 唯一  下端：
	private String allocationOutNo;//上端：分厂调拨出库单号 VARCHAR(20) 下端：
	private String supplierCode;//上端：供货商代码 VARCHAR(60) 下端：
	private String allocationOutCode;//上端：调出经销商代码 VARCHAR(60) 下端：
	private String allocationOutName;//上端：调出经销商名称 VARCHAR(60) 下端：
	private String remark;//上端：备注 VARCHAR(400) 下端：
	public Long getEnterId() {
		return enterId;
	}

	public void setEnterId(Long enterId) {
		this.enterId = enterId;
	}
	private HashMap<Integer,BaseVO> delist;
	

	public HashMap<Integer, BaseVO> getDelist() {
		return delist;
	}

	public void setDelist(HashMap<Integer, BaseVO> delist) {
		this.delist = delist;
	}

	public Date getAllocationTime() {
		return allocationTime;
	}
	public void setAllocationTime(Date allocationTime) {
		this.allocationTime = allocationTime;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getAllocationEnterNo() {
		return allocationEnterNo;
	}
	public void setAllocationEnterNo(String allocationEnterNo) {
		this.allocationEnterNo = allocationEnterNo;
	}
	public String getAllocationOutNo() {
		return allocationOutNo;
	}
	public void setAllocationOutNo(String allocationOutNo) {
		this.allocationOutNo = allocationOutNo;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getAllocationOutCode() {
		return allocationOutCode;
	}
	public void setAllocationOutCode(String allocationOutCode) {
		this.allocationOutCode = allocationOutCode;
	}
	public String getAllocationOutName() {
		return allocationOutName;
	}
	public void setAllocationOutName(String allocationOutName) {
		this.allocationOutName = allocationOutName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
