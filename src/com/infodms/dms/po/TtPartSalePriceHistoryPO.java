/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-14 15:20:58
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartSalePriceHistoryPO extends PO{

	private Integer state;
	private Date updateDate;
	private String remark;
	private Long createBy;
	private Integer status;
	private Long updateBy;
	private Long partId;
	private Long priceId;
	private Long deleteBy;
	private Long disableBy;
	private Double oldSalePrice9;
	private Double salePrice9;
	private Date disableDate;
	private Date deleteDate;
	private Double salePrice4;
	private Double salePrice3;
	private Double salePrice2;
	private Double salePrice1;
	private Double salePrice8;
	private Double salePrice7;
	private Double salePrice6;
	private Double salePrice5;
	private Double oldSalePrice12;
	private Double salePrice12;
	private Double oldSalePrice13;
	private Double salePrice13;
	private Double oldSalePrice10;
	private Double salePrice10;
	private Long historyId;
	private Double oldSalePrice11;
	private Double salePrice11;
	private Double oldSalePrice14;
	private Double salePrice14;
	private Double oldSalePrice15;
	private Double salePrice15;
	private Double oldSalePrice3;
	private Double oldSalePrice4;
	private Double oldSalePrice1;
	private Double oldSalePrice2;
	private Date createDate;
	private Double oldSalePrice7;
	private Double oldSalePrice8;
	private Double oldSalePrice5;
	private Double oldSalePrice6;
	private Date oldPriceValidStartDate;
	private Date oldPriceValidEndDate;
    private Date priceValidStartDate;
    private Date priceValidEndDate;
    private Date salePriceStartDate;
    private Date salePriceEndDate;
    private Date oldSalePriceStartDate;
    private Date oldSalePriceEndDate;
    
	public Date getOldSalePriceStartDate() {
        return oldSalePriceStartDate;
    }

    public void setOldSalePriceStartDate(Date oldSalePriceStartDate) {
        this.oldSalePriceStartDate = oldSalePriceStartDate;
    }

    public Date getOldSalePriceEndDate() {
        return oldSalePriceEndDate;
    }

    public void setOldSalePriceEndDate(Date oldSalePriceEndDate) {
        this.oldSalePriceEndDate = oldSalePriceEndDate;
    }

    public Date getSalePriceStartDate() {
        return salePriceStartDate;
    }

    public void setSalePriceStartDate(Date salePriceStartDate) {
        this.salePriceStartDate = salePriceStartDate;
    }

    public Date getSalePriceEndDate() {
        return salePriceEndDate;
    }

    public void setSalePriceEndDate(Date salePriceEndDate) {
        this.salePriceEndDate = salePriceEndDate;
    }

    public void setState(Integer state){
		this.state=state;
	}

	public Integer getState(){
		return this.state;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
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

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setPriceId(Long priceId){
		this.priceId=priceId;
	}

	public Long getPriceId(){
		return this.priceId;
	}

	public void setDeleteBy(Long deleteBy){
		this.deleteBy=deleteBy;
	}

	public Long getDeleteBy(){
		return this.deleteBy;
	}

	public void setDisableBy(Long disableBy){
		this.disableBy=disableBy;
	}

	public Long getDisableBy(){
		return this.disableBy;
	}

	public void setOldSalePrice9(Double oldSalePrice9){
		this.oldSalePrice9=oldSalePrice9;
	}

	public Double getOldSalePrice9(){
		return this.oldSalePrice9;
	}

	public void setSalePrice9(Double salePrice9){
		this.salePrice9=salePrice9;
	}

	public Double getSalePrice9(){
		return this.salePrice9;
	}

	public void setDisableDate(Date disableDate){
		this.disableDate=disableDate;
	}

	public Date getDisableDate(){
		return this.disableDate;
	}

	public void setDeleteDate(Date deleteDate){
		this.deleteDate=deleteDate;
	}

	public Date getDeleteDate(){
		return this.deleteDate;
	}

	public void setSalePrice4(Double salePrice4){
		this.salePrice4=salePrice4;
	}

	public Double getSalePrice4(){
		return this.salePrice4;
	}

	public void setSalePrice3(Double salePrice3){
		this.salePrice3=salePrice3;
	}

	public Double getSalePrice3(){
		return this.salePrice3;
	}

	public void setSalePrice2(Double salePrice2){
		this.salePrice2=salePrice2;
	}

	public Double getSalePrice2(){
		return this.salePrice2;
	}

	public void setSalePrice1(Double salePrice1){
		this.salePrice1=salePrice1;
	}

	public Double getSalePrice1(){
		return this.salePrice1;
	}

	public void setSalePrice8(Double salePrice8){
		this.salePrice8=salePrice8;
	}

	public Double getSalePrice8(){
		return this.salePrice8;
	}

	public void setSalePrice7(Double salePrice7){
		this.salePrice7=salePrice7;
	}

	public Double getSalePrice7(){
		return this.salePrice7;
	}

	public void setSalePrice6(Double salePrice6){
		this.salePrice6=salePrice6;
	}

	public Double getSalePrice6(){
		return this.salePrice6;
	}

	public void setSalePrice5(Double salePrice5){
		this.salePrice5=salePrice5;
	}

	public Double getSalePrice5(){
		return this.salePrice5;
	}

	public void setOldSalePrice12(Double oldSalePrice12){
		this.oldSalePrice12=oldSalePrice12;
	}

	public Double getOldSalePrice12(){
		return this.oldSalePrice12;
	}

	public void setSalePrice12(Double salePrice12){
		this.salePrice12=salePrice12;
	}

	public Double getSalePrice12(){
		return this.salePrice12;
	}

	public void setOldSalePrice13(Double oldSalePrice13){
		this.oldSalePrice13=oldSalePrice13;
	}

	public Double getOldSalePrice13(){
		return this.oldSalePrice13;
	}

	public void setSalePrice13(Double salePrice13){
		this.salePrice13=salePrice13;
	}

	public Double getSalePrice13(){
		return this.salePrice13;
	}

	public void setOldSalePrice10(Double oldSalePrice10){
		this.oldSalePrice10=oldSalePrice10;
	}

	public Double getOldSalePrice10(){
		return this.oldSalePrice10;
	}

	public void setSalePrice10(Double salePrice10){
		this.salePrice10=salePrice10;
	}

	public Double getSalePrice10(){
		return this.salePrice10;
	}

	public void setHistoryId(Long historyId){
		this.historyId=historyId;
	}

	public Long getHistoryId(){
		return this.historyId;
	}

	public void setOldSalePrice11(Double oldSalePrice11){
		this.oldSalePrice11=oldSalePrice11;
	}

	public Double getOldSalePrice11(){
		return this.oldSalePrice11;
	}

	public void setSalePrice11(Double salePrice11){
		this.salePrice11=salePrice11;
	}

	public Double getSalePrice11(){
		return this.salePrice11;
	}

	public void setOldSalePrice14(Double oldSalePrice14){
		this.oldSalePrice14=oldSalePrice14;
	}

	public Double getOldSalePrice14(){
		return this.oldSalePrice14;
	}

	public void setSalePrice14(Double salePrice14){
		this.salePrice14=salePrice14;
	}

	public Double getSalePrice14(){
		return this.salePrice14;
	}

	public void setOldSalePrice15(Double oldSalePrice15){
		this.oldSalePrice15=oldSalePrice15;
	}

	public Double getOldSalePrice15(){
		return this.oldSalePrice15;
	}

	public void setSalePrice15(Double salePrice15){
		this.salePrice15=salePrice15;
	}

	public Double getSalePrice15(){
		return this.salePrice15;
	}

	public void setOldSalePrice3(Double oldSalePrice3){
		this.oldSalePrice3=oldSalePrice3;
	}

	public Double getOldSalePrice3(){
		return this.oldSalePrice3;
	}

	public void setOldSalePrice4(Double oldSalePrice4){
		this.oldSalePrice4=oldSalePrice4;
	}

	public Double getOldSalePrice4(){
		return this.oldSalePrice4;
	}

	public void setOldSalePrice1(Double oldSalePrice1){
		this.oldSalePrice1=oldSalePrice1;
	}

	public Double getOldSalePrice1(){
		return this.oldSalePrice1;
	}

	public void setOldSalePrice2(Double oldSalePrice2){
		this.oldSalePrice2=oldSalePrice2;
	}

	public Double getOldSalePrice2(){
		return this.oldSalePrice2;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setOldSalePrice7(Double oldSalePrice7){
		this.oldSalePrice7=oldSalePrice7;
	}

	public Double getOldSalePrice7(){
		return this.oldSalePrice7;
	}

	public void setOldSalePrice8(Double oldSalePrice8){
		this.oldSalePrice8=oldSalePrice8;
	}

	public Double getOldSalePrice8(){
		return this.oldSalePrice8;
	}

	public void setOldSalePrice5(Double oldSalePrice5){
		this.oldSalePrice5=oldSalePrice5;
	}

	public Double getOldSalePrice5(){
		return this.oldSalePrice5;
	}

	public void setOldSalePrice6(Double oldSalePrice6){
		this.oldSalePrice6=oldSalePrice6;
	}

	public Double getOldSalePrice6(){
		return this.oldSalePrice6;
	}

    public Date getOldPriceValidStartDate() {
        return oldPriceValidStartDate;
    }

    public void setOldPriceValidStartDate(Date oldPriceValidStartDate) {
        this.oldPriceValidStartDate = oldPriceValidStartDate;
    }

    public Date getOldPriceValidEndDate() {
        return oldPriceValidEndDate;
    }

    public void setOldPriceValidEndDate(Date oldPriceValidEndDate) {
        this.oldPriceValidEndDate = oldPriceValidEndDate;
    }

    public Date getPriceValidStartDate() {
        return priceValidStartDate;
    }

    public void setPriceValidStartDate(Date priceValidStartDate) {
        this.priceValidStartDate = priceValidStartDate;
    }

    public Date getPriceValidEndDate() {
        return priceValidEndDate;
    }

    public void setPriceValidEndDate(Date priceValidEndDate) {
        this.priceValidEndDate = priceValidEndDate;
    }

	
}