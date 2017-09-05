package com.infodms.dms.bean;

/**
 * @Title: PartinfoBean.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-3
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class PartinfoBean {   
	private String orderId;   //采购订单Id
	private String partCode;   //配件代码
	private String partName;   //配件名称
	private String supplierCode;//供应商代码
	private String supplierName;//供应商名称
	private String dealerCode;//经销商代码
	private String dealerName;//经销商名称
	private String partId;//配件ID
	private String dealerId;//经销商ID
	private String beginTime;//开始时间
	private String endTime;//结束时间
	private String doStatus;//库存状态
	private String dcCode;//库存代码
	private String dcName;//库存名称
	private String dcId;//库存ID
	private String dealerLevel;//经销商级别
	private String startDate;//提报开始时间
	private String endDate;//提报结束时间
	private String handleDate;//处理时间
	private String orderMaxLines;//提报最大行数
	private String allowSubmitTimes;//周期内允许提报次数
	private String discountRate;//折扣
	private String cycleType;//周期类型
	private String paramId;//规则ID
	private String sehDate;//规则时间的连串
	private String orderNo;//采购订单编号
	private String orderStatus;//订单状态
	private String requireDate;//要求到货时间
	private String unit;//单位
	private String miniPack;//最小包装数
	private String count;//订购数量
	private String remark;//备注
	private String paperQuantity;//账面库存
	private String safeQuantity;//安全库存
	private String salePrice;//销售价格
	private String disPrice;//折后价格
	private String orderPrice;//销售总价格
	private String orderCount;//销售个数
	private String secondPaperQuantity;//二级账面库存
	private String secondSafeQuantity;//二级安全库存
	private String doCount;//货运数量
	private String signCount;//签收数量
	private String signNo;
	private String doNo;//货运单号
	private String orgCode;//组织代码
	
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getDoNo() {
		return doNo;
	}
	public void setDoNo(String doNo) {
		this.doNo = doNo;
	}
	public String getSecondPaperQuantity() {
		return secondPaperQuantity;
	}
	public void setSecondPaperQuantity(String secondPaperQuantity) {
		this.secondPaperQuantity = secondPaperQuantity;
	}
	public String getSecondSafeQuantity() {
		return secondSafeQuantity;
	}
	public void setSecondSafeQuantity(String secondSafeQuantity) {
		this.secondSafeQuantity = secondSafeQuantity;
	}
	public String getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}
	public String getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getSehDate() {
		return sehDate;
	}
	public void setSehDate(String sehDate) {
		this.sehDate = sehDate;
	}
	public String getDealerLevel() {
		return dealerLevel;
	}
	public void setDealerLevel(String dealerLevel) {
		this.dealerLevel = dealerLevel;
	}
	public String getDealerId() {
		return dealerId;
	}
	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getPartId() {
		return partId;
	}
	public void setPartId(String partId) {
		this.partId = partId;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDoStatus() {
		return doStatus;
	}
	public void setDoStatus(String doStatus) {
		this.doStatus = doStatus;
	}
	public String getDcCode() {
		return dcCode;
	}
	public void setDcCode(String dcCode) {
		this.dcCode = dcCode;
	}
	public String getDcName() {
		return dcName;
	}
	public void setDcName(String dcName) {
		this.dcName = dcName;
	}
	public String getDcId() {
		return dcId;
	}
	public void setDcId(String dcId) {
		this.dcId = dcId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getHandleDate() {
		return handleDate;
	}
	public void setHandleDate(String handleDate) {
		this.handleDate = handleDate;
	}
	public String getOrderMaxLines() {
		return orderMaxLines;
	}
	public void setOrderMaxLines(String orderMaxLines) {
		this.orderMaxLines = orderMaxLines;
	}
	public String getAllowSubmitTimes() {
		return allowSubmitTimes;
	}
	public void setAllowSubmitTimes(String allowSubmitTimes) {
		this.allowSubmitTimes = allowSubmitTimes;
	}
	public String getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}
	public String getCycleType() {
		return cycleType;
	}
	public void setCycleType(String cycleType) {
		this.cycleType = cycleType;
	}
	public String getParamId() {
		return paramId;
	}
	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
	public String getRequireDate() {
		return requireDate;
	}
	public void setRequireDate(String requireDate) {
		this.requireDate = requireDate;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getMiniPack() {
		return miniPack;
	}
	public void setMiniPack(String miniPack) {
		this.miniPack = miniPack;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((partId == null) ? 0 : partId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PartinfoBean other = (PartinfoBean) obj;
		if (partId == null) {
			if (other.partId != null)
				return false;
		} else if (!partId.equals(other.partId))
			return false;
		return true;
	}
	public String getPaperQuantity() {
		return paperQuantity;
	}
	public void setPaperQuantity(String paperQuantity) {
		this.paperQuantity = paperQuantity;
	}
	public String getSafeQuantity() {
		return safeQuantity;
	}
	public void setSafeQuantity(String safeQuantity) {
		this.safeQuantity = safeQuantity;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	public String getDisPrice() {
		return disPrice;
	}
	public void setDisPrice(String disPrice) {
		this.disPrice = disPrice;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getDoCount() {
		return doCount;
	}
	public void setDoCount(String doCount) {
		this.doCount = doCount;
	}
	public String getSignCount() {
		return signCount;
	}
	public void setSignCount(String signCount) {
		this.signCount = signCount;
	}
	public String getSignNo() {
		return signNo;
	}
	public void setSignNo(String signNo) {
		this.signNo = signNo;
	}
}
