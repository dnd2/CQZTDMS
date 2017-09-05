/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-09-09 09:46:09
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtQuestionPO extends PO{

	private Long questionModule;
	private String answer;
	private Date updateDate;
	private Long createBy;
	private Date createDate;
	private Integer questionType;
	private Integer questionStatus;
	private Long questionId;
	private String questionDescribe;
	private Long answerBy;
	private Long updateBy;
	private String questionNo;
	private Integer isCommon;
	private Integer dealerType;

	public void setQuestionModule(Long questionModule){
		this.questionModule=questionModule;
	}

	public Long getQuestionModule(){
		return this.questionModule;
	}

	public void setAnswer(String answer){
		this.answer=answer;
	}

	public String getAnswer(){
		return this.answer;
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

	public void setQuestionType(Integer questionType){
		this.questionType=questionType;
	}

	public Integer getQuestionType(){
		return this.questionType;
	}

	public void setQuestionStatus(Integer questionStatus){
		this.questionStatus=questionStatus;
	}

	public Integer getQuestionStatus(){
		return this.questionStatus;
	}

	public void setQuestionId(Long questionId){
		this.questionId=questionId;
	}

	public Long getQuestionId(){
		return this.questionId;
	}

	public void setQuestionDescribe(String questionDescribe){
		this.questionDescribe=questionDescribe;
	}

	public String getQuestionDescribe(){
		return this.questionDescribe;
	}

	public void setAnswerBy(Long answerBy){
		this.answerBy=answerBy;
	}

	public Long getAnswerBy(){
		return this.answerBy;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setQuestionNo(String questionNo){
		this.questionNo=questionNo;
	}

	public String getQuestionNo(){
		return this.questionNo;
	}

	public void setIsCommon(Integer isCommon){
		this.isCommon=isCommon;
	}

	public Integer getIsCommon(){
		return this.isCommon;
	}

	public Integer getDealerType() {
		return dealerType;
	}

	public void setDealerType(Integer dealerType) {
		this.dealerType = dealerType;
	}

}