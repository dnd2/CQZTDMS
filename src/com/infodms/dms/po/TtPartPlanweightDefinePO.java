/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-05 16:06:31
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartPlanweightDefinePO extends PO{

	private Integer state;
	private Date disableDate;
	private Date deleteDate;
	private Date updateDate;
	private Double twelveWeight;
	private Long createBy;
	private Double sixWeight;
	private Integer status;
	private Double threeWeight;
	private Long deftId;
	private Long updateBy;
	private Date createDate;
	private Long disableBy;
	private Long deleteBy;

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setTwelveWeight(Double twelveWeight){
		this.twelveWeight=twelveWeight;
	}

	public Double getTwelveWeight(){
		return this.twelveWeight;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setSixWeight(Double sixWeight){
		this.sixWeight=sixWeight;
	}

	public Double getSixWeight(){
		return this.sixWeight;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setThreeWeight(Double threeWeight){
		this.threeWeight=threeWeight;
	}

	public Double getThreeWeight(){
		return this.threeWeight;
	}

	public void setDeftId(Long deftId){
		this.deftId=deftId;
	}

	public Long getDeftId(){
		return this.deftId;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

}