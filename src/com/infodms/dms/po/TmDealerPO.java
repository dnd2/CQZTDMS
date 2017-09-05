/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-10-31 20:35:11
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmDealerPO extends PO{

	/*
	 * 经销商拼音搜索  20150520
	 */
	private String pinyin;
	
	private Long cityId;
	private Date imageDate;
	private String remark;
	private String taxDisrate;
	private String invoiceAccount;
	private String chAddress;
	private Date destroydate;
	private Long serviceStatus;
	private Integer isYth;
	private String taxesNo;
	private String legalTelphone;
	private Long oemCompanyId;
	private String webmasterPhone;
	private String zyScope;
	private Integer imageLevel;
	private Long updateBy;
	private String legal;
	private Date changeDate;
	private Long priceId;
	private String balanceLevel;
	private Integer isStatus;
	private String webmasterName;
	private String zccode;
	private String personCharge;
	private Integer taxLevel;
	private Integer imageLevel2;
	private String zipCode;
	private String spyPhone;
	private String taxInvoice;
	private String dealerCode;
	private Date endBalanceDate;
	private String treeCode;
	private Integer dealerLabourType;
	private Integer mainResources;
	private String jyScope;
	private Date beginBalanceDate;
	private String invoiceLevel;
	private Integer counties;
	private String linkMan;
	private String hotLinePhone;
	private String legalEmail;
	private String legalPhone;
	private String invoicePhone;
	private Integer dealerType;
	private String dealerStar;
	private Integer adminLevel;
	private String faxNo;
	private String invoiceAdd;
	private Integer isDqv;
	private Date createDate;
	private Long dealerOrgId;
	private Integer serviceLevel;
	private String zzaddress;
	private String invoiceTelphone;
	private Long dealerId;
	private String marketName;
	private Integer isSpecial;
	private Date updateDate;
	private String marketTel;
	private Long createBy;
	private Long parentDealerD;
	private Integer pdealerType;
	private Integer status;
	private Integer sysStatus;
	private String phone;
	private String bank;
	private String invoicePostAdd;
	private Integer dealerClass;
	private String dutyPhone;
	private String taxpayerNo;
	private String erpCode;
	private String email;
	private String invoicePersion;
	private String beginBank;
	private String township;
	private Integer isNbdw;
	private String chAddress2;
	private String dealerShortname;
	private Integer isScan;
	private String spyMan;
	private Long companyId;
	private String address;
	private Integer areaLevel;
	private Integer secendAutidStatus;
	private String oldDealerCode;
	private Long provinceId;
	private String legalTel;
	private Date beginOldDate;
	private String dealerName;
	private Date endOldDate;
	private Integer dealerLevel;
	private String oldDealerCode2;
	private Integer clCqFlag;
	private Integer taxRateId;
	private Integer isStatus1;
	private String secondDealerMail;//二级网络邮箱
	private String billAddress;//开票地址  2017-08-16新增
	
	public String getBillAddress() {
		return billAddress;
	}

	public void setBillAddress(String billAddress) {
		this.billAddress = billAddress;
	}

	public String getSecondDealerMail() {
		return secondDealerMail;
	}

	public void setSecondDealerMail(String secondDealerMail) {
		this.secondDealerMail = secondDealerMail;
	}

	public Integer getIsStatus1() {
		return isStatus1;
	}

	public void setIsStatus1(Integer isStatus1) {
		this.isStatus1 = isStatus1;
	}

	private String brand;
	private Date sitedate;
	private String taxpayerNature;

	public void setCityId(Long cityId){
		this.cityId=cityId;
	}

	public Long getCityId(){
		return this.cityId;
	}

	public void setImageDate(Date imageDate){
		this.imageDate=imageDate;
	}

	public Date getImageDate(){
		return this.imageDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setTaxDisrate(String taxDisrate){
		this.taxDisrate=taxDisrate;
	}

	public String getTaxDisrate(){
		return this.taxDisrate;
	}

	public void setInvoiceAccount(String invoiceAccount){
		this.invoiceAccount=invoiceAccount;
	}

	public String getInvoiceAccount(){
		return this.invoiceAccount;
	}

	public void setChAddress(String chAddress){
		this.chAddress=chAddress;
	}

	public String getChAddress(){
		return this.chAddress;
	}

	public void setDestroydate(Date destroydate){
		this.destroydate=destroydate;
	}

	public Date getDestroydate(){
		return this.destroydate;
	}

	public void setServiceStatus(Long serviceStatus){
		this.serviceStatus=serviceStatus;
	}

	public Long getServiceStatus(){
		return this.serviceStatus;
	}

	public void setIsYth(Integer isYth){
		this.isYth=isYth;
	}

	public Integer getIsYth(){
		return this.isYth;
	}

	public void setTaxesNo(String taxesNo){
		this.taxesNo=taxesNo;
	}

	public String getTaxesNo(){
		return this.taxesNo;
	}

	public void setLegalTelphone(String legalTelphone){
		this.legalTelphone=legalTelphone;
	}

	public String getLegalTelphone(){
		return this.legalTelphone;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setWebmasterPhone(String webmasterPhone){
		this.webmasterPhone=webmasterPhone;
	}

	public String getWebmasterPhone(){
		return this.webmasterPhone;
	}

	public void setZyScope(String zyScope){
		this.zyScope=zyScope;
	}

	public String getZyScope(){
		return this.zyScope;
	}

	public void setImageLevel(Integer imageLevel){
		this.imageLevel=imageLevel;
	}

	public Integer getImageLevel(){
		return this.imageLevel;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setLegal(String legal){
		this.legal=legal;
	}

	public String getLegal(){
		return this.legal;
	}

	public void setChangeDate(Date changeDate){
		this.changeDate=changeDate;
	}

	public Date getChangeDate(){
		return this.changeDate;
	}

	public void setPriceId(Long priceId){
		this.priceId=priceId;
	}

	public Long getPriceId(){
		return this.priceId;
	}

	public void setBalanceLevel(String balanceLevel){
		this.balanceLevel=balanceLevel;
	}

	public String getBalanceLevel(){
		return this.balanceLevel;
	}

	public void setIsStatus(Integer isStatus){
		this.isStatus=isStatus;
	}

	public Integer getIsStatus(){
		return this.isStatus;
	}

	public void setWebmasterName(String webmasterName){
		this.webmasterName=webmasterName;
	}

	public String getWebmasterName(){
		return this.webmasterName;
	}

	public void setZccode(String zccode){
		this.zccode=zccode;
	}

	public String getZccode(){
		return this.zccode;
	}

	public void setPersonCharge(String personCharge){
		this.personCharge=personCharge;
	}

	public String getPersonCharge(){
		return this.personCharge;
	}

	public void setTaxLevel(Integer taxLevel){
		this.taxLevel=taxLevel;
	}

	public Integer getTaxLevel(){
		return this.taxLevel;
	}

	public void setImageLevel2(Integer imageLevel2){
		this.imageLevel2=imageLevel2;
	}

	public Integer getImageLevel2(){
		return this.imageLevel2;
	}

	public void setZipCode(String zipCode){
		this.zipCode=zipCode;
	}

	public String getZipCode(){
		return this.zipCode;
	}

	public void setSpyPhone(String spyPhone){
		this.spyPhone=spyPhone;
	}

	public String getSpyPhone(){
		return this.spyPhone;
	}

	public void setTaxInvoice(String taxInvoice){
		this.taxInvoice=taxInvoice;
	}

	public String getTaxInvoice(){
		return this.taxInvoice;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setEndBalanceDate(Date endBalanceDate){
		this.endBalanceDate=endBalanceDate;
	}

	public Date getEndBalanceDate(){
		return this.endBalanceDate;
	}

	public void setTreeCode(String treeCode){
		this.treeCode=treeCode;
	}

	public String getTreeCode(){
		return this.treeCode;
	}

	public void setDealerLabourType(Integer dealerLabourType){
		this.dealerLabourType=dealerLabourType;
	}

	public Integer getDealerLabourType(){
		return this.dealerLabourType;
	}

	public void setMainResources(Integer mainResources){
		this.mainResources=mainResources;
	}

	public Integer getMainResources(){
		return this.mainResources;
	}

	public void setJyScope(String jyScope){
		this.jyScope=jyScope;
	}

	public String getJyScope(){
		return this.jyScope;
	}

	public void setBeginBalanceDate(Date beginBalanceDate){
		this.beginBalanceDate=beginBalanceDate;
	}

	public Date getBeginBalanceDate(){
		return this.beginBalanceDate;
	}

	public void setInvoiceLevel(String invoiceLevel){
		this.invoiceLevel=invoiceLevel;
	}

	public String getInvoiceLevel(){
		return this.invoiceLevel;
	}

	public void setCounties(Integer counties){
		this.counties=counties;
	}

	public Integer getCounties(){
		return this.counties;
	}

	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}

	public String getLinkMan(){
		return this.linkMan;
	}

	public void setHotLinePhone(String hotLinePhone){
		this.hotLinePhone=hotLinePhone;
	}

	public String getHotLinePhone(){
		return this.hotLinePhone;
	}

	public void setLegalEmail(String legalEmail){
		this.legalEmail=legalEmail;
	}

	public String getLegalEmail(){
		return this.legalEmail;
	}

	public void setLegalPhone(String legalPhone){
		this.legalPhone=legalPhone;
	}

	public String getLegalPhone(){
		return this.legalPhone;
	}

	public void setInvoicePhone(String invoicePhone){
		this.invoicePhone=invoicePhone;
	}

	public String getInvoicePhone(){
		return this.invoicePhone;
	}

	public void setDealerType(Integer dealerType){
		this.dealerType=dealerType;
	}

	public Integer getDealerType(){
		return this.dealerType;
	}

	public void setDealerStar(String dealerStar){
		this.dealerStar=dealerStar;
	}

	public String getDealerStar(){
		return this.dealerStar;
	}

	public void setAdminLevel(Integer adminLevel){
		this.adminLevel=adminLevel;
	}

	public Integer getAdminLevel(){
		return this.adminLevel;
	}

	public void setFaxNo(String faxNo){
		this.faxNo=faxNo;
	}

	public String getFaxNo(){
		return this.faxNo;
	}

	public void setInvoiceAdd(String invoiceAdd){
		this.invoiceAdd=invoiceAdd;
	}

	public String getInvoiceAdd(){
		return this.invoiceAdd;
	}

	public void setIsDqv(Integer isDqv){
		this.isDqv=isDqv;
	}

	public Integer getIsDqv(){
		return this.isDqv;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDealerOrgId(Long dealerOrgId){
		this.dealerOrgId=dealerOrgId;
	}

	public Long getDealerOrgId(){
		return this.dealerOrgId;
	}

	public void setServiceLevel(Integer serviceLevel){
		this.serviceLevel=serviceLevel;
	}

	public Integer getServiceLevel(){
		return this.serviceLevel;
	}

	public void setZzaddress(String zzaddress){
		this.zzaddress=zzaddress;
	}

	public String getZzaddress(){
		return this.zzaddress;
	}

	public void setInvoiceTelphone(String invoiceTelphone){
		this.invoiceTelphone=invoiceTelphone;
	}

	public String getInvoiceTelphone(){
		return this.invoiceTelphone;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setMarketName(String marketName){
		this.marketName=marketName;
	}

	public String getMarketName(){
		return this.marketName;
	}

	public void setIsSpecial(Integer isSpecial){
		this.isSpecial=isSpecial;
	}

	public Integer getIsSpecial(){
		return this.isSpecial;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setMarketTel(String marketTel){
		this.marketTel=marketTel;
	}

	public String getMarketTel(){
		return this.marketTel;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setParentDealerD(Long parentDealerD){
		this.parentDealerD=parentDealerD;
	}

	public Long getParentDealerD(){
		return this.parentDealerD;
	}

	public void setPdealerType(Integer pdealerType){
		this.pdealerType=pdealerType;
	}

	public Integer getPdealerType(){
		return this.pdealerType;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setSysStatus(Integer sysStatus){
		this.sysStatus=sysStatus;
	}

	public Integer getSysStatus(){
		return this.sysStatus;
	}

	public void setPhone(String phone){
		this.phone=phone;
	}

	public String getPhone(){
		return this.phone;
	}

	public void setBank(String bank){
		this.bank=bank;
	}

	public String getBank(){
		return this.bank;
	}

	public void setInvoicePostAdd(String invoicePostAdd){
		this.invoicePostAdd=invoicePostAdd;
	}

	public String getInvoicePostAdd(){
		return this.invoicePostAdd;
	}

	public void setDealerClass(Integer dealerClass){
		this.dealerClass=dealerClass;
	}

	public Integer getDealerClass(){
		return this.dealerClass;
	}

	public void setDutyPhone(String dutyPhone){
		this.dutyPhone=dutyPhone;
	}

	public String getDutyPhone(){
		return this.dutyPhone;
	}

	public void setTaxpayerNo(String taxpayerNo){
		this.taxpayerNo=taxpayerNo;
	}

	public String getTaxpayerNo(){
		return this.taxpayerNo;
	}

	public void setErpCode(String erpCode){
		this.erpCode=erpCode;
	}

	public String getErpCode(){
		return this.erpCode;
	}

	public void setEmail(String email){
		this.email=email;
	}

	public String getEmail(){
		return this.email;
	}

	public void setInvoicePersion(String invoicePersion){
		this.invoicePersion=invoicePersion;
	}

	public String getInvoicePersion(){
		return this.invoicePersion;
	}

	public void setBeginBank(String beginBank){
		this.beginBank=beginBank;
	}

	public String getBeginBank(){
		return this.beginBank;
	}

	public void setTownship(String township){
		this.township=township;
	}

	public String getTownship(){
		return this.township;
	}

	public void setIsNbdw(Integer isNbdw){
		this.isNbdw=isNbdw;
	}

	public Integer getIsNbdw(){
		return this.isNbdw;
	}

	public void setChAddress2(String chAddress2){
		this.chAddress2=chAddress2;
	}

	public String getChAddress2(){
		return this.chAddress2;
	}

	public void setDealerShortname(String dealerShortname){
		this.dealerShortname=dealerShortname;
	}

	public String getDealerShortname(){
		return this.dealerShortname;
	}

	public void setIsScan(Integer isScan){
		this.isScan=isScan;
	}

	public Integer getIsScan(){
		return this.isScan;
	}

	public void setSpyMan(String spyMan){
		this.spyMan=spyMan;
	}

	public String getSpyMan(){
		return this.spyMan;
	}

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getAddress(){
		return this.address;
	}

	public void setAreaLevel(Integer areaLevel){
		this.areaLevel=areaLevel;
	}

	public Integer getAreaLevel(){
		return this.areaLevel;
	}

	public void setSecendAutidStatus(Integer secendAutidStatus){
		this.secendAutidStatus=secendAutidStatus;
	}

	public Integer getSecendAutidStatus(){
		return this.secendAutidStatus;
	}

	public void setOldDealerCode(String oldDealerCode){
		this.oldDealerCode=oldDealerCode;
	}

	public String getOldDealerCode(){
		return this.oldDealerCode;
	}

	public void setProvinceId(Long provinceId){
		this.provinceId=provinceId;
	}

	public Long getProvinceId(){
		return this.provinceId;
	}

	public void setLegalTel(String legalTel){
		this.legalTel=legalTel;
	}

	public String getLegalTel(){
		return this.legalTel;
	}

	public void setBeginOldDate(Date beginOldDate){
		this.beginOldDate=beginOldDate;
	}

	public Date getBeginOldDate(){
		return this.beginOldDate;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setEndOldDate(Date endOldDate){
		this.endOldDate=endOldDate;
	}

	public Date getEndOldDate(){
		return this.endOldDate;
	}

	public void setDealerLevel(Integer dealerLevel){
		this.dealerLevel=dealerLevel;
	}

	public Integer getDealerLevel(){
		return this.dealerLevel;
	}

	public void setOldDealerCode2(String oldDealerCode2){
		this.oldDealerCode2=oldDealerCode2;
	}

	public String getOldDealerCode2(){
		return this.oldDealerCode2;
	}

	public void setClCqFlag(Integer clCqFlag){
		this.clCqFlag=clCqFlag;
	}

	public Integer getClCqFlag(){
		return this.clCqFlag;
	}

	public void setBrand(String brand){
		this.brand=brand;
	}

	public String getBrand(){
		return this.brand;
	}

	public void setSitedate(Date sitedate){
		this.sitedate=sitedate;
	}

	public Date getSitedate(){
		return this.sitedate;
	}

	public void setTaxpayerNature(String taxpayerNature){
		this.taxpayerNature=taxpayerNature;
	}

	public String getTaxpayerNature(){
		return this.taxpayerNature;
	}

	public Integer getTaxRateId() {
		return taxRateId;
	}

	public void setTaxRateId(Integer taxRateId) {
		this.taxRateId = taxRateId;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

}