package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

/**
 * @author PGM 服务活动管理（车厂端）
 */
public class TtAsActivityBean extends PO {
	private static final long serialVersionUID = -5857170292107600122L;
	private String troubleDesc;
	private String troubleReason;
	private String repairMethod;
	private String appRemark;
	private String subject_no;
	private String vehicle_num;
	private String sumcar;
	private String fsumcar;
	private String partUnit;
	private String new_id;
	private String subject_name;
	private String tow_type_activity;
	private String subjectId;
	private String companyId;
	private String createBy;
	private String activityId;
	private String isClaim;
	private String createDate;
	private String evaluateid;
	private String uploadPrePeriod;
	private String partFee;
	private String link;
	private String activity_num;
	private String activityCode;
	private String status;
	private String duty_person;
	private String subject_start_date;
	private String subject_end_date;
	private String activityType;
	private String releasedate;
	private String updateDate;
	private String dealwith;
	private String solution;
	private String enddate;
	private String worktimeFee;
	private String accessory;
	private String isFixfee;
	private String carNum;
	private String claimGuide;
	private String updateBy;
	private String claimType;
	private String otherFee;
	private String activityKind;
	private String activityName;
	private String startdate;
	private String isDel;
	private String itemId;
	private String itemCode;
	private String itemName;
	private String normalLabor;
	private String dealerName;
	private String activityFee;
	private Integer isSpec;
	private Double discount;
	private Integer fore;
	private Integer payType; //付费类型
	private Integer isGua; //是否在三包内
	private Integer singleCarNum;
	private Integer maxCar;
	private Long setDirect;
	private String activity_code;
	private String activity_name;
	private String part_num;
	private String part_num_w;
	private String malFunction;
	private Long malId;
    private Integer days; 
	private String hasPart;
	// 配件
	private String partsId;
	private String partNo;
	private String partName;
	private String isMainPart;
	private String partAmount;
	private String partPrice;
	private String partQuantity;
	private String on_amount;
	private String on_camount;
	private String evaluate;
	private String measures;
	private String labour;
	private String remark;
	private String amount;
	private String id;
	private String itemCodes;
	private String itemDesc;
	private String groupCode;
	private String groupName;
	private String pratResponsDesc;
	private Integer pratResponsId;
	private Long realPartId;
	private String factstartdate;
	private String factenddate;
	private String mainPartCode;
	private String mainPartName;
	private Integer partUseType;
	private String partUseName;
	public String getPartUseName() {
		return partUseName;
	}

	public void setPartUseName(String partUseName) {
		this.partUseName = partUseName;
	}

	public Integer getPartUseType() {
		return partUseType;
	}

	public void setPartUseType(Integer partUseType) {
		this.partUseType = partUseType;
	}

	//车龄
	private String idAge;
	private String saleDateEnd;
	private String saleDateStart;
	private String customerType;
	private String dateType;
	// MATERIAL_GROUP_ID
	private String materialGroupId;
	// 车辆性质
	private String codeId;
	private String codeDesc;
	private String carCharactor;
	// 车型列表
	private String groupId;
	private String groupLevel;
	private String parentGroupId;
	private String parentGroupName;
	private String parentGroupCode;
	// 活动工时
	private String labourId;
	private String labourCode;
	private String cnDes;
	private String labourHour;
	private String dealerId;
    private String customerName;
    private String vin;
    private String carStatus;
    private String dealerCode;
    // 活动评估
    private String inAmount;
    //服务活动计划分析 
    private String bn;
    private String cn;
    private String en;
    private String pers;
    //生产基地
    private String carYieldly;
    //服务活动车辆范围：售前车\售后车
    private String vehicleArea;
    //工时价格
    private Double parameterValue;
    //金额
    private Double laborFee;
	private Long   milageConfine;
	
