/**********************************************************************
* <pre>
* FILE : ValidateUtil.java
* CLASS : ValidateUtil
* 
* AUTHOR : ChenLiang
*
* FUNCTION : 数据验证类型规则.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-09-21| ChenLiang  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/

package com.infodms.dms.common;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 后台验证工具类
 * 
 * @author SuMMeR
 */
public class ValidateUtil
{

	/**
	 * 验证是否为车牌号
	 * 
	 * @param vhclLic
	 * @return
	 */
	public static boolean isVhclLic(String vhclLic)
	{
		boolean isVhclLic = false;
		// 验证是否汉字开头
		if (isStartWithChinese(vhclLic))
		{
			// 如果是判断后面是否为大写字母和数字组成的0-8未字符串
			isVhclLic = isMajusculeAndNum(vhclLic.substring(1), 0, 8);
		}
		else
		{
			// 如果不是,验证是否为大写字母和数字组成的0-8未字符串
			isVhclLic = isMajusculeAndNum(vhclLic, 0, 8);
		}
		return isVhclLic;
	}

	/**
	 * 验证是否为VIN
	 * 
	 * @param vin
	 * @return
	 */
	public static boolean isVin(String vin)
	{
		boolean isVin = false;
		if(vin == null || "".equals(vin) || vin.length() != 17) {
			return isVin;
		}
		// 验证是否为0-17位的大写字母及数字
		isVin = isMajusculeAndNum(vin, 0, 17);
		return isVin;
	}

	/**
	 * 验证是否为认证号
	 * 
	 * @param authNum
	 * @return
	 */
	public static boolean isAuthNum(String authNum)
	{
		// 认证号必须有大写字母与数字组成
		boolean isAuthNum = false;
		isAuthNum = isMajusculeAndNum(authNum, 0, 12);
		return isAuthNum;
	}

	/**
	 * 验证是否中文开头
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isStartWithChinese(String str)
	{
		boolean isChinese = false;
		if (null != str && str.length() > 0)
		{
			char c = str.trim().charAt(0);
			if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
			{
				isChinese = false;
			}
			else
			{
				if (Character.isLetter(c))
				{
					isChinese = true;
				}
				else
				{
					isChinese = false;
				}
			}
		}
		return isChinese;
	}

	/**
	 * 
	 * Function    : 验证是否为字母,数字所组成的x-y位字符串
	 * LastUpdate  : 2009-9-16
	 * @param str
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean isCharAndNum(String str, int x, int y)
	{
		Pattern p = Pattern.compile("^[a-zA-Z0-9]{" + x + "," + y + "}+$");
		return p.matcher(str).find();
	}

	/**
	 * 验证是否为大写字母,数字及'-'所组成的x-y位字符串
	 * 
	 * @param str
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean isMajusculeAndNum(String str, int x, int y)
	{
		Pattern p = Pattern.compile("^[-A-Z0-9]{" + x + "," + y + "}+$");
		return p.matcher(str).find();
	}
	
	/**
	 * 正整数(123456)
	 * @param str
	 * @return
	 */
	public static boolean isDigit(String str, int x, int y) {
		Pattern p = Pattern.compile("^\\d{" + x + "," + y + "}$");
		return p.matcher(str).find();
	}
	
	/**
	 * 验证日期类型数据(2009-11-11)
	 * @param str
	 * @return
	 */
	public static boolean isDate(String str,int x,int y) {
		if(str.length() < x || str.length() > y) {
			return false;
		}
		if(str.length() == 0 && x == 0) {
			return true;
		}
		Pattern p = Pattern.compile("^(\\d{4})-(\\d{1,2})-(\\d{1,2})$");
		return p.matcher(str).find();
	}
	
	/**
	 * 字母和中文组成(sadas哈哈)
	 * @param str
	 * @return
	 */
	public static boolean isLetterCn(String str, int x, int y) {
		Pattern p = Pattern.compile("^[a-zA-Z\u4e00-\u9fa5]{" + x + "," + y + "}$");
		return p.matcher(str).find();
	}
	
