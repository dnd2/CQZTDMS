/**********************************************************************
* <pre>
* FILE : YuanPattern.java
* CLASS : YuanPattern
*
* AUTHOR : ChenLiang
*
* FUNCTION : TODO
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
* 		  |2009-10-09| ChenLiang| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/

package com.infodms.dms.common.pattern;

import com.infodms.dms.common.ErrorMsgContainer;
import com.infodms.dms.common.MyPattern;
import com.infodms.dms.common.ValidateUtil;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;

public class YuanPattern extends MyPattern
{
	public void compile(ErrorMsgContainer container, Object... parms) throws BizException
	{
		String value = CommonUtils.checkNull((String) parms[0]);
		if(value.length() == 0 && minLength != 0) {
			container.addErrorMsg(actionKey + "不能为空");
		}else{
			if (!ValidateUtil.isYuan(value,minLength, maxLength))
			{
				container.addErrorMsg(actionKey + "圆格式错误");
			}
		}
	}
}
