/**********************************************************************
* <pre>
* FILE : MyPattern.java
* CLASS : MyPattern
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
* $Id: MyPattern.java,v 1.1 2010/08/16 01:44:17 yuch Exp $
*/

package com.infodms.dms.common;

import org.apache.log4j.Logger;

import com.infodms.dms.exception.BizException;
/**
 * Function    : 验证模式抽象类
 * @author     : SuMMeR
 * CreateDate  : 2009-9-15
 * @version    :
 */
public abstract class MyPattern
{
	/**
	 *  
	 * Function    : 实例化方法,可能返回null，注意处理
	 * LastUpdate  : 2009-9-15
	 * @param pattern
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws Exception 
	 */
	@SuppressWarnings("finally")
	public static MyPattern getInstance(String pattern, String actionKey, int minLength, int maxLength) throws Exception
	{
		MyPattern myPattern = null;
		try
		{
			myPattern = (MyPattern) Class.forName("com.infodms.dms.common.pattern." + pattern).newInstance();
			myPattern.minLength = minLength;
			myPattern.maxLength = maxLength;
			myPattern.actionKey = actionKey;
		}
		finally
		{
			return myPattern;
		}
	}

	/**
	 *  
	 * Function    : 验证方法(抽象)
	 * LastUpdate  : 2009-9-15
	 * @param param
	 * @param pattern
	 * @param container
	 */
	public abstract void compile(ErrorMsgContainer container, Object... parms) throws BizException;

	/**
	 * 
	 * Function    : 如果错误容器数量已满则抛出错误信息，不再继续验证
	 * LastUpdate  : 2009-9-15
	 * @param container
	 * @throws Exception
	 */
	/*
	public void errMsgContainerFull(ErrorMsgContainer container) throws BizException
	{
		if (container.isFull())
		{
			throw new BizException(act, ErrorCodeConstant.NOT_MATCH, container.getAllErrorMsg());
		}
	}
	*/
	//protected ActionContext act;
	protected String actionKey;

	protected int minLength;

	protected int maxLength;

	public static Logger logger = Logger.getLogger(MyPattern.class);
}
