package com.infoservice.dms.chana.vo;

/**
 * 
 * @ClassName : FaultModeDetailVO
 * @Description : 失效模式信息VO
 * @author : luole 
 * @CreateDate : 2013-5-24
 */
@SuppressWarnings("serial")
public class FaultModeDetailVO extends BaseVO {
	private String faultCode;// 上端：故障法定代码 varchar2(50) 下端：
	private String faultName;// 上端：故障法定名称 varchar2(50) 下端：
	private String failureModeCode;// 上端：失效模式代码 varchar2(50) 下端：
	private String failureModeName;// 上端：失效模式名称 varchar2(50) 下端：
	private Integer status;// 上端：状态 NUMBER(8) 有效：10011001，无效10011002 下端：

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

	public String getFailureModeCode() {
		return failureModeCode;
	}

	public void setFailureModeCode(String failureModeCode) {
		this.failureModeCode = failureModeCode;
	}

	public String getFailureModeName() {
		return failureModeName;
	}

	public void setFailureModeName(String failureModeName) {
		this.failureModeName = failureModeName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
