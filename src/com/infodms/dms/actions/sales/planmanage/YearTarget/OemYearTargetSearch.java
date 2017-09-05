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

/***
 * 年度导入查询
 * */
public class OemYearTargetSearch {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String yearTargetSearchInitUrl = "/jsp/sales/planmanage/yearplan/yearplansearchinit.jsp";
	
	/*
	 * 初始化查询页面
	 */
	public void oemYearTargetSearchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao=YearPlanDao.getInstance();
		try {
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaBusList", areaBusList);
			List<Map<String, Object>> serlist=PlanUtil.selectSeries(logonUser.getCompanyId().toString());
			act.setOutData("serlist", serlist);
			List<String> yearList=new ArrayList();
			String style = "inline";
			//查询目标年份
			Calendar calendar=Calendar.getInstance();
			int year=calendar.get(Calendar.YEAR);
			yearList.add(year+"");
			yearList.add(year+1+"");
			act.setOutData("yearList", yearList);
			act.setOutData("curYear", year);
			String duty=logonUser.getDutyType(logonUser.getDealerId());
			String orgId=logonUser.getOrgId().toString();
			if(Constant.DUTY_TYPE_DEPT.toString().equals(duty)){
				orgId=logonUser.getParentOrgId();
			}
			if (Constant.DUTY_TYPE_LARGEREGION.toString().equals(duty)) {
				style = "none";
			}
			act.setOutData("orgId", orgId);
			act.setOutData("style", style);
			act.setOutData("dutyType", duty);
			act.setOutData("userType", logonUser.getUserType());
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
		String dealerId = dao.getDealerIdByPostSql(logonUser);
		try {
			RequestWrapper request=act.getRequest();
			String year=CommonUtils.checkNull(request.getParamValue("year"));
			// String chngType=CommonUtils.checkNull(request.getParamValue("chngType"));//2区域，1经销商
			String planType=CommonUtils.checkNull(request.getParamValue("planType"));//计划类型
			// String buss_area=CommonUtils.checkNull(request.getParamValue("buss_area"));//业务范围
			String list=dao.selectOemMaxPlanVer(year,logonUser.getCompanyId().toString(),dealerId,planType);
			act.setOutData("maxVer", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 总部明细查询
	 */
	public void oemYearlyPlanDetailSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao=YearPlanDao.getInstance();
		try {
			Map<String, Object> conMap=new HashMap<String, Object>();
			String dutyType=logonUser.getDutyType(logonUser.getDealerId());
			String logonOrgType="";
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
				logonOrgType="LARGEREGION";
			}
			RequestWrapper request=act.getRequest();
			String plan_year=request.getParamValue("year");
			String plan_ver=request.getParamValue("plan_ver");
			String plan_desc=request.getParamValue("plan_desc");
			// String buss_area=request.getParamValue("buss_area");
			String planType=request.getParamValue("planType");
			String dealerId = dao.getDealerIdByPostSql(logonUser);
			String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize"));
			conMap.put("plan_year", plan_year);
			conMap.put("plan_ver", plan_ver);
			conMap.put("plan_desc", plan_desc);
			conMap.put("dealerId", dealerId);
			conMap.put("planType", planType);
			conMap.put("logonOrgType", logonOrgType);
			conMap.put("logonOrgId", logonUser.getOrgId().toString());
			conMap.put("companyId", logonUser.getCompanyId());
			PageResult<Map<String, Object>> ps=null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")): 1; 
			String dealerCode=request.getParamValue("dealerCode");
			conMap.put("dealerCode", dealerCode);
			ps = dao.oemSelectDealerYearPlan(logonUser.getDutyType(logonUser.getDealerId()), logonUser.getOrgId().toString(), conMap, Integer.parseInt(pageSize), curPage);
			/*String chgnType=request.getParamValue("chngType");
			//2：组织、1：经销商
			if("2".equals(chgnType)){
				String orgCode=request.getParamValue("orgCode");
				conMap.put("orgCode", orgCode);
				ps=dao.oemSelectYearlyPlan(conMap, Constant.PAGE_SIZE, curPage);
			}else{
				String dealerCode=request.getParamValue("dealerCode");
				conMap.put("dealerCode", dealerCode);
				ps=dao.oemSelectDealerYearlyPlan(conMap, Constant.PAGE_SIZE, curPage);
			}*/
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 汇总查询
	 */
	public void oemYearPlanTotalSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao=YearPlanDao.getInstance();
		String dealerId = dao.getDealerIdByPostSql(logonUser);
		try {
			Map<String, Object> conMap=new HashMap<String, Object>();
			String dutyType=logonUser.getDutyType(logonUser.getDealerId());
			String logonOrgType="";
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
				logonOrgType="LARGEREGION";
			}
			RequestWrapper request=act.getRequest();
			String plan_year=request.getParamValue("year");
			String plan_ver=request.getParamValue("plan_ver");
			String plan_desc=request.getParamValue("plan_desc");
			String planType=request.getParamValue("planType");
			String buss_area=request.getParamValue("buss_area");
			String pageSize = CommonUtils.checkNull(request.getParamValue("pageSize"));
			List<Map<String, Object>> serlist= PlanUtil.selectAreaGroup( logonUser.getCompanyId().toString(), 2);
			// List<Map<String, Object>> serlist=PlanUtil.selectSeries(logonUser.getCompanyId().toString());
			conMap.put("plan_year", plan_year);
			conMap.put("plan_ver", plan_ver);
			conMap.put("plan_desc", plan_desc);
			conMap.put("logonOrgType", logonOrgType);
			conMap.put("logonOrgId", logonUser.getOrgId().toString());
			conMap.put("companyId", logonUser.getCompanyId().toString());
			conMap.put("planType", planType);
			conMap.put("dealerId", dealerId);
			PageResult<Map<String, Object>> ps = null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")): 1; 
			String dealerCode=request.getParamValue("dealerCode");
			conMap.put("dealerCode", dealerCode);
			ps = dao.oemSelectDealerYearPlanTotal(logonUser.getDutyType(logonUser.getDealerId()), logonUser.getOrgId().toString(), conMap, serlist, Integer.parseInt(pageSize), curPage);
			/*String chgnType=request.getParamValue("chngType");
			//2：组织、1：经销商
			if("2".equals(chgnType)){
				String orgCode=request.getParamValue("orgCode");
				conMap.put("orgCode", orgCode);
				ps=dao.oemSelectOrgYearlyPlanTotal(conMap,serlist, Constant.PAGE_SIZE, curPage);
			}else{
				String dealerCode=request.getParamValue("dealerCode");
				conMap.put("dealerCode", dealerCode);
				ps=dao.oemSelectDealerYearlyPlanTotal(conMap, serlist,Constant.PAGE_SIZE, curPage);
			}*/
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
	public void yearPlanSearchExport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao=YearPlanDao.getInstance();
		String dealerId = dao.getDealerIdByPostSql(logonUser);
		OutputStream os = null;
		try {
			Map<String, Object> conMap=new HashMap<String, Object>();
			String dutyType=logonUser.getDutyType(logonUser.getDealerId());
			String logonOrgType="";
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
				logonOrgType="LARGEREGION";
			}
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();
			String chgnType=request.getParamValue("chngType");//2：组织、1：经销商
			String plan_year=request.getParamValue("year");
			String plan_ver=request.getParamValue("plan_ver");
			String plan_desc=request.getParamValue("plan_desc");
			String areaId=request.getParamValue("buss_area");
			String planType=request.getParamValue("planType");
			conMap.put("plan_year", plan_year);
			conMap.put("plan_ver", plan_ver);
			conMap.put("plan_desc", plan_desc);
			conMap.put("buss_area", areaId);
			conMap.put("dealerId", dealerId);
			conMap.put("planType", planType);
			conMap.put("logonOrgType", logonOrgType);
			conMap.put("logonOrgId", logonUser.getOrgId().toString());
			conMap.put("companyId", logonUser.getCompanyId());
			String downType=CommonUtils.checkNull(request.getParamValue("donwtype"));//1：汇总、2：明细
			String org_id = logonUser.getOrgId().toString();
			conMap.put("dutyType", dutyType);
			conMap.put("org_id", org_id);
			// 导出的文件名
			String fileName = "年度目标.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			List<List<Object>> list=null;
			if ("1".equals(downType)) {
				List<Map<String, Object>> serlist = PlanUtil.selectAreaGroup(logonUser.getCompanyId().toString(), 2);
				String dealerCode = request.getParamValue("dealerCode");
				conMap.put("dealerCode", dealerCode);
				list = getTotalDownLoadList(conMap, serlist, chgnType);
			} else {
				String dealerCode = request.getParamValue("dealerCode");
				conMap.put("dealerCode", dealerCode);
				list = getDealerDetailDownLoadList(conMap);
			}
			/*if("1".equals(downType)){
				List<Map<String, Object>> serlist=PlanUtil.selectSeries(logonUser.getCompanyId().toString());
				if("2".equals(chgnType)){
					String orgCode=request.getParamValue("orgCode");
					conMap.put("orgCode", orgCode);
					list=getTotalDownLoadList(conMap,serlist,chgnType);
				}else{
					String dealerCode=request.getParamValue("dealerCode");
					conMap.put("dealerCode", dealerCode);
					list=getTotalDownLoadList(conMap,serlist,chgnType);
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
			}*/
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
	private List<List<Object>> getTotalDownLoadList(Map<String, Object> conMap, List<Map<String, Object>> serlist,String chgnType){
		YearPlanDao dao=YearPlanDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();
		List<Object> listTemp = new LinkedList<Object>();
		List<Map<String, Object>> results = null;
		if("2".equals(chgnType)){
			/*listTemp.add("组织代码");
			listTemp.add("组织名称");
			for (int i = 0; i < serlist.size(); i++) {
				Map<String, Object> map = serlist.get(i);
				listTemp.add(map.get("GROUP_NAME").toString());
			}
			listTemp.add("合计");
			list.add(listTemp);
			results= dao.oemSelectOrgYearlyPlanTotalDown(conMap, serlist);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("ORG_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
				for (int j = 0; j < serlist.size(); j++) {
					listTemp.add(CommonUtils.checkNull(record.get("S" + j)));
				}
				listTemp.add(CommonUtils.checkNull(record.get("SALE_AMOUNT")));
				list.add(listTemp);
			}*/
		}else{
			listTemp.add("经销商代码");
			listTemp.add("经销商名称");
			for (int i = 0; i < serlist.size(); i++) {
				Map<String, Object> map = serlist.get(i);
				listTemp.add(map.get("GROUP_NAME").toString());
			}
			listTemp.add("合计");
			list.add(listTemp);
			results= dao.oemSelectDealerTotalYearPlanTotalDown(conMap, serlist);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
				for (int j = 0; j < serlist.size(); j++) {
					listTemp.add(CommonUtils.checkNull(record.get("S" + j)));
				}
				listTemp.add(CommonUtils.checkNull(record.get("SALE_AMOUNT")));
				list.add(listTemp);
			}
		}
		
		return list;
	}
	
	/*
	 * dealer明细查询下载
	 */
	private List<List<Object>> getDealerDetailDownLoadList(Map<String, Object> conMap) {
		YearPlanDao dao = YearPlanDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();
		List<Object> listTemp = new LinkedList<Object>();
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
	
	/*
	 * org明细查询下载(原来的下载)
	 */
	/*private List<List<Object>> getOrgDetailDownLoadList(Map<String, Object> conMap){
		YearPlanDao dao=YearPlanDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();

		List<Object> listTemp = new LinkedList<Object>();
		listTemp.add("区域代码");
		listTemp.add("区域");
		listTemp.add("车系代码");
		listTemp.add("车系");
		listTemp.add("合计");
		for (int i = 1; i <13; i++) {
			listTemp.add(i+"月");
		}
		list.add(listTemp);

		List<Map<String, Object>> results =dao.oemSelectOrgDetailYearlyPlanDown(conMap);
		
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("ORG_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
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
	*/
	
	/*
	 * dealer明细查询下载
	 */
	/*private List<List<Object>> getDealerDetailDownLoadList(Map<String, Object> conMap){
		YearPlanDao dao=YearPlanDao.getInstance();
		List<List<Object>> list = new LinkedList<List<Object>>();
		List<Object> listTemp = new LinkedList<Object>();
		listTemp.add("区域");
		listTemp.add("经销商代码");
		listTemp.add("经销商");
		listTemp.add("车系代码");
		listTemp.add("车系");
		listTemp.add("合计");
		for (int i = 1; i <13; i++) {
			listTemp.add(i+"月");
		}
		list.add(listTemp);
		List<Map<String, Object>> results =dao.oemSelectDealerDetailYearlyPlanDown(conMap);
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> record = results.get(i);
			listTemp = new LinkedList<Object>();
			listTemp.add(CommonUtils.checkNull(record.get("ORG_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("AMOUNT")));
			for (int j = 1; j < 13; j++) {
				listTemp.add(CommonUtils.checkNull(record.get("M" + j)));
			}
			list.add(listTemp);
		}
		return list;
	}*/
	
	
}
