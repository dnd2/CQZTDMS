package com.infodms.dms.actions.sales.ordermanage.reqquery;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.ordermanage.reqquery.ReqQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class ReqQuery {
	private Logger logger = Logger.getLogger(ReqQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse() ;
	
	private final String REQ_QUERY_INIT = "/jsp/sales/ordermanage/reqquery/oemReqQuery.jsp" ;
	private final String DLR_REQ_QUERY_INIT = "/jsp/sales/ordermanage/reqquery/dlrReqQuery.jsp" ;
	private final String VHCL_INFO_INIT = "/jsp/sales/ordermanage/reqquery/vhclInfo.jsp" ;
	
	public void reqQueryInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
			
			List<Map<String, Object>> areaList = ReqQueryDao.getDlrArea(logonUser, null) ;
			
			act.setOutData("areaList", areaList) ;
			act.setOutData("orgId", getOrgId(logonUser)) ;
			
			act.setForword(REQ_QUERY_INIT) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运申请查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void dlrReqQueryInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
			
			List<Map<String, Object>> areaList = ReqQueryDao.getDlrArea(logonUser, Constant.DEALER_LEVEL_01.toString()) ;
			
			act.setOutData("areaList", areaList) ;
			act.setOutData("dealerLeveal", Constant.DEALER_LEVEL_01) ;
			
			act.setForword(DLR_REQ_QUERY_INIT) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "一级经销商发运申请查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void lowDlrReqQueryInit() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
			
			List<Map<String, Object>> areaList = ReqQueryDao.getDlrArea(logonUser, Constant.DEALER_LEVEL_02.toString()) ;
			
			act.setOutData("areaList", areaList) ;
			act.setOutData("dealerLeveal", Constant.DEALER_LEVEL_02) ;
			
			act.setForword(DLR_REQ_QUERY_INIT) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "下级经销商发运申请查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void oemReqQueryHead() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
			
			String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize")) ;
			String areaId = CommonUtils.checkNull(request.getParamValue("area")) ;
			String reqNo = CommonUtils.checkNull(request.getParamValue("reqNo")) ;
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")) ;
			String reqStatus = CommonUtils.checkNull(request.getParamValue("reqStatus")) ;
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")) ;
			String reqExeStatus = CommonUtils.checkNull(request.getParamValue("reqExeStatus")) ;
			String oDlr = CommonUtils.checkNull(request.getParamValue("oDlr")) ;
			String bDlr = CommonUtils.checkNull(request.getParamValue("bDlr")) ;
			String rDlr = CommonUtils.checkNull(request.getParamValue("rDlr")) ;
			String startTime = CommonUtils.checkNull(request.getParamValue("startTime")) ;
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime")) ;
			String beginDate = CommonUtils.checkNull(request.getParamValue("beginDate")) ;
			String overDate = CommonUtils.checkNull(request.getParamValue("overDate")) ;
			String dealerLeveal = CommonUtils.checkNull(request.getParamValue("dealerLeveal")) ;
			String isFleet = CommonUtils.checkNull(request.getParamValue("isFleet")) ;
			
			if("".equals(pageSize)) {
				pageSize = "0" ;
			} 
			
			pageSize = Integer.parseInt(pageSize) > Constant.PAGE_SIZE ? pageSize : Constant.PAGE_SIZE.toString() ;
			pageSize = Integer.parseInt(pageSize) > Constant.PAGE_SIZE_MAX ? Constant.PAGE_SIZE_MAX.toString() : pageSize ;
			
			if(CommonUtils.isNullString(areaId)) {
				areaId = getAreaStr(logonUser, dealerLeveal) ;
			}
			
			if(Constant.DEALER_LEVEL_01.toString().equals(dealerLeveal)) {
				bDlr = ReqQueryDao.getDlr(logonUser.getDealerId(), Constant.DEALER_LEVEL_01.toString()) ;
				if(bDlr.length() <= 0) {
					bDlr = "-1" ;
				}
			} else if(Constant.DEALER_LEVEL_02.toString().equals(dealerLeveal)) {
				oDlr = ReqQueryDao.getDlr(logonUser.getDealerId(), Constant.DEALER_LEVEL_02.toString()) ;
				if(oDlr.length() <= 0) {
					oDlr = "-1" ;
				}
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			
			map.put("areaId", areaId) ;
			map.put("reqNo", reqNo) ;
			map.put("orderNo", orderNo) ;
			map.put("reqStatus", reqStatus) ;
			map.put("orderType", orderType) ;
			map.put("reqExeStatus", reqExeStatus) ;
			map.put("oDlr", oDlr) ;
			map.put("bDlr", bDlr) ;
			map.put("rDlr", rDlr) ;
			map.put("startTime", startTime) ;
			map.put("endTime", endTime) ;
			map.put("beginDate", beginDate) ;
			map.put("overDate", overDate) ;
			map.put("orgId", getOrgId(logonUser)) ;
			map.put("isFleet", isFleet) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			
			PageResult<Map<String, Object>> ps = reqQueryHead(map, Integer.parseInt(pageSize), curPage);
			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运申请头表查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void oemReqQueryTotal() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
			
			String areaId = CommonUtils.checkNull(request.getParamValue("area")) ;
			String reqNo = CommonUtils.checkNull(request.getParamValue("reqNo")) ;
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")) ;
			String reqStatus = CommonUtils.checkNull(request.getParamValue("reqStatus")) ;
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")) ;
			String reqExeStatus = CommonUtils.checkNull(request.getParamValue("reqExeStatus")) ;
			String oDlr = CommonUtils.checkNull(request.getParamValue("oDlr")) ;
			String bDlr = CommonUtils.checkNull(request.getParamValue("bDlr")) ;
			String rDlr = CommonUtils.checkNull(request.getParamValue("rDlr")) ;
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")) ;
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode")) ;
			String startTime = CommonUtils.checkNull(request.getParamValue("startTime")) ;
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime")) ;
			String beginDate = CommonUtils.checkNull(request.getParamValue("beginDate")) ;
			String overDate = CommonUtils.checkNull(request.getParamValue("overDate")) ;
			String dealerLeveal = CommonUtils.checkNull(request.getParamValue("dealerLeveal")) ;
			String isFleet = CommonUtils.checkNull(request.getParamValue("isFleet")) ;
			
			if(CommonUtils.isNullString(areaId)) {
				areaId = getAreaStr(logonUser, dealerLeveal) ;
			}
			
			if(Constant.DEALER_LEVEL_01.toString().equals(dealerLeveal)) {
				bDlr = ReqQueryDao.getDlr(logonUser.getDealerId(), Constant.DEALER_LEVEL_01.toString()) ;
				if(bDlr.length() <= 0) {
					bDlr = "-1" ;
				}
			} else if(Constant.DEALER_LEVEL_02.toString().equals(dealerLeveal)) {
				oDlr = ReqQueryDao.getDlr(logonUser.getDealerId(), Constant.DEALER_LEVEL_02.toString()) ;
				if(oDlr.length() <= 0) {
					oDlr = "-1" ;
				}
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			
			map.put("areaId", areaId) ;
			map.put("reqNo", reqNo) ;
			map.put("orderNo", orderNo) ;
			map.put("reqStatus", reqStatus) ;
			map.put("orderType", orderType) ;
			map.put("reqExeStatus", reqExeStatus) ;
			map.put("oDlr", oDlr) ;
			map.put("bDlr", bDlr) ;
			map.put("rDlr", rDlr) ;
			map.put("startTime", startTime) ;
			map.put("endTime", endTime) ;
			map.put("beginDate", beginDate) ;
			map.put("overDate", overDate) ;
			map.put("materialCode", materialCode) ;
			map.put("groupCode", groupCode) ;
			map.put("orgId", getOrgId(logonUser)) ;
			map.put("isFleet", isFleet) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			
			PageResult<Map<String, Object>> ps = reqQueryTotal(map, Constant.PAGE_SIZE_MAX, curPage);
			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运申请汇总查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void oemReqQueryRes() {
		AclUserBean logonUser = null ;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
			
			String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize")) ;
			String areaId = CommonUtils.checkNull(request.getParamValue("area")) ;
			String reqNo = CommonUtils.checkNull(request.getParamValue("reqNo")) ;
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")) ;
			String reqStatus = CommonUtils.checkNull(request.getParamValue("reqStatus")) ;
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")) ;
			String reqExeStatus = CommonUtils.checkNull(request.getParamValue("reqExeStatus")) ;
			String oDlr = CommonUtils.checkNull(request.getParamValue("oDlr")) ;
			String bDlr = CommonUtils.checkNull(request.getParamValue("bDlr")) ;
			String rDlr = CommonUtils.checkNull(request.getParamValue("rDlr")) ;
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")) ;
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode")) ;
			String startTime = CommonUtils.checkNull(request.getParamValue("startTime")) ;
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime")) ;
			String beginDate = CommonUtils.checkNull(request.getParamValue("beginDate")) ;
			String overDate = CommonUtils.checkNull(request.getParamValue("overDate")) ;
			String dealerLeveal = CommonUtils.checkNull(request.getParamValue("dealerLeveal")) ;
			String isFleet = CommonUtils.checkNull(request.getParamValue("isFleet")) ;
			
			if("".equals(pageSize)) {
				pageSize = "0" ;
			} 
			
			pageSize = Integer.parseInt(pageSize) > Constant.PAGE_SIZE ? pageSize : Constant.PAGE_SIZE.toString() ;
			pageSize = Integer.parseInt(pageSize) > Constant.PAGE_SIZE_MAX ? Constant.PAGE_SIZE_MAX.toString() : pageSize ;
			
			if(CommonUtils.isNullString(areaId)) {
				areaId = getAreaStr(logonUser, dealerLeveal) ;
			}
			
			if(Constant.DEALER_LEVEL_01.toString().equals(dealerLeveal)) {
				bDlr = ReqQueryDao.getDlr(logonUser.getDealerId(), Constant.DEALER_LEVEL_01.toString()) ;
				if(bDlr.length() <= 0) {
					bDlr = "-1" ;
				}
			} else if(Constant.DEALER_LEVEL_02.toString().equals(dealerLeveal)) {
				oDlr = ReqQueryDao.getDlr(logonUser.getDealerId(), Constant.DEALER_LEVEL_02.toString()) ;
				if(oDlr.length() <= 0) {
					oDlr = "-1" ;
				}
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			
			map.put("areaId", areaId) ;
			map.put("reqNo", reqNo) ;
			map.put("orderNo", orderNo) ;
			map.put("reqStatus", reqStatus) ;
			map.put("orderType", orderType) ;
			map.put("reqExeStatus", reqExeStatus) ;
			map.put("oDlr", oDlr) ;
			map.put("bDlr", bDlr) ;
			map.put("rDlr", rDlr) ;
			map.put("startTime", startTime) ;
			map.put("endTime", endTime) ;
			map.put("beginDate", beginDate) ;
			map.put("overDate", overDate) ;
			map.put("materialCode", materialCode) ;
			map.put("groupCode", groupCode) ;
			map.put("orgId", getOrgId(logonUser)) ;
			map.put("isFleet", isFleet) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			
			PageResult<Map<String, Object>> ps = reqQueryRes(map, Integer.parseInt(pageSize), curPage);
			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运申请资源明细查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void oemReqDownloadHead() {
		AclUserBean logonUser = null ;
		OutputStream os = null;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
			
			String areaId = CommonUtils.checkNull(request.getParamValue("area")) ;
			String reqNo = CommonUtils.checkNull(request.getParamValue("reqNo")) ;
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")) ;
			String reqStatus = CommonUtils.checkNull(request.getParamValue("reqStatus")) ;
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")) ;
			String reqExeStatus = CommonUtils.checkNull(request.getParamValue("reqExeStatus")) ;
			String oDlr = CommonUtils.checkNull(request.getParamValue("oDlr")) ;
			String bDlr = CommonUtils.checkNull(request.getParamValue("bDlr")) ;
			String rDlr = CommonUtils.checkNull(request.getParamValue("rDlr")) ;
			String startTime = CommonUtils.checkNull(request.getParamValue("startTime")) ;
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime")) ;
			String beginDate = CommonUtils.checkNull(request.getParamValue("beginDate")) ;
			String overDate = CommonUtils.checkNull(request.getParamValue("overDate")) ;
			String dealerLeveal = CommonUtils.checkNull(request.getParamValue("dealerLeveal")) ;
			String isFleet = CommonUtils.checkNull(request.getParamValue("isFleet")) ;
			
			if(CommonUtils.isNullString(areaId)) {
				areaId = getAreaStr(logonUser, dealerLeveal) ;
			}
			
			if(Constant.DEALER_LEVEL_01.toString().equals(dealerLeveal)) {
				bDlr = ReqQueryDao.getDlr(logonUser.getDealerId(), Constant.DEALER_LEVEL_01.toString()) ;
				if(bDlr.length() <= 0) {
					bDlr = "-1" ;
				}
			} else if(Constant.DEALER_LEVEL_02.toString().equals(dealerLeveal)) {
				oDlr = ReqQueryDao.getDlr(logonUser.getDealerId(), Constant.DEALER_LEVEL_02.toString()) ;
				if(oDlr.length() <= 0) {
					oDlr = "-1" ;
				}
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			
			map.put("areaId", areaId) ;
			map.put("reqNo", reqNo) ;
			map.put("orderNo", orderNo) ;
			map.put("reqStatus", reqStatus) ;
			map.put("orderType", orderType) ;
			map.put("reqExeStatus", reqExeStatus) ;
			map.put("oDlr", oDlr) ;
			map.put("bDlr", bDlr) ;
			map.put("rDlr", rDlr) ;
			map.put("startTime", startTime) ;
			map.put("endTime", endTime) ;
			map.put("beginDate", beginDate) ;
			map.put("overDate", overDate) ;
			map.put("orgId", getOrgId(logonUser)) ;
			map.put("isFleet", isFleet) ;
			
			// 导出的文件名
			String fileName = "发运申请信息.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();
			
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("销售单号");
			listTemp.add("订单类型");
			listTemp.add("发运申请单号");
			listTemp.add("发运申请状态");
			listTemp.add("发运方式");
			listTemp.add("申请日期");
			listTemp.add("申请数量");
			listTemp.add("保留数量");
			listTemp.add("开票数量");
			listTemp.add("发运数量");
			listTemp.add("验收数量");
			listTemp.add("开票单位");
			listTemp.add("订货单位");
			listTemp.add("收车单位");
			listTemp.add("发运信心");
			list.add(listTemp);
			
			List<Map<String, Object>> results = reqDownloadHead(map);
			
			if(!CommonUtils.isNullList(results)) {
				int len = results.size() ;
				
				for(int i=0; i<len; i++) {
					Map<String, Object> record = results.get(i);
					listTemp = new LinkedList<Object>();
					listTemp.add(CommonUtils.checkNull(record.get("ORDER_NO")));
					listTemp.add(CommonUtils.checkNull(record.get("ORDER_TYPE_DESC")));
					listTemp.add(CommonUtils.checkNull(record.get("DLVRY_REQ_NO")));
					listTemp.add(CommonUtils.checkNull(record.get("REQ_STATUS_DESC")));
					listTemp.add(CommonUtils.checkNull(record.get("DELIVERY_TYPE_DESC")));
					listTemp.add(CommonUtils.checkNull(record.get("REQ_DATE")));
					listTemp.add(CommonUtils.checkNull(record.get("TOTAL_REQ")));
					listTemp.add(CommonUtils.checkNull(record.get("TOTAL_RESERVE")));
					listTemp.add(CommonUtils.checkNull(record.get("TOTAL_DELIVERY")));
					listTemp.add(CommonUtils.checkNull(record.get("TOTAL_MATCH")));
					listTemp.add(CommonUtils.checkNull(record.get("TOTAL_INSPECTION")));
					listTemp.add(CommonUtils.checkNull(record.get("BINFO")));
					listTemp.add(CommonUtils.checkNull(record.get("OINFO")));
					listTemp.add(CommonUtils.checkNull(record.get("RINFO")));
					listTemp.add(CommonUtils.checkNull(record.get("KINFO")));
					list.add(listTemp);
				}
			}
			
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运申请信息下载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void oemReqDownloadTotal() {
		AclUserBean logonUser = null ;
		OutputStream os = null;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
			
			String areaId = CommonUtils.checkNull(request.getParamValue("area")) ;
			String reqNo = CommonUtils.checkNull(request.getParamValue("reqNo")) ;
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")) ;
			String reqStatus = CommonUtils.checkNull(request.getParamValue("reqStatus")) ;
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")) ;
			String reqExeStatus = CommonUtils.checkNull(request.getParamValue("reqExeStatus")) ;
			String oDlr = CommonUtils.checkNull(request.getParamValue("oDlr")) ;
			String bDlr = CommonUtils.checkNull(request.getParamValue("bDlr")) ;
			String rDlr = CommonUtils.checkNull(request.getParamValue("rDlr")) ;
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")) ;
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode")) ;
			String startTime = CommonUtils.checkNull(request.getParamValue("startTime")) ;
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime")) ;
			String beginDate = CommonUtils.checkNull(request.getParamValue("beginDate")) ;
			String overDate = CommonUtils.checkNull(request.getParamValue("overDate")) ;
			String dealerLeveal = CommonUtils.checkNull(request.getParamValue("dealerLeveal")) ;
			String isFleet = CommonUtils.checkNull(request.getParamValue("isFleet")) ;
			
			if(CommonUtils.isNullString(areaId)) {
				areaId = getAreaStr(logonUser, dealerLeveal) ;
			}
			
			if(Constant.DEALER_LEVEL_01.toString().equals(dealerLeveal)) {
				bDlr = ReqQueryDao.getDlr(logonUser.getDealerId(), Constant.DEALER_LEVEL_01.toString()) ;
				if(bDlr.length() <= 0) {
					bDlr = "-1" ;
				}
			} else if(Constant.DEALER_LEVEL_02.toString().equals(dealerLeveal)) {
				oDlr = ReqQueryDao.getDlr(logonUser.getDealerId(), Constant.DEALER_LEVEL_02.toString()) ;
				if(oDlr.length() <= 0) {
					oDlr = "-1" ;
				}
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			
			map.put("areaId", areaId) ;
			map.put("reqNo", reqNo) ;
			map.put("orderNo", orderNo) ;
			map.put("reqStatus", reqStatus) ;
			map.put("orderType", orderType) ;
			map.put("reqExeStatus", reqExeStatus) ;
			map.put("oDlr", oDlr) ;
			map.put("bDlr", bDlr) ;
			map.put("rDlr", rDlr) ;
			map.put("startTime", startTime) ;
			map.put("endTime", endTime) ;
			map.put("beginDate", beginDate) ;
			map.put("overDate", overDate) ;
			map.put("materialCode", materialCode) ;
			map.put("groupCode", groupCode) ;
			map.put("orgId", getOrgId(logonUser)) ;
			map.put("isFleet", isFleet) ;
			
			// 导出的文件名
			String fileName = "发运申请汇总.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();
			
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("物料编号");
			listTemp.add("物料名称");
			listTemp.add("申请数量");
			listTemp.add("保留数量");
			listTemp.add("开票数量");
			listTemp.add("发运数量");
			listTemp.add("验收数量");
			list.add(listTemp);
			
			List<Map<String, Object>> results = reqDownloadTotal(map);
			
			if(!CommonUtils.isNullList(results)) {
				int len = results.size() ;
				
				for(int i=0; i<len; i++) {
					Map<String, Object> record = results.get(i);
					listTemp = new LinkedList<Object>();
					listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
					listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("TOTAL_REQ")));
					listTemp.add(CommonUtils.checkNull(record.get("TOTAL_RESERVE")));
					listTemp.add(CommonUtils.checkNull(record.get("TOTAL_DELIVERY")));
					listTemp.add(CommonUtils.checkNull(record.get("TOTAL_MATCH")));
					listTemp.add(CommonUtils.checkNull(record.get("TOTAL_INSPECTION")));
					list.add(listTemp);
				}
			}
			
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运申请汇总下载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void oemReqDownloadRes() {
		AclUserBean logonUser = null ;
		OutputStream os = null;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER) ;
			
			String areaId = CommonUtils.checkNull(request.getParamValue("area")) ;
			String reqNo = CommonUtils.checkNull(request.getParamValue("reqNo")) ;
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")) ;
			String reqStatus = CommonUtils.checkNull(request.getParamValue("reqStatus")) ;
			String orderType = CommonUtils.checkNull(request.getParamValue("orderType")) ;
			String reqExeStatus = CommonUtils.checkNull(request.getParamValue("reqExeStatus")) ;
			String oDlr = CommonUtils.checkNull(request.getParamValue("oDlr")) ;
			String bDlr = CommonUtils.checkNull(request.getParamValue("bDlr")) ;
			String rDlr = CommonUtils.checkNull(request.getParamValue("rDlr")) ;
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")) ;
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode")) ;
			String startTime = CommonUtils.checkNull(request.getParamValue("startTime")) ;
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime")) ;
			String beginDate = CommonUtils.checkNull(request.getParamValue("beginDate")) ;
			String overDate = CommonUtils.checkNull(request.getParamValue("overDate")) ;
			String dealerLeveal = CommonUtils.checkNull(request.getParamValue("dealerLeveal")) ;
			String isFleet = CommonUtils.checkNull(request.getParamValue("isFleet")) ;
			
			if(CommonUtils.isNullString(areaId)) {
				areaId = getAreaStr(logonUser, dealerLeveal) ;
			}
			
			if(Constant.DEALER_LEVEL_01.toString().equals(dealerLeveal)) {
				bDlr = ReqQueryDao.getDlr(logonUser.getDealerId(), Constant.DEALER_LEVEL_01.toString()) ;
				if(bDlr.length() <= 0) {
					bDlr = "-1" ;
				}
			} else if(Constant.DEALER_LEVEL_02.toString().equals(dealerLeveal)) {
				oDlr = ReqQueryDao.getDlr(logonUser.getDealerId(), Constant.DEALER_LEVEL_02.toString()) ;
				if(oDlr.length() <= 0) {
					oDlr = "-1" ;
				}
			}
			
			Map<String, String> map = new HashMap<String, String>() ;
			
			map.put("areaId", areaId) ;
			map.put("reqNo", reqNo) ;
			map.put("orderNo", orderNo) ;
			map.put("reqStatus", reqStatus) ;
			map.put("orderType", orderType) ;
			map.put("reqExeStatus", reqExeStatus) ;
			map.put("oDlr", oDlr) ;
			map.put("bDlr", bDlr) ;
			map.put("rDlr", rDlr) ;
			map.put("startTime", startTime) ;
			map.put("endTime", endTime) ;
			map.put("beginDate", beginDate) ;
			map.put("overDate", overDate) ;
			map.put("materialCode", materialCode) ;
			map.put("groupCode", groupCode) ;
			map.put("orgId", getOrgId(logonUser)) ;
			map.put("isFleet", isFleet) ;
			
			// 导出的文件名
			String fileName = "发运申请资源明细.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();
			
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("销售单号");
			listTemp.add("订单类型");
			listTemp.add("发运申请单号");
			listTemp.add("发运单状态");
			listTemp.add("申请日期");
			listTemp.add("申请总量");
			listTemp.add("配置编码");
			listTemp.add("物料编码");
			listTemp.add("申请数量");
			listTemp.add("保留数量");
			listTemp.add("开票单位");
			listTemp.add("订货单位");
			listTemp.add("收车单位");
			list.add(listTemp);
			
			List<Map<String, Object>> results = reqDownloadRes(map);
			
			if(!CommonUtils.isNullList(results)) {
				int len = results.size() ;
				
				for(int i=0; i<len; i++) {
					Map<String, Object> record = results.get(i);
					listTemp = new LinkedList<Object>();
					listTemp.add(CommonUtils.checkNull(record.get("ORDER_NO")));
					listTemp.add(CommonUtils.checkNull(record.get("ORDER_TYPE_DESC")));
					listTemp.add(CommonUtils.checkNull(record.get("DLVRY_REQ_NO")));
					listTemp.add(CommonUtils.checkNull(record.get("REQ_STATUS_DESC")));
					listTemp.add(CommonUtils.checkNull(record.get("REQ_DATE")));
					listTemp.add(CommonUtils.checkNull(record.get("REQ_TOTAL_AMOUNT")));
					listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
					listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
					listTemp.add(CommonUtils.checkNull(record.get("REQ_AMOUNT")));
					listTemp.add(CommonUtils.checkNull(record.get("RESERVE_AMOUNT")));
					listTemp.add(CommonUtils.checkNull(record.get("BINFO")));
					listTemp.add(CommonUtils.checkNull(record.get("OINFO")));
					listTemp.add(CommonUtils.checkNull(record.get("RINFO")));
					list.add(listTemp);
				}
			}
			
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运申请资源明细下载");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getVhcl() {
		String dlvryId = CommonUtils.checkNull(request.getParamValue("dlvryId")) ;
		
		if(!CommonUtils.isNullString(dlvryId)) {
			List<Map<String, Object>> vhclList = ReqQueryDao.getVhcl(dlvryId) ;
			
			act.setOutData("vhclList", vhclList) ;
		}
		
		act.setForword(VHCL_INFO_INIT) ;
	}
	
	private String getAreaStr(AclUserBean logonUser, String level) {
		StringBuffer strBuff = new StringBuffer("") ;
		
		List<Map<String, Object>> areaList = ReqQueryDao.getDlrArea(logonUser, level) ;
			
		if(!CommonUtils.isNullList(areaList)) {
			int len = areaList.size() ;
				
			for(int i=0; i<len; i++) {
				if(strBuff.length() <= 0) {
					strBuff.append(areaList.get(i).get("AREA_ID")) ;
				} else {
					strBuff.append(",").append(areaList.get(i).get("AREA_ID")) ;
				}
			}
		}
		
		return strBuff.toString() ;
	}
	
	public static String getAreaIdStr(AclUserBean logonUser, String level) {
		ReqQuery rq = new ReqQuery() ;
		
		return rq.getAreaStr (logonUser, level) ;
	}
	
	private String getOrgId(AclUserBean logonUser) {
		String orgId = "" ;
		
		String dutyType = logonUser.getDutyType() ;
		
		if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			orgId = logonUser.getOrgId().toString() ;
		}
		
		return orgId ;
	}
	
	public static PageResult<Map<String, Object>> reqQueryHead(Map<String, String> map, int pageSize, int curPage) {
		PageResult<Map<String, Object>> reqResult = ReqQueryDao.reqQueryHead(map, pageSize, curPage) ;
		
		return reqResult ;
	}
	
	public static PageResult<Map<String, Object>> reqQueryTotal(Map<String, String> map, int pageSize, int curPage) {
		PageResult<Map<String, Object>> totalResult = ReqQueryDao.reqQueryTotal(map, pageSize, curPage) ;
		
		return totalResult ;
	}
	
	public static PageResult<Map<String, Object>> reqQueryRes(Map<String, String> map, int pageSize, int curPage) {
		PageResult<Map<String, Object>> resResult = ReqQueryDao.reqQueryRes(map, pageSize, curPage) ;
		
		return resResult ;
	}
	
	public static List<Map<String, Object>> reqDownloadHead(Map<String, String> map) {
		List<Map<String, Object>> reqList = ReqQueryDao.reqDownloadHead(map) ;
		
		return reqList ;
	}
	
	public static List<Map<String, Object>> reqDownloadTotal(Map<String, String> map) {
		List<Map<String, Object>> reqList = ReqQueryDao.reqDownloadTotal(map) ;
		
		return reqList ;
	}
	
	public static List<Map<String, Object>> reqDownloadRes(Map<String, String> map) {
		List<Map<String, Object>> resList = ReqQueryDao.reqDownloadRes(map) ;
		
		return resList ;
	}
}
