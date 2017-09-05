package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsDealerAccountPo extends PO{
	private Long dealerId;
	private Long accountId;
	private Double accountBalance;
	private Date createDate;
	private Long createBy;
	private Date updateDate;
	private Long updateBy;
	
	public void setDealerId (Long dealerId){
		this.dealerId= dealerId;
	}
	
	public Long getDealerId(){
		return dealerId;
	}
	
	public void setAccountId(Long accountId){
		 this.accountId = accountId;
	}
	
	public Long getAccountId(){
		return accountId;
	}
	
	public void setAccountBalance(Double accountBalance){
		this.accountBalance = accountBalance;
	}
	
	public Double getAccountBalance(){
		return accountBalance;
	}
	
	public void setCreateDate(Date createDate){
		this.createDate =createDate;
	}
	
	public Date getCreateDate(){
		return createDate;
	}
	
	public void setCreateBy(Long createBy){
		this.createBy = createBy;
	}
	
	public Long getCreateBy(){
		return createBy;
	}
	
	public void setUpdateDate(Date updateDate){
		this.updateDate =updateDate;
	}
	
	public Date getUpdateDate(){
		return updateDate;
	}
	
	public void setUpdateBy(Long updateBy){
		this.updateBy = updateBy;
	}
	
	public Long getUpdateBy(){
		return updateBy;
	}
	
	
}
