/**********************************************************************
* <pre>
* FILE : ValidateCodeConstant.java
* CLASS : ValidateCodeConstant
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
* 		  |2009-9-15| SuMMeR| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: ValidateCodeConstant.java,v 1.1 2010/08/16 01:44:17 yuch Exp $
*/

package com.infodms.dms.common;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2009-9-15
 * @version    : V0.1
 */
public class ValidateCodeConstant
{
	// 错误信息每次显示条数
	public static int ERR_MSG_PAGE_SIZE = 5;

	/********************公**用**模**式***************************************/
	// VIN号验证模式
	public static String VIN_PATTERN = "VinPattern";

	// 车牌号验证模式
	public static String VHCL_LIC_PATTERN = "VhclLicPattern";
	
	// 字母及数字模式
	public static String CHAR_AND_NUMBER = "CharAndNumberPattern";
	
	/**
	 * 验证日期
	 */
	public static final String DATE_PATTERN = "DatePattern";
	
	/**
	 * 验证数字中文字母
	 */
	public static final String DIGIT_LETTER_CN_PATTERN = "DigitLetterCnPattern";
	
	/**
	 * 验证数字和字母
	 */
	public static final String DIGIT_LETTER_PATTERN = "DigitLetterPattern";
	
	/**
	 * 验证数字和字母-_
	 */
	public static final String DOC_NUMBER_PATTERN = "DocNumberPattern";
	
	/**
	 * 验证正整数
	 */
	public static final String DIGIT_PATTERN = "DigitPattern";
	
	/**
	 * 验证正浮点数
	 */
	public static final String DOUBLE_PATTERN = "DoublePattern";
	
	/**
	 * 验证Email
	 */
	public static final String EMAIL_PATTERN = "EmailPattern";
	
	/**
	 * 验证字母和中文组合
	 */
	public static final String LETTER_CN_PATTERN = "LetterCnPattern";
	
	/**
	 * 验证发动机号
	 */
	public static final String FDJH_PATTERN = "FdjhPattern";
	
	/**
	 * 验证万
	 */
	public static final String WAN_PATTERN = "WanPattern";
	
	/**
	 * 验证圆
	 */
	public static final String YUAN_PATTERN = "YuanPattern";
	
	/**
	 * 验证',%
	 */
	public static final String NOQUOTATION_PATTERN = "NoquotationPattern";
	
	/**
	 * 验证长度
	 */
	public static final String LENGTH_PATTERN = "LengthPattern";
	/**
	 * 验证下拉框
	 */
	public static final String SELECT_PATTERN = "SelectPattern";
	/**
	 * 验证名称
	 */
	public static final String NAME_PATTERN = "NamePattern";
	/**
	 * 验证电话
	 */
	public static final String PHONE_PATTERN = "PhonePattern";
	/***********************************************************************/
}
