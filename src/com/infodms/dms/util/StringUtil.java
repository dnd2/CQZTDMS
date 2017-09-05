package com.infodms.dms.util;

import java.math.BigDecimal;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.apache.log4j.Logger;

public class StringUtil {
	private static Logger logger = Logger.getLogger(StringUtil.class);
	public static String conCateNatePhoneNum(String telpre,String tel){
		String returnStr = "";
		if(telpre != null && !"".equalsIgnoreCase(telpre) && !"null".equalsIgnoreCase(telpre)){
			returnStr = telpre+"-";
		}
		if(tel != null && !"".equalsIgnoreCase(tel) && !"null".equalsIgnoreCase(tel)){
			returnStr = returnStr + tel;
		}
		return returnStr;
	}
	/**
	 * @author Andy Z
	 * 使用 sign 链接两个字符串
	 * for example:
	 * concatenateWithSign("ABC","DEF","/") 
	 * return "ABC/DEF"
	 * 页面如果需要换行，可以sign 传入 "<BR>"
	 * 
	 * @param str1
	 * @param str2
	 * @param sign
	 * @return
	 */
	public static String concatenateWithSign(String str1,String str2,String sign){
		String returnStr = "";
		if(str1 != null && !"".equalsIgnoreCase(str1) && str2 != null && !"".equalsIgnoreCase(str2)){
			returnStr = (str1+sign+str2);
		}else{
			if(str1 != null && !"".equalsIgnoreCase(str1)){
				returnStr = (str1);
			}
			if(str2 != null && !"".equalsIgnoreCase(str2)){
				returnStr = (str2);
			}
		}
		return returnStr;
	}
	
	/**
	 * @function:将String类型的数值转换成 乘以 一定比率的 Double类型
	 * @param str
	 * @param ratio
	 * @return
	 * @author Andy Z
	 */
	public static Double switchDubFromStr(String str,double ratio){
		double dubStr = 0.00;
		BigDecimal bd = null;
		if(str != null && !"".equalsIgnoreCase(str)){
			dubStr = Double.parseDouble(str);
			dubStr = Double.parseDouble(StringUtil.mul(dubStr, ratio));
			bd = new BigDecimal(dubStr+"");
			bd = bd.setScale(4);
			return bd.doubleValue();
		}else{
			return dubStr;
		}	
	}
	/**
	 * @function:将Double类型的数值转换成 乘以 一定比率的 String类型
	 * @param sub
	 * @param ratio
	 * @return
	 * @author Andy Z
	 */
	public static String switchStrFromDub(Double sub,double ratio){
		String str = "0";
		if(sub != null){
			str = StringUtil.mul(sub.doubleValue(),ratio);
//			System.out.println("chuanru"+sub.doubleValue());
//			System.out.println("xishu====="+ratio);
//			System.out.println("xishu2===="+new BigDecimal(ratio).setScale(4,2));
//			System.out.println("xishu3===="+new BigDecimal(ratio).setScale(4,2).doubleValue());
//			BigDecimal bd = new BigDecimal(subStr+"");
//			bd = bd.setScale(4,2);
//			str = bd.doubleValue() + "";
			
		}
		return StringUtil.mulZero(str);
	}
	/**
	 * @function:将Integer类型的数值转换成 乘以 一定比率的 String类型
	 * @param sub
	 * @param ratio
	 * @return
	 * @author Andy Z
	 */
	public static String switchStrFromInt(Double sub,int ratio){
		String str = "0";
		if(sub != null){
			int subStr = sub.intValue() * ratio;
			str = subStr + "";
		}
		return str;
	}
	/**
	 * 取汉字首字母
	 * @param str
	 * @return
	 */
	public static String getAllFirstLetter(String str) {
        String convert = "";
        char fstCh = str.charAt(0);
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(fstCh);
        return convert += pinyinArray[0].charAt(0);
    }
	
	/**
	 * double 乘法
	 */
	public static String mul(double d1,double d2){
		BigDecimal bd1 = new BigDecimal(Double.toString(d1)).setScale(4,2);
		BigDecimal bd2 = new BigDecimal(Double.toString(d2)).setScale(4,2);
		return bd1.multiply(bd2).setScale(4,2).toString();
	}
	public static String mulZero(String str){
		String returnStr = "";
		boolean flag = false;
		if(str != ""){
			returnStr = str;
			int pointAt = returnStr.length();
			if(returnStr.indexOf(".") != -1){
				pointAt = returnStr.indexOf(".");
			}
			for(int i = returnStr.length()-1; i >= 0 ; i--){
				if((i > pointAt-1 )&&( "0".equalsIgnoreCase(returnStr.charAt(i)+"") || ".".equalsIgnoreCase(returnStr.charAt(i)+""))){
					returnStr = returnStr.substring(0,i);
				}else{
					break;
				}
			}
		}
		return returnStr;
	}
	
