/**********************************************************************
 * <pre>
 * FILE : importCompanyBean.java
 * CLASS : importCompanyBean
 *
 * AUTHOR : lwj
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2009-12-7|   lwj   | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.bean;

import java.util.Date;

public class importCompanyBean {

	private String companyCode;
	private String companyName;
	private String companyShortName;
	private String mark;//备注
	private Date createDate;
	private Date updateDate;
	private String updateBy;
	private String createBy;
	private String aucStat;
	private String rowNum; //行号
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyShortName() {
		return companyShortName;
	}
	public void setCompanyShortName(String companyShortName) {
		this.companyShortName = companyShortName;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	public String getAucStat() {
		return aucStat;
	}
	public void setAucStat(String aucStat) {
		this.aucStat = aucStat;
	}
	public String getRowNum() {
		return rowNum;
	}
	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}
	
}
