/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-11-03 16:36:03
* CreateBy   : xianchao zhang
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TcOperateRemindPO extends PO{

	private String actionStat;
	private Date updateDate;
	private String createBy;
	private String procUrl;
	private String tableId;
	private String submiter;
	private String operateType;
	private Date createDate;
	private String dlrId;
	private Date plannedDate;
	private String custType;
	private String mainContMode;
	private String custName;
	private String remindType;
	private String operateRemindId;
	private String deptId;
	private String updateBy;
	private Double price;
	private String vin;
	private String plannedContent;
	private String vhclLic;

	public void setActionStat(String actionStat){
		this.actionStat=actionStat;
	}

	public String getActionStat(){
		return this.actionStat;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setProcUrl(String procUrl){
		this.procUrl=procUrl;
	}

	public String getProcUrl(){
		return this.procUrl;
	}

	public void setTableId(String tableId){
		this.tableId=tableId;
	}

	public String getTableId(){
		return this.tableId;
	}

	public void setSubmiter(String submiter){
		this.submiter=submiter;
	}

	public String getSubmiter(){
		return this.submiter;
	}

	public void setOperateType(String operateType){
		this.operateType=operateType;
	}

	public String getOperateType(){
		return this.operateType;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDlrId(String dlrId){
		this.dlrId=dlrId;
	}

	public String getDlrId(){
		return this.dlrId;
	}

	public void setPlannedDate(Date plannedDate){
		this.plannedDate=plannedDate;
	}

	public Date getPlannedDate(){
		return this.plannedDate;
	}

	public void setCustType(String custType){
		this.custType=custType;
	}

	public String getCustType(){
		return this.custType;
	}

	public void setMainContMode(String mainContMode){
		this.mainContMode=mainContMode;
	}

	public String getMainContMode(){
		return this.mainContMode;
	}

	public void setCustName(String custName){
		this.custName=custName;
	}

	public String getCustName(){
		return this.custName;
	}

	public void setRemindType(String remindType){
		this.remindType=remindType;
	}

	public String getRemindType(){
		return this.remindType;
	}

	public void setOperateRemindId(String operateRemindId){
		this.operateRemindId=operateRemindId;
	}

	public String getOperateRemindId(){
		return this.operateRemindId;
	}

	public void setDeptId(String deptId){
		this.deptId=deptId;
	}

	public String getDeptId(){
		return this.deptId;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setPlannedContent(String plannedContent){
		this.plannedContent=plannedContent;
	}

	public String getPlannedContent(){
		return this.plannedContent;
	}

	public void setVhclLic(String vhclLic){
		this.vhclLic=vhclLic;
	}

	public String getVhclLic(){
		return this.vhclLic;
	}

}