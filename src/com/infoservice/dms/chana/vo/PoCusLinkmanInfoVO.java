/**
 * Copyright (c) 2006-2008 OEM Infoservice Corp. 2006-2008,All Rights Reserved.
 * This software is published under the Infoservice DMS Service Inner Solution Team.
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * @File name:  com.infoservice.dcs.sales.customer.de.DSO0401.java
 * @Create on:  2008-6-12
 * @Author   :  wujiangtao
 *
 * @ChangeList
 * ---------------------------------------------------
 * NO    Date                     Editor               ChangeReasons
 * 1.    2008-6-12                 wujiangtao              Add 
 *
 */
package com.infoservice.dms.chana.vo;


/**
 * ҵ������jϵ����Ϣ��VO
 * @author Administrator
 * @date 2008-6-12 ����10:11:39
 *
 */
@SuppressWarnings("serial")
public class PoCusLinkmanInfoVO extends BaseVO {

	private String contactorDepartment;//������ VARCHAR(30)
	private String positionName;//ְ����� VARCHAR(30) 
	private String mobile;//�ֻ� VARCHAR(30)
	private String eMail;//E_MAIL VARCHAR(60)
	private String fax;//���� VARCHAR(30)
	private String contactorName;//jϵ��  VARCHAR(30)
	private String company;//��λ��� VARCHAR(150)
	private String phone;//�绰 VARCHAR(120)
	private Integer contactorType;//jϵ������ NUMERIC(8)
	private Integer bestContactType;//���jϵ��ʽ NUMERIC(8)
	private Integer gender;//�Ա� NUMERIC(8)
	private Integer isDefaultContactor;//�Ƿ�Ĭ��jϵ�� NUMERIC(8)
	private String remark;//��ע VARCHAR(300)
	
	
	public Integer getBestContactType() {
		return bestContactType;
	}


	public void setBestContactType(Integer bestContactType) {
		this.bestContactType = bestContactType;
	}


	public String getCompany() {
		return company;
	}


	public void setCompany(String company) {
		this.company = company;
	}


	public String getContactorDepartment() {
		return contactorDepartment;
	}


	public void setContactorDepartment(String contactorDepartment) {
		this.contactorDepartment = contactorDepartment;
	}


	public String getContactorName() {
		return contactorName;
	}


	public void setContactorName(String contactorName) {
		this.contactorName = contactorName;
	}


	public Integer getContactorType() {
		return contactorType;
	}


	public void setContactorType(Integer contactorType) {
		this.contactorType = contactorType;
	}



	public String getEMail() {
		return eMail;
	}


	public void setEMail(String mail) {
		eMail = mail;
	}


	public String getFax() {
		return fax;
	}


	public void setFax(String fax) {
		this.fax = fax;
	}


	public Integer getGender() {
		return gender;
	}


	public void setGender(Integer gender) {
		this.gender = gender;
	}


	public Integer getIsDefaultContactor() {
		return isDefaultContactor;
	}


	public void setIsDefaultContactor(Integer isDefaultContactor) {
		this.isDefaultContactor = isDefaultContactor;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getPositionName() {
		return positionName;
	}


	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}

}