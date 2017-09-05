/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-11-14 15:19:22
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TPcVechilePO extends PO{

	private Long ctmId;
	private String pin;
	private Date buyDate;
	private Date updateDate;
	private Long createBy;
	private String carNumber;
	private Long vechileId;
	private Long vechileColor;
	private Date createDate;
	private Date boardDate;
	private Long status;
	private Long updateBy;
	private Double price;
	private String modelName;
	private String vin;
	private Date productDate;
	private String lowVin;
	private String modelCode;

	public void setCtmId(Long ctmId){
		this.ctmId=ctmId;
	}

	public Long getCtmId(){
		return this.ctmId;
	}

	public void setPin(String pin){
		this.pin=pin;
	}

	public String getPin(){
		return this.pin;
	}

	public void setBuyDate(Date buyDate){
		this.buyDate=buyDate;
	}

	public Date getBuyDate(){
		return this.buyDate;
	}

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

	public void setCarNumber(String carNumber){
		this.carNumber=carNumber;
	}

	public String getCarNumber(){
		return this.carNumber;
	}

	public void setVechileId(Long vechileId){
		this.vechileId=vechileId;
	}

	public Long getVechileId(){
		return this.vechileId;
	}

	public void setVechileColor(Long vechileColor){
		this.vechileColor=vechileColor;
	}

	public Long getVechileColor(){
		return this.vechileColor;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setBoardDate(Date boardDate){
		this.boardDate=boardDate;
	}

	public Date getBoardDate(){
		return this.boardDate;
	}

	public void setStatus(Long status){
		this.status=status;
	}

	public Long getStatus(){
		return this.status;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPrice(Double price){
		this.price=price;
	}

	public Double getPrice(){
		return this.price;
	}

	public void setModelName(String modelName){
		this.modelName=modelName;
	}

	public String getModelName(){
		return this.modelName;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setProductDate(Date productDate){
		this.productDate=productDate;
	}

	public Date getProductDate(){
		return this.productDate;
	}

	public void setLowVin(String lowVin){
		this.lowVin=lowVin;
	}

	public String getLowVin(){
		return this.lowVin;
	}

	public void setModelCode(String modelCode){
		this.modelCode=modelCode;
	}

	public String getModelCode(){
		return this.modelCode;
	}

}