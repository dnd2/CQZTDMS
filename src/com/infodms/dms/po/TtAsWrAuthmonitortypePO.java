/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-11-09 15:17:58
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrAuthmonitortypePO extends PO{

	private Date updateDate;
	private String partBigtypeCode;
	private Long updateBy;
	private Long partBigtypeId;
	private Long createBy;
	private Long oemCompanyId;
	private Date createDate;
	private String approvalLevel;
	private Integer isDel;
	private String partBigtypeName;
	private Long id;

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setPartBigtypeCode(String partBigtypeCode){
		this.partBigtypeCode=partBigtypeCode;
	}

	public String getPartBigtypeCode(){
		return this.partBigtypeCode;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPartBigtypeId(Long partBigtypeId){
		this.partBigtypeId=partBigtypeId;
	}

	public Long getPartBigtypeId(){
		return this.partBigtypeId;
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

	public void setApprovalLevel(String approvalLevel){
		this.approvalLevel=approvalLevel;
	}

	public String getApprovalLevel(){
		return this.approvalLevel;
	}

	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}

	public Integer getIsDel(){
		return this.isDel;
	}

	public void setPartBigtypeName(String partBigtypeName){
		this.partBigtypeName=partBigtypeName;
	}

	public String getPartBigtypeName(){
		return this.partBigtypeName;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}