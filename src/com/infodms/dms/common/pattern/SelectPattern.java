/**********************************************************************
* <pre>
* FILE : NullPattern.java
* CLASS : NullPattern
*
* AUTHOR : xianchao zhang
*
* FUNCTION : TODO
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
* 		  |Oct 12, 2009| xianchao zhang| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: SelectPattern.java,v 1.1 2010/08/16 01:43:19 yuch Exp $
*/
package com.infodms.dms.common.pattern;

import com.infodms.dms.common.ErrorMsgContainer;
import com.infodms.dms.common.MyPattern;
import com.infodms.dms.common.ValidateUtil;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;

/**
 * 功能说明：
 * 典型用法：
 * 示例程序如下：
 * 特殊用法：
 * 创建者：xianchao zhang
 * 创建时间：Oct 12, 2009
 * 修改人：
 * 修改时间：
 * 修改原因：
 * 修改内容：
 * 版本：0.1
 */
public class SelectPattern extends MyPattern {

	@Override
	public void compile(ErrorMsgContainer container, Object... parms)
			throws BizException {
		String value = CommonUtils.checkNull((String) parms[0]);
		if(value.length() == 0 && minLength != 0) {
			container.addErrorMsg(actionKey + "不能为空");
		}else{
			if (!ValidateUtil.isSelect(value,minLength, maxLength))
			{
				container.addErrorMsg(actionKey + "格式不正确");
			}
		}
	}

}
