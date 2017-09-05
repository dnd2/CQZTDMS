/**********************************************************************
* <pre>
* FILE : VhclLicPattern.java
* CLASS : VhclLicPattern
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
* $Id: VhclLicPattern.java,v 1.1 2010/08/16 01:43:19 yuch Exp $
*/

package com.infodms.dms.common.pattern;

import com.infodms.dms.common.ErrorMsgContainer;
import com.infodms.dms.common.MyPattern;
import com.infodms.dms.common.ValidateUtil;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;

/**
 * Function    : 车牌号验证模式
 * @author     : SuMMeR
 * CreateDate  : 2009-9-15
 * @version    :
 */
public class VhclLicPattern extends MyPattern
{

	/* (non-Javadoc)
	 * @see com.jmc.dms.common.MyPattern#compile(java.lang.String, java.lang.String, com.jmc.dms.common.ErrorMsgContainer)
	 */
	@Override
	public void compile(ErrorMsgContainer container, Object... parms) throws BizException
	{
		String vhclLic = CommonUtils.checkNull((String) parms[0]);
		if(vhclLic.length() == 0 && minLength != 0) {
			container.addErrorMsg(actionKey + "不能为空");
		}else{
			if (!ValidateUtil.isCarno(vhclLic,0,200))
			{
				container.addErrorMsg(actionKey + "格式错误");
			}
		}
	}

}
