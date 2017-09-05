/**********************************************************************
* <pre>
* FILE : ErrorMsgContainer.java
* CLASS : ErrorMsgContainer
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
* $Id: ErrorMsgContainer.java,v 1.1 2010/08/16 01:44:17 yuch Exp $
*/

package com.infodms.dms.common;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2009-9-15
 * @version    :
 */
public class ErrorMsgContainer
{
	/**
	 * 
	 * Function    : 错误信息容器
	 * LastUpdate  : 2009-9-15
	 * @return
	 */
	public static ErrorMsgContainer getInstance()
	{
		ErrorMsgContainer container = new ErrorMsgContainer();
		container.errMsg = new StringBuffer("");
		container.errorNum = 0;
		return container;
	}

	/**
	 * 
	 * Function    : 获取全部错误信息
	 * LastUpdate  : 2009-9-15
	 * @return
	 */
	public StringBuffer getAllErrorMsg()
	{
		return errMsg;
	}

	/**
	 * 
	 * Function    : 增加错误信息
	 * LastUpdate  : 2009-9-15
	 * @param err
	 */
	public void addErrorMsg(String err)
	{
		if(errorNum==0){
			errMsg.append((errorNum + 1) + ": " + err);
		}else{
			errMsg.append("<br/>" + (errorNum + 1) + ": " + err);
		}
		errorNum++;
	}

	/**
	 *  
	 * Function    : 检查是否有错误信息
	 * LastUpdate  : 2009-9-15
	 * @return
	 */
	public boolean hasErrMsg()
	{
		if (errorNum > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 
	 * Function    : 验证错误信息是否已满,如果已满则抛出异常
	 * LastUpdate  : 2009-9-15
	 * @return
	 */
	public boolean isFull()
	{
		if (errorNum == ValidateCodeConstant.ERR_MSG_PAGE_SIZE)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// 错误信息
	private StringBuffer errMsg;

	// 错误数量
	private int errorNum;
}
