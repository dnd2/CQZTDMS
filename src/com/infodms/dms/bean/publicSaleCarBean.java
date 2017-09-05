package com.infodms.dms.bean;

import java.util.Date;

public class publicSaleCarBean {

	//TT_VHCL_CTF_APY 人证申请表  TT_VHCL_AUC 拍卖车信息表。 TM_DLR经销商表
	
	//SGM 拍卖车查询Bean
	private String vhclAucId;//拍卖车id
	private String vin;//vin
	private String vhclType; //车辆说明
    private String dlrId;//经销商Id
	private String dlrCode; //经销商代码
	private String dlrName; //经销商名称 
	private String dlrShortName;//经销商简称
	private String prpsCertYn;//是否建议认证 
	private String certStat; //认证状态
	private String appDate; //申请日期
	private String appvDate;//审核日期
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
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
	public String getPrpsCertYn() {
		return prpsCertYn;
	}
	public void setPrpsCertYn(String prpsCertYn) {
		this.prpsCertYn = prpsCertYn;
	}
	public String getCertStat() {
		return certStat;
	}
	public void setCertStat(String certStat) {
		this.certStat = certStat;
	}
 
	public String getAppDate() {
		return appDate;
	}
	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}
	public String getAppvDate() {
		return appvDate;
	}
	public void setAppvDate(String appvDate) {
		this.appvDate = appvDate;
	}
	public String getVhclAucId() {
		return vhclAucId;
	}
	public void setVhclAucId(String vhclAucId) {
		this.vhclAucId = vhclAucId;
	}
	public String getDlrShortName() {
		return dlrShortName;
	}
	public void setDlrShortName(String dlrShortName) {
		this.dlrShortName = dlrShortName;
	}
	
	
}
