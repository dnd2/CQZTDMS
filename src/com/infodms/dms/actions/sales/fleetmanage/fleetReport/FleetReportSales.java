package com.infodms.dms.actions.sales.fleetmanage.fleetReport;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;

public class FleetReportSales {
	public Logger logger = Logger.getLogger(FleetReportSales.class);   
	private final String initPutUrl="/jsp/report/fleetPutReport.jsp";
	private final String initSalesUrl="/jsp/report/fleetSalesReport.jsp";
	
	
	public void initPutReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	try{
		Date date_ = new Date(System.currentTimeMillis());
		Date date1 = new Date(date_.getYear(),date_.getMonth(),01);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(date_);
		String date2 = dateFormat.format(date1);
		act.setOutData("date", date);
		act.setOutData("date2", date2);
		act.setForword(initPutUrl);
	}catch(Exception e) {//异常方法
		BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"大客户提报报表有误！");
		logger.error(logonUser,e1);
		act.setException(e1);
	}
	}
	
	public void initSalesReport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	try{
		Date date_ = new Date(System.currentTimeMillis());
		Date date1 = new Date(date_.getYear(),date_.getMonth(),01);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(date_);
		String date2 = dateFormat.format(date1);
		act.setOutData("date", date);
		act.setOutData("date2", date2);
		act.setForword(initSalesUrl);
	}catch(Exception e) {//异常方法
		BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"大客户实销报表有误！");
		logger.error(logonUser,e1);
		act.setException(e1);
	}
	}
	
	
}
