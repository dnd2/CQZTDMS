package com.infodms.dms.bean;

import java.util.Date;

public class CtfTargetBean {
	private String targetId;
	private Date updateDate;
	private String targetNum;
	private String updateBy;
	private String createBy;
	private String ctfYear;
	private Date createDate;
	private String dealerId;
	private String dealerCode;
	private String dealerName;
	private String row; //行数
	private String mark;//错误信息
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCtfYear() {
		return ctfYear;
	}
	public void setCtfYear(String ctfYear) {
		this.ctfYear = ctfYear;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getDealerId() {
		return dealerId;
	}
	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}
	public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getTargetNum() {
		return targetNum;
	}
	public void setTargetNum(String targetNum) {
		this.targetNum = targetNum;
	}
}
