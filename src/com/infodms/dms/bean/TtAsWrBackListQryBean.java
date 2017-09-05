package com.infodms.dms.bean;

import java.util.Date;

import com.infoservice.po3.bean.PO;

public class TtAsWrBackListQryBean extends PO{

	private static final long serialVersionUID = -4707976763548118171L;
	private Long id;//回运清单主键
	private String return_no;//回运清单号
	private Date create_date;//创建日期
	private Date return_date;//提报日期
	private Integer wr_amount;//索赔单数
	private Integer part_item_amount;//回运配件项数
	private String dealer_shortname;
	private Date arrive_date;
	private String borrow_person;
	private String borrow_phone;
	private String borrow_no;
	private Date require_date;
	private Long  is_delay;
	private Long  re_status;
	
	
	public Long getRe_status() {
		return re_status;
	}
	public void setRe_status(Long re_status) {
		this.re_status = re_status;
	}
	public Long getIs_delay() {
		return is_delay;
	}
	public void setIs_delay(Long is_delay) {
		this.is_delay = is_delay;
	}
	public Date getRequire_date() {
		return require_date;
	}
	public void setRequire_date(Date require_date) {
		this.require_date = require_date;
	}
	private Integer part_amount;//配件数
	private String yieldlyName;
	private Integer return_type;
	private String sign_name;
	public String getSign_name() {
		return sign_name;
	}
	public void setSign_name(String sign_name) {
		this.sign_name = sign_name;
	}
	public Integer getReturn_type() {
		return return_type;
	}
	public void setReturn_type(Integer return_type) {
		this.return_type = return_type;
	}
	public String getYieldlyName() {
		return yieldlyName;
	}
	public void setYieldlyName(String yieldlyName) {
		this.yieldlyName = yieldlyName;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getAuth_price() {
		return auth_price;
	}
	public void setAuth_price(Integer authPrice) {
		auth_price = authPrice;
	}
	private Integer parkage_amount;//装箱总数
	
	private Long return_id;//物流单主键
	private String box_no;//装箱单号
	private String old_no;//物流单号
	private String old_status;//物流单状态
	
	private Integer price;
	private Integer auth_price;
	public String getBorrow_no() {
		return borrow_no;
	}
	public void setBorrow_no(String borrow_no) {
		this.borrow_no = borrow_no;
	}
	public String getBorrow_person() {
		return borrow_person;
	}
	public void setBorrow_person(String borrow_person) {   
		this.borrow_person = borrow_person;
	}
	public String getBorrow_phone() {
		return borrow_phone;
	}
	public void setBorrow_phone(String borrow_phone) {
		this.borrow_phone = borrow_phone;
	}
	public Date getArrive_date() {
		return arrive_date;
	}
	public void setArrive_date(Date arriveDate) {
		arrive_date = arriveDate;
	}
	public String getDealer_shortname() {
		return dealer_shortname;
	}
	public void setDealer_shortname(String dealer_shortname) {
		this.dealer_shortname = dealer_shortname;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getAuth_person_name() {
		return auth_person_name;
	}
	public void setAuth_person_name(String authPersonName) {
		auth_person_name = authPersonName;
	}
	private String auth_person_name;
	
	public String getOld_status() {
		return old_status;
	}
	public void setOld_status(String oldStatus) {
		old_status = oldStatus;
	}
	public String getOld_no() {
		return old_no;
	}
	public void setOld_no(String oldNo) {
		old_no = oldNo;
	}
	public String getBox_no() {
		return box_no;
	}
	public void setBox_no(String boxNo) {
		box_no = boxNo;
	}
	public Long getReturn_id() {
		return return_id;
	}
	public void setReturn_id(Long returnId) {
		return_id = returnId;
	}
	/*************Iverson By 2010-11-02*************/
	private String wr_start_date;//开始到结束时间
	public String getWr_start_date() {
		return wr_start_date;
	}
	public void setWr_start_date(String wrStartDate) {
		wr_start_date = wrStartDate;
	}
	/*************Iverson By 2010-11-02 end*************/
	private String status_desc;//处理状态解释
	private Integer status;//处理状态
	private String freight_type;//货运方式解释
	private String yieldly;//产地
	private String dealer_id;//经销商ID
	private String dealer_name;//经销商名称
	private String dealer_level;//经销商级别;
	private String self_dealer_id;//当前用户经销商ID
	
	/*************Iverson By 2010-11-08*************/
	private String box_number;//
	private String tranNo;//
	
	public String getTranNo() {
		return tranNo;
	}
	public void setTranNo(String tranNo) {
		this.tranNo = tranNo;
	}
	public String getBox_number() {
		return box_number;
	}
	public void setBox_number(String boxNumber) {
		box_number = boxNumber;
	}
	/*************Iverson By 2010-11-08 end*************/
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
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Date getReturn_date() {
		return return_date;
	}
	public void setReturn_date(Date return_date) {
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
	public String getStatus_desc() {
		return status_desc;
	}
	public void setStatus_desc(String status_desc) {
		this.status_desc = status_desc;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getFreight_type() {
		return freight_type;
	}
	public void setFreight_type(String freight_type) {
		this.freight_type = freight_type;
	}
	public String getYieldly() {
		return yieldly;
	}
	public void setYieldly(String yieldly) {
		this.yieldly = yieldly;
	}
	public String getDealer_name() {
		return dealer_name;
	}
	public void setDealer_name(String dealer_name) {
		this.dealer_name = dealer_name;
	}
	public String getDealer_level() {
		return dealer_level;
	}
	public void setDealer_level(String dealer_level) {
		this.dealer_level = dealer_level;
	}
	public String getDealer_id() {
		return dealer_id;
	}
	public void setDealer_id(String dealer_id) {
		this.dealer_id = dealer_id;
	}
	public String getSelf_dealer_id() {
		return self_dealer_id;
	}
	public void setSelf_dealer_id(String self_dealer_id) {
		this.self_dealer_id = self_dealer_id;
	}

	
}
