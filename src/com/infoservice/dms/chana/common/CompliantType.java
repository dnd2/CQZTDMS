package com.infoservice.dms.chana.common;

import com.infodms.dms.common.Constant;

public class CompliantType {
	/**
	 * 
	* @Title: getComType 
	* @Description: TODO(根据投诉小类找大类) 
	* @param @param comType 投诉小类 TC_CODE
	* @return Integer   投诉大类 TC_CODE
	* @throws
	 */
	public static Integer getComType(Integer comType) {
		if (comType == Integer.parseInt(Constant.SERVICE_COMP_TYPE_01)
				|| Integer.parseInt(Constant.SERVICE_COMP_TYPE_02) == comType) {
			return Constant.COMP_TYPE_TYPE_01;
		}
		return null;
	}
}
