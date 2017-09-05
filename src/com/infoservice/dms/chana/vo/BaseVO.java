package com.infoservice.dms.chana.vo;

import java.util.Date;

import com.infodms.dms.common.Constant;
import com.infoservice.de.convertor.f2.VO;

@SuppressWarnings("serial")
public class BaseVO implements VO {

	private String entityCode; //下端：经销商代码  CHAR(8)  上端： 
	private Date downTimestamp; //下发的时间
	private Integer isValid; //是否有效
	private String errorMsg; //错误消息
	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public Date getDownTimestamp() {
		return downTimestamp;
	}

	public void setDownTimestamp(Date downTimestamp) {
		this.downTimestamp = downTimestamp;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		if (isValid == 0 || isValid.intValue() == Constant.STATUS_ENABLE) {
			this.isValid = Constant.STATUS_ENABLE;
		} else {
			this.isValid = Constant.STATUS_DISABLE;
		}
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String toXMLString() {
		// TODO Auto-generated method stub
		return null;
	}

}
