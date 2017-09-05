/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2011-05-20 16:42:13
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsCarDisplacementDlrPO extends PO{

	private Long displacementDlrId;
	private Long dealerId;
	private Long displacementPrc ;

	public Long getDisplacementPrc() {
		return displacementPrc;
	}

	public void setDisplacementPrc(Long displacementPrc) {
		this.displacementPrc = displacementPrc;
	}

	public void setDisplacementDlrId(Long displacementDlrId){
		this.displacementDlrId=displacementDlrId;
	}

	public Long getDisplacementDlrId(){
		return this.displacementDlrId;
	}

	public void setDealerId(Long dealerId){
		this.dealerId=dealerId;
	}

	public Long getDealerId(){
		return this.dealerId;
	}

}