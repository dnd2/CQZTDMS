package com.infodms.dms.bean;

public class TmUserInfoBean {

	private String orgDesc;
	private String jmcPose;
	private String poseName;
	
	private Long quickFuncId;
	private Long poseId;
	private Long funcId;
	private String funcName;
	private Integer funcOrder;
	
	public String getJmcPose() {
		return jmcPose;
	}
	public void setJmcPose(String jmcPose) {
		this.jmcPose = jmcPose;
	}
	public Long getQuickFuncId() {
		return quickFuncId;
	}
	public void setQuickFuncId(Long quickFuncId) {
		this.quickFuncId = quickFuncId;
	}
	public Long getPoseId() {
		return poseId;
	}
	public void setPoseId(Long poseId) {
		this.poseId = poseId;
	}
	public Long getFuncId() {
		return funcId;
	}
	public void setFuncId(Long funcId) {
		this.funcId = funcId;
	}
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public Integer getFuncOrder() {
		return funcOrder;
	}
	public void setFuncOrder(Integer funcOrder) {
		this.funcOrder = funcOrder;
	}
	public String getOrgDesc() {
		return orgDesc;
	}
	public void setOrgDesc(String orgDesc) {
		this.orgDesc = orgDesc;
	}
	public String getPoseName() {
		return poseName;
	}
	public void setPoseName(String poseName) {
		this.poseName = poseName;
	}
}
