package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class TtAsWrOldPartDetailListBean extends PO{
	private static final long serialVersionUID = 3055907093826532281L;
    
	private Long id;//回运清单明细主键
	private String claim_no;//索赔申请单号
	private String claim_type;//索赔类型
	private String vin;//vin
	private Long part_id;//配件编号
	private String part_code;//配件代码
	private String part_name;//配件名称
	private Integer n_return_amount;//需回运数
	private Integer return_amount;//回运数
	private Integer sign_amount;//签收数
	private String box_no;//装箱单号
	private String warehouse_region;//库区
	private String deduct_remark;//抵扣原因
	private String proc_factory;
	private String barcode_no;
	private String in_warhouse_date;//审核入库时间
	private String report_date;
	private String other_remark;
	
	public String getOther_remark() {
		return other_remark;
	}
	public void setOther_remark(String other_remark) {
		this.other_remark = other_remark;
	}
	public String getReport_date() {
		return report_date;
	}
	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}
	public Long getId() {
		return id;
	}
	public String getBarcode_no() {
		return barcode_no;
	}
	public void setBarcode_no(String barcodeNo) {
		barcode_no = barcodeNo;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getClaim_no() {
		return claim_no;
	}
	public void setClaim_no(String claim_no) {
		this.claim_no = claim_no;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Long getPart_id() {
		return part_id;
	}
	public void setPart_id(Long part_id) {
		this.part_id = part_id;
	}
	public String getPart_name() {
		return part_name;
	}
	public void setPart_name(String part_name) {
		this.part_name = part_name;
	}
	public Integer getN_return_amount() {
		return n_return_amount;
	}
	public void setN_return_amount(Integer n_return_amount) {
		this.n_return_amount = n_return_amount;
	}
	public Integer getReturn_amount() {
		return return_amount;
	}
	public void setReturn_amount(Integer return_amount) {
		this.return_amount = return_amount;
	}
	public String getBox_no() {
		return box_no;
	}
	public void setBox_no(String box_no) {
		this.box_no = box_no;
	}
	public String getWarehouse_region() {
		return warehouse_region;
	}
	public void setWarehouse_region(String warehouse_region) {
		this.warehouse_region = warehouse_region;
	}
	public String getDeduct_remark() {
		return deduct_remark;
	}
	public void setDeduct_remark(String deduct_remark) {
		this.deduct_remark = deduct_remark;
	}
	public String getPart_code() {
		return part_code;
	}
	public void setPart_code(String part_code) {
		this.part_code = part_code;
	}
	public String getProc_factory() {
		return proc_factory;
	}
	public void setProc_factory(String proc_factory) {
		this.proc_factory = proc_factory;
	}
	public Integer getSign_amount() {
		return sign_amount;
	}
	public void setSign_amount(Integer sign_amount) {
		this.sign_amount = sign_amount;
	}
	public String getClaim_type() {
		return claim_type;
	}
	public void setClaim_type(String claim_type) {
		this.claim_type = claim_type;
	}
	public String getIn_warhouse_date() {
		return in_warhouse_date;
	}
	public void setIn_warhouse_date(String in_warhouse_date) {
		this.in_warhouse_date = in_warhouse_date;
	}
	
}
