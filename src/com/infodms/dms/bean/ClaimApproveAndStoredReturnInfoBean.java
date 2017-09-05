package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class ClaimApproveAndStoredReturnInfoBean extends PO{
	
	private static final long serialVersionUID = -1170027069150341557L;
	
	private Long id;//回运清单主键
    private String dealer_code;//
    private String dealer_name;//经销商
    private String attach_area;//
    private Integer transport_type;//货运方式代码
	private String transport_desc;//货运方式解释
	private Integer parkage_amount;//装箱总数
	private String return_no;//回运清单号
	private String create_date;//创建日期
	private String return_date;//提报日期
	private Integer wr_amount;//索赔单数
	private Integer part_item_amount;//回运配件项数
	private Integer part_amount;//配件数
	private Integer return_type;//回运类型
	private String return_desc;//回运类型解释
	private String wr_start_date;//索赔单提报日期段
	private String tran_no;//货运单号
	private String price_remark;
	private String send_time;
	private Integer realBoxNo;//实到箱数
	private Integer partPakge;//包装情况
	private Integer partMark;//故障卡情况
	private Integer partDetail;//标签情况
	private Integer outPartPackge;
	private String signRemark;
	private String transport_no;
	private String transport_remark;
	private String transportName;
	private String transportCompany;
	private String inWarhouseName;
	private Integer yieldly;
	
	private String transport_id;
	
	
	public String getTransportCompany() {
		return transportCompany;
	}
	public void setTransportCompany(String transportCompany) {
		this.transportCompany = transportCompany;
	}
	public String getTransport_remark() {
		return transport_remark;
	}
	public void setTransport_remark(String transport_remark) {
		this.transport_remark = transport_remark;
	}
	public String getSend_time() {
		return send_time;
	}
	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}
	public String getTransport_id() {
		return transport_id;
	}
	public void setTransport_id(String transport_id) {
		this.transport_id = transport_id;
	}
	public Double getPrice() {
		return price;
	}
	public String getPrice_remark() {
		return price_remark;
	}
	public void setPrice_remark(String priceRemark) {
		price_remark = priceRemark;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	private Double price;//运费
	private Integer status;//物流单状态
	private String remark;//备注
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getNewPrice() {
		return newPrice;
	}
	public void setNewPrice(Double newPrice) {
		this.newPrice = newPrice;
	}
	private Double newPrice;//审核运费
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	/*************Iverson by 2010-11-04*****************************/
	private String tel;//三包员电话
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDealer_code() {
		return dealer_code;
	}
	public void setDealer_code(String dealer_code) {
		this.dealer_code = dealer_code;
	}
	public String getDealer_name() {
		return dealer_name;
	}
	public void setDealer_name(String dealer_name) {
		this.dealer_name = dealer_name;
	}
	public String getAttach_area() {
		return attach_area;
	}
	public void setAttach_area(String attach_area) {
		this.attach_area = attach_area;
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
	public Integer getParkage_amount() {
		return parkage_amount;
	}
	public void setParkage_amount(Integer parkage_amount) {
		this.parkage_amount = parkage_amount;
	}
	public String getReturn_no() {
		return return_no;
	}
	public void setReturn_no(String return_no) {
		this.return_no = return_no;
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
	public String getWr_start_date() {
		return wr_start_date;
	}
	public void setWr_start_date(String wr_start_date) {
		this.wr_start_date = wr_start_date;
	}
	public String getTran_no() {
		return tran_no;
	}
	public void setTran_no(String tran_no) {
		this.tran_no = tran_no;
	}
	public String getTransport_no() {
		return transport_no;
	}
	public void setTransport_no(String transport_no) {
		this.transport_no = transport_no;
	}
	public Integer getRealBoxNo() {
		return realBoxNo;
	}
	public void setRealBoxNo(Integer realBoxNo) {
		this.realBoxNo = realBoxNo;
	}
	public Integer getPartPakge() {
		return partPakge;
	}
	public void setPartPakge(Integer partPakge) {
		this.partPakge = partPakge;
	}
	public Integer getPartMark() {
		return partMark;
	}
	public void setPartMark(Integer partMark) {
		this.partMark = partMark;
	}
	public Integer getPartDetail() {
		return partDetail;
	}
	public void setPartDetail(Integer partDetail) {
		this.partDetail = partDetail;
	}
	public String getSignRemark() {
		return signRemark;
	}
	public void setSignRemark(String signRemark) {
		this.signRemark = signRemark;
	}
	public Integer getYieldly() {
		return yieldly;
	}
	public void setYieldly(Integer yieldly) {
		this.yieldly = yieldly;
	}
	public String getTransportName() {
		return transportName;
	}
	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}
	public Integer getOutPartPackge() {
		return outPartPackge;
	}
	public void setOutPartPackge(Integer outPartPackge) {
		this.outPartPackge = outPartPackge;
	}
	public String getInWarhouseName() {
		return inWarhouseName;
	}
	public void setInWarhouseName(String inWarhouseName) {
		this.inWarhouseName = inWarhouseName;
	}
	
}
