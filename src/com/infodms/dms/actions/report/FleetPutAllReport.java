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
import com.infodms.dms.dao.report.FleetPutAllDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class FleetPutAllReport {
	private Logger logger = Logger.getLogger(FleetPutAllReport.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private final static String FleetPutAll_URL= "/jsp/report/totalReport/FleetPutAll.jsp";
	private FleetPutAllDao dao = FleetPutAllDao.getInstance();
	
	public void getFleetPutAllReport(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String auditBeginTime = CommonUtils.checkNull(request.getParamValue("auditBeginTime"));
			String auditEndTime = CommonUtils.checkNull(request.getParamValue("auditEndTime"));
			String submitBeginTime = CommonUtils.checkNull(request.getParamValue("submitBeginTime"));
			String submitEndTime = CommonUtils.checkNull(request.getParamValue("submitEndTime"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));
			if(orgId == null || "".equals(orgId)) {
				orgId = logonUser.getOrgId().toString();
			}
			
			act.setOutData("auditBeginTime", auditBeginTime);
			act.setOutData("auditEndTime", auditEndTime);
			act.setOutData("submitBeginTime", submitBeginTime);
			act.setOutData("submitEndTime", submitEndTime);
			
			
			map.put("auditBeginTime", auditBeginTime);
			map.put("auditEndTime", auditEndTime);
			map.put("submitBeginTime", submitBeginTime);
			map.put("submitEndTime", submitEndTime);
			map.put("groupCode", groupCode);
			map.put("dealerId", dealerId);
			map.put("orgId", orgId);
			map.put("fleetType", fleetType);
			
			
			List<Map<String, Object>> list_FleetPutAll = dao.getFleetPutAllSelect(map);
			
			Calendar  calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day =calendar.get(Calendar.DAY_OF_MONTH);
			
			act.setOutData("year", year);
			act.setOutData("month", month);
			act.setOutData("day", day);
			
			act.setOutData("list_FleetPutAll", list_FleetPutAll);
			act.setForword(FleetPutAll_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"大客户信息报备汇总表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void FleetPutAllReportDownload(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String auditBeginTime = CommonUtils.checkNull(request.getParamValue("auditBeginTime"));
			String auditEndTime = CommonUtils.checkNull(request.getParamValue("auditEndTime"));
			String submitBeginTime = CommonUtils.checkNull(request.getParamValue("submitBeginTime"));
			String submitEndTime = CommonUtils.checkNull(request.getParamValue("submitEndTime"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));

			map.put("auditBeginTime", auditBeginTime);
			map.put("auditEndTime", auditEndTime);
			map.put("submitBeginTime", submitBeginTime);
			map.put("submitEndTime", submitEndTime);
			map.put("groupCode", groupCode);
			map.put("dealerId", dealerId);
			map.put("orgId", orgId);
			map.put("fleetType", fleetType);
			
			// 导出的文件名
			String fileName = "大客户信息报备汇总表.csv";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			List<List<Object>> lists = new LinkedList<List<Object>>();
			List<Object> list = new LinkedList<Object>();
			
			list.add("大区");
			list.add("省份");
			list.add("服务中心名称");
			list.add("备案时间");
			list.add("审批时间");
			list.add("大客户名称");
			list.add("联系人");
			list.add("电话");
			list.add("客户类型");
			list.add("车系");
			list.add("备案数");
			list.add("合同审批数");
			list.add("享受政策支持");
			list.add("备注");
			lists.add(list);
			
			List<Map<String, Object>> result = dao.getFleetPutAllSelect(map);
			int len = result.size();
			for (int i = 0; i < len; i++) {
				Map<String, Object>  record = result.get(i);
				list = new LinkedList<Object>();
				list.add(CommonUtils.checkNull(record.get("ROOT_ORG_NAME")));
				list.add(CommonUtils.checkNull(record.get("REGION_NAME")));
				list.add(CommonUtils.checkNull(record.get("DEALER_NAME")));
				list.add(CommonUtils.checkNull(record.get("SUBMIT_DATE")));
				list.add(CommonUtils.checkNull(record.get("AUDIT_DATE")));
				list.add(CommonUtils.checkNull(record.get("FLEET_NAME")));
				list.add(CommonUtils.checkNull(record.get("MAIN_LINKMAN")));
				list.add(CommonUtils.checkNull(record.get("MAIN_PHONE")));
				list.add(CommonUtils.checkNull(record.get("FLEET_TYPE")));
				list.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
				list.add(CommonUtils.checkNull(record.get("SERIES_COUNT")));
				list.add(CommonUtils.checkNull(record.get("INTENT_COUNT")));
				list.add(CommonUtils.checkNull(record.get("INTENT_POINT")));
				list.add(CommonUtils.checkNull(record.get("REQ_REMARK")));
				lists.add(list);
			}
			OutputStream os = response.getOutputStream();
			CsvWriterUtil.writeCsv(lists, os);
			
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "大客户信息报备汇总表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void download(){
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String auditBeginTime = CommonUtils.checkNull(request.getParamValue("auditBeginTime"));
			String auditEndTime = CommonUtils.checkNull(request.getParamValue("auditEndTime"));
			String submitBeginTime = CommonUtils.checkNull(request.getParamValue("submitBeginTime"));
			String submitEndTime = CommonUtils.checkNull(request.getParamValue("submitEndTime"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));

			map.put("auditBeginTime", auditBeginTime);
			map.put("auditEndTime", auditEndTime);
			map.put("submitBeginTime", submitBeginTime);
			map.put("submitEndTime", submitEndTime);
			map.put("groupCode", groupCode);
			map.put("dealerId", dealerId);
			map.put("orgId", orgId);
			map.put("fleetType", fleetType);
			
			// 导出的文件名
			String fileName = "大客户信息报备汇总表.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("大客户信息报备汇总表", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			 
			List<Map<String, Object>> list = dao.getFleetPutAllSelect(map);
			int len = list.size();
			int y = 0 ;
			
			long tsc = 0;
			long tic = 0;
			
			sheet.mergeCells(0, y, 13, y);
			sheet.addCell(new jxl.write.Label(0, y, "大客户信息报备汇总表",wcf));
			
			++y;
			sheet.mergeCells(0, y, 13, y);
			sheet.addCell(new jxl.write.Label(0, y, "备案起止时间："+auditBeginTime+"--"+auditEndTime+"     "+"审批起止时间："+submitBeginTime+"--"+submitEndTime));
			
			++y;
			sheet.addCell(new Label(0, y, "大区"));
			sheet.addCell(new Label(1, y, "省份"));
			sheet.addCell(new Label(2, y, "服务中心名称"));
			sheet.addCell(new Label(3, y, "备案时间"));
			sheet.addCell(new Label(4, y, "审批时间"));
			sheet.addCell(new Label(5, y, "大客户名称"));
			sheet.addCell(new Label(6, y, "联系人"));
			sheet.addCell(new Label(7, y, "电话"));
			sheet.addCell(new Label(8, y, "客户类型"));
			sheet.addCell(new Label(9, y, "车系"));
			sheet.addCell(new Label(10, y, "备案数"));
			sheet.addCell(new Label(11, y, "合同审批数"));
			sheet.addCell(new Label(12, y, "享受政策支持"));
			sheet.addCell(new Label(13, y, "备注"));
			if(list!=null){
				long ssc = 0;
				long sic = 0;
				
				long ssc_rn = 0;
				long sic_rn = 0;
				
				
				long ssc_ron = 0;
				long sic_ron = 0;
				for (int i = 0; i < len; i++) {
					long sc = Long.parseLong(list.get(i).get("SERIES_COUNT").toString());
					long ic = Long.parseLong(list.get(i).get("INTENT_COUNT").toString());
					if(i==0){
						ssc = sc;
						sic = ic;
						
						tsc = sc;
						tic = ic;
						
						
						ssc_rn = sc;
						sic_rn = ic;
						
						ssc_ron = sc;
						sic_ron = ic;
						++y;
						sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME")==null ?"":list.get(i).get("ROOT_ORG_NAME").toString()));
						sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME")==null ?"":list.get(i).get("REGION_NAME").toString()));
						sheet.addCell(new Label(2, y, list.get(i).get("DEALER_NAME")==null ?"":list.get(i).get("DEALER_NAME").toString()));
						sheet.addCell(new Label(3, y, list.get(i).get("SUBMIT_DATE")==null ?"":list.get(i).get("SUBMIT_DATE").toString()));
						sheet.addCell(new Label(4, y, list.get(i).get("AUDIT_DATE")==null ?"":list.get(i).get("AUDIT_DATE").toString()));
						sheet.addCell(new Label(5, y, list.get(i).get("FLEET_NAME")==null ?"":list.get(i).get("FLEET_NAME").toString()));
						sheet.addCell(new Label(6, y, list.get(i).get("MAIN_LINKMAN")==null ?"":list.get(i).get("MAIN_LINKMAN").toString()));
						sheet.addCell(new Label(7, y, list.get(i).get("MAIN_PHONE")==null ?"":list.get(i).get("MAIN_PHONE").toString()));
						sheet.addCell(new Label(8, y, list.get(i).get("FLEET_TYPE")==null ?"":list.get(i).get("FLEET_TYPE").toString()));
						sheet.addCell(new Label(9, y, list.get(i).get("GROUP_NAME")==null ?"":list.get(i).get("GROUP_NAME").toString()));
						sheet.addCell(new Label(10, y, Integer.parseInt(list.get(i).get("SERIES_COUNT").toString())==0 ?"":list.get(i).get("SERIES_COUNT").toString()));
						sheet.addCell(new Label(11, y, Integer.parseInt(list.get(i).get("INTENT_COUNT").toString())==0 ?"":list.get(i).get("INTENT_COUNT").toString()));
						sheet.addCell(new Label(12, y, list.get(i).get("INTENT_POINT")==null ?"":list.get(i).get("INTENT_POINT").toString()));
						sheet.addCell(new Label(13, y, list.get(i).get("REQ_REMARK")==null ?"":list.get(i).get("REQ_REMARK").toString()));
					}else{
						String before = list.get(i-1).get("DEALER_NAME").toString();
						String after = list.get(i).get("DEALER_NAME").toString();
						
						String before11 = list.get(i-1).get("REGION_NAME").toString();
						String after12 = list.get(i).get("REGION_NAME").toString();
						
						String before21 = list.get(i-1).get("ROOT_ORG_NAME").toString();
						String after22 = list.get(i).get("ROOT_ORG_NAME").toString();
						if(after.equals(before)&&after12.equals(before11)&&after22.equals(before21)){
							ssc += sc;
							sic += ic;
							
							tsc += sc;
							tic += ic;
							
							
							ssc_rn += sc;
							sic_rn += ic;
							
							ssc_ron += sc;
							sic_ron += ic;
							++y;
							sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME")==null ?"":list.get(i).get("ROOT_ORG_NAME").toString()));
							sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME")==null ?"":list.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new Label(2, y, list.get(i).get("DEALER_NAME")==null ?"":list.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new Label(3, y, list.get(i).get("SUBMIT_DATE")==null ?"":list.get(i).get("SUBMIT_DATE").toString()));
							sheet.addCell(new Label(4, y, list.get(i).get("AUDIT_DATE")==null ?"":list.get(i).get("AUDIT_DATE").toString()));
							sheet.addCell(new Label(5, y, list.get(i).get("FLEET_NAME")==null ?"":list.get(i).get("FLEET_NAME").toString()));
							sheet.addCell(new Label(6, y, list.get(i).get("MAIN_LINKMAN")==null ?"":list.get(i).get("MAIN_LINKMAN").toString()));
							sheet.addCell(new Label(7, y, list.get(i).get("MAIN_PHONE")==null ?"":list.get(i).get("MAIN_PHONE").toString()));
							sheet.addCell(new Label(8, y, list.get(i).get("FLEET_TYPE")==null ?"":list.get(i).get("FLEET_TYPE").toString()));
							sheet.addCell(new Label(9, y, list.get(i).get("GROUP_NAME")==null ?"":list.get(i).get("GROUP_NAME").toString()));
							sheet.addCell(new Label(10, y, Integer.parseInt(list.get(i).get("SERIES_COUNT").toString())==0 ?"":list.get(i).get("SERIES_COUNT").toString()));
							sheet.addCell(new Label(11, y, Integer.parseInt(list.get(i).get("INTENT_COUNT").toString())==0 ?"":list.get(i).get("INTENT_COUNT").toString()));
							sheet.addCell(new Label(12, y, list.get(i).get("INTENT_POINT")==null ?"":list.get(i).get("INTENT_POINT").toString()));
							sheet.addCell(new Label(13, y, list.get(i).get("REQ_REMARK")==null ?"":list.get(i).get("REQ_REMARK").toString()));
						}else{
							++y;
							sheet.mergeCells(0, y, 2, y);
							sheet.addCell(new Label(0, y, "经销商合计",wcf));
							sheet.mergeCells(3, y, 9, y);
							
							if(ssc==0){
								sheet.addCell(new Label(10, y,""));
							}
							else{
								sheet.addCell(new Label(10, y, String.valueOf(ssc)));
							}
							if(sic==0){
								sheet.addCell(new Label(11, y, ""));
							}
							else{
								sheet.addCell(new Label(11, y, String.valueOf(sic)));
							}
							
							
							sheet.mergeCells(12, y, 13, y);
							
							
							
							ssc = sc;
							sic = ic;
							
							tsc += sc;
							tic += ic;
							
							//开始1
							
							String before1 = list.get(i-1).get("REGION_NAME").toString();
							String after1 = list.get(i).get("REGION_NAME").toString();
							
							
							String before31 = list.get(i-1).get("ROOT_ORG_NAME").toString();
							String after32 = list.get(i).get("ROOT_ORG_NAME").toString();
							if(after1.equals(before1)&&after32.equals(before31)){
								ssc_rn += sc;
								sic_rn += ic;
								
								ssc_ron += sc;
								sic_ron += ic;
							}else{
								++y;
								sheet.mergeCells(0, y, 2, y);
								sheet.addCell(new Label(0, y, "省份合计",wcf));
								sheet.mergeCells(3, y, 9, y);
								
								if(ssc_rn==0){
									sheet.addCell(new Label(10, y, ""));
								}
								else{
									sheet.addCell(new Label(10, y, String.valueOf(ssc_rn)));
								}
								if(sic_rn==0){
									sheet.addCell(new Label(11, y, ""));
								}
								else{
									sheet.addCell(new Label(11, y, String.valueOf(sic_rn)));
									
								}
								
								sheet.mergeCells(12, y, 13, y);
								
								
								ssc_rn = 0;
								sic_rn = 0;
								
								ssc_rn += sc;
								sic_rn += ic;
								
								/*ssc_ron += sc;
								sic_ron += ic;*/
								//开始2
								String before2 = list.get(i-1).get("ROOT_ORG_NAME").toString();
								String after2 = list.get(i).get("ROOT_ORG_NAME").toString();
								if(after2.endsWith(before2)){
									ssc_ron += sc;
									sic_ron += ic;
								}else{
									++y;
									sheet.mergeCells(0, y, 2, y);
									sheet.addCell(new Label(0, y, "大区合计",wcf));
									sheet.mergeCells(3, y, 9, y);
									
									if(ssc_ron==0){
										sheet.addCell(new Label(10, y, ""));
									}
									else{
										sheet.addCell(new Label(10, y, String.valueOf(ssc_ron)));
									}
									
									if(sic_ron==0){
										sheet.addCell(new Label(11, y, ""));
									}
									else{
										sheet.addCell(new Label(11, y, String.valueOf(sic_ron)));
									}
									
									sheet.mergeCells(12, y, 13, y);
									
									
									
									ssc_ron = 0;
									sic_ron = 0;
									
									
									ssc_ron += sc;
									sic_ron += ic;
								}
								//结束2
							}
							//结束1
							
							
							++y;
							sheet.addCell(new Label(0, y, list.get(i).get("ROOT_ORG_NAME")==null ?"":list.get(i).get("ROOT_ORG_NAME").toString()));
							sheet.addCell(new Label(1, y, list.get(i).get("REGION_NAME")==null ?"":list.get(i).get("REGION_NAME").toString()));
							sheet.addCell(new Label(2, y, list.get(i).get("DEALER_NAME")==null ?"":list.get(i).get("DEALER_NAME").toString()));
							sheet.addCell(new Label(3, y, list.get(i).get("SUBMIT_DATE")==null ?"":list.get(i).get("SUBMIT_DATE").toString()));
							sheet.addCell(new Label(4, y, list.get(i).get("AUDIT_DATE")==null ?"":list.get(i).get("AUDIT_DATE").toString()));
							sheet.addCell(new Label(5, y, list.get(i).get("FLEET_NAME")==null ?"":list.get(i).get("FLEET_NAME").toString()));
							sheet.addCell(new Label(6, y, list.get(i).get("MAIN_LINKMAN")==null ?"":list.get(i).get("MAIN_LINKMAN").toString()));
							sheet.addCell(new Label(7, y, list.get(i).get("MAIN_PHONE")==null ?"":list.get(i).get("MAIN_PHONE").toString()));
							sheet.addCell(new Label(8, y, list.get(i).get("FLEET_TYPE")==null ?"":list.get(i).get("FLEET_TYPE").toString()));
							sheet.addCell(new Label(9, y, list.get(i).get("GROUP_NAME")==null ?"":list.get(i).get("GROUP_NAME").toString()));
							sheet.addCell(new Label(10, y, Integer.parseInt(list.get(i).get("SERIES_COUNT").toString())==0 ?"":list.get(i).get("SERIES_COUNT").toString()));
							sheet.addCell(new Label(11, y, Integer.parseInt(list.get(i).get("INTENT_COUNT").toString())==0 ?"":list.get(i).get("INTENT_COUNT").toString()));
							sheet.addCell(new Label(12, y, list.get(i).get("INTENT_POINT")==null ?"":list.get(i).get("INTENT_POINT").toString()));
							sheet.addCell(new Label(13, y, list.get(i).get("REQ_REMARK")==null ?"":list.get(i).get("REQ_REMARK").toString()));
						}
						if(i==len-1){
							++y;
							sheet.mergeCells(0, y, 2, y);
							sheet.addCell(new Label(0, y, "经销商合计",wcf));
							sheet.mergeCells(3, y, 9, y);
							
							if(ssc==0){
								sheet.addCell(new Label(10, y,""));
							}
							else{
								sheet.addCell(new Label(10, y, String.valueOf(ssc)));
							}
							if(sic==0){
								sheet.addCell(new Label(11, y, ""));
							}
							else{
								sheet.addCell(new Label(11, y, String.valueOf(sic)));
							}
							sheet.mergeCells(12, y, 13, y);
							
							++y;
							sheet.mergeCells(0, y, 2, y);
							sheet.addCell(new Label(0, y, "省份合计",wcf));
							sheet.mergeCells(3, y, 9, y);
							if(ssc_rn==0){
								sheet.addCell(new Label(10, y, ""));
							}
							else{
								sheet.addCell(new Label(10, y, String.valueOf(ssc_rn)));
							}
							if(sic_rn==0){
								sheet.addCell(new Label(11, y, ""));
							}
							else{
								sheet.addCell(new Label(11, y, String.valueOf(sic_rn)));
								
							}
							
							++y;
							sheet.mergeCells(0, y, 2, y);
							sheet.addCell(new Label(0, y, "大区合计",wcf));
							sheet.mergeCells(3, y, 9, y);
							if(ssc_ron==0){
								sheet.addCell(new Label(10, y, ""));
							}
							else{
								sheet.addCell(new Label(10, y, String.valueOf(ssc_ron)));
							}
							
							if(sic_ron==0){
								sheet.addCell(new Label(11, y, ""));
							}
							else{
								sheet.addCell(new Label(11, y, String.valueOf(sic_ron)));
							}
							sheet.mergeCells(12, y, 13, y);
						}
					}
				}
			}
			
			
			++y;
			sheet.mergeCells(0, y, 2, y);
			sheet.addCell(new Label(0, y, "合计",wcf));
			sheet.mergeCells(3, y, 9, y);
			if(tsc==0){
				sheet.addCell(new Label(10, y, ""));
			}
			else{
				sheet.addCell(new Label(10, y, String.valueOf(tsc)));
			}
			
			if(tic==0){
				sheet.addCell(new Label(11, y, ""));
			}
			else{
				sheet.addCell(new Label(11, y, String.valueOf(tic)));
			}
		
			
			sheet.mergeCells(12, y, 13, y);
			
			
		
			
			workbook.write();
			workbook.close();
			
			os.flush();
			os.close();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "大客户信息报备汇总表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
