package com.infodms.dms.actions.customerRelationships.performancemanage;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.customerRelationships.knowledgelibrarymanage.KnowledgeLibraryManage2;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.PerformanceManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmComplaintRulePO;
import com.infodms.dms.po.TtCrmReturnRulePO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

public class PerformanceManage {

	
	private static Logger logger = Logger.getLogger(KnowledgeLibraryManage2.class);
	private static final String complaintAssessRuleSet="/jsp/customerRelationships/performancemanage/complaintAssessRuleSet.jsp";
	private static final String reviewAssessRuleSet="/jsp/customerRelationships/performancemanage/reviewAssessRuleSet.jsp";
	
	/**
	 * 投诉考核规则-查询
	 * 2013-5-8
	 */
	public void complaintAssessRuleSet()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		PerformanceManageDao dao = PerformanceManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			int pageSize = 10 ;
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			List<Map<String,Object>> level = dao.getAssessLevel();//考核等级
			List<Map<String,Object>> item = dao.getComplaintAssessItem();//考核项目
			List<Map<String,Object>> ps = dao.complaintAssessRuleInfo( logonUser) ;
			act.setOutData("level", level);
			act.setOutData("item", item);
			act.setOutData("ps",ps);
			act.setForword(complaintAssessRuleSet);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉绩效考核设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 投诉考核规则保存
	 */
	public void saveComplaintRule(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		PerformanceManageDao dao = PerformanceManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String []levels = request.getParamValues("assessLevel");
			String []items = request.getParamValues("assessItem");
			String []mintarget = request.getParamValues("mintarget");
			String []maxtarget = request.getParamValues("maxtarget");
			String []weights = request.getParamValues("weight");
			TtCrmComplaintRulePO po = new TtCrmComplaintRulePO();
			dao.delete(po);
			dao.saveComplaintRule(levels,items,mintarget,maxtarget,weights,logonUser);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉绩效考核设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 回访考核规则查询
	 */
	public void reviewAssessRuleSet()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		PerformanceManageDao dao = PerformanceManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			int pageSize = 10 ;
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			List<Map<String,Object>> ps = dao.reviewAssessRuleInfo( logonUser) ;
			act.setOutData("ps",ps);
			act.setForword(reviewAssessRuleSet);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉绩效考核设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 回访考核规则-保存
	 */
	public void saveReviewRule(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		PerformanceManageDao dao = PerformanceManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			//String []levels = request.getParamValues("assessLevel");
			String []items = request.getParamValues("assessItem");
			String []targets = request.getParamValues("target");
			String []weights = request.getParamValues("weight");
			// String [] ids = request.getParamValues("ru_id");
			TtCrmReturnRulePO po = new TtCrmReturnRulePO();
			dao.delete(po);
			dao.saveReviewRule(null,items,targets,weights,logonUser,null);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉绩效考核设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
