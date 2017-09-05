package com.infoservice.dms.chana.vo;

import java.util.Date;

/**
 * @Title: SalesVehicleApplyResultVO.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-26
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
@SuppressWarnings("serial")
public class SalesVehicleApplyResultVO extends BaseVO {
	private String soNo; //下端：上报销售单编号  CHAR(12)  上端：  
	private String vin; //下端：VIN  VARCHAR(17)  上端：  
	private String EntityName; //下端：经销商名称 ASC_NAME VARCHAR(300)  上端：DEALER_NAME VARCHAR2(60) 
	private String subEntityCode; //下端：二级经销商代码  CHAR(8)  上端：  
	private String subEntityName; //下端：二级经销商名称 ASC_NAME VARCHAR(300)  上端：DEALER_NAME VARCHAR2(60) 
	private String applyResult; //下端：上报结果  VARCHAR(90) 11231001 实销审核状态 审核通过11231002 实销审核状态 审核驳回 上端： 
	private String businessType; //下端：业务类型   0 通知结果 1 通知一级经销商 上端：
	private Date salesDate;
	
	public Date getSalesDate() {
		return salesDate;
	}
	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}
	public String getSoNo() {
		return soNo;
	}
	public void setSoNo(String soNo) {
		this.soNo = soNo;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getEntityName() {
		return EntityName;
	}
	public void setEntityName(String entityName) {
		EntityName = entityName;
	}
	public String getSubEntityCode() {
		return subEntityCode;
	}
	public void setSubEntityCode(String subEntityCode) {
		this.subEntityCode = subEntityCode;
	}
	public String getSubEntityName() {
		return subEntityName;
	}
	public void setSubEntityName(String subEntityName) {
		this.subEntityName = subEntityName;
	}
	public String getApplyResult() {
		return applyResult;
	}
	public void setApplyResult(String applyResult) {
		this.applyResult = applyResult;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("[soNo] = ").append(soNo)
		   .append(", [vin] = ").append(vin)
		   .append(", [EntityName] = ").append(EntityName)
		   .append(", [subEntityCode] = ").append(subEntityCode)
		   .append(", [subEntityName] = ").append(subEntityName)
		   .append(", [applyResult] = ").append(applyResult)
		   .append(", [businessType] = ").append(businessType)
		   .append(", [salesDate] = ").append(salesDate);
		return str.toString();
	}

}
