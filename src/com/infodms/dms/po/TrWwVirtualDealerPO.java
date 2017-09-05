/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-04-04 16:27:28
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TrWwVirtualDealerPO extends PO{

	private String wwDealerName;
	private String wwDealerCode;
	private Long id;
	private Long dmsDealerId;

	public void setWwDealerName(String wwDealerName){
		this.wwDealerName=wwDealerName;
	}

	public String getWwDealerName(){
		return this.wwDealerName;
	}

	public void setWwDealerCode(String wwDealerCode){
		this.wwDealerCode=wwDealerCode;
	}

	public String getWwDealerCode(){
		return this.wwDealerCode;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return this.id;
	}

	public void setDmsDealerId(Long dmsDealerId){
		this.dmsDealerId=dmsDealerId;
	}

	public Long getDmsDealerId(){
		return this.dmsDealerId;
	}

}