package com.infoservice.dms.chana.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 
 * @ClassName : PtDeliverpVO
 * @Description : 下发货运单VO
 * @author : luole CreateDate : 2013-5-28
 */
@SuppressWarnings("serial")
public class PtDeliverpVO extends BaseVO {
	private Long deliveryId;//上端：ID NUMBER(16) 下端：
	private Date deliveryTime;// 上端：发运日期 TIMESTAMP 下端：
	private String deliveryPdc;// 上端：发运地址 VARCHAR(50) 下端：
	private String deliveryOrderNo;// 上端：发运单注册号 VARCHAR(20) 下端：
	private String deliveryCompany;// 上端：货运公司 VARCHAR(60) 下端：
	private Double receivableCases;// 上端：应收箱数 NUMERIC(10,2) 下端：
	private String deliveryPayer;// 上端：运费付款人 VARCHAR(20) 下端：
	private Integer shippingWay;// 上端：货运方式 NUMERIC(8) 下端：
	private Date invoiceDate;// 上端：开票日期 TIMESTAMP 下端：
	private String remark;// 上端：备注 VARCHAR(400) 下端：
	private HashMap<Integer,BaseVO> delist;
	

	public Long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

	public HashMap<Integer, BaseVO> getDelist() {
		return delist;
	}

	public void setDelist(HashMap<Integer, BaseVO> delist) {
		this.delist = delist;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getDeliveryPdc() {
		return deliveryPdc;
	}

	public void setDeliveryPdc(String deliveryPdc) {
		this.deliveryPdc = deliveryPdc;
	}

	public String getDeliveryOrderNo() {
		return deliveryOrderNo;
	}

	public void setDeliveryOrderNo(String deliveryOrderNo) {
		this.deliveryOrderNo = deliveryOrderNo;
	}

	public String getDeliveryCompany() {
		return deliveryCompany;
	}

	public void setDeliveryCompany(String deliveryCompany) {
		this.deliveryCompany = deliveryCompany;
	}

	public Double getReceivableCases() {
		return receivableCases;
	}

	public void setReceivableCases(Double receivableCases) {
		this.receivableCases = receivableCases;
	}

	public String getDeliveryPayer() {
		return deliveryPayer;
	}

	public void setDeliveryPayer(String deliveryPayer) {
		this.deliveryPayer = deliveryPayer;
	}

	public Integer getShippingWay() {
		return shippingWay;
	}

	public void setShippingWay(Integer shippingWay) {
		this.shippingWay = shippingWay;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
