package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;
/**
 * 类说明：索赔旧件审批入库查询列表Bean
 * 作者：  赵伦达
 */
public class ClaimOldPartApproveStoreListBean extends PO{
	
	private static final long serialVersionUID = 247602811396993302L;

	private Long id;//回运清单明细主键
	private String dealer_id;//经销商代码
	private String dealer_name;//经销商简称
	private String claim_no;//索赔申请单号
	private String create_date;//
	private String return_date;//
	private Long return_type;//
	private String return_desc;//
	private Integer wr_amount;//索赔单数
	private Integer parkage_amount;//装箱总数
	private Integer part_amount;//配件数
	private Integer back_type;//回运清单状态
	private String  back_desc;//回运清单状态
	private String trans_no;//货运单号
	private String box_no;//装箱单号
	private String wr_start_date;//旧件开始结束时间
	private String in_warhouse_name;//签收入库人
	private String sign_name;
	private String sign_date;
	private Long is_overduecheck ;//
	private String is_overdue ;//
	private Integer yieldly;
	private String sign_no;
	private Integer audit_no;
	private String transport_no;
	private String borrow_person;
	private String borrow_phone;
	private String next_time;
	private String return_no;
	private String tawor_id;
	private String name;
	
	
	
	
	
	
	
	
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTawor_id() {
		return tawor_id;
	}
	public void setTawor_id(String tawor_id) {
		this.tawor_id = tawor_id;
	}
	public String getReturn_no() {
		return return_no;
	}
	public void setReturn_no(String return_no) {
		this.return_no = return_no;
	}
	public String getNext_time() {
		return next_time;
	}
	public void setNext_time(String next_time) {
		this.next_time = next_time;
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
	public String getTransport_no() {
		return transport_no;
	}
	public void setTransport_no(String transport_no) {
		this.transport_no = transport_no;
	}
	public Integer getAudit_no() {
		return audit_no;
	}
	public void setAudit_no(Integer audit_no) {
		this.audit_no = audit_no;
	}
	public Integer getYieldly() {
		return yieldly;
	}
	public void setYieldly(Integer yieldly) {
		this.yieldly = yieldly;
	}
	public Long getIs_overduecheck() {
		return is_overduecheck;
	}
	public void setIs_overduecheck(Long isOverduecheck) {
		is_overduecheck = isOverduecheck;
	}
	public String getIs_overdue() {
		return is_overdue;
	}
	public void setIs_overdue(String isOverdue) {
		is_overdue = isOverdue;
	}
	private String create_dates;//
	
	private String is_sm;
	public String getIs_sm() {
		return is_sm;
	}
	public void setIs_sm(String isSm) {
		is_sm = isSm;
	}
	public String getCreate_dates() {
		return create_dates;
	}
	public void setCreate_dates(String createDates) {
		create_dates = createDates;
	}
	public String getWr_start_date() {
		return wr_start_date;
	}
	public void setWr_start_date(String wrStartDate) {
		wr_start_date = wrStartDate;
	}
	public String getBox_no() {
		return box_no;
	}
	public void setBox_no(String boxNo) {
		box_no = boxNo;
	}
	/*******Iverson by 2010-11-04 三包员电话**********************************/
	private String tel;//
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	/*******Iverson by 2010-11-04 三包员电话**********************************/
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDealer_id() {
		return dealer_id;
	}
	public void setDealer_id(String dealer_id) {
		this.dealer_id = dealer_id;
	}
	public String getDealer_name() {
		return dealer_name;
	}
	public void setDealer_name(String dealer_name) {
		this.dealer_name = dealer_name;
	}
	public String getClaim_no() {
		return claim_no;
	}
	public void setClaim_no(String claim_no) {
		this.claim_no = claim_no;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getReturn_date() {
		return return_date;
	}
	public void setReturn_date(String return_date) {
		this.return_date = return_date;
	}
	public Long getReturn_type() {
		return return_type;
	}
	public void setReturn_type(Long return_type) {
		this.return_type = return_type;
	}
	public String getReturn_desc() {
		return return_desc;
	}
	public void setReturn_desc(String return_desc) {
		this.return_desc = return_desc;
	}
	public Integer getWr_amount() {
		return wr_amount;
	}
	public void setWr_amount(Integer wr_amount) {
		this.wr_amount = wr_amount;
	}
	public Integer getParkage_amount() {
		return parkage_amount;
	}
	public void setParkage_amount(Integer parkage_amount) {
		this.parkage_amount = parkage_amount;
	}
	public Integer getPart_amount() {
		return part_amount;
	}
	public void setPart_amount(Integer part_amount) {
		this.part_amount = part_amount;
	}
	public Integer getBack_type() {
		return back_type;
	}
	public void setBack_type(Integer back_type) {
		this.back_type = back_type;
	}
	public String getBack_desc() {
		return back_desc;
	}
	public void setBack_desc(String back_desc) {
		this.back_desc = back_desc;
	}
	public String getTrans_no() {
		return trans_no;
	}
	public void setTrans_no(String trans_no) {
		this.trans_no = trans_no;
	}
	public String getIn_warhouse_name() {
		return in_warhouse_name;
	}
	public void setIn_warhouse_name(String in_warhouse_name) {
		this.in_warhouse_name = in_warhouse_name;
	}
	public String getSign_name() {
		return sign_name;
	}
	public void setSign_name(String sign_name) {
		this.sign_name = sign_name;
	}
	public String getSign_date() {
		return sign_date;
	}
	public void setSign_date(String sign_date) {
		this.sign_date = sign_date;
	}
	public void setSign_no(String sign_no) {
		this.sign_no = sign_no;
	}
	public String getSign_no() {
		return sign_no;
	}
}
