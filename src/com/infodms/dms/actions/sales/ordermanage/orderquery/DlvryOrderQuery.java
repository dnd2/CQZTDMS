package com.infodms.dms.actions.sales.ordermanage.orderquery;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DlvryOrderQuery {
	private Logger logger = Logger.getLogger(OemSalesOrderQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final OrderQueryDao dao = OrderQueryDao.getInstance();
	private final OrderDeliveryDao deliveryDao  = OrderDeliveryDao.getInstance();
	private final AccountBalanceDetailDao accoutDao = AccountBalanceDetailDao.getInstance();

	private final String DLVRY_ORDER_QUERY_URL = "/jsp/sales/ordermanage/orderquery/dlvryOrderQuery.jsp";//发运订单查询
	private final String DLVRY_ORDER_DETAIL_URL = "/jsp/sales/ordermanage/orderquery/dlvryOrderDetail.jsp";//发运订单明细
	
	/*
	 * 发运订单查询
	 */
	public void dlvryOrderQueryPre(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			act.setOutData("areaList", areaList);
			act.setForword(DLVRY_ORDER_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "发运订单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 发运订单查询
	 */
	public void dlvryOrderQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {

			String beginTime = request.getParamValue("beginTime");
			String endTime = request.getParamValue("endTime");
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String transType = request.getParamValue("transType");
			String reqStatus = request.getParamValue("reqStatus");
			String orderNo = request.getParamValue("orderNo");
			String initOrderNo = request.getParamValue("initOrderNo");
			String dlvryOrderNo = request.getParamValue("dlvryOrderNo");
			String isFleet = request.getParamValue("isFleet");
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")) ;
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			if(Utility.testString(beginTime)){
				beginTime = beginTime + " 00:00:00";
			}
			if(Utility.testString(endTime)){
				endTime = endTime + " 23:59:59";
			}
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());
			String dealerIds = logonUser.getDealerId();
			dealerIds = OrderQueryDao.getInstance().getDlr(dealerIds, Constant.DEALER_LEVEL_02.toString()) ;
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("areaId", areaId);
			map.put("transType", transType);
			map.put("reqStatus", reqStatus);
			map.put("orderNo", orderNo);
			map.put("initOrderNo", initOrderNo);
			map.put("dlvryOrderNo", dlvryOrderNo);
			map.put("isFleet", isFleet);
			map.put("companyId", companyId.toString());
			map.put("areaIds", areaIds);
			map.put("dealerIds", dealerIds);
			map.put("orderType", orderType) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.dlvryOrderQuery(map, curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "发运订单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void dlvryOrderDetail(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");
			String orderType = request.getParamValue("orderType");
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Map<String, Object> map = deliveryDao.getdeliveryInfoMap(reqId);
			
			String areaGet = ((BigDecimal) map.get("AREA_ID")).toString();
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			areaPO.setAreaId(new Long(areaGet));
			List<PO> areaList = dao.select(areaPO);
			Long erpCode = null;
			if(areaList.size() != 0){
				areaPO = (TmBusinessAreaPO)areaList.get(0);
				erpCode = areaPO.getErpCode();
			}
			
			List<Map<String, Object>> list = deliveryDao.getorderResourceReserveDetailList(reqId,orderType,String.valueOf(companyId),erpCode.toString());
			BigDecimal dealerId = (BigDecimal)map.get("BILLING_ORG_ID");
			List<Map<String,Object>> discountList = accoutDao.getDiscountAccountInfoByDealerId(dealerId.toString());
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setOutData("discountList", discountList);
			act.setForword(DLVRY_ORDER_DETAIL_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商确认明细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
