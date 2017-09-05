package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

public class TtAsWrNewsRolePO extends PO {
	private Long newsId;
	private Long roleId;
	public Long getNewsId()
	{
		return newsId;
	}
	public void setNewsId(Long newsId)
	{
		this.newsId = newsId;
	}
	public Long getRoleId()
	{
		return roleId;
	}
	public void setRoleId(Long roleId)
	{
		this.roleId = roleId;
	}
}
