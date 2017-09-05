package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

/**
 * <ul>
 * <li>文件名称: TtSalesRebateAuditPO.java</li>
 * <li>文件描述: </li>
 * <li>版权所有: 版权所有(C)2012-2013</li>
 * <li>内容摘要: </li>
 * <li>完成日期: 2013-4-19 下午2:15:13</li>
 * <li>修改记录: </li>
 * </ul>
 * 
 * @version 1.0
 * @author wangsongwei
 */
@SuppressWarnings("serial")
public class TtSalesRebateAuditPO extends PO {
	
	private Long auditId;
	private Long rebId;
	private Long id;
	private Long auditPer;
	private Date auditDate;
	private Long auditStatus;
	private String remark;
	private Long createBy;
	private Date createDate;
	private Long updateBy;
	private Date updateDate;
	public Long getAuditId()
	{
		return auditId;
	}
	public void setAuditId(Long auditId)
	{
		this.auditId = auditId;
	}
	public Long getRebId()
	{
		return rebId;
	}
	public void setRebId(Long rebId)
	{
		this.rebId = rebId;
	}
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Long getAuditPer()
	{
		return auditPer;
	}
	public void setAuditPer(Long auditPer)
	{
		this.auditPer = auditPer;
	}
	public Date getAuditDate()
	{
		return auditDate;
	}
	public void setAuditDate(Date auditDate)
	{
		this.auditDate = auditDate;
	}
	public Long getAuditStatus()
	{
		return auditStatus;
	}
	public void setAuditStatus(Long auditStatus)
	{
		this.auditStatus = auditStatus;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public Long getCreateBy()
	{
		return createBy;
	}
	public void setCreateBy(Long createBy)
	{
		this.createBy = createBy;
	}
	public Date getCreateDate()
	{
		return createDate;
	}
	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
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
	
	
}
