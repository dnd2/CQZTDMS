package com.infodms.dms.actions.help.questionSolve;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.help.QuestionSearchDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class QuestionSearch {
	private Logger logger = Logger.getLogger(QuestionSolve.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private QuestionSearchDao dao = QuestionSearchDao.getInstance();
	
	private static final String QuestionSearch_URL ="/jsp/help/searchInit.jsp";
	private static final String searchDetail_URL ="/jsp/help/searchDetail.jsp";
	/**************问题查询（车厂）初始化***************/
	public void searchInit(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(QuestionSearch_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"查询提交的且未回答的问题");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	/**************得到已经保存了的数据***************/
	public void getSaveQuestionMessage(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String questionClass__search = CommonUtils.checkNull(request.getParamValue("questionClass__search"));
			String isCommon__search = CommonUtils.checkNull(request.getParamValue("isCommon__search"));
			String satatus_search = CommonUtils.checkNull(request.getParamValue("satatus_search"));
			String questionDescr_search = CommonUtils.checkNull(request.getParamValue("questionDescr_search"));
			
			Map<String, String> map = new  HashMap<String, String>();
			map.put("questionClass__search", questionClass__search);
			map.put("isCommon__search", isCommon__search);
			map.put("satatus_search", satatus_search);
			map.put("questionDescr_search", questionDescr_search);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps=dao.getSaveQuestions(map, 500, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"得到已经保存了的数据");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	/**************明细查询***************/
	
	public void searchDetail(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(QuestionSearch_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"查询提交的且未回答的问题");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	
/**************问题查询（车厂）明细***************/
	
	public void getCommonDetail(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String QID = CommonUtils.checkNull(request.getParamValue("QID"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("QID", QID);
			
			
			List<Map<String, Object>> list = dao.detail(map);
			int len = list.size();
			String qType = null;
			String qDesc = null;
			String qAnswer = null;
			for (int i = 0; i < len; i++) {
				qType =  String.valueOf(list.get(i).get("QUESTION_TYPE"));
				qDesc = (String) list.get(i).get("QUESTION_DESCRIBE");
				qAnswer = (String) list.get(i).get("ANSWER");
			}
			act.setOutData("QID", QID);
			act.setOutData("qType", qType);
			act.setOutData("qDesc", qDesc);
			act.setOutData("qAnswer", qAnswer);
			act.setForword(searchDetail_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"问题查询（车厂）明细");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
}
