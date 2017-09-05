package com.infodms.dms.bean;

import com.infodms.dms.po.TtAsWrOldOutNoticePO;

@SuppressWarnings("serial")
public class TtAsWrOldOutNoticeBean extends TtAsWrOldOutNoticePO {
	private String outTime;
	private Integer delFlag;
	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
}
