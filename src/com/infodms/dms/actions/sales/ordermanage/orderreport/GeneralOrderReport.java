/**
 * @Title: GeneralOrderReport.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-21
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sales.ordermanage.orderreport;

import java.math.BigDecimal;
import java.util.*;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.relation.DealerRelationDAO;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.salesConsultant.SalesConsultantDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetOrderNOUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class GeneralOrderReport {
	private Logger logger = Logger.getLogger(GeneralOrderReport.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final OrderReportDao dao = OrderReportDao.getInstance();

	private final String GENERAL_ORDER_REPORT_QUERY_URL = "/jsp/sales/ordermanage/orderreport/generalOrderReportQuery.jsp";// 常规订单提报查询页面
	private final String GENERAL_ORDER_REPORT_QUERY_CVS_URL = "/jsp/sales/ordermanage/orderreport/generalOrderReportQueryCVS.jsp";// 常规订单提报查询页面
	private final String GENERAL_ORDER_REPORT_ADD_URL = "/jsp/sales/ordermanage/orderreport/generalOrderReportAdd.jsp";// 常规订单提报新增页面
	private final String GENERAL_ORDER_DETAIL_LIST_URL = "/jsp/sales/ordermanage/orderreport/generalOrderDetailList.jsp";
	public void generalOrderReoprtQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, String>> dateList = dao.getGeneralDateList(oemCompanyId);
			
			List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>() ;
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				areaList = MaterialGroupManagerDao.getDealerLevelBusiness(poseId.toString());
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				areaList = MaterialGroupManagerDao.getDealerLevelBusinessAll(poseId.toString());
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			Map<String,Object> m=CommonUtils.getIsConstracExpire(logonUser.getDealerCode());
			int count= 	Integer.parseInt(m.get("COUNT").toString());
			String contractDate=m.get("CONTRACT_DATE").toString();
			act.setOutData("IsExpire", count);
			act.setOutData("expireDate", contractDate);
			 act.setOutData("IsExpire", count);

            Calendar c=Calendar.getInstance();
            c.setTime(new Date());
            int w=c.get(Calendar.DAY_OF_WEEK)-1;
            if(w==0){
                w=7;
            }
            TmBusinessParaPO po=new TmBusinessParaPO();
            po.setParaId(50051001);
            po=(TmBusinessParaPO)dao.select(po).get(0);
            int x=Integer.parseInt(po.getParaValue());
            int isReportable=0;
            isReportable=(w<=x) ? 1 :0;
            act.setOutData("isReportable",isReportable);
            act.setOutData("x",x);



			act.setForword(GENERAL_ORDER_REPORT_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "常规订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void generalOrderReoprtQueryPreCVS() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, String>> dateList = dao.getGeneralDateList(oemCompanyId);
			
			List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>() ;
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				areaList = MaterialGroupManagerDao.getDealerLevelBusiness(poseId.toString());
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				areaList = MaterialGroupManagerDao.getDealerLevelBusinessAll(poseId.toString());
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			act.setForword(GENERAL_ORDER_REPORT_QUERY_CVS_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "常规订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void generalOrderReoprtQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerIds = logonUser.getDealerId();
			String orderWeek = CommonUtils.checkNull(request.getParamValue("orderWeek"));
			String[] array = orderWeek.split("-");
			String year = array[0];
			String week = array[1];
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			
			if (!areaId.equals("")) {
				array = areaId.split("\\|");
				areaId = array[0];
				dealerIds = array[1];
			}
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getDealerLevelBusinessIdStr(poseId.toString());

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getGeneralOrderReportList(year, week, areaId, areaIds, dealerIds, logonUser.getOemCompanyId(), curPage, Constant.PAGE_SIZE_MAX);
			
			act.setOutData("ps", ps);
			act.setOutData("orderWeek", orderWeek);
			act.setOutData("areaId", areaId);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "常规订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 常规订单提报:查看订单详细信息
	 * */
	public void showOrderDetail(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerIds = logonUser.getDealerId();
			String orderWeek = CommonUtils.checkNull(request.getParamValue("orderWeek"));
			String[] array = orderWeek.split("-");
			String year = array[0];
			String week = array[1];
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			if (!areaId.equals("")) {
				array = areaId.split("\\|");
				areaId = array[0];
			}
			// 业务范围id字符串
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getDealerLevelBusinessIdStr(poseId.toString());

			List <Map<String, Object>> detailList = dao.getOrderDetailList(year, week, areaId, areaIds, dealerIds);
			act.setOutData("detailList", detailList);
			act.setForword(GENERAL_ORDER_DETAIL_LIST_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "常规订单提报:查看订单详细信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 常规订单提报点击修改执行的方法
	 */
	public void generalOrderReoprtAddPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String year = CommonUtils.checkNull(request.getParamValue("year"));
			String week = CommonUtils.checkNull(request.getParamValue("week"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			Map<String, Object> detail = dao.getGeneralOrderReportDetail(year, week, areaId, dealerId, groupId, logonUser.getOemCompanyId());

			List<Map<String, Object>> materialList = dao.getGeneralReportMaterialList(year, week, dealerId, groupId);

			act.setOutData("detail", detail);
			act.setOutData("groupId", groupId) ;
			act.setOutData("materialList", materialList);
			act.setForword(GENERAL_ORDER_REPORT_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "常规订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//订单明细保存
	public void generalOrderReoprtAdd() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			Integer orgType = logonUser.getOrgType();
			String year = CommonUtils.checkNull(request.getParamValue("year"));
			String week = CommonUtils.checkNull(request.getParamValue("week"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String index = CommonUtils.checkNull(request.getParamValue("index"));
			String totalPrice = CommonUtils.checkNull(request.getParamValue("total"));
			String applyAmount = CommonUtils.checkNull(request.getParamValue("applyAmount"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				act.setOutData("sysServer", "jc") ;
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				act.setOutData("sysServer", "cvs") ;
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
			
			if(!chkAmount(dealerId, areaId, year, week, groupId, applyAmount, logonUser.getOemCompanyId())) {
				act.setOutData("returnValue", 2) ;
			} else {

				// 得到当前月份
				TmDateSetPO datePO = new TmDateSetPO();
				datePO.setSetYear(year);
				datePO.setSetWeek(week);
				datePO = dao.geTmDateSetPO(datePO);
				Integer month = datePO != null ? new Integer(datePO.getSetMonth()) : null;
	
				// 判断是否存在未提报的订单
				TtVsOrderPO po = new TtVsOrderPO();
				po.setOrderYear(new Integer(year));
				po.setOrderWeek(new Integer(week));
				po.setOrderOrgId(new Long(dealerId));
				po.setAreaId(new Long(areaId));
				po.setOrderType(Constant.ORDER_TYPE_01);
				po.setOrderStatus(Constant.ORDER_STATUS_01);// 未提报
				po = dao.geTtVsOrderPO(po);
				Long orderId = (po == null ? new Long(SequenceManager.getSequence("")) : po.getOrderId());
	
				// 保存订单明细
				for (int i = 1; i <= Integer.parseInt(index); i++) {
					String material = CommonUtils.checkNull(request.getParamValue("material" + i));
					String amount = CommonUtils.checkNull(request.getParamValue("amount" + i));
					String price = CommonUtils.checkNull(request.getParamValue("price" + i));
					String pricelistId = CommonUtils.checkNull(request.getParamValue("priceListId" + i));
	
					// 删除该订单下所有明细
					TtVsOrderDetailPO detailPO = new TtVsOrderDetailPO();
					detailPO.setOrderId(orderId);
					detailPO.setMaterialId(new Long(material));
					dao.deleteSalesOrderDetail(detailPO);
	
					if (!amount.equals("") && Double.parseDouble(amount) > 0) {
						// 明细保存
						detailPO = new TtVsOrderDetailPO();
						detailPO.setDetailId(new Long(SequenceManager.getSequence("")));
						detailPO.setOrderId(orderId);
						detailPO.setMaterialId(new Long(material));
						detailPO.setOrderAmount(new Integer(amount));
						detailPO.setSinglePrice(new Double(price));
						detailPO.setTotalPrice(new Double(detailPO.getOrderAmount().intValue() * detailPO.getSinglePrice().doubleValue()));
						detailPO.setVer(new Integer(0));
						detailPO.setCreateDate(new Date());
						detailPO.setCreateBy(logonUser.getUserId());
						detailPO.setPriceListId(pricelistId==null||"".equals(pricelistId)?null:new Long(pricelistId));
						dao.insertSalesOrderDetail(detailPO);
					}
				}
				if (po == null) {
					po = new TtVsOrderPO();
					po.setOrderId(orderId);
					po.setCompanyId(GetOemcompanyId.getOemCompanyId(logonUser));
					po.setOrderType(Constant.ORDER_TYPE_01);
					po.setIsRefitOrder(0);
					po.setOrderYear(new Integer(year));
					po.setOrderMonth(month);
					po.setOrderWeek(new Integer(week));
					po.setOrderOrgType(orgType);
					po.setOrderOrgId(new Long(dealerId));
					po.setBillingOrgType(new Long(orgType));
					
					DealerRelationDAO dr = DealerRelationDAO.getInstance() ;
					String firstDlrId = dr.getFirstDlr(dealerId).get("DEALER_ID").toString() ;
					
					po.setBillingOrgId(new Long(firstDlrId));
					TmDealerPO tmDealerPO = new TmDealerPO();
					tmDealerPO.setDealerId(new Long(dealerId));
					List dealerList = dao.select(tmDealerPO);
					String dealerCode = ((TmDealerPO)dealerList.get(0)).getDealerCode();
					Map<String,String> codeMap  = GetOrderNOUtil.getAreaShortcode(areaId);
					String areaCode = codeMap.get("AREA_SHORTCODE");
//					String orderNO = GetOrderNOUtil.getOrderNO(areaCode,Constant.ORDER_TYPE_01+"", "D", dealerCode);
					String orderNO=SequenceManager.getSequence("");
					po.setOrderNo(orderNO);
					po.setOrderStatus(Constant.ORDER_STATUS_01);
					po.setVer(new Integer(0));
					po.setCreateDate(new Date());
					po.setCreateBy(logonUser.getUserId());
					po.setAreaId(new Long(areaId));
					po.setOrderPrice(new Double(totalPrice));
					dao.insertSalesOrder(po);
				} else {
					TtVsOrderPO condition = new TtVsOrderPO();
					condition.setOrderId(orderId);
	
					TtVsOrderPO value = new TtVsOrderPO();
					value.setOrderPrice(new Double(dao.getOrderPrice(po.getOrderId().toString())));
					value.setUpdateDate(new Date());
					value.setUpdateBy(logonUser.getUserId());
	
					dao.update(condition, value);
				}
	
				act.setOutData("returnValue", 1);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "常规订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 常规订单提报
	public void generalOrderReoprtSubmit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orderWeek = CommonUtils.checkNull(request.getParamValue("orderWeekGet"));
			String[] array = orderWeek.split("-");
			String year = array[0];
			String week = array[1];
			String areaId = CommonUtils.checkNull(request.getParamValue("areaIdGet"));
			array = areaId.split("\\|");
			areaId = array[0];
			String dealerId = logonUser.getDealerId();
			//根据dealerId获取priceId（价格类型）
			TmDealerPO tdPO=new TmDealerPO();
			tdPO.setDealerId(new Long(dealerId));
			tdPO=(TmDealerPO) dao.select(tdPO).get(0);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", year);
			map.put("week", week);
			map.put("areaId", areaId);
			map.put("dealerId", dealerId);

			// 判断是否存在未提报的订单
			List<Map<String, Object>> list = dao.getGeneralOrderReportList(map);
			if (list.size() == 0) {
				act.setOutData("returnValue", 2);
			} else {
				List<Map<String, Object>> errorList = dao.getGeneralReportErrorList(year, week, areaId, dealerId, logonUser.getOemCompanyId());
				// 判断已提报数量与本次提报数量之和是否小于最小提报量
				if (errorList.size() != 0) {
					act.setOutData("returnValue", 3);
				} else {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> temp = list.get(i);
						BigDecimal orderId = (BigDecimal) temp.get("ORDER_ID");
						TtVsOrderPO condition = new TtVsOrderPO();
						TtVsOrderPO value = new TtVsOrderPO();
						condition.setOrderId(new Long(orderId.longValue()));
						value.setRaiseDate(new Date());
						value.setOrderStatus(Constant.ORDER_STATUS_03);// 已提报
						value.setUpdateBy(logonUser.getUserId());
						value.setUpdateDate(value.getRaiseDate());
						value.setPriceId(tdPO.getPersonCharge());
						dao.updateSalesOrder(condition, value);
					}
					act.setOutData("returnValue", 1);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "常规订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	// 常规订单提报
	public void generalOrderReoprtSubmitCVS() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orderWeek = CommonUtils.checkNull(request.getParamValue("orderWeekGet"));
			String[] array = orderWeek.split("-");
			String year = array[0];
			String week = array[1];
			String areaId = CommonUtils.checkNull(request.getParamValue("areaIdGet"));
			array = areaId.split("\\|");
			areaId = array[0];
			String dealerId = logonUser.getDealerId();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("year", year);
			map.put("week", week);
			map.put("areaId", areaId);
			map.put("dealerId", dealerId);

			// 判断是否存在未提报的订单
			List<Map<String, Object>> list = dao.getGeneralOrderReportList(map);
			if (list.size() == 0) {
				act.setOutData("returnValue", 2);
			} else {
				List<Map<String, Object>> errorList = dao.getGeneralReportErrorListCVS(year, week, areaId, dealerId, logonUser.getOemCompanyId());
				List<Map<String, Object>> errMaxList = dao.getGeneralReportMaxErrorList(year, week, areaId, dealerId, logonUser.getOemCompanyId());
				// 判断已提报数量与本次提报数量之和是否小于最小提报量
				if (errorList.size() != 0) {
					StringBuffer groupStr = new StringBuffer("") ;
					
					int len = errorList.size() ;
					
					for(int i=0; i<len; i++) {
						if(groupStr.length() == 0) {
							groupStr.append(errorList.get(i).get("GROUP_CODE").toString()) ;
						} else {
							groupStr.append(",").append(errorList.get(i).get("GROUP_CODE").toString()) ;
						}
					}
					
					act.setOutData("groupStr", groupStr.toString()) ;
					act.setOutData("returnValue", 3);
				} else if(errMaxList.size() != 0) {
					act.setOutData("returnValue", 4);
				} else {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> temp = list.get(i);
						BigDecimal orderId = (BigDecimal) temp.get("ORDER_ID");
						TtVsOrderPO condition = new TtVsOrderPO();
						TtVsOrderPO value = new TtVsOrderPO();
						condition.setOrderId(new Long(orderId.longValue()));
						value.setRaiseDate(new Date());
						value.setOrderStatus(Constant.ORDER_STATUS_03);// 已提报
						value.setUpdateBy(logonUser.getUserId());
						value.setUpdateDate(value.getRaiseDate());
						dao.updateSalesOrder(condition, value);
					}
					act.setOutData("returnValue", 1);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "常规订单提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	private boolean chkAmount(String dealerId, String areaId, String year, String week, String groupId, String applyAmount,String companyId) {
		Map<String, String> map = new HashMap<String, String>() ;
		
		map.put("dealerId", dealerId) ;
		map.put("areaId", areaId) ;
		map.put("year", year) ;
		map.put("week", week) ;
		map.put("groupId", groupId) ;
		map.put("companyId", companyId) ;
		
		boolean flag = true ;
		
		OrderReportDao ord = new OrderReportDao() ;
		Map<String, Object> groupMap = ord.chkAmount(map) ;
		
		int quotaAmount = Integer.parseInt(groupMap.get("QUOTA_AMT").toString()) ;
		int orderAmount = Integer.parseInt(groupMap.get("ORDER_AMOUNT").toString()) ;
		
		if(quotaAmount < Integer.parseInt(applyAmount) + orderAmount) {
			flag = false ;
		}
		
		return flag ;
	}
	
	private boolean chkOrder(String dealerId, String areaId, String orderType, String orderStatus, String year, String week) {
		Map<String, String> map = new HashMap<String, String>() ;
		
		map.put("dealerId", dealerId) ;
		map.put("areaId", areaId) ;
		map.put("orderType", orderType) ;
		map.put("orderStatus", orderStatus) ;
		map.put("year", year) ;
		map.put("week", week) ;
		
		if(!"0".equals(OrderReportDao.chkOrder(map))) {
			return true ;
		}
		
		return false ;
	}
	
	public void isNullOrder() {
		String flag = "0" ;
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String dealerIds = logonUser.getDealerId();
		String orderWeek = CommonUtils.checkNull(request.getParamValue("orderWeek"));
		String[] array = orderWeek.split("-");
		String year = array[0];
		String week = array[1];
		String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
		if (!areaId.equals("")) {
			array = areaId.split("\\|");
			areaId = array[0];
		}
		
		if(!chkOrder(dealerIds, areaId, Constant.ORDER_TYPE_01.toString(), Constant.ORDER_STATUS_01.toString(), year, week)) {
			flag = "1" ;
		}
		
		act.setOutData("flag", flag) ;
	}
}
