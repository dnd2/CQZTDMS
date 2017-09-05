/**
 * @Title: MatchDetailInfoQuery.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-27
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sales.ordermanage.orderdetail;

import java.io.OutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderquery.DealerSalesOrderQuery;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.ordermanage.orderdetail.OrderDetailInfoQueryDao;
import com.infodms.dms.dao.sales.ordermanage.orderdetail.OrderOptionDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderQueryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yinshunhui
 * 
 */
public class OrderOperation {
	private Logger logger = Logger.getLogger(DealerSalesOrderQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final OrderOptionDao dao = OrderOptionDao
			.getInstance();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();


	private final String LOCK_INIT_URL = "/jsp/sales/ordermanage/orderdetail/orderLockInit.jsp";// 订单锁定
	private final String RES_INIT_URL = "/jsp/sales/ordermanage/orderdetail/orderResInit.jsp";// 订单免责
	private final String ORDER_REQ_URL = "/jsp/sales/ordermanage/orderdetail/orderReqDetail.jsp";// 发运订单查询
	private final String ORDER_RES_URL = "/jsp/sales/ordermanage/orderdetail/orderResDetail.jsp";// 订单免责明细操作
	private final String ORDER_SOURCE_URL = "/jsp/sales/ordermanage/orderdetail/orderResourceInit.jsp";// 订单免责明细操作
	private final String ORDER_SOURCE_DETAIL_URL = "/jsp/sales/ordermanage/orderdetail/orderResourceDetail.jsp";// 资源明细
	private final String ORDER_RESPOND_FIRST_URL = "/jsp/sales/ordermanage/orderdetail/orderResFirstInit.jsp";// 订单免责初审操作
	private final String ORDER_RESPOND_FIRST = "/jsp/sales/ordermanage/orderdetail/orderResFirstDetail.jsp";// 订单免责初审操作
	private final String ORDER_RESPOND_END_URL = "/jsp/sales/ordermanage/orderdetail/orderResEndInit.jsp";// 订单免责终审操作
	private final String ORDER_RESPOND_END = "/jsp/sales/ordermanage/orderdetail/orderResEndDetail.jsp";// 订单免责终审操作
	//免责的方法
	
	public void doInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			OrderQueryDao orderDao=new OrderQueryDao();
			List<String> years = orderDao.getYearList();
			List<String> weeks = orderDao.getWeekList();
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new java.util.Date(), oemCompanyId);
			act.setOutData("years", years);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", dateSet != null ? dateSet.getSetYear() : "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek() : "");
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
			act.setForword(RES_INIT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单免责操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
		//免责初审查询
	
	public void doRespondFirstInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			OrderQueryDao orderDao=new OrderQueryDao();
			List<String> years = orderDao.getYearList();
			List<String> weeks = orderDao.getWeekList();
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new java.util.Date(), oemCompanyId);
			act.setOutData("years", years);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", dateSet != null ? dateSet.getSetYear() : "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek() : "");
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
			String week=request.getParamValue("week");
			if(week!=null || "".equals(week)){
				act.setOutData("curWeek",week);
			}
			act.setForword(ORDER_RESPOND_FIRST_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单免责操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//免责终审查询
	
	public void doRespondEndInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			OrderQueryDao orderDao=new OrderQueryDao();
			List<String> years = orderDao.getYearList();
			List<String> weeks = orderDao.getWeekList();
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new java.util.Date(), oemCompanyId);
			act.setOutData("years", years);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", dateSet != null ? dateSet.getSetYear() : "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek() : "");
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());	
			String week=request.getParamValue("week");
			if(week!=null || "".equals(week)){
				act.setOutData("curWeek",week);
			}
			act.setForword(ORDER_RESPOND_END_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单免责操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
		//进入订单资源报表的方法
	
	public void doSourceInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			OrderQueryDao orderDao=new OrderQueryDao();
			List<String> years = orderDao.getYearList();
			List<String> weeks = orderDao.getWeekList();
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new java.util.Date(), oemCompanyId);
			act.setOutData("years", years);
			act.setOutData("weeks", weeks);
			act.setOutData("curYear", dateSet != null ? dateSet.getSetYear() : "");
			act.setOutData("curWeek", dateSet != null ? dateSet.getSetWeek() : "");
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
			act.setForword(ORDER_SOURCE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单免责操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//订单资源查询
	public void orderResourceQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			// 查看日期配置表中当天的记录
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new java.util.Date(), oemCompanyId);
			
			String materialCode=request.getParamValue("materialCode");
			OrderQueryDao orderDao=new OrderQueryDao();
			Map<String,String> map=new HashMap<String,String>();
			map.put("materialCode",materialCode);
			map.put("orderYear",dateSet.getSetYear() );
			map.put("orderWeek", dateSet.getSetWeek());
			List<Map<String,Object>> resourceList=orderDao.orderResourceQuery(map);
			act.setOutData("resourceList", resourceList);
			act.setForword(ORDER_SOURCE_DETAIL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单免责操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 锁定资源明细查询
	 */
	public void resourceQueryDetail(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String material_id=act.getRequest().getParamValue("material_id");
			String orderId=act.getRequest().getParamValue("orderId");
			List<Map<String,Object>> list=dao.getLockResourceDetail(material_id);
			act.setOutData("list", list);
			act.setOutData("orderId", orderId);
			act.setForword(ORDER_REQ_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"资源锁定明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 解锁的方法
	 */
	public void doLockInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setOutData("orgId", logonUser.getOrgId());
			act.setForword(LOCK_INIT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单车辆明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void orderAllQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orderNo = CommonUtils.checkNull(request
					.getParamValue("orderNo"));
			String dealerCodes=CommonUtils.checkNull(request.getParamValue("dealerCodes"));
			String isRespond=CommonUtils.checkNull(request.getParamValue("isRespond"));
			String week=CommonUtils.checkNull(request.getParamValue("week"));
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String endWeek=CommonUtils.checkNull(request.getParamValue("endWeek"));
			String endYear=CommonUtils.checkNull(request.getParamValue("endYear"));
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));
			List<Map<String,Object>> dateList = dao.getGeneralDeliveryDateList_New(oemCompanyId);//dao.getGeneralDateList();
			String orderYearWeek =dateList.get(0).get("code").toString();
			String[] array = orderYearWeek.split("-");
			String orderYear = array[0];
			String orderWeek = array[1];
			Map<String, String> map = new HashMap<String, String>();
			map.put("orderNo", orderNo);
			map.put("dealerCodes", dealerCodes);
			map.put("orderYear", orderYear);
			map.put("orderWeek", orderWeek);
			map.put("dutyType", logonUser.getDutyType());
			map.put("orgId", logonUser.getOrgId().toString());
			map.put("dalerId", logonUser.getDealerId());
			map.put("isRespond", isRespond);
			map.put("week", week);
			map.put("year", year);
			map.put("endWeek", endWeek);
			map.put("endYear", endYear);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> list = dao.getOrderAllQuery(map,Integer.parseInt(pageSize),curPage);
			act.setOutData("ps", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单车辆明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//订单初审查询数据
	public void orderResFirstQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orderNo = CommonUtils.checkNull(request
					.getParamValue("orderNo"));
			String dealerCodes=CommonUtils.checkNull(request.getParamValue("dealerCodes"));
			String isRespond=CommonUtils.checkNull(request.getParamValue("isRespond"));
			String week=CommonUtils.checkNull(request.getParamValue("week"));
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));
			List<Map<String,Object>> dateList = dao.getGeneralDeliveryDateList_New(oemCompanyId);//dao.getGeneralDateList();
			String orderYearWeek =dateList.get(0).get("code").toString();
			String[] array = orderYearWeek.split("-");
			String orderYear = array[0];
			String orderWeek = array[1];
			Map<String, String> map = new HashMap<String, String>();
			map.put("orderNo", orderNo);
			map.put("dealerCodes", dealerCodes);
			map.put("orderYear", orderYear);
			map.put("orderWeek", orderWeek);
			map.put("dutyType", logonUser.getDutyType());
			map.put("orgId", logonUser.getOrgId().toString());
			map.put("dalerId", logonUser.getDealerId());
			map.put("isRespond", isRespond);
			map.put("week", week);
			map.put("year", year);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> list = dao.getOrderFisrtQuery(map,Integer.parseInt(pageSize),curPage);
			act.setOutData("ps", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单车辆明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//订单终审查询数据
	public void orderResEndQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orderNo = CommonUtils.checkNull(request
					.getParamValue("orderNo"));
			String dealerCodes=CommonUtils.checkNull(request.getParamValue("dealerCodes"));
			String isRespond=CommonUtils.checkNull(request.getParamValue("isRespond"));
			String week=CommonUtils.checkNull(request.getParamValue("week"));
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));
			List<Map<String,Object>> dateList = dao.getGeneralDeliveryDateList_New(oemCompanyId);//dao.getGeneralDateList();
			String orderYearWeek =dateList.get(0).get("code").toString();
			String[] array = orderYearWeek.split("-");
			String orderYear = array[0];
			String orderWeek = array[1];
			Map<String, String> map = new HashMap<String, String>();
			map.put("orderNo", orderNo);
			map.put("dealerCodes", dealerCodes);
			map.put("orderYear", orderYear);
			map.put("orderWeek", orderWeek);
			map.put("dutyType", logonUser.getDutyType());
			map.put("orgId", logonUser.getOrgId().toString());
			map.put("dalerId", logonUser.getDealerId());
			map.put("isRespond", isRespond);
			map.put("week", week);
			map.put("year", year);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> list = dao.getOrderEndQuery(map,Integer.parseInt(pageSize),curPage);
			act.setOutData("ps", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单车辆明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void lockUpdate() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orderDetailId = CommonUtils.checkNull(request
					.getParamValue("orderDetailId"));
			String isLock = CommonUtils.checkNull(request
					.getParamValue("isLock"));
			TtVsOrderDetailPO tvo1=new TtVsOrderDetailPO();
			tvo1.setDetailId(new Long(orderDetailId));
			TtVsOrderDetailPO tvo2=new TtVsOrderDetailPO();
			tvo2.setIsLock(new Long(isLock));
			int flag=dao.update(tvo1, tvo2);
			act.setOutData("flag", flag);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单锁定操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 常规订单发运申请（针对订单明细）
	 */
	public void lockDetailQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
				
				ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
				List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>() ;
				String orderId=atx.getRequest().getParamValue("orderId");
				String reqURL = atx.getRequest().getContextPath();
				if("/CVS-SALES".equals(reqURL.toUpperCase())){
					act.setOutData("returnValue", 1);
				}else{
					act.setOutData("returnValue", 2);
				}
				String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
				if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
					areaList = MaterialGroupManagerDao.getDealerLevelBusiness(logonUser.getPoseId().toString());
				} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
					areaList = MaterialGroupManagerDao.getDealerLevelBusinessAll(logonUser.getPoseId().toString());
				} else {
					throw new RuntimeException("判断当前系统的系统参数错误！") ;
				}
				Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
				List<Map<String,Object>> dateList = dao.getGeneralDeliveryDateList_New(oemCompanyId);//dao.getGeneralDateList();
				String orderYearWeek =dateList.get(0).get("code").toString();
				String[] array = orderYearWeek.split("-");
				String orderYear = array[0];
				String orderWeek = array[1];
				String areaId = request.getParamValue("areaId");		//业务范围
				String orderNo = CommonUtils.checkNull(request.getParamValue("orderNO")) ;
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));
				String startDate=CommonUtils.checkNull(request.getParamValue("startDate")) ;
				String endDate=CommonUtils.checkNull(request.getParamValue("endDate"));
				String dealerCodes=CommonUtils.checkNull(request.getParamValue("dealerCodes"));
				String materialCode=CommonUtils.checkNull(request.getParamValue("materialCode"));
				Map<String,String >map=new HashMap<String,String>();
				map.put("orderYear", orderYear);
				map.put("orderWeek", orderWeek);
				map.put("areaId", areaId);
				map.put("startDate", startDate);
				map.put("endDate", endDate);
				map.put("oemCompanyId", oemCompanyId.toString());
				map.put("orderNo", orderNo);
				map.put("dealerCodes",dealerCodes);
				map.put("orderId", orderId);
				map.put("materialCode", materialCode);
				PageResult<Map<String, Object>> list = dao.getDeliveryDetailQuery(map,Integer.parseInt("100"),curPage);
				act.setOutData("ps", list);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单发运申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//免责数据修改
	public void respondUpdate() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			int flag=1;
			String [] detailIds=request.getParamValues("detailId");
			String [] repondAmounts=request.getParamValues("respAmount");
			String [] checkAmounts=request.getParamValues("orderAmount");
			String []callAmounts=request.getParamValues("callAmount");
			int length=detailIds.length;
			for(int i=0;i<length;i++){
				int tempRespond=Integer.parseInt(repondAmounts[i]);
				int checkAmount=Integer.parseInt(checkAmounts[i]);
				int callAmount=Integer.parseInt(callAmounts[i]);
				if(tempRespond>(checkAmount-callAmount)){
					flag=0;
					break;
				}
				TtVsOrderDetailPO t1=new TtVsOrderDetailPO();
				t1.setDetailId(new Long(detailIds[i]));
				TtVsOrderDetailPO t2=new TtVsOrderDetailPO();
				t2.setRespondAmount(new Long(repondAmounts[i]));
				dao.update(t1, t2);
			}
			act.setOutData("flag", flag);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单免责操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//免责数据修改初始化
	public void respondUpdateInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orderId=act.getRequest().getParamValue("orderId");
			Map<String,String> map=new HashMap<String,String>();
			map.put("orderId", orderId);
			List<Map<String,Object>> list=dao.getOrderDetailList(map);
			act.setOutData("list", list);
			act.setOutData("orderId", orderId);
			act.setForword(ORDER_RES_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单免责操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//订单初审查询
	public void respondFirstInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orderId=act.getRequest().getParamValue("orderId");
			String week=act.getRequest().getParamValue("week");
			Map<String,String> map=new HashMap<String,String>();
			map.put("orderId", orderId);
			List<Map<String,Object>> list=dao.getOrderDetailList(map);
			act.setOutData("list", list);
			act.setOutData("orderId", orderId);
			act.setOutData("week", week);
			act.setForword(ORDER_RESPOND_FIRST);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单免责操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//订单免责初审
	public void respondFirst() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String flag=request.getParamValue("flag");
			String orderId=request.getParamValue("order_id");
//			String [] detailIds=request.getParamValues("detailId");
//			String [] repondAmounts=request.getParamValues("respAmount");
//			String [] checkAmounts=request.getParamValues("orderAmount");
//			String []callAmounts=request.getParamValues("callAmount");
//			int length=detailIds.length;
//			for(int i=0;i<length;i++){
//				int tempRespond=Integer.parseInt(repondAmounts[i]);
//				int checkAmount=Integer.parseInt(checkAmounts[i]);
//				int callAmount=Integer.parseInt(callAmounts[i]);
//				if(tempRespond>(checkAmount-callAmount)){
//					flag=0;
//					break;
//				}
//				TtVsOrderDetailPO t1=new TtVsOrderDetailPO();
//				t1.setDetailId(new Long(detailIds[i]));
//				TtVsOrderDetailPO t2=new TtVsOrderDetailPO();
//				t2.setRespondAmount(new Long(repondAmounts[i]));
//				dao.update(t1, t2);
//			}
			TtVsOrderPO tvo1=new TtVsOrderPO();
			tvo1.setOrderId(new Long(orderId));
			TtVsOrderPO tvo2=new TtVsOrderPO();
			//判断是通过还是驳回1表示通过0表示驳回
			if("1".equals(flag)){
				tvo2.setRespondStatus(Constant.RESPOND_STATUS_TYPE_02.toString());
				dao.update(tvo1, tvo2);
				
			}else{
				tvo2.setRespondStatus(Constant.RESPOND_STATUS_TYPE_04.toString());
				dao.update(tvo1, tvo2);
			}
			act.setOutData("flag", flag);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单免责操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	//订单免责提报
	public void respondCommit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			String orderId=request.getParamValue("orderId");
			TtVsOrderPO tvo1=new TtVsOrderPO();
			tvo1.setOrderId(new Long(orderId));
			TtVsOrderPO tvo2=new TtVsOrderPO();
			//判断是通过还是驳回1表示通过0表示驳回
			tvo2.setRespondStatus(Constant.RESPOND_STATUS_TYPE_02.toString());
			dao.update(tvo1, tvo2);
			act.setOutData("flag", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单免责操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//订单终审查询
	public void respondEndInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String orderId=act.getRequest().getParamValue("orderId");
			String week=act.getRequest().getParamValue("week");
			Map<String,String> map=new HashMap<String,String>();
			map.put("orderId", orderId);
			List<Map<String,Object>> list=dao.getOrderDetailList(map);
			act.setOutData("list", list);
			act.setOutData("orderId", orderId);
			act.setOutData("week", week);
			act.setForword(ORDER_RESPOND_END);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单免责操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//订单终审
	public void respondEnd() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String flag=request.getParamValue("flag");
			String orderId=request.getParamValue("order_id");
//			int flag=1;
//			String [] detailIds=request.getParamValues("detailId");
//			String [] repondAmounts=request.getParamValues("respAmount");
//			String [] checkAmounts=request.getParamValues("orderAmount");
//			String []callAmounts=request.getParamValues("callAmount");
//			int length=detailIds.length;
//			for(int i=0;i<length;i++){
//				int tempRespond=Integer.parseInt(repondAmounts[i]);
//				int checkAmount=Integer.parseInt(checkAmounts[i]);
//				int callAmount=Integer.parseInt(callAmounts[i]);
//				if(tempRespond>(checkAmount-callAmount)){
//					flag=0;
//					break;
//				}
//				TtVsOrderDetailPO t1=new TtVsOrderDetailPO();
//				t1.setDetailId(new Long(detailIds[i]));
//				TtVsOrderDetailPO t2=new TtVsOrderDetailPO();
//				t2.setRespondAmount(new Long(repondAmounts[i]));
//				dao.update(t1, t2);
//			}
			TtVsOrderPO tvo1=new TtVsOrderPO();
			tvo1.setOrderId(new Long(orderId));
			TtVsOrderPO tvo2=new TtVsOrderPO();
			//判断是通过还是驳回1表示通过0表示驳回
			if("1".equals(flag)){
				tvo2.setRespondStatus(Constant.RESPOND_STATUS_TYPE_03.toString());
				dao.update(tvo1, tvo2);
			}else{
				tvo2.setRespondStatus(Constant.RESPOND_STATUS_TYPE_04.toString());
				dao.update(tvo1, tvo2);
			}
			act.setOutData("flag", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订单免责操作");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 根据机构查询欠款明细导出
	 */
	public void getOrderDetailDownload(){
		try {
			
			Map<String,String> map=new HashMap<String,String>();			
			String orderId=CommonUtils.checkNull(request.getParamValue("orderId"));
			map.put("orderId", orderId);
			List<Map<String,Object>> balanceList=dao.getOrderDetailSelect(map);
			// 导出的文件名
			String fileName = "订单明细.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("订单明细查询", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			List<List<Object>> lists = new LinkedList<List<Object>>();
			int y=0;
			sheet.mergeCells(0, y, 10, y);
			sheet.addCell(new jxl.write.Label(0, y, "订单明细",wcf));
			++y;
			sheet.addCell(new Label(0, y, "经销商代码"));
			sheet.addCell(new Label(1, y, "经销商名称"));
			sheet.addCell(new Label(2, y, "销售订单号"));
			sheet.addCell(new Label(3, y, "车系"));
			sheet.addCell(new Label(4, y, "物料编码"));
			sheet.addCell(new Label(5, y, "物料名称"));
			sheet.addCell(new Label(6, y, "颜色"));
			sheet.addCell(new Label(7, y, "单价"));
			sheet.addCell(new Label(8, y, "已审核数量"));
			sheet.addCell(new Label(9, y, "已申请数量"));
			sheet.addCell(new Label(10, y, "免责数量"));
			int length=balanceList.size();
			for(int i=0;i<length;i++){
				++y;
				 Map<String,Object> maps=balanceList.get(i);
				sheet.addCell(new Label(0, y, CommonUtils.checkNull(maps.get("DEALER_CODE"))));
				sheet.addCell(new Label(1, y, CommonUtils.checkNull(maps.get("DEALER_SHORTNAME"))));
				sheet.addCell(new Label(2, y, CommonUtils.checkNull(maps.get("ORDER_NO"))));
				sheet.addCell(new Label(3, y, CommonUtils.checkNull(maps.get("SERIES_NAME"))));
				sheet.addCell(new Label(4, y, CommonUtils.checkNull(maps.get("MATERIAL_CODE"))));
				sheet.addCell(new Label(5, y, CommonUtils.checkNull(maps.get("MATERIAL_NAME"))));
				sheet.addCell(new Label(6, y, CommonUtils.checkNull(maps.get("COLOR_NAME"))));
				sheet.addCell(new Label(7, y, CommonUtils.checkNull(maps.get("SINGLE_PRICE"))));
				sheet.addCell(new Label(8, y, CommonUtils.checkNull(maps.get("CHECK_AMOUNT"))));
				sheet.addCell(new Label(9, y, CommonUtils.checkNull(maps.get("CALL_AMOUNT"))));
				sheet.addCell(new Label(10, y, CommonUtils.checkNull(maps.get("RES_AMOUNT"))));
			}
			workbook.write();
			workbook.close();
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			act.setException(e1);
		}
	}
	/*
	 * 根据机构查询欠款明细导出
	 */
	public void downLoadResData(){
		try {
			Map<String,String> map=new HashMap<String,String>();	
			String isRespond=CommonUtils.checkNull(request.getParamValue("isRespond"));
			String orderId=CommonUtils.checkNull(request.getParamValue("orderId"));
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String week=CommonUtils.checkNull(request.getParamValue("week"));
			String endWeek=CommonUtils.checkNull(request.getParamValue("endWeek"));
			String endYear=CommonUtils.checkNull(request.getParamValue("endYear"));
			String dealerCodes=CommonUtils.checkNull(request.getParamValue("dealerCodes"));
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));
			map.put("orderNo", orderNo);
			map.put("dealerCodes", dealerCodes);
			map.put("isRespond", isRespond);
			map.put("week", week);
			map.put("year", year);
			map.put("orderId", orderId);
			map.put("year", year);
			map.put("week", week);
			map.put("endYear", endYear);
			map.put("endWeek", endWeek);
			map.put("isRespond", isRespond);
			List<Map<String,Object>> balanceList=dao.getOrderDetailSelect(map);
			// 导出的文件名
			String fileName = "订单免责信息.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("免责信息", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			List<List<Object>> lists = new LinkedList<List<Object>>();
			int y=0;
			sheet.mergeCells(0, y, 10, y);
			sheet.addCell(new jxl.write.Label(0, y, "免责信息",wcf));
			++y;
			sheet.addCell(new Label(0, y, "经销商代码"));
			sheet.addCell(new Label(1, y, "经销商名称"));
			sheet.addCell(new Label(2, y, "销售订单号"));
			sheet.addCell(new Label(3, y, "车系"));
			sheet.addCell(new Label(4, y, "物料编码"));
			sheet.addCell(new Label(5, y, "物料名称"));
			sheet.addCell(new Label(6, y, "颜色"));
			//sheet.addCell(new Label(7, y, "单价"));
			sheet.addCell(new Label(7, y, "已审核数量"));
			sheet.addCell(new Label(8, y, "已申请数量"));
			sheet.addCell(new Label(9, y, "免责数量"));
			int length=balanceList.size();
			for(int i=0;i<length;i++){
				++y;
				 Map<String,Object> maps=balanceList.get(i);
				sheet.addCell(new Label(0, y, CommonUtils.checkNull(maps.get("DEALER_CODE"))));
				sheet.addCell(new Label(1, y, CommonUtils.checkNull(maps.get("DEALER_SHORTNAME"))));
				sheet.addCell(new Label(2, y, CommonUtils.checkNull(maps.get("ORDER_NO"))));
				sheet.addCell(new Label(3, y, CommonUtils.checkNull(maps.get("SERIES_NAME"))));
				sheet.addCell(new Label(4, y, CommonUtils.checkNull(maps.get("MATERIAL_CODE"))));
				sheet.addCell(new Label(5, y, CommonUtils.checkNull(maps.get("MATERIAL_NAME"))));
				sheet.addCell(new Label(6, y, CommonUtils.checkNull(maps.get("COLOR_NAME"))));
			//	sheet.addCell(new Label(7, y, CommonUtils.checkNull(maps.get("SINGLE_PRICE"))));
				sheet.addCell(new Label(7, y, CommonUtils.checkNull(maps.get("CHECK_AMOUNT"))));
				sheet.addCell(new Label(8, y, CommonUtils.checkNull(maps.get("CALL_AMOUNT"))));
				sheet.addCell(new Label(9, y, CommonUtils.checkNull(maps.get("RES_AMOUNT"))));
			}
			workbook.write();
			workbook.close();
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			act.setException(e1);
		}
	}
	
	/*
	 * 根据机构查询欠款明细导出
	 */
	public void downLoadSourceData(){
		try {
			AclUserBean logonUser = (AclUserBean) act.getSession().get(
					Constant.LOGON_USER);
			Map<String,String> map=new HashMap<String,String>();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			OrderQueryDao orderDao=new OrderQueryDao();
			TmDateSetPO dateSet = reportDao.getTmDateSetPO(new java.util.Date(), oemCompanyId);
			String materialCode=request.getParamValue("materialCode");
			map.put("materialCode",materialCode);
			map.put("orderYear",dateSet.getSetYear() );
			map.put("orderWeek", dateSet.getSetWeek());
			List<Map<String,Object>> balanceList=orderDao.orderResourceQuery(map);
			// 导出的文件名
			String fileName = "订单资源信息.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("订单资源", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			List<List<Object>> lists = new LinkedList<List<Object>>();
			int y=0;
			sheet.mergeCells(0, y, 10, y);
			sheet.addCell(new jxl.write.Label(0, y, "订单资源",wcf));
			++y;
			sheet.addCell(new Label(0, y, "车系"));
			sheet.addCell(new Label(1, y, "物料代码"));
			sheet.addCell(new Label(2, y, "物料名称"));
			sheet.addCell(new Label(3, y, "颜色"));
			sheet.addCell(new Label(4, y, "已审核数量"));
			sheet.addCell(new Label(5, y, "可用资源"));
			sheet.addCell(new Label(6, y, "未满足常规订单"));
			sheet.addCell(new Label(7, y, "实际可用数量"));
			int length=balanceList.size();
			for(int i=0;i<length;i++){
				++y;
				 Map<String,Object> maps=balanceList.get(i);
				sheet.addCell(new Label(0, y, CommonUtils.checkNull(maps.get("SERIES_NAME"))));
				sheet.addCell(new Label(1, y, CommonUtils.checkNull(maps.get("MATERIAL_CODE"))));
				sheet.addCell(new Label(2, y, CommonUtils.checkNull(maps.get("MATERIAL_NAME"))));
				sheet.addCell(new Label(3, y, CommonUtils.checkNull(maps.get("COLOR_NAME"))));
				sheet.addCell(new Label(4, y, CommonUtils.checkNull(maps.get("CALL_AMOUNT"))));
				sheet.addCell(new Label(5, y, CommonUtils.checkNull(maps.get("ACT_STOCK"))));
				sheet.addCell(new Label(6, y, CommonUtils.checkNull(maps.get("UNDO_ORDER_AMOUNT"))));
				sheet.addCell(new Label(7, y, CommonUtils.checkNull(maps.get("ACT_RESOURCE_AMOUNT"))));
			}
			workbook.write();
			workbook.close();
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			act.setException(e1);
		}
	}
}
