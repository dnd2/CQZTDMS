package com.infodms.dms.actions.sales.ordermanage.orderaudit;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.VerFlagDao;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsOrderResourceReservePO;
import com.infodms.dms.po.TtVsReqCheckPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.ReqLogUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import static com.infodms.dms.actions.sales.financemanage.AccountOpera.*;
/**
 * @Title: 集团客户代交车审核Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-13
 * @author 
 * @mail   
 * @version 1.0
 * @remark 
 */
public class FleetOrderCheck {
	public Logger logger = Logger.getLogger(FleetOrderCheck.class);   
	OrderAuditDao dao  = OrderAuditDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();
	private final String initUrl = "/jsp/sales/ordermanage/orderaudit/fleetOrderCheck.jsp";
	private final String detailUrl = "/jsp/sales/ordermanage/orderaudit/fleetOrderDetailCheck.jsp";
	/**
	 * 集团客户代交车审核页面初始化
	 */
	public void fleetOrderCheckInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
//			Calendar c = Calendar.getInstance();
//			String year = Integer.toString(c.get(Calendar.YEAR));
			//String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
			List yearList = dao.getDateYearList();
			List weekList = dao.getDateWeekList();
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new java.util.Date(), oemCompanyId);
			act.setOutData("yearList", yearList);
			act.setOutData("weekList", weekList);
			act.setOutData("week", dateSet != null ? dateSet.getSetWeek() : "");
			act.setOutData("year", dateSet != null ? dateSet.getSetYear() : "");
