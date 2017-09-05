package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartOutstockDtlPO extends PO{

	private Long packgQty;
	private Long outId;
	private Long slineId;
	private Date updateDate;
	private Long createBy;
	private String remark;
	private Integer status;
	private Integer isLack;
	private Double salePrice;
	private Long reservedQty;
	private Long updateBy;
	private Integer isReplaced;
	private Long minPackage;
	private Long partId;
	private Long deleteBy;
	private Long disableBy;
	private Long outlineId;
	private Long buyQty;
	private String unit;
	private Date disableDate;
	private Date deleteDate;
	private Long solineId;
	private Long soId;
	private Long boQty;
	private Integer isPlan;
	private String partCode;
	private Long stockQty;
	private String partCname;
	private Integer isGift;
	private Long outstockQty;
	private Double saleAmount;
	private Integer ver;
	private Integer isDirect;
	private String partOldcode;
	private Date createDate;
	private Long lineNo;
	private Long locId;
	private String batchNo;

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Long getLocId() {
		return locId;
	}

	public void setLocId(Long locId) {
		this.locId = locId;
	}

	public void setPackgQty(Long packgQty){
		this.packgQty=packgQty;
	}

	public Long getPackgQty(){
		return this.packgQty;
	}

	public void setOutId(Long outId){
		this.outId=outId;
	}

	public Long getOutId(){
		return this.outId;
	}

	public void setSlineId(Long slineId){
		this.slineId=slineId;
	}

	public Long getSlineId(){
		return this.slineId;
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

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setIsLack(Integer isLack){
		this.isLack=isLack;
	}

	public Integer getIsLack(){
		return this.isLack;
	}

	public void setSalePrice(Double salePrice){
		this.salePrice=salePrice;
	}

	public Double getSalePrice(){
		return this.salePrice;
	}

	public void setReservedQty(Long reservedQty){
		this.reservedQty=reservedQty;
	}

	public Long getReservedQty(){
		return this.reservedQty;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setIsReplaced(Integer isReplaced){
		this.isReplaced=isReplaced;
	}

	public Integer getIsReplaced(){
		return this.isReplaced;
	}

	public void setMinPackage(Long minPackage){
		this.minPackage=minPackage;
	}

	public Long getMinPackage(){
		return this.minPackage;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
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

	public void setOutlineId(Long outlineId){
		this.outlineId=outlineId;
	}

	public Long getOutlineId(){
		return this.outlineId;
	}

	public void setBuyQty(Long buyQty){
		this.buyQty=buyQty;
	}

	public Long getBuyQty(){
		return this.buyQty;
	}

	public void setUnit(String unit){
		this.unit=unit;
	}

	public String getUnit(){
		return this.unit;
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

	public void setSolineId(Long solineId){
		this.solineId=solineId;
	}

	public Long getSolineId(){
		return this.solineId;
	}

	public void setSoId(Long soId){
		this.soId=soId;
	}

	public Long getSoId(){
		return this.soId;
	}

	public void setBoQty(Long boQty){
		this.boQty=boQty;
	}

	public Long getBoQty(){
		return this.boQty;
	}

	public void setIsPlan(Integer isPlan){
		this.isPlan=isPlan;
	}

	public Integer getIsPlan(){
		return this.isPlan;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setStockQty(Long stockQty){
		this.stockQty=stockQty;
	}

	public Long getStockQty(){
		return this.stockQty;
	}

	public void setPartCname(String partCname){
		this.partCname=partCname;
	}

	public String getPartCname(){
		return this.partCname;
	}

	public void setIsGift(Integer isGift){
		this.isGift=isGift;
	}

	public Integer getIsGift(){
		return this.isGift;
	}

	public void setOutstockQty(Long outstockQty){
		this.outstockQty=outstockQty;
	}

	public Long getOutstockQty(){
		return this.outstockQty;
	}

	public void setSaleAmount(Double saleAmount){
		this.saleAmount=saleAmount;
	}

	public Double getSaleAmount(){
		return this.saleAmount;
	}

	public void setVer(Integer ver){
		this.ver=ver;
	}

	public Integer getVer(){
		return this.ver;
	}

	public void setIsDirect(Integer isDirect){
		this.isDirect=isDirect;
	}

	public Integer getIsDirect(){
		return this.isDirect;
	}

	public void setPartOldcode(String partOldcode){
		this.partOldcode=partOldcode;
	}

	public String getPartOldcode(){
		return this.partOldcode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setLineNo(Long lineNo){
		this.lineNo=lineNo;
	}

	public Long getLineNo(){
		return this.lineNo;
	}

}