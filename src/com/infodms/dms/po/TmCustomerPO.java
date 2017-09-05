/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-05-17 10:46:20
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmCustomerPO extends PO{

	private String curtAddress;
	private String custName;
	private Date updateDate;
	private String curtPhone;
	private Long updateBy;
	private String custCard;
	private Long createBy;
	private Date createDate;
	private Long custId;

	public void setCurtAddress(String curtAddress){
		this.curtAddress=curtAddress;
	}

	public String getCurtAddress(){
		return this.curtAddress;
	}

	public void setCustName(String custName){
		this.custName=custName;
	}

	public String getCustName(){
		return this.custName;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCurtPhone(String curtPhone){
		this.curtPhone=curtPhone;
	}

	public String getCurtPhone(){
		return this.curtPhone;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setCustCard(String custCard){
		this.custCard=custCard;
	}

	public String getCustCard(){
		return this.custCard;
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

	public void setCustId(Long custId){
		this.custId=custId;
	}

	public Long getCustId(){
		return this.custId;
	}

}