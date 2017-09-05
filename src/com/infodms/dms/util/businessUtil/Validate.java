/**********************************************************************
* <pre>
* FILE : AuthApplyValidate.java
* CLASS : AuthApplyValidate
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
* $Id: Validate.java,v 1.1 2010/08/16 01:44:40 yuch Exp $
*/

package com.infodms.dms.util.businessUtil;

import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.ErrorMsgContainer;
import com.infodms.dms.common.MsgCarrier;
import com.infodms.dms.common.MyPattern;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2009-9-15
 * @version    :
 */
public class Validate
{
	public Logger logger = Logger.getLogger(Validate.class);

	/* (non-Javadoc)
	 * @see com.jmc.dms.util.businessUtil.Validate#doValidate(com.infoservice.mvc.context.RequestWrapper)
	 */
	public static void doValidate(ActionContext act, List<MsgCarrier> list) throws BizException
	{
		// 创建错误容器
		ErrorMsgContainer container = ErrorMsgContainer.getInstance();
		for (int i = 0; i < list.size(); i++)
		{
			MsgCarrier carrier = list.get(i);
			String[] params = carrier.getParams();
			MyPattern pattern = carrier.getPattern();
			pattern.compile(container, params);
			if (container.hasErrMsg())
			{
				if (i == (list.size() - 1))
				{
					throw new BizException(act, ErrorCodeConstant.NOT_MATCH, container.getAllErrorMsg());
				}
				if (container.isFull())
				{
					throw new BizException(act, ErrorCodeConstant.NOT_MATCH, container.getAllErrorMsg());
				}
			}
		}
	}
}
