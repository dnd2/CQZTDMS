package com.infodms.dms.actions.help.questionSolve;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.help.QuestionReportedDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtQuestionPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class QuestionReported {
	private Logger logger = Logger.getLogger(QuestionReported.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private QuestionReportedDao dao = QuestionReportedDao.getInstance();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	
	private static final String QuestionReported_URL ="/jsp/help/questionReported.jsp";
	private static final String QuestionAdd_URL ="/jsp/help/questionAdd.jsp";
	private static final String QuestionReportedDetail_URL ="/jsp/help/questionReportedDetail.jsp";
	private static final String questionSetting_URL ="/jsp/help/questionSetting.jsp ";
	
	private static final String saveUpdateDetail_URL ="/jsp/help/saveUpdateDetail.jsp";
	private static final String saveUpdateInit_URL ="/jsp/help/saveUpdate.jsp";
	private static final String reportedSearchInit_URL ="/jsp/help/reportedSearch.jsp";
	private static final String reportedSearchDetail_URL ="/jsp/help/reportedSearchDetail.jsp";
	
	/********初始化页面*********/
	public void questionReportedInit(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(QuestionReported_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"问题解答");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	
	/********已保存问题修改初始化*********/
	public void saveUpdateInit(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(saveUpdateInit_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"已保存问题修改初始化");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	/********从数据库中得到已保存问题数据*********/
	public void getSaveUpdate(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String questionClass_su = CommonUtils.checkNull(request.getParamValue("questionClass_su"));
			String questionDesc_su = CommonUtils.checkNull(request.getParamValue("questionDesc_su"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("questionClass_su", questionClass_su);
			map.put("questionDesc_su", questionDesc_su);
			
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			
			
			PageResult<Map<String, Object>> ps = null;
			ps = dao.getSaveUpdateData(map, 500, curPage);
			act.setOutData("ps", ps);
			//act.setForword(saveUpdateInit_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"从数据库中得到已保存问题数据");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	/********已保存问题数据修改*********/
	public void saveUpdate(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String QID = CommonUtils.checkNull(request.getParamValue("QID"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("QID", QID);
			List<Map<String, Object>> list = dao.getReportedDetail(map);
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
			act.setForword(saveUpdateDetail_URL);
			} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"已保存问题数据修改");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	
	/********已保存问题数据删除*********/
	public void deleteSave(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String QID = CommonUtils.checkNull(request.getParamValue("QID"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("QID", QID);
			//TtQuestionPO po = new TtQuestionPO();
			List<TtQuestionPO> po = new ArrayList<TtQuestionPO>();
			QuestionReportedDao.deleteTq(map, po);
			//dao.deleteReported(po);
			} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"已保存问题数据删除");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	
	/********提交问题查询初始化*********/
	public void reportedSearchInit(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(reportedSearchInit_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"提交问题查询初始化");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	
	/********得到提交问题数据*********/
	public void getReportedSearchData(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String questionClass_rs = CommonUtils.checkNull(request.getParamValue("questionClass_rs"));
			String questionDesc_rs = CommonUtils.checkNull(request.getParamValue("questionDesc_rs"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("questionClass_rs", questionClass_rs);
			map.put("questionDesc_rs", questionDesc_rs);
			
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			
			
			PageResult<Map<String, Object>> ps = null;
			ps = dao.getReportedSearch(map, 500, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"得到提交问题数据");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	public void ReportedSearch(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String QID = CommonUtils.checkNull(request.getParamValue("QID"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("QID", QID);
			List<Map<String, Object>> list = dao.getReportedDetail(map);
			int len = list.size();
			String qType = null;
			String qDesc = null;
			String qAnswer = null;
			for (int i = 0; i < len; i++) {
				//qType = String.valueOf(list.get(i).get("QUESTION_TYPE"));
				qType = (String) list.get(i).get("QUESTION_TYPE");
				qDesc = (String) list.get(i).get("QUESTION_DESCRIBE");
				qAnswer = (String) list.get(i).get("ANSWER");
			}
			act.setOutData("QID", QID);
			act.setOutData("qType", qType);
			act.setOutData("qDesc", qDesc);
			act.setOutData("qAnswer", qAnswer);
			act.setForword(reportedSearchDetail_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"得到提交问题数据");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	
	
	/****查询页面*******/
	public void getDetailMessageOfQuestion(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String questionClass = CommonUtils.checkNull(request.getParamValue("questionClass"));
			String questionDesc = CommonUtils.checkNull(request.getParamValue("questionDesc"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("questionClass", questionClass);
			map.put("questionDesc", questionDesc);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			
			
			PageResult<Map<String, Object>> ps = null;
			ps = dao.getDetaliQuestionMessage(map, 500, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"问题解答");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	
	/*********问题新增页面跳转*********/
	public void addQuestionPage(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			act.setForword(QuestionAdd_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"问题新增页面");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	/********问题提报的提交**********/
	
	public void addQuestion(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String addQuestionClass = CommonUtils.checkNull(request.getParamValue("addQuestionClass"));
			String addQuestionDesc = CommonUtils.checkNull(request.getParamValue("addQuestionDesc"));
			Integer isCommon = 10041001;
			if(request.getParamValue("isCommon_set") != null && !"".equals(request.getParamValue("isCommon_set"))){
				isCommon = Integer.parseInt(request.getParamValue("isCommon_set"));
			}
			long userId = logonUser.getUserId();//获取用户ID
			String newsCode = SequenceManager.getSequence2("");//生成questionId
			String questionNo = "QU" + newsCode ;
			
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			
			TtQuestionPO tq = new TtQuestionPO();
			
			tq.setQuestionNo(questionNo);
			tq.setCreateBy(userId);
			tq.setCreateDate(new Date());
			//tq.setIsCommon(Constant.IF_TYPE_NO);
			tq.setQuestionId(Long.parseLong(newsCode));
			tq.setQuestionStatus(Integer.parseInt(status));
			tq.setQuestionType(Integer.parseInt(addQuestionClass));
			tq.setQuestionDescribe(addQuestionDesc);
			tq.setIsCommon(isCommon);
			QuestionReportedDao.insertTq(tq);
			
			String []fjids = request.getParamValues("fjid");					    			//附件ID
			
			//附件添加
			FileUploadManager.fileUploadByBusiness(newsCode, fjids, logonUser);
			
			addQuestionPage() ;
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"问题新增提交");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	
	/****问题提报的提交保存*****/
	
	public void saveQuestion(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//String addQuestionClass = CommonUtils.checkNull(request.getParamValue("addQuestionClass"));
			String addQuestionDesc = CommonUtils.checkNull(request.getParamValue("addQuestionDesc"));
			long userId = logonUser.getUserId();//获取用户ID
			String newsCode = SequenceManager.getSequence2("");//生成questionId
			String questionNo = SequenceManager.getSequence("QUES");
			TtQuestionPO tq = new TtQuestionPO();
			
			tq.setQuestionNo(questionNo);
			tq.setCreateBy(userId);
			tq.setCreateDate(new Date());
			tq.setIsCommon(Constant.IF_TYPE_NO);
			tq.setQuestionId(Long.parseLong(newsCode));
			tq.setQuestionStatus(Constant.QUETION_STATUS_1);
			tq.setQuestionType(Constant.QUETION_TYPE_1);
			tq.setQuestionDescribe(addQuestionDesc);
			QuestionReportedDao.insertTq(tq);
			
			String []fjids = request.getParamValues("fjid");					    			//附件ID
			
			//附件添加
			FileUploadManager.fileUploadByBusiness(newsCode, fjids, logonUser);
			
			act.setForword(QuestionAdd_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"问题新增保存");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
	/*************明细查询**************/
	
	public void getDetailMessages(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String QID = CommonUtils.checkNull(request.getParamValue("QID"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("QID", QID);
			List<Map<String, Object>> list = dao.getReportedDetail(map);
			int len = list.size();
			String qType =null;
			String qDesc = null;
			String qAnswer = null;
			for (int i = 0; i < len; i++) {
				qType = String.valueOf(list.get(i).get("QUESTION_TYPE")) ;
				qDesc = (String) list.get(i).get("QUESTION_DESCRIBE");
				qAnswer = (String) list.get(i).get("ANSWER");
			}
			if(len > 0){//查询附件信息
				FsFileuploadPO detail = new FsFileuploadPO();
				detail.setYwzj(Long.valueOf(QID));
				List<FsFileuploadPO> lists = dao.select(detail);
				act.setOutData("lists", lists);
			}
			
			act.setOutData("QID", QID);
			act.setOutData("qType", qType);
			act.setOutData("qDesc", qDesc);
			act.setOutData("qAnswer", qAnswer);
			act.setForword(QuestionReportedDetail_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"明细查询");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}

	/***********经销商端问题设置***************/
	public void questionSetting(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(questionSetting_URL);
		} catch (Exception e) {
			BizException e1=new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商端问题设置");
			logger.error(logonUser, e);
			act.setException(e1);
		}
	}
}
