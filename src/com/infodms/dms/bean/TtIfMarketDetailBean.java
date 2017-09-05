package com.infodms.dms.bean;

import java.util.Date;

import com.infoservice.po3.bean.PO;

public class TtIfMarketDetailBean extends PO{
	private String orderId;//工单号
	private Long dealerId;//经销商ID
	private String dealerName;//经销商
	private String linkMan;//订单联系人
	private String tel;//联系电话
	private String money;//申报金额
	private String vin;//车辆识别码
	private String group_name;//车系
	private String engine_no;//发动机编号
	private String factory_date;//出厂日期
	private String delivery_date;//购车日期
	private String history_mile;//行驶里程数
	private String customerName;//客户姓名
	private String mobile;//客户联系电话
	private String address_desc;//客户地址
	private Integer info_type;//信息类型
	private String order_date;//提报日期
	private String comp_type;//投诉类型
	//private String content;//申报内容
	private String dealerCode;
	private String dealerShortname;
	private String problemDescribe;
	private String userRequest;
	private String adviceDealMode;
	private Integer status;//市场问题工单状态
	
	public String getProblemDescribe() {
		return problemDescribe;
	}
	public void setProblemDescribe(String problemDescribe) {
		this.problemDescribe = problemDescribe;
	}
	public String getUserRequest() {
		return userRequest;
	}
	public void setUserRequest(String userRequest) {
		this.userRequest = userRequest;
	}
	public String getAdviceDealMode() {
		return adviceDealMode;
	}
	public void setAdviceDealMode(String adviceDealMode) {
		this.adviceDealMode = adviceDealMode;
	}
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getDealerShortname() {
		return dealerShortname;
	}
	public void setDealerShortname(String dealerShortname) {
		this.dealerShortname = dealerShortname;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getEngine_no() {
		return engine_no;
	}
	public void setEngine_no(String engine_no) {
		this.engine_no = engine_no;
	}
	public String getFactory_date() {
		return factory_date;
	}
	public void setFactory_date(String factory_date) {
		this.factory_date = factory_date;
	}
	public String getDelivery_date() {
		return delivery_date;
	}
	public void setDelivery_date(String delivery_date) {
		this.delivery_date = delivery_date;
	}
	public String getHistory_mile() {
		return history_mile;
	}
	public void setHistory_mile(String history_mile) {
		this.history_mile = history_mile;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress_desc() {
		return address_desc;
	}
	public void setAddress_desc(String address_desc) {
		this.address_desc = address_desc;
	}
	public Integer getInfo_type() {
		return info_type;
	}
	public void setInfo_type(Integer info_type) {
		this.info_type = info_type;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	public String getComp_type() {
		return comp_type;
	}
	public void setComp_type(String comp_type) {
		this.comp_type = comp_type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
//	public String getContent() {
//		return content;
//	}
//	public void setContent(String content) {
//		this.content = content;
//	}
}
