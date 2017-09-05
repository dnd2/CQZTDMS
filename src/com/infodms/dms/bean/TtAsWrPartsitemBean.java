package com.infodms.dms.bean;

import com.infodms.dms.po.TtAsWrPartsitemPO;

@SuppressWarnings("serial")
public class TtAsWrPartsitemBean extends TtAsWrPartsitemPO {

	private Integer codeId;
	private String codeDesc;
	private String wrLabourname;
	private String partUseName;
	public String getPartUseName() {
		return partUseName;
	}
	public void setPartUseName(String partUseName) {
		this.partUseName = partUseName;
	}
	public Integer getCodeId() {
		return codeId;
	}
	public void setCodeId(Integer codeId) {
		this.codeId = codeId;
	}
	public String getCodeDesc() {
		return codeDesc;
	}
	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
	public String getWrLabourname() {
		return wrLabourname;
	}
	public void setWrLabourname(String wrLabourname) {
		this.wrLabourname = wrLabourname;
	}
}
