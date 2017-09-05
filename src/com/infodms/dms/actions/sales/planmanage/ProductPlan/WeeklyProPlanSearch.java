package com.infodms.dms.actions.sales.planmanage.ProductPlan;


import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.helpers.DateLayout;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.planmanage.ProductPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

public class WeeklyProPlanSearch {

	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String weeklyProPlanSearchInit = "/jsp/sales/planmanage/productplan/weeklyproplansearch.jsp";
	
	public void weeklyProPlanSearchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<String> dlist=PlanUtil.getAimDate(1);
			int intweek=PlanUtil.getCurIntWeek();
			act.setOutData("intweek", intweek);
			act.setOutData("dlist", dlist);
			
			String curyear=PlanUtil.getCurrentYear();
			String nexyear=PlanUtil.getRadomDate(1, "year");
			String curmonth=PlanUtil.getCurrentMonth();
			act.setOutData("curyear", curyear);
			act.setOutData("nexyear", nexyear);
			act.setOutData("curmonth", curmonth);
			act.setForword(weeklyProPlanSearchInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 周滚动计划查询
	 */
	public void weeklyProPlanSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
		try {
			RequestWrapper request=act.getRequest();
			String year=request.getParamValue("year");
			String month=request.getParamValue("month");
			String week=request.getParamValue("week");
			String groupCode=request.getParamValue("groupCode");
			
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("year", year);
			map.put("month", month);
			map.put("week", week);
			map.put("groupCode", groupCode);
			map.put("companyId", logonUser.getCompanyId().toString());
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao.selectWeeklyPorPlanSearch(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 查询周七天日期
	 * 存在跨月、跨年周的情况，如果不需要查询出七天的日期，刚把月份的注释打开
	 */
	public void getWeekDate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
		try {
			RequestWrapper request=act.getRequest();
			String year=request.getParamValue("year")==null?"":request.getParamValue("year");
			//String month=request.getParamValue("month")==null?"":request.getParamValue("month");
			String week=request.getParamValue("week")==null?"":request.getParamValue("week");
			TmDateSetPO cpo=new TmDateSetPO();
			cpo.setSetYear(year);
			cpo.setSetWeek(week);
			cpo.setCompanyId(logonUser.getCompanyId());
			//年周七天日期
			List<TmDateSetPO> dList=dao.selectDateSetDateList(cpo);
			/*List<String> list=new ArrayList();
			list=getWeekDateOpe(year, month, week);*/
			act.setOutData("list", dList);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//查询周日期
	private List<String> getWeekDateOpe(String year,String month,String week){
		
		ProductPlanDao dao=ProductPlanDao.getInstance();
		TmDateSetPO cpo=new TmDateSetPO();
		cpo.setSetYear(year);
		//cpo.setSetYear(month);
		cpo.setSetWeek(week);
		List<TmDateSetPO> dList=dao.select(cpo);
		List<String> list=new ArrayList();
		for(int i=0;i<dList.size();i++){
 			TmDateSetPO po=(TmDateSetPO)dList.get(i);
 			String s=po.getSetDate();
 			String d=s.substring(s.length()-4,s.length()-2)+"."+s.substring(s.length()-2,s.length());
 			list.add(d);
		}
		return list;
	}
	public void weeklyProPlanExport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		ProductPlanDao dao=ProductPlanDao.getInstance();
		OutputStream os = null;
		try {
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();
			String year = CommonUtils.checkNull(request.getParamValue("year"));
			String month = CommonUtils.checkNull(request.getParamValue("month"));
			String week = CommonUtils.checkNull(request.getParamValue("week"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			
			TmDateSetPO cpo=new TmDateSetPO();
			cpo.setSetYear(year);
			cpo.setSetWeek(week);
			cpo.setCompanyId(logonUser.getCompanyId());
			//年周七天日期
			List<TmDateSetPO> dList=dao.selectDateSetDateList(cpo);
			
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("year", year);
			map.put("month", month);
			map.put("week", week);
			map.put("groupCode", groupCode);
			map.put("companyId", logonUser.getCompanyId());
			
			List<String> dateList=getWeekDateOpe(year, month, week);
			// 导出的文件名
			String fileName = "周滚动计划.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("配置代码");
			listTemp.add("配置名称");
			listTemp.add("物料代码");
			listTemp.add("颜色");
			listTemp.add("周度合计");
			String s="";
			for (int i = 0; i < dList.size(); i++) {
				TmDateSetPO po=dList.get(i);
				s=po.getSetDate();
				s=s.substring(4,6)+"."+s.substring(6,8);
				
			/*	switch (i) {
				case 0:
					s="日";
					break;
				case 1:
					s="一";
					break;
				case 2:
					s="二";
					break;
				case 3:
					s="三";
					break;
				case 4:
					s="四";
					break;
				case 5:
					s="五";
					break;
				case 6:
					s="六";
					break;
				default:
					break;
				}*/
				listTemp.add(s);
			}
			list.add(listTemp);

			List<Map<String, Object>> results = dao.selectWeeklyPorPlanDownLoad(map);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("COLOR_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("WEEK_AMT")));
				listTemp.add(CommonUtils.checkNull(record.get("ONE_AMT")));
				listTemp.add(CommonUtils.checkNull(record.get("TWO_AMT")));
				listTemp.add(CommonUtils.checkNull(record.get("THREE_AMT")));
				listTemp.add(CommonUtils.checkNull(record.get("FOUR_AMT")));
				listTemp.add(CommonUtils.checkNull(record.get("FIVE_AMT")));
				listTemp.add(CommonUtils.checkNull(record.get("SIX_AMT")));
				listTemp.add(CommonUtils.checkNull(record.get("SEVEN_AMT")));
				
				list.add(listTemp);
			}
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
}
