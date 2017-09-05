/**********************************************************************
* <pre>
* FILE : RightMessageConstant.java
* CLASS : RightMessageConstant
*
* AUTHOR : xianchao zhang
*
* FUNCTION : 正确提示信息变量
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
* 		  |Aug 25, 2009| xianchao zhang| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: RightMessageConstant.java,v 1.1 2010/08/16 01:44:17 yuch Exp $
*/
package com.infodms.dms.common;

/**
 * 功能说明：
 * 典型用法：
 * 示例程序如下：
 * 特殊用法：
 * 创建者：xianchao zhang
 * 创建时间：Aug 25, 2009
 * 修改人：
 * 修改时间：
 * 修改原因：
 * 修改内容：
 * 版本：0.1
 */
public class RightMessageConstant {
	/**
	 * --------------------------------正确提示信息变量--------开始--------------------------
	 */
	//成功提示信息的标识，作为Action执行成功之后的提示信息的标识
	public static String SUCCESS_MESSAGE = "SUCCESS_MESSAGE"; //act.setOutData(key,value)中的Key
	//保存成功提示信息的标识，即key
	public static String ADD_MESSAGE = "M00000001"; //{0}:{1}保存成功
	
	//更新成功提示信息的标识，即key
	public static String UPDATE_MESSAGE = "M00000002"; //{0}:{1}更新成功
	
	//删除成功提示信息的标识，即key
	public static String DELETE_MESSAGE = "M00000003"; //{0}:{1}更新成功
	
	
	//保存成功提示信息的标识，即key
	public static String ADD_MENU_MESSAGE = "M00000004"; //{0}:{1}保存成功！点击确认返回查询界面或者点击左边菜单进入其他功能
	
	//更新成功提示信息的标识，即key
	public static String UPDATE_MENU_MESSAGE = "M00000005"; //{0}:{1}更新成功！点击确认返回查询界面或者点击左边菜单进入其他功能
	
	//删除成功提示信息的标识，即key
	public static String DELETE_MENU_MESSAGE = "M00000006"; // {0}:{1}删除成功！点击确认返回查询界面或者点击左边菜单进入其他功能
	
	//已经存在，是否更新？
	public static String YES_OR_NO_UPDATE_MESSAGE = "M00000007"; // {0}:{1}已经存在，是否更新?
	
	//失效成功提示信息的标识，即key
	public static String DISABLE_MESSAGE = "M00000008"; // {0}:{1}失效成功
	
	//操作成功提示信息的标识，即key
	public static String OPERATE_SUCCESS_MESSAGE = "M00000009"; // {0}操作成功！点击确认返回查询界面或者点击左边菜单进入其他功能
	
	//提交成功提示信息的标识，即key
	public static String PUTIN_MENU_MESSAGE = "M00000010"; //{0}:{1}提交成功！点击确认返回查询界面或者点击左边菜单进入其他功能
	
	//审批成功提示信息的标识，即key
	public static String APPROVAL_MENU_MESSAGE = "M00000011"; //{0}:{1}审批成功！点击确认返回查询界面或者点击左边菜单进入其他功能
	
	/**
	 * --------------------------------结束---------------------------------------------
	 */
}
