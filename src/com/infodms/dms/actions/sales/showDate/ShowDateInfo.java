package com.infodms.dms.actions.sales.showDate;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.storageManage.CheckVehicle;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

public class ShowDateInfo extends BaseDao {

	public Logger logger = Logger.getLogger(CheckVehicle.class);
	private ActionContext act = ActionContext.getContext();
	private static final ShowDateInfo dao = new ShowDateInfo();
	RequestWrapper request = act.getRequest();
	public static final ShowDateInfo getInstance() {
		return dao;
	}
	public void  showDate() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String startYear = CommonUtils.checkNull(request.getParamValue("startYear"));
			String startWeek = CommonUtils.checkNull(request.getParamValue("startWeek"));
			String endYear = CommonUtils.checkNull(request.getParamValue("endYear"));
			String endWeek = CommonUtils.checkNull(request.getParamValue("endWeek"));
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

			Map<String, String> map = new HashMap<String, String>() ;
			map.put("year", startYear) ;
			map.put("week", startWeek) ;
			
			OrderReportDao ord = new OrderReportDao() ;
			Map<String, Object> dateMap = ord.getDayByWeek(map) ;
			
			String date_start = dateMap.get("MINDATE").toString().substring(0, 4) + "/" + dateMap.get("MINDATE").toString().substring(4, 6) + "/" + dateMap.get("MINDATE").toString().substring(6, 8) ;
			String date_end = dateMap.get("MAXDATE").toString().substring(0, 4) + "/" + dateMap.get("MAXDATE").toString().substring(4, 6) + "/" + dateMap.get("MAXDATE").toString().substring(6, 8) ;
			
			if(!CommonUtils.isNullString(endYear)) {
				map.put("year", endYear) ;
				map.put("week", endWeek) ;
				dateMap = ord.getDayByWeek(map) ;
				date_end = dateMap.get("MAXDATE").toString().substring(0, 4) + "/" + dateMap.get("MAXDATE").toString().substring(4, 6) + "/" + dateMap.get("MAXDATE").toString().substring(6, 8) ;
			}
			
			String showDate = "";
			if (null != date_start && !"".equals(date_start) && null != date_end && !"".equals(date_end)) {
				showDate = date_start + "  至  " + date_end;
				act.setOutData("showDate", showDate);
			} else {
				act.setOutData("showDate", "");
			}
			act.setOutData("returnValue", 1);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "根据周度查询时间");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}

