package com.infodms.dms.actions.sales.ordermanage.orderaudit;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.util.LockControl;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

public class UnLockBill {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();

	public void unLockOper() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String billId=request.getParamValue("billId");
				String sessionId=request.getParamValue("sessionId");
				String userId=request.getParamValue("userId");
				LockControl.destroyLock(sessionId, billId,userId);
				act.setOutData("flag", "1");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "检测资源是否占用查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
