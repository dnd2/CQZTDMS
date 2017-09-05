package com.infoservice.dms.chana.vo;

import java.util.Date;
import java.util.HashMap;

/**
 * 
 * @ClassName : PtAllocationOutVo
 * @Description : 配件调拨出库VO
 * @author : 	luole
 * @CreateDate : 2013-5-28
 */
@SuppressWarnings("serial")
public class PtAllocationOutVO extends BaseVO {
	private Long outId;// 上端： ID  NUMBER(16) 下端：
	private Date outTime;// 上端： 出库日期 TIMESTAMP 下端：
	private String allocationOutNo;// 上端： 调拨出库单号 VARCHAR(20) 唯一 下端：
	private String allocationEnterCode;// 上端： 调入经销商代码 VARCHAR(60) 下端：
	private String allocationEnterName;// 上端： 调入经销商名称 VARCHAR(60) 下端：
	private String outCode;// 上端： 出库经销商代码 VARCHAR(60) 下端：
	private String outName;// 上端： 出库经销商名称 VARCHAR(60) 下端：
	private String remark;// 上端： 备注 VARCHAR(400) 下端：
	private HashMap<Integer, BaseVO> delist;// 明细

	
	public Long getOutId() {
		return outId;
	}

	public void setOutId(Long outId) {
		this.outId = outId;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	public String getAllocationOutNo() {
		return allocationOutNo;
	}

	public void setAllocationOutNo(String allocationOutNo) {
		this.allocationOutNo = allocationOutNo;
	}

	public String getAllocationEnterCode() {
		return allocationEnterCode;
	}

	public void setAllocationEnterCode(String allocationEnterCode) {
		this.allocationEnterCode = allocationEnterCode;
	}

	public String getAllocationEnterName() {
		return allocationEnterName;
	}

	public void setAllocationEnterName(String allocationEnterName) {
		this.allocationEnterName = allocationEnterName;
	}

	public String getOutCode() {
		return outCode;
	}

	public void setOutCode(String outCode) {
		this.outCode = outCode;
	}

	public String getOutName() {
		return outName;
	}

	public void setOutName(String outName) {
		this.outName = outName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public HashMap<Integer, BaseVO> getDelist() {
		return delist;
	}

	public void setDelist(HashMap<Integer, BaseVO> delist) {
		this.delist = delist;
	}

}
