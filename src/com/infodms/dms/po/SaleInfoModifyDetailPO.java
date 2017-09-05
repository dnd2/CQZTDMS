package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class SaleInfoModifyDetailPO extends PO{
	private String modifyTitle;
	private String beforeModify;
	private String afterModify;
	public String getModifyTitle() {
		return modifyTitle;
	}
	public void setModifyTitle(String modifyTitle) {
		this.modifyTitle = modifyTitle;
	}
	public String getBeforeModify() {
		return beforeModify;
	}
	public void setBeforeModify(String beforeModify) {
		this.beforeModify = beforeModify;
	}
	public String getAfterModify() {
		return afterModify;
	}
	public void setAfterModify(String afterModify) {
		this.afterModify = afterModify;
	}
	
}
