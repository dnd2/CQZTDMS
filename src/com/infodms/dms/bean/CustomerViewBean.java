/**********************************************************************
* <pre>
* FILE : CustomerViewBean.java
* CLASS : CustomerViewBean
*
* AUTHOR : John
*
* FUNCTION : 客户360视图Bean
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
* 		  |2009-9-14| John| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: CustomerViewBean.java,v 1.1 2010/08/16 01:42:32 yuch Exp $
*/
package com.infodms.dms.bean;

/**
 * 功能说明：客户360视图Bean
 * 典型用法：
 * 示例程序如下：
 * 特殊用法：
 * 创建者：John
 * 创建时间：2009-9-14
 * 修改人：
 * 修改时间：
 * 修改原因：
 * 修改内容：
 * 版本：0.1
 */
public class CustomerViewBean {
	private String vin; //vin
	private String doDate; //操作日期
	private String custViewType; //类型
	private String plannedContent; //计划/执行内容
	private String level; //意向级别
	private String psn; //业务人员
	private String remark; //备注
	
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getDoDate() {
		return doDate;
	}
	public void setDoDate(String doDate) {
		this.doDate = doDate;
	}
	public String getCustViewType() {
		return custViewType;
	}
	public void setCustViewType(String custViewType) {
		this.custViewType = custViewType;
	}
	public String getPlannedContent() {
		return plannedContent;
	}
	public void setPlannedContent(String plannedContent) {
		this.plannedContent = plannedContent;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getPsn() {
		return psn;
	}
	public void setPsn(String psn) {
		this.psn = psn;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
