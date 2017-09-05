package com.infodms.dms.actions.report;

import java.io.OutputStream;
import java.util.Calendar;
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
import com.infodms.dms.dao.report.FleetPrepareDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class FleetPrepareReport {
	private Logger logger = Logger.getLogger(FleetPrepareReport.class);
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private AclUserBean logonUser = null;
	private FleetPrepareDao dao = FleetPrepareDao.getInstance();
	
	//需要跳转到的jsp页面的URL
	private  final String FleetPrepare_URL="/jsp/report/totalReport/FleetPrepareReport.jsp";
	public void getFleetPrepareReport(){
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, String> map = new HashMap<String, String>();
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String checkDate1 = CommonUtils.checkNull(request.getParamValue("checkDate1"));
			String checkDate2 = CommonUtils.checkNull(request.getParamValue("checkDate2"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			
			act.setOutData("checkDate1", checkDate1);
			act.setOutData("checkDate2", checkDate2);
			map.put("orgId", orgId);
			map.put("checkDate1", checkDate1);
			map.put("checkDate2", checkDate2);
			map.put("dealerId", dealerId);
			act.setOutData("map", map);
			List<Map<String, Object>> list_FleetPrepareReport = dao.getFleetPrepareSelect(map);
			act.setOutData("list_FleetPrepareReport", list_FleetPrepareReport);
			int len = list_FleetPrepareReport.size();
			int totalAmount = 0;
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			
			for(int i = 0;i<len;i++){
				Object sumData = list_FleetPrepareReport.get(i).get("SERIES_COUNT");
				int SumData = Integer.parseInt(sumData.toString());
				totalAmount +=SumData;
			}
			act.setOutData("totalAmount", totalAmount);
			act.setForword(FleetPrepare_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证处理情况");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void FleetPrepareReportDowLoad(){
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			
			
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String checkDate1 = CommonUtils.checkNull(request.getParamValue("checkDate1"));
			String checkDate2 = CommonUtils.checkNull(request.getParamValue("checkDate2"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			
			
			Map<String, String> map = new HashMap<String, String>();
			
			map.put("orgId", orgId);
			map.put("checkDate1", checkDate1);
			map.put("checkDate2", checkDate2);
			map.put("dealerId", dealerId);
			
			
			

			// 导出的文件名
			String fileName = "集团客户报备.csv";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			List<List<Object>> lists = new LinkedList<List<Object>>();
			List<Object> list = new LinkedList<Object>();
			list.add("组织");
			list.add("省");
			list.add("提报单位");
			list.add("提报日期");
			list.add("客户名称");
			list.add("客户类型");
			list.add("主营业务");
			list.add("邮编");
			list.add("详细地址");
			list.add("主要联系人");
			list.add("职务");
			list.add("电话");
			list.add("车系");
			list.add("数量");
			list.add("备注");
			list.add("确认状态");
			list.add("确认说明");
			list.add("确认人");
			list.add("确认时间");
			lists.add(list);
			
			List<Map<String, Object>> result = dao.getFleetPrepareSelect(map);
			int len = result.size();
			for (int i = 0; i < len; i++) {
				Map<String, Object>  record = result.get(i);
				list = new LinkedList<Object>();
				list.add(CommonUtils.checkNull(record.get("ROOT_ORG_NAME")));
				list.add(CommonUtils.checkNull(record.get("REGION_NAME")));
				list.add(CommonUtils.checkNull(record.get("COMPANY_SHORTNAME")));
				list.add(CommonUtils.checkNull(record.get("SUBMIT_DATE")));
				list.add(CommonUtils.checkNull(record.get("FLEET_NAME")));
				list.add(CommonUtils.checkNull(record.get("FLEET_TYPE")));
				list.add(CommonUtils.checkNull(record.get("MAIN_BUSINESS")));
				list.add(CommonUtils.checkNull(record.get("ZIP_CODE")));
				list.add(CommonUtils.checkNull(record.get("ADDRESS")));
				list.add(CommonUtils.checkNull(record.get("MAIN_LINKMAN")));
				list.add(CommonUtils.checkNull(record.get("MAIN_JOB")));
				list.add(CommonUtils.checkNull(record.get("MAIN_PHONE")));
				list.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
				list.add(CommonUtils.checkNull(record.get("SERIES_COUNT")));
				list.add(CommonUtils.checkNull(record.get("REQ_REMARK")));
				list.add(CommonUtils.checkNull(record.get("STATUS")));
				list.add(CommonUtils.checkNull(record.get("AUDIT_REMARK")));
				list.add(CommonUtils.checkNull(record.get("NAME")));
				list.add(CommonUtils.checkNull(record.get("AUDIT_DATE")));
				lists.add(list);
			}
			OutputStream os = response.getOutputStream();
			CsvWriterUtil.writeCsv(lists, os);
			
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "集团客户报备下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	public void download(){
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String checkDate1 = CommonUtils.checkNull(request.getParamValue("checkDate1"));
			String checkDate2 = CommonUtils.checkNull(request.getParamValue("checkDate2"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			
			
			Map<String, String> map = new HashMap<String, String>();
			
			map.put("orgId", orgId);
			map.put("checkDate1", checkDate1);
			map.put("checkDate2", checkDate2);
			map.put("dealerId", dealerId);
			
			
			

			// 导出的文件名
			String fileName = "集团客户报备.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			List<Map<String, Object>> list = dao.getFleetPrepareSelect(map);
			int len = list.size();
			int y = 0 ;
			long tsc = 0;
			
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("集团客户报备", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			sheet.mergeCells(0, y, 18, y);
			sheet.addCell(new Label(0, y, "集团客户报备",wcf));
			
			++y;
			sheet.mergeCells(0, y, 18, y);
			sheet.addCell(new Label(0, y, "提报日期："+checkDate1+"--"+checkDate2));
			
			++y;
			sheet.addCell(new Label(0, y, "组织"));
			sheet.addCell(new Label(1, y, "省"));
			sheet.addCell(new Label(2, y, "提报单位"));
			sheet.addCell(new Label(3, y, "提报日期"));
			sheet.addCell(new Label(4, y, "客户名称"));
			sheet.addCell(new Label(5, y, "客户类型"));
			sheet.addCell(new Label(6, y, "主营业务"));
			sheet.addCell(new Label(7, y, "邮编"));
			sheet.addCell(new Label(8, y, "详细地址"));
			sheet.addCell(new Label(9, y, "主联系人"));
			sheet.addCell(new Label(10, y, "职务"));
			sheet.addCell(new Label(11, y, "电话"));
			sheet.addCell(new Label(12, y, "车系"));
			sheet.addCell(new Label(13, y, "数量"));
			sheet.addCell(new Label(14, y, "备注"));
			sheet.addCell(new Label(15, y, "确认状态"));
			sheet.addCell(new Label(16, y, "确认说明"));
			sheet.addCell(new Label(17, y, "确认人"));
			sheet.addCell(new Label(18, y, "确认时间"));
			
			if(list!=null){
				long ssc = 0;
				
				long ssc_ron =0 ;
				for (int i = 0; i < len; i++) {
					long sc = Long.parseLong(list.get(i).get("SERIES_COUNT").toString());
					if(i==0){
						ssc = sc;
						tsc = sc;
						ssc_ron = sc;
						++y;
						sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME")==null ?"":list.get(i).get("ROOT_ORG_NAME").toString()));
						sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME")==null ?"":list.get(i).get("REGION_NAME").toString()));
						sheet.addCell(new Label(2, y, list.get(i).get("COMPANY_SHORTNAME")==null ?"":list.get(i).get("COMPANY_SHORTNAME").toString()));
						sheet.addCell(new Label(3, y, list.get(i).get("SUBMIT_DATE")==null ?"":list.get(i).get("SUBMIT_DATE").toString()));
						sheet.addCell(new Label(4, y, list.get(i).get("FLEET_NAME")==null ?"":list.get(i).get("FLEET_NAME").toString()));
						sheet.addCell(new Label(5, y, list.get(i).get("FLEET_TYPE")==null ?"":list.get(i).get("FLEET_TYPE").toString()));
						sheet.addCell(new Label(6, y, list.get(i).get("MAIN_BUSINESS")==null ?"":list.get(i).get("MAIN_BUSINESS").toString()));
						sheet.addCell(new Label(7, y, list.get(i).get("ZIP_CODE")==null ?"":list.get(i).get("ZIP_CODE").toString()));
						sheet.addCell(new Label(8, y, list.get(i).get("ADDRESS")==null ?"":list.get(i).get("ADDRESS").toString()));
						sheet.addCell(new Label(9, y, list.get(i).get("MAIN_LINKMAN")==null ?"":list.get(i).get("MAIN_LINKMAN").toString()));
						sheet.addCell(new Label(10, y, list.get(i).get("MAIN_JOB")==null ?"":list.get(i).get("MAIN_JOB").toString()));
						sheet.addCell(new Label(11, y, list.get(i).get("MAIN_PHONE")==null ?"":list.get(i).get("MAIN_PHONE").toString()));
						sheet.addCell(new Label(12, y, list.get(i).get("GROUP_NAME")==null ?"":list.get(i).get("GROUP_NAME").toString()));
						sheet.addCell(new Label(13, y, list.get(i).get("SERIES_COUNT")==null ?"":list.get(i).get("SERIES_COUNT").toString()));
						sheet.addCell(new Label(14, y, list.get(i).get("REQ_REMARK")==null ?"":list.get(i).get("REQ_REMARK").toString()));
						sheet.addCell(new Label(15, y, list.get(i).get("STATUS")==null ?"":list.get(i).get("STATUS").toString()));
						sheet.addCell(new Label(16, y, list.get(i).get("AUDIT_REMARK")==null ?"":list.get(i).get("AUDIT_REMARK").toString()));
						sheet.addCell(new Label(17, y, list.get(i).get("NAME")==null ?"":list.get(i).get("NAME").toString()));
						sheet.addCell(new Label(18, y, list.get(i).get("AUDIT_DATE")==null ?"":list.get(i).get("AUDIT_DATE").toString()));
						
						
					}else{
						String befor = list.get(i-1).get("REGION_NAME").toString();
						String after = list.get(i).get("REGION_NAME").toString();
						
						String befor11 = list.get(i-1).get("ROOT_ORG_NAME").toString();
						String after12 = list.get(i).get("ROOT_ORG_NAME").toString();
						if(after.equals(befor)&&after12.equals(befor11)){
							ssc += sc;
							ssc_ron += sc ;
							tsc +=sc;
							
							++y;
							sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME")==null ?"":list.get(i).get("ROOT_ORG_NAME").toString()));
							sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME")==null ?"":list.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new Label(2, y, list.get(i).get("COMPANY_SHORTNAME")==null ?"":list.get(i).get("COMPANY_SHORTNAME").toString()));
							sheet.addCell(new Label(3, y, list.get(i).get("SUBMIT_DATE")==null ?"":list.get(i).get("SUBMIT_DATE").toString()));
							sheet.addCell(new Label(4, y, list.get(i).get("FLEET_NAME")==null ?"":list.get(i).get("FLEET_NAME").toString()));
							sheet.addCell(new Label(5, y, list.get(i).get("FLEET_TYPE")==null ?"":list.get(i).get("FLEET_TYPE").toString()));
							sheet.addCell(new Label(6, y, list.get(i).get("MAIN_BUSINESS")==null ?"":list.get(i).get("MAIN_BUSINESS").toString()));
							sheet.addCell(new Label(7, y, list.get(i).get("ZIP_CODE")==null ?"":list.get(i).get("ZIP_CODE").toString()));
							sheet.addCell(new Label(8, y, list.get(i).get("ADDRESS")==null ?"":list.get(i).get("ADDRESS").toString()));
							sheet.addCell(new Label(9, y, list.get(i).get("MAIN_LINKMAN")==null ?"":list.get(i).get("MAIN_LINKMAN").toString()));
							sheet.addCell(new Label(10, y, list.get(i).get("MAIN_JOB")==null ?"":list.get(i).get("MAIN_JOB").toString()));
							sheet.addCell(new Label(11, y, list.get(i).get("MAIN_PHONE")==null ?"":list.get(i).get("MAIN_PHONE").toString()));
							sheet.addCell(new Label(12, y, list.get(i).get("GROUP_NAME")==null ?"":list.get(i).get("GROUP_NAME").toString()));
							sheet.addCell(new Label(13, y, list.get(i).get("SERIES_COUNT")==null ?"":list.get(i).get("SERIES_COUNT").toString()));
							sheet.addCell(new Label(14, y, list.get(i).get("REQ_REMARK")==null ?"":list.get(i).get("REQ_REMARK").toString()));
							sheet.addCell(new Label(15, y, list.get(i).get("STATUS")==null ?"":list.get(i).get("STATUS").toString()));
							sheet.addCell(new Label(16, y, list.get(i).get("AUDIT_REMARK")==null ?"":list.get(i).get("AUDIT_REMARK").toString()));
							sheet.addCell(new Label(17, y, list.get(i).get("NAME")==null ?"":list.get(i).get("NAME").toString()));
							sheet.addCell(new Label(18, y, list.get(i).get("AUDIT_DATE")==null ?"":list.get(i).get("AUDIT_DATE").toString()));
							
							
							
						}else{
							++y;
							sheet.mergeCells(0, y, 2, y);
							sheet.addCell(new Label(0, y, "省份合计",wcf));
							sheet.mergeCells(3, y, 12, y);
							sheet.addCell(new Label(13, y, String.valueOf(ssc)));
							sheet.mergeCells(14, y, 18, y);
							
							ssc = sc;
							tsc +=sc;
							//开始
							String befor_ron = list.get(i-1).get("ROOT_ORG_NAME").toString();
							String after_ron = list.get(i).get("ROOT_ORG_NAME").toString();
							if(after_ron.equals(befor_ron)){
								ssc_ron += sc;
							}else{
								++y;
								sheet.mergeCells(0, y, 2, y);
								sheet.addCell(new Label(0, y, "大区合计",wcf));
								sheet.mergeCells(3, y, 12, y);
								sheet.addCell(new Label(13, y, String.valueOf(ssc_ron)));
								sheet.mergeCells(14, y, 18, y);
								
								ssc_ron = 0;
								ssc_ron += sc;
							}
							//结束
							++y;
							sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME")==null ?"":list.get(i).get("ROOT_ORG_NAME").toString()));
							sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME")==null ?"":list.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new Label(2, y, list.get(i).get("COMPANY_SHORTNAME")==null ?"":list.get(i).get("COMPANY_SHORTNAME").toString()));
							sheet.addCell(new Label(3, y, list.get(i).get("SUBMIT_DATE")==null ?"":list.get(i).get("SUBMIT_DATE").toString()));
							sheet.addCell(new Label(4, y, list.get(i).get("FLEET_NAME")==null ?"":list.get(i).get("FLEET_NAME").toString()));
							sheet.addCell(new Label(5, y, list.get(i).get("FLEET_TYPE")==null ?"":list.get(i).get("FLEET_TYPE").toString()));
							sheet.addCell(new Label(6, y, list.get(i).get("MAIN_BUSINESS")==null ?"":list.get(i).get("MAIN_BUSINESS").toString()));
							sheet.addCell(new Label(7, y, list.get(i).get("ZIP_CODE")==null ?"":list.get(i).get("ZIP_CODE").toString()));
							sheet.addCell(new Label(8, y, list.get(i).get("ADDRESS")==null ?"":list.get(i).get("ADDRESS").toString()));
							sheet.addCell(new Label(9, y, list.get(i).get("MAIN_LINKMAN")==null ?"":list.get(i).get("MAIN_LINKMAN").toString()));
							sheet.addCell(new Label(10, y, list.get(i).get("MAIN_JOB")==null ?"":list.get(i).get("MAIN_JOB").toString()));
							sheet.addCell(new Label(11, y, list.get(i).get("MAIN_PHONE")==null ?"":list.get(i).get("MAIN_PHONE").toString()));
							sheet.addCell(new Label(12, y, list.get(i).get("GROUP_NAME")==null ?"":list.get(i).get("GROUP_NAME").toString()));
							sheet.addCell(new Label(13, y, list.get(i).get("SERIES_COUNT")==null ?"":list.get(i).get("SERIES_COUNT").toString()));
							sheet.addCell(new Label(14, y, list.get(i).get("REQ_REMARK")==null ?"":list.get(i).get("REQ_REMARK").toString()));
							sheet.addCell(new Label(15, y, list.get(i).get("STATUS")==null ?"":list.get(i).get("STATUS").toString()));
							sheet.addCell(new Label(16, y, list.get(i).get("AUDIT_REMARK")==null ?"":list.get(i).get("AUDIT_REMARK").toString()));
							sheet.addCell(new Label(17, y, list.get(i).get("NAME")==null ?"":list.get(i).get("NAME").toString()));
							sheet.addCell(new Label(18, y, list.get(i).get("AUDIT_DATE")==null ?"":list.get(i).get("AUDIT_DATE").toString()));
							
						}
						if(i==len-1){
							++y;
							sheet.mergeCells(0, y, 2, y);
							sheet.addCell(new Label(0, y, "省份合计",wcf));
							sheet.mergeCells(3, y, 12, y);
							sheet.addCell(new Label(13, y, String.valueOf(ssc)));
							sheet.mergeCells(14, y, 18, y);
							
							++y;
							sheet.mergeCells(0, y, 2, y);
							sheet.addCell(new Label(0, y, "大区合计",wcf));
							sheet.mergeCells(3, y, 12, y);
							sheet.addCell(new Label(13, y, String.valueOf(ssc_ron)));
							sheet.mergeCells(14, y, 18, y);
						}
					}
				}
			}
			
			++y;
			sheet.mergeCells(0, y, 2, y);
			sheet.addCell(new Label(0, y, "合计",wcf));
			sheet.mergeCells(3, y, 12, y);
			sheet.addCell(new Label(13, y, String.valueOf(tsc)));
			sheet.mergeCells(14, y, 18, y);
			
			
			workbook.write();
			workbook.close();
			
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "集团客户报备下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
