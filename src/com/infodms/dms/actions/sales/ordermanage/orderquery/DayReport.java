package com.infodms.dms.actions.sales.ordermanage.orderquery;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import jcifs.util.transport.Request;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.storageManage.CheckVehicle;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.ordermanage.orderquery.DayReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmPoseBusinessAreaPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

public class DayReport extends BaseDao{
	public Logger logger = Logger.getLogger(CheckVehicle.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final DayReportDao dao = new DayReportDao ();
	public static final DayReportDao getInstance() {
		return dao;
	}
	private final String  dayReportInitUrl = "/jsp/sales/ordermanage/orderquery/dayReportInit.jsp";
	private final String  dayReportDetailInitUrl = "/jsp/sales/ordermanage/orderquery/dayReportDetailInit.jsp";
	private final String  regionDetailReportInitUrl = "/jsp/sales/ordermanage/orderquery/regionDetailReportInit.jsp";
	/** 销售日报表URL */
	private final String  saleOfDailyReportInitUrl = "/jsp/sales/ordermanage/orderquery/saleOfDailyReportInit.jsp";
	
	/**
	 * FUNCTION		:	当日销售看板：主页面展示
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-10-13
	 */
	public void dayReportInit(){
		ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
		String reqURL = atx.getRequest().getContextPath();
		AclUserBean logonUser = null;
		try {
			String sys = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			if(Constant.COMPANY_CODE_CVS.equals(sys.toUpperCase())) {
				act.setOutData("returnValue", 1);
			} else if(Constant.COMPANY_CODE_JC.equals(sys.toUpperCase())) {
				act.setOutData("returnValue", 2);
			}
			/*if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}*/
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1; 
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String orgId=String.valueOf(logonUser.getOrgId());
			TmOrgPO po=new TmOrgPO();
			po.setOrgId(Long.parseLong(orgId));
			po.setDutyType(Constant.DUTY_TYPE_COMPANY);
			System.out.println(dao.select(po).size());
			if(dao.select(po).size()>0){
				orgId="";
			}
			
			//查询重庆基地的启票实销数据
			String[] cq = {"CQ"} ;
			List<Map<String,Object>> boardCqList = dao.getBoardList(cq);
			act.setOutData("boardCqList", boardCqList);
			//查询河北基地的启票实销数据
			String[] hb = {"HB", "LS"} ;
			List<Map<String,Object>> boardHbList = dao.getBoardList(hb);
			act.setOutData("boardHbList", boardHbList);
			//查询南京基地的启票实销数据
			String[] nj = {"NJ"} ;
			List<Map<String,Object>> boardNjList = dao.getBoardList(nj);
			act.setOutData("boardNjList", boardNjList);
			/*//查询重庆基地的启票实销数据
			List<Map<String,Object>> boardCqList = dao.getBoardListCq_All();
			act.setOutData("boardCqList", boardCqList);
			//查询河北基地的启票实销数据
			List<Map<String,Object>> boardHbList = dao.getBoardListHb_All();
			act.setOutData("boardHbList", boardHbList);
			//查询南京基地的启票实销数据
			List<Map<String,Object>> boardNjList = dao.getBoardListNj_All();
			act.setOutData("boardNjList", boardNjList);*/
			//String orgId="2010010100070687";
			/*List<Map<String,Object>> boardList = dao.getBoardList_All(year,month,orgId);
			List<Map<String,Object>> boardListDay = dao.getBoardList_Day(year,month,day,orgId);*/
			
			List<Map<String,Object>> newBoardList = dao.newBoardList(year, month, day, orgId) ;
			//查询“月度启票任务”
			String monthBillPlan = dao.getMonthBillPlan(companyId, year, month,orgId);
			//查询“年度启票任务”
			String yearBillPlan = dao.getYearBillPlan(companyId, year,orgId);
			//查询“月度零售任务”
			String monthSalesPlan = dao.getMonthSalesPlan(companyId, year, month,orgId);
			//查询“年度零售任务”
			String yearSalesPlan = dao.getYearSalesPlan(companyId, year,orgId);
			/*act.setOutData("boardList", boardList);
			act.setOutData("boardListDay", boardListDay);*/
			act.setOutData("newBoardList", newBoardList) ;
			act.setOutData("monthBillPlan", monthBillPlan);
			act.setOutData("yearBillPlan", yearBillPlan);
			act.setOutData("monthSalesPlan", monthSalesPlan);
			act.setOutData("yearSalesPlan", yearSalesPlan);
			act.setForword(dayReportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "当日销售看板：主页面展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	public void detailReport(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String flag = CommonUtils.checkNull(request.getParamValue("flag"));
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1; 
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			List seriesList = dao.getSeriesList();
			String orgId=String.valueOf(logonUser.getOrgId());
			TmOrgPO po=new TmOrgPO();
			po.setDutyType(Constant.DUTY_TYPE_COMPANY);
			po.setOrgId(Long.parseLong(orgId));
			if(dao.select(po).size()>0){
				orgId="";
			}
			//String orgId="2010010100070687";
			List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
			if ("3".equals(flag)) {
				detailList = dao.getDetailList(year,month,day,seriesList,orgId);
			}
			if ("2".equals(flag)) {
				detailList = dao.getDetailList(year,month,0,seriesList,orgId);
			}
			if ("1".equals(flag)) {
				detailList = dao.getDetailList(year,0,0,seriesList,orgId);
			}
			act.setOutData("flag", flag);
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			act.setOutData("seriesList", seriesList);
			act.setOutData("detailList", detailList);
			act.setForword(dayReportDetailInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "当日销售看板：明细展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void regionDetailReport(){
		
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String flag = CommonUtils.checkNull(request.getParamValue("flag"));
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1; 
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			List seriesList = dao.getSeriesList();
			List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
			detailList = dao.getRegionDetail(year,0,0,seriesList, flag, orgId);
			act.setOutData("flag", flag);
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			act.setOutData("orgId", orgId);
			act.setOutData("seriesList", seriesList);
			act.setOutData("detailList", detailList);
			act.setForword(regionDetailReportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "当日销售看板：区域明细展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION		:	销售日报表：主页面展示
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2012-02-02
	 */
	public void saleDayReportInit() {
		ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Calendar calendar = Calendar.getInstance();
		String startTime = (calendar.get(Calendar.MONTH)+1) + "月" +  calendar.getMinimum(Calendar.DAY_OF_MONTH) + "日";
		String endTime = (calendar.get(Calendar.MONTH)+1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
		int excluted = calendar.get(Calendar.DAY_OF_MONTH); //已执行天数
		double schedule = (double)calendar.get(Calendar.DAY_OF_MONTH) / calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
		List ps = dao.getSaleOfDailyReportList(areaList);
		act.setOutData("startTime", startTime);
		act.setOutData("endTime", endTime);
		act.setOutData("excluted", excluted);
		act.setOutData("schedule", schedule);
		act.setOutData("areaList", areaList); //业务范围
		act.setOutData("ps", ps);
		act.setForword(saleOfDailyReportInitUrl);
	}
	
	/**
	 * FUNCTION		:	销售日报表：主页面展示
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2012-02-06
	 * */
	public void saleDayReportByParam() {
		ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//参数列表
		
		
		//大区
		String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
		String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
		//省份
		String regionId = CommonUtils.checkNull(request.getParamValue("regionId"));
		String regionCode = CommonUtils.checkNull(request.getParamValue("regionCode"));
		//物料组
		String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
		String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
		//业务范围
		String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
		
		Calendar calendar = Calendar.getInstance();
		String startTime = (calendar.get(Calendar.MONTH)+1) + "月" +  calendar.getMinimum(Calendar.DAY_OF_MONTH) + "日";
		String endTime = (calendar.get(Calendar.MONTH)+1) + "月" + calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + "日";
		int excluted = calendar.get(Calendar.DAY_OF_MONTH); //已执行天数
		double schedule = (double)calendar.get(Calendar.DAY_OF_MONTH) / calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
		List ps = dao.getSaleOfDailyReportList(orgId, regionCode, groupId, areaId, areaList);
		act.setOutData("startTime", startTime);
		act.setOutData("endTime", endTime);
		act.setOutData("excluted", excluted);
		act.setOutData("schedule", schedule);
		act.setOutData("areaList", areaList); //业务范围
		act.setOutData("orgId", orgId);
		act.setOutData("orgCode", orgCode);
		act.setOutData("regionId", regionId);
		act.setOutData("regionCode", regionCode);
		act.setOutData("groupId", groupId);
		act.setOutData("groupCode", groupCode);
		act.setOutData("area_id", areaId);
		act.setOutData("ps", ps);
		act.setForword(saleOfDailyReportInitUrl);
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
