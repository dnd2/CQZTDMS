package com.infoservice.dms.chana.vo;

import java.util.Date;

/**
 * 
 * @ClassName     : PtAllocationStreamVO 
 * @Description   : OEM库房流水上传VO
 * @author        : luole
 * CreateDate     : 2013-5-28
 */
@SuppressWarnings("serial")
public class PtAllocationStreamVO extends BaseVO{
	private Long flowId;       		// 下端：流水号	  NUMERIC(14)   上端：FLOW_ID
	private String entityCode; 		// 下端：经销商CODE VARCHAR(30)   上端：ENTITY_CODE
	private String storageCode;		// 下端：仓库代码     VARCHAR(4)   上端：STORAGE_CODE 
	private String partNo;     		// 下端：配件代码      VARCHAR(27)  上端：PART_NO
	private String partName;   		// 下端：配件名称      VARCHAR(120)  上端：PART_NAME
	private String sheetNo;   		// 下端：单据号码      VARCHAR(12) 上端：SHEET_NO	
	private Integer inOutType;  	// 下端：出入库类型   NUMERIC(8)  上端：IN_OUT_TYPE
	private Integer inOutTag;   	// 下端：是否出库       NUMERIC(8)  上端：IN_OUT_TAG    备注IN_OUT_TAG  12781001 出库    12781002 入库
	private Double stockInQuantity; // 下端：进数量	   NUMERIC(8,2) 上端：STOCK_IN_QUANTITY	
	private Double stockOutQuantity;// 下端：出数量	   NUMERIC(8,2) 上端：STOCK_OUT_QUANTITY	
	private Double stockQuantity;	// 下端：库存数量       NUMERIC(8,2)  上端：STOCK_QUANTITY	
	private String operator;   		// 下端：操作员	   VARCHAR(4)  上端：OPERATOR
	private Date operateDate;  		// 下端：发生日期         TIMESTAMP  上端：OPERATE_DATE
	private Long repairPartId;	// 下端：维修领料明细ID  NUMERIC(14)  上端：REPAIR_PART_ID
	private Double costAmount; 		// 下端：成本金额        NUMERIC(12,4)  上端：COST_AMOUNT	
	public Long getFlowId() {
		return flowId;
	}
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
	public String getEntityCode() {
		return entityCode;
	}
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}
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
	public String getSheetNo() {
		return sheetNo;
	}
	public void setSheetNo(String sheetNo) {
		this.sheetNo = sheetNo;
	}
	public Integer getInOutType() {
		return inOutType;
	}
	public void setInOutType(Integer inOutType) {
		this.inOutType = inOutType;
	}
	public Integer getInOutTag() {
		return inOutTag;
	}
	public void setInOutTag(Integer inOutTag) {
		this.inOutTag = inOutTag;
	}
	public Double getStockInQuantity() {
		return stockInQuantity;
	}
	public void setStockInQuantity(Double stockInQuantity) {
		this.stockInQuantity = stockInQuantity;
	}
	public Double getStockOutQuantity() {
		return stockOutQuantity;
	}
	public void setStockOutQuantity(Double stockOutQuantity) {
		this.stockOutQuantity = stockOutQuantity;
	}
	public Double getStockQuantity() {
		return stockQuantity;
	}
	public void setStockQuantity(Double stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}
	public Long getRepairPartId() {
		return repairPartId;
	}
	public void setRepairPartId(Long repairPartId) {
		this.repairPartId = repairPartId;
	}
	public Double getCostAmount() {
		return costAmount;
	}
	public void setCostAmount(Double costAmount) {
		this.costAmount = costAmount;
	}
	
}
