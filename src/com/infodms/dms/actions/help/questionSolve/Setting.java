package com.infodms.dms.actions.help.questionSolve;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.help.SettingDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtQuestionChangePO;
import com.infodms.dms.po.TtQuestionPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class Setting {
	private Logger logger = Logger.getLogger(QuestionSolve.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private SettingDao dao = SettingDao.getInstance();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	
	private static final String QuestionSetting_URL ="/jsp/help/setInit.jsp";
	private static final String addQuestion_URL ="/jsp/help/addQuestion.jsp";
	private static final String setQuestion_URL ="/jsp/help/setQuestion.jsp";
	/**************问题设置（车厂）初始化***************/
	public void setInit(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(QuestionSetting_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"问题设置（车厂）初始化");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	/**************得到问题设置查询所得到的数据***************/
	public void getSetMessage(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String questionClass_set = CommonUtils.checkNull(request.getParamValue("questionClass_set"));
			String isCommon_set = CommonUtils.checkNull(request.getParamValue("isCommon_set"));
			String questionDescr_set = CommonUtils.checkNull(request.getParamValue("questionDescr_set"));
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("questionClass_set", questionClass_set);
			map.put("isCommon_set", isCommon_set);
			map.put("questionDescr_set", questionDescr_set);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			
			PageResult<Map<String, Object>> ps=dao.getSettingMessage(map, 500, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"得到问题设置查询所得到的数据");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	/*****************常规问题新增初始化*********************/
	public void addQuestionInit(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(addQuestion_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规问题新增初始化");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	/*****************常规问题新增*********************/
	public void addQuestion(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String qTypt_common = CommonUtils.checkNull(request.getParamValue("qTypt_common"));
			String qDesc_common = CommonUtils.checkNull(request.getParamValue("qDesc_common"));
			String qAnswer_common = CommonUtils.checkNull(request.getParamValue("qAnswer_common"));
			
			long userId = logonUser.getUserId();//获取用户ID
			String newsCode = SequenceManager.getSequence2("");//生成questionId
			String questionNo = "QU" + newsCode ;
			String QID = CommonUtils.checkNull(request.getParamValue("QID"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("QID", QID);
			
			
			TtQuestionPO tqpValue = new TtQuestionPO();
			tqpValue.setQuestionId(Long.parseLong(newsCode));
			tqpValue.setCreateBy(userId);
			tqpValue.setCreateDate(new Date());
			tqpValue.setQuestionNo(questionNo);
			tqpValue.setQuestionDescribe(qDesc_common);
			tqpValue.setQuestionType(Integer.parseInt(qTypt_common));
			tqpValue.setIsCommon(Constant.IF_TYPE_YES);
			tqpValue.setQuestionStatus(Constant.QUETION_STATUS_3);
			tqpValue.setAnswer(qAnswer_common);
			tqpValue.setAnswerBy(userId);
			
			SettingDao.inserttqpValue(tqpValue);
			
			String []fjids = request.getParamValues("fjid");					    			//附件ID
			
			//附件添加
			FileUploadManager.fileUploadByBusiness(newsCode, fjids, logonUser);
			addQuestionInit();
			//act.setForword(addQuestion_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"问题设置（车厂）初始化");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	
	
/*****************常规问题设置页面跳转*********************/
	
	
	public void setQuestionInit(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String QID = CommonUtils.checkNull(request.getParamValue("QID"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("QID", QID);
			List<Map<String, Object>> list = dao.getCommonSetMessages(map);
			int len = list.size();
			String desc = null;
			String classFy = null;
			String id = null;
			for (int i = 0; i < len; i++) {
				desc = (String) list.get(i).get("QUESTION_DESCRIBE");
				classFy = (String) list.get(i).get("QUESTION_TYPE");
				//System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"+classFy);
				id = String.valueOf(list.get(i).get("QUESTION_ID"));
			}
			
			//System.out.println("+++++++++++++++++++++"+classFy);
			act.setOutData("id", id);
			act.setOutData("desc", desc);
			act.setOutData("classFy", classFy);
			act.setForword(setQuestion_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规问题设置初始化");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	/*****************常规问题设置*********************/
	
	
	public void setQuestion(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//得到要修改的数据
			String questionStatus__sq =CommonUtils.checkNull(request.getParamValue("questionStatus__sq"));
			String questionClass__sq =CommonUtils.checkNull(request.getParamValue("questionClass__sq"));
			String isCommon__sq =CommonUtils.checkNull(request.getParamValue("isCommon__sq"));
			String answerContent_sq =CommonUtils.checkNull(request.getParamValue("answerContent_sq"));
			String questionId_sq =CommonUtils.checkNull(request.getParamValue("questionId_sq"));
			Integer dealerType = Integer.parseInt(request.getParamValue("dealerType"));
			System.out.println("HHHHHHHHHHHHHHHHHHHHHHHH"+questionId_sq);
			long userId = logonUser.getUserId();//获取用户ID
			
			
			String QID = CommonUtils.checkNull(request.getParamValue("QID"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("QID", QID);
			List<Map<String, Object>> list = dao.getCommonSetMessages(map);
			int len = list.size();
			String id = null;
			for (int i = 0; i < len; i++) {
				id = String.valueOf(list.get(i).get("QUESTION_ID"));
			}
			act.setOutData("id", id);
			
			TtQuestionPO tqpKey = new TtQuestionPO();
			tqpKey.setQuestionId(Long.parseLong(id));
			TtQuestionPO tqpValue = new TtQuestionPO();
			tqpValue.setUpdateBy(userId);
			tqpValue.setUpdateDate(new Date());
			tqpValue.setQuestionType(Integer.parseInt(questionClass__sq));
			tqpValue.setQuestionStatus(Integer.parseInt(questionStatus__sq));
			tqpValue.setIsCommon(Integer.parseInt(isCommon__sq));
			tqpValue.setAnswer(answerContent_sq);
			tqpValue.setAnswerBy(userId);
			tqpValue.setDealerType(dealerType);
			SettingDao.updateValue(tqpKey, tqpValue);
			
			
			setQuestionInit();
			//act.setForword(setQuestion_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"问题设置（车厂）初始化");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
}
