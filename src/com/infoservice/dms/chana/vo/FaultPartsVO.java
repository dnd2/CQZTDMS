package com.infoservice.dms.chana.vo;

/**
 * 
 * @ClassName : FaultPartsVO
 * @Description : 配件信息VO
 * @author : luole 
 * @CreateDate : 2013-5-24
 */

@SuppressWarnings("serial")
public class FaultPartsVO extends BaseVO{

	private String faultCode;// 上端：故障法定代码 varchar2(50) 下端：
	private String faultName;// 上端：故障法定名称 varchar2(50) 下端：
	private String partCode;// 上端：配件代码 varchar2(50) 下端：
	private String partName;// 上端：配件名称 varchar2(50) 下端：
	private Integer status;// 上端：状态 NUMBER(8) 有效：10011001，无效10011002  下端：

	public String getFaultCode() {
		return faultCode;
	}

	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}

	public String getFaultName() {
		return faultName;
	}

	public void setFaultName(String faultName) {
		this.faultName = faultName;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
