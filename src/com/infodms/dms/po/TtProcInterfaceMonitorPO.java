/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-03-20 15:16:03
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtProcInterfaceMonitorPO extends PO{

	private Integer status;
	private String procName;
	private String interfaceName;
	private Date createDate;
	private Integer count;

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setProcName(String procName){
		this.procName=procName;
	}

	public String getProcName(){
		return this.procName;
	}

	public void setInterfaceName(String interfaceName){
		this.interfaceName=interfaceName;
	}

	public String getInterfaceName(){
		return this.interfaceName;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCount(Integer count){
		this.count=count;
	}

	public Integer getCount(){
		return this.count;
	}

}