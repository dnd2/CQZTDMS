package com.infodms.dms.actions.help.questionSolve;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;



import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.help.CommonQuestionDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class CommonQuestion {
	private Logger logger = Logger.getLogger(QuestionSolve.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private CommonQuestionDao dao = CommonQuestionDao.getInstance();
	private static final String CommonQuestion_URl="/jsp/help/commonInit.jsp";
	private static final String commonDetail_URl="/jsp/help/commonDetail.jsp";
	
	
	/**************常规问题初始化***************/
	public void commonInit(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(CommonQuestion_URl);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规问题初始化");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	/**************得到常规问题数据***************/
	public void getCommonData(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String questionClass_common = CommonUtils.checkNull(request.getParamValue("questionClass_common"));
			String questionDesc_common = CommonUtils.checkNull(request.getParamValue("questionDesc_common"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("questionClass_common", questionClass_common);
			map.put("questionDesc_common", questionDesc_common);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			
			
			PageResult<Map<String, Object>> ps = null;
			ps = dao.getCommonQuestions(map, 500, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"得到常规问题数据");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	/**************得到常规问题明细数据数据***************/
	
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
				qType = String.valueOf(list.get(i).get("QUESTION_TYPE"));
				qDesc = (String) list.get(i).get("QUESTION_DESCRIBE");
				qAnswer = (String) list.get(i).get("ANSWER");
			}
			act.setOutData("QID", QID);
			act.setOutData("qType", qType);
			act.setOutData("qDesc", qDesc);
			act.setOutData("qAnswer", qAnswer);
			
			
			act.setForword(commonDetail_URl);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"得到常规问题明细数据数据");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
}
