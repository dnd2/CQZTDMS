package com.infodms.dms.actions.util;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderreport.UrgentOrderReport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

/**
 * 公用类,验证用户操作是否超时操作
 * @author 韩晓宇
 * 2012-04-28
 * */
public class UserValidator {
	
	private Logger logger = Logger.getLogger(UserValidator.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	/**
	 * 验证是否是当前用户操作,避免同台机器登陆不同用户时出现后权限错乱问题
	 * */
	public void validate() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		boolean result = false;
		try {
			Long userId = (request.getParamValue("userId") != null ? Long.parseLong(request.getParamValue("userId")) : null);
			String sessionId = request.getParamValue("sessionId");
			String value = request.getParamValue("value");
			if(userId != null && sessionId != null && act.getSession().getId().equals(sessionId) && userId.longValue() != logonUser.getUserId().longValue()) {
				result = true;
			}
			act.setOutData("value", value);
			act.setOutData("result", result);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "公用用户超时验证!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	
}
