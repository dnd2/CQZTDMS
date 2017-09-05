package com.infodms.yxdms.entity.repairorder;

import java.util.Date;

import com.infoservice.po3.bean.PO;
@SuppressWarnings("serial")
public class TtAsRepairOrderDeleteNewPO extends PO {
	private Integer balanceYieldly;
	private String soNo;
	private String customerDesc;
	private Integer traceTag;
	private Integer roType;
	private String remark;
	private Integer rosplitStatus;
	private Integer isTakePartOld;
	private Date completeTime;
	private Long updateBy;
	private String primaryRoNo;
	private Double labourAmount;
	private Integer orderValuableType;
	private String delivererAdress;
	private String delivererPhone;
	private Integer isReturn;
	private Date downTimestamp;
	private Integer activityType;
	private Double balanceAmount;
	private String serviceAdvisorAss;
	private Integer isChangeOdograph;
	private String recommendCustomerName;
	private Integer checkedEnd;
	private String salesPartNo;
	private String recommendEmpName;
	private Integer vehicleTopDesc;
	private Double outMileage;
	private Integer isvalid;
	private Long secondDealerId;
	private Integer isCustomerInAsc;
	private String deliveryUser;
	private Integer carUseType;
	private Date createDate;
	private Integer oilRemain;
	private String memberNo;
	private Double subobbAmount;
	private String sequenceNo;
	private Long dealerId;
	private Integer isActivity;
	private Integer inReason;
	private String troubleReason;
	private Date updateDate;
	private Double salesPartAmount;
	private String troubleDescriptions;
	private Long createBy;
	private Integer forlStatus;
	private Integer waitPartTag;
	private String chiefTechnician;
	private Double receiveAmount;
	private Date forBalanceTime;
	private Double totalMileage;
	private Integer isWash;
	private Integer dataType;
	private String deliverer;
	private String ownerNo;
	private String dealerShortname;
	private Date roCreateDate;
	private Double firstEstimateAmount;
	private Integer isClaimFore;
	private String delivererMobile;
	private String secondDealerName;
	private String testDriver;
	private String secondDealerCode;
	private String dealerName;
	private String lockUser;
	private String repairMethod;
	private String camCode;
	private Integer approvalYn;
	private String insurationNo;
	private Double overItemAmount;
	private Integer traceTime;
	private Integer modifyNum;
	private String license;
	private Integer roStatus;
	private String noTraceReason;
	private Integer roChargeType;
	private String estimateNo;
	private String ownerName;
	private Integer vinIntype;
	private String bookingOrderNo;
	private Long id;
	private Integer freeTimes;
	private Integer delivererGender;
	private String engineNo;
	private Double accessoriesPrice;//辅料费
	private Double compensationMoney;//补偿费
	private Integer completeTag;
	private Integer deliveryTag;
	private String remark1;
	private String auditContent;
	private String remark2;
	private Integer partFlag;
	private Double derateAmount;
	private String dealerCode;
	private Integer customerPreCheck;
	private Integer accreditAmount;
	private Double changeMileage;
	private Double totalChangeMileage;
	private Integer ownerProperty;
	private String insurationCode;
	private String model;
	private String roNo;
	private Integer needRoadTest;
	private Double labourPrice;
	private Integer ver;
	private Date printRoTime;
	private Long lineNo;
	private Integer notEnterWorkshop;
	private Date estimateBeginTime;
	private Integer quoteEndAccurate;
	private String remarks;
	private Double addItemAmount;
	private String vin;
	private Double repairAmount;
	private String series;
	private Double repairPartAmount;
	private Date deliveryDate;
	private String otherRepairType;
	private Integer isDel;
	private Integer quelityGrate;
	private Integer isCloseRo;
	private Integer waitInfoTag;
	private Integer ifStatus;
	private Date endTimeSupposed;
	private Integer isSeasonCheck;
	private String finishUser;
	private Integer isMaintain;
	private Integer isTrace;
	private Integer explainedBalanceAccounts;
	private String serviceAdvisor;
	private String repairTypeCode;
	private Integer isDe;
	private Date guaranteeDate;
	private Double estimateAmount;
	private Double inMileage;
	private String brand;
	private Date printRpTime;
	private Integer isWarning;
	private Integer isCancel;
	private String color;
	private String ownerPhone;
	private String packageName;
	private String carUseDesc;
	private String modelName ;
	private String roPackageId ;
	private String wrgroupId ;
	private String warningLevel;
	private String roModelId;
	private String keepFitNo;
	private Double discountAmount;
	private Double realAmount;
	private String freeRo;
	private Integer isVisit;//服务活动模板带过来是否回访
	
