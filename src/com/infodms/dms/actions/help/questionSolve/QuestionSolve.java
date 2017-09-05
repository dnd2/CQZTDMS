package com.infodms.dms.actions.help.questionSolve;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.help.QuestionSolveDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtQuestionPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class QuestionSolve {
	private Logger logger = Logger.getLogger(QuestionSolve.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private QuestionSolveDao dao = QuestionSolveDao.getInstance();
	
	private static final String QuestionSolve_URL ="/jsp/help/questionSolve.jsp";
	private static final String QuestionSolveAnswer_URL ="/jsp/help/questionSolveAnswer.jsp";
	
	private String qid = "";
	
	/**********初始化问题解决页面**********/
	public void questionSolveInit(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(QuestionSolve_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"问题解答");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	/********查询提交的，未回答的问题**********/
	public void getDealerQuestion(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String questionClass_solve = CommonUtils.checkNull(request.getParamValue("questionClass_solve"));
			String questionDescr = CommonUtils.checkNull(request.getParamValue("questionDescr"));
			//String status_solve = CommonUtils.checkNull(request.getParamValue("status_solve"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("questionClass_solve", questionClass_solve);
			map.put("questionDescr", questionDescr);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			
			
			PageResult<Map<String, Object>> ps = null;
			ps = dao.getQuestions(map, 500, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"查询提交的且未回答的问题");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	/**************明细查询跳转****************/
	
	public void toDtail(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String QId=CommonUtils.checkNull(request.getParamValue("qIds"));
			if(QId == null || "".equals(QId)){
				if(qid == null || "".equals(qid)){
					logger.error("问题ID未传！");
					return;
				}
				QId = qid;
			}
			Map<String, String> mapdetail = new HashMap<String, String>();
			mapdetail.put("QId", QId);
			List<Map<String, Object>> list_detail = dao.detail(mapdetail);
			int len = list_detail.size();
			String desc = null;
			String classFy = null;
			String id = null;
			String answer = null;
			String common = null;
			String dealerType = null;
			for(int i=0;i<len;i++){
				 desc = (String) list_detail.get(i).get("QUESTION_DESCRIBE");
				 classFy =  (String) list_detail.get(i).get("QUESTION_TYPE");
				 id =  String.valueOf(list_detail.get(i).get("QUESTION_ID")) ;
				 if(list_detail.get(i).get("ANSWER") != null){
					 answer = (String) list_detail.get(i).get("ANSWER"); 
				 }
				 if(list_detail.get(i).get("IS_COMMON") != null){
					 String t = list_detail.get(i).get("IS_COMMON").toString();
					if(t.equals("10041001")){
						common = "是";
					} else{
						common = "否";
					}
				 }
				 if(list_detail.get(i).get("DEALER_TYPE") != null){
					String t = list_detail.get(i).get("DEALER_TYPE").toString();
					if(t.equals("10771001")){
						dealerType = "经销商整车销售";
					} else if(t.equals("10771002")){
						dealerType = "经销商售后";
					}else{
						dealerType = "所有";
					}
				 }else{
					 dealerType = "所有";
				 }
			}
			if(len > 0){//查询附件信息
				FsFileuploadPO detail = new FsFileuploadPO();
				detail.setYwzj(Long.valueOf(QId));
				List<FsFileuploadPO> lists = dao.select(detail);
				act.setOutData("lists", lists);
			}
			System.out.println("?????????????????????????????????"+id);
			act.setOutData("id", id);
			act.setOutData("QId", QId);
			act.setOutData("desc", desc);
			act.setOutData("answer", answer);
			act.setOutData("common", common);
			act.setOutData("dealerType", dealerType);
			act.setOutData("classFy", classFy);
			act.setForword(QuestionSolveAnswer_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"明细查询跳转");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	/**********回答提交的未回答的问题*************/
	public void answerQuestion(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			long userId = logonUser.getUserId();//获取用户ID
			String answerContent = CommonUtils.checkNull(request.getParamValue("answerContent"));
			Integer common = Integer.parseInt(request.getParamValue("common"));
			Integer dealerType = Integer.parseInt(request.getParamValue("dealerType"));
			String QId=CommonUtils.checkNull(request.getParamValue("qIds"));
			if(QId == null || "".equals(QId)){
				logger.error("回答提交问题时，未传问题ID！");
				return;
			}
			qid = QId;
			Map<String, String> mapdetail = new HashMap<String, String>();
			mapdetail.put("QId", QId);
			List<Map<String, Object>> list_detail = dao.detail(mapdetail);
			int len = list_detail.size();
			String id = null;
			for(int i=0;i<len;i++){
				 id =  String.valueOf(list_detail.get(i).get("QUESTION_ID")) ;
			}
			System.out.println("++++++++++++++"+id);
			TtQuestionPO tqKey = new TtQuestionPO();
			tqKey.setQuestionId(Long.parseLong(id));
			TtQuestionPO tqValue = new TtQuestionPO();
			tqValue.setAnswer(answerContent);
			tqValue.setIsCommon(common);
			tqValue.setDealerType(dealerType);
			tqValue.setQuestionStatus(Constant.QUETION_STATUS_3);
			tqValue.setAnswerBy(userId);
			dao.update(tqKey, tqValue);
			
			String []fjids = request.getParamValues("fjid");					    			//附件ID
			//附件添加
			FileUploadManager.fileUploadByBusiness(id, fjids, logonUser);
			
			
			toDtail();
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"回答问题");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	
	
	/**********取消提交的问题*************/
	public void cancelQuestion(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			long userId = logonUser.getUserId();//获取用户ID
			String answerContent = CommonUtils.checkNull(request.getParamValue("answerContent"));
			String QId=CommonUtils.checkNull(request.getParamValue("qIds"));
			
			TtQuestionPO tqKey = new TtQuestionPO();
			tqKey.setQuestionId(Long.parseLong(String.valueOf(QId)));
			TtQuestionPO tqValue = new TtQuestionPO();
			tqValue.setAnswer(answerContent);
			tqValue.setAnswerBy(userId);
			tqValue.setQuestionStatus(Constant.QUETION_STATUS_4);
			dao.update(tqKey, tqValue);
			
			String []fjids = request.getParamValues("fjid");					    			//附件ID
			//附件添加
			FileUploadManager.fileUploadByBusiness(QId, fjids, logonUser);
			
			Map<String, String> mapdetail = new HashMap<String, String>();
			mapdetail.put("QId", QId);
			List<Map<String, Object>> list_detail = dao.detail(mapdetail);
			int len = list_detail.size();
			String desc = null;
			String classFy = null;
			for(int i=0;i<len;i++){
				 desc = (String) list_detail.get(i).get("QUESTION_DESCRIBE");
				 classFy =  (String) list_detail.get(i).get("QUESTION_TYPE");
			}
			act.setOutData("QId", QId);
			act.setOutData("desc", desc);
			act.setOutData("classFy", classFy);
			
			
			//dao.updateQuestion(mapUpdate);
			act.setForword(QuestionSolveAnswer_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"回答问题");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	
	/**************车厂端问题查询*******************/
	public void questionSerch(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车厂端问题查询");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
}
