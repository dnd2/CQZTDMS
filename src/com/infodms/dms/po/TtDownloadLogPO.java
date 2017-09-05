/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-10-27 13:50:26
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtDownloadLogPO extends PO{
	private Long logId;
	private String logDownloadBtn;
	private String logDownloadMenu;
	private Date downloadTime;
	private Date createDate;
	private Long createBy;
	private Date updateDate;
	private Long updateBy;
	private String downloadUser;
	
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	public String getLogDownloadBtn() {
		return logDownloadBtn;
	}
	public void setLogDownloadBtn(String logDownloadBtn) {
		this.logDownloadBtn = logDownloadBtn;
	}
	
	public String getLogDownloadMenu() {
		return logDownloadMenu;
	}
	public void setLogDownloadMenu(String logDownloadMenu) {
		this.logDownloadMenu = logDownloadMenu;
	}
	public Date getDownloadTime() {
		return downloadTime;
	}
	public void setDownloadTime(Date downloadTime) {
		this.downloadTime = downloadTime;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public String getDownloadUser() {
		return downloadUser;
	}
	public void setDownloadUser(String downloadUser) {
		this.downloadUser = downloadUser;
	}
	
}