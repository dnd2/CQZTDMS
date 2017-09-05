package com.infoservice.dms.chana.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsClientPO extends PO{
	private Long id;
	private String secretKey;
	private String clientIp;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
}
