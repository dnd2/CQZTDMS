/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-03-02 14:32:01
* CreateBy   : nova
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsEnergyConservationPO extends PO{

	private String conservationNo;
	private Integer isExport;
	private String dlrLinktel;
	private Long dealerId;
	private Date updateDate;
	private Long createBy;
	private String remark;
	private Double factoryPrice;
	private Integer status;
	private Long dlrProviceId;
	private Long dlrCityId;
	private Long ctmId;
	private Long modelId;
	private Long conservationId;
	private Long updateBy;
	private Long exportUser;
	private String dlrAddress;
	private Integer ctmZipCode;
	private Integer conservationStatus;
	private Long vehicleId;
	private String ctmAddress;
	private Long ctmProviceId;
	private Long ctmCityId;
	private Long ctmTownId;
	private Date payDate;
	private Double payMoney;
	private Long dlrTownId;
	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public Double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}

	public Long getDlrTownId() {
		return dlrTownId;
	}

	public void setDlrTownId(Long dlrTownId) {
		this.dlrTownId = dlrTownId;
	}

	public Long getDlrCityId() {
		return dlrCityId;
	}

	public void setDlrCityId(Long dlrCityId) {
		this.dlrCityId = dlrCityId;
	}

	public Long getCtmCityId() {
		return ctmCityId;
	}

	public void setCtmCityId(Long ctmCityId) {
		this.ctmCityId = ctmCityId;
	}

	public Long getCtmTownId() {
		return ctmTownId;
	}

	public void setCtmTownId(Long ctmTownId) {
		this.ctmTownId = ctmTownId;
	}

	private Date salesDate;
	private String dlrLinkman;
	private String vehicleNo;
	private String ctmLinkman;
	private Integer dlrZipCode;
	private Long orgId;
	private Long exportFlag;
	private Long salesId;
	private Date createDate;
	private Long exportPose;
	private Double salesPrice;
	private String invoceNo;
	private String ctmLinktel;

	public void setConservationNo(String conservationNo){
		this.conservationNo=conservationNo;
	}

	public String getConservationNo(){
		return this.conservationNo;
	}

	public void setIsExport(Integer isExport){
		this.isExport=isExport;
	}

	public Integer getIsExport(){
		return this.isExport;
	}

	public void setDlrLinktel(String dlrLinktel){
		this.dlrLinktel=dlrLinktel;
	}

	public String getDlrLinktel(){
		return this.dlrLinktel;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}


	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setFactoryPrice(Double factoryPrice){
		this.factoryPrice=factoryPrice;
	}

	public Double getFactoryPrice(){
		return this.factoryPrice;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setDlrProviceId(Long dlrProviceId){
		this.dlrProviceId=dlrProviceId;
	}

	public Long getDlrProviceId(){
		return this.dlrProviceId;
	}

	public void setCtmId(Long ctmId){
		this.ctmId=ctmId;
	}

	public Long getCtmId(){
		return this.ctmId;
	}

	public void setModelId(Long modelId){
		this.modelId=modelId;
	}

	public Long getModelId(){
		return this.modelId;
	}

	public void setConservationId(Long conservationId){
		this.conservationId=conservationId;
	}

	public Long getConservationId(){
		return this.conservationId;
	}


	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}


	public Long getExportUser() {
		return exportUser;
	}

	public void setExportUser(Long exportUser) {
		this.exportUser = exportUser;
	}

	public void setDlrAddress(String dlrAddress){
		this.dlrAddress=dlrAddress;
	}

	public String getDlrAddress(){
		return this.dlrAddress;
	}

	public void setCtmZipCode(Integer ctmZipCode){
		this.ctmZipCode=ctmZipCode;
	}

	public Integer getCtmZipCode(){
		return this.ctmZipCode;
	}

	public void setConservationStatus(Integer conservationStatus){
		this.conservationStatus=conservationStatus;
	}

	public Integer getConservationStatus(){
		return this.conservationStatus;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

	public void setCtmAddress(String ctmAddress){
		this.ctmAddress=ctmAddress;
	}

	public String getCtmAddress(){
		return this.ctmAddress;
	}

	public void setCtmProviceId(Long ctmProviceId){
		this.ctmProviceId=ctmProviceId;
	}

	public Long getCtmProviceId(){
		return this.ctmProviceId;
	}

	public void setSalesDate(Date salesDate){
		this.salesDate=salesDate;
	}

	public Date getSalesDate(){
		return this.salesDate;
	}

	public void setDlrLinkman(String dlrLinkman){
		this.dlrLinkman=dlrLinkman;
	}

	public String getDlrLinkman(){
		return this.dlrLinkman;
	}

	public void setVehicleNo(String vehicleNo){
		this.vehicleNo=vehicleNo;
	}

	public String getVehicleNo(){
		return this.vehicleNo;
	}

	public void setCtmLinkman(String ctmLinkman){
		this.ctmLinkman=ctmLinkman;
	}

	public String getCtmLinkman(){
		return this.ctmLinkman;
	}

	public void setDlrZipCode(Integer dlrZipCode){
		this.dlrZipCode=dlrZipCode;
	}

	public Integer getDlrZipCode(){
		return this.dlrZipCode;
	}

	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}

	public Long getOrgId(){
		return this.orgId;
	}

	public void setExportFlag(Long exportFlag){
		this.exportFlag=exportFlag;
	}

	public Long getExportFlag(){
		return this.exportFlag;
	}

	public void setSalesId(Long salesId){
		this.salesId=salesId;
	}

	public Long getSalesId(){
		return this.salesId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public Long getExportPose() {
		return exportPose;
	}

	public void setExportPose(Long exportPose) {
		this.exportPose = exportPose;
	}

	public void setSalesPrice(Double salesPrice){
		this.salesPrice=salesPrice;
	}

	public Double getSalesPrice(){
		return this.salesPrice;
	}

	public void setInvoceNo(String invoceNo){
		this.invoceNo=invoceNo;
	}

	public String getInvoceNo(){
		return this.invoceNo;
	}

	public void setCtmLinktel(String ctmLinktel){
		this.ctmLinktel=ctmLinktel;
	}

	public String getCtmLinktel(){
		return this.ctmLinktel;
	}

}