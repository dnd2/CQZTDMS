/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-12-17 17:00:58
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartLogisticsInfoPO extends PO{

	private String logisticsNo2;
	private Long logisticsId;
	private Long updateBy;
	private Date updateDate;
	private String logisticsInfo;
	private Long createBy;
	private Date createDate;
	private Date logisticsDate;
	private String logisticsNo;

	public void setLogisticsNo2(String logisticsNo2){
		this.logisticsNo2=logisticsNo2;
	}

	public String getLogisticsNo2(){
		return this.logisticsNo2;
	}

	public void setLogisticsId(Long logisticsId){
		this.logisticsId=logisticsId;
	}

	public Long getLogisticsId(){
		return this.logisticsId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setLogisticsInfo(String logisticsInfo){
		this.logisticsInfo=logisticsInfo;
	}

	public String getLogisticsInfo(){
		return this.logisticsInfo;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setLogisticsDate(Date logisticsDate){
		this.logisticsDate=logisticsDate;
	}

	public Date getLogisticsDate(){
		return this.logisticsDate;
	}

	public void setLogisticsNo(String logisticsNo){
		this.logisticsNo=logisticsNo;
	}

	public String getLogisticsNo(){
		return this.logisticsNo;
	}

}