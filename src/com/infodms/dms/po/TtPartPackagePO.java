/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-12-09 12:07:04
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartPackagePO extends PO{

	private String packUom;
	private Date updateDate;
	private Long packId;
	private Long createBy;
	private Integer status;
	private Long packType;
	private String locName;
	private String packName;
	private Double packQty;
	private Long updateBy;
	private String packSpec2;
	private Date createDate;
	private String packSpec;

	public void setPackUom(String packUom){
		this.packUom=packUom;
	}

	public String getPackUom(){
		return this.packUom;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPackId(Long packId){
		this.packId=packId;
	}

	public Long getPackId(){
		return this.packId;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setPackType(Long packType){
		this.packType=packType;
	}

	public Long getPackType(){
		return this.packType;
	}

	public void setLocName(String locName){
		this.locName=locName;
	}

	public String getLocName(){
		return this.locName;
	}

	public void setPackName(String packName){
		this.packName=packName;
	}

	public String getPackName(){
		return this.packName;
	}

	public void setPackQty(Double packQty){
		this.packQty=packQty;
	}

	public Double getPackQty(){
		return this.packQty;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPackSpec2(String packSpec2){
		this.packSpec2=packSpec2;
	}

	public String getPackSpec2(){
		return this.packSpec2;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPackSpec(String packSpec){
		this.packSpec=packSpec;
	}

	public String getPackSpec(){
		return this.packSpec;
	}

}