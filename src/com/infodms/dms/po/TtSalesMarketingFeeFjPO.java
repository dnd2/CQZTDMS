/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-01-16 10:12:55
* CreateBy   : wizard_lee
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtSalesMarketingFeeFjPO extends PO{

	private Integer fjTypeId;
	private Long sdtlId;
	private Integer fjOrderBy;
	private Long feeFjId;

	public void setFjTypeId(Integer fjTypeId){
		this.fjTypeId=fjTypeId;
	}

	public Integer getFjTypeId(){
		return this.fjTypeId;
	}

	public void setSdtlId(Long sdtlId){
		this.sdtlId=sdtlId;
	}

	public Long getSdtlId(){
		return this.sdtlId;
	}

	public void setFjOrderBy(Integer fjOrderBy){
		this.fjOrderBy=fjOrderBy;
	}

	public Integer getFjOrderBy(){
		return this.fjOrderBy;
	}

	public void setFeeFjId(Long feeFjId){
		this.feeFjId=feeFjId;
	}

	public Long getFeeFjId(){
		return this.feeFjId;
	}

}