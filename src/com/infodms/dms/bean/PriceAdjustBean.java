package com.infodms.dms.bean;
import java.util.Date;

import com.infoservice.po3.bean.PO;
public class PriceAdjustBean extends PO {
	private static final long serialVersionUID = 6904516585719637919L;
	private Long id;
	private Long adjustMode;
	private Long typeAdjust;
	private Double adjustPrice;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private String codeDesc;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAdjustMode() {
		return adjustMode;
	}
	public void setAdjustMode(Long adjustMode) {
		this.adjustMode = adjustMode;
	}
	public Long getTypeAdjust() {
		return typeAdjust;
	}
	public void setTypeAdjust(Long typeAdjust) {
		this.typeAdjust = typeAdjust;
	}
	public Double getAdjustPrice() {
		return adjustPrice;
	}
	public void setAdjustPrice(Double adjustPrice) {
		this.adjustPrice = adjustPrice;
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
	public String getCodeDesc() {
		return codeDesc;
	}
	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
	
}
