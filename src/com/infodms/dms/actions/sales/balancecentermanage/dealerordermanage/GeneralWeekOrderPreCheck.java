package com.infodms.dms.actions.sales.balancecentermanage.dealerordermanage;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage.DealerGeneralOrderCheckDAO;
import com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage.DealerOrderCheckDAO;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderQueryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class GeneralWeekOrderPreCheck {
	public Logger logger = Logger.getLogger(GeneralOrderPreCheck.class);
	private ActionContext act = ActionContext.getContext();
	ResponseWrapper response = act.getResponse();
	private static final GeneralOrderPreCheck dao = new GeneralOrderPreCheck();
	private final OrderQueryDao orderQueryDao = OrderQueryDao.getInstance();

	public static final GeneralOrderPreCheck getInstance() {
		return dao;
	}
	RequestWrapper request = act.getRequest();
	
	/*------------------------------------------------------------- url -------------------------------------------------------------*/
	
	private static final String GENERAL_PRE_CHK_INIT = "/jsp/sales/balancecentermanage/dealerordermanage/weekOrderPreCheckInit.jsp" ;
	private static final String GENERAL_ORDER_INFO = "/jsp/sales/balancecentermanage/dealerordermanage/weekOrderChkDtl.jsp" ;
	
	/*---------------------------------------------------------- action方法 ----------------------------------------------------------*/
	
	public void generalPreChkInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			Long poseId = logonUser.getPoseId();
			
			// 年列表
			List<String> years = orderQueryDao.getYearList() ;
			// 月列表
			List<String> months = orderQueryDao.getMonthList() ;
			// 周列表
			List<String> weeks = orderQueryDao.getWeekList() ;	
			
			// 获取只为对应业务范围
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			
			int curYear = OrderDeliveryDao.getNowDate()[0] ;
			int curMonth = OrderDeliveryDao.getNowDate()[1] ;
			int curWeek = OrderDeliveryDao.getNowDate()[2]-2 ;
			
			act.setOutData("years", years);
			act.setOutData("months", months);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", curYear);
			act.setOutData("curMonth", curMonth);
			act.setOutData("curWeek", curWeek);
			act.setOutData("areaList", areaList);
			
			act.setForword(GENERAL_PRE_CHK_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单审核页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void dealerOrderCheckInit_Query() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			GeneralOrderPreCheck gopc = new GeneralOrderPreCheck() ;
			gopc.dealerOrderCheckInit_Query() ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单审核：查询可审核的订单");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void ReqDetailInto(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = CommonUtils.checkNull(request.getParamValue("reqId"));
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			
			//1.订单信息
			Map<String, Object> orderMap = DealerGeneralOrderCheckDAO.orderInfoQuery(reqId) ;
			//2.发运申请信息
			Map<String, Object> reqMap = DealerGeneralOrderCheckDAO.reqInfoQuery(reqId) ;
			//3.发运申请明细信息
			List<Map<String, Object>> reqDtlList = DealerGeneralOrderCheckDAO.reqDtlInfoQuery(reqId) ;
			
			String areaId = reqMap.get("AREA_ID").toString() ;
			String is_fleet = reqMap.get("IS_FLEET").toString() ;
			String k_dealer = reqMap.get("K_ID").toString() ;
			String fundType = reqMap.get("FUND_TYPE").toString() ;
			
			if (null != is_fleet && "1".equals(is_fleet)) {
				Map<String,String> fleetInfo = DealerGeneralOrderCheckDAO.getFleetInfo(reqId) ;
				String fleet_id = String.valueOf(fleetInfo.get("FLEET_ID"));
				String fleet_name = String.valueOf(fleetInfo.get("FLEET_NAME"));
				String address = String.valueOf(fleetInfo.get("ADDRESS"));
				
				act.setOutData("fleet_id", fleet_id);
				act.setOutData("fleet_name", fleet_name);
				act.setOutData("address", address);
			}
			
			//4.订单审核日志信息
			String orderId = orderMap.get("ORDER_ID").toString() ;
			List<Map<String,Object>> checkHisList = DealerOrderCheckDAO.getCheckHisList(orderId);
			
			//5. 发运申请变更日志信息
			List<Map<String,Object>> reqChngList = DealerGeneralOrderCheckDAO.reqChngQuery(reqId) ;
			
			//6. 发运申请审核日志信息
			List<Map<String,Object>> reqChkList = DealerGeneralOrderCheckDAO.reqChkQuery(reqId) ;
			
			
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());// 业务范围列表
			List<Map<String, Object>> fundTypeList = AccountBalanceDetailDao.getInstance().getNoDiscountAccountInfoByDealerId(k_dealer);
	
			// 获得是否需要资金检查
			TmBusinessParaPO para = OrderReportDao.getInstance().getTmBusinessParaPO(Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, oemCompanyId);
			String isCheck = para.getParaValue();
				
			// 获得订单启票最大折扣点
			TmBusinessParaPO para2 = OrderReportDao.getInstance().getTmBusinessParaPO(Constant.ORDER_REPORT_MAX_DISCOUNT_RATE_PARA, oemCompanyId);
			String ratePara = para2.getParaValue();
			
			act.setOutData("areaList", areaList);// 业务范围列表
			act.setOutData("fundTypeList", fundTypeList);// 资金类型列表
			act.setOutData("fundType", fundType) ;
			act.setOutData("isCheck", isCheck);
			act.setOutData("is_fleet", is_fleet) ;
			act.setOutData("reqChngList", reqChngList) ;
			act.setOutData("reqChkList", reqChkList) ;  
			act.setOutData("deliveryType", Constant.TRANSPORT_TYPE_02) ;
				
			act.setOutData("orderInfo", orderMap);
			act.setOutData("reqMap", reqMap);
			act.setOutData("orderDetailList", reqDtlList);
			act.setOutData("checkHisList", checkHisList);
			act.setOutData("areaId", areaId);
			act.setOutData("ratePara", ratePara);
				
			act.setForword(GENERAL_ORDER_INFO) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商订单审核：查询要审核订单详细信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void checkSubmitAction(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER); /////
		try {
			GeneralOrderPreCheck gopc = new GeneralOrderPreCheck() ;
			gopc.checkSubmitAction() ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商普通订单预审核：提交");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void retBack() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try {
			GeneralOrderPreCheck gopc = new GeneralOrderPreCheck() ;
			gopc.retBack() ;
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商普通订单预审核：取消");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
