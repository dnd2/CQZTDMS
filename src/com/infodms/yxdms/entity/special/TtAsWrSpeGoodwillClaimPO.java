/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2017-09-05 13:46:06
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.yxdms.entity.special;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrSpeGoodwillClaimPO extends PO{

	private String dealerContact;
	private String tecPartCode;
	private String claimNo;
	private Double claimAmount;
	private Integer percent;
	private String complainAdvice;
	private String eventTheme;
	private String dealerPhone;
	private String job;
	private String partNameDealer;
	private String supplyNameDealer;
	private Long specialId;
	private Long id;
	private Date problemDate;
	private String supplyCodeDealer;
	private Integer isSupplyClaim;
	private Integer isClaim;
	private String partCodeDealer;
	private String tecSupplyCode;

	public void setDealerContact(String dealerContact){
		this.dealerContact=dealerContact;
	}

	public String getDealerContact(){
		return this.dealerContact;
	}

	public void setTecPartCode(String tecPartCode){
		this.tecPartCode=tecPartCode;
	}

	public String getTecPartCode(){
		return this.tecPartCode;
	}

	public void setClaimNo(String claimNo){
		this.claimNo=claimNo;
	}

	public String getClaimNo(){
		return this.claimNo;
	}

	public void setClaimAmount(Double claimAmount){
		this.claimAmount=claimAmount;
	}

	public Double getClaimAmount(){
		return this.claimAmount;
	}

	public void setPercent(Integer percent){
		this.percent=percent;
	}

	public Integer getPercent(){
		return this.percent;
	}

	public void setComplainAdvice(String complainAdvice){
		this.complainAdvice=complainAdvice;
	}

	public String getComplainAdvice(){
		return this.complainAdvice;
	}

	public void setEventTheme(String eventTheme){
		this.eventTheme=eventTheme;
	}

	public String getEventTheme(){
		return this.eventTheme;
	}

	public void setDealerPhone(String dealerPhone){
		this.dealerPhone=dealerPhone;
	}

	public String getDealerPhone(){
		return this.dealerPhone;
	}

	public void setJob(String job){
		this.job=job;
	}

	public String getJob(){
		return this.job;
	}

	public void setPartNameDealer(String partNameDealer){
		this.partNameDealer=partNameDealer;
	}

	public String getPartNameDealer(){
		return this.partNameDealer;
	}

	public void setSupplyNameDealer(String supplyNameDealer){
		this.supplyNameDealer=supplyNameDealer;
	}

	public String getSupplyNameDealer(){
		return this.supplyNameDealer;
	}

	public void setSpecialId(Long specialId){
		this.specialId=specialId;
	}

	public Long getSpecialId(){
		return this.specialId;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setProblemDate(Date problemDate){
		this.problemDate=problemDate;
	}

	public Date getProblemDate(){
		return this.problemDate;
	}

	public void setSupplyCodeDealer(String supplyCodeDealer){
		this.supplyCodeDealer=supplyCodeDealer;
	}

	public String getSupplyCodeDealer(){
		return this.supplyCodeDealer;
	}

	public void setIsSupplyClaim(Integer isSupplyClaim){
		this.isSupplyClaim=isSupplyClaim;
	}

	public Integer getIsSupplyClaim(){
		return this.isSupplyClaim;
	}

	public void setIsClaim(Integer isClaim){
		this.isClaim=isClaim;
	}

	public Integer getIsClaim(){
		return this.isClaim;
	}

	public void setPartCodeDealer(String partCodeDealer){
		this.partCodeDealer=partCodeDealer;
	}

	public String getPartCodeDealer(){
		return this.partCodeDealer;
	}

	public void setTecSupplyCode(String tecSupplyCode){
		this.tecSupplyCode=tecSupplyCode;
	}

	public String getTecSupplyCode(){
		return this.tecSupplyCode;
	}

}