/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-07-05 14:54:43
* CreateBy   : 
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;

import java.util.Date;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class FsFileuploadPO extends PO{

	private String fileurl;
	private Long updateBy;
	private Date updateDate;
	private Long createBy;
	private String filename;
	private Long fjid;
	private Date createDate;
	private String fileid;
	private Integer status;
	private Long ywzj;
	private String pjid;
	
	public String getPjid() {
		return pjid;
	}

	public void setPjid(String pjid) {
		this.pjid = pjid;
	}

	public void setFileurl(String fileurl){
		this.fileurl=fileurl;
	}

	public String getFileurl(){
		return this.fileurl;
	}

	public void setUpdateBy(Long updateBy){
		this.updateBy=updateBy;
	}

	public Long getUpdateBy(){
		return this.updateBy;
	}

	public void setUpdateDate(Date updateDate){
		this.updateDate=updateDate;
	}

	public Date getUpdateDate(){
		return this.updateDate;
	}

	public void setCreateBy(Long createBy){
		this.createBy=createBy;
	}

	public Long getCreateBy(){
		return this.createBy;
	}

	public void setFilename(String filename){
		this.filename=filename;
	}

	public String getFilename(){
		return this.filename;
	}

	public void setFjid(Long fjid){
		this.fjid=fjid;
	}

	public Long getFjid(){
		return this.fjid;
	}

	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setFileid(String fileid){
		this.fileid=fileid;
	}

	public String getFileid(){
		return this.fileid;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setYwzj(Long ywzj){
		this.ywzj=ywzj;
	}

	public Long getYwzj(){
		return this.ywzj;
	}

}