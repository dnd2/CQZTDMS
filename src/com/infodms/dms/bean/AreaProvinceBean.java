package com.infodms.dms.bean;

import com.infoservice.po3.bean.PO;

public class AreaProvinceBean extends PO {
	private Long orgId ;
	private String orgCode ;
	private String orgName ;
	private Long regionId ;
	private String regionCode ;
	private String regionName ;
	private String areaLevel;
	private String codeDesc;
	public String getCodeDesc() {
		return codeDesc;
	}
	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setAreaLevel(String areaLevel) {
		this.areaLevel = areaLevel;
	}
	public String getAreaLevel() {
		return areaLevel;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}	
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Long getRegionId() {
		return regionId;
	}
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
}