	/**
	 * 数字和字母组成(sadas111)
	 * @param str
	 * @return
	 */
	public static boolean isDigitLetter(String str, int x, int y) {
		Pattern p = Pattern.compile("^[a-zA-Z0-9]{" + x + "," + y + "}$");
		return p.matcher(str).find();
	}
	
	/**
	 * 数字和字母组成,-_(sadas111_-)
	 * @param str
	 * @return
	 */
	public static boolean isDocNumber(String str, int x, int y) {
		Pattern p = Pattern.compile("^[a-zA-Z0-9-_]{" + x + "," + y + "}$");
		return p.matcher(str).find();
	}
	
	/**
	 * 数字，字母和中文组成(121sadas哈哈)
	 * @param str
	 * @return
	 */
	public static boolean isDigitLetterCn(String str, int x, int y) {
		Pattern p = Pattern.compile("^[a-zA-Z\u4E00-\u9FA5\\d]{" + x + "," + y + "}$");
		return p.matcher(str).find();
	}
	
	/**
	 * Email(dsds@ddd.com)
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str, int x, int y) {
		if(str.length() < x || str.length() > y) {
			return false;
		}
		if(str.length() == 0 && x == 0) {
			return true;
		}
		Pattern p = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*{" + x + "," + y + "}$");
		return p.matcher(str).find();
	}
	
	/**
	 * 验证长度
	 * @param str
	 * @return
	 */
	public static boolean isLength(String str, int x, int y) {
		if(str.length() >= x && str.length() <= y) {
			return true;
		}
		return false;
	}
	
	/**
	 * 发动机号
	 * @param str
	 * @return
	 */
	public static boolean isFdjh(String str, int x, int y) {
		Pattern p = Pattern.compile("^[a-zA-Z0-9-*]{" + x + "," + y + "}$");
		return p.matcher(str).find();
	}
	
	/**
	 * 万
	 * @param str
	 * @return
	 */
	public static boolean isWan(String str, int x, int y) {
		if(str.length() == 0 && x == 0) {
			return true;
		}
		Pattern p = Pattern.compile("^[0-9]{1,4}(|\\.[0-9]{1,4})$");
		return p.matcher(str).find();
	}
	
	/**
	 * 圆
	 * @param str
	 * @return
	 */
	public static boolean isYuan(String str, int x, int y) {
		if(str.length() == 0 && x == 0) {
			return true;
		}
		Pattern p = Pattern.compile("^[0-9]{1,8}(|\\.[0-9]{1,2})$");
		return p.matcher(str).find();
	}
	
	/**
	 * 不能输入 '%
	 * @param str
	 * @return
	 */
	public static boolean isNoquotation(String str, int x, int y) {
		Pattern p = Pattern.compile("^([^'%]){" + x + "," + y + "}$");
		return p.matcher(str).find();
	}
	
	
	/**
	 * 正浮点数(11.11)
	 * @param str
	 * @return
	 */
	public static boolean isDouble(String str, int x, int y) {
		
		String pattStr = "^\\d{"+x+","+y+"}+(|\\.\\d{1,2})$";
		Pattern p = Pattern.compile(pattStr);
		return p.matcher(str).find();
	}
	/**
	 * add by @author xianchao zhang
	* 功能说明： 验证是否下拉框
	* @param str
	* @param x
	* @param y
	* @return
	* 最后修改时间：Oct 12, 2009
	 */
	public static boolean isSelect(String str,int x,int y){
		if(str.length() == 0 && x == 0) {
			return true;
		}
		return isDigit(str,x,y);
	}
	/**
	 * add by @author xianchao zhang
	 * 验证名称格式
	 * @param str
	 * @return
	 */
	public static boolean isName(String str, int x, int y) {
		Pattern p = Pattern.compile("^[a-zA-Z-_\u4E00-\u9FA5\\d]{" + x + "," + y + "}$");
		return p.matcher(str).find();
	}
	
