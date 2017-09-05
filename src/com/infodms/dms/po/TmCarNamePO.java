/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-02-26 06:35:52
* CreateBy   : tianheng
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmCarNamePO extends PO{

	private Date updateDate;
	private Long createBy;
	private Long carNameId;
	private Date createDate;
	private String englishName;
	private Integer oldModelSift;
	private Long seriesId;
	private Integer status;
	private Long updateBy;
	private String carNameCode;
	private Integer isDown;
	private String chineseName;
	private String remark;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCarNameId(Long carNameId){
		this.carNameId=carNameId;
	}

	public Long getCarNameId(){
		return this.carNameId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setEnglishName(String englishName){
		this.englishName=englishName;
	}

	public String getEnglishName(){
		return this.englishName;
	}

	public void setOldModelSift(Integer oldModelSift){
		this.oldModelSift=oldModelSift;
	}

	public Integer getOldModelSift(){
		return this.oldModelSift;
	}

	public void setSeriesId(Long seriesId){
		this.seriesId=seriesId;
	}

	public Long getSeriesId(){
		return this.seriesId;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCarNameCode(String carNameCode){
		this.carNameCode=carNameCode;
	}

	public String getCarNameCode(){
		return this.carNameCode;
	}

	public void setIsDown(Integer isDown){
		this.isDown=isDown;
	}

	public Integer getIsDown(){
		return this.isDown;
	}

	public void setChineseName(String chineseName){
		this.chineseName=chineseName;
	}

	public String getChineseName(){
		return this.chineseName;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

}