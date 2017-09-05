/**********************************************************************
* <pre>
* FILE : ServiceActivityManageSummarySearch.java
* CLASS : ServiceActivityManageSummarySearch
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理---服务活动评估查询
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-13| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageSummarySearch.java,v 1.1 2010/08/16 01:44:11 yuch Exp $
 */
package com.infodms.dms.actions.claim.serviceActivity;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageSummaryDao;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageSummarySearchDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsActivityEvaluatePO;
import com.infodms.dms.po.TtAsActivitySubjectPO;
import com.infodms.dms.po.TtAsSubjietEvaluatePO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.DBService;
/**
 * Function       :  服务活动管理---服务活动评估查询
 * @author        :  PGM
 * CreateDate     :  2010-06-13
 * @version       :  0.1
 */
public class ServiceActivityManageSummarySearch {
	private Logger logger = Logger.getLogger(ServiceActivityManageSummarySearch.class);
	private ServiceActivityManageSummarySearchDao dao = ServiceActivityManageSummarySearchDao.getInstance();
	private ServiceActivityManageSummaryDao dao1 = ServiceActivityManageSummaryDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ServiceActivityManageSummarySearchInitUrl = "/jsp/claim/serviceActivity/serviceActivityManageSummarySearch.jsp";//查询页面
	private final String ServiceActivityOptionUrl = "/jsp/claim/serviceActivity/serviceActivityManageSummarySearchInfo.jsp";//编辑页面
	private final String ServiceActivityManageSummaryEvaluateInitUrl = "/jsp/claim/serviceActivity/serviceActivityManageSummaryEvaluate.jsp";
	private final String ServiceActivityManageSummaryEvaluateZRInitUrl = "/jsp/claim/serviceActivity/serviceActivityManageSummaryEvaluateZR.jsp";
	private final String ServiceActivityManageSummaryEvaluateInitUrlChaxun = "/jsp/claim/serviceActivity/serviceActivityManageSummaryEvaluateChaxun.jsp";
	private final String ServiceActivityManageSummaryEvaluatePrint = "/jsp/claim/serviceActivity/serviceActivityManageEvaluatePrint.jsp";
	private final String ServiceActivityManageSummaryEvaluate = "/jsp/claim/serviceActivity/serviceActivityManageEvaluate.jsp";
	private final String ServiceActivityManageSummaryEvaluateZR = "/jsp/claim/serviceActivity/serviceActivityManageEvaluateZR.jsp";
	private final String ServiceActivityManageSummaryEvaluateChaxun =  "/jsp/claim/serviceActivity/serviceActivityManageEvaluateChaxun.jsp";
	private final String ServiceActivityManageSummaryEvaluateInfor = "/jsp/claim/serviceActivity/serviceActivityManageEvaluateInfor.jsp";
	/**
	 * Function       :  服务活动管理---服务活动评估查询页面初始化
	 * @param         :  
	 * @return        :  serviceActivityManageSummarySearchInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-13
	 */
	public void serviceActivityManageSummarySearchInit(){
		try {
			act.setForword(ServiceActivityManageSummarySearchInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估查询页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void  serviceActivityEvaluate()
	{
		try {
			act.setForword(ServiceActivityManageSummaryEvaluateInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估查询页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void  serviceActivityEvaluateZR()
	{
		try {
			act.setForword(ServiceActivityManageSummaryEvaluateZRInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估查询页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void serviceActivityback(){
		try {
			RequestWrapper request = act.getRequest();
			String subject_no = request.getParamValue("subjectid");    //主题编号
			String subject_start_date = request.getParamValue("dealerId");          //活动开始日期
			StringBuffer sql= new StringBuffer();
			sql.append("update TT_AS_ACTIVITY_EVALUATE t\n" );
			sql.append("   set t.STATUS = null  , t.re_status = null \n" );
			sql.append(" where t.SUBJECT_ID =\n" + subject_no );
			sql.append("   and t.DEALER_ID = \n" + subject_start_date);
			sql.append("");
            dao.update(sql.toString(), null);
			
			
			act.setForword(ServiceActivityManageSummarySearchInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void serviceActivityEvaluateChaxun()
	{
		try {
			act.setForword(ServiceActivityManageSummaryEvaluateInitUrlChaxun);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估查询页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void serviceActivityEvaluateQuery()
	{
		try {
			RequestWrapper request = act.getRequest();
			String subject_id = request.getParamValue("subject_id");  
			 List<Map<String, Object>> list= dao.serviceActivityEvaluateQuery(subject_id);
			act.setOutData("Evaluate", list);
			TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
			subjectPO.setSubjectId(Long.parseLong(subject_id));
			 List<TtAsActivitySubjectPO> sList= dao.select(subjectPO);
			 subjectPO = sList.get(0);
			 int i = Utility.compareDate1(Utility.handleDate(subjectPO.getSubjectEndDate()),Utility.handleDate( new Date()), 0);
			 if(i >= subjectPO.getDays())
			 {
				 act.setOutData("day", "yes");
			 }else
			 {
				 act.setOutData("day", "no");
			 }
			act.setForword(ServiceActivityManageSummaryEvaluate);
			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void serviceActivityEvaluateQueryZR()
	{
		try {
			RequestWrapper request = act.getRequest();
			String subject_id = request.getParamValue("subject_id");  
			 List<Map<String, Object>> list= dao.serviceActivityEvaluateQuery(subject_id);
			act.setOutData("Evaluate", list);
			act.setOutData("subject_id",subject_id );
			TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
			subjectPO.setSubjectId(Long.parseLong(subject_id));
			 List<TtAsActivitySubjectPO> sList= dao.select(subjectPO);
			 subjectPO = sList.get(0);
			 int i = Utility.compareDate1(Utility.handleDate(subjectPO.getSubjectEndDate()),Utility.handleDate( new Date()), 0);
			 if(i >= subjectPO.getDays())
			 {
				 act.setOutData("day", "yes");
			 }else
			 {
				 act.setOutData("day", "no");
			 }
			act.setForword(ServiceActivityManageSummaryEvaluateZR);
			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void serviceActivityEvaluateQueryChaxun()
	{
		try {
			RequestWrapper request = act.getRequest();
			String subject_id = request.getParamValue("subject_id");  
			 List<Map<String, Object>> list= dao.serviceActivityEvaluateQuery(subject_id);
			act.setOutData("Evaluate", list);
			act.setForword(ServiceActivityManageSummaryEvaluateChaxun);
			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void serviceActivityEvaluateQueryPrint()
	{
		try {
			RequestWrapper request = act.getRequest();
			String subject_id = request.getParamValue("subject_id");  
			 List<Map<String, Object>> list= dao.serviceActivityEvaluateQuery(subject_id);
			act.setOutData("Evaluate", list);
			act.setForword(ServiceActivityManageSummaryEvaluatePrint);
			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估打印");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void serviceActivityManageSummaryEvaluateQuery()
	{
		try {
			RequestWrapper request = act.getRequest();
			String subject_no = request.getParamValue("subject_no");     
			String subject_name = request.getParamValue("subject_name");           
			String activityType = request.getParamValue("activityType"); 
			String startdate = request.getParamValue("startdate"); 
			String enddate = request.getParamValue("enddate"); 
			TtAsActivityBean ActivityBean = new TtAsActivityBean();
			ActivityBean.setSubject_no(subject_no);
			ActivityBean.setSubject_name(subject_name);
			ActivityBean.setActivityType(activityType);
			ActivityBean.setSubject_start_date(startdate);
			ActivityBean.setSubject_end_date(enddate);
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getServiceActivityManageSummaryEvaluateQuery(ActivityBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void serviceActivityManageSummaryEvaluateQueryZR()
	{
		try {
			RequestWrapper request = act.getRequest();
			String subject_no = request.getParamValue("subject_no");     
			String subject_name = request.getParamValue("subject_name");           
			String activityType = request.getParamValue("activityType"); 
			String startdate = request.getParamValue("startdate"); 
			String enddate = request.getParamValue("enddate"); 
			TtAsActivityBean ActivityBean = new TtAsActivityBean();
			ActivityBean.setSubject_no(subject_no);
			ActivityBean.setSubject_name(subject_name);
			ActivityBean.setActivityType(activityType);
			ActivityBean.setSubject_start_date(startdate);
			ActivityBean.setSubject_end_date(enddate);
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getServiceActivityManageSummaryEvaluateQueryZR(ActivityBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void serviceActivityManageSummaryEvaluateQueryChaxun()
	{
		try {
			RequestWrapper request = act.getRequest();
			String subject_no = request.getParamValue("subject_no");     
			String subject_name = request.getParamValue("subject_name");           
			String activityType = request.getParamValue("activityType"); 
			String startdate = request.getParamValue("startdate"); 
			String enddate = request.getParamValue("enddate"); 
			TtAsActivityBean ActivityBean = new TtAsActivityBean();
			ActivityBean.setSubject_no(subject_no);
			ActivityBean.setSubject_name(subject_name);
			ActivityBean.setActivityType(activityType);
			ActivityBean.setSubject_start_date(startdate);
			ActivityBean.setSubject_end_date(enddate);
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getServiceActivityManageSummaryEvaluateQueryChaxun(ActivityBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void serviceActivityManageSummaryEvaluateInfor()
	{
		try {
			RequestWrapper request = act.getRequest();
			String DEALER_ID = request.getParamValue("DEALER_ID");     
			String SUBJECT_ID = request.getParamValue("SUBJECT_ID"); 
			 List<Map<String, Object>> list= dao.serviceActivityEvaluateQueryInfor(SUBJECT_ID, DEALER_ID);
			act.setOutData("Evaluate", list);
			act.setForword(ServiceActivityManageSummaryEvaluateInfor);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  根据条件查询服务活动管理---服务活动评估查询中符合条件的信息
	 * @param         :  request-活动编号、经销商代码、活动开始日期、活动结束日期
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-13
	 */
	
	public void serviceActivityManageSummaryEvaluateUpdate()
	{
		try {
			RequestWrapper request = act.getRequest();
			String iconunt = request.getParamValue("iconunt");  
			for(int i = 1;i<Integer.parseInt(iconunt);i++)
			{
				TtAsSubjietEvaluatePO evaluatePO = new TtAsSubjietEvaluatePO();
				String DEALER_ID = request.getParamValue("DEALER_ID"+i);           
				String SUBJECT_ID = request.getParamValue("SUBJECT_ID"+i);
				String EVALUATE_TYPE = request.getParamValue("EVALUATE_TYPE"+i); 
				String EVALUATE_AMOUNT = request.getParamValue("EVALUATE_AMOUNT"+i); 
				evaluatePO.setDealerId(Long.parseLong(DEALER_ID));
				evaluatePO.setSubjectId(Long.parseLong(SUBJECT_ID));
				dao.delete(evaluatePO);
				evaluatePO.setEvaluateRes(Long.parseLong(EVALUATE_TYPE));
				evaluatePO.setBalanceAmount(Double.parseDouble(EVALUATE_AMOUNT));
				dao.insert(evaluatePO);
				act.setOutData("msg","yes");
			}
			
			
			
			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void serviceActivityManageSummaryEvaluateCommit()
	{
		try {
			RequestWrapper request = act.getRequest();
			String iconunt = request.getParamValue("iconunt");  
			for(int i = 1;i<Integer.parseInt(iconunt);i++)
			{
				TtAsSubjietEvaluatePO evaluatePO = new TtAsSubjietEvaluatePO();
				String DEALER_ID = request.getParamValue("DEALER_ID"+i);           
				String SUBJECT_ID = request.getParamValue("SUBJECT_ID"+i);
				String EVALUATE_TYPE = request.getParamValue("EVALUATE_TYPE"+i); 
				String EVALUATE_AMOUNT = request.getParamValue("EVALUATE_AMOUNT"+i); 
				evaluatePO.setDealerId(Long.parseLong(DEALER_ID));
				evaluatePO.setSubjectId(Long.parseLong(SUBJECT_ID));
				dao.delete(evaluatePO);
				evaluatePO.setEvaluateRes(Long.parseLong(EVALUATE_TYPE));
				evaluatePO.setBalanceAmount(Double.parseDouble(EVALUATE_AMOUNT));
				dao.insert(evaluatePO);
				StringBuffer sql= new StringBuffer();
				sql.append("update TT_AS_ACTIVITY_SUBJECT t set t.EVALUATE = 1 where  t.SUBJECT_ID =" +SUBJECT_ID );
				dao.update(sql.toString(), null);
				act.setOutData("msg","yes");
			}
			
			
			
			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估上报成功");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void serviceActivityManageSummarcommit()
	{
		try {
			RequestWrapper request = act.getRequest();
			String type = request.getParamValue("vau");
			String subject_id = request.getParamValue("subject_id");
			StringBuffer sql = new StringBuffer();
			if(type.equals("1"))
			{
				sql.append("update TT_AS_ACTIVITY_SUBJECT t set t.EVALUATE = 2 where  t.SUBJECT_ID =" +subject_id );
			}else
			{
				sql.append("update TT_AS_ACTIVITY_SUBJECT t set t.EVALUATE = 0 where  t.SUBJECT_ID =" +subject_id );
			}
			dao.update(sql.toString(), null);
			act.setOutData("msg","yes");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估上报成功");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void serviceActivityManageSummarySearchQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String subject_no = request.getParamValue("subject_no");     
			String subject_name = request.getParamValue("subject_name");           
			String dealer_id = request.getParamValue("dealer_id");
			
			String dealer_name = request.getParamValue("dealer_name"); 
			String activityType = request.getParamValue("activityType"); 
			String startdate = request.getParamValue("startdate"); 
			String enddate = request.getParamValue("enddate"); 
			TtAsActivityBean ActivityBean = new TtAsActivityBean();
			ActivityBean.setSubject_no(subject_no);
			ActivityBean.setSubject_name(subject_name);
			ActivityBean.setDealerName(dealer_name);
			ActivityBean.setDealerId(dealer_id);
			ActivityBean.setActivityType(activityType);
			ActivityBean.setSubject_start_date(startdate);
			ActivityBean.setSubject_end_date(enddate);
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getServiceActivityManageSummaryDealerSearchQuery(ActivityBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(ServiceActivityManageSummarySearchInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}