/**
 * 月度订单审核
 * */
package com.infodms.dms.actions.sales.ordermanage.orderaudit;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.storageManage.CheckVehicle;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.audit.MonthOrderCheckDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtVsOrderCheckPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class MonthOrderCheck extends BaseDao{

	public Logger logger = Logger.getLogger(CheckVehicle.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final MonthOrderCheckDao dao = new MonthOrderCheckDao ();
	public static final MonthOrderCheckDao getInstance() {
		return dao;
	}
	OrderDeliveryDao deliveryDao = OrderDeliveryDao.getInstance();
	private final String MONTH_ORDER_PRE_CHECK_INIT_URL = "/jsp/sales/ordermanage/orderaudit/monthOrderPreChkInit.jsp" ;
	private final String  monthOrderCheck_BUS_Init_URL = "/jsp/sales/ordermanage/orderaudit/monthOrderCheck_BUS_Init.jsp";	//  月度订单审核：页面初始化(事业部)
	private final String  monthOrderCheck_SAL_Init_URL = "/jsp/sales/ordermanage/orderaudit/monthOrderCheck_SAL_Init.jsp";	//  月度订单审核：页面初始化(销售部)
	private final String  monthOrderCheck_BUS_Detail_URL = "/jsp/sales/ordermanage/orderaudit/monthOrderCheck_BUS_Detail.jsp";	//  月度订单审核：页面初始化(事业部)
	private final String  monthOrderCheck_SAL_Detail_URL = "/jsp/sales/ordermanage/orderaudit/monthOrderCheck_SAL_Detail.jsp";	//  月度订单审核：页面初始化(事业部)
	
	public void monthOrderPreCheckInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> dateList = deliveryDao.getMonthGeneralDateList(oemCompanyId);
			List<Map<String, Object>> areaList=MaterialGroupManagerDao.getDealerLevelBusiness(logonUser.getPoseId().toString());
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			act.setForword(MONTH_ORDER_PRE_CHECK_INIT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月的订单审核：页面初始化(事业部)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 月度订单审核：页面初始化(事业部)
	 * */
	public void monthOrderCheck_BUS_Init() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> dateList = deliveryDao.getMonthGeneralDateList(oemCompanyId);
			List<Map<String, Object>> areaList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			act.setForword(monthOrderCheck_BUS_Init_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月的订单审核：页面初始化(事业部)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void monthOrderPreCheckQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String year = "";
			String month = "";
			String orderMonth = CommonUtils.checkNull(request.getParamValue("orderMonth"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNo"));
			if (!"".equals(orderMonth)) {
				String[] array = orderMonth.split("-");
				year = array[0];
				month = array[1];
			}
			
			String dealerId = new DealerRelation().getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId()) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
			PageResult<Map<String, Object>> ps = dao.monthOrderPreCheckQuery(year, month, areaId, dealerId, orderNO, Constant.PAGE_SIZE, curPage) ;
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月的订单审核：页面初始化(预审核)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 月度订单审核：查询可审核的订单列表(事业部)
	 * */
	public void monthOrderCheck_BUS_Query(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String year = "";
			String month = "";
			String orderMonth = CommonUtils.checkNull(request.getParamValue("orderMonth"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNo"));
			
			String dutyType = logonUser.getDutyType() ;
			
			if (!"".equals(orderMonth)) {
				String[] array = orderMonth.split("-");
				year = array[0];
				month = array[1];
			}
			
			Long orgId = logonUser.getOrgId();
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)) {
				orgId = null ;
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
			PageResult<Map<String, Object>> ps = dao.monthOrderCheck_BUS_QueryList(orgId,year, month, areaId, dealerCode, orderNO, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月的订单审核：页面初始化(事业部)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	public void monthOrderCheck_BUS_Detail(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			Map<String,Object> orderInfo = dao.getOrderInfo(orderId);
			List<Map<String,Object>> dList = dao.getDetailOrderList(orderId);
			act.setOutData("orderInfo", orderInfo);
			act.setOutData("dList", dList);
			act.setForword(monthOrderCheck_BUS_Detail_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月的订单审核：页面初始化(事业部)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 月度订单审核提交（事业部）
	 * */
	public void monthOrderCheck_BUS_submit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String checkStatus = CommonUtils.checkNull(request.getParamValue("checkStatus"));
			String checkDesc = CommonUtils.checkNull(request.getParamValue("checkDesc"));
			
			String dutyType = logonUser.getDutyType() ;

			TtVsOrderPO tempPO = new TtVsOrderPO();
			tempPO.setOrderId(Long.parseLong(orderId));
			
			TtVsOrderPO valuePO = new TtVsOrderPO();
			if ("1".equals(checkStatus)) {
				if(Constant.DUTY_TYPE_DEALER.toString().equals(dutyType))
					valuePO.setOrderStatus(Constant.ORDER_STATUS_08);
				else					
					valuePO.setOrderStatus(Constant.ORDER_STATUS_09);
			}else{
				valuePO.setOrderStatus(Constant.ORDER_STATUS_04);
			}
			valuePO.setUpdateBy(logonUser.getUserId());
			valuePO.setUpdateDate(new Date());
			dao.update(tempPO, valuePO);
			
			TtVsOrderCheckPO checkPO = new TtVsOrderCheckPO();
			checkPO.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
			checkPO.setOrderId(Long.parseLong(orderId));
			checkPO.setCheckOrgId(logonUser.getOrgId());
			checkPO.setCheckPositionId(logonUser.getPoseId());
			checkPO.setCheckUserId(logonUser.getUserId());
			checkPO.setCheckDate(new Date());
			if ("1".equals(checkStatus)) {
				if(Constant.DUTY_TYPE_DEALER.toString().equals(dutyType))
					checkPO.setCheckStatus(Constant.CHECK_STATUS_01);
				else
					checkPO.setCheckStatus(Constant.CHECK_STATUS_03);
			}else{
				if(Constant.DUTY_TYPE_DEALER.toString().equals(dutyType))
					checkPO.setCheckStatus(Constant.CHECK_STATUS_02);
				else
					checkPO.setCheckStatus(Constant.CHECK_STATUS_04);
			}
			if (!"".equals(checkDesc)) {
				checkPO.setCheckDesc(checkDesc.trim());
			}
			
			if(Constant.DUTY_TYPE_DEALER.toString().equals(dutyType))
				checkPO.setCheckStatus(Constant.ORDER_CHECK_TYPE_DLR);
			else if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType))
				checkPO.setCheckType(Constant.ORDER_CHECK_TYPE_01);
			
			checkPO.setCreateBy(logonUser.getUserId());
			checkPO.setCreateDate(new Date());
			dao.insert(checkPO);
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月的订单审核：页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 月度订单审核：页面初始化(销售部)
	 * */
	public void monthOrderCheck_SAL_Init() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> dateList = deliveryDao.getMonthGeneralDateList(oemCompanyId);
			List<Map<String, Object>> areaList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			act.setForword(monthOrderCheck_SAL_Init_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月的订单审核：页面初始化(事业部)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 月度订单审核：查询可审核的订单列表(销售部)
	 * */
	public void monthOrderCheck_SAL_Query(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String year = "";
			String month = "";
			String orderMonth = CommonUtils.checkNull(request.getParamValue("orderMonth"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNo"));
			System.out.println(orderMonth);
			if (!"".equals(orderMonth)) {
				String[] array = orderMonth.split("-");
				year = array[0];
				month = array[1];
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
			
			PageResult<Map<String, Object>> ps = dao.monthOrderCheck_SAL_QueryList(year, month, areaId, dealerCode, orderNO, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月的订单审核：页面初始化(事业部)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	public void monthOrderCheck_SAL_Detail(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			Map<String,Object> orderInfo = dao.getOrderInfo(orderId);
			List<Map<String,Object>> dList = dao.getDetailOrderList(orderId);
			act.setOutData("orderInfo", orderInfo);
			act.setOutData("dList", dList);
			act.setForword(monthOrderCheck_SAL_Detail_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月的订单审核：页面初始化(事业部)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 月度订单审核提交（销售部）
	 * */
	public void monthOrderCheck_SAL_submit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String checkStatus = CommonUtils.checkNull(request.getParamValue("checkStatus"));
			String checkDesc = CommonUtils.checkNull(request.getParamValue("checkDesc"));
			String[] detailId = request.getParamValues("detailId"); 
			String[] checkAmount = request.getParamValues("checkAmount"); 

			TtVsOrderPO tempPO = new TtVsOrderPO();
			tempPO.setOrderId(Long.parseLong(orderId));
			
			TtVsOrderPO valuePO = new TtVsOrderPO();
			if ("1".equals(checkStatus)) {
				valuePO.setOrderStatus(Constant.ORDER_STATUS_05);
			}else{
				valuePO.setOrderStatus(Constant.ORDER_STATUS_04);
			}
			valuePO.setUpdateBy(logonUser.getUserId());
			valuePO.setUpdateDate(new Date());
			dao.update(tempPO, valuePO);
			
			for (int i = 0; i < detailId.length; i++) {
				TtVsOrderDetailPO tempDetailPO = new TtVsOrderDetailPO();
				tempDetailPO.setDetailId(Long.parseLong(detailId[i]));
				TtVsOrderDetailPO valueDetailPO = new TtVsOrderDetailPO();
				valueDetailPO.setCheckAmount(Integer.parseInt(checkAmount[i]));
				valueDetailPO.setUpdateBy(logonUser.getUserId());
				valueDetailPO.setUpdateDate(new Date());
				dao.update(tempDetailPO, valueDetailPO);
			}
			
			TtVsOrderCheckPO checkPO = new TtVsOrderCheckPO();
			checkPO.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
			checkPO.setOrderId(Long.parseLong(orderId));
			checkPO.setCheckOrgId(logonUser.getOrgId());
			checkPO.setCheckPositionId(logonUser.getPoseId());
			checkPO.setCheckUserId(logonUser.getUserId());
			checkPO.setCheckDate(new Date());
			if ("1".equals(checkStatus)) {
				checkPO.setCheckStatus(Constant.CHECK_STATUS_03);
			}else{
				checkPO.setCheckStatus(Constant.CHECK_STATUS_04);
			}
			if (!"".equals(checkDesc)) {
				checkPO.setCheckDesc(checkDesc.trim());
			}
			checkPO.setCheckType(Constant.ORDER_CHECK_TYPE_02);
			checkPO.setCreateBy(logonUser.getUserId());
			checkPO.setCreateDate(new Date());
			dao.insert(checkPO);
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月的订单审核：页面初始化(事业部)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 月度订单批量审核提交（销售部）
	 * */
	public void monthOrderAllCheck(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String year = "";
			String month = "";
			String orderMonth = CommonUtils.checkNull(request.getParamValue("orderMonth"));
			if (!"".equals(orderMonth)) {
				String[] array = orderMonth.split("-");
				year = array[0];
				month = array[1];
			}
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			
			TtVsOrderPO tempPO = new TtVsOrderPO();
			tempPO.setOrderYear(new Integer(year));
			tempPO.setOrderMonth(new Integer(month));
			tempPO.setOrderType(Constant.ORDER_TYPE_01);
			tempPO.setOrderStatus(Constant.ORDER_STATUS_09);
			tempPO.setAreaId(Long.parseLong(areaId));
			List<PO> list = dao.select(tempPO);
			for(int i = 0; i < list.size(); i++){
				tempPO = (TtVsOrderPO)list.get(i);
				TtVsOrderPO coditionPO = new TtVsOrderPO();
				coditionPO.setOrderId(tempPO.getOrderId());
				TtVsOrderPO valuePO = new TtVsOrderPO();
				valuePO.setOrderStatus(Constant.ORDER_STATUS_05);
				valuePO.setUpdateBy(logonUser.getUserId());
				valuePO.setUpdateDate(new Date());
				dao.update(coditionPO, valuePO);//更新订单表
			
				
				deliveryDao.updateOrderDetailByOrderId(tempPO.getOrderId(), logonUser.getUserId());//更新订单明细表
				
				TtVsOrderCheckPO checkPO = new TtVsOrderCheckPO();
				checkPO.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
				checkPO.setOrderId(tempPO.getOrderId());
				checkPO.setCheckOrgId(logonUser.getOrgId());
				checkPO.setCheckPositionId(logonUser.getPoseId());
				checkPO.setCheckUserId(logonUser.getUserId());
				checkPO.setCheckDate(new Date());
				checkPO.setCheckStatus(Constant.CHECK_STATUS_03);
				checkPO.setCheckType(Constant.ORDER_CHECK_TYPE_02);
				checkPO.setCreateBy(logonUser.getUserId());
				checkPO.setCreateDate(new Date());
				dao.insert(checkPO);//新增订单审核记录
			}
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月的订单审核：页面初始化(事业部)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
