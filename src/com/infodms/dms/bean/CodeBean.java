package com.infodms.dms.bean;

/**
 * 字典表
 * @author john
 *
 */
public class CodeBean {
	private String codeId;
	private String codeDesc;
	private String type;
	public String getCodeId() {
		return codeId;
	}
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}
	public String getCodeDesc() {
		return codeDesc;
	}
	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
