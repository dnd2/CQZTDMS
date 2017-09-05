/**********************************************************************
 * <pre>
 * FILE : TmCarNameBean.java
 * CLASS : TmCarNameBean
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

public class TmCarNameBean {
	
	private Integer oldModelSift;
	private Long seriesId;
	private Date updateDate;
	private Integer status;
	private Long updateUser;
	private Long carNameId;
	private Long createUser;
	private String carNameCode;
	private String chineseName;
	private Date createDate;
	private String remark;
	private String englishName;
	
	private Long brandId;
	private String brandName;
	private String seriesName;
	
	
	public Integer getOldModelSift() {
		return oldModelSift;
	}
	public void setOldModelSift(Integer oldModelSift) {
		this.oldModelSift = oldModelSift;
	}
	public Long getSeriesId() {
		return seriesId;
	}
	public void setSeriesId(Long seriesId) {
		this.seriesId = seriesId;
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
	public Long getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
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
	public String getCarNameCode() {
		return carNameCode;
	}
	public void setCarNameCode(String carNameCode) {
		this.carNameCode = carNameCode;
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
	public String getSeriesName() {
		return seriesName;
	}
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
}
