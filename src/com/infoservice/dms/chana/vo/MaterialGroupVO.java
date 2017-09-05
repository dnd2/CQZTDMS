package com.infoservice.dms.chana.vo;

/**
 * @Title: MaterialGroupVO.java
 *
 * @Description:物料组下发接口VO
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-23
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
@SuppressWarnings("serial")
public class MaterialGroupVO extends BaseVO {
	private String brandCode; //品牌代码 varchar(30)
	private String brandName; //品牌名称 varchar(90)
	private String seriesCode; //车系代码 varchar(30)
	private String seriesName; //车系名称 varchar(90)
	private String modelCode; //车型代码 varchar(30)
	private String modelName; //车型名称 varchar(90)
	private String configCode; //配置代码 varchar(30)
	private String configName; //配置名称 varchar(150)

	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getSeriesCode() {
		return seriesCode;
	}
	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getConfigCode() {
		return configCode;
	}
	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("[brandCode] = ").append(brandCode)
		   .append(", [seriesCode] = ").append(seriesCode)
		   .append(", [seriesName] = ").append(seriesName)
		   .append(", [modelCode] = ").append(modelCode)
		   .append(", [modelName] = ").append(modelName)
		   .append(", [configCode] = ").append(configCode)
		   .append(", [configName] = ").append(configName);
		return str.toString();
	}
}
