package com.infoservice.dms.chana.vo;

import java.util.Date;

public class FailCustomerVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date visitDate;        //回访日期
	private String soldBy;           //销售顾问
	private String customerName;     //客户姓名
	private String phone;            //电话
	private String model;            //意向车型
	private String failModel;        //战败车型
	private String abortReason;      //战败原因
	private String remark;           //备注
	public Date getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}
	public String getSoldBy() {
		return soldBy;
	}
	public void setSoldBy(String soldBy) {
		this.soldBy = soldBy;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getFailModel() {
		return failModel;
	}
	public void setFailModel(String failModel) {
		this.failModel = failModel;
	}
	public String getAbortReason() {
		return abortReason;
	}
	public void setAbortReason(String abortReason) {
		this.abortReason = abortReason;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
