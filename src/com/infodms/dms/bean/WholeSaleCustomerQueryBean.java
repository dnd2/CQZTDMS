package com.infodms.dms.bean;

import java.util.Date;

public class WholeSaleCustomerQueryBean {
	
	//客户基本信息
	private Long customerId;                           //客户ID	
	private String customerName;                         //客户名称
	private String certificateNum;                        //客户姓名
	private String linkmanMobile;                          //联系方式	对应修改后字段MAIN_CONT_MODE
	private String customerAdress;                            //所属经销商ID	
	private Long salesNum;                         //所在省份-------------
	private String orgName;                             //所在城市-------------
	private Long classType; 
	private String linkmanName;
	private Long salesType;
	private Date updateDate;
	private Long updateBy;
	
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCertificateNum() {
		return certificateNum;
	}
	public void setCertificateNum(String certificateNum) {
		this.certificateNum = certificateNum;
	}
	public String getLinkmanMobile() {
		return linkmanMobile;
	}
	public void setLinkmanMobile(String linkmanMobile) {
		this.linkmanMobile = linkmanMobile;
	}
	public String getCustomerAdress() {
		return customerAdress;
	}
	public void setCustomerAdress(String customerAdress) {
		this.customerAdress = customerAdress;
	}
	public Long getSalesNum() {
		return salesNum;
	}
	public void setSalesNum(Long salesNum) {
		this.salesNum = salesNum;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public Long getClassType() {
		return classType;
	}
	public void setClassType(Long classType) {
		this.classType = classType;
	}
	public String getLinkmanName() {
		return linkmanName;
	}
	public void setLinkmanName(String linkmanName) {
		this.linkmanName = linkmanName;
	}
	public Long getSalesType() {
		return salesType;
	}
	public void setSalesType(Long salesType) {
		this.salesType = salesType;
	}
	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}


	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}
}
