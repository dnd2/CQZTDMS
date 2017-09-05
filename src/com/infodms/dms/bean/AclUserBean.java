package com.infodms.dms.bean;

import com.infodms.dms.common.Constant;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.component.acl.AclUser;

public class AclUserBean extends AclUser{
	private static final long serialVersionUID = 1L;
	/**
	 * 用户所属公司ID
	 */
	private Long companyId;
	
	/**
	 * 用户类型
	 */
	private Integer userType;
	//组织ID
	private Long orgId;
	private String addr;//传入一个状态大家不到当地址用
	private String actn;
	//组织类型
	private Integer orgType;
	//经销商ID
	private String dealerId;
	//经销商代码
	private String dealerCode;
	//车厂用户业务范围
	private String oemPositionArea;
	/**
	 * 用户ID
	 */
	private Long userId;
	
	/**
	 * 用户当前登录的职位ID
	 */
	private Long poseId;
	
	//职位类型
	private Integer poseType;
	//职位业务类别
	private Integer poseBusType;
	
	//经销商OEM_COMPANY_ID
	private String oemCompanyId;
	
	//经销商DEALER_ORG_ID
	private String  dealerOrgId;
	//分机号
	private String  txtExt;
	
	//经销商与供应商关联
	private Long venderId;
	public Long getVenderId() {
		return venderId;
	}

	public void setVenderId(Long venderId) {
		this.venderId = venderId;
	}

	//经销商类型
	private Integer dealerType;
	
	//组织DUTY_TYPE
	private String dutyType;
	
	//上级组织PARENT_ORG_ID
	private String parentOrgId;
	
	//公司code
	private String companyCode;
	//下端登录id
	private String rpcUserId;
	//接口请求标志
	private String rpcFlag;
	//接口用户
	private String rpcName;
	private String rpcStat;
	private Integer chooseDealer;
	public Integer getChooseDealer() {
		return chooseDealer;
	}

	public void setChooseDealer(Integer chooseDealer) {
		this.chooseDealer = chooseDealer;
	}

	public String getOemCompanyId()
	{
		return oemCompanyId;
	}

	public void setOemCompanyId(String oemCompanyId)
	{
		this.oemCompanyId = oemCompanyId;
	}

	public String getDealerOrgId()
	{
		return dealerOrgId;
	}

	public void setDealerOrgId(String dealerOrgId)
	{
		this.dealerOrgId = dealerOrgId;
	}

	public String getDutyType()
	{
		return dutyType;
	}
	
	public String getDutyType(String dealerId)
	{
		if (CommonUtils.isEmpty(dealerId)) {
			return dutyType;
		} else {
			return Constant.DUTY_TYPE_DEALER+"";
		}
		
	}

	public void setDutyType(String dutyType)
	{
		this.dutyType = dutyType;
	}

	public String getParentOrgId()
	{
		return parentOrgId;
	}

	public void setParentOrgId(String parentOrgId)
	{
		this.parentOrgId = parentOrgId;
	}

	public Integer getPoseType()
	{
		return poseType;
	}

	public void setPoseType(Integer poseType)
	{
		this.poseType = poseType;
	}

	public Integer getPoseBusType()
	{
		return poseBusType;
	}

	public void setPoseBusType(Integer poseBusType)
	{
		this.poseBusType = poseBusType;
	}

	/**
	 * 当前登录用户所在部门，以及下级部门
	 */
	private String bxjDept;
	
	public String getBxjDept() {
		return bxjDept;
	}

	public void setBxjDept(String bxjDept) {
		this.bxjDept = bxjDept;
	}

	public Long getPoseId() {
		return poseId;
	}

	public void setPoseId(Long poseId) {
		this.poseId = poseId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String toString(){
		return "登陆用户信息：用户ID:"+userId+"；用户名："+super.getName()+"；经销商ID："+dealerId+"部门："+orgId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getOemPositionArea() {
		return oemPositionArea;
	}

	public void setOemPositionArea(String oemPositionArea) {
		this.oemPositionArea = oemPositionArea;
	}

	public Integer getOrgType() {
		return orgType;
	}

	public void setOrgType(Integer orgType) {
		this.orgType = orgType;
	}

	public String getDealerId()
	{
		return dealerId;
	}

	public void setDealerId(String dealerId)
	{
		this.dealerId = dealerId;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getRpcUserId() {
		return rpcUserId;
	}

	public void setRpcUserId(String rpcUserId) {
		this.rpcUserId = rpcUserId;
	}

	public String getRpcFlag() {
		return rpcFlag;
	}

	public void setRpcFlag(String rpcFlag) {
		this.rpcFlag = rpcFlag;
	}

	public String getRpcName() {
		return rpcName;
	}

	public void setRpcName(String rpcName) {
		this.rpcName = rpcName;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public Integer getDealerType() {
		return dealerType;
	}

	public void setDealerType(Integer dealerType) {
		this.dealerType = dealerType;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getActn() {
		return actn;
	}

	public void setActn(String actn) {
		this.actn = actn;
	}

	public String getTxtExt() {
		return txtExt;
	}

	public void setTxtExt(String txtExt) {
		this.txtExt = txtExt;
	}

	public String getRpcStat() {
		return rpcStat;
	}

	public void setRpcStat(String rpcStat) {
		this.rpcStat = rpcStat;
	}




	
}
