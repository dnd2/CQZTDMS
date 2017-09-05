package com.infodms.dms.actions.sales.planmanage.RequirementForecast;

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
import com.infodms.dms.dao.sales.planmanage.OemRequireForecastReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

public class DealerRequireForecastSearch {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String requireforecastSearchInitUrl = "/jsp/sales/planmanage/requireforecast/dealerrequireforecastsearch.jsp";
	
	/*
	 * 初始化查询页面
	 */
	public void dealerRequireForecastSearchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> serlist=PlanUtil.selectSeries(logonUser.getOemCompanyId());
			act.setOutData("serlist", serlist);
			List<String> yearList=new ArrayList();
			List<String> monthList=new ArrayList();
			//查询目标年份
			Calendar calendar=Calendar.getInstance();
			yearList.add(calendar.get(Calendar.YEAR)+"");
			monthList.add(calendar.get(Calendar.MONTH)+"");
			calendar.add(Calendar.YEAR, 1);
			yearList.add(calendar.get(Calendar.YEAR)+"");
			act.setOutData("yearList", yearList);
			act.setOutData("monthList", monthList);
			
			List<Map<String, Object>> forecastTypeList=MaterialGroupManagerDao.getForecastType(6059);//zxf
			act.setOutData("forecastTypeList", forecastTypeList);
			
			String a=monthList.get(0);
			//业务范围查询
			 List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getDealerBusiness(logonUser.getPoseId().toString());
			 act.setOutData("areaBusList", areaBusList);
			act.setForword(requireforecastSearchInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 车厂汇总查询
	 */
	public void oemRequireForecastTotalSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao=OemRequireForecastReportDao.getInstance();
		try {
			Map<String, Object> conMap=new HashMap<String, Object>();
			RequestWrapper request=act.getRequest();
			String plan_year=CommonUtils.checkNull(request.getParamValue("year"));
			String plan_month=CommonUtils.checkNull(request.getParamValue("month"));
			String forecast_type=request.getParamValue("forecasttype"); //zxf
			String groupCode=CommonUtils.checkNull(request.getParamValue("groupCode"));
			String areaIdstr=CommonUtils.checkNull(request.getParamValue("areaId"));
			//String areaId="";
			String dealerId="";
			if(areaIdstr.indexOf(",")!=-1){
				//areaId=areaIdstr.split(",")[0];
				dealerId=areaIdstr.split(",")[1];
			}else{
				//areaId=areaIdstr;
				dealerId=logonUser.getDealerId().toString();
			}
			
			conMap.put("plan_year", plan_year);
			conMap.put("plan_month", plan_month);
			conMap.put("forecast_type", forecast_type); //zxf
			conMap.put("groupCode", groupCode);
			conMap.put("dealerId", dealerId);
			conMap.put("companyId", logonUser.getOemCompanyId());
			//conMap.put("areaId", areaId);
			
			PageResult<Map<String, Object>> ps=null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")): 1; 
			ps=dao.dealerSelectRequireForecast(conMap, Constant.PAGE_SIZE_MAX, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 需求预测下载
	 */
	public void dealerRequireForecastExport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();
			Map<String, Object> conMap=new HashMap<String, Object>();
			String plan_year=CommonUtils.checkNull(request.getParamValue("year"));
			String plan_month=CommonUtils.checkNull(request.getParamValue("month"));
			String groupCode=CommonUtils.checkNull(request.getParamValue("groupCode"));
			String areaIdstr=CommonUtils.checkNull(request.getParamValue("areaId"));
			String areaId="";
			String dealerId="";
			if(areaIdstr.indexOf(",")!=-1){
				areaId=areaIdstr.split(",")[0];
				dealerId=areaIdstr.split(",")[1];
			}else{
				areaId=areaIdstr;
				dealerId=logonUser.getDealerId().toString();
			}
			
			conMap.put("plan_year", plan_year);
			conMap.put("plan_month", plan_month);
			conMap.put("groupCode", groupCode);
			conMap.put("dealerId", dealerId);
			conMap.put("companyId", logonUser.getOemCompanyId());
			conMap.put("areaId", areaId);
			
			// 导出的文件名
			String fileName = "需求预测.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			List<List<Object>> list=getTotalDownLoadList(conMap);
			
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "最大配额总量查询");
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
	private List<List<Object>> getTotalDownLoadList(Map<String, Object> conMap){
		OemRequireForecastReportDao dao=OemRequireForecastReportDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();
		List<Object> listTemp = new LinkedList<Object>();
		List<Map<String, Object>> results = null;
		listTemp.add("车系代码");
		listTemp.add("车系名称");
		listTemp.add("车型代码");
		listTemp.add("车型名称");
		listTemp.add("配置代码");
		listTemp.add("配置名称");
		listTemp.add("预测数量");
		list.add(listTemp);
	    results= dao.dealerSelectRequireForecastDown(conMap);
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("SERIES_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("SERIES_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("MODEL_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("MODEL_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("REPORT_AMOUNT")));
			list.add(listTemp);
		}
		return list;
	}
}