	/**
	 * add by @author xianchao zhang
	 * 验证电话格式
	 * @param str
	 * @return
	 */
	public static boolean isPhone(String str, int x, int y) {
		if(str.length()>15){
			return false;
		}
		for (int i = 0; i < str.length(); i++){
		    char checkCharacter=str.charAt(i);
            if((checkCharacter>='0'&&checkCharacter<='9')||(checkCharacter=='(')||(checkCharacter==')')||(checkCharacter=='-'))
		    {
			    //alert("合法");
				//合法字符;数字'('')'和'-';
		    }else
			{
				 // alert("非法");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 验证车牌号
	 * @param str
	 * @return
	 */
	public static boolean isCarno(String str, int x, int y) {
		Pattern p = Pattern.compile("^[a-zA-Z-·_\u4E00-\u9FA5\\d]{" + x + "," + y + "}$");
		return p.matcher(str).find();
	}
	
	public static void main(String[] args)
	{
		//System.out.println(isMajusculeAndNum("2001-A1-1-1",0,100));
		//System.out.println("打撒".getBytes().length);
		//System.out.println(isDate("11",0,100));
		System.out.println(isCarno("沪A·1234!5",0,200));
	}
	/**
	 * 是否是金额
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("^(-)?(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,2})?$"); 
	    return pattern.matcher(str).matches();    
	 }
	/**
	 * 是否是金额
	 * @param str
	 * @return
	 */
	public static boolean isNumericOne(String str){ 
	    Pattern pattern = Pattern.compile("^(-)?(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,2})?$"); 
	    return pattern.matcher(str).matches();    
	 } 
	/**
	 * 是否是日期
	 * @param str
	 * @return
	 */
	/*public static boolean isValidDate(String sDate) {  
	     String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";  
	     String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"  
	             + "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"  
	             + "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"  
	             + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("  
	             + "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"  
	             + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";  
	     if ((sDate != null)) {  
	         Pattern pattern = Pattern.compile(datePattern1);  
	         Matcher match = pattern.matcher(sDate);  
	         if (match.matches()) {  
	             pattern = Pattern.compile(datePattern2);  
	             match = pattern.matcher(sDate);  
	             return match.matches();  
	         }  
	         else {  
	             return false;  
	         }  
	     }  
	     return false;  
	}  */
	public static boolean isValidDate(String sDate) {
		/*String[] kk=sDate.split("-");
		if(kk.length>2){
			if(kk[0].length()==2){
				sDate="20"+kk[0]+"-"+kk[1]+"-"+kk[2];
			}
		}*/
        String eL = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1][0-9])|([2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(sDate);
        boolean b = m.matches();
        if ((sDate != null)) {  
        	if (b) {
                return true;
             } else {
            	return false;
             }
	     }  
	     return false;
  }
	/**
	 * 判断是否满足金额
	 * @param str
	 * @return
	 */
	public static boolean isCount(String str){ 
	    Pattern pattern = Pattern.compile("^(\\d+\\.\\d{1,2}|\\d+)$"); 
	    return pattern.matcher(str).matches();    
	 } 
	public static boolean isNum(String str){ 
	    Pattern pattern = Pattern.compile("^\\d+$"); 
	    return pattern.matcher(str).matches();    
	 } 
	public static boolean isDayOfMonth(String str){ 
		boolean flag = true;
	   if(str==null||"".equalsIgnoreCase(str.trim())){
		   flag=false;
	   }else if(str.length()!=8){
		   flag=false;
	   }
		   	Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.set(Calendar.YEAR,Integer.parseInt(str.substring(0, 4)));
			cal.set(Calendar.MONTH, Integer.parseInt(str.substring(4, 6))-1);//Java月份才0开始算
			int dateOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			if(Integer.parseInt(str.substring(6, 8))>dateOfMonth){
				 flag=false;
			}
	    return flag;    
	 } 
}
