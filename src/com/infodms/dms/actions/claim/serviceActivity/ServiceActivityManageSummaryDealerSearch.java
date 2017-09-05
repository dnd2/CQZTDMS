/**********************************************************************
* <pre>
* FILE : ServiceActivityManageSummaryDealerSearch.java
* CLASS : ServiceActivityManageSummaryDealerSearch
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理---服务活动评估经销商查询
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
 * $Id: ServiceActivityManageSummaryDealerSearch.java,v 1.1 2010/08/16 01:44:10 yuch Exp $
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
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageSummaryDealerSearchDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsActivityConductPO;
import com.infodms.dms.po.TtAsActivityEvaluatePO;
import com.infodms.dms.po.TtAsActivityPerPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.DBService;
/**
 * Function       :  服务活动管理---服务活动评估经销商查询
 * @author        :  PGM
 * CreateDate     :  2010-06-13
 * @version       :  0.1
 */
public class ServiceActivityManageSummaryDealerSearch {
	private Logger logger = Logger.getLogger(ServiceActivityManageSummaryDealerSearch.class);
	private ServiceActivityManageSummaryDealerSearchDao dao = ServiceActivityManageSummaryDealerSearchDao.getInstance();
	private ServiceActivityManageSummaryDao dao1 = ServiceActivityManageSummaryDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String serviceActivityManageSummaryDealerSearchInitUrl = "/jsp/claim/serviceActivity/serviceActivityManageSummaryDealerSearch.jsp";//查询页面
	private final String ServiceActivityOptionUrl = "/jsp/claim/serviceActivity/serviceActivityManageSummaryDealerSearchInfo.jsp";//编辑页面
	/**
	 * Function       :  服务活动管理---服务活动评估经销商查询页面初始化
	 * @param         :  
	 * @return        :  serviceActivityManageSummarySearchInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-13
	 */
	public void serviceActivityManageDealerInit(){
		try {
			act.setForword(serviceActivityManageSummaryDealerSearchInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估经销商查询页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询服务活动管理---服务活动评估经销商查询中符合条件的信息
	 * @param         :  request-活动编号、经销商代码、活动开始日期、活动结束日期
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-13
	 */
	public void serviceActivityManageSummaryDealerSearchQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String subject_no = request.getParamValue("subject_no");     
			String subject_name = request.getParamValue("subject_name");           
			String dealerId = logonUser.getDealerId();//经销商ID 
			String activityType = request.getParamValue("activityType"); 
			String startdate = request.getParamValue("startdate"); 
			String enddate = request.getParamValue("enddate"); 
			TtAsActivityBean ActivityBean = new TtAsActivityBean();
			ActivityBean.setDealerId(dealerId);
			ActivityBean.setSubject_no(subject_no);
			ActivityBean.setSubject_name(subject_name);
			ActivityBean.setActivityType(activityType);
			ActivityBean.setSubject_start_date(startdate);
			ActivityBean.setSubject_end_date(enddate);
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getServiceActivityManageSummaryDealerSearchQuery(ActivityBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(serviceActivityManageSummaryDealerSearchInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估经销商查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function         : 服务活动管理---服务活动评估经销商查询[编辑]查询方法：状态为：[已经下发]的活动
	 * @param           : request-活动ID
	 * @return          : 满足条件的服务活动管理信息
	 * @throws          : 
	 * LastUpdate       : 2010-06-13
	 */
	public void	serviceActivitySummaryQuery(){
		try {
		    ServiceActivityManageSummaryDao dao1 = ServiceActivityManageSummaryDao.getInstance();
			RequestWrapper request = act.getRequest();
			String subjectid = request.getParamValue("subject_id");    //活动ID
			String dealerId = request.getParamValue("DEALER_ID");//经销商ID
			act.setOutData("dealerId", dealerId);
			if(dealerId == null )
			{
				dealerId = logonUser.getDealerId();
			}
			if(logonUser.getDealerId() == null)
			{
				act.setOutData("type",1);
			}
			String sql = "select  t.root_org_name  from vw_org_dealer_service t where t.root_dealer_id="+dealerId;
			//ResultSet rs= conn.prepareStatement(sql).executeQuery();
			List<Map<String,Object>> list = dao.pageQuery(sql, null, dao.getFunName());
			if(list!=null&&list.size()>0){
				act.setOutData("org_name", list.get(0).get("ROOT_ORG_NAME"));
			}
//			if(rs.next())
//			{
//				String org_name= rs.getString(1);
//				act.setOutData("org_name", org_name);
//			}
//			conn.prepareStatement(sql).executeQuery();
			TtAsActivityBean ttAsActivityBean=dao1.serviceActivitySummaryQuery(subjectid,dealerId);//调用服务活动评估[编辑]查询方法
			String  Subject_end_date= ttAsActivityBean.getSubject_end_date();
			Date end_date = Utility.getDate(Subject_end_date, 1);
			if(end_date.after(new Date()))
			{
				act.setOutData("typeDate","NO");
			}else
			{
				act.setOutData("typeDate","YES");
			}
		
			List<Map<String,Object>> SList= dao1.SType(subjectid,dealerId);
			List<Map<String,Object>> ZList= dao1.ZType(subjectid,dealerId);
			
			StringBuffer sb = new StringBuffer();
			sb.append("select * from tt_as_activity_per t where t.SUMMARY_ID ="+subjectid+" and t.DEALER_ID = "+dealerId+"   order by t.CODE_TYPE ");
		    List<TtAsActivityPerPO> perList= dao.select(TtAsActivityPerPO.class, sb.toString(),null);
			act.setOutData("perList", perList);
			
			TtAsActivityConductPO conductPO = new TtAsActivityConductPO();
			conductPO.setSummaryId(Long.parseLong(subjectid));
			List<TtAsActivityConductPO> conList= dao.select(conductPO);

			sb = new StringBuffer();
			sb.append("select t.W_name WNAME , t.W_add WADD,to_char(t.publish_date,'yyyy-mm-dd') PUBLISHDATE,t.conduct_cont  CONDUCTCONT,t.MEDIA_NAME  from TT_AS_activity_conduct t where    t.DEALER_ID = "+dealerId+" and  t.SUMMARY_ID ="+subjectid+"");
			act.setOutData("conList", dao.pageQuery(sb.toString(),null, dao.getFunName()));

			
			if(SList.size() > 0)
			{
				List<Map<String,Object>> SAmount= dao1.SAmount(subjectid,dealerId);
				act.setOutData("SAmount", SAmount.get(0));
			}else
			{
				act.setOutData("Stype", 0);
			}
			if(ZList.size() > 0)
			{
				List<Map<String,Object>> ZAmount= dao1.ZAmount(subjectid,dealerId);
				act.setOutData("ZAmount", ZAmount.get(0));
			}else{
				act.setOutData("Ztype", 0);
			}
			
			
			act.setOutData("SList", SList);
			act.setOutData("ZList", ZList);
			FsFileuploadPO po = new FsFileuploadPO();
			if(ttAsActivityBean.getEvaluateid() != null )
			{
				po.setYwzj(Long.parseLong(ttAsActivityBean.getEvaluateid()));
				List<FsFileuploadPO> fsFileuploadPO= dao.select(po);
				act.setOutData("FileuploadPO", fsFileuploadPO);
			}else
			{
				po.setYwzj(Long.parseLong("111111"));
				List<FsFileuploadPO> fsFileuploadPO= dao.select(po);
				act.setOutData("FileuploadPO", fsFileuploadPO );
			}
			
			
			act.setOutData("ttAsActivityBean", ttAsActivityBean);
			act.setForword(ServiceActivityOptionUrl);//跳转到服务活动评估[编辑]页面
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估经销商查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}