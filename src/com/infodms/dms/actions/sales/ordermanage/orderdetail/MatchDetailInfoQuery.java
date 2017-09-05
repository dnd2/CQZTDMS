/**
 * @Title: MatchDetailInfoQuery.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-27
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sales.ordermanage.orderdetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderquery.DealerSalesOrderQuery;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.ordermanage.orderdetail.OrderDetailInfoQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

/**
 * @author yuyong
 * 
 */
public class MatchDetailInfoQuery {
	private Logger logger = Logger.getLogger(DealerSalesOrderQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final OrderDetailInfoQueryDao dao = OrderDetailInfoQueryDao
			.getInstance();

	private final String MATCH_INFO_URL = "/jsp/sales/ordermanage/orderdetail/matchDetailInfo.jsp";// 销售订单查询页面

	public void matchDetailInfoQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String matId = CommonUtils.checkNull(request.getParamValue("matId"));

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderNo", orderNo);
			map.put("matId", matId);

			List<Map<String, Object>> matchList = dao.getMatchList(map);

			act.setOutData("matchList", matchList);
			act.setForword(MATCH_INFO_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单车辆明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void matchInfoQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String deliveryId = CommonUtils.checkNull(request
					.getParamValue("deliveryId"));

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deliveryId", deliveryId);

			List<Map<String, Object>> matchList = dao.getMatchList2(map);

			act.setOutData("matchList", matchList);
			act.setForword(MATCH_INFO_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单车辆明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