//			act.setOutData("year", year);
			//act.setOutData("week", week);
		    act.setOutData("areaBusList", areaBusList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户代交车审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户代交车审核查询
	 */
	public void fleetOrderCheckQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String startYear =request.getParamValue("startYear");			//订单起始年
			String endYear =request.getParamValue("endYear");				//订单结束年
			String startWeek = request.getParamValue("startWeek");			//订单起始周
			String endWeek = request.getParamValue("endWeek");				//订单结束周
			String orderType = request.getParamValue("orderType");			//订单类型
			String orderNo =request.getParamValue("orderNo");			    //订单号码
			String areaId = request.getParamValue("areaId");				//业务范围
			String groupCode = request.getParamValue("groupCode");			//物料组CODE
			String dealerCode =request.getParamValue("dealerCode");			//经销商CODE
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			Long companyId = logonUser.getCompanyId();
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());
			
			String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			
			PageResult<Map<String, Object>> ps = dao.getFleetOrderCheckList(orgId, startYear, endYear, startWeek, endWeek, areaId, groupCode, dealerCode, orderType, orderNo, companyId, areaIds, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setOutData("orderType", orderType);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户代交车审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户代交车审核明细页面查询
	 */
	public void fleetOrderDetailCheckQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");		//申请ID
			String ver = request.getParamValue("ver");
			Map<String, Object> map = dao.getFleetOrderInfo(reqId);
			List<Map<String,Object>> list = dao.getFleetOrderDetailList(reqId);
			act.setOutData("reqId", reqId);
			act.setOutData("ver", ver);
			act.setOutData("map", map);
			act.setOutData("list", list);
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户代交车审核明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户订单取消 
	 */
	public void FleetOrderBackCheckQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reqVer = request.getParamValue("reqVer");// 发运申请VER
			String orderId = request.getParamValue("orderId"); // 订单ID
			String reqId = request.getParamValue("reqId"); // 发运申请ID
			String orderType = request.getParamValue("orderType"); // 订单类型
			String[] reqAmount = request.getParamValues("reqAmount");// 申请数量
			String[] detailId = request.getParamValues("detailId");// 发运申请明细ID
			String[] orderDetailId = request.getParamValues("orderDetailId");// 订单明细ID
			String isBingcai = CommonUtils.checkNull(request.getParamValue("amounttype")); //资金类型:1=兵财存货融资; 0=非兵财存货融资 
			if(isBingcai.equals("兵财存货融资")){
				isBingcai="1";
			}else{
				isBingcai="0";
			}
			String returnValue = "1";

			boolean verFlag = true;
			verFlag = VerFlagDao.getVerFlag("TT_VS_DLVRY_REQ", "REQ_ID", reqId, reqVer);
			if (verFlag) {
				// 发运申请更新
				TtVsDlvryReqPO tvdrp1 = new TtVsDlvryReqPO();
				tvdrp1.setReqId(new Long(reqId));
				TtVsDlvryReqPO tvdrp2 = new TtVsDlvryReqPO();
				tvdrp2.setReqStatus(Constant.ORDER_REQ_STATUS_07);// 已取消
				tvdrp2.setVer(new Integer(Integer.parseInt(reqVer) + 1));
				tvdrp2.setUpdateBy(logonUser.getUserId());
				tvdrp2.setUpdateDate(new Date(System.currentTimeMillis()));
				dao.update(tvdrp1, tvdrp2);

				// 释放保留资源
				for (int i = 0; i < detailId.length; i++) {
					if (detailId[i] != null && !detailId.equals("")) {
						TtVsOrderResourceReservePO tvorrp1 = new TtVsOrderResourceReservePO();
						tvorrp1.setReqDetailId(new Long(detailId[i]));
						TtVsOrderResourceReservePO tvorrp2 = new TtVsOrderResourceReservePO();
						tvorrp2.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_02);// 已取消
						dao.update(tvorrp1, tvorrp2);

						// 常规订单更新订单明细
						if (Integer.parseInt(orderType) == Constant.ORDER_TYPE_01.intValue()) {
							TtVsOrderDetailPO tvodp = reportDao.getTtSalesOrderDetail(orderDetailId[i]);

							TtVsOrderDetailPO tvodp1 = new TtVsOrderDetailPO();
							tvodp1.setDetailId(tvodp.getDetailId());
							TtVsOrderDetailPO tvodp2 = new TtVsOrderDetailPO();
							tvodp2.setCallAmount(tvodp.getCallAmount().intValue()- Integer.parseInt(reqAmount[i]));
							tvodp2.setUpdateBy(logonUser.getUserId());
							tvodp2.setUpdateDate(new Date(System.currentTimeMillis()));
							
							dao.update(tvodp1, tvodp2) ;
						}
					}
				}

				// 非常规订单更新订单
				if (Integer.parseInt(orderType) != Constant.ORDER_TYPE_01.intValue()) {
					TtVsOrderPO tvop1 = new TtVsOrderPO();
					tvop1.setOrderId(new Long(orderId));
					TtVsOrderPO tvop2 = new TtVsOrderPO();
					tvop2.setOrderStatus(Constant.ORDER_STATUS_06);// 已取消
					tvop2.setUpdateBy(logonUser.getUserId());
					tvop2.setUpdateDate(new Date(System.currentTimeMillis()));
					dao.update(tvop1, tvop2);
				}

				// 释放资金
				if (!"1".equals(isBingcai)) {
					dmsReleasePrice(reqId, logonUser.getUserId().toString());
				}
				/*// 释放资金
				if (!"1".equals(isBingcai)) {
					AccountBalanceDetailDao dao = AccountBalanceDetailDao.getInstance();
					dao.releaseAllFreezeAmountByReqId(reqId, logonUser.getUserId().toString());
				}*/
				
				//向发运申请操作日志表写入日志信息
				ReqLogUtil.creatReqLog(Long.parseLong(reqId), Constant.REQ_LOG_TYPE_09, logonUser.getUserId());
				//同步结算中心下级经销商订单
				if (Integer.parseInt(orderType) != Constant.ORDER_TYPE_01.intValue()) {
					AccountBalanceDetailDao.getInstance().syncOrderToDFS(Long.valueOf(orderId), true, logonUser.getUserId(), true);
				} else {
					AccountBalanceDetailDao.getInstance().syncReqToDFS(Long.parseLong(reqId), true);
				}
			} else {
				returnValue = "2";
			}
			act.setOutData("returnValue", returnValue);
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SAVE_FAILURE_CODE,"大客户审核驳回！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商地址查询
	 */
	public void dealerAddressQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerCode = request.getParamValue("dealerCode");		//经销商ID
			List<Map<String,Object>> list = dao.getdealerAddress(dealerCode);
			act.setOutData("list", list);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户代交车审核明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 集团客户代交车审核保存
	 */
	public void fleetOrderCheckSave(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String reqId = request.getParamValue("reqId");
			String ver = request.getParamValue("ver");
			String deliveryType = request.getParamValue("deliveryType");
			String addressId = request.getParamValue("addressId");
			String dealerCode = request.getParamValue("dealerCode");
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));
			TmDealerPO tempPO = new TmDealerPO();
			tempPO.setDealerCode(dealerCode.trim());
		
			Long dealerId = new Long(0);
			List dList = dao.select(tempPO);
			if (null != dList && dList.size() > 0) {
				TmDealerPO valuePO = (TmDealerPO)dList.get(0);
				dealerId = valuePO.getDealerId();
			}
			
			boolean verFlag = VerFlagDao.getVerFlag("TT_VS_DLVRY_REQ", "REQ_ID", reqId, ver);
			if(verFlag){
				TtVsDlvryReqPO tvdrp1 =new TtVsDlvryReqPO();
				TtVsDlvryReqPO tvdrp2 =new TtVsDlvryReqPO();
				tvdrp1.setReqId(Long.parseLong(reqId));
				tvdrp2.setUpdateDate(new Date(System.currentTimeMillis()));
				tvdrp2.setUpdateBy(logonUser.getUserId());
				tvdrp2.setDeliveryType(new Integer(deliveryType));
				tvdrp2.setReceiver(dealerId);
				if(tvdrp2.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_02.intValue()){
					tvdrp2.setAddressId(Long.parseLong(addressId));
					tvdrp2.setLinkMan(linkMan);
					tvdrp2.setTel(tel);
				}
				
				if(CommonUtils.getNowSys(GetOemcompanyId.getOemCompanyId(logonUser)) == 0) {
					tvdrp2.setReqStatus(Constant.ORDER_REQ_STATUS_DJCQR);
				} else {
					tvdrp2.setReqStatus(Constant.ORDER_REQ_STATUS_01);
				}
				
				dao.update(tvdrp1,tvdrp2);
				
				TtVsReqCheckPO tvrcp = new TtVsReqCheckPO();
				tvrcp.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
				tvrcp.setReqId(Long.parseLong(reqId));
				tvrcp.setCheckOrgId(logonUser.getOrgId());
				tvrcp.setCheckPositionId(logonUser.getPoseId());
				tvrcp.setCheckUserId(logonUser.getUserId());
				tvrcp.setCheckStatus(Constant.CHECK_STATUS_05);
				tvrcp.setCheckDate(new Date(System.currentTimeMillis())) ;
				tvrcp.setCreateBy(logonUser.getUserId());
				tvrcp.setCreateDate(new Date(System.currentTimeMillis()));
				dao.insert(tvrcp);
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户代交车审核保存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
