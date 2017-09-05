package com.infodms.yxdms.entity.maintain;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsYsqPartDelLogPO extends PO{

	private Long partId;
	private Long ysqId;
	private String partName;
	private Long id;
	private Long createBy;
	private Date createDate;
	private String partCode;
	private Long partType;
	private Long delBy;
	private Date delDate;
	
    
	public Long getYsqId() {
		return ysqId;
	}

	public void setYsqId(Long ysqId) {
		this.ysqId = ysqId;
	}

	public Long getDelBy() {
		return delBy;
	}

	public void setDelBy(Long delBy) {
		this.delBy = delBy;
	}

	public Date getDelDate() {
		return delDate;
	}

	public void setDelDate(Date delDate) {
		this.delDate = delDate;
	}

	public Long getPartType() {
		return partType;
	}

	public void setPartType(Long partType) {
		this.partType = partType;
	}

	public void setPartId(Long partId){
		this.partId=partId;
	}

	public Long getPartId(){
		return this.partId;
	}

	public void setPartName(String partName){
		this.partName=partName;
	}

	public String getPartName(){
		return this.partName;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setPartCode(String partCode){
		this.partCode=partCode;
	}

	public String getPartCode(){
		return this.partCode;
	}

}