/**********************************************************************
* <pre>
* FILE : IndivDispostionBean.java
* CLASS : IndivDispostionBean
* 
* AUTHOR : ZhangLei
*
* FUNCTION :个人任务清单BEAN.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-09-05| ZhangLei  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: IndivDispostionBean.java,v 1.1 2010/08/16 01:42:33 yuch Exp $
 */
package com.infodms.dms.bean;

import java.util.Date;

public class IndivDispostionBean {
	//TableID
	private String tableId;
	//车辆ID
	private String vhclId;
	//评估师ID
	private String evaluater;
	//计划时间
	private Date plannedDate;
	//计划内容
	private String plannedContent;
	//客户ID
	private String custId;
	//客户类型
	private String cusype;
	//客户名称
	private String custName;
	//客户联系方式
	private String mainContMode;
	//经销商ID
	private String dlrId;
	//评估师部门ID
	private String evaDeptId;
	//VIN
	private String VIN;
	//车牌号
	private String vhclLic;
	//车辆收购价
	private String vhclPurPrice	;
	//车辆销售价
	private String vhclSalePrice;
	//车辆调拨价
	private String vhclAlocPrice;
	//出入库类型ID
	private String invInoutTypeId;
	public String getVhclId() {
		return vhclId;
	}
	public void setVhclId(String vhclId) {
		this.vhclId = vhclId;
	}
	public String getInvInoutTypeId() {
		return invInoutTypeId;
	}
	public void setInvInoutTypeId(String invInoutTypeId) {
		this.invInoutTypeId = invInoutTypeId;
	}
	public String getVhclAlocPrice() {
		return vhclAlocPrice;
	}
	public void setVhclAlocPrice(String vhclAlocPrice) {
		this.vhclAlocPrice = vhclAlocPrice;
	}
	public String getVhclSalePrice() {
		return vhclSalePrice;
	}
	public void setVhclSalePrice(String vhclSalePrice) {
		this.vhclSalePrice = vhclSalePrice;
	}
	public String getVIN() {
		return VIN;
	}
	public void setVIN(String vin) {
		VIN = vin;
	}
	public String getVhclLic() {
		return vhclLic;
	}
	public void setVhclLic(String vhclLic) {
		this.vhclLic = vhclLic;
	}
	public String getVhclPurPrice() {
		return vhclPurPrice;
	}
	public void setVhclPurPrice(String vhclPurPrice) {
		this.vhclPurPrice = vhclPurPrice;
	}
	public String getEvaDeptId() {
		return evaDeptId;
	}
	public void setEvaDeptId(String evaDeptId) {
		this.evaDeptId = evaDeptId;
	}
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getEvaluater() {
		return evaluater;
	}
	public void setEvaluater(String evaluater) {
		this.evaluater = evaluater;
	}
	
	public Date getPlannedDate() {
		return plannedDate;
	}
	public void setPlannedDate(Date plannedDate) {
		this.plannedDate = plannedDate;
	}
	public String getPlannedContent() {
		return plannedContent;
	}
	public void setPlannedContent(String plannedContent) {
		this.plannedContent = plannedContent;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCusype() {
		return cusype;
	}
	public void setCusype(String cusype) {
		this.cusype = cusype;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getMainContMode() {
		return mainContMode;
	}
	public void setMainContMode(String mainContMode) {
		this.mainContMode = mainContMode;
	}
	public String getDlrId() {
		return dlrId;
	}
	public void setDlrId(String dlrId) {
		this.dlrId = dlrId;
	}
}
