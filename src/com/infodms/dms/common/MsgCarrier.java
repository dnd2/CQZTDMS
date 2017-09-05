/**********************************************************************
* <pre>
* FILE : MsgCarrier.java
* CLASS : MsgCarrier
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
* 		  |2009-9-16| SuMMeR| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: MsgCarrier.java,v 1.1 2010/08/16 01:44:17 yuch Exp $
*/

package com.infodms.dms.common;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2009-9-16
 * @version    : v0.1
 */
public class MsgCarrier
{
	public static MsgCarrier getInstance(String pattrenCode, String actionKey, int minLength, int maxLength, String... params) throws Exception
	{
		MsgCarrier carrier = new MsgCarrier();
		carrier.params = params;
		carrier.pattern = MyPattern.getInstance(pattrenCode, actionKey, minLength, maxLength);
		return carrier;
	}

	public MyPattern getPattern()
	{
		return pattern;
	}

	public void setPattern(MyPattern pattern)
	{
		this.pattern = pattern;
	}

	public String[] getParams()
	{
		return params;
	}

	public void setParams(String[] params)
	{
		this.params = params;
	}

	// 参数
	private String[] params;

	// 验证模式
	private MyPattern pattern;
}
