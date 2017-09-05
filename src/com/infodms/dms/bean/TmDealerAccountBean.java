/**********************************************************************
 * <pre>
 * FILE : TmDealerAccountBean.java
 * CLASS : TmDealerAccountBean
 *
 * AUTHOR : yangyong
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2009-12-30|yangyong| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.bean;

import java.util.Date;

public class TmDealerAccountBean {
	private String accountBank;
	private Long accountId;
	private String accountNo;
	private Date updateDate;
	private String invHead;
	private Long updateBy;
	private Long createBy;
	private Date createDate;
	private String invHeadContent;
	private Long dealerId;
	
	private String dealerShortname;

	public String getAccountBank() {
		return accountBank;
	}

	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getInvHead() {
		return invHead;
	}

	public void setInvHead(String invHead) {
		this.invHead = invHead;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getInvHeadContent() {
		return invHeadContent;
	}

	public void setInvHeadContent(String invHeadContent) {
		this.invHeadContent = invHeadContent;
	}

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public String getDealerShortname() {
		return dealerShortname;
	}

	public void setDealerShortname(String dealerShortname) {
		this.dealerShortname = dealerShortname;
	}
}
