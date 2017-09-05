package com.infodms.dms.bean;

import java.util.Date;

/**
 * @author Andy Z
 * 车型视图对象
 */
public class ModelViewBean {
	private String brandId;      //厂家品牌ID
	private String brandCode;    //厂家品牌代码
	private String brandName;    //厂家品牌名称
	private String brandStauts;  //厂家品牌状态
	
	private String seriesId;      //车系ID
	private String seriesCode;    //车系Code
	private String seriesName;    //车系名称
	private String seriesStatus;  //车系状态
	
	private String modelId;      //车型ID
	private String modelCode;    //车型代码
	private String modelName;    //车型名称
	private String modelStatus;  //车型状态
	
	private String vhclType;     //车辆类型
	private String gearBox;      //变速箱
	private String noticeCode;   //公告代码
	private String modelYear;    //车型年
	private String modelMonth;   //车型月
	private String dspm;         //排量
	
	private String vhclQuotId;             //行情ID
	private String goodConDlrPrice;        //良好车况批发价
	private String goodConRetailPrice;     //良好车况零售价
	private String avgConDlrPrice;         //平均车况批发价
	private String avgConRetailPrice;      //平均车况零售价
	private String efficientYearMonth;     //有效年月
	private String msrp;                   //新车厂家指导价
	private Date importedDate;             //导入日期
	
	private String modelPartId;	
	
	public String getModelPartId() {
		return modelPartId;
	}
	public void setModelPartId(String modelPartId) {
		this.modelPartId = modelPartId;
	}
	public String getVhclQuotId() {
		return vhclQuotId;
	}
	public void setVhclQuotId(String vhclQuotId) {
		this.vhclQuotId = vhclQuotId;
	}
	public String getGoodConDlrPrice() {
		return goodConDlrPrice;
	}
	public void setGoodConDlrPrice(String goodConDlrPrice) {
		this.goodConDlrPrice = goodConDlrPrice;
	}
	public String getGoodConRetailPrice() {
		return goodConRetailPrice;
	}
	public void setGoodConRetailPrice(String goodConRetailPrice) {
		this.goodConRetailPrice = goodConRetailPrice;
	}
	public String getAvgConRetailPrice() {
		return avgConRetailPrice;
	}
	public void setAvgConRetailPrice(String avgConRetailPrice) {
		this.avgConRetailPrice = avgConRetailPrice;
	}
	public String getAvgConDlrPrice() {
		return avgConDlrPrice;
	}
	public void setAvgConDlrPrice(String avgConDlrPrice) {
		this.avgConDlrPrice = avgConDlrPrice;
	}
	public String getEfficientYearMonth() {
		return efficientYearMonth;
	}
	public void setEfficientYearMonth(String efficientYearMonth) {
		this.efficientYearMonth = efficientYearMonth;
	}
	public String getMsrp() {
		return msrp;
	}
	public void setMsrp(String msrp) {
		this.msrp = msrp;
	}
	public Date getImportedDate() {
		return importedDate;
	}
	public void setImportedDate(Date importedDate) {
		this.importedDate = importedDate;
	}	
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
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
	public String getBrandStauts() {
		return brandStauts;
	}
	public void setBrandStauts(String brandStauts) {
		this.brandStauts = brandStauts;
	}
	public String getSeriesId() {
		return seriesId;
	}
	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
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
	public String getSeriesStatus() {
		return seriesStatus;
	}
	public void setSeriesStatus(String seriesStatus) {
		this.seriesStatus = seriesStatus;
	}
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
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
	public String getModelStatus() {
		return modelStatus;
	}
	public void setModelStatus(String modelStatus) {
		this.modelStatus = modelStatus;
	}
	public String getVhclType() {
		return vhclType;
	}
	public void setVhclType(String vhclType) {
		this.vhclType = vhclType;
	}
	public String getGearBox() {
		return gearBox;
	}
	public void setGearBox(String gearBox) {
		this.gearBox = gearBox;
	}
	public String getNoticeCode() {
		return noticeCode;
	}
	public void setNoticeCode(String noticeCode) {
		this.noticeCode = noticeCode;
	}
	public String getModelYear() {
		return modelYear;
	}
	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}
	public String getDspm() {
		return dspm;
	}
	public void setDspm(String dspm) {
		this.dspm = dspm;
	}
	public String getModelMonth() {
		return modelMonth;
	}
	public void setModelMonth(String modelMonth) {
		this.modelMonth = modelMonth;
	}

}
