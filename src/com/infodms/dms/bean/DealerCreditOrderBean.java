/**********************************************************************
 * <pre>
 * FILE : DealerCreditOrderBean.java
 * CLASS : DealerCreditOrderBean
 *
 * AUTHOR : zxy
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2009-12-25|zxy| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.bean;

import java.util.Date;

public class DealerCreditOrderBean {


	private Long orderId;
	private Long credenceId;
	private String contractNo; //订单号
	private String orderNo; //订单号
	private String orderDate; //订单日期
	private Integer amount; //确认数量
//	private String creditNum; //开票数量
	private Integer totalNum = 0; 
	private Integer lastNum = 0;
	private Integer backNum = 0; //已还款数量
	private Integer notNum = 0;
	private Integer yesNum = 0;
	private Double price; //单价
	private Long mode;		//车型 
	private String modeName;		//车型 
	private Long ed;		//业务模式
	private String edName;		//业务模式,额度模式
	private String xmname;		//额度说明，就是额度表中项目名称 
	private String credenceCode;//项目批次代码 
	private Long backType;		//还款类型 
	private String backTypeName;		//还款类型 
	private Long backDays;		//还款期限（天数）
	private String backDate;		//还款期限（日期）
	private String statusINV;		//开票状态
	private String statusINVDate;		//开票日期
	private String VIN;		//车架号
	private String flag;		//金额/台次
	private Double totalAmount; //额度总金额
	private Double lastAmount; //额度剩余金额
	private String loopFlag;		//循环标志

	private Long credenceBackId;
	private String appDate;
	private String confirmDate;
	private String status;
	private Long channel;
	private Long orgId;
	private String dealerName;

	private Long contractId;
	private String addressName;
	private String reqDate;
	private Double proprice; //折扣价
	private String transType;
	private String transDate;
	private String paraCode;
	private String paraName;
	private String rmsk;
	private String rmsk2;
	private String rmsk3;
	private Date confDate;
	private Date app1Date;
	private String seriesCode;
	private String carNameCode;
	private String carTypeCode;
	private String modelCode;
	private String name;
	private String areaName;
	private String beginDate;
	private String endDate;
	private String statusConfDate;//订单确认日期
//	private String statusINVDate;//订单开票日期
	private Double useMoney; //已还款金额
	private Double yesMoney; //已确认金额
	private Double notMoney; //未确认金额
	private Double backMoney; //已还款金额
	private String carStyle;//使用车型
	private Double aviMoney; //可使用金额
	private Long ratio; //年费率
	private String remitMarkNo; 
	private Long remitTanceId;
	private String svoNo; 
	private String svoId; 
	private String linkMan; 
	private String linkTel; 

	public Long getRatio() {
		return ratio;
	}
	public void setRatio(Long ratio) {
		this.ratio = ratio;
	}
	public String getCarStyle() {
		return carStyle;
	}
	public void setCarStyle(String carStyle) {
		this.carStyle = carStyle;
	}
	public Double getAviMoney() {
		return aviMoney;
	}
	public void setAviMoney(Double aviMoney) {
		this.aviMoney = aviMoney;
	}
	public String getRmsk() {
		return rmsk;
	}
	public void setRmsk(String rmsk) {
		this.rmsk = rmsk;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public Double getProprice() {
		return proprice;
	}
	public void setProprice(Double proprice) {
		this.proprice = proprice;
	}
	public Long getCredenceBackId() {
		return credenceBackId;
	}
	public void setCredenceBackId(Long credenceBackId) {
		this.credenceBackId = credenceBackId;
	}
	public String getAppDate() {
		return appDate;
	}
	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getStatusINV() {
		return statusINV;
	}
	public void setStatusINV(String statusINV) {
		this.statusINV = statusINV;
	}
	public String getStatusINVDate() {
		return statusINVDate;
	}
	public void setStatusINVDate(String statusINVDate) {
		this.statusINVDate = statusINVDate;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Long getMode() {
		return mode;
	}
	public void setMode(Long mode) {
		this.mode = mode;
	}
	public Long getEd() {
		return ed;
	}
	public void setEd(Long ed) {
		this.ed = ed;
	}
	public String getXmname() {
		return xmname;
	}
	public void setXmname(String xmname) {
		this.xmname = xmname;
	}
	public String getCredenceCode() {
		return credenceCode;
	}
	public void setCredenceCode(String credenceCode) {
		this.credenceCode = credenceCode;
	}
	public Long getBackType() {
		return backType;
	}
	public void setBackType(Long backType) {
		this.backType = backType;
	}
	public Long getBackDays() {
		return backDays;
	}
	public void setBackDays(Long backDays) {
		this.backDays = backDays;
	}
	public String getBackDate() {
		return backDate;
	}
	public void setBackDate(String backDate) {
		this.backDate = backDate;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getModeName() {
		return modeName;
	}
	public void setModeName(String modeName) {
		this.modeName = modeName;
	}
	public String getEdName() {
		return edName;
	}
	public void setEdName(String edName) {
		this.edName = edName;
	}
	public String getBackTypeName() {
		return backTypeName;
	}
	public void setBackTypeName(String backTypeName) {
		this.backTypeName = backTypeName;
	}
	public Long getCredenceId() {
		return credenceId;
	}
	public void setCredenceId(Long credenceId) {
		this.credenceId = credenceId;
	}
	public Integer getBackNum() {
		return backNum;
	}
	public void setBackNum(Integer backNum) {
		this.backNum = backNum;
	}
	public String getVIN() {
		return VIN;
	}
	public void setVIN(String vin) {
		VIN = vin;
	}
	public Long getContractId() {
		return contractId;
	}
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}
	public String getAddressName() {
		return addressName;
	}
	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}
	public String getReqDate() {
		return reqDate;
	}
	public void setReqDate(String reqDate) {
		this.reqDate = reqDate;
	}
	public String getParaCode() {
		return paraCode;
	}
	public void setParaCode(String paraCode) {
		this.paraCode = paraCode;
	}
	public String getParaName() {
		return paraName;
	}
	public void setParaName(String paraName) {
		this.paraName = paraName;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public Date getConfDate() {
		return confDate;
	}
	public void setConfDate(Date confDate) {
		this.confDate = confDate;
	}
	public String getSeriesCode() {
		return seriesCode;
	}
	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}
	public String getCarNameCode() {
		return carNameCode;
	}
	public void setCarNameCode(String carNameCode) {
		this.carNameCode = carNameCode;
	}
	public String getCarTypeCode() {
		return carTypeCode;
	}
	public void setCarTypeCode(String carTypeCode) {
		this.carTypeCode = carTypeCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getApp1Date() {
		return app1Date;
	}
	public void setApp1Date(Date app1Date) {
		this.app1Date = app1Date;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getLastAmount() {
		return lastAmount;
	}
	public void setLastAmount(Double lastAmount) {
		this.lastAmount = lastAmount;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getLoopFlag() {
		return loopFlag;
	}
	public void setLoopFlag(String loopFlag) {
		this.loopFlag = loopFlag;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Double getYesMoney() {
		return yesMoney;
	}
	public void setYesMoney(Double yesMoney) {
		this.yesMoney = yesMoney;
	}
	public Double getNotMoney() {
		return notMoney;
	}
	public void setNotMoney(Double notMoney) {
		this.notMoney = notMoney;
	}
	public Double getBackMoney() {
		return backMoney;
	}
	public void setBackMoney(Double backMoney) {
		this.backMoney = backMoney;
	}
	public String getStatusConfDate() {
		return statusConfDate;
	}
	public void setStatusConfDate(String statusConfDate) {
		this.statusConfDate = statusConfDate;
	}
	public Double getUseMoney() {
		return useMoney;
	}
	public void setUseMoney(Double useMoney) {
		this.useMoney = useMoney;
	}
	public String getRemitMarkNo() {
		return remitMarkNo;
	}
	public void setRemitMarkNo(String remitMarkNo) {
		this.remitMarkNo = remitMarkNo;
	}
	public Long getRemitTanceId() {
		return remitTanceId;
	}
	public void setRemitTanceId(Long remitTanceId) {
		this.remitTanceId = remitTanceId;
	}
	public String getRmsk2() {
		return rmsk2;
	}
	public void setRmsk2(String rmsk2) {
		this.rmsk2 = rmsk2;
	}
	public String getRmsk3() {
		return rmsk3;
	}
	public void setRmsk3(String rmsk3) {
		this.rmsk3 = rmsk3;
	}
	public String getSvoNo() {
		return svoNo;
	}
	public void setSvoNo(String svoNo) {
		this.svoNo = svoNo;
	}
	public String getSvoId() {
		return svoId;
	}
	public void setSvoId(String svoId) {
		this.svoId = svoId;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getLinkTel() {
		return linkTel;
	}
	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public Long getChannel() {
		return channel;
	}
	public void setChannel(Long channel) {
		this.channel = channel;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public Integer getNotNum() {
		return notNum;
	}
	public void setNotNum(Integer notNum) {
		this.notNum = notNum;
	}
	public Integer getYesNum() {
		return yesNum;
	}
	public void setYesNum(Integer yesNum) {
		this.yesNum = yesNum;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getLastNum() {
		return lastNum;
	}
	public void setLastNum(Integer lastNum) {
		this.lastNum = lastNum;
	}
}

