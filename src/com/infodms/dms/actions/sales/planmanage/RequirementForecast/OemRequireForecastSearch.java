package com.infodms.dms.actions.sales.planmanage.RequirementForecast;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
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
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.planmanage.OemRequireForecastReportDao;
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

public class OemRequireForecastSearch {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String requireforecastSearchInitUrl = "/jsp/sales/planmanage/requireforecast/oemrequireforecastsearch.jsp";
	
	/*
	 * 初始化查询页面
	 */
	public void oemRequireForecastSearchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);

        String dutyType=logonUser.getDutyType(logonUser.getDealerId());

		YearPlanDao dao=YearPlanDao.getInstance();
		try {
			List<Map<String, Object>> serlist=dao.selectSeries(logonUser.getCompanyId().toString());
			act.setOutData("serlist", serlist);
			List<String> yearList=new ArrayList();
			List<String> monthList=new ArrayList();
			//查询目标年份
			Calendar calendar=Calendar.getInstance();
			yearList.add(calendar.get(Calendar.YEAR)+"");
			monthList.add(calendar.get(Calendar.MONTH)+"");
			calendar.add(Calendar.YEAR, 1);
			yearList.add(calendar.get(Calendar.YEAR)+"");
			act.setOutData("monthList", monthList);
			act.setOutData("yearList", yearList);
			String duty=logonUser.getDutyType();
			String orgId=logonUser.getOrgId().toString();
			if(Constant.DUTY_TYPE_DEPT.toString().equals(duty)){
				orgId=logonUser.getParentOrgId();
			}
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaBusList", areaBusList);
			act.setOutData("orgId", orgId);
			
			List<Map<String, Object>> forecastTypeList=MaterialGroupManagerDao.getForecastType(6059);//zxf
			act.setOutData("forecastTypeList", forecastTypeList);
			act.setOutData("dutyType", dutyType);

			act.setForword(requireforecastSearchInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 大区查找
	 * */
	public void OrgRequireFindAll(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao=OemRequireForecastReportDao.getInstance();
		try {	
			 RequestWrapper request=act.getRequest();
			 String orgCode =  request.getParamValue("orgCode");
			 String orgName = request.getParamValue("orgName");
			 String companyId = logonUser.getCompanyId()+"";
			 
			 PageResult<Map<String, Object>> ps = dao.OrgRequireFindAllDao(orgCode,orgName,companyId,20,1);
			 act.setOutData("ps", ps);
		} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
				logger.error(logonUser,e1);
				act.setException(e1);
		}
	}	
	
	
	/*
	 * 明细查询
	 */
	public void oemRequireForecastDetailSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OemRequireForecastReportDao dao=OemRequireForecastReportDao.getInstance();
		try {
			Map<String, Object> conMap=new HashMap<String, Object>();
			
			String dutyType=logonUser.getDutyType(logonUser.getDealerId());
			String logonOrgType="";
			//如果登录的是大区
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
				logonOrgType="LARGEREGION";
			}
			//如果登录的是小区
			if(Constant.DUTY_TYPE_SMALLREGION.toString().equals(dutyType)){
				logonOrgType="SMALLREGION";
			}
			//如果登录的是公司
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)){
				logonOrgType="COMPANY";
			}
			RequestWrapper request=act.getRequest();
			String plan_year=request.getParamValue("year");
			String plan_month=request.getParamValue("month");
			String forecast_type=request.getParamValue("forecasttype"); //zxf
			String groupCode=request.getParamValue("groupCode")==null?"":request.getParamValue("groupCode");
			String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
            String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));

			
			conMap.put("plan_year", plan_year);
			conMap.put("plan_month", plan_month);
			conMap.put("forecast_type", forecast_type); //zxf
			conMap.put("groupCode", groupCode);
			conMap.put("logonOrgType", logonOrgType);
			conMap.put("logonOrgId", logonUser.getOrgId().toString());
			conMap.put("companyId", logonUser.getCompanyId());
			conMap.put("areaId", areaId);
			
			PageResult<Map<String, Object>> ps=null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")): 1; 
			String chgnType=request.getParamValue("chngType");
			//2：组织、1：经销商
			if("2".equals(chgnType)){
				String orgCode=request.getParamValue("orgCode");
				conMap.put("orgCode", orgCode);
				ps=dao.oemSelectOrgRequireForecast(conMap,Integer.parseInt(pageSize), curPage);
			}else{
				String dealerCode=request.getParamValue("dealerCode");
				conMap.put("dealerCode", dealerCode);
				
				String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
				 if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
					 ps=dao.oemSelectDealerRequireForecast(conMap, Integer.parseInt(pageSize), curPage);
					} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
						ps=dao.oemSelectDealerRequireForecast_CVS(conMap, Integer.parseInt(pageSize), curPage);
					} else {
						throw new RuntimeException("判断当前系统的系统参数错误！") ;
					}
			}
			act.setOutData("ps", ps);
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
			String dutyType=logonUser.getDutyType(logonUser.getDealerId());
			String logonOrgType="";
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
				logonOrgType="LARGEREGION";
			}
			
			if(Constant.DUTY_TYPE_SMALLREGION.toString().equals(dutyType)){
				logonOrgType="SMALLREGION";
			}

			
			RequestWrapper request=act.getRequest();
			String forecast_year=request.getParamValue("year");
			String forecast_month=request.getParamValue("month");
			String forecast_type=request.getParamValue("forecasttype"); //zxf
			String groupCode=request.getParamValue("groupCode")==null?"":request.getParamValue("groupCode");
			//String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
			
			conMap.put("forecast_year", forecast_year);
			conMap.put("forecast_month", forecast_month);
			conMap.put("forecast_type", forecast_type); //zxf
			conMap.put("logonOrgType", logonOrgType);
			conMap.put("logonOrgId", logonUser.getOrgId().toString());
			conMap.put("groupCode", groupCode);
			conMap.put("companyId", logonUser.getCompanyId());
			//conMap.put("areaId", areaId);
			conMap.put("dutyType", dutyType);
			String chgnType=request.getParamValue("chngType");
			List<Map<String, Object>> serlist=PlanUtil.selectAreaGroup( logonUser.getCompanyId().toString(), 2);
			
			PageResult<Map<String, Object>> ps=null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")): 1; 
			//DUTY_TYPE_LARGEREGION：区域、ELSE 是部门，区域汇总下级经销商上报数据和区域预测数据
			//部门汇总区域上报数据和部门预测数据
			
			

			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
				if("2".equals(chgnType)){
					String orgCode=request.getParamValue("orgCode");
					conMap.put("orgCode", orgCode);
					ps=dao.oemSelectOrgForecastTotal(conMap,serlist,Constant.PAGE_SIZE_MAX, curPage);
				}else{
					String dealerCode=request.getParamValue("dealerCode");
					conMap.put("dealerCode", dealerCode);
					ps=dao.oemSelectDealerMonthForecastTotal(conMap,serlist,Constant.PAGE_SIZE_MAX, curPage);
				}
			}else{
				if("2".equals(chgnType)){
					String orgCode=request.getParamValue("orgCode");
					conMap.put("orgCode", orgCode);
					ps=dao.oemSelectOrgForecastTotal(conMap,serlist,Constant.PAGE_SIZE_MAX, curPage);
				}else{
					String dealerCode=request.getParamValue("dealerCode");
					conMap.put("dealerCode", dealerCode);
					ps=dao.oemSelectDealerMonthForecastTotal(conMap,serlist,Constant.PAGE_SIZE_MAX, curPage);
				}
				
			}
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
	public void oemRequireForecastExport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();
			Map<String, Object> conMap=new HashMap<String, Object>();
			String dutyType=logonUser.getDutyType();
			String logonOrgType="";
			//如果登录的是大区
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
				logonOrgType="LARGEREGION";
			}
			//如果登录的是小区
			if(Constant.DUTY_TYPE_SMALLREGION.toString().equals(dutyType)){
				logonOrgType="SMALLREGION";
			}
			//如果登录的是公司
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)){
				logonOrgType="COMPANY";
			}
			String plan_year=request.getParamValue("year");
			String plan_month=request.getParamValue("month");
			String forecast_type=request.getParamValue("forecasttype"); //zxf
			String groupCode=request.getParamValue("groupCode")==null?"":request.getParamValue("groupCode");
			String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
			List<Map<String, Object>> serlist=PlanUtil.selectAreaGroup( logonUser.getCompanyId().toString(), 2);
			conMap.put("plan_year", plan_year);
			conMap.put("plan_month", plan_month);
			conMap.put("logonOrgType", logonOrgType);
			conMap.put("logonOrgId", logonUser.getOrgId().toString());
			conMap.put("groupCode", groupCode);
			conMap.put("companyId", logonUser.getCompanyId());
			conMap.put("areaId", areaId);
			conMap.put("forecast_type", forecast_type);
			
			String downType=CommonUtils.checkNull(request.getParamValue("donwtype"));//1：汇总、2：明细
			String chgnType=request.getParamValue("chngType");
			// 导出的文件名
			String fileName = "需求预测.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			List<List<Object>> list=null;
			if("1".equals(downType)){
				if("2".equals(chgnType)){
					String orgCode=request.getParamValue("orgCode");
					conMap.put("orgCode", orgCode);
					list=getOrgTotalDownLoadList(conMap,serlist);
				}else{
					String dealerCode=request.getParamValue("dealerCode");
					conMap.put("dealerCode", dealerCode);
					list=getDLRTotalDownLoadList(conMap,serlist);
				}
			}else{
				if("2".equals(chgnType)){
					String orgCode=request.getParamValue("orgCode");
					conMap.put("orgCode", orgCode);
					list=getOrgDetailDownLoadList(conMap);
				}else{
					String dealerCode=request.getParamValue("dealerCode");
					conMap.put("dealerCode", dealerCode);
					list=getDealerDetailDownLoadList(conMap);
				}
			}
			
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "需求预测查询");
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
	 * 区域汇总查询下载
	 */
	private List<List<Object>> getOrgTotalDownLoadList(Map<String, Object> conMap,List<Map<String,Object>> serlist){
		OemRequireForecastReportDao dao=OemRequireForecastReportDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();
		List<Object> listTemp = new LinkedList<Object>();
		List<Map<String, Object>> results = null;
		listTemp.add("区域代码");
		listTemp.add("区域名称");
		int len = 0;
		if (serlist != null && serlist.size()>0) {
			len = serlist.size() ;
			for(int i=0; i<len;i++) {
				listTemp.add(serlist.get(i).get("GROUP_NAME").toString()) ;
			}
		}
		listTemp.add("合计");
		list.add(listTemp);
			results= dao.SelectOrgForecastTotal(conMap,serlist);
			// results= dao.oemSelectDeptRequireForecastTotalDown(conMap);
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("ORG_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
			for (int j=0; j<len; j++) {
				listTemp.add(record.get("S"+j)) ;
			}
			listTemp.add(CommonUtils.checkNull(record.get("FA")));
			list.add(listTemp);
		}
		return list;
	}
	/*
	 * 经销商汇总查询下载
	 */
	private List<List<Object>> getDLRTotalDownLoadList(Map<String, Object> conMap,List<Map<String,Object>> serlist){
		OemRequireForecastReportDao dao=OemRequireForecastReportDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();
		List<Object> listTemp = new LinkedList<Object>();
		List<Map<String, Object>> results = null;
		listTemp.add("经销商代码");
		listTemp.add("经销商名称");
		int len = 0;
		if (serlist != null && serlist.size()>0) {
			len = serlist.size() ;
			for(int i=0; i<len;i++) {
				listTemp.add(serlist.get(i).get("GROUP_NAME").toString()) ;
			}
		}
		listTemp.add("合计");
		list.add(listTemp);
			results= dao.SelectDLRForecastTotal(conMap,serlist);
			// results= dao.oemSelectDeptRequireForecastTotalDown(conMap);
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
			for (int j=0; j<len; j++) {
				listTemp.add(record.get("S"+j)) ;
			}
			listTemp.add(CommonUtils.checkNull(record.get("FA")));
			list.add(listTemp);
		}
		return list;
	}
	/*
	 * org明细查询下载
	 */
	private List<List<Object>> getOrgDetailDownLoadList(Map<String, Object> conMap){
		OemRequireForecastReportDao dao=OemRequireForecastReportDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		List<Object> listTemp = new LinkedList<Object>();
		listTemp.add("组织代码");
		listTemp.add("组织名称");
		listTemp.add("月份");
		listTemp.add("产品组代码");
		listTemp.add("产品组名称");
		
		if(logonUser.getDutyType(logonUser.getDealerId()).toString().equals(Constant.DUTY_TYPE_LARGEREGION.toString())){
			listTemp.add("大区数量");
			listTemp.add("经销商数量");
		}
		if(logonUser.getDutyType(logonUser.getDealerId()).toString().equals(Constant.DUTY_TYPE_COMPANY.toString())){
			listTemp.add("车厂数量");
			listTemp.add("大区数量");
			listTemp.add("经销商数量");
		}
		list.add(listTemp);
		RequestWrapper request=act.getRequest();
		Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")): 1; 
		PageResult<Map<String, Object>> ps=dao.oemSelectOrgRequireForecast(conMap,999999, curPage);
		List<Map<String, Object>> results =ps.getRecords();
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("ORG_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("FORECAST_MONTH"))+"");
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE"))+"");
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
			if(logonUser.getDutyType(logonUser.getDealerId()).toString().equals(Constant.DUTY_TYPE_LARGEREGION.toString())){
				listTemp.add(CommonUtils.checkNull(record.get("FORECAST_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_FORECAST_AMOUNT")));
			}
			if(logonUser.getDutyType(logonUser.getDealerId()).toString().equals(Constant.DUTY_TYPE_COMPANY.toString())){
				listTemp.add(CommonUtils.checkNull(record.get("FORECAST_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("LARGE_FORECAST_AMOUNT")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_FORECAST_AMOUNT")));
			}
			list.add(listTemp);
		}
		return list;
	}
	public void selectAreaGroup(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		 // MonthPlanDao dao=MonthPlanDao.getInstance();
		try {
			RequestWrapper request=act.getRequest();
		    String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
		    List<Map<String, Object>> list=PlanUtil.selectAreaGroup(logonUser.getCompanyId().toString(), 2);
			act.setOutData("serList", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * dealer明细查询下载
	 */
	private List<List<Object>> getDealerDetailDownLoadList(Map<String, Object> conMap){
		OemRequireForecastReportDao dao=OemRequireForecastReportDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();

		List<Object> listTemp = new LinkedList<Object>();
		
		listTemp.add("经销商代码");
		listTemp.add("经销商");
		listTemp.add("月份");
		listTemp.add("产品组代码");
		listTemp.add("产品组名称");
		listTemp.add("数量");
		list.add(listTemp);

		List<Map<String, Object>> results =dao.oemSelectDealerRequireForecastDown(conMap);
		
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
			listTemp.add(CommonUtils.checkNull(record.get("FORECAST_MONTH")+""));
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("FORECAST_AMOUNT")));
			list.add(listTemp);
		}
		return list;
	}
}
