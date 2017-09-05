package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtCompResKeepPO extends PO {

	private Long keepId;
	private Long materialId;
	private Long num;
	private Date keepDate;
	private Long createBy;
	private Date createDate;	
	private Long updateBy;
	private Date updateDate;
	private Long orgId;

	public void setKeepId(Long keepId){
		this.keepId=keepId;
	}

	public Long getKeepId(){
		return this.keepId;
	}	
	
	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}	

	public void setNum(Long num){
		this.num=num;
	}

	public Long getNum(){
		return this.num;
	}		
	
	public void setKeepDate(Date keepDate){
		this.keepDate=keepDate;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Date getKeepDate(){
		return this.keepDate;
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

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}
	

	
}
