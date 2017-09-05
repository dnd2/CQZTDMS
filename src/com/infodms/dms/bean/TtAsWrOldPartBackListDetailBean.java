package com.infodms.dms.bean;


import com.infoservice.po3.bean.PO;

public class TtAsWrOldPartBackListDetailBean extends PO{

	private static final long serialVersionUID = 8785353852199414450L;
	private Long id;//回运清单主键
	private String return_no;//回运清单号
	private String return_date;//提报日期
	private Integer wr_amount;//索赔单数
	private Integer part_item_amount;//回运配件项数
	private Integer part_amount;//配件数
	private Integer parkage_amount;//装箱总数
	private Integer return_type;//回运类型
	private String return_desc;//回运类型解释
	private Integer status;//处理代码
	private String status_desc;//处理解释
	private String transport_no;//货运单号
	private Integer transport_type;//货运方式代码
	private String transport_desc;//货运方式解释
	private String create_date;//创建日期
	private String creator;//建单人
	private String tran_no;
	private String sendDate;//发运日期
	private String arrive_date;//预计到货日期
	private String wr_start_date;
	private String tel;
	private String remark;
	private String transport_remark;
	private String transport_company;
	private String sign_remark;
	private String transport_id;
	private String transport_name;
	private String price;
	private String authPrice;
	private String priceRemark;
	
	
	public String getTransport_company() {
		return transport_company;
	}
	public void setTransport_company(String transport_company) {
		this.transport_company = transport_company;
	}
	public String getPriceRemark() {
		return priceRemark;
	}
	public void setPriceRemark(String priceRemark) {
		this.priceRemark = priceRemark;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAuthPrice() {
		return authPrice;
	}
	public void setAuthPrice(String authPrice) {
		this.authPrice = authPrice;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getWr_start_date() {
		return wr_start_date;
	}
	public void setWr_start_date(String wrStartDate) {
		wr_start_date = wrStartDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReturn_no() {
		return return_no;
	}
	public void setReturn_no(String return_no) {
		this.return_no = return_no;
	}
	public String getReturn_date() {
		return return_date;
	}
	public void setReturn_date(String return_date) {
		this.return_date = return_date;
	}
	public Integer getWr_amount() {
		return wr_amount;
	}
	public void setWr_amount(Integer wr_amount) {
		this.wr_amount = wr_amount;
	}
	public Integer getPart_item_amount() {
		return part_item_amount;
	}
	public void setPart_item_amount(Integer part_item_amount) {
		this.part_item_amount = part_item_amount;
	}
	public Integer getPart_amount() {
		return part_amount;
	}
	public void setPart_amount(Integer part_amount) {
		this.part_amount = part_amount;
	}
	public Integer getParkage_amount() {
		return parkage_amount;
	}
	public void setParkage_amount(Integer parkage_amount) {
		this.parkage_amount = parkage_amount;
	}
	public Integer getReturn_type() {
		return return_type;
	}
	public void setReturn_type(Integer return_type) {
		this.return_type = return_type;
	}
	public String getReturn_desc() {
		return return_desc;
	}
	public void setReturn_desc(String return_desc) {
		this.return_desc = return_desc;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStatus_desc() {
		return status_desc;
	}
	public void setStatus_desc(String status_desc) {
		this.status_desc = status_desc;
	}
	public Integer getTransport_type() {
		return transport_type;
	}
	public void setTransport_type(Integer transport_type) {
		this.transport_type = transport_type;
	}
	public String getTransport_desc() {
		return transport_desc;
	}
	public void setTransport_desc(String transport_desc) {
		this.transport_desc = transport_desc;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getTran_no() {
		return tran_no;
	}
	public void setTran_no(String tran_no) {
		this.tran_no = tran_no;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getArrive_date() {
		return arrive_date;
	}
	public void setArrive_date(String arrive_date) {
		this.arrive_date = arrive_date;
	}
	public String getTransport_id() {
		return transport_id;
	}
	public void setTransport_id(String transport_id) {
		this.transport_id = transport_id;
	}
	public String getTransport_name() {
		return transport_name;
	}
	public void setTransport_name(String transport_name) {
		this.transport_name = transport_name;
	}
	public String getSign_remark() {
		return sign_remark;
	}
	public void setSign_remark(String sign_remark) {
		this.sign_remark = sign_remark;
	}
	public String getTransport_remark() {
		return transport_remark;
	}
	public void setTransport_remark(String transport_remark) {
		this.transport_remark = transport_remark;
	}
	public String getTransport_no() {
		return transport_no;
	}
	public void setTransport_no(String transport_no) {
		this.transport_no = transport_no;
	}
	
}
