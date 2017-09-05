package com.infodms.dms.actions.report.jcafterservicereport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.report.jcafterservicereport.ServiceCenterMonthlyReportDao;
import com.infodms.dms.dao.report.jcafterservicereport.SpecialCostReportDao;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class ThreeGuaranteesAuditReport {
	private Logger logger = Logger.getLogger(ThreeGuaranteesAuditReport.class);
	//jsp\report\jcafterservicereport\threeGuaranteesAuditReport.jsp
	 private final String THREE_AUDIT = "/jsp/report/jcafterservicereport/threeGuaranteesAuditReport.jsp" ;//配件三包判定页面
/**
 * 三包审核统计费用明细报表
 */
	public void threeGuaranteesAuditReportInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
	      List<Map<String, Object>> areaList = dao.getAreaList();//区域
	      act.setOutData("areaList", areaList);
		act.setForword(THREE_AUDIT);
	}
	
	public void threeGuaranteesAuditReportView(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
        String beginTime = request.getParamValue("beginTime");		
        String endTime = request.getParamValue("endTime");	
        String audit_beginTime = request.getParamValue("audit_beginTime");		
        String audit_endTime = request.getParamValue("audit_endTime");	
        String dealerCode = request.getParamValue("dealerCode");
        String areaName = request.getParamValue("areaName");
        act.setOutData("beginTime", beginTime);
        act.setOutData("endTime", endTime);
        act.setOutData("audit_beginTime", audit_beginTime);
        act.setOutData("audit_endTime", audit_endTime);
        act.setOutData("dealerCode", dealerCode);
        act.setOutData("areaName", areaName);
		ServiceCenterMonthlyReportDao dao = new ServiceCenterMonthlyReportDao();
		List<Map<String,Object>>  list = dao.threeGuaranteesAuditReportView( beginTime, endTime, audit_beginTime, audit_endTime, dealerCode, areaName);
		List<Map<String,Object>>  listTotal = dao.totalThreeGuaranteesAuditReportView(beginTime, endTime, audit_beginTime, audit_endTime, dealerCode, areaName);
		act.setOutData("list", list);
		act.setOutData("listTotal", listTotal.get(0));
		act.setForword(THREE_AUDIT);
	}
}
