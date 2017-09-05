/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-08-25 14:03:25
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpScMatchPO extends PO{

	private Long materialId;
	private Long tmpMatchId;
	private Long orderId;
	private Integer matchNumber;

	public void setMaterialId(Long materialId){
		this.materialId=materialId;
	}

	public Long getMaterialId(){
		return this.materialId;
	}

	public void setTmpMatchId(Long tmpMatchId){
		this.tmpMatchId=tmpMatchId;
	}

	public Long getTmpMatchId(){
		return this.tmpMatchId;
	}

	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}

	public Long getOrderId(){
		return this.orderId;
	}

	public void setMatchNumber(Integer matchNumber){
		this.matchNumber=matchNumber;
	}

	public Integer getMatchNumber(){
		return this.matchNumber;
	}

}