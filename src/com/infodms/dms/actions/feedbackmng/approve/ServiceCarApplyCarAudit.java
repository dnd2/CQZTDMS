package com.infodms.dms.actions.feedbackmng.approve;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.common.DealerUtilDAO;
import com.infodms.dms.dao.feedbackMng.ServiceCarApplyAuditDAO;
import com.infodms.dms.dao.feedbackMng.ServiceCarApplyDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtIfServicecarAuditPO;
import com.infodms.dms.po.TtIfServicecarExtPO;
import com.infodms.dms.po.TtIfServicecarPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
* @ClassName: ServiceCarApplyCarAudit 
* @Description: TODO(服务车申请表轿车事业部审批) 
* @author wangchao 
* @date May 24, 2010 4:30:39 PM 
*
 */
public class ServiceCarApplyCarAudit {
	public Logger logger = Logger.getLogger(ServiceCarApplyCarAudit.class);
	private ServiceCarApplyAuditDAO dao = ServiceCarApplyAuditDAO.getInstance();
	private ServiceCarApplyDAO sdao = ServiceCarApplyDAO.getInstance();
	private DealerUtilDAO cdao = DealerUtilDAO.getInstance();
	private final String serviceCarApplyTeamAuditURL = "/jsp/feedbackMng/approve/serviceCarApplyCarAudit.jsp";// 主页面
	private final String serviceCarApplyAuditURL = "/jsp/feedbackMng/approve/serviceCarApplyAudit.jsp";// 审批页面
	/**
	 * 
	* @Title: serviceCarApplyCarAuditForward 
	* @Description: TODO(服务车申请表轿车事业部首页跳转) 
	* @param      
	* @return void    返回类型 
	* @throws
	 */
	public void serviceCarApplyCarAuditForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(serviceCarApplyTeamAuditURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "服务车申请表轿车事业部审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	* @Title: servicecarapplyAuditPre 
	* @Description: TODO(审核跳转页面) 
	* @param     
	* @return void    返回类型 
	* @throws
	 */
	public void servicecarapplyAuditPre() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String type = request.getParamValue("TYPE");
			String orderId = request.getParamValue("ORDER_ID");
			if ("TEAM".equals(type)) {
				request.setAttribute("TITLE", "服务车申请表大区审批");
			} else if ("SERVICE".equals(type)) {
				request.setAttribute("TITLE", "服务车申请表售后服务部审批");
			} else if ("CAR".equals(type)) {
				request.setAttribute("TITLE", "服务车申请表轿车事业部审批");
			}
			TtIfServicecarExtPO tisp = sdao.queryDetailByOrderId(orderId);
			List<TtIfServicecarExtPO> ls = sdao.queryAuditDetailByOrderId(orderId);
			request.setAttribute("servicecarBean", tisp);
			request.setAttribute("auditDetails", ls);
			act.setForword(serviceCarApplyAuditURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "服务车申请表轿车事业部首页");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	* @Title: applyQuery 
	* @Description: TODO(查询) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void applyQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		String dealerId = logonUser.getDealerId();
		LinkedList params = new LinkedList();
		String dealerIds = cdao.getDealerIdsToString(logonUser.getOrgId());
	
		//System.out.println(logonUser.getOrgId());
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			StringBuffer con = new StringBuffer();
			String right = request.getParamValue("RIGHT");
			String orderId = request.getParamValue("ORDER_ID");
			String strDate = request.getParamValue("CON_APPLY_DATE_START");
			String endDate = request.getParamValue("CON_APPLY_DATE_END");
			String modelId = request.getParamValue("MODEL_ID");
			String dealerName = request.getParamValue("DEALER_NAME");
			String dealerCode = request.getParamValue("dealerCode");
			//dealerId = request.getParamValue("DEALER_ID")==null?null:Long.parseLong(request.getParamValue("DEALER_ID"));
			//当开始时间和结束时间相同时
			if(null!=strDate&&!"".equals(strDate)&&null!=endDate&&!"".equals(endDate)){
					strDate = strDate+" 00:00:00";
					endDate = endDate+" 23:59:59";
			}
			//工单号like
			if (orderId!=null&&!"".equals(orderId)) {
				con.append(" and t.ORDER_ID like'%"+orderId+"%' "); //
			}
			//介于开始时间
			if (strDate!=null&&!"".equals(strDate)) {
				con.append(" and t.app_date >= to_date('"+strDate+"', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			//结束时间
			if (endDate!=null&&!"".equals(endDate)) {
				con.append(" and t.app_date <= to_date('"+endDate+"', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			//经销商代码
			if (dealerId!=null&&!"".equals(dealerId)) {
				con.append(" and t.DEALER_ID='"+dealerId+"' ");
			}
			if (null!=companyId&&!"".equals(companyId)) {
				con.append(" and t.company_id="+companyId);
			}
			//经销商代码
			if (Utility.testString(dealerCode)) {
				con.append(Utility.getConSqlByParamForEqual(dealerCode, params, "d", "dealer_code")); 
			}
			//经销商名称
			if (!"".equals(dealerName)&&null!=dealerName){
				con.append(" and d.DEALER_NAME LIKE '"+dealerName+"%' ");
			}
			//车型
			if (modelId!=null&&!"".equals(modelId)) {
				con.append(" and t.GROUP_ID='"+modelId+"' ");
			}
			//经销商代码不为空
			/*if (!"".equals(dealerIds)&&null!=dealerIds){
				con.append(Utility.getConSqlByParamForEqual(dealerIds, param, "t", "DEALER_ID"));
			}*/
			if ("TEAM".equals(right)){
				con.append(" and app_status="+Constant.SERVICE_APPLY_ACTIVE_STATUS_REPORTED+" "); //经销商已上报
			}else if ("SERVICE".equals(right)) {
				con.append(" and app_status="+Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS+" "); //大区审核通过
			}else if ("CAR".equals(right)) {
				con.append(" and app_status="+Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_PASS+" "); //售后审核通过
			}
			PageResult<TtIfServicecarExtPO> list = dao.applyQuery(logonUser.getUserId(),con.toString(),params,curPage,Constant.PAGE_SIZE);
			//List ls = list.getRecords();
			act.setOutData("ps", list);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务车申请表轿车事业部审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: passOrRefuse 
	* @Description: TODO(通过和驳回操作) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void passOrRefuse() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long userId = logonUser.getUserId();
		Long orgId = logonUser.getOrgId();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String right = request.getParamValue("RIGHT");
			String type = request.getParamValue("TYPE");
			String orderId = request.getParamValue("ORDER_ID");
			String content = request.getParamValue("AUDIT_CONTENT");
			TtIfServicecarAuditPO param = new TtIfServicecarAuditPO();
			TtIfServicecarPO tisp = new TtIfServicecarPO();
			tisp.setOrderId(orderId);
			tisp.setUpdateBy(userId);
			param.setOrderId(orderId);
			param.setAuditContent(content);
			param.setAuditBy(userId);
			param.setOrgId(orgId);
			dao.passOrRefuse(right,type, param,tisp);
//			act.setForword(serviceCarApplyTeamAuditURL);
			serviceCarApplyCarAuditForward();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "服务车申请表轿车事业部审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}


}
