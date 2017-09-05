package com.infoservice.dms.chana.vo;

import java.util.Date;
import java.util.HashMap;

public class RepairOrderResultVO extends BaseVO {

	private String roNo; //下端：工单号  CHAR(12)  上端：  
	private String salesPartNo; //下端：配件销售单号  CHAR(12)  上端：  
	private String bookingOrderNo; //下端：预约单号  CHAR(12)  上端：  
	private String estimateNo; //下端：估价单号  CHAR(12)  上端：  
	private String soNO; //下端：销售订单编号  CHAR(12)  上端：  
	private Integer roType; //下端：工单类型  NUMERIC(8) 12531001 维修
	private String repairTypeCode; //下端：维修类型代码  CHAR(4)  上端：  
	private String otherRepairType; //下端：其他维修类型  VARCHAR(60)  上端：  
	private Integer vehicleTopDesc; //下端：车顶号  NUMERIC(8)  上端：  
	private String sequenceNo; //下端：车顶号序列码  VARCHAR(6)  上端：  
	private String primaryRoNo; //下端：原工单号  CHAR(12)  上端：  
	private String insurationNo; //下端：理赔单号  VARCHAR(15)  上端：  
	private String insurationCode; //下端：保险公司代码  CHAR(12)  上端：  
	private Integer isCustomerInAsc; //下端：客户是否在厂  NUMERIC(8) 下端传以下值
	private Integer isSeasonCheck; //下端：是否质检  NUMERIC(8) 下端传以下值
	private Integer oilRemain; //下端：进厂油料  NUMERIC(8) 12501001 0
	private Integer isWash; //下端：是否洗车  NUMERIC(8) 下端传以下值
	private Integer isTrace; //下端：是否三日电访  NUMERIC(8) 下端传以下值
	private Integer traceTime; //下端：三日电访时间  NUMERIC(8) 11251001 中午
	private String noTraceReason; //下端：不回访原因  VARCHAR(60)  上端：  
	private Integer needRoadTest; //下端：是否路试  NUMERIC(8) 下端传以下值
	private String recommendEmpName; //下端：推荐人  VARCHAR(30)  上端：  
	private String recommendCustomerName; //下端：推荐单位  VARCHAR(90)  上端：  
	private String serviceAdvisor; //下端：服务专员  CHAR(4)  上端：  
	private String serviceAdvisorAss; //下端：开单人员  CHAR(4)  上端：  
	private Integer roStatus; //下端：工单状态  NUMERIC(8) 12551001 在修
	private Date roCreateDate; //下端：工单开单日期  TIMESTAMP  上端：  
	private Date endTimeSupposed; //下端：预交车时间  TIMESTAMP  上端：  
	private String chiefTechnician; //下端：指定技师  CHAR(4)  上端：  
	private String ownerNo; //下端：车主编号  CHAR(12)  上端：  
	private String ownerName; //下端：车主  VARCHAR(120)  上端：  
	private Integer ownerProperty; //下端：车主性质  NUMERIC(8) 对应转换
	private String license; //下端：车牌号  VARCHAR(30)  上端：  
	private String vin; //下端：VIN  VARCHAR(17)  上端：  
	private String engineNo; //下端：发动机号  VARCHAR(30)  上端：  
	private String brand; //下端：品牌  VARCHAR(30)  上端：  
	private String series; //下端：车系  VARCHAR(30)  上端：  
	private String model; //下端：车型  VARCHAR(30)  上端：  
	private Double inMileage; //下端：进厂行驶里程  NUMERIC(10,2)  上端：  
	private Double outMileage; //下端：出厂行驶里程  NUMERIC(10,2)  上端：  
	private Integer isChangeOdograph; //下端：是否换表  NUMERIC(8) 下端传以下值
	private Double changeMileage; //下端：换表里程  NUMERIC(10,2)  上端：  
	private Double totalChangeMileage; //下端：累计换表里程  NUMERIC(10,2)  上端：  
	private Double totalMileage; //下端：行驶总里程  NUMERIC(10,2)  上端：  
	private String deliverer; //下端：送修人  VARCHAR(30)  上端：  
	private Integer delivererGender; //下端：送修人性别  NUMERIC(8) 前两种对应转换，未知转为0
	private String delivererPhone; //下端：送修人电话  VARCHAR(30)  上端：  
	private String delivererMobile; //下端：送修人手机  VARCHAR(30)  上端：  
	private String finishUser; //下端：完工验收人  CHAR(4)  上端：  
	private Integer completeTag; //下端：是否竣工  NUMERIC(8) 下端传以下值
	private Integer waitInfoTag; //下端：待信标志  NUMERIC(8) 下端传以下值
	private Integer waitPartTag; //下端：待料标志  NUMERIC(8) 下端传以下值
	private Date completeTime; //下端：竣工时间  TIMESTAMP  上端：  
	private Date forBalanceTime; //下端：提交结算时间  TIMESTAMP  上端：  
	private Integer deliveryTag; //下端：交车标识  NUMERIC(8) 12581001 已交车
	private Date deliveryDate; //下端：交车日期  TIMESTAMP  上端：  
	private String deliveryUser; //下端：交车人  VARCHAR(30)  上端：  
	private Float labourPrice; //下端：工时单价  NUMERIC(8,2)  上端：  
	private Double labourAmount; //下端：工时费  NUMERIC(12,2)  上端：  
	private Double repairPartAmount; //下端：维修材料费  NUMERIC(12,2)  上端：  
	private Double salesPartAmount; //下端：销售材料费  NUMERIC(12,2)  上端：  
	private Double additemAmount; //下端：附加项目费  NUMERIC(12,2)  上端：  
	private Double overItemAmount; //下端：辅料管理费  NUMERIC(10,2)  上端：  
	private Double repairAmount; //下端：维修金额  NUMERIC(12,2)  上端：  
	private Double estimateAmount; //下端：预估金额  NUMERIC(12,2)  上端：  
	private Double balanceAmount; //下端：结算金额  NUMERIC(14,2)  上端：  
	private Double receiveAmount; //下端：收款金额  NUMERIC(12,2)  上端：  
	private Double subObbAmount; //下端：去零金额  NUMERIC(12,2)  上端：  
	private Double derateAmount; //下端：减免金额  NUMERIC(12,2)  上端：  
	private Double firstEstimateAmount; //下端：首次预估金额  NUMERIC(12,2)  上端：  
	private Integer traceTag; //下端：跟踪标识  NUMERIC(8) 下端传以下值
	private String remark; //下端：备注  VARCHAR(300)  上端：  
	private String remark1; //下端：备注1  VARCHAR(300)  上端：  
	private String remark2; //下端：备注2  VARCHAR(300)  上端：  
	private String testDriver; //下端：试车员  CHAR(4)  上端：  
	private Date printRoTime; //下端：工单打印时间  TIMESTAMP  上端：  
	private Integer roChargeType; //下端：工单付费类型  NUMERIC(8) 均为空 上端：  
	private Date printRpTime; //下端：预先捡料单打印时间  TIMESTAMP  上端：  
	private Date estimateBeginTime; //下端：预计开工时间  TIMESTAMP  上端：  
	private Integer isActivity; //下端：是否参加活动  NUMERIC(8) 下端传以下值
	private Integer isCloseRo; //下端：是否仓库关单  NUMERIC(8) 下端传以下值
	private Integer isMaintain; //下端：是否含保养  NUMERIC(8) 下端传以下值
	private String customerDesc; //下端：顾客描述  VARCHAR(300)  上端：  
	private Integer roSplitStatus; //下端：工单拆分状态  NUMERIC(8) 13601001 未拆
	private String memberNo; //下端：会员编号  CHAR(12)  上端：  
	private Integer inReason; //下端：进厂原因  NUMERIC(8) 13851001 预约
	private Integer modifyNum; //下端：修改次数  NUMERIC(8) 普通数值 上端：  
	private Integer quoteEndAccurate; //下端：最终报价准确  NUMERIC(8) 均为空 上端：  
	private Integer customerPreCheck; //下端：陪客预检  NUMERIC(8) 均为空 上端：  
	private Integer checkedEnd; //下端：终检完成  NUMERIC(8) 均为空 上端：  
	private Integer explainedBalanceAccounts; //下端：解释结算单  NUMERIC(8) 均为空 上端：  
	private Integer notEnterWorkshop; //下端：工单不进车间  NUMERIC(8) 下端传以下值
	private Integer isTakePartOld; //下端：是否带走旧件  NUMERIC(8) 下端传以下值
	private String LockUser; //下端：锁定人  CHAR(4)  上端：
	private String repairTypeName; //维修类型名称	VARCHAR(30)	
	private HashMap<Integer, BaseVO> labourVoList; //下端：维修项目列表    上端：  
	private HashMap<Integer, BaseVO> repairPartVoList; //下端：维修配件列表    上端：  
	private HashMap<Integer, BaseVO> salesPartVoList; //下端：销售配件列表    上端：  
	private HashMap<Integer, BaseVO> addItemVoList; //下端：附加项目列表    上端：  
	private HashMap<Integer, BaseVO> manageVoList; //下端：辅料管理费列表    上端：  
	public String getRoNo() {
		return roNo;
	}
	public void setRoNo(String roNo) {
		this.roNo = roNo;
	}
	public String getSalesPartNo() {
		return salesPartNo;
	}
	public void setSalesPartNo(String salesPartNo) {
		this.salesPartNo = salesPartNo;
	}
	public String getBookingOrderNo() {
		return bookingOrderNo;
	}
	public void setBookingOrderNo(String bookingOrderNo) {
		this.bookingOrderNo = bookingOrderNo;
	}
	public String getEstimateNo() {
		return estimateNo;
	}
	public void setEstimateNo(String estimateNo) {
		this.estimateNo = estimateNo;
	}
	public String getSoNO() {
		return soNO;
	}
	public void setSoNO(String soNO) {
		this.soNO = soNO;
	}
	public Integer getRoType() {
		return roType;
	}
	public void setRoType(Integer roType) {
		this.roType = roType;
	}
	public String getRepairTypeCode() {
		return repairTypeCode;
	}
	public void setRepairTypeCode(String repairTypeCode) {
		this.repairTypeCode = repairTypeCode;
	}
	public String getOtherRepairType() {
		return otherRepairType;
	}
	public void setOtherRepairType(String otherRepairType) {
		this.otherRepairType = otherRepairType;
	}
	public Integer getVehicleTopDesc() {
		return vehicleTopDesc;
	}
	public void setVehicleTopDesc(Integer vehicleTopDesc) {
		this.vehicleTopDesc = vehicleTopDesc;
	}
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getPrimaryRoNo() {
		return primaryRoNo;
	}
	public void setPrimaryRoNo(String primaryRoNo) {
		this.primaryRoNo = primaryRoNo;
	}
	public String getInsurationNo() {
		return insurationNo;
	}
	public void setInsurationNo(String insurationNo) {
		this.insurationNo = insurationNo;
	}
	public String getInsurationCode() {
		return insurationCode;
	}
	public void setInsurationCode(String insurationCode) {
		this.insurationCode = insurationCode;
	}
	public Integer getIsCustomerInAsc() {
		return isCustomerInAsc;
	}
	public void setIsCustomerInAsc(Integer isCustomerInAsc) {
		this.isCustomerInAsc = isCustomerInAsc;
	}
	public Integer getIsSeasonCheck() {
		return isSeasonCheck;
	}
	public void setIsSeasonCheck(Integer isSeasonCheck) {
		this.isSeasonCheck = isSeasonCheck;
	}
	public Integer getOilRemain() {
		return oilRemain;
	}
	public void setOilRemain(Integer oilRemain) {
		this.oilRemain = oilRemain;
	}
	public Integer getIsWash() {
		return isWash;
	}
	public void setIsWash(Integer isWash) {
		this.isWash = isWash;
	}
	public Integer getIsTrace() {
		return isTrace;
	}
	public void setIsTrace(Integer isTrace) {
		this.isTrace = isTrace;
	}
	public Integer getTraceTime() {
		return traceTime;
	}
	public void setTraceTime(Integer traceTime) {
		this.traceTime = traceTime;
	}
	public String getNoTraceReason() {
		return noTraceReason;
	}
	public void setNoTraceReason(String noTraceReason) {
		this.noTraceReason = noTraceReason;
	}
	public Integer getNeedRoadTest() {
		return needRoadTest;
	}
	public void setNeedRoadTest(Integer needRoadTest) {
		this.needRoadTest = needRoadTest;
	}
	public String getRecommendEmpName() {
		return recommendEmpName;
	}
	public void setRecommendEmpName(String recommendEmpName) {
		this.recommendEmpName = recommendEmpName;
	}
	public String getRecommendCustomerName() {
		return recommendCustomerName;
	}
	public void setRecommendCustomerName(String recommendCustomerName) {
		this.recommendCustomerName = recommendCustomerName;
	}
	public String getServiceAdvisor() {
		return serviceAdvisor;
	}
	public void setServiceAdvisor(String serviceAdvisor) {
		this.serviceAdvisor = serviceAdvisor;
	}
	public String getServiceAdvisorAss() {
		return serviceAdvisorAss;
	}
	public void setServiceAdvisorAss(String serviceAdvisorAss) {
		this.serviceAdvisorAss = serviceAdvisorAss;
	}
	public Integer getRoStatus() {
		return roStatus;
	}
	public void setRoStatus(Integer roStatus) {
		this.roStatus = roStatus;
	}
	public Date getRoCreateDate() {
		return roCreateDate;
	}
	public void setRoCreateDate(Date roCreateDate) {
		this.roCreateDate = roCreateDate;
	}
	public Date getEndTimeSupposed() {
		return endTimeSupposed;
	}
	public void setEndTimeSupposed(Date endTimeSupposed) {
		this.endTimeSupposed = endTimeSupposed;
	}
	public String getChiefTechnician() {
		return chiefTechnician;
	}
	public void setChiefTechnician(String chiefTechnician) {
		this.chiefTechnician = chiefTechnician;
	}
	public String getOwnerNo() {
		return ownerNo;
	}
	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public Integer getOwnerProperty() {
		return ownerProperty;
	}
	public void setOwnerProperty(Integer ownerProperty) {
		this.ownerProperty = ownerProperty;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
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
	public Double getInMileage() {
		return inMileage;
	}
	public void setInMileage(Double inMileage) {
		this.inMileage = inMileage;
	}
	public Double getOutMileage() {
		return outMileage;
	}
	public void setOutMileage(Double outMileage) {
		this.outMileage = outMileage;
	}
	public Integer getIsChangeOdograph() {
		return isChangeOdograph;
	}
	public void setIsChangeOdograph(Integer isChangeOdograph) {
		this.isChangeOdograph = isChangeOdograph;
	}
	public Double getChangeMileage() {
		return changeMileage;
	}
	public void setChangeMileage(Double changeMileage) {
		this.changeMileage = changeMileage;
	}
	public Double getTotalChangeMileage() {
		return totalChangeMileage;
	}
	public void setTotalChangeMileage(Double totalChangeMileage) {
		this.totalChangeMileage = totalChangeMileage;
	}
	public Double getTotalMileage() {
		return totalMileage;
	}
	public void setTotalMileage(Double totalMileage) {
		this.totalMileage = totalMileage;
	}
	public String getDeliverer() {
		return deliverer;
	}
	public void setDeliverer(String deliverer) {
		this.deliverer = deliverer;
	}
	public Integer getDelivererGender() {
		return delivererGender;
	}
	public void setDelivererGender(Integer delivererGender) {
		this.delivererGender = delivererGender;
	}
	public String getDelivererPhone() {
		return delivererPhone;
	}
	public void setDelivererPhone(String delivererPhone) {
		this.delivererPhone = delivererPhone;
	}
	public String getDelivererMobile() {
		return delivererMobile;
	}
	public void setDelivererMobile(String delivererMobile) {
		this.delivererMobile = delivererMobile;
	}
	public String getFinishUser() {
		return finishUser;
	}
	public void setFinishUser(String finishUser) {
		this.finishUser = finishUser;
	}
	public Integer getCompleteTag() {
		return completeTag;
	}
	public void setCompleteTag(Integer completeTag) {
		this.completeTag = completeTag;
	}
	public Integer getWaitInfoTag() {
		return waitInfoTag;
	}
	public void setWaitInfoTag(Integer waitInfoTag) {
		this.waitInfoTag = waitInfoTag;
	}
	public Integer getWaitPartTag() {
		return waitPartTag;
	}
	public void setWaitPartTag(Integer waitPartTag) {
		this.waitPartTag = waitPartTag;
	}
	public Date getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}
	public Date getForBalanceTime() {
		return forBalanceTime;
	}
	public void setForBalanceTime(Date forBalanceTime) {
		this.forBalanceTime = forBalanceTime;
	}
	public Integer getDeliveryTag() {
		return deliveryTag;
	}
	public void setDeliveryTag(Integer deliveryTag) {
		this.deliveryTag = deliveryTag;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getDeliveryUser() {
		return deliveryUser;
	}
	public void setDeliveryUser(String deliveryUser) {
		this.deliveryUser = deliveryUser;
	}
	public Float getLabourPrice() {
		return labourPrice;
	}
	public void setLabourPrice(Float labourPrice) {
		this.labourPrice = labourPrice;
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
	public Double getSalesPartAmount() {
		return salesPartAmount;
	}
	public void setSalesPartAmount(Double salesPartAmount) {
		this.salesPartAmount = salesPartAmount;
	}
	public Double getAdditemAmount() {
		return additemAmount;
	}
	public void setAdditemAmount(Double additemAmount) {
		this.additemAmount = additemAmount;
	}
	public Double getOverItemAmount() {
		return overItemAmount;
	}
	public void setOverItemAmount(Double overItemAmount) {
		this.overItemAmount = overItemAmount;
	}
	public Double getRepairAmount() {
		return repairAmount;
	}
	public void setRepairAmount(Double repairAmount) {
		this.repairAmount = repairAmount;
	}
	public Double getEstimateAmount() {
		return estimateAmount;
	}
	public void setEstimateAmount(Double estimateAmount) {
		this.estimateAmount = estimateAmount;
	}
	public Double getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	public Double getReceiveAmount() {
		return receiveAmount;
	}
	public void setReceiveAmount(Double receiveAmount) {
		this.receiveAmount = receiveAmount;
	}
	public Double getSubObbAmount() {
		return subObbAmount;
	}
	public void setSubObbAmount(Double subObbAmount) {
		this.subObbAmount = subObbAmount;
	}
	public Double getDerateAmount() {
		return derateAmount;
	}
	public void setDerateAmount(Double derateAmount) {
		this.derateAmount = derateAmount;
	}
	public Double getFirstEstimateAmount() {
		return firstEstimateAmount;
	}
	public void setFirstEstimateAmount(Double firstEstimateAmount) {
		this.firstEstimateAmount = firstEstimateAmount;
	}
	public Integer getTraceTag() {
		return traceTag;
	}
	public void setTraceTag(Integer traceTag) {
		this.traceTag = traceTag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getTestDriver() {
		return testDriver;
	}
	public void setTestDriver(String testDriver) {
		this.testDriver = testDriver;
	}
	public Date getPrintRoTime() {
		return printRoTime;
	}
	public void setPrintRoTime(Date printRoTime) {
		this.printRoTime = printRoTime;
	}
	public Integer getRoChargeType() {
		return roChargeType;
	}
	public void setRoChargeType(Integer roChargeType) {
		this.roChargeType = roChargeType;
	}
	public Date getPrintRpTime() {
		return printRpTime;
	}
	public void setPrintRpTime(Date printRpTime) {
		this.printRpTime = printRpTime;
	}
	public Date getEstimateBeginTime() {
		return estimateBeginTime;
	}
	public void setEstimateBeginTime(Date estimateBeginTime) {
		this.estimateBeginTime = estimateBeginTime;
	}
	public Integer getIsActivity() {
		return isActivity;
	}
	public void setIsActivity(Integer isActivity) {
		this.isActivity = isActivity;
	}
	public Integer getIsCloseRo() {
		return isCloseRo;
	}
	public void setIsCloseRo(Integer isCloseRo) {
		this.isCloseRo = isCloseRo;
	}
	public Integer getIsMaintain() {
		return isMaintain;
	}
	public void setIsMaintain(Integer isMaintain) {
		this.isMaintain = isMaintain;
	}
	public String getCustomerDesc() {
		return customerDesc;
	}
	public void setCustomerDesc(String customerDesc) {
		this.customerDesc = customerDesc;
	}
	public Integer getRoSplitStatus() {
		return roSplitStatus;
	}
	public void setRoSplitStatus(Integer roSplitStatus) {
		this.roSplitStatus = roSplitStatus;
	}
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	public Integer getInReason() {
		return inReason;
	}
	public void setInReason(Integer inReason) {
		this.inReason = inReason;
	}
	public Integer getModifyNum() {
		return modifyNum;
	}
	public void setModifyNum(Integer modifyNum) {
		this.modifyNum = modifyNum;
	}
	public Integer getQuoteEndAccurate() {
		return quoteEndAccurate;
	}
	public void setQuoteEndAccurate(Integer quoteEndAccurate) {
		this.quoteEndAccurate = quoteEndAccurate;
	}
	public Integer getCustomerPreCheck() {
		return customerPreCheck;
	}
	public void setCustomerPreCheck(Integer customerPreCheck) {
		this.customerPreCheck = customerPreCheck;
	}
	public Integer getCheckedEnd() {
		return checkedEnd;
	}
	public void setCheckedEnd(Integer checkedEnd) {
		this.checkedEnd = checkedEnd;
	}
	public Integer getExplainedBalanceAccounts() {
		return explainedBalanceAccounts;
	}
	public void setExplainedBalanceAccounts(Integer explainedBalanceAccounts) {
		this.explainedBalanceAccounts = explainedBalanceAccounts;
	}
	public Integer getNotEnterWorkshop() {
		return notEnterWorkshop;
	}
	public void setNotEnterWorkshop(Integer notEnterWorkshop) {
		this.notEnterWorkshop = notEnterWorkshop;
	}
	public Integer getIsTakePartOld() {
		return isTakePartOld;
	}
	public void setIsTakePartOld(Integer isTakePartOld) {
		this.isTakePartOld = isTakePartOld;
	}
	public String getLockUser() {
		return LockUser;
	}
	public void setLockUser(String lockUser) {
		LockUser = lockUser;
	}
	public String getRepairTypeName() {
		return repairTypeName;
	}
	public void setRepairTypeName(String repairTypeName) {
		this.repairTypeName = repairTypeName;
	}
	public HashMap<Integer, BaseVO> getLabourVoList() {
		return labourVoList;
	}
	public void setLabourVoList(HashMap<Integer, BaseVO> labourVoList) {
		this.labourVoList = labourVoList;
	}
	public HashMap<Integer, BaseVO> getRepairPartVoList() {
		return repairPartVoList;
	}
	public void setRepairPartVoList(HashMap<Integer, BaseVO> repairPartVoList) {
		this.repairPartVoList = repairPartVoList;
	}
	public HashMap<Integer, BaseVO> getSalesPartVoList() {
		return salesPartVoList;
	}
	public void setSalesPartVoList(HashMap<Integer, BaseVO> salesPartVoList) {
		this.salesPartVoList = salesPartVoList;
	}
	public HashMap<Integer, BaseVO> getAddItemVoList() {
		return addItemVoList;
	}
	public void setAddItemVoList(HashMap<Integer, BaseVO> addItemVoList) {
		this.addItemVoList = addItemVoList;
	}
	public HashMap<Integer, BaseVO> getManageVoList() {
		return manageVoList;
	}
	public void setManageVoList(HashMap<Integer, BaseVO> manageVoList) {
		this.manageVoList = manageVoList;
	}
	
}
