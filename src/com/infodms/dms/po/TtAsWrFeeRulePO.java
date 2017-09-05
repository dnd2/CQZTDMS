/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-05-20 19:13:28
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrFeeRulePO extends PO{

	private Long updateBy;
	private Date updateDate;
	private Integer frLawDays;
	private String frLawStandard;
	private Long createBy;
	private String frCode;
	private Date createDate;
	private Integer frLevel;
	private Long frId;
	private Integer frWrDays;

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

	public void setFrLawDays(Integer frLawDays){
		this.frLawDays=frLawDays;
	}

	public Integer getFrLawDays(){
		return this.frLawDays;
	}

	public void setFrLawStandard(String frLawStandard){
		this.frLawStandard=frLawStandard;
	}

	public String getFrLawStandard(){
		return this.frLawStandard;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setFrCode(String frCode){
		this.frCode=frCode;
	}

	public String getFrCode(){
		return this.frCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setFrLevel(Integer frLevel){
		this.frLevel=frLevel;
	}

	public Integer getFrLevel(){
		return this.frLevel;
	}

	public void setFrId(Long frId){
		this.frId=frId;
	}

	public Long getFrId(){
		return this.frId;
	}

	public void setFrWrDays(Integer frWrDays){
		this.frWrDays=frWrDays;
	}

	public Integer getFrWrDays(){
		return this.frWrDays;
	}

}