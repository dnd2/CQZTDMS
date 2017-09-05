package com.infodms.dms.actions.sales.planmanage.RequirementForecast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.planmanage.RequireForecastDifferenceReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

public class RequireForecastDifferenceSearch {

	public Logger logger = Logger.getLogger(RequireForecastDifferenceSearch.class);

	private final String requireForecastDifferenceSearchInitUrl = "/jsp/sales/planmanage/requireforecast/requireforecastdifferencesearch.jsp";

	/**
	 * 需求预测差异初始化查询页面
	 */
	public void requireForecastDifferenceSearchInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<String> yearList = new ArrayList();
			List<String> monthList = new ArrayList();
			// 查询目标年份
			Calendar calendar = Calendar.getInstance();
			yearList.add(calendar.get(Calendar.YEAR) + "");
			monthList.add(calendar.get(Calendar.MONTH) + "");
			calendar.add(Calendar.YEAR, 1);
			yearList.add(calendar.get(Calendar.YEAR) + "");
			act.setOutData("monthList", monthList);
			act.setOutData("yearList", yearList);
			String duty = logonUser.getDutyType();
			String orgId = logonUser.getOrgId().toString();
			if (Constant.DUTY_TYPE_DEPT.toString().equals(duty)) {
				orgId = logonUser.getParentOrgId();
			}
			List<Map<String, Object>> areaBusList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaBusList", areaBusList);
			act.setOutData("orgId", orgId);
			act.setForword(requireForecastDifferenceSearchInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 需求预测差异汇总查询
	 * */
	public void requireForecastTotalSearch() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequireForecastDifferenceReportDao dao = RequireForecastDifferenceReportDao.getInstance();
		try {
			Map<String, Object> conMap = new HashMap<String, Object>();
			RequestWrapper request = act.getRequest();
			String forecast_year = request.getParamValue("year");
			String forecast_month = request.getParamValue("month");
			//大区
			String orgCode = request.getParamValue("orgCode");
			//物料组
			String groupCode = request.getParamValue("groupCode") == null ? "" : request.getParamValue("groupCode");
			//业务范围
			String areaId = CommonUtils.checkNull(request.getParamValue("buss_area"));

			conMap.put("forecast_year", forecast_year);
			conMap.put("forecast_month", forecast_month);
			conMap.put("logonOrgType", logonUser.getOrgType().toString());
			conMap.put("groupCode", groupCode);
			conMap.put("orgCode", orgCode);
			conMap.put("areaId", areaId);
			PageResult<Map<String, Object>> ps = null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			// 查询需求差异预测数据
			ps = dao.selectDealerMonthForecastTotal(conMap, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 需求预测差异下载
	 * */
	public void requireForecastDifferenceExport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		OutputStream os = null;
		try {
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();
			Map<String, Object> conMap = new HashMap<String, Object>();
			String forecast_year = request.getParamValue("year");
			String forecast_month = request.getParamValue("month");
			String groupCode = request.getParamValue("groupCode") == null ? "" : request.getParamValue("groupCode");
			String areaId = CommonUtils.checkNull(request.getParamValue("buss_area"));
			//大区
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			conMap.put("forecast_year", forecast_year);
			conMap.put("forecast_month", forecast_month);
			conMap.put("groupCode", groupCode);
			conMap.put("areaId", areaId);
			conMap.put("orgCode", orgCode);
			// 导出的文件名
			String fileName = "需求差异预测.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			List<List<Object>> list = null;
			list = getDifferenceTotalList(conMap);
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "需求预测差异查询");
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

	/**
	 * 需求预测差异区域汇总查询下载
	 * @param conMap 参数集合
	 */
	private List<List<Object>> getDifferenceTotalList(Map<String, Object> conMap) {
		RequireForecastDifferenceReportDao dao = RequireForecastDifferenceReportDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();
		List<Object> listTemp = new LinkedList<Object>();
		List<Map<String, Object>> results = null;
		listTemp.add("大区");
		listTemp.add("业务范围");
		listTemp.add("年");
		listTemp.add("月");
		listTemp.add("预测配置");
		listTemp.add("经销商预测");
		listTemp.add("大区预测调整");
		listTemp.add("差异");
		listTemp.add("大区预测人");
		listTemp.add("大区预测调整时间");
		list.add(listTemp);
		results = dao.getDifferenceTotal(conMap);
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("AREA_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("FORECAST_YEAR")));
			listTemp.add(CommonUtils.checkNull(record.get("FORECAST_MONTH")));
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("TT2")));
			listTemp.add(CommonUtils.checkNull(record.get("TT1")));
			listTemp.add(CommonUtils.checkNull(record.get("COMPARE")));
			listTemp.add(CommonUtils.checkNull(record.get("NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("CREATE_DATE")));
			list.add(listTemp);
		}
		return list;
	}
}
