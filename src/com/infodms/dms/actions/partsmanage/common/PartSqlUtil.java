package com.infodms.dms.actions.partsmanage.common;

public class PartSqlUtil {
	/**
	 * 
	* @Title: genSqlByOrderId 
	* @Description: TODO(根据采购订单ID查询货运单信息) 
	* @param @param orderId
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String genSqlByOrderId(Long orderId) {
		StringBuilder str = new StringBuilder("");
		str.append(" (SELECT PART_CODE, COUNT FROM TT_PT_SHIPPINGSHEETITEM WHERE DO_NO = \n")
			.append(" (SELECT DO_NO FROM TT_PT_SHIPPINGSHEET T WHERE SO_NO = \n")
			.append(" (SELECT SO_NO FROM TT_PT_SALES  WHERE ORDER_NO = \n")
			.append(" (SELECT ORDER_NO FROM TT_PT_ORDER C WHERE C.ORDER_ID = \n")
			.append(orderId)
			.append(" )))) \n");
		return str.toString();
	}
	
	public static void main(String[] args) {
		String s = genSqlByOrderId(2010062300058671L);
		System.out.println(s);
	}

}
