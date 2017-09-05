package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;


@SuppressWarnings("serial")
public class TtAsPartBorrowStorePO extends PO{

	private String dealerName;
	private String claimNo;
	private Integer claimType;
	private Integer allAmount;
	private String supplyName;
	private String vin;
	private String partName;
	private String dealerCode;
	private Long id;
	private String partCode;
	private String modelName;
	private String supplyCode;
	private Integer status;
	private Long pkid;
	private String createPerson;
	private Date createDate;
	private String nextPerson;
	private Date nextDate;
	private String borrowMan;
	private String returnMan;

	public String getBorrowMan() {
		return borrowMan;
	}

	public void setBorrowMan(String borrowMan) {
		this.borrowMan = borrowMan;
	}

	public String getReturnMan() {
		return returnMan;
	}

	public void setReturnMan(String returnMan) {
		this.returnMan = returnMan;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setClaimNo(String claimNo){
		this.claimNo=claimNo;
	}

	public String getClaimNo(){
		return this.claimNo;
	}

	public void setClaimType(Integer claimType){
		this.claimType=claimType;
	}

	public Integer getClaimType(){
		return this.claimType;
	}

	public void setAllAmount(Integer allAmount){
		this.allAmount=allAmount;
	}

	public Integer getAllAmount(){
		return this.allAmount;
	}

	public void setSupplyName(String supplyName){
		this.supplyName=supplyName;
	}

	public String getSupplyName(){
		return this.supplyName;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setModelName(String modelName){
		this.modelName=modelName;
	}

	public String getModelName(){
		return this.modelName;
	}

	public void setSupplyCode(String supplyCode){
		this.supplyCode=supplyCode;
	}

	public String getSupplyCode(){
		return this.supplyCode;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getNextPerson() {
		return nextPerson;
	}

	public void setNextPerson(String nextPerson) {
		this.nextPerson = nextPerson;
	}

	public Date getNextDate() {
		return nextDate;
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}

}