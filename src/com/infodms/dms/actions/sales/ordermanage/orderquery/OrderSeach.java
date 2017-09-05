package com.infodms.dms.actions.sales.ordermanage.orderquery;

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
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderSeachDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtVsDlvryDtlPO;
import com.infodms.dms.po.TtVsDlvryPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsReqCheckPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class OrderSeach {
	private Logger logger = Logger.getLogger(OrderSeach.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final OrderSeachDao dao = OrderSeachDao.getInstance();

	private final String ORDER_QUERY_URL = "/jsp/sales/ordermanage/orderquery/orderSeachQuery.jsp";// 批售订单查询页
	private final String ORDER_DETAIL_QUERY_URL = "/jsp/sales/ordermanage/orderquery/orderSeachDetail.jsp";// 批售订单明细页
	public void orderSeachInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);

			String dateStr = DateTimeUtil.parseDateToDate(new Date());
			act.setOutData("dateStr", dateStr);

			List<Map<String, Object>> areaBusList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			String duty = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();
			if (Constant.DUTY_TYPE_DEPT.toString().equals(duty)) {
				orgId = logonUser.getParentOrgId();
			}
			// 回显查询条件
			String dealerCode = request.getParamValue("dealerCode"); // 经销商CODE
			String areaId = request.getParamValue("areaId"); // 业务范围ID
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String orderTypeSel = request.getParamValue("orderTypeSel"); // 订单类型
			String orderNo = request.getParamValue("orderNo"); // 订单号码
			String reqStatus = request.getParamValue("reqStatus"); // 状态
			String orgCode = request.getParamValue("orgCode");// 区域代码
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			// 获得是否需要资金检查
			act.setOutData("areaBusList", areaBusList);
			act.setOutData("orgId", orgId);
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("areaId", areaId);
			act.setOutData("groupCode", groupCode);
			act.setOutData("orderTypeSel", orderTypeSel);
			act.setOutData("orderNo", orderNo);
			act.setOutData("reqStatus", reqStatus);
			act.setOutData("orgCode", orgCode);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setRedirect(ORDER_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 订单资源审核查询
	 */
	public void orderSeachQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String areaId = request.getParamValue("areaId"); // 业务范围ID
			String area = request.getParamValue("area"); // 业务范围IDS
			String dealerCode = request.getParamValue("dealerCode"); // 经销商CODE
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String orderType = request.getParamValue("orderTypeSel"); // 订单类型
			String orderNo = request.getParamValue("orderNo"); // 订单号码
			String reqStatus = request.getParamValue("reqStatus"); // 状态
			// String orgCode = request.getParamValue("orgCode");// 区域代码
			String orgIdN = request.getParamValue("orgId");// 区域代码
			String beginTime = request.getParamValue("startDate");// 开始日期
			String endTime = request.getParamValue("endDate");// 结束日期
			String dutyType=logonUser.getDutyType(logonUser.getDealerId());
			//Long companyId2 = GetOemcompanyId.getOemCompanyId(logonUser);
			//Long companyId1=logonUser.getCompanyId();
			Long companyId;
			if(Constant.DUTY_TYPE_DEALER.toString().equals(dutyType)){
				companyId=new Long(logonUser.getOemCompanyId());
			}else{
				companyId=logonUser.getCompanyId();
			}
			
			String orgId = "";

			String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize"));
			orgId=logonUser.getOrgId().toString();
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)){
				orgId="1";
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("areaId", areaId);
			map.put("area", area);
			map.put("groupCode", groupCode);
			map.put("dealerCode", dealerCode);
			map.put("dealerId", logonUser.getDealerId());
			map.put("orderType", orderType);
			map.put("orderNo", orderNo);
			map.put("companyId", companyId.toString());
			map.put("reqStatus", reqStatus);
			// map.put("orgCode", orgCode);
			map.put("dutyType", dutyType);
			map.put("orgId", orgId);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("operateType", "2");
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getOrderSeachQuery(map, curPage, Integer.parseInt(pageSize));
			act.setOutData("ps", ps);
			act.setOutData("orderType", orderType);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订单资源审核明细
	 */
	public void orderSeachDetailQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {

			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			/*
			 * String reqURL = atx.getRequest().getContextPath();
			 * if("/CVS-SALES".equals(reqURL.toUpperCase())){
			 * act.setOutData("returnValue", 1); }else{
			 * act.setOutData("returnValue", 2); }
			 */

			String paraSys = CommonDAO.getPara(Constant.CHANA_SYS.toString());

			if (Constant.COMPANY_CODE_JC.equals(paraSys.toUpperCase())) {
				act.setOutData("returnValue", 2);
			} else if (Constant.COMPANY_CODE_CVS.equals(paraSys.toUpperCase())) {
				act.setOutData("returnValue", 1);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！");
			}

			String par = CommonDAO.getPara(Constant.CHG_SINGLE_PRICE.toString());

			act.setOutData("par", par);

			String parReserve = CommonDAO.getPara(Constant.IS_ALLOW_RESERVE_MORE.toString());

			act.setOutData("parReserve", parReserve);

			String orderId = request.getParamValue("orderId"); // 订单ID
			String reqId = request.getParamValue("reqId"); // 发车申请ID
			String orderYearWeek = request.getParamValue("orderYearWeek"); // 订单年周
			String dealerCode = request.getParamValue("dealerCode"); // 经销商CODE
			String areaId = request.getParamValue("areaId"); // 业务范围ID
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String orderType = request.getParamValue("orderType"); // 订单类型
			String orderTypeSel = request.getParamValue("orderTypeSel");
			String orderNo = request.getParamValue("orderNo"); // 订单号码
			String reqStatus = request.getParamValue("reqStatus"); // 状态
			String orgCode = request.getParamValue("orgCode");// 区域代码
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			String historyCount = request.getParamValue("historyCount");
		
			Map<String, Object> map = dao.getOrderInfoByReqId(reqId);
			String  warseId= null;
			if(map.get("WAREHOUSE_ID")!=null){
				warseId=	map.get("WAREHOUSE_ID").toString();
			}
			/*String areaGet = ((BigDecimal) map.get("AREA_ID")).toString();
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			areaPO.setAreaId(new Long(areaGet));
			List<PO> areaList = dao.select(areaPO);
			Long erpCode = null;
			if (areaList.size() != 0) {
				areaPO = (TmBusinessAreaPO) areaList.get(0);
				erpCode = areaPO.getErpCode();
			}*/

			List<Map<String, Object>> list1 = dao.getorderResourceReserveDetailList(warseId,reqId, orderType, logonUser.getCompanyId().toString());
			List<Map<String, Object>> list2 = dao.getReqCheckList(reqId);
			//List<Map<String, Object>> wareHouseList = dao.getWareHouseList(logonUser.getCompanyId().toString(), areaGet);// 仓库列表
			TmBusinessParaPO para = dao.getTmBusinessParaPO(Constant.RESOURCE_RESERVE_CHECK_GENERAL_ORDER_PARA, logonUser.getCompanyId());
			// 获得订单启票最大折扣点
			TmBusinessParaPO para2 = dao.getTmBusinessParaPO(Constant.ORDER_REPORT_MAX_DISCOUNT_RATE_PARA, logonUser.getCompanyId());
			String ratePara = para2.getParaValue();
			//根据orderid查询priceId价格类型
			String priceId="";
			//TtVsDlvryReqPO tvp=new TtVsDlvryReqPO();
			//tvp.setOrderId(new Long(orderId));
			//tvp=(TtVsDlvryReqPO) dao.select(tvp).get(0);
			TtVsOrderPO tvo=new TtVsOrderPO();
			tvo.setOrderId(new Long(orderId));
			tvo=(TtVsOrderPO) dao.select(tvo).get(0);
			TmDealerPO tm=new TmDealerPO();
			tm.setDealerId(tvo.getOrderOrgId());
			tm=(TmDealerPO) dao.select(tm).get(0);
			priceId=tm.getPersonCharge();
			OrderReportDao dao1=OrderReportDao.getInstance();
			BigDecimal rebateAmount = dao1.getRebateAccount(map.get("BILLING_ORG_ID").toString());//冻结资金
			Map<String, Object> mapLinkInfo=dao1.getAddressInfo_("", orderId);
			act.setOutData("mapLinkInfo", mapLinkInfo);
			act.setOutData("rebateEnableAmount", rebateAmount);
			act.setOutData("rebateAmount", rebateAmount);
			act.setOutData("priceId", priceId);
			act.setOutData("map", map);
			act.setOutData("orderId", orderId);
			act.setOutData("reqId", reqId);
			act.setOutData("list1", list1);
			act.setOutData("list2", list2);
			act.setOutData("orderYearWeek", orderYearWeek);
			act.setOutData("dealerCode", dealerCode);
			act.setOutData("areaId", areaId);
			act.setOutData("groupCode", groupCode);
			act.setOutData("orderType", orderType);
			act.setOutData("orderTypeSel", orderTypeSel);
			act.setOutData("orderNo", orderNo);
			act.setOutData("reqStatus", reqStatus);
			act.setOutData("orgCode", orgCode);
			//act.setOutData("wareHouseList", wareHouseList);
			act.setOutData("checkGeneral", para != null ? para.getParaValue() : "0");
			act.setOutData("ratePara", ratePara);
			//act.setOutData("erpCode", erpCode);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setOutData("historyCount", historyCount);
			act.setOutData("sessionId", act.getSession().getId());
			act.setForword(ORDER_DETAIL_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
}
