/**********************************************************************
 * <pre>
 * FILE : TmFleetPromotionInventoryBean.java
 * CLASS : TmFleetPromotionInventoryBean
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

public class TmFleetPromotionStockBean {
	private Long promotionProductId;
	private Long promotionId;
	private Long productId;
	private Long orderId;
	private Integer applyAmount;
	private Integer contractPrice;
	private Integer guidePrice;
	private Integer normalPrice;
	private Double preferentialPoint;
	private Integer preferentialAmount;
	private String productCode;
	private String brandName;
	private String modelName;
	public Long getPromotionProductId() {
		return promotionProductId;
	}
	public void setPromotionProductId(Long promotionProductId) {
		this.promotionProductId = promotionProductId;
	}
	public Long getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Integer getApplyAmount() {
		return applyAmount;
	}
	public void setApplyAmount(Integer applyAmount) {
		this.applyAmount = applyAmount;
	}
	public Integer getContractPrice() {
		return contractPrice;
	}
	public void setContractPrice(Integer contractPrice) {
		this.contractPrice = contractPrice;
	}
	public Integer getGuidePrice() {
		return guidePrice;
	}
	public void setGuidePrice(Integer guidePrice) {
		this.guidePrice = guidePrice;
	}
	public Integer getNormalPrice() {
		return normalPrice;
	}
	public void setNormalPrice(Integer normalPrice) {
		this.normalPrice = normalPrice;
	}
	public Integer getPreferentialAmount() {
		return preferentialAmount;
	}
	public void setPreferentialAmount(Integer preferentialAmount) {
		this.preferentialAmount = preferentialAmount;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public Double getPreferentialPoint() {
		return preferentialPoint;
	}
	public void setPreferentialPoint(Double preferentialPoint) {
		this.preferentialPoint = preferentialPoint;
	}
}
