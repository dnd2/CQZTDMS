package com.infodms.dms.dao.claim.oldPart;

public class ClaimTools {

	/**
	 * Function：处理Map中参数
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-13
	 */
	public static String dealParamStr(Object param){
		if(param==null){
			return "";
		}else{
			return String.valueOf(param).toString();
		}
	}
}
