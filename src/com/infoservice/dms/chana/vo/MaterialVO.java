package com.infoservice.dms.chana.vo;

import java.util.Date;

/**
 * @Title: MaterialVO.java
 *
 * @Description:物料VO
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-26
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
@SuppressWarnings("serial")
public class MaterialVO extends BaseVO {
	private String productCode;		//产品代码
	private String productName;		//产品名称
	private Integer productType;	//产品类型 上端无
	private Integer productStatus;  //产品状态
	private String brandCode;		//品牌代码
	private String seriesCode;		//车系代码
	private String modelCode;		//车型代码
	private String colorCode;		//颜色代码
	private String configCode;		//配置代码
	private Double oemDirectivePrice;//车厂指导价
	private Double directivePrice;  //销售指导价
	private Date exeuntDate;		//退市日期 上端 失效日期
	private Date enterDate;			//上市日期
	private String remark;			//注释
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getProductType() {
		return productType;
	}
	public void setProductType(Integer productType) {
		this.productType = productType;
	}
	public Integer getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(Integer productStatus) {
		this.productStatus = productStatus;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getSeriesCode() {
		return seriesCode;
	}
	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	public String getConfigCode() {
		return configCode;
	}
	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}
	public Double getOemDirectivePrice() {
		return oemDirectivePrice;
	}
	public void setOemDirectivePrice(Double oemDirectivePrice) {
		this.oemDirectivePrice = oemDirectivePrice;
	}
	public Double getDirectivePrice() {
		return directivePrice;
	}
	public void setDirectivePrice(Double directivePrice) {
		this.directivePrice = directivePrice;
	}
	public Date getExeuntDate() {
		return exeuntDate;
	}
	public void setExeuntDate(Date exeuntDate) {
		this.exeuntDate = exeuntDate;
	}
	public Date getEnterDate() {
		return enterDate;
	}
	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
