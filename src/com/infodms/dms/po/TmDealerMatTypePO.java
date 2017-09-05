/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-12-12 17:20:45
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmDealerMatTypePO extends PO{

	private Date updateDate;
	private Long updateBy;
	private Integer exportSalesFlag;
	private Long materialId;
	private Long createBy;
	private Date createDate;
	private Integer isRuleMat;
	private Integer isInsale;
	private Long dealerId;
	private Long id;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setExportSalesFlag(Integer exportSalesFlag){
		this.exportSalesFlag=exportSalesFlag;
	}

	public Integer getExportSalesFlag(){
		return this.exportSalesFlag;
	}

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
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

	public void setIsRuleMat(Integer isRuleMat){
		this.isRuleMat=isRuleMat;
	}

	public Integer getIsRuleMat(){
		return this.isRuleMat;
	}

	public void setIsInsale(Integer isInsale){
		this.isInsale=isInsale;
	}

	public Integer getIsInsale(){
		return this.isInsale;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}