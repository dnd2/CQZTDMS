package com.infodms.dms.actions.sales.ordermanage.orderdetail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.orderdetail.OrderDetailInfoQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

public class OrderDetailInfoQuery {

	public Logger logger = Logger.getLogger(OrderDetailInfoQuery.class);   
	private final OrderDetailInfoQueryDao dao  = OrderDetailInfoQueryDao.getInstance();
	private final OrderDeliveryDao deliveryDao  = OrderDeliveryDao.getInstance();
	private final AccountBalanceDetailDao accoutDao = AccountBalanceDetailDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/ordermanage/orderdetail/orderDetailInfo.jsp";
	private final String detailUrl = "/jsp/sales/ordermanage/orderdetail/dlvryReqDetailInfo.jsp";
	/**
	 * 订单明细查询
	 */
	public void orderDetailInfoQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orgType = logonUser.getOrgType().toString() ;
			String orderId = request.getParamValue("orderId");
			String orderNo = request.getParamValue("orderNo");
			int priceId=0;
			int ids=0;
			Map<String, Object> map = dao.orderInfo(orderId, orderNo);
			List<Map<String, Object>> list =dao.orderDetail(orderId, orderNo);
			
			List<Map<String, Object>> remarkList = null ;
			
			if(orgType.equals(Constant.ORG_TYPE_DEALER.toString())) {
				String dealerId = logonUser.getDealerId() ;
				
				remarkList = dao.getRemark(orderId, orderNo, dealerId) ;
			} else {
				remarkList = dao.getRemark(orderId, orderNo) ;
			}
			
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setOutData("remarkList", remarkList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"订单明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void dlvryReqQueryInfo(){
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
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"发运订单明细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
