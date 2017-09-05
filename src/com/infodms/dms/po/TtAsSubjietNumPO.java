/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-16 09:36:15
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsSubjietNumPO extends PO{

	private Long subjectid;
	private Long num;

	public void setSubjectid(Long subjectid){
		this.subjectid=subjectid;
	}

	public Long getSubjectid(){
		return this.subjectid;
	}

	public void setNum(Long num){
		this.num=num;
	}

	public Long getNum(){
		return this.num;
	}

}