/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-20 10:46:09
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcSuggestPO extends PO{

	private Long suggestId;
	private String offerName;
	private String offerPhone;
	private String suggestInfo;
	private String createBy;
	private Date createDate;
	private String updateBy;
	private Date updateDate;
	
	public Long getSuggestId() {
		return suggestId;
	}
	public void setSuggestId(Long suggestId) {
		this.suggestId = suggestId;
	}
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	public String getOfferPhone() {
		return offerPhone;
	}
	public void setOfferPhone(String offerPhone) {
		this.offerPhone = offerPhone;
	}
	public String getSuggestInfo() {
		return suggestInfo;
	}
	public void setSuggestInfo(String suggestInfo) {
		this.suggestInfo = suggestInfo;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}