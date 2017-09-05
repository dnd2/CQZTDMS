package com.infoservice.dms.chana.vo;

import java.util.Date;
import java.util.HashMap;

@SuppressWarnings("serial")
public class ActivityVO extends BaseVO {
	private String activityCode; //活动编号 varchar(15)
	private String activityName; //活动名称 varchar(60)
	private Integer activityType; //活动类型 NUMERIC(8)
	private String activityKind; //活动类别 varchar(150)
	private String launchEntity; //发起机构 CHAR(8)
	private String launchEntityName; //发起机构名称 varchar(300)
	private Date beginDate; //开始日期 TIMESTAMP
	private Date endDate; //结束日期 TIMESTAMP
	private Date releaseDate; //发布时间 TIMESTAMP
	private Integer releaseTag; //发布状态 NUMERIC(8)
	private Double labourAmount; //工时费 NUMERIC(12,2)
	private Double repairPartAmount; //维修材料费 NUMERIC(12,2)
	private Double activityAmount; //活动总金额 NUMERIC(12,2)
	private Double otherFee; //下端：其它费用  NUMERIC(12,2)  上端：OTHER_FEE NUMBER(9,2) 
	private Integer isClaim; //是否索赔 NUMERIC(8)
	private Integer isFree; //是否免费 NUMERIC(8)
	private Date salesDateBegin; //下端：销售开始日期 SALES_DATE_BEGIN TIMESTAMP  上端：  
	private Date salesDateEnd; //下端：销售结束日期 SALES_DATE_END TIMESTAMP  上端：  
	private Double mileageBegin; //下端：行程开始里程 MILEAGE_BEGIN NUMERIC(10,2)  上端：  
	private Double mileageEnd; //下端：行程结束里程 MILEAGE_END NUMERIC(10,2)  上端：  
	private Integer repairCount; //下端：参加活动总台次 ACTIVITY_SUM_COUNT NUMERIC(8)  上端：  
	private Integer singleCount; //下端：单台参加活动总次数 ACTIVITY_COUNT NUMERIC(8)  上端：  
	private Integer vehiclePurpose; //下端：车辆用途 VEHICLE_PURPOSE NUMERIC(8)  上端：  
	private Date productDateBegin; //下端：制造起始日期 PRODUCT_DATE_BEGIN TIMESTAMP  上端：  
	private Date productDateEnd; //下端：制造结束日期 PRODUCT_DATE_END TIMESTAMP  上端：  
	private HashMap<Integer, BaseVO> labourVoList; //维修项目列表 
	private HashMap<Integer, BaseVO> partVoList; //配件列表 
	private HashMap<Integer, BaseVO> modelVoList; //车型列表 
	private HashMap<Integer, BaseVO> vehicleVoList; //车辆列表 
	private HashMap<Integer, BaseVO> yieldlyVoList; //车辆产地列表 
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public Integer getActivityType() {
		return activityType;
	}
	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}
	public String getActivityKind() {
		return activityKind;
	}
	public void setActivityKind(String activityKind) {
		this.activityKind = activityKind;
	}
	public String getLaunchEntity() {
		return launchEntity;
	}
	public void setLaunchEntity(String launchEntity) {
		this.launchEntity = launchEntity;
	}
	public String getLaunchEntityName() {
		return launchEntityName;
	}
	public void setLaunchEntityName(String launchEntityName) {
		this.launchEntityName = launchEntityName;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public Integer getReleaseTag() {
		return releaseTag;
	}
	public void setReleaseTag(Integer releaseTag) {
		this.releaseTag = releaseTag;
	}
	public Double getLabourAmount() {
		return labourAmount;
	}
	public void setLabourAmount(Double labourAmount) {
		this.labourAmount = labourAmount;
	}
	public Double getRepairPartAmount() {
		return repairPartAmount;
	}
	public void setRepairPartAmount(Double repairPartAmount) {
		this.repairPartAmount = repairPartAmount;
	}
	public Double getActivityAmount() {
		return activityAmount;
	}
	public void setActivityAmount(Double activityAmount) {
		this.activityAmount = activityAmount;
	}
	public Double getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}
	public Integer getIsClaim() {
		return isClaim;
	}
	public void setIsClaim(Integer isClaim) {
		this.isClaim = isClaim;
	}
	public Integer getIsFree() {
		return isFree;
	}
	public void setIsFree(Integer isFree) {
		this.isFree = isFree;
	}
	public Date getSalesDateBegin() {
		return salesDateBegin;
	}
	public void setSalesDateBegin(Date salesDateBegin) {
		this.salesDateBegin = salesDateBegin;
	}
	public Date getSalesDateEnd() {
		return salesDateEnd;
	}
	public void setSalesDateEnd(Date salesDateEnd) {
		this.salesDateEnd = salesDateEnd;
	}
	public Double getMileageBegin() {
		return mileageBegin;
	}
	public void setMileageBegin(Double mileageBegin) {
		this.mileageBegin = mileageBegin;
	}
	public Double getMileageEnd() {
		return mileageEnd;
	}
	public void setMileageEnd(Double mileageEnd) {
		this.mileageEnd = mileageEnd;
	}
	public Integer getRepairCount() {
		return repairCount;
	}
	public void setRepairCount(Integer repairCount) {
		this.repairCount = repairCount;
	}
	public Integer getSingleCount() {
		return singleCount;
	}
	public void setSingleCount(Integer singleCount) {
		this.singleCount = singleCount;
	}
	public Integer getVehiclePurpose() {
		return vehiclePurpose;
	}
	public void setVehiclePurpose(Integer vehiclePurpose) {
		this.vehiclePurpose = vehiclePurpose;
	}
	public Date getProductDateBegin() {
		return productDateBegin;
	}
	public void setProductDateBegin(Date productDateBegin) {
		this.productDateBegin = productDateBegin;
	}
	public Date getProductDateEnd() {
		return productDateEnd;
	}
	public void setProductDateEnd(Date productDateEnd) {
		this.productDateEnd = productDateEnd;
	}
	public HashMap<Integer, BaseVO> getLabourVoList() {
		return labourVoList;
	}
	public void setLabourVoList(HashMap<Integer, BaseVO> labourVoList) {
		this.labourVoList = labourVoList;
	}
	public HashMap<Integer, BaseVO> getPartVoList() {
		return partVoList;
	}
	public void setPartVoList(HashMap<Integer, BaseVO> partVoList) {
		this.partVoList = partVoList;
	}
	public HashMap<Integer, BaseVO> getModelVoList() {
		return modelVoList;
	}
	public void setModelVoList(HashMap<Integer, BaseVO> modelVoList) {
		this.modelVoList = modelVoList;
	}
	public HashMap<Integer, BaseVO> getVehicleVoList() {
		return vehicleVoList;
	}
	public void setVehicleVoList(HashMap<Integer, BaseVO> vehicleVoList) {
		this.vehicleVoList = vehicleVoList;
	}
	public HashMap<Integer, BaseVO> getYieldlyVoList() {
		return yieldlyVoList;
	}
	public void setYieldlyVoList(HashMap<Integer, BaseVO> yieldlyVoList) {
		this.yieldlyVoList = yieldlyVoList;
	}
	
	
}
