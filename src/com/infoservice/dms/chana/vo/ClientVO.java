package com.infoservice.dms.chana.vo;
@SuppressWarnings("serial")
public class ClientVO extends BaseVO{
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
