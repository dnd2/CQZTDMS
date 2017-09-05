package com.infodms.dms.actions.report.storge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.storge.NotBoardQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

/**未组板发运统计报表
 * @author RANJIAN
 * @deprecated 2013-9-6
 */
public class NotBoardReport {
	private Logger logger = Logger.getLogger(NotBoardReport.class) ;
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest() ;
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private NotBoardQueryDao dao = NotBoardQueryDao.getInstance();
	
	private final String NOT_BOARD_QUERY_URL = "/jsp/report/storge/notBoardQuery.jsp";
	private final String NOT_BOARD_INFO = "/jsp/report/storge/notBoardInfo.jsp";
	private final String PRINT_BOARD_INFO = "/jsp/report/storge/printBoardInfo.jsp";
	/*
	 *未组板发运统计报表查询初始化
	 */
	public void notBoardReportInit(){
		try {
			act.setForword(NOT_BOARD_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "未组板发运统计报表查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void notBoardReportInfo(){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			//String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			//map.put("startDate", startDate);
			map.put("endDate", endDate);
			List<Map<String, Object>> result = dao.getNotBoardInfo(map);
			List<Map<String, Object>> seriesList =  dao.getSeriesList();
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
			act.setOutData("jdzcount", jdzcount);
			act.setOutData("jjcount", jjcount);
			act.setOutData("hfcount", hfcount);
			//act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setForword(NOT_BOARD_INFO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "查看信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void printBoardReportInfo(){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			//String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			//map.put("startDate", startDate);
			map.put("endDate", endDate);
			List<Map<String, Object>> result = dao.getNotBoardInfo(map);
			List<Map<String, Object>> seriesList =  dao.getSeriesList();
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
			act.setOutData("jdzcount", jdzcount);
			act.setOutData("jjcount", jjcount);
			act.setOutData("hfcount", hfcount);
			//act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setForword(PRINT_BOARD_INFO);
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

