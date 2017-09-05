package com.infodms.dms.actions.crm.report;

import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderaudit.OrderResourceReserveFirst;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.report.ReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class Report {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String DAILY_SALES_REPORT_URL = "/jsp/crm/report/dailySalesReport.jsp";// 每日销售简报
	private final String CTM_DISTRIBUTE_REPORT_URL = "/jsp/crm/report/ctmDistributeReport.jsp";// 建档客户分布表
	private final String SALES_RANKING_REPORT_URL = "/jsp/crm/report/salesRankingReport.jsp";// 销售排名报表
	private final String SALES_RANKING_DETAIL_URL = "/jsp/crm/report/salesRankingDetail.jsp";// 销售排名报表详细
	
	//每日销售简报查询页面
	public void dailySalesReport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("dutyType", logonUser.getDutyType());
            act.setOutData("orgId", logonUser.getOrgId());
			act.setForword(DAILY_SALES_REPORT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "进入每日销售简报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//每日销售简报查询
	public void dailySalesReportQuery() {
		ReportDao dao = new ReportDao();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
		String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
		String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
		try {
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.dailySalesReportQuery(dealerCode,
					startDate,endDate,
					curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询每日销售简报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//建档客户分布表
	public void ctmDistributeReport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("dutyType", logonUser.getDutyType());
            act.setOutData("orgId", logonUser.getOrgId());
			act.setForword(CTM_DISTRIBUTE_REPORT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "进入建档客户分布表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//建档客户分布表查询
	public void ctmDistributeReportQuery() {
		ReportDao dao = new ReportDao();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
		String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
		String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
		try {
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.ctmDistributeReportQuery(dealerCode,
					startDate,endDate,
					curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询建档客户分布表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//销售排名报表
	public void salesRankingReport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("dutyType", logonUser.getDutyType());
            act.setOutData("orgId", logonUser.getOrgId());
			act.setForword(SALES_RANKING_REPORT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "进入销售排名报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//销售排名报表查询
	public void salesRankingReportQuery() {
		ReportDao dao = new ReportDao();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
		String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
		String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
		try {
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.salesRankingReportQuery(dealerCode,
					startDate,endDate,
					curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询销售排名报表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//销售排名报表详细页面
	public void salesRankingDetail() {
		ActionContext act = ActionContext.getContext();
		
		RequestWrapper request = act.getRequest();
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try {
			String dealerId=request.getParamValue("dealerId");
			String startDate=request.getParamValue("startDate");
			String endDate=request.getParamValue("endDate");
			
			act.setOutData("dealerId", dealerId);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setForword(SALES_RANKING_DETAIL_URL);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//销售排名详细查询
	public void salesRankingDetailQuery() {
		ReportDao dao = new ReportDao();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
		String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
		String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
		try {
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.salesRankingDetailQuery(dealerId,
					startDate,endDate,
					curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询销售排名详细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
