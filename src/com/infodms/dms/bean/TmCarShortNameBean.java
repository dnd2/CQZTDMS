/**********************************************************************
 * <pre>
 * FILE : TmCarShortNameBean.java
 * CLASS : TmCarShortNameBean
 *
 * AUTHOR : yangyong
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2009-12-15|yangyong| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.bean;

import java.util.Date;

public class TmCarShortNameBean {

	private Integer oldModelSift;
	private Date updateDate;
	private Integer status;
	private String carTypeCode;
	private Long updateUser;
	private Long carTypeId;
	private Long carNameId;
	private Long createUser;
	private String chineseName;
	private Date createDate;
	private String remark;
	private String englishName;
	
	private Long brandId;
	private String brandName;
	private Long seriesId;
	private String seriesName;
	private String carName;
	
	
	public Integer getOldModelSift() {
		return oldModelSift;
	}
	public void setOldModelSift(Integer oldModelSift) {
		this.oldModelSift = oldModelSift;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCarTypeCode() {
		return carTypeCode;
	}
	public void setCarTypeCode(String carTypeCode) {
		this.carTypeCode = carTypeCode;
	}
	public Long getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}
	public Long getCarTypeId() {
		return carTypeId;
	}
	public void setCarTypeId(Long carTypeId) {
		this.carTypeId = carTypeId;
	}
	public Long getCarNameId() {
		return carNameId;
	}
	public void setCarNameId(Long carNameId) {
		this.carNameId = carNameId;
	}
	public Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	public String getChineseName() {
		return chineseName;
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public Long getSeriesId() {
		return seriesId;
	}
	public void setSeriesId(Long seriesId) {
		this.seriesId = seriesId;
	}
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	public String getCarName() {
		return carName;
	}
	public void setCarName(String carName) {
		this.carName = carName;
	}
}
