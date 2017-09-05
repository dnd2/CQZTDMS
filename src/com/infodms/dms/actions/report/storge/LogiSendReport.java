package com.infodms.dms.actions.report.storge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.report.storge.LogiSendQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

/**物流运输报表
 * @author RANJIAN
 * @deprecated 2013-9-9
 */
public class LogiSendReport {
	private Logger logger = Logger.getLogger(LogiSendReport.class) ;
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest() ;
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private LogiSendQueryDao dao = LogiSendQueryDao.getInstance();
	
	private final String LOGI_SEND_QUERY_URL = "/jsp/report/storge/logiSendQuery.jsp";
	private final String LOGI_SEND_INFO = "/jsp/report/storge/logiSendInfo.jsp";
	private final String PRINT_LOGI_SEND_INFO = "/jsp/report/storge/printLogiInfo.jsp";
	/*
	 *物流运输报表查询初始化
	 */
	public void logiSendReportInit(){
		try {
			act.setForword(LOGI_SEND_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物流运输报表初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void logiSendReportInfo(){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			if("".equals(endDate)){//为空默认当前日渐
				endDate = AjaxSelectDao.getInstance().getCurrentServerTime();
			} 
			//map.put("startDate", startDate);
			String year=endDate.substring(0,4);
			map.put("endDate", endDate.substring(0,10));
			List<Map<String, Object>> result = dao.getLogiSendInfo(map);
			List<Map<String, Object>> seriesList =  dao.getSeriesList();
			//List<Map<String, Object>> historyList =  dao.getHistoryList();
			
			int jdzcount=0,jjcount=0,hfcount=0;
			for(int i=0;i<seriesList.size();i++){
				Map<String, Object> seri=seriesList.get(i);
				if(seri.get("AREA_ID").toString().equals(Constant.areaIdJZD)){//景德镇
					jdzcount++;
				}
				if(seri.get("AREA_ID").toString().equals(Constant.areaIdJJ)){//九江
					jjcount++;
				}
				if(seri.get("AREA_ID").toString().equals(Constant.areaIdHF)){//合肥
					hfcount++;
				}
			}
			act.setOutData("result", result);
			act.setOutData("seriesList", seriesList);
			//act.setOutData("historyList", historyList);
			act.setOutData("jdzcount", jdzcount);
			act.setOutData("jjcount", jjcount);
			act.setOutData("hfcount", hfcount);
			act.setOutData("endDate", endDate);
			act.setOutData("year", year);
			act.setForword(LOGI_SEND_INFO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "查看信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void printlogiReportInfo(){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			if("".equals(endDate)){//为空默认当前日渐
				endDate = AjaxSelectDao.getInstance().getCurrentServerTime();
			} 
			String year=endDate.substring(0,4);
			map.put("endDate", endDate.substring(0,10));
			List<Map<String, Object>> result = dao.getLogiSendInfo(map);
			List<Map<String, Object>> seriesList =  dao.getSeriesList();
			//List<Map<String, Object>> historyList =  dao.getHistoryList();
			int jdzcount=0,jjcount=0,hfcount=0;
			for(int i=0;i<seriesList.size();i++){
				Map<String, Object> seri=seriesList.get(i);
				if(seri.get("AREA_ID").toString().equals(Constant.areaIdJZD)){//景德镇
					jdzcount++;
				}
				if(seri.get("AREA_ID").toString().equals(Constant.areaIdJJ)){//九江
					jjcount++;
				}
				if(seri.get("AREA_ID").toString().equals(Constant.areaIdHF)){//合肥
					hfcount++;
				}
			}
			act.setOutData("result", result);
			act.setOutData("seriesList", seriesList);
			//act.setOutData("historyList", historyList);
			act.setOutData("jdzcount", jdzcount);
			act.setOutData("jjcount", jjcount);
			act.setOutData("hfcount", hfcount);
			act.setOutData("endDate", endDate);
			act.setOutData("year", year);
			act.setForword(PRINT_LOGI_SEND_INFO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "导出信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void getSeriesList(){
		try {
			List<Map<String, Object>> list = dao.getSeriesList();
			act.setOutData("list", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车系查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}

