package com.infodms.dms.bean;

/**
 * @author john
 * 选择经销商公用模块用到的bean
 */
public class DlrBean {
	private String dlrId;
	private String dlrCode;
	private String dlrName;
	private String dlrShortName;
	private String codeName;
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
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
	public String getDlrShortName() {
		return dlrShortName;
	}
	public void setDlrShortName(String dlrShortName) {
		this.dlrShortName = dlrShortName;
	}
}
