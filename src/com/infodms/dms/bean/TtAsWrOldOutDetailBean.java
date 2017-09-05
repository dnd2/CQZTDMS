package com.infodms.dms.bean;

import com.infodms.dms.po.TtAsWrOldOutDetailPO;

@SuppressWarnings("serial")
public class TtAsWrOldOutDetailBean extends TtAsWrOldOutDetailPO {

	private String outName;
	private Integer outAmount;
	private String outTime;
	private String partCode;
	private Integer stockAmount;
	private Integer outAll;
	private String partName;
	private Long ID;
	private String dealerCode;
	private String dealerName;
	private String modelName;
	private Integer allAmount;
	private String outTypeName;
	private String tel;
	private String taxName;
	private String bank;
	private String account;
	private String dealType;
	private Double claimPrice;
	private Double labourHours;
	private Double labourAmount;
	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getOutTypeName() {
		return outTypeName;
	}

	public void setOutTypeName(String outTypeName) {
		this.outTypeName = outTypeName;
	}

	public Integer getAllAmount() {
		return allAmount;
	}

	public void setAllAmount(Integer allAmount) {
		this.allAmount = allAmount;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getOutName() {
		return outName;
	}

	public void setOutName(String outName) {
		this.outName = outName;
	}

	public Integer getOutAmount() {
		return outAmount;
	}

	public void setOutAmount(Integer outAmount) {
		this.outAmount = outAmount;
	}

	public String getPartCode() {
		return partCode;
	}

	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}

	public Integer getStockAmount() {
		return stockAmount;
	}

	public void setStockAmount(Integer stockAmount) {
		this.stockAmount = stockAmount;
	}

	public Integer getOutAll() {
		return outAll;
	}

	public void setOutAll(Integer outAll) {
		this.outAll = outAll;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Double getClaimPrice() {
		return claimPrice;
	}

	public void setClaimPrice(Double claimPrice) {
		this.claimPrice = claimPrice;
	}

	public Double getLabourHours() {
		return labourHours;
	}

	public void setLabourHours(Double labourHours) {
		this.labourHours = labourHours;
	}

	public Double getLabourAmount() {
		return labourAmount;
	}

	public void setLabourAmount(Double labourAmount) {
		this.labourAmount = labourAmount;
	}
}
