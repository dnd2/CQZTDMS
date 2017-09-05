package com.infodms.dms.bean;

import java.util.Date;
/**
 * 客户关怀信息
 * 
 * @author LAX
 */
public class CustCareBean {
	//客户关怀信息
	private String custId;         //客户ID
	private String custName;       //客户名称 	
	private String custType;       //客户类型	
	private String contName;       //联系人	
	private String contCellPone;   //联系人手机
	private String workPhone;      //联系人电话
	private String contAllPone;	
	private String businessMan;    //业务人员	
	private String plannedDate;    //最近计划时间	
	private String custCallBackId; //关怀明细ID	
	private String careType;       //关怀类型	
	private String careTypeId;     //关怀类型ID	
	private String careDate;       //关怀时间	
	private String plannedContent; //计划内容	
	private String careResult;     //关怀结果	
	private String careResultId;   //关怀结果ID	
	private String remark;         //备注

	public String getCareResultId() {
		return careResultId;
	}
	public void setCareResultId(String careResultId) {
		this.careResultId = careResultId;
	}
	public String getCareTypeId() {
		return careTypeId;
	}
	public void setCareTypeId(String careTypeId) {
		this.careTypeId = careTypeId;
	}
	public String getCustCallBackId() {
		return custCallBackId;
	}
	public void setCustCallBackId(String custCallBackId) {
		this.custCallBackId = custCallBackId;
	}
	public String getCareDate() {
		return careDate;
	}
	public void setCareDate(String careDate) {
		this.careDate = careDate;
	}
	public String getPlannedContent() {
		return plannedContent;
	}
	public void setPlannedContent(String plannedContent) {
		this.plannedContent = plannedContent;
	}
	public String getCareResult() {
		return careResult;
	}
	public void setCareResult(String careResult) {
		this.careResult = careResult;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCustType() {
		return custType;
	}
	public void setCustType(String custType) {
		this.custType = custType;
	}
	public String getContName() {
		return contName;
	}
	public void setContName(String contName) {
		this.contName = contName;
	}
	public String getContCellPone() {
		return contCellPone;
	}
	public void setContCellPone(String contCellPone) {
		this.contCellPone = contCellPone;
	}
	public String getContAllPone() {
		return contAllPone;
	}
	public void setContAllPone(String contAllPone) {
		this.contAllPone = contAllPone;
	}
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	public String getBusinessMan() {
		return businessMan;
	}
	public void setBusinessMan(String businessMan) {
		this.businessMan = businessMan;
	}
	public String getPlannedDate() {
		return plannedDate;
	}
	public void setPlannedDate(String plannedDate) {
		this.plannedDate = plannedDate;
	}
	public String getCareType() {
		return careType;
	}
	public void setCareType(String careType) {
		this.careType = careType;
	}
	

}
