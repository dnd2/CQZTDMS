package com.infodms.dms.actions.claim.application;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ActivityEvaluateBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.claim.application.ActivityEvaluateDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.ActionUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class ActivityEvaluate {

	private Logger logger = Logger.getLogger(BalanceDealerQuery.class);
	private final ActivityEvaluateDao dao = ActivityEvaluateDao.getInstance();
	private final String ACTIVITY_EVALUATE = "/jsp/claim/application/activityEvaluate.jsp";// 服务活动评估
	private final String CTIVITY_EVA_QUERY = "/jsp/claim/application/activityEvaluateQuery.jsp";// 服务活动评估查询
	private final String CTIVITY_EVA_DETAIL = "/jsp/claim/application/activityEvaluateDetail.jsp";// 服务活动评估明细查看

	
	/**
	 * 服务活动评估跳转
	 */
	public void actEvaInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(ACTIVITY_EVALUATE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动评估");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 服务活动评估查询数据
	 */
	public void actEvaQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			String subjectId = request.getParamValue("SUBJECT_ID"); // 得到查询页面的服务活动主题ID
			PageResult<ActivityEvaluateBean> ps = dao.actEvaQuery(
					Long.parseLong(subjectId), ActionUtil.getPageSize(request), curPage);
			act.setOutData("ps", ps);
			// 自定义每页大小 by chenyub@yonyou.com
			ActionUtil.setCustomPageSizeFlag(act, true);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动评估");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
