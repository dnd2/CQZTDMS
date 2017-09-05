/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-10-20 11:45:37
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmPtPartTypePO extends PO{

	private String parttypeName;
	private Date updateDate;
	private Long partNum;
	private String parttypeCode;
	private Long updateBy;
	private Integer isMax;
	private Long createBy;
	private Long oemCompanyId;
	private Date createDate;
	private Integer isReturn;
	private Long id;

	public void setParttypeName(String parttypeName){
		this.parttypeName=parttypeName;
	}

	public String getParttypeName(){
		return this.parttypeName;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPartNum(Long partNum){
		this.partNum=partNum;
	}

	public Long getPartNum(){
		return this.partNum;
	}

	public void setParttypeCode(String parttypeCode){
		this.parttypeCode=parttypeCode;
	}

	public String getParttypeCode(){
		return this.parttypeCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setIsMax(Integer isMax){
		this.isMax=isMax;
	}

	public Integer getIsMax(){
		return this.isMax;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setOemCompanyId(Long oemCompanyId){
		this.oemCompanyId=oemCompanyId;
	}

	public Long getOemCompanyId(){
		return this.oemCompanyId;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setIsReturn(Integer isReturn){
		this.isReturn=isReturn;
	}

	public Integer getIsReturn(){
		return this.isReturn;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}