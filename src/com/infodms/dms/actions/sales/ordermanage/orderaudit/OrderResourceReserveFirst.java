/**
 * 
 */
package com.infodms.dms.actions.sales.ordermanage.orderaudit;

import static com.infodms.dms.actions.sales.financemanage.AccountOpera.dmsFreezePrice;
import static com.infodms.dms.actions.sales.financemanage.AccountOpera.dmsReleasePrice;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderreport.SpecialNeedConfirm;
import com.infodms.dms.actions.sales.planmanage.QuotaAssign.ResourceReserveQuery;
import com.infodms.dms.actions.util.LockControl;
import com.infodms.dms.base.OrderBase;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.base.OrderDAO;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.common.VerFlagDao;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.SpecialNeedDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.CuxErpDeliverPO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TmVsAddressPO;
import com.infodms.dms.po.TmWarehousePO;
import com.infodms.dms.po.TtProcInterfaceMonitorPO;
import com.infodms.dms.po.TtReqLogPO;
import com.infodms.dms.po.TtVsAccountTypePO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsOrderResourceReservePO;
import com.infodms.dms.po.TtVsReqCheckPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.businessUtil.ReqLogUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author Administrator
 * 
 */
public class OrderResourceReserveFirst {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final OrderAuditDao dao = OrderAuditDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();
	private final AccountBalanceDetailDao accoutDao = AccountBalanceDetailDao.getInstance();

	private final String ORDER_RESOURCE_RESERVE_QUERY_URL = "/jsp/sales/ordermanage/orderaudit/orderResourceReserveQueryFirst.jsp";// 订单资源审核查询页
	private final String ORDER_RESOURCE_RESERVE_DETAIL_QUERY_URL = "/jsp/sales/ordermanage/orderaudit/orderResourceReserveDetailFirst.jsp";// 订单资源审核明细页
	private final String ORDER_RESOURCE_RESERVE_PATCH_NO_QUERY_URL = "/jsp/sales/ordermanage/orderaudit/orderResourceReservePatchNoQueryFirst.jsp";// 订单资源审核批次号选择页
	private final String LOCK_INIT_URL="/jsp/sales/ordermanage/orderaudit/unFitNormalDetail.jsp";
	public void checkIsOper() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String billId=request.getParamValue("billId");
				String sessionId=act.getSession().getId();
				String userId=logonUser.getUserId().toString();
				String lockReturn=LockControl.checkData(billId, sessionId,userId);
				act.setOutData("flag", lockReturn);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "检测资源是否占用查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void orderResourceReserveQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new java.util.Date(), oemCompanyId);
			String dateStr = DateTimeUtil.parseDateToDate(new Date());
			act.setOutData("dateStr", dateStr);

