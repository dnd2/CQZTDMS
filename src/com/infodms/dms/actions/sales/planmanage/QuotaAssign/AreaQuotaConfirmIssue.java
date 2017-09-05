/**
 * @Title: AreaQuotaConfirmIssue.java
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

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.planmanage.QuotaAssignDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtVsQuotaPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * @author yuyong
 *
 */
public class AreaQuotaConfirmIssue {
	private Logger logger = Logger.getLogger(AreaQuotaConfirmIssue.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final QuotaAssignDao dao = QuotaAssignDao.getInstance();

	private final String AREA_QUOTA_CONFIRM_QUERY_URL = "/jsp/sales/planmanage/quotaassign/areaQuotaConfirmQuery.jsp";// 区域配额确认下发初始页面
	private final String AREA_QUOTA_ISSUE_QUERY_URL = "/jsp/sales/planmanage/quotaassign/areaQuotaIssueQuery.jsp";// 区域配额确认下发页面
	private final String AREA_QUOTA_ISSUE_DETAIL_URL = "/jsp/sales/planmanage/quotaassign/areaQuotaIssueDetail.jsp";// 区域配额确认下发明细页面

	// 区域配额确认下发按业务范围分组pre
	public void areaQuotaConfirmIssuePre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(AREA_QUOTA_CONFIRM_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "区域配额确认下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 区域配额确认下发按业务范围分组查询
	public void areaQuotaConfirmQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String companyId = logonUser.getCompanyId().toString();
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao
					.getPoseIdBusinessIdStr(poseId.toString());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getAreaQuotaConfirmQueryList(map, curPage,
							Constant.PAGE_SIZE_MAX,logonUser.getOrgId());
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "区域配额确认下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 区域配额确认下发pre
	public void areaQuotaIssuePre() {
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

			act.setForword(AREA_QUOTA_ISSUE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "区域配额确认下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 区域配额确认下发查询
	public void areaQuotaIssueQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String quotaYear = CommonUtils.checkNull(request
					.getParamValue("quotaYear"));
			String quotaMonth = CommonUtils.checkNull(request
					.getParamValue("quotaMonth"));
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("areaId", areaId);
			map.put("quotaYear", quotaYear);
			map.put("quotaMonth", quotaMonth);
			map.put("companyId", companyId);

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getAreaQuotaIssueQueryList(map, curPage,
							Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "区域配额确认下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 区域配额确认下发
	public void areaQuotaIssue() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String quotaYear = CommonUtils.checkNull(request
					.getParamValue("quotaYear"));
			String quotaMonth = CommonUtils.checkNull(request
					.getParamValue("quotaMonth"));

			TtVsQuotaPO po = new TtVsQuotaPO();
			po.setQuotaYear(new Integer(quotaYear));
			po.setQuotaMonth(new Integer(quotaMonth));
			po.setAreaId(new Long(areaId));
			po.setQuotaType(Constant.QUOTA_TYPE_01);
			po.setStatus(Constant.QUOTA_STATUS_01);

			List<PO> list = dao.select(po);
			for (int i = 0; i < list.size(); i++) {
				TtVsQuotaPO quota = (TtVsQuotaPO) list.get(i);

				TtVsQuotaPO condition = new TtVsQuotaPO();
				condition.setQuotaId(quota.getQuotaId());

				TtVsQuotaPO value = new TtVsQuotaPO();
				value.setStatus(Constant.QUOTA_STATUS_02);
				value.setUpdateBy(logonUser.getUserId());
				value.setUpdateDate(new Date());

				dao.update(condition, value);
            }

            act.setOutData("returnValue", 1);

            //调用配额计算存储过程
            List<Object> ins = new LinkedList<Object>();
            ins.add(logonUser.getUserId());
            ins.add(areaId);
            ins.add(quotaYear);
            ins.add(quotaMonth);

            List<Integer> outs = new LinkedList<Integer>();

            RunAfterDispToOrgThread radtot=new RunAfterDispToOrgThread("P_RUN_AFTER_DISP_TO_ORG", ins, outs);
            Thread thread = new Thread(radtot);
            thread.start();

        } catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "区域配额确认下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 区域配额确认下发明细
	public void areaQuotaIssueDetailPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String quotaYear = CommonUtils.checkNull(request
					.getParamValue("quotaYear"));
			String quotaMonth = CommonUtils.checkNull(request
					.getParamValue("quotaMonth"));
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String orgId = CommonUtils
					.checkNull(request.getParamValue("orgId"));
			String groupId = CommonUtils.checkNull(request
					.getParamValue("groupId"));
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("quotaYear", quotaYear);
			map.put("quotaMonth", quotaMonth);
			map.put("areaId", areaId);
			map.put("orgId", orgId);
			map.put("groupId", groupId);
			map.put("companyId", companyId);

			List<Map<String, Object>> list = dao
					.getAreaQuotaIssueSerieAmount(map);

			act.setOutData("quotaYear", quotaYear);
			act.setOutData("quotaMonth", quotaMonth);
			act.setOutData("areaId", areaId);
			act.setOutData("orgId", orgId);
			act.setOutData("groupId", groupId);
			act.setOutData("list", list);

			act.setForword(AREA_QUOTA_ISSUE_DETAIL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "区域配额确认下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void areaQuotaIssueDetailQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String quotaYear = CommonUtils.checkNull(request
					.getParamValue("quotaYear"));
			String quotaMonth = CommonUtils.checkNull(request
					.getParamValue("quotaMonth"));
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String orgId = CommonUtils
					.checkNull(request.getParamValue("orgId"));
			String groupId = CommonUtils.checkNull(request
					.getParamValue("groupId"));
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("quotaYear", quotaYear);
			map.put("quotaMonth", quotaMonth);
			map.put("areaId", areaId);
			map.put("orgId", orgId);
			map.put("groupId", groupId);
			map.put("companyId", companyId);

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getAreaQuotaIssueDetailList(map, curPage,
							Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "区域配额确认下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
