/**
 * @Title: OemOrderCancelQuery.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-11
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sales.ordermanage.orderadjust;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.orgmng.GetOrgIdsOrDealerIdsDAO;
import com.infodms.dms.dao.sales.ordermanage.orderadjust.OrderAdjustDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderQueryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class OemOrderCancelQuery {
	private Logger logger = Logger.getLogger(OemOrderCancelQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final OrderAdjustDao dao = OrderAdjustDao.getInstance();
	private final OrderQueryDao queryDao = OrderQueryDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();

	private final String OEM_ORDER_CANCEL_QUERY_URL = "/jsp/sales/ordermanage/orderadjust/oemOrderCancelQuery.jsp";// 订单取消查询页面

	/**
	 * 订单取消初始化
	 */
	public void orderCancelQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String dutyType = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			List<String> years = queryDao.getYearList();
			List<String> weeks = queryDao.getWeekList();
			// 业务范围列表
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getPoseIdBusiness(poseId.toString());
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(),
					oemCompanyId);

			act.setOutData("areaList", areaList);
			act.setOutData("orgId", dutyType.equals(Constant.DUTY_TYPE_DEPT
					.toString()) ? parentOrgId : orgId);
			act.setOutData("years", years);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", dateSet != null ? dateSet.getSetYear()
					: "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek()
					: "");
			act.setForword(OEM_ORDER_CANCEL_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单取消");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 订单取消查询
	 */
	public void orderCancelQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orderYear1 = CommonUtils.checkNull(request
					.getParamValue("orderYear1"));
			String orderWeek1 = CommonUtils.checkNull(request
					.getParamValue("orderWeek1"));
			String orderYear2 = CommonUtils.checkNull(request
					.getParamValue("orderYear2"));
			String orderWeek2 = CommonUtils.checkNull(request
					.getParamValue("orderWeek2"));
			String dealerCode = CommonUtils.checkNull(request
					.getParamValue("dealerCode"));
			String orderType = CommonUtils.checkNull(request
					.getParamValue("orderType"));
			String orderNo = CommonUtils.checkNull(request
					.getParamValue("orderNo"));
			String orgId = logonUser.getOrgId().toString();
			String parentOrgId = logonUser.getParentOrgId();
			String dutyType = logonUser.getDutyType();
			String dealerSql = GetOrgIdsOrDealerIdsDAO.getDealerIdsForOrder(
					orgId, parentOrgId, dutyType, "TD1");
			String companyId = logonUser.getCompanyId().toString();
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime")); 
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao
					.getPoseIdBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderYear1", orderYear1);
			map.put("orderWeek1", orderWeek1);
			map.put("orderYear2", orderYear2);
			map.put("orderWeek2", orderWeek2);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("orderNo", orderNo);
			map.put("dealerSql", dealerSql);
			map.put("companyId", companyId);
			map.put("areaId", areaId);
			map.put("areaIds", areaIds);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getOemOrderCancelQueryList(map, curPage,
							Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单取消查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