			List<Map<String, Object>> dateList = dao.getNastyDateList(oemCompanyId);
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
			TmBusinessParaPO para = dao.getTmBusinessParaPO(Constant.URGENT_ORDER_ACCOUNT_CHECK_PARA, oemCompanyId);
			String isCheck = para.getParaValue();
			act.setOutData("dateList", dateList);
			act.setOutData("areaBusList", areaBusList);
			act.setOutData("isCheck", isCheck);
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
			act.setOutData("sessionId",act.getSession().getId());
			act.setOutData("userId",logonUser.getUserId());
			act.setForword(ORDER_RESOURCE_RESERVE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 订单资源初审查询
	 */
	public void orderResourceReserveQuery() {
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

			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String dutyType = logonUser.getDutyType();
			String orgId = "";

			String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize"));

			if (Constant.DUTY_TYPE_LARGEREGION.toString().equalsIgnoreCase(dutyType)) {
				orgId = logonUser.getOrgId().toString();
			} else {
				if (!CommonUtils.isNullString(orgIdN)) {
					orgId = orgIdN;
				}
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("areaId", areaId);
			map.put("area", area);
			map.put("groupCode", groupCode);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("orderNo", orderNo);
			map.put("companyId", companyId.toString());
			map.put("reqStatus", reqStatus);
			// map.put("orgCode", orgCode);
			map.put("dutyType", dutyType);
			map.put("orgId", orgId);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("operateType", "1");
			map.put("userId", logonUser.getUserId().toString());
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getOrderResourceReserveQuery(map, curPage, Integer.parseInt(pageSize));
			act.setOutData("ps", ps);
			act.setOutData("orderType", orderType);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void orderResourceReserveQueryLoad() {
		OutputStream os = null;
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String areaId = request.getParamValue("areaId"); // 业务范围ID
			String area = request.getParamValue("area"); // 业务范围IDS
			String dealerCode = request.getParamValue("dealerCode"); // 经销商CODE
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String orderType = request.getParamValue("orderTypeSel"); // 订单类型
			String orderNo = request.getParamValue("orderNo"); // 订单号码
			String reqStatus = request.getParamValue("reqStatus"); // 状态
			String orgCode = request.getParamValue("orgCode");// 区域代码
			String beginTime = request.getParamValue("startDate");// 开始日期
			String endTime = request.getParamValue("endDate");// 结束日期

			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String dutyType = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("areaId", areaId);
			map.put("area", area);
			map.put("groupCode", groupCode);
			map.put("dealerCode", dealerCode);
			map.put("orderType", orderType);
			map.put("orderNo", orderNo);
			map.put("companyId", companyId.toString());
			map.put("reqStatus", reqStatus);
			map.put("orgCode", orgCode);
			map.put("dutyType", dutyType);
			map.put("orgId", orgId);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			// PageResult<Map<String, Object>> ps =
			// dao.getOrderResourceReserveLoadQuery(map, curPage,999999);
			PageResult<Map<String, Object>> ps = dao.getOrderResourceReserveDetailDownLoad(map, curPage, 999999);
			ResponseWrapper response = act.getResponse();
			// 导出的文件名
			String fileName = "资源审核下载.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 定义一个集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			// 标题
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("开票单位代码");
			listTemp.add("开票单位名称");
			listTemp.add("采购单位名称");
			listTemp.add("订单号码");
			listTemp.add("发运申请单号");
			listTemp.add("提报日期");
			listTemp.add("订单类型");
			listTemp.add("状态");
			listTemp.add("申请数量");
			listTemp.add("已保留数量");
			listTemp.add("资金类型");
			listTemp.add("订单总价");
			listTemp.add("车型编码");
			listTemp.add("单价");
			list.add(listTemp);
			List<Map<String, Object>> rslist = ps.getRecords();
			if (rslist != null) {
				for (int i = 0; i < rslist.size(); i++) {
					map = rslist.get(i);
					List<Object> listValue = new LinkedList<Object>();
					listValue = new LinkedList<Object>();
					listValue.add(map.get("DEALER_CODE") != null ? map.get("DEALER_CODE") : "");
					listValue.add(map.get("DEALER_NAME") != null ? map.get("DEALER_NAME") : "");
					listValue.add(map.get("ORDER_DEALER_NAME") != null ? map.get("ORDER_DEALER_NAME") : "");
					listValue.add(map.get("ORDER_NO") != null ? map.get("ORDER_NO") : "");
					listValue.add(map.get("DLVRY_REQ_NO") != null ? map.get("DLVRY_REQ_NO") : "");
					listValue.add(map.get("RAISE_DATE") != null ? map.get("RAISE_DATE") : "");
					listValue.add(map.get("ORDER_TYPE") != null ? map.get("ORDER_TYPE") : "");
					listValue.add(map.get("REQ_STATUS") != null ? map.get("REQ_STATUS") : "");
					listValue.add(map.get("ORDER_AMOUNT") != null ? map.get("ORDER_AMOUNT") : "");
					listValue.add(map.get("RESERVE_AMOUNT") != null ? map.get("RESERVE_AMOUNT") : "");
					listValue.add(map.get("FUND_TYPE") != null ? map.get("FUND_TYPE") : "");
					listValue.add(map.get("REQ_TOTAL_PRICE") != null ? map.get("REQ_TOTAL_PRICE") : "");
					listValue.add(map.get("CO") != null ? map.get("CO") : "");
					listValue.add(map.get("CO") != null ? map.get("SINGLE_PRICE") : "");
					list.add(listValue);
				}
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "资源审核下载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 订单资源审核明细
	 */
	public void orderResourceReserveDetailQuery() {
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
			act.setForword(ORDER_RESOURCE_RESERVE_DETAIL_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 订单资源初步审核通过
	 */
	public void orderResourceReserveSave() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] materialId = request.getParamValues("materialId"); // 物料ID
			String[] reqAmount = request.getParamValues("reqAmount"); // 申请数量
			String[] reserveAmount = request.getParamValues("reserveAmount"); // 保留数量
			String[] warhouseAmount=request.getParamValues("stockAmount");
			String[] singlePrice = request.getParamValues("singlePrice"); // 单价
			String[] discountRate = request.getParamValues("discountRate"); // 折扣率
			String[] totalPrice = request.getParamValues("totalPrice"); // 金额
			String[] discountSPrice = request.getParamValues("discountSPrice"); // 折扣后单价
			String[] discountPrice = request.getParamValues("discountPrice"); // 折扣额
			String[] batchNo = request.getParamValues("batchNo"); // 批次号
			String reqVer = request.getParamValue("reqVer"); // 发运申请VER
			String[] ver = request.getParamValues("ver"); // 发运申请明细VER
			String orderId = request.getParamValue("orderId"); // 订单ID
			String reqId = request.getParamValue("reqId"); // 发运申请ID
			String[] detailId = request.getParamValues("detailId"); // 发运申请明细ID
			String[] orderDetailId = request.getParamValues("orderDetailId"); // 订单明细ID
			String orderType = request.getParamValue("orderType"); // 订单类型
			String warehouseId = request.getParamValue("warehouseId"); // 仓库ID
			String fundTypeId = request.getParamValue("fundType"); // 资源类型
			String priceId = request.getParamValue("priceId"); // 价格类型
			String otherPriceReason = request.getParamValue("otherPriceReason"); // 使用其他价格原因
			String dealerId = request.getParamValue("dealerId"); // 开票方id

			String checkRemark = request.getParamValue("checkRemark"); // 审核描述
			//String reqTotalPrice = request.getParamValue("reqTotalPrice"); // 总价(折扣后)
			String reqTotalPrice = request.getParamValue("payAmount"); // 总价(返利后)
			String discountTotalPrice = request.getParamValue("discountTotalPrice"); // 折扣总额
			String modifyFlag = request.getParamValue("modifyFlag"); // 是否修改标志
			String reserveTotalAmount = request.getParamValue("reserveTotalAmount"); // 保留数量合计
			String accountId = CommonUtils.checkNull(request.getParamValue("accountId")); // 资金账户
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));// 折扣账户
			String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType")); // 发运方式
			String orderRemark = CommonUtils.checkNull(request.getParamValue("orderRemark")); // 备注
			String rebateAmount=CommonUtils.checkNull(request.getParamValue("rebateAmount")); // 返利金额
			String[] lineRebateAmount = request.getParamValues("lineRebateAmountHid"); // 返利金额
			String[] lineRebateTotalPriceHid = request.getParamValues("lineRebateTotalPriceHid"); //返利后订单总价
			
			// String receiver =
			// CommonUtils.checkNull(request.getParamValue("receiver")); // 收货方
			// String linkMan =
			// CommonUtils.checkNull(request.getParamValue("linkMan")); // 联系人
			// String tel = CommonUtils.checkNull(request.getParamValue("tel"));
			// // 联系电话

			String isBingcai = CommonUtils.checkNull(request.getParamValue("isBingcai")); // 资金类型:1=兵财存货融资;
			// 0=非兵财存货融资
			String old_fund_type_id = CommonUtils.checkNull(request.getParamValue("old_fund_type_id")); // 原资金类型id

			String returnValue = "1";
			
			//判断发运申请的状态是否符合条件
			//判断发运申请的状态yinshunhui
			TtVsDlvryReqPO tvdrp=new TtVsDlvryReqPO();
			tvdrp.setReqId(new Long(reqId));
			 tvdrp= (TtVsDlvryReqPO) dao.select(tvdrp).get(0);
			if(!(tvdrp.getReqStatus().intValue()==Constant.ORDER_REQ_STATUS_01||tvdrp.getReqStatus().intValue()==Constant.ORDER_REQ_STATUS_05||tvdrp.getReqStatus().intValue()==Constant.ORDER_REQ_STATUS_CYBH)){
				act.setOutData("returnValue", 2);
				return;
			}
			
			boolean verFlag = true;
			verFlag = VerFlagDao.getVerFlag("TT_VS_DLVRY_REQ", "REQ_ID", reqId, reqVer);
			if (verFlag) {
				/*
				 * for (int m = 0; m < detailId.length; m++) { if (detailId[m] !=
				 * null && !detailId[m].equals("")) { boolean verFlagTmp =
				 * VerFlagDao.getVerFlag("TT_VS_DLVRY_REQ_DTL", "DETAIL_ID",
				 * detailId[m], ver[m]); if (verFlagTmp == false) { verFlag =
				 * false; } } }
				 */
				if (verFlag) {
					TtVsOrderPO orderPo = reportDao.getTtSalesOrder(orderId);// 订单po

					/*
					 * if(!"1".equals(isBingcai)){ // 资金校验(如果
					 * 资金类型:1=兵财存货融资，不做资金校验) Map<String, Object> accountMap =
					 * reportDao.getAvailableAmount(fundTypeId.split("\\|")[0],
					 * dealerId, reqId); BigDecimal availableAmount =
					 * (BigDecimal) accountMap.get("AVAILABLE_AMOUNT"); if
					 * (availableAmount.doubleValue() <
					 * Double.parseDouble(reqTotalPrice)) { returnValue = "3"; } }
					 */

					// 校验通过，冻结资金
					if (returnValue.equals("1")) {
						// 查看原资金类型是否是“兵财存货融资”(如果IS_USE_ORDER_ACCOUNT ==
						// 1，说明是"兵财存货融资")
						Map<String, String> oldFund = accoutDao.oldIsCaibing(old_fund_type_id);
						String old_isCaibing = String.valueOf(oldFund.get("IS_USE_ORDER_ACCOUNT"));

						/*
						 * // 资金释放 if (!"1".equals(old_isCaibing) &&
						 * "1".equals(isBingcai)) { }
						 */
						dmsReleasePrice(reqId.toString(), logonUser.getUserId().toString());
						// 冻结资金(如果 资金类型:1=兵财存货融资，不做资金同步)

						/*
						 * if (!"1".equals(old_isCaibing) &&
						 * "1".equals(isBingcai)) { AccountBalanceDetailDao dao =
						 * AccountBalanceDetailDao.getInstance();
						 * dao.releaseAllFreezeAmountByReqId(reqId,
						 * logonUser.getUserId().toString()); }
						 */

						/*
						 * // 冻结资金(如果 资金类型:1=兵财存货融资，不做资金同步) if
						 * (!accountId.equals("") && !"1".equals(isBingcai)) {
						 * accoutDao.syncAccountFreeze(reqId, accountId, new
						 * BigDecimal(reqTotalPrice),
						 * logonUser.getUserId().toString()); } if
						 * (!discountAccountId.equals("") &&
						 * !"1".equals(isBingcai)) {
						 * accoutDao.syncAccountFreeze(reqId, discountAccountId,
						 * new BigDecimal(discountTotalPrice),
						 * logonUser.getUserId().toString()); }
						 */

						// 更新订单表
						TtVsOrderPO tvop1 = new TtVsOrderPO();
						TtVsOrderPO tvop2 = new TtVsOrderPO();
						tvop1.setOrderId(Long.parseLong(orderId));
						tvop2.setVer(orderPo.getVer().intValue() + 1);
						tvop2.setFundTypeId(new Long(fundTypeId.split("\\|")[0]));
						// tvop2.setOrderRemark(orderRemark);
						tvop2.setOrderStatus(Constant.ORDER_COM_STATUS_05);
						tvop2.setUpdateBy(logonUser.getUserId());
						tvop2.setUpdateDate(new Date(System.currentTimeMillis()));
						dao.update(tvop1, tvop2);

						// 更新发运申请表
						TtVsDlvryReqPO tvdrp1 = new TtVsDlvryReqPO();
						TtVsDlvryReqPO tvdrq2 = new TtVsDlvryReqPO();
						tvdrp1.setReqId(new Long(reqId));

						TtVsDlvryReqPO t1 = (TtVsDlvryReqPO) dao.select(tvdrp1).get(0);
						tvdrq2.setFundType(new Long(fundTypeId.split("\\|")[0]));
						tvdrq2.setReqRemark(orderRemark);
						if (priceId != null && !priceId.equals("")) {
							tvdrq2.setPriceId(priceId);
						}
						tvdrq2.setOtherPriceReason(otherPriceReason);
						tvdrq2.setModifyFlag(new Integer(modifyFlag));
						tvdrq2.setWarehouseId(new Long(warehouseId));
						tvdrq2.setRebateAmount(Double.valueOf(rebateAmount));
						if (modifyFlag.equals("1")) {
							tvdrq2.setReqStatus(Constant.ORDER_REQ_STATUS_02);// 经销商待确认
						} else {
							//根据code设置查询typeid
							List<Map<String,Object>> accountList=CommonUtils.getNeedRemarkAcntId();
							for(int i=0;i<accountList.size();i++){
								if(fundTypeId.equals(accountList.get(0).get("TYPE_ID").toString())){
									tvdrq2.setReqRemark("留合格证!!!   "+orderRemark);
								}
							}
							tvdrq2.setReqStatus(Constant.ORDER_REQ_STATUS_FINAL);// 初审完成
							tvdrq2.setfAuditTime(new Date());
							tvdrq2.setAuditTime(new Date());

						}
						
						tvdrq2.setReqTotalPrice(new Double(reqTotalPrice));
						// tvdrq2.setDiscount(new Double(discountTotalPrice));
						tvdrq2.setReserveTotalAmount(new Integer(reserveTotalAmount));
						tvdrq2.setDeliveryType(new Integer(deliveryType));
						// 发运方式为自提时
//						if (tvdrq2.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_01.intValue()) {
//							if (Integer.parseInt(orderType) == Constant.ORDER_TYPE_01.intValue()) {
//								if (t1.getOrderDealerId() == null || t1.getOrderDealerId() == 0) {
//									tvdrq2.setReceiver(orderPo.getOrderOrgId());
//								} else {
//									tvdrq2.setReceiver(t1.getOrderDealerId());
//								}
//							} else {
//								tvdrq2.setReceiver(orderPo.getOrderOrgId());
//							}
//						}
						// 发运方式为发运时
						/*
						 * else { tvdrq2.setReceiver(new Long(receiver));
						 * tvdrq2.setLinkMan(linkMan); tvdrq2.setTel(tel); }
						 */
						tvdrq2.setVer(new Integer(Integer.parseInt(reqVer) + 1));
						tvdrq2.setUpdateBy(logonUser.getUserId());
						tvdrq2.setUpdateDate(new Date(System.currentTimeMillis()));
//						if (Integer.parseInt(orderType) == Constant.ORDER_TYPE_01.intValue()) {
							tvdrq2.setReqTotalAmount(new Integer(reserveTotalAmount));
//						}

						int c = dao.update(tvdrp1, tvdrq2);

						// orderPo = reportDao.getTtSalesOrder(orderId);
						// com.wzy.util.string.Out.syso("sada",java.lang.);
						ResourceReserveQuery rrq = new ResourceReserveQuery();
						Long logId = rrq.insLog(reqId, logonUser.getUserId());

						for (int i = 0; i < detailId.length; i++) {
							if (detailId[i] != null && !detailId[i].equals("")) {

								// 更新订单明细
								TtVsOrderDetailPO tvodp1 = new TtVsOrderDetailPO();
								TtVsOrderDetailPO tvodp2 = new TtVsOrderDetailPO();
								tvodp1.setDetailId(new Long(orderDetailId[i]));
								if (Integer.parseInt(orderType) == Constant.ORDER_TYPE_01.intValue()) {
									TtVsOrderDetailPO tvodp = reportDao.getTtSalesOrderDetail(orderDetailId[i]);
									tvodp2.setCallAmount(tvodp.getCallAmount() - new Integer(reqAmount[i]) + new Integer(reserveAmount[i]));
								} else {
									tvodp2.setCheckAmount(new Integer(reserveAmount[i]));
								}
								dao.update(tvodp1, tvodp2);

								// 更新发车申请明细

//								if (new BigDecimal(100).subtract(new BigDecimal(discountRate[i])).divide(new BigDecimal(100)).multiply(new BigDecimal(singlePrice[i])).compareTo(new BigDecimal(discountSPrice[i])) == -1) {
//									throw new RuntimeException("单价与折扣后单价比较出现异常，请检查后再提交！");
//								}

								TtVsDlvryReqDtlPO tvdrdp1 = new TtVsDlvryReqDtlPO();
								TtVsDlvryReqDtlPO tvdrdp2 = new TtVsDlvryReqDtlPO();
								tvdrdp1.setDetailId(new Long(detailId[i]));
								tvdrdp2.setSinglePrice(new Double(singlePrice[i]));
								tvdrdp2.setTotalPrice(new Double(totalPrice[i]));
								//tvdrdp2.setDiscountRate(new Float(discountRate[i]));
								//tvdrdp2.setDiscountSPrice(new Double(discountSPrice[i]));
								//tvdrdp2.setDiscountPrice(new Double(discountPrice[i]));
								tvdrdp2.setReserveAmount(new Integer(reserveAmount[i]));
								tvdrdp2.setWarhouseAmount(new Integer(warhouseAmount[i]));
								tvdrdp2.setUpdateBy(logonUser.getUserId());
								tvdrdp2.setUpdateDate(new Date(System.currentTimeMillis()));
								tvdrdp2.setVer(new Integer(Integer.parseInt(ver[i] == null || ver[i].equals("") ? "0" : ver[i]) + 1));
								tvdrdp2.setReqAmount(new Integer(reserveAmount[i]));
								tvdrdp2.setRebateAmount(new Double(lineRebateAmount[i]));
								tvdrdp2.setAfterRebatePrice(new Double(lineRebateTotalPriceHid[i]));
								dao.update(tvdrdp1, tvdrdp2);
								
								// 删除资源保留
								TtVsOrderResourceReservePO tvorrp = new TtVsOrderResourceReservePO();
								tvorrp.setReqDetailId(new Long(detailId[i]));
								dao.delete(tvorrp);
								// 新增资源保留
								//System.out.println("===========length=========="+batchNo.length+"====="+batchNo[0]);
								//if (batchNo[i] != null && !batchNo[i].equals("")) {
								//	String[] tempArray = batchNo[i].split("\\/");
									//for (int j = 0; j < tempArray.length; j++) {
									//	String tempStr = tempArray[j];
									//	String[] tempArray2 = tempStr.split("-");
									//	String patchNO = tempArray2[0];
									//	String count = tempArray2[1];
										tvorrp = new TtVsOrderResourceReservePO();
										tvorrp.setReserveId(Long.parseLong(SequenceManager.getSequence("")));
										tvorrp.setReqDetailId(new Long(detailId[i]));
										tvorrp.setMaterialId(new Long(materialId[i]));
										tvorrp.setBatchNo("1");
										//yinshunhui start 
										//tvorrp.setAmount(new Integer(count));
										tvorrp.setAmount(new Integer(reserveAmount[i]));
										//end
										tvorrp.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_01);
										tvorrp.setOemCompanyId(logonUser.getCompanyId());
										tvorrp.setWarehouseId(new Long(warehouseId));
										tvorrp.setReserveType(Constant.RESERVE_TYPE_01);
										tvorrp.setCreateBy(logonUser.getUserId());
										tvorrp.setCreateDate(new Date());
										dao.insert(tvorrp);
										rrq.insDtlLog(logId, detailId[i], "1", "0", "0", materialId[i], logonUser.getUserId());
								//}
								//}

							} else {

								// 新增订单明细
								TtVsOrderDetailPO tvodp = new TtVsOrderDetailPO();
								tvodp.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
								tvodp.setOrderId(new Long(orderId));
								tvodp.setMaterialId(new Long(materialId[i]));
								tvodp.setOrderAmount(new Integer(0));
								tvodp.setCallAmount(new Integer(0));
								tvodp.setCheckAmount(new Integer(reserveAmount[i]));
								tvodp.setSinglePrice(new Double(singlePrice[i]));
								tvodp.setTotalPrice(new Double(totalPrice[i]) );
								tvodp.setVer(new Integer(0));
								//tvodp.setDiscountRate(new Float(discountRate[i]));
								//tvodp.setDiscountSPrice(new Double(discountSPrice[i]));
								//tvodp.setDiscountPrice(new Double(discountPrice[i]));
								tvodp.setCreateBy(logonUser.getUserId());
								tvodp.setCreateDate(new Date(System.currentTimeMillis()));
								dao.insert(tvodp);

								// 新增发车申请明细
								TtVsDlvryReqDtlPO tvdrdp = new TtVsDlvryReqDtlPO();
								tvdrdp.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
								tvdrdp.setReqId(new Long(reqId));
								tvdrdp.setOrderDetailId(tvodp.getDetailId());
								tvdrdp.setMaterialId(new Long(materialId[i]));
								tvdrdp.setReqAmount(new Integer(0));
								tvdrdp.setVer(new Integer(0));
								tvdrdp.setSinglePrice(new Double(singlePrice[i]));
								tvdrdp.setTotalPrice(new Double(totalPrice[i]));
								//tvdrdp.setDiscountRate(new Float(discountRate[i]));
								//tvdrdp.setDiscountSPrice(new Double(discountSPrice[i]));
								//tvdrdp.setDiscountPrice(new Double(discountPrice[i]));
								tvdrdp.setReserveAmount(new Integer(reserveAmount[i]));
								tvdrdp.setCreateBy(logonUser.getUserId());
								tvdrdp.setCreateDate(new Date(System.currentTimeMillis()));
								dao.insert(tvdrdp);

								// 新增资源保留
								//if (batchNo[i] != null && !batchNo[i].equals("")) {
								//	String[] tempArray = batchNo[i].split("\\/");
								//	for (int j = 0; j < tempArray.length; j++) {
								//		String tempStr = tempArray[j];
								//		String[] tempArray2 = tempStr.split("-");
								//		String patchNO = tempArray2[0];
								//		String count = tempArray2[1];
										TtVsOrderResourceReservePO tvorrp = new TtVsOrderResourceReservePO();
										tvorrp.setReserveId(Long.parseLong(SequenceManager.getSequence("")));
										tvorrp.setReqDetailId(tvdrdp.getDetailId());
										tvorrp.setMaterialId(new Long(materialId[i]));
										tvorrp.setBatchNo("1");
										//yinshunhui start 
										//tvorrp.setAmount(new Integer(count));
										tvorrp.setAmount(new Integer(reserveAmount[i]));
										//end 
										tvorrp.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_01);
										tvorrp.setOemCompanyId(logonUser.getCompanyId());
										tvorrp.setWarehouseId(new Long(warehouseId));
										tvorrp.setReserveType(Constant.RESERVE_TYPE_01);
										dao.insert(tvorrp);

										rrq.insDtlLog(logId, tvdrdp.getDetailId().toString(), "1", "0", "0", materialId[i], logonUser.getUserId());
								//	}
								//}
							}
						}

						if (c > 0) {
							if (tvdrq2.getReqStatus() == Constant.ORDER_REQ_STATUS_03){

								List<Object> ins = new LinkedList<Object>();
								ins.add(new Long(reqId));
								//发车申请终审
								dao.callProcedure("CZDSPROD.P_ORDER_DLVRY_10008", ins, null);// 存储过程_将发运申请单插入到topup_dms
								//发车申请终审
								// 提车计划单10008
							}
						}
						// 向发运申请操作日志表写入日志信息
						ReqLogUtil.creatReqLog(new Long(reqId), Constant.REQ_LOG_TYPE_02, logonUser.getUserId());
//						// 新增发车申请审核记录
						TtVsReqCheckPO tvrcp = new TtVsReqCheckPO();
						tvrcp.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
						tvrcp.setReqId(new Long(reqId));
						tvrcp.setCheckOrgId(logonUser.getOrgId());
						tvrcp.setCheckPositionId(logonUser.getPoseId());
						tvrcp.setCheckUserId(logonUser.getUserId());
						tvrcp.setCheckDate(new Date(System.currentTimeMillis()));
						tvrcp.setCheckStatus(Constant.ORDER_REQ_STATUS_03);
						if (checkRemark != null && !"".equals(checkRemark)) {
							tvrcp.setCheckDesc(checkRemark);
						}
						tvrcp.setCreateBy(logonUser.getUserId());
						tvrcp.setCreateDate(new Date(System.currentTimeMillis()));
						dao.insert(tvrcp);
						
						
						
//						if (!accountId.equals("") && !"1".equals(isBingcai)) {
//							dmsFreezePrice(reqId, accountId, new BigDecimal(reqTotalPrice), logonUser.getUserId().toString());
//						}
//						if (!discountAccountId.equals("") && !"1".equals(isBingcai)) {
//							dmsFreezePrice(reqId, discountAccountId, new BigDecimal(discountTotalPrice), logonUser.getUserId().toString());
//						}

						if (Integer.parseInt(orderType) != Constant.ORDER_TYPE_01.intValue()) {
							AccountBalanceDetailDao.getInstance().syncOrderToDFS(Long.valueOf(orderId), true, logonUser.getUserId(), false);
						} else {
							AccountBalanceDetailDao.getInstance().syncReqToDFS(Long.parseLong(reqId), false);
						}

						/* 插入DMS-> ERP 接口表：CUX_ERP_DELIVER 数据 begin */
						//申请终审start
						String orderDealerId = CommonUtils.checkNull(request.getParamValue("orderDealerId")); // 订货方经销商ID
						
						String receiverDealerId = CommonUtils.checkNull(request.getParamValue("receiver")); // 收货方经销商ID
						String orderTypeName = CommonUtils.checkNull(request.getParamValue("orderTypeName")); // 订单类型
						finalCheckSave(orderDealerId, receiverDealerId, orderTypeName, warehouseId, reqId, checkRemark);
						//this.dmsSendToErp(orderDealerId, receiverDealerId, orderTypeName, warehouseId, reqId);
						/* 插入DMS-> ERP 接口表：CUX_ERP_DELIVER 数据 end */
						//putReqStatusToLog( reqId);
						//申请终审end
					}

				} else {
					returnValue = "2";
				}
				//**订单终审start**//
				OrderResourceReserve orderResouce=new OrderResourceReserve();
				orderResouce.orderResourceReserveSave();
				//**订单终审end**//
			} else {
				returnValue = "2";
			}
			act.setOutData("returnValue", returnValue);
		} catch (RuntimeException e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.PUTIN_FAILURE_CODE, e.getMessage());
			logger.error(logonUser, e1);
			act.setException(e);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SAVE_FAILURE_CODE, "订单资源审核保存");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	private void finalCheckSave(String orderDealerId,String receiverDealerId,String orderTypeName,String warehouseId,String reqId,String checkRemark){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
		
		//*新增发车申请终审记录start//
		TtVsReqCheckPO tvrcp1 = new TtVsReqCheckPO();
		tvrcp1.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
		tvrcp1.setReqId(new Long(reqId));
		tvrcp1.setCheckOrgId(logonUser.getOrgId());
		tvrcp1.setCheckPositionId(logonUser.getPoseId());
		tvrcp1.setCheckUserId(logonUser.getUserId());
		tvrcp1.setCheckDate(new Date(System.currentTimeMillis()));
		tvrcp1.setCheckStatus(Constant.ORDER_REQ_STATUS_FINAL);
		if (checkRemark != null && !"".equals(checkRemark)) {
			tvrcp1.setCheckDesc(checkRemark);
		}
		tvrcp1.setCreateBy(logonUser.getUserId());
		tvrcp1.setCreateDate(new Date(System.currentTimeMillis()));
		dao.insert(tvrcp1);
		// 新增发车申请终审记录 end //
		this.dmsSendToErp(orderDealerId, receiverDealerId, orderTypeName, warehouseId, reqId);
		/* 插入DMS-> ERP 接口表：CUX_ERP_DELIVER 数据 end */
		putReqStatusToLog( reqId);
		} catch (RuntimeException e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.PUTIN_FAILURE_CODE, e.getMessage());
			logger.error(logonUser, e1);
			act.setException(e);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SAVE_FAILURE_CODE, "订单资源审核保存");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 把发运申请的状态写入记录表中
	 * @param reqId
	 */
	private void putReqStatusToLog(String reqId){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//获取reqstatus
		TtVsDlvryReqPO tvdr =new TtVsDlvryReqPO();
		tvdr.setReqId(new Long(reqId));
		tvdr=(TtVsDlvryReqPO) dao.select(tvdr).get(0);
		String logId=SequenceManager.getSequence("");
		TtReqLogPO tvlp =new TtReqLogPO();
		tvlp.setLogId(new BigDecimal(logId));
		tvlp.setReqId(new BigDecimal(reqId));
		tvlp.setReqStatus(new BigDecimal(tvdr.getReqStatus()));
		tvlp.setCreateBy(new BigDecimal(logonUser.getUserId()));
		tvlp.setCreateDate(new Date());
		dao.insert(tvlp);
	}
	
	
	/**
	 * 经销商订单发运申请提报表（CUX_ERP_DELIVER）
	 * 
	 * @param orderDealerId :
	 *            订货方经销商ID （TT_VS_ORDER 表 ORDER_ORG_ID）
	 * @param receiverDealerId :
	 *            收货方经销商ID （TT_VS_DLVRY_REQ 表 RECEIVER）
	 * @param orderTypeName :
	 *            订单类型
	 * @param warehouseId :
	 *            仓库ID
	 * @param reqId :
	 *            发运申请ID
	 */
	private void dmsSendToErp(String orderDealerId, String receiverDealerId, String orderTypeName, String warehouseId, String reqId) {
		logger.info("====*********************DMS -> ERP 对接口表 经销商订单发运申请提报表【CUX_ERP_DELIVER】 下发开始 ************************************* ====");
		try {
			TmWarehousePO wpo = new TmWarehousePO();// 仓库PO
			wpo.setWarehouseId(new Long(warehouseId));
			wpo = (TmWarehousePO) dao.getPoObject(wpo);
			TmDealerPO dpo = dao.getDlrByID(new Long(orderDealerId));// 经销商PO
			TmVsAddressPO apo = dao.getDlrAddressByID(new Long(receiverDealerId));// 经销商地址PO
			TtVsDlvryReqPO repo = new TtVsDlvryReqPO();// 发运申请PO
			repo.setReqId(new Long(reqId));
			repo = (TtVsDlvryReqPO) dao.getPoObject(repo);
			TtVsDlvryReqDtlPO redpo = new TtVsDlvryReqDtlPO();// 发运申请明细PO
			redpo.setReqId(new Long(reqId));
			List list = dao.select(redpo);
			if (list != null && list.size() > 0) {
				if (dpo != null && apo != null && repo != null) {

					for (int i = 0; i < list.size(); i++) {
						CuxErpDeliverPO cedpo = new CuxErpDeliverPO();// 接口表PO
						
						cedpo.setCustomerNumber(dpo.getDealerCode());// 经销商代码
						cedpo.setCustomerName(dpo.getDealerName());// 经销商名称
						cedpo.setShipToLocation(apo.getAddCode());// 收货地址编号
						// 收货方(RECEIVER)地址编号
						//cedpo.setInvoiceToLocation(dpo.getBillAddress());// 订货方的 开票地址					
						cedpo.setOrderNumber(repo.getDlvryReqNo());// 编号必须为数字类型
						// (申请单编号)
						if("订做车订单".equals(orderTypeName)){
							cedpo.setOrderType("1");
						}else if("补充订单".equals(orderTypeName)){
							cedpo.setOrderType("2");
						}else if("特殊订单".equals(orderTypeName)){
							cedpo.setOrderType("3");
						}
//						cedpo.setOrderType(orderTypeName);// 订单类型（文字)
						cedpo.setOrderDate(repo.getReqDate());// 订购日期
						// 经销商发运申请提报日期
//						if (wpo != null) {
//							cedpo.setShipFrom(wpo.getWarehouseName());// 出货工厂
//							// 仓库名称
//						}
						cedpo.setRequestDate(repo.getReqDate());// 请求日期
						// 默认为经销商申请提报日期
						cedpo.setScheduleShipDate(repo.getReqDate());// 计划发运日期
						// 默认为经销商申请提报日期
						if (repo.getFundType() != null) {
							TtVsAccountTypePO tpo = new TtVsAccountTypePO();
							tpo.setTypeId(repo.getFundType());
							tpo = (TtVsAccountTypePO) dao.getPoObject(tpo);
							if (tpo != null) {
//								cedpo.setFundsType(tpo.getTypeCode());// 资金类型在ERP
								cedpo.setFundsType(tpo.getMarkCode());
								// 维护
								// （code）
							}
						}
						cedpo.setPlateNumber(BigDecimal.valueOf(repo.getTmpLicenseAmount()));// 临时牌照的数量
						cedpo.setStatus("N");// 处理标识“N”,“Y”

						redpo = (TtVsDlvryReqDtlPO) list.get(i);
						cedpo.setPriceListId(BigDecimal.valueOf(redpo.getPriceId()));// 价目表ID（子表中信息）
						TmVhclMaterialPO mpo = new TmVhclMaterialPO();// 物料PO
						mpo.setMaterialId(redpo.getMaterialId());
						mpo = (TmVhclMaterialPO) dao.getPoObject(mpo);
						if (mpo != null) {
							cedpo.setItemCode(mpo.getMaterialCode());// 整车的物料编码
						}
						cedpo.setOrderedQuantity(BigDecimal.valueOf(redpo.getReqAmount()));// 申请数量
						cedpo.setCreationDate(new Date());// 创建时间
						cedpo.setSeqId(new BigDecimal(redpo.getDetailId()));//新增 ： seq_id
						dao.insert(cedpo);
						cedpo = null;
					}
					TtProcInterfaceMonitorPO log = new TtProcInterfaceMonitorPO();
					log.setInterfaceName("DMS->ERP 经销商订单发运申请提报表【CUX_ERP_DELIVER】下发");
					log.setProcName("DMS->ERP【CUX_ERP_DELIVER】 SEND");
					log.setCount(list.size());
					log.setCreateDate(new Date());
					log.setStatus(new Integer(1));
					dao.insert(log);
					logger.info("====*****DMS -> ERP 对接口表 经销商订单发运申请提报表【CUX_ERP_DELIVER】 下发结束,共下发【" + list.size() + "】条数据 ***************** ====");
				} else {
					String erro = "";
					if (dpo == null) {
						erro = "经销商信息";
					} else if (apo == null) {
						erro = "经销商地址信息";
					} else if (repo == null) {
						erro = "订单发运申请信息";
					} else {
						erro = "未知错误";
					}
					logger.info("====*****DMS -> ERP 对接口表 经销商订单发运申请提报表 【CUX_ERP_DELIVER】 下发失败了,【" + erro + " 为 NULL】 *** ====");
				}

			} else {
				logger.info("====*****DMS -> ERP 对接口表 经销商订单发运申请提报表 【CUX_ERP_DELIVER】 下发失败了,【订单发运申请明细信息长度为 0 】 *** ====");
			}
		} catch (NumberFormatException e) {
			// TODO 自动生成 catch 块
			TtProcInterfaceMonitorPO log = new TtProcInterfaceMonitorPO();
			log.setInterfaceName("DMS->ERP 经销商订单发运申请提报表【CUX_ERP_DELIVER】下发");
			log.setProcName("DMS->ERP【CUX_ERP_DELIVER】 SEND");
			log.setCount(new Integer(0));
			log.setCreateDate(new Date());
			log.setStatus(new Integer(0));
			dao.insert(log);
			e.printStackTrace();
			logger.info("====*****DMS -> ERP 对接口表 经销商订单发运申请提报表【CUX_ERP_DELIVER】 下发失败了 ****************************** ====");
		}
	}

	/**
	 * 订单资源审核：保留数量确认
	 */
	public void reserveAmountConfirm() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] materialId = request.getParamValues("materialId");// 物料ID
			String[] reqAmount = request.getParamValues("reqAmount");// 申请数量
			String[] reserveAmount = request.getParamValues("reserveAmount");// 保留数量
			String[] singlePrice = request.getParamValues("singlePrice");// 单价
			String[] discountRate = request.getParamValues("discountRate");// 折扣率
			String[] totalPrice = request.getParamValues("totalPrice");// 金额
			String[] discountSPrice = request.getParamValues("discountSPrice");// 折扣后单价
			String[] discountPrice = request.getParamValues("discountPrice");// 折扣额
			String[] batchNo = request.getParamValues("batchNo");// 批次号
			String reqVer = request.getParamValue("reqVer");// 发运申请VER
			String[] ver = request.getParamValues("ver");// 发运申请明细VER
			String orderId = request.getParamValue("orderId"); // 订单ID
			String reqId = request.getParamValue("reqId"); // 发运申请ID
			String[] detailId = request.getParamValues("detailId");// 发运申请明细ID
			String[] orderDetailId = request.getParamValues("orderDetailId");// 订单明细ID
			String orderType = request.getParamValue("orderType"); // 订单类型
			String warehouseId = request.getParamValue("warehouseId"); // 仓库ID
			String fundTypeId = request.getParamValue("fundType"); // 资源类型
			String priceId = request.getParamValue("priceId"); // 价格类型
			String otherPriceReason = request.getParamValue("otherPriceReason"); // 使用其他价格原因
			String dealerId = request.getParamValue("dealerId"); // 开票方id
			// String checkRemark = request.getParamValue("checkRemark"); //
			// 审核描述
			String reqTotalPrice = request.getParamValue("reqTotalPrice"); // 总价(折扣后)
			String discountTotalPrice = request.getParamValue("discountTotalPrice"); // 折扣总额
			String modifyFlag = request.getParamValue("modifyFlag"); // 是否修改标志
			String reserveTotalAmount = request.getParamValue("reserveTotalAmount"); // 保留数量合计
			String accountId = CommonUtils.checkNull(request.getParamValue("accountId"));// 资金账户
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));// 折扣账户
			String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType"));// 发运方式
			String orderRemark = CommonUtils.checkNull(request.getParamValue("orderRemark"));// 备注
			// String receiver =
			// CommonUtils.checkNull(request.getParamValue("receiver")); // 收货方
			// String linkMan =
			// CommonUtils.checkNull(request.getParamValue("linkMan")); // 联系人
			// String tel = CommonUtils.checkNull(request.getParamValue("tel"));
			// // 联系电话

			String isBingcai = CommonUtils.checkNull(request.getParamValue("isBingcai")); // 资金类型:1=兵财存货融资;
			
			//判断发运申请的状态
			TtVsDlvryReqPO tvdrp=new TtVsDlvryReqPO();
			tvdrp.setReqId(new Long(reqId));
			 tvdrp= (TtVsDlvryReqPO) dao.select(tvdrp).get(0);
			if(!(tvdrp.getReqStatus().intValue()==Constant.ORDER_REQ_STATUS_01||tvdrp.getReqStatus().intValue()==Constant.ORDER_REQ_STATUS_05)){
				act.setOutData("returnValue", 2);
				return;
			}
			// 0=非兵财存货融资

			String returnValue = "1";

			boolean verFlag = true;
			verFlag = VerFlagDao.getVerFlag("TT_VS_DLVRY_REQ", "REQ_ID", reqId, reqVer);
			if (verFlag) {

				if (verFlag) {
					TtVsOrderPO orderPo = reportDao.getTtSalesOrder(orderId);// 订单po

					/*
					 * // 资金校验 if (!"1".equals(isBingcai)) { Map<String,
					 * Object> accountMap =
					 * reportDao.getAvailableAmount(fundTypeId.split("\\|")[0],dealerId,
					 * reqId); BigDecimal availableAmount = (BigDecimal)
					 * accountMap.get("AVAILABLE_AMOUNT"); if
					 * (availableAmount.doubleValue() <
					 * Double.parseDouble(reqTotalPrice)) { returnValue = "3"; } }
					 */

					// 校验通过，冻结资金
					if (returnValue.equals("1")) {
						dmsReleasePrice(reqId.toString(), logonUser.getUserId().toString());
						// 冻结资金(资金类型:1=兵财存货融资,不做资金同步)

						/*
						 * // 冻结资金(资金类型:1=兵财存货融资,不做资金同步) if
						 * (!accountId.equals("") && !"1".equals(isBingcai)) {
						 * accoutDao.syncAccountFreeze(reqId, accountId, new
						 * BigDecimal(reqTotalPrice),
						 * logonUser.getUserId().toString()); } if
						 * (!discountAccountId.equals("") &&
						 * !"1".equals(isBingcai)) {
						 * accoutDao.syncAccountFreeze(reqId, discountAccountId,
						 * new BigDecimal(discountTotalPrice),
						 * logonUser.getUserId().toString()); }
						 */

						// 更新订单表
						TtVsOrderPO tvop1 = new TtVsOrderPO();
						TtVsOrderPO tvop2 = new TtVsOrderPO();
						tvop1.setOrderId(Long.parseLong(orderId));
						tvop2.setVer(orderPo.getVer().intValue() + 1);
						tvop2.setFundTypeId(new Long(fundTypeId.split("\\|")[0]));
						// tvop2.setOrderRemark(orderRemark);
						tvop2.setUpdateBy(logonUser.getUserId());
						tvop2.setUpdateDate(new Date(System.currentTimeMillis()));
						dao.update(tvop1, tvop2);

						// 更新发运申请表
						TtVsDlvryReqPO tvdrp1 = new TtVsDlvryReqPO();
						TtVsDlvryReqPO tvdrq2 = new TtVsDlvryReqPO();
						tvdrp1.setReqId(new Long(reqId));

						TtVsDlvryReqPO t1 = (TtVsDlvryReqPO) dao.select(tvdrp1).get(0);
						tvdrq2.setReqRemark(orderRemark);
						tvdrq2.setFundType(new Long(fundTypeId.split("\\|")[0]));
						if (priceId != null && !priceId.equals("")) {
							tvdrq2.setPriceId(priceId);
						}
						tvdrq2.setOtherPriceReason(otherPriceReason);
						tvdrq2.setModifyFlag(new Integer(modifyFlag));
						tvdrq2.setWarehouseId(new Long(warehouseId));

						// tvdrq2.setReqTotalPrice(new Double(reqTotalPrice));
						tvdrq2.setDiscount(new Double(discountTotalPrice));
						tvdrq2.setReserveTotalAmount(new Integer(reserveTotalAmount));
						tvdrq2.setDeliveryType(new Integer(deliveryType));
						// 发运方式为自提时
//						if (tvdrq2.getDeliveryType().intValue() == Constant.TRANSPORT_TYPE_01.intValue()) {
//							if (Integer.parseInt(orderType) == Constant.ORDER_TYPE_01.intValue()) {
//								if (t1.getOrderDealerId() == null || t1.getOrderDealerId().intValue() == 0) {
//									tvdrq2.setReceiver(orderPo.getOrderOrgId());
//								} else {
//									tvdrq2.setReceiver(t1.getOrderDealerId());
//								}
//							} else {
//								tvdrq2.setReceiver(orderPo.getOrderOrgId());
//							}
//						}

						tvdrq2.setVer(new Integer(Integer.parseInt(reqVer) + 1));
						tvdrq2.setUpdateBy(logonUser.getUserId());
						tvdrq2.setUpdateDate(new Date(System.currentTimeMillis()));
						/*
						 * if (Integer.parseInt(orderType) ==
						 * Constant.ORDER_TYPE_01.intValue()) {
						 * tvdrq2.setReqTotalAmount(new
						 * Integer(reserveTotalAmount)); }
						 */
						dao.update(tvdrp1, tvdrq2);

						// orderPo = reportDao.getTtSalesOrder(orderId);

						ResourceReserveQuery rrq = new ResourceReserveQuery();
						Long logId = rrq.insLog(reqId, logonUser.getUserId());

						for (int i = 0; i < detailId.length; i++) {
							if (detailId[i] != null && !detailId[i].equals("")) {
								if (Integer.parseInt(orderType) != Constant.ORDER_TYPE_01.intValue()) {
									// 更新订单明细
									TtVsOrderDetailPO tvodp1 = new TtVsOrderDetailPO();
									TtVsOrderDetailPO tvodp2 = new TtVsOrderDetailPO();
									tvodp1.setDetailId(new Long(orderDetailId[i]));

									/*
									 * TtVsOrderDetailPO tvodp =
									 * reportDao.getTtSalesOrderDetail(orderDetailId[i]);
									 * tvodp2.setCallAmount(tvodp.getCallAmount()-
									 * new Integer(reqAmount[i])+ new
									 * Integer(reserveAmount[i])); } else {
									 */
									tvodp2.setCheckAmount(new Integer(reserveAmount[i]));

									dao.update(tvodp1, tvodp2);
								}

								// 更新发车申请明细
								TtVsDlvryReqDtlPO tvdrdp1 = new TtVsDlvryReqDtlPO();
								TtVsDlvryReqDtlPO tvdrdp2 = new TtVsDlvryReqDtlPO();
								tvdrdp1.setDetailId(new Long(detailId[i]));
								tvdrdp2.setSinglePrice(new Double(singlePrice[i]));
								tvdrdp2.setTotalPrice(new Double(totalPrice[i]) - new Double(discountPrice[i]));
								tvdrdp2.setDiscountRate(new Float(discountRate[i]));
								tvdrdp2.setDiscountSPrice(new Double(discountSPrice[i]));
								tvdrdp2.setDiscountPrice(new Double(discountPrice[i]));
								tvdrdp2.setReserveAmount(new Integer(reserveAmount[i]));
								tvdrdp2.setUpdateBy(logonUser.getUserId());
								tvdrdp2.setUpdateDate(new Date(System.currentTimeMillis()));
								tvdrdp2.setVer(new Integer(Integer.parseInt(ver[i] == null || ver[i].equals("") ? "0" : ver[i]) + 1));
								/*
								 * if (Integer.parseInt(orderType) ==
								 * Constant.ORDER_TYPE_01 .intValue()) {
								 * tvdrdp2.setReqAmount(new
								 * Integer(reserveAmount[i])); }
								 */
								dao.update(tvdrdp1, tvdrdp2);

								// 删除资源保留
								TtVsOrderResourceReservePO tvorrp = new TtVsOrderResourceReservePO();
								tvorrp.setReqDetailId(new Long(detailId[i]));
								if(!"10201001".equals(orderType)){//如果是常规订单不做删除资源了保留资源的操作
									
									dao.delete(tvorrp);
								}
								
								

								// 新增资源保留
								if (batchNo[i] != null && !batchNo[i].equals("")) {
									String[] tempArray = batchNo[i].split("\\/");
									for (int j = 0; j < tempArray.length; j++) {
										String tempStr = tempArray[j];
										String[] tempArray2 = tempStr.split("-");
										String patchNO = tempArray2[0];
										String count = tempArray2[1];
										tvorrp = new TtVsOrderResourceReservePO();
										tvorrp.setReserveId(Long.parseLong(SequenceManager.getSequence("")));
										tvorrp.setReqDetailId(new Long(detailId[i]));
										tvorrp.setMaterialId(new Long(materialId[i]));
										tvorrp.setBatchNo(patchNO);
										tvorrp.setAmount(new Integer(count));
										tvorrp.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_01);
										tvorrp.setOemCompanyId(logonUser.getCompanyId());
										tvorrp.setWarehouseId(new Long(warehouseId));
										tvorrp.setReserveType(Constant.RESERVE_TYPE_01);
										if(!"10201001".equals(orderType)){//如果是常规订单不做删除资源了保留资源的操作
											dao.insert(tvorrp);
										}

										rrq.insDtlLog(logId, detailId[i], patchNO, count, "0", materialId[i], logonUser.getUserId());
									}
								}
							} else {

								// 新增订单明细
								TtVsOrderDetailPO tvodp = new TtVsOrderDetailPO();
								tvodp.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
								tvodp.setOrderId(new Long(orderId));
								tvodp.setMaterialId(new Long(materialId[i]));
								tvodp.setOrderAmount(new Integer(0));
								tvodp.setCallAmount(new Integer(0));
								tvodp.setCheckAmount(new Integer(reserveAmount[i]));
								tvodp.setSinglePrice(new Double(singlePrice[i]));
								tvodp.setTotalPrice(new Double(totalPrice[i]) - new Double(discountPrice[i]));
								tvodp.setVer(new Integer(0));
								tvodp.setDiscountRate(new Float(discountRate[i]));
								tvodp.setDiscountSPrice(new Double(discountSPrice[i]));
								tvodp.setDiscountPrice(new Double(discountPrice[i]));
								tvodp.setCreateBy(logonUser.getUserId());
								tvodp.setCreateDate(new Date(System.currentTimeMillis()));
								dao.insert(tvodp);

								// 新增发车申请明细
								TtVsDlvryReqDtlPO tvdrdp = new TtVsDlvryReqDtlPO();
								tvdrdp.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
								tvdrdp.setReqId(new Long(reqId));
								tvdrdp.setOrderDetailId(tvodp.getDetailId());
								tvdrdp.setMaterialId(new Long(materialId[i]));
								tvdrdp.setReqAmount(new Integer(0));
								tvdrdp.setVer(new Integer(0));
								tvdrdp.setSinglePrice(new Double(singlePrice[i]));
								tvdrdp.setTotalPrice(new Double(totalPrice[i]) - new Double(discountPrice[i]));
								tvdrdp.setDiscountRate(new Float(discountRate[i]));
								tvdrdp.setDiscountSPrice(new Double(discountSPrice[i]));
								tvdrdp.setDiscountPrice(new Double(discountPrice[i]));
								tvdrdp.setReserveAmount(new Integer(reserveAmount[i]));
								tvdrdp.setCreateBy(logonUser.getUserId());
								tvdrdp.setCreateDate(new Date(System.currentTimeMillis()));
								dao.insert(tvdrdp);

								// 新增资源保留
								if (batchNo[i] != null && !batchNo[i].equals("")) {
									String[] tempArray = batchNo[i].split("\\/");
									for (int j = 0; j < tempArray.length; j++) {
										String tempStr = tempArray[j];
										String[] tempArray2 = tempStr.split("-");
										String patchNO = tempArray2[0];
										String count = tempArray2[1];
										TtVsOrderResourceReservePO tvorrp = new TtVsOrderResourceReservePO();
										tvorrp.setReserveId(Long.parseLong(SequenceManager.getSequence("")));
										tvorrp.setReqDetailId(tvdrdp.getDetailId());
										tvorrp.setMaterialId(new Long(materialId[i]));
										tvorrp.setBatchNo(patchNO);
										tvorrp.setAmount(new Integer(count));
										tvorrp.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_01);
										tvorrp.setOemCompanyId(logonUser.getCompanyId());
										tvorrp.setWarehouseId(new Long(warehouseId));
										tvorrp.setReserveType(Constant.RESERVE_TYPE_01);
										if(!"10201001".equals(orderType)){//如果是常规订单不做删除资源了保留资源的操作
											dao.insert(tvorrp);
										}

										rrq.insDtlLog(logId, tvdrdp.getDetailId().toString(), patchNO, count, "0", materialId[i], logonUser.getUserId());
									}
								}
							}
						}
						if (!accountId.equals("") && !"1".equals(isBingcai)) {
							dmsFreezePrice(reqId, accountId, new BigDecimal(reqTotalPrice), logonUser.getUserId().toString());
						}
						if (!discountAccountId.equals("") && !"1".equals(isBingcai)) {
							dmsFreezePrice(reqId, discountAccountId, new BigDecimal(discountTotalPrice), logonUser.getUserId().toString());
						}
					}

				} else {
					returnValue = "2";
				}
			} else {
				returnValue = "2";
			}
			act.setOutData("returnValue", returnValue);
		} catch (RuntimeException e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.PUTIN_FAILURE_CODE, e.getMessage());
			logger.error(logonUser, e1);
			act.setException(e);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SAVE_FAILURE_CODE, "订单资源审核：保留数量确认");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 批次号选择
	 */
	public void patchNoSelect() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String wareHouseId = CommonUtils.checkNull(request.getParamValue("wareHouseId"));// 仓库id
			String materalId = CommonUtils.checkNull(request.getParamValue("materalId")); // 订单数量
			String batchNo = CommonUtils.checkNull(request.getParamValue("batchNo")); // 物料id
			String amount = CommonUtils.checkNull(request.getParamValue("amount")); // 配车数量
			String reserveActualAmount = CommonUtils.checkNull(request.getParamValue("reserveActualAmount")); // 实际配车数量
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 订单类型
			String specialBatchNo = CommonUtils.checkNull(request.getParamValue("specialBatchNo")); // 特殊批次号
			String initNo = CommonUtils.checkNull(request.getParamValue("initNo")); // 初始批次
			String reqAmount = CommonUtils.checkNull(request.getParamValue("reqAmount")); // 申请数量
			Long companyId = logonUser.getCompanyId();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("wareHouseId", wareHouseId);
			map.put("materalId", materalId);
			map.put("orderType", orderType);
			map.put("specialBatchNo", specialBatchNo);
			map.put("companyId", companyId.toString());

			List<Map<String, Object>> w_List = dao.getPatchNoSelectQuery(map);

			act.setOutData("w_List", w_List);
			act.setOutData("batchNo", batchNo);
			act.setOutData("materalId", materalId);
			act.setOutData("amount", amount);
			act.setOutData("reserveActualAmount", reserveActualAmount);
			act.setOutData("initNo", initNo);
			act.setOutData("reqAmount", reqAmount);
			act.setForword(ORDER_RESOURCE_RESERVE_PATCH_NO_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核批次号选择");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void orderResourceReserveCancel() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reqVer = request.getParamValue("reqVer");// 发运申请VER
			String orderId = request.getParamValue("orderId"); // 订单ID
			String reqId = request.getParamValue("reqId"); // 发运申请ID
			String orderType = request.getParamValue("orderType"); // 订单类型
			String[] reqAmount = request.getParamValues("reqAmount");// 申请数量
			String[] detailId = request.getParamValues("detailId");// 发运申请明细ID
			String[] orderDetailId = request.getParamValues("orderDetailId");// 订单明细ID
			String isBingcai = CommonUtils.checkNull(request.getParamValue("isBingcai")); // 资金类型:1=兵财存货融资;
			// 0=非兵财存货融资

			String returnValue = "1";

			boolean verFlag = true;
			verFlag = VerFlagDao.getVerFlag("TT_VS_DLVRY_REQ", "REQ_ID", reqId, reqVer);
			if (verFlag) {
				// 释放资金
				if (!"1".equals(isBingcai)) {
					dmsReleasePrice(reqId, logonUser.getUserId().toString());
				}

				// 发运申请更新
				TtVsDlvryReqPO tvdrp1 = new TtVsDlvryReqPO();
				tvdrp1.setReqId(new Long(reqId));
				TtVsDlvryReqPO tvdrp2 = new TtVsDlvryReqPO();
				tvdrp2.setReqStatus(Constant.ORDER_REQ_STATUS_07);// 已取消
				tvdrp2.setVer(new Integer(Integer.parseInt(reqVer) + 1));
				tvdrp2.setUpdateBy(logonUser.getUserId());
				tvdrp2.setUpdateDate(new Date(System.currentTimeMillis()));
				tvdrp2.setfAuditTime(new Date(System.currentTimeMillis())); //update by zhulei for 写入发运申请表初审时间 on 2016-08-16
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
							tvodp2.setCallAmount(tvodp.getCallAmount().intValue() - Integer.parseInt(reqAmount[i]));
							tvodp2.setUpdateBy(logonUser.getUserId());
							tvodp2.setUpdateDate(new Date(System.currentTimeMillis()));
							dao.update(tvodp1, tvodp2);
						}
					}
				}

				// 非常规订单更新订单
				if (Integer.parseInt(orderType) != Constant.ORDER_TYPE_01.intValue()) {
					TtVsOrderPO tvop1 = new TtVsOrderPO();
					tvop1.setOrderId(new Long(orderId));
					TtVsOrderPO tvop2 = new TtVsOrderPO();
					tvop2.setOrderStatus(Constant.ORDER_COM_STATUS_06);// 已取消
					tvop2.setUpdateBy(logonUser.getUserId());
					tvop2.setUpdateDate(new Date(System.currentTimeMillis()));
					dao.update(tvop1, tvop2);

					OrderBase ob = new OrderBase();
					String oldOrderId = ob.getOldOrderId(orderId);

					if (!CommonUtils.isNullString(oldOrderId)) {
						OrderDAO odao = OrderDAO.getInstance();
						Map<String, String> map = new HashMap<String, String>();
						map.put("orderStatus", Constant.ORDER_COM_STATUS_06.toString());
						map.put("updateBy", logonUser.getUserId().toString());
						map.put("updateDate", (String.valueOf(new Date(System.currentTimeMillis()).getTime())));
						odao.orderModify(oldOrderId, map);
					}

					// add by WHX,2012.09.12
					// ======================================================START
					SpecialNeedDao snDao = SpecialNeedDao.getInstance();
					TtVsOrderPO tvo = snDao.orderQuery(Long.parseLong(orderId));

//					int order_Type = tvo.getOrderType();

//					if (order_Type == Constant.ORDER_TYPE_03.intValue()) {
//						Map<String, String> oMap = new HashMap<String, String>();
//						oMap.put("reqId", tvo.getSpecialReqId().toString());
//						oMap.put("status", Constant.SPECIAL_NEED_STATUS_08.toString());
//						oMap.put("userId", logonUser.getUserId().toString());
//
//						SpecialNeedConfirm snc = new SpecialNeedConfirm();
//						snc.spcialReqStatusUpdate(oMap);
//					}
					// ======================================================END
				}

				/*
				 * // 释放资金 if (!"1".equals(isBingcai)) { AccountBalanceDetailDao
				 * dao = AccountBalanceDetailDao.getInstance();
				 * dao.releaseAllFreezeAmountByReqId(reqId,
				 * logonUser.getUserId().toString()); }
				 */

				// 向发运申请操作日志表写入日志信息
				ReqLogUtil.creatReqLog(Long.parseLong(reqId), Constant.REQ_LOG_TYPE_09, logonUser.getUserId());
				// 同步结算中心下级经销商订单
				if (Integer.parseInt(orderType) != Constant.ORDER_TYPE_01.intValue()) {
					AccountBalanceDetailDao.getInstance().syncOrderToDFS(Long.valueOf(orderId), true, logonUser.getUserId(), true);
				} else {
					AccountBalanceDetailDao.getInstance().syncReqToDFS(Long.parseLong(reqId), true);
				}
			} else {
				returnValue = "2";
			}
			act.setOutData("returnValue", returnValue);
		} catch (RuntimeException e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.PUTIN_FAILURE_CODE, e.getMessage());
			logger.error(logonUser, e1);
			act.setException(e);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SAVE_FAILURE_CODE, "订单资源审核保存");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void orderResourceReserveBack() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String reqVer = request.getParamValue("reqVer");// 发运申请VER
			String orderId = request.getParamValue("orderId"); // 订单ID
			String reqId = request.getParamValue("reqId"); // 发运申请ID
			String orderType = request.getParamValue("orderType"); // 订单类型
			String[] detailId = request.getParamValues("detailId");// 发运申请明细ID
			String isBingcai = CommonUtils.checkNull(request.getParamValue("isBingcai")); // 资金类型:1=兵财存货融资;
			// 0=非兵财存货融资

			String returnValue = "1";
			//判断发运申请的状态
			TtVsDlvryReqPO tvdrp=new TtVsDlvryReqPO();
			tvdrp.setReqId(new Long(reqId));
			 tvdrp= (TtVsDlvryReqPO) dao.select(tvdrp).get(0);
			if(!(tvdrp.getReqStatus().intValue()==Constant.ORDER_REQ_STATUS_01||tvdrp.getReqStatus().intValue()==Constant.ORDER_REQ_STATUS_05||tvdrp.getReqStatus().intValue()==Constant.ORDER_REQ_STATUS_CYBH)){
				act.setOutData("returnValue", 2);
				return;
			}

			boolean verFlag = true;
			verFlag = VerFlagDao.getVerFlag("TT_VS_DLVRY_REQ", "REQ_ID", reqId, reqVer);
			if (verFlag) {
				// 释放资金
				if (!"1".equals(isBingcai)) {
					dmsReleasePrice(reqId, logonUser.getUserId().toString());
				}

				// 发运申请更新
				TtVsDlvryReqPO tvdrp1 = new TtVsDlvryReqPO();
				tvdrp1.setReqId(new Long(reqId));
				TtVsDlvryReqPO tvdrp2 = new TtVsDlvryReqPO();
				tvdrp2.setReqStatus(Constant.ORDER_REQ_STATUS_07);// 驳回
				tvdrp2.setVer(new Integer(Integer.parseInt(reqVer) + 1));
				tvdrp2.setUpdateBy(logonUser.getUserId());
				tvdrp2.setUpdateDate(new Date(System.currentTimeMillis()));
				tvdrp2.setfAuditTime(new Date(System.currentTimeMillis())); //update by zhulei for 写入发运申请表初审时间 on 2016-08-16
				dao.update(tvdrp1, tvdrp2);

				// 释放保留资源
				for (int i = 0; i < detailId.length; i++) {
					if (detailId[i] != null && !detailId.equals("")) {
						TtVsOrderResourceReservePO tvorrp1 = new TtVsOrderResourceReservePO();
						tvorrp1.setReqDetailId(new Long(detailId[i]));
						TtVsOrderResourceReservePO tvorrp2 = new TtVsOrderResourceReservePO();
						tvorrp2.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_02);// 已取消
						dao.update(tvorrp1, tvorrp2);
					}
				}

				// 订做车订单
