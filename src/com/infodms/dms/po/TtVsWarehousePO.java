/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2014-03-19 10:20:39
* CreateBy   : ranpok
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtVsWarehousePO extends PO{

	private String warehousecode;
	private String orgcode;
	private String warehousetype;
	private String orgname;
	private String warehousename;

	public void setWarehousecode(String warehousecode){
		this.warehousecode=warehousecode;
	}

	public String getWarehousecode(){
		return this.warehousecode;
	}

	public void setOrgcode(String orgcode){
		this.orgcode=orgcode;
	}

	public String getOrgcode(){
		return this.orgcode;
	}

	public void setWarehousetype(String warehousetype){
		this.warehousetype=warehousetype;
	}

	public String getWarehousetype(){
		return this.warehousetype;
	}

	public void setOrgname(String orgname){
		this.orgname=orgname;
	}

	public String getOrgname(){
		return this.orgname;
	}

	public void setWarehousename(String warehousename){
		this.warehousename=warehousename;
	}

	public String getWarehousename(){
		return this.warehousename;
	}

}