package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

public class TtAsWrNewsReadPO extends PO {
	
	private Long newsId;
	private Long dealerId;
	private Date readDate;
	private String newsback;
	private Long readUserId;
	
	public Long getReadUserId() {
		return readUserId;
	}
	public void setReadUserId(Long readUserId) {
		this.readUserId = readUserId;
	}
	public Long getNewsId()
	{
		return newsId;
	}
	public void setNewsId(Long newsId)
	{
		this.newsId = newsId;
	}
	public Long getDealerId()
	{
		return dealerId;
	}
	public void setDealerId(Long dealerId)
	{
		this.dealerId = dealerId;
	}
	public Date getReadDate()
	{
		return readDate;
	}
	public void setReadDate(Date readDate)
	{
		this.readDate = readDate;
	}
	public String getNewsback()
	{
		return newsback;
	}
	public void setNewsback(String newsback)
	{
		this.newsback = newsback;
	}
	
}
