/**********************************************************************
 * <pre>
 * FILE : OrderUtil.java
 * CLASS : OrderUtil
 * 
 * AUTHOR : ChenLiang
 *
 * FUNCTION : 表格排序工具.
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2009-09-11| ChenLiang  | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.common;

public class OrderUtil {

	public static String addOrderBy(String sql,String orderName, String da) {
		StringBuffer bf = new StringBuffer();

		bf.append(" SELECT * FROM ( ");
		bf.append(sql);
		bf.append(" ) ");
		
		if(orderName != null && !"".equals(orderName)) {
			/**
			 * add by zhangxianchao 
			 * 新增按拼音排序的方式
			 */
			String[] arr = orderName.split("-");
			String orderType = null;
			if(arr!=null && arr.length>=2){
				orderName = arr[0];
				orderType = arr[1];
			}else{
				orderName = arr[0];
			}
			if(orderType!=null&&orderType.equals("pingyin")){
				bf.append(" order by NLSSORT("+orderName+",'NLS_SORT = SCHINESE_PINYIN_M')"
						+ ("1".equals(da) ? " asc" : " desc"));
			}else{
				bf.append(" order by " + orderName
						+ ("1".equals(da) ? " asc" : " desc"));
			}
		}

		return bf.toString();
	}

}
