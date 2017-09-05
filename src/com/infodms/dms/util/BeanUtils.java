package com.infodms.dms.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.infoservice.mvc.context.RequestWrapper;

public class BeanUtils {
	public static Logger logger = Logger.getLogger(BeanUtils.class);

	public static <T> List<T> getBean(ResultSet rs, T object) throws Exception {
		ArrayList<T> objList = new ArrayList<T>();
		while (rs.next()) {
			objList.add(getOneBean(rs, object));
		}
		if (rs != null) {
			rs.close();
		}
		return objList;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getOneBean(ResultSet rs, T object) throws Exception {
		Class<?> classType = object.getClass();
		Field[] fields = classType.getDeclaredFields();// 得到对象中的字段
		// 每次循环时，重新实例化一个与传过来的对象类型一样的对象
		T objectCopy = (T) classType.getConstructor(new Class[] {})
				.newInstance(new Object[] {});
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			Object value = null;
			// 根据字段类型决定结果集中使用哪种get方法从数据中取到数据
			if (field.getType().equals(String.class)) {
				value = rs.getString(fieldName);
				if (value == null) {
					value = "";
				}
			}
			if (field.getType().equals(int.class)) {
				value = rs.getInt(fieldName);
			}
			if (field.getType().equals(java.util.Date.class)) {
				value = rs.getDate(fieldName);
			}
			// 获得属性的首字母并转换为大写，与setXXX对应
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String setMethodName = "set" + firstLetter + fieldName.substring(1);
			Method setMethod = classType.getMethod(setMethodName,
					new Class[] { field.getType() });
			setMethod.invoke(objectCopy, new Object[] { value });// 调用对象的setXXX方法
		}

		return objectCopy;
	}

