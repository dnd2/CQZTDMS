package com.infodms.dms.actions.sales.planmanage.QuotaAssign;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.planmanage.StorageConfirmDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtVsQuotaPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @author yinshunhui
 *
 */
public class StorageConfirm {
	private Logger logger=Logger.getLogger(StorageImport.class);
	private ActionContext act=ActionContext.getContext();
	RequestWrapper request=act.getRequest();
	ResponseWrapper response=act.getResponse();
	
	private final StorageConfirmDao dao = StorageConfirmDao.getInstance();
	//页面跳转的路径
	private final String STORAGE_CONFIRM_QUERY_URL = "/jsp/sales/planmanage/quotaassign/storageConfirmQuery.jsp";// 区域配额确认下发初始页面
	private final String STORAGE_SURE_QUERY_URL="/jsp/sales/planmanage/quotaassign/storageSureQuery.jsp";
	private final String STORAGE_SURE_DETAIL_URL = "/jsp/sales/planmanage/quotaassign/storageSureDetail.jsp";// 区域配额确认下发明细页面
	// 车厂配额确认下发按业务范围分组pre
	public void storageConfirmPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(STORAGE_CONFIRM_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "区域配额确认下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	// 车厂配额确认按业务范围分组查询
	public void storageConfirmQuery() {
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
					.getStorageConfirmQueryList(map, curPage,
							Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车厂配额确认");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	// 车厂配额确认pre
	public void storageSurePre() {
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

			act.setForword(STORAGE_SURE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车厂配额确认");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	// 车厂配额确认下发查询
	public void storageSureQuery() {
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
					.getStorageQueryList(map, curPage,
							Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车厂配额确认");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 车厂配额确认
	public void storageSure() {
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
			po.setQuotaType(Constant.QUOTA_TYPE_03);
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

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车厂配额确认");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 车厂配额确认明细
	public void storageSureDetailPre() {
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
					.getStorageSureSerieAmount(map);

			act.setOutData("quotaYear", quotaYear);
			act.setOutData("quotaMonth", quotaMonth);
			act.setOutData("areaId", areaId);
			act.setOutData("orgId", orgId);
			act.setOutData("groupId", groupId);
			act.setOutData("list", list);

			act.setForword(STORAGE_SURE_DETAIL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车厂配额确认");
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
					.getStorageSureDetailList(map, curPage,
							Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车厂配额确认");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
