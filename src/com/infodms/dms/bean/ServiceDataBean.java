package com.infodms.dms.bean;

/**
 * 
 * <p>Title:ServiceInfoBean.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-5-17</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class ServiceDataBean {
    // 资料ID
	private String dataId;
	// 资料名称
	private String dataName;
	// 资料单价
	private String price;
	// 状态
	private String status;
	// 创建时间
	private String createDate;
	// 创建人员
	private String createBy;
	// 更新时间
	private String updateDate;
	// 更新人员
	private String updateBy;
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getDataName() {
		return dataName;
	}
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	
	
	
}
