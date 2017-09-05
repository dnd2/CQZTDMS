package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

/**
 * @author PGM
 * 奖惩审批查询(经销商端)
 */
public class PunishmentApplyDealerMantainBean extends PO {
	private static final long serialVersionUID = -3418889889282261795L;
	private String dealerId;
	private String dealerCode;   //服务中心代码
	private String dealerName;   //服务中心名称
	private String orderId;       //工单号
	private String linkMan;          //申请单位(当前登录人)
	private String rewardType;    //类型
	private String createDate;      //创建时间
	private String rewardStatus; //工单状态
	private String tel;//联系电话
	private String rewardMode;//奖惩方式
	private String rewardMoney;//奖惩金额
	private String rewardDate;//申请日期
	private String rewardContent;//申请内容
	private String auditDate;//审批时间
	private String auditBy;//审核人
	private String auditStatus;//审批状态
	private String auditContent;//审批意见
	private String name;//提报人员
    private String beginTime;//开始时间
	private String endTime;//结束时间
	private String userId;//ID
	
	public String getDealerId() {
		return dealerId;
	}
	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}
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
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getRewardType() {
		return rewardType;
	}
	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getRewardStatus() {
		return rewardStatus;
	}
	public void setRewardStatus(String rewardStatus) {
		this.rewardStatus = rewardStatus;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getRewardMode() {
		return rewardMode;
	}
	public void setRewardMode(String rewardMode) {
		this.rewardMode = rewardMode;
	}
	public String getRewardMoney() {
		return rewardMoney;
	}
	public void setRewardMoney(String rewardMoney) {
		this.rewardMoney = rewardMoney;
	}
	public String getRewardDate() {
		return rewardDate;
	}
	public void setRewardDate(String rewardDate) {
		this.rewardDate = rewardDate;
	}
	public String getRewardContent() {
		return rewardContent;
	}
	public void setRewardContent(String rewardContent) {
		this.rewardContent = rewardContent;
	}
	public String getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}
	public String getAuditBy() {
		return auditBy;
	}
	public void setAuditBy(String auditBy) {
		this.auditBy = auditBy;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditContent() {
		return auditContent;
	}
	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
