/**
 * 月度常规订单提报
 * */
package com.infodms.dms.actions.sales.ordermanage.orderreport;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.MonthOrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetOrderNOUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class MonthOrderReport extends BaseDao{

	public Logger logger = Logger.getLogger(MonthOrderReport.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final MonthOrderReport dao = new MonthOrderReport ();
	private static final OrderDeliveryDao deliveryDao = OrderDeliveryDao.getInstance();
	public static final MonthOrderReport getInstance() {
		return dao;
	}
	private final String monthOrderReoprtInit_URL = "/jsp/sales/ordermanage/orderreport/monthOrderReoprtInit.jsp";	// 月度常规订单提报初始页面
	private final String monthOrderReoprtInit_URL_DO = "/sales/ordermanage/orderreport/MonthOrderReport/monthOrderReoprtInit.do";
	private final String updateMonthreportInit_URL = "/jsp/sales/ordermanage/orderreport/updateMonthreportInit.jsp";		// 月度常规订单修改初始页面
	
	/**
	 * FUNCTION		:	月度常规订单提报页面初始化
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-10-8
	 */
	public void monthOrderReoprtInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String beginDate = "";
			String endDate = "";
			String timeFlag = "2";
			List<Map<String, Object>> dateList = deliveryDao.getMonthGeneralDateList(oemCompanyId);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusiness(poseId.toString()) ;//MaterialGroupManagerDao.getDealerLevelBusiness(poseId.toString());
			List<Map<String, Object>> timeList = MaterialGroupManagerDao.getCommonOrderDate(oemCompanyId);
			if (null != timeList && timeList.size()>0) {
				beginDate = String.valueOf(timeList.get(0).get("PARA_VALUE"));
				endDate = String.valueOf(timeList.get(1).get("PARA_VALUE"));
			}
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int day_Last = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			if (beginDate == "") {
				beginDate = "1";
			}
			if (endDate == "") {
				endDate = day_Last+"";
			}
			if (Integer.parseInt(beginDate)<=day && day<=Integer.parseInt(endDate)) {
				timeFlag = "1";
			}
			act.setOutData("timeFlag", timeFlag);
			act.setOutData("beginDate", beginDate);
			act.setOutData("endDate", endDate);
			act.setOutData("dateList", dateList);
			act.setOutData("areaList", areaList);
			act.setForword(monthOrderReoprtInit_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度常规订单提报页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION		:	月度常规订单提报:查询可提报月度订单列表
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-10-8
	 */
	public void getCanMonthReport(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String year = "";
			String month = "";
			String dealerId = logonUser.getDealerId();
			String orderMonth = CommonUtils.checkNull(request.getParamValue("orderMonth"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String[] arrayIds = areaId.split("\\|");
			areaId = arrayIds[0];
			if (!"".equals(orderMonth)) {
				String[] array = orderMonth.split("-");
				year = array[0];
				month = array[1];
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
			
			PageResult<Map<String, Object>> ps = MonthOrderReportDao.getCanMonthReportList(dealerId, year, month, areaId, Constant.PAGE_SIZE, curPage);
			int totalRecords = ps.getTotalRecords();
			act.setOutData("ps", ps);
			act.setOutData("totalRecords", totalRecords);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度常规订单提报:查询可提报月度订单列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION		:	月度常规订单提报:修改月度订单页面初始化
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-10-8
	 */
	public void updateMonthreportInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long poseId = logonUser.getPoseId();
			String year = "";
			String month = "";
			String orderMonth = CommonUtils.checkNull(request.getParamValue("orderMonth"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String order_id = CommonUtils.checkNull(request.getParamValue("order_id"));
			String order_no = CommonUtils.checkNull(request.getParamValue("order_no"));
			String areaName = CommonUtils.checkNull(request.getParamValue("areaName"));
			String[] arrayIds = areaId.split("\\|");
			areaId = arrayIds[0];
			if (!"".equals(orderMonth)) {
				String[] array = orderMonth.split("-");
				year = array[0];
				month = array[1];
			}
			List<Map<String, Object>> dList = MonthOrderReportDao.getCanMonthDetailReportList(year, month, areaId,order_id);
			List<Map<String, Object>> checkList = MonthOrderReportDao.getCheckList(order_id);
			
			if(!CommonUtils.isNullList(dList)) {
				String productId = dList.get(0).get("PRODUCT_COMBO_ID") == null ? "" : dList.get(0).get("PRODUCT_COMBO_ID").toString() ;
				
				act.setOutData("productId", productId) ;
			}
			
			act.setOutData("dealerId", dList.get(0).get("ORDER_ORG_ID").toString());
			act.setOutData("areaName", areaName);
			act.setOutData("areaId", areaId);
			act.setOutData("dList", dList);
			act.setOutData("checkList", checkList);
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("order_id", order_id);
			act.setOutData("order_no", order_no);
			act.setForword(updateMonthreportInit_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度常规订单提报:新增月度订单页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 月度订单新增产品：得到产品信息
	 * */
	public void showMaterialInfo(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String material_id = CommonUtils.checkNull(request.getParamValue("material_id"));
			Map<String,Object> materialInfo = MonthOrderReportDao.getMaterialInfo(material_id);
			act.setOutData("materialInfo", materialInfo);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度常规订单提报:新增月度订单页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 月度订单信息修改保存
	 * */
	public void reportChangeSaveOrSubmit(){
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String detailIds_delete = CommonUtils.checkNull(request.getParamValue("detailIds_delete"));	//用户删除的订单明细
			String[] detail_id = request.getParamValues("detail_id");									//订单明细id
			String[] material_id = request.getParamValues("material_id");								//物料id
			String[] order_amount = request.getParamValues("order_amount");								//订单明细数量
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); 					//订单id
			String operateType = CommonUtils.checkNull(request.getParamValue("operateType"));           //操作类型：1=保存；2=修改
			String order_amonut = CommonUtils.checkNull(request.getParamValue("order_amonut"));         //订单明细总计
			String area_id = CommonUtils.checkNull(request.getParamValue("areaId")); 					//业务范围id
			String order_no = CommonUtils.checkNull(request.getParamValue("order_no")); 				//订单号
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); 				//经销商id
			String orderYear = CommonUtils.checkNull(request.getParamValue("orderYear")); 
			String orderMonth = CommonUtils.checkNull(request.getParamValue("orderMonth")); 
			String productId = CommonUtils.checkNull(request.getParamValue("_productName_")) ;
			
			//删除用户选择删除的物料
			if (!"".equals(detailIds_delete)) {
				String[] ids_delete = detailIds_delete.split(",");
				for (int i = 0; i < ids_delete.length; i++) {
					TtVsOrderDetailPO detailPO = new TtVsOrderDetailPO();
					detailPO.setDetailId(Long.parseLong(ids_delete[i]));
					dao.delete(detailPO);
				}
			}
			//修改物料对应数量
			if (null != detail_id && detail_id.length>0) {
				for (int i = 0; i < detail_id.length; i++) {
					if (null != detail_id[i] && !"".equals(detail_id[i])) {
						TtVsOrderDetailPO temp = new TtVsOrderDetailPO();
						temp.setDetailId(Long.parseLong(detail_id[i]));
						TtVsOrderDetailPO value = new TtVsOrderDetailPO();
						value.setOrderAmount(Integer.parseInt(order_amount[i]));
						dao.update(temp, value);
					}
				}
			}
			//如果id为空，说明是第一次新增订单
			Long order_Id = Long.parseLong(SequenceManager.getSequence(""));
			
			TmDealerPO tmDealerPO = new TmDealerPO();
			tmDealerPO.setDealerId(Long.parseLong(dealerId));
		 	List dealerList = dao.select(tmDealerPO);
			
			if("".equals(orderId)){
				TtVsOrderPO orderPO = new TtVsOrderPO();
				orderPO.setOrderId(order_Id);
				orderPO.setCompanyId(GetOemcompanyId.getOemCompanyId(logonUser));
				orderPO.setAreaId(Long.parseLong(area_id));
				Map<String,String> codeMap  = GetOrderNOUtil.getAreaShortcode(area_id);
				String area_Code = codeMap.get("AREA_SHORTCODE");
				
			 	String dealerCode = ((TmDealerPO)dealerList.get(0)).getDealerCode();
				orderPO.setOrderNo(GetOrderNOUtil.getOrderNO(area_Code,Constant.ORDER_TYPE_02+"", "D", dealerCode));
				orderPO.setOrderType(Constant.ORDER_TYPE_01);
				orderPO.setIsRefitOrder(new Integer(0));
				orderPO.setOrderYear(Integer.parseInt(orderYear));
				orderPO.setOrderMonth(Integer.parseInt(orderMonth));
				orderPO.setOrderOrgType(Constant.ORG_TYPE_DEALER);
				orderPO.setOrderOrgId(Long.parseLong(dealerId));
				orderPO.setRaiseDate(new Date());
				if ("1".equals(operateType)) {
					orderPO.setBillingOrgId(Long.parseLong(this.getFirstDlr(dealerId).get("DEALER_ID").toString()));
					
					orderPO.setOrderStatus(Constant.ORDER_STATUS_01);
				} else {
					if(Constant.DEALER_LEVEL_01.toString().equals(((TmDealerPO)dealerList.get(0)).getDealerLevel().toString())) {
						orderPO.setBillingOrgId(Long.parseLong(dealerId));
						
						String para = CommonDAO.getPara(Constant.NORMAL_ORDER_ORG_CHK.toString()) ;
						
						if(Integer.parseInt(para) > 0) {
							orderPO.setOrderStatus(Constant.ORDER_STATUS_08);
						} else {
							orderPO.setOrderStatus(Constant.ORDER_STATUS_09);
						}
						
					} else {
						orderPO.setBillingOrgId(Long.parseLong(this.getFirstDlr(dealerId).get("DEALER_ID").toString()));
						orderPO.setOrderStatus(Constant.ORDER_STATUS_02);
					}
				}
					
				orderPO.setVer(new Integer(0));
				orderPO.setCreateBy(logonUser.getUserId());
				orderPO.setCreateDate(new Date());
				orderPO.setIsFleet(new Integer(0));
				Integer orgType = logonUser.getOrgType();
				orderPO.setBillingOrgType(new Long(orgType));
				
				if(!CommonUtils.isNullString(productId)) {
					orderPO.setProductComboId(Long.parseLong(productId)) ;
				}
				
				dao.insert(orderPO);
			} else {
				TtVsOrderPO orderPO = new TtVsOrderPO();
				orderPO.setOrderId(Long.parseLong(orderId)) ;
				TtVsOrderPO newOrderPO = new TtVsOrderPO();
				
				if ("1".equals(operateType)) {
					newOrderPO.setOrderStatus(Constant.ORDER_STATUS_01);
				} else {
					if(Constant.DEALER_LEVEL_01.toString().equals(((TmDealerPO)dealerList.get(0)).getDealerLevel().toString())) {
						String para = CommonDAO.getPara(Constant.NORMAL_ORDER_ORG_CHK.toString()) ;
						
						if(Integer.parseInt(para) > 0) {
							newOrderPO.setOrderStatus(Constant.ORDER_STATUS_08);
						} else {
							newOrderPO.setOrderStatus(Constant.ORDER_STATUS_09);
						}
						
					} else {
						newOrderPO.setOrderStatus(Constant.ORDER_STATUS_02);
					}
				}
				
				if(!CommonUtils.isNullString(productId)) {
					newOrderPO.setProductComboId(Long.parseLong(productId)) ;
				}
				
				dao.update(orderPO, newOrderPO) ;
			}
			//新增物料及数量
			if (null != material_id && material_id.length>0) {
				for (int i = 0; i < material_id.length; i++) {
					if (null == detail_id[i] || "".equals(detail_id[i])) {
						TtVsOrderDetailPO vsOrderDetailPO = new TtVsOrderDetailPO();
						vsOrderDetailPO.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
						if (!"".equals(orderId)) {
							vsOrderDetailPO.setOrderId(Long.parseLong(orderId));
						}else {
							vsOrderDetailPO.setOrderId(order_Id);
						}
						vsOrderDetailPO.setMaterialId(Long.parseLong(material_id[i]));
						vsOrderDetailPO.setOrderAmount(Integer.parseInt(order_amount[i]));
						vsOrderDetailPO.setVer(new Integer(0));
						vsOrderDetailPO.setCreateBy(logonUser.getUserId());
						vsOrderDetailPO.setCreateDate(new Date());
						dao.insert(vsOrderDetailPO);
					}
				}
			}
			/*if ("2".equals(operateType)) {
				if("".equals(orderId)){
					orderId = order_Id+"";
				}
				TtVsOrderPO tempOrderPO = new TtVsOrderPO();
				tempOrderPO.setOrderId(Long.parseLong(orderId));
				TtVsOrderPO valueOrderPO = new TtVsOrderPO();
				valueOrderPO.setOrderStatus(Constant.ORDER_STATUS_08);
				dao.update(tempOrderPO,valueOrderPO);
			}*/
			act.setForword(monthOrderReoprtInit_URL_DO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度订单信息修改保存");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 校验是否可以新增月度订单
	 * */
	public void check_addMonthreportInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			String orderMonth = CommonUtils.checkNull(request.getParamValue("orderMonth")); //订单月度
			String year = "";
			String month = "";
			if (!"".equals(orderMonth)) {
				String[] array = orderMonth.split("-");
				year = array[0];
				month = array[1];
			}
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String[] arrayIds = areaId.split("\\|");										//订单业务范围
			areaId = arrayIds[0];
			String dealerId = arrayIds[1];													//订单所属经销商

			String isCommit = MonthOrderReportDao.isCommit(dealerId, year, month, areaId);
			if ("1".equals(isCommit)) {
				act.setOutData("returnValue", 1);
			}else{
				act.setForword(monthOrderReoprtInit_URL_DO);
				act.setOutData("returnValue", 2);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "新增月度订单页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 新增月度订单
	 * */
	public void addMonthreportInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			String orderMonth = CommonUtils.checkNull(request.getParamValue("orderMonth")); //订单月度
			String year = "";
			String month = "";
			if (!"".equals(orderMonth)) {
				String[] array = orderMonth.split("-");
				year = array[0];
				month = array[1];
			}
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String[] arrayIds = areaId.split("\\|");										//订单业务范围
			areaId = arrayIds[0];
			String dealerId = arrayIds[1];													//订单所属经销商

			
			Map<String,String> orderMap = MonthOrderReportDao.getOldOrder(year, month, areaId, dealerId);
			if (null != orderMap && orderMap.size()>0) {
				String orderId = String.valueOf(orderMap.get("ORDER_ID"));
				List<Map<String, Object>> dList = MonthOrderReportDao.getCanMonthDetailReportList(year, month, areaId,orderId+"");
				List<Map<String, Object>> checkList = MonthOrderReportDao.getCheckList(orderId);
				
				if(!CommonUtils.isNullList(dList)) {
					String productId = dList.get(0).get("PRODUCT_COMBO_ID") == null ? "" : dList.get(0).get("PRODUCT_COMBO_ID").toString() ;
					
					act.setOutData("productId", productId) ;
				}
				
				act.setOutData("order_id", orderId);
				act.setOutData("checkList", checkList);
				act.setOutData("dList", dList);
			}
			
			String areaName = arrayIds[2];
			act.setOutData("areaName", areaName);
			act.setOutData("areaId", areaId);
			act.setOutData("dealerId", dealerId);
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setForword(updateMonthreportInit_URL);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "新增月度订单页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 删除月度订单信息
	 * */
	public void delOrder(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			String order_id = CommonUtils.checkNull(request.getParamValue("order_id")); 
			TtVsOrderDetailPO detailPO = new TtVsOrderDetailPO();
			detailPO.setOrderId(Long.parseLong(order_id));
			dao.delete(detailPO);
			TtVsOrderPO orderPO = new TtVsOrderPO();
			orderPO.setOrderId(Long.parseLong(order_id));
			dao.delete(orderPO);
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "删除月度订单信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
	}
	/**
	 * 月度订单提报：直接提报
	 * */
	public void orderCommit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			String order_id = CommonUtils.checkNull(request.getParamValue("order_id")); 
			
			TtVsOrderPO tempOrderPO = new TtVsOrderPO();
			tempOrderPO.setOrderId(Long.parseLong(order_id));
			TtVsOrderPO tempOrderPO1 = (TtVsOrderPO)dao.select(tempOrderPO).get(0) ;
			
			TtVsOrderPO valueOrderPO = new TtVsOrderPO();
			
			TmDealerPO tmd = new TmDealerPO() ;
			tmd.setDealerId(tempOrderPO1.getOrderOrgId()) ;
			if(Constant.DEALER_LEVEL_01.toString().equals((((TmDealerPO)dao.select(tmd).get(0)).getDealerLevel().toString()))) {
				String para = CommonDAO.getPara(Constant.NORMAL_ORDER_ORG_CHK.toString()) ;
			
				if(Integer.parseInt(para) > 0) {
					valueOrderPO.setOrderStatus(Constant.ORDER_STATUS_08);
				} else {
					valueOrderPO.setOrderStatus(Constant.ORDER_STATUS_09);
				}
			} else 
				valueOrderPO.setOrderStatus(Constant.ORDER_STATUS_02);
			
			dao.update(tempOrderPO,valueOrderPO);
			
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "删除月度订单信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	
	public Map<String, Object> getFirstDlr(String dealerId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select *\n");
		sql.append("  from tm_dealer tmd\n");  
		sql.append(" where 1 = 1\n");  
		sql.append("   and tmd.dealer_level = ").append(Constant.DEALER_LEVEL_01).append("\n");  
		sql.append(" start with tmd.dealer_id = ").append(dealerId).append("\n");  
		sql.append("connect by prior tmd.parent_dealer_d = tmd.dealer_id\n");

		return dao.pageQueryMap(sql.toString(), null, dao.getFunName()) ;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
