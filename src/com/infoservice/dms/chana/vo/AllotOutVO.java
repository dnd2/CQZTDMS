package com.infoservice.dms.chana.vo;

import java.util.HashMap;

public class AllotOutVO extends BaseVO{
	 private String consigneeCode; // 交车单位代码
	 private String consigneeName; // 交车单位名称
	 private HashMap<Integer, OutVehicleVO> vins;//车辆列表
	 private String remark;//备注
	 public String getConsigneeCode() {
		return consigneeCode;
	}
	public void setConsigneeCode(String consigneeCode) {
		this.consigneeCode = consigneeCode;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public HashMap<Integer, OutVehicleVO> getVins() {
		return vins;
	}
	public void setVins(HashMap<Integer, OutVehicleVO> vins) {
		this.vins = vins;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
