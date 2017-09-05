package com.infodms.dms.bean;

import java.util.Date;

import com.infoservice.po3.bean.PO;

public class TtAsActivityDealerBean extends PO
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dealerName;
	private Long dealerId;
	private Long updateBy;
	private Date updateDate;
	private String dealerCode;
	private Long createBy;
	private Long activityId;
	private Long areaId;
	private Date createDate;
	private Integer status;
	private Long adId;
	private String phone;
	public String getDealerName()
	{
		return dealerName;
	}
	public void setDealerName(String dealerName)
	{
		this.dealerName = dealerName;
	}
	public Long getDealerId()
	{
		return dealerId;
	}
	public void setDealerId(Long dealerId)
	{
		this.dealerId = dealerId;
	}
	public Long getUpdateBy()
	{
		return updateBy;
	}
	public void setUpdateBy(Long updateBy)
	{
		this.updateBy = updateBy;
	}
	public Date getUpdateDate()
	{
		return updateDate;
	}
	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}
	public String getDealerCode()
	{
		return dealerCode;
	}
	public void setDealerCode(String dealerCode)
	{
		this.dealerCode = dealerCode;
	}
	public Long getCreateBy()
	{
		return createBy;
	}
	public void setCreateBy(Long createBy)
	{
		this.createBy = createBy;
	}
	public Long getActivityId()
	{
		return activityId;
	}
	public void setActivityId(Long activityId)
	{
		this.activityId = activityId;
	}
	public Long getAreaId()
	{
		return areaId;
	}
	public void setAreaId(Long areaId)
	{
		this.areaId = areaId;
	}
	public Date getCreateDate()
	{
		return createDate;
	}
	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public Long getAdId()
	{
		return adId;
	}
	public void setAdId(Long adId)
	{
		this.adId = adId;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}

}
