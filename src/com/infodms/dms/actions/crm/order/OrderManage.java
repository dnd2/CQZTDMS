package com.infodms.dms.actions.crm.order;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderaudit.OrderResourceReserveFirst;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.dealerleadsmanage.DlrLeadsManageDao;
import com.infodms.dms.dao.crm.order.OrderManageDao;
import com.infodms.dms.dao.crm.taskmanage.TaskManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcCustomerPO;
import com.infodms.dms.po.TPcLeadsAllotPO;
import com.infodms.dms.po.TPcLinkManPO;
import com.infodms.dms.po.TPcOrderAuditPO;
import com.infodms.dms.po.TPcOrderDetailAuditPO;
import com.infodms.dms.po.TPcOrderDetailPO;
import com.infodms.dms.po.TPcOrderPO;
import com.infodms.dms.po.TPcRemindPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class OrderManage {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();

	private final String ORDER_QUERY_URL = "/jsp/crm/order/orderInit.jsp";// 订单查询页面
	private final String ORDER_DETAIL_QUERY_URL = "/jsp/crm/order/orderDetailInit.jsp";// 订单详细页面
	private final String ORDER_UPDATE_URL = "/jsp/crm/order/orderUpdateInit.jsp";// 修改/退订单页面
	private final String ORDER_AUDIT_QUERY_URL = "/jsp/crm/order/orderAuditInit.jsp";// 订单修改/退订单审核页面
	private final String ORDER_AUDIT_AMOUNT_URL = "/jsp/crm/order/orderAuditAmountInit.jsp";// 订单修改/退订单审核页面
	private final String ORDER_AUDIT_URL = "/jsp/crm/order/orderAudit.jsp";// 审核页面
	private final String ORDER_AMOUNT_URL = "/jsp/crm/order/orderAmountAudit.jsp";// 审核页面
	private final String ORDER_PRINT_URL = "/jsp/crm/order/printOrder.jsp";// 订单查询页面
	private final String ORDER_CAR_QUERY_URL = "/jsp/crm/order/orderCarInit.jsp";// 订车详细查询页面
	private final String ORDER_AUDIT_CAR_QUERY_URL = "/jsp/crm/order/orderAuditCarInit.jsp";// 订车/退车详细查询页面
	
	public void doInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
		String adviser = null;
		String managerLogon = "yes";
		String adviserLogon = "no";
		String userId = null;
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			managerLogon = "no";
			adviserLogon = "yes";
		} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
			managerLogon = "no";
			adviserLogon = "no";
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
		} else if(CommonUtils.judgeDcrcLogin(logonUser.getUserId().toString())){//判断是否DCRC登陆
			managerLogon = "no";
			adviserLogon = "no";
		} 
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			adviser = logonUser.getUserId().toString();
		}
		String dutyType=logonUser.getDutyType().toString();
		
		//获取顾问列表
		List<DynaBean> adviserList = dao.getAdviserBydealer2(logonUser.getDealerId(),userId);
		//获取分组列表
		List<DynaBean> groupList = dao.getGroupBydealer(logonUser.getDealerId());
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			act.setOutData("adviserList", adviserList);
			act.setOutData("groupList", groupList);
			act.setOutData("managerLogon", managerLogon);
			act.setOutData("adviserLogon", adviserLogon);
			act.setOutData("funcStr", funcStr);
			act.setOutData("userDealerId", userDealerId);
			act.setOutData("adviser", adviser);
			act.setOutData("dutyType", dutyType);
			act.setForword(ORDER_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "进入订单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//订车详细信息查询
	public void orderAuditCarInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
		String adviser = null;
		String managerLogon = "yes";
		String adviserLogon = "no";
		String userId = null;
		
		  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
          String today=sdf.format(new Date());

          String dutyType=logonUser.getDutyType().toString();
			
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			managerLogon = "no";
			adviserLogon = "yes";
		} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
			managerLogon = "no";
			adviserLogon = "no";
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
		} else if(CommonUtils.judgeDcrcLogin(logonUser.getUserId().toString())){//判断是否DCRC登陆
			managerLogon = "no";
			adviserLogon = "no";
		} 
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			adviser = logonUser.getUserId().toString();
		}
		//获取顾问列表
		List<DynaBean> adviserList = dao.getAdviserBydealer2(logonUser.getDealerId(),userId);
		//获取分组列表
		List<DynaBean> groupList = dao.getGroupBydealer(logonUser.getDealerId());
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			act.setOutData("adviserList", adviserList);
			act.setOutData("groupList", groupList);
			act.setOutData("managerLogon", managerLogon);
			act.setOutData("adviserLogon", adviserLogon);
			act.setOutData("funcStr", funcStr);
			act.setOutData("userDealerId", userDealerId);
			act.setOutData("adviser", adviser);
			act.setOutData("dutyType", dutyType);
			act.setOutData("orgId", logonUser.getOrgId());
            act.setOutData("today", today);
			act.setForword(ORDER_AUDIT_CAR_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "进入订单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	

	/**
	 * 订车/退车记录初始查询
	 */
	public void orderAuditCarFindQuery() {
		OrderManageDao dao = new OrderManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String userId = null;
		String dutyType=logonUser.getDutyType();
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			userId = logonUser.getUserId().toString();
		} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
		}
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId"));
			String orderDate = CommonUtils.checkNull(request.getParamValue("orderDate"));
			String orderDateEnd = CommonUtils.checkNull(request.getParamValue("orderDateEnd"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("order_status"));
			String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
			String adviserId = CommonUtils.checkNull(request.getParamValue("adviserId"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			//String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));
			String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String  beStatus = CommonUtils.checkNull(request.getParamValue("be_status"));
			String adviser = userId;
			if("".equals(orderDate)){
				orderDate="2014-01-01";
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps =null;
		   if("10431005".equals(dutyType)){
				 ps=dao.orderAuditCarFindQuery1(customerName,
							telephone,orderDate,orderDateEnd,orderStatus,userDealerId,adviser,adviserId,seriesId,groupId,dealerCode,
							curPage, Integer.parseInt(pageSize));
				}else{
				 ps = dao.orderAuditCarFindQuery(customerName,
						telephone,orderDate,orderDateEnd,orderStatus,userDealerId,adviser,adviserId,seriesId,groupId,dealerCode,
						curPage, Integer.parseInt(pageSize));
				}
			
			act.setOutData("userDealerId", userDealerId);
			act.setOutData("adviser", adviser);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订车/退车记录信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	

	/**
	 * 订车/退车记录下载
	 */
	public void orderAuditDownload() {
		OrderManageDao dao = new OrderManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String userId = null;
		String dutyType=logonUser.getDutyType();
		ResponseWrapper response = act.getResponse();
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			userId = logonUser.getUserId().toString();
		} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
		}
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId"));
			String orderDate = CommonUtils.checkNull(request.getParamValue("orderDate"));
			String orderDateEnd = CommonUtils.checkNull(request.getParamValue("orderDateEnd"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("order_status"));
			String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
			String adviserId = CommonUtils.checkNull(request.getParamValue("adviserId"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			//String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));
			String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String  beStatus = CommonUtils.checkNull(request.getParamValue("be_status"));
			String adviser = userId;
			if("".equals(orderDate)){
				orderDate="2014-01-01";
			}
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps =null;
		   if("10431005".equals(dutyType)){
				 ps=dao.orderAuditCarFindQuery1(customerName,
							telephone,orderDate,orderDateEnd,orderStatus,userDealerId,adviser,adviserId,seriesId,groupId,dealerCode,
							curPage, 200000);
				}else{
					System.out.println("dutyType:"+dutyType);
				 ps = dao.orderAuditCarFindQuery(customerName,
						telephone,orderDate,orderDateEnd,orderStatus,userDealerId,adviser,adviserId,seriesId,groupId,dealerCode,
						curPage, 200000);
				}
		   
			List<Map<String, Object>> results=  ps.getRecords();
			
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			List<Object> headTemp = new LinkedList<Object>();
			
			if(!"10431005".equals(dutyType)){
				headTemp.add("大区");
				headTemp.add("省份");
				headTemp.add("经销商代码");
				headTemp.add("经销商名称");
				headTemp.add("客户姓名");
				headTemp.add("车系");
				headTemp.add("数量");
				headTemp.add("订单日期");
				headTemp.add("订单状态");
				headTemp.add("顾问");
				list.add(headTemp);
				
				for(int i=0;i<results.size();i++){
					List<Object> listTemp = new LinkedList<Object>();
					Map<String, Object> record = results.get(i);
					listTemp.add(CommonUtils.checkNull(record.get("ROOT_ORG_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("PQ_ORG_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
					listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
					listTemp.add(CommonUtils.checkNull(record.get("CUSTOMER_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("SERIES_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("ORDERNUM")));
					listTemp.add(CommonUtils.checkNull(record.get("ORDER_DATE")));
					listTemp.add(CommonUtils.checkNull(record.get("TASK_STATUS")));
					listTemp.add(CommonUtils.checkNull(record.get("ADVISER")));
					list.add(listTemp);
				}
			}else{
				headTemp.add("经销商代码");
				headTemp.add("经销商名称");
				headTemp.add("订单编号");
				headTemp.add("客户姓名");
				headTemp.add("手机");
				headTemp.add("车型");
				headTemp.add("数量");
				headTemp.add("订单日期");
				headTemp.add("订单状态");
				headTemp.add("顾问");
				list.add(headTemp);
				
				for(int i=0;i<results.size();i++){
					List<Object> listTemp = new LinkedList<Object>();
					Map<String, Object> record = results.get(i);
					listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
					listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
					listTemp.add(CommonUtils.checkNull(record.get("ORDER_ID")));
					listTemp.add(CommonUtils.checkNull(record.get("CUSTOMER_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("TELEPHONE")));
					listTemp.add(CommonUtils.checkNull(record.get("SERIES_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("ORDERNUM")));
					listTemp.add(CommonUtils.checkNull(record.get("ORDER_DATE")));
					listTemp.add(CommonUtils.checkNull(record.get("TASK_STATUS")));
					listTemp.add(CommonUtils.checkNull(record.get("ADVISER")));
					list.add(listTemp);
				}
			}
			// 导出的文件名
			String fileName = "订车退车记录下载.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			OutputStream os = response.getOutputStream();
			this.createXlsFile(list, os);
			
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订车/退车记录下载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	
	
	//订车详细信息查询
	public void orderCarInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
		String adviser = null;
		String managerLogon = "yes";
		String adviserLogon = "no";
		String userId = null;
		
		  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
          String today=sdf.format(new Date());

          String dutyType=logonUser.getDutyType();
			
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			managerLogon = "no";
			adviserLogon = "yes";
		} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
			managerLogon = "no";
			adviserLogon = "no";
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
		} else if(CommonUtils.judgeDcrcLogin(logonUser.getUserId().toString())){//判断是否DCRC登陆
			managerLogon = "no";
			adviserLogon = "no";
		} 
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			adviser = logonUser.getUserId().toString();
		}
		//获取顾问列表
		List<DynaBean> adviserList = dao.getAdviserBydealer2(logonUser.getDealerId(),userId);
		//获取分组列表
		List<DynaBean> groupList = dao.getGroupBydealer(logonUser.getDealerId());
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			act.setOutData("adviserList", adviserList);
			act.setOutData("groupList", groupList);
			act.setOutData("managerLogon", managerLogon);
			act.setOutData("adviserLogon", adviserLogon);
			act.setOutData("funcStr", funcStr);
			act.setOutData("userDealerId", userDealerId);
			act.setOutData("adviser", adviser);
			act.setOutData("dutyType", dutyType);
			act.setOutData("orgId", logonUser.getOrgId());
            act.setOutData("today", today);
			act.setForword(ORDER_CAR_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "进入订单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	/**
	 * 订车初始查询
	 */
	public void orderCarFindQuery() {
		OrderManageDao dao = new OrderManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String userId = null;
		String dutyType=logonUser.getDutyType();
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			userId = logonUser.getUserId().toString();
		} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
		}
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId"));
			String orderDate = CommonUtils.checkNull(request.getParamValue("orderDate"));
			String orderDateEnd = CommonUtils.checkNull(request.getParamValue("orderDateEnd"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("order_status"));
			String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
			String adviserId = CommonUtils.checkNull(request.getParamValue("adviserId"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			//String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));
			String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String  beStatus = CommonUtils.checkNull(request.getParamValue("be_status"));
			String adviser = userId;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps =null;
			
			if(!"10001".equals(beStatus) && !"10002".equals(beStatus) && !"10003".equals(beStatus)){
				if("10431005".equals(dutyType)){
				 ps=dao.orderCarFindQuery1(customerName,
							telephone,orderDate,orderDateEnd,orderStatus,userDealerId,adviser,adviserId,seriesId,groupId,dealerCode,
							curPage, Integer.parseInt(pageSize));
				}else{
				 ps = dao.orderCarFindQuery(customerName,
						telephone,orderDate,orderDateEnd,orderStatus,userDealerId,adviser,adviserId,seriesId,groupId,dealerCode,
						curPage, Integer.parseInt(pageSize));
				}
			}else{
				if("10431005".equals(dutyType)){
					 ps=dao.orderCarSuggestFindQuery1(customerName,
								telephone,orderDate,orderDateEnd,orderStatus,userDealerId,adviser,adviserId,seriesId,groupId,dealerCode,beStatus,
								curPage, Integer.parseInt(pageSize));
				}else{
					ps=dao.orderCarOldSuggestFindQuery(customerName,
							telephone,orderDate,orderDateEnd,orderStatus,userDealerId,adviser,adviserId,seriesId,groupId,dealerCode,beStatus,
							curPage, Integer.parseInt(pageSize));
				}
			}
			act.setOutData("userDealerId", userDealerId);
			act.setOutData("adviser", adviser);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订车信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 订单详细下载
	 */
	public void orderDetailDownload(){
		OrderManageDao dao = new OrderManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String userId = null;
		String dutyType=logonUser.getDutyType();
		ResponseWrapper response = act.getResponse();
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			userId = logonUser.getUserId().toString();
		} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
		}
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId"));
			String orderDate = CommonUtils.checkNull(request.getParamValue("orderDate"));
			String orderDateEnd = CommonUtils.checkNull(request.getParamValue("orderDateEnd"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("order_status"));
			String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
			String adviserId = CommonUtils.checkNull(request.getParamValue("adviserId"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			//String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			//String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));
			String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String  beStatus = CommonUtils.checkNull(request.getParamValue("be_status"));
			String adviser = userId;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps =null;
			if(!"10001".equals(beStatus) && !"10002".equals(beStatus) && !"10003".equals(beStatus)){
				if("10431005".equals(dutyType)){
				 ps=dao.orderCarFindQuery1(customerName,
							telephone,orderDate,orderDateEnd,orderStatus,userDealerId,adviser,adviserId,seriesId,groupId,dealerCode,
							curPage, 200000);
				}else{
				 ps = dao.orderCarFindQuery(customerName,
						telephone,orderDate,orderDateEnd,orderStatus,userDealerId,adviser,adviserId,seriesId,groupId,dealerCode,
						curPage, 200000);
				}
			}else{
				if("10431005".equals(dutyType)){
					 ps=dao.orderCarSuggestFindQuery1(customerName,
								telephone,orderDate,orderDateEnd,orderStatus,userDealerId,adviser,adviserId,seriesId,groupId,dealerCode,beStatus,
								curPage, 200000);
				}else{
					ps=dao.orderCarOldSuggestFindQuery(customerName,
							telephone,orderDate,orderDateEnd,orderStatus,userDealerId,adviser,adviserId,seriesId,groupId,dealerCode,beStatus,
							curPage, 200000);
				}
			}
			List<Map<String, Object>> results=  ps.getRecords();
			
	
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			List<Object> headTemp = new LinkedList<Object>();
			
			System.out.println("长度============="+results.size());
			if(!"10431005".equals(dutyType)){
				headTemp.add("大区");
				headTemp.add("省份");
				headTemp.add("经销商代码");
				headTemp.add("经销商名称");
				headTemp.add("客户姓名");
				headTemp.add("车型");
				headTemp.add("颜色");
				headTemp.add("数量");
				headTemp.add("订单日期");
				headTemp.add("订单状态");
				headTemp.add("顾问");
				list.add(headTemp);
				
				for(int i=0;i<results.size();i++){
					List<Object> listTemp = new LinkedList<Object>();
					Map<String, Object> record = results.get(i);
					listTemp.add(CommonUtils.checkNull(record.get("ROOT_ORG_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("PQ_ORG_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
					listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
					listTemp.add(CommonUtils.checkNull(record.get("CUSTOMER_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("SERIES_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("COLOR_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("NUM")));
					listTemp.add(CommonUtils.checkNull(record.get("ORDER_DATE")));
					listTemp.add(CommonUtils.checkNull(record.get("TASK_STATUS")));
					listTemp.add(CommonUtils.checkNull(record.get("ADVISER")));
					list.add(listTemp);
				}
			}else{
				headTemp.add("经销商代码");
				headTemp.add("经销商名称");
				headTemp.add("订单编号");
				headTemp.add("客户姓名");
				headTemp.add("手机");
				headTemp.add("车型");
				headTemp.add("颜色");
				headTemp.add("数量");
				headTemp.add("订单日期");
				headTemp.add("订单状态");
				headTemp.add("顾问");
				list.add(headTemp);
				
				for(int i=0;i<results.size();i++){
					List<Object> listTemp = new LinkedList<Object>();
					Map<String, Object> record = results.get(i);
					listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
					listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
					listTemp.add(CommonUtils.checkNull(record.get("ORDER_ID")));
					listTemp.add(CommonUtils.checkNull(record.get("CUSTOMER_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("TELEPHONE")));
					listTemp.add(CommonUtils.checkNull(record.get("SERIES_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("COLOR_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("NUM")));
					listTemp.add(CommonUtils.checkNull(record.get("ORDER_DATE")));
					listTemp.add(CommonUtils.checkNull(record.get("TASK_STATUS")));
					listTemp.add(CommonUtils.checkNull(record.get("ADVISER")));
					list.add(listTemp);
				}
			}
			// 导出的文件名
			String fileName = "订车明细下载.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			OutputStream os = response.getOutputStream();
			this.createXlsFile(list, os);
			
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "订车明细下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	
	 /**
	 * @FUNCTION   :创建下载文件
	 * @author     : zhulei
	 * @param      : @param content
	 * @param      : @param os
	 * @param      : @throws ParseException
	 * @return     :
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void createXlsFile(List<List<Object>> content,OutputStream os) throws ParseException{
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("下载模板", 0);


			for(int i=0;i<content.size();i++){
				for(int j = 0;j<content.get(i).size();j++){
						// 添加单元格
						sheet.addCell(new Label(j,i,(content.get(i).get(j) != null ? content.get(i).get(j).toString() : "")));

				}

			}
			workbook.write();
			workbook.close();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 订单数量审核初始查询
	 */
	public void orderAmountFindQuery() {
		OrderManageDao dao = new OrderManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String userId = null;
		
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			userId = logonUser.getUserId().toString();
		} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
		}
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String orderDate = CommonUtils.checkNull(request.getParamValue("orderDate"));
			String orderDateEnd = CommonUtils.checkNull(request.getParamValue("orderDateEnd"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("order_status"));
			String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
			String adviserId = CommonUtils.checkNull(request.getParamValue("adviserId"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));
			String adviser = userId;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.orderAmountFindQuery(customerName,
					telephone,orderDate,orderDateEnd,orderStatus,userDealerId,adviser,adviserId,groupId,vin,
					curPage,Integer.parseInt(pageSize));
			act.setOutData("userDealerId", userDealerId);
			act.setOutData("adviser", adviser);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "销售线索查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订单初始查询
	 */
	public void orderFindQuery() {
		OrderManageDao dao = new OrderManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String userId = null;
		PageResult<Map<String, Object>> ps;
		//判断是否顾问登陆
		if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
			userId = logonUser.getUserId().toString();
		} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
			//获取主管下属分组的所有顾问
			userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
		}
	    String orgId=logonUser.getOrgId().toString();
	    String dutyType= logonUser.getDutyType().toString();
		if(Constant.DUTY_TYPE_COMPANY.toString().equals(logonUser.getDutyType())){
			orgId="1";
		}
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String orderDate = CommonUtils.checkNull(request.getParamValue("orderDate"));
			String orderDateEnd = CommonUtils.checkNull(request.getParamValue("orderDateEnd"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("order_status"));
			String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
			String adviserId = CommonUtils.checkNull(request.getParamValue("adviserId"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));
			String adviser = userId;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			if( Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())){//销售经理审核界面
			 ps = dao.orderFindQuery(customerName,
					telephone,orderDate,orderDateEnd,orderStatus,userDealerId,adviser,adviserId,groupId,vin,
					curPage,Integer.parseInt(pageSize));
			}else if(Constant.DUTY_TYPE_COMPANY.toString().equals(logonUser.getDutyType())){//车厂总部人员查询界面
				ps = dao.orderCompanyFindQuery(customerName,
						telephone,orderDate,orderDateEnd,orderStatus,userDealerId,dealerCode,vin,orgId,dutyType,
						curPage,Integer.parseInt(pageSize));
			}else{//车厂区域经理审核界面
				ps = dao.orderMannagerFindQuery(customerName,
						telephone,orderDate,orderDateEnd,orderStatus,userDealerId,adviser,adviserId,groupId,vin,orgId,dutyType,
						curPage,Integer.parseInt(pageSize));
			}
			act.setOutData("userDealerId", userDealerId);
			act.setOutData("adviser", adviser);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "销售线索查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	

	/**
	 * 订单详细页面
	 */
	public void orderAmountDetailInit() {
		ActionContext act = ActionContext.getContext();
		OrderManageDao dao = new OrderManageDao();
		TaskManageDao dao2 = new TaskManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String orderId = act.getRequest().getParamValue("orderId");
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		try {
			//获取客户基本信息
			List<DynaBean> customerList = dao.getCustomerInfo(orderId);
			//获取车主基本信息
			List<DynaBean> ownerList = dao2.getOwnerInfo(orderId);
			//获取订单状态
			TPcOrderPO orderPo = new TPcOrderPO();
			orderPo.setOrderId(Long.parseLong(orderId));
			TPcOrderPO orderPo2 = (TPcOrderPO)dao.select(orderPo).get(0);
			String orderStatus = orderPo2.getOrderStatus().toString();
			
			//获取Table1信息
			List<DynaBean> table1List = dao2.getTable1Info(orderId,orderStatus);
			//获取Table2信息
			List<DynaBean> table2List = dao2.getTable2Info(orderId,orderStatus);
			String oldCustomerName=null;
			String oldTelephone=null;
			String oldVehicleId=null;
			String relation_code=null;
			String customerId=null;
			if(customerList.get(0).get("CUSTOMER_ID")!=null && !"".equals(customerList.get(0).get("CUSTOMER_ID"))) {
				customerId = customerList.get(0).get("CUSTOMER_ID").toString();
				System.out.println("客户ID："+customerId);
			}
			//获取老客户推荐信息
			List<DynaBean> oldList = dao2.getOldCustomerInfo(customerId);
			int old=oldList.size();
			if(old>0){
				DynaBean dbo = oldList.get(0);
				
				//获取老客户姓名
				if(dbo.get("LINK_MAN")!=null  && !"".equals(dbo.get("LINK_MAN"))) {
					oldCustomerName = dbo.get("LINK_MAN").toString();
				}
				//获取老客户电话
				if(dbo.get("LINK_PHONE")!=null && !"".equals(dbo.get("LINK_PHONE")) ) {
					oldTelephone = dbo.get("LINK_PHONE").toString();
				}
				//获取老客户车架号
				if(dbo.get("OLD_VEHICLE_ID")!=null && !"".equals(dbo.get("OLD_VEHICLE_ID")) ) {
					oldVehicleId = dbo.get("OLD_VEHICLE_ID").toString();
				}
				//获取老客户车架号
				if(dbo.get("RELATION_CODE")!=null && !"".equals(dbo.get("RELATION_CODE")) ) {
					relation_code = dbo.get("RELATION_CODE").toString();
					relation_code=relation_code.trim();
				}
			}
			
			act.setOutData("orderId", orderId);
			act.setOutData("nowDate", nowDate);
			act.setOutData("customerList", customerList);
			act.setOutData("ownerList", ownerList);
			act.setOutData("table1List", table1List);
			act.setOutData("table2List", table2List);
			act.setOutData("oldCustomerName", oldCustomerName);
			act.setOutData("oldTelephone", oldTelephone);
			act.setOutData("oldVehicleId", oldVehicleId);
			act.setOutData("relation_code", relation_code);
			
			act.setForword(ORDER_DETAIL_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 订单详细页面
	 */
	public void orderDetailInit() {
		ActionContext act = ActionContext.getContext();
		OrderManageDao dao = new OrderManageDao();
		TaskManageDao dao2 = new TaskManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String orderId = act.getRequest().getParamValue("orderId");
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		try {
			//获取客户基本信息
			List<DynaBean> customerList = dao.getCustomerInfo(orderId);
			//获取车主基本信息
			List<DynaBean> ownerList = dao2.getOwnerInfo(orderId);
			//获取订单状态
			TPcOrderPO orderPo = new TPcOrderPO();
			orderPo.setOrderId(Long.parseLong(orderId));
			TPcOrderPO orderPo2 = (TPcOrderPO)dao.select(orderPo).get(0);
			String orderStatus = orderPo2.getOrderStatus().toString();
			String oldCustomerName=null;
			String oldTelephone=null;
			String oldVehicleId=null;
			String relation_code=null;
			String customerId=null;
			if(customerList.get(0).get("CUSTOMER_ID")!=null && !"".equals(customerList.get(0).get("CUSTOMER_ID"))) {
				customerId = customerList.get(0).get("CUSTOMER_ID").toString();
				System.out.println("客户ID："+customerId);
			}
			//获取老客户推荐信息
			List<DynaBean> oldList = dao2.getOldCustomerInfo(customerId);
			int old=oldList.size();
			if(old>0){
				DynaBean dbo = oldList.get(0);
				
				//获取老客户姓名
				if(dbo.get("LINK_MAN")!=null  && !"".equals(dbo.get("LINK_MAN"))) {
					oldCustomerName = dbo.get("LINK_MAN").toString();
				}
				//获取老客户电话
				if(dbo.get("LINK_PHONE")!=null && !"".equals(dbo.get("LINK_PHONE")) ) {
					oldTelephone = dbo.get("LINK_PHONE").toString();
				}
				//获取老客户车架号
				if(dbo.get("OLD_VEHICLE_ID")!=null && !"".equals(dbo.get("OLD_VEHICLE_ID")) ) {
					oldVehicleId = dbo.get("OLD_VEHICLE_ID").toString();
				}
				//获取老客户车架号
				if(dbo.get("RELATION_CODE")!=null && !"".equals(dbo.get("RELATION_CODE")) ) {
					relation_code = dbo.get("RELATION_CODE").toString();
					relation_code=relation_code.trim();
				}
			}
			//获取Table1信息
			List<DynaBean> table1List = dao2.getTable1Info(orderId,orderStatus);
			//获取Table2信息
			List<DynaBean> table2List = dao2.getTable2Info(orderId,orderStatus);
			
			act.setOutData("orderId", orderId);
			act.setOutData("nowDate", nowDate);
			act.setOutData("customerList", customerList);
			act.setOutData("oldCustomerName", oldCustomerName);
			act.setOutData("oldTelephone", oldTelephone);
			act.setOutData("oldVehicleId", oldVehicleId);
			act.setOutData("relation_code", relation_code);
			act.setOutData("ownerList", ownerList);
			act.setOutData("table1List", table1List);
			act.setOutData("table2List", table2List);
			
			act.setForword(ORDER_DETAIL_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订单修改/退订单页面
	 */
	public void orderUpdateInit() {
		ActionContext act = ActionContext.getContext();
		OrderManageDao dao = new OrderManageDao();
		TaskManageDao dao2 = new TaskManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String orderId = act.getRequest().getParamValue("orderId");
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		try {
			//获取客户基本信息
			List<DynaBean> customerList = dao.getCustomerInfo(orderId);
			
			//获取车主基本信息
			List<DynaBean> ownerList = dao2.getOwnerInfo(orderId);
			//获取订单状态用于前台判断是否能进行退单操作（为部分交车不能 退单）
			String orderStatus = ownerList.get(0).get("ORDER_STATUS").toString();
			//获取意向颜色列表
			//List<DynaBean> colorList = dao2.getColorList();
			//获取省市区
			String oldPro = null;
			String oldCity = null;
			String oldArea = null;
			String oldCustomerName=null;
			String oldTelephone=null;
			String oldVehicleId=null;
			String relation_code=null;
			String customerId=null;
			if(customerList.get(0).get("CUSTOMER_ID")!=null && !"".equals(customerList.get(0).get("CUSTOMER_ID"))) {
				customerId = customerList.get(0).get("CUSTOMER_ID").toString();
				System.out.println("客户ID："+customerId);
			}
			if(ownerList.get(0).get("PRO2")!=null && !"".equals(ownerList.get(0).get("PRO2"))) {
				oldPro = ownerList.get(0).get("PRO2").toString();
			}
			if(ownerList.get(0).get("CITY2")!=null && !"".equals(ownerList.get(0).get("CITY2"))) {
				oldCity = ownerList.get(0).get("CITY2").toString();
			}
			if(ownerList.get(0).get("AREA2")!=null && !"".equals(ownerList.get(0).get("AREA2"))) {
				oldArea = ownerList.get(0).get("AREA2").toString();
			}
			
			//获取老客户推荐信息
			List<DynaBean> oldList = dao2.getOldCustomerInfo(customerId);
			int old=oldList.size();
			if(old>0){
				DynaBean dbo = oldList.get(0);
				
				//获取老客户姓名
				if(dbo.get("LINK_MAN")!=null  && !"".equals(dbo.get("LINK_MAN"))) {
					oldCustomerName = dbo.get("LINK_MAN").toString();
				}
				//获取老客户电话
				if(dbo.get("LINK_PHONE")!=null && !"".equals(dbo.get("LINK_PHONE")) ) {
					oldTelephone = dbo.get("LINK_PHONE").toString();
				}
				//获取老客户车架号
				if(dbo.get("OLD_VEHICLE_ID")!=null && !"".equals(dbo.get("OLD_VEHICLE_ID")) ) {
					oldVehicleId = dbo.get("OLD_VEHICLE_ID").toString();
				}
				//获取老客户车架号
				if(dbo.get("RELATION_CODE")!=null && !"".equals(dbo.get("RELATION_CODE")) ) {
					relation_code = dbo.get("RELATION_CODE").toString();
					relation_code=relation_code.trim();
				}
			}
			//获取Table1信息
			List<DynaBean> table1List = dao2.getTable1Info(orderId,orderStatus);
			//获取Table2信息
			List<DynaBean> table2List = dao2.getTable2Info(orderId,orderStatus);
			
			int size1 = table1List.size();
			int size2 = table2List.size();
			
			act.setOutData("orderId", orderId);
			act.setOutData("oldPro", oldPro);
			act.setOutData("oldCity", oldCity);
			act.setOutData("oldArea", oldArea);
			act.setOutData("nowDate", nowDate);
			act.setOutData("orderStatus", orderStatus);
			//act.setOutData("colorList", colorList);
			act.setOutData("customerList", customerList);
			act.setOutData("oldCustomerName", oldCustomerName);
			act.setOutData("oldTelephone", oldTelephone);
			act.setOutData("oldVehicleId", oldVehicleId);
			act.setOutData("relation_code", relation_code);
			act.setOutData("ownerList", ownerList);
			act.setOutData("table1List", table1List);
			act.setOutData("table2List", table2List);
			act.setOutData("size1", size1);
			act.setOutData("size2", size2);
			
			act.setForword(ORDER_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订单修改/退订单
	 */
	public void orderUpdate() {
		ActionContext act = ActionContext.getContext();
		OrderManageDao dao = new OrderManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		//获取订单页面参数
		String orderId = act.getRequest().getParamValue("orderId");
		String customerId = act.getRequest().getParamValue("customerId");
		String typeflag = act.getRequest().getParamValue("typeflag");
		String ownerName = act.getRequest().getParamValue("owner_name");//车主姓名
		String ownerPhone = act.getRequest().getParamValue("owner_phone");//车主姓名
		String ownerPaperType = act.getRequest().getParamValue("owner_paper_type");//证件类型
		String ownerPaperNo = act.getRequest().getParamValue("owner_paper_no");//证件号码
		String dPro = CommonUtils.checkNull(act.getRequest().getParamValue("dPro"));//省
		String dCity = CommonUtils.checkNull(act.getRequest().getParamValue("dCity"));//市
		String dArea = CommonUtils.checkNull(act.getRequest().getParamValue("dArea"));//区
		String ownerAddress = act.getRequest().getParamValue("owner_address");//详细地址
		String newProductSale = act.getRequest().getParamValue("new_product_sale");//新产品预售
		String dealType = act.getRequest().getParamValue("deal_type");//成交类型
		String dealerCode = act.getRequest().getParamValue("dealerCode");//交车地点
		String testDriving = act.getRequest().getParamValue("test_driving");//试乘试驾
		String table1Length = act.getRequest().getParamValue("table1Length");//未确定车架号tableRow数
		String table2Length = act.getRequest().getParamValue("table2Length");//已确定车架号tableRow数
		String reasonRemark = act.getRequest().getParamValue("reason_remark");
		
		String oldName=act.getRequest().getParamValue("old_customer_name");//老客户姓名
		String oldphone=act.getRequest().getParamValue("old_telephone");//老客户联系电话
		String oldVehicleId=act.getRequest().getParamValue("old_vehicle_id");//老客户车架号
		String JsSucessNO=act.getRequest().getParamValue("JsSucessNO");//是否有老客户推荐
	    String laoSucessNO=act.getRequest().getParamValue("laoSucessNO");//老客户推荐还是朋友推荐

	  
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			//判断订单状态（如若已提交订单审核则抛出异常）
			TPcOrderPO orderPo = new TPcOrderPO();
			orderPo.setOrderId(Long.parseLong(orderId));
			List<PO> poxx = dao.select(orderPo);
			orderPo = (TPcOrderPO)poxx.get(0);
			Integer orderStatus = orderPo.getOrderStatus();
			if(orderStatus == 60231003 || orderStatus == 60231004) {
				throw new Exception("该条数据已被处理过！");
			}
			//修改订单内车主信息
			TPcOrderPO oldOrderPo = new TPcOrderPO();
			oldOrderPo.setOrderId(Long.parseLong(orderId));
			TPcOrderPO newOrderPo = new TPcOrderPO();
			newOrderPo.setOrderId(Long.parseLong(orderId));
			newOrderPo.setFinishDate(new Date());
			newOrderPo.setOwnerName(ownerName);
			newOrderPo.setOwnerPhone(ownerPhone);
			newOrderPo.setOwnerPaperType(ownerPaperType);
			newOrderPo.setOwnerPaperNo(ownerPaperNo);
			newOrderPo.setOwnerProvince(dPro);
			newOrderPo.setOwnerCity(dCity);
			newOrderPo.setOwnerArea(dArea);
			newOrderPo.setOwnerAddress(ownerAddress);
			newOrderPo.setNewProductSale(Integer.parseInt(newProductSale));
			newOrderPo.setDealType(dealType);
			newOrderPo.setDeliveryAddress(dealerCode);
			newOrderPo.setTestDriving(Integer.parseInt(testDriving));
			if(typeflag.equals("update")) {//修改订单
				newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_03);
			} else {//退订单
				newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_04);
			}
			dao.update(oldOrderPo, newOrderPo);
			
			//保存订单审核表
			String newAuditId = SequenceManager.getSequence("");
			TPcOrderAuditPO orderAuditPo = new TPcOrderAuditPO();
			orderAuditPo.setOrderAuditId(Long.parseLong(newAuditId));
			orderAuditPo.setOrderId(Long.parseLong(orderId));
			orderAuditPo.setCustomerId(Long.parseLong(customerId));
			orderAuditPo.setManagerAudit(Constant.DIRECTOR_AUDIT_01);
			orderAuditPo.setCreateBy(logonUser.getUserId().toString());
			orderAuditPo.setCreateDate(new Date());
			orderAuditPo.setStatus(Constant.STATUS_ENABLE);
			if(typeflag.equals("update")) {//修改订单
				orderAuditPo.setUpdateType(Constant.UPDATE_TYPE_01);
			} else {//退订单
				orderAuditPo.setUpdateType(Constant.UPDATE_TYPE_02);
			}
			orderAuditPo.setReasonRemark(reasonRemark);
			if(JsSucessNO=="10041001" || "10041001".equals(JsSucessNO)){
				if(laoSucessNO=="60581001" || "60581001".equals(laoSucessNO)){
					if(oldVehicleId !=null && !"".equals(oldVehicleId)){
					orderAuditPo.setOld_customer_name(oldName);
					orderAuditPo.setOld_telephone(oldphone);
					orderAuditPo.setOld_vehicle_id(oldVehicleId);
					orderAuditPo.setOld_relation_code(laoSucessNO);
					}
				}else if(laoSucessNO=="60581002" || "60581002".equals(laoSucessNO)){
					if(oldphone !=null && !"".equals(oldphone)){
					orderAuditPo.setOld_customer_name(oldName);
					orderAuditPo.setOld_telephone(oldphone);
					orderAuditPo.setOld_relation_code(laoSucessNO);
					}
				}
			}
			dao.insert(orderAuditPo);
			
			if(typeflag.equals("update")) {//为修改订单时
				//取table1里的数据
				for(int i1=1;i1<=Integer.parseInt(table1Length);i1++) {
					String detailId = act.getRequest().getParamValue("hiddendetailrow"+i1);
					String oldOrderDetailID = act.getRequest().getParamValue("materialNamehrow"+i1);
					String model = act.getRequest().getParamValue("materialNamerow"+i1);//车型
					String color = act.getRequest().getParamValue("intent_colorrow"+i1);//意向颜色
					String price = act.getRequest().getParamValue("pricerow"+i1);//价格
					String num = act.getRequest().getParamValue("numrow"+i1);//数量
					String amount = act.getRequest().getParamValue("amountrow"+i1);//总金额
					String deposit = act.getRequest().getParamValue("depositrow"+i1);//订金
					String earnest = act.getRequest().getParamValue("earnestrow"+i1);//定金
					String pre_pay_date = act.getRequest().getParamValue("pre_pay_daterow"+i1);//余款付款日期
					String pre_delivery_date = act.getRequest().getParamValue("pre_delivery_daterow"+i1);//交车日期
					String delivery_num = act.getRequest().getParamValue("delivery_numberrow"+i1);//已交车数量
					if(model!=null&&!"".equals(model)) {
						//保存到订单审核子表
						String newDetailAuditId = SequenceManager.getSequence("");
						TPcOrderDetailAuditPO orderDetailAuditPo = new TPcOrderDetailAuditPO();
						orderDetailAuditPo.setOrderDetailAuditId(Long.parseLong(newDetailAuditId));
						orderDetailAuditPo.setOrderAuditId(Long.parseLong(newAuditId));
						orderDetailAuditPo.setOrderId(Long.parseLong(orderId));
						
						if(oldOrderDetailID!=null && !"".equals(oldOrderDetailID)) {
						orderDetailAuditPo.setOldOrderDetailId(Long.parseLong(oldOrderDetailID));
						}
						if(detailId!=null && !"".equals(detailId)) {
							orderDetailAuditPo.setOrderDetailId(Long.parseLong(detailId));
						}
						orderDetailAuditPo.setCustomerId(Long.parseLong(customerId));
						orderDetailAuditPo.setStatus(Constant.STATUS_ENABLE);
						orderDetailAuditPo.setCreateBy(logonUser.getUserId().toString());
						orderDetailAuditPo.setCreateDate(new Date());
						orderDetailAuditPo.setIntentModel(model);
						orderDetailAuditPo.setIntentColor(color);
						orderDetailAuditPo.setNum(Integer.parseInt(num));
						orderDetailAuditPo.setDeliveryDate(sdf.parse(pre_delivery_date));
						orderDetailAuditPo.setAmount(Float.parseFloat(amount));
						if(deposit!=null&&!"".equals(deposit)){
							orderDetailAuditPo.setDeposit(Float.parseFloat(deposit));
						} else {
							orderDetailAuditPo.setEarnest(Float.parseFloat(earnest));
						}
						
						orderDetailAuditPo.setBalanceDate(sdf.parse(pre_pay_date));
						
						//orderDetailAuditPo.setDeliveryNumber(Integer.parseInt(delivery_num));
						boolean intentFlag=CommonUtils.judgeIfForeign(model);
						if(intentFlag){
							orderDetailAuditPo.setDeliveryNumber(Integer.parseInt(num));
						}else{
							orderDetailAuditPo.setDeliveryNumber(Integer.parseInt(delivery_num));
						}
						
						orderDetailAuditPo.setPrice(Float.parseFloat(price));
						dao.insert(orderDetailAuditPo);
					}
					
				}
				
				//取table2里的数据
				for(int i1=1;i1<=Integer.parseInt(table2Length);i1++) {
					int colorRow = i1;
					String detailId = act.getRequest().getParamValue("yhiddendetailyrow"+i1);
					String vinId = act.getRequest().getParamValue("hiddenyvinIdyrow"+i1);//VinId
					String model2 = act.getRequest().getParamValue("hiddenymaterialCodeyrow"+i1);//车型
					String color2 = act.getRequest().getParamValue("ytheColoryvinIdyrow"+colorRow);//意向颜色
					String price2 = act.getRequest().getParamValue("ypriceyrow"+i1);//价格
					String num2 = act.getRequest().getParamValue("ynumyrow"+i1);//数量
					String amount2 = act.getRequest().getParamValue("yamountyrow"+i1);//总金额
					String deposit2 = act.getRequest().getParamValue("ydeposityrow"+i1);//订金
					String earnest2 = act.getRequest().getParamValue("yearnestyrow"+i1);//定金
					String pre_pay_date2 = act.getRequest().getParamValue("ypre_pay_dateyrow"+i1);//余款付款日期
					String pre_delivery_date2 = act.getRequest().getParamValue("ypre_delivery_dateyrow"+i1);//交车日期
					String delivery_num2 = act.getRequest().getParamValue("ydelivery_numberyrow"+i1);//已交车数量
					if(model2!=null&&!"".equals(model2)) {
						//保存到订单审核子表
						String newDetailAuditId = SequenceManager.getSequence("");
						TPcOrderDetailAuditPO orderDetailAuditPo2 = new TPcOrderDetailAuditPO();
						orderDetailAuditPo2.setOrderDetailAuditId(Long.parseLong(newDetailAuditId));
						orderDetailAuditPo2.setOrderAuditId(Long.parseLong(newAuditId));
						orderDetailAuditPo2.setOrderId(Long.parseLong(orderId));
						if(detailId!=null && !"".equals(detailId)) {
							orderDetailAuditPo2.setOrderDetailId(Long.parseLong(detailId));
						}
						orderDetailAuditPo2.setCustomerId(Long.parseLong(customerId));
						orderDetailAuditPo2.setStatus(Constant.STATUS_ENABLE);
						orderDetailAuditPo2.setCreateBy(logonUser.getUserId().toString());
						orderDetailAuditPo2.setCreateDate(new Date());
						orderDetailAuditPo2.setVin(vinId);
						orderDetailAuditPo2.setMaterial(Long.parseLong(model2));
						orderDetailAuditPo2.setColor(color2);
						orderDetailAuditPo2.setNum(Integer.parseInt(num2));
						orderDetailAuditPo2.setDeliveryDate(sdf.parse(pre_delivery_date2));
						orderDetailAuditPo2.setAmount(Float.parseFloat(amount2));
						if(deposit2!=null&&!"".equals(deposit2)){
							orderDetailAuditPo2.setDeposit(Float.parseFloat(deposit2));
						} else {
							orderDetailAuditPo2.setEarnest(Float.parseFloat(earnest2));
						}
						orderDetailAuditPo2.setBalanceDate(sdf.parse(pre_pay_date2));
						orderDetailAuditPo2.setDeliveryNumber(Integer.parseInt(delivery_num2));
						orderDetailAuditPo2.setPrice(Float.parseFloat(price2));
						dao.insert(orderDetailAuditPo2);
						//获取当前车辆状态
						TmVehiclePO td = new TmVehiclePO();
						td.setVehicleId(Long.parseLong(vinId));
						td=(TmVehiclePO) dao.select(td).get(0);
						
						//锁定车辆(修改车辆状态)
						if(Constant.VEHICLE_LIFE_03.toString().equals(td.getLifeCycle().toString())){
							TmVehiclePO oldVehiclePo = new TmVehiclePO();
							oldVehiclePo.setVehicleId(Long.parseLong(vinId));
							
							TmVehiclePO newVehiclePo = new TmVehiclePO();
							//newVehiclePo.setVehicleId(Long.parseLong(vinId));
							newVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_10);
							dao.update(oldVehiclePo, newVehiclePo);
						}
						
					}
				}
				//暂时标记提醒信息为锁定
				TPcOrderDetailPO detailPo = new TPcOrderDetailPO();
				detailPo.setOrderId(Long.parseLong(orderId));
				List<PO> po = dao.select(detailPo);
				for(int i=0;i<po.size();i++) {
					detailPo = (TPcOrderDetailPO)po.get(i);
					
					TPcRemindPO oldRemindPo = new TPcRemindPO();
					oldRemindPo.setBeremindId(detailPo.getOrderDetailId());
					oldRemindPo.setRemindType(Constant.REMIND_TYPE_09.toString());
					oldRemindPo.setRemindStatus(Constant.TASK_STATUS_01);
					TPcRemindPO newRemindPo = new TPcRemindPO();
					newRemindPo.setRemindStatus(Constant.TASK_STATUS_04);
					dao.update(oldRemindPo, newRemindPo);
				}
				 
				//新增提醒信息
				CommonUtils.addRemindInfo(Constant.REMIND_TYPE_13.toString(), orderId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
			} else {//退订单时
				//暂时标记提醒信息为锁定
				TPcOrderDetailPO detailPo = new TPcOrderDetailPO();
				detailPo.setOrderId(Long.parseLong(orderId));
				List<PO> po = dao.select(detailPo);
				for(int i=0;i<po.size();i++) {
					detailPo = (TPcOrderDetailPO)po.get(i);
					
					TPcRemindPO oldRemindPo = new TPcRemindPO();
					oldRemindPo.setBeremindId(detailPo.getOrderDetailId());
					oldRemindPo.setRemindType(Constant.REMIND_TYPE_09.toString());
					oldRemindPo.setRemindStatus(Constant.TASK_STATUS_01);
					TPcRemindPO newRemindPo = new TPcRemindPO();
					newRemindPo.setRemindStatus(Constant.TASK_STATUS_04);
					dao.update(oldRemindPo, newRemindPo);
				}
				//新增提醒信息
				CommonUtils.addRemindInfo(Constant.REMIND_TYPE_14.toString(), orderId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	

	/**
	 * 新增订单驳回重新修改提交处理方法
	 */
	public void orderBohUpdate() {
		ActionContext act = ActionContext.getContext();
		OrderManageDao dao = new OrderManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		//获取订单页面参数
		String orderId = act.getRequest().getParamValue("orderId");
		String customerId = act.getRequest().getParamValue("customerId");
		String typeflag = act.getRequest().getParamValue("typeflag");
		String ownerName = act.getRequest().getParamValue("owner_name");//车主姓名
		String ownerPhone = act.getRequest().getParamValue("owner_phone");//车主姓名
		String ownerPaperType = act.getRequest().getParamValue("owner_paper_type");//证件类型
		String ownerPaperNo = act.getRequest().getParamValue("owner_paper_no");//证件号码
		String dPro = CommonUtils.checkNull(act.getRequest().getParamValue("dPro"));//省
		String dCity = CommonUtils.checkNull(act.getRequest().getParamValue("dCity"));//市
		String dArea = CommonUtils.checkNull(act.getRequest().getParamValue("dArea"));//区
		String ownerAddress = act.getRequest().getParamValue("owner_address");//详细地址
		String newProductSale = act.getRequest().getParamValue("new_product_sale");//新产品预售
		String dealType = act.getRequest().getParamValue("deal_type");//成交类型
		String dealerCode = act.getRequest().getParamValue("dealerCode");//交车地点
		String testDriving = act.getRequest().getParamValue("test_driving");//试乘试驾
		String table1Length = act.getRequest().getParamValue("table1Length");//未确定车架号tableRow数
		String table2Length = act.getRequest().getParamValue("table2Length");//已确定车架号tableRow数
		String reasonRemark = act.getRequest().getParamValue("reason_remark");
		String AuditFlag = act.getRequest().getParamValue("AuditFlag");//判断订单是否需要提交经理审核
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			//判断订单状态（如若已提交订单审核则抛出异常）
			TPcOrderPO orderPo = new TPcOrderPO();
			orderPo.setOrderId(Long.parseLong(orderId));
			List<PO> poxx = dao.select(orderPo);
			orderPo = (TPcOrderPO)poxx.get(0);
			Integer orderStatus = orderPo.getOrderStatus();
			if(orderStatus == 60231008 || orderStatus == 60231008) {
				throw new Exception("该条数据已被处理过！");
			}

			CommonUtils.delOrderAuditAmount(orderId,null);//删除原始订单明细数据
			
			if(!AuditFlag.equals("sucess")){
			//修改订单内车主信息
				System.out.println("AuditFlag1111111111111111:"+AuditFlag);
			TPcOrderPO oldOrderPo = new TPcOrderPO();
			oldOrderPo.setOrderId(Long.parseLong(orderId));
			TPcOrderPO newOrderPo = new TPcOrderPO();
			newOrderPo.setOrderId(Long.parseLong(orderId));
			newOrderPo.setFinishDate(new Date());
			newOrderPo.setOwnerName(ownerName);
			newOrderPo.setOwnerPhone(ownerPhone);
			newOrderPo.setOwnerPaperType(ownerPaperType);
			newOrderPo.setOwnerPaperNo(ownerPaperNo);
			newOrderPo.setOwnerProvince(dPro);
			newOrderPo.setOwnerCity(dCity);
			newOrderPo.setOwnerArea(dArea);
			newOrderPo.setOwnerAddress(ownerAddress);
			newOrderPo.setNewProductSale(Integer.parseInt(newProductSale));
			newOrderPo.setDealType(dealType);
			newOrderPo.setDeliveryAddress(dealerCode);
			newOrderPo.setTestDriving(Integer.parseInt(testDriving));
			newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_08);
			
			dao.update(oldOrderPo, newOrderPo);
			
			//保存订单审核表
			String newAuditId = SequenceManager.getSequence("");
			TPcOrderAuditPO orderAuditPo = new TPcOrderAuditPO();
			orderAuditPo.setOrderAuditId(Long.parseLong(newAuditId));
			orderAuditPo.setOrderId(Long.parseLong(orderId));
			orderAuditPo.setCustomerId(Long.parseLong(customerId));
			orderAuditPo.setManagerAudit(Constant.DIRECTOR_AUDIT_01);
			orderAuditPo.setCreateBy(logonUser.getUserId().toString());
			orderAuditPo.setCreateDate(new Date());
			orderAuditPo.setStatus(Constant.STATUS_ENABLE);
			orderAuditPo.setUpdateType(Constant.UPDATE_TYPE_03);

			dao.insert(orderAuditPo);
			Date dateTime = new Date();//当前时间
			
				//取table1里的数据
				for(int i1=1;i1<=Integer.parseInt(table1Length);i1++) {
					String newTaskId = SequenceManager.getSequence("");
					String detailId = act.getRequest().getParamValue("hiddendetailrow"+i1);
					String oldOrderDetailID = act.getRequest().getParamValue("materialNamehrow"+i1);
					String model = act.getRequest().getParamValue("materialNamerow"+i1);//车型
					String color = act.getRequest().getParamValue("intent_colorrow"+i1);//意向颜色
					String price = act.getRequest().getParamValue("pricerow"+i1);//价格
					String num = act.getRequest().getParamValue("numrow"+i1);//数量
					String amount = act.getRequest().getParamValue("amountrow"+i1);//总金额
					String deposit = act.getRequest().getParamValue("depositrow"+i1);//订金
					String earnest = act.getRequest().getParamValue("earnestrow"+i1);//定金
					String pre_pay_date = act.getRequest().getParamValue("pre_pay_daterow"+i1);//余款付款日期
					String pre_delivery_date = act.getRequest().getParamValue("pre_delivery_daterow"+i1);//交车日期
					String delivery_num = act.getRequest().getParamValue("delivery_numberrow"+i1);//已交车数量
					if(model!=null&&!"".equals(model)) {
						//保存到订单详细子表
						TPcOrderDetailPO orderDetailPo = new TPcOrderDetailPO();
						orderDetailPo.setOrderDetailId(Long.parseLong(newTaskId));
						orderDetailPo.setOrderId(Long.parseLong(orderId));
						orderDetailPo.setCustomerId(Long.parseLong(customerId));
						orderDetailPo.setIntentModel(model);
						orderDetailPo.setIntentColor(color);
						orderDetailPo.setNum(Integer.parseInt(num));
						orderDetailPo.setCreateDate(dateTime);
						orderDetailPo.setOrderdDate(dateTime);
						orderDetailPo.setCreateBy(logonUser.getUserId().toString());
						orderDetailPo.setDeliveryDate(sdf.parse(pre_delivery_date));
						orderDetailPo.setAmount(Float.parseFloat(amount));
						if(deposit!=null && !"".equals(deposit)) {
							orderDetailPo.setDeposit(Float.parseFloat(deposit));
						} else {
							orderDetailPo.setEarnest(Float.parseFloat(earnest));
						}
						orderDetailPo.setBalanceDate(sdf.parse(pre_pay_date));
						boolean intentFlag=CommonUtils.judgeIfForeign(model);
						if(intentFlag){
							orderDetailPo.setDeliveryNumber(Integer.parseInt(num));
						}else{
							orderDetailPo.setDeliveryNumber(0);
						}
						
						orderDetailPo.setPrice(Float.parseFloat(price));
						orderDetailPo.setTaskStatus(Constant.TASK_STATUS_01);
						orderDetailPo.setPreviousTask(Long.parseLong(orderId));
						dao.insert(orderDetailPo);
						
					}
					
				}				
			
				//取table2里的数据
				for(int i1=1;i1<=Integer.parseInt(table2Length);i1++) {
					int colorRow = i1;
					String newTaskId2 = SequenceManager.getSequence("");
					String detailId = act.getRequest().getParamValue("yhiddendetailyrow"+i1);
					String vinId = act.getRequest().getParamValue("hiddenyvinIdyrow"+i1);//VinId
					String model2 = act.getRequest().getParamValue("hiddenymaterialCodeyrow"+i1);//车型
					String color2 = act.getRequest().getParamValue("ytheColoryvinIdyrow"+colorRow);//意向颜色
					String price2 = act.getRequest().getParamValue("ypriceyrow"+i1);//价格
					String num2 = act.getRequest().getParamValue("ynumyrow"+i1);//数量
					String amount2 = act.getRequest().getParamValue("yamountyrow"+i1);//总金额
					String deposit2 = act.getRequest().getParamValue("ydeposityrow"+i1);//订金
					String earnest2 = act.getRequest().getParamValue("yearnestyrow"+i1);//定金
					String pre_pay_date2 = act.getRequest().getParamValue("ypre_pay_dateyrow"+i1);//余款付款日期
					String pre_delivery_date2 = act.getRequest().getParamValue("ypre_delivery_dateyrow"+i1);//交车日期
					String delivery_num2 = act.getRequest().getParamValue("ydelivery_numberyrow"+i1);//已交车数量
					if(model2!=null&&!"".equals(model2)) {
						//保存到订单详细子表
						TPcOrderDetailPO orderDetailPo2 = new TPcOrderDetailPO();
						orderDetailPo2.setOrderDetailId(Long.parseLong(newTaskId2));
						orderDetailPo2.setOrderId(Long.parseLong(orderId));
						orderDetailPo2.setCustomerId(Long.parseLong(customerId));
						orderDetailPo2.setVehicleId(Long.parseLong(vinId));
						orderDetailPo2.setMaterial(Long.parseLong(model2));
						orderDetailPo2.setColor(color2);
						orderDetailPo2.setNum(Integer.parseInt(num2));
						orderDetailPo2.setCreateDate(new Date());
						orderDetailPo2.setCreateBy(logonUser.getUserId().toString());
						orderDetailPo2.setDeliveryDate(sdf.parse(pre_delivery_date2));
						orderDetailPo2.setAmount(Float.parseFloat(amount2));
						if(deposit2!=null && !"".equals(deposit2)) {
							orderDetailPo2.setDeposit(Float.parseFloat(deposit2));
						} else {
							orderDetailPo2.setEarnest(Float.parseFloat(earnest2));
						}
						orderDetailPo2.setBalanceDate(sdf.parse(pre_pay_date2));
						orderDetailPo2.setDeliveryNumber(0);
						orderDetailPo2.setPrice(Float.parseFloat(price2));
						orderDetailPo2.setTaskStatus(Constant.TASK_STATUS_01);
						orderDetailPo2.setPreviousTask(Long.parseLong(orderId));
						dao.insert(orderDetailPo2);
						
					}
				}
				//暂时标记提醒信息为锁定
				TPcOrderDetailPO detailPo = new TPcOrderDetailPO();
				detailPo.setOrderId(Long.parseLong(orderId));
				List<PO> po = dao.select(detailPo);
				for(int i=0;i<po.size();i++) {
					detailPo = (TPcOrderDetailPO)po.get(i);
					
					TPcRemindPO oldRemindPo = new TPcRemindPO();
					oldRemindPo.setBeremindId(detailPo.getOrderDetailId());
					oldRemindPo.setRemindType(Constant.REMIND_TYPE_09.toString());
					oldRemindPo.setRemindStatus(Constant.TASK_STATUS_01);
					TPcRemindPO newRemindPo = new TPcRemindPO();
					newRemindPo.setRemindStatus(Constant.TASK_STATUS_04);
					dao.update(oldRemindPo, newRemindPo);
				}
			//新增提醒信息
			CommonUtils.addRemindInfo(Constant.REMIND_TYPE_21.toString(), orderId, customerId, logonUser.getDealerId(), "", sdf.format(new Date()),"");
			}else{
				//标记本次订单主表任务完成
				TPcOrderPO oldOrderPo = new TPcOrderPO();
				oldOrderPo.setOrderId(Long.parseLong(orderId));
				TPcOrderPO newOrderPo = new TPcOrderPO();
				newOrderPo.setOrderId(Long.parseLong(orderId));
				newOrderPo.setTaskStatus(Constant.TASK_STATUS_02);
				newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);//设置订单信息为有效
				newOrderPo.setFinishDate(new Date());
				newOrderPo.setOwnerName(ownerName);
				newOrderPo.setOwnerPhone(ownerPhone);
				newOrderPo.setOwnerPaperType(ownerPaperType);
				newOrderPo.setOwnerPaperNo(ownerPaperNo);
				newOrderPo.setOwnerProvince(dPro);
				newOrderPo.setOwnerCity(dCity);
				newOrderPo.setOwnerArea(dArea);
				newOrderPo.setOwnerAddress(ownerAddress);
				newOrderPo.setNewProductSale(Integer.parseInt(newProductSale));
				newOrderPo.setDealType(dealType);
				newOrderPo.setTestDriving(Integer.parseInt(testDriving));
				dao.update(oldOrderPo, newOrderPo);
				
				//修改客户档案信息
				TPcCustomerPO oldCustomerPo = new TPcCustomerPO();
				oldCustomerPo.setCustomerId(Long.parseLong(customerId));
				TPcCustomerPO newCustomerPo = new TPcCustomerPO();
				newCustomerPo.setCustomerId(Long.parseLong(customerId));
				newCustomerPo.setBuyWay(dealType);
				newCustomerPo.setIfDrive(Long.parseLong(testDriving));
				dao.update(oldCustomerPo,newCustomerPo);
				
				//增加接触点信息
				CommonUtils.addContackPoint(Constant.POINT_WAY_07, "新增订单", customerId, logonUser.getUserId().toString(), logonUser.getDealerId());
				Date dateTime = new Date();//当前时间
				 
				//新增下次交车任务
				//取table1里的数据
				//取table1里的数据
				for(int i1=1;i1<=Integer.parseInt(table1Length);i1++) {
					String newTaskId = SequenceManager.getSequence("");
					String detailId = act.getRequest().getParamValue("hiddendetailrow"+i1);
					String oldOrderDetailID = act.getRequest().getParamValue("materialNamehrow"+i1);
					String model = act.getRequest().getParamValue("materialNamerow"+i1);//车型
					String color = act.getRequest().getParamValue("intent_colorrow"+i1);//意向颜色
					String price = act.getRequest().getParamValue("pricerow"+i1);//价格
					String num = act.getRequest().getParamValue("numrow"+i1);//数量
					String amount = act.getRequest().getParamValue("amountrow"+i1);//总金额
					String deposit = act.getRequest().getParamValue("depositrow"+i1);//订金
					String earnest = act.getRequest().getParamValue("earnestrow"+i1);//定金
					String pre_pay_date = act.getRequest().getParamValue("pre_pay_daterow"+i1);//余款付款日期
					String pre_delivery_date = act.getRequest().getParamValue("pre_delivery_daterow"+i1);//交车日期
					String delivery_num = act.getRequest().getParamValue("delivery_numberrow"+i1);//已交车数量
					if(model!=null&&!"".equals(model)) {
						//保存到订单详细子表
						System.out.println("detailId:"+detailId);
						System.out.println("orderId:"+orderId);
						TPcOrderDetailPO orderDetailPo = new TPcOrderDetailPO();
						orderDetailPo.setOrderDetailId(Long.parseLong(newTaskId));
						orderDetailPo.setOrderId(Long.parseLong(orderId));
						orderDetailPo.setCustomerId(Long.parseLong(customerId));
						orderDetailPo.setIntentModel(model);
						orderDetailPo.setIntentColor(color);
						orderDetailPo.setNum(Integer.parseInt(num));
						orderDetailPo.setCreateDate(dateTime);
						orderDetailPo.setOrderdDate(dateTime);
						orderDetailPo.setCreateBy(logonUser.getUserId().toString());
						orderDetailPo.setDeliveryDate(sdf.parse(pre_delivery_date));
						orderDetailPo.setAmount(Float.parseFloat(amount));
						if(deposit!=null && !"".equals(deposit)) {
							orderDetailPo.setDeposit(Float.parseFloat(deposit));
						} else {
							orderDetailPo.setEarnest(Float.parseFloat(earnest));
						}
						orderDetailPo.setBalanceDate(sdf.parse(pre_pay_date));
						boolean intentFlag=CommonUtils.judgeIfForeign(model);
						if(intentFlag){
							orderDetailPo.setDeliveryNumber(Integer.parseInt(num));
						}else{
							orderDetailPo.setDeliveryNumber(0);
						}
						orderDetailPo.setPrice(Float.parseFloat(price));
						orderDetailPo.setTaskStatus(Constant.TASK_STATUS_01);
						orderDetailPo.setPreviousTask(Long.parseLong(orderId));
						dao.insert(orderDetailPo);
						
						if(!intentFlag){
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_09.toString(), newTaskId, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), pre_delivery_date, num);
						}
					}
				}
				//取table2里的数据
				for(int i1=1;i1<=Integer.parseInt(table2Length);i1++) {
					int colorRow = i1;
					String newTaskId2 = SequenceManager.getSequence("");
					String detailId = act.getRequest().getParamValue("yhiddendetailyrow"+i1);
					String vinId = act.getRequest().getParamValue("hiddenyvinIdyrow"+i1);//VinId
					String model2 = act.getRequest().getParamValue("hiddenymaterialCodeyrow"+i1);//车型
					String color2 = act.getRequest().getParamValue("ytheColoryvinIdyrow"+colorRow);//意向颜色
					String price2 = act.getRequest().getParamValue("ypriceyrow"+i1);//价格
					String num2 = act.getRequest().getParamValue("ynumyrow"+i1);//数量
					String amount2 = act.getRequest().getParamValue("yamountyrow"+i1);//总金额
					String deposit2 = act.getRequest().getParamValue("ydeposityrow"+i1);//订金
					String earnest2 = act.getRequest().getParamValue("yearnestyrow"+i1);//定金
					String pre_pay_date2 = act.getRequest().getParamValue("ypre_pay_dateyrow"+i1);//余款付款日期
					String pre_delivery_date2 = act.getRequest().getParamValue("ypre_delivery_dateyrow"+i1);//交车日期
					String delivery_num2 = act.getRequest().getParamValue("ydelivery_numberyrow"+i1);//已交车数量
					if(model2!=null&&!"".equals(model2)) {
						//保存到订单详细子表
						TPcOrderDetailPO orderDetailPo2 = new TPcOrderDetailPO();
						orderDetailPo2.setOrderDetailId(Long.parseLong(newTaskId2));
						orderDetailPo2.setOrderId(Long.parseLong(orderId));
						orderDetailPo2.setCustomerId(Long.parseLong(customerId));
						orderDetailPo2.setVehicleId(Long.parseLong(vinId));
						orderDetailPo2.setMaterial(Long.parseLong(model2));
						orderDetailPo2.setColor(color2);
						orderDetailPo2.setNum(Integer.parseInt(num2));
						orderDetailPo2.setCreateDate(new Date());
						orderDetailPo2.setCreateBy(logonUser.getUserId().toString());
						orderDetailPo2.setDeliveryDate(sdf.parse(pre_delivery_date2));
						orderDetailPo2.setAmount(Float.parseFloat(amount2));
						if(deposit2!=null && !"".equals(deposit2)) {
							orderDetailPo2.setDeposit(Float.parseFloat(deposit2));
						} else {
							orderDetailPo2.setEarnest(Float.parseFloat(earnest2));
						}
						orderDetailPo2.setBalanceDate(sdf.parse(pre_pay_date2));
						orderDetailPo2.setDeliveryNumber(0);
						orderDetailPo2.setPrice(Float.parseFloat(price2));
						orderDetailPo2.setTaskStatus(Constant.TASK_STATUS_01);
						orderDetailPo2.setPreviousTask(Long.parseLong(orderId));
						dao.insert(orderDetailPo2);
						
						
						//锁定车辆(修改车辆状态)
						TmVehiclePO oldVehiclePo = new TmVehiclePO();
						oldVehiclePo.setVehicleId(Long.parseLong(vinId));
						TmVehiclePO newVehiclePo = new TmVehiclePO();
						newVehiclePo.setVehicleId(Long.parseLong(vinId));
						newVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_10);
						dao.update(oldVehiclePo, newVehiclePo);
						
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_09.toString(), newTaskId2, customerId, logonUser.getDealerId(), logonUser.getUserId().toString(), pre_delivery_date2, num2);
					}
				}
				//标记提醒信息为已完成
				CommonUtils.setRemindDone(orderId,Constant.REMIND_TYPE_08.toString());
				}
			} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订单数量审核初始页面
	 */
	public void doAuditAmountInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		try {
			String adviser = null;
			String managerLogon = "yes";
			String adviserLogon = "no";
			String userId = null;
			
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				managerLogon = "no";
				adviserLogon = "yes";
			} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
				managerLogon = "no";
				adviserLogon = "no";
				//获取主管下属分组的所有顾问
				userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
			} else if(CommonUtils.judgeDcrcLogin(logonUser.getUserId().toString())){//判断是否DCRC登陆
				managerLogon = "no";
				adviserLogon = "no";
			} 
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				adviser = logonUser.getUserId().toString();
			}
			//获取顾问列表
			List<DynaBean> adviserList = dao.getAdviserBydealer2(logonUser.getDealerId(),userId);
			//获取分组列表
			List<DynaBean> groupList = dao.getGroupBydealer(logonUser.getDealerId());
			
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			act.setOutData("funcStr", funcStr);
			act.setOutData("adviserList", adviserList);
			act.setOutData("groupList", groupList);
			act.setOutData("managerLogon", managerLogon);
			act.setOutData("adviserLogon", adviserLogon);
			act.setForword(ORDER_AUDIT_AMOUNT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单数量审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订单修改/退单审核初始页面
	 */
	public void doAuditInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		try {
			String adviser = null;
			String managerLogon = "yes";
			String adviserLogon = "no";
			String userId = null;
			
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				managerLogon = "no";
				adviserLogon = "yes";
			} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
				managerLogon = "no";
				adviserLogon = "no";
				//获取主管下属分组的所有顾问
				userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
			} else if(CommonUtils.judgeDcrcLogin(logonUser.getUserId().toString())){//判断是否DCRC登陆
				managerLogon = "no";
				adviserLogon = "no";
			} 
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				adviser = logonUser.getUserId().toString();
			}
			String dutyType= logonUser.getDutyType().toString();
			//获取顾问列表
			List<DynaBean> adviserList = dao.getAdviserBydealer2(logonUser.getDealerId(),userId);
			//获取分组列表
			List<DynaBean> groupList = dao.getGroupBydealer(logonUser.getDealerId());
			
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			act.setOutData("funcStr", funcStr);
			act.setOutData("adviserList", adviserList);
			act.setOutData("groupList", groupList);
			act.setOutData("managerLogon", managerLogon);
			act.setOutData("adviserLogon", adviserLogon);
			act.setOutData("dutyType", dutyType);
			act.setForword(ORDER_AUDIT_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "进入订单修改/退单审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订单数量审核页面
	 */
	public void orderAmountAuditInit() {
		ActionContext act = ActionContext.getContext();
		OrderManageDao dao = new OrderManageDao();
		TaskManageDao dao2 = new TaskManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String orderId = act.getRequest().getParamValue("orderId");
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		try {
			//获取客户基本信息
			List<DynaBean> customerList = dao.getCustomerInfo(orderId);
			//获取车主基本信息
			List<DynaBean> ownerList = dao2.getOwnerInfo(orderId);
			//获取订单状态用于前台判断是否能进行退单操作（为部分交车不能 退单）
			String orderStatus = ownerList.get(0).get("ORDER_STATUS").toString();
			//获取意向颜色列表
			//List<DynaBean> colorList = dao2.getColorList();
			//获取省市区
			String oldPro = null;
			String oldCity = null;
			String oldArea = null;
			if(ownerList.get(0).get("PRO2")!=null && !"".equals(ownerList.get(0).get("PRO2"))) {
				oldPro = ownerList.get(0).get("PRO2").toString();
			}
			if(ownerList.get(0).get("CITY2")!=null && !"".equals(ownerList.get(0).get("CITY2"))) {
				oldCity = ownerList.get(0).get("CITY2").toString();
			}
			if(ownerList.get(0).get("AREA2")!=null && !"".equals(ownerList.get(0).get("AREA2"))) {
				oldArea = ownerList.get(0).get("AREA2").toString();
			}
			//获取Table1信息
			List<DynaBean> table1List = dao2.getTable1Info(orderId,orderStatus);
			//获取Table2信息
			List<DynaBean> table2List = dao2.getTable2Info(orderId,orderStatus);
			
			int size1 = table1List.size();
			int size2 = table2List.size();
			
			act.setOutData("orderId", orderId);
			act.setOutData("oldPro", oldPro);
			act.setOutData("oldCity", oldCity);
			act.setOutData("oldArea", oldArea);
			act.setOutData("nowDate", nowDate);
			act.setOutData("orderStatus", orderStatus);
			//act.setOutData("colorList", colorList);
			act.setOutData("customerList", customerList);
			act.setOutData("ownerList", ownerList);
			act.setOutData("table1List", table1List);
			act.setOutData("table2List", table2List);
			act.setOutData("size1", size1);
			act.setOutData("size2", size2);
			
			act.setForword(ORDER_AMOUNT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订单数量审核
	 */
	public void orderAmountAudit() {
		ActionContext act = ActionContext.getContext();
		OrderManageDao dao = new OrderManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String orderId = act.getRequest().getParamValue("orderId");
		String orderStatus = act.getRequest().getParamValue("orderStatus");
		String if_agree = act.getRequest().getParamValue("if_agree"); //是否同意
		String audit_remark = act.getRequest().getParamValue("audit_remark"); //经理审核意见
		String reason_remark = act.getRequest().getParamValue("audit_remark"); //修改、退单说明
		String table1Length = act.getRequest().getParamValue("table1Length");//未确定车架号tableRow数
		String table2Length = act.getRequest().getParamValue("table2Length");//已确定车架号tableRow数
		String reasonRemark = act.getRequest().getParamValue("reason_remark");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTime=new Date();
		
		//获取customerId
		TPcOrderPO orderpo = new TPcOrderPO();
		orderpo.setOrderId(Long.parseLong(orderId));
		List<PO> po00 = dao.select(orderpo);
		orderpo = (TPcOrderPO)po00.get(0);
		String customerId = orderpo.getCustomerId().toString();
		//获取adviser
		TPcCustomerPO customerpo = new TPcCustomerPO();
		customerpo.setCustomerId(Long.parseLong(customerId));
		List<PO> po0 = dao.select(customerpo);
		customerpo = (TPcCustomerPO)po0.get(0);
		String adviser = customerpo.getAdviser().toString();
		try {
			//判断订单状态（如若已提交订单审核则抛出异常）
			TPcOrderPO oPo = new TPcOrderPO();
			oPo.setOrderId(Long.parseLong(orderId));
			List<PO> poxx = dao.select(oPo);
			oPo = (TPcOrderPO)poxx.get(0);
			Integer oStatus = oPo.getOrderStatus();
			if(oStatus != 60231008 ) {
				throw new Exception("该条数据已被处理过！");
			}
			//修改订单审核表
			TPcOrderAuditPO oldOrderAuditPo = new TPcOrderAuditPO();
			oldOrderAuditPo.setOrderId(Long.parseLong(orderId));
			oldOrderAuditPo.setManagerAudit(Constant.DIRECTOR_AUDIT_01);
			TPcOrderAuditPO newOrderAuditPo = new TPcOrderAuditPO();
			newOrderAuditPo.setOrderId(Long.parseLong(orderId));
			if(if_agree=="yes"||"yes".equals(if_agree)) {
				newOrderAuditPo.setManagerAudit(Constant.DIRECTOR_AUDIT_02);//审核通过
			} else if(if_agree=="no"||"no".equals(if_agree)) {
				newOrderAuditPo.setManagerAudit(Constant.DIRECTOR_AUDIT_03);//审核驳回
			}
			newOrderAuditPo.setUpdateDate(dateTime);
			newOrderAuditPo.setUpdateBy(logonUser.getUserId().toString());
			newOrderAuditPo.setUpdateType(Constant.UPDATE_TYPE_03);//数量审核
			//newOrderAuditPo.setManagerAuditRemark(audit_remark);
			dao.update(oldOrderAuditPo, newOrderAuditPo);
			
			
			//取table1里的数据
			for(int i1=1;i1<=Integer.parseInt(table1Length);i1++) {
				String detailId = act.getRequest().getParamValue("hiddendetailrow"+i1);
				String oldOrderDetailID = act.getRequest().getParamValue("materialNamehrow"+i1);
				String model = act.getRequest().getParamValue("materialNamerow"+i1);//车型
				String color = act.getRequest().getParamValue("intent_colorrow"+i1);//意向颜色
				String price = act.getRequest().getParamValue("pricerow"+i1);//价格
				String num = act.getRequest().getParamValue("numrow"+i1);//数量
				String amount = act.getRequest().getParamValue("amountrow"+i1);//总金额
				String deposit = act.getRequest().getParamValue("depositrow"+i1);//订金
				String earnest = act.getRequest().getParamValue("earnestrow"+i1);//定金
				String pre_pay_date = act.getRequest().getParamValue("pre_pay_daterow"+i1);//余款付款日期
				String pre_delivery_date = act.getRequest().getParamValue("pre_delivery_daterow"+i1);//交车日期
				String delivery_num = act.getRequest().getParamValue("delivery_numberrow"+i1);//已交车数量
				
				if(detailId!=null&&!"".equals(detailId)){
				TPcOrderDetailPO orderdePo = new TPcOrderDetailPO();
				orderdePo.setOrderDetailId(Long.parseLong(detailId));
				List<PO> list = dao.select(orderdePo);
				orderdePo = (TPcOrderDetailPO)list.get(0);
				String taskStatus = orderdePo.getTaskStatus().toString();
				if(taskStatus!=null&&!"".equals(taskStatus)){
					TPcOrderDetailPO oldOrderdePo1 = new TPcOrderDetailPO();
					oldOrderdePo1.setOrderDetailId(Long.parseLong(detailId));
					TPcOrderDetailPO newOrderdePo1 = new TPcOrderDetailPO();
					newOrderdePo1.setNum(Integer.valueOf(num));
					newOrderdePo1.setAmount(Float.valueOf(amount));
					newOrderdePo1.setTaskStatus(Constant.TASK_STATUS_01);
					newOrderdePo1.setBalanceDate(sdf.parse(pre_pay_date));
					newOrderdePo1.setDeliveryDate(sdf.parse(pre_delivery_date));
					newOrderdePo1.setUpdateDate(dateTime);
					dao.update(oldOrderdePo1, newOrderdePo1);
				}
				
				}
				
				}
			
			//取table2里的数据
			for(int i1=1;i1<=Integer.parseInt(table2Length);i1++) {
				int colorRow = i1;
				String detailId = act.getRequest().getParamValue("yhiddendetailyrow"+i1);
				String vinId = act.getRequest().getParamValue("hiddenyvinIdyrow"+i1);//VinId
				String model2 = act.getRequest().getParamValue("hiddenymaterialCodeyrow"+i1);//车型
				String color2 = act.getRequest().getParamValue("ytheColoryvinIdyrow"+colorRow);//意向颜色
				String price2 = act.getRequest().getParamValue("ypriceyrow"+i1);//价格
				String num2 = act.getRequest().getParamValue("ynumyrow"+i1);//数量
				String amount2 = act.getRequest().getParamValue("yamountyrow"+i1);//总金额
				String deposit2 = act.getRequest().getParamValue("ydeposityrow"+i1);//订金
				String earnest2 = act.getRequest().getParamValue("yearnestyrow"+i1);//定金
				String pre_pay_date2 = act.getRequest().getParamValue("ypre_pay_dateyrow"+i1);//余款付款日期
				String pre_delivery_date2 = act.getRequest().getParamValue("ypre_delivery_dateyrow"+i1);//交车日期
				String delivery_num2 = act.getRequest().getParamValue("ydelivery_numberyrow"+i1);//已交车数量
				
				if(detailId!=null&&!"".equals(detailId)){
					TPcOrderDetailPO orderdePo = new TPcOrderDetailPO();
					orderdePo.setOrderDetailId(Long.parseLong(detailId));
					List<PO> list = dao.select(orderdePo);
					orderdePo = (TPcOrderDetailPO)list.get(0);
					String taskStatus = orderdePo.getTaskStatus().toString();
					
					if(taskStatus!=null&&!"".equals(taskStatus)){
						TPcOrderDetailPO oldOrderdePo2 = new TPcOrderDetailPO();
						oldOrderdePo2.setOrderDetailId(Long.parseLong(detailId));
						TPcOrderDetailPO newOrderdePo2 = new TPcOrderDetailPO();
						newOrderdePo2.setTaskStatus(Constant.TASK_STATUS_01);
						newOrderdePo2.setBalanceDate(sdf.parse(pre_pay_date2));
						newOrderdePo2.setDeliveryDate(sdf.parse(pre_delivery_date2));
						newOrderdePo2.setUpdateDate(dateTime);
						dao.update(oldOrderdePo2, newOrderdePo2);
					}
			
				}
			}
		
			
			TPcOrderPO oldOrderpo = new TPcOrderPO();
			oldOrderpo.setOrderId(Long.parseLong(orderId));
			TPcOrderPO newOrderpo = new TPcOrderPO();
			if(if_agree=="no"||"no".equals(if_agree)) {
				newOrderpo.setOrderStatus(Constant.TPC_ORDER_STATUS_11);//审核驳回
			}else{
			newOrderpo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);//审核通过
			}
			dao.update(oldOrderpo, newOrderpo);
			//标记提醒信息为已完成
			CommonUtils.setRemindDone(orderId,Constant.REMIND_TYPE_21.toString());
		
			doAuditAmountInit();
			act.setForword(ORDER_AUDIT_AMOUNT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 订单修改/退单审核页面
	 */
	public void orderAuditInit() {
		ActionContext act = ActionContext.getContext();
		OrderManageDao dao = new OrderManageDao();
		TaskManageDao dao2 = new TaskManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String orderId = act.getRequest().getParamValue("orderId");
		String orderStatus = act.getRequest().getParamValue("orderStatus");
		String dutyType= logonUser.getDutyType(logonUser.getDealerId()).toString();
		Date date = new Date();//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(date);
		try {
			//获取客户基本信息
			List<DynaBean> customerList = dao.getCustomerInfo(orderId);
			//获取车主基本信息
			List<DynaBean> ownerList = dao2.getOwnerInfo(orderId);
			//获取省市区
			String oldPro = null;
			String oldCity = null;
			String oldArea = null;
			if(ownerList.get(0).get("PRO2")!=null && !"".equals(ownerList.get(0).get("PRO2"))) {
				oldPro = ownerList.get(0).get("PRO2").toString();
			}
			if(ownerList.get(0).get("CITY2")!=null && !"".equals(ownerList.get(0).get("CITY2"))) {
				oldCity = ownerList.get(0).get("CITY2").toString();
			}
			if(ownerList.get(0).get("AREA2")!=null && !"".equals(ownerList.get(0).get("AREA2"))) {
				oldArea = ownerList.get(0).get("AREA2").toString();
			}
			//获取Table1信息
			List<DynaBean> table1List = dao2.getTable1InfoByAudit(orderId);
			//获取Table2信息
			List<DynaBean> table2List = dao2.getTable2InfoByAudit(orderId);
			//获取订单审核信息
			List<DynaBean> orderAuditList = dao.getOrderAuditInfo(orderId);
			//获取修改/退订原因说明
			String reasonRemark = "";
			String oldCustomerName=null;
			String oldTelephone=null;
			String oldVehicleId=null;
			String relation_code=null;
			
			if(orderAuditList.get(0).get("REASON_REMARK")!=null && !"".equals(orderAuditList.get(0).get("REASON_REMARK"))) {
				reasonRemark = orderAuditList.get(0).get("REASON_REMARK").toString();
			}
			if(orderAuditList.get(0).get("OLD_CUSTOMER_NAME")!=null && !"".equals(orderAuditList.get(0).get("OLD_CUSTOMER_NAME"))) {
				oldCustomerName = orderAuditList.get(0).get("OLD_CUSTOMER_NAME").toString();
			}
			if(orderAuditList.get(0).get("OLD_TELEPHONE")!=null && !"".equals(orderAuditList.get(0).get("OLD_TELEPHONE"))) {
				oldTelephone = orderAuditList.get(0).get("OLD_TELEPHONE").toString();
			}
			if(orderAuditList.get(0).get("OLD_VEHICLE_ID")!=null && !"".equals(orderAuditList.get(0).get("OLD_VEHICLE_ID"))) {
				oldVehicleId = orderAuditList.get(0).get("OLD_VEHICLE_ID").toString();
			}
			if(orderAuditList.get(0).get("OLD_RELATION_CODE")!=null && !"".equals(orderAuditList.get(0).get("OLD_RELATION_CODE"))) {
				relation_code = orderAuditList.get(0).get("OLD_RELATION_CODE").toString();
			}
		
			
			act.setOutData("orderId", orderId);
			act.setOutData("oldPro", oldPro);
			act.setOutData("oldCity", oldCity);
			act.setOutData("oldArea", oldArea);
			act.setOutData("nowDate", nowDate);
			act.setOutData("reasonRemark", reasonRemark);
			act.setOutData("oldCustomerName", oldCustomerName);
			act.setOutData("oldTelephone", oldTelephone);
			act.setOutData("oldVehicleId", oldVehicleId);
			act.setOutData("relation_code", relation_code);
			act.setOutData("orderStatus", orderStatus);
			act.setOutData("customerList", customerList);
			act.setOutData("ownerList", ownerList);
			act.setOutData("table1List", table1List);
			act.setOutData("table2List", table2List);
			act.setOutData("dutyType", dutyType);
			
			act.setForword(ORDER_AUDIT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订单修改/退单审核 区域经理审核
	 */
	public void doAreaAudit() {
		ActionContext act = ActionContext.getContext();
		OrderManageDao dao = new OrderManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String orderId = act.getRequest().getParamValue("orderId");
		String orderStatus = act.getRequest().getParamValue("orderStatus");
		String if_agree = act.getRequest().getParamValue("if_agree"); //是否同意
		String audit_remark = act.getRequest().getParamValue("audit_remark"); //经理审核意见
		String reason_remark = act.getRequest().getParamValue("audit_remark"); //修改、退单说明
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTime=new Date();
		//获取customerId
		TPcOrderPO orderpo = new TPcOrderPO();
		orderpo.setOrderId(Long.parseLong(orderId));
		List<PO> po00 = dao.select(orderpo);
		orderpo = (TPcOrderPO)po00.get(0);
		String customerId = orderpo.getCustomerId().toString();
		//获取adviser
		TPcCustomerPO customerpo = new TPcCustomerPO();
		customerpo.setCustomerId(Long.parseLong(customerId));
		List<PO> po0 = dao.select(customerpo);
		customerpo = (TPcCustomerPO)po0.get(0);
		String adviser = customerpo.getAdviser().toString();
		try {
			//判断订单状态（如若已提交订单审核则抛出异常）
			TPcOrderPO oPo = new TPcOrderPO();
			oPo.setOrderId(Long.parseLong(orderId));
			List<PO> poxx = dao.select(oPo);
			oPo = (TPcOrderPO)poxx.get(0);
			Integer oStatus = oPo.getOrderStatus();
			if(oStatus != 60231009 && oStatus != 60231010) {
				throw new Exception("该条数据已被处理过！");
			}
			//修改订单审核表
			TPcOrderAuditPO oldOrderAuditPo = new TPcOrderAuditPO();
			oldOrderAuditPo.setOrderId(Long.parseLong(orderId));
			oldOrderAuditPo.setManagerAudit(Constant.DIRECTOR_AUDIT_01);
			TPcOrderAuditPO newOrderAuditPo = new TPcOrderAuditPO();
			newOrderAuditPo.setOrderId(Long.parseLong(orderId));
			if(if_agree=="yes"||"yes".equals(if_agree)) {
				newOrderAuditPo.setManagerAudit(Constant.DIRECTOR_AUDIT_02);//审核通过
			} else if(if_agree=="no"||"no".equals(if_agree)) {
				newOrderAuditPo.setManagerAudit(Constant.DIRECTOR_AUDIT_03);//审核驳回
			}
			newOrderAuditPo.setUpdateDate(dateTime);
			newOrderAuditPo.setUpdateBy(logonUser.getUserId().toString());
			if(orderStatus == "60231009" || "60231009".equals(orderStatus)) {
				newOrderAuditPo.setUpdateType(Constant.UPDATE_TYPE_01);//修改
			} else {
				newOrderAuditPo.setUpdateType(Constant.UPDATE_TYPE_01);//退单
			}
			newOrderAuditPo.setManagerAuditRemark(audit_remark);
			dao.update(oldOrderAuditPo, newOrderAuditPo);
			//退单审核
			if(orderStatus == "60231010" || "60231010".equals(orderStatus)) {
				//审核通过
				if(if_agree=="yes"||"yes".equals(if_agree)) {
					//解锁车辆(修改车辆状态)
					TPcOrderDetailPO orderPo = new TPcOrderDetailPO();
					orderPo.setOrderId(Long.parseLong(orderId));
					List<PO> list = dao.select(orderPo);
					TPcOrderDetailPO orderPo2 = null;
					Long vinId = 0L;
					for(int i=0;i<list.size();i++) {
						orderPo2 = (TPcOrderDetailPO)list.get(i);
						vinId = orderPo2.getVehicleId();
						if(vinId!=0){
							TmVehiclePO oldVehiclePo = new TmVehiclePO();
							oldVehiclePo.setVehicleId(vinId);
							TmVehiclePO newVehiclePo = new TmVehiclePO();
							//newVehiclePo.setVehicleId(vinId);
							newVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_03);
							dao.update(oldVehiclePo, newVehiclePo);
						}
					}
					//将订单详细表里的数据设为无效
					TPcOrderDetailPO oldOrderDetailPo = new TPcOrderDetailPO();
					TPcOrderDetailPO newOrderDetailPo = new TPcOrderDetailPO();
					oldOrderDetailPo.setOrderId(Long.parseLong(orderId));
					oldOrderDetailPo.setTaskStatus(Constant.TASK_STATUS_01);
					newOrderDetailPo.setOrderId(Long.parseLong(orderId));
					newOrderDetailPo.setTaskStatus(Constant.TASK_STATUS_03);
					newOrderDetailPo.setOrderdDate(dateTime);
					newOrderDetailPo.setUpdateDate(dateTime);
					newOrderDetailPo.setUpdateBy(logonUser.getUserId().toString());
					dao.update(oldOrderDetailPo, newOrderDetailPo);
					//修改订单主表(标记任务为未完成,并且订单状态为退单 )
					TPcOrderPO oldOrderPo2 = new TPcOrderPO();
					oldOrderPo2.setOrderId(Long.parseLong(orderId));
					TPcOrderPO newOrderPo2 = new TPcOrderPO();
					newOrderPo2.setOrderId(Long.parseLong(orderId));
					newOrderPo2.setTaskStatus(Constant.TASK_STATUS_01);
					newOrderPo2.setOrderStatus(Constant.TPC_ORDER_STATUS_07);
					newOrderPo2.setFinishDate(null);
					newOrderPo2.setOrderDate(dateTime);
					dao.update(oldOrderPo2, newOrderPo2);
					
					//将为锁定的提醒信息标记为无效
					TPcOrderDetailPO detailPo = new TPcOrderDetailPO();
					detailPo.setOrderId(Long.parseLong(orderId));
					List<PO> po = dao.select(detailPo);
					for(int i=0;i<po.size();i++) {
						detailPo = (TPcOrderDetailPO)po.get(i);
						
						TPcRemindPO oldRemindPo = new TPcRemindPO();
						oldRemindPo.setBeremindId(detailPo.getOrderDetailId());
						oldRemindPo.setRemindType(Constant.REMIND_TYPE_09.toString());
						oldRemindPo.setRemindStatus(Constant.TASK_STATUS_04);
						TPcRemindPO newRemindPo = new TPcRemindPO();
						newRemindPo.setRemindStatus(Constant.TASK_STATUS_03);
						dao.update(oldRemindPo, newRemindPo);
					}
					//增加接触点信息
					CommonUtils.addContackPoint(Constant.POINT_WAY_13, reason_remark, customerId, adviser, logonUser.getDealerId());
					//新增提醒信息
					CommonUtils.addRemindInfo(Constant.REMIND_TYPE_18.toString(), orderId, customerId, logonUser.getDealerId(), adviser, sdf.format(dateTime),"");
				} else {//审核驳回
					//获取原订单状态(completeList>0为部分交车，否则为有效)
					List<DynaBean> completeList = dao.getOldOrderStatus(orderId);
					//修改订单主表状态为有效或者部分交车
					TPcOrderPO oldOrderPo = new TPcOrderPO();
					oldOrderPo.setOrderId(Long.parseLong(orderId));
					TPcOrderPO newOrderPo = new TPcOrderPO();
					newOrderPo.setOrderId(Long.parseLong(orderId));
					if(completeList.size()>0) {//部分交车
						newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_02);
					} else {
						newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
					}
					dao.update(oldOrderPo, newOrderPo);
					
					//将为锁定的提醒信息标记为进行中
					TPcOrderDetailPO detailPo = new TPcOrderDetailPO();
					detailPo.setOrderId(Long.parseLong(orderId));
					List<PO> po = dao.select(detailPo);
					for(int i=0;i<po.size();i++) {
						detailPo = (TPcOrderDetailPO)po.get(i);
						
						TPcRemindPO oldRemindPo = new TPcRemindPO();
						oldRemindPo.setBeremindId(detailPo.getOrderDetailId());
						oldRemindPo.setRemindType(Constant.REMIND_TYPE_09.toString());
						oldRemindPo.setRemindStatus(Constant.TASK_STATUS_04);
						TPcRemindPO newRemindPo = new TPcRemindPO();
						newRemindPo.setRemindStatus(Constant.TASK_STATUS_01);
						dao.update(oldRemindPo, newRemindPo);
					}
				}
				//标记提醒信息为已完成
				CommonUtils.setRemindDone(orderId,Constant.REMIND_TYPE_14.toString());
				
				//修改审核
			} else {
				//审核通过
				
				if(if_agree=="yes"||"yes".equals(if_agree)) {
					//解锁车辆(修改车辆状态)（订单子表）
					TPcOrderDetailPO orderPo = new TPcOrderDetailPO();
					orderPo.setOrderId(Long.parseLong(orderId));
					orderPo.setTaskStatus(60171001);
					List<PO> list = dao.select(orderPo);
					TPcOrderDetailPO orderPo2 = null;
					Long vinId = null;
					for(int i=0;i<list.size();i++) {
						orderPo2 = (TPcOrderDetailPO)list.get(i);
						vinId = orderPo2.getVehicleId();
						if(vinId!=null && !"".equals(vinId)&&!"0".equals(vinId.toString())){
							//获取当前车辆状态
							TmVehiclePO td = new TmVehiclePO();
							td.setVehicleId(vinId);
							td=(TmVehiclePO) dao.select(td).get(0);
							
							//锁定车辆(修改车辆状态)
							if(Constant.VEHICLE_LIFE_10.toString().equals(td.getLifeCycle().toString())){
								TmVehiclePO oldVehiclePo = new TmVehiclePO();
								oldVehiclePo.setVehicleId(vinId);
								oldVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_10);
								TmVehiclePO newVehiclePo = new TmVehiclePO();
								newVehiclePo.setVehicleId(vinId);
								newVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_03);
								dao.update(oldVehiclePo, newVehiclePo);
							}
						}
					}
					//锁定车辆(修改车辆状态)（订单审核表）
					TPcOrderDetailAuditPO orderAuditPo = new TPcOrderDetailAuditPO();
					orderAuditPo.setOrderId(Long.parseLong(orderId));
					orderAuditPo.setStatus(10011001);
					List<PO> listAudit = dao.select(orderAuditPo);
					TPcOrderDetailAuditPO orderAuditPo2 = null;
					String vinIdAudit = null;
					for(int i=0;i<listAudit.size();i++) {
						orderAuditPo2 = (TPcOrderDetailAuditPO)listAudit.get(i);
						vinIdAudit = orderAuditPo2.getVin();
						if(vinIdAudit!=null && !"".equals(vinIdAudit)) {
							//获取当前车辆状态
							TmVehiclePO td = new TmVehiclePO();
							td.setVehicleId(Long.parseLong(vinIdAudit));
							td=(TmVehiclePO) dao.select(td).get(0);
							//锁定车辆(修改车辆状态)
							if(Constant.VEHICLE_LIFE_03.toString().equals(td.getLifeCycle().toString())||Constant.VEHICLE_LIFE_10.toString().equals(td.getLifeCycle().toString())){
								TmVehiclePO oldVehiclePo = new TmVehiclePO();
								oldVehiclePo.setVehicleId(Long.parseLong(vinIdAudit));
								//oldVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_03);
								TmVehiclePO newVehiclePo = new TmVehiclePO();
								newVehiclePo.setVehicleId(Long.parseLong(vinIdAudit));
								newVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_10);
								dao.update(oldVehiclePo, newVehiclePo);
							}
						}
					}
					//将订单审核详细表数据放入订单子表中
					//1：将原订单详细表任务进行中的数据设为无效
					
					TPcOrderDetailPO oldOrderDetailPo = new TPcOrderDetailPO();
					oldOrderDetailPo.setOrderId(Long.parseLong(orderId));
					oldOrderDetailPo.setTaskStatus(Constant.TASK_STATUS_01);
					TPcOrderDetailPO newOrderDetailPo = new TPcOrderDetailPO();
					newOrderDetailPo.setOrderId(Long.parseLong(orderId));
					newOrderDetailPo.setTaskStatus(Constant.TASK_STATUS_03);
					newOrderDetailPo.setUpdateDate(dateTime);
					newOrderDetailPo.setUpdateBy(logonUser.getUserId().toString());
					newOrderDetailPo.setOrderdDate(dateTime);
					dao.update(oldOrderDetailPo, newOrderDetailPo);
				
					/*
					List<DynaBean> orderDetailList = dao.getOrderDetail(orderId,Constant.TASK_STATUS_03);
					Iterator itod = orderDetailList.iterator();
					while(itod.hasNext()) {
						DynaBean db = (DynaBean)itod.next();
						TPcOrderDetailPO oldOrderDetailPo = new TPcOrderDetailPO();
						oldOrderDetailPo.setOrderDetailId(Long.parseLong(db.get("ORDER_DETAIL_ID").toString()));
						TPcOrderDetailPO newOrderDetailPo = new TPcOrderDetailPO();
					    //newOrderDetailPo.setOrderId(Long.parseLong(orderId));
						newOrderDetailPo.setTaskStatus(Constant.TASK_STATUS_03);
						newOrderDetailPo.setUpdateDate(new Date());
						newOrderDetailPo.setUpdateBy(logonUser.getUserId().toString());
						newOrderDetailPo.setOrderdDate(new Date());
						dao.update(oldOrderDetailPo, newOrderDetailPo);
					}
					*/
					//2：循环订单审核详细表数据放入订单详细表中
					//获取订单审核详细表数据
					List<DynaBean> orderDetailAuditList = dao.getOrderDetailAudit(orderId);
					Iterator it = orderDetailAuditList.iterator();
					while(it.hasNext()) {
						DynaBean db = (DynaBean)it.next();
						String orderDetailId="";
						TPcOrderDetailPO orderDetailPo = new TPcOrderDetailPO();
						
						orderDetailPo.setOrderId(Long.parseLong(orderId));
						orderDetailPo.setCustomerId(Long.parseLong(db.get("CUSTOMER_ID").toString()));
						orderDetailPo.setPreviousTask(Long.parseLong(orderId));
						//如果数量等于已交车数量，即表明该交车任务完成（标记任务状态为已完成）
						if(db.get("NUM").toString() == db.get("DELIVERY_NUMBER").toString() || db.get("NUM").toString().equals(db.get("DELIVERY_NUMBER").toString())) {
							orderDetailPo.setTaskStatus(Constant.TASK_STATUS_02);
						} else {
							orderDetailPo.setTaskStatus(Constant.TASK_STATUS_01);
						}
						if(db.get("MATERIAL")!=null && !"".equals(db.get("MATERIAL"))) {
							orderDetailPo.setMaterial(Long.parseLong(db.get("MATERIAL").toString()));
						}
						if(db.get("COLOR")!=null && !"".equals(db.get("COLOR"))) {
							orderDetailPo.setColor(db.get("COLOR").toString());
						}
						if(db.get("INTENT_COLOR")!=null && !"".equals(db.get("INTENT_COLOR"))) {
							orderDetailPo.setIntentColor(db.get("INTENT_COLOR").toString());
						}
						orderDetailPo.setNum(Integer.parseInt(db.get("NUM").toString()));
						orderDetailPo.setCreateBy(db.get("CREATE_BY").toString());
						orderDetailPo.setDeliveryDate(sdf.parse(db.get("DELIVERY_DATE").toString()));
						orderDetailPo.setAmount(Float.parseFloat(db.get("AMOUNT").toString()));
						if(db.get("DEPOSIT")!=null && !"".equals(db.get("DEPOSIT"))) {
							orderDetailPo.setDeposit(Float.parseFloat(db.get("DEPOSIT").toString()));
						}
						if(db.get("EARNEST")!=null && !"".equals(db.get("EARNEST"))) {
							orderDetailPo.setEarnest(Float.parseFloat(db.get("EARNEST").toString()));
						}
						orderDetailPo.setBalanceDate(sdf.parse(db.get("BALANCE_DATE").toString()));
						orderDetailPo.setDeliveryNumber(Integer.parseInt(db.get("DELIVERY_NUMBER").toString()));
						orderDetailPo.setPrice(Float.parseFloat(db.get("PRICE").toString()));
						if(db.get("VIN")!=null && !"".equals(db.get("VIN"))) {
							orderDetailPo.setVehicleId(Long.parseLong(db.get("VIN").toString()));
						}
						if(db.get("INTENT_MODEL")!=null && !"".equals(db.get("INTENT_MODEL"))) {
							orderDetailPo.setIntentModel(db.get("INTENT_MODEL").toString());
						}
						//如果原有订单明细行，则根据detailid更新；如果新增的明细行，则插入订单明细表，并设置orderdDate
						if(db.get("ORDER_DETAIL_ID")!=null && !"".equals(db.get("ORDER_DETAIL_ID"))){
							orderDetailId = db.get("ORDER_DETAIL_ID").toString();
							TPcOrderDetailPO oldOrderDetailPo1 = new TPcOrderDetailPO();
							oldOrderDetailPo1.setOrderDetailId(Long.parseLong(db.get("ORDER_DETAIL_ID").toString()));
							dao.update(oldOrderDetailPo1, orderDetailPo);
							
						}else{
							orderDetailId = SequenceManager.getSequence("");
							orderDetailPo.setOrderDetailId(Long.parseLong(orderDetailId));
							if(db.get("OLD_ORDER_DETAIL_ID")!=null && !"".equals(db.get("OLD_ORDER_DETAIL_ID"))){
							String oldOrderDetailId = db.get("OLD_ORDER_DETAIL_ID").toString();	
							orderDetailPo.setOldOrderDetailId(Long.parseLong(oldOrderDetailId));
							}
							orderDetailPo.setCreateDate(dateTime);
							orderDetailPo.setOrderdDate(dateTime);
							dao.insert(orderDetailPo);
						}
						
						
						Long cnum = Long.parseLong(db.get("NUM").toString())-Long.parseLong(db.get("DELIVERY_NUMBER").toString());
						if(cnum!=0L) {
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_09.toString(), orderDetailId.toString(), customerId, logonUser.getDealerId(), adviser, db.get("DELIVERY_DATE").toString(),cnum.toString());	
						}
					}
					//将为锁定的提醒信息标记为无效
					TPcOrderDetailPO detailPo = new TPcOrderDetailPO();
					detailPo.setOrderId(Long.parseLong(orderId));
					List<PO> po2 = dao.select(detailPo);
					for(int i=0;i<po2.size();i++) {
						detailPo = (TPcOrderDetailPO)po2.get(i);
						
						TPcRemindPO oldRemindPo = new TPcRemindPO();
						oldRemindPo.setBeremindId(detailPo.getOrderDetailId());
						oldRemindPo.setRemindType(Constant.REMIND_TYPE_09.toString());
						oldRemindPo.setRemindStatus(Constant.TASK_STATUS_04);
						TPcRemindPO newRemindPo = new TPcRemindPO();
						newRemindPo.setRemindStatus(Constant.TASK_STATUS_03);
						dao.update(oldRemindPo, newRemindPo);
					}
					//判断该订单是否已完成
					List<DynaBean> orderCompleteList = dao.getOrderComplete(orderId);
					if(orderCompleteList.size()==0) {
						//设置订单主表状态为已完成
						TPcOrderPO orderPoold = new TPcOrderPO();
						orderPoold.setOrderId(Long.parseLong(orderId));
						TPcOrderPO orderPonew = new TPcOrderPO();
						orderPonew.setOrderId(Long.parseLong(orderId));
						orderPonew.setTaskStatus(Constant.TASK_STATUS_02);
						dao.update(orderPoold, orderPonew);
					}
					
					//增加接触点信息
					CommonUtils.addContackPoint(Constant.POINT_WAY_12, reason_remark, customerId, adviser, logonUser.getDealerId());
				} else {//审核驳回
					//解锁车辆(修改车辆状态)（订单审核表）
					TPcOrderDetailAuditPO orderAuditPo = new TPcOrderDetailAuditPO();
					orderAuditPo.setOrderId(Long.parseLong(orderId));
					List<PO> listAudit = dao.select(orderAuditPo);
					TPcOrderDetailAuditPO orderAuditPo2 = null;
					String vinIdAudit = null;
				
					for(int i=0;i<listAudit.size();i++) {
						orderAuditPo2 = (TPcOrderDetailAuditPO)listAudit.get(i);
						vinIdAudit = orderAuditPo2.getVin();
						if(vinIdAudit!=null && !"".equals(vinIdAudit)) {
							TmVehiclePO oldVehiclePo = new TmVehiclePO();
							oldVehiclePo.setVehicleId(Long.parseLong(vinIdAudit));
							TmVehiclePO newVehiclePo = new TmVehiclePO();
							//获取当前车辆状态
							TmVehiclePO td = new TmVehiclePO();
							td.setVehicleId(Long.parseLong(vinIdAudit));
							td=(TmVehiclePO) dao.select(td).get(0);
							//锁定车辆(修改车辆状态)
							if(Constant.VEHICLE_LIFE_03.toString().equals(td.getLifeCycle().toString())||Constant.VEHICLE_LIFE_10.toString().equals(td.getLifeCycle().toString())){
							newVehiclePo.setVehicleId(Long.parseLong(vinIdAudit));
							newVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_03);
							dao.update(oldVehiclePo, newVehiclePo);
							}
						}
					}
					//锁定车辆(修改车辆状态)（订单子表）
					TPcOrderDetailPO orderPo = new TPcOrderDetailPO();
					orderPo.setOrderId(Long.parseLong(orderId));
					List<PO> list = dao.select(orderPo);
					TPcOrderDetailPO orderPo2 = null;
					Long vinId = 0L;
					for(int i=0;i<list.size();i++) {
						orderPo2 = (TPcOrderDetailPO)list.get(i);
						vinId = orderPo2.getVehicleId();
						if(vinId!=0){
							TmVehiclePO oldVehiclePo = new TmVehiclePO();
							oldVehiclePo.setVehicleId(vinId);
							TmVehiclePO newVehiclePo = new TmVehiclePO();
							//newVehiclePo.setVehicleId(vinId);
							//获取当前车辆状态
							TmVehiclePO td = new TmVehiclePO();
							td.setVehicleId(vinId);
							td=(TmVehiclePO) dao.select(td).get(0);
							//锁定车辆(修改车辆状态)
							if(Constant.VEHICLE_LIFE_03.toString().equals(td.getLifeCycle().toString())||Constant.VEHICLE_LIFE_10.toString().equals(td.getLifeCycle().toString())){
								newVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_10);
								dao.update(oldVehiclePo, newVehiclePo);
							}
						}
					}
					//将为锁定的提醒信息标记为经行中
					TPcOrderDetailPO detailPo = new TPcOrderDetailPO();
					detailPo.setOrderId(Long.parseLong(orderId));
					List<PO> po2 = dao.select(detailPo);
					for(int i=0;i<po2.size();i++) {
						detailPo = (TPcOrderDetailPO)po2.get(i);
						
						TPcRemindPO oldRemindPo = new TPcRemindPO();
						oldRemindPo.setBeremindId(detailPo.getOrderDetailId());
						oldRemindPo.setRemindType(Constant.REMIND_TYPE_09.toString());
						oldRemindPo.setRemindStatus(Constant.TASK_STATUS_04);
						TPcRemindPO newRemindPo = new TPcRemindPO();
						newRemindPo.setRemindStatus(Constant.TASK_STATUS_01);
						dao.update(oldRemindPo, newRemindPo);
					}
				
				}
				//获取原订单状态(completeList>0为部分交车，否则为有效)
				List<DynaBean> completeList = dao.getOldOrderStatus(orderId);
				boolean flag=false;//订单是否交车完成
				flag=CommonUtils.getOrderIfFinish(orderId.toString());
				
				//修改订单主表状态为有效或者部分交车
				TPcOrderPO oldOrderPo = new TPcOrderPO();
				oldOrderPo.setOrderId(Long.parseLong(orderId));
				TPcOrderPO newOrderPo = new TPcOrderPO();
				newOrderPo.setOrderId(Long.parseLong(orderId));
				if(completeList.size()>0) {//部分交车
					if(flag) {//完成交车
						newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_05);
					} else {
						newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_02);
					}
				} else {
					newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
				}
				newOrderPo.setTaskStatus(Constant.TASK_STATUS_02);//标记订单任务完成
				dao.update(oldOrderPo, newOrderPo);
				//标记提醒信息为已完成
				CommonUtils.setRemindDone(orderId,Constant.REMIND_TYPE_13.toString());
			}

			//设置订单审核详细表数据为无效
			TPcOrderDetailAuditPO oldOrderDetailAuditPo = new TPcOrderDetailAuditPO();
			oldOrderDetailAuditPo.setOrderId(Long.parseLong(orderId));
			TPcOrderDetailAuditPO newOrderDetailAuditPo = new TPcOrderDetailAuditPO();
			newOrderDetailAuditPo.setOrderId(Long.parseLong(orderId));
			newOrderDetailAuditPo.setStatus(Constant.STATUS_DISABLE);
			newOrderDetailAuditPo.setUpdateDate(dateTime);
			newOrderDetailAuditPo.setUpdateBy(logonUser.getUserId().toString());
			dao.update(oldOrderDetailAuditPo, newOrderDetailAuditPo);
			
			doAuditInit();
			act.setForword(ORDER_AUDIT_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 校验订单修改时是否改变订车数量
	 */
	public void checkOrderNum(){
		ActionContext act = ActionContext.getContext();
		TaskManageDao dao = new TaskManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String orderId = act.getRequest().getParamValue("orderId");
		try {
			String dealerId=logonUser.getDealerId().toString();
			int dNum=0;
			int aNum=0;
			dNum=CommonUtils.getOrderDetailNum(orderId);
			aNum=CommonUtils.getOrderDetailAuditNum(orderId);
			act.setOutData("orderCount", dNum);
			act.setOutData("auditCount", aNum);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "校验订单修改时是否改变订车数量");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	} 
	/**
	 * 订单修改/退单审核 销售经理审核
	 */
	public void doAudit() {
		ActionContext act = ActionContext.getContext();
		OrderManageDao dao = new OrderManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String orderId = act.getRequest().getParamValue("orderId");
		String orderStatus = act.getRequest().getParamValue("orderStatus");
		String if_agree = act.getRequest().getParamValue("if_agree"); //是否同意
		String audit_remark = act.getRequest().getParamValue("audit_remark"); //经理审核意见
		String reason_remark = act.getRequest().getParamValue("audit_remark"); //修改、退单说明
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTime=new Date();
		//获取customerId
		TPcOrderPO orderpo = new TPcOrderPO();
		orderpo.setOrderId(Long.parseLong(orderId));
		List<PO> po00 = dao.select(orderpo);
		orderpo = (TPcOrderPO)po00.get(0);
		String customerId = orderpo.getCustomerId().toString();
		//获取adviser
		TPcCustomerPO customerpo = new TPcCustomerPO();
		customerpo.setCustomerId(Long.parseLong(customerId));
		List<PO> po0 = dao.select(customerpo);
		customerpo = (TPcCustomerPO)po0.get(0);
		String adviser = customerpo.getAdviser().toString();
		try {
			//判断订单状态（如若已提交订单审核则抛出异常）
			TPcOrderPO oPo = new TPcOrderPO();
			oPo.setOrderId(Long.parseLong(orderId));
			List<PO> poxx = dao.select(oPo);
			oPo = (TPcOrderPO)poxx.get(0);
			Integer oStatus = oPo.getOrderStatus();
			if(oStatus != 60231003 && oStatus != 60231004) {
				throw new Exception("该条数据已被处理过！");
			}

			//修改订单审核表
			TPcOrderAuditPO oldOrderAuditPo = new TPcOrderAuditPO();
			oldOrderAuditPo.setOrderId(Long.parseLong(orderId));
			oldOrderAuditPo.setManagerAudit(Constant.DIRECTOR_AUDIT_01);
			TPcOrderAuditPO newOrderAuditPo = new TPcOrderAuditPO();
			newOrderAuditPo.setOrderId(Long.parseLong(orderId));
			if(if_agree=="yes"||"yes".equals(if_agree)) {
				newOrderAuditPo.setManagerAudit(Constant.DIRECTOR_AUDIT_02);//审核通过
			} else if(if_agree=="no"||"no".equals(if_agree)) {
				newOrderAuditPo.setManagerAudit(Constant.DIRECTOR_AUDIT_03);//审核驳回
			}
			newOrderAuditPo.setUpdateDate(dateTime);
			newOrderAuditPo.setUpdateBy(logonUser.getUserId().toString());
			if(orderStatus == "60231003" || "60231003".equals(orderStatus)) {
				newOrderAuditPo.setUpdateType(Constant.UPDATE_TYPE_01);//修改
			} else {
				newOrderAuditPo.setUpdateType(Constant.UPDATE_TYPE_01);//退单
			}
			newOrderAuditPo.setManagerAuditRemark(audit_remark);
			dao.update(oldOrderAuditPo, newOrderAuditPo);
			
			//退单审核
			if(orderStatus == "60231004" || "60231004".equals(orderStatus)) {
				//审核通过
				if(if_agree=="yes"||"yes".equals(if_agree)) {
					//解锁车辆(修改车辆状态)
					TPcOrderDetailPO orderPo = new TPcOrderDetailPO();
					orderPo.setOrderId(Long.parseLong(orderId));
					List<PO> list = dao.select(orderPo);
					TPcOrderDetailPO orderPo2 = null;
					Long vinId = 0L;
					for(int i=0;i<list.size();i++) {
						orderPo2 = (TPcOrderDetailPO)list.get(i);
						vinId = orderPo2.getVehicleId();
						if(vinId!=0){
							TmVehiclePO oldVehiclePo = new TmVehiclePO();
							oldVehiclePo.setVehicleId(vinId);
							TmVehiclePO newVehiclePo = new TmVehiclePO();
							//newVehiclePo.setVehicleId(vinId);
							newVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_03);
							dao.update(oldVehiclePo, newVehiclePo);
						}
					}
					//将订单详细表里的数据设为无效
					TPcOrderDetailPO oldOrderDetailPo = new TPcOrderDetailPO();
					TPcOrderDetailPO newOrderDetailPo = new TPcOrderDetailPO();
					oldOrderDetailPo.setOrderId(Long.parseLong(orderId));
					oldOrderDetailPo.setTaskStatus(Constant.TASK_STATUS_01);
					newOrderDetailPo.setOrderId(Long.parseLong(orderId));
					newOrderDetailPo.setTaskStatus(Constant.TASK_STATUS_03);
					newOrderDetailPo.setOrderdDate(dateTime);
					newOrderDetailPo.setUpdateDate(dateTime);
					newOrderDetailPo.setUpdateBy(logonUser.getUserId().toString());
					dao.update(oldOrderDetailPo, newOrderDetailPo);
					//修改订单主表(标记任务为未完成,并且订单状态为退单 )
					TPcOrderPO oldOrderPo2 = new TPcOrderPO();
					oldOrderPo2.setOrderId(Long.parseLong(orderId));
					TPcOrderPO newOrderPo2 = new TPcOrderPO();
					newOrderPo2.setOrderId(Long.parseLong(orderId));
					newOrderPo2.setTaskStatus(Constant.TASK_STATUS_01);
					newOrderPo2.setOrderStatus(Constant.TPC_ORDER_STATUS_07);
					newOrderPo2.setFinishDate(null);
					newOrderPo2.setOrderDate(dateTime);
					dao.update(oldOrderPo2, newOrderPo2);
					
					//将为锁定的提醒信息标记为无效
					TPcOrderDetailPO detailPo = new TPcOrderDetailPO();
					detailPo.setOrderId(Long.parseLong(orderId));
					List<PO> po = dao.select(detailPo);
					for(int i=0;i<po.size();i++) {
						detailPo = (TPcOrderDetailPO)po.get(i);
						
						TPcRemindPO oldRemindPo = new TPcRemindPO();
						oldRemindPo.setBeremindId(detailPo.getOrderDetailId());
						oldRemindPo.setRemindType(Constant.REMIND_TYPE_09.toString());
						oldRemindPo.setRemindStatus(Constant.TASK_STATUS_04);
						TPcRemindPO newRemindPo = new TPcRemindPO();
						newRemindPo.setRemindStatus(Constant.TASK_STATUS_03);
						dao.update(oldRemindPo, newRemindPo);
					}
					//增加接触点信息
					CommonUtils.addContackPoint(Constant.POINT_WAY_13, reason_remark, customerId, adviser, logonUser.getDealerId());
					//新增提醒信息
					CommonUtils.addRemindInfo(Constant.REMIND_TYPE_18.toString(), orderId, customerId, logonUser.getDealerId(), adviser, sdf.format(dateTime),"");
				} else {//审核驳回
					//获取原订单状态(completeList>0为部分交车，否则为有效)
					List<DynaBean> completeList = dao.getOldOrderStatus(orderId);
					//修改订单主表状态为有效或者部分交车
					TPcOrderPO oldOrderPo = new TPcOrderPO();
					oldOrderPo.setOrderId(Long.parseLong(orderId));
					TPcOrderPO newOrderPo = new TPcOrderPO();
					newOrderPo.setOrderId(Long.parseLong(orderId));
					if(completeList.size()>0) {//部分交车
						newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_02);
					} else {
						newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
					}
					dao.update(oldOrderPo, newOrderPo);
					
					//将为锁定的提醒信息标记为进行中
					TPcOrderDetailPO detailPo = new TPcOrderDetailPO();
					detailPo.setOrderId(Long.parseLong(orderId));
					List<PO> po = dao.select(detailPo);
					for(int i=0;i<po.size();i++) {
						detailPo = (TPcOrderDetailPO)po.get(i);
						
						TPcRemindPO oldRemindPo = new TPcRemindPO();
						oldRemindPo.setBeremindId(detailPo.getOrderDetailId());
						oldRemindPo.setRemindType(Constant.REMIND_TYPE_09.toString());
						oldRemindPo.setRemindStatus(Constant.TASK_STATUS_04);
						TPcRemindPO newRemindPo = new TPcRemindPO();
						newRemindPo.setRemindStatus(Constant.TASK_STATUS_01);
						dao.update(oldRemindPo, newRemindPo);
					}
				}
				//标记提醒信息为已完成
				CommonUtils.setRemindDone(orderId,Constant.REMIND_TYPE_14.toString());
				
				//修改审核
			} else {
				//审核通过
				if(if_agree=="yes"||"yes".equals(if_agree)) {
					//解锁车辆(修改车辆状态)（订单子表）
					TPcOrderDetailPO orderPo = new TPcOrderDetailPO();
					orderPo.setOrderId(Long.parseLong(orderId));
					orderPo.setTaskStatus(60171001);
					List<PO> list = dao.select(orderPo);
					TPcOrderDetailPO orderPo2 = null;
					Long vinId = null;
					for(int i=0;i<list.size();i++) {
						orderPo2 = (TPcOrderDetailPO)list.get(i);
						vinId = orderPo2.getVehicleId();
						if(vinId!=null && !"".equals(vinId)&&!"0".equals(vinId.toString())){
							//获取当前车辆状态
							TmVehiclePO td = new TmVehiclePO();
							td.setVehicleId(vinId);
							td=(TmVehiclePO) dao.select(td).get(0);
							
							//锁定车辆(修改车辆状态)
							if(Constant.VEHICLE_LIFE_10.toString().equals(td.getLifeCycle().toString())){
								TmVehiclePO oldVehiclePo = new TmVehiclePO();
								oldVehiclePo.setVehicleId(vinId);
								oldVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_10);
								TmVehiclePO newVehiclePo = new TmVehiclePO();
								newVehiclePo.setVehicleId(vinId);
								newVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_03);
								dao.update(oldVehiclePo, newVehiclePo);
							}
						}
					}
					//锁定车辆(修改车辆状态)（订单审核表）
					TPcOrderDetailAuditPO orderAuditPo = new TPcOrderDetailAuditPO();
					orderAuditPo.setOrderId(Long.parseLong(orderId));
					orderAuditPo.setStatus(10011001);
					List<PO> listAudit = dao.select(orderAuditPo);
					TPcOrderDetailAuditPO orderAuditPo2 = null;
					String vinIdAudit = null;
					for(int i=0;i<listAudit.size();i++) {
						orderAuditPo2 = (TPcOrderDetailAuditPO)listAudit.get(i);
						vinIdAudit = orderAuditPo2.getVin();
						if(vinIdAudit!=null && !"".equals(vinIdAudit)) {
							//获取当前车辆状态
							TmVehiclePO td = new TmVehiclePO();
							td.setVehicleId(Long.parseLong(vinIdAudit));
							td=(TmVehiclePO) dao.select(td).get(0);
							//锁定车辆(修改车辆状态)
							if(Constant.VEHICLE_LIFE_03.toString().equals(td.getLifeCycle().toString())||Constant.VEHICLE_LIFE_10.toString().equals(td.getLifeCycle().toString())){
								TmVehiclePO oldVehiclePo = new TmVehiclePO();
								oldVehiclePo.setVehicleId(Long.parseLong(vinIdAudit));
								//oldVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_03);
								TmVehiclePO newVehiclePo = new TmVehiclePO();
								newVehiclePo.setVehicleId(Long.parseLong(vinIdAudit));
								newVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_10);
								dao.update(oldVehiclePo, newVehiclePo);
							}
						}
					}
					//将订单审核详细表数据放入订单子表中
					//1：将原订单详细表任务进行中的数据设为无效
					
					TPcOrderDetailPO oldOrderDetailPo = new TPcOrderDetailPO();
					oldOrderDetailPo.setOrderId(Long.parseLong(orderId));
					oldOrderDetailPo.setTaskStatus(Constant.TASK_STATUS_01);
					TPcOrderDetailPO newOrderDetailPo = new TPcOrderDetailPO();
					newOrderDetailPo.setOrderId(Long.parseLong(orderId));
					newOrderDetailPo.setTaskStatus(Constant.TASK_STATUS_03);
					newOrderDetailPo.setUpdateDate(dateTime);
					newOrderDetailPo.setUpdateBy(logonUser.getUserId().toString());
					newOrderDetailPo.setOrderdDate(dateTime);
					dao.update(oldOrderDetailPo, newOrderDetailPo);
				
					/*
					List<DynaBean> orderDetailList = dao.getOrderDetail(orderId,Constant.TASK_STATUS_03);
					Iterator itod = orderDetailList.iterator();
					while(itod.hasNext()) {
						DynaBean db = (DynaBean)itod.next();
						TPcOrderDetailPO oldOrderDetailPo = new TPcOrderDetailPO();
						oldOrderDetailPo.setOrderDetailId(Long.parseLong(db.get("ORDER_DETAIL_ID").toString()));
						TPcOrderDetailPO newOrderDetailPo = new TPcOrderDetailPO();
					    //newOrderDetailPo.setOrderId(Long.parseLong(orderId));
						newOrderDetailPo.setTaskStatus(Constant.TASK_STATUS_03);
						newOrderDetailPo.setUpdateDate(new Date());
						newOrderDetailPo.setUpdateBy(logonUser.getUserId().toString());
						newOrderDetailPo.setOrderdDate(new Date());
						dao.update(oldOrderDetailPo, newOrderDetailPo);
					}
					*/
					//2：循环订单审核详细表数据放入订单详细表中
					//获取订单审核详细表数据
					List<DynaBean> orderDetailAuditList = dao.getOrderDetailAudit(orderId);
					Iterator it = orderDetailAuditList.iterator();
					while(it.hasNext()) {
						DynaBean db = (DynaBean)it.next();
						String orderDetailId="";
						TPcOrderDetailPO orderDetailPo = new TPcOrderDetailPO();
						
						orderDetailPo.setOrderId(Long.parseLong(orderId));
						orderDetailPo.setCustomerId(Long.parseLong(db.get("CUSTOMER_ID").toString()));
						orderDetailPo.setPreviousTask(Long.parseLong(orderId));
						
						//如果数量等于已交车数量，即表明该交车任务完成（标记任务状态为已完成）
						if(db.get("NUM").toString() == db.get("DELIVERY_NUMBER").toString() || db.get("NUM").toString().equals(db.get("DELIVERY_NUMBER").toString())) {
							orderDetailPo.setTaskStatus(Constant.TASK_STATUS_02);
						} else {
							orderDetailPo.setTaskStatus(Constant.TASK_STATUS_01);
						}
						if(db.get("MATERIAL")!=null && !"".equals(db.get("MATERIAL"))) {
							orderDetailPo.setMaterial(Long.parseLong(db.get("MATERIAL").toString()));
						}
						if(db.get("COLOR")!=null && !"".equals(db.get("COLOR"))) {
							orderDetailPo.setColor(db.get("COLOR").toString());
						}
						if(db.get("INTENT_COLOR")!=null && !"".equals(db.get("INTENT_COLOR"))) {
							orderDetailPo.setIntentColor(db.get("INTENT_COLOR").toString());
						}
						orderDetailPo.setNum(Integer.parseInt(db.get("NUM").toString()));
						orderDetailPo.setCreateBy(db.get("CREATE_BY").toString());
						orderDetailPo.setDeliveryDate(sdf.parse(db.get("DELIVERY_DATE").toString()));
						orderDetailPo.setAmount(Float.parseFloat(db.get("AMOUNT").toString()));
						if(db.get("DEPOSIT")!=null && !"".equals(db.get("DEPOSIT"))) {
							orderDetailPo.setDeposit(Float.parseFloat(db.get("DEPOSIT").toString()));
						}
						if(db.get("EARNEST")!=null && !"".equals(db.get("EARNEST"))) {
							orderDetailPo.setEarnest(Float.parseFloat(db.get("EARNEST").toString()));
						}
						orderDetailPo.setBalanceDate(sdf.parse(db.get("BALANCE_DATE").toString()));
						orderDetailPo.setDeliveryNumber(Integer.parseInt(db.get("DELIVERY_NUMBER").toString()));
						orderDetailPo.setPrice(Float.parseFloat(db.get("PRICE").toString()));
						if(db.get("VIN")!=null && !"".equals(db.get("VIN"))) {
							orderDetailPo.setVehicleId(Long.parseLong(db.get("VIN").toString()));
						}
						if(db.get("INTENT_MODEL")!=null && !"".equals(db.get("INTENT_MODEL"))) {
							orderDetailPo.setIntentModel(db.get("INTENT_MODEL").toString());
						}
						//如果原有订单明细行，则根据detailid更新；如果新增的明细行，则插入订单明细表，并设置orderdDate
						if(db.get("ORDER_DETAIL_ID")!=null && !"".equals(db.get("ORDER_DETAIL_ID"))){
							orderDetailId = db.get("ORDER_DETAIL_ID").toString();
							TPcOrderDetailPO oldOrderDetailPo1 = new TPcOrderDetailPO();
							oldOrderDetailPo1.setOrderDetailId(Long.parseLong(db.get("ORDER_DETAIL_ID").toString()));
							dao.update(oldOrderDetailPo1, orderDetailPo);
							
						}else{
							orderDetailId = SequenceManager.getSequence("");
							orderDetailPo.setOrderDetailId(Long.parseLong(orderDetailId));
							if(db.get("OLD_ORDER_DETAIL_ID")!=null && !"".equals(db.get("OLD_ORDER_DETAIL_ID"))){
							String oldOrderDetailId = db.get("OLD_ORDER_DETAIL_ID").toString();	
							orderDetailPo.setOldOrderDetailId(Long.parseLong(oldOrderDetailId));
							}
							orderDetailPo.setCreateDate(dateTime);
							orderDetailPo.setOrderdDate(dateTime);
							dao.insert(orderDetailPo);
						}
						
						
						Long cnum = Long.parseLong(db.get("NUM").toString())-Long.parseLong(db.get("DELIVERY_NUMBER").toString());
						if(cnum!=0L) {
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_09.toString(), orderDetailId.toString(), customerId, logonUser.getDealerId(), adviser, db.get("DELIVERY_DATE").toString(),cnum.toString());	
						}
					}
					//将为锁定的提醒信息标记为无效
					TPcOrderDetailPO detailPo = new TPcOrderDetailPO();
					detailPo.setOrderId(Long.parseLong(orderId));
					List<PO> po2 = dao.select(detailPo);
					for(int i=0;i<po2.size();i++) {
						detailPo = (TPcOrderDetailPO)po2.get(i);
						
						TPcRemindPO oldRemindPo = new TPcRemindPO();
						oldRemindPo.setBeremindId(detailPo.getOrderDetailId());
						oldRemindPo.setRemindType(Constant.REMIND_TYPE_09.toString());
						oldRemindPo.setRemindStatus(Constant.TASK_STATUS_04);
						TPcRemindPO newRemindPo = new TPcRemindPO();
						newRemindPo.setRemindStatus(Constant.TASK_STATUS_03);
						dao.update(oldRemindPo, newRemindPo);
					}
					//判断该订单是否已完成
					List<DynaBean> orderCompleteList = dao.getOrderComplete(orderId);
					if(orderCompleteList.size()==0) {
						//设置订单主表状态为已完成
						TPcOrderPO orderPoold = new TPcOrderPO();
						orderPoold.setOrderId(Long.parseLong(orderId));
						TPcOrderPO orderPonew = new TPcOrderPO();
						orderPonew.setOrderId(Long.parseLong(orderId));
						orderPonew.setTaskStatus(Constant.TASK_STATUS_02);
						dao.update(orderPoold, orderPonew);
					}
					
					String old_Vin=null;
					String relation_code=null;
	
					TPcOrderAuditPO tPo = new TPcOrderAuditPO();
					tPo.setOrderId(Long.parseLong(orderId));
					List<PO> pox = dao.select(tPo);
					tPo = (TPcOrderAuditPO)pox.get(0);
					old_Vin=tPo.getOld_vehicle_id();
					relation_code=tPo.getOld_relation_code();
					TaskManageDao tdao = new TaskManageDao();
					
					System.out.println("old_Vin:"+old_Vin);
					System.out.println("relation_code:"+relation_code);
					if(relation_code !=null && !"".equals(relation_code)){
					
					tdao.deleteLinkMan(customerId,"60581001"); 
					tdao.deleteLinkMan(customerId,"60581002"); 
					TPcLinkManPO linkMan=new TPcLinkManPO();
					String linkId = SequenceManager.getSequence("");
					linkMan.setLinkId(Long.parseLong(linkId));
					linkMan.setLinkMan(tPo.getOld_customer_name().trim());
					linkMan.setLinkPhone(tPo.getOld_telephone().trim());
					linkMan.setCreateDate(new Date());
					if(relation_code=="60581001" || "60581001".equals(relation_code))
					{
						linkMan.setRelationship("老客户推荐");
						linkMan.setRelationCode(Constant.linkMan_status_01.toString());
						linkMan.setOldVehicleId(tPo.getOld_vehicle_id().trim());
					}else{
						linkMan.setRelationship("朋友转介绍");
						linkMan.setRelationCode(Constant.linkMan_status_02.toString());
					}
					linkMan.setCtmId(Long.parseLong(customerId));
					linkMan.setStatus(new Long(Constant.STATUS_ENABLE));
					
					
					dao.insert(linkMan);
					}else{
						tdao.deleteLinkMan(customerId,"60581001"); //修改选择否时删除已经存在的老客户转介绍关联信息
						tdao.deleteLinkMan(customerId,"60581002"); //修改选择否时删除已经存在的朋友转介绍关联信息
					} 
					//增加接触点信息
					CommonUtils.addContackPoint(Constant.POINT_WAY_12, reason_remark, customerId, adviser, logonUser.getDealerId());
				} else {//审核驳回
					//解锁车辆(修改车辆状态)（订单审核表）
					TPcOrderDetailAuditPO orderAuditPo = new TPcOrderDetailAuditPO();
					orderAuditPo.setOrderId(Long.parseLong(orderId));
					List<PO> listAudit = dao.select(orderAuditPo);
					TPcOrderDetailAuditPO orderAuditPo2 = null;
					String vinIdAudit = null;
					for(int i=0;i<listAudit.size();i++) {
						orderAuditPo2 = (TPcOrderDetailAuditPO)listAudit.get(i);
						vinIdAudit = orderAuditPo2.getVin();
						if(vinIdAudit!=null && !"".equals(vinIdAudit)) {
							TmVehiclePO oldVehiclePo = new TmVehiclePO();
							oldVehiclePo.setVehicleId(Long.parseLong(vinIdAudit));
							TmVehiclePO newVehiclePo = new TmVehiclePO();
							//获取当前车辆状态
							TmVehiclePO td = new TmVehiclePO();
							td.setVehicleId(Long.parseLong(vinIdAudit));
							td=(TmVehiclePO) dao.select(td).get(0);
							//锁定车辆(修改车辆状态)
							if(Constant.VEHICLE_LIFE_03.toString().equals(td.getLifeCycle().toString())||Constant.VEHICLE_LIFE_10.toString().equals(td.getLifeCycle().toString())){
							newVehiclePo.setVehicleId(Long.parseLong(vinIdAudit));
							newVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_03);
							dao.update(oldVehiclePo, newVehiclePo);
							}
						}
					}
					//锁定车辆(修改车辆状态)（订单子表）
					TPcOrderDetailPO orderPo = new TPcOrderDetailPO();
					orderPo.setOrderId(Long.parseLong(orderId));
					List<PO> list = dao.select(orderPo);
					TPcOrderDetailPO orderPo2 = null;
					Long vinId = 0L;
					for(int i=0;i<list.size();i++) {
						orderPo2 = (TPcOrderDetailPO)list.get(i);
						vinId = orderPo2.getVehicleId();
						if(vinId!=0){
							TmVehiclePO oldVehiclePo = new TmVehiclePO();
							oldVehiclePo.setVehicleId(vinId);
							TmVehiclePO newVehiclePo = new TmVehiclePO();
							//newVehiclePo.setVehicleId(vinId);
							//获取当前车辆状态
							TmVehiclePO td = new TmVehiclePO();
							td.setVehicleId(vinId);
							td=(TmVehiclePO) dao.select(td).get(0);
							//锁定车辆(修改车辆状态)
							if(Constant.VEHICLE_LIFE_03.toString().equals(td.getLifeCycle().toString())||Constant.VEHICLE_LIFE_10.toString().equals(td.getLifeCycle().toString())){
								newVehiclePo.setLifeCycle(Constant.VEHICLE_LIFE_10);
								dao.update(oldVehiclePo, newVehiclePo);
							}
						}
					}
					//将为锁定的提醒信息标记为经行中
					TPcOrderDetailPO detailPo = new TPcOrderDetailPO();
					detailPo.setOrderId(Long.parseLong(orderId));
					List<PO> po2 = dao.select(detailPo);
					for(int i=0;i<po2.size();i++) {
						detailPo = (TPcOrderDetailPO)po2.get(i);
						
						TPcRemindPO oldRemindPo = new TPcRemindPO();
						oldRemindPo.setBeremindId(detailPo.getOrderDetailId());
						oldRemindPo.setRemindType(Constant.REMIND_TYPE_09.toString());
						oldRemindPo.setRemindStatus(Constant.TASK_STATUS_04);
						TPcRemindPO newRemindPo = new TPcRemindPO();
						newRemindPo.setRemindStatus(Constant.TASK_STATUS_01);
						dao.update(oldRemindPo, newRemindPo);
					}
				}
				//获取原订单状态(completeList>0为部分交车，否则为有效)
				List<DynaBean> completeList = dao.getOldOrderStatus(orderId);
				boolean flag=false;//订单是否交车完成
				flag=CommonUtils.getOrderIfFinish(orderId.toString());
				
				//修改订单主表状态为有效或者部分交车
				TPcOrderPO oldOrderPo = new TPcOrderPO();
				oldOrderPo.setOrderId(Long.parseLong(orderId));
				TPcOrderPO newOrderPo = new TPcOrderPO();
				newOrderPo.setOrderId(Long.parseLong(orderId));
				if(completeList.size()>0) {//部分交车
					if(flag) {//完成交车
						newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_05);
					} else {
						newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_02);
					}
				} else {
					newOrderPo.setOrderStatus(Constant.TPC_ORDER_STATUS_01);
				}
				dao.update(oldOrderPo, newOrderPo);
				//标记提醒信息为已完成
				CommonUtils.setRemindDone(orderId,Constant.REMIND_TYPE_13.toString());
				
				//设置订单审核详细表数据为无效
				TPcOrderDetailAuditPO oldOrderDetailAuditPo = new TPcOrderDetailAuditPO();
				oldOrderDetailAuditPo.setOrderId(Long.parseLong(orderId));
				TPcOrderDetailAuditPO newOrderDetailAuditPo = new TPcOrderDetailAuditPO();
				newOrderDetailAuditPo.setOrderId(Long.parseLong(orderId));
				newOrderDetailAuditPo.setStatus(Constant.STATUS_DISABLE);
				newOrderDetailAuditPo.setUpdateDate(dateTime);
				newOrderDetailAuditPo.setUpdateBy(logonUser.getUserId().toString());
				dao.update(oldOrderDetailAuditPo, newOrderDetailAuditPo);
			}

				
		  
			
			
			doAuditInit();
			act.setForword(ORDER_AUDIT_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
	
	public void printDetailInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String orderId = request.getParamValue("orderId");
		try {
			Map<String,String> map=new HashMap<String, String>();
			map.put("orderId", orderId);
			OrderManageDao dao = new OrderManageDao();
			List<Map<String,Object>> orderList=dao.getPrintOrderList(map);
			act.setOutData("orderList", orderList);
			act.setForword(ORDER_PRINT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "进入订单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}