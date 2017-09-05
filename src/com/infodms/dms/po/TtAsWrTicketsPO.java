/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-30 17:52:19
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsWrTicketsPO extends PO{

	private Long balanceYieldly;
	private Integer aplcount;
	private String claimType;
	private Long dealerid;
	private String numberAp;
	private Double daCarriage;
	private Long letter;
	private String goodsnum;
	private Long lettersf;
	private String dealername;
	private Date startdate;
	private Double carriage;
	private Long id;
	private Double sumCarriage;
	private Date enddate;

	public void setBalanceYieldly(Long balanceYieldly){
		this.balanceYieldly=balanceYieldly;
	}

	public Long getBalanceYieldly(){
		return this.balanceYieldly;
	}

	public void setAplcount(Integer aplcount){
		this.aplcount=aplcount;
	}

	public Integer getAplcount(){
		return this.aplcount;
	}

	public void setClaimType(String claimType){
		this.claimType=claimType;
	}

	public String getClaimType(){
		return this.claimType;
	}

	public void setDealerid(Long dealerid){
		this.dealerid=dealerid;
	}

	public Long getDealerid(){
		return this.dealerid;
	}

	public void setNumberAp(String numberAp){
		this.numberAp=numberAp;
	}

	public String getNumberAp(){
		return this.numberAp;
	}

	public void setDaCarriage(Double daCarriage){
		this.daCarriage=daCarriage;
	}

	public Double getDaCarriage(){
		return this.daCarriage;
	}

	public void setLetter(Long letter){
		this.letter=letter;
	}

	public Long getLetter(){
		return this.letter;
	}

	public void setGoodsnum(String goodsnum){
		this.goodsnum=goodsnum;
	}

	public String getGoodsnum(){
		return this.goodsnum;
	}

	public void setLettersf(Long lettersf){
		this.lettersf=lettersf;
	}

	public Long getLettersf(){
		return this.lettersf;
	}

	public void setDealername(String dealername){
		this.dealername=dealername;
	}

	public String getDealername(){
		return this.dealername;
	}

	public void setStartdate(Date startdate){
		this.startdate=startdate;
	}

	public Date getStartdate(){
		return this.startdate;
	}

	public void setCarriage(Double carriage){
		this.carriage=carriage;
	}

	public Double getCarriage(){
		return this.carriage;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setSumCarriage(Double sumCarriage){
		this.sumCarriage=sumCarriage;
	}

	public Double getSumCarriage(){
		return this.sumCarriage;
	}

	public void setEnddate(Date enddate){
		this.enddate=enddate;
	}

	public Date getEnddate(){
		return this.enddate;
	}

}