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
import com.infodms.dms.dao.sales.ordermanage.orderquery.MonReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmPoseBusinessAreaPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

public class MonReport extends BaseDao{
	public Logger logger = Logger.getLogger(CheckVehicle.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
//	private static final DayReportDao dao = new DayReportDao ();
	private static final MonReportDao dao = new MonReportDao ();
	public static final MonReportDao getInstance() {
		return dao;
	}

	/** 销售日报表URL */
	private final String  saleOfDailyReportInitUrl = "/jsp/sales/ordermanage/orderquery/saleOfDailyReportInit.jsp";
	/// 月度销售报表
	private final String  MonReportInitUrl = "/jsp/sales/ordermanage/orderquery/MonReportInit.jsp";
	
	
	/**
	 * FUNCTION		:	月度销售报表：主页面展示
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2012-06-07
	 */
	public void MonReportInit(){
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
			
		//	List<Map<String,Object>> newBoardList = dao.newBoardList(year, month, day, orgId) ;
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
			
			//获取最新月份数据”
			String Mon_name = dao.getMonth(companyId, year, month,orgId);
			
			//当前参数月份
			///String Mon_name = CommonUtils.checkNull(request.getParamValue("monname"));
			
			
			///汇总
			List<Map<String,Object>> newmonrep1list = dao.newmonreplist1(year, month, day, orgId,Mon_name) ;
			//逐月启票
			List<Map<String,Object>> newmonrep2list = dao.newmonreplist2(year, month, day, orgId,Mon_name) ;
			//逐月零售
			List<Map<String,Object>> newmonrep3list = dao.newmonreplist3(year, month, day, orgId,Mon_name) ;
			//A网销售，库存，启票
			List<Map<String,Object>> newmonrep4list = dao.newmonreplist4(year, month, day, orgId,Mon_name,1) ;
			//B网销售，库存，启票
			List<Map<String,Object>> newmonrep5list = dao.newmonreplist4(year, month, day, orgId,Mon_name,2) ;
			//华南特区  销售，库存，启票
			List<Map<String,Object>> newmonrep6list = dao.newmonreplist4(year, month, day, orgId,Mon_name,3) ;
			
			//A网启票明细
			List<Map<String,Object>> newmonrep7list = dao.newmonreplist5(year, month, day, orgId,Mon_name,1,1) ;
			//B网启票明细
			List<Map<String,Object>> newmonrep8list = dao.newmonreplist5(year, month, day, orgId,Mon_name,1,2) ;
			//华南特区启票明细
			List<Map<String,Object>> newmonrep9list = dao.newmonreplist5(year, month, day, orgId,Mon_name,1,3) ;
			
			//A网实销明细
			List<Map<String,Object>> newmonrep10list = dao.newmonreplist5(year, month, day, orgId,Mon_name,2,1) ;
			//B网实销明细
			List<Map<String,Object>> newmonrep11list = dao.newmonreplist5(year, month, day, orgId,Mon_name,2,2) ;
			//华南特区实销明细
			List<Map<String,Object>> newmonrep12list = dao.newmonreplist5(year, month, day, orgId,Mon_name,2,3) ;
			
			List<Map<String, Object>> monlist = dao.getmonlist();
			act.setOutData("monlist", monlist); //月报列表
			
			
			act.setOutData("Mon_name", Mon_name) ;	
			act.setOutData("newmonrep1list", newmonrep1list) ;
			act.setOutData("newmonrep2list", newmonrep2list) ;
			act.setOutData("newmonrep3list", newmonrep3list) ;
			act.setOutData("newmonrep4list", newmonrep4list) ;
			act.setOutData("newmonrep5list", newmonrep5list) ;
			act.setOutData("newmonrep6list", newmonrep6list) ;
			act.setOutData("newmonrep7list", newmonrep7list) ;
			
			act.setOutData("newmonrep8list", newmonrep8list) ;
			act.setOutData("newmonrep9list", newmonrep9list) ;
			act.setOutData("newmonrep10list", newmonrep10list) ;
			act.setOutData("newmonrep11list", newmonrep11list) ;
			act.setOutData("newmonrep12list", newmonrep12list) ;
			
						
		//	act.setOutData("newBoardList", newBoardList) ;
			act.setOutData("monthBillPlan", monthBillPlan);
			act.setOutData("yearBillPlan", yearBillPlan);
			act.setOutData("monthSalesPlan", monthSalesPlan);
			act.setOutData("yearSalesPlan", yearSalesPlan);
			act.setForword(MonReportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度销售报表：主页面展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	/**
	 * FUNCTION		:	月度销售报表：主页面展示
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2012-06-07
	 */
	public void MonReportInitPara(){
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
			
			//获取最新月份数据”
			//String Mon_name = dao.getMonth(companyId, year, month,orgId);
			
			//当前参数月份
			String Mon_name = CommonUtils.checkNull(request.getParamValue("monname"));
			
			
			///汇总
			List<Map<String,Object>> newmonrep1list = dao.newmonreplist1(year, month, day, orgId,Mon_name) ;
			//逐月启票
			List<Map<String,Object>> newmonrep2list = dao.newmonreplist2(year, month, day, orgId,Mon_name) ;
			//逐月零售
			List<Map<String,Object>> newmonrep3list = dao.newmonreplist3(year, month, day, orgId,Mon_name) ;
			//A网销售，库存，启票
			List<Map<String,Object>> newmonrep4list = dao.newmonreplist4(year, month, day, orgId,Mon_name,1) ;
			//B网销售，库存，启票
			List<Map<String,Object>> newmonrep5list = dao.newmonreplist4(year, month, day, orgId,Mon_name,2) ;
			//华南特区  销售，库存，启票
			List<Map<String,Object>> newmonrep6list = dao.newmonreplist4(year, month, day, orgId,Mon_name,3) ;
			
			//A网启票明细
			List<Map<String,Object>> newmonrep7list = dao.newmonreplist5(year, month, day, orgId,Mon_name,1,1) ;
			//B网启票明细
			List<Map<String,Object>> newmonrep8list = dao.newmonreplist5(year, month, day, orgId,Mon_name,1,2) ;
			//华南特区启票明细
			List<Map<String,Object>> newmonrep9list = dao.newmonreplist5(year, month, day, orgId,Mon_name,1,3) ;
			
			//A网实销明细
			List<Map<String,Object>> newmonrep10list = dao.newmonreplist5(year, month, day, orgId,Mon_name,2,1) ;
			//B网实销明细
			List<Map<String,Object>> newmonrep11list = dao.newmonreplist5(year, month, day, orgId,Mon_name,2,2) ;
			//华南特区实销明细
			List<Map<String,Object>> newmonrep12list = dao.newmonreplist5(year, month, day, orgId,Mon_name,2,3) ;
			
			List<Map<String, Object>> monlist = dao.getmonlist();
			act.setOutData("monlist", monlist); //月报列表
			
			
			act.setOutData("Mon_name", Mon_name) ;	
			act.setOutData("newmonrep1list", newmonrep1list) ;
			act.setOutData("newmonrep2list", newmonrep2list) ;
			act.setOutData("newmonrep3list", newmonrep3list) ;
			act.setOutData("newmonrep4list", newmonrep4list) ;
			act.setOutData("newmonrep5list", newmonrep5list) ;
			act.setOutData("newmonrep6list", newmonrep6list) ;
			act.setOutData("newmonrep7list", newmonrep7list) ;
			
			act.setOutData("newmonrep8list", newmonrep8list) ;
			act.setOutData("newmonrep9list", newmonrep9list) ;
			act.setOutData("newmonrep10list", newmonrep10list) ;
			act.setOutData("newmonrep11list", newmonrep11list) ;
			act.setOutData("newmonrep12list", newmonrep12list) ;
			
						
			act.setOutData("newBoardList", newBoardList) ;
			act.setOutData("monthBillPlan", monthBillPlan);
			act.setOutData("yearBillPlan", yearBillPlan);
			act.setOutData("monthSalesPlan", monthSalesPlan);
			act.setOutData("yearSalesPlan", yearSalesPlan);
			act.setForword(MonReportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "月度销售报表：主页面展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 			
		
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
