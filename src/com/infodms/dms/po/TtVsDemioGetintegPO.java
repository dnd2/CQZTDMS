/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-01-23 16:10:43
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsDemioGetintegPO extends PO{

	private Long getInteg;
	private String carDemio;
	private Long demioGetintegId;
	private Long authenticationLevel;

	public void setGetInteg(Long getInteg){
		this.getInteg=getInteg;
	}

	public Long getGetInteg(){
		return this.getInteg;
	}

	public void setCarDemio(String carDemio){
		this.carDemio=carDemio;
	}

	public String getCarDemio(){
		return this.carDemio;
	}

	public void setDemioGetintegId(Long demioGetintegId){
		this.demioGetintegId=demioGetintegId;
	}

	public Long getDemioGetintegId(){
		return this.demioGetintegId;
	}

	public void setAuthenticationLevel(Long authenticationLevel){
		this.authenticationLevel=authenticationLevel;
	}

	public Long getAuthenticationLevel(){
		return this.authenticationLevel;
	}

}