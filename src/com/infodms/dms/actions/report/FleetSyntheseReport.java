package com.infodms.dms.actions.report;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.GetCommonType;
import com.infodms.dms.dao.report.FleetSyntheseReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

/**
 * 集团客户综合查询Action
 * */
public class FleetSyntheseReport {

	private Logger logger = Logger.getLogger(FleetSyntheseReport.class);
	ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act. getResponse();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private FleetSyntheseReportDao dao = new FleetSyntheseReportDao();
	private final String FleetSyntheseReportInitUrl = "/jsp/report/fleetSynthese.jsp";
	private final String FleetSyntheseReportUrl = "/jsp/report/fleetSyntheseReport.jsp";
	
	/**
	 * 集团客户综合查询
	 * 2012-04-09
	 * HXY
	 * */
	public void getFleetSynthese() {
		try {
			act.setForword(FleetSyntheseReportInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户综合查询--查询
	 * 2012-04-09
	 * HXY
	 * */
	public void getFleetSyntheseReport(){
		try {
			//新增报备起止确认时间
			String startTime = CommonUtils.checkNull(request.getParamValue("startTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			//新增省份
			String region = CommonUtils.checkNull(request.getParamValue("region"));
			//新增集团客户名称			
			String fleetName = CommonUtils.checkNull(request.getParamValue("fleetName"));
			//新增经销商
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			//新增区域(分销中心)
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String dutyType = logonUser.getDutyType();
			Map<String, String> map = new HashMap<String, String>();
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			map.put("region", region);
			map.put("fleetName", fleetName);
			map.put("dealerCode", dealerCode);
			map.put("orgCode", orgCode);
			if(dutyType.equals(Constant.DUTY_TYPE_LARGEREGION.toString())) {
				map.put("orgId", logonUser.getOrgId().toString());
			}
			List<Map<String, Object>> reportList = groupQuery(map);
			act.setOutData("list_report", reportList);
			act.setForword(FleetSyntheseReportUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 集团客户综合查询--分步查询将查询结果汇总,解决查询时间慢问题
	 * 2012-04-13
	 * HXY
	 * */
	public List<Map<String, Object>> groupQuery(Map<String, String> map) {
		List<Map<String, Object>> reportListOne = dao.getFleetSyntheseReportPartOne(map);
		List<Map<String, Object>> reportListTwo = dao.getFleetSyntheseReportPartTwo(map);
		LinkedList<Map<String, Object>> reportList = new LinkedList<Map<String, Object>>();
		Map<String, Object> result = null;
		Map<String, Object> one = null;
		Map<String, Object> two = null;
		for(int i=0; i<reportListOne.size(); i++) {
			one = reportListOne.get(i);
			result = one;
			for(int j=0; j<reportListTwo.size(); j++) {
				two = reportListTwo.get(j);
				if(two != null && one != null && two.get("CONTRACT_ID") != null && one.get("CONTRACT_ID") != null && ((BigDecimal)two.get("CONTRACT_ID")).intValue() == ((BigDecimal)one.get("CONTRACT_ID")).intValue()) {
					result.put("ACTCOUNT", two.get("ACTCOUNT"));
				} 
			}
			reportList.addLast(result);
		}
		return reportList;
	}
	
	/**
	 * 集团客户综合查询--下载
	 * */
	public void FleetSyntheseReportdownload(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {

			//新增报备起止确认时间
			String startTime = CommonUtils.checkNull(request.getParamValue("startTime"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			//新增省份
			String region = CommonUtils.checkNull(request.getParamValue("region"));
			//新增集团客户名称			
			String fleetName = CommonUtils.checkNull(request.getParamValue("fleetName"));
			//新增经销商
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			//新增区域(分销中心)
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			map.put("region", region);
			map.put("fleetName", fleetName);
			map.put("dealerCode", dealerCode);
			map.put("orgCode", orgCode);
			
			// 导出的文件名
			String fileName = "集团客户综合查询表.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("集团客户综合查询表", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			List<Map<String, Object>> reportList =  groupQuery(map);

			int len = reportList.size();
			
			int row = 0;
			
			sheet.mergeCells(0, row, 28, row);
			sheet.addCell(new  Label(0, row, "集团客户综合查询",wcf));
			
			
			++row;
			sheet.addCell(new  Label(0, row, "分销中心"));
			sheet.addCell(new  Label(1, row, "省份"));
			sheet.addCell(new  Label(2, row, "提报日期"));
			sheet.addCell(new  Label(3, row, "提报单位"));
			sheet.addCell(new  Label(4, row, "客户名称"));
			sheet.addCell(new  Label(5, row, "客户类型"));
			/*sheet.addCell(new  Label(6, row, "主营业务"));*/
			sheet.addCell(new  Label(6, row, "邮编"));
			/*sheet.addCell(new  Label(8, row, "区域"));*/
			sheet.addCell(new  Label(7, row, "详细地址"));
			sheet.addCell(new  Label(8, row, "主联系人"));
			sheet.addCell(new  Label(9, row, "职务"));
			sheet.addCell(new  Label(10, row, "电话"));
			/*sheet.addCell(new  Label(13, row, "车系"));*/
			sheet.addCell(new  Label(11, row, "报备数量"));
			/*sheet.addCell(new  Label(15, row, "备注"));
			sheet.addCell(new  Label(16, row, "确认状态"));
			sheet.addCell(new  Label(17, row, "确认说明"));*/
			sheet.addCell(new  Label(12, row, "确认人"));
			sheet.addCell(new  Label(13, row, "确认时间"));
			sheet.addCell(new  Label(14, row, "跟进时间"));
			sheet.addCell(new  Label(15, row, "跟进内容"));
			sheet.addCell(new  Label(16, row, "签约日期"));
			sheet.addCell(new  Label(17, row, "有效期起"));
			sheet.addCell(new  Label(18, row, "有效期止"));
			/*sheet.addCell(new  Label(25, row, "签约车系"));*/
			sheet.addCell(new  Label(19, row, "合同数量"));
			sheet.addCell(new  Label(20, row, "实销数量"));
			
			String value = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if(reportList != null && len != 0){
				for(int i=0;i<len;i++){
					++row;
					sheet.addCell(new  Label(0, row, reportList.get(i).get("ROOT_ORG_NAME")==null?"":reportList.get(i).get("ROOT_ORG_NAME").toString()));
					sheet.addCell(new  Label(1, row, reportList.get(i).get("PROVINCE")==null?"":reportList.get(i).get("PROVINCE").toString()));
					sheet.addCell(new  Label(2, row, reportList.get(i).get("SUBMIT_DATE")==null?"": dateFormat.format(reportList.get(i).get("SUBMIT_DATE"))));
					sheet.addCell(new  Label(3, row, reportList.get(i).get("DEALER_NAME")==null?"":reportList.get(i).get("DEALER_NAME").toString()));
					sheet.addCell(new  Label(4, row, reportList.get(i).get("FLEET_NAME")==null?"":reportList.get(i).get("FLEET_NAME").toString()));
					sheet.addCell(new  Label(5, row, reportList.get(i).get("FLEET_TYPE")==null?"":reportList.get(i).get("FLEET_TYPE").toString()));
					/*BigDecimal mainBusiness = (BigDecimal)reportList.get(i).get("MAIN_BUSINESS");
					value = mainBusiness == null ? "" : GetCommonType.getMyCommonType(mainBusiness.toString());
					sheet.addCell(new  Label(6, row, value));*/
					sheet.addCell(new  Label(6, row, reportList.get(i).get("ZIP_CODE")==null?"":reportList.get(i).get("ZIP_CODE").toString()));
					/*sheet.addCell(new  Label(8, row, reportList.get(i).get("REGION_NAME")==null?"":reportList.get(i).get("REGION_NAME").toString()));*/
					sheet.addCell(new  Label(7, row, reportList.get(i).get("ADDRESS")==null?"":reportList.get(i).get("ADDRESS").toString()));
					sheet.addCell(new  Label(8, row, reportList.get(i).get("MAIN_LINKMAN")==null?"":reportList.get(i).get("MAIN_LINKMAN").toString()));
					sheet.addCell(new  Label(9, row, reportList.get(i).get("MAIN_JOB")==null?"":reportList.get(i).get("MAIN_JOB").toString()));
					sheet.addCell(new  Label(10, row, reportList.get(i).get("MAIN_PHONE")==null?"":reportList.get(i).get("MAIN_PHONE").toString()));
					/*BigDecimal isAll = (BigDecimal)reportList.get(i).get("SERIES_ID");
					if(isAll != null && isAll.intValue() == -1) {
						value = "全系";
					} else {
						value = (String)reportList.get(i).get("CARS");
					}
					sheet.addCell(new  Label(13, row, value == null ? "" : value));*/
					sheet.addCell(new  Label(11, row, reportList.get(i).get("SERIES_COUNT")==null?"":reportList.get(i).get("SERIES_COUNT").toString()));
					/*sheet.addCell(new  Label(15, row, reportList.get(i).get("FOLLOW_REMARK")==null?"":reportList.get(i).get("FOLLOW_REMARK").toString()));*/
					/*BigDecimal status = (BigDecimal)reportList.get(i).get("STATUS");
					value = status == null ? "" : GetCommonType.getMyCommonType(String.valueOf(status));
					sheet.addCell(new  Label(16, row, value));*/
					/*sheet.addCell(new  Label(17, row, reportList.get(i).get("REQ_REMARK")==null?"":reportList.get(i).get("REQ_REMARK").toString()));*/
					sheet.addCell(new  Label(12, row, reportList.get(i).get("NAME")==null?"":reportList.get(i).get("NAME").toString()));
					sheet.addCell(new  Label(13, row, reportList.get(i).get("AUDIT_DATE")==null?"": dateFormat.format(reportList.get(i).get("AUDIT_DATE"))));
					sheet.addCell(new  Label(14, row, reportList.get(i).get("FOLLOW_DATE")==null?"": dateFormat.format(reportList.get(i).get("FOLLOW_DATE"))));
					sheet.addCell(new  Label(15, row, reportList.get(i).get("FOLLOW_REMARK")==null?"":reportList.get(i).get("FOLLOW_REMARK").toString()));
					sheet.addCell(new  Label(16, row, reportList.get(i).get("CHECK_DATE")==null?"": dateFormat.format(reportList.get(i).get("CHECK_DATE"))));
					sheet.addCell(new  Label(17, row, reportList.get(i).get("START_DATE")==null?"": dateFormat.format(reportList.get(i).get("START_DATE"))));
					sheet.addCell(new  Label(18, row, reportList.get(i).get("END_DATE")==null?"": dateFormat.format(reportList.get(i).get("END_DATE"))));
					/*isAll = (BigDecimal)reportList.get(i).get("SERIES_ID");
					if(isAll != null && isAll.intValue() == -1) {
						value = "全系";
					} else {
						value = (String)reportList.get(i).get("GROUP_NAME");
					}
					sheet.addCell(new  Label(25, row, value == null ? "" : value));*/
					sheet.addCell(new  Label(19, row, reportList.get(i).get("INTENT_COUNT")==null?"":reportList.get(i).get("INTENT_COUNT").toString()));
					sheet.addCell(new  Label(20, row, reportList.get(i).get("ACTCOUNT")==null?"":reportList.get(i).get("ACTCOUNT").toString()));
				}		
			}			
			
			workbook.write();
			workbook.close();
			os.flush();
			os.close();
			
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户综合查询下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
