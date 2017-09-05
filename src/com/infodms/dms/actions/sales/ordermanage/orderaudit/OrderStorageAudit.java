package com.infodms.dms.actions.sales.ordermanage.orderaudit;

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

public class OrderStorageAudit {
	private Logger logger = Logger.getLogger(OrderResourceReserve.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final OrderAuditDao dao = OrderAuditDao.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();
	private final AccountBalanceDetailDao accoutDao = AccountBalanceDetailDao.getInstance();

	private final String ORDER_STORAGE_QUERY_URL = "/jsp/sales/ordermanage/orderaudit/orderStorageQuery.jsp";// 订单资源审核查询页
	private final String ORDER_STORAGE_DETAIL_QUERY_URL = "/jsp/sales/ordermanage/orderaudit/orderStorageDetail.jsp";// 订单资源审核明细页
	public void orderStorageQueryPre() {
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
			act.setRedirect(ORDER_STORAGE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 订单资源审核查询
	 */
	public void orderStorageQuery() {
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
			map.put("operateType", "2");
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getOrderStorAgeQuery(map, curPage, Integer.parseInt(pageSize));
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
	public void orderStorageDetailQuery() {
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
			act.setForword(ORDER_STORAGE_DETAIL_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void orderStorageSave() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String returnValue = "1";
			String reqId = request.getParamValue("reqId"); // 发车申请ID
			String checkRemark = request.getParamValue("checkRemark"); // 审核描述
			String storageAuditFlag = request.getParamValue("storageAuditFlag"); //
			TtVsDlvryReqPO tvdrp1=new TtVsDlvryReqPO();
			TtVsDlvryReqPO tvdrp2=new TtVsDlvryReqPO();
			tvdrp1.setReqId(new Long(reqId));
			if("1".equals(storageAuditFlag)){
				tvdrp2.setReqStatus(Constant.ORDER_REQ_STATUS_CYQR);
				
				List<Map<String, Object>> map=dao.getDelvyInfo(reqId);
				TtVsDlvryReqPO tvdPo=new TtVsDlvryReqPO();
				if(map.size()==1){
				//tvdPo.setReqId(new Long(reqId));
				TtVsDlvryPO tvdp=new TtVsDlvryPO();
				tvdp.setReqId(new Long(reqId));
				tvdp.setReqNo(map.get(0).get("DLVRY_REQ_NO").toString());
				tvdp.setReqTotal(map.get(0).get("REQ_TOTAL_AMOUNT").toString());
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("DELIVERY_TYPE")))&&CommonUtils.checkNull(map.get(0).get("DELIVERY_TYPE"))!=null){
					tvdp.setReqShipType(new Integer(CommonUtils.checkNull(map.get(0).get("DELIVERY_TYPE"))));
				}
				
				tvdp.setReqDate((Date) map.get(0).get("REQ_DATE"));
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("RECEIVER")))&&CommonUtils.checkNull(map.get(0).get("RECEIVER"))!=null){
					
					tvdp.setReqRecDealerId(new Long(CommonUtils.checkNull(map.get(0).get("RECEIVER"))));
					tvdp.setOrdPurDealerId(new Long(CommonUtils.checkNull(map.get(0).get("RECEIVER"))));
				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("ADDRESS_ID")))&&CommonUtils.checkNull(map.get(0).get("ADDRESS_ID"))!=null){
					
					tvdp.setReqRecAddrId(new Long(CommonUtils.checkNull(map.get(0).get("ADDRESS_ID"))));
				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("PROVINCE_ID")))&&CommonUtils.checkNull(map.get(0).get("PROVINCE_ID"))!=null){
					tvdp.setReqRecProvId(new Long(CommonUtils.checkNull(map.get(0).get("PROVINCE_ID"))));
					tvdp.setDlvBalProvId(new Long(CommonUtils.checkNull(map.get(0).get("PROVINCE_ID"))));

				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("CITY_ID")))&&CommonUtils.checkNull(map.get(0).get("CITY_ID"))!=null){
					tvdp.setReqRecCityId(new Long(CommonUtils.checkNull(map.get(0).get("CITY_ID"))));
					tvdp.setDlvBalCityId(new Long(CommonUtils.checkNull(map.get(0).get("CITY_ID"))));

				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("TOWN_ID")))&&CommonUtils.checkNull(map.get(0).get("TOWN_ID"))!=null){
					tvdp.setReqRecCountyId(new Long(CommonUtils.checkNull(map.get(0).get("TOWN_ID"))));
					tvdp.setDlvBalCountyId(new Long(CommonUtils.checkNull(map.get(0).get("TOWN_ID"))));

				}
				tvdp.setReqRecAddr(CommonUtils.checkNull(map.get(0).get("ADDRESS")));
				tvdp.setReqLinkMan(CommonUtils.checkNull(map.get(0).get("LINK_MAN")));
				tvdp.setReqTel(CommonUtils.checkNull(map.get(0).get("TEL")));
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("WAREHOUSE_ID")))&&CommonUtils.checkNull(map.get(0).get("WAREHOUSE_ID"))!=null){
					tvdp.setReqWhId(new Long(CommonUtils.checkNull(map.get(0).get("WAREHOUSE_ID"))));
					tvdp.setDlvWhId(new Long(CommonUtils.checkNull(map.get(0).get("WAREHOUSE_ID"))));

				}
				tvdp.setReqRemark(CommonUtils.checkNull(map.get(0).get("REQ_REMARK")));
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("ORDER_ID")))&&CommonUtils.checkNull(map.get(0).get("ORDER_ID"))!=null){
					tvdp.setOrdId(new Long(CommonUtils.checkNull(map.get(0).get("ORDER_ID"))));
				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("ORDER_NO")))&&CommonUtils.checkNull(map.get(0).get("ORDER_NO"))!=null){
					tvdp.setOrdNo(CommonUtils.checkNull(map.get(0).get("ORDER_NO")));
				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("ORDER_DEALER_ID")))&&CommonUtils.checkNull(map.get(0).get("ORDER_DEALER_ID"))!=null){
					tvdp.setOrdPurDealerId(new Long(CommonUtils.checkNull(map.get(0).get("ORDER_DEALER_ID"))));

				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("RESERVE_TOTAL_AMOUNT")))&&CommonUtils.checkNull(map.get(0).get("RESERVE_TOTAL_AMOUNT"))!=null){
					tvdp.setOrdTotal(new Integer(CommonUtils.checkNull(map.get(0).get("RESERVE_TOTAL_AMOUNT"))));
				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("DLV_TYPE")))&&CommonUtils.checkNull(map.get(0).get("DLV_TYPE"))!=null){
					tvdp.setDlvType(new Integer(CommonUtils.checkNull(map.get(0).get("DLV_TYPE"))));

				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("DELIVERY_TYPE")))&&CommonUtils.checkNull(map.get(0).get("DELIVERY_TYPE"))!=null){
					tvdp.setDlvShipType(new Integer(CommonUtils.checkNull(map.get(0).get("DELIVERY_TYPE"))));

				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("DLV_STATUS")))&&CommonUtils.checkNull(map.get(0).get("DLV_STATUS"))!=null){
					tvdp.setDlvStatus(new Integer(CommonUtils.checkNull(map.get(0).get("DLV_STATUS"))));
				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("DLV_IS_ZZ")))&&CommonUtils.checkNull(map.get(0).get("DLV_IS_ZZ"))!=null){
					tvdp.setDlvIsZz(new Integer(CommonUtils.checkNull(map.get(0).get("DLV_IS_ZZ"))));
				}
				
				dao.insert(tvdp);
				List<Map<String,Object>> dtlMap=dao.getDelvyDtlInfo(reqId);
				for(int i=0;i<dtlMap.size();i++){
					TtVsDlvryDtlPO tvddp=new TtVsDlvryDtlPO();
					if(!"".equals(CommonUtils.checkNull(dtlMap.get(i).get("DETAIL_ID")))&&CommonUtils.checkNull(dtlMap.get(i).get("DETAIL_ID"))!=null){
						tvddp.setReqDetailId(new Long(CommonUtils.checkNull(dtlMap.get(i).get("DETAIL_ID"))));
					}
					if(!"".equals(CommonUtils.checkNull(dtlMap.get(i).get("REQ_ID")))&&CommonUtils.checkNull(dtlMap.get(i).get("REQ_ID"))!=null){
						tvddp.setReqId(new Long(CommonUtils.checkNull(dtlMap.get(i).get("REQ_ID"))));
					}
					if(!"".equals(CommonUtils.checkNull(dtlMap.get(i).get("ORDER_ID")))&&CommonUtils.checkNull(dtlMap.get(i).get("ORDER_ID"))!=null){
						tvddp.setOrdId(new Long(CommonUtils.checkNull(dtlMap.get(i).get("ORDER_ID"))));
					}
					if(!"".equals(CommonUtils.checkNull(dtlMap.get(i).get("ORDER_DETAIL_ID")))&&CommonUtils.checkNull(dtlMap.get(i).get("ORDER_DETAIL_ID"))!=null){
						tvddp.setOrdDetailId(new Long(CommonUtils.checkNull(dtlMap.get(i).get("ORDER_DETAIL_ID"))));
					}
					if(!"".equals(CommonUtils.checkNull(dtlMap.get(i).get("MATERIAL_ID")))&&CommonUtils.checkNull(dtlMap.get(i).get("MATERIAL_ID"))!=null){
						tvddp.setMaterialId(new Long(CommonUtils.checkNull(dtlMap.get(i).get("MATERIAL_ID"))));
					}
					if(!"".equals(CommonUtils.checkNull(dtlMap.get(i).get("RESERVE_AMOUNT")))&&CommonUtils.checkNull(dtlMap.get(i).get("RESERVE_AMOUNT"))!=null){
						tvddp.setOrdTotal(new Integer(CommonUtils.checkNull(dtlMap.get(i).get("RESERVE_AMOUNT"))));
					}
					if(!"".equals(CommonUtils.checkNull(dtlMap.get(i).get("DEALER_REQ_AMOUNT")))&&CommonUtils.checkNull(dtlMap.get(i).get("DEALER_REQ_AMOUNT"))!=null){
						tvddp.setReqTotal(new Integer(CommonUtils.checkNull(dtlMap.get(i).get("DEALER_REQ_AMOUNT"))));
					}
					dao.insert(tvddp);
				}
				}else{
					returnValue="2";
				}
			}else if("2".equals(storageAuditFlag)){
				tvdrp2.setReqStatus(Constant.ORDER_REQ_STATUS_CYBH);
			}
			tvdrp2.setAuditTime(new Date());
			dao.update(tvdrp1, tvdrp2);
			//插入审核记录
			TtVsReqCheckPO tvrcp = new TtVsReqCheckPO();
			tvrcp.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
			tvrcp.setReqId(new Long(reqId));
			tvrcp.setCheckOrgId(logonUser.getOrgId());
			tvrcp.setCheckPositionId(logonUser.getPoseId());
			tvrcp.setCheckUserId(logonUser.getUserId());
			tvrcp.setCheckDate(new Date(System.currentTimeMillis()));
			if("1".equals(storageAuditFlag)){
				tvrcp.setCheckStatus(Constant.ORDER_REQ_STATUS_CYQR);
			}else{
				tvrcp.setCheckStatus(Constant.ORDER_REQ_STATUS_CYBH);
			}
			
			if (checkRemark != null && !"".equals(checkRemark)) {
				tvrcp.setCheckDesc(checkRemark);
			}
			tvrcp.setCreateBy(logonUser.getUserId());
			tvrcp.setCreateDate(new Date(System.currentTimeMillis()));
			dao.insert(tvrcp);
			
			act.setOutData("returnValue", returnValue);
		} catch (RuntimeException e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.PUTIN_FAILURE_CODE, e.getMessage());
			logger.error(logonUser, e1);
			act.setException(e);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SAVE_FAILURE_CODE, "订单储运审核保存");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void orderStorageSaveBatch(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String checkStorageIds= request.getParamValue("checkStorageId"); // 发车申请ID
			String[] reqIds=checkStorageIds.split(",");
			String returnValue="1";
			for(int i=0;i<reqIds.length;i++){
				TtVsDlvryReqPO tvdrp1=new TtVsDlvryReqPO();
				TtVsDlvryReqPO tvdrp2=new TtVsDlvryReqPO();
				tvdrp1.setReqId(new Long (reqIds[i]));
				tvdrp2.setReqStatus(Constant.ORDER_REQ_STATUS_CYQR);
				tvdrp2.setAuditTime(new Date());
				dao.update(tvdrp1, tvdrp2);
				//插入审核记录
				TtVsReqCheckPO tvrcp = new TtVsReqCheckPO();
				tvrcp.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
				tvrcp.setReqId(new Long(reqIds[i]));
				tvrcp.setCheckOrgId(logonUser.getOrgId());
				tvrcp.setCheckPositionId(logonUser.getPoseId());
				tvrcp.setCheckUserId(logonUser.getUserId());
				tvrcp.setCheckDate(new Date(System.currentTimeMillis()));
				tvrcp.setCheckStatus(Constant.ORDER_REQ_STATUS_CYQR);
				tvrcp.setCreateBy(logonUser.getUserId());
				tvrcp.setCreateDate(new Date(System.currentTimeMillis()));
				dao.insert(tvrcp);
				
				//向发运申请表写入数据
				List<Map<String, Object>> map=dao.getDelvyInfo(reqIds[i]);
				TtVsDlvryReqPO tvdPo=new TtVsDlvryReqPO();
				if(map.size()==1){
				//tvdPo.setReqId(new Long(reqId));
				TtVsDlvryPO tvdp=new TtVsDlvryPO();
				tvdp.setReqId(new Long(reqIds[i]));
				tvdp.setReqNo(map.get(0).get("DLVRY_REQ_NO").toString());
				tvdp.setReqTotal(map.get(0).get("REQ_TOTAL_AMOUNT").toString());
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("DELIVERY_TYPE")))&&CommonUtils.checkNull(map.get(0).get("DELIVERY_TYPE"))!=null){
					tvdp.setReqShipType(new Integer(CommonUtils.checkNull(map.get(0).get("DELIVERY_TYPE"))));
				}
				
				tvdp.setReqDate((Date) map.get(0).get("REQ_DATE"));
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("RECEIVER")))&&CommonUtils.checkNull(map.get(0).get("RECEIVER"))!=null){
					
					tvdp.setReqRecDealerId(new Long(CommonUtils.checkNull(map.get(0).get("RECEIVER"))));
					tvdp.setOrdPurDealerId(new Long(CommonUtils.checkNull(map.get(0).get("RECEIVER"))));
				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("ADDRESS_ID")))&&CommonUtils.checkNull(map.get(0).get("ADDRESS_ID"))!=null){
					
					tvdp.setReqRecAddrId(new Long(CommonUtils.checkNull(map.get(0).get("ADDRESS_ID"))));
				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("PROVINCE_ID")))&&CommonUtils.checkNull(map.get(0).get("PROVINCE_ID"))!=null){
					tvdp.setReqRecProvId(new Long(CommonUtils.checkNull(map.get(0).get("PROVINCE_ID"))));
					tvdp.setDlvBalProvId(new Long(CommonUtils.checkNull(map.get(0).get("PROVINCE_ID"))));

				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("CITY_ID")))&&CommonUtils.checkNull(map.get(0).get("CITY_ID"))!=null){
					tvdp.setReqRecCityId(new Long(CommonUtils.checkNull(map.get(0).get("CITY_ID"))));
					tvdp.setDlvBalCityId(new Long(CommonUtils.checkNull(map.get(0).get("CITY_ID"))));

				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("TOWN_ID")))&&CommonUtils.checkNull(map.get(0).get("TOWN_ID"))!=null){
					tvdp.setReqRecCountyId(new Long(CommonUtils.checkNull(map.get(0).get("TOWN_ID"))));
					tvdp.setDlvBalCountyId(new Long(CommonUtils.checkNull(map.get(0).get("TOWN_ID"))));

				}
				tvdp.setReqRecAddr(CommonUtils.checkNull(map.get(0).get("ADDRESS")));
				tvdp.setReqLinkMan(CommonUtils.checkNull(map.get(0).get("LINK_MAN")));
				tvdp.setReqTel(CommonUtils.checkNull(map.get(0).get("TEL")));
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("WAREHOUSE_ID")))&&CommonUtils.checkNull(map.get(0).get("WAREHOUSE_ID"))!=null){
					tvdp.setReqWhId(new Long(CommonUtils.checkNull(map.get(0).get("WAREHOUSE_ID"))));
					tvdp.setDlvWhId(new Long(CommonUtils.checkNull(map.get(0).get("WAREHOUSE_ID"))));

				}
				tvdp.setReqRemark(CommonUtils.checkNull(map.get(0).get("REQ_REMARK")));
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("ORDER_ID")))&&CommonUtils.checkNull(map.get(0).get("ORDER_ID"))!=null){
					tvdp.setOrdId(new Long(CommonUtils.checkNull(map.get(0).get("ORDER_ID"))));
				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("ORDER_NO")))&&CommonUtils.checkNull(map.get(0).get("ORDER_NO"))!=null){
					tvdp.setOrdNo(CommonUtils.checkNull(map.get(0).get("ORDER_NO")));
				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("ORDER_DEALER_ID")))&&CommonUtils.checkNull(map.get(0).get("ORDER_DEALER_ID"))!=null){
					tvdp.setOrdPurDealerId(new Long(CommonUtils.checkNull(map.get(0).get("ORDER_DEALER_ID"))));

				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("RESERVE_TOTAL_AMOUNT")))&&CommonUtils.checkNull(map.get(0).get("RESERVE_TOTAL_AMOUNT"))!=null){
					tvdp.setOrdTotal(new Integer(CommonUtils.checkNull(map.get(0).get("RESERVE_TOTAL_AMOUNT"))));
				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("DLV_TYPE")))&&CommonUtils.checkNull(map.get(0).get("DLV_TYPE"))!=null){
					tvdp.setDlvType(new Integer(CommonUtils.checkNull(map.get(0).get("DLV_TYPE"))));

				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("DELIVERY_TYPE")))&&CommonUtils.checkNull(map.get(0).get("DELIVERY_TYPE"))!=null){
					tvdp.setDlvShipType(new Integer(CommonUtils.checkNull(map.get(0).get("DELIVERY_TYPE"))));

				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("DLV_STATUS")))&&CommonUtils.checkNull(map.get(0).get("DLV_STATUS"))!=null){
					tvdp.setDlvStatus(new Integer(CommonUtils.checkNull(map.get(0).get("DLV_STATUS"))));
				}
				if(!"".equals(CommonUtils.checkNull(map.get(0).get("DLV_IS_ZZ")))&&CommonUtils.checkNull(map.get(0).get("DLV_IS_ZZ"))!=null){
					tvdp.setDlvIsZz(new Integer(CommonUtils.checkNull(map.get(0).get("DLV_IS_ZZ"))));
				}
				 
				dao.insert(tvdp);
				List<Map<String,Object>> dtlMap=dao.getDelvyDtlInfo(reqIds[i]);
				for(int j=0;j<dtlMap.size();j++){
					TtVsDlvryDtlPO tvddp=new TtVsDlvryDtlPO();
					if(!"".equals(CommonUtils.checkNull(dtlMap.get(j).get("DETAIL_ID")))&&CommonUtils.checkNull(dtlMap.get(j).get("DETAIL_ID"))!=null){
						tvddp.setReqDetailId(new Long(CommonUtils.checkNull(dtlMap.get(j).get("DETAIL_ID"))));
					}
					if(!"".equals(CommonUtils.checkNull(dtlMap.get(j).get("REQ_ID")))&&CommonUtils.checkNull(dtlMap.get(j).get("REQ_ID"))!=null){
						tvddp.setReqId(new Long(CommonUtils.checkNull(dtlMap.get(j).get("REQ_ID"))));
					}
					if(!"".equals(CommonUtils.checkNull(dtlMap.get(j).get("ORDER_ID")))&&CommonUtils.checkNull(dtlMap.get(j).get("ORDER_ID"))!=null){
						tvddp.setOrdId(new Long(CommonUtils.checkNull(dtlMap.get(j).get("ORDER_ID"))));
					}
					if(!"".equals(CommonUtils.checkNull(dtlMap.get(j).get("ORDER_DETAIL_ID")))&&CommonUtils.checkNull(dtlMap.get(j).get("ORDER_DETAIL_ID"))!=null){
						tvddp.setOrdDetailId(new Long(CommonUtils.checkNull(dtlMap.get(j).get("ORDER_DETAIL_ID"))));
					}
					if(!"".equals(CommonUtils.checkNull(dtlMap.get(j).get("MATERIAL_ID")))&&CommonUtils.checkNull(dtlMap.get(j).get("MATERIAL_ID"))!=null){
						tvddp.setMaterialId(new Long(CommonUtils.checkNull(dtlMap.get(j).get("MATERIAL_ID"))));
					}
					if(!"".equals(CommonUtils.checkNull(dtlMap.get(j).get("RESERVE_AMOUNT")))&&CommonUtils.checkNull(dtlMap.get(j).get("RESERVE_AMOUNT"))!=null){
						tvddp.setOrdTotal(new Integer(CommonUtils.checkNull(dtlMap.get(j).get("RESERVE_AMOUNT"))));
					}
					if(!"".equals(CommonUtils.checkNull(dtlMap.get(j).get("DEALER_REQ_AMOUNT")))&&CommonUtils.checkNull(dtlMap.get(j).get("DEALER_REQ_AMOUNT"))!=null){
						tvddp.setReqTotal(new Integer(CommonUtils.checkNull(dtlMap.get(j).get("DEALER_REQ_AMOUNT"))));
					}
					dao.insert(tvddp);
				}
			}else{
				returnValue="2";
			}
			}
			act.setOutData("returnValue", returnValue);
			
			}catch (RuntimeException e) {
				BizException e1 = new BizException(act, e, ErrorCodeConstant.PUTIN_FAILURE_CODE, e.getMessage());
				logger.error(logonUser, e1);
				act.setException(e);
			} catch (Exception e) {// 异常方法
				BizException e1 = new BizException(act, e, ErrorCodeConstant.SAVE_FAILURE_CODE, "订单储运审核保存");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
	}
}
