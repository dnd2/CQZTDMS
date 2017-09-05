package com.infodms.dms.actions.indicator;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.indicator.AssessmentIndicatorsDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;




/**
 * 考核指标查询 Action
 * @author 韩晓宇
 * @date 2012-02-28
 * */
public class AssessmentIndicatorsAction {

	public Logger logger = Logger.getLogger(AssessmentIndicatorsAction.class);
	private ActionContext act = ActionContext.getContext();
	private AssessmentIndicatorsDAO dao = AssessmentIndicatorsDAO.getInstance();
	private RequestWrapper request = act.getRequest();
	
	/** 考核指标查询初始化页面 */
	private static final String ASSESSMENT_INDICATORS_INIT_URL = "/jsp/indicator/assessmentIndicatorsInit.jsp";
	
	/** 考核指标查询显示页面 */
	private static final String ASSESSMENT_INDICATORS_URL = "/jsp/indicator/assessmentIndicators.jsp";
	
	/**
	 * 考核指标查询初始化
	 * */
	public void assessmentIndicatorsInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.setForword(ASSESSMENT_INDICATORS_INIT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "考核指标查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 考核指标条件查询
	 * */
	public void indicatorQuery() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("startDate", startDate) ;
			map.put("endDate", endDate) ;
			map.put("dealerId", dealerId) ;
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1 ;
			PageResult<Map<String, Object>> ps = dao.indicatorQuery(map, Constant.PAGE_SIZE, curPage) ;
			act.setOutData("ps", ps) ;			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "考核指标条件查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
