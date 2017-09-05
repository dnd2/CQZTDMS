package com.infodms.dms.actions.sales.planmanage.YearTarget;

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
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

public class DealerYearTargetSearch {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String yearTargetSearchInitUrl = "/jsp/sales/planmanage/yearplan/dealeryearplansearchinit.jsp";
	
	private void tmp(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 初始化查询页面
	 */
	public void dealerYearTargetSearchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao=YearPlanDao.getInstance();
		try {
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaBusList", areaBusList);
			List<Map<String, Object>> serlist=PlanUtil.selectSeries(logonUser.getOemCompanyId());
			act.setOutData("serlist", serlist);
			List<String> yearList=new ArrayList();
			//查询目标年份
			Calendar calendar=Calendar.getInstance();
			yearList.add(calendar.get(Calendar.YEAR)+"");
			calendar.add(Calendar.YEAR, 1);
			yearList.add(calendar.get(Calendar.YEAR)+"");
			act.setOutData("yearList", yearList);
			
			act.setForword(yearTargetSearchInitUrl);
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
		YearPlanDao dao=YearPlanDao.getInstance();
		try {
			RequestWrapper request=act.getRequest();
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			String planType=CommonUtils.checkNull(request.getParamValue("planType"));//计划类型
			String buss_area=CommonUtils.checkNull(request.getParamValue("buss_area"));//业务范围
			List<Map<String, Object>> list =dao.selectDealerMaxPlanVer(year,logonUser.getOemCompanyId(),logonUser.getDealerId(),buss_area,planType);
			act.setOutData("maxVer", list);
			
			act.setForword(yearTargetSearchInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 总部明细查询
	 */
	public void dealerYearlyPlanSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao=YearPlanDao.getInstance();
		try {
			Map<String, Object> conMap=new HashMap<String, Object>();
			RequestWrapper request=act.getRequest();
			String plan_year=request.getParamValue("year")==null?"0":request.getParamValue("year");
			String plan_ver=request.getParamValue("plan_ver")==null?"0":request.getParamValue("plan_ver");
			String plan_desc=request.getParamValue("plan_desc");
			String buss_area=request.getParamValue("buss_area");
			String planType=request.getParamValue("planType");
			
			conMap.put("plan_year", plan_year);
			conMap.put("plan_ver", plan_ver);
			conMap.put("plan_desc", plan_desc);
			conMap.put("buss_area", buss_area);
			conMap.put("planType", planType);
			conMap.put("dealerId", logonUser.getDealerId());
			conMap.put("companyId", logonUser.getOemCompanyId());
			
			PageResult<Map<String, Object>> ps=null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")): 1; 
			ps=dao.dealerSelectDetailYearlyPlan(conMap, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 年度目标下载
	 */
	public void dealerYearPlanExport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			Map<String, Object> conMap=new HashMap<String, Object>();
			String dutyType=logonUser.getDutyType();
			String logonOrgType="";
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
				logonOrgType="LARGEREGION";
			}
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();

			String plan_year=request.getParamValue("year");
			String plan_ver=request.getParamValue("plan_ver");
			String plan_desc=request.getParamValue("plan_desc");
			String buss_area=request.getParamValue("buss_area");
			String planType=request.getParamValue("planType");
			
			conMap.put("plan_year", plan_year);
			conMap.put("plan_ver", plan_ver);
			conMap.put("plan_desc", plan_desc);
			conMap.put("buss_area", buss_area);
			conMap.put("planType", planType);
			conMap.put("dealerId", logonUser.getDealerId());
			conMap.put("companyId", logonUser.getOemCompanyId());
			

			// 导出的文件名
			String fileName = "年度目标.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			List<List<Object>> list=getDealerDetailDownLoadList(conMap);
			
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
	 * dealer明细查询下载
	 */
	private List<List<Object>> getDealerDetailDownLoadList(Map<String, Object> conMap){
		YearPlanDao dao=YearPlanDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();

		List<Object> listTemp = new LinkedList<Object>();
		
		listTemp.add("车系代码");
		listTemp.add("车系");
		listTemp.add("合计");
		for (int i = 1; i <13; i++) {
			listTemp.add(i+"月");
		}
		list.add(listTemp);

		List<Map<String, Object>> results =dao.dealerSelectDetailYearlyPlanDown(conMap);
		
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("AMOUNT")));
			for (int j = 1; j < 13; j++) {
				listTemp.add(CommonUtils.checkNull(record.get("M" + j)));
			}
			list.add(listTemp);
		}
		return list;
	}
}
