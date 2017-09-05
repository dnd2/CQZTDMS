/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-05-12 15:44:24
* CreateBy   : Administrator
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TcAaPO extends PO {

	private String name;
	private Integer age;
	private Integer id;

	public void setName(String name){
		this.name=name;
	}

	public String getName(){
		return this.name;
	}

	public void setAge(Integer age){
		this.age=age;
	}

	public Integer getAge(){
		return this.age;
	}

	public void setId(Integer id){
		this.id=id;
	}

	public Integer getId(){
		return this.id;
	}

}