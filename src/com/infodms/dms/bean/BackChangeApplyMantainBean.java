/**********************************************************************
* <pre>
* FILE : BackChangeApplyMantainBean.java
* CLASS : BackChangeApplyMantainBean
* 
* AUTHOR : WangJinBao
*
* FUNCTION : 退换车申请书维护查询Bean.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-17| WangJinBao  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

/**
 * Function       :  退换车申请书查询明细Bean
 * @author        :  wangjinbao
 * CreateDate     :  2010-05-17
 * @version       :  0.1
 */
public class BackChangeApplyMantainBean extends PO{
	private String orderId;                     //工单号
	private String dealerCode;                 //申请单位代码
	private String dealerName;                 //申请单位名称
	private String linkManager;                 //服务中心经理
	private String linkMan;                     //服务中心经办人
	private String exType;                     //退换类型
	private String vin;                         //车辆识别码（VIN）
	private String model;                       //车型
	private String groupName;                   //车系  
	private String engineNo;                       //发动机号
	private String productionDate;            //出厂日期
	private String sellTime;                 //购车日期
	private String mileage;                //行驶里程
	private String customerName;          //客户姓名
	private String curtPhone;            //客户联系电话
	private String curtAddress;            //客户联系地址
	private String createDate;                 //创建时间
	
	private String exStatus;                   //工单状态 
	private String auditDate;                     //审批时间
	private String auditBy;          //审批人员
	private String deptName;             //人员部门
	private String auditContent;            //审批意见
	private String problemDescribe;
	private String userRequest;
	private String adviceDealMode;
	private String costDetail;
	private Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProblemDescribe() {
		return problemDescribe;
	}
	public void setProblemDescribe(String problemDescribe) {
		this.problemDescribe = problemDescribe;
	}
	public String getUserRequest() {
		return userRequest;
	}
	public void setUserRequest(String userRequest) {
		this.userRequest = userRequest;
	}
	public String getAdviceDealMode() {
		return adviceDealMode;
	}
	public void setAdviceDealMode(String adviceDealMode) {
		this.adviceDealMode = adviceDealMode;
	}
	public String getCostDetail() {
		return costDetail;
	}
	public void setCostDetail(String costDetail) {
		this.costDetail = costDetail;
	}
	/**
	 * @return createDate
	 */
	public String getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate 要设置的 createDate
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return exStatus
	 */
	public String getExStatus() {
		return exStatus;
	}
	/**
	 * @param exStatus 要设置的 exStatus
	 */
	public void setExStatus(String exStatus) {
		this.exStatus = exStatus;
	}
	/**
	 * @return exType
	 */
	public String getExType() {
		return exType;
	}
	/**
	 * @param exType 要设置的 exType
	 */
	public void setExType(String exType) {
		this.exType = exType;
	}
	/**
	 * @return model
	 */
	public String getModel() {
		return model;
	}
	/**
	 * @param model 要设置的 model
	 */
	public void setModel(String model) {
		this.model = model;
	}
	/**
	 * @return orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId 要设置的 orderId
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return vin
	 */
	public String getVin() {
		return vin;
	}
	/**
	 * @param vin 要设置的 vin
	 */
	public void setVin(String vin) {
		this.vin = vin;
	}
	/**
	 * @return auditBy
	 */
	public String getAuditBy() {
		return auditBy;
	}
	/**
	 * @param auditBy 要设置的 auditBy
	 */
	public void setAuditBy(String auditBy) {
		this.auditBy = auditBy;
	}
	/**
	 * @return auditContent
	 */
	public String getAuditContent() {
		return auditContent;
	}
	/**
	 * @param auditContent 要设置的 auditContent
	 */
	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}
	/**
	 * @return auditDate
	 */
	public String getAuditDate() {
		return auditDate;
	}
	/**
	 * @param auditDate 要设置的 auditDate
	 */
	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}
	/**
	 * @return curtAddress
	 */
	public String getCurtAddress() {
		return curtAddress;
	}
	/**
	 * @param curtAddress 要设置的 curtAddress
	 */
	public void setCurtAddress(String curtAddress) {
		this.curtAddress = curtAddress;
	}
	/**
	 * @return curtPhone
	 */
	public String getCurtPhone() {
		return curtPhone;
	}
	/**
	 * @param curtPhone 要设置的 curtPhone
	 */
	public void setCurtPhone(String curtPhone) {
		this.curtPhone = curtPhone;
	}
	/**
	 * @return customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName 要设置的 customerName
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return dealerCode
	 */
	public String getDealerCode() {
		return dealerCode;
	}
	/**
	 * @param dealerCode 要设置的 dealerCode
	 */
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	/**
	 * @return dealerName
	 */
	public String getDealerName() {
		return dealerName;
	}
	/**
	 * @param dealerName 要设置的 dealerName
	 */
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	/**
	 * @return deptName
	 */
	public String getDeptName() {
		return deptName;
	}
	/**
	 * @param deptName 要设置的 deptName
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	/**
	 * @return engineNo
	 */
	public String getEngineNo() {
		return engineNo;
	}
	/**
	 * @param engineNo 要设置的 engineNo
	 */
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	/**
	 * @return linkMan
	 */
	public String getLinkMan() {
		return linkMan;
	}
	/**
	 * @param linkMan 要设置的 linkMan
	 */
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	/**
	 * @return linkManager
	 */
	public String getLinkManager() {
		return linkManager;
	}
	/**
	 * @param linkManager 要设置的 linkManager
	 */
	public void setLinkManager(String linkManager) {
		this.linkManager = linkManager;
	}
	/**
	 * @return mileage
	 */
	public String getMileage() {
		return mileage;
	}
	/**
	 * @param mileage 要设置的 mileage
	 */
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	/**
	 * @return productionDate
	 */
	public String getProductionDate() {
		return productionDate;
	}
	/**
	 * @param productionDate 要设置的 productionDate
	 */
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	/**
	 * @return sellTime
	 */
	public String getSellTime() {
		return sellTime;
	}
	/**
	 * @param sellTime 要设置的 sellTime
	 */
	public void setSellTime(String sellTime) {
		this.sellTime = sellTime;
	}
	/**
	 * @return groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * @param groupName 要设置的 groupName
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
	
	


}
