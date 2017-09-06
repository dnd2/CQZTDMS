/*
 * Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
 * This software is published under the terms of the Infoservice Software
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * CreateDate : 2010-09-13 20:23:11
 * CreateBy   : Administrator
 * Comment    : generate by com.sgm.po.POGen
 */

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsDlvryPO extends PO {

	private Long reqId;
	private String reqNo;
	private String reqTotal;
	private Integer reqShipType;
	private Date reqDate;
	private Long reqRecDealerId;
	private Long reqRecAddrId;
	private Long reqRecProvId;
	private Long reqRecCityId;
	private Long reqRecCountyId;
	private String reqRecAddr;
	private String reqLinkMan;
	private String reqTel;
	private Long reqWhId;
	private String reqRemark;
	private Long ordId;
	private String ordNo;
	private Long ordPurDealerId;
	private Integer ordTotal;
	private Integer dlvType;
	private Integer dlvShipType;
	private Integer dlvStatus;
	private Integer dlvFpTotal;
	private Integer dlvBdTotal;
	private Integer dlvFyTotal;
	private Integer dlvJjTotal;
	private Integer dlvYsTotal;
	private Date dlvFyDate;
	private Date dlvJjDate;
	private Long dlvWhId;
	private Long dlvLogiId;
	private Long dlvBalProvId;
	private Long dlvBalCityId;
	private Long dlvBalCountyId;
//	private Integer dlvIsZz;
//	private Long dlvZzProvId;
//	private Long dlvZzCityId;
//	private Long dlvZzCountyId;
	private Long createBy;
	private Date createDate;
	private Long updateBy;
	private Date updateDate;
	private Integer ver;
	private Long reqRecWhId;
	private Date dlvDate;
	private Integer dlvIsSd;
	private Long dlvBy;
	private String dlvRemark;
	private Long dlvRecWhId;
	private Date auditDate;
	private Long auditBy;
	private String auditRemark;
//	private Long zzWhId;
	
	public Date getAuditDate() {
		return auditDate;
	}
//	public Integer getDlvIsZz() {
//		return dlvIsZz;
//	}
//	public void setDlvIsZz(Integer dlvIsZz) {
//		this.dlvIsZz = dlvIsZz;
//	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public Long getAuditBy() {
		return auditBy;
	}
	public void setAuditBy(Long auditBy) {
		this.auditBy = auditBy;
	}
	public String getAuditRemark() {
		return auditRemark;
	}
	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}
	public Long getDlvRecWhId() {
		return dlvRecWhId;
	}
	public void setDlvRecWhId(Long dlvRecWhId) {
		this.dlvRecWhId = dlvRecWhId;
	}
	public Long getDlvBy() {
		return dlvBy;
	}
	public void setDlvBy(Long dlvBy) {
		this.dlvBy = dlvBy;
	}
	public String getDlvRemark() {
		return dlvRemark;
	}
	public void setDlvRemark(String dlvRemark) {
		this.dlvRemark = dlvRemark;
	}
	public Integer getDlvIsSd() {
		return dlvIsSd;
	}
	public void setDlvIsSd(Integer dlvIsSd) {
		this.dlvIsSd = dlvIsSd;
	}
	public Date getDlvDate() {
		return dlvDate;
	}
	public void setDlvDate(Date dlvDate) {
		this.dlvDate = dlvDate;
	}
	public Long getReqRecWhId() {
		return reqRecWhId;
	}
	public void setReqRecWhId(Long reqRecWhId) {
		this.reqRecWhId = reqRecWhId;
	}
	public String getReqTotal() {
		return reqTotal;
	}
	public void setReqTotal(String reqTotal) {
		this.reqTotal = reqTotal;
	}
	public Long getReqId() {
		return reqId;
	}
	public void setReqId(Long reqId) {
		this.reqId = reqId;
	}
	public String getReqNo() {
		return reqNo;
	}
	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}
	public Integer getReqShipType() {
		return reqShipType;
	}
	public void setReqShipType(Integer reqShipType) {
		this.reqShipType = reqShipType;
	}
	public Date getReqDate() {
		return reqDate;
	}
	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
	}
	public Long getReqRecDealerId() {
		return reqRecDealerId;
	}
	public void setReqRecDealerId(Long reqRecDealerId) {
		this.reqRecDealerId = reqRecDealerId;
	}
	public Long getReqRecAddrId() {
		return reqRecAddrId;
	}
	public void setReqRecAddrId(Long reqRecAddrId) {
		this.reqRecAddrId = reqRecAddrId;
	}
	public Long getReqRecProvId() {
		return reqRecProvId;
	}
	public void setReqRecProvId(Long reqRecProvId) {
		this.reqRecProvId = reqRecProvId;
	}
	public Long getReqRecCityId() {
		return reqRecCityId;
	}
	public void setReqRecCityId(Long reqRecCityId) {
		this.reqRecCityId = reqRecCityId;
	}
	public Long getReqRecCountyId() {
		return reqRecCountyId;
	}
	public void setReqRecCountyId(Long reqRecCountyId) {
		this.reqRecCountyId = reqRecCountyId;
	}
	public String getReqRecAddr() {
		return reqRecAddr;
	}
	public void setReqRecAddr(String reqRecAddr) {
		this.reqRecAddr = reqRecAddr;
	}
	public String getReqLinkMan() {
		return reqLinkMan;
	}
	public void setReqLinkMan(String reqLinkMan) {
		this.reqLinkMan = reqLinkMan;
	}
	public String getReqTel() {
		return reqTel;
	}
	public void setReqTel(String reqTel) {
		this.reqTel = reqTel;
	}
	public Long getReqWhId() {
		return reqWhId;
	}
	public void setReqWhId(Long reqWhId) {
		this.reqWhId = reqWhId;
	}
	public String getReqRemark() {
		return reqRemark;
	}
	public void setReqRemark(String reqRemark) {
		this.reqRemark = reqRemark;
	}
	public Long getOrdId() {
		return ordId;
	}
	public void setOrdId(Long ordId) {
		this.ordId = ordId;
	}
	public String getOrdNo() {
		return ordNo;
	}
	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}
	public Long getOrdPurDealerId() {
		return ordPurDealerId;
	}
	public void setOrdPurDealerId(Long ordPurDealerId) {
		this.ordPurDealerId = ordPurDealerId;
	}
	public Integer getOrdTotal() {
		return ordTotal;
	}
	public void setOrdTotal(Integer ordTotal) {
		this.ordTotal = ordTotal;
	}
	public Integer getDlvType() {
		return dlvType;
	}
	public void setDlvType(Integer dlvType) {
		this.dlvType = dlvType;
	}
	public Integer getDlvShipType() {
		return dlvShipType;
	}
	public void setDlvShipType(Integer dlvShipType) {
		this.dlvShipType = dlvShipType;
	}
	public Integer getDlvStatus() {
		return dlvStatus;
	}
	public void setDlvStatus(Integer dlvStatus) {
		this.dlvStatus = dlvStatus;
	}
	public Integer getDlvFpTotal() {
		return dlvFpTotal;
	}
	public void setDlvFpTotal(Integer dlvFpTotal) {
		this.dlvFpTotal = dlvFpTotal;
	}
	public Integer getDlvBdTotal() {
		return dlvBdTotal;
	}
	public void setDlvBdTotal(Integer dlvBdTotal) {
		this.dlvBdTotal = dlvBdTotal;
	}
	public Integer getDlvFyTotal() {
		return dlvFyTotal;
	}
	public void setDlvFyTotal(Integer dlvFyTotal) {
		this.dlvFyTotal = dlvFyTotal;
	}
	public Integer getDlvJjTotal() {
		return dlvJjTotal;
	}
	public void setDlvJjTotal(Integer dlvJjTotal) {
		this.dlvJjTotal = dlvJjTotal;
	}
	public Integer getDlvYsTotal() {
		return dlvYsTotal;
	}
	public void setDlvYsTotal(Integer dlvYsTotal) {
		this.dlvYsTotal = dlvYsTotal;
	}
	public Date getDlvFyDate() {
		return dlvFyDate;
	}
	public void setDlvFyDate(Date dlvFyDate) {
		this.dlvFyDate = dlvFyDate;
	}
	public Date getDlvJjDate() {
		return dlvJjDate;
	}
	public void setDlvJjDate(Date dlvJjDate) {
		this.dlvJjDate = dlvJjDate;
	}
	public Long getDlvWhId() {
		return dlvWhId;
	}
	public void setDlvWhId(Long dlvWhId) {
		this.dlvWhId = dlvWhId;
	}
	public Long getDlvLogiId() {
		return dlvLogiId;
	}
	public void setDlvLogiId(Long dlvLogiId) {
		this.dlvLogiId = dlvLogiId;
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
	
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Integer getVer() {
		return ver;
	}
	public void setVer(Integer ver) {
		this.ver = ver;
	}
}