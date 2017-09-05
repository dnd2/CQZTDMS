package com.infodms.dms.bean;

import java.util.Date;

import com.infoservice.po3.bean.PO;

public class TtIfMarketBean extends PO{
	private String orderId;//工单号
	private Long dealer_id;//经销商ID
	private String dealer_name;//经销商名称
	private String dealer_code;//经销商代码
	private String dealer_shortname;//经销商简称
	private String vin;//车辆识别码
	private String group_name;//车系
	private Double money;//申报金额
	private String compType;//投诉类型
	private Integer infoType;//信息类型
	private String content;//申报内容
	private String orderDate;//提报日期
	private String code_desc;//工单状态
	private Date createDate;//入库时间
	private Long createBy;//入库人id
	private Date updateDate;//修改日期
	private Long updateBy;//修改
	private String linkMan;//订单联系人id
	private String tel;//联系电话
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Long getDealer_id() {
		return dealer_id;
	}
	public void setDealer_id(Long dealer_id) {
		this.dealer_id = dealer_id;
	}
	public String getDealer_name() {
		return dealer_name;
	}
	public void setDealer_name(String dealer_name) {
		this.dealer_name = dealer_name;
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
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getCompType() {
		return compType;
	}
	public void setCompType(String compType) {
		this.compType = compType;
	}
	public Integer getInfoType() {
		return infoType;
	}
	public void setInfoType(Integer infoType) {
		this.infoType = infoType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getCode_desc() {
		return code_desc;
	}
	public void setCode_desc(String code_desc) {
		this.code_desc = code_desc;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
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
	public String getDealer_code() {
		return dealer_code;
	}
	public void setDealer_code(String dealer_code) {
		this.dealer_code = dealer_code;
	}
	public String getDealer_shortname() {
		return dealer_shortname;
	}
	public void setDealer_shortname(String dealer_shortname) {
		this.dealer_shortname = dealer_shortname;
	}
}
