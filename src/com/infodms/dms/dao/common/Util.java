package com.infodms.dms.dao.common;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;

public class Util {
	private static Map<String, String[][]> pers = new HashMap<String, String[][]>();
	private static Logger logger = Logger.getLogger(Util.class);

	public static String[][] getFieldsNames(String key, ResultSet rs) {
		/*********mod by liuxh 20100803解析Select字段不放缓存**********/
//		String[][] o = pers.get(key);
//
//		if (o == null) {
//			o = getFieldsNameType(rs);
//			putFieldsNames(key, o);
//		}
//		return o;
		return getFieldsNameType(rs);
		/*********mod by liuxh 20100803解析Select字段不放缓存**********/
	}
	public static void putFieldsNames(String sql, String[][] names) {
		pers.put(sql, names);
	}

	/**
	 * @param key
	 * @param rs
	 * @return
	 * @deprecated 新的调用请用{@link} PersisUtil.getMap(ResultSet rs)
	 */
	public static Map<String, Object> getMap(String key, ResultSet rs) {
		return getMapPrivate(rs, 2);
	}
	/**
	 * @param rs
	 * @param deep 0,PersisUtil class; 1, class who invoke PersisUtil; 2, and so on
	 * @return
	 */
	private static Map<String, Object> getMapPrivate(ResultSet rs, int deep) {
		Throwable stackTraceSource = new Throwable();
		StackTraceElement[] stes = stackTraceSource.getStackTrace();

		String key = null;
		if (stes.length >0) {
			StackTraceElement ste = stes[deep];
			key = ste.getClassName().concat(".").concat(ste.getMethodName()).concat(".lineNum:").concat(
					Integer.toString(ste.getLineNumber()));
		} else {
			Random rd = new Random(Long.MAX_VALUE);
			key = Double.toHexString(rd.nextDouble());
		}
		String[][] fileds = getFieldsNames(key.toString(), rs);
		Map<String, Object> result = null;
		try {
			result = new HashMap<String, Object>();
			for (int i = 0; i < fileds[0].length; i++) {
				result.put(fileds[0][i], rs.getObject(i + 1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * @param rs
	 * @return
	 */
	public static Map<String, Object> getMap(ResultSet rs) {
		return getMapPrivate(rs, 2);
	}

	/**
	 * @param key
	 *            object collection keys
	 * @param rs
	 *            query result set
	 * @return Map<String, Object>
	 * @deprecated 新的调用请用{@link} PersisUtil.getMap(ResultSet rs)
	 */
	public static Map<String, Object> getMap2(String key, ResultSet rs) {
			return getMapPrivate(rs, 2);
	}

	/**
	 * 根据结果集得到所有字段名称和类型
	 * 
	 * @param ResultSet
	 *            rs 结果集
	 * @return 2唯字段数组，[0][]为字段名称[1][]为类型
	 */
	private static String[][] getFieldsNameType(ResultSet rs) {
		ResultSetMetaData metaData = null;
		try {
			metaData = rs.getMetaData();

			int i = metaData.getColumnCount();
			String[][] field = new String[2][i];
			for (int j = 0; j < i; j++) {
				field[0][j] = (metaData.getColumnName(j + 1)).toUpperCase();
				field[1][j] = metaData.getColumnClassName(j + 1);
			}
			return field;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("");
		}
	}
	/**
	 * 非空
	 * @param string1
	 * @return
	 */
	public static String notNull(String str) {
		if (str != null&&!str.equals("null"))
			return str;
		else
			return "";
	}
	/**
	 * 数字判断
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String checkStr) { 
	    try { 
	    	checkStr=notNull(checkStr);
	    	int k=checkStr.lastIndexOf(".");
	    	if(k!=-1)
	    		checkStr=checkStr.substring(0,k);
	        Integer.parseInt(checkStr); 
	        return true; // 是数字
	    } catch (NumberFormatException err) { 
	        return false; // 不是数字 
	    } 
	}
	/**
	 * 是否包含
	 * @param list
	 * @param containValue
	 * @return
	 */
	public static boolean contains(List<Long> list,Long containValue){
		boolean falg=false;
		for(Long listValue:list){
			if(listValue.longValue()==containValue.longValue()){
				falg=true;
			}
		}
		return falg;
	}
	/**
	 * 通过物料组查询条件
	 * @param groupCode
	 * @return
	 */
	public static String getGroupQuerySQL(String groupCode){
		StringBuffer buffer = new StringBuffer();
		if (null != groupCode && !"".equals(groupCode)) {
			String[] materialCodes = groupCode.split(",");
			if (null != materialCodes && materialCodes.length>0) {
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length()-1);
			}
		}
		StringBuffer sb=new StringBuffer();
		sb.append("    SELECT   a.GROUP_ID\n");
		sb.append("      FROM   TM_VHCL_MATERIAL_GROUP a\n");
		sb.append("START WITH   a.GROUP_ID IN (SELECT   B.GROUP_ID\n");
		sb.append("                              FROM   TM_VHCL_MATERIAL_GROUP B\n");
		sb.append("                             WHERE   B.GROUP_CODE IN("+buffer.toString()+") )\n");
		sb.append("CONNECT BY   PRIOR a.GROUP_ID = a.parent_group_id\n");
		return sb.toString();
	}
	/**
	 * 车厂产地权限
	 * @param poseId
	 * @param yieldlyColumn
	 * @return
	 */
	public static String getPoseYieldly(String yieldlyColumn){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		StringBuffer sql=new StringBuffer();
		sql.append("AND EXISTS (SELECT 1 \n");
		sql.append("  FROM TM_POSE_BUSINESS_AREA TPBA, TM_BUSINESS_AREA TBA\n" );
		sql.append(" WHERE TPBA.AREA_ID = TBA.AREA_ID\n" );
		sql.append("   AND TBA.STATUS = "+Constant.STATUS_ENABLE+"\n" );
		sql.append("   AND TPBA.POSE_ID = "+logonUser.getPoseId()+" AND TBA.AREA_ID="+yieldlyColumn+")\n");
		return sql.toString();
	}
	public static void main(String[] args) throws Exception{
//		Calendar calendarDay=Calendar.getInstance();
//		calendarDay=null;
//		calendarDay=Calendar.getInstance();
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//		Date date22=sdf.parse("2011-12-31");
//		calendarDay.setTime(date22);
//		System.out.println(sdf.format(calendarDay.getTime()));
//		List al = new ArrayList();
//		  al.add("200,600");
//		  al.add(",400");
//		  al.add("200,400,600");
//		  String appCode = "";
//		  for (int m=0;m<al.size();m++){
//			  String tempStr=al.get(m).toString();
//			  if(tempStr.startsWith(",")){
//				  tempStr=tempStr.replaceFirst(",", "");
//			  }
//			  if(tempStr.endsWith(",")){
//				  tempStr=tempStr.substring(0, tempStr.length()-1);
//			  }
//			  appCode+=tempStr+",";
//	      }
//		  List al1 = new ArrayList();
//		  String[] strs=appCode.split(",");
//		  for(int j=0;j<strs.length;j++){
//				if(!al1.contains(strs[j])){
//					if(!strs[j].trim().equals("")){
//						al1.add(strs[j].trim());
//					}
//				}
//			}
//			String str="";
//			for(int k=0;k<al1.size();k++){
//				str+=al1.get(k).toString()+",";
//			}
//			str=str.substring(0, str.length()-1);
//
//			System.out.println(str);
		jugeVinNull("null");
		
	}
	public static void jugeVinNull(String vin) throws Exception{
    	if(null==vin||vin.trim().equals("")||vin.trim().equalsIgnoreCase("null")||vin.trim().length()<17){
    		throw new Exception("车架号不能为空!");
    	}
    	System.out.println("ghip");
    }
	 
}






