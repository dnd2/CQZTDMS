package com.infodms.dms.actions.report.dmsReport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.yxdms.service.CommonService;
import com.infodms.yxdms.service.impl.CommonServiceImpl;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class Application extends BaseAction {

	private final ApplicationDao dao = ApplicationDao.getInstance();
	private static final AjaxSelectDao ajaxDao = AjaxSelectDao.getInstance();
	private  CommonService  commonservice  = new CommonServiceImpl();
	private PageResult<Map<String, Object>> list = null;

	// =========单车索赔金额报表=========
	public void report1() {
		String query = getParam("query");
		if ("true".equals(query)) {
			list = dao.report1(request, loginUser,getPage(1), getCurrPage());
		}
		this.sendDataAndJsp("report1", list);
	}
	
	public void expotDealerRelationData() {
		ActionContext act = ActionContext.getContext();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			RequestWrapper request = act.getRequest();
			String dealerId = CommonUtils.checkNull(request.getParamValue("xs_dealerId"));
			String shDealerId = CommonUtils.checkNull(request.getParamValue("sh_dealerId"));
			map.put("dealerId", dealerId);
			map.put("shDealerId", shDealerId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			list = DealerInfoDao.getInstance().queryBindingRelationInfo(map, curPage, Constant.PAGE_SIZE_MAX);
			dao.expotDealerRelationData(act, list);
		} catch (Exception e) {
			logger.info("查询失败！");
		}
	}
	
	public void expotData1() {
		list = dao.report1(request,loginUser, getPage(2), getCurrPage());
		dao.expotData1(act, list);
		this.sendDataAndJsp("report1", list);
	}

	// =========保养台次查询报表=========
	public void report2() {
		list = dao.report2(request,loginUser, getPage(1), getCurrPage());
		this.sendDataAndJsp("report2", list);
	}

	public void expotData2() {
		list = dao.report2(request,loginUser, getPage(2), getCurrPage());
		dao.expotData2(act, list);
		this.sendDataAndJsp("report2", list);
	}

	// =========服务活动完成率报表=========
	public void report3() {
		String query = getParam("query");
		if ("true".equals(query)) {
			list = dao.report3(request,loginUser, getPage(1), getCurrPage());
		}
		this.sendDataAndJsp("report3", list);
	}

	public void expotData3() {
		list = dao.report3(request, loginUser,getPage(2), getCurrPage());
		dao.expotData3(act, list);
	//	this.sendDataAndJsp("report3", list);
	}

	// =========索赔结算清单报表=========
	public void report4() {
		String query = getParam("query");
		if ("true".equals(query)) {
			list = dao.report4(request,loginUser, getPage(1), getCurrPage());
		}
		this.sendDataAndJsp("report4", list);
	}

	public void expotData4() {
		list = dao.report4(request, loginUser,getPage(2), getCurrPage());
		dao.expotData4(act, list);
		this.sendDataAndJsp("report4", list);
	}

	// =========导出索赔单明细报表=========
	public void report5() {
		String query = getParam("query");
		if ("true".equals(query)) {
			list = dao.report5(request, loginUser,getPage(1), getCurrPage());
		}
		this.sendDataAndJsp("report5", list);
	}

	public void expotData5() {
		list = dao.report5(request,loginUser, getPage(2), getCurrPage());
		dao.expotData5(act, list);
		this.sendDataAndJsp("report5", list);
	}

	// =========旧件导出入或服务活动VIN=========
	public void reportTemp() {
		list = dao.report6(request, getPage(1), getCurrPage());
		this.sendDataAndJsp("report6", list);
	}

	public void expotData6() {
		list = dao.report6(request, getPage(2), getCurrPage());
		dao.expotData6(act, list);
		this.sendDataAndJsp("report6", list);
	}

	// ==================品质情报报表
	public void report7() {
		
		try {
			List<Map<String, Object>> orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);
			act.setOutData("orglist", orgList);
			String query = getParam("query");
			if ("true".equals(query)) {
				list = dao.report7(request,loginUser, getPage(1), getCurrPage());
			}
			this.sendDataAndJsp("report7_1", list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void report3CS() {
		String query = getParam("query");
		if ("true".equals(query)) {
			list = dao.report7(request,loginUser, getPage(1), getCurrPage());
		}
		this.sendDataAndJsp("report7", list);
	}

	public void expotData7() {
		list = dao.report7(request, loginUser,getPage(2), getCurrPage());
		dao.expotData7(act, list);
	}

	// ===============单台车品质汇报报表
	public void report8() {
		list = dao.report8(request, getPage(1), getCurrPage());
		this.sendDataAndJsp("report8", list);
	}

	public void expotData8() {
		list = dao.report8(request, getPage(2), getCurrPage());
		dao.expotData8(act, list);
	}

	// ================大区统计报表
	public void report9() {
		list = dao.report9(request, getPage(1), getCurrPage());
		this.sendDataAndJsp("report9", list);
	}

	public void expotData9() {
		list = dao.report9(request, getPage(2), getCurrPage());
		dao.expotData9(act, list);
	}

	// =========出库明细报表=========
	public void reportOutStore() {
		String query = getParam("query");
		if ("true".equals(query)) {
			list = dao.reportOutStore(request, loginUser,getPage(1), getCurrPage());
		}
		this.sendDataAndJsp("reportOutStore", list);
	}

	public void expotReportOutStore() {
		list = dao.reportOutStore(request, loginUser, getPage(2), getCurrPage());
		dao.expotReportOutStore(act, list);
	}

	// ============================
	public void report6_1() {
		dao.report6_1(request, act);
	}

	public void report6() {
		list = dao.report6(request, getPage(1), getCurrPage());
		this.sendDataAndJsp("report6", list);
	}

	// =======================================================
	/**
	 * 质改索赔明细导出
	 */
	public void partNumDetai1Data() {
		list = dao.partNumDetai1Data(request, getPage(2), getCurrPage());
		dao.partNumDetai1Data(act, list);
	}

	/**
	 * 查询供应商
	 */
	public void selectSupplierCode() {
		list = dao.selectSupplierCode(request, getPage(1), getCurrPage());
		this.act.setOutData("ps", list);
	}

	/**
	 * 得到是否导出的页数
	 * 
	 * @param type
	 *            1查询页 2导出页
	 * @return
	 */
	private Integer getPage(int type) {
		Integer page_size = 0;
		if (type == 1) {
			page_size = ActionUtil.getPageSize(request);
			try{
				ActionUtil.setCustomPageSizeFlag(act, true);
			}catch(Exception e){
				AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "服务活动评估");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		} else if (type == 2) {
			page_size = Constant.PAGE_SIZE_MAX;
		}
		return page_size;
	}

	/**
	 * 公共的跳转报表
	 * 
	 * @param jspName
	 */
	public void sendDataAndJsp(String jspName,
			PageResult<Map<String, Object>> list) {
		this.act.setOutData("ps", list);
		String sendUrl = sendUrl(Application.class, jspName);
		this.sendMsgByUrl(sendUrl, "售后报表");
	}

	public void report1Sum() {
		Map<String, Object> valueMap = dao.report1Sum(request,loginUser,
				Constant.PAGE_SIZE, getCurrPage());
		act.setOutData("valueMap", valueMap);
	}

	public void report2Sum() {
		Map<String, Object> valueMap = dao.report2Sum(request,loginUser,
				Constant.PAGE_SIZE, getCurrPage());
		act.setOutData("valueMap", valueMap);
	}

	public void report3Sum() {
		Map<String, Object> valueMap = dao.report3Sum(request,
				Constant.PAGE_SIZE, getCurrPage());
		act.setOutData("valueMap", valueMap);
	}

	public void report4Sum() {
		Map<String, Object> valueMap = dao.report4Sum(request,Constant.PAGE_SIZE, getCurrPage());
		act.setOutData("valueMap", valueMap);
	}

	public void report17() {
		this.sendDataAndJsp("report7", list);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void queryReport17() {
		String startDate = CommonUtils.checkNull(request
				.getParamValue("startDate"));
		String math = CommonUtils.checkNull(request.getParamValue("math"));
		String endDate = CommonUtils
				.checkNull(request.getParamValue("endDate"));
		String startMonth = startDate.substring(0, 7);
		String endMonth = endDate.substring(0, 7);
		String N = CommonUtils.checkNull(request.getParamValue("N"));
		String A = CommonUtils.checkNull(request.getParamValue("A"));
		String carModel = CommonUtils.checkNull(request.getParamValue("carModel"));
		String model = DaoFactory.getSqlByarrIn(carModel);
		Map<String, String> params = new HashMap();
		params.put("startDate", startMonth);
		params.put("endDate", endMonth);
		params.put("model", model);
		params.put("N", N);
		params.put("A", A);
		params.put("math", math);
		list = dao.report17(request, getPage(1), getCurrPage(), params);
		this.sendDataAndJsp("report7", list);
	}

	public void reportCPV() {
		this.sendDataAndJsp("reportCPV", list);
	}

	public void queryCPV() {
		String startDate = CommonUtils.checkNull(request
				.getParamValue("startDate"));
		String endDate = CommonUtils
				.checkNull(request.getParamValue("endDate"));
		String startMonth = startDate.substring(0, 7);
		String endMonth = endDate.substring(0, 7);
		String sDate = CommonUtils.checkNull(request.getParamValue("sDate"));
		String sMile = CommonUtils.checkNull(request.getParamValue("sMile"));
		String carModel = request.getParamValue("carModel");

		String[] models = carModel.split(",");

		String model = "";
		for (int i = 0; i < models.length; i++) {
			model = model + "'" + models[i] + "'";
			if (i != models.length - 1) {
				model += ",";
			}
		}
		Map<String, String> params = new HashMap();
		params.put("startDate", startMonth);
		params.put("endDate", endMonth);
		params.put("model", model);
		params.put("sDate", sDate);
		params.put("sMile", sMile);
		list = dao.CPVReport(request, getPage(1), getCurrPage(), params);
		this.sendDataAndJsp("ReportCPV", list);
	}

	public void expotDataCPV() {
		String startDate = CommonUtils.checkNull(request
				.getParamValue("startDate"));
		String endDate = CommonUtils
				.checkNull(request.getParamValue("endDate"));
		String startMonth = startDate.substring(0, 7);
		String endMonth = endDate.substring(0, 7);
		String sDate = CommonUtils.checkNull(request.getParamValue("sDate"));
		String sMile = CommonUtils.checkNull(request.getParamValue("sMile"));
		String carModel = request.getParamValue("carModel");

		String[] models = carModel.split(",");

		String model = "";
		for (int i = 0; i < models.length; i++) {
			model = model + "'" + models[i] + "'";
			if (i != models.length - 1) {
				model += ",";
			}
		}
		Map<String, String> params = new HashMap();
		params.put("startDate", startMonth);
		params.put("endDate", endMonth);
		params.put("model", model);
		params.put("sDate", sDate);
		params.put("sMile", sMile);
		list = dao.CPVReport(request, getPage(1), getCurrPage(), params);
		dao.expotDataCPV(act, list);
		this.sendDataAndJsp("reportCPV", list);
	}

	public void expotData3CS() {
		String startDate = CommonUtils.checkNull(request
				.getParamValue("startDate"));
		String math = CommonUtils.checkNull(request.getParamValue("math"));
		String endDate = CommonUtils
				.checkNull(request.getParamValue("endDate"));
		String startMonth = startDate.substring(0, 7);
		String endMonth = endDate.substring(0, 7);
		String N = CommonUtils.checkNull(request.getParamValue("N"));
		String A = CommonUtils.checkNull(request.getParamValue("A"));

		String carModel = request.getParamValue("carModel");

		String[] models = carModel.split(",");

		String model = "";
		for (int i = 0; i < models.length; i++) {
			model = model + "'" + models[i] + "'";
			if (i != models.length - 1) {
				model += ",";
			}
		}

		Map<String, String> params = new HashMap();
		params.put("startDate", startMonth);
		params.put("endDate", endMonth);
		params.put("model", model);
		params.put("N", N);
		params.put("A", A);
		params.put("math", math);
		list = dao.report17(request, getPage(1), getCurrPage(), params);
		dao.expotData3CS(act, list);
		this.sendDataAndJsp("report7", list);
	}

	public void report11() {
		this.sendDataAndJsp("report11", list);
	}

	public void report14() {
		this.sendDataAndJsp("report14", list);
	}

	public void queryReport11() {
		String year = request.getParamValue("year");
		if (year == null || year.equals("")) {
			year = new SimpleDateFormat("yyyy").format(new Date());
		}
		list = dao.report11(request, getPage(1), getCurrPage(), year);

		this.sendDataAndJsp("report11", list);
	}

	public void expotData11() {
		String year = request.getParamValue("year");
		if (year == null || year.equals("")) {
			year = new SimpleDateFormat("yyyy").format(new Date());
		}
		list = dao.report11(request, getPage(2), getCurrPage(), year);
		dao.expotData11(act, list);
	}

	public void report12() {
		String year = CommonUtils.checkNull(request.getParamValue("year"));
		String month = CommonUtils.checkNull(request.getParamValue("month"));
		act.setOutData("year", year);
		act.setOutData("month", month);
		this.sendDataAndJsp("report12", null);
	}

	public void report23() {
		String year = CommonUtils.checkNull(request.getParamValue("year"));
		String month = CommonUtils.checkNull(request.getParamValue("month"));
		act.setOutData("year", year);
		act.setOutData("month", month);
		this.sendDataAndJsp("report13", null);
	}

	public void queryReport12() {
		String year = request.getParamValue("year");
		String month = request.getParamValue("month");
		String FaCode = request.getParamValue("FaCode");
		String FaShortName = request.getParamValue("FaShortName");
		String date = year + "-" + month;
		Map<String, String> params = new HashMap();
		params.put("date", date);
		params.put("FaCode", FaCode);
		params.put("FaShortName", FaShortName);
		list = dao.report12(request, getPage(1), getCurrPage(), params);
		this.sendDataAndJsp("report12", list);
	}

	public void queryReport13() {
		String year = request.getParamValue("year");
		String month = request.getParamValue("month");
		String FaCode = request.getParamValue("FaCode");
		String FaShortName = request.getParamValue("FaShortName");
		String detailCode = request.getParamValue("detailCode");
		String detailName = request.getParamValue("detailName");
		String date = year + "-" + month;
		Map<String, String> params = new HashMap();
		params.put("date", date);
		params.put("FaCode", FaCode);
		params.put("FaShortName", FaShortName);
		params.put("detailCode", detailCode);
		params.put("detailName", detailName);
		list = dao.report13(request, getPage(1), getCurrPage(), params);
		this.sendDataAndJsp("report13", list);
	}

	public void queryReport14() {
		String startDate = request.getParamValue("startDate");
		String endDate = request.getParamValue("endDate");
		String FaCode = request.getParamValue("FaCode");
		String FaShortName = request.getParamValue("FaShortName");
		String detailCode = request.getParamValue("detailCode");
		String detailName = request.getParamValue("detailName");
		Map<String, String> params = new HashMap();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("FaCode", FaCode);
		params.put("FaShortName", FaShortName);
		params.put("detailCode", detailCode);
		params.put("detailName", detailName);
		list = dao.report14(request, getPage(1), getCurrPage(), params);
		this.sendDataAndJsp("report14", list);
	}

	public void expotReport14() {
		String startDate = request.getParamValue("startDate");
		String endDate = request.getParamValue("endDate");
		String FaCode = request.getParamValue("FaCode");
		String FaShortName = request.getParamValue("FaShortName");
		String detailCode = request.getParamValue("detailCode");
		String detailName = request.getParamValue("detailName");
		Map<String, String> params = new HashMap();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("FaCode", FaCode);
		params.put("FaShortName", FaShortName);
		params.put("detailCode", detailCode);
		params.put("detailName", detailName);
		list = dao.report14(request, getPage(2), getCurrPage(), params);
		dao.expotData14(act, list);
	}

	public void expotData13() {
		String year = request.getParamValue("year");
		String month = request.getParamValue("month");
		String FaCode = request.getParamValue("FaCode");
		String FaShortName = request.getParamValue("FaShortName");
		String detailCode = request.getParamValue("detailCode");
		String detailName = request.getParamValue("detailName");
		String date = year + "-" + month;
		Map<String, String> params = new HashMap();
		params.put("date", date);
		params.put("FaCode", FaCode);
		params.put("FaShortName", FaShortName);
		params.put("detailCode", detailCode);
		params.put("detailName", detailName);
		list = dao.report13(request, getPage(2), getCurrPage(), params);
		dao.expotData13(act, list);
	}

	public void expotData12() {
		String year = CommonUtils.checkNull(request.getParamValue("year"));
		String month = CommonUtils.checkNull(request.getParamValue("month"));
		String FaCode = CommonUtils.checkNull(request.getParamValue("FaCode"));
		String FaShortName = CommonUtils.checkNull(request
				.getParamValue("FaShortName"));
		String date = year + "-" + month;
		Map<String, String> params = new HashMap();
		params.put("date", date);
		params.put("FaCode", FaCode);
		params.put("FaShortName", FaShortName);
		list = dao.report12(request, getPage(2), getCurrPage(), params);
		dao.expotData12(act, list);
	}

	public void report15() {
		this.sendDataAndJsp("report15", list);
	}

	public void queryReport15() {
		String year = request.getParamValue("year");
		if (year == null || year.equals("")) {
			year = new SimpleDateFormat("yyyy").format(new Date());
		}
		list = dao.report15(request, getPage(1), getCurrPage(), year);
		this.sendDataAndJsp("report11", list);
	}

	public void expotData15() {
		String year = request.getParamValue("year");
		if (year == null || year.equals("")) {
			year = new SimpleDateFormat("yyyy").format(new Date());
		}
		list = dao.report15(request, getPage(2), getCurrPage(), year);
		dao.expotData15(act, list);
	}

	public void reportDealer() {
		
		String year = request.getParamValue("year");
		String month = request.getParamValue("month");
		String date = year + "-" + month;
		
		Map<String, String> params = new HashMap();
		params.put("date", date);
		list = dao.reportDealer(request, getPage(1), getCurrPage(), params);
		act.setOutData("year", year);
		act.setOutData("month", month);
		this.sendDataAndJsp("reportDealer", list);
	}

	public void expotReportDealer() {
		String year = request.getParamValue("year");
		String month = request.getParamValue("month");
		String date = year + "-" + month;
		Map<String, String> params = new HashMap();
		params.put("date", date);
		list = dao.reportDealer(request, getPage(2), getCurrPage(), params);
		dao.expotDataDealer(act, list);
	}

	public void reportRige() {
		String year = request.getParamValue("year");
		String month = request.getParamValue("month");
		
		
		
		String date = year + "-" + month;
		Map<String, String> params = new HashMap();
		params.put("date", date);
		list = dao.reportRige(request, getPage(1), getCurrPage(), params);
		act.setOutData("year", year);
		act.setOutData("month", month);
		this.sendDataAndJsp("reportRige", list);
	}

	public void expotReportRige() {
		String year = request.getParamValue("year");
		String month = request.getParamValue("month");
		String date = year + "-" + month;
		Map<String, String> params = new HashMap();
		params.put("date", date);
		list = dao.reportRige(request, getPage(2), getCurrPage(), params);
		dao.expotDataRige(act, list);
	}

	public void reportIn() {
		String year = request.getParamValue("year");
		String month = request.getParamValue("month");
		String date = year + "-" + month;
		Map<String, String> params = new HashMap();
		params.put("date", date);
		list = dao.reportIn(request, getPage(1), getCurrPage(), params);
		act.setOutData("year", year);
		act.setOutData("month", month);
		this.sendDataAndJsp("reportIn", list);
	}

	public void expotReportIn() {
		String year = request.getParamValue("year");
		String month = request.getParamValue("month");
		String date = year + "-" + month;
		Map<String, String> params = new HashMap();
		params.put("date", date);
		list = dao.reportIn(request, getPage(2), getCurrPage(), params);
		act.setOutData("year", year);
		act.setOutData("month", month);
		dao.expotDataIn(act, list);
	}

	public void reportRefuse() {
		String year = request.getParamValue("year");
		String month = request.getParamValue("month");
		String date = year + "-" + month;
		Map<String, String> params = new HashMap();
		params.put("date", date);
		list = dao.reportRefuse(request, getPage(1), getCurrPage(), params);
		act.setOutData("year", year);
		act.setOutData("month", month);
		this.sendDataAndJsp("reportRefuse", list);
	}

	public void expotReportRefuse() {
		String year = request.getParamValue("year");
		String month = request.getParamValue("month");
		String date = year + "-" + month;
		Map<String, String> params = new HashMap();
		params.put("date", date);
		list = dao.reportRefuse(request, getPage(2), getCurrPage(), params);
		dao.expotDataRefuse(act, list);
	}

	public void reportOldPart() {
		this.sendDataAndJsp("reportOldPart", null);
	}

	public void queryReportOldPart() {
		String date = request.getParamValue("date");

		date = date.substring(0, 7);
		String FaCode = request.getParamValue("FaCode");
		String FaShortName = request.getParamValue("FaShortName");
		String detailCode = request.getParamValue("detailCode");
		String detailName = request.getParamValue("detailName");
		Map<String, String> params = new HashMap();
		params.put("FaCode", FaCode);
		params.put("FaShortName", FaShortName);
		params.put("detailCode", detailCode);
		params.put("detailName", detailName);
		params.put("date", date);
		list = dao.reportOldPart(request, getPage(1), getCurrPage(), params);
		this.sendDataAndJsp("reportOldPart", list);
	}

	public void expotReportOldPart() {
		String date = request.getParamValue("date");
		date = date.substring(0, 7);
		String FaCode = request.getParamValue("FaCode");
		String FaShortName = request.getParamValue("FaShortName");
		String detailCode = request.getParamValue("detailCode");
		String detailName = request.getParamValue("detailName");
		Map<String, String> params = new HashMap();
		params.put("FaCode", FaCode);
		params.put("FaShortName", FaShortName);
		params.put("detailCode", detailCode);
		params.put("detailName", detailName);
		params.put("date", date);
		list = dao.reportOldPart(request, getPage(2), getCurrPage(), params);
		dao.expotDataOldPart(act, list);

	}

	public void carSupply() {
		this.sendDataAndJsp("reportCarSupply", null);
	}

	public void carFix() {
		this.sendDataAndJsp("CarFix", null);
	}

	public void queryCarSupply() {

		String partCode = request.getParamValue("partCode");
		String partName = request.getParamValue("partName");
		String startDate = request.getParamValue("startDate");
		String endDate = request.getParamValue("endDate");
		Map<String, String> params = new HashMap();
		params.put("partCode", partCode);
		params.put("partName", partName);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		list = dao.reportCarSupply(request, getPage(1), getCurrPage(), params);
		this.sendDataAndJsp("reportCarSupply", list);
	}

	public void queryCarFix() {
		String dealerCodess = request.getParamValue("dealerCode");
		Map<String, String> params = new HashMap();

		if (dealerCodess != null) {
			String dealerCodes[] = dealerCodess.split(",");
			String codes = "";
			for (int i = 0; i < dealerCodes.length; i++) {
				codes = codes + "'" + dealerCodes[i] + "'";
				if (i != dealerCodes.length - 1) {
					codes += ",";
				}
			}
			params.put("dealerCode", codes);
		}
		String FaShortName = request.getParamValue("FaShortName");
		String VIN = request.getParamValue("VIN");
		String startDate = request.getParamValue("startDate");
		String endDate = request.getParamValue("endDate");

		params.put("FaShortName", FaShortName);
		params.put("VIN", VIN);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		list = dao.reportCarFix(request, loginUser,getPage(1), getCurrPage(), params);
		this.sendDataAndJsp("reportCarFix", list);
	}

	public void exportCarSupply() {
		String FaCode = request.getParamValue("FaCode");
		String FaShortName = request.getParamValue("FaShortName");
		Map<String, String> params = new HashMap();
		params.put("FaCode", FaCode);
		params.put("FaShortName", FaShortName);
		list = dao.reportCarSupply(request, getPage(2), getCurrPage(), params);
		dao.expotDataCarSupply(act, list);
	}

	public void exportCarFix() {
		String dealerCodess = request.getParamValue("dealerCode");
		Map<String, String> params = new HashMap();

		if (dealerCodess != null) {
			String dealerCodes[] = dealerCodess.split(",");
			String codes = "";
			for (int i = 0; i < dealerCodes.length; i++) {
				codes = codes + "'" + dealerCodes[i] + "'";
				if (i != dealerCodes.length - 1) {
					codes += ",";
				}
			}
			params.put("dealerCode", codes);
		}
		String FaShortName = request.getParamValue("FaShortName");
		String VIN = request.getParamValue("VIN");
		String startDate = request.getParamValue("startDate");
		String endDate = request.getParamValue("endDate");

		params.put("FaShortName", FaShortName);
		params.put("VIN", VIN);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		list = dao.reportCarFix(request,loginUser, getPage(2), getCurrPage(), params);
		this.sendDataAndJsp("reportCarFix", list);
		dao.expotDataCarfix(act, list);
	}

	public void reportQualityRegion() {
		this.sendDataAndJsp("reportQualityRegion", null);
	}

	public void reportQualityDealer() {
		this.sendDataAndJsp("reportQualityDealer", null);
	}

	public void reportQualityMonth() {
		this.sendDataAndJsp("reportQualityMonth", null);
	}

	public void queryQualityRegion() {
		String FaCode = request.getParamValue("FaCode");
		String FaShortName = request.getParamValue("FaShortName");
		String VIN = request.getParamValue("VIN");
		Map<String, String> params = new HashMap();
		params.put("FaCode", FaCode);
		params.put("FaShortName", FaShortName);
		params.put("VIN", VIN);
		list = dao.reportQualityRegion(request, getPage(1), getCurrPage(),
				params);
		this.sendDataAndJsp("reportCarFix", list);
	}

	public void queryQualityDealer() {
		String dealerCodess = request.getParamValue("dealerCode");
		String startDate = request.getParamValue("startDate");
		String endDate = request.getParamValue("endDate");
		Map<String, String> params = new HashMap();
		if (dealerCodess != null) {
			String dealerCodes[] = dealerCodess.split(",");
			String codes = "";
			for (int i = 0; i < dealerCodes.length; i++) {
				codes = codes + "'" + dealerCodes[i] + "'";
				if (i != dealerCodes.length - 1) {
					codes += ",";
				}
			}
			params.put("dealerCode", codes);
		}
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		list = dao.reportQualityDealer(request,loginUser, getPage(1), getCurrPage(),
				params);
		this.sendDataAndJsp("reportQualityDealer", list);
	}

	public void queryQualityMonth() {
		String month = request.getParamValue("month");
		Map<String, String> params = new HashMap();
		params.put("month", month);
		list = dao.reportQualityMonth(request, getPage(1), getCurrPage(),
				params);
		this.sendDataAndJsp("reportQualityMonth", list);
	}

	public void exportQualityRegion() {
		String FaCode = request.getParamValue("FaCode");
		String FaShortName = request.getParamValue("FaShortName");
		String VIN = request.getParamValue("VIN");
		Map<String, String> params = new HashMap();
		params.put("FaCode", FaCode);
		params.put("FaShortName", FaShortName);
		params.put("VIN", VIN);
		list = dao.reportQualityRegion(request, getPage(2), getCurrPage(),
				params);
		dao.expotDataQualityRegion(act, list);
	}

	public void exportQualityDealer() {
		String dealerCodess = request.getParamValue("dealerCode");
		Map<String, String> params = new HashMap();
		if (dealerCodess != null) {
			String dealerCodes[] = dealerCodess.split(",");
			String codes = "";
			for (int i = 0; i < dealerCodes.length; i++) {
				codes = codes + "'" + dealerCodes[i] + "'";
				if (i != dealerCodes.length - 1) {
					codes += ",";
				}
			}
			params.put("dealerCode", codes);
		}
		list = dao.reportQualityDealer(request,loginUser, getPage(2), getCurrPage(),
				params);
		dao.expotDataQualityDealer(act, list);
	}

	public void exportQualityMonth() {
		String month = request.getParamValue("month");
		Map<String, String> params = new HashMap();
		params.put("month", month);
		list = dao.reportQualityMonth(request, getPage(2), getCurrPage(),
				params);
		dao.expotDataQualityMonth(act, list);
	}

	public void exportQualityDate() {
		String month = request.getParamValue("month");
		Map<String, String> params = new HashMap();
		params.put("month", month);
		list = dao
				.reportQualityDate(request, getPage(2), getCurrPage(), params);
		dao.expotDataQualityMonth(act, list);
	}

	public void reportStockRemoveal() {
		this.sendDataAndJsp("reportStockRemoveal", null);
	}

	public void reportRefuseQuestions() {
		this.sendDataAndJsp("reportRefuseQuestions", null);
	}

	public void reportClaimParts() {
		List listpo=  commonservice.findSelectList(null, null, null, "select t.group_id,t.group_code,t.group_name from tm_vhcl_material_group t where t.group_level=3 and t.group_code is not null");
		act.setOutData("listpo", listpo);
		this.newdate();
		this.sendDataAndJsp("reportClaimParts", null);
	}

	public void newdate() {
		Calendar c=Calendar.getInstance();
		c.set(Calendar.DATE, 1);//把日期设置为当月第一天
		c.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
		act.setOutData("start",c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-" + "1");
		act.setOutData("end", c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-" + c.get(Calendar.DATE));
		
	}

	public void queryStockRemoveal() {
		
		list = dao.reportStockRemoveal(request, getPage(1), getCurrPage());
		this.sendDataAndJsp("reportStockRemoveal", list);
	}

	public void queryRefuseQuestions() {
		String FaCode = request.getParamValue("FaCode");
		String FaShortName = request.getParamValue("FaShortName");
		String startDate = request.getParamValue("startDate");
		String endDate = request.getParamValue("endDate");
		Map<String, String> params = new HashMap();
		params.put("FaCode", FaCode);
		params.put("FaShortName", FaShortName);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		list = dao.reportRefuseQuestions(request,loginUser, getPage(1), getCurrPage(),
				params);
		this.sendDataAndJsp("reportRefuseQuestions", list);
	}

	public void queryClaimParts() {
		String dealerCodess = request.getParamValue("dealerCode");
		Map<String, String> params = new HashMap();
		if (dealerCodess != null) {
			String dealerCodes[] = dealerCodess.split(",");
			String codes = "";
			for (int i = 0; i < dealerCodes.length; i++) {
				codes = codes + "'" + dealerCodes[i] + "'";
				if (i != dealerCodes.length - 1) {
					codes += ",";
				}
			}
			
			params.put("dealerCode", codes);
		}
		String partCode = request.getParamValue("partCode");
		String partName = request.getParamValue("partName");
		String group_id = request.getParamValue("group_id");
		String carId = dao.getCarId(group_id);
		String startDate = request.getParamValue("startDate");
		String endDate = request.getParamValue("endDate");
		String claim_type = request.getParamValue("claim_type");

		params.put("partCode", partCode);
		params.put("partName", partName);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("carId", carId);
		params.put("claim_type", claim_type);
		list = dao.reportClaimParts(request,loginUser, Constant.PAGE_SIZE, getCurrPage(), params);
		this.sendDataAndJsp("reportClaimParts", list);
	}

	public void exportStockRemoveal() {
		list = dao.reportStockRemoveal(request, getPage(2), getCurrPage());
		dao.expotDataStockRemoveal(act, list);
	}

	public void exportRefuseQuestions() {
		String FaCode = request.getParamValue("FaCode");
		String FaShortName = request.getParamValue("FaShortName");
		String startDate = request.getParamValue("startDate");
		String endDate = request.getParamValue("endDate");
		Map<String, String> params = new HashMap();
		params.put("FaCode", FaCode);
		params.put("FaShortName", FaShortName);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		list = dao.reportRefuseQuestions(request,loginUser, getPage(2), getCurrPage(),
				params);
		dao.expotDataRefuseQuestions(act, list);
	}

	public void exportClaimParts() {
		String dealerCodess = request.getParamValue("dealerCode");
		Map<String, String> params = new HashMap();
		if (dealerCodess != null) {
			String dealerCodes[] = dealerCodess.split(",");
			String codes = "";
			for (int i = 0; i < dealerCodes.length; i++) {
				codes = codes + "'" + dealerCodes[i] + "'";
				if (i != dealerCodes.length - 1) {
					codes += ",";
				}
			}
			params.put("dealerCode", codes);
		}
		String partCode = request.getParamValue("partCode");
		String partName = request.getParamValue("partName");
		String group_id = request.getParamValue("group_id");
		String carId = dao.getCarId(group_id);
		String startDate = request.getParamValue("startDate");
		String endDate = request.getParamValue("endDate");
		String claim_type = request.getParamValue("claim_type");

		params.put("partCode", partCode);
		params.put("partName", partName);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("carId", carId);
		params.put("claim_type", claim_type);
		list = dao.reportClaimParts1(request,loginUser, getPage(2), getCurrPage(), params);//导出查询
		dao.expotDataClaimParts(act, list);
	}

	public void reportproductCar() {
		String month = request.getParamValue("month");
		String year = request.getParamValue("year");
		String N = request.getParamValue("N");
		String date = year + "-" + month;

		act.setOutData("date", date);
		act.setOutData("N", N);
		this.sendDataAndJsp("reportProductCar", null);
	}

	public void queryReportproductCar() {
		String date = request.getParamValue("date");
		String N = request.getParamValue("N");
		String VIN = request.getParamValue("VIN");
		Map<String, String> params = new HashMap();
		params.put("date", date);
		params.put("N", N);
		params.put("VIN", VIN);
		list = dao.reportproductCar(request, getPage(1), getCurrPage(), params);
		this.sendDataAndJsp("reportProductCar", list);
	}

	public void reportsaleCar() {
		String month = request.getParamValue("month");
		String year = request.getParamValue("year");
		String N = request.getParamValue("N");
		String date = year + "-" + month;
		act.setOutData("date", date);
		act.setOutData("N", N);
		this.sendDataAndJsp("reportSaleCar", list);
	}

	public void queryReportsaleCar() {
		String date = request.getParamValue("date");
		String N = request.getParamValue("N");
		String VIN = request.getParamValue("VIN");
		Map<String, String> params = new HashMap();
		params.put("date", date);
		params.put("N", N);
		params.put("VIN", VIN);
		list = dao.reportsaleCar(request, getPage(1), getCurrPage(), params);
		this.sendDataAndJsp("reportSaleCar", list);
	}

	public void exportReportProductCar() {
		String date = request.getParamValue("date");
		String N = request.getParamValue("N");
		String VIN = request.getParamValue("VIN");
		Map<String, String> params = new HashMap();
		params.put("date", date);
		params.put("N", N);
		params.put("VIN", VIN);
		list = dao.reportproductCar(request, getPage(2), getCurrPage(), params);
		dao.expotDataProductCar(act, list);
	}

	public void exportReportsaleCar() {
		String date = request.getParamValue("date");
		String N = request.getParamValue("N");
		String VIN = request.getParamValue("VIN");
		Map<String, String> params = new HashMap();
		params.put("date", date);
		params.put("N", N);
		params.put("VIN", VIN);
		list = dao.reportsaleCar(request, getPage(2), getCurrPage(), params);
		dao.expotDataSaleCar(act, list);
	}

	public void reportClaimCar() {
		String month = CommonUtils.checkNull(request.getParamValue("month"));
		String math = CommonUtils.checkNull(request.getParamValue("math"));
		String year = CommonUtils.checkNull(request.getParamValue("year"));
		if (Integer.parseInt(month) < 10) {
			month = "0" + month;
		}
		String date = year + "-" + month;
		String N = CommonUtils.checkNull(request.getParamValue("N"));
		String A = CommonUtils.checkNull(request.getParamValue("A"));
		String carModel = CommonUtils.checkNull(request.getParamValue("carModel"));
		String[] models = carModel.split(",");
		String model = "";
		for (int i = 0; i < models.length; i++) {
			model = model + "'" + models[i] + "'";
			if (i != models.length - 1) {
				model += ",";
			}
		}
		act.setOutData("date", date);
		act.setOutData("N", N);
		act.setOutData("A", A);
		act.setOutData("math", math);
		act.setOutData("model", model);

		this.sendDataAndJsp("reportClaimCar", null);
	}

	public void queryReportClaimCar() {
		String date = CommonUtils.checkNull(request.getParamValue("date"));
		String N = CommonUtils.checkNull(request.getParamValue("N"));
		String A = CommonUtils.checkNull(request.getParamValue("A"));
		String model = CommonUtils.checkNull(request.getParamValue("model"));
		String math = CommonUtils.checkNull(request.getParamValue("math"));
		String VIN = CommonUtils.checkNull(request.getParamValue("VIN"));

		Map<String, String> params = new HashMap();
		params.put("model", model);
		params.put("N", N);
		params.put("A", A);
		params.put("math", math);
		params.put("date", date);
		params.put("VIN", VIN);
		list = dao.reportClaimCar(request, getPage(1), getCurrPage(), params);
		this.sendDataAndJsp("reportClaimCar", list);
	}

	public void exportReportClaimCar() {
		String date = CommonUtils.checkNull(request.getParamValue("date"));
		String N = CommonUtils.checkNull(request.getParamValue("N"));
		String A = CommonUtils.checkNull(request.getParamValue("A"));
		String model = CommonUtils.checkNull(request.getParamValue("model"));
		String math = CommonUtils.checkNull(request.getParamValue("math"));
		String VIN = CommonUtils.checkNull(request.getParamValue("VIN"));

		Map<String, String> params = new HashMap();
		params.put("model", model);
		params.put("N", N);
		params.put("A", A);
		params.put("math", math);
		params.put("date", date);
		params.put("VIN", VIN);
		list = dao.reportClaimCar(request, getPage(1), getCurrPage(), params);
		dao.expotDataClaimCar(act, list);
	}

	public void reportMonthUgencyParts() {
		this.sendDataAndJsp("reportMonthUgencyParts", null);
	}

	@SuppressWarnings("unused")
	public void queryReportMonthUgencyParts() {
		String dealerCode = request.getParamValue("dealerCode");
		String dealerName = request.getParamValue("dealerName");
		String partCode = CommonUtils.checkNull(request
				.getParamValue("partCode"));

		String partName = request.getParamValue("partName");
		String tranNo = request.getParamValue("tranNo");

		String claimNo = request.getParamValue("claimNo");
		String VIN = request.getParamValue("VIN");
		String delayDay = request.getParamValue("delayDay");
		String transSituation = request.getParamValue("transSituation");

		String publishStartDate = request.getParamValue("publishStartDate");

		String publishEndDate = request.getParamValue("publishEndDate");
		String transStartDate = request.getParamValue("transStartDate");
		String transEndDate = request.getParamValue("transEndDate");
		String inStartDate = request.getParamValue("inStartDate");
		String inEndDate = request.getParamValue("inEndDate");

		String borrowStartDate = request.getParamValue("borrowStartDate");
		String borrowEndDate = request.getParamValue("borrowEndDate");
		String returnStartDate = request.getParamValue("returnStartDate");
		String returnEndDate = request.getParamValue("returnEndDate");
		String issued_person = request.getParamValue("issued_person");

		Map<String, String> params = new HashMap();
		params.put("dealerCode", dealerCode);
		params.put("dealerName", dealerName);
		params.put("partCode", partCode);
		params.put("partName", partName);
		params.put("tranNo", tranNo);

		params.put("claimNo", claimNo);
		params.put("VIN", VIN);
		params.put("delayDay", delayDay);
		params.put("transSituation", transSituation);
		params.put("publishStartDate", publishStartDate);

		params.put("publishEndDate", publishEndDate);
		params.put("transStartDate", transStartDate);
		params.put("transEndDate", transEndDate);
		params.put("inStartDate", inStartDate);
		params.put("inEndDate", inEndDate);

		params.put("borrowStartDate", borrowStartDate);
		params.put("borrowEndDate", borrowEndDate);
		params.put("returnStartDate", returnStartDate);
		params.put("returnEndDate", returnEndDate);
		params.put("issued_person", issued_person);
		
		list = dao.reportMonthUgencyParts(request,loginUser, getPage(1), getCurrPage(),
				params);
		this.sendDataAndJsp("reportMonthUgencyParts", list);

	}

	@SuppressWarnings("unused")
	public void exportReportMonthUgencyParts() {
		String dealerCode = request.getParamValue("dealerCode");
		String dealerName = request.getParamValue("dealerName");
		String partCode = CommonUtils.checkNull(request
				.getParamValue("partCode"));

		String partName = request.getParamValue("partName");
		String tranNo = request.getParamValue("tranNo");

		String claimNo = request.getParamValue("claimNo");
		String VIN = request.getParamValue("VIN");
		String delayDay = request.getParamValue("delayDay");
		String transSituation = request.getParamValue("transSituation");
		String issued_person = request.getParamValue("issued_person");
		String publishStartDate = request.getParamValue("publishStartDate");

		String publishEndDate = request.getParamValue("publishEndDate");
		String transStartDate = request.getParamValue("transStartDate");
		String transEndDate = request.getParamValue("transEndDate");
		String inStartDate = request.getParamValue("inStartDate");
		String inEndDate = request.getParamValue("inEndDate");

		String borrowStartDate = request.getParamValue("borrowStartDate");
		String borrowEndDate = request.getParamValue("borrowEndDate");
		String returnStartDate = request.getParamValue("returnStartDate");
		String returnEndDate = request.getParamValue("returnEndDate");

		Map<String, String> params = new HashMap();
		params.put("dealerCode", dealerCode);
		params.put("dealerName", dealerName);
		params.put("partCode", partCode);
		params.put("partName", partName);
		params.put("tranNo", tranNo);

		params.put("claimNo", claimNo);
		params.put("VIN", VIN);
		params.put("delayDay", delayDay);
		params.put("transSituation", transSituation);
		params.put("publishStartDate", publishStartDate);

		params.put("publishEndDate", publishEndDate);
		params.put("transStartDate", transStartDate);
		params.put("transEndDate", transEndDate);
		params.put("inStartDate", inStartDate);
		params.put("inEndDate", inEndDate);

		params.put("borrowStartDate", borrowStartDate);
		params.put("borrowEndDate", borrowEndDate);
		params.put("returnStartDate", returnStartDate);
		params.put("returnEndDate", returnEndDate);
		params.put("issued_person", issued_person);
		list = dao.reportMonthUgencyParts(request, loginUser,getPage(2), getCurrPage(),
				params);
		dao.expotDataMonthUgencyParts(act, list);

	}
	
	public void Claimoldpart(){
		String query = getParam("query");
		if ("true".equals(query)) {
			list = dao.ClaimoldpartQuery(request, loginUser,getPage(1), getCurrPage());
			this.sendDataAndJsp("Claim_oldpart_report", list);
		}
	}
	public void SettlementReport(){
		String type = getParam("type");
		if ("query".equals(type)) {
			list = dao.SettlementReportQuery(request, loginUser,getPage(1), getCurrPage());
		}
		this.sendDataAndJsp("Settlement_Report", list);
	}
	public void SettlementExport(){
		dao.SettlementExport(act,request, loginUser,getPage(2), getCurrPage());
	}
	
	public void OldpartSummaryReport(){
		String type = getParam("type");
		if ("query".equals(type)) {
			list = dao.OldpartSummaryReportQuery(request, loginUser,getPage(1), getCurrPage());
		}
		this.sendDataAndJsp("Oldpart_Summary_Report", list);
	}
	public void OldpartSummaryExport(){
		dao.OldpartSummaryExport(request,act, loginUser,Constant.PAGE_SIZE_MAX, getCurrPage());
	}
	/**
	 * 索赔数据跟踪
	 */
	public void ClaimDataTracking(){
		String type = getParam("type");
		if ("query".equals(type)) {
			list = dao.ClaimDataTrackingQuery(request, loginUser,Constant.PAGE_SIZE, getCurrPage());
		}
		this.sendDataAndJsp("Claim_Data_Tracking", list);
	}
	public void ExportClaimDataTracking(){
		 dao.ClaimDataTrackingQuery(request,act, loginUser,Constant.PAGE_SIZE_MAX, getCurrPage());
	}
	/**
	 * 预授权通过率报表
	 */
	public void authorizationRate(){
		try {
		String type = getParam("type");
		List<Map<String, Object>> orgList;
			orgList = ajaxDao.getOrgList(2, Constant.ORG_TYPE_OEM);//大区
			act.setOutData("orglist", orgList);
			if ("query".equals(type)) {
				list = dao.authorizationRateQuery(request, loginUser,Constant.PAGE_SIZE, getCurrPage());
			}
			this.sendDataAndJsp("authorization_Rate_list", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void ExportauthorizationRate(){
		 dao.ExportauthorizationRate(request,act, loginUser,Constant.PAGE_SIZE_MAX, getCurrPage());
		
	}
	/**
	 * 索赔单冻结报表
	 */
	
	public void ClaimsReport(){
		String type = getParam("type");
		CommonUtilActions commonUtilActions = new CommonUtilActions();
		// 取得该用户拥有的产地权限
		String yieldly = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());
		List<TmBusinessAreaPO> list1 = dao.select(new TmBusinessAreaPO());
		if ("query".equals(type)) {
			try {
			    list = dao.queryApplication(request,loginUser, ActionUtil.getPageSize(request), getCurrPage());
				ActionUtil.setCustomPageSizeFlag(act, true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		act.setOutData("loginUser", loginUser.getName());
		act.setOutData("yieldly", yieldly);
		act.setOutData("yieldlys", list1);
		//车型
		act.setOutData("modelList", commonUtilActions.getAllModel());
		this.sendDataAndJsp("Claims_Report_list", list);
	}
	public void ClaimsReportExport(){
		dao.ClaimsReportExport(act,request,loginUser,Constant.PAGE_SIZE_MAX, getCurrPage());
	}
	/**
	 * 计算总金额
	 */
	public void SumClaimAmount(){
		 Map<String, Object> map = 	dao.SumClaimAmount(loginUser,request);
	       BigDecimal	amount=(BigDecimal) map.get("SUMAMOUNT");
	    	if (null==amount) {
	    		act.setOutData("SUMAMOUNT", 0);
			}else {
				act.setOutData("SUMAMOUNT", amount);
			}
	    	
	}
	
}
