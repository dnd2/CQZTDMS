package com.infodms.dms.bean;

/**
 * 客户信息
 * 
 * @author LAX
 */
public class CustBean {
	private String custId;         //客户ID
	private String custName;       //客户名称 	
	private String custSex;        //客户性别
	private String custType;       //客户类型
	private String custPhone;      //客户联系电话
	private String contName;       //联系人姓名
	private String contSex;        //联系人性别
	private String contPhone;	   //联系人电话
	private String zip;            //邮编	
	private String address;        //地址
	private String start;          //状态
	private String errorInfo;      //错误信息
	private int number;         //行号
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
	public String getCustSex() {
		return custSex;
	}
	public void setCustSex(String custSex) {
		this.custSex = custSex;
	}
	public String getCustType() {
		return custType;
	}
	public void setCustType(String custType) {
		this.custType = custType;
	}
	public String getCustPhone() {
		return custPhone;
	}
	public void setCustPhone(String custPhone) {
		this.custPhone = custPhone;
	}
	public String getContName() {
		return contName;
	}
	public void setContName(String contName) {
		this.contName = contName;
	}
	public String getContSex() {
		return contSex;
	}
	public void setContSex(String contSex) {
		this.contSex = contSex;
	}
	public String getContPhone() {
		return contPhone;
	}
	public void setContPhone(String contPhone) {
		this.contPhone = contPhone;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}

}