	/**
	 * 
	 * @param str1
	 * @param str2
	 * @param flag 是否以百分比输出
	 * @return
	 * str1 / str2
	 */
	public static String div(String str1,String str2,boolean flag){
		String returnStr = "";
		if(str1 != null && str2 != null && !"".equalsIgnoreCase(str1) && !"".equalsIgnoreCase(str2)){
			if(Double.parseDouble(str2) != 0){
				BigDecimal bd1 = new BigDecimal(str1).setScale(4,2);
				BigDecimal bd2 = new BigDecimal(str2).setScale(4,2);
				returnStr = bd1.divide(bd2,4,BigDecimal.ROUND_HALF_UP).setScale(4,2).toString();
				returnStr = StringUtil.mulZero(returnStr);
				if(flag){
					BigDecimal returnBD = new BigDecimal(returnStr).setScale(2,2);
					returnStr = StringUtil.mulZero((returnBD.multiply(new BigDecimal("100"))).toString()) + "%"; 
				}
			}
		}
		return returnStr;
		
	}
	/**
	 * @param str
	 * @return val=null or val = "" return true
	 * added by wangwenhu 2010-03-18
	 */
	public static boolean notNull(String str){
		return !(str== null || "".equals(str.trim()));
	}
	public static boolean isNull(String str){
		return (str== null || "".equals(str.trim()));
	}
	
	/**
	 * 
	* @Title: compileStr 
	* @Description: TODO(将带逗号的字符串组装成带单引号) 
	* @param @param str  "1,2,3,4"
	* @return String    返回类型 '1','2','3','4'
	* @throws
	 */
	public static String compileStr(String str) {
		StringBuilder strBuilder = new StringBuilder();
		if (isNull(str)) {
			throw new IllegalArgumentException("Invalid str, str == " + str);
		}
		String[] ss = str.split(",");
		for (int i = 0; i < ss.length; i++) {
			if (i == ss.length - 1) {
				strBuilder.append("'").append(ss[i]).append("'");
				break;
			}
			if (!isNull(ss[i])) {
				strBuilder.append("'").append(ss[i]).append("'").append(",");
			}
		}
		return strBuilder.toString();
	}
	/**
	 * 
	* @Title: compileStr 
	* @Description: TODO(将list的id转化成1,2,3  用于sql的in语句) 
	* @param @param ids
	* @return String   1,2,3
	* @throws
	 */
	public static String compileStr(List<Long> ids) {
		StringBuilder strBuilder = new StringBuilder();
		for (Long id : ids) {
			strBuilder.append(id);
			strBuilder.append(",");
		}
		if (strBuilder.length() > 0) {
			strBuilder.deleteCharAt(strBuilder.length() - 1);
		}
		return strBuilder.toString();
	}
	
	//格式化字符串中包含的特殊字符
	public static String fmtSpecialStr(String str){
		String newStr = str ;
		if(str.contains("-"))
			newStr = newStr.replace("-","_");
		if(str.contains("."))
			newStr = newStr.replace(".","__");
		if(str.contains("("))
			newStr = newStr.replace("(", "__");
		if(str.contains(""))
			newStr = newStr.replace(")", "__");
		if(str.contains("/"))
			newStr = newStr.replace("/","__");
		if(str.contains("+"))
			newStr = newStr.replace("+","__");
		if(str.contains("&"))
			newStr = newStr.replace("&","__");
		return newStr ;
	}
	/**
	 * 
	 * @Title      : 判断SQL尾数是WHERE
	 * @Description: TODO 判断SQL是否以WHERE结尾，是则不用拼接'and'，否则需要。
	 * @param      : @param sql 
	 * @return     : Boolean
	 * @author     : Wangming
	 * LastDate    : 2013-4-2
	 */
	public static boolean isEndWithWhere(String sql){
		return sql.trim().toLowerCase().endsWith("where");
	}
	
	/**
	 * 查询条件有IN的，将参数带入原来的查询SQL中
	 * 
	 * @param queryString
	 * @param params
	 * @return
	 */
	public static String getSqlCondition(String queryString, List<Object> params) {
		if (queryString != null && !"".equals(queryString))
		{
			String[] splitString = queryString.split(",");
			String splitConnectString = "";
			for (int i = 0; i < splitString.length; i++)
			{
				splitConnectString += "?,";
				params.add(splitString[i]);
			}
			splitConnectString = splitConnectString.substring(0, splitConnectString.length() - 1);

			return splitConnectString;
		}
		else
		{
			return "''";
		}
	}

	/**
	 * 查询条件有IN的，将参数带入原来的查询SQL中
	 * 
	 * @param queryString
	 * @param params
	 * @return
	 */
	public static String getSqlCondition(String[] splitString, List<Object> params) {
		if (splitString != null)
		{
			String splitConnectString = "";
			for (int i = 0; i < splitString.length; i++)
			{
				splitConnectString += "?,";
				params.add(splitString[i]);
			}
			splitConnectString = splitConnectString.substring(0, splitConnectString.length() - 1);

			return splitConnectString;
		}
		else
		{
			return "''";
		}
	}
	
//	public static void main(String args[]) {
//		String a = "5060";
//		String b = "17.0001";
//		String c = "15.6540";
//		String d = "16.0000";
//		System.out.println("16.0000====>"+StringUtil.mulZero(d));
//		System.out.println("5060====>"+StringUtil.mulZero(a));
//		System.out.println("17.0001====>"+StringUtil.mulZero(b));
//		System.out.println("15.6540====>"+StringUtil.mulZero(c));
//	}
}
