/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-10-04 12:33:31
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsAccountFreezeLogPO extends PO{

	private Date opdate;
	private Long freezeId;
	private String nFreezeAmount;
	private String oFreezeAmount;
	private Long userId;

	public void setOpdate(Date opdate){
		this.opdate=opdate;
	}

	public Date getOpdate(){
		return this.opdate;
	}

	public void setFreezeId(Long freezeId){
		this.freezeId=freezeId;
	}

	public Long getFreezeId(){
		return this.freezeId;
	}

	public void setNFreezeAmount(String nFreezeAmount){
		this.nFreezeAmount=nFreezeAmount;
	}

	public String getNFreezeAmount(){
		return this.nFreezeAmount;
	}

	public void setOFreezeAmount(String oFreezeAmount){
		this.oFreezeAmount=oFreezeAmount;
	}

	public String getOFreezeAmount(){
		return this.oFreezeAmount;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return this.userId;
	}

}