package com.infodms.dms.actions.sales.ordermanage.orderquery;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.storageManage.CheckVehicle;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.DayDealerReportDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.DayReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

public class DayDealerReport  extends BaseDao{
	public Logger logger = Logger.getLogger(CheckVehicle.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final DayDealerReportDao dao = new DayDealerReportDao ();
	public static final DayDealerReportDao getInstance() {
		return dao;
	}
	private final String  dayReportInitUrl = "/jsp/sales/ordermanage/orderquery/dayDealerReportInit.jsp";
	private final String  regionDealerDetailReportInitUrl = "/jsp/sales/ordermanage/orderquery/regionDealerDetailReportInit.jsp";
	public void dayDealerReportInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1; 
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			//String orgId=String.valueOf(logonUser.getOrgId());
			//String orgId="2010010100070687";
			String dealerId=logonUser.getDealerId();
			List<Map<String,Object>> boardList = dao.getBoardList_All(year,month,dealerId);
			List<Map<String,Object>> boardListDay = dao.getBoardList_Day(year,month,day,dealerId);
			//查询“月度启票任务”
			String monthBillPlan = dao.getMonthBillPlan(companyId, year, month,dealerId);
			//查询“年度启票任务”
			String yearBillPlan = dao.getYearBillPlan(companyId, year,dealerId);
			//查询“月度零售任务”
			String monthSalesPlan = dao.getMonthSalesPlan(companyId, year, month,dealerId);
			//查询“年度零售任务”
			String yearSalesPlan = dao.getYearSalesPlan(companyId, year,dealerId);
			act.setOutData("boardList", boardList);
			act.setOutData("boardListDay", boardListDay);
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
public void regionDealerDetailReport(){
		
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			//String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String orgId=String.valueOf(logonUser.getOrgId());
			String dealerId=logonUser.getDealerId();
			String flag = CommonUtils.checkNull(request.getParamValue("flag"));
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1; 
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			List seriesList = dao.getSeriesList();
			List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
			detailList = dao.getRegionDetail(year,0,0,seriesList, flag, dealerId);
			act.setOutData("flag", flag);
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			act.setOutData("orgId", orgId);
			act.setOutData("seriesList", seriesList);
			act.setOutData("detailList", detailList);
			act.setForword(regionDealerDetailReportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "当日销售看板：区域明细展示");
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
