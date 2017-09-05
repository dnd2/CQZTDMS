package com.infodms.dms.bean;

/**
 * 三包策略查询条件
 * @author XZM
 */
public class TreeGuaranteesStrategyBean {
	/** 三包策略代码 */
	private String gameCode;
	/** 三包策略名称 */
	private String gameName;
	/** 三包策略保养次数 */
	private String maintaimNum;
	/** 三包策略状态 */
	private String status;
	/** 三包规则代码 */
	private String ruleCode;
	/** 三包规则名称 */
	private String ruleName;
	/** 分页页数 */
	private Integer curPage;
	/** 分页每页记录数 */
	private Integer pageSize;
	/** 用户所属公司ID */
	private Long companyId;
	/** 三包策略类型 */
	private String gameType;

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getMaintaimNum() {
		return maintaimNum;
	}

	public void setMaintaimNum(String maintaimNum) {
		this.maintaimNum = maintaimNum;
	}

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

	public Integer getCurPage() {
		return curPage;
	}

	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	
}
