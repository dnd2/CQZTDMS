/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2010-01-15 10:12:23
* CreateBy   : xwj
* Comment    : generate by com.sgm.po.POGen
*/

package com.infoservice.dms.chana.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class MqQueueInfoPO extends PO{

	private String type;
	private String mqmName;
	private String queueName;
	private Integer enable;
	private String channel;
	private String description;
	private Integer ccsid;
	private String host;
	private Integer port;

	public void setType(String type){
		this.type=type;
	}

	public String getType(){
		return this.type;
	}

	public void setMqmName(String mqmName){
		this.mqmName=mqmName;
	}

	public String getMqmName(){
		return this.mqmName;
	}

	public void setQueueName(String queueName){
		this.queueName=queueName;
	}

	public String getQueueName(){
		return this.queueName;
	}

	public void setEnable(Integer enable){
		this.enable=enable;
	}

	public Integer getEnable(){
		return this.enable;
	}

	public void setChannel(String channel){
		this.channel=channel;
	}

	public String getChannel(){
		return this.channel;
	}

	public void setDescription(String description){
		this.description=description;
	}

	public String getDescription(){
		return this.description;
	}

	public void setCcsid(Integer ccsid){
		this.ccsid=ccsid;
	}

	public Integer getCcsid(){
		return this.ccsid;
	}

	public void setHost(String host){
		this.host=host;
	}

	public String getHost(){
		return this.host;
	}

	public void setPort(Integer port){
		this.port=port;
	}

	public Integer getPort(){
		return this.port;
	}

}