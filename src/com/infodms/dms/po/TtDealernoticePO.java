/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2015-05-21 14:35:17
* CreateBy   : chenyu
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

public class TtDealernoticePO extends PO{

	private static final long serialVersionUID = -4353310070018409974L;
	private Date dnBusinessstatetime;  // 业务单状态时间
	private Date dnHandletime;		   // 处理时间
	private String dnHandlestate;      // 处理状态
	private String dnDealerid;         // 经销商id
	private String dnBusinessid;       // 业务单id
	private Date dnCreatetime;         // 创建时间
	private String dnBusinessstate;    // 业务单状态
	private String dnHandleuser;       // 处理人	
	private String nmId;               // 模版id
	private String dnTarid;            // 提醒目标id
	private String id;                 // id主键
	private String dnNoticecontent;    // 提醒内容
	private String dnBusinessno;       // 业务单号

	public void setDnBusinessstatetime(Date dnBusinessstatetime){
		this.dnBusinessstatetime=dnBusinessstatetime;
	}

	public Date getDnBusinessstatetime(){
		return this.dnBusinessstatetime;
	}

	public void setDnHandletime(Date dnHandletime){
		this.dnHandletime=dnHandletime;
	}

	public Date getDnHandletime(){
		return this.dnHandletime;
	}

	public void setDnHandlestate(String dnHandlestate){
		this.dnHandlestate=dnHandlestate;
	}

	public String getDnHandlestate(){
		return this.dnHandlestate;
	}

	public void setDnDealerid(String dnDealerid){
		this.dnDealerid=dnDealerid;
	}

	public String getDnDealerid(){
		return this.dnDealerid;
	}

	public void setDnBusinessid(String dnBusinessid){
		this.dnBusinessid=dnBusinessid;
	}

	public String getDnBusinessid(){
		return this.dnBusinessid;
	}

	public void setDnCreatetime(Date dnCreatetime){
		this.dnCreatetime=dnCreatetime;
	}

	public Date getDnCreatetime(){
		return this.dnCreatetime;
	}

	public void setDnBusinessstate(String dnBusinessstate){
		this.dnBusinessstate=dnBusinessstate;
	}

	public String getDnBusinessstate(){
		return this.dnBusinessstate;
	}

	public void setDnHandleuser(String dnHandleuser){
		this.dnHandleuser=dnHandleuser;
	}

	public String getDnHandleuser(){
		return this.dnHandleuser;
	}

	public void setNmId(String nmId){
		this.nmId=nmId;
	}

	public String getNmId(){
		return this.nmId;
	}

	public void setDnTarid(String dnTarid){
		this.dnTarid=dnTarid;
	}

	public String getDnTarid(){
		return this.dnTarid;
	}

	public void setId(String id){
		this.id=id;
	}

	public String getId(){
		return this.id;
	}

	public void setDnNoticecontent(String dnNoticecontent){
		this.dnNoticecontent=dnNoticecontent;
	}

	public String getDnNoticecontent(){
		return this.dnNoticecontent;
	}

	public void setDnBusinessno(String dnBusinessno){
		this.dnBusinessno=dnBusinessno;
	}

	public String getDnBusinessno(){
		return this.dnBusinessno;
	}

}