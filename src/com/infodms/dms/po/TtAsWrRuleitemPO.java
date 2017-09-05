/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-06-10 10:35:06
* CreateBy   : chun_chang
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrRuleitemPO extends PO{

	private String elementValue;
	private Long ruleNo;
	private Date updateDate;
	private Integer elementPosition;
	private Long updateBy;
	private Long createBy;
	private String booleanComparison;
	private String comparisonOp;
	private Date createDate;
	private Long elementNo;
	private Long id;

	public void setElementValue(String elementValue){
		this.elementValue=elementValue;
	}

	public String getElementValue(){
		return this.elementValue;
	}

	public void setRuleNo(Long ruleNo){
		this.ruleNo=ruleNo;
	}

	public Long getRuleNo(){
		return this.ruleNo;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setElementPosition(Integer elementPosition){
		this.elementPosition=elementPosition;
	}

	public Integer getElementPosition(){
		return this.elementPosition;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setBooleanComparison(String booleanComparison){
		this.booleanComparison=booleanComparison;
	}

	public String getBooleanComparison(){
		return this.booleanComparison;
	}

	public void setComparisonOp(String comparisonOp){
		this.comparisonOp=comparisonOp;
	}

	public String getComparisonOp(){
		return this.comparisonOp;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setElementNo(Long elementNo){
		this.elementNo=elementNo;
	}

	public Long getElementNo(){
		return this.elementNo;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

}