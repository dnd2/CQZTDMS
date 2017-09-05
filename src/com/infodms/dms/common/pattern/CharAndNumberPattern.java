/**********************************************************************
* <pre>
* FILE : CharAndNumberPattern.java
* CLASS : CharAndNumberPattern
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
* $Id: CharAndNumberPattern.java,v 1.1 2010/08/16 01:43:19 yuch Exp $
*/

package com.infodms.dms.common.pattern;

import com.infodms.dms.common.ErrorMsgContainer;
import com.infodms.dms.common.MyPattern;
import com.infodms.dms.common.ValidateUtil;
import com.infodms.dms.exception.BizException;

/**
 * Function    : 验证字母及数字组合模式
 * @author     : SuMMeR
 * CreateDate  : 2009-9-16
 * @version    :
 */
public class CharAndNumberPattern extends MyPattern
{

	/* (non-Javadoc)
	 * @see com.jmc.dms.common.MyPattern#compile(com.jmc.dms.common.ErrorMsgContainer, java.lang.Object[])
	 */
	@Override
	public void compile(ErrorMsgContainer container, Object... parms) throws BizException
	{
		String value = (String) parms[0];
		if(value.length() == 0 && minLength != 0) {
			container.addErrorMsg(actionKey + "不能为空");
		}else{
			if (!ValidateUtil.isCharAndNum(value, minLength, maxLength))
			{
				container.addErrorMsg(actionKey + "必须为字母和数字组成");
			}
		}
	}
}
