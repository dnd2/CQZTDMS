/**********************************************************************
 * <pre>
 * FILE :   RepairItem.java
 * CLASS :  RepairItem
 *
 * AUTHOR : witti
 *
 * FUNCTION : 维修历史明细数据bean
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
 * $Id: RepairItem.java,v 1.1 2010/08/16 01:43:46 yuch Exp $
 */

package com.infodms.dms.common.repairorder.edwclient;

/**
 * Funciton     : 维修历史明细数据对象
 * Author  		: witti
 * Create_Date  : 2009-08-21
 * Version		: 0.1
 */
public class RepairItem {
	//维修历史明细数据信息
	private String balanceNo; 			// 结算单号
	private String troubleDesc; 		// 故障描述
	private String troubleCause; 		// 故障原因
	private String code; 				// 项目代码/配件代码/代码
	private String name; 				// 项目名称/配件名称/名称
	private double stdLabourHour; 		// 标准工时
	private double addLabourHour; 		// 附加工时
	private String chargeMode;			// 收费区分
	private double partQuantity;    	// 配件数量
	private String unit;           	 	// 单位
	private String remark;          	// 说明
	private String type;          		// 类型
	
	public String getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}
	public String getTroubleDesc() {
		return troubleDesc;
	}
	public void setTroubleDesc(String troubleDesc) {
		this.troubleDesc = troubleDesc;
	}
	public String getTroubleCause() {
		return troubleCause;
	}
	public void setTroubleCause(String troubleCause) {
		this.troubleCause = troubleCause;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getStdLabourHour() {
		return stdLabourHour;
	}
	public void setStdLabourHour(double stdLabourHour) {
		this.stdLabourHour = stdLabourHour;
	}
	public double getAddLabourHour() {
		return addLabourHour;
	}
	public void setAddLabourHour(double addLabourHour) {
		this.addLabourHour = addLabourHour;
	}
	public String getChargeMode() {
		return chargeMode;
	}
	public void setChargeMode(String chargeMode) {
		this.chargeMode = chargeMode;
	}
	public double getPartQuantity() {
		return partQuantity;
	}
	public void setPartQuantity(double partQuantity) {
		this.partQuantity = partQuantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
