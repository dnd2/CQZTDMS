package com.infodms.dms.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

/**
 * 
 * @Description 工具类
 * @author xiehuibo
 * @date 2014-1-15 下午5:00:32
 * @version 2.0
 */
public class XHBUtil {
	
	private String a = "1";
	
	/**
	 * 为空判断
	 * @param value  判断的值
	 * @param errorValue 如果为空返回的默认值
	 * @return
	 */
	public static String IsNull(Object value , String errorValue){
		if(null == value || "".equals(value)){
			return errorValue;
		}else{
			return value.toString();
		}
	}
	/**
	 * 判断是否为空
	 * @param value
	 * @return
	 */
	public static boolean IsNull(Object value){
		if(null == value || "".equals(value.toString())|| "null".equals(value)){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 通过上传参数拼接查询条件  key 使用po的变量名，在前面添加sql的别名用_ 下划线隔开
	 * @param request
	 * @return
	 */
	public static String getSqlWhere(RequestWrapper request ){
		 Enumeration<String > it = request.getParamNames();
		 StringBuffer sb = new StringBuffer();
		 String tem = "";
		 String temvalue = "";
		 while(it.hasMoreElements()){
			 tem = it.nextElement().replaceAll("!", ".");
			 temvalue = request.getParamValue(tem);
			 if(!IsNull(temvalue)){
				 sb.append(" and ").append(tem).append(" = ").append(temvalue);
			 }
			 
		 }
		return sb.toString();
	}
	/**
	 * 前台jsp传入参数必须和po字段名一样   不区分大小写,但必须无符号和统一大小写
	 * @param request
	 * @param po
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 * @throws ParseException 
	 */
	public static void initPo(RequestWrapper request ,Object po) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException{
		Class c = po.getClass();
		String tem = "";
		SimpleDateFormat sa = new SimpleDateFormat("yyyy-MM-dd");
		for(Field f :c.getDeclaredFields()){
			tem = request.getParamValue(f.getName().toLowerCase());
			if(IsNull(tem)){
				tem = request.getParamValue(f.getName().toUpperCase());
			}
			if(!IsNull(tem)){
					if(f.getType().equals(String.class)){
						c.getMethod("set" + getMethodName(f.getName()),String.class).invoke(po, new Object[]{tem});
					}else if(f.getType().equals(Double.class)){
						c.getMethod("set" + getMethodName(f.getName()),Double.class).invoke(po, new Object[]{Double.parseDouble(tem)});
					}else if(f.getType().equals(Date.class)){
						c.getMethod("set" + getMethodName(f.getName()),Date.class).invoke(po, sa.parse(tem));
//						System.out.println(c.getMethod("get"+getMethodName(f.getName())).invoke(po, null));
					}else if(f.getType().equals(Long.class)){
						c.getMethod("set" + getMethodName(f.getName()),Long.class).invoke(po, new Object[]{Long.parseLong(tem)});
					}
			}
		}
	}
	
	/**
	 * 导出对象
	 * @param content 数据
	 * @param response 
	 * @param fileName 文件名
	 * @throws ParseException
	 */
	public static void createXlsFile(List<List<Object>> content,ResponseWrapper response,String fileName) throws Exception{
		OutputStream os = null;
		WritableWorkbook workbook = null;
		try {
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			os = response.getOutputStream();
			workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("下载模板", 0);
			for(int i=0;i<content.size();i++){
				for(int j = 0;j<content.get(i).size();j++){
						// 添加单元格
						sheet.addCell(new Label(j,i,(content.get(i).get(j) != null ? content.get(i).get(j).toString() : "")));
				}
			}
			workbook.write();
			workbook.close();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			/*if (null != workbook) {
				workbook.close();
			}*/
			if (null != os) {
				os.close();
			}
		}
	}
	
	/**
	 * 得到方法名
	 * @return
	 */
	private static String getMethodName(String fieldName){
		fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		return fieldName;
	}
	
	
}
