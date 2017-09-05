/*
* Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
* This software is published under the terms of the Infoservice Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-07-18 09:33:32
* CreateBy   : KFQ
* Comment    : generate by com.sgm.po.POGen
*/

package com.infodms.dms.po;


@SuppressWarnings("serial")
public class TtAsWrOldReturnedDetailExtendPO extends TtAsWrOldReturnedDetailPO {
	private String dealerName;
	private String dealerCode;
	private String partType;
	
	public String getPartType() {
		return partType;
	}

	public void setPartType(String partType) {
		this.partType = partType;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
}