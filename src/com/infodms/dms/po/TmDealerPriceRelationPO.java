package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;
public class TmDealerPriceRelationPO extends PO{

	private Integer isDefault;
	private Long dealerId;
	private Long updateBy;
	private Date updateDate;
	private Long priceId;
	private Long createBy;
	private Date createDate;
	private Long relationId;

	public void setIsDefault(Integer isDefault){
		this.isDefault=isDefault;
	}

	public Integer getIsDefault(){
		return this.isDefault;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
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

	public void setPriceId(Long priceId){
		this.priceId=priceId;
	}

	public Long getPriceId(){
		return this.priceId;
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

	public void setRelationId(Long relationId){
		this.relationId=relationId;
	}

	public Long getRelationId(){
		return this.relationId;
	}

}