	private String pull_in_num ;
	private String pull_in_mean ;
	private String pull_in_region ;
	private String pull_in_incre ;
	private String customer_num ;
	private String customer_mean ;
	private String customer_region ;
	private String customer_incre ;
	private String price_num ;
	private String price_mean ;
	private String price_region ;
	private String price_incre ;
	private String open_num ;
	private String open_mean ;
	private String open_region ;
	private String open_incre ;
	private Integer reStatus; 
	public Integer getReStatus() {
		return reStatus;
	}

	public void setReStatus(Integer reStatus) {
		this.reStatus = reStatus;
	}

	public Long getMilageConfine() {
		return milageConfine;
	}

	public void setMilageConfine(Long milageConfine) {
		this.milageConfine = milageConfine;
	}
    
    
	public Double getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(Double parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getCompanyId() {
		return companyId;
	}

	public String getParentGroupCode() {
		return parentGroupCode;
	}

	public void setParentGroupCode(String parentGroupCode) {
		this.parentGroupCode = parentGroupCode;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getIsClaim() {
		return isClaim;
	}

	public void setIsClaim(String isClaim) {
		this.isClaim = isClaim;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUploadPrePeriod() {
		return uploadPrePeriod;
	}

	public Integer getSingleCarNum() {
		return singleCarNum;
	}

	public void setSingleCarNum(Integer singleCarNum) {
		this.singleCarNum = singleCarNum;
	}

	public Integer getMaxCar() {
		return maxCar;
	}

	public void setMaxCar(Integer maxCar) {
		this.maxCar = maxCar;
	}


	public Long getSetDirect()
	{
		return setDirect;
	}

	public void setSetDirect(Long setDirect)
	{
		this.setDirect = setDirect;
	}

	public void setUploadPrePeriod(String uploadPrePeriod) {
		this.uploadPrePeriod = uploadPrePeriod;
	}

	public String getPartFee() {
		return partFee;
	}

	public void setPartFee(String partFee) {
		this.partFee = partFee;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getReleasedate() {
		return releasedate;
	}

	public void setReleasedate(String releasedate) {
		this.releasedate = releasedate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getDealwith() {
		return dealwith;
	}

	public void setDealwith(String dealwith) {
		this.dealwith = dealwith;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getWorktimeFee() {
		return worktimeFee;
	}

	public void setWorktimeFee(String worktimeFee) {
		this.worktimeFee = worktimeFee;
	}

	public String getAccessory() {
		return accessory;
	}

	public void setAccessory(String accessory) {
		this.accessory = accessory;
	}

	public String getIsFixfee() {
		return isFixfee;
	}

	public void setIsFixfee(String isFixfee) {
		this.isFixfee = isFixfee;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getClaimGuide() {
		return claimGuide;
	}

	public void setClaimGuide(String claimGuide) {
		this.claimGuide = claimGuide;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}

	public String getActivityKind() {
		return activityKind;
	}

	public void setActivityKind(String activityKind) {
		this.activityKind = activityKind;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getNormalLabor() {
		return normalLabor;
	}

	public void setNormalLabor(String normalLabor) {
		this.normalLabor = normalLabor;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getActivityFee() {
		return activityFee;
	}

	public void setActivityFee(String activityFee) {
		this.activityFee = activityFee;
	}

	public String getPartsId() {
		return partsId;
	}

	public void setPartsId(String partsId) {
		this.partsId = partsId;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartQuantity() {
		return partQuantity;
	}

	public void setPartQuantity(String partQuantity) {
		this.partQuantity = partQuantity;
	}

	public String getIsMainPart() {
		return isMainPart;
	}

	public void setIsMainPart(String isMainPart) {
		this.isMainPart = isMainPart;
	}

	public String getPartAmount() {
		return partAmount;
	}

	public void setPartAmount(String partAmount) {
		this.partAmount = partAmount;
	}

	public String getPartPrice() {
		return partPrice;
	}

	public void setPartPrice(String partPrice) {
		this.partPrice = partPrice;
	}

	public String getPartUnit() {
		return partUnit;
	}

	public void setPartUnit(String partUnit) {
		this.partUnit = partUnit;
	}

	public String getItemCodes() {
		return itemCodes;
	}

	public void setItemCodes(String itemCodes) {
		this.itemCodes = itemCodes;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdAge() {
		return idAge;
	}

	public void setIdAge(String idAge) {
		this.idAge = idAge;
	}

	public String getSaleDateEnd() {
		return saleDateEnd;
	}

	public void setSaleDateEnd(String saleDateEnd) {
		this.saleDateEnd = saleDateEnd;
	}

	public String getSaleDateStart() {
		return saleDateStart;
	}

	public void setSaleDateStart(String saleDateStart) {
		this.saleDateStart = saleDateStart;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getMaterialGroupId() {
		return materialGroupId;
	}

	public void setMaterialGroupId(String materialGroupId) {
		this.materialGroupId = materialGroupId;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public String getCodeDesc() {
		return codeDesc;
	}

	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}

	public String getCarCharactor() {
		return carCharactor;
	}

	public void setCarCharactor(String carCharactor) {
		this.carCharactor = carCharactor;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupLevel() {
		return groupLevel;
	}

	public void setGroupLevel(String groupLevel) {
		this.groupLevel = groupLevel;
	}

	public String getParentGroupId() {
		return parentGroupId;
	}

	public void setParentGroupId(String parentGroupId) {
		this.parentGroupId = parentGroupId;
	}
	
	public String getParentGroupName() {
		return parentGroupName;
	}

	public void setParentGroupName(String parentGroupName) {
		this.parentGroupName = parentGroupName;
	}

	public String getLabourId() {
		return labourId;
	}

	public void setLabourId(String labourId) {
		this.labourId = labourId;
	}

	public String getLabourCode() {
		return labourCode;
	}

	public void setLabourCode(String labourCode) {
		this.labourCode = labourCode;
	}

	public String getCnDes() {
		return cnDes;
	}

	public void setCnDes(String cnDes) {
		this.cnDes = cnDes;
	}

	public String getLabourHour() {
		return labourHour;
	}

	public void setLabourHour(String labourHour) {
		this.labourHour = labourHour;
	}

	public String getDealerId() {
		return dealerId;
	}

	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getCarStatus() {
		return carStatus;
	}

	public void setCarStatus(String carStatus) {
		this.carStatus = carStatus;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getInAmount() {
		return inAmount;
	}

	public void setInAmount(String inAmount) {
		this.inAmount = inAmount;
	}

	public String getBn() {
		return bn;
	}

	public void setBn(String bn) {
		this.bn = bn;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}

	public String getPers() {
		return pers;
	}

	public void setPers(String pers) {
		this.pers = pers;
	}

	public String getCarYieldly() {
		return carYieldly;
	}

	public void setCarYieldly(String carYieldly) {
		this.carYieldly = carYieldly;
	}

	public String getVehicleArea() {
		return vehicleArea;
	}

	public void setVehicleArea(String vehicleArea) {
		this.vehicleArea = vehicleArea;
	}

	public Integer getIsSpec() {
		return isSpec;
	}

	public void setIsSpec(Integer isSpec) {
		this.isSpec = isSpec;
	}

	public Integer getFore() {
		return fore;
	}

	public void setFore(Integer fore) {
		this.fore = fore;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getIsGua() {
		return isGua;
	}

	public void setIsGua(Integer isGua) {
		this.isGua = isGua;
	}

	public String getSubjectId()
	{
		return subjectId;
	}

	public void setSubjectId(String subjectId)
	{
		this.subjectId = subjectId;
	}

	public String getSubject_no()
	{
		return subject_no;
	}

	public void setSubject_no(String subject_no)
	{
		this.subject_no = subject_no;
	}

	public String getSubject_name()
	{
		return subject_name;
	}

	public void setSubject_name(String subject_name)
	{
		this.subject_name = subject_name;
	}

	public String getNew_id()
	{
		return new_id;
	}

	public void setNew_id(String new_id)
	{
		this.new_id = new_id;
	}

	public String getSumcar()
	{
		return sumcar;
	}

	public void setSumcar(String sumcar)
	{
		this.sumcar = sumcar;
	}

	public String getFsumcar()
	{
		return fsumcar;
	}

	public void setFsumcar(String fsumcar)
	{
		this.fsumcar = fsumcar;
	}

	public String getVehicle_num()
	{
		return vehicle_num;
	}

	public void setVehicle_num(String vehicle_num)
	{
		this.vehicle_num = vehicle_num;
	}

	public String getDuty_person()
	{
		return duty_person;
	}

	public void setDuty_person(String duty_person)
	{
		this.duty_person = duty_person;
	}

	public String getSubject_start_date()
	{
		return subject_start_date;
	}

	public void setSubject_start_date(String subject_start_date)
	{
		this.subject_start_date = subject_start_date;
	}

	public String getSubject_end_date()
	{
		return subject_end_date;
	}

	public void setSubject_end_date(String subject_end_date)
	{
		this.subject_end_date = subject_end_date;
	}

	public String getOn_amount()
	{
		return on_amount;
	}

	public void setOn_amount(String on_amount)
	{
		this.on_amount = on_amount;
	}

	public String getOn_camount()
	{
		return on_camount;
	}

	public void setOn_camount(String on_camount)
	{
		this.on_camount = on_camount;
	}

	public String getEvaluate()
	{
		return evaluate;
	}

	public void setEvaluate(String evaluate)
	{
		this.evaluate = evaluate;
	}

	public String getMeasures()
	{
		return measures;
	}

	public void setMeasures(String measures)
	{
		this.measures = measures;
	}

	public String getActivity_code()
	{
		return activity_code;
	}

	public void setActivity_code(String activity_code)
	{
		this.activity_code = activity_code;
	}

	public String getActivity_name()
	{
		return activity_name;
	}

	public void setActivity_name(String activity_name)
	{
		this.activity_name = activity_name;
	}

	public String getPart_num()
	{
		return part_num;
	}

	public void setPart_num(String part_num)
	{
		this.part_num = part_num;
	}

	public String getPart_num_w()
	{
		return part_num_w;
	}

	public void setPart_num_w(String part_num_w)
	{
		this.part_num_w = part_num_w;
	}

	public String getEvaluateid()
	{
		return evaluateid;
	}

	public void setEvaluateid(String evaluateid)
	{
		this.evaluateid = evaluateid;
	}

	public String getActivity_num() {
		return activity_num;
	}

	public void setActivity_num(String activity_num) {
		this.activity_num = activity_num;
	}

	public String getTow_type_activity() {
		return tow_type_activity;
	}

	public void setTow_type_activity(String tow_type_activity) {
		this.tow_type_activity = tow_type_activity;
	}

	public String getLabour() {
		return labour;
	}

	public void setLabour(String labour) {
		this.labour = labour;
	}

	public String getMalFunction() {
		return malFunction;
	}

	public void setMalFunction(String malFunction) {
		this.malFunction = malFunction;
	}

	public Long getMalId() {
		return malId;
	}

	public void setMalId(Long malId) {
		this.malId = malId;
	}

	public String getHasPart() {
		return hasPart;
	}

	public void setHasPart(String hasPart) {
		this.hasPart = hasPart;
	}

	public String getPratResponsDesc() {
		return pratResponsDesc;
	}

	public void setPratResponsDesc(String pratResponsDesc) {
		this.pratResponsDesc = pratResponsDesc;
	}

	public Integer getPratResponsId() {
		return pratResponsId;
	}

	public void setPratResponsId(Integer pratResponsId) {
		this.pratResponsId = pratResponsId;
	}

	public Long getRealPartId() {
		return realPartId;
	}

	public void setRealPartId(Long realPartId) {
		this.realPartId = realPartId;
	}

	

	public String getPull_in_mean() {
		return pull_in_mean;
	}

	public void setPull_in_mean(String pull_in_mean) {
		this.pull_in_mean = pull_in_mean;
	}

	public String getPull_in_region() {
		return pull_in_region;
	}

	public void setPull_in_region(String pull_in_region) {
		this.pull_in_region = pull_in_region;
	}

	public String getPull_in_incre() {
		return pull_in_incre;
	}

	public void setPull_in_incre(String pull_in_incre) {
		this.pull_in_incre = pull_in_incre;
	}

	

	public String getCustomer_mean() {
		return customer_mean;
	}

	public void setCustomer_mean(String customer_mean) {
		this.customer_mean = customer_mean;
	}

	public String getCustomer_region() {
		return customer_region;
	}

	public void setCustomer_region(String customer_region) {
		this.customer_region = customer_region;
	}

	public String getCustomer_incre() {
		return customer_incre;
	}

	public void setCustomer_incre(String customer_incre) {
		this.customer_incre = customer_incre;
	}

	

	public String getPrice_mean() {
		return price_mean;
	}

	public void setPrice_mean(String price_mean) {
		this.price_mean = price_mean;
	}

	public String getPrice_region() {
		return price_region;
	}

	public void setPrice_region(String price_region) {
		this.price_region = price_region;
	}

	public String getPrice_incre() {
		return price_incre;
	}

	public void setPrice_incre(String price_incre) {
		this.price_incre = price_incre;
	}

	
	public String getPull_in_num() {
		return pull_in_num;
	}

	public void setPull_in_num(String pull_in_num) {
		this.pull_in_num = pull_in_num;
	}

	public String getCustomer_num() {
		return customer_num;
	}

	public void setCustomer_num(String customer_num) {
		this.customer_num = customer_num;
	}

	public String getPrice_num() {
		return price_num;
	}

	public void setPrice_num(String price_num) {
		this.price_num = price_num;
	}

	public String getOpen_num() {
		return open_num;
	}

	public void setOpen_num(String open_num) {
		this.open_num = open_num;
	}

	public String getOpen_mean() {
		return open_mean;
	}

	public void setOpen_mean(String open_mean) {
		this.open_mean = open_mean;
	}

	public String getOpen_region() {
		return open_region;
	}

	public void setOpen_region(String open_region) {
		this.open_region = open_region;
	}

	public String getOpen_incre() {
		return open_incre;
	}

	public void setOpen_incre(String open_incre) {
		this.open_incre = open_incre;
	}

	public String getFactstartdate() {
		return factstartdate;
	}

	public void setFactstartdate(String factstartdate) {
		this.factstartdate = factstartdate;
	}

	public String getFactenddate() {
		return factenddate;
	}

	public void setFactenddate(String factenddate) {
		this.factenddate = factenddate;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public String getMainPartCode() {
		return mainPartCode;
	}

	public void setMainPartCode(String mainPartCode) {
		this.mainPartCode = mainPartCode;
	}

	public String getMainPartName() {
		return mainPartName;
	}

	public void setMainPartName(String mainPartName) {
		this.mainPartName = mainPartName;
	}

	public String getTroubleDesc() {
		return troubleDesc;
	}

	public void setTroubleDesc(String troubleDesc) {
		this.troubleDesc = troubleDesc;
	}

	public String getTroubleReason() {
		return troubleReason;
	}

	public void setTroubleReason(String troubleReason) {
		this.troubleReason = troubleReason;
	}

	public String getRepairMethod() {
		return repairMethod;
	}

	public void setRepairMethod(String repairMethod) {
		this.repairMethod = repairMethod;
	}

	public String getAppRemark() {
		return appRemark;
	}

	public void setAppRemark(String appRemark) {
		this.appRemark = appRemark;
	}

	public Double getLaborFee() {
		return laborFee;
	}

	public void setLaborFee(Double laborFee) {
		this.laborFee = laborFee;
	}
}
