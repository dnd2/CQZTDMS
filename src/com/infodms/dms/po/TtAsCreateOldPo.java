package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtAsCreateOldPo extends PO {
	private Long id;
	private Integer nameType;
	private String codeOld;
	private String nameOld;
	private String createUser;
	private Date createDate;

	public TtAsCreateOldPo() {
	}

	public TtAsCreateOldPo(Long id, Integer nameType, String codeOld,
			String nameOld, String createUser, Date createDate) {
		this.id = id;
		this.nameType = nameType;
		this.codeOld = codeOld;
		this.nameOld = nameOld;
		this.createUser = createUser;
		this.createDate = createDate;
	}

	public TtAsCreateOldPo(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNameType() {
		return nameType;
	}

	public void setNameType(Integer nameType) {
		this.nameType = nameType;
	}

	public String getNameOld() {
		return nameOld;
	}

	public void setNameOld(String nameOld) {
		this.nameOld = nameOld;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCodeOld() {
		return codeOld;
	}

	public void setCodeOld(String codeOld) {
		this.codeOld = codeOld;
	}

}
