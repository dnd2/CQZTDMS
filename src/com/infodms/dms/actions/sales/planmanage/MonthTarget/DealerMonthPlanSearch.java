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

public class DealerMonthPlanSearch {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String monthplanSearchInitUrl = "/jsp/sales/planmanage/monthplan/dealermonthplansearch.jsp";
	
	/*
	 * 初始化查询页面
	 */
	public void dealerMonthPlanSearchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao=YearPlanDao.getInstance();
		try {
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaBusList", areaBusList);
				
			List<Map<String, Object>> serlist=PlanUtil.selectSeries(logonUser.getOemCompanyId());;
			act.setOutData("serlist", serlist);
			List<String> yearList=new ArrayList();
			//查询目标年份
			Calendar calendar=Calendar.getInstance();
			yearList.add(calendar.get(Calendar.YEAR)+"");
			calendar.add(Calendar.YEAR, 1);
			yearList.add(calendar.get(Calendar.YEAR)+"");
			act.setOutData("yearList", yearList);
			
			act.setForword(monthplanSearchInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 查询最大的版本号
	 */
	public void selectMaxPlanVer(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		MonthPlanDao dao=MonthPlanDao.getInstance();
		try {
			RequestWrapper request=act.getRequest();
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String month=CommonUtils.checkNull(request.getParamValue("month"));
			String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
			String maxVer=dao.dealerSelectMaxPlanVer(year,month,logonUser.getOemCompanyId().toString(),logonUser.getDealerId().toString(),areaId);
			act.setOutData("maxVer", maxVer);
			//act.setForword(monthplanSearchInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 总部明细查询
	 */
	public void dealerMonthPlanSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		MonthPlanDao dao=MonthPlanDao.getInstance();
		try {
			Map<String, Object> conMap=new HashMap<String, Object>();
			
			RequestWrapper request=act.getRequest();
			String plan_year=CommonUtils.checkNull(request.getParamValue("year"));
			String plan_month=CommonUtils.checkNull(request.getParamValue("month"));
			String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
			String plan_ver=CommonUtils.checkNull(request.getParamValue("plan_ver"));
			String plan_desc=CommonUtils.checkNull(request.getParamValue("plan_desc"));
			/**
			 * 增加任务类型查询条件 modify by zhaolunda 2010-08-19
			 */
			String task_type=CommonUtils.checkNull(request.getParamValue("task_type"));
			if(null==plan_ver||"".equals(plan_ver)){
				plan_ver="0";
			}
			conMap.put("plan_year", plan_year);
			conMap.put("plan_month", plan_month);
			conMap.put("areaId", areaId);
			conMap.put("plan_ver", plan_ver);
			conMap.put("plan_desc", plan_desc);
			conMap.put("dealerId", logonUser.getDealerId().toString());
			conMap.put("companyId", logonUser.getOemCompanyId().toString());
			conMap.put("task_type", task_type);
			
			PageResult<Map<String, Object>> ps=null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")): 1; 
			ps=dao.dealerSelectMonthPlan(conMap, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 月度任务下载
	 */
	public void dealerMonthPlanSearchExport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			Map<String, Object> conMap=new HashMap<String, Object>();
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();

			String plan_year=CommonUtils.checkNull(request.getParamValue("year"));
			String plan_month=CommonUtils.checkNull(request.getParamValue("month"));
			String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
			String plan_ver=CommonUtils.checkNull(request.getParamValue("plan_ver"));
			String plan_desc=CommonUtils.checkNull(request.getParamValue("plan_desc"));
			
			if(null==plan_ver||"".equals(plan_ver)){
				plan_ver="0";
			}
			
			conMap.put("plan_year", plan_year);
			conMap.put("plan_month", plan_month);
			conMap.put("plan_ver", plan_ver);
			conMap.put("plan_desc", plan_desc);
			conMap.put("dealerId", logonUser.getDealerId().toString());
			conMap.put("companyId", logonUser.getOemCompanyId().toString());
			conMap.put("areaId", areaId);
			

			// 导出的文件名
			String fileName = "月度任务.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			 List<List<Object>> list=getDealerMonthPlanDownLoadList(conMap);
			
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度任务查询");
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
	 * org明细查询下载
	 */
	private List<List<Object>> getDealerMonthPlanDownLoadList(Map<String, Object> conMap){
		MonthPlanDao dao=MonthPlanDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();

		List<Object> listTemp = new LinkedList<Object>();
		listTemp.add("经销商代码");
		listTemp.add("经销商");
		listTemp.add("车系代码");
		listTemp.add("车系");
		listTemp.add("月度任务");
		list.add(listTemp);

		List<Map<String, Object>> results =dao.dealerSelectMonthPlanDown(conMap);
		
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("SALE_AMOUNT")));
			list.add(listTemp);
		}
		return list;
	}
}
