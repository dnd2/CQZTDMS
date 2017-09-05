/**********************************************************************
 * <pre>
 * FILE : TmFleetPromotionBean.java
 * CLASS : TmFleetPromotionBean
 *
 * AUTHOR : zzg
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2010-1-5|zzg| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.bean;

import java.util.Date;

public class TmFleetPromotionBean {
	private Long promotionId;
	private Long brandId;
	private Long orgId;
	private Long fleetId;
	private Integer promotionType;
	private Integer promotionCode;
	private Integer applyType;
	private Integer isCredit;
	private Integer creditType;
	private Integer creditMonth;
	private String remark;
	private Integer status;
	private Date auditDate;
	public Long getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getFleetId() {
		return fleetId;
	}
	public void setFleetId(Long fleetId) {
		this.fleetId = fleetId;
	}
	public Integer getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(Integer promotionType) {
		this.promotionType = promotionType;
	}
	public Integer getPromotionCode() {
		return promotionCode;
	}
	public void setPromotionCode(Integer promotionCode) {
		this.promotionCode = promotionCode;
	}
	public Integer getApplyType() {
		return applyType;
	}
	public void setApplyType(Integer applyType) {
		this.applyType = applyType;
	}
	public Integer getIsCredit() {
		return isCredit;
	}
	public void setIsCredit(Integer isCredit) {
		this.isCredit = isCredit;
	}
	public Integer getCreditType() {
		return creditType;
	}
	public void setCreditType(Integer creditType) {
		this.creditType = creditType;
	}
	public Integer getCreditMonth() {
		return creditMonth;
	}
	public void setCreditMonth(Integer creditMonth) {
		this.creditMonth = creditMonth;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
}
