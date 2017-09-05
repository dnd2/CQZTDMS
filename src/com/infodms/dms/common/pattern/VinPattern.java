/**********************************************************************
* <pre>
* FILE : VinPattern.java
* CLASS : VinPattern
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
* $Id: VinPattern.java,v 1.1 2010/08/16 01:43:19 yuch Exp $
*/

package com.infodms.dms.common.pattern;

import com.infodms.dms.common.ErrorMsgContainer;
import com.infodms.dms.common.MyPattern;
import com.infodms.dms.common.ValidateUtil;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2009-9-15
 * @version    :
 */
public class VinPattern extends MyPattern
{

	/* (non-Javadoc)
	 * @see com.jmc.dms.common.MyPattern#compile(java.lang.String, java.lang.String, com.jmc.dms.common.ErrorMsgContainer)
	 */
	@Override
	public void compile(ErrorMsgContainer container, Object... parms) throws BizException
	{
		String vin = CommonUtils.checkNull((String) parms[0]);
		if(vin.length() == 0 && minLength != 0) {
			container.addErrorMsg(actionKey + "不能为空");
		}else{
			// VIN必须为大写字母,数字组成
			if (!ValidateUtil.isVin(vin))
			{
				container.addErrorMsg(actionKey + "必须为大写字母,数字组成");
			}
		}
		//errMsgContainerFull(container);
	}

}
