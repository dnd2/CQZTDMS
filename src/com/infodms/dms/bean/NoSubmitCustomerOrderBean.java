/**********************************************************************
 * <pre>
 * FILE : NoSubmitCustomerOrderBean.java
 * CLASS : NoSubmitCustomerOrderBean
 *
 * AUTHOR : ZHANGLONG
 *
 * FUNCTION : 未提交客户订单Bean
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2009-12-21|ZHANGLONG| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.bean;

/**
 * FUNCTION		:	未提交客户订单Bean
 * @author 		:	ZHANGLONG
 * CreateDate	:	2009-12-18
 * @version		:	0.1
 */
public class NoSubmitCustomerOrderBean {
	//订单编号
	private String customerOrderId;
	//客户名称
	private String customerName;
	//联系电话
	private String customerMobile;
	//状态
	private String status;
	
	public String getCustomerOrderId() {
		return customerOrderId;
	}
	public void setCustomerOrderId(String customerOrderId) {
		this.customerOrderId = customerOrderId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerMobile() {
		return customerMobile;
	}
	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
