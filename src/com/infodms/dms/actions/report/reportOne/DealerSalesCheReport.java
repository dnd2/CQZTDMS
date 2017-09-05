package com.infodms.dms.actions.report.reportOne;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.report.dealerSalesReportQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**经销商实销报表
 * @author Administrator
 *
 */
public class DealerSalesCheReport {
	private Logger logger = Logger.getLogger(DealerSalesCheReport.class) ;
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest() ;
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private dealerSalesReportQueryDao dao = dealerSalesReportQueryDao.getInstance();
	
	private final String DEALER_SALES_CHECK_QUERY_URL = "/jsp/report/sales/dealerSalesCheReportQuery.jsp";
	private final String DEALER_SALES_CHECK_INFO_URL = "/jsp/report/sales/dealerSalesCheReportInfo.jsp";
	/*
	 * 经销商实销查询报表初始化
	 */
	public void dealerSalesCheReportInit(){
		try {
			/******/
			String endDate = AjaxSelectDao.getInstance().getSimpleCurrentServerTime();
			String startDate = endDate.substring(0,8);
			startDate = startDate + "01";
			act.setOutData("endDate", endDate);
			act.setOutData("startDate", startDate);
			/******/
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			String dutyType = logonUser.getDutyType();
			if(Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType)){
				act.setOutData("command", "3");// 3-车厂用户 4-大区用户
			}
			//如果当前登录用户为大区用户
			else{
				act.setOutData("command", "4");
			}
			act.setOutData("areaList", areaList);
			act.setForword(DEALER_SALES_CHECK_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
		
	   /**
	    * 查询(车厂端)
	    */
		public void getDealerSalesCheckInfo(){
			try {
				Map<String,Object> param = new HashMap<String, Object>();
				String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//开始时间
				String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//结束时间
				String command = CommonUtils.checkNull(request.getParamValue("command"));//判断当前用户信息的标示符
				String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
				if(!"".equals(command) && "3".equals(command)){
					String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));//区域选择
					param.put("orgId", orgId);
				}
				param.put("startDate", startDate);
				param.put("endDate", endDate);
				param.put("command", command);
				param.put("dealerId", dealerId);
				param.put("poseId", logonUser.getPoseId().toString());
				List<Map<String, Object>> result = dao.getDealerSalesCheckInfo(param,logonUser);
				List<Map<String, Object>> seriesList =  dao.getSeriesList();
				act.setOutData("result", result);
				act.setOutData("seriesList", seriesList);
				act.setOutData("size", seriesList.size());
				act.setForword(DEALER_SALES_CHECK_INFO_URL);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商实销");
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
	 * 经销商端下载
	 */
	public void toDealerSalesExcel(){
		try {
			Map<String,Object> param = new HashMap<String, Object>();
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//开始时间
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//结束时间
			String command = CommonUtils.checkNull(request.getParamValue("command"));//判断当前用户信息的标示符
			param.put("startDate", startDate);
			param.put("endDate", endDate);
			param.put("command", command);
			param.put("poseId", logonUser.getPoseId().toString());
			if(!"".equals(command) && "1".equals(command)){//一级经销商
				String sonDealer = CommonUtils.checkNull(request.getParamValue("sonDealer"));//是否包含下级经销商
				param.put("dealerId", logonUser.getDealerId());
				param.put("sonDealer", sonDealer);
			}
			List<Map<String, Object>> result = dao.getDealerSaleInfo(param);
			List<Map<String, Object>> seriesList = dao.getSeriesList();
			String[] excelHead = new String[seriesList.size()+2];
			String[] columns = new String[seriesList.size()+2];
			excelHead[0] = "经销商代码";
			excelHead[1] = "经销商名称";
			columns[0] = "DEALER_CODE";
			columns[1] = "DEALER_NAME";
			for(int i = 0 ; i < seriesList.size() ; i++){
				Map<String, Object> map = seriesList.get(i);
				excelHead[i+2] = (String)map.get("GROUP_NAME");
				columns[i+2] = (String)map.get("GROUP_NAME");
			}
			toReportExcel(act.getResponse(), act.getRequest(), "经销商实销报表.xls", excelHead, columns, result);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "导出");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 车厂端下载
	 */
	public void toOemDealerSalesExcel(){
		try {
			Map<String,Object> param = new HashMap<String, Object>();
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//开始时间
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//结束时间
			String command = CommonUtils.checkNull(request.getParamValue("command"));//判断当前用户信息的标示符
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			if(!"".equals(command) && "3".equals(command)){
				String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));//区域选择
				param.put("orgId", orgId);
			}
			param.put("startDate", startDate);
			param.put("endDate", endDate);
			param.put("command", command);
			param.put("dealerId", dealerId);
			param.put("poseId", logonUser.getPoseId().toString());
			List<Map<String, Object>> result = dao.getOrgDealerSalesInfo(param,logonUser);
			List<Map<String, Object>> seriesList = dao.getSeriesList();
			String[] excelHead = new String[seriesList.size()+3];
			String[] columns = new String[seriesList.size()+3];
			excelHead[0] = "省份";
			excelHead[1] = "经销商代码";
			excelHead[2] = "经销商名称";
			columns[0] = "ORG_NAME";
			columns[1] = "DEALER_CODE";
			columns[2] = "DEALER_NAME";
			for(int i = 0 ; i < seriesList.size() ; i++){
				Map<String, Object> map = seriesList.get(i);
				excelHead[i+3] = (String)map.get("GROUP_NAME");
				columns[i+3] = (String)map.get("GROUP_NAME");
			}
			toReportExcel(act.getResponse(), act.getRequest(), "经销商实销报表.xls", excelHead, columns, result);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "导出");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public static Object toReportExcel(ResponseWrapper response,RequestWrapper request,String excelName,String[] excelHead,String[] columns,List<Map<String,Object>> result) throws Exception{
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
			if (excelHead != null && excelHead.length > 0) {
				for (int i = 0; i < excelHead.length; i++) {
					sheet.addCell(new Label(i, 0, excelHead[i],wcf));
					sheet.setColumnView(i, 10);
				}
			}
			if(result != null && result.size()>0){
				for(int i = 0 ; i < result.size() ; i++){
					Map<String, Object> map = result.get(i);
					for(int j = 0 ; j < excelHead.length ; j++){
						sheet.addCell(new Label(j,i+1,map.get(columns[j])==null?"":map.get(columns[j]).toString(),wcf));
					}	
				}
			}
			
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

