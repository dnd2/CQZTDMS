package com.infodms.dms.actions.sales.planmanage.MonthTarget;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.planmanage.MonthPlanDao;
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

public class OemMonthPlanSearch {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String monthplanSearchInitUrl = "/jsp/sales/planmanage/monthplan/monthplansearch.jsp";

	/*
	 * 初始化查询页面
	 */
	public void oemMonthPlanSearchInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao = YearPlanDao.getInstance();
		try {
			List<Map<String, Object>> serlist = PlanUtil.selectSeries();
			act.setOutData("serlist", serlist);
			List<String> yearList = new ArrayList();
			String style = "inline";
			// 查询目标年份
			Calendar calendar = Calendar.getInstance();
			yearList.add(calendar.get(Calendar.YEAR) + "");
			calendar.add(Calendar.YEAR, 1);
			yearList.add(calendar.get(Calendar.YEAR) + "");
			act.setOutData("yearList", yearList);
			String duty = logonUser.getDutyType(logonUser.getDealerId());
			String orgId = logonUser.getOrgId().toString();
			String dutyType = logonUser.getDutyType(logonUser.getDealerId());
			if (Constant.DUTY_TYPE_DEPT.toString().equals(duty)) {
				orgId = logonUser.getParentOrgId();
			}
			if (Constant.DUTY_TYPE_LARGEREGION.toString().equals(duty)) {
				style = "none";
			}
			//List<Map<String, Object>> areaBusList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			// act.setOutData("areaBusList", areaBusList);
			act.setOutData("orgId", orgId);
			act.setOutData("style", style);
			act.setOutData("dutyType", dutyType);
			act.setOutData("userType", logonUser.getUserType());
			act.setForword(monthplanSearchInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 查询最大的版本号
	 */
	public void selectMaxPlanVer() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		MonthPlanDao dao = MonthPlanDao.getInstance();
		String dealerId = dao.getDealerIdByPostSql(logonUser);
		try {
			RequestWrapper request = act.getRequest();
			String dutyType = logonUser.getDutyType(logonUser.getDealerId());
			String logonOrgType = "";
			if (Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				logonOrgType = "2";
			} else {
				logonOrgType = "1";
			}
			String year = CommonUtils.checkNull(request.getParamValue("year"));
			String month = CommonUtils.checkNull(request.getParamValue("month"));
			String chngType = CommonUtils.checkNull(request.getParamValue("chngType"));
			// String areaId = CommonUtils.checkNull(request.getParamValue("buss_area"));
			// String planType = CommonUtils.checkNull(request.getParamValue("planType"));
			String maxVer = dao.selectMaxPlanVer(year, month, logonUser.getCompanyId().toString(), dealerId, chngType, null, logonOrgType);
			act.setOutData("maxVer", maxVer);
			// act.setForword(monthplanSearchInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void selectAreaGroup() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		MonthPlanDao dao = MonthPlanDao.getInstance();
		String dealerId = dao.getDealerIdByPostSql(logonUser);
		try {
			RequestWrapper request = act.getRequest();
			//String areaId = CommonUtils.checkNull(request.getParamValue("buss_area"));
			List<Map<String, Object>> list = PlanUtil.selectAreaGroup(logonUser.getCompanyId().toString(), 2);
			act.setOutData("serList", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 总部明细查询
	 */
	public void oemMonthPlanDetailSearch() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		MonthPlanDao dao = MonthPlanDao.getInstance();
		String dealerId = dao.getDealerIdByPostSql(logonUser);
		try {
			Map<String, Object> conMap = new HashMap<String, Object>();

			String dutyType = logonUser.getDutyType(logonUser.getDealerId());
			String logonOrgType = "";
			if (Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				logonOrgType = "LARGEREGION";
			}
			RequestWrapper request = act.getRequest();
			String plan_year = CommonUtils.checkNull(request.getParamValue("year"));
			String plan_month = CommonUtils.checkNull(request.getParamValue("month"));
			String plan_ver = CommonUtils.checkNull(request.getParamValue("plan_ver"));
			String plan_desc = CommonUtils.checkNull(request.getParamValue("plan_desc"));
			//String areaId = CommonUtils.checkNull(request.getParamValue("buss_area"));
			// String planType = CommonUtils.checkNull(request.getParamValue("planType"));
			String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize"));
			conMap.put("plan_year", plan_year);
			conMap.put("plan_month", plan_month);
			conMap.put("dealerId", dealerId);
			conMap.put("plan_ver", plan_ver);
			conMap.put("plan_desc", plan_desc);
			// conMap.put("planType", planType);
			conMap.put("logonOrgType", logonOrgType);
			conMap.put("logonOrgId", logonUser.getOrgId().toString());
			conMap.put("companyId", logonUser.getCompanyId().toString());
			PageResult<Map<String, Object>> ps = null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			String chgnType = request.getParamValue("chngType");
			// 2：组织、1：经销商
			String dealerCode = request.getParamValue("dealerCode");
			conMap.put("dealerCode", dealerCode);
			ps = dao.oemSelectDealerMonthPlan(logonUser.getDutyType(logonUser.getDealerId()), logonUser.getOrgId().toString(), conMap, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 车厂汇总查询
	 */
	public void oemMonthPlanTotalSearch() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		MonthPlanDao dao = MonthPlanDao.getInstance();
		String dealerId = dao.getDealerIdByPostSql(logonUser);
		try {
			Map<String, Object> conMap = new HashMap<String, Object>();
			String dutyType = logonUser.getDutyType(logonUser.getDealerId());
			String logonOrgType = "";
			if (Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				logonOrgType = "LARGEREGION";
			}
			RequestWrapper request = act.getRequest();
			String plan_year = CommonUtils.checkNull(request.getParamValue("year"));
			String plan_month = CommonUtils.checkNull(request.getParamValue("month"));
			String plan_ver = CommonUtils.checkNull(request.getParamValue("plan_ver"));
			String plan_desc = CommonUtils.checkNull(request.getParamValue("plan_desc"));
			// String areaId = CommonUtils.checkNull(request.getParamValue("buss_area"));
			//String planType = CommonUtils.checkNull(request.getParamValue("planType"));// 任务类型
			String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize"));
			List<Map<String, Object>> serlist = PlanUtil.selectAreaGroup( logonUser.getCompanyId().toString(), 2);
			// List<Map<String, Object>> serlist=PlanUtil.selectSeries();
			conMap.put("plan_year", plan_year);
			conMap.put("plan_month", plan_month);
			conMap.put("plan_ver", plan_ver);
			conMap.put("plan_desc", plan_desc);
			conMap.put("logonOrgType", logonOrgType);
			conMap.put("logonOrgId", logonUser.getOrgId().toString());
			conMap.put("companyId", logonUser.getCompanyId().toString());
			conMap.put("dealerId", dealerId);
			//conMap.put("planType", planType);
			PageResult<Map<String, Object>> ps = null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			// String chgnType = request.getParamValue("chngType");
			// 2：组织、1：经销商
			String dealerCode = request.getParamValue("dealerCode");
			conMap.put("dealerCode", dealerCode);
			ps = dao.oemSelectDealerMonthPlanTotal(logonUser.getDutyType(logonUser.getDealerId()), logonUser.getOrgId().toString(), conMap, serlist, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 月度任务下载
	 */
	public void yearPlanSearchExport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao = YearPlanDao.getInstance();
		String dealerId = dao.getDealerIdByPostSql(logonUser);
		OutputStream os = null;
		try {
			Map<String, Object> conMap = new HashMap<String, Object>();
			String dutyType = logonUser.getDutyType(logonUser.getDealerId());
			String logonOrgType = "";
			if (Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				logonOrgType = "LARGEREGION";
			}
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();
			String chgnType = CommonUtils.checkNull(request.getParamValue("chngType"));// 2：组织、1：经销商
			String plan_year = CommonUtils.checkNull(request.getParamValue("year"));
			String plan_month = CommonUtils.checkNull(request.getParamValue("month"));
			String plan_ver = CommonUtils.checkNull(request.getParamValue("plan_ver"));
			String plan_desc = CommonUtils.checkNull(request.getParamValue("plan_desc"));
			// String areaId = CommonUtils.checkNull(request.getParamValue("buss_area"));
			// String planType = CommonUtils.checkNull(request.getParamValue("planType"));// 月度类型：（用于长安汽车）
			conMap.put("plan_year", plan_year);
			conMap.put("plan_month", plan_month);
			conMap.put("plan_ver", plan_ver);
			conMap.put("plan_desc", plan_desc);
			conMap.put("logonOrgType", logonOrgType);
			conMap.put("logonOrgId", logonUser.getOrgId().toString());
			conMap.put("companyId", logonUser.getCompanyId().toString());
			conMap.put("dealerId", dealerId);
			// conMap.put("planType", planType);
			String org_id = logonUser.getOrgId().toString();
			conMap.put("dutyType", dutyType);
			conMap.put("org_id", org_id);
			String downType = CommonUtils.checkNull(request.getParamValue("donwtype"));// 1：汇总、2：明细
			// 导出的文件名
			String fileName = "月度任务.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			List<List<Object>> list = null;
			if ("1".equals(downType)) {
				List<Map<String, Object>> serlist = PlanUtil.selectAreaGroup( logonUser.getCompanyId().toString(), 2);
				String dealerCode = request.getParamValue("dealerCode");
				conMap.put("dealerCode", dealerCode);
				list = getTotalDownLoadList(conMap, serlist, chgnType);
			} else {
				String dealerCode = request.getParamValue("dealerCode");
				conMap.put("dealerCode", dealerCode);
				list = getDealerDetailDownLoadList(conMap);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "最大配额总量查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * 汇总查询下载
	 */
	private List<List<Object>> getTotalDownLoadList(Map<String, Object> conMap, List<Map<String, Object>> serlist, String chgnType) {
		MonthPlanDao dao = MonthPlanDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();
		List<Object> listTemp = new LinkedList<Object>();
		List<Map<String, Object>> results = null;
		listTemp.add("经销商代码");
		listTemp.add("经销商名称");
		// listTemp.add("任务类型");
		for (int i = 0; i < serlist.size(); i++) {
			Map<String, Object> map = serlist.get(i);
			listTemp.add(map.get("GROUP_NAME").toString());
		}
		listTemp.add("合计");
		list.add(listTemp);
		results = dao.oemSelectDealerTotalMonthPlanTotalDown(conMap, serlist);
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
			// listTemp.add(CommonUtils.checkNull(record.get("PLAN_TYPE")));
			for (int j = 0; j < serlist.size(); j++) {
				listTemp.add(CommonUtils.checkNull(record.get("S" + j)));
			}
			listTemp.add(CommonUtils.checkNull(record.get("SALE_AMOUNT")));
			list.add(listTemp);
		}
		return list;
	}

	/*
	 * org明细查询下载
	 */
	private List<List<Object>> getOrgDetailDownLoadList(Map<String, Object> conMap) {
		MonthPlanDao dao = MonthPlanDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();
		List<Object> listTemp = new LinkedList<Object>();
		// listTemp.add("区域代码");
		// listTemp.add("区域");
		// listTemp.add("车系代码");
		// listTemp.add("车系");
		// listTemp.add("月度任务");
		listTemp.add("区域代码");
		listTemp.add("区域名称");
		listTemp.add("经销商代码");
		listTemp.add("经销商");
		listTemp.add("任务类型");
		listTemp.add("车系代码");
		listTemp.add("车系");
		listTemp.add("月度任务");
		list.add(listTemp);
		List<Map<String, Object>> results = dao.oemSelectOrgDetailMonthPlanDown(conMap);
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			// listTemp.add(CommonUtils.checkNull(record.get("ORG_CODE")));
			// listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("ORG_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
			listTemp.add(CommonUtils.checkNull(record.get("PLAN_TYPE")));
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("SALE_AMOUNT")));
			list.add(listTemp);
		}
		return list;
	}

	/*
	 * dealer明细查询下载
	 */
	private List<List<Object>> getDealerDetailDownLoadList(Map<String, Object> conMap) {
		MonthPlanDao dao = MonthPlanDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();
		List<Object> listTemp = new LinkedList<Object>();
		/*
		 * listTemp.add("经销商代码"); listTemp.add("经销商"); listTemp.add("车系代码");
		 * listTemp.add("车系"); listTemp.add("月度任务");
		 */
		listTemp.add("区域代码");
		listTemp.add("区域名称");
		listTemp.add("经销商代码");
		listTemp.add("经销商");
		// listTemp.add("任务类型");
		listTemp.add("车系代码");
		listTemp.add("车系");
		listTemp.add("月度任务");
		list.add(listTemp);
		List<Map<String, Object>> results = dao.oemSelectDealerDetailMonthPlanDown(conMap);
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("ORG_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
			// listTemp.add(CommonUtils.checkNull(record.get("PLAN_TYPE")));
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("SALE_AMOUNT")));
			list.add(listTemp);
		}
		return list;
	}
}
