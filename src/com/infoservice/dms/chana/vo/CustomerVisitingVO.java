package com.infoservice.dms.chana.vo;

import java.util.Date;

/**
 * @author Administrator
 * 展厅客流及潜客日终统计上报
 * add by tanv 2013-09-09
 */
public class CustomerVisitingVO extends BaseVO {
	private static final long serialVersionUID = 1L;
	private Date uploadDate;               //日期
	private String businessType;           //业务范围
	private String series;                 //车系
	private String model;                  //车型
	private String config;                 //配置
	private String colorCode;              //颜色
	private Integer cusSource;             //客户来源
	private Integer mediaDetail;           //信息渠道
	private Integer newRecordTotal;        //展厅客流数量
	private Integer firstVisitCount;       //首次客流数量
	private Integer firstVisitCallCount;   //首次客流留电量
	private Integer traceCount;            //潜客回访数量
	private Double avgTraceRate;           //平均跟踪次数
	private Double successTraceRate;       //成交潜客平均跟踪此数
	private Double failTraceRate;          //战败潜客平均跟踪次数
	private Integer inBobyCount;           //来店潜客数
	private Integer inCallCount;           //来电潜客数
	private Integer customerCount;         //潜客数量
	private Integer testDriverCount;       //试驾人数
	private Integer haveBargainCount;      //潜客成交数
	private Integer sendCarCount;          //潜客交车数
	private Integer depositsCount;         //当日订金台数
	private Integer levelHCount;           //当日H级潜客数
	private Integer levelACount;           //当日A级潜客数
	private Integer levelBCount;           //当日B级潜客数
	private Integer levelCCount;           //当日C级潜客数
	private Integer visitCallCount;        //客户留电量
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	public Integer getCusSource() {
		return cusSource;
	}
	public void setCusSource(Integer cusSource) {
		this.cusSource = cusSource;
	}
	public Integer getMediaDetail() {
		return mediaDetail;
	}
	public void setMediaDetail(Integer mediaDetail) {
		this.mediaDetail = mediaDetail;
	}
	public Integer getNewRecordTotal() {
		return newRecordTotal;
	}
	public void setNewRecordTotal(Integer newRecordTotal) {
		this.newRecordTotal = newRecordTotal;
	}
	public Integer getFirstVisitCount() {
		return firstVisitCount;
	}
	public void setFirstVisitCount(Integer firstVisitCount) {
		this.firstVisitCount = firstVisitCount;
	}
	public Integer getFirstVisitCallCount() {
		return firstVisitCallCount;
	}
	public void setFirstVisitCallCount(Integer firstVisitCallCount) {
		this.firstVisitCallCount = firstVisitCallCount;
	}
	public Integer getTraceCount() {
		return traceCount;
	}
	public void setTraceCount(Integer traceCount) {
		this.traceCount = traceCount;
	}
	public Double getAvgTraceRate() {
		return avgTraceRate;
	}
	public void setAvgTraceRate(Double avgTraceRate) {
		this.avgTraceRate = avgTraceRate;
	}
	public Double getSuccessTraceRate() {
		return successTraceRate;
	}
	public void setSuccessTraceRate(Double successTraceRate) {
		this.successTraceRate = successTraceRate;
	}
	public Double getFailTraceRate() {
		return failTraceRate;
	}
	public void setFailTraceRate(Double failTraceRate) {
		this.failTraceRate = failTraceRate;
	}
	public Integer getInBobyCount() {
		return inBobyCount;
	}
	public void setInBobyCount(Integer inBobyCount) {
		this.inBobyCount = inBobyCount;
	}
	public Integer getInCallCount() {
		return inCallCount;
	}
	public void setInCallCount(Integer inCallCount) {
		this.inCallCount = inCallCount;
	}
	public Integer getCustomerCount() {
		return customerCount;
	}
	public void setCustomerCount(Integer customerCount) {
		this.customerCount = customerCount;
	}
	public Integer getTestDriverCount() {
		return testDriverCount;
	}
	public void setTestDriverCount(Integer testDriverCount) {
		this.testDriverCount = testDriverCount;
	}
	public Integer getHaveBargainCount() {
		return haveBargainCount;
	}
	public void setHaveBargainCount(Integer haveBargainCount) {
		this.haveBargainCount = haveBargainCount;
	}
	public Integer getSendCarCount() {
		return sendCarCount;
	}
	public void setSendCarCount(Integer sendCarCount) {
		this.sendCarCount = sendCarCount;
	}
	public Integer getDepositsCount() {
		return depositsCount;
	}
	public void setDepositsCount(Integer depositsCount) {
		this.depositsCount = depositsCount;
	}
	public Integer getLevelHCount() {
		return levelHCount;
	}
	public void setLevelHCount(Integer levelHCount) {
		this.levelHCount = levelHCount;
	}
	public Integer getLevelACount() {
		return levelACount;
	}
	public void setLevelACount(Integer levelACount) {
		this.levelACount = levelACount;
	}
	public Integer getLevelBCount() {
		return levelBCount;
	}
	public void setLevelBCount(Integer levelBCount) {
		this.levelBCount = levelBCount;
	}
	public Integer getLevelCCount() {
		return levelCCount;
	}
	public void setLevelCCount(Integer levelCCount) {
		this.levelCCount = levelCCount;
	}
	public Integer getVisitCallCount() {
		return visitCallCount;
	}
	public void setVisitCallCount(Integer visitCallCount) {
		this.visitCallCount = visitCallCount;
	}
	
}
