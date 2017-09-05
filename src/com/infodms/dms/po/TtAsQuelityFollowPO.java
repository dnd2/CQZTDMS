package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsQuelityFollowPO extends PO {
	
	private Long id;
	private Long carTieId;// 车系
	private Long carTypeId;// 车型
	private String vin;//底盘号
	private Date roCreateDate;//车辆创建时间
	private Date roRepairDateOne;//维修日期起
	private Date roRepairDateTwo;//维修日期止
	private String makerCode;// 部件厂代码
	private String makerName;// 部件厂名称
	private String malCode;// 故障类别代码
	private String malName;// 故障现象
	private String partCode;// 零件号
	private String partName;//零件号名称
	private String remark;//备注
	private Long partNum; //故障零件数
	private String createUser; // 创建人
	private Date createDate;//创建时间
	
	public TtAsQuelityFollowPO() {
		super();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCarTieId() {
		return carTieId;
	}
	public void setCarTieId(Long carTieId) {
		this.carTieId = carTieId;
	}
	public Long getCarTypeId() {
		return carTypeId;
	}
	public void setCarTypeId(Long carTypeId) {
		this.carTypeId = carTypeId;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Date getRoCreateDate() {
		return roCreateDate;
	}
	public void setRoCreateDate(Date roCreateDate) {
		this.roCreateDate = roCreateDate;
	}
	public Date getRoRepairDateOne() {
		return roRepairDateOne;
	}
	public void setRoRepairDateOne(Date roRepairDateOne) {
		this.roRepairDateOne = roRepairDateOne;
	}
	public Date getRoRepairDateTwo() {
		return roRepairDateTwo;
	}
	public void setRoRepairDateTwo(Date roRepairDateTwo) {
		this.roRepairDateTwo = roRepairDateTwo;
	}
	public String getMakerCode() {
		return makerCode;
	}
	public void setMakerCode(String makerCode) {
		this.makerCode = makerCode;
	}
	public String getMakerName() {
		return makerName;
	}
	public void setMakerName(String makerName) {
		this.makerName = makerName;
	}
	public String getMalCode() {
		return malCode;
	}
	public void setMalCode(String malCode) {
		this.malCode = malCode;
	}
	public String getMalName() {
		return malName;
	}
	public void setMalName(String malName) {
		this.malName = malName;
	}
	public String getPartCode() {
		return partCode;
	}
	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getPartNum() {
		return partNum;
	}
	public void setPartNum(Long partNum) {
		this.partNum = partNum;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
