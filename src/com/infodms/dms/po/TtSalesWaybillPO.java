/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-04-28 17:18:03
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesWaybillPO extends PO{

	private Date billCrtDate;
	private Long addressId;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Long sendStatus;
	private Long areaId;
	private Long balId;
	private Long status;
	private String sendShortdealerName;
	private Long billPrintPer;
	private String orDealerName;
	private String chepaiNo;
	private String siji;
	private Long updateBy;
	private String tel;
	private Date backCrmDate;
	private Long backCrmPer;
	private String wLogiName;
	private String billNo;
	private String invoice;
	private Integer isStatus;
	private Long orDealerId;
	private Long sendDealerId;
	private Integer isConfirm;
	private String orShortdealerName;
	private Date arrDate;
	private Long logiId;
	private Integer vehNum;
	private Date billPrintDate;
	private Long wLogiId;
	private Long billCrtPer;
	private Long billId;
	private Date createDate;
	private Integer boNum;
	private String sendDealerName;
	private String scpRemark;
	private Integer scpIsys;
	
	private Date confirmDate;//运单确认时间
	private Integer invoiceStatus;//开票状态
	private Long invoiceUser;//开票人
	private Date invoiceDate;//开票时间
	private Date lastCarDate;//最后交车时间
	private Integer printCount;//打印次数
	
	private String addressInfo;
	private Long dlvBalProvId;
	private Long dlvBalCityId;
	private Long dlvBalCountyId;
	private Integer dlvShipType;
	private  Double  billAmount;//挂账金额
	private Long balProvId;
	private Long balCityId;
	private Long balCountyId;
	private Double supplyMoney;
	private Double deductMoney;
	private Double otherMoney;
	private Integer isChange;
	private Long applyId;
	private String applyRemark;
	
	public String getApplyRemark() {
		return applyRemark;
	}
	public void setApplyRemark(String applyRemark) {
		this.applyRemark = applyRemark;
	}
	public Long getApplyId() {
		return applyId;
	}
	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}
	public Double getOtherMoney() {
		return otherMoney;
	}
	public void setOtherMoney(Double otherMoney) {
		this.otherMoney = otherMoney;
	}
	public Integer getIsChange() {
		return isChange;
	}
	public void setIsChange(Integer isChange) {
		this.isChange = isChange;
	}
	public Double getSupplyMoney() {
		return supplyMoney;
	}
	public void setSupplyMoney(Double supplyMoney) {
		this.supplyMoney = supplyMoney;
	}
	public Double getDeductMoney() {
		return deductMoney;
	}
	public void setDeductMoney(Double deductMoney) {
		this.deductMoney = deductMoney;
	}
	public Long getBalProvId() {
		return balProvId;
	}
	public void setBalProvId(Long balProvId) {
		this.balProvId = balProvId;
	}
	public Long getBalCityId() {
		return balCityId;
	}
	public void setBalCityId(Long balCityId) {
		this.balCityId = balCityId;
	}
	public Long getBalCountyId() {
		return balCountyId;
	}
	public void setBalCountyId(Long balCountyId) {
		this.balCountyId = balCountyId;
	}
	/**
	 * @return the billAmount
	 */
	public Double getBillAmount() {
		return billAmount;
	}
	/**
	 * @param billAmount the billAmount to set
	 */
	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}
	public Long getDlvBalProvId() {
		return dlvBalProvId;
	}
	public void setDlvBalProvId(Long dlvBalProvId) {
		this.dlvBalProvId = dlvBalProvId;
	}
	public Long getDlvBalCityId() {
		return dlvBalCityId;
	}
	public void setDlvBalCityId(Long dlvBalCityId) {
		this.dlvBalCityId = dlvBalCityId;
	}
	public Long getDlvBalCountyId() {
		return dlvBalCountyId;
	}
	public void setDlvBalCountyId(Long dlvBalCountyId) {
		this.dlvBalCountyId = dlvBalCountyId;
	}
	public Integer getDlvShipType() {
		return dlvShipType;
	}
	public void setDlvShipType(Integer dlvShipType) {
		this.dlvShipType = dlvShipType;
	}
	public String getAddressInfo() {
		return addressInfo;
	}
	public void setAddressInfo(String addressInfo) {
		this.addressInfo = addressInfo;
	}
	/**
	 * @return the lastCarDate
	 */
	public Date getLastCarDate() {
		return lastCarDate;
	}
	/**
	 * @param lastCarDate the lastCarDate to set
	 */
	public void setLastCarDate(Date lastCarDate) {
		this.lastCarDate = lastCarDate;
	}
	public Integer getPrintCount() {
		return printCount;
	}
	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public Long getInvoiceUser() {
		return invoiceUser;
	}
	public void setInvoiceUser(Long invoiceUser) {
		this.invoiceUser = invoiceUser;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public Date getBillCrtDate() {
		return billCrtDate;
	}
	public void setBillCrtDate(Date billCrtDate) {
		this.billCrtDate = billCrtDate;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Long getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(Long sendStatus) {
		this.sendStatus = sendStatus;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public Long getBalId() {
		return balId;
	}
	public void setBalId(Long balId) {
		this.balId = balId;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public String getSendShortdealerName() {
		return sendShortdealerName;
	}
	public void setSendShortdealerName(String sendShortdealerName) {
		this.sendShortdealerName = sendShortdealerName;
	}
	public Long getBillPrintPer() {
		return billPrintPer;
	}
	public void setBillPrintPer(Long billPrintPer) {
		this.billPrintPer = billPrintPer;
	}
	public String getOrDealerName() {
		return orDealerName;
	}
	public void setOrDealerName(String orDealerName) {
		this.orDealerName = orDealerName;
	}
	public String getChepaiNo() {
		return chepaiNo;
	}
	public void setChepaiNo(String chepaiNo) {
		this.chepaiNo = chepaiNo;
	}
	public String getSiji() {
		return siji;
	}
	public void setSiji(String siji) {
		this.siji = siji;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Date getBackCrmDate() {
		return backCrmDate;
	}
	public void setBackCrmDate(Date backCrmDate) {
		this.backCrmDate = backCrmDate;
	}
	public Long getBackCrmPer() {
		return backCrmPer;
	}
	public void setBackCrmPer(Long backCrmPer) {
		this.backCrmPer = backCrmPer;
	}
	public String getwLogiName() {
		return wLogiName;
	}
	public void setwLogiName(String wLogiName) {
		this.wLogiName = wLogiName;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public Integer getIsStatus() {
		return isStatus;
	}
	public void setIsStatus(Integer isStatus) {
		this.isStatus = isStatus;
	}
	public Long getOrDealerId() {
		return orDealerId;
	}
	public void setOrDealerId(Long orDealerId) {
		this.orDealerId = orDealerId;
	}
	public Long getSendDealerId() {
		return sendDealerId;
	}
	public void setSendDealerId(Long sendDealerId) {
		this.sendDealerId = sendDealerId;
	}

	public Integer getIsConfirm() {
		return isConfirm;
	}
	public void setIsConfirm(Integer isConfirm) {
		this.isConfirm = isConfirm;
	}
	public String getOrShortdealerName() {
		return orShortdealerName;
	}
	public void setOrShortdealerName(String orShortdealerName) {
		this.orShortdealerName = orShortdealerName;
	}
	public Date getArrDate() {
		return arrDate;
	}
	public void setArrDate(Date arrDate) {
		this.arrDate = arrDate;
	}
	public Long getLogiId() {
		return logiId;
	}
	public void setLogiId(Long logiId) {
		this.logiId = logiId;
	}
	public Integer getVehNum() {
		return vehNum;
	}
	public void setVehNum(Integer vehNum) {
		this.vehNum = vehNum;
	}
	public Date getBillPrintDate() {
		return billPrintDate;
	}
	public void setBillPrintDate(Date billPrintDate) {
		this.billPrintDate = billPrintDate;
	}
	public Long getwLogiId() {
		return wLogiId;
	}
	public void setwLogiId(Long wLogiId) {
		this.wLogiId = wLogiId;
	}
	public Long getBillCrtPer() {
		return billCrtPer;
	}
	public void setBillCrtPer(Long billCrtPer) {
		this.billCrtPer = billCrtPer;
	}
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getBoNum() {
		return boNum;
	}
	public void setBoNum(Integer boNum) {
		this.boNum = boNum;
	}
	public String getSendDealerName() {
		return sendDealerName;
	}
	public void setSendDealerName(String sendDealerName) {
		this.sendDealerName = sendDealerName;
	}
	public String getScpRemark() {
		return scpRemark;
	}
	public void setScpRemark(String scpRemark) {
		this.scpRemark = scpRemark;
	}
	public Integer getScpIsys() {
		return scpIsys;
	}
	public void setScpIsys(Integer scpIsys) {
		this.scpIsys = scpIsys;
	}

}