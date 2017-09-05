/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-08-25 14:03:38
* CreateBy   : Arthur_Liu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TmpScMatchVehiclePO extends PO{

	private Long tmpMatchId;
	private String vin;
	private Long tmpVehicleId;
	private Long vehicleId;

	public void setTmpMatchId(Long tmpMatchId){
		this.tmpMatchId=tmpMatchId;
	}

	public Long getTmpMatchId(){
		return this.tmpMatchId;
	}

	public void setVin(String vin){
		this.vin=vin;
	}

	public String getVin(){
		return this.vin;
	}

	public void setTmpVehicleId(Long tmpVehicleId){
		this.tmpVehicleId=tmpVehicleId;
	}

	public Long getTmpVehicleId(){
		return this.tmpVehicleId;
	}

	public void setVehicleId(Long vehicleId){
		this.vehicleId=vehicleId;
	}

	public Long getVehicleId(){
		return this.vehicleId;
	}

}