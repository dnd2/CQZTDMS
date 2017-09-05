package com.infodms.dms.actions.sales.ordermanage.orderreport;

import java.math.BigDecimal;
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
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.LowOrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetOrderNOUtil;
import com.infodms.dms.util.businessUtil.ReqLogUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

public class LowMonthGeneralOrderCall {
	public Logger logger = Logger.getLogger(LowMonthGeneralOrderCall.class);
	OrderDeliveryDao dao = OrderDeliveryDao.getInstance();
	private final AccountBalanceDetailDao accoutDao = AccountBalanceDetailDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private static final String INIT_URL = "/jsp/sales/ordermanage/orderreport/lowMonthGeneralOrderCallPre.jsp" ;
	private static final String LIST_URL = "/jsp/sales/ordermanage/orderreport/lowMonthGeneralOrderCall.jsp";
	
	/*---------------------------------------------------------- action方法 ----------------------------------------------------------*/
	
	public void LowMonthCallInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> dateList = dao.getMonthMakeDateList(oemCompanyId);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString(), Constant.DEALER_LEVEL_02.toString());
			
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			
			act.setForword(INIT_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "下级经销商月度常规订单启票初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void applyQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderYearMonth = request.getParamValue("orderYearMonth");
			String[] array = orderYearMonth.split("-");
			String orderYear = array[0];
			String orderMonth = array[1];
			String areaId = request.getParamValue("areaId"); // 业务范围
			String dealerId = request.getParamValue("dealerId"); // 经销商ID
			String orderNo = request.getParamValue("orderNo"); // 订单号码
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			
			String firstDlr = LowOrderDeliveryDao.getFirstDealer(dealerId) ;
			
			if(CommonUtils.isNullString(firstDlr)) {
				throw new BizException("该经销商不存在一级经销商！") ;
			}
			
			List<Map<String, Object>> list = dao.getMonthGeneralOrderCallList(orderYear, orderMonth, areaId, dealerId, dealerId, orderNo, oemCompanyId);
			List<Map<String, Object>> accountlist = dao.getDealerAccount(firstDlr);
			
			act.setOutData("firstDlr", firstDlr) ;
			act.setOutData("list", list);
			act.setOutData("accountlist", accountlist);
			act.setOutData("dealerId", dealerId);
			
			act.setForword(LIST_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "下级经销商月度常规订单启票查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void applySubmit() {

		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long userId = logonUser.getUserId();

		try {
			String[] detailId = request.getParamValues("detailId");
			String[] orderId = request.getParamValues("orderId");
			String[] areaId = request.getParamValues("areaId");
			String[] materialId = request.getParamValues("materialId");
			// String[] callAmount = request.getParamValues("callAmount");
			String[] applyAmount = request.getParamValues("applyAmount");

			String typeId = request.getParamValue("typeId"); // 账户类型ID
			String transportType = request.getParamValue("transportType"); // 发运方式
			String addressId = request.getParamValue("addressId"); // 发运地址ID
			String fleetId = request.getParamValue("fleetId"); // 大客户ID
			String address = request.getParamValue("address"); // 大客户地址
			String receiver = CommonUtils.checkNull(request.getParamValue("receiver"));// 收货方
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));// 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));// 联系电话
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover")); // 是否代交车
			String reqTotalAmount = request.getParamValue("reqTotalAmount"); // 发运申请总量
			String dealerId = request.getParamValue("dealerId"); // 经销商id
			String firstDlr = CommonUtils.checkNull(request.getParamValue("firstDlr")) ; // 一级经销商id
			String reqRemark = CommonUtils.checkNull(request.getParamValue("orderRemark")).trim() ;
			
			String returnValue = "1" ;	// 错误信息标识

			/*TtVsOrderDetailPO tsodpContion = new TtVsOrderDetailPO(); // 订单明细表
			TtVsOrderDetailPO tsodpValue = new TtVsOrderDetailPO();*/

			if (applyAmount != null && applyAmount.length != 0) {
				MonthGeneralOrderCall mgoc = new MonthGeneralOrderCall() ;
				String str = mgoc.getMatCode(detailId, applyAmount) ;
				
				if(!CommonUtils.isNullString(str)){
					act.setOutData("metStr", str);
					returnValue = "2" ;
				} else {
					TtVsDlvryReqPO tvdrpo = new TtVsDlvryReqPO(); // 发运申请表
					Long reqId = Long.parseLong(SequenceManager.getSequence(""));
					tvdrpo.setReqId(reqId);
					tvdrpo.setAreaId(new Long(areaId[0]));
					tvdrpo.setOrderId(new Long(orderId[0]));
					tvdrpo.setFundType(new Long(typeId));
					tvdrpo.setReqRemark(reqRemark) ;
					if (null != isCover && "1".equals(isCover)) {
						tvdrpo.setIsFleet(1);
						tvdrpo.setFleetId(Long.parseLong(fleetId));
						tvdrpo.setFleetAddress(address);
					} else {
						tvdrpo.setIsFleet(0);
						tvdrpo.setDeliveryType(Integer.parseInt(transportType));
						if (Integer.parseInt(transportType) == Constant.TRANSPORT_TYPE_02) {
							tvdrpo.setAddressId(Long.parseLong(addressId));
							tvdrpo.setReceiver(new Long(receiver));
							tvdrpo.setLinkMan(linkMan);
							tvdrpo.setTel(tel);
						} else {
							tvdrpo.setReceiver(new Long(dealerId));
						}
					}
					
					tvdrpo.setOrderDealerId(Long.parseLong(dealerId)) ;
					tvdrpo.setBilDealerId(Long.parseLong(firstDlr)) ;
					tvdrpo.setReqStatus(Constant.ORDER_REQ_STATUS_YSH);
					tvdrpo.setReqDate(new Date());
					tvdrpo.setReqTotalAmount(new Integer(reqTotalAmount));
					tvdrpo.setReqExecStatus(Constant.REQ_EXEC_STATUS_01);
					tvdrpo.setVer(new Integer(0));
					tvdrpo.setCreateBy(userId);
					tvdrpo.setCreateDate(new Date());
					
					// 获得业务范围编码
					Map<String, String> codeMap = GetOrderNOUtil.getAreaShortcode(areaId[0]);
					String areaCode = codeMap.get("AREA_SHORTCODE");
					// 获得经销商代码
					TmDealerPO tmDealerPO = new TmDealerPO();
					tmDealerPO.setDealerId(new Long(dealerId));
					List<PO> dealerList = dao.select(tmDealerPO);
					String dealerCode = ((TmDealerPO) dealerList.get(0)).getDealerCode();
					// 获得发运订单编号
					String dlvryReqNO = GetOrderNOUtil.getDlvryReqNO(areaCode, "D", dealerCode);
					tvdrpo.setDlvryReqNo(dlvryReqNO);
					tvdrpo.setCallLeavel(Constant.DEALER_LEVEL_02) ;
	
					dao.insert(tvdrpo);// 插入发运申请
					//向发运申请操作日志表写入日志信息
					ReqLogUtil.creatReqLog(reqId, Constant.REQ_LOG_TYPE_YTB, logonUser.getUserId());
					for (int i = 0; i < applyAmount.length; i++) {
						if (applyAmount[i] != null && !"".equals(applyAmount[i]) && Integer.parseInt(applyAmount[i]) > 0) {
							TtVsDlvryReqDtlPO tvdrdpo = new TtVsDlvryReqDtlPO(); // 发运申请明细表
							tvdrdpo.setDetailId(Long.parseLong(SequenceManager.getSequence("")));// 设置发运申请明细主键
							tvdrdpo.setReqId(tvdrpo.getReqId());
							tvdrdpo.setOrderDetailId(Long.parseLong(detailId[i]));
							tvdrdpo.setMaterialId(Long.parseLong(materialId[i]));
							tvdrdpo.setReqAmount(Integer.parseInt(applyAmount[i]));
							tvdrdpo.setVer(0);
							tvdrdpo.setCreateBy(userId);
							tvdrdpo.setCreateDate(new Date());
							
							dao.insert(tvdrdpo); // 插入发运申请明细表
	
							// tsodpContion.setDetailId(Long.parseLong(detailId[i]));
							// tsodpValue.setCallAmount(Integer.parseInt(callAmount[i]) + Integer.parseInt(applyAmount[i]));
							// tsodpValue.setUpdateBy(userId);
							// tsodpValue.setUpdateDate(new Date());
							// dao.update(tsodpContion, tsodpValue);// 更新订单明细表
						}
					}
				}
			}
			
			act.setOutData("returnValue", returnValue);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "下级经销商月度常规订单启票");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*---------------------------------------------------------- public方法 ----------------------------------------------------------*/
	
	public void getGeneralOrderNO() {
		String pageDate = CommonUtils.checkNull(request.getParamValue("orderYearMonth")) ;
		String pageArea = CommonUtils.checkNull(request.getParamValue("area")) ;
		
		String pageYear = pageDate.split("-")[0] ;
		String pageMonth = pageDate.split("-")[1] ;
		String pageAreaId = pageArea.split("\\|")[0] ;
		String pageDealerId = pageArea.split("\\|")[1] ;
		
		String flag = "1" ;
		
		Map<String, String> map = new HashMap<String, String>() ;
		
		map.put("areaId", pageAreaId) ;
		map.put("dealerId", pageDealerId) ;
		map.put("year", pageYear) ;
		map.put("month", pageMonth) ;
		map.put("orderType", Constant.ORDER_TYPE_01.toString()) ;
		map.put("orderStatus", Constant.ORDER_STATUS_05.toString()) ;
		
		List<Map<String, Object>> orderList = dao.getOrderNOByOrderUnit(map) ;
		
		if(CommonUtils.isNullList(orderList)) {
			flag = "0" ;
		} 
		
		act.setOutData("orderList", orderList) ;
		act.setOutData("flag", flag) ;
	}
}
