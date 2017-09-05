/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2009-12-24 16:51:28
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class SvoChangPO extends PO{

	private String changename;
	private String changeid;

	public void setChangename(String changename){
		this.changename=changename;
	}

	public String getChangename(){
		return this.changename;
	}

	public void setChangeid(String changeid){
		this.changeid=changeid;
	}

	public String getChangeid(){
		return this.changeid;
	}

}