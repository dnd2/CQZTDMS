/**********************************************************************
 * <pre>
 * FILE :   RepairOrder.java
 * CLASS :  RepairOrder
 *
 * AUTHOR : witti
 *
 * FUNCTION : 维修历史主信息数据bean
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|    DATE    |   NAME    |    REASON   | CHANGE REQ.
9
 *----------------------------------------------------------------------
 *         | 2009-08-21 |  witti    |    Created  |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
/**
 * $Id: RepairOrder.java,v 1.1 2010/08/16 01:43:46 yuch Exp $
 */

package com.infodms.dms.common.repairorder.edwclient;

/**
 * Funciton     : 维修历史主数据对象
 * Author  		: witti
 * Create_Date  : 2009-08-21
 * Vertion		: 0.1
 */
public class RepairOrder {
	//维修历史主数据信息
	private String balanceNo; 			// 结算单号
	private String roNo; 				// 工单号
	private String startTime; 			// 开单日期
	private String license; 			// 车牌号
	private String roType; 				// 工单类型
	private String repairType; 			// 维修类型
	private String inMileage; 			// 进厂里程
	private String isChangeMileage;		// 是否换表
	
	public String getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}
	public String getRoNo() {
		return roNo;
	}
	public void setRoNo(String roNo) {
		this.roNo = roNo;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getRoType() {
		return roType;
	}
	public void setRoType(String roType) {
		this.roType = roType;
	}
	public String getRepairType() {
		return repairType;
	}
	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}
	public String getInMileage() {
		return inMileage;
	}
	public void setInMileage(String inMileage) {
		this.inMileage = inMileage;
	}
	public String getIsChangeMileage() {
		return isChangeMileage;
	}
	public void setIsChangeMileage(String isChangeMileage) {
		this.isChangeMileage = isChangeMileage;
	}
	
}
