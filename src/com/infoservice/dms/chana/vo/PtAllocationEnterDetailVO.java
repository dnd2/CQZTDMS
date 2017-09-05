package com.infoservice.dms.chana.vo;

/**
 * 
 * @ClassName : PtAllocationEnterDetailVO
 * @Description : 配件调拨入库明细VO
 * @author : wangjun 
 * @CreateDate : 2013-5-28
 */

@SuppressWarnings("serial")
public class PtAllocationEnterDetailVO extends BaseVO{
	private String partNo;//上端：配件代码 VARCHAR(27) 下端：
	private String partName;//上端：配件名称 VARCHAR(120) 下端：
	private String unitName;//上端：计量单位名称 VARCHAR(12) 下端：
	private Double allocationQty;//上端：调拨数量 NUMERIC(12,2)下端：
	private String storageCode;//上端：仓库代码 VARCHAR(4) 下端：
	private String storageName;//上端：仓库名称 VARCHAR(30) 下端：
	private String storagePositionCode;//上端：库位代码 VARCHAR(30) 下端：
	private String storagePositionName;//上端：库位名称 VARCHAR(30) 下端：
	private Double inPrice;//上端：调拨不含税单价 NUMERIC(12,4) 默认为空 经销商在店面系统自己录入 下端：
	private Integer sort;//上端：排序 NUMERIC(8) 下端：
	public String getPartNo() {
		return partNo;
	}
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Double getAllocationQty() {
		return allocationQty;
	}
	public void setAllocationQty(Double allocationQty) {
		this.allocationQty = allocationQty;
	}
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}
	public String getStorageName() {
		return storageName;
	}
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	public String getStoragePositionCode() {
		return storagePositionCode;
	}
	public void setStoragePositionCode(String storagePositionCode) {
		this.storagePositionCode = storagePositionCode;
	}
	public String getStoragePositionName() {
		return storagePositionName;
	}
	public void setStoragePositionName(String storagePositionName) {
		this.storagePositionName = storagePositionName;
	}
	public Double getInPrice() {
		return inPrice;
	}
	public void setInPrice(Double inPrice) {
		this.inPrice = inPrice;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}
