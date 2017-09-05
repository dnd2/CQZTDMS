/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-14 14:49:40
* CreateBy   : ZLD
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrCruisePO extends PO{

	private String crNo;
	private Double crDay;
	private Long dealerId;
	private Long companyId;
	private Date updateDate;
	private Long createBy;
	private Integer status;
	private String crPhone;
	private String crCause;
	private Long updateBy;
	private String crPrincipal;
	private Date auditDate;
	private Long id;
	private Date makeDate;
	private Double crMileage;
	private String auditingOpinion;
	private Integer isSpefee;
	private Date createDate;
	private String crWhither;
	private Integer isSuspend;
	private Date suspendDate;
	private String fixPointDate;
	public Integer getIsSuspend() {
		return isSuspend;
	}

	public void setIsSuspend(Integer isSuspend) {
		this.isSuspend = isSuspend;
	}

	public Date getSuspendDate() {
		return suspendDate;
	}

	public void setSuspendDate(Date suspendDate) {
		this.suspendDate = suspendDate;
	}

	public String getFixPointDate() {
		return fixPointDate;
	}

	public void setFixPointDate(String fixPointDate) {
		this.fixPointDate = fixPointDate;
	}

	public void setCrNo(String crNo){
		this.crNo=crNo;
	}

	public String getCrNo(){
		return this.crNo;
	}

	public void setCrDay(Double crDay){
		this.crDay=crDay;
	}

	public Double getCrDay(){
		return this.crDay;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
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

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setCrPhone(String crPhone){
		this.crPhone=crPhone;
	}

	public String getCrPhone(){
		return this.crPhone;
	}

	public void setCrCause(String crCause){
		this.crCause=crCause;
	}

	public String getCrCause(){
		return this.crCause;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCrPrincipal(String crPrincipal){
		this.crPrincipal=crPrincipal;
	}

	public String getCrPrincipal(){
		return this.crPrincipal;
	}

	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	public Date getAuditDate(){
		return this.auditDate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setMakeDate(Date makeDate){
		this.makeDate=makeDate;
	}

	public Date getMakeDate(){
		return this.makeDate;
	}

	public void setCrMileage(Double crMileage){
		this.crMileage=crMileage;
	}

	public Double getCrMileage(){
		return this.crMileage;
	}

	public void setAuditingOpinion(String auditingOpinion){
		this.auditingOpinion=auditingOpinion;
	}

	public String getAuditingOpinion(){
		return this.auditingOpinion;
	}

	public void setIsSpefee(Integer isSpefee){
		this.isSpefee=isSpefee;
	}

	public Integer getIsSpefee(){
		return this.isSpefee;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCrWhither(String crWhither){
		this.crWhither=crWhither;
	}

	public String getCrWhither(){
		return this.crWhither;
	}

}