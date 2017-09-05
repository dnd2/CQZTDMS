package com.infodms.dms.bean;

import com.infodms.dms.po.TtAsWrLabouritemPO;

@SuppressWarnings("serial")
public class TtAsWrLabouritemBean extends TtAsWrLabouritemPO {
	private String malName;
	private String partCode;
	private String mainPartCode;
	private String showMainPart;
	public String getPartCode() {
		return partCode;
	}

	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}

	public String getMainPartCode() {
		return mainPartCode;
	}

	public void setMainPartCode(String mainPartCode) {
		this.mainPartCode = mainPartCode;
	}

	public String getMalName() {
		return malName;
	}

	public void setMalName(String malName) {
		this.malName = malName;
	}

	public String getShowMainPart() {
		return showMainPart;
	}

	public void setShowMainPart(String showMainPart) {
		this.showMainPart = showMainPart;
	}
}
