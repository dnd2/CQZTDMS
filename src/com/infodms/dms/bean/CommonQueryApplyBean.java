package com.infodms.dms.bean;

/**
 * 申请单位
 * 
 * @author PGM
 * 
 */

public class CommonQueryApplyBean {
	// 申请单位基本信息
	private String userId; // 服务大区ID
	private String name; // 服务大区下属人员名称
	private String phone; // 服务大区下属人员电话

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