	/**
	 * map 转 Bean
	 * 
	 * @param mpFrom
	 * @param objTo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object mapToBean(Map mpFrom, Object objTo) {
		Object[] objKeys = mpFrom.keySet().toArray();
		String strFieldName = "";

		try {
			for (Object objkey : objKeys) {
				strFieldName = objkey.toString();

				Field objField = objTo.getClass().getDeclaredField(
						strFieldName.toLowerCase());
				objField.setAccessible(true);

				objField.set(objTo, mpFrom.get(strFieldName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objTo;
	}

	/**
	 * map 转 Bean
	 * 
	 * @param map
	 * @param cls
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T map2Bean(Map map, T t) {
		Object obj = null;
		Class cls = null;
		try {
			cls = t.getClass();
			obj = cls.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// 取出bean里的所有方法
		Method[] methods = cls.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			// 取方法名
			String methodName = method.getName();
			// 取出方法的类型
			Class[] cc = method.getParameterTypes();
			if (cc.length != 1){
				continue;
			}
			// 如果方法名没有以set开头的则退出本次for
			if (methodName.indexOf("set") < 0){
				continue;
			}
			// 类型
			String type = cc[0].getSimpleName();

			try {
				// 转成小写
				// Object value = method.substring(3).toLowerCase();
				String value = methodName.substring(3, 4).toLowerCase()
						+ methodName.substring(4);
				char[] vb = value.toCharArray();
				String _value = new String(value);
				for(int vi=vb.length-1;vi>=0;vi--){
					String curChar = String.valueOf(vb[vi]);
					if(!StringUtils.isNumeric(curChar)&&curChar.toUpperCase()==curChar){
						_value = _value.substring(0, vi)+"_"+_value.substring(vi);
					}
				}
				value = value.toUpperCase();
				_value = _value.toUpperCase();
//				System.out.println("value = " + value+" ; _value = "+_value);
				// 如果map里有该key
				if (map.containsKey(value) && map.get(value) != null) {
					// 调用其底层方法
					setValue(type, map.get(value), i, methods, obj);
				} else if(map.containsKey(_value) && map.get(_value) != null){
					// 调用其底层方法
					setValue(type, map.get(_value), i, methods, obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (T)obj;
	}

	/**
	 * 将bean转成Map
	 * @param t
	 * @return
	 * @author chenyub@yonyou.com
	 */
	@SuppressWarnings("rawtypes")
	public static <T> Map<String, Object> bean2Map(T t){
		Map<String, Object> map = new HashMap<String, Object>();
		Class cls = null;
		try {
			cls = t.getClass();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Field[] flds = cls.getDeclaredFields();
		if(!ArrayUtils.isEmpty(flds)){
			for (Field field : flds) {
				String fieldName = field.getName();
				String setter = "get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
				try{
					Method curMethod = cls.getMethod(setter, new Class[]{});
					map.put(fieldName, curMethod.invoke(t, new Object[]{}));
				}catch(NoSuchMethodException e){
					e.printStackTrace();
					continue;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
	
	/**
	 * 把Resultset对象的数据转成Map
	 * @param rs
	 * @return
	 * @throws SQLException
	 * @author chenyub@yonyou.com
	 */
	public static Map<String, Object> rs2Map(ResultSet rs) throws SQLException {
		Map<String, Object> hm = new HashMap<String, Object>();  
        ResultSetMetaData rsmd = rs.getMetaData();  
        int count = rsmd.getColumnCount();  
        for (int i = 1; i <= count; i++) {  
            String key = rsmd.getColumnLabel(i);  
            Object value = rs.getObject(i);  
            hm.put(key, value);  
        }  
        return hm;  
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getSubmitBean(RequestWrapper request, T object) {
		Class<?> classType = object.getClass();
		Field[] fields = classType.getDeclaredFields();// 得到对象中的字段
		// 每次循环时，重新实例化一个与传过来的对象类型一样的对象
		T objectCopy = null;
		try {
			objectCopy = (T) classType.getConstructor(new Class[] {}).newInstance(new Object[] {});
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String fieldName = field.getName();
				// 获得属性的首字母并转换为大写，与setXXX对应
				String firstLetter = fieldName.substring(0, 1).toUpperCase();
				String setMethodName = "set" + firstLetter
						+ fieldName.substring(1);
				Method setMethod;
				try {
					setMethod = classType.getMethod(setMethodName,
							new Class[] { field.getType() });
					Object value = null;
					// 根据字段类型决定结果集中使用哪种get方法从数据中取到数据
//					if (field.getType().equals(String.class)) {
						value = request.getParamValue(fieldName);
						if (value == null) {
							continue;
						}
//					}
					setValue(value, setMethod, objectCopy);
					setMethod.invoke(objectCopy, new Object[] { value });// 调用对象的setXXX方法
				} catch (SecurityException e) {
					e.printStackTrace();
					continue;
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
					continue;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					continue;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					continue;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					continue;
				}

			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return objectCopy;
	}
	

	/**
	 * 调用底层方法设置值
	 */
	private static void setValue(String type, Object value, int i,
			Method[] method, Object bean) {
		if (value != null && !value.equals("")) {
			try {
				if (type.equals("String")) {
					// 第一个参数:从中调用基础方法的对象 第二个参数:用于方法调用的参数
					method[i].invoke(bean, new Object[] { value });
				} else if (type.equals("int") || type.equals("Integer")) {
					method[i].invoke(bean, new Object[] { new Integer(""
							+ value) });
				} else if (type.equals("double") || type.equals("Double")) {
					method[i].invoke(bean,
							new Object[] { new Double("" + value) });
				} else if (type.equals("float") || type.equals("Float")) {
					method[i].invoke(bean,
							new Object[] { new Float("" + value) });
				} else if (type.equals("long") || type.equals("Long")) {
					method[i].invoke(bean,
							new Object[] { new Long("" + value) });
				} else if (type.equals("boolean") || type.equals("Boolean")) {
					method[i].invoke(bean,
							new Object[] { Boolean.valueOf("" + value) });
				} else if (type.equals("BigDecimal")) {
					method[i].invoke(bean, new Object[] { new BigDecimal(""
							+ value) });
				} else if (type.equals("Date")) {
					Date date = null;
					if (value.getClass().getName().equals("java.util.Date")) {
						date = (Date) value;
					} else {
						String format = (value.toString()).indexOf(":") > 0 ? "yyyy-MM-dd hh:mm:ss"
								: "yyyy-MM-dd";
						SimpleDateFormat sf = new SimpleDateFormat();
						sf.applyPattern(format);
						date = sf.parse(value.toString());
					}
					if (date != null) {
						method[i].invoke(bean, new Object[] { date });
					}
				} else if (type.equals("byte[]")) {
					method[i].invoke(bean,
							new Object[] { new String(value + "").getBytes() });
				}
			} catch (Exception e) {
				System.out
						.println("将linkHashMap 或 HashTable 里的值填充到javabean时出错,请检查!");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 调用底层方法设置值
	 */
	@SuppressWarnings("rawtypes")
	private static void setValue(Object value, Method method,
			Object bean) {
		if (value != null && !value.equals("")) {
			try {
				
				Class[] cc = method.getParameterTypes();
				String type = cc[0].getSimpleName();
				if (type.equals("String")) {
					// 第一个参数:从中调用基础方法的对象 第二个参数:用于方法调用的参数
					method.invoke(bean, new Object[] { value });
				} else if (type.equals("int") || type.equals("Integer")) {
					method.invoke(bean, new Object[] { new Integer(""
							+ value) });
				} else if (type.equals("double") || type.equals("Double")) {
					method.invoke(bean,
							new Object[] { new Double("" + value) });
				} else if (type.equals("float") || type.equals("Float")) {
					method.invoke(bean,
							new Object[] { new Float("" + value) });
				} else if (type.equals("long") || type.equals("Long")) {
					method.invoke(bean,
							new Object[] { new Long("" + value) });
				} else if (type.equals("boolean") || type.equals("Boolean")) {
					method.invoke(bean,
							new Object[] { Boolean.valueOf("" + value) });
				} else if (type.equals("BigDecimal")) {
					method.invoke(bean, new Object[] { new BigDecimal(""
							+ value) });
				} else if (type.equals("Date")) {
					Date date = null;
					if (value.getClass().getName().equals("java.util.Date")) {
						date = (Date) value;
					} else {
						String format = (value.toString()).indexOf(":") > 0 ? "yyyy-MM-dd hh:mm:ss"
								: "yyyy-MM-dd";
						SimpleDateFormat sf = new SimpleDateFormat();
						sf.applyPattern(format);
						date = sf.parse(value.toString());
					}
					if (date != null) {
						method.invoke(bean, new Object[] { date });
					}
				} else if (type.equals("byte[]")) {
					method.invoke(bean,
							new Object[] { new String(value + "").getBytes() });
				}
			} catch (Exception e) {
				System.out
						.println("将linkHashMap 或 HashTable 里的值填充到javabean时出错,请检查!");
				e.printStackTrace();
			}
		}
	}

	public static void copyProperties(Object src, Object target){
		org.springframework.beans.BeanUtils.copyProperties(src, target);
	}
}
