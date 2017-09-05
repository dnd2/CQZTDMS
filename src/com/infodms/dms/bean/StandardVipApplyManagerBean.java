package com.infodms.dms.bean;
/**
 * @Title: 
 *
 * @Description:InfoFrame3.0.V01
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-17
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class StandardVipApplyManagerBean {
	private Long  id;
	private String updateDate;    //修改时间
	private String createBy;      //创建人
	private String orderId;       //申请单号
	private String createDate;    //创建时间
	private String dealerId;	  //服务中心ID
	private String zipCode;		  //邮编
	private String stContent;     //申请内容
	private String linkMan;		  //联系人
	private String stStatus;      //申请状态
	private String updateBy;	  //修改人
	private String vin;			  //车辆识别码
	private String stType;	      //申请类型   合格证或VIP
	private String stDate;		  //申请提报时间
	private String tel;			  //电话
	private String stAction;	  //申请动作
	private String address;		  //地址
	private String isDel;		  //删除标示
	private Long   companyId;
	private String beginTime;	  //创建开始时间
	private String endTime;       //创建结束时间
	private String bbeginTime;	  //创建开始时间
	private String bendTime;       //创建结束时间
	private String pbeginTime;	  //创建开始时间
	private String pendTime;       //创建结束时间
	private String vehicleModel;  //车系
	
	private String orgId;		  //组织ID
	private String customerName;  //车主姓名
	
	private String dealerName;	  //经销商名称
	private String dealerCode;	  //经销商代码
	private String areaName;
	private String seriesName;
	private String modelName;
	private String feeType;
	private String freeTims;
	private String repairType;
	private String campaignCode;
	private String campaignName;
	private String claType;
	private String groupCode;
	private String province;
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getClaType() {
		return claType;
	}
	public void setClaType(String claType) {
		this.claType = claType;
	}
	public String getBbeginTime() {
		return bbeginTime;
	}
	public void setBbeginTime(String bbeginTime) {
		this.bbeginTime = bbeginTime;
	}
	public String getBendTime() {
		return bendTime;
	}
	public void setBendTime(String bendTime) {
		this.bendTime = bendTime;
	}
	public String getPbeginTime() {
		return pbeginTime;
	}
	public void setPbeginTime(String pbeginTime) {
		this.pbeginTime = pbeginTime;
	}
	public String getPendTime() {
		return pendTime;
	}
	public void setPendTime(String pendTime) {
		this.pendTime = pendTime;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getCampaignCode() {
		return campaignCode;
	}
	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public String getFreeTims() {
		return freeTims;
	}
	public void setFreeTims(String freeTims) {
		this.freeTims = freeTims;
	}
	public String getRepairType() {
		return repairType;
	}
	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}
	//modify by xiayanpeng begin 加入SESSION
	private AclUserBean logonUser;
	private String dutyType;
	//modify by xiayanpeng end 
	
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getVehicleModel() {
		return vehicleModel;
	}
	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getDealerId() {
		return dealerId;
	}
	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getStContent() {
		return stContent;
	}
	public void setStContent(String stContent) {
		this.stContent = stContent;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getStStatus() {
		return stStatus;
	}
	public void setStStatus(String stStatus) {
		this.stStatus = stStatus;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getStType() {
		return stType;
	}
	public void setStType(String stType) {
		this.stType = stType;
	}
	public String getStDate() {
		return stDate;
	}
	public void setStDate(String stDate) {
		this.stDate = stDate;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getStAction() {
		return stAction;
	}
	public void setStAction(String stAction) {
		this.stAction = stAction;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIsDel() {
		return isDel;
	}
	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public AclUserBean getLogonUser() {
		return logonUser;
	}
	public void setLogonUser(AclUserBean logonUser) {
		this.logonUser = logonUser;
	}
	public String getDutyType() {
		return dutyType;
	}
	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
}
