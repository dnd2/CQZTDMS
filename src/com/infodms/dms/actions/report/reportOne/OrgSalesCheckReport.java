package com.infodms.dms.actions.report.reportOne;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.report.orgSalesCheckReportQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

/**各大区实销开票报表
 * @author Administrator
 *
 */
public class OrgSalesCheckReport {
	private Logger logger = Logger.getLogger(OrgSalesCheckReport.class) ;
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest() ;
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private orgSalesCheckReportQueryDao dao = orgSalesCheckReportQueryDao.getInstance();
	
	private final String ORG_SALES_CHECK_QUERY = "/jsp/report/sales/orgSalesCheckReportQuery.jsp";
	private final String ORG_SALES_CHECK_INFO = "/jsp/report/sales/orgSalesCheckReportInfo.jsp";
	/*
	 *各大区实销开票报表查询初始化
	 */
	public void orgSalesCheckReportInit(){
		try {
			String endDate = AjaxSelectDao.getInstance().getSimpleCurrentServerTime();
			Calendar c = Calendar.getInstance();
			String startDate = endDate.substring(0, 8);
		    startDate = startDate+"01";
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setForword(ORG_SALES_CHECK_QUERY);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getOrgSalesCheckReportInfo(){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			List<Map<String, Object>> result = dao.getDealerCheckInfo(map);
			List<Map<String, Object>> seriesList =  dao.getSeriesList();
			act.setOutData("result", result);
			act.setOutData("seriesList", seriesList);
			act.setOutData("size", seriesList.size());
			act.setForword(ORG_SALES_CHECK_INFO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "大区开票实销信息");
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
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车系");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 导出
	 */
	public void toExcel(){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			List<Map<String, Object>> result = dao.getDealerCheckInfo(map);
			List<Map<String, Object>> seriesList =  dao.getSeriesList();
			toReportExcel(act.getResponse(), request, "大区开票实销统计.xls", result, seriesList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "导出");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public Object toReportExcel(ResponseWrapper response,RequestWrapper request,String excelName,List<Map<String,Object>> result,List<Map<String,Object>> seriesList) throws Exception{
		jxl.write.WritableWorkbook wwkb = null;
		OutputStream out = null;
		try {	
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(excelName,"utf-8"));
			out = response.getOutputStream();
			wwkb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet sheet = wwkb.createSheet(excelName,0);
			//居中，size：10
			WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setAlignment(Alignment.CENTRE);
			int num = seriesList.size();
			sheet.mergeCells(0, 0, num*2+1, 0);
			sheet.addCell(new Label(0,0,"各大区开票实销统计表",wcf));
			sheet.mergeCells(0, 1, 0, 2);
			sheet.mergeCells(1, 1, num, 1);
			sheet.mergeCells(num+1, 1, 2*num, 1);
			sheet.addCell(new Label(0,1,"大区",wcf));
			sheet.addCell(new Label(1,1,"开票",wcf));
			sheet.addCell(new Label(num+1,1,"实销",wcf));
			int count = 1;
			for(int i = 0 ; i < seriesList.size(); i++){
				Map<String, Object> map = seriesList.get(i);
				String groupName = map.get("GROUP_NAME").toString();
				sheet.addCell(new Label(count,2,groupName,wcf));
				count++;
			}
			for(int i = 0 ; i < seriesList.size(); i++){
				Map<String, Object> map = seriesList.get(i);
				String groupName = map.get("GROUP_NAME").toString();
				sheet.addCell(new Label(count,2,groupName,wcf));
				count++;
			}
			int count_1 = 3 ;
			for(int i = 0 ; i < result.size(); i++){
				Map<String, Object> map = result.get(i);
				int count_2 = 1;
				sheet.addCell(new Label(0,count_1,map.get("ROOT_ORG_NAME_2").toString(),wcf));
				for(int j = 0 ; j < seriesList.size() ; j++){
					Map<String, Object> map_1 = seriesList.get(j);
					String groupName = map_1.get("GROUP_NAME").toString();
					sheet.addCell(new Label(count_2,count_1,map.get(groupName+"1")==null?"0":map.get(groupName+"1").toString(),wcf));
					count_2 ++;
				}
				for(int j = 0 ; j < seriesList.size() ; j++){
					Map<String, Object> map_1 = seriesList.get(j);
					String groupName = map_1.get("GROUP_NAME").toString();
					sheet.addCell(new Label(count_2,count_1,map.get(groupName+"2")==null?"0":map.get(groupName+"2").toString(),wcf));
					count_2 ++;
				}
				count_1 ++;
			}
//			if(result != null && result.size()>0){
//				for(int i = 0 ; i < result.size() ; i++){
//					Map<String, Object> map = result.get(i);
//					for(int j = 0 ; j < excelHead.length ; j++){
//						sheet.addCell(new Label(j,i+1,map.get(columns[j])==null?"":map.get(columns[j]).toString(),wcf));
//					}	
//				}
//			}
			
			wwkb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if (null != wwkb) {
				wwkb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
}

