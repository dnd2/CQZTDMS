package com.infodms.dms.bean;

import java.util.Date;

import com.infodms.dms.po.TtCustomerPO;

public class TtRelationBean extends TtCustomerPO {
	private Long ctmId;			//客户编号
	public Long getCtmId() {
		return ctmId;
	}
	public void setCtmId(Long ctmId) {
		this.ctmId = ctmId;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getCtmName() {
		return ctmName;
	}
	public void setCtmName(String ctmName) {
		this.ctmName = ctmName;
	}
	public String getMainPhone() {
		return mainPhone;
	}
	public void setMainPhone(String mainPhone) {
		this.mainPhone = mainPhone;
	}
	public Integer getCtmType() {
		return ctmType;
	}
	public void setCtmType(Integer ctmType) {
		this.ctmType = ctmType;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getGuestStars() {
		return guestStars;
	}
	public void setGuestStars(String guestStars) {
		this.guestStars = guestStars;
	}
	public String getMaterialId() {
		return materialId;
	}
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public Date getDatestart() {
		return datestart;
	}
	public void setDatestart(Date datestart) {
		this.datestart = datestart;
	}
	public Date getDateend() {
		return dateend;
	}
	public void setDateend(Date dateend) {
		this.dateend = dateend;
	}
	public String getYieldly() {
		return yieldly;
	}
	public void setYieldly(String yieldly) {
		this.yieldly = yieldly;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Date getPurchasedDate() {
		return purchasedDate;
	}
	public void setPurchasedDate(Date purchasedDate) {
		this.purchasedDate = purchasedDate;
	}
	public String getSeriesCode() {
		return seriesCode;
	}
	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	private String cardNum;
	private String ctmName;		//客户姓名
	private String mainPhone;	//客户电话
	private Integer ctmType;	//客户类型
	private Integer sex;		//客户性别
	private String guestStars;	//客户星级
	private String materialId;	//物料ID
	private String materialCode;//物料编码
	private String materialName;//物料名称
	private Date datestart;		//购车日期起
	private Date dateend;		//购车日期止
	private String yieldly;		//生产基地
	private String vin;			//VIN
	private Date purchasedDate;	//购车时间
	private String seriesCode;	//车系
	private String modelCode;	//车型
}
