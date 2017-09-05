/**********************************************************************
* <pre>
* FILE : DigitLetterCnPattern.java
* CLASS : DigitLetterCnPattern
* 
* AUTHOR : ChenLiang
*
* FUNCTION : 验证数字中文字母.
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

package com.infodms.dms.common.pattern;

import com.infodms.dms.common.ErrorMsgContainer;
import com.infodms.dms.common.MyPattern;
import com.infodms.dms.common.ValidateUtil;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;

public class NamePattern extends MyPattern
{
	public void compile(ErrorMsgContainer container, Object... parms) throws BizException
	{
		String value = CommonUtils.checkNull((String) parms[0]);
		if(value.length() == 0 && minLength != 0) {
			container.addErrorMsg(actionKey + "不能为空");
		}else{
			if (!ValidateUtil.isName(value,minLength, maxLength))
			{
				container.addErrorMsg(actionKey + "不能输入数字、字母、-、_和中文以外的字符.");
			}
		}
	}

}
