/**********************************************************************
* <pre>
* FILE : ServiceActivityVechileSituation.java
* CLASS : ServiceActivityVechileSituation
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理---服务活动及车辆情况查询
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
 * $Id: ServiceActivityVechileSituation.java,v 1.2 2010/10/14 09:56:00 zuoxj Exp $
 */
package com.infodms.dms.actions.claim.serviceActivity;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityVehicleSituationDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  服务活动管理---服务活动及车辆情况查询
 * @author        :  PGM
 * CreateDate     :  2010-06-13
 * @version       :  0.1
 */
public class ServiceActivityVechileSituation {
	private Logger logger = Logger.getLogger(ServiceActivityVechileSituation.class);
	private ServiceActivityVehicleSituationDao dao = ServiceActivityVehicleSituationDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ServiceActivityInitUrl = "/jsp/claim/serviceActivity/serviceActivityVechileSituation.jsp";//服务活动及车辆情况查询页面

	/**
	 * Function       :  服务活动管理---服务活动及车辆情况查询页面初始化
	 * @param         :  
	 * @return        :  serviceActivityVechileSituationInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-13
	 */
	public void serviceActivityVechileSituationInit(){
		try {
			act.setForword(ServiceActivityInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动及车辆情况查询页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询服务活动管理---服务活动及车辆情况查询中符合条件的信息
	 * @param         :  request-活动编号、活动状态、活动开始日期、活动结束日期
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-13
	 */
	public void serviceActivityVechileSituationQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String activityCode = request.getParamValue("activityCode");    //活动编号
			String activityName = request.getParamValue("activityName");    //活动名称
			String status = request.getParamValue("status");                //活动状态
			String startdate = request.getParamValue("startdate");         //活动开始日期
			String enddate = request.getParamValue("enddate");            //活动结束日期
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);      //公司ID
			TtAsActivityBean ActivityBean = new TtAsActivityBean();
			ActivityBean.setActivityCode(activityCode);
			ActivityBean.setActivityName(activityName);
			ActivityBean.setStatus(status);
			ActivityBean.setStartdate(startdate);
			ActivityBean.setEnddate(enddate);
			ActivityBean.setCompanyId(String.valueOf(companyId));
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getServiceActivityVechileSituationQuery(ActivityBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(ServiceActivityInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---经销商管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}