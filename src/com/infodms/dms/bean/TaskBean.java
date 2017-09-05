/**********************************************************************
* <pre>
* FILE : TaskBean.java
* CLASS : TaskBean
* 
* AUTHOR : wangjianguo
*
* FUNCTION :定时任务BEAN.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-08-27| wangjianguo  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: TaskBean.java,v 1.1 2010/08/16 01:42:33 yuch Exp $
 */
package com.infodms.dms.bean;

public class TaskBean {
	//组织ID
	private String orgId;
	//品牌ID
	private String brandId;
	//品牌名称
	private String brandName;
	//组织名称
	private String orgName;
	//销售任务数量
	private String amount;
	//年度
	private String years;
	//版本号
	private String version;
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getYears() {
		return years;
	}
	public void setYears(String years) {
		this.years = years;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
}
