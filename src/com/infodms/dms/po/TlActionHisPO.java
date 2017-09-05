/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-09-24 10:53:08
* CreateBy   : xianchao zhang
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.DateTimeUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;


@SuppressWarnings("serial")
public class TlActionHisPO extends PO{

	private String deptId;
	private Date updateDate;
	private Long responseTime;
	private String updateBy;
	private String actionUrl;
	private String createBy;
	private String actionHisId;
	private Long userId;
	private String dlrId;
	private Date createDate;
	private Date startDate;
	private Date endDate;
	private String serverId;
	
	private static POFactory factory = POFactoryBuilder.getInstance();
	public Logger logger = Logger.getLogger(TlActionHisPO.class);
	public TlActionHisPO(){};
	
	public TlActionHisPO(ActionContext atx,Date now,long useTime){
		try{
			AclUserBean logonUser = (AclUserBean) (atx.getSession().get(Constant.LOGON_USER));
			this.serverId = System.getenv("serverId");
			this.responseTime = useTime;
			String actionUrl = atx.getRequest().getRequestURI();
			String actionContext = atx.getRequest().getContextPath();
			this.actionUrl = actionUrl.substring(actionContext.length(), actionUrl.lastIndexOf("."));
			this.actionHisId = factory.getStringPK(this);
			if(logonUser!=null&&logonUser.getUserId()!=null){
				this.userId = logonUser.getUserId();
			}else {
				this.userId = new Long(-1);
			}
			this.startDate = now;
			this.endDate = DateTimeUtil.getDateByTime(now.getTime()+useTime);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
	}
	
	public void setDeptId(String deptId){
		this.deptId=deptId;
	}

	public String getDeptId(){
		return this.deptId;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setResponseTime(Long responseTime){
		this.responseTime=responseTime;
	}

	public Long getResponseTime(){
		return this.responseTime;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setActionUrl(String actionUrl){
		this.actionUrl=actionUrl;
	}

	public String getActionUrl(){
		return this.actionUrl;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setActionHisId(String actionHisId){
		this.actionHisId=actionHisId;
	}

	public String getActionHisId(){
		return this.actionHisId;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return this.userId;
	}

	public void setDlrId(String dlrId){
		this.dlrId=dlrId;
	}

	public String getDlrId(){
		return this.dlrId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}

	public Date getStartDate(){
		return this.startDate;
	}

	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}

	public Date getEndDate(){
		return this.endDate;
	}
	
	public void setServerId(String serverId){
		this.serverId=serverId;
	}
	
	public String getServerId(){
		return this.serverId;
	}
}