package com.infodms.dms.actions.feedbackmng.query;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.feedbackMng.ServiceActivityApplyDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtIfWrActivityExtPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName: ServiceCarApplyDealerQuery
 * @Description: TODO(服务车申请表经销商端查询)
 * @author wangchao
 * @date May 24, 2010 5:26:12 PM
 * 
 */
public class ServiceActivityApplyDealerQuery {

	public Logger logger = Logger
			.getLogger(ServiceActivityApplyDealerQuery.class);
	private ServiceActivityApplyDAO dao = ServiceActivityApplyDAO.getInstance();
	private final String serviceActivityApplyDealerQueryURL = "/jsp/feedbackMng/query/serviceActivityApplyDealerQuery.jsp";// 查询页面

	/**
	 * 
	 * @Title: serviceCarApplyQueryForward
	 * @Description: TODO(查询跳转页面)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void serviceActivityApplyQueryForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.setOutData("seriesList", InfoFeedBackMng
					.getVehicleSeriesByDealerId());
			act.setForword(serviceActivityApplyDealerQueryURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, "服务车申请表查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 * @Title: applyQuery
	 * @Description: TODO(查询)
	 * @param 设定文件
	 * @return void 返回类型
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
			String actType = request.getParamValue("ACT_TYPE");
			String appStatus = request.getParamValue("APP_STATUS");
			String actName = request.getParamValue("ACT_NAME");
			// 工单号like
			if (orderId != null && !"".equals(orderId)) {
				con.append(" and ORDER_ID like'%" + orderId + "%' "); //
			}
			// 介于开始时间
			if (strDate != null && !"".equals(strDate)) {
				con.append(" and ACT_DATE >= to_date('" + strDate +" 00:00:00"
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 结束时间
			if (endDate != null && !"".equals(endDate)) {
				con.append(" and ACT_DATE <= to_date('" + endDate + " 23:59:59"
						+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			}
			// 经销商代码
			if (dealerId != null && !"".equals(dealerId)) {
				con.append(" and t.DEALER_ID='" + dealerId + "' ");
			}
			// 车型
			if (actType != null && !"".equals(actType)) {
				con.append(" and T.ACT_TYPE='" + actType + "' ");
			}
			// 申请状态
			if (appStatus != null && !"".equals(appStatus)) {
				con.append(" and T.ACT_STATUS='" + appStatus + "' ");
			}
			// 活动名称
			if (actName != null && !"".equals(actName)) {
				con.append(" and T.ACT_NAME LIKE'%" + actName + "%' ");
			}
			con.append(" and t.act_type<>"+Constant.SERVICE_APPLY_ACTIVE_STATUS_UNREPORT);
			PageResult<TtIfWrActivityExtPO> list = dao.applyQuery(con
					.toString(), null,curPage, Constant.PAGE_SIZE);
			// List ls = list.getRecords();
			act.setOutData("ps", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "服务车申请表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

}
