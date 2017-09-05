/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-07-01 13:44:15
* CreateBy   : Yu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartTransPlanPO extends PO{

	private Integer state;
	private Long outId;
	private Long dealerId;
	private Date updateDate;
	private Long createBy;
	private String trplanCode;
	private Integer status;
	private String transType;
	private Long printBy;
	private Date printDate;
	private Long updateBy;
	private String otwDes;
	private String remark1;
	private String remark2;
	private String dealerCode;
	private String arrDate;
	private Long pickOrderId;
	private Long trplanId;
	private Long whId;
	private Date arrDate2;
	private String transportOrg;
	private String dealerName;
	private Long addrId;
	private Integer printNum;
	private Long sellerId;
	private Integer ver;
	private Date createDate;
	private Double transSumprice;
	

	public Double getTransSumprice() {
		return transSumprice;
	}

	public void setTransSumprice(Double transSumprice) {
		this.transSumprice = transSumprice;
	}

	public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setOutId(Long outId){
		this.outId=outId;
	}

	public Long getOutId(){
		return this.outId;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
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

	public void setTrplanCode(String trplanCode){
		this.trplanCode=trplanCode;
	}

	public String getTrplanCode(){
		return this.trplanCode;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setTransType(String transType){
		this.transType=transType;
	}

	public String getTransType(){
		return this.transType;
	}

	public void setPrintBy(Long printBy){
		this.printBy=printBy;
	}

	public Long getPrintBy(){
		return this.printBy;
	}

	public void setPrintDate(Date printDate){
		this.printDate=printDate;
	}

	public Date getPrintDate(){
		return this.printDate;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setOtwDes(String otwDes){
		this.otwDes=otwDes;
	}

	public String getOtwDes(){
		return this.otwDes;
	}

	public void setRemark1(String remark1){
		this.remark1=remark1;
	}

	public String getRemark1(){
		return this.remark1;
	}

	public void setRemark2(String remark2){
		this.remark2=remark2;
	}

	public String getRemark2(){
		return this.remark2;
	}

	public void setDealerCode(String dealerCode){
		this.dealerCode=dealerCode;
	}

	public String getDealerCode(){
		return this.dealerCode;
	}

	public void setArrDate(String arrDate){
		this.arrDate=arrDate;
	}

	public String getArrDate(){
		return this.arrDate;
	}

	public void setPickOrderId(Long pickOrderId){
		this.pickOrderId=pickOrderId;
	}

	public Long getPickOrderId(){
		return this.pickOrderId;
	}

	public void setTrplanId(Long trplanId){
		this.trplanId=trplanId;
	}

	public Long getTrplanId(){
		return this.trplanId;
	}

	public void setWhId(Long whId){
		this.whId=whId;
	}

	public Long getWhId(){
		return this.whId;
	}

	public void setArrDate2(Date arrDate2){
		this.arrDate2=arrDate2;
	}

	public Date getArrDate2(){
		return this.arrDate2;
	}

	public void setTransportOrg(String transportOrg){
		this.transportOrg=transportOrg;
	}

	public String getTransportOrg(){
		return this.transportOrg;
	}

	public void setDealerName(String dealerName){
		this.dealerName=dealerName;
	}

	public String getDealerName(){
		return this.dealerName;
	}

	public void setAddrId(Long addrId){
		this.addrId=addrId;
	}

	public Long getAddrId(){
		return this.addrId;
	}

	public void setPrintNum(Integer printNum){
		this.printNum=printNum;
	}

	public Integer getPrintNum(){
		return this.printNum;
	}

	public void setSellerId(Long sellerId){
		this.sellerId=sellerId;
	}

	public Long getSellerId(){
		return this.sellerId;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

}