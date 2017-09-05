package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

public class TtAsWrBackupListPO extends PO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String partName;
	private Date updateDate;
	private Long ruleId;
	private Double claimMelieage;
	private Integer claimMonth;
	private Long updateBy;
	private Long createBy;
	private String partCode;
	private Date createDate;
	private String partWarType;

	private Long id;
	
	
	public String getPartWarType() {
		return partWarType;
	}

	public void setPartWarType(String partWarType) {
		this.partWarType = partWarType;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setRuleId(Long ruleId){
		this.ruleId=ruleId;
	}

	public Long getRuleId(){
		return this.ruleId;
	}

	public void setClaimMelieage(Double claimMelieage){
		this.claimMelieage=claimMelieage;
	}

	public Double getClaimMelieage(){
		return this.claimMelieage;
	}

	public Integer getClaimMonth() {
		return claimMonth;
	}

	public void setClaimMonth(Integer claimMonth) {
		this.claimMonth = claimMonth;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}
}
