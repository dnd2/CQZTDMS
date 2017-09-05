/**
 * @Title: DealerQuotaConfirmIssue.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-22
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sales.planmanage.QuotaAssign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.orgmng.GetOrgIdsOrDealerIdsDAO;
import com.infodms.dms.dao.sales.planmanage.QuotaAssignDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class DealerQuotaConfirmIssue {
	private Logger logger = Logger.getLogger(DealerQuotaConfirmIssue.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final QuotaAssignDao dao = QuotaAssignDao.getInstance();

	private final String DEALER_QUOTA_CONFIRM_QUERY_URL = "/jsp/sales/planmanage/quotaassign/dealerQuotaConfirmQuery.jsp";// 经销商配额确认下发初始页面
	private final String DEALER_QUOTA_ISSUE_QUERY_URL = "/jsp/sales/planmanage/quotaassign/dealerQuotaIssueQuery.jsp";// 经销商配额确认下发页面
	private final String DEALER_QUOTA_ISSUE_DETAIL_URL = "/jsp/sales/planmanage/quotaassign/dealerQuotaIssueDetail.jsp";// 经销商配额确认下发明细页面

	// 经销商配额确认下发按业务范围分组pre
	public void dealerQuotaConfirmIssuePre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(DEALER_QUOTA_CONFIRM_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商配额确认下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 经销商配额确认下发按业务范围分组查询
	public void dealerQuotaConfirmQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrder(
					orgId, parentOrgId, dutyType, "TD");
			String companyId = logonUser.getCompanyId().toString();
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao
					.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getDealerQuotaConfirmQueryList(map, curPage,
							Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商配额确认下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 经销商配额确认下发pre
	public void dealerQuotaIssuePre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String quotaYear = CommonUtils.checkNull(request
					.getParamValue("quotaYear"));
			String quotaMonth = CommonUtils.checkNull(request
					.getParamValue("quotaMonth"));

			act.setOutData("areaId", areaId);
			act.setOutData("quotaYear", quotaYear);
			act.setOutData("quotaMonth", quotaMonth);

			act.setForword(DEALER_QUOTA_ISSUE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商配额确认下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 经销商配额确认下发查询
	public void dealerQuotaIssueQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String quotaYear = CommonUtils.checkNull(request
					.getParamValue("quotaYear"));
			String quotaMonth = CommonUtils.checkNull(request
					.getParamValue("quotaMonth"));
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrderNew(
					orgId, parentOrgId, dutyType, "TD1");
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("areaId", areaId);
			map.put("quotaYear", quotaYear);
			map.put("quotaMonth", quotaMonth);
			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("orgId", logonUser.getOrgId().toString());
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getDealerQuotaIssueQueryList(map, curPage,
							Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商配额确认下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 经销商配额确认下发
	public void dealerQuotaIssue() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String quotaYear = CommonUtils.checkNull(request
					.getParamValue("quotaYear"));
			String quotaMonth = CommonUtils.checkNull(request
					.getParamValue("quotaMonth"));
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIds(
					orgId, parentOrgId, dutyType);
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("areaId", areaId);
			map.put("quotaYear", quotaYear);
			map.put("quotaMonth", quotaMonth);
			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);

			dao.dealerQuotaIssue(map);

			act.setOutData("returnValue", 1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商配额确认下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 经销商配额确认下发明细
	public void dealerQuotaIssueDetailPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String quotaYear = CommonUtils.checkNull(request
					.getParamValue("quotaYear"));
			String quotaMonth = CommonUtils.checkNull(request
					.getParamValue("quotaMonth"));
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String dealerId = CommonUtils.checkNull(request
					.getParamValue("dealerId"));
			String groupId = CommonUtils.checkNull(request
					.getParamValue("groupId"));
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("quotaYear", quotaYear);
			map.put("quotaMonth", quotaMonth);
			map.put("areaId", areaId);
			map.put("dealerId", dealerId);
			map.put("groupId", groupId);
			map.put("companyId", companyId);

			List<Map<String, Object>> list = dao
					.getDealerQuotaIssueSerieAmount(map);

			act.setOutData("quotaYear", quotaYear);
			act.setOutData("quotaMonth", quotaMonth);
			act.setOutData("areaId", areaId);
			act.setOutData("dealerId", dealerId);
			act.setOutData("groupId", groupId);
			act.setOutData("list", list);

			act.setForword(DEALER_QUOTA_ISSUE_DETAIL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商配额确认下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void dealerQuotaIssueDetailQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String quotaYear = CommonUtils.checkNull(request
					.getParamValue("quotaYear"));
			String quotaMonth = CommonUtils.checkNull(request
					.getParamValue("quotaMonth"));
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String dealerId = CommonUtils.checkNull(request
					.getParamValue("dealerId"));
			String groupId = CommonUtils.checkNull(request
					.getParamValue("groupId"));
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("quotaYear", quotaYear);
			map.put("quotaMonth", quotaMonth);
			map.put("areaId", areaId);
			map.put("dealerId", dealerId);
			map.put("groupId", groupId);
			map.put("companyId", companyId);

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getDealerQuotaIssueDetailList(map, curPage,
							Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商配额确认下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
