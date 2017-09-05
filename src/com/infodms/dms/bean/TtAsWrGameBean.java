package com.infodms.dms.bean;

import com.infodms.dms.po.TtAsWrGamePO;

/**
 * 三包策略信息
 * @author XZM
 */
public class TtAsWrGameBean extends TtAsWrGamePO {
	private static final long serialVersionUID = -799363481092290406L;
	
	/** 三包策略状态名称 */
	private String status;
	/** 三包策略对应规则代码 */
	private String ruleCode;
	/** 三包策略对应规则名称 */
	private String ruleName;
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}	
}
