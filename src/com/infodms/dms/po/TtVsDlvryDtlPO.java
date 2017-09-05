/*
 * Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
 * This software is published under the terms of the Infoservice Software
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * CreateDate : 2010-09-29 22:43:58
 * CreateBy   : Administrator
 * Comment    : generate by com.sgm.po.POGen
 */

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsDlvryDtlPO extends PO {
	private Long reqDetailId;
	private Long reqId;
	private Long ordId;
	private Long ordDetailId;
	private Long materialId;
	private Integer ordTotal;
	private Integer reqTotal;
	private Integer fpTotal;
	private Integer bdTotal;
	private Integer fyTotal;
	private Integer jjTotal;
	private Integer ysTotal;
	private Long createBy;
	private Date createDate;
	private Long updateBy;
	private Date updateDate;
	private Integer ver;
	public Long getReqDetailId() {
		return reqDetailId;
	}
	public void setReqDetailId(Long reqDetailId) {
		this.reqDetailId = reqDetailId;
	}
	public Long getReqId() {
		return reqId;
	}
	public void setReqId(Long reqId) {
		this.reqId = reqId;
	}
	public Long getOrdId() {
		return ordId;
	}
	public void setOrdId(Long ordId) {
		this.ordId = ordId;
	}
	public Long getOrdDetailId() {
		return ordDetailId;
	}
	public void setOrdDetailId(Long ordDetailId) {
		this.ordDetailId = ordDetailId;
	}
	public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	public Integer getOrdTotal() {
		return ordTotal;
	}
	public void setOrdTotal(Integer ordTotal) {
		this.ordTotal = ordTotal;
	}
	public Integer getReqTotal() {
		return reqTotal;
	}
	public void setReqTotal(Integer reqTotal) {
		this.reqTotal = reqTotal;
	}
	public Integer getFpTotal() {
		return fpTotal;
	}
	public void setFpTotal(Integer fpTotal) {
		this.fpTotal = fpTotal;
	}
	public Integer getBdTotal() {
		return bdTotal;
	}
	public void setBdTotal(Integer bdTotal) {
		this.bdTotal = bdTotal;
	}
	public Integer getFyTotal() {
		return fyTotal;
	}
	public void setFyTotal(Integer fyTotal) {
		this.fyTotal = fyTotal;
	}
	public Integer getJjTotal() {
		return jjTotal;
	}
	public void setJjTotal(Integer jjTotal) {
		this.jjTotal = jjTotal;
	}
	public Integer getYsTotal() {
		return ysTotal;
	}
	public void setYsTotal(Integer ysTotal) {
		this.ysTotal = ysTotal;
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