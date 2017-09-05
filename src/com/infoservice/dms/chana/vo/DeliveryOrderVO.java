package com.infoservice.dms.chana.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class DeliveryOrderVO extends BaseVO {
	
	private String doNo; //下端：发运单号 ORDER_REGEDIT_NO VARCHAR(20)  上端：DO_NO VARCHAR2(30) 
	private String deliveryPdc; //下端：发运地址 DELIVERY_PDC VARCHAR(50)  上端：DELIVERY_PDC VARCHAR2(50) 
	private String paperDoNo; //下端：纸质发运单号 DELIVERY_ORDER_NO VARCHAR(20)  上端：DO_NO VARCHAR2(30) 
	private Integer shippingWay; //下端：货运方式 SHIPPING_WAY NUMERIC(8) 下端页面不展示，先不转换 上端：SHIPPING_CONDITION NUMBER(8)
	private String deliveryCompany; //下端：货运公司 DELIVERY_COMPANY VARCHAR(60)  上端：DELIVERY_COMPANY VARCHAR2(50) 
	private Double receivableCases; //下端：应收箱数 RECEIVABLE_CASES NUMERIC(10,2)  上端：  
	private Double factCases; //下端：实到箱数 FACT_CASES NUMERIC(10,2)  上端：  
	private String deliveryPayer; //下端：运费付款人 DELIVERY_PAYER VARCHAR(20)  上端：  
	private Date invoiceDate; //下端：开票日期 INVOICE_DATE TIMESTAMP  上端：  
	private String signforPerson; //下端：签收人 SIGNFOR_PERSON VARCHAR(30)  上端：SIGN_USER_ID  
	private Date signforDate; //下端：签收日期 SIGNFOR_DATE TIMESTAMP  上端：SIGN_DATE  
	private Date deliveryTime; //下端：发运日期 DELIVERY_TIME TIMESTAMP  上端：CREATE_DATE  
	private Integer isSigned; //下端：是否签收 IS_SIGNED NUMERIC(8)  上端：是  
	private String remark; //下端：备注 REMARK VARCHAR(300)  上端：  
	private HashMap<Integer, BaseVO> deliveryPartVoList; //下端：配件列表    上端：  
	public String getDoNo() {
		return doNo;
	}
	public void setDoNo(String doNo) {
		this.doNo = doNo;
	}
	public String getDeliveryPdc() {
		return deliveryPdc;
	}
	public void setDeliveryPdc(String deliveryPdc) {
		this.deliveryPdc = deliveryPdc;
	}
	public String getPaperDoNo() {
		return paperDoNo;
	}
	public void setPaperDoNo(String paperDoNo) {
		this.paperDoNo = paperDoNo;
	}
	public Integer getShippingWay() {
		return shippingWay;
	}
	public void setShippingWay(Integer shippingWay) {
		this.shippingWay = shippingWay;
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
	public Double getFactCases() {
		return factCases;
	}
	public void setFactCases(Double factCases) {
		this.factCases = factCases;
	}
	public String getDeliveryPayer() {
		return deliveryPayer;
	}
	public void setDeliveryPayer(String deliveryPayer) {
		this.deliveryPayer = deliveryPayer;
	}
	public String getSignforPerson() {
		return signforPerson;
	}
	public void setSignforPerson(String signforPerson) {
		this.signforPerson = signforPerson;
	}
	public Integer getIsSigned() {
		return isSigned;
	}
	public void setIsSigned(Integer isSigned) {
		this.isSigned = isSigned;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public Date getSignforDate() {
		return signforDate;
	}
	public void setSignforDate(Date signforDate) {
		this.signforDate = signforDate;
	}
	public Date getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public HashMap<Integer, BaseVO> getDeliveryPartVoList() {
		return deliveryPartVoList;
	}
	public void setDeliveryPartVoList(HashMap<Integer, BaseVO> deliveryPartVoList) {
		this.deliveryPartVoList = deliveryPartVoList;
	}

}
