/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-10-21 09:48:48
* CreateBy   : Iverson
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrProductPackagePO extends PO{

	private String packageCode;
	private Long productId;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private Integer status;
	private String packageName;
	private Long provinceId;

	public void setPackageCode(String packageCode){
		this.packageCode=packageCode;
	}

	public String getPackageCode(){
		return this.packageCode;
	}

	public void setProductId(Long productId){
		this.productId=productId;
	}

	public Long getProductId(){
		return this.productId;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setPackageName(String packageName){
		this.packageName=packageName;
	}

	public String getPackageName(){
		return this.packageName;
	}

	public void setProvinceId(Long provinceId){
		this.provinceId=provinceId;
	}

	public Long getProvinceId(){
		return this.provinceId;
	}

}