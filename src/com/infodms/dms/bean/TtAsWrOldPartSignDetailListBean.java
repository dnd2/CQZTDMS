package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class TtAsWrOldPartSignDetailListBean extends PO{
	
	private static final long serialVersionUID = 2981241886425625062L;
	
	private Long id;//回运清单明细主键
	private String claim_no;//索赔申请单号
	private String vin;//vin
	private String part_code;//配件代码
	private String part_name;//配件名称
	private String producer_code;//供应商代码
	private Integer return_amount;//回运数
	private Integer sign_amount;//签收数
	private String box_no;//装箱单号
	private String warehouse_region;//库区
	private Integer deduct_remark;//抵扣原因
	private String deduct_desc;
	private String localWarHouse;
	private String other_remark;
	private String is_in_house;
	private String in_warhouse_status;
	private String in_warhouse_status_name;
	
	
	
	public String getIn_warhouse_status() {
		return in_warhouse_status;
	}
	public void setIn_warhouse_status(String in_warhouse_status) {
		this.in_warhouse_status = in_warhouse_status;
	}
	public String getIn_warhouse_status_name() {
		return in_warhouse_status_name;
	}
	public void setIn_warhouse_status_name(String in_warhouse_status_name) {
		this.in_warhouse_status_name = in_warhouse_status_name;
	}
	public String getOther_remark() {
		return other_remark;
	}
	public void setOther_remark(String other_remark) {
		this.other_remark = other_remark;
	}
	public String getLocalWarHouse() {
		return localWarHouse;
	}
	public void setLocalWarHouse(String localWarHouse) {
		this.localWarHouse = localWarHouse;
	}
	public Long getId() {
		return id;
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

	public String getPart_code() {
		return part_code;
	}
	public void setPart_code(String part_code) {
		this.part_code = part_code;
	}
	public String getPart_name() {
		return part_name;
	}
	public void setPart_name(String part_name) {
		this.part_name = part_name;
	}
	public Integer getReturn_amount() {
		return return_amount;
	}
	public void setReturn_amount(Integer return_amount) {
		this.return_amount = return_amount;
	}
	public Integer getSign_amount() {
		return sign_amount;
	}
	public void setSign_amount(Integer sign_amount) {
		this.sign_amount = sign_amount;
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
	public Integer getDeduct_remark() {
		return deduct_remark;
	}
	public void setDeduct_remark(Integer deduct_remark) {
		this.deduct_remark = deduct_remark;
	}
	public String getDeduct_desc() {
		return deduct_desc;
	}
	public void setDeduct_desc(String deduct_desc) {
		this.deduct_desc = deduct_desc;
	}
	public String getProducer_code() {
		return producer_code;
	}
	public void setProducer_code(String producer_code) {
		this.producer_code = producer_code;
	}
	public String getIs_in_house() {
		return is_in_house;
	}
	public void setIs_in_house(String is_in_house) {
		this.is_in_house = is_in_house;
	}
}