//				if (Integer.parseInt(orderType) == Constant.ORDER_TYPE_03.intValue()) {
					TtVsOrderPO tvop1 = new TtVsOrderPO();
					tvop1.setOrderId(new Long(orderId));
					TtVsOrderPO tvop2 = new TtVsOrderPO();
					tvop2.setOrderStatus(Constant.ORDER_COM_STATUS_04);// 驳回
					tvop2.setUpdateBy(logonUser.getUserId());
					tvop2.setUpdateDate(new Date(System.currentTimeMillis()));
					dao.update(tvop1, tvop2);

					OrderBase ob = new OrderBase();
					String oldOrderId = ob.getOldOrderId(orderId);

					if (!CommonUtils.isNullString(oldOrderId)) {
						OrderDAO odao = OrderDAO.getInstance();
						Map<String, String> map = new HashMap<String, String>();
						map.put("orderStatus", Constant.ORDER_COM_STATUS_06.toString());
						map.put("updateBy", logonUser.getUserId().toString());
						map.put("updateDate", (String.valueOf(new Date(System.currentTimeMillis()).getTime())));
						odao.orderModify(oldOrderId, map);
					}

					// add by WHX,2012.09.12
					// ======================================================START
					SpecialNeedDao snDao = SpecialNeedDao.getInstance();
					TtVsOrderPO tvo = snDao.orderQuery(Long.parseLong(orderId));

