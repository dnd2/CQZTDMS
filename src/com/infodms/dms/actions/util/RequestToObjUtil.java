package com.infodms.dms.actions.util;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infoservice.mvc.context.RequestWrapper;

public class RequestToObjUtil {
	
	public static List requestToObjList(RequestWrapper request, Class clazz){
		Enumeration<String> enu = request.getParamNames();
		try {
			String clazzname = clazz.getName().substring(clazz.getName().lastIndexOf(".")+1);
			String lowerNmae =  clazzname.substring(0, 1).toLowerCase()+clazzname.substring(1);
			Map<String,Object> mapObj = new HashMap<String, Object>();
			 while(enu.hasMoreElements()){
				 String key = enu.nextElement();
				  String value = request.getParamValue(key);
				  String[] classnameAndPropertyName = key.split("\\.");
				  if(classnameAndPropertyName.length==2){
						 if((null!=value)&&(null!=classnameAndPropertyName[0]&&classnameAndPropertyName[0].length()>0) && (null!=classnameAndPropertyName[1]&&classnameAndPropertyName[1].length()>0)){
							 String pname= classnameAndPropertyName[0].substring(0, 1).toLowerCase()+classnameAndPropertyName[0].substring(1);//参数name
							 String[] nameAndSequences = pname.split("\\*#\\*");
							 if((nameAndSequences.length==2)&&(null!=nameAndSequences[0]&&nameAndSequences[0].length()>0)&&(null!=nameAndSequences[1]&&nameAndSequences[1].length()>0)){
								 if(nameAndSequences[0].equals(lowerNmae)){
									 Object obj = null;
									 if(null!=mapObj.get(nameAndSequences[1])){
										 obj = mapObj.get(nameAndSequences[1]);
									 }else{
										 obj = clazz.newInstance();
										 mapObj.put(nameAndSequences[1], obj);
									 }
									 if(null!=obj){
										 String setMethodName ="set"+classnameAndPropertyName[1].substring(0, 1).toUpperCase()+classnameAndPropertyName[1].substring(1);
										 String type = clazz.getDeclaredField(classnameAndPropertyName[1]).getType().toString().substring( clazz.getDeclaredField(classnameAndPropertyName[1]).getType().toString().lastIndexOf(".")+1);
										 Method setMethod = clazz.getMethod(setMethodName, new Class[] { clazz.getDeclaredField(classnameAndPropertyName[1]).getType() });
										 setMethod.invoke(obj, typeConversion(value,type));
									 }
								 }
							 }
						 }

				  }
			 }
			 List list =  new  ArrayList(mapObj.values());   
			 return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Object requestToObj(RequestWrapper request, Class clazz){
		Enumeration<String> enu = request.getParamNames();
		try {
			Object obj = clazz.newInstance();
			String clazzname = clazz.getName().substring(clazz.getName().lastIndexOf(".")+1);
			String lowerNmae =  clazzname.substring(0, 1).toLowerCase()+clazzname.substring(1);
			boolean hasValue = false;
			  while(enu.hasMoreElements()){  
				  String key = enu.nextElement();
				  String value = request.getParamValue(key);
				  String[] classnameAndPropertyName = key.split("\\.");
				  if(classnameAndPropertyName.length==2){
					 if((null!=value)&&(null!=classnameAndPropertyName[0]&&classnameAndPropertyName[0].length()>0) && (null!=classnameAndPropertyName[1]&&classnameAndPropertyName[1].length()>0)){
						 String pname= classnameAndPropertyName[0].substring(0, 1).toLowerCase()+classnameAndPropertyName[0].substring(1);//参数name
						 if(pname.equals(lowerNmae)){
							 String setMethodName ="set"+classnameAndPropertyName[1].substring(0, 1).toUpperCase()+classnameAndPropertyName[1].substring(1);
							 String type = clazz.getDeclaredField(classnameAndPropertyName[1]).getType().toString().substring( clazz.getDeclaredField(classnameAndPropertyName[1]).getType().toString().lastIndexOf(".")+1);
							 Method setMethod = clazz.getMethod(setMethodName, new Class[] { clazz.getDeclaredField(classnameAndPropertyName[1]).getType() });
							 setMethod.invoke(obj, typeConversion(value,type));
							 hasValue = true;
						 }
					 }
				  }
		        }  
			  if(hasValue){
				  return obj;
			  }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Object typeConversion(String value, String type) {
		try {
			if(null==value||value.equals("null")){
				return null;
			}
			if(type.equals("String")){
				return value;
			}else if(type.equals("Double")||type.equals("double")){
				value = value.replace(",", "");
				return Double.parseDouble(value);
			}else if(type.equals("Long")||type.equals("long")){
				return Long.parseLong(value);
			}else if(type.equals("Integer")||type.equals("int")){
				return Integer.parseInt(value);
			}else if(type.equals("Float")||type.equals("float")){
				 return Float.parseFloat(value);
			}else if(type.equals("Date")){
				try {
					DateFormat df1 =new SimpleDateFormat("yyyy-MM-dd");
					return df1.parse(value);
				} catch (ParseException e) {
					DateFormat df2 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						return df2.parse(value);
					} catch (ParseException e1) {
						return null;
					}
				}
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static void main(String[] args) {
		String s = "324dfsf*#*322fw";
		System.out.println(s.contains("*#*"));
		/*String[] ss = s.split("\\*#\\*");
		//System.out.println(ss[0].toString()+"----"+ss[1].toString());
		String m = "300,000.00";
		m = m.replace(",", "");
		NumberFormat numberFormat = NumberFormat.getNumberInstance();  
		numberFormat.setMinimumFractionDigits(2);
		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMaximumIntegerDigits(10);  
		//System.out.println(numberFormat.format(m));
		System.out.println(Double.parseDouble(m));*/
	}
}
