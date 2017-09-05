package com.infodms.dms.bean;

import java.util.Date;

public class CarTransmitBean {

	private Date updateDate;
	private String vhclAucId;
	private String updateBy;
	private String createBy;
	private String vin;
	private String prpsCertYn;//是否建议认证
	private Date createDate; 
	private String aucStat;
	private String vhclType; //车辆说明
	private String mark;//备注
	
	private String dlrId;//经销商Id
	private String dlrCode; //经销商代码
	private String dlrName; //经销商名称
	private String rowNum; //行号
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getVhclAucId() {
		return vhclAucId;
	}
	public void setVhclAucId(String vhclAucId) {
		this.vhclAucId = vhclAucId;
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
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getPrpsCertYn() {
		return prpsCertYn;
	}
	public void setPrpsCertYn(String prpsCertYn) {
		this.prpsCertYn = prpsCertYn;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getAucStat() {
		return aucStat;
	}
	public void setAucStat(String aucStat) {
		this.aucStat = aucStat;
	}
	public String getVhclType() {
		return vhclType;
	}
	public void setVhclType(String vhclType) {
		this.vhclType = vhclType;
	}
	public String getDlrId() {
		return dlrId;
	}
	public void setDlrId(String dlrId) {
		this.dlrId = dlrId;
	}
	public String getDlrCode() {
		return dlrCode;
	}
	public void setDlrCode(String dlrCode) {
		this.dlrCode = dlrCode;
	}
	public String getDlrName() {
		return dlrName;
	}
	public void setDlrName(String dlrName) {
		this.dlrName = dlrName;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getRowNum() {
		return rowNum;
	}
	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}
	
}
