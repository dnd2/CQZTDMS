package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TempDebugmsgPO extends PO{
	  private Date logtime;
	  private String objectname;
	  private String code;
	  private String message;
	  private String logcomment;
	
	public TempDebugmsgPO() {
		super();
	}
	public Date getLogtime() {
		return logtime;
	}
	public void setLogtime(Date logtime) {
		this.logtime = logtime;
	}
	public String getObjectname() {
		return objectname;
	}
	public void setObjectname(String objectname) {
		this.objectname = objectname;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getLogcomment() {
		return logcomment;
	}
	public void setLogcomment(String logcomment) {
		this.logcomment = logcomment;
	}
	  
}
