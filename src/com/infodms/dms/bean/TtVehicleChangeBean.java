package com.infodms.dms.bean;

import java.util.Date;

public class TtVehicleChangeBean {
	private Integer applyType;
	private String applyRemark;
	private String vin;
	private Date updateDate;
	private Long createBy; //如果是经销商 dealerId  总部 userId
	private Long claimTacticsId;
	private Date applyDate;
	private String applyStartDate;//提报开始日期
	private String applyEndDate;//提报结束日期
	//private Long dealerId;//创建经销商
	private Date checkDate;
	private Integer status;
	private Long applyPerson;
	private String applyData;
	private Integer isDel;
	private Long ctmId;
	private Date purchasedDate;
	private Long modelId;
	private Long updateBy;
	private String checkRemark;
	private Double mileage;
	private Long seriesId;
	private Long id;
	private Integer freeTimes;
	private String engineNo;
	private String yieldly;
	private Long errorDealerId;
	private Long checkPerson;
	private String vehicleNo;
	private Integer ver;
	private Date createDate;
	private Integer errorType;
	private String checkQueryStatus;//车辆变更审核查询标志  只在审核页面有值

	public void setApplyType(Integer applyType){
		this.applyType=applyType;
	}

	public Integer getApplyType(){
		return this.applyType;
	}

	public void setApplyRemark(String applyRemark){
		this.applyRemark=applyRemark;
	}

	public String getApplyRemark(){
		return this.applyRemark;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setClaimTacticsId(Long claimTacticsId){
		this.claimTacticsId=claimTacticsId;
	}

	public Long getClaimTacticsId(){
		return this.claimTacticsId;
	}

	public void setApplyDate(Date applyDate){
		this.applyDate=applyDate;
	}

	public Date getApplyDate(){
		return this.applyDate;
	}

	public void setCheckDate(Date checkDate){
		this.checkDate=checkDate;
	}

	public Date getCheckDate(){
		return this.checkDate;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setApplyPerson(Long applyPerson){
		this.applyPerson=applyPerson;
	}

	public Long getApplyPerson(){
		return this.applyPerson;
	}

	public void setApplyData(String applyData){
		this.applyData=applyData;
	}

	public String getApplyData(){
		return this.applyData;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setCtmId(Long ctmId){
		this.ctmId=ctmId;
	}

	public Long getCtmId(){
		return this.ctmId;
	}

	public void setPurchasedDate(Date purchasedDate){
		this.purchasedDate=purchasedDate;
	}

	public Date getPurchasedDate(){
		return this.purchasedDate;
	}

	public void setModelId(Long modelId){
		this.modelId=modelId;
	}

	public Long getModelId(){
		return this.modelId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCheckRemark(String checkRemark){
		this.checkRemark=checkRemark;
	}

	public String getCheckRemark(){
		return this.checkRemark;
	}

	public void setMileage(Double mileage){
		this.mileage=mileage;
	}

	public Double getMileage(){
		return this.mileage;
	}

	public void setSeriesId(Long seriesId){
		this.seriesId=seriesId;
	}

	public Long getSeriesId(){
		return this.seriesId;
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

	public void setEngineNo(String engineNo){
		this.engineNo=engineNo;
	}

	public String getEngineNo(){
		return this.engineNo;
	}

	public void setYieldly(String yieldly){
		this.yieldly=yieldly;
	}

	public String getYieldly(){
		return this.yieldly;
	}

	public void setErrorDealerId(Long errorDealerId){
		this.errorDealerId=errorDealerId;
	}

	public Long getErrorDealerId(){
		return this.errorDealerId;
	}

	public void setCheckPerson(Long checkPerson){
		this.checkPerson=checkPerson;
	}

	public Long getCheckPerson(){
		return this.checkPerson;
	}

	public void setVehicleNo(String vehicleNo){
		this.vehicleNo=vehicleNo;
	}

	public String getVehicleNo(){
		return this.vehicleNo;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setErrorType(Integer errorType){
		this.errorType=errorType;
	}

	public Integer getErrorType(){
		return this.errorType;
	}

	public String getApplyStartDate() {
		return applyStartDate;
	}

	public void setApplyStartDate(String applyStartDate) {
		this.applyStartDate = applyStartDate;
	}

	public String getApplyEndDate() {
		return applyEndDate;
	}

	public void setApplyEndDate(String applyEndDate) {
		this.applyEndDate = applyEndDate;
	}

//	public Long getDealerId() {
//		return dealerId;
//	}
//
//	public void setDealerId(Long dealerId) {
//		this.dealerId = dealerId;
//	}

	public String getCheckQueryStatus() {
		return checkQueryStatus;
	}

	public void setCheckQueryStatus(String checkQueryStatus) {
		this.checkQueryStatus = checkQueryStatus;
	}

}
