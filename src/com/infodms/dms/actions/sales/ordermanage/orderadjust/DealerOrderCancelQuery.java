/**
 * @Title: DealerOrderCancelQuery.java
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.ordermanage.orderadjust.OrderAdjustDao;
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
public class DealerOrderCancelQuery {
	private Logger logger = Logger.getLogger(DealerOrderCancelQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final OrderAdjustDao dao = OrderAdjustDao.getInstance();

	private final String DEALER_ORDER_CANCEL_QUERY_URL = "/jsp/sales/ordermanage/orderadjust/dealerOrderCancelQuery.jsp";// 订单取消查询页面

	/**
	 * 订单取消初始化
	 */
	public void orderCancelQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerBusiness(poseId.toString());
			act.setOutData("areaList", areaList);
			act.setForword(DEALER_ORDER_CANCEL_QUERY_URL);
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
			String startDate = CommonUtils.checkNull(request
					.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request
					.getParamValue("endDate"));
			String orderType = CommonUtils.checkNull(request
					.getParamValue("orderType"));
			String areaId = CommonUtils.checkNull(request
					.getParamValue("areaId"));
			String dealerId = logonUser.getDealerId();
			String companyId = logonUser.getOemCompanyId();
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao
					.getDealerBusinessIdStr(poseId.toString());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("orderType", orderType);
			map.put("areaId", areaId);
			map.put("dealerId", dealerId);
			map.put("companyId", companyId);
			map.put("areaIds", areaIds);

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getDealerOrderCancelQueryList(map, curPage,
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
