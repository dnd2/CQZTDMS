/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-11-03 16:36:02
* CreateBy   : xianchao zhang
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TrPoseBinsPO extends PO{

	private Date updateDate;
	private String updateBy;
	private String createBy;
	private Date createDate;
	private String poseId;
	private String poseBinsId;
	private String binsCodeId;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPoseId(String poseId){
		this.poseId=poseId;
	}

	public String getPoseId(){
		return this.poseId;
	}

	public void setPoseBinsId(String poseBinsId){
		this.poseBinsId=poseBinsId;
	}

	public String getPoseBinsId(){
		return this.poseBinsId;
	}

	public void setBinsCodeId(String binsCodeId){
		this.binsCodeId=binsCodeId;
	}

	public String getBinsCodeId(){
		return this.binsCodeId;
	}

}