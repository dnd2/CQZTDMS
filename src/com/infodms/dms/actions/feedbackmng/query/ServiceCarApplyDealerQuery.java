package com.infodms.dms.actions.feedbackmng.query;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.feedbackMng.ServiceCarApplyDAO;
import com.infodms.dms.dao.orgmng.GetOrgIdsOrDealerIdsDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtIfServicecarExtPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
* @ClassName: ServiceCarApplyDealerQuery 
* @Description: TODO(服务活动申请表经销商端查询) 
* @author wangchao 
* @date May 24, 2010 5:26:12 PM 
*
 */
public class ServiceCarApplyDealerQuery {

	public Logger logger = Logger.getLogger(ServiceCarApplyQuery.class);
	private ServiceCarApplyDAO dao = ServiceCarApplyDAO.getInstance();
	private final String serviceCarApplyDealerQueryURL = "/jsp/feedbackMng/query/serviceCarApplyDealerQuery.jsp";// 查询页面
	private final String serviceCarApplyDealerMiniQueryURL = "/jsp/feedbackMng/query/serviceCarApplyDealerMiniQuery.jsp";// 查询页面

	/**
	 * 
	* @Title: serviceCarApplyQueryForward 
	* @Description: TODO(查询跳转页面) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void serviceCarApplyQueryForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(serviceCarApplyDealerQueryURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "服务活动申请表查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/**
	 * 
	* @Title: serviceCarApplyQueryForward 
	* @author subo
	* @Description: TODO(微车查询跳转页面) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void serviceCarApplyQueryMiniInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(serviceCarApplyDealerMiniQueryURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "服务活动申请表查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/**
	 * 
	* @Title: applyMiniQuery 
	* @author subo
	* @Description: TODO(查询) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void applyMiniQuery() {

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			StringBuffer con = new StringBuffer();
			String orderId = request.getParamValue("ORDER_ID");
			String strDate = request.getParamValue("CON_APPLY_DATE_START");
			String endDate = request.getParamValue("CON_APPLY_DATE_END");
			String modelId = request.getParamValue("MODEL_ID");
			String appStatus = request.getParamValue("APP_STATUS");
			
			//转换APP_STATUS(由于轿车微车用2套tc_code,有重复代码)--此处查询转换,插入不用再修改
			if(Constant.SERVICE_APPLY_ACTIVE_STATUS_UNREPORT_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_STATUS_UNREPORT.toString();
			if(Constant.SERVICE_APPLY_ACTIVE_STATUS_REPORTED_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_STATUS_REPORTED.toString();
			if(Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS.toString();
			if(Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT.toString();
			if(Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_PASS_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_PASS.toString();
			if(Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT.toString();
			if(Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_PASS_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_PASS.toString();
			if(Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT_MINI.toString().equals(appStatus))
				appStatus = Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT.toString();
			
			// 工单号like
			if (orderId != null && !"".equals(orderId)) {
				con.append(" and ORDER_ID like'%" + orderId + "%' "); //
			}
			// 介于开始时间
			if (strDate != null && !"".equals(strDate)) {
				con.append(" and APP_DATE >= to_date('" + strDate +" 00:00:00"
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 结束时间
			if (endDate != null && !"".equals(endDate)) {
				con.append(" and APP_DATE <= to_date('" + endDate + " 23:59:59"
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 经销商代码
			if (dealerId != null && !"".equals(dealerId)) {
				con.append(" and t.DEALER_ID='" + dealerId + "' ");
			}
			// 车型
			if (modelId != null && !"".equals(modelId)) {
				con.append(" and T.GROUP_ID='" + modelId + "' ");
			}
			//申请状态
			if (appStatus != null && !"".equals(appStatus)) {
				//审核通过：大区审核，售后服务部，轿车事业部
				/*if (Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS.equals(appStatus)) {
					con.append(" and (T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_PASS + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_PASS + "') ");
				//审核驳回：大区审核，售后服务部，轿车事业部
				}else if (Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT.equals(appStatus)){
					con.append(" and (T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT + "') ");
				}else {*/
					//已上报，待上报
					con.append(" and T.APP_STATUS='" + appStatus + "' ");
				/*}*/
			}

			PageResult<TtIfServicecarExtPO> list = dao.applyQuery(con
					.toString(),null, curPage, Constant.PAGE_SIZE);
			// List ls = list.getRecords();
			act.setOutData("ps", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动申请表");
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
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			StringBuffer con = new StringBuffer();
			String orderId = request.getParamValue("ORDER_ID");
			String strDate = request.getParamValue("CON_APPLY_DATE_START");
			String endDate = request.getParamValue("CON_APPLY_DATE_END");
			String modelId = request.getParamValue("MODEL_ID");
			String appStatus = request.getParamValue("APP_STATUS");
			// 工单号like
			if (orderId != null && !"".equals(orderId)) {
				con.append(" and ORDER_ID like'%" + orderId + "%' "); //
			}
			// 介于开始时间
			if (strDate != null && !"".equals(strDate)) {
				con.append(" and APP_DATE >= to_date('" + strDate +" 00:00:00"
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 结束时间
			if (endDate != null && !"".equals(endDate)) {
				con.append(" and APP_DATE <= to_date('" + endDate + " 23:59:59"
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 经销商代码
			if (dealerId != null && !"".equals(dealerId)) {
				con.append(" and t.DEALER_ID='" + dealerId + "' ");
			}
			// 车型
			if (modelId != null && !"".equals(modelId)) {
				con.append(" and T.GROUP_ID='" + modelId + "' ");
			}
			//申请状态
			if (appStatus != null && !"".equals(appStatus)) {
				//审核通过：大区审核，售后服务部，轿车事业部
				/*if (Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS.equals(appStatus)) {
					con.append(" and (T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_PASS + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_PASS + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_PASS + "') ");
				//审核驳回：大区审核，售后服务部，轿车事业部
				}else if (Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT.equals(appStatus)){
					con.append(" and (T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_AREA_STATUS_REJECT + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_CAR_ENTER_STATUS_REJECT + "' ");
					con.append(" or T.APP_STATUS='" + Constant.SERVICE_APPLY_ACTIVE_SERVICE_STATUS_REJECT + "') ");
				}else {*/
					//已上报，待上报
					con.append(" and T.APP_STATUS='" + appStatus + "' ");
				/*}*/
			}

			PageResult<TtIfServicecarExtPO> list = dao.applyQuery(con
					.toString(),null, curPage, Constant.PAGE_SIZE);
			// List ls = list.getRecords();
			act.setOutData("ps", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动申请表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

}
