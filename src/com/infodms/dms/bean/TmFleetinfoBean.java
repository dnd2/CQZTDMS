/**********************************************************************
 * <pre>
 * FILE : TmFleetinfoBean.java
 * CLASS : TmFleetinfoBean
 *
 * AUTHOR : zzg
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2009-12-22|zzg| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.bean;

import java.util.Date;

public class TmFleetinfoBean {
	
	private Long fleetId;//批售客户ID
	private Long brandId;//品牌id
	private String brandCode;//品牌code
	private String brandName;//品牌名称
	//批售支持申请
	private String promotionCode;//批售价格优惠编号
	private String applyUpdate;//申请日期
	private Long  promotionType;//申请类型
	private Long  isConfirm;//确认
	private Long price;
	private Long  modelId;//车型id
	private Long  preferentialApplyId;//申请类型
	private Long  applyType;//申请类型
	private Long  applyAmount;//申请总数量
	private String  applyAmountInit;//申请总数量（库存/订单）
	private Long  reportAmount;//已申报数量
	private Long  promotionId;//批售支持申请id
	private String  promotionTypeName;//申请类型名称
	private String  statusName;//状态名称
	private String  auditDate;//审批日期
	private String  remark;
	private String  orderNo;
	private Double preferentialPoint;
	private Integer preferentialAmount;
	private String modelCode;//车型代码
	private String modelName;//车型名称
	private String  invoiceNo;
	private String  statusinvdate;
	private String  vin;
	private String  customerCode;
	private String  approveRemark;
	private String  flagUsde;
	private Integer  channel;
	private Long  preferentialApplyDetailId;//批售支持申请id
	private Long  confirmQuantity ; 
	private Long  applyQuantity ; 
 	private Long  confirmAmount;
 	private String  confirmAmountInit;//通过数量(库存/订单)
	//批售支持申请
	private String orgParentName;//地区
	private String codeName;//系统字典名称
	private String orgName;//代理商名称
	private String orgCode;//代理商代码
	private String fleetName;//客户名称
	private String flag;//是否可以提报标识
	private String phone;//客户电话
	private Integer vocation;//行业分类
	private String vocationName;//行业分类名称
	private Integer profession;//职业
	private String mainLinkman;//主要联系人
	private String mainBusiness;//主营业务
	private String personalSize;//人员规模
	private String fundSize;//资金规模
	private Integer buyCarUse;//购车用途
	private String email;//Email 
	private String address;//详细地址 
	private String post;//邮编
	private String reportUpdate;//上报日期
	private Integer status;//确认状态
	private Date createDate;//创建日期
	private Date updateDate;//更新日期
	private Long updateUser;//更新人
	private Long createUser;//创建人
	
	
	
	private String customerName;
	private String className;
	private String linkmanName;
	
	private String customerAdress;
	private String customerEmail;
	private Long customerType;
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	private Long fleetProvince;
	private Long fleetCity;
	private Long fleetCounty;
	private String fleetWwwAddress;
	private String fleetFix;
	private Integer linkmanSex;
	
	
	private String dealerName;
	
	
	
	public Long getFleetProvince() {
		return fleetProvince;
	}
	public void setFleetProvince(Long fleetProvince) {
		this.fleetProvince = fleetProvince;
	}
	public Long getFleetCity() {
		return fleetCity;
	}
	public void setFleetCity(Long fleetCity) {
		this.fleetCity = fleetCity;
	}
	public Long getFleetCounty() {
		return fleetCounty;
	}
	public void setFleetCounty(Long fleetCounty) {
		this.fleetCounty = fleetCounty;
	}
	public String getFleetWwwAddress() {
		return fleetWwwAddress;
	}
	public void setFleetWwwAddress(String fleetWwwAddress) {
		this.fleetWwwAddress = fleetWwwAddress;
	}
	public String getFleetFix() {
		return fleetFix;
	}
	public void setFleetFix(String fleetFix) {
		this.fleetFix = fleetFix;
	}
	public Integer getLinkmanSex() {
		return linkmanSex;
	}
	public void setLinkmanSex(Integer linkmanSex) {
		this.linkmanSex = linkmanSex;
	}
	public String getMainBusiness() {
		return mainBusiness;
	}
	public void setMainBusiness(String mainBusiness) {
		this.mainBusiness = mainBusiness;
	}
	public String getPersonalSize() {
		return personalSize;
	}
	public void setPersonalSize(String personalSize) {
		this.personalSize = personalSize;
	}
	public String getFundSize() {
		return fundSize;
	}
	public void setFundSize(String fundSize) {
		this.fundSize = fundSize;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getLinkmanName() {
		return linkmanName;
	}
	public void setLinkmanName(String linkmanName) {
		this.linkmanName = linkmanName;
	}
	public String getCustomerAdress() {
		return customerAdress;
	}
	public void setCustomerAdress(String customerAdress) {
		this.customerAdress = customerAdress;
	}
	public String getCustomerEmail() {
		return customerEmail;
	}
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	public Long getCustomerType() {
		return customerType;
	}
	public void setCustomerType(Long customerType) {
		this.customerType = customerType;
	}
	public Long getFleetId() {
		return fleetId;
	}
	public void setFleetId(Long fleetId) {
		this.fleetId = fleetId;
	}
	public String getFleetName() {
		return fleetName;
	}
	public void setFleetName(String fleetName) {
		this.fleetName = fleetName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getVocation() {
		return vocation;
	}
	public void setVocation(Integer vocation) {
		this.vocation = vocation;
	}
	public Integer getProfession() {
		return profession;
	}
	public void setProfession(Integer profession) {
		this.profession = profession;
	}
	public String getMainLinkman() {
		return mainLinkman;
	}
	public void setMainLinkman(String mainLinkman) {
		this.mainLinkman = mainLinkman;
	}
	public Integer getBuyCarUse() {
		return buyCarUse;
	}
	public void setBuyCarUse(Integer buyCarUse) {
		this.buyCarUse = buyCarUse;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Long getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}
	public Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getVocationName() {
		return vocationName;
	}
	public void setVocationName(String vocationName) {
		this.vocationName = vocationName;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getReportUpdate() {
		return reportUpdate;
	}
	public void setReportUpdate(String reportUpdate) {
		this.reportUpdate = reportUpdate;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getPromotionCode() {
		return promotionCode;
	}
	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}
	public String getApplyUpdate() {
		return applyUpdate;
	}
	public void setApplyUpdate(String applyUpdate) {
		this.applyUpdate = applyUpdate;
	}
	public String getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}
	public Long getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(Long promotionType) {
		this.promotionType = promotionType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPromotionTypeName() {
		return promotionTypeName;
	}
	public void setPromotionTypeName(String promotionTypeName) {
		this.promotionTypeName = promotionTypeName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getOrgParentName() {
		return orgParentName;
	}
	public void setOrgParentName(String orgParentName) {
		this.orgParentName = orgParentName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public Long getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public Long getApplyAmount() {
		return applyAmount;
	}
	public void setApplyAmount(Long applyAmount) {
		this.applyAmount = applyAmount;
	}
	public Long getReportAmount() {
		return reportAmount;
	}
	public void setReportAmount(Long reportAmount) {
		this.reportAmount = reportAmount;
	}
	public Long getApplyType() {
		return applyType;
	}
	public void setApplyType(Long applyType) {
		this.applyType = applyType;
	}
	public Long getPreferentialApplyId() {
		return preferentialApplyId;
	}
	public void setPreferentialApplyId(Long preferentialApplyId) {
		this.preferentialApplyId = preferentialApplyId;
	}
	public Double getPreferentialPoint() {
		return preferentialPoint;
	}
	public void setPreferentialPoint(Double preferentialPoint) {
		this.preferentialPoint = preferentialPoint;
	}
	public Integer getPreferentialAmount() {
		return preferentialAmount;
	}
	public void setPreferentialAmount(Integer preferentialAmount) {
		this.preferentialAmount = preferentialAmount;
	}
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getStatusinvdate() {
		return statusinvdate;
	}
	public void setStatusinvdate(String statusinvdate) {
		this.statusinvdate = statusinvdate;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	public Long getPreferentialApplyDetailId() {
		return preferentialApplyDetailId;
	}
	public void setPreferentialApplyDetailId(Long preferentialApplyDetailId) {
		this.preferentialApplyDetailId = preferentialApplyDetailId;
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
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public Long getIsConfirm() {
		return isConfirm;
	}
	public void setIsConfirm(Long isConfirm) {
		this.isConfirm = isConfirm;
	}
	public Long getConfirmQuantity() {
		return confirmQuantity;
	}
	public void setConfirmQuantity(Long confirmQuantity) {
		this.confirmQuantity = confirmQuantity;
	}
	public Long getConfirmAmount() {
		return confirmAmount;
	}
	public void setConfirmAmount(Long confirmAmount) {
		this.confirmAmount = confirmAmount;
	}
	public String getApproveRemark() {
		return approveRemark;
	}
	public void setApproveRemark(String approveRemark) {
		this.approveRemark = approveRemark;
	}
	public Long getApplyQuantity() {
		return applyQuantity;
	}
	public void setApplyQuantity(Long applyQuantity) {
		this.applyQuantity = applyQuantity;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getFlagUsde() {
		return flagUsde;
	}
	public void setFlagUsde(String flagUsde) {
		this.flagUsde = flagUsde;
	}
	public String getApplyAmountInit() {
		return applyAmountInit;
	}
	public void setApplyAmountInit(String applyAmountInit) {
		this.applyAmountInit = applyAmountInit;
	}
	public String getConfirmAmountInit() {
		return confirmAmountInit;
	}
	public void setConfirmAmountInit(String confirmAmountInit) {
		this.confirmAmountInit = confirmAmountInit;
	}
}
