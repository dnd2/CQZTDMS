package com.infodms.dms.actions.sales.ordermanage.orderquery;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateOfJob;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("rawtypes")
public class OrderReport extends BaseDao {

	public Logger logger = Logger.getLogger(OrderReport.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final OrderReportDao dao = new OrderReportDao();
	private byte status;
	public static final OrderReportDao getInstance() {
		return dao;
	}
	
	private final String  orderReportInitUrl = "/jsp/sales/ordermanage/orderquery/orderReportInit.jsp";
	/*
	private final String  orderReportDetailInitUrl = "/jsp/sales/ordermanage/orderquery/orderReportDetailInit.jsp";
	private final String  regionDetailOrderReportInitUrl = "/jsp/sales/ordermanage/orderquery/regionDetailOrderReportInit.jsp";
	private final String  DealerDetailOrderReportInitUrl = "/jsp/sales/ordermanage/orderquery/regionDetailOrderReportInit.jsp";
	*/
	
	/**
	 * 订单监控看板初始化
	 * */
	public void orderReportInit(){
		AclUserBean logonUser = null;
		status = 0;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			long orgId = logonUser.getOrgId();
			int dutyType = Integer.parseInt(logonUser.getDutyType());
			Calendar calendar = Calendar.getInstance();
			//当月
			int currentYear = calendar.get(Calendar.YEAR);
			int currentMonth = calendar.get(Calendar.MONTH) + 1;
			//上月
			calendar.add(Calendar.MONTH, -1);
			int lastMonth = calendar.get(Calendar.MONTH) + 1;
			int lastYear = calendar.get(Calendar.YEAR);
			//下月
			calendar.add(Calendar.MONTH, +2);
			int nextMonth = calendar.get(Calendar.MONTH) + 1;
			int nextYear = calendar.get(Calendar.YEAR);
			
			DateOfJob doj = new DateOfJob() ;
			String currentDate = doj.getMonthDayEndByWeek(lastYear, lastMonth, 0, 1) + " - " + doj.getMonthDayEndByWeek(currentYear, currentMonth, 0, 0) ;//当月日期
			String lastDate = doj.getMonthDayBeforeByWeek(lastYear, lastMonth, 0) + " - " + doj.getMonthDayEndByWeek(lastYear, lastMonth, 0, 0) ;//上月日期
			String nextDate = doj.getMonthDayEndByWeek(currentYear, currentMonth, 0, 1) + " - " + doj.getMonthDayEndByWeek(nextYear, nextMonth, 0, 0) ;//下月日期
			
			act.setOutData("currentDate", currentDate) ;
			act.setOutData("lastDate", lastDate) ;
			act.setOutData("nextDate", nextDate) ;
			
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areas = acc.getAreaStr(areaList) ;

//			List<Map<String,Object>> orderBoardList = dao.getOrderBoard(currentYear, currentMonth, orgId, dutyType, areas) ;
//			
//			//求A网和B网的个数
//			List<Map<String,Object>> areaCountList = dao.getAreaCount(currentYear, currentMonth, orgId, dutyType, areas);
			
//			act.setOutData("orderBoardList", orderBoardList);
//			act.setOutData("areaCountList", areaCountList);
			act.setOutData("currentMonth", currentMonth); 
			act.setOutData("currentYear", currentYear);
			act.setOutData("lastMonth", lastMonth);
			act.setOutData("lastYear", lastYear);
			act.setOutData("nextYear", nextYear);
			act.setOutData("nextMonth", nextMonth);
			act.setOutData("monthStatus", status);
			act.setForword(orderReportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单监控看板：主页面展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	/**
	 * 选择月份后重新查询订单监控看板
	 * */
	public void orderReportByMonth() {
		ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
		String selectedMonth = CommonUtils.checkNull(atx.getRequest().getParamValue("month"));
		int month = -1;
		if(!"".equals(month)) {
			month = Integer.parseInt(selectedMonth);
		}
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			long orgId = logonUser.getOrgId();
			int dutyType = Integer.parseInt(logonUser.getDutyType());
			Calendar calendar = Calendar.getInstance();
			//当月
			int currentYear = calendar.get(Calendar.YEAR);
			int currentMonth = calendar.get(Calendar.MONTH) + 1;
			//上月
			calendar.add(Calendar.MONTH, -1);
			int lastMonth = calendar.get(Calendar.MONTH) + 1;
			int lastYear = calendar.get(Calendar.YEAR);
			//下月
			calendar.add(Calendar.MONTH, +2);
			int nextMonth = calendar.get(Calendar.MONTH) + 1;
			int nextYear = calendar.get(Calendar.YEAR);
			
			DateOfJob doj = new DateOfJob() ;
			String currentDate = doj.getMonthDayEndByWeek(lastYear, lastMonth, 0, 1) + " - " + doj.getMonthDayEndByWeek(currentYear, currentMonth, 0, 0) ;//当月日期
			String lastDate = doj.getMonthDayBeforeByWeek(lastYear, lastMonth, 0) + " - " + doj.getMonthDayEndByWeek(lastYear, lastMonth, 0, 0) ;//上月日期
			String nextDate = doj.getMonthDayEndByWeek(currentYear, currentMonth, 0, 1) + " - " + doj.getMonthDayEndByWeek(nextYear, nextMonth, 0, 0) ;//下月日期
			
			act.setOutData("currentDate", currentDate) ;
			act.setOutData("lastDate", lastDate) ;
			act.setOutData("nextDate", nextDate) ;
			
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			
//			ActivitiesCamCost acc = new ActivitiesCamCost() ;
//			String areas = acc.getAreaStr(areaList) ;
			
			List<Map<String,Object>> orderBoardList = null;
			
			//求A网和B网的个数
			List<Map<String,Object>> areaCountList = null;
			
			
			//获取状态(判断选择的是下月或上月)
			if(month > currentMonth) {
				if(currentYear > lastYear && month == 12) {
					//跨年(上月) 例如:12 1 2
					status = -1;
//					orderBoardList = dao.getOrderBoard(lastYear, month, orgId, dutyType, areas);
//					areaCountList = dao.getAreaCount(lastYear, month, orgId, dutyType, areas);
				} else  {
					//下月
					status = 1;
//					orderBoardList = dao.getOrderBoard(currentYear, month, orgId, dutyType, areas);
//					areaCountList = dao.getAreaCount(currentYear, month, orgId, dutyType, areas);
				} 
			} else if(month < currentMonth) {
				 if(currentYear < nextYear && month == 1){
					//跨年(下月) 例如:11 12 1
					status = 1;
//					orderBoardList = dao.getOrderBoard(nextYear, month, orgId, dutyType, areas);
//					areaCountList = dao.getAreaCount(nextYear, month, orgId, dutyType, areas);
				} else {
					//上月
					status = -1;
//					orderBoardList = dao.getOrderBoard(currentYear, month, orgId, dutyType, areas);
//					areaCountList = dao.getAreaCount(currentYear, month, orgId, dutyType, areas);
				}
			} else {
//				orderBoardList = dao.getOrderBoard(currentYear, month, orgId, dutyType, areas);
//				areaCountList = dao.getAreaCount(currentYear, month, orgId, dutyType, areas);
			}
			act.setOutData("orderBoardList", orderBoardList);
			act.setOutData("areaCountList", areaCountList);
			act.setOutData("monthStatus", status);
			act.setOutData("currentYear", currentYear);
			act.setOutData("currentMonth", currentMonth);
			act.setOutData("lastMonth", lastMonth);
			act.setOutData("lastYear", lastYear);
			act.setOutData("nextMonth", nextMonth);
			act.setOutData("nextYear", nextYear);
			act.setForword(orderReportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单监控看板：主页面展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
