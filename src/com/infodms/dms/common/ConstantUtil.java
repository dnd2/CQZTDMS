/**********************************************************************
 * <pre>
 * FILE : ConstantUtil.java
 * CLASS : ConstantUtil
 *
 * AUTHOR : SuMMeR
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		  |2009-8-30| SuMMeR| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
/**
 * $Id: ConstantUtil.java,v 1.1 2010/08/16 01:44:17 yuch Exp $
 */

package com.infodms.dms.common;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * 数据字典工具类
 * 
 * @author SuMMeR
 */
public class ConstantUtil
{
	/**
	 * 根据类型获取字典表数据
	 * 
	 * @param type
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static LinkedHashMap<String, String> getCodeListByType(String type) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{
		Class<?> constant = Class.forName("com.jmc.dms.common.Constant");
		LinkedHashMap<String, String> codes = new LinkedHashMap<String, String>();
		Field[] fields = constant.getDeclaredFields();
		for (int i = 0; i < fields.length; i++)
		{
			if (fields[i].getName().contains(type))
			{
				String key = fields[i].getName();
				String value = fields[i].get(constant).toString();
				codes.put(key, value);
			}
		}
		return codes;
	}

	public static void main(String[] args)
	{
		ConstantUtil util = new ConstantUtil();
		try
		{
			Map<String, String> map = util.getCodeListByType("VEHICLE_FRONT");
			Set<Entry<String, String>> set = map.entrySet();
			Iterator<Entry<String, String>> it = set.iterator();
			while (it.hasNext())
			{
				Entry<String, String> e = it.next();
				System.out.println(e.getKey());
				System.out.println(e.getValue());
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