	public Integer getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Integer isCancel) {
		this.isCancel = isCancel;
	}

	public void setBalanceYieldly(Integer balanceYieldly){
		this.balanceYieldly=balanceYieldly;
	}

	public Integer getBalanceYieldly(){
		return this.balanceYieldly;
	}

	public void setSoNo(String soNo){
		this.soNo=soNo;
	}

	public String getSoNo(){
		return this.soNo;
	}

	public void setCustomerDesc(String customerDesc){
		this.customerDesc=customerDesc;
	}

	public String getCustomerDesc(){
		return this.customerDesc;
	}

	public void setTraceTag(Integer traceTag){
		this.traceTag=traceTag;
	}

	public Integer getTraceTag(){
		return this.traceTag;
	}

	public void setRoType(Integer roType){
		this.roType=roType;
	}

	public Integer getRoType(){
		return this.roType;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setRosplitStatus(Integer rosplitStatus){
		this.rosplitStatus=rosplitStatus;
	}

	public Integer getRosplitStatus(){
		return this.rosplitStatus;
	}

	public void setIsTakePartOld(Integer isTakePartOld){
		this.isTakePartOld=isTakePartOld;
	}

	public Integer getIsTakePartOld(){
		return this.isTakePartOld;
	}

	public void setCompleteTime(Date completeTime){
		this.completeTime=completeTime;
	}

	public Date getCompleteTime(){
		return this.completeTime;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPrimaryRoNo(String primaryRoNo){
		this.primaryRoNo=primaryRoNo;
	}

	public String getPrimaryRoNo(){
		return this.primaryRoNo;
	}

	public void setLabourAmount(Double labourAmount){
		this.labourAmount=labourAmount;
	}

	public Double getLabourAmount(){
		return this.labourAmount;
	}

	public void setOrderValuableType(Integer orderValuableType){
		this.orderValuableType=orderValuableType;
	}

	public Integer getOrderValuableType(){
		return this.orderValuableType;
	}

	public void setDelivererAdress(String delivererAdress){
		this.delivererAdress=delivererAdress;
	}

	public String getDelivererAdress(){
		return this.delivererAdress;
	}

	public void setDelivererPhone(String delivererPhone){
		this.delivererPhone=delivererPhone;
	}

	public String getDelivererPhone(){
		return this.delivererPhone;
	}

	public void setIsReturn(Integer isReturn){
		this.isReturn=isReturn;
	}

	public Integer getIsReturn(){
		return this.isReturn;
	}

	public void setDownTimestamp(Date downTimestamp){
		this.downTimestamp=downTimestamp;
	}

	public Date getDownTimestamp(){
		return this.downTimestamp;
	}

	public void setActivityType(Integer activityType){
		this.activityType=activityType;
	}

	public Integer getActivityType(){
		return this.activityType;
	}

	public void setBalanceAmount(Double balanceAmount){
		this.balanceAmount=balanceAmount;
	}

	public Double getBalanceAmount(){
		return this.balanceAmount;
	}

	public void setServiceAdvisorAss(String serviceAdvisorAss){
		this.serviceAdvisorAss=serviceAdvisorAss;
	}

	public String getServiceAdvisorAss(){
		return this.serviceAdvisorAss;
	}

	public void setIsChangeOdograph(Integer isChangeOdograph){
		this.isChangeOdograph=isChangeOdograph;
	}

	public Integer getIsChangeOdograph(){
		return this.isChangeOdograph;
	}

	public void setRecommendCustomerName(String recommendCustomerName){
		this.recommendCustomerName=recommendCustomerName;
	}

	public String getRecommendCustomerName(){
		return this.recommendCustomerName;
	}

	public void setCheckedEnd(Integer checkedEnd){
		this.checkedEnd=checkedEnd;
	}

	public Integer getCheckedEnd(){
		return this.checkedEnd;
	}

	public void setSalesPartNo(String salesPartNo){
		this.salesPartNo=salesPartNo;
	}

	public String getSalesPartNo(){
		return this.salesPartNo;
	}

	public void setRecommendEmpName(String recommendEmpName){
		this.recommendEmpName=recommendEmpName;
	}

	public String getRecommendEmpName(){
		return this.recommendEmpName;
	}

	public void setVehicleTopDesc(Integer vehicleTopDesc){
		this.vehicleTopDesc=vehicleTopDesc;
	}

	public Integer getVehicleTopDesc(){
		return this.vehicleTopDesc;
	}

	public void setOutMileage(Double outMileage){
		this.outMileage=outMileage;
	}

	public Double getOutMileage(){
		return this.outMileage;
	}

	public void setIsvalid(Integer isvalid){
		this.isvalid=isvalid;
	}

	public Integer getIsvalid(){
		return this.isvalid;
	}

	public void setSecondDealerId(Long secondDealerId){
		this.secondDealerId=secondDealerId;
	}

	public Long getSecondDealerId(){
		return this.secondDealerId;
	}

	public void setIsCustomerInAsc(Integer isCustomerInAsc){
		this.isCustomerInAsc=isCustomerInAsc;
	}

	public Integer getIsCustomerInAsc(){
		return this.isCustomerInAsc;
	}

	public void setDeliveryUser(String deliveryUser){
		this.deliveryUser=deliveryUser;
	}

	public String getDeliveryUser(){
		return this.deliveryUser;
	}

	public void setCarUseType(Integer carUseType){
		this.carUseType=carUseType;
	}

	public Integer getCarUseType(){
		return this.carUseType;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOilRemain(Integer oilRemain){
		this.oilRemain=oilRemain;
	}

	public Integer getOilRemain(){
		return this.oilRemain;
	}

	public void setMemberNo(String memberNo){
		this.memberNo=memberNo;
	}

	public String getMemberNo(){
		return this.memberNo;
	}

	public void setSubobbAmount(Double subobbAmount){
		this.subobbAmount=subobbAmount;
	}

	public Double getSubobbAmount(){
		return this.subobbAmount;
	}

	public void setSequenceNo(String sequenceNo){
		this.sequenceNo=sequenceNo;
	}

	public String getSequenceNo(){
		return this.sequenceNo;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setIsActivity(Integer isActivity){
		this.isActivity=isActivity;
	}

	public Integer getIsActivity(){
		return this.isActivity;
	}

	public void setInReason(Integer inReason){
		this.inReason=inReason;
	}

	public Integer getInReason(){
		return this.inReason;
	}

	public void setTroubleReason(String troubleReason){
		this.troubleReason=troubleReason;
	}

	public String getTroubleReason(){
		return this.troubleReason;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setSalesPartAmount(Double salesPartAmount){
		this.salesPartAmount=salesPartAmount;
	}

	public Double getSalesPartAmount(){
		return this.salesPartAmount;
	}

	public void setTroubleDescriptions(String troubleDescriptions){
		this.troubleDescriptions=troubleDescriptions;
	}

	public String getTroubleDescriptions(){
		return this.troubleDescriptions;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setForlStatus(Integer forlStatus){
		this.forlStatus=forlStatus;
	}

	public Integer getForlStatus(){
		return this.forlStatus;
	}

	public void setWaitPartTag(Integer waitPartTag){
		this.waitPartTag=waitPartTag;
	}

	public Integer getWaitPartTag(){
		return this.waitPartTag;
	}

	public void setChiefTechnician(String chiefTechnician){
		this.chiefTechnician=chiefTechnician;
	}

	public String getChiefTechnician(){
		return this.chiefTechnician;
	}

	public void setReceiveAmount(Double receiveAmount){
		this.receiveAmount=receiveAmount;
	}

	public Double getReceiveAmount(){
		return this.receiveAmount;
	}

	public void setForBalanceTime(Date forBalanceTime){
		this.forBalanceTime=forBalanceTime;
	}

	public Date getForBalanceTime(){
		return this.forBalanceTime;
	}

	public void setTotalMileage(Double totalMileage){
		this.totalMileage=totalMileage;
	}

	public Double getTotalMileage(){
		return this.totalMileage;
	}

	public void setIsWash(Integer isWash){
		this.isWash=isWash;
	}

	public Integer getIsWash(){
		return this.isWash;
	}

	public void setDataType(Integer dataType){
		this.dataType=dataType;
	}

	public Integer getDataType(){
		return this.dataType;
	}

	public void setDeliverer(String deliverer){
		this.deliverer=deliverer;
	}

	public String getDeliverer(){
		return this.deliverer;
	}

	public void setOwnerNo(String ownerNo){
		this.ownerNo=ownerNo;
	}

	public String getOwnerNo(){
		return this.ownerNo;
	}

	public void setDealerShortname(String dealerShortname){
		this.dealerShortname=dealerShortname;
	}

	public String getDealerShortname(){
		return this.dealerShortname;
	}

	public void setRoCreateDate(Date roCreateDate){
		this.roCreateDate=roCreateDate;
	}

	public Date getRoCreateDate(){
		return this.roCreateDate;
	}

	public void setFirstEstimateAmount(Double firstEstimateAmount){
		this.firstEstimateAmount=firstEstimateAmount;
	}

	public Double getFirstEstimateAmount(){
		return this.firstEstimateAmount;
	}

	public void setIsClaimFore(Integer isClaimFore){
		this.isClaimFore=isClaimFore;
	}

	public Integer getIsClaimFore(){
		return this.isClaimFore;
	}

	public void setDelivererMobile(String delivererMobile){
		this.delivererMobile=delivererMobile;
	}

	public String getDelivererMobile(){
		return this.delivererMobile;
	}

	public void setSecondDealerName(String secondDealerName){
		this.secondDealerName=secondDealerName;
	}

	public String getSecondDealerName(){
		return this.secondDealerName;
	}

	public void setTestDriver(String testDriver){
		this.testDriver=testDriver;
	}

	public String getTestDriver(){
		return this.testDriver;
	}

	public void setSecondDealerCode(String secondDealerCode){
		this.secondDealerCode=secondDealerCode;
	}

	public String getSecondDealerCode(){
		return this.secondDealerCode;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setLockUser(String lockUser){
		this.lockUser=lockUser;
	}

	public String getLockUser(){
		return this.lockUser;
	}

	public void setRepairMethod(String repairMethod){
		this.repairMethod=repairMethod;
	}

	public String getRepairMethod(){
		return this.repairMethod;
	}

	public void setCamCode(String camCode){
		this.camCode=camCode;
	}

	public String getCamCode(){
		return this.camCode;
	}

	public void setApprovalYn(Integer approvalYn){
		this.approvalYn=approvalYn;
	}

	public Integer getApprovalYn(){
		return this.approvalYn;
	}

	public void setInsurationNo(String insurationNo){
		this.insurationNo=insurationNo;
	}

	public String getInsurationNo(){
		return this.insurationNo;
	}

	public void setOverItemAmount(Double overItemAmount){
		this.overItemAmount=overItemAmount;
	}

	public Double getOverItemAmount(){
		return this.overItemAmount;
	}

	public void setTraceTime(Integer traceTime){
		this.traceTime=traceTime;
	}

	public Integer getTraceTime(){
		return this.traceTime;
	}

	public void setModifyNum(Integer modifyNum){
		this.modifyNum=modifyNum;
	}

	public Integer getModifyNum(){
		return this.modifyNum;
	}

	public void setLicense(String license){
		this.license=license;
	}

	public String getLicense(){
		return this.license;
	}

	public void setRoStatus(Integer roStatus){
		this.roStatus=roStatus;
	}

	public Integer getRoStatus(){
		return this.roStatus;
	}

	public void setNoTraceReason(String noTraceReason){
		this.noTraceReason=noTraceReason;
	}

	public String getNoTraceReason(){
		return this.noTraceReason;
	}

	public void setRoChargeType(Integer roChargeType){
		this.roChargeType=roChargeType;
	}

	public Integer getRoChargeType(){
		return this.roChargeType;
	}

	public void setEstimateNo(String estimateNo){
		this.estimateNo=estimateNo;
	}

	public String getEstimateNo(){
		return this.estimateNo;
	}

	public void setOwnerName(String ownerName){
		this.ownerName=ownerName;
	}

	public String getOwnerName(){
		return this.ownerName;
	}

	public void setVinIntype(Integer vinIntype){
		this.vinIntype=vinIntype;
	}

	public Integer getVinIntype(){
		return this.vinIntype;
	}

	public void setBookingOrderNo(String bookingOrderNo){
		this.bookingOrderNo=bookingOrderNo;
	}

	public String getBookingOrderNo(){
		return this.bookingOrderNo;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setFreeTimes(Integer freeTimes){
		this.freeTimes=freeTimes;
	}

	public Integer getFreeTimes(){
		return this.freeTimes;
	}

	public void setDelivererGender(Integer delivererGender){
		this.delivererGender=delivererGender;
	}

	public Integer getDelivererGender(){
		return this.delivererGender;
	}

	public void setEngineNo(String engineNo){
		this.engineNo=engineNo;
	}

	public String getEngineNo(){
		return this.engineNo;
	}

	public void setAccessoriesPrice(Double accessoriesPrice){
		this.accessoriesPrice=accessoriesPrice;
	}

	public Double getAccessoriesPrice(){
		return this.accessoriesPrice;
	}

	public void setCompleteTag(Integer completeTag){
		this.completeTag=completeTag;
	}

	public Integer getCompleteTag(){
		return this.completeTag;
	}

	public void setDeliveryTag(Integer deliveryTag){
		this.deliveryTag=deliveryTag;
	}

	public Integer getDeliveryTag(){
		return this.deliveryTag;
	}

	public void setRemark1(String remark1){
		this.remark1=remark1;
	}

	public String getRemark1(){
		return this.remark1;
	}

	public void setAuditContent(String auditContent){
		this.auditContent=auditContent;
	}

	public String getAuditContent(){
		return this.auditContent;
	}

	public void setRemark2(String remark2){
		this.remark2=remark2;
	}

	public String getRemark2(){
		return this.remark2;
	}

	public void setPartFlag(Integer partFlag){
		this.partFlag=partFlag;
	}

	public Integer getPartFlag(){
		return this.partFlag;
	}

	public void setDerateAmount(Double derateAmount){
		this.derateAmount=derateAmount;
	}

	public Double getDerateAmount(){
		return this.derateAmount;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setCustomerPreCheck(Integer customerPreCheck){
		this.customerPreCheck=customerPreCheck;
	}

	public Integer getCustomerPreCheck(){
		return this.customerPreCheck;
	}

	public void setAccreditAmount(Integer accreditAmount){
		this.accreditAmount=accreditAmount;
	}

	public Integer getAccreditAmount(){
		return this.accreditAmount;
	}

	public void setChangeMileage(Double changeMileage){
		this.changeMileage=changeMileage;
	}

	public Double getChangeMileage(){
		return this.changeMileage;
	}

	public void setTotalChangeMileage(Double totalChangeMileage){
		this.totalChangeMileage=totalChangeMileage;
	}

	public Double getTotalChangeMileage(){
		return this.totalChangeMileage;
	}

	public void setOwnerProperty(Integer ownerProperty){
		this.ownerProperty=ownerProperty;
	}

	public Integer getOwnerProperty(){
		return this.ownerProperty;
	}

	public void setInsurationCode(String insurationCode){
		this.insurationCode=insurationCode;
	}

	public String getInsurationCode(){
		return this.insurationCode;
	}

	public void setModel(String model){
		this.model=model;
	}

	public String getModel(){
		return this.model;
	}

	public void setRoNo(String roNo){
		this.roNo=roNo;
	}

	public String getRoNo(){
		return this.roNo;
	}

	public void setNeedRoadTest(Integer needRoadTest){
		this.needRoadTest=needRoadTest;
	}

	public Integer getNeedRoadTest(){
		return this.needRoadTest;
	}

	public void setLabourPrice(Double labourPrice){
		this.labourPrice=labourPrice;
	}

	public Double getLabourPrice(){
		return this.labourPrice;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setPrintRoTime(Date printRoTime){
		this.printRoTime=printRoTime;
	}

	public Date getPrintRoTime(){
		return this.printRoTime;
	}

	public void setLineNo(Long lineNo){
		this.lineNo=lineNo;
	}

	public Long getLineNo(){
		return this.lineNo;
	}

	public void setNotEnterWorkshop(Integer notEnterWorkshop){
		this.notEnterWorkshop=notEnterWorkshop;
	}

	public Integer getNotEnterWorkshop(){
		return this.notEnterWorkshop;
	}

	public void setEstimateBeginTime(Date estimateBeginTime){
		this.estimateBeginTime=estimateBeginTime;
	}

	public Date getEstimateBeginTime(){
		return this.estimateBeginTime;
	}

	public void setQuoteEndAccurate(Integer quoteEndAccurate){
		this.quoteEndAccurate=quoteEndAccurate;
	}

	public Integer getQuoteEndAccurate(){
		return this.quoteEndAccurate;
	}

	public void setRemarks(String remarks){
		this.remarks=remarks;
	}

	public String getRemarks(){
		return this.remarks;
	}

	public void setAddItemAmount(Double addItemAmount){
		this.addItemAmount=addItemAmount;
	}

	public Double getAddItemAmount(){
		return this.addItemAmount;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setRepairAmount(Double repairAmount){
		this.repairAmount=repairAmount;
	}

	public Double getRepairAmount(){
		return this.repairAmount;
	}

	public void setSeries(String series){
		this.series=series;
	}

	public String getSeries(){
		return this.series;
	}

	public void setRepairPartAmount(Double repairPartAmount){
		this.repairPartAmount=repairPartAmount;
	}

	public Double getRepairPartAmount(){
		return this.repairPartAmount;
	}

	public void setDeliveryDate(Date deliveryDate){
		this.deliveryDate=deliveryDate;
	}

	public Date getDeliveryDate(){
		return this.deliveryDate;
	}

	public void setOtherRepairType(String otherRepairType){
		this.otherRepairType=otherRepairType;
	}

	public String getOtherRepairType(){
		return this.otherRepairType;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setQuelityGrate(Integer quelityGrate){
		this.quelityGrate=quelityGrate;
	}

	public Integer getQuelityGrate(){
		return this.quelityGrate;
	}

	public void setIsCloseRo(Integer isCloseRo){
		this.isCloseRo=isCloseRo;
	}

	public Integer getIsCloseRo(){
		return this.isCloseRo;
	}

	public void setWaitInfoTag(Integer waitInfoTag){
		this.waitInfoTag=waitInfoTag;
	}

	public Integer getWaitInfoTag(){
		return this.waitInfoTag;
	}

	public void setIfStatus(Integer ifStatus){
		this.ifStatus=ifStatus;
	}

	public Integer getIfStatus(){
		return this.ifStatus;
	}

	public void setEndTimeSupposed(Date endTimeSupposed){
		this.endTimeSupposed=endTimeSupposed;
	}

	public Date getEndTimeSupposed(){
		return this.endTimeSupposed;
	}

	public void setIsSeasonCheck(Integer isSeasonCheck){
		this.isSeasonCheck=isSeasonCheck;
	}

	public Integer getIsSeasonCheck(){
		return this.isSeasonCheck;
	}

	public void setFinishUser(String finishUser){
		this.finishUser=finishUser;
	}

	public String getFinishUser(){
		return this.finishUser;
	}

	public void setIsMaintain(Integer isMaintain){
		this.isMaintain=isMaintain;
	}

	public Integer getIsMaintain(){
		return this.isMaintain;
	}

	public void setIsTrace(Integer isTrace){
		this.isTrace=isTrace;
	}

	public Integer getIsTrace(){
		return this.isTrace;
	}

	public void setExplainedBalanceAccounts(Integer explainedBalanceAccounts){
		this.explainedBalanceAccounts=explainedBalanceAccounts;
	}

	public Integer getExplainedBalanceAccounts(){
		return this.explainedBalanceAccounts;
	}

	public void setServiceAdvisor(String serviceAdvisor){
		this.serviceAdvisor=serviceAdvisor;
	}

	public String getServiceAdvisor(){
		return this.serviceAdvisor;
	}

	public void setRepairTypeCode(String repairTypeCode){
		this.repairTypeCode=repairTypeCode;
	}

	public String getRepairTypeCode(){
		return this.repairTypeCode;
	}

	public void setIsDe(Integer isDe){
		this.isDe=isDe;
	}

	public Integer getIsDe(){
		return this.isDe;
	}

	public void setGuaranteeDate(Date guaranteeDate){
		this.guaranteeDate=guaranteeDate;
	}

	public Date getGuaranteeDate(){
		return this.guaranteeDate;
	}

	public void setEstimateAmount(Double estimateAmount){
		this.estimateAmount=estimateAmount;
	}

	public Double getEstimateAmount(){
		return this.estimateAmount;
	}

	public void setInMileage(Double inMileage){
		this.inMileage=inMileage;
	}

	public Double getInMileage(){
		return this.inMileage;
	}

	public void setBrand(String brand){
		this.brand=brand;
	}

	public String getBrand(){
		return this.brand;
	}

	public void setPrintRpTime(Date printRpTime){
		this.printRpTime=printRpTime;
	}

	public Date getPrintRpTime(){
		return this.printRpTime;
	}

	public Double getCompensationMoney() {
		return compensationMoney;
	}

	public void setCompensationMoney(Double compensationMoney) {
		this.compensationMoney = compensationMoney;
	}

	public Integer getIsWarning() {
		return isWarning;
	}

	public void setIsWarning(Integer isWarning) {
		this.isWarning = isWarning;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getOwnerPhone() {
		return ownerPhone;
	}

	public void setOwnerPhone(String ownerPhone) {
		this.ownerPhone = ownerPhone;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getCarUseDesc() {
		return carUseDesc;
	}

	public void setCarUseDesc(String carUseDesc) {
		this.carUseDesc = carUseDesc;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getWrgroupId() {
		return wrgroupId;
	}

	public void setWrgroupId(String wrgroupId) {
		this.wrgroupId = wrgroupId;
	}

	public String getWarningLevel() {
		return warningLevel;
	}

	public void setWarningLevel(String warningLevel) {
		this.warningLevel = warningLevel;
	}

	public String getRoPackageId() {
		return roPackageId;
	}

	public void setRoPackageId(String roPackageId) {
		this.roPackageId = roPackageId;
	}

	public String getRoModelId() {
		return roModelId;
	}

	public void setRoModelId(String roModelId) {
		this.roModelId = roModelId;
	}

	public String getKeepFitNo() {
		return keepFitNo;
	}

	public void setKeepFitNo(String keepFitNo) {
		this.keepFitNo = keepFitNo;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(Double realAmount) {
		this.realAmount = realAmount;
	}

	public String getFreeRo() {
		return freeRo;
	}

	public void setFreeRo(String freeRo) {
		this.freeRo = freeRo;
	}

	public Integer getIsVisit() {
		return isVisit;
	}

	public void setIsVisit(Integer isVisit) {
		this.isVisit = isVisit;
	}


}
