/**********************************************************************
* <pre>
* FILE : ServiceActivityPlanAnalyse.java
* CLASS : ServiceActivityPlanAnalyse
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理---服务活动计划分析
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
 * $Id: ServiceActivityPlanAnalyse.java,v 1.2 2010/11/25 13:33:56 yangheng Exp $
 */
package com.infodms.dms.actions.claim.serviceActivity;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityPlanAnalyseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsActivityPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  服务活动管理---服务活动计划分析
 * @author        :  PGM
 * CreateDate     :  2010-06-13
 * @version       :  0.1
 */
public class ServiceActivityPlanAnalyse {
	private Logger logger = Logger.getLogger(ServiceActivityPlanAnalyse.class);
	private ServiceActivityPlanAnalyseDao dao = ServiceActivityPlanAnalyseDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ServiceActivityInitUrl = "/jsp/claim/serviceActivity/serviceActivityPlanAnalyse.jsp";//查询页面

	/**
	 * Function       :  服务活动管理---服务活动计划分析页面初始化
	 * @param         :  
	 * @return        :  serviceActivityPlanAnalyseInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-13
	 */
	@SuppressWarnings("unchecked")
	public void serviceActivityPlanAnalyseInit(){
		try {
			RequestWrapper request =act.getRequest();
			List<TtAsActivityPO> list=dao.serviceActivityCode();
			request.setAttribute("list", list);
			act.setForword(ServiceActivityInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动及车辆情况查询页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void serviceActivityPlanAnalyseQueryName(){
		try {
			RequestWrapper request =act.getRequest();
			String activityCode = request.getParamValue("activityCode");
			String activity_name = request.getParamValue("activity_name");
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.serviceActivityName(activityCode,activity_name,curPage);			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务计划分析得到活动名称");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询服务活动管理---服务活动计划分析中符合条件的信息
	 * @param         :  request-活动编号、活动经销商编码
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-13
	 */
	public void serviceActivityPlanAnalyseQuery(){
		try {
			RequestWrapper request = act.getRequest();
			StringBuffer sb = new StringBuffer();
			List<Object> params = new LinkedList<Object>();
			String activityId = request.getParamValue("activityId");       //活动编号
			String dealerCode = request.getParamValue("dealerCode");       //活动经销商编码
			String dealerName = request.getParamValue("dealerName");       //活动经销商名称
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);      //公司ID
			TtAsActivityBean ActivityBean = new TtAsActivityBean();
			ActivityBean.setActivityId(activityId);
			ActivityBean.setDealerCode(dealerCode);
			ActivityBean.setDealerName(dealerName);

			//拼sql的查询条件
			if (Utility.testString(dealerCode)) {
				sb.append(Utility.getConSqlByParamForEqual(dealerCode, params, "a", "dealer_code"));
			}
			ActivityBean.setCompanyId(String.valueOf(companyId));
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getServiceActivityPlanAnalyseQuery(ActivityBean,sb.toString(),params,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(ServiceActivityInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---经销商管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}