//					int order_Type = tvo.getOrderType();
//
//					if (order_Type == Constant.ORDER_TYPE_03.intValue()) {
//						Map<String, String> oMap = new HashMap<String, String>();
//						oMap.put("reqId", tvo.getSpecialReqId().toString());
//						oMap.put("status", Constant.SPECIAL_NEED_STATUS_08.toString());
//						oMap.put("userId", logonUser.getUserId().toString());
//
//						SpecialNeedConfirm snc = new SpecialNeedConfirm();
//						snc.spcialReqStatusUpdate(oMap);
//					}
					// ======================================================END
				}

				// 向发运申请操作日志表写入日志信息
				ReqLogUtil.creatReqLog(Long.parseLong(reqId), Constant.REQ_LOG_TYPE_BH, logonUser.getUserId());
//			} else {
//				returnValue = "2";
//			}
			act.setOutData("returnValue", returnValue);
		} catch (RuntimeException e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.PUTIN_FAILURE_CODE, e.getMessage());
			logger.error(logonUser, e1);
			act.setException(e);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "订做车订单资源审核驳回");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// ---------------------------------------------by Fengalon
	public BigDecimal getLowJSZXPriceNormal(String orderId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderId", orderId);

		Map<String, Object> theMap = accoutDao.getJSZXNormalPrice(map);

		if (!CommonUtils.isNullMap(theMap)) {
			BigDecimal oldPrice = new BigDecimal(theMap.get("OLDPRICE").toString());

			return oldPrice;
		} else {
			// return -999,for no data exception
			return new BigDecimal("-999");
		}
	}

	public BigDecimal getLowJSZXPriceUrgent(String orderId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderId", orderId);

		Map<String, Object> theMap = accoutDao.getJSZXUrgentPrice(map);

		if (!CommonUtils.isNullMap(theMap)) {
			BigDecimal oldPrice = new BigDecimal(theMap.get("OLDPRICE").toString());

			return oldPrice;
		} else {
			// return -999,for no data exception
			return new BigDecimal("-999");
		}
	}

	public int toComparePrice(BigDecimal newPrice, BigDecimal oldPrice) {
		return newPrice.compareTo(oldPrice);
	}

	public void compareToAction() {
/*		String orderType = request.getParamValue("orderType");
		String orderId = request.getParamValue("orderId");

		String[] materialId = request.getParamValues("materialId");
		String[] reserveAmount = request.getParamValues("reserveAmount");

		BigDecimal newPrice = new BigDecimal("0");

		if (materialId.length == 0) {
			act.setOutData("priComparaFlag", "-2");

			return;
		} else {
			int len = materialId.length;

			String priceId = null;
			Map<String, Object> map = null;

			if (Constant.ORDER_TYPE_01.toString().equals(orderType)) {
				map = accoutDao.getOldNormalPrice(orderId);
			} else if (Constant.ORDER_TYPE_02.toString().equals(orderType)) {
				//map = accoutDao.getOldUrgentPrice(orderId);
			}

			if (!CommonUtils.isNullMap(map)) {
				priceId = map.get("PRICE_ID").toString();
			} else {
				act.setOutData("priComparaFlag", "2");

				return;
			}

//			for (int i = 0; i < len; i++) {
//				BigDecimal thePrice = new BigDecimal(accoutDao.getSingleCountPrice(priceId, materialId[i]).get("SALES_PRICE").toString()).multiply(new BigDecimal(reserveAmount[i]));
//				newPrice = newPrice.add(thePrice);
//			}
		}

		// defind param, for oldPrice being "-999"
		BigDecimal oldFlag = new BigDecimal("-999");

		// 常规订单判断
		if (Constant.ORDER_TYPE_01.toString().equals(orderType)) {
			BigDecimal oldPrice = new BigDecimal("0");

			oldPrice = this.getLowJSZXPriceNormal(orderId);

			if (oldFlag.compareTo(oldPrice) != 0) {
				if (this.toComparePrice(newPrice, oldPrice) == 1) {
					act.setOutData("priComparaFlag", "-1");

					return;
				}
			}
			// 补充订单判断
		} else if (Constant.ORDER_TYPE_02.toString().equals(orderType)) {
			BigDecimal oldPrice = new BigDecimal("0");

			oldPrice = this.getLowJSZXPriceUrgent(orderId);

			if (oldFlag.compareTo(oldPrice) != 0) {
				if (this.toComparePrice(newPrice, oldPrice) == 1) {
					act.setOutData("priComparaFlag", "-1");

					return;
				}
			}
		}
*/
		act.setOutData("priComparaFlag", "1");
	}
	/**
	 * 验证在接口表中是否存在该类型
	 */
	public void checkExists() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String typeId=request.getParamValue("typeId");
				String dealerId=request.getParamValue("dealerId");
				TtVsAccountTypePO tvat=new TtVsAccountTypePO();
				tvat.setTypeId(new Long(typeId));
				tvat=(TtVsAccountTypePO) dao.select(tvat).get(0);
				String typeCode=tvat.getTypeCode();
				Map<String ,Object> map=CommonUtils.getAmounts(dealerId, typeCode);
				act.setOutData("Flag", map);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询视图表中是否存在该资金类型异常！！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 未满足常规订单明细查询
	 */
	public void resourceQueryDetail(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String material_id=act.getRequest().getParamValue("material_id");
			List<Map<String,Object>> list=dao.getUnFitNormalOrderDetail(material_id);
			act.setOutData("list", list);
			act.setForword(LOCK_INIT_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"资源锁定明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 验证在接口表中是否存在该类型
	 */
	public void orderStockCheck() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String []materialId=request.getParamValues("materialId");
				String [] resAmount=request.getParamValues("reserveAmount");
				String orderType=request.getParamValue("orderType");
				String warehouseId=request.getParamValue("warehouseId");
				int flag=dao.getResourceStatus(materialId,resAmount,orderType,warehouseId);
				act.setOutData("Flag", flag);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "验证资源失败！！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
