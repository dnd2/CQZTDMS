package com.infodms.dms.actions.sales.ordermanage.orderaudit;

import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.storageManage.CheckVehicle;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.audit.MonthOrderCheckQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.po.TtVsOrderCheckPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
public class MonthOrderCheckQuery extends BaseDao{

	public Logger logger = Logger.getLogger(CheckVehicle.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private static final MonthOrderCheckQueryDao dao = new MonthOrderCheckQueryDao ();
	public static final MonthOrderCheckQueryDao getInstance() {
		return dao;
	}
	private final String  monthOrderCheckQuery_BUS_Init_URL = "/jsp/sales/ordermanage/orderaudit/monthOrderCheckQuery_BUS_Init.jsp";	
	private final String  monthOrderCheckQuery_SAL_Init_URL = "/jsp/sales/ordermanage/orderaudit/monthOrderCheckQuery_SAL_Init.jsp";	
	private final String  monthOrderCheckQuery_BUS_Query_Detail_URL = "/jsp/sales/ordermanage/orderaudit/monthOrderCheckQuery_BUS_Query_Detail.jsp";	
	private final String  monthOrderCheckQuery_SAL_Query_Detail_URL = "/jsp/sales/ordermanage/orderaudit/monthOrderCheckQuery_SAL_Query_Detail.jsp";	
	
	/**
	 * 月度订单审核查询：页面初始化(事业部)
	 * */
	public void monthOrderCheckQuery_BUS_Init() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(calendar.YEAR);
			List yearList = new ArrayList();
			yearList.add(year-1);
			yearList.add(year);
			yearList.add(year+1);
			
			int month= calendar.get(calendar.MONTH)+1;
			List<Map<String, Object>> areaList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("yearList", yearList);
			act.setOutData("areaList", areaList);
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setForword(monthOrderCheckQuery_BUS_Init_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月的订单审核：页面初始化(事业部)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 月度订单审核查询：结果展示(事业部)明细
	 * */
	public void monthOrderCheckQuery_BUS_Query(){
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			String startYear = CommonUtils.checkNull(request.getParamValue("startYear"));
			String endYear = CommonUtils.checkNull(request.getParamValue("endYear"));
			String startMonth = CommonUtils.checkNull(request.getParamValue("startMonth"));
			String endMonth = CommonUtils.checkNull(request.getParamValue("endMonth"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
			Long orgId = logonUser.getOrgId();
			PageResult<Map<String, Object>> ps = dao.monthOrderCheck_BUS_QueryList(orgId,startYear,startMonth,endYear, endMonth, areaId,groupCode, dealerCode, orderNO,orderStatus, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度订单审核查询：结果展示(事业部)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 月度订单审核查询：结果展示(事业部)汇总
	 * */
	public void monthOrderCheckQuery_BUS_Query_Sum(){
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			String startYear = CommonUtils.checkNull(request.getParamValue("startYear"));
			String endYear = CommonUtils.checkNull(request.getParamValue("endYear"));
			String startMonth = CommonUtils.checkNull(request.getParamValue("startMonth"));
			String endMonth = CommonUtils.checkNull(request.getParamValue("endMonth"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
			Long orgId = logonUser.getOrgId();
			PageResult<Map<String, Object>> ps = dao.monthOrderCheck_BUS_QueryList_Sum(orgId,startYear,startMonth,endYear, endMonth, areaId,groupCode, dealerCode, orderNO,orderStatus, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度订单审核查询：结果展示(事业部)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查看详细信息(事业部)
	 * */
	public void monthOrderCheckQuery_BUS_Query_Detail(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String check_id = CommonUtils.checkNull(request.getParamValue("check_id"));
			Map<String,Object> orderInfo = dao.getOrderInfo(orderId);
			List<Map<String,Object>> dList = dao.getMonthOrderCheckQuery_BUS_Query_Detail(orderId);
			TtVsOrderCheckPO checkPO = new TtVsOrderCheckPO();
			checkPO.setCheckId(Long.parseLong(check_id));
			List cList = dao.select(checkPO);
			if (null != cList && cList.size()>0) {
				TtVsOrderCheckPO orderCheckPO = (TtVsOrderCheckPO)cList.get(0);
				Integer checkStatus = orderCheckPO.getCheckStatus();
				String checkDes = orderCheckPO.getCheckDesc();
				act.setOutData("checkStatus", checkStatus);
				act.setOutData("checkDes", checkDes);
			}
			act.setOutData("orderInfo", orderInfo);
			act.setOutData("dList", dList);
			act.setForword(monthOrderCheckQuery_BUS_Query_Detail_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度订单审核查询：结果展示(事业部)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查看详细信息(销售部)
	 * */
	public void monthOrderCheckQuery_SAL_Query_Detail(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String check_id = CommonUtils.checkNull(request.getParamValue("check_id"));
			Map<String,Object> orderInfo = dao.getOrderInfo(orderId);
			List<Map<String,Object>> dList = dao.getMonthOrderCheckQuery_SAL_Query_Detail(orderId);
			TtVsOrderCheckPO checkPO = new TtVsOrderCheckPO();
			checkPO.setCheckId(Long.parseLong(check_id));
			List cList = dao.select(checkPO);
			if (null != cList && cList.size()>0) {
				TtVsOrderCheckPO orderCheckPO = (TtVsOrderCheckPO)cList.get(0);
				Integer checkStatus = orderCheckPO.getCheckStatus();
				String checkDes = orderCheckPO.getCheckDesc();
				act.setOutData("checkStatus", checkStatus);
				act.setOutData("checkDes", checkDes);
			}
			act.setOutData("orderInfo", orderInfo);
			act.setOutData("dList", dList);
			act.setForword(monthOrderCheckQuery_SAL_Query_Detail_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度订单审核查询：结果展示(事业部)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 月度订单审核查询页面初始化（销售部）
	 * */
	public void monthOrderCheckQuery_SAL_Init(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(calendar.YEAR);
			List yearList = new ArrayList();
			yearList.add(year-1);
			yearList.add(year);
			yearList.add(year+1);
			
			int month= calendar.get(calendar.MONTH)+1;
			List<Map<String, Object>> areaList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("yearList", yearList);
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("areaList", areaList);
			act.setForword(monthOrderCheckQuery_SAL_Init_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度订单审核查询页面初始化（销售部）");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询展示（销售部）汇总下载
	 * */
	public void monthOrderCheckQuery_SAL_Sum_Load(){
		 AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		    OutputStream os = null;
		    try {
		  	String startYear = CommonUtils.checkNull(request.getParamValue("startYear"));
			String endYear = CommonUtils.checkNull(request.getParamValue("endYear"));
			String startMonth = CommonUtils.checkNull(request.getParamValue("startMonth"));
			String endMonth = CommonUtils.checkNull(request.getParamValue("endMonth"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orgIds = CommonUtils.checkNull(request.getParamValue("orgId")) ;
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
			Map<String, Object> map = new HashMap<String, Object>();
			
		      // 导出的文件名
		      String fileName = "月度常规订单汇总下载.csv";
		      // 导出的文字编码
		      fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
		      response.setContentType("Application/text/csv");
		      response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		      // 定义一个集合
		      List<List<Object>> list = new LinkedList<List<Object>>();
		      // 标题
		      List<Object> listTemp = new LinkedList<Object>();
		      listTemp.add("车系");
		      listTemp.add("物料编号");
		      listTemp.add("物料名称");
		      listTemp.add("颜色名称");
		      listTemp.add("提报数量");
		      listTemp.add("审核数量");
		      listTemp.add("启票数量");
		      list.add(listTemp);
		      List<Map<String, Object>> mylist = dao.monthOrderCheckQuery_SAL_QueryList_Sum_Load(orgIds, startYear, startMonth,endYear,endMonth, areaId,groupCode, dealerCode, orderNO,orderStatus, Constant.PAGE_SIZE, 99999);
		      // 将page对象转换成LIST形式
		      //List<Map<String, Object>> mylist= ps.getRecords(); 
		      for (int i = 0; i < mylist.size(); i++) {
		        map = mylist.get(i);
		        List<Object> listValue = new LinkedList<Object>();
		        listValue = new LinkedList<Object>();
		        listValue.add(map.get("GROUP_NAME") != null ? map.get("GROUP_NAME") : "");
		        listValue.add(map.get("MATERIAL_CODE") != null ? map.get("MATERIAL_CODE") : "");
				listValue.add(map.get("MATERIAL_NAME") != null ? map.get("MATERIAL_NAME") : "");
				listValue.add(map.get("COLOR_NAME") != null ? map.get("COLOR_NAME") : "");
				listValue.add(map.get("ORDER_AMOUNT") != null ? map.get("ORDER_AMOUNT") : "");
				listValue.add(map.get("CHECK_AMOUNT") != null ? map.get("CHECK_AMOUNT") : "");
				listValue.add(map.get("CALL_AMOUNT") != null ? map.get("CALL_AMOUNT") : "");
				list.add(listValue);
			}
					os = response.getOutputStream();
					CsvWriterUtil.writeCsv(list, os);
					os.flush();
	} catch (Exception e) {
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.QUERY_FAILURE_CODE, "月度常规订单汇总下载");
		logger.error(logonUser,e1);
		act.setException(e1);
	}
}
		  
	
	/**
	 * 查询展示（销售部）详细下载
	 * */
	public void monthOrderCheckQuery_SAL_Load(){
		 AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		    OutputStream os = null;
		    try {
		      String startYear = CommonUtils.checkNull(request.getParamValue("startYear"));
				String endYear = CommonUtils.checkNull(request.getParamValue("endYear"));
				String startMonth = CommonUtils.checkNull(request.getParamValue("startMonth"));
				String endMonth = CommonUtils.checkNull(request.getParamValue("endMonth"));
				String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
				String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
				String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
				String orderNO = CommonUtils.checkNull(request.getParamValue("orderNo"));
				String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
				String orgIds = CommonUtils.checkNull(request.getParamValue("orgId")) ;
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
				Map<String, Object> map = new HashMap<String, Object>();
			
		      // 导出的文件名
		      String fileName = "月度常规订单明细下载.csv";
		      // 导出的文字编码
		      fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
		      response.setContentType("Application/text/csv");
		      response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		      // 定义一个集合
		      List<List<Object>> list = new LinkedList<List<Object>>();
		      // 标题
		      List<Object> listTemp = new LinkedList<Object>();
		      List<Map<String, Object>> rslist = dao.monthOrderCheckQuery_SAL_QueryList_Load(orgIds, startYear, startMonth,endYear,endMonth, areaId,groupCode, dealerCode, orderNO,orderStatus, Constant.PAGE_SIZE, 99999);
		      listTemp.add("采购单位代码");
		      listTemp.add("采购单位名称");
		      listTemp.add("开票单位代码");
		      listTemp.add("开票单位名称");
		      listTemp.add("订单号");
		      listTemp.add("提报日期");
		      listTemp.add("物料编号");
		      listTemp.add("订单状态");
		      listTemp.add("提报数量");
		      listTemp.add("审核数量");
		      listTemp.add("启票数量");
		      list.add(listTemp);
		      // 将page对象转换成LIST形式
		      for (int i = 0; i < rslist.size(); i++) {
		        map = rslist.get(i);
		        List<Object> listValue = new LinkedList<Object>();
		        listValue = new LinkedList<Object>();
		        listValue.add(map.get("DEALER_CODE") != null ? map.get("DEALER_CODE") : "");
		        listValue.add(map.get("DEALER_NAME") != null ? map.get("DEALER_NAME") : "");
		        listValue.add(map.get("DEALER_CODE1") != null ? map.get("DEALER_CODE1") : "");
		        listValue.add(map.get("DEALER_NAME1") != null ? map.get("DEALER_NAME1") : "");
				listValue.add(map.get("ORDER_NO") != null ? map.get("ORDER_NO") : "");
				listValue.add(map.get("RAISE_DATE") != null ? map.get("RAISE_DATE") : "");
				listValue.add(map.get("MATERIAL_CODE") != null ? map.get("MATERIAL_CODE") : "");
				listValue.add(map.get("ORDER_STATUS") != null ? map.get("ORDER_STATUS") : "");
				listValue.add(map.get("ORDER_AMOUNT") != null ? map.get("ORDER_AMOUNT") : "");
				listValue.add(map.get("CHECK_AMOUNT") != null ? map.get("CHECK_AMOUNT") : "");
				listValue.add(map.get("CALL_AMOUNT") != null ? map.get("CALL_AMOUNT") : "");
				list.add(listValue);
			}
					os = response.getOutputStream();
					CsvWriterUtil.writeCsv(list, os);
					os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度订单审核查询：结果展示(事业部)下载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询展示（销售部）详细
	 * */
	public void monthOrderCheckQuery_SAL(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String startYear = CommonUtils.checkNull(request.getParamValue("startYear"));
			String endYear = CommonUtils.checkNull(request.getParamValue("endYear"));
			String startMonth = CommonUtils.checkNull(request.getParamValue("startMonth"));
			String endMonth = CommonUtils.checkNull(request.getParamValue("endMonth"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orgIds = CommonUtils.checkNull(request.getParamValue("orgId"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
			
			PageResult<Map<String, Object>> ps = dao.monthOrderCheckQuery_SAL_QueryList(orgIds, startYear, startMonth,endYear,endMonth, areaId,groupCode, dealerCode, orderNO,orderStatus, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度订单审核查询：结果展示(事业部)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询展示（销售部）汇总
	 * */
	public void monthOrderCheckQuery_SAL_Sum(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String startYear = CommonUtils.checkNull(request.getParamValue("startYear"));
			String endYear = CommonUtils.checkNull(request.getParamValue("endYear"));
			String startMonth = CommonUtils.checkNull(request.getParamValue("startMonth"));
			String endMonth = CommonUtils.checkNull(request.getParamValue("endMonth"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String orderNO = CommonUtils.checkNull(request.getParamValue("orderNo"));
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));
			String orgIds = CommonUtils.checkNull(request.getParamValue("orgId"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
			PageResult<Map<String, Object>> ps = dao.monthOrderCheckQuery_SAL_QueryList_Sum(orgIds, startYear, startMonth,endYear,endMonth, areaId,groupCode, dealerCode, orderNO,orderStatus, 9999, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度订单审核查询：结果展示(事业部)");
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